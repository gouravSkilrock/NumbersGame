<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%
	response.setDateHeader("Expires", 0);
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en">

	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />

		<link rel="stylesheet"
			href="<%=request.getContextPath()%>/LMSImages/css/styles.css"
			type="text/css" />

		<title><%=application.getAttribute("JSP_PAGE_TITLE")%></title>
		<s:head theme="ajax" debug="false" />

		<script>
function isST3BOUserValidation(){
 var frmVal  = new parent.frames[0].Validator("newuser");
  
  frmVal.aV("firstName","req",'<s:text name="error.fname.empty"/>',"fname");
  frmVal.aV("firstName","alpha",'<s:text name="error.name.empty"/>',"fname");
  
  frmVal.aV("lastName","req",'<s:text name="error.lname.empty"/>',"lname");
  frmVal.aV("lastName","alpha",'<s:text name="error.name.empty"/>(<s:text name="error.spcl.char"/>)',"lname");
  
  frmVal.aV("userName","req",'<s:text name="msg.pls.enter"/> <s:text name="BOUser"/> <s:text name="label.name"/>',"user");
  frmVal.aV("userName","regexp=[^A-Za-z0-9àâäèéêëîïìôœùûüÿçÀÂÄÈÉÊËÎÏÔŒÙÛÜŸÇ\\_]",'<s:text name="error.invalid.val"/>',"user");
  
  
  frmVal.aV("email","req",'<s:text name="error.email.incorrectemail"/>',"Email");
  frmVal.aV("email","email",'<s:text name="error.email.incorrectemail"/>',"Email");
  
  frmVal.aV("phone","req",'<s:text name="error.phno.empty"/>',"Phone");
  frmVal.aV("phone","numeric",'<s:text name="error.phno.incorrectvalue"/>',"Phone");

  frmVal.aV("orgName","dontselect=0",'<s:text name="error.orgname.empty"/>',"orgError");
  frmVal.aV("role","dontselect=0",'<s:text name="error.role.empty"/>',"Role");
   frmVal.aV("secQues","dontselect=0",'<s:text name="error.secretque.empty"/>',"sQues");
  
  frmVal.aV("secAns","req",'<s:text name="error.secretans.empty"/>',"sAns");
  
  //alert(document.error_disp_handler.isValid);
  return document.error_disp_handler.isValid;

}
function getEmailPrivList(url, divId) {
//alert(url);
	_resp  = _ajaxCall(url);
 	if(_resp.result){
 	_respData=_resp.data;
 	if(_respData!=""){
 	_id.o('e-mail_privileges').style.display = 'block';
 	var emailPrivList = _respData.split(","),table = '<table border="0" cellpadding="3"><tr>',idAndTitle="";
	for(var i=0,len=emailPrivList.length; i<len; i++) {
		idAndTitle = emailPrivList[i].split("=");
		var tr = '<td nowrap="nowrap"><input type="checkbox" name="emailPrivId" value="'+idAndTitle[0].replace(" ", "")+'"/>'+
				'<label class="checkboxLabel">'+idAndTitle[1]+'</label></td>';
		table = table + tr ;		
	}
	table = table + '</tr></table>';
	_id.i(divId,table);
	}
	}else{
	_id.i(divId,'<s:text name="error"/>');
	}
 }
 function fetchRoles(url){
 	_resp  = _ajaxCall(url);
 	if(_resp.result){
 	_respData=_resp.data;
 				var newSel = _id.o('roleid');
 				var arr = _respData.replace('{','').replace('}','').split(',');
 				if(arr[0] != ''){
 					for(var i=0;i<arr.length;i++){	
		 				var opt = new Option(arr[i].split('=')[1],arr[i].split('=')[0]);
						newSel.options[i+1] = opt;
					}
 				}
	}else{
	_id.i(divId,'Error');
	}
 
 }
</script>
	</head>


	<body
		onload=" getEmailPrivList('bo_um_registration_FetchEmailPriv.action?privType=BO', 'emailPriv'),fetchRoles('um_common_fetchRoles.action'), onLoadAjax('getListAjax.action?listType=company','orgName'),_id.finiFocus('newuser','firstName')">

		<%@include file="/com/skilrock/lms/web/loginMgmt/menu.jsp"%>
		<div id="wrap">

			<div id="top_form">
				<h3>
					<s:text name="label.back.ofc.usr.reg" />
				</h3>

				<s:form name="newuser" action="bo_um_boUserReg_Save"
					onsubmit="return isST3BOUserValidation()">
					<table border="0" cellpadding="2" cellspacing="0" width="750">
						<s:actionerror />
						<s:fielderror />

						<tr>
							<td align="center" colspan="2">
								<div id="fname"></div>
							</td>
						</tr>
						<s:textfield key="label.fname" maxlength="30" name="firstName" required="true"/>
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

						<s:textfield key="label.email" maxlength="50" name="email" required="true"/>
						<tr>
							<td align="center" colspan="2">
								<div id="Phone"></div>
							</td>
						</tr>


						<s:textfield key="label.ph.no" maxlength="15" name="phone"
							id="phone" required="true"/>

						<tr>
							<td align="center" colspan="2">
								<div id="orgError"></div>
							</td>
						</tr>

						<s:select key="label.org" cssClass="option" id="orgName"
							name="orgName" headerKey="-1" headerValue="%{getText('label.please.select')}"
							list="{}" required="true"/>

						<tr>
							<td align="center" colspan="2">
								<div id="Role"></div>
							</td>
						</tr>

						<s:select key="label.role" name="role" id="roleid" headerKey="-1"
							headerValue="%{getText('label.please.select')}" list="{}" cssClass="option" required="true"></s:select>

						<s:textfield key="label.status" readonly="true" name="status"
							value="ACTIVE" />

						<tr>
							<td align="center" colspan="2">
								<div id="sQues"></div>
							</td>
						</tr>

						<s:select key="label.secquestion" name="secQues" headerKey="-1"
							headerValue="%{getText('label.please.select')}" cssClass="option"
							list="#{'What is Your Pets Name?':getText('whats.ur.pets.name'),'Which is your fav game?':getText('which.is.ur.fav.game'),'What is your college name?':getText('what.is.ur.college.name')}"
							value="gameType" required="true"/>

						<tr>
							<td align="center" colspan="2">
								<div id="sAns"></div>

							</td>
						</tr>
						<s:textfield name="secAns" key="label.secanswer" maxlength="30" required="true"/>

						<s:hidden label="Password" name="password" value="skilrock" />
						<s:hidden label="Organisation Id" name="orgId" />
						<s:hidden label="Organisation Type" name="orgType" />

						<tr>
							<td align="right" class="label" id="e-mail_privileges"
								style="display: none;">
								<s:text name="label.email.privileges" />
								:

							</td>
							<td>
								<div id="emailPriv"></div>
							</td>
						</tr>

						<tr>
							<td>
								&nbsp;
							</td>
							<td align="left">
								<s:submit theme="simple" key="btn.reg" cssClass="button" />
							</td>
						</tr>
					</table>


				</s:form>
			</div>
		</div>
	</body>
</html>
<code id="headId" style="visibility: hidden">
	$RCSfile: bo_um_boUserReg_Menu.jsp,v $ $Revision: 1.3 $
</code>