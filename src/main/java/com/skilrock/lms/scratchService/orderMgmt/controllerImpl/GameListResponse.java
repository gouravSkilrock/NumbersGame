package com.skilrock.lms.scratchService.orderMgmt.controllerImpl;

import java.util.ArrayList;
import java.util.List;

public class GameListResponse {
	
	private int  requestId;
	private int responseCode;
	List<GameListResponseData> responseData=new ArrayList<GameListResponseData>();
	
	
	
	
	public int getRequestId() {
		return requestId;
	}
	public void setRequestId(int requestId) {
		this.requestId = requestId;
	}
	public int getResponseCode() {
		return responseCode;
	}
	public void setResponseCode(int responseCode) {
		this.responseCode = responseCode;
	}
	public List<GameListResponseData> getResponseData() {
		return responseData;
	}
	public void setResponseData(List<GameListResponseData> responseData) {
		this.responseData = responseData;
	}
	
	
	@Override
	public String toString() {
		return "GameListResponse [requestId=" + requestId + ", responseCode=" + responseCode + ", responseData="
				+ responseData + "]";
	}
	
	

}
