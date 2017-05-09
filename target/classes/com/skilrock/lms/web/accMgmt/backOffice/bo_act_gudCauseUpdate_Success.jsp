	<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
	<%@ taglib prefix="s" uri="/struts-tags"%>	
	<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
	<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
		<link rel="stylesheet" href="<%=request.getContextPath() %>/LMSImages/css/styles.css" type="text/css"/>
		<title><%=application.getAttribute("JSP_PAGE_TITLE")%></title>
		<script>
		path ="<%=request.getContextPath() %>";
		</script>

	</head>

<body >

	<%@include file="/com/skilrock/lms/web/loginMgmt/menu.jsp" %>
	<div id="wrap">
    <div id="top_form">
 	<h5><s:text name="Good_Cause"/> Rate Updated Successfully.</h5> <br />
 	<s:a href="bo_act_gudCauseUpdate_Menu.action">update  <s:text name="Good_Cause"/> Rate for Another Game</s:a>
 	
 	
	 </div></div>	
	
	
</body>
</html>
<code id="headId" style="visibility: hidden">
$RCSfile: bo_act_gudCauseUpdate_Success.jsp,v $
$Revision: 1.2 $
</code>