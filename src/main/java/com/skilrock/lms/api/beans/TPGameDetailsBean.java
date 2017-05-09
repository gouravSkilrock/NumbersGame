package com.skilrock.lms.api.beans;

import java.io.Serializable;
import java.util.List;

public class TPGameDetailsBean implements Serializable {
	
	
	private static final long serialVersionUID = 1L;
	private List<GameInfoBean> gameBeanList;
	private String nextTimeTogGetInfo;
	private String errorCode;
	private boolean isSuccess;
	public List<GameInfoBean> getGameBeanList() {
		return gameBeanList;
	}
	public String getNextTimeTogGetInfo() {
		return nextTimeTogGetInfo;
	}
	
	public boolean isSuccess() {
		return isSuccess;
	}
	public void setGameBeanList(List<GameInfoBean> gameBeanList) {
		this.gameBeanList = gameBeanList;
	}
	public void setNextTimeTogGetInfo(String nextTimeTogGetInfo) {
		this.nextTimeTogGetInfo = nextTimeTogGetInfo;
	}
	public void setSuccess(boolean isSuccess) {
		this.isSuccess = isSuccess;
	}
	public String getErrorCode() {
		return errorCode;
	}
	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}
	
	
}
