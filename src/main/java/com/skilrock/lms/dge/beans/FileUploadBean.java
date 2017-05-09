package com.skilrock.lms.dge.beans;

import java.io.File;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class FileUploadBean implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Map<String, String> drawIdMap;
	private Set<String> drawIdSet;
	private File file;
	private String fileName;
	private int GameId;
	private int GameNo;
	//private List<String> recordList;
	private String refNo;
	private int retailerOrgId;
	private int retailerUserId;
	private String status;
	//private Map<String, String> ticketAmtMap;
	private Timestamp uploadTime;
	
	private List<OfflineTicketBean> ticketBeanList=null;

	public List<OfflineTicketBean> getTicketBeanList() {
		return ticketBeanList;
	}

	public void setTicketBeanList(List<OfflineTicketBean> ticketBeanList) {
		this.ticketBeanList = ticketBeanList;
	}

	public Map<String, String> getDrawIdMap() {
		return drawIdMap;
	}

	public Set<String> getDrawIdSet() {
		return drawIdSet;
	}

	public File getFile() {
		return file;
	}

	public String getFileName() {
		return fileName;
	}

	public int getGameId() {
		return GameId;
	}

	public int getGameNo() {
		return GameNo;
	}

//	public List<String> getRecordList() {
//		return recordList;
//	}

	public String getRefNo() {
		return refNo;
	}

	public int getRetailerOrgId() {
		return retailerOrgId;
	}

	public int getRetailerUserId() {
		return retailerUserId;
	}

	public String getStatus() {
		return status;
	}

//	public Map<String, String> getTicketAmtMap() {
//		return ticketAmtMap;
//	}

	public Timestamp getUploadTime() {
		return uploadTime;
	}

	public void setDrawIdMap(Map<String, String> drawIdMap) {
		this.drawIdMap = drawIdMap;
	}

	public void setDrawIdSet(Set<String> drawIdSet) {
		this.drawIdSet = drawIdSet;
	}

	public void setFile(File file) {
		this.file = file;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public void setGameId(int gameId) {
		GameId = gameId;
	}

	public void setGameNo(int gameNo) {
		GameNo = gameNo;
	}

//	public void setRecordList(List<String> recordList) {
//		this.recordList = recordList;
//	}

	public void setRefNo(String refNo) {
		this.refNo = refNo;
	}

	public void setRetailerOrgId(int retailerOrgId) {
		this.retailerOrgId = retailerOrgId;
	}

	public void setRetailerUserId(int retailerUserId) {
		this.retailerUserId = retailerUserId;
	}

	public void setStatus(String status) {
		this.status = status;
	}

//	public void setTicketAmtMap(Map<String, String> ticketAmtMap) {
//		this.ticketAmtMap = ticketAmtMap;
//	}

	public void setUploadTime(Timestamp uploadTime) {
		this.uploadTime = uploadTime;
	}

}
