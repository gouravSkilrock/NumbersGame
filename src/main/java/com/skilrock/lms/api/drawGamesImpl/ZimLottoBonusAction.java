package com.skilrock.lms.api.drawGamesImpl;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;

import com.skilrock.lms.ajax.AjaxRequestHelper;
import com.skilrock.lms.api.beans.RaffleBean;
import com.skilrock.lms.api.beans.TPLottoPurchaseBean;
import com.skilrock.lms.beans.UserInfoBean;
import com.skilrock.lms.common.ConfigurationVariables;
import com.skilrock.lms.common.utility.LMSUtility;
import com.skilrock.lms.coreEngine.drawGames.common.DGErrorMsg;
import com.skilrock.lms.coreEngine.drawGames.playMgmt.DrawGameRPOSHelper;
import com.skilrock.lms.dge.beans.LottoPurchaseBean;
import com.skilrock.lms.dge.beans.RafflePurchaseBean;
import com.skilrock.lms.web.drawGames.common.Util;

public class ZimLottoBonusAction {
	
	private static int gameNumber = Util.getGameId("ZimLottoBonus");

	public static TPLottoPurchaseBean purchaseTicketProcess(
			TPLottoPurchaseBean zimLottoBonusBean, UserInfoBean userBean) {
		ServletContext sc =LMSUtility.sc;
		String isDraw = (String) sc.getAttribute("IS_DRAW");
		if (isDraw.equalsIgnoreCase("NO")) {
			zimLottoBonusBean.setSuccess(false);
			zimLottoBonusBean.setErrorCode("104");//DRAW_GAME_NOT_AVAILABLE
			return zimLottoBonusBean;
		}
		
		try{
			if(Long.parseLong(zimLottoBonusBean.getRefTransId()) <= 0){
				
				zimLottoBonusBean.setSuccess(false);
				zimLottoBonusBean.setErrorCode("105");//Third Party Transaction Id not received
				return zimLottoBonusBean;
			}else {
				if(Util.checkDupRefTransId(zimLottoBonusBean.getRefTransId(),userBean.getUserOrgId())){
					
					zimLottoBonusBean.setSuccess(false);
					zimLottoBonusBean.setErrorCode("105");//Third Party Transaction Id not received
					return zimLottoBonusBean;
				}
				
			}
			}
			catch (Exception e) {
				zimLottoBonusBean.setSuccess(false);
				zimLottoBonusBean.setErrorCode("105");//Third Party Transaction Id not received
				return zimLottoBonusBean;
			}
		List<String> playerPicked = new ArrayList<String>();
		String refMerchantId = (String) sc.getAttribute("REF_MERCHANT_ID");
		Map<Integer, Map<Integer, String>> drawIdTableMap = (Map<Integer, Map<Integer, String>>) sc
				.getAttribute("drawIdTableMap");
		
		String purchaseChannel = "LMS_API";
		String[] picknumbers = zimLottoBonusBean.getPickedNumbers();

		LottoPurchaseBean lottoPurchaseBean = new LottoPurchaseBean();
		lottoPurchaseBean.setDrawIdTableMap(drawIdTableMap);
		lottoPurchaseBean.setGame_no(gameNumber);
		lottoPurchaseBean.setGameDispName(Util.getGameDisplayName(gameNumber));
		lottoPurchaseBean.setNoOfDraws(Integer.parseInt(zimLottoBonusBean.getNoOfDraws()));
		lottoPurchaseBean.setPartyId(userBean.getUserOrgId());
		lottoPurchaseBean.setPartyType(userBean.getUserType());
		lottoPurchaseBean.setUserId(userBean.getUserId());
		lottoPurchaseBean.setRefMerchantId(refMerchantId);
		lottoPurchaseBean.setPurchaseChannel(purchaseChannel);
		lottoPurchaseBean.setIsAdvancedPlay(Integer.parseInt(zimLottoBonusBean.getIsAdvancedPlay()));
		lottoPurchaseBean.setNoPicked(Integer.parseInt(zimLottoBonusBean.getNoPicked()));
		lottoPurchaseBean.setPlayType(zimLottoBonusBean.getPlayType());
		if(!"0".equals(zimLottoBonusBean.getLastSoldTicketNo()) && zimLottoBonusBean.getLastSoldTicketNo()!=null){
			String lastSoldTicketNo = zimLottoBonusBean.getLastSoldTicketNo().substring(0, zimLottoBonusBean.getLastSoldTicketNo().length()-2);
			lottoPurchaseBean.setLastSoldTicketNo(lastSoldTicketNo);
		}
		
		/*if (drawIdArr != null) {
			lottoPurchaseBean.setDrawDateTime(Arrays.asList(drawIdArr));
		}*/
		// lottoPurchaseBean.setPanel_id(panel_id);
		lottoPurchaseBean.setPlayerPicked(playerPicked);
		// lottoPurchaseBean.setTotalPurchaseAmt(Double.parseDouble(totalPurchaseAmt));

		lottoPurchaseBean.setPicknumbers(picknumbers);
		lottoPurchaseBean.setPickedNumbers(picknumbers[0]);
		String finalPurchaseData = null;

		if (picknumbers.length < 1) {
			lottoPurchaseBean.setSaleStatus("ERROR");
			zimLottoBonusBean.setErrorCode("106");//purchase data error
			zimLottoBonusBean.setSuccess(false);
			return zimLottoBonusBean;
		}
		for (int i = 0; i < picknumbers.length; i++) {
			if (!"QP".equals(picknumbers[i])) {
				if (!Util.validateNumber(
						ConfigurationVariables.ZIMLOTTOBONUS_START_RANGE,
						ConfigurationVariables.ZIMLOTTOBONUS_END_RANGE, picknumbers[i],
						false)) {
					lottoPurchaseBean.setSaleStatus("ERROR");					
					zimLottoBonusBean.setErrorCode("106");//purchase data error
					zimLottoBonusBean.setSuccess(false);
					return zimLottoBonusBean;
				}
			}
		}

		DrawGameRPOSHelper helper = new DrawGameRPOSHelper();
		try {
			helper.zimLottoBonusPurchaseTicket(userBean,
					lottoPurchaseBean);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		String saleStatus = lottoPurchaseBean.getSaleStatus();
		if (!"SUCCESS".equalsIgnoreCase(saleStatus)) {
			finalPurchaseData =  DGErrorMsg.buyErrorCode(saleStatus);
			zimLottoBonusBean.setErrorCode(finalPurchaseData);
			zimLottoBonusBean.setSuccess(false);
			return zimLottoBonusBean;
		}
		//Storing TP Txn Id in DB
		Util.storeTPTxnId(userBean.getUserOrgId(), lottoPurchaseBean.getRefTransId(), zimLottoBonusBean.getRefTransId(), 
				zimLottoBonusBean.getMobileNumber());
		/*if(isIdStored == -1){
			zimLottoBonusBean.setErrorMsg("Duplicate Transaction Id");
			zimLottoBonusBean.setSuccess(false);
			return zimLottoBonusBean;
		}else if(isIdStored == 0){
			zimLottoBonusBean.setErrorMsg("Transaction Id could not be stored");
			zimLottoBonusBean.setSuccess(false);
			return zimLottoBonusBean;
		}*/
		zimLottoBonusBean.setPurchaseTime(lottoPurchaseBean.getPurchaseTime());
		AjaxRequestHelper ajxHelper = new AjaxRequestHelper();
		ajxHelper.getAvlblCreditAmt(userBean);
		double bal = userBean.getAvailableCreditLimit()
				- userBean.getClaimableBal();
		NumberFormat nFormat = NumberFormat.getInstance();
		nFormat.setMinimumFractionDigits(2);
		String balance =  nFormat.format(bal).replace(",", "");
		zimLottoBonusBean.setBalance(balance);
		zimLottoBonusBean.setDrawDateTime(lottoPurchaseBean.getDrawDateTime());
		//zimLottoBonusBean.setPlayerPicked(lottoPurchaseBean.getPlayerPicked());
		//zimLottoBonusBean.setIsQuickPick(lottoPurchaseBean.getIsQuickPick());
		String retPickNum[] = new String[lottoPurchaseBean.getPlayerPicked().size()];
		for(int k = 0 ; k <lottoPurchaseBean.getPlayerPicked().size();k++ ){
			retPickNum[k] = lottoPurchaseBean.getPlayerPicked().get(k);
		}
		zimLottoBonusBean.setPickedNumbers(retPickNum);
		zimLottoBonusBean.setQpStatus(lottoPurchaseBean.getIsQuickPick());
		

		List<LottoPurchaseBean> promoGameList=lottoPurchaseBean.getPromoPurchaseBeanList();
		List<TPLottoPurchaseBean> tpPromoGameList=new ArrayList<TPLottoPurchaseBean>();
		if(promoGameList != null && promoGameList.size()>0){
			for(int i=0;i<promoGameList.size();i++){
			LottoPurchaseBean lottoBean=null;
			TPLottoPurchaseBean tpLottoBean=new TPLottoPurchaseBean();
			lottoBean=promoGameList.get(i);
			tpLottoBean.setTicketNumber(lottoBean.getTicket_no()+lottoBean.getReprintCount());
			tpLottoBean.setPurchaseTime(lottoBean.getPurchaseTime());
			tpLottoBean.setDrawDateTime(lottoBean.getDrawDateTime());
			tpLottoBean.setBottomAdMessageList(lottoBean.getAdvMsg().get("BOTTOM"));
			tpLottoBean.setTopAdMessageList(lottoBean.getAdvMsg().get("TOP"));
			tpLottoBean.setTotalPurchaseAmt(lottoBean.getTotalPurchaseAmt()+"");
			tpLottoBean.setTicketCost(lottoBean.getTotalPurchaseAmt()+"");
			tpLottoBean.setLmsTranxId(lottoBean.getRefTransId());
			tpLottoBean.setNoOfDraws(lottoBean.getNoOfDraws()+"");
			tpLottoBean.setQpStatus(lottoBean.getIsQuickPick());
			tpLottoBean.setPlayType(lottoBean.getPlayType());
			tpLottoBean.setNoPicked(lottoBean.getNoPicked()+"");
			tpLottoBean.setIsAdvancedPlay(lottoBean.getIsAdvancedPlay()+"");
			String pickNum[] = new String[lottoBean.getPlayerPicked().size()];
			for(int k = 0 ; k <lottoBean.getPlayerPicked().size();k++ ){
				pickNum[k] = lottoBean.getPlayerPicked().get(k);
			}
			tpLottoBean.setPickedNumbers(pickNum);
			
			tpLottoBean.setSuccess(true);
			tpLottoBean.setErrorCode("100");//OK
			tpPromoGameList.add(tpLottoBean);
			}
		}
		
		zimLottoBonusBean.setPromoPurchaseBeanList(tpPromoGameList);
		List<RafflePurchaseBean> raffleList= lottoPurchaseBean.getRafflePurchaseBeanList();
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
			zimLottoBonusBean.setRaffleData(tpRaffBean);
			zimLottoBonusBean.setRaffle(true);
		}
					
		zimLottoBonusBean.setTopAdMessageList(lottoPurchaseBean.getAdvMsg().get("TOP"));
		zimLottoBonusBean.setBottomAdMessageList(lottoPurchaseBean.getAdvMsg().get("BOTTOM"));	
		
		zimLottoBonusBean.setTicketNumber(lottoPurchaseBean.getTicket_no()+lottoPurchaseBean.getReprintCount());		
		zimLottoBonusBean.setTotalPurchaseAmt(lottoPurchaseBean
				.getTotalPurchaseAmt()+"");
		zimLottoBonusBean.setTicketCost(lottoPurchaseBean.getTotalPurchaseAmt()+"");
		zimLottoBonusBean.setLmsTranxId(lottoPurchaseBean.getRefTransId());	
		zimLottoBonusBean.setNoOfDraws(lottoPurchaseBean.getNoOfDraws()+"");
		zimLottoBonusBean.setSuccess(true);
		zimLottoBonusBean.setErrorCode("100");//OK
		return zimLottoBonusBean;
	}

	
}
