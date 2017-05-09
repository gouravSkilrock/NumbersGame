package com.skilrock.lms.sportsLottery.userMgmt.javaBeans;

import java.io.Serializable;
import java.util.List;

public class MenuDataBean implements Serializable {
	private static final long serialVersionUID = 1L;

	private int menuId;
	private String menuName;
	private List<MenuItemDataBean> menuItems;

	public MenuDataBean() {
	}

	public int getMenuId() {
		return menuId;
	}

	public void setMenuId(int menuId) {
		this.menuId = menuId;
	}

	public String getMenuName() {
		return menuName;
	}

	public void setMenuName(String menuName) {
		this.menuName = menuName;
	}

	public List<MenuItemDataBean> getMenuItems() {
		return menuItems;
	}

	public void setMenuItems(List<MenuItemDataBean> menuItems) {
		this.menuItems = menuItems;
	}

	@Override
	public String toString() {
		return "MenuDataBean [menuId=" + menuId + ", menuName=" + menuName
				+ ", menuItems=" + menuItems + "]";
	}

}