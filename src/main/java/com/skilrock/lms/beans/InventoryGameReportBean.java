package com.skilrock.lms.beans;

import java.sql.Date;
import com.skilrock.lms.common.utility.DateFormatConverter;

public class InventoryGameReportBean {

	private int activebooks;
	private int bookagent;
	private int bookbo;
	private int bookretailer;
	private int gameid;
	private String gamename;
	private int gamenbr;
	private String gamestatus;
	private Date pwtenddate;
	private Date saleenddate;
	private String startdate;
	private int totalbooks;

	public int getActivebooks() {
		return activebooks;
	}

	public int getBookagent() {
		return bookagent;
	}

	public int getBookbo() {
		return bookbo;
	}

	public int getBookretailer() {
		return bookretailer;
	}

	public int getGameid() {
		return gameid;
	}

	public String getGamename() {
		return gamename;
	}

	public int getGamenbr() {
		return gamenbr;
	}

	/*
	 * public void setSaleenddate(Date saleenddate) {
	 * this.saleenddate=saleenddate; //this.saleenddate =
	 * DateFormatConverter.parseDateToString(saleenddate); }
	 */
	public String getGamestatus() {
		return gamestatus;
	}

	/**
	 * @return the pwtenddate
	 */
	public Date getPwtenddate() {
		return pwtenddate;
	}

	public Date getSaleenddate() {
		return saleenddate;
	}

	public String getStartdate() {
		return startdate;
	}

	public int getTotalbooks() {
		return totalbooks;
	}

	public void setActivebooks(int activebooks) {
		this.activebooks = activebooks;
	}

	public void setBookagent(int bookagent) {
		this.bookagent = bookagent;
	}

	public void setBookbo(int bookbo) {
		this.bookbo = bookbo;
	}

	public void setBookretailer(int bookretailer) {
		this.bookretailer = bookretailer;
	}

	public void setGameid(int gameid) {
		this.gameid = gameid;
	}

	public void setGamename(String gamename) {
		this.gamename = gamename;
	}

	public void setGamenbr(int gamenbr) {
		this.gamenbr = gamenbr;
	}

	public void setGamestatus(String gamestatus) {
		this.gamestatus = gamestatus;
	}

	/**
	 * @param pwtenddate
	 *            the pwtenddate to set
	 */
	public void setPwtenddate(Date pwtenddate) {
		this.pwtenddate = pwtenddate;
	}

	public void setSaleenddate(Date saleenddate) {
		this.saleenddate = saleenddate;
	}

	public void setStartdate(Date startdate) {
		this.startdate = DateFormatConverter.parseDateToString(startdate);
	}

	public void setTotalbooks(int totalbooks) {
		this.totalbooks = totalbooks;
	}

}
