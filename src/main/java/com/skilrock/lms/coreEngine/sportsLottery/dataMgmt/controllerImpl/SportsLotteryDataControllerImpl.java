package com.skilrock.lms.coreEngine.sportsLottery.dataMgmt.controllerImpl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.skilrock.lms.coreEngine.sportsLottery.beans.GameMasterBean;
import com.skilrock.lms.coreEngine.sportsLottery.beans.GameTypeMasterBean;
import com.skilrock.lms.coreEngine.sportsLottery.common.SportLotteryServiceIntegration;
import com.skilrock.lms.coreEngine.sportsLottery.common.SportsLotteryUtils;

public class SportsLotteryDataControllerImpl {
	private static Log logger = LogFactory
			.getLog(SportsLotteryDataControllerImpl.class);

	
	
	public List<GameMasterBean> getSportsLotteryGameData(String merchantName) {

		List<GameMasterBean> gameMasterList = null;
		try {
			gameMasterList = SportLotteryServiceIntegration
					.getSportLotteryGameData(merchantName);
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return gameMasterList;
	}

	public List<GameMasterBean> getSportsLotteryOnStartServerData(String merchantName) {

		List<GameMasterBean> gameMasterList = null;
		GameMasterBean gameMasterBean=null;
		Map<Integer, GameMasterBean> gameMap=null;
		Map<Integer, GameTypeMasterBean> gameTypeMap=null;
		GameTypeMasterBean gameTypeBean=null;
		try {
			gameMasterList = SportLotteryServiceIntegration
					.getSportsLotteryOnStartServerData(merchantName);
			gameMap=new HashMap<Integer, GameMasterBean>();
			gameTypeMap=new HashMap<Integer, GameTypeMasterBean>();
			for(int i=0;i<gameMasterList.size();i++){
				gameMasterBean=gameMasterList.get(i);
				gameMap.put(gameMasterBean.getGameId(), gameMasterBean);
				
				for(int j=0;j<gameMasterBean.getGameTypeMasterList().size();j++){
					gameTypeBean=gameMasterBean.getGameTypeMasterList().get(j);
					gameTypeMap.put(gameTypeBean.getGameTypeId(), gameTypeBean);
				}
				
			}
			
			
			SportsLotteryUtils.gameInfoMap=gameMap;
			SportsLotteryUtils.gameTypeInfoMap=gameTypeMap;
			
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return gameMasterList;
	}

	public static void main(String[] args) {
		logger.info(new SportsLotteryDataControllerImpl()
				.getSportsLotteryOnStartServerData("WGRL"));
	}
}