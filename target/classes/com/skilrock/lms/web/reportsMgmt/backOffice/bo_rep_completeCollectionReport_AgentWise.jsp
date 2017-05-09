
<%@page import="com.skilrock.lms.beans.DateBeans"%>
<%@page import="com.skilrock.lms.common.utility.GetDate"%>
<%@page import="com.skilrock.lms.common.utility.FormatNumber"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<div align="center"><h3><u><s:text name="SALE"/> <s:text name="PWT"/> <s:text name="Collection" /> <s:text name="Report"/> &nbsp;
	<%if("".equals(((DateBeans)session.getAttribute("datebean") ).getReportType())) {%>
  		 From : 
  		<%=GetDate.getConvertedDate(( (DateBeans)session.getAttribute("datebean") ).getStartDate())%>
  		&nbsp;&nbsp;To&nbsp;:&nbsp;
  		<%=GetDate.getConvertedDate(((DateBeans)session.getAttribute("datebean") ).getEndDate())%>
  		
	<%} else {out.println("Of : "+((DateBeans)session.getAttribute("datebean") ).getReportType());}%>
	</u></h3></div>

<div id="excel" align="center"text-align:left;width: 750px;font-size: 9pt">
	<s:a
		href="bo_rep_CompltCollReport_ExpExcel.action?orgType=BO&start_date=%{start_date}&end_Date=%{end_Date}&reportType=%{reportType}"><s:text name="btn.exportasexcel"/></s:a>
</div>
<table width="100%" border="1" cellpadding="3" cellspacing="0"
	bordercolor="#CCCCCC" align="center" style="font-size: 12px">
	<s:set name="DG" value="%{#session.isDG}"></s:set>
	<s:set name="SE" value="%{#session.isSE}"></s:set>
	<s:set name="CS" value="%{#session.isCS}"></s:set>
	<s:set name="OLA" value="%{#session.isOLA}"></s:set>
	<s:set name="SLE" value="%{#session.isSLE}"></s:set>
	<s:set name="IW" value="%{#session.isIW}"></s:set>
	<s:set name="VS" value="%{#session.isVS}"></s:set>
	<s:set name="colCount" value="2" />
	
	<s:if test="%{#OLA}">
		<s:set name="colCount" value="3"></s:set>
	</s:if>
	<s:if test="%{#SE}">
		<s:set name="colCount" value="2"></s:set>
	</s:if>
	<s:if test="%{#SLE}">
		<s:set name="colCount" value="2"></s:set>
	</s:if>
	<s:if test="%{#IW}">
		<s:set name="colCount" value="2"></s:set>
	</s:if>
	<s:if test="%{#VS}">
		<s:set name="colCount" value="2"></s:set>
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
	<s:if test="%{#DG&&#SE&&#CS}">
		<s:set name="colCount" value="5" />
	</s:if>
	<s:if test="%{#DG&&#SE&&#SLE}">
		<s:set name="colCount" value="6"></s:set>
	</s:if>
	<s:if test="%{#DG&&#SE&&#IW}">
		<s:set name="colCount" value="6"></s:set>
	</s:if>
	<s:if test="%{#DG&&#SE&&#VS}">
		<s:set name="colCount" value="6"></s:set>
	</s:if>
	<s:if test="%{#DG&&#SE&&#SLE&&#IW}">
		<s:set name="colCount" value="8"></s:set>
	</s:if>
	<s:if test="%{#DG&&#SE&&#SLE&&#VS}">
		<s:set name="colCount" value="8"></s:set>
	</s:if>
	<s:if test="%{#DG&&#SE&&#CS&&#OLA}">
		<s:set name="colCount" value="8"></s:set>
	</s:if>
	<s:if test="%{#DG&&#SE&&#CS&&#OLA&&#SLE}">
		<s:set name="colCount" value="10"></s:set>
	</s:if>
	<s:if test="%{#DG&&#SE&&#CS&&#OLA&&#IW}">
		<s:set name="colCount" value="10"></s:set>
	</s:if>
	<s:if test="%{#DG&&#SE&&#CS&&#OLA&&#VS}">
		<s:set name="colCount" value="10"></s:set>
	</s:if>
	<s:if test="%{#DG&&#SE&&#CS&&#OLA&&#SLE&&#IW}">
		<s:set name="colCount" value="12"></s:set>
	</s:if>
	<s:if test="%{#DG&&#SE&&#CS&&#OLA&&#SLE&&#VS}">
		<s:set name="colCount" value="12"></s:set>
	</s:if>

	<s:if test="%{#session.result!=null}">
		<tr>
		<s:if test="%{#application.COUNTRY_DEPLOYED == 'SAFARIBET'}">
			<th align="center" colspan="<s:property value="#colCount+1"/>" ><s:property value="#session.orgName" /></th>
		</s:if>
		<s:elseif test="%{#application.COUNTRY_DEPLOYED=='NIGERIA'}">
			<th align="center" colspan="<s:property value="#colCount+1"/>" ><s:property value="#session.orgName" /></th>
		</s:elseif>
		<s:else>
			<th align="center" colspan="<s:property value="#colCount+3"/>" ><s:property value="#session.orgName" /></th>
		</s:else>
			<th align="center"><s:text name="label.amt.in" /> <s:property value="#application.CURRENCY_SYMBOL" /></th>
		</tr>
		<tr>
			<td align="center" colspan="<s:property value="#colCount+4"/>">
				<s:property value="#session.orgAdd" />
			</td>
		</tr>
		<tr align="center">
			<th rowspan="3">
				<s:if test="%{reportType=='Agent Wise'}">
				<s:text name="label.party.name" />
				</s:if>
				<s:elseif test="%{reportType=='Day Wise'}">
				<s:text name="label.date" />
				</s:elseif>
			</th>
			<th colspan="<s:property value="#colCount+3"/>">
				<s:a
					href="bo_rep_completeCollectionReport_SearchExpand.action?start_date=%{start_date}&end_Date=%{end_Date}&reportType=%{reportType} Expand"
					theme="ajax" targets="result"><s:text name="label.expand" />
				</s:a>
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
					<s:text name="label.iw.game" />
				</th>
			</s:if>
			<s:if test="%{#VS}">
				<th colspan="2">
					<s:text name="label.vs.game" />
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
		<s:set name="olaDeposit" value="0.0" />
		<s:set name="olaWithdrwal" value="0.0" />
		<s:set name="olaNetGaming" value="0.0" />
		<s:set name="slSale" value="0.0" />
		<s:set name="slPwt" value="0.0" />
		<s:set name="IWSale" value="0.0" />
		<s:set name="IWPwt" value="0.0" />
		<s:set name="VSSale" value="0.0" />
		<s:set name="VSPwt" value="0.0" />
		<s:set name="totCash" value="0.0" />
		<s:set name="drawCash" value="0.0" />
		<s:set name="scratchCash" value="0.0" />
		<s:set name="csCash" value="0.0" />
		<s:set name="olaCash" value="0.0" />
		<s:set name="sleCash" value="0.0" />
		<s:set name="IWCash" value="0.0" />
		<s:set name="VSCash" value="0.0" />

		<s:iterator id="result" value="#session.result">
			<s:set name="orgValue" value="%{value}"></s:set>
			<tr class="startSortable">
				<td>
					<s:if test="%{reportType=='Agent Wise'}">
						<s:a
							href="bo_rep_completeCollectionReport_RetalerWise.action?reportType=Retailer Wise&agtId=%{key}&start_date=%{start_date}&end_Date=%{end_Date}&agtOrgName=%{#orgValue.orgName}"
							theme="ajax" targets="result">
							<s:property value="%{#orgValue.orgName}" />
						</s:a>
					</s:if>
					<s:elseif test="%{reportType=='Day Wise'}">
						<s:a
							href="bo_rep_completeCollectionReport_RetalerWise.action?reportType=Agent Wise&dateType=SQL&agtId=0&start_date=%{#orgValue.orgName}&end_Date=%{#orgValue.orgName}&agtOrgName=%{#orgValue.orgName}"
							theme="ajax" targets="result">
							<s:property value="%{#orgValue.orgName}" />
						</s:a>
					</s:elseif>
				</td>
				<s:if test="%{#DG}">
					<td align="right">
						<s:set name="drawSale"
							value="%{#orgValue.drawSale-#orgValue.drawCancel}" />
						<%=FormatNumber.formatNumberForJSP(pageContext
								.getAttribute("drawSale"))%>
					</td>
					<td align="right">
						<s:set name="drawPwt"
							value="%{#orgValue.drawPwt+#orgValue.drawDirPlyPwt}" />
						<%=FormatNumber.formatNumberForJSP(pageContext
								.getAttribute("drawPwt"))%>
					</td>
					<s:set name="drawCash"
						value="%{#orgValue.drawSale-#orgValue.drawCancel-#orgValue.drawPwt-#orgValue.drawDirPlyPwt}"></s:set>
				</s:if>
				<s:if test="%{#SE}">
					<td align="right">
						<s:set name="scratchSale" value="%{#orgValue.scratchSale}" />
						<%=FormatNumber.formatNumberForJSP(pageContext
								.getAttribute("scratchSale"))%>
					</td>
					<td align="right">
						<s:set name="scratchPwt"
							value="%{#orgValue.scratchPwt+#orgValue.scratchDirPlyPwt}" />
						<%=FormatNumber.formatNumberForJSP(pageContext
								.getAttribute("scratchPwt"))%>
					</td>
					<s:set name="scratchCash"
						value="%{#orgValue.scratchSale-#orgValue.scratchPwt-#orgValue.scratchDirPlyPwt}"></s:set>
				</s:if>
				<s:if test="%{#CS}">
					<td align="right">
						<s:set name="commServSale"
							value="%{#orgValue.CSSale-#orgValue.CSCancel}" />
						<%=FormatNumber.formatNumberForJSP(pageContext
								.getAttribute("commServSale"))%>
					</td>
					<s:set name="csCash" value="%{#orgValue.CSSale-#orgValue.CSCancel}"></s:set>
				</s:if>
				<s:if test="%{#OLA}">
					<td align="right">
						<s:set name="olaDeposit1"
							value="%{#orgValue.OlaDepositAmt-#orgValue.OlaDepositCancelAmt}"></s:set>
						<%=FormatNumber.formatNumberForJSP(pageContext
								.getAttribute("olaDeposit1"))%>
					</td>
					<td align="right">
						<s:set name="olaWithdrwal1" value="%{#orgValue.OlaWithdrawalAmt}"></s:set>
						<%=FormatNumber.formatNumberForJSP(pageContext
								.getAttribute("olaWithdrwal1"))%>
					</td>
					<td align="right">
						<s:set name="olaNetGaming1" value="%{#orgValue.OlaNetGaming}"></s:set>
						<%=FormatNumber.formatNumberForJSP(pageContext
								.getAttribute("olaNetGaming1"))%>
					</td>
					<s:set name="olaCash"
						value="%{#orgValue.OlaDepositAmt-#orgValue.OlaDepositCancelAmt-#orgValue.OlaWithdrawalAmt-#orgValue.OlaNetGaming}"></s:set>
				</s:if>
				<s:if test="%{#SLE}">
					<td align="right">
						<s:set name="sleSale"
							value="%{#orgValue.sleSale-#orgValue.sleCancel}" />
						<%=FormatNumber.formatNumberForJSP(pageContext
								.getAttribute("sleSale"))%>
					</td>
					<td align="right">
						<s:set name="slePwt"
							value="%{#orgValue.slePwt+#orgValue.sleDirPlyPwt}" />
						<%=FormatNumber.formatNumberForJSP(pageContext
								.getAttribute("slePwt"))%>
					</td>
					<s:set name="sleCash"
						value="%{#orgValue.sleSale-#orgValue.sleCancel-#orgValue.slePwt-#orgValue.sleDirPlyPwt}"></s:set>
				</s:if>
				<s:if test="%{#IW}">
					<td align="right">
						<s:set name="iwSale"
							value="%{#orgValue.iwSale-#orgValue.iwCancel}" />
						<%=FormatNumber.formatNumberForJSP(pageContext
								.getAttribute("iwSale"))%>
					</td>
					<td align="right">
						<s:set name="iwPwt"
							value="%{#orgValue.iwPwt+#orgValue.iwDirPlyPwt}" />
						<%=FormatNumber.formatNumberForJSP(pageContext
								.getAttribute("iwPwt"))%>
					</td>
					<s:set name="IWCash"
						value="%{#orgValue.iwSale-#orgValue.iwCancel-#orgValue.iwPwt-#orgValue.iwDirPlyPwt}"></s:set>
				</s:if>
				<s:if test="%{#VS}">
					<td align="right">
						<s:set name="vsSale"
							value="%{#orgValue.vsSale-#orgValue.vsCancel}" />
						<%=FormatNumber.formatNumberForJSP(pageContext
								.getAttribute("vsSale"))%>
					</td>
					<td align="right">
						<s:set name="vsPwt"
							value="%{#orgValue.vsPwt+#orgValue.vsDirPlyPwt}" />
						<%=FormatNumber.formatNumberForJSP(pageContext
								.getAttribute("vsPwt"))%>
					</td>
					<s:set name="VSCash"
						value="%{#orgValue.vsSale-#orgValue.vsCancel-#orgValue.vsPwt-#orgValue.vsDirPlyPwt}"></s:set>
				</s:if>

				<th align="right">
					<s:set name="cash" value="#drawCash+#scratchCash+#csCash+#olaCash+#sleCash+#IWCash+#VSCash"></s:set>
					<%=FormatNumber.formatNumberForJSP(pageContext.getAttribute("cash"))%>
				</th>
				<s:set name="dgSale"
					value="%{#dgSale+#orgValue.drawSale-#orgValue.drawCancel}" />
				<s:set name="dgPwt"
					value="%{#dgPwt+#orgValue.drawPwt+#orgValue.drawDirPlyPwt}" />
				<s:set name="seSale" value="%{#seSale+#orgValue.scratchSale}" />
				<s:set name="sePwt"
					value="%{#sePwt+#orgValue.scratchPwt+#orgValue.scratchDirPlyPwt}" />
				<s:set name="csSale"
					value="%{#csSale+#orgValue.CSSale-#orgValue.CSCancel}" />

				<s:set name="olaDeposit"
					value="%{#olaDeposit+#orgValue.OlaDepositAmt-#orgValue.OlaDepositCancelAmt}" />
				<s:set name="olaWithdrwal"
					value="%{#olaWithdrwal+#orgValue.OlaWithdrawalAmt}" />
				<s:set name="olaNetGaming"
					value="%{#olaNetGaming+#orgValue.OlaNetGaming}"></s:set>

				<s:set name="slSale"
					value="%{#slSale+#orgValue.sleSale-#orgValue.sleCancel}" />
				<s:set name="IWSale"
					value="%{#IWSale+#orgValue.iwSale-#orgValue.iwCancel}" />
				<s:set name="VSSale"
					value="%{#VSSale+#orgValue.vsSale-#orgValue.vsCancel}" />
				<!--<s:set name="sleTest"
					value="%{#orgValue.sleCancel}" />
					--><%//System.out.println("SLE Cancel Amt:"+FormatNumber.formatNumberForJSP(pageContext.getAttribute("sleTest")));%>
				<s:set name="slPwt"
					value="%{#slPwt+#orgValue.slePwt+#orgValue.sleDirPlyPwt}" />
				<s:set name="IWPwt"
					value="%{#IWPwt+#orgValue.iwPwt+#orgValue.iwDirPlyPwt}" />
				<s:set name="VSPwt"
					value="%{#VSPwt+#orgValue.vsPwt+#orgValue.vsDirPlyPwt}" />

				<s:if test="{#cash == 'null'}">
					<!-- Do Nothing -->
				</s:if>
				<s:else>
					<s:set name="totCash" value="%{#totCash+#cash}" />
				</s:else>
			</tr>
		</s:iterator>

		<tr>
			<th>
				<s:text name="label.total" />
			</th>
			<s:if test="%{#DG}">
				<th align="right">
					<%=FormatNumber.formatNumberForJSP(pageContext
							.getAttribute("dgSale"))%>
				</th>
				<th align="right">
					<%=FormatNumber.formatNumberForJSP(pageContext
							.getAttribute("dgPwt"))%>
				</th>
			</s:if>
			<s:if test="%{#SE}">
				<th align="right">
					<%=FormatNumber.formatNumberForJSP(pageContext
							.getAttribute("seSale"))%>
				</th>
				<th align="right">
					<%=FormatNumber.formatNumberForJSP(pageContext
							.getAttribute("sePwt"))%>
				</th>
			</s:if>
			<s:if test="%{#CS}">
				<th align="right">
					<%=FormatNumber.formatNumberForJSP(pageContext
							.getAttribute("csSale"))%>
				</th>
			</s:if>
			<s:if test="%{#OLA}">
				<th align="right">
					<%=FormatNumber.formatNumberForJSP(pageContext
							.getAttribute("olaDeposit"))%>
				</th>
				<th align="right">
					<%=FormatNumber.formatNumberForJSP(pageContext
							.getAttribute("olaWithdrwal"))%>
				</th>
				<th align="right">
					<%=FormatNumber.formatNumberForJSP(pageContext
							.getAttribute("olaNetGaming"))%>
				</th>
			</s:if>
			<s:if test="%{#SLE}">
				<th align="right">
					<%//System.out.println("SLE Total:"+ FormatNumber.formatNumberForJSP(pageContext.getAttribute("sleSale")));%>
					<%=FormatNumber.formatNumberForJSP(pageContext
							.getAttribute("slSale"))%>
				</th>
				<th align="right">
					<%=FormatNumber.formatNumberForJSP(pageContext
							.getAttribute("slPwt"))%>
				</th>
			</s:if>
			<s:if test="%{#IW}">
				<th align="right">
					<%//System.out.println("SLE Total:"+ FormatNumber.formatNumberForJSP(pageContext.getAttribute("sleSale")));%>
					<%=FormatNumber.formatNumberForJSP(pageContext
							.getAttribute("IWSale"))%>
				</th>
				<th align="right">
					<%=FormatNumber.formatNumberForJSP(pageContext
							.getAttribute("IWPwt"))%>
				</th>
			</s:if>
			<s:if test="%{#VS}">
				<th align="right">
					<%=FormatNumber.formatNumberForJSP(pageContext
							.getAttribute("VSSale"))%>
				</th>
				<th align="right">
					<%=FormatNumber.formatNumberForJSP(pageContext
							.getAttribute("VSPwt"))%>
				</th>
			</s:if>
			<th align="right">
				<%=FormatNumber.formatNumberForJSP(pageContext
						.getAttribute("totCash"))%>
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
$RCSfile: bo_rep_completeCollectionReport_AgentWise.jsp,v $
$Revision: 1.3 $
</code>