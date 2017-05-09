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

public class KenoAction {

	private static int gameNumber = Util.getGameId("Keno");
	
	public static TPKenoPurchaseBean purchaseTicketProcess(
			TPKenoPurchaseBean kenoPurchaseBean, UserInfoBean userBean) {
		System.out.println(XFireServletController.getRequest().getRemoteHost());
		ServletContext sc =LMSUtility.sc;
		
		String isDraw = (String) sc.getAttribute("IS_DRAW");
		if (isDraw.equalsIgnoreCase("NO")) {
			kenoPurchaseBean.setErrorCode("104");//DRAW_GAME_NOT_AVAILABLE
			return kenoPurchaseBean;
		}
		
		try{
			if(Long.parseLong(kenoPurchaseBean.getRefTransId()) <= 0){
				
				kenoPurchaseBean.setSuccess(false);
				kenoPurchaseBean.setErrorCode("105");//Third Party Transaction Id not received
				return kenoPurchaseBean;
			}else {
				if(Util.checkDupRefTransId(kenoPurchaseBean.getRefTransId(),userBean.getUserOrgId())){
					
					kenoPurchaseBean.setSuccess(false);
					kenoPurchaseBean.setErrorCode("105");//Third Party Transaction Id not received
					return kenoPurchaseBean;
				}
				
			}
			}
			catch (Exception e) {
				kenoPurchaseBean.setSuccess(false);
				kenoPurchaseBean.setErrorCode("105");//Third Party Transaction Id not received
				return kenoPurchaseBean;
			}
		
		Map<Integer, Map<Integer, String>> drawIdTableMap = (Map<Integer, Map<Integer, String>>) sc
				.getAttribute("drawIdTableMap");
		List<String> playerPicked = new ArrayList<String>();
		String refMerchantId = (String) sc.getAttribute("REF_MERCHANT_ID");
		String purchaseChannel = "LMS_API";
		PanelBean panelBean = null;
		int noOfPanels = kenoPurchaseBean.getPanelList().size();
		String[] betAmtStr = new String[noOfPanels];
		String[] QPStr = new String[noOfPanels];
		String[] pickedNumbersArr = new String[noOfPanels];
		String[] noPickedArr = new String[noOfPanels];
		String[] playTypeArr = new String[noOfPanels];
		for(int i =0;i<noOfPanels;i++){
			panelBean = kenoPurchaseBean.getPanelList().get(i);
			betAmtStr[i] = panelBean.getBetAmountMultiple()+"";
			QPStr[i] = panelBean.getIsQp()+"";
			pickedNumbersArr[i] = panelBean.getPickedNumbers();
			noPickedArr[i] = panelBean.getNoPicked();
			
			if(!"QP".equalsIgnoreCase(pickedNumbersArr[i]) && pickedNumbersArr[i].split(",").length != Integer.parseInt(noPickedArr[i])){
				kenoPurchaseBean.setSuccess(false);
				kenoPurchaseBean.setErrorCode("116");
				return kenoPurchaseBean;
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
		drawGamePurchaseBean.setNoOfDraws(Integer.parseInt(kenoPurchaseBean.getNoOfDraws()));
		drawGamePurchaseBean.setIsAdvancedPlay(Integer.parseInt(kenoPurchaseBean.getIsAdvancePlay()));
		if (kenoPurchaseBean.getDrawId() != null && kenoPurchaseBean.getDrawId().length > 0 && Integer.parseInt(kenoPurchaseBean.getIsAdvancePlay())==1) {
			drawGamePurchaseBean.setDrawDateTime(Arrays.asList(kenoPurchaseBean.getDrawId()));
			drawGamePurchaseBean.setNoOfDraws(kenoPurchaseBean.getDrawId().length);
		}
		drawGamePurchaseBean.setRefMerchantId(refMerchantId);
		drawGamePurchaseBean.setPurchaseChannel(purchaseChannel);
		drawGamePurchaseBean.setBonus("N");
		drawGamePurchaseBean.setPlayType(playTypeArr);
		drawGamePurchaseBean.setDrawIdTableMap(drawIdTableMap);
		drawGamePurchaseBean.setNoOfPanel(noOfPanel);
		if(!"0".equals(kenoPurchaseBean.getLastSoldTicketNo()) && kenoPurchaseBean.getLastSoldTicketNo()!=null){
			String lastSoldTicketNo = kenoPurchaseBean.getLastSoldTicketNo().substring(0, kenoPurchaseBean.getLastSoldTicketNo().length()-2);
			drawGamePurchaseBean.setLastSoldTicketNo(lastSoldTicketNo);
		}
		DrawGameRPOSHelper helper = new DrawGameRPOSHelper();
		try {
			helper.commonPurchseProcess(userBean,
					drawGamePurchaseBean);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		String finalPurchaseData = null;

		String saleStatus = drawGamePurchaseBean.getSaleStatus();
		if (!"SUCCESS".equalsIgnoreCase(saleStatus)) {
			finalPurchaseData = DGErrorMsg.buyErrorCode(saleStatus);			
			
			kenoPurchaseBean.setSuccess(false);
			kenoPurchaseBean.setErrorCode(finalPurchaseData);
			return kenoPurchaseBean;
		}

		//Storing TP Txn Id in DB
		Util.storeTPTxnId(userBean.getUserOrgId(), drawGamePurchaseBean.getRefTransId(), kenoPurchaseBean.getRefTransId(), 
				kenoPurchaseBean.getMobileNumber());

		AjaxRequestHelper ajxHelper = new AjaxRequestHelper();
		ajxHelper.getAvlblCreditAmt(userBean);
		double bal = userBean.getAvailableCreditLimit()
				- userBean.getClaimableBal();
		NumberFormat nFormat = NumberFormat.getInstance();
		nFormat.setMinimumFractionDigits(2);
		String balance =  nFormat.format(bal).replace(",", "");
		kenoPurchaseBean.setDrawDateTimeList(drawGamePurchaseBean.getDrawDateTime());
		kenoPurchaseBean.setBalance(balance);
		kenoPurchaseBean.setTicketNumber(drawGamePurchaseBean.getTicket_no()+drawGamePurchaseBean.getReprintCount());
		kenoPurchaseBean.setLmsTranxId(drawGamePurchaseBean.getRefTransId());

		int noOfPanelss = drawGamePurchaseBean.getNoOfPanel();
		for (int i = 0; i < noOfPanelss; i++) {
			panelBean = kenoPurchaseBean.getPanelList().get(i);
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
			kenoPurchaseBean.setRaffleData(tpRaffBean);
			kenoPurchaseBean.setRaffle(true);
		}
		kenoPurchaseBean.setTopAdMessageList(drawGamePurchaseBean.getAdvMsg().get("TOP"));
		kenoPurchaseBean.setBottomAdMessageList(drawGamePurchaseBean.getAdvMsg().get("BOTTOM"));
		kenoPurchaseBean.setPurchaseTime(drawGamePurchaseBean.getPurchaseTime());

		

		kenoPurchaseBean.setTotalPurchaseAmt(drawGamePurchaseBean.getTotalPurchaseAmt()+"");
		kenoPurchaseBean.setTicketCost(drawGamePurchaseBean.getTotalPurchaseAmt()+"");
		kenoPurchaseBean.setSuccess(true);
		kenoPurchaseBean.setErrorCode("100");
		return kenoPurchaseBean;

	}

}
