	<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="com.skilrock.lms.common.utility.FormatNumber"%>
	<%@ taglib prefix="s" uri="/struts-tags"%>
	
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
   <h3>Submit <s:text name="Good_Cause"/></h3>

			 <table width="684" border="1" cellpadding="3" cellspacing="0" bordercolor="#CCCCCC" align="center">
			   
			   <tr>
			   <td colspan="7" align="center"><b>Search Results</b></td>
			   </tr>
			   
			   <tr>
			     <th>Game Name</th>
			     <th>Status </th>
			     <th>Type</th>
			     <th>From Date</th>
			     <th>To Date</th>
			     <th>Amount</th>
			     <th>Submit</th>			     
			   </tr>
			 <s:if test="#session.TASK_Govt_Comm_SEARCH_RESULTS.size !=0">
			  <s:iterator id="task" value="#session.TASK_Govt_Comm_SEARCH_RESULTS">
			    <tr>
			      
			      <td> <s:property  id="task"  value="%{gameId}"/>-<s:property value="gameName"/></td>
			      <td> <s:property value="status"/></td>
			      <td> <s:property value="transactionType"/></td>
			      <td><s:property value="startDate"/></td>
			      <td><s:property value="endDate"/></td>
			      <td> <s:set name="amount" value="amount"/><%=FormatNumber.formatNumberForJSP(pageContext.getAttribute("amount"))%></td>
			      	<s:if test="%{#session.serviceCode=='DG'}">
			      		<td> <s:a href="com/skilrock/lms/account/bo_act_gudCauseSubmit_Detail.action?gameId=%{gameId}& taskId=%{taskId}& transactionType=%{transactionType}& amt=%{amount}&serviceName=DG">DETAILS</s:a></td>
					</s:if>
					<s:elseif test="%{#session.serviceCode=='SLE'}">
						<td>
			      			<s:a href="com/skilrock/lms/account/bo_act_gudCauseSubmit_Detail.action?gameId=%{gameId}& taskId=%{taskId}& transactionType=%{transactionType}& amt=%{amount}&serviceName=SLE">DETAILS</s:a>
			      		</td>
					</s:elseif>
					<s:else>
						<s:if test="%{#session.serviceCode=='SE'}">
							<td> <s:a href="com/skilrock/lms/account/bo_act_gudCauseSubmit_Detail.action?gameId=%{gameId}& taskId=%{taskId}& transactionType=%{transactionType}& amt=%{amount}&serviceName=SE">DETAILS</s:a></td>
						</s:if>
					</s:else>
			     </tr>
			  </s:iterator> 
			</s:if>
			<s:else>
				<tr>
					<td colspan="7" align="center">No Records to Process</td>
				</tr>
			</s:else>
			</table>
			<div id="ans">
			</div></div></div>	
	
						
</body>
</html>
<code id="headId" style="visibility: hidden">
$RCSfile: bo_act_gudCauseSubmit_Search.jsp,v $
$Revision: 1.3 $
</code>