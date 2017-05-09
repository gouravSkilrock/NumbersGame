package com.skilrock.lms.coreEngine.commercialService.common;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.skilrock.camlot.coreEngine.common.CommonSMSHelper;
import com.skilrock.lms.ajax.AjaxRequestHelper;
import com.skilrock.lms.beans.CSSaleBean;
import com.skilrock.lms.beans.PWSaleDataBean;
import com.skilrock.lms.beans.UserInfoBean;
import com.skilrock.lms.common.db.DBConnect;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.common.utility.FormatNumber;
import com.skilrock.lms.coreEngine.payWorld.common.PayWorldHelper;
import com.skilrock.lms.embedded.drawGames.common.EmbeddedErrors;
import com.skilrock.lms.web.drawGames.common.Util;
import com.skilrock.lms.web.drawGames.common.UtilApplet;

public class CSPWSaleHelper {
	static Log logger = LogFactory.getLog(CSPWSaleHelper.class);
	
	public String pwSaleTransaction(String product, String retailerid, String userName,String operatorcode, String circode, double denomination,   double rechargeAmt, int bulkqty,String narration, String agtid, String agtpwd, String loginstatus,String appver, String cs_isVoucherPrintON, String mobileNum,long lastTransId) throws Exception{
		
		CSSaleTransactionsHelper csHelper = new CSSaleTransactionsHelper();
		CSSaleBean sBean = new CSSaleBean();
		boolean isFlexi = true;
		sBean.setProdCode(product);
		sBean.setRetOrgId(Integer.parseInt(retailerid));
		sBean.setUserName(userName);
		sBean.setBalance(0.0);
		if(denomination == 0){
			isFlexi = true;
			sBean.setUnitPrice(rechargeAmt);
		}else{
			isFlexi = false;
			sBean.setUnitPrice(denomination);
		}
		sBean.setMult(bulkqty);
		sBean.setMrpAmt(sBean.getUnitPrice()*bulkqty);
		sBean.setRetOrgId(Integer.parseInt(retailerid));
		sBean.setCSRefTxId(0);
		sBean.setOperatorCode(operatorcode);
		sBean.setCircleCode(circode);
		sBean.setDenomination(denomination);
		//Check Last Successful Transaction

		if(lastTransId !=0){
			if(!(lastTransId+"").equalsIgnoreCase(CSUtil.fetchLastCSTransId(sBean.getRetOrgId()))){
				return "ErrorMsg:Reprint last voucher|ErrorCode:02";
			}
		}
		sBean = csHelper.CommServSaleBalDeduction(sBean);
		if(sBean.getErrorCode() != 100){
			Class name = EmbeddedErrors.class;
			return "ErrorMsg:"+(String)name.getDeclaredField("ERROR_"+sBean.getErrorCode()).get(null)+ "|";
		}
		PayWorldHelper helper = new PayWorldHelper();
		String respData = helper.transactionRequest(agtid,sBean.getRMSRefId(),retailerid,operatorcode, circode, product, denomination, bulkqty, narration, agtpwd, loginstatus, appver);
		if(!respData.equalsIgnoreCase("")){
			PWSaleDataBean pwDataBean = new PWSaleDataBean();
			pwDataBean.setRetailerId(retailerid);
			pwDataBean.setOrgBalance(sBean.getBalance());
			pwDataBean.setRMStransId(sBean.getRMSRefId()+"");
			pwDataBean.setProdId(sBean.getProdId());
			pwDataBean.setCategoryId(sBean.getCategoryId());
			pwDataBean.setOperatorCode(operatorcode);
			pwDataBean.setCirCode(circode);
			pwDataBean.setProduct(product);
			pwDataBean.setDenomination(denomination);
			pwDataBean.setBulkQty(bulkqty);
			pwDataBean.setNarration(narration);
			
			if(respData.indexOf("ERROR") != -1){
				if(respData.indexOf("No Response From Host") != -1){
					//return respData+"|TransId:"+sBean.getRMSRefId()+"|DateTime:"+ new java.sql.Date(sBean.getTransTime().getTime()).toString() +"|"; //send RMS Transaction ID in case of No response from host (Transaction can be tracked later)
					return "ErrorMsg:Transaction Pending|"; //send RMS Transaction ID in case of No response from host (Transaction can be tracked later)
				}
				//Generate a saleCancel Request
				sBean.setCSRefTxIdForRefund(0);
				sBean.setRMSRefIdForRefund(sBean.getRMSRefId());
				sBean.setCSRefTxId(0);
				sBean.setReasonForCancel("CANCEL_SERVER");
				sBean = csHelper.CommServSaleRefund(sBean);

				if(sBean.getErrorCode() != 100){
					Class name = EmbeddedErrors.class;
					return "ErrorMsg:"+(String)name.getDeclaredField("ERROR_"+sBean.getErrorCode()).get(null)+ "|"; 
				}
				if("NO".equalsIgnoreCase(cs_isVoucherPrintON)){
					System.out.println("SMS Sent to The Mobile Number: "+mobileNum); //SMS API is to be implemented
					new CommonSMSHelper(mobileNum,helper.formatRespForSMS(respData)).sendSMS("demoint", "demoint2010");
				}
				return respData;
			}else{
				//updating the cs_ref_transaction_id
				boolean success = (new PayWorldHelper()).updatePWTransId(pwDataBean.getRMStransId(),respData.split("%\\$")[0], pwDataBean.getProdId(), pwDataBean.getCategoryId());
				//to send response as it is without parsing and removing the $$$ at the end
				//update CS last sale transaction
				CSUtil.updateLastCSTransId(sBean.getRetOrgId(), sBean.getRMSRefId());
				//appending advertisement message data
				Map<String, List<String>> advMap= Util.getAdvMessage(sBean.getRetOrgId(),	sBean.getCategoryId(), "PLAYER", "SALE", "CS");
				
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
				//return sBean.getRMSRefId()+"%$"+FormatNumber.formatNumber(sBean.getBalance())+"%$"+respData.substring(0, respData.length()-3)+"|"+advtMsg+"|";//helper.getTerminalFormatSaleData(helper.parseSaleResponseData(respData,pwDataBean));
				return sBean.getRMSRefId()+"%$"+FormatNumber.formatNumber(sBean.getBalance())+"%$"+respData.substring(0, respData.length()-3)+"|%"+advtMsg+"|%";
				
			}
		}else{
			return "#ERROR:Your Request is in a queue|"; // we have to implement the logic of request for reprint
		}

	}
	
	public String fetchPWSaleResponse(String cs_isVoucherPrintON)throws Exception{
		PayWorldHelper helper = new PayWorldHelper();
		//operator -> circle -> product -> denomination -> detailsMap[Keys]('talktime', 'validity', 'adminFee', 'serviceTax')
		//Map<String, Map<String, Map<String, Map<String, Map<String, String>>>>> data= helper.fetchServiceDataForTerminal();
		return helper.getTerminalFormatServiceDataNew(cs_isVoucherPrintON);
		
	}
	
	public String fetchReprintLastTrans(int retOrgId, String merchantId, String merchantPwd, String loginstatus, String appver, UserInfoBean userBean)throws Exception{
		Connection con = DBConnect.getConnection();
		PreparedStatement pstmt1, pstmt2 = null;
		ResultSet rs1, rs2 = null;
		PayWorldHelper helper = new PayWorldHelper();
		String statusData = "";
		StringBuilder rtrnData = new StringBuilder();
		String categoryCode = null;
		int categoryId=0;
		Map<String, List<String>> advMap= null;
		StringBuilder topMsgsStr = null;
		StringBuilder bottomMsgsStr = null;
		String advtMsg = "";
		try{
			pstmt1 = con.prepareStatement("select lrm.transaction_id, lrm.game_id, pm.category_id, pcm.category_code from st_lms_retailer_transaction_master lrm, st_cs_product_master pm, st_cs_product_category_master pcm where retailer_org_id = ? and lrm.transaction_type = 'CS_SALE' and pm.product_id = lrm.game_id and pm.category_id=pcm.category_id order by lrm.transaction_date desc limit 1");
			pstmt1.setInt(1, retOrgId);
			rs1 = pstmt1.executeQuery();
			while(rs1.next()){
				categoryId=rs1.getInt("category_id");
				categoryCode = rs1.getString("category_code");
				pstmt2  = con.prepareStatement("select cs_ref_transaction_id from st_cs_sale_"+ rs1.getString("category_id") +" where transaction_id = "+rs1.getInt("transaction_id"));
				rs2 = pstmt2.executeQuery();
				while(rs2.next()){
					if(rs2.getString("cs_ref_transaction_id").equalsIgnoreCase("0")){
						return "#ERROR:Last Transaction is pending|";
					}else{
						//to make the denomination in double
						statusData = helper.transStatusRequest("", rs1.getString("transaction_id"), merchantId, merchantPwd, loginstatus, "PIN", appver);
						String strArr[] = statusData.split("%\\$");
						if(strArr[7].indexOf(".") == -1){
							strArr[7] = strArr[7]+".00";
						}else{
							if(strArr[7].split("\\.")[1].length() == 1){
								strArr[7] = strArr[7]+"0";
							}
						}
						strArr[6] = categoryCode;
						
						for(String str: strArr){
							rtrnData.append(str+"%$");
						}
						statusData = rtrnData.substring(0, rtrnData.length()-2);
					}
				}
			}
			
			advMap= Util.getAdvMessage(retOrgId,	categoryId, "PLAYER", "SALE", "CS");
			
			topMsgsStr = new StringBuilder("");
			bottomMsgsStr = new StringBuilder("");

			UtilApplet.getAdvMsgs(advMap, topMsgsStr,
					bottomMsgsStr, 10);
			
			if (topMsgsStr.length() != 0) {
				advtMsg = "topAdvMsg:" + topMsgsStr + "|";
			}

			if (bottomMsgsStr.length() != 0) {
				advtMsg = advtMsg + "bottomAdvMsg:" + bottomMsgsStr + "|";
			}
			
		}catch (SQLException e) {
			logger.error("Exception: " + e);
			e.printStackTrace();
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
		if(statusData.indexOf("ERROR") != -1){
			return statusData;
		}
		return new AjaxRequestHelper().getAvlblCreditAmt(userBean).split("=")[3]+"%$"+statusData.toString().substring(0,statusData.toString().length()-3)+"|"+advtMsg;
	}
	
	/*public static String fetchLastCSTransId(int retOrgId) throws LMSException{
		String lastTransId = "";
		Connection con = DBConnect.getConnection();
		PreparedStatement pstmt1 = null;
		ResultSet rs1 = null;
		try{
			pstmt1 = con.prepareStatement("select CSLastSaleTransId from st_lms_last_sale_transaction where organization_id = ?");
			pstmt1.setInt(1, retOrgId);
			rs1 = pstmt1.executeQuery();
			if(rs1.next()){
				lastTransId = rs1.getString("CSLastSaleTransId");
			}
		}catch(SQLException e) {
			logger.error("Exception: " + e);
			e.printStackTrace();
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
		return lastTransId;
	}*/
	
	/*public static void updateLastCSTransId(int retOrgId,long newTransId) throws LMSException{
		Connection con = DBConnect.getConnection();
		try{
			Statement stmt = con.createStatement();
			stmt.executeUpdate("update st_lms_last_sale_transaction set CSLastSaleTransId = "+ newTransId +" where organization_id = "+ retOrgId);
		}catch(SQLException e) {
			logger.error("Exception: " + e);
			e.printStackTrace();
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
	}
	*/
	public static void main(String args[]) throws Exception{
		System.out.println(new CSPWSaleHelper().fetchPWSaleResponse(""));
	}

}
