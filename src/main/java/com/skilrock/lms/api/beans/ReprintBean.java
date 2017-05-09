package com.skilrock.lms.api.beans;

import java.io.Serializable;

public class ReprintBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private TPKenoPurchaseBean kenoBean;
	private TPLottoPurchaseBean lottoBean;
	private boolean isSuccess;
	private String errorCode;

	private PWTApiBean pwtBean;
	private CancelBean cancelBean;

	public PWTApiBean getPwtBean() {
		return pwtBean;
	}

	public void setPwtBean(PWTApiBean pwtBean) {
		this.pwtBean = pwtBean;
	}

	public CancelBean getCancelBean() {
		return cancelBean;
	}

	public void setCancelBean(CancelBean cancelBean) {
		this.cancelBean = cancelBean;
	}

	public TPKenoPurchaseBean getKenoBean() {
		return kenoBean;
	}

	public TPLottoPurchaseBean getLottoBean() {
		return lottoBean;
	}

	public void setKenoBean(TPKenoPurchaseBean kenoBean) {
		this.kenoBean = kenoBean;
	}

	public void setLottoBean(TPLottoPurchaseBean lottoBean) {
		this.lottoBean = lottoBean;
	}

	public boolean isSuccess() {
		return isSuccess;
	}

	public void setSuccess(boolean isSuccess) {
		this.isSuccess = isSuccess;
	}

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

}
