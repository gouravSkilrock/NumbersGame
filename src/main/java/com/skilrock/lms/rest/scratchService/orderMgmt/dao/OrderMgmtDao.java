package com.skilrock.lms.rest.scratchService.orderMgmt.dao;

import java.sql.Connection;
import java.util.List;

import com.skilrock.lms.coreEngine.scratchService.common.ScratchException;
import com.skilrock.lms.scratchService.orderMgmt.controllerImpl.GameListResponseData;

public interface OrderMgmtDao {

	public List<GameListResponseData> getGameListDaoImpl(Connection connection) throws ScratchException;
}
