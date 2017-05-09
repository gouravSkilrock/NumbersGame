<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="com.skilrock.lms.common.utility.CommonMethods"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.util.*"%>
<%@taglib prefix="s" uri="/struts-tags"%>
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
		<script>var projectName="<%=request.getContextPath() %>"</script>
		<script type="text/javascript"
			src="<%=request.getContextPath()%>/com/skilrock/lms/web/reportsMgmt/backOffice/js/report.js"></script>
		<script type="text/javascript"
			src="<%=request.getContextPath()%>/com/skilrock/lms/web/common/globalJs/calender.js"></script>
		<script type="text/javascript">
			function getData() {
				var reportTitle = document.getElementById("reportTitle").value;
				var tblData = "<span><b>Report Name : </b></span>"+ reportTitle + "</br>";
				 tblData += "<span><b>Start Date : </b></span>"+document.getElementById("start_date").value+"</br>";
				 tblData += "<span><b>End Date : </b></span>"+document.getElementById("end_date").value+"</br>";	
				 tblData += document.getElementById("tableDataDiv").innerHTML;
				//var tblData = document.getElementById("tableDataDiv").innerHTML;
				tblData = tblData.replace(document.getElementById("sortRow").innerHTML,"");
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
					Retailer Wise Summary Transaction Report
				</h3>
				<s:form action="bo_rep_retWiseSummeryTxnReport_Search">
					<table border="0" cellpadding="2" cellsapacing="0">
						<tr>
							<td colspan="2">
								<table cellpadding="3" cellspacing="0" border="0">
									<s:set name="stDate" id="stDate" value="#session.presentDate" />
									<%
										Calendar prevCal = Calendar.getInstance();
											String startDate = CommonMethods
													.convertDateInGlobalFormat(new java.sql.Date(prevCal
															.getTimeInMillis()).toString(), "yyyy-mm-dd",
															"yyyy-mm-dd");
									%>
									<tr>
										<td>
											<label class="label">
												&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Start Date
												<span>*</span>:&nbsp;
											</label>
											<input type="text" name="startDate" id="start_date"
												value="<%=startDate%>" readonly size="12">
												<input type="button"
													style="width: 19px; height: 19px; background: url('<%=request.getContextPath()%>/LMSImages/imagesCal/dateIcon.gif'); top left; border: 0;"
													onclick="displayCalendar(document.getElementById('start_date'),'yyyy-mm-dd', this, '<%=startDate%>', false, '')" />
										</td>
									</tr>
									<tr>
										<td>
											<label class="label">
												&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;End Date
												<span>*</span>:&nbsp;
											</label>
											<input type="text" name="endDate" id="end_date"
												value="<%=startDate%>" readonly size="12">
												<input type="button"
													style="width: 19px; height: 19px; background: url('<%=request.getContextPath()%>/LMSImages/imagesCal/dateIcon.gif'); top left; border: 0;"
													onclick="displayCalendar(document.getElementById('end_date'),'yyyy-mm-dd', this, '<%=startDate%>',false, '')" />
										</td>
									</tr>
								</table>
							</td>
						</tr>
						<tr>
							<td align="right">
								<s:submit name="search" value="Search" align="right"
									targets="down" theme="ajax" cssClass="button" />
							</td>
						</tr>
					</table>
				</s:form>
				<div id="down"></div>
				<div id="result" style="overflow-x: auto; overflow-y: hidden;"></div>
			</div>
		</div>
	</body>
</html>
<code id="headId" style="visibility: hidden">
	$RCSfile: bo_rep_retWiseSummeryTxnReport_Menu.jsp,v $ $Revision:
	1.1.2.4 $
</code>