package com.skilrock.lms.api.drawGamesImpl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;

import javax.servlet.ServletContext;

import com.skilrock.lms.ajax.AjaxRequestHelper;
import com.skilrock.lms.api.beans.CancelBean;
import com.skilrock.lms.api.beans.GameDrawInfoBean;
import com.skilrock.lms.api.beans.GameInfoBean;
import com.skilrock.lms.api.beans.PWTApiBean;
import com.skilrock.lms.api.beans.PanelBean;
import com.skilrock.lms.api.beans.RaffleBean;
import com.skilrock.lms.api.beans.ReprintBean;
import com.skilrock.lms.api.beans.TPGameDetailsBean;
import com.skilrock.lms.api.beans.TPKenoPurchaseBean;
import com.skilrock.lms.api.beans.TPLottoPurchaseBean;
import com.skilrock.lms.api.beans.TPUserBalaanceBean;
import com.skilrock.lms.api.beans.TicketHeaderInfoBean;
import com.skilrock.lms.beans.LoginBean;
import com.skilrock.lms.beans.ServiceRequest;
import com.skilrock.lms.beans.ServiceResponse;
import com.skilrock.lms.beans.UserInfoBean;
import com.skilrock.lms.common.db.DBConnect;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.common.utility.CommonMethods;
import com.skilrock.lms.common.utility.LMSUtility;
import com.skilrock.lms.coreEngine.drawGames.drawMgmt.DrawGameMgmtHelper;
import com.skilrock.lms.coreEngine.drawGames.playMgmt.DrawGameRPOSHelper;
import com.skilrock.lms.coreEngine.loginMgmt.common.UserAuthenticationHelper;
import com.skilrock.lms.coreEngine.service.IServiceDelegate;
import com.skilrock.lms.coreEngine.service.ServiceDelegate;
import com.skilrock.lms.coreEngine.service.dge.ServiceMethodName;
import com.skilrock.lms.coreEngine.service.dge.ServiceName;
import com.skilrock.lms.dge.beans.BetDetailsBean;
import com.skilrock.lms.dge.beans.CancelTicketBean;
import com.skilrock.lms.dge.beans.DrawDetailsBean;
import com.skilrock.lms.dge.beans.DrawIdBean;
import com.skilrock.lms.dge.beans.FortunePurchaseBean;
import com.skilrock.lms.dge.beans.FortuneTwoPurchaseBean;
import com.skilrock.lms.dge.beans.KenoPurchaseBean;
import com.skilrock.lms.dge.beans.LottoPurchaseBean;
import com.skilrock.lms.dge.beans.MainPWTDrawBean;
import com.skilrock.lms.dge.beans.PanelIdBean;
import com.skilrock.lms.dge.beans.RaffleDrawIdBean;
import com.skilrock.lms.dge.beans.RafflePurchaseBean;
import com.skilrock.lms.web.drawGames.common.Util;

public class DrawGameAPIHelper {
	@SuppressWarnings("unchecked")
	public static TPGameDetailsBean apiDgData(UserInfoBean userBean)
			throws LMSException {
		Connection con = DBConnect.getConnection();
		TPGameDetailsBean returnBean = null;

		Map<String, List<DrawDetailsBean>> gameMap = null;
		TreeMap frzTimeMap = (TreeMap) LMSUtility.sc.getContext("/DrawGameWeb")
				.getAttribute("FREEZETIME_DATA");
		try {
			String selQry = "select game_id,game_name_dev,game_name,game_nbr,offline_freeze_time,retailer_sale_comm_rate,ifnull(promo_game_type,'STANDARD') game_type,ifnull(raffle_ticket_type,0) raffle_ticket_type from st_dg_game_master left outer join st_dg_promo_scheme ps on game_nbr=promo_game_id and ps.status='ACTIVE' where game_status!='SALE_CLOSE' order by game_nbr";

			PreparedStatement pstmt = con.prepareStatement(selQry);
			ResultSet rs = pstmt.executeQuery();
			returnBean = new TPGameDetailsBean();
			List<GameInfoBean> gameInfoList = new ArrayList<GameInfoBean>();
			GameInfoBean gameBean = null;
			while (rs.next()) {
				gameBean = new GameInfoBean();
				String gameName = rs.getString("game_name_dev");
				int gameNo = rs.getInt("game_nbr");
				gameBean.setGameCode(gameName);
				gameBean.setGameNo(gameNo);
				gameBean.setGameDisplayName(rs.getString("game_name"));
				String gameType = rs.getString("game_type");

				if ("STANDARD".equalsIgnoreCase(gameType)) {
					gameBean.setType(0);
				} else if ("RAFFLE".equalsIgnoreCase(gameType)) {
					gameBean.setType(1);
				} else if ("PROMO".equalsIgnoreCase(gameType)) {
					gameBean.setType(2);
				}

				String raffleType = rs.getString("raffle_ticket_type");
				if ("REFERENCE".equalsIgnoreCase(raffleType)) {
					gameBean.setTypeSpecificInfo(2);
				} else if ("ORIGINAL".equalsIgnoreCase(raffleType)) {
					gameBean.setTypeSpecificInfo(1);
				} else {
					gameBean.setTypeSpecificInfo(0);
				}

				Iterator<BetDetailsBean> priceMapIter = Util.getGameMap().get(gameName)
						.getPriceMap().values().iterator();
				if (priceMapIter.hasNext()) {
					gameBean.setUnit_price(priceMapIter.next().getUnitPrice());
				}

				// offlineFzTimeMap.put(gameNo,
				// rs.getInt("offline_freeze_time"));
				gameBean.setOnline_FTG(Integer.parseInt((String)frzTimeMap.get(gameNo)));
				gameBean.setTicket_validity(Util.getGameMap().get(gameName)
						.getTicketExpiryPeriod());
				double jackPotAmt=Util.getGameMap().get(gameName).getJackpotLimit();
				NumberFormat nf = NumberFormat.getInstance();
				nf.setMinimumFractionDigits(2);
				
				String jkPotAmt = nf.format(jackPotAmt).replaceAll(",", "");
				gameBean.setJackpotAmount(jkPotAmt);
				gameInfoList.add(gameBean);
			}

			/*
			 * retBean.setUserId(userId); retBean.setGameData(gameData);
			 * retBean.setOffline(isOffline);
			 * retBean.setOfflineFzTimeMap(offlineFzTimeMap);
			 * retBean.setRetSaleComm(retSaleComm);
			 */

			IServiceDelegate delegate = ServiceDelegate.getInstance();
			ServiceRequest sReq = new ServiceRequest();
			sReq.setServiceName(ServiceName.DRAWGAME);
			sReq.setServiceMethod(ServiceMethodName.FETCH_ACTIVE_DRAW_DETAILS);
			
			ServiceResponse resp = delegate.getResponse(sReq);
			if (resp.getIsSuccess()) {
				// retBean = (OfflineRetailerBean) resp.getResponseData();
				gameMap = (HashMap<String, List<DrawDetailsBean>>) resp.getResponseData();
			} else {
				throw new LMSException("Some Error");
			}

			List<com.skilrock.lms.api.beans.DrawDetailsBean> drawDetailsListAPI = null;
			TreeMap<Long, String> frzmap = new TreeMap<Long, String>();
			for (int i = 0; i < gameInfoList.size(); i++) {
				gameBean = gameInfoList.get(i);
				List<DrawDetailsBean> drawDetailsList = gameMap.get(gameBean.getGameCode());

				gameBean.setNoOfDraws(drawDetailsList.size());
				DrawDetailsBean drawBeanDGE = null;
				com.skilrock.lms.api.beans.DrawDetailsBean drawBeanAPI = null;
				drawDetailsListAPI = new ArrayList<com.skilrock.lms.api.beans.DrawDetailsBean>();
				TreeSet<Long> timeSet = new TreeSet<Long>();
				for (int j = 0; j < drawDetailsList.size(); j++) {
					drawBeanDGE = drawDetailsList.get(j);
					drawBeanAPI = new com.skilrock.lms.api.beans.DrawDetailsBean();
					drawBeanAPI.setDrawId(drawBeanDGE.getDrawId());
					drawBeanAPI.setDrawDateTime(drawBeanDGE.getDrawDateTime()
							.toString());
					drawBeanAPI.setDrawName(drawBeanDGE.getDrawName());
					drawDetailsListAPI.add(drawBeanAPI);
					drawBeanDGE.getDrawDateTime().getTime();
					timeSet.add(drawBeanDGE.getDrawDateTime().getTime());
				}

				
				frzmap.put(timeSet.first()
						- (Long.parseLong((String)frzTimeMap.get(gameBean.getGameNo()))*1000),
						gameBean.getGameCode());
				gameBean.setDrawBeanList(drawDetailsListAPI);
			}

			/*
			 * if (isOffline) { returnData.append("|PromoInfo:"); selQry =
			 * "select scheme_id,sale_game_id,promo_game_id,sale_ticket_amt,if(valid_for_draw='INDOOR',1,if(valid_for_draw='OUTDOOR',0,2)) valid_for_draw from st_dg_promo_scheme where status='ACTIVE'"
			 * ; pstmt = con.prepareStatement(selQry); rs =
			 * pstmt.executeQuery(); boolean isPromo = false; while (rs.next())
			 * { isPromo = true; returnData.append(rs.getString("scheme_id") +
			 * ","); returnData.append(rs.getString("sale_game_id") + ",");
			 * returnData.append(rs.getString("promo_game_id") + ",");
			 * returnData.append(rs.getString("sale_ticket_amt") + ",");
			 * returnData.append(rs.getString("valid_for_draw") + "#"); } if
			 * (isPromo) { returnData.deleteCharAt(returnData.length() - 1); }
			 * else { returnData.delete(returnData.length() -
			 * "|PromoInfo:".length(), returnData.length()); } }
			 */

			Iterator<Long> frzmapIter = frzmap.keySet().iterator();
			if (frzmapIter.hasNext()) {
				String nextTimeTogGetInfo = new Timestamp(frzmapIter.next())
						.toString();
				returnBean.setNextTimeTogGetInfo(nextTimeTogGetInfo);
			}

			returnBean.setGameBeanList(gameInfoList);

		} catch (Exception e) {
			e.printStackTrace();
			throw new LMSException("Some Error");
		} finally {
			DBConnect.closeCon(con);
		}
		returnBean.setErrorCode("100");
		returnBean.setSuccess(true);
		return returnBean;
	}

	public static UserInfoBean getUserData(String userName) {
		ServletContext sc = LMSUtility.sc;
		UserInfoBean userBean = null;
		Map<String, UserInfoBean> currentUsersMap = (Map<String, UserInfoBean>) sc
				.getAttribute("API_LOGGED_IN_USERS_MAP");

		if (currentUsersMap != null) {
			if (!currentUsersMap.isEmpty()) {
				userBean = currentUsersMap.get(userName);
			}
		} else {

		}
		return userBean;
	}

	public static UserInfoBean createUserData(String userName, String password) {
		ServletContext sc = LMSUtility.sc;
		LoginBean loginBean = new UserAuthenticationHelper()
				.loginAuthentication(userName, password, "TERMINAL", "2000", null, false);
		UserInfoBean userBean = null;
		if("success".equals(loginBean.getStatus()) || "FirstTime".equals(loginBean.getStatus())){
			userBean = loginBean.getUserInfo();
	        Connection connection = DBConnect.getConnection();
	        try {
				PreparedStatement pstmt = connection.prepareStatement("select merchant_ip,user_name,password from st_lms_merchant_auth_master where  user_name = ?");
				pstmt.setString(1, userName);
				ResultSet rs = pstmt.executeQuery();
				String merIp = null;
				String tpPass = null;			
				if(rs.next()){
					merIp = rs.getString("merchant_ip");
					tpPass = rs.getString("password");
				}
				if(userBean.isTPUser()){
					userBean.setTpIp(merIp);
					userBean.setTpTxnPassword(tpPass);
				}
			} catch (SQLException e) {
				
				e.printStackTrace();
			}	
		}			
		
		return userBean;
	}

	public static ReprintBean reprintTicket(UserInfoBean userBean,
			String lmsTranxIdToReprint) {		
		ReprintBean reprintBean = new ReprintBean();			
		
		DrawGameRPOSHelper dgHelper = new DrawGameRPOSHelper();
		Object gameBean = dgHelper.reprintTicket(userBean, false, null, true,
				lmsTranxIdToReprint,null);
		reprintBean = reprintTPTicket(gameBean,userBean);
		return reprintBean;
	}

	private static ReprintBean reprintTPTicket(Object gameBean, UserInfoBean userBean) {
		ReprintBean reprintBean = new ReprintBean();
		LottoPurchaseBean lottobean = null;
		KenoPurchaseBean kenobean = null;
		TPKenoPurchaseBean tpKenoBean = new TPKenoPurchaseBean();
		TPLottoPurchaseBean tpLottoBean = new TPLottoPurchaseBean();
		AjaxRequestHelper ajxHelper = new AjaxRequestHelper();
		ajxHelper.getAvlblCreditAmt(userBean);

		double bal = userBean.getAvailableCreditLimit()
				- userBean.getClaimableBal();
		NumberFormat nFormat = NumberFormat.getInstance();
		nFormat.setMinimumFractionDigits(2);
		String balance =  nFormat.format(bal).replace(",", "");
		
		if (gameBean instanceof FortunePurchaseBean) {
			// to be implemented as this game is available
			reprintBean.setErrorCode("119");//Invalid Game Ticket
			reprintBean.setSuccess(false);
		} else if (gameBean instanceof FortuneTwoPurchaseBean) {
			// to be implemented as this game is available
			reprintBean.setErrorCode("119");//Invalid Game Ticket
			reprintBean.setSuccess(false);
		} else if (gameBean instanceof KenoPurchaseBean) {
			kenobean = (KenoPurchaseBean) gameBean;
			tpKenoBean.setTicketNumber(kenobean.getTicket_no()
					+ kenobean.getReprintCount());
			tpKenoBean.setBalance(balance);
			tpKenoBean.setGameCode(Util.getGameName(kenobean.getGame_no()));
			//tpKenoBean.setLmsTranxId(lmsTranxIdToReprint);
			tpKenoBean.setPurchaseTime(kenobean.getPurchaseTime());
			tpKenoBean.setNoOfDraws(kenobean.getNoOfDraws()+"");
			tpKenoBean.setTicketCost(kenobean.getTotalPurchaseAmt()+"");
			tpKenoBean.setTotalPurchaseAmt(kenobean.getTotalPurchaseAmt()+"");
			tpKenoBean.setDrawDateTimeList(kenobean.getDrawDateTime());
			tpKenoBean.setRaffle(kenobean.isRaffelAssociated());
			PanelBean panelBean = null;
			List<PanelBean> panelList = new ArrayList<PanelBean>();
			int noOfPanels = kenobean.getPlayType().length;
			for (int i = 0; i < noOfPanels; i++) {
				panelBean = new PanelBean();
				panelBean
						.setBetAmountMultiple(kenobean.getBetAmountMultiple()[i]+"");
				panelBean.setIsQp(kenobean.getIsQuickPick()[i]+"");
			//	panelBean.setNoPicked(Integer
			//			.parseInt(kenobean.getNoPicked()[i]));
				panelBean.setPickedNumbers(kenobean.getPlayerData()[i]);
				panelBean.setPlayType(kenobean.getPlayType()[i]);
				panelBean.setNoOfLines(kenobean.getNoOfLines()[i]+"");
				panelBean.setUnitPrice(kenobean.getUnitPrice()[i]+"");
				panelList.add(panelBean);

			}
			tpKenoBean.setPanelList(panelList);
			List<RafflePurchaseBean> raffleList= (List<RafflePurchaseBean>)kenobean.getPromoPurchaseBean();
			if(raffleList != null){
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
				tpKenoBean.setRaffleData(tpRaffBean);
				tpKenoBean.setRaffle(true);
			}
			tpKenoBean.setTopAdMessageList(kenobean.getAdvMsg().get("TOP"));
			tpKenoBean.setBottomAdMessageList(kenobean.getAdvMsg().get("BOTTOM"));
			tpKenoBean.setSuccess(true);
			reprintBean.setKenoBean(tpKenoBean);
			reprintBean.setErrorCode("100");//Success
			reprintBean.setSuccess(true);

		} else if (gameBean instanceof LottoPurchaseBean) {
			lottobean = (LottoPurchaseBean) gameBean;
			tpLottoBean.setTicketNumber(lottobean.getTicket_no()
					+ lottobean.getReprintCount());
			tpLottoBean.setGameCode(Util.getGameName(lottobean.getGame_no()));
			tpLottoBean.setPurchaseTime(lottobean.getPurchaseTime());

			tpLottoBean.setBalance(balance);

			tpLottoBean.setTicketCost(lottobean.getTotalPurchaseAmt()+"");
			tpLottoBean.setDrawDateTime(lottobean.getDrawDateTime());			
			String[] pickedNumbers = new String[lottobean.getPlayerPicked().size()];
			for(int i = 0 ; i<lottobean.getPlayerPicked().size();i++){
				pickedNumbers[i] = lottobean.getPlayerPicked().get(i);
			}
			tpLottoBean.setPickedNumbers(pickedNumbers);
			tpLottoBean.setNoPicked(lottobean.getNoPicked()+"");
			tpLottoBean.setNoOfDraws(lottobean.getNoOfDraws()+"");
			//tpLottoBean.setLmsTranxId(lmsTranxIdToReprint);
			tpLottoBean.setQpStatus(lottobean.getIsQuickPick());
			
			List<LottoPurchaseBean> promoGameList=lottobean.getPromoPurchaseBeanList();
			List<TPLottoPurchaseBean> tpPromoGameList=new ArrayList<TPLottoPurchaseBean>();
			if(promoGameList != null && promoGameList.size()>0){
				for(int i=0;i<promoGameList.size();i++){
				LottoPurchaseBean lottoReprintBean=null;
				TPLottoPurchaseBean tpLottoReprintBean=new TPLottoPurchaseBean();
				lottoReprintBean=promoGameList.get(i);
				tpLottoReprintBean.setTicketNumber(lottoReprintBean.getTicket_no()+lottoReprintBean.getReprintCount());
				tpLottoReprintBean.setPurchaseTime(lottoReprintBean.getPurchaseTime());
				tpLottoReprintBean.setDrawDateTime(lottoReprintBean.getDrawDateTime());
				if(lottoReprintBean.getAdvMsg() !=null){
				tpLottoReprintBean.setBottomAdMessageList(lottoReprintBean.getAdvMsg().get("BOTTOM"));
				tpLottoReprintBean.setTopAdMessageList(lottoReprintBean.getAdvMsg().get("TOP"));
				}
				tpLottoReprintBean.setTotalPurchaseAmt(lottoReprintBean.getTotalPurchaseAmt()+"");
				tpLottoReprintBean.setTicketCost(lottoReprintBean.getTotalPurchaseAmt()+"");
				tpLottoReprintBean.setLmsTranxId(lottoReprintBean.getRefTransId());
				tpLottoReprintBean.setNoOfDraws(lottoReprintBean.getNoOfDraws()+"");
				tpLottoReprintBean.setQpStatus(lottoReprintBean.getIsQuickPick());
				tpLottoReprintBean.setPlayType(lottoReprintBean.getPlayType());
				tpLottoReprintBean.setNoPicked(lottoReprintBean.getNoPicked()+"");
				tpLottoReprintBean.setIsAdvancedPlay(lottoReprintBean.getIsAdvancedPlay()+"");
				String pickNum[] = new String[lottoReprintBean.getPlayerPicked().size()];
				for(int k = 0 ; k <lottoReprintBean.getPlayerPicked().size();k++ ){
					pickNum[k] = lottoReprintBean.getPlayerPicked().get(k);
				}
				tpLottoReprintBean.setPickedNumbers(pickNum);
				
				tpLottoReprintBean.setSuccess(true);
				tpLottoReprintBean.setErrorCode("100");//OK
				tpPromoGameList.add(tpLottoReprintBean);
				}
			}
			
			tpLottoBean.setPromoPurchaseBeanList(tpPromoGameList);
			
			
			tpLottoBean.setRaffle(lottobean.isRaffelAssociated());
			List<RafflePurchaseBean> raffleList= lottobean.getRafflePurchaseBeanList();
			if(raffleList != null){
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
				tpLottoBean.setRaffleData(tpRaffBean);
				tpLottoBean.setRaffle(true);
			}
			tpLottoBean.setTotalPurchaseAmt(lottobean.getTotalPurchaseAmt()+"");
			tpLottoBean.setTopAdMessageList(lottobean.getAdvMsg().get("TOP"));
			tpLottoBean.setBottomAdMessageList(lottobean.getAdvMsg().get("BOTTOM"));
			tpLottoBean.setSuccess(true);
			reprintBean.setLottoBean(tpLottoBean);
			reprintBean.setSuccess(true);
			reprintBean.setErrorCode("100");//Success

		} else if (gameBean instanceof String
				&& "RG_RPERINT".equals(gameBean.toString())) {
			reprintBean.setErrorCode("113");//REPRINT_FRAUD
			reprintBean.setSuccess(false);
		} else if (gameBean instanceof String) {
			reprintBean.setErrorCode("121");//REPRINT_FAIL
			reprintBean.setSuccess(false);
		} else {
			reprintBean.setErrorCode("120");//Reprint Error
			reprintBean.setSuccess(false);
		}

		return reprintBean;
	}

	public static CancelBean cancelTicket(UserInfoBean userBean,
			String lmsTranxIdToRefund, String refTransId, String mobileNumber) {
		CancelBean cancelBean = new CancelBean();	
		
		try{
			if(Long.parseLong(refTransId) <= 0){
				
				cancelBean.setSuccess(false);
				cancelBean.setErrorCode("105");//Third Party Transaction Id not received
				return cancelBean;
			}else {
				if(Util.checkDupRefTransId(refTransId,userBean.getUserOrgId())){
					
					cancelBean.setSuccess(false);
					cancelBean.setErrorCode("105");//Third Party Transaction Id not received
					return cancelBean;
				}
				
			}
			}
			catch (Exception e) {
				cancelBean.setSuccess(false);
				cancelBean.setErrorCode("105");//Third Party Transaction Id not received
				return cancelBean;
			}
						
		cancelBean.setLmsTranxIdToRefund(lmsTranxIdToRefund);
		cancelBean.setRefTransId(refTransId);
		DrawGameRPOSHelper dghelper = new DrawGameRPOSHelper();
		String ticketNumber = dghelper.getTicketNumberFrmTxnId(
				lmsTranxIdToRefund, userBean.getUserOrgId());
		if (ticketNumber == null) {
			cancelBean.setSuccess(false);
			cancelBean.setErrorCode("118");//InValid Transaction Id
			return cancelBean;
		}
		CancelTicketBean cancelTicketBean = new CancelTicketBean();
		String repCount = CommonMethods.fetchReprintCount(Util.getGamenoFromTktnumber(ticketNumber+"00"), ticketNumber);
		cancelTicketBean.setTicketNo(ticketNumber+repCount);
		cancelTicketBean.setPartyId(userBean.getUserOrgId());
		cancelTicketBean.setPartyType(userBean.getUserType());
		cancelTicketBean.setUserId(userBean.getUserId());
		cancelTicketBean.setCancelChannel("LMS_API");
		cancelTicketBean.setRefMerchantId((String)LMSUtility.sc.getAttribute("REF_MERCHANT_ID"));
		Map<Integer, Map<Integer, String>> drawIdTableMap = (Map<Integer, Map<Integer, String>>)LMSUtility.sc
		.getContext("/DrawGameWeb").getAttribute("drawIdTableMap");
		cancelTicketBean.setDrawIdTableMap(drawIdTableMap);

		try {
			cancelTicketBean.setAutoCancel(false);
			cancelTicketBean = dghelper.cancelTicket(cancelTicketBean,
					userBean, false,"manual");// isAutoCancel false
			if (cancelTicketBean.isValid()) {
			Util.storeTPTxnId(userBean.getUserOrgId(), cancelTicketBean.getRefTransId(), refTransId, mobileNumber);
					
				AjaxRequestHelper ajxHelper = new AjaxRequestHelper();
				ajxHelper.getAvlblCreditAmt(userBean);

				double bal = userBean.getAvailableCreditLimit()
						- userBean.getClaimableBal();
				cancelBean.setRefundAmount(cancelTicketBean.getRefundAmount());
				cancelBean.setTicketNumber(cancelTicketBean.getTicketNo());
				cancelBean.setBalance(bal);
				cancelBean.setLmsTransId(cancelTicketBean.getRefTransId());
				cancelBean.setSuccess(true);
				cancelBean.setErrorCode("100");
				return cancelBean;

			} else {
				cancelBean.setSuccess(false);
				cancelBean.setErrorCode("101");//ticket could not be cancelled ...
				return cancelBean;
			}

		} catch (Exception e) {
			e.printStackTrace();
			cancelBean.setSuccess(false);			
			cancelBean.setErrorCode("101");//ticket could not be cancelled ...
			return cancelBean;
		}
	}

	public static PWTApiBean pwtVerification(UserInfoBean userBean, String ticketNo) {
		PWTApiBean pwtBean = new PWTApiBean();
		MainPWTDrawBean mainPwtBean = new MainPWTDrawBean();	
			
		DrawGameRPOSHelper dgHelper = new DrawGameRPOSHelper();
		mainPwtBean.setTicketNo(ticketNo);
		pwtBean.setTicketNo(ticketNo);
		try {
			mainPwtBean = dgHelper.prizeWinningTicket(mainPwtBean, userBean,
					(String)LMSUtility.sc.getAttribute("REF_MERCHANT_ID"));
			
			
			if ("ERROR".equalsIgnoreCase(mainPwtBean.getStatus())) {
				pwtBean.setSuccess(false);
				pwtBean.setErrorCode("122");//Can Not Verify.Invalid PWT
				return pwtBean;				
			} else if ("FRAUD".equalsIgnoreCase(mainPwtBean.getStatus())) {
				pwtBean.setSuccess(false);
				pwtBean.setErrorCode("122");//Can Not Verify.Invalid PWT
				return pwtBean;	
			} else if ("ERROR_INVALID"
					.equalsIgnoreCase(mainPwtBean.getStatus())) {
				pwtBean.setSuccess(false);
				pwtBean.setErrorCode("117");//Invalid Ticket.
				return pwtBean;	
			} else if ("CANCELLED".equalsIgnoreCase(mainPwtBean.getStatus())) {
				pwtBean.setSuccess(false);
				pwtBean.setErrorCode("117");//Invalid Ticket.
				return pwtBean;	
			} else if ("UN_AUTH".equalsIgnoreCase(mainPwtBean.getStatus())) {
				pwtBean.setSuccess(false);
				pwtBean.setErrorCode("123");//Can Not Verify.Invalid Sub-Agent
				return pwtBean;	
			} else if (mainPwtBean.getStatus() != null
					&& mainPwtBean.getStatus().equalsIgnoreCase(
							"OUT_VERIFY_LIMIT")) {
				pwtBean.setSuccess(false);
				pwtBean.setErrorCode("124");//Can Not Verify.High Prize
				return pwtBean;	
			} else if ("HIGH_PRIZE".equalsIgnoreCase(mainPwtBean.getStatus())) {
				pwtBean.setSuccess(false);
				pwtBean.setErrorCode("124");//Can Not Verify.High Prize
				return pwtBean;				
			} else if ("OUT_PAY_LIMIT"
					.equalsIgnoreCase(mainPwtBean.getStatus())) {
				pwtBean.setSuccess(false);
				pwtBean.setErrorCode("124");//Can Not Verify.High Prize
				return pwtBean;	
			
			} else if ("TICKET_EXPIRED".equalsIgnoreCase(mainPwtBean
					.getStatus())) {
				pwtBean.setSuccess(false);
				pwtBean.setErrorCode("125");//Expired or Invalid Ticket
				return pwtBean;	
			
			}
			if (!mainPwtBean.isValid()) {
				pwtBean.setSuccess(false);
				pwtBean.setErrorCode("117");//Invalid Ticket.
				return pwtBean;
			} else {
				com.skilrock.lms.api.beans.DrawDetailsBean drawApiBean = null;
				List<com.skilrock.lms.api.beans.DrawDetailsBean> drawDetailList = new ArrayList<com.skilrock.lms.api.beans.DrawDetailsBean>();
				DrawIdBean drawDgBean = null;
				PanelIdBean panelBean = null;
				PanelBean panelApiBean = null;
				RaffleDrawIdBean raffleDgBean=null;
				RaffleBean raffleApiBean=null;
				//List<PanelBean> panelList = new ArrayList<PanelBean>();
				// this loop need to be changed in case of Raffle Reference
				for(int l=0;l<mainPwtBean.getWinningBeanList().size();l++){
					if(mainPwtBean.getWinningBeanList().get(l)
							.getRaffleDrawIdBeanList() == null){
						
				for (int i = 0; i < mainPwtBean.getWinningBeanList().get(l)
						.getDrawWinList().size(); i++) {
					drawDgBean = mainPwtBean.getWinningBeanList().get(l)
							.getDrawWinList().get(i);
					drawApiBean = new com.skilrock.lms.api.beans.DrawDetailsBean();
					drawApiBean.setDrawDateTime(drawDgBean.getDrawDateTime());
					
					panelApiBean = new PanelBean();
					int nonWin = 0;
					int win = 0;
					int register = 0;
					int claim = 0;
					int outVerify = 0;
					int pndPay = 0;
					boolean isResAwaited = false;
					double drawAmt = 0.0;
					List<PanelIdBean> panelWinList = drawDgBean
							.getPanelWinList();
					if (panelWinList != null) {
						for (int k = 0; k < panelWinList.size(); k++) {
							panelBean = panelWinList.get(k);

							if (panelBean.getStatus().equals("NON WIN")) {
								nonWin++;
							} else if (panelBean.getStatus().equals(
									"NORMAL_PAY")) {
								drawAmt = panelBean.getWinningAmt() + drawAmt;
								win++;
							} else if (panelBean.getStatus().equals("CLAIMED")) {
								claim++;
							} else if (panelBean.getStatus().equals("PND_PAY")) {
								pndPay++;
							} else if (panelBean.getStatus().equals(
									"HIGH_PRIZE")) {
								register++;
							} else if (panelBean.getStatus().equals(
									"OUT_PAY_LIMIT")) {
								register++;
							} else if (panelBean.getStatus().equals(
									"OUT_VERIFY_LIMIT")) {
								outVerify++;
							}
						}

					} else {
						isResAwaited = true;
					}
					if (isResAwaited) {
						panelApiBean.setMessage("Result Awaitted");
						//panelApiBean.setNoOfPanels(panelWinList.size());
					} else {
						if (nonWin != 0) {
							panelApiBean.setMessage("Try Again");
							panelApiBean.setNoOfPanels(nonWin+"");
						}
						if (win != 0) {
							panelApiBean.setMessage("Win Amt " + drawAmt);
							panelApiBean.setNoOfPanels(win+"");
						}
						if (register != 0) {
							panelApiBean.setMessage("Registration Required");
							panelApiBean.setNoOfPanels(register+"");
						}
						if (claim != 0) {
							panelApiBean.setMessage("CLAIMED");
							panelApiBean.setNoOfPanels(claim+"");
						}
						if (pndPay != 0) {
							panelApiBean.setMessage("IN PROCESS");
							panelApiBean.setNoOfPanels(pndPay+"");
						}
						if (outVerify != 0) {
							panelApiBean.setMessage("OUT OF VERIFY");
							panelApiBean.setNoOfPanels(outVerify+"");
						}

					}
					// panelApiBean is to be added to the drawAPiBean and then
					drawApiBean.setPanelBean(panelApiBean);
					drawDetailList.add(drawApiBean);
				}
			}else{
				raffleDgBean = (RaffleDrawIdBean) mainPwtBean.getWinningBeanList().get(l)
				.getRaffleDrawIdBeanList().get(0);
				raffleApiBean = new com.skilrock.lms.api.beans.RaffleBean();
				raffleApiBean.setDrawDateTime(raffleDgBean.getDrawDateTime());
				//raffleApiBean.setTicketNumber(raffleDgBean.getRaffleTicketNumberInDB());
				
				if (raffleDgBean.getStatus().equals("NON_WIN")) {
				raffleApiBean.setMessage("Try Again");
				} else if(raffleDgBean.getStatus().equals("CLAIMED")){
					raffleApiBean.setMessage("CLAIMED");
				} else if(raffleDgBean.getStatus().equals("NORMAL_PAY")){
					raffleApiBean.setMessage("Win Amt " +raffleDgBean.getWinningAmt());
				} else if(raffleDgBean.getStatus().equals("OUT_PAY_LIMIT")){
					raffleApiBean.setMessage("Registration Required");
				} else if(raffleDgBean.getStatus().equals("HIGH_PRIZE")){
					raffleApiBean.setMessage("Registration Required");
				} else if(raffleDgBean.getStatus().equals("OUT_VERIFY_LIMIT")){
					raffleApiBean.setMessage("OUT OF VERIFY");
				} else if(raffleDgBean.getStatus().equals("PND_PAY")){
					raffleApiBean.setMessage("IN PROCESS");
				} else{
					raffleApiBean.setMessage("Result Awaitted");
				}
			}
			} 
				pwtBean.setRaffleBean(raffleApiBean);
				pwtBean.setDrawBeanList(drawDetailList);
			}
			NumberFormat nf = NumberFormat.getInstance();
			nf.setMinimumFractionDigits(2);
			double totWinAmt = mainPwtBean.getTotlticketAmount();
			String totPwtAmt = nf.format(totWinAmt).replaceAll(",", "");
			pwtBean.setTotalWinning(totPwtAmt);
			pwtBean.setGameCode(Util
					.getGameName(mainPwtBean.getMainTktGameNo()));
			pwtBean.setSuccess(true);
			pwtBean.setErrorCode("100");//success
			return pwtBean;

		} catch (SQLException e) {
			e.printStackTrace();
			pwtBean.setSuccess(false);
			pwtBean.setErrorCode("500");//Internal Server error
			return pwtBean;
		}

	}

	public static PWTApiBean pwtPayment(UserInfoBean userBean, String ticketNo,
			String refTransId, String mobileNumber) {// refTransId to be stored in
													// LMS
		PWTApiBean pwtBean = new PWTApiBean();
		MainPWTDrawBean mainPwtBean = new MainPWTDrawBean();
			
		
		mainPwtBean.setTicketNo(ticketNo);
		pwtBean.setTicketNo(ticketNo);
		
		try{
			if(Long.parseLong(refTransId) <= 0){
				
				pwtBean.setSuccess(false);
				pwtBean.setErrorCode("105");//Third Party Transaction Id not received
				return pwtBean;
			}else {
				if(Util.checkDupRefTransId(refTransId,userBean.getUserOrgId())){
					
					pwtBean.setSuccess(false);
					pwtBean.setErrorCode("105");//Third Party Transaction Id not received
					return pwtBean;
				}
				
			}
			}
			catch (Exception e) {
				pwtBean.setSuccess(false);
				pwtBean.setErrorCode("105");//Third Party Transaction Id not received
				return pwtBean;
			}
		
		try {
			DrawGameRPOSHelper dgHelper = new DrawGameRPOSHelper();
			mainPwtBean = dgHelper.prizeWinningTicket(mainPwtBean, userBean,
					(String)LMSUtility.sc.getAttribute("REF_MERCHANT_ID"));
			String highPrizeCriteria = (String) LMSUtility.sc.getAttribute("HIGH_PRIZE_CRITERIA");
			String highPrizeAmt = (String) LMSUtility.sc.getAttribute("HIGH_PRIZE_AMT");
			String highPrizeScheme = (String) LMSUtility.sc.getAttribute(
							"DRAW_GAME_HIGH_PRIZE_SCHEME");
			if (!mainPwtBean.isValid()) {
				pwtBean.setSuccess(false);
				pwtBean.setErrorCode("117");//Invalid Ticket.
				return pwtBean;
			} else {
				mainPwtBean = dgHelper.payPwtTicket(mainPwtBean, userBean,highPrizeCriteria ,highPrizeAmt ,highPrizeScheme);
				
				if("ERROR".equalsIgnoreCase(mainPwtBean.getStatus())){
					pwtBean.setSuccess(false);
					pwtBean.setErrorCode("117");//Invalid Ticket.
					return pwtBean;
				}
				
				if(mainPwtBean.getTransactionIdList() != null && mainPwtBean.getTransactionIdList().size()>0){
					List<Long> txnIdList = mainPwtBean.getTransactionIdList();
					for (int i = 0; i < txnIdList.size(); i++) {
						Util.storeTPTxnId(userBean.getUserOrgId(), txnIdList.get(i)+"", refTransId, mobileNumber);
					}
				pwtBean.setLmsTranxIdList(mainPwtBean.getTransactionIdList());
				}else {				
					pwtBean.setSuccess(false);
					pwtBean.setErrorCode("126");//Pwt Payment could not be processed
					return pwtBean;
				}
				com.skilrock.lms.api.beans.DrawDetailsBean drawApiBean = null;
				List<com.skilrock.lms.api.beans.DrawDetailsBean> drawDetailList = new ArrayList<com.skilrock.lms.api.beans.DrawDetailsBean>();
				DrawIdBean drawDgBean = null;
				PanelIdBean panelBean = null;
				PanelBean panelApiBean = null;
				RaffleDrawIdBean raffleDgBean=null;
				RaffleBean raffleApiBean=null;
				AjaxRequestHelper ajxHelper = new AjaxRequestHelper();
				ajxHelper.getAvlblCreditAmt(userBean);

				double bal = userBean.getAvailableCreditLimit()
						- userBean.getClaimableBal();
				// this loop need to be changed in case of Raffle Reference
				for(int l=0;l<mainPwtBean.getWinningBeanList().size();l++){
					if(mainPwtBean.getWinningBeanList().get(l)
							.getRaffleDrawIdBeanList() == null){
						
				
				for (int i = 0; i < mainPwtBean.getWinningBeanList().get(l)
						.getDrawWinList().size(); i++) {
					drawDgBean = mainPwtBean.getWinningBeanList().get(l)
							.getDrawWinList().get(i);
					drawApiBean = new com.skilrock.lms.api.beans.DrawDetailsBean();
					drawApiBean.setDrawDateTime(drawDgBean.getDrawDateTime());
					// List<PanelBean> panelList = new ArrayList<PanelBean>();
					panelApiBean = new PanelBean();
					int nonWin = 0;
					int win = 0;
					int register = 0;
					int claim = 0;
					int outVerify = 0;
					int pndPay = 0;
					boolean isResAwaited = false;
					double drawAmt = 0.0;
					List<PanelIdBean> panelWinList = drawDgBean
							.getPanelWinList();
					if (panelWinList != null) {
						for (int k = 0; k < panelWinList.size(); k++) {
							panelBean = panelWinList.get(k);

							if (panelBean.getStatus().equals("NON WIN")) {
								nonWin++;
							} else if (panelBean.getStatus().equals(
									"NORMAL_PAY")) {
								drawAmt = panelBean.getWinningAmt() + drawAmt;
								win++;
							} else if (panelBean.getStatus().equals("CLAIMED")) {
								claim++;
							} else if (panelBean.getStatus().equals("PND_PAY")) {
								pndPay++;
							} else if (panelBean.getStatus().equals(
									"HIGH_PRIZE")) {
								register++;
							} else if (panelBean.getStatus().equals(
									"OUT_PAY_LIMIT")) {
								register++;
							} else if (panelBean.getStatus().equals(
									"OUT_VERIFY_LIMIT")) {
								outVerify++;
							}
						}

					} else {
						isResAwaited = true;
					}
					if (isResAwaited) {
						panelApiBean.setMessage("Result Awaitted");
						//panelApiBean.setNoOfPanels(panelWinList.size());
					} else {
						if (nonWin != 0) {
							panelApiBean.setMessage("Try Again");
							panelApiBean.setNoOfPanels(nonWin+"");
						}
						if (win != 0) {
							panelApiBean.setMessage("Win Amt " + drawAmt);
							panelApiBean.setNoOfPanels(win+"");
						}
						if (register != 0) {
							panelApiBean.setMessage("Registration Required");
							panelApiBean.setNoOfPanels(register+"");
						}
						if (claim != 0) {
							panelApiBean.setMessage("CLAIMED");
							panelApiBean.setNoOfPanels(claim+"");
						}
						if (pndPay != 0) {
							panelApiBean.setMessage("IN PROCESS");
							panelApiBean.setNoOfPanels(pndPay+"");
						}
						if (outVerify != 0) {
							panelApiBean.setMessage("OUT OF VERIFY");
							panelApiBean.setNoOfPanels(outVerify+"");
						}

					}
					// panelApiBean is to be added to the drawAPiBean and then
					drawApiBean.setPanelBean(panelApiBean);
					drawDetailList.add(drawApiBean);
				}
					}else {
						raffleDgBean = (RaffleDrawIdBean) mainPwtBean.getWinningBeanList().get(l)
						.getRaffleDrawIdBeanList().get(0);
						raffleApiBean = new com.skilrock.lms.api.beans.RaffleBean();
						raffleApiBean.setDrawDateTime(raffleDgBean.getDrawDateTime());
						//raffleApiBean.setTicketNumber(raffleDgBean.getRaffleTicketNumberInDB());
						if (raffleDgBean.getStatus().equals("NON_WIN")) {
							raffleApiBean.setMessage("Try Again");
							} else if(raffleDgBean.getStatus().equals("CLAIMED")){
								raffleApiBean.setMessage("CLAIMED");
							} else if(raffleDgBean.getStatus().equals("NORMAL_PAY")){
								raffleApiBean.setMessage("Win Amt " +raffleDgBean.getWinningAmt());
							} else if(raffleDgBean.getStatus().equals("OUT_PAY_LIMIT")){
								raffleApiBean.setMessage("Registration Required");
							} else if(raffleDgBean.getStatus().equals("HIGH_PRIZE")){
								raffleApiBean.setMessage("Registration Required");
							} else if(raffleDgBean.getStatus().equals("OUT_VERIFY_LIMIT")){
								raffleApiBean.setMessage("OUT OF VERIFY");
							} else if(raffleDgBean.getStatus().equals("PND_PAY")){
								raffleApiBean.setMessage("IN PROCESS");
							} else{
								raffleApiBean.setMessage("Result Awaitted");
							}
					}
				}
				pwtBean.setRaffleBean(raffleApiBean);
				pwtBean.setDrawBeanList(drawDetailList);
				NumberFormat nf = NumberFormat.getInstance();
				nf.setMinimumFractionDigits(2);
				double totWinAmt = mainPwtBean.getTotlticketAmount();
				String totPwtAmt = nf.format(totWinAmt).replaceAll(",", "");
				pwtBean.setTotalWinning(totPwtAmt);
				pwtBean.setBalance(bal);
				
				
				if(mainPwtBean.isReprint()){
					pwtBean.setReprntReq(mainPwtBean.isReprint());
					Object gameBean = mainPwtBean.getPurchaseBean();
					ReprintBean reprintBean = reprintTPTicket(gameBean, userBean);
					pwtBean.setReprintBean(reprintBean);
				}
				pwtBean.setSuccess(true);
				pwtBean.setErrorCode("100");//success
				return pwtBean;
			}

		} catch (Exception e) {
			e.printStackTrace();
			pwtBean.setSuccess(false);
			pwtBean.setErrorCode("500");//Internal Server error
			return pwtBean;
		}

	}

	public static List<GameDrawInfoBean> drawResult(String gameCode, String date) {
		List<GameDrawInfoBean> returnList = new ArrayList<GameDrawInfoBean>();

		GameDrawInfoBean gameBean = null;

		if (gameCode != null) {
			gameBean = new GameDrawInfoBean();
			gameBean.setGameCode(gameCode);

			ServletContext sc = LMSUtility.sc;
			DrawGameRPOSHelper helper = new DrawGameRPOSHelper();
			TreeMap drawGameData = helper.getDrawGameData();
			sc.setAttribute("GAME_DATA", drawGameData);
			List list = (List) drawGameData.get(Util.getGameMap().get(gameCode)
					.getGameNo());

			List<DrawDetailsBean> drawDetailsList = (List<DrawDetailsBean>) list
					.get(3);

			int drawResultCount = drawDetailsList.size() > 10 ? 10
					: drawDetailsList.size();
			String[] drawResult = null;

			List<com.skilrock.lms.api.beans.DrawDetailsBean> drawResultList = new ArrayList<com.skilrock.lms.api.beans.DrawDetailsBean>();
			for (int i = 0; i < drawResultCount; i++) {
				com.skilrock.lms.api.beans.DrawDetailsBean drawBeanAPI = new com.skilrock.lms.api.beans.DrawDetailsBean();
				DrawDetailsBean drawBean = drawDetailsList.get(i);
				drawBeanAPI.setDrawId(drawBean.getDrawId());
				drawBeanAPI.setDrawDateTime(drawBean.getDrawDateTime()
						.toString());
				drawBeanAPI.setDrawName(drawBean.getDrawName());
				drawBeanAPI.setSymbols(drawBean.getWinningResult());

				drawResultList.add(drawBeanAPI);
			}
			gameBean.setDrawResultList(drawResultList);

			returnList.add(gameBean);

		} else {
			try {
				Map<String, LinkedHashMap<String, DrawDetailsBean>> resultMap = new DrawGameMgmtHelper()
						.fetchWinningResultDateWise(date, date);
				Iterator iter = resultMap.entrySet().iterator();

				while (iter.hasNext()) {
					gameBean = new GameDrawInfoBean();
					Map.Entry<String, LinkedHashMap<String, DrawDetailsBean>> pair = (Map.Entry<String, LinkedHashMap<String, DrawDetailsBean>>) iter
							.next();
					String gameNameDev = pair.getKey();
					gameBean.setGameCode(gameNameDev);
					List<com.skilrock.lms.api.beans.DrawDetailsBean> drawResultList = new ArrayList<com.skilrock.lms.api.beans.DrawDetailsBean>();

					Iterator<DrawDetailsBean> innerIter = pair.getValue()
							.values().iterator();
					while (innerIter.hasNext()) {
						DrawDetailsBean drawBean = innerIter.next();
						int drawId = drawBean.getDrawId();
						String drawName = drawBean.getDrawName();
						Timestamp drawDateTime = drawBean.getDrawDateTime();
						String winningResult = drawBean.getWinningResult();

						com.skilrock.lms.api.beans.DrawDetailsBean drawBeanAPI = new com.skilrock.lms.api.beans.DrawDetailsBean();
						drawBeanAPI.setDrawDateTime(drawDateTime.toString());
						drawBeanAPI.setDrawId(drawId);
						drawBeanAPI.setDrawName(drawName);
						drawBeanAPI.setSymbols(winningResult);

						drawResultList.add(drawBeanAPI);
					}
					gameBean.setDrawResultList(drawResultList);
					
					returnList.add(gameBean);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return returnList;
	}

	public static TPUserBalaanceBean getUserBal(UserInfoBean userBean) {
		TPUserBalaanceBean  userBalBean = new TPUserBalaanceBean();
		AjaxRequestHelper ajxHelper = new AjaxRequestHelper();
		ajxHelper.getAvlblCreditAmt(userBean);
		double bal = userBean.getAvailableCreditLimit()
				- userBean.getClaimableBal();
		System.out.println("inside getUserBal and bal is:"+bal);
		userBalBean.setBalance(bal);
		userBalBean.setUserName(userBean.getUserName());
		userBalBean.setSuccess(true);
		userBalBean.setErrorCode("100");
		return userBalBean;
		
	}
	
	public static TicketHeaderInfoBean getHeaderInfo() {
		TicketHeaderInfoBean  tktInfoBean = new TicketHeaderInfoBean();
		tktInfoBean.setCompanyName((String)LMSUtility.sc.getAttribute("ORG_NAME_JSP"));
		tktInfoBean.setIsLogoDisplayOnTkt("YES");//to be done later ...
		tktInfoBean.setTicketType("SAMPLE");//to be done later ...
		tktInfoBean.setSuccess(true);
		tktInfoBean.setErrorCode("100");
		return tktInfoBean;
	}
	
	public static List<String> getTransactionType(List<Long> lmsTranxIdList){
		List<String> lmsTranxIdTypeList = new DrawGameRPOSHelper().getTransactionType(lmsTranxIdList);
		return lmsTranxIdTypeList;
	}
	
	public static List<Long> getLMSTxnIdList(int retOrgId, String refTxnId){
		List<Long> returnList = DrawGameRPOSHelper.getLMSTxnIdList(retOrgId, refTxnId);
		return returnList;
	}
	
	public static String getMobileNo(int retOrgId, String refTxnId){
		String mobileNo = DrawGameRPOSHelper.getMobileNo(retOrgId, refTxnId);
		return mobileNo;
	}
	
	public static ReprintBean getTransactionStatusAndData(UserInfoBean userBean, String refTxnId, List<Long> lmsTranxIdList, List<String> lmsTranxIdTypeList, String mobileNo){
		ReprintBean reprintBean = new ReprintBean();
		if(lmsTranxIdList != null && lmsTranxIdList.size() > 0){
			if("DG_SALE_KENO".equalsIgnoreCase(lmsTranxIdTypeList.get(0))){
				KenoPurchaseBean kenoBean = (KenoPurchaseBean)DrawGameRPOSHelper.getTransactionStatusAndData(userBean, lmsTranxIdList, lmsTranxIdTypeList);
				TPKenoPurchaseBean tpKenoBean = new TPKenoPurchaseBean();
				
				fillKenoSaleTxnStatusAndData(userBean, kenoBean, tpKenoBean);
				
				tpKenoBean.setLmsTranxId(lmsTranxIdList.get(0) + "");
				tpKenoBean.setMobileNumber(mobileNo);
				tpKenoBean.setRefTransId(refTxnId);
				tpKenoBean.setUserName(userBean.getUserName());
				tpKenoBean.setErrorCode("100");
				
				
				reprintBean.setKenoBean(tpKenoBean);
				reprintBean.setErrorCode("100");//Success
				reprintBean.setSuccess(true);
			} else if("DG_SALE_TANZANIALOTTO".equalsIgnoreCase(lmsTranxIdTypeList.get(0))){
				LottoPurchaseBean lottoBean = (LottoPurchaseBean)DrawGameRPOSHelper.getTransactionStatusAndData(userBean, lmsTranxIdList, lmsTranxIdTypeList);
				TPLottoPurchaseBean tpLottoBean = new TPLottoPurchaseBean();
				
				fillTanzanialottoSaleTxnStatusAndData(userBean, lottoBean, tpLottoBean);
				
				tpLottoBean.setLmsTranxId(lmsTranxIdList.get(0) + "");
				tpLottoBean.setMobileNumber(mobileNo);
				tpLottoBean.setRefTransId(refTxnId);
				tpLottoBean.setUserName(userBean.getUserName());
				tpLottoBean.setErrorCode("100");
				
				
				reprintBean.setLottoBean(tpLottoBean);
				reprintBean.setErrorCode("100");//Success
				reprintBean.setSuccess(true);
			} else if("DG_SALE_BONUSBALLLOTTO".equalsIgnoreCase(lmsTranxIdTypeList.get(0))){
				LottoPurchaseBean lottoBean = (LottoPurchaseBean)DrawGameRPOSHelper.getTransactionStatusAndData(userBean, lmsTranxIdList, lmsTranxIdTypeList);
				TPLottoPurchaseBean tpLottoBean = new TPLottoPurchaseBean();
				
				fillBonusBalllottoSaleTxnStatusAndData(userBean, lottoBean, tpLottoBean);
				
				tpLottoBean.setLmsTranxId(lmsTranxIdList.get(0) + "");
				tpLottoBean.setMobileNumber(mobileNo);
				tpLottoBean.setRefTransId(refTxnId);
				tpLottoBean.setUserName(userBean.getUserName());
				tpLottoBean.setErrorCode("100");
				
				
				reprintBean.setLottoBean(tpLottoBean);
				reprintBean.setErrorCode("100");//Success
				reprintBean.setSuccess(true);
			} else if(lmsTranxIdTypeList.get(0).contains("DG_REFUND_CANCEL")){
				CancelTicketBean cancelBean = (CancelTicketBean)DrawGameRPOSHelper.getTransactionStatusAndData(userBean, lmsTranxIdList, lmsTranxIdTypeList);
				CancelBean tpCancelBean = new CancelBean();
				
				fillCancelTxnStatusAndData(userBean, cancelBean, tpCancelBean);
				
				tpCancelBean.setRefTransId(refTxnId);
				tpCancelBean.setLmsTranxIdToRefund(lmsTranxIdList.get(0) + "");
				tpCancelBean.setLmsTransId(lmsTranxIdList.get(0) + "");
				
				reprintBean.setCancelBean(tpCancelBean);
				reprintBean.setErrorCode("100");//Success
				reprintBean.setSuccess(true);
			} else if(lmsTranxIdTypeList.get(0).contains("PWT")){
				PWTApiBean tpPWTBean = (PWTApiBean) DrawGameRPOSHelper.getTransactionStatusAndData(userBean, lmsTranxIdList, lmsTranxIdTypeList);
				
				tpPWTBean.setLmsTranxIdList(lmsTranxIdList);
				tpPWTBean.setErrorCode("100");
				tpPWTBean.setSuccess(true);
				
				reprintBean.setPwtBean(tpPWTBean);
				reprintBean.setErrorCode("100");//Success
				reprintBean.setSuccess(true);
			}
		}
		return reprintBean;
	}
	
	public static void fillKenoSaleTxnStatusAndData(UserInfoBean userBean, KenoPurchaseBean kenoBean, TPKenoPurchaseBean tpKenoBean){
		AjaxRequestHelper ajxHelper = new AjaxRequestHelper();
		ajxHelper.getAvlblCreditAmt(userBean);

		double bal = userBean.getAvailableCreditLimit() - userBean.getClaimableBal();
		NumberFormat nFormat = NumberFormat.getInstance();
		nFormat.setMinimumFractionDigits(2);
		String balance =  nFormat.format(bal).replace(",", "");
		
		tpKenoBean.setTicketNumber(kenoBean.getTicket_no() + kenoBean.getReprintCount());
		
		tpKenoBean.setBalance(balance);
		tpKenoBean.setGameCode(Util.getGameName(kenoBean.getGame_no()));
		tpKenoBean.setIsAdvancePlay(kenoBean.getIsAdvancedPlay() + "");
		//tpKenoBean.setLmsTranxId(lmsTranxIdToReprint);
		tpKenoBean.setPurchaseTime(kenoBean.getPurchaseTime());
		tpKenoBean.setNoOfDraws(kenoBean.getNoOfDraws()+"");
		tpKenoBean.setTicketCost(kenoBean.getTotalPurchaseAmt()+"");
		tpKenoBean.setTotalPurchaseAmt(kenoBean.getTotalPurchaseAmt()+"");
		tpKenoBean.setDrawDateTimeList(kenoBean.getDrawDateTime());
		tpKenoBean.setRaffle(kenoBean.isRaffelAssociated());
		PanelBean panelBean = null;
		List<PanelBean> panelList = new ArrayList<PanelBean>();
		int noOfPanels = kenoBean.getPlayType().length;
		for (int i = 0; i < noOfPanels; i++) {
			panelBean = new PanelBean();
			panelBean.setBetAmountMultiple(kenoBean.getBetAmountMultiple()[i]+"");
			panelBean.setIsQp(kenoBean.getIsQuickPick()[i]+"");
			panelBean.setNoPicked(kenoBean.getPlayerData()[i].split(",").length + "");
			panelBean.setPickedNumbers(kenoBean.getPlayerData()[i]);
			panelBean.setPlayType(kenoBean.getPlayType()[i]);
			panelBean.setNoOfLines(kenoBean.getNoOfLines()[i]+"");
			panelBean.setUnitPrice(kenoBean.getUnitPrice()[i]+"");
			panelBean.setNoOfPanels(noOfPanels+"");
			panelList.add(panelBean);

		}
		tpKenoBean.setPanelList(panelList);
		List<RafflePurchaseBean> raffleList= kenoBean.getRafflePurchaseBeanList();
		if(raffleList != null){
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
			tpKenoBean.setRaffleData(tpRaffBean);
			tpKenoBean.setRaffle(true);
		}
		tpKenoBean.setTopAdMessageList(kenoBean.getAdvMsg().get("TOP"));
		tpKenoBean.setBottomAdMessageList(kenoBean.getAdvMsg().get("BOTTOM"));
		tpKenoBean.setSuccess(true);
	}
	
	public static void fillTanzanialottoSaleTxnStatusAndData(UserInfoBean userBean, LottoPurchaseBean lottoBean, TPLottoPurchaseBean tpLottoBean){
		AjaxRequestHelper ajxHelper = new AjaxRequestHelper();
		ajxHelper.getAvlblCreditAmt(userBean);

		double bal = userBean.getAvailableCreditLimit() - userBean.getClaimableBal();
		NumberFormat nFormat = NumberFormat.getInstance();
		nFormat.setMinimumFractionDigits(2);
		String balance =  nFormat.format(bal).replace(",", "");
		tpLottoBean.setBalance(balance);
		tpLottoBean.setBottomAdMessageList(lottoBean.getAdvMsg().get("BOTTOM"));
		tpLottoBean.setDrawDateTime(lottoBean.getDrawDateTime());
		tpLottoBean.setGameCode(Util.getGameName(lottoBean.getGame_no()));
		tpLottoBean.setIsAdvancedPlay(lottoBean.getIsAdvancedPlay() + "");
		tpLottoBean.setNoOfDraws(lottoBean.getNoOfDraws()+"");
		//tpLottoBean.setNoPicked(noPicked);
		
		String[] pickedNumbers = new String[lottoBean.getPlayerPicked().size()];
		for(int i = 0 ; i<lottoBean.getPlayerPicked().size();i++){
			pickedNumbers[i] = lottoBean.getPlayerPicked().get(i);
		}
		
		tpLottoBean.setPickedNumbers(pickedNumbers);
		tpLottoBean.setPurchaseTime(lottoBean.getPurchaseTime());
		tpLottoBean.setQpStatus(lottoBean.getIsQuickPick());
		tpLottoBean.setRaffle(lottoBean.isRaffelAssociated());
		tpLottoBean.setTicketCost(lottoBean.getTotalPurchaseAmt()+"");
		tpLottoBean.setTicketNumber(lottoBean.getTicket_no() + lottoBean.getReprintCount());
		tpLottoBean.setTopAdMessageList(lottoBean.getAdvMsg().get("TOP"));
		tpLottoBean.setTotalPurchaseAmt(lottoBean.getTotalPurchaseAmt() + "");
				
		List<RafflePurchaseBean> raffleList= lottoBean.getRafflePurchaseBeanList();
		if(raffleList != null){
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
			tpLottoBean.setRaffleData(tpRaffBean);
			tpLottoBean.setRaffle(true);
		}
		
		tpLottoBean.setSuccess(true);
	}
	
	public static void fillBonusBalllottoSaleTxnStatusAndData(UserInfoBean userBean, LottoPurchaseBean lottoBean, TPLottoPurchaseBean tpLottoBean){
		AjaxRequestHelper ajxHelper = new AjaxRequestHelper();
		ajxHelper.getAvlblCreditAmt(userBean);

		double bal = userBean.getAvailableCreditLimit() - userBean.getClaimableBal();
		NumberFormat nFormat = NumberFormat.getInstance();
		nFormat.setMinimumFractionDigits(2);
		String balance =  nFormat.format(bal).replace(",", "");
		tpLottoBean.setBalance(balance);
		tpLottoBean.setBottomAdMessageList(lottoBean.getAdvMsg().get("BOTTOM"));
		tpLottoBean.setDrawDateTime(lottoBean.getDrawDateTime());
		tpLottoBean.setGameCode(Util.getGameName(lottoBean.getGame_no()));
		tpLottoBean.setIsAdvancedPlay(lottoBean.getIsAdvancedPlay() + "");
		tpLottoBean.setNoOfDraws(lottoBean.getNoOfDraws()+"");
		//tpLottoBean.setNoPicked(noPicked);
		
		String[] pickedNumbers = new String[lottoBean.getPlayerPicked().size()];
		for(int i = 0 ; i<lottoBean.getPlayerPicked().size();i++){
			pickedNumbers[i] = lottoBean.getPlayerPicked().get(i);
		}
		
		tpLottoBean.setPickedNumbers(pickedNumbers);
		tpLottoBean.setPurchaseTime(lottoBean.getPurchaseTime());
		tpLottoBean.setQpStatus(lottoBean.getIsQuickPick());
		tpLottoBean.setRaffle(lottoBean.isRaffelAssociated());
		tpLottoBean.setTicketCost(lottoBean.getTotalPurchaseAmt()+"");
		tpLottoBean.setTicketNumber(lottoBean.getTicket_no() + lottoBean.getReprintCount());
		tpLottoBean.setTopAdMessageList(lottoBean.getAdvMsg().get("TOP"));
		tpLottoBean.setTotalPurchaseAmt(lottoBean.getTotalPurchaseAmt() + "");
				
		List<RafflePurchaseBean> raffleList= lottoBean.getRafflePurchaseBeanList();
		if(raffleList != null){
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
			tpLottoBean.setRaffleData(tpRaffBean);
			tpLottoBean.setRaffle(true);
		}
		
		tpLottoBean.setSuccess(true);
	}
	
	public static void fillCancelTxnStatusAndData(UserInfoBean userBean, CancelTicketBean cancelBean, CancelBean tpCancelBean){
		AjaxRequestHelper ajxHelper = new AjaxRequestHelper();
		ajxHelper.getAvlblCreditAmt(userBean);
		double bal = userBean.getAvailableCreditLimit() - userBean.getClaimableBal();
		tpCancelBean.setBalance(bal);
		tpCancelBean.setTicketNumber(cancelBean.getTicketNo() + cancelBean.getReprintCount());
		tpCancelBean.setRefundAmount(cancelBean.getRefundAmount());
		
		tpCancelBean.setSuccess(true);
	}
}
