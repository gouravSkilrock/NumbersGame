package com.skilrock.lms.coreEngine.instantPrint.service;

import com.skilrock.lms.beans.ServiceRequest;
import com.skilrock.lms.beans.ServiceResponse;

public interface IServiceDelegate {
	
	public ServiceResponse getResponse(ServiceRequest sReq);

}
