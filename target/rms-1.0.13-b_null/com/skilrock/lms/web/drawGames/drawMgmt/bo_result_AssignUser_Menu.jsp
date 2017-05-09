
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
				var arr = _id.n('user');
				var count = 0;
				for(var i=0; i<(arr.length); i++){
					if(arr[i].value != -1){
						count = count + 1;
					}
				}
				
				if(count <= 1){
					alert('Please Select Atleast Two Users');
					return false;
				}
				
				for(var i=0; i<(arr.length); i++){
					if(arr[i].value != -1){
						for(var j=(i+1); j<arr.length; j++){
							if(arr[i].value == arr[j].value){
								alert('Please Select Different Users');
								return false;
							}
						}
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
				<div id="data">
					<h3>
						Assign Users
					</h3>
					<s:form name="searchForm" id="searchFormId"
						onsubmit="return validate()" action="resultUserAssignSave">

						<table cellspacing="0" cellpadding="3" border="0"
							bordercolor="#CCCCCC">
							<tr>
								<td>
									<s:select list="#session.DRAWGAME_LIST" name="gameNo"
										label="Game Name" id="game_no" cssClass="option" />
								</td>
							</tr>
							
							<s:iterator value="#session.BO_RESULT_ASSIGN_USERS_COUNT" status="ti">
							<tr>
								<td align="right">
									<s:div id="errDiv%{#ti.index}"/>
								</td>
							</tr>
							<tr>
								<td>
									<s:select list="#session.BO_USER_LIST" name="user"
										label="User %{#ti.index+1}" id="user%{#ti.index+1}" headerKey="-1" headerValue="--Please Select--" cssClass="option" />
								</td>
							</tr>
							</s:iterator>
							
							<tr>
								<td>
									<s:submit value="Assign User" align="center" cssClass="button" />
								</td>
							</tr>


						</table>

					</s:form>

					<div id="draw_res_div">

					</div>

				</div>
			</div>
		</div>
	</body>
</html>

<code id="headId" style="visibility: hidden">
$RCSfile: bo_result_AssignUser_Menu.jsp,v $
$Revision: 1.2 $
</code>