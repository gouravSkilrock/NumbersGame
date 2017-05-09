package com.skilrock.lms.beans;

import java.io.Serializable;

public class PackSeriesFlagBean implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private boolean finalflag = true;
	private boolean gamenbrflag = true;
	private boolean gamenbrformatflag = true;
	private boolean packdigitformatflag = true;
	private String packfrn = null;
	private boolean packseriespresenceflag = true;
	private String packton = null;
	private String status = null;
	private int totpk = 0;

	public String getPackfrn() {
		return packfrn;
	}

	public String getPackton() {
		return packton;
	}

	public String getStatus() {
		return status;
	}

	public int getTotpk() {
		return totpk;
	}

	public boolean isFinalflag() {
		return finalflag;
	}

	public boolean isGamenbrflag() {
		return gamenbrflag;
	}

	public boolean isGamenbrformatflag() {
		return gamenbrformatflag;
	}

	public boolean isPackdigitformatflag() {
		return packdigitformatflag;
	}

	public boolean isPackseriespresenceflag() {
		return packseriespresenceflag;
	}

	public void setFinalflag(boolean finalflag) {
		this.finalflag = finalflag;
	}

	public void setGamenbrflag(boolean gamenbrflag) {
		this.gamenbrflag = gamenbrflag;
	}

	public void setGamenbrformatflag(boolean gamenbrformatflag) {
		this.gamenbrformatflag = gamenbrformatflag;
	}

	public void setPackdigitformatflag(boolean packdigitformatflag) {
		this.packdigitformatflag = packdigitformatflag;
	}

	public void setPackfrn(String packfrn) {
		this.packfrn = packfrn;
	}

	public void setPackseriespresenceflag(boolean packseriespresenceflag) {
		this.packseriespresenceflag = packseriespresenceflag;
	}

	public void setPackton(String packton) {
		this.packton = packton;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public void setTotpk(int totpk) {
		this.totpk = totpk;
	}
}
