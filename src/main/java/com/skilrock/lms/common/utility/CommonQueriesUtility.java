package com.skilrock.lms.common.utility;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.ServletActionContext;

public class CommonQueriesUtility {
	static Log logger = LogFactory.getLog(CommonQueriesUtility.class);

	public String insTransaction() {
		try {
			HttpServletRequest request = ServletActionContext.getRequest();
			String INSERT_LMS_TRANSACTION_MASTER = "INSERT INTO st_lms_transaction_master (user_type, service_code, interface) VALUES (?, '"
					+ request.getAttribute("code")
					+ "', '"
					+ request.getAttribute("interfaceType") + "')";
			return INSERT_LMS_TRANSACTION_MASTER;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}

}
