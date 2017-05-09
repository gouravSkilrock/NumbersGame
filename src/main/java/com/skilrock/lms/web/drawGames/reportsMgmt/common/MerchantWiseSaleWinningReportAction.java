package com.skilrock.lms.web.drawGames.reportsMgmt.common;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.opensymphony.xwork2.ActionSupport;
import com.skilrock.lms.coreEngine.drawGames.reportMgmt.MerchatWiseSaleWinningReportHelper;
import com.skilrock.lms.dge.beans.MerchantWiseSaleWinningBean;

public class MerchantWiseSaleWinningReportAction extends ActionSupport {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	Log logger = LogFactory.getLog(MerchantWiseSaleWinningReportAction.class);
	private String start_date;
	private String end_Date;
	private MerchantWiseSaleWinningBean merchantwiseSaleWinningBean;

	public String getStart_date() {
		return start_date;
	}

	public void setStart_date(String startDate) {
		start_date = startDate;
	}

	public String getEnd_Date() {
		return end_Date;
	}

	public void setEnd_Date(String endDate) {
		end_Date = endDate;
	}

	public void setMerchantwiseSaleWinningBean(
			MerchantWiseSaleWinningBean merchantwiseSaleWinningBean) {
		this.merchantwiseSaleWinningBean = merchantwiseSaleWinningBean;
	}

	public MerchantWiseSaleWinningBean getMerchantwiseSaleWinningBean() {
		return merchantwiseSaleWinningBean;
	}

	public String fetchSaleWinningData() {
		MerchatWiseSaleWinningReportHelper helper = null;
		try {
			helper = new MerchatWiseSaleWinningReportHelper();
			merchantwiseSaleWinningBean = helper.fetchSaleWinningData(
					start_date + " 00:00:00", end_Date + " 23:59:59");
		} catch (Exception e) {
			e.printStackTrace();
			return ERROR;
		}
		return SUCCESS;
	}
	public String fetchMtnSaleWinningData() {
		MerchatWiseSaleWinningReportHelper helper = null;
		try {
			helper = new MerchatWiseSaleWinningReportHelper();
			merchantwiseSaleWinningBean = helper.fetchMtnSaleWinningData(
					start_date + " 00:00:00", end_Date + " 23:59:59");
			 
			
		} catch (Exception e) {
			e.printStackTrace();
			return ERROR;
		}
		return SUCCESS;
	}
}