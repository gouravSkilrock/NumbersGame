<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="com.skilrock.lms.common.utility.CommonMethods"%>
<%@page import="java.util.Calendar"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%
	java.util.Calendar calendar = java.util.Calendar.getInstance();
	calendar.setTimeInMillis(System.currentTimeMillis());
	calendar.set(calendar.HOUR_OF_DAY, 23);
	calendar.set(calendar.MINUTE, 59);
	calendar.set(calendar.SECOND, 59);
%>
<%
	response.setHeader("Cache-Control", "no-store"); //HTTP 1.1
	response.setHeader("Pragma", "no-cache"); //HTTP 1.0
	response.setDateHeader("Expires", 0); //prevents caching at the proxy server
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">

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

		<script>var projectName="<%=request.getContextPath()%>"</script>
		<script type="text/javascript"
			src="<%=request.getContextPath()%>/com/skilrock/lms/web/common/globalJs/calender.js"></script>
		<script type="text/javascript"
			src="<%=request.getContextPath()%>/com/skilrock/lms/web/common/globalJs/jquery-1.10.1.min.js"></script>
		<script type="text/javascript"
				src="<%=request.getContextPath()%>/com/skilrock/lms/web/reportsMgmt/backOffice/js/ActInactReportjs.js">
				</script>

	</head>
	<body onload="showHideData();">
		<%@include file="/com/skilrock/lms/web/loginMgmt/menu.jsp"%>
		<div id="wrap">
			<div id="top_form">
				<h3>
					<b>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<s:text name="label.active.inactive.terminal"/>
						<s:text name="Report"/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</b>
				</h3>

				
				<s:form name="ActInactInvStatusReport"
					action="bo_rep_actInactInvStatus_reportSearch"
					onsubmit="return validateData();return validateDates();">

					<br />
					<br />
					<div id="dates"></div>
					<div id="errorDiv"></div>
					<input type="hidden" name="agentCode" id="agentCode"/>
					<table border="0" cellpadding="2" cellspacing="0" width="400"
						style="width: 100%; vertical-align: middle;" align='center'>
						
						<tr>
							<td align="left">
							<i><!--<s:label value="Report Type" id="reportTypeLabel" required="true" theme="simple"/>--><s:text name="label.report.type"/><font color = 'red'>*</font></i>
								<s:select name="reportType" id="reportType" headerKey="-1"
									headerValue="-- Please Select --"
									list="#{'1':'ALL','2':'LMC WISE','3':'REGION WISE','4':'CONSOLIDATED'}"
									required="true" cssClass="option" onchange="showHideData();" theme="simple"/>
							</td>

						</tr>
						<tr>
							<td>
								
									<s:set name="startDate" id="startDate"
										value="#session.presentDate" />
									<%
										Calendar prevCal = Calendar.getInstance();
											String startDate = CommonMethods
													.convertDateInGlobalFormat(new java.sql.Date(prevCal
															.getTimeInMillis()).toString(), "yyyy-mm-dd",
															"yyyy-mm-dd");
									%>
									</td>
									</tr>
									<tr>
										<td>
											<label class="label">
												<s:text name="label.start.date"/>
												<font color = 'red'><span>*</span></font>:&nbsp;
											</label>
											<input type="text" name="startDate" id="startDate"
												value="<%=startDate%>" readonly size="12">
											<input type="button"
												style="width: 19px; height: 19px; background: url('<%=request.getContextPath()%>/LMSImages/imagesCal/dateIcon.gif'); top left; border: 0;"
												onclick="displayCalendar(_id.o('startDate'),'yyyy-mm-dd', this, '<%=startDate%>', false, '<%=startDate%>')" />
										</td>
									</tr>
									
									<tr>
										<td>
										<div id="endDateDiv">
											<label class="label">
												<s:text name="label.end.date"/>
												<font color = 'red'><span>*</span></font>:&nbsp;
											</label>
											<input type="text" name="endDate" id="endDate"
												value="<%=startDate%>" readonly size="12" onchange="showHideData();" >
											<input type="button"
												style="width: 19px; height: 19px; background: url('<%=request.getContextPath()%>/LMSImages/imagesCal/dateIcon.gif'); top left; border: 0;"
												onclick="displayCalendar(_id.o('endDate'),'yyyy-mm-dd', this, '<%=startDate%>',false, '<%=startDate%>')" />
												</div>
										</td>
									</tr>
									
						<tr>
						<td align="left">
							<div id ="agentNameDiv">
								<i><!--<s:label value="Select Organization" id="agentNameLabel" required="true" theme="simple"/>--><s:text name="label.select.org"/><font color = 'red'>*</font></i>
								<s:select name="agentName" id="agentName" headerKey="-1"
									headerValue="-- Please Select --"
									list="{}"
									required="true" cssClass="option"  theme="simple" onchange="onAgentChange();clearDiv();"/>
							</div>
							</td>
						</tr>
						<tr>
						
							<td align="left">
							<div id ="cityNameDiv">
							<i><!--<s:label value="Select Region" id="cityNameLabel" required="true" theme="simple"/>--><s:text name="label.slct.region"/><font color = 'red'>*</font></i>
								<s:select name="cityName" id="cityName" headerKey="-1"
									headerValue="-- Please Select --"
									list="{}"
									required="true" cssClass="option"  theme="simple" onchange="createRegions(this.value),clearDiv();"/>
							</div>
							</td>
						
						</tr>
						<tr>
							<td align="left">
							<div id ="city"></div>
							</td>
						
						</tr>
						<tr>
							<td>
								<label class="label">
									<s:text name="label.amount" />
								</label>
								<s:select name="creteria" list="#{'GT':'Greater Then Equal To (>=) ','EQ':'Equals To (=)','LT':'Less Then Equal To (<=) '}" cssClass="option" theme="simple" />
								<s:textfield name="amount" theme="simple" />
							</td>
						</tr>
						<tr>
						<td>
						<s:submit key="btn.submit" align="center"  cssClass="button"
						targets="down" theme="ajax" />
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
	$RCSfile: bo_rep_actInactInvStatus_reportMenu.jsp,v $ $Revision:
	1.1.1.1$
</code>