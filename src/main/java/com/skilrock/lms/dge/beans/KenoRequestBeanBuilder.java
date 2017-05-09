package com.skilrock.lms.dge.beans;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class KenoRequestBeanBuilder implements Serializable {
	private static final long serialVersionUID = 1L;
	private int[] betAmountMultiple;
	Map<Integer, Map<Integer, String>> drawIdTableMap;
	private int game_no;
	private int gameId;
	private int isAdvancedPlay;
	private int[] isQuickPick;
	private int noOfDraws;
	private String[] noPicked;
	private int partyId;
	private String partyType;
	private String[] playerData;
	private String[] playType;
	private String purchaseChannel;
	private String refMerchantId;
	private String refTransId;
	private double totalPurchaseAmt;
	private double[] unitPrice;
	private int userId;
	private int userMappingId;
	private int serviceId;
	private boolean isPromotkt;
	private List<String> drawDateTime;
	private boolean [] QPPreGenerated;
	

	public KenoRequestBeanBuilder setQPPreGenerated(boolean[] qPPreGenerated) {
		QPPreGenerated = qPPreGenerated;
		return this;
	}


	public KenoRequestBeanBuilder setBetAmountMultiple(int[] betAmountMultiple) {
		this.betAmountMultiple = betAmountMultiple;
		return this;
	}

	public KenoRequestBeanBuilder setDrawIdTableMap(Map<Integer, Map<Integer, String>> drawIdTableMap) {
		this.drawIdTableMap = drawIdTableMap;
		return this;
	}

	public KenoRequestBeanBuilder setGame_no(int gameNo) {
		game_no = gameNo;
		return this;
	}

	public KenoRequestBeanBuilder setGameId(int gameId) {
		this.gameId = gameId;
		return this;
	}

	public KenoRequestBeanBuilder setIsAdvancedPlay(int isAdvancedPlay) {
		this.isAdvancedPlay = isAdvancedPlay;
		return this;
	}

	public KenoRequestBeanBuilder setIsQuickPick(int[] isQuickPick) {
		this.isQuickPick = isQuickPick;
		return this;
	}

	public KenoRequestBeanBuilder setNoOfDraws(int noOfDraws) {
		this.noOfDraws = noOfDraws;
		return this;
	}

	public KenoRequestBeanBuilder setNoPicked(String[] noPicked) {
		this.noPicked = noPicked;
		return this;
	}

	public KenoRequestBeanBuilder setPartyId(int partyId) {
		this.partyId = partyId;
		return this;
	}

	public KenoRequestBeanBuilder setPartyType(String partyType) {
		this.partyType = partyType;
		return this;
	}

	public KenoRequestBeanBuilder setPlayerData(String[] playerData) {
		this.playerData = playerData;
		return this;
	}

	public KenoRequestBeanBuilder setPlayType(String[] playType) {
		this.playType = playType;
		return this;
	}

	public KenoRequestBeanBuilder setPurchaseChannel(String purchaseChannel) {
		this.purchaseChannel = purchaseChannel;
		return this;
	}

	public KenoRequestBeanBuilder setRefMerchantId(String refMerchantId) {
		this.refMerchantId = refMerchantId;
		return this;
	}

	public KenoRequestBeanBuilder setRefTransId(String refTransId) {
		this.refTransId = refTransId;
		return this;
	}

	public KenoRequestBeanBuilder setTotalPurchaseAmt(double totalPurchaseAmt) {
		this.totalPurchaseAmt = totalPurchaseAmt;
		return this;
	}

	public KenoRequestBeanBuilder setUnitPrice(double[] unitPrice) {
		this.unitPrice = unitPrice;
		return this;
	}
	
	public KenoRequestBeanBuilder setUserId(int userId) {
		this.userId = userId;
		return this;
	}

	public KenoRequestBeanBuilder setUserMappingId(int userMappingId) {
		this.userMappingId = userMappingId;
		return this;
	}

	public KenoRequestBeanBuilder setServiceId(int serviceId) {
		this.serviceId = serviceId;
		return this;
	}

	public KenoRequestBeanBuilder setPromotkt(boolean isPromotkt) {
		this.isPromotkt = isPromotkt;
		return this;
	}

	public KenoRequestBeanBuilder setDrawDateTime(List<String> drawDateTime) {
		this.drawDateTime = drawDateTime;
		return this;
	}
	
	  public KenoRequestBean preapreKenoRequestBean() {
	      return new  KenoRequestBean(betAmountMultiple, drawIdTableMap, game_no, gameId, isAdvancedPlay, isQuickPick, noOfDraws, noPicked, partyId, partyType, playerData, playType, purchaseChannel, refMerchantId, refTransId, totalPurchaseAmt, unitPrice, userId, userMappingId, serviceId, isPromotkt, drawDateTime, QPPreGenerated);
	   }
}