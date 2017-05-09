<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="com.skilrock.lms.common.utility.FormatNumber"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@page import="java.text.NumberFormat"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">

<head>



<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />

	<link rel="stylesheet" href="<%=request.getContextPath() %>/LMSImages/css/styles.css"  type="text/css"/>

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
	 
	 alert("Balance : ( "+availAmt+" ) is not Enough to Approve this Order,Please Use Remove or Add More to Cart Option");
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
	<h3>Order Request for <s:property value="#application.TIER_MAP.RETAILER" /></h3>
		<s:form action="agt_om_selfIniOrderRet_Search">
			<%NumberFormat format = NumberFormat.getInstance();
														format.setMaximumFractionDigits(2);%>
		 
				
				<s:set name="avlAmt" value="#session.AVAIL_AMT_RET"></s:set>
		<s:hidden name="availableAmt" value="%{avlAmt}" id="availableAmt"></s:hidden>
		
				
				
				
				 <table width="300" border="0" cellpadding="3" cellspacing="0" bordercolor="#CCCCCC">
		 	<tr><th><s:property value="#application.TIER_MAP.RETAILER" />:</th><td><s:property value="%{retOrgName}"/></td></tr>
		 </table>
			
			 <table width="100%" border="1" cellpadding="2" cellspacing="0" bordercolor="#CCCCCC" align="center">
			   
			   <tr>
			   <td colspan="9">Cart Details</td>
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
			    <tr>
			      <s:set name="amount"  value="ticketPrice * orderedQty * ticketsPerBook"></s:set>
			      <s:set name="totalAmountDiscount" value="(ticketPrice * orderedQty * ticketsPerBook)-((ticketPrice * orderedQty * ticketsPerBook)*(retSaleRate/100))"></s:set>
			      <%
			        Double  amt=(Double)pageContext.getAttribute("amount");
			        String amount=format.format(amt);
			        
			        Double  amtDiscount=(Double)pageContext.getAttribute("totalAmountDiscount");
			        String totalAmountDiscount=format.format(amtDiscount);
			        
			        
			      %>
			  	  <td> <s:property value="gameName"/></td>
			      <td> <s:property value="gameNbr"/></td>
			      <td> <s:set name="strticketPrice" value="ticketPrice" /><%=FormatNumber.formatNumberForJSP(pageContext.getAttribute("strticketPrice")) %></td>
			      <td> <s:property value="startDate"/></td>
			      <td> <s:property value="saleEndDate"/></td>
			      <td> <s:property value="orderedQty"/></td>
			      <td> <%=amount %></td>
			       <td> <%=totalAmountDiscount%></td>
			      <td><s:a href="agt_om_selfIniOrderRet_RemFrmCart.action?gameId=%{#Cart.gameId}">Remove</s:a></td>
			   </tr>
			   <s:set name="total" value="#total+#totalAmountDiscount" />
			  </s:iterator> 
			  <s:hidden name="totalSaleAmt" value="%{total}>" id="totalSaleAmt"></s:hidden>
			 </table> 
			
				<table width="100%" border="1" cellpadding="3" cellspacing="0" bordercolor="#CCCCCC" align="center">
			<tr>
			<s:if test="#session.CART.size > 0">
			<td>
			<s:submit theme="simple" action="agt_om_selfIniOrderRet_GenOrder" onclick="return checkAvailable()" onkeypress="return checkAvailable()" value="Generate Order" cssClass="button"/>
			</td>
			</s:if>
			<td>
			<s:submit theme="simple" action="agt_om_selfIniOrderRet_AddMoreToCart"  value="Add More to Cart" cssClass="button"/>
			</td>
			</tr>
			</table>
		</s:form>
</div></div>
	</body>
</html>
<code id="headId" style="visibility: hidden">
$RCSfile: agt_om_selfIniOrderRet_CartDet.jsp,v $
$Revision: 1.3 $
</code>