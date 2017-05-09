<%@taglib prefix="s" uri="/struts-tags"%>
<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="java.util.Calendar"%>
<%@page import="com.skilrock.lms.common.utility.CommonMethods"%>
<%@page import="com.skilrock.lms.common.Utility"%>
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
			$(document).ready(function() {
			});

			function selectAll(element, onActivity) {
				$('.'+onActivity).each(function() {
					this.checked = $(element).is(":checked");
				});
			}

			function processRequest(action) {
				var isNotSelect = true;
				var data = $('.startSortable').map(function() {
					var $row = $(this);
					if ($row.find('.selectId').prop('checked')) {
						isNotSelect = false;
						return {
							id : $row.find('.id').val()
						};
					}
				}).get();

				if(isNotSelect) {
					alert('Please Select Any Request.');
				} else {
					if (confirm("Do you really want to Process ?")) {
						_ajaxUnsync("<%=request.getContextPath()%>/com/skilrock/lms/web/accMgmt/bo_act_processBankDepositRequest.action", "fetchRequestData", "status="+action+"&jsonParamData="+JSON.stringify(data));
						alert("Value Updated Successfully.");
					}
				}
			}

			function fetchRequestData() {
				/*
				$("#errorDiv").html("");
				$("#resultDiv").html("<img src='<%=request.getContextPath()%>/LMSImages/images/loadingAjax.gif' alt='loading' />");
				var text = _ajaxCall("<%=request.getContextPath()%>/com/skilrock/lms/web/accMgmt/bo_act_processBankDepositRequest_Search.action?retName="+$("#retName").val()+"&receiptNo="+$("#receiptNo").val()+"&startDate="+$("#start_date").val()+"&endDate="+$("#endDate").val()+"&status="+$("#status").val());
				alert(text.data);
				$("#resultDiv").html(text.data);
				*/

				$('#btnSubmit').trigger('click');

				return false;
			}

			function getData() {
				var preData = document.getElementById("tableDataDiv").innerHTML;
				$('tr th:nth-child(10)').remove();
				$('tr td:nth-child(10)').remove();
				var tblData = document.getElementById("tableDataDiv").innerHTML;
				tblData = tblData.replace(document.getElementById("sortRow").innerHTML, "");

				var dateData = "<table><tr><td>Start Date</td><td>"+$("#start_date").val()+"</td></tr><tr><td>End Date</td><td>"+$("#endDate").val()+"</td></tr></table>";
				document.getElementById('tableValue').value = dateData +"<br/><br/>"+ tblData;
				document.getElementById("tableDataDiv").innerHTML = preData;

				return false;
			}
		</script>
	</head>
	<body>
		<%@include file="/com/skilrock/lms/web/loginMgmt/menu.jsp"%>
		<div id="wrap">
			<div id="top_form">
				<div align="left">
					<h3>Process Bank Deposit</h3>
				</div>
				<s:form action="bo_act_processBankDepositRequest_Search" theme="simple">
					<table>
						<tr>
							<td colspan="2">
								<div id="errorDiv" style="color:red;"></div>
							</td>
						</tr>
						<tr>
							<td>
								<label class="label">Retailer Name</label>
							</td>
							<td>
								<s:textfield name="retName" id="retName" />
							</td>
						</tr>
						<tr>
							<td>
								<label class="label">Receipt No</label>
							</td>
							<td>
								<s:textfield name="receiptNo" id="receiptNo" />
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
								<input type="button" style="width: 19px; height: 19px; background: url('<%=request.getContextPath()%>/LMSImages/imagesCal/dateIcon.gif'); top left; border: 0;" onclick="displayCalendar(document.getElementById('start_date'),'yyyy-mm-dd', this, '<%=startDate%>', '<s:property value="%{deploymentDate}"/>', document.getElementById('endDate').value)" />
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
							<td>
								<label class="label">Status</label>
							</td>
							<td>
								<s:select name="status" id="status" list="#{'PENDING':'Pending', 'APPROVED':'Approved', 'CANCELLED':'Cancelled'}" cssClass="option" />
							</td>
						</tr>
						<tr>
							<td>&nbsp;</td>
							<td><s:submit id="btnSubmit" key="Search" align="right" targets="resultDiv" theme="ajax" cssClass="button" /></td>
						</tr>
					</table>
				</s:form>
				<div id="resultDiv"></div>
			</div>
		</div>
	</body>
</html>
<code id="headId" style="visibility: hidden">
	$RCSfile: bo_act_processBankDepositRequest_Menu.jsp,v $ $Revision: 1.3 $
</code>