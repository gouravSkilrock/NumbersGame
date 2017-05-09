<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%@ taglib prefix="s" uri="/struts-tags"%>
<%
	response.setDateHeader("Expires", 0);
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">

	<head>
		<script>var projectName="<%=request.getContextPath()%>"</script>
		<script type="text/javascript"
			src="<%=request.getContextPath()%>/com/skilrock/lms/web/common/globalJs/calender.js"></script>
		<script type="text/javascript"
			src="<%=request.getContextPath()%>/com/skilrock/lms/web/accMgmt/backOffice/js/clearCheqeValidation.js"></script>
		<link type="text/css" rel="stylesheet"
			href="<%=request.getContextPath()%>/LMSImages/css/lmsCalendar.css"
			media="screen" />

		<s:head theme="ajax" debug="false" />
		<meta http-equiv="Content-Type"
			content="text/html; charset=iso-8859-1" />

		<link rel="stylesheet"
			href="<%=request.getContextPath()%>/LMSImages/css/styles.css"
			type="text/css" />
		<script type="text/javascript">
	
</script>
		<title><%=application.getAttribute("JSP_PAGE_TITLE")%></title>

	</head>
	<body onload="getAgentList();">
		<%@include file="/com/skilrock/lms/web/loginMgmt/menu.jsp"%>

		<div id="wrap">

			<div id="top_form">
				<div id="limitDiv" style="align: center;"></div>
				<h3>
					Search Cheques For Clearance
				</h3>
				<s:form action="bo_act_clrChq_Search">
					<s:hidden name="agentNameValue" id="agentNameValue"></s:hidden>
					<s:select name="agentOrgName" id="agetntOrganizationId"
						label="%{#application.TIER_MAP.AGENT} Name"
						list="{}" headerValue="--Please Select--"
						headerKey="-1" cssClass="option"
						onchange="verifyAgent(this.value)"></s:select>
					<s:textfield name="chkNbr" id="chkNumberId" label="Cheque Nbr"></s:textfield>
					<s:textfield name="draweeBnk" label="Drawee Bank"></s:textfield>
					<s:select name="chqStatus" label="Cheque Status"
						list="{'PENDING','CLEARED','CANCEL'}" cssClass="option"></s:select>
					<s:submit value="Search" theme="ajax" targets="searchResults"
						cssClass="button"></s:submit>

				</s:form>
				<div id="searchResults">
				</div>

			</div>
		</div>
	</body>
</html>

<code id="headId" style="visibility: hidden">
$RCSfile: bo_act_clrChq_Menu.jsp,v $
$Revision: 1.2 $
</code>