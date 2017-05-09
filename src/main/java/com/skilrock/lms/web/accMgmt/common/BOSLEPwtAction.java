package com.skilrock.lms.web.accMgmt.common;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.opensymphony.xwork2.ModelDriven;
import com.skilrock.lms.beans.BOMasterApprovalBean;
import com.skilrock.lms.common.BaseAction;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.controllerImpl.WinningMgmtControllerImplSLE;

public class BOSLEPwtAction extends BaseAction implements ModelDriven<BOMasterApprovalBean> {
	static Log logger = LogFactory.getLog(BOSLEPwtAction.class);
	private static final long serialVersionUID = 1L;

	public BOSLEPwtAction() {
		super(BOSLEPwtAction.class);
	}

	@Override
	public BOMasterApprovalBean getModel() {
		approvalBean = new BOMasterApprovalBean();
		return approvalBean;
	}

	private BOMasterApprovalBean approvalBean;
	private List<BOMasterApprovalBean> masterApprovalList;
	private String processType;

	public BOMasterApprovalBean getApprovalBean() {
		return approvalBean;
	}

	public void setApprovalBean(BOMasterApprovalBean approvalBean) {
		this.approvalBean = approvalBean;
	}

	public List<BOMasterApprovalBean> getMasterApprovalList() {
		return masterApprovalList;
	}

	public void setMasterApprovalList(List<BOMasterApprovalBean> masterApprovalList) {
		this.masterApprovalList = masterApprovalList;
	}

	public String getProcessType() {
		return processType;
	}

	public void setProcessType(String processType) {
		this.processType = processType;
	}

	public String getMasterApprovalRequests() throws Exception {
		WinningMgmtControllerImplSLE controllerImpl = WinningMgmtControllerImplSLE.getInstance();
		try {
			masterApprovalList = controllerImpl.getMasOrPendingRequests(approvalBean, "PND_MAS");
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return SUCCESS;
	}

	public String processMasterApproval() throws Exception {
		WinningMgmtControllerImplSLE controllerImpl = WinningMgmtControllerImplSLE.getInstance();
		try {
			boolean status = controllerImpl.processMasterApproval(approvalBean.getTaskId(), processType, getUserBean().getUserId(), getUserBean().getUserType());
			logger.info("processMasterApproval - "+status);
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return SUCCESS;
	}

	public String getPendingPaymentRequests() throws Exception {
		WinningMgmtControllerImplSLE controllerImpl = WinningMgmtControllerImplSLE.getInstance();
		try {
			masterApprovalList = controllerImpl.getMasOrPendingRequests(approvalBean, "PND_PAY");
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return SUCCESS;
	}

	public String processPaymentRequests() throws Exception {
		WinningMgmtControllerImplSLE controllerImpl = WinningMgmtControllerImplSLE.getInstance();
		try {
			double taxAmount = approvalBean.getTaxAmount();
			double winAmount = approvalBean.getWinningAmount();
			double netAmount = approvalBean.getNetAmount();
			if(taxAmount > winAmount || taxAmount < 0) {
				taxAmount = 0.00;
				netAmount = winAmount;
			} else {
				netAmount = winAmount-taxAmount;
			}
			approvalBean.setTaxAmount(taxAmount);
			approvalBean.setNetAmount(netAmount);
			approvalBean.setWinningAmount(winAmount);

			boolean status = controllerImpl.processPaymentRequests(approvalBean, processType, getUserBean());
			logger.info("processMasterApproval - "+status);
		} catch (LMSException e) {
			logger.info("ErrorCode:" + e.getErrorCode() + " ErrorMessage:"+ e.getErrorMessage());
			request.setAttribute("LMS_EXCEPTION", e.getErrorMessage());
			return "applicationLMSAjaxException";
		}

		return SUCCESS;
	}
}