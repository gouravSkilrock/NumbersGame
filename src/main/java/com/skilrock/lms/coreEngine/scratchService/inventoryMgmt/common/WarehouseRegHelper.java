package com.skilrock.lms.coreEngine.scratchService.inventoryMgmt.common;

import java.sql.*;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.skilrock.lms.common.db.DBConnect;
import com.skilrock.lms.common.db.QueryManager;
import com.skilrock.lms.common.db.TableConstants;
import com.skilrock.lms.common.exception.LMSErrors;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.coreEngine.scratchService.inventoryMgmt.beans.WarehouseRegBean;
import com.skilrock.lms.web.scratchService.inventoryMgmt.common.WarehouseRegAction;

public class WarehouseRegHelper {
	
	static Log logger = LogFactory.getLog(WarehouseRegHelper.class);

	public static String registerWarehouse(WarehouseRegBean regBean) throws LMSException {
		
		Statement st = null ;
		Statement st1 = null ;
		Statement st2 = null ;
		ResultSet rs = null ;
		ResultSet rs1 = null ;
		Connection conn = null ;
		
		String cityCode = "" ;
		String stateCode = "" ;
		String countryCode = "" ;
		
		try{
			conn = DBConnect.getConnection();
			conn.setAutoCommit(false);
			
			st = conn.createStatement() ;
			st1 = conn.createStatement() ;
			st2 = conn.createStatement() ;
			
			if(isWarehouseExists(regBean.getWhName().trim(), conn))
				return "failure" ;
			
			String getCountryCode = QueryManager.getST3CountryCode() + " where name='" + regBean.getCountry() + "' ";
			
			rs = st.executeQuery(getCountryCode) ;
			
			if(rs.next())
				countryCode = rs.getString(TableConstants.COUNTRY_CODE) ;
			
			String getCityAndStateCode = QueryManager.getStateAndCityCode() + " where city_name='"+regBean.getCity()+"' and name = '"+regBean.getState()+"' and sm.country_code='"+countryCode+"' ";
			
			rs1 = st1.executeQuery(getCityAndStateCode) ;
			
			logger.info("City State Code : "+getCityAndStateCode);
			
			if(rs1.next()){
				cityCode = rs1.getString("city_code") ;
				stateCode = rs1.getString(TableConstants.STATE_CODE) ;
			}
			
			String createWarehouse = "INSERT INTO st_se_warehouse_master(warehouse_name, warehouse_created_date, city_id, state_id, address1, address2, warehouse_owner_id, warehouse_type) values('"+regBean.getWhName().trim()+"', '"+new Timestamp(System.currentTimeMillis())+"', '"+cityCode+"', '"+stateCode+"', '"+regBean.getAddrLine1()+"', '"+regBean.getAddrLine2()+"', '"+regBean.getOwnerId()+"', '"+regBean.getWarehouseType()+"')" ;
			int success = st2.executeUpdate(createWarehouse) ;
			
			if(! (success == 1))
					throw new LMSException(LMSErrors.GENERAL_EXCEPTION_ERROR_CODE,LMSErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
			
			conn.commit() ;
		}
		catch(Exception e)
		{
			logger.info("Exception occurred due to " + e.getMessage());
		}
		finally{
			DBConnect.closeResource(st, st1, st2, rs, rs1, conn) ;
		}
		return "success" ;
	}
	
	public static boolean isWarehouseExists(String warehouseName, Connection conn)
	{
		Statement st = null ;
		ResultSet rs = null ;
		boolean exists = false ;
		
		try{
			st = conn.createStatement() ;
			
			String checkWarehouse = "select warehouse_name from st_se_warehouse_master where warehouse_name = '" + warehouseName + "';";
			
			rs = st.executeQuery(checkWarehouse) ;
			
			if(rs.next())
				exists = true ;
			
		}catch(Exception e)
		{
			e.printStackTrace() ;
		}
		finally{
			DBConnect.closeResource(st, rs) ;
		}
		
		return exists ;
	}
	
	
}
