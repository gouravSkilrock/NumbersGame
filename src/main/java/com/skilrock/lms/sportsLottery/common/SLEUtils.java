package com.skilrock.lms.sportsLottery.common;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.skilrock.lms.rolemgmt.beans.userPrivBean;
import com.skilrock.lms.sportsLottery.userMgmt.javaBeans.MenuDataBean;
import com.skilrock.lms.sportsLottery.userMgmt.javaBeans.MenuItemDataBean;
import com.skilrock.lms.sportsLottery.userMgmt.javaBeans.PrivilegeDataBean;

public class SLEUtils {
	public static void addSLEPrivileges(Map<String, TreeMap<String, TreeMap<String, List<String>>>> lmsPrivMap, List<PrivilegeDataBean> slePrivList) {
		TreeMap<String, TreeMap<String, List<String>>> interfaceMap = null;
		TreeMap<String, List<String>> privMap = null;
		List<String> groupNameList = null;

		int i = 24;
		for(PrivilegeDataBean privilegeBean : slePrivList) {
			i += 2;
			/*if (!rolePrivMap.containsKey("Sports Lottery")) {
				interfaceMap = new TreeMap<String, TreeMap<String, List<String>>>();
			}*/
			interfaceMap = new TreeMap<String, TreeMap<String, List<String>>>();
			privMap = new TreeMap<String, List<String>>();
			for(MenuDataBean menuBean : privilegeBean.getMenuList()) {
				if(menuBean.getMenuItems().size() > 0) {
					groupNameList = new ArrayList<String>();
					for(MenuItemDataBean menuItemBean : menuBean.getMenuItems()) {
						groupNameList.add(menuItemBean.getMenuItemName());
					}
					privMap.put(menuBean.getMenuName().toUpperCase(), groupNameList);
				}
			}
			interfaceMap.put(privilegeBean.getInterfaceDevName()+"-"+i, privMap);
			lmsPrivMap.put("Sports Lottery", interfaceMap);
		}
	}

	public static void editSLEPrivileges(Map<String, TreeMap<String, TreeMap<String, List<userPrivBean>>>> lmsPrivMap, List<PrivilegeDataBean> slePrivList) {
		TreeMap<String, TreeMap<String, List<userPrivBean>>> interfaceMap = null;
		TreeMap<String, List<userPrivBean>> privMap = null;
		List<userPrivBean> groupNameList = null;
		userPrivBean privBean = null;

		int i = 0;
		
		TreeMap<String, TreeMap<String, List<userPrivBean>>> sleInterfaceMap = lmsPrivMap.get("Sports Lottery");
		Map<String, Integer> sleKeys = new HashMap<String, Integer>();
		
		for(String key : sleInterfaceMap.keySet()) {
			sleKeys.put(key.split("-")[0], Integer.parseInt(key.split("-")[1]));
		}
		
		for (PrivilegeDataBean privilegeBean : slePrivList) {
			// i += 2;
			if (sleKeys.containsKey(privilegeBean.getInterfaceDevName()))
				i = sleKeys.get(privilegeBean.getInterfaceDevName());
			else
				continue;
			/*if (!lmsPrivMap.containsKey("SLE")) {
				interfaceMap = new TreeMap<String, TreeMap<String, List<userPrivBean>>>();
			}*/
			interfaceMap = new TreeMap<String, TreeMap<String, List<userPrivBean>>>();
			privMap = new TreeMap<String, List<userPrivBean>>();
			for(MenuDataBean menuBean : privilegeBean.getMenuList()) {
				if(menuBean.getMenuItems().size() > 0) {
					groupNameList = new ArrayList<userPrivBean>();
					for(MenuItemDataBean menuItemBean : menuBean.getMenuItems()) {
						privBean = new userPrivBean();
						privBean.setPrivTitle(menuItemBean.getMenuItemName());
						privBean.setStatus(menuItemBean.getIsAssign()==true?"ACTIVE":"INACTIVE");
						privBean.setPrivRelatedTo(menuBean.getMenuName());
						groupNameList.add(privBean);
					}
					privMap.put(menuBean.getMenuName().toUpperCase(), groupNameList);
				}
			}
			interfaceMap.put(privilegeBean.getInterfaceDevName()+"-"+i, privMap);
			lmsPrivMap.put("Sports Lottery", interfaceMap);
		}
	}
}