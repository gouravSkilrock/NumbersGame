package com.skilrock.lms.coreEngine.scratchService.inventoryMgmt.common;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import com.skilrock.lms.beans.BookSeriesBean;
import com.skilrock.lms.beans.GameTicketFormatBean;
import com.skilrock.lms.common.db.DBConnect;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.common.utility.MD5Encoder;

public class UpdateBookNVirnStatusHelper {

	// fetch the game details for PWT on Retailer End
	public String fetchPwtGameDetails() throws LMSException {
		Connection connection = DBConnect.getConnection();
		try {

			StringBuilder nbrFormat = new StringBuilder();
			Statement stmt1 = connection.createStatement();
			String fetchGameDetailsQuery = "select res.game_name,res.game_nbr,res.game_id, sgtnf.pack_nbr_digits,"
					+ " sgtnf.book_nbr_digits,sgtnf.ticket_nbr_digits,sgtnf.game_nbr_digits,sgtnf.game_virn_digits"
					+ " from st_se_game_ticket_nbr_format sgtnf,(select game_name,game_nbr,game_id from st_se_game_master )"
					+ " res where sgtnf.game_id = res.game_id";
			ResultSet result = stmt1.executeQuery(fetchGameDetailsQuery);
			boolean flag = false;
			while (result.next()) {
				nbrFormat.append("Nx*" + result.getString("game_id") + ":");
				nbrFormat.append(result.getString("game_nbr") + ":");
				nbrFormat.append(result.getString("game_name") + ":");
				nbrFormat.append(result.getInt("pack_nbr_digits") + ":");
				nbrFormat.append(result.getInt("book_nbr_digits") + ":");
				nbrFormat.append(result.getInt("ticket_nbr_digits") + ":");
				nbrFormat.append(result.getInt("game_nbr_digits") + ":");
				// nbrFormat.append(result.getInt("game_virn_digits"));
				flag = true;
			}
			System.out.println("result of pwt " + nbrFormat.toString());
			String nbrFormatStr = "";
			if (flag) {
				nbrFormatStr = nbrFormat.delete(0, 3).toString();
			}
			System.out.println("returned string = " + nbrFormatStr);
			return nbrFormatStr;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new LMSException(e);
		} catch (Exception e) {
			e.printStackTrace();
			throw new LMSException(e);
		} finally {
			try {
				connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
				throw new LMSException(e);
			}
		}
	}

	public Map fetchValidBookList(int gameId, int gameNbrDigits,
			int packNbrDigits, int bookNbrDigits, int noOfBkPerPack,
			String[] bookNo, String[] bookSeriesFrom, String[] bookSeriesTo,
			String[] packNo, String[] packSeriesFrom, String[] packSeriesTo,
			String statusType, Connection connection) throws LMSException {
		try {
			Map bookMap = new TreeMap();

			// Set<String> validBooks = new TreeSet<String>();
			Map<String, String> validBooks = new TreeMap<String, String>();
			Set<String> inValidBooks = new TreeSet<String>();
			Set<String> invalidPacks = new TreeSet<String>();
			List<BookSeriesBean> invalidBkSerList = new ArrayList<BookSeriesBean>();
			List<BookSeriesBean> invalidPkSerList = new ArrayList<BookSeriesBean>();

			ResultSet rs = null;
			boolean flag = false;
			// verify books
			if (bookNo != null && bookNo.length > 0) {
				for (int i = 0, l = bookNo.length; i < l; i++) {
					if (!"".equals(bookNo[i].trim())) {
						flag = isBookFormatValid(bookNo[i], gameNbrDigits,
								packNbrDigits, bookNbrDigits, gameId,
								noOfBkPerPack, connection);
						if (flag) {
							validBooks.put(bookNo[i], "");
						} else {
							inValidBooks.add(bookNo[i]);
						}
					}
				}
			}
			System.out.println("invalid books : " + inValidBooks
					+ "\nvalid books : " + validBooks);
			String bookNbrStr = validBooks.keySet().toString()
					.replace("[", "'").replace("]", "'").replace(", ", "','");
			System.out.println("invalid books : " + inValidBooks
					+ "\nvalid books : " + validBooks + "bookNbrStr"
					+ bookNbrStr);
			String fetchBookQuery = "select book_nbr, current_owner, book_status from st_se_game_inv_status where book_nbr in ("
					+ bookNbrStr + ") ";
			PreparedStatement bookPstmt = connection
					.prepareStatement(fetchBookQuery);
			if (bookNo != null && bookNo.length > 0) {
				for (int i = 0, l = bookNo.length; i < l; i++) {
					if (!"".equals(bookNo[i].trim())) {
						rs = bookPstmt.executeQuery();
						while (rs.next()) {
							if (!"CLAIMED".equalsIgnoreCase(rs
									.getString("book_status"))) {
								validBooks.put(rs.getString("book_nbr"), rs
										.getString("current_owner"));
							} else {
								inValidBooks.add(rs.getString("book_nbr"));
								validBooks.remove(rs.getString("book_nbr"));
							}
						}

					}
				}
			}

			// verify book series
			boolean statusTypeFlag = true;
			if ("ACTIVATE".equalsIgnoreCase(statusType.trim())) {
				statusTypeFlag = false;
			}

			String fetchBookSerListQuery = "select book_nbr, current_owner, book_status from st_se_game_inv_status where book_nbr between ? and ? ";
			PreparedStatement bookSerPstmt = connection
					.prepareStatement(fetchBookSerListQuery);
			BookSeriesBean serBean = null;
			String fromArr[] = null, toArr[] = null;
			int totBooks = 0;
			if (bookSeriesFrom != null && bookSeriesTo != null
					&& bookSeriesFrom.length > 0 && bookSeriesTo.length > 0) {
				for (int i = 0, l = bookSeriesFrom.length, k = bookSeriesTo.length; i < l
						&& i < k; i++) {
					if (!"".equals(bookSeriesFrom[i].trim())
							&& !"".equals(bookSeriesTo[i].trim())) {
						serBean = new BookSeriesBean();
						serBean.setBookNbrFrom(bookSeriesFrom[i]);
						serBean.setBookNbrTo(bookSeriesTo[i]);
						serBean.setStatus("Book Series is Not Valid");

						bookSerPstmt.setString(1, bookSeriesFrom[i]);
						bookSerPstmt.setString(2, bookSeriesTo[i]);
						System.out.println("bookSerPstmt = " + bookSerPstmt);
						rs = bookSerPstmt.executeQuery();

						fromArr = bookSeriesFrom[i].split("-");
						toArr = bookSeriesTo[i].split("-");
						totBooks = Integer.parseInt(toArr[1])
								- Integer.parseInt(fromArr[1]) + 1;
						rs.last(); // note 1
						int size = rs.getRow(); // note 2
						rs.beforeFirst(); // note 3

						boolean pkflag = false;
						System.out.println("totBooks= = " + totBooks
								+ ", rs.getFetchSize()=" + size
								+ " condition = " + (totBooks == size));
						if (totBooks == size) {
							while (rs.next()) {
								if (!"CLAIMED".equalsIgnoreCase(rs
										.getString("book_status"))) {
									validBooks.put(rs.getString("book_nbr"), rs
											.getString("current_owner"));
									pkflag = false;
								} else {
									inValidBooks.add(rs.getString("book_nbr"));

								}
							}
							if (pkflag) {
								invalidBkSerList.add(serBean);
							}
						} else {
							invalidBkSerList.add(serBean);
						}
					}
				}
			}
			System.out.println("invalid books : " + inValidBooks
					+ "\nvalid books : " + validBooks + "\ninvalidBkSerList : "
					+ invalidBkSerList);

			// verify Pack

			String fetchPackBookListQuery = "select book_nbr, current_owner, book_status from st_se_game_inv_status where pack_nbr =? ";
			PreparedStatement packPstmt = connection
					.prepareStatement(fetchPackBookListQuery);
			if (packNo != null && packNo.length > 0) {
				for (int i = 0, l = packNo.length; i < l; i++) {
					if (!"".equals(packNo[i].trim())) {
						packPstmt.setString(1, packNo[i]);
						rs = packPstmt.executeQuery();
						boolean pkflag = true;
						while (rs.next()) {
							if (!"CLAIMED".equalsIgnoreCase(rs
									.getString("book_status"))) {
								validBooks.put(rs.getString("book_nbr"), rs
										.getString("current_owner"));
								pkflag = false;
							} else {
								inValidBooks.add(rs.getString("book_nbr"));
							}
						}
						if (pkflag) {
							invalidPacks.add(packNo[i]);
						}
					}
				}
			}
			System.out.println("invalid books : " + inValidBooks
					+ "\nvalid books : " + validBooks + "\ninvalidPacks : "
					+ invalidPacks);

			// verify Pack Series

			String fetchPackBookSerListQuery = "select book_nbr, current_owner, book_status from st_se_game_inv_status where pack_nbr between ? and ? ";
			PreparedStatement packSerPstmt = connection
					.prepareStatement(fetchPackBookSerListQuery);
			if (packSeriesFrom != null && packSeriesTo != null
					&& packSeriesFrom.length > 0 && packSeriesTo.length > 0) {
				for (int i = 0, l = packSeriesFrom.length, k = packSeriesTo.length; i < l
						&& i < k; i++) {
					if (!"".equals(packSeriesFrom[i].trim())
							&& !"".equals(packSeriesTo[i].trim())) {
						serBean = new BookSeriesBean();
						serBean.setBookNbrFrom(packSeriesFrom[i]);
						serBean.setBookNbrTo(packSeriesTo[i]);
						serBean.setStatus("Pack Series is Not Valid");

						packSerPstmt.setString(1, packSeriesFrom[i]);
						packSerPstmt.setString(2, packSeriesTo[i]);
						rs = packSerPstmt.executeQuery();

						fromArr = packSeriesFrom[i].split("-");
						toArr = packSeriesTo[i].split("-");
						totBooks = Integer.parseInt(toArr[1])
								- Integer.parseInt(fromArr[1]) + 1;
						rs.last(); // note 1
						int size = rs.getRow(); // note 2
						rs.beforeFirst(); // note 3

						boolean pkflag = false;
						System.out.println("totBooks= = " + totBooks
								+ ", rs.getFetchSize()=" + size
								+ " condition = " + (totBooks == size));
						if (totBooks == size) {
							while (rs.next()) {
								if (!("CLAIMED".equalsIgnoreCase(rs
										.getString("book_status")) && statusTypeFlag)) {
									validBooks.put(rs.getString("book_nbr"), rs
											.getString("current_owner"));
									pkflag = false;
								} else {
									inValidBooks.add(rs.getString("book_nbr"));
								}
							}
							if (pkflag) {
								invalidPkSerList.add(serBean);
							}
						} else {
							invalidPkSerList.add(serBean);
						}
					}
				}
			}
			System.out.println("invalid books : " + inValidBooks
					+ "\nvalid books : " + validBooks + "\ninvalidPkSerList : "
					+ invalidPkSerList);

			bookMap.put("validBooks", validBooks);
			bookMap.put("inValidBooks", inValidBooks);
			bookMap.put("invalidPacks", invalidPacks);
			bookMap.put("invalidBkSerList", invalidBkSerList);
			bookMap.put("invalidPkSerList", invalidPkSerList);
			return bookMap;

		} catch (SQLException e) {
			e.printStackTrace();
			throw new LMSException();
		}
	}

	public Map<String, Set<String>> fetchValidVirnList(int gameId, String file,
			Connection con) throws LMSException {
		Map<String, Set<String>> map = new TreeMap<String, Set<String>>();
		try {

			// get the game details from database
			GameTicketFormatBean ticketFmtBean = getGameDetails(con, gameId);
			int gameNbrDigits = ticketFmtBean.getGameNbrDigits();
			int maxRankDigits = ticketFmtBean.getMaxRankDigits();
			int gameNbr = ticketFmtBean.getGameNbr();
			int virnDigits = ticketFmtBean.getGameVirnDigits();

			// VIRN file read from stream
			File fileObj = new File(file);
			if (!fileObj.exists()) {
				return null;
			}
			InputStreamReader fileStreamReader = new InputStreamReader(
					new FileInputStream(file));
			BufferedReader br = new BufferedReader(fileStreamReader);

			// It count the total no of virn_codes in file
			Set<String> virnCodeList = new TreeSet<String>();
			Set<String> inVirnCodeList = new TreeSet<String>();

			String strLine = null, virnCode = null;
			while ((strLine = br.readLine()) != null) {
				if ("".equals(strLine.trim())
						|| strLine.trim().length() != gameNbrDigits
								+ maxRankDigits + virnDigits
						|| gameNbr != Integer.parseInt(strLine.substring(0,
								gameNbrDigits))) {
					inVirnCodeList.add(strLine);
					continue;
				}
				virnCode = strLine.substring(gameNbrDigits + maxRankDigits,
						strLine.length());
				virnCodeList.add(virnCode);

			}
			// if(virnCodeList!=null && !virnCodeList.isEmpty())
			map.put("validVirn", virnCodeList);
			// if(inVirnCodeList!=null && !inVirnCodeList.isEmpty())
			map.put("inValidVirn", inVirnCodeList);

			return map;
		} catch (IOException e) {
			e.printStackTrace();
			throw new LMSException(e);
		}

	}

	private GameTicketFormatBean getGameDetails(Connection con, int gameId) {
		GameTicketFormatBean gameTicketFmtBean = new GameTicketFormatBean();
		Connection conn = con;
		try {
			String fetchGameDetQuery = "select gm.game_id, gm.game_nbr, game_nbr_digits, game_rank_digits "
					+ ", game_virn_digits from st_se_game_ticket_nbr_format gt, st_se_game_master gm where gm.game_id = gt.game_id "
					+ " and gm.game_id =" + gameId;
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(fetchGameDetQuery);
			while (rs.next()) {
				gameTicketFmtBean.setGameId(rs.getInt("game_id"));
				gameTicketFmtBean.setGameNbr(rs.getInt("game_nbr"));
				gameTicketFmtBean
						.setGameNbrDigits(rs.getInt("game_nbr_digits"));
				gameTicketFmtBean.setMaxRankDigits(rs
						.getInt("game_rank_digits"));
				gameTicketFmtBean.setGameVirnDigits(rs
						.getInt("game_virn_digits"));
			}
			rs.close();
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return gameTicketFmtBean;

	}

	public boolean isBookFormatValid(String bookNbr, int gameNbrDigits,
			int packNbrDigits, int bookNbrDigits, int gameId,
			int noOfBkPerPack, Connection connection) throws SQLException {

		boolean isDesh = bookNbr.indexOf("-") != -1;
		if (!isDesh) {
			if (bookNbr.length() == gameNbrDigits + packNbrDigits
					+ bookNbrDigits
					&& noOfBkPerPack >= Integer.parseInt(bookNbr.substring(
							gameNbrDigits + packNbrDigits, bookNbr.length()))) {
				bookNbr = bookNbr.substring(0, gameNbrDigits) + "-"
						+ bookNbr.substring(gameNbrDigits);
				return true;
			} else {
				return false;
				// Number Format Error/Out of Range Please Check
			}
		} else if (bookNbr.split("-").length == 2) {
			if (bookNbr.length() == gameNbrDigits + packNbrDigits
					+ bookNbrDigits + 1
					&& noOfBkPerPack >= Integer.parseInt(bookNbr
							.substring(gameNbrDigits + packNbrDigits + 1,
									bookNbr.length()))) {
				return true;
			} else {
				return false;
				// Number Format Error/Out of Range Please Check
			}
		} else {
			return false;
			// "Number Format Error/Out of Range Please Check"
		}

	}

	public Map updateBookNVirnStatus(String filePath, int gameId,
			String[] bookNo, String[] bookSeriesFrom, String[] bookSeriesTo,
			String[] packNo, String[] packSeriesFrom, String[] packSeriesTo,
			int userId, int userOrgId, String userType, String statusType,
			String remarks, String activateAt) throws LMSException {

		// validate data
		Connection connection = DBConnect.getConnection();
		Map bookMap = new TreeMap();

		try {
			connection.setAutoCommit(false);

			System.out.println("status ---- " + statusType);
			// update Book's status

			String getTicketFormatQuery = "select a.nbr_of_books_per_pack, b.pack_nbr_digits, "
					+ "b.book_nbr_digits, b.game_nbr_digits, game_virn_digits, game_rank_digits  "
					+ "from st_se_game_master a,st_se_game_ticket_nbr_format b where  a.game_id=b.game_id"
					+ " and a.game_id=?";
			PreparedStatement pstmt = connection
					.prepareStatement(getTicketFormatQuery);
			pstmt.setInt(1, gameId);
			ResultSet result = pstmt.executeQuery();
			System.out.println("getTicketFormatQuery = " + pstmt);
			if (result.next()) {
				int noOfBkPerPack = result.getInt("nbr_of_books_per_pack");
				int gameNbrDigits = result.getInt("game_nbr_digits");
				int packNbrDigits = result.getInt("pack_nbr_digits");
				int bookNbrDigits = result.getInt("book_nbr_digits");

				bookMap = fetchValidBookList(gameId, gameNbrDigits,
						packNbrDigits, bookNbrDigits, noOfBkPerPack, bookNo,
						bookSeriesFrom, bookSeriesTo, packNo, packSeriesFrom,
						packSeriesTo, statusType, connection);

				TreeMap<String, String> validBooks = (TreeMap<String, String>) bookMap
						.get("validBooks");
				;
				Set<String> inValidBooks = (Set<String>) bookMap
						.get("inValidBooks");
				Set<String> invalidPacks = (Set<String>) bookMap
						.get("invalidPacks");
				List<BookSeriesBean> invalidBkSerList = (List<BookSeriesBean>) bookMap
						.get("invalidBkSerList");
				List<BookSeriesBean> invalidPkSerList = (List<BookSeriesBean>) bookMap
						.get("invalidPkSerList");

				if (validBooks != null && validBooks.size() > 0) {
					updateBookStatus(validBooks, inValidBooks, userId,
							userOrgId, gameId, gameNbrDigits, packNbrDigits,
							statusType, userType, remarks, connection,
							activateAt);
				}

			}

			// update VIRN's status
			Map<String, Set<String>> map = null;

			Set<String> inValidVirnCodeSet = new TreeSet<String>();
			Set<String> virnCodeSetToUpdate = new TreeSet<String>();

			if (filePath != null && !"".equals(filePath.trim())) {
				System.out.println("File path ===" + filePath + "===");
				map = fetchValidVirnList(gameId, filePath, connection);
				if (map != null && !map.isEmpty()) {
					inValidVirnCodeSet = map.get("inValidVirn");
					virnCodeSetToUpdate = map.get("validVirn");
				}
				System.out.println("inValidVirnCodeSet = " + inValidVirnCodeSet
						+ "   , virnCodeSetToUpdate = " + virnCodeSetToUpdate);
				if (virnCodeSetToUpdate != null
						&& virnCodeSetToUpdate.size() > 0) {
					updateVirnStatus(virnCodeSetToUpdate, inValidVirnCodeSet,
							userId, userOrgId, gameId, statusType, remarks,
							connection);
				}
				System.out.println("inValidVirnCodeList = "
						+ inValidVirnCodeSet + "\nvirnCodeSetToUpdate = "
						+ virnCodeSetToUpdate);

			}
			bookMap.put("inValidVirnCodeSet", inValidVirnCodeSet);
			bookMap.put("virnCodeSetToUpdate", virnCodeSetToUpdate);

			connection.commit();

			return bookMap;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new LMSException(e);
		} finally {
			try {
				if (connection != null) {
					connection.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
				throw new LMSException(e);
			}
		}
	}

	public void updateBookStatus(TreeMap<String, String> validBooks,
			Set<String> invalidBooks, int userId, int userOrgId, int gameId,
			int gameNbrDigits, int packNbrDigits, String statusType,
			String userType, String remarks, Connection connection,
			String activateAt) throws SQLException {

		String updGameInvStatusQuery = "update st_se_game_inv_status set book_status = ? where book_nbr = ? and book_status !='CLAIMED' and game_id = "
				+ gameId;
		PreparedStatement updGameInvStatusPstmt = connection
				.prepareStatement(updGameInvStatusQuery);

		String insIntoMissBkHistoryQuery = "insert into st_se_miss_book_history(game_id, pack_nbr, book_nbr,"
				+ " status, user_id, user_org_id, date, remarks) values ("
				+ gameId
				+ ", ?, ?, ?, "
				+ userId
				+ ", "
				+ userOrgId
				+ ", '"
				+ new Timestamp(new Date().getTime())
				+ "', '"
				+ remarks
				+ "') ";
		PreparedStatement insIntoMissBkHistoryPstmt = connection
				.prepareStatement(insIntoMissBkHistoryQuery);

		int bkUpdRowCount = 0, bkInsRowCount = 0, bkRowUp;
		int packLength = gameNbrDigits + packNbrDigits + 1;

		String packNbr = null;
		String bookNo = null;
		String curOwner = null;
		String status = "MISSING";
		System.out.println("statusType = " + statusType);
		Iterator<Map.Entry<String, String>> validMapItr = validBooks.entrySet()
				.iterator();
		while (validMapItr.hasNext()) {
			Map.Entry<String, String> validPair = validMapItr.next();
			bookNo = validPair.getKey();// Book Nbr
			curOwner = validPair.getValue();// Owner

			System.out.println("Book No ::" + bookNo + "  Cur Owner ::"
					+ curOwner);
			if ("ACTIVATE".equalsIgnoreCase(statusType.trim())) {
				status = "INACTIVE";
				if (activateAt.equals("BO-AGENT")
						&& !"BO".equalsIgnoreCase(curOwner.trim())) {
					status = "ACTIVE";
				} else if (activateAt.equals("AGENT-RETAILER")
						&& "RETAILER".equalsIgnoreCase(curOwner.trim())) {
					status = "ACTIVE";
				}

			}

			System.out.println("Status ::" + status);
			updGameInvStatusPstmt.setString(1, status);
			updGameInvStatusPstmt.setString(2, bookNo);
			System.out.println("Query ::" + updGameInvStatusPstmt);
			bkRowUp = updGameInvStatusPstmt.executeUpdate();
			bkUpdRowCount += bkRowUp;

			packNbr = bookNo.substring(0, packLength);

			// ins data into st_se_miss_book_history
			if (bkRowUp > 0) {
				insIntoMissBkHistoryPstmt.setString(1, packNbr);
				insIntoMissBkHistoryPstmt.setString(2, bookNo);
				insIntoMissBkHistoryPstmt.setString(3, status);
				bkInsRowCount += insIntoMissBkHistoryPstmt.executeUpdate();
			} else {
				invalidBooks.add(bookNo);
				System.out.println(bookNo + " status is not updated in table");
			}
		}

		System.out.println("in validBooks : " + invalidBooks);

	}

	public void updateVirnStatus(Set<String> virnCodeSetToUpdate,
			Set<String> inValidVirnCodeSet, int userId, int userOrgId,
			int gameId, String statusType, String remarks, Connection connection)
			throws SQLException, LMSException {

		String fetchGameNbrQuery = "select game_nbr from st_se_game_master where game_id = "
				+ gameId;
		Statement stmt = connection.createStatement();
		ResultSet rs = stmt.executeQuery(fetchGameNbrQuery);
		int gameNbr = -1;
		if (rs.next()) {
			gameNbr = rs.getInt("game_nbr");
		} else {
			throw new LMSException("game Nbr is not find for game id = "
					+ gameId);
		}

		String status = "MISSING";

		// update status of VIRNS into st_se_pwt_inv_ table query
		String updVirnStatusQuery = "update st_se_pwt_inv_"
				+ gameNbr
				+ " set status = 'MISSING' where "
				+ " virn_code = ? and (status='UNCLM_PWT' or status='UNCLM_CANCELLED') and game_id ="
				+ gameId;

		if ("ACTIVATE".equalsIgnoreCase(statusType.trim())) {
			updVirnStatusQuery = "update st_se_pwt_inv_" + gameNbr
					+ " set status = 'UNCLM_PWT' where "
					+ " virn_code = ? and status='MISSING' and game_id ="
					+ gameId;
			status = "UNCLM_PWT";
		}
		PreparedStatement updVirnStatusPstmt = connection
				.prepareStatement(updVirnStatusQuery);

		// insert VIRNS into st_se_miss_pwt_inv_history table query
		String insIntoMissVirnHistoryQuery = "insert into st_se_miss_pwt_inv_history (virn_code, game_id, status, "
				+ " user_id, user_org_id, date, remarks) values(?, "
				+ gameId
				+ ", '"
				+ status
				+ "', "
				+ userId
				+ ", "
				+ userOrgId
				+ ", '"
				+ new Timestamp(new Date().getTime()) + "', '" + remarks + "')";
		PreparedStatement insIntoMissVirnHistoryPstmt = connection
				.prepareStatement(insIntoMissVirnHistoryQuery);

		int virnInsRowCount = 0, virnUpdRowCount = 0;
		for (String virnCode : virnCodeSetToUpdate) {
			updVirnStatusPstmt.setString(1, MD5Encoder.encode(virnCode.trim()));
			virnUpdRowCount = updVirnStatusPstmt.executeUpdate();

			// ins data into st_miss_virn_history
			if (virnUpdRowCount > 0) {
				insIntoMissVirnHistoryPstmt.setString(1, virnCode);
				virnInsRowCount += insIntoMissVirnHistoryPstmt.executeUpdate();
			} else {
				// virnCodeSetToUpdate.remove(virnCode);
				inValidVirnCodeSet.add(virnCode);
				System.out.println(virnCode + " Not Exist in st_se_pwt_inv_"
						+ gameNbr + " table");
			}
		}

		String fetchPrizeTypeQuery = "select prize_amt from st_se_rank_master where  game_id= ? group by prize_amt";
		PreparedStatement fetchPrizeTypePstmt = connection
				.prepareStatement(fetchPrizeTypeQuery);
		fetchPrizeTypePstmt.setInt(1, gameId);
		ResultSet fetchPrizeTypeRs = fetchPrizeTypePstmt.executeQuery();

		// query to update cancel VIRN list by recalculating the st_se_pwt_inv_?
		// game table
		String updCancelVirnQuery = "update st_se_rank_master  set no_of_prize_cancel = (select count(aa.pwt_amt)"
				+ " 'no_of_prize_cancel' from st_se_pwt_inv_? aa where aa.status='MISSING' and aa.game_id=? "
				+ " and pwt_amt = ?) where prize_amt = ? and game_id=?";
		PreparedStatement updCancelVirnPstmt = connection
				.prepareStatement(updCancelVirnQuery);
		updCancelVirnPstmt.setInt(1, gameNbr);
		updCancelVirnPstmt.setInt(2, gameId);

		System.out.println("updCancelVirnPstmtQuery == " + updCancelVirnPstmt);
		double prizeAmt = 0.0, noOfRowsUpd = 0;
		while (fetchPrizeTypeRs.next()) {
			prizeAmt = fetchPrizeTypeRs.getDouble("prize_amt");
			updCancelVirnPstmt.setDouble(3, prizeAmt);
			updCancelVirnPstmt.setDouble(4, prizeAmt);
			updCancelVirnPstmt.setInt(5, gameId);
			System.out
					.println("updCancelVirnPstmt ===== " + updCancelVirnPstmt);
			noOfRowsUpd = updCancelVirnPstmt.executeUpdate();
			System.out.println(noOfRowsUpd + " rows updated.");
		}

	}

	public String validateBookNVirnEntries(String fileName, int gameId,
			String[] bookNbr, String[] bookSeriesFrom, String[] bookSeriesTo,
			String[] packNbr, String[] packSeriesFrom, String[] packSeriesTo,
			String remarks) {
		String errMes = "";
		if (gameId <= 0) {
			errMes = "Please Select The Game Name.";
		}
		if (fileName == null || "".equals(fileName.trim())) {
			if ((bookNbr == null || bookNbr.length <= 0)
					&& (bookSeriesFrom == null || bookSeriesFrom.length <= 0)
					&& (bookSeriesTo == null || bookSeriesTo.length <= 0)
					&& (packNbr == null || packNbr.length <= 0)
					&& (packSeriesFrom == null || packSeriesFrom.length <= 0)
					&& (packSeriesTo == null || packSeriesTo.length <= 0)) {
				errMes = "Please Fill Empty Entries.";
			} else if (remarks == null || "".equals(remarks.trim())) {
				errMes = "Please Write Remarks.";
			}
		} else if (remarks == null || "".equals(remarks.trim())) {
			errMes = "Please Fill Entries in Remarks.";
		}
		System.out.println(errMes);
		return errMes;
	}

}
