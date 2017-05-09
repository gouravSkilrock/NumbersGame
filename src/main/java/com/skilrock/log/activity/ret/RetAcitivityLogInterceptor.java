package com.skilrock.log.activity.ret;

import java.util.Enumeration;
import java.util.Properties;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.ServletActionContext;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;
import com.skilrock.lms.beans.UserInfoBean;
import com.skilrock.lms.common.utility.PropertyLoader;
import com.skilrock.lms.rest.common.TransactionManager;

public class RetAcitivityLogInterceptor extends AbstractInterceptor {
	static Log logger = LogFactory.getLog(RetAcitivityLogInterceptor.class);
	private static final long serialVersionUID = 1L;
	
	private String serviceName;
	private String interfaceType;

	

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
		TransactionManager.startTransaction();
		TransactionManager.setResponseData("false");
		request.setAttribute("REQUEST_ID",TransactionManager.getActivityId());
		StringBuilder requestStr=new StringBuilder("RET__ACTIVITY_TRAIL-"+request.getAttribute("REQUEST_ID"));
		try{
	
		Enumeration headerNames = request.getHeaderNames();
		requestStr.append("HeaderInfo-");
		while(headerNames.hasMoreElements()) {
		  String headerName = (String)headerNames.nextElement();
		  requestStr.append("#").append(headerName).append("=").append(request.getHeader(headerName));
		 }
		requestStr.append(",ParameterInfo-");
		Enumeration params = request.getParameterNames(); 
		String userName=null;
		String lacId=null;
		String cId =null;
		while(params.hasMoreElements()){
		 String paramName = (String)params.nextElement();
		 if("username".equalsIgnoreCase(paramName)&&request.getParameter(paramName)!=null){
			 userName=request.getParameter(paramName);
		  }else if("lacId".equalsIgnoreCase(paramName)&&request.getParameter(paramName)!=null){
				 lacId=request.getParameter(paramName);
			  }else{
			  requestStr.append("#").append(paramName).append("=").append(request.getParameter(paramName));
		  }
		 
		
		 }
		UserInfoBean userBean = (UserInfoBean) request.getSession().getAttribute("USER_INFO");
		/*String lacId = (String) request.getSession().getAttribute("LAC");
		String cId = (String) request.getSession().getAttribute("CID");*/
		if(userName==null&&userBean!=null){
			userName=userBean.getUserName();
			
		}
		requestStr.append("#userName=").append(userName);
		if(lacId==null){
			lacId = (String) request.getSession().getAttribute("LAC");
			cId = (String) request.getSession().getAttribute("CID");
			
		}
		//String userName = (userBean == null)?(request.getParameter("username")==null ? ((request.getParameter("userName")==null) ? "" : request.getParameter("userName")) : request.getParameter("username")):userBean.getUserName();
	
	    
	   // String uname = (request.getParameter("username")==null ? ((request.getParameter("userName")==null) ? "" : request.getParameter("userName")) : request.getParameter("username"));
	    
	    requestStr.append("#IPAddress=").append(request.getRemoteAddr());
	    requestStr.append("#ActionName=").append(invocation.getInvocationContext().getName());
	    requestStr.append("#serviceName=").append(serviceName);
	    requestStr.append("#interfaceType=").append(interfaceType);
	  
	    if (lacId != null && cId != null) {
			requestStr.append("#LACID=").append(lacId);
			requestStr.append("#CID=").append(cId);
		}
		///logger.info(requestStr.toString());
	    long startTime =System.currentTimeMillis();
		invocation.invoke();
		requestStr.append("#status=").append(TransactionManager.getResponseData());
		 //StringBuilder responseStr=new StringBuilder("RESPONSE_AUDIT_TRAIL-"+request.getAttribute("AUDIT_ID"));
		 //responseStr.append("#userName=").append(request.getParameter("username"));
		//responseStr.append("#userName=").append(userName);
		long endTime =System.currentTimeMillis();
	  	requestStr.append("#responseTime=").append(endTime-startTime);
		   
	/*	 responseStr.append("#Exit_Time=").append(sdfDate.format(new Date()));
		 responseStr.append("#ActionName=").append(invocation.getInvocationContext().getName());
		 responseStr.append("#serviceName=").append(serviceName);*/
		//logger.info("helloss");
	    // ActivityLogger.printInfo("className: {} msg: {}", RetAcitivityLogInterceptor.class.getName(),requestStr.toString());
	  	Properties properties = PropertyLoader.loadProperties("config/LMS.properties");
		//String JBOSS_AS = properties.getProperty("JBOSS_AS");
		logger.info("#ActivityReponse={}#className="+RetAcitivityLogInterceptor.class.getName()+"#ID="+TransactionManager.getAuditId()+"#responseData="+requestStr.toString());
		/*if ("TRUE".equalsIgnoreCase(JBOSS_AS.trim())) {
			logger.info("#ActivityReponse={}#className="+RetAcitivityLogInterceptor.class.getName()+"#ID="+TransactionManager.getAuditId()+"#responseData="+requestStr.toString());
		}
		else{
			ActivityLogger.printInfo("#ActivityReponse={}#className={}#ID={}#responseData={} ","",RetAcitivityLogInterceptor.class.getName(),TransactionManager.getAuditId(),requestStr.toString());
		}*/
			
		 TransactionManager.endTransaction();
		}catch (Exception e) {
			TransactionManager.endTransaction();
			throw e;
		}
		
		return null;
	}
	
		    @Override
	    public void destroy() {
	    }

	 }
