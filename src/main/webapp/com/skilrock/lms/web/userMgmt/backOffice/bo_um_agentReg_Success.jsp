<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">

<head>



<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />

<link rel="stylesheet" href="<%=request.getContextPath() %>/LMSImages/css/styles.css"  type="text/css"/>

<title><%=application.getAttribute("JSP_PAGE_TITLE")%></title>
	

	</head>

<body>

<%@include file="/com/skilrock/lms/web/loginMgmt/menu.jsp" %>
<div id="wrap">

  <div id="top_form">
   <h3><s:property value="#application.TIER_MAP.AGENT" /> <s:text name="label.org"/>: <font color=red><s:property  value="#session.ORGANIZATION_NAME"/></font> <s:text name="label.has.been.created.success"/></h3>
   <h3><s:property value="#application.TIER_MAP.AGENT" /> <s:text name="label.org.code"/>: <font color=red><s:property  value="#session.ORG_CODE"/></font></h3>
    <h5><s:text name="label.pass.sent.to.mail"/></h5>

<s:form>
		
			<s:a theme="simple" href="bo_um_agentReg_Menu.action?request_locale=%{#session.WW_TRANS_I18N_LOCALE}"><s:text name="msg.reg.auth"/> <s:property value="#application.TIER_MAP.AGENT" /> <s:text name="label.org"/></s:a>
		</s:form>
		</div></div>
</body>
</html>
<code id="headId" style="visibility: hidden">
$RCSfile: bo_um_agentReg_Success.jsp,v $
$Revision: 1.3 $
</code>