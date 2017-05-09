package com.skilrock.lms.api.drawGamesImpl;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;

import org.codehaus.xfire.transport.http.XFireServletController;

import com.skilrock.lms.ajax.AjaxRequestHelper;
import com.skilrock.lms.api.beans.PanelBean;
import com.skilrock.lms.api.beans.RaffleBean;
import com.skilrock.lms.api.beans.TPKenoPurchaseBean;
import com.skilrock.lms.dge.beans.KenoPurchaseBean;
import com.skilrock.lms.beans.RafflePurchaseBean;
import com.skilrock.lms.beans.UserInfoBean;
import com.skilrock.lms.common.utility.LMSUtility;
import com.skilrock.lms.coreEngine.drawGames.common.DGErrorMsg;
import com.skilrock.lms.coreEngine.drawGames.playMgmt.DrawGameRPOSHelper;
import com.skilrock.lms.web.drawGames.common.Util;

public class KenoTwoAction {

	private static int gameNumber = Util.getGameId("KenoTwo");
	
	public static TPKenoPurchaseBean purchaseTicketProcess(
			TPKenoPurchaseBean kenoTwoPurchaseBean, UserInfoBean userBean) {
		System.out.println(XFireServletController.getRequest().getRemoteHost());
		ServletContext sc =LMSUtility.sc;
		
		String isDraw = (String) sc.getAttribute("IS_DRAW");
		if (isDraw.equalsIgnoreCase("NO")) {
			kenoTwoPurchaseBean.setErrorCode("104");//DRAW_GAME_NOT_AVAILABLE
			return kenoTwoPurchaseBean;
		}
		
		try{
			if(Long.parseLong(kenoTwoPurchaseBean.getRefTransId()) <= 0){
				
				kenoTwoPurchaseBean.setSuccess(false);
				kenoTwoPurchaseBean.setErrorCode("105");//Third Party Transaction Id not received
				return kenoTwoPurchaseBean;
			}else {
				if(Util.checkDupRefTransId(kenoTwoPurchaseBean.getRefTransId(),userBean.getUserOrgId())){
					
					kenoTwoPurchaseBean.setSuccess(false);
					kenoTwoPurchaseBean.setErrorCode("105");//Third Party Transaction Id not received
					return kenoTwoPurchaseBean;
				}
				
			}
			}
			catch (Exception e) {
				kenoTwoPurchaseBean.setSuccess(false);
				kenoTwoPurchaseBean.setErrorCode("105");//Third Party Transaction Id not received
				return kenoTwoPurchaseBean;
			}
		
		Map<Integer, Map<Integer, String>> drawIdTableMap = (Map<Integer, Map<Integer, String>>) sc
				.getAttribute("drawIdTableMap");
		List<String> playerPicked = new ArrayList<String>();
		String refMerchantId = (String) sc.getAttribute("REF_MERCHANT_ID");
		String purchaseChannel = "LMS_API";
		PanelBean panelBean = null;
		int noOfPanels = kenoTwoPurchaseBean.getPanelList().size();
		String[] betAmtStr = new String[noOfPanels];
		String[] QPStr = new String[noOfPanels];
		String[] pickedNumbersArr = new String[noOfPanels];
		String[] noPickedArr = new String[noOfPanels];
		String[] playTypeArr = new String[noOfPanels];
		for(int i =0;i<noOfPanels;i++){
			panelBean = kenoTwoPurchaseBean.getPanelList().get(i);
			betAmtStr[i] = panelBean.getBetAmountMultiple()+"";
			QPStr[i] = panelBean.getIsQp()+"";
			pickedNumbersArr[i] = panelBean.getPickedNumbers();
			noPickedArr[i] = panelBean.getNoPicked();
			
			if(!"QP".equalsIgnoreCase(pickedNumbersArr[i]) && pickedNumbersArr[i].split(",").length != Integer.parseInt(noPickedArr[i])){
				kenoTwoPurchaseBean.setSuccess(false);
				kenoTwoPurchaseBean.setErrorCode("116");
				return kenoTwoPurchaseBean;
			}
			
			playTypeArr[i] = panelBean.getPlayType();
		}

		
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
		drawGamePurchaseBean.setNoOfDraws(Integer.parseInt(kenoTwoPurchaseBean.getNoOfDraws()));
		drawGamePurchaseBean.setIsAdvancedPlay(Integer.parseInt(kenoTwoPurchaseBean.getIsAdvancePlay()));
		if (kenoTwoPurchaseBean.getDrawId() != null && kenoTwoPurchaseBean.getDrawId().length > 0 && Integer.parseInt(kenoTwoPurchaseBean.getIsAdvancePlay())==1) {
			drawGamePurchaseBean.setDrawDateTime(Arrays.asList(kenoTwoPurchaseBean.getDrawId()));
			drawGamePurchaseBean.setNoOfDraws(kenoTwoPurchaseBean.getDrawId().length);
		}
		drawGamePurchaseBean.setRefMerchantId(refMerchantId);
		drawGamePurchaseBean.setPurchaseChannel(purchaseChannel);
		drawGamePurchaseBean.setBonus("N");
		drawGamePurchaseBean.setPlayType(playTypeArr);
		drawGamePurchaseBean.setDrawIdTableMap(drawIdTableMap);
		drawGamePurchaseBean.setNoOfPanel(noOfPanel);
		if(!"0".equals(kenoTwoPurchaseBean.getLastSoldTicketNo()) && kenoTwoPurchaseBean.getLastSoldTicketNo()!=null){
			String lastSoldTicketNo = kenoTwoPurchaseBean.getLastSoldTicketNo().substring(0, kenoTwoPurchaseBean.getLastSoldTicketNo().length()-2);
			drawGamePurchaseBean.setLastSoldTicketNo(lastSoldTicketNo);
		}
		DrawGameRPOSHelper helper = new DrawGameRPOSHelper();
		try {
			helper.commonPurchseProcessKenoTwo(userBean,
					drawGamePurchaseBean);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		String finalPurchaseData = null;

		String saleStatus = drawGamePurchaseBean.getSaleStatus();
		if (!"SUCCESS".equalsIgnoreCase(saleStatus)) {
			finalPurchaseData = DGErrorMsg.buyErrorCode(saleStatus);			
			
			kenoTwoPurchaseBean.setSuccess(false);
			kenoTwoPurchaseBean.setErrorCode(finalPurchaseData);
			return kenoTwoPurchaseBean;
		}

		//Storing TP Txn Id in DB
		Util.storeTPTxnId(userBean.getUserOrgId(), drawGamePurchaseBean.getRefTransId(), kenoTwoPurchaseBean.getRefTransId(), 
				kenoTwoPurchaseBean.getMobileNumber());

		AjaxRequestHelper ajxHelper = new AjaxRequestHelper();
		ajxHelper.getAvlblCreditAmt(userBean);
		double bal = userBean.getAvailableCreditLimit()
				- userBean.getClaimableBal();
		NumberFormat nFormat = NumberFormat.getInstance();
		nFormat.setMinimumFractionDigits(2);
		String balance =  nFormat.format(bal).replace(",", "");
		kenoTwoPurchaseBean.setDrawDateTimeList(drawGamePurchaseBean.getDrawDateTime());
		kenoTwoPurchaseBean.setBalance(balance);
		kenoTwoPurchaseBean.setTicketNumber(drawGamePurchaseBean.getTicket_no()+drawGamePurchaseBean.getReprintCount());
		kenoTwoPurchaseBean.setLmsTranxId(drawGamePurchaseBean.getRefTransId());

		int noOfPanelss = drawGamePurchaseBean.getNoOfPanel();
		for (int i = 0; i < noOfPanelss; i++) {
			panelBean = kenoTwoPurchaseBean.getPanelList().get(i);
			panelBean.setBetAmountMultiple(drawGamePurchaseBean.getBetAmountMultiple()[i]+"");
			panelBean.setIsQp(drawGamePurchaseBean.getIsQuickPick()[i]+"");
			panelBean.setNoPicked(drawGamePurchaseBean.getNoPicked()[i]);
			panelBean.setPickedNumbers(drawGamePurchaseBean.getPlayerData()[i]);
			panelBean.setPlayType(drawGamePurchaseBean.getPlayType()[i]);
			panelBean.setNoOfLines(drawGamePurchaseBean.getNoOfLines()[i]+"");
			panelBean.setUnitPrice(drawGamePurchaseBean.getUnitPrice()[i]+"");
			
		}
		
		List<RafflePurchaseBean> raffleList= (List<RafflePurchaseBean>)drawGamePurchaseBean.getPromoPurchaseBean();
		if(raffleList != null && raffleList.size()>0){
			RafflePurchaseBean raffBean = null;
			RaffleBean tpRaffBean = null;			
			raffBean = raffleList.get(0);//hard coded as only 1 raffle applicable at this time //Yogesh Sir
			tpRaffBean = new RaffleBean();
			tpRaffBean.setDrawDateTime(raffBean.getDrawDateTime());
			tpRaffBean.setGameCode(Util.getGameName(raffBean.getGame_no()));
			if("ORIGINAL".equalsIgnoreCase(raffBean.getRaffleTicketType())){
			tpRaffBean.setTicketNumber(raffBean.getRaffleTicket_no()+raffBean.getReprintCount());
			}
			tpRaffBean.setPurchaseTime(raffBean.getPurchaseTime());	
			kenoTwoPurchaseBean.setRaffleData(tpRaffBean);
			kenoTwoPurchaseBean.setRaffle(true);
		}
		kenoTwoPurchaseBean.setTopAdMessageList(drawGamePurchaseBean.getAdvMsg().get("TOP"));
		kenoTwoPurchaseBean.setBottomAdMessageList(drawGamePurchaseBean.getAdvMsg().get("BOTTOM"));
		kenoTwoPurchaseBean.setPurchaseTime(drawGamePurchaseBean.getPurchaseTime());

		

		kenoTwoPurchaseBean.setTotalPurchaseAmt(drawGamePurchaseBean.getTotalPurchaseAmt()+"");
		kenoTwoPurchaseBean.setTicketCost(drawGamePurchaseBean.getTotalPurchaseAmt()+"");
		kenoTwoPurchaseBean.setSuccess(true);
		kenoTwoPurchaseBean.setErrorCode("100");
		return kenoTwoPurchaseBean;

	}

}
