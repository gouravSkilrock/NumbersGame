package com.skilrock.lms.scratch.orderBeans;

import com.skilrock.lms.common.exception.LMSErrors;
import com.skilrock.lms.common.exception.LMSException;

public class OrderListBean {
	
	private int gameId;
	private int noOfItems;
	
	public int getGameId() {
		return gameId;
	}
	public void setGameId(int gameId) {
		this.gameId = gameId;
	}
	public int getNoOfItems() {
		return noOfItems;
	}
	public void setNoOfItems(int noOfItems) {
		this.noOfItems = noOfItems;
	}
	
	public void validateGameId() throws LMSException{
		if(this.getGameId() == 0){
		  throw new LMSException(LMSErrors.INVALID_DATA_ERROR_CODE, "GameId Is Not Present");	
		}
	}
	
	public void validateNoOfItems() throws LMSException{
		if(this.getNoOfItems() == 0){
		  throw new LMSException(LMSErrors.INVALID_DATA_ERROR_CODE, "Nunber Of Items Is Not Present");	
		}
	}
}
