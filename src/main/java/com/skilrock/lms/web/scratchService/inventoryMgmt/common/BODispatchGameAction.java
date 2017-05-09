/*
 * � copyright 2007, SkilRock Technologies, A division of Sugal & Damani Lottery Agency Pvt. Ltd.
 * All Rights Reserved
 * The contents of this file are the property of Sugal & Damani Lottery Agency Pvt. Ltd.
 * and are subject to a License agreement with Sugal & Damani Lottery Agency Pvt. Ltd.; you may
 * not use this file except in compliance with that License.  You may obtain a
 * copy of that license from:
 * Legal Department
 * Sugal & Damani Lottery Agency Pvt. Ltd.
 * 6/35,WEA, Karol Bagh,
 * New Delhi
 * India - 110005
 * This software is distributed under the License and is provided on an �AS IS�
 * basis, without warranty of any kind, either express or implied, unless
 * otherwise provided in the License.  See the License for governing rights and
 * limitations under the License.
 */

package com.skilrock.lms.web.scratchService.inventoryMgmt.common;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;

import com.opensymphony.xwork2.ActionSupport;
import com.skilrock.lms.GameContants;
import com.skilrock.lms.beans.BookBean;
import com.skilrock.lms.beans.BookSeriesBean;
import com.skilrock.lms.beans.DispatchOrderResponse;
import com.skilrock.lms.beans.InvOrderBean;
import com.skilrock.lms.beans.OrderBean;
import com.skilrock.lms.beans.OrderedGameBean;
import com.skilrock.lms.beans.OrgAddressBean;
import com.skilrock.lms.beans.PackBean;
import com.skilrock.lms.beans.UserInfoBean;
import com.skilrock.lms.common.db.DBConnect;
import com.skilrock.lms.common.db.TableConstants;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.coreEngine.scratchService.inventoryMgmt.common.BODispatchGameHelper;
import com.skilrock.lms.coreEngine.scratchService.orderMgmt.common.BOOrderProcessHelper;

/**
 * This class provides methods for handling the order dispatch at BO's end
 * 
 * @author Skilrock Technologies
 * 
 */
public class BODispatchGameAction extends ActionSupport implements
		ServletRequestAware, ServletResponseAware {
	public static final String APPLICATION_ERROR = "applicationError";

	static Log logger = LogFactory.getLog(BODispatchGameAction.class);

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int bookCountId;

	private List<BookBean> bookList;
	private String[] bookNbr;

	String[] bookNbrFrmStr = null;
	private String bookNbrFrom;
	private Object bookNbrFromArr;

	private String bookNbrTo;

	private Object bookNbrToArr;
	String[] bookNbrToStr = null;
	private String bookNbrToVerify;
	private double bookPrice;
	private List bookSeriesAll;
	private List<BookBean> bookSeriesList;

	private int defaultNbrOfBooks = 5;

	private int gameId;
	private boolean isAddBookEnabled;

	private boolean isInValidEntry;
	private boolean isProceedNext;

	private int noOfBooksToDispatch;

	private OrderedGameBean orderdedGame;
	private int packCountId;

	private List<PackBean> packList;

	private String[] packNbr;

	private String packNbrToVerify;

	private HttpServletRequest request;
	private HttpServletResponse response;

	/**
	 * This method is called when the Add Book button is pressed
	 * 
	 * @return String
	 */
	public String addBook() {

		System.out.println("---------I am in ADD Book--------------");

		// reInitialize();

		HttpServletResponse response = getResponse();
		response.setHeader("Pragma", "no-cache"); // HTTP 1.0
		response.setHeader("Cache-Control", "no-cache"); // HTTP 1.1
		response.setDateHeader("Expires", 0); // prevents caching at the proxy
		// server
		response.setHeader("Cache-Control", "private"); // HTTP 1.1
		response.setHeader("Cache-Control", "no-store"); // HTTP 1.1
		response.setHeader("Cache-Control", "max-stale=0"); // HTTP 1.1

		// System.out.println("add bookkkkkkkkkkkkk::" +
		// request.getParameter("hidBookNbr"));

		// System.out.println("add bookkkkkkkkkkkkk::" + getHidBookNbr());

		HttpSession session = getRequest().getSession();
		List<BookBean> bookList = (List<BookBean>) session
				.getAttribute("BOOK_LIST");
		List<PackBean> packList = (List<PackBean>) session
				.getAttribute("PACK_LIST");

		BookBean bookBean = null;
		if (bookList != null) {

			copyBookValues(bookList);
			bookBean = new BookBean();
			bookBean.setValid(true);
			bookList.add(bookBean);
			setBookList(bookList);
		}

		/*
		 * try { PrintWriter out = getResponse().getWriter(); out.write("This is
		 * Aman Chawla"); } catch (IOException e) { // TODO Auto-generated catch
		 * block e.printStackTrace(); }
		 */

		return SUCCESS;

	}

	private void changeDispatchBooks() {

		HttpSession session = getRequest().getSession();

		List<PackBean> packList = (List<PackBean>) session
				.getAttribute("PACK_LIST");

		List<BookBean> bookList = (List<BookBean>) session
				.getAttribute("BOOK_LIST");

		List<BookBean> bookSeriesList = (List<BookBean>) session
				.getAttribute("BOOK_SERIES_LIST");

		// for books
		String bookNbr = null;

		System.out
				.println("-----:::::::::::::::::::::::::::::::::::::::::::::::::::::::"
						+ getNoOfBooksToDispatch());

		if (bookList != null) {
			for (BookBean bean : bookList) {
				bookNbr = bean.getBookNumber();

				if (bookNbr != null && !bookNbr.trim().equals("")) {

					if (bean.getIsValid()) {
						setNoOfBooksToDispatch(getNoOfBooksToDispatch() + 1);
					}
				}

			}
		}

		// for book Series List
		if (bookSeriesList != null) {
			for (BookBean bean : bookSeriesList) {
				bookNbr = bean.getBookNumber();

				if (bookNbr != null && !bookNbr.trim().equals("")) {

					if (bean.getIsValid()) {
						setNoOfBooksToDispatch(getNoOfBooksToDispatch() + 1);
					}

				}

			}
		}
		// for packs
		String packNbr = null;
		if (packList != null) {

			OrderedGameBean gameBean = (OrderedGameBean) session
					.getAttribute("ORDERED_GAME");

			int booksPerPack = gameBean.getNbrOfBooksPerPack();

			for (PackBean bean : packList) {
				packNbr = bean.getPackNumber();

				if (packNbr != null && !packNbr.trim().equals("")) {

					if (bean.getIsValid()) {

						setNoOfBooksToDispatch(getNoOfBooksToDispatch()
								+ booksPerPack);
					}
				}

			}
		}

	}

	public void checkValidBookInseries(List<BookBean> bookSeriesList) {
		HttpSession session = getRequest().getSession();
		List bookSeriesAll = (ArrayList) session
				.getAttribute("BOOK_SERIES_ALL");
		String bookNbr = null;
		if (bookSeriesList != null) {
			for (BookBean bean : bookSeriesList) {
				bookNbr = bean.getBookNumber();

				if (bookNbr != null && !bookNbr.trim().equals("")) {
					// System.out.println("--in
					// book=series---"+bean.getIsValid());

					if (!bean.getIsValid()) {
						int bookNum = Integer.parseInt(bookNbr.replaceAll("-",
								""));
						BookSeriesBean bookSeBean;
						for (int i = 0; i < bookSeriesAll.size(); i++) {
							bookSeBean = (BookSeriesBean) bookSeriesAll.get(i);
							System.out.println(bookSeBean.getBookNbrFrom()
									+ "--in bookserieslist---" + bookNum
									+ "--------" + bookSeBean.getBookNbrTo());

							if (Integer.parseInt(bookSeBean.getBookNbrFrom()
									.replaceAll("-", "")) <= bookNum
									&& Integer
											.parseInt(bookSeBean.getBookNbrTo()
													.replaceAll("-", "")) >= bookNum) {
								// System.out.println(bookSeBean.getBookNbrFrom()+"--in
								// bookserieslist---"+bookNum);
								bookSeBean
										.setStatus("Tickets in Series are Already in Pack");
								setBookSeriesAll(bookSeriesAll);
							}
						}
					}
				}

			}
		}

		session.setAttribute("BOOK_SERIES_ALL", bookSeriesAll);
		// System.out.println("--in bookserieslist-after
		// sett--"+((BookSeriesBean)bookSeriesAll.get(0)).getStatus());
	}

	private void copyBookValues(List<BookBean> bookList) {
		BookBean bookBean = null;

		System.out.println("BookNbr Array::" + getBookNbr());

		System.out
				.println("Nbr of boks to dispatch" + getNoOfBooksToDispatch());

		String[] bookNbr = getBookNbr();
		System.out.println("BookNbr.length:" + bookNbr.length);

		HttpSession session = getRequest().getSession();
		OrderedGameBean orderedGameBean = (OrderedGameBean) session
				.getAttribute("ORDERED_GAME");

		if (bookNbr != null) {

			if (bookNbr.length == 1) {

				String bookVal = null;
				StringTokenizer bookTokens = new StringTokenizer(bookNbr[0],
						",");

				System.out.println("Value Passed::" + bookNbr[0]);
				System.out.println("BookTokens::" + bookTokens.countTokens());

				int i = 0;
				while (bookTokens.hasMoreTokens()) {

					bookVal = bookTokens.nextToken();
					System.out.println(bookVal);

					bookVal = bookVal.trim();

					if (bookVal.indexOf("-") == -1
							&& bookVal.length() > orderedGameBean
									.getGameNbrDigits()) {
						bookVal = bookVal.substring(0, orderedGameBean
								.getGameNbrDigits())
								+ "-"
								+ bookVal.substring(orderedGameBean
										.getGameNbrDigits());
						bookBean = new BookBean();
						bookBean.setValid(true);
						bookBean.setBookNumber(bookVal);
						bookList.add(bookBean);

					}
					i = i + 1;
				}
				setBookList(bookList);
				System.out.println("After Setting BookList in copyBookValues::"
						+ bookList);
			}
		}

		/*
		 * for(String b : bookNbr) { System.out.println("Book:::" + b); }
		 * 
		 * 
		 * 
		 * if (bookNbr != null) { System.out.println("Inside NOt Null ---BookNbr
		 * Array::" + getBookNbr()); for (int i = 0; i < bookNbr.length; i++) {
		 * if (!bookNbr[i].trim().equals("")) { bookBean = new BookBean();
		 * bookBean.setBookNumber(bookNbr[i]); bookList.add(bookBean); } } }
		 */

	}

	private void copyPackValues(List<PackBean> packList) {
		PackBean packBean = null;

		System.out.println("PackNbr Array::" + getPackNbr());

		String[] packNbr = getPackNbr();
		System.out.println("PackNbr.length:" + packNbr.length);

		HttpSession session = getRequest().getSession();
		OrderedGameBean orderedGameBean = (OrderedGameBean) session
				.getAttribute("ORDERED_GAME");

		if (packNbr != null) {

			if (packNbr.length == 1) {

				String packVal = null;
				StringTokenizer packTokens = new StringTokenizer(packNbr[0],
						",");

				System.out.println("Value Passed::" + packNbr[0]);
				System.out.println("PackTokens::" + packTokens.countTokens());

				int i = 0;
				while (packTokens.hasMoreTokens()) {

					packVal = packTokens.nextToken();

					packVal = packVal.trim();

					if (packVal.indexOf("-") == -1
							&& packVal.length() > orderedGameBean
									.getGameNbrDigits()) {
						packVal = packVal.substring(0, orderedGameBean
								.getGameNbrDigits())
								+ "-"
								+ packVal.substring(orderedGameBean
										.getGameNbrDigits());
					}

					System.out.println(packVal);
					packBean = packList.get(i++);
					packBean.setPackNumber(packVal);
				}
			}
		}
	}

	/**
	 * This method is used to display the dispatch game page
	 * 
	 * @return String
	 */
	public String dispatchGame() {

		HttpSession session = getRequest().getSession();
		OrderedGameBean bean = (OrderedGameBean) session
				.getAttribute("ORDERED_GAME");
		double remainingAvailableCreditLimit = ((Double) session
				.getAttribute("REMAINING_AVAILABLE_CREDIT_AMT")).doubleValue();
		remainingAvailableCreditLimit = remainingAvailableCreditLimit
				- getBookPrice() * getNoOfBooksToDispatch();
		session.setAttribute("REMAINING_AVAILABLE_CREDIT_AMT",
				remainingAvailableCreditLimit);
		System.out.println("ramaining amount is &&&&&&&&&&&&&   "
				+ remainingAvailableCreditLimit);

		System.out
				.println("-----No of already dispatched books in the ordred bean"
						+ bean.getNbrOfBooksDlvrd() + "----------");

		List<PackBean> packList = null;
		List<BookBean> bookList = null;
		List<BookBean> bookSeriesList = new ArrayList<BookBean>();
		List bookSeriesAll = new ArrayList();

		int nbrOfAppBooks;
		int nbrOfBooksPerPack;
		int nbrOfAppBooksRemain;
		if (bean != null) {
			setOrderdedGame(bean);

			nbrOfAppBooks = bean.getNbrOfBooksApp();
			nbrOfAppBooksRemain = bean.getRemainingBooksToDispatch();
			nbrOfBooksPerPack = bean.getNbrOfBooksPerPack();

			if (nbrOfAppBooksRemain >= nbrOfBooksPerPack) {
				int nbrOfPacks = nbrOfAppBooksRemain / nbrOfBooksPerPack;

				if (nbrOfPacks > 0) {

					packList = new ArrayList<PackBean>();

					/*
					 * if (nbrOfPacks > defaultNbrOfPacks) { nbrOfPacks =
					 * defaultNbrOfPacks; }
					 */

					PackBean packBean = null;
					for (int i = 0; i < nbrOfPacks; i++) {
						packBean = new PackBean();
						packBean.setValid(true);
						packList.add(packBean);

					}

					setPackList(packList);
					// session.setAttribute("PACK_LIST", packList);
				}
			}

			System.out.println("About to set default books");
			bookList = new ArrayList<BookBean>();
			BookBean bookBean = null;

			int nbrOfBooksToAdd = 0;
			if (nbrOfAppBooksRemain > defaultNbrOfBooks) {
				nbrOfBooksToAdd = defaultNbrOfBooks;
				setAddBookEnabled(true);

			} else {
				nbrOfBooksToAdd = nbrOfAppBooksRemain;
			}

			for (int i = 0; i < nbrOfBooksToAdd; i++) {
				bookBean = new BookBean();
				bookBean.setValid(true);

				bookList.add(bookBean);
			}

			setBookList(bookList);
			// session.setAttribute("BOOK_LIST", bookList);

		}

		setNoOfBooksToDispatch(0);
		session.setAttribute("PACK_LIST", packList);
		session.setAttribute("BOOK_LIST", bookList);
		session.setAttribute("BOOK_SERIES_LIST", bookSeriesList);
		session.setAttribute("BOOK_SERIES_ALL", bookSeriesAll);
		return SUCCESS;

	}

	public String dispatchOrder() throws Exception {

		System.out
				.println("-------------------------------------------------dispatchOrder------------------------------------");

		HttpSession session = getRequest().getSession();
		List<InvOrderBean> invOrderList = (List<InvOrderBean>) session
				.getAttribute("INV_ORDER_LIST");
		int totalApproveBooksForOrder = (Integer) session
				.getAttribute("Total_Approve_books");

		System.out.println(invOrderList.size()
				+ "size of invOrderList------------------------------------");
		UserInfoBean userInfoBean = (UserInfoBean) session
				.getAttribute("USER_INFO");
		String boOrgName = userInfoBean.getOrgName();
		int userOrgID = userInfoBean.getUserOrgId();

		int orderId = (Integer) session.getAttribute("ORDER_ID");
		int agentOrgId = (Integer) session.getAttribute("AGENT_ORG_ID");
		String rootPath = (String) session.getAttribute("ROOT_PATH");
		System.out.println("----------------InvOrderList::"
				+ invOrderList.size());
		if (invOrderList != null) {
			// try {
			BOOrderProcessHelper helper = new BOOrderProcessHelper();
			int DCId = helper.assignOrder(invOrderList, totalApproveBooksForOrder,
					orderId, boOrgName, userOrgID, agentOrgId, rootPath,
					 userInfoBean.getUserId());
			session.setAttribute("DEL_CHALLAN_ID", DCId);
			/*
			 * } catch (LMSException le) { return APPLICATION_ERROR; }
			 */
		}

		return SUCCESS;
	}

	public int getBookCountId() {
		return bookCountId;
	}

	public List<BookBean> getBookList() {
		return bookList;
	}

	public String[] getBookNbr() {
		return bookNbr;
	}

	public String[] getBookNbrFrmStr() {
		return bookNbrFrmStr;
	}

	public String getBookNbrFrom() {
		return bookNbrFrom;
	}

	/*
	 * public String addPack() {
	 * 
	 * reInitialize(); System.out.println("add bookkkkkkkkkkkkk"); HttpSession
	 * session = getRequest().getSession(); List<PackBean> packList = (List<PackBean>)
	 * session .getAttribute("PACK_LIST"); List<BookBean> bookList = (List<BookBean>)
	 * session .getAttribute("BOOK_LIST");
	 * 
	 * if (packList != null) {
	 * 
	 * copyPackValues(packList); packList.add(new PackBean());
	 * setPackList(packList); }
	 * 
	 * if (bookList != null) { copyBookValues(bookList); setBookList(bookList); }
	 * 
	 * return SUCCESS; }
	 */

	public Object getBookNbrFromArr() {
		return bookNbrFromArr;
	}

	public String getBookNbrTo() {
		return bookNbrTo;
	}

	public Object getBookNbrToArr() {
		return bookNbrToArr;
	}

	public String[] getBookNbrToStr() {
		return bookNbrToStr;
	}

	public String getBookNbrToVerify() {
		return bookNbrToVerify;
	}

	public double getBookPrice() {
		return bookPrice;
	}

	public List getBookSeriesAll() {
		return bookSeriesAll;
	}

	/*
	 * private void copyPackValues(List<PackBean> packList) { PackBean packBean =
	 * null; System.out.println("---" + getPackNbr()); for (int i = 0; i <
	 * packList.size(); i++) { packBean = packList.get(i);
	 * System.out.println("PackBean::" + packBean);
	 * packBean.setPackNumber(getPackNbr()[i]);
	 * System.out.println("PackBean11::" + packBean); } }
	 */

	public List<BookBean> getBookSeriesList() {
		return bookSeriesList;
	}

	public int getGameId() {
		return gameId;
	}

	public boolean getIsAddBookEnabled() {
		return isAddBookEnabled;
	}

	public boolean getIsInValidEntry() {
		return isInValidEntry;
	}

	public boolean getIsProceedNext() {
		return isProceedNext;
	}

	public int getNoOfBooksToDispatch() {
		return noOfBooksToDispatch;
	}

	public OrderedGameBean getOrderdedGame() {
		return orderdedGame;
	}

	public int getPackCountId() {
		return packCountId;
	}

	public List<PackBean> getPackList() {
		return packList;
	}

	public String[] getPackNbr() {
		return packNbr;
	}

	public String getPackNbrToVerify() {
		return packNbrToVerify;
	}

	public HttpServletRequest getRequest() {
		return request;
	}

	public HttpServletResponse getResponse() {
		return response;
	}

	private void reInitialize() {
		HttpSession session = getRequest().getSession();
		OrderedGameBean bean = (OrderedGameBean) session
				.getAttribute("ORDERED_GAME");
		if (bean != null) {
			setOrderdedGame(bean);
		}

		List<PackBean> packList = (List<PackBean>) session
				.getAttribute("PACK_LIST");
		List<BookBean> bookList = (List<BookBean>) session
				.getAttribute("BOOK_LIST");

		System.out.println("-----booklist---" + bookList);

		if (packList != null) {
			setPackList(packList);
		}
		if (bookList != null) {

			if (bookList.size() == 0) {
				OrderedGameBean orderedGameBean = (OrderedGameBean) session
						.getAttribute("ORDERED_GAME");

				int nbrOfAppBooks = orderedGameBean.getNbrOfBooksApp();

				int nbrOfBooksToAdd = 0;
				if (nbrOfAppBooks > defaultNbrOfBooks) {
					nbrOfBooksToAdd = defaultNbrOfBooks;
					setAddBookEnabled(true);

				} else {
					nbrOfBooksToAdd = nbrOfAppBooks;
				}
				BookBean bookBean = null;
				for (int i = 0; i < nbrOfBooksToAdd; i++) {
					bookBean = new BookBean();
					bookBean.setValid(true);

					bookList.add(bookBean);
				}
			}

			setBookList(bookList);
		}
	}

	private void saveInvData(OrderedGameBean orderedGameBean,
			List<BookBean> bookList, List<PackBean> packList) {
		System.out.println("???????????// saveInvData");
		HttpSession session = getRequest().getSession();
		List<InvOrderBean> invOrderList = (List<InvOrderBean>) session
				.getAttribute("INV_ORDER_LIST");

		InvOrderBean invOrderBean = null;
		if (invOrderList != null) {
			invOrderBean = new InvOrderBean();
			invOrderBean.setOrderedGameBean(orderedGameBean);
			if (bookList != null) {
				invOrderBean.setBookList(bookList);
			}
			if (packList != null) {
				invOrderBean.setPackList(packList);
			}

			orderedGameBean.setReadyForDispatch(true);
			invOrderList.add(invOrderBean);

		}

	}

	public void setAddBookEnabled(boolean isAddBookEnabled) {
		this.isAddBookEnabled = isAddBookEnabled;
	}

	public void setBookCountId(int bookCountId) {
		this.bookCountId = bookCountId;
	}

	public void setBookList(List<BookBean> bookList) {
		this.bookList = bookList;
	}

	public void setBookNbr(String[] bookNbr) {
		this.bookNbr = bookNbr;
	}

	public void setBookNbrFrmStr(String[] bookNbrFrmStr) {
		this.bookNbrFrmStr = bookNbrFrmStr;
	}

	public void setBookNbrFrom(String bookNbrFrom) {
		this.bookNbrFrom = bookNbrFrom;
	}

	public void setBookNbrFromArr(Object bookNbrFromArr) {
		this.bookNbrFromArr = bookNbrFromArr;
	}

	public void setBookNbrTo(String bookNbrTo) {
		this.bookNbrTo = bookNbrTo;
	}

	public void setBookNbrToArr(Object bookNbrToArr) {
		this.bookNbrToArr = bookNbrToArr;
	}

	public void setBookNbrToStr(String[] bookNbrToStr) {
		this.bookNbrToStr = bookNbrToStr;
	}

	public void setBookNbrToVerify(String bookNbrToVerify) {
		this.bookNbrToVerify = bookNbrToVerify;
	}

	public void setBookPrice(double bookPrice) {
		this.bookPrice = bookPrice;
	}

	public void setBookSeriesAll(List bookSeriesAll) {
		this.bookSeriesAll = bookSeriesAll;
	}

	public void setBookSeriesList(List<BookBean> bookSeriesList) {
		this.bookSeriesList = bookSeriesList;
	}

	public void setGameId(int gameId) {
		this.gameId = gameId;
	}

	public void setInValidEntry(boolean isInValidEntry) {
		this.isInValidEntry = isInValidEntry;
	}

	public void setNoOfBooksToDispatch(int noOfBooksToDispatch) {
		this.noOfBooksToDispatch = noOfBooksToDispatch;
	}

	public void setOrderdedGame(OrderedGameBean orderdedGame) {
		this.orderdedGame = orderdedGame;
	}

	public void setPackCountId(int packCountId) {
		this.packCountId = packCountId;
	}

	public void setPackList(List<PackBean> packList) {
		this.packList = packList;
	}

	public void setPackNbr(String[] packNbr) {
		this.packNbr = packNbr;
	}

	public void setPackNbrToVerify(String packNbrToVerify) {
		this.packNbrToVerify = packNbrToVerify;
	}

	public void setProceedNext(boolean isProceedNext) {
		this.isProceedNext = isProceedNext;
	}

	public void setResponse(HttpServletResponse response) {
		this.response = response;
	}

	public void setServletRequest(HttpServletRequest request) {
		this.request = request;

	}

	public void setServletResponse(HttpServletResponse response) {
		this.response = response;

	}

	/**
	 * This method verifies the book entered by the user
	 * 
	 * @param bookList
	 * 
	 * @return String
	 * @throws Exception
	 */
	public void verifyBook(String bookNbr, List<BookBean> bookList,
			Connection connection, int roleId) throws Exception {

		boolean isValid = false;
		int gameId = getGameId();
		boolean isBlank = false;
		boolean isDuplicate = false;

		HttpSession session = getRequest().getSession();
		List<BookBean> bookSeriesList = (List<BookBean>) session
				.getAttribute("BOOK_SERIES_LIST");

		// System.out.println("Bokkkkkkkk To
		// verify::::::::::::::::::::::::::::::::" + bookNbr);

		// try {
		if (bookNbr != null && !bookNbr.trim().equals("")) {

			// add hyphens if necessary

			// System.out.println(":::::::::" + bookNbr.indexOf("-"));

			if (bookNbr.indexOf("-") == -1) {
				OrderedGameBean orderedGameBean = (OrderedGameBean) session
						.getAttribute("ORDERED_GAME");

				bookNbr = bookNbr.substring(0, orderedGameBean
						.getGameNbrDigits())
						+ "-"
						+ bookNbr.substring(orderedGameBean.getGameNbrDigits());

				System.out.println("New book nbr:::" + bookNbr);

			}

			// System.out.println("---------------vvvvvvvvvvvvv----------" +
			// bookNbr);
			BODispatchGameHelper helper = new BODispatchGameHelper();
			isValid = helper.verifyBook(gameId, bookNbr, connection, roleId);
		}

		else {
			// System.out.println("---------------vvvvvvvvvvvvv----------");
			isValid = true;
		}

		// when the user changes the bookNbr to blanks
		/*
		 * if (bookNbr != null && bookNbr.trim().equals("")) {
		 * System.out.println("--For Blank--------"); isBlank = true; BookBean
		 * blankBean = bookList.get(bookCountId - 1);
		 * blankBean.setBookNumber(""); blankBean.setValid(true);
		 * blankBean.setStatus(null); }
		 */

		/*
		 * } catch (LMSException le) { return APPLICATION_ERROR; }
		 */

		if (!isBlank) {

			System.out.println("--If Not Blank--------");
			if (bookList != null) {
				// copyBookValues(bookList);

				// for duplicate check

				int dupCount = 0;

				int bookCountId = getBookCountId();
				System.out.println("BookCountId:::::::::::" + bookCountId);

				for (BookBean bean : bookList) {
					if (bookNbr.equals(bean.getBookNumber())) {
						dupCount++;
						// System.out.println("----in dup
						// counter----"+dupCount);
					}

					if (dupCount > 1) {
						isDuplicate = true;
						bean.setValid(false);
						bean.setStatus("Duplicate Book");
						// System.out.println("----in dup counter-if
						// block---"+dupCount);
						break;
					}
				}

				/*
				 * if (dupCount <= 1) { BookBean dupBean =
				 * bookList.get(bookCountId - 1); dupBean.setValid(true);
				 * dupBean.setStatus(null); }
				 */

				if (!isDuplicate) {
					for (BookBean bean : bookList) {
						if (bookNbr.equals(bean.getBookNumber())) {
							if (!isValid) {
								bean.setValid(false);
								bean.setStatus("Wrong Book Number");
								break;
							} else {
								bean.setValid(true);
								bean.setStatus(null);
								break;
							}
						}

					}

				}

			}
		}

		setBookList(bookList);
		// changeDispatchBooks();
	}

	public String verifyBookSeries() throws LMSException {

		// global connection to be used everywhere
		Connection connection = null;
		 
		connection = DBConnect.getConnection();

		boolean isValid = false;
		boolean isSeriesValid = true;
		int gameId = getGameId();
		HttpSession session = getRequest().getSession();
		int roleId=((UserInfoBean)session.getAttribute("USER_INFO")).getRoleId();
		List<BookBean> bookList = new ArrayList();
		List<BookBean> bookSeriesList = new ArrayList();
		List bookSeriesAll = new ArrayList();
		session.setAttribute("BOOK_SERIES_ALL", bookSeriesAll);
		session.setAttribute("BOOK_LIST", bookList);
		session.setAttribute("BOOK_SERIES_LIST", bookSeriesList);
		StringTokenizer bookNbrFrmTok = new StringTokenizer(
				((String[]) bookNbrFromArr)[0], ",");
		StringTokenizer bookNbrToTok = new StringTokenizer(
				((String[]) bookNbrToArr)[0], ",");
		bookNbrFrmStr = new String[bookNbrFrmTok.countTokens()];
		bookNbrToStr = new String[bookNbrToTok.countTokens()];
		int frmTok = 0;
		while (bookNbrFrmTok.hasMoreTokens()) {
			bookNbrFrmStr[frmTok] = bookNbrFrmTok.nextToken();
			bookNbrToStr[frmTok] = bookNbrToTok.nextToken();
			frmTok = frmTok + 1;
		}
		System.out.println("---Series length" + bookNbrFrmStr);

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
					BookSeriesBean bookSeBean = new BookSeriesBean();
					bookSeBean.setBookNbrFrom(bookNbrFrom);
					bookSeBean.setBookNbrTo(bookNbrTo);
					bookSeBean.setStatus("");
					bookSeBean.setValid(isValid);
					for (int i = 0; i < noOfbooks + 1; i++) {
						String bookNbr = String.valueOf(bookNbrFrmInt);

						if (bookNbr != null && !bookNbr.trim().equals("")) {
							// add hyphens if necessary

							System.out.println(":::::::::"
									+ bookNbr.indexOf("-"));

							if (bookNbr.indexOf("-") == -1) {
								OrderedGameBean orderedGameBean = (OrderedGameBean) session
										.getAttribute("ORDERED_GAME");

								bookNbr = bookNbr.substring(0, orderedGameBean
										.getGameNbrDigits())
										+ "-"
										+ bookNbr.substring(orderedGameBean
												.getGameNbrDigits());

								System.out.println("New book nbr:::" + bookNbr);

							}

							System.out
									.println("---------------vvvvvvvvvvvvv----------"
											+ bookNbr);
							BODispatchGameHelper helper = new BODispatchGameHelper();
							isValid = helper.verifyBook(gameId, bookNbr,
									connection, roleId);

							if (isValid) {
								BookBean bookBean = new BookBean();
								bookBean.setValid(true);
								bookBean.setBookNumber(bookNbr);
								for (BookBean bean : bookSeriesList) {
									if (bookNbr.equals(bean.getBookNumber())) {
										isSeriesValid = false;
										bookSeBean
												.setStatus("Series Contains Tickets of Another Series");
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
						System.out.println("inside for of verifyBookSeries");

						bookNbrFrmInt++;

					}
					if (isSeriesValid) {
						bookSeBean.setValid(true);
						session
								.setAttribute("BOOK_SERIES_LIST",
										bookSeriesList);
					}
					bookSeriesAll.add(bookSeBean);
					session.setAttribute("BOOK_SERIES_ALL", bookSeriesAll);
					// changeDispatchBooks();
					System.out
							.println("@@@@@@@@@@@@@################$$$$$$$$$$$$$%%%%%%%%%%%%%%%%");
				}
			}
		}
		verifyIndividualBooks(bookList, connection, roleId);

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
		setBookList(bookList);
		session.setAttribute("BOOK_LIST", bookList);
		changeDispatchBooks();
		System.out.println("---------Trying to prevent caching------"
				+ bookList);
		HttpServletResponse response = getResponse();
		response.setHeader("Pragma", "no-cache"); // HTTP 1.0
		response.setHeader("Cache-Control", "no-cache"); // HTTP 1.1
		response.setDateHeader("Expires", 0); // prevents caching at the proxy
		// server
		response.setHeader("Cache-Control", "private"); // HTTP 1.1
		response.setHeader("Cache-Control", "no-store"); // HTTP 1.1
		response.setHeader("Cache-Control", "max-stale=0"); // HTTP 1.1

		return SUCCESS;

	}

	/**
	 * This method is used for verifying the book and pack entries and
	 * dispatching the game if entries are correct
	 * 
	 * @return String
	 * @throws Exception
	 */
	public String verifyDispatchEntry() throws Exception {

		boolean isVerified = false;

		System.out.println("In Verify Dispatch--------------------@@@");
		HttpSession session = getRequest().getSession();

		List<PackBean> packList = (List<PackBean>) session
				.getAttribute("PACK_LIST");

		List<BookBean> bookList = (List<BookBean>) session
				.getAttribute("BOOK_LIST");
		List<BookBean> bookSeriesList = (List<BookBean>) session
				.getAttribute("BOOK_SERIES_LIST");

		// List<PackBean> packList = new ArrayList<PackBean>();
		// List<BookBean> bookList = new ArrayList<BookBean>();

		if (packList != null) {
			// copyPackValues(packList);
			setPackList(packList);

		}

		if (bookList != null) {
			// copyBookValues(bookList);
			setBookList(bookList);

		}
		if (bookSeriesList != null) {
			setBookSeriesList(bookSeriesList);
			/*
			 * if(!bookList.containsAll(bookSeriesList)){
			 * bookList.addAll(bookSeriesList); }
			 * setBookSeriesList(bookSeriesList);
			 */
		}

		System.out.println("Nbr of Books to dispatch::" + noOfBooksToDispatch);

		OrderedGameBean orderedGameBean = (OrderedGameBean) session
				.getAttribute("ORDERED_GAME");
		System.out.println("OrderedGameBean::" + orderedGameBean);

		if (orderedGameBean != null) {
			orderedGameBean.setNbrOfBooksToDispatch(getNoOfBooksToDispatch());
		}

		BODispatchGameHelper helper = new BODispatchGameHelper();

		try {
			if (orderedGameBean != null && bookList != null) {

				isVerified = helper.isBookAndPackValid(packList, bookList,
						bookSeriesList);
				System.out.println("First IsVerified::" + isVerified);
				if (isVerified) {
					isVerified = helper.verifyDispatchEntry(orderedGameBean,
							packList, bookList, bookSeriesList);
					System.out.println("2nd IsVerified::" + isVerified);
				}

				System.out.println("IsVerified::" + isVerified);
			}
		} catch (Exception le) {
			le.printStackTrace();
			System.out.println("Helllllllllllo");
		}

		if (isVerified) {
			setProceedNext(true);
			bookList.addAll(bookSeriesList);
			setBookList(bookList);
			saveInvData(orderedGameBean, bookList, packList);
			System.out.println("properly success from verifyDispatchEntry");
			return SUCCESS;

		} else {
			reInitialize();
			checkValidBookInseries(bookSeriesList);
			int validBookCount = helper.recalculateDispatchBooks(packList,
					bookList, bookSeriesList, orderedGameBean
							.getNbrOfBooksPerPack());
			setNoOfBooksToDispatch(validBookCount);

			if (orderedGameBean != null) {
				if (validBookCount < orderedGameBean.getNbrOfBooksApp()) {
					setAddBookEnabled(true);

				}
			}

			setInValidEntry(true);
			return ERROR;
		}

		/*
		 * if(packList != null && packList.size() > 0){ for(int i=0;i<packList.size();i++){
		 * PackBean b = packList.get(i); System.out.println("Packnum:" +
		 * b.getPackNbr()); System.out.println("Packnum Valid:" +
		 * b.getIsValid()); } }
		 * 
		 * if(bookList != null && bookList.size() > 0){ for(int i=0;i<bookList.size();i++){
		 * BookBean b = bookList.get(i); System.out.println("Booknum:" +
		 * b.getBookNbr()); System.out.println("Booknum Valid:" +
		 * b.getIsValid()); } }
		 */

	}

	public void verifyIndividualBooks(List<BookBean> bookList,
			Connection connection, int roleId) throws LMSException {

		copyBookValues(bookList);
		for (BookBean bean : bookList) {
			try {
				verifyBook(bean.getBookNumber(), bookList, connection, roleId);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				throw new LMSException("sqlException", e);
			}
		}
	}

	/**
	 * This method verifies the pack entered by the user
	 * 
	 * @return String
	 * @throws Exception
	 */
	public String verifyPack() throws Exception {

		boolean isValid = false;
		int gameId = getGameId();
		boolean isBlank = false;
		boolean isDuplicate = false;

		HttpSession session = getRequest().getSession();
		List<PackBean> packList = (List<PackBean>) session
				.getAttribute("PACK_LIST");
		String packNbr = getPackNbrToVerify();

		try {
			if (packNbr != null && !packNbr.trim().equals("")) {

				if (packNbr.indexOf("-") == -1) {
					OrderedGameBean orderedGameBean = (OrderedGameBean) session
							.getAttribute("ORDERED_GAME");

					packNbr = packNbr.substring(0, orderedGameBean
							.getGameNbrDigits())
							+ "-"
							+ packNbr.substring(orderedGameBean
									.getGameNbrDigits());

					System.out.println("New pack nbr:::" + packNbr);

				}

				BODispatchGameHelper helper = new BODispatchGameHelper();
				isValid = helper.verifyPack(gameId, packNbr);
			} else {
				isValid = true;
			}

			// when the user changes the packNbr to blanks
			if (packNbr != null && packNbr.trim().equals("")) {
				System.out.println("--For Blank--------");
				isBlank = true;
				PackBean blankBean = packList.get(packCountId - 1);
				blankBean.setPackNumber("");
				blankBean.setValid(true);
				blankBean.setStatus(null);
			}

		} catch (Exception le) {
			le.printStackTrace();
		}

		/*
		 * try { PrintWriter out = getResponse().getWriter(); if (isValid) {
		 * out.write("valid"); } } catch (IOException e) { // TODO
		 * Auto-generated catch block e.printStackTrace(); }
		 */

		if (!isBlank) {

			System.out.println("--If Not Blank--------");
			if (packList != null) {
				copyPackValues(packList);

				// for duplicate check

				int dupCount = 0;

				int packCountId = getPackCountId();
				System.out.println("PackCountId:::::::::::" + packCountId);

				for (PackBean bean : packList) {
					if (packNbr.equals(bean.getPackNumber())) {
						dupCount++;
					}

					if (dupCount > 1) {
						isDuplicate = true;
						PackBean dupBean = packList.get(packCountId - 1);
						dupBean.setValid(false);
						dupBean.setStatus("Duplicate Pack");
						break;
					}
				}

				if (dupCount <= 1) {
					PackBean dupBean = packList.get(packCountId - 1);
					dupBean.setValid(true);
					dupBean.setStatus(null);
				}

				if (!isDuplicate) {
					for (PackBean bean : packList) {
						if (packNbr.equals(bean.getPackNumber())) {
							if (!isValid) {
								bean.setValid(false);
								bean.setStatus("Wrong Pack Number");
								break;
							} else {
								bean.setValid(true);
								bean.setStatus(null);
								break;
							}
						}
					}
				}

			}
		}

		setPackList(packList);
		changeDispatchBooks();

		System.out.println("---------Trying to prevent caching------");
		HttpServletResponse response = getResponse();
		response.setHeader("Pragma", "no-cache"); // HTTP 1.0
		response.setHeader("Cache-Control", "no-cache"); // HTTP 1.1
		response.setDateHeader("Expires", 0); // prevents caching at the proxy
		// server
		response.setHeader("Cache-Control", "private"); // HTTP 1.1
		response.setHeader("Cache-Control", "no-store"); // HTTP 1.1
		response.setHeader("Cache-Control", "max-stale=0"); // HTTP 1.1

		return SUCCESS;
	}

	/*		BO Dispatch Order Starts	*/
	private String gameNumber;
	private String gameName;
	private String challanId;
	private int agentOrgId;
	private String agtOrgName;
	private int orderId;
	private String orderNumber;
	private String orderDate;
	private String end;
	private String searchResultsAvailable;

	public String getGameNumber() {
		return gameNumber;
	}

	public void setGameNumber(String gameNumber) {
		this.gameNumber = gameNumber;
	}

	public String getGameName() {
		return gameName;
	}

	public void setGameName(String gameName) {
		this.gameName = gameName;
	}
	
	public String getChallanId() {
		return challanId;
	}

	public void setChallanId(String challanId) {
		this.challanId = challanId;
	}

	public int getAgentOrgId() {
		return agentOrgId;
	}

	public void setAgentOrgId(int agentOrgId) {
		this.agentOrgId = agentOrgId;
	}

	public String getAgtOrgName() {
		return agtOrgName;
	}

	public void setAgtOrgName(String agtOrgName) {
		this.agtOrgName = agtOrgName;
	}

	public int getOrderId() {
		return orderId;
	}

	public void setOrderId(int orderId) {
		this.orderId = orderId;
	}

	public String getOrderNumber() {
		return orderNumber;
	}

	public void setOrderNumber(String orderNumber) {
		this.orderNumber = orderNumber;
	}

	public String getOrderDate() {
		return orderDate;
	}

	public void setOrderDate(String orderDate) {
		this.orderDate = orderDate;
	}

	public String getEnd() {
		return end;
	}

	public void setEnd(String end) {
		this.end = end;
	}

	public String getSearchResultsAvailable() {
		return searchResultsAvailable;
	}

	public void setSearchResultsAvailable(String searchResultsAvailable) {
		this.searchResultsAvailable = searchResultsAvailable;
	}

	public String dispatchOrderMenu() throws LMSException {
		HttpSession session = getRequest().getSession();
		session.setAttribute("APP_ORDER_LIST1", null);
		session.setAttribute("APP_ORDER_LIST", null);
		session.setAttribute("SearchResultsAvailable", null);
		session.setAttribute("CREDIT_LIMIT", null);
		session.setAttribute("CREDIT_AMT", null);

		return SUCCESS;
	}

	public String dispatchOrderSearch() throws LMSException {
		HttpSession session = getRequest().getSession();
		session.setAttribute("APP_ORDER_LIST1", null);
		session.setAttribute("APP_ORDER_LIST", null);
		session.setAttribute("SearchResultsAvailable", null);
		session.setAttribute("Total_Approve_books", null);
		session.setAttribute("CREDIT_LIMIT", null);
		session.setAttribute("CREDIT_AMT", null);
		session.setAttribute("REMAINING_AVAILABLE_CREDIT_AMT", null);

		Map<String, String> searchMap = new HashMap<String, String>();
		searchMap.put(GameContants.GAME_NAME, gameName);
		searchMap.put(GameContants.GAME_NBR, gameNumber);
		searchMap.put(TableConstants.ORG_NAME, agtOrgName);
		searchMap.put(GameContants.ORDER_ID, orderNumber);

		BODispatchGameHelper helper = new BODispatchGameHelper();
		List<OrderBean> searchResults = helper.dispatchOrderSearch(gameName, gameNumber, agtOrgName, orderNumber,challanId,((UserInfoBean) session.getAttribute("USER_INFO")).getRoleId());

		if (searchResults != null && searchResults.size() > 0) {
			session.setAttribute("APP_ORDER_LIST1", searchResults);
			session.setAttribute("startValueOrderSearch", new Integer(0));
			session.setAttribute("SearchResultsAvailable", "Yes");
		} else {
			session.setAttribute("SearchResultsAvailable", "No");
		}
		searchAjax();

		return SUCCESS;
	}

	public String searchAjax() {
		int startValue = 0;
		int endValue = 0;
		HttpSession session = getRequest().getSession();
		List ajaxList = (List) session.getAttribute("APP_ORDER_LIST1");
		List ajaxSearchList = new ArrayList();
		if (ajaxList != null) {
			if (getEnd() != null)
				end = getEnd();
			else
				end = "first";

			startValue = (Integer) session.getAttribute("startValueOrderSearch");
			if (end.equals("first")) {
				startValue = 0;
				endValue = startValue + 5;
				if (endValue > ajaxList.size()) {
					endValue = ajaxList.size();
				}
			} else if (end.equals("Previous")) {
				startValue = startValue - 5;
				if (startValue < 5) {
					startValue = 0;
				}
				endValue = startValue + 5;
				if (endValue > ajaxList.size()) {
					endValue = ajaxList.size();
				}
			} else if (end.equals("Next")) {
				startValue = startValue + 5;
				endValue = startValue + 5;
				if (endValue > ajaxList.size()) {
					endValue = ajaxList.size();
				}
				if (startValue > ajaxList.size()) {
					startValue = ajaxList.size() - ajaxList.size() % 5;
				}
			} else if (end.equals("last")) {
				endValue = ajaxList.size();
				startValue = endValue - endValue % 5;
			}
			if (startValue == endValue) {
				startValue = endValue - 5;
			}
			for (int i = startValue; i < endValue; i++) {
				ajaxSearchList.add(ajaxList.get(i));
			}
			session.setAttribute("APP_ORDER_LIST", ajaxSearchList);
			session.setAttribute("startValueOrderSearch", startValue);
			setSearchResultsAvailable("Yes");
		}

		return SUCCESS;
	}

	public String dispatchOrderDetails() throws LMSException {
		HttpSession session = getRequest().getSession();

		OrgAddressBean addrBean = new BOOrderProcessHelper().fetchAddress(agentOrgId);
		if (addrBean != null)
			session.setAttribute("ORG_ADDR", addrBean);

		DispatchOrderResponse dispatchOrderResponse = new BODispatchGameHelper().getBookListFromOrderId(orderId);
		session.setAttribute("ORDER_ID", getOrderId());
		session.setAttribute("ORDER_DATE", getOrderDate());
		session.setAttribute("AGT_ORG_NAME", getAgtOrgName());
		session.setAttribute("AGENT_ORG_ID", agentOrgId);
		session.setAttribute("CHALLAN_ID", getChallanId());
		session.setAttribute("BOOK_LIST", dispatchOrderResponse);

		return SUCCESS;
	}

	@SuppressWarnings("unchecked")
	public String dispatchOrderSave() throws Exception {
		HttpSession session = getRequest().getSession();
		UserInfoBean userInfoBean = (UserInfoBean) session.getAttribute("USER_INFO");
		int orderId = (Integer) session.getAttribute("ORDER_ID");
		Map<String, Map<String, List<String>>> gameMap = ((DispatchOrderResponse) session.getAttribute("BOOK_LIST")).getOrderData();
		new BODispatchGameHelper().dispatchOrder(orderId, userInfoBean.getUserOrgId(), userInfoBean.getUserType(),userInfoBean.getUserId(), gameMap);

		return SUCCESS;
	}

	/*private static String roundTo2DecimalPlaces(double value) {
		DecimalFormat df = new DecimalFormat("0.000");
		String doublevalue = df.format(value);
		return doublevalue;
	}*/
	/*		BO Dispatch Order End		*/
}