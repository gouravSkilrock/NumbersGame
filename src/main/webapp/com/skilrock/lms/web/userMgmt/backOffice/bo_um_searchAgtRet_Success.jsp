
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%response.setDateHeader("Expires",
   System.currentTimeMillis() + 10*24*60*60*1000);%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en">

<head>



<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />

<link rel="stylesheet" href="<%=request.getContextPath() %>/LMSImages/css/styles.css" type="text/css"/>

<title><%=application.getAttribute("JSP_PAGE_TITLE")%></title>
	

	</head>

<body>
<%@include file="/com/skilrock/lms/web/loginMgmt/menu.jsp"%>
<div id="wrap">

  <div id="top_form">
  <s:if test="#session.RETURN_STATUS == 'NEGATIVE_ACTIVE'">
  	<h3><font style="color:red"> <s:property value="#session.ORG_NAME"/></font> <s:text name="msg.org.stat.cnnt.be"/> <font style="color:red">'<s:text name="ACTIVE"/>'</font> <s:text name="msg.due.to.neg.bal"/>.</h3>
  </s:if>
  <s:else><h3><font style="color:red"> <s:property value="#session.ORG_NAME"/></font> <s:text name="msg.org.detail.edit.success"/>!</h3>
  </s:else>
   

		</div></div>	
</html>
<code id="headId" style="visibility: hidden">
$RCSfile: bo_um_searchAgtRet_Success.jsp,v $
$Revision: 1.3 $
</code>