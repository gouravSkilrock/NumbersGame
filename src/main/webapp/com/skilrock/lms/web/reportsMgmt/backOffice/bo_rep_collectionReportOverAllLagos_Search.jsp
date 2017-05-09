<%@page import="com.skilrock.lms.beans.DateBeans"%>
<%@page import="com.skilrock.lms.common.utility.GetDate"%>
<%@page import="com.skilrock.lms.common.utility.FormatNumber"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<center>
<s:form action="bo_rep_CollReportOverAllLagos_ExpExcel.action">
<s:hidden id="tableValue" name="valueToSend" />
<s:submit value="Export As Excel" cssStyle="margin-right:-400px;background:#77b3d7;font-size:15px" onclick="getData();"/>
</s:form>
</center>
<div id="tableDataDiv">
<div align="center"><h3><u>Detailed <s:text name="Collection" /> <s:text name="Report"/> &nbsp;
	<%if("".equals(((DateBeans)session.getAttribute("datebean") ).getReportType())) {%>
  		 From : 
  		<%=GetDate.getConvertedDate(( (DateBeans)session.getAttribute("datebean") ).getStartDate())%>
  		&nbsp;&nbsp;To&nbsp;:&nbsp;
  		<%=GetDate.getConvertedDate(((DateBeans)session.getAttribute("datebean") ).getEndDate())%>
  		
	<%} else {out.println("Of : "+((DateBeans)session.getAttribute("datebean") ).getReportType());}%>
	</u></h3></div>	

<table width="100%" border="1" cellpadding="3" cellspacing="0"
	bordercolor="#CCCCCC" align="center" id="expandTable"
	style="font-size: 12px">
	<s:set name="DG" value="%{#session.isDG}"></s:set>
	<s:set name="SE" value="%{#session.isSE}"></s:set>
	<s:set name="CS" value="%{#session.isCS}"></s:set>
	<s:set name="OLA" value="%{#session.isOLA}"></s:set>
	<s:set name="DGCount" value="0"></s:set>
	<s:set name="SECount" value="0"></s:set>
	<s:set name="CSCount" value="0"></s:set>
	<s:set name="OLACount" value="0"></s:set>
	<s:set name="colCount" value="8" />
	<s:if test="%{#session.resultExpand!=null}">
		<s:iterator value="%{#session.gameList}">
			<s:if test="%{value=='DG'}">
				<s:set name="DGCount" value="#DGCount+1"></s:set>
			</s:if>
			<s:if test="%{value=='SE'}">
				<s:set name="SECount" value="#SECount+1"></s:set>
			</s:if>
			<s:if test="%{value=='CS'}">
				<s:set name="CSCount" value="#CSCount+1"></s:set>
			</s:if>
			<s:if test="%{value=='OLA'}">
				<s:set name="OLACount" value="#OLACount+1"></s:set>
			</s:if>
			<s:set name="colCount" value="#colCount+1"></s:set>
		</s:iterator>
				<tr>
			<th align="center" colspan="<s:property value="#colCount"/>" ><s:property value="#session.orgName" /></th>
			<th align="center">Amount in <s:property value="#application.CURRENCY_SYMBOL" /></th>
		</tr>
		<tr>
			<td align="center" colspan="<s:property value="#colCount"/>"><s:property value="#session.orgAdd" /></td>
		</tr>
		<tr align="center">
			<th rowspan="3">
				Party Name
			</th>
			<th rowspan="3">
				Opening Balance
			</th>
			<s:if test="%{#DG}">
				<th colspan="<s:property value="%{#DGCount*2}" />">
					Draw Game
				</th>
			</s:if>
			<s:if test="%{#SE}">
				<th colspan="<s:property value="%{#SECount*2}" />">
					Scratch Game
				</th>
			</s:if>
			<s:if test="%{#CS}">
				<th colspan="<s:property value="%{#CSCount*2}" />">
					Commercial Services
				</th>
			</s:if>
			<s:if test="%{#OLA}">
				<th colspan="<s:property value="%{#OLACount*3}" />">
					Offline Affiliate
				</th>
			</s:if>
			<th rowspan="3">
				Net Payments/Receipts
			</th>
			<th rowspan="3" nowrap="nowrap">
				Grand Total
			</th>
			<th rowspan="3">
				Closing Balance
			</th>
		</tr>
		<tr align="center">
		<s:if test="%{#DG}">
			<s:iterator value="%{#session.gameList}">
				<s:if test="%{value=='DG'}">
					<th colspan="2">
						<s:property value="%{key}" />
					</th>
				</s:if>
			</s:iterator>
		</s:if>
		<s:if test="%{#SE}">
			<s:iterator value="%{#session.gameList}">
				<s:if test="%{value=='SE'}">
					<th colspan="2">
						<s:property value="%{key}" />
					</th>
				</s:if>
			</s:iterator>
		</s:if>
		<s:if test="%{#CS}">
			<s:iterator value="%{#session.gameList}">
				<s:if test="%{value=='CS'}">
					<th colspan="2">
						<s:property value="%{key}" />
					</th>
				</s:if>
			</s:iterator>
		</s:if>
		<s:if test="%{#OLA}">
			<s:iterator value="%{#session.gameList}">
				<s:if test="%{value=='OLA'}">
					<th colspan="3">
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
				<th nowrap="nowrap">
					<s:text name="PWT" />
					Amount
				</th>
				</s:if>
			</s:iterator>
		</s:if>
		<s:if test="%{#SE}">
			<s:iterator value="%{#session.gameList}">
				<s:if test="%{value=='SE'}">
					<th nowrap="nowrap">
					Sale Amount
				</th>
				<th nowrap="nowrap">
					<s:text name="PWT" />
					Amount
				</th>
				</s:if>
			</s:iterator>
		</s:if>
		<s:if test="%{#CS}">
			<s:iterator value="%{#session.gameList}">
				<s:if test="%{value=='CS'}">
					<th nowrap="nowrap">
					Sale Amount
				</th>
				<th nowrap="nowrap">
					Cancel Amount
				</th>
				</s:if>
			</s:iterator>
		</s:if>
		<s:if test="%{#OLA}">
			<s:iterator value="%{#session.gameList}">
				<s:if test="%{value=='OLA'}">
					<th nowrap="nowrap">
					Deposit Amount
				</th>
				<th nowrap="nowrap">
					Withdrawal Amount
				</th>
				<th nowrap="nowrap">
					Net Gaming Amount
				</th>
				</s:if>
			</s:iterator>
		</s:if>
		</tr>
		<s:set name="openBal" value="0.0" />
		<s:set name="dgSale" value="0.0" />
		<s:set name="dgPwt" value="0.0" />
		<s:set name="seSale" value="0.0" />
		<s:set name="sePwt" value="0.0" />
		<s:set name="csSale" value="0.0" />
		<s:set name="csCancel" value="0.0" />
		<s:set name="olaWithdraw" value="0.0" />
		<s:set name="olaDeposit" value="0.0" />
		<s:set name="netGamingComm" value="0.0" />
		<s:set name="drawCash" value="0.0" />
		<s:set name="scratchCash" value="0.0" />
		<s:set name="netPayment" value="0.0" />
		<s:set name="totTotal" value="0.0" />
		<s:set name="closeBal" value="0.0" />
		<s:iterator id="result" value="#session.resultExpand">
			<s:set name="orgValue" value="%{value}"></s:set>
			<tr class="startSortable" style="white-space: nowrap;">
				<td nowrap="nowrap">
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
							<s:set name="sale"
								value="%{#gameValue.drawSale-#gameValue.drawCancel}" />
							<%=FormatNumber
									.formatNumberForJSP(pageContext
											.getAttribute("sale"))%>
						</td>
						<td align="right">
							<s:set name="pwt"
								value="%{#gameValue.drawPwt+#gameValue.drawDirPlyPwt}" />
							<%=FormatNumber
									.formatNumberForJSP(pageContext
											.getAttribute("pwt"))%>
						</td>
						<s:set name="gameTotal"
							value="%{#gameTotal+#gameValue.drawSale-#gameValue.drawCancel-#gameValue.drawPwt-#gameValue.drawDirPlyPwt}" />
					</s:if>
				</s:iterator>
				</s:if>
				<s:if test="%{#SE}">
				<s:iterator value="%{#session.gameList}">
					<s:if test="%{value=='SE'}">
						<s:set name="gameValue" value="%{#gameBeanMap[key]}" />
						<td align="right">
							<s:set name="sale"
								value="%{#gameValue.drawSale-#gameValue.drawCancel}" />
							<%=FormatNumber
									.formatNumberForJSP(pageContext
											.getAttribute("sale"))%>
						</td>
						<td align="right">
							<s:set name="pwt"
								value="%{#gameValue.drawPwt+#gameValue.drawDirPlyPwt}" />
							<%=FormatNumber
									.formatNumberForJSP(pageContext
											.getAttribute("pwt"))%>
						</td>
						<s:set name="gameTotal"
							value="%{#gameTotal+#gameValue.drawSale-#gameValue.drawCancel-#gameValue.drawPwt-#gameValue.drawDirPlyPwt}" />
					</s:if>
				</s:iterator>
				</s:if>
				<s:if test="%{#CS}">
				<s:iterator value="%{#session.gameList}">
					<s:if test="%{value=='CS'}">
						<s:set name="gameValue" value="%{#gameBeanMap[key]}" />
						<td align="right">
							<s:set name="cssale"
								value="%{#gameValue.CSSale}" />
							<%=FormatNumber
									.formatNumberForJSP(pageContext
											.getAttribute("cssale"))%>
						</td>
						<td align="right">
							<s:set name="cscancel"
								value="%{#gameValue.CSCancel}" />
							<%=FormatNumber
									.formatNumberForJSP(pageContext
											.getAttribute("cscancel"))%>
						</td>
						<s:set name="gameTotal"
							value="%{#gameTotal+#gameValue.CSSale-#gameValue.CSCancel}" />
					</s:if>
				</s:iterator>
				</s:if>
				<s:if test="%{#OLA}">
				<s:iterator value="%{#session.gameList}">
					<s:if test="%{value=='OLA'}">
						<s:set name="gameValue" value="%{#gameBeanMap[key]}" />
						<td align="right">
							<s:set name="olaDeposit"
								value="%{#gameValue.OlaDepositAmt}" />
							<%=FormatNumber
									.formatNumberForJSP(pageContext
											.getAttribute("olaDeposit"))%>
						</td>
						<td align="right">
							<s:set name="olaWithdraw"
								value="%{#gameValue.OlaWithdrawalAmt}" />
							<%=FormatNumber
									.formatNumberForJSP(pageContext
											.getAttribute("olaWithdraw"))%>
						</td>
						<td align="right">
							<s:set name="netGamingComm"
								value="%{#gameValue.OlaNetGaming}" />
							<%=FormatNumber
									.formatNumberForJSP(pageContext
											.getAttribute("netGamingComm"))%>
						</td>
						<s:set name="gameTotal"
							value="%{#gameTotal+#gameValue.OlaDepositAmt-#gameValue.OlaWithdrawalAmt-#gameValue.OlaNetGaming}" />
					</s:if>
				</s:iterator>
				</s:if>
				<td align="right">
					<s:set name="netPayment" value="%{#orgValue.netPayments}" />
					<%=FormatNumber.formatNumberForJSP(pageContext
							.getAttribute("netPayment"))%>
				</td>
				
				<th nowrap="nowrap" align="right">
					<s:set name="total" value="#gameTotal-#netPayment"></s:set>
					<%=FormatNumber.formatNumberForJSP(pageContext
							.getAttribute("total"))%>
				</th>
				<th nowrap="nowrap" align="right">
					<s:set name="agtCloseBal" value="#openingBal+#total"></s:set>
					<%=FormatNumber.formatNumberForJSP(pageContext
							.getAttribute("agtCloseBal"))%>
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
<code id="headId" style="visibility: hidden">
	$RCSfile: bo_rep_collectionReportOverAllLagos_Search.jsp,v $
	$Revision: 1.2 $
</code>