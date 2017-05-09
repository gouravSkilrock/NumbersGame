<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">

	<head>

		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />

		<link rel="stylesheet"
			href="<%=request.getContextPath()%>/LMSImages/css/styles.css"
			type="text/css" />

		<title><%=application.getAttribute("JSP_PAGE_TITLE")%></title>
		<s:head theme="ajax" debug="false" />
		<script>var path = "<%=request.getContextPath()%>";
	
</script>
		<script
			src="<%=request.getContextPath()%>/com/skilrock/lms/web/userMgmt/backOffice/js/dashBoard_new.js" />
		<script></script>

	</head>
	<body>
		<%@include file="/com/skilrock/lms/web/loginMgmt/menu.jsp"%>

		<div id="wrap">

			<div id="top_form">
				<h3>
					<s:text name="label.dashboard.back.ofc"/>
				</h3>


				<s:form name="dashBoard_new" id="dashBoard_new" method="POST">
					<div>
						<s:select name="orgType" id="orgType" key="label.org.type"
							list="#{'AGENT':#application.TIER_MAP.AGENT.toUpperCase()}"
							onchange="fetchActData(this.value);" headerKey="-1"
							headerValue="%{getText('label.please.select')}" cssClass="option" />
					</div>
					<div id="errDiv" />
				</s:form>

				<div id="reportDiv" style="display: none;">
				</div>
			</div>
		</div>

	</body>
</html>

<code id="headId" style="visibility: hidden">
	$RCSfile: bo_um_dashBoard_new_Menu.jsp,v $ $Revision: 1.3 $
</code>