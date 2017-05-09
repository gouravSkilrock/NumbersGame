package com.skilrock.lms.keba.reportMgmt.action;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;

import org.apache.struts2.ServletActionContext;

import com.skilrock.lms.ajax.AjaxRequestHelper;
import com.skilrock.lms.beans.CustomTransactionReportBean;
import com.skilrock.lms.beans.UserInfoBean;
import com.skilrock.lms.common.BaseAction;
import com.skilrock.lms.common.db.DBConnect;
import com.skilrock.lms.common.exception.LMSErrors;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.coreEngine.reportsMgmt.common.CustomTransactionReportHelper;
import com.skilrock.lms.coreEngine.userMgmt.common.CommonFunctionsHelper;
import com.skilrock.lms.web.drawGames.common.Util;

public class RetailerTransactionReportAction extends BaseAction {
	private static final long serialVersionUID = 1L;

	public RetailerTransactionReportAction() {
		super(RetailerTransactionReportAction.class);
	}

	private String requestData;

	public void retailerTransactionReport() throws Exception {
		ServletContext sc = ServletActionContext.getServletContext();
		JSONObject responseObject = new JSONObject();
		PrintWriter out = null;
		try {
			JSONObject requestData = (JSONObject) JSONSerializer.toJSON(request.getParameter("requestData"));
			String userName = (String) requestData.get("userName");
			response.setContentType("application/json");
			out = response.getWriter();

		Map currentUserSessionMap = (Map) sc.getAttribute("LOGGED_IN_USERS");
		if (currentUserSessionMap == null) {
			throw new LMSException(LMSErrors.SESSION_EXPIRED_ERROR_CODE,LMSErrors.SESSION_EXPIRED_ERROR_MESSAGE);
			}
		// logger.debug(" LOGGED_IN_USERS maps is " + currentUserSessionMap);

		HttpSession session = (HttpSession) currentUserSessionMap.get(userName);
		if (!CommonFunctionsHelper.isSessionValid(session)) {
			throw new LMSException(LMSErrors.SESSION_EXPIRED_ERROR_CODE,LMSErrors.SESSION_EXPIRED_ERROR_MESSAGE);
		}
		UserInfoBean userBean = (UserInfoBean) session.getAttribute("USER_INFO");
		String refMerchantId = (String) sc.getAttribute("REF_MERCHANT_ID");
		
		
		Connection con=DBConnect.getConnection();
		Map<String, CustomTransactionReportBean> retailerMap = new LinkedHashMap<String, CustomTransactionReportBean>();

		SimpleDateFormat sd =new SimpleDateFormat("yyyy-MM-dd");
		String start_date="2014-01-29";
		Timestamp startDate = new Timestamp( sd
				.parse(start_date).getTime());
		CustomTransactionReportHelper helper=new CustomTransactionReportHelper();
		helper.collectionTransactionWise(startDate, Util.getCurrentTimeStamp(), con, true, false,
					false, false, false, false, retailerMap, userBean.getUserOrgId());
		Map<String, CustomTransactionReportBean> newMap = new TreeMap(Collections.reverseOrder());
		newMap.putAll(retailerMap);
			Iterator<Map.Entry<String, CustomTransactionReportBean>> itr1 = newMap
					.entrySet().iterator();
			AjaxRequestHelper ajxHelper = new AjaxRequestHelper();
			ajxHelper.getAvlblCreditAmt(userBean,con);
			double openBal=userBean.getAvailableCreditLimit() - userBean.getClaimableBal();
			
			responseObject.put("openingBal", openBal);
			
			
			JSONObject transactionObject=null;
			JSONArray transactionArray=new JSONArray();
			
			
			
			
			while (itr1.hasNext()) {
				Map.Entry<String, CustomTransactionReportBean> pair = itr1
						.next();
				CustomTransactionReportBean bean = pair.getValue();
				
				bean.setCurrentBalance(openBal);
				if(bean.getAmount() == 0){
					continue;
				}
				
				transactionObject=new JSONObject();
				transactionObject.put("date", bean.getDate());
				transactionObject.put("service", bean.getService());
				transactionObject.put("particular", bean.getGameName());
				transactionObject.put("amount", bean.getAmount());
				transactionObject.put("avilableBal", openBal);
				openBal = openBal - bean.getAmount();
				transactionArray.add(transactionObject);	
				
				if(transactionArray.size() >20){
					break;
				}
			}
		
		responseObject.put("transactionData", transactionArray);
		responseObject.put("errorMsg", "");
		responseObject.put("isSuccess", true);
		//String winningResult = "Result Awaited";
		/*for (int i = 0; i < winningResultList.size(); i++) {
			String[] result = winningResultList.get(i).split("=");
			Long time = Long.parseLong(result[0]);
			Timestamp t = new Timestamp(time);
			if (t.toString().split("\\.")[0].equalsIgnoreCase(drawTime)) {
				winningResult = result[1];
				
				 * if ("RaffleGame".equalsIgnoreCase(Util.getGameName(gameNo)))
				 * { winningResult = new
				 * RaffleHelper().swapRaffleResult(winningResult); }
				 
			}
		}*/
		//logger.debug("Winning Result:" + winningResult + "|");
		
		} catch (IOException e) {
			e.printStackTrace();
			responseObject.put("errorMsg", "IOException Occured.");
			responseObject.put("isSuccess", false);
			return;
		} catch (LMSException e) {
			e.printStackTrace();
			if(e.getErrorCode() == 2013){
				responseObject.put("errorMsg", e.getErrorMessage());
			}else{
			responseObject.put("errorMsg", "LMSException Occured.");
			}
			responseObject.put("isSuccess", false);
			return;
		}  catch (Exception e) {
			e.printStackTrace();
			responseObject.put("errorMsg", "Exception Occured.");
			responseObject.put("isSuccess", false);
			return;
		} finally {
			if (responseObject.isEmpty()) {
				responseObject.put("errorMsg", "Compile Time Error.");
				responseObject.put("isSuccess", false);
			}
			logger.info("ZimLottoBonus Sale Response Data : " + responseObject);
			out.print(responseObject);
			out.flush();
			out.close();
		}
	}

	public String getRequestData() {
		return requestData;
	}

	public void setRequestData(String requestData) {
		this.requestData = requestData;
	}

}
