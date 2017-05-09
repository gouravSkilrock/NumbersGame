
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="com.skilrock.lms.beans.UserInfoBean"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en">

	<head>

		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />

		<link rel="stylesheet"
			href="<%=request.getContextPath()%>/LMSImages/css/styles.css"
			type="text/css" />

		<title><%=application.getAttribute("JSP_PAGE_TITLE")%></title>

		<script>
			function isST3BOUserValidation(){
			 var frmVal  = new parent.frames[0].Validator("newsubuser");
			  frmVal.aV("firstName","req",'<s:text name="error.fname.empty" />',"fname");
			  frmVal.aV("firstName","alpha",'<s:text name="error.name.empty" />',"fname");
			  
			  frmVal.aV("lastName","req",'<s:text name="error.lname.empty" />',"lname");
			  frmVal.aV("lastName","alpha",'<s:text name="error.name.empty" />(<s:text name="error.spcl.char" />)',"lname");
			  
			  frmVal.aV("userName","req",'<s:text name="msg.pls.enter" /> <s:text name="BOUser"/> <s:text name="label.name" />',"user");
			  frmVal.aV("userName","regexp=[^A-Za-z0-9àâäèéêëîïìôœùûüÿçÀÂÄÈÉÊËÎÏÔŒÙÛÜŸÇ\\_]",'<s:text name="error.invalid.val" />',"user");
			  
			  frmVal.aV("email","req",'<s:text name="error.email.incorrectemail" />',"Email");
			  frmVal.aV("email","email",'<s:text name="error.email.incorrectemail" />',"Email");
			  
			  frmVal.aV("phone","req",'<s:text name="error.phno.empty" />',"Phone");
			  frmVal.aV("phone","numeric",'<s:text name="error.phno.incorrectvalue" />',"Phone");
			
			  frmVal.aV("secQues","dontselect=0",'<s:text name="error.secretque.empty" />',"sQues");
			  
			  frmVal.aV("secAns","req",'<s:text name="error.secretans.empty" />',"sAns");
			  
			 // alert(document.error_disp_handler.isValid);
			return document.error_disp_handler.isValid;
			
			}
		</script>
		<s:head theme="ajax" debug="false" />
	</head>


	<body onload="_id.finiFocus('newsubuser','firstName')">
		<%@include file="/com/skilrock/lms/web/loginMgmt/menu.jsp"%>

		<div id="wrap">

			<div id="top_form">
				<h3>
					<s:text name="label.new.sub.usr.reg" />
				</h3>
				<%
					UserInfoBean user = null;
					user = (UserInfoBean) session.getAttribute("USER_INFO");
					System.out.println("Name:--" + user.getUserName() + " Head "
							+ user.getIsRoleHeadUser() + " Role  "
							+ user.getIsMasterRole());
					String isRoleHeadUser = user.getIsRoleHeadUser();
					String isMasterRole = user.getIsMasterRole();
					if (isRoleHeadUser.equalsIgnoreCase("Y")) {
				%>
				<s:form name="newsubuser" action="bo_um_addBoSubUser_Save"
					onsubmit="return isST3BOUserValidation()">
					<table border="0" cellpadding="2" cellspacing="0" width="450">
						<font color="red"> <s:actionerror /></font>
						<tr>
							<td align="center" colspan="2">
								<div id="fname"></div>
							</td>
						</tr>
						<s:textfield key="label.fname" maxlength="30" name="firstName" required="true" />
						<tr>
							<td align="center" colspan="2">
								<div id="lname"></div>
							</td>
						</tr>
						<s:textfield key="label.lname" maxlength="30" name="lastName" required="true"/>
						<tr>
							<td align="center" colspan="2">
								<div id="user"></div>
							</td>
						</tr>

						<s:textfield key="label.login.name" maxlength="30" name="userName" required="true"/>

						<tr>
							<td align="center" colspan="2">
								<div id="Email"></div>
							</td>
						</tr>

						<s:textfield key="label.email" maxlength="42" name="email" required="true" />
						<tr>
							<td align="center" colspan="2">
								<div id="Phone"></div>
							</td>
						</tr>


						<s:textfield key="label.ph.no" maxlength="15" name="phone"
							id="phone" required="true"/>


						<tr>
							<td align="center" colspan="2">
								<div id="Role"></div>
							</td>
						</tr>


						<s:textfield key="label.status" readonly="true" name="status"
							value="ACTIVE" />

						<tr>
							<td align="center" colspan="2">
								<div id="sQues"></div>
							</td>
						</tr>

						<s:select key="label.secquestion" name="secQues" cssClass="option"
							headerKey="-1" headerValue="%{getText('label.please.select')}"
							list="#{'What is Your Pets Name?':getText('whats.ur.pets.name'),'Which is your fav game?':getText('which.is.ur.fav.game'),'What is your college name?':getText('what.is.ur.college.name')}"
							value="gameType" required="true"/>

						<tr>
							<td align="center" colspan="2">
								<div id="sAns"></div>
							</td>
						</tr>
						<s:textfield name="secAns" key="label.secanswer" maxlength="30" required="true" />


						<tr>
							<td></td>
							<td>
								<table>
									<s:submit key="btn.reg" align="left" cssClass="button" />
								</table>
							</td>
						</tr>

					</table>
				</s:form>

				<%
					} else {
				%>
				<div>
					<jsp:include page="/com/skilrock/lms/web/loginMgmt/unauthorize.jsp"></jsp:include>
				</div>
				<%
					}
				%>
			</div>
		</div>
	</body>
</html>
<code id="headId" style="visibility: hidden">
	$RCSfile: bo_um_addBoSubUser_Menu.jsp,v $ $Revision: 1.6 $
</code>