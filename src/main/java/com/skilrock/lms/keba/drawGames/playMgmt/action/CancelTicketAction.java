package com.skilrock.lms.keba.drawGames.playMgmt.action;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;

import org.apache.struts2.ServletActionContext;

import com.skilrock.lms.ajax.AjaxRequestHelper;
import com.skilrock.lms.beans.UserInfoBean;
import com.skilrock.lms.common.BaseAction;
import com.skilrock.lms.coreEngine.drawGames.playMgmt.DrawGameRPOSHelper;
import com.skilrock.lms.dge.beans.CancelTicketBean;
import com.skilrock.lms.web.drawGames.common.Util;
import com.skilrock.lms.web.drawGames.common.UtilApplet;

public class CancelTicketAction extends BaseAction {
	private static final long serialVersionUID = 1L;

	public CancelTicketAction() {
		super(CancelTicketAction.class);
	}

	private long LSTktNo;
	private String requestData;

	public long getLSTktNo() {
		return LSTktNo;
	}

	public void setLSTktNo(long lSTktNo) {
		LSTktNo = lSTktNo;
	}

	public String getRequestData() {
		return requestData;
	}

	public void setRequestData(String requestData) {
		this.requestData = requestData;
	}

	@SuppressWarnings("unchecked")
	public void cancelTicket() throws Exception {

		ServletContext sc = ServletActionContext.getServletContext();
		JSONObject responseObject = new JSONObject();
		PrintWriter out = null;
		String ticketNumber = "";
		try {
			JSONObject canceltRequestData = (JSONObject) JSONSerializer.toJSON(request.getParameter("requestData"));
			logger.info("Cancel Request Data : " + canceltRequestData);

			response.setContentType("application/json");
			out = response.getWriter();

			String userName = canceltRequestData.getString("userName").trim();
			boolean autoCancel = Boolean.parseBoolean(canceltRequestData.getString("autoCancel").trim());
			String cancelType = canceltRequestData.getString("cancelType").trim();

			String isDraw = (String) sc.getAttribute("IS_DRAW");
			int barCodeCount = -1;
			int inpType = Integer.parseInt((String) sc.getAttribute("InpType"));
			if (isDraw.equalsIgnoreCase("NO")) {
				responseObject.put("isSuccess", false);
				responseObject.put("errorMsg", "Draw Game Not Available.");
			}

			Map currentUserSessionMap = (Map) sc.getAttribute("LOGGED_IN_USERS");

			HttpSession session = (HttpSession) currentUserSessionMap.get(userName);
			UserInfoBean userInfoBean = (UserInfoBean) session.getAttribute("USER_INFO");
			String refMerchantId = (String) sc.getAttribute("REF_MERCHANT_ID");

			String cancelChannel = "LMS_Terminal";
			CancelTicketBean cancelTicketBean = new CancelTicketBean();

			Map<Integer, Map<Integer, String>> drawIdTableMap = (Map<Integer, Map<Integer, String>>) sc.getAttribute("drawIdTableMap");

			cancelTicketBean.setDrawIdTableMap(drawIdTableMap);
			DrawGameRPOSHelper helper = new DrawGameRPOSHelper();

			cancelTicketBean.setCancelDuaraion("true".equalsIgnoreCase((String)sc.getAttribute("IS_CANCEL_DURATION")));
			cancelTicketBean.setCancelDuration(Integer.parseInt((String)sc.getAttribute("CANCEL_DURATION")));

			cancelTicketBean.setCancelType((String) sc.getAttribute("CANCEL_TYPE"));
			if ("LAST_SOLD_TICKET".equalsIgnoreCase((String) sc.getAttribute("CANCEL_TYPE")) || autoCancel) {
				ticketNumber = helper.getLastSoldTicketNO(userInfoBean,"TERMINAL");

				if (ticketNumber != null) {
					ticketNumber = ticketNumber + Util.getRpcAppenderForTickets(ticketNumber.length());
				} else {
					responseObject.put("isSuccess", false);
					responseObject.put("errorMsg", "Invalid Ticket or Ticket Can Not be Cancelled.");
				}
	
				if (autoCancel) {
					if(ticketNumber.equals(String.valueOf(LSTktNo))) {
						responseObject.put("isSuccess", false);
						responseObject.put("errorMsg", "Last Request Could Not be Processed, Try Again.");
					}
				}
			} else if("TICKET_NO".equalsIgnoreCase((String) sc.getAttribute("CANCEL_TYPE"))) {
				if (inpType == 1 || (inpType == 3 && ticketNumber.length() == com.skilrock.lms.common.ConfigurationVariables.barcodeCount)) {
					barCodeCount = Integer.parseInt(Util.getBarCodeCountFromTicketNumber(cancelTicketBean.getTicketNo()));
				}

				ticketNumber = Util.getTicketNumber(ticketNumber, inpType);
			}

			if (ticketNumber == null && LSTktNo == 0) {
				responseObject.put("isSuccess", false);
				responseObject.put("errorMsg", "Invalid Ticket or Ticket Can Not be Cancelled.");
			} else {
				cancelTicketBean.setBarCodeCount(barCodeCount);
				cancelTicketBean.setTicketNo(ticketNumber);
				cancelTicketBean.setPartyId(userInfoBean.getUserOrgId());
				cancelTicketBean.setPartyType(userInfoBean.getUserType());
				cancelTicketBean.setUserId(userInfoBean.getUserId());
				cancelTicketBean.setCancelChannel(cancelChannel);
				cancelTicketBean.setRefMerchantId(refMerchantId);
				cancelTicketBean.setAutoCancel(autoCancel);
				cancelTicketBean = helper.cancelTicket(cancelTicketBean, userInfoBean, autoCancel, cancelType);

				AjaxRequestHelper ajxHelper = new AjaxRequestHelper();
				ajxHelper.getAvlblCreditAmt(userInfoBean);

				double balance = userInfoBean.getAvailableCreditLimit() - userInfoBean.getClaimableBal();

				if (cancelTicketBean.isValid()) {
					String advtMsg = "";
					if (cancelTicketBean.getRefundAmount() > 0) {
						StringBuilder topMsgsStr = new StringBuilder("");
						StringBuilder bottomMsgsStr = new StringBuilder("");
						UtilApplet.getAdvMsgs(cancelTicketBean.getAdvMsg(), topMsgsStr, bottomMsgsStr, 10);

						if (topMsgsStr.length() != 0) {
							advtMsg = "topAdvMsg:" + topMsgsStr + "|";
						}

						if (bottomMsgsStr.length() != 0) {
							advtMsg = advtMsg + "bottomAdvMsg:" + bottomMsgsStr
							+ "|";
						}
					}

					responseObject.put("isSuccess", true);
					responseObject.put("errorMsg", "");
					responseObject.put("refundAmount", cancelTicketBean.getRefundAmount());
					responseObject.put("ticketNumber", Util.getTktWithoutRpcNBarCodeCount(cancelTicketBean.getTicketNo(), cancelTicketBean.getTicketNo().length()) + cancelTicketBean.getReprintCount());
					responseObject.put("balance", balance);
					/*
					finalCancelData = "RfdA:"
						+ cancelTicketBean.getRefundAmount()
						+ "|TktN:"
						+ Util.getTktWithoutRpcNBarCodeCount(cancelTicketBean.getTicketNo(), cancelTicketBean.getTicketNo().length())
								+ cancelTicketBean.getReprintCount() + "|Balance:"
								+ balance + "|" + advtMsg;
					*/
				} else if (cancelTicketBean.isError()) {
					responseObject.put("isSuccess", false);
					responseObject.put("errorMsg", cancelTicketBean.getErrMsg());
				} else if(autoCancel) {
					responseObject.put("isSuccess", false);
					responseObject.put("errorMsg", "Last Request Could Not be Processed, Try Again.");
				} else {
					responseObject.put("isSuccess", false);
					responseObject.put("errorMsg", "Invalid Ticket or Ticket Can Not be Cancelled.");
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
			responseObject.put("errorMsg", "IOException Occured.");
			responseObject.put("isSuccess", false);
			return;
		} catch (Exception e) {
			e.printStackTrace();
			responseObject.put("errorMsg", "Exception Occured.");
			responseObject.put("isSuccess", false);
			return;
		} finally {
			if (responseObject.isEmpty()) {
				responseObject.put("errorMsg", "Compile Time Error.");
				responseObject.put("isSuccess", false);
			}
			logger.info("Cancel Response Data : " + responseObject);
			out.print(responseObject);
			out.flush();
			out.close();
		}
	}
}