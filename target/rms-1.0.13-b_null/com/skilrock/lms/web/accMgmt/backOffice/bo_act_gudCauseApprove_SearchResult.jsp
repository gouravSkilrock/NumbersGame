
<%@page import="com.skilrock.lms.common.utility.FormatNumber"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html>
	<head>
		<s:head theme="ajax"/>
		
	</head>
<body onload="disable()">
		<div id="err" style="display:none; font-size:14px;">Please select at least one game to approve</div>
	  	<s:form action="bo_act_gudCauseApprove_Save" name="approve_gov_comm" id="approve_gov_comm">
			<table width="750" border="1" cellpadding="3" cellspacing="0" bordercolor="#CCCCCC" align="center">
				<tr>
					<th align="center" colspan="5"> <s:text name="Good_Cause"/> Amount Details</th>
				</tr>
				<tr>			
					<th align="center">Game Name</th>
					<th align="center">From Date</th>
					<th align="center">To Date</th>
					<th align="center"><s:text name="Good_Cause"/> Amt</th>
					<th align="center">Approve</th>						
				</tr>
					<s:if test="#session.boTaskApproveList.size()>0">
					<s:iterator value="#session.boTaskApproveList" status="rowNu">
						<s:set name="checkBoxNo" value="#rowNu.index" />
						<tr>
								<td align="left"><s:property value="value.gameId" /> - <s:property value="value.gameName" /></td>
								<td align="center"><s:property value="value.startDate" /></td>
								<td align="center"><s:property value="value.endDate" /></td>
								<td align="right"><s:set name="amount" value="value.govAmount"/><%=FormatNumber.formatNumberForJSP(pageContext.getAttribute("amount"))%></td>
								<td align="center"><input type="checkbox" checked="checked"  name="taskIds" onclick="return hideErr();" value="<s:property value="value.taskId" />"/></td>
						</tr>
					</s:iterator>
					<tr>
							<th align="center" colspan="3">Total <s:text name="Good_Cause"/> Amount</th>
							<th align="right"><s:set name="amount" value="#session.boTaskApproveTotalAmt"/><%=FormatNumber.formatNumberForJSP(pageContext.getAttribute("amount"))%></th>							
							<th align="center"><input type="submit"  value="Approve" onclick="return checkboxValidate();" /></th>
					</tr>	
					</s:if>
					<s:else>
						<tr>
							<th colspan="5" align="center"> No <s:text name="Good_Cause"/> Amount To be Approve</th>
						</tr>
					</s:else>				
			 </table>
			 <br/><br>
	</s:form>
			
</body>
</html>
<code id="headId" style="visibility: hidden">
$RCSfile: bo_act_gudCauseApprove_SearchResult.jsp,v $
$Revision: 1.3 $
</code>