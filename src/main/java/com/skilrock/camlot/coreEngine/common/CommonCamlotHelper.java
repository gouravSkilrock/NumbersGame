package com.skilrock.camlot.coreEngine.common;

import java.math.BigInteger;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.NumberFormat;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.cssl.ctp.il.xsd.infra_v1.EntryMethodType;
import com.cssl.ctp.il.xsd.infra_v1.PaymentMethodType;
import com.cssl.ctp.il.xsd.infra_v1.TransactionTypeType;
import com.skilrock.cs.beans.CamlotAvailBean;
import com.skilrock.cs.beans.CamlotFaultBean;
import com.skilrock.cs.beans.CamlotSOAPHeaderBean;
import com.skilrock.cs.beans.CamlotSaleBean;
import com.skilrock.lms.ajax.AjaxRequestHelper;
import com.skilrock.lms.beans.CSSaleBean;
import com.skilrock.lms.beans.UserInfoBean;
import com.skilrock.lms.common.db.DBConnect;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.common.utility.FormatNumber;
import com.skilrock.lms.common.utility.PropertyLoader;
import com.skilrock.lms.coreEngine.commercialService.common.CSSaleTransactionsHelper;
import com.skilrock.lms.coreEngine.commercialService.common.CSUtil;
import com.skilrock.lms.embedded.drawGames.common.EmbeddedErrors;
public class CommonCamlotHelper {
	private static Log logger = LogFactory.getLog(CommonCamlotHelper.class);
	private boolean saleSuccessFlag = false;
	private  static String CTPOutletID;
	private  static String CTPUniqueID;
	private  static String CTP_CurrCode;
	private  static String CTP_Locale;
	static{
		
		PropertyLoader.loadProperties("config/LMS.properties");		
		//added by yogesh
		CTPOutletID = PropertyLoader.getProperty("CTPOutletID");
		CTPUniqueID = PropertyLoader.getProperty("CTPUniqueID");
		CTP_CurrCode=PropertyLoader.getProperty("CTP_CurrCode");
		CTP_Locale =PropertyLoader.getProperty("CTP_Locale");
	}
	
	public String camlotSale(String product, String retailerid, String userName, String operatorCode, double denomination, double rechargeAmt, int bulkqty,String mobileNum,long lastTransId) throws Exception{
		StringBuilder finalResp = new StringBuilder();
		CamlotSaleBean cbean = new CamlotSaleBean();
		CamlotAvailBean availBean = new CamlotAvailBean();
		CamlotSOAPHeaderBean headerBean = new CamlotSOAPHeaderBean();
		CamlotFaultBean fault = new CamlotFaultBean();
		cbean.setFault(fault);
		CSSaleBean csbean = new CSSaleBean();
		CSSaleTransactionsHelper sale = new CSSaleTransactionsHelper();
		if(Double.compare(rechargeAmt, 0D) == 0){
			cbean.setAmount(denomination*bulkqty);  // case of non-flexi
			cbean.setCategoryId(fetchCategoryId(product,operatorCode,"*",denomination));
			csbean.setDenomination(cbean.getAmount());
		}else{
			cbean.setAmount(rechargeAmt);   // case of flexi
			cbean.setCategoryId(fetchCategoryId(product,operatorCode,"*",0.00));
			csbean.setDenomination(0.00);
		}
		if(cbean.getCategoryId() == 0){
			finalResp.append("ErrorMsg:Inavlid Product|");
			return finalResp.toString();
		}
		System.out.println("sale Success");
		//csbean.setBalance(balance);
		csbean.setProdCode(product);
		csbean.setCategoryId(cbean.getCategoryId());
		csbean.setCircleCode("*"); //hardcoded untill now
		csbean.setCSRefTxId(0); //Before sending request to CAMLOT
		
		csbean.setMrpAmt((double)cbean.getAmount());
		csbean.setMult(1);     //hardcoded untill now
		csbean.setOperatorCode(operatorCode);
		csbean.setRetOrgId(Integer.parseInt(retailerid));
		csbean.setUnitPrice(cbean.getAmount());
		csbean.setUserName(userName);
		//Check Last Successful Transaction

		if(lastTransId !=0){
			if(!(lastTransId+"").equalsIgnoreCase(CSUtil.fetchLastCSTransId(csbean.getRetOrgId()))){
				return "ErrorMsg:Reprint last voucher|ErrorCode:02";
			}
		}
		csbean = sale.CommServSaleBalDeduction(csbean);
		if(csbean.getErrorCode() != 100){
			Class name = EmbeddedErrors.class;
			finalResp.append("ErrorMsg:"+(String)name.getDeclaredField("ERROR_"+csbean.getErrorCode()).get(null)+ "|");
			return finalResp.toString(); 
		}else{
			//cbean.setMobileNum(new BigInteger(mobileNum+"",10));
			cbean.setProductId(product.split("-")[0]);
			//headerBean.setCTPOutletID("101001");  // hard coded untill now: CTP Outlet Id
			headerBean.setCTPOutletID(CTPOutletID);
			//headerBean.setUniqueID("67b8c439add16df4280faf529963d3927f94a51d");     // hard coded untill now: Unique Id
			headerBean.setUniqueID(CTPUniqueID);   
			headerBean.setRetailerStoreID(retailerid);
			headerBean.setTransactionType(TransactionTypeType.SALE);
			headerBean.setPaymentMethod(PaymentMethodType.CASH);
			headerBean.setEntryMethod(EntryMethodType.MAGNETIC_SWIPE);
			headerBean.setClientRequestID(csbean.getRMSRefId()+"");
			headerBean.setMessageSequenceID(csbean.getRMSRefId()+"");
			headerBean.setTimeStamp("");  // timestamp need to be set
			//headerBean.setLocale("en_TZ"); //hardcoded untill now
			headerBean.setLocale(CTP_Locale);
			cbean.setHeader(headerBean);
			cbean.setMobileNum(mobileNum);
			//cbean.setCurrCode("TZS");
			cbean.setCurrCode(CTP_CurrCode);
			//checking availability on each sale
			availBean.setHeader(headerBean);
			availBean.setVerbose(true);
			
			try{
				//availBean = new CamlotAvailabilityHelper(availBean).checkServiceAvailabilty();
				/*checking service availability*/
				//if(availBean.getFault().getCode().equalsIgnoreCase("Service Unavailable") && availBean.isAvailable()){
					System.out.println("Availability Success");
					if(Double.compare(csbean.getDenomination(),0.0) != 0){ //voucher type (non-flexible)
						try{
							cbean.getHeader().setMessageTypeID("MobileVoucherTopUp");
									new CamlotMobileVTUSaleHelper(cbean).saleMobileVTU();
						}catch(Exception e){
							e.printStackTrace();
						}
						if(cbean.getFault().getCode().equalsIgnoreCase("2010")&& cbean.getFault().getMessage().equalsIgnoreCase("Mobile voucher top up request successful.")){
							CommonCamlotHelper.updatePWTransId(cbean.getHeader().getClientRequestID().toString(), cbean.getHeader().getOriginalTransactionID().toString(), cbean.getCategoryId());
							CSUtil.updateLastCSTransId(csbean.getRetOrgId(), csbean.getRMSRefId());
							CSUtil.insertIntoCamlotTansactionLog(csbean.getRMSRefId(), cbean.getHeader().getOriginalTransactionID(),cbean.getProviderTransactionRef() , cbean.getPINNumber().toString(), cbean.getExpiryDate(), cbean.getMobileNum());
							formatCamlotSaleResponse(csbean, cbean, csbean.getBalance(), finalResp);
							logger.debug(finalResp.toString());
							saleSuccessFlag = true;
							return finalResp.toString();
							
						}else{//Response from Camlot Sale is not SUCCESS
							//send error in cbean that some sale service fault occured while transaction @ camlot
							String faultmsg = cbean.getFault().getMessage();
							if(faultmsg.length()>20){
								finalResp.append("ErrorMsg:"+ faultmsg.substring(0, 20) +"|");
							}else{
								finalResp.append("ErrorMsg:"+ faultmsg +"|");
							}
							
						}
						/*}else{// Response from CAMLOT Availability is False
								//send error in cbean that the service is unavailable
								finalResp.append("ErrorMsg:"+ cbean.getFault().getMessage() +"|");
						}*/
						if(!saleSuccessFlag){
							csbean.setCSRefTxIdForRefund(0);
							csbean.setRMSRefIdForRefund(csbean.getRMSRefId());
							csbean.setReasonForCancel("cancel_server");
							csbean = sale.CommServSaleBalDeduction(csbean);
							System.out.println("CSErrorCode:"+csbean.getErrorCode());
						}
						return finalResp.toString();
					}else if(Double.compare(csbean.getDenomination(),0.0) == 0){   // For Electronic Top Up (Flexible)
						try{
							cbean.getHeader().setMessageTypeID("MobileElectronicTopUp");
								new CamlotMobileETUSaleHelper(cbean).saleMobileETU();
						 }catch(Exception e){
						 	e.printStackTrace();
						 }
						 if(cbean.getFault().getCode().equalsIgnoreCase("2020")){
							 CommonCamlotHelper.updatePWTransId(cbean.getHeader().getClientRequestID().toString(), cbean.getHeader().getOriginalTransactionID().toString(), cbean.getCategoryId());
							 CSUtil.updateLastCSTransId(csbean.getRetOrgId(), csbean.getRMSRefId());
							 CSUtil.insertIntoCamlotTansactionLog(csbean.getRMSRefId(), cbean.getHeader().getOriginalTransactionID(),cbean.getProviderTransactionRef() , null, null, cbean.getMobileNum());
							 finalResp.append(csbean.getRMSRefId()+"%$");
							 finalResp.append(csbean.getBalance()+"%$");
							finalResp.append(cbean.getHeader().getOriginalTransactionID()+"%$");
							finalResp.append((new java.sql.Timestamp(csbean.getTransTime().getTime())).toString().split("\\.")[0]+"%$");
							finalResp.append(cbean.getMobileNum()+"%$");
							finalResp.append(FormatNumber.formatNumber(cbean.getAmount())+"|");
							
							 logger.debug(finalResp.toString());
							 saleSuccessFlag = true;
							return finalResp.toString();
						 }else{
							 String faultmsg = cbean.getFault().getMessage();
								if(faultmsg.length()>20){
									finalResp.append("ErrorMsg:"+ faultmsg.substring(0, 20) +"|");
								}else{
									finalResp.append("ErrorMsg:"+ faultmsg +"|");
								}
						 }
						 if(!saleSuccessFlag){
								csbean.setCSRefTxIdForRefund(0);
								csbean.setRMSRefIdForRefund(csbean.getRMSRefId());
								csbean.setReasonForCancel("cancel_server");
								csbean = sale.CommServSaleBalDeduction(csbean);
								System.out.println("CSErrorCode:"+csbean.getErrorCode());
							}
						 return finalResp.toString();
					}
			}catch(Exception e){
				e.printStackTrace();
				csbean.setCSRefTxIdForRefund(0);
				csbean.setRMSRefIdForRefund(csbean.getRMSRefId());
				csbean.setReasonForCancel("cancel_server");
				csbean = sale.CommServSaleRefund(csbean);
				logger.debug("CSErrorCode:"+csbean.getErrorCode());
				String errorMsg = e.getLocalizedMessage();
				if(errorMsg.length()>20){
					finalResp.append("ErrorMsg:"+e.getLocalizedMessage().substring(0, 20)+ "|");
				}else{
					finalResp.append("ErrorMsg:"+e.getLocalizedMessage()+ "|");
				}
			}
			return finalResp.toString();
		}
			
	}
	
	public static boolean updatePWTransId(String RMSTxId, String CamlotTxId, int categoryId)throws LMSException{
		boolean status = false;
		Connection con = DBConnect.getConnection();
		try{
			Statement stmt = con.createStatement();
			String query = "update st_cs_sale_"+categoryId+" set cs_ref_transaction_id='"+CamlotTxId+"' where transaction_id="+RMSTxId;
			logger.debug("update transaction id got from Camlot: "+query);
			int rows = stmt.executeUpdate(query);
			if(rows == 1){
				status = true;
			}
		}catch (SQLException e) {
			logger.error("Exception: " + e);
			e.printStackTrace();
			throw new LMSException(e);
		} finally {
			try {
				if (con != null && !con.isClosed()) {
					con.close();
				}
			} catch (SQLException e) {
				logger.error("Exception: " + e);
				e.printStackTrace();
				throw new LMSException(e);
			}
		}
		return status;
	}
	
	private void formatCamlotSaleResponse(CSSaleBean bean, CamlotSaleBean camlotbean, double balance, StringBuilder resp)throws Exception {
		resp.append(camlotbean.getHeader().getClientRequestID()+"%$");
		NumberFormat nFormat = NumberFormat.getInstance();
		nFormat.setMinimumFractionDigits(2);
		resp.append(nFormat.format(balance).replaceAll(",", "")+"%$");
		resp.append(camlotbean.getHeader().getOriginalTransactionID()+"%$");
		resp.append((new java.sql.Timestamp(bean.getTransTime().getTime())).toString().split("\\.")[0]+"%$");
		resp.append(""+"%$");
		resp.append(camlotbean.getPINNumber()+"%$");
		resp.append(camlotbean.getExpiryDate()+"|");
		
		/*Map<String, List<String>> advMap= Util.getAdvMessage(bean.getRetOrgId(),	bean.getCategoryId(), "PLAYER", "SALE", "CS");
		
		StringBuilder topMsgsStr = new StringBuilder("");
		StringBuilder bottomMsgsStr = new StringBuilder("");
		String advtMsg = "";

		UtilApplet.getAdvMsgs(advMap, topMsgsStr,
				bottomMsgsStr, 10);
		
		if (topMsgsStr.length() != 0) {
			advtMsg = "topAdvMsg:" + topMsgsStr + "|";
		}

		if (bottomMsgsStr.length() != 0) {
			advtMsg = advtMsg + "bottomAdvMsg:" + bottomMsgsStr + "|";
		}
		
		resp.append(advtMsg+"|");*/
		
	}
	
	public int fetchCategoryId(String prodCode, String operatorCode, String circleCode, double denomination){
		int catId = 0;
		Connection con = DBConnect.getConnection();
		PreparedStatement pstmt = null;
		try{
				pstmt = con.prepareStatement("select category_id from st_cs_product_master pm, st_cs_operator_master om where pm.operator_code = om.operator_code and product_code = ? and om.operator_code = ? and circle_code = ? and denomination = ? and pm.status = 'ACTIVE' and om.status = 'ACTIVE'");
				pstmt.setString(1, prodCode);
				pstmt.setString(2, operatorCode);
				pstmt.setString(3, circleCode);
				pstmt.setDouble(4, denomination);
				ResultSet rs =pstmt.executeQuery();
				if(rs.next()){
					catId = rs.getInt("category_id");
				}
		} catch (SQLException sqe) {
			sqe.printStackTrace();
		}
		return catId;
	}
	
	public String camlotRefund(UserInfoBean userBean){
		CamlotSaleBean cbean = new CamlotSaleBean();
		CSSaleBean csbean = new CSSaleBean();
		String optr_name = null;
		String circle_name = null;
		
		StringBuffer finalResp = new StringBuffer();
		Connection con = DBConnect.getConnection();
		try{
			PreparedStatement pstmt = con.prepareStatement("select RMS_trans_id, cs_trans_id, Provider_ref_id, pin, expiry_date, mobile_num from st_cs_camlot_transaction_log where RMS_trans_id  = (select CSLastSaleTransId from st_lms_last_sale_transaction where organization_id = ?)");
			pstmt.setInt(1, userBean.getUserOrgId());
			ResultSet rs = pstmt.executeQuery();
			if(rs.next()){
				csbean.setRMSRefId(rs.getInt("RMS_trans_id"));
				csbean.setCSRefTxId(Integer.parseInt(rs.getString("cs_trans_id")));
				cbean.setProviderTransactionRef(rs.getString("Provider_ref_id"));
				if("NA".equalsIgnoreCase(rs.getString("pin"))){
					cbean.setPINNumber(new BigInteger("0",10));
				}else{
					cbean.setPINNumber(new BigInteger(rs.getString("pin"),10));
				}
				cbean.setExpiryDate(rs.getString("expiry_date"));
				cbean.setMobileNum(rs.getString("mobile_num"));
			}
			pstmt.close();
			rs.close();
			pstmt = con.prepareStatement("select pcm.category_code,pm.category_id, pm.product_code, om.operator_name, om.operator_code, cm.circle_name, cm.circle_code, pm.denomination, rtm.transaction_date from st_lms_retailer_transaction_master rtm, st_cs_product_master pm, st_cs_product_details pdm, st_cs_operator_master om, st_cs_circle_master cm,st_cs_product_category_master pcm where rtm.transaction_type = 'CS_SALE' and pm.category_id=pcm.category_id and rtm.transaction_id = ? and rtm.game_id = pm.product_id and rtm.game_id = pdm.product_id and om.operator_code = pm.operator_code and cm.circle_code = pm.circle_code");
			pstmt.setInt(1, csbean.getRMSRefId());
			rs = pstmt.executeQuery();
			if(rs.next()){
				csbean.setTransTime(rs.getTimestamp("transaction_date"));
				csbean.setCategoryId(rs.getInt("category_id"));
				csbean.setCategoryCode(rs.getString("category_code"));
				csbean.setDenomination(rs.getDouble("denomination"));
				csbean.setProdCode(rs.getString("product_code"));
				csbean.setOperatorCode(rs.getString("operator_code"));
				csbean.setCircleCode(rs.getString("circle_code"));
				optr_name = rs.getString("operator_name");
			    circle_name = rs.getString("circle_name");
				
			}
			pstmt.close();
			rs.close();
			if(Double.compare(csbean.getDenomination(),0.0) == 0){ //case of flexi
				pstmt = con.prepareStatement("select mrp_amt from st_cs_sale_? where transaction_id = ?");
				pstmt.setInt(1, csbean.getCategoryId());
				pstmt.setInt(2, csbean.getRMSRefId());
				rs = pstmt.executeQuery();
				if(rs.next()){
					csbean.setMrpAmt(rs.getDouble("mrp_amt"));
				}
			}
			NumberFormat nFormat = NumberFormat.getInstance();
			nFormat.setMinimumFractionDigits(2);
			finalResp.append(nFormat.format(Double.parseDouble(new AjaxRequestHelper().getAvlblCreditAmt(userBean).split("=")[3])).replaceAll(",", "")+"%$");
			finalResp.append(csbean.getCSRefTxId()+"%$");
			finalResp.append(csbean.getRMSRefId()+"%$");
			finalResp.append("%$");
			if(Double.compare(csbean.getDenomination(),0.0) == 0){
				finalResp.append("%$");
			}else{
				finalResp.append(cbean.getPINNumber()+"%$");
			}
			finalResp.append(csbean.getOperatorCode()+"%$");
			finalResp.append(csbean.getCircleCode()+"%$");
			finalResp.append(csbean.getCategoryCode()+"%$");
			finalResp.append(FormatNumber.formatNumber(csbean.getDenomination())+"%$");
			finalResp.append("%$");
			finalResp.append((new java.sql.Timestamp(csbean.getTransTime().getTime())).toString().split("\\.")[0]+"%$");
			finalResp.append(cbean.getExpiryDate());
			if(Double.compare(csbean.getDenomination(),0.0) == 0){
				finalResp.append("%$");
				finalResp.append(cbean.getMobileNum()+"%$");
				finalResp.append(FormatNumber.formatNumber(csbean.getMrpAmt()));
			}
			finalResp.append("|");
		}catch(Exception sqe){
			sqe.printStackTrace();
			finalResp.append("ErrorMsg:Reprint Error|");
		}
		return finalResp.toString();
	}
}
