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

public class BonusBallLottoAction {
	
	private static int gameNumber = Util.getGameId("BonusBalllotto");

	public static TPLottoPurchaseBean purchaseTicketProcess(
			TPLottoPurchaseBean bonusBallLottoBean, UserInfoBean userBean) {
		ServletContext sc =LMSUtility.sc;
		String isDraw = (String) sc.getAttribute("IS_DRAW");
		if (isDraw.equalsIgnoreCase("NO")) {
			bonusBallLottoBean.setSuccess(false);
			bonusBallLottoBean.setErrorCode("104");//DRAW_GAME_NOT_AVAILABLE
			return bonusBallLottoBean;
		}
		
		try{
			if(Long.parseLong(bonusBallLottoBean.getRefTransId()) <= 0){
				
				bonusBallLottoBean.setSuccess(false);
				bonusBallLottoBean.setErrorCode("105");//Third Party Transaction Id not received
				return bonusBallLottoBean;
			}else {
				if(Util.checkDupRefTransId(bonusBallLottoBean.getRefTransId(),userBean.getUserOrgId())){
					
					bonusBallLottoBean.setSuccess(false);
					bonusBallLottoBean.setErrorCode("105");//Third Party Transaction Id not received
					return bonusBallLottoBean;
				}
				
			}
			}
			catch (Exception e) {
				bonusBallLottoBean.setSuccess(false);
				bonusBallLottoBean.setErrorCode("105");//Third Party Transaction Id not received
				return bonusBallLottoBean;
			}
		List<String> playerPicked = new ArrayList<String>();
		String refMerchantId = (String) sc.getAttribute("REF_MERCHANT_ID");
		Map<Integer, Map<Integer, String>> drawIdTableMap = (Map<Integer, Map<Integer, String>>) sc
				.getAttribute("drawIdTableMap");
		
		String purchaseChannel = "LMS_API";
		String[] picknumbers = bonusBallLottoBean.getPickedNumbers();

		LottoPurchaseBean lottoPurchaseBean = new LottoPurchaseBean();
		lottoPurchaseBean.setDrawIdTableMap(drawIdTableMap);
		lottoPurchaseBean.setGame_no(gameNumber);
		lottoPurchaseBean.setGameDispName(Util.getGameDisplayName(gameNumber));
		lottoPurchaseBean.setNoOfDraws(Integer.parseInt(bonusBallLottoBean.getNoOfDraws()));
		lottoPurchaseBean.setPartyId(userBean.getUserOrgId());
		lottoPurchaseBean.setPartyType(userBean.getUserType());
		lottoPurchaseBean.setUserId(userBean.getUserId());
		lottoPurchaseBean.setRefMerchantId(refMerchantId);
		lottoPurchaseBean.setPurchaseChannel(purchaseChannel);
		lottoPurchaseBean.setIsAdvancedPlay(Integer.parseInt(bonusBallLottoBean.getIsAdvancedPlay()));
		lottoPurchaseBean.setNoPicked(Integer.parseInt(bonusBallLottoBean.getNoPicked()));
		
		if(!"0".equals(bonusBallLottoBean.getLastSoldTicketNo()) && bonusBallLottoBean.getLastSoldTicketNo()!=null){
			String lastSoldTicketNo = bonusBallLottoBean.getLastSoldTicketNo().substring(0, bonusBallLottoBean.getLastSoldTicketNo().length()-2);
			lottoPurchaseBean.setLastSoldTicketNo(lastSoldTicketNo);
		}
		
		/*if (drawIdArr != null) {
			lottoPurchaseBean.setDrawDateTime(Arrays.asList(drawIdArr));
		}*/
		// lottoPurchaseBean.setPanel_id(panel_id);
		lottoPurchaseBean.setPlayerPicked(playerPicked);
		// lottoPurchaseBean.setTotalPurchaseAmt(Double.parseDouble(totalPurchaseAmt));

		lottoPurchaseBean.setPicknumbers(picknumbers);
		String finalPurchaseData = null;

		if (picknumbers.length < 1) {
			lottoPurchaseBean.setSaleStatus("ERROR");
			bonusBallLottoBean.setErrorCode("106");//purchase data error
			bonusBallLottoBean.setSuccess(false);
			return bonusBallLottoBean;
		}
		for (int i = 0; i < picknumbers.length; i++) {
			if (!"QP".equals(picknumbers[i])) {
				if (!Util.validateNumber(
						ConfigurationVariables.ZIMLOTTOBONUS_START_RANGE,
						ConfigurationVariables.ZIMLOTTOBONUS_END_RANGE, picknumbers[i],
						false)) {
					lottoPurchaseBean.setSaleStatus("ERROR");					
					bonusBallLottoBean.setErrorCode("106");//purchase data error
					bonusBallLottoBean.setSuccess(false);
					return bonusBallLottoBean;
				}
			}
		}

		DrawGameRPOSHelper helper = new DrawGameRPOSHelper();
		try {
			helper.fastLottoPurchaseTicket(userBean,
					lottoPurchaseBean);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		String saleStatus = lottoPurchaseBean.getSaleStatus();
		if (!"SUCCESS".equalsIgnoreCase(saleStatus)) {
			finalPurchaseData =  DGErrorMsg.buyErrorCode(saleStatus);
			bonusBallLottoBean.setErrorCode(finalPurchaseData);
			bonusBallLottoBean.setSuccess(false);
			return bonusBallLottoBean;
		}
		//Storing TP Txn Id in DB
		Util.storeTPTxnId(userBean.getUserOrgId(), lottoPurchaseBean.getRefTransId(), bonusBallLottoBean.getRefTransId(), 
				bonusBallLottoBean.getMobileNumber());
		/*if(isIdStored == -1){
			bonusBallLottoBean.setErrorMsg("Duplicate Transaction Id");
			bonusBallLottoBean.setSuccess(false);
			return bonusBallLottoBean;
		}else if(isIdStored == 0){
			bonusBallLottoBean.setErrorMsg("Transaction Id could not be stored");
			bonusBallLottoBean.setSuccess(false);
			return bonusBallLottoBean;
		}*/
		bonusBallLottoBean.setPurchaseTime(lottoPurchaseBean.getPurchaseTime());
		AjaxRequestHelper ajxHelper = new AjaxRequestHelper();
		ajxHelper.getAvlblCreditAmt(userBean);
		double bal = userBean.getAvailableCreditLimit()
				- userBean.getClaimableBal();
		NumberFormat nFormat = NumberFormat.getInstance();
		nFormat.setMinimumFractionDigits(2);
		String balance =  nFormat.format(bal).replace(",", "");
		bonusBallLottoBean.setBalance(balance);
		bonusBallLottoBean.setDrawDateTime(lottoPurchaseBean.getDrawDateTime());
		//bonusBallLottoBean.setPlayerPicked(lottoPurchaseBean.getPlayerPicked());
		//bonusBallLottoBean.setIsQuickPick(lottoPurchaseBean.getIsQuickPick());
		String retPickNum[] = new String[lottoPurchaseBean.getPlayerPicked().size()];
		for(int k = 0 ; k <lottoPurchaseBean.getPlayerPicked().size();k++ ){
			retPickNum[k] = lottoPurchaseBean.getPlayerPicked().get(k);
		}
		bonusBallLottoBean.setPickedNumbers(retPickNum);
		bonusBallLottoBean.setQpStatus(lottoPurchaseBean.getIsQuickPick());
		

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
			bonusBallLottoBean.setRaffleData(tpRaffBean);
			bonusBallLottoBean.setRaffle(true);
		}
					
		bonusBallLottoBean.setTopAdMessageList(lottoPurchaseBean.getAdvMsg().get("TOP"));
		bonusBallLottoBean.setBottomAdMessageList(lottoPurchaseBean.getAdvMsg().get("BOTTOM"));	
		
		bonusBallLottoBean.setTicketNumber(lottoPurchaseBean.getTicket_no()+lottoPurchaseBean.getReprintCount());		
		bonusBallLottoBean.setTotalPurchaseAmt(lottoPurchaseBean
				.getTotalPurchaseAmt()+"");
		bonusBallLottoBean.setTicketCost(lottoPurchaseBean.getTotalPurchaseAmt()+"");
		bonusBallLottoBean.setLmsTranxId(lottoPurchaseBean.getRefTransId());	
		bonusBallLottoBean.setNoOfDraws(lottoPurchaseBean.getNoOfDraws()+"");
		bonusBallLottoBean.setSuccess(true);
		bonusBallLottoBean.setErrorCode("100");//OK
		return bonusBallLottoBean;
	}

	
}
