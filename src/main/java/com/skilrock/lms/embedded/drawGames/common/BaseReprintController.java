package com.skilrock.lms.embedded.drawGames.common;

import java.util.List;

import com.skilrock.lms.beans.UserInfoBean;
import com.skilrock.lms.dge.beans.EmbeddedReprint;
import com.skilrock.lms.dge.beans.FortunePurchaseBean;
import com.skilrock.lms.dge.beans.FortuneThreePurchaseBean;
import com.skilrock.lms.dge.beans.FortuneTwoPurchaseBean;
import com.skilrock.lms.dge.beans.KenoPurchaseBean;
import com.skilrock.lms.dge.beans.LottoPurchaseBean;
import com.skilrock.lms.dge.beans.RafflePurchaseBean;
import com.skilrock.lms.web.drawGames.common.Util;

public abstract class BaseReprintController {

	public abstract String prepareFinalResponse(EmbeddedReprint embeddedReprint);

	public static String buildPromoReprintData(Object PromoPurchaseBean, UserInfoBean userInfoBean, String countryDeployed)throws Exception {
		if (PromoPurchaseBean instanceof FortunePurchaseBean) {
			FortunePurchaseBean fortunePurchaseBean = (FortunePurchaseBean) PromoPurchaseBean;
			String promoGameName = Util.getGameName(fortunePurchaseBean.getGame_no());
			return "PromoTkt:" + ReprintHepler.reprintFortuneTicket(fortunePurchaseBean, promoGameName) + "QP:" + fortunePurchaseBean.getIsQuickPick()[0] + "|";

		} else if (PromoPurchaseBean instanceof LottoPurchaseBean) {
			LottoPurchaseBean lottoPurchaseBean = (LottoPurchaseBean) PromoPurchaseBean;
			String promoGameName = Util.getGameName(lottoPurchaseBean.getGame_no());
			return "PromoTkt:" + ReprintHepler.reprintLottoTicket(lottoPurchaseBean, promoGameName) + "QP:" + lottoPurchaseBean.getIsQuickPick()[0] + "|";

		} else if (PromoPurchaseBean instanceof KenoPurchaseBean) {
			KenoPurchaseBean kenoPurchaseBean = (KenoPurchaseBean) PromoPurchaseBean;
			String promoGameName = Util.getGameName(kenoPurchaseBean.getGame_no());
			return "PromoTkt:" + ReprintHepler.reprintKenoTicket(kenoPurchaseBean, promoGameName,userInfoBean.getTerminalBuildVersion(), countryDeployed) + "QP:" + kenoPurchaseBean.getIsQuickPick()[0] + "|";

		} else if (PromoPurchaseBean instanceof FortuneTwoPurchaseBean) {
			FortuneTwoPurchaseBean fortuneTwoPurchaseBean = (FortuneTwoPurchaseBean) PromoPurchaseBean;
			String promoGameName = Util.getGameName(fortuneTwoPurchaseBean.getGame_no());
			return "PromoTkt:" + ReprintHepler.reprintFortuneTwoTicket(fortuneTwoPurchaseBean, promoGameName) + "QP:" + fortuneTwoPurchaseBean.getIsQuickPick()[0] + "|";

		} else if (PromoPurchaseBean instanceof FortuneThreePurchaseBean) {
			FortuneThreePurchaseBean fortuneThreePurchaseBean = (FortuneThreePurchaseBean) PromoPurchaseBean;
			String promoGameName = Util.getGameName(fortuneThreePurchaseBean.getGame_no());
			return "PromoTkt:" + ReprintHepler.reprintFortuneThreeTicket(fortuneThreePurchaseBean, promoGameName) + "QP:" + fortuneThreePurchaseBean.getIsQuickPick()[0] + "|";

		} else if (PromoPurchaseBean instanceof List) {
			List<RafflePurchaseBean> rafflePurchaseBeanList = (List<RafflePurchaseBean>) PromoPurchaseBean;
			return CommonMethods.buildRaffleData(rafflePurchaseBeanList);
		}
		return "";
	}

}
