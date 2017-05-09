<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="com.skilrock.lms.common.utility.FormatNumber"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@page import="java.text.NumberFormat"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">

<head>



<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />

<link rel="stylesheet" href="<%=request.getContextPath() %>/LMSImages/css/styles.css" type="text/css"/>

<title><%=application.getAttribute("JSP_PAGE_TITLE")%></title>
	

<s:head theme="ajax" debug="false"/>
	
		 
		
		<script type="text/javascript"  src="<%=request.getContextPath() %>/com/skilrock/lms/web/scratchService/orderMgmt/backOffice/js/boOrder.js"></script>
	</head>

	<body>
	<%@include file="/com/skilrock/lms/web/loginMgmt/menu.jsp" %>

	<div id="wrap">

  <div id="top_form">
   <h3>Order Request for <s:property value="#application.TIER_MAP.AGENT" /></h3>
	
		<%NumberFormat format = NumberFormat.getInstance();
														format.setMaximumFractionDigits(2);%>
		<s:form name="boGameDetailsForm">
			
			
		 <table border="1" width="300" cellpadding="2" cellspacing="0" bordercolor="#CCCCCC">
		 <tr align="left"><th><s:property value="#application.TIER_MAP.AGENT" /></th><td><s:property value="%{agtOrgName}"/></td></tr>
		
		     <s:set name="creditLimitStr" value="%{#session.CREDIT_LIMIT}" ></s:set>
			 <s:set name="creditAmtStr" value="%{#session.CREDIT_AMT}" ></s:set>
		  
		  <tr align="left"><th>Credit Limit</th><td><%=FormatNumber.formatNumberForJSP(pageContext.getAttribute("creditLimitStr")) %></td></tr>
		  <tr align="left"><th>Current Credit Amount</th><td><%=FormatNumber.formatNumberForJSP(pageContext.getAttribute("creditAmtStr")) %></td></tr>
		  
		 </table>	
		 <br>
		 <s:if test="%{cartEmpty == 'No'}" >
			 <table  border="1" width="300" cellpadding="2" cellspacing="0" bordercolor="#CCCCCC" >
			   
			   <tr>
			   <th colspan="7" align="center">Cart Detail</th>
			   </tr>
			   <tr>
			     <th>Game Name</th>
			     <th>Quantity</th>
			     <th>Amount</th>
			   </tr>
			   <%
								String totalAmountFinal = null;
										String totalAmount = null;
										double dobl = 0.0;
							%>
			   <s:set name="total" value="0.0"/>
			  <s:iterator id="Cart" value="#session.CART">
			    <s:set name = "total2" value="%{ticketPrice * orderedQty * ticketsPerBook}"/>
			    <tr>
			      
			      <td> <s:property value="gameName"/></td>
			      <td> <s:property value="orderedQty"/></td>
			      <%
														dobl = (Double) pageContext.getAttribute("total2");
														totalAmount = format.format(dobl);
														
										%>
			      <td> <%=totalAmount %></td>
			
					<s:set name = "total" value="#total+#total2"/>
			     </tr>
			  </s:iterator> 
			  <%
										NumberFormat formatTotal = NumberFormat.getInstance();
										formatTotal.setMaximumFractionDigits(2);
										dobl = (Double) pageContext.getAttribute("total");
										totalAmountFinal = formatTotal.format(dobl);
										
							%>
			 <tr><td colspan=2>Total Amount</td><td><%= totalAmountFinal%></td></tr>
			   
			  
			 </table> 
			 
			
			</s:if>
		 
			 <table width="720" border="1" cellpadding="2" cellspacing="0" bordercolor="#CCCCCC">	   
			  
			  <tr>
			  <th colspan="4">Game Details</th>
			  </tr>
			 
			  <tr align="left">  
				  <th>Game Number</th>
				  <td><s:property   value="#session.GAME_DETAILS_BEAN.gameNbr"/></td>
				  <th>Game Name</th>
				  <td><s:property  value="#session.GAME_DETAILS_BEAN.gameName"/></td>
				  
			  </tr>
			  
			  <tr align="left">  
				  <th>Ticket Price</th>
				  <td><s:set name="strticketPrice" value="#session.GAME_DETAILS_BEAN.ticketPrice" /><%=FormatNumber.formatNumberForJSP(pageContext.getAttribute("strticketPrice")) %></td>
				  <th>Game Start Date</th>
				  <td><s:property   value="#session.GAME_DETAILS_BEAN.startDate"/></td>
			  </tr>
			  
			  
			  <tr align="left">  
				  <th>Sale End Date</th>
				  <td><s:property  value="#session.GAME_DETAILS_BEAN.saleEndDate"/></td>
				  <th>Pwt End Date</th>
				  <td><s:property   value="#session.GAME_DETAILS_BEAN.pwtEndDate"/></td>
			  </tr>
			  
			  <tr align="left">  
				  <th> Tickets Per Book</th>
				  <td><s:property  value="#session.GAME_DETAILS_BEAN.ticketsPerBook"/></td>
				  <th>Books Per Pack</th><td><s:property value="#session.GAME_DETAILS_BEAN.booksPerPack"/></td>
			  </tr>
			  
			  <tr align="left">  
				  <th>Number of Books Available</th>
				  <td><s:property  value="#session.GAME_DETAILS_BEAN.nbrOfBooksAvailable"/></td>
				  <s:set name="booksAvail" value="#session.GAME_DETAILS_BEAN.nbrOfBooksAvailable" />
				  <s:hidden name="nbrOfBooksAvailable" value="%{booksAvail}" />
				  <th>Number of Books Already Approved</th>
				  <td><s:property   value="#session.GAME_DETAILS_BEAN.nbrOfBooksApp"/></td>
				  <s:set name="booksApp" value="#session.GAME_DETAILS_BEAN.nbrOfBooksApp" />
				  <s:hidden name="nbrOfBooksApp" value="%{booksApp}"/>
			  </tr>
			  <tr><td colspan="4"><div id="booksDiv"></div></td></tr>
			  
			   <tr>
			  
			  <th colspan="2">Total Books Already with <s:property value="#application.TIER_MAP.AGENT" /></th>
			  <td colspan="2">
			  <s:property value="#session.TOTAL_BOOKS"/>
			  </td>
			  </tr>
			  <tr>
			  
			  <th colspan="2">Total Dispatched Books to <s:property value="#application.TIER_MAP.RETAILER" /> by the <s:property value="#application.TIER_MAP.AGENT" /></th>
			  <td colspan="2">
			  <s:property value="#session.TOTAL_BOOKS_WITH_RET_BY_AGT"/>
			  </td>
			  </tr>
			  
			  
			   <tr>
			  
			  <th colspan="2">Total Dispatched Books to <s:property value="#application.TIER_MAP.AGENT" /> by the BO</th>
			  <td colspan="2">
			  <s:property value="#session.TOTAL_BOOKS_FOR_AGT_BY_BO"/>
			  </td>
			  </tr>
			  <tr>
			  
			  <th colspan="2">Number of Books To Order</th>
			  <td colspan="2">
			  <s:textfield theme="simple"label="Number of Books To Order" name="quantity" maxlength="9" value="%{gameQty}"></s:textfield>
			  </td>
			  </tr>
			  
			  <s:hidden name="gameId" key="#session.GAME_DETAILS_BEAN.gameId" />
			 
			 </table>
			 <br /><br />
			  
			  
			  <table width="300" border="1" cellpadding="3" cellspacing="0" bordercolor="#CCCCCC">
			  
			  <tr align="left">
			  <th>Prize Amount</th>
			  <th>No of Prizes Remaining</th>
			  </tr>
			  <s:iterator value="prizeStatusList">
			  <tr align="left">
			  <td><s:property   value="%{prizeAmt}"/></td>
			  <td><s:property   value="%{nbrOfPrizeLeft}"/></td>
			  </tr>
			  </s:iterator>
			  </table>
			 
			  
			 <table width="300" border="0" cellpadding="3" cellspacing="0">
			  <tr>
			  
			  <td><s:submit theme="simple" value="Add to Cart" action="bo_om_selfIniOrderAgt_AddToCart" onclick="return validateData()" onkeypress="return validateData()" cssClass="button"/><s:submit theme="simple" value="Cancel" action="bo_om_selfIniOrderAgt_CancelOrder"  cssClass="button"/></td>
			  </tr>
		
			  </table>     
	
		</s:form>
		</div></div>
	</body>
</html>
<code id="headId" style="visibility: hidden">
$RCSfile: bo_om_selfIniOrderAgt_OrderDet.jsp,v $
$Revision: 1.2 $
</code>