package com.skilrock.lms.beans;

import java.io.Serializable;

public class RetActivityColumnStatusBean implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private boolean DG;
	private boolean SE;
	private boolean OLA;
	private boolean CS;
	private boolean SLE;
	private boolean IW;
	private boolean VS;
	private boolean Sale;
	private boolean Pwt;
	private boolean Login;
	private boolean HeartBeat;

	public boolean isDG() {
		return DG;
	}

	public void setDG(boolean dG) {
		DG = dG;
	}

	public boolean isSE() {
		return SE;
	}

	public void setSE(boolean sE) {
		SE = sE;
	}

	public boolean isOLA() {
		return OLA;
	}

	public void setOLA(boolean oLA) {
		OLA = oLA;
	}

	public boolean isSLE() {
		return SLE;
	}

	public void setSLE(boolean sLE) {
		SLE = sLE;
	}

	public boolean isCS() {
		return CS;
	}

	public void setCS(boolean cS) {
		CS = cS;
	}

	public boolean isSale() {
		return Sale;
	}

	public void setSale(boolean sale) {
		Sale = sale;
	}

	public boolean isPwt() {
		return Pwt;
	}

	public void setPwt(boolean pwt) {
		Pwt = pwt;
	}

	public boolean isLogin() {
		return Login;
	}

	public void setLogin(boolean login) {
		Login = login;
	}

	public boolean isHeartBeat() {
		return HeartBeat;
	}

	public void setHeartBeat(boolean heartBeat) {
		HeartBeat = heartBeat;
	}

	public boolean isIW() {
		return IW;
	}

	public void setIW(boolean iW) {
		IW = iW;
	}

	public boolean isVS() {
		return VS;
	}

	public void setVS(boolean vS) {
		VS = vS;
	}

	@Override
	public String toString() {
		return "RetActivityColumnStatusBean [DG=" + DG + ", SE=" + SE
				+ ", OLA=" + OLA + ", CS=" + CS + ", SLE=" + SLE + ", IW=" + IW
				+ ", VS=" + VS + ", Sale=" + Sale + ", Pwt=" + Pwt + ", Login="
				+ Login + ", HeartBeat=" + HeartBeat + "]";
	}

}
