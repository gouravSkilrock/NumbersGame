package com.skilrock.lms.api.lmsPayment;

import com.skilrock.lms.api.beans.PWTApiBean;
import com.skilrock.lms.api.beans.TpPwtApiBean;
import com.skilrock.lms.api.lmsPayment.beans.LmsCashPaymentBean;
import com.skilrock.lms.api.lmsPayment.beans.LmsCashPaymentResponseBean;
import com.skilrock.lms.api.lmsPayment.beans.LmsOrgInfoBean;

public interface ILmsPaymentService {
			
	public LmsCashPaymentResponseBean depositCashPayment(LmsCashPaymentBean cashPaymentBean);

	public LmsOrgInfoBean getOrgInfo(String organizationCode,String systemUserName,String systemUserPassword);
	
	public LmsCashPaymentResponseBean getPaymentStatus(String refTransId,String systemUserName,String systemUserPassword);
	
	public PWTApiBean verifyTicket(String ticketNbr,String systemUserName,String systemUserPassword);
	
	public PWTApiBean pwtPayment(TpPwtApiBean pwtReqBean);
}