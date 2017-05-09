<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="com.skilrock.lms.common.utility.FormatNumber"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

	
<html>
	<s:head theme="ajax"/>

<body>
<div id="wrap">

  <div id="top_form">
	 <table width="100%" border="0" align="center" cellpadding="0" cellspacing="0">
			   			<TR>
  		<TD>
  		<div style="height: 20px" id="testDiv1">
   		 <table width="90%" border="1" cellpadding="5" cellspacing="0" bordercolor="#CCCCCC" align="center"> 
			   <tr>
				     <th width="15%" align="center">Month</th>
				     <th width="15%" align="center">Service</th>
				     <th width="15%" align="center">Status </th>
				     <th width="15%" align="center">Amount</th>
				     <th width="15%" align="center">Approve</th>			     
				     <th width="15%" align="center"><s:text name="VAT"/> Details</th>
			  </tr>
		</table>
		 </div>
		 </TD>
		 </TR>
		 <TR>
		<TD>
			<div style="height: 9px" id="testDiv2">
				 <table width="95%" border="0" cellpadding="5" cellspacing="0"  align="center">
				   <tr>
				     <td colspan=6>&nbsp;</td>
				   </tr>
				</table> 
			</div>
		</TD>
		</TR>
		<TR>
	<TD>
			   <div  id="testDiv">
			   <table width="90%" border="1" cellpadding="5" cellspacing="0"
			bordercolor="#CCCCCC" align="center">
			  <s:if test="#session.TASK_SEARCH_RESULTS.size !=0">
			  <s:iterator id="task" value="#session.TASK_SEARCH_RESULTS" status="taskIndex">
			  <s:set name="amountStr" value="amount" ></s:set>
			    <tr id="<s:property value="#taskIndex.index"/>">
			      <td width="15%" align="center"><s:property  id="task"  value="%{month}"/></td>
			      <td width="15%" align="center"><s:property value="serviceName"/></td>
			      <td width="15%" align="center"><s:property value="status"/></td>
			      
			      <td width="15%" align="center"><%=FormatNumber.formatNumberForJSP(pageContext.getAttribute("amountStr")) %></td>
			      <td width="15%" align="center"><s:a  targets="result1" theme="ajax" href="bo_act_vatApprove_Save.action?taskId=%{taskId}&month=%{month}" onclick="document.getElementById('%{#taskIndex.index}').style.display='none';">Approve</s:a></td>
			     <!--  <td><a href="bo_act_vatApprove_Detail.action?taskId=<s:property value = "%{taskId}"/>& month=<s:property value = "%{month}"/>& serviceCode=<s:property value = "%{serviceCode}"/>" >detail<a></td> -->
			     <td width="15%" align="center"><s:a  targets="result1" theme="ajax" href="bo_act_vatApprove_Detail.action?taskId=%{taskId}&month=%{month}& serviceCode=%{serviceCode}" >detail</s:a></td>
			     </tr>
			  </s:iterator> 
			  	</s:if>
			  	<s:else>
					<tr>
						<td colspan="7" align="center">No Records to Process</td>
					</tr>
				</s:else>
				</table>
				</div>
				</TD>
				</TR>
				
			 </table> 
			
	
	</div>
	</div>
		
</body>
</html>
<code id="headId" style="visibility: hidden">
$RCSfile: bo_act_vatApprove_Search.jsp,v $
$Revision: 1.5 $
</code>