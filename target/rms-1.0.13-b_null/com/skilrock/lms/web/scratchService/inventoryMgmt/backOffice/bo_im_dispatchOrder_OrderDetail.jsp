<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="com.skilrock.lms.common.utility.FormatNumber"%>
<%@page import="java.util.Date"%>
<%@page import="java.util.Random"%>
<%@ taglib prefix="s" uri="/struts-tags"%>


<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">

<head>



<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />

<link rel="stylesheet" href="<%=request.getContextPath() %>/LMSImages/css/styles.css" type="text/css"/>

<title><%=application.getAttribute("JSP_PAGE_TITLE")%></title>
	

<s:head theme="ajax" debug="false"/>
<script >
function checkTotalBooks()
{
if(document.getElementById("totBooks").value=="0"){
alert("You can not dispatch zero books");
return false;
}
return _subValid('subOrder');
}
</script>
	</head>

<body>

<%@include file="/com/skilrock/lms/web/loginMgmt/menu.jsp"%>

<div id="wrap">

  <div id="top_form">
  	<h3>Order Process : Book Dispatching</h3>
		<s:form action="bo_im_dispatchOrder_Save" onsubmit="return checkTotalBooks();">
		
			 <%pageContext.setAttribute("randomNum",new Date().getTime()+""+(new Random().nextInt(100000)+100000));%>
			 <table width="750" border="0" cellpadding="4" cellspacing="0">
			 <tr><td>
			 <table width="750" border="0" cellpadding="2" cellspacing="0" bgcolor="#EBF1F1"> 
			 <tr>
			 <th>Order Number</th><td><s:property value="#session.ORDER_ID"/></td>
			 <th>Order Date</th><td><s:property value="#session.ORDER_DATE"/></td>
			 </tr>
			 <tr>
			 <th><s:property value="#application.TIER_MAP.AGENT" /> Name</th><td><s:property value="#session.AGT_ORG_NAME"/></td>
			 <th>Address</th><td><s:property value="#session.ORG_ADDR.addrLine1"/>&nbsp; <s:property value="#session.ORG_ADDR.addrLine2"/></td>
			 </tr>
			
			 <tr><td></td><td></td><td></td><td><s:property value="#session.ORG_ADDR.city"/> ,&nbsp; <s:property value="#session.ORG_ADDR.state"/></td></tr>
			 
			 <tr><td></td><td></td><td></td><td><s:property value="#session.ORG_ADDR.country"/></td></tr>
			 <tr>
			 <s:set name="creditLimitStr" value="%{#session.CREDIT_LIMIT}" ></s:set>
			 <s:set name="creditAmtStr" value="%{#session.CREDIT_AMT}" ></s:set>			 
			 <th>Credit Limit : </th><td><%=FormatNumber.formatNumberForJSP(pageContext.getAttribute("creditLimitStr")) %></td>
			 <th>Current Credit : </th><td><%=FormatNumber.formatNumberForJSP(pageContext.getAttribute("creditAmtStr")) %></td>
			 </tr>
			 </table>
			 </td></tr>
		  	 </table>
							<br />
			 <table width="750" border="1" cellpadding="2" cellspacing="0">
			    
			   
			   <tr>
			     <th>Sr No</th>
			     <th>Game Number</th>
			     <th>Game Name</th>
			     <th>Number of Books Approved</th>
			     <th>Number of Books To Dispatch</th>
			     <th>No of Books Already Dispatched</th>
			     <th>No of books Remaining to Dispatch</th>
			     
			   </tr>
			    <s:set name="totalBooksToDispatch" value="0"></s:set>
			  <s:iterator  value="#session.APP_ORDER_GAME_LIST" status="appStatus">
			  
			    <tr>
			      <td><s:property value="#appStatus.index + 1" /></td>
			      <s:if test="%{isReadyForDispatch}">
			      <td><s:property value="gameNbr"/></td>
			      </s:if>
			      <s:else><s:set name="gameId" value="%{gameId}"></s:set>
			      <td><a  href="bo_im_dispatchOrder_OrderGameDetail.action?gameId=<%=pageContext.getAttribute("gameId")%>&<%=pageContext.getAttribute("randomNum")%>"><s:property value="gameNbr"/></a></td>
			      </s:else>
			      <td> <s:property value="gameName"/></td>
			      <td> <s:property value="nbrOfBooksApp"/><s:hidden name="nbrOfBooksApp" value="nbrOfBooksApp"/></td>
			       <td> <s:property value="nbrOfBooksToDispatch"/><s:hidden name="nbrOfBooksToDispatch" value="nbrOfBooksToDispatch"/></td>
			<s:set name="bookToDispatch" value="nbrOfBooksToDispatch"></s:set>
			 <s:set name="totalBooksToDispatch" value="#totalBooksToDispatch+#bookToDispatch"></s:set>
			 <td><s:property value="nbrOfBooksDlvrd"/></td>
			 <td><s:property value="remainingBooksToDispatch"/></td>
			     </tr>
			     
			     
			  </s:iterator> 
			 </table>
			 
			<input type="hidden" name="totalBooks" id="totBooks" value="<%=pageContext.getAttribute("totalBooksToDispatch") %>"/>
			<s:if test="%{isDispatch}">
			<div id="subCheck" style="position:absolute;"></div>
			<s:submit  value="Dispatch Order" id="subOrder" cssClass="button" />
			</s:if>
			
		</s:form>
</div></div>
	</body>
</html>
<code id="headId" style="visibility: hidden">
$RCSfile: bo_im_dispatchOrder_OrderDetail.jsp,v $
$Revision: 1.3 $
</code>