package com.skilrock.lms.rest.common;

import com.skilrock.lms.web.drawGames.common.Util;

public class TransactionManager {
	private static final ThreadLocal<LocalThreadData> context = new ThreadLocal<LocalThreadData>();

	public static void startTransaction() {
		// logic to start a transaction
		// ...
		LocalThreadData localThread = new LocalThreadData();
		localThread.setAuditId(Util.getAuditTrailId());
		localThread.setAuditTime(System.currentTimeMillis());
		localThread.setActivityId(Util.getActivityId());
		context.set(localThread);
	}

	public static long getAuditId() {
		return context.get().getAuditId();
	}

	public static long getAuditTime() {
		return context.get().getAuditTime();
	}
	public static long getActivityId() {
		return context.get().getActivityId();
	}
	
	public static int getMerchantId() {
		return context.get().getMerchantId();
	}
	
	public static void setMerchantId(int merchantId) {
		 context.get().setMerchantId(merchantId);
	}
	public static void setResponseData(String responseData) {
		 context.get().setResponseData(responseData);
	}
	public static String getResponseData() {
		 return context.get().getResponseData();
	}
	
  public static ThreadLocal<LocalThreadData> getContext() {
		return context;
	}

public static void endTransaction() {
		
		context.remove();
	}
}