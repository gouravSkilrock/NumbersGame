package com.skilrock.lms.coreEngine.scratchService.inventoryMgmt.common;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import com.skilrock.lms.common.db.DBConnect;
import com.skilrock.lms.common.exception.LMSException;

public class BookWiseInvDetailForAgtHelper {

	private Map<String, List<String>> createSeriesOfBooks(
			Map<String, List<String>> map, int booksPerPack) {
		Map<String, List<String>> seriesPackWiseMap = new TreeMap<String, List<String>>();
		String firstBook = "";
		String lastBook = "";
		List<String> bookSeriesList = null;
		List<String> bookList = null;
		int totalbooks = 0;
		Set<String> packSet = map.keySet();

		for (String pack : packSet) {
			bookList = map.get(pack);
			firstBook = bookList.get(0);
			bookSeriesList = new ArrayList<String>();
			int compTicket = Integer.parseInt(firstBook.substring(firstBook
					.indexOf("-") + 1)) + 1;
			int length = bookList.size();
			totalbooks = totalbooks + length;
			if (length == 1) {
				bookSeriesList.add(firstBook + "TO" + bookList.get(length - 1));
			} else if (length == booksPerPack) {
				bookSeriesList.add(firstBook + "TO" + bookList.get(length - 1));
			} else {
				for (int i = 0; i < length - 1; i++) {
					int newTicket = Integer.parseInt(bookList.get(i + 1)
							.substring(firstBook.indexOf("-") + 1));
					if (newTicket != compTicket) {
						lastBook = bookList.get(i);
						bookSeriesList.add(firstBook + "TO" + lastBook);

						firstBook = bookList.get(i + 1);
						compTicket = Integer.parseInt(firstBook
								.substring(firstBook.indexOf("-") + 1));
					}
					compTicket += 1;
					if (!(i + 1 < length - 1)) {
						lastBook = bookList.get(i + 1);
						bookSeriesList.add(firstBook + "TO" + lastBook);
					}
				}
			}
			seriesPackWiseMap.put(pack, bookSeriesList);
		}
		// System.out.println("Total no of books === "+totalbooks);
		return seriesPackWiseMap;
	}

	public Map<String, String> getGameMap() throws LMSException {
		Map<String, String> gameMap = new HashMap<String, String>();
		Connection con = DBConnect.getConnection();
		PreparedStatement pstmt = null;
		try {
			pstmt = con
					.prepareStatement("select game_id, game_nbr, concat(game_nbr, concat('-',game_name)) 'game_name'  from st_se_game_master order by game_nbr");
			ResultSet rs = pstmt.executeQuery();
			System.out.println("getgameList");
			while (rs.next()) {
				String gameId = rs.getInt("game_id") + "";
				String gameName = rs.getString("game_name");
				gameMap.put(gameId, gameName);
			}
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new LMSException(e);
		} finally {
			try {
				if (pstmt != null) {
					pstmt.close();
				}
				if (con != null && !con.isClosed()) {
					con.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return gameMap;
	}

	public List<String> getRetailerList(int orgId) throws LMSException {
		List<String> retOrgNameList = new ArrayList<String>();
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		con = DBConnect.getConnection();
		try {
			pstmt = con
					.prepareStatement("select o.name, o.organization_type, o.organization_id  from st_lms_organization_master o where o.organization_type='RETAILER' and o.parent_id=? order by o.name");
			pstmt.setInt(1, orgId);
			rs = pstmt.executeQuery();
			System.out.println("retailer list  query ==== " + pstmt);
			while (rs.next()) {
				int org_id = rs.getInt("organization_id");
				String retName = rs.getString("name");
				retOrgNameList.add(org_id + "=" + retName);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new LMSException(e);
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (pstmt != null) {
					pstmt.close();
				}
				if (con != null && !con.isClosed()) {
					con.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return retOrgNameList;
	}

	public String getTotalBooksWithOrg(int gameid, int orgId, String orgType)
			throws LMSException {
		StringBuilder resString = new StringBuilder("NONE");
		Map<String, List<String>> packWiseBookSeriesMap = null;
		Map<String, List<String>> packBooksMap = new TreeMap<String, List<String>>();
		List<String> list = new ArrayList<String>();
		Connection con = DBConnect.getConnection();
		PreparedStatement pstmt = null;
		try {
			String agentBooksDet = "select gm.nbr_of_books_per_pack, gis.pack_nbr, gis.book_nbr, om.name 'agent_name' from  st_se_game_inv_status gis, st_se_game_master gm, st_lms_organization_master om where  gis.current_owner_id=om.organization_id  and gm.game_id=gis.game_id and gis.current_owner = ? and om.organization_id=? and gis.game_id  = ? order by gis.book_nbr";
			// String agentBooksDet = "select gm.nbr_of_books_per_pack,
			// gis.pack_nbr, gis.book_nbr, om.name 'agent_name' from
			// st_se_game_inv_status gis, st_se_game_master gm,
			// st_lms_organization_master om where
			// gis.current_owner_id=om.organization_id and
			// gm.game_id=gis.game_id and gis.current_owner = ? and
			// (gis.book_status = 'ACTIVE' or gis.book_status = 'INACTIVE' ) and
			// om.organization_id=? and gis.game_id = ? order by gis.book_nbr";
			pstmt = con.prepareStatement(agentBooksDet);
			pstmt.setString(1, orgType);
			pstmt.setInt(2, orgId);
			pstmt.setInt(3, gameid);
			ResultSet rs = pstmt.executeQuery();
			System.out.println("QUERY IS ===" + pstmt + "\n\n\n");
			String bookNbr = "";
			boolean first = true;
			String packNbr = "";
			String newPackNbr = "";
			int booksPerPack = -1;

			while (rs.next()) {
				booksPerPack = rs.getInt("nbr_of_books_per_pack");
				newPackNbr = rs.getString("pack_nbr");
				bookNbr = rs.getString("book_nbr");
				if (packNbr.equals(newPackNbr)) {
					list.add(bookNbr);
				} else {
					if (!first) {
						packBooksMap.put(packNbr, list);
					}
					list = new ArrayList<String>();
					packNbr = newPackNbr;
					list.add(bookNbr);
				}
				first = false;
			}
			packBooksMap.put(packNbr, list);
			rs.close();

			packBooksMap.remove("");

			// this method return the series of books on based of packs
			packWiseBookSeriesMap = createSeriesOfBooks(packBooksMap,
					booksPerPack);

			first = true;
			if (packWiseBookSeriesMap.size() > 0) {
				resString = new StringBuilder("");
				Set<String> packs = packWiseBookSeriesMap.keySet();
				for (String pack : packs) {
					if (first) {
						first = false;
					} else {
						resString.append("pack");
					}
					resString.append(pack);
					resString.append("book");
					// System.out.println(pack+", No of books
					// ="+(packBooksMap.get(pack)).size());
					List<String> bookSeriesList = packWiseBookSeriesMap
							.get(pack);
					String books = bookSeriesList.toString().replace("[", "")
							.replace("]", "");
					// System.out.println("books === "+books);
					resString.append(books);

				}
				// System.out.println("========================================");

			}
			System.out.println(" response  String  === " + resString);
			// generate the book_nbr from series
			// generateSeries(packWiseBookSeriesMap);

		} catch (SQLException e) {
			e.printStackTrace();
			throw new LMSException(e);
		} finally {
			try {
				if (pstmt != null) {
					pstmt.close();
				}
				if (con != null && !con.isClosed()) {
					con.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return resString.toString();
	}

}
