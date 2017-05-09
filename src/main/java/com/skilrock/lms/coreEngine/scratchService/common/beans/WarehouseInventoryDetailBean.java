package com.skilrock.lms.coreEngine.scratchService.common.beans;

import java.util.Map;

public class WarehouseInventoryDetailBean {
	private String warehouseName;
	private Map<Integer, WarehouseWiseGameInventoryDetailBean> warehouseGameMap;

	public String getWarehouseName() {
		return warehouseName;
	}

	public void setWarehouseName(String warehouseName) {
		this.warehouseName = warehouseName;
	}

	public Map<Integer, WarehouseWiseGameInventoryDetailBean> getWarehouseGameMap() {
		return warehouseGameMap;
	}

	public void setWarehouseGameMap(
			Map<Integer, WarehouseWiseGameInventoryDetailBean> warehouseGameMap) {
		this.warehouseGameMap = warehouseGameMap;
	}

	@Override
	public String toString() {
		return "WarehouseInventoryDetailBean [warehouseName=" + warehouseName
				+ ", warehouseGameMap=" + warehouseGameMap + "]";
	}

}
