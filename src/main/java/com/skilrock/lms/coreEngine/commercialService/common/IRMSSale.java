package com.skilrock.lms.coreEngine.commercialService.common;

import com.skilrock.lms.beans.CSLoginBean;
import com.skilrock.lms.beans.CSSaleBean;
import com.skilrock.lms.beans.CSOrgBean;

//Generated by MyEclipse
public interface IRMSSale {
	public CSSaleBean saleOperation(String userName, int retOrgId, double balance, String prodCode, double unitPrice, int Multiple, double MrpAmt, double NetAmt, int CSRefTrxId,int RMSRefId, CSOrgBean orgBean,String AuthCode, String status, int ErrorCode);
	public CSSaleBean saleCancelOperation(int retOrgId, double balance, int CSRefTxIdForRefund, int RMSRefIdForRefund, int CSRefTxId, int RMSRefId,String AuthCode, String Status, int ErrorCode, String reasonForCancel);
	public CSLoginBean terminalLoginOperation(String userName, double balance);
	public CSLoginBean terminalReprintAuthOperation(String userName, int CSRefTxId, int RMSRefId, String authCode, String status, int errorCode);
}