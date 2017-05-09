<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="com.skilrock.lms.common.utility.CommonMethods"%>
<%@page import="java.util.Calendar"%>
<%@taglib prefix="s" uri="/struts-tags"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
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
			src="<%=request.getContextPath()%>/com/skilrock/lms/web/reportsMgmt/backOffice/js/ActInactReportjs.js"></script>
		<script type="text/javascript"
			src="<%=request.getContextPath()%>/com/skilrock/lms/web/reportsMgmt/backOffice/graphs/js/PrepareChartsAndGraphs.js"></script>
	</head>
	<body onload="populateCityList();">
		<%@include file="/com/skilrock/lms/web/loginMgmt/menu.jsp"%>
		<%@include
			file="/com/skilrock/lms/web/reportsMgmt/drawables/jsp/GraphsAndCharts.jsp"%>
		<div id="wrap">
			<div id="top_form">
				<h3>
					<b>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<s:text
							name="label.active.inactive.terminal" /> <s:text name="Report" />&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</b>
				</h3>

				<s:form>
					<s:hidden id="checkType"></s:hidden>
					<br />
					<br />
					<div id="dates"></div>
					<div id="errorDiv"></div>
					<table border="0" cellpadding="2" cellspacing="1" width="400"
						style="width: 100%; vertical-align: middle;" align='center'>
						<tr>
							<td align="right">
								<!--<s:label key="label.chart.type" value="label.chart.type" id="chartTypeLabel"
										required="true" theme="simple" />-->
								<s:text name="label.chart.type" /><font color='red'>*</font>
							</td>
							<td align="left">
								<s:radio name="chartType" id="chartType" key="label.chart.type"
									list="#{'line':getText('label.line'),'column':getText('label.column'),'pie':getText('label.pie')}"
									required="true" cssClass="option" onchange="checkChartType();"
									theme="simple" />
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
							<td>
							</td>
						</tr>
						<tr>
							<td align="right">
								<label class="label">
									<s:text name="label.start.date" />
									<font color='red'> <span>*</span></font>:&nbsp;
								</label>
							</td>
							<td align="left">
								<input type="text" name="startDate" id="startDate"
									value="<%=startDate%>" readonly size="12"
									onchange="_id.i('dates' ,'');">
									<input type="button"
										style="width: 19px; height: 19px; background: url('<%=request.getContextPath()%>/LMSImages/imagesCal/dateIcon.gif'); top left; border: 0;"
										onclick="displayCalendar(_id.o('startDate'),'yyyy-mm-dd', this, '<%=startDate%>', false, '<%=startDate%>')" />
							</td>
						</tr>

						<tr>
							<td align="right">
								<div id="endDateDiv">
									<label class="label">
										<s:text name="label.end.date" />
										<font color='red'> <span>*</span></font>:&nbsp;
									</label>
							</td>
							<td align="left">
								<input type="text" name="endDate" id="endDate"
									value="<%=startDate%>" readonly size="12"
									onchange="_id.i('dates' ,'');">
									<input type="button"
										style="width: 19px; height: 19px; background: url('<%=request.getContextPath()%>/LMSImages/imagesCal/dateIcon.gif'); top left; border: 0;"
										onclick="displayCalendar(_id.o('endDate'),'yyyy-mm-dd', this, '<%=startDate%>',false, '<%=startDate%>')" />
									</div>
							</td>
						</tr>
						<tr>
							<td align="right">
								<!--<s:label value="Report Type" id="reportTypeLabel"
									required="true" theme="simple" />
								-->
								<s:text name="label.report.type" /><font color='red'>*</font>
							</td>
							<td align="left">
								<s:select name="reportType" id="reportType" headerKey="-1"
									headerValue="-- Please Select --" list="#{'1':'REGION WISE'}"
									required="true" cssClass="option"
									onchange="populateCityList();" theme="simple" />
							</td>

						</tr>

						<tr>

							<td align="right">
								<div id="regionNameLabelDiv">
									<div id="regionNameLabelDiv">
										<!--<s:label value="Select Region" id="reagionNameLabel"
											required="true" theme="simple" />
										--><s:text name="label.slct.region" /><font color='red'>*</font>
									</div>
							</td>
							<td align="left">
								<div id="regionNameDiv">
									<s:select name="regionName" id="regionName" headerKey="-1"
										headerValue="-- Please Select --" list="{}" required="true"
										cssClass="option" theme="simple"
										onchange="createCities(this.value),clearDiv();" />
								</div>
							</td>

						</tr>
						<tr>
							<td align="right">
								<div id="cityNameLabelDiv">
									<!--<s:label value="Select City" id="cityNameLabel" required="true"
										theme="simple" />
									-->
									<s:text name="label.slct.city" /><font color='red'>*</font>
								</div>
							</td>
							<td align="left">
								<div id="cityNameDiv">
									<s:select name="cityName" id="cityName" headerKey="-1"
										headerValue="-- Please Select --" list="{}" required="true"
										cssClass="option" theme="simple"
										onchange="createZones(this.value);clearDiv();" />
								</div>
							</td>
						</tr>

						<tr>
							<td align="center" colspan="2">
								<div id="zones"></div>
							</td>

						</tr>
						<tr>
							<td>
								<s:hidden name="reportItem" id="reportItem"></s:hidden>
							</td>
						</tr>
						<tr>
							<td align="center" colspan="2">
								<input type="button" value="<s:text name="btn.submit" />"
									onclick="forSubmission('down')" />
							</td>

						</tr>
					</table>
				</s:form>
				<table border="1" id="GraphTable"
					style="display: none; width: 100%; border: 3px solid #F2F2F2">
					<tr>
						<th>
							<div id="down" style="text-align: center"></div>
						</th>
					</tr>
				</table>
			</div>
		</div>
	</body>
</html>
<code id="headId" style="visibility: hidden">
	$RCSfile: bo_rep_graphRepOf_actInactInvStatus_reportMenu.jsp,v $
	$Revision: 1.3 $
</code>