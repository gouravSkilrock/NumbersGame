<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%response.setDateHeader("Expires",
   System.currentTimeMillis() + 10*24*60*60*1000);%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">

<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<link rel="stylesheet" href="<%=request.getContextPath() %>/LMSImages/css/styles.css"  type="text/css"/>
	<title><%=application.getAttribute("JSP_PAGE_TITLE")%></title>
  </head>
  
  <body>
  <%@include file="/com/skilrock/lms/web/loginMgmt/menu.jsp"%>
     <s:if test="%{#session.STATUS=='success'}">
  	<h3><s:text name="msg.inv.return.success"/></h3>
  	</s:if>
  	<s:else>
  	<h3><s:text name="msg.inv.detail.mismatch.try.again"/></h3>
  	</s:else>
  </body>
</html>
<code id="headId" style="visibility: hidden">
$RCSfile: agt_im_consNonConsReturnInv_Success.jsp,v $
$Revision: 1.3 $
</code>