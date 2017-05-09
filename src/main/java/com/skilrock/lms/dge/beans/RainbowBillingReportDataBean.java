package com.skilrock.lms.dge.beans;

import java.io.Serializable;

public class RainbowBillingReportDataBean implements Serializable {
	private static final long serialVersionUID = 1L;

	private int drawId;
	private int eventId;
	private String drawDateTime;
	private String drawName;
	private RainbowWinReportDataBean basicDataBean;
	private RainbowWinReportDataBean powerDataBean;

	public int getDrawId() {
		return drawId;
	}

	public void setDrawId(int drawId) {
		this.drawId = drawId;
	}

	public int getEventId() {
		return eventId;
	}

	public void setEventId(int eventId) {
		this.eventId = eventId;
	}

	public String getDrawDateTime() {
		return drawDateTime;
	}

	public void setDrawDateTime(String drawDateTime) {
		this.drawDateTime = drawDateTime;
	}

	public String getDrawName() {
		return drawName;
	}

	public void setDrawName(String drawName) {
		this.drawName = drawName;
	}

	public RainbowWinReportDataBean getBasicDataBean() {
		return basicDataBean;
	}

	public void setBasicDataBean(RainbowWinReportDataBean basicDataBean) {
		this.basicDataBean = basicDataBean;
	}

	public RainbowWinReportDataBean getPowerDataBean() {
		return powerDataBean;
	}

	public void setPowerDataBean(RainbowWinReportDataBean powerDataBean) {
		this.powerDataBean = powerDataBean;
	}

	@Override
	public String toString() {
		return "RainbowBillingReportDataBean [drawId=" + drawId + ", eventId="
				+ eventId + ", drawDateTime=" + drawDateTime + ", drawName="
				+ drawName + ", basicDataBean=" + basicDataBean
				+ ", powerDataBean=" + powerDataBean + "]";
	}

}
