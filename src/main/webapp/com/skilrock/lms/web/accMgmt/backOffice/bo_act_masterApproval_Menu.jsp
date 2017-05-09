
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
	</head>
	<body>
		<%@include file="/com/skilrock/lms/web/loginMgmt/menu.jsp"%>
		<div id="wrap">
			<div id="top_form">
				<h3>
					BO Master Approval
				</h3>
				<div id="divFrm" style="display: block">
					<s:form action="bo_act_masterApproval_Search" theme="simple">
						<table cellpadding="2" cellspacing="0" border="0" width="450">
							<tr>
								<td>Request For</td>
								<td><s:select name="requestFor" list="#{'PLAYER':'PLAYER','AGENT':#application.TIER_MAP.AGENT.toUpperCase(),'RETAILER':#application.TIER_MAP.RETAILER.toUpperCase()}" cssClass="option" /></td>
							</tr>
							<tr>
								<td>Request Id</td>
								<td><s:textfield name="requestId" /></td>
							</tr>
							<tr>
								<td>Player First Name</td>
								<td><s:textfield name="firstName" /></td>
							</tr>
							<tr>
								<td>Player Last Name</td>
								<td><s:textfield name="lastName" /></td>
							</tr>
							<tr>
								<td>&nbsp;</td>
								<td>
									<s:submit value="Search" targets="resultDiv" theme="ajax" cssClass="button" />
								</td>
							</tr>
						</table>
					</s:form>
				</div>
				<div id="resultDiv"></div>
			</div>
		</div>
	</body>
</html>
<code id="headId" style="visibility: hidden">
	$RCSfile: bo_act_masterApproval_Menu.jsp,v $ $Revision: 1.3 $
</code>