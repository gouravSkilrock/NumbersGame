<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="com.skilrock.lms.common.utility.CommonMethods"%>
<%@page import="java.util.Calendar"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
	<head>
		<s:head theme="ajax" debug="false" />
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<link rel="stylesheet" href="<%=request.getContextPath()%>/LMSImages/css/styles.css" type="text/css" />
		<title><%=application.getAttribute("JSP_PAGE_TITLE")%></title>
		<script type="text/javascript" src="<%=request.getContextPath()%>/com/skilrock/lms/web/drawGames/reportsMgmt/backOffice/js/report.js"></script>
		<link type="text/css" rel="stylesheet" href="<%=request.getContextPath()%>/LMSImages/css/lmsCalendar.css" media="screen" />
		<script>var projectName="<%=request.getContextPath()%>"</script>
		<script type="text/javascript" src="<%=request.getContextPath()%>/com/skilrock/lms/web/common/globalJs/calender.js"></script>
		<script type="text/javascript" src="<%=request.getContextPath() %>/com/skilrock/lms/web/common/globalJs/jquery-1.10.1.min.js"></script>
		<script>
			$(document).ready(function() {
				var merchantWallets = $('#merchantWallets').val();
				var obj = jQuery.parseJSON(merchantWallets);
				$.each(obj, function(index, value) {
					$('#merchantName').append($('<option></option>').val(index).html(index));
				});

				$('#merchantName').change(function() {
					$("#down").html("");
					$('#walletType').find('option').remove().end().append($('<option></option>').val(-1).html('--Please Select--'));

					var merchantName = $('#merchantName').val();
					$.each(obj, function(index, value) {
						if(merchantName == index) {
							if(value != undefined) {
								$.each(value, function(index1, value1) {
									$('#walletType').append($('<option></option>').val(value1).html(value1));
								});
							}
						}
	    			});
    			});

    			$('#walletType').change(function() {
    				$("#down").html("");
    			});

				$('#transactionType').change(function() {
    				$("#down").html("");
    			});

				$('#status').change(function() {
    				$("#down").html("");
    			});
			});

			function validate() {
				$("#errorMessageDiv").html("");
				var errorMsg = "";
				if($("#merchantName").val()==-1)
					errorMsg += "Please Select Merchant.<br/>";
				if($("#walletType").val()==-1)
					errorMsg += "Please Select Wallet.<br/>";
				if($("#transactionType").val()==-1)
					errorMsg += "Please Select Transaction Type.<br/>";
				if($("#status").val()==-1)
					errorMsg += "Please Select Status.<br/>";

					if(errorMsg == "")
						return true;
					else {
						$("#errorMessageDiv").html(errorMsg);
						return false;
					}
			}

			function selectAllCB(element) {
				$('.transId').each(function() {
					this.checked = $(element).is(":checked");
				});
			}

			var regularExpression = /^[a-zA-Z0-9]+$/;
			function processRequest(action) {
				var isEmpty = false;
				var isInvalid = false;
				var data = $('#dataTable tbody tr').map(function() {
					var $row = $(this);
					if ($row.find('.transId').prop('checked')) {
						if($row.find('.merchantTransId').val()==null || $row.find('.merchantTransId').val()=='')
							isEmpty = true;

						if(!regularExpression.test($row.find('.merchantTransId').val()))
							isInvalid = true;
					}
				});

				if(isEmpty) {
					alert('Please Enter Transaction Id.');
				} else if(isInvalid) {
					alert('Enter Valid Transaction Id.');
				} else {
					var isNotSelect = true;
					var data = $('#dataTable tbody tr').map(function() {
						var $row = $(this);
						if ($row.find('.transId').prop('checked')) {
							isNotSelect = false;
	
							return {
								transId : $row.find('.transId').val(),
								merchantTransId : $row.find('.merchantTransId').val()
							};
						}
					}).get();

					if(isNotSelect) {
						alert('Please Select Any Transaction.');
					} else {
						if (confirm("Are you sure, you want to Process ?")) {
							_ajaxUnsync("<%=request.getContextPath()%>/com/skilrock/lms/web/drawGames/reportsMgmt/bo_dg_rep_processRequest.action", "down", "merchantName="+$("#merchantName").val()+"&action="+action+"&jsonParamData="+JSON.stringify(data));
							$("#down").html("");
							alert("Transaction Process Successfully.");
						}
					}
				}
			}

			function createTableData() {
				tblHTML = $("#tableDataDiv").html();
				$('th:nth-child(11)').remove();
				$('td:nth-child(11)').remove();

				var tblData = '<div><b>Draw Game Reconciliation Report</b></div><br/>';
					tblData += '<div><b>Merchant Name : </b>'+$("#merchantName").val()+'</div>';
					tblData += '<div><b>Wallet Type : </b>'+$("#walletType").val()+'</div>';
					tblData += '<div><b>Transaction Type : </b>'+$("#transactionType").val()+'</div>';
					tblData += '<div><b>Start Date : </b>'+$("#start_date").val()+'</div>';
					tblData += '<div><b>End Date : </b>'+$("#end_date").val()+'</div>';
					tblData += '<div><b>Status : </b>'+$("#status").val()+'</div><br>';
					tblData += document.getElementById("tableDataDiv").innerHTML;

			    $('#tableValue').val(tblData);
				$("#tableDataDiv").html(tblHTML);

				return false;
			}
		</script>
	</head>
	<body>
		<%@include file="/com/skilrock/lms/web/loginMgmt/menu.jsp"%>
		<div id="wrap">
			<div id="top_form">
				<h3>
					<s:text name="label.ussd.merchant.reconciliation.report"/>
				</h3>
				<s:form name="reconcileData" action="bo_dg_rep_reconciliationSearch" onsubmit="return validate();">
					<s:hidden id="merchantWallets" name="merchantWallets" />
					<table border="0" cellpadding="2" cellspacing="0" width="400" style="vertical-align: middle;">
						<tr>
							<td align="left" colspan="2">
								<s:div id="errorMessageDiv" cssStyle="color:red; ">
								</s:div>
							</td>
						</tr>
						<tr>
							<td>Select Merchant<span class="required">*</span></td>
							<td>
								<s:select name="merchantName" id="merchantName" theme="simple"
									headerKey="-1" headerValue="%{getText('label.please.select')}"
									list="{}" cssClass="option" />
							</td>
						</tr>
						<tr>
							<td>Wallet Type<span class="required">*</span></td>
							<td>
								<s:select name="walletType" id="walletType" theme="simple"
									headerKey="-1" headerValue="%{getText('label.please.select')}"
									list="{}"
									cssClass="option" />
							</td>
						</tr>
						<tr>
							<td>Transaction Type<span class="required">*</span></td>
							<td>
								<s:select name="transactionType" id="transactionType" theme="simple"
									headerKey="-1" headerValue="%{getText('label.please.select')}"
									list="#{'ALL':'ALL', 'SALE':'SALE', 'PWT':'WINNING'}" cssClass="option" />
							</td>
						</tr>
						<tr>
							<td align="left" colspan="2">
								<div style="color: red; text-align: center" id="date_e"></div>
							</td>
						</tr>
						<tr>
							<td>
								<%
									Calendar prevCal = Calendar.getInstance();
									String currentDate = CommonMethods.convertDateInGlobalFormat(new java.sql.Date(prevCal.getTimeInMillis()).toString(), "yyyy-mm-dd", "yyyy-mm-dd");
									String lastDate = CommonMethods.getLastArchDate();
								%>
								<label class="label"><s:text name="label.start.date" /></label>
							</td>
							<td>
								<input type="text" name="startDate" id="start_date" value="<%=currentDate %>" readonly size="12" />
								<input type="button"
									style="width: 19px; height: 19px; background: url('<%=request.getContextPath()%>/LMSImages/imagesCal/dateIcon.gif'); top left; border: 0;"
									onclick="displayCalendar(document.getElementById('start_date'),'yyyy-mm-dd', this,'<%=currentDate%>', '', '<%=currentDate%>')" />
							</td>
						</tr>
						<tr>
							<td>
								<label class="label"><s:text name="label.end.date" /></label>
							</td>
							<td>
								<input type="text" name="endDate" id="end_date" value="<%=currentDate %>" readonly size="12" />
								<input type="button"
									style="width: 19px; height: 19px; background: url('<%=request.getContextPath()%>/LMSImages/imagesCal/dateIcon.gif'); top left; border: 0;"
									onclick="displayCalendar(document.getElementById('end_date'),'yyyy-mm-dd', this,'<%=currentDate%>', '', '<%=currentDate%>')" />
							</td>
						</tr>
						<tr>
							<td>Status<span class="required">*</span></td>
							<td>
								<s:select name="status" id="status" theme="simple"
									headerKey="-1" headerValue="%{getText('label.please.select')}"
									list="#{'INITIATED':'INITIATED', 'FAILED':'FAILED', 'DONE':'DONE'}"
									cssClass="option" />
							</td>
						</tr>
						<tr>
							<td colspan="2">
								<s:submit name="search" key="btn.srch" align="center" targets="down" theme="ajax" cssClass="button" />
							</td>
						</tr>
					</table>
				</s:form>
				<div id="down" style="text-align: center"></div>
			</div>
		</div>
	</body>
</html>
<code id="headId" style="visibility: hidden">
	$RCSfile: bo_dg_rep_ReconciliationMenu.jsp,v $ $Revision: 1.3 $
</code>