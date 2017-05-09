package com.skilrock.lms.dge.beans;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


public class SchedulerBean implements Serializable {
	static Log logger = LogFactory.getLog(SchedulerBean.class);
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private List<DrawScheduleBean> drawScheduleList;
	private StringBuilder freezeOrPerform = new StringBuilder("");
	private int gameNo;
	private boolean isSuccess = false;
	private String responseMessage;
	private String updatedFreezeTime;

	public List<DrawScheduleBean> getDrawScheduleList() {
		return drawScheduleList;
	}

	public StringBuilder getFreezeOrPerform() {
		return freezeOrPerform;
	}

	public int getGameNo() {
		return gameNo;
	}

	public String getResponseMessage() {
		return responseMessage;
	}

	public String getUpdatedFreezeTime() {
		return updatedFreezeTime;
	}

	public boolean isSuccess() {
		return isSuccess;
	}

	public void setDrawScheduleList(List<DrawScheduleBean> drawScheduleList) {
		this.drawScheduleList = drawScheduleList;
	}

	public void setFreezeOrPerform(StringBuilder freezeOrPerform) {
		this.freezeOrPerform = freezeOrPerform;
	}

	public void setGameNo(int gameNo) {
		this.gameNo = gameNo;
	}

	public void setResponseMessage(String responseMessage) {
		this.responseMessage = responseMessage;
	}

	public void setSuccess(boolean isSuccess) {
		this.isSuccess = isSuccess;
	}

	public void setUpdatedFreezeTime(String updatedFreezeTime) {
		this.updatedFreezeTime = updatedFreezeTime;
	}

}
