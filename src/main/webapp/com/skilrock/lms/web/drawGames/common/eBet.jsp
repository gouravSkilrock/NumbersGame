<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@page import="com.skilrock.lms.beans.UserInfoBean;"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
 <head>
	<meta charset="UTF-8">
	<title>Game</title>
	
	<link href="https://fonts.googleapis.com/css?family=Roboto:300,400,500,700" rel="stylesheet">
	<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/font-awesome/4.6.3/css/font-awesome.min.css" />
	<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/v-ebetslip.css" media="screen">
	<script type="text/javascript" src="<%=request.getContextPath()%>/com/skilrock/lms/web/common/globalJs/jquery-1.10.1.min.js"></script>
	
</head>
<body>
  <input type="hidden" id="userName" value="<%=((UserInfoBean)session.getAttribute("USER_INFO")).getUserName()%>">
  <input type="hidden" id="sessionId" value="<%=session.getId()%>">
	<div class="blockMainWrapper">
		<div class="blockWrapper clearfix">
			<!-- block start here-->
	
	
			
			</div>
			<!-- block end here-->
			
			

			
		</div>
	
</body>
<script type="text/javascript" src="<%=request.getContextPath()%>/com/skilrock/lms/web/common/globalJs/ajaxCall.js"></script>
<script>var path="<%=request.getContextPath()%>";</script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/eBetSlip.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/dateformat.js"></script>
<script>var currencySymbol ='<%=application.getAttribute("CURRENCY_SYMBOL")%>';</script>
<script>var organizationName ='<%=application.getAttribute("ORG_NAME_JSP")%>';</script>

</html>
