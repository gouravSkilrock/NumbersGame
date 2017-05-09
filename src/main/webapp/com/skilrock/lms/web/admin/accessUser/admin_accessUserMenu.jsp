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
		<script>
			var data="";
			var orgTypeMap=new Object();
			function fillDetail(){
				data=(document.getElementById('userData').value).replace("{","").replace("}","").replace(/ /g,"");
				var dataArr=data.split("],");
				var orgType=_id.o('orgType');
				orgType.options.length=1;
				for(var i=0;i<dataArr.length;i++){
					var dataValue=dataArr[i].split("=");
					orgTypeMap[dataValue[0]]=dataValue[1].replace("[","").replace("]","");
					orgType.options[i+1]=new Option(dataValue[0],dataValue[0]);
				}
			}
			function getUser(orgType){
				var userNameList=orgTypeMap[orgType].split(",");
				var userName=_id.o('userName');
				userName.options.length=1;
				for(var i=0;i<userNameList.length;i++){
					userName.options[i+1]=new Option(userNameList[i],userNameList[i]);
				}
			}
		</script>
	</head>
	<body onload="fillDetail()">
		<%@include file="/com/skilrock/lms/web/loginMgmt/menu.jsp"%>
		<div id="wrap">
			<div id="top_form">
				<h3>
					Access Any User
				</h3>
				<s:form name="accessUser" action="admin_loginAsAnyUserSuccess" target="_top">
					<table border="0" cellpadding="2" cellspacing="0">
						<s:hidden name="userData" id="userData" value="%{userData}"></s:hidden>
						<s:select cssClass="option" id="orgType" name="orgType"
							label="Select Orgnization Type" list="{}" headerKey="-1"
							headerValue="--Please Select--" onchange="getUser(this.value)"></s:select>
						<s:select cssClass="option" id="userName" name="userName"
							label="Select User Name" list="{}" headerKey="-1"
							headerValue="--Please Select--"></s:select>
						<s:submit value="Log In" cssClass="button"></s:submit>
					</table>
				</s:form>
			</div>
		</div>
	</body>
</html>
<code id="headId" style="visibility: hidden">
	$RCSfile: admin_accessUserMenu.jsp,v $ $Revision: 1.1.2.1 $
</code>