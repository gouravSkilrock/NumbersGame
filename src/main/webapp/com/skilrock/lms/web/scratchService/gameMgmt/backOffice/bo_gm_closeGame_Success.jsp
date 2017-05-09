<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">

<head>



<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />

<link rel="stylesheet" href="<%=request.getContextPath() %>/LMSImages/css/styles.css"  type="text/css"/>

<title><%=application.getAttribute("JSP_PAGE_TITLE")%></title>
	

<s:head theme="ajax" debug="false"/>
	</head>
<body>
	<%@include file="/com/skilrock/lms/web/loginMgmt/menu.jsp"%>
	<div id="wrap">

  <div id="top_form">
 <h3 > <s:text name="label.game"/> <s:property value="#session.GAME_NAME"/> (<s:text name="label.nbr"/> <s:property value="#session.GAME_NBR"/>)<s:text name="label.terminated"/></h3> 
<s:form>
			
			<table border="0" cellpadding="2" cellspacing="0">
			<tr>
			  <td><s:a theme="simple" href="bo_gm_closeGame_Menu.action"><s:text name="label.back.see.close.games"/></s:a></td>
			</tr>
			
			</table>
			
		</s:form></div></div>
</body>
</html>
<code id="headId" style="visibility: hidden">
$RCSfile: bo_gm_closeGame_Success.jsp,v $
$Revision: 1.3 $
</code>