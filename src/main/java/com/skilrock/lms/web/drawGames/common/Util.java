package com.skilrock.lms.web.drawGames.common;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.CRC32;
import java.util.zip.Checksum;

import javax.servlet.ServletContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.ActionSupport;
import com.skilrock.lms.ajax.AjaxRequestHelper;
import com.skilrock.lms.beans.MessageDetailsBean;
import com.skilrock.lms.beans.OrgDataBean;
import com.skilrock.lms.beans.PromoGameBean;
import com.skilrock.lms.beans.ResponsibleGamingBean;
import com.skilrock.lms.common.ConfigurationVariables;
import com.skilrock.lms.common.Utility;
import com.skilrock.lms.common.db.DBConnect;
import com.skilrock.lms.common.exception.LMSErrors;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.common.filter.LMSFilterDispatcher;
import com.skilrock.lms.common.utility.LMSUtility;
import com.skilrock.lms.coreEngine.drawGames.playMgmt.ServerStartUpData;
import com.skilrock.lms.dge.beans.DrawDetailsBean;
import com.skilrock.lms.dge.beans.FortunePurchaseBean;
import com.skilrock.lms.dge.beans.GameMasterLMSBean;
import com.skilrock.lms.dge.beans.KenoPurchaseBean;
import com.skilrock.lms.dge.beans.LottoPurchaseBean;
import com.skilrock.lms.dge.beans.RaffleDrawIdBean;
import com.skilrock.lms.dge.beans.RafflePurchaseBean;
import com.skilrock.lms.dge.beans.ValidateTicketBean;
import com.skilrock.lms.embedded.drawGames.common.CommonMethods;


public class Util extends ActionSupport {

	public static Map<String, Map<Long, String>> drawIdDateMap = null;
	public static Map<Integer, Map<Integer, String>> drawIdTableMap = null;
	public static TreeMap<Integer, List<List>> gameData = null;
	private static Map<Integer, GameMasterLMSBean> gameMap = null;
	private static Map<Integer, GameMasterLMSBean> sleGameMap = null;
	private static Map<Integer, GameMasterLMSBean> lmsGameMap = null;
	private static Map<Integer, GameMasterLMSBean> iwGameMap = null;
	private static Map<Integer, GameMasterLMSBean> vsGameMap = null;
	public static Map<Integer, Double> jackPotMap = null;
	public static Map<Integer, Map<Integer, DrawDetailsBean>> drawDetailsMap = null;
	public static Map<Integer, List<String>> drawNameListMap = null;
	// added by yogesh
	public static Map<Integer, Map<Integer, OrgDataBean>> orgDataMap = null;
	public static Map<Integer, Map<Integer, OrgDataBean>> sleOrgDataMap = null;
	public static Map<Integer, Map<Integer, OrgDataBean>> iwOrgDataMap = null;
	public static Map<Integer, Map<Integer, OrgDataBean>> vsOrgDataMap = null;
	public static Map<Integer, Map<Integer, List<MessageDetailsBean>>> advMsgDataMap = null;
	public static Map<Integer,List<PromoGameBean>> promoGameBeanMap=null;
	public static Map<String, ResponsibleGamingBean> respGamingCriteriaStatusMap=null;
	public static Map<Integer, String> catMap = null;
	public static Map<String, Integer> serviceCodeIDMap = null;
	static Log logger = LogFactory.getLog(Util.class);
	public static boolean onfreezeSale = true;
	//private static boolean isGameMapSet = false;
	/**
	 * 
	 */
	
	static {
		setCategoryMapDetails();
		setServiceCodeIdMap();
	}
	/*static {
		logger.info("Game Map initialization starts....");
		setGameMapOnFirstCall();
		logger.info("Game Map initialization ends....");
	}*/
	
	private static final long serialVersionUID = 1L;

	public static String convertCollToStr(Object obj) {
		return obj.toString().replace("[", "").replace("]", "")
				.replace("{", "").replace("}", "");
	}
	

	private static void setGameMapOnFirstCall() {
		ServerStartUpData.onStartGameData(LMSUtility.sc);
		
	}


	public static int fetchOrgIdOfUser(int userId) {
		int orgId = 0;
		Connection con = DBConnect.getConnection();
		Statement stmt = null;
		ResultSet rs = null;
		try {
			stmt = con.createStatement();
			rs = stmt
					.executeQuery("select organization_id from st_lms_user_master where user_id = "
							+ userId);
			while (rs.next()) {
				orgId = rs.getInt("organization_id");
			}
		} catch (Exception e) {
			e.printStackTrace();
			// throw new LMSException(e);
		} finally {
			try {
				if (con != null && !con.isClosed()) {
					con.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return orgId;
	}

	public static int getAdvMsgs(Map<String, List<String>> advMsgMap,
			StringBuilder topMsgsStr, StringBuilder bottomMsgsStr,
			int appletHeight) {
		String advtMsgEnable = (String) ServletActionContext
				.getServletContext().getAttribute("ADVT_MSG");
		if (advtMsgEnable != null && !"NULL".equalsIgnoreCase(advtMsgEnable)
				&& "YES".equalsIgnoreCase(advtMsgEnable.trim())) {
			if (advMsgMap != null) {
				List<String> topMsgsList = advMsgMap.get("TOP");
				List<String> bottomMsgsList = advMsgMap.get("BOTTOM");
				int msgLen = 0;
				if (topMsgsList != null) {
					for (int i = 0; i < topMsgsList.size(); i++) {
						topMsgsStr = topMsgsStr
								.append(topMsgsList.get(i) + "~");
						msgLen = topMsgsList.get(i).length();
						if (msgLen > 17) {
							appletHeight = appletHeight + 11 * (msgLen / 17)
									+ 11;
						} else {
							appletHeight = appletHeight + 22;
						}
					}
					if (topMsgsStr.length() > 0) {
						topMsgsStr.deleteCharAt(topMsgsStr.length() - 1);
					}
				}
				if (bottomMsgsList != null) {
					for (int i = 0; i < bottomMsgsList.size(); i++) {
						bottomMsgsStr = bottomMsgsStr.append(bottomMsgsList
								.get(i)
								+ "~");
						msgLen = bottomMsgsList.get(i).length();
						if (msgLen > 17) {
							appletHeight = appletHeight + 11 * (msgLen / 17)
									+ 11;
						} else {
							appletHeight = appletHeight + 22;
						}
					}
					if (bottomMsgsStr.length() > 0) {
						bottomMsgsStr.deleteCharAt(bottomMsgsStr.length() - 1);
					}
				}
			}
		}
		return appletHeight;
	}

	public static String getDGIP(String project) {
		ServletContext sc = ServletActionContext.getServletContext();
		return (String) sc.getAttribute("HOST") + "://"
				+ (String) sc.getAttribute(project)
				+ (String) sc.getAttribute("PORT");
	}

	public static String getGameDisplayName(int gameId) {
		Iterator<Map.Entry<Integer, GameMasterLMSBean>> gameNumItr = getGameMap()
				.entrySet().iterator();
		while (gameNumItr.hasNext()) {
			Map.Entry<Integer, GameMasterLMSBean> gameNumpair = gameNumItr
					.next();
			if (gameId == gameNumpair.getValue().getGameId()) {
				return gameNumpair.getValue().getGameName();
			}
		}
		return null;
	}

	public static GameMasterLMSBean getGameMasterLMSBean(int gameId) {
		return getGameMap().get(gameId);
	}
	
	public static GameMasterLMSBean getSLEGameMasterLMSBean(int gameId) {
		return sleGameMap.get(gameId);
	}

	public static double getSaleCommVariance(int organizationId, int gameId) {
		return (orgDataMap.get(organizationId)).get(gameId).getSaleCommVar();
	}

	public static double getSLESaleCommVariance(int organizationId, int gameId) {
		return (sleOrgDataMap.get(organizationId)).get(gameId).getSaleCommVar();
	}

	public static double getSLEPwtCommVariance(int organizationId, int gameId) {
		return (sleOrgDataMap.get(organizationId)).get(gameId).getPwtCommVar();
	}
	
	public static double getIWSaleCommVariance(int organizationId, int gameId) {
		return (iwOrgDataMap.get(organizationId)).get(gameId).getSaleCommVar();
	}

	public static double getIWPwtCommVariance(int organizationId, int gameId) {
		return (iwOrgDataMap.get(organizationId)).get(gameId).getPwtCommVar();
	}
	
	public static double getVSSaleCommVariance(int organizationId, int gameId) {
		return (vsOrgDataMap.get(organizationId)).get(gameId).getSaleCommVar();
	}

	public static double getVSPwtCommVariance(int organizationId, int gameId) {
		return (vsOrgDataMap.get(organizationId)).get(gameId).getPwtCommVar();
	}
	
	public static GameMasterLMSBean getIWGameMasterLMSBean(int gameId) {
		return iwGameMap.get(gameId);
	}
	
	public static GameMasterLMSBean getVSGameMasterLMSBean(int gameId) {
		return vsGameMap.get(gameId);
	}

	public static void updateSaleCommVariance(int organizationId, int gameId,
			double updatedCommVar) {
		(orgDataMap.get(organizationId)).get(gameId).setSaleCommVar(
				updatedCommVar);
	}
	
	public static void updateSaleCommVarianceIW(int organizationId, int gameId, double updatedCommVar) {
		(iwOrgDataMap.get(organizationId)).get(gameId).setSaleCommVar(updatedCommVar);
	}
	
	public static void updateSaleCommVarianceVS(int organizationId, int gameId, double updatedCommVar) {
		(vsOrgDataMap.get(organizationId)).get(gameId).setSaleCommVar(updatedCommVar);
	}

	
	public static int getGameIdFromGameNumber(int gameNumber) {
		Iterator<Map.Entry<Integer, GameMasterLMSBean>> gameNumItr = getGameMap()
				.entrySet().iterator();
		while (gameNumItr.hasNext()) {
			Map.Entry<Integer, GameMasterLMSBean> gameNumpair = gameNumItr
					.next();
			if(gameNumpair.getValue().getGameStatus() != null && (gameNumpair.getValue().getGameStatus().equals("OPEN") || gameNumpair.getValue().getGameStatus().equals("SALE_HOLD"))) {
			if (gameNumber == gameNumpair.getValue().getGameNo()) {
				return gameNumpair.getValue().getGameId();
			}
			}
		}
		return 0;
	}
	public static int getGameIdFromLmsGameNumber(int gameNumber) {
		Iterator<Map.Entry<Integer, GameMasterLMSBean>> gameNumItr = getLmsGameMap()
				.entrySet().iterator();
		while (gameNumItr.hasNext()) {
			Map.Entry<Integer, GameMasterLMSBean> gameNumpair = gameNumItr
					.next();
			if(gameNumpair.getValue().getGameStatus().equals("OPEN")||gameNumpair.getValue().getGameStatus().equals("SALE_HOLD")){
			if (gameNumber == gameNumpair.getValue().getGameNo()) {
				return gameNumpair.getValue().getGameId();
			}
			}
		}
		return 0;
	}

	public static Map<Integer, GameMasterLMSBean> getGameMap() {
		if (gameMap == null) {
			setGameMapOnFirstCall();
		}
		return gameMap;
	}
	
	public static Map<Integer, GameMasterLMSBean> getSLEGameMap() {
		return sleGameMap;
	}
	
	public static Map<Integer, GameMasterLMSBean> getLmsGameMap() {
		if (lmsGameMap == null) {
			setGameMapOnFirstCall();
		}
		return lmsGameMap;
	}
	
	public static Map<Integer, GameMasterLMSBean> getIWGameMap() {
		return iwGameMap;
	}
	
	public static Map<Integer, GameMasterLMSBean> getVSGameMap() {
		return vsGameMap;
	}

	public static String getGameName(int gameId) {
		Iterator<Map.Entry<Integer, GameMasterLMSBean>> gameNumItr = getGameMap()
				.entrySet().iterator();
		while (gameNumItr.hasNext()) {
			Map.Entry<Integer, GameMasterLMSBean> gameNumpair = gameNumItr
					.next();
			if (gameId == gameNumpair.getValue().getGameId()) {
				return gameNumpair.getValue().getGameNameDev();
			}
		}
		return null;
	}

	public static int getGameNumberFromGameId(int gameId) {
		Iterator<Map.Entry<Integer, GameMasterLMSBean>> gameNumItr = getGameMap()
				.entrySet().iterator();
		while (gameNumItr.hasNext()) {
			Map.Entry<Integer, GameMasterLMSBean> gameNumpair = gameNumItr
					.next();
			if(gameNumpair.getValue().getGameStatus()!=null && gameNumpair.getValue().getGameStatus().equals("OPEN")){
			if (gameId == gameNumpair.getValue().getGameId()) {
				return gameNumpair.getValue().getGameNo();
			}
			}
		}
		return 0;
	}


	public static int getGameId(String gameDevName){
		Iterator<Map.Entry<Integer, GameMasterLMSBean>> gameNumItr = getGameMap()
		.entrySet().iterator();
		while (gameNumItr.hasNext()) {
			Map.Entry<Integer, GameMasterLMSBean> gameNumpair = gameNumItr
			.next();
			System.out.println(gameNumpair.getValue().getGameNameDev());
			if(gameNumpair.getValue().getGameStatus().equals("OPEN")){
			if (gameDevName.equals(gameNumpair.getValue().getGameNameDev())) {
				return gameNumpair.getValue().getGameId();
			}
		}
	}
	return 0;
	}
	
	/*public static List<Integer> getGameNumberList() {
		List<Integer> gameNumList = new ArrayList<Integer>();
		Iterator<Map.Entry<Integer, GameMasterLMSBean>> gameNumItr = gameMap
				.entrySet().iterator();
		while (gameNumItr.hasNext()) {
			Map.Entry<Integer, GameMasterLMSBean> gameNumpair = gameNumItr
					.next();
			gameNumList.add(gameNumpair.getValue().getGameNo());
		}
		return gameNumList;
	}*/
	
	public static List<Integer> getGameNumberList() {
		List<Integer> gameNumList = new ArrayList<Integer>();
		Iterator<Map.Entry<Integer, GameMasterLMSBean>> gameNumItr = getGameMap()
				.entrySet().iterator();
		while (gameNumItr.hasNext()) {
			Map.Entry<Integer, GameMasterLMSBean> gameNumpair = gameNumItr
					.next();
			gameNumList.add(gameNumpair.getValue().getGameId());
		}
		return gameNumList;
	}
	
	public static List<Integer> getLMSGameNumberList() {
		List<Integer> gameNumList = new ArrayList<Integer>();
		Iterator<Map.Entry<Integer, GameMasterLMSBean>> gameNumItr = getLmsGameMap()
				.entrySet().iterator();
		while (gameNumItr.hasNext()) {
			Map.Entry<Integer, GameMasterLMSBean> gameNumpair = gameNumItr
					.next();
			gameNumList.add(gameNumpair.getValue().getGameId());
		}
		return gameNumList;
	}
	
	public static List<Integer> getCategoryNumberList(Connection con) throws SQLException {
		List<Integer> categoryNumList = new ArrayList<Integer>();
		try
		{
		PreparedStatement pStatement = con.prepareStatement("select category_id from st_cs_product_category_master where status='ACTIVE'");
		ResultSet rs = pStatement.executeQuery();
		while (rs.next()) {
			categoryNumList.add(rs.getInt("category_id"));
		}
		}
		catch (Exception e) {
			e.printStackTrace();
			// throw new LMSException(e);
		} 
		return categoryNumList;
	}
	
	public static List<Integer> getWalletNumberList(Connection con) throws SQLException {
		List<Integer> walletNumList = new ArrayList<Integer>();
		try
		{
		PreparedStatement pStatement = con.prepareStatement("select wallet_id from st_ola_wallet_master where wallet_status='ACTIVE'");
		ResultSet rs = pStatement.executeQuery();
		while (rs.next()) {
			walletNumList.add(rs.getInt("wallet_id"));
		}
		}
		catch (Exception e) {
			e.printStackTrace();
		} 
		return walletNumList;
	}

	public static String getGameType(int gameId) {
		String gameName = getGameName(gameId);
		if (gameName != null) {
			if (gameName.equals("Fortune") || gameName.equals("Zerotonine") || gameName.equals("OneToTwelve")
					|| gameName.equals("Card16") || gameName.equals("Card12")) {
				return "Fortune";
			} else if (gameName.equals("Lotto") || gameName.equals("Zimlotto")
					|| gameName.equals("Zimlottotwo")
					|| gameName.equals("Zimlottothree")
					|| gameName.equals("ZimLottoBonus")
					|| gameName.equals("ZimLottoBonusFree")
					|| gameName.equals("ZimLottoBonusTwo")
					|| gameName.equals("ZimLottoBonusTwoFree")
					|| gameName.equals("Fastlotto")
					|| gameName.equals("Tanzanialotto")
					|| gameName.equals("BonusBalllotto")
					|| gameName.equals("BonusBallTwo")) {
				return "Lotto";
			} else if (gameName.equals("Keno") || gameName.equals("KenoTwo") || gameName.equals("KenoFour") || gameName.equals("KenoFive")  || gameName.equals("KenoSix") || gameName.equals("KenoSeven")

					|| gameName.equals("KenoEight") || "Super2".equals(gameName) || "SuperTwo".equals(gameName) || "TwelveByTwentyFour".equals(gameName) || "TenByTwenty".equals(gameName) || gameName.equals("MiniRoulette") || gameName.equals("FullRoulette")||"TenByThirty".equals(gameName) || "KenoNine".equals(gameName)) {

				return "Keno";
			} else if ("RaffleGame".equals(gameName)
					|| "RaffleGame1".equals(gameName) || "DGRaffle".equalsIgnoreCase(gameName) || "SERaffle".equalsIgnoreCase(gameName)) {
				return "RAFFLE";
			} else if ("FortuneTwo".equalsIgnoreCase(gameName)) {
				return "FortuneTwo";
			} else if ("FortuneThree".equalsIgnoreCase(gameName)){
				return "FortuneThree";
			} else if ("RainbowGame".equalsIgnoreCase(gameName) || gameName.equals("PickFour") || gameName.equals("PickThree")){
				return "Rainbow";
			}
		}
		return null;
	}

	// ------Added on 01/07/10-----

	public static int getOfflineFileGameNo(String fileName) {
		return fileName.contains("_") ? Integer
				.parseInt(fileName.split("_")[1]) : Integer.parseInt(fileName
				.charAt(4)
				+ "");
	}

	public static String getOrgName(int userOrgId) {
		String orgName = null;
		Connection con = DBConnect.getConnection();
		Statement stmt = null;
		ResultSet rs = null;
		try {
			stmt = con.createStatement();
			rs = stmt
					.executeQuery("select name from st_lms_organization_master where organization_id = '"
							+ userOrgId + "'");
			while (rs.next()) {
				orgName = rs.getString("name").toUpperCase();
			}
			logger.debug("orgName-------" + orgName);
		} catch (Exception e) {
			e.printStackTrace();
			// throw new LMSException(e);
		} finally {
			try {
				if (con != null && !con.isClosed()) {
					con.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return orgName;
	}

	//this method is depricated and not used commented on 11 april 2013 by sumit
	/*public static int getRandomNo(int startRange, int endRange) {
		// int randomNo = (int) ((Math.random() * (endRange - startRange)) +
		// startRange);
		int randomNo = RNGUtilities.generateRandomNumber(startRange, endRange);
		return randomNo;
	}*/
	//this method is depricated and not used commented on 11 april 2013 by sumit
	/*public static String getRandomNoKeno(int startRange, int endRange,
			int noOfQP) {
		String randStr = "";
		Set randSet = new TreeSet();
		while (randSet.size() != noOfQP) {
			// randStr=""+(int)((Math.random() * (endRange - startRange)) +
			// startRange);
			randStr = ""
					+ RNGUtilities.generateRandomNumber(startRange, endRange);
			randSet.add(randStr.length() > 1 ? randStr : "0" + randStr);
		}

		return randSet.toString().replace("[", "").replace("]", "").replace(
				" ", "");
	}*/

	public static double getUnitPrice(int gameId, String betType) {
		Double amount = (getGameMap().get(gameId).getPriceMap().get(
				betType).getUnitPrice());
		return amount == null ? 0.0 : amount;
	}

	public static int maxPickedGameWise(int gameNo) {
		int maxPicked = 1; // For Fortune type Games
		String gameDevName = getGameName(gameNo);
		if ("keno".equalsIgnoreCase(gameDevName)) {
			maxPicked = 5;
		} else if ("lotto".equalsIgnoreCase(gameDevName)) {
			maxPicked = 6;
		} else if ("zimlotto".equalsIgnoreCase(gameDevName)) {
			maxPicked = 6;
		} else if ("zimlottotwo".equalsIgnoreCase(gameDevName)) {
			maxPicked = 6;
		}else if ("zimlottothree".equalsIgnoreCase(gameDevName)) {
			maxPicked = 6;
		} else if ("fastlotto".equalsIgnoreCase(gameDevName)) {
			maxPicked = 5;
		} else if ("KenoTwo".equalsIgnoreCase(gameDevName)) {
			maxPicked = 5;
		}
		return maxPicked;
	}

	public static void setGameMap(Map<Integer, GameMasterLMSBean> gameMap) {
		Util.gameMap = gameMap;
		Collections.unmodifiableMap(Util.gameMap);
	}
	
	public static void setSLEGameMap(Map<Integer, GameMasterLMSBean> gameMap) {
		Util.sleGameMap = gameMap;
		Collections.unmodifiableMap(Util.sleGameMap);
	}
	
	public static void setLmsGameMap(Map<Integer, GameMasterLMSBean> lmsGameMap) {
		Util.lmsGameMap = lmsGameMap;
		Collections.unmodifiableMap(Util.lmsGameMap);
	}
	
	public static void setIWGameMap(Map<Integer, GameMasterLMSBean> gameMap) {
		Util.iwGameMap = gameMap;
		Collections.unmodifiableMap(Util.iwGameMap);
	}
	
	public static void setVSGameMap(Map<Integer, GameMasterLMSBean> gameMap) {
		Util.vsGameMap = gameMap;
		Collections.unmodifiableMap(Util.vsGameMap);
	}

	public static boolean validateNumber(int startRange, int endRange,
			String data, boolean isDuplicate) {

		String[] dataArr = data.split(",");
		SortedSet<Integer> dataSet = new TreeSet<Integer>();
		boolean isValid = true;
		for (String element : dataArr) {
			dataSet.add(Integer.parseInt(element));
		}

		if (!isDuplicate) {
			if (dataArr.length != dataSet.size()) {
				isValid = false;
			}
		}

		if (dataSet.last() > endRange || dataSet.first() < startRange) {
			isValid = false;
		}

		return isValid;
	}

	public static boolean checkSpecialCharacter(String data) {
		
		Pattern pattern = Pattern.compile("[^,0-9,a-z,A-Z]");
		  Matcher matcher = pattern.matcher(data);
		  boolean b=matcher.find();
		  System.out.print(b);
		return b;
	}
	
	public static ValidateTicketBean validateTkt(String tktNum) {
		int retIdLen = 0;
		int gameNo = 0;
		int tktLen = 0;
		int day = 0;
		String gameName = null;
		String tktBuf = null;
		String ticketNumInDB=null;
		String reprintCount = null;
		ValidateTicketBean tktBean = new ValidateTicketBean();
		tktBean.setValid(false);
		if (tktNum != null
				&& (tktNum.length() == ConfigurationVariables.tktLenA || tktNum
						.length() == ConfigurationVariables.tktLenB)) {
			if (tktNum.length() == ConfigurationVariables.tktLenA) {
				tktLen = ConfigurationVariables.tktLenA;
				retIdLen = ConfigurationVariables.retIdLenA;
				tktBuf = tktNum.substring(retIdLen, retIdLen
						+ ConfigurationVariables.gameNoLenA);
				reprintCount = tktNum.substring(tktLen - 2, tktLen);
				ticketNumInDB=tktNum.substring(0, tktLen - 2);
			} else if (tktNum.length() == ConfigurationVariables.tktLenB) {
				tktLen = ConfigurationVariables.tktLenB;
				retIdLen = ConfigurationVariables.retIdLenB;
				tktBuf = tktNum.substring(retIdLen, retIdLen
						+ ConfigurationVariables.gameNoLenB);
				reprintCount = tktNum.substring(tktLen - 1, tktLen);
				ticketNumInDB=tktNum.substring(0, tktLen - 1);
			}
			gameNo = Integer.parseInt(tktBuf);
			day = Integer.parseInt(tktNum.substring(retIdLen + tktBuf.length(),
					retIdLen + tktBuf.length() + 3));
			gameName = getGameName(getGameIdFromLmsGameNumber(gameNo));
			if (gameName != null) {
				tktBean.setGameName(gameName);
				tktBean.setGameNo(gameNo);
				tktBean.setReprintCount(reprintCount);
				tktBean.setTicketNumInDB(ticketNumInDB);
				tktBean.setValid(true);
			}
			if (day > 366) {
				//tktBean.setValid(false);
			}
			tktBean.setDayOfTicket(day);
		}
		logger.debug(tktBean.getGameNo() + "**Validate"
				+ tktBean.getTicketNumInDB() + "**" + tktBean.getReprintCount()
				+ "**" + tktBean.isValid());
		return tktBean;
	}

	public static int getRaffData(
			List<RafflePurchaseBean> rafflePurchaseBeanList,
			StringBuilder raffleData, int appletHeight) {
		if(rafflePurchaseBeanList != null){
			RafflePurchaseBean bean = null;
			if (rafflePurchaseBeanList.size() > 0) {
				raffleData.deleteCharAt(raffleData.length() - 1);
			}
			for (int i = 0; i < rafflePurchaseBeanList.size(); i++) {
				bean = rafflePurchaseBeanList.get(i);
				String raffTktType = bean.getRaffleTicketType();
				raffleData.append("raffTktType-sprtr-" + raffTktType
						+ "#raffTktNo-sprtr-" + bean.getRaffleTicket_no()
						+ "#raffDrawTime-sprtr-" + bean.getDrawDateTime()
						+ "#raffDispName-sprtr-" + bean.getGameDispName() + "Nxt");
				if ("ORIGINAL".equalsIgnoreCase(raffTktType)) {
					appletHeight = appletHeight + 250;
				} else if ("REFERENCE".equalsIgnoreCase(raffTktType)) {
					appletHeight = appletHeight + 44;
				}
			}
			if (bean != null) {
				raffleData.delete(raffleData.length() - 3, raffleData.length());
			}
		}
		return appletHeight;
	}

	public static int getPromoData(
			List<LottoPurchaseBean> promoPurchaseBeanList,
			StringBuilder finalPromoData, int appletHeight) {
		 
			for(int j=0;j<promoPurchaseBeanList.size();j++){
				LottoPurchaseBean lottoBean=promoPurchaseBeanList.get(j);
			String time = lottoBean.getPurchaseTime()
			.replace(" ", "|Time:").replace(".0", "");
          	int listSize = lottoBean.getDrawDateTime().size();
			StringBuilder drawTimeBuild = new StringBuilder("");
			for (int i = 0; i < listSize; i++) {
				drawTimeBuild.append(("|DrawDate:" + lottoBean
						.getDrawDateTime().get(i)).replace(" ", "|DrawTime:")
						.replace(".0", ""));
			}
			StringBuilder stBuilder = new StringBuilder("");
			for (int i = 0; i < lottoBean.getPlayerPicked().size(); i++) {
				stBuilder.append("|Num:"
						+ lottoBean.getPlayerPicked().get(i) + "|QP:"
						+ lottoBean.getIsQuickPick()[i]);
			}

			List<RafflePurchaseBean> rafflePurchaseBeanList = lottoBean
			.getRafflePurchaseBeanList();
			String raffleData = CommonMethods
			.buildRaffleData(rafflePurchaseBeanList);
			
			String finalData = "PromoTkt:" + "TicketNo:"
					+ lottoBean.getTicket_no() + lottoBean.getReprintCount()
					+ "|Date:" + time 
					+ "|PlayType:" + lottoBean.getPlayType()
					+ stBuilder.toString()+ "|TicketCost:"
					+ lottoBean.getTotalPurchaseAmt() + drawTimeBuild.toString()
					+ "|"
					+ raffleData;
			finalPromoData.append(finalData);
			}
		
		
		return appletHeight;
	}
	public static int getRaffPWTData(
			List<RaffleDrawIdBean> raffleDrawIdBeanList,
			StringBuilder raffleData, int appletHeight) {
		RaffleDrawIdBean bean = null;
		if (raffleDrawIdBeanList.size() > 0) {
			raffleData.deleteCharAt(raffleData.length() - 1);
		}
		for (int i = 0; i < raffleDrawIdBeanList.size(); i++) {
			bean = raffleDrawIdBeanList.get(i);
			String status = bean.getStatus();
			String pwtStatus = bean.getPwtStatus();
			double totalRaffleAmount = 0.0;

			if ("FRAUD".equalsIgnoreCase(status)) {
				raffleData.append("Cannot Verify.Invalid PWT");
			} else if ("TICKET_EXPIRED".equalsIgnoreCase(status)) {
				raffleData.append("Expired or Invalid Ticket");
			} else if ("RES_AWAITED".equalsIgnoreCase(status)) {
				raffleData.append("Awaited~" + bean.getDrawDateTime());
			} else if ("NON_WIN".equalsIgnoreCase(status)) {
				raffleData.append("TRY AGAIN~" + bean.getDrawDateTime());
			} else if ("NORMAL_PAY".equalsIgnoreCase(status)) {
				/*
				 * totalRaffleAmount = totalRaffleAmount +
				 * Double.parseDouble(bean.getWinningAmt());
				 */
				raffleData.append("WIN " + bean.getWinningAmt() + "~"
						+ bean.getDrawDateTime() + "~" + bean.getWinningAmt());
			} else if ("CLAIMED".equalsIgnoreCase(status)) {
				raffleData.append("CLAIMED~" + bean.getDrawDateTime());
			} else if ("PND_PAY".equalsIgnoreCase(pwtStatus)) {
				raffleData.append("Cannot Verify.High Prize");
			} else if ("HIGH_PRIZE".equalsIgnoreCase(pwtStatus)) {
				raffleData.append("Cannot Verify.High Prize");
			} else if ("OUT_VERIFY_LIMIT".equalsIgnoreCase(pwtStatus)) {
				raffleData.append("Cannot Verify.High Prize");
			} else if ("OUT_PAY_LIMIT".equalsIgnoreCase(pwtStatus)) {
				raffleData.append("Cannot Verify.High Prize");
			}
			raffleData.append("Nxt");
			appletHeight = appletHeight + 155;
		}
		if (bean != null) {
			raffleData.delete(raffleData.length() - 3, raffleData.length());
		}

		return appletHeight;
	}

	public static String getPromoData(Object bean, String promoOriginalData) {
		StringBuilder finalData = new StringBuilder("");
		if (bean != null) {
			if (bean instanceof List) {
				// To Be Implemented Later...
			} else {
				int barcodeCount=-1;
				String gameName = "";
				String tktNo = "";
				String rpcCount = "";
				String saleStatus = "";
				String purchaseTime = "";
				String gameDispName = "";
				List drawDateList = null;
				StringBuilder drawDateTime = new StringBuilder("");
				StringBuilder pickNumStr = new StringBuilder("");
				int expiryPeriod = 0;
				int noOfDraws = 0;
				double totPurchaseAmt = 0.0;
				StringBuilder topMsgsStr = new StringBuilder(" ");
				StringBuilder bottomMsgsStr = new StringBuilder(" ");
				StringBuilder raffleData = new StringBuilder(" ");
				int totalQuantity = 0;
				StringBuilder gameDependentData = new StringBuilder("");

				if (bean instanceof FortunePurchaseBean) {
					FortunePurchaseBean fortuneBean = (FortunePurchaseBean) bean;
					gameName = Util.getGameName(fortuneBean.getGame_no());
					tktNo = fortuneBean.getTicket_no();
					rpcCount = fortuneBean.getReprintCount();
					saleStatus = fortuneBean.getSaleStatus();
					purchaseTime = fortuneBean.getPurchaseTime();
					gameDispName = fortuneBean.getGameDispName();
					drawDateList = fortuneBean.getDrawDateTime();

					List<Integer> pickedNumbers = fortuneBean
							.getPickedNumbers();
					pickNumStr = new StringBuilder("");
					for (int pickNum : pickedNumbers) {
						pickNumStr.append(pickNum + ",");
					}
					if (pickNumStr.length() > 0) {
						pickNumStr.deleteCharAt(pickNumStr.length() - 1);
					}

					int[] betAmtMulti = fortuneBean.getBetAmountMultiple();
					StringBuilder betAmtStr = new StringBuilder("");
					for (int betAmt : betAmtMulti) {
						betAmtStr.append(betAmt + ",");
						totalQuantity = totalQuantity + betAmt;
					}
					if (betAmtStr.length() > 0) {
						betAmtStr.deleteCharAt(betAmtStr.length() - 1);
					}

					expiryPeriod = Util.getGameMap().get(
							Util.getGameName(fortuneBean.getGame_no()))
							.getTicketExpiryPeriod();
					noOfDraws = fortuneBean.getNoOfDraws();
					totPurchaseAmt = fortuneBean.getTotalPurchaseAmt();

					int appHeight = 0;
					if (fortuneBean.getAdvMsg() != null) {
						appHeight = getAdvMsgs(fortuneBean.getAdvMsg(),
								topMsgsStr, bottomMsgsStr, appHeight);
					}

					if (fortuneBean.getRafflePurchaseBeanList() != null) {
						appHeight = getRaffData(fortuneBean
								.getRafflePurchaseBeanList(), raffleData,
								appHeight);
					}

					gameDependentData.append("|isQP=" + fortuneBean.getIsQP()
							+ "|betAmountMultiple=" + betAmtStr
							+ "|totalQuantity=" + totalQuantity);

				} else if (bean instanceof LottoPurchaseBean) {
					LottoPurchaseBean lottoBean = (LottoPurchaseBean) bean;
					gameName = "Zimlottotwo";
					tktNo = lottoBean.getTicket_no();
					rpcCount = lottoBean.getReprintCount();
					saleStatus = lottoBean.getSaleStatus();
					purchaseTime = lottoBean.getPurchaseTime();
					gameDispName = lottoBean.getGameDispName();
					drawDateList = lottoBean.getDrawDateTime();
					barcodeCount = lottoBean.getBarcodeCount();
					List<String> pickedNumbers = lottoBean.getPlayerPicked();
					pickNumStr = new StringBuilder("");
					for (String pickNum : pickedNumbers) {
						pickNumStr.append(pickNum + ";");
					}
					if (pickNumStr.length() > 0) {
						pickNumStr.deleteCharAt(pickNumStr.length() - 1);
					}

					/*
					expiryPeriod = Util.getGameMap().get(
							Util.getGameName(lottoBean.getGame_no()))
							.getTicketExpiryPeriod();
					*/
					expiryPeriod = Util.getGameMap().get(lottoBean.getGameId())
							.getTicketExpiryPeriod();
					noOfDraws = lottoBean.getNoOfDraws();
					totPurchaseAmt = lottoBean.getTotalPurchaseAmt();

					int appHeight = 0;
					if (lottoBean.getAdvMsg() != null) {
						appHeight = getAdvMsgs(lottoBean.getAdvMsg(),
								topMsgsStr, bottomMsgsStr, appHeight);
					}

					if (lottoBean.getRafflePurchaseBeanList() != null) {
						appHeight = getRaffData(lottoBean
								.getRafflePurchaseBeanList(), raffleData,
								appHeight);
					}
					if("Perm6".equalsIgnoreCase(lottoBean.getPlayType())){
						totalQuantity=lottoBean.getNoOfLines();
					}else{
					totalQuantity = lottoBean.getPanel_id().length;
					}
					StringBuilder quickPickStr = new StringBuilder("");
					for (int quickPick : lottoBean.getIsQuickPick()) {
						quickPickStr.append(quickPick + ",");
					}
					if (quickPickStr.length() > 0) {
						quickPickStr.deleteCharAt(quickPickStr.length() - 1);
					}

					gameDependentData.append("|isQuickPickArray=" + quickPickStr
							+ "|totalQuantity=" + totalQuantity + "|playType=" +lottoBean.getPlayType());
				} else if (bean instanceof KenoPurchaseBean) {
					KenoPurchaseBean kenoBean = (KenoPurchaseBean) bean;
					gameName = Util.getGameName(kenoBean.getGame_no());
					tktNo = kenoBean.getTicket_no();
					rpcCount = kenoBean.getReprintCount();
					saleStatus = kenoBean.getSaleStatus();
					purchaseTime = kenoBean.getPurchaseTime();
					gameDispName = kenoBean.getGameDispName();
					drawDateList = kenoBean.getDrawDateTime();
					expiryPeriod = Util.getGameMap().get(
							Util.getGameName(kenoBean.getGame_no()))
							.getTicketExpiryPeriod();
					noOfDraws = kenoBean.getNoOfDraws();
					totPurchaseAmt = kenoBean.getTotalPurchaseAmt();

					int appHeight = 0;
					if (kenoBean.getAdvMsg() != null) {
						appHeight = getAdvMsgs(kenoBean.getAdvMsg(),
								topMsgsStr, bottomMsgsStr, appHeight);
					}

					if (kenoBean.getRafflePurchaseBeanList() != null) {
						appHeight = getRaffData(kenoBean
								.getRafflePurchaseBeanList(), raffleData,
								appHeight);
					}

					String[] playerDataApp = kenoBean.getPlayerData();
					String[] playType = kenoBean.getPlayType();
					int[] noOfLines = kenoBean.getNoOfLines();
					double[] unitPrice = kenoBean.getUnitPrice();
					int[] betAmtMul = kenoBean.getBetAmountMultiple();
					int[] isQP = kenoBean.getIsQuickPick();
					double[] panelPrice = new double[playType.length];

					StringBuilder playerDataStr = new StringBuilder(" ");
					StringBuilder playTypeStr = new StringBuilder(" ");
					StringBuilder noOfLinesStr = new StringBuilder(" ");
					StringBuilder unitPriceStr = new StringBuilder(" ");
					StringBuilder isQPStr = new StringBuilder(" ");
					StringBuilder panelPriceStr = new StringBuilder(" ");

					if (playerDataApp.length > 0) {
						playerDataStr.deleteCharAt(playerDataStr.length() - 1);
						playTypeStr.deleteCharAt(playTypeStr.length() - 1);
						noOfLinesStr.deleteCharAt(noOfLinesStr.length() - 1);
						unitPriceStr.deleteCharAt(unitPriceStr.length() - 1);
						isQPStr.deleteCharAt(isQPStr.length() - 1);
						panelPriceStr.deleteCharAt(panelPriceStr.length() - 1);

						for (int panelId = 0; panelId < playerDataApp.length; panelId++) {
							playerDataStr.append(playerDataApp[panelId] + "~");
							playTypeStr.append(playType[panelId] + "~");
							noOfLinesStr.append(noOfLines[panelId] + "~");
							unitPriceStr
									.append((unitPrice[panelId] * betAmtMul[panelId])
											+ "~");
							isQPStr.append(isQP[panelId] + "~");
							panelPriceStr
									.append((unitPrice[panelId]
											* betAmtMul[panelId]
											* noOfLines[panelId] * noOfDraws)
											+ "~");

						}

						playerDataStr.deleteCharAt(playerDataStr.length() - 1);
						playTypeStr.deleteCharAt(playTypeStr.length() - 1);
						noOfLinesStr.deleteCharAt(noOfLinesStr.length() - 1);
						unitPriceStr.deleteCharAt(unitPriceStr.length() - 1);
						isQPStr.deleteCharAt(isQPStr.length() - 1);
						panelPriceStr.deleteCharAt(panelPriceStr.length() - 1);
					}

					pickNumStr = playerDataStr;

					gameDependentData.append("|isQP=" + isQPStr
							+ "|totalQuantity=" + noOfLinesStr + "|playType="
							+ playTypeStr + "|unitPriceStr=" + unitPriceStr
							+ "|panelPriceStr=" + panelPriceStr);
				}
				for (int i = 0; i < drawDateList.size(); i++) {
					drawDateTime.append(drawDateList.get(i).toString().split("&")[0] + ",");
				}
				if (drawDateTime.length() > 0) {
					drawDateTime.deleteCharAt(drawDateTime.length() - 1);
				}

				finalData.append("data=" + tktNo + rpcCount + (barcodeCount!=-1 && LMSFilterDispatcher.isBarCodeRequired?barcodeCount:"")+"|gameName="
						+ gameName + "|mode=Buy" + "|saleStatus=" + saleStatus
						+ promoOriginalData + "|reprintCount=" + rpcCount
						+ "|purchaseTime=" + purchaseTime + "|gameDispName="
						+ gameDispName + "|ticketNumber=" + tktNo
						+ "|drawDateTime=" + drawDateTime + "|pickedNumbers="
						+ pickNumStr + "|expiryPeriod=" + expiryPeriod
						+ "|noOfDraws=" + noOfDraws + "|totalPurchaseAmt="
						+ totPurchaseAmt + "|topAdvMsg=" + topMsgsStr
						+ "|bottomAdvMsg=" + bottomMsgsStr + "|raffleData="
						+ raffleData + gameDependentData);
			}
		}
		return finalData.toString();
	}

	public static String getOrgNameFromTktNo(String tktNum, String orgType) {
		if (tktNum != null
				&& (tktNum.length() == ConfigurationVariables.tktLenA || tktNum
						.length() == ConfigurationVariables.tktLenB)) {
			String userId = null;
			String orgName = null;
			if (tktNum.length() == ConfigurationVariables.tktLenA) {
				userId = String.valueOf(getUserIdFromTicket(tktNum));
			} else if (tktNum.length() == ConfigurationVariables.tktLenB) {
				//userId = tktNum.substring(0, ConfigurationVariables.retIdLenB);
				userId = String.valueOf(getUserIdFromTicket(tktNum));
			}
			if (userId != null) {
				orgName = AjaxRequestHelper.getOrgNameFromUserId(Integer
						.parseInt(userId), orgType);
				return orgName;
			}

		}

		return null;
	}

	/*public static String getOrgNameFromTktNoTemp(String tktNum, String orgType) {
		if (tktNum != null
				&& (tktNum.length() == ConfigurationVariables.tktLenA || tktNum
						.length() == ConfigurationVariables.tktLenB)) {
			String userId = null;
			String orgName = null;
			if (tktNum.length() == ConfigurationVariables.tktLenA) {
				userId =String.valueOf(getUserIdFromTicket(tktNum));
			} else if (tktNum.length() == ConfigurationVariables.tktLenB) {
				userId = tktNum.substring(0, ConfigurationVariables.retIdLenB);
			}
			if (userId != null) {
				orgName = AjaxRequestHelper.getOrgNameFromUserId(Integer
						.parseInt(userId), orgType);
				return orgName;
			}

		}

		return null;
	}*/
	
	
	
	public static Map<Integer, String> fetchActiveScratchGameList() {
		Map<Integer, String> gMap = new HashMap<Integer, String>();
		Connection con = DBConnect.getConnection();

		try {
			Statement pstmt = con.createStatement();
			ResultSet rs = pstmt
					.executeQuery("select game_nbr,game_name from st_se_game_master where game_status = 'OPEN'");
			while (rs.next()) {
				gMap.put(rs.getInt("game_nbr"), rs.getString("game_name"));
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return gMap;
	}

	public static int getGamenoFromTktnumber(String tktNum) {

		int retIdLen = 0;
		int gameNo = 0;
		int tktLen = 0;
		String tktBuf = null;

		if (tktNum != null
				&& (tktNum.length() == ConfigurationVariables.tktLenA || tktNum
						.length() == ConfigurationVariables.tktLenB)) {
			if (tktNum.length() == ConfigurationVariables.tktLenA) {
				tktLen = ConfigurationVariables.tktLenA;
				retIdLen = ConfigurationVariables.retIdLenA;
				tktBuf = tktNum.substring(retIdLen, retIdLen
						+ ConfigurationVariables.gameNoLenA);
			} else if (tktNum.length() == ConfigurationVariables.tktLenB) {
				tktLen = ConfigurationVariables.tktLenB;
				retIdLen = ConfigurationVariables.retIdLenB;
				tktBuf = tktNum.substring(retIdLen, retIdLen
						+ ConfigurationVariables.gameNoLenB);
			}
			gameNo = Integer.parseInt(tktBuf);
		}
		return gameNo;

	}
	
	public static int getserviceIdFromTktNumber(String tktNum) throws LMSException {

		int serviceId = 0;
		int randomDigits = 0;

		try{
			if (tktNum != null	&& (tktNum.length() == ConfigurationVariables.tktLenB)) {
				randomDigits = ConfigurationVariables.RANDOM_RET_ID_B_RAND_DIGIT;
				serviceId = Integer.parseInt(tktNum.substring(randomDigits, (randomDigits + ConfigurationVariables.gameNoLenB)));
			}else{
				throw new LMSException(LMSErrors.INVALID_TICKET_ERROR_CODE , LMSErrors.INVALID_TICKET_ERROR_MESSAGE);
			}
		}catch (LMSException e) {
			throw e;
		}catch (Exception e) {
			throw new LMSException(LMSErrors.GENERAL_EXCEPTION_ERROR_CODE , LMSErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		}
		return serviceId;
	}
	
	public static boolean checkDupRefTransId(String tpTxnId,int userOrgId){
		String fetchRefTransId = "select count(*) from st_lms_tp_txn_mapping where tp_ref_txn_id="+tpTxnId+" and retailer_org_id="+userOrgId;
		boolean isDuplicate=true;
		
		try{
			Connection con = DBConnect.getConnection();
			
			/*pstmt = con.prepareStatement("Select lms_txn_id from st_lms_tp_txn_mapping where tp_ref_txn_id = "+tpTxnId);
			ResultSet rs = pstmt.executeQuery();
			if(rs.next()){//tp txn id already exists in DB
				return -1;
			}*/
			Statement stmt = con.createStatement();
			ResultSet rs=stmt.executeQuery(fetchRefTransId);
			
			if(rs.next()){
				if(rs.getInt(1)==0){
					isDuplicate=false;
				}
				}
			DBConnect.closeCon(con);
		}catch (Exception e) {
			e.printStackTrace();
			return true;
		}
		
		return isDuplicate;
	}

	public static String getTickNumberFromTransTickNo(long transTickNumber,
			int gameId, String query, Connection con) throws SQLException,
			LMSException {
		
		String ticketNumber = "";
		Statement stmt = con.createStatement();
		ResultSet rs = stmt.executeQuery(query);
		if (rs.next()) {
			ticketNumber = rs.getString("ticket_nbr")  + getRpcAppenderForTickets(rs.getString("ticket_nbr").length());
		}
		return ticketNumber;
	}
	
	
	public static int getMappingIdFromTxId(Timestamp txDateDate, int userId,
			Connection con) throws Exception {
		
		int mappingId = 0;
		ResultSet rs = null;
		con = DBConnect.getConnection();
		PreparedStatement pstmt = con.prepareStatement("select user_mapping_id from st_lms_user_random_id_mapping_history where user_id  = ? and date(mapping_id_gen_date)<= date(?) order by mapping_id_gen_date DESC limit 1");
		pstmt.setInt(1, userId);
		pstmt.setTimestamp(2, txDateDate);
		rs = pstmt.executeQuery();
		if (rs.next()) {
			mappingId = rs.getInt("user_mapping_id");
		} else {
			throw new LMSException(); // TEMP
		}
		return mappingId;
	}
	
	public static int storeTPTxnId(int userOrgId,String lmsTxnId,String tpTxnId,String mobileNo){
		String query = "insert into st_lms_tp_txn_mapping (retailer_org_id,lms_txn_id,tp_ref_txn_id,mobile_no) values (?,?,?,?)";
		int rowInserted = 0;
		PreparedStatement pstmt  = null;
		try{
			Connection con = DBConnect.getConnection();
			con.setAutoCommit(false);
			/*pstmt = con.prepareStatement("Select lms_txn_id from st_lms_tp_txn_mapping where tp_ref_txn_id = "+tpTxnId);
			ResultSet rs = pstmt.executeQuery();
			if(rs.next()){//tp txn id already exists in DB
				return -1;
			}*/
			pstmt  = con.prepareStatement(query);
			pstmt.setInt(1, userOrgId);
			pstmt.setString(2, lmsTxnId);
			pstmt.setString(3, tpTxnId);
			pstmt.setString(4, mobileNo);
			logger.debug("TP API Insertion query >>"+pstmt);
			rowInserted = pstmt.executeUpdate();
			if(rowInserted > 0 ){
				con.commit();
			}
			DBConnect.closeCon(con);
		}catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
		
		return rowInserted;
	}
	
	public static int insertLastSoldTicketTeminal(int userOrgId,String ticketNo,int gameId,Connection con) throws LMSException{
		
		//needs to be add new column interface type in st_dg_terminal_sale_ticket_gameId
		String query = "insert into st_dg_terminal_sale_ticket_" + gameId +" (Retailer_org_id,ticket_nbr,purchase_time) values (?,?,?)";
		int rowInserted = 0;
		PreparedStatement pstmt  = null;
		try{
			
			pstmt  = con.prepareStatement(query);
			pstmt.setInt(1,userOrgId);
			//pstmt.setString(2, ticketNo.substring(0, ticketNo.length() - 2));
			pstmt.setString(2, ticketNo);
			pstmt.setString(3, Util.getCurrentTimeString());
			
			logger.debug("Last Sold ticket Insertion query >>"+pstmt);
			rowInserted = pstmt.executeUpdate();
			
		}catch (Exception e) {
			e.printStackTrace();
			throw new LMSException("");
		}
		
		return rowInserted;
	}
	
	public static String sendMsgToUsers(String phnNo, String msg){
		String finalResp = "";
		String urlName = null;
		String urlData = null;
		try{
			String smsAPICountry = (String) ServletActionContext.getServletContext().getAttribute("SMS_API_COUNTRY");
			if (smsAPICountry != null && !"NULL".equalsIgnoreCase(smsAPICountry)) {
				if("KENYA".equalsIgnoreCase(smsAPICountry)){
					//urlName = "http://push1.maccesssmspush.com/servlet/com.aclwireless.pushconnectivity.listeners.TextListener";
					//urlData = "userId:aclint1|pass:aclint1|appid:aclint1|subappid:aclint1|msgtype:1|contenttype:1|selfid:true|to:"+phnNo+"|from:DEMO|dlrreq:true|text:"+msg;
					String userName = "demoint";
					String pwd = "demoint1981";
					
					urlName = "http://203.122.58.168/prepaidgetbroadcast/PrepaidGetBroadcast";
					urlData = "userid:"+userName+"|pwd:"+pwd+"|msgtype:s|ctype:1|sender:sender|pno:"+phnNo+"|msgtxt:"+msg+"|alert:0";
					
					String apiResp = callSMSAPI(urlName, urlData);
					logger.debug("SMS API Response:" + apiResp);
					
					finalResp = formatSMSResponse(apiResp, smsAPICountry);
					logger.debug("SMS Foramatted Response:" + finalResp);
				} else if("ZIMBABWE".equalsIgnoreCase(smsAPICountry)){
					
				}
			}
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return finalResp;
	}
	
	public static String callSMSAPI(String urlName, String urlData){
		String result = "";
		try {
			String dataArr[] = urlData.split("\\|");
			StringBuilder urlStr = new StringBuilder("");
			for (String data : dataArr) {
				String encodeData[] = data.split(":");
				urlStr.append(URLEncoder.encode(encodeData[0], "UTF-8") + "="
						+ URLEncoder.encode(encodeData[1], "UTF-8") + "&");
			}
			urlStr.deleteCharAt(urlStr.length() - 1);
		
			// Send data
			URL url = new URL(urlName);
			URLConnection conn = url.openConnection();
			conn.setDoOutput(true);
			OutputStreamWriter wr = new OutputStreamWriter(conn
					.getOutputStream());
			wr.write(urlStr.toString());
			wr.flush();
		
			// Get the response
			BufferedReader rd = new BufferedReader(new InputStreamReader(conn
					.getInputStream()));
			result = rd.readLine();
			rd.close();
			wr.close();
			logger.debug("URL Name:" + urlName);
			logger.debug("URL Data:" + urlData);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	public static String formatSMSResponse(String apiResp, String smsAPICountry){
		String finalResp = null;
		
		finalResp = apiResp;
		
		return finalResp;
	}
	
	public static List<String> rec(int start, int elementToChoose,
			int returnCnt, String[] indexArr, String[] elements,
			StringBuffer stbuff, List<String> comboList) {

		if (returnCnt == elementToChoose) {
			return comboList;
		}
		returnCnt++;
		for (int i = start; i < elements.length; i++) {

			indexArr[returnCnt - 1] = "" + i;
			if (returnCnt == elementToChoose) {

				stbuff = new StringBuffer();
				for (String element : indexArr) {
					stbuff.append("," + elements[Integer.parseInt(element)]);
				}
				stbuff.delete(0, 1);
				comboList.add(stbuff.toString());
				comboList.add("Nxt");
			}

			rec(++start, elementToChoose, returnCnt, indexArr, elements,
					stbuff, comboList);
		}
		return comboList;
	}

	// NEW CODE SHOBHIT
	public static Map<String, List<String>> getDGSaleAdvMessage(int orgId, int gameId) throws SQLException
	{
		Map<String, List<String>> msgMap = new HashMap<String, List<String>>();

		Set<MessageDetailsBean> msgSet = new HashSet<MessageDetailsBean>();
		if(advMsgDataMap.get(orgId) != null && advMsgDataMap.get(orgId).get(gameId) != null)
			msgSet.addAll(advMsgDataMap.get(orgId).get(gameId));
		if(advMsgDataMap.get(-1) != null && advMsgDataMap.get(-1).get(gameId) != null)
			msgSet.addAll(advMsgDataMap.get(-1).get(gameId));

		String msgLocation = null;
		for(MessageDetailsBean bean : msgSet)
		{
			msgLocation = bean.getMessageLocation();
			if(msgMap.containsKey(msgLocation))
			{
				msgMap.get(msgLocation).add(bean.getMessageText());
			}
			else
			{
				List<String> tempList = new ArrayList<String>();
				tempList.add(bean.getMessageText());
				msgMap.put(msgLocation, tempList);
			}
		}

		return msgMap;
	}

	
	public static Map<String, List<String>> getAdvMessage(int orgId, int gameId,
			String forOrgType, String activity, String serviceType) throws SQLException {
		Map<String, List<String>> msgMap = new HashMap<String, List<String>>();
		Connection con = DBConnect.getConnection();
		
		try{
			msgMap=getAdvMessage(orgId,gameId,forOrgType,activity,serviceType,con);
		}catch (SQLException se) {
			
		}finally{
			DBConnect.closeCon(con);
		}
		
		return msgMap;
	}
	
	
	@SuppressWarnings("unchecked")
	public static Map<String, List<String>> getAdvMessage(int orgId, int gameId,
			String forOrgType, String activity, String serviceType,Connection con) throws SQLException {
		Map<String, List<String>> msgMap = new HashMap<String, List<String>>();
		int serviceId = ((Map<String, Integer>)LMSUtility.sc.getAttribute("SERVICES_CODE_ID_MAP")).get(serviceType);
		
		Statement drawStmt = con.createStatement();
		String msgLocation = null;
		String query = "select msg_text,org_id,game_id,activity,msg_location  from st_dg_adv_msg_org_mapping mop,st_dg_adv_msg_master mm where (org_id="
				+ orgId
				+ " or org_id=-1) and mm.msg_id = mop.msg_id and mm.status='ACTIVE'  and service_id="+serviceId+"  and msg_for='"
				+ forOrgType + "'";
		StringBuilder whereClause = new StringBuilder("");
		if (gameId != 0) {
			whereClause.append(" and mop.game_id=" + gameId);
		}
		if (activity != null) {
			whereClause.append(" and (mm.activity='" + activity
					+ "' or mm.activity='ALL')");
		}
		logger.debug(query + whereClause);
		ResultSet retRs = drawStmt.executeQuery(query + whereClause);
		while (retRs.next()) {
			msgLocation = retRs.getString("msg_location");
			if (msgMap.containsKey(msgLocation)) {
				msgMap.get(msgLocation).add(retRs.getString("msg_text"));
			} else {
				List<String> tempList = new ArrayList<String>();
				tempList.add(retRs.getString("msg_text"));
				msgMap.put(retRs.getString("msg_location"), tempList);
			}
		}
		
		return msgMap;
	}
	
	
	public static void setHeartBeatAndSaleTime(int orgId,String taskName,Connection con) {		
		PreparedStatement pstmt = null;
		String query =null;
		try{
			if(taskName.equals("SALE")){
				query = "update st_lms_ret_offline_master set last_HBT_time=?,dg_last_sale_time=? where organization_id=?";
			} else if(taskName.equals("PWT")){
				query = "update st_lms_ret_offline_master set last_HBT_time=?,dg_last_pwt_time=? where organization_id=?";
			}else if(taskName.equals("OLA_DEP")){
				query = "update st_lms_ret_offline_master set last_HBT_time=?,ola_last_deposit_time=? where organization_id=?";
			}else if(taskName.equals("OLA_WITH")){
				query = "update st_lms_ret_offline_master set last_HBT_time=?,ola_last_withdrawal_time=? where organization_id=?";
			}else if(taskName.equals("SLE_SALE")){
				query = "update st_lms_ret_offline_master set last_HBT_time=?,sle_last_sale_time=? where organization_id=?";
			}else if(taskName.equals("SLE_PWT")){
				query = "update st_lms_ret_offline_master set last_HBT_time=?,sle_last_pwt_time=? where organization_id=?";
			} else if ("IW_SALE".equals(taskName)) {
				query = "update st_lms_ret_offline_master set last_HBT_time=?,iw_last_sale_time=? where organization_id=?";
			} else if ("IW_PWT".equals(taskName)) {
				query = "update st_lms_ret_offline_master set last_HBT_time=?, iw_last_pwt_time=? where organization_id=?";
			}else if ("VS_SALE".equals(taskName)) {
				query = "update st_lms_ret_offline_master set last_HBT_time=?, vs_last_sale_time=? where organization_id=?";
			} else if ("VS_PWT".equals(taskName)) {
				query = "update st_lms_ret_offline_master set last_HBT_time=?, vs_last_pwt_time=? where organization_id=?";
			}
			pstmt = con.prepareStatement(query);
			pstmt.setTimestamp(1, Util.getCurrentTimeStamp());
			pstmt.setTimestamp(2, Util.getCurrentTimeStamp());
			pstmt.setInt(3, orgId);
			pstmt.executeUpdate();			
		}catch(SQLException e){
			logger.error(e);
			e.printStackTrace();
		}
	}
	
	public static int getUserIdFromTicket(String ticketNumber) {
		int userId=0;
		try{
		userId = getUserIDForTicketNumber(ticketNumber); 
		}catch (Exception e) {
			logger.error("Exception : - " , e);
		}
		return userId;
	}

	public static int getUserIdFromTicket(String ticketNumber, Connection connection) {
		int userId = 0;
		try {
			userId = getUserIDForTicketNumber(ticketNumber, connection); 
		} catch (Exception e) {
			logger.error("Exception : - " , e);
		}

		return userId;
	}

	/**
	 * 
	 * @param ticketNumber
	 * @return
	 * @throws LMSException
	 */
	private static int getUserIDForTicketNumber(String ticketNumber) throws LMSException {
		int userId = 0;
		Connection con = null;
		try {
			 con = DBConnect.getConnection();
			 userId = getUserIDForTicketNumber(ticketNumber, con);
		}catch (LMSException e) {
			logger.error("LMSException :- " , e);
			throw e;
		}catch (Exception e) {
			logger.error("Exception :- " , e);
			throw new LMSException(LMSErrors.GENERAL_EXCEPTION_ERROR_CODE , LMSErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		}finally{
		DBConnect.closeCon(con);
	}
		return userId;
	}

	private static int getUserIDForTicketNumber(String ticketNumber, Connection connection) throws LMSException {

		int userId = 0;
		int gameId = 0;
		int gameNbr = 0;
		int serviceId = 0; 
		String serviceCode = null;
		
		String tktNbr = null;
		String checkQuery = null;
		Timestamp tktFormatChngDate = null;
		
		ResultSet rs = null;
		PreparedStatement pstmt = null;
		try {
			tktFormatChngDate = new Timestamp(new SimpleDateFormat("yyyy-MM-dd").parse(Utility.getPropertyValue("USER_MAPPING_ID_DEPLOYMENT_DATE")).getTime());
			 tktNbr =  getTicketNumber(ticketNumber, 3);
			 if("".equalsIgnoreCase(tktNbr) || "ERROR".equalsIgnoreCase(tktNbr)) 
				 throw new LMSException(LMSErrors.INVALID_TICKET_ERROR_CODE,LMSErrors.INVALID_TICKET_ERROR_MESSAGE);
			 
			 serviceId = getserviceIdFromTktNumber(tktNbr);
			 // GET SERVICE CODE FROM SERVICE ID AND DECIDE THE SALE TABLE  if DG then , st_dg_ret_sale_GAMEID
			 serviceCode = getServiceCodeFromId(serviceId , connection);

			 if(!"SLE".equals(serviceCode)) {
				 gameNbr = getGamenoFromTktnumber(tktNbr);
				 gameId = Util.getGameIdFromGameNumber(gameNbr);
				 if(gameId == 0)
					 throw new LMSException(LMSErrors.INVALID_TICKET_ERROR_CODE,LMSErrors.INVALID_TICKET_ERROR_MESSAGE);
			 	}
			 
			 if(tktFormatChngDate.before(getDateOfTransaction(tktNbr))){

				 if(serviceCode == null){
					throw new LMSException(LMSErrors.INVALID_SERVICE_ON_TICKET_ERROR_CODE , LMSErrors.INVALID_SERVICE_ON_TICKET_ERROR_MESSAGE);
				 }else if(serviceCode.equalsIgnoreCase("DG")){
					checkQuery = "select retailer_user_id user_id , transaction_date from st_dg_ret_sale_? sale , st_lms_retailer_transaction_master tx where sale.transaction_id = tx.transaction_id and ticket_nbr = ?";
					pstmt = connection.prepareStatement(checkQuery);
					pstmt.setInt(1, gameId);
					pstmt.setString(2 , tktNbr.substring(0,tktNbr.length()-1));
				 }else if("SLE".equals(serviceCode)){
					checkQuery = "select retailer_user_id user_id from st_sle_ret_sale sale inner join st_lms_retailer_transaction_master tx on sale.transaction_id = tx.transaction_id and ticket_nbr = ?";
					pstmt = connection.prepareStatement(checkQuery);
					pstmt.setString(1 , tktNbr.substring(0,tktNbr.length()-1));
				 }else{
					 throw new LMSException(LMSErrors.INVALID_SERVICE_ON_TICKET_ERROR_CODE ,LMSErrors.INVALID_SERVICE_ON_TICKET_ERROR_MESSAGE);
				 }

				rs = pstmt.executeQuery();
				if(rs.next()){
					userId =  rs.getInt("user_id");
				}else{
					throw new LMSException(LMSErrors.INVALID_TICKET_ERROR_CODE,LMSErrors.INVALID_TICKET_ERROR_MESSAGE);
				}
			 }else{
				 if (ticketNumber.length() ==ConfigurationVariables.currentTktLen && ticketNumber.length() == ConfigurationVariables.tktLenB) {
						userId=Integer.parseInt(ticketNumber.substring(0,ConfigurationVariables.retIdLenB));
					}else{
						throw new LMSException(LMSErrors.INVALID_TICKET_ERROR_CODE,LMSErrors.INVALID_TICKET_ERROR_MESSAGE);
					}
				}
		}catch (LMSException e) {
			logger.error("LMSException :- " , e);
			throw e;
		}catch (SQLException e) {
			logger.error("SQLException :- " , e);
			throw new LMSException(LMSErrors.SQL_EXCEPTION_ERROR_CODE , LMSErrors.SQL_EXCEPTION_ERROR_MESSAGE);
		}catch (Exception e) {
			logger.error("Exception :- " , e);
			throw new LMSException(LMSErrors.GENERAL_EXCEPTION_ERROR_CODE , LMSErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		}finally{
			DBConnect.closeConnection(pstmt, rs);
		}
		return userId;
	}

	public static String getServiceCodeFromId(int serviceId , Connection con){

		ResultSet rs = null;
		Statement stmt = null;
		String serviceCode = null;
		try {
			
			stmt = con.createStatement();
			rs = stmt.executeQuery("select SQL_CACHE service_code from st_lms_service_master where service_id = "+serviceId);
			if(rs.next()){
				serviceCode = rs.getString("service_code");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			DBConnect.closeConnection(stmt, rs);
		}

		return serviceCode;
	}
	
	public static String getTicketNumberFormat(String ticketNumber) throws LMSException{
		Timestamp tktFormatChngDate = null;
		
		try{
				tktFormatChngDate = new Timestamp(new SimpleDateFormat("yyyy-MM-dd").parse(Utility.getPropertyValue("USER_MAPPING_ID_DEPLOYMENT_DATE")).getTime());
				 if(tktFormatChngDate.before(getDateOfTransaction(ticketNumber))){
					 return "NEWTKTFORMAT";
				 }else{
					 return "OLDTKTFORMAT";
				 }			
			
		}catch(LMSException e){
			logger.error("LMSException :- " , e);
			throw e;
		}catch(Exception e){
			logger.error("LMSException :- " , e);
			throw new LMSException(LMSErrors.GENERAL_EXCEPTION_ERROR_CODE , LMSErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		}
	}
	
	public static Timestamp getDateOfTransaction(String tktNum) throws LMSException {
		int day = 0;
		Timestamp dateOfTransaction = null;
		if (tktNum != null && tktNum.length() == ConfigurationVariables.tktLenB) {
			int range = ConfigurationVariables.retIdLenB+ ConfigurationVariables.gameNoLenB;
			day = Integer.parseInt(tktNum.substring(range, (range+3)));
			Calendar cal = Calendar.getInstance();
			int expiryPeriod = cal.get(Calendar.DAY_OF_YEAR) - day;
			if (expiryPeriod < 0) {
				cal.set(cal.get(Calendar.YEAR) - 1, 1, 1);
				cal.set(Calendar.DAY_OF_YEAR, day);
			}else{
				cal.set(Calendar.DAY_OF_YEAR, day);
			}
			cal.set(Calendar.HOUR, 00);
			cal.set(Calendar.MINUTE, 00);
			cal.set(Calendar.SECOND, 00);
			dateOfTransaction = new Timestamp(cal.getTimeInMillis());
			/*if (day > 366) {
				throw new LMSException(LMSErrors.INVALID_TICKET_ERROR_CODE , LMSErrors.INVALID_TICKET_ERROR_MESSAGE);
			} else {
				Calendar cal = Calendar.getInstance();
				int expiryPeriod = cal.get(Calendar.DAY_OF_YEAR) - day;
				if (expiryPeriod < 0) {
					cal.set(cal.get(Calendar.YEAR) - 1, 1, 1);
					cal.set(Calendar.DAY_OF_YEAR, day);
				}else{
					cal.set(Calendar.DAY_OF_YEAR, day);
				}
				cal.set(Calendar.HOUR, 00);
				cal.set(Calendar.MINUTE, 00);
				cal.set(Calendar.SECOND, 00);
				dateOfTransaction = new Timestamp(cal.getTimeInMillis());
			}*/
		}else{
			throw new LMSException(LMSErrors.INVALID_TICKET_ERROR_CODE , LMSErrors.INVALID_TICKET_ERROR_MESSAGE);
		}
		return dateOfTransaction;
	}
	
	public static boolean canClaimAnywhere(String ticketNo, boolean isAgent,
			int userOrgId) {

		int userId = 0;
		int organizationId = 0;
		boolean canClaim = false;
		
		ResultSet rs = null;
		Connection con = null;
		PreparedStatement pstmt = null;
		try {

			userId = Util.getUserIdFromTicket(ticketNo);
			String retQuery = "(select organization_id from st_lms_user_master where user_id="
					+ userId + ")";
			String agtQuery = "(select parent_id organization_id from st_lms_organization_master where organization_id="
					+ retQuery + ")";

			con = DBConnect.getConnection();
			pstmt = con
					.prepareStatement("select claim_any_ticket from st_lms_oranization_limits where organization_id=?");
			pstmt.setInt(1, userOrgId);
			rs = pstmt.executeQuery();

			if (rs.next() && rs.getBoolean(1)) {
				canClaim = true;
			} else {
				if (isAgent)
					pstmt = con.prepareStatement(agtQuery);
				else
					pstmt = con.prepareStatement(retQuery);

				rs = pstmt.executeQuery();
				while (rs.next()) {
					organizationId = rs.getInt(1);
				}
				if (organizationId == userOrgId)
					canClaim = true;
			}
		} catch (SQLException e) {
			logger.error("SQLException" , e);
		}catch (Exception e) {
			logger.error("Exception" , e);
		}finally{
		DBConnect.closeConnection(con, pstmt, rs);
	}
		return canClaim;
	}
	
	public static String getTicketNumber(String ticketNumber, int InpType) {
		String originalTicket = "";
		if (ticketNumber != null && ticketNumber != "") {
			int ticketLength = ticketNumber.length();
			switch (InpType) {
			case 1:
				if (ticketLength == com.skilrock.lms.common.ConfigurationVariables.barcodeCount) {
					originalTicket = Util.getTicketNumberForBarCode(ticketNumber);
				}
				break;
			case 2:
				if (ticketLength == ConfigurationVariables.tktLenA
						|| ticketLength == ConfigurationVariables.tktLenB) 
					originalTicket = ticketNumber;
				
				break;
			case 3:
				if (ticketLength == com.skilrock.lms.common.ConfigurationVariables.barcodeCount) {
					originalTicket = Util.getTicketNumberForBarCode(ticketNumber);
				} else if (ticketLength == ConfigurationVariables.tktLenA
						|| ticketLength == ConfigurationVariables.tktLenB) {
					originalTicket = ticketNumber;
				}
				break;
			default:
				originalTicket = "ERROR";
				break;
			}
		}
		return originalTicket;
	}
	
	public static String getTicketNumberForBarCode(String ticketNumber) {
		return ticketNumber.substring(0, com.skilrock.lms.common.ConfigurationVariables.barcodeCount
				- com.skilrock.lms.common.ConfigurationVariables.barcodeRandomLength);
	}
	
	public static String getBarCodeCountFromTicketNumber(String ticketNumber) {
		return ticketNumber.substring(com.skilrock.lms.common.ConfigurationVariables.barcodeCount
				- com.skilrock.lms.common.ConfigurationVariables.barcodeRandomLength,com.skilrock.lms.common.ConfigurationVariables.barcodeCount);
	}
	
	public static String getBalInString(double bal){
		NumberFormat nf = NumberFormat.getInstance();
		nf.setMinimumFractionDigits(2);
		return nf.format(bal).replaceAll(",", "");
	}
	
	public static String getCurrentTimeString(){
		return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime());
	}
	
	public static Timestamp getCurrentTimeStamp(){
		return new Timestamp(Calendar.getInstance().getTimeInMillis());
	}
	
	public static String getDateTimeFormat(Timestamp dateTime){
		SimpleDateFormat sd=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return sd.format(dateTime);
	}
	
	public static long id=0;
	public static synchronized long getAuditTrailId(){
		
		return id++;
	}
	private static long activityId=0;
	public static synchronized long getActivityId(){
		
		return ++activityId;
	}
	public static Long getCheckSum(String chkSumStr){
		byte bytes[] = chkSumStr.getBytes();
       Checksum checksum = new CRC32();
        checksum.update(bytes,0,bytes.length);
        Long lngChecksum = checksum.getValue();
        return lngChecksum;
	}
	
	public static void setRespGamingCriteriaStatusMap(){

		Connection con = DBConnect.getConnection();
		Statement stmt = null;
		ResultSet rs = null;
		ResponsibleGamingBean respCritBean=null;
		try {
			respGamingCriteriaStatusMap=new HashMap<String, ResponsibleGamingBean>();
			stmt = con.createStatement();
			rs = stmt
					.executeQuery("select criteria_type,criteria,criteria_limit,crit_status,organization_type,crit_action from st_lms_rg_criteria_limit");
			while (rs.next()) {
					respCritBean=new ResponsibleGamingBean();
					respCritBean.setCriteriaLimit((rs.getDouble("criteria_limit")));
					respCritBean.setStatus(rs.getString("crit_status"));
					respCritBean.setOrgType(rs.getString("organization_type"));
					respCritBean.setCriAction((rs.getString("crit_action")));
					respGamingCriteriaStatusMap.put(rs.getString("criteria")+rs.getString("criteria_type"), respCritBean);
					
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			// throw new LMSException(e);
		} finally {
			try {
				if (con != null && !con.isClosed()) {
					con.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
	}
	public static int fetchUserIdFormOrgId(int orgId) {
		int userId = 0;
		Connection con = DBConnect.getConnection();
		Statement stmt = null;
		ResultSet rs = null;
		try {
			stmt = con.createStatement();
			rs = stmt
					.executeQuery("select user_id from st_lms_user_master where organization_id = "
							+ orgId);
			while (rs.next()) {
				userId = rs.getInt("user_id");
			}
		} catch (Exception e) {
			e.printStackTrace();
			// throw new LMSException(e);
		} finally {
			try {
				if (con != null && !con.isClosed()) {
					con.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return userId;
	}
	
	public static int getDivValueForLastSoldTkt(int lastTktNoLen){
		return (lastTktNoLen==ConfigurationVariables.tktLenA?100:10);
	}
	
	public static String getRpcAppenderForTickets(int ticketNoLen){
		return (ticketNoLen==14?"00":"0");
	}
	
	public static String getTktWithoutRpcNBarCodeCount(String ticketNumber , int len){
		return len==ConfigurationVariables.barcodeCount?ticketNumber.substring(0, len - 3): 
						(len==ConfigurationVariables.tktLenB?ticketNumber.substring(0, len - 1):
																ticketNumber.substring(0, len - 2));
	}

	public static int getRowCount(ResultSet resultSet) {
		if (resultSet == null) {
			return 0;
		}
		try {
			resultSet.last();
			return resultSet.getRow();
		} catch (SQLException exp) {
			exp.printStackTrace();
		} finally {
			try {
				resultSet.beforeFirst();
			} catch (SQLException exp) {
				exp.printStackTrace();
			}
		}
		return 0;
	}
	
	public static boolean isBoAuthorizedToClaimTicket(String ticketNo) {

		int userId = 0;
		int organizationId = 0;
		boolean canClaim=false;
		ResultSet rs = null;
		Connection con = null;
		String tktNbr = null;
		PreparedStatement pstmt = null;
		try {
			tktNbr = Util.getTicketNumber(ticketNo, 3); 
			userId = Util.getUserIdFromTicket(tktNbr);
			con = DBConnect.getConnection();
			pstmt=con.prepareStatement("select parent_id organization_id from st_lms_organization_master where organization_id=(select organization_id from st_lms_user_master where user_id=?)");
			pstmt.setInt(1, userId);
			rs = pstmt.executeQuery();
			if (rs.next()) 
				organizationId = rs.getInt("organization_id");
			else
				return true;
			
			pstmt=con.prepareStatement("select is_acting_as_bo_for_pwt from st_lms_oranization_limits where organization_id=?");
			pstmt.setInt(1, organizationId);
			rs = pstmt.executeQuery();
			
			if (rs.next()) 
				canClaim = rs.getBoolean("is_acting_as_bo_for_pwt");
		
		} catch (SQLException e) {
			e.printStackTrace();
	}
		finally{
			DBConnect.closeConnection(con, pstmt, rs);
		}
		return canClaim;
	}
	
	// sets The category map , not for any other purpose.
	public static void setCategoryMapDetails() {

			ResultSet rs = null;
			Connection con = null;
			Statement stmt = null;
			try {
				con = DBConnect.getConnection();
				stmt = con.createStatement();
				rs = stmt.executeQuery("select category_id,category_code from st_cs_product_category_master");
				catMap = new HashMap<Integer, String>();
				while (rs.next())
					catMap.put(rs.getInt("category_id"), rs.getString("category_code"));
			} catch (Exception e) {
				logger.error("Exception :- " + e);
			}finally{
				DBConnect.closeConnection(con, stmt, rs);
			}
	}
	
	public static String getCategoryName(int categoryId){
		return catMap.get(categoryId);
	}

	
	public static void setServiceCodeIdMap() {
		ResultSet rs = null;
		Connection con = null;
		Statement stmt = null;
		try {
			con = DBConnect.getConnection();
			stmt = con.createStatement();
			rs = stmt.executeQuery("select service_code, service_id from st_lms_service_master where status='ACTIVE' and service_code<>'MGMT'");
			serviceCodeIDMap = new HashMap<String, Integer>();
			while (rs.next()){
				serviceCodeIDMap.put(rs.getString("service_code")  , rs.getInt("service_id"));
			}
		} catch (SQLException e) {
			logger.error("SQLException :- ",e);
		}catch (Exception e) {
			logger.error("Exception :- ",e);
		}finally{
			DBConnect.closeConnection(con, stmt, rs);
		}
	}
	public static int getServiceIdFormCode(String serviceCode){
		int serviceId = 0;
		if(serviceCodeIDMap.containsKey(serviceCode)){
			serviceId =  serviceCodeIDMap.get(serviceCode);
		}
		return serviceId;
	}
	
	public static int getLen(int id){
		int length = (int)(Math.log10(id)+1);
		return length;
	} 

	public static int getLen(long id){
		int length = (int)(Math.log10(id)+1);
		return length;
	} 

	public static boolean canClaimRetailer(String ticketNumber, int retOrgId, int parentOrgId, Connection connection) {
		boolean canClaim = false;
		try {
			int userId = Util.getUserIdFromTicket(ticketNumber, connection);
			canClaim = canClaimRetailer(userId, retOrgId, parentOrgId, connection);
		} catch (Exception e) {
			logger.error("Exception - ", e);
		}

		return canClaim;
	}

	public static boolean canClaimRetailerIW(int userId, int retOrgId, int parentOrgId, Connection connection) {
		int ticketRetOrgId = 0;
		int ticketParentOrgId = 0;
		String selfClaim = null;
		String otherClaim = null;
		String claimAtSelfRet = null;
		String claimAtOtherRetSameAgt = null;
		String claimAtOtherRet = null;
		boolean canClaim = false;

		Statement stmt = null;
		ResultSet rs = null;
		try {
			stmt = connection.createStatement();

			String query = "SELECT organization_id FROM st_lms_user_master WHERE user_id="+userId;
			rs = stmt.executeQuery(query);
			if(rs.next()) {
				ticketRetOrgId = rs.getInt("organization_id");
			}

//			query = "SELECT self_claim, other_claim FROM st_lms_oranization_limits WHERE organization_id="+retOrgId;
//			rs = stmt.executeQuery(query);
//			while(rs.next()) {
//				selfClaim = rs.getString("self_claim");
//				otherClaim = rs.getString("other_claim");
//			}
//
//			query = "SELECT claim_at_self_ret, claim_at_other_ret_same_agt, claim_at_other_ret FROM st_lms_ret_sold_claim_criteria WHERE organization_id="+ticketRetOrgId;
//			rs = stmt.executeQuery(query);
//			while(rs.next()) {
//				claimAtSelfRet = rs.getString("claim_at_self_ret");
//				claimAtOtherRetSameAgt = rs.getString("claim_at_other_ret_same_agt");
//				claimAtOtherRet = rs.getString("claim_at_other_ret");
//			}
//
//			if ("NO".equalsIgnoreCase(selfClaim) && "NO".equalsIgnoreCase(otherClaim)) {
//				return false;
//			}

//			query = "SELECT parent_id FROM st_lms_organization_master WHERE organization_id="+ticketRetOrgId;
//			rs = stmt.executeQuery(query);
//			if(rs.next()) {
//				ticketParentOrgId = rs.getInt("parent_id");
//			}

			if (ticketRetOrgId == retOrgId) {
				canClaim = true;
			} else {
				canClaim = false;
			}
		} catch (SQLException se) {
			logger.error("SQLException - ", se);
		} catch (Exception e) {
			logger.error("Exception - ", e);
		} finally {
			DBConnect.closeStmt(stmt);
			DBConnect.closeRs(rs);
		}

		return canClaim;
	}
	
	public static boolean canClaimRetailer(int userId, int retOrgId, int parentOrgId, Connection connection) {
		int ticketRetOrgId = 0;
		int ticketParentOrgId = 0;
		String selfClaim = null;
		String otherClaim = null;
		String claimAtSelfRet = null;
		String claimAtOtherRetSameAgt = null;
		String claimAtOtherRet = null;
		boolean canClaim = false;

		Statement stmt = null;
		ResultSet rs = null;
		try {
			stmt = connection.createStatement();

			String query = "SELECT organization_id FROM st_lms_user_master WHERE user_id="+userId;
			rs = stmt.executeQuery(query);
			if(rs.next()) {
				ticketRetOrgId = rs.getInt("organization_id");
			}

			query = "SELECT self_claim, other_claim FROM st_lms_oranization_limits WHERE organization_id="+retOrgId;
			rs = stmt.executeQuery(query);
			while(rs.next()) {
				selfClaim = rs.getString("self_claim");
				otherClaim = rs.getString("other_claim");
			}

			query = "SELECT claim_at_self_ret, claim_at_other_ret_same_agt, claim_at_other_ret FROM st_lms_ret_sold_claim_criteria WHERE organization_id="+ticketRetOrgId;
			rs = stmt.executeQuery(query);
			while(rs.next()) {
				claimAtSelfRet = rs.getString("claim_at_self_ret");
				claimAtOtherRetSameAgt = rs.getString("claim_at_other_ret_same_agt");
				claimAtOtherRet = rs.getString("claim_at_other_ret");
			}

			if ("NO".equalsIgnoreCase(selfClaim) && "NO".equalsIgnoreCase(otherClaim)) {
				return false;
			}

			query = "SELECT parent_id FROM st_lms_organization_master WHERE organization_id="+ticketRetOrgId;
			rs = stmt.executeQuery(query);
			if(rs.next()) {
				ticketParentOrgId = rs.getInt("parent_id");
			}

			if (ticketRetOrgId == retOrgId) {
				canClaim = ("YES".equalsIgnoreCase(selfClaim) && "YES".equalsIgnoreCase(claimAtSelfRet)) ? true : false;
			} else {
				if (ticketParentOrgId == parentOrgId) {
					canClaim = ("YES".equalsIgnoreCase(otherClaim) && "YES".equalsIgnoreCase(claimAtOtherRetSameAgt)) ? true : false;
				} else {
					canClaim = ("YES".equalsIgnoreCase(otherClaim) && "YES".equalsIgnoreCase(claimAtOtherRet)) ? true : false;
				}
			}
		} catch (SQLException se) {
			logger.error("SQLException - ", se);
		} catch (Exception e) {
			logger.error("Exception - ", e);
		} finally {
			DBConnect.closeStmt(stmt);
			DBConnect.closeRs(rs);
		}

		return canClaim;
	}

	public static boolean canClaimAgent(String ticketNumber, int agentOrgId, Connection connection) {
		boolean canClaim = false;
		try {
			int retUserId = Util.getUserIdFromTicket(ticketNumber, connection);
			canClaim = canClaimAgent(retUserId, agentOrgId, connection);
		} catch (Exception e) {
			logger.error("Exception - ", e);
		}

		return canClaim;
	}

	public static boolean canClaimAgent(int retUserId, int claimAgentOrgId, Connection connection) {
		int parentOrgId = 0;
		String selfClaim = null;
		String otherClaim = null;
		String claimAtSelfAgt = null;
		String claimAtOtherAgt = null;
		boolean canClaim = false;
		int agentOrgId = 0;

		ResultSet rs = null;
		Statement stmt = null;
		try {
			stmt = connection.createStatement();
			rs = stmt.executeQuery("select p.organization_id from st_lms_user_master p inner join st_lms_user_master c on c.parent_user_id = p.user_id where c.user_id = " + retUserId + ";");
			if (rs.next()) {
				agentOrgId = rs.getInt("organization_id");
			} else
				return false;
			
			String query = "SELECT self_claim, other_claim FROM st_lms_oranization_limits WHERE organization_id=" + agentOrgId;
			
			rs = stmt.executeQuery(query);
			if (rs.next()) {
				selfClaim = rs.getString("self_claim");
				otherClaim = rs.getString("other_claim");
			}

			if ("NO".equalsIgnoreCase(selfClaim) && "NO".equalsIgnoreCase(otherClaim) && rs.next()) {
				return false;
			} else {
				String agtQuery = "SELECT parent_id FROM st_lms_organization_master WHERE organization_id=(SELECT organization_id FROM st_lms_user_master WHERE user_id="+retUserId+");";
				rs = stmt.executeQuery(agtQuery);
				while (rs.next()) {
					parentOrgId = rs.getInt("parent_id");
				}

				String retQuery = "SELECT claim_at_self_agt, claim_at_other_agt FROM st_lms_ret_sold_claim_criteria WHERE organization_id=(SELECT organization_id FROM st_lms_user_master WHERE user_id="+retUserId+");";
				rs = stmt.executeQuery(retQuery);
				while (rs.next()) {
					claimAtSelfAgt = rs.getString("claim_at_self_agt");
					claimAtOtherAgt = rs.getString("claim_at_other_agt");
				}

				if (parentOrgId == claimAgentOrgId) {
					canClaim = ("YES".equalsIgnoreCase(selfClaim) && "YES".equalsIgnoreCase(claimAtSelfAgt)) ? true : false;
				} else {
					canClaim = ("YES".equalsIgnoreCase(otherClaim) && "YES".equalsIgnoreCase(claimAtOtherAgt)) ? true : false;
				}
			}
		} catch (SQLException se) {
			logger.error("SQLException - ", se);
		} catch (Exception e) {
			logger.error("Exception - ", e);
		} finally {
			DBConnect.closeStmt(stmt);
			DBConnect.closeRs(rs);
		}

		return canClaim;
	}
	
	public static boolean canClaimAgentIW(int retUserId, int claimAgentOrgId, Connection connection) {
		int parentOrgId = 0;
		String selfClaim = null;
		String otherClaim = null;
		String claimAtSelfAgt = null;
		String claimAtOtherAgt = null;
		boolean canClaim = false;
		int agentOrgId = 0;

		ResultSet rs = null;
		Statement stmt = null;
		try {
			stmt = connection.createStatement();
			rs = stmt.executeQuery("select p.organization_id from st_lms_user_master p inner join st_lms_user_master c on c.parent_user_id = p.user_id where c.user_id = " + retUserId + ";");
			if (rs.next()) {
				agentOrgId = rs.getInt("organization_id");
			} else
				return false;
			
//			String query = "SELECT self_claim, other_claim FROM st_lms_oranization_limits WHERE organization_id=" + agentOrgId;
//			
//			rs = stmt.executeQuery(query);
//			if (rs.next()) {
//				selfClaim = rs.getString("self_claim");
//				otherClaim = rs.getString("other_claim");
//			}
//
//			if ("NO".equalsIgnoreCase(selfClaim) && "NO".equalsIgnoreCase(otherClaim) && rs.next()) {
//				return false;
//			} else {
//				String agtQuery = "SELECT parent_id FROM st_lms_organization_master WHERE organization_id=(SELECT organization_id FROM st_lms_user_master WHERE user_id="+retUserId+");";
//				rs = stmt.executeQuery(agtQuery);
//				while (rs.next()) {
//					parentOrgId = rs.getInt("parent_id");
//				}
//
//				String retQuery = "SELECT claim_at_self_agt, claim_at_other_agt FROM st_lms_ret_sold_claim_criteria WHERE organization_id=(SELECT organization_id FROM st_lms_user_master WHERE user_id="+retUserId+");";
//				rs = stmt.executeQuery(retQuery);
//				while (rs.next()) {
//					claimAtSelfAgt = rs.getString("claim_at_self_agt");
//					claimAtOtherAgt = rs.getString("claim_at_other_agt");
//				}
//
//				if (parentOrgId == claimAgentOrgId) {
//					canClaim = ("YES".equalsIgnoreCase(selfClaim) && "YES".equalsIgnoreCase(claimAtSelfAgt)) ? true : false;
//				} else {
//					canClaim = ("YES".equalsIgnoreCase(otherClaim) && "YES".equalsIgnoreCase(claimAtOtherAgt)) ? true : false;
//				}
//			}
			
			if (agentOrgId == claimAgentOrgId)
				canClaim = true;
			else
				canClaim = false;
		} catch (SQLException se) {
			logger.error("SQLException - ", se);
		} catch (Exception e) {
			logger.error("Exception - ", e);
		} finally {
			DBConnect.closeStmt(stmt);
			DBConnect.closeRs(rs);
		}

		return canClaim;
	}

	public static boolean canClaimBO(String ticketNumber, Connection connection) {
		boolean canClaim = false;
		try {
			int userId = getUserIdFromTicket(ticketNumber, connection);
			canClaim = canClaimBO(userId, connection);
		} catch (Exception e) {
			logger.error("Exception - ", e);
		}

		return canClaim;
	}

	public static boolean canClaimBO(int userId, Connection connection) {
		String claimAtBo = null;
		Statement stmt = null;
		ResultSet rs = null;
		boolean canClaim = false;
		try {
			stmt = connection.createStatement();
			String query = "SELECT claim_at_bo FROM st_lms_ret_sold_claim_criteria WHERE organization_id=(SELECT organization_id FROM st_lms_user_master WHERE user_id="+userId+");";
			rs = stmt.executeQuery(query);
			if (rs.next()) {
				claimAtBo = rs.getString("claim_at_bo");
			}

			canClaim = ("YES".equalsIgnoreCase(claimAtBo)) ? true : false;
		} catch (SQLException se) {
			logger.error("SQLException - ", se);
		} catch (Exception e) {
			logger.error("Exception - ", e);
		} finally {
			DBConnect.closeStmt(stmt);
			DBConnect.closeRs(rs);
		}

		return canClaim;
	}

	public static String changeFormat(String oldFormat, String newFormat, String data) throws ParseException{
		
		DateFormat formatter = new SimpleDateFormat(oldFormat);
		Date oldFormatedDate = formatter.parse(data);
		return new SimpleDateFormat(newFormat).format(oldFormatedDate);
	}

	public static String getUserTypeFromUsername(String username){

		ResultSet rs = null;
		Statement stmt = null;
		String userType = null;
		Connection conn = null ;
		try {
			conn = DBConnect.getConnection();
			stmt = conn.createStatement();
			rs = stmt.executeQuery("select organization_type from st_lms_user_master where user_name = '"+username+"';");
			if(rs.next()){
				userType = rs.getString("organization_type");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			DBConnect.closeConnection(conn, stmt, rs);
		}

		return userType;
	}

	public static Map<Integer, String> fetchAllBOUsers() {
		Map<Integer, String> boUsers = new HashMap<Integer, String>(); 

		Connection con = DBConnect.getConnection();
		Statement stmt = null;
		ResultSet rs = null;
		try {
			stmt = con.createStatement();
			rs = stmt
					.executeQuery("select um.user_id user_id, concat(cd.first_name, ' ', cd.last_name) org_name from st_lms_user_master um inner join st_lms_user_contact_details cd on um.user_id = cd.user_id where um.organization_type = 'BO' ;");
			while (rs.next()) {
				boUsers.put(rs.getInt("user_id"), rs.getString("org_name"));
			}
		} catch (Exception e) {
			e.printStackTrace();
			// throw new LMSException(e);
		} finally {
			DBConnect.closeResource(con, stmt, rs);
		}

		return boUsers;
	}
	
	
	public static int fetchUserIdFromUserName(String userName) throws LMSException {
		int userId = 0;

		Connection con = null;
		Statement stmt = null;
		ResultSet rs = null;

		try {
			con = DBConnect.getConnection();
			stmt = con.createStatement();
			rs = stmt.executeQuery("select user_id from st_lms_user_master where user_name = '" + userName + "';");
			if (rs.next()) {
				userId = rs.getInt("user_id");
			} else {
				throw new LMSException(LMSErrors.USER_NAME_DOES_NOT_EXISTS_CODE, LMSErrors.USER_NAME_DOES_NOT_EXISTS_MESSAGE);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBConnect.closeCon(con);
		}
		return userId;
	}
	
	
	public static void setEbetSaleRequestStatusDone(String tokenId,int retailerId) 
	{
		PreparedStatement ps = null;
		Connection con = null;
		try {
			con = DBConnect.getConnection();
			
			setEbetSaleRequestStatusDone(tokenId, retailerId,con);
		}catch (Exception e) {
			logger.error("ERROR",e);
		} finally {
			DBConnect.closeResource(con, ps);
		}
		
	}
		
		public static void setEbetSaleRequestStatusDone(String tokenId,int retailerId,Connection con) 
		{
			PreparedStatement ps = null;
			try {
				
			   String query = "UPDATE st_lms_ebet_sale_request AS esr,st_lms_ebet_retailer_mappping AS erm SET esr.processed_datetime=?,esr.status=? WHERE esr.organization_id=? AND esr.status=? AND esr.token_id=?";
				ps = con.prepareStatement(query);
				ps.setString(1, Util.getCurrentTimeString());
				ps.setString(2, "DONE");
				ps.setInt(3, retailerId);
				ps.setString(4,"Initiated");
				ps.setString(5,tokenId);
				logger.info("ps**************** "+ps);
				ps.execute();
			}catch (SQLException e) {
				logger.error("SQLERROR",e);
			} finally {
				DBConnect.closeResource(ps);
			}
			
		}			
}
