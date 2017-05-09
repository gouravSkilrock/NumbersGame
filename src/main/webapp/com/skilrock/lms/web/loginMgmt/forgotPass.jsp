<%@ taglib prefix="s" uri="/struts-tags" %>
	<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
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
		<div align="center">
		<span style="float:none; top:0px; left:0px"><img src="<%=request.getContextPath() %>/LMSImages/images/headerphoto.gif" /></span>
		</div>
	<div id="wrap">

  <div id="top_form">
  
   		 <h3>Forgot Password </h3>
   <s:form action="assignPassword" method="POST" >
   
  
   <table  cellpadding="4" cellspacing="0" border="0">
   <tr><td></td></tr>
   <tr>
   <td colspan="2"><s:actionerror/>   <s:actionmessage/></td>
   </tr>
   	<tr><td> </td></tr>
<tr>
			<td colspan="2" align="center">Login Name</td>
			<td align="left"><s:textfield theme="simple" name="userName" /></td>
</tr>

  

<tr><td align="center">
<s:submit theme="simple" value="Submit" align="center" cssClass="button"/>
</td></tr>
</table>
</s:form>
		</div></div>
</body>
</html>
<code id="headId" style="visibility: hidden">
$RCSfile: forgotPass.jsp,v $
$Revision: 1.3 $
</code>