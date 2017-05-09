package com.skilrock.lms.common.utility;




import java.io.OutputStream;

import java.text.SimpleDateFormat;

import java.util.Date;



import javax.servlet.http.HttpServletRequest;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.ServletActionContext;



import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;


import com.skilrock.lms.beans.UserInfoBean;

import com.skilrock.lms.rest.common.TransactionManager;


public class AuditLogInterceptor extends AbstractInterceptor {
	static Log logger = LogFactory.getLog(AuditLogInterceptor.class);
	private static final long serialVersionUID = 1L;
	
	private String serviceName;
	private String interfaceType;
	private OutputStream os;
	

	public String getServiceName() {
		return serviceName;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}
	
	

	public String getInterfaceType() {
		return interfaceType;
	}

	public void setInterfaceType(String interfaceType) {
		this.interfaceType = interfaceType;
	}

	@Override
	public String intercept(ActionInvocation invocation) throws Exception  {
		
		HttpServletRequest request = ServletActionContext.getRequest();

		 boolean isTxnExists=TransactionManager.getContext().get()==null?false:true;
		SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try{
		   
			if(!isTxnExists){
				TransactionManager.startTransaction();
			}
		

		UserInfoBean userBean = (UserInfoBean) request.getSession().getAttribute("USER_INFO");
		String lacId = (String) request.getSession().getAttribute("LAC");
		String cId = (String) request.getSession().getAttribute("CID");
		String userName = (userBean == null)?(request.getParameter("username")==null ? ((request.getParameter("userName")==null) ? "" : request.getParameter("userName")) : request.getParameter("username")):userBean.getUserName();
		
		request.setAttribute("AUDIT_ID",TransactionManager.getAuditId());
	    String strDate = sdfDate.format(new Date());
	    StringBuilder requestStr=new StringBuilder("REQUEST_AUDIT_TRAIL-"+request.getAttribute("AUDIT_ID"));
	    
	   // String uname = (request.getParameter("username")==null ? ((request.getParameter("userName")==null) ? "" : request.getParameter("userName")) : request.getParameter("username"));
	    requestStr.append("#userName=").append(userName);

	    requestStr.append("#IPAddress=").append(request.getRemoteAddr());
	    requestStr.append("#Entry_Time=").append(strDate);
	    requestStr.append("#ActionName=").append(invocation.getInvocationContext().getName());
	    requestStr.append("#serviceName=").append(serviceName);
	    requestStr.append("#interfaceType=").append(interfaceType);
	    if (lacId != null && cId != null) {
			requestStr.append("#LACID=").append(lacId);
			requestStr.append("#CID=").append(cId);
		}
	    request.setAttribute("re","sumit");
		logger.info(requestStr.toString());
		invocation.invoke();
		 StringBuilder responseStr=new StringBuilder("RESPONSE_AUDIT_TRAIL-"+request.getAttribute("AUDIT_ID"));
		 //responseStr.append("#userName=").append(request.getParameter("username"));
		//responseStr.append("#userName=").append(userName);
		
		    responseStr.append("#userName=").append(userName);
		   
		 responseStr.append("#Exit_Time=").append(sdfDate.format(new Date()));
		 responseStr.append("#ActionName=").append(invocation.getInvocationContext().getName());
		 responseStr.append("#serviceName=").append(serviceName);
		 logger.info(responseStr.toString());

		 if(!isTxnExists){
			 TransactionManager.endTransaction();
		 }
				
		 
		}catch (Exception e) {
			 if(!isTxnExists){
				 TransactionManager.endTransaction();
			 }
			
			throw e;
		}
		
		return null;
	}
	

	    @Override
	    public void destroy() {
	    }

	   }