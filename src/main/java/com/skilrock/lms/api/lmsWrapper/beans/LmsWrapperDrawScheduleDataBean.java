package com.skilrock.lms.api.lmsWrapper.beans;

import java.io.Serializable;

public class LmsWrapperDrawScheduleDataBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String checkPPR;
	private String drawDateTime;
	private String drawDay;
	private int drawId;
	private String drawStatus;
	private String freezeDateTime;
	private String performStatus;
	private String winningResult;

	@Override
	public boolean equals(Object beanObj) {
		if (beanObj != null) {
			if (beanObj instanceof LmsWrapperDrawScheduleDataBean) {
				if (beanObj == this) {
					return true;
				}
				LmsWrapperDrawScheduleDataBean bean = (LmsWrapperDrawScheduleDataBean) beanObj;
				if (bean.getDrawId() == this.drawId) {
					return true;
				}
			}
		}
		return false;
	}

	public String getCheckPPR() {
		return checkPPR;
	}

	public String getDrawDateTime() {
		return drawDateTime;
	}

	public String getDrawDay() {
		return drawDay;
	}

	public int getDrawId() {
		return drawId;
	}

	public String getDrawStatus() {
		return drawStatus;
	}

	public String getFreezeDateTime() {
		return freezeDateTime;
	}

	public String getPerformStatus() {
		return performStatus;
	}

	public String getWinningResult() {
		return winningResult;
	}

	public void setCheckPPR(String checkPPR) {
		this.checkPPR = checkPPR;
	}

	public void setDrawDateTime(String drawDateTime) {
		this.drawDateTime = drawDateTime;
	}

	public void setDrawDay(String drawDay) {
		this.drawDay = drawDay;
	}

	public void setDrawId(int drawId) {
		this.drawId = drawId;
	}

	public void setDrawStatus(String drawStatus) {
		this.drawStatus = drawStatus;
	}

	public void setFreezeDateTime(String freezeDateTime) {
		this.freezeDateTime = freezeDateTime;
	}

	public void setPerformStatus(String performStatus) {
		this.performStatus = performStatus;
	}

	public void setWinningResult(String winningResult) {
		this.winningResult = winningResult;
	}

	/*
	 * public static void main(String[] args) { List<DrawScheduleBeanResult>
	 * list = new ArrayList<DrawScheduleBeanResult>();
	 * 
	 * DrawScheduleBeanResult bean = new DrawScheduleBeanResult();
	 * bean.setDrawId(2); list.add(bean); DrawScheduleBeanResult abean = new
	 * DrawScheduleBeanResult(); abean.setDrawId(1); list.add(abean);
	 * 
	 * DrawScheduleBeanResult dbean = new DrawScheduleBeanResult();
	 * dbean.setDrawId(3);
	 * 
	 * System.out.println(list.contains(dbean)); }
	 */

}