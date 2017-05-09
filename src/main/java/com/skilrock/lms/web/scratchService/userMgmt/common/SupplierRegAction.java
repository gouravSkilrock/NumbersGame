package com.skilrock.lms.web.scratchService.userMgmt.common;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.interceptor.ServletRequestAware;

import com.opensymphony.xwork2.ActionSupport;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.coreEngine.scratchService.userMgmt.common.SupplierRegHelper;

/**
 * <p>
 * This class checks the userId and password and solves the authentication
 * purpose.
 * </p>
 */
public class SupplierRegAction extends ActionSupport implements
		ServletRequestAware {

	public static final String APPLICATION_ERROR = "applicationError";

	static Log logger = LogFactory.getLog(SupplierRegAction.class);

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String addrLine1;

	private String addrLine2;
	private String city;
	private String country;
	private long pin;
	private HttpServletRequest request;
	private String state;
	private String supName;

	public String createRetailer() {

		HttpSession session = getRequest().getSession();
		session.setAttribute("SUP_NAME", getSupName());

		String returntype;
		SupplierRegHelper createRet = new SupplierRegHelper();
		try {
			returntype = createRet.createRetailer(getSupName(), getAddrLine1(),
					getAddrLine2(), getCity(), getCountry(), getState(),
					getPin());
		} catch (LMSException le) {
			return APPLICATION_ERROR;
		}

		/*
		 * if(returntype.equals("ERROR")) { addActionError("USER Already
		 * Exists!!"); logger.debug("error retiutn"); return ERROR; }
		 * 
		 * if(returntype.equals("INPUT")) { addActionError("BO USER Already
		 * Exists!!"); logger.debug("error retiutn"); return INPUT; }
		 */
		if (returntype.equals("SUCCESS")) {
			return SUCCESS;
		} else {
			addActionError(getText("msg.supp.exist"));
		}
		return ERROR;

	}

	public String getAddrLine1() {
		return addrLine1;
	}

	public String getAddrLine2() {
		return addrLine2;
	}

	public String getCity() {
		return city;
	}

	public String getCountry() {
		return country;
	}

	public long getPin() {
		return pin;
	}

	public HttpServletRequest getRequest() {
		return request;
	}

	public String getState() {
		return state;
	}

	public String getSupName() {
		return supName;
	}

	public void setAddrLine1(String addrLine1) {
		this.addrLine1 = addrLine1;
	}

	public void setAddrLine2(String addrLine2) {
		this.addrLine2 = addrLine2;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public void setPin(long pin) {
		this.pin = pin;
	}

	public void setServletRequest(HttpServletRequest request) {
		this.request = request;
	}

	public void setState(String state) {
		this.state = state;
	}

	public void setSupName(String supName) {
		this.supName = supName;
	}
}