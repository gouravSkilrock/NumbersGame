package com.skilrock.lms.rest.scratchService.orderMgmt.service;

import java.util.List;

import com.skilrock.lms.beans.UserInfoBean;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.coreEngine.scratchService.common.ScratchException;
import com.skilrock.lms.rest.services.bean.DaoBean;
import com.skilrock.lms.rest.services.bean.ScracthMgmtBean;
import com.skilrock.lms.scratchService.orderMgmt.controllerImpl.GameListResponseData;

public interface ScratchService {
	
	public boolean isBookNumberListValid(ScracthMgmtBean bean);
	public String updateBookListStatus(ScracthMgmtBean bean);
	public List<GameListResponseData> getGameList() throws Exception,ScratchException;
	public int getAgentOrganizationId(String tpUserId) throws LMSException;
	public DaoBean getRetailerData(String tpUserId) throws LMSException;
	public UserInfoBean getUserBeanFromTPUserId(String tpUserId) throws LMSException;
	public int getParentOrgId(int userId) throws LMSException;
	
}
