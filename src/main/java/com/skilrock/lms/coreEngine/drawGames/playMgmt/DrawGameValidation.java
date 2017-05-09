package com.skilrock.lms.coreEngine.drawGames.playMgmt;

import java.util.HashSet;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.skilrock.lms.dge.beans.LottoPurchaseBean;

public class DrawGameValidation {

	static Log logger = LogFactory.getLog(DrawGameValidation.class);
	
	
	// ZimLottobonus Data Validation
	public static boolean zimLottoBonusDataValidation(LottoPurchaseBean lottoPurchaseBean) {
		int noOfDraws = lottoPurchaseBean.getNoOfDraws();
		String playType = lottoPurchaseBean.getPlayType();
		
		if ("Direct6".equals(playType)) {
			String[] picknumbers = lottoPurchaseBean.getPicknumbers();
			Set<Integer> picknumSet;
			String pickNum[];
			int noOfPanels = picknumbers.length;
			logger.debug("no of Panels: " + noOfPanels);
			if (noOfDraws > 0 && noOfPanels > 0) {
				if(noOfPanels > 40){
					return false;
				}
				for (int i = 0; i < noOfPanels; i++) {
					if (picknumbers[i].equalsIgnoreCase("QP") && !lottoPurchaseBean.isQPPreGenerated()) {
						logger.debug("quick pick Selected");

					} else {
						logger.debug("Picked Numbers:" + picknumbers[i]);
						pickNum = picknumbers[i].split(",");
						picknumSet = new HashSet<Integer>();
						for (String element : pickNum) {
							picknumSet.add(Integer.parseInt(element));
						}
						if (pickNum.length != picknumSet.size()
								|| pickNum.length > com.skilrock.lms.dge.gameconstants.ZimLottoBonusConstants.MAX_PLAYER_PICKED) {
							logger.debug("picNum.Length: " + pickNum.length
									+ "Set length:  " + picknumSet.size());
							return false;
						}
					}
				}
				return true;
			}
		} else if ("Perm6".equals(playType)) {
			int noPicked=lottoPurchaseBean.getNoPicked();
			if(noPicked <7 || noPicked >15){
				return false;
			}
			return true;
		}
		return false;
	}
}
