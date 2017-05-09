package com.skilrock.lms.dao;

import com.skilrock.lms.coreEngine.sportsLottery.common.SLEException;
import com.skilrock.lms.sportsLottery.userMgmt.javaBeans.UserDataBean;

public interface UserMgmtDao {
	public UserDataBean getUserInfo(String userName) throws SLEException;
	public void updateUserLogout(String userName);
}