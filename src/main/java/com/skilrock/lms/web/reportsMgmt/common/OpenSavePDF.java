package com.skilrock.lms.web.reportsMgmt.common;

import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;

import com.opensymphony.xwork2.ActionSupport;

/**
 * This class is used for displaying the Pdf file by appending the result into
 * the Response.
 * 
 * @author Skilrock Technologies
 * 
 */
public class OpenSavePDF extends ActionSupport implements ServletRequestAware,
		ServletResponseAware {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	Log logger = LogFactory.getLog(OpenSavePDF.class);
	String pathofPDF = null;
	HttpServletRequest request;
	HttpServletResponse response;

	public HttpServletRequest getRequest() {
		return request;
	}

	public HttpServletResponse getResponse() {
		return response;
	}

	/**
	 * This method is used for displaying the Pdf file by appending the result
	 * into the Response.
	 * 
	 * @return String
	 */
	public String getResponsePDF() {
		logger.debug("Inside boLedger");
		logger.debug("response cache control when pdf generataion");
		// logger.debug("In PDF Generation in Graph Action getResponsePDF
		// OPen save PDF");
		// logger.debug("response cache control when pdf generataion");
		int lengthOfFile = 0;
		try {
			HttpSession session = getRequest().getSession();
			lengthOfFile = ((byte[]) session.getAttribute("GENERATED_BYTES")).length;
			logger.debug("Length of File" + lengthOfFile);
			// logger.debug("Length of File"+lengthOfFile);
			if (lengthOfFile > 800) {
				response.setContentType("application/pdf");
				OutputStream OutStrm = response.getOutputStream();
				OutStrm.write((byte[]) session.getAttribute("GENERATED_BYTES"));
				logger
						.debug("Length of File"
								+ ((byte[]) session
										.getAttribute("GENERATED_BYTES")).length);
				// logger.debug("Length of File" + ((byte[])
				// session.getAttribute("GENERATED_BYTES")).length);
				OutStrm.flush();
				return null;
			}
			// OutStrm.close();
		} catch (java.lang.IllegalStateException e) {
			logger
					.error("Error in Output Stream in Graph Report Action getResponsePDF Method");
			// System.out
			// .println("Error in Output Stream in Graph Report Action
			// getResponsePDF Method");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		/*
		 * if(lengthOfFile>0){ return null; }
		 */
		return SUCCESS;
	}
	
	private String receiptId;
	
	
	
	public String getReceiptPDF() {
		logger.debug("Inside Receipt Generator");
		logger.debug("response cache control when pdf generataion");
		int lengthOfFile = 0;
		try {
				HttpSession session = getRequest().getSession();
				Map<String, byte[]> receiptByteMap = (HashMap<String, byte[]>)session.getAttribute("RECEIPT_BYTE_MAP");
				lengthOfFile = receiptByteMap.get(receiptId).length;
				logger.debug("Length of File" + lengthOfFile);
				if (lengthOfFile > 800) {
					response.setContentType("application/pdf");
					OutputStream OutStrm = response.getOutputStream();
					OutStrm.write(receiptByteMap.get(receiptId));
					logger.debug("Length of File"+ (receiptByteMap.get(receiptId)).length);
					OutStrm.flush();
				return null;
			}
		} catch (java.lang.IllegalStateException e) {
			logger.error("Error in Output Stream in Graph Report Action getResponsePDF Method");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return SUCCESS;
	}

	public void setServletRequest(HttpServletRequest request) {
		this.request = request;

	}

	public void setServletResponse(HttpServletResponse response) {
		this.response = response;

	}

	public void setReceiptId(String receiptId) {
		this.receiptId = receiptId;
	}

	public String getReceiptId() {
		return receiptId;
	}

}
