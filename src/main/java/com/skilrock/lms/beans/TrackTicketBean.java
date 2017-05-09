package com.skilrock.lms.beans;

public class TrackTicketBean {

	private String book_nbr;
	private String claimed_at;
	private String claimed_by;
	private String claimed_on_date;
	private int game_id;
	private String game_name;
	private String isTicketFormatValid;
	private String remarks;
	private String status;
	private String ticket_nbr;

	public String getBook_nbr() {
		return book_nbr;
	}

	public String getClaimed_at() {
		return claimed_at;
	}

	public String getClaimed_by() {
		return claimed_by;
	}

	public String getClaimed_on_date() {
		return claimed_on_date;
	}

	public int getGame_id() {
		return game_id;
	}

	public String getGame_name() {
		return game_name;
	}

	public String getIsTicketFormatValid() {
		return isTicketFormatValid;
	}

	public String getRemarks() {
		return remarks;
	}

	public String getStatus() {
		return status;
	}

	public String getTicket_nbr() {
		return ticket_nbr;
	}

	public void setBook_nbr(String book_nbr) {
		this.book_nbr = book_nbr;
	}

	public void setClaimed_at(String claimed_at) {
		this.claimed_at = claimed_at;
	}

	public void setClaimed_by(String claimed_by) {
		this.claimed_by = claimed_by;
	}

	public void setClaimed_on_date(String claimed_on_date) {
		this.claimed_on_date = claimed_on_date;
	}

	public void setGame_id(int game_id) {
		this.game_id = game_id;
	}

	public void setGame_name(String game_name) {
		this.game_name = game_name;
	}

	public void setIsTicketFormatValid(String isTicketFormatValid) {
		this.isTicketFormatValid = isTicketFormatValid;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public void setTicket_nbr(String ticket_nbr) {
		this.ticket_nbr = ticket_nbr;
	}

}