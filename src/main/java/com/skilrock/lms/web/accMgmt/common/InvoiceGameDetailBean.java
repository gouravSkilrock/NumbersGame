package com.skilrock.lms.web.accMgmt.common;

import java.util.ArrayList;
import java.util.List;

public class InvoiceGameDetailBean {

	private List<String> bookNbrList = new ArrayList<String>();
	private String gameName;
	private int nbrBooks;
	private List<String> packNbrList = new ArrayList<String>();
	private String salCommVar;

	public List<String> getBookNbrList() {
		return bookNbrList;
	}

	public String getGameName() {
		return gameName;
	}

	public int getNbrBooks() {
		return nbrBooks;
	}

	public List<String> getPackNbrList() {
		return packNbrList;
	}

	public String getSalCommVar() {
		return salCommVar;
	}

	public void setBookNbrList(List bookNbrList) {
		this.bookNbrList.addAll(bookNbrList);
	}

	public void setGameName(String gameName) {
		this.gameName = gameName;
	}

	public void setNbrBooks(int nbrBooks) {
		this.nbrBooks = nbrBooks;
	}

	public void setPackNbrList(String packNbrList) {
		this.packNbrList.add(packNbrList);
	}

	public void setSalCommVar(String salCommVar) {
		this.salCommVar = salCommVar;
	}

}
