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
		<s:elseif test="%{#application.COUNTRY_DEPLOYED=='SAFARIBET'}">
			<span style="float:none; top:0px; left:0px"><img src="<%=request.getContextPath() %>/LMSImages/images/header_logo_safaribet.png"></span>
        </s:elseif>
		</div>
   	<div id="wrap">

			<div id="top_form">
            
      		<table width="700" border="0" cellspacing="0" cellpadding="4" style="border:#CCCCCC 1px solid" align="center">
        <tr>
        <td colspan="2" height="80" align="center"><b><big><s:text name="lottery.mgmt.system"></s:text></big></b></td>
        </tr><tr>
        <td width="101" align="center" bgcolor="#FFFFFF"><img src="<%=request.getContextPath() %>/LMSImages/images/session_time_out.gif" width="40" height="42" /></td>
        <td width="599"  bgcolor="#F2F2FF"><h2><s:text name="seesion.time.out.login.again"></s:text></h2> </td>
        </tr><tr>
        <td colspan="2"  height="80">
        <table width="650" border="0" cellspacing="0" cellpadding="8" align="center">
        <tr>
        <td colspan="2" align="center"><s:text name="for.ur.protctn.autmatic.logout.seesion.expire"></s:text><br /><br />  </td></tr><tr>
        <td colspan="2" align="center" bgcolor="#F4F4F4" style="border:#CCCCCC 1px solid"><b>Suggestion</b><br /><br /><s:text name="security.rsns.disable.out.logout.again"></s:text></td></tr><tr>
        <td align="left"><a href="<%=request.getContextPath()%>/com/skilrock/lms/web/loginMgmt/Logout.action"><s:text name="click.here.to.login.again"></s:text></a></td>
        <td>&nbsp;</td></tr> </table>
		</td></tr></table>
        
 
  </div></div>
  </body>
</html>
<code id="headId" style="visibility: hidden">
$RCSfile: loggedOut.jsp,v $
$Revision: 1.3 $
</code>