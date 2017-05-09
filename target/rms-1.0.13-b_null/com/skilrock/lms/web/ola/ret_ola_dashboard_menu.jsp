<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">

	<head>

		<meta http-equiv="Content-Type"
			content="text/html; charset=iso-8859-1" />

		<link rel="stylesheet"
			href="<%=request.getContextPath()%>/LMSImages/css/styles.css"
			type="text/css" />

		<title><%=application.getAttribute("JSP_PAGE_TITLE")%></title>
		<s:head theme="ajax" debug="false" />
		<script>var path = "<%=request.getContextPath()%>
	    </script>
		<script
			src="<%=request.getContextPath()%>/com/skilrock/lms/web/ola/js/playersDashboard.js" />
		<script></script>
	</head>
	<body>
		<%@include file="/com/skilrock/lms/web/loginMgmt/menu.jsp"%>

		<div id="wrap">
			<div id="top_form">
				<h3>
					<s:text name="Player Dashboard New" />
				</h3>
				<s:form name="dashBoardForPlayer" id="dashBoardForPlayer">
					<div>
						<s:select name="orgType" id="orgType" label="Org Type"
							list="{'PLAYERS'}" onchange="fetchActData(this.value);"
							headerKey="-1" headerValue="---Please Select---"
							cssClass="option" />
					</div>
					<div id="errDiv" />
				</s:form>

				<div id="reportDiv">
				</div>
			</div>
		</div>

	</body>
</html>
<code id="headId" style="visibility: hidden">
	$RCSfile: ret_ola_dashboard_menu.jsp,v $ $Revision: 1.2 $
</code>