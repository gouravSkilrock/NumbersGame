package com.skilrock.lms.common.utility;




import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;
import com.skilrock.lms.embedded.drawGames.common.EmbeddedErrors;
import com.skilrock.lms.web.drawGames.common.Util;

public class checkCRC extends AbstractInterceptor{
/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
static Log logger =LogFactory.getLog(checkCRC.class);
	@Override
	public String intercept(ActionInvocation invocation) throws Exception {
		
		 String result = null;
		
		HttpServletRequest request=ServletActionContext.getRequest();
		HttpServletResponse response=ServletActionContext.getResponse();
		String crcString=request.getParameter("pickedNumbers")+request.getParameter("noPicked")+request.getParameter("playType");
		
		//Convert string to bytes
        long lngChecksum=Util.getCheckSum(crcString);
        //logger.info("CRC32 checksum for "+crcString+" is :" + lngChecksum);
        //logger.info("Get CRC For Sale"+request.getParameter("CRC"));
        
        if(request.getParameter("CRC") == null || !(lngChecksum== Long.valueOf(request.getParameter("CRC")))){
        	logger.info("CRC32 checksum for "+crcString+" is :" + lngChecksum);
            logger.info("Get CRC For Sale"+request.getParameter("CRC"));
           
        	response
			.getOutputStream()
			.write(
					("ErrorMsg:" + EmbeddedErrors.ERROR_MSG)
							.getBytes());
        	return result;
				
        }
   		invocation.invoke();
   		
		if(request.getAttribute("purchaseData")!=null && !"".equals(request.getAttribute("purchaseData")) && !"WEB".equals((String) request.getAttribute("interfaceType"))){
		 long responseCRC=Util.getCheckSum((String)request.getAttribute("purchaseData"));
		 logger.info("check sum String for Response"+request.getAttribute("purchaseData")+" is "+responseCRC);
		 response
			.getOutputStream()
			.write(
					("CRC:"+String.valueOf(responseCRC)+"|")
							.getBytes());
		}
		return result;
	}
	


}
