package com.skilrock.lms.coreEngine.scratchService.orderMgmt.serviceImpl;

import java.sql.Connection;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.skilrock.lms.common.db.DBConnect;
import com.skilrock.lms.common.exception.LMSErrors;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.coreEngine.scratchService.orderMgmt.beans.ScratchGameDataBean;
import com.skilrock.lms.coreEngine.scratchService.orderMgmt.daoImpl.ScratchGameDataDaoImpl;

public class ScratchGameDataServiceImpl {

	final static Log logger = LogFactory
			.getLog(ScratchGameDataServiceImpl.class);

	public List<ScratchGameDataBean> getScratchGameList() throws LMSException {
		logger
				.info("***** Inside getScratchGameList method of ScratchGameDataServiceImpl class");

		List<ScratchGameDataBean> scratchGameList = null;
		ScratchGameDataDaoImpl scratchGameDataDaoImpl = null;
		Connection con = null;

		try {
			con = DBConnect.getConnection();
			scratchGameDataDaoImpl = new ScratchGameDataDaoImpl();
			scratchGameList = scratchGameDataDaoImpl.getScratchGameData(con);
			logger.info("Scratch Game List is " + scratchGameList.toString());
		} catch (LMSException e) {
			throw e;
		} catch (Exception e) {
			throw new LMSException(LMSErrors.GENERAL_EXCEPTION_ERROR_CODE,
					LMSErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		} finally {
			DBConnect.closeCon(con);
		}
		return scratchGameList;
	}
}
