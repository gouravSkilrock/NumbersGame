package com.skilrock.lms.sportsLottery.userMgmt.javaBeans;

import java.io.Serializable;
import java.util.List;

public class PrivilegeDataBean implements Serializable {
	private static final long serialVersionUID = 1L;

	private String interfaceDispName;
	private String interfaceDevName;
	private List<MenuDataBean> menuList;

	public PrivilegeDataBean() {
	}

	public String getInterfaceDispName() {
		return interfaceDispName;
	}

	public void setInterfaceDispName(String interfaceDispName) {
		this.interfaceDispName = interfaceDispName;
	}

	public String getInterfaceDevName() {
		return interfaceDevName;
	}

	public void setInterfaceDevName(String interfaceDevName) {
		this.interfaceDevName = interfaceDevName;
	}

	public List<MenuDataBean> getMenuList() {
		return menuList;
	}

	public void setMenuList(List<MenuDataBean> menuList) {
		this.menuList = menuList;
	}

	@Override
	public String toString() {
		return "PrivilegeDataBean [interfaceDispName=" + interfaceDispName
				+ ", interfaceDevName=" + interfaceDevName + ", menuList="
				+ menuList + "]";
	}

}