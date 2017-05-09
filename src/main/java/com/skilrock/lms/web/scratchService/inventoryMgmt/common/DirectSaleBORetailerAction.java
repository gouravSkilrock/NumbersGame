package com.skilrock.lms.web.scratchService.inventoryMgmt.common;

import java.io.PrintWriter;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;

import com.opensymphony.xwork2.ActionSupport;
import com.skilrock.lms.beans.ActiveGameBean;
import com.skilrock.lms.beans.BookBean;
import com.skilrock.lms.beans.BookSeriesBean;
import com.skilrock.lms.beans.GameTicketFormatBean;
import com.skilrock.lms.beans.UserInfoBean;
import com.skilrock.lms.common.db.DBConnect;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.common.utility.CommonMethods;
import com.skilrock.lms.coreEngine.reportsMgmt.common.GraphReportHelper;
import com.skilrock.lms.coreEngine.scratchService.inventoryMgmt.common.DirectSaleBOHelper;
import com.skilrock.lms.coreEngine.scratchService.pwtMgmt.common.PwtTicketHelper;
import com.skilrock.lms.web.scratchService.inventoryMgmt.serviceImpl.DirectSaleBORetailerServiceImpl;

public class DirectSaleBORetailerAction extends ActionSupport implements
		ServletRequestAware, ServletResponseAware {
	static final Log logger = LogFactory
			.getLog(DirectSaleBORetailerAction.class);

	private static final long serialVersionUID = 1L;

	private int agtOrgName;

	private int retOrgName;

	private String[] bookArr;
	private int[] bookCountArr;
	private String[] bookCountArr1;
	private String[] bookFromArr;
	private int[] bookFromCountArr;
	private String[] bookFromCountArr1;
	private String[] bookNumber;

	private String[] bookTOArr;

	private int[] bookToCountArr;
	private String[] bookToCountArr1;
	private int boOrgId;
	// private String gameName;
	private String[] gameName;

	private int gamesToDisplay;

	HttpServletRequest request;
	private HttpServletResponse response;

	private Map<String, List> verifiedGameMap;
	private int agtOrgId;

	public String dispatchOrder() throws LMSException {
		ServletContext sc = ServletActionContext.getServletContext();
		HttpSession session = getRequest().getSession();
		UserInfoBean userInfoBean = (UserInfoBean) session
				.getAttribute("USER_INFO");
		int agtId = userInfoBean.getUserId();
		int agtOrgId = userInfoBean.getUserOrgId();
		String newBookStatusRet = "INACTIVE";
		String newBookStatusAgt = "INACTIVE";
		String rootPath = (String) session.getAttribute("ROOT_PATH");
		Map<Integer, List<String>> gameBookMap = (Map<Integer, List<String>>) session
				.getAttribute("DISPATCH_BOOK_MAP");
		UserInfoBean userBean = null;
		String agtSaleReturnValue = null;
		String retSaleReturnValue = null;
		String returnValue = null;
		int invoiceId = 0;
		String txnReturnValue = null;
		DirectSaleBORetailerServiceImpl directSaleBORetailerServiceImpl = new DirectSaleBORetailerServiceImpl();

		try {
			if (gameBookMap.size() > 0) {
				if ("BO-AGENT".equals((String) sc
						.getAttribute("BOOK_ACTIVATION_AT"))) {
					newBookStatusAgt = "ACTIVE";
				}

				if ("AGENT-RETAILER".equals((String) sc
						.getAttribute("BOOK_ACTIVATION_AT"))) {
					newBookStatusRet = "ACTIVE";
				} else if ("BO-AGENT".equals((String) sc
						.getAttribute("BOOK_ACTIVATION_AT"))) {
					newBookStatusRet = "ACTIVE";
				}
				userBean = CommonMethods.fetchUserData(agtOrgName);
				txnReturnValue = directSaleBORetailerServiceImpl
						.startTransaction(gameBookMap, agtId, agtOrgId,
								agtOrgName, rootPath,
								userInfoBean.getOrgName(), newBookStatusAgt,
								retOrgName, newBookStatusRet);

				System.out.println("Txn Return Value " + txnReturnValue);
				agtSaleReturnValue = txnReturnValue.split("#\\$")[0];
				retSaleReturnValue = txnReturnValue.split("#\\$")[1];

				session.setAttribute("DEL_CHALLAN_ID",
						Integer.parseInt(retSaleReturnValue.split("Nxt")[0]));

				invoiceId = Integer
						.parseInt(agtSaleReturnValue.split("Nxt")[1]);
				if (invoiceId > -1) {
					GraphReportHelper graphReportHelper = new GraphReportHelper();
					graphReportHelper.createTextReportBO(invoiceId,
							userBean.getOrgName(), agtOrgName, rootPath);
				}

				invoiceId = Integer
						.parseInt(retSaleReturnValue.split("Nxt")[1]);

				if (invoiceId > -1) {
					GraphReportHelper graphReportHelper = new GraphReportHelper();
					graphReportHelper.createTextReportAgent(invoiceId,
							rootPath, userBean.getUserId(),
							userBean.getOrgName());
				}
				returnValue = SUCCESS;
			} else
				returnValue = ERROR;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return returnValue;
	}

	public void verifyBooks() throws Exception {
		boolean isDispatch = false;
		DirectSaleBORetailerServiceImpl directSaleBORetailerServiceImpl = new DirectSaleBORetailerServiceImpl();
		DirectSaleBOHelper directSalehelper = new DirectSaleBOHelper();
		verifiedGameMap = new HashMap<String, List>();

		PwtTicketHelper pwtTicketHelper = new PwtTicketHelper();
		List<ActiveGameBean> activeGameList = pwtTicketHelper.getActiveGames();
		List<GameTicketFormatBean> gameFormatList = null;
		GameTicketFormatBean gameFormatBean = null;
		if (activeGameList != null && activeGameList.size() > 0) {
			gameFormatList = pwtTicketHelper
					.getGameTicketFormatList(activeGameList);
		} else {
			throw new LMSException("NO Active Game Exist");
		}
		System.out.println("heeeeeeeeeeeeeeeegggggggg "
				+ (String) ((String[]) getBookArr())[0]);
		System.out
				.println("heeeeeeeeeeeeeeeeggggggggcccc "
						+ ((String) ((String[]) getBookArr())[0]).split(",")
								.toString());
		System.out.println("game Name number "
				+ getGameName()[0].split(",").toString());

		HttpSession session = getRequest().getSession();
		UserInfoBean userInfoBean = (UserInfoBean) session
				.getAttribute("USER_INFO");
		boOrgId = userInfoBean.getUserOrgId();
		System.out.println("book numbervvvv "
				+ getBookArr()[0].split(",").length);

		Map<String, List<List<String>>> gameBookMap = directSaleBORetailerServiceImpl
				.getGameBookMapVerify(getGameName()[0].split(","),
						getBookArr()[0].split(","),
						getBookFromArr()[0].split(","),
						getBookTOArr()[0].split(","),
						getBookCountArr1()[0].split(","),
						getBookFromCountArr1()[0].split(","));

		Iterator gameBkMapItr = gameBookMap.entrySet().iterator();

		while (gameBkMapItr.hasNext()) {
			Map.Entry gameBkpair = (Map.Entry) gameBkMapItr.next();

			String gmName = (String) gameBkpair.getKey();
			int gameNbr = Integer.parseInt(gmName.split("-")[0]);
			int gameId = pwtTicketHelper.getGameId(activeGameList, gmName);

			for (int i = 0; i < gameFormatList.size(); i++) {
				gameFormatBean = gameFormatList.get(i);
				if (gameId == gameFormatBean.getGameId()) {
					break;
				}
			}

			int gameNbrDigits = gameFormatBean.getGameNbrDigits();
			int bkDigits = gameFormatBean.getBookDigits();
			List<List<String>> bkList = (List<List<String>>) gameBkpair
					.getValue();

			directSaleBORetailerServiceImpl.verifyBookSeries(bkList.get(1),
					bkList.get(0), gameId, gmName, gameNbr, gameNbrDigits,
					bkDigits, boOrgId, verifiedGameMap);
		}

		Map<Integer, List<String>> dispatchMap = new HashMap<Integer, List<String>>();

		StringBuilder jsString = new StringBuilder();
		Iterator itBkLs = verifiedGameMap.entrySet().iterator();
		Map msgCode = new HashMap();
		BookBean bkBean = null;
		BookSeriesBean bkSeBean = null;
		while (itBkLs.hasNext()) {
			List<String> validBkList = new ArrayList<String>();
			Map.Entry pairsBk = (Map.Entry) itBkLs.next();
			jsString.append(pairsBk.getKey() + "*G*");
			List bkList = (List) pairsBk.getValue();
			List bookList = (List) bkList.get(0);
			List bookSeList = (List) bkList.get(1);
			Iterator itbkList = bookList.iterator();
			Iterator itbkSeList = bookSeList.iterator();
			Iterator itbkSeAllList = ((List) bkList.get(2)).iterator();
			while (itbkList.hasNext()) {
				bkBean = (BookBean) itbkList.next();
				jsString.append(bkBean.getBookNumber() + ":"
						+ bkBean.getStatus() + "*M*");
				if (bkBean.getIsValid()) {
					validBkList.add(bkBean.getBookNumber());
				}
			}
			jsString.append("*G*");
			while (itbkSeList.hasNext()) {
				bkSeBean = (BookSeriesBean) itbkSeList.next();
				jsString.append(bkSeBean.getBookNbrFrom() + ":"
						+ bkSeBean.getBookNbrTo() + ":" + bkSeBean.getStatus()
						+ "*M*");
			}
			while (itbkSeAllList.hasNext()) {
				bkBean = (BookBean) itbkSeAllList.next();
				if (bkBean.getIsValid()) {
					validBkList.add(bkBean.getBookNumber());
				}
			}
			jsString.append("Nx*");
			if (validBkList.size() > 0) {
				dispatchMap.put(Integer.parseInt(((String) pairsBk.getKey())
						.split("-")[0]), validBkList);
			}
		}
		System.out.println(dispatchMap);
		session.setAttribute("DISPATCH_BOOK_MAP", dispatchMap);

		// calculate total sale amount for books
		isDispatch = directSalehelper.getSalePrice(dispatchMap,
				agtOrgName);
		if(isDispatch == true){
			isDispatch = directSalehelper.getSalePrice(dispatchMap,
					retOrgName);
		}
		
		jsString.append("Nx*" + isDispatch);
		PrintWriter out = getResponse().getWriter();
		response.setContentType("text/html");
		out.print(jsString);
		System.out.println(jsString);
	}

	public int getAgtOrgName() {
		return agtOrgName;
	}

	public String[] getBookArr() {
		return bookArr;
	}

	public int[] getBookCountArr() {
		return bookCountArr;
	}

	public String[] getBookCountArr1() {
		return bookCountArr1;
	}

	public String[] getBookFromArr() {
		return bookFromArr;
	}

	public int[] getBookFromCountArr() {
		return bookFromCountArr;
	}

	public String[] getBookFromCountArr1() {
		return bookFromCountArr1;
	}

	public String[] getBookNumber() {
		return bookNumber;
	}

	public String[] getBookTOArr() {
		return bookTOArr;
	}

	public int[] getBookToCountArr() {
		return bookToCountArr;
	}

	public String[] getBookToCountArr1() {
		return bookToCountArr1;
	}

	public String[] getGameName() {
		return gameName;
	}

	public int getGamesToDisplay() {
		return gamesToDisplay;
	}

	public HttpServletRequest getRequest() {
		return request;
	}

	public HttpServletResponse getResponse() {
		return response;
	}

	public Map<String, List> getVerifiedGameMap() {
		return verifiedGameMap;
	}

	public void setAgtOrgName(int agtOrgName) {
		this.agtOrgName = agtOrgName;
	}

	public void setBookArr(String[] bookArr) {
		this.bookArr = bookArr;
	}

	public void setBookCountArr(int[] bookCountArr) {
		this.bookCountArr = bookCountArr;
	}

	public void setBookCountArr1(String[] bookCountArr1) {
		this.bookCountArr1 = bookCountArr1;
	}

	public void setBookFromArr(String[] bookFromArr) {
		this.bookFromArr = bookFromArr;
	}

	public void setBookFromCountArr(int[] bookFromCountArr) {
		this.bookFromCountArr = bookFromCountArr;
	}

	public void setBookFromCountArr1(String[] bookFromCountArr1) {
		this.bookFromCountArr1 = bookFromCountArr1;
	}

	public void setBookNumber(String[] bookNumber) {
		this.bookNumber = bookNumber;
	}

	public void setBookTOArr(String[] bookTOArr) {
		this.bookTOArr = bookTOArr;
	}

	public void setBookToCountArr(int[] bookToCountArr) {
		this.bookToCountArr = bookToCountArr;
	}

	public void setBookToCountArr1(String[] bookToCountArr1) {
		this.bookToCountArr1 = bookToCountArr1;
	}

	public void setGameName(String[] gameName) {
		this.gameName = gameName;
	}

	// Methods By Gaurav For Agent Book Series and Book Verification

	public void setGamesToDisplay(int gamesToDisplay) {
		this.gamesToDisplay = gamesToDisplay;
	}

	public void setServletRequest(HttpServletRequest request) {
		this.request = request;
	}

	public void setServletResponse(HttpServletResponse response) {
		this.response = response;
	}

	public void setVerifiedGameMap(Map<String, List> verifiedGameMap) {
		this.verifiedGameMap = verifiedGameMap;
	}

	public int getAgtOrgId() {
		return agtOrgId;
	}

	public void setAgtOrgId(int agtOrgId) {
		this.agtOrgId = agtOrgId;
	}

	public int getRetOrgName() {
		return retOrgName;
	}

	public void setRetOrgName(int retOrgName) {
		this.retOrgName = retOrgName;
	}

}