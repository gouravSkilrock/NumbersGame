package com.skilrock.lms.keba.drawGames.javaBeans;

import java.io.Serializable;
import java.util.List;

public class SaleRequestBean implements Serializable {

	private static final long serialVersionUID = 1L;
	private double totalPurchaseAmt;
	private String gameName;
	private int noOfPanels;
	private int noOfDraws;
	private String userName;
	private String plrMobileNumber;
	private boolean isAdvancePlay;
	private List<PanelRequestBean> panelData;
	private List<DrawRequestBean> drawData;
	
	public List<PanelRequestBean> getPanelData() {
		return panelData;
	}

	public String getPlrMobileNumber() {
		return plrMobileNumber;
	}

	public void setPlrMobileNumber(String plrMobileNumber) {
		this.plrMobileNumber = plrMobileNumber;
	}

	public void setPanelData(List<PanelRequestBean> panelData) {
		this.panelData = panelData;
	}

	public List<DrawRequestBean> getDrawData() {
		return drawData;
	}

	public void setDrawData(List<DrawRequestBean> drawData) {
		this.drawData = drawData;
	}

	public double getTotalPurchaseAmt() {
		return totalPurchaseAmt;
	}
	
	public void setTotalPurchaseAmt(double totalPurchaseAmt) {
		this.totalPurchaseAmt = totalPurchaseAmt;
	}
	
	public int getNoOfPanels() {
		return noOfPanels;
	}
	
	public void setNoOfPanels(int noOfPanels) {
		this.noOfPanels = noOfPanels;
	}
	
	public int getNoOfDraws() {
		return noOfDraws;
	}
	
	public void setNoOfDraws(int noOfDraws) {
		this.noOfDraws = noOfDraws;
	}
	
	public String getUserName() {
		return userName;
	}
	
	public void setUserName(String userName) {
		this.userName = userName;
	}
	
	public boolean isAdvancePlay() {
		return isAdvancePlay;
	}
	
	public void setAdvancePlay(boolean isAdvancePlay) {
		this.isAdvancePlay = isAdvancePlay;
	}

	public String getGameName() {
		return gameName;
	}

	public void setGameName(String gameName) {
		this.gameName = gameName;
	}
	
	

}