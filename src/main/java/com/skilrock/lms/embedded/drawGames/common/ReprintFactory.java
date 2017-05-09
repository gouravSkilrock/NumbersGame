package com.skilrock.lms.embedded.drawGames.common;

import com.skilrock.lms.common.exception.LMSException;

public class ReprintFactory {

	public ReprintContext fetchReprintGameTypeInstance(String gameBeanType) throws LMSException{
		ReprintContext reprintContext = null;
		BaseReprintController baseReprintController = null;
		switch(gameBeanType){
			case "RafflePurchaseBean":
				baseReprintController = new RaffleReprint();
				break;
			case "FortunePurchaseBean":
				baseReprintController = new FortuneReprint();
				break;
			case "LottoPurchaseBean":
				baseReprintController = new LottoReprint();
				break;
			case "KenoPurchaseBean":
				baseReprintController = new KenoReprint();
				break;
			case "FortuneTwoPurchaseBean":
				baseReprintController = new FortuneTwoReprint();
				break;
			case "FortuneThreePurchaseBean":
				baseReprintController = new FortuneThreeReprint();
				break;
			case "String":
				baseReprintController = new StringReprint();
				break;
			default:
				throw new LMSException(EmbeddedErrors.REPRINT_FAIL_ERROR_CODE,EmbeddedErrors.REPRINT_FAIL);
		}
		reprintContext = new ReprintContext(baseReprintController);
		return reprintContext;
	}
	
}
