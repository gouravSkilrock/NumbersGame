
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
	
	<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
	
	<head>
		
		<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
	
	 <link rel="stylesheet" href="<%=request.getContextPath() %>/LMSImages/css/styles.css"  type="text/css"/>
		<title><%=application.getAttribute("JSP_PAGE_TITLE")%></title>
	<s:head theme="ajax" debug="false"/>
	</head>

	<body>
	
	    <%@include file="/com/skilrock/lms/web/loginMgmt/menu.jsp" %>
	     <div id="wrap">
         <div id="top_form">
 
		<h3>
			Ticket Validation Before <s:text name="PWT"/> Receipts
		</h3>
		<h3>
			Following Tickets Has Been Validated and Processed for Acceptance
		</h3>
		<s:form>
		<table border="1" width="50%" cellpadding="3" cellspacing="0">
			<tr>
				<td width="60%" align="center">
					<s:text name="Ticket Number" />
				</td>
				<td width="40%" align="center">
					<s:text name="Validity Status" />
				</td>
			</tr>

			<s:iterator value="#session.SAVED_TICKET_LIST">
				<tr>
					<td width="60%">
						<s:property value="%{ticketNumber}" />
					</td>
					<td width="40%">
						<s:property value="%{status}" />
					</td>
				</tr>
			</s:iterator>
		</table>
		
		<table width="50%">
			<tr></tr>
			<tr></tr>
			<tr></tr>
			<tr></tr>
			<tr></tr>
			<tr></tr>
			<tr></tr>
			<tr>
				<td align="left">
					
				</td>
				<td width="50%" align="right">
					<a href="bo_pwt_tktVerification_Menu.action">More Ticket Check</a>
				</td>
			</tr>
		</table>
		</s:form>
		</div>
		</div>
	</body>
</html>
<code id="headId" style="visibility: hidden">
$RCSfile: bo_pwt_tktVerification_Success.jsp,v $
$Revision: 1.2 $
</code>