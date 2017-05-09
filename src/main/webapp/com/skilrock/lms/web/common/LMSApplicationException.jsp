<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
	<head>
		<meta http-equiv="CACHE-CONTROL" content="NO-CACHE" />
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<link rel="stylesheet" href="<%=request.getContextPath() %>/LMSImages/css/styles.css"  type="text/css"/>
		<title><%=application.getAttribute("JSP_PAGE_TITLE")%></title>	
	</head>
	<body>
	      <div class="txt3" align="center" style="font-size: 14px"><br/><b>
				<%=request.getAttribute("LMS_EXCEPTION")%></b>
	      </div>
	</body>
	
</html>
<code id="headId" style="visibility: hidden">
$RCSfile: LMSApplicationException.jsp,v $
$Revision: 1.3 $
</code>