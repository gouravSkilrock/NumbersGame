package com.skilrock.lms.dge.beans;

import java.io.Serializable;
import java.util.Map;

public class RaffleResultDataBean implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private int drawId;
	private int gameNo;
	private Map<String, LocationSaleBean> locatinNRetListmap;
	
	public int getDrawId() {
		return drawId;
	}
	public void setDrawId(int drawId) {
		this.drawId = drawId;
	}
	public int getGameNo() {
		return gameNo;
	}
	public void setGameNo(int gameNo) {
		this.gameNo = gameNo;
	}
	public Map<String, LocationSaleBean> getLocatinNRetListmap() {
		return locatinNRetListmap;
	}
	public void setLocatinNRetListmap(
			Map<String, LocationSaleBean> locatinNRetListmap) {
		this.locatinNRetListmap = locatinNRetListmap;
	}
	
	
}
