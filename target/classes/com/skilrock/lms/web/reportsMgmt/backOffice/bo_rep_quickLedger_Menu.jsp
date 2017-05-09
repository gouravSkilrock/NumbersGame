<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="java.util.Calendar"%>
<%@page import="com.skilrock.lms.common.utility.CommonMethods"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<%
	response.setHeader("Cache-Control", "no-store"); //HTTP 1.1
	response.setHeader("Pragma", "no-cache"); //HTTP 1.0
	response.setDateHeader("Expires", 0); //prevents caching at the proxy server
%>
<html>
	<head>
		<s:head theme="ajax" debug="false" />
		<meta http-equiv="Content-Type"
			content="text/html; charset=utf-8" />
		<link rel="stylesheet"
			href="<%=request.getContextPath()%>/LMSImages/css/styles.css"
			type="text/css" />
		<title><%=application.getAttribute("JSP_PAGE_TITLE")%></title>
		<link type="text/css" rel="stylesheet"
			href="<%=request.getContextPath()%>/LMSImages/css/lmsCalendar.css"
			media="screen" />
	<script>var projectName="<%=request.getContextPath() %>"</script>	
	<script type="text/javascript" src="<%=request.getContextPath() %>/com/skilrock/lms/web/reportsMgmt/backOffice/js/quickLedgerRep.js"></script>
	<script type="text/javascript"
			src="<%=request.getContextPath()%>/com/skilrock/lms/web/common/globalJs/calender.js"></script>
	</head>

	<body onload="fillOptions()">
		<%@include file="/com/skilrock/lms/web/loginMgmt/menu.jsp"%>

		<div id="wrap">
			<div id="top_form">
				<h3>
					<s:text name="menu.quick.ledger"/>
				</h3>
				<s:form action="bo_rep_quickLedger_Search"  onsubmit="return compareTime();">
					<table  border="0" cellpadding="2" cellspacing="0">
						<tr>
							<td align="center" colspan="2">
								<div id="errorDiv"></div>
							</td>
						</tr>
						<tr  align="left">
							<td><s:text name="label.slct.agnt.org"/></td>
							<td>
								<s:select theme="simple" cssClass="option" name="agentOrgId" id="agentOrgId"
									list="{}" headerKey="-1"
									headerValue="%{getText('label.please.select')}"
									onchange="getRetailerList(this)"></s:select>
							</td><td><div id="agtReport"></div></td>
						</tr>
						<tr>
						<br/>
						</tr>
						  <tr align="left" id='retDiv'>
							<td><s:text name="label.slct.rtailr.org"/></td>
							<td>
								<s:select theme="simple" cssClass="option" name="retOrgId"
									id="retOrgId" list="{}" headerKey="-1"
									headerValue="%{getText('label.please.select')}"></s:select>
							</td><td><div id="retReport"></div></td>
						</tr>
						<tr align="left">
							<td>
								<s:text name="label.report.type"/><span class="required">*</span>
							</td>
							<td>
								<s:select theme="simple" cssClass="option" name="reportType"
									id="reportType" list="#{'Transaction Report':getText('label.txn.rep'),'Ledger Report':getText('label.ledger.rep')}"
									headerKey="-1" headerValue="%{getText('label.please.select')}"></s:select>
							</td><td><div id="report"></div></td>
						</tr>
						
						<!--<tr align="left">
							<td>
								Ledger Type
							</td>
							<td>
								<s:select theme="simple" cssClass="option" name="ledgerType"
									id="reportType" list="{'Date Wise'}"
									headerKey="-1" headerValue="--Please Select--"
									></s:select>
							</td>
						</tr>
						
							--><tr>
							<td colspan="2">
								<table cellpadding="3"
									cellspacing="0" border="0">
									<s:set name="stDate" id="stDate" value="#session.presentDate" />
									<%
										Calendar prevCal = Calendar.getInstance();
											String startDate = CommonMethods
													.convertDateInGlobalFormat(new java.sql.Date(prevCal
															.getTimeInMillis()).toString(), "yyyy-mm-dd",
															"yyyy-mm-dd");
															
															String lastDate = CommonMethods.getLastArchDate();
									%>
									<tr>
										<td rowspan="2">
											<label class="label">
											<s:text name="label.date"/><span>*</span>:&nbsp;
											</label>
											<input type="hidden" value="<%=lastDate %>" id="arch_date" />
											<input type="text" name="start_date" id="start_date"
												value="<%=startDate%>" readonly size="12" onchange="closeDiv();">
											<input type="button"
												style="width: 19px; height: 19px; background: url('<%=request.getContextPath()%>/LMSImages/imagesCal/dateIcon.gif'); top left; border: 0;"
												onclick="displayCalendar(document.getElementById('start_date'),'yyyy-mm-dd', this, '<%=startDate%>', false, '')" />
										</td>
										<td>&nbsp;&nbsp;&nbsp;&nbsp;<s:text name="label.from.time"/> :</td>
										<td>
												<table>
													<s:select name="fromHour" cssClass="option" id="from_hour"
														list="{''}" />
												</table>
											</td>
											<td>
												<table>
													<s:select name="fromMin" cssClass="option" id="from_min"
														list="{''}" />
												</table>
											</td>
											<td>
												<table>
													<s:select name="fromSec" cssClass="option" id="from_sec"
														list="{''}" />
												</table>
											</td>
									</tr>
									<tr>
									<td>&nbsp;&nbsp;&nbsp;&nbsp;  <s:text name="label.to.time"/> :</td>
									<td>
												<table>
													<s:select name="toHour" cssClass="option" id="to_hour"
														list="{''}" />
												</table>
											</td>
											<td>
												<table>
													<s:select name="toMin" cssClass="option" id="to_min"
														list="{''}" />
												</table>
											</td>
											<td>
												<table>
													<s:select name="toSec" cssClass="option" id="to_sec"
														list="{''}" />
												</table>
											</td>
									</tr>
									</table>
							</td>
						</tr>
						<!--<tr>
							<td>
								<div id="dates"></div>
							</td>
						</tr>
						<tr>
							<td align="center" colspan="2">
								<%
									//Calendar prevCal = Calendar.getInstance();
										//String startDate = CommonMethods
											//	.convertDateInGlobalFormat(new java.sql.Date(prevCal
												//		.getTimeInMillis()).toString(), "yyyy-mm-dd",
													//	(String) session.getAttribute("date_format"));
								%>
								<s:hidden name="curDate" id="curDate" value="<%=startDate%>"></s:hidden>
								<div id="date"
									style="display: none; text-align: left; width: 60%">
									<s:div id="newDates" theme="ajax"
										href="rep_common_fetchDateWithTime.action">
									</s:div>
								</div>
							</td>

						</tr>
						--><tr>
							<td>
								<s:submit name="search" key="btn.srch" align="right"
									targets="down" theme="ajax" cssClass="button" />
							</td>
						</tr>
					</table>
				</s:form>
				<div id="down" style="text-align: center"></div>
			</div>
		</div>
	</body>
</html>

<code id="headId" style="visibility: hidden">
$RCSfile: bo_rep_quickLedger_Menu.jsp,v $
$Revision: 1.3 $
</code>