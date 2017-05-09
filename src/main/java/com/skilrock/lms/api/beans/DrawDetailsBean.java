package com.skilrock.lms.api.beans;

import java.io.Serializable;
import java.util.List;

public class DrawDetailsBean implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private int drawId;
	private String drawDateTime;
	private String drawName;
	private String symbols;
	private List<PanelBean> panelList;
	private PanelBean panelBean;
	
	
	
	
	public PanelBean getPanelBean() {
		return panelBean;
	}
	public void setPanelBean(PanelBean panelBean) {
		this.panelBean = panelBean;
	}
	public List<PanelBean> getPanelList() {
		return panelList;
	}
	public void setPanelList(List<PanelBean> panelList) {
		this.panelList = panelList;
	}
	public String getDrawDateTime() {
		return drawDateTime;
	}
	public String getDrawName() {
		return drawName;
	}
	public String getSymbols() {
		return symbols;
	}
	public void setDrawDateTime(String drawDateTime) {
		this.drawDateTime = drawDateTime;
	}
	public void setDrawName(String drawName) {
		this.drawName = drawName;
	}
	public void setSymbols(String symbols) {
		this.symbols = symbols;
	}
	public int getDrawId() {
		return drawId;
	}
	public void setDrawId(int drawId) {
		this.drawId = drawId;
	}
	
	
	

}
