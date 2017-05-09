	<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
	<%@ taglib prefix="s" uri="/struts-tags"%>
	<%@page import="java.text.NumberFormat"%>
	
	<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
	
	<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
	
	<%
	response.setHeader("Cache-Control","no-store"); //HTTP 1.1
	response.setHeader("Pragma","no-cache"); //HTTP 1.0
	response.setDateHeader ("Expires", 0); //prevents caching at the proxy server
%>
	<head>
	

	<META HTTP-EQUIV="CACHE-CONTROL" CONTENT="NO-CACHE" />
	
	
	
	<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
	
	<link rel="stylesheet" href="<%=request.getContextPath() %>/LMSImages/css/styles.css" type="text/css"/>
	
	<title><%=application.getAttribute("JSP_PAGE_TITLE")%></title>
		
	
	<s:head theme="ajax" debug="false"/>
	<script>
	function checkAvailable()
	{
	var availAmt=document.getElementById("availableAmt").value;
	var saleAmt=document.getElementById("totalSaleAmt").value;
	//alert("availAmt "+availAmt+" saleAmt "+saleAmt);
	//alert(parseFloat(saleAmt));
	if(parseFloat(saleAmt) > parseFloat(availAmt) )
	{
	 
	 alert("Balance ( "+availAmt+" ) is not Enough to Approve this Order,Please Use Remove or Add More to Cart Option");
	 return false;
	}
	return true;
	}
	
	
	
	</script>
		</head>

	<body>
	<%@include file="/com/skilrock/lms/web/loginMgmt/menu.jsp" %>

	<div id="wrap">

  <div id="top_form">
   <h3>Order Request for <s:property value="#application.TIER_MAP.AGENT" /></h3>
	<%NumberFormat format = NumberFormat.getInstance();
	format.setMaximumFractionDigits(2);%>

	<%
		String totalAmount = null;
		String totalAmountDiscount = null;
		double dobl = 0.0;
		double dob2=0.0;
		
	%>
		
		<s:form action="bo_om_selfIniOrderAgt_Search">
		<s:set name="avlAmt" value="#session.AVAIL_AMT"></s:set>
		<s:hidden name="availableAmt" value="%{avlAmt}" id="availableAmt"></s:hidden>
		 <table  border="0" cellpadding="3" cellspacing="0" bordercolor="#CCCCCC">
		 	<tr><th><s:property value="#application.TIER_MAP.AGENT" />:</th><td><s:property value="%{agtOrgName}"/></td></tr>
		 </table>
			<br/>
			 <table width="750" border="1" cellpadding="2" cellspacing="0" bordercolor="#CCCCCC" align="center">
			   
			   <tr>
			   <td colspan="8"><b>Cart Detail</b></td>
			   </tr>
			   <tr>
			     <th>Game Name</th>
			     <th>Game Number</th>
			     <th>Ticket Price</th>
			     <th>Game Start Date</th>
			     <th>Game End Date</th>
			     <th>Quantity</th>
			     <th>MRP Amount</th>
			     <th>Sale Price</th>
			     <th>Remove From Cart</th>
			   </tr>
			  
			  
			  <s:set name="total" value="0.0" />
							
			  
			  
			  <s:iterator id="Cart" value="#session.CART">
			    <s:set name="totalAmount" value="ticketPrice * orderedQty * ticketsPerBook"></s:set>
			    <s:set name="totalAmountDiscount" value="(ticketPrice * orderedQty * ticketsPerBook)-((ticketPrice * orderedQty * ticketsPerBook)*(agentSaleRate/100))"></s:set>
			    <%
			      dobl = (Double) pageContext.getAttribute("totalAmount");
				  totalAmount = format.format(dobl);
				  dob2=(Double) pageContext.getAttribute("totalAmountDiscount");
				  totalAmountDiscount=format.format(dob2);
			     %>
			    
			    <tr>
			      <td> <s:property value="gameName"/></td>
			      <td> <s:property value="gameNbr"/></td>
			      <td> <s:property value="ticketPrice"/></td>
			      <td> <s:property value="startDate"/></td>
			      <td> <s:property value="saleEndDate"/></td>
			      <td> <s:property value="orderedQty"/></td>
			      <td> <%=totalAmount%></td>
			      <td> <%=totalAmountDiscount%></td>
			       		      
			      <td><s:a  href="bo_om_selfIniOrderAgt_RemoveFromCart.action?gameId=%{#Cart.gameId}">Remove</s:a></td>
			   </tr>
			   <s:set name="total" value="#total+#totalAmountDiscount" />
			   
			  </s:iterator> 
			 <s:hidden name="totalSaleAmt" value="%{total}" id="totalSaleAmt"></s:hidden>
			 </table> 
			
			
			
			<table width="750" border="1" cellpadding="3" cellspacing="0" bordercolor="#CCCCCC" align="center">
			<tr>
			<td colspan="2" align="center">
			<s:if test="#session.CART.size > 0">
			
			<s:submit theme="simple" action="bo_om_selfIniOrderAgt_GenOrder" onclick="return checkAvailable()" onkeypress="return checkAvailable()" value="Generate Order" cssClass="button"/>
			
			</s:if>			
			<s:submit theme="simple" action="bo_om_selfIniOrderAgt_AddMoreToCart"  value="Add More to Cart" cssClass="button" />
			</td>
			</tr>
			</table>
		</s:form>
		</div></div>
	</body>
</html>
<code id="headId" style="visibility: hidden">
$RCSfile: bo_om_selfIniOrderAgt_CartDet.jsp,v $
$Revision: 1.2 $
</code>