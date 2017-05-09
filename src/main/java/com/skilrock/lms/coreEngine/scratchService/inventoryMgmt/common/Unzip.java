package com.skilrock.lms.coreEngine.scratchService.inventoryMgmt.common;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import sun.misc.BASE64Encoder;

import com.skilrock.lms.beans.GameTicketFormatBean;
import com.skilrock.lms.common.db.DBConnect;
import com.skilrock.lms.common.db.QueryManager;
import com.skilrock.lms.common.exception.LMSException;

public class Unzip {
	static Log logger = LogFactory.getLog(Unzip.class);

	public static final void copyInputStream(InputStream in, OutputStream out)
			throws IOException {
		byte[] buffer = new byte[1024];
		int len;

		while ((len = in.read(buffer)) >= 0) {
			out.write(buffer, 0, len);
		}

		in.close();
		out.close();
	}

	public static boolean insertVirnInDB(int gameNo) {
		boolean complete = false;
		logger.debug(new Date() + "*****Start******" + (new Date()).getTime());
		try {
			Connection conn = DBConnect.getConnection();
			conn.setAutoCommit(false);
			Statement stmt = conn.createStatement();
			String rankQuery = "select rm.game_id,rm.rank_nbr,rm.prize_amt,rm.prize_level,if(rm.prize_amt=0,'NO_PRIZE_PWT','UNCLM_PWT') as status from st_se_rank_master rm,st_se_game_master gm where rm.game_id=gm.game_id and gm.game_nbr="
					+ gameNo;
			ResultSet rs = stmt.executeQuery(rankQuery);
			Map<Integer, String> rankPrizeMap = new HashMap<Integer, String>();
			while (rs.next()) {
				rankPrizeMap.put(rs.getInt("rank_nbr"), rs.getString("game_id")
						+ "," + rs.getString("prize_amt") + ",'"
						+ rs.getString("prize_level") + "','"
						+ rs.getString("status") + "'");
			}
			logger.debug("*********rankPrizeMap*::" + rankPrizeMap);
			Unzip unzip = new Unzip();
			String folderName = null;
			// String newFolderName = null;
			folderName = "C:\\" + unzip.unZipFile("C:\\CashIt.zip", "C:\\"); // give
			// filepath
			// newFolderName = folderName.replace("/", "") + "_encoded";
			// (new File(newFolderName)).mkdir();
			File myDir = new File(folderName);
			if (myDir.exists() && myDir.isDirectory()) {
				File[] files = myDir.listFiles();
				for (int i = 0; i < files.length; i++) {
					ArrayList<String> value = null;// unzip.readFile(files[i].toString(),
					// rankPrizeMap);
					ArrayList<String> list = new ArrayList<String>();
					String crTable = "CREATE TABLE `st_se_pwt_inv_"
							+ gameNo
							+ "_"
							+ i
							+ "` (`virn_code` varchar(24),`id1` varchar(24) ,`id2` varchar(24) ,`game_id` int(10) ,  `pwt_amt` decimal(20,2) ,     `prize_level` varchar(10) ,    `status` varchar(25)  ) ENGINE=InnoDB DEFAULT CHARSET=utf8 CHECKSUM=1 DELAY_KEY_WRITE=1 ROW_FORMAT=DYNAMIC ";
					stmt = conn.createStatement();
					stmt.execute(crTable);
					logger.debug(new Date() + "**Inserting*Data**Start******"
							+ (new Date()).getTime());
					for (String string : value) {
						StringBuilder str = new StringBuilder(
								"insert into st_se_pwt_inv_" + gameNo + "_" + i
										+ " values (");
						str.append(string + ")");
						list.add(str.toString());
					}
					logger.debug(new Date() + "**Inserting*Data*Qry*******"
							+ (new Date()).getTime());
					unzip.performBatch(conn, list);
					logger.debug(new Date() + "**Inserting*Data**End******"
							+ (new Date()).getTime());
					String insTable = "insert into st_se_pwt_inv_" + gameNo
							+ " select * from st_se_pwt_inv_" + gameNo + "_"
							+ i;
					stmt = conn.createStatement();
					stmt.executeUpdate(insTable);
					String delTable = "drop table st_se_pwt_inv_" + gameNo
							+ "_" + i;
					stmt = conn.createStatement();
					stmt.execute(delTable);
				}
			}

			conn.commit();
			conn.setAutoCommit(true);
			conn.close();
			logger
					.debug(new Date() + "*****End******"
							+ (new Date()).getTime());
			complete = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return complete;
	}

	public static final void main(String[] args) {
		insertVirnInDB(101);
	}

	MessageDigest md5;

	public Unzip() {
		try {
			md5 = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
	}

	public void getEnCodedStr(String decStr, StringBuilder str,
			int gameNbrDigits, int maxRankDigits, Map<String, Map<Integer, Integer>> bookRankMap)
			throws NoSuchAlgorithmException {
		// StringBuilder encStr = new StringBuilder("");
		str.append("(");
		// int rank = Integer.parseInt(decStr.charAt(3) + "");
		// logger.debug("****rank"+rank);
		String decStrArr[] = decStr.split("\t");
		decStrArr[0] = decStrArr[0].trim(); // virn_code
		decStrArr[1] = decStrArr[1].trim(); // ticket_nbr

		String bookNumber = decStrArr[1].substring(0, decStrArr[1].lastIndexOf("-"));
		Map<Integer, Integer> rankMap = bookRankMap.get(bookNumber);
		if(rankMap == null) {
			rankMap = new HashMap<Integer, Integer>();
			bookRankMap.put(bookNumber, rankMap);
		}
		Integer rank = Integer.valueOf(decStrArr[0].substring(gameNbrDigits, gameNbrDigits + maxRankDigits));
		if(rankMap.get(rank) == null)
			rankMap.put(rank, 1);
		else
			rankMap.put(rank, rankMap.get(rank)+1);

		// for (int i = 0; i < decStrArr.length; i++) {
		// if(i==0){
		// decStrArr[i].substring(gameNbrDigits+maxRankDigits);
		// }
		// str.append("'"
		// + (new BASE64Encoder()).encode(md5.digest(decStrArr[i]
		// .getBytes())) + "',");
		// }

		String virn_code = decStrArr[0]
				.substring(gameNbrDigits + maxRankDigits);
		str.append("'"
				+ (new BASE64Encoder())
						.encode(md5.digest(virn_code.getBytes()))
				+ "','"
				+ (new BASE64Encoder()).encode(md5.digest(decStrArr[1]
						.getBytes()))
				+ "','"
				+ (new BASE64Encoder()).encode(md5.digest("1234".getBytes()))
				+ "',"
				+ decStrArr[0].substring(gameNbrDigits, gameNbrDigits
						+ maxRankDigits) + "),");

		// str.append(decStrArr[0].substring(gameNbrDigits, gameNbrDigits
		// + maxRankDigits)
		// + "),");
		// System.out.println(str);
		decStrArr = null;
		try {
			// logger.info("--encFun Call--");
			// System.gc();
			// finalize();
		} catch (Throwable e) {
			e.printStackTrace();
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

	public void performBatch(Connection conn, ArrayList<String> list) {
		Statement stmt;
		try {
			int n = 0;
			for (int i = 0; i < list.size(); i += 50000) {
				stmt = conn.createStatement();
				n = n + 50000;
				for (int j = i; j < n; j++) {
					stmt.addBatch(list.get(j));
					logger.debug(list.get(j));
				}
				stmt.executeBatch();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void readFile(Connection conn, String fileName, int gameNo,
			int gameNbrDigits, int maxRankDigits, Map<String, Map<Integer, Integer>> bookRankMap) throws SQLException,
			FileNotFoundException, NoSuchAlgorithmException, IOException {
		// ArrayList<String> fileData = new ArrayList<String>();

		FileInputStream fstream = new FileInputStream(fileName);
		DataInputStream in = new DataInputStream(fstream);
		BufferedReader br = new BufferedReader(new InputStreamReader(in));
		String strLine;
		logger.debug(new Date() + "**Reading***Start******"
				+ (new Date()).getTime());
		StringBuilder str = new StringBuilder("insert into st_se_pwt_inv_"
				+ gameNo + "_1 (virn_code,id1,id2,game_id) values ");
		// str.append("str" + ")");
		int startQry = 0, EndQry = 5000;
		boolean execQry = false;
		Statement stmt = conn.createStatement();
		while ((strLine = br.readLine()) != null) {
			execQry = true;
			getEnCodedStr(strLine, str, gameNbrDigits, maxRankDigits, bookRankMap);
			startQry++;
			if (startQry == EndQry) {
				str.deleteCharAt(str.length() - 1);

				// PreparedStatement
				// pstmt=conn.prepareStatement(str.toString());
				stmt.addBatch(str.toString());
				// logger.debug("***str"+str);
				logger.debug(new Date() + "*******ins*****************"
						+ new Date().getTime());
				// pstmt.setInt(1, gameNo);
				// logger.debug("******str\n"+pstmt);
				// int i=pstmt.executeUpdate();
				// logger.debug("insert "+i+" records");
				str = new StringBuilder("insert into st_se_pwt_inv_" + gameNo
						+ "_1 (virn_code,id1,id2,game_id) values ");
				startQry = 0;
				execQry = false;
				System.gc();
			}

			// logger.debug(strLine);
			// fileData.add(strLine);
		}
		if (execQry) {
			str.deleteCharAt(str.length() - 1);
			stmt.addBatch(str.toString());
			// PreparedStatement
			// pstmt=conn.prepareStatement(str.toString());
			// pstmt.setInt(1, gameNo);
			// int i=pstmt.executeUpdate();
			// logger.debug("insert "+i+" records");
		}
		int i[] = stmt.executeBatch();
		logger.debug("insert " + i.length + " records");
		logger.debug(new Date() + "***Reading**End******"
				+ (new Date()).getTime());
		in.close();
		// return fileData;
	}

	public Map<String, String> readRankForUploadedVirn(Connection conn,
			int gameNo) throws LMSException {
		Map<String, String> rankPrizeMap = null;
		try {
			Statement stmt = conn.createStatement();
			String rankQuery = "select rm.game_id,rm.rank_nbr,rm.prize_amt,rm.prize_level,if(rm.prize_amt=0,'NO_PRIZE_PWT','UNCLM_PWT') as status from st_se_rank_master rm,st_se_game_master gm where rm.game_id=gm.game_id and gm.game_nbr="
					+ gameNo;
			ResultSet rs = stmt.executeQuery(rankQuery);
			rankPrizeMap = new HashMap<String, String>();
			while (rs.next()) {
				rankPrizeMap.put(rs.getString("rank_nbr"), rs
						.getString("game_id")
						+ ","
						+ rs.getString("prize_amt")
						+ ",'"
						+ rs.getString("prize_level")
						+ "','"
						+ rs.getString("status") + "'");
			}
			logger.debug("*********rankPrizeMap::" + rankPrizeMap);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return rankPrizeMap;

	}

	public String unZipFile(String filePath, String destPath) {
		Enumeration<? extends ZipEntry> entries;
		ZipFile zipFile;
		String folderName = null;
		try {
			zipFile = new ZipFile(filePath);

			entries = zipFile.entries();

			while (entries.hasMoreElements()) {
				ZipEntry entry = (ZipEntry) entries.nextElement();

				if (entry.isDirectory()) {
					(new File(destPath + entry.getName())).mkdir();
					folderName = entry.getName();
					continue;
				}
				copyInputStream(zipFile.getInputStream(entry),
						new BufferedOutputStream(new FileOutputStream(destPath
								+ entry.getName())));
			}

			zipFile.close();
		} catch (IOException ioe) {
			logger.debug("Unhandled exception:");
			ioe.printStackTrace();
			return null;
		}
		return folderName;
	}

	public String virnFileUpload(int gameNo, String gameName, String details,
			String startDate, String saleendDate, String pwtendDate,
			String encSchemeType) {
		logger.debug("gameNbr::" + gameNo + "  gameName::" + gameName
				+ "  startDate::" + startDate + "  saleendDate::" + saleendDate
				+ "  pwtendDate::" + pwtendDate);
		logger.debug(new Date() + "*****Start******" + (new Date()).getTime());
		try {
			Connection conn = DBConnect.getConnection();
			conn.setAutoCommit(false);

			// get the game details from database
			GameTicketFormatBean ticketFmtBean = getGameDetails(conn, gameNo,
					gameName);
			int gameNbrDigits = ticketFmtBean.getGameNbrDigits();
			int maxRankDigits = ticketFmtBean.getMaxRankDigits();
			int game_id = ticketFmtBean.getGameId();
			if (gameNbrDigits == 0 || game_id == 0 || maxRankDigits == 0) {
				return "error";
			}

			// Map<String, String> rankPrizeMap =
			// readRankForUploadedVirn(conn,gameNo);
			String delTable = "drop table if exists  st_se_pwt_inv_" + gameNo
					+ "_1";
			Statement stmt = conn.createStatement();
			stmt.execute(delTable);

			String crTable = "CREATE TABLE `st_se_pwt_inv_"
					+ gameNo
					+ "_1` (`virn_code` varchar(24),`id1` varchar(24) ,`id2` varchar(24) ,`game_id` int(10) ,  `pwt_amt` decimal(20,2) ,     `prize_level` varchar(10) ,    `status` varchar(25), ticket_status enum('ACTIVE','MISSING','INACTIVE','SOLD') DEFAULT 'INACTIVE') ENGINE=InnoDB DEFAULT CHARSET=utf8 CHECKSUM=1 DELAY_KEY_WRITE=1 ROW_FORMAT=DYNAMIC ";
			stmt = conn.createStatement();
			stmt.execute(crTable);
			Map<String, Map<Integer, Integer>> bookRankMap = new HashMap<String, Map<Integer,Integer>>();
			readFile(conn, details, gameNo, gameNbrDigits, maxRankDigits, bookRankMap);

			/*	Update Low Winning Tier Tickets Start	*/
			stmt = conn.createStatement();
			Map<Integer, String> prizeMap = new HashMap<Integer, String>();
			ResultSet rs = stmt.executeQuery("SELECT rank_nbr, prize_level FROM st_se_rank_master WHERE game_id = " + game_id + ";");
			while(rs.next())
				prizeMap.put(rs.getInt("rank_nbr"), rs.getString("prize_level"));

			stmt = conn.createStatement();
			for(Map.Entry<String, Map<Integer, Integer>> bookEntry : bookRankMap.entrySet()) {
				int lowCount = 0;
				String bookNumber = bookEntry.getKey();
				for(Map.Entry<Integer, Integer> rankEntry : bookEntry.getValue().entrySet()) {
					Integer rank = rankEntry.getKey();
					Integer count = rankEntry.getValue();

					if("LOW".equals(prizeMap.get(rank)))
						lowCount += count;
				}
				stmt.addBatch("UPDATE st_se_game_inv_status SET total_low_win_tier_tickets="+lowCount+" WHERE book_nbr='"+bookNumber+"';");
			}
			stmt.executeBatch();
			/*	Update Low Winning Tier Tickets End		*/

			String updateTlb = "update st_se_pwt_inv_"
					+ gameNo
					+ "_1 spi,st_se_rank_master rm set spi.game_id="
					+ game_id
					+ ",spi.pwt_amt=rm.prize_amt,  spi.prize_level=rm.prize_level,spi.status=if(rm.prize_amt=0.00,'NO_PRIZE_PWT','UNCLM_PWT'), ticket_status='ACTIVE' where spi.game_id=rm.rank_nbr and rm.game_id="
					+ game_id;
			stmt = conn.createStatement();
			logger.debug(new Date() + "*****UPD Tmp Table******"
					+ (new Date()).getTime());
			stmt.executeUpdate(updateTlb);
			logger.debug(new Date() + "*****UPD Tmp Table  End******"
					+ (new Date()).getTime());
			String insTable = "insert into st_se_pwt_inv_" + gameNo
					+ " select * from st_se_pwt_inv_" + gameNo + "_1";
			stmt = conn.createStatement();
			stmt.executeUpdate(insTable);
			logger.debug(new Date() + "*****Ins Main Table End******"
					+ (new Date()).getTime());
			delTable = "drop table st_se_pwt_inv_" + gameNo + "_1";
			stmt = conn.createStatement();
			stmt.execute(delTable);

			// date formatted in the MySQL form
			DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			dateFormat.setCalendar(Calendar.getInstance());
			java.sql.Date stDate = new java.sql.Date(dateFormat
					.parse(startDate).getTime());
			java.sql.Date saleEndDate = new java.sql.Date(dateFormat.parse(
					saleendDate).getTime());
			java.sql.Date pwtEndDate = new java.sql.Date(dateFormat.parse(
					pwtendDate).getTime());

			// update st_se_game_master
			Statement stmt1 = conn.createStatement();
			String querytoInsertDate = QueryManager.insertGameDates() + "'"
					+ stDate + "',sale_end_date='" + saleEndDate
					+ "',pwt_end_date='" + pwtEndDate
					+ "',add_inv_status='F'  where game_id=" + game_id + "";
			stmt1.executeUpdate(querytoInsertDate);
			logger.debug(new Date() + "*****update Rank Table Start******"
					+ (new Date()).getTime());
			Statement updateRankMaster = conn.createStatement();
			String totalPrize = "update st_se_rank_master b,(select aa.game_id, aa.pwt_amt, count(aa.pwt_amt) 'total_no_of_prize' from st_se_pwt_inv_"
					+ gameNo
					+ " aa  where aa.game_id="
					+ game_id
					+ " group by aa.game_id, aa.pwt_amt)a set b.total_no_of_prize = a.total_no_of_prize where a.game_id = b.game_id and a.pwt_amt = b.prize_amt and  a.game_id="
					+ game_id;
			updateRankMaster.executeUpdate(totalPrize);
			logger.debug(new Date() + "*****update Rank Table End******"
					+ (new Date()).getTime());
			conn.commit();
			logger.debug(new Date() + "*****After Commit******"
					+ (new Date()).getTime());
			conn.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		logger.debug(new Date() + "*****End******" + (new Date()).getTime());
		return "success";

	}

	public void writeFile(String filePath, ArrayList<String> value) {
		try {
			logger.debug(filePath);
			File f = new File(filePath);
			FileOutputStream fop = new FileOutputStream(f);

			if (f.exists()) {
				for (String str : value) {
					logger.debug(str);
					fop.write((str + "\n").getBytes());
				}
				fop.flush();
				fop.close();
			} else {
				logger.debug("This file is not exist");
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
