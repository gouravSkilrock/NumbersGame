package com.skilrock.lms.rest.scratchService.inventoryMgmt.dao;

import java.sql.Connection;
import java.sql.SQLException;

import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.rest.services.bean.DaoBean;

public interface InventoryDao {
	public int insertTicketByTicketSaleTxn(Connection con, DaoBean daoBean) throws SQLException;
	public String updateSellTicketStatusAsUnSold(DaoBean daoBean) throws LMSException;
}
