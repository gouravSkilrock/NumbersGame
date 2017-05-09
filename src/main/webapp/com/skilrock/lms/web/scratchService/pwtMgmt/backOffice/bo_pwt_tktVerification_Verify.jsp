	<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
	<%@ taglib prefix="s" uri="/struts-tags"%>
	
	<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
	
	<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
	
	<head>
	
	
	
	<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
	
	<link rel="stylesheet" href="<%=request.getContextPath() %>/LMSImages/css/styles.css" scrolling="no" type="text/css"/>
	
	<title><%=application.getAttribute("JSP_PAGE_TITLE")%></title>

		</head>

	<body>
	<%@include file="/com/skilrock/lms/web/loginMgmt/menu.jsp" %>

	<div id="wrap">

  <div id="top_form">
   <h3>Ticket Validation Before <s:text name="PWT"/> Receipts</h3>
		<s:form action="bo_pwt_tktVerification_Save.action">
			

			<table width="100%" border="1" align="center" cellpadding="3" cellspacing="0" bordercolor="#CCCCCC">
				<tr>
					<th align="center"><s:text name="Ticket Number" /></th>
					<th align="center"><s:text name="Validity Status" /></th>					
					<th align="center"><s:text name="Message Code" /></th>
					<th align="center"><s:text name="Remark" /></th>
				</tr>

				<s:iterator value="#session.VERIFIED_TICKET_LIST">
					<tr>
						<s:if test="%{validity=='InValid Ticket'}">
							<td ><font color="red"><s:property value="%{ticketNumber}" /></font></td>
							<td ><font color="red"><s:property value="%{validity}" /></font></td>						
							<td ><font color="red"><s:property value="%{messageCode}" /></font></td>
							<td ><font color="red"><s:property value="%{status}" /></font></td>
						</s:if>
						<s:else>
							<td ><s:property value="%{ticketNumber}" /></td>
							<td ><s:property value="%{validity}" /></td>						
							<td ><s:property value="%{messageCode}" /></td>
							<td >&nbsp;<s:property value="%{status}" /></td>
						</s:else>
					</tr>					
				</s:iterator>
				
				<!--  
					<s:if test="%{#session.PWT_FLAG=='Valid'}">	
					<tr>
						<td align="right" colspan="4">
						<div id="subCheck" style="position:absolute;"></div>
							<s:submit theme="simple" cssClass="button" value="Accept Valid Tickets" id="subTrans" onclick="return _subValid(this.id);" />
						</td>
					</tr>
					</s:if>
					<s:else>
				-->
				<tr>
			 		 <td align="left" colspan="4"><s:a theme="simple" href="bo_pwt_tktVerification_Menu.action">Do  Another <s:text name="PWT"/> Ticket Check</s:a></td>
				</tr>
				<!--
				</s:else>
				-->
			</table>		
		
		</s:form>
		
		</div></div>
	</body>
</html>

<code id="headId" style="visibility: hidden">
$RCSfile: bo_pwt_tktVerification_Verify.jsp,v $
$Revision: 1.2 $
</code>