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

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
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
import com.skilrock.lms.beans.BookBean;
import com.skilrock.lms.beans.GameBean;
import com.skilrock.lms.beans.PackBean;
import com.skilrock.lms.beans.UserInfoBean;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.coreEngine.scratchService.inventoryMgmt.common.SalesReturnAgentHelper;
import com.skilrock.lms.coreEngine.scratchService.inventoryMgmt.common.SalesReturnHelper;

/**
 * This class provides methods for verify packs,books and to get valid packs and
 * books
 * 
 * @author Skilrock Technologies
 * 
 */
public class SalesReturnAgentAction extends ActionSupport implements
		ServletRequestAware, ServletResponseAware {

	/**
	 * 
	 */
	static Log logger = LogFactory.getLog(SalesReturnAgentAction.class);
	private static final long serialVersionUID = 1L;
	private List<BookBean> bookList;
	private String[] bookNumber;
	List<Integer> bookRetAgtList = new ArrayList<Integer>();
	private String[] bookSeriesFrom;
	private String[] bookSeriesTo;
	private String flag;
	private String game_Name;
	private String gameName;
	Map m = new LinkedHashMap();
	private String organization_Name;
	private List<PackBean> packList;
	private List<String> packNum;
	private String[] packNumber;
	List<Integer> packRetAgtList = new ArrayList<Integer>();
	private String[] packs;
	private HttpServletRequest request;
	private HttpServletResponse response;
	private String SaleReturn;
	private List<BookBean> savedBookBeanList;
	private List<PackBean> savedPackBeanList;

	private String Type = "";

	private List<BookBean> verifiedBookBeanList;

	private List<PackBean> verifiedPackBeanList;
	private int orgId;
	/**
	 * This method is used to get the retailers organizations name to display on
	 * sale return page
	 * 
	 * @return String
	 */
	public String displaySalesReturnEntryPage() {
		//HttpSession session = getRequest().getSession();
		setPackList(new ArrayList<PackBean>());
		setBookList(new ArrayList<BookBean>());
		/*logger.info("session " + session);
		SalesReturnAgentHelper salesReturnHelper = new SalesReturnAgentHelper();
		List<OrgInfoBean> organizationBeanList = salesReturnHelper
				.getOrganizations(session);*/
		//logger.info("Organization's List is: " + organizationBeanList);
		//session.setAttribute("ORGANIZATION_BEAN_LIST", organizationBeanList);
		/*
		 * session.setAttribute("ACTIVE_GAME_LIST",activeGameList);
		 * session.setAttribute("PACK_LIST",packBeanList);
		 * session.setAttribute("BOOK_LIST",bookBeanList);
		 */
		return SUCCESS;
	}

	public List<BookBean> getBookList() {
		return bookList;
	}

	public String[] getBookNumber() {
		return bookNumber;
	}

	public String[] getBookSeriesFrom() {
		return bookSeriesFrom;
	}

	public String[] getBookSeriesTo() {
		return bookSeriesTo;
	}

	public String getFlag() {
		return flag;
	}

	public String getGame_Name() {
		return game_Name;
	}

	public String getGameName() {
		return gameName;
	}

	public Map getM() {
		return m;
	}

	public String getOrganization_Name() {
		return organization_Name;
	}

	/*
	 * new actions by vineet
	 */

	public List<PackBean> getPackList() {
		return packList;
	}

	public List<String> getPackNum() {
		return packNum;
	}

	public String[] getPackNumber() {
		return packNumber;
	}

	public String[] getPacks() {
		return packs;
	}

	public HttpServletRequest getRequest() {
		return request;
	}

	public HttpServletResponse getResponse() {
		return response;
	}

	public String getSaleReturn() {
		return SaleReturn;
	}

	public List<BookBean> getSavedBookBeanList() {
		return savedBookBeanList;
	}

	public List<PackBean> getSavedPackBeanList() {
		return savedPackBeanList;
	}

	public String getType() {
		return Type;
	}

	public List<BookBean> getVerifiedBookBeanList() {
		return verifiedBookBeanList;
	}

	public List<PackBean> getVerifiedPackBeanList() {
		return verifiedPackBeanList;
	}

	public String salesReturnAjax() {
		PrintWriter out;
		SalesReturnHelper helper = new SalesReturnHelper();
		try {
			out = getResponse().getWriter();

			String orgName = getType();
			logger.info("" + orgName);
			ArrayList gameList =null;
			String html = "";
			if (orgId>0) {

				 gameList = helper.getGameList(orgId);
			}else{
				html = "<select class=\"option\" name=\"gameName\" id=\"gameName\"  onchange=\"_saleRetAgt('im_common_saleReturn_fetchPacknBookList.action?gameName='+(this.value).split(\'-\')[1]+'&agentOrgName=')\"><option class=\"option\" value=\"-1\">--Please Select--</option>";
				html += "</select>";
				response.setContentType("text/html");
				out.print(html);
				return null;
			}

			// session.setAttribute("GAME_LIST",characters);
			// And yes, I know creating HTML in an Action is generally very bad
			// form,
			// but I wanted to keep this exampel simple.

			
			html = "<select class=\"option\" name=\"gameName\" id=\"gameName\"  onchange=\"_saleRetAgt('im_common_saleReturn_fetchPacknBookList.action?gameName='+(this.value).split(\'-\')[1]+'&agentOrgName=')\"><option class=\"option\" value=\"-1\">--Please Select--</option>";

			GameBean bean = null;
			for (Iterator it = gameList.iterator(); it.hasNext();) {
				bean = (GameBean) it.next();
				html += "<option class=\"option\" value=\""
						+ (Integer) bean.getGameNbr() + "-"
						+ bean.getGameName() + "\">"
						+ (Integer) bean.getGameNbr() + "-"
						+ bean.getGameName() + "</option>";
			}
			html += "</select>";
			response.setContentType("text/html");
			out.print(html);
			return null;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * This method is used to accept valid packs and books
	 * 
	 * @return String
	 * @throws Exception
	 */

	public String saveSalesReturnData() throws Exception {
		logger.info("enter in to save book and pack function#############################");

		ServletContext sc = ServletActionContext.getServletContext();
		HttpSession session = getRequest().getSession();
		SalesReturnAgentHelper helper = new SalesReturnAgentHelper();
		int receipt_id = 0;

		game_Name = (String) session.getAttribute("GAME_NAME");
		organization_Name = (String) session.getAttribute("ORG_NAME");
		orgId = ((Integer) session.getAttribute("ORG_CODE")).intValue();

		logger.info("game_Name  " + game_Name);
		logger.info("organization_Name  " + organization_Name);

		//int org_id = helper.getOrganizationIdFromDataBase(organization_Name);
		int game_id = helper.getGameIdFromDataBase(game_Name.split("-")[1]);
		String newBookStatus = "INACTIVE";
		logger.info("Game Id is :" + game_id);
		logger.info("org Id is :" + orgId);

		List<PackBean> packList = (List<PackBean>) session
				.getAttribute("VERIFIED_PACK_LIST");
		List<BookBean> bookList = (List<BookBean>) session
				.getAttribute("VERIFIED_BOOK_LIST");

		logger.info("packList  " + packList);
		logger.info("bookList  " + bookList);

		setSavedPackBeanList(helper.selectValidPacks(packList));
		setSavedBookBeanList(helper.selectValidBooks(bookList));

		session.setAttribute("VALID_PACK_LIST", getSavedPackBeanList());
		session.setAttribute("VALID_BOOK_LIST", getSavedBookBeanList());

		logger.info("getSavedPackBeanList()  " + getSavedPackBeanList());
		logger.info("getSavedBookBeanList()  " + getSavedBookBeanList());

		String rootPath = (String) session.getAttribute("ROOT_PATH");
		UserInfoBean userInfoBean = (UserInfoBean) session
				.getAttribute("USER_INFO");
		String loggedInUserOrgName = userInfoBean.getOrgName();
		if ("BO-AGENT".equals(sc.getAttribute("BOOK_ACTIVATION_AT"))) {
			newBookStatus = "ACTIVE";
		}
		receipt_id = helper.doTransaction(game_id, orgId, getSavedPackBeanList(),getSavedBookBeanList(), session, rootPath, newBookStatus);
		boolean isSet = helper.showCreditNote(receipt_id);
		session.setAttribute("showCreditNote", isSet);
		logger.info("book and pack save function is complete");
		return SUCCESS;
	}

	public void setBookList(List<BookBean> bookList) {
		this.bookList = bookList;
	}

	public void setBookNumber(String[] bookNumber) {
		this.bookNumber = bookNumber;
	}

	public void setBookSeriesFrom(String[] bookSeriesFrom) {
		this.bookSeriesFrom = bookSeriesFrom;
	}

	public void setBookSeriesTo(String[] bookSeriesTo) {
		this.bookSeriesTo = bookSeriesTo;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}

	public void setGame_Name(String game_Name) {
		this.game_Name = game_Name;
	}

	public void setGameName(String gameName) {
		this.gameName = gameName;
	}

	public void setM(Map m) {
		this.m = m;
	}

	public void setOrganization_Name(String organization_Name) {
		this.organization_Name = organization_Name;
	}

	public void setPackList(List<PackBean> packList) {
		this.packList = packList;
	}

	public void setPackNum(List<String> packNum) {
		this.packNum = packNum;
	}

	public void setPackNumber(String[] packNumber) {
		this.packNumber = packNumber;
	}

	public void setPacks(String[] packs) {
		this.packs = packs;
	}

	public void setRequest(HttpServletRequest request) {
		this.request = request;
	}

	public void setSaleReturn(String saleReturn) {
		SaleReturn = saleReturn;
	}

	public void setSavedBookBeanList(List<BookBean> savedBookBeanList) {
		this.savedBookBeanList = savedBookBeanList;
	}

	public void setSavedPackBeanList(List<PackBean> savedPackBeanList) {
		this.savedPackBeanList = savedPackBeanList;
	}

	public void setServletRequest(HttpServletRequest request) {
		this.request = request;
	}

	public void setServletResponse(HttpServletResponse response) {
		this.response = response;

	}

	public void setType(String Type) {
		this.Type = Type;
	}

	public void setVerifiedBookBeanList(List<BookBean> verifiedBookBeanList) {
		this.verifiedBookBeanList = verifiedBookBeanList;
	}

	public void setVerifiedPackBeanList(List<PackBean> verifiedPackBeanList) {
		this.verifiedPackBeanList = verifiedPackBeanList;
	}

	/**
	 * This method is used to verify packs and books for sale return by retailer
	 * 
	 * @return String
	 */

	public String verifyPacksAndBooks() throws LMSException {
		logger.info("enter in to verify book and pack function");
		HttpSession session = getRequest().getSession();
		SalesReturnAgentHelper helper = new SalesReturnAgentHelper();

		// check if retailer online
		String isRetOnline = (String) ServletActionContext.getServletContext()
				.getAttribute("RET_ONLINE");
		logger.info("----------retailer online  " + isRetOnline);

		logger.info("game_Name  " + gameName);
		logger.info("organization_Name  " + organization_Name);

		//int org_id = helper.getOrganizationIdFromDataBase(organization_Name);
		int game_id = helper.getGameIdFromDataBase(gameName.split("-")[1]);
		int game_nbr = Integer.parseInt(gameName.split("-")[0]);

		logger.info("Game Id is :" + game_id);
		logger.info("org Id is :" + orgId);

		List<PackBean> packList = new ArrayList<PackBean>();
		List<BookBean> bookList = new ArrayList<BookBean>();
		PackBean packBean = null;
		BookBean bookBean = null;

		if (getPackNumber() != null) {
			int packSize = getPackNumber().length;
			for (int i = 0; i < packSize; i++) {
				String pn = packNumber[i];
				logger.info("packnumber is " + pn);
				if (pn != "" && pn != null && !pn.equals("Please Select")) {

					packBean = new PackBean();
					packBean.setPackNumber(pn);
					packList.add(packBean);
				}
			}
		}
		if (getBookNumber() != null) {
			int bookSize = getBookNumber().length;
			for (int i = 0; i < bookSize; i++) {
				String bn = bookNumber[i];
				logger.info("booknumber is " + bn);
				if (bn != "" && bn != null && !bn.equals("Please Select")) {
					bookBean = new BookBean();
					bookBean.setBookNumber(bn);
					bookList.add(bookBean);
				}
			}
		}

		if (getBookSeriesFrom() != null) {
			for (int i = 0; i < bookSeriesFrom.length; i++) {
				if (!bookSeriesFrom[i].equals("")
						&& !bookSeriesTo[i].equals("")
						&& bookSeriesFrom[i] != null && bookSeriesTo[i] != null) {
					int bookFrom = Integer.parseInt(bookSeriesFrom[i]
							.split("-")[1]);
					int bookTo = Integer
							.parseInt(bookSeriesTo[i].split("-")[1]);
					int lenOfDigit = bookSeriesTo[i].split("-")[1].length();
					String gameNum = bookSeriesFrom[i].split("-")[0];
					for (int j = bookFrom; j < bookTo + 1; j++) {

						String lastDigit = "" + j;
						logger.info(lenOfDigit + "--lENGTH OF DIGIT --"
								+ lastDigit.length());
						while (lastDigit.length() < lenOfDigit) {
							lastDigit = "0" + lastDigit;
						}
						logger.info("booknumber is " + gameNum + "-"
								+ lastDigit);
						bookBean = new BookBean();
						bookBean.setBookNumber(gameNum + "-" + lastDigit);
						bookList.add(bookBean);
					}
				}

			}
		}

		session.setAttribute("GAME_NAME", gameName);
		session.setAttribute("ORG_NAME", organization_Name);
		session.setAttribute("ORG_CODE", Integer.valueOf(orgId));

		session.setAttribute("PACK_LIST", packList);
		session.setAttribute("BOOK_LIST", bookList);

		session.setAttribute("PACK_FLAG", null);
		// session.setAttribute("BOOK_FLAG",null);
		logger.info("packList  " + packList);
		logger.info("bookList  " + bookList);

		setVerifiedPackBeanList(helper.doVerifiedPacks(packList, game_id,
				orgId, isRetOnline, game_nbr));
		setVerifiedBookBeanList(helper.doVerifiedBooks(bookList, game_id,
				orgId, isRetOnline, game_nbr));
		String packFlag = helper.getPackFlag();
		// String bookFlag=helper.getPackFlag();
		session.setAttribute("VERIFIED_PACK_LIST", getVerifiedPackBeanList());
		session.setAttribute("VERIFIED_BOOK_LIST", getVerifiedBookBeanList());

		logger.info("getVerifiedPackBeanList()  "
				+ getVerifiedPackBeanList());
		logger.info("getVerifiedBookBeanList()  "
				+ getVerifiedBookBeanList());

		setSaleReturn(packFlag);

		session.setAttribute("SALE_RET_FLAG", packFlag);
		// session.setAttribute("PACK_FLAG",packFlag);
		// session.setAttribute("BOOK_FLAG",bookFlag);
		logger.info("Verify book and Pack Is complete  " + packFlag);
		return SUCCESS;
	}

	public int getOrgId() {
		return orgId;
	}

	public void setOrgId(int orgId) {
		this.orgId = orgId;
	}

}
