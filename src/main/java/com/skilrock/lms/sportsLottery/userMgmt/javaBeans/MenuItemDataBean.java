package com.skilrock.lms.sportsLottery.userMgmt.javaBeans;

import java.io.Serializable;

public class MenuItemDataBean implements Serializable {
	private static final long serialVersionUID = 1L;

	private int menuItemId;
	private String menuItemName;
	private Boolean isAssign;

	public MenuItemDataBean() {
	}

	public int getMenuItemId() {
		return menuItemId;
	}

	public void setMenuItemId(int menuItemId) {
		this.menuItemId = menuItemId;
	}

	public String getMenuItemName() {
		return menuItemName;
	}

	public void setMenuItemName(String menuItemName) {
		this.menuItemName = menuItemName;
	}

	public Boolean getIsAssign() {
		return isAssign;
	}

	public void setIsAssign(Boolean isAssign) {
		this.isAssign = isAssign;
	}

	@Override
	public String toString() {
		return "MenuItemDataBean [menuItemId=" + menuItemId + ", menuItemName="
				+ menuItemName + ", isAssign=" + isAssign + "]";
	}

}