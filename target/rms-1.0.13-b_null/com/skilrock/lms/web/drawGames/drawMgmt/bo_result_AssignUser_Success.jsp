
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
		<meta http-equiv="Content-Type"
			content="text/html; charset=iso-8859-1" />
		<title><%=application.getAttribute("JSP_PAGE_TITLE")%></title>

		<link rel="stylesheet"
			href="<%=request.getContextPath()%>/LMSImages/css/styles.css"
			type="text/css" />
		<link type="text/css" rel="stylesheet"
			href="<%=request.getContextPath()%>/LMSImages/css/lmsCalendar.css"
			media="screen" />
		<script>var projectName="<%=request.getContextPath()%>"</script>
		<s:head theme="ajax" debug="false" />
		<script>
			function validate(){
			if(_id.v("user1")!=_id.v("user2")){
			return true;
			}else{
			alert("Please Select different Users");
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
						String message = (String)session.getAttribute("BO_USER_ASSIGN_MESSAGE");
						
						if(message != null){
							if(message.equalsIgnoreCase("SUCCESS")){
								%>
									<H3>BO USERS ASSIGNED SUCCESSFULLY.</H3>
								<%
							} else if(message.equalsIgnoreCase("ALREADY_EXIST")){
							%>
								<H3>YOU HAVE ALREADY ASSIGNED USERS FOR THIS GAME. PLEASE GO TO EDIT PAGE FOR ASSIGNING NEW USERS</H3>
								<s:a href="resultUserAssignFetch.action">Go to Edit Result Submission User's </s:a>
							<%
							}else if(message.equalsIgnoreCase("ERROR")){
							%>
								<H3>ERROR IN ASSIGNING USERS.</H3>
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
$RCSfile: bo_result_AssignUser_Success.jsp,v $
$Revision: 1.2 $
</code>