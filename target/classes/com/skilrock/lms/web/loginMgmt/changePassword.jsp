<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%response.setDateHeader("Expires",
   System.currentTimeMillis() + 10*24*60*60*1000);%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">

<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<link rel="stylesheet" href="<%=request.getContextPath() %>/LMSImages/css/styles.css"
		type="text/css" />

	<title><%=application.getAttribute("JSP_PAGE_TITLE")%></title>

	<script type="text/javascript" src="js/changePass.js"></script>

	<script>

	function isEqual()
	{
	 
	if(document.changePass.newPassword.value!=document.changePass.verifynewPassword.value)
	  {
	   alert('<s:text name="error.verifyNew.pass"/>');
	   document.changePass.verifynewPassword.focus();
	   return false;
	  }
	  else
	   if(document.changePass.password.value==document.changePass.newPassword.value)
	   {
	    alert('<s:text name="error.thisIs.ur.curr.pass"/>');
	    document.changePass.newPassword.focus();
	    return false;
	   }
	   else
	    alert('<s:text name="error.enterNew.pass"/>');
	   	
	}

</script>
</head>
	<body onload="initialFocus()">
	
		<s:if test="#session.FIRST=='false'">
		<%@include file="/com/skilrock/lms/web/loginMgmt/menu.jsp"%>
		</s:if>
		<div id="wrap">

  <div id="top_form">
   <h3><s:text name="label.change.pass"/> </h3>
		<s:form onsubmit="return isST3Filled()" name="changePass"
			action="pass" method="POST" >
			<table border="0" cellpadding="2" cellspacing="0">
				<tr>
					<td colspan="2">
						<s:actionerror />
						<s:fielderror />
					</td>
				</tr>
				<tr>
					<td></td>
				</tr>
				<tr>
					<td>
						<div id="currentPass" align="center"></div>
					</td>
				</tr>
				<tr>
					<td>
						<div id="oldPass" align="center"></div>
					</td>
				</tr>
				<tr>
					<td>
						<s:password name="password" key="label.old.pass" required="true" />
					</td>
				</tr>
				<tr>
					<td>
						<div id="newPass"></div>
					</td>
				</tr>
				<tr>
					<td>
						<s:password name="newPassword" key="label.new.pass" required="true" />
					</td>
				</tr>
				<tr>
					<td>
						<div id="verifyPass"></div>
					</td>
				</tr>
				<tr>
					<td>
						<s:password name="verifynewPassword" key="label.re.enter.new.pass" required="true" />
					</td>
				</tr>
				<tr>
					<td>
						<s:submit cssClass="button" key="btn.change" align="center"
							targets="down" required="true" />
					</td>
					<s:if test="#session.FIRST">
					<td>
					   <a href="login.jsp"><s:text name="msg.back.to.login.page"/>
					   </a>
					 </td>   
					 </s:if>
				</tr>

			</table>
		</s:form>
			</div></div>
		<div id="down"></div>

	</body>
</html>
<code id="headId" style="visibility: hidden">
$RCSfile: changePassword.jsp,v $
$Revision: 1.3 $
</code>