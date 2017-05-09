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
		</head>

<body>
	<div id="wrap">

  <div id="top_form">
  	<table width="100%" border="0" align="center" cellpadding="0" cellspacing="0">
  	<TR>
  		<TD>
  		<div style="position: absolute;height: 20px" id="testDiv1">
   	 <table width="75%" border="1" cellpadding="5" cellspacing="0" bordercolor="#CCCCCC" align="center">
			   <tr>
			     <th width="15%">Month</th>
			     <th width="15%">Service </th>
			     <th width="15%">Status </th>
			     <th width="15%"> Amount</th>
			     <th width="15%">Approve</th>			     
			     
			    
			   </tr>
		</table> 
	</div>
	</TD>
	</TR>
	<TR>
		<TD>
			<div style="position: relative;height: 20px" id="testDiv2">
				 <table width="75%" border="0" cellpadding="5" cellspacing="0"  align="center">
				   <tr>
				     <td colspan=5>&nbsp;</td>
				   </tr>
				</table> 
			</div>
		</TD>
		</TR>
	<TR>
	<TD>
			   <div style="position: absolute;height: 180px; overflow-x: hidden;overflow-y: scroll;" id="testDiv">
			   <table width="75%" border="1" cellpadding="5" cellspacing="0"
			bordercolor="#CCCCCC" align="center">
			   <s:if test="#session.TASK_SEARCH_RESULTS.size !=0">
			  <s:iterator id="task" value="#session.TASK_SEARCH_RESULTS" status="taskIndex">
			    <tr id="<s:property value="#taskIndex.index"/>" >
			      <td width="15%"> <s:property  id="task"  value="%{month}"/></td>
			      <td width="15%"> <s:property value="serviceName"/></td>
			      <td width="15%"> <s:property value="status"/></td>
			      <td width="15%"> <s:set name="amount" value="amount"/><%=FormatNumber.formatNumberForJSP(pageContext.getAttribute("amount"))%></td>
			      <td width="15%"> <s:a targets="result1" theme="ajax" href="bo_act_deductionApprove_Save.action?taskId=%{taskId}&month=%{month}" onclick="document.getElementById('%{#taskIndex.index}').style.display='none';">Approve</s:a></td>
			     </tr>
			  </s:iterator> 
			  	</s:if>
			  	<s:else>
					<tr>
						<td colspan="5" align="center">No Records to Process</td>
					</tr>
				</s:else>
				</table>
				</div>
				</TD>
				</TR>
	</table>

	
	
	</div></div>		
</body>
</html>
<code id="headId" style="visibility: hidden">
$RCSfile: bo_act_deductionApprove_Search.jsp,v $
$Revision: 1.4 $
</code>