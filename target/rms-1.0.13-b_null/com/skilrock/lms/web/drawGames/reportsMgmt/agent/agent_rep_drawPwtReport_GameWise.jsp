
<%@page import="com.skilrock.lms.common.utility.FormatNumber"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<s:set  name="startDate" value="%{start_date}" />
<s:set name="endDate"  value="%{end_Date}" />

<div align="center">
			<h3>
				<u>Draw <s:text name="PWT"/> <s:text name="Report"/> Game Wise&nbsp; From : <s:property value="startDate" />
					&nbsp;&nbsp;To&nbsp;:&nbsp; <s:property value="endDate"/></u>
			</h3>
		</div>

<s:if test="#session.reportList.size>0">
<s:div id="excel"
					cssStyle="text-align:center;width: 100%;font-size: 9pt">
					<s:a href="agent_rep_drawPwtReport_exportExcel.action?reportType=Game Wise&start_date=%{#startDate}&end_Date=%{#endDate}&agentOrgId=%{agentOrgId}">Export As Excel</s:a>
				</s:div>
</s:if>

<table width="100%" border="1" cellpadding="3" cellspacing="0"
	bordercolor="#CCCCCC" align="center">
	<s:if test="%{#session.reportList!=null && #session.reportList.size()>0}">
	<tr>
		<th align="center" colspan="2" ><s:property value="#session.orgName" /></th>
		<th align="center">Amount in <s:property value="#application.CURRENCY_SYMBOL" /></th>
	</tr>
	<!--<tr>
		<td align="center" colspan="3"><s:property value="#session.orgAdd" /></td>
	</tr>
	--><tr align="center">
			<th colspan="4">
				<s:a href="agent_rep_drawPwtReport_SearchExpand.action?reportType=Game Wise Expand&start_date=%{#startDate}&end_Date=%{#endDate}&agentOrgId=%{agentOrgId}" targets="result" theme="ajax">Expand</s:a>
			</th>
		</tr>
		<tr align="center">
			<th>
				S. No.
			</th>
			<th>
				Game Name
			</th>
			<th>
				MRP Amount
			</th>
		</tr>
		<s:set name="totMrpPwt" value="0.0"></s:set>
		<s:iterator id="result" value="#session.reportList" status="index">
			<tr class="startSortable">
				<td nowrap="nowrap">
					<s:property value="%{#index.index+1}" />
				</td>
				<td nowrap="nowrap">
					<s:property value="gameName" />
				</td>
				<td align="right">
						<s:set name="pwtMrpAmt" value="%{pwtMrpAmt}" />
						<%=FormatNumber.formatNumberForJSP(pageContext.getAttribute("pwtMrpAmt"))%>
				</td>				
			</tr>
			<s:set name="totMrpPwt" value="#totMrpPwt+#pwtMrpAmt"></s:set>
		</s:iterator>
		<tr>
			<th colspan="2">Total</th>
			<th align="right"><%=FormatNumber.formatNumberForJSP(pageContext.getAttribute("totMrpPwt"))%></th>
		</tr>
	</s:if>
	<s:else>
		<tr>
			<th align="center">
				No Records Found
			</th>
		</tr>
	</s:else>
</table>


<code id="headId" style="visibility: hidden">
$RCSfile: agent_rep_drawPwtReport_GameWise.jsp,v $
$Revision: 1.3 $
</code>