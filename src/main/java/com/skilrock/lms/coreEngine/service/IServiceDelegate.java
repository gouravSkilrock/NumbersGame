package com.skilrock.lms.coreEngine.service;

import com.skilrock.lms.beans.ServiceRequest;
import com.skilrock.lms.beans.ServiceResponse;



/**
 * @author Arun Tanwar
 * 
 * <pre>
 * Change History
 * Change Date     Changed By     Change Description
 * -----------     ----------     ------------------
 * (e.g.)
 * 01-JAN-2010     ABxxxxxx       CR#zzzzzz:
 * 26-MAY-2010	   Arun Tanwar    CR#:Performance enhancement changes 
 * </pre>
 */
public interface IServiceDelegate {
	/**
	 * This method will invoke the SessionBean method.
	 * 
	 * @param sReq -
	 *            ServiceRequest coarse grain object containing bean data
	 * @return - ServiceResponse object containing bean data
	 */
	public ServiceResponse getResponse(ServiceRequest sReq);
	public String getResponseString(ServiceRequest sReq);
	public String getResponseStringForTrainingExpense(ServiceRequest sReq);

}
