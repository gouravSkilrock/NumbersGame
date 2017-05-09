<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<%
	response.setHeader("Cache-Control","no-store"); //HTTP 1.1
	response.setHeader("Pragma","no-cache"); //HTTP 1.0
	response.setDateHeader ("Expires", 0); //prevents caching at the proxy server
%>
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">

<head>
	<s:head theme="ajax" debug="false"/>
	<meta http-equiv="CACHE-CONTROL" content="NO-CACHE" />
	<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
	<link rel="stylesheet" href="<%=request.getContextPath() %>/LMSImages/css/styles.css"  type="text/css"/>
	<title><%=application.getAttribute("JSP_PAGE_TITLE")%></title>
</head>

<body>
	<%@include file="/com/skilrock/lms/web/loginMgmt/menu.jsp"%>

<div id="wrap"><div id="top_form">

  <h3>Winning Ticket And VIRN Success Page</h3>	
	<div id="first">
	 
			 <table  border="1" cellpadding="2" cellspacing="0"  width="400px" bordercolor="#CCCCC">
			 	<tr>
			 		<th align="center">Ticket Number</th>
			 		<th align="center">Virn Number</th>
			 		<th align="center">Winning Amount</th>
			 	</tr>
			 	
			 	<tr>	
			 	<td align="left"><s:property value="%{#session.pwtDetailMap.tktBean.ticketNumber}" /></td>	
			 	<td align="left"><s:property value="%{#session.pwtDetailMap.pwtBean.virnCode}" /></td>	
			 	<td align="right"><s:property value="%{#session.pwtDetailMap.pwtBean.pwtAmount}" /></td>			
				</tr>	
					
			 </table>		 

	</div>
	<br />
	<a href="<%=request.getContextPath()%>/com/skilrock/lms/web/scratchService/pwtMgmt/retailer/RetailerPWTHome.jsp">Verify More Tickets</a>	
</div></div>

</body>

<script>parent.frames[0].updateBalance("<%=request.getContextPath()%>/com/skilrock/lms/web/loginMgmt/updateAvlCreditAmt.action");</script>
</html>
<code id="headId" style="visibility: hidden">
$RCSfile: retPwtSuccess.jsp,v $
$Revision: 1.2 $
</code>