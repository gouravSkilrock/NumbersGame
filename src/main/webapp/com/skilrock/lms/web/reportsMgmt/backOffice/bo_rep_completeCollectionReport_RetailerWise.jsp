
<%@page import="com.skilrock.lms.common.utility.FormatNumber"%>
<%@page import="org.apache.struts2.components.Form"%><%@ taglib prefix="s" uri="/struts-tags"%>
<div align="center">
	<h3>
		<u><s:text name="Collection"/> <s:text name="Report"/> <s:text name="label.for"/> <s:property value="%{agtOrgName}" /> </u>
	</h3>
</div>
<s:set name="DG" value="%{#session.isDG}"></s:set>
<s:set name="SE" value="%{#session.isSE}"></s:set>
<s:set name="CS" value="%{#session.isCS}"></s:set>
<s:set name="OLA" value="%{#session.isOLA}"></s:set>
<s:set name="SLE" value="%{#session.isSLE}"></s:set>
<s:set name="IW" value="%{#session.isIW}"></s:set>
<s:set name="VS" value="%{#session.isVS}"></s:set>
<s:set name="colCountForRet" value="2" />
<s:set name="colCount" value="2" />
	<s:if test="%{#DG&&#SE}">
		<s:set name="colCount" value="3" />
		<s:set name="colCountForRet" value="4" />
	</s:if>
	<s:if test="%{#DG&&#SLE}">
		<s:set name="colCount" value="3" />
		<s:set name="colCountForRet" value="4" />
	</s:if>
	<s:if test="%{#DG&&#IW}">
		<s:set name="colCount" value="3" />
		<s:set name="colCountForRet" value="4" />
	</s:if>
	<s:if test="%{#DG&&#VS}">
		<s:set name="colCount" value="3" />
		<s:set name="colCountForRet" value="4" />
	</s:if>
	<s:if test="%{#DG&&#SE&#CS}">
		<s:set name="colCount" value="4" />
		<s:set name="colCountForRet" value="5" />
	</s:if>
	<s:if test="%{#DG&&#SE&#SLE}">
		<s:set name="colCount" value="5" />
		<s:set name="colCountForRet" value="6" />
	</s:if>
	<s:if test="%{#DG&&#SE&#IW}">
		<s:set name="colCount" value="5" />
		<s:set name="colCountForRet" value="6" />
	</s:if>
	<s:if test="%{#DG&&#SE&#VS}">
		<s:set name="colCount" value="5" />
		<s:set name="colCountForRet" value="6" />
	</s:if>
	<s:if test="%{#DG&&#SE&&#CS&&#OLA}">
	<s:set name="colCount" value="7"></s:set>
		<s:set name="colCountForRet" value="8" />
	</s:if>
	<s:if test="%{#DG&&#SE&&#CS&&#OLA&&#SLE}">
	<s:set name="colCount" value="9"></s:set>
		<s:set name="colCountForRet" value="10" />
	</s:if>
	<s:if test="%{#DG&&#SE&&#CS&&#OLA&&#IW}">
	<s:set name="colCount" value="9"></s:set>
		<s:set name="colCountForRet" value="10" />
	</s:if>
	<s:if test="%{#DG&&#SE&&#CS&&#OLA&&#VS}">
	<s:set name="colCount" value="9"></s:set>
		<s:set name="colCountForRet" value="10" />
	</s:if>
<s:if test="%{reportType!='Agent Wise'}">
<table width="100%" border="1" cellpadding="3" cellspacing="0"
	bordercolor="#CCCCCC" align="center" style="font-size: 12px">
	<tr align="center">
		<th colspan="<s:property value="#colCount"/>" >
			<s:text name="Report"/> <s:text name="label.for"/> <s:text name="PWT"/> <s:text name="label.by.plr"/>
		</th>
	</tr>
	<tr align="center">
		<th>
			<s:text name="label.org.name" />
		</th>
		<s:if test="%{#DG}">
			<th>
				<s:text name="label.draw.game" />
			</th>
		</s:if>
		<s:if test="%{#SE}">
			<th>
				<s:text name="label.scratch.game" />
			</th>
		</s:if>
		<s:if test="%{#SLE}">
			<th>
				<s:text name="label.sport.lot" />
			</th>
		</s:if>
		<s:if test="%{#IW}">
			<th>
				<s:text name="IW" />
			</th>
		</s:if>
		<s:if test="%{#VS}">
			<th>
				<s:text name="VS" />
			</th>
		</s:if>
	</tr>

	<tr>
		<td>
			<s:property value="%{agtOrgName}" />
		</td>
		<s:if test="%{#DG}">
			<td align="right">
				<s:property value="%{#session.drawDirPlyPwt}" />
			</td>
		</s:if>
		<s:if test="%{#SE}">
			<td align="right">
				<s:property value="%{#session.scratchDirPlyPwt}" />
			</td>
		</s:if>
		<s:if test="%{#SLE}">
			<td align="right">
				<s:property value="%{#session.sleDirPlyPwt}" />
			</td>
		</s:if>
		<s:if test="%{#IW}">
			<td align="right">
				<s:property value="%{#session.iwDirPlyPwt}" />
			</td>
		</s:if>
		<s:if test="%{#VS}">
			<td align="right">
				<s:property value="%{#session.vsDirPlyPwt}" />
			</td>
		</s:if>
	</tr>
</table>
<br />
</s:if>
<table width="100%" border="1" cellpadding="3" cellspacing="0"
	bordercolor="#CCCCCC" align="center" style="font-size: 12px">
	<tr>
		<th align="center" colspan="<s:property value="#colCountForRet+1"/>" ><s:property value="#session.AgtOrgName" /></th>
		<th align="center">Amount in <s:property value="#application.CURRENCY_SYMBOL" /></th>
	</tr>
	<tr>
		<td align="center" colspan="<s:property value="#colCountForRet+2"/>"><s:property value="#session.AgtOrgAdd" /></td>
	</tr>
	<s:if test="%{#session.result_retailer!=null&& #session.result_retailer.size>0}">
		<tr align="center">
			<th rowspan="2">
				<s:text name="label.org.name" />
			</th>
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
				<th colspan="1">
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
					<s:text name="IW" />
				</th>
			</s:if>
			<s:if test="%{#VS}">
				<th colspan="2">
					<s:text name="VS" />
				</th>
			</s:if>
			<th rowspan="2">
				<s:text name="label.net.amt" />
			</th>
		</tr>
		<tr align="center">
			<s:if test="%{#DG}">
				<th>
					<s:text name="label.sale.amt" />
				</th>
				<th>
					<s:text name="PWT"/> <s:text name="label.amount" />
				</th>
			</s:if>
			<s:if test="%{#SE}">
				<th>
					<s:text name="label.sale.amt" />
				</th>
				<th>
					<s:text name="PWT"/> <s:text name="label.amount" />
				</th>
			</s:if>
			<s:if test="%{#CS}">
				<th>
					<s:text name="label.sale.amt" />
				</th>
			</s:if>
			<s:if test="%{#OLA}">
			<th>
				<s:text name="label.depo.amt" />		
			</th>
			<th>
				<s:text name="label.wdl.amt" />
			</th>
			<th>
				<s:text name="label.net.gaming.amt" />
			</th>
			</s:if>
			<s:if test="%{#SLE}">
				<th>
					<s:text name="label.sale.amt" />
				</th>
				<th>
					<s:text name="PWT"/> <s:text name="label.amount" />
				</th>
			</s:if>
			<s:if test="%{#IW}">
				<th>
					<s:text name="label.sale.amt" />
				</th>
				<th>
					<s:text name="PWT"/> <s:text name="label.amount" />
				</th>
			</s:if>
			<s:if test="%{#VS}">
				<th>
					<s:text name="label.sale.amt" />
				</th>
				<th>
					<s:text name="PWT"/> <s:text name="label.amount" />
				</th>
			</s:if>
		</tr>

		<s:set name="dgSale" value="0.0" />
		<s:set name="dgPwt" value="0.0" />
		<s:set name="seSale" value="0.0" />
		<s:set name="sePwt" value="0.0" />
		<s:set name="csSale" value="0.0" />
		<s:set name="olaDeposit" value="0.0"></s:set>
		<s:set name="olaWithdrawal" value="0.0"></s:set>
		<s:set name="olaNetGaming" value="0.0"></s:set>
		<s:set name="slSale" value="0.0" />
		<s:set name="slPwt" value="0.0" />
		<s:set name="iwSale" value="0.0" />
		<s:set name="iwPwt" value="0.0" />
		<s:set name="vsSale" value="0.0" />
		<s:set name="vsPwt" value="0.0" />
		<s:set name="totCash" value="0.0" />
		<s:set name="drawCash" value="0.0" />
		<s:set name="scratchCash" value="0.0" />
		<s:set name="csCash" value="0.0" />
		<s:set name="olaCash" value="0.0"></s:set>
		<s:set name="sleCash" value="0.0" />
		<s:set name="iwCash" value="0.0" />
		<s:set name="vsCash" value="0.0" />
		<s:iterator id="result" value="#session.result_retailer">
			<s:set name="orgValue" value="%{value}"></s:set>
			<tr class="startSortable">
				<td>
					<s:property value="%{#orgValue.orgName}" />
				</td>
				<s:if test="%{#DG}">
					<td align="right">
						<s:set name="drawSale" value="%{#orgValue.drawSale-#orgValue.drawCancel}" />
						<%=FormatNumber.formatNumberForJSP(pageContext.getAttribute("drawSale"))%>
					</td>
					<td align="right">
						<s:set name="drawPwt" value="%{#orgValue.drawPwt+#orgValue.drawDirPlyPwt}" />
						<%=FormatNumber.formatNumberForJSP(pageContext.getAttribute("drawPwt"))%>
					</td>
					<s:set name="drawCash"
						value="%{#orgValue.drawSale-#orgValue.drawCancel-#orgValue.drawPwt-#orgValue.drawDirPlyPwt}"></s:set>
				</s:if>
				<s:if test="%{#SE}">
					<td align="right">
						<s:set name="scratchSale" value="%{#orgValue.scratchSale}" />
						<%=FormatNumber.formatNumberForJSP(pageContext.getAttribute("scratchSale"))%>
					</td>
					<td align="right">
						<s:set name="scratchPwt" value="%{#orgValue.scratchPwt+#orgValue.scratchDirPlyPwt}" />
						<%=FormatNumber.formatNumberForJSP(pageContext.getAttribute("scratchPwt"))%>
					</td>
					<s:set name="scratchCash"
						value="%{#orgValue.scratchSale-#orgValue.scratchPwt-#orgValue.scratchDirPlyPwt}"></s:set>
				</s:if>
				<s:if test="%{#CS}">
					<td align="right">
						<s:set name="commServSale" value="%{#orgValue.CSSale-#orgValue.CSCancel}" />
						<%=FormatNumber.formatNumberForJSP(pageContext.getAttribute("commServSale"))%>
					</td>
					<s:set name="csCash"
						value="%{#orgValue.CSSale-#orgValue.CSCancel}"></s:set>
				</s:if>
				<s:if test="%{#OLA}">
					<td align="right">
						<s:set name="olaDeposit1" value="%{#orgValue.OlaDepositAmt-#orgValue.OlaDepositCancelAmt}" />
						<%=FormatNumber.formatNumberForJSP(pageContext.getAttribute("olaDeposit1"))%>
					</td>
					<td align="right">
						<s:set name="olaWithdrawal1" value="%{#orgValue.OlaWithdrawalAmt}" />
						<%=FormatNumber.formatNumberForJSP(pageContext.getAttribute("olaWithdrawal1"))%>
					</td>
					<td align="right">
						<s:set name="olaNetGaming1" value="%{#orgValue.OlaNetGaming}" />
						<%=FormatNumber.formatNumberForJSP(pageContext.getAttribute("olaNetGaming1"))%>
					</td>
					<s:set name="olaCash"
						value="%{#orgValue.OlaDepositAmt-#orgValue.OlaDepositCancelAmt-#orgValue.OlaWithdrawalAmt-#orgValue.OlaNetGaming}"></s:set>
				</s:if>
				<s:if test="%{#SLE}">
					<td align="right">
						<s:set name="sleSale" value="%{#orgValue.sleSale-#orgValue.sleCancel}" />
						<%=FormatNumber.formatNumberForJSP(pageContext.getAttribute("sleSale"))%>
					</td>
					<td align="right">
						<s:set name="slePwt" value="%{#orgValue.slePwt+#orgValue.sleDirPlyPwt}" />
						<%=FormatNumber.formatNumberForJSP(pageContext.getAttribute("slePwt"))%>
					</td>
					<s:set name="sleCash"
						value="%{#orgValue.sleSale-#orgValue.sleCancel-#orgValue.slePwt-#orgValue.sleDirPlyPwt}"></s:set>
				</s:if>
				<s:if test="%{#IW}">
					<td align="right">
						<s:set name="IWSale" value="%{#orgValue.iwSale-#orgValue.iwCancel}" />
						<%=FormatNumber.formatNumberForJSP(pageContext.getAttribute("IWSale"))%>
					</td>
					<td align="right">
						<s:set name="IWPwt" value="%{#orgValue.iwPwt+#orgValue.iwDirPlyPwt}" />
						<%=FormatNumber.formatNumberForJSP(pageContext.getAttribute("IWPwt"))%>
					</td>
					<s:set name="iwCash"
						value="%{#orgValue.iwSale-#orgValue.iwCancel-#orgValue.iwPwt-#orgValue.iwDirPlyPwt}"></s:set>
				</s:if>
				<s:if test="%{#VS}">
					<td align="right">
						<s:set name="VSSale" value="%{#orgValue.vsSale-#orgValue.vsCancel}" />
						<%=FormatNumber.formatNumberForJSP(pageContext.getAttribute("VSSale"))%>
					</td>
					<td align="right">
						<s:set name="VSPwt" value="%{#orgValue.vsPwt+#orgValue.vsDirPlyPwt}" />
						<%=FormatNumber.formatNumberForJSP(pageContext.getAttribute("VSPwt"))%>
					</td>
					<s:set name="vsCash"
						value="%{#orgValue.vsSale-#orgValue.vsCancel-#orgValue.vsPwt-#orgValue.vsDirPlyPwt}"></s:set>
				</s:if>
				<th align="right">
					<s:set name="cash" value="#drawCash+#scratchCash+#csCash+#olaCash+#sleCash+#iwCash+#vsCash"></s:set>
					<%=FormatNumber.formatNumberForJSP(pageContext.getAttribute("cash"))%>

				</th>
				<s:set name="dgSale" value="%{#dgSale+#orgValue.drawSale-#orgValue.drawCancel}" />
				<s:set name="dgPwt" value="%{#dgPwt+#orgValue.drawPwt+#orgValue.drawDirPlyPwt}" />
				<s:set name="seSale" value="%{#seSale+#orgValue.scratchSale}" />
				<s:set name="sePwt" value="%{#sePwt+#orgValue.scratchPwt+#orgValue.scratchDirPlyPwt}" />
				<s:set name="csSale" value="%{#csSale+#orgValue.CSSale-#orgValue.CSCancel}" />
				<s:set name="olaDeposit" value="%{#olaDeposit+#orgValue.OlaDepositAmt-#orgValue.OlaDepositCancelAmt}"></s:set>
				<s:set name="olaWithdrawal" value="%{#olaWithdrawal+#orgValue.OlaWithdrawalAmt}"></s:set>
				<s:set name="olaNetGaming" value="%{#olaNetGaming+#orgValue.OlaNetGaming}"></s:set>
				<s:set name="slSale" value="%{#slSale+#orgValue.sleSale-#orgValue.sleCancel}" />
				<s:set name="iwSale" value="%{#iwSale+#orgValue.iwSale-#orgValue.iwCancel}" />
				<s:set name="vsSale" value="%{#vsSale+#orgValue.vsSale-#orgValue.vsCancel}" />
				<s:set name="slPwt" value="%{#slPwt+#orgValue.slePwt+#orgValue.sleDirPlyPwt}" />
				<s:set name="iwPwt" value="%{#iwPwt+#orgValue.iwPwt+#orgValue.iwDirPlyPwt}" />
				<s:set name="vsPwt" value="%{#vsPwt+#orgValue.vsPwt+#orgValue.vsDirPlyPwt}" />
				<s:set name="totCash" value="%{#totCash+#cash}" />
			</tr>
		</s:iterator>
		<tr>
			<th>
				<s:text name="label.total" />
			</th>
			<s:if test="%{#DG}">
				<th align="right">
					<%=FormatNumber.formatNumberForJSP(pageContext.getAttribute("dgSale"))%>
				</th>
				<th align="right">
					<%=FormatNumber.formatNumberForJSP(pageContext.getAttribute("dgPwt"))%>
				</th>
			</s:if>
			<s:if test="%{#SE}">
				<th align="right">
					<%=FormatNumber.formatNumberForJSP(pageContext.getAttribute("seSale"))%>
				</th>
				<th align="right">
					<%=FormatNumber.formatNumberForJSP(pageContext.getAttribute("sePwt"))%>
				</th>
			</s:if>
			<s:if test="%{#CS}">
				<th align="right">
					<%=FormatNumber.formatNumberForJSP(pageContext.getAttribute("csSale"))%>
				</th>
			</s:if>
			<s:if test="%{#OLA}">
			<th align="right">
				<%=FormatNumber.formatNumberForJSP(pageContext.getAttribute("olaDeposit")) %>
			</th>
			<th align="right">
				<%=FormatNumber.formatNumberForJSP(pageContext.getAttribute("olaWithdrawal")) %>
			</th>
			<th align="right">
				<%=FormatNumber.formatNumberForJSP(pageContext.getAttribute("olaNetGaming")) %>
			</th>
			</s:if>
			<s:if test="%{#SLE}">
				<th align="right">
					<%=FormatNumber.formatNumberForJSP(pageContext.getAttribute("slSale"))%>
				</th>
				<th align="right">
					<%=FormatNumber.formatNumberForJSP(pageContext.getAttribute("slPwt"))%>
				</th>
			</s:if>
			<s:if test="%{#IW}">
				<th align="right">
					<%=FormatNumber.formatNumberForJSP(pageContext.getAttribute("iwSale"))%>
				</th>
				<th align="right">
					<%=FormatNumber.formatNumberForJSP(pageContext.getAttribute("iwPwt"))%>
				</th>
			</s:if>
			<s:if test="%{#VS}">
				<th align="right">
					<%=FormatNumber.formatNumberForJSP(pageContext.getAttribute("vsSale"))%>
				</th>
				<th align="right">
					<%=FormatNumber.formatNumberForJSP(pageContext.getAttribute("vsPwt"))%>
				</th>
			</s:if>
			<th align="right">
				<%=FormatNumber.formatNumberForJSP(pageContext.getAttribute("totCash"))%>
			</th>
		</tr>
		<s:if test="%{reportType!='Agent Wise'}">
		<s:set name="agentId" value="%{agtId}" />
		<s:iterator value="%{#session.result}">
			<s:if test="%{key==#agentId}">
			<s:set name="agtValue" value="%{value}"/>
			<tr>
			<th>
				<s:property value="#application.TIER_MAP.AGENT" /> <s:text name="label.total" />
			</th>
			<s:if test="%{#DG}">
				<th align="right">
					<s:set name="agtDGSale" value="%{#agtValue.drawSale-#agtValue.drawCancel}" />
					<%=FormatNumber.formatNumberForJSP(pageContext.getAttribute("agtDGSale"))%>
				</th>
				<th align="right">
					<s:set name="agtDGPwt" value="%{#agtValue.drawPwt+#agtValue.drawDirPlyPwt}" />
					<%=FormatNumber.formatNumberForJSP(pageContext.getAttribute("agtDGPwt"))%>
				</th>
			</s:if>
			<s:if test="%{#SE}">
				<th align="right">
					<s:set name="agtSESale" value="%{#agtValue.scratchSale}" />
					<%=FormatNumber.formatNumberForJSP(pageContext.getAttribute("agtSESale"))%>
				</th>
				<th align="right">
					<s:set name="agtSEPwt" value="%{#agtValue.scratchPwt+#agtValue.scratchDirPlyPwt}" />
					<%=FormatNumber.formatNumberForJSP(pageContext.getAttribute("agtSEPwt"))%>
				</th>
			</s:if>
			<s:if test="%{#CS}">
				<th align="right">
					<s:set name="agtCSSale" value="%{#agtValue.CSSale-#agtValue.CSCancel}" />
					<%=FormatNumber.formatNumberForJSP(pageContext.getAttribute("agtCSSale"))%>
				</th>
			</s:if>
			<s:if test="%{#OLA}">
			<th align="right">
				<s:set name="agtOLADeposit" value="%{#agtValue.OlaDepositAmt-#agtValue.OlaDepositCancelAmt}"></s:set>
				<%=FormatNumber.formatNumberForJSP(pageContext.getAttribute("agtOLADeposit")) %>
			</th>
			<th align="right">
				<s:set name="agtOLAWithdrawal" value="%{#agtValue.OlaWithdrawalAmt}"></s:set>
				<%=FormatNumber.formatNumberForJSP(pageContext.getAttribute("agtOLAWithdrawal")) %>
			</th>
				<th align="right">
				<s:set name="agtOLANetGaming" value="%{#agtValue.OlaNetGaming}"></s:set>
				<%=FormatNumber.formatNumberForJSP(pageContext.getAttribute("agtOLANetGaming")) %>
			</th>
			</s:if>
			<s:if test="%{#SLE}">
				<th align="right">
					<s:set name="agtSLESale" value="%{#agtValue.sleSale-#agtValue.sleCancel}" />
					<%=FormatNumber.formatNumberForJSP(pageContext.getAttribute("agtSLESale"))%>
				</th>
				<th align="right">
					<s:set name="agtSLEPwt" value="%{#agtValue.slePwt+#agtValue.sleDirPlyPwt}" />
					<%=FormatNumber.formatNumberForJSP(pageContext.getAttribute("agtSLEPwt"))%>
				</th>
			</s:if>
			<s:if test="%{#IW}">
				<th align="right">
					<s:set name="agtIWSale" value="%{#agtValue.iwSale-#agtValue.iwCancel}" />
					<%=FormatNumber.formatNumberForJSP(pageContext.getAttribute("agtIWSale"))%>
				</th>
				<th align="right">
					<s:set name="agtIWPwt" value="%{#agtValue.iwPwt+#agtValue.iwDirPlyPwt}" />
					<%=FormatNumber.formatNumberForJSP(pageContext.getAttribute("agtIWPwt"))%>
				</th>
			</s:if>
			<s:if test="%{#VS}">
				<th align="right">
					<s:set name="agtVSSale" value="%{#agtValue.vsSale-#agtValue.vsCancel}" />
					<%=FormatNumber.formatNumberForJSP(pageContext.getAttribute("agtVSSale"))%>
				</th>
				<th align="right">
					<s:set name="agtVSPwt" value="%{#agtValue.vsPwt+#agtValue.vsDirPlyPwt}" />
					<%=FormatNumber.formatNumberForJSP(pageContext.getAttribute("agtVSPwt"))%>
				</th>
			</s:if>
			<th align="right">
				<s:set name="agtCase" value="%{#agtValue.drawSale-#agtValue.drawCancel-#agtValue.drawPwt-#agtValue.drawDirPlyPwt+#agtValue.scratchSale-#agtValue.scratchPwt-#agtValue.scratchDirPlyPwt+#agtValue.CSSale-#agtValue.CSCancel+#agtValue.OlaDepositAmt-#agtValue.OlaDepositCancelAmt-#agtValue.OlaWithdrawalAmt-#agtValue.OlaNetGaming+#agtValue.sleSale-#agtValue.sleCancel-#agtValue.slePwt-#agtValue.sleDirPlyPwt+#agtValue.iwSale-#agtValue.iwCancel-#agtValue.iwPwt-#agtValue.iwDirPlyPwt+#agtValue.vsSale-#agtValue.vsCancel-#agtValue.vsPwt-#agtValue.vsDirPlyPwt}" />
				<%=FormatNumber.formatNumberForJSP(pageContext.getAttribute("agtCase"))%>
			</th>
		</tr>
			</s:if>
		</s:iterator>
		</s:if>
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
$RCSfile: bo_rep_completeCollectionReport_RetailerWise.jsp,v $
$Revision: 1.3 $
</code>