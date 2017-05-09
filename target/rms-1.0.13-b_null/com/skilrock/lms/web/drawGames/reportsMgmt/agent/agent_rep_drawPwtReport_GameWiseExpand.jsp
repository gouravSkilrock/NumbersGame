
<%@page import="com.skilrock.lms.common.utility.FormatNumber"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<s:if test="%{#session.isExpand}">
<div align="center">
	<h3>
		<u>For Details Please Choose start date after
			 <s:property value="%{lastDate}" />
			
		</u>
	</h3>
</div>
</s:if>
<s:else>
<div align="center">
	<h3>
		<u>Draw <s:text name="PWT"/> <s:text name="Report"/> <s:property value="%{reportType}" />&nbsp;
			From : <s:property value="%{start_date}" />
			&nbsp;&nbsp;To&nbsp;:&nbsp; <s:property value="%{end_Date}" />
		</u>
	</h3>
</div>

<s:if test="%{reportType=='Retailer Wise Expand'}">
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
				Amount in
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
				S. No.
			</th>
			<th>
				Game Name
			</th>
			<th>
				Prize Amount
			</th>
			<th>
				No. Of Tickets
			</th>
			<%-- <th>
				<s:text name="label.state"/>
			</th>
			<th>
				<s:text name="label.city"/>
			</th> --%>
			<th>
				MRP Amount
			</th>
			<s:if test="%{reportType=='Retailer Wise Expand'}">
				<th>
					Net Amount
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
				<td align="right">
					<s:set name="noOfTkt" value="%{noOfTkt}" />
					<%=pageContext.getAttribute("noOfTkt")%>
				</td>
					<%-- <td nowrap="nowrap"><s:property value="stateCode" /></td>
					<td nowrap="nowrap"><s:property value="cityCode" /></td> --%>
					<td align="right">
					<s:set name="pwtMrpAmt" value="%{pwtMrpAmt}" />
					<%=FormatNumber.formatNumberForJSP(pageContext
									.getAttribute("pwtMrpAmt"))%>
				</td>
				<s:if test="%{reportType=='Retailer Wise Expand'}">
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
				Total
			</th>
			<th align="right"><%=FormatNumber.formatNumberForJSP(pageContext
								.getAttribute("totMrpPwt"))%></th>
			<s:if test="%{reportType=='Retailer Wise Expand'}">
				<th align="right"><%=FormatNumber.formatNumberForJSP(pageContext
									.getAttribute("totNetPwt"))%></th>
			</s:if>
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
</s:else>
<code id="headId" style="visibility: hidden">
$RCSfile: agent_rep_drawPwtReport_GameWiseExpand.jsp,v $
$Revision: 1.3 $
</code>