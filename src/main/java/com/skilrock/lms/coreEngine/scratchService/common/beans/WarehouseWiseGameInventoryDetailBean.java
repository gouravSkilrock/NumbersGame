package com.skilrock.lms.coreEngine.scratchService.common.beans;

import java.util.List;
import java.util.Map;

public class WarehouseWiseGameInventoryDetailBean {
	private String gameName;
	private Map<String, List<String>> packBookList;

	public String getGameName() {
		return gameName;
	}

	public void setGameName(String gameName) {
		this.gameName = gameName;
	}

	public Map<String, List<String>> getPackBookList() {
		return packBookList;
	}

	public void setPackBookList(Map<String, List<String>> packBookList) {
		this.packBookList = packBookList;
	}

	@Override
	public String toString() {
		return "WarehouseWiseGameInventoryDetailBean [gameName=" + gameName
				+ ", packBookList=" + packBookList + "]";
	}

}
