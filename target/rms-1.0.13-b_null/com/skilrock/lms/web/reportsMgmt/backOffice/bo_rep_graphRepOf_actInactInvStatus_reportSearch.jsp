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
		<meta http-equiv="Content-Type"
			content="text/html; charset=utf-8" />

		<link rel="stylesheet"
			href="<%=request.getContextPath()%>/LMSImages/css/styles.css"
			type="text/css" />

		<title><%=application.getAttribute("JSP_PAGE_TITLE")%></title>
		
	</head>
	<body>
		<s:hidden id="chartData" value="%{chartData}" />
		<s:hidden id="chartType" value="%{chartType}" />
		<script>
			renderDrawables();
		</script>
					<div id='container'
						style='margin: 0 auto'></div>
	</body>
</html>
<code id="headId" style="visibility: hidden">
	$RCSfile: bo_rep_graphRepOf_actInactInvStatus_reportSearch.jsp,v $
	$Revision: 1.3 $
</code>