
<%@page import="com.skilrock.lms.common.utility.FormatNumber"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<s:if test="%{#session.isExpand}">
<div align="center">
	<h3>
		<u><s:text name="msg.detail.pls.choose.st.date"/>
			 <s:property value="%{lastDate}" />
			
		</u>
	</h3>
</div>
</s:if>
<s:else>
<div align="center">
	<h3>
		<u><s:text name="label.scratch"/> <s:text name="PWT" /> <s:text name="Report" />
		<s:if test="%{reportType=='Agent Wise Expand'}">
			<s:property value="%{getText('label.agentwise')}" /> Expand&nbsp;
		</s:if>
		<s:else>
			<s:property value="%{reportType}" />&nbsp;
		</s:else> 
			<s:text name="label.from"/> : <s:property value="%{start_date}" />
			&nbsp;&nbsp;<s:text name="label.to"/>&nbsp;:&nbsp; <s:property value="%{end_Date}" />
		</u>
	</h3>
</div>

<s:if test="%{reportType=='Agent Wise Expand'}">
	<s:set name="colCount" value="5" />
</s:if>
<s:else>
	<s:set name="colCount" value="4" />
</s:else>

<table width="100%" border="1" cellpadding="3" cellspacing="0"
	bordercolor="#CCCCCC" align="center">
	<s:if
		test="%{#session.reportList!=null && #session.reportList.size()>0}">
		<tr>
			<th align="center" colspan="<s:property value="#colCount"/>">
				<s:property value="#session.orgName" />
			</th>
			<th align="center">
				<s:text name="label.amt.in"/>
				<s:property value="#application.CURRENCY_SYMBOL" />
			</th>
		</tr>
		<tr>
			<td align="center" colspan="<s:property value="#colCount+1"/>">
				<s:property value="#session.orgAdd" />
			</td>
		</tr>
		<tr align="center">
			<th>
				<s:text name="label.s.no"/>
			</th>
			<th>
				<s:text name="label.game.name"/>
			</th>
			<th>
				<s:text name="label.prize.amt"/>
			</th>
			<th>
				<s:text name="label.no.of.tkts"/>
			</th>
			<th>
				<s:text name="label.mrp.amt"/>
			</th>
			<s:if test="%{reportType=='Agent Wise Expand'}">
				<th>
					<s:text name="label.net.amt"/>
				</th>
			</s:if>
		</tr>
		<s:set name="totMrpPwt" value="0.0"></s:set>
		<s:set name="totNetPwt" value="0.0"></s:set>
		<s:iterator id="result" value="#session.reportList" status="index">
			<tr class="startSortable">
				<td nowrap="nowrap">
					<s:property value="%{#index.index+1}" />
				</td>
				<td nowrap="nowrap">
					<s:property value="gameName" />
				</td>
				<td align="right">
					<s:set name="priceAmt" value="%{priceAmt}" />
					<%=FormatNumber.formatNumberForJSP(pageContext
									.getAttribute("priceAmt"))%>
				</td>
				<td>
					<s:set name="noOfTkt" value="%{noOfTkt}" />
					<%=pageContext.getAttribute("noOfTkt")%>
				</td>
				<td align="right">
					<s:set name="pwtMrpAmt" value="%{pwtMrpAmt}" />
					<%=FormatNumber.formatNumberForJSP(pageContext
									.getAttribute("pwtMrpAmt"))%>
				</td>
				<s:if test="%{reportType=='Agent Wise Expand'}">
					<td align="right">
						<s:set name="pwtNetAmt" value="%{pwtNetAmt}" />
						<%=FormatNumber.formatNumberForJSP(pageContext
										.getAttribute("pwtNetAmt"))%>
					</td>
				<s:set name="totNetPwt" value="#totNetPwt+pwtNetAmt"></s:set>
				</s:if>
			</tr>
			<s:set name="totMrpPwt" value="#totMrpPwt+#pwtMrpAmt"></s:set>
		</s:iterator>
		<tr>
			<th colspan="4">
				<s:text name="label.total"/>
			</th>
			<th align="right"><%=FormatNumber.formatNumberForJSP(pageContext
								.getAttribute("totMrpPwt"))%><%System.out.println("stedkgjh erkgjh"+pageContext
									.getAttribute("totMrpPwt")); %></th>
			<s:if test="%{reportType=='Agent Wise Expand'}">
				<th align="right"><%=FormatNumber.formatNumberForJSP(pageContext
									.getAttribute("totNetPwt"))%></th>
			</s:if>
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
</s:else>

<code id="headId" style="visibility: hidden">
$RCSfile: bo_rep_scratchPwtReport_GameWiseExpand.jsp,v $
$Revision: 1.3 $
</code>