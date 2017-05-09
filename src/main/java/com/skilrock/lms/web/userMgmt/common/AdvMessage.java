package com.skilrock.lms.web.userMgmt.common;

import java.io.PrintWriter;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import com.skilrock.lms.ajax.AjaxRequestHelper;
import com.skilrock.lms.beans.InboxMessageBean;
import com.skilrock.lms.beans.OrganizationBean;
import com.skilrock.lms.beans.UserInfoBean;
import com.skilrock.lms.common.BaseAction;
import com.skilrock.lms.common.utility.CommonMethods;
import com.skilrock.lms.common.utility.LMSUtility;
import com.skilrock.lms.coreEngine.instantWin.common.IWUtil;
import com.skilrock.lms.coreEngine.messageMgmt.common.InboxMessageMgmtHelper;
import com.skilrock.lms.coreEngine.ola.common.OLAUtility;
import com.skilrock.lms.coreEngine.sportsLottery.common.SLEUtil;
import com.skilrock.lms.coreEngine.sportsLottery.common.controllerImpl.CommonMethodsControllerImpl;
import com.skilrock.lms.coreEngine.userMgmt.common.AdvMsgHelper;
import com.skilrock.lms.coreEngine.userMgmt.common.RetailerAdvMsgHelper;
import com.skilrock.lms.web.drawGames.common.Util;
import com.skilrock.lms.web.reportsMgmt.common.ReportUtility;

/**
 * 
 * @author Gaurav Ujjwal
 * 
 * <pre>
 * Change History
 * Change Date     Changed By     Change Description
 * -----------     ----------     ------------------
 * (e.g.)
 * 01-JAN-2005     ABxxxxxx       CR#zzzzzz: blah blah blah... 
 * 28-MAY-2010     Arun Tanwar    CR#L0375:Implementation of winning numbers for manual entry(freezed draws).
 * 02-MAY-2010     Arun Tanwar    CR#L0375:Implementation of winning numbers for manual entry. Method getManualEntryData added.
 * 03-MAY-2010     Arun Tanwar    CR#L0375:Implementation of entering PMEP for ACTIVE draws. Method getManualDeclareData added.
 * </pre>
 */
public class AdvMessage extends BaseAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String[] gameName;
	private String[] categoryName;
	private String[] walletName;
	private String[] gameNo;
	
	private String[] categoryNo;
	private String[] walletNo;
	private String serviceId;
	private String serviceType;

	private String message;
	private String msgLocation;
	private String orgType;
	private String[] retName;
	private String search_type;
	private String status;
	private String activity;
	private int msgId;
	
	private HashMap<Integer, String> commSerList;
	private HashMap<Integer, String> drawSerList;
	private Map<Integer, String> olaSerList;
	private List<OrganizationBean> organizationList;
	
	// FOR 'RETAILER' TYPE
	private String mode;
	private String activity1;
	private String[] agtName;
	
	private String activity2;
	private String activity3;

	private String messageSubject;
	private String messageContent;
	private String expiryDate;
	private String expHr;
	private String expMin;
	private String expSec;
	private boolean isPopup;
	private boolean isMandatory;
	private String interfaceType;
	private List<InboxMessageBean> messageList;
	private String type;

	public AdvMessage() {
		super(AdvMessage.class);
	}

	public String fetchAdvMsgData() throws Exception {
		HttpSession session = request.getSession();
		if("AgentWise".equalsIgnoreCase(search_type) || "LocationWise".equalsIgnoreCase(search_type)) {
			AdvMsgHelper helper = new AdvMsgHelper();
			session.setAttribute("RET_MAP", helper.fetchAdvMessageData(search_type));
			return "RETAILER";
		} else {	
			AjaxRequestHelper helper = new AjaxRequestHelper();
			organizationList = helper.getBoAgtList(search_type);
			return "BO_AGENT";
		}
	}

	public String advMessageMenu() throws Exception {
		commSerList=null;
		drawSerList=null;
		olaSerList=null;
		try {
			setCommSerList(ReportUtility.fetchActiveCategoriesCommSerData());
			setDrawSerList(ReportUtility.fetchActiveGameDrawDataMenu());
			setOlaSerList(OLAUtility.getOlaWalletDataMap());
		} catch (Exception e) {
			logger.error(" EXCEPTION : " +e );
		}
		return SUCCESS;
	}

	public String getInterfaceList() {
		List<String> interfaceList = null;
		PrintWriter out = null;
		try {
			interfaceList = CommonMethods.getTierWiseInterfaceList(orgType);
			String interfaceString = "";
			if(interfaceList.size()>0) {
				for(String interfaceName : interfaceList) {
					interfaceString += interfaceName+",";
				}
				interfaceString = interfaceString.substring(0, interfaceString.length()-1);
			}

			out = response.getWriter();
			response.setContentType("text/html");
			out.write(interfaceString);
			out.flush();
			out.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return SUCCESS;
	}

	@SuppressWarnings("unchecked")
	public String saveAdvMsgData() throws Exception {
		int srvcId =0;
		String result = "error";
		HttpSession session = request.getSession();
		UserInfoBean userInfoBean = (UserInfoBean) session.getAttribute("USER_INFO");
		AdvMsgHelper helper = new AdvMsgHelper();
		
		if("PLAYER".equalsIgnoreCase(orgType)){
			srvcId = ((HashMap<String,Integer>)LMSUtility.sc.getAttribute("SERVICES_CODE_ID_MAP")).get(serviceId);
			if("DG".equalsIgnoreCase(serviceType)){
				result = helper.saveAdvMessageData(orgType, gameNo, retName, message,
						userInfoBean.getUserId(), msgLocation, activity, srvcId);
				logger.debug("----selection Map--" + session.getAttribute("RET_MAP"));
				gameName = new String[gameNo.length];
				for (int i = 0; i < gameNo.length; i++) {
					gameName[i] = Util.getGameDisplayName(Integer.parseInt(gameNo[i]));
				}
				setGameName(gameName);
			} else if("CS".equalsIgnoreCase(serviceType)){
				categoryName = new String[categoryNo.length];
				result = helper.saveAdvMessageData(orgType, categoryNo, retName, message,
						userInfoBean.getUserId(), msgLocation, activity2, srvcId);
				for (int i = 0; i < categoryNo.length; i++) {
					categoryName[i] = Util.getCategoryName(Integer.parseInt(categoryNo[i])); 
				}
				setCategoryName(categoryName);
			} else if("OLA".equalsIgnoreCase(serviceType)){
				walletName = new String[walletNo.length];
				result = helper.saveAdvMessageData(orgType, walletNo, retName, message,
						userInfoBean.getUserId(), msgLocation, activity3, srvcId);
				for (int i = 0; i < walletNo.length; i++) {
					walletName[i] = OLAUtility.getWalletName(Integer.parseInt(walletNo[i])); 
				}
				setWalletName(walletName);
			}
		} else if ("BO".equalsIgnoreCase(orgType) || "AGENT".equalsIgnoreCase(orgType) || "RETAILER".equalsIgnoreCase(orgType)) {
			//result = helper.saveAdvMessageDataForRetailer(orgType, agtName, retName, message, userInfoBean.getUserId(), "NA", activity1);
			if("TERMINAL".equals(interfaceType)) {
				messageSubject = messageSubject.replace("~", "").replace("%", "").replace("|", "").replace("#", "").replaceAll("( )+", " ").trim();
				messageContent = messageContent.replaceAll("[\n\r]", "").replace("~", "").replace("%", "").replace("|", "").replace("#", "").replaceAll("( )+", " ").trim();
			}

			InboxMessageMgmtHelper messageHelper = new InboxMessageMgmtHelper();

			Timestamp expiryPeriod = null;
			if(("FLASH".equals(interfaceType) || "RETAILER".equals(orgType))) {
				SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				expiryPeriod = new Timestamp(simpleDateFormat.parse(expiryDate+" "+expHr+":"+expMin+":"+expSec).getTime());
			}
			messageHelper.addFlashInboxMessages(messageSubject, messageContent, expiryPeriod, mode, 
					(isPopup==true)?"YES":"NO", (isMandatory==true)?"YES":"NO", userInfoBean.getUserId(),
					orgType, interfaceType, retName);
			result="SUCCESS_BAR";
		}

		if(!"REGISTRATION".equals(mode)) {
			if (!Arrays.asList(retName).contains("-1")) 
				for(int i=0; i<retName.length; i++)
					retName[i] = retName[i].split("~")[1];
		}

		return result;
	}

	/*
	public String fetchAdvMessage() throws Exception {
		InboxMessageMgmtHelper helper = new InboxMessageMgmtHelper();
		UserInfoBean userBean = null;
		msgId = 0;
		try {
			userBean = getUserBean();
			messageList = helper.fetchUserWiseMessage(msgId, userBean.getUserId(), userBean.getUserType(), interfaceType, "INBOX");
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return SUCCESS;
	}
	*/

	public String fetchAdvMessage() throws Exception {
		InboxMessageMgmtHelper helper = new InboxMessageMgmtHelper();
		UserInfoBean userBean = null;
		msgId = 0;
		try {
			userBean = getUserBean();
			messageList = helper.fetchUserWiseMessage(msgId, userBean.getUserId(), userBean.getUserType(), interfaceType, "INBOX");
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return SUCCESS;
	}

	/*
	public String fetchActiveDrawDataMenu() throws Exception {
		commSerList=null;
		drawSerList=null;
		olaSerList=null;
		try {
			setCommSerList(ReportUtility.fetchActiveCategoriesCommSerData());
			setDrawSerList(ReportUtility.fetchActiveGameDrawDataMenu());
			setOlaSerList(OLAUtility.getOlaWalletDataMap());
		} catch (Exception e) {
			logger.error(" EXCEPTION : " +e );
		}
		return SUCCESS;
	}
	*/
	
	public String getAdvMsgDataForEdit() throws Exception {
		HttpSession session = request.getSession();
		AdvMsgHelper helper = new AdvMsgHelper();
		session.setAttribute("ADV_MSG_MAP", helper.getAdvMsgForEdit());
		logger.debug("----Adv Msg Map--" + session.getAttribute("ADV_MSG_MAP"));
		InboxMessageMgmtHelper messageMgmtHelper = new InboxMessageMgmtHelper();
		messageList = messageMgmtHelper.getAllMessagesByStatus(null);
		return SUCCESS;
	}
	
	
	public String editAdvMsgData() throws Exception {
		if("plrMessage".equals(type)) {
			HttpSession session = request.getSession();
			UserInfoBean userBean = (UserInfoBean) session.getAttribute("USER_INFO");
			AdvMsgHelper helper = new AdvMsgHelper();
			boolean status = helper.editAdvMsgStatus(msgId, userBean.getUserId(),userBean.getUserOrgId());
			if (!status) {
				return "applicationError";
			}
			Util.advMsgDataMap = new RetailerAdvMsgHelper().getAdvMsgDataMap();
			SLEUtil.advMessageMap = CommonMethodsControllerImpl.getInstance().getSLEAdvMessageMap();
			IWUtil.advMessageMap = com.skilrock.lms.coreEngine.instantWin.common.controllerImpl.CommonMethodsControllerImpl.getInstance().getIWAdvMessageMap();
		} else if ("retMessage".equals(type)) {
			InboxMessageMgmtHelper helper = new InboxMessageMgmtHelper();
			InboxMessageBean messageBean = new InboxMessageBean();
			messageBean.setMessageId(msgId);
			messageBean.setExpiryDate(expiryDate);
			messageBean.setIsPopup((isPopup)==true?"YES":"NO");
			messageBean.setIsMandatory((isMandatory)==true?"YES":"NO");
			messageBean.setStatus(status);
			helper.editInboxMessage(messageBean);
		}

		return SUCCESS;
	}


	public String[] getGameName() {
		return gameName;
	}

	public String[] getGameNo() {
		return gameNo;
	}

	public String getMessage() {
		return message;
	}

	public String getMsgLocation() {
		return msgLocation;
	}

	public String getOrgType() {
		return orgType;
	}

	public String[] getRetName() {
		return retName;
	}

	public String getSearch_type() {
		return search_type;
	}

	public String getStatus() {
		return status;
	}

	public void setGameName(String[] gameName) {
		this.gameName = gameName;
	}

	public void setGameNo(String[] gameNo) {
		this.gameNo = gameNo;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public void setMsgLocation(String msgLocation) {
		this.msgLocation = msgLocation;
	}

	public void setOrgType(String orgType) {
		this.orgType = orgType;
	}

	public void setRetName(String[] retName) {
		this.retName = retName;
	}

	public void setSearch_type(String search_type) {
		this.search_type = search_type;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getActivity() {
		return activity;
	}

	public void setActivity(String activity) {
		this.activity = activity;
	}

	public String getMode() {
		return mode;
	}

	public void setMode(String mode) {
		this.mode = mode;
	}

	public String getActivity1() {
		return activity1;
	}

	public void setActivity1(String activity1) {
		this.activity1 = activity1;
	}

	public String[] getAgtName() {
		return agtName;
	}

	public void setAgtName(String[] agtName) {
		this.agtName = agtName;
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

	public String getServiceType() {
		return serviceType;
	}

	public void setServiceType(String serviceType) {
		this.serviceType = serviceType;
	}

	public String getServiceId() {
		return serviceId;
	}

	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
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

	public int getMsgId() {
		return msgId;
	}

	public void setMsgId(int msgId) {
		this.msgId = msgId;
	}

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

	public List<OrganizationBean> getOrganizationList() {
		return organizationList;
	}

	public void setOrganizationList(List<OrganizationBean> organizationList) {
		this.organizationList = organizationList;
	}

	public String getMessageSubject() {
		return messageSubject;
	}

	public void setMessageSubject(String messageSubject) {
		this.messageSubject = messageSubject;
	}

	public String getMessageContent() {
		return messageContent;
	}

	public void setMessageContent(String messageContent) {
		this.messageContent = messageContent;
	}

	public String getExpiryDate() {
		return expiryDate;
	}

	public void setExpiryDate(String expiryDate) {
		this.expiryDate = expiryDate;
	}

	public String getExpHr() {
		return expHr;
	}

	public void setExpHr(String expHr) {
		this.expHr = expHr;
	}

	public String getExpMin() {
		return expMin;
	}

	public void setExpMin(String expMin) {
		this.expMin = expMin;
	}

	public String getExpSec() {
		return expSec;
	}

	public void setExpSec(String expSec) {
		this.expSec = expSec;
	}

	public boolean getIsPopup() {
		return isPopup;
	}

	public void setIsPopup(boolean isPopup) {
		this.isPopup = isPopup;
	}

	public boolean getIsMandatory() {
		return isMandatory;
	}

	public void setIsMandatory(boolean isMandatory) {
		this.isMandatory = isMandatory;
	}

	public String getInterfaceType() {
		return interfaceType;
	}

	public void setInterfaceType(String interfaceType) {
		this.interfaceType = interfaceType;
	}

	public List<InboxMessageBean> getMessageList() {
		return messageList;
	}

	public void setMessageList(List<InboxMessageBean> messageList) {
		this.messageList = messageList;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
}