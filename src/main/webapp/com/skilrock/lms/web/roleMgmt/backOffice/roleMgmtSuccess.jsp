	<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
	<%@ taglib prefix="s" uri="/struts-tags"%>
	
	<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
	
	<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
	
	<head>
	
	
	
	<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
	
	<link rel="stylesheet" href="<%=request.getContextPath() %>/LMSImages/css/styles.css"  type="text/css"/>
	
	<title><%=application.getAttribute("JSP_PAGE_TITLE")%></title>
	</head>
		<body>
		<%@include file="/com/skilrock/lms/web/loginMgmt/menu.jsp"%>

	<div id="wrap">

  <div id="top_form">			
<h3>Sub User With User Name: <font color=red><s:property value="#session.USER_DETAILS.userName"/></font> has been created successfully</h3>
<h5>User's Password has been sent to User's Email ID.</h5>


</div></div>
</body>
</html><code id="headId" style="visibility: hidden">$Id: roleMgmtSuccess.jsp,v 1.1 2010/04/01 04:23:07 gaurav Exp $</code>