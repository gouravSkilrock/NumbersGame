package com.skilrock.lms.web.drawGames.common;

public class WebReprintFactory {

	public WebReprintContext fetchReprintGameTypeInstance(String gameBeanType){
		WebReprintContext webReprintContext = null;
		WebBaseReprintController webBaseReprintController = null;
		switch(gameBeanType){
			case "FortunePurchaseBean":
				webBaseReprintController = new WebFortuneReprint();
				break;
			case "LottoPurchaseBean":
				webBaseReprintController = new WebLottoReprint();
				break;
			case "KenoPurchaseBean":
				webBaseReprintController = new WebKenoReprint();
				break;
			case "String":
				webBaseReprintController = new WebStringReprint();
				break;
			default:
				webBaseReprintController = new WebDefaultReprint();
		}
		webReprintContext = new WebReprintContext(webBaseReprintController);
		return webReprintContext;
	}
	
}
