package com.skilrock.lms.web.scratchService.reportsMgmt.common;

import java.io.PrintWriter;
import java.util.Map;

import com.skilrock.lms.common.BaseAction;
import com.skilrock.lms.coreEngine.scratchService.inventoryMgmt.common.GameInventoryStatusForBOHelper;

public class WarehouseReportAction extends BaseAction {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private int warehouseId;
	private int gameId;

	public WarehouseReportAction() {
		super(WarehouseReportAction.class);
	}

	public int getWarehouseId() {
		return warehouseId;
	}

	public void setWarehouseId(int warehouseId) {
		this.warehouseId = warehouseId;
	}

	public int getGameId() {
		return gameId;
	}

	public void setGameId(int gameId) {
		this.gameId = gameId;
	}

	@Override
	public String execute() {
		logger.info("inside execute");
		GameInventoryStatusForBOHelper helper = new GameInventoryStatusForBOHelper();
		try {
			Map<String, String> gameMap = helper.getGameMap();
			logger.info("gameMAp ==== " + gameMap);
			request.getSession().setAttribute("gameMap", gameMap);
			request.getSession().setAttribute("wareHouseMap", helper.fetchWareHouseMap());
		} catch (Exception e) {
			// TODO: handle exception
		}
		return SUCCESS;
	}

	public void fetchWarehouseWiseInventory() {
		String responseStr = null;
		PrintWriter out = null;
		try {
			out = response.getWriter();

			GameInventoryStatusForBOHelper helper = new GameInventoryStatusForBOHelper();
			responseStr = helper.fetchWarehouseWiseInventory(warehouseId, gameId);
			out.print(responseStr);
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

}
