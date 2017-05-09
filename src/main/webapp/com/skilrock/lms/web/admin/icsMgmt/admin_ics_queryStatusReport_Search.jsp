<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%
	response.setHeader("Cache-Control", "no-store"); //HTTP 1.1
	response.setHeader("Pragma", "no-cache"); //HTTP 1.0
	response.setDateHeader("Expires", 0); //prevents caching at the proxy server
%>

<s:form action="admin_ics_queryStatusReport_Mail" theme="simple">
	<div id="mailErrorDiv" style="color:red;"></div>
	<s:textfield name="mailToNameList" id="mailToNameList" />
	<s:submit value="Send Mail" onclick="return validateMailTo();" />
</s:form>

<table width="100%" border="1" cellpadding="2" cellspacing="0"
	bordercolor="#CCCCCC" align="center" valign="top" id="searchTable">
	<tr>
		<th>S. No.</th>
		<th>Query ID</th>
		<th>Query Title</th>
		<th>Query Description</th>
		<th>Expected Result</th>
		<th>Actual Result</th>
		<th>ICS Run Date</th>
		<th>Run By</th>
	</tr>
	<s:iterator value="#session.ICS_REPORT_LIST" status="taskIndex">
		<tr>
			<td><s:property value="#taskIndex.index+1"/></td>
			<td><s:property value="queryId"/></td>
			<td><s:property value="queryTitle"/></td>
			<td><s:property value="queryDescription"/></td>
			<td><s:property value="expectedResult"/></td>
			<td><s:property value="actualResult"/></td>
			<td><s:property value="icsRunDate"/></td>
			<td><s:property value="runBy"/></td>
		</tr>
	</s:iterator>
</table>

<code id="headId" style="visibility: hidden">
	$RCSfile: admin_ics_queryStatusReport_Search.jsp,v $ $Revision: 1.1.2.3 $
</code>