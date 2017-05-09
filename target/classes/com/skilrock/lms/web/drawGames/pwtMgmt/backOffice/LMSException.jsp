<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
	<head>
		<meta http-equiv="CACHE-CONTROL" content="NO-CACHE" />
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<link rel="stylesheet" href="<%=request.getContextPath() %>/LMSImages/css/styles.css"  type="text/css"/>
		<title><%=application.getAttribute("JSP_PAGE_TITLE")%></title>	
		<script type="text/javascript" src="<%=request.getContextPath() %>/com/skilrock/lms/web/drawGames/pwtMgmt/js/pwtTktValidation.js"></script>
		<s:head theme="ajax" debug="false"/>	
	</head>
	<body><br><br>
	      <div class="txt3" align="center" style="font-size: 14px"><b>
				<%=request.getAttribute("LMS_EXCEPTION")%></b>
	      </div>
	</body>
	
</html>
<code id="headId" style="visibility: hidden">
$RCSfile: LMSException.jsp,v $
$Revision: 1.3 $
</code>