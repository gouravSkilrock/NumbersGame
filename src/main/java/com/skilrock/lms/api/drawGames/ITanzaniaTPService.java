package com.skilrock.lms.api.drawGames;

import java.util.List;

import com.skilrock.lms.api.beans.CancelBean;
import com.skilrock.lms.api.beans.GameDrawInfoBean;
import com.skilrock.lms.api.beans.PWTApiBean;
import com.skilrock.lms.api.beans.ReprintBean;
import com.skilrock.lms.api.beans.TPGameDetailsBean;
import com.skilrock.lms.api.beans.TPKenoPurchaseBean;
import com.skilrock.lms.api.beans.TPLottoPurchaseBean;
import com.skilrock.lms.api.beans.TPUserBalaanceBean;
import com.skilrock.lms.api.beans.TicketHeaderInfoBean;

public interface ITanzaniaTPService {
	public TPLottoPurchaseBean tanzaniaLottoSale(
			TPLottoPurchaseBean tanzaniaLottoBean);

	public TPLottoPurchaseBean bonusBallLottoSale(
			TPLottoPurchaseBean bonusBallLottoBean);

	public TPKenoPurchaseBean kenoSale(
			TPKenoPurchaseBean kenoPurchaseBean);

	public ReprintBean rePrintTicket(String userName, String password,
			String lmsTranxIdToReprint);

	public CancelBean cancelTicket(String userName, String password,
			String lmsTranxIdToRefund, String refTransId, String mobileNumber);

	public List<GameDrawInfoBean> drawResult(String userName, String password,
			String gameCode, String date);

	public PWTApiBean pwtVerification(String userName, String password,
			String ticketNo);

	public PWTApiBean pwtPayment(String userName, String password, String ticketNo,
			String refTransId,String mobileNumber);
	public TPUserBalaanceBean getBalanceInfo(String userName, String password);
	public TPGameDetailsBean gameInfo(String userName, String password);
	public TicketHeaderInfoBean getTicketHeaderInfo(String userName,String password);
}
