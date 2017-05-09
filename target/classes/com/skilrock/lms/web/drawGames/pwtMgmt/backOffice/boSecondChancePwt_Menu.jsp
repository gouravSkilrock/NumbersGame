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
		<script>var projectName="<%=request.getContextPath()%>"</script>
		<script src="<%=request.getContextPath()%>/com/skilrock/lms/web/common/globalJs/jquery-1.10.1.min.js"></script>
		<script type="text/javascript">
			$(document).ready(function() {
				
			});
		</script>
	</head>
	<body>
		<%@include file="/com/skilrock/lms/web/loginMgmt/menu.jsp"%>
		<div id="wrap">
			<div id="top_form">
				<h3>
					Second Chance PWT
				</h3>
				<s:form method="post">
					<s:actionerror/>
					<s:hidden name="orgType" value="BO" />
					<s:textfield name="ticketNumber" id="ticketNumber" label="Enter Ticket Number" />
					<s:textfield name="verificationCode" id="verificationCode" label="Enter Verification Code" />
					<s:submit value="Verify Ticket" cssClass="button" id="verifyTicket"
						theme="ajax" targets="resultDiv"
						action="bo_secondChancePwt_Verify"></s:submit>
				</s:form>
				<div id="resultDiv"></div>
			</div>
		</div>
	</body>
</html>
<code id="headId" style="visibility: hidden">
	$RCSfile: boSecondChancePwt_Menu.jsp,v $ $Revision: 1.3 $
</code>