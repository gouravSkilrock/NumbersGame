package com.skilrock.lms.scratchService.dataMgmt.controllerImpl;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.skilrock.lms.common.db.DBConnect;
import com.skilrock.lms.common.exception.GenericException;
import com.skilrock.lms.common.exception.LMSErrors;
import com.skilrock.lms.coreEngine.scratchService.orderMgmt.beans.ScratchGameDataBean;
import com.skilrock.lms.scratchService.dataMgmt.daoImpl.ScratchGameDataDaoImpl;

public class ScratchGameDataControllerImpl {

	final static Log logger = LogFactory
			.getLog(ScratchGameDataControllerImpl.class);

	private static ScratchGameDataControllerImpl singleInstance;
	private ScratchGameDataControllerImpl(){}
	 public static ScratchGameDataControllerImpl getSingleInstance() {
		    if (singleInstance == null) {
		      synchronized (ScratchGameDataControllerImpl.class) {
		        if (singleInstance == null) {
		          singleInstance = new ScratchGameDataControllerImpl();
		        }
		      }
		    }
		    return singleInstance;
		  }
	 
	public List<ScratchGameDataBean> getScratchGameList() throws  GenericException {
		logger
				.info("***** Inside getScratchGameList method of ScratchGameDataServiceImpl class");

		List<ScratchGameDataBean> scratchGameList = null;
		ScratchGameDataDaoImpl scratchGameDataDaoImpl = null;
		Connection con = null;

		try {
			con = DBConnect.getConnection();
			scratchGameDataDaoImpl = ScratchGameDataDaoImpl.getSingleInstance();
			scratchGameList = scratchGameDataDaoImpl.getScratchGameData(con);
			logger.info("Scratch Game List is " + scratchGameList.toString());
		}  catch (SQLException e) {
			throw new GenericException(LMSErrors.SQL_EXCEPTION_CODE, e);
		}catch (Exception e) {
			throw new GenericException(LMSErrors.GENERAL_EXCEPTION_CODE, e);
		} finally {
			DBConnect.closeCon(con);
		}
		return scratchGameList;
	}
}
