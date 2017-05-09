
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="com.skilrock.lms.common.utility.FormatNumber"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%response.setDateHeader("Expires",
   System.currentTimeMillis() + 10*24*60*60*1000);%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">

<head>



<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />

<link rel="stylesheet" href="<%=request.getContextPath() %>/LMSImages/css/styles.css" type="text/css"/>

<title><%=application.getAttribute("JSP_PAGE_TITLE")%></title>
	


	</head>

<body>
<%@include file="/com/skilrock/lms/web/loginMgmt/menu.jsp"%>


<s:set name="extendedLimitStr" value="#session.extendedLimit"></s:set>
	<s:set name="aca" value="#session.ACA"></s:set>
	<%
		if (pageContext.getAttribute("aca").toString().contains(
					"Amount")) {
	%>
	<font color=red><s:property value="#session.ACA" /></font>
	<%
		} else { %>
			 <s:set name="extendedLimitStr" value="#session.extendedLimit" ></s:set>
      <h3>Organization's Credit Limit has been extended by  <font color=red><%=FormatNumber.formatNumberForJSP(pageContext.getAttribute("extendedLimitStr")) %></font> <s:property  value="%{#application.currencyWord}" /> for <font color=red><s:property value="#session.extendedLimitForDays"/></font> days only successfully</h3>
	<%
		}
	%>								
<%
	session.removeAttribute("ACA");
%>
</body>
</html>
<code id="headId" style="visibility: hidden">
$RCSfile: agt_um_extendCrLimit_Success.jsp,v $
$Revision: 1.2 $
</code>