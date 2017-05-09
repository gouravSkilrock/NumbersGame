package com.skilrock.lms.api.ola;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.skilrock.lms.common.CommonValidation;
import com.skilrock.lms.common.db.DBConnect;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.coreEngine.ola.OlaHelper;
import com.skilrock.lms.coreEngine.ola.OlaRummyWithRequestHelper;
import com.skilrock.lms.coreEngine.ola.common.OLAUtility;
import com.skilrock.lms.web.ola.CashCardPinGeneratorHelper;
import com.skilrock.lms.api.ola.beans.OlaRummyDepositBean;
import com.skilrock.lms.api.ola.beans.OlaWithdrawlRequestBean;
import com.skilrock.lms.api.ola.beans.OlaWithdrwalDetailsBean;

public class OlaServiceApiHelper {
	static Log logger = LogFactory.getLog(OlaServiceApiHelper.class);
	public OlaRummyDepositBean verifyUserRefId(OlaRummyDepositBean olaServiceBean,String ip,String desKey,String propKey) throws Exception {
		Connection con = DBConnect.getConnection();
		CashCardPinGeneratorHelper helper = new CashCardPinGeneratorHelper();
		con.setAutoCommit(false);
		String plrId = olaServiceBean.getPlayerId();
		String serialNumber =olaServiceBean.getSerialNumber();
		String pinNumber=helper.encryptPin(olaServiceBean.getOlaPIN(),desKey,propKey); // encrypt Pin 
  		String userName = olaServiceBean.getUserName();
		String password = olaServiceBean.getPassword();
		String tpTransId =olaServiceBean.getRefTransId();
		double amount =olaServiceBean.getDepositeAmount();
		String query = null;
		String query1 =null;
		Calendar cal = Calendar.getInstance();
		Timestamp currentDate = null;
		currentDate = new Timestamp(cal.getTime().getTime());
		java.sql.Date dateNow =	new java.sql.Date(currentDate.getTime());
		int walletId = getWalletId(userName,password,ip,con);// check for userName,Password,Ip
		int deno = (int)amount;
			if(walletId==-1){
				olaServiceBean.setErrorCode(500);	
				olaServiceBean.setValidDeposit(false);
			logger.info("Some Internal Exception");
				return olaServiceBean;
						}
			else if(walletId==0){
				olaServiceBean.setErrorCode(102);	//Authentication Error
				olaServiceBean.setValidDeposit(false);
				logger.info("Authentication Error Username or Password or IP doesnot match");
				return olaServiceBean;
			}
		
		if(olaServiceBean.getDepositType().equalsIgnoreCase("OLA")){
				query = "select verification_status,lms_transaction_id  from st_ola_pin_rep_rm_2 where " +
						" serial_number='" +serialNumber+"' and  pin_number='"+pinNumber+"'and amount="+amount+" and player_id  ='"
						+ plrId + "' and expiry_date>='"+dateNow+"'";
				query1=" update st_ola_pin_rep_rm_2 set verification_status='DONE',tp_transaction_id='"+tpTransId+"', verification_date ='"+currentDate+"'where " +
						" serial_number='" +serialNumber+"' and  pin_number='"+pinNumber+"'and amount="+amount+" and player_id  ='"
						+ plrId + "'";
				}else if(olaServiceBean.getDepositType().equalsIgnoreCase("PIN")){
					String tableName = 	"st_ola_cashcard_rm_"+walletId+"_"+deno+"";
					boolean tableExits = checkTable(tableName,con ); 
						if(!tableExits){
											olaServiceBean.setErrorCode(500);	
											olaServiceBean.setValidDeposit(false);
											logger.info("Some Internal Error::Table doesnot exits ");
											return olaServiceBean;
									}
					query = "select verification_status,generation_id,lms_transaction_id from st_ola_cashcard_rm_"+walletId+"_"+deno+" where serial_number='" +serialNumber+"' and  pin_number ='"+pinNumber+"'and amount="+amount+" and expiry_date>='"+dateNow+"'";
					query1=" update st_ola_cashcard_rm_"+walletId+"_"+deno+" set verification_status='DONE',tp_transaction_id='"+tpTransId+"',player_id='"+plrId+"',verification_date ='"+currentDate+"' where  serial_number='" +serialNumber+"' and  pin_number='"+pinNumber+"'and amount="+amount+"";
				
				}else{
						olaServiceBean.setErrorCode(500);	
						olaServiceBean.setValidDeposit(false);
						logger.info("Some Internal Error ");
						return olaServiceBean;
						
					}
				
			try {
				PreparedStatement ps = con.prepareStatement(query);
				ResultSet rs1 = ps.executeQuery();
				if (rs1.next()) {
						if(rs1.getString("verification_status").equalsIgnoreCase("PENDING"))
						{						
							PreparedStatement ps1 = con.prepareStatement(query1);
							ps1.executeUpdate();
							if(rs1.getInt("lms_transaction_id")==0){
								//Update Pin Status 
								boolean isUpdate = updatePinStatus(con,rs1.getInt("generation_id"));
								if(!isUpdate){
									olaServiceBean.setErrorCode(500);		//Some Error;	
									logger.info("Some Internal Error in Pin Status Update");
									olaServiceBean.setValidDeposit(false);
									return olaServiceBean;
								}
							}
							
							con.commit();
							olaServiceBean.setErrorCode(100);// Success
							olaServiceBean.setValidDeposit(true);
							return olaServiceBean;
						}
						else if(rs1.getString("verification_status").equalsIgnoreCase("DONE")){							
							olaServiceBean.setErrorCode(101);	//Invalid Deposit;
							olaServiceBean.setValidDeposit(false);
							return olaServiceBean;
						}
						else {
							olaServiceBean.setErrorCode(500);		//Some Error;	
							logger.info("Some Internal Error ");
							olaServiceBean.setValidDeposit(false);
							return olaServiceBean;
						}				
				}		
				else {
					olaServiceBean.setErrorCode(101);	    // Invalid Deposit
					logger.info("Invalid Deposit");
					olaServiceBean.setValidDeposit(false);	//verify = "declined";
					return olaServiceBean;
				}	
			}	
			catch (Exception e){
				e.printStackTrace();
				logger.info("Some Internal Exception ");
				olaServiceBean.setErrorCode(500);// Some Error
				olaServiceBean.setValidDeposit(false);	//verify = "declined";
				return olaServiceBean;
				
			}
		
}
	
	private int getWalletId(String userName,String password,String ip,Connection con){
		String query = "select wallet_id,system_ip from st_ola_wallet_authentication_master where system_user_name=? and system_password=? ";
	try {
			PreparedStatement ps1 = con.prepareStatement(query);
			String validIPs[] ;
			ps1.setString(1,userName);
			ps1.setString(2,password);
			logger.info("Authentication Query"+ps1);
			ResultSet rs2 = ps1.executeQuery();
		if(rs2.next()){
					validIPs = rs2.getString("system_ip").split(",");
					for(int i=0;i<validIPs.length;i++){
						if(ip.equalsIgnoreCase(validIPs[i])){
								logger.info("Address "+ip+" is authorized ");
								return rs2.getInt("wallet_id");
						}
						
					}
					logger.info("Address "+ip+" is not authorized ");	
				return 0;	
			}
		else {
			return 0;
		}
		}catch(Exception e) {
			e.printStackTrace();
			return -1;
		}
		
	}
	
	public boolean checkTable(String tableName, Connection con) {

		try {
			ResultSet rs = con.getMetaData().getTables(null, null, tableName,null);
			if (rs.next()) {
				return true;
			}
		}
		catch (SQLException e) {
			e.printStackTrace();
			return false;
		}

		return false;
	}
   public boolean updatePinStatus(Connection con,int generationId){
	   String query ="update st_ola_pin_status set no_of_pin_redeemed=no_of_pin_redeemed+1 where generation_id="+generationId;
		   try{
			   PreparedStatement ps = con.prepareStatement(query);
			   int update= ps.executeUpdate();
			   if(update==1){
				   return true;
			   }
		   }catch(Exception e){
			e.printStackTrace();     
		   }

	   return false;
   }
   
	public OlaWithdrawlRequestBean processWithdrawalRequest(OlaWithdrawlRequestBean olaServiceBean,String ip) throws Exception {
		Connection con = null;
		PreparedStatement ps=null;
		String userName = olaServiceBean.getUserName();
		String password = olaServiceBean.getPassword();
		boolean isProcessedFlag =false; //falg to check that atleast one withdrwal request processed
		ResultSet rs =null;
	try {
			con = DBConnect.getConnection();
			// check for userName,Password,Ip
			int walletId = getWalletId(userName,password,ip,con);
			
			if(walletId==-1){
					olaServiceBean.setErrorCode(500);	
					olaServiceBean.setSuccess(false);
					olaServiceBean.setErrorMsg("Internal Server Error  In Verification");
					logger.info("Internal Server Error  In Verification");
					return olaServiceBean;
					}
			else if(walletId==0){
					olaServiceBean.setErrorCode(102);	//Authentication Error
					olaServiceBean.setSuccess(false);
					olaServiceBean.setErrorMsg("Authentication Error Username or Password or IP doesnot match");
					logger.info("Authentication Error Username or Password or IP doesnot match");
					return olaServiceBean;
				}
			con.setAutoCommit(false);
			double amount=0;
			for(OlaWithdrwalDetailsBean olaWithDetailsBean : olaServiceBean.getWithdrawalDetailList()){
				olaWithDetailsBean.setSuccess(false);
				String requestId =olaWithDetailsBean.getRequestId();
				amount = olaWithDetailsBean.getPlrAmount();
				String plrUserName = olaWithDetailsBean.getUserName();
				String walletName =OLAUtility.getWalletBean(OLAUtility.getWalletIntBean(olaWithDetailsBean.getWalletName()).getWalletId()).getWalletDevName();	
				String phonNbr =olaWithDetailsBean.getPlrPhoneNbr();
				// Validate Data
				
				if(CommonValidation.isEmpty(plrUserName)){
					logger.info("IllegalArgument plrUserName  In Wihtdrawal Request Processing ");
					olaServiceBean.setErrorCode(500);
					olaServiceBean.setSuccess(false);
					olaServiceBean.setErrorMsg("Illegal UserName");
					return olaServiceBean;
				}
				if(CommonValidation.isEmpty(walletName)){
					logger.info("IllegalArgument WalletName In Wihtdrawal Request Processing ");
					olaServiceBean.setErrorCode(500);
					olaServiceBean.setSuccess(false);
					olaServiceBean.setErrorMsg("Illegal WalletName");
					return olaServiceBean;
				}
				if(CommonValidation.isEmpty(requestId)){
					logger.info("Illegal Request Id Wihtdrawal Request  ");
					olaServiceBean.setErrorCode(500);
					olaServiceBean.setSuccess(false);
					olaServiceBean.setErrorMsg("Illegal Request Id");
					return olaServiceBean;
				}
				if(amount<=0){
					
					logger.info("IllegalArgument Withdrawal Amount  In Wihtdrawal Request ");
					olaServiceBean.setErrorCode(500);
					olaServiceBean.setSuccess(false);
					olaServiceBean.setErrorMsg("Invalid Withdrawal Amount");
					return olaServiceBean;
					
				}
				if(!CommonValidation.isValidPhoneNumber(phonNbr)){
					logger.info("IllegalArgument Phone Number  In Wihtdrawal Request  ");
					olaServiceBean.setErrorCode(500);
					olaServiceBean.setSuccess(false);
					olaServiceBean.setErrorMsg("Invalid Phone Number ");
					return olaServiceBean;
				}
				
				String	query ="select request_id from st_ola_rummy_withdrawal_rep where request_id=?";
				ps = con.prepareStatement(query);
				ps.setString(1,requestId);
				rs = ps.executeQuery();
				logger.info("Duplicate Req Id Query"+query);
				if(rs.next()){
					olaWithDetailsBean.setSuccess(false);
					olaWithDetailsBean.setErrorCode(103);
					olaWithDetailsBean.setErrorMsg("Duplicate Withdrawal Request");
					continue;
				}
				boolean isOlaBind =OlaHelper.checkPlrBinding(con,olaWithDetailsBean.getUserName(),olaWithDetailsBean.getWalletName());
				if(!isOlaBind){
					olaWithDetailsBean.setSuccess(false);
					olaWithDetailsBean.setErrorCode(104);
					olaWithDetailsBean.setErrorMsg("Player Not Present In Ola System");
					continue;
				}
				query = "insert into st_ola_rummy_withdrawal_rep(account_id,user_id,wallet_name,request_id,isBind,request_date,plr_email,plr_phone,plr_banking_name,bank_name,bank_account_nbr,bank_branch_name,bank_branch_city,ifs_code,amount,transfer_mode,approval_date,rms_process_status) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
				
				ps = con.prepareStatement(query);
				ps.setString(1,olaWithDetailsBean.getPlrId());
				ps.setString(2,plrUserName.trim());
				ps.setString(3,olaWithDetailsBean.getWalletName());
				ps.setString(4,requestId.trim());
				ps.setString(5,"YES");
				ps.setDate(6,olaWithDetailsBean.getRequestDate() );
				ps.setString(7,olaWithDetailsBean.getPlrEmail());
				ps.setString(8,phonNbr.trim());
				ps.setString(9,"NA");
				ps.setString(10,"NA");
				ps.setString(11,"NA");
				ps.setString(12,"NA");
				ps.setString(13,"NA");
				ps.setString(14,"NA");
				ps.setDouble(15,amount);
				ps.setString(16,"ONLINE");
				ps.setDate(17,olaWithDetailsBean.getApproveDate() );
				ps.setString(18,"PENDING");
				logger.info("Withdrawal Request Query"+ps);
				int insertedRow =ps.executeUpdate();
				if(insertedRow!=1){
					olaWithDetailsBean.setSuccess(false);
					olaWithDetailsBean.setErrorCode(500);
					olaWithDetailsBean.setErrorMsg("Internal Server Error While Processing This Withdrawal Request  ");
					continue;
				}
				con.commit();
				isProcessedFlag=true;
				olaWithDetailsBean.setSuccess(true);
				olaWithDetailsBean.setErrorCode(100);
				olaWithDetailsBean.setErrorMsg(" Withdrawal Request  Processed Successfully");
				
				
			}
		
		}catch (Exception e){
			
			e.printStackTrace();
			logger.info("Internal Server Error  In Wihtdrawal Request Processing ");
			olaServiceBean.setErrorCode(500);
			olaServiceBean.setSuccess(false);
			olaServiceBean.setErrorMsg("Internal Server Error  In Wihtdrawal Request Processing ");
			return olaServiceBean;
			
	}finally{
			
			try{
				
				if(con!=null){
					
					con.close();
				}
				if(rs!=null){
					
					rs.close();
				}
				if(ps!=null){
	
					ps.close();
				}
			}catch(Exception e){
				e.printStackTrace();
				
			}
			
		}
	if(isProcessedFlag){
		ExecutorService executorService = Executors.newSingleThreadExecutor();

		executorService.execute(new Runnable() {
		    public void run() {
		    	try {
					OlaRummyWithRequestHelper.processWithRequest();
				} catch (LMSException e) {
						e.printStackTrace();
				}
		    }
		});
		    
		executorService.shutdown();

	
	}
		olaServiceBean.setErrorCode(100);	
		olaServiceBean.setSuccess(true);
		olaServiceBean.setErrorMsg("Withdrawal Request Processed Successfully");
		return olaServiceBean;
		
}

}
