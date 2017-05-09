<%@page import="com.skilrock.lms.beans.DateBeans"%>
<%@page import="com.skilrock.lms.common.utility.GetDate"%>
<%@page import="com.skilrock.lms.common.utility.FormatNumber"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="com.skilrock.lms.common.utility.CommonMethods"%>
<%@page import="java.util.Calendar"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<%
	response.setHeader("Cache-Control", "no-store"); //HTTP 1.1
	response.setHeader("Pragma", "no-cache"); //HTTP 1.0
	response.setDateHeader("Expires", 0); //prevents caching at the proxy server
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
	<head>
		<s:head theme="ajax" debug="false" />
		<meta http-equiv="Content-Type"
			content="text/html; charset=iso-8859-1" />
		<link rel="stylesheet"
			href="<%=request.getContextPath()%>/LMSImages/css/styles.css"
			type="text/css" />
		<title><%=application.getAttribute("JSP_PAGE_TITLE")%></title>

		<link type="text/css" rel="stylesheet"
			href="<%=request.getContextPath()%>/LMSImages/css/lmsCalendar.css"
			media="screen" />
		<script>var projectName="<%=request.getContextPath()%>"</script>
		<script type="text/javascript"
			src="<%=request.getContextPath()%>/com/skilrock/lms/web/common/globalJs/calender.js"></script>
		<script>
	function getData() {
		var tblData = document.getElementById("tableDataDiv").innerHTML;
		tblData = tblData.replace(document.getElementById("sortRow").innerHTML,
				"");
		document.getElementById('tableValue').value = tblData;
		return false;
	}
</script>

	</head>
<body>
		<%@include file="/com/skilrock/lms/web/loginMgmt/menu.jsp"%>
		<div id="wrap">
			<div id="top_form">
			<h3>
					<s:text name="Agent"/> Wise Payout Center <s:text name="Report"/>
			</h3>

<s:if test="%{message!=null}">
<center><h1><s:label value="%{message}"/></h1></center>
</s:if>
<s:else>
<div align="left">
<s:form action="bo_rep_payOutCenterReportAgentWiseMenu_ExportToExcel.action"  method="post" enctype="multipart/form-data">
<s:hidden id="tableValue" name="valueToSend" />
<s:submit value="Export As Excel" cssStyle="margin-right:-400px;background:#77b3d7;font-size:15px" onclick="getData();"/>
</s:form>
</div>
<div id="tableDataDiv">
<table width="100%" border="1" cellpadding="3" cellspacing="0"
	bordercolor="#CCCCCC" align="center" id="expandTable"
	style="font-size: 12px">
		<tr align="center" >
			<th nowrap="nowrap">
				<s:text name="Agent" />	Name
			</th>
			<th nowrap="nowrap">
				Block Amount
			</th>
				<th nowrap="nowrap">
				Block Days
			</th>
				<th nowrap="nowrap">
				Block Action
			</th>
		</tr>	
		<s:iterator id="result" value="%{payoutListAgentWise}" status="taskIndex">
			<tr class="startSortable" style="white-space: nowrap;">
				<td align="left" nowrap="nowrap">
					<s:property value="%{organizationName}" />
				</td>
				<td align="left" nowrap="nowrap">
					<s:property value="%{blockAmt}" />
				</td>
				<td align="left" nowrap="nowrap">
					<s:property value="%{blockDays}" />
				</td>
				<td align="left" nowrap="nowrap">
					<s:property value="%{blockAction}" />
				</td>
		</tr>
		</s:iterator>
</table>
</div>
</s:else>
</div>
</div>

</body>
</html>


<code id="headId" style="visibility: hidden">
	$RCSfile: bo_rep_payOutCenterReportAgentWiseSearch.jsp,v $ $Revision: 1.3 $
</code>