package com.skilrock.lms.embedded.drawGames.playMgmt;

import java.io.IOException;
import java.text.NumberFormat;
import java.util.Map;

import javax.servlet.ServletContext;

import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.ActionContext;
import com.skilrock.lms.beans.RafflePurchaseBean;
import com.skilrock.lms.beans.UserInfoBean;
import com.skilrock.lms.common.BaseAction;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.common.utility.LMSUtility;
import com.skilrock.lms.coreEngine.drawGames.common.DGErrorMsg;
import com.skilrock.lms.coreEngine.drawGames.playMgmt.DrawGameRPOSHelper;
import com.skilrock.lms.coreEngine.drawGames.playMgmt.RaffleSecondChanceHelper;
import com.skilrock.lms.embedded.drawGames.common.EmbeddedErrors;
import com.skilrock.lms.web.drawGames.common.Util;
import com.skilrock.lms.web.drawGames.common.UtilApplet;

public class RaffleAction extends BaseAction {
	private static final long serialVersionUID = 1L;

	private int gameId=0;
	private RafflePurchaseBean rafflePurchaseBean;
	private long LSTktNo;
	private String userName;
	private String ticketNo;
	private String virnCode;
	private String serviceName;
	private String plrMobileNumber;

	public RaffleAction() {
		super(RaffleAction.class);
	}

	public RafflePurchaseBean getRafflePurchaseBean() {
		return rafflePurchaseBean;
	}

	public void setRafflePurchaseBean(RafflePurchaseBean rafflePurchaseBean) {
		this.rafflePurchaseBean = rafflePurchaseBean;
	}

	public long getLSTktNo() {
		return LSTktNo;
	}

	public void setLSTktNo(long lSTktNo) {
		LSTktNo = lSTktNo;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getTicketNo() {
		return ticketNo;
	}

	public void setTicketNo(String ticketNo) {
		this.ticketNo = ticketNo;
	}

	public String getVirnCode() {
		return virnCode;
	}

	public void setVirnCode(String virnCode) {
		this.virnCode = virnCode;
	}

	public String getServiceName() {
		return serviceName;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	public String getPlrMobileNumber() {
		return plrMobileNumber;
	}

	public void setPlrMobileNumber(String plrMobileNumber) {
		this.plrMobileNumber = plrMobileNumber;
	}

	@SuppressWarnings("unchecked")
	public void purchaseTicketProcess() {
		String gameDevName = null;
		try {
			ServletContext sc = ServletActionContext.getServletContext();
			String isDraw = (String) sc.getAttribute("IS_DRAW");
			if (isDraw.equalsIgnoreCase("NO")) {
				response.getOutputStream().write(
						("ErrorMsg:" + EmbeddedErrors.DRAW_GAME_NOT_AVAILABLE)
								.getBytes());
				return;
			}

			RaffleSecondChanceHelper helper = new RaffleSecondChanceHelper();
			gameDevName = helper.getSecondChanceGameName(serviceName);
			if(gameDevName == null) {
				response.getOutputStream().write(("ErrorMsg:" + EmbeddedErrors.SALE_NOT_ALLOWED_ERROR).getBytes());
				return;
			}
			gameId = Util.getGameId(gameDevName);

			UserInfoBean userBean = getUserBean(userName);
			Map<Integer, Map<Integer, String>> drawIdTableMap = (Map<Integer, Map<Integer, String>>) sc.getAttribute("drawIdTableMap");
			String refMerchantId = (String) sc.getAttribute("REF_MERCHANT_ID");
			int autoCancelHoldDays = Integer.parseInt((String) sc.getAttribute("AUTO_CANCEL_CLOSER_DAYS"));
			String actionName=ActionContext.getContext().getName();
			long lastPrintedTicket = 0;
			int lstGameId = 0;
			if (LSTktNo != 0) {
				lastPrintedTicket = LSTktNo / Util.getDivValueForLastSoldTkt(String.valueOf(LSTktNo).length());
				lstGameId = Util.getGameIdFromGameNumber(Util.getGamenoFromTktnumber(String.valueOf(LSTktNo)));
				new DrawGameRPOSHelper().checkLastPrintedTicketStatusAndUpdate(userBean, lastPrintedTicket, "TERMINAL", refMerchantId, autoCancelHoldDays, actionName, lstGameId);
			}

			//String countryDeployed = (String) sc.getAttribute("COUNTRY_DEPLOYED");

			int inputType = Integer.parseInt((String) sc.getAttribute("InpType"));
			ticketNo = Util.getTicketNumber(ticketNo, inputType);

			rafflePurchaseBean = new RafflePurchaseBean();
			rafflePurchaseBean.setParentTktNo(ticketNo);
			rafflePurchaseBean.setVirnCode(virnCode);
			rafflePurchaseBean.setGameId(gameId);
			rafflePurchaseBean.setGame_no(Util.getGameNumberFromGameId(gameId));
			rafflePurchaseBean.setGameDispName(Util.getGameDisplayName(gameId));
			rafflePurchaseBean.setRaffle_no(gameId);
			int serviceId = Util.getServiceIdFormCode(request.getAttribute("code").toString());
			rafflePurchaseBean.setServiceId(serviceId);
			rafflePurchaseBean.setUserMappingId(userBean.getCurrentUserMappingId());
			rafflePurchaseBean.setServiceName(serviceName);
			rafflePurchaseBean.setPartyId(userBean.getUserOrgId());
			rafflePurchaseBean.setPartyType("RETAILER");
			rafflePurchaseBean.setUserId(userBean.getUserId());
			rafflePurchaseBean.setRefMerchantId((String) sc.getAttribute("REF_MERCHANT_ID"));
			rafflePurchaseBean.setPurchaseChannel("LMS_Terminal");
			rafflePurchaseBean.setPlrMobileNumber(plrMobileNumber);
			rafflePurchaseBean.setDrawIdTableMap(drawIdTableMap);
			rafflePurchaseBean.setNoOfDrawPlayedFor(1);
			rafflePurchaseBean.setAdvancedPlay(false);
			rafflePurchaseBean.setBetAmountMultiple(new int[]{1});
			rafflePurchaseBean.setUnitPrice(0.0);
			rafflePurchaseBean.setTicketPrice(0.0);
			rafflePurchaseBean.setTotalPurchaseAmt(0.0);
			rafflePurchaseBean.setPromotkt(true);
			String barcodeType = (String) LMSUtility.sc.getAttribute("BARCODE_TYPE");
			rafflePurchaseBean.setBarcodeType(barcodeType);

			rafflePurchaseBean = helper.rafflePurchaseTicket(userBean, rafflePurchaseBean);
			setRafflePurchaseBean(rafflePurchaseBean);

			String finalPurchaseData = null;
			String saleStatus = rafflePurchaseBean.getSaleStatus();
			if (!"SUCCESS".equalsIgnoreCase(saleStatus)) {
				finalPurchaseData = "ErrorMsg:"
						+ DGErrorMsg.buyErrMsg(saleStatus) + "|";
				response.getOutputStream().write(finalPurchaseData.getBytes());
				return;
			}

			NumberFormat nf = NumberFormat.getInstance();
			nf.setMinimumFractionDigits(2);
			double bal = userBean.getAvailableCreditLimit() - userBean.getClaimableBal();
			String balance = nf.format(bal).replaceAll(",", "");

			StringBuilder topMsgsStr = new StringBuilder("");
			StringBuilder bottomMsgsStr = new StringBuilder("");
			String advtMsg = "";

			UtilApplet.getAdvMsgs(rafflePurchaseBean.getAdvMsg(), topMsgsStr, bottomMsgsStr, 10);

			if (topMsgsStr.length() != 0) {
				advtMsg = "topAdvMsg:" + topMsgsStr + "|";
			}

			if (bottomMsgsStr.length() != 0) {
				advtMsg = advtMsg + "bottomAdvMsg:" + bottomMsgsStr + "|";
			}

			String time = rafflePurchaseBean.getPurchaseTime().replace(" ","|Time:");
			String drawDateTime = rafflePurchaseBean.getDrawDateTime().split("\\*")[0].replace(" ","|DrawTime:");
			String drawName = rafflePurchaseBean.getDrawDateTime().split("\\*")[1];

			StringBuilder finalData = new StringBuilder("RaffleData:");
			finalData.append("TicNo:").append(rafflePurchaseBean.getRaffleTicket_no()).append(rafflePurchaseBean.getReprintCount());
			finalData.append("|brCd:").append(rafflePurchaseBean.getRaffleTicket_no()).append(rafflePurchaseBean.getReprintCount()).append(rafflePurchaseBean.getBarcodeCount());
			finalData.append("|Date:").append(time);
			finalData.append("|GameName:").append(rafflePurchaseBean.getGameDispName());
			finalData.append("|DrawDate:").append(drawDateTime);
			finalData.append("|DrawName:").append(drawName);
			finalData.append("|").append(advtMsg);

			finalPurchaseData = finalData.toString();
			request.setAttribute("purchaseData", finalPurchaseData);
			response.getOutputStream().write(finalPurchaseData.getBytes());
		} catch (IOException e) {
			e.printStackTrace();
			try {
				response.getOutputStream().write("Error!Try Again".getBytes());
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			return;
		} catch (LMSException e) {
			e.printStackTrace();
			try {
				//response.getOutputStream().write("Error!Try Again".getBytes());
				response.getOutputStream().write(("ErrorMsg:"+e.getErrorMessage()).getBytes());
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			return;
		} catch (Exception e) {
			e.printStackTrace();
			try {
				response.getOutputStream().write("Error!Try Again".getBytes());
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			return;
		}
	}
}