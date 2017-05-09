<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="com.skilrock.lms.common.utility.FormatNumber"%>
<%@page import="com.skilrock.lms.common.utility.CommonMethods"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@page import="java.text.NumberFormat"%>
	
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
	
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
	
<head>	
		<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />	
		<link rel="stylesheet" href="<%=request.getContextPath() %>/LMSImages/css/styles.css"  type="text/css"/>	
		<title><%=application.getAttribute("JSP_PAGE_TITLE")%></title>	
		<script type="text/javascript" src="<%=request.getContextPath() %>/com/skilrock/lms/web/scratchService/pwtMgmt/backOffice/js/playerPendingPWTDetails.js"></script>
		<script type="text/javascript" src="<%=request.getContextPath() %>/com/skilrock/lms/web/scratchService/pwtMgmt/backOffice/js/denyPwt.js"></script>
		<s:head theme="ajax" debug="false"/>
		<link type="text/css" rel="stylesheet" href="<%=request.getContextPath() %>/LMSImages/css/lmsCalendar.css" media="screen"/>
		<script>
		var projectName="<%=request.getContextPath() %>"
		
			function _subTrans(){
			return (  calculateNetAmt() && _subValid('subTrans'));
			}
		</script>
		<script type="text/javascript" src="<%=request.getContextPath() %>/com/skilrock/lms/web/common/globalJs/calender.js"></script>		
	
</head>

<body >
	<%@include file="/com/skilrock/lms/web/loginMgmt/menu.jsp" %>
	<div id="wrap">
	<div id="top_form">
   		<h3>Player <s:text name="PWT"/> Details</h3>
		<s:actionerror/>
		<s:actionmessage/>

		<s:form  id="form1" name="form1" >
			<s:iterator id="SearchGameResults" value="#session.SEARCH_RESULTS_PWT">			   
				<table border="1" width="50%" align="center" style="border-color: #CCCCCC; table-layout: auto;" cellspacing="0" cellpadding="3">
					 <tr>
				 	 	<th>Player First Name</th>
				 	 	<td><s:property value="playerFirstName"/></td>
				 	</tr>
					 <tr>
					  		<th>Player Last Name</th>
					  		<td><s:property value="playerLastName"/></td>
					 </tr>
					 <tr>
					 		<th>Game Name</th>
					 		<td><s:property value="gameName"/></td>
					 </tr>
					 <tr>
					 		<th>Game Number</th>
					 		<td><s:property value="gameNbr"/></td>
					 </tr>
					 <tr>
					 	    <th>Transaction Date</th>
					 	    <td><table><s:property value="trancDate"/></table></td>
					 </tr>
					 <tr>
					    	<th>Status</th>
					    	<td><s:property value="status"/></td>
					 </tr>
					 <tr>
					     	<th>PWT Amount </th>
					     	<td><s:set name="PWT_AMOUNT" value="pwtAmt" /><%=FormatNumber.formatNumberForJSP(pageContext.getAttribute("PWT_AMOUNT"))%>
					     	
					     	<s:hidden name="pwtAmt" id="pwtAmt" value="%{pwtAmt}"/>
					     	<s:hidden name="transactionId" id="transactionId" value="%{transactionId}"/></td>
					     	<s:hidden name="virnCode" id="virnCode" value="%{virnCode}"/>
					     	<s:hidden name="ticketNbr" id="ticketNbr" value="%{ticketNbr}"/>
					     	<s:hidden name="gameId" id="gameId" value="%{gameId}"/>
					     	<s:hidden name="gameNbr" id="gameNbr" value="%{gameNbr}"/>
					 </tr>
					 <s:if test="%{detail=='No'}">
				 <tr>
				 		<td  colspan=2><div id="tax1"></div></td>
				 </tr>
				 <tr>
						<th>Tax</th><td ><s:textfield name="tax" id="tax" theme="simple" onchange="calculateNetAmt();" onblur="calculateNetAmt();" value=""/></td>
			   	 </tr>
			     <tr>
			   			<th>Net Amount</th><td><s:textfield name="netAmt" id="netAmt" readonly="true" theme="simple" value=""/></td>
			   	 </tr>
			   	 <tr><td  colspan=2><div id="chequeVerify"></div></td></tr>
			   	 <tr> <th>cheque Nbr</th><td ><s:textfield name="chequeNbr" id="chequeNbr" maxlength="6" theme="simple"/></td></tr>
			   	 <tr> <th>Drawee Bank</th><td ><s:textfield name="draweeBank" id="draweeBank" theme="simple"/></td></tr>
			     <tr> <th>Cheque date</th><td >
			     
			     		<table>
			     			<%String endDate = CommonMethods.convertDateInGlobalFormat((String)session.getAttribute("presentDate"), "yyyy-mm-dd", "dd-MM-yyyy");%>
							
								<input type="text" name="chequeDate" id="chequeDate" value="" readonly size="12"/>
    							<input type="button" style=" width:19px; height: 19px; background: url('<%=request.getContextPath() %>/LMSImages/imagesCal/dateIcon.gif'); top left; border:0 ; " onclick="displayCalendar(document.getElementById('chequeDate'), 'dd-MM-yyyy', this, '<%=endDate%>', '<%=endDate%>', 'false')" />
    	
			     		</table>
			     		
			     		
			     		 </td></tr>
			     <tr> <th>Issuing Party Name</th><td ><s:textfield name="issuingParty" id="issuingParty" value="%{#application.ORG_NAME_JSP}" size="30" theme="simple"/></td></tr>
			     <tr>
			   		<td><s:submit theme="simple" formId="form1" action="CancelPWTReceive" value="Back To Search"/></td>
			   		<td align="right">
			   		<table><div id="subCheck" style="position:absolute;"></div>
			   			<s:submit theme="simple" formId="form1" action="CommitDirectPlayerPWTReceive" value="Submit" id="subTrans" onclick="return _subTrans()" />
			   			<s:submit theme="simple" formId="form1"  value="Deny"  onclick=" return showDenyFields();" onkeypress="return showDenyFields();" />
			 		</table>
			 		</td>
			    </tr>
			   
			    <tr><td colspan="2"></td></tr>
			   
			   
			   </s:if>
			   <s:else>
			   		<s:set name="tax"  value="%{tax}"></s:set>
			   		<s:set name="netAmt"   value="%{netAmt}"></s:set>
					<%Double taxDouble=(Double)pageContext.getAttribute("tax");
						Double netAmtDouble=(Double)pageContext.getAttribute("netAmt");
						String tax=FormatNumber.formatNumberForJSP(taxDouble);
						tax=tax.replace(",","");
						String netAmount=FormatNumber.formatNumberForJSP(netAmtDouble);
						netAmount=netAmount.replace(",","");
						session.setAttribute("TAX",tax);
						session.setAttribute("NET_AMOUNT",netAmount);
 					%>
			  		<tr> <th>Tax</th><td ><s:textfield name="tax" id="tax" theme="simple" disabled="true" value="%{#session.TAX}"/></td></tr>
			  		<tr><th>Net Amount</th><td><s:textfield name="netAmt" id="netAmt" disabled="true" theme="simple" value="%{#session.NET_AMOUNT}"/></td></tr>
			   		<tr><td><h3>Cheque Deatils</h3></td></tr>
			   		<tr><th>Cheque Number</th><td><s:textfield name="chqNumber" id="chnum" disabled="true" theme="simple" value="%{chequeNbr}"/></td></tr>
			   		<tr><th>Cheque Date</th><td><s:textfield name="chqDate" id="chqDate" disabled="true" theme="simple" value="%{chequeDate}"/></td></tr>
                	<tr><th>Drawee Bank</th><td><s:textfield name="bankName" id="bankName" disabled="true" theme="simple" value="%{draweeBank}"/></td></tr>			   
			    	<tr><td><s:submit theme="simple" formId="form1" action="CancelPWTReceive" value="Cancel" cssClass="button"/></td></tr>
		  	</s:else>
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
			   			<tr><td align="right" colspan="2"><s:submit theme="simple" formId="form1" action="DenyDirectPlayerPWTReceive" value="Deny PWT"  onclick="return checkDenyPwtStatus();" onkeypress="return checkDenyPwtStatus();"/></td></tr>			   			
			</table>
    </s:iterator> 

</s:form>
	<br /><br/>
		<br /><br/>
			<br /><br/>
</body>
</html>
<code id="headId" style="visibility: hidden">
$RCSfile: playerPendingPWTDetails.jsp,v $
$Revision: 1.2 $
</code>