package com.skilrock.lms.rest.common;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response.Status;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.web.drawGames.common.Util;
import com.sun.jersey.spi.container.ContainerRequest;
import com.sun.jersey.spi.container.ContainerRequestFilter;
import com.sun.jersey.spi.container.ContainerResponse;
import com.sun.jersey.spi.container.ContainerResponseFilter;


public class AuthorizationRequestFilter implements ContainerRequestFilter,ContainerResponseFilter{
	
	
	private static final Logger logger = LoggerFactory.getLogger(AuthorizationRequestFilter.class);
	@Override
	public ContainerRequest filter(ContainerRequest req) {
		int merchantId=0;
		try {
			//System.out.println(Thread.currentThread().getId());
			/*String method = req.getMethod();
	        String path = req.getPath(true);
			 */
			String path = req.getRequestUri().getPath();
			TransactionManager.startTransaction();
			
			logger.info("AUDIT_REQUEST_ID={}=Request Header={}",TransactionManager.getAuditId(),req.getRequestHeaders());

	        //Get the authentification passed in HTTP headers parameters
			
	        String userName = /*"sleUser";*/req.getHeaderValue("userName");
	        String password = /*"slePassword";*/req.getHeaderValue("password");
	        
	        //If the user does not have the right (does not provide any HTTP Basic Auth)
	        if(!(path.contains("virtualBetting") || path.contains("tpSlMgmt"))){
		        if(userName == null || password == null){
		            throw new WebApplicationException(Status.UNAUTHORIZED);
		        }else{
		        	
		        	try {
		        		merchantId=MerchantAuthorizationHelper.merchantAuthorization(userName, password);
		        		TransactionManager.setMerchantId(merchantId);
					} catch (LMSException e) {
						 throw new WebApplicationException(Status.UNAUTHORIZED);
					}
		        }
	        }
	        
			StringWriter writer = new StringWriter();
			IOUtils.copy(req.getEntityInputStream(), writer, "UTF-8");

			// This is your POST Body as String
			String body = writer.toString();
			logger.info("AUDIT_REQUEST_ID={}=merchantId{}=requestTime={}=requestUri={}=requestData={}",new Object[]{TransactionManager.getAuditId(),TransactionManager.getMerchantId(),Util.getCurrentTimeStamp(),req.getRequestUri(),body});
			
			//System.out.println(req.getRequestHeader("principal"));
			InputStream in = new ByteArrayInputStream(body.getBytes());
			req.setEntityInputStream(in);
		} catch (IOException e) {
		}
		return req;
	}
	@Override
	public ContainerResponse filter(ContainerRequest request,
			ContainerResponse response) {
		
		//System.out.println(Thread.currentThread().getId());
		logger.info("AUDIT_RESPONSE_ID={}=TimeTaken{}=",TransactionManager.getAuditId(),System.currentTimeMillis() - TransactionManager.getAuditTime());
		logger.info("AUDIT_RESPONSE_ID={}=responseTime={}=responseStatus={}=statusType={}=requestUri={}=responseData={}",new Object[]{TransactionManager.getAuditId(),Util.getCurrentTimeStamp(), response.getStatus(),response.getStatusType(), request.getRequestUri(),response.getEntity()});
		TransactionManager.endTransaction();
		return response;
	}

}