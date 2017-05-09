package com.skilrock.lms.beans;

import java.sql.Timestamp;

public class OfflineApprovalBean {
	int fileId;
	int retUserId;
	int gameNo;
	String retName;
	String gameName;
	double saleValue;
	int totalTkt;
	Timestamp uploadTime;
	String fileStatus;
	String fileName;

	public int getGameNo() {
		return gameNo;
	}

	public void setGameNo(int gameNo) {
		this.gameNo = gameNo;
	}

	public int getFileId() {
		return fileId;
	}

	public void setFileId(int fileId) {
		this.fileId = fileId;
	}

	public int getRetUserId() {
		return retUserId;
	}

	public void setRetUserId(int retUserId) {
		this.retUserId = retUserId;
	}

	public String getRetName() {
		return retName;
	}

	public void setRetName(String retName) {
		this.retName = retName;
	}

	public String getGameName() {
		return gameName;
	}

	public void setGameName(String gameName) {
		this.gameName = gameName;
	}

	public double getSaleValue() {
		return saleValue;
	}

	public void setSaleValue(double saleValue) {
		this.saleValue = saleValue;
	}

	public int getTotalTkt() {
		return totalTkt;
	}

	public void setTotalTkt(int totalTkt) {
		this.totalTkt = totalTkt;
	}

	public Timestamp getUploadTime() {
		return uploadTime;
	}

	public void setUploadTime(Timestamp uploadTime) {
		this.uploadTime = uploadTime;
	}

	public String getFileStatus() {
		return fileStatus;
	}

	public void setFileStatus(String fileStatus) {
		this.fileStatus = fileStatus;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
}
