package com.skilrock.lms.web.ola;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import com.skilrock.lms.beans.UserInfoBean;
import com.skilrock.lms.common.db.DBConnect;
import com.skilrock.lms.coreEngine.ola.common.CommonFunctionsHelper;

public class OlaRummyRefundPinHelper {

public String initRefundPin(int walletId,long pinNbr,long serialNbr,String plrName,double amount,String desKey,String propKey){
	if(amount>0){
		Connection con = DBConnect.getConnection();
		String cancelReason ="CANCEL_MANUAL";
		String returnType =refundPin(walletId, pinNbr, serialNbr, plrName,amount,desKey,propKey,con,cancelReason);	
		try {
			con.close();
		} catch (SQLException e) {
				e.printStackTrace();
				return "Error In Refund";
		}
		return returnType;
	}
return "Amount Should Be Greater Than Zero";	
}	
	
public String  refundPin(int walletId,long pinNbr,long serialNbr,String plrName,double amount,String desKey,String propKey,Connection con,String cancelReason){

	String query =null;
	PreparedStatement ps = null;
	PreparedStatement ps1 = null;
	CashCardPinGeneratorHelper helper = new CashCardPinGeneratorHelper();
	String enPin = helper.encryptPin(pinNbr+"", desKey, propKey);
	try{
		con.setAutoCommit(false);
		
		query=" select * from (select * from (select serial_number,lms_transaction_id from st_ola_pin_rep_rm_"+walletId+" where serial_number=? and pin_number=? and amount=? " +
				"	and player_id=? and verification_status=?)pinRep inner join (select transaction_id,net_amt,agent_net_amt,retailer_comm,agent_comm,ims_ref_transaction_id,retailer_org_id from st_ola_ret_deposit)retD on retD.transaction_id=pinRep.lms_transaction_id) main"+  
				" inner join (select a.user_id,a.parent_user_id,b.organization_id parent_org_id,a.organization_id from st_lms_user_master a,st_lms_user_master b where a.parent_user_id=b.user_id)sub on sub.organization_id=main.retailer_org_id ";

		
		ps =con.prepareStatement(query);
		ps.setLong(1, serialNbr);
		ps.setString(2, enPin);
		ps.setDouble(3, amount);
		ps.setString(4, plrName);
		ps.setString(5, "PENDING");
		ResultSet rs = ps.executeQuery();
		if(rs.next()){
			UserInfoBean userBean= new UserInfoBean ();
			userBean.setUserOrgId(rs.getInt("retailer_org_id"));
			userBean.setParentOrgId(rs.getInt("parent_org_id"));
			userBean.setUserId(rs.getInt("user_id"));
			// call refund
			boolean isRefund = rummyDepositeRefund(amount,
					rs.getDouble("net_amt"),rs.getDouble("agent_net_amt"),rs.getDouble("retailer_comm"),
					rs.getDouble("agent_comm"),plrName, con,walletId, userBean,
					rs.getInt("lms_transaction_id"),rs.getInt("ims_ref_transaction_id"),cancelReason);
			if (isRefund == true) {
				// Update pin verification status
				query ="update st_ola_pin_rep_rm_2 set verification_status=? where serial_number=? and pin_number=? and amount=? " +
								"	and player_id=? and verification_status=?";
				ps1 = con.prepareStatement(query);
				ps1.setString(1,"CANCELED");
				ps1.setLong(2, serialNbr);
				ps1.setString(3, enPin);
				ps1.setDouble(4, amount);
				ps1.setString(5, plrName);
				ps1.setString(6, "PENDING");
				ps1.executeUpdate();
				con.commit();
				return "success";
				// return error;

			} else {
				System.out
						.println("Error During Refund to the retailer ");
				
				return "Error In Refund";
				// return "error during Refund the money";
			}
		
		}
		else{
			return "No Match Found Please Enter Correct Details";
		}
	}catch(Exception e){
		e.printStackTrace();
		
	}
	return "Error In Refund";
}

public boolean rummyDepositeRefund(double depositAmt, double retNetAmt,
		double agentNetAmt, double retailerComm, double agentComm,
		String userName, Connection con, int walletId,
		UserInfoBean userBean, long depositTransactionId,
		long imsTransactionID,String cancelReason) throws SQLException {

	// update in transaction master
	String insertInLMS = "insert into st_lms_transaction_master(user_type,service_code,interface) values('RETAILER','OLA','WEB')";
	PreparedStatement pstmt1 = con.prepareStatement(insertInLMS);
	long transactionId = 0;
	pstmt1.executeUpdate();
	ResultSet rs1 = pstmt1.getGeneratedKeys();
	if (rs1.next()) {
		transactionId = rs1.getLong(1);
		// insert into retailer transaction master
		pstmt1 = con
				.prepareStatement("INSERT INTO st_lms_retailer_transaction_master (transaction_id,retailer_user_id,retailer_org_id,game_id,transaction_date,transaction_type) VALUES (?,?,?,?,?,?)");
		pstmt1.setLong(1, transactionId);
		pstmt1.setInt(2, userBean.getUserId());
		pstmt1.setInt(3, userBean.getUserOrgId());
		pstmt1.setInt(4, walletId);
		pstmt1
				.setTimestamp(5, new java.sql.Timestamp(new Date()
						.getTime()));
		pstmt1.setString(6, "OLA_DEPOSIT_REFUND");
		pstmt1.executeUpdate();

		pstmt1 = con
				.prepareStatement("insert into st_ola_ret_deposit_refund(transaction_id, wallet_id, retailer_org_id, ims_ref_transaction_id, ola_ref_transaction_id, claim_status, cancel_reason, deposit_amt, net_amt, agent_net_amt, retailer_comm, agent_comm, agent_ref_transaction_id, party_id) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
		pstmt1.setLong(1, transactionId);
		pstmt1.setInt(2, walletId);
		pstmt1.setInt(3, userBean.getUserOrgId());
		pstmt1.setLong(4, imsTransactionID);
		pstmt1.setLong(5, depositTransactionId);
		pstmt1.setString(6, "CLAIM_BAL");
		pstmt1.setString(7,cancelReason);
		pstmt1.setDouble(8, depositAmt);
		pstmt1.setDouble(9, retNetAmt);
		pstmt1.setDouble(10, agentNetAmt);
		pstmt1.setDouble(11, retailerComm);
		pstmt1.setDouble(12, agentComm);
		pstmt1.setInt(13, 0);
		pstmt1.setString(14, userName);
		pstmt1.executeUpdate();
		// update ret_comm in st_ola_ret_withdrawl
		// pstmt1 =
		// con.prepareStatement("update st_ola_ret_deposit set retailer_comm=(retailer_comm-"+retailerComm+"),agent_comm=(agent_comm-"+agentComm+") where transaction_id="+depositTransactionId+"");
		// pstmt1.executeUpdate();

		// update st_lms_organization_master for claimable balance
		// for
		// retailer
		CommonFunctionsHelper commHelper = new CommonFunctionsHelper();
		commHelper.updateOrgBalance("CLAIM_BAL", retNetAmt, userBean
				.getUserOrgId(), "DEBIT", con);

		// update st_lms_organization_master for claimable balance
		// for
		// agent
		commHelper.updateOrgBalance("CLAIM_BAL", agentNetAmt, userBean
				.getParentOrgId(), "DEBIT", con);

	} else {
		return false;
	}
	return true;
}

}
