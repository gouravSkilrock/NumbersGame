package com.skilrock.lms.web.scratchService.inventoryMgmt.common;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONObject;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.skilrock.lms.beans.UserInfoBean;
import com.skilrock.lms.common.exception.LMSErrors;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.common.utility.LMSUtility;
import com.skilrock.lms.coreEngine.drawGames.playMgmt.DrawGameRPOSHelper;
import com.skilrock.lms.coreEngine.scratchService.inventoryMgmt.common.BookWiseInvDetailForAgtHelper;
import com.skilrock.lms.coreEngine.scratchService.inventoryMgmt.common.BookWiseInvDetailForRetHelper;
import com.skilrock.lms.web.drawGames.common.CookieMgmtForTicketNumber;
import com.skilrock.lms.web.drawGames.common.Util;

public class BookWiseInvDetailForRet extends ActionSupport implements
		ServletRequestAware, ServletResponseAware {
	static Log logger = LogFactory.getLog(BookWiseInvDetailForAgt.class);

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String[] bookNumber;
	private int gameid;
	private String[] gameKey;
	private String bookNbr1;
	private int orgId;
	private String userName;
	private HttpServletRequest request;
	private HttpServletResponse response;
	private HttpSession session;
	private String type;
	private int invoiceId;
	private String invoiceReceipt;

	
	public String activateBooks() {
		logger.info("--inside activateBooks--");
		session = request.getSession();
		UserInfoBean userInfoBean = (UserInfoBean) session
				.getAttribute("USER_INFO");

		BookWiseInvDetailForRetHelper helper = new BookWiseInvDetailForRetHelper();
		Map gameBookMap = helper.activateBooks(userInfoBean.getUserOrgId());
		session.setAttribute("GAME_BOOK_MAP", gameBookMap);
		return SUCCESS;
	}

	@Override
	public String execute() throws LMSException {
		System.out.println("bookWiseinventoryDetails");
		session = request.getSession();
		
		UserInfoBean userInfo = (UserInfoBean) request.getSession().getAttribute("USER_INFO");
		ServletContext sc = LMSUtility.sc;
		int gameId=0;
		long lastPrintedTicket=0;
		String refMerchantId = (String) sc.getAttribute("REF_MERCHANT_ID");
		int autoCancelHoldDays=Integer.parseInt((String) sc.getAttribute("AUTO_CANCEL_CLOSER_DAYS"));
		
		String actionName=ActionContext.getContext().getName();
		DrawGameRPOSHelper drawGameRPOSHelper = new DrawGameRPOSHelper();
		//drawGameRPOSHelper.checkLastPrintedTicketStatusAndUpdate(userInfo,lastPrintedTicket,"WEB",refMerchantId,autoCancelHoldDays,actionName,gameId);
		try {
			long LSTktNo =  CookieMgmtForTicketNumber.getTicketNumberFromCookie(request, userInfo.getUserName());
			if(LSTktNo !=0){
				lastPrintedTicket = LSTktNo/Util.getDivValueForLastSoldTkt(String.valueOf(LSTktNo).length());
				gameId = Util.getGameIdFromGameNumber(Util.getGamenoFromTktnumber(String.valueOf(LSTktNo)));
			}
			drawGameRPOSHelper.insertEntryIntoPrintedTktTableForWeb(gameId, userInfo.getUserOrgId(), lastPrintedTicket, "WEB", Util.getCurrentTimeStamp(), actionName);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		}
		
		BookWiseInvDetailForRetHelper helper = new BookWiseInvDetailForRetHelper();
		Map<String, String> gameMap = helper.getGameMap();
		System.out.println("gameMap in bookWiseinventoryDetails ==== "
				+ gameMap);
		session.setAttribute("retListGame", gameMap);
		return SUCCESS;
	}

	public String[] getBookNumber() {
		return bookNumber;
	}

	public int getGameid() {
		return gameid;
	}

	public String[] getGameKey() {
		return gameKey;
	}
	public String getBookNbr1() {
		return bookNbr1;
	}

	public void setBookNbr1(String bookNbr1) {
		this.bookNbr1 = bookNbr1;
	}
	
	
	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}
	public void getInventoryDetailsForRet() throws LMSException {
		try {
			System.out.println("type = " + type + "\t, orgId = " + orgId
					+ "\t, gameId = " + gameid);
			String responseStr = null;
			PrintWriter out = response.getWriter();

			BookWiseInvDetailForAgtHelper helper = new BookWiseInvDetailForAgtHelper();

			session = request.getSession();
			UserInfoBean infoBean = (UserInfoBean) session
					.getAttribute("USER_INFO");
			
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
			
			
			
			responseStr = helper.getTotalBooksWithOrg(gameid, infoBean
					.getUserOrgId(), infoBean.getUserType());

			out.print(responseStr);
		} catch (IOException e) {
			throw new LMSException(e);
		} catch (LMSException e) {
			throw new LMSException(e);
		} catch (Exception e) {
			throw new LMSException(e);
		}
	}

	public int getOrgId() {
		return orgId;
	}

	public String getType() {
		return type;
	}

	public void setBookNumber(String[] bookNumber) {
		this.bookNumber = bookNumber;
	}

	public void setGameid(int gameid) {
		this.gameid = gameid;
	}

	public void setGameKey(String[] gameKey) {
		this.gameKey = gameKey;
	}

	public void setOrgId(int orgId) {
		this.orgId = orgId;
	}

	public void setServletRequest(HttpServletRequest req) {
		this.request = req;
	}

	public void setServletResponse(HttpServletResponse res) {
		this.response = res;
	}

	public void setType(String type) {
		this.type = type;
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

	public String updateBooks() throws LMSException {
		logger.info("--inside updateBooks--");
		session = request.getSession();
		UserInfoBean userInfoBean = (UserInfoBean) session
				.getAttribute("USER_INFO");
		BookWiseInvDetailForRetHelper helper = new BookWiseInvDetailForRetHelper();
		List<String> bookNumberList = new ArrayList<String>();
		for (String str : getBookNumber()) {
			bookNumberList.add(str);
		}

		String[] response = helper.updateBooks(userInfoBean.getUserOrgId(),userInfoBean.getUserId(), bookNumberList);
		if(response != null) {
			invoiceId = Integer.parseInt(response[0]);
			invoiceReceipt = response[1];
		}

		return SUCCESS;
	}
	
	public void bookActivate()  {
		logger.info("-- Inside activateBook --");
		PrintWriter out = null;
		UserInfoBean userBean = null;
		JSONObject js = new JSONObject();
		String bookNbr=null;
		try {
			response.setContentType("application/json");
			bookNbr =  getBookNbr1().trim();		
			out = response.getWriter();
			userBean = (UserInfoBean) request.getSession().getAttribute("USER_INFO");
			BookWiseInvDetailForRetHelper helper = new BookWiseInvDetailForRetHelper();
			String status = helper.validateBookAndStatus(userBean.getUserOrgId(), bookNbr.replaceAll("-", ""));
			logger.info("Book Status - "+status);
			js.put("isSuccess", false);
			js.put("responseCode", 500);
			if("ACTIVE".equals(status)) { 
				js.put("responseMsg", "Book Already Active.");
			} else if("CLAIMED".equals(status)) { 
				js.put("responseMsg", " Book Already Active.");
			} else if("MISSING".equals(status)) { 
				js.put("responseMsg", "Book is Missing");
			} else if("NO_BOOK_FOUND".equals(status)) {
				js.put("responseMsg", "Book Not Found ");
			}else if("IN_TRANSIT".equals(status)) {
				js.put("responseMsg", "Book have not been recieved yet ");
			}else if(bookNbr.contains("-") && !status.equals(bookNbr)) {
				js.put("responseMsg", "Invalid Book Number ");
			}else{
				List<String> bookList = new ArrayList<String>();
				bookList.add(bookNbr);
				helper.updateBooks(userBean.getUserOrgId(),userBean.getUserId(), bookList);
				logger.info("Book Activated Successfully - "+bookNbr);
				js.put("isSuccess", true);
				js.put("responseCode", 0);
				js.put("responseMsg", "Book Activated Successfully ");
				
			}
		} catch (LMSException e) {
			js.put("isSuccess", false);
			js.put("responseCode", e.getErrorCode());
			js.put("responseMsg", e.getErrorMessage());
		}catch (Exception e) {
			e.printStackTrace();
			js.put("isSuccess", false);
			js.put("responseCode", 500);
			js.put("responseMsg", LMSErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		}
		out.print(js);
		out.flush();
		out.close();
	}
}