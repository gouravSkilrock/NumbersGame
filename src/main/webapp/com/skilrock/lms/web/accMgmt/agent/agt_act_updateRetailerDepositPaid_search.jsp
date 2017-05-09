<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="com.skilrock.lms.common.utility.FormatNumber"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%
	response.setHeader("Cache-Control", "no-store"); //HTTP 1.1
	response.setHeader("Pragma", "no-cache"); //HTTP 1.0
	response.setDateHeader("Expires", 0); //prevents caching at the proxy server
%>

<s:if test="postSaleDepositAgentDateMap.size()>0">
<table width="100%" border="1" cellpadding="2" cellspacing="0"
	bordercolor="#CCCCCC" align="center" valign="top">
	<tr>
		<!--<th width="10%"> 
			Date
		</th>
		--><th width="15%">
			Deposit Amount
		</th>
		<th width="10%">
			 Deposit Comm Rate
		</th>
		
		<th width="10%">
			 Deposit Comm Amount
		</th>
		<th width="15%">
			
			Tax Amount
		</th>
	<th width="10%">
			
			Gprs Charges
		</th>
		<th width="15%">
			
			Net Amount To Pay
		</th>
		<th width="15%">
			
			Status
		</th>
		
	</tr>
	<s:iterator  value="%{postSaleDepositAgentDateMap}" >
		
		<tr >
			<!--<td>
				<s:property value="%{value.date}" />
				
			</td>
	        --><td align="right">
			<s:set name="depAmount" value="%{value.depAmount}" />
			  <%=FormatNumber.formatNumberForJSP(pageContext.getAttribute("depAmount")) %>				
			</td>
			<td align="right">
			<s:set name="depCommRate" value="%{value.depCommRate}" />
			  <%=FormatNumber.formatNumberForJSP(pageContext.getAttribute("depCommRate")) %>				
			</td>
			<td align="right">
			<s:set name="commAmount" value="%{value.commAmount}" />
			  <%=FormatNumber.formatNumberForJSP(pageContext.getAttribute("commAmount")) %>				
			</td>
			<td align="right">
			<s:set name="taxCharges" value="%{value.taxCharges}" />
			  <%=FormatNumber.formatNumberForJSP(pageContext.getAttribute("taxCharges")) %>				
			</td>
			<td align="right">
			<s:set name="charges_1" value="%{value.charges_1}" />
			  <%=FormatNumber.formatNumberForJSP(pageContext.getAttribute("charges_1")) %>				
			</td>
			<td align="right">
			<s:set name="netAmount" value="%{value.netAmount}" />
			  <%=FormatNumber.formatNumberForJSP(pageContext.getAttribute("netAmount")) %>				
			</td>
			<td>
			<s:set name="status" value="%{value.status}" />
			<s:if test="%{value.status == 'APPROVED'}">
			Pending To Pay
			</s:if>
			<s:else>
			Paid
			</s:else>
				
				
			</td>
		</tr>
	</s:iterator>
	<div id="butDiv">
	<s:if test="#status == 'APPROVED'">
	
	<tr>
		<td colspan="7" align="center">
			<table>
			<input type="button" onclick="updateAgentComm()"
					value="Update Selected" class="button" />
					<s:hidden name="stDate" id="stDate" value="%{startDate}"></s:hidden>
					<s:hidden name="enDate" id="enDate" value="%{endDate}"></s:hidden>
					
			<!--<s:a href="com/skilrock/lms/web/accMgmt/bo_act_update_postagent_deposit_submit.action?startDate=%{startDate}&endDate=%{endDate}&agentOrgId=%{agentOrgId}">Submit</s:a>
				
			--></table>
		</td>
	</tr>
	
	</s:if>
	</div>
	</s:if>
	<s:else>
	No Record Found
	</s:else>
	
	
	
</table>
<div id="subDiv"><s:property value="%{resStatus}"/> </div>
<code id="headId" style="visibility: hidden">
	$RCSfile: agt_act_updateRetailerDepositPaid_search.jsp,v $ $Revision: 1.2 $
</code>