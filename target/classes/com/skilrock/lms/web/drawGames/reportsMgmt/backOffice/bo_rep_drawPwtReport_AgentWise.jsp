
<%@page import="com.skilrock.lms.common.utility.FormatNumber"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<s:set name="startDate" value="%{start_date}" />
<s:set name="endDate" value="%{end_Date}" />
<s:set name="state" value="%{stateCode}" />
<s:set name="city" value="%{cityCode}" />

<div align="center">
			<h3>
				<u><s:text name="label.draw"/> <s:text name="PWT"/> <s:text name="Report"/> <s:property value="#application.TIER_MAP.AGENT"/> <s:text name="label.wise"/>&nbsp; <s:text name="label.from"/> : <s:property value="startDate" />
					&nbsp;&nbsp;<s:text name="label.to"/>&nbsp;:&nbsp; <s:property value="endDate"/></u>
			</h3>
		</div>

<s:if test="#session.reportList.size>0">
<s:div id="excel"
					cssStyle="text-align:center;width: 100%;font-size: 9pt">
					<s:a href="bo_rep_drawPwtReport_exportExcel.action?reportType=Agent Wise&start_date=%{#startDate}&end_Date=%{#endDate}&stateCode=%{#state}&cityCode=%{#city}"><s:text name="btn.exportasexcel"/></s:a>
				</s:div>
</s:if>

<table width="100%" border="1" cellpadding="3" cellspacing="0"
	bordercolor="#CCCCCC" align="center">
	<s:if
		test="%{#session.reportList!=null && #session.reportList.size()>0}">
		<tr>
			<th align="center" colspan="5"><s:property value="#session.orgName" /></th>
			<th align="center" ><s:text name="label.amt.in"/> <s:property value="#application.CURRENCY_SYMBOL" /></th>
		</tr>
		<tr>
			<td align="center" colspan="6"><s:property value="#session.orgAdd" /></td>
		</tr>
		<tr align="center">
			<th>
				<s:text name="label.sNo"/>
			</th>
			<th>
				<s:property value="#application.TIER_MAP.AGENT"/> <s:text name="label.name"/>
			</th>
			<th>
				<s:text name="label.state"/>
			</th>
			<th>
				<s:text name="label.city"/>
			</th>
			<th>
				<s:text name="label.pwt.amt"/>
			</th>
			
			<th>
				<s:text name="label.net.amt"/>
			</th>
		</tr>
		<s:set name="totMrpPwt" value="0.0"></s:set>
		<s:set name="totNetPwt" value="0.0"></s:set>
		<s:iterator id="result" value="#session.reportList" status="index">
			<tr  class="startSortable">
				<td nowrap="nowrap">
					<s:property value="%{#index.index+1}" />
				</td>
				<td nowrap="nowrap">
					<s:a
						href="bo_rep_drawPwtReport_SearchExpand.action?reportType=Agent Wise Expand&start_date=%{#startDate}&end_Date=%{#endDate}&agentOrgId=%{gameNo}&stateCode=%{#state}&cityCode=%{#city}"
						targets="result" theme="ajax">
						<s:property value="gameName" />
					</s:a>
				</td>
				<td nowrap="nowrap">
					<s:property value="stateCode" />
				</td>
				<td nowrap="nowrap">
					<s:property value="cityCode" />
				</td>
				<td align="right">
					<s:set name="pwtMrpAmt" value="%{pwtMrpAmt}" />
					<%=FormatNumber.formatNumberForJSP(pageContext
									.getAttribute("pwtMrpAmt"))%>
				</td>				
				<td align="right">
					<s:set name="pwtNetAmt" value="%{pwtNetAmt}" />
					<%=FormatNumber.formatNumberForJSP(pageContext
									.getAttribute("pwtNetAmt"))%>
				</td>
			</tr>
			<s:set name="totMrpPwt" value="#totMrpPwt+#pwtMrpAmt"></s:set>
			<s:set name="totNetPwt" value="#totNetPwt+pwtNetAmt"></s:set>
		</s:iterator>
		<tr>
			<th colspan="4">
				<s:text name="label.total"/>
			</th>
			<th align="right"><%=FormatNumber.formatNumberForJSP(pageContext
								.getAttribute("totMrpPwt"))%></th>
			<th align="right"><%=FormatNumber.formatNumberForJSP(pageContext
								.getAttribute("totNetPwt"))%></th>
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

<div align="center">
	<h3>
		<u><s:text name="label.back.ofc"/> <s:text name="Direct_player"/><s:text name="PWT"/> <s:text name="label.gamewise"/> </u>
	</h3>
</div>
<table width="100%" border="1" cellpadding="3" cellspacing="0"
	bordercolor="#CCCCCC" align="center">
	<s:if test="%{#session.reportListBODirPly!=null && #session.reportListBODirPly.size()>0}">
	<tr align="center">
			<th colspan="4">
				<s:a href="bo_rep_drawPwtReport_SearchExpand.action?reportType=BO Wise Expand&start_date=%{#startDate}&end_Date=%{#endDate}&stateCode=%{#state}&cityCode=%{#city}" targets="result" theme="ajax"><s:text name="label.expand"/></s:a>
			</th>
		</tr>
		<tr align="center">
			<th>
				<s:text name="label.sNo"/>
			</th>
			<th>
				<s:text name="label.game.name"/>
			</th>
			<%-- <th>
				<s:text name="label.state"/>
			</th>
			<th>
				<s:text name="label.city"/>
			</th> --%>
			<th>
				<s:text name="label.pwt.amt"/>
			</th>
		</tr>
		<s:set name="totMrpPwt" value="0.0"></s:set>
		<s:iterator id="result" value="#session.reportListBODirPly" status="index">
			<tr class="startSortable">
				<td nowrap="nowrap">
					<s:property value="%{#index.index+1}" />
				</td>
				<td nowrap="nowrap">
					<s:property value="gameName" />
				</td>
				<%-- <td nowrap="nowrap">
					<s:property value="stateCode" />
				</td>
				<td nowrap="nowrap">
					<s:property value="cityCode" />
				</td> --%>
				<td>
						<s:set name="pwtMrpAmt" value="%{pwtMrpAmt}" />
						<%=FormatNumber.formatNumberForJSP(pageContext.getAttribute("pwtMrpAmt"))%>
				</td>			
			</tr>
			<s:set name="totMrpPwt" value="#totMrpPwt+#pwtMrpAmt"></s:set>
		</s:iterator>
		<tr>
			<th colspan="2">Total</th>
			<th><%=FormatNumber.formatNumberForJSP(pageContext.getAttribute("totMrpPwt"))%></th>
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
$RCSfile: bo_rep_drawPwtReport_AgentWise.jsp,v $
$Revision: 1.3 $
</code>