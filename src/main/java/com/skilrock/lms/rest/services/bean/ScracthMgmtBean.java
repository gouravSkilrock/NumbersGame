package com.skilrock.lms.rest.services.bean;

import java.util.List;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class ScracthMgmtBean extends SuccessJsonResponse {

	private long requestId;
	private String tpUserId;
	@NotNull
	@Size(min=1)
	private String dlNumber;
	@Size(min=1)
	private List<String> bookList;
	private String bookNumber;
	private String tpTransactionId;
	private String ticketNumber;
	
	
	public String getTicketNumber() {
		return ticketNumber;
	}
	public void setTicketNumber(String ticketNumber) {
		this.ticketNumber = ticketNumber;
	}
	public String getTpTransactionId() {
		return tpTransactionId;
	}
	public void setTpTransactionId(String tpTransactionId) {
		this.tpTransactionId = tpTransactionId;
	}
	public String getBookNumber() {
		return bookNumber;
	}
	public void setBookNumber(String bookNumber) {
		this.bookNumber = bookNumber;
	}
	public long getRequestId() {
		return requestId;
	}
	public void setRequestId(long requestId) {
		this.requestId = requestId;
	}
	public String getTpUserId() {
		return tpUserId;
	}
	public void setTpUserId(String tpUserId) {
		this.tpUserId = tpUserId;
	}
	public String getDlNumber() {
		return dlNumber;
	}
	public void setDlNumber(String dlNumber) {
		this.dlNumber = dlNumber;
	}
	public List<String> getBookList() {
		return bookList;
	}
	public void setBookList(List<String> bookList) {
		this.bookList = bookList;
	}
	
}
