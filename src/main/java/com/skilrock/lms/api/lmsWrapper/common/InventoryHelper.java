package com.skilrock.lms.api.lmsWrapper.common;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;

import com.skilrock.lms.common.db.DBConnect;

public class InventoryHelper {

	public static HashMap<String,String> brandIdMap=null;
	public static HashMap<String,String> modelIdMap=null;
	public static HashMap<String,String> inventoryIdMap=null;
	public static HashMap<String,String> brandNameMap=null;
	public static HashMap<String,String> modelNameMap=null;
	public static HashMap<String,String> inventoryNameMap=null;
	public static HashMap<String,String> consInventoryIdMap=null;
	public static HashMap<String,String> consInventoryNameMap=null;
	public static HashMap<String,String> consModelIdMap=null;
	public static HashMap<String,String> consInvIdFromModelMap=null;
	public static void setInventoryDataToMap(){
		ResultSet rs =null;
		brandIdMap=new HashMap<String, String>();
		modelIdMap=new HashMap<String, String>();
		inventoryIdMap=new HashMap<String, String>();
		brandNameMap=new HashMap<String,String>();
		modelNameMap=new HashMap<String, String>();
		inventoryNameMap=new HashMap<String, String>();
		consInventoryIdMap=new HashMap<String, String>();
		consInventoryNameMap=new HashMap<String, String>();
		consModelIdMap=new HashMap<String, String>();
		consInvIdFromModelMap=new HashMap<String, String>();
		String modelDataQry="select model_id,model_name from st_lms_inv_model_master order by model_id asc";
		String brandDataQry="select brand_id,brand_name from st_lms_inv_brand_master order by brand_id asc";
		String inventoryDataQry="select inv_id,inv_name from st_lms_inv_master where inv_type='NON_CONS' order by inv_id asc";
		
		String consInventoryDataQry="select inv_id,inv_name from st_lms_inv_master where inv_type='CONS' order by inv_id asc";
		String consModelDataQry="select inv_model_id,inv_id from st_lms_cons_inv_specification  order by inv_id asc";
		Connection con=DBConnect.getConnection();
		try {
			Statement stmt=con.createStatement();
			rs=stmt.executeQuery(modelDataQry);
			
				while(rs.next()){
					modelIdMap.put(rs.getString("model_id"),rs.getString("model_name"));
					modelNameMap.put(rs.getString("model_name"),rs.getString("model_id"));
				}
			
			
			rs=stmt.executeQuery(brandDataQry);
			
				while(rs.next()){
					brandIdMap.put(rs.getString("brand_id"),rs.getString("brand_name"));
					brandNameMap.put(rs.getString("brand_name"),rs.getString("brand_id"));
				}
			
			
			rs=stmt.executeQuery(inventoryDataQry);
			
				while(rs.next()){
					inventoryIdMap.put(rs.getString("inv_id"),rs.getString("inv_name"));
					inventoryNameMap.put(rs.getString("inv_name"),rs.getString("inv_id"));
				}
				
				rs=stmt.executeQuery(consInventoryDataQry);
				
				while(rs.next()){
					consInventoryIdMap.put(rs.getString("inv_id"),rs.getString("inv_name"));
					consInventoryNameMap.put(rs.getString("inv_name"),rs.getString("inv_id"));
				}
				rs=stmt.executeQuery(consModelDataQry);
				
				while(rs.next()){
					consModelIdMap.put(rs.getString("inv_id"),rs.getString("inv_model_id"));
					consInvIdFromModelMap.put(rs.getString("inv_model_id"), rs.getString("inv_id"));
				}
		} catch (SQLException e) {
			
			e.printStackTrace();
		}
		finally{
			try{
				con.close();
			}
			catch(Exception se){
				se.printStackTrace();
			}
		}
		
		
	}
}
