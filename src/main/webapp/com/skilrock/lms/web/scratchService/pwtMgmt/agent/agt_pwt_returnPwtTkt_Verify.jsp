<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
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
   <h3>Change Winning Ticket Status</h3>
		<s:form action="agt_pwt_returnPwtTkt_Save.action">
			

			<table border="1" width="75%" cellpadding="2" cellspacing="0">
				<tr>
					<td width="30%" align="center">
						<s:text name="Ticket Number" />
					</td>
					<td width="25%" align="center">
						<s:text name="Validity Status" />
					</td>
					<td width="45%" align="center">
						<s:text name="Remark" />
					</td>
				</tr>
				<s:iterator value="#session.VERIFIED_TICKET_LIST">
					<tr>
						<td width="30%">
							<s:property value="%{ticketNumber}" />
						</td>
						<td width="25%">
							<s:property value="%{validity}" />
						</td>
						<td width="45%">
							<font color="red"><s:property value="%{status}" />
							</font>
						</td>
					</tr>
				</s:iterator>
			</table>

				<s:if test="%{#session.HIGH_PRIZE=='Valid'}">
			<table border="1" width="75%" cellpadding="2" cellspacing="0">
				<tr></tr>
				<tr></tr>
				<tr>
					<td align="right"><div id="subCheck" style="position:absolute;"></div>
						<s:submit theme="simple" cssClass="button" value="Accept Valid Tickets" id="subTrans" onclick="return _subValid(this.id);"/>
					</td>

				</tr>
			</table>
</s:if>
	
<s:else>			
	
			<table border="1" width="75%" cellpadding="2" cellspacing="0">
			<tr>
			  <td><s:a theme="simple" href="agt_pwt_returnPwtTkt_Menu.action">Change Status For Another Winning</s:a></td>
			</tr>
			
			</table>
		

</s:else>
		</s:form>
		</div></div>
	</body>
</html>
<code id="headId" style="visibility: hidden">
$RCSfile: agt_pwt_returnPwtTkt_Verify.jsp,v $
$Revision: 1.2 $
</code>