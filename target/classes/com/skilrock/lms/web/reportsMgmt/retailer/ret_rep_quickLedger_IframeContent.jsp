<%@page import="com.skilrock.lms.common.utility.FormatNumber"%>
<%@ page language="java" pageEncoding="ISO-8859-1"%>

<%@ taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	<head>

	</head>
	<script>
function printLed(){
	parent.window.frames['ledgerFrame'].focus();
	parent.window.frames['ledgerFrame'].print();
}
</script>

	<body onload="printLed()">
		<div style="width: 180px; height: 330px;">
			<table border="0" align="left" cellpadding="0" cellspacing="0">
				<tr>
					<td colspan="2" align="center">
						**********************
					</td>
				</tr>
				<tr>
					<td colspan="2" align="center">
						<span><s:property value="#application.ORG_NAME_JSP" /> </span>
					</td>
				</tr>
				<tr>
					<td colspan="2" align="center">
						<span><b>Legder- <s:property
									value="#session.ledgerType" /> </b> </span>
					</td>
				</tr>
				<tr>
					<td colspan="2" align="center">
						<s:property value="#session.retName" />
					</td>
				</tr>
				<tr>
					<td align="left">
						<s:property value="#session.todayDate" />
					</td>
					<td align="right">
						<s:property value="#session.todayTime" />
					</td>
				</tr>
				<tr>
					<td colspan="2" align="center">
						---------------------------
					</td>
				</tr>
				<s:if test="#application.IS_DRAW == 'YES'">
					<tr>
						<td align="left" nowrap>
							DRAW SALE(-)
						</td>
						<td align="right">
							<s:set name="netsaleJsp" value="%{#session.netsale}" /><%=FormatNumber.formatNumberForJSP(pageContext
								.getAttribute("netsaleJsp"))%></td>
					</tr>
				</s:if>
				<tr>
					<td align="left">
						NET PWT(+)
					</td>
					<td align="right">
						<s:set name="netPwtJsp" value="%{#session.netPwt}" /><%=FormatNumber.formatNumberForJSP(pageContext
							.getAttribute("netPwtJsp"))%></td>
				</tr>
				<tr>
					<td align="left">
						NET PYMT(+)
					</td>
					<td align="right">
						<s:set name="netPaymentJsp" value="%{#session.netPayment}" /><%=FormatNumber.formatNumberForJSP(pageContext
							.getAttribute("netPaymentJsp"))%></td>
				</tr>
				<tr>
					<td colspan="2" align="center">
						---------------------------
					</td>
				</tr>
				<s:if test="#application.IS_SCRATCH == 'YES'">
					<tr>
						<td align="left" nowrap="nowrap">
							SCRATCH SALE
						</td>
						<td align="right">
							<s:set name="scratchSaleJsp" value="%{#session.scratchSale}" /><%=FormatNumber.formatNumberForJSP(pageContext
								.getAttribute("scratchSaleJsp"))%></td>
					</tr>
				</s:if>



				<s:if test="#application.IS_CS == 'YES'">
					<tr>
						<td colspan="2" align="center">
							---------------------------
						</td>
					</tr>
					<tr>
						<td align="left" nowrap="nowrap">
							CS SALE
						</td>
						<td align="right">
							<s:set name="csSaleJsp" value="%{#session.netSaleCS}" /><%=FormatNumber.formatNumberForJSP(pageContext
								.getAttribute("csSaleJsp"))%></td>
					</tr>
				</s:if>
				
				<s:if test="#application.IS_OLA == 'YES'">
					<tr>
						<td colspan="2" align="center">
							---------------------------
						</td>
					</tr>
					<tr>
						<td align="left" nowrap="nowrap">
							OLA DEPOSIT (-)
						</td>
						<td align="right">
							<s:set name="olaDepJsp" value="%{#session.olaDeposit}" /><%=FormatNumber.formatNumberForJSP(pageContext
								.getAttribute("olaDepJsp"))%></td>
					</tr>
					<tr>
						<td align="left" nowrap="nowrap">
							OLA WDRWL (+)
						</td>
						<td align="right">
							<s:set name="olaWthdrwJsp" value="%{#session.olaWithdrawal}" /><%=FormatNumber.formatNumberForJSP(pageContext
								.getAttribute("olaWthdrwJsp"))%></td>
					</tr>
				</s:if>

				<s:if test="#application.IS_SLE == 'YES'">
					<tr>
						<td colspan="2" align="center">
							---------------------------
						</td>
					</tr>
					<tr>
						<td align="left" nowrap="nowrap">
							SLE SALE (-)
						</td>
						<td align="right">
							<s:set name="sleSaleJsp" value="%{#session.sleSale}" /><%=FormatNumber.formatNumberForJSP(pageContext
								.getAttribute("sleSaleJsp"))%></td>
					</tr>
					<tr>
						<td align="left" nowrap="nowrap">
							SLE PWT (+)
						</td>
						<td align="right">
							<s:set name="slePwtJsp" value="%{#session.slePwt}" /><%=FormatNumber.formatNumberForJSP(pageContext
								.getAttribute("slePwtJsp"))%></td>
					</tr>
				</s:if>
				
				<s:if test="#application.IS_IW == 'YES'">
					<tr>
						<td colspan="2" align="center">
							---------------------------
						</td>
					</tr>
					<tr>
						<td align="left" nowrap="nowrap">
							IW SALE (-)
						</td>
						<td align="right">
							<s:set name="iwSaleJsp" value="%{#session.iwSale}" /><%=FormatNumber.formatNumberForJSP(pageContext
								.getAttribute("iwSaleJsp"))%></td>
					</tr>
					<tr>
						<td align="left" nowrap="nowrap">
							IW PWT (+)
						</td>
						<td align="right">
							<s:set name="iwPwtJsp" value="%{#session.iwPwt}" /><%=FormatNumber.formatNumberForJSP(pageContext
								.getAttribute("iwPwtJsp"))%></td>
					</tr>
				</s:if>
				
				<s:if test="#application.IS_VS == 'YES'">
					<tr>
						<td colspan="2" align="center">
							---------------------------
						</td>
					</tr>
					<tr>
						<td align="left" nowrap="nowrap">
							VS SALE (-)
						</td>
						<td align="right">
							<s:set name="vsSaleJsp" value="%{#session.vsSale}" /><%=FormatNumber.formatNumberForJSP(pageContext
								.getAttribute("vsSaleJsp"))%></td>
					</tr>
					<tr>
						<td align="left" nowrap="nowrap">
							VS PWT (+)
						</td>
						<td align="right">
							<s:set name="vsPwtJsp" value="%{#session.vsPwt}" /><%=FormatNumber.formatNumberForJSP(pageContext
								.getAttribute("vsPwtJsp"))%></td>
					</tr>
				</s:if>
				
				<tr>
					<td colspan="2" align="center">
						---------------------------
					</td>
				</tr>


				<tr>
					<td align="left">
						CASH COL
					</td>
					<td align="right">
						<s:set name="cashColJsp" value="%{#session.cashCol}" /><%=FormatNumber.formatNumberForJSP(pageContext
							.getAttribute("cashColJsp"))%></td>
				</tr>

				<tr>
					<td colspan="2" align="left">
						**********************
					</td>
				</tr>




			</table>
		</div>
	</body>
</html>
