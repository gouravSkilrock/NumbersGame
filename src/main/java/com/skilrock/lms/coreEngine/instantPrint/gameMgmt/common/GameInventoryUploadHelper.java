package com.skilrock.lms.coreEngine.instantPrint.gameMgmt.common;

import com.skilrock.ipe.instantprint.GameInventoryDetailsBean;
import com.skilrock.ipe.instantprint.IInstantPrint;
import com.skilrock.ipe.instantprint.InstantPrint;




public class GameInventoryUploadHelper {
	public void saveInventory(GameInventoryDetailsBean detailsBean) {
		InstantPrint service = new InstantPrint();
		IInstantPrint portType = service.getInstantPrintServicePort();
		String returnType = null;
		returnType=portType.inventoryUpload(detailsBean);
		System.out.println(returnType);
		NewGameUploadHelper.onStartGame();
	}
}
	