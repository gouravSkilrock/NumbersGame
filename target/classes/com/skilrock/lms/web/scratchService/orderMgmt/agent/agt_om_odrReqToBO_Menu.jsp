<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="com.skilrock.lms.common.utility.FormatNumber"%>
<%@page import="java.text.NumberFormat"%>
<%@ taglib prefix="s" uri="/struts-tags"%>


<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">

<head>
	<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
	<link rel="stylesheet" href="<%=request.getContextPath() %>/LMSImages/css/styles.css" type="text/css"/>
	<title><%=application.getAttribute("JSP_PAGE_TITLE")%></title>
	<link type="text/css" rel="stylesheet" href="<%=request.getContextPath() %>/LMSImages/css/lmsCalendar.css" media="screen"/>
	<script>var projectName="<%=request.getContextPath() %>"</script>
	<script type="text/javascript" src="<%=request.getContextPath() %>/com/skilrock/lms/web/common/globalJs/calender.js"></script>		
	<script type="text/javascript" src="<%=request.getContextPath() %>/com/skilrock/lms/web/scratchService/orderMgmt/agent/js/agt_orderMgmt_order.js"/>
<s:head theme="ajax" debug="false"/>
</head>

<body>

<%@include file="/com/skilrock/lms/web/loginMgmt/menu.jsp"%>
<div id="wrap">
<div id="top_form">
  
	       <h3>Order Request</h3>
	        
		<s:form name="orderForm" action="agt_om_odrReqToBO_SearchResult" onsubmit="validateSearchData()">
			
		  <s:if test="%{cartEmpty == 'No'}" >
		  Credit Limit=<b><s:set name="strCreditLimit" value="%{#session.CREDIT_LIMIT}" /><%=FormatNumber.formatNumberForJSP(pageContext.getAttribute("strCreditLimit")) %></b>
		  Current Credit Amount=<b><s:set name="strCreditAmt" value="%{#session.CREDIT_AMT}" /><%=FormatNumber.formatNumberForJSP(pageContext.getAttribute("strCreditAmt")) %></b>
	 	<br />
						<br />
			
			 <table border="0" cellpadding="2" cellspacing="0"  width="385">
			   
			   <tr>
			   <th colspan="3" align="center" >Cart Details</th>
			   </tr>
			   <tr>
			     <th>Game Name</th>
			     <th>Quantity</th>
			     <th>Amount</th>
			   </tr>
			   	  <s:set name="total" value="0.0"/>
			   	  <%String totalAmountFinal=null;
					String totalAmount=null;
					double dobl=0.0;%>
			  <s:iterator id="Cart" value="#session.CART">
			    <tr>
			     	<s:set name = "total2" value="%{ticketPrice * orderedQty * ticketsPerBook}"/> 
			      <td> <s:property value="gameName"/></td>
			      <td> <s:property value="orderedQty"/></td>
			      <% NumberFormat format= NumberFormat.getInstance();
					format.setMaximumFractionDigits(2);
					dobl=(Double)pageContext.getAttribute("total2");
					totalAmount=format.format(dobl);
					
			      %>
			      <td><%=totalAmount%></td>
			  
					<s:set name = "total" value="#total+#total2"/>
		        </tr>
			  </s:iterator>
			  <% NumberFormat formatTotal= NumberFormat.getInstance();
					formatTotal.setMaximumFractionDigits(2);
					dobl=(Double)pageContext.getAttribute("total");
					 totalAmountFinal=formatTotal.format(dobl);
					%>
			  <tr><td colspan=2>Total Amount</td><td><%=totalAmountFinal %></td></tr> 
			  
			   <tr>
				 <td align="center" colspan="3"><s:submit theme="simple" action="agt_om_odrReqToBO_ProcessCart" value="Process Cart" cssClass="button"/>
				 <s:submit theme="simple" action="agt_om_odrReqToBO_CancelCart" value="Cancel" cssClass="button"/></td>
			</tr>
		    </table> 
			    <p>&nbsp;</p>
			</s:if>
		
			<table border="1" cellpadding="3" cellspacing="0" bordercolor="#CCCCCC">
			
			
			<tr><td>
			<table cellpadding="2" cellspacing="0" border="0">

				<tr><th >	Search Game</th></tr>


				<tr>
				  <td align="left">
					<div id="gameNumDiv"></div>Game Number:
					<s:textfield label="Game Number" theme="simple" name="gameNbr"></s:textfield>
				  </td>
				</tr>
				
				<tr>
				<td align="left">&nbsp;&nbsp;&nbsp;Game Name:
					<s:textfield label="Game Name" theme="simple" name="gameName"></s:textfield>
				  </td>
				</tr>
				

				<tr>
				
				<td colspan="3" align="right">		
				<div id="ticketPriceDiv"></div>
					<s:text name="selectprice">Ticket Price:</s:text>
					<s:select theme="simple" headerKey="1" value="'Equal To'" name="priceMatch" label="Price" list="{'Less Than Equal To','Greater Than Equal To','Equal To'}" cssClass="option"></s:select><s:textfield theme="simple" name="ticketPrice"cssClass="option"></s:textfield>
				</td>
				</tr>

				


                <tr>
				<td colspan="3" align="right">		
																<s:div theme="ajax" href="agt_om_selfIniOrderRet_Date.action" >
												</s:div>
				</td>
					
				</tr>

				<s:submit theme="ajax" name="search" value="Search" cssClass="button" targets="resultSearch" />
				

            </table>
            </td></tr>
		  </table>
			<p>&nbsp;</p>

			<div id = "bottom">
			
			</div>

		</s:form>
	 </div>
	 				<s:div theme="ajax" id="resultSearch">
				</s:div>
	 </div>
	</body>
</html>
<code id="headId" style="visibility: hidden">
$RCSfile: agt_om_odrReqToBO_Menu.jsp,v $
$Revision: 1.3 $
</code>