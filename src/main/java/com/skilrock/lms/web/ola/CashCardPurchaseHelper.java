package com.skilrock.lms.web.ola;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

import com.skilrock.lms.beans.CashCardDepositBean;
import com.skilrock.lms.beans.CashCardPurchaseDataBean;
import com.skilrock.lms.beans.OrgPwtLimitBean;
import com.skilrock.lms.beans.UserInfoBean;
import com.skilrock.lms.common.db.DBConnect;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.coreEngine.ola.OlaHelper;
import com.skilrock.lms.coreEngine.ola.common.CommonFunctionsHelper;
import com.skilrock.lms.coreEngine.ola.common.OLAUtility;


public class CashCardPurchaseHelper {
	PreparedStatement pstmt = null;
	PreparedStatement pstmt1 = null;
	PreparedStatement pstmt3 = null;
	
	public synchronized CashCardDepositBean cashCardPurchase(double amount,
			 UserInfoBean userBean, int walletId,
			String depositAnyWhere,
			CashCardDepositBean cashCardDepositBean,
			boolean isPendingData, String rootPath, String userPhone,Date expiryDate,String desKey,String propKey) {
		
			Connection con = DBConnect.getConnection();
		try {
			con.setAutoCommit(false);
			String isBinding = OLAUtility.affiliatePlrBindingRummy(depositAnyWhere,
					cashCardDepositBean.getPartyId(), userBean.getUserName(),userBean.getUserOrgId(), walletId, con); 
			
			if (isBinding.equalsIgnoreCase("OK")) {
			cashCardDepositBean= cashCardSale(cashCardDepositBean.getPartyId(),amount,
						userBean, walletId,cashCardDepositBean, isPendingData, rootPath,con);// LMS Transaction 
				if (!cashCardDepositBean.getReturnType().equalsIgnoreCase(
						"true")) {
					System.out.println(cashCardDepositBean.getReturnType());
					return cashCardDepositBean;
				} else {						
														
					cashCardDepositBean.setCashCardList(getPINfromPinRep(cashCardDepositBean.getCashCardList(),walletId,con));						
					int  isUpdate;
						for(int i=0;i<cashCardDepositBean.getCashCardList().size();i++){
							 isUpdate = cashCardDeposit(cashCardDepositBean.getCashCardList().get(i),walletId,expiryDate,userPhone, con, cashCardDepositBean.getPartyId(), cashCardDepositBean.getTransactionId(),desKey,propKey);
						    if(isUpdate!=1){
						    	cashCardDepositBean.setSuccess(false);
								cashCardDepositBean.setReturnType("Some Error During Pin Purchase");
								return cashCardDepositBean;
						    }
						    	
						}							
				}
				con.commit();
				cashCardDepositBean.setSuccess(true);				
				return cashCardDepositBean;
			}
			
			else {
				cashCardDepositBean.setSuccess(false);
				cashCardDepositBean
						.setReturnType(isBinding);
				return cashCardDepositBean;
				
				
			}	
			

		}
		catch (Exception e) {
			cashCardDepositBean.setReturnType("Some Error");
			e.printStackTrace();	
			
		}finally{
			try {
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();				
				// TODO Auto-generated catch block
				
			}
		}

		return cashCardDepositBean;
	}
	
	
	
	
	public int cashCardDeposit(CashCardPurchaseDataBean cashCardPurchDataBean,int walletId,Date expiryDate,String userPhone,Connection con,String partyId,Long transactionId,String desKey,String propKey) throws Exception {
		int deno = (int)cashCardPurchDataBean.getAmount();
		String tableName = "st_ola_cashcard_rm_"+walletId+ "_"+deno;
		String tableExits = checkTable(tableName,con ); 
		String query =null;
		CashCardPinGeneratorHelper helper = new CashCardPinGeneratorHelper();
		String   pin_nbr=helper.encryptPin(((Long) cashCardPurchDataBean.getPinNbr()).toString(),desKey,propKey);
				
		try {			
				if(!tableExits.equalsIgnoreCase("true"))
					{
						query = "create table "+ tableName+ "(serial_number bigint(16) unsigned NOT NULL,pin_number  varchar(50) NOT NULL,amount decimal(10,2) NOT NULL,expiry_date date NOT NULL,"                      
	                      + " distributor varchar(10) NOT NULL,player_id varchar(50),lms_transaction_id bigint(20) NOT NULL,tp_transaction_id varchar(20) default NULL,verification_date datetime ,"                 
	                     + " verification_status enum('PENDING','DONE') NOT NULL, PRIMARY KEY  (serial_number))";
						pstmt = con.prepareStatement(query);
						pstmt.executeUpdate();
				
					}
			
				pstmt1 = con.prepareStatement("insert into "
					+ tableName + "(serial_number,pin_number,amount,expiry_date,distributor,lms_transaction_id ,verification_status) values(?,?,?,?,?,?,?)");

				pstmt1.setLong(1, cashCardPurchDataBean.getSerialNumber());
				pstmt1.setString(2,pin_nbr);
				pstmt1.setDouble(3, cashCardPurchDataBean.getAmount());
				pstmt1.setDate(4,(java.sql.Date)expiryDate);// convert util date to sql date 
				pstmt1.setString(5, "OLA");// OLA Default distributor 
				pstmt1.setLong(6,transactionId);//lms_transaction_id default 0 
				pstmt1.setString(7, "PENDING");// status default PENDING
				pstmt1.executeUpdate();
				
			return 1;
			
		} catch (Exception e) {
			e.printStackTrace();
			throw new LMSException("Error during deposit");
		} 
		
	}

 public ArrayList<CashCardPurchaseDataBean> getPINfromPinRep(ArrayList<CashCardPurchaseDataBean> cashCardPurchaseDataBean,int walletId,Connection con ) throws LMSException{
		
		if(cashCardPurchaseDataBean.size()>0){
			try{
				
			
			for(int i=0;i<cashCardPurchaseDataBean.size();i++){
				CashCardPinGeneratorHelper helper = new CashCardPinGeneratorHelper();
				//Get the lastGeneratedSerial count and Day Count
				String lastGeneratedSerialDayCount[] = helper.getLastGeneratedPin(walletId,con,"FLEXI");
				HashSet<Long> hPin = new HashSet<Long>();
				List<Long> listSerial = new ArrayList<Long>();	
				String lastGeneratedSerial=lastGeneratedSerialDayCount[0];
				String lastGeneratedDayCount=lastGeneratedSerialDayCount[1];
				listSerial = helper.randomSerial("FLEXI", listSerial, 1,walletId,lastGeneratedSerial,lastGeneratedDayCount);
				hPin = helper.randomPin(hPin,1);// generate 1  pin
				List<Long> listPin = new ArrayList<Long>(hPin);
				System.out.println("SR. Number " + listSerial.get(0)+"Pin Number"+listPin.get(0));
				// update st_ola_pin_generation
				pstmt3 =con.prepareStatement("update st_ola_pin_generation set last_generated_serial_nbr=? where wallet_id =? and pin_type = ? ");
				pstmt3.setString(1,listSerial.get(0).toString().substring(7));
				pstmt3.setInt(2,walletId);
				pstmt3.setString(3,"FIXED");
				pstmt3.executeUpdate();
				cashCardPurchaseDataBean.get(i).setPinNbr(listPin.get(0));
				cashCardPurchaseDataBean.get(i).setSerialNumber(listSerial.get(0));
				
				}
			
			}
			catch(Exception e){
				
				e.printStackTrace();
			}
		
				
		}
		
		
		return cashCardPurchaseDataBean;
	}
	// Added By Neeraj
	public 	CashCardDepositBean   cashCardSale(
			String userName, double depositAmt,
			UserInfoBean userBean, int walletId,CashCardDepositBean cashCardDeposit,
			boolean isPendingData, String rootPath,Connection con) throws LMSException {
		
		
		//Connection con = DBConnect.getConnection();
		OlaHelper olaHelper = new OlaHelper();
		double retailerComm = 0.0;
		double agentComm = 0.0;
		double retNetAmt = 0.0;
		double agentNetAmt = 0.0;
		long imsTransactionId = 0;
		long agentRefTransactionId = 0;
		int isUpdate;
		
		try {
			
			int agentOrgId = userBean.getParentOrgId();
			int retOrgId = userBean.getUserOrgId();

			retailerComm = CommonFunctionsHelper.fetchOLACommOfOrganization(
					walletId, retOrgId, "DEPOSIT", "RETAILER", con);
			agentComm = CommonFunctionsHelper.fetchOLACommOfOrganization(
					walletId, retOrgId, "DEPOSIT", "AGENT", con);

			// check with organizations limit
			CommonFunctionsHelper commonFunction = new CommonFunctionsHelper();
			OrgPwtLimitBean orgPwtLimit = commonFunction
					.fetchPwtLimitsOfOrgnization(retOrgId, con);
			if (orgPwtLimit == null) { // send mail to back office
				System.out.println("OLA Limits Are Not defined Properly!!");
				throw new LMSException("OLA Limits Are Not defined Properly!!");
			}
			double olaDepositLimit = orgPwtLimit.getOlaDepositLimit();
			System.out.println("olaDepositLimit" + olaDepositLimit);
			System.out.println("ola deposite money" + depositAmt);

			if (depositAmt > olaDepositLimit) {
				System.out
						.println("Deposit amount is greater then deposit limit");
				cashCardDeposit
						.setReturnType("Deposit amount is greater then deposit limit");
				return 	cashCardDeposit ;
				// return "Deposit amount is greater then deposit limit";
			}
			// check with retailer and agent balance to deposit
			int isCheck = olaHelper.checkOrgBalance(depositAmt, retOrgId, agentOrgId,
					con, retailerComm, agentComm);
			System.out.println("ischeck" + isCheck);

			if (isCheck == -1) {
				// Agent has insufficient
				cashCardDeposit.setReturnType("Agent has insufficient");
				return 	cashCardDeposit ;
				// return "Agent has insufficient";

			} else if (isCheck == -2) {
				// Error LMS
				cashCardDeposit .setReturnType("Error LMS");
				return 		cashCardDeposit ;
				// return "Error LMS";
			} else if (isCheck == 0) {
				// Retailer has insufficient
				cashCardDeposit 
						.setReturnType("Retailer has insufficient Balance ");
				return 		cashCardDeposit;
				// return "Retailer has insufficient";
			}
			// insert in LMS transaction master
			if (isCheck == 2) {

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
					pstmt1.setTimestamp(5, new java.sql.Timestamp(new Date()
							.getTime()));
					pstmt1.setString(6, "OLA_CASHCARD_SALE");
					isUpdate = pstmt1.executeUpdate();

					// insert in deposit master
					retNetAmt = (depositAmt - ((depositAmt * retailerComm) / 100));
					agentNetAmt = (depositAmt - ((depositAmt * agentComm) / 100));					
					String insertQry = "insert into st_ola_ret_deposit(transaction_id, wallet_id, party_id, retailer_org_id, deposit_amt, retailer_comm, net_amt, agent_comm, agent_net_amt, agent_ref_transaction_id, claim_status, deposit_channel, ims_ref_transaction_id) values(?,?,?,?,?,?,?,?,?,?,?,?,?)";
					PreparedStatement pstmtUpdate = con
							.prepareStatement(insertQry);
					pstmtUpdate.setLong(1, transactionId);
					pstmtUpdate.setInt(2, walletId);
					pstmtUpdate.setString(3, userName);
					pstmtUpdate.setInt(4, userBean.getUserOrgId());
					pstmtUpdate.setDouble(5, depositAmt);
					pstmtUpdate.setDouble(6, retailerComm);
					pstmtUpdate.setDouble(7, retNetAmt);
					pstmtUpdate.setDouble(8, agentComm);
					pstmtUpdate.setDouble(9, agentNetAmt);
					pstmtUpdate.setLong(10, agentRefTransactionId);
					pstmtUpdate.setString(11, "CLAIM_BAL");
					pstmtUpdate.setString(12, "WEB");
					pstmtUpdate.setLong(13, imsTransactionId);
					pstmtUpdate.executeUpdate();

					// update st_lms_organization_master for claimable balance
					// for
					// retailer
					CommonFunctionsHelper commHelper = new CommonFunctionsHelper();
					commHelper.updateOrgBalance("CLAIM_BAL", retNetAmt,
							userBean.getUserOrgId(), "CREDIT", con);

					// update st_lms_organization_master for claimable balance
					// for
					// agent
					commHelper.updateOrgBalance("CLAIM_BAL", agentNetAmt,
							userBean.getParentOrgId(), "CREDIT", con);					
					//con.commit();
					cashCardDeposit.setTransactionId(transactionId);	// transactionId
				}

				else {
					System.out
							.println("Trabsaction Id is not Generated in LMS transaction master");
					cashCardDeposit
							.setReturnType("error in Deposit the money");
					return 	cashCardDeposit;
					// return "error in Deposit the money";
				}
			} else {
				System.out.println("Error During balance verification");
				cashCardDeposit
						.setReturnType("Error During balance verification");
				return 		cashCardDeposit;
				// return "Error During balance verification";
			}

			// con.commit();
		}

		catch (Exception e) {
			e.printStackTrace();
			throw new LMSException("Error during deposit");
		}
		
		cashCardDeposit.setReturnType("true");
		return 		cashCardDeposit;
		// return "true";
	}
	
	public String checkTable(String tableName, Connection con) {

		try {
			ResultSet rs = con.getMetaData().getTables(null, null, tableName,
					null);
			if (rs.next()) {
				return "true";
			}

		}

		catch (SQLException e) {
			e.printStackTrace();
		}

		return "false";
	}
	
	
	
	
}
