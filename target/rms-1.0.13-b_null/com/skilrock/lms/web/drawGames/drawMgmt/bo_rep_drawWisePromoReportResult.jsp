<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@page import="com.skilrock.lms.dge.beans.ReportBeanDrawModule"%>
<%@page import="com.skilrock.lms.dge.beans.ReportDrawBean"%>
<%@page import="com.skilrock.lms.dge.beans.ReportGameBean"%>
<%@page import="com.skilrock.lms.common.utility.FormatNumber"%>
<%
	response.setDateHeader("Expires", 0);
%>
<%
	response.setHeader("Cache-Control", "no-store"); //HTTP 1.1
	response.setHeader("Pragma", "no-cache"); //HTTP 1.0
	response.setDateHeader("Expires", 0); //prevents caching at the proxy server
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">

<head>
<s:head theme="ajax" debug="false" />
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<link rel="stylesheet"
	href="<%=request.getContextPath()%>/LMSImages/css/styles.css"
	type="text/css" />
<title><%=application.getAttribute("JSP_PAGE_TITLE")%></title>
</head>

<body>
	<s:if test="%{message!=null}">
		<s:property value="message" />
	</s:if>
	<s:else>
	<div align="left">
		<s:form action="exportReportAsExcelWithoutTable.action" method="post"
			enctype="multipart/form-data">
			<s:hidden id="tableValue" name="reportData" />
			<s:hidden name="reportName" value="Draw_Wise_Report" />
			<s:submit key="btn.exportasexcel"
				cssStyle="margin-right:-400px;font-size:15px"
				onclick="getTableData();" />
		</s:form>
	</div>
	<div id="wrap">
		<div id="top_form">
				<s:actionerror />
				<div id='tableDataDiv'>
					<table width="100%" border="1" cellpadding="3" cellspacing="0"
						bordercolor="#CCCCCC" align="center">
						<tr>
							<center> <u> <b></> <s:text
									name="label.promo.draw.game.rep" /> <s:text name="label.from" /> : <s:property
									value="dateOfDraw" /> &nbsp;&nbsp; <s:text name="label.to" />&nbsp;:&nbsp;<s:property
									value="end_Date" /> </b> </u></center>
						</tr>
						<tr>
							<th colspan="15" align="center"><s:text
									name="label.draw.data" />
							</th>
						</tr>
						<tr>
							<th><s:text name="label.draw.time" /></th>
							<th><s:text name="label.draw.freeze.time" /></th>
							<th><s:text name="label.draw.name" /></th>
							<th><s:text name="label.draw.status" /></th>
							<th><s:text name="label.win.res" /></th>
							<th><s:text name="label.sale.cashVal" /></th>
							<th><s:text name="label.sale.promoVal" /></th>
							<th><s:text name="label.sale.val" /></th>
							<th><s:text name="label.win.cashAmt" /></th>
							<th><s:text name="label.win.promoAmt" /></th>
							<th><s:text name="label.win.amt" /></th>
							<th><s:text name="label.claim.cashAmt" /></th>
							<th><s:text name="label.claim.promoAmt" /></th>
							<th><s:text name="label.claim.amt" /></th>
						</tr>
						<s:set name="dataBeanList" value="drawDataBeanList" />
						<s:if test="#dataBeanList.size > 0">
							<!-- Initialize Variales For Table Data Total -->
							<s:set name="lmsTotalSal" value="0" />
							<s:set name="lmsTotalWinAmt" value="0" />
							<s:set name="lmsTotalCashSal" value="0" />
							<s:set name="lmsTotalPromoSal" value="0" />
							<s:set name="lmsTotalWin" value="0" />
							<s:set name="lmsTotalCashWin" value="0" />
							<s:set name="lmsTotalPromoWin" value="0" />
							<s:set name="lmsTotalClaim" value="0" />
							<s:set name="lmsTotalCashClaim" value="0" />
							<s:set name="lmsTotalPromoClaim" value="0" />
							<s:set name="dataBean" id="dataBean" value="#dataBeanList" />
							<s:iterator id="beanCart" value="%{#dataBean}">
								<tr class="startSortable">
									<td align="left">
										<s:property value="drawDateTime" />
									</td>
									<td align="left">
										<s:property value="drawFreezeTime" />
									</td>
									<td align="left" id='<s:property value="drawDateTime"/>'>
											<s:property value="drawName" />
									</td>
									<td align="left" id='<s:property value="drawDateTime"/>'>
										<s:property value="drawStatus" />
									</td>
									<td align="left">
										<s:property value="winningResult" />
									</td>
									<td align="right">
										<s:set name="totalCashSaleAmt" value="lmsSaleBean.totalSaleValue" /> <%=FormatNumber.formatNumberForJSP(pageContext.getAttribute("totalCashSaleAmt"))%> 
										<s:set name="lmsTotalCashSal" value="%{#lmsTotalCashSal + #totalCashSaleAmt}" />
									</td>
									<td align="right">
										<s:set name="totalPromoSaleAmt" value="lmsSaleBean.totalPromoSaleValue" /> <%=FormatNumber.formatNumberForJSP(pageContext.getAttribute("totalPromoSaleAmt"))%> 
										<s:set name="lmsTotalPromoSal" value="%{#lmsTotalPromoSal + #totalPromoSaleAmt}" />
									</td>
									<td align="right">
										 <s:set name="totalSal" value="%{#totalCashSaleAmt + #totalPromoSaleAmt}" /><%=FormatNumber.formatNumberForJSP(pageContext.getAttribute("totalSal"))%> 
										<s:set name="lmsTotalSal" value="%{#lmsTotalSal + #totalCashSaleAmt + #totalPromoSaleAmt}" />
									</td>
	
									<td align="right">
										<s:set name="totalCashWinAmt" value="lmsSaleBean.totalWinningAmount" /> <%=FormatNumber.formatNumberForJSP(pageContext.getAttribute("totalCashWinAmt"))%>
										<s:set name="lmsTotalCashWin" value="%{#lmsTotalCashWin + #totalCashWinAmt}" />
									</td>
									<td align="right">
										<s:set name="totalPromoWinAmt" value="lmsSaleBean.totalPromoWinningAmount" /> <%=FormatNumber.formatNumberForJSP(pageContext.getAttribute("totalPromoWinAmt"))%>
										<s:set name="lmsTotalPromoWin" value="%{#lmsTotalPromoWin + #totalPromoWinAmt}" />
									</td>
									<td align="right">
										<s:set name="totalWinAmt" value="%{#totalCashWinAmt + #totalPromoWinAmt}" /><%=FormatNumber.formatNumberForJSP(pageContext.getAttribute("totalWinAmt"))%>
										<s:set name="lmsTotalWinAmt" value="%{#lmsTotalWinAmt + #totalCashWinAmt + #totalPromoWinAmt}"/>
									</td>
	
									<td align="right">
										<s:set name="totalCashClaimAmt" value="lmsSaleBean.claimedAmount" /> <%=FormatNumber.formatNumberForJSP(pageContext.getAttribute("totalCashClaimAmt"))%>
										<s:set name="lmsTotalCashClaim" value="%{#lmsTotalCashClaim + #totalCashClaimAmt}" />
									</td>
									<td align="right">
										<s:set name="totalPromoClaimAmt" value="lmsSaleBean.promoClaimedAmount" /> <%=FormatNumber.formatNumberForJSP(pageContext.getAttribute("totalPromoClaimAmt"))%>
										<s:set name="lmsTotalPromoClaim" value="%{#lmsTotalPromoClaim + #totalPromoClaimAmt}" />
									</td>
									<td align="right">
										<s:set name="totalWinAmt" value="%{#totalCashClaimAmt + #totalPromoClaimAmt}" /><%=FormatNumber.formatNumberForJSP(pageContext.getAttribute("totalWinAmt"))%>
										<s:set name="lmsTotalClaim" value="%{#lmsTotalClaim + #totalCashClaimAmt + #totalPromoClaimAmt}" />
									</td>
								</tr>
							</s:iterator>
							<tr>
								<td colspan="5" align="center"><s:text name="label.total" />
								</td>
								<td align="right">
									<s:set name="totalCashSale" value="%{#lmsTotalCashSal}" /> <%=FormatNumber.formatNumberForJSP(pageContext.getAttribute("totalCashSale"))%>
								</td>
								<td align="right">
									<s:set name="totalPromoSale" value="%{#lmsTotalPromoSal}" /> <%=FormatNumber.formatNumberForJSP(pageContext.getAttribute("totalPromoSale"))%>
								</td>
								<td align="right">
									<s:set name="totalSale" value="%{#lmsTotalSal}" /> <%=FormatNumber.formatNumberForJSP(pageContext.getAttribute("totalSale"))%>
								</td>
								<td align="right">
									<s:set name="totalCashWinAmt" value="%{#lmsTotalCashWin}" /> <%=FormatNumber.formatNumberForJSP(pageContext.getAttribute("totalCashWinAmt"))%>
								</td>
								<td align="right">
									<s:set name="totalPromoWinAmt" value="%{#lmsTotalPromoWin}" /> <%=FormatNumber.formatNumberForJSP(pageContext.getAttribute("totalPromoWinAmt"))%>
								</td>
								<td align="right">
									<s:set name="totalWinAmt" value="%{#lmsTotalWinAmt}" /> <%=FormatNumber.formatNumberForJSP(pageContext.getAttribute("totalWinAmt"))%>
								</td>
								<td align="right">
									<s:set name="totalCashClaimAmt" value="%{#lmsTotalCashClaim}" /> <%=FormatNumber.formatNumberForJSP(pageContext.getAttribute("totalCashClaimAmt"))%>
								</td>
								<td align="right">
									<s:set name="totalPromoClaimAmt" value="%{#lmsTotalPromoClaim}" /> <%=FormatNumber.formatNumberForJSP(pageContext.getAttribute("totalPromoClaimAmt"))%>
								</td>
								<td align="right">
									<s:set name="totalClaimAmt" value="%{#lmsTotalClaim}" /> <%=FormatNumber.formatNumberForJSP(pageContext.getAttribute("totalClaimAmt"))%>
								</td>
							</tr>
						</s:if>
						<s:else>
							<tr>
								<td colspan="6" align="center"><s:text
										name="msg.no.record.process" />
								</td>
							</tr>
						</s:else>
					</table>
				</div>
			</div>
		</div>
	</s:else>
</body>
</html>
<code id="headId" style="visibility: hidden"> $RCSfile:
	bo_rep_drawWisePromoReportResult.jsp,v $ $Revision:
	1.3.6.3.2.17.4.6.2.5 $ </code>
