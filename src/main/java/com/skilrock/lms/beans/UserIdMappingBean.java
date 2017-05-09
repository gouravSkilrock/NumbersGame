package com.skilrock.lms.beans;

import com.skilrock.lms.beans.RetUserMappingIdInfoBean;

import java.sql.Timestamp;
import java.util.LinkedHashMap;
import java.util.List;

public class UserIdMappingBean {

	private boolean isAll;
	private boolean exists;
	private boolean isSpecific;
	private boolean isFirstGeneration;
	private boolean isThirdPartyGeneration;

	private int userId;
	private int expiryDays;
	private int lastCodeExpiryDays;
	private int doneByUserId;
	private int userMappingId;
	private long generationTime;

	private String activity;
	private String requesInitiateTime;
	private String newGenerationDateTime;
	private String advGenerationDateTime;

	private String newCodeExpiryDateTime;
	private String advCodeExpiryDateTime;

	private List<Integer> userIdList;
	private List<Integer> mappingIdList;
	private LinkedHashMap<Integer, Integer> userMappingIdMap;

	private List<Integer> advMappingIdList;
	private LinkedHashMap<Integer, Integer> advUserMappingIdMap;
	private List<RetUserMappingIdInfoBean> advUserMappingIdList;

	private Timestamp lastSuccDate;
	private Timestamp lastExpDate;

	private int curStrtRange;
	private int curEndRange;

	private boolean newRangeReq;

	public boolean isAll() {
		return isAll;
	}

	public void setAll(boolean isAll) {
		this.isAll = isAll;
	}

	public boolean isExists() {
		return exists;
	}

	public void setExists(boolean exists) {
		this.exists = exists;
	}

	public boolean isSpecific() {
		return isSpecific;
	}

	public void setSpecific(boolean isSpecific) {
		this.isSpecific = isSpecific;
	}

	public boolean isThirdPartyGeneration() {
		return isThirdPartyGeneration;
	}

	public boolean isFirstGeneration() {
		return isFirstGeneration;
	}

	public void setFirstGeneration(boolean isFirstGeneration) {
		this.isFirstGeneration = isFirstGeneration;
	}

	public void setThirdPartyGeneration(boolean isThirdPartyGeneration) {
		this.isThirdPartyGeneration = isThirdPartyGeneration;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public int getExpiryDays() {
		return expiryDays;
	}

	public void setExpiryDays(int expiryDays) {
		this.expiryDays = expiryDays;
	}

	public int getLastCodeExpiryDays() {
		return lastCodeExpiryDays;
	}

	public void setLastCodeExpiryDays(int lastCodeExpiryDays) {
		this.lastCodeExpiryDays = lastCodeExpiryDays;
	}

	public int getDoneByUserId() {
		return doneByUserId;
	}

	public void setDoneByUserId(int doneByUserId) {
		this.doneByUserId = doneByUserId;
	}

	public int getUserMappingId() {
		return userMappingId;
	}

	public void setUserMappingId(int userMappingId) {
		this.userMappingId = userMappingId;
	}

	public long getGenerationTime() {
		return generationTime;
	}

	public void setGenerationTime(long generationTime) {
		this.generationTime = generationTime;
	}

	public String getActivity() {
		return activity;
	}

	public void setActivity(String activity) {
		this.activity = activity;
	}

	public String getRequesInitiateTime() {
		return requesInitiateTime;
	}

	public void setRequesInitiateTime(String requesInitiateTime) {
		this.requesInitiateTime = requesInitiateTime;
	}

	public String getNewGenerationDateTime() {
		return newGenerationDateTime;
	}

	public void setNewGenerationDateTime(String newGenerationDateTime) {
		this.newGenerationDateTime = newGenerationDateTime;
	}

	public String getAdvGenerationDateTime() {
		return advGenerationDateTime;
	}

	public void setAdvGenerationDateTime(String advGenerationDateTime) {
		this.advGenerationDateTime = advGenerationDateTime;
	}

	public String getNewCodeExpiryDateTime() {
		return newCodeExpiryDateTime;
	}

	public void setNewCodeExpiryDateTime(String newCodeExpiryDateTime) {
		this.newCodeExpiryDateTime = newCodeExpiryDateTime;
	}

	public String getAdvCodeExpiryDateTime() {
		return advCodeExpiryDateTime;
	}

	public void setAdvCodeExpiryDateTime(String advCodeExpiryDateTime) {
		this.advCodeExpiryDateTime = advCodeExpiryDateTime;
	}

	public List<Integer> getUserIdList() {
		return userIdList;
	}

	public void setUserIdList(List<Integer> userIdList) {
		this.userIdList = userIdList;
	}

	public List<Integer> getMappingIdList() {
		return mappingIdList;
	}

	public void setMappingIdList(List<Integer> mappingIdList) {
		this.mappingIdList = mappingIdList;
	}

	public LinkedHashMap<Integer, Integer> getUserMappingIdMap() {
		return userMappingIdMap;
	}

	public void setUserMappingIdMap(
			LinkedHashMap<Integer, Integer> userMappingIdMap) {
		this.userMappingIdMap = userMappingIdMap;
	}

	public List<Integer> getAdvMappingIdList() {
		return advMappingIdList;
	}

	public void setAdvMappingIdList(List<Integer> advMappingIdList) {
		this.advMappingIdList = advMappingIdList;
	}

	public LinkedHashMap<Integer, Integer> getAdvUserMappingIdMap() {
		return advUserMappingIdMap;
	}

	public void setAdvUserMappingIdMap(
			LinkedHashMap<Integer, Integer> advUserMappingIdMap) {
		this.advUserMappingIdMap = advUserMappingIdMap;
	}

	public List<RetUserMappingIdInfoBean> getAdvUserMappingIdList() {
		return advUserMappingIdList;
	}

	public void setAdvUserMappingIdList(
			List<RetUserMappingIdInfoBean> advUserMappingIdList) {
		this.advUserMappingIdList = advUserMappingIdList;
	}

	public Timestamp getLastSuccDate() {
		return lastSuccDate;
	}

	public void setLastSuccDate(Timestamp lastSuccDate) {
		this.lastSuccDate = lastSuccDate;
	}

	public Timestamp getLastExpDate() {
		return lastExpDate;
	}

	public void setLastExpDate(Timestamp lastExpDate) {
		this.lastExpDate = lastExpDate;
	}

	public int getCurStrtRange() {
		return curStrtRange;
	}

	public void setCurStrtRange(int curStrtRange) {
		this.curStrtRange = curStrtRange;
	}

	public int getCurEndRange() {
		return curEndRange;
	}

	public void setCurEndRange(int curEndRange) {
		this.curEndRange = curEndRange;
	}

	public boolean isNewRangeReq() {
		return newRangeReq;
	}

	public void setNewRangeReq(boolean newRangeReq) {
		this.newRangeReq = newRangeReq;
	}

}
