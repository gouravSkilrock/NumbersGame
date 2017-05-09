package com.skilrock.lms.embedded.drawGames.common;

import com.skilrock.lms.dge.beans.EmbeddedReprint;

public class ReprintContext {

	private BaseReprintController baseReprintController;
	
	public ReprintContext(BaseReprintController baseReprintController) {
		this.baseReprintController = baseReprintController;
	}
	
	public String reprintTicket(EmbeddedReprint embeddedReprint) {
		return baseReprintController.prepareFinalResponse(embeddedReprint);
	}
	
}
