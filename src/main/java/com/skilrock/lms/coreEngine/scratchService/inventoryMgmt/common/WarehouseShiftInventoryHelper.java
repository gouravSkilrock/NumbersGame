package com.skilrock.lms.coreEngine.scratchService.inventoryMgmt.common;

import java.sql.*;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.skilrock.lms.beans.UserInfoBean;
import com.skilrock.lms.common.db.DBConnect;
import com.skilrock.lms.coreEngine.scratchService.inventoryMgmt.serviceImpl.WarehouseShiftInventoryServiceImpl;
import com.skilrock.lms.web.scratchService.inventoryMgmt.common.DirectSaleBOAction;

public class WarehouseShiftInventoryHelper {
	
	Log logger = LogFactory.getLog(WarehouseShiftInventoryHelper.class);

	public boolean verifyBook(String warehouse, String bookNbr, int gameId, int gameNbr,
			Connection connection) {
		
		Statement st = null ;
		ResultSet rs = null ;
		boolean isValid = false ;
		
		try{
			st = connection. createStatement();
			String ifWarehouseContainsBook = "select book_nbr from st_se_game_inv_status where current_owner = 'BO' and book_nbr = '" + bookNbr + "' and warehouse_id = '" + warehouse + "';" ;
			
			rs = st. executeQuery(ifWarehouseContainsBook) ;
			
			if(rs .next())
				isValid = true ;
		}
		catch(Exception e){
			e.printStackTrace() ;
		}
		finally{
			DBConnect.closeResource(st, rs) ;
		}
		
		return isValid ;
	}

	public boolean updateStatus(int gameId, String fromWarehouse, String toWarehouse,
			String bookNbr, Connection connection) {
		
		Statement st = null ;
		int isSuccess = 0;
		
		try{
			st = connection.createStatement() ;
			String updateStatusTable = "update st_se_game_inv_status set warehouse_id = " + toWarehouse + ", book_status = 'ASSIGNED' where game_id = " + gameId + " and book_nbr = '"+bookNbr+"';" ;
			
			isSuccess= st .executeUpdate(updateStatusTable) ;
		}catch(Exception e){
			e.printStackTrace() ;
		}finally{
			DBConnect.closeResource(st) ;
		}
		
		return (isSuccess == 1) ? true : false;
	}

	public int createHistory(int gameId, String bookNbr, int gameNbrDigits,
			int bkDigits, String toWarehouse, Connection connection, int warehouseOwnerId, UserInfoBean userInfoBean) {
		Statement st = null ;
		int dcId = 0;
		try{
			st = connection.createStatement() ;
			
			String createHistory = "insert into st_se_game_inv_detail(game_id, pack_nbr, book_nbr, current_owner, current_owner_id, warehouse_id, transaction_date, transaction_at, changed_by_user_id, book_status, order_invoice_dc_id) values ("+gameId + ", '" +bookNbr.substring(0, gameNbrDigits+bkDigits+1) + "', '" + bookNbr + "', 'BO', "+ userInfoBean.getUserOrgId() + ", "+toWarehouse+", '" + new Timestamp(System.currentTimeMillis()) + "', 'BO', "+userInfoBean.getUserId()+", 'ASSIGNED',"+dcId+");" ;
			
			logger.info("query : " + createHistory) ;
			
			st.executeUpdate(createHistory) ;
			
		}catch(Exception e){
			e.printStackTrace() ;
		}finally{
			DBConnect.closeResource(st) ;
		}
		
		return dcId;
	}

	public static int getWarehouseOwnerId(String fromWarehouse) {
		
		Connection conn = null ;
		Statement st = null ;
		ResultSet rs = null ;
		int ownerId = 0 ;
		
		try{
			conn = DBConnect.getConnection() ;
			
			st = conn.createStatement() ;
			
			String getWarehouseOwnerId = "select warehouse_owner_id from st_se_warehouse_master where warehouse_id = " + fromWarehouse + ";" ;
			
			rs = st.executeQuery(getWarehouseOwnerId) ;
			
			if(rs.next())
				ownerId = rs.getInt("warehouse_owner_id") ;
			
			
		}catch(Exception e){
			e.printStackTrace() ;
		}finally{
			DBConnect.closeResource(conn, rs, st) ;
		}
		
		
		return ownerId;
	}

}
