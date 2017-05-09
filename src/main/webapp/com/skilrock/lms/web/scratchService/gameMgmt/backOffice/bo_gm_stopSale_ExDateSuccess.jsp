<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>


<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
<s:head  debug="false"/>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
        <link rel="stylesheet" href="<%=request.getContextPath() %>/LMSImages/css/styles.css" type="text/css"/>
		<title><%=application.getAttribute("JSP_PAGE_TITLE")%></title>
</head>
<body>
<%@include
				file="/com/skilrock/lms/web/loginMgmt/menu.jsp"%>
				
				<div id="wrap">

			    <div id="top_form">
<s:form action="bo_gm_stopSale_Menu.action" method="POST" >

	<s:actionerror />
         <s:fielderror />
         		   
  <h3> <s:text name="label.you.have.successfully.changed.dates"/> </h3>
  <h3><s:text name="label.new.sale.end.date"/>  : <s:property value="#session.SALE_END_DATE"/></h3>
  <h3><s:text name="label.new.pwt.end.date"/>   : <s:property value="#session.PWT_END_DATE"/></h3>
  
  	<s:submit align="center" key="btn.back.game.details" cssClass="button" />
</s:form>
</div>
</div>
</body>
</html>
<code id="headId" style="visibility: hidden">
$RCSfile: bo_gm_stopSale_ExDateSuccess.jsp,v $
$Revision: 1.3 $
</code>