package com.skilrock.lms.api.ola;

import com.skilrock.lms.api.ola.beans.OlaRummyDepositBean;
import com.skilrock.lms.api.ola.beans.OlaWithdrawlRequestBean;





public interface IOlaService {
	
	public OlaRummyDepositBean plrDepositVerification(OlaRummyDepositBean olaServiceBean);
	public OlaWithdrawlRequestBean plrWithdrawlRequest(OlaWithdrawlRequestBean olaServiceBean);
	
	
}
