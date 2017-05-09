<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="java.util.Calendar"%>
<%@page import="com.skilrock.lms.common.utility.CommonMethods"%>
<%@page import="com.skilrock.lms.common.Utility"%>
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
		<script>var projectName="<%=request.getContextPath()%>" </script>
		<link rel="stylesheet" href="<%=request.getContextPath()%>/LMSImages/css/styles.css" type="text/css" />
		<title><%=application.getAttribute("JSP_PAGE_TITLE")%></title>
		<script type="text/javascript" src="<%=request.getContextPath() %>/com/skilrock/lms/web/reportsMgmt/backOffice/js/report.js"></script>
		<script src="<%=request.getContextPath()%>/com/skilrock/lms/web/common/globalJs/jquery-1.10.1.min.js"></script>
		<link type="text/css" rel="stylesheet" href="<%=request.getContextPath()%>/LMSImages/css/lmsCalendar.css" media="screen" />
		<script type="text/javascript" src="<%=request.getContextPath()%>/com/skilrock/lms/web/common/globalJs/calender.js"></script>
		<script>
			var projectName="<%=request.getContextPath()%>"
			$(document).ready(function() {
					
			});

			var validateForm = function() {
				$("#errorDiv").html("");
				$("#resultDiv").html("");

				if($("#boUserId option:selected").val() == -1) {
					$("#errorDiv").html("Please Select Any User");
					return false;
				}
				if($("#serviceCode option:selected").val() == -1) {
					$("#errorDiv").html("Please Select Service");
					return false;
				}

				return true;
			};
			
			function getData(mainD, valueD, tabD) {			
						var tblData ="";				
						tblData += "<span><b>Report Name : </b></span>"+ $("#reportName").val() + "</br>";
						tblData += "<span><b>Start Date : </b></span>"+ $("#start_date").val() + "</br>"; 
					 	tblData += "<span><b>End Date : </b></span>"+ $("#endDate").val() + "</br>";
					 	tblData += document.getElementById(mainD).innerHTML;
					 	tblData += document.getElementById(tabD).innerHTML;
						if(document.getElementById("sortRow") != null) {
							tblData = tblData.replace(document.getElementById("sortRow").innerHTML, "");
						}
						document.getElementById(valueD).value = tblData;
						return true;
					}
			
			
			
		</script>
	</head>
	<body>
		<%@include file="/com/skilrock/lms/web/loginMgmt/menu.jsp"%>
		<div id="wrap">
			<div id="top_form">
				<div align="left">
					<h3>Privilege Modification Report</h3>
				</div>
					<s:form action="bo_um_priviledgeModificationReport_Search" id="privModifyForm" onsubmit="return validateForm();" theme="simple">
						<table>
							<tr>
								<td colspan="2">
									<div id="errorDiv" style="color:red;"></div>
								</td>
							</tr>
							<tr>
								<td>User Name</td>
								<td><s:select name="boUserId" id="boUserId" list="boUserMap" listKey="key" listValue="%{value}" headerKey="-1" headerValue="--Please Select--" cssClass="option" /></td>
							</tr>
							<tr>
								<%
									Calendar prevCal = Calendar.getInstance();
									String startDate = CommonMethods.convertDateInGlobalFormat(new java.sql.Date(prevCal.getTimeInMillis()).toString(), Utility.getPropertyValue("date_format"), Utility.getPropertyValue("date_format"));
								%>
								<td>
									<label class="label">
										<s:text name="label.start.date"/><font color='red'><span>*</span></font>:&nbsp;
									</label>
								</td>
								<td>
									<input type="text" name="startDate" id="start_date" value="<%=startDate%>" readonly size="12" />
									<input type="button" style="width: 19px; height: 19px; background: url('<%=request.getContextPath()%>/LMSImages/imagesCal/dateIcon.gif'); top left; border: 0;" onclick="displayCalendar(document.getElementById('start_date'),'yyyy-mm-dd', this, '<%=startDate%>', '<s:property value="%{deploymentDate}"/>', '')" />
								</td>
							</tr>
							<tr>
								<td>
									<label class="label">
										<s:text name="label.end.date"/><font color='red'><span>*</span></font>:&nbsp;
									</label>
								</td>
								<td>
									<input type="text" name="endDate" id="endDate" value="<%=startDate%>" readonly size="12" onchange="showHideData();" />
									<input type="button" style="width: 19px; height: 19px; background: url('<%=request.getContextPath()%>/LMSImages/imagesCal/dateIcon.gif'); top left; border: 0;" onclick="displayCalendar(_id.o('endDate'),'yyyy-mm-dd', this, '<%=startDate%>',false, '<%=startDate%>')" />
								</td>
							</tr>
							<tr>
								<td>Select Service</td>
								<td><s:select name="serviceCode" id="serviceCode" list="serviceMap" listKey="key" listValue="%{value}" headerKey="-1" headerValue="--Please Select--" cssClass="option" /></td>
							</tr>
							<tr>
								<td>&nbsp;</td>
								<td><s:submit key="Search" align="right" targets="resultDiv" theme="ajax" cssClass="button" /></td>
							</tr>
						</table>
					</s:form>
				<div id="resultDiv"></div>
			</div>
		</div>
	</body>
</html>
<code id="headId" style="visibility: hidden">
	$RCSfile: bo_rep_priviledgeModificationReport_Menu.jsp,v $ $Revision: 1.3 $
</code>