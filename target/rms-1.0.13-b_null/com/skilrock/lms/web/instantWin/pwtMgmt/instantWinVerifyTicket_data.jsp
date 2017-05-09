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

<title>My JSP 'instantWinVerifyTicket_data.jsp' starting page</title>

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
		<s:if test="%{verifyTicketResponseBean.errorCode != 0}">
			<s:if test="%{verifyTicketResponseBean.errorCode == -1}">
				<center><b><s:label><s:property value="verifyTicketResponseBean.errorMsg"/></s:label></b></center>
			</s:if>
			<s:else>
				<center><font color="red"><s:label><s:property value="verifyTicketResponseBean.errorMsg"/></s:label></font></center>
			</s:else>
		</s:if>
		<s:else>
			<br />
			<br />
			<s:form id="payform" action="payPwtTicket">
				<s:hidden id="ticketNbr" name="ticketNbr" value="%{verifyTicketResponseBean.tktNbr}"></s:hidden>
				<s:hidden id="winningAmt" name="winningAmt" value="%{verifyTicketResponseBean.winningAmt}"></s:hidden>
				<table width="100%" border="1" cellpadding="3" cellspacing="0" bordercolor="#CCCCCC" align="center" style="font-size: 12px">
					<tr>
						<th colspan="5" align="center">Ticket Data</th>
					</tr>
					<tr>
						<th>Ticket Number</th>
						<th>Purchased Time</th>
						<th>Purchased Channel</th>
						<!-- <th>Ticket Data</th> -->
						<th>Ticket Status</th>
						<th>Winning Amount</th>
					</tr>
					<tr>
						<td><s:property value="verifyTicketResponseBean.tktNbr" /></td>
						<td><s:property value="verifyTicketResponseBean.purchaseTime" /></td>
						<td><s:property value="verifyTicketResponseBean.purchasedFrom" /></td>
						<%-- <td><s:property value="verifyTicketResponseBean.tktData" /></td> --%>
						<td><s:property value="verifyTicketResponseBean.tktStatus" /></td>
						<td style="right"><s:property value="verifyTicketResponseBean.winningAmt" /></td>
					</tr>
				</table>
				<br />
				<br />
				<s:if test="verifyTicketResponseBean.paymentAllowed == true">
					<!-- <td > -->
					<div>
						<div style="margin-left:320px;">
							<s:submit formId="payform" id="payWinning" key="btn.pay.winning.amt" align="centre" cssClass="button" targets="result" theme="ajax" />
						</div>
					</div>
				</s:if>
			</s:form>
		</s:else>
	</div>
</body>
</html>

<code id="headId" style="visibility: hidden"> $RCSfile:
	instantWinVerifyTicket_data.jsp,v $ $Revision: 1.3 $ </code>
