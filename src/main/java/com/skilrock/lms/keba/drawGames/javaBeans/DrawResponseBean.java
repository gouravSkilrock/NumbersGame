package com.skilrock.lms.keba.drawGames.javaBeans;

import java.io.Serializable;

public class DrawResponseBean implements Serializable {

	private static final long serialVersionUID = 1L;
	private String drawName;
	private String drawDateTime;
	
	public String getDrawName() {
		return drawName;
	}
	
	public void setDrawName(String drawName) {
		this.drawName = drawName;
	}
	
	public String getDrawDateTime() {
		return drawDateTime;
	}
	
	public void setDrawDateTime(String drawDateTime) {
		this.drawDateTime = drawDateTime;
	}

}