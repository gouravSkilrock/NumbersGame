<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="com.skilrock.lms.common.utility.FormatNumber"%>
<%@ taglib prefix="s" uri="/struts-tags"%>


<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
	<head>
		<title><%=application.getAttribute("JSP_PAGE_TITLE")%></title>
		<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
	
	   <link rel="stylesheet" href="<%=request.getContextPath() %>/LMSImages/css/styles.css" type="text/css"/>
	</head>

	<body>
	<%@include
				file="/com/skilrock/lms/web/loginMgmt/menu.jsp"%>
		<div id="wrap">
			<div id="top_form">
			 <h3>Direct Player <s:text name="PWT"/> Tickets Verification Done</h3>
			<table>

				  		<s:iterator id="SearchGameResults" value="#session.Receipt_Id">
						    <tr>
						     	<td><s:text name="Receipt Id">Receipt Id:</s:text> <td> <s:property value="%{intValue()}"/></td>
						     
						   </tr>
						  <tr>  
						  	<td><a theme="simple" target="_blank" href="<%=request.getContextPath() %>/com/skilrock/lms/web/reportsMgmt/rep_common_openPDF.action">Show Receipt</a></td>
						</tr>
					</s:iterator>
			  </table> 
			  
			<table border="1" cellpadding="3" cellspacing="0" bordercolor="#CCCCCC" align="center">				

				<tr><td>Game Number : <s:property value="#session.GAME_NBR"/>	</td></tr>
				<tr><td>Game Name : <s:property value="#session.GAME_NAME"/></td></tr>
					
					
				<tr><td>
					 <s:set name="pwtAmount" value="#session.PWT_AMT"/>
					       <%String amt=FormatNumber.formatNumberForJSP(pageContext.getAttribute("pwtAmount"));
					       		session.setAttribute("pwtAmt", amt);
					        %>
					    <s:text name="PWT"/> Amount : <s:property value="#session.PWT_AMT"/>  
					
					</td></tr>
				<tr><td><s:a href="startPendingPWT.action" >Another Pending PWTs Receive</s:a></td></tr>
			</table>
			
           
	</div></div>
	</body>
</html>
<code id="headId" style="visibility: hidden">
$RCSfile: playerPWTSuccess.jsp,v $
$Revision: 1.2 $
</code>