package com.skilrock.lms.web.userMgmt.common;

import java.io.PrintWriter;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.opensymphony.xwork2.ModelDriven;
import com.skilrock.lms.beans.UserInfoBean;
import com.skilrock.lms.common.BaseAction;
import com.skilrock.lms.common.exception.GenericException;
import com.skilrock.lms.common.utility.LMSUtility;
import com.skilrock.lms.coreEngine.instantWin.common.IWUtil;
import com.skilrock.lms.coreEngine.ola.common.OLAUtility;
import com.skilrock.lms.coreEngine.sportsLottery.common.SLEUtil;
import com.skilrock.lms.coreEngine.sportsLottery.common.controllerImpl.CommonMethodsControllerImpl;
import com.skilrock.lms.coreEngine.userMgmt.common.AdvMsgHelper;
import com.skilrock.lms.coreEngine.userMgmt.javaBeans.MessageInboxBean;
import com.skilrock.lms.coreEngine.userMgmt.serviceImpl.MessageInboxServiceImpl;
import com.skilrock.lms.web.drawGames.common.Util;
import com.skilrock.lms.web.reportsMgmt.common.ReportUtility;

public class MessageInboxAction extends BaseAction implements
		ModelDriven<MessageInboxBean> {
	private static final long serialVersionUID = 1L;

	private String orgType;
	private String interfaceType;
	private Map<String, String> userTypeMap;
	private int mode;
	private List<MessageInboxBean> messageList = null;
	private MessageInboxBean messageBean;
	private String[] retName;
	private String messages;

	/* Additional Variables for Add Messages for Player Start */
	private HashMap<Integer, String> commSerList;
	private HashMap<Integer, String> drawSerList;
	private Map<Integer, String> olaSerList;
	private Map<Integer, String> sleServiceMap;
	private Map<Integer, String> iwServiceMap;

	private String[] gameName;
	private String[] categoryName;
	private String[] walletName;
	private String[] gameNo;
	private String[] sleGameNo;
	private String[] iwGameNo;
	private String[] categoryNo;
	private String[] walletNo;
	private String serviceId;
	private String serviceType;
	private String message;
	private String msgLocation;
	private String activity;
	private String activity2;
	private String activity3;

	/* Additional Variables for Add Messages for Player End */

	public HashMap<Integer, String> getCommSerList() {
		return commSerList;
	}

	public void setCommSerList(HashMap<Integer, String> commSerList) {
		this.commSerList = commSerList;
	}

	public HashMap<Integer, String> getDrawSerList() {
		return drawSerList;
	}

	public void setDrawSerList(HashMap<Integer, String> drawSerList) {
		this.drawSerList = drawSerList;
	}

	public Map<Integer, String> getOlaSerList() {
		return olaSerList;
	}

	public void setOlaSerList(Map<Integer, String> olaSerList) {
		this.olaSerList = olaSerList;
	}

	public Map<Integer, String> getSleServiceMap() {
		return sleServiceMap;
	}

	public void setSleServiceMap(Map<Integer, String> sleServiceMap) {
		this.sleServiceMap = sleServiceMap;
	}

	public String getOrgType() {
		return orgType;
	}

	public void setOrgType(String orgType) {
		this.orgType = orgType;
	}

	public String getInterfaceType() {
		return interfaceType;
	}

	public void setInterfaceType(String interfaceType) {
		this.interfaceType = interfaceType;
	}

	public Map<String, String> getUserTypeMap() {
		return userTypeMap;
	}

	public void setUserTypeMap(Map<String, String> userTypeMap) {
		this.userTypeMap = userTypeMap;
	}

	public int getMode() {
		return mode;
	}

	public void setMode(int mode) {
		this.mode = mode;
	}

	public List<MessageInboxBean> getMessageList() {
		return messageList;
	}

	public void setMessageList(List<MessageInboxBean> messageList) {
		this.messageList = messageList;
	}

	public MessageInboxBean getMessageBean() {
		return messageBean;
	}

	public void setMessageBean(MessageInboxBean messageBean) {
		this.messageBean = messageBean;
	}

	public String[] getRetName() {
		return retName;
	}

	public void setRetName(String[] retName) {
		this.retName = retName;
	}

	public String getMessages() {
		return messages;
	}

	public void setMessages(String messages) {
		this.messages = messages;
	}

	public String[] getGameName() {
		return gameName;
	}

	public void setGameName(String[] gameName) {
		this.gameName = gameName;
	}

	public String[] getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String[] categoryName) {
		this.categoryName = categoryName;
	}

	public String[] getWalletName() {
		return walletName;
	}

	public void setWalletName(String[] walletName) {
		this.walletName = walletName;
	}

	public String[] getGameNo() {
		return gameNo;
	}

	public void setGameNo(String[] gameNo) {
		this.gameNo = gameNo;
	}

	public String[] getSleGameNo() {
		return sleGameNo;
	}

	public void setSleGameNo(String[] sleGameNo) {
		this.sleGameNo = sleGameNo;
	}

	public String[] getCategoryNo() {
		return categoryNo;
	}

	public void setCategoryNo(String[] categoryNo) {
		this.categoryNo = categoryNo;
	}

	public String[] getWalletNo() {
		return walletNo;
	}

	public void setWalletNo(String[] walletNo) {
		this.walletNo = walletNo;
	}

	public String getServiceId() {
		return serviceId;
	}

	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}

	public String getServiceType() {
		return serviceType;
	}

	public void setServiceType(String serviceType) {
		this.serviceType = serviceType;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getMsgLocation() {
		return msgLocation;
	}

	public void setMsgLocation(String msgLocation) {
		this.msgLocation = msgLocation;
	}

	public String getActivity() {
		return activity;
	}

	public void setActivity(String activity) {
		this.activity = activity;
	}

	public String getActivity2() {
		return activity2;
	}

	public void setActivity2(String activity2) {
		this.activity2 = activity2;
	}

	public String getActivity3() {
		return activity3;
	}

	public void setActivity3(String activity3) {
		this.activity3 = activity3;
	}

	public String[] getIwGameNo() {
		return iwGameNo;
	}

	public void setIwGameNo(String[] iwGameNo) {
		this.iwGameNo = iwGameNo;
	}
	
	public Map<Integer, String> getIwServiceMap() {
		return iwServiceMap;
	}

	public void setIwServiceMap(Map<Integer, String> iwServiceMap) {
		this.iwServiceMap = iwServiceMap;
	}

	@Override
	public MessageInboxBean getModel() {
		messageBean = new MessageInboxBean();
		return messageBean;
	}

	public MessageInboxAction() {
		super(MessageInboxAction.class);
	}

	public String addNewMessageMenu() {
		try {
			commSerList = ReportUtility.fetchActiveCategoriesCommSerData();
			drawSerList = ReportUtility.fetchActiveGameDrawDataMenu();
			olaSerList = OLAUtility.getOlaWalletDataMap();
			sleServiceMap = CommonMethodsControllerImpl.getInstance().getSLEGameMapForAdvMessage();
			iwServiceMap = com.skilrock.lms.coreEngine.instantWin.common.controllerImpl.CommonMethodsControllerImpl.getInstance().getIWGameMapForAdvMessage();

			userTypeMap = new LinkedHashMap<String, String>();

			List<String> userTypeList = MessageInboxServiceImpl.getSingleInstance().getActiveUserType();
			if(userTypeList.contains("BO"))
				userTypeMap.put("BO", getText("BOUser"));
			if(userTypeList.contains("AGENT"))
				userTypeMap.put("AGENT", getText("AGENT"));
			if(userTypeList.contains("RETAILER"))
				userTypeMap.put("RETAILER", getText("Retailer"));

			userTypeMap.put("PLAYER", getText("label.plr"));
		} catch (GenericException e) {
			e.printStackTrace();
			// return
			// Response.status(e.getErrorCode()).entity(LMSErrorProperty.getPropertyValue(e.getErrorCode())).build();
		}

		return SUCCESS;
	}

	public String getInterfaceList() {
		PrintWriter out = null;
		List<String> interfaceList = null;
		JSONObject jsonObject = new JSONObject();
		try {
			interfaceList = MessageInboxServiceImpl.getSingleInstance()
					.getActiveInterfaceType(orgType);
			out = response.getWriter();
			response.setContentType("application/json");
			// out.write(new Gson().toJson(interfaceList));
			jsonObject.put("isSuccess", true);
			jsonObject.put("responseData", interfaceList);
		} catch (Exception ex) {
			ex.printStackTrace();
			jsonObject.put("isSuccess", false);
			jsonObject.put("errorMsg", getText("msg.some.error"));
		} finally {
			out.print(jsonObject);
			out.flush();
			out.close();
		}

		return SUCCESS;
	}

	public String getModeList() {
		PrintWriter out = null;
		Map<Integer, String> modeMap = null;
		JSONObject jsonObject = new JSONObject();
		try {
			modeMap = MessageInboxServiceImpl.getSingleInstance()
					.getActiveModes(orgType, interfaceType);
			out = response.getWriter();
			response.setContentType("application/json");
			// out.write(new Gson().toJson(modeMap));
			jsonObject.put("isSuccess", true);
			jsonObject.put("responseData", modeMap);
		} catch (Exception ex) {
			ex.printStackTrace();
			jsonObject.put("isSuccess", false);
			jsonObject.put("errorMsg", getText("msg.some.error"));
		} finally {
			out.print(jsonObject);
			out.flush();
			out.close();
		}

		return SUCCESS;
	}

	@SuppressWarnings("unchecked")
	public String addNewMessageSubmit() throws Exception {
		UserInfoBean userBean = null;
		String result = "error";
		try {
			userBean = getUserBean();
			if ("PLAYER".equalsIgnoreCase(orgType)) {
				int srvcId = 0;
				AdvMsgHelper helper = new AdvMsgHelper();

				message = message.replaceAll("_", "").replaceAll(",", "");

				srvcId = ((HashMap<String, Integer>) LMSUtility.sc
						.getAttribute("SERVICES_CODE_ID_MAP")).get(serviceId);
				if ("DG".equalsIgnoreCase(serviceType)) {
					result = helper.saveAdvMessageData(orgType, gameNo,
							retName, message, userBean.getUserId(),
							msgLocation, activity, srvcId);
					gameName = new String[gameNo.length];
					for (int i = 0; i < gameNo.length; i++) {
						gameName[i] = Util.getGameDisplayName(Integer
								.parseInt(gameNo[i]));
					}
					setGameName(gameName);
				} else if ("CS".equalsIgnoreCase(serviceType)) {
					categoryName = new String[categoryNo.length];
					result = helper.saveAdvMessageData(orgType, categoryNo,
							retName, message, userBean.getUserId(),
							msgLocation, activity2, srvcId);
					for (int i = 0; i < categoryNo.length; i++) {
						categoryName[i] = Util.getCategoryName(Integer
								.parseInt(categoryNo[i]));
					}
					setCategoryName(categoryName);
				} else if ("OLA".equalsIgnoreCase(serviceType)) {
					walletName = new String[walletNo.length];
					result = helper.saveAdvMessageData(orgType, walletNo,
							retName, message, userBean.getUserId(),
							msgLocation, activity3, srvcId);
					for (int i = 0; i < walletNo.length; i++) {
						walletName[i] = OLAUtility.getWalletName(Integer
								.parseInt(walletNo[i]));
					}
					setWalletName(walletName);
				} else if ("SLE".equalsIgnoreCase(serviceType)) {
					result = helper.saveAdvMessageData(orgType, sleGameNo,
							retName, message, userBean.getUserId(),
							msgLocation, activity, srvcId);
					gameName = new String[sleGameNo.length];
					for (int i = 0; i < sleGameNo.length; i++) {
						gameName[i] = SLEUtil.gameTypeInfoMap.get(Integer.parseInt(sleGameNo[i])).getGameTypeDispName();
					}
					setGameName(gameName);

					SLEUtil.advMessageMap = CommonMethodsControllerImpl.getInstance().getSLEAdvMessageMap();
				} else if ("IW".equalsIgnoreCase(serviceType)) {
					result = helper.saveAdvMessageData(orgType, iwGameNo, retName, message, userBean.getUserId(), msgLocation, activity, srvcId);
					gameName = new String[iwGameNo.length];
					for (int i = 0; i < iwGameNo.length; i++) {
						gameName[i] = IWUtil.gameInfoMap.get(Integer.parseInt(iwGameNo[i])).getGameDispName();
					}
					setGameName(gameName);

					IWUtil.advMessageMap = com.skilrock.lms.coreEngine.instantWin.common.controllerImpl.CommonMethodsControllerImpl.getInstance().getIWAdvMessageMap();
				}
			} else {
				// AdvMsgHelper helper = new AdvMsgHelper();
				// result = helper.saveAdvMessageDataForRetailer(orgType,
				// agtName, retName, message, userInfoBean.getUserId(), "NA",
				// activity1);
				if ("TERMINAL".equals(interfaceType)) {
					messageBean.setMessageSubject(messageBean.getMessageSubject()
								.replaceAll("[\n\r]", "")
								.replaceAll("~", "")
								.replaceAll("%", "")
								.replaceAll("|", "")
								.replaceAll("_", "")
								.replaceAll(",", "")
								.replaceAll("#", "")
								.replaceAll("( )+", " ").trim());
				}

				Timestamp expiryPeriod = null;
				if (("FLASH".equals(messageBean.getMessageType()) || "RETAILER"
						.equals(orgType))) {
					SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
							"yyyy-MM-dd HH:mm:ss");
					expiryPeriod = new Timestamp(simpleDateFormat.parse(
							messageBean.getExpiryDate() + " "
									+ messageBean.getExpHr() + ":"
									+ messageBean.getExpMin() + ":"
									+ messageBean.getExpSec()).getTime());
				}
				messageBean.setExpiryPeriod(expiryPeriod);
				messageBean.setMessageTypeId(mode);
				messageBean.setMessageFor(orgType);
				messageBean.setUserId(userBean.getUserId());
				MessageInboxServiceImpl.getSingleInstance().addNewMessage(
						messageBean, retName);

				result = "SUCCESS_BAR";
			}

			// if(!"REGISTRATION".equals(messageBean.getMessageType())) {
			if (!"ALL".equals(messageBean.getUserSelection())) {
				if (!Arrays.asList(retName).contains("-1")) {
					for (int i = 0; i < retName.length; i++) {
						retName[i] = retName[i].split("~")[1];
					}
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return result;
	}

	public String fetchWebMessages() throws Exception {
		UserInfoBean userBean = null;
		List<String> statusList = null;
		JsonArray statusArray = null;
		JsonObject statusObject = null;

		int noOfMessages = 0;
		int messageCount = 0;
		int index = 0;
		JSONArray jsonArray = null;
		JSONObject jsonObject = null;
		JSONObject responseObject = new JSONObject();
		PrintWriter out = null;
		try {
			userBean = getUserBean();
			statusArray = new JsonParser().parse(messages).getAsJsonArray();
			statusList = new ArrayList<String>();
			for (int i = 0; i < statusArray.size(); i++) {
				statusObject = statusArray.get(i).getAsJsonObject();
				statusList.add("'" + statusObject.get("status").getAsString()
						+ "'");
			}

			messageList = MessageInboxServiceImpl.getSingleInstance()
					.fetchWebMessages(userBean.getUserId(),
							userBean.getUserType(), statusList,
							messageBean.getMessageNumber());
			noOfMessages = MessageInboxServiceImpl.getSingleInstance()
					.getTotalNoOfMessages(userBean.getUserId(),
							userBean.getUserType(), statusList);
			messageCount = MessageInboxServiceImpl.getSingleInstance()
					.getMessageCount("INBOX", userBean.getUserType(), "WEB");
			// messageList =
			// MessageInboxServiceImpl.getSingleInstance().fetchWebMessages(11067,
			// "BO", statusList, messageBean.getMessageNumber());
			// noOfMessages =
			// MessageInboxServiceImpl.getSingleInstance().getTotalNoOfMessages(11067,
			// "BO", statusList);
			// messageCount =
			// MessageInboxServiceImpl.getSingleInstance().getMessageCount("INBOX",
			// "BO", "WEB");

			jsonArray = new JSONArray();
			for (MessageInboxBean bean : messageList) {
				jsonObject = new JSONObject();
				jsonObject.put("messageId", bean.getMessageId());
				jsonObject.put("creatorUserName", bean.getCreatorUserName());
				String content = bean.getMessageSubject();
				if (content.length() > 20) {
					index = content.lastIndexOf(" ", 20);
					if (index == -1) {
						index = 20;
					}
					content = content.substring(0, index);
				}
				jsonObject.put("messageSubject", content);
				content = bean.getMessageContent();
				if (content.length() > 30) {
					index = content.lastIndexOf(" ", 30);
					if (index == -1) {
						index = 30;
					}
					content = content.substring(0, index);
				}
				jsonObject.put("messageContent", content);
				jsonObject.put("messageDate", bean.getMessageDate());
				jsonObject.put("mandatoryStatus", bean.getMandatoryStatus());
				jsonObject.put("status", bean.getStatus());
				jsonArray.add(jsonObject);
			}

			jsonObject = new JSONObject();
			jsonObject.put("noOfMessages", noOfMessages);
			jsonObject.put("messageCount", messageCount);
			jsonObject.put("messageList", jsonArray);
			out = response.getWriter();
			response.setContentType("text/html");
			responseObject.put("isSuccess", true);
			responseObject.put("responseData", jsonObject);
		} catch (Exception ex) {
			ex.printStackTrace();
			responseObject.put("isSuccess", false);
			responseObject.put("errorMsg", getText("msg.some.error"));
		} finally {
			out.print(responseObject);
			out.flush();
			out.close();
		}

		return SUCCESS;
	}

	public String fetchWebMessageDetail() throws Exception {
		UserInfoBean userBean = null;
		List<Integer> messageList = null;
		JSONObject jsonObject = null;
		JSONObject responseObject = new JSONObject();
		PrintWriter out = null;
		try {
			userBean = getUserBean();
			if ("UNREAD".equals(messageBean.getStatus())) {
				messageList = new ArrayList<Integer>();
				messageList.add(messageBean.getMessageId());
				MessageInboxServiceImpl.getSingleInstance()
						.updateUserMessageStatus(messageList, "READ");
			}
			messageBean = MessageInboxServiceImpl.getSingleInstance()
					.fetchWebMessageDetail(messageBean.getMessageId(),
							userBean.getUserId());
			// messageBean =
			// MessageInboxServiceImpl.getSingleInstance().fetchWebMessageDetail(messageBean.getMessageId(),
			// 11067);

			jsonObject = new JSONObject();
			jsonObject.put("messageId", messageBean.getMessageId());
			jsonObject.put("messageSubject", messageBean.getMessageSubject());
			jsonObject.put("messageContent", messageBean.getMessageContent());
			jsonObject.put("messageDate", messageBean.getMessageDate());
			jsonObject.put("mandatoryStatus", messageBean.getMandatoryStatus());
			jsonObject.put("creatorUserName", messageBean.getCreatorUserName());

			out = response.getWriter();
			response.setContentType("text/html");
			responseObject.put("isSuccess", true);
			responseObject.put("responseData", jsonObject);
		} catch (Exception ex) {
			ex.printStackTrace();
			responseObject.put("isSuccess", false);
			responseObject.put("errorMsg", getText("msg.some.error"));
		} finally {
			out.print(responseObject);
			out.flush();
			out.close();
		}

		return SUCCESS;
	}

	public String searchWebMessages() throws Exception {
		UserInfoBean userBean = null;

		JSONArray jsonArray = null;
		JSONObject jsonObject = null;
		JSONObject responseObject = new JSONObject();
		int index = 0;
		PrintWriter out = null;
		try {
			userBean = getUserBean();
			messageList = MessageInboxServiceImpl.getSingleInstance()
					.searchWebMessages(userBean.getUserId(),
							userBean.getUserType(), messages);
			// messageList =
			// MessageInboxServiceImpl.getSingleInstance().searchWebMessages(11067,
			// "BO", messages);

			jsonArray = new JSONArray();
			for (MessageInboxBean bean : messageList) {
				jsonObject = new JSONObject();
				jsonObject.put("messageId", bean.getMessageId());
				jsonObject.put("creatorUserName", bean.getCreatorUserName());
				String content = bean.getMessageSubject();
				if (content.length() > 20) {
					index = content.lastIndexOf(" ", 20);
					if (index == -1) {
						index = 20;
					}
					content = content.substring(0, index);
				}
				jsonObject.put("messageSubject", content);
				content = bean.getMessageContent();
				if (content.length() > 30) {
					index = content.lastIndexOf(" ", 30);
					if (index == -1) {
						index = 30;
					}
					content = content.substring(0, index);
				}
				jsonObject.put("messageContent", content);
				jsonObject.put("messageDate", bean.getMessageDate());
				jsonObject.put("mandatoryStatus", bean.getMandatoryStatus());
				jsonObject.put("status", bean.getStatus());
				jsonArray.add(jsonObject);
			}
			out = response.getWriter();
			response.setContentType("text/html");
			responseObject.put("isSuccess", true);
			responseObject.put("responseData", jsonArray);
		} catch (Exception ex) {
			ex.printStackTrace();
			responseObject.put("isSuccess", false);
			responseObject.put("errorMsg", getText("msg.some.error"));
		} finally {
			out.print(responseObject);
			out.flush();
			out.close();
		}

		return SUCCESS;
	}

	public String deleteWebMessage() throws Exception {
		List<Integer> messageList = null;
		JSONObject responseObject = new JSONObject();
		PrintWriter out = null;
		try {
			messageList = new ArrayList<Integer>();
			messageList.add(messageBean.getMessageId());
			MessageInboxServiceImpl.getSingleInstance()
					.updateUserMessageStatus(messageList, "DELETE");
			out = response.getWriter();
			response.setContentType("text/html");
			responseObject.put("isSuccess", true);
		} catch (Exception ex) {
			ex.printStackTrace();
			responseObject.put("isSuccess", false);
			responseObject.put("errorMsg", getText("msg.some.error"));
		} finally {
			out.print(responseObject);
			out.flush();
			out.close();
		}
		return SUCCESS;
	}

	public String changeMessageStatus() throws Exception {
		List<Integer> messageList = null;
		JsonObject messageObject = null;
		JSONObject responseObject = new JSONObject();
		PrintWriter out = null;
		try {
			System.out.println(messages + "-" + messageBean.getStatus());
			JsonArray messageIdArray = new JsonParser().parse(messages)
					.getAsJsonArray();
			messageList = new ArrayList<Integer>();
			for (int i = 0; i < messageIdArray.size(); i++) {
				messageObject = messageIdArray.get(i).getAsJsonObject();
				messageList.add(messageObject.get("messageId").getAsInt());
			}

			MessageInboxServiceImpl.getSingleInstance()
					.updateUserMessageStatus(messageList,
							messageBean.getStatus());
			out = response.getWriter();
			response.setContentType("text/html");
			responseObject.put("isSuccess", true);
		} catch (Exception ex) {
			ex.printStackTrace();
			responseObject.put("isSuccess", false);
			responseObject.put("errorMsg", getText("msg.some.error"));
		} finally {
			out.print(responseObject);
			out.flush();
			out.close();
		}
		return SUCCESS;
	}

	public String editNewMessageMenu() {
		HttpSession session = null;
		AdvMsgHelper helper = null;
		try {
			userTypeMap = new LinkedHashMap<String, String>();

			List<String> userTypeList = MessageInboxServiceImpl.getSingleInstance().getActiveUserType();
			if(userTypeList.contains("BO"))
				userTypeMap.put("BO", getText("BOUser"));
			if(userTypeList.contains("AGENT"))
				userTypeMap.put("AGENT", getText("AGENT"));
			if(userTypeList.contains("RETAILER"))
				userTypeMap.put("RETAILER", getText("Retailer"));

			userTypeMap.put("PLAYER", getText("label.plr"));

			session = getSession();
			helper = new AdvMsgHelper();
			session.setAttribute("ADV_MSG_MAP", helper.getAdvMsgForEdit());
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return SUCCESS;
	}

	public String getMessageListForEdit() throws Exception {
		try {
			messageList = MessageInboxServiceImpl.getSingleInstance()
					.getMessageListForEdit(mode);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return SUCCESS;
	}

	public String updateMessageStatus() throws Exception {
		try {
			MessageInboxServiceImpl.getSingleInstance().updateMessageStatus(
					messageBean.getMessageId(), messageBean.getStatus());
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return SUCCESS;
	}
}