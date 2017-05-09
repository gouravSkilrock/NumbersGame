<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="com.skilrock.lms.common.utility.FormatNumber"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">

<head>



<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />

<link rel="stylesheet" href="<%=request.getContextPath() %>/LMSImages/css/styles.css"  type="text/css"/>

<title><%=application.getAttribute("JSP_PAGE_TITLE")%></title>

	</head>
	<body>
	
	<%@include file="/com/skilrock/lms/web/loginMgmt/menu.jsp"%>
	
	<div id="wrap">

  <div id="top_form">
   <h3>VIRN Validation for <s:text name="PWT"/> Receipt TNV</h3>
	
		<s:form>
			
			
			<table border="1" cellpadding="3" cellspacing="0" bordercolor="#CCCCCC" width="100%">                
           
               <s:if test="%{#session.saveBOPwtTicketsData.isSuccess}">
			   <tr><th colspan="7" align="center">The <s:text name="PWT"/> of <s:property  value="%{#application.currencyWord}" />: <s:set name="strAmount" value="%{#session.saveBOPwtTicketsData.amount}" /><%=FormatNumber.formatNumberForJSP(pageContext.getAttribute("strAmount")) %> is valid and should be paid</th></tr>            
               </s:if>
               <tr>
	               <th nowrap="nowrap">Game Name</th>
	               <th nowrap="nowrap"><s:property value="#application.TIER_MAP.AGENT" /> Name</th>
	               <th>Virn Code</th>
	               <th><s:text name="PWT"/> Amount</th>
	               <th>Verification Status</th>
	               <th>Message Code</th>
	               <th>Message</th> 
	               <th>Ticket NBR</th>
	               <th>Ticket MSG</th>                
               </tr>
			
			   <s:iterator value="%{#session.saveBOPwtTicketsData.pwtList}" >
			
			   <tr>
			    
				    <s:if test="%{isValid}">
				  			    				
				    	<td nowrap="nowrap"><s:property value="%{#session.saveBOPwtTicketsData.gameNbr_Name}" /></td>
				    	<td nowrap="nowrap"><s:property value="%{#session.saveBOPwtTicketsData.agtOrgName}" /></td>	   
					    <td><s:property  value="%{virnCode}"/></td>
					    <td><s:set name="strpwtAmount" value="%{pwtAmount}" /><%=FormatNumber.formatNumberForJSP(pageContext.getAttribute("strpwtAmount")) %></td>
					    <td><s:property   value="%{verificationStatus}"/>&nbsp;</td>
					    <td><s:property   value="%{messageCode}"/>&nbsp;</td>
					    <s:set name="messageFormatted" value="%{message}"></s:set>
					    <td nowrap="nowrap"><%=pageContext.getAttribute("messageFormatted")%></td>	
					     <td><s:property   value="%{ticketNumber}"/>&nbsp;</td>
					      <td><s:property   value="%{ticketMessage}"/>&nbsp;</td>			    			
				    </s:if>
			       <s:else>
				   <s:if test="%{!isHighLevel}">
				   
				   		<td nowrap="nowrap"><font color="red"><s:property value="%{#session.saveBOPwtTicketsData.gameNbr_Name}" /></font></td>
				    	<td nowrap="nowrap"><font color="red"><s:property value="%{#session.saveBOPwtTicketsData.agtOrgName}" /></font></td>
				    	<td><font color="red"><s:property  value="%{virnCode}"/></font></td>
					    <td><font color="red"><s:label theme="simple" value="No Prize"/></font></td>
					    <td><font color="red"><s:property   value="%{verificationStatus}"/>&nbsp;</font></td>
					    <td><font color="red"><s:property   value="%{messageCode}"/>&nbsp;</font></td>
					    <s:set name="messageFormatted" value="%{message}"></s:set>					   
					    <td nowrap="nowrap"><font color="red"> <%=pageContext.getAttribute("messageFormatted")%></font></td>
					    <td><s:property   value="%{ticketNumber}"/>&nbsp;</td>
					      <td><s:property   value="%{ticketMessage}"/>&nbsp;</td>				    
					  </s:if>
					  <s:else>

					  	<td nowrap="nowrap"><font color="blue"><s:property value="%{#session.saveBOPwtTicketsData.gameNbr_Name}" /></font></td>
				    	<td nowrap="nowrap"><font color="blue"><s:property value="%{#session.saveBOPwtTicketsData.agtOrgName}" /></font></td>
				    	<td><font color="blue"><s:property  value="%{virnCode}"/></font></td>
					    <td><font color="blue"><s:label theme="simple" value="No Prize"/></font></td>
					    <td><font color="blue"><s:property   value="%{verificationStatus}"/>&nbsp;</font></td>
					    <td><font color="blue"><s:property   value="%{messageCode}"/></font>&nbsp;</td>
					    <s:set name="messageFormatted" value="%{message}"></s:set>
					    <td nowrap="nowrap"><font color="blue"><%=pageContext.getAttribute("messageFormatted")%></font></td>
					    <td><s:property   value="%{ticketNumber}"/>&nbsp;</td>
					      <td><s:property   value="%{ticketMessage}"/>&nbsp;</td>
				     </s:else>
				   </s:else>		    
			</tr>     
			</s:iterator>
        </table>

           		
			<table width="400" border="0" cellpadding="3" cellspacing="0" bordercolor="#CCCCCC" align="left">
			<tr>
			  <td><s:a theme="simple" href="bo_pwt_virnVerification_Menu_TNV.action">Enter More PWT</s:a></td>
			</tr>
			<s:if test="%{#session.saveBOPwtTicketsData.isSuccess}">
			   <tr>  
			      <td><a theme="simple" target="_blank" href="<%=request.getContextPath()%>/com/skilrock/lms/web/reportsMgmt/rep_common_openPDF.action">Show Receipt</a></td>
			   </tr>
			</s:if>
			</table>
		</s:form>
</div></div>
	</body>
</html>

<code id="headId" style="visibility: hidden">
$RCSfile: bo_pwt_virnVerification_SuccessTNV.jsp,v $
$Revision: 1.2 $
</code>