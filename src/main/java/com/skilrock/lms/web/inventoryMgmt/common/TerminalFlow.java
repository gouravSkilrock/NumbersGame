package com.skilrock.lms.web.inventoryMgmt.common;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;

import com.opensymphony.xwork2.ActionSupport;
import com.skilrock.lms.beans.InvTransitionBean;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.coreEngine.inventoryMgmt.TerminalFlowHelper;

public class TerminalFlow extends ActionSupport implements ServletRequestAware,ServletResponseAware {
	private static final long serialVersionUID = 1L;

	private HttpServletRequest request;
	private HttpServletResponse response;
	private List<InvTransitionBean> transitionList;
	private String termNumber;
	private String modelId;
	private String bind_len;	
	
	
	public String getTermFlow() {
		try {
				int bindingLength = Integer.parseInt(bind_len);
				String rSerNo = termNumber.trim();
				String validSerialNo = termNumber.trim();
				if(bindingLength > 0 && validSerialNo.length() >= bindingLength){
					termNumber = validSerialNo.substring(validSerialNo.length()- bindingLength, validSerialNo.length());
					validSerialNo = new TerminalFlowHelper().fetchSerialNo(Integer.parseInt(modelId.split("-")[0]), bindingLength, termNumber);
					if(!(rSerNo.equals(validSerialNo) || rSerNo.equals(termNumber))){
						validSerialNo = null;
					}
				}
				StringBuilder sb = new StringBuilder("");		
				if(validSerialNo != null){
					transitionList = new TerminalFlowHelper().getTermFlow(validSerialNo, sb, Integer.parseInt(modelId.split("-")[0]));
				}else{
					sb.append("Invalid");
				}
				
				if("Invalid".equalsIgnoreCase(sb.toString())){
					request.getSession().setAttribute("IS_VALID_TERMINAL", "false");
					System.out.println("Terminal Number: Invalid Terminal");
				} else {
					request.getSession().setAttribute("IS_VALID_TERMINAL", "true");
					System.out.println("Terminal Number: Valid Terminal");
				}
			} catch (LMSException e) {
				e.printStackTrace();
			}
		return SUCCESS;
	}

	public String getBind_len() {
		return bind_len;
	}

	public void setBind_len(String bindLen) {
		bind_len = bindLen;
	}

	public HttpServletResponse getResponse() {
		return response;
	}

	public List<InvTransitionBean> getTransitionList() {
		return transitionList;
	}

	public void setServletResponse(HttpServletResponse response) {
		this.response = response;
	}

	public void setTransitionList(List<InvTransitionBean> transitionList) {
		this.transitionList = transitionList;
	}

	public String getTermNumber() {
		return termNumber;
	}

	public void setTermNumber(String termNumber) {
		this.termNumber = termNumber;
	}	
	
	public void setServletRequest(HttpServletRequest request) {
		this.request = request;
	}
	
	public HttpServletRequest getRequest() {
		return request;
	}
	
	public String getModelId() {
		return modelId;
	}

	public void setModelId(String modelId) {
		this.modelId = modelId;
	}
	
}
