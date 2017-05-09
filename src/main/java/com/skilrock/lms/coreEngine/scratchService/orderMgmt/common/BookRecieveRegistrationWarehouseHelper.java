package com.skilrock.lms.coreEngine.scratchService.orderMgmt.common;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.skilrock.lms.common.db.DBConnect;
import com.skilrock.lms.coreEngine.scratchService.pwtMgmt.common.CommonFunctionsHelper;

public class BookRecieveRegistrationWarehouseHelper {
	static Log logger = LogFactory.getLog(BookRecieveRegistrationWarehouseHelper.class);

	public Map<String, List<String>> getBooks(int userOrgId, int userId,String challanId) {
		Statement stmt=null;
		ResultSet rs=null;
		Connection con=DBConnect.getConnection();
		Map<String, List<String>> gameBookMap = new HashMap<String, List<String>>();
		List<String> booksList = null;
		String seperator = "-";
		String query=null;
		String dcId=null;
		StringBuilder warehouseId=new StringBuilder();
		try{
			stmt=con.createStatement();
			query="select receipt_id from st_lms_bo_receipts where generated_id='"+challanId+"';";
			rs=stmt.executeQuery(query);
			if(rs.next())
				dcId=rs.getString("receipt_id");
			
			query="select warehouse_id from st_se_warehouse_master where warehouse_owner_id="+userId+";";
			rs=stmt.executeQuery(query);
			while(rs.next()){
				warehouseId.append(rs.getString("warehouse_id")).append(",");
			}
			query="select gis.game_id,gm.game_nbr,gm.game_name,gis.book_nbr from st_se_game_inv_status gis,st_se_game_master gm where gis.current_owner_id="+userOrgId+" and gis.current_owner='BO' and gis.game_id=gm.game_id and gis.book_status='ASSIGNED' and bo_dl_id='"+dcId+"' and warehouse_id in ("+warehouseId.substring(0, warehouseId.length()-1)+");";
			rs = stmt.executeQuery(query);
			String gameDel = null;
			while (rs.next()) {
				gameDel = rs.getInt("game_nbr") + seperator
						+ rs.getString("game_name");
				if (gameBookMap.containsKey(gameDel)) {
					booksList.add(rs.getString("book_nbr"));
				} else {
					booksList = new ArrayList();
					gameBookMap.put(gameDel, booksList);
					booksList.add(rs.getString("book_nbr"));
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return gameBookMap;
	}

	public boolean updateBooks(int userOrgId, int userId,
			List<String> bookNumberList) throws SQLException {
		Statement stmt=null;
		ResultSet rs=null;
		Connection con=DBConnect.getConnection();
		int gameId=0;
		int warehouseId=-1;
		String packNbr=null; 
		String query=null;
		try{
			stmt=con.createStatement();
			con.setAutoCommit(false);
//			query="select warehouse_id from st_se_warehouse_master where warehouse_owner_id="+userId+";";
//			rs=stmt.executeQuery(query);
//			if(rs.next())
//				warehouseId=rs.getInt("warehouse_id");
			
			for (String bookNbr : bookNumberList) {	
				query="select game_id,pack_nbr, warehouse_id from st_se_game_inv_status where book_nbr='"+bookNbr+"'";
				rs=stmt.executeQuery(query);
				while(rs.next()){
					warehouseId=rs.getInt("warehouse_id");
					gameId=rs.getInt("game_id");
					packNbr=rs.getString("pack_nbr");
				}
				query="update st_se_game_inv_status set book_status='INACTIVE' where book_nbr = '"+bookNbr+"' ";
				stmt.executeUpdate(query);
				CommonFunctionsHelper helper =new CommonFunctionsHelper();
				helper.updateGameInvDetail(gameId, packNbr, bookNbr, "BO", userOrgId,userId,"INACTIVE",warehouseId, con);
			}
			con.commit();
		}catch(Exception e){
			e.printStackTrace();
			con.rollback();
			return false;
		}
		return true;
	}

}
