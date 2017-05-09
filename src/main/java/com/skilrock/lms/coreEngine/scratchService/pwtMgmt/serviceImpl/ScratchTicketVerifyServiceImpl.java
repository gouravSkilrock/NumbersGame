package com.skilrock.lms.coreEngine.scratchService.pwtMgmt.serviceImpl;

import java.sql.Connection;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.skilrock.lms.common.db.DBConnect;
import com.skilrock.lms.common.exception.LMSErrors;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.coreEngine.scratchService.pwtMgmt.daoImpl.ScratchTicketVerifyDaoImpl;

public class ScratchTicketVerifyServiceImpl {

	final static Log logger = LogFactory
			.getLog(ScratchTicketVerifyServiceImpl.class);

	public void verifyScratchTicket(String ticketNbr, String virnNbr)
			throws LMSException {
		logger
				.info("***** Inside verifyScratchTicket method of ScratchTicketVerifyServiceImpl class");

		ScratchTicketVerifyDaoImpl scratchTicketVerifyDaoImpl = null;
		Connection con = null;

		try {
			con = DBConnect.getConnection();
			scratchTicketVerifyDaoImpl = new ScratchTicketVerifyDaoImpl();

			scratchTicketVerifyDaoImpl.isScratchTicketValid(ticketNbr, virnNbr,
					con);
		} catch (LMSException e) {
			throw e;
		} catch (Exception e) {
			throw new LMSException(LMSErrors.GENERAL_EXCEPTION_ERROR_CODE,
					LMSErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		} finally {
			DBConnect.closeCon(con);
		}
	}

}
