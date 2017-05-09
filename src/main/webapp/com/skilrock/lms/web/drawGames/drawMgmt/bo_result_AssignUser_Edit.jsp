
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%
	String deployMentDate = (String) application
			.getAttribute("DEPLOYMENT_DATE");
	deployMentDate = deployMentDate.replaceAll("-", "/");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">

	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<title><%=application.getAttribute("JSP_PAGE_TITLE")%></title>

		<link rel="stylesheet"
			href="<%=request.getContextPath()%>/LMSImages/css/styles.css"
			type="text/css" />
		<link type="text/css" rel="stylesheet"
			href="<%=request.getContextPath()%>/LMSImages/css/lmsCalendar.css"
			media="screen" />
		<script>var projectName="<%=request.getContextPath()%></script>
		<s:head theme="ajax" debug="false" />
		<script>
			function validate() {
				if (_id.v("user1") != _id.v("user2")) {
					return true;
				} else {
					alert('<s:text name="error.select.diff.user"/>');
				}
				return false;
			}
		</script>
	</head>
	<body>
		<%@include file="/com/skilrock/lms/web/loginMgmt/menu.jsp"%>

		<div id="wrap">
			<div id="top_form">
				<div id="data">
					<%
						String message = (String) session
								.getAttribute("BO_USER_ASSIGN_MESSAGE");

						if (message != null) {
							if (message.equalsIgnoreCase("SUCCESS")) {
					%>
					<H3>
						<s:text name="label.bo.usr.assign.success"/>.
					</H3>
					<%
						} else if (message.equalsIgnoreCase("ERROR")) {
					%>
					<H3>
						<s:text name="label.error.assign.usr"/>.
					</H3>
					<%
						}
						}
					%>


				</div>
			</div>
		</div>
	</body>
</html>

<code id="headId" style="visibility: hidden">
	$RCSfile: bo_result_AssignUser_Edit.jsp,v $ $Revision: 1.3 $
</code>