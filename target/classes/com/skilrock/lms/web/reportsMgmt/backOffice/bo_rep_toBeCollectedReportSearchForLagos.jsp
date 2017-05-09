<%@page import="com.skilrock.lms.beans.DateBeans"%>
<%@page import="com.skilrock.lms.common.utility.GetDate"%>
<%@page import="com.skilrock.lms.common.utility.FormatNumber"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<s:if test="%{message != null}">
<center><h3><s:label value="%{message}"></s:label></h3></center>
</s:if>
<s:else>
<div align="left">
<s:form action="bo_rep_toBeCollectedReportInExcelForLagos.action"  method="post" enctype="multipart/form-data">
<s:hidden id="tableValue" name="valueToSend" />
<s:submit value="Export As Excel" cssStyle="margin-right:-400px;background:#77b3d7;font-size:15px" onclick="getData();"/>
</s:form>
</div>
<div id="tableDataDiv">
<div align="center">
	<h3>
		<u>To Be Collected <s:text name="Report" /> <s:text name="Agent" />
			Wise &nbsp; <%
 	if ("".equals(((DateBeans) session.getAttribute("datebean"))
 			.getReportType())) {
 %>
			From : <%=GetDate.getConvertedDate(((DateBeans) session
						.getAttribute("datebean")).getStartDate())%>
			&nbsp;&nbsp;To&nbsp;:&nbsp; <%=GetDate.getConvertedDate(((DateBeans) session
						.getAttribute("datebean")).getEndDate())%>

			<%
				} else {
					out.println("Of : "
							+ ((DateBeans) session.getAttribute("datebean"))
									.getReportType());
				}
			%>
		</u>
	</h3>
</div>

<table width="100%" border="1" cellpadding="3" cellspacing="0"
	bordercolor="#CCCCCC" align="center" id="expandTable"
	style="font-size: 12px">
	<s:set name="DG" value="%{#session.isDG}"></s:set>
	<s:set name="IW" value="%{#session.isIW}"></s:set>
	<s:set name="VS" value="%{#session.isVS}"></s:set>
	<s:set name="DGCount" value="0"></s:set>
	<s:set name="IWCount" value="0"></s:set>
	<s:set name="VSCount" value="0"></s:set>	
	<s:set name="colCount" value="0" />
	<s:if test="%{#session.resultExpand!=null}">
		<s:iterator value="%{#session.gameList}">
			<s:if test="%{value=='DG'}">
				<s:set name="DGCount" value="#DGCount+1"></s:set>
			</s:if>
			<s:if test="%{value=='IW'}">
				<s:set name="IWCount" value="#IWCount+1"></s:set>
			</s:if>
			<s:if test="%{value=='VS'}">
				<s:set name="VSCount" value="#VSCount+1"></s:set>
			</s:if>
			<s:set name="colCount" value="#colCount+1"></s:set>
		</s:iterator>
		<tr>
			<th align="center" colspan="<s:property value="#colCount+9"/>">
				<s:property value="#session.orgName" />
			</th>
			
		</tr>
		<tr>
			<td align="center" colspan="<s:property value="#colCount+9"/>">
				<s:property value="#session.orgAdd" />
			</td>
		</tr>
		<tr align="center">
			<th rowspan="3">
				<s:text name="Agent" />
				NAME
			</th>
			<th rowspan="3">
				Opening Balance
			</th>
			<s:if test="%{#DG}">
				<th colspan="<s:property value="%{#DGCount}" />">
					DRAW GAME
				</th>
			</s:if>
			<s:if test="%{#IW}">
				<th colspan="<s:property value="%{#IWCount}" />">
					IW
				</th>
			</s:if>
			<s:if test="%{#VS}">
				<th colspan="<s:property value="%{#VSCount}" />">
					VS
				</th>
			</s:if>
			<th rowspan="3" nowrap="nowrap">
				DEBIT/CREDIT NOTE
			</th>

			<th rowspan="3" nowrap="nowrap">
				INCENTIVE
			</th>

			<th rowspan="3" nowrap="nowrap">
				RECEIVABLE
			</th>

			<th rowspan="3" nowrap="nowrap">
				PWT
			</th>
			<th rowspan="3" nowrap="nowrap">
				CASH
			</th>
			<th rowspan="3" nowrap="nowrap">
				BANK
			</th>
			<th rowspan="3" nowrap="nowrap">
				NET AMOUNT
			</th>
		</tr>
		<tr align="center">
			<s:if test="%{#DG}">
				<s:iterator value="%{#session.gameList}">
					<s:if test="%{value=='DG'}">
						<th>
							<s:property value="%{key}" />
						</th>
					</s:if>
					<s:if test="%{value=='IW'}">
						<th>
							<s:property value="%{key}" />
						</th>
					</s:if>
					<s:if test="%{value=='VS'}">
						<th>
							<s:property value="%{key}" />
						</th>
					</s:if>
				</s:iterator>
			</s:if>
		</tr>
		<tr align="center">
			<s:if test="%{#DG}">
				<s:iterator value="%{#session.gameList}">
					<s:if test="%{value=='DG'}">
						<th nowrap="nowrap">
							Sale Amount
						</th>
					</s:if>
					<s:if test="%{value=='IW'}">
						<th nowrap="nowrap">
							Sale Amount
						</th>
					</s:if>
					<s:if test="%{value=='VS'}">
						<th nowrap="nowrap">
							Sale Amount
						</th>
					</s:if>
				</s:iterator>
			</s:if>
		</tr>
		<s:set name="dgSale" value="0.0" />
		<s:set name="iwSale" value="0.0" />
		<s:set name="vsSale" value="0.0" />
		<s:set name="totTotal" value="0.0" />
		<s:set name="debit" value="0.0" />
		<s:set name="credit" value="0.0" />
		<s:set name="total" value="0.0" />
		<s:set name="openingBal" value="0.0" />
		<s:set name="pwt" value="0.0" />
		<s:set name="cash" value="0.0" />
		<s:set name="bank" value="0.0" />
		<s:set name="netAmt" value="0.0" />
		<s:iterator id="result" value="#session.resultExpand" status="index">
			<s:set name="orgValue" value="%{value}"></s:set>
			<tr class="startSortable" style="white-space: nowrap;">
				<td align="left" nowrap="nowrap">
					<s:property value="%{#orgValue.agentName}" />
				</td>
				<td align="right">
					<s:set name="openingBal" value="%{#orgValue.openingBal}" />
					<%=FormatNumber.formatNumberForJSP(pageContext
							.getAttribute("openingBal"))%>
				</td>
				<s:set name="gameBeanMap" value="#orgValue.gameBeanMap" />
				<s:set name="gameTotal" value="0.0" />
				<s:if test="%{#DG}">
					<s:iterator value="%{#session.gameList}">
						<s:if test="%{value=='DG'}">
							<s:set name="gameValue" value="%{#gameBeanMap[key]}" />
							<td align="right">
								<s:set name="sale" value="%{#gameValue.drawSale}" />
								<%=FormatNumber
										.formatNumberForJSP(pageContext
												.getAttribute("sale"))%>
							</td>
							<s:set name="gameTotal" value="%{#gameTotal+#gameValue.drawSale}" />
						</s:if>
					</s:iterator>
				</s:if>
				<s:if test="%{#IW}">
					<s:iterator value="%{#session.gameList}">
						<s:if test="%{value=='IW'}">
							<s:set name="gameValue" value="%{#gameBeanMap[key]}" />
							<td align="right">
								<s:set name="sale" value="%{#gameValue.iwSale}" />
								<%=FormatNumber.formatNumberForJSP(pageContext.getAttribute("sale"))%>
							</td>
							<s:set name="gameTotal" value="%{#gameTotal+#gameValue.iwSale}" />
						</s:if>
					</s:iterator>
				</s:if>
				<s:if test="%{#VS}">
					<s:iterator value="%{#session.gameList}">
						<s:if test="%{value=='VS'}">
							<s:set name="gameValue" value="%{#gameBeanMap[key]}" />
							<td align="right">
								<s:set name="sale" value="%{#gameValue.iwSale}" />
								<%=FormatNumber.formatNumberForJSP(pageContext.getAttribute("sale"))%>
							</td>
							<s:set name="gameTotal" value="%{#gameTotal+#gameValue.vsSale}" />
						</s:if>
					</s:iterator>
				</s:if>
				<th nowrap="nowrap" align="right">
					<s:set name="debit" value="%{#orgValue.debit}" />
					<%=FormatNumber.formatNumberForJSP(pageContext
							.getAttribute("debit"))%>
				</th>

				<th nowrap="nowrap" align="right">
					<s:set name="credit" value="#orgValue.credit"></s:set>
					<%=FormatNumber.formatNumberForJSP(pageContext
							.getAttribute("credit"))%>
				</th>
				<th nowrap="nowrap" align="right">
					<s:set name="total" value="#openingBal+#gameTotal+#debit-#credit"></s:set>
					<%=FormatNumber.formatNumberForJSP(pageContext
							.getAttribute("total"))%>
				</th>
				<th nowrap="nowrap" align="right">
					<s:set name="pwt" value="#orgValue.dgPwt"></s:set>
					<%=FormatNumber.formatNumberForJSP(pageContext
							.getAttribute("pwt"))%>
				</th>
				<th nowrap="nowrap" align="right">
					<s:set name="cash" value="#orgValue.cash"></s:set>
					<%=FormatNumber.formatNumberForJSP(pageContext
							.getAttribute("cash"))%>
				</th>
				<th nowrap="nowrap" align="right">
					<s:set name="bank" value="#orgValue.bankDep"></s:set>
					<%=FormatNumber.formatNumberForJSP(pageContext
							.getAttribute("bank"))%>
				</th>

				<th nowrap="nowrap" align="right">
					<s:set name="netAmt" value="#total-#pwt-#cash-#bank"></s:set>
					<%=FormatNumber.formatNumberForJSP(pageContext
							.getAttribute("netAmt"))%>
				</th>

			</tr>
		</s:iterator>
	</s:if>
	<s:else>
		<tr>
			<th align="center">
				No Records Found
			</th>
		</tr>
	</s:else>
</table>
</div>
</s:else>
<code id="headId" style="visibility: hidden">
	$RCSfile: bo_rep_toBeCollectedReportSearchForLagos.jsp,v $ $Revision:
	1.1.4.2 $
</code>