package com.skilrock.lms.web.scratchService.pwtMgmt.common;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.opensymphony.xwork2.ActionSupport;
import com.skilrock.lms.beans.GamePlayerPWTBean;
import com.skilrock.lms.beans.PrizeStatusBean;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.coreEngine.scratchService.pwtMgmt.common.ApprovePlayerPWTHelper;

public class ApprovePlayerPWTAction extends ActionSupport {

	static Log logger = LogFactory.getLog(ApprovePlayerPWTAction.class);

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String denyPwtStatus;
	private int gameId;
	private int gameNbr;
	List<GamePlayerPWTBean> gamePlayerBeanList;
	GamePlayerPWTBean gamePlrDetaillbean;
	private int playerReceiptId;

	private int plrId;

	private List<PrizeStatusBean> prizeStatusList;
	private double pwtAmt;
	private String ticketNbr;

	private String virnCode;

	public String approvePWT() {
		System.out.println("inside approve pwt");
		ApprovePlayerPWTHelper appPwtHelper = new ApprovePlayerPWTHelper();
		boolean isupdateDone = appPwtHelper.approvePWT(playerReceiptId);
		if (isupdateDone) {
			return SUCCESS;
		} else {
			return ERROR;
		}
	}

	public String denyPWT() throws LMSException {
		System.out.println("inside deny pwt");
		ApprovePlayerPWTHelper appPwtHelper = new ApprovePlayerPWTHelper();
		boolean isupdateDone = appPwtHelper.denyPWT(playerReceiptId, gameId,
				virnCode, ticketNbr, denyPwtStatus, gameNbr);
		if (isupdateDone) {
			return SUCCESS;
		} else {
			return ERROR;
		}
	}

	public String getDenyPwtStatus() {
		return denyPwtStatus;
	}

	public int getGameId() {
		return gameId;
	}

	public int getGameNbr() {
		return gameNbr;
	}

	public List<GamePlayerPWTBean> getGamePlayerBeanList() {
		return gamePlayerBeanList;
	}

	public GamePlayerPWTBean getGamePlrDetaillbean() {
		return gamePlrDetaillbean;
	}

	public int getPlayerReceiptId() {
		return playerReceiptId;
	}

	public int getPlrId() {
		return plrId;
	}

	public List<PrizeStatusBean> getPrizeStatusList() {
		return prizeStatusList;
	}

	public double getPwtAmt() {
		return pwtAmt;
	}

	public String getTicketNbr() {
		return ticketNbr;
	}

	public String getUnapprovedPwt() throws LMSException {

		ApprovePlayerPWTHelper appPwtHelper = new ApprovePlayerPWTHelper();
		gamePlayerBeanList = appPwtHelper.getUnapprovedPwt();
		setGamePlayerBeanList(gamePlayerBeanList);

		return SUCCESS;
	}

	public String getUnapprovedPwtDetails() throws LMSException {

		ApprovePlayerPWTHelper appPwtHelper = new ApprovePlayerPWTHelper();
		gamePlrDetaillbean = appPwtHelper
				.getUnapprovedPwtDetails(plrId, gameId);
		gamePlrDetaillbean.setPwtAmt(pwtAmt);
		gamePlrDetaillbean.setTicketNbr(ticketNbr);
		gamePlrDetaillbean.setPlayerReceiptId(playerReceiptId);
		gamePlrDetaillbean.setGameId(gameId);
		gamePlrDetaillbean.setVirnCode(virnCode);
		setGamePlrDetaillbean(gamePlrDetaillbean);

		List<PrizeStatusBean> prizeList = appPwtHelper
				.fetchRemainingPrizeList(gameId);
		if (prizeList != null) {
			setPrizeStatusList(prizeList);
		}

		return SUCCESS;

	}

	public String getVirnCode() {
		return virnCode;
	}

	public void setDenyPwtStatus(String denyPwtStatus) {
		this.denyPwtStatus = denyPwtStatus;
	}

	public void setGameId(int gameId) {
		this.gameId = gameId;
	}

	public void setGameNbr(int gameNbr) {
		this.gameNbr = gameNbr;
	}

	public void setGamePlayerBeanList(List<GamePlayerPWTBean> gamePlayerBeanList) {
		this.gamePlayerBeanList = gamePlayerBeanList;
	}

	public void setGamePlrDetaillbean(GamePlayerPWTBean gamePlrDetaillbean) {
		this.gamePlrDetaillbean = gamePlrDetaillbean;
	}

	public void setPlayerReceiptId(int playerReceiptId) {
		this.playerReceiptId = playerReceiptId;
	}

	public void setPlrId(int plrId) {
		this.plrId = plrId;
	}

	public void setPrizeStatusList(List<PrizeStatusBean> prizeStatusList) {
		this.prizeStatusList = prizeStatusList;
	}

	public void setPwtAmt(double pwtAmt) {
		this.pwtAmt = pwtAmt;
	}

	public void setTicketNbr(String ticketNbr) {
		this.ticketNbr = ticketNbr;
	}

	public void setVirnCode(String virnCode) {
		this.virnCode = virnCode;
	}

}