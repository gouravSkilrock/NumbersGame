package com.skilrock.ipe.Bean;

import java.io.File;
import java.io.Serializable;
import java.sql.Timestamp;





public class GameInventoryDetailsLMSBean implements Serializable {

	private static final long serialVersionUID = 1L;
	private int gameNo;
	private String gameName;
	private File virnFile;
	private String packFrom;
	private String packTo;
	private Timestamp startDate;
	private Timestamp saleEndDate;
	private Timestamp pwtEndDate;
	
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

	public File getVirnFile() {
		return virnFile;
	}

	public void setVirnFile(File virnFile) {
		this.virnFile = virnFile;
	}

	public String getPackFrom() {
		return packFrom;
	}

	public void setPackFrom(String packFrom) {
		this.packFrom = packFrom;
	}

	public String getPackTo() {
		return packTo;
	}

	public void setPackTo(String packTo) {
		this.packTo = packTo;
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
	
}
