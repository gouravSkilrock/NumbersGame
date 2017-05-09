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

			function validateForm(){
			var status = $("#status").val();
			
			if(status == -1){
				$("#errorDiv").html("Please Select Status");
				return false;
			}else{
			$("#errorDiv").html("");
			return true;
			}
			
			}
			
			function getCheckBoxValues(){
				var data = $('#dTableNew tbody tr').map(function() {
					var $row = $(this);
					if ($row.find('#data').prop('checked')) {
						return {
							userId : $row.find('#userId').val(),
							gameId : $row.find('#gameId').val(),
							mrpAmt : $row.find('#mrpAmt').text(),
							txnId : $row.find('#txnId').text(),
							ticketNumber : $row.find('#ticketNumber').text()
						};
					}
				}).get();

						_ajaxUnsync("<%=request.getContextPath()%>/com/skilrock/lms/web/reportsMgmt/bo_rep_VS_Sale_Reconc_Settle.action", "", "jsonParamData="+JSON.stringify(data));
						alert("Transaction(s) Settled Successfully.");
			}
			
		</script>
	</head>
	<body>
		<%@include file="/com/skilrock/lms/web/loginMgmt/menu.jsp"%>
		<div id="wrap">
			<div id="top_form">
				<div align="left">
					<h3>VS Reconciliation Report</h3>
					<div id="main-heading" style="display: none">
					VS Reconciliation Report
				</div>
				</div>
					<s:form action="bo_rep_VS_Sale_Reconc_Search" id="privModifyForm" onsubmit="return validateForm();" theme="simple">
						<table>
							<tr>
								<td colspan="2">
									<div id="errorDiv" style="color:red;"></div>
								</td>
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
								<td>Select Status</td>
								<td><s:select headerKey="-1" headerValue="Please Select" list="{'PENDING', 'FAILED'}" id="status" name="status" cssClass="option"></s:select> </td>
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
		<script type="text/javascript"
			src="<%=request.getContextPath()%>/js/Export_Excel.js"></script>
		<script type="text/javascript"
			src="<%=request.getContextPath()%>/js/Export_PDF.js"></script>
		<script type="text/javascript"
			src="<%=request.getContextPath()%>/js/jquery.dataTables.js"></script>
	</body>
</html>
<code id="headId" style="visibility: hidden">
	$RCSfile: bo_rep_VS_Sale_Reconc_Menu.jsp,v $ $Revision: 1.3 $
</code>