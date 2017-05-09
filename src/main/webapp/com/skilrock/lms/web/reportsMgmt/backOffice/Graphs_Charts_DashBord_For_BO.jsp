<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<%
	response.setHeader("Expires", "0");
	// Set standard HTTP/1.1 no-cache headers.
	response.setHeader("Cache-Control",
			"no-store, no-cache, must-revalidate");
	// Set IE extended HTTP/1.1 no-cache headers (use addHeader).
	response.addHeader("Cache-Control", "post-check=0, pre-check=0");
	// Set standard HTTP/1.0 no-cache header.
	response.setHeader("Pragma", "no-cache");
%>
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
	<head>


		<meta http-equiv="cache-control" content="no-cache"></meta>
		<meta http-equiv="pragma" content="no-cache"></meta>
		<meta http-equiv="expires" content="0"></meta>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<link rel="stylesheet" href="<%=request.getContextPath()%>/LMSImages/css/styles.css" type="text/css" />
		<title><%=application.getAttribute("JSP_PAGE_TITLE")%></title>

		<link type="text/css" rel="stylesheet" href="<%=request.getContextPath()%>/LMSImages/css/lmsCalendar.css" media="screen" />

		<link type="text/css" rel="stylesheet" href="<%=request.getContextPath()%>/com/skilrock/lms/web/reportsMgmt/backOffice/graphs/css/overlay.css" media="screen" />

		<script>var projectName="<%=request.getContextPath()%>"</script>
		<script type="text/javascript" src="<%=request.getContextPath()%>/com/skilrock/lms/web/reportsMgmt/backOffice/graphs/js/DrawablesForLMSDashBoard.js"></script>

	</head>
	<body onload="renderDrawablesForDashBoard()">
		<%@include file="/com/skilrock/lms/web/loginMgmt/menu.jsp"%>
		<%@include file="/com/skilrock/lms/web/reportsMgmt/drawables/jsp/GraphsAndCharts.jsp"%>

		<s:hidden id="csvPath" value="%{csvPath}" />
		<s:hidden id="currencySymbol" value="%{currencySymbol}" />
		<s:hidden id="country" value="%{country}"></s:hidden>
		<div id="wrap">
			<div id="top_form">

				<table id="mainTable" cellpadding="3" cellspacing="10" width="400"
					style="width: 100%; border: 0px solid; margin-left: -102px; border-color: #FFFFFF;"
					align='left'>

				</table>

			</div>
		</div>
	</body>
</html>
<code id="headId" style="visibility: hidden">
	$RCSfile: Graphs_Charts_DashBord_For_BO.jsp,v $ $Revision: 1.1.2.3.2.1
	$
</code>