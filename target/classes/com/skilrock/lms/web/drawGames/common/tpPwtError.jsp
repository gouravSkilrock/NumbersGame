<%@ page language="java" pageEncoding="ISO-8859-1"%>
<%@page import="com.skilrock.lms.dge.beans.PWTDrawBean"%>
<%@page import="com.skilrock.lms.web.drawGames.pwtMgmt.javaBeans.*"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html:html locale="true">
<head>
	<html:base />

	<title>pwtError.jsp</title>

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


	




	<%
	
	PwtVerifyTicketBean pwtBean = (PwtVerifyTicketBean) session.getAttribute("PWT_BEAN") ;
	System.out.println("Error Msg : " + pwtBean.getTicketStatus() +"/"+ pwtBean.getResponseMsg());
	System.out.println(pwtBean.getResponseCode());
	if(150 == pwtBean.getResponseCode()){
	System.out.println("Inside if");
	%>
	
	<script>
	alert("PWT LIMIT EXCEEDED.")
	</script>
	<%}else{
	System.out.println("Inside else");
	 %>
	<%= pwtBean.getResponseMsg() == null ? pwtBean.getTicketStatus() : pwtBean.getResponseMsg() %>
	<% } %>
	
</body>
</html:html>
