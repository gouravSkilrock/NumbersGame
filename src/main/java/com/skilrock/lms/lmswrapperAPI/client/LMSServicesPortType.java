
package com.skilrock.lms.lmswrapperAPI.client;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import com.skilrock.lms.api.lms.beans.LmsUserIdMappingRequestBean;
import com.skilrock.lms.api.lms.beans.LmsUserIdMappingResponseBean;

@WebService(name = "LMSServicesPortType", targetNamespace = "http://lms.api.lms.skilrock.com")
@SOAPBinding(use = SOAPBinding.Use.LITERAL, parameterStyle = SOAPBinding.ParameterStyle.WRAPPED)
public interface LMSServicesPortType {

	@WebMethod(operationName = "getRandomMappingIdFromThirdParty", action = "")
	@WebResult(name = "out", targetNamespace = "http://lms.api.lms.skilrock.com")
	public LmsUserIdMappingResponseBean getRandomMappingIdFromThirdParty(
			@WebParam(name = "in0", targetNamespace = "http://lms.api.lms.skilrock.com") LmsUserIdMappingRequestBean in0);

}
