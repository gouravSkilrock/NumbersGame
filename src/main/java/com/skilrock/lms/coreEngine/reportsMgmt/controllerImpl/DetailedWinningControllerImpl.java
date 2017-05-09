package com.skilrock.lms.coreEngine.reportsMgmt.controllerImpl;

import java.sql.Connection;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.sf.json.JSONObject;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.skilrock.lms.beans.ServiceRequest;
import com.skilrock.lms.common.db.DBConnect;
import com.skilrock.lms.common.exception.LMSErrors;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.coreEngine.reportsMgmt.daoImpl.DetailedWinningPaymentDaoImpl;
import com.skilrock.lms.coreEngine.reportsMgmt.javaBeans.DetailedPaymentTransactionalBean;
import com.skilrock.lms.coreEngine.service.IServiceDelegate;
import com.skilrock.lms.coreEngine.service.ServiceDelegate;
import com.skilrock.lms.coreEngine.service.ServiceDelegateSLE;
import com.skilrock.lms.coreEngine.service.dge.ServiceMethodName;
import com.skilrock.lms.coreEngine.service.dge.ServiceName;
import com.skilrock.lms.web.drawGames.common.Util;

public class DetailedWinningControllerImpl {
	final static Log logger = LogFactory.getLog(DetailedWinningControllerImpl.class);

	private static DetailedWinningControllerImpl instance;

	private DetailedWinningControllerImpl() {
	}

	public static DetailedWinningControllerImpl getInstance() {
		if (instance == null) {
			synchronized (DetailedWinningControllerImpl.class) {
				if (instance == null) {
					instance = new DetailedWinningControllerImpl();
				}
			}
		}
		return instance;
	}

	@SuppressWarnings("unchecked")
	public List<DetailedPaymentTransactionalBean> fetchData(String serviceCode, Timestamp startTime, int gameId, Timestamp endTime, String reportType, String detailType, int agentOrgId, int retOrgId, int gameTypeId) throws LMSException {
		ServiceRequest sReq = new ServiceRequest();
		String sResp = null;
		IServiceDelegate delegate = null;
		JSONObject reqObj = new JSONObject();
		Connection con = null;
		SimpleDateFormat dateFormat = null;
		List<DetailedPaymentTransactionalBean> reportData = null;
		Set<Integer> retailerSet = null;
		Map<Long, String> retailerOrgCodeMap = null;

		if ("DG".equals(serviceCode))
			delegate = ServiceDelegate.getInstance();
		else if ("SLE".equals(serviceCode))
			delegate = ServiceDelegateSLE.getInstance();

		try {
			dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			sReq.setServiceName(ServiceName.REPORTS_MGMT);
			sReq.setServiceMethod(ServiceMethodName.GET_DETAILED_WINNING_PAYMENT_REPORT);

			con = DBConnect.getConnection();
			if ("LMC".equalsIgnoreCase(detailType))
				retailerSet = DetailedWinningPaymentDaoImpl.getInstance().fetchRetailers(agentOrgId, con);
			if ("ALL".equalsIgnoreCase(reportType)) {
				retailerOrgCodeMap = DetailedWinningPaymentDaoImpl.getInstance().fetchRetailersOrgCode(con);

				if ("LMC".equalsIgnoreCase(detailType))
					reqObj.put("retList", retailerSet);
				else if ("RETAILER".equalsIgnoreCase(detailType))
					reqObj.put("retList", Util.fetchUserIdFormOrgId(retOrgId));
				reqObj.put("detailType", detailType);
				reqObj.put("startTime", dateFormat.format(startTime));
				reqObj.put("endTime", dateFormat.format(endTime));
				reqObj.put("gameId", gameId);

				if ("SLE".equals(serviceCode))
					reqObj.put("gameTypeId", gameTypeId);

				sReq.setServiceData(reqObj);
				sResp = delegate.getResponseString(sReq);

				// sResp="{\"isSuccess\":true,\"responseData\":[{\"eventId\":427,\"drawName\":\"Friday Bonanza\",\"userId\":\"27286\",\"ticketNbr\":\"381932005146170110\",\"partyId\":\"16018\",\"userType\":\"RETAILER\",\"saleDateTime\":\"2016-01-05 14:34:40.0\",\"saleAmt\":0.1,\"winAmt\":3.6,\"refTxnId\":\"68493079\"},{\"eventId\":427,\"drawName\":\"Friday Bonanza\",\"userId\":\"27355\",\"ticketNbr\":\"391432005104530010\",\"partyId\":\"16072\",\"userType\":\"RETAILER\",\"saleDateTime\":\"2016-01-05 11:12:59.0\",\"saleAmt\":0.1,\"winAmt\":3.6,\"refTxnId\":\"68493014\"},{\"eventId\":427,\"drawName\":\"Friday Bonanza\",\"userId\":\"27351\",\"ticketNbr\":\"398432004248040010\",\"partyId\":\"16068\",\"userType\":\"RETAILER\",\"saleDateTime\":\"2016-01-04 12:50:26.0\",\"saleAmt\":0.1,\"winAmt\":3.6,\"refTxnId\":\"68492907\"},{\"eventId\":427,\"drawName\":\"Friday Bonanza\",\"userId\":\"27351\",\"ticketNbr\":\"401532001248040040\",\"partyId\":\"16068\",\"userType\":\"RETAILER\",\"saleDateTime\":\"2016-01-01 16:18:23.0\",\"saleAmt\":0.1,\"winAmt\":3.6,\"refTxnId\":\"68492658\"},{\"eventId\":427,\"drawName\":\"Friday Bonanza\",\"userId\":\"11003\",\"ticketNbr\":\"433932005188050100\",\"partyId\":\"3\",\"userType\":\"RETAILER\",\"saleDateTime\":\"2016-01-05 13:34:29.0\",\"saleAmt\":0.1,\"winAmt\":3.6,\"refTxnId\":\"68493073\"},{\"eventId\":427,\"drawName\":\"Friday Bonanza\",\"userId\":\"27286\",\"ticketNbr\":\"537832005146170070\",\"partyId\":\"16018\",\"userType\":\"RETAILER\",\"saleDateTime\":\"2016-01-05 14:23:33.0\",\"saleAmt\":0.1,\"winAmt\":3.6,\"refTxnId\":\"68493075\"}]}";
				if (sResp != null) {
					JsonObject data = new JsonParser().parse(sResp).getAsJsonObject();
					if ("true".equalsIgnoreCase(data.get("isSuccess").getAsString())) {
						reportData = (List<DetailedPaymentTransactionalBean>) new Gson().fromJson((JsonElement) data.get("responseData"), new TypeToken<List<DetailedPaymentTransactionalBean>>() {}.getType());
						Iterator<DetailedPaymentTransactionalBean> iterator = reportData.iterator();
						while (iterator.hasNext()) {
							String value = null;
							DetailedPaymentTransactionalBean bean = iterator.next();
							value = retailerOrgCodeMap.get(Long.valueOf(bean.getUserId()));
							bean.setRetOrgCode(value.split("-")[0]);
							bean.setAgtOrgCode(value.split("-")[1]);
							bean.setWinTransDate(DetailedWinningPaymentDaoImpl.getInstance().fetchWinTransDate(serviceCode, bean.getTicketNbr(), gameId, con));
						}
						return reportData;
					}
				}
			} else if ("TRANSACTIONAL".equalsIgnoreCase(reportType)) {
				reportData = DetailedWinningPaymentDaoImpl.getInstance().getReportData(serviceCode, detailType, retailerSet, gameId, startTime, endTime, retOrgId, con);
				return reportData;
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new LMSException(LMSErrors.GENERAL_EXCEPTION_ERROR_CODE, LMSErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		} finally {
			DBConnect.closeCon(con);
		}
		return null;
	}
}