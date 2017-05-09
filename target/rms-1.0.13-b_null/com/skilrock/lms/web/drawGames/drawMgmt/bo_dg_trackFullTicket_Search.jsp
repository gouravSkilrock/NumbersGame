<%@ page contentType="text/html; charset=ISO-8859-1"
	pageEncoding="UTF-8"%>
<%@page import="com.skilrock.lms.common.utility.FormatNumber"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<HTML xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
	<head>
		<meta http-equiv="Content-Type"
			content="text/html; charset=iso-8859-1" />
		<link rel="stylesheet"
			href="<%=request.getContextPath()%>/LMSImages/css/styles.css"
			type="text/css" />
		<title><%=application.getAttribute("JSP_PAGE_TITLE")%></title>
		<s:head theme="ajax" debug="false" />
	</head>
	<body>
		<s:if test="%{ticketBean.status eq 'MAX_ATTEMP_REACHED'}">
			<center><h3 style="color:red; ">Maximum Attempts Limit for Track Ticket is Reached.</h3></center>
		</s:if>
		<s:elseif test="%{ticketBean.status eq 'INVALID_TICKET_NUMBER'}">
			<center><h3 style="color:red; ">Invalid Ticket Number.</h3></center>
		</s:elseif>
		<s:elseif test="%{ticketBean.status eq 'INVALID_TICKET_LENGTH'}">
			<center><h3 style="color:red; ">Please Enter 13 to 14 Digits Only.</h3></center>
		</s:elseif>
		<s:elseif test="%{ticketBean.status eq 'INVALID_TICKET_LENGTH_NEW'}">
			<center><h3 style="color:red; ">Please Enter 14 Digits of New Ticket Number.</h3></center>
		</s:elseif>
		<s:elseif test="%{ticketBean.status eq 'TICKET_NOT_EXIST'}">
			<center><h3 style="color:red; ">Sorry, Ticket Not Exist.</h3></center>
		</s:elseif>
		<s:elseif test="%{ticketBean.status eq 'SUCCESS'}">
			<table border="1" cellpadding="4" cellspacing="0" width="40%"
				align="center" bordercolor="#cccccc">
				<tr>
					<th style="background: #eee">Ticket Number</th>
					<td><s:property value="ticketBean.ticketNumber" /><s:property value="ticketBean.reprintCount" /></td>
				</tr>
			</table>
		</s:elseif>
	</body>
</HTML>
<code id="headId" style="visibility: hidden">
	$RCSfile: bo_dg_trackFullTicket_Search.jsp,v $ $Revision: 1.3 $
</code>