package com.skilrock.lms.coreEngine.scratchService.invoiceMgmt.serviceImpl;

import java.sql.Connection;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.skilrock.lms.common.db.DBConnect;
import com.skilrock.lms.coreEngine.scratchService.common.ScratchErrors;
import com.skilrock.lms.coreEngine.scratchService.common.ScratchException;
import com.skilrock.lms.coreEngine.scratchService.common.beans.OrderGameBookBeanMaster;
import com.skilrock.lms.coreEngine.scratchService.invoiceMgmt.daoImpl.ScratchInvoiceDaoImpl;

public class ScratchInvoiceServiceImpl {

	final static Log logger = LogFactory.getLog(ScratchInvoiceServiceImpl.class);

	// Done
	public void generateScratchInvoiceAndDeductBalance() throws ScratchException {
		Connection con = null;
		try {
			con = DBConnect.getConnection();
			new ScratchInvoiceDaoImpl().generateScratchInvoiceAndDeductBalance(con);
		} catch (ScratchException e) {
			throw e;
		} catch (Exception e) {
			e.printStackTrace();
			throw new ScratchException(ScratchErrors.GENERAL_EXCEPTION_ERROR_CODE, ScratchErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		} finally {
			DBConnect.closeCon(con);
		}
	}

	// Done
	public void checkAndUpdateForInvoice(int orgId, Map<Integer, OrderGameBookBeanMaster> gameBookMap) throws ScratchException {
		Connection con = null;
		try {
			con = DBConnect.getConnection();
			new ScratchInvoiceDaoImpl().checkAndUpdateForInvoice(orgId, gameBookMap, con);
		} catch (ScratchException e) {
			throw e;
		} catch (Exception e) {
			e.printStackTrace();
			throw new ScratchException(ScratchErrors.GENERAL_EXCEPTION_ERROR_CODE, ScratchErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		} finally {
			DBConnect.closeCon(con);
		}
	}

	// DONE
	public int generateInvoiceForAgent(int orgId, Map<Integer, OrderGameBookBeanMaster> gameBookMap, String dl) throws ScratchException {
		Connection con = null;
		int invoiceId = -1;
		try {
			con = DBConnect.getConnection();
			invoiceId = new ScratchInvoiceDaoImpl().generateInvoiceForAgent(orgId, gameBookMap, dl, con);
		} catch (ScratchException e) {
			throw e;
		} catch (Exception e) {
			e.printStackTrace();
			throw new ScratchException(ScratchErrors.GENERAL_EXCEPTION_ERROR_CODE, ScratchErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		} finally {
			DBConnect.closeCon(con);
		}
		return invoiceId;
	}

	public int generateInvoiceForRetailer(int orgId, Map<Integer, OrderGameBookBeanMaster> gameBookMap, String dl) throws ScratchException {
		Connection con = null;
		int invoiceId = -1;
		try {
			con = DBConnect.getConnection();
			invoiceId = new ScratchInvoiceDaoImpl().generateInvoiceForRetailer(orgId, gameBookMap, dl, con);
		} catch (ScratchException e) {
			throw e;
		} catch (Exception e) {
			e.printStackTrace();
			throw new ScratchException(ScratchErrors.GENERAL_EXCEPTION_ERROR_CODE, ScratchErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		} finally {
			DBConnect.closeCon(con);
		}
		return invoiceId;
	}
	
}
