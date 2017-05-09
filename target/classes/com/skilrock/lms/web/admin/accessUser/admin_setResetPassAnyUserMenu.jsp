<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">

	<head>
		<meta http-equiv="Content-Type"
			content="text/html; charset=iso-8859-1" />
		<link rel="stylesheet"
			href="<%=request.getContextPath()%>/LMSImages/css/styles.css"
			type="text/css" />
		<title><%=application.getAttribute("JSP_PAGE_TITLE")%></title>
		<s:head theme="ajax" debug="false" />
	</head>
	<script>
	function updatePass(newPass){
	var rowId = newPass.replace("newPass","");
	var newPassword=_id.v(newPass);//alert(newPassword);
	_id.o(newPass).value="";
	if(newPassword==""){
		alert("Please Enter New Password");
		return false;
	}
	_ajaxCall((_id.o('href'+rowId).href)+"&autoPass="+newPassword,"res"+rowId);	
	}
	function changeLabel(){
		var val = _id.o('loginID').innerHTML;
		if(_id.o('result').innerHTML=='Successfully Done!!'){
		if(val=="Allow LogIn All Users"){
		_id.i('loginID','Stop LogIn All Users');
		}else{
		_id.i('loginID','Allow LogIn All Users');
		}
		}
	}
	</script>

	<body>
		<%@include file="/com/skilrock/lms/web/loginMgmt/menu.jsp"%>
		<div id="wrap">
			<div id="top_form">
				<h3>
					Set Reset Password Any User
				</h3>
				<s:form name="setResetPass"
					action="admin_setResetPasswordAnyUserSearch">
					<table border="0" cellpadding="2" cellspacing="0" width="450">
						<s:select cssClass="option" id="orgType" name="orgType"
							label="Select Orgnization Type" list="%{#application.TIER_MAP}"
							headerKey="-1" headerValue="--Please Select--"></s:select>
						<s:submit id="search" align="left" theme="ajax" targets="bottom"
							value="Search" cssClass="button" />
						<tr>
							<td>
								<s:a href="admin_logoutAllUser.action" theme="ajax"
									targets="result">LogOut All Users</s:a>
							</td>
						</tr>
						<tr>
							<td>
								<s:a href="admin_stopLogInAllUser.action" theme="ajax"
									targets="result" notifyTopics="changeLabel()" id="loginID">
									<s:property value="stopLogIn" />
								</s:a>
							</td>
						</tr>
					</table>
				</s:form>
				<div id="bottom">
				</div>
				<div id="result"></div>
			</div>
		</div>
	</body>
</html>
<code id="headId" style="visibility: hidden">
	$RCSfile: admin_setResetPassAnyUserMenu.jsp,v $ $Revision: 1.1.2.2 $
</code>