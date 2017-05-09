<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="com.skilrock.lms.common.utility.FormatNumber"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">

<head>



<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />

<link rel="stylesheet" href="<%=request.getContextPath() %>/LMSImages/css/styles.css" scrolling="no" type="text/css"/>

<title><%=application.getAttribute("JSP_PAGE_TITLE")%></title>
	

<s:head theme="ajax" debug="false"/>
	</head>

<body>

<%@include file="/com/skilrock/lms/web/loginMgmt/menu.jsp" %>

<div id="wrap">

  <div id="top_form">
	<h3>Order Process : Book Dispatching</h3>
		<s:form action="agt_im_dispatchOrder_Save">
		
		
			
			<p>&nbsp;</p>
			<table width="80%" border="1" align="center" cellpadding="3" cellspacing="0">
			 <tr><td>
			 <table>
			 <tr>
			 <th>Order Number</th><td><s:property value="#session.AGT_ORDER_ID"/></td>
			 <th>Order Date</th><td><s:property value="#session.AGT_ORDER_DATE"/></td>
			 </tr>
			 <tr>
			 <th><s:property value="#application.TIER_MAP.RETAILER" /> Name</th><td><s:property value="#session.RET_ORG_NAME"/></td>
			 <th>Address</th><td><s:property value="#session.RET_ORG_ADDR.addrLine1"/>,<s:property value="#session.RET_ORG_ADDR.addrLine2"/></td></tr>
			 <tr><td></td><td></td><td></td><td><s:property value="#session.RET_ORG_ADDR.city"/>,<s:property value="#session.RET_ORG_ADDR.state"/></td></tr>
			 <tr><td></td><td></td><td></td><td><s:property value="#session.RET_ORG_ADDR.country"/></td></tr>
			 
			 <tr>			 
			 <th>Credit Limit : </th><td><s:set name="strCreditLimit" value="%{#session.CREDIT_LIMIT}" /><%=FormatNumber.formatNumberForJSP(pageContext.getAttribute("strCreditLimit")) %></td>
			 <th>Current Credit Amount: </th><td><s:set name="strCreditAmt" value="%{#session.CREDIT_AMT}" /><%=FormatNumber.formatNumberForJSP(pageContext.getAttribute("strCreditAmt")) %></td>
			 </tr>
			 </table>
			 
			 </td></tr>
		
		  </table>
			
			 <p>&nbsp;</p>
			 <table align="center" border="1" >
			
			 <table align="center" border="1" cellpadding="3" cellspacing="0" bordercolor="#CCCCCC">
			    
			   
			   <tr>
			     <th>Sr No</th>
			     <th>Game Number</th>
			     <th>Game Name</th>
			     <th>Number of Books Approved</th>
			     <th>Number of Books To Dispatch</th>
			     <th>No of Books Already Dispatched</th>
			     <th>No of books Remaining to Dispatch</th>
			     
			   </tr>
			 
			  <s:iterator  value="#session.AGT_APP_ORDER_GAME_LIST" status="appStatus">
			    
			    <tr>
			      <td><s:property value="#appStatus.index + 1" /></td>
			      <s:if test="%{isReadyForDispatch}">
			      <td><s:property value="gameNbr"/></td>
			      </s:if>
			      <s:else>
			      <td><s:a  href="agt_im_dispatchOrder_OrderGameDetail.action?gameId=%{gameId}"><s:property value="gameNbr"/></s:a></td>
			      </s:else>
			      <td> <s:property value="gameName"/></td>
			      <td> <s:property value="nbrOfBooksApp"/></td>
			  	  <td><s:property value="nbrOfBooksToDispatch"/></td>
			  	  <td><s:property value="nbrOfBooksDlvrd"/></td>
			 <td><s:property value="remainingBooksToDispatch"/></td>
		        </tr>
			     
			     
			  </s:iterator> 
			 </table> 
			
			<s:if test="%{isDispatch}">
			<div id="subCheck" style="position:absolute;"></div>
			<s:submit value="Dispatch Order" cssClass="button" id="subTrans" onclick="return _subValid(this.id);"/>
			</s:if>
			
	</table>
</s:form>
</div></div>
</body>
</html>
<code id="headId" style="visibility: hidden">
$RCSfile: agt_im_dispatchOrder_OrderDisplay.jsp,v $
$Revision: 1.3 $
</code>