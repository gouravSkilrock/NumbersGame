package com.skilrock.lms.web.scratchService.inventoryMgmt.common;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONObject;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.skilrock.lms.beans.UserInfoBean;
import com.skilrock.lms.common.exception.LMSErrors;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.common.utility.LMSUtility;
import com.skilrock.lms.coreEngine.drawGames.playMgmt.DrawGameRPOSHelper;
import com.skilrock.lms.coreEngine.scratchService.inventoryMgmt.common.SoldTicketEntryForRetHelper;
import com.skilrock.lms.web.drawGames.common.CookieMgmtForTicketNumber;
import com.skilrock.lms.web.drawGames.common.Util;

public class SoldTicketEntryForRet extends ActionSupport implements
		ServletRequestAware, ServletResponseAware {
	static Log logger = LogFactory.getLog(SoldTicketEntryForRet.class);

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String[] bookNbr;
	private String[] currRem;

	private String[] gameName;

	private HttpServletRequest request;
	private HttpServletResponse response;
	private String[] tktInBook;
	private String[] updCurrRem;
	private String json;

	public void fetchBooksDetail() throws Exception {
		logger.debug("fetchBooksDetail");
		try {
			
			UserInfoBean userInfo = (UserInfoBean) request.getSession().getAttribute("USER_INFO");
			ServletContext sc = LMSUtility.sc;
			int gameId=0;
			long lastPrintedTicket=0;
			String refMerchantId = (String) sc.getAttribute("REF_MERCHANT_ID");
			int autoCancelHoldDays=Integer.parseInt((String) sc.getAttribute("AUTO_CANCEL_CLOSER_DAYS"));
			
			String actionName=ActionContext.getContext().getName();
			DrawGameRPOSHelper drawGameRPOSHelper = new DrawGameRPOSHelper();
			//drawGameRPOSHelper.checkLastPrintedTicketStatusAndUpdate(userInfo,lastPrintedTicket,"WEB",refMerchantId,autoCancelHoldDays,actionName,gameId);
			try{
				long LSTktNo =  CookieMgmtForTicketNumber.getTicketNumberFromCookie(request, userInfo.getUserName());
				if(LSTktNo !=0){
					lastPrintedTicket = LSTktNo/Util.getDivValueForLastSoldTkt(String.valueOf(LSTktNo).length());
					gameId = Util.getGameIdFromGameNumber(Util.getGamenoFromTktnumber(String.valueOf(LSTktNo)));
				}
				drawGameRPOSHelper.insertEntryIntoPrintedTktTableForWeb(gameId, userInfo.getUserOrgId(), lastPrintedTicket, "WEB", Util.getCurrentTimeStamp(), actionName);
			}catch(Exception e){
				//e.printStackTrace();
			}
			
			
			PrintWriter pw = response.getWriter();
			SoldTicketEntryForRetHelper helper = new SoldTicketEntryForRetHelper();
			String resString = helper.fetchBooksDetails(userInfo.getUserOrgId());
			pw.print(resString);
		} catch (IOException e) {
			e.printStackTrace();
			throw new LMSException(e);
		}

	}

	public String[] getBookNbr() {
		return bookNbr;
	}

	public String[] getCurrRem() {
		return currRem;
	}

	public String[] getGameName() {
		return gameName;
	}

	public String[] getTktInBook() {
		return tktInBook;
	}

	public String[] getUpdCurrRem() {
		return updCurrRem;
	}

	public void saveSoldTicketEntry() throws Exception  {
		logger.debug("saveSoldTicketEntry");
		HttpSession session = request.getSession();
		UserInfoBean userInfo = (UserInfoBean) session.getAttribute("USER_INFO");
		PrintWriter out = null;
		JSONObject js = new JSONObject();
		response.setContentType("application/json");
		ServletContext sc = LMSUtility.sc;
		int gameId=0;
		long lastPrintedTicket=0;
		String refMerchantId = (String) sc.getAttribute("REF_MERCHANT_ID");
		int autoCancelHoldDays=Integer.parseInt((String) sc.getAttribute("AUTO_CANCEL_CLOSER_DAYS"));
		
		String actionName=ActionContext.getContext().getName();
		DrawGameRPOSHelper drawGameRPOSHelper = new DrawGameRPOSHelper();
		//drawGameRPOSHelper.checkLastPrintedTicketStatusAndUpdate(userInfo,lastPrintedTicket,"WEB",refMerchantId,autoCancelHoldDays,actionName,gameId);
		try{
			long LSTktNo =  CookieMgmtForTicketNumber.getTicketNumberFromCookie(request, userInfo.getUserName());
			if(LSTktNo !=0){
				lastPrintedTicket = LSTktNo/Util.getDivValueForLastSoldTkt(String.valueOf(LSTktNo).length());
				gameId = Util.getGameIdFromGameNumber(Util.getGamenoFromTktnumber(String.valueOf(LSTktNo)));
			}
			drawGameRPOSHelper.insertEntryIntoPrintedTktTableForWeb(gameId, userInfo.getUserOrgId(), lastPrintedTicket, "WEB", Util.getCurrentTimeStamp(), actionName);
		}catch(Exception e){
			//e.printStackTrace();
		}
		
		
		//SoldTicketEntryForRetHelper helper = new SoldTicketEntryForRetHelper();
		try {
			    out = response.getWriter();
				updCurrRem=Arrays.toString(updCurrRem).split("[\\[\\]]")[1].split(",");
				bookNbr=Arrays.toString(bookNbr).split("[\\[\\]]")[1].split(",");
				currRem=Arrays.toString(currRem).split("[\\[\\]]")[1].split(",");
				tktInBook=Arrays.toString(tktInBook).split("[\\[\\]]")[1].split(",");
			
				for (String updateRemTkt : updCurrRem) {
					int i = Integer.parseInt(updateRemTkt.trim());
					if (i < 0) {
						throw new LMSException("Please Enter Positive Value");
					}
				}
				boolean flag = SoldTicketEntryForRetHelper.saveSoldTicketEntry(bookNbr, currRem, userInfo,updCurrRem, tktInBook);
				if (flag) {
					//return SUCCESS;
					js.put("isSuccess", true);
					js.put("responseCode", 0);
					js.put("responseMsg", "Sold Ticket Entries Done Successfully ");
				}
				else{
					js.put("isSuccess", false);
					js.put("responseCode", 500);
					js.put("responseMsg", "Some Internal Error Occured !!! ");
				}
			}catch (LMSException e) {
				logger.error("LMSException",e);
				throw new LMSException("Some Internal Error");
			}catch (Exception e) {
				logger.error("Exception",e);
				throw new LMSException("Please Enter Correct Value");
			}

		out.print(js);
		out.flush();
		out.close();
		//return ERROR;
	}

	public void updateSaleTicketEntry(){
		JsonObject js=null;
		PrintWriter out = null;
		JsonObject res=new JsonObject();
		try {
			response.setContentType("application/json");
			out = response.getWriter();
			js=new JsonParser().parse(json).getAsJsonObject();
			String ticketNbr=js.get("ticketNbr").getAsString();
			UserInfoBean userInfo = (UserInfoBean) request.getSession()
					.getAttribute("USER_INFO");
			
			new SoldTicketEntryForRetHelper().updateSellTicketStatus(ticketNbr, userInfo,null);
			res.addProperty("responseCode", 0);
			res.addProperty("responseMsg", "Sell ticket Entry Done Succesfully");
			
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

	public void setBookNbr(String[] bookNbr) {
		this.bookNbr = bookNbr;
	}

	public void setCurrRem(String[] currRem) {
		this.currRem = currRem;
	}

	public void setGameName(String[] gameName) {
		this.gameName = gameName;
	}

	public void setServletRequest(HttpServletRequest req) {
		this.request = req;

	}

	public void setServletResponse(HttpServletResponse res) {
		this.response = res;

	}

	public void setTktInBook(String[] tktInBook) {
		this.tktInBook = tktInBook;
	}

	public void setUpdCurrRem(String[] updCurrRem) {
		this.updCurrRem = updCurrRem;
	}

	public String getJson() {
		return json;
	}

	public void setJson(String json) {
		this.json = json;
	}

}
