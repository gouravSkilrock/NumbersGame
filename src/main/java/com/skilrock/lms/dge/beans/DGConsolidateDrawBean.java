package com.skilrock.lms.dge.beans;

import java.io.Serializable;

public class DGConsolidateDrawBean implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	// Draw Specific Information
	private String drawDateTime;
	private String drawDay;
	private String drawFreezeTime;
	private int drawId;
	private String drawEventId;
	private String drawName;
	private String drawStatus;
	private String winningResult;
	// Client Specific Beans
	private DGPMSSaleBean pmsSaleBean;
	private DGLMSSaleBean lmsSaleBean;
	private DGOkPosSaleBean okPosSaleBean;
	private DGAsoftSaleBean aSoftSaleBean;
	private MTNSaleBean mtnSaleBean;
	private DGWEAVERSaleBean weaverSaleBean;

	public String getDrawDateTime() {
		return drawDateTime;
	}

	public String getDrawDay() {
		return drawDay;
	}

	public String getDrawFreezeTime() {
		return drawFreezeTime;
	}

	public int getDrawId() {
		return drawId;
	}

	public String getDrawName() {
		return drawName;
	}

	public String getDrawStatus() {
		return drawStatus;
	}

	public String getWinningResult() {
		return winningResult;
	}

	public DGPMSSaleBean getPmsSaleBean() {
		return pmsSaleBean;
	}

	public DGLMSSaleBean getLmsSaleBean() {
		return lmsSaleBean;
	}

	public void setDrawDateTime(String drawDateTime) {
		this.drawDateTime = drawDateTime;
	}

	public void setDrawDay(String drawDay) {
		this.drawDay = drawDay;
	}

	public void setDrawFreezeTime(String drawFreezeTime) {
		this.drawFreezeTime = drawFreezeTime;
	}

	public void setDrawId(int drawId) {
		this.drawId = drawId;
	}

	public void setDrawName(String drawName) {
		this.drawName = drawName;
	}

	public void setDrawStatus(String drawStatus) {
		this.drawStatus = drawStatus;
	}

	public void setWinningResult(String winningResult) {
		this.winningResult = winningResult;
	}

	public void setPmsSaleBean(DGPMSSaleBean pmsSaleBean) {
		this.pmsSaleBean = pmsSaleBean;
	}

	public void setLmsSaleBean(DGLMSSaleBean lmsSaleBean) {
		this.lmsSaleBean = lmsSaleBean;
	}

	public String getDrawEventId() {
		return drawEventId;
	}

	public void setDrawEventId(String drawEventId) {
		this.drawEventId = drawEventId;
	}

	public void setOkPosSaleBean(DGOkPosSaleBean okPosSaleBean) {
		this.okPosSaleBean = okPosSaleBean;
	}

	public DGOkPosSaleBean getOkPosSaleBean() {
		return okPosSaleBean;
	}

	public void setaSoftSaleBean(DGAsoftSaleBean aSoftSaleBean) {
		this.aSoftSaleBean = aSoftSaleBean;
	}

	public DGAsoftSaleBean getaSoftSaleBean() {
		return aSoftSaleBean;
	}

	public MTNSaleBean getMtnSaleBean() {
		return mtnSaleBean;
	}

	public void setMtnSaleBean(MTNSaleBean mtnSaleBean) {
		this.mtnSaleBean = mtnSaleBean;
	}
	
	

	public DGWEAVERSaleBean getWeaverSaleBean() {
		return weaverSaleBean;
	}

	public void setWeaverSaleBean(DGWEAVERSaleBean weaverSaleBean) {
		this.weaverSaleBean = weaverSaleBean;
	}

	@Override
	public String toString() {
		return "DGConsolidateDrawBean [drawDateTime=" + drawDateTime
				+ ", drawDay=" + drawDay + ", drawFreezeTime=" + drawFreezeTime
				+ ", drawId=" + drawId + ", drawEventId=" + drawEventId
				+ ", drawName=" + drawName + ", drawStatus=" + drawStatus
				+ ", winningResult=" + winningResult + ", pmsSaleBean="
				+ pmsSaleBean + ", lmsSaleBean=" + lmsSaleBean
				+ ", okPosSaleBean=" + okPosSaleBean + ", aSoftSaleBean="
				+ aSoftSaleBean + ", mtnSaleBean=" + mtnSaleBean + "]";
	}

}
