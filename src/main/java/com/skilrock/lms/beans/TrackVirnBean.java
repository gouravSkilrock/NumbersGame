package com.skilrock.lms.beans;

public class TrackVirnBean {
	private String claimed_at;
	private String claimed_by;
	private String claimed_on_date;
	private String encoded_virn_code;
	private String game_name;
	private String pwt_amt;
	private String remarks;
	private String status;
	private String virn_code;

	public String getClaimed_at() {
		return claimed_at;
	}

	public String getClaimed_by() {
		return claimed_by;
	}

	public String getClaimed_on_date() {
		return claimed_on_date;
	}

	public String getEncoded_virn_code() {
		return encoded_virn_code;
	}

	public String getGame_name() {
		return game_name;
	}

	public String getPwt_amt() {
		return pwt_amt;
	}

	public String getRemarks() {
		return remarks;
	}

	public String getStatus() {
		return status;
	}

	public String getVirn_code() {
		return virn_code;
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

	public void setEncoded_virn_code(String encoded_virn_code) {
		this.encoded_virn_code = encoded_virn_code;
	}

	public void setGame_name(String game_name) {
		this.game_name = game_name;
	}

	public void setPwt_amt(String pwt_amt) {
		this.pwt_amt = pwt_amt;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public void setVirn_code(String virn_code) {
		this.virn_code = virn_code;
	}

}