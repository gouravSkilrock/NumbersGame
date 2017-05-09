<%@page import="com.skilrock.lms.beans.DateBeans"%>
<%@page import="com.skilrock.lms.common.utility.GetDate"%>
<%@page import="com.skilrock.lms.common.utility.FormatNumber"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<s:set name="DG" value="%{#session.isDG}"></s:set>
<s:set name="SE" value="%{#session.isSE}"></s:set>
<s:set name="SLE" value="%{#session.isSLE}"></s:set>
<s:set name="IW" value="%{#session.isIW}"></s:set>
<s:set name="VS" value="%{#session.isVS}"></s:set>
<s:set name="colCount" value="2" />
<s:if test="%{#DG&&#SE}">
	<s:set name="colCount" value="3" />
</s:if>
<s:if test="%{#DG&&#SLE}">
	<s:set name="colCount" value="4" />
</s:if>
<s:if test="%{#DG&&#IW}">
	<s:set name="colCount" value="4" />
</s:if>
<s:if test="%{#DG&&#VS}">
	<s:set name="colCount" value="4" />
</s:if>
<s:if test="%{#DG&&#SE&&#SLE}">
	<s:set name="colCount" value="5" />
</s:if>
<s:if test="%{#DG&&#SE&&#IW}">
	<s:set name="colCount" value="5" />
</s:if>
<s:if test="%{#DG&&#SE&&#VS}">
	<s:set name="colCount" value="5" />
</s:if>
<s:if test="%{#DG&&#SE&&#SLE&&#IW}">
	<s:set name="colCount" value="6" />
</s:if>
<s:if test="%{#DG&&#SE&&#SLE&&#VS}">
	<s:set name="colCount" value="6" />
</s:if>
<s:if test="%{#DG&&#SE&&#SLE&&#VS&&#IW}">
	<s:set name="colCount" value="7" />
</s:if>
<s:if test="%{reportType!='Day Wise'}">
	<table width="100%" border="1" cellpadding="3" cellspacing="0"
		bordercolor="#CCCCCC" align="center">
		<tr align="center">
			<th colspan="<s:property value="#colCount"/>">
				Report for Winning By Player
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
			<s:if test="%{#SLE}">
				<th>
					Sports Lottery
				</th>
			</s:if>
			<s:if test="%{#IW}">
				<th>
					Instant Win
				</th>
			</s:if>
			<s:if test="%{#VS}">
				<th>
					Virtual Sports
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
					<%=FormatNumber.formatNumberForJSP(pageContext
							.getAttribute("agtDGDirPwt"))%>
				</td>
			</s:if>
			<s:if test="%{#SE}">
				<td>
					<s:set name="agtSEDirPwt" value="%{#session.scratchDirPlyPwt}" />
					<%=FormatNumber.formatNumberForJSP(pageContext
							.getAttribute("agtSEDirPwt"))%>
				</td>
			</s:if>
			<s:if test="%{#SLE}">
				<td>
					<s:set name="agtSLEDirPwt" value="%{#session.sleDirPlyPwt}" />
					<%=FormatNumber.formatNumberForJSP(pageContext
							.getAttribute("agtSLEDirPwt"))%>
				</td>
			</s:if>
			<s:if test="%{#IW}">
				<td>
					<s:set name="agtIWDirPwt" value="%{#session.iwDirPlyPwt}" />
					<%=FormatNumber.formatNumberForJSP(pageContext.getAttribute("agtIWDirPwt"))%>
				</td>
			</s:if>
			<s:if test="%{#VS}">
				<td>
					<s:set name="agtVSDirPwt" value="%{#session.vsDirPlyPwt}" />
					<%=FormatNumber.formatNumberForJSP(pageContext.getAttribute("agtVSDirPwt"))%>
				</td>
			</s:if>
		</tr>
	</table>
	<br />
</s:if>
<s:if test="%{#DG&&#SE}">
	<s:set name="colCount" value="4" />
</s:if>
<s:if test="%{#DG&&#SLE}">
	<s:set name="colCount" value="4" />
</s:if>
<s:if test="%{#DG&&#IW}">
	<s:set name="colCount" value="4" />
</s:if>
<s:if test="%{#DG&&#VS}">
	<s:set name="colCount" value="4" />
</s:if>
<s:if test="%{#DG&&#SE&&#SLE}">
	<s:set name="colCount" value="6" />
</s:if>
<s:if test="%{#DG&&#SE&&#IW}">
	<s:set name="colCount" value="6" />
</s:if>
<s:if test="%{#DG&&#SE&&#VS}">
	<s:set name="colCount" value="6" />
</s:if>
<s:if test="%{#DG&&#SE&&#SLE&&#IW}">
	<s:set name="colCount" value="8" />
</s:if>
<s:if test="%{#DG&&#SE&&#SLE&&#VS}">
	<s:set name="colCount" value="8" />
</s:if>
<s:if test="%{#DG&&#SE&&#SLE&&#VS&&#IW}">
	<s:set name="colCount" value="10" />
</s:if>

<div align="center">
	<h3>
		<u>Sale <s:text name="PWT" /> <s:text name="Collection" /> <s:text
				name="Report" /> &nbsp; <%
 	if ("".equals(((DateBeans) session.getAttribute("datebean"))
 			.getReportType())) {
 %> From : <%=GetDate.getConvertedDate(((DateBeans) session
						.getAttribute("datebean")).getStartDate())%>
			&nbsp;&nbsp;To&nbsp;:&nbsp; <%=GetDate.getConvertedDate(((DateBeans) session
						.getAttribute("datebean")).getEndDate())%> <%
 	} else {
 		out.println("Of : "
 				+ ((DateBeans) session.getAttribute("datebean"))
 						.getReportType());
 	}
 %> </u>
	</h3>
</div>

<div id="excel" align="center"text-align:left;width: 750px;font-size: 9pt">
	<s:a
		href="bo_rep_CompltCollReport_ExpExcel.action?orgType=AGENT&start_date=%{start_date}&end_Date=%{end_Date}&reportType=%{reportType}">Export As Excel</s:a>
</div>

<table width="100%" border="1" cellpadding="3" cellspacing="0"
	bordercolor="#CCCCCC" align="center">
	<tr>
		<th align="center" colspan="<s:property value="#colCount+1"/>">
			<s:property value="#session.orgName" />
		</th>
		<th align="center">
			Amount in
			<s:property value="#application.CURRENCY_SYMBOL" />
		</th>
	</tr>
	<tr>
		<td align="center" colspan="<s:property value="#colCount+2"/>">
			<s:property value="#session.orgAdd" />
		</td>
	</tr>
	<s:if
		test="%{#session.result_retailer!=null && #session.result_retailer.size>0}">

		<tr align="center">
			<th rowspan="3">
				<s:if test="%{reportType=='Retailer Wise'}">
				Party Name
				</s:if>
				<s:elseif test="%{reportType=='Day Wise'}">
				Date
				</s:elseif>
			</th>
			<th colspan="<s:property value="#colCount"/>">
				<s:a
					href="agt_rep_completeCollectionReport_SearchExpand.action?agtId=%{agtId}&start_date=%{start_date}&end_Date=%{end_Date}&reportType=%{reportType} Expand"
					theme="ajax" targets="result">Service Name
				</s:a>

			</th>
			<th rowspan="3">
				Net Amount
			</th>
		</tr>
		<tr align="center">
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
			<s:if test="%{#SLE}">
				<th colspan="2">
					Sports Lottery
				</th>
			</s:if>
			<s:if test="%{#IW}">
				<th colspan="2">
					Instant Win
				</th>
			</s:if>
			<s:if test="%{#VS}">
				<th colspan="2">
					Virtual Sports
				</th>
			</s:if>
		</tr>
		<tr align="center">
			<s:if test="%{#DG&&#SE}">
				<th>
					Sale Amount
				</th>
				<th>
					<s:text name="PWT" />
					Amount
				</th>
			</s:if>
			<s:if test="%{#DG&&#SLE}">
				<th>
					Sale Amount
				</th>
				<th>
					<s:text name="PWT" />
					Amount
				</th>
			</s:if>
			<s:if test="%{#DG&&#IW}">
				<th>
					Sale Amount
				</th>
				<th>
					<s:text name="PWT" />
					Amount
				</th>
			</s:if>
			<s:if test="%{#DG&&#VS}">
				<th>
					Sale Amount
				</th>
				<th>
					<s:text name="PWT" />
					Amount
				</th>
			</s:if>
			<th>
				Sale Amount
			</th>
			<th>
				<s:text name="PWT" />
				Amount
			</th>

		</tr>

		<s:set name="dgSale" value="0.0" />
		<s:set name="dgPwt" value="0.0" />
		<s:set name="slSale" value="0.0" />
		<s:set name="slPwt" value="0.0" />
		<s:set name="iweSale" value="0.0" />
		<s:set name="iwePwt" value="0.0" />
		<s:set name="vsSale" value="0.0" />
		<s:set name="vsPwt" value="0.0" />
		<s:set name="seSale" value="0.0" />
		<s:set name="sePwt" value="0.0" />
		<s:set name="totCash" value="0.0" />
		<s:set name="drawCash" value="0.0" />
		<s:set name="scratchCash" value="0.0" />
		<s:set name="sleCash" value="0.0" />
		<s:set name="iwCash" value="0.0" />
		<s:set name="vsCash" value="0.0" />

		<s:iterator id="result" value="#session.result_retailer">
			<s:set name="orgValue" value="%{value}"></s:set>
			<tr class="startSortable">
				<td>
					<s:if test="%{reportType=='Retailer Wise'}">
						<s:property value="%{#orgValue.orgName}" />
					</s:if>
					<s:elseif test="%{reportType=='Day Wise'}">
						<s:a
							href="agt_rep_completeCollectionReport_RetalerWise_OnDayWise.action?reportType=Retailer Wise&dateType=SQL&agtId=%{agtId}&start_date=%{#orgValue.orgName}&end_Date=%{#orgValue.orgName}&agtOrgName=%{#orgValue.orgName}"
							theme="ajax" targets="result">
							<s:property value="%{#orgValue.orgName}" />
						</s:a>
					</s:elseif>
				</td>
				<s:if test="%{#DG}">
					<td>
						<s:set name="drawSale"
							value="%{#orgValue.drawSale-#orgValue.drawCancel}" />
						<%=FormatNumber.formatNumberForJSP(pageContext
								.getAttribute("drawSale"))%>
					</td>
					<td>
						<s:set name="drawPwt"
							value="%{#orgValue.drawPwt+#orgValue.drawDirPlyPwt}" />
						<%=FormatNumber.formatNumberForJSP(pageContext
								.getAttribute("drawPwt"))%>
					</td>
					<s:set name="drawCash"
						value="%{#orgValue.drawSale-#orgValue.drawCancel-#orgValue.drawPwt-#orgValue.drawDirPlyPwt}"></s:set>
				</s:if>
				<s:if test="%{#SE}">
					<td>
						<s:set name="scratchSale" value="%{#orgValue.scratchSale}" />
						<%=FormatNumber.formatNumberForJSP(pageContext
								.getAttribute("scratchSale"))%>
					</td>
					<td>
						<s:set name="scratchPwt"
							value="%{#orgValue.scratchPwt+#orgValue.scratchDirPlyPwt}" />
						<%=FormatNumber.formatNumberForJSP(pageContext
								.getAttribute("scratchPwt"))%>
					</td>
					<s:set name="scratchCash"
						value="%{#orgValue.scratchSale-#orgValue.scratchPwt-#orgValue.scratchDirPlyPwt}"></s:set>
				</s:if>
				<s:if test="%{#SLE}">
					<td>
						<s:set name="sleSale"
							value="%{#orgValue.sleSale-#orgValue.sleCancel}" />
						<%=FormatNumber.formatNumberForJSP(pageContext
								.getAttribute("sleSale"))%>
					</td>
					<td>
						<s:set name="slePwt"
							value="%{#orgValue.slePwt+#orgValue.sleDirPlyPwt}" />
						<%=FormatNumber.formatNumberForJSP(pageContext
								.getAttribute("slePwt"))%>
					</td>
					<s:set name="sleCash"
						value="%{#orgValue.sleSale-#orgValue.sleCancel-#orgValue.slePwt-#orgValue.sleDirPlyPwt}"></s:set>
				</s:if>
				<s:if test="%{#IW}">
					<td>
						<s:set name="iwSale" value="%{#orgValue.iwSale-#orgValue.iwCancel}" />
						<%=FormatNumber.formatNumberForJSP(pageContext.getAttribute("iwSale"))%>
					</td>
					<td>
						<s:set name="iwPwt" value="%{#orgValue.iwPwt+#orgValue.iwDirPlyPwt}" />
						<%=FormatNumber.formatNumberForJSP(pageContext.getAttribute("iwPwt"))%>
					</td>
					<s:set name="iwCash" value="%{#orgValue.iwSale-#orgValue.iwCancel-#orgValue.iwPwt-#orgValue.iwDirPlyPwt}"></s:set>
				</s:if>
				<s:if test="%{#VS}">
					<td>
						<s:set name="vsSale" value="%{#orgValue.vsSale-#orgValue.vsCancel}" />
						<%=FormatNumber.formatNumberForJSP(pageContext.getAttribute("vsSale"))%>
					</td>
					<td>
						<s:set name="vsPwt" value="%{#orgValue.vsPwt+#orgValue.vsDirPlyPwt}" />
						<%=FormatNumber.formatNumberForJSP(pageContext.getAttribute("vsPwt"))%>
					</td>
					<s:set name="vsCash" value="%{#orgValue.vsSale-#orgValue.vsCancel-#orgValue.vsPwt-#orgValue.vsDirPlyPwt}"></s:set>
				</s:if>
				<th>
					<s:set name="cash" value="#drawCash+#scratchCash+#sleCash+#iwCash+#vsCash"></s:set>
					<%=FormatNumber.formatNumberForJSP(pageContext.getAttribute("cash"))%>
				</th>
				<s:set name="dgSale"
					value="%{#dgSale+#orgValue.drawSale-#orgValue.drawCancel}" />
				<s:set name="dgPwt"
					value="%{#dgPwt+#orgValue.drawPwt+#orgValue.drawDirPlyPwt}" />
				<s:set name="seSale" value="%{#seSale+#orgValue.scratchSale}" />
				<s:set name="sePwt"
					value="%{#sePwt+#orgValue.scratchPwt+#orgValue.scratchDirPlyPwt}" />
				<s:set name="slSale"
					value="%{#slSale+#orgValue.sleSale-#orgValue.sleCancel}" />
				<s:set name="slPwt"
					value="%{#slPwt+#orgValue.slePwt+#orgValue.sleDirPlyPwt}" />
				<s:set name="iweSale"
					value="%{#iweSale+#orgValue.iwSale-#orgValue.iwCancel}" />
				<s:set name="iwePwt"
					value="%{#iwePwt+#orgValue.iwPwt+#orgValue.iwDirPlyPwt}" />
				<s:set name="vsPwt"
					value="%{#vsPwt+#orgValue.vsPwt+#orgValue.vsDirPlyPwt}" />
				<s:set name="totCash" value="%{#totCash+#cash}" />
			</tr>
		</s:iterator>
		<tr>
			<th>
				Total
			</th>
			<s:if test="%{#DG}">
				<th>
					<%=FormatNumber.formatNumberForJSP(pageContext
							.getAttribute("dgSale"))%>
				</th>
				<th>
					<%=FormatNumber.formatNumberForJSP(pageContext
							.getAttribute("dgPwt"))%>
				</th>
			</s:if>
			<s:if test="%{#SE}">
				<th>
					<%=FormatNumber.formatNumberForJSP(pageContext
							.getAttribute("seSale"))%>
				</th>
				<th>
					<%=FormatNumber.formatNumberForJSP(pageContext
							.getAttribute("sePwt"))%>
				</th>
			</s:if>
			<s:if test="%{#SLE}">
				<th>
					<%=FormatNumber.formatNumberForJSP(pageContext
							.getAttribute("slSale"))%>
				</th>
				<th>
					<%=FormatNumber.formatNumberForJSP(pageContext
							.getAttribute("slPwt"))%>
				</th>
			</s:if>
			<s:if test="%{#IW}">
				<th>
					<%=FormatNumber.formatNumberForJSP(pageContext
							.getAttribute("iweSale"))%>
				</th>
				<th>
					<%=FormatNumber.formatNumberForJSP(pageContext
							.getAttribute("iwePwt"))%>
				</th>
			</s:if>
			<s:if test="%{#VS}">
				<th>
					<%=FormatNumber.formatNumberForJSP(pageContext
							.getAttribute("vsSale"))%>
				</th>
				<th>
					<%=FormatNumber.formatNumberForJSP(pageContext
							.getAttribute("vsPwt"))%>
				</th>
			</s:if>
			<th>
				<%=FormatNumber.formatNumberForJSP(pageContext
						.getAttribute("totCash"))%>
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
	$RCSfile: agt_rep_completeCollectionReport_RetailerWise.jsp,v $
	$Revision: 1.3 $
</code>