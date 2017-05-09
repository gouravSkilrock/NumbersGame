	<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@page import="com.skilrock.lms.common.utility.FormatNumber"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
	
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
	
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />	
	<link rel="stylesheet" href="<%=request.getContextPath() %>/LMSImages/css/styles.css" type="text/css"/>	
	<title><%=application.getAttribute("JSP_PAGE_TITLE")%></title>	
	<s:head theme="ajax" debug="false"/>
</head>

<body>
	

<div id="wrap">

  <div id="top_form">
   		 <table width="100%" border="1" cellpadding="3" cellspacing="0" bordercolor="#CCCCCC" align="center">
			   
			   
			   <tr>
			     <th align="center">Month</th>
			     <th align="center">Service</th>
			     <th align="center">Status </th>
			     <th align="center">Type</th>
			     <th align="center">Amount</th>
			     <th align="center">Approve</th>			     
			     <th align="center">VAT Details</th>
			    
			   </tr>
			   <s:if test="#session.TASK_SEARCH_RESULTS.size !=0">
			  <s:iterator id="task" value="#session.TASK_SEARCH_RESULTS" status="taskIndex">
			  <s:set name="amountStr" value="amount" ></s:set>
			    <tr id="<s:property value="#taskIndex.index"/>">
				      <td><s:property  id="task"  value="%{month}"/></td>
				       <td><s:property value="serviceName"/></td>
				      <td><s:property value="status"/></td>
				      <td>&nbsp;<s:property value="transactionType"/></td>
				      <td><%=FormatNumber.formatNumberForJSP(pageContext.getAttribute("amountStr")) %></td>
				      <td><s:a targets="result1" theme="ajax" href="agt_act_vatApprove_Save.action?taskId=%{taskId}&month=%{month}" onclick="document.getElementById('%{#taskIndex.index}').style.display='none';">Approve</s:a></td>
				      <td><s:a targets="result1" theme="ajax" href="agt_act_vatApprove_Detail.action?taskId=%{taskId}&month=%{month}& serviceCode=%{serviceCode}" >detail</s:a></td>
			     </tr>
			  </s:iterator> 
			  	</s:if>
			  	<s:else>
					<tr>
						<td colspan="7" align="center">No Records to Process</td>
					</tr>
				</s:else>
			 </table> 

	
	
	</div></div>		
</body>
</html>
<code id="headId" style="visibility: hidden">
$RCSfile: agt_act_vatApprove_Search.jsp,v $
$Revision: 1.2 $
</code>