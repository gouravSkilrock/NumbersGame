package com.skilrock.lms.controller.dataMgmtController;

import com.skilrock.lms.coreEngine.sportsLottery.common.SLEException;
import com.skilrock.lms.rest.services.bean.TPRequestBean;

public interface ReconcileMgmtController {
	void reconcileSLETransactions(TPRequestBean requestBean) throws SLEException;
}
