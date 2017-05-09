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
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<link rel="stylesheet"
			href="<%=request.getContextPath()%>/LMSImages/css/styles.css"
			type="text/css" />
		<title><%=application.getAttribute("JSP_PAGE_TITLE")%></title>
		<link type="text/css" rel="stylesheet"
			href="<%=request.getContextPath()%>/LMSImages/css/lmsCalendar.css"
			media="screen" />

		<script>
			var projectName="<%=request.getContextPath()%>"
		</script>
		<script type="text/javascript"
			src="<%=request.getContextPath()%>/com/skilrock/lms/web/common/globalJs/calender.js"></script>

	</head>
	<body>
		<%@include file="/com/skilrock/lms/web/loginMgmt/menu.jsp"%>

		<div id="wrap">
			<div id="top_form">
				<h3>
					<s:text name="label.sale" />
					<s:text name="Report" />
				</h3>
				<s:form action="bo_rep_lsSaleReport_result">
					<table width="450" border="0" cellpadding="2" cellspacing="0">
						<tr>
							<td align="center" colspan="2">
								<div id="errorDiv"></div>
							</td>
						</tr>
						<%
							Calendar prevCal = Calendar.getInstance();
								String currentDate = CommonMethods.convertDateInGlobalFormat(new java.sql.Date(prevCal.getTimeInMillis()).toString(), "yyyy-mm-dd","yyyy-mm-dd");
						%>
						<s:set name="stDate" id="stDate" value="#session.presentDate" />

						<tr>
							<td class="tdLabel">
								<label class="label">
									<s:text name="label.start.date" />
									<span>*</span>:&nbsp;
								</label>
							</td>
							<td>
								<input type="text" name="startDate" id="start_date"
									onchange="fetchDrawNames()"
									value="<s:property value="#session.presentDate"/>" readonly
									size="12">
									<input type="button"
										style="width: 19px; height: 19px; background: url('<%=request.getContextPath()%>/LMSImages/imagesCal/dateIcon.gif'); top left; border: 0;"
										onclick="displayCalendar(document.getElementById('start_date'),'yyyy-mm-dd', this,'<%=currentDate%>',null ,'<%=currentDate%>')" />
							</td>
						</tr>
						<tr>
							<td class="tdLabel">
								<label class="label">
									<s:text name="label.end.date" />
									<span>*</span>:&nbsp;
								</label>
							</td>
							<td>
								<input type="text" name="endDate" id="end_date"
									onchange="fetchDrawNames()"
									value="<s:property value="#session.presentDate"/>" readonly
									size="12">
									<input type="button"
										style="width: 19px; height: 19px; background: url('<%=request.getContextPath()%>/LMSImages/imagesCal/dateIcon.gif'); top left; border: 0;"
										onclick="displayCalendar(document.getElementById('end_date'),'yyyy-mm-dd', this,'<%=currentDate%>',null , '<%=currentDate%>')" />
							</td>
						</tr>
						<tr>
							<td>
								<s:submit name="search" key="btn.srch" align="right"
									targets="down" theme="ajax" cssClass="button" />
							</td>
						</tr>
					</table>
				</s:form>
				<div id="down"></div>
				<div id="result" style="overflow-x: auto; overflow-y: hidden;"></div>
			</div>
		</div>
	</body>
</html>

<code id="headId" style="visibility: hidden">
	$RCSfile: bo_rep_saleLsReport_menu.jsp,v $ $Revision:
	1.1.2.8.8.2.2.2.16.3 $
</code>