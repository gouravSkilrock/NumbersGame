package com.skilrock.lms.web.ola;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;

import com.skilrock.lms.beans.OlaPinSalePaymentBean;
import com.skilrock.lms.beans.UserInfoBean;
import com.skilrock.lms.common.db.DBConnect;
import com.skilrock.lms.common.db.QueryManager;
import com.skilrock.lms.common.utility.GenerateRecieptNo;
import com.skilrock.lms.web.drawGames.common.Util;

public class SubmitOLAPinSalePayementHelper {
/**
 * this method fetch approved tasks for particular distributor
 * @param distributorType
 * @return  ArrayList of OlaPinSalePaymentBean(have data of approved tasks)
 */
public ArrayList<OlaPinSalePaymentBean> fetchPinSalePaymentTask(String distributorType){
	Connection con =DBConnect.getConnection();
	PreparedStatement ps =null;
	String query =null;
	ArrayList<OlaPinSalePaymentBean> olaPinSalePaymentSubmitList = new ArrayList();
	try{
		query ="select task_id,st.wallet_id,start_date,end_date,total_amount,sale_comm_rate,net_amount,wm.wallet_display_name,distributor,st.wallet_id from("+
				"(select task_id,wallet_id,distributor,start_date,end_date,total_amount,sale_comm_rate,net_amount from st_ola_pin_sale_task where distributor=? and approve_status='APPROVED')st"+
				" inner join( st_ola_wallet_master wm )on wm.wallet_id=st.wallet_id)";
		ps =con.prepareStatement(query);
		ps.setString(1,distributorType);
		ResultSet rs =ps.executeQuery();
		while(rs.next()){
			OlaPinSalePaymentBean olaPinSalePayementSubmitBean = new OlaPinSalePaymentBean();
			olaPinSalePayementSubmitBean.setTaskId(rs.getInt("task_id"));
			olaPinSalePayementSubmitBean.setStartDate(rs.getDate("start_date"));
			olaPinSalePayementSubmitBean.setEndDate(rs.getDate("end_date"));
			olaPinSalePayementSubmitBean.setWalletName(rs.getString("wallet_display_name"));
			olaPinSalePayementSubmitBean.setTotalAmount(rs.getDouble("total_amount"));
			olaPinSalePayementSubmitBean.setCommRate(rs.getDouble("sale_comm_rate"));
			olaPinSalePayementSubmitBean.setNetAmount(rs.getDouble("net_amount"));
			olaPinSalePayementSubmitBean.setDistributor(rs.getString("distributor"));
			olaPinSalePayementSubmitBean.setWalletId(rs.getInt("wallet_id"));
			olaPinSalePaymentSubmitList.add(olaPinSalePayementSubmitBean);
		}
		
	}catch(Exception e){
		e.printStackTrace();
	}
return 	olaPinSalePaymentSubmitList;
}
/**
 * this method make transactions for checked records and generate a reciept for transactions
 * @param olaPinSalePaymentSubmitList
 * @param check
 * @param userBean
 * @return String true/false/recieptNo
 */
public String savePinSalePayment (ArrayList<OlaPinSalePaymentBean> olaPinSalePaymentSubmitList ,int[]check,UserInfoBean userBean){
	Connection con = DBConnect.getConnection();
	String lmsTransQuery = "insert into st_lms_transaction_master(user_type,service_code,interface) values(?,?,?)";
	PreparedStatement psSLTM = null;
	long transactionId=0;
	ArrayList<Long> transactionIdList=new ArrayList<Long>();
	String 	boTransQuery=QueryManager.insertInBOTransactionMaster();
	double totalAmount =0.0;
	
	try {
		con.setAutoCommit(false);
			for (int n = 0; n < olaPinSalePaymentSubmitList.size(); n++) {

				for (int i = 0; i < check.length; i++) {
					System.out.println(check[i]);
					OlaPinSalePaymentBean pinSalePaymentSubmitBean = olaPinSalePaymentSubmitList
							.get(n);
					if (pinSalePaymentSubmitBean.getTaskId() == check[i]) {
							
						totalAmount=totalAmount+pinSalePaymentSubmitBean.getNetAmount();
						psSLTM = con.prepareStatement(lmsTransQuery);
						psSLTM.setString(1, "BO");
						psSLTM.setString(2, "OLA");
						psSLTM.setString(3, "WEB");
						psSLTM.executeUpdate();
						ResultSet rsSLTM = psSLTM.getGeneratedKeys();
						if (rsSLTM.next()) {
							transactionId = rsSLTM.getLong(1);
						}else{
							//return Error;
							
						}
						// Insert Data into st_lms_bo_tranasction_master
						PreparedStatement psBTM = con
								.prepareStatement(boTransQuery);
						psBTM.setLong(1, transactionId);
						psBTM.setInt(2, userBean.getUserId());
						psBTM.setInt(3, userBean.getUserOrgId());
						psBTM.setString(4, "OLA_DISTRIBUTOR");
						psBTM.setObject(5, null);
						psBTM.setTimestamp(6, new java.sql.Timestamp(new Date()
								.getTime()));
						psBTM.setString(7, "OLA_CASHCARD_SALE");

						psBTM.executeUpdate();
						// Insert Data int st_ola_bo_distributor_transaction
						String bodTransQuery = "insert into st_ola_bo_distributor_transaction values(?,?,?,?,?,?,?,?,?,?)";
						PreparedStatement psBDTM = con.prepareStatement(bodTransQuery);
						psBDTM.setLong(1,transactionId );
						psBDTM.setInt(2,pinSalePaymentSubmitBean.getWalletId() );
						psBDTM.setDouble(3,pinSalePaymentSubmitBean.getTotalAmount() );
						psBDTM.setDouble(4,pinSalePaymentSubmitBean.getCommRate());
						psBDTM.setDouble(5,pinSalePaymentSubmitBean.getNetAmount() );
						psBDTM.setString(6,pinSalePaymentSubmitBean.getDistributor() );
						psBDTM.setString(7, "OLA_CASHCARD_SALE");
						psBDTM.setDate(8,pinSalePaymentSubmitBean.getStartDate());
						psBDTM.setDate(9,pinSalePaymentSubmitBean.getEndDate());
						psBDTM.setString(10,"OLA");
						psBDTM.executeUpdate();
						//update approve_status in st_ola_pin_sale_task 
						String updateQuery="update st_ola_pin_sale_task  set approve_status='DONE',ref_lms_transaction_id=? where task_id=? and approve_status='APPROVED'";
						PreparedStatement psUpdate=con.prepareStatement(updateQuery);
						psUpdate.setLong(1,transactionId);
						psUpdate.setInt(2, pinSalePaymentSubmitBean.getTaskId());
						int isUpdate =psUpdate.executeUpdate();
												
						if(isUpdate!=1){
							System.out.println(".................Already Done or not exists");
							return "false";
						}
						System.out.println(psUpdate);
						transactionIdList.add(transactionId);
					}
				}
			}
	// Generate Bo Receipt for all transactions
	String autoGeneRecieptNo=insertRcptMapping(con,transactionIdList);
	if(!autoGeneRecieptNo.equalsIgnoreCase("false")&&autoGeneRecieptNo!=null){
		con.commit();
	}
	return autoGeneRecieptNo;
	} catch (Exception e) {
			e.printStackTrace();

	}finally{
		try{
			if(con!=null){
				con.close();
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
		
	
	return "false";
	

}

	private String insertRcptMapping(Connection con,
			ArrayList<Long> transactionIdList) throws SQLException {

		// START INSERTION OF RECEIPT ID FOR GOVERNMENT RECEIPTS

		String insBoRcpt = QueryManager.insertInBOReceipts();
		String insRcpTrMap = QueryManager.insertBOReceiptTrnMapping();
		int id = 0;
		PreparedStatement preBoRcpt = null;
		PreparedStatement preRcpTrMap = null;
		// PreparedStatement preRcpGenMap = null;

		PreparedStatement autoGenPstmt = null;
		// String getLatestRecieptNumber="SELECT * from
		// st_bo_receipt_gen_mapping where receipt_type=? ORDER BY generated_id
		// DESC LIMIT 1 ";
		autoGenPstmt = con
				.prepareStatement("SELECT * from st_lms_bo_receipts where (receipt_type=? or receipt_type=?) ORDER BY generated_id DESC LIMIT 1");
		autoGenPstmt.setString(1, "OLA_INVOICE");
		autoGenPstmt.setString(2, "OLA_RECEIPT");
		ResultSet recieptRs = autoGenPstmt.executeQuery();
		String lastRecieptNoGenerated = null;

		while (recieptRs.next()) {
			lastRecieptNoGenerated = recieptRs.getString("generated_id");
		}

		String autoGeneRecieptNo = GenerateRecieptNo.getRecieptNo(
				"OLA_INVOICE", lastRecieptNoGenerated, "BO");

		// insert receipt master

		preBoRcpt = con.prepareStatement(QueryManager.insertInReceiptMaster());
		preBoRcpt.setString(1, "BO");
		preBoRcpt.executeUpdate();

		ResultSet rss = preBoRcpt.getGeneratedKeys();
		rss.next();
		id = rss.getInt(1);

		// insert into bo receipts

		preBoRcpt = con.prepareStatement(insBoRcpt);
		preBoRcpt.setInt(1, id);
		preBoRcpt.setString(2, "OLA_INVOICE");
		preBoRcpt.setObject(3, null);
		preBoRcpt.setString(4, "OLA_DISTRIBUTOR");
		preBoRcpt.setString(5, autoGeneRecieptNo);
		preBoRcpt.setTimestamp(6, Util.getCurrentTimeStamp());

		/*
		 * //prepare4.setString(1, autoGeneRecieptNo); preBoRcpt.setString(1,
		 * "GOVT_RCPT"); preBoRcpt.setString(2, null);
		 */

		preBoRcpt.executeUpdate();

		/*
		 * //insert into recipt gen reciept mapping String
		 * updateBoRecieptGenMapping=QueryManager.updateST5BOReceiptGenMapping();
		 * preRcpGenMap=con.prepareStatement(updateBoRecieptGenMapping);
		 * preRcpGenMap.setInt(1,id);
		 * preRcpGenMap.setString(2,autoGeneRecieptNo);
		 * preRcpGenMap.setString(3,"GOVT_RCPT"); preRcpGenMap.executeUpdate();
		 */

		for (int i = 0; i < transactionIdList.size(); i++) {

			preRcpTrMap = con.prepareStatement(insRcpTrMap);
			preRcpTrMap.setInt(1, id);
			preRcpTrMap.setLong(2, transactionIdList.get(i));
			preRcpTrMap.executeUpdate();
		}

		// END INSERTION 
		return autoGeneRecieptNo;
	}


}
