package com.skilrock.lms.beans;

import java.io.Serializable;
import java.sql.Timestamp;

public class HistoryBean implements Serializable {
	private static final long serialVersionUID = 1L;

	private long id;
	private int organizationId;
	private String changeType;
	private String changeValue;
	private String updatedValue;
	private Timestamp changeTime;
	private int doneByUserId;
	private String comments;
	private String requestIp;
	private String tableName;
	private String fieldName;
	private String dateAppender; 

	public HistoryBean() {
	}

	public HistoryBean(int organizationId, int doneByUserId,
			String comments, String requestIp) {
		this.organizationId = organizationId;
		this.doneByUserId = doneByUserId;
		this.comments = comments;
		this.requestIp = requestIp;
	}

	public HistoryBean(int organizationId, String changeType,
			String changeValue, String updatedValue, int doneByUserId,
			String comments, String requestIp) {
		this.organizationId = organizationId;
		this.changeType = changeType;
		this.changeValue = changeValue;
		this.updatedValue = updatedValue;
		this.doneByUserId = doneByUserId;
		this.comments = comments;
		this.requestIp = requestIp;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public int getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(int organizationId) {
		this.organizationId = organizationId;
	}

	public String getChangeType() {
		return changeType;
	}

	public void setChangeType(String changeType) {
		this.changeType = changeType;
	}

	public String getChangeValue() {
		return changeValue;
	}

	public void setChangeValue(String changeValue) {
		this.changeValue = changeValue;
	}

	public String getUpdatedValue() {
		return updatedValue;
	}

	public void setUpdatedValue(String updatedValue) {
		this.updatedValue = updatedValue;
	}

	public Timestamp getChangeTime() {
		return changeTime;
	}

	public void setChangeTime(Timestamp changeTime) {
		this.changeTime = changeTime;
	}

	public int getDoneByUserId() {
		return doneByUserId;
	}

	public void setDoneByUserId(int doneByUserId) {
		this.doneByUserId = doneByUserId;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public String getRequestIp() {
		return requestIp;
	}

	public void setRequestIp(String requestIp) {
		this.requestIp = requestIp;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public String getFieldName() {
		return fieldName;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	public String getDateAppender() {
		return dateAppender;
	}

	public void setDateAppender(String dateAppender) {
		this.dateAppender = dateAppender;
	}
	
}