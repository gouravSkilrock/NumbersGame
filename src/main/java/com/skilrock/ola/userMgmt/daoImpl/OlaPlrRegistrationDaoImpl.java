package com.skilrock.ola.userMgmt.daoImpl;

import java.sql.Connection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import sun.net.www.content.text.plain;

import com.mysql.jdbc.Statement;
import com.skilrock.lms.common.db.DBConnect;
import com.skilrock.lms.common.exception.GenericException;
import com.skilrock.lms.common.exception.LMSErrors;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.common.utility.MD5Encoder;
import com.skilrock.lms.web.drawGames.common.Util;
import com.skilrock.ola.common.OLAUtility;
import com.skilrock.ola.javaBeans.CityDataBean;
import com.skilrock.ola.javaBeans.CountryDataBean;
import com.skilrock.ola.javaBeans.StateDataBean;
import com.skilrock.ola.userMgmt.javaBeans.OlaPlayerRegistrationRequestBean;

public class OlaPlrRegistrationDaoImpl  {
	
	static Log logger = LogFactory.getLog(OlaPlrRegistrationDaoImpl.class);
	
	public 	void  registerPlayer(OlaPlayerRegistrationRequestBean playerBean,Connection con) throws LMSException, GenericException{
		ResultSet rs=null;
		try{
			
				// save Player Details
				String insQry = "insert into st_ola_player_master(username,wallet_id,account_id,fname,lname,gender,date_of_birth,password,email,phone, address, city, state, country, status, registration_date, registration_type) values (?, ?,?,?, ?, ?, ?, ?, ?, ?, ?, ?, ?,?,?,?,?)";
				PreparedStatement statement = con.prepareStatement(insQry);
				statement.setString(1, (playerBean.getUsername()==null||playerBean.getUsername().isEmpty())?playerBean.getPhone():playerBean.getUsername());
				statement.setInt(2, playerBean.getWalletId());
				statement.setString(3, playerBean.getAccountId());
				statement.setString(4, playerBean.getFirstName());
				statement.setString(5, playerBean.getLastName());
				if(playerBean.getGender()==null){
					statement.setString(6, null);
				}
				else if(playerBean.getGender().equalsIgnoreCase("male") || playerBean.getGender().equalsIgnoreCase("m")){
					statement.setString(6, "M");
				}else{
					statement.setString(6, "F");
				}
			    if(playerBean.getDateOfBirth() == null || playerBean.getDateOfBirth().trim().isEmpty()){
			    	statement.setString(7, "0000-00-00");
			    }else{
			    	statement.setString(7, playerBean.getDateOfBirth());
			    }
				
			
				statement.setString(8, playerBean.getPassword()==null?"":MD5Encoder.encode(playerBean.getPassword()));
				if(playerBean.getEmail()==null){
					statement.setString(9, "");
				}
				else{
					statement.setString(9, playerBean.getEmail());
				}
				statement.setString(10, playerBean.getPhone());
				statement.setString(11, playerBean.getAddress());
				if(playerBean.getCity()==null || "-1".equals(playerBean.getCity())){
					statement.setString(12, "");
				}
				else{
					statement.setString(12, playerBean.getCity());
				}
				if( playerBean.getState()==null || "-1".equals(playerBean.getState())){
					statement.setString(13, "");
				}
				else{
					statement.setString(13, playerBean.getState());
				}
				statement.setString(14, playerBean.getCountry());
				statement.setString(15, "ACTIVE");
				if("DIRECT".equalsIgnoreCase(playerBean.getRegType())){
					statement.setString(16, playerBean.getPlrRegDate());
				}else{
					playerBean.setPlrRegDate(Util.getCurrentTimeString());
					statement.setTimestamp(16, Util.getCurrentTimeStamp());
				}
				
				statement.setString(17, playerBean.getRegType());
				logger.info("Insert in Player master :"+statement);
				int isUpdate =statement.executeUpdate();
				 rs = statement.getGeneratedKeys();
				 rs.next();
				 playerBean.setPlayerId(rs.getInt(1));
				if(isUpdate!=1){
					throw new LMSException(12);
				}
				if("ANONYMOUS".equalsIgnoreCase(playerBean.getRegFieldType())){
					isUpdate=0;
					statement.clearParameters();
					
					Calendar cal = Calendar.getInstance();
					cal.setTime(new Date());
					int DDD = cal.get(Calendar.DAY_OF_YEAR);
					String userName = (DDD/100)!=0?DDD+"":(DDD/10==0?"00"+DDD:"0"+DDD);
					
					playerBean.setUsername(userName+String.valueOf(playerBean.getPlayerId()));
					playerBean.setPhone(playerBean.getUsername());
					
					
					insQry = "UPDATE st_ola_player_master SET username=?,phone=? WHERE lms_plr_id=?";
					statement = con.prepareStatement(insQry);
					statement.setString(1, playerBean.getUsername());
					statement.setString(2, playerBean.getPhone());
					statement.setInt(3, playerBean.getPlayerId());
					isUpdate=statement.executeUpdate();
					if(isUpdate!=1){
						throw new LMSException(LMSErrors.SQL_EXCEPTION_ERROR_CODE,LMSErrors.SQL_EXCEPTION_ERROR_MESSAGE);
					}
				}	
								
		} catch (Exception e) {
			e.printStackTrace();
			throw new GenericException(LMSErrors.GENERAL_EXCEPTION_CODE, e);
		}
	}
	
	public 	void  updateAccountIdAndPassword(OlaPlayerRegistrationRequestBean playerBean,Connection con) throws LMSException, GenericException{
		int isUpdated=0;
		PreparedStatement pstmt=null;
		try{
			String query="UPDATE st_ola_player_master set account_id=? , password=? WHERE lms_plr_id=?";
			pstmt=con.prepareStatement(query);
			pstmt.setString(1, playerBean.getAccountId());
			pstmt.setString(2, playerBean.getPassword());
			pstmt.setInt(3, playerBean.getPlayerId());
			isUpdated=pstmt.executeUpdate();
			if(isUpdated<=0){
				throw new LMSException(LMSErrors.SQL_EXCEPTION_ERROR_CODE,LMSErrors.SQL_EXCEPTION_ERROR_MESSAGE);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new GenericException(LMSErrors.GENERAL_EXCEPTION_CODE, e);
		}
	}
	
	
	
	public List<CountryDataBean> getCountryListMap() throws SQLException {

		PreparedStatement statePstmt = null;
		ResultSet stateRs = null;
		PreparedStatement cityPstmt = null;
		ResultSet cityRs = null;
		PreparedStatement countryPstmt = null;
		ResultSet countryRs = null;
		Connection con = null;
		StateDataBean stateBean = null;
		CountryDataBean countryBean = null;
		CityDataBean cityBean = null;
		Map<String,List<CityDataBean>> cityBeanListMap = null;
		Map<String,List<StateDataBean>> stateBeanListMap = null;
		List<CountryDataBean> countryBeanList = null;
			con = DBConnect.getConnection();
			countryBeanList = new ArrayList<CountryDataBean>();
			cityPstmt = con.prepareStatement("SELECT city_code,city_name,state_code FROM st_lms_city_master WHERE STATUS='ACTIVE' ORDER BY state_code, city_name");
			logger.info("CityPstmt: "+cityPstmt);
			cityRs = cityPstmt.executeQuery();
			cityBeanListMap = new HashMap<String, List<CityDataBean>>();
			List<CityDataBean> cityBeanList = null;
			while(cityRs.next()) {
				if(!cityBeanListMap.containsKey(cityRs.getString("state_code")))
					cityBeanList = new ArrayList<CityDataBean>();
				cityBean = new CityDataBean();
				cityBean.setCityCode(cityRs.getString("city_code"));
				cityBean.setCityName(cityRs.getString("city_name"));
				cityBeanList.add(cityBean);
				cityBeanListMap.put(cityRs.getString("state_code"), cityBeanList);	
			}
			
			statePstmt = con.prepareStatement("SELECT country_code,state_code,NAME FROM st_lms_state_master WHERE STATUS='ACTIVE' ORDER BY country_code, name");
			logger.info("StatePstmt: "+statePstmt);
			stateRs = statePstmt.executeQuery();
			stateBeanListMap = new HashMap<String, List<StateDataBean>>();
			List<StateDataBean> stateBeanList = null;
			while(stateRs.next()) {
				if(!stateBeanListMap.containsKey(stateRs.getString("country_code")))
					stateBeanList=new ArrayList<StateDataBean>();
				stateBean=new StateDataBean();
				stateBean.setStateCode(stateRs.getString("state_code"));
				stateBean.setStateName(stateRs.getString("name"));
				stateBean.setCityBeanList(cityBeanListMap.get(stateRs.getString("state_code")));
				stateBeanList.add(stateBean);
				stateBeanListMap.put(stateRs.getString("country_code"), stateBeanList);	
			}
			
			countryPstmt = con.prepareStatement("SELECT country_code,NAME FROM st_lms_country_master WHERE STATUS='ACTIVE' ORDER BY name");
			logger.info("CountryPstmt: "+countryPstmt);
			countryRs = countryPstmt.executeQuery();
			while(countryRs.next()) {
				countryBean = new CountryDataBean();
				countryBean.setCountryCode(countryRs.getString("country_code"));
				countryBean.setCountryName(countryRs.getString("name"));
				countryBean.setStateBeanList(stateBeanListMap.get(countryRs.getString("country_code")));
				countryBeanList.add(countryBean);
			}
		return countryBeanList;
	}
}
