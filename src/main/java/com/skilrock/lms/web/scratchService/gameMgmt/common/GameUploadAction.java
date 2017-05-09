/***
 *  * � copyright 2007, SkilRock Technologies, A division of Sugal & Damani Lottery Agency Pvt. Ltd.
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
 * 
 */
package com.skilrock.lms.web.scratchService.gameMgmt.common;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;

import com.opensymphony.xwork2.ActionSupport;
import com.skilrock.lms.beans.GameBean;
import com.skilrock.lms.beans.GameTicketFormatBean;
import com.skilrock.lms.beans.PackNumberSeriesBean;
import com.skilrock.lms.beans.PackSeriesFlagBean;
import com.skilrock.lms.beans.SupplierBean;
import com.skilrock.lms.beans.UserInfoBean;
import com.skilrock.lms.common.db.DBConnect;
import com.skilrock.lms.common.db.QueryManager;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.common.utility.MD5Encoder;
import com.skilrock.lms.coreEngine.scratchService.pwtMgmt.common.CommonFunctionsHelper;
import com.skilrock.lms.coreEngine.scratchService.gameMgmt.common.GameuploadHelper;
import com.skilrock.lms.coreEngine.scratchService.inventoryMgmt.common.Unzip;

/**
 * This class is used to upload the game details.
 * 
 * @author Skilrock Technologies
 * 
 */
public class GameUploadAction extends ActionSupport implements
		ServletRequestAware, ServletResponseAware {
	static Log logger = LogFactory.getLog(GameUploadAction.class);
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static int findDigit(int k) {
		int count = 0;
		while (k != 0) {
			k = k / 10;
			count++;
		}
		return count;
	}

	private String agent_pwt_comm_rate = null;
	private String agent_sale_comm_rate = null;
	private double agentPWTCommRate;
	private double agentSaleCommRate;
	private int booksperPack;
	private String callFlag;
	private Connection conn;
	private String details;
	private int digitsofBook;
	private int digitsofPack;
	private int digitsofTicket;
	private String end = null;
	private String filePath;
	private int gameId;
	private String gameName = null;
	private int gameNumber;
	private String gameType;
	private int gameVirnDigits;
	int govt_commrate;
	int govt_pwt_commrate;
	private Double govtCommRate = null;
	private String govtCommRule;
	private String govtPWTCommRate = null;
	private double minAssProfit;
	private Boolean newGameAvailable = false;
	private String packNFrom;
	private String packNTo;
	private String[] packNumberFrom;
	private String[] packNumberTo;
	private List<PackSeriesFlagBean> packSeriesFlagList;
	private List<PackNumberSeriesBean> packSeriesList;
	private double priceperTicket;
	private double prizePayOutRatio;
	private String pwtendDate;
	private String rankupload;
	private HttpServletRequest request = null;
	private HttpServletResponse response;
	private String retailer_pwt_comm_rate = null;
	private String retailer_sale_comm_rate = null;
	private double retailerPWTCommRate;
	private double retailerSaleCommRate;
	private String saleendDate;
	HttpSession session = null;
	int start = 0;
	private String startDate;
	private List<SupplierBean> supplier_list;
	private String supplier_Name;
	private Map<Integer, String> warehouseMap;
	private int warehouseId;
	/**
	 * This method is used to insert Game book and pack details into the
	 * database
	 * 
	 * @return String
	 * @throws LMSException
	 */
	private int supplierId;

	private List<GameBean> ticket_game_list;
	private int ticketpetBook;
	private long ticketsInScheme;

	private int[] totalpack;

	/*
	 * public List<RankDetailBean> readRank(int gameId)throws Exception{ List<RankDetailBean>
	 * list= new ArrayList<RankDetailBean>(); File file =new
	 * File(getRankupload()); FileReader fstream = new FileReader(file);
	 * BufferedReader br = new BufferedReader(fstream); String strLine;
	 * RankDetailBean rank = null;
	 * 
	 * System.out.println("game id is " + gameId);
	 * 
	 * String a = ""; //Rank1=Rank String b = ""; //1|30|0|H|N String c = "";
	 * //30 String d = ""; //H String e = ""; //10N String f = ""; //1 String
	 * status = null; StringTokenizer st = null; StringTokenizer st1 = null; int
	 * rank_id = 0; int prize_amount = 0; while ((strLine = br.readLine()) !=
	 * null){ System.out.println ("vvv----- "+ strLine); rank=new
	 * RankDetailBean(); rank.setGameId(gameId); a=""; //Rank1=Rank b="";
	 * //1|30|0|H|N c=""; //30 d=""; //H e=""; //10N f=""; //1 rank_id=0;
	 * prize_amount=0; status=null; st=new StringTokenizer(strLine); for(int
	 * i=0;i<3;i++){ if(st.hasMoreTokens()){ if(i<2) a=a+st.nextToken();
	 * 
	 * else b=b+st.nextToken();
	 * 
	 * }System.out.println("first token "+ a); System.out.println("second token
	 * "+b); } st1=new StringTokenizer(b,"|"); for(int i=0;i<5;i++){
	 * if(st1.hasMoreTokens()){ if(i==0){ f=f+st1.nextToken();
	 * rank_id=Integer.parseInt(f); rank.setRank(rank_id); } else if(i==1){
	 * c=c+st1.nextToken(); prize_amount=Integer.parseInt(c);
	 * rank.setPrize_amount(prize_amount); } else if(i==3){ d=d+st1.nextToken();
	 * if(d.equals("H")) status="HIGH"; else if(d.equals("L")) status="LOW";
	 * else status=""; rank.setStatus(status); } else e=e+st1.nextToken(); } }
	 * list.add(rank); } //Close the input stream br.close(); return list; }
	 */

	/*
	 * public String initialUploadBasicDetails() { ServletContext
	 * sc=ServletActionContext.getServletContext();
	 * agent_sale_comm_rate=(String)sc.getAttribute("AGT_SALE_COMM_RATE");
	 * retailer_sale_comm_rate=(String)sc.getAttribute("RET_SALE_COMM_RATE");
	 * agent_pwt_comm_rate=(String)sc.getAttribute("AGT_PWT_COMM_RATE");
	 * retailer_pwt_comm_rate=(String)sc.getAttribute("RET_PWT_COMM_RATE");
	 * 
	 * govtCommRule=(String)sc.getAttribute("GOVT_COMM_RULE");
	 * System.out.println("govt rule is " + govtCommRule); session =
	 * getRequest().getSession();
	 * 
	 * //this.setAgent_sale_comm_rate(agent_sale_comm_rate);
	 * session.setAttribute("x", this); return SUCCESS; }
	 * 
	 * 
	 */

	private String totalPackStr;

	private double vatPercentage;

	/**
	 * This method is used to validate pack series ,
	 * 
	 * @param packnbrf
	 *            is the first pack number
	 * @param packnbrt
	 *            is the last pack number
	 * @return Bean of PackSeriesFlagBean type
	 * @throws LMSException
	 */

	public PackSeriesFlagBean checkPackSeriesValidity(String packnbrf,
			String packnbrt, int totpk) throws LMSException {

		PackSeriesFlagBean flagbean = null;
		System.out.println("game name is = " + getGameName());
		String gameNameArr[] = getGameName().split("-");
		GameuploadHelper gameuploadHelper = new GameuploadHelper();

		// here we check the validity of pack series enter by user
		flagbean = gameuploadHelper.checkPackValidity(packnbrf, packnbrt,
				gameNameArr[1], Integer.parseInt(gameNameArr[0]));
		flagbean.setPackfrn(packnbrf);
		flagbean.setPackton(packnbrt);
		flagbean.setTotpk(totpk);

		// set the error message to display user
		String html = "";
		if (!flagbean.isGamenbrflag()) {
			flagbean.setFinalflag(false);
			html = " *Pack Series Is Invalid";
		} else if (!flagbean.isGamenbrformatflag()) {
			flagbean.setFinalflag(false);
			html = " *Pack Series Is Invalid";
		} else if (!flagbean.isPackdigitformatflag()) {
			flagbean.setFinalflag(false);
			html = " *Pack Series Is Invalid";
		} else if (!flagbean.isPackseriespresenceflag()) {
			flagbean.setFinalflag(false);
			html = html + " *Packs In DB";
		}

		// set this flag message into bean
		if (html == "") {
			html = html + "Pack Series Is Valid";
			flagbean.setStatus(html);
		} else {
			flagbean.setStatus(html);
		}
		System.out.println("Hello ->  " + html);
		return flagbean;

	}

	public void checkPackSeriesValidityAjax() throws LMSException {
		List<PackSeriesFlagBean> list = new ArrayList<PackSeriesFlagBean>();
		System.out.println("from = " + packNumberFrom + "  To = "
				+ packNumberTo + "  totalpack = " + totalPackStr);
		packNumberFrom = packNumberFrom[0].split(",");
		packNumberTo = packNumberTo[0].split(",");
		String tpStr[] = totalPackStr.split(",");
		int[] arr = new int[tpStr.length];
		for (int i = 0; i < tpStr.length; i++) {
			arr[i] = Integer.parseInt(tpStr[i].trim());
		}
		totalpack = arr;
		PackSeriesFlagBean flagBean = null;
		int packfromsize = getPackNumberFrom().length;
		// int totPk = getTotalpack().length;
		// int packto = getPackNumberTo().length;
		System.out.println("fromLength = " + packNumberFrom.length
				+ "  ToLength = " + packNumberTo.length
				+ "  totalpackLength = " + totalpack.length);

		for (int i = 0; i < packfromsize; i++) {
			String pnf = packNumberFrom[i];
			int totpack = totalpack[i];
			String pnt = packNumberTo[i];
			System.out.println("packnumber Series Start From(pnf) " + pnf
					+ "   End At(pnt) " + pnt);
			// check the pack series validity
			if (pnf != null && pnf != null && pnt != null && pnt != null) {
				flagBean = checkPackSeriesValidity(pnf, pnt, totpack);
				list.add(flagBean);
			}
		}
		setPackSeriesFlagList(list);

		HttpSession session = getRequest().getSession();
		session.setAttribute("VERIFIED_PACKSERIES_FLAGBEAN_LIST",
				getPackSeriesFlagList());
		System.out.println("VERIFIED_PACKSERIES_FLAGBEAN_LIST" + list);

		PrintWriter out;
		try {
			out = getResponse().getWriter();
			response.setContentType("text/html");
			StringBuilder responseStr = new StringBuilder("");
			for (PackSeriesFlagBean bean : list) {
				responseStr.append("," + bean.getStatus());
			}
			out.print(responseStr.delete(0, 1).toString());
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public String displayGameUploadInventory() {
		HttpSession session = getRequest().getSession();
		GameuploadHelper gameuploadHelper = new GameuploadHelper();
		ticket_game_list = gameuploadHelper.fatchVirnGameList("NEW");
		session.setAttribute("NEW_GAME_LIST", ticket_game_list);
		System.out.println("New game list is == " + ticket_game_list);
		return SUCCESS;
	}

	public String displayTicketsUploadInventory() {
		HttpSession session = getRequest().getSession();
		session.removeAttribute("VERIFIED_PACKSERIES_FLAGBEAN_LIST");
		try {
			warehouseMap = new CommonFunctionsHelper().fetchWarehouseMap();
		} catch (Exception e) {
			// TODO: handle exception
		}
		return SUCCESS;
	}

	public void displayTicketsUploadInventoryAjax() {
		System.out.println("displayTicketsUploadInventoryAjax == "
				+ getGameType() + " ,callFlag = " + callFlag);
		PrintWriter out;
		try {
			out = getResponse().getWriter();
			response.setContentType("text/html");
			GameuploadHelper gameuploadHelper = new GameuploadHelper();
			ArrayList<GameBean> gameList = gameuploadHelper
					.fatchGameList(getGameType());
			StringBuilder gameString = new StringBuilder();
			;
			for (GameBean gameBean : gameList) {
				gameString.append("," + gameBean.getGameName());
			}
			gameString.delete(0, 1);
			// get the supplier list first time
			if (callFlag != null && "FIRST".equalsIgnoreCase(callFlag.trim())) {
				Map<Integer, String> supplierList = gameuploadHelper
						.fatchSupplierList();
				String supplierString = supplierList.toString()
						.replace("{", "").replace("}", "");
				gameString = gameString.append("||" + supplierString);
			}
			out.print(gameString.toString());
			System.out.println("commited data == " + gameString);

		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * This method is used to check for duplicate VIRN file
	 * 
	 * @return void
	 */

	public void fileStatusCheckAjax() throws Exception {
		// System.out.println("gggggggggggggggggggggggg");
		boolean virnFlag = true;
		PrintWriter out;
		out = getResponse().getWriter();
		GameuploadHelper gameHelper = new GameuploadHelper();
		StringBuilder vclist = new StringBuilder("");
		System.out.println(getGameName());
		System.out.println("function called readrank");
		System.out.println("file path is " + getFilePath());
		File file = new File(getFilePath());
		FileReader fstream = null;
		try {
			System.out.println("11111111111111111111111");
			fstream = new FileReader(file);
			BufferedReader br = new BufferedReader(fstream);
			System.out.println("6666666666666666666666");
			String strLine = null;
			// String a = "";
			// String b = "";
			String c = "";
			boolean flag = true;
			System.out.println("22222222222222==== gameName = " + gameName);
			String gameNameArr[] = getGameName().split("-");
			int game_id = gameHelper.getGameId(gameNameArr[0], gameNameArr[1]);
			GameTicketFormatBean ticketFmtBean = gameHelper
					.getGameNbrDigitsByGameId(game_id);
			int gameNbrDigits = ticketFmtBean.getGameNbrDigits();
			int maxRankDigits = ticketFmtBean.getMaxRankDigits();
			if (gameNbrDigits == 0 || game_id == 0 || maxRankDigits == 0) {
				System.out
						.println("***************ERROR TYPE BLOCKER************");
			}
			String virn_code = null;
			while ((strLine = br.readLine()) != null) {
				int lineLength = strLine.length();
				// a = "";
				// b = "";
				c = "";
				for (int i = 0; i < lineLength; i++) {
					// if (i < gameNbrDigits)
					// a = a + strLine.charAt(i);
					// if (i >= gameNbrDigits && i <
					// (gameNbrDigits+maxRankDigits))
					// b = b + strLine.charAt(i);
					if (i >= gameNbrDigits + maxRankDigits && i < lineLength) {
						c = c + strLine.charAt(i);
					}
				}
				if (c != "") {
					virn_code = MD5Encoder.encode(c);
					if (flag) {
						vclist.append("'");
						vclist.append(virn_code);
						vclist.append("'");
						flag = false;
					} else {
						// vclist = vclist + ",'" + virn_code + "'";
						vclist.append(",'");
						vclist.append(virn_code);
						vclist.append("'");
					}
				}
			}
			System.out.println("game id on action is" + game_id);
			System.out.println("game name is " + getGameName());

			virnFlag = gameHelper.fileStatusCheck(vclist.toString(), game_id,
					gameNameArr[0]);
			System.out.println("flag is " + virnFlag);
			out.println(virnFlag);

			// return SUCCESS;
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		// return SUCCESS;
	}

	public String getAgent_pwt_comm_rate() {
		return agent_pwt_comm_rate;
	}

	public String getAgent_sale_comm_rate() {
		return agent_sale_comm_rate;
	}

	public double getAgentPWTCommRate() {
		return agentPWTCommRate;
	}

	public double getAgentSaleCommRate() {
		return agentSaleCommRate;
	}

	public int getBooksperPack() {
		return booksperPack;
	}

	public String getCallFlag() {
		return callFlag;
	}

	public Connection getConn() {
		return conn;
	}

	public String getDetails() {
		return details;
	}

	public int getDigitsofBook() {
		return digitsofBook;
	}

	public int getDigitsofPack() {
		return digitsofPack;
	}

	public int getDigitsofTicket() {
		return digitsofTicket;
	}

	public String getEnd() {
		return end;
	}

	public String getFilePath() {
		return filePath;
	}

	/**
	 * This method is used to get the game dates by the name
	 * 
	 * @return Void
	 * @throws LMSException
	 */
	public void getGameDates() throws LMSException {
		PrintWriter out;
		try {
			out = getResponse().getWriter();
			GameuploadHelper gameuploadHelper = new GameuploadHelper();
			String allDatesString = gameuploadHelper.getInitialDates(
					getGameName(), getGameType());
			// String arr[]=new String[3];
			// arr[0]=allDatesString.s
			out.print(allDatesString);
			System.out.println("success return");
		} catch (IOException e) {

			e.printStackTrace();

		}

	}

	public int getGameId() {
		return gameId;
	}

	public String getGameName() {
		return gameName;
	}

	public int getGameNumber() {
		return gameNumber;
	}

	public String getGameType() {
		return gameType;
	}

	public int getGameVirnDigits() {
		return gameVirnDigits;
	}

	public int getGovt_commrate() {
		return govt_commrate;
	}

	public int getGovt_pwt_commrate() {
		return govt_pwt_commrate;
	}

	public Double getGovtCommRate() {
		return govtCommRate;
	}

	public String getGovtCommRule() {
		return govtCommRule;
	}

	public String getGovtPWTCommRate() {
		return govtPWTCommRate;
	}

	public double getMinAssProfit() {
		return minAssProfit;
	}

	public Boolean getNewGameAvailable() {
		return newGameAvailable;
	}

	public String getPackNFrom() {
		return packNFrom;
	}

	public String getPackNTo() {
		return packNTo;
	}

	public String[] getPackNumberFrom() {
		return packNumberFrom;
	}

	public String[] getPackNumberTo() {
		return packNumberTo;
	}

	public List<PackSeriesFlagBean> getPackSeriesFlagList() {
		return packSeriesFlagList;
	}

	public List<PackNumberSeriesBean> getPackSeriesList() {
		return packSeriesList;
	}

	public double getPriceperTicket() {
		return priceperTicket;
	}

	public double getPrizePayOutRatio() {
		return prizePayOutRatio;
	}

	public String getPwtendDate() {
		return pwtendDate;
	}

	public String getRankupload() {
		return rankupload;
	}

	public HttpServletRequest getRequest() {
		return request;
	}

	public HttpServletResponse getResponse() {
		return response;
	}

	public String getRetailer_pwt_comm_rate() {
		return retailer_pwt_comm_rate;
	}

	public String getRetailer_sale_comm_rate() {
		return retailer_sale_comm_rate;
	}

	public double getRetailerPWTCommRate() {
		return retailerPWTCommRate;
	}

	public double getRetailerSaleCommRate() {
		return retailerSaleCommRate;
	}

	public String getSaleendDate() {
		return saleendDate;
	}

	public int getStart() {
		return start;
	}

	public String getStartDate() {
		return startDate;
	}

	public List<SupplierBean> getSupplier_list() {
		return supplier_list;
	}

	public String getSupplier_Name() {
		return supplier_Name;
	}

	public int getSupplierId() {
		return supplierId;
	}

	public List<GameBean> getTicket_game_list() {
		return ticket_game_list;
	}

	public int getTicketpetBook() {
		return ticketpetBook;
	}

	public long getTicketsInScheme() {
		return ticketsInScheme;
	}

	public int[] getTotalpack() {
		return totalpack;
	}

	public String getTotalPackStr() {
		return totalPackStr;
	}

	public double getVatPercentage() {
		return vatPercentage;
	}
	
	public Map<Integer, String> getWarehouseMap() {
		return warehouseMap;
	}

	public void setWarehouseMap(Map<Integer, String> warehouseMap) {
		this.warehouseMap = warehouseMap;
	}
	
	public int getWarehouseId() {
		return warehouseId;
	}

	public void setWarehouseId(int warehouseId) {
		this.warehouseId = warehouseId;
	}

	public String insertTicketsUploadInventoryInDb() throws LMSException {
		HttpSession session = getRequest().getSession();
		UserInfoBean userBean = new UserInfoBean();
		userBean = (UserInfoBean) session.getAttribute("USER_INFO");

		List<PackNumberSeriesBean> seriesList = new ArrayList<PackNumberSeriesBean>();
		PackNumberSeriesBean seriesBean = null;
		int packfromsize = getPackNumberFrom().length;
		// int totalpck = getTotalpack().length;
		// int packto = getPackNumberTo().length;

		System.out.println("packseries list size: " + packfromsize);
		// get the total rows details of packs
		for (int i = 0; i < packfromsize; i++) {
			String pnf = packNumberFrom[i];
			int totpac = totalpack[i];
			String pnt = packNumberTo[i];
			System.out.println("packnumber Series Start From(pnf) " + pnf);
			System.out.println("packnumber Series End At(pnt) " + pnt);
			if (pnf != null && pnf != null && pnt != null && pnt != null) {
				seriesBean = new PackNumberSeriesBean();
				seriesBean.setPackNumberFrom(pnf);// by yogesh
				seriesBean.setTotalpack(totpac);// by Mukul
				seriesBean.setPackNumberTo(pnt); // by yogesh
				seriesList.add(seriesBean);
			}
		}
		// gameNameArr[1] = gameName and gameNameArr[0] = gameNbr
		String gameNameArr[] = this.gameName.split("-");
		System.out.println("gameName of ticket uploaded = " + gameName);
		GameuploadHelper helper = new GameuploadHelper();
		System.out.println("Pack Series List :" + seriesList);
		helper.insertTicketsInDB(seriesList, gameNameArr[1], Integer
				.parseInt(gameNameArr[0]), getSupplierId(), userBean
				.getUserOrgId(), userBean.getUserId(), warehouseId);

		return SUCCESS;
	}

	public String insertVirnZipFile() {
		if (Unzip.insertVirnInDB(gameId)) {
			return SUCCESS;
		}
		System.out.println("********gameId::" + gameId);
		return SUCCESS;
	}

	/**
	 * This method is used to display new games
	 * 
	 * @return String
	 * @author Skilrock Technologies
	 */

	public String newGamestoStart() {
		HttpSession session = getRequest().getSession();
		List<GameBean> newGameList = null;
		GameuploadHelper gameuploadHelper = new GameuploadHelper();
		newGameList = gameuploadHelper.getNewGames();
		session.setAttribute("NEW_GAME_START_LIST", newGameList);
		session.setAttribute("NEW_GAME_START_LIST1", newGameList);
		session.setAttribute("startValueGameSearch", new Integer(0));
		if (newGameList != null && newGameList.size() > 0) {
			startGameAjax();
		}

		return SUCCESS;

	}

	public void setAgent_pwt_comm_rate(String agent_pwt_comm_rate) {
		this.agent_pwt_comm_rate = agent_pwt_comm_rate;
	}

	public void setAgent_sale_comm_rate(String agent_sale_comm_rate) {
		this.agent_sale_comm_rate = agent_sale_comm_rate;
	}

	public void setAgentPWTCommRate(double agentPWTCommRate) {
		this.agentPWTCommRate = agentPWTCommRate;
	}

	public void setAgentSaleCommRate(double agentSaleCommRate) {
		this.agentSaleCommRate = agentSaleCommRate;
	}

	public void setBooksperPack(int booksperPack) {
		this.booksperPack = booksperPack;
	}

	public void setCallFlag(String callFlag) {
		this.callFlag = callFlag;
	}

	public void setConn(Connection conn) {
		this.conn = conn;
	}

	public void setDetails(String details) {
		this.details = details;
	}

	public void setDigitsofBook(int digitsofBook) {
		this.digitsofBook = digitsofBook;
	}

	public void setDigitsofPack(int digitsofPack) {
		this.digitsofPack = digitsofPack;
	}

	public void setDigitsofTicket(int digitsofTicket) {
		this.digitsofTicket = digitsofTicket;
	}

	public void setEnd(String end) {
		this.end = end;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public void setGameId(int gameId) {
		this.gameId = gameId;
	}

	public void setGameName(String gameName) {
		this.gameName = gameName;
	}

	public void setGameNameInSession() throws Exception {

		int game_id = 0;
		int game_nbr = 0;
		int finalGameDigit = 0;

		 
		Connection conn = null;
		conn = DBConnect.getConnection();

		try {

			// Getting Game Details using game id
			String query1 = QueryManager.getST4GameDetailsUsingGameName();
			PreparedStatement stmt1 = conn.prepareStatement(query1);
			ResultSet resultSet = null;
			stmt1.setString(1, gameName);
			resultSet = stmt1.executeQuery();
			while (resultSet.next()) {
				game_id = resultSet.getInt("game_id");
				game_nbr = resultSet.getInt("game_nbr");
			}
			int game_nbr_digit = 0;
			game_nbr_digit = findDigit(game_nbr);
			System.out.println("game_id " + game_id);
			String query2 = "select * from st_se_game_ticket_nbr_format where game_id = ?"; // QueryManager.getST4GameDetailsUsingGameName();
			PreparedStatement stmt2 = conn.prepareStatement(query2);
			stmt2.setInt(1, game_id);
			ResultSet resultSet2 = stmt2.executeQuery();
			while (resultSet2.next()) {

				finalGameDigit = resultSet2.getInt("game_nbr_digits");
			}

			System.out
					.println("Ticket Formate is :-------------game digit------"
							+ finalGameDigit);

			PrintWriter out;
			out = getResponse().getWriter();
			String html = "<input type=\"hidden\" value=\"" + finalGameDigit
					+ "\" id=\"hidd\"/>";
			response.setContentType("text/html");

			out.print(html);

			HttpSession session = getRequest().getSession();

			session.setAttribute("GAME_DIGITS", finalGameDigit);

			System.out.println("printwriter out  ----" + html);

		} finally {
			if (conn != null) {
				conn.close();
			}
		}
	}

	public void setGameNumber(int gameNumber) {
		this.gameNumber = gameNumber;
	}

	public void setGameType(String gameType) {
		this.gameType = gameType;
	}

	public void setGameVirnDigits(int gameVirnDigits) {
		this.gameVirnDigits = gameVirnDigits;
	}

	public void setGovt_commrate(int govt_commrate) {
		this.govt_commrate = govt_commrate;
	}

	public void setGovt_pwt_commrate(int govt_pwt_commrate) {
		this.govt_pwt_commrate = govt_pwt_commrate;
	}

	public void setGovtCommRate(Double govtCommRate) {
		this.govtCommRate = govtCommRate;
	}

	public void setGovtCommRule(String govtCommRule) {
		this.govtCommRule = govtCommRule;
	}

	public void setGovtPWTCommRate(String govtPWTCommRate) {
		this.govtPWTCommRate = govtPWTCommRate;
	}

	public void setMinAssProfit(double minAssProfit) {
		this.minAssProfit = minAssProfit;
	}

	public void setNewGameAvailable(Boolean newGameAvailable) {
		this.newGameAvailable = newGameAvailable;
	}

	public void setPackNFrom(String packNFrom) {
		this.packNFrom = packNFrom;
	}

	public void setPackNTo(String packNTo) {
		this.packNTo = packNTo;
	}

	public void setPackNumberFrom(String[] packNumberFrom) {
		this.packNumberFrom = packNumberFrom;
	}

	public void setPackNumberTo(String[] packNumberTo) {
		this.packNumberTo = packNumberTo;
	}

	public void setPackSeriesFlagList(
			List<PackSeriesFlagBean> packSeriesFlagList) {
		this.packSeriesFlagList = packSeriesFlagList;
	}

	public void setPackSeriesList(List<PackNumberSeriesBean> packSeriesList) {
		this.packSeriesList = packSeriesList;
	}

	public void setPriceperTicket(double priceperTicket) {
		this.priceperTicket = priceperTicket;
	}

	public void setPrizePayOutRatio(double prizePayOutRatio) {
		this.prizePayOutRatio = prizePayOutRatio;
	}

	public void setPwtendDate(String pwtendDate) {
		this.pwtendDate = pwtendDate;
	}

	public void setRankupload(String rankupload) {
		this.rankupload = rankupload;
	}

	public void setRequest(HttpServletRequest request) {
		this.request = request;
	}

	public void setResponse(HttpServletResponse response) {
		this.response = response;
	}

	public void setRetailer_pwt_comm_rate(String retailer_pwt_comm_rate) {
		this.retailer_pwt_comm_rate = retailer_pwt_comm_rate;
	}

	public void setRetailer_sale_comm_rate(String retailer_sale_comm_rate) {
		this.retailer_sale_comm_rate = retailer_sale_comm_rate;
	}

	public void setRetailerPWTCommRate(double retailerPWTCommRate) {
		this.retailerPWTCommRate = retailerPWTCommRate;
	}

	public void setRetailerSaleCommRate(double retailerSaleCommRate) {
		this.retailerSaleCommRate = retailerSaleCommRate;
	}

	public void setSaleendDate(String saleendDate) {
		this.saleendDate = saleendDate;
	}

	public void setServletRequest(HttpServletRequest request) {
		this.request = request;

	}

	public void setServletResponse(HttpServletResponse response) {
		this.response = response;
		// TODO Auto-generated method stub

	}

	public void setStart(int start) {
		this.start = start;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public void setSupplier_list(List<SupplierBean> supplier_list) {
		this.supplier_list = supplier_list;
	}

	public void setSupplier_Name(String supplier_Name) {
		this.supplier_Name = supplier_Name;
	}

	public void setSupplierId(int supplierId) {
		this.supplierId = supplierId;
	}

	public void setTicket_game_list(List<GameBean> ticket_game_list) {
		this.ticket_game_list = ticket_game_list;
	}

	public void setTicketpetBook(int ticketpetBook) {
		this.ticketpetBook = ticketpetBook;
	}

	public void setTicketsInScheme(long ticketsInScheme) {
		this.ticketsInScheme = ticketsInScheme;
	}

	public void setTotalpack(int[] totalpack) {
		this.totalpack = totalpack;
	}

	public void setTotalPackStr(String totalPackStr) {
		this.totalPackStr = totalPackStr;
	}

	public void setVatPercentage(double vatPercentage) {
		this.vatPercentage = vatPercentage;
	}

	/**
	 * This method is used to start new games
	 * 
	 * @return String
	 * @author Skilrock Technologies
	 */

	public String startGame() {

		GameuploadHelper gameuploadHelper = new GameuploadHelper();
		gameuploadHelper.startGame(getGameId());
		return SUCCESS;

	}

	/**
	 * This method is used for pagination of Company(Org) search Results .
	 * 
	 * @return SUCCESS
	 */
	public String startGameAjax() {
		int endValue = 0;
		int startValue = 0;
		// PrintWriter out = getResponse().getWriter();
		HttpSession session = getRequest().getSession();
		List<GameBean> ajaxList = (List) session
				.getAttribute("NEW_GAME_START_LIST1");
		List<GameBean> ajaxSearchList = new ArrayList();
		System.out.println("end " + ajaxList);
		if (ajaxList != null) {
			if (getEnd() != null) {
				end = getEnd();
			} else {
				end = "first";
			}
			// System.out.println("end "+end);
			startValue = (Integer) session.getAttribute("startValueGameSearch");
			if (end.equals("first")) {
				System.out.println("i m in first");
				startValue = 0;
				endValue = startValue + 5;

				if (endValue > ajaxList.size()) {
					endValue = ajaxList.size();
				}
			} else if (end.equals("Previous")) {
				System.out.println("i m in Previous");
				startValue = startValue - 5;
				if (startValue < 5) {
					startValue = 0;
				}
				endValue = startValue + 5;
				if (endValue > ajaxList.size()) {
					endValue = ajaxList.size();
				}
			} else if (end.equals("Next")) {
				System.out.println("i m in Next");
				startValue = startValue + 5;
				endValue = startValue + 5;
				if (endValue > ajaxList.size()) {
					endValue = ajaxList.size();
				}
				if (startValue > ajaxList.size()) {
					startValue = ajaxList.size() - ajaxList.size() % 5;
				}
			} else if (end.equals("last")) {
				endValue = ajaxList.size();
				startValue = endValue - endValue % 5;

			}
			if (startValue == endValue) {
				startValue = endValue - 5;
			}
			// System.out.println("End value"+endValue);
			// System.out.println("Start Value"+startValue);
			for (int i = startValue; i < endValue; i++) {
				ajaxSearchList.add(ajaxList.get(i));
				System.out.println("for loop " + i + "times");
			}
			session.setAttribute("NEW_GAME_START_LIST", ajaxSearchList);
			session.setAttribute("startValueGameSearch", startValue);
		}
		return SUCCESS;
	}

	// when radio button clicked
	public void TicketsUploadGameNameAjax() {
		System.out.println("Hello" + getGameType());
		PrintWriter out;
		try {
			out = getResponse().getWriter();
			String game_type = getGameType();
			System.out.println("" + game_type);
			if (game_type == null) {
				game_type = "";
			}
			GameuploadHelper gameuploadHelper = new GameuploadHelper();
			ArrayList<GameBean> game = gameuploadHelper
					.fatchGameList(game_type);
			System.out.println("Game List is: " + game);
			StringBuilder html = new StringBuilder("");
			html
					.append("<select name=\"gameName\" class=\"option\" id=\"gameNameId\" onblur=\"check()\" onchange=\"disableSubmit(),setGameDigit()\"><option class=\"option\" value=\"Please Select\">Please Select</option>");
			int i = 0;
			GameBean bean = null;
			for (Iterator<GameBean> it = game.iterator(); it.hasNext();) {
				bean = it.next();
				String name = bean.getGameName();
				i++;
				html.append("<option class=\"option\" value=\"" + name + "\">"
						+ name + "</option>");
			}
			html.append("</select>");
			response.setContentType("text/html");
			out.print(html.toString());
			System.out.println("Hello" + html);

		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * This method is used to Upload games basic details and rank file. throws
	 * LMSException
	 * 
	 * @return SUCCESS
	 */
	public String uploadBasicDetails() throws Exception {

		HttpSession session = getRequest().getSession();
		String govt_comm_type = null;
		System.out.println("rule is " + getGovtCommRule());
		if (govtCommRule.equals("fixedper")) {
			System.out.println("inside this");
			govt_comm_type = "FIXED_PER";

		} else if (govtCommRule.equals("minprofit")) {
			govt_comm_type = "MIN_PROFIT";
		} else if (govtCommRule.equals("notapplicable")) {
			govt_comm_type = "NOT_APP";
		}

		String defaultInvoiceMethod = (String) ServletActionContext.getServletContext().getAttribute("SCRATCH_INVOICING_METHOD_DEFAULT");
		logger.info("DEFAULT INVOICING METHOD : " +  defaultInvoiceMethod);
		
		GameuploadHelper gameHelper = new GameuploadHelper();
		String returnType = gameHelper.uploadBasicDetails(govt_comm_type,
				priceperTicket, gameNumber, gameName, ticketpetBook,
				booksperPack, agentSaleCommRate, agentPWTCommRate,
				retailerSaleCommRate, retailerPWTCommRate, govtCommRate,
				minAssProfit, getDigitsofPack(), getDigitsofBook(),
				getDigitsofTicket(), getRankupload(), getPrizePayOutRatio(),
				getVatPercentage(), getTicketsInScheme(), getGameVirnDigits(), defaultInvoiceMethod);
		if (returnType.equals("ERROR")) {
			addActionError(getText("msg.this.game.uploaded.previously"));
			return ERROR;

		} else { // remove session attributes
			session.removeAttribute("x");
			return SUCCESS;
		}

	}

	// public String uploadVirnFile() throws LMSException {
	public String uploadVirn() throws LMSException {

		GameuploadHelper helper = new GameuploadHelper();
		String type = SUCCESS;
		String gameNameArr[] = gameName.split("-");
		final String ENC_SCHEME_TYPE = "1W_ENC_OF_ALL";
		System.out.println("**********details**********\n" + details);
		// type = helper.uploadVirnFile(Integer.parseInt(gameNameArr[0]),
		// gameNameArr[1], details, startDate, saleendDate, pwtendDate,
		// ENC_SCHEME_TYPE);
		Unzip unzip = new Unzip();
		unzip.virnFileUpload(Integer.parseInt(gameNameArr[0]), gameNameArr[1],
				details, startDate, saleendDate, pwtendDate, ENC_SCHEME_TYPE);
		if ("ERROR".equalsIgnoreCase(type)) {
			addActionError(getText("msg.this.file.uploaded.priviously"));
		}
		return type;
	}

	public void VirnUploadGameNameAjax() {
		PrintWriter out;

		try {
			out = getResponse().getWriter();

			String game_type = getGameType();
			System.out.println("" + game_type);
			if (game_type == null) {
				game_type = "";
			}
			GameuploadHelper gameuploadHelper = new GameuploadHelper();
			ArrayList<GameBean> game = gameuploadHelper
					.fatchVirnGameList(game_type);

			String html = "";
			html = "<select class=\"option\" name=\"gameName\" id=\"game_Name\" onchange=\"gameDatesfromDB()\"><option class=\"option\" value=\"Please Select\">Please Select</option>";
			int i = 0;
			GameBean bean = null;
			for (Iterator<GameBean> it = game.iterator(); it.hasNext();) {
				bean = it.next();
				String name = bean.getGameName();
				i++;
				html += "<option class=\"option\" value=\"" + name + "\">"
						+ name + "</option>";
			}
			html += "</select>";
			response.setContentType("text/html");
			out.print(html);
			System.out.println(html);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}