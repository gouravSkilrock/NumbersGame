package com.skilrock.lms.web.instantPrint.gameMgmt.common;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;

import com.opensymphony.xwork2.ActionSupport;
import com.skilrock.ipe.Bean.GameLMSBean;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.coreEngine.instantPrint.common.IPEUtility;
import com.skilrock.lms.coreEngine.instantPrint.gameMgmt.common.NewGameUploadHelper;

public class NewGameUploadAction extends ActionSupport implements
		ServletRequestAware, ServletResponseAware {

	private static final long serialVersionUID = 1L;
	private HttpServletRequest request;
	private HttpServletResponse response;
	private int bookaNbr;
	private int ticketNbr;
	private int gameNumber;
	private String gameName = null;
	private double priceperTicket;
	private int ticketpetBook;
	private int booksperPack;
	private int gameVirnDigits;
	private int digitsofBook;
	private int digitsofPack;
	private int digitsofTicket;
	private double agentPWTCommRate;
	private double agentSaleCommRate;
	private double retailerPWTCommRate;
	private double retailerSaleCommRate;
	private String govtCommRule;
	private double govtCommRate;
	private double minAssProfit;
	private File rankupload;
	private File imageupload;
	private double prizePayOutRatio;
	private double vatPercentage;
	private long ticketsInScheme;
	private int gameId;
	private String end = null;
	private String gameType;
	private String printType;
	private File printschemeupload;
	
	public File getPrintschemeupload() {
		return printschemeupload;
	}

	public void setPrintschemeupload(File printschemeupload) {
		this.printschemeupload = printschemeupload;
	}

	public int getGameId() {
		return gameId;
	}

	public void setGameId(int gameId) {
		this.gameId = gameId;
	}

	public String getEnd() {
		return end;
	}

	public void setEnd(String end) {
		this.end = end;
	}

	public int getGameNumber() {
		return gameNumber;
	}

	public void setGameNumber(int gameNumber) {
		this.gameNumber = gameNumber;
	}

	public String getGameName() {
		return gameName;
	}

	public void setGameName(String gameName) {
		this.gameName = gameName;
	}

	public double getPriceperTicket() {
		return priceperTicket;
	}

	public void setPriceperTicket(double priceperTicket) {
		this.priceperTicket = priceperTicket;
	}

	public int getTicketpetBook() {
		return ticketpetBook;
	}

	public void setTicketpetBook(int ticketpetBook) {
		this.ticketpetBook = ticketpetBook;
	}

	public int getBooksperPack() {
		return booksperPack;
	}

	public void setBooksperPack(int booksperPack) {
		this.booksperPack = booksperPack;
	}

	public int getGameVirnDigits() {
		return gameVirnDigits;
	}

	public void setGameVirnDigits(int gameVirnDigits) {
		this.gameVirnDigits = gameVirnDigits;
	}

	public int getDigitsofBook() {
		return digitsofBook;
	}

	public void setDigitsofBook(int digitsofBook) {
		this.digitsofBook = digitsofBook;
	}

	public int getDigitsofPack() {
		return digitsofPack;
	}

	public void setDigitsofPack(int digitsofPack) {
		this.digitsofPack = digitsofPack;
	}

	public int getDigitsofTicket() {
		return digitsofTicket;
	}

	public void setDigitsofTicket(int digitsofTicket) {
		this.digitsofTicket = digitsofTicket;
	}

	public double getAgentPWTCommRate() {
		return agentPWTCommRate;
	}

	public void setAgentPWTCommRate(double agentPWTCommRate) {
		this.agentPWTCommRate = agentPWTCommRate;
	}

	public double getAgentSaleCommRate() {
		return agentSaleCommRate;
	}

	public void setAgentSaleCommRate(double agentSaleCommRate) {
		this.agentSaleCommRate = agentSaleCommRate;
	}

	public double getRetailerPWTCommRate() {
		return retailerPWTCommRate;
	}

	public void setRetailerPWTCommRate(double retailerPWTCommRate) {
		this.retailerPWTCommRate = retailerPWTCommRate;
	}

	public double getRetailerSaleCommRate() {
		return retailerSaleCommRate;
	}

	public void setRetailerSaleCommRate(double retailerSaleCommRate) {
		this.retailerSaleCommRate = retailerSaleCommRate;
	}

	public String getGovtCommRule() {
		return govtCommRule;
	}

	public void setGovtCommRule(String govtCommRule) {
		this.govtCommRule = govtCommRule;
	}

	public double getGovtCommRate() {
		return govtCommRate;
	}

	public void setGovtCommRate(double govtCommRate) {
		this.govtCommRate = govtCommRate;
	}

	public double getMinAssProfit() {
		return minAssProfit;
	}

	public void setMinAssProfit(double minAssProfit) {
		this.minAssProfit = minAssProfit;
	}

	public File getRankupload() {
		return rankupload;
	}

	public void setRankupload(File rankupload) {
		this.rankupload = rankupload;
	}

	public double getPrizePayOutRatio() {
		return prizePayOutRatio;
	}

	public void setPrizePayOutRatio(double prizePayOutRatio) {
		this.prizePayOutRatio = prizePayOutRatio;
	}

	public double getVatPercentage() {
		return vatPercentage;
	}

	public void setVatPercentage(double vatPercentage) {
		this.vatPercentage = vatPercentage;
	}

	public long getTicketsInScheme() {
		return ticketsInScheme;
	}

	public void setTicketsInScheme(long ticketsInScheme) {
		this.ticketsInScheme = ticketsInScheme;
	}

	public void setServletRequest(HttpServletRequest req) {
		this.request = req;
	}

	public void setServletResponse(HttpServletResponse res) {
		this.response = res;
	}

	public HttpServletRequest getRequest() {
		return request;
	}

	public HttpServletResponse getResponse() {
		return response;
	}

	public int getBookaNbr() {
		return bookaNbr;
	}

	public void setBookaNbr(int bookaNbr) {
		this.bookaNbr = bookaNbr;
	}

	public int getTicketNbr() {
		return ticketNbr;
	}

	public void setTicketNbr(int ticketNbr) {
		this.ticketNbr = ticketNbr;
	}
	public String getPrintType() {
		return printType;
	}

	public void setPrintType(String printType) {
		this.printType = printType;
	}
	public String getGameType() {
		return gameType;
	}

	public void setGameType(String gameType) {
		this.gameType = gameType;
	}
	public File getImageupload() {
		return imageupload;
	}

	public void setImageupload(File imageupload) {
		this.imageupload = imageupload;
	}

	public void generateTicketFormate() throws IOException {

		PrintWriter out = getResponse().getWriter();
		int nbr_books = getBookaNbr();
		int nbr_ticket = getTicketNbr();

		int pdigits = 0;
		int tdigits = (nbr_ticket + "").length();
		int bdigits = (nbr_books + "").length();
		if (bdigits >= 4) {
			pdigits = 2;
		} else if (bdigits <= 3) {
			bdigits = 3;
			pdigits = 3;

		}

		String values = "";
		String pdigitsstr = "" + pdigits + ":";
		String bdigitsstr = "" + bdigits + ":";
		String tdigitsstr = "" + tdigits;
		values = values
				.concat(bdigitsstr.concat(pdigitsstr).concat(tdigitsstr));
		out.print(values);
	}

	public String saveNewGame() throws LMSException {
/*		System.out.println("-gameNumber-" + gameNumber + "-gameName-"
				+ gameName + "-priceperTicket-" + priceperTicket
				+ "-ticketpetBook-" + ticketpetBook);
		System.out.println("-booksperPack-" + booksperPack + "-gameVirnDigits-"
				+ gameVirnDigits + "-digitsofBook-" + digitsofBook
				+ "-digitsofPack-" + digitsofPack);
		System.out.println("-digitsofTicket-" + digitsofTicket
				+ "-agentPWTCommRate-" + agentPWTCommRate
				+ "-agentSaleCommRate-" + agentSaleCommRate);
		System.out.println("-retailerPWTCommRate-" + retailerPWTCommRate
				+ "-retailerSaleCommRate-" + retailerSaleCommRate
				+ "-govtCommRule-" + govtCommRule);
		System.out.println("-govtCommRate-" + govtCommRate + "-minAssProfit-"
				+ minAssProfit + "-rankupload-" + rankupload
				+ "-prizePayOutRatio-" + prizePayOutRatio);
		System.out.println("-vatPercentage-" + vatPercentage
				+ "-ticketsInScheme-" + ticketsInScheme);
		System.out.println("-GameType-" + gameType
				+ "-PrintType-" + printType);
		System.out.println("-PrintScheme-" + printschemeupload.getAbsolutePath());*/
		ZipFile zipImg=null;
		try {
			zipImg=new ZipFile(imageupload);
//			System.out.println(zipImg);
		} catch (ZipException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
		
		NewGameUploadHelper helper = new NewGameUploadHelper();
		if (zipImg!=null) {
		 helper.saveGameBasicDetails(gameNumber, gameName, priceperTicket,
				ticketpetBook, booksperPack, gameVirnDigits, digitsofBook,
				digitsofPack, digitsofTicket, agentPWTCommRate,
				agentSaleCommRate, retailerPWTCommRate, retailerSaleCommRate,
				govtCommRule, govtCommRate, minAssProfit, rankupload,imageupload,printschemeupload, prizePayOutRatio, vatPercentage,
				ticketsInScheme,gameType,printType);
		}
		return SUCCESS;
	}
	
	public String newGamestoStart() {
		//NewGameUploadHelper.onStartGame();
		HttpSession session = getRequest().getSession();
		List<GameLMSBean> newGameMap = IPEUtility.fetchNewGameMap();
		session.setAttribute("NEW_GAME_START_LIST", newGameMap);
		session.setAttribute("NEW_GAME_START_LIST1", newGameMap);
		session.setAttribute("startValueGameSearch", new Integer(0));
		if (newGameMap != null && newGameMap.size() > 0) {
			startGamePageIndex();
		}

		return SUCCESS;

	}
	
	public String startGame() {
		NewGameUploadHelper helper = new NewGameUploadHelper();
		boolean isStart=helper.saveStartGame(gameId);
		if (isStart) {
			return SUCCESS;
		}
		return ERROR;
	}
	
	@SuppressWarnings("unchecked")
	public String startGamePageIndex() {
		int endValue = 0;
		int startValue = 0;
		// PrintWriter out = getResponse().getWriter();
		HttpSession session = getRequest().getSession();
		List<GameLMSBean> ajaxList = (List<GameLMSBean>) session.getAttribute("NEW_GAME_START_MAP1");
		List<GameLMSBean> ajaxSearchList = new ArrayList<GameLMSBean>();
//		System.out.println("end " + ajaxList);
		if (ajaxList != null) {
			if (getEnd() != null) {
				end = getEnd();
			} else {
				end = "first";
			}
			// System.out.println("end "+end);
			startValue = (Integer) session.getAttribute("startValueGameSearch");
			if (end.equals("first")) {
//				System.out.println("i m in first");
				startValue = 0;
				endValue = startValue + 5;

				if (endValue > ajaxList.size()) {
					endValue = ajaxList.size();
				}
			} else if (end.equals("Previous")) {
//				System.out.println("i m in Previous");
				startValue = startValue - 5;
				if (startValue < 5) {
					startValue = 0;
				}
				endValue = startValue + 5;
				if (endValue > ajaxList.size()) {
					endValue = ajaxList.size();
				}
			} else if (end.equals("Next")) {
//				System.out.println("i m in Next");
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
			for (int i = startValue; i < endValue; i++) {
				ajaxSearchList.add(ajaxList.get(i));
//				System.out.println("for loop " + i + "times");
			}
			session.setAttribute("NEW_GAME_START_LIST", ajaxSearchList);
			session.setAttribute("startValueGameSearch", startValue);
		}
		return SUCCESS;
	}

}
