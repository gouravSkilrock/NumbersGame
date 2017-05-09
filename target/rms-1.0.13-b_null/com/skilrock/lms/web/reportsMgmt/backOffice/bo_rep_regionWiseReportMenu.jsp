<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="com.skilrock.lms.common.utility.CommonMethods"%>
<%@page import="java.util.Calendar"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.util.Date"%>
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
		<script type="text/javascript"	src="<%=request.getContextPath()%>/com/skilrock/lms/web/common/globalJs/calender.js"></script>
		<script>var path="<%=request.getContextPath()%>"</script>
		<script type="text/javascript"
			src="<%=request.getContextPath()%>/com/skilrock/lms/web/common/globalJs/jquery-1.10.1.min.js"></script>
		<script type="text/javascript"	src="<%=request.getContextPath()%>/com/skilrock/lms/web/reportsMgmt/backOffice/js/bo_rep_stateWise.js"></script>

	</head>

	<body >
		<%@include file="/com/skilrock/lms/web/loginMgmt/menu.jsp"%>
		<div id="wrap">
			<div id="top_form">
				<h3>
				<s:text name="State"/> <s:text name="label.wise.dg"/> <s:text name="Report"/>
				</h3>


				<s:form name="repForm" id="repForm" action="bo_rep_RegionDataSearch" onsubmit="return validateDates();" method="post">

					<table border="0" cellpadding="2" cellspacing="0" width="400" style="width:100%;vertical-align:middle; ">
						<tr>
							<td align="left" colspan="2">
								<div id="error"></div>
							</td>
						</tr>
						<tr>
						<td colspan="2" align="left">
						
						<table> <s:radio key="label.choose.report.type" name="repType" id="repType" list="#{'DW':getText('label.drawwise'),'DT':getText('label.datewise')}" value="#{'DW'}" /></table>
						
						</td >
						</tr>
						<tr>
							<td colspan="2">
							&nbsp; 	<s:text name="label.game.name"/> &nbsp; &nbsp;&nbsp; &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; :
								<s:select name="gameId" id="gameId" theme="simple" headerKey="-1" headerValue="%{getText('label.please.select')}"
									 list="#session.DRAWGAME_LIST"
									cssClass="option"/>
							</td>
						</tr>
						<tr>
							<td colspan="2">
								<div id="dates"></div>
							</td>
						</tr>
						<tr>
							<td colspan="2">
								<table cellpadding="3"
									cellspacing="0" border="0">
									<s:set name="stDate" id="stDate" value="#session.presentDate" />
									<%
										Calendar prevCal = Calendar.getInstance();
										String startDate = CommonMethods.convertDateInGlobalFormat(new java.sql.Date(prevCal.getTimeInMillis()).toString(), "yyyy-mm-dd", "yyyy-mm-dd");

										SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
										Date archivingDate = dateFormat.parse(CommonMethods.getLastArchDate());
										Calendar archCal = Calendar.getInstance();
										archCal.setTime(archivingDate);
										archCal.add(Calendar.DATE, 1); 
										String lastDate = dateFormat.format(archCal.getTime());
									%>
									<tr>
										<td>
											<label class="label">
												<s:text name="label.start.date"/>
						                           &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;:&nbsp;
											</label>
											<input type="text" name="startDate" id="startDate"
												value="<%=startDate%>" readonly size="12"  onchange="fetchDrawNames()">
											<input type="button"
												style="width: 19px; height: 19px; background: url('<%=request.getContextPath()%>/LMSImages/imagesCal/dateIcon.gif'); top left; border: 0;"
												onclick="displayCalendar(document.getElementById('startDate'),'yyyy-mm-dd', this, '<%=startDate%>', '<%=lastDate%>', '<%=startDate%>')" />
										</td>
									</tr>
									<tr>
										<td>
											<label class="label">
												<s:text name="label.end.date"/>
											&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;:&nbsp;
											</label>
											<input type="text" name="endDate" id="endDate"
												value="<%=startDate%>" readonly size="12"  onchange="fetchDrawNames()">
											<input type="button"
												style="width: 19px; height: 19px; background: url('<%=request.getContextPath()%>/LMSImages/imagesCal/dateIcon.gif'); top left; border: 0;"
												onclick="displayCalendar(document.getElementById('endDate'),'yyyy-mm-dd', this, '<%=startDate%>', '<%=lastDate%>', '<%=startDate%>')" />
										</td>
									</tr>
									<tr>
							<td colspan="2">
								<div id="drawNameDiv" style="display: none;">
									<s:text name="label.draw.name"/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;: 
									<s:select name="drawName" theme="simple" id="drawName" 
										headerKey="-1" headerValue="%{getText('label.ALL')}" list="{}" cssClass="option"
										onchange="_id.i('down','');_id.i('result','');" />
								</div>
							</td>
						</tr>
								</table>
							</td>
						</tr>
	                   <tr style="display: none;">
										<td align="right">
											<label class="label">
												<s:text name="label.country"/>
												<span class="required">*</span>:
											</label>
										</td>
										<td>
											<s:select theme="simple" key="label.country" id="country"
												name="Country" headerKey="-1"
												headerValue="%{getText('label.please.select')}" list="{}"
												cssClass="option" required="true" />
										</td>
									</tr>
								
									<tr>
									
									
										<td colspan="2">
										
											<label class="label"><s:text name="label.select"/>
												<s:text name="State"></s:text>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;:
														</label>
										
										
											<s:select theme="simple"  id="stateCode"
												name="stateCode" headerKey="-1"
												headerValue="%{getText('label.ALL')}" list="{}"
												cssClass="option" required="true" />
										</td>
										
										
										
									</tr>
						<tr>
							<td colspan="2">
								<s:submit name="search" key="btn.srch" align="center"
									targets="down" theme="ajax" cssClass="button" />
							</td>
						</tr>

					</table>
				</s:form>

				<div id="down" style="text-align: center"></div>
				<div id="result" style="text-align: center"></div>
				<br />
				<br />
			</div>
		</div>

	</body>
</html>

<code id="headId" style="visibility: hidden">
	$RCSfile: bo_rep_regionWiseReportMenu.jsp,v $ $Revision: 1.3 $
</code>