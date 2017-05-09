package com.skilrock.lms.web.scratchService.inventoryMgmt.common;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.skilrock.lms.beans.BookPackBean;

public class GenerateTicketsNumber {
	static Log logger = LogFactory.getLog(GenerateTicketsNumber.class);

	private static final int PACK_BOOK_DIGITS = 6;

	/**
	 * For count the Digits of number.
	 * 
	 * @param int
	 *            k: that is the number.
	 * @return total digits, int.
	 */

	public static int findDigit(int k) {
		int count = 0;
		while (k != 0) {
			k = k / 10;
			count++;
		}
		return count;
	}

	/**
	 * For Find the Min values in both.
	 * 
	 * @param x,
	 *            int.
	 * @param y,
	 *            int.
	 * @return min value, int.
	 */

	public static int findMin(int x, int y) {
		int min = 0;
		if (x < y) {
			min = x;
		} else if (y < x) {
			min = y;
		} else {
			min = y;
		}
		return min;
	}

	public static int getPACK_BOOK_DIGITS() {
		return PACK_BOOK_DIGITS;
	}

	private List<String> bookNbrList;
	private List<String> finalBookNbrList;
	private int finalPackDigit, finalBookDigit, packNbrFrom, packNbrTo;
	private int game_Nbr;

	private List<BookPackBean> gameInvList;

	private List<String> packNbrList;

	private int totalPack, totalBook;

	public GenerateTicketsNumber(int totalPack, int game_nbr,
			int finalPackDigit, int finalBookDigit, int packNbrFrom,
			int packNbrTo) {
		System.out
				.println("int totalPack, int game_nbr, int finalPackDigit, int finalBookDigit, int packNbrFrom, int packNbrTo   "
						+ totalPack
						+ game_nbr
						+ finalPackDigit
						+ finalBookDigit + packNbrFrom + packNbrTo);
		this.totalPack = totalPack;
		this.game_Nbr = game_nbr;
		this.finalBookDigit = finalBookDigit;
		this.finalPackDigit = finalPackDigit;
		this.packNbrFrom = packNbrFrom;
		this.packNbrTo = packNbrTo;
		this.setPackNumberList();
	}

	/**
	 * Constructor for set the total books and total packs values. And setting
	 * the values of final digits lenght for book and pack digits. And
	 * generating the inventry of game.
	 * 
	 * @param totalPack
	 * @param totalBook
	 * @param game_nbr
	 */
	public GenerateTicketsNumber(int totalPack, int totalBook, int game_nbr,
			int finalPackDigit, int finalBookDigit, int packNbrFrom,
			int packNbrTo) {
		System.out
				.println("int totalPack, int totalBook, int game_nbr, int finalPackDigit, int finalBookDigit, int packNbrFrom, int packNbrTo   "
						+ totalPack
						+ totalBook
						+ game_nbr
						+ finalPackDigit
						+ finalBookDigit + packNbrFrom + packNbrTo);
		this.totalPack = totalPack;
		this.totalBook = totalBook;
		this.game_Nbr = game_nbr;
		this.finalBookDigit = finalBookDigit;
		this.finalPackDigit = finalPackDigit;
		this.packNbrFrom = packNbrFrom;
		this.packNbrTo = packNbrTo;
		// setDigitsCount(totalPack, totalBook);
		generateGameInventory();
	}

	/**
	 * For Generat a List of BOOKPACKBEAN. Setting the pack number and book
	 * number list. For append the pack number and book number to generate final
	 * book number.
	 * 
	 * @return list of final book numbers.
	 */

	public void generateGameInventory() {

		setPackNumberList();
		setBookNumberList();

		List<String> finalBookNumberList = new ArrayList<String>();
		List<BookPackBean> gameInvList = new ArrayList<BookPackBean>();
		Iterator<String> bookIterator = null;
		Iterator<String> packIterator = getPackNbrList().iterator();
		String packNumber, bookNumber;
		BookPackBean bean = null;
		System.out.println("final Book nbr list is");
		while (packIterator.hasNext()) {
			packNumber = packIterator.next();
			bookIterator = getBookNbrList().iterator();
			while (bookIterator.hasNext()) {
				bookNumber = packNumber + bookIterator.next();
				bean = new BookPackBean();
				bean.setGame_nbr(this.getGame_Nbr());
				bean.setPack_nbr(packNumber);
				bean.setBook_nbr(bookNumber);
				gameInvList.add(bean);
				// System.out.println(bookNumber);
				finalBookNumberList.add(bookNumber);
			}
		}
		this.setFinalBookNbrList(finalBookNumberList);
		this.setGameInvList(gameInvList);
	}

	public List<String> getBookNbrList() {
		return bookNbrList;
	}

	public int getFinalBookDigit() {
		return finalBookDigit;
	}

	public List<String> getFinalBookNbrList() {
		return finalBookNbrList;
	}

	public int getFinalPackDigit() {
		return finalPackDigit;
	}

	public int getGame_Nbr() {
		return game_Nbr;
	}

	public List<BookPackBean> getGameInvList() {
		return gameInvList;
	}

	public int getPackNbrFrom() {
		return packNbrFrom;
	}

	public List<String> getPackNbrList() {
		return packNbrList;
	}

	public int getPackNbrTo() {
		return packNbrTo;
	}

	public int getTotalBook() {
		return totalBook;
	}

	public int getTotalPack() {
		return totalPack;
	}

	/**
	 * For get the zero string of specified length
	 * 
	 * @param length
	 *            of zero string, int.
	 * @return zero string like: "000"
	 */
	public String getZeroString(int length) {
		StringBuffer buffer = new StringBuffer();
		for (int i = 1; i <= length; i++) {
			buffer = buffer.append(0);
		}
		return buffer.toString();
	}

	public void setBookNbrList(List<String> bookNbrList) {
		this.bookNbrList = bookNbrList;
	}

	/**
	 * For Generate book number List.
	 * 
	 * @param totalBook
	 * @param finalBookDigit
	 * @return list of book numbers
	 */

	public void setBookNumberList() {
		List<String> bookNumberList = new ArrayList<String>();
		int bdig;
		int zeroStringLength;
		String bookNbr = null;
		for (int i = 1; i <= getTotalBook(); i++) {
			bookNbr = "";
			bdig = findDigit(i);
			zeroStringLength = getFinalBookDigit() - bdig;
			if (zeroStringLength != 0) {
				bookNbr = bookNbr + getZeroString(zeroStringLength);
			}
			bookNbr = bookNbr + i;
			// System.out.println("Book nbr is: "+bookNbr);
			bookNumberList.add(bookNbr);
		}
		this.setBookNbrList(bookNumberList);
	}

	/**
	 * For to Set The Book And Pack digits
	 * 
	 * @param totalPack
	 * @param totalBook
	 */

	public void setDigitsCount(int totalPack, int totalBook) {
		int pDigits, bDigits;
		int minDigitValue, sumOfDigits;

		pDigits = findDigit(totalPack);
		bDigits = findDigit(totalBook);
		System.out.println("pd= " + pDigits + "  bd= " + bDigits);
		sumOfDigits = pDigits + bDigits;
		System.out.println("sumOfDigits= " + sumOfDigits);
		if (sumOfDigits >= PACK_BOOK_DIGITS) {
			System.out.println("Comes in first case");
			this.finalPackDigit = pDigits;
			this.finalBookDigit = bDigits;
		} else {
			System.out.println("Comes in second case");
			while (sumOfDigits != PACK_BOOK_DIGITS) {
				minDigitValue = findMin(pDigits, bDigits);
				if (minDigitValue == pDigits) {
					pDigits = pDigits + 1;
				} else {
					bDigits = bDigits + 1;
				}
				sumOfDigits = pDigits + bDigits;
			}
			this.finalPackDigit = pDigits;
			this.finalBookDigit = bDigits;
			System.out.println("final bdigits: " + this.finalBookDigit
					+ "  fonal pdigits: " + this.finalPackDigit);

		}
	}

	public void setFinalBookDigit(int finalBookDigit) {
		this.finalBookDigit = finalBookDigit;
	}

	public void setFinalBookNbrList(List<String> finalBookNbrList) {
		this.finalBookNbrList = finalBookNbrList;
	}

	public void setFinalPackDigit(int finalPackDigit) {
		this.finalPackDigit = finalPackDigit;
	}

	public void setGame_Nbr(int game_Nbr) {
		this.game_Nbr = game_Nbr;
	}

	public void setGameInvList(List<BookPackBean> gameInvList) {
		this.gameInvList = gameInvList;
	}

	public void setPackNbrFrom(int packNbrFrom) {
		this.packNbrFrom = packNbrFrom;
	}

	public void setPackNbrList(List<String> packNbrList) {
		this.packNbrList = packNbrList;
	}

	public void setPackNbrTo(int packNbrTo) {
		this.packNbrTo = packNbrTo;
	}

	/**
	 * Fro Generate Pack Number List
	 * 
	 * @param totalPack
	 * @param finalPackDigit
	 * @return packNumberList
	 */

	public void setPackNumberList() {
		List<String> packNumberList = new ArrayList<String>();
		int pdig;
		int zeroStringLength;
		String packNbr = null;
		for (int i = getPackNbrFrom(); i <= getPackNbrTo(); i++) {
			packNbr = getGame_Nbr() + "-";
			pdig = findDigit(i);
			zeroStringLength = getFinalPackDigit() - pdig;
			if (zeroStringLength != 0) {
				packNbr = packNbr + getZeroString(zeroStringLength);
			}
			packNbr = packNbr + i;
			System.out.println("pack no is : " + packNbr);
			packNumberList.add(packNbr);
		}
		this.setPackNbrList(packNumberList);
	}

	public void setTotalBook(int totalBook) {
		this.totalBook = totalBook;
	}

	public void setTotalPack(int totalPack) {
		this.totalPack = totalPack;
	}

	/**
	 * For Insert into the st_se_game_inv_details table. heer we are using null
	 * for Back Office id(current owner id).
	 * 
	 * @param game_id.
	 * @param transaction_id.
	 */
	public void updateGameInvDetailTable(int game_id, int transaction_id,
			Connection connection, int userOrgId, int userId, int warehouseId) {
		PreparedStatement pstmt = null;
		String query = "insert into st_se_game_inv_detail (transaction_id, game_id, pack_nbr, book_nbr, current_owner,current_owner_id, transaction_date, changed_by_user_id, warehouse_id, book_status) values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
		try {
			pstmt = connection.prepareStatement(query);
			Iterator<BookPackBean> iterator = getGameInvList().iterator();
			BookPackBean bean = null;
			int totalRowsUpdate = 0;

			while (iterator.hasNext()) {
				bean = iterator.next();
				pstmt.setInt(1, transaction_id);
				pstmt.setInt(2, game_id);
				pstmt.setString(3, bean.getPack_nbr());
				pstmt.setString(4, bean.getBook_nbr());
				pstmt.setString(5, "BO");
				pstmt.setInt(6, userOrgId);
				pstmt.setTimestamp(7, new java.sql.Timestamp(
						new java.util.Date().getTime()));
				pstmt.setInt(8, userId);
				pstmt.setInt(9, warehouseId);
				pstmt.setString(10, "INACTIVE");
				int rowsUpdate = pstmt.executeUpdate();
				totalRowsUpdate = rowsUpdate + totalRowsUpdate;
			}
			System.out
					.println("total rows inserted into st_se_game_inv_detail : "
							+ totalRowsUpdate);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (pstmt != null) {
					pstmt.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * For Insert into the st_se_game_inv_status table. heer we are using null
	 * for Back Office id(current owner id).
	 * 
	 * @param game_id
	 */
	public void updateGameInvStatusTable(int game_id, Connection connection,
			int userOrgId, int warehouseId) {
		PreparedStatement pstmt = null;
		// updated by arun to set default status of books as 'INACTIVE'
		// String query = "insert into st_se_game_inv_status (game_id, pack_nbr,
		// book_nbr, current_owner,current_owner_id) values(?,?,?,?,?)";
		String query = "insert into st_se_game_inv_status (game_id, pack_nbr, book_nbr, current_owner,current_owner_id, book_status, warehouse_id) values(?, ?, ?, ?, ?, ?, ?)";
		try {
			pstmt = connection.prepareStatement(query);
			Iterator<BookPackBean> iterator = getGameInvList().iterator();
			int totalRowsUpdate = 0;
			BookPackBean bean = null;
			while (iterator.hasNext()) {
				bean = iterator.next();
				pstmt.setInt(1, game_id);
				pstmt.setString(2, bean.getPack_nbr());
				pstmt.setString(3, bean.getBook_nbr());
				pstmt.setString(4, "BO");
				pstmt.setInt(5, userOrgId);
				pstmt.setString(6, "INACTIVE"); // added by arun
				pstmt.setInt(7, warehouseId);
				int rows = pstmt.executeUpdate();
				totalRowsUpdate = totalRowsUpdate + rows;
			}
			System.out
					.println("total rows inserted into st_se_game_inv_status : "
							+ totalRowsUpdate);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (pstmt != null) {
					pstmt.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
}
