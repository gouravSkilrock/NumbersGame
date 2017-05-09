package com.skilrock.lms.keba.drawGames.javaBeans;

import java.io.Serializable;

public class DrawRequestBean implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private int drawId;

	public int getDrawId() {
		return drawId;
	}

	public void setDrawId(int drawId) {
		this.drawId = drawId;
	}
	
}