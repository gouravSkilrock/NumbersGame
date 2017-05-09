<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<head>
<title><%=application.getAttribute("JSP_PAGE_TITLE")%></title>
<link rel="stylesheet" href="<%=request.getContextPath() %>/LMSImages/css/styles.css" type="text/css"/>
<%System.out.println(application.getAttribute("ERROR_TIME")); %>
<script>
var brNmVer="",err="<%=application.getAttribute("ERROR_TIME")%>";frm0=parent.frames[0],logJsErr = "<%=request.getContextPath() %>/com/skilrock/lms/web/loginMgmt/logJsError.action?message=";
if(err==""||err=="null")err="Error Not Catched at Server";
if(frm0.BrowserDetect){		
		_bd=frm0.BrowserDetect;
		brNmVer="Browser--"+_bd.browser+" Ver --"+_bd.version+" OS--"+_bd.OS;
		logJsErr = logJsErr+"--"+brNmVer+"--"+err;
}
if(frm0.document.getElementById("currentLoggedUser"))
orgName="&agentOrgName="+frm0.document.getElementById("currentLoggedUser").childNodes[0].nodeValue;
frm0.ajaxReq(logJsErr+orgName+"&errTime="+err,"jsErr");
</script>
</head>
<html>
	<body>
   <h3><s:text name="msg.some.internal.error.try.after.some.time" /></h3>
	</body>
</html>

<code id="headId" style="visibility: hidden">
$RCSfile: applicationError.jsp,v $
$Revision: 1.3 $
</code>