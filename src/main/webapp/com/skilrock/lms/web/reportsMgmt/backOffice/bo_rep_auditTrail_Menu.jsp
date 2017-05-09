<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="java.util.Calendar"%>
<%@page import="com.skilrock.lms.common.utility.CommonMethods"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<%
	response.setHeader("Cache-Control", "no-store"); //HTTP 1.1
	response.setHeader("Pragma", "no-cache"); //HTTP 1.0
	response.setDateHeader("Expires", 0); //prevents caching at the proxy server
%>
<html>
<head>
<s:head theme="ajax" debug="false" />
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
<link rel="stylesheet"
	href="<%=request.getContextPath()%>/LMSImages/css/styles.css"
	type="text/css" />
<title><%=application.getAttribute("JSP_PAGE_TITLE")%></title>
<link type="text/css" rel="stylesheet"
	href="<%=request.getContextPath()%>/LMSImages/css/lmsCalendar.css"
	media="screen" />
<script>
	var projectName="<%=request.getContextPath()%>";
	function getData() {
		var tblData = document.getElementById("tableDataDiv").innerHTML;
		tblData = tblData.replace(document.getElementById("sortRow").innerHTML,
				"");
		document.getElementById('tableValue').value = tblData;
		return false;
	}
	
	function checkUserName() {
		var userId = document.getElementById("userId");
		if(userId == '-1') 
			return false;
		else 
			return true;
	}
	
	function setUsername()
	{
	var uname = $("#userId option:selected").text();
	$("#user").val(uname);
	}
</script>
<script type="text/javascript"
	src="<%=request.getContextPath()%>/com/skilrock/lms/web/common/globalJs/calender.js"></script>
<script type="text/javascript"
	src="<%=request.getContextPath()%>/com/skilrock/lms/web/reportsMgmt/backOffice/js/report.js"></script>
</head>
<body>
	<%@include file="/com/skilrock/lms/web/loginMgmt/menu.jsp"%>
	<div id="wrap">
		<div id="top_form">
			<!-- <h3>Region Wise Bank Report</h3> -->
			<h3>Audit Trail Report</h3>
			<s:form action="bo_rep_auditTrail_Search" onsubmit=" return (validateDates() && checkUserName());" method="post">
			<s:hidden name="user" id="user" />
				<table width="450" border="0" cellpadding="2" cellspacing="0">
					<tr>
						<td align="center" colspan="2">
							<div id="dates"></div></td>
					</tr>
					<tr>
						<td align="right">Select User Name: </td>
						<td><s:select list="orgNameMap" id = "userId" name="userId" theme="simple" headerKey="-1" headerValue="--Select--" cssClass="option" onchange="setUsername();"/>
						
						</td>
					</tr>
					<%
						Calendar prevCal = Calendar.getInstance();
							String startDate = CommonMethods
									.convertDateInGlobalFormat(
											new java.sql.Date(prevCal.getTimeInMillis())
													.toString(), "yyyy-mm-dd",
											(String) session.getAttribute("date_format"));
					%>
					<tr>
						<td align="right"><label class="label">Start Date<font color='red'> <span>*</span>: 
						</label>
						</td>
						<td align="left"><input type="text" name="startDate"
							id="start_date" value="<%=startDate%>" readonly size="12" /> <input
							type="button"
							style=" width:19px; height: 19px; background: url('<%=request.getContextPath()%>/LMSImages/imagesCal/dateIcon.gif'); top left; border:0 ; "
							onclick="displayCalendar(document.getElementById('start_date'),'dd-mm-yyyy', this, '<%=startDate%>', false, '<%=startDate%>')" />
						</td>
					</tr>
					<tr>
						<td align="right"><label class="label">End Date <font color='red'> <span>*</span>: 
						</label>
						</td>
						<td align="left"><input type="text" name="endDate"
							id="end_date" value="<%=startDate%>" readonly size="12" /> <input
							type="button"
							style=" width:19px; height: 19px; background: url('<%=request.getContextPath()%>/LMSImages/imagesCal/dateIcon.gif'); top left; border:0 ; "
							onclick="displayCalendar(document.getElementById('end_date'),'dd-mm-yyyy', this, '<%=startDate%>',false, '<%=startDate%>')" />
						</td>
					</tr>
					<tr>
						<td colspan="2">
							<s:submit value="Search" align="right" targets="result" theme="ajax" cssClass="button" />
						</td>
					</tr>
				</table>
			</s:form>
			<div id="result"></div>
		</div>
	</div>
</body>
</html>
<code id="headId" style="visibility: hidden"> $RCSfile:
	bo_rep_auditTrail_Menu.jsp,v $ $Revision: 1.3 $ </code>