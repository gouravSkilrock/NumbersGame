<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@page import="com.skilrock.lms.common.utility.FormatNumber"%>
<%@page import="com.skilrock.lms.common.utility.GetDate"%>
<%
	response.setHeader("Cache-Control", "no-store"); //HTTP 1.1
	response.setHeader("Pragma", "no-cache"); //HTTP 1.0
	response.setDateHeader("Expires", 0); //prevents caching at the proxy server
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
<s:head theme="ajax" debug="false" />
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
<link rel="stylesheet"
	href="<%=request.getContextPath()%>/LMSImages/css/styles.css"
	type="text/css" />
<title><%=application.getAttribute("JSP_PAGE_TITLE")%></title>


</head>
<body>
	<div id="wrap">
		<div id="top_form">
		<s:if test="%{auditTrailBeans neq null and auditTrailBeans.size()>0}">
			<div align="center">
				<h3>
					<u>Audit Trail Report &nbsp; From : <s:property
						value="startDate" /> &nbsp;&nbsp;To&nbsp;:&nbsp; <s:property
						value="endDate" /> </u>
				</h3>
			</div>
			<div align="left">
				<s:form action="exportReportAsExcel.action" method="post"
					enctype="multipart/form-data">
					<s:hidden id="tableValue" name="reportData" />
					<s:hidden name="reportName" value="AUDIT_TRAIL_Report" />
					<s:submit value="Export As Excel"
						cssStyle="margin-right:-400px;font-size:15px" onclick="getData();" />
				</s:form>
			</div>
			<div id='tableDataDiv'>
				<table width="100%" border="1" cellpadding="3" cellspacing="0"
					bordercolor="#CCCCCC" align="center">
					<tr style="display:none;">
					<th align="center" colspan="6">
					Audit Trail Report</th>
					</tr>
					<tr style="display:none;">
					<th align="center" colspan="6">
					Duration - <s:property
						value="startDate" />&nbsp;&nbsp;to&nbsp;&nbsp; <s:property
						value="endDate" /></th>
					</tr>
					<tr style="display:none;">
					<th align="center" colspan="6">
					Username - <s:property value="user" /></th>
					</tr>
					<tr>
						<th align="center" colspan=6><s:property value="orgName" />
						</th>
					</tr>
					
					<tr>
						<th align="center">S.No</th>
						<th align="center">User Name</th>
						<th align="center">User Id</th>
						<th align="center">User Ip</th>
						<th align="center">Time</th>
						<th align="center">Activity</th>
					</tr>
					<s:iterator id="result" value="auditTrailBeans" status="status">
						<tr class="startSortable">
							<td><s:property value="%{#status.index+1}" />
							</td>
							<td><s:property value="userName" />
							</td>
							<td><s:property value="loginName" />
							</td>
							<td><s:property value="accessIp" />
							</td>
							<td><s:property value="accessTime" />
							</td>
							<td><s:property value="activity" />
							</td>
						</tr>
					</s:iterator>
				</table>
			</div>
			</s:if>
			<s:else>
			<center><h1></>NO RECORDS TO PROCESS</h1></center>
			</s:else>
		</div>
	</div>
</body>
</html>
<code id="headId" style="visibility: hidden"> $RCSfile:
	bo_rep_auditTrail_Search.jsp,v $ $Revision: 1.3 $ </code>