package com.skilrock.lms.web.ola;

import java.sql.Blob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.skilrock.lms.beans.OlaPinSalePaymentBean;
import com.skilrock.lms.common.db.DBConnect;
import com.skilrock.lms.common.exception.LMSErrors;
import com.skilrock.lms.common.exception.LMSException;
/**
 * This class provide methods to fetch and approve pin sale payment data 
 * @author Neeraj Jain
 *
 */
public class CalculateOLAPinSaleHelper {
	
	static Log logger =LogFactory.getLog(CalculateOLAPinSaleHelper.class);
/**
 * This method search various denomination tables to find redeemed pins sale amount between selected end date and start date(which can deployment date of last approved record end data for particular wallet) 
 * sale payment records grouped by walletId and sale_comm_rate
 * @param distributorType Pin Distributor
 * @param enDate    Up to which sale payment should be calculated 
 * @param deployDate project deployment date
 * @return ArrayList of OlaPinSalePaymentBean 
 */	
	
	public ArrayList<OlaPinSalePaymentBean> pinSaleData(String distributorType,String enDate,String deployDate){
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat formatDeploy = new SimpleDateFormat("dd-MM-yyyy");
		Connection con = DBConnect.getConnection();
		PreparedStatement ps =null;
		PreparedStatement psMain =null;
		PreparedStatement psInner =null;
		String query = null;
		String queryMain = null;
		String queryInner=null;
		ResultSet rs=null;
		ResultSet rsMain=null;
		ResultSet rsInner=null;
		ArrayList<OlaPinSalePaymentBean> olaPinSalePaymentList = new ArrayList();
		try {
			java.sql.Date taskEndDate = new java.sql.Date(format.parse(enDate).getTime());
			java.sql.Date taskStartDate = new java.sql.Date(formatDeploy.parse(deployDate).getTime());
			java.sql.Date startDate = new java.sql.Date(formatDeploy.parse(deployDate).getTime());
			Calendar cal = Calendar.getInstance();
			cal.setTime(taskEndDate);
			cal.add(Calendar.DAY_OF_YEAR, 1);
			java.sql.Date endDate =  new java.sql.Date(cal.getTime().getTime() );
			
			// select amount ,walletId,walletName from st_ola_pin_status(pin generation record table)
			queryMain="	select amount,ps.wallet_id,generated_for,wm.wallet_display_name from ("+
						"(select group_concat(distinct(amount)) amount,wallet_id,generated_for  from st_ola_pin_status " +
						" where generated_for=? and generation_time<? group by wallet_id )ps inner join st_ola_wallet_master wm  on wm.wallet_id=ps.wallet_id  and wallet_status='ACTIVE')";
			psMain = con.prepareStatement(queryMain);
			psMain.setString(1,distributorType );
			psMain.setDate(2,endDate);
			logger.info(psMain);
			rsMain=psMain.executeQuery();
			int recordId =0;
			int walletId=0;
			String walletName=null;
			while(rsMain.next()){
				String querySub=" ";
				walletId=rsMain.getInt("wallet_id");
				walletName=rsMain.getString("wallet_display_name");
				// Last date of approved/done task for particular wallet and distributor 
				query ="select end_date,wallet_id from (select end_date,wallet_id from st_ola_pin_sale_task  where distributor=? and wallet_id=? order by end_date desc )as st group by wallet_id ";
				ps =con.prepareStatement(query);
				ps.setString(1,distributorType);
				ps.setInt(2,walletId);
				rs = ps.executeQuery();
				logger.info("ps: "+ps);
				if(rs.next()){
					cal.setTime(rs.getDate("end_date"));
					cal.add(Calendar.DAY_OF_YEAR, 1);
					taskStartDate =  new java.sql.Date(cal.getTime().getTime() );
					querySub=" and verification_date>='"+taskStartDate+"'";
				}
				else{
					taskStartDate=startDate;
					querySub=" and verification_date>='"+startDate+"' ";
				}
				// get all amount for which pin has been generated
				Blob blob =rsMain.getBlob("amount");
				byte[]data =blob.getBytes(1,(int)blob.length() );
				String [] denoType=new String(data).split(",");
				logger.info("walletId: "+walletId+" denominations "+new String(data));
				HashMap<Double,Double> totalAmountMap =new HashMap<Double, Double>();
				HashMap<Double,Double> netAmountMap =new HashMap<Double, Double>();
				HashSet<Double> commRateList= new HashSet<Double>();
				double totalAmount=0.0;	
				double netAmount=0.0;
				double commRate=0.0;
				for(int i=0;i<denoType.length;i++){
					String tableName ="st_ola_cashcard_rm_"+walletId+"_"+denoType[i];// denomination table
					// calculate sale payment amount grouped by wallet and commission rate
					queryInner = " select sum(aa.amount) totalamount,sum(aa.amount-((aa.amount*sale_comm_rate)/100)) netamount,bb.wallet_id,sale_comm_rate  from("
						+ " (select sum(amount) amount,generation_id from "
						+tableName
						+ " where  verification_status=? and distributor=? and verification_date<?"				
						+ querySub
						+ " group by generation_id)aa "
						+ " inner join (select sale_comm_rate,generation_id,generated_for,wallet_id  from st_ola_pin_status)bb on aa.generation_id =bb.generation_id )group by bb.generated_for,sale_comm_rate " ;
					psInner =con.prepareStatement(queryInner);
					psInner.setString(1,"DONE");
					psInner.setString(2,distributorType);
					psInner.setDate(3,endDate);
					rsInner =psInner.executeQuery();
					logger.info(psInner);
					while(rsInner.next()){
						// make map of total amount and net amount taking commission rate as key object 
						totalAmount=rsInner.getDouble("totalamount");
						netAmount=rsInner.getDouble("netamount");
						commRate=rsInner.getDouble("sale_comm_rate");
						commRateList.add(commRate);
							if(totalAmountMap.size()>0){
								
								if(totalAmountMap.get(rsInner.getDouble("sale_comm_rate"))!=null){
									
									totalAmountMap.put(commRate,totalAmount+totalAmountMap.get(commRate));
									netAmountMap.put(commRate,netAmount+netAmountMap.get(commRate));
								}
								else{
									totalAmountMap.put(commRate,totalAmount);
									netAmountMap.put(commRate,netAmount);
								}
								
							}
							else{
								totalAmountMap.put(commRate,totalAmount);
								netAmountMap.put(commRate,netAmount);
							}
					
					}
				}
				//prepare pinsalepaymentBean list
				if(commRateList.size()>0&& (totalAmountMap.size()==netAmountMap.size())){
					ArrayList<Double> commRateArray=new ArrayList<Double>(commRateList);
					for(int i=0;i<commRateArray.size();i++){
						recordId++;
						OlaPinSalePaymentBean pinSalePaymentBean= new OlaPinSalePaymentBean();	
						pinSalePaymentBean.setWalletId(rsMain.getInt("wallet_id"));
						pinSalePaymentBean.setDistributor(distributorType);
						pinSalePaymentBean.setTaskId(recordId);
						pinSalePaymentBean.setStartDate(taskStartDate);
						pinSalePaymentBean.setEndDate(taskEndDate);
						pinSalePaymentBean.setTotalAmount(totalAmountMap.get(commRateArray.get(i)));
						pinSalePaymentBean.setNetAmount(netAmountMap.get(commRateArray.get(i)));
						pinSalePaymentBean.setCommRate(commRateArray.get(i));
						pinSalePaymentBean.setWalletName(walletName);
						olaPinSalePaymentList.add(pinSalePaymentBean);
						
					}
					
				}
				else{
					continue;
				}
				
			}
				
		}catch(Exception e){
			e.printStackTrace();
		}finally {
			try {
				con.close();
				logger.info("Connection Closed");
			} catch (SQLException e) {
				e.printStackTrace();

			}
		}
		
		return olaPinSalePaymentList;
		
	}
/**
 * this method save approved pin sale payment
 * this method also validate that all pin sale payment of particular wallet should approved together
 * @param olaPinSalePaymentBeanList
 * @param check
 * @return boolean 
 * @throws LMSException 
 */	
	public boolean saveApprovedData(
			ArrayList<OlaPinSalePaymentBean> olaPinSalePaymentBeanList,
			int[] check) throws LMSException {

		Connection con = DBConnect.getConnection();
		HashSet<String > checkedWalletMap= new HashSet<String>();//walletName which are approved
		HashSet<String > uncheckedWalletMap= new HashSet<String>();// walletName which are not approved
		try {
			 			con.setAutoCommit(false);
			 // Insert approve payment			
			String queryInsert = "insert into st_ola_pin_sale_task(wallet_id,distributor,start_date,end_date,total_amount,sale_comm_rate,net_amount,approve_status) values(?,?,?,?,?,?,?,?)";
			
		
			for (int n = 0; n < olaPinSalePaymentBeanList.size(); n++) {
				OlaPinSalePaymentBean pinSalePaymentBean = olaPinSalePaymentBeanList
				.get(n);
				
			boolean isChecked =false;	// to check that pinSalePaymentBean is approved or not
				for (int i = 0; i < check.length; i++) {
					System.out.println(check[i]);
								
					if (pinSalePaymentBean.getTaskId() == check[i]) {
						
						PreparedStatement ps1 = con.prepareStatement(queryInsert);
						ps1.setInt(1, pinSalePaymentBean.getWalletId());
						ps1.setString(2, pinSalePaymentBean.getDistributor());
						ps1.setDate(3, pinSalePaymentBean.getStartDate());
						ps1.setDate(4, pinSalePaymentBean.getEndDate());
						ps1.setDouble(5, pinSalePaymentBean.getTotalAmount());
						ps1.setDouble(6, pinSalePaymentBean.getCommRate());
						ps1.setDouble(7, pinSalePaymentBean.getNetAmount());
						ps1.setString(8, "APPROVED");
						ps1.executeUpdate();
						logger.info("checkID:"+check[i]+"ps1"+ps1);
						isChecked=true;

					}
					
					
				}
				
				if(isChecked){
					if(uncheckedWalletMap.size()>0){
						ArrayList<String> walletArr = new ArrayList<String>(uncheckedWalletMap) ;
						for(int k=0;k<walletArr.size();k++){
							if(walletArr.get(k).equalsIgnoreCase(pinSalePaymentBean.getWalletName())){
								throw new LMSException(0,"all pin sale payment of particular wallet should approved together");
							}
						}
					}
					checkedWalletMap.add(pinSalePaymentBean.getWalletName());
				}
				else{
					if(checkedWalletMap.size()>0){
						ArrayList<String> walletArr = new ArrayList<String>(checkedWalletMap) ;
						for(int k=0;k<walletArr.size();k++){
							if(walletArr.get(k).equalsIgnoreCase(pinSalePaymentBean.getWalletName())){
								throw new LMSException(0,"all pin sale payment of particular wallet should approved together");
							}
						}
					}	
					uncheckedWalletMap.add(pinSalePaymentBean.getWalletName());
				}
				
			

			}
		
			con.commit();
			return true;

		} catch (LMSException e) {
			throw e;
		}catch (Exception e) {
			e.printStackTrace();
			throw new LMSException(LMSErrors.GENERAL_EXCEPTION_ERROR_CODE,LMSErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		} finally {
			try {
				if (con != null) {
					con.close();
					logger.info("connection closed");
				}
			} catch (Exception e) {

			}
		}

	}

	
}
