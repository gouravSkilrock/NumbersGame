package com.skilrock.lms.beans;

public class CashCardPinBean {
	private String filePath;
	private String returnType;
	private long startSerialNumber ;
	private long endSerialNumber ;
	private boolean isSuccess;
	public boolean isSuccess() {
		return isSuccess;
	}
	public void setSuccess(boolean isSuccess) {
		this.isSuccess = isSuccess;
	}
	public String getReturnType() {
		return returnType;
	}
	public void setReturnType(String returnType) {
		this.returnType = returnType;
	}
	
	
	public long getStartSerialNumber() {
		return startSerialNumber;
	}
	public void setStartSerialNumber(long startSerialNumber) {
		this.startSerialNumber = startSerialNumber;
	}
	public long getEndSerialNumber() {
		return endSerialNumber;
	}
	public void setEndSerialNumber(long endSerialNumber) {
		this.endSerialNumber = endSerialNumber;
	}
	public String getFilePath() {
		return filePath;
	}
	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
	
	

}
