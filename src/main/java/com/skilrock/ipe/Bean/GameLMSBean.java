package com.skilrock.ipe.Bean;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Map;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;


	
public class GameLMSBean implements Serializable {

	private static final long serialVersionUID = 1L;
	private int gameId;
	private int gameNo;
	private String gameName;
	private double ticketPrice;
	private Timestamp startDate;
	private Timestamp saleEndDate;
	private Timestamp pwtEndDate;
	private String gameStatus;
	private Map<String, Integer> activeBookMap;
	private int noOfTktPerBook;
	private byte[] gameKey;
	private int packNoDigit;
	private int bookNoDigit;
	private int tktNoDigit;
	private int gameNoDigit;
	private int rankDigit;
	private int virnDigit;
	private String gamePrintScheme;
	private String isSample;
	private String textOrImage;
	private Map<String, Integer> imageSizeMap;
	private Map<String, byte[]> imageDataMap;
	private Map<String, ArrayList<String>> imageTypeMap;
	private String gameLogoType;
	private String gameLogoCode;
	private String prizeLogoType;

	public String getGameLogoType() {
		return gameLogoType;
	}

	public void setGameLogoType(String gameLogoType) {
		this.gameLogoType = gameLogoType;
	}

	public String getPrizeLogoType() {
		return prizeLogoType;
	}

	public void setPrizeLogoType(String prizeLogoType) {
		this.prizeLogoType = prizeLogoType;
	}

	public Map<String, Integer> getImageSizeMap() {
		return imageSizeMap;
	}

	public void setImageSizeMap(Map<String, Integer> imageSizeMap) {
		this.imageSizeMap = imageSizeMap;
	}

	public Map<String, byte[]> getImageDataMap() {
		return imageDataMap;
	}

	public void setImageDataMap(Map<String, byte[]> imageDataMap) {
		this.imageDataMap = imageDataMap;
	}

	public byte[] getGameKey() {
		return gameKey;
	}

	public void setGameKey(byte[] gameKey) {
		this.gameKey = gameKey;
	}

	public int getGameId() {
		return gameId;
	}

	public void setGameId(int gameId) {
		this.gameId = gameId;
	}

	public int getGameNo() {
		return gameNo;
	}

	public void setGameNo(int gameNo) {
		this.gameNo = gameNo;
	}

	public String getGameName() {
		return gameName;
	}

	public void setGameName(String gameName) {
		this.gameName = gameName;
	}

	public double getTicketPrice() {
		return ticketPrice;
	}

	public void setTicketPrice(double ticketPrice) {
		this.ticketPrice = ticketPrice;
	}

	public Timestamp getStartDate() {
		return startDate;
	}

	public void setStartDate(Timestamp startDate) {
		this.startDate = startDate;
	}

	public Timestamp getSaleEndDate() {
		return saleEndDate;
	}

	public void setSaleEndDate(Timestamp saleEndDate) {
		this.saleEndDate = saleEndDate;
	}

	public Timestamp getPwtEndDate() {
		return pwtEndDate;
	}

	public void setPwtEndDate(Timestamp pwtEndDate) {
		this.pwtEndDate = pwtEndDate;
	}

	public String getGameStatus() {
		return gameStatus;
	}

	public void setGameStatus(String gameStatus) {
		this.gameStatus = gameStatus;
	}

	public Map<String, Integer> getActiveBookMap() {
		return activeBookMap;
	}

	public void setActiveBookMap(Map<String, Integer> activeBookMap) {
		this.activeBookMap = activeBookMap;
	}

	public int getNoOfTktPerBook() {
		return noOfTktPerBook;
	}

	public void setNoOfTktPerBook(int noOfTktPerBook) {
		this.noOfTktPerBook = noOfTktPerBook;
	}

	public int getPackNoDigit() {
		return packNoDigit;
	}

	public void setPackNoDigit(int packNoDigit) {
		this.packNoDigit = packNoDigit;
	}

	public int getBookNoDigit() {
		return bookNoDigit;
	}

	public void setBookNoDigit(int bookNoDigit) {
		this.bookNoDigit = bookNoDigit;
	}

	public int getTktNoDigit() {
		return tktNoDigit;
	}

	public void setTktNoDigit(int tktNoDigit) {
		this.tktNoDigit = tktNoDigit;
	}

	public int getGameNoDigit() {
		return gameNoDigit;
	}

	public void setGameNoDigit(int gameNoDigit) {
		this.gameNoDigit = gameNoDigit;
	}

	public int getRankDigit() {
		return rankDigit;
	}

	public void setRankDigit(int rankDigit) {
		this.rankDigit = rankDigit;
	}

	public int getVirnDigit() {
		return virnDigit;
	}

	public void setVirnDigit(int virnDigit) {
		this.virnDigit = virnDigit;
	}

	public String getGamePrintScheme() {
		return gamePrintScheme;
	}

	public void setGamePrintScheme(String gamePrintScheme) {
		this.gamePrintScheme = gamePrintScheme;
	}

	public String getIsSample() {
		return isSample;
	}

	public void setIsSample(String isSample) {
		this.isSample = isSample;
	}

	public String getTextOrImage() {
		return textOrImage;
	}

	public void setTextOrImage(String textOrImage) {
		this.textOrImage = textOrImage;
	}

	public Map<String, ArrayList<String>> getImageTypeMap() {
		return imageTypeMap;
	}

	public void setImageTypeMap(Map<String, ArrayList<String>> imageTypeMap) {
		this.imageTypeMap = imageTypeMap;
	}

	public String getGameLogoCode() {
		return gameLogoCode;
	}

	public void setGameLogoCode(String gameLogoCode) {
		this.gameLogoCode = gameLogoCode;
	}

	

}
