<%@page import="com.skilrock.lms.common.Utility"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<base href="<%=basePath%>">

<title>My JSP 'payPWTTicket.jsp' starting page</title>

<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">
<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
<meta http-equiv="description" content="This is my page">
<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->

</head>
	<body>
		 <div id="wrap">
		 	<div id="top_form">
				<s:if test="verifyTicketResponseBean.errorCode == 0">
					<br/>
					<center><b><h3><s:label>Please Pay <s:property value="winningAmt"/> <s:property value="%{#application.CURRENCY_SYMBOL}" /> To Player</s:label></h3></b></</center>
					<%-- <center><b><h3><s:label>Please Pay <s:property value="winningAmt"/><s:property value="<%Utility.getPropertyValue("CURRENCY_SYMBOL"); %>"/> To Player</s:label></h3></b></</center> --%>
				</s:if>
				<s:else>
					<center><font color="red"><s:label><s:property value="verifyTicketResponseBean.errorMsg"/></s:label></font></center>
				</s:else>
			</div>
		</div>
	</body>
</html>