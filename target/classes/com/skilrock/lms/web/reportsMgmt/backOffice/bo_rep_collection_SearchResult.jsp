
<%@page import="com.skilrock.lms.common.utility.FormatNumber"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<html>
	<%
		response.setHeader("Cache-Control", "no-store"); //HTTP 1.1
		response.setHeader("Pragma", "no-cache"); //HTTP 1.0
		response.setDateHeader("Expires", 0); //prevents caching at the proxy server
	%>
	<s:head theme="ajax"/>
<body onload="disable()">
  
	<s:set name="draw" value="#application.IS_DRAW" />
	<s:set name="scratch" value="#application.IS_SCRATCH" />
	<s:set name="cs" value="#application.IS_CS"/>
	<s:if test="#session.collectionReport.size>0">	
	
			<div id="excel"  text-align: left;width: 750px;font-size: 9pt"><s:a href="bo_rep_collection_ExpExcel.action?orgType=BO">Export As Excel</s:a></div>			
			<div style="overflow-x:scroll">
			<table border="1" cellpadding="2" cellspacing="0" bordercolor="#CCCCCC" align="center">
				<tr>
					<th align="center" colspan="21" ><s:property value="#session.lastRowMap.orgName" /></th>
					<th align="right" nowrap="nowrap">Amount in <s:property value="#application.CURRENCY_SYMBOL" /></th>
				</tr>
				<tr>
					<td align="center" colspan="22"><s:property value="#session.lastRowMap.orgAdd" /></td>
				</tr>
				<tr>
					<td align="center" colspan="22"><s:property value="#session.lastRowMap.collectionMsg" /></td>
				</tr>
				
				<tr>
					<th>&nbsp; </th>
					<th>&nbsp;</th>
					<th colspan="1" align="center">&nbsp;</th>
					<s:if test="%{#application.IS_DRAW == 'YES'}">
						<th colspan="4" align="center">DRAW DETAILS</th>
					</s:if>
					<s:if test="%{#application.IS_SCRATCH == 'YES'}">
						<th colspan="4" align="center">SCRATCH DETAILS</th>
					</s:if>
					<s:if test="%{#application.IS_CS == 'YES'}">
						<th colspan="3" align="center">COMM. SERV. DETAILS</th>
					</s:if>
					<th colspan="7" align="center">ACCOUNTS DETAILS</th>
				</tr>
				
				<tr>	
					<th align="center" nowrap="nowrap">SNo</th>
					<th align="center"nowrap="nowrap"><s:property value="#application.TIER_MAP.AGENT" /> Name</th>
					<th align="center" nowrap="nowrap">OPENING BAL.</th>
										
					<s:if test="%{#application.IS_DRAW == 'YES'}">
						<th align="center" nowrap="nowrap">SALE</th>
						<th align="center" nowrap="nowrap">SALE REF.</th>
						<th align="center" nowrap="nowrap"><s:text name="PWT"/></th>
						<th align="center" >TOTAL</th>
					</s:if>
					
					<s:if test="%{#application.IS_SCRATCH == 'YES'}">
						<th align="center" nowrap="nowrap">SALE</th>
						<th align="center" nowrap="nowrap">SALE RET.</th>
						<th align="center" nowrap="nowrap"><s:text name="PWT"/></th>
						<th align="center" >TOTAL</th>
					</s:if>
					
					<s:if test="%{#application.IS_CS == 'YES'}">
						<th align="center" nowrap="nowrap">SALE</th>
						<th align="center" nowrap="nowrap">SALE RET.</th>
						<th align="center" >TOTAL</th>
					</s:if>
					
					<th align="center"  nowrap="nowrap">CASH</th>
					<th align="center" nowrap="nowrap">CHQ</th>
					<th align="center" nowrap="nowrap">CHQ RET</th>
					<th align="center" nowrap="nowrap">CREDIT</th>
					<th align="center" nowrap="nowrap">DEBIT</th>
					<th align="center" nowrap="nowrap">BANK DEPOSIT</th>
					<th align="center" nowrap="nowrap">TOTAL</th>
					
					<th align="center" nowrap="nowrap">GRAND TOTAL</th>					
				</tr>
				<s:iterator value="#session.collectionReport">
				<tr class="startSortable">	
					<td align="right"><s:property value="srNo" /></td>
					<td align="left" nowrap="nowrap"><s:property value="name" /></td>
					<td align="right" nowrap="nowrap"><s:set name="openBalJsp" value="%{openBal}"/><%=FormatNumber.formatNumberForJSP(pageContext.getAttribute("openBalJsp")) %></td>
										
					<s:if test="%{#application.IS_DRAW == 'YES'}">
						<td align="right" nowrap="nowrap"><s:set name="drawSaleJsp" value="%{drawSale}"/><%=FormatNumber.formatNumberForJSP(pageContext.getAttribute("drawSaleJsp")) %></td>
						<td align="right" nowrap="nowrap"><s:set name="drawSaleRefundJsp" value="%{drawSaleRefund}"/><%=FormatNumber.formatNumberForJSP(pageContext.getAttribute("drawSaleRefundJsp")) %></td>
						<td align="right" nowrap="nowrap"><s:set name="drawPwtJsp" value="%{drawPwt}"/><%=FormatNumber.formatNumberForJSP(pageContext.getAttribute("drawPwtJsp")) %></td>
						<th align="right" nowrap="nowrap"><s:set name="drawTotalJsp" value="%{drawTotal}"/><%=FormatNumber.formatNumberForJSP(pageContext.getAttribute("drawTotalJsp")) %></th>
					</s:if>
					
					<s:if test="%{#application.IS_SCRATCH == 'YES'}">
						<td align="right" nowrap="nowrap"><s:set name="scratchSaleJsp" value="%{scratchSale}"/><%=FormatNumber.formatNumberForJSP(pageContext.getAttribute("scratchSaleJsp")) %></td>
						<td align="right" nowrap="nowrap"><s:set name="saleRetJsp" value="%{saleRet}"/><%=FormatNumber.formatNumberForJSP(pageContext.getAttribute("saleRetJsp")) %></td>
						<td align="right" nowrap="nowrap"><s:set name="scratchPwtJsp" value="%{scratchPwt}"/><%=FormatNumber.formatNumberForJSP(pageContext.getAttribute("scratchPwtJsp")) %></td>
						<th align="right" nowrap="nowrap"><s:set name="scratchTotalJsp" value="%{scratchTotal}"/><%=FormatNumber.formatNumberForJSP(pageContext.getAttribute("scratchTotalJsp")) %></th>
					</s:if>
					
					<s:if test="%{#application.IS_CS == 'YES'}">
						<td align="right" nowrap="nowrap"><s:set name="CSSaleJsp" value="%{CSSale}"/><%=FormatNumber.formatNumberForJSP(pageContext.getAttribute("CSSaleJsp")) %></td>
						<td align="right" nowrap="nowrap"><s:set name="CSSaleRefundJsp" value="%{CSSaleRefund}"/><%=FormatNumber.formatNumberForJSP(pageContext.getAttribute("CSSaleRefundJsp")) %></td>
						<th align="right" nowrap="nowrap"><s:set name="CSTotalJsp" value="%{CSTotal}"/><%=FormatNumber.formatNumberForJSP(pageContext.getAttribute("CSTotalJsp")) %></th>
					</s:if>
					
					<td align="right" nowrap="nowrap"><s:set name="cashJsp" value="%{cash}"/><%=FormatNumber.formatNumberForJSP(pageContext.getAttribute("cashJsp")) %></td>
					<td align="right" nowrap="nowrap"><s:set name="chqJsp" value="%{chq}"/><%=FormatNumber.formatNumberForJSP(pageContext.getAttribute("chqJsp")) %></td>
					<td align="right" nowrap="nowrap"><s:set name="chqRetJsp" value="%{chqRet}"/><%=FormatNumber.formatNumberForJSP(pageContext.getAttribute("chqRetJsp")) %></td>
					<td align="right" nowrap="nowrap"><s:set name="creditJsp" value="%{credit}"/><%=FormatNumber.formatNumberForJSP(pageContext.getAttribute("creditJsp")) %></td>
					<td align="right" nowrap="nowrap"><s:set name="debitJsp" value="%{debit}"/><%=FormatNumber.formatNumberForJSP(pageContext.getAttribute("debitJsp")) %></td>	
					<td align="right" nowrap="nowrap"><s:set name="bankJsp" value="%{bankDeposit}"/><%=FormatNumber.formatNumberForJSP(pageContext.getAttribute("bankJsp")) %></td>
					<th align="right" nowrap="nowrap"><s:set name="recTotalJsp" value="%{recTotal}"/><%=FormatNumber.formatNumberForJSP(pageContext.getAttribute("recTotalJsp")) %></th>

					<th align="right" nowrap="nowrap"><s:set name="grandTotalJsp" value="%{grandTotal}"/><%=FormatNumber.formatNumberForJSP(pageContext.getAttribute("grandTotalJsp")) %></th>					
				</tr>
				</s:iterator>					
				
				<tr>
					<td align="right">&nbsp;</td>
					<th align="left">TOTAL</th>
					<th align="right" nowrap="nowrap"><s:set name="totalOpenBalJsp" value="%{#session.lastRowMap.totalOpenBal}"/><%=FormatNumber.formatNumberForJSP(pageContext.getAttribute("totalOpenBalJsp")) %></th>
					
					<s:if test="%{#application.IS_DRAW == 'YES'}">
						<th align="right" nowrap="nowrap"><s:set name="totalDrawSaleJsp" value="%{#session.lastRowMap.totalDrawSale}"/><%=FormatNumber.formatNumberForJSP(pageContext.getAttribute("totalDrawSaleJsp")) %></th>
						<th align="right" nowrap="nowrap"><s:set name="totalDrawSaleRefundJsp" value="%{#session.lastRowMap.totalDrawSaleRefund}"/><%=FormatNumber.formatNumberForJSP(pageContext.getAttribute("totalDrawSaleRefundJsp")) %></th>
						<th align="right" nowrap="nowrap"><s:set name="totalDrawPwtJsp" value="%{#session.lastRowMap.totalDrawPwt}"/><%=FormatNumber.formatNumberForJSP(pageContext.getAttribute("totalDrawPwtJsp")) %></th>
						<th align="right" nowrap="nowrap"><s:set name="totalDrawTotalJsp" value="%{#session.lastRowMap.totalDrawTotal}"/><%=FormatNumber.formatNumberForJSP(pageContext.getAttribute("totalDrawTotalJsp")) %></th>
					</s:if>
					
					<s:if test="%{#application.IS_SCRATCH == 'YES'}">
						<th align="right" nowrap="nowrap"><s:set name="totalScratchSaleJsp" value="%{#session.lastRowMap.totalScratchSale}"/><%=FormatNumber.formatNumberForJSP(pageContext.getAttribute("totalScratchSaleJsp")) %></th>
						<th align="right" nowrap="nowrap"><s:set name="totalSaleRetJsp" value="%{#session.lastRowMap.totalSaleRet}"/><%=FormatNumber.formatNumberForJSP(pageContext.getAttribute("totalSaleRetJsp")) %></th>
						<th align="right" nowrap="nowrap"><s:set name="totalScratchPwtJsp" value="%{#session.lastRowMap.totalScratchPwt}"/><%=FormatNumber.formatNumberForJSP(pageContext.getAttribute("totalScratchPwtJsp")) %></th>
						<th align="right" nowrap="nowrap"><s:set name="totalScratchTotalJsp" value="%{#session.lastRowMap.totalScratchTotal}"/><%=FormatNumber.formatNumberForJSP(pageContext.getAttribute("totalScratchTotalJsp")) %></th>
					</s:if>
					
					<s:if test="%{#application.IS_CS == 'YES'}">
						<th align="right" nowrap="nowrap"><s:set name="totalCSSaleJsp" value="%{#session.lastRowMap.totalCSSale}"/><%=FormatNumber.formatNumberForJSP(pageContext.getAttribute("totalCSSaleJsp")) %></th>
						<th align="right" nowrap="nowrap"><s:set name="totalCSSaleRefundJsp" value="%{#session.lastRowMap.totalCSSaleRefund}"/><%=FormatNumber.formatNumberForJSP(pageContext.getAttribute("totalCSSaleRefundJsp")) %></th>
						<th align="right" nowrap="nowrap"><s:set name="totalCSTotalJsp" value="%{#session.lastRowMap.totalCSTotal}"/><%=FormatNumber.formatNumberForJSP(pageContext.getAttribute("totalCSTotalJsp")) %></th>
					</s:if>
					
					<th align="right" nowrap="nowrap"><s:set name="totalCashJsp" value="%{#session.lastRowMap.totalCash}"/><%=FormatNumber.formatNumberForJSP(pageContext.getAttribute("totalCashJsp")) %></th>
					<th align="right" nowrap="nowrap"><s:set name="totalChqJsp" value="%{#session.lastRowMap.totalChq}"/><%=FormatNumber.formatNumberForJSP(pageContext.getAttribute("totalChqJsp")) %></th>
					<th align="right" nowrap="nowrap"><s:set name="totalChqRetJsp" value="%{#session.lastRowMap.totalChqRet}"/><%=FormatNumber.formatNumberForJSP(pageContext.getAttribute("totalChqRetJsp")) %></th>
					<th align="right" nowrap="nowrap"><s:set name="totalChqRetJsp" value="%{#session.lastRowMap.totalCredit}"/><%=FormatNumber.formatNumberForJSP(pageContext.getAttribute("totalCreditJsp")) %></th>
					<th align="right" nowrap="nowrap"><s:set name="totalDebitJsp" value="%{#session.lastRowMap.totalDebit}"/><%=FormatNumber.formatNumberForJSP(pageContext.getAttribute("totalDebitJsp")) %></th>	
					<th align="right" nowrap="nowrap"><s:set name="totalbankJsp" value="%{#session.lastRowMap.totalBankDeposit}"/><%=FormatNumber.formatNumberForJSP(pageContext.getAttribute("totalbankJsp")) %></th>
					<th align="right" nowrap="nowrap"><s:set name="totalRecTotalJsp" value="%{#session.lastRowMap.totalRecTotal}"/><%=FormatNumber.formatNumberForJSP(pageContext.getAttribute("totalRecTotalJsp")) %></th>
					
					<th align="right" nowrap="nowrap"><s:set name="totalGrandTotalJsp" value="%{#session.lastRowMap.totalGrandTotal}"/><%=FormatNumber.formatNumberForJSP(pageContext.getAttribute("totalGrandTotalJsp")) %></th>	
				</tr>	
				</table>
				</div>
			</s:if>			
			<s:else>					
				<div style="text-align: center">No Record Found</div>
			</s:else>
						
			 
			 <br/>
		
	
	
  		
	
	
</body>
</html>
<code id="headId" style="visibility: hidden">
$RCSfile: bo_rep_collection_SearchResult.jsp,v $
$Revision: 1.3 $
</code>