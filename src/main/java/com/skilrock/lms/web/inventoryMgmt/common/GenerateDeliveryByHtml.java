package com.skilrock.lms.web.inventoryMgmt.common;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.struts2.interceptor.ServletRequestAware;

import com.opensymphony.xwork2.ActionSupport;
import com.skilrock.lms.beans.UserInfoBean;
import com.skilrock.lms.common.db.QueryManager;
import com.skilrock.lms.coreEngine.inventoryMgmt.GenerateDeliveryByHtmlHelper;
import com.skilrock.lms.coreEngine.inventoryMgmt.GenerateDeliveryForAgentByHtmlHelper;

public class GenerateDeliveryByHtml extends ActionSupport implements
		ServletRequestAware {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static String getReportGenerationTime() {
		String datestr = new SimpleDateFormat("dd/MM/yyyy")
				.format(new java.util.Date());
		Calendar cal = Calendar.getInstance();
		// int HH=cal.get(Calendar.HOUR)==0? 12:cal.get(Calendar.HOUR);
		int HH = cal.get(Calendar.HOUR_OF_DAY);
		String MM = cal.get(Calendar.MINUTE) < 10 ? "0"
				+ cal.get(Calendar.MINUTE) : "" + cal.get(Calendar.MINUTE);
		String SS = cal.get(Calendar.SECOND) < 10 ? "0"
				+ cal.get(Calendar.SECOND) : "" + cal.get(Calendar.SECOND);
		String AM_PM = cal.get(Calendar.AM_PM) > 0 ? " PM" : " AM";
		// datestr=datestr+" "+HH+":"+MM+AM_PM;
		datestr = datestr + "  " + (HH < 10 ? "0" + HH : HH) + ":" + MM + ":"
				+ SS;
		System.out.println((HH < 10 ? "0" + HH : HH) + "  ================ "
				+ datestr);

		return datestr;
	}
	
	private String id;
	private String orgType;
	private String ownerName;
	private HashMap parameterMap = new HashMap();
	private HttpServletRequest request;

	private String type;

	public String genrateDeliveryNoteForAgent() {

		HttpSession session = request.getSession();
		String formatString = (String) session.getAttribute("date_format");
		// SimpleDateFormat dateformat=new SimpleDateFormat(formatString);

		String orgName;
		int orgId;
		if (orgType != null) {
			orgId = new GenerateDeliveryForAgentByHtmlHelper(type, 0)
					.getAgentOrgId(id);
			orgName = ownerName;
		} else {
			UserInfoBean userBean = (UserInfoBean) session
					.getAttribute("USER_INFO");
			orgId = userBean.getUserOrgId();
			orgName = userBean.getOrgName();
		}

		// get the address details based on organization id
		String addQuery = QueryManager.getST6AddressQuery();
		// String orgAdd=new LedgerHelper().getAddress(addQuery,id+"",null);

		if (type == null) {
			System.out.println("type is null ======================== ");
			type = "DLCHALLAN";
		}
		GenerateDeliveryForAgentByHtmlHelper helper = new GenerateDeliveryForAgentByHtmlHelper(
				type, orgId);
		Map map = null;// helper.getInvoiceDetail(Integer.parseInt(this.id.trim()));

		List<String> orgDetail = helper.getOrgDetails(orgId);

		parameterMap.put("boOrgName", orgName);
		parameterMap.put("boOrgAdd", orgDetail.get(0));
		parameterMap.put("vatRef", orgDetail.get(1));
		parameterMap.put("formatString", formatString);
		parameterMap.put("dcGenerationTime", getReportGenerationTime());

		if ("DSRCHALLAN".equalsIgnoreCase(this.type.trim())) {
			map = helper.getSaleReturnChallan(Integer.parseInt(this.id.trim()));
			parameterMap.put("crDate", helper.transactionDate);
			parameterMap.put("crNote", helper.detailMap.get("creditNote"));
			parameterMap.put("srnNo", helper.detailMap.get("srNo"));
			parameterMap.put("srnDate", helper.transactionDate);
		}

		else {
			map = helper.getInvoiceDetail(Integer.parseInt(this.id.trim()));
			parameterMap.put("invoiceDate", helper.transactionDate);
			parameterMap.put("invoiceId", helper.detailMap.get("invoiceId"));
			parameterMap.put("dcNo", helper.detailMap.get("dcId"));
			parameterMap.put("dcDate", helper.transactionDate);
			parameterMap.put("orderNo", helper.detailMap.get("orderId") + "");
			parameterMap.put("orderDate", helper.detailMap.get("orderDate"));
		}

		CustomerBean custBean = helper.bean;
		if (custBean != null) {
			parameterMap.put("customerName", custBean.getCustomerName());
			parameterMap.put("customerAdd1", custBean.getCustomerAdd1());
			parameterMap.put("customerAdd2", custBean.getCustomerAdd2());
		}

		System.out.println("map ================ " + parameterMap);
		System.out.println("ivoice detail map " + map);
		session.setAttribute("staticReportMap", parameterMap);
		session.setAttribute("deliveryChallanType", type);
		session.setAttribute("detailsMap", map);

		return SUCCESS;
	}

	public String genrateDeliveryNoteForBO() {

		HttpSession session = request.getSession();
		String formatString = (String) session.getAttribute("date_format");
		SimpleDateFormat dateformat = new SimpleDateFormat(formatString);
		UserInfoBean userBean = (UserInfoBean) session
				.getAttribute("USER_INFO");
		int orgId = userBean.getUserOrgId();

		// get the address details based on organization id
		String addQuery = QueryManager.getST6AddressQuery();
		// String orgAdd=new LedgerHelper().getAddress(addQuery,id+"",null);
		String orgName = userBean.getOrgName();

		if (type == null) {
			System.out.println("type is null ======================== ");
			type = "DLCHALLAN";
		}
		GenerateDeliveryByHtmlHelper helper = new GenerateDeliveryByHtmlHelper(
				type, orgId);
		LinkedHashMap<String,Map<String,String>> map = null;// helper.getInvoiceDetail(Integer.parseInt(this.id.trim()));
		List<String> orgDetail = helper.getOrgDetails(orgId);
		parameterMap.put("boOrgName", orgName);
		parameterMap.put("boOrgAdd", orgDetail.get(0));
		parameterMap.put("vatRef", orgDetail.get(1));
		parameterMap.put("formatString", formatString);
		parameterMap.put("dcGenerationTime", getReportGenerationTime());
		 map = helper.getInvoiceDetail(Integer.parseInt(this.id.trim()));
		
		LinkedHashMap<String, String> voucherInfo=helper.getvoucherDetail(Integer.parseInt(this.id.trim()));

		System.out.println("map ================ " + voucherInfo);
		parameterMap.put("dcNo", voucherInfo.get("dcId"));
		parameterMap.put("dcDate", voucherInfo.get("transactionDate"));
		
		System.out.println("map ================ " + parameterMap);
		CustomerBean custBean = helper.getInvoiceForCustomerDetail(Integer.parseInt(voucherInfo.get("currentOwnerId")));
		if (custBean != null) {
			parameterMap.put("customerName", custBean.getCustomerName());
			parameterMap.put("customerAdd1", custBean.getCustomerAdd1());
			parameterMap.put("customerAdd2", custBean.getCustomerAdd2());
		}
		session.setAttribute("staticReportMap", parameterMap);
		session.setAttribute("deliveryChallanType", type);
		session.setAttribute("detailsMap", map);
		return SUCCESS;
	}

	public String getId() {
		return id;
	}

	public String getOrgType() {
		return orgType;
	}

	public String getOwnerName() {
		return ownerName;
	}

	public String getType() {
		return type;
	}

	public String serachDeliveryChallanForBO() {
		System.out.println("serachDeliveryChallanForBO ====== " + orgType + " "
				+ type + "  " + id);
		if ("RETAILER".equalsIgnoreCase(orgType.trim())) {
			genrateDeliveryNoteForAgent();
		} else {
			genrateDeliveryNoteForBO();
		}
		return SUCCESS;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setOrgType(String orgType) {
		this.orgType = orgType;
	}

	public void setOwnerName(String ownerName) {
		this.ownerName = ownerName;
	}

	public void setServletRequest(HttpServletRequest req) {
		this.request = req;

	}

	public void setType(String type) {
		this.type = type;
	}

	
	
}
