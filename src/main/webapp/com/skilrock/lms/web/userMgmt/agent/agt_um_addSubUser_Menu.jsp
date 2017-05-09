
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="com.skilrock.lms.beans.UserInfoBean"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">

	<head>



		<meta http-equiv="Content-Type"
			content="text/html; charset=iso-8859-1" />

		<link rel="stylesheet" href="<%=request.getContextPath() %>/LMSImages/css/styles.css"
			type="text/css" />

		<title><%=application.getAttribute("JSP_PAGE_TITLE")%></title>


		

		<script>
function isST3BOUserValidation(){

 var frmVal  = new parent.frames[0].Validator("newsubuser");

  frmVal.aV("firstName","req","Please Enter First Name","fname");
  frmVal.aV("firstName","alpha","Please Enter Valid Name","fname");
  
  frmVal.aV("lastName","req","Please Enter Last Name","lname");
  frmVal.aV("lastName","alpha","Please Enter Valid Name (Special Characters and Numbers not Allowed)","lname");
  
  frmVal.aV("userName","req","Please Enter "+tierMap['AGENT']+" Name","user");
  frmVal.aV("userName","regexp=[^A-Za-z0-9\\_]","Please Enter Valid Value","user");
  
  
  frmVal.aV("email","req","Please Enter Email Address","Email");
  frmVal.aV("email","email","Please Enter Correct Email Address","Email");
  
  frmVal.aV("phone","req","Please Enter Phone Number","Phone");
  frmVal.aV("phone","numeric","Please Enter Correct Value For Phone Number","Phone");

   frmVal.aV("secQues","dontselect=0","Please Select Secret Question","sQues");
  
  frmVal.aV("secAns","req","Please Enter Secret Answer","sAns");
  
 // alert(document.error_disp_handler.isValid);
return document.error_disp_handler.isValid;

}


</script>
		<s:head theme="ajax" debug="false" />
	</head>


	<body onload="_id.finiFocus('newsubuser','firstName')">
	<%@include
				file="/com/skilrock/lms/web/loginMgmt/menu.jsp"%>

		<div id="wrap">

			<div id="top_form">
				<h3>
					New Sub User registration
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
				<s:form name="newsubuser" action="agt_um_addSubUser_Save" onsubmit="return isST3BOUserValidation()">
					<table border="0" cellpadding="2" cellspacing="0" width="450">
						<font color="red"><s:actionerror /></font>
						<tr>
							<td align="center" colspan="2">
								<div id="fname"></div>
							</td>
						</tr>
						<s:textfield label="First Name" maxlength="30" name="firstName" />
						<tr>
							<td align="center" colspan="2">
								<div id="lname"></div>
							</td>
						</tr>
						<s:textfield label="Last Name" maxlength="30" name="lastName" />
						<tr>
							<td align="center" colspan="2">
								<div id="user"></div>
							</td>
						</tr>

						<s:textfield label="Login Name" maxlength="30" name="userName" />
						
						<tr>
							<td align="center" colspan="2">
								<div id="Email"></div>
							</td>
						</tr>

						<s:textfield label="Email" maxlength="42" name="email" />
						<tr>
							<td align="center" colspan="2">
								<div id="Phone"></div>
							</td>
						</tr>


						<s:textfield label="Phone Number" maxlength="15" name="phone"
							id="phone" />

						


						<tr>
							<td align="center" colspan="2">
								<div id="Role"></div>
							</td>
						</tr>


						<s:textfield label="Status" readonly="true" name="status"
							value="ACTIVE" />

						<tr>
							<td align="center" colspan="2">
								<div id="sQues"></div>
							</td>
						</tr>

						<s:select label="Secret Question " name="secQues"
							cssClass="option" headerKey="-1"
							headerValue="-- Please Select --"
							list="{'What is Your Pets Name?','Which is your favourite game?','What is your college name?'}"
							value="gameType" />

						<tr>
							<td align="center" colspan="2">
								<div id="sAns"></div>
							</td>
						</tr>
						<s:textfield name="secAns" label="Secret Answer" maxlength="30" />


						<tr><td></td><td><table><s:submit value="Register" align="left" cssClass="button" /></table></td></tr>
						
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
$RCSfile: agt_um_addSubUser_Menu.jsp,v $
$Revision: 1.4 $
</code>