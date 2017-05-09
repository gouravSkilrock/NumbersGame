<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="com.skilrock.lms.common.utility.FormatNumber"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">

<head>



<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />

<link rel="stylesheet" href="<%=request.getContextPath() %>/LMSImages/css/styles.css"  type="text/css"/>

<title><%=application.getAttribute("JSP_PAGE_TITLE")%></title>
	

	</head>
	<body>
	
	<%@include file="/com/skilrock/lms/web/loginMgmt/menu.jsp"%>
	
	<div id="wrap">

  <div id="top_form">
   <h3>Final <s:text name="PWT"/> Receipt</h3>
	
		<s:form>
			<s:if test="%{#session.GEN_RCT_SUC}">
			<h3>The Pwt of Rs : 
				<s:set name="amount" value="%{#session.AMOUNT}"/><%=FormatNumber.formatNumberForJSP(pageContext.getAttribute("amount"))%>
				
				is valid and should be paid</h3>
			</s:if>
			<table width="400" border="1" cellpadding="3" cellspacing="0" bordercolor="#CCCCCC" align="left">
			<tr>
			  <td><s:a theme="simple" href="bo_pwt_bulkFinalRcpt_Menu.action">Back To Search</s:a></td>
			</tr>
			<s:if test="%{#session.GEN_RCT_SUC}">
			   <tr>  
			      <td><a theme="simple" target="_blank" href="<%=request.getContextPath()%>/com/skilrock/lms/web/reportsMgmt/rep_common_openPDF.action">Show Receipt</a></td>
			   </tr>
			</s:if>
			<s:else>
				 <tr>  
			      <td>No Valid Pwt To Pay </td>
			   </tr>
			</s:else>
			</table>

		</s:form>
</div></div>
	</body>
</html>
<code id="headId" style="visibility: hidden">
$RCSfile: bo_pwt_bulkFinalRcpt_Success.jsp,v $
$Revision: 1.2 $
</code>