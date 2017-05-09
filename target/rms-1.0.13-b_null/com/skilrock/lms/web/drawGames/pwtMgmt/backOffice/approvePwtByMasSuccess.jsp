<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
	<head>
	    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<link rel="stylesheet" href="<%=request.getContextPath() %>/LMSImages/css/styles.css"  type="text/css"/>
	    <title><%application.getAttribute("JSP_PAGE_TITLE"); %></title>   
	</head>
	
		  <body>
		       <%@include file="/com/skilrock/lms/web/loginMgmt/menu.jsp" %>
		      <h3><s:text name="msg.req.has.approv.success" /></h3>
		       <table border="1" cellpadding="2" cellspacing="0" width="300px">
		           <tr>
		             <td>
		                <s:text name="label.remarks" />: 
				 		<s:property value="%{#session.REMARKS}"/>
		             </td>
		            </tr>  
		       </table>
		  
		  </body>

</html>
<code id="headId" style="visibility: hidden">
$RCSfile: approvePwtByMasSuccess.jsp,v $
$Revision: 1.3 $
</code>