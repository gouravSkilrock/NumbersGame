<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="com.skilrock.lms.common.utility.FormatNumber"%>
<%@ taglib prefix="s" uri="/struts-tags"%>


<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">

<head>
	<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
	<link rel="stylesheet" href="<%=request.getContextPath()%>/LMSImages/css/styles.css" type="text/css"/>
	<title><%=application.getAttribute("JSP_PAGE_TITLE")%></title>
	<s:head theme="ajax" debug="false"/>
	<script type="text/javascript" src="<%=request.getContextPath()%>/com/skilrock/lms/account/js/denyPwt.js"></script>
</head>

<body>
	<%@include file="/com/skilrock/lms/web/loginMgmt/menu.jsp" %>
	<div id="wrap">
	<div id="top_form">
  
       <s:form id="form1" >
    
            <s:hidden name="playerReceiptId" value="%{gamePlrDetaillbean.playerReceiptId}"></s:hidden>
            <s:hidden name="virnCode" value="%{gamePlrDetaillbean.virnCode}"></s:hidden>
            <s:hidden name="gameId" value="%{gamePlrDetaillbean.gameId}"></s:hidden>
            <s:hidden name="ticketNbr" value="%{gamePlrDetaillbean.ticketNbr}"></s:hidden>
            <s:hidden name="gameNbr" value="%{gamePlrDetaillbean.gameNbr}"></s:hidden>
           
            
            <table border="1" width="100%" style="border-color: #CCCCCC" cellspacing="0" cellpadding="3">
            	<tr><th align="center" colspan="5">Player Details</th></tr>
             	<tr>
             		<th align="center">Player Name</th>
             		<th align="center">Player Address</th>
             		<th align="center">City</th>
             		<th align="center">Email Address</th>
             		<th align="center">Phone</th>
             	</tr>
	         	<tr> 
	                <td><s:property value="gamePlrDetaillbean.firstName"/>&nbsp;<s:property value="gamePlrDetaillbean.lastName"/></td>
                    <td><s:property value="gamePlrDetaillbean.plrAddr1"/>&nbsp;<s:property value="gamePlrDetaillbean.plrAddr2"/></td>
                    <td><s:property value="gamePlrDetaillbean.plrCity"/></td>                              
                    <td><s:property value="gamePlrDetaillbean.emailId"/></td>                              
                    <td><s:property value="gamePlrDetaillbean.phone"/></td>                              
             </tr>               
            </table>    
               
               
            <br/>  
           <table border="1" width="90%" style="border-color: #CCCCCC" cellspacing="0" cellpadding="3">
               	<tr><th align="center" colspan="3">Winning details</th></tr>
             	<tr>
               		<th align="center">Game Name</th>
               		<th align="center">Ticket Number</th>
               		<th align="center">Winning Amount</th>
                </tr>
	            <tr>
	            	<td><s:property value="gamePlrDetaillbean.gameNbr"/>-<s:property value="gamePlrDetaillbean.gameName"/></td>                              
             	    <td><s:property value="gamePlrDetaillbean.ticketNbr"/></td>
             	    <td  align="right"><s:set name="pwtAmount" value="%{gamePlrDetaillbean.pwtAmt}"/><%=FormatNumber.formatNumberForJSP(pageContext.getAttribute("pwtAmount")) %></td>
                </tr>
          </table>
          
           
		 <br/>
		 <table border="1" width="100%" style="border-color: #CCCCCC" align = "center" cellspacing="0" cellpadding="3">
				<tr><th align="center" colspan="8">Remaining Prize Details of Game</th></tr>
				<tr>
					<th  align="left">Prize Amount</th>					
				
				<s:iterator value="prizeStatusList">				
						<th align="center"><s:property value="%{prizeAmt}" /></th>
					</s:iterator>
				</tr>
				
				<tr>
				<th  align="left">Remaining Prizes</th>
				<s:iterator value="prizeStatusList">				
					<td align="right"><s:property value="%{nbrOfPrizeLeft}" /></td>				
				</s:iterator>
				</tr>
			</table>
                 
            <table align="center">
                 <tr align="center">
	                    <td><table><div id="subCheck" style="position:absolute;"></div>
	                    <s:submit value="Submit"  action="approvePWT" id="subTrans" onclick="return _subValid(this.id)" /></table>
	                    </td>
	                    <td><table> <s:submit theme="simple" formId="form1"  value="Deny"  onclick=" return showDenyFields();" onkeypress="return showDenyFields();" /></table></td>
	                 </tr> 
            </table>
		
		
			<table id="denyfields" width="50%" border="1" cellspacing="0" align = "center" cellpadding="3" style="display : none;border-color: #CCCCCC">
			   			<tr>
			   				<% //'CANCELLED_PERMANENT':'Permanent Cancellation', 'UNCLM_CANCELLED':'Temporary Cancellation' %>
			   				<td colspan="2"><table><s:radio name="denyPwtStatus" title="On selecting 'Temporary Cancellation' This Ticket No. and VIRN No. Will be Accepted in LMS in Future." id="denyTempPwtStatusId" list="{'Temporary Cancellation'}"  onclick="return confirm('On selecting Temporary Cancellation This Ticket No. and VIRN No. Will be Accepted in LMS in Future.')" /></table></td>
			   			</tr>
			   			<tr>
			   				<% //'CANCELLED_PERMANENT':'Permanent Cancellation', 'UNCLM_CANCELLED':'Temporary Cancellation' %>
			   				<td colspan="2"><table><s:radio name="denyPwtStatus" title="On selecting 'Permanent Cancellation' This Ticket No. and VIRN No. Will Never be Accepted in LMS in Future." id="denyPwtStatusId" list="{'Permanent Cancellation'}"  onclick="return confirm('On selecting Permanent Cancellation This Ticket No. and VIRN No. Will Never be Accepted in LMS in Future.')"/></table></td>
			   			</tr>
			   			<tr><td align="right" colspan="2"><s:submit theme="simple" formId="form1" action="denyPWT" value="Deny PWT"  onclick="return checkDenyPwtStatus();" onkeypress="return checkDenyPwtStatus();"/></td></tr>			   			
			</table>
					
		
		
	</s:form>  
  </div>
  </div>
  
</body>
</html><code id="headId" style="visibility: hidden">$Id: approvePwtDetails.jsp,v 1.1 2010/04/02 12:12:06 gaurav Exp $</code>