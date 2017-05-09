package com.skilrock.lms.embedded.accMgmt.common;

import java.io.IOException;
import java.util.List;

import com.opensymphony.xwork2.ModelDriven;
import com.skilrock.lms.beans.UserInfoBean;
import com.skilrock.lms.common.BaseAction;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.coreEngine.accMgmt.javaBeans.BankDepositBean;
import com.skilrock.lms.coreEngine.accMgmt.serviceImpl.BankDepositServiceImpl;
import com.skilrock.lms.embedded.drawGames.common.EmbeddedErrors;
import com.skilrock.lms.web.bankMgmt.beans.BankDetailsBean;
import com.skilrock.lms.web.drawGames.common.Util;

public class RetailerBankDepositAction extends BaseAction implements ModelDriven<BankDepositBean> {
	private static final long serialVersionUID = 1L;

	public RetailerBankDepositAction() {
		super(RetailerBankDepositAction.class);
	}

	private BankDepositBean depositBean;

	public BankDepositBean getDepositBean() {
		return depositBean;
	}

	public void setDepositBean(BankDepositBean depositBean) {
		this.depositBean = depositBean;
	}

	@Override
	public BankDepositBean getModel() {
		depositBean = new BankDepositBean();
		return depositBean;
	}

	public void getBankDetails() throws LMSException, IOException {
		List<BankDetailsBean> bankDetailList = BankDepositServiceImpl.getInstance().getBankDetails();
		StringBuilder responseBuilder = new StringBuilder("BankDetails:");
		for(BankDetailsBean detailBean : bankDetailList) {
			responseBuilder.append(detailBean.getBankId()).append("_").append(detailBean.getBankFullName()).append("_").append(detailBean.getAccountNo()).append("_").append(detailBean.getDescription()).append("~");
		}

		if(bankDetailList.size() > 0)
			responseBuilder.deleteCharAt(responseBuilder.length() - 1);
		else
			responseBuilder = new StringBuilder("ErrorMsg:" + EmbeddedErrors.NO_BANK_EXIST_ERROR_MESSAGE + "ErrorCode:" + EmbeddedErrors.NO_BANK_EXIST_ERROR_CODE+"|");

		logger.info("getBankList Response Data - "+responseBuilder.toString());

		response.getOutputStream().write(responseBuilder.toString().getBytes());
	}

	public void notifyBankDeposit() throws Exception {
		logger.info("Request Data - "+depositBean);

		UserInfoBean userBean = getUserBean(depositBean.getUserName());
		depositBean.setUserId(userBean.getUserId());
		depositBean.setOrganizationId(userBean.getUserOrgId());
		depositBean.setBranchName("NA");
		depositBean.setDate(Util.changeFormat("dd/MM/yyyy", "yyyy-MM-dd", depositBean.getDate()));
		boolean status = BankDepositServiceImpl.getInstance().notifyBankDeposit(depositBean);

		String message = (status) ? "Deposit Successfully" : "Deposit Failed";
		response.getOutputStream().write(message.getBytes());
	}

	public void getLastRecords() throws Exception {
		UserInfoBean userBean = getUserBean(depositBean.getUserName());
		List<BankDepositBean> depositList = BankDepositServiceImpl.getInstance().getLastRecords(userBean.getUserId(), 3);
		StringBuilder responseBuilder = new StringBuilder("LastRecords:");
		for(BankDepositBean depositBean : depositList) {
			responseBuilder.append(depositBean.getReceiptNo()).append("_").append(depositBean.getBankName()).append("_").append(depositBean.getAmount()).append("_").append(depositBean.getRequestDate()).append("_").append(depositBean.getStatus()).append("~");
		}

		if(depositList.size() > 0)
			responseBuilder.deleteCharAt(responseBuilder.length() - 1);
		else
			responseBuilder = new StringBuilder("ErrorMsg:" + EmbeddedErrors.NO_RECORD_EXIST_ERROR_MESSAGE + "ErrorCode:" + EmbeddedErrors.NO_RECORD_EXIST_ERROR_CODE+"|");

		logger.info("Last Records Response Data - "+responseBuilder.toString());

		response.getOutputStream().write(responseBuilder.toString().getBytes());
	}
}