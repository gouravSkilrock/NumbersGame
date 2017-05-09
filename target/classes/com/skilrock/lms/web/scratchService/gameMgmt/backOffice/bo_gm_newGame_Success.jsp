<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<%@ taglib prefix="s" uri="/struts-tags"%>
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<%response.setDateHeader("Expires",
   System.currentTimeMillis() + 10*24*60*60*1000);%>
<head>



<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />

<link rel="stylesheet" href="<%=request.getContextPath() %>/LMSImages/css/styles.css" type="text/css"/>

<title><%=application.getAttribute("JSP_PAGE_TITLE")%></title>
	

	</head>
<body>

				<%@include file="/com/skilrock/lms/web/loginMgmt/menu.jsp"%>

	<div id="wrap">

  <div id="top_form">
   <h3><s:text name="label.basic.details.uploaded.successfully"/></h3>

		</div></div>
</body>





</html>
<code id="headId" style="visibility: hidden">
$RCSfile: bo_gm_newGame_Success.jsp,v $
$Revision: 1.3 $
</code>