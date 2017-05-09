package com.skilrock.lms.web.scratchService.inventoryMgmt.common;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

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
import com.skilrock.lms.coreEngine.scratchService.inventoryMgmt.common.DirectSaleAgentHelper;
import com.skilrock.lms.coreEngine.scratchService.pwtMgmt.common.PwtTicketHelper;

public class DirectSaleAgentAction extends ActionSupport implements
		ServletRequestAware, ServletResponseAware {
	static Log logger = LogFactory.getLog(DirectSaleAgentAction.class);

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static void main(String[] args) {
		DirectSaleAgentAction tt = new DirectSaleAgentAction();
		String bookFromArr = "111-001001";
		String bookTOArr = "111-001005";
		System.out.println(tt.getBookListFromSeriesVerify(bookFromArr,
				bookTOArr));
	}

	private int agentOrgId;

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
	// private String gameName;
	private String[] gameName;
	private int gamesToDisplay;

	HttpServletRequest request;

	private HttpServletResponse response;
	private int retOrgName;

	private Map<String, List> verifiedGameMap;

	private List<BookBean> copyBookValues(List<BookBean> bookList,
			List<String> frontBookList, int gameNbrDigits) {
		BookBean bookBean = null;

		System.out.println("BookNbr Front Book List::" + frontBookList);

		System.out.println("BookNbr.length:" + frontBookList.size());

		if (frontBookList != null && frontBookList.size() > 0) {
			String bookVal = null;
			Iterator bkItr = frontBookList.iterator();
			while (bkItr.hasNext()) {
				int dup = 0;
				bookVal = ((String) bkItr.next()).trim();
				System.out.println(bookVal);

				if (bookVal.indexOf("-") == -1
						&& bookVal.length() > gameNbrDigits) {
					bookVal = bookVal.substring(0, gameNbrDigits) + "-"
							+ bookVal.substring(gameNbrDigits);
					bookBean = new BookBean();
					bookBean.setValid(false);
					bookBean.setBookNumber(bookVal);
					for (int i = 0; i < frontBookList.size(); i++) {
						if (bookVal.replaceAll("-", "").equals(
								frontBookList.get(i))) {
							dup++;
						}
					}
					if (dup > 1) {
						bookBean.setStatus("Duplicate Book");
					}
					bookList.add(bookBean);
				}
			}
			// System.out.println("After Setting BookList in copyBookValues::" +
			// bookList);
		}
		return bookList;
	}

	public String dispatchOrder() throws LMSException {
		ServletContext sc = ServletActionContext.getServletContext();
		HttpSession session = getRequest().getSession();
		UserInfoBean userInfoBean = (UserInfoBean) session
				.getAttribute("USER_INFO");
		int agtId = userInfoBean.getUserId();
		int agtOrgId = userInfoBean.getUserOrgId();
		String newBookStatus = "INACTIVE";
		String rootPath = (String) session.getAttribute("ROOT_PATH");
		DirectSaleAgentHelper directSalehelper = new DirectSaleAgentHelper();
		Map<Integer, List<String>> gameBookMap = (Map<Integer, List<String>>) session
				.getAttribute("DISPATCH_BOOK_MAP");
		if (gameBookMap.size() > 0) {
			if ("AGENT-RETAILER".equals((String) sc
					.getAttribute("BOOK_ACTIVATION_AT"))) {
				newBookStatus = "ACTIVE";
			} else if ("BO-AGENT".equals((String) sc
					.getAttribute("BOOK_ACTIVATION_AT"))) {
				newBookStatus = "ACTIVE";
			}
			int DCID = directSalehelper.dispatchOrderDirectSale(gameBookMap,
					agtId, agtOrgId, retOrgName, rootPath, userInfoBean
							.getOrgName(), newBookStatus);
			session.setAttribute("DEL_CHALLAN_ID", DCID);
			return SUCCESS;
		} else {
			return ERROR;
		}

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

	public List<String> getBookListFromSeries(String bookFromArr,
			String bookTOArr) {
		List<String> bookList = new ArrayList<String>();

		String[] bookNbrFrmStr = null;
		String[] bookNbrToStr = null;
		StringTokenizer bookNbrFrmTok = new StringTokenizer(bookFromArr, ",");
		StringTokenizer bookNbrToTok = new StringTokenizer(bookTOArr, ",");
		bookNbrFrmStr = new String[bookNbrFrmTok.countTokens()];
		bookNbrToStr = new String[bookNbrToTok.countTokens()];

		// System.out.println("book from str " + bookNbrFrmStr);
		// System.out.println("book to str " + bookNbrToStr);

		int frmTok = 0;
		while (bookNbrFrmTok.hasMoreTokens()) {
			bookNbrFrmStr[frmTok] = bookNbrFrmTok.nextToken();
			bookNbrToStr[frmTok] = bookNbrToTok.nextToken();
			frmTok = frmTok + 1;
		}
		// System.out.println("---Series length"+bookNbrFrmStr);

		if (bookNbrFrmStr != null) {
			for (int seriesNo = 0; seriesNo < bookNbrFrmStr.length; seriesNo++) {
				if (bookNbrFrmStr[seriesNo] != "") {

					String bookNbrFrom = bookNbrFrmStr[seriesNo];
					String bookNbrTo = bookNbrToStr[seriesNo];

					int bookNbrFrmInt = Integer.parseInt(bookNbrFrom
							.replaceAll("-", ""));
					int bookNbrToInt = Integer.parseInt(bookNbrTo.replaceAll(
							"-", ""));
					int noOfbooks = bookNbrToInt - bookNbrFrmInt;

					for (int i = 0; i < noOfbooks + 1; i++) {
						String bookNbr = String.valueOf(bookNbrFrmInt);
						if (bookNbr.indexOf("-") == -1) {
							bookNbr = bookNbr.substring(0, 3) + "-"
									+ bookNbr.substring(3);
							System.out.println("New book nbr:::" + bookNbr);

						}
						bookList.add(bookNbr);
						bookNbrFrmInt++;
					}
				}
			}
		}

		return bookList;
	}

	public List<String> getBookListFromSeriesVerify(String bookFromArr,
			String bookTOArr) {
		List<String> bookList = new ArrayList<String>();

		String[] bookNbrFrmStr = null;
		String[] bookNbrToStr = null;
		StringTokenizer bookNbrFrmTok = new StringTokenizer(bookFromArr, ",");
		StringTokenizer bookNbrToTok = new StringTokenizer(bookTOArr, ",");
		bookNbrFrmStr = new String[bookNbrFrmTok.countTokens()];
		bookNbrToStr = new String[bookNbrToTok.countTokens()];

		System.out.println("from tokens " + bookNbrFrmStr);
		System.out.println("to tokens " + bookNbrToStr);

		int frmTok = 0;
		while (bookNbrFrmTok.hasMoreTokens()) {
			bookNbrFrmStr[frmTok] = bookNbrFrmTok.nextToken();
			bookNbrToStr[frmTok] = bookNbrToTok.nextToken();
			frmTok = frmTok + 1;
		}

		// System.out.println("---Series length"+bookNbrFrmStr);

		if (bookNbrFrmStr != null) {
			for (int seriesNo = 0; seriesNo < bookNbrFrmStr.length; seriesNo++) {
				if (bookNbrFrmStr[seriesNo] != "") {

					String bookNbrFrom = bookNbrFrmStr[seriesNo];
					String bookNbrTo = bookNbrToStr[seriesNo];

					int bookNbrFrmInt = Integer.parseInt(bookNbrFrom
							.replaceAll("-", ""));
					int bookNbrToInt = Integer.parseInt(bookNbrTo.replaceAll(
							"-", ""));
					int noOfbooks = bookNbrToInt - bookNbrFrmInt;

					for (int i = 0; i < noOfbooks + 1; i++) {
						String bookNbr = String.valueOf(bookNbrFrmInt);
						if (bookNbr.length() < 9) {
							// to be applied
							break;
						}

						System.out.println("book number " + bookNbr);
						if (bookNbr.indexOf("-") == -1) {
							bookNbr = bookNbr.substring(0, 3) + "-"
									+ bookNbr.substring(3);
							// System.out.println("New book nbr:::" + bookNbr);

						}
						bookList.add(bookNbr);
						bookNbrFrmInt++;
					}
				}
			}
		}

		return bookList;
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

	public Map<String, List<String>> getGameBookMap(String[] gameName,
			String[] bookArr, String[] bookFromArr, String[] bookTOArr,
			int[] bookCountArr, int[] bookFromCountArr, int[] bookToCountArr) {
		// System.out.println(bookArr.length);
		System.out.println(bookArr[0]);
		Map<String, List<String>> gameBookMap = new HashMap<String, List<String>>();
		int bookStart = 0;
		int bookSeriesStart = 0;
		System.out.println("book count arr " + bookCountArr.length);
		System.out.println("book series count arr " + bookFromCountArr.length);

		for (int i = 0; i < gameName.length; i++) {
			System.out.println("inside main loop " + gameName[i]);

			List<String> bookList = new ArrayList<String>();
			List<String> bookSeriesList = new ArrayList<String>();
			// int bookStart=0;
			int bookEnd = bookCountArr[i] + bookStart;
			// int bookSeriesStart=0;
			int bookSeriesEnd = bookFromCountArr[i] + bookSeriesStart;

			System.out.println("book start" + bookStart);
			System.out.println("book end " + bookEnd);

			for (int start = bookStart; start < bookEnd; start++) {
				System.out.println("inside book loop at  " + start + ":: "
						+ bookArr[start]);
				bookList.add(bookArr[start]);
				bookStart++;
			}
			System.out.println("book series start" + bookSeriesStart);
			System.out.println("book series  end " + bookSeriesEnd);
			for (int start = bookSeriesStart; start < bookSeriesEnd; start++) {
				System.out.println("inside series loop");
				bookSeriesList = getBookListFromSeries(bookFromArr[start],
						bookTOArr[start]);
				bookList.addAll(bookSeriesList);
				bookSeriesStart++;
			}

			// bookStart=bookStart;
			// bookSeriesStart=bookSeriesStart;
			System.out.println(bookList);

			gameBookMap.put(gameName[i], bookList);
		}

		return gameBookMap;

	}

	public Map<String, List<List<String>>> getGameBookMapVerify(
			String[] gameName, String[] bookArr, String[] bookFromArr,
			String[] bookTOArr, String[] bookCountArr, String[] bookFromCountArr) {

		Map<String, List<List<String>>> gameBookMap = new HashMap<String, List<List<String>>>();

		int bookStart = 0;
		int bookSeriesStart = 0;

		for (int i = 0; i < gameName.length; i++) {

			if (!gameName[i].equals("-1") && !gameName[i].equals("")) {
				List<String> bookList = new ArrayList<String>();
				List<String> bookSeriesList = new ArrayList<String>();
				List<List<String>> bkAndbkSerList = new ArrayList<List<String>>();
				int bookEnd = Integer.parseInt(bookCountArr[i]) + bookStart;
				int bookSeriesEnd = Integer.parseInt(bookFromCountArr[i])
						+ bookSeriesStart;

				for (int start = bookStart; start < bookEnd; start++) {
					if (!bookArr[start].equals("")) {
						bookList.add(bookArr[start].replaceAll("-", ""));
					}
					bookStart++;
				}
				bkAndbkSerList.add(bookList);
				for (int start = bookSeriesStart; start < bookSeriesEnd; start++) {
					if (!bookFromArr[start].equals("")
							&& !bookFromArr[start].equals("")) {
						bookSeriesList.add(bookFromArr[start].replaceAll("-",
								"")
								+ ":" + bookTOArr[start].replaceAll("-", ""));
					}
					bookSeriesStart++;
				}
				bkAndbkSerList.add(bookSeriesList);
				gameBookMap.put(gameName[i], bkAndbkSerList);

			} else {
				bookStart = Integer.parseInt(bookCountArr[i]) + bookStart;
				bookSeriesStart = Integer.parseInt(bookFromCountArr[i])
						+ bookSeriesStart;
			}
		}
		System.out.println(" Game Book Map**" + gameBookMap);

		return gameBookMap;

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

	public String getRetailerAndGameName() throws LMSException {
		/*
		 * HttpSession session=getRequest().getSession(); UserInfoBean
		 * userInfoBean = (UserInfoBean) session.getAttribute("USER_INFO");
		 * 
		 * ServletContext sc=ServletActionContext.getServletContext(); //
		 * gamesToDisplay =(Integer)sc.getAttribute("NUMBER_OF_GAMES_PER_PAGE");
		 * 
		 * DirectSaleAgentHelper directSalehelper=new DirectSaleAgentHelper();
		 * session.setAttribute("RETAILER_LIST",directSalehelper.getRetailers(userInfoBean.getUserOrgId()));
		 * session.setAttribute("GAME_LIST",directSalehelper.getGames());
		 */
		return SUCCESS;
	}

	public int getRetOrgName() {
		return retOrgName;
	}

	public Map<String, List> getVerifiedGameMap() {
		return verifiedGameMap;
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

	public void setGamesToDisplay(int gamesToDisplay) {
		this.gamesToDisplay = gamesToDisplay;
	}

	public void setRetOrgName(int retOrgName) {
		this.retOrgName = retOrgName;
	}

	public void setServletRequest(HttpServletRequest request) {
		this.request = request;
	}

	// Methods By Gaurav For Agent Book Series and Book Verification

	public void setServletResponse(HttpServletResponse response) {
		this.response = response;
	}

	public void setVerifiedGameMap(Map<String, List> verifiedGameMap) {
		this.verifiedGameMap = verifiedGameMap;
	}

	public List<BookBean> verifyBook(String bookNbr, List<BookBean> bookList,
			int gameId, int gameNbr, Connection connection) throws Exception {
		boolean isValid = false;
		DirectSaleAgentHelper helper = new DirectSaleAgentHelper();

		// System.out.println(isValid+"*********"+bookNbr);
		if (bookList != null) {
			isValid = helper.verifyBook(agentOrgId, bookNbr, gameId, gameNbr,
					connection);
			for (BookBean bean : bookList) {
				if (bookNbr.equals(bean.getBookNumber())) {
					if (!isValid) {
						bean.setValid(false);
						bean.setStatus("Wrong Book Number");
						// System.out.println(isValid+"***isDuplicate-if*****"+bookNbr);

					} else {
						bean.setValid(true);
						bean.setStatus(null);
						// System.out.println(isValid+"***isDuplicate-else*****"+bookNbr);

					}
				}
			}

		}
		return bookList;
	}

	public void verifyBooks() throws Exception {
		DirectSaleAgentHelper directSalehelper = new DirectSaleAgentHelper();
		verifiedGameMap = new HashMap<String, List>();

		PwtTicketHelper pwtTicketHelper = new PwtTicketHelper();
		List<ActiveGameBean> activeGameList = pwtTicketHelper.getActiveGames();
		List<GameTicketFormatBean> gameFormatList = null;
		GameTicketFormatBean gameFormatBean = null;
		if (activeGameList != null && activeGameList.size() > 0) {
			gameFormatList = pwtTicketHelper
					.getGameTicketFormatList(activeGameList);
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
		agentOrgId = userInfoBean.getUserOrgId();
		System.out.println("book numbervvvv "
				+ getBookArr()[0].split(",").length);

		Map<String, List<List<String>>> gameBookMap = getGameBookMapVerify(
				getGameName()[0].split(","), getBookArr()[0].split(","),
				getBookFromArr()[0].split(","), getBookTOArr()[0].split(","),
				getBookCountArr1()[0].split(","), getBookFromCountArr1()[0]
						.split(","));

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

			verifyBookSeries(bkList.get(1), bkList.get(0), gameId, gmName,
					gameNbr, gameNbrDigits, bkDigits);
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
		boolean isDispatch = directSalehelper.getSalePrice(dispatchMap,
				retOrgName);
		jsString.append("Nx*" + isDispatch);
		PrintWriter out = getResponse().getWriter();
		response.setContentType("text/html");
		out.print(jsString);
		System.out.println(jsString);
	}

	public void verifyBookSeries(List<String> bkSerList,
			List<String> frontBookList, int gameId, String gameName,
			int gameNbr, int gameNbrDigits, int bkDigits) throws LMSException {

		// global connection to be used everywhere
		Connection connection = null;
		 
		connection = DBConnect.getConnection();

		List verifiedList = new ArrayList();
		boolean isValid = false;
		boolean isSeriesValid = true;

		List<BookBean> bookList = new ArrayList();
		List<BookBean> bookSeriesList = new ArrayList();
		List bookSeriesAll = new ArrayList();

		System.out.println("---Series length" + bkSerList.size());

		if (bkSerList != null) {
			for (int seriesNo = 0; seriesNo < bkSerList.size(); seriesNo++) {
				BookSeriesBean bookSeBean = new BookSeriesBean();
				bookSeBean
						.setBookNbrFrom(bkSerList.get(seriesNo).split(":")[0]);
				bookSeBean.setBookNbrTo(bkSerList.get(seriesNo).split(":")[1]);
				bookSeBean.setStatus("");
				bookSeBean.setValid(isValid);

				String bookNbrFrom = bkSerList.get(seriesNo).split(":")[0]
						.replaceAll("-", "");
				String bookNbrTo = bkSerList.get(seriesNo).split(":")[1]
						.replaceAll("-", "");
				if (bookNbrFrom.substring(0, bookNbrFrom.length() - bkDigits)
						.equals(
								bookNbrTo.substring(0, bookNbrTo.length()
										- bkDigits))) {
					int bookNbrFrmInt = Integer.parseInt(bookNbrFrom.substring(
							bookNbrFrom.length() - bkDigits, bookNbrFrom
									.length()));
					int bookNbrToInt = Integer.parseInt(bookNbrTo.substring(
							bookNbrTo.length() - bkDigits, bookNbrTo.length()));
					int noOfbooks = bookNbrToInt - bookNbrFrmInt;

					for (int i = 0; i < noOfbooks + 1; i++) {

						String bookNbr = String.valueOf(bookNbrFrom.substring(
								0, (bookNbrFrom.length() - ("" + bookNbrFrmInt)
										.length()))
								+ bookNbrFrmInt);

						if (bookNbr != null && !bookNbr.trim().equals("")) {
							// add hyphens if necessary

							// System.out.println(gameNbrDigits+":::::::::" +
							// bookNbrFrom);
							bookNbr = bookNbr.substring(0, gameNbrDigits) + "-"
									+ bookNbr.substring(gameNbrDigits);
							System.out.println("New book nbr:::" + bookNbr);
							DirectSaleAgentHelper helper = new DirectSaleAgentHelper();
							isValid = helper.verifyBook(agentOrgId, bookNbr,
									gameId, gameNbr, connection);

							if (isValid) {
								BookBean bookBean = new BookBean();
								bookBean.setValid(true);
								bookBean.setBookNumber(bookNbr);

								for (BookBean bean : bookSeriesList) {
									if (bookNbr.trim().equals(
											bean.getBookNumber().trim())) {
										isSeriesValid = false;
										bookSeBean
												.setStatus("Series Contains Tickets of Another Series");
										bookSeBean.setValid(false);
										break;// New series contains ticket of
										// old series
									}
								}
								if (isSeriesValid) {
									bookSeriesList.add(bookBean);
								}

							} else {
								isSeriesValid = false;
								bookSeBean.setStatus("Series Not Valid");
								break;// Series not valid
							}
						}
						// System.out.println("inside for of verifyBookSeries");
						bookNbrFrmInt++;

					}
				} else {
					isSeriesValid = false;
					bookSeBean.setStatus("Series Not Valid");
				}
				if (isSeriesValid) {
					bookSeBean.setValid(true);
				}
				bookSeriesAll.add(bookSeBean);
			}
		}
		bookList = verifyIndividualBooks(bookList, frontBookList, gameId,
				gameNbr, gameNbrDigits, connection);

		for (int i = 0; i < bookSeriesList.size(); i++) {
			for (int j = 0; j < bookList.size(); j++) {
				// System.out.println(bookSeriesList.size()+"-Gaura
				// Test--"+bookList.size());
				if (((BookBean) bookList.get(j)).getBookNumber().equals(
						((BookBean) bookSeriesList.get(i)).getBookNumber())) {
					BookBean bean = (BookBean) bookList.get(j);
					bean.setValid(false);
					bean.setStatus("Book Number already in Book Series");
				}
			}
		}
		verifiedList.add(bookList);
		verifiedList.add(bookSeriesAll);
		verifiedList.add(bookSeriesList);
		verifiedGameMap.put(gameName, verifiedList);
		System.out.println(gameName + "***" + verifiedGameMap);

		try {
			if (connection != null) {
				connection.close();
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public List<BookBean> verifyIndividualBooks(List<BookBean> bookList,
			List<String> frontBookList, int gameId, int gameNbr,
			int gameNbrDigits, Connection connection) throws LMSException {

		bookList = copyBookValues(bookList, frontBookList, gameNbrDigits);
		for (BookBean bean : bookList) {
			try {
				System.out.println(bean.getBookNumber() + "***"
						+ bean.getStatus());

				if (bean.getStatus() == null) {
					bookList = verifyBook(bean.getBookNumber(), bookList,
							gameId, gameNbr, connection);
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				throw new LMSException("sqlException", e);
			}
		}
		return bookList;
	}
}