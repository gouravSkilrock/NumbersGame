<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="java.util.Calendar"%>
<%@page import="com.skilrock.lms.common.utility.CommonMethods"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<%
	response.setHeader("Cache-Control", "no-store"); //HTTP 1.1
	response.setHeader("Pragma", "no-cache"); //HTTP 1.0
	response.setDateHeader("Expires", 0); //prevents caching at the proxy server
%>
<html>
	<head>
		<s:head theme="ajax" debug="false" />
		<meta http-equiv="Content-Type"
			content="text/html; charset=iso-8859-1" />
		<link rel="stylesheet"
			href="<%=request.getContextPath()%>/LMSImages/css/styles.css"
			type="text/css" />
		<link type="text/css"
			rel="stylesheet" href="<%=request.getContextPath() %>/LMSImages/css/lmsCalendar.css" media="screen"/>
		<title><%=application.getAttribute("JSP_PAGE_TITLE")%></title>
		<script>
			var projectName="<%=request.getContextPath()%>"
		</script>
		<script type="text/javascript"
			src="<%=request.getContextPath()%>/com/skilrock/lms/web/common/globalJs/jquery-1.10.1.min.js"></script>
		<script type="text/javascript"
			src="<%=request.getContextPath()%>/com/skilrock/lms/web/common/globalJs/calender.js"></script>
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
					Events Insertion
				</h3>
				<s:form action="eventInsertionTeamSearch" onsubmit="return formSubmit(this.id,'down');" theme="simple">
					<table width="450" border="0" cellpadding="2" cellspacing="0">
						<tr>
							<td align="center" colspan="2">
								<div id="errorDiv" style="color: red;"></div>
							</td>
						</tr>
						<tr>
							<td align="left">
								Select Game Name
							</td>
							<td align="left">
								<s:select id="gameId" headerKey="-1" headerValue="--Please Select--"
									name="gameId" list="%{gameMap}" listKey="key"
									listValue="%{value.gameDispName}" cssClass="option"></s:select>
							</td>
						</tr>
						<tr>
							<td>
								&nbsp;
							</td>
							<td align="left">
								<s:submit name="submit" value="Submit" align="right" cssClass="button" />
							</td>
						</tr>
					</table>
				</s:form>
				<div id="down"></div>
				<div id="result"></div>
			</div>
		</div>
	</body>
</html>

<code id="headId" style="visibility: hidden">
	$RCSfile: bo_sle_eventInsertion_Menu.jsp,v $ $Revision: 1.2 $
</code>