package com.skilrock.lms.common.utility;

import java.text.DecimalFormat;
import java.text.NumberFormat;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public final class FormatNumber {
	static Log logger = LogFactory.getLog(FormatNumber.class);

	private static String strNumber = null;

	public static final String formatNumber(int number) {
		NumberFormat format = NumberFormat.getInstance();
		format.setMaximumFractionDigits(2);
		return strNumber = format.format(number);
	}

	public synchronized static final String formatNumber(Object number) {
		// logger.debug("number " +number );
		Object fnumber;
		if (number == null) {
			return "0";
		}
		if (number instanceof String) {
			String num = ((String) number).replace(",", "");
			fnumber = Double.parseDouble(num);
		} else {
			fnumber = number;
		}
		NumberFormat format = NumberFormat.getInstance();
		format.setMaximumFractionDigits(2);
		strNumber = format.format(fnumber);
		// logger.debug();
		strNumber = strNumber.replace(",", "");
		// logger.debug("cvfxdfv" + strNumber);
		if (strNumber.indexOf(".") == -1) {
			strNumber = strNumber + ".00";
		} else {
			String newNo = strNumber.substring(strNumber.indexOf(".") + 1);
			logger.debug(newNo);
			if (newNo.length() < 2) {
				strNumber = strNumber + "0";
			}

		}
		return strNumber;
	}

	public synchronized static final String formatNumberForJSP(Object number) {
		Object fnumber;
		if (number == null) {
			return "0.00";
		}
		if (number instanceof String) {
			String num = ((String) number).replace(",", "");
			fnumber = Double.parseDouble(num);
		} else {
			fnumber = number;
		}
		NumberFormat format = NumberFormat.getInstance();
		format.setMaximumFractionDigits(2);
		strNumber = format.format(fnumber);
		if (strNumber.indexOf(".") == -1) {
			strNumber = strNumber + ".00";
		} else {
			String newNo = strNumber.substring(strNumber.indexOf(".") + 1);
			if (newNo.length() < 2) {
				strNumber = strNumber + "0";
			}
		}
		// logger.debug(strNumber);
		return strNumber;
	}

	public synchronized static final String formatNumberForJSPWithoutZero(
			Object number) {
		Object fnumber;
		if (number == null) {
			return "0";
		}
		if (number instanceof String) {
			String num = ((String) number).replace(",", "");
			fnumber = Double.parseDouble(num);
		} else {
			fnumber = number;
		}
		NumberFormat format = NumberFormat.getInstance();
		format.setMaximumFractionDigits(0);
		strNumber = format.format(fnumber);
		return strNumber;
	}

	public synchronized static final String formatPDFNumbers(double number) {
		NumberFormat format = NumberFormat.getInstance();
		format.setMaximumFractionDigits(2);
		strNumber = format.format(number);
		strNumber = strNumber.replace(",", "");

		if (strNumber.indexOf(".") == -1) {
			strNumber = strNumber + ".00";
		} else {
			String newNo = strNumber.substring(strNumber.indexOf(".") + 1);
			logger.debug(newNo);
			if (newNo.length() < 2) {
				strNumber = strNumber + "0";
			}

		}
		return strNumber;
	}
	
	public synchronized static final String formatToTwoDecimal(Object number){
		
		return number== null ?"0.00":new DecimalFormat("#0.00").format(number);
		
	}

	public static void main(String[] args) {
		logger.debug(formatNumberForJSPWithoutZero(123.9));
	}
}
