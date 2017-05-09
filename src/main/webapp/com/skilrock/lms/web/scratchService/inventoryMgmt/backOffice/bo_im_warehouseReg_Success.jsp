	<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
	<%@ taglib prefix="s" uri="/struts-tags"%>
	
	<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
	
	<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
	
	<head>
	
	
	
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	
	<link rel="stylesheet" href="<%=request.getContextPath() %>/LMSImages/css/styles.css" scrolling="no" type="text/css"/>
	
	<title><%=application.getAttribute("JSP_PAGE_TITLE")%></title>

		</head>
<body>


	<%@include file="/com/skilrock/lms/web/loginMgmt/menu.jsp" %>

	<div id="wrap">

  <div id="top_form">
   <h3><s:text name="label.warehouse"/>: <font color=red><s:property  value="#session.NEW_WH_REG"/></font> <s:text name="label.reg.success"/></h3>

 
<s:form>
			
			<table border="0" cellpadding="2" cellspacing="0">
			<tr>
			  <td><s:a theme="simple" href="bo_um_warehouseReg_Menu.action"><s:text name="label.reg.another.warehouse"/></s:a></td>
			</tr>
			
			</table>
			
		</s:form>
		</div></div>
</body>
</html>
<code id="headId" style="visibility: hidden">
$RCSfile: bo_im_warehouseReg_Success.jsp,v $
$Revision: 1.3 $
</code>