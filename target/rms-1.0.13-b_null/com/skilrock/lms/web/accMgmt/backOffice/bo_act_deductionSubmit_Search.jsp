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
		</head>
<body>


	<div id="wrap">

  <div id="top_form">
   		 <table width="684" border="1" cellpadding="3" cellspacing="0" bordercolor="#CCCCCC" align="center">
				 
			   <tr><th align="center" colspan="5">Search Result</th></tr>
			   
			 <s:if test="#session.TASK_TDS_SEARCH_RESULTS.size !=0">
			 <tr>
			     <th>Month</th>
			     <th>Service</th>
			     <th>Status </th>
			     <th>Type</th>
			     <th>Amount</th>
			     <th>Details</th>			     
			     
			    
			   </tr>
			  <s:iterator id="task" value="#session.TASK_TDS_SEARCH_RESULTS" status="taskIndex">
			    <tr id="<s:property value="#taskIndex.index"/>">
			      
			      <td> <s:property  id="task"  value="%{month}"/></td>
			      <td> <s:property value="serviceName"/></td>
			      <td> <s:property value="status"/></td>
			      <td> <s:property value="transactionType"/></td>
			      <td> <s:set name="amount" value="amount"/><%=FormatNumber.formatNumberForJSP(pageContext.getAttribute("amount"))%></td>
			      <td> <s:a targets="result1" theme="ajax" href="com/skilrock/lms/account/bo_act_deductionSubmit_Save.action?taskId=%{taskId}&transactionType=%{transactionType}&month=%{month}&amt=%{amount}&serviceCode=%{serviceCode}" onclick="document.getElementById('%{#taskIndex.index}').style.display='none';">Submit</s:a></td>
			     </tr>
			  </s:iterator>
			 </s:if>
			 <s:else>
				<tr>
					<td colspan="5" align="center">No Records to Process for Tax Submission</td>
				</tr>
			 </s:else>
			 </table> 
			
			<div id="ans">
			</div>
	<div id="subCheck" style="position:absolute;"></div>
	
	</div></div>		
</body>
</html>
<code id="headId" style="visibility: hidden">
$RCSfile: bo_act_deductionSubmit_Search.jsp,v $
$Revision: 1.2 $
</code>