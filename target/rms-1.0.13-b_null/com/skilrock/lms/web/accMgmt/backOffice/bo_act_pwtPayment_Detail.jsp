
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="com.skilrock.lms.common.utility.FormatNumber"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
	<%
		response.setHeader("Cache-Control", "no-store"); //HTTP 1.1
		response.setHeader("Pragma", "no-cache"); //HTTP 1.0
		response.setDateHeader("Expires", 0); //prevents caching at the proxy server
	%>
	<head>
		<link type="text/css" rel="stylesheet" href="<%=request.getContextPath()%>/LMSImages/css/lmsCalendar.css" media="screen" />
		<script>var projectName="<%=request.getContextPath()%></script>
		<script type="text/javascript" src="<%=request.getContextPath()%>/com/skilrock/lms/web/common/globalJs/calender.js"></script>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<link rel="stylesheet" href="<%=request.getContextPath()%>/LMSImages/css/styles.css" type="text/css" />
		<title><%=application.getAttribute("JSP_PAGE_TITLE")%></title>
		<script type="text/javascript" src="<%=request.getContextPath()%>/com/skilrock/lms/web/accMgmt/backOffice/js/rightClickDisable.js"></script>
		<script type="text/javascript" src="<%=request.getContextPath()%>/com/skilrock/lms/web/accMgmt/backOffice/js/ACT_ST5_Validation.js"></script>
		<script type="text/javascript" src="<%=request.getContextPath()%>/com/skilrock/lms/web/accMgmt/backOffice/js/digitToWord.js"></script>
		<s:head theme="ajax" debug="false" />
		<script type="text/javascript">
			function processRequestType(requestType) {
				document.getElementById("processType").value = requestType;
				document.getElementById("myForm").submit();
			}

			function setNetAmount(taxAmount, winAmount) {
			    document.getElementById("taxAmtErrMsg").innerHTML= "";
			    if(""== taxAmount){
	    			document.getElementById("taxAmount").value = "0.00";
	               	document.getElementById("netAmount").value = winAmount;
	            }
			    else{
					taxAmount = parseFloat(taxAmount);
					winAmount = parseFloat(winAmount);
					if(taxAmount > winAmount || taxAmount < 0) {
					    document.getElementById("taxAmtErrMsg").innerHTML =  i18nOb.prop('error.invalid.tax.amt');
						document.getElementById("taxAmount").value = "0.00";
						document.getElementById("netAmount").value = winAmount;
					} else {
						document.getElementById("netAmount").value = winAmount-taxAmount;
					}
				}  
	     }
		</script>
	</head>
	<body>
		<%@include file="/com/skilrock/lms/web/loginMgmt/menu.jsp"%>
		<div id="wrap">
			<div id="top_form">
				<h3>
					BO PWT Payment
				</h3>
				<div id="divFrm" style="display: block" class="color:black; ">
					<s:form action="bo_act_pwtPayment_Submit" id="myForm" theme="simple">
					<s:token />
						<table cellpadding="2" cellspacing="0" bordercolor="#cccccc" border="1" width="50%" align="center">
							<tr>
								<th colspan="2" align="center">Player Information</th>
							</tr>
							<tr>
								<td>Player Name</td>
								<td><s:property value="firstName" /> <s:property value="lastName" /></td>
							</tr>
							<tr>
								<td>Party Type</td>
								<td><s:property value="requestFor" /></td>
							</tr>
							<tr>
								<td>Party City</td>
								<td><s:property value="city" /></td>
							</tr>
							<tr>
								<th colspan="2" align="center">PWT Information</th>
							</tr>
							<tr>
								<td>Ticket Number</td>
								<td><s:property value="ticketNumber" />0</td>
							</tr>
							<tr>
								<td>Winning Amount</td>
								<td><s:property value="winningAmount" /></td>
							</tr>
							<tr>
								<td>Tax Amount</td>
								<td><s:textfield name="taxAmount" id="taxAmount" value="%{taxAmount}" onblur='setNetAmount(this.value, %{winningAmount})' /><span id="taxAmtErrMsg"></span> </td>
							</tr>
							<tr>
								<td>Net Amount</td>
								<td><s:textfield name="netAmount" id="netAmount" value="%{netAmount}" readonly="true" /></td>
							</tr>
						</table>
						<br/><br/>
						<div align="center">
							<s:hidden name="taskId" value="%{taskId}" />
							<s:hidden name="gameId" value="%{gameId}" />
							<s:hidden name="gameTypeId" value="%{gameTypeId}" />
							<s:hidden name="drawId" value="%{drawId}" />
							<s:hidden name="ticketNumber" value="%{ticketNumber}" />
							<s:hidden name="winningAmount" value="%{winningAmount}" />
							<s:hidden name="playerId" value="%{playerId}" />
							<s:hidden name="processType" id="processType" value="" />
							<input type="button" value="Pay" onclick="processRequestType('APPROVE');" />
							<input type="button" value="Deny" onclick="processRequestType('DENY');" />
						</div>
					</s:form>
				</div>
			</div>
		</div>
	</body>
</html>
<code id="headId" style="visibility: hidden">
	$RCSfile: bo_act_pwtPayment_Detail.jsp,v $ $Revision: 1.3 $
</code>