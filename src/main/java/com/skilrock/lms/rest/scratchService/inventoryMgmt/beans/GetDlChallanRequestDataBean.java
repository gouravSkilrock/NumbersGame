package com.skilrock.lms.rest.scratchService.inventoryMgmt.beans;

import java.util.List;

public class GetDlChallanRequestDataBean {
	
	private String gameName;
	private List<String> bookList;
	
	public String getGameName() {
		return gameName;
	}
	public void setGameName(String gameName) {
		this.gameName = gameName;
	}
	public List<String> getBookList() {
		return bookList;
	}
	public void setBookList(List<String> bookList) {
		this.bookList = bookList;
	}
}
