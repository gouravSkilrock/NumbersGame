package com.skilrock.lms.dge.beans;

import java.io.Serializable;
import java.util.Map;

public class CommonBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int draw_id;
	Map<Integer, Map<Integer, String>> drawIdTableMap;
	private int game_no;

	public int getDraw_id() {
		return draw_id;
	}

	public Map<Integer, Map<Integer, String>> getDrawIdTableMap() {
		return drawIdTableMap;
	}

	public int getGame_no() {
		return game_no;
	}

	public void setDraw_id(int draw_id) {
		this.draw_id = draw_id;
	}

	public void setDrawIdTableMap(
			Map<Integer, Map<Integer, String>> drawIdTableMap) {
		this.drawIdTableMap = drawIdTableMap;
	}

	public void setGame_no(int game_no) {
		this.game_no = game_no;
	}

}
