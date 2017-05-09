<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>


<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
        <link rel="stylesheet" href="<%=request.getContextPath() %>/LMSImages/css/styles.css" type="text/css"/>
		<title><%=application.getAttribute("JSP_PAGE_TITLE")%></title>
	</head>

	<body>
		<%@include
				file="/com/skilrock/lms/web/loginMgmt/menu.jsp"%>
				
				<div id="wrap">

			    <div id="top_form">
		<h3>
			Change  Ticket Status
		</h3>
		<h7>
			Following Tckets Status Has Been Changed
		</h7>

		<table  width="50%" border="1" cellpadding="3" cellspacing="0" >
			<tr>
				<td width="60%" align="center">
					<s:text name="Ticket Number" />
				</td>
				<td width="40%" align="center">
					<s:text name="Status" />
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
			
			<tr>
				
				<td width="50%" align="right">
					<a href="agt_pwt_returnPwtTkt_Menu.action">More Ticket Status Change</a>
				</td>
			</tr>
		</table>


</div>
</div>
	</body>
</html>
<code id="headId" style="visibility: hidden">
$RCSfile: agt_pwt_returnPwtTkt_Success.jsp,v $
$Revision: 1.2 $
</code>