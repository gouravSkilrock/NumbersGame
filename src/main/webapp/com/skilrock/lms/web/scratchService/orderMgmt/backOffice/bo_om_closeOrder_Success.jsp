<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%response.setDateHeader("Expires",
   System.currentTimeMillis() + 10*24*60*60*1000);%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>



<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />

<link rel="stylesheet" href="<%=request.getContextPath() %>/LMSImages/css/styles.css" scrolling="no" type="text/css" />

<title><%=application.getAttribute("JSP_PAGE_TITLE")%></title>

	</head>
<body>
<%@include file="/com/skilrock/lms/web/loginMgmt/menu.jsp" %>

<div id="wrap">

  <div id="top_form">
<h3>Order is SuccessFully Updated</h3>
</div></div>
</body>
</html><code id="headId" style="visibility: hidden">$Id: bo_om_closeOrder_Success.jsp,v 1.1 2010/04/02 12:12:10 gaurav Exp $</code>