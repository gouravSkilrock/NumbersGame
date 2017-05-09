<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
	<head>
		<s:head theme="ajax" debug="false" />
		<link rel="stylesheet"
			href="<%=request.getContextPath()%>/LMSImages/css/styles.css"
			type="text/css" />
		<title><%=application.getAttribute("JSP_PAGE_TITLE")%></title>
		<script>var projectName="<%=request.getContextPath()%></script>
		<script
			src="<%=request.getContextPath()%>/com/skilrock/lms/web/common/globalJs/jquery-1.10.1.min.js"></script>
		<script type="text/javascript">
			$(document).ready(function() {
				validateTicketDetails = function() {
					var errorMessage = "";
					if($("#ticketNumber").val() == "") {
						errorMessage += "Please Enter Ticket Number.<br/>";
					}

					 if($("#inpRemarks").val().length >1000) {
						errorMessage += "Remarks Maximum Length is 1000.<br/>";
					} 
					   else {
						$("#remarks").val($("#inpRemarks").val())
					}

					if(errorMessage == "") {
						$("#errorMessageDiv").html("");
						return true;
					} else {
						$("#errorMessageDiv").html(errorMessage);
						return false;
					}
				};

				clearDiv = function () {
					$("#resultDiv").html("");
				};
			});
		</script>
	</head>
	<body>
		<%@include file="/com/skilrock/lms/web/loginMgmt/menu.jsp"%>
		<div id="wrap">
			<div id="top_form">
				<h3>
					Track Full Ticket
				</h3>
				<s:form method="GET" action="bo_dg_trackFullTicket_Search" >
					<s:hidden id="remarks" name="remarks" />
					<s:div id="errorMessageDiv" cssStyle="color:red; "></s:div>
					<s:radio name="ticketFormat" list="#{'OLD':'12345678901234XXXX', 'NEW':'XXXX12345678901234'}" value="%{'OLD'}" label="Select Format" />
					<s:textfield name="ticketNumber" id="ticketNumber" label="Enter Ticket Number" maxlength="14" />
					<s:textarea id="inpRemarks" label="Enter Remarks" />
					<s:label value="* (Maximum 1000 Characters allowed)" cssStyle="color:red; " />
					<s:submit value="Details" cssClass="button" onclick="return validateTicketDetails();" theme="ajax" targets="resultDiv" />
				</s:form>
				<div id="resultDiv"></div>
			</div>
		</div>
	</body>
</html>
<code id="headId" style="visibility: hidden">
	$RCSfile: bo_dg_trackFullTicket_Menu.jsp,v $ $Revision: 1.3 $
</code>