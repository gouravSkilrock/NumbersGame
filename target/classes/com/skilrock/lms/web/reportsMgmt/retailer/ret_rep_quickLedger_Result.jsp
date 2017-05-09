<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html>
<%
		response.setHeader("Cache-Control", "no-store"); //HTTP 1.1
		response.setHeader("Pragma", "no-cache"); //HTTP 1.0
		response.setDateHeader("Expires", 0); //prevents caching at the proxy server
%>
<head>
	<s:head theme="ajax"/>
</head>
<body>
<br/>
	<div align="center">
		<h3>
			<u>Ledger For&nbsp;<% out.println(": "+ (String)session.getAttribute("ledgerType"));%></u>
		</h3>
	</div>	
	<table width="100%" border="0" cellpadding="3" cellspacing="0" bordercolor="#CCCCCC" align="center">
		<tr>
	 		<td>
	 			<iframe name="ledgerFrame" id="ledgerFrame" frameborder="0" width="230px" height="360px" src="<%=request.getContextPath() %>/com/skilrock/lms/web/reportsMgmt/ledgerIframeAction.action"></iframe>
	 		</td>
	 	</tr>
	</table>
</body>
</html>
<code id="headId" style="visibility: hidden">
$RCSfile: ret_rep_quickLedger_Result.jsp,v $
$Revision: 1.2 $
</code>