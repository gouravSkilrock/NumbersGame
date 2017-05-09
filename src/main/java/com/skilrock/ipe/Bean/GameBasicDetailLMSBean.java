package com.skilrock.ipe.Bean;

import java.io.File;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.zip.ZipFile;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;



public class GameBasicDetailLMSBean implements Serializable {

	private static final long serialVersionUID = 1L;
	private int gameId;
	private int gameNo;
	private String gameName;
	private double ticketPrice;
	private Timestamp startDate;
	private Timestamp saleEndDate;
	private Timestamp pwtEndDate;
	private int noOfBooks;
	private int noOfTicketsPerBook;
	private int noOfBooksPerPack;
	private String gameStatus;
	private File rankFile;
	private int digitsOfPack;
	private int digitsOfBook;
	private int digitsOfTicket;
	private int digitsOfVirn;
	//private ZipFile imgFile;
	private File imgFile;
	private String gameType;
	private String printType;
	private File xmlScheme;
	private boolean success;

	public File getImgFile() {
		return imgFile;
	}

	public void setImgFile(File imgFile) {
		this.imgFile = imgFile;
	}
	public File getXMLScheme() {
		return xmlScheme;
	}

	public void setXMLScheme(File xmlScheme) {
		this.xmlScheme = xmlScheme;
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

	public int getNoOfBooks() {
		return noOfBooks;
	}

	public void setNoOfBooks(int noOfBooks) {
		this.noOfBooks = noOfBooks;
	}

	public int getNoOfTicketsPerBook() {
		return noOfTicketsPerBook;
	}

	public void setNoOfTicketsPerBook(int noOfTicketsPerBook) {
		this.noOfTicketsPerBook = noOfTicketsPerBook;
	}

	public int getNoOfBooksPerPack() {
		return noOfBooksPerPack;
	}

	public void setNoOfBooksPerPack(int noOfBooksPerPack) {
		this.noOfBooksPerPack = noOfBooksPerPack;
	}

	public String getGameStatus() {
		return gameStatus;
	}

	public void setGameStatus(String gameStatus) {
		this.gameStatus = gameStatus;
	}

	public File getRankFile() {
		return rankFile;
	}

	public void setRankFile(File rankFile) {
		this.rankFile = rankFile;
	}

	public int getDigitsOfPack() {
		return digitsOfPack;
	}

	public void setDigitsOfPack(int digitsOfPack) {
		this.digitsOfPack = digitsOfPack;
	}

	public int getDigitsOfBook() {
		return digitsOfBook;
	}

	public void setDigitsOfBook(int digitsOfBook) {
		this.digitsOfBook = digitsOfBook;
	}

	public int getDigitsOfTicket() {
		return digitsOfTicket;
	}

	public void setDigitsOfTicket(int digitsOfTicket) {
		this.digitsOfTicket = digitsOfTicket;
	}

	public int getDigitsOfVirn() {
		return digitsOfVirn;
	}

	public void setDigitsOfVirn(int digitsOfVirn) {
		this.digitsOfVirn = digitsOfVirn;
	}
/*	public ZipFile getImgFile() {
		return imgFile;
	}

	public void setImgFile(ZipFile imgFile) {
		this.imgFile = imgFile;
		
	}*/

	public String getGameType() {
		return gameType;
	}

	public void setGameType(String gameType) {
		this.gameType = gameType;
	}

	public String getPrintType() {
		return printType;
	}

	public void setPrintType(String printType) {
		this.printType = printType;
	}
	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}


}
