package com.skilrock.lms.common.utility;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;

public class Service extends AbstractInterceptor {

	static Log logger = LogFactory.getLog(Service.class);

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String code;
	private String interfaceType;

	public String getCode() {
		return code;
	}

	public String getInterfaceType() {
		return interfaceType;
	}

	@Override
	public String intercept(ActionInvocation invocation) throws Exception {
		HttpServletRequest request = ServletActionContext.getRequest();
		request.setAttribute("code", getCode());
		request.setAttribute("interfaceType", getInterfaceType());
		return invocation.invoke();
	}

	public void setCode(final String code) {
		this.code = code;
	}

	public void setInterfaceType(String interfaceType) {
		this.interfaceType = interfaceType;
	}
}
