<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<%
	response.setHeader("Cache-Control","no-store"); //HTTP 1.1
	response.setHeader("Pragma","no-cache"); //HTTP 1.0
	response.setDateHeader ("Expires", 0); //prevents caching at the proxy server
%>
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">

<head>

  </head>
  
  <body>
    <%@include file="/com/skilrock/lms/web/loginMgmt/menu.jsp" %>
    <h5>This Ticket Can Only Be Claimed At Retailer End</h5>
  </body>
</html>
<code id="headId" style="visibility: hidden">
$RCSfile: pwtError.jsp,v $
$Revision: 1.3 $
</code>