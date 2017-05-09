<%@ page import="org.apache.struts2.components.Form"%>
<%@page import="com.skilrock.lms.common.utility.FormatNumber"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<div align="center">
	<h1>Agent Wise Incentive Report</h1>
</div>
<s:if test="agentMap != null && agentMap.size() > 0">
	<div align="center">
		<s:form action="bo_rep_agent_Incentive_ExpExcel" method="post" enctype="multipart/form-data">
			<s:hidden id="tableValue" name="reportData" />
			<s:hidden id="userType" name="userType" value="Agent" />
			<s:submit value="Export As Excel" align="center" onclick="getData();" />
		</s:form>
	</div>
</s:if>

<div id="tableDataDiv">
	<div align="center">
		<h2>
			From <s:property value="fromDate" />
			to <s:property value="toDate" />
		</h2>
	</div>
	<table width="100%" border="1" cellpadding="3" cellspacing="0" bordercolor="#CCCCCC" align="center" style="font-size: 12px">
		<s:if test="agentMap != null && agentMap.size() > 0">
			<tr>
				<th>Agent Name</th>
				<th>Sale Amount</th>
				<th>Wining Amount</th>
				<th>Non Winning Amount</th>
				<th>Incentive Amount</th>
			</tr>
			<s:set name="saleSumAgt" value="0.0" />
			<s:set name="winSumAgt" value="0.0" />
			<s:set name="nonWinSumAgt" value="0.0" />
			<s:set name="incentiveSumAgt" value="0.0" />
			<s:iterator value="agentMap.values()">
				<tr class="startSortable">
					<td>
						<s:a href="bo_rep_retailers_incentive_search.action?orgId=%{organizationId}&orgName=%{organizationName}&fromDate=%{fromDate}&toDate=%{toDate}" theme="ajax" targets="result">
							<s:property value="organizationName" />
						</s:a>
					</td>
					<td>
						<s:property value="sale" /><s:set name="saleTotAgt" value="sale" />
					</td>
					<td>
						<s:property value="winning" /><s:set name="winTotAgt" value="winning" />
					</td>
					<td>
						<s:property value="%{sale - winning}" /><s:set name="nonWinTotAgt" value="%{sale - winning}" />
					</td>
					<td>
						<s:property value="incentiveAmount" /><s:set name="incentiveTotAgt" value="incentiveAmount" />
					</td>
				</tr>
				<s:set name="saleSumAgt" value="#saleSumAgt+#saleTotAgt" />
				<s:set name="winSumAgt" value="#winSumAgt+#winTotAgt" />
				<s:set name="nonWinSumAgt" value="#nonWinSumAgt+#nonWinTotAgt" />
				<s:set name="incentiveSumAgt" value="#incentiveSumAgt+#incentiveTotAgt" />
			</s:iterator>
			<tr>
				<th>Total</th>
				<th><%=FormatNumber.formatNumberForJSP(pageContext.getAttribute("saleSumAgt"))%></th>
				<th><%=FormatNumber.formatNumberForJSP(pageContext.getAttribute("winSumAgt"))%></th>
				<th><%=FormatNumber.formatNumberForJSP(pageContext.getAttribute("nonWinSumAgt"))%></th>
				<th><%=FormatNumber.formatNumberForJSP(pageContext.getAttribute("incentiveSumAgt"))%></th>
			</tr>
		</s:if>
		<s:else>
			<center>No Records Found.</center>
		</s:else>
	</table>
</div>
<code id="headId" style="visibility: hidden"> $RCSfile:
	bo_rep_incentive_Data.jsp,v $ $Revision: 1.3 $ </code>