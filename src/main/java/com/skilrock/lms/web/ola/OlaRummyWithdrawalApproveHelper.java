package com.skilrock.lms.web.ola;


import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import com.skilrock.lms.beans.OlaRummyWithdrawalBean;
import com.skilrock.lms.common.db.DBConnect;

public class OlaRummyWithdrawalApproveHelper {
Log logger = LogFactory.getLog(OlaRummyWithdrawalApproveAction.class);
	
public ArrayList<OlaRummyWithdrawalBean> rummyWithdrawalData(ArrayList<OlaRummyWithdrawalBean> withdrawalBeanList,String startDate,String endDate,String transferMode,String status){
	Connection con = DBConnect.getConnection();
	StringBuilder query = new StringBuilder("select task_id,request_date,user_id,plr_banking_name,amount,bank_name,bank_account_nbr,bank_branch_name,bank_branch_city,ifs_code,transfer_mode,rms_process_status from st_ola_rummy_withdrawal_rep where request_date >=? and request_date <=? and isBind=? ");
	PreparedStatement ps = null;
	if(!status.equalsIgnoreCase("ALL") ){
		query.append(" and rms_process_status = '"+status+"'");
		if(!transferMode.equalsIgnoreCase("ALL")){
			query.append(" and  transfer_mode  = '"+transferMode+"'");
		}
	}
	else {
		if(!transferMode.equalsIgnoreCase("ALL")){
			query.append(" and transfer_mode  = '"+transferMode+"'");	
		}

	}
	try {
		con.setAutoCommit(false);
		ps = con.prepareStatement(query.toString());
		ps.setString(1,startDate);
		ps.setString(2,endDate);
		ps.setString(3,"NO");
		ResultSet rs=ps.executeQuery();	
	while(rs.next()){
			OlaRummyWithdrawalBean withdrawalBean = new OlaRummyWithdrawalBean ();
			withdrawalBean.setTaskId(rs.getInt("task_id"));	
			withdrawalBean.setAccountId(rs.getString("user_id"));
			withdrawalBean.setAmount(rs.getDouble("amount"));
			withdrawalBean.setBankAcNumber(rs.getString("bank_account_nbr"));
			withdrawalBean.setBankName(rs.getString("bank_name")+" "+rs.getString("bank_branch_city"));
			withdrawalBean.setName(rs.getString("plr_banking_name"));
			withdrawalBean.setStatus(rs.getString("rms_process_status"));
			withdrawalBean.setTransferMode(rs.getString("transfer_mode"));
			withdrawalBean.setRequestDate(rs.getDate("request_date"));
			withdrawalBeanList.add(withdrawalBean);
		}
	con.commit();	
	}
	catch(Exception e){
		
		e.printStackTrace();
	}
	finally {
		try {
			con.close();
		} catch (SQLException e) {
			e.printStackTrace();

		}
	}
	return withdrawalBeanList;
}

public String updateRummyWithdrawal(int taskId[]){
	
	Connection con =null;
	PreparedStatement ps = null;
	
	try {
			con = DBConnect.getConnection();
			StringBuilder taskIds = new StringBuilder();
			StringBuilder respData = new StringBuilder();
			Calendar cal = Calendar.getInstance();
			Date currentDt = null;
			currentDt = new Date(cal.getTime().getTime());
			for(int i=0;i<taskId.length;i++){
				taskIds.append(taskId[i]+",");
				respData.append(taskId[i]+",DONEN@xt");
			}
			taskIds.deleteCharAt(taskIds.length()-1);
			String query ="update st_ola_rummy_withdrawal_rep set rms_process_status =?,rms_process_date=? where task_id in("+taskIds.toString()+")";
		
			con.setAutoCommit(false);
			ps = con.prepareStatement(query);
			ps.setString(1,"DONE");
			ps.setDate(2,currentDt);
			logger.info("Update st_ola_rummy_withdrawal_rep "+ps);
		int isUpdate=ps.executeUpdate();	
		if(isUpdate!=taskId.length){
			logger.info("Some Error In Update st_ola_rummy_withdrawal_rep ");
			return "";
		}
		
		
		con.commit();
		logger.info("respData "+respData.toString());
		return respData.toString();
	}catch(Exception e){
		e.printStackTrace();
	}finally{
		
			try {
				if(con!=null){
						con.close();
				}
				if(ps!=null){
					ps.close();
					}
			} catch (SQLException e) {
				
				e.printStackTrace();
			}
		
		
	}
	
	
	
	return "";
}
public InputStream writeDataIntoExcel(ArrayList<OlaRummyWithdrawalBean> withdrawalBeanList){
	HSSFWorkbook wb =new HSSFWorkbook();
	HSSFSheet wbsheet = wb.createSheet();
	HSSFRow row = null;
	HSSFCell cell =null;
	wbsheet.setColumnWidth((short)1,(short)5000);
	wbsheet.setColumnWidth((short)2,(short)5000);
	wbsheet.setColumnWidth((short)3,(short)5000);
	wbsheet.setColumnWidth((short)4,(short)5000);
	wbsheet.setColumnWidth((short)5,(short)5000);
	wbsheet.setColumnWidth((short)6,(short)5000);
	wbsheet.setColumnWidth((short)7,(short)5000);
	wbsheet.setColumnWidth((short)8,(short)5000);

	
	row = wbsheet.createRow(1);
	row.setHeight((short)500);

	cell = row.createCell((short)1);
	cell.setCellValue(" Account Id ");
	cell = row.createCell((short)2);
	cell.setCellValue(" Name ");
	cell = row.createCell((short)3);
	cell.setCellValue(" Address ");
	cell = row.createCell((short)4);
	cell.setCellValue(" Amount ");
	cell = row.createCell((short)5);
	cell.setCellValue(" Bank Account Number ");
	cell = row.createCell((short)6);
	cell.setCellValue(" Bank Name ");
	cell = row.createCell((short)7);
	cell.setCellValue(" Transfer Mode ");
	cell = row.createCell((short)8);
	cell.setCellValue(" Status ");
	
	
	
for(int i=3;i<withdrawalBeanList.size()+3;i++){
	OlaRummyWithdrawalBean withdrawalBean = withdrawalBeanList.get(i-3);
	row = wbsheet.createRow(i);
	row.setHeight((short)500);

	cell = row.createCell((short)1);
	cell.setCellValue(withdrawalBean.getAccountId());
	cell = row.createCell((short)2);
	cell.setCellValue(withdrawalBean.getName());
	cell = row.createCell((short)3);
	cell.setCellValue(withdrawalBean.getAddress());
	cell = row.createCell((short)4);
	cell.setCellValue(withdrawalBean.getAmount());
	cell = row.createCell((short)5);
	cell.setCellValue(withdrawalBean.getBankAcNumber());
	cell = row.createCell((short)6);
	cell.setCellValue(withdrawalBean.getBankName());
	cell = row.createCell((short)7);
	cell.setCellValue(withdrawalBean.getTransferMode());
	cell = row.createCell((short)8);
	cell.setCellValue(withdrawalBean.getStatus());
	
	
}
	try {
		
		 ByteArrayOutputStream baos = new ByteArrayOutputStream();
		    wb.write(baos);
		 InputStream excelStream = new ByteArrayInputStream(baos.toByteArray());
       return    excelStream;
		
	}
	catch(Exception e){
		e.printStackTrace();
	}	
	
	return null;
	
}
	

}
