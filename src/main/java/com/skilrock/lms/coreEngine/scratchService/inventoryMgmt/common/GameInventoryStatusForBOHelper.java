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
import java.util.Set;
import java.util.TreeMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.google.gson.Gson;
import com.skilrock.lms.common.db.DBConnect;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.coreEngine.scratchService.common.ScratchErrors;
import com.skilrock.lms.coreEngine.scratchService.common.ScratchException;
import com.skilrock.lms.coreEngine.scratchService.common.beans.WarehouseInventoryDetailBean;
import com.skilrock.lms.coreEngine.scratchService.common.beans.WarehouseWiseGameInventoryDetailBean;

public class GameInventoryStatusForBOHelper {
	private Log logger = LogFactory.getLog(GameInventoryStatusForBOHelper.class);
	public static void main(String[] args) {
		new GameInventoryStatusForBOHelper().getGameInvetoryWithBO(55);
	}

	StringBuilder resString = null;

	private Map<String, List<String>> CreateSeriesOfBooks(
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
		// logger.info("Total no of books === "+totalbooks);
		return seriesPackWiseMap;
	}

	/*
	 * public Map getGameInvetoryTotal(int gameid) { Set<String> packSet=new
	 * TreeSet<String>(); Set<String> bookSet=new TreeSet<String>(); List<String>
	 * list=new ArrayList<String>(); Connection con=new
	 * DBConnect().getConnection(); PreparedStatement pstmt=null; try {
	 * pstmt=con.prepareStatement("select game_id, pack_nbr, book_nbr,
	 * current_owner from st_se_game_inv_status where game_id = "+gameid+" order
	 * by book_nbr"); ResultSet rs=pstmt.executeQuery(); String bookNbr="";
	 * boolean first=true; String packNbr=""; String newPackNbr="";
	 * 
	 * while(rs.next()) { newPackNbr=rs.getString("pack_nbr");
	 * bookNbr=rs.getString("book_nbr"); if(packNbr.equals(newPackNbr)){
	 * list.add(bookNbr); }else{ if(!first){ packSet.add(newPackNbr); } list=new
	 * ArrayList<String>(); packNbr=newPackNbr; list.add(bookNbr); }
	 * first=false; } bookSet.addAll(list);
	 * 
	 * rs.close(); } catch (SQLException e) { e.printStackTrace(); } finally {
	 * try { if(pstmt!=null) pstmt.close(); if(con!=null && (!con.isClosed()))
	 * con.close(); } catch (SQLException e) { e.printStackTrace(); } }
	 * 
	 * return null; }
	 * 
	 * 
	 * 
	 * public Map getBoTotalActiveBooks(int gameid) { Map map=new TreeMap();
	 * List<String> list=new ArrayList<String>(); Connection con=new
	 * DBConnect().getConnection(); PreparedStatement pstmt=null; String
	 * name=""; String newName=null; String parent_name=null; boolean
	 * first=true; try { pstmt=con.prepareStatement(" select game_id, pack_nbr,
	 * book_nbr, book_status, (select name from st_lms_organization_master where
	 * organization_id=current_owner_id)'ret_name', (select name from
	 * st_lms_organization_master where
	 * organization_id=om.parent_id)'agent_name' from st_se_game_inv_status,
	 * st_lms_organization_master om where organization_id=current_owner_id and
	 * book_status ='ACTIVE' and game_id = ? order by current_owner_id,
	 * book_nbr"); pstmt.setInt(1, gameid); ResultSet rs=pstmt.executeQuery();
	 * String bookNbr=null; while(rs.next()) {
	 * parent_name=rs.getString("parent_name");
	 * newName=rs.getString("ret_name"); bookNbr=rs.getString("book_nbr");
	 * if(name.equals(newName)){ list.add(bookNbr); }else{ if(!first){
	 * map.put(name+" <i>Parent Name:- "+parent_name, list); } list=new
	 * ArrayList<String>(); name=newName; list.add(bookNbr); } first=false; }
	 * map.put(name, list); rs.close(); } catch (SQLException e) {
	 * e.printStackTrace(); } finally { try { if(pstmt!=null) pstmt.close();
	 * if(con!=null && (!con.isClosed())) con.close(); } catch (SQLException e) {
	 * e.printStackTrace(); } } return map; }
	 */

	public List<String> getAgentList() {
		List<String> agentList = new ArrayList<String>();
		Connection con = DBConnect.getConnection();
		PreparedStatement pstmt = null;
		try {
			pstmt = con
					.prepareStatement("select name 'agent_name' from st_lms_organization_master where organization_type='AGENT' order by name");
			ResultSet rs = pstmt.executeQuery();
			String agentName = null;
			// logger.info("getAgentList");
			while (rs.next()) {
				agentName = rs.getString("agent_name");
				// logger.info(agentName);
				agentList.add(agentName);
			}
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
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
		return agentList;
	}
/**
 * @changed agentName to agtId for orgCode Implementation 
 * @param gameid
 * @param agtId
 * @return
 */
	public String getBoTotalBooksWithAgent(int gameid, int agtId) {
		logger.info("agent called");
		resString = new StringBuilder("NONE");
		Map<String, List<String>> packWiseBookSeriesMap = null;
		Map<String, List<String>> packBooksMap = new TreeMap<String, List<String>>();
		List<String> list = new ArrayList<String>();
		Connection con = DBConnect.getConnection();
		PreparedStatement pstmt = null;
		try {
			String agentBooksDet = "select gm.nbr_of_books_per_pack, gis.pack_nbr, gis.book_nbr, om.name 'agent_name' from  st_se_game_inv_status gis, st_se_game_master gm, st_lms_organization_master om where  gis.current_owner_id=om.organization_id  and gm.game_id=gis.game_id and gis.current_owner = 'AGENT' and om.organization_id=? and gis.game_id  = ? order by gis.book_nbr";
			// String agentBooksDet = "select gm.nbr_of_books_per_pack,
			// gis.pack_nbr, gis.book_nbr, om.name 'agent_name' from
			// st_se_game_inv_status gis, st_se_game_master gm,
			// st_lms_organization_master om where
			// gis.current_owner_id=om.organization_id and
			// gm.game_id=gis.game_id and gis.current_owner = 'AGENT' and
			// (gis.book_status = 'ACTIVE' or gis.book_status = 'INACTIVE' ) and
			// om.name like ? and gis.game_id = ? order by gis.book_nbr";
			pstmt = con.prepareStatement(agentBooksDet);
			pstmt.setInt(1, agtId);
			pstmt.setInt(2, gameid);
			ResultSet rs = pstmt.executeQuery();
			logger.info("QUERY IS ===" + pstmt + "\n\n\n");
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
			packWiseBookSeriesMap = CreateSeriesOfBooks(packBooksMap,
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
					// logger.info(pack+", No of books
					// ="+(packBooksMap.get(pack)).size());
					List<String> bookSeriesList = packWiseBookSeriesMap
							.get(pack);
					String books = bookSeriesList.toString().replace("[", "")
							.replace("]", "");
					// logger.info("books === "+books);
					resString.append(books);

				}
				// logger.info("========================================");

			}
			logger.info(" response  String  === " + resString);
			// generate the book_nbr from series
			// generateSeries(packWiseBookSeriesMap);

		} catch (SQLException e) {
			e.printStackTrace();
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

	public String getBoTotalBooksWithRetailer(int gameid, int retId) {
		logger.info("ret called");
		resString = new StringBuilder("NONE");
		Map<String, List<String>> packWiseBookSeriesMap = null;
		Map<String, List<String>> packBooksMap = new TreeMap<String, List<String>>();
		List<String> list = new ArrayList<String>();
		Connection con = DBConnect.getConnection();
		PreparedStatement pstmt = null;
		try {

			// when we required All books
			String booksWithRet = "select gm.nbr_of_books_per_pack, gis.pack_nbr, gis.book_nbr, om.name 'agent_name' from  st_se_game_inv_status gis, st_se_game_master gm, st_lms_organization_master om where  gis.current_owner_id=om.organization_id  and gm.game_id=gis.game_id and gis.current_owner = 'RETAILER'  and om.organization_id= ? and gis.game_id  = ? order by gis.book_nbr";

			// when we only required non claimable books
			// String booksWithRet = "select gm.nbr_of_books_per_pack,
			// gis.pack_nbr, gis.book_nbr, om.name 'agent_name' from
			// st_se_game_inv_status gis, st_se_game_master gm,
			// st_lms_organization_master om where
			// gis.current_owner_id=om.organization_id and
			// gm.game_id=gis.game_id and gis.current_owner = 'RETAILER' and
			// (gis.book_status = 'ACTIVE' or gis.book_status = 'INACTIVE' ) and
			// om.name like ? and gis.game_id = ? order by gis.book_nbr";
			pstmt = con.prepareStatement(booksWithRet);
			pstmt.setInt(1,retId);
			pstmt.setInt(2, gameid);
			ResultSet rs = pstmt.executeQuery();
			logger.info("QUERY IS ===" + pstmt + "\n\n\n");
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
			packWiseBookSeriesMap = CreateSeriesOfBooks(packBooksMap,
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
					// logger.info(pack+", No of books
					// ="+(packBooksMap.get(pack)).size());
					List<String> bookSeriesList = packWiseBookSeriesMap
							.get(pack);
					String books = bookSeriesList.toString().replace("[", "")
							.replace("]", "");
					// logger.info("books === "+books);
					resString.append(books);

				}
				// logger.info("========================================");
			}

			logger.info(" response  String  === " + resString);
			// generate the book_nbr from series
			// generateSeries(packWiseBookSeriesMap);

		} catch (SQLException e) {
			e.printStackTrace();
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

	public String getGameInvetoryWithBO(int gameid) {
		resString = new StringBuilder("NONE");
		logger.info("bo called");
		Map<String, List<String>> packWiseBookSeriesMap = null;
		Map<String, List<String>> packBooksMap = new TreeMap<String, List<String>>();
		List<String> list = new ArrayList<String>();
		Connection con = DBConnect.getConnection();
		PreparedStatement pstmt = null;
		try {
			pstmt = con
					.prepareStatement("select gm.nbr_of_books_per_pack, gis.pack_nbr, gis.book_nbr from  st_se_game_inv_status gis, st_se_game_master gm where  gm.game_id=gis.game_id and gis.current_owner = 'BO' and gis.game_id  = ? order by gis.book_nbr");
			pstmt.setInt(1, gameid);
			ResultSet rs = pstmt.executeQuery();
			logger.info("QUERY IS ===" + pstmt + "\n\n\n");
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
			packWiseBookSeriesMap = CreateSeriesOfBooks(packBooksMap,
					booksPerPack);

			if (packWiseBookSeriesMap.size() > 0) {
				resString = new StringBuilder("");
				first = true;
				Set<String> packs = packWiseBookSeriesMap.keySet();
				for (String pack : packs) {
					if (first) {
						first = false;
					} else {
						resString.append("pack");
					}
					resString.append(pack);
					resString.append("book");
					// logger.info(pack+", No of books
					// ="+(packBooksMap.get(pack)).size());
					List<String> bookSeriesList = packWiseBookSeriesMap
							.get(pack);
					String books = bookSeriesList.toString().replace("[", "")
							.replace("]", "");
					// logger.info("books === "+books);
					resString.append(books);

				}
				// logger.info("========================================");
			}

			logger.info(" response  String  === " + resString);

			// generateSeries(packWiseBookSeriesMap);

		} catch (SQLException e) {
			e.printStackTrace();
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

	public Map<String, String> getGameMap() {
		Map<String, String> gameMap = new HashMap<String, String>();
		Connection con = DBConnect.getConnection();
		PreparedStatement pstmt = null;
		try {
			pstmt = con
					.prepareStatement("select game_id, game_nbr, concat(game_nbr, concat('-',game_name)) 'game_name'  from st_se_game_master order by game_nbr");
			ResultSet rs = pstmt.executeQuery();
			logger.info("getgameList");
			while (rs.next()) {
				String gameId = rs.getInt("game_id") + "";
				String gameName = rs.getString("game_name");
				gameMap.put(gameId, gameName);
			}
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
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

	public List<String> getRetailerList(String agentOrgName)
			throws LMSException {
		List<String> retOrgNameList = new ArrayList<String>();
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		con = DBConnect.getConnection();
		try {
			pstmt = con
					.prepareStatement("select o.name, o.organization_type  from st_lms_organization_master o, st_lms_organization_master om where o.organization_type='RETAILER' and o.parent_id=om.organization_id and  om.name like ? order by o.name");
			if ("-1".equalsIgnoreCase(agentOrgName.trim())) {
				pstmt.setString(1, "%");
			} else {
				pstmt.setString(1, agentOrgName.replace("amp", "&").trim());
			}
			rs = pstmt.executeQuery();
			logger.info("retailer list  query ==== " + pstmt);

			while (rs.next()) {
				String retName = rs.getString("name");
				retOrgNameList.add(retName);
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
	
	public String fetchWarehouseWiseInventory(int warehouse, int gameId) {
		resString = new StringBuilder("NONE");
		Connection con = DBConnect.getConnection();
		
		Map<Integer, WarehouseInventoryDetailBean> warehouseInventoryMap = new HashMap<Integer, WarehouseInventoryDetailBean>();
		WarehouseInventoryDetailBean warehouseInventoryDetailBean = null;
		Map<Integer, WarehouseWiseGameInventoryDetailBean> warehouseGameMap = null;
		WarehouseWiseGameInventoryDetailBean warehouseWiseGameInventoryDetailBean = null;
		Map<String, List<String>> packBookList = null;
		List<String> bookList = null;
		
		Statement stmt = null;
		ResultSet rs = null; 
		
		String jsonValue = null;
		
		String query = "select wm.warehouse_id, wm.warehouse_name, gm.game_id, gm.game_name, gm.nbr_of_books_per_pack, gis.pack_nbr, gis.book_nbr from st_se_game_inv_status gis inner join st_se_game_master gm on gis.game_id = gm.game_id inner join st_se_warehouse_master wm on gis.warehouse_id = wm.warehouse_id where gm.game_id = gis.game_id and gis.current_owner = 'BO' ";
		try {
			if (warehouse != -1) {
				query = query + " and wm.warehouse_id = " + warehouse;
			}

			if (gameId != -1) {
				query = query + " and gm.game_id = " + gameId;
			}
			
			query += " order by gis.book_nbr;";

			stmt = con.createStatement();
			rs = stmt.executeQuery(query);
			
			while (rs.next()) {
				if (warehouseInventoryMap.containsKey(rs.getInt("warehouse_id"))) {
					if (warehouseInventoryMap.get(rs.getInt("warehouse_id")).getWarehouseGameMap().containsKey(rs.getInt("game_id"))) {
						if(warehouseInventoryMap.get(rs.getInt("warehouse_id")).getWarehouseGameMap().get(rs.getInt("game_id")).getPackBookList().containsKey(rs.getString("pack_nbr"))) {
							warehouseInventoryMap.get(rs.getInt("warehouse_id")).getWarehouseGameMap().get(rs.getInt("game_id")).getPackBookList().get(rs.getString("pack_nbr")).add(rs.getString("book_nbr"));
						} else {
							bookList = new ArrayList<String>();
							bookList.add(rs.getString("book_nbr"));
							warehouseInventoryMap.get(rs.getInt("warehouse_id")).getWarehouseGameMap().get(rs.getInt("game_id")).getPackBookList().put(rs.getString("pack_nbr"), bookList);
						}
					} else {
						packBookList = new HashMap<String, List<String>>();
						bookList = new ArrayList<String>();
						bookList.add(rs.getString("book_nbr"));
						packBookList.put(rs.getString("pack_nbr"), bookList);

						warehouseWiseGameInventoryDetailBean = new WarehouseWiseGameInventoryDetailBean();
						warehouseWiseGameInventoryDetailBean.setGameName(rs.getString("game_name"));
						warehouseWiseGameInventoryDetailBean.setPackBookList(packBookList);
						
						warehouseInventoryMap.get(rs.getInt("warehouse_id")).getWarehouseGameMap().put(rs.getInt("game_id"), warehouseWiseGameInventoryDetailBean);
					}
				} else {
					packBookList = new HashMap<String, List<String>>();
					bookList = new ArrayList<String>();
					bookList.add(rs.getString("book_nbr"));
					packBookList.put(rs.getString("pack_nbr"), bookList);

					warehouseWiseGameInventoryDetailBean = new WarehouseWiseGameInventoryDetailBean();
					warehouseWiseGameInventoryDetailBean.setGameName(rs.getString("game_name"));
					warehouseWiseGameInventoryDetailBean.setPackBookList(packBookList);

					warehouseGameMap = new HashMap<Integer, WarehouseWiseGameInventoryDetailBean>();
					warehouseGameMap.put(rs.getInt("game_id"), warehouseWiseGameInventoryDetailBean);

					warehouseInventoryDetailBean = new WarehouseInventoryDetailBean();
					warehouseInventoryDetailBean.setWarehouseName(rs.getString("warehouse_name"));
					warehouseInventoryDetailBean.setWarehouseGameMap(warehouseGameMap);

					warehouseInventoryMap.put(rs.getInt("warehouse_id"), warehouseInventoryDetailBean);
				}
			}
			
			jsonValue = new Gson().toJson(warehouseInventoryMap);
			
			logger.info("Warehouse Wise Inventory Map " + warehouseInventoryMap);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (stmt != null) {
					stmt.close();
				}
				if (con != null && !con.isClosed()) {
					con.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return jsonValue;
	}

	public Map<Integer, String> fetchWareHouseMap() throws ScratchException {
		Statement stmt = null;
		ResultSet rs = null;
		Connection con = null;
		Map<Integer, String> warehouseMap = new HashMap<Integer, String>();
		try {
			con = DBConnect.getConnection();
			stmt = con.createStatement();
			rs = stmt.executeQuery("select warehouse_id, warehouse_name from st_se_warehouse_master;");
			while (rs.next()) {
				warehouseMap.put(rs.getInt("warehouse_id"), rs.getString("warehouse_name"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new ScratchException(ScratchErrors.SQL_EXCEPTION_ERROR_CODE, ScratchErrors.SQL_EXCEPTION_ERROR_MESSAGE);
		} catch (Exception e) {
			e.printStackTrace();
			throw new ScratchException(ScratchErrors.GENERAL_EXCEPTION_ERROR_CODE, ScratchErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		} finally {
			DBConnect.closeCon(con);
		}
		return warehouseMap;
	}

}