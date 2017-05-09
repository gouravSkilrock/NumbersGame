package com.skilrock.lms.beans;

import java.io.Serializable;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * This object holds the details sent from the presentation layer to the service
 * layer.
 * 
 */
public class ServiceRequest implements Serializable {
	static Log logger = LogFactory.getLog(ServiceRequest.class);

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	// It contains the data that needs to sent to the
	// service layer
	private Object serviceData;

	// the name of the method that needs to be called
	private String serviceMethod;

	// the name of the service that needs to be called
	private String serviceName;

	/**
	 * @return the serviceData
	 */
	public Object getServiceData() {
		return serviceData;
	}

	/**
	 * @return the serviceMethod
	 */
	public String getServiceMethod() {
		return serviceMethod;
	}

	/**
	 * @return the serviceName
	 */
	public String getServiceName() {
		return serviceName;
	}

	/**
	 * @param serviceData
	 *            the serviceData to set
	 */
	public void setServiceData(Object serviceData) {
		this.serviceData = serviceData;
	}

	/**
	 * @param serviceMethod
	 *            the serviceMethod to set
	 */
	public void setServiceMethod(String serviceMethod) {
		this.serviceMethod = serviceMethod;
	}

	/**
	 * @param serviceName
	 *            the serviceName to set
	 */
	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

}
