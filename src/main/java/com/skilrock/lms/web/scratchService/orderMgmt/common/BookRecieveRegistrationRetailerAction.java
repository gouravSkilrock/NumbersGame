package com.skilrock.lms.web.scratchService.orderMgmt.common;

import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.opensymphony.xwork2.ActionSupport;
import com.skilrock.lms.beans.DLChallanDetails;
import com.skilrock.lms.beans.UserInfoBean;
import com.skilrock.lms.common.BaseAction;
import com.skilrock.lms.common.exception.LMSErrors;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.coreEngine.reportsMgmt.common.GraphReportHelper;
import com.skilrock.lms.coreEngine.scratchService.orderMgmt.common.BookRecieveRegistrationRetailerHelper;

import net.sf.json.JSON;
import net.sf.json.JSONObject;

public class BookRecieveRegistrationRetailerAction extends BaseAction {
	private static final long serialVersionUID = 1L;

	private String challanId;
	private String[] bookNumber;
	private int invoiceId;
	private String invoiceReceipt;
	private int userOrgId;

	public int getUserOrgId() {
		return userOrgId;
	}

	public void setUserOrgId(int userOrgId) {
		this.userOrgId = userOrgId;
	}

	public BookRecieveRegistrationRetailerAction() {
		super("BookRecieveRegistrationRetailerAction");
	}

	public String getChallanId() {
		return challanId;
	}

	public void setChallanId(String challanId) {
		this.challanId = challanId;
	}

	public String[] getBookNumber() {
		return bookNumber;
	}

	public void setBookNumber(String[] bookNumber) {
		this.bookNumber = bookNumber;
	}

	public int getInvoiceId() {
		return invoiceId;
	}

	public void setInvoiceId(int invoiceId) {
		this.invoiceId = invoiceId;
	}

	public String getInvoiceReceipt() {
		return invoiceReceipt;
	}

	public void setInvoiceReceipt(String invoiceReceipt) {
		this.invoiceReceipt = invoiceReceipt;
	}

	public void getBooks() throws LMSException{	
	
		PrintWriter out = null;
		JsonObject res=new JsonObject();
		try {
			response.setContentType("application/json");
			out = response.getWriter();
			logger.info("--inside activateBooks--");
	
			UserInfoBean userInfoBean = getUserBean();
			BookRecieveRegistrationRetailerHelper helper = new BookRecieveRegistrationRetailerHelper();
			Map<String, List<String>> gameBookMap = helper.getBooks(userInfoBean.getUserOrgId(),challanId);
			
			if (gameBookMap.size()>0) {
				res.addProperty("responseCode", 0);
				res.addProperty("gameBookData",new Gson().toJson(gameBookMap));
			}else{
				res.addProperty("responseCode", LMSErrors.GENERAL_EXCEPTION_ERROR_CODE);
				res.addProperty("responseMsg", "Invalid challan id");
			}
			
		}catch (LMSException e) {
			res.addProperty("responseCode", e.getErrorCode());
			res.addProperty("responseMsg", e.getErrorMessage());
		}catch (Exception e) {
			e.printStackTrace();
			res.addProperty("responseCode", LMSErrors.GENERAL_EXCEPTION_ERROR_CODE);
			res.addProperty("responseMsg", LMSErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		}
		out.print(res);
		out.flush();
		out.close();
	}
	
	

	public void submitBooks() throws SQLException, LMSException {
		
		logger.info("--inside submitBooks--");
		PrintWriter out = null;
		JsonObject res=new JsonObject();
		try {
			response.setContentType("application/json");
			out = response.getWriter();
			UserInfoBean userInfoBean = getUserBean();
			BookRecieveRegistrationRetailerHelper helper = new BookRecieveRegistrationRetailerHelper();
			List<String> bookNumberList = new ArrayList<String>();
			String bookArr[]=getBookNumber()[0].split(",");
			for (String str : bookArr) {
				bookNumberList.add(str);
			}

			String[] response = helper.updateBooks(userInfoBean.getUserOrgId(), userInfoBean.getUserId(), bookNumberList);
			if(response != null) {				 
				invoiceId = (response[0] == null || response[0].trim().isEmpty()) ? 0 : Integer.parseInt(response[0]);
				invoiceReceipt = response[1];
			}
			res.addProperty("responseCode", 0);
			res.addProperty("invoiceId",invoiceId);
			res.addProperty("invoiceReceipt",invoiceReceipt);
		} catch (LMSException e) {
			res.addProperty("responseCode", e.getErrorCode());
			res.addProperty("responseMsg", e.getErrorMessage());
		}catch (Exception e) {
			e.printStackTrace();
			res.addProperty("responseCode", LMSErrors.GENERAL_EXCEPTION_ERROR_CODE);
			res.addProperty("responseMsg", LMSErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		}
		out.print(res);
		out.flush();
		out.close();
	}
	
	public void fetchAvailableDLChallan() throws LMSException{
		logger.info("--inside fetchAvailableDLChallan--");
		PrintWriter out = null;
		JSONObject res = new JSONObject();
		List<DLChallanDetails> challanList = null;
		try {
			response.setContentType("application/json");
			out = response.getWriter();
			BookRecieveRegistrationRetailerHelper helper = new BookRecieveRegistrationRetailerHelper();
			challanList = helper.fetchAvailableDLChallan(userOrgId);
			res.put("responseCode", 0);
			res.put("responseMsg", "Success");
			res.put("responseData",challanList);
		}catch (LMSException e) {
			res.put("responseCode", e.getErrorCode());
			res.put("responseMsg", e.getErrorMessage());
		}catch (Exception e) {
			e.printStackTrace();
			res.put("responseCode", LMSErrors.GENERAL_EXCEPTION_ERROR_CODE);
			res.put("responseMsg", LMSErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		}
		out.print(new Gson().toJson(res));
		out.flush();
		out.close();
	}
}