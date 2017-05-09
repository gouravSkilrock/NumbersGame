
<%@page import="com.skilrock.lms.common.utility.FormatNumber"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<br />
<br />
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
		<u>Draw Sale <s:text name="Report"/> <s:property value="%{reportType}" />&nbsp;
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
				Price Amount
			</th>
			<th>
				No. Of Tickets
			</th>
			<th>
				MRP Amount
			</th>
			<s:if test="%{reportType=='Retailer Wise Expand'}">
				<th>
					Net Amount
				</th>
			</s:if>
		</tr>
		<s:set name="totMrpSale" value="0.0"></s:set>
		<s:set name="totNetSale" value="0.0"></s:set>
		<s:iterator id="result" value="#session.reportList" status="index">
			<tr class="startSortable">
				<td nowrap="nowrap">
					<s:property value="%{#index.index+1}" />
				</td>
				<td nowrap="nowrap">
					<s:property value="gameName" />
				</td>
				<td align="right">
					<s:set name="unitPriceAmt" value="%{unitPriceAmt}" />
					<%=FormatNumber.formatNumberForJSP(pageContext
									.getAttribute("unitPriceAmt"))%>
				</td>
				<td align="right">
					<s:set name="noOfTkt" value="%{noOfTkt}" />
					<%=pageContext.getAttribute("noOfTkt")%>
				</td>
				<td align="right">
					<s:set name="saleMrpAmt" value="%{saleMrpAmt}" />
					<%=FormatNumber.formatNumberForJSP(pageContext
									.getAttribute("saleMrpAmt"))%>
				</td>
				<s:if test="%{reportType=='Retailer Wise Expand'}">
					<td align="right">
						<s:set name="saleNetAmt" value="%{saleNetAmt}" />
						<%=FormatNumber.formatNumberForJSP(pageContext
										.getAttribute("saleNetAmt"))%>
					</td>
					<s:set name="totNetSale" value="#totNetSale+saleNetAmt"></s:set>
				</s:if>
			</tr>
			<s:set name="totMrpSale" value="#totMrpSale+#saleMrpAmt"></s:set>

		</s:iterator>
		<tr>
			<th colspan="4">
				Total
			</th>
			<th align="right"><%=FormatNumber.formatNumberForJSP(pageContext
								.getAttribute("totMrpSale"))%></th>
			<s:if test="%{reportType=='Retailer Wise Expand'}">
				<th align="right"><%=FormatNumber.formatNumberForJSP(pageContext
									.getAttribute("totNetSale"))%></th>
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
$RCSfile: agent_rep_drawSaleReport_GameWiseExpand.jsp,v $
$Revision: 1.2 $
</code>