
<%@page import="com.skilrock.lms.common.utility.FormatNumber"%><%@ taglib prefix="s" uri="/struts-tags"%>
<div align="center">
	<h3>
		<u>Collection <s:text name="Report"/> for <s:property value="%{agtOrgName}" /> </u>
	</h3>
</div>
<s:set name="DG" value="%{#session.isDG}"></s:set>
<s:set name="SE" value="%{#session.isSE}"></s:set>
<s:set name="colCount" value="2" />
	<s:if test="%{#DG&&#SE}">
		<s:set name="colCount" value="3" />
	</s:if>
<s:if test="%{reportType=='Retailer Wise'}">
<table width="100%" border="1" cellpadding="3" cellspacing="0"
	bordercolor="#CCCCCC" align="center">
	<tr align="center">
		<th colspan="<s:property value="#colCount"/>" >
			<s:text name="Report"/> for <s:text name="PWT"/> By Player
		</th>
	</tr>
	<tr align="center">
		<th>
			Organization Name
		</th>
		<s:if test="%{#DG}">
			<th>
				Draw Game
			</th>
		</s:if>
		<s:if test="%{#SE}">
			<th>
				Scratch Game
			</th>
		</s:if>
	</tr>

	<tr>
		<td>
			<s:property value="%{agtOrgName}" />
		</td>
		<s:if test="%{#DG}">
			<td>
				<s:set name="agtDGDirPwt" value="%{#session.drawDirPlyPwt}" />
				<%=FormatNumber.formatNumberForJSP(pageContext.getAttribute("agtDGDirPwt"))%>
			</td>
		</s:if>
		<s:if test="%{#SE}">
			<td>
				<s:set name="agtSEDirPwt" value="%{#session.scratchDirPlyPwt}" />
				<%=FormatNumber.formatNumberForJSP(pageContext.getAttribute("agtSEDirPwt"))%>
			</td>
		</s:if>
	</tr>
</table>
<br />
</s:if>
<table width="100%" border="1" cellpadding="3" cellspacing="0"
	bordercolor="#CCCCCC" align="center">
	<s:if test="%{#session.result_retailer!=null&& #session.result_retailer.size>0}">
		<tr align="center">
			<th rowspan="2">
				Organization Name
			</th>
			<s:if test="%{#DG}">
				<th colspan="2">
					Draw Game
				</th>
			</s:if>
			<s:if test="%{#SE}">
				<th colspan="2">
					Scratch Game
				</th>
			</s:if>
			<th rowspan="2">
				Net Amount
			</th>
		</tr>
		<tr align="center">
			<s:if test="%{#DG&&#SE}">
				<th>
					Sale Amount
				</th>
				<th>
					<s:text name="PWT"/> Amount
				</th>
			</s:if>
			<th>
				Sale Amount
			</th>
			<th>
				<s:text name="PWT"/> Amount
			</th>

		</tr>

		<s:set name="dgSale" value="0.0" />
		<s:set name="dgPwt" value="0.0" />
		<s:set name="seSale" value="0.0" />
		<s:set name="sePwt" value="0.0" />
		<s:set name="totCash" value="0.0" />
		<s:set name="drawCash" value="0.0" />
		<s:set name="scratchCash" value="0.0" />
		<s:iterator id="result" value="#session.result_retailer">
			<s:set name="orgValue" value="%{value}"></s:set>
			<tr class="startSortable">
				<td>
					<s:property value="%{#orgValue.orgName}" />
				</td>
				<s:if test="%{#DG}">
					<td>
						<s:set name="drawSale" value="%{#orgValue.drawSale-#orgValue.drawCancel}" />
						<%=FormatNumber.formatNumberForJSP(pageContext.getAttribute("drawSale"))%>
					</td>
					<td>
						<s:set name="drawPwt" value="%{#orgValue.drawPwt+#orgValue.drawDirPlyPwt}" />
						<%=FormatNumber.formatNumberForJSP(pageContext.getAttribute("drawPwt"))%>
					</td>
					<s:set name="drawCash"
						value="%{#orgValue.drawSale-#orgValue.drawCancel-#orgValue.drawPwt-#orgValue.drawDirPlyPwt}"></s:set>
				</s:if>
				<s:if test="%{#SE}">
					<td>
						<s:set name="scratchSale" value="%{#orgValue.scratchSale}" />
						<%=FormatNumber.formatNumberForJSP(pageContext.getAttribute("scratchSale"))%>
					</td>
					<td>
						<s:set name="scratchPwt"
							value="%{#orgValue.scratchPwt+#orgValue.scratchDirPlyPwt}" />
						<%=FormatNumber.formatNumberForJSP(pageContext.getAttribute("scratchPwt"))%>
					</td>
					<s:set name="scratchCash"
						value="%{#orgValue.scratchSale-#orgValue.scratchPwt-#orgValue.scratchDirPlyPwt}"></s:set>
				</s:if>
				<th>


					<s:set name="cash" value="#drawCash+#scratchCash"></s:set>
					<%=FormatNumber.formatNumberForJSP(pageContext.getAttribute("cash"))%>

				</th>
				<s:set name="dgSale"
					value="%{#dgSale+#orgValue.drawSale-#orgValue.drawCancel}" />
				<s:set name="dgPwt" value="%{#dgPwt+#orgValue.drawPwt+#orgValue.drawDirPlyPwt}" />
				<s:set name="seSale" value="%{#seSale+#orgValue.scratchSale}" />
				<s:set name="sePwt" value="%{#sePwt+#orgValue.scratchPwt+#orgValue.scratchDirPlyPwt}" />
				<s:set name="totCash" value="%{#totCash+#cash}" />
			</tr>
		</s:iterator>
		<tr>
			<th>
				Total
			</th>
			<s:if test="%{#DG}">
				<th>
					<%=FormatNumber.formatNumberForJSP(pageContext.getAttribute("dgSale"))%>
				</th>
				<th>
					<%=FormatNumber.formatNumberForJSP(pageContext.getAttribute("dgPwt"))%>
				</th>
			</s:if>
			<s:if test="%{#SE}">
				<th>
					<%=FormatNumber.formatNumberForJSP(pageContext.getAttribute("seSale"))%>
				</th>
				<th>
					<%=FormatNumber.formatNumberForJSP(pageContext.getAttribute("sePwt"))%>
				</th>
			</s:if>
			<th>
				<%=FormatNumber.formatNumberForJSP(pageContext.getAttribute("totCash"))%>
			</th>
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
$RCSfile: agt_rep_completeCollectionReportMRP_DayWise.jsp,v $
$Revision: 1.2 $
</code>