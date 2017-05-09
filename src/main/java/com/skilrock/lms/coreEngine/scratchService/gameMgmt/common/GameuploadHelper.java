package com.skilrock.lms.coreEngine.scratchService.gameMgmt.common;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.TreeMap;

import javax.crypto.NoSuchPaddingException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.skilrock.lms.beans.GameBean;
import com.skilrock.lms.beans.GameTicketFormatBean;
import com.skilrock.lms.beans.PackNumberSeriesBean;
import com.skilrock.lms.beans.PackSeriesFlagBean;
import com.skilrock.lms.beans.RankDetailBean;
import com.skilrock.lms.beans.VirnCodeBean;
import com.skilrock.lms.common.db.DBConnect;
import com.skilrock.lms.common.db.QueryManager;
import com.skilrock.lms.common.db.TableConstants;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.common.utility.GameUtilityHelper;
import com.skilrock.lms.common.utility.MD5Encoder;
import com.skilrock.lms.web.scratchService.inventoryMgmt.common.GenerateTicketsNumber;
import com.skilrock.lms.web.scratchService.utility.VirnEncoderNDecoder;
import com.skilrock.lms.web.scratchService.utility.VirnEncoderNDecoder.EncryptionException;

/**
 * This helper class is used to upload the game
 * 
 * @author ABC
 * 
 */
public class GameuploadHelper {
	
	static Log logger = LogFactory.getLog(GameuploadHelper.class);

	private static int findDigit(int k) {
		int count = 0;
		while (k != 0) {
			k = k / 10;
			count++;
		}
		return count;
	}

	public PackSeriesFlagBean checkPackValidity(String packnbrf,
			String packnbrt, String gameName, int gameNbr) throws LMSException {

		int finalPackDigit = 0, finalBookDigit = 0, finalGameDigit = 0, totalPack = 0;
		PackSeriesFlagBean flagBean = new PackSeriesFlagBean();
		Connection conn = DBConnect.getConnection();

		try {
			// Getting Game Details using game id
			GameTicketFormatBean ticketFmtBean = getGameDetails(conn, gameNbr,
					gameName);
			int game_id = ticketFmtBean.getGameId();
			int game_nbr = gameNbr;
			int game_nbr_digit = findDigit(game_nbr);

			String query2 = "select * from st_se_game_ticket_nbr_format where game_id = ?"; // QueryManager.getST4GameDetailsUsingGameName();
			PreparedStatement stmt2 = conn.prepareStatement(query2);
			stmt2.setInt(1, game_id);
			ResultSet resultSet2 = stmt2.executeQuery();
			while (resultSet2.next()) {
				finalBookDigit = resultSet2.getInt("book_nbr_digits");
				finalPackDigit = resultSet2.getInt("pack_nbr_digits");
				finalGameDigit = resultSet2.getInt("game_nbr_digits");
			}
			System.out.println("Ticket Formate is :---" + "finalBookDigit  "
					+ finalBookDigit + ",  finalPackDigit : " + finalPackDigit
					+ ",  finalGameDigit : " + finalGameDigit);

			String pf_ft = null, pf_st = null, pt_ft = null, pt_st = null;
			int tempgamenbrforpt = 0, tempgamenbrforpf = 0, integerpackfrom = 0, integerpackto = 0;
			StringTokenizer st1 = null, st2 = null;

			if (packnbrt.indexOf("-") == -1
					&& packnbrt.length() > finalGameDigit) {
				packnbrt = packnbrt.substring(0, finalGameDigit) + "-"
						+ packnbrt.substring(finalGameDigit);
			}
			if (packnbrf.indexOf("-") == -1
					&& packnbrf.length() > finalGameDigit) {
				packnbrf = packnbrf.substring(0, finalGameDigit) + "-"
						+ packnbrf.substring(finalGameDigit);

			}

			st1 = new StringTokenizer(packnbrt, "-");
			st2 = new StringTokenizer(packnbrf, "-");
			for (int i = 0; i < 2; i++) {
				if (st1.hasMoreTokens()) {
					if (i == 1) {
						pt_st = st1.nextToken();
						pf_st = st2.nextToken();
						integerpackfrom = Integer.parseInt(pf_st);
						integerpackto = Integer.parseInt(pt_st);
						if (pt_st.length() != finalPackDigit
								|| pf_st.length() != finalPackDigit) {
							flagBean.setPackdigitformatflag(false);
						}
					} else {
						pt_ft = st1.nextToken();
						pf_ft = st2.nextToken();
						tempgamenbrforpf = Integer.parseInt(pf_ft);
						tempgamenbrforpt = Integer.parseInt(pt_ft);
						if (pt_ft.length() != game_nbr_digit
								|| pf_ft.length() != game_nbr_digit) {
							flagBean.setGamenbrformatflag(false);
						}
						if (tempgamenbrforpt != game_nbr
								|| tempgamenbrforpf != game_nbr) {
							flagBean.setGamenbrflag(false);
						}
					}
				}
			}
			totalPack = integerpackto - integerpackfrom + 1;
			System.out.println("totalpack in this series " + totalPack);
			GenerateTicketsNumber ticketsNumber = new GenerateTicketsNumber(
					totalPack, game_nbr, finalPackDigit, finalBookDigit,
					integerpackfrom, integerpackto);
			System.out.println("ticket list = "
					+ ticketsNumber.getPackNbrList());
			List<String> packlist = ticketsNumber.getPackNbrList();
			Iterator<String> itr = packlist.iterator();
			String query = "";

			while (itr.hasNext()) {
				if (query == "") {
					query = query + "'" + itr.next() + "'";
				} else {
					query = query + "," + "'" + itr.next() + "'";
				}
			}
			String validateQuery = "select * from st_se_game_inv_status where pack_nbr in("
					+ query + ")";
			System.out.println("validateQuery ->  " + validateQuery);
			PreparedStatement stmt3 = conn.prepareStatement(validateQuery);
			ResultSet resultSet3 = null;
			resultSet3 = stmt3.executeQuery();
			while (resultSet3.next()) {
				flagBean.setPackseriespresenceflag(false);
			}
			return flagBean;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new LMSException(e);
		} finally {
			try {
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	}

	/**
	 * This method is used when we using the '1W Encryption' to create the virn
	 * list to insert into database.
	 * 
	 * @param br
	 *            BufferReader
	 * @param encVirnStrBuilder
	 * @param gameNbrDigits
	 * @param maxRankDigits
	 * @param game_id
	 * @param rankDetailMap
	 * @return
	 * @throws NumberFormatException
	 * @throws IOException
	 */
	private ArrayList<VirnCodeBean> create1WEncVirnBeanListWithFixedCode(
			BufferedReader br, StringBuilder encVirnStrBuilder,
			int gameNbrDigits, int maxRankDigits, int game_id,
			Map<Integer, RankDetailBean> rankDetailMap)
			throws NumberFormatException, IOException {

		VirnCodeBean virnBean = null;
		String strLine = null;
		int rank_id = 0;
		String virn_code = null;
		String encoded_virn_code = null;
		RankDetailBean rankDetailBean = null;
		ArrayList<VirnCodeBean> virnBeanList = new ArrayList<VirnCodeBean>(
				50000);
		encVirnStrBuilder.ensureCapacity(50000 * 15);
		final String DEFAULT_KEY = "1234";
		String enckey = null, encTicket = null;
		String virnDetArr[];
		while ((strLine = br.readLine()) != null) {
			if ("".equals(strLine.trim())) {
				continue;
			}

			virnDetArr = strLine.split("\t");

			virnDetArr[0] = virnDetArr[0].trim(); // virn_code
			virnDetArr[1] = virnDetArr[1].trim(); // ticket_nbr
			// DEFAULT_KEY //verification_code

			// get the rank id and virn_code
			rank_id = Integer.parseInt(virnDetArr[0].substring(gameNbrDigits,
					gameNbrDigits + maxRankDigits));
			virn_code = strLine.substring(gameNbrDigits + maxRankDigits,
					virnDetArr[0].length());

			// 1W encryption of virn , ticket and key is done
			encoded_virn_code = MD5Encoder.encode(virn_code);
			enckey = MD5Encoder.encode(DEFAULT_KEY);
			encTicket = MD5Encoder.encode(virnDetArr[1]);

			encVirnStrBuilder.append("'").append(encoded_virn_code)
					.append("',");

			// get the prize amount and status detail of game
			rankDetailBean = rankDetailMap.get(rank_id);

			// virn bean is created
			virnBean = new VirnCodeBean();
			virnBean.setGame_id(game_id);
			virnBean.setPwt_amt(rankDetailBean.getPrize_amount());
			virnBean.setPrize_level(rankDetailBean.getStatus());
			if (rankDetailBean.getPrize_amount() > 0) {
				virnBean.setStatus("UNCLM_PWT");
			} else {
				virnBean.setStatus("NO_PRIZE_PWT");
			}
			virnBean.setVirn_code(encoded_virn_code);
			virnBean.setId1(encTicket);
			virnBean.setId2(enckey);
			virnBeanList.add(virnBean);
		}

		virnBeanList.trimToSize();
		encVirnStrBuilder.trimToSize();

		return virnBeanList;
	}

	/**
	 * This method is used when we using the '2W Encryption of virn(1W Encoded) &
	 * ticket(1W Encoded) with variable code(1W)' to create the virn list to
	 * insert into database.
	 * 
	 * @param br
	 *            BufferReader
	 * @param encVirnStrBuilder
	 * @param gameNbrDigits
	 * @param maxRankDigits
	 * @param game_id
	 * @param rankDetailMap
	 * @return
	 * @throws NumberFormatException
	 * @throws IOException
	 * @throws EncryptionException
	 * @throws NoSuchPaddingException
	 * @throws NoSuchAlgorithmException
	 */
	private ArrayList<VirnCodeBean> create2WEncVirnBeanList(BufferedReader br,
			StringBuilder encVirnStrBuilder, int gameNbrDigits,
			int maxRankDigits, int game_id,
			Map<Integer, RankDetailBean> rankDetailMap)
			throws NumberFormatException, IOException, EncryptionException,
			NoSuchAlgorithmException, NoSuchPaddingException {

		VirnCodeBean virnBean = null;
		String strLine = null;
		int rank_id = 0;
		String virn_code = null;
		String encoded_virn_code = null, encodedTkt = null, enckey = null;
		RankDetailBean rankDetailBean = null;
		ArrayList<VirnCodeBean> virnBeanList = new ArrayList<VirnCodeBean>(
				50000);
		encVirnStrBuilder.ensureCapacity(50000 * 15);
		String virnDetArr[];
		VirnEncoderNDecoder encoder = new VirnEncoderNDecoder();
		while ((strLine = br.readLine()) != null) {
			if ("".equals(strLine.trim())) {
				continue;
			}

			virnDetArr = strLine.split("\t");

			virnDetArr[0] = virnDetArr[0].trim(); // virn_code
			virnDetArr[1] = virnDetArr[1].trim(); // ticket_nbr
			virnDetArr[2] = virnDetArr[2].trim(); // verification_code

			// get the rank id and virn_code
			rank_id = Integer.parseInt(virnDetArr[0].substring(gameNbrDigits,
					gameNbrDigits + maxRankDigits));
			virn_code = strLine.substring(gameNbrDigits + maxRankDigits,
					virnDetArr[0].length());

			// encrypt the virn, ticket and code using 1W encryption
			encoded_virn_code = MD5Encoder.encode(virn_code);
			encodedTkt = MD5Encoder.encode(virnDetArr[1]);
			// enckey = MD5Encoder.encode(virnDetArr[2]);

			// 2W encryption of ticket using (virn(1W)+code(1W))
			encoded_virn_code = encoder.encrypt(encoded_virn_code,
					virnDetArr[2]);
			encodedTkt = encoder.encrypt(encodedTkt, virnDetArr[2]);

			// get the prize amount and status detail of game
			rankDetailBean = rankDetailMap.get(rank_id);

			// virn bean is created
			virnBean = new VirnCodeBean();
			virnBean.setVirn_code(encoded_virn_code);
			virnBean.setGame_id(game_id);
			virnBean.setPwt_amt(rankDetailBean.getPrize_amount());
			virnBean.setPrize_level(rankDetailBean.getStatus());
			if (rankDetailBean.getPrize_amount() > 0) {
				virnBean.setStatus("UNCLM_PWT");
			} else {
				virnBean.setStatus("NO_PRIZE_PWT");
			}
			virnBean.setId1(encodedTkt);
			virnBean.setId2("");
			// virnBean.setId2(enckey);
			virnBeanList.add(virnBean);

			encVirnStrBuilder.append("'").append(encoded_virn_code)
					.append("',");
		}

		virnBeanList.trimToSize();
		encVirnStrBuilder.trimToSize();

		return virnBeanList;
	}

	/**
	 * This method is used when we using the '2W Encryption for ticket with
	 * fixed code' to create the virn list to insert into database.
	 * 
	 * @param br
	 *            BufferReader
	 * @param encVirnStrBuilder
	 * @param gameNbrDigits
	 * @param maxRankDigits
	 * @param game_id
	 * @param rankDetailMap
	 * @return
	 * @throws NumberFormatException
	 * @throws IOException
	 * @throws EncryptionException
	 * @throws NoSuchPaddingException
	 * @throws NoSuchAlgorithmException
	 */
	private ArrayList<VirnCodeBean> create2WEncVirnBeanListWithFixedCode(
			BufferedReader br, StringBuilder encVirnStrBuilder,
			int gameNbrDigits, int maxRankDigits, int game_id,
			Map<Integer, RankDetailBean> rankDetailMap)
			throws NumberFormatException, IOException, EncryptionException,
			NoSuchAlgorithmException, NoSuchPaddingException {

		VirnCodeBean virnBean = null;
		String strLine = null;
		int rank_id = 0;
		String virn_code = null;
		String encoded_virn_code = null, encodedTkt = null, encKey = null;
		RankDetailBean rankDetailBean = null;
		ArrayList<VirnCodeBean> virnBeanList = new ArrayList<VirnCodeBean>(
				50000);
		encVirnStrBuilder.ensureCapacity(50000 * 15);
		String virnDetArr[];
		VirnEncoderNDecoder encoder = new VirnEncoderNDecoder();
		final String DEFAULT_KEY = "1234";
		// final String DEFAULT_ENCRYPTION_KEY = MD5Encoder.encode(DEFAULT_KEY);
		// //1234
		while ((strLine = br.readLine()) != null) {
			if ("".equals(strLine.trim())) {
				continue;
			}

			virnDetArr = strLine.split("\t");

			virnDetArr[0] = virnDetArr[0].trim(); // virn_code
			virnDetArr[1] = virnDetArr[1].trim(); // ticket_nbr
			// DEFAULT_KEY; //verification_code

			// get the rank id and virn_code
			rank_id = Integer.parseInt(virnDetArr[0].substring(gameNbrDigits,
					gameNbrDigits + maxRankDigits));
			virn_code = strLine.substring(gameNbrDigits + maxRankDigits,
					virnDetArr[0].length());

			// 2W encryption of ticket using (virn+code)
			encodedTkt = encoder.encrypt(virnDetArr[1],
					(virn_code + DEFAULT_KEY));
			encoded_virn_code = MD5Encoder.encode(virn_code);
			encKey = MD5Encoder.encode(DEFAULT_KEY);

			encVirnStrBuilder.append("'").append(encoded_virn_code)
					.append("',");

			// get the prize amount and status detail of game
			rankDetailBean = rankDetailMap.get(rank_id);

			// virn bean is created
			virnBean = new VirnCodeBean();
			virnBean.setVirn_code(encoded_virn_code);
			virnBean.setGame_id(game_id);
			virnBean.setPwt_amt(rankDetailBean.getPrize_amount());
			virnBean.setPrize_level(rankDetailBean.getStatus());
			if (rankDetailBean.getPrize_amount() > 0) {
				virnBean.setStatus("UNCLM_PWT");
			} else {
				virnBean.setStatus("NO_PRIZE_PWT");
			}
			virnBean.setId1(encodedTkt);
			virnBean.setId2(encKey);
			virnBeanList.add(virnBean);
		}

		virnBeanList.trimToSize();
		encVirnStrBuilder.trimToSize();

		return virnBeanList;
	}

	/**
	 * This method is used to fetch the game listfrom the data base according to
	 * game type
	 * 
	 * @param game_type
	 * @return ArrayList of GameBean type
	 */

	public ArrayList<GameBean> fatchGameList(String gameType) {
		 
		ArrayList<GameBean> list = new ArrayList<GameBean>();
		GameBean gameBean = null;
		Connection con = DBConnect.getConnection();
		Statement stmt2 = null;
		ResultSet rs2 = null;
		try {
			stmt2 = con.createStatement();
			String gmdetail = QueryManager.getGameDetails() + "'" + gameType
					+ "' and add_inv_status='F'";
			// String gmdetail = "select game_id, game_status, game_nbr,
			// game_name, sale_end_date from st_se_game_master where game_status
			// IN ";
			// if("ALL".equalsIgnoreCase(gameType.trim()))
			// gmdetail = gmdetail +"('NEW', 'OPEN') and add_inv_status='F'";
			// else gmdetail = gmdetail +"('"+gameType+"') and
			// add_inv_status='F'";
			System.out.println("fetch " + gameType + " game list Query Is:"
					+ gmdetail);
			rs2 = stmt2.executeQuery(gmdetail);
			while (rs2.next()) {
				gameBean = new GameBean();
				gameBean.setGameId(rs2.getInt(TableConstants.GAME_ID));
				gameBean.setGameName(rs2.getInt("game_nbr") + "-"
						+ rs2.getString(TableConstants.GAME_NAME));
				gameBean.setSaleEndDate(rs2
						.getDate(TableConstants.SALE_END_DATE));
				// gameBean.setGameStatus(rs2.getString("game_status"));
				list.add(gameBean);
			}
			return list;
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (con != null) {
					con.close();
				}
			} catch (SQLException se) {
				se.printStackTrace();
			}
		}
		return null;
	}

	/**
	 * This method is used to fetch the supplier list from the database
	 * 
	 * @return ArrayList of SupplierBean type
	 * @author Skilrock Technologies
	 * @throws SQLException
	 */

	public Map<Integer, String> fatchSupplierList() throws SQLException {
		Map<Integer, String> supplierMap = new TreeMap<Integer, String>();
		Connection con = DBConnect.getConnection();
		try {
			Statement stmt = con.createStatement();
			ResultSet rs = stmt
					.executeQuery("select *  from st_se_supplier_master");
			while (rs.next()) {
				int supplierid = rs.getInt(TableConstants.SUPPLIER_ID);
				String supplierName = rs
						.getString(TableConstants.SUPPLIER_NAME);
				supplierMap.put(supplierid, supplierName);
			}
			System.out.println("supplier list == " + supplierMap);
			return supplierMap;
		} finally {
			if (con != null) {
				con.close();
			}
		}

	}

	/**
	 * This method is used to fetch game list to upload VIRN code
	 * 
	 * @param game_type
	 * @return ArrayList of GameBean type
	 */
	public ArrayList<GameBean> fatchVirnGameList(String game_type) {
		 
		ArrayList<GameBean> list = new ArrayList<GameBean>();
		GameBean gameBean = null;
		Connection con = DBConnect.getConnection();
		Statement stmt2 = null;
		ResultSet rs2 = null;
		try {
			stmt2 = con.createStatement();
			String gmdetail = null;
			System.out.println("game type is " + game_type);

			gmdetail = QueryManager.getGameDetails() + "'" + game_type
					+ "'and add_inv_status='T'";
			System.out.println("Query _-------" + gmdetail);
			rs2 = stmt2.executeQuery(gmdetail);
			while (rs2.next()) {
				gameBean = new GameBean();
				gameBean.setGameNbr(rs2.getInt("game_nbr"));
				gameBean.setGameId(rs2.getInt(TableConstants.GAME_ID));
				gameBean.setGameName(rs2.getInt("game_nbr") + "-"
						+ rs2.getString(TableConstants.GAME_NAME));
				gameBean.setSaleEndDate(rs2
						.getDate(TableConstants.SALE_END_DATE));
				list.add(gameBean);
			}
			return list;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				if (con != null) {
					con.close();
				}
			} catch (SQLException se) {
				se.printStackTrace();
			}
		}
		return null;
	}

	/**
	 * This method inside helper class is used to check validity for VIRN file
	 * 
	 * @param vcList
	 * @param gameId
	 * @return boolean
	 * @throws LMSException
	 */
	public boolean fileStatusCheck(String vcList, int gameId, String gameNbr)
			throws LMSException {
		boolean virnFlag = true;
		 
		Connection connection;
		connection = DBConnect.getConnection();
		ResultSet resultSet = null;
		PreparedStatement stmt = null;
		String query = "select * from st_se_pwt_inv_" + gameNbr
				+ " where game_id=" + gameId + " and virn_code in(" + vcList
				+ ")";

		// System.out.println("Query ----" +query );
		try {
			stmt = connection.prepareStatement(query);
			resultSet = stmt.executeQuery();
			while (resultSet.next()) {
				// System.out.println("virn duplicate is" +
				// resultSet.getString("virn_code")) ;
				virnFlag = false;
				return virnFlag;
			}
			query = null;
			return virnFlag;
		} catch (Exception e) {
			throw new LMSException("Please Upload Correct File");
		} finally {
			try {
				if (stmt != null) {
					stmt.close();
				}
				if (connection != null) {
					connection.close();
				}
			} catch (SQLException e2) {
				e2.printStackTrace();
				throw new LMSException("Exception During Close Statement", e2);
			}
		}
	}

	private GameTicketFormatBean getGameDetails(Connection con, int gameNbr,
			String gameName) {
		GameTicketFormatBean gameTicketFmtBean = new GameTicketFormatBean();
		Connection conn = con;
		try {
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt
					.executeQuery("select game_id, game_nbr_digits, game_rank_digits from st_se_game_ticket_nbr_format where game_id =(select game_id from st_se_game_master where game_name='"
							+ gameName + "' and game_nbr = " + gameNbr + ")");
			while (rs.next()) {
				gameTicketFmtBean.setGameId(rs.getInt("game_id"));
				gameTicketFmtBean
						.setGameNbrDigits(rs.getInt("game_nbr_digits"));
				gameTicketFmtBean.setMaxRankDigits(rs
						.getInt("game_rank_digits"));
			}
			rs.close();
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return gameTicketFmtBean;

	}

	/**
	 * This method is used to get the game Id for given game Name
	 * 
	 * @param gameName
	 * @return int
	 */

	public int getGameId(String gameNbr, String gameName) {

		int gameId = 0;

		Connection connection = DBConnect.getConnection();
		try {
			Statement stmt = connection.createStatement();
			ResultSet gameDetails = stmt
					.executeQuery("select game_id from st_se_game_master where game_name='"
							+ gameName + "' and game_nbr = " + gameNbr);
			while (gameDetails.next()) {
				gameId = gameDetails.getInt("game_id");
			}
			System.out.println("game id is " + gameId);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (connection != null) {
					connection.close();
				}
			} catch (SQLException se) {
				se.printStackTrace();
			}
		}
		return gameId;
	}

	public GameTicketFormatBean getGameNbrDigitsByGameId(int game_id)
			throws LMSException {

		 
		Connection conn = DBConnect.getConnection();
		GameTicketFormatBean gameTicketFmtBean = new GameTicketFormatBean();
		try {
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt
					.executeQuery("select game_nbr_digits,game_rank_digits from st_se_game_ticket_nbr_format where game_id="
							+ game_id + "");
			while (rs.next()) {
				gameTicketFmtBean
						.setGameNbrDigits(rs.getInt("game_nbr_digits"));
				gameTicketFmtBean.setMaxRankDigits(rs
						.getInt("game_rank_digits"));

			}
		} catch (SQLException e) {
			e.printStackTrace();

		} finally {
			try {
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException se) {
				se.printStackTrace();
			}
		}
		return gameTicketFmtBean;

	}

	/**
	 * This method is used to get start date,sale end date,pwt end date for a
	 * game
	 * 
	 * @param gameName
	 *            is name of the game
	 * @param GameType
	 *            is type of game
	 * @return String
	 * @throws LMSException
	 */
	public String getInitialDates(String gameName, String GameType)
			throws LMSException {

		Connection connection = DBConnect.getConnection();
		try {

			String gameNameArr[] = gameName.split("-");
			Date startDate;
			Date saleEndDate;
			Date pwtEndDate;
			String allDatesString = "";
			Statement stmt = connection.createStatement();
			// write this query into helper class
			String getGameDatesQuery = QueryManager.getGameDates() + "'"
					+ gameNameArr[1] + "' and game_nbr =" + gameNameArr[0];
			System.out.println("game dates query == " + getGameDatesQuery);
			ResultSet gameDetails = stmt.executeQuery(getGameDatesQuery);
			while (gameDetails.next()) {
				int gameId = gameDetails.getInt(TableConstants.GAME_ID);
				startDate = gameDetails.getDate(TableConstants.START_DATE);
				saleEndDate = gameDetails.getDate(TableConstants.SALE_END_DATE);
				pwtEndDate = gameDetails.getDate(TableConstants.PWT_END_DATE);
				allDatesString = "" + startDate + "*" + saleEndDate + "*"
						+ pwtEndDate;
				System.out.println("game id ==" + gameId
						+ "  ,alldatestring is " + allDatesString);

			}
			return allDatesString;

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
			}
		}

	}

	/**
	 * this method inside helper class is used to get the NEW games
	 * 
	 * @return
	 */
	public List<GameBean> getNewGames() {

		 
		Connection connection;
		connection = DBConnect.getConnection();
		GameBean gameBean = null;
		List<GameBean> list = new ArrayList<GameBean>();
		try {
			Statement stmt = connection.createStatement();
			ResultSet gameDetails = stmt
					.executeQuery("select * from st_se_game_master where game_status='NEW'and start_date <= 'CURRENT_DATE'");
			while (gameDetails.next()) {
				gameBean = new GameBean();
				gameBean.setGameId(gameDetails.getInt(TableConstants.GAME_ID));
				gameBean.setGameName(gameDetails
						.getString(TableConstants.GAME_NAME));
				gameBean
						.setGameNbr(gameDetails.getInt(TableConstants.GAME_NBR));
				gameBean.setStartDate(gameDetails
						.getDate(TableConstants.SGM_START_DATE));
				list.add(gameBean);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (connection != null) {
					connection.close();
				}
			} catch (SQLException se) {
				se.printStackTrace();
			}
		}
		return list;
	}

	/**
	 * This method is used to insert game dates and VIRN file into database
	 * 
	 * @param vcbeanList
	 * @param d1
	 *            is the game start date
	 * @param d2
	 *            is Sale End Date
	 * @param d3
	 *            is PWT End Date
	 * @param gameId
	 *            is The GAme ID
	 * @throws LMSException
	 */
	public void insertTicketDetailsIntoDataBase(List<VirnCodeBean>

	vcbeanList, java.sql.Date d1, java.sql.Date d2, java.sql.Date d3, int gameId)
			throws

			LMSException {

		 
		Connection connection;
		connection = DBConnect.getConnection();
		Statement stmt1 = null;
		PreparedStatement stmt = null;
		System.out.println("d3 is " + d3);
		String query = QueryManager.updatePwtInvTable();
		// write this query into helper class
		String querytoInsertDate = QueryManager.insertGameDates() + "'" + d1
				+ "',sale_end_date='" + d2 + "',pwt_end_date='" + d3
				+ "',add_inv_status='F'  where game_id=" + gameId + "";
		// String querytoInsertDate="update st_se_game_master set
		// start_date='"+d1+"',sale_end_date='"+d2+"',pwt_end_date='"+d3+"',add_inv_status='F'
		// where game_id="+gameId+"";
		// System.out.println("Query ----" +querytoInsertDate );
		//  
		// connection = DBConnect.getConnection();
		// System.out.println("virn bean list is "+ vcbeanList);
		VirnCodeBean vcBean = null;
		Iterator<VirnCodeBean> iterator = vcbeanList.iterator();
		try {
			connection.setAutoCommit(false);

			stmt = connection.prepareStatement(query);
			stmt1 = connection.createStatement();
			while (iterator.hasNext()) {
				vcBean = iterator.next();
				// System.out.println("Vc Bean is :"+vcBean);
				// System.out.println("vitn is " + vcBean.getVirn_code());
				// System.out.println("prize level is "+vcBean.getPrize_level()
				// );
				stmt.setString(1, MD5Encoder.encode(vcBean.getVirn_code()));
				// System.out.println("to insert ************" +
				// MD5Encoder.encode(vcBean.getVirn_code()));
				stmt.setInt(2, vcBean.getGame_id());
				stmt.setDouble(3, vcBean.getPwt_amt());
				stmt.setString(4, vcBean.getPrize_level());
				stmt.setString(5, vcBean.getStatus());
				stmt.executeUpdate();
				// System.out.println("55555555555555555555555");
			}

			stmt1.executeUpdate(querytoInsertDate); // by yogesh

			System.out.println("arun time " + Calendar.getInstance().getTime());
			// by arun & yogesh to run code temporary
			// String noOfPrizeRemain="update st_se_rank_master b, (select
			// aa.game_id, aa.pwt_amt, count(aa.pwt_amt) 'total_no_of_prize',
			// count(aa.pwt_amt) 'no_of_prize_rem' from st_se_pwt_inv aa where
			// (aa.status='UNCLM_PWT' or aa.status ='UNCLM_CANCELLED') and
			// aa.game_id="+gameId+" group by aa.game_id, aa.pwt_amt)a set
			// b.no_of_prize_rem = a.no_of_prize_rem where a.game_id = b.game_id
			// and a.pwt_amt = b.prize_amt and a.game_id="+gameId;
			String totalPrize = "update st_se_rank_master b,(select aa.game_id, aa.pwt_amt, count(aa.pwt_amt) 'total_no_of_prize' from st_se_pwt_inv aa  where aa.game_id="
					+ gameId
					+ " group by aa.game_id, aa.pwt_amt)a set b.total_no_of_prize = a.total_no_of_prize where a.game_id = b.game_id and a.pwt_amt = b.prize_amt and  a.game_id="
					+ gameId;

			Statement updaterankMaster = connection.createStatement();
			// updaterankMaster.executeUpdate(noOfPrizeRemain);
			updaterankMaster.executeUpdate(totalPrize);
			System.out.println("arun time after "
					+ Calendar.getInstance().getTime());

			connection.commit();
			System.out.println("heeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeee");
		} catch (SQLException e) {
			try {
				connection.rollback();
			} catch (SQLException e1) {

				e1.printStackTrace();
				throw new LMSException("Exception During Rollback", e1);
			}
			e.printStackTrace();
			throw new LMSException(e);

		} catch (Exception e) {
			throw new LMSException("Please Upload Correct File");

		} finally {
			try {
				if (stmt != null) {
					stmt.close();
				}
				if (connection != null) {
					connection.close();
				}
			} catch (SQLException e2) {
				e2.printStackTrace();
				throw new LMSException("Exception During Close Statement", e2);
			}
		}
	}

	/**
	 * This method in helper class is used to insert packs and books for a game
	 * into database
	 * 
	 * @param seriesList
	 * @param gameName
	 * @param supplierName
	 * @throws LMSException
	 */
	public void insertTicketsInDB(List<PackNumberSeriesBean> seriesList,
			String gameName, int gameNbr, int supplierId, int userOrgId,
			int userId, int warehouseId) throws LMSException {
		System.out
				.println("Entered In to Helper class for ticket generate and save to db");
		Connection conn = DBConnect.getConnection();

		try {
			conn.setAutoCommit(false);
			// Getting Game Details and game ticket format
			// st_se_game_ticket_nbr_format using game name and game nbr
			String getGameDetailQuery = "select * from st_se_game_master aa, st_se_game_ticket_nbr_format bb "
					+ " where bb.game_id = aa.game_id and aa.game_name = ? and aa.game_nbr = ?";
			PreparedStatement pstmt = conn.prepareStatement(getGameDetailQuery);
			pstmt.setString(1, gameName);
			pstmt.setInt(2, gameNbr);
			ResultSet resultSet = pstmt.executeQuery();

			if (resultSet.next()) {
				int game_id = resultSet.getInt(TableConstants.GAME_ID);
				int game_nbr = resultSet.getInt(TableConstants.GAME_NBR);
				int BookPerPack = resultSet
						.getInt(TableConstants.BOOKS_PER_PACK);
				int ticketsPerBook = resultSet
						.getInt(TableConstants.TICKETS_PER_BOOK);
				double ticketPrice = resultSet
						.getDouble(TableConstants.TICKET_PRICE);
				int finalBookDigit = resultSet
						.getInt(TableConstants.BOOK_NBR_DIGITS);
				int finalPackDigit = resultSet
						.getInt(TableConstants.PACK_NBR_DIGITS);

				// System.out.println("game_id " + game_id +" "+"game_nbr :" +
				// game_nbr);
				// System.out.println("nbr_of_books_per_pack :" + BookPerPack+"
				// , nbr_of_tickets_per_book " + ticketsPerBook+" , "+"
				// ticket_price: "+ ticketPrice);
				// System.out.println("finalBookDigit " + finalBookDigit +" ,
				// finalPackDigit " + finalPackDigit);

				// insert into LMS transaction master
				String insertLMSTransMas = QueryManager
						.insertInLMSTransactionMaster();
				PreparedStatement LMStmstmt = conn
						.prepareStatement(insertLMSTransMas);
				LMStmstmt.setString(1, "BO");
				LMStmstmt.executeUpdate();
				ResultSet tmKeys = LMStmstmt.getGeneratedKeys();

				// get the auto generated key from transaction_master of last
				// entry
				if (tmKeys.next()) {
					int transactionId = tmKeys.getInt(1);

					// insert details into transaction_master table
					String insertIntoTransactionMaster = QueryManager
							.insertInBOTransactionMaster();
					PreparedStatement tmstmt = conn
							.prepareStatement(insertIntoTransactionMaster);

					tmstmt.setInt(1, transactionId);
					tmstmt.setInt(2, userId);
					tmstmt.setInt(3, userOrgId);
					tmstmt.setString(4, "SUPPLIER");
					tmstmt.setInt(5, supplierId);
					tmstmt.setTimestamp(6, new java.sql.Timestamp(
							new java.util.Date().getTime()));
					tmstmt.setString(7, "PURCHASE");

					/*
					 * tmstmt.setString(1, "SUPPLIER"); tmstmt.setInt(2,
					 * supplierId); tmstmt.setTimestamp(3, new
					 * java.sql.Timestamp(new java.util.Date().getTime()));
					 * tmstmt.setString(4, "PURCHASE");
					 */
					tmstmt.executeUpdate();
					System.out
							.println("insertion into st_lms_bo_transaction_master query is == "
									+ tmstmt);

					System.out.println("Serieslist is : " + seriesList);

					int totalPack = 0, totalBooksInAllSeries = 0, totalPackInAllseries = 0;
					// iterate the pack series bean from list
					for (PackNumberSeriesBean seriesBean : seriesList) {
						String[] packTo = seriesBean.getPackNumberTo().split(
								"-");
						String[] packFrom = seriesBean.getPackNumberFrom()
								.split("-");
						totalPack = Integer.parseInt(packTo[1])
								- Integer.parseInt(packFrom[1]) + 1;
						totalPackInAllseries = totalPackInAllseries + totalPack;
						// System.out.println("packNbrFrom :
						// "+Integer.parseInt(packFrom[1]) +" , packnbrTo :
						// "+Integer.parseInt(packTo[1])
						// +" total pack = "+totalPack + " totalPackInAllseries
						// = "+totalPackInAllseries);
						// generate tickets number from uploaded packs
						GenerateTicketsNumber ticketsNumber = new GenerateTicketsNumber(
								totalPack, BookPerPack, game_nbr,
								finalPackDigit, finalBookDigit, Integer
										.parseInt(packFrom[1]), Integer
										.parseInt(packTo[1]));
						// update game inventory status table
						ticketsNumber.updateGameInvStatusTable(game_id, conn,
								userOrgId, warehouseId);
						// update game inventory details table
						ticketsNumber.updateGameInvDetailTable(game_id,
								transactionId, conn, userOrgId, userId, warehouseId);
					}

					totalBooksInAllSeries = totalPackInAllseries * BookPerPack;
					int totalTickets = totalBooksInAllSeries * ticketsPerBook;
					double amount = ticketPrice * totalTickets;
					System.out.println("total pack in series "
							+ totalPackInAllseries + " , total books : "
							+ totalBooksInAllSeries + " \n Total Tickets : "
							+ totalTickets + " ,  Total Amount : " + amount);

					// insert uploaded ticket inventory details into
					// st_se_supplier_bo_transaction table
					String insertIntoSupplierBOTrans = QueryManager
							.getST5SupplierTransQuery();
					PreparedStatement supplierBOTransStmt = conn
							.prepareStatement(insertIntoSupplierBOTrans);
					supplierBOTransStmt.setInt(1, transactionId);
					supplierBOTransStmt.setInt(2, game_id);
					supplierBOTransStmt.setInt(3, supplierId);
					supplierBOTransStmt.setInt(4, totalBooksInAllSeries);
					supplierBOTransStmt.setDouble(5, amount);
					int SBTRowsUpdate = supplierBOTransStmt.executeUpdate();
					System.out
							.println("rows updates : "
									+ SBTRowsUpdate
									+ "  , insert into st_se_supplier_bo_transaction : "
									+ supplierBOTransStmt);

					String updateGameMaster = "update st_se_game_master set nbr_of_tickets=ifnull(nbr_of_tickets,0)+? ,nbr_of_books=ifnull(nbr_of_books,0)+? ,add_inv_status = 'T' where game_id = ?";
					PreparedStatement updateGameMasterPstmt = conn
							.prepareStatement(updateGameMaster);
					updateGameMasterPstmt.setInt(1, totalTickets);
					updateGameMasterPstmt.setInt(2, totalBooksInAllSeries);
					updateGameMasterPstmt.setInt(3, game_id);
					updateGameMasterPstmt.executeUpdate();

					conn.commit();

				} else {
					System.out
							.println("auto generated transaction_id of st_lms_transaction_master table is not generated.");
					throw new LMSException("Problem in Ticket Inventory upload");
				}
			} else {
				System.out.println("There is no game details found.");
				throw new LMSException("Problem in Ticket Inventory upload");
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new LMSException(e);
		} finally {
			try {
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		}
	}

	private void insertVirnPWTTicketInvDetailsIntoDataBase(Connection con,
			ArrayList<VirnCodeBean> virnBeanList, Date stDate,
			Date saleEndDate, Date pwtEndDate, int gameId, int gameNbr)
			throws LMSException, SQLException {
		Connection connection = con;
		String query = "insert into st_se_pwt_inv_"
				+ gameNbr
				+ " (virn_code, game_id, pwt_amt, prize_level, id1, id2, status) values ";
		StringBuilder appendQuery = new StringBuilder("");
		VirnCodeBean virnBean = null;

		connection.setAutoCommit(false);
		Statement stmt2 = connection.createStatement();

		// insert ticket details into database
		int fstLength = virnBeanList.size();
		for (int i = 0; i < fstLength; i += 30000) {
			virnBean = virnBeanList.get(i);
			appendQuery.ensureCapacity(fstLength);
			appendQuery.append(query).append("('").append(
					virnBean.getVirn_code());
			appendQuery.append("', ").append(gameId).append(", ");
			appendQuery.append(virnBean.getPwt_amt()).append(", '");
			appendQuery.append(virnBean.getPrize_level()).append("', '");
			appendQuery.append(virnBean.getId1()).append("', '");
			appendQuery.append(virnBean.getId2()).append(
					"', '" + virnBean.getStatus() + "')");
			int secLength = i + 1 + 30000;
			for (int k = i + 1; k < fstLength && k < secLength; k++) {
				virnBean = virnBeanList.get(k);
				appendQuery.append(",('").append(virnBean.getVirn_code());
				appendQuery.append("', ").append(gameId).append(", ");
				appendQuery.append(virnBean.getPwt_amt()).append(", '");
				appendQuery.append(virnBean.getPrize_level()).append("', '");
				appendQuery.append(virnBean.getId1()).append("', '");
				appendQuery.append(virnBean.getId2()).append(
						"', '" + virnBean.getStatus() + "')");
			}
			i = i + 1;
			stmt2.execute(appendQuery.toString());
			appendQuery = new StringBuilder("");
		}

		// update the game details
		Statement stmt1 = connection.createStatement();
		String querytoInsertDate = QueryManager.insertGameDates() + "'"
				+ stDate + "',sale_end_date='" + saleEndDate
				+ "',pwt_end_date='" + pwtEndDate
				+ "',add_inv_status='F'  where game_id=" + gameId + "";
		stmt1.executeUpdate(querytoInsertDate);

		// update the rank master table for 'noOfPrizeRemain and totalPrize'

		// String noOfPrizeRemain = "update st_se_rank_master b, (select
		// aa.game_id, aa.pwt_amt, count(aa.pwt_amt) " +
		// " 'total_no_of_prize', count(aa.pwt_amt) 'no_of_prize_rem' from
		// st_se_pwt_inv_"+gameNbr
		// +" aa where (aa.status='UNCLM_PWT' or aa.status ='UNCLM_CANCELLED')
		// and aa.game_id="
		// + gameId + " group by aa.game_id, aa.pwt_amt)a set b.no_of_prize_rem
		// = a.no_of_prize_rem where a.game_id = b.game_id and a.pwt_amt =
		// b.prize_amt and a.game_id="+ gameId;
		// updaterankMaster.executeUpdate(noOfPrizeRemain);
		Statement updateRankMaster = connection.createStatement();
		String totalPrize = "update st_se_rank_master b,(select aa.game_id, aa.pwt_amt, count(aa.pwt_amt) 'total_no_of_prize' from st_se_pwt_inv_"
				+ gameNbr
				+ " aa  where aa.game_id="
				+ gameId
				+ " group by aa.game_id, aa.pwt_amt)a set b.total_no_of_prize = a.total_no_of_prize where a.game_id = b.game_id and a.pwt_amt = b.prize_amt and  a.game_id="
				+ gameId;
		updateRankMaster.executeUpdate(totalPrize);

		connection.commit();

	}

	/**
	 * This method inside helper class is used to read the rank file
	 * 
	 * @param gameId
	 * @param Rankupload
	 * @return List of RankDetailBean type
	 * @throws Exception
	 */

	public List<RankDetailBean> readRank(int gameId, String Rankupload)
			throws Exception {
		List<RankDetailBean> list = new ArrayList<RankDetailBean>();
		File file = new File(Rankupload);
		FileReader fstream = new FileReader(file);
		BufferedReader br = new BufferedReader(fstream);
		String strLine;
		RankDetailBean rank = null;

		System.out.println("game id is " + gameId);

		String a = ""; // Rank1=Rank
		String b = ""; // 1|30|0|H|N
		String c = ""; // 30
		String d = ""; // H
		String e = ""; // 10N
		String f = ""; // 1
		String noOfPrize = "";
		String status = null;
		StringTokenizer st = null;
		StringTokenizer st1 = null;
		int rank_id = 0;
		double prize_amount = 0;
		while ((strLine = br.readLine()) != null) {
			System.out.println("vvv----- " + strLine);
			rank = new RankDetailBean();
			rank.setGameId(gameId);
			a = ""; // Rank1=Rank
			b = ""; // 1|30|0|H|N
			c = ""; // 30
			d = ""; // H
			e = ""; // 10N
			f = ""; // 1
			noOfPrize = "";
			rank_id = 0;
			prize_amount = 0;
			status = null;
			st = new StringTokenizer(strLine);
			for (int i = 0; i < 3; i++) {
				if (st.hasMoreTokens()) {
					if (i < 2) {
						a = a + st.nextToken();
					} else {
						b = b + st.nextToken();
					}

				}
				System.out.println("first token  " + a);
				System.out.println("second token " + b);
			}
			st1 = new StringTokenizer(b, "|");
			for (int i = 0; i < 5; i++) {
				if (st1.hasMoreTokens()) {
					if (i == 0) {
						f = f + st1.nextToken();
						rank_id = Integer.parseInt(f);
						rank.setRank(rank_id);
					} else if (i == 1) {
						c = c + st1.nextToken();
						prize_amount = Double.parseDouble(c);
						rank.setPrize_amount(prize_amount);
					} else if (i == 3) {
						d = d + st1.nextToken();
						if (d.equals("H")) {
							status = "HIGH";
						} else if (d.equals("L")) {
							status = "LOW";
						} else {
							status = "";
						}
						rank.setStatus(status);
					}
					// here done the code to fetch the no_of_prize_list from
					// rank file
					// if 'get_no_of_prize_from=RANK' in property file
					else if ("RANK".equalsIgnoreCase(GameUtilityHelper
							.getNoOfPrizeFromValue())
							&& i == 4) {
						noOfPrize = noOfPrize + st1.nextToken();
						long noOfPrizeLong = Long.parseLong(noOfPrize);
						System.out.println("no of prize === " + noOfPrizeLong);
						rank.setNoOfPrize(noOfPrizeLong);
					} else {
						e = e + st1.nextToken();
					}
				}
			}
			list.add(rank);
		}
		// Close the input stream
		br.close();
		return list;

	}

	/**
	 * This method is used to read rank details from database for a game
	 * 
	 * @param gameName
	 *            is game name
	 * @return List of RankDetailBean type
	 * @throws LMSException
	 */
	public List<RankDetailBean> readRank(String gameName) throws LMSException {

		 
		Connection connection;
		connection = DBConnect.getConnection();
		try {

			List<RankDetailBean> list = new ArrayList<RankDetailBean>();
			;

			Statement stmt1 = connection.createStatement();
			Statement stmt2 = connection.createStatement();
			int gameId = 0;
			System.out.println("name is " + gameName);
			// write this query in helper class
			ResultSet gameDetails = stmt2.executeQuery(QueryManager.getGameId()
					+ "'" + gameName + "'");

			// ResultSet gameDetails=stmt2.executeQuery("select game_id from
			// st_se_game_master where game_name='"+gameName+"'");
			while (gameDetails.next()) {
				gameId = gameDetails.getInt("game_id");
			}
			// write this query into helper class
			ResultSet rs = stmt1.executeQuery(QueryManager
					.getDetailsfromGameMaster()
					+ gameId + "");
			// ResultSet rs=stmt1.executeQuery("select * from st_se_rank_master
			// where game_id="+gameId+"");
			// System.out.println("rs is "+ rs);
			RankDetailBean rank = null;
			while (rs.next()) {
				rank = new RankDetailBean();
				rank.setGameId(gameId);
				rank.setPrize_amount(rs.getDouble(TableConstants.PRIZE_AMT));
				// System.out.println(rs.getDouble(TableConstants.PRIZE_AMT));
				rank.setRank(rs.getInt(TableConstants.RANK_NBR));
				// System.out.println(rs.getInt(TableConstants.RANK_NBR));
				rank.setStatus(rs.getString(TableConstants.PRIZE_LEVEL));
				// System.out.println(rs.getString(TableConstants.PRIZE_LEVEL));
				list.add(rank);
			}
			return list;
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
			}
		}

	}

	private Map<Integer, RankDetailBean> readRankForUploadedVirn(
			Connection con, int gameNbr, String gameName) throws LMSException {
		System.out.println("name is " + gameNbr + "-" + gameName);
		RankDetailBean rank = null;
		Map<Integer, RankDetailBean> rankMapBean = new TreeMap<Integer, RankDetailBean>();
		Connection connection = con;
		try {
			PreparedStatement pstmt = connection
					.prepareStatement("select game_id, rank_nbr, prize_amt, prize_amt, prize_level, total_no_of_prize, no_of_prize_claim, no_of_prize_cancel from st_se_rank_master where game_id = (select game_id from st_se_game_master where game_name=? and game_nbr = ?)");
			pstmt.setString(1, gameName);
			pstmt.setInt(2, gameNbr);
			ResultSet rs = pstmt.executeQuery();
			System.out.println(pstmt);
			while (rs.next()) {
				rank = new RankDetailBean();
				rank.setGameId(rs.getInt(TableConstants.GAME_ID));
				rank.setPrize_amount(rs.getDouble(TableConstants.PRIZE_AMT));
				rank.setRank(rs.getInt(TableConstants.RANK_NBR));
				rank.setStatus(rs.getString(TableConstants.PRIZE_LEVEL));
				rankMapBean.put(rank.getRank(), rank);
			}
			return rankMapBean;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new LMSException(e);

		}

	}

	/**
	 * This method is used to start new games
	 * 
	 * @param gameId
	 * @return void
	 */

	public void startGame(int gameId) {

		 
		Connection connection;
		connection = DBConnect.getConnection();
		try {
			Statement stmt = connection.createStatement();
			stmt
					.executeUpdate("update st_se_game_master set game_status='OPEN' where game_id="
							+ gameId + "");
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (connection != null) {
					connection.close();
				}

			} catch (SQLException se) {
				se.printStackTrace();
			}
		}

	}

	/*
	 * public static void main(String[] args) { //new
	 * GameuploadHelper().uploadVirnFile(gameName, details, startDate,
	 * saleendDate, pwtendDate); try { new
	 * GameuploadHelper().uploadVirnFile(103, "abcdef",
	 * "D:\\desktop\\VirnFiles\\PartyTime\\VirnFiles\\VIRN_PartyTime4.txt",
	 * "2009-03-26", "2009-03-27", "2009-03-28"); } catch (Exception e) {
	 * 
	 * e.printStackTrace(); } }
	 */

	/**
	 * This method inside helper class is used to insert game basic details into
	 * database
	 * 
	 * @param govt_comm_type
	 * @param priceperTicket
	 * @param gameNumber
	 * @param gameName
	 * @param ticketpetBook
	 * @param booksperPack
	 * @param agentSaleCommRate
	 * @param agentPWTCommRate
	 * @param retailerSaleCommRate
	 * @param retailerPWTCommRate
	 * @param govtCommRate
	 * @param minAssProfit
	 * @param digitsofPack
	 * @param digitsofBook
	 * @param digitsofTicket
	 * @param Rankupload
	 * @return String
	 * @throws LMSException
	 */
	public String uploadBasicDetails(String govt_comm_type,
			double priceperTicket, int gameNumber, String gameName,
			int ticketpetBook, int booksperPack, double agentSaleCommRate,
			double agentPWTCommRate, double retailerSaleCommRate,
			double retailerPWTCommRate, double govtCommRate,
			double minAssProfit, int digitsofPack, int digitsofBook,
			int digitsofTicket, String Rankupload, double prizePayOutRatio,
			double vatPercentage, long ticketsInScheme, int gameVirnDigits, String defaultInvoiceMethod)
			throws LMSException {

		Connection conn = DBConnect.getConnection();

		try {
			conn.setAutoCommit(false);
			PreparedStatement pstmt = null;

			// check that game already
			String query = QueryManager.getST3GamesDetails();
			Statement st = conn.createStatement();
			System.out.println(query + " where game_nbr=" + gameNumber
					+ " or game_name='" + gameName + "'");
			ResultSet rsGame = st.executeQuery(query + " where game_nbr="
					+ gameNumber + " or game_name='" + gameName + "'");
			if (rsGame.next()) {
				// This Game has been already loaded
				return "ERROR";
			}

			int defaultSchemaId = getSchemeId(conn, defaultInvoiceMethod);
			// insert game details into st_se_game_master table
			String insertIntoGameMaster = QueryManager.getST5GameMasterQuery();
			PreparedStatement preState = conn
					.prepareStatement(insertIntoGameMaster);
			preState.setInt(1, gameNumber);
			preState.setString(2, gameName);
			preState.setDouble(3, priceperTicket);
			preState.setInt(4, ticketpetBook);
			preState.setInt(5, booksperPack);
			preState.setDouble(6, agentSaleCommRate);
			preState.setDouble(7, agentPWTCommRate);
			preState.setDouble(8, retailerSaleCommRate);
			preState.setDouble(9, retailerPWTCommRate);
			preState.setDouble(10, govtCommRate);
			preState.setString(11, "NEW");
			preState.setString(12, govt_comm_type);
			preState.setDouble(13, minAssProfit);
			preState.setString(14, "F");
			preState.setDouble(15, prizePayOutRatio);
			preState.setDouble(16, vatPercentage);
			preState.setLong(17, ticketsInScheme);
			preState.setInt(18, defaultSchemaId);
			preState.executeUpdate();
			System.out
					.println("insert into st_se_game_master query : " + pstmt);
			ResultSet rs = preState.getGeneratedKeys();
			System.out.println("Resulset from st_se_game_master" + rs);
			int gameId = -1;

			if (rs.next()) {

				gameId = rs.getInt(1);
				System.out.println("generated game id is " + gameId);

				// read rank file
				List<RankDetailBean> list = readRank(gameId, Rankupload);
				System.out.println("rank file list =  " + list);

				Iterator<RankDetailBean> iterator = list.iterator();
				int rankNo;
				int maxRank = 0;
				String prizeLevel;
				double prizeAmount;

				// added by arun
				long totalNoOfPrize = 0, noOfPrizeClaimed = 0, noOfPrizeCancel = 0;

				while (iterator.hasNext()) {
					RankDetailBean rankBean = iterator.next();
					gameId = rankBean.getGameId();
					System.out.println("game id now " + gameId);

					rankNo = rankBean.getRank();
					if (rankNo > maxRank) {
						maxRank = rankNo;
					}

					prizeAmount = rankBean.getPrize_amount();
					prizeLevel = rankBean.getStatus();

					// updated by arun when winning prize remaining process
					if ("RANK".equalsIgnoreCase(GameUtilityHelper
							.getNoOfPrizeFromValue())) {
						totalNoOfPrize = rankBean.getNoOfPrize();
					} else {
						totalNoOfPrize = 0;
					}

					// insert game rank details into st_se_rank_master
					pstmt = conn.prepareStatement(QueryManager
							.insertintoRankMaster());
					pstmt.setInt(1, gameId);
					pstmt.setInt(2, rankNo);
					pstmt.setDouble(3, prizeAmount);
					pstmt.setString(4, prizeLevel);
					pstmt.setLong(5, totalNoOfPrize);
					pstmt.setLong(6, noOfPrizeClaimed);
					pstmt.setLong(7, noOfPrizeCancel);
					pstmt.executeUpdate();
					System.out.println("insertIntoRankMaster table qury "
							+ pstmt);
				}

				// insert game ticket details into st_se_game_ticket_nbr_format
				// table
				String insertTicketFormat = QueryManager.insertTicketFormat()
						+ "(" + gameId + "," + digitsofPack + ","
						+ digitsofBook + "," + digitsofTicket + ","
						+ String.valueOf(gameNumber).length() + ","
						+ String.valueOf(maxRank).length() + ","
						+ gameVirnDigits + ")";

				Statement stmt = conn.createStatement();
				stmt.executeUpdate(insertTicketFormat);
				System.out.println("Query to insert for ticket number format "
						+ stmt);

				// putting the entry into gov_comm_rate_history table
				PreparedStatement preStatement = conn
						.prepareStatement("insert into st_se_game_govt_comm_history values (?, ?, ?)");
				preStatement.setInt(1, gameId);
				preStatement.setDouble(2, govtCommRate);
				preStatement.setTimestamp(3, new Timestamp(new java.util.Date()
						.getTime()));
				int i = preStatement.executeUpdate();
				System.out.println(preStatement + "\ntotal rows updates == "
						+ i);

				// create st_pwt_table_? for game wise
				/*String createPwtInvTable = "CREATE TABLE  st_se_pwt_inv_"
						+ gameNumber
						+ "  ("
						+ " virn_code  varchar(24) unique NOT NULL,"
						+ " id1 varchar(24) NOT NULL default '',"
						+ " id2 varchar(24) default '', "
						+ " game_id  int(10) unsigned NOT NULL,"
						+ " pwt_amt  decimal(20,2) NOT NULL,"
						+ " prize_level  varchar(10) NOT NULL default '',"
						+ " status  varchar(25) NOT NULL default '',"
						+ " PRIMARY KEY  ( virn_code , game_id )"
						+ " ) ENGINE=InnoDB DEFAULT CHARSET=utf8 CHECKSUM=1 DELAY_KEY_WRITE=1 ROW_FORMAT=DYNAMIC";*/
				
				String createPwtInvTable = "CREATE TABLE `st_se_pwt_inv_"
						+ gameNumber
						+ "` (`virn_code` varchar(24) NOT NULL,`id1` varchar(24) NOT NULL default '',`id2` varchar(24) default '',`game_id` int(10) unsigned NOT NULL,`pwt_amt` decimal(20,2) NOT NULL,`prize_level` varchar(10) NOT NULL default '',`status` varchar(25) NOT NULL default '', ticket_status enum('ACTIVE','MISSING','INACTIVE','SOLD') DEFAULT 'INACTIVE', PRIMARY KEY  (`virn_code`,`id1`,`game_id`)) ENGINE=InnoDB DEFAULT CHARSET=utf8 CHECKSUM=1 DELAY_KEY_WRITE=1 ROW_FORMAT=DYNAMIC";
				
				Statement pwtStmt = conn.createStatement();
				pwtStmt.executeUpdate(createPwtInvTable);
				System.out.println("Query to createPwtInvTable " + pwtStmt);

				Statement tickStmt = conn.createStatement();
				String ticketPwtInvTable = "CREATE TABLE  st_se_pwt_tickets_inv_"
						+ gameNumber
						+ "  ( "
						+ " ticket_nbr  varchar(20) unique NOT NULL, "
						+ " game_id  int(10) unsigned NOT NULL, "
						+ " book_nbr  varchar(15) NOT NULL, "
						+ " status  varchar(25) NOT NULL default '', "
						+ " verify_by_user  int(10) unsigned NOT NULL, "
						+ " verify_by_org  int(10) unsigned NOT NULL, "
						+ " channel  varchar(30) default NULL, "
						+ " interface  varchar(30) default NULL, "
						+ " party_org_id int(10) default NULL,   "
						+ " date datetime NOT NULL, "
						+ " PRIMARY KEY  ( ticket_nbr , game_id ) "
						+ " ) ENGINE=InnoDB DEFAULT CHARSET=utf8";
				tickStmt.executeUpdate(ticketPwtInvTable);
				System.out.println("Query to createPwtInvTable " + tickStmt);

				stmt = conn.createStatement();
				query = "INSERT INTO st_se_org_game_invoice_methods (organization_id, game_id, invoice_method_id, invoice_method_value) SELECT organization_id, '"+gameId+"', "+defaultSchemaId+", (SELECT scheme_value FROM st_se_invoicing_methods WHERE invoice_method_id="+defaultSchemaId+") FROM st_lms_organization_master WHERE organization_type IN ('AGENT', 'RETAILER');";
				logger.info("Insert In st_se_org_game_invoice_methods - "+query);
				stmt.executeUpdate(query);

				conn.commit();
				return "success";
			} else {
				return "ERROR";
			}
		} catch (Exception e) {
			try {
				conn.rollback();
			} catch (SQLException e1) {
				System.out.println("Error In rollback");
				e1.printStackTrace();
			}
			System.out.println("error" + e.toString());
			e.printStackTrace();
			throw new LMSException(e);
		} finally {
			DBConnect.closeCon(conn);
			
			//	Licensing Server Validation
			//	LSControllerImpl.getInstance().clientValidation();
		}

	}

	private int getSchemeId(Connection conn, String defaultInvoiceMethod) {
		
		Statement stmt =  null ;
		ResultSet rs = null ;
		int invoicingId = 0 ;
		try{
			stmt = conn.createStatement() ;
			
			String query = "select invoice_method_id from st_se_invoicing_methods where scheme_type = '" + defaultInvoiceMethod + "' limit 1;" ;
			
			rs = stmt.executeQuery(query) ;
			
			logger.info("Query Statement : " + stmt) ;
			
			if(rs.next())
				invoicingId = rs.getInt("invoice_method_id") ;
			
		}catch(Exception e){
			e.printStackTrace() ;
		}finally{
			DBConnect.closeResource(rs, stmt) ;
		}
		
		return invoicingId ;
	}

	public String uploadVirnFile(int gameNbr, String gameName, String details,
			String startDate, String saleendDate, String pwtendDate,
			String encSchemeType) throws LMSException {
		Connection con = DBConnect.getConnection();
		try {

			// get the game details from database
			GameTicketFormatBean ticketFmtBean = getGameDetails(con, gameNbr,
					gameName);
			int gameNbrDigits = ticketFmtBean.getGameNbrDigits();
			int maxRankDigits = ticketFmtBean.getMaxRankDigits();
			int game_id = ticketFmtBean.getGameId();
			if (gameNbrDigits == 0 || game_id == 0 || maxRankDigits == 0) {
				return "error";
			}

			// get rank file details from database
			Map<Integer, RankDetailBean> rankDetailMap = readRankForUploadedVirn(
					con, gameNbr, gameName);

			// VIRN file read from stream
			InputStreamReader fileStreamReader = new InputStreamReader(
					new FileInputStream(details));
			BufferedReader br = new BufferedReader(fileStreamReader);

			StringBuilder encVirnStrBuilder = new StringBuilder("");

			ArrayList<VirnCodeBean> virnBeanList = null;
			if ("1W_ENC_OF_ALL".equalsIgnoreCase(encSchemeType.trim())) {// 1W
				// Encryption
				// of
				// virn,
				// ticket
				// and
				// code
				virnBeanList = create1WEncVirnBeanListWithFixedCode(br,
						encVirnStrBuilder, gameNbrDigits, maxRankDigits,
						game_id, rankDetailMap);
			} else if ("2W_ENC_OF_TKT".equalsIgnoreCase(encSchemeType.trim())) { // 2W
				// Encryption
				// of
				// ticket(key
				// is
				// virn+code)
				// and
				// 1W
				// encryption
				// of
				// virn
				// &
				// code
				virnBeanList = create2WEncVirnBeanListWithFixedCode(br,
						encVirnStrBuilder, gameNbrDigits, maxRankDigits,
						game_id, rankDetailMap);
			} else if ("2W_ENC_OF_TKT_VIRN".equalsIgnoreCase(encSchemeType
					.trim())) { // 2W Encryption of virn & ticket(that are
				// already 1W Encrypted ) using code(1W)
				virnBeanList = create2WEncVirnBeanList(br, encVirnStrBuilder,
						gameNbrDigits, maxRankDigits, game_id, rankDetailMap);
			}

			// check virn_code list for duplicate into database
			String encVirnListString = encVirnStrBuilder.deleteCharAt(
					encVirnStrBuilder.length() - 1).toString();
			boolean virnFileStatusCheckFlag = fileStatusCheck(
					encVirnListString, game_id, gameNbr + "");
			if (!virnFileStatusCheckFlag) {
				return "error";
			}

			// date formatted in the MySQL form
			DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			dateFormat.setCalendar(Calendar.getInstance());
			java.sql.Date stDate = new java.sql.Date(dateFormat
					.parse(startDate).getTime());
			java.sql.Date saleEndDate = new java.sql.Date(dateFormat.parse(
					saleendDate).getTime());
			java.sql.Date pwtEndDate = new java.sql.Date(dateFormat.parse(
					pwtendDate).getTime());

			// insert data into st_se_pwt_inv table database
			insertVirnPWTTicketInvDetailsIntoDataBase(con, virnBeanList,
					stDate, saleEndDate, pwtEndDate, game_id, gameNbr);
			return "success";

		} catch (NumberFormatException e) {
			e.printStackTrace();
			throw new LMSException();
		} catch (IOException e) {
			e.printStackTrace();
			throw new LMSException();
		} catch (LMSException e) {
			e.printStackTrace();
			throw new LMSException();
		} catch (ParseException e) {
			e.printStackTrace();
			throw new LMSException();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new LMSException();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			throw new LMSException();
		} catch (NoSuchPaddingException e) {
			e.printStackTrace();
			throw new LMSException();
		} catch (EncryptionException e) {
			e.printStackTrace();
			throw new LMSException();
		} finally {
			try {
				if (con != null) {
					con.close();
				}
			} catch (SQLException e2) {
				e2.printStackTrace();
				throw new LMSException("Exception During Close Statement", e2);
			}
		}
	}

	
	
	
	public static Map<Integer, String> getAllWarehouses(int orgId, String action)
	{
		Map<Integer, String> warehouseNames = new HashMap<Integer, String>();
		Statement st = null ;
		ResultSet rs = null ;
		Connection conn = null ;
		try{
			conn = DBConnect.getConnection() ;
			st = conn.createStatement() ;
			String getWarehouse = "select warehouse_id, warehouse_name from st_se_warehouse_master "+ ("FROM".equalsIgnoreCase(action) ? (" where warehouse_owner_id = " + orgId) : (" where warehouse_owner_id != " + orgId)) ;
			
			rs = st.executeQuery(getWarehouse) ;
			
			while(rs.next())
				warehouseNames.put(rs.getInt("warehouse_id"), rs.getString("warehouse_name")) ;
			
			
		}catch(Exception e){
			e.printStackTrace() ;
		}
		
		
		return warehouseNames ;
	}

}
