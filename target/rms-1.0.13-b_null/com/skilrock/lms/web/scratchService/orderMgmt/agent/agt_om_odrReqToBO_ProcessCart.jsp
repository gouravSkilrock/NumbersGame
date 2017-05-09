<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="com.skilrock.lms.common.utility.FormatNumber"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">

<head>



<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />

<link rel="stylesheet" href="<%=request.getContextPath() %>/LMSImages/css/styles.css"  type="text/css"/>

<title><%=application.getAttribute("JSP_PAGE_TITLE")%></title>
	

<s:head theme="ajax" debug="false"/>
	


		<title><%=application.getAttribute("JSP_PAGE_TITLE")%></title>

	</head>

	<body>
<%@include file="/com/skilrock/lms/web/loginMgmt/menu.jsp" %>
<div id="wrap">
  <div id="top_form">
  
   <h3>Order Request</h3>
   
		<s:form>
			

	
	  <table width="750" border="1" cellpadding="2" cellspacing="0" bordercolor="#CCCCCC" align="center">
	 
			   
			   <tr>
			   <td colspan="8">Cart Details</td>
			   </tr>
			   <tr>
			     <th>Game Name</th>
			     <th>Game Number</th>
			     <th>Ticket Price</th>
			     <th>Game Start Date</th>
			     <th>Game End Date</th>
			     <th>Quantity</th>
			     <th>Amount</th>
			     <th>Remove From Cart</th>
			   </tr>
			  
			  <s:iterator id="Cart" value="#session.CART">
			    <tr>
			      
			      
			  	  <td> <s:property value="gameName"/></td>
			      <td> <s:property value="gameNbr"/></td>
			      <td> <s:set name="strticketPrice" value="ticketPrice" /><%=FormatNumber.formatNumberForJSP(pageContext.getAttribute("strticketPrice")) %></td>
			      <td> <s:property value="startDate"/></td>
			      <td> <s:property value="saleEndDate"/></td>
			      <td> <s:property value="orderedQty"/></td>
			      <td> <s:set name="strAmount" value="ticketPrice * orderedQty * ticketsPerBook" /><%=FormatNumber.formatNumberForJSP(pageContext.getAttribute("strAmount")) %></td>
			      <td><s:a  href="agt_om_odrReqToBO_RemFromCart.action?gameId=%{#Cart.gameId}">Remove</s:a></td>
			   </tr>
			  </s:iterator> 
		  </table> 
			
				<table width="750" border="1" cellpadding="3" cellspacing="0" bordercolor="#CCCCCC" align="center">
			<tr>
			<s:if test="#session.CART.size > 0">
			<td>
			<s:submit theme="simple" action="agt_om_odrReqToBO_Save" value="Generate Order" cssClass="button"/>
			</td>
			</s:if>
			<td>
			<s:submit theme="simple" action="agt_om_odrReqToBO_AddMoreToCart" value="Add More to Cart" cssClass="button"/>
			</td>
			</tr>
			</table><p>&nbsp;</p>
		</s:form>

	</body>
</html><code id="headId" style="visibility: hidden">$Id: agt_om_odrReqToBO_ProcessCart.jsp,v 1.1 2010/04/02 12:12:06 gaurav Exp $</code>
