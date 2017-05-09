package com.skilrock.lms.web.drawGames.playMgmt;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.interceptor.ServletRequestAware;

import com.opensymphony.xwork2.ActionSupport;
import com.skilrock.lms.beans.UserInfoBean;
import com.skilrock.lms.coreEngine.drawGames.common.DGErrorMsg;
import com.skilrock.lms.coreEngine.drawGames.playMgmt.DrawGameRPOSHelper;
import com.skilrock.lms.dge.beans.KenoPurchaseBean;
import com.skilrock.lms.dge.gameconstants.KenoConstants;
import com.skilrock.lms.rest.common.TransactionManager;
import com.skilrock.lms.web.drawGames.common.Util;

public class KenoAction extends ActionSupport implements ServletRequestAware {

	private static final long serialVersionUID = 1L;

	public static void main(String[] args) throws Exception {
		new KenoAction().purchaseTicketProcess();
	}

	public static List<String> rec(int start, int elementToChoose,
			int returnCnt, String[] indexArr, String[] elements,
			StringBuffer stbuff, List<String> comboList, String qp) {

		if (returnCnt == elementToChoose) {
			return comboList;
		}
		returnCnt++;
		for (int i = start; i < elements.length; i++) {

			indexArr[returnCnt - 1] = "" + i;
			if (returnCnt == elementToChoose) {

				stbuff = new StringBuffer();
				for (String element : indexArr) {
					stbuff.append("," + elements[Integer.parseInt(element)]);
				}
				stbuff.delete(0, 1);
				comboList.add(stbuff.toString());
				if ("No".equalsIgnoreCase(qp)) {
					comboList.add("Nxt");
				} else if ("Yes".equalsIgnoreCase(qp)) {
					comboList.add("QP");
				}
			}

			rec(++start, elementToChoose, returnCnt, indexArr, elements,
					stbuff, comboList, qp);
		}
		return comboList;
	}

	private String betAmt;
	private String[] drawIdArr;
	private String errMsg;
	private int gameNumber = Util.getGameId("Keno");
	private int isAdvancedPlay;
	private KenoPurchaseBean kenoPurchaseBean;
	Log logger = LogFactory.getLog(KenoAction.class);
	private int noOfDraws;
	private int noOfLines;
	private String noPicked;
	private String pickedNumbers;
	private String playType;
	private String QP;
	private HttpServletRequest request;

	private HttpServletResponse response;

	private String totalPurchaseAmt;
	
private String plrMobileNumber;
	
	
	
	
	public String getPlrMobileNumber() {
		return plrMobileNumber;
	}

	public void setPlrMobileNumber(String plrMobileNumber) {
		this.plrMobileNumber = plrMobileNumber;
	}

	public String getBetAmt() {
		return betAmt;
	}

	public String[] getDrawIdArr() {
		return drawIdArr;
	}

	public String getErrMsg() {
		return errMsg;
	}

	public int getGameNumber() {
		return gameNumber;
	}

	public int getIsAdvancedPlay() {
		return isAdvancedPlay;
	}

	public KenoPurchaseBean getKenoPurchaseBean() {
		return kenoPurchaseBean;
	}

	public void getLines() throws IOException {
		String[] indexArr = new String[2];
		StringBuffer stbuild = null;
		List<String> comboList = new ArrayList<String>();
		String[] numbArr = new String[Integer.parseInt(pickedNumbers)];
		comboList = rec(0, 2, 0, indexArr, numbArr, stbuild, comboList, "Line");
		PrintWriter out = getResponse().getWriter();
		logger.debug("lines******" + comboList.size());
		// logger.debug("lines******" + comboList.size());
		out.print(comboList.size());
	}

	public int getNoOfDraws() {
		return noOfDraws;
	}

	public int getNoOfLines() {
		return noOfLines;
	}

	public String getNoPicked() {
		return noPicked;
	}

	public String getPickedNumbers() {
		return pickedNumbers;
	}

	public String getPlayType() {
		return playType;
	}

	public String getQP() {
		return QP;
	}

	public HttpServletRequest getRequest() {
		return request;
	}

	public HttpServletResponse getResponse() {
		return response;
	}

	public String getTotalPurchaseAmt() {
		return totalPurchaseAmt;
	}

	@SuppressWarnings("unchecked")
	public String purchaseTicketProcess() throws Exception {
		logger.debug("Inside purchaseTicketProcess");
		HttpSession session = request.getSession();
		UserInfoBean userBean = (UserInfoBean) session
				.getAttribute("USER_INFO");
		ServletContext sc = ServletActionContext.getServletContext();
		Map<Integer, Map<Integer, String>> drawIdTableMap = (Map<Integer, Map<Integer, String>>) sc
				.getAttribute("drawIdTableMap");

		String refMerchantId = (String) sc.getAttribute("REF_MERCHANT_ID");
		logger.debug("pickedNumbers ::" + pickedNumbers);
		logger.debug("noOfDraws ::" + noOfDraws);
		logger.debug("totalPurchaseAmt ::" + totalPurchaseAmt);
		logger.debug("playType ::" + playType);
		logger.debug("QP ::" + QP);
		logger.debug("betAmt ::" + betAmt);
		logger.debug("noOfLines ::" + noOfLines);
		logger.debug("noPicked ::" + noPicked);

		String[] betAmtStr = betAmt.split("Nxt");
		String[] QPStr = QP.split("Nxt");
		String[] pickedNumbersArr = pickedNumbers.split("Nxt");
		String[] noPickedArr = noPicked.replaceAll(" ", "").split("Nxt");
		String[] playTypeArr = playType.split("Nxt");
		int noOfPanel = pickedNumbersArr.length;
		int[] betAmtArr = new int[noOfPanel];
		int[] QPArr = new int[noOfPanel];
		for (int i = 0; i < noOfPanel; i++) {
			betAmtArr[i] = Integer.parseInt(betAmtStr[i]);
			QPArr[i] = Integer.parseInt(QPStr[i]);
		}

		KenoPurchaseBean drawGamePurchaseBean = new KenoPurchaseBean();
		drawGamePurchaseBean.setGame_no(gameNumber);
		drawGamePurchaseBean.setGameDispName(Util
				.getGameDisplayName(gameNumber));
		drawGamePurchaseBean.setBetAmountMultiple(betAmtArr);
		drawGamePurchaseBean.setIsQuickPick(QPArr);
		drawGamePurchaseBean.setPlayerData(pickedNumbersArr);
		drawGamePurchaseBean.setNoPicked(noPickedArr);
		drawGamePurchaseBean.setPartyId(userBean.getUserOrgId());
		drawGamePurchaseBean.setPartyType(userBean.getUserType());
		drawGamePurchaseBean.setUserId(userBean.getUserId());
		drawGamePurchaseBean.setNoOfDraws(noOfDraws);
		drawGamePurchaseBean.setIsAdvancedPlay(isAdvancedPlay);
		drawGamePurchaseBean.setPlrMobileNumber(plrMobileNumber);
		if (drawIdArr != null) {
			drawGamePurchaseBean.setDrawDateTime(Arrays.asList(drawIdArr));
		}
		drawGamePurchaseBean.setRefMerchantId(refMerchantId);
		drawGamePurchaseBean.setPurchaseChannel("LMS_Web");
		drawGamePurchaseBean.setBonus("N");
		drawGamePurchaseBean.setTotalPurchaseAmt(Double
				.parseDouble(totalPurchaseAmt));
		drawGamePurchaseBean.setPlayType(playTypeArr);
		drawGamePurchaseBean.setDrawIdTableMap(drawIdTableMap);
		drawGamePurchaseBean.setNoOfPanel(noOfPanel);

		// keno validation
		boolean isValid = true;
		for (int i = 0; i < noOfPanel; i++) {
			String pickValue = KenoConstants.BET_TYPE_MAP.get(playTypeArr[i]).toString();
			String playerData = pickedNumbersArr[i];
			if (playTypeArr[i].contains("Direct")
					|| "Banker1AgainstAll".equals(playTypeArr[i])) {
				isValid = noPickedArr[i].equals(pickValue);
				logger.debug("-Direct---" + playTypeArr[i] + "---"
						+ noPickedArr[i]);
				if (!isValid) {
					break;
				}
			} else if (playTypeArr[i].contains("Perm")) {
				String defPick[] = pickValue.split(",");
				String selPick = noPickedArr[i];
				logger.debug("-Perm---" + playTypeArr[i] + "---"
						+ noPickedArr[i]);
				if (Integer.parseInt(defPick[0]) > Integer.parseInt(selPick)
						|| Integer.parseInt(defPick[1]) < Integer
								.parseInt(selPick)) {
					isValid = false;
					break;
				}
			} else if ("Banker".equals(playTypeArr[i])) {
				logger.debug("-Banker---" + playTypeArr[i] + "---"
						+ noPickedArr[i]);
				String defPick[] = pickValue.split(",");
				String selPick[] = noPickedArr[i].split(",");
				// for upper line & below line
				if (Integer.parseInt(defPick[0]) > Integer.parseInt(selPick[0])
						|| Integer.parseInt(defPick[1]) < Integer
								.parseInt(selPick[0])
						|| Integer.parseInt(defPick[2]) > Integer
								.parseInt(selPick[1])
						|| Integer.parseInt(defPick[3]) < Integer
								.parseInt(selPick[1])) {
					isValid = false;
					break;
				}
			}
			if (!"QP".equals(playerData)) {
				isValid = Util
						.validateNumber(KenoConstants.START_RANGE,
								KenoConstants.END_RANGE, playerData.replace(
										",UL,", ",").replace(",BL", ""), false);
				logger.debug("-Data---" + playTypeArr[i] + "---"
						+ noPickedArr[i] + "---" + playerData);
				if (!isValid) {
					break;
				}
			}
		}
		
		TransactionManager.setResponseData("true");

		if (!isValid) {
			drawGamePurchaseBean.setSaleStatus("INVALID_INPUT");// Error Draw
			setKenoPurchaseBean(drawGamePurchaseBean);
			logger.error("-----------Keno Validation Error-------------------"
					+ drawGamePurchaseBean.getSaleStatus());
			return SUCCESS;
		}

		DrawGameRPOSHelper helper = new DrawGameRPOSHelper();
		setKenoPurchaseBean(helper.commonPurchseProcess(userBean,
				drawGamePurchaseBean));
		logger.debug(kenoPurchaseBean.getPlayerPicked() + "msg---------"
				+ kenoPurchaseBean.getTicket_no());
		String saleStatus = getKenoPurchaseBean().getSaleStatus();
		if (!"SUCCESS".equalsIgnoreCase(saleStatus)) {
			setErrMsg(DGErrorMsg.buyErrMsg(saleStatus));
			return ERROR;
		}
		return SUCCESS;

	}

	public String reprintTicket() throws Exception {
		logger.debug("Inside reprintTicket");
		HttpSession session = request.getSession();
		UserInfoBean userInfoBean = (UserInfoBean) session
				.getAttribute("USER_INFO");

		DrawGameRPOSHelper helper = new DrawGameRPOSHelper();
		setKenoPurchaseBean((KenoPurchaseBean) helper
				.reprintTicket(userInfoBean));
		logger.debug("keno reprint ok");
		// logger.debug("keno reprint ok");
		return SUCCESS;
	}

	public void setBetAmt(String betAmt) {
		this.betAmt = betAmt;
	}

	public void setDrawIdArr(String[] drawIdArr) {
		this.drawIdArr = drawIdArr;
	}

	public void setErrMsg(String errMsg) {
		this.errMsg = errMsg;
	}

	public void setGameNumber(int gameNumber) {
		this.gameNumber = gameNumber;
	}

	public void setIsAdvancedPlay(int isAdvancedPlay) {
		this.isAdvancedPlay = isAdvancedPlay;
	}

	public void setKenoPurchaseBean(KenoPurchaseBean kenoPurchaseBean) {
		this.kenoPurchaseBean = kenoPurchaseBean;
	}

	public void setNoOfDraws(int noOfDraws) {
		this.noOfDraws = noOfDraws;
	}

	/*
	 * public String getPickedNum() { return pickedNum; }
	 * 
	 * public void setPickedNum(String pickedNum) { this.pickedNum = pickedNum; }
	 */

	public void setNoOfLines(int noOfLines) {
		this.noOfLines = noOfLines;
	}

	public void setNoPicked(String noPicked) {
		this.noPicked = noPicked;
	}

	public void setPickedNumbers(String pickedNumbers) {
		this.pickedNumbers = pickedNumbers;
	}

	public void setPlayType(String playType) {
		this.playType = playType;
	}

	public void setQP(String qp) {
		QP = qp;
	}

	public void setServletRequest(HttpServletRequest request) {
		this.request = request;
	}

	public void setServletResponse(HttpServletResponse response) {
		this.response = response;

	}

	public void setTotalPurchaseAmt(String totalPurchaseAmt) {
		this.totalPurchaseAmt = totalPurchaseAmt;
	}

}
