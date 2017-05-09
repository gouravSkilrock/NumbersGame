<%@ taglib prefix="s" uri="/struts-tags" %>
	<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
	<%@ taglib prefix="s" uri="/struts-tags"%>
	
	<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
	
	<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
	
	<head>
	
	
	
	<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
	
	<link rel="stylesheet" href="<%=request.getContextPath() %>/LMSImages/css/styles.css" type="text/css"/>
	
	<title><%=application.getAttribute("JSP_PAGE_TITLE")%></title>

		</head>
<body><div align="center">
		<span style="float:none; top:0px; left:0px"><img src="<%=request.getContextPath() %>/LMSImages/images/headerphoto.gif"></span>
		</div>
	<div id="wrap">

  <div id="top_form">
  
   		 <h3> Enter Security Answer</h3>

<s:form action="checkAns" method="POST" >
<table border="0" cellpadding="4" cellspacing="0">
<tr>
<td colspan="3" align="center" style="color: red">
	<s:property value="#session.UserQuesBean.secQues" />         
  </td></tr> 

		<tr>
		<td align="right">Security Answer :</td>
		<td align="left"><s:textfield theme="simple" label="Answer" name="secAnsUser" /></td>
		<td align="center"><s:submit theme="simple" value="Submit" align="center" cssClass="button"/></td>
		</tr>
</table>
</s:form>
		</div></div>
		
<body>
</html>
<code id="headId" style="visibility: hidden">
$RCSfile: secAns.jsp,v $
$Revision: 1.2 $
</code>