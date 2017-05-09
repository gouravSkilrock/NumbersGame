package com.skilrock.lms.dge.beans;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class MerchantWiseSaleWinningBean implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Map<String, Map<String, SaleWinningBean>> dataMap;
    private List<String> gameList;
    private Map<String, Map<String,Map <String,SaleWinningBean>>> merchantGameWalletData;
    private boolean response;
    private String archivingDate;



	public String getArchivingDate() {
		return archivingDate;
	}

	public void setArchivingDate(String archivingDate) {
		this.archivingDate = archivingDate;
	}

	public boolean isResponse() {
		return response;
	}

	public void setResponse(boolean response) {
		this.response = response;
	}

	public Map<String, Map<String, Map<String, SaleWinningBean>>> getMerchantGameWalletData() {
		return merchantGameWalletData;
	}

	public void setMerchantGameWalletData(
			Map<String, Map<String, Map<String, SaleWinningBean>>> merchantGameWalletData) {
		this.merchantGameWalletData = merchantGameWalletData;
	}

	public Map<String, Map<String, SaleWinningBean>> getDataMap() {
		return dataMap;
	}

	public void setDataMap(Map<String, Map<String, SaleWinningBean>> dataMap) {
		this.dataMap = dataMap;
	}

	public void setGameList(List<String> gameList) {
		this.gameList = gameList;
	}

	public List<String> getGameList() {
		return gameList;
	}
}
