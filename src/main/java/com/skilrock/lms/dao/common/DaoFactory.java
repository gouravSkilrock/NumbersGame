package com.skilrock.lms.dao.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.skilrock.lms.dao.AccMgmtDao;
import com.skilrock.lms.dao.UserMgmtDao;
import com.skilrock.lms.daoImpl.AccMgmtDaoImpl;
import com.skilrock.lms.daoImpl.UserMgmtDaoImpl;

public class DaoFactory {
	private static Logger logger = LoggerFactory.getLogger(DaoFactory.class);

	private DaoFactory() {
	}

	private static UserMgmtDao userMgmtDao = null;
	private static AccMgmtDao accMgmtDao = null;

	static {
		logger.info("Initializing all the DAO Classes.....");
		userMgmtDao = UserMgmtDaoImpl.getInstance();
		accMgmtDao = AccMgmtDaoImpl.getInstance();
	}

	public static UserMgmtDao getUserMgmtDao() {
		return userMgmtDao;
	}

	public static AccMgmtDao getAccMgmtDao() {
		return accMgmtDao;
	}

}
