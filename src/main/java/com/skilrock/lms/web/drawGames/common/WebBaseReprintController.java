package com.skilrock.lms.web.drawGames.common;

import com.skilrock.lms.dge.beans.WebReprint;

import net.sf.json.JSONObject;

public abstract class WebBaseReprintController {

	public abstract JSONObject prepareReprintFinalResponse(WebReprint webReprintBean);
	
}
