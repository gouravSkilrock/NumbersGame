package com.skilrock.lms.web.drawGames.playMgmt;

import java.util.List;

import com.skilrock.lms.beans.UserInfoBean;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class ZimLottoBonusRequest {
	private JSONObject commonSaleData;
	private JSONArray betTypeData;
	private String totalPurchaseAmount;
	private String tokenId;
	private UserInfoBean userBean;
	private JSONObject jsonResponse;
	private String[] numbersPicked;
	private String playType;
	private List<String> drawDateTime;
	private String[] drawId;
	private Integer[] isQuickPick;
	private int[] betAmountMultiple;
	private int betAmount;
	private int numberPicked;
	private boolean qpPreGenerated;
	private int isAdvancedPlay;

	public ZimLottoBonusRequest(JSONObject jsonResponse, List<String> drawDateTime) {
		this.jsonResponse = jsonResponse;
		this.drawDateTime = drawDateTime;
	}

	public JSONObject getCommonSaleData() {
		return commonSaleData;
	}

	public void setCommonSaleData(JSONObject commonSaleData) {
		this.commonSaleData = commonSaleData;
	}

	public JSONArray getBetTypeData() {
		return betTypeData;
	}

	public void setBetTypeData(JSONArray betTypeData) {
		this.betTypeData = betTypeData;
	}

	public String getTotalPurchaseAmount() {
		return totalPurchaseAmount;
	}

	public void setTotalPurchaseAmount(String totalPurchaseAmount) {
		this.totalPurchaseAmount = totalPurchaseAmount;
	}

	public String getTokenId() {
		return tokenId;
	}

	public void setTokenId(String tokenId) {
		this.tokenId = tokenId;
	}

	public UserInfoBean getUserBean() {
		return userBean;
	}

	public void setUserBean(UserInfoBean userBean) {
		this.userBean = userBean;
	}

	public JSONObject getJsonResponse() {
		return jsonResponse;
	}

	public void setJsonResponse(JSONObject jsonResponse) {
		this.jsonResponse = jsonResponse;
	}

	public String[] getNumbersPicked() {
		return numbersPicked;
	}

	public void setNumbersPicked(String[] numbersPicked) {
		this.numbersPicked = numbersPicked;
	}

	public String getPlayType() {
		return playType;
	}

	public void setPlayType(String playType) {
		this.playType = playType;
	}

	public List<String> getDrawDateTime() {
		return drawDateTime;
	}

	public void setDrawDateTime(List<String> drawDateTime) {
		this.drawDateTime = drawDateTime;
	}

	public String[] getDrawId() {
		return drawId;
	}

	public void setDrawId(String[] drawId) {
		this.drawId = drawId;
	}

	public Integer[] getIsQuickPick() {
		return isQuickPick;
	}

	public void setIsQuickPick(Integer[] isQuickPick) {
		this.isQuickPick = isQuickPick;
	}

	public int[] getBetAmountMultiple() {
		return betAmountMultiple;
	}

	public void setBetAmountMultiple(int[] betAmountMultiple) {
		this.betAmountMultiple = betAmountMultiple;
	}

	public int getBetAmount() {
		return betAmount;
	}

	public void setBetAmount(int betAmount) {
		this.betAmount = betAmount;
	}

	public int getNumberPicked() {
		return numberPicked;
	}

	public void setNumberPicked(int numberPicked) {
		this.numberPicked = numberPicked;
	}

	public boolean isQpPreGenerated() {
		return qpPreGenerated;
	}

	public void setQpPreGenerated(boolean qpPreGenerated) {
		this.qpPreGenerated = qpPreGenerated;
	}

	public int getIsAdvancedPlay() {
		return isAdvancedPlay;
	}

	public void setIsAdvancedPlay(int isAdvancedPlay) {
		this.isAdvancedPlay = isAdvancedPlay;
	}
}