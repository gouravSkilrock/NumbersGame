
<%@page import="com.skilrock.lms.common.utility.FormatNumber"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<s:set  name="startDate" value="%{start_date}" />
<s:set name="endDate"  value="%{end_Date}" />
<s:set name="state" value="%{stateCode}" />
<s:set name="city" value="%{cityCode}" />

<div align="center">
			<h3>
				<u><s:text name="label.draw.sale"/> <s:text name="Report"/> <s:text name="label.gamewise"/>&nbsp; <s:text name="label.from"/> : <s:property value="startDate" />
					&nbsp;&nbsp;<s:text name="label.to"/>&nbsp;:&nbsp; <s:property value="endDate"/></u>
			</h3>
		</div>

<s:if test="#session.reportList.size>0">
				<s:div id="excel"
					cssStyle="text-align:center;width: 100%;font-size: 9pt">
					<s:a href="bo_rep_lsSaleReport_exportToExcel.action?reportType=Game Wise&startDate=%{startDate}&endDate=%{endDate}"><s:text name="btn.exportasexcel"/></s:a>
				</s:div>
</s:if>

<table width="100%" border="1" cellpadding="3" cellspacing="0"
	bordercolor="#CCCCCC" align="center">
	<s:if test="%{#session.reportList!=null && #session.reportList.size()>0}">
	<tr>
		<th align="center" colspan="2" ><s:property value="#session.orgName" /></th>
		<th align="center"><s:text name="label.amt.in"/> <s:property value="#application.CURRENCY_SYMBOL" /></th>
	</tr>
	<tr>
		<td align="center" colspan="3"><s:property value="#session.orgAdd" /></td>
	</tr>
	<!--<tr align="center">
			<th colspan="4">
				<s:a href="bo_rep_drawSaleReport_SearchExpand.action?reportType=Game Wise Expand&start_date=%{#startDate}&end_Date=%{#endDate}" targets="result" theme="ajax"><s:text name="label.expand"/></s:a>
			</th>
		</tr>
		--><tr align="center">
			<th>
				<s:text name="label.sNo"/>
			</th>
			<th>
				<s:text name="label.game.name"/>
			</th>
			<th>
				<s:text name="label.mrp.amt"/>
			</th>
		</tr>
		<s:set name="totMrpSale" value="0.0"></s:set>
		<s:iterator id="result" value="#session.reportList" status="index">
			<tr class="startSortable">
				<td nowrap="nowrap">
					<s:property value="%{#index.index+1}" />
				</td>
				<td nowrap="nowrap">
					<s:property value="gameName" />
				</td>
				<td align="right">
						<s:set name="saleMrpAmt" value="%{saleMrpAmt}" />
						<%=FormatNumber.formatNumberForJSP(pageContext.getAttribute("saleMrpAmt"))%>
				</td>			
			</tr>
			<s:set name="totMrpSale" value="#totMrpSale+#saleMrpAmt"></s:set>
		</s:iterator>
		<tr>
			<th colspan="2"><s:text name="label.total"/></th>
			<th align="right"><%=FormatNumber.formatNumberForJSP(pageContext.getAttribute("totMrpSale"))%></th>
		</tr>
	</s:if>
	<s:else>
		<tr>
			<th align="center">
				<s:text name="msg.no.record"/>
			</th>
		</tr>
	</s:else>
</table>

<code id="headId" style="visibility: hidden">
	$RCSfile: bo_rep_saleLsReport_result.jsp,v $ $Revision: 1.3 $
</code>