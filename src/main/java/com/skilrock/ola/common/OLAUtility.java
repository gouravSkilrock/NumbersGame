package com.skilrock.ola.common;


import java.util.Iterator;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.skilrock.ola.commonMethods.daoImpl.OlaCommonMethodDaoImpl;
import com.skilrock.ola.javaBeans.OlaWalletBean;

public class OLAUtility {
	static Log  logger = LogFactory.getLog(OLAUtility.class);
	
	private static Map<Integer, OlaWalletBean> walletDataMap=null;
	
	static{
		OlaCommonMethodDaoImpl commonDao=new OlaCommonMethodDaoImpl();
		walletDataMap=commonDao.olaWalletDetails();
	}
	
	public static String getVerificationType(int walletId){
		return walletDataMap.get(walletId).getVerificationType();
	}
	
	public static String getVerificationType(String walletDevName) {
		String verificationType = null;
		Iterator<Map.Entry<Integer, OlaWalletBean>> walletIdIter = walletDataMap
				.entrySet().iterator();
		while (walletIdIter.hasNext()) {
			Map.Entry<Integer, OlaWalletBean> walletIdIterVal = walletIdIter
					.next();
			if (walletDevName.equals(walletIdIterVal.getValue()
					.getWalletDevName())) {
				verificationType = walletIdIterVal.getValue()
						.getVerificationType();
			}
		}
		return verificationType;

	}
	
	public static int getWalletId(String walletDevName) {
		int walletId = 0;
		Iterator<Map.Entry<Integer, OlaWalletBean>> walletIdIter = walletDataMap
				.entrySet().iterator();
		while (walletIdIter.hasNext()) {
			Map.Entry<Integer, OlaWalletBean> walletIdIterVal = walletIdIter
					.next();
			if (walletDevName.equals(walletIdIterVal.getValue()
					.getWalletDevName())) {
				walletId = walletIdIterVal.getValue()
						.getWalletId();
			}
		}
		return walletId;

	}
	
	public static String getWalletName(int walletId) {
		return walletDataMap.get(walletId).getWalletDevName();
	}
	
	
	public static Map<Integer,OlaWalletBean> getOlaWalletDataMap(){
		return walletDataMap;	
	}

}
