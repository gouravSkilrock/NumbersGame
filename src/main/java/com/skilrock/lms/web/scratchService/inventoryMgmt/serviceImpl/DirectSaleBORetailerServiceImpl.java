package com.skilrock.lms.web.scratchService.inventoryMgmt.serviceImpl;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.skilrock.lms.beans.BookBean;
import com.skilrock.lms.beans.BookSeriesBean;
import com.skilrock.lms.beans.UserInfoBean;
import com.skilrock.lms.common.db.DBConnect;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.common.utility.CommonMethods;
import com.skilrock.lms.coreEngine.scratchService.inventoryMgmt.common.DirectSaleAgentHelper;
import com.skilrock.lms.coreEngine.scratchService.inventoryMgmt.common.DirectSaleBOHelper;

public class DirectSaleBORetailerServiceImpl {

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
								+ ":"
								+ bookTOArr[start].replaceAll("-", ""));
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

	public void verifyBookSeries(List<String> bkSerList,
			List<String> frontBookList, int gameId, String gameName,
			int gameNbr, int gameNbrDigits, int bkDigits, int boOrgId,
			Map<String, List> verifiedGameMap) throws LMSException {

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
						.equals(bookNbrTo.substring(0, bookNbrTo.length()
								- bkDigits))) {
					int bookNbrFrmInt = Integer.parseInt(bookNbrFrom.substring(
							bookNbrFrom.length() - bkDigits,
							bookNbrFrom.length()));
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
							DirectSaleBOHelper helper = new DirectSaleBOHelper();
							isValid = helper.verifyBook(boOrgId, bookNbr,
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
				gameNbr, gameNbrDigits, boOrgId, connection);

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
			int gameNbrDigits, int boOrgId, Connection connection)
			throws LMSException {

		bookList = copyBookValues(bookList, frontBookList, gameNbrDigits);
		for (BookBean bean : bookList) {
			try {
				System.out.println(bean.getBookNumber() + "***"
						+ bean.getStatus());

				if (bean.getStatus() == null) {
					bookList = verifyBook(bean.getBookNumber(), bookList,
							gameId, gameNbr, boOrgId, connection);
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				throw new LMSException("sqlException", e);
			}
		}
		return bookList;
	}

	public List<BookBean> verifyBook(String bookNbr, List<BookBean> bookList,
			int gameId, int gameNbr, int boOrgId, Connection connection)
			throws Exception {
		boolean isValid = false;
		DirectSaleBOHelper helper = new DirectSaleBOHelper();

		// System.out.println(isValid+"*********"+bookNbr);
		if (bookList != null) {
			isValid = helper.verifyBook(boOrgId, bookNbr, gameId, gameNbr,
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

	public String startTransaction(Map<Integer, List<String>> gameBookMap,
			int agtId, int agtOrgId, int agtOrgName, String rootPath,
			String boOrgName, String newBookStatusAgt, int retOrgId,
			String newBookStatusRet) {
		Connection con = null;
		DirectSaleBOHelper directSalehelper = new DirectSaleBOHelper();
		DirectSaleAgentHelper directSaleAgentHelper = new DirectSaleAgentHelper();
		String agtSaleReturnValue = null;
		String retSaleReturnValue = null;
		UserInfoBean userBean = null;
		try {
			if (gameBookMap.size() > 0) {
				con = DBConnect.getConnection();
				con.setAutoCommit(false);

				agtSaleReturnValue = directSalehelper.dispatchOrderDirectSale(
						gameBookMap, agtId, agtOrgId, agtOrgName, rootPath,
						boOrgName, newBookStatusAgt, con);

				userBean = CommonMethods.fetchUserData(agtOrgName);
				retSaleReturnValue = directSaleAgentHelper
						.dispatchOrderDirectSale(gameBookMap,
								userBean.getUserId(), agtOrgName, retOrgId,
								rootPath, userBean.getOrgName(),
								newBookStatusRet, con);
				con.commit();

				// invoiceId = Integer
				// .parseInt(agtSaleReturnValue.split("Nxt")[1]);
				// if (invoiceId > -1) {
				// GraphReportHelper graphReportHelper = new
				// GraphReportHelper();
				// graphReportHelper.createTextReportBO(invoiceId,
				// userBean.getOrgName(), agtOrgName, rootPath);
				// }
				//
				// invoiceId = Integer
				// .parseInt(retSaleReturnValue.split("Nxt")[1]);
				//
				// if (invoiceId > -1) {
				// GraphReportHelper graphReportHelper = new
				// GraphReportHelper();
				// graphReportHelper.createTextReportAgent(invoiceId,
				// rootPath, userBean.getUserId(),
				// userBean.getOrgName());
				// }
			} else
				throw new LMSException();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBConnect.closeCon(con);
		}
		return agtSaleReturnValue + "#$" + retSaleReturnValue;
	}
}
