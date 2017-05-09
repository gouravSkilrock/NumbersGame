package com.skilrock.ola.reportsMgmt.controllerImpl;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.skilrock.lms.common.Utility;
import com.skilrock.lms.common.db.DBConnect;
import com.skilrock.lms.common.exception.GenericException;
import com.skilrock.lms.common.exception.LMSErrors;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.ola.common.OLAUtility;
import com.skilrock.ola.userMgmt.javaBeans.OlaPlayerRegistrationRequestBean;

public class SearchPlayerControllerImpl  {
	
	static Log logger = LogFactory.getLog(SearchPlayerControllerImpl.class);
	
	public ArrayList<OlaPlayerRegistrationRequestBean> searchPlayer(int retOrgId,
			String walletName, String regToDate, String regFromDate, String alias,
			String regType) throws LMSException, GenericException {
		Connection con = DBConnect.getConnection();
		ArrayList<OlaPlayerRegistrationRequestBean> plrDetailsBeanList = new ArrayList<OlaPlayerRegistrationRequestBean>();
		int walletId = 0;
		boolean tmp = false;
		try {			
				StringBuilder searchPlayer=new StringBuilder();
				
					if(!walletName.equalsIgnoreCase("-1")){
						walletId = OLAUtility.getWalletId(walletName);
						searchPlayer.append("pm.wallet_id="+walletId);
						tmp = true;
					}
					
					if (!regType.equalsIgnoreCase("-1")) {
						if(tmp){
							searchPlayer.append(" AND registration_type = '"+regType+"'");
						}else{
							searchPlayer.append(" registration_type = '"+regType+"'");
							tmp = true;
						}
					}
					if(regFromDate != null){
						if(tmp){
							searchPlayer.append(" AND registration_date> '"+regFromDate+"'");
						}else{
							searchPlayer.append("  registration_date> '"+regFromDate+"'");
							tmp = true;;
						}
					}	
					if(regToDate != null){
						if(tmp){
							searchPlayer.append(" AND registration_date< '"+regToDate+"'");
						}else{
							searchPlayer.append("  registration_date< '"+regToDate+"'");
							tmp = true;
						}
					}
					if (retOrgId > 0) {
						if(tmp){
							searchPlayer.append(" AND ref_user_org_id = "+retOrgId);
						}else{
							searchPlayer.append("  ref_user_org_id = "+retOrgId);
							tmp = true;
						}
					}
					if(alias != null){
						if(tmp){
							searchPlayer.append(" AND (username like '"+alias+"%')");
						}else{
							searchPlayer.append("  (username like '"+alias+"%')");
							tmp = true;
						}
					}
						
				
			Statement stmt = null;
			String query="select IF(IFNULL(username,'')='',phone,username) username,IF(concat(IFNULL(fname,''),' ',IFNULL(lname,''))='','N.A',concat(IFNULL(fname,''),' ',IFNULL(lname,''))) as name,pm.wallet_id,email,registration_date,ref_user_org_id ,registration_type from st_ola_player_master pm inner join st_ola_affiliate_plr_mapping apm on " +
					" pm.lms_plr_id=apm.player_id and pm.wallet_id=apm.wallet_id where "+searchPlayer.toString();
			stmt=con.createStatement();
			logger.info(query);
			ResultSet rs = stmt.executeQuery(query);
			
			while (rs.next()) {
				OlaPlayerRegistrationRequestBean plrDetailBean = new OlaPlayerRegistrationRequestBean();				
					plrDetailBean.setUsername(rs.getString("username"));
					plrDetailBean.setFirstName(rs.getString("name"));
					plrDetailBean.setEmail(rs.getString("email"));					
					plrDetailBean.setPlrRegDate(rs.getString("registration_date").substring(0, rs.getString("registration_date").indexOf('.')));
					plrDetailBean.setWalletName(OLAUtility.getWalletName(rs.getInt("wallet_id")));
					plrDetailBean.setRegType(rs.getString("registration_type"));
					plrDetailsBeanList.add(plrDetailBean);
				}
		}catch(SQLException se){
			throw new GenericException(LMSErrors.SQL_EXCEPTION_CODE, se);
		}catch(Exception e){
			e.printStackTrace();
			throw new GenericException(LMSErrors.GENERAL_EXCEPTION_CODE, e);
		}
		return plrDetailsBeanList;
	}

}
