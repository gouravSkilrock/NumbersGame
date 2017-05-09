package com.skilrock.lms.api.drawGamesImpl;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;

import com.opensymphony.xwork2.ActionSupport;
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

public class TanzaniaLottoAction extends ActionSupport {

	private static final long serialVersionUID = 1L;
	private static int gameNumber = Util.getGameId("Tanzanialotto");
	
	public static TPLottoPurchaseBean purchaseTicketProcess(
			TPLottoPurchaseBean tanzaniaLottoBean,UserInfoBean userBean) {
		
		ServletContext sc =LMSUtility.sc;
		String isDraw = (String) sc.getAttribute("IS_DRAW");
		if (isDraw.equalsIgnoreCase("NO")) {
			tanzaniaLottoBean.setErrorCode("104");//DRAW_GAME_NOT_AVAILABLE
			return tanzaniaLottoBean;
		}
		try{
			if(Long.parseLong(tanzaniaLottoBean.getRefTransId()) <= 0){
				
				tanzaniaLottoBean.setSuccess(false);
				tanzaniaLottoBean.setErrorCode("105");//Third Party Transaction Id not received
				return tanzaniaLottoBean;
			}else {
				if(Util.checkDupRefTransId(tanzaniaLottoBean.getRefTransId(),userBean.getUserOrgId())){
					
					tanzaniaLottoBean.setSuccess(false);
					tanzaniaLottoBean.setErrorCode("105");//Third Party Transaction Id not received
					return tanzaniaLottoBean;
				}
				
			}
			}
			catch (Exception e) {
				tanzaniaLottoBean.setSuccess(false);
				tanzaniaLottoBean.setErrorCode("105");//Third Party Transaction Id not received
				return tanzaniaLottoBean;
			}
		
		
		List<String> playerPicked = new ArrayList<String>();
		String refMerchantId = (String) sc.getAttribute("REF_MERCHANT_ID");
		Map<Integer, Map<Integer, String>> drawIdTableMap = (Map<Integer, Map<Integer, String>>) sc
				.getAttribute("drawIdTableMap");
		
		String purchaseChannel = "LMS_API";
		String[] picknumbers = tanzaniaLottoBean.getPickedNumbers();

		LottoPurchaseBean lottoPurchaseBean = new LottoPurchaseBean();
		// lottoPurchaseBean.setBetAmountMultiple(betAmountMultiples);
		lottoPurchaseBean.setDrawIdTableMap(drawIdTableMap);
		lottoPurchaseBean.setGame_no(gameNumber);
		lottoPurchaseBean.setGameDispName(Util.getGameDisplayName(gameNumber));
		// lottoPurchaseBean.setIsQuickPick(isQuickPick);
		lottoPurchaseBean.setNoOfDraws(Integer.parseInt(tanzaniaLottoBean.getNoOfDraws()));
		lottoPurchaseBean.setPartyId(userBean.getUserOrgId());
		lottoPurchaseBean.setPartyType(userBean.getUserType());
		lottoPurchaseBean.setUserId(userBean.getUserId());
		lottoPurchaseBean.setRefMerchantId(refMerchantId);
		lottoPurchaseBean.setPurchaseChannel(purchaseChannel);
		// if (drawIdArr != null) {
		// lottoPurchaseBean.setDrawDateTime(Arrays.asList(drawIdArr));
		// }
		// lottoPurchaseBean.setPanel_id(panel_id);
		lottoPurchaseBean.setPlayerPicked(playerPicked);
		// lottoPurchaseBean.setTotalPurchaseAmt(Double.parseDouble(totalPurchaseAmt));
		lottoPurchaseBean.setPicknumbers(picknumbers);
		lottoPurchaseBean.setNoPicked(Integer.parseInt(tanzaniaLottoBean.getNoPicked()));
		if(!"0".equals(tanzaniaLottoBean.getLastSoldTicketNo()) && tanzaniaLottoBean.getLastSoldTicketNo()!=null){
			String lastSoldTicketNo = tanzaniaLottoBean.getLastSoldTicketNo().substring(0, tanzaniaLottoBean.getLastSoldTicketNo().length()-2);
			lottoPurchaseBean.setLastSoldTicketNo(lastSoldTicketNo);
		}
		String finalPurchaseData = null;

		if (picknumbers.length < 1) {
			tanzaniaLottoBean.setErrorCode("106");//purchase data error
			return tanzaniaLottoBean;
		}
		for (int i = 0; i < picknumbers.length; i++) {
			if (!"QP".equals(picknumbers[i])) {
				if (!Util.validateNumber(
						ConfigurationVariables.LOTTO_START_RANGE,
						ConfigurationVariables.LOTTO_END_RANGE, picknumbers[i],
						false)) {					
					tanzaniaLottoBean.setErrorCode("106");//purchase data error
					return tanzaniaLottoBean;
				}
			}
		}

		DrawGameRPOSHelper helper = new DrawGameRPOSHelper();
		try {
			helper.tanzaniaLottoPurchaseTicket(userBean,
					lottoPurchaseBean);
		} catch (Exception e) {
			e.printStackTrace();
		}
		String saleStatus = lottoPurchaseBean.getSaleStatus();
		
		if (!"SUCCESS".equalsIgnoreCase(saleStatus)) {
			finalPurchaseData = DGErrorMsg.buyErrorCode(saleStatus);
			tanzaniaLottoBean.setSuccess(false);
			tanzaniaLottoBean.setErrorCode(finalPurchaseData);
			return tanzaniaLottoBean;
		}
		//Storing TP Txn Id in DB
		Util.storeTPTxnId(userBean.getUserOrgId(), lottoPurchaseBean.getRefTransId(), tanzaniaLottoBean.getRefTransId(), 
				tanzaniaLottoBean.getMobileNumber());
		
		tanzaniaLottoBean.setPurchaseTime(lottoPurchaseBean.getPurchaseTime());
		AjaxRequestHelper ajxHelper = new AjaxRequestHelper();
		ajxHelper.getAvlblCreditAmt(userBean);
		double bal = userBean.getAvailableCreditLimit()
				- userBean.getClaimableBal();
		NumberFormat nFormat = NumberFormat.getInstance();
		nFormat.setMinimumFractionDigits(2);
		String balance =  nFormat.format(bal).replace(",", "");
		tanzaniaLottoBean.setBalance(balance);
		tanzaniaLottoBean.setDrawDateTime(lottoPurchaseBean.getDrawDateTime());
		tanzaniaLottoBean.setPickedNumbers(lottoPurchaseBean.getPlayerPicked().toArray(new String[0]));
		tanzaniaLottoBean.setQpStatus(lottoPurchaseBean.getIsQuickPick());
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
			tanzaniaLottoBean.setRaffleData(tpRaffBean);
			tanzaniaLottoBean.setRaffle(true);
		}
		tanzaniaLottoBean.setTopAdMessageList(lottoPurchaseBean.getAdvMsg().get("TOP"));
		tanzaniaLottoBean.setBottomAdMessageList(lottoPurchaseBean.getAdvMsg().get("BOTTOM"));		
		
		tanzaniaLottoBean.setTicketNumber(lottoPurchaseBean.getTicket_no()+lottoPurchaseBean.getReprintCount());	
		tanzaniaLottoBean.setTotalPurchaseAmt(lottoPurchaseBean
				.getTotalPurchaseAmt()+"");
		tanzaniaLottoBean.setTicketCost(lottoPurchaseBean.getTotalPurchaseAmt()+"");
		tanzaniaLottoBean.setLmsTranxId((lottoPurchaseBean.getRefTransId()));		
		tanzaniaLottoBean.setNoOfDraws(lottoPurchaseBean.getNoOfDraws()+"");
		tanzaniaLottoBean.setSuccess(true);
		tanzaniaLottoBean.setErrorCode("100");//Success
		return tanzaniaLottoBean;
	}

	

}
