package com.skilrock.lms.coreEngine.userMgmt.common;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.common.utility.LMSUtility;
import com.skilrock.lms.coreEngine.service.ServiceDelegateVS;
import com.skilrock.lms.coreEngine.virtualSport.beans.VSCommonResponseBean;
import com.skilrock.lms.coreEngine.virtualSport.beans.VSRequestBean;
import com.skilrock.lms.coreEngine.virtualSport.beans.VSResponseBean;
import com.skilrock.lms.coreEngine.virtualSport.common.daoImpl.CommonMethodsDaoImpl;

enum VirtualSportsURL {
	SESSION_URL(
			"http://asia-api.golden-race.net/api/?method=auth&uid=7299&hash=72aeb32fe0aebf2f6f67aba298bf442d&key=UepaR6UnUtNKZSrorRc"), 
	REGISTER_SHOP(
			"http://asia-api.golden-race.net/api/v2.php?method=admin::add_entity&token="), 
	REGISTER_HARDWARE(
			"http://asia-api.golden-race.net/api/v2.php?method=admin::add_entity&token="), 
	REGISTER_RETAILER(
			"http://asia-api.golden-race.net/api/v2.php?method=admin::add_entity&token="), 
	RETAILER_RESET_PASSWORD(
			"http://asia-api.golden-race.net/api/v2?method=admin::set_staff_hash&token="), 
	GET_SALE_TXN_STATUS(
			"http://asia-api.golden-race.net/api/v2?method=Admin::slw_retry_confirm&transid=");

	private String value;

	private VirtualSportsURL() {}

	private VirtualSportsURL(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
}

public class VirtualSportsIntegration {
	
	private static final Logger logger = LoggerFactory.getLogger(VirtualSportsIntegration.class);
	
	public enum Single {
		INSTANCE;
		VirtualSportsIntegration instance = new VirtualSportsIntegration();

		public VirtualSportsIntegration getInstance() {
			if (instance == null)
				return new VirtualSportsIntegration();
			else
				return instance;
		}
	}

	public void registerRetailer(VSRequestBean vsRequestBean) throws LMSException {
		String url = (String) LMSUtility.sc.getAttribute("VIRTUAL_BETTING_API_URL");
		String sourceId = (String) LMSUtility.sc.getAttribute("VIRTUAL_BETTING_SOURCE_ID");

		// Step 1, Create Session
		VSResponseBean sessionBean = createSession();
		// Step 2, Open Shop
		VSResponseBean shopBean = createShop(sessionBean.getVsCommonResponseBean().getSessionToken(), vsRequestBean);
//		CommonMethodsDaoImpl.getInstance().updateVSShopId(shopBean.getVsCommonResponseBean().getNewEntityId(), orgUserData.getUserId());
		// Step 3, Register Hardware
		VSResponseBean hardwareBean = createHardware(sessionBean.getVsCommonResponseBean().getSessionToken(), shopBean.getVsCommonResponseBean().getNewEntityId(), vsRequestBean);
//		CommonMethodsDaoImpl.getInstance().updateVSPrinterData(hardwareBean.getVsCommonResponseBean().getNewhardWareId(), hardwareBean.getVsCommonResponseBean().getNewEntityId(), orgUserData.getUserId());
		// Step 4, Register Retailer/Operator
		VSResponseBean retailerBean = createRetailer(sessionBean.getVsCommonResponseBean().getSessionToken(), hardwareBean.getVsCommonResponseBean().getTargetId(), vsRequestBean);
//		CommonMethodsDaoImpl.getInstance().updateVsRetailerEntityId(retailerBean.getVsCommonResponseBean().getNewEntityName(), orgUserData.getUserId());
		// Assign Default Priviledges to the retailer
		VSResponseBean priviledgeBean = configureDefaultSettings(sessionBean.getVsCommonResponseBean().getSessionToken(), hardwareBean.getVsCommonResponseBean().getNewEntityId(), url, sourceId);
	}
	
	public VSResponseBean resetPassword(VSRequestBean vsRequestBean) throws LMSException {
		VSResponseBean resetPasswordBean = null;
		// Step 1, Create Session
		VSResponseBean sessionBean = createSession();
		// Step 2, Reset Password
		String targetId = CommonMethodsDaoImpl.getInstance().fetchVsRetailerEntityId(vsRequestBean.getUserId());
		if(targetId != null) {
			resetPasswordBean = resetPassword(sessionBean.getVsCommonResponseBean().getSessionToken(), targetId, vsRequestBean.getPassword());
		}
		return resetPasswordBean;
	}
	
	public VSResponseBean createSession() {
		VSResponseBean vsResponseBean = null;
		VSCommonResponseBean vsCommonResponseBean = null;
		String str = null;
		InputStream resp = null;
		NodeList nodeList = null;
		Node nNode = null;
		try {
			str = ServiceDelegateVS.getInstance().getResponseInputStream(VirtualSportsURL.SESSION_URL.getValue());
			resp = new ByteArrayInputStream(str.getBytes());

			if (resp != null) {
				vsResponseBean = new VSResponseBean();
				DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
				DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
				Document doc = dBuilder.parse(resp);
				doc.getDocumentElement().normalize();

				nodeList = doc.getElementsByTagName("xapi_response");
				nNode = nodeList.item(0);
				if (nNode.getNodeType() == Node.ELEMENT_NODE) {
					Element eElement = (Element) nNode;
					vsResponseBean.setMethodName(eElement.getElementsByTagName("method_called").item(0).getTextContent());
					vsResponseBean.setRequestIp(eElement.getElementsByTagName("request_ip").item(0).getTextContent());
					vsResponseBean.setUtcDate(eElement.getElementsByTagName("utc_date").item(0).getTextContent());
				}

				vsCommonResponseBean = new VSCommonResponseBean();
				nodeList = doc.getElementsByTagName("response");
				nNode = nodeList.item(0);
				if (nNode.getNodeType() == Node.ELEMENT_NODE) {
					Element eElement = (Element) nNode;
					vsCommonResponseBean.setResult(eElement.getElementsByTagName("result").item(0).getTextContent());
					vsCommonResponseBean.setSessionToken(eElement.getElementsByTagName("session_token").item(0).getTextContent());
					vsResponseBean.setVsCommonResponseBean(vsCommonResponseBean);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println(vsResponseBean);
		return vsResponseBean;
	}
	
	public VSResponseBean createShop(String sessionToken, VSRequestBean vsRequestBean) {
		VSResponseBean vsResponseBean = null;
		String str = null;
		InputStream resp = null;
		VSCommonResponseBean vsCommonResponseBean = null;
		NodeList nodeList = null;
		Node nNode = null;

		StringBuilder url = new StringBuilder().append(VirtualSportsURL.REGISTER_SHOP.getValue()).append(sessionToken).append("&target_id=7297&entity_type=11&entity_name=").append(vsRequestBean.getOrgName());
		try {
			str = ServiceDelegateVS.getInstance().getResponseInputStream(url.toString());
			if(str != null) {
				resp = new ByteArrayInputStream(str.getBytes());
				vsResponseBean = new VSResponseBean();
				DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
				DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
				Document doc = dBuilder.parse(resp);
				doc.getDocumentElement().normalize();

				nodeList = doc.getElementsByTagName("xapi_response");
				nNode = nodeList.item(0);
				if (nNode.getNodeType() == Node.ELEMENT_NODE) {
					Element eElement = (Element) nNode;
					vsResponseBean.setMethodName(eElement.getElementsByTagName("method_called").item(0).getTextContent());
					vsResponseBean.setRequestIp(eElement.getElementsByTagName("request_ip").item(0).getTextContent());
					vsResponseBean.setUtcDate(eElement.getElementsByTagName("utc_date").item(0).getTextContent());
				}

				vsCommonResponseBean = new VSCommonResponseBean();
				nodeList = doc.getElementsByTagName("response");
				nNode = nodeList.item(0);
				if (nNode.getNodeType() == Node.ELEMENT_NODE) {
					Element eElement = (Element) nNode;
					vsCommonResponseBean.setResult(eElement.getElementsByTagName("result").item(0).getTextContent());
					vsCommonResponseBean.setTargetId(eElement.getElementsByTagName("target_id").item(0).getTextContent());
					vsCommonResponseBean.setNewEntityId(eElement.getElementsByTagName("new_entity_id").item(0).getTextContent());
					vsCommonResponseBean.setNewEntityName(eElement.getElementsByTagName("new_entity_name").item(0).getTextContent());
					vsResponseBean.setVsCommonResponseBean(vsCommonResponseBean);
				}
				
				CommonMethodsDaoImpl.getInstance().updateVSShopId(vsResponseBean.getVsCommonResponseBean().getNewEntityId(), vsRequestBean.getUserId());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println(vsResponseBean);
		return vsResponseBean;
	}

	public VSResponseBean createHardware(String sessionToken, String entityId, VSRequestBean vsRequestBean) {
		VSResponseBean vsResponseBean = null;
		String str = null;
		InputStream resp = null;
		VSCommonResponseBean vsCommonResponseBean = null;
		NodeList nodeList = null;
		Node nNode = null;

		StringBuilder url = new StringBuilder().append(VirtualSportsURL.REGISTER_HARDWARE.getValue()).append(sessionToken).append("&target_id=").append(entityId).append("&entity_type=16&entity_name=").append(vsRequestBean.getTerminalId());
		try {
			str = ServiceDelegateVS.getInstance().getResponseInputStream(url.toString());
			if(str != null) {
				resp = new ByteArrayInputStream(str.getBytes());
				vsResponseBean = new VSResponseBean();
				DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
				DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
				Document doc = dBuilder.parse(resp);
				doc.getDocumentElement().normalize();

				nodeList = doc.getElementsByTagName("xapi_response");
				nNode = nodeList.item(0);
				if (nNode.getNodeType() == Node.ELEMENT_NODE) {
					Element eElement = (Element) nNode;
					vsResponseBean.setMethodName(eElement.getElementsByTagName("method_called").item(0).getTextContent());
					vsResponseBean.setRequestIp(eElement.getElementsByTagName("request_ip").item(0).getTextContent());
					vsResponseBean.setUtcDate(eElement.getElementsByTagName("utc_date").item(0).getTextContent());
				}

				vsCommonResponseBean = new VSCommonResponseBean();
				nodeList = doc.getElementsByTagName("response");
				nNode = nodeList.item(0);
				if (nNode.getNodeType() == Node.ELEMENT_NODE) {
					Element eElement = (Element) nNode;
					vsCommonResponseBean.setResult(eElement.getElementsByTagName("result").item(0).getTextContent());
					vsCommonResponseBean.setTargetId(eElement.getElementsByTagName("target_id").item(0).getTextContent());
					vsCommonResponseBean.setNewEntityId(eElement.getElementsByTagName("new_entity_id").item(0).getTextContent());
					vsCommonResponseBean.setNewEntityName(eElement.getElementsByTagName("new_entity_name").item(0).getTextContent());
					vsCommonResponseBean.setNewhardWareId(eElement.getElementsByTagName("new_hardware_id").item(0).getTextContent());
					vsResponseBean.setVsCommonResponseBean(vsCommonResponseBean);
				}
				CommonMethodsDaoImpl.getInstance().updateVSPrinterData(vsResponseBean.getVsCommonResponseBean().getNewhardWareId(), vsResponseBean.getVsCommonResponseBean().getNewEntityId(), vsRequestBean.getUserId());

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println(vsResponseBean);
		return vsResponseBean;
	}

	public VSResponseBean createRetailer(String sessionToken, String targetId, VSRequestBean vsRequestBean) {
		VSResponseBean vsResponseBean = null;
		String str = null;
		InputStream resp = null;
		VSCommonResponseBean vsCommonResponseBean = null;
		NodeList nodeList = null;
		Node nNode = null;

		StringBuilder url = new StringBuilder().append(VirtualSportsURL.REGISTER_RETAILER.getValue()).append(sessionToken).append("&target_id=").append(targetId).append("&entity_type=13&entity_name=").append(vsRequestBean.getUserName());
		try {
			str = ServiceDelegateVS.getInstance().getResponseInputStream(url.toString());
			if(str != null) {
				resp = new ByteArrayInputStream(str.getBytes());
				vsResponseBean = new VSResponseBean();
				DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
				DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
				Document doc = dBuilder.parse(resp);
				doc.getDocumentElement().normalize();

				nodeList = doc.getElementsByTagName("xapi_response");
				nNode = nodeList.item(0);
				if (nNode.getNodeType() == Node.ELEMENT_NODE) {
					Element eElement = (Element) nNode;
					vsResponseBean.setMethodName(eElement.getElementsByTagName("method_called").item(0).getTextContent());
					vsResponseBean.setRequestIp(eElement.getElementsByTagName("request_ip").item(0).getTextContent());
					vsResponseBean.setUtcDate(eElement.getElementsByTagName("utc_date").item(0).getTextContent());
				}

				vsCommonResponseBean = new VSCommonResponseBean();
				nodeList = doc.getElementsByTagName("response");
				nNode = nodeList.item(0);
				if (nNode.getNodeType() == Node.ELEMENT_NODE) {
					Element eElement = (Element) nNode;
					vsCommonResponseBean.setResult(eElement.getElementsByTagName("result").item(0).getTextContent());
					vsCommonResponseBean.setTargetId(eElement.getElementsByTagName("target_id").item(0).getTextContent());
					vsCommonResponseBean.setNewEntityId(eElement.getElementsByTagName("new_entity_id").item(0).getTextContent());
					vsCommonResponseBean.setNewEntityName(eElement.getElementsByTagName("new_entity_name").item(0).getTextContent());
					vsResponseBean.setVsCommonResponseBean(vsCommonResponseBean);
				}
				CommonMethodsDaoImpl.getInstance().updateVsRetailerEntityId(vsResponseBean.getVsCommonResponseBean().getNewEntityId(), vsRequestBean.getUserId());

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return vsResponseBean;
	}
	
	public VSResponseBean resetPassword(String sessionToken, String targetId, String password) {
		VSResponseBean vsResponseBean = null;
		String str = null;
		InputStream resp = null;
		VSCommonResponseBean vsCommonResponseBean = null;
		NodeList nodeList = null;
		Node nNode = null;

		StringBuilder url = new StringBuilder().append(VirtualSportsURL.RETAILER_RESET_PASSWORD.getValue()).append(sessionToken).append("&target_id=").append(targetId).append("&pin_hash=").append(password.replaceAll("==", "--"));
		try {
			str = ServiceDelegateVS.getInstance().getResponseInputStream(url.toString());
			if (str != null) {
				resp = new ByteArrayInputStream(str.getBytes());
				vsResponseBean = new VSResponseBean();
				DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
				DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
				Document doc = dBuilder.parse(resp);
				doc.getDocumentElement().normalize();

				nodeList = doc.getElementsByTagName("xapi_response");
				nNode = nodeList.item(0);
				if (nNode.getNodeType() == Node.ELEMENT_NODE) {
					Element eElement = (Element) nNode;
					vsResponseBean.setMethodName(eElement.getElementsByTagName("method_called").item(0).getTextContent());
					vsResponseBean.setRequestIp(eElement.getElementsByTagName("request_ip").item(0).getTextContent());
					vsResponseBean.setUtcDate(eElement.getElementsByTagName("utc_date").item(0).getTextContent());
				}

				vsCommonResponseBean = new VSCommonResponseBean();
				nodeList = doc.getElementsByTagName("response");
				nNode = nodeList.item(0);
				if (nNode.getNodeType() == Node.ELEMENT_NODE) {
					Element eElement = (Element) nNode;
					vsCommonResponseBean.setResult(eElement.getElementsByTagName("result").item(0).getTextContent());
					vsCommonResponseBean.setTargetId(eElement.getElementsByTagName("target_id").item(0).getTextContent());
					vsCommonResponseBean.setNewPinHash(eElement.getElementsByTagName("new_pin_hash").item(0).getTextContent());
					vsResponseBean.setVsCommonResponseBean(vsCommonResponseBean);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return vsResponseBean;
	}
	
	public VSResponseBean getSaleTxnStatus(VSRequestBean vsRequestBean) throws LMSException {
		// Step 1, Create Session
		VSResponseBean sessionBean = createSession();
		// Step 2, Reset Password
		VSResponseBean txnStatusBean = getSaleTxnStatus(sessionBean.getVsCommonResponseBean().getSessionToken(), vsRequestBean.getTxnId());
		return txnStatusBean;
	}
	
	public VSResponseBean getSaleTxnStatus(String sessionToken, String transId) {
		VSResponseBean vsResponseBean = null;
		String str = null;
		InputStream resp = null;
		VSCommonResponseBean vsCommonResponseBean = null;
		NodeList nodeList = null;
		Node nNode = null;

		StringBuilder url = new StringBuilder().append(VirtualSportsURL.GET_SALE_TXN_STATUS.getValue()).append(transId).append("&token=").append(sessionToken);
		try {
			str = ServiceDelegateVS.getInstance().getResponseInputStream(url.toString());
			if (str != null) {
				resp = new ByteArrayInputStream(str.getBytes());
				vsResponseBean = new VSResponseBean();
				DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
				DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
				Document doc = dBuilder.parse(resp);
				doc.getDocumentElement().normalize();

				nodeList = doc.getElementsByTagName("xapi_response");
				nNode = nodeList.item(0);
				if (nNode.getNodeType() == Node.ELEMENT_NODE) {
					Element eElement = (Element) nNode;
					vsResponseBean.setMethodName(eElement.getElementsByTagName("method_called").item(0).getTextContent());
					vsResponseBean.setRequestIp(eElement.getElementsByTagName("request_ip").item(0).getTextContent());
					vsResponseBean.setUtcDate(eElement.getElementsByTagName("utc_date").item(0).getTextContent());
				}

				vsCommonResponseBean = new VSCommonResponseBean();
				nodeList = doc.getElementsByTagName("response");
				nNode = nodeList.item(0);
				String result = null;
				if (nNode.getNodeType() == Node.ELEMENT_NODE) {
					Element eElement = (Element) nNode;
					result = eElement.getElementsByTagName("result").item(0).getTextContent();
					vsCommonResponseBean.setResult(result);
					if("success".equalsIgnoreCase(result))
						logger.info(eElement.getElementsByTagName("slw_response").item(0).getTextContent());
					vsResponseBean.setVsCommonResponseBean(vsCommonResponseBean);
				}
				
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return vsResponseBean;
	}
	
	public VSResponseBean configureDefaultSettings(String sessionToken, String targetId, String URL, String sourceId) {
		VSResponseBean vsResponseBean = null;
		String str = null;
		InputStream resp = null;
		VSCommonResponseBean vsCommonResponseBean = null;
		NodeList nodeList = null;
		Node nNode = null;

		StringBuilder url = new StringBuilder().append(URL).append("api/v2.php?method=admin::set_configurator&token=").append(sessionToken).append("&source_id=").append(sourceId).append("&target_id=").append(targetId).append("&target_section=all");
		try {
			str = ServiceDelegateVS.getInstance().getResponseInputStream(url.toString());
			if(str != null) {
				resp = new ByteArrayInputStream(str.getBytes());
				vsResponseBean = new VSResponseBean();
				DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
				DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
				Document doc = dBuilder.parse(resp);
				doc.getDocumentElement().normalize();

				nodeList = doc.getElementsByTagName("xapi_response");
				nNode = nodeList.item(0);
				if (nNode.getNodeType() == Node.ELEMENT_NODE) {
					Element eElement = (Element) nNode;
					vsResponseBean.setMethodName(eElement.getElementsByTagName("method_called").item(0).getTextContent());
					vsResponseBean.setRequestIp(eElement.getElementsByTagName("request_ip").item(0).getTextContent());
					vsResponseBean.setUtcDate(eElement.getElementsByTagName("utc_date").item(0).getTextContent());
				}

				vsCommonResponseBean = new VSCommonResponseBean();
				nodeList = doc.getElementsByTagName("response");
				nNode = nodeList.item(0);
				if (nNode.getNodeType() == Node.ELEMENT_NODE) {
					Element eElement = (Element) nNode;
					vsCommonResponseBean.setResult(eElement.getElementsByTagName("result").item(0).getTextContent());
					vsCommonResponseBean.setSourceId(eElement.getElementsByTagName("source_id").item(0).getTextContent());
					vsCommonResponseBean.setTargetId(eElement.getElementsByTagName("target_id").item(0).getTextContent());
					vsCommonResponseBean.setTargetSection(eElement.getElementsByTagName("target_section").item(0).getTextContent());
					vsResponseBean.setVsCommonResponseBean(vsCommonResponseBean);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return vsResponseBean;
	}
	
}