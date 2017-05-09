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

package com.skilrock.lms.coreEngine.scratchService.inventoryMgmt.common;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.skilrock.lms.beans.BookBean;
import com.skilrock.lms.beans.DispatchOrderResponse;
import com.skilrock.lms.beans.OrderBean;
import com.skilrock.lms.beans.OrderedGameBean;
import com.skilrock.lms.beans.PackBean;
import com.skilrock.lms.common.db.DBConnect;
import com.skilrock.lms.common.db.QueryManager;
import com.skilrock.lms.common.db.TableConstants;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.web.drawGames.common.Util;

/**
 * This is a helper class providing methods for handling the order dispatch at
 * Agent end
 * 
 * @author Skilrock Technologies
 * 
 */
public class AgentDispatchGameHelper {
	private static final Logger logger = LoggerFactory.getLogger(AgentDispatchGameHelper.class);
	private boolean checkDuplicatePackBook(int gameId, List<PackBean> packList,
			List<BookBean> bookList, int agtOrgId, Connection connection)
			throws LMSException {
		boolean isValid = true;
		String packs = null;
		if (packList != null) {
			packs = getPacks(packList);
			if (packs != null) {
				isValid = verifyPackBook(gameId, packs, bookList, connection);

			}
		}

		System.out.println("---Duplicate Pack Book::" + isValid);
		return isValid;
	}

	private String getBooks(List<BookBean> bookList) {
		BookBean bookBean = null;
		StringBuffer books = new StringBuffer();

		if (bookList != null) {

			// int size = bookList.size();
			for (int i = 0; i < bookList.size(); i++) {
				// System.out.println("Loop Itr:" + i);
				bookBean = bookList.get(i);
				String bookNbr = bookBean.getBookNumber();
				// System.out.println("BookNbr:" + bookNbr);
				if (bookNbr == null || bookNbr != null
						&& bookNbr.trim().equals("")) {
					bookList.remove(i);
					i = -1;
				}
			}

			int size = bookList.size();
			boolean isValidBookPresent = false;
			for (int i = 0; i < size; i++) {
				// System.out.println("Loop Itr:" + i);
				bookBean = bookList.get(i);
				String bookNbr = bookBean.getBookNumber();
				// System.out.println("BookNbr:" + bookNbr);
				if (bookNbr != null && !bookNbr.trim().equals("")) {
					isValidBookPresent = true;
					books.append("'");
					books.append(bookNbr);
					books.append("'");
					books.append(",");
				}

			}

			if (isValidBookPresent) {

				int length = books.length();
				books.deleteCharAt(length - 1);
				return books.toString();
			}

		}

		return null;
	}

	private String getPacks(List<PackBean> packList) {
		PackBean packBean = null;
		StringBuffer packs = new StringBuffer();

		if (packList != null) {

			// int size = packList.size();
			for (int i = 0; i < packList.size(); i++) {
				// System.out.println("Loop Itr:" + i);
				packBean = packList.get(i);
				String packNbr = packBean.getPackNumber();
				// System.out.println("PackNbr:" + packNbr);
				if (packNbr == null || packNbr != null
						&& packNbr.trim().equals("")) {
					packList.remove(i);
					i = -1;
				}
			}

			// prepare a comma-separated pack list

			int size = packList.size();
			boolean isValidPackPresent = false;
			for (int i = 0; i < size; i++) {
				// System.out.println("Loop Itr:" + i);
				packBean = packList.get(i);
				String packNbr = packBean.getPackNumber();
				// System.out.println("PackNbr:" + packNbr);
				if (packNbr != null && !packNbr.trim().equals("")) {
					isValidPackPresent = true;
					packs.append("'");
					packs.append(packNbr);
					packs.append("'");
					packs.append(",");
				}

			}

			if (isValidPackPresent) {

				int length = packs.length();
				packs.deleteCharAt(length - 1);
				return packs.toString();
			}

		}

		return null;
	}

	/**
	 * This method verifies the book and pack entries
	 * 
	 * @param packList
	 * @param bookList
	 * @return boolean
	 */
	public boolean isBookAndPackValid(List<PackBean> packList,
			List<BookBean> bookList, List<BookBean> bookSeriesList) {

		if (bookList != null) {
			System.out.println("Size::" + bookList.size());
			for (BookBean bean : bookList) {
				System.out.println("::" + bean.getIsValid());
				if (!bean.getIsValid()) {
					return false;
				}
			}
		}

		if (packList != null) {
			System.out.println("Size::" + packList.size());
			for (PackBean bean : packList) {
				System.out.println("::" + bean.getIsValid());
				if (!bean.getIsValid()) {
					return false;
				}
			}
		}

		if (bookSeriesList != null) {
			System.out.println("Size::" + bookSeriesList.size());
			for (BookBean bean : bookSeriesList) {
				System.out.println("::" + bean.getIsValid());
				if (!bean.getIsValid()) {
					return false;
				}
			}
		}

		return true;
	}

	/**
	 * This method is used for calculating the dispatch books
	 * 
	 * @param packList
	 * @param bookList
	 * @param nbrOfBooksPerPack
	 * @return int
	 */
	public int recalculateDispatchBooks(List<PackBean> packList,
			List<BookBean> bookList, List<BookBean> bookSeriesList,
			int nbrOfBooksPerPack) {

		int validBookCount = 0;

		// for books

		String bookNbr = null;
		String packNbr = null;

		if (bookList != null) {
			for (BookBean bean : bookList) {
				bookNbr = bean.getBookNumber();

				if (bookNbr != null && !bookNbr.trim().equals("")) {

					if (bean.getIsValid()) {
						validBookCount++;
					}
				}

			}
		}

		if (packList != null) {
			for (PackBean bean : packList) {
				packNbr = bean.getPackNumber();

				if (packNbr != null && !packNbr.trim().equals("")) {

					if (bean.getIsValid()) {
						validBookCount += nbrOfBooksPerPack;
					}
				}

			}
		}

		if (bookSeriesList != null) {
			for (BookBean bean : bookSeriesList) {
				bookNbr = bean.getBookNumber();

				if (bookNbr != null && !bookNbr.trim().equals("")) {

					if (bean.getIsValid()) {
						validBookCount++;
					}
				}

			}
		}

		return validBookCount;
	}

	private void removeBlanksFromPackList(List<PackBean> packList) {

		PackBean bean = null;
		if (packList != null) {
			for (int i = 0; i < packList.size(); i++) {
				bean = packList.get(i);

				String packNbr = bean.getPackNumber();
				if (packNbr == null || packNbr != null
						&& packNbr.trim().equals("")) {
					packList.remove(i);
					i = -1;
				}
			}
		}

	}

	/**
	 * This method verifies if the passed book is a valid book
	 * 
	 * @param gameId
	 * @param agtOrgId
	 * @param bookToVerify
	 * @return boolean
	 * @throws LMSException
	 */
	public boolean verifyBook(int gameId, int agtOrgId, String bookToVerify,
			Connection conn, int gameNbr) throws LMSException {

		// System.out.println("Agent Org Id::" + agtOrgId);
		boolean isValid = false;
		boolean ownercheck = false;
		boolean pwtCheck = false;
		boolean pwtCheckTemp = false;
		Connection connection = conn;
		try {

			PreparedStatement pstmt = null;
			ResultSet resultSet = null;
			String query = null;

			/*
			 *   connection =
			 * dbConnect.getConnection();
			 */

			query = QueryManager.getST1AgentVerifyQuery()
					+ "  and (book_status='INACTIVE')";
			pstmt = connection.prepareStatement(query);

			pstmt.setInt(1, gameId);
			pstmt.setString(2, bookToVerify);

			resultSet = pstmt.executeQuery();

			String currOwner = null;
			int currOwnerId = -1;
			while (resultSet.next()) {
				currOwner = resultSet.getString(TableConstants.SGIS_CURR_OWNER);
				currOwnerId = resultSet
						.getInt(TableConstants.SGIS_CURR_OWNER_ID);
				if (currOwner != null && currOwner.equals("AGENT")
						&& currOwnerId == agtOrgId) {
					ownercheck = true;
				}
			}

			// ckeck for ticket of this book in main ticket table
			pwtCheck = verifyBookValidityWithPWT(gameId, bookToVerify,
					connection, gameNbr);
			pwtCheckTemp = verifyBookValidityWithPWTTempTable(gameId,
					bookToVerify, connection);
			if (ownercheck == true && pwtCheck == true && pwtCheckTemp == true) {
				isValid = true;
			}

		} catch (SQLException e) {

			e.printStackTrace();
			throw new LMSException(e);
		}
		/*
		 * finally{ try{ if(connection!=null) connection.close();
		 * }catch(SQLException se){ se.printStackTrace(); } }
		 */

		return isValid;
	}

	private boolean verifyBooks(int gameId, String books,
			List<BookBean> bookList, int agtOrgId, Connection connection)
			throws LMSException {

		// Connection connection = null;
		Statement statement = null;
		ResultSet resultSet = null;
		boolean isBookValid = true;
		try {

			//  
			// connection = DBConnect.getConnection();
			statement = connection.createStatement();

			String query = QueryManager.getST1AgentBookInvVerifyQuery();

			query = query + gameId + " and book_nbr in ( " + books + " )";

			resultSet = statement.executeQuery(query);

			String currOwner = null;
			String bookNbr = null;
			BookBean bean = null;
			int currOwnerId = -1;
			while (resultSet.next()) {
				currOwner = resultSet.getString(TableConstants.SGIS_CURR_OWNER);
				currOwnerId = resultSet
						.getInt(TableConstants.SGIS_CURR_OWNER_ID);
				bookNbr = resultSet.getString(TableConstants.SGIS_BOOK_NBR);

				for (int i = 0; i < bookList.size(); i++) {
					bean = bookList.get(i);
					if (bookNbr.equals(bean.getBookNumber())) {

						if (currOwner != null && currOwner.equals("AGENT")
								&& currOwnerId == agtOrgId) {
							bean.setValid(true);
							bean.setStatus(null);
						} else {
							isBookValid = false;
							bean.setValid(false);
							bean.setStatus("Wrong Book Number");
						}
					}
					// isBookValid = isBookValid && bean.getIsValid();
				}

			}

			for (int i = 0; i < bookList.size(); i++) {
				bean = bookList.get(i);
				isBookValid = isBookValid && bean.getIsValid();
			}

			if (resultSet != null && !resultSet.previous()) {

				isBookValid = false;
			}

		} catch (SQLException e) {

			e.printStackTrace();
			throw new LMSException(e);
		} finally {

			try {

				if (statement != null) {
					statement.close();
				}
				/*
				 * if (connection != null) { connection.close(); }
				 */
			} catch (SQLException se) {
				se.printStackTrace();
			}
		}

		System.out.println("isBookValid::" + isBookValid);
		return isBookValid;
	}

	// Check Book Verification For PWT Tickets in main ticket table.
	public boolean verifyBookValidityWithPWT(int game_id, String bknbr,
			Connection conn, int gameNbr) {

		System.out
				.println("Enter In To verify book with PWT ^^^^^^^^^^^^^^^^^^^^^^^");

		boolean bookPwtFlag = true;
		//  
		// Connection conn=null;
		// conn= DBConnect.getConnection();
		String query6 = QueryManager.getST4BookOfTicketsOfPwt();
		PreparedStatement stmt6;
		try {
			stmt6 = conn.prepareStatement(query6);
			stmt6.setInt(1, gameNbr);
			stmt6.setInt(2, game_id);
			stmt6.setString(3, bknbr);
			ResultSet rs = stmt6.executeQuery();
			while (rs.next()) {
				bookPwtFlag = false;
			}

			if (bookPwtFlag == false) {
				System.out.println("bookPwtFlag is not valid: " + bookPwtFlag);
			} else {
				System.out.println("bookPwtFlag is valid: " + bookPwtFlag);
			}

			System.out
					.println("^^^^^^^^^^^^^^^^^^^^^^^^book verify for PWT is complete");
			return bookPwtFlag;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}/*
			 * finally{ try{ if(conn!=null) conn.close(); }catch(SQLException
			 * se){ se.printStackTrace(); } }
			 */
		return bookPwtFlag;
	}

	// Check Book Verification For PWT Tickets in temp ticket table.
	public boolean verifyBookValidityWithPWTTempTable(int game_id,
			String bknbr, Connection conn) {

		System.out
				.println("Enter In To verify book with PWT ^^^^^^^^^^^^^^^^^^^^^^^");

		boolean bookPwtFlag = true;
		//  
		// Connection conn=null;
		// conn= DBConnect.getConnection();
		String query6 = QueryManager.getST4BookOfTicketsOfPwtTmpTable();
		PreparedStatement stmt6;
		try {
			stmt6 = conn.prepareStatement(query6);
			stmt6.setInt(1, game_id);
			stmt6.setString(2, bknbr);
			ResultSet rs = stmt6.executeQuery();
			while (rs.next()) {
				bookPwtFlag = false;
			}

			if (bookPwtFlag == false) {
				System.out.println("bookPwtFlag is not valid: " + bookPwtFlag);
			} else {
				System.out.println("bookPwtFlag is valid: " + bookPwtFlag);
			}

			System.out
					.println("^^^^^^^^^^^^^^^^^^^^^^^^book verify for PWT in temporary ticket table  is complete");
			return bookPwtFlag;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}/*
			 * finally{ try{ if(conn!=null) conn.close(); }catch(SQLException
			 * se){ se.printStackTrace(); } }
			 */
		return bookPwtFlag;
	}

	/**
	 * This method verifies the book and pack entries entered by the user
	 * 
	 * @param orderedGameBean
	 * @param packList
	 * @param bookList
	 * @param agtOrgId
	 * @return
	 * @throws LMSException
	 */
	public boolean verifyDispatchEntry(OrderedGameBean orderedGameBean,
			List<PackBean> packList, List<BookBean> bookList, int agtOrgId,
			List<BookBean> bookSeriesList) throws LMSException {

		int gameId = orderedGameBean.getGameId();
		String books;
		boolean isPackValid = true;
		boolean isBookValid = true;

		// global connection to use for all verification

		Connection connection = null;
		 
		connection = DBConnect.getConnection();
		// verify entered packs
		if (packList != null) {
			removeBlanksFromPackList(packList);
			isPackValid = verifyPacks(gameId, packList, agtOrgId, connection);
		}

		// verify entered books
		if (bookList != null) {
			books = getBooks(bookList);
			System.out.println("Books::" + books);

			if (books != null) {
				isBookValid = verifyBooks(gameId, books, bookList, agtOrgId,
						connection);
			}
		}

		// check for duplicate books
		boolean isDuplicateBook = checkDuplicatePackBook(gameId, packList,
				bookList, agtOrgId, connection);
		boolean isDuplicateBookSeries = checkDuplicatePackBook(gameId,
				packList, bookSeriesList, agtOrgId, connection);
		System.out.println("nh^^^^^^^^^^^^^^^^");
		if (connection != null) {
			try {
				connection.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return isPackValid && isBookValid && isDuplicateBook;
	}

	/**
	 * This method verifies if the passed pack is a valid pack
	 * 
	 * @param gameId
	 * @param agtOrgId
	 * @param packToVerify
	 * @return boolean
	 * @throws LMSException
	 */
	public boolean verifyPack(int gameId, int agtOrgId, String packToVerify,
			int gameNbr) throws LMSException {

		boolean isValid = false;
		boolean ownercheck = false;
		boolean pwtCheck = false;
		boolean pwtCheckTemp = false;
		Connection connection = null;
		try {

			PreparedStatement pstmt = null;
			ResultSet resultSet = null;
			 
			connection = DBConnect.getConnection();

			// query = QueryManager.getST1AgentPackInvVerifyQuery()+" and
			// book_status!='MISSING'";
			// pstmt = connection.prepareStatement(query);

			String sqlQuery = "select current_owner,current_owner_id, nbr_of_books_per_pack "
					+ "from st_se_game_inv_status aa,  st_se_game_master bb where "
					+ " aa.game_id = bb.game_id and aa.game_id = ? and "
					+ " aa.pack_nbr = ? and (book_status!='MISSING' and book_status!='CLAIMED')";
			pstmt = connection.prepareStatement(sqlQuery);

			pstmt.setInt(1, gameId);
			pstmt.setString(2, packToVerify);
			System.out.println("Arun === changed query " + pstmt);
			resultSet = pstmt.executeQuery();

			String currOwner = null;
			int currOwnerId = -1;
			int noOfBooksPerPack = -1, count = 0;
			while (resultSet.next()) {
				noOfBooksPerPack = resultSet.getInt("nbr_of_books_per_pack");
				currOwner = resultSet.getString(TableConstants.SGIS_CURR_OWNER);
				currOwnerId = resultSet
						.getInt(TableConstants.SGIS_CURR_OWNER_ID);

				if (currOwner != null && currOwner.equals("AGENT")
						&& currOwnerId == agtOrgId) {
					ownercheck = true;
					count += 1;
				} else {
					ownercheck = false;
					break;
				}
			}
			pwtCheck = verifyPackValidityWithPWT(gameId, packToVerify, gameNbr);
			pwtCheckTemp = verifyPackValidityWithPWTTempTable(gameId,
					packToVerify);

			if (ownercheck == true && pwtCheck == true && pwtCheckTemp == true) {
				isValid = true;
			}
			// check for no of valid books is equal to noOfBooksPerPack
			if (isValid && noOfBooksPerPack != count) {
				isValid = false;
			}

		} catch (SQLException e) {

			e.printStackTrace();
			throw new LMSException(e);
		} finally {
			try {
				if (connection != null) {
					connection.close();
				}
			} catch (SQLException se) {
				se.printStackTrace();
				throw new LMSException(se);
			}
		}

		return isValid;
	}

	private boolean verifyPackBook(int gameId, String packs,
			List<BookBean> bookList, Connection connection) throws LMSException {

		// Connection connection = null;
		Statement statement = null;
		ResultSet resultSet = null;
		boolean isBookValid = true;
		try {

			//  
			// connection = DBConnect.getConnection();
			statement = connection.createStatement();

			String query = QueryManager.getST1AgentBookInvVerifyQuery();

			query = query + gameId + " and pack_nbr in ( " + packs + " )";

			resultSet = statement.executeQuery(query);

			String bookNbr = null;
			BookBean bean = null;
			while (resultSet.next()) {

				bookNbr = resultSet.getString(TableConstants.SGIS_BOOK_NBR);

				System.out.println("Agent BookList size " + bookList.size());
				for (int i = 0; i < bookList.size(); i++) {
					bean = bookList.get(i);
					if (bookNbr.equals(bean.getBookNumber())) {

						isBookValid = false;
						bean.setValid(false);
						bean
								.setStatus("Duplicate Book.This book is already part of the pack chosen above");

					} /*
						 * else { bean.setValid(true); bean.setStatus(null); }
						 */

				}

				// System.out.println("Iter" + i + " " + isBookValid);
			}

			for (int i = 0; i < bookList.size(); i++) {
				bean = bookList.get(i);
				isBookValid = isBookValid && bean.getIsValid();
			}

			if (resultSet != null && !resultSet.previous()) {

				System.out.println("--making false---");
				isBookValid = false;
			}

		} catch (SQLException e) {

			e.printStackTrace();
			throw new LMSException(e);

		} finally {

			try {

				if (statement != null) {
					statement.close();
				}
				/*
				 * if (connection != null) { connection.close(); }
				 */
			} catch (SQLException se) {
				se.printStackTrace();
			}
		}

		System.out.println("Agent isBookValid::" + isBookValid);
		return isBookValid;

	}

	private boolean verifyPacks(int gameId, List<PackBean> packList,
			int agtOrgId, Connection connection) throws LMSException {

		boolean isPackValid = true;
		if (packList != null && packList.size() > 0) {

			PackBean bean = null;

			// Connection connection = null;
			PreparedStatement pstmt = null;
			ResultSet resultSet = null;

			try {

				//  
				// connection = DBConnect.getConnection();

				// String query = QueryManager.getST1AgentPackInvVerifyQuery()+"
				// and book_status!='MISSING'";;
				// pstmt = connection.prepareStatement(query);
				String sqlQuery = "select current_owner,current_owner_id, nbr_of_books_per_pack "
						+ "from st_se_game_inv_status aa,  st_se_game_master bb where "
						+ " aa.game_id = bb.game_id and aa.game_id = ? and "
						+ " aa.pack_nbr = ? and (book_status!='MISSING' and book_status!='CLAIMED')";
				pstmt = connection.prepareStatement(sqlQuery);

				for (int i = 0; i < packList.size(); i++) {
					bean = packList.get(i);
					pstmt.setInt(1, gameId);
					pstmt.setString(2, bean.getPackNumber());

					resultSet = null;
					resultSet = pstmt.executeQuery();

					System.out.println("Rs::----------------------------"
							+ resultSet);
					if (resultSet == null) {
						isPackValid = false;
						System.out.println("ResultSet::"
								+ resultSet.getFetchSize());
					}

					String currOwner = null;
					int currOwnerId = -1;
					int noOfBooksPerPack = -1, count = 0;
					while (resultSet.next()) {
						currOwner = resultSet
								.getString(TableConstants.SGIS_CURR_OWNER);
						currOwnerId = resultSet
								.getInt(TableConstants.SGIS_CURR_OWNER_ID);
						noOfBooksPerPack = resultSet
								.getInt("nbr_of_books_per_pack");

						System.out.println("PackNbr::" + bean.getPackNumber());
						System.out.println("CurrOwner::" + currOwner);
						if (currOwner != null && currOwner.equals("AGENT")
								&& currOwnerId == agtOrgId) {
							bean.setValid(true);
							count += 1;

						} else {
							isPackValid = false;
							bean.setValid(false);
							bean.setStatus("Wrong Pack Number");
							break;
						}
					}
					// check for no of valid books is equal to noOfBooksPerPack
					if (bean.getIsValid() && noOfBooksPerPack != count) {
						bean.setValid(false);
					}
					if (resultSet != null && !resultSet.previous()) {

						isPackValid = false;
					}

				}
			} catch (SQLException e) {
				e.printStackTrace();
				throw new LMSException(e);
			} finally {

				try {

					if (pstmt != null) {
						pstmt.close();
					}
					/*
					 * if (connection != null) { connection.close(); }
					 */
				} catch (SQLException se) {
					se.printStackTrace();
				}
			}

		}
		System.out.println("isPackValid::" + isPackValid);
		return isPackValid;
	}

	// Check Pack Verification For PWT tickets.
	public boolean verifyPackValidityWithPWT(int game_id, String pknbr,
			int gameNbr) throws LMSException {

		System.out
				.println("Enter In To verify pack with PWT +++++++++++++++++++++++++++++");

		boolean packPwtFlag = true;
		 
		Connection conn = null;
		conn = DBConnect.getConnection();
		String query6 = QueryManager
				.getST4PWTDetailsForBookNbrUsingGamePackNbr();
		PreparedStatement stmt6;
		try {
			stmt6 = conn.prepareStatement(query6);
			stmt6.setInt(1, gameNbr);
			stmt6.setInt(2, game_id);
			stmt6.setInt(3, game_id);
			stmt6.setString(4, pknbr);
			ResultSet rs = stmt6.executeQuery();
			while (rs.next()) {
				packPwtFlag = false;
			}
			if (packPwtFlag == false) {
				System.out.println("packPwtFlag is not valid: " + packPwtFlag);
			} else {
				System.out.println("packPwtFlag is valid: " + packPwtFlag);
			}

			System.out
					.println("+++++++++++++++++++++ verify pack with PWT is complete");
			return packPwtFlag;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException se) {
				se.printStackTrace();
				throw new LMSException(se);
			}
		}

		return packPwtFlag;
	}

	// Check Pack Verification For PWT tickets in temporary ticket table.
	public boolean verifyPackValidityWithPWTTempTable(int game_id, String pknbr) {

		System.out
				.println("Enter In To verify pack with PWT +++++++++++++++++++++++++++++");

		boolean packPwtFlag = true;
		 
		Connection conn = null;
		conn = DBConnect.getConnection();
		String query6 = QueryManager
				.getST4PWTDetailsForBookNbrUsingGamePackNbrTempTable();
		PreparedStatement stmt6;
		try {
			stmt6 = conn.prepareStatement(query6);
			stmt6.setInt(1, game_id);
			stmt6.setInt(2, game_id);
			stmt6.setString(3, pknbr);
			ResultSet rs = stmt6.executeQuery();
			while (rs.next()) {
				packPwtFlag = false;
			}
			if (packPwtFlag == false) {
				System.out.println("packPwtFlag is not valid: " + packPwtFlag);
			} else {
				System.out.println("packPwtFlag is valid: " + packPwtFlag);
			}

			System.out
					.println("+++++++++++++++++++++ verify pack with PWT is complete in temp ticket table ");
			return packPwtFlag;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {

			try {
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return packPwtFlag;
	}

	public List<OrderBean> dispatchOrderSearch(String gameName, String gameNumber, String agtOrgName, String orderNumber, int orgId,String challanId) throws LMSException {
		Connection connection = null;
		Statement statement = null;
		ResultSet resultSet = null;
		List<OrderBean> searchResults = new ArrayList<OrderBean>();
		OrderBean orderBean = null;
		try {
			StringBuilder queryBuilder = new StringBuilder();
			queryBuilder.append("SELECT sao.order_id, sao.retailer_org_id, som.name, sao.order_date, sao.order_status,generated_id FROM st_se_agent_order sao, st_se_agent_order_invoices saoi,st_se_agent_ordered_games sog, st_lms_agent_receipts lac,st_lms_organization_master som, st_se_game_master gm WHERE sao.retailer_org_id=som.organization_id AND sao.order_status IN('ASSIGNED','SEMI_ASSIGNED') AND sog.game_id=gm.game_id AND sao.order_id=sog.order_id and saoi.order_id=sao.order_id and lac.receipt_id=saoi.dc_id and sao.agent_org_id = "+ orgId + " ");
			if(gameName!=null && gameName.length()>0)
				queryBuilder.append(" AND game_name LIKE '%").append(gameName).append("%'");
			if(gameNumber!=null && gameNumber.length()>0)
				queryBuilder.append(" AND game_nbr LIKE '%").append(gameNumber).append("%'");
			if(agtOrgName!=null && agtOrgName.length()>0)
				queryBuilder.append(" AND name LIKE '%").append(agtOrgName).append("%'");
			if(orderNumber!=null && orderNumber.length()>0)
				queryBuilder.append(" AND sao.order_id LIKE '%").append(orderNumber).append("%'");
			if(challanId!=null && challanId.length()>0)
				queryBuilder.append(" AND generated_id LIKE '%").append(challanId).append("%'");
			queryBuilder.append(" GROUP BY order_id;");

			connection = DBConnect.getConnection();
			statement = connection.createStatement();
			logger.info("dispatchOrderSearch - "+queryBuilder.toString());
			resultSet = statement.executeQuery(queryBuilder.toString());
			while (resultSet.next()) {
				orderBean = new OrderBean();
				orderBean.setOrderId(resultSet.getInt("order_id"));
				orderBean.setAgentOrgId(resultSet.getInt("retailer_org_id"));
				orderBean.setAgentOrgName(resultSet.getString("name"));
				orderBean.setOrderDate(resultSet.getDate("order_date"));
				orderBean.setChallanId(resultSet.getString("generated_id"));
				if (resultSet.getString("order_status").equalsIgnoreCase("APPROVED"))
					orderBean.setOrderStatus("Approved");
				if (resultSet.getString("order_status").equalsIgnoreCase("PROCESSED"))
					orderBean.setOrderStatus("Processed");
				if (resultSet.getString("order_status").equalsIgnoreCase("SEMI_PROCESSED"))
					orderBean.setOrderStatus("Semi Processed");
				if (resultSet.getString("order_status").equalsIgnoreCase("ASSIGNED"))
					orderBean.setOrderStatus("Assigned");
				if (resultSet.getString("order_status").equalsIgnoreCase("SEMI_ASSIGNED"))
					orderBean.setOrderStatus("Semi Assigned");

				searchResults.add(orderBean);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new LMSException(e);
		} finally {
			DBConnect.closeCon(connection);
			DBConnect.closeStmt(statement);
			DBConnect.closeRs(resultSet);
		}

		return searchResults;
	}

	public DispatchOrderResponse getBookListFromOrderId(int orderId) {
		Connection connection = null;
		Statement statement = null;
		ResultSet resultSet = null;
		DispatchOrderResponse dispatchOrderResponse = null ;
		Map<String, Map<String, List<String>>> gameMap = new HashMap<String, Map<String,List<String>>>();
		Map<String, List<String>> packMap = null;
		List<String> bookList = null;
		try { 
			connection = DBConnect.getConnection();
			statement = connection.createStatement();
			String query = "SELECT gm.game_id, game_name, pack_nbr, book_nbr, gid.book_status FROM st_se_game_inv_detail gid INNER JOIN st_se_agent_order_invoices aoi ON gid.order_invoice_dc_id=aoi.order_invoice_dc_id INNER JOIN st_se_game_master gm ON gid.game_id=gm.game_id WHERE aoi.order_id="+orderId+";";
			System.out.println("getBookListFromOrderId - "+query);
			resultSet = statement.executeQuery(query);
			while (resultSet.next()) {
				if(!"ASSIGNED".equalsIgnoreCase(resultSet.getString("book_status"))){
					return new DispatchOrderResponse(500);
				}
				if(!gameMap.containsKey(resultSet.getString("game_name")))
					gameMap.put(resultSet.getString("game_name"), new HashMap<String, List<String>>());
				packMap = gameMap.get(resultSet.getString("game_name"));

				if(!packMap.containsKey(resultSet.getString("pack_nbr")))
					packMap.put(resultSet.getString("pack_nbr"), new ArrayList<String>());
				bookList = packMap.get(resultSet.getString("pack_nbr"));

				bookList.add(resultSet.getString("book_nbr"));
				dispatchOrderResponse = new DispatchOrderResponse(200, gameMap);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBConnect.closeCon(connection);
			DBConnect.closeStmt(statement);
			DBConnect.closeRs(resultSet);
		}

		return dispatchOrderResponse;
	}

	public void dispatchOrder(int orderId, int userOrgId, String userType, Map<String, Map<String, List<String>>> gameMap) {
		Connection connection = null;
		Statement stmt = null;
		try {
			connection = DBConnect.getConnection();
			connection.setAutoCommit(false);

			stmt = connection.createStatement();
			stmt.executeUpdate("UPDATE st_se_agent_order SET order_status=(IF(order_status='SEMI_ASSIGNED','SEMI_PROCESSED','PROCESSED')) WHERE order_id="+orderId+";");
			stmt.executeUpdate("UPDATE st_se_agent_order_invoices SET order_status=(IF(order_status='SEMI_ASSIGNED','SEMI_PROCESSED','PROCESSED')) WHERE order_id="+orderId+";");
			for(Map.Entry<String, Map<String, List<String>>> gameEntry : gameMap.entrySet())
				for(Map.Entry<String, List<String>> packEntry : gameEntry.getValue().entrySet())
					for(String book : packEntry.getValue()) {
						stmt.executeUpdate("UPDATE st_se_game_inv_status SET book_status='IN_TRANSIT' WHERE book_nbr='"+book+"';");
						stmt.executeUpdate("insert into st_se_game_inv_detail(game_id, pack_nbr, book_nbr, current_owner, current_owner_id, transaction_date, transaction_at, changed_by_user_id, book_status, warehouse_id, agent_invoice_id, ret_invoice_id) select game_id, pack_nbr, book_nbr, current_owner, current_owner_id, '"+Util.getCurrentTimeString()+"' transaction_date, '"+userType+"' transaction_at, "+userOrgId+" changed_by_user_id, 'IN_TRANSIT' book_status, warehouse_id, agent_invoice_id, ret_invoice_id from st_se_game_inv_status where  book_nbr='"+book+"'");
					}
			connection.commit();
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			DBConnect.closeCon(connection);
			DBConnect.closeStmt(stmt);
		}
	}
}