<%@page import="com.skilrock.lms.beans.DateBeans"%>
<%@page import="com.skilrock.lms.common.utility.GetDate"%>
<%@page import="com.skilrock.lms.common.utility.FormatNumber"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<div align="center"><h3><u><s:text name="Detailed" /> <s:text name="Collection" /> <s:text name="Report"/> &nbsp;
	<%if("".equals(((DateBeans)session.getAttribute("datebean") ).getReportType())) {%>
  		 <s:text name="label.from"/> : 
  		<%=GetDate.getConvertedDate(( (DateBeans)session.getAttribute("datebean") ).getStartDate())%>
  		&nbsp;&nbsp;<s:text name="label.to"/>&nbsp;:&nbsp;
  		<%=GetDate.getConvertedDate(((DateBeans)session.getAttribute("datebean") ).getEndDate())%>
  		
	<%} else {%>
	<s:text name="label.of"/> <s:text name="label.current.day"/>
	<%}%>
	</u></h3></div>	

<div id="excel"  align="center" style="display:none;" text-align: left;width: 750px;font-size: 9pt"><s:a href="bo_rep_CollReportOverAll_ExpExcel.action?orgType=BO&start_date=%{start_date}&end_Date=%{end_Date}&reportType=%{totaltime}"><s:text name="btn.exportasexcel" /></s:a></div>
<table width="100%" border="1" cellpadding="3" cellspacing="0"
	bordercolor="#CCCCCC" align="center" style="font-size: 12px">
	<s:set name="DG" value="%{#session.isDG}"></s:set>
	<s:set name="SE" value="%{#session.isSE}"></s:set>
	<s:set name="CS" value="%{#session.isCS}"></s:set>
	<s:set name="OLA" value="%{#session.isOLA}"></s:set>
	<s:set name="SLE" value="%{#session.isSLE}"></s:set>
	<s:set name="IW" value="%{#session.isIW}"></s:set>
	<s:set name="VS" value="%{#session.isVS}"></s:set>
	<s:set name="colCount" value="0" />
	<s:if test="%{#DG}">
		<s:set name="colCount" value="#colCount+2" />
	</s:if>
	<s:if test="%{#SE}">
		<s:set name="colCount" value="#colCount+2" />
	</s:if>
	<s:if test="%{#CS}">
		<s:set name="colCount" value="#colCount+2" />
	</s:if>
	<s:if test="%{#OLA}">
		<s:set name="colCount" value="#colCount+3" />
	</s:if>
	<s:if test="%{#SLE}">
		<s:set name="colCount" value="#colCount+2" />
	</s:if>
	<s:if test="%{#IW}">
		<s:set name="colCount" value="#colCount+2" />
	</s:if>
	<s:if test="%{#VS}">
		<s:set name="colCount" value="#colCount+2" />
	</s:if>
	<s:if test="%{#session.result!=null}">
		<tr>
			<th align="center" colspan="<s:property value="#colCount+4"/>" ><s:property value="#session.orgName" /></th>
			<th align="center"><s:text name="label.amt.in" /> <s:property value="#application.CURRENCY_SYMBOL" /></th>
		</tr>
		<tr>
			<td align="center" colspan="<s:property value="#colCount+5"/>">
				<s:property value="#session.orgAdd" />
			</td>
		</tr>
		<tr align="center">
			<th rowspan="3">
				<s:text name="label.party.name" />
			</th>
			<th rowspan="3">
				<s:text name="label.open.bal" />
			</th>
			<th colspan="<s:property value="#colCount+1"/>">
				<s:a
					href="bo_rep_collectionReportOverAll_SearchExpand.action?start_date=%{start_date}&end_Date=%{end_Date}"
					theme="ajax" targets="result" notifyTopics="calcTotal()"><s:text name="label.expand" />
				</s:a>
			</th>
			<th rowspan="3" nowrap="nowrap">
				<s:text name="label.grand.total" />
			</th>
			<th rowspan="3" nowrap="nowrap">
				<s:text name="label.close.bal" />
			</th>
		</tr>
		<tr align="center">
			<s:if test="%{#DG}">
				<th colspan="2">
					<s:text name="label.draw.game" />
				</th>
			</s:if>
			<s:if test="%{#SE}">
				<th colspan="2">
					<s:text name="label.scratch.game" />
				</th>
			</s:if>
			<s:if test="%{#CS}">
				<th colspan="2">
					<s:text name="label.comm.serv" />
				</th>
			</s:if>
			<s:if test="%{#OLA}">
				<th colspan="3">
					<s:text name="label.offline.aff" />
				</th>
			</s:if>
			<s:if test="%{#SLE}">
				<th colspan="2">
					<s:text name="label.sport.lot" />
				</th>
			</s:if>
			<s:if test="%{#IW}">
				<th colspan="2">
					<s:text name="label.instant.win" />
				</th>
			</s:if>
			<s:if test="%{#VS}">
				<th colspan="2">
					<s:text name="label.virtual.sport" />
				</th>
			</s:if>
			<th rowspan="2">
				<s:text name="label.net.pay.rec" />
			</th>
		</tr>
		<tr align="center">
			<%-- <s:if test="%{#DG&&#SE}">
				<th nowrap="nowrap">
					<s:text name="label.sale.amt" />
				</th>
				<th nowrap="nowrap">
					<s:text name="PWT"/> <s:text name="label.amount" />
				</th>
			</s:if> --%>
			<s:if test="%{#DG}">
				<th nowrap="nowrap">
				<s:text name="label.sale.amt" />
			    </th>
			<th nowrap="nowrap">
				<s:text name="PWT"/> <s:text name="label.amount" />
			</th>
			</s:if>
			<s:if test="%{#SE}">
				<th nowrap="nowrap">
				<s:text name="label.sale.amt" />
			    </th>
			<th nowrap="nowrap">
				<s:text name="PWT"/> <s:text name="label.amount" />
			</th>
			</s:if>
			<s:if test="%{#CS}">
				<th nowrap="nowrap">
					<s:text name="label.sale.amt" />
				</th>
				<th nowrap="nowrap">
					<s:text name="label.cancel.amt" />
				</th>
			</s:if>
			<s:if test="%{#OLA}">
				<th nowrap="nowrap">
					<s:text name="label.withdrawal" />
				</th>
				<th nowrap="nowrap">
					<s:text name="label.deposit" />
				</th>
				<th nowrap="nowrap">
					<s:text name="label.net.gaming" />
				</th>
			</s:if>
			<s:if test="%{#SLE}">
				<th nowrap="nowrap">
					<s:text name="label.sale.amt" />
				</th>
				<th nowrap="nowrap">
					<s:text name="PWT" /> <s:text name="label.amount" />
				</th>
			</s:if>
			<s:if test="%{#IW}">
				<th nowrap="nowrap">
					<s:text name="label.sale.amt" />
				</th>
				<th nowrap="nowrap">
					<s:text name="PWT" /> <s:text name="label.amount" />
				</th>
			</s:if>
			<s:if test="%{#VS}">
				<th nowrap="nowrap">
					<s:text name="label.sale.amt" />
				</th>
				<th nowrap="nowrap">
					<s:text name="PWT" /> <s:text name="label.amount" />
				</th>
			</s:if>
		</tr>
		<s:set name="openBal" value="0.0" />
		<s:set name="dgSale" value="0.0" />
		<s:set name="dgPwt" value="0.0" />
		<s:set name="seSale" value="0.0" />
		<s:set name="sePwt" value="0.0" />
		<s:set name="csSaleAmt" value="0.0" />
		<s:set name="csCancelAmt" value="0.0" />
		<s:set name="olaDepoAmt" value="0.0"></s:set>
		<s:set name="olaWdAmt" value="0.0"></s:set>
		<s:set name="netGamingComm2" value="0.0"></s:set>
		<s:set name="drawCash" value="0.0" />
		<s:set name="sleCash" value="0.0" />
		<s:set name="scratchCash" value="0.0" />
		<s:set name="netPayment" value="0.0" />
		<s:set name="csSale" value="0.0" />
		<s:set name="csCancel" value="0.0" />
		<s:set name="olaWithdraw" value="0.0" />
		<s:set name="olaDeposit" value="0.0" />
		<s:set name="netGamingComm" value="0.0" />
		<s:set name="slSale" value="0.0" />
		<s:set name="slPwt" value="0.0" />
		<s:set name="totTotal" value="0.0" />
		<s:set name="closeBal" value="0.0" />
		<s:set name="iwSale" value="0.0" />
		<s:set name="iwPwt" value="0.0" />
		<s:set name="iwCash" value="0.0" />
		<s:set name="vsSale" value="0.0" />
		<s:set name="vsPwt" value="0.0" />
		<s:set name="vsCash" value="0.0" />
		<s:iterator id="result" value="#session.result">
			<s:set name="orgValue" value="%{value}"></s:set>
			<tr class="startSortable">
				<td nowrap="nowrap">
					<s:property value="%{#orgValue.agentName}" />
				</td>
				<td align="right">
					<s:set name="openingBal" value="%{#orgValue.openingBal}" />
					<%=FormatNumber.formatNumberForJSP(pageContext
							.getAttribute("openingBal"))%>
				</td>
				<s:if test="%{#DG}">
					<td align="right" noWrap="nowrap">
						<s:set name="drawSale"
							value="%{#orgValue.dgSale-#orgValue.dgCancel}" />
						<%=FormatNumber.formatNumberForJSP(pageContext
								.getAttribute("drawSale"))%>
					</td>
					<td align="right" noWrap="nowrap">
						<s:set name="drawPwt"
							value="%{#orgValue.dgPwt+#orgValue.dgDirPlyPwt}" />
						<%=FormatNumber.formatNumberForJSP(pageContext
								.getAttribute("drawPwt"))%>
					</td>
					<s:set name="drawCash"
						value="%{#orgValue.dgSale-#orgValue.dgCancel-#orgValue.dgPwt-#orgValue.dgDirPlyPwt}"></s:set>
				</s:if>
				<s:if test="%{#SE}">
					<td align="right" noWrap="nowrap">
						<s:set name="scratchSale" value="%{#orgValue.seSale}" />
						<%=FormatNumber.formatNumberForJSP(pageContext
								.getAttribute("scratchSale"))%>
					</td>
					<td align="right" noWrap="nowrap">
						<s:set name="scratchPwt"
							value="%{#orgValue.sePwt+#orgValue.seDirPlyPwt}" />
						<%=FormatNumber.formatNumberForJSP(pageContext
								.getAttribute("scratchPwt"))%>
					</td>
					<s:set name="scratchCash"
						value="%{#orgValue.seSale-#orgValue.sePwt-#orgValue.seDirPlyPwt}"></s:set>
				</s:if>
				<s:if test="%{#CS}">
					<td align="right" noWrap="nowrap">
						<s:set name="csSaleAmt" value="%{#orgValue.CSSale}" />
						<%=FormatNumber.formatNumberForJSP(pageContext
								.getAttribute("csSaleAmt"))%>
					</td>
					<td align="right" noWrap="nowrap">
						<s:set name="csCancelAmt" value="%{#orgValue.CSCancel}" />
						<%=FormatNumber.formatNumberForJSP(pageContext
								.getAttribute("csCancelAmt"))%>
					</td>
				</s:if>
				<s:if test="%{#OLA}">
					<td align="right" noWrap="nowrap">
						<s:set name="olaWdAmt"
							value="%{#orgValue.withdrawal - #orgValue.withdrawalRefund}" />
						<%=FormatNumber.formatNumberForJSP(pageContext
								.getAttribute("olaWdAmt"))%>
					</td>
					<td align="right" noWrap="nowrap">
						<s:set name="olaDepoAmt"
							value="%{#orgValue.deposit - #orgValue.depositRefund}" />
						<%=FormatNumber.formatNumberForJSP(pageContext
								.getAttribute("olaDepoAmt"))%>
					</td>
					<td align="right" noWrap="nowrap">
						<s:set name="netGamingComm2" value="%{#orgValue.netGamingComm}" />
						<%=FormatNumber.formatNumberForJSP(pageContext
								.getAttribute("netGamingComm2"))%>
					</td>
				</s:if>
				<s:if test="%{#SLE}">
					<td align="right" noWrap="nowrap">
						<s:set name="sleNetSale"
							value="%{#orgValue.sleSale-#orgValue.sleCancel}" />
						<%=FormatNumber.formatNumberForJSP(pageContext
								.getAttribute("sleNetSale"))%>
					</td>
					<td align="right" noWrap="nowrap">
						<s:set name="sleNetPwt"
							value="%{#orgValue.slePwt+#orgValue.sleDirPlyPwt}" />
						<%=FormatNumber.formatNumberForJSP(pageContext
								.getAttribute("sleNetPwt"))%>
					</td>
					<s:set name="sleCash"
						value="%{#orgValue.sleSale-#orgValue.sleCancel-#orgValue.slePwt-#orgValue.sleDirPlyPwt}"></s:set>
				</s:if>
				<s:if test="%{#IW}">
					<td align="right" noWrap="nowrap">
						<s:set name="iwNetSale" value="%{#orgValue.iwSale-#orgValue.iwCancel}" />
						<%=FormatNumber.formatNumberForJSP(pageContext.getAttribute("iwNetSale"))%>
					</td>
					<td align="right" noWrap="nowrap">
						<s:set name="iwNetPwt" value="%{#orgValue.iwPwt+#orgValue.iwDirPlyPwt}" />
						<%=FormatNumber.formatNumberForJSP(pageContext.getAttribute("iwNetPwt"))%>
					</td>
					<s:set name="iwCash"
						value="%{#orgValue.iwSale-#orgValue.iwCancel-#orgValue.iwPwt-#orgValue.iwDirPlyPwt}"></s:set>
				</s:if>
				<s:if test="%{#VS}">
					<td align="right" noWrap="nowrap">
						<s:set name="vsNetSale" value="%{#orgValue.vsSale-#orgValue.vsCancel}" />
						<%=FormatNumber.formatNumberForJSP(pageContext.getAttribute("vsNetSale"))%>
					</td>
					<td align="right" noWrap="nowrap">
						<s:set name="vsNetPwt" value="%{#orgValue.vsPwt+#orgValue.vsDirPlyPwt}" />
						<%=FormatNumber.formatNumberForJSP(pageContext.getAttribute("vsNetPwt"))%>
					</td>
					<s:set name="vsCash"
						value="%{#orgValue.vsSale-#orgValue.vsCancel-#orgValue.vsPwt-#orgValue.vsDirPlyPwt}"></s:set>
				</s:if>
				<td align="right" noWrap="nowrap">
					<s:set name="agtNetPayment"
						value="%{#orgValue.cash+#orgValue.cheque-#orgValue.chequeReturn-#orgValue.debit+#orgValue.credit+#orgValue.bankDep}" />
					<%=FormatNumber.formatNumberForJSP(pageContext
							.getAttribute("agtNetPayment"))%>
				</td>
				<th align="right" noWrap="nowrap">
					<s:set name="total"
						value="#drawCash+#scratchCash-#agtNetPayment+#csSaleAmt-#csCancelAmt+#olaDepoAmt-#olaWdAmt-#netGamingComm2+#sleCash+#iwCash+#vsCash"></s:set>
					<%=FormatNumber.formatNumberForJSP(pageContext
							.getAttribute("total"))%>
				</th>
				<th align="right" noWrap="nowrap">
					<s:set name="agtCloseBal" value="#openingBal+#total"></s:set>
					<%=FormatNumber.formatNumberForJSP(pageContext
							.getAttribute("agtCloseBal"))%>
				</th>
				<s:set name="openBal" value="%{#openBal+#openingBal}" />
				<s:set name="dgSale"
					value="%{#dgSale+#orgValue.dgSale-#orgValue.dgCancel}" />
				<s:set name="dgPwt"
					value="%{#dgPwt+#orgValue.dgPwt+#orgValue.dgDirPlyPwt}" />
				<s:set name="seSale" value="%{#seSale+#orgValue.seSale}" />
				<s:set name="sePwt"
					value="%{#sePwt+#orgValue.sePwt+#orgValue.seDirPlyPwt}" />
				<s:set name="csSale" value="%{#csSale+#orgValue.CSSale}" />
				<s:set name="csCancel" value="%{#csCancel+#orgValue.CSCancel}" />
				<s:set name="olaWithdraw"
					value="%{#olaWithdraw+#orgValue.withdrawal-#orgValue.withdrawalRefund}" />
				<s:set name="olaDeposit"
					value="%{#olaDeposit+#orgValue.deposit-#orgValue.depositRefund}" />
				<s:set name="netGamingComm"
					value="%{#netGamingComm+#orgValue.netGamingComm}" />

				<s:set name="slSale"
					value="%{#slSale+#orgValue.sleSale-#orgValue.sleCancel}" />
				<s:set name="slPwt"
					value="%{#slPwt+#orgValue.slePwt+#orgValue.sleDirPlyPwt}" />

				<s:set name="iwSale"
					value="%{#iwSale+#orgValue.iwSale-#orgValue.iwCancel}" />
				<s:set name="iwPwt"
					value="%{#iwPwt+#orgValue.iwPwt+#orgValue.iwDirPlyPwt}" />
					
				<s:set name="vsSale"
					value="%{#vsSale+#orgValue.vsSale-#orgValue.vsCancel}" />
				<s:set name="vsPwt"
					value="%{#vsPwt+#orgValue.vsPwt+#orgValue.vsDirPlyPwt}" />

				<s:set name="netPayment"
					value="%{#netPayment+#orgValue.cash+#orgValue.cheque-#orgValue.chequeReturn-#orgValue.debit+#orgValue.credit+#orgValue.bankDep}" />

				<s:set name="totTotal"
					value="%{#totTotal+#drawCash+#scratchCash-#agtNetPayment+#csSaleAmt-#csCancelAmt+#olaDepoAmt-#olaWdAmt-#netGamingComm2+#sleCash+#iwCash+#vsCash}" />
				<s:set name="closeBal" value="%{#closeBal+#openingBal+#total}" />
			</tr>
		</s:iterator>
		<tr>
			<th>
				<s:text name="label.total" />
			</th>
			<th align="right" noWrap="nowrap">
				<%=FormatNumber.formatNumberForJSP(pageContext
						.getAttribute("openBal"))%>
			</th>
			<s:if test="%{#DG}">
				<th align="right" noWrap="nowrap">
					<%=FormatNumber.formatNumberForJSP(pageContext
							.getAttribute("dgSale"))%>
				</th>
				<th align="right" noWrap="nowrap">
					<%=FormatNumber.formatNumberForJSP(pageContext
							.getAttribute("dgPwt"))%>
				</th>
			</s:if>
			<s:if test="%{#SE}">
				<th align="right" noWrap="nowrap">
					<%=FormatNumber.formatNumberForJSP(pageContext
							.getAttribute("seSale"))%>
				</th>
				<th align="right" noWrap="nowrap">
					<%=FormatNumber.formatNumberForJSP(pageContext
							.getAttribute("sePwt"))%>
				</th>
			</s:if>
			<s:if test="%{#CS}">
				<th align="right" noWrap="nowrap">
					<%=FormatNumber.formatNumberForJSP(pageContext
							.getAttribute("csSale"))%>
				</th>
				<th align="right" noWrap="nowrap">
					<%=FormatNumber.formatNumberForJSP(pageContext
							.getAttribute("csCancel"))%>
				</th>
			</s:if>
			<s:if test="%{#OLA}">
				<th align="right" noWrap="nowrap">
					<%=FormatNumber.formatNumberForJSP(pageContext
							.getAttribute("olaWithdraw"))%>
				</th>
				<th align="right" noWrap="nowrap">
					<%=FormatNumber.formatNumberForJSP(pageContext
							.getAttribute("olaDeposit"))%>
				</th>
				<th align="right" noWrap="nowrap">
					<%=FormatNumber.formatNumberForJSP(pageContext
							.getAttribute("netGamingComm"))%>
				</th>
			</s:if>
			<s:if test="%{#SLE}">
				<th align="right" noWrap="nowrap">
					<%=FormatNumber.formatNumberForJSP(pageContext
							.getAttribute("slSale"))%>
				</th>
				<th align="right" noWrap="nowrap">
					<%=FormatNumber.formatNumberForJSP(pageContext
							.getAttribute("slPwt"))%>
				</th>
			</s:if>
			<s:if test="%{#IW}">
				<th align="right" noWrap="nowrap">
					<%=FormatNumber.formatNumberForJSP(pageContext.getAttribute("iwSale"))%>
				</th>
				<th align="right" noWrap="nowrap">
					<%=FormatNumber.formatNumberForJSP(pageContext.getAttribute("iwPwt"))%>
				</th>
			</s:if>
			<s:if test="%{#VS}">
				<th align="right" noWrap="nowrap">
					<%=FormatNumber.formatNumberForJSP(pageContext.getAttribute("vsSale"))%>
				</th>
				<th align="right" noWrap="nowrap">
					<%=FormatNumber.formatNumberForJSP(pageContext.getAttribute("vsPwt"))%>
				</th>
			</s:if>
			<th align="right" noWrap="nowrap">
				<%=FormatNumber.formatNumberForJSP(pageContext
						.getAttribute("netPayment"))%>
			</th>
			<th align="right" noWrap="nowrap">
				<%=FormatNumber.formatNumberForJSP(pageContext
						.getAttribute("totTotal"))%>
			</th>
			<th align="right" noWrap="nowrap">
				<%=FormatNumber.formatNumberForJSP(pageContext
						.getAttribute("closeBal"))%>
			</th>
		</tr>
	</s:if>
	<s:else>
		<tr>
			<th align="center">
				<s:text name="msg.no.record" />
			</th>
		</tr>
	</s:else>
</table>

<code id="headId" style="visibility: hidden">
	$RCSfile: bo_rep_collectionReportOverAll_Search.jsp,v $ $Revision:
	1.1.2.18 $
</code>