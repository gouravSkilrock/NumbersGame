/**
 * 
 */
package com.skilrock.lms.coreEngine.service.dge;

import java.io.Serializable;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * This abstract class assigns a name for each service Method.
 */
public abstract class ServiceMethodName implements Serializable {
	

	public static final String DRAW_GAME_ANALYSIS_REPORT="fetchAnalysisData";
	
	public static final String FETCH_DRAW_ANALYSIS_DATA="fetchDrawAnalysisDataRetailerWise";

	public static final String FETCH_COMBINITION_DRAW_WISE="fetchDrawWiseCombiData";

	
	public static final String FETCH_DRAW_RESULT_FOR_MAILING_USERS="fetchDrawResultForMailingUser";
	
	public static final String FETCH_TICKETS_TO_CANCEL="fetchTicketsTocancel";
	
	public static final String FETCH_DRAW_GAME_DATA = "fetchGameData";
	
	public static final String FETCH_DRAW_GAME_RESULT_DATA = "getDrawResults";
	
	// CARD 12
	public static final String CARD12_FETCH_DRAW_LIST = "fetchDrawList";

	public static final String CARD12_FETCH_TICKET_WIN = "fetchTicketWinning";

	public static final String CARD12_FETCH_WINNING_RES = "fetchWinningResult";

	public static final String CARD12_PURCHASE_TICKET = "purchaseTicket";

	public static final String CARD12_REPRINT_TICKET = "reprintTicket";
	
	public static final String CARD12_PERFORM_DRAW_DGW = "performDraw";
	
	public static final String CARD12_Prize_Distribution_DGW = "prizeDistribution";

	// CARD 16
	public static final String CARD16_FETCH_DRAW_LIST = "fetchDrawList";

	public static final String CARD16_FETCH_TICKET_WIN = "fetchTicketWinning";

	public static final String CARD16_FETCH_WINNING_RES = "fetchWinningResult";

	public static final String CARD16_PURCHASE_TICKET = "purchaseTicket";

	public static final String CARD16_REPRINT_TICKET = "reprintTicket";
	
	public static final String CARD16_PERFORM_DRAW_DGW = "performDraw";
	
	public static final String CARD16_Prize_Distribution_DGW = "prizeDistribution";

	public static final String DRAW_PANEL_SALE = "fetchDrawPanelSale";

	public static final String DRAWGAME_ACTION_ON_HOLD_DRAW = "actionOnHold";

	public static final String DRAWGAME_CANCEL_DRAW = "cancelDraw";
	
	public static final String DRAWGAME_FREEZE_DRAW = "freezeDraw";

	public static final String DRAWGAME_CANCEL_TICKET = "cancelTicket";

	public static final String DRAWGAME_CHANGE_FREEZETIME = "changeFreezeTime";

	public static final String DRAWGAME_CHANGE_PWT_STATUS = "changePwtTicketStatus";

	public static final String DRAWGAME_CHECK_NEXTDRAW = "checkNextDraw";

	public static final String DRAWGAME_FETCH_DATA = "fetchDrawData";
	
	public static final String DRAWGAME_FETCH_ANALYSIS_REP_DATA = "fetchAnaylysisRepDrawData"; // by neeraj 
			
	public static final String DRAWGAME_FETCH_MACHINE_DATA = "fetchDrawMachineData";

	public static final String DRAWGAME_FETCH_DRAWSCHEDULE = "fetchDrawSchedule";

	// DRAW GAME MANAGEMENT
	public static final String DRAWGAME_FETCH_GAMEDATA = "fetchDrawGameData";

	public static final String DRAWGAME_FETCH_MANUAL_DECLARE_DATA = "fetchManualDeclareData";

	public static final String DRAWGAME_FETCH_MANUAL_ENTRY_DATA = "fetchManualEntryData";
	
	public static final String DRAWGAME_FETCH_MANUAL_MACHINE_ENTRY_DATA = "fetchManualMachineEntryData";

	public static final String DRAWGAME_FETCH_REPRINT_COUNT = "fetchReprintCount";

	public static final String DRAWGAME_FETCH_UPDATED_DATA = "fetchUpdatedData";

	public static final String DRAWGAME_HOLD_DRAW = "holdDraw";

	public static final String DRAWGAME_INITIATE_DRAWSCHEDULE = "initiateDrawSchedule";

	public static final String DRAWGAME_PERFORM_MANUAL_DECLARE_ENTRY = "performManualDeclareEntry";

	public static final String DRAWGAME_PERFORM_MANUAL_WINNING_ENTRY = "performManualWinningEntry";
	
	public static final String DRAWGAME_PERFORM_MANUAL_WINNING_MACHINE_NUMBER_ENTRY = "performManualWinningMachineNumberEntry";

	public static final String DRAWGAME_POSTPONE_DRAW = "postponeDraw";

	public static final String DRAWGAME_PRIZE_DISTRIBUTION = "prizeDistribution";

	public static final String DRAWGAME_PRZE_WINNING_TICKET = "verifyTicket";
	
	//public static final String DRAWGAME_PRZE_WINNING_TICKET = "verifyTicketMandy";

	public static final String DRAWGAME_PWT_UPDATE = "pwtUpdate";
	
	public static final String DRAWGAME_PWT_TP_UPDATE = "pwtTpUpdate";

	public static final String DRAWGAME_RANK_CHK = "rankChkDraw";

	public static final String DRAWGAME_TRACK_TICKET = "ticketWinStatus";
	
	public static final String RAFFLE_TRACK_TICKET = "raffleWinStatus";
	
	public static final String DRAWGAME_PWT_UNCLM = "fetchUnclaimPWTDetails";

	public static final String FASTLOTTO_FETCH_DRAW_LIST = "fetchDrawList";

	public static final String FASTLOTTO_FETCH_TICKET_WIN = "fetchTicketWinning";

	public static final String FASTLOTTO_FETCH_WINNING_RES = "fetchWinningResult";

	// FAST LOTTO
	public static final String FASTLOTTO_PURCHASE_TICKET = "purchaseTicket";

	public static final String FASTLOTTO_REPRINT_TICKET = "reprintTicket";

	public static final String FETCH_DG_DATA_OFFLINE = "fetchDrawGameDataOffline";

	public static final String FETCH_JACKPOT_DETAIL = "fetchDrawGameJackpotDetail";

	public static final String FETCH_SUB_DRAW_RESULT = "fetchSubDrawResult";
	
	public static final String FASTLOTTO_PERFORM_DRAW_DGW = "performDraw";
	
	public static final String FASTLOTTO_Prize_Distribution_DGW = "prizeDistribution";

	public static final String FETCH_WINNING_TICKET_DATA = "fetchWinningTicketsRetailerWise";

	// FORTUNE
	public static final String FORTUNE_FETCH_DRAW_LIST = "fetchDrawList";

	public static final String FORTUNE_FETCH_TICKET_WIN = "fetchTicketWinning";

	public static final String FORTUNE_FETCH_WINNING_RES = "fetchWinningResult";

	public static final String FORTUNE_PURCHASE_TICKET = "fortunePurchaseTicket";

	public static final String FORTUNE_REPRINT_TICKET = "reprintTicket";
	
	public static final String FORTUNE_PERFORM_DRAW_DGW = "performDraw";
	
	public static final String FORTUNE_Prize_Distribution_DGW = "prizeDistribution";

	public static final String INSERT_QUERY_DGE = "insertQueryDGE";

	
	// FORTUNE TWO	
	public static final String FORTUNE_TWO_FETCH_DRAW_LIST = "fetchDrawList";

	public static final String FORTUNE_TWO_FETCH_TICKET_WIN = "fetchTicketWinning";

	public static final String FORTUNE_TWO_FETCH_WINNING_RES = "fetchWinningResult";
	
	public static final String FORTUNE_TWO_PURCHASE_TICKET = "purchaseTicket";

	public static final String FORTUNE_TWO_REPRINT_TICKET = "reprintTicket";
	
	public static final String FORTUNE_TWO_PERFORM_DRAW_DGW = "performDraw";
	
	public static final String FORTUNE_TWO_Prize_Distribution_DGW = "prizeDistribution";

	
    // FORTUNE THREE
	public static final String FORTUNE_THREE_FETCH_DRAW_LIST = "fetchDrawList";

	public static final String FORTUNE_THREE_FETCH_TICKET_WIN = "fetchTicketWinning";

	public static final String FORTUNE_THREE_FETCH_WINNING_RES = "fetchWinningResult";
	
	public static final String FORTUNE_THREE_PURCHASE_TICKET = "purchaseTicket";

	public static final String FORTUNE_THREE_REPRINT_TICKET = "reprintTicket";
	
	public static final String FORTUNE_THREE_PERFORM_DRAW_DGW = "performDraw";
	
	public static final String FORTUNE_THREE_Prize_Distribution_DGW = "prizeDistribution";
	// public static final String DRAWGAME_PRZE_WINNING_TICKET =
	// "verifyMainTicketNew";
	
	//KENO
	public static final String KENO_FETCH_DRAW_LIST = "fetchDrawList";
	
	public static final String KENO_FETCH_TICKET_WIN = "fetchTicketWinning";

	public static final String KENO_FETCH_WINNING_RES = "fetchWinningResult";

	public static final String KENO_PURCHASE_TICKET = "kenoPurchaseTicket";

	public static final String KENO_REPRINT_TICKET = "reprintTicket";
	
	public static final String KENO_PERFORM_DRAW_DGW = "performDraw";
	
	public static final String KENO_Prize_Distribution_DGW = "prizeDistribution";
	
	public static final String KENO_SALE_TXN_STATUS_AND_DATA = "kenoSaleTxnStatusAndData";
	
	//KENOTWO
	public static final String KENOTWO_FETCH_DRAW_LIST = "fetchDrawList";
	
	public static final String KENOTWO_FETCH_TICKET_WIN = "fetchTicketWinning";

	public static final String KENOTWO_FETCH_WINNING_RES = "fetchWinningResult";

	public static final String KENOTWO_PURCHASE_TICKET = "kenoTwoPurchaseTicket";

	public static final String KENOTWO_REPRINT_TICKET = "reprintTicket";
	
	public static final String KENOTWO_PERFORM_DRAW_DGW = "performDraw";
	
	public static final String KENOTWO_Prize_Distribution_DGW = "prizeDistribution";
	
	//KENOFOUR
	public static final String KENOFOUR_FETCH_DRAW_LIST = "fetchDrawList";
	
	public static final String KENOFOUR_FETCH_TICKET_WIN = "fetchTicketWinning";

	public static final String KENOFOUR_FETCH_WINNING_RES = "fetchWinningResult";

	public static final String KENOFOUR_PURCHASE_TICKET = "kenoFourPurchaseTicket";

	public static final String KENOFOUR_REPRINT_TICKET = "reprintTicket";
	
	public static final String KENOFOUR_PERFORM_DRAW_DGW = "performDraw";
	
	public static final String KENOFOUR_Prize_Distribution_DGW = "prizeDistribution";
	
	//KENOFIVE KENO WITH DOUBLE CHANCE
	public static final String KENOFIVE_FETCH_DRAW_LIST = "fetchDrawList";
	
	public static final String KENOFIVE_FETCH_TICKET_WIN = "fetchTicketWinning";

	public static final String KENOFIVE_FETCH_WINNING_RES = "fetchWinningResult";

	public static final String KENOFIVE_PURCHASE_TICKET = "kenoFivePurchaseTicket";

	public static final String KENOFIVE_REPRINT_TICKET = "reprintTicket";
	
	public static final String KENOFIVE_PERFORM_DRAW_DGW = "performDraw";
	
	public static final String KENOFIVE_Prize_Distribution_DGW = "prizeDistribution";

	public static final String KENOEIGHT_PURCHASE_TICKET = "kenoEightPurchaseTicket";

	//FOURDIGIT
	public static final String FOURDIGIT_FETCH_DRAW_LIST = "fetchDrawList";
	
	public static final String FOURDIGIT_FETCH_TICKET_WIN = "fetchTicketWinning";

	public static final String FOURDIGIT_FETCH_WINNING_RES = "fetchWinningResult";

	public static final String FOURDIGIT_PURCHASE_TICKET = "purchaseTicket";

	public static final String FOURDIGIT_REPRINT_TICKET = "reprintTicket";
	
	public static final String FOURDIGIT_PERFORM_DRAW_DGW = "performDraw";
	
	public static final String FOURDIGIT_Prize_Distribution_DGW = "prizeDistribution";
	
	//KENOSIX
	public static final String KENOSIX_FETCH_DRAW_LIST = "fetchDrawList";
	
	public static final String KENOSIX_FETCH_TICKET_WIN = "fetchTicketWinning";

	public static final String KENOSIX_FETCH_WINNING_RES = "fetchWinningResult";
	
	public static final String KENOSIX_PURCHASE_TICKET = "kenoSixPurchaseTicket";

	public static final String KENOSEVEN_PURCHASE_TICKET = "kenoSevenPurchaseTicket";
	
	public static final String RAINBOW_PURCHASE_TICKET = "rainbowGamePurchaseTicket";

	public static final String KENOSIX_REPRINT_TICKET = "reprintTicket";
	
	public static final String KENOSIX_PERFORM_DRAW_DGW = "performDraw";
	
	public static final String KENOSIX_Prize_Distribution_DGW = "prizeDistribution";
	
	//SUPERTWO
	public static final String SUPERTWO_FETCH_DRAW_LIST = "fetchDrawList";
	
	public static final String SUPERTWO_FETCH_TICKET_WIN = "fetchTicketWinning";

	public static final String SUPERTWO_FETCH_WINNING_RES = "fetchWinningResult";

	public static final String SUPERTWO_PURCHASE_TICKET = "purchaseTicket";

	public static final String SUPERTWO_REPRINT_TICKET = "reprintTicket";
	
	public static final String SUPERTWO_PERFORM_DRAW_DGW = "performDraw";
	
	public static final String SUPERTWO_Prize_Distribution_DGW = "prizeDistribution";
	
	static Log logger = LogFactory.getLog(ServiceMethodName.class);

	public static final String LOTTO_FETCH_DRAW_LIST = "fetchDrawList";

	public static final String LOTTO_FETCH_TICKET_WIN = "fetchTicketWinning";

	public static final String LOTTO_FETCH_WINNING_RES = "fetchWinningResult";

	// LOTTO
	public static final String LOTTO_PURCHASE_TICKET = "purchaseTicket";

	public static final String LOTTO_REPRINT_TICKET = "reprintTicket";
	
	public static final String LOTTO_PERFORM_DRAW_DGW = "performDraw";
	
	public static final String LOTTO_Prize_Distribution_DGW = "prizeDistribution";

	public static final String ON_START_GAME_DATA = "onStartGameData";

	public static final String RAFFLE_CANCEL_TICKET = "cancelTicket";

	public static final String RAFFLE_FETCH_DRAW_LIST = "rafflefetchDrawList";

	public static final String RAFFLE_FETCH_TICKET_WIN = "rafflefetchTicketWinning";

	public static final String RAFFLE_FETCH_WINNING_RES = "rafflefetchWinningResult";

	public static final String RAFFLE_PRZE_WINNING_TICKET = "verifyRaffleTicketNew";

	public static final String RAFFLE_PURCHASE_TICKET = "raffleSecondChancePurchaseTicket";

	// Offline

	public static final String RAFFLE_PWT_UPDATE = "rafflePwtUpdate";
	
	public static final String RAFFLE_REPRINT_TICKET = "raffleReprintTicket";
	
	public static final String RAFFLE_PERFORM_DRAW_DGW = "performDraw";
	
	public static final String RAFFLE_Prize_Distribution_DGW = "prizeDistribution";
	
	public static final String REPERFORM_DRAW_FOROFFLINE = "reperformDrawAfterBOFileUploadOffline";

	public static final String SAVE_SUB_DRAW_RESULT = "performManualWinningBO";
	// ZERO TO NINE
	public static final String ZEROTONINE_FETCH_DRAW_LIST = "fetchDrawList";
	public static final String ZEROTONINE_FETCH_TICKET_WIN = "fetchTicketWinning";

	public static final String ZEROTONINE_FETCH_WINNING_RES = "fetchWinningResult";

	public static final String ZEROTONINE_PURCHASE_TICKET = "zeroToNinePurchaseTicket";

	public static final String ZEROTONINE_REPRINT_TICKET = "reprintTicket";
	
	public static final String ZEROTONINE_PERFORM_DRAW_DGW = "performDraw";
	
	public static final String ZEROTONINE_Prize_Distribution_DGW = "prizeDistribution";

	// ZIM LOTTO
	public static final String ZIMLOTTO_FETCH_DRAW_LIST = "fetchDrawList";

	public static final String ZIMLOTTO_FETCH_TICKET_WIN = "fetchTicketWinning";

	public static final String ZIMLOTTO_FETCH_WINNING_RES = "fetchWinningResult";

	public static final String ZIMLOTTO_PURCHASE_TICKET = "purchaseTicket";
	
	public static final String ZIMLOTTO_REPRINT_TICKET = "reprintTicket";
	
	public static final String ZIMLOTTO_PERFORM_DRAW_DGW = "performDraw";
	
	public static final String ZIMLOTTO_Prize_Distribution_DGW = "prizeDistribution";
	
	public static final String FETCH_WINNING_RESULT_DATE_WISE = "fetchWinningResultDateWise";
	
	//TANZANIA LOTTO
	
	
	public static final String TANZANIALOTTO_FETCH_DRAW_LIST = "fetchDrawList";

	public static final String TANZANIALOTTO_FETCH_TICKET_WIN = "fetchTicketWinning";

	public static final String TANZANIALOTTO_FETCH_WINNING_RES = "fetchWinningResult";

	public static final String TANZANIALOTTO_PURCHASE_TICKET = "purchaseTicket";

	public static final String TANZANIALOTTO_REPRINT_TICKET = "reprintTicket";
	
	public static final String TANZANIALOTTO_PERFORM_DRAW_DGW = "performDraw";
	
	public static final String TANZANIALOTTO_Prize_Distribution_DGW = "prizeDistribution";
	
	public static final String TANZANIALOTTO_SALE_TXN_STATUS_AND_DATA = "tanzanialottoSaleTxnStatusAndData";
	
	
	// ZIM LOTTO
	public static final String ZIMLOTTOTWO_FETCH_DRAW_LIST = "fetchDrawList";

	public static final String ZIMLOTTOTWO_FETCH_TICKET_WIN = "fetchTicketWinning";

	public static final String ZIMLOTTOTWO_FETCH_WINNING_RES = "fetchWinningResult";

	public static final String ZIMLOTTOTWO_PURCHASE_TICKET = "purchaseTicket";

	public static final String ZIMLOTTOTWO_REPRINT_TICKET = "reprintTicket";
	
	public static final String ZIMLOTTO_TWO_PERFORM_DRAW_DGW = "performDraw";
	
	public static final String ZIMLOTTO_TWO_Prize_Distribution_DGW = "prizeDistribution";
	
	
	// ZIM LOTTO BONUS
	public static final String ZIMLOTTOBONUS_FETCH_DRAW_LIST = "fetchDrawList";

	public static final String ZIMLOTTOBONUS_FETCH_TICKET_WIN = "fetchTicketWinning";

	public static final String ZIMLOTTOBONUS_FETCH_WINNING_RES = "fetchWinningResult";

	public static final String ZIMLOTTOBONUS_PURCHASE_TICKET = "zimLottoBonusPurchaseTicket";

	public static final String ZIMLOTTOBONUS_REPRINT_TICKET = "reprintTicket";
	
	public static final String ZIMLOTTO_BONUS_PERFORM_DRAW_DGW = "performDraw";
	
	public static final String ZIMLOTTO_BONUS_Prize_Distribution_DGW = "prizeDistribution";
	// ZIM LOTTO BONUS TWO
	public static final String ZIMLOTTOBONUSTWO_FETCH_DRAW_LIST = "fetchDrawList";

	public static final String ZIMLOTTOBONUSTWO_FETCH_TICKET_WIN = "fetchTicketWinning";

	public static final String ZIMLOTTOBONUSTWO_FETCH_WINNING_RES = "fetchWinningResult";

	public static final String ZIMLOTTOBONUSTWO_PURCHASE_TICKET = "zimLottoBonusTwoPurchaseTicket";

	public static final String ZIMLOTTOBONUSTWO_REPRINT_TICKET = "reprintTicket";
	
	public static final String ZIMLOTTOBONUSTWO_PERFORM_DRAW_DGW = "performDraw";
	
	public static final String ZIMLOTTOBONUSTWO_Prize_Distribution_DGW = "prizeDistribution";
	
	// ZIM LOTTO BONUS FREE
	public static final String ZIMLOTTOBONUSFREE_FETCH_DRAW_LIST = "fetchDrawList";

	public static final String ZIMLOTTOBONUSFREE_FETCH_TICKET_WIN = "fetchTicketWinning";

	public static final String ZIMLOTTOBONUSFREE_FETCH_WINNING_RES = "fetchWinningResult";

	public static final String ZIMLOTTOBONUSFREE_PURCHASE_TICKET = "zimLottoBonusFreePurchaseTicket";

	public static final String ZIMLOTTOBONUSFREE_REPRINT_TICKET = "reprintTicket";
	
	public static final String ZIMLOTTO_BONUSFREE_PERFORM_DRAW_DGW = "performDraw";
	
	public static final String ZIMLOTTO_BONUSFREE_Prize_Distribution_DGW = "prizeDistribution";
	
	// ZIM LOTTO BONUS TWO FREE
	public static final String ZIMLOTTOBONUSTWOFREE_FETCH_DRAW_LIST = "fetchDrawList";

	public static final String ZIMLOTTOBONUSTWOFREE_FETCH_TICKET_WIN = "fetchTicketWinning";

	public static final String ZIMLOTTOBONUSTWOFREE_FETCH_WINNING_RES = "fetchWinningResult";

	public static final String ZIMLOTTOBONUSTWOFREE_PURCHASE_TICKET = "zimLottoBonusTwoFreePurchaseTicket";

	public static final String ZIMLOTTOBONUSTWOFREE_REPRINT_TICKET = "reprintTicket";
	
	public static final String ZIMLOTTOBONUSTWOFREE_PERFORM_DRAW_DGW = "performDraw";
	
	public static final String ZIMLOTTOBONUSTWOFREE_Prize_Distribution_DGW = "prizeDistribution";
	
	// ZIM LOTTO THREE
	public static final String ZIMLOTTOTHREE_FETCH_DRAW_LIST = "fetchDrawList";

	public static final String ZIMLOTTOTHREE_FETCH_TICKET_WIN = "fetchTicketWinning";

	public static final String ZIMLOTTOTHREE_FETCH_WINNING_RES = "fetchWinningResult";

	public static final String ZIMLOTTOTHREE_PURCHASE_TICKET = "purchaseTicket";

	public static final String ZIMLOTTOTHREE_REPRINT_TICKET = "reprintTicket";
	
	public static final String ZIMLOTTO_THREE_PERFORM_DRAW_DGW = "performDraw";
	
	public static final String ZIMLOTTO_THREE_Prize_Distribution_DGW = "prizeDistribution";
	
	// BONUS BALL LOTTO
	public static final String BONUSBALLLOTTO_SALE_TXN_STATUS_AND_DATA = "bonusBalllottoSaleTxnStatusAndData";
	
	
	// BONUS BALL TWO
	public static final String BONUSBALLTWO_PURCHASE_TICKET = "purchaseTicket";
	
	
	public static final String FETCH_LIVE_DRAW_DATA="fetchLiveDrawData";
	
	public static final String ON_START_GAME_DATA_DGW = "onStartDGWTest";
	public static final String FETCH_DRAW_MANAGER_DATA_DGW = "fetchDrawManagerDataDGW";
	public static final String FETCH_DRAW_DATA_DGW = "fetchDrawDataDGW";
	public static final String FETCH_WIN_SYMBOL_DGW = "fetchWinSymbolDGW";
	public static final String PREPONE_DRAW_DGW = "preponeDrawDGW";
	public static final String GET_DRAW_PERFORM_TIME = "getDrawPerformTime";
	public static final String SET_GAME_MAP = "setGameMap";
	
	public static final String GET_GAME_DATA = "getGameData";
	public static final String GET_ACTIVE_GAMES_DGW = "getActiveGamesDGW";
	public static final String UPDATE_GEN_TICKET_NO = "updateGenTicketNo";
	public static final String GENERATE_WINNING_FOR_RAFFLE = "generateWinningForRaffle";
	public static final String GET_WINNING_TICKETS = "getWinningTickets";
	public static final String UPDATE_DRAW_ID_MAP = "updateDrawIdMap";
	
	public static final String FETCH_ACTIVE_DRAW_DETAILS = "fetchActiveDrawDetails";//changes
	
	public static final String CANCEL_TXN_STATUS_AND_DATA = "cancelTxnStatusAndData";
	
	//Number Analysis Report
	public static final String FETCH_NUMBER_BET_AMOUNT_DATA = "getNumberData";
	public static final String FETCH_NAME_OF_DRAW_DATA = "fetchDrawName";
	public static final String DRAWGAME_FETCH_CANCEL_DRAW_DATA = "fetchDrawCancelData";
	
	public static final String PING = "ping";
	// Added To Get Draw Game Data For ALL Clients(LMS+PMS)
	public static final String FETCH_DRAW_GAME_CONSOLIDATE_DATA="fetchDrawGameConsolidateData";
	public static final String FETCH_DRAW_WISE_MTN_DATA="fetchDrawGameMTNData";
	public static final String FETCH_MTN_LEDGER_DATA="fetchMTNLedgerData";
	public static final String FETCH_RANK_WISE_WINNING_DATA="fetchRankWiseWinningData";
	public static final String FETCH_DRAW_GAME_CONSOLIDATE_DATA_FOR_GRAPHS="fetchDrawGameConsolidateDataForGarphsAndCharts";
	public static final String GET_DRAW_DATA_RETAILER_WISE="getDrawDataRetailerWise";
	public static final String GET_DETAILED_WINNING_PAYMENT_REPORT="getDetailedWinningPaymentReport";

	public static final String FETCH_LOGIN_DRAW_DATA = "fetchLoginDrawData";

	public static final String FETCH_CANCEL_TICKET_FROM_DATE = "fetchCancelTicketsFromDate";

	public static final String VERIFY_DRAW_GAME_TICKET = "verifyDrawGameTicket";
	public static final String VERIFY_TP_TICKET = "verifyTpTicket";
	public static final String FETCH_DRAW_GAME_CONSOLIDATE_DATA_EXPAND ="getConsolidatedTicketsForDraw";
	public static final String BLOCK_BUNCH_TICKET_IN_DGE ="blockBunchTicket";
	public static final String FETCH_TICKETS_TO_BLOCK_UNBLOCK_IN_DGE ="blockedTicketDetails";
	public static final String BLOCK_UNBLOCK_TICKET_IN_DGE = "bolckUnblockTickets";
	//public static final String PETITION_TICKET_VERIFY = "petitionTicketPwtVerify";
	//public static final String PETITION_TICKET_PAY = "petitionTicketPwtPay";
	public static final String TRACK_FULL_TICKET_DETAILS = "trackTicketDetails";
	public static final String TRACK_TP_TICKET_DETAILS = "trackTpTicketData";
	public static final String WINNING_DATA_REPORT = "winningDataReport";
	public static final String FETCH_BLOCKED_TICKETS="fetchBlockedTickets";
	public static final String FETCH_MERCHANT_SALE_WINNING_DATA="fetchMerchantSaleWinningData";
	public static final String FETCH_MTN_SALE_WINNING_DATA="fetchMtnSaleWinningData";
	
	public static final String TWELVEBYTWENTYFOUR_PURCHASE_TICKET = "twelveByTwentyFourPurchaseTicket";
	public static final String PICKTHREE_PURCHASE_TICKET = "pickThreePurchaseTicket";
	public static final String PICKFOUR_PURCHASE_TICKET = "pickFourPurchaseTicket";
	public static final String ONETOTWELVE_PURCHASE_TICKET = "oneToTwelvePurchaseTicket";
	public static final String ROULETTE_PURCHASE_TICKET = "fullRoulettePurchaseTicket";
	public static final String TENBYTWENTY_PURCHASE_TICKET = "tenByTwentyPurchaseTicket";
	public static final String FETCH_PROMO_DRAW_GAME_CONSOLIDATE_DATA="fetchPromoDrawGameConsolidateData";
	public static final String FETCH_MERCHANT_WISE_TRANSACTION_DATA = "fetchMerchantWiseTxns";
	public static final String FETCH_USSD_SUBSCRIBER_DATA = "fetchUssdSubscriberData";
	
	public static final String FETCH_MERCHANT_WALLET_DATA = "fetchMerchantWalletData";
	public static final String FETCH_MERCHANT_TRANSACTIONS = "fetchMerchantTransactions";
	public static final String PROCESS_RECONCILIATION_DATA = "processReconciliationData";
	
	public static final String FETCH_PENDING_WINNING_TRANSFER_TRANSACTIONS = "fetchPendingWinningTransferData" ;
	public static final String PUSH_PENDING_WINNINGS = "pushPendingWinning" ;
	
	public static final String FETCH_DRAW_GAME_JACKPOT_DETAIL_RAINBOW = "fetchDrawGameJackpotDetailRainbow" ;
	public static final String FETCH_DRAW_GAME_DAILY_BILLING_REPORT_RANBOW = "fetchRainbowDrawGameBillingReport";
	
	public static final String FETCH_GAMEWISE_RANDOM_NUMBER = "getGameWiseRandomNumber";
	
	public static final String KENONINE_PURCHASE_TICKET = "kenoNinePurchaseTicket";
	public static final String TENBYTHIRTY_PURCHASE_TICKET = "tenByThirtyPurchaseTicket";
	
	// BingoSeventyFive
	public static final String BINGOSEVENTYFIVE_PURCHASE_TICKET = "bingoSeventyFivePurchaseTicket";
}