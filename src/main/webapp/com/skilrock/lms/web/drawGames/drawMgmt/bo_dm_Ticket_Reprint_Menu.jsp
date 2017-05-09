<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

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
		<script type="text/javascript">function validateTicketNo(){
															val = document.getElementById('ticketNum').value;
															if( val == ''){
																alert('Please enter a ticket number');
																return false;
															}
															if(val.length<14 || val.length >16){
																alert('Please enter a valid ticket number');
																	document.getElementById('ticketNum').value = '';
																	document.getElementById('ticketNum').focus();
																	return false;
															}
															else{
																if((isNaN(val))){
																	alert('Please enter a valid ticket number');
																	document.getElementById('ticketNum').value = '';
																	document.getElementById('ticketNum').focus();
																	return false;
																}
															}
															return true;
													}
		</script>
	</head>

	<body>
		<%@include file="/com/skilrock/lms/web/loginMgmt/menu.jsp"%>
		<div id="wrap">
			<div id="top_form">
				<h3>
					Ticket Reprint
				</h3>
				<s:form action="bo_dm_ticket_reprint_details" onsubmit="return validateTicketNo()">
					<table>
						<s:textfield name="ticketNum" id="ticketNum"
							label="Enter Ticket Number"></s:textfield>
						<s:submit value="Submit"
							cssClass="button"></s:submit>
					</table>
			</s:form>
				
		</div>



	</body>
</html>

<code id="headId" style="visibility: hidden">
$RCSfile: bo_dm_Ticket_Reprint_Menu.jsp,v $
$Revision: 1.2 $
</code>