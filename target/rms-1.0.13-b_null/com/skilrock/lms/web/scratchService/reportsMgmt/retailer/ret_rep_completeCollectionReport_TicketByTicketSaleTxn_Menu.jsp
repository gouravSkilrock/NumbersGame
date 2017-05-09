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
			content="text/html; charset=iso-8859-1" />
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
		<script type="text/javascript"
			src="<%=request.getContextPath()%>/com/skilrock/lms/web/reportsMgmt/backOffice/js/report.js"></script>
	</head>
	<body>
		<%@include file="/com/skilrock/lms/web/loginMgmt/menu.jsp"%>
	

		<div id="wrap">
			<div id="top_form">
				<h3>
					Scratch Sale <s:text name="Winning"/> <s:text name="Collection" /> <s:text name="Report"/>
				</h3>
				<s:form action="ret_rep_TicketByTicketSaleTxn_Search" onsubmit="return validateDates()">
					<table width="450" border="0" cellpadding="2" cellspacing="0">
						<tr>
							<td align="center" colspan="2">
								<div id="errorDiv"></div>
							</td>
						</tr>
						<tr>
							<td align="center" colspan="2">
							<s:hidden name="agtOrgName" value="%{#session.USER_INFO.orgCode}"></s:hidden>
							<s:hidden name="reportType" value="%{#application.TIER_MAP.RETAILER + ' Wise Expand'}"></s:hidden>
							<s:hidden name="agtId" value="%{#session.USER_INFO.userOrgId}"></s:hidden>
							</td>
						</tr>
						<tr>
							<td>
								<div id="dates"></div>
							</td>
						</tr>
						<tr>
							<td align="center" colspan="2">
								<%
									Calendar prevCal = Calendar.getInstance();
										String startDate = CommonMethods
												.convertDateInGlobalFormat(new java.sql.Date(prevCal
														.getTimeInMillis()).toString(), "yyyy-mm-dd",
														(String) session.getAttribute("date_format"));
								%>
								<s:hidden name="curDate" id="curDate" value="<%=startDate%>"></s:hidden>
								<div id="date"
									style="display: block; text-align: left; width: 60%">
									<s:div id="newDates" theme="ajax"
										href="rep_common_fetchDate.action">
									</s:div>
								</div>
							</td>

						</tr>
						<tr>
							<td>
								<s:submit name="search" value="Search" align="right"
									targets="down" theme="ajax" cssClass="button"
									/>
							</td>
						</tr>
					</table>
				</s:form>
				<div id="down"></div>
				<div id="result"></div>
			</div>
		</div>
		<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery.dataTables.js"></script>
	</body>
</html>

<code id="headId" style="visibility: hidden">
$RCSfile: agt_rep_completeCollectionReport_TicketByTicketSaleTxn_Menu.jsp,v $
$Revision: 1.2 $
</code>