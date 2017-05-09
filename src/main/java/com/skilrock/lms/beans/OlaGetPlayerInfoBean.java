package com.skilrock.lms.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class OlaGetPlayerInfoBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String messageId;
	private String errorCode;
	private String errorText;
	List<OlaGetPlayerBindingInfoBean> playerDataMap = null;
	public List<OlaGetPlayerBindingInfoBean> getPlayerDataMap() {
		return playerDataMap;
	}

	public void setPlayerDataMap(OlaGetPlayerBindingInfoBean bean1) {
		if (playerDataMap == null) {
			playerDataMap = new ArrayList<OlaGetPlayerBindingInfoBean>();
		}
		playerDataMap.add(bean1);

	}

	public String getMessageId() {
		return messageId;
	}

	public void setMessageId(String messageId) {
		this.messageId = messageId;
	}

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	public String getErrorText() {
		return errorText;
	}

	public void setErrorText(String errorText) {
		this.errorText = errorText;
	}
	
}
