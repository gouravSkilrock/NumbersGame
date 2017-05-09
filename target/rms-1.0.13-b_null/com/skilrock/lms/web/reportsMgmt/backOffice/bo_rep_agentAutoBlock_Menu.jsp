<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@page import="com.skilrock.lms.common.utility.FormatNumber"%>
<%@page import="com.skilrock.lms.common.utility.GetDate"%>
<%
	response.setHeader("Cache-Control", "no-store"); //HTTP 1.1
	response.setHeader("Pragma", "no-cache"); //HTTP 1.0
	response.setDateHeader("Expires",  0);
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
	<head>
		<s:head theme="ajax" debug="false" />
		<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
		<link rel="stylesheet" href="<%=request.getContextPath()%>/LMSImages/css/styles.css" type="text/css" />
		<title><%=application.getAttribute("JSP_PAGE_TITLE")%></title>
		<script type="text/javascript" src="//ajax.googleapis.com/ajax/libs/jquery/1.9.1/jquery.min.js"></script>
		<script>
			$(document).ready(function() {
				$("#generateReport").click(function(e) {
					$("#generateReport").hide();
				});
			});

			function getData() {
				var tblData = document.getElementById("tableDataDiv").innerHTML;
				tblData = tblData.replace(document.getElementById("sortRow").innerHTML, "");
				document.getElementById('tableValue').value = tblData;
				return false;
			}
			function updateOrgStatus(orgId) {
				var currentStatus = (document.getElementById("currentStatusHide_"+orgId).value);
				var text = _ajaxCall(projectName+"/com/skilrock/lms/web/reportsMgmt/bo_rep_updateAgentStatus.action?orgId="+orgId+"&currentStatus="+currentStatus.toUpperCase());

				var updatedStatus = (currentStatus == 'ACTIVE') ? "INACTIVE" : "ACTIVE";
				document.getElementById("updateStatus_"+orgId).value = currentStatus;
				document.getElementById("currentStatusHide_"+orgId).value = updatedStatus;
				document.getElementById("currentStatus_"+orgId).innerHTML = updatedStatus;
			}
		</script>
	</head>
	<body>
		<%@include file="/com/skilrock/lms/web/loginMgmt/menu.jsp"%>
		<div id="wrap">
			<div id="top_form">
				<div align="center">
					<h3><s:text name="report.agent.auto.block" /></h3>
				</div>
				<center>
					<div id="reportDiv">
						<s:form action="bo_rep_agentAutoBlock_Details">
							<s:submit name="search" id="generateReport" key="Generate Report" align="right" targets="reportDiv" theme="ajax" cssClass="button" />
						</s:form>
					</div>
				</center>
			</div>
		</div>
	</body>
</html>
<code id="headId" style="visibility: hidden">
	$RCSfile: bo_rep_agentAutoBlock_Menu.jsp,v $ $Revision: 1.3 $
</code>