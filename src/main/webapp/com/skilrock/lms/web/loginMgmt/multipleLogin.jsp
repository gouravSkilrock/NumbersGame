<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en">

	<head>
			<meta http-equiv="Content-Type"
			content="text/html; charset=UTF-8" />

		<link rel="stylesheet"
			href="<%=request.getContextPath()%>/LMSImages/css/styles.css"
			type="text/css" />

		<title><%=application.getAttribute("JSP_PAGE_TITLE")%></title>

	
		
	</head>

   <body>
   <div align="center">
   <s:if test="%{#application.COUNTRY_DEPLOYED=='ZIMBABWE'}"> 
		<span style="float:none; top:0px; left:0px"><img src="<%=request.getContextPath() %>/LMSImages/images/headerphoto_ZIM.gif"></span>
		</s:if>
		<s:elseif test="%{#application.COUNTRY_DEPLOYED=='GHANA'}">
			<span style="float:none; top:0px; left:0px"><img src="<%=request.getContextPath() %>/LMSImages/images/headerphoto_GHA.gif"></span>
		</s:elseif>
		<s:elseif test="%{#application.COUNTRY_DEPLOYED=='NIGERIA'}">
			<span style="float:none; top:0px; left:0px"><img src="<%=request.getContextPath() %>/LMSImages/images/headerphoto_NIG.gif"></span>
		</s:elseif>
		<s:elseif test="%{#application.COUNTRY_DEPLOYED=='BENIN'}">
			<span style="float:none; top:0px; left:0px"><img src="<%=request.getContextPath() %>/LMSImages/images/BeninLogoLogin.jpg"></span>
		</s:elseif>
		<s:elseif test="%{#application.COUNTRY_DEPLOYED=='SIKKIM'}">
			<span style="float:none; top:0px; left:0px"><img src="<%=request.getContextPath() %>/LMSImages/images/BeninLogoLogin.jpg"></span>
		</s:elseif>
		<s:elseif test="%{#application.COUNTRY_DEPLOYED=='PHILIP'}">
			<span style="float:none; top:0px; left:0px"><img src="<%=request.getContextPath() %>/LMSImages/images/banner_philip.gif"></span>
		</s:elseif>
		</div>
   	<div id="wrap">

			<div id="top_form">
 		 <h2><s:text name="you.loggin.diff.brwser.same.user"></s:text></h2>

  <a href="<%=request.getContextPath()%>/com/skilrock/lms/web/loginMgmt/Logout.action">Please Click Here To Login</a>
  </div></div>
  </body>
</html>
<code id="headId" style="visibility: hidden">
$RCSfile: multipleLogin.jsp,v $
$Revision: 1.3 $
</code>