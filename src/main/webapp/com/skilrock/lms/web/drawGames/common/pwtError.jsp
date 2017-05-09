<%@ page language="java" pageEncoding="ISO-8859-1"%>
<%@page import="com.skilrock.lms.dge.beans.PWTDrawBean"%>
<%@page import="com.skilrock.lms.dge.beans.MainPWTDrawBean"%>

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
	MainPWTDrawBean pwtDrawBean = (MainPWTDrawBean) session.getAttribute("PWT_RES");
	
	if ("ERROR".equalsIgnoreCase(pwtDrawBean.getStatus())) {
	%>
		<b>Invalid Ticket</b>
	<%
	} else if (pwtDrawBean.getStatus().equalsIgnoreCase("FRAUD")) {
	%>
		<b>Invalid Winning limit Exceed</b>
		<script>var errMsg="Invalid Winning limit Exceed"</script>
	<%
	} else if (!pwtDrawBean.isValid()) {
	%>
		<b> Invalid Ticket</b>
	<%
	} else if (pwtDrawBean.getStatus().equalsIgnoreCase("UN_AUTH")) {
	%>
		<b> Unauthorized <s:property value="#application.TIER_MAP.RETAILER" /> for this ticket</b>
	<%
	} else if (pwtDrawBean.getStatus().equalsIgnoreCase("OUT_VERIFY_LIMIT")) {
	%>
		<b> Amount is out of verification limit. Please go to higher end.</b>
	<%
	} else if (pwtDrawBean.getStatus().equalsIgnoreCase("TICKET_EXPIRED")) {
	%>
		<b>Expired or Invalid Ticket</b>
	<%
	} else if (pwtDrawBean.getStatus().equalsIgnoreCase("PWT_LIMIT_EXCEED")) {
	%>
		<b>Winning Limit Exceed</b>
		<script>var errMsg="Invalid Winning limit Exceed"</script>
	<%
	}
	else if (pwtDrawBean.getStatus().equalsIgnoreCase("HIGH_PRIZE")) {
	%>
		<b>Ticket has High Prize Amount. Please go to higher end.</b>
	<%
	} else if (pwtDrawBean.getStatus().equalsIgnoreCase("OUT_PAY_LIMIT")) {
	%>
		<b>Amount is out of Payment limit. Please go to higher end. </b>
	<%
	}%>
	
	<script>
		if(typeof errMsg!="undefined"){
			parent.displayErrMsg(errMsg);
		}else{
			parent.displayErrMsg();
		}
	</script>
</body>
</html:html>
