package com.skilrock.lms.web.drawGames.common;

import com.skilrock.lms.dge.beans.WebReprint;

import net.sf.json.JSONObject;

public class WebReprintContext {

	private WebBaseReprintController webBaseReprintController;
	
	public WebReprintContext(WebBaseReprintController webBaseReprintController){
		this.webBaseReprintController = webBaseReprintController;
	}
	
	public JSONObject reprintTicket(WebReprint webReprintBean){
		return webBaseReprintController.prepareReprintFinalResponse(webReprintBean);
	}
}
