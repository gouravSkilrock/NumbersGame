package com.skilrock.lms.web.reportsMgmt.common;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;

import org.apache.struts2.ServletActionContext;

import com.skilrock.lms.common.BaseAction;
import com.skilrock.lms.coreEngine.reportsMgmt.controllerImpl.IntakeReportControllerImpl;
import com.skilrock.lms.coreEngine.reportsMgmt.javaBeans.IntakeReportDataBean;

public class IntakeReportAction extends BaseAction {
	private static final long serialVersionUID = 1L;

	public IntakeReportAction() {
		super(IntakeReportAction.class);
	}

	private String startDate;
	private String endDate;
	private String service;
	private String serviceDispName;
	private List<IntakeReportDataBean> reportList;

	public String getService() {
		return service;
	}

	public void setService(String service) {
		this.service = service;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	
	public String getServiceDispName() {
		return serviceDispName;
	}

	public void setServiceDispName(String serviceDispName) {
		this.serviceDispName = serviceDispName;
	}

	public List<IntakeReportDataBean> getReportList() {
		return reportList;
	}

	public void setReportList(List<IntakeReportDataBean> reportList) {
		this.reportList = reportList;
	}

	public String reportMenu() {
		return SUCCESS;
	}

	public String reportSearch() {
		
		try {
			SimpleDateFormat dateFormat = null;
			dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			Timestamp startTime = new Timestamp(dateFormat.parse(startDate).getTime());
			Timestamp endTime = new Timestamp(dateFormat.parse(endDate).getTime()+(24*60*60*1000-1000));	
			

			reportList = IntakeReportControllerImpl.getInstance().fetchReportData(service,startTime, endTime);
			ServletContext context = ServletActionContext.getServletContext();
			Map<String, String> serviceCodeNameMap = (Map<String, String>) context.getAttribute("SERVICES_CODE_NAME_MAP");

			serviceDispName = serviceCodeNameMap.get(service);
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return SUCCESS;
	}
	
	/*	public void reportSearch(){
		JSONArray dataArr = new JSONArray();
		JSONArray rowArr = null;
		JSONObject aaData = null;
		int count = 1;
		SimpleDateFormat dateFormat = null;
		IntakeReportDataBean bean;
		Map<Date, IntakeReportDataBean> map = null;
		try {
			
			dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			Timestamp startTime = new Timestamp(dateFormat.parse(startDate).getTime());
			Timestamp endTime = new Timestamp(dateFormat.parse(endDate).getTime()+(24*60*60*1000-1000));
			reportList = IntakeReportControllerImpl.getInstance().fetchReportData(startTime, endTime);
			Iterator<IntakeReportDataBean> iterator = reportList.iterator();
			while(iterator.hasNext()){
				IntakeReportDataBean intakeBean = iterator.next();
				rowArr = new JSONArray();
				rowArr.add(count);
				count++;
				rowArr.add(intakeBean.getTransDate());
				rowArr.add(intakeBean.getRetOrgCode());
				rowArr.add(intakeBean.getAgtOrgCode());
				rowArr.add("Sent from fpfcc System");
				rowArr.add("Small Win Payment");
				rowArr.add(intakeBean.getPwtAmount());
				rowArr.add(intakeBean.getCity());
				rowArr.add(intakeBean.getAreaName());
				dataArr.add(rowArr);
			}
			aaData = new JSONObject();
			aaData.put("aaData", dataArr);

			JSONArray js = new JSONArray();
			JSONObject colObj = null;
			colObj = new JSONObject();
			colObj.put("sTitle", "Sr.No");
			colObj.put("sClass", "right-format");
			js.add(colObj);
			colObj = new JSONObject();
			colObj.put("sTitle", "Transaction Date");
			colObj.put("sClass", "left-format");
			js.add(colObj);
			colObj = new JSONObject();
			colObj.put("sTitle", "Reatiler ID");
			colObj.put("sClass", "numeric-comma amount-format");
			js.add(colObj);
			colObj = new JSONObject();
			colObj.put("sTitle", "LMC ID");
			colObj.put("sClass", "numeric-comma amount-format");
			js.add(colObj);
			colObj = new JSONObject();
			colObj.put("sTitle", "Transaction Reference");
			colObj.put("sClass", "numeric-comma amount-format");
			js.add(colObj);
			colObj = new JSONObject();
			colObj.put("sTitle", "Type of Transaction");
			colObj.put("sClass", "numeric-comma amount-format");
			js.add(colObj);
			colObj = new JSONObject();
			colObj.put("sTitle", "Amount Credited");
			colObj.put("sClass", "numeric-comma amount-format");
			js.add(colObj);
			colObj = new JSONObject();
			colObj.put("sTitle", "POS Sales Location");
			colObj.put("sClass", "numeric-comma amount-format");
			js.add(colObj);
			colObj = new JSONObject();
			colObj.put("sTitle", "Post Zone Location");
			colObj.put("sClass", "numeric-comma amount-format");
			js.add(colObj);

			aaData.put("aoColumns", js);
			aaData.put("totalRow", dataArr.size());
			aaData.put("startDate", startDate);
			aaData.put("endDate", endDate);
			aaData.put("rowCount", -1);
			aaData.put("colCount", -1);
			aaData.put("responseCode", 0);
			sendResponse(aaData);
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
		
		public void sendResponse(JSONObject responseData) throws IOException{
			response.setContentType("application/json");
			PrintWriter out = null;
			out = response.getWriter();
			out.print(responseData);
			out.flush();
			out.close();
		}
*/
	/*public void exportAsExcel() throws IOException {
		response.setContentType("application/vnd.ms-excel");
		response.setHeader("Content-Disposition", "attachment; filename=PriviledgeModificationReport.xls");
		PrintWriter out = response.getWriter();
		if (tableValue != null) {
			tableValue = tableValue.replaceAll("<tbody>", "").replaceAll("</tbody>", "").trim();
			out.write("<table border='1' width='100%' >" + tableValue + "</table>");
		}
	}*/
}