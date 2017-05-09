<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="com.skilrock.lms.common.utility.FormatNumber"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">

<head>



		<meta http-equiv="Content-Type"
			content="text/html; charset=iso-8859-1" />

				<link rel="stylesheet" href="<%=request.getContextPath() %>/LMSImages/css/styles.css"
			type="text/css" />

		<title><%=application.getAttribute("JSP_PAGE_TITLE")%></title>
		
		
	</head>
	
<body>
<%@include file="/com/skilrock/lms/web/loginMgmt/menu.jsp" %>
 
<s:form>
			
			<table>
			<tr>
			<td>  <s:text name="Good_Cause"/> Rs. <%= FormatNumber.formatNumberForJSP((Double)session.getAttribute("amount"))%></td> 
			<td>For Game <s:property  value="#session.GAME_DETAILS.{gameName}" /> has been Submitted</td>
			</tr>
			<tr>
			   <td align="left">
			      <a  href="<%=request.getContextPath() %>/com/skilrock/lms/web/reportsMgmt/rep_common_openPDF.action" target="_blank">Show Receipt</a>
			  </td>
			</tr>	
			<tr>
			  <td align="left"><a theme="simple" href="bo_act_gudCauseSubmit_Menu.action">Submit Another Good Cause</a></td>
			</tr>	
	
			</table>
			
		</s:form>
</body>
</html>
<code id="headId" style="visibility: hidden">
$RCSfile: bo_act_gudCauseSubmit_Success.jsp,v $
$Revision: 1.2 $
</code>