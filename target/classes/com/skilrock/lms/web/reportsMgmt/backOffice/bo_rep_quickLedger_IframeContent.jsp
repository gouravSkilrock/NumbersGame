<%@page import="com.skilrock.lms.common.utility.FormatNumber"%>
<%@ page language="java" pageEncoding="ISO-8859-1"%>

<%@ taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	<head>

	</head>
	<script>
	function printLed() {
		parent.window.frames['ledgerFrame'].focus();
		parent.window.frames['ledgerFrame'].print();
	}
</script>

	<!-- <body onload="printLed()"> -->
	<body>
		<div style=" height: 330px;">
			<table border="0" align="left" cellpadding="0" cellspacing="0">
				<tr>
					<td colspan="2" align="center">
						********************************
					</td>
				</tr>
				<tr>
					<td colspan="2" align="center">
						<span><s:property value="#application.ORG_NAME_JSP" /> </span>
					</td>
				</tr>
				<tr>
					<td colspan="2" align="center">
						<!--<span><b>Legder- <s:property
									value="#session.ledgerType" /> </b> </span>
					-->
						<span><b><s:text name="label.ledger.rep"/></b> 
						</span>
					</td>
				</tr>
				<tr>
					<td colspan="2" align="center">
						<s:property value="#session.retName" />
					</td>
				</tr>
				<tr>
				<td>&nbsp;</td>
				</tr>
				<tr>
					<td align="left" rowspan="2">
						<s:property value="#session.reportDate" />
					</td>
					<td align="right">
						<s:text name="label.from"/>:
						<s:property value="#session.fromTime" />
					</td>
				</tr>
				<tr>
					<td align="right">
						<s:text name="label.to"/>:
						<s:property value="#session.toTime" />
					</td>
				</tr>
				<tr>
					<td colspan="2" align="center">
						----------------------------------------------------
					</td>
				</tr>
				<s:if test="#application.IS_DRAW == 'YES'">
					<tr>
						<td align="left" nowrap>
							<s:text name="label.net.sle"/>(-)
						</td>
						<td align="right">
							<s:set name="netsaleJsp" value="%{#session.netsale}" /><%=FormatNumber.formatNumberForJSP(pageContext
						.getAttribute("netsaleJsp"))%></td>
					</tr>
					
				</s:if>

				<tr>
					<td align="left">
						<s:text name="label.net.pwt"/>(+)
					</td>
					<td align="right">
						<s:set name="netPwtJsp" value="%{#session.netPwt}" /><%=FormatNumber.formatNumberForJSP(pageContext
					.getAttribute("netPwtJsp"))%></td>
				</tr>

				<s:if test="#application.IS_SCRATCH == 'YES'">

					<tr>
						<td align="left" nowrap="nowrap">
							<s:text name="label.scratch.purchase"/>(-)
						</td>
						<td align="right">
							<s:set name="scratchPurJsp" value="%{#session.purchase}" /><%=FormatNumber.formatNumberForJSP(pageContext
						.getAttribute("scratchPurJsp"))%></td>
					</tr>
				</s:if>
				<tr>
					<td align="left">
						<s:text name="label.net.pymt"/>(+)
					</td>
					<td align="right">
						<s:set name="netPaymentJsp" value="%{#session.netPayment}" /><%=FormatNumber.formatNumberForJSP(pageContext
					.getAttribute("netPaymentJsp"))%></td>
				</tr>
				<tr>
					<td colspan="2" align="center">
						----------------------------------------------------
					</td>
				</tr>


				<s:if test="#application.IS_SCRATCH == 'YES'">
					<s:if test="#application.SE_LAST_SOLD_TKT_ENTRY == 'YES'">
						<tr>
							<td align="left" nowrap="nowrap">
								<s:text name="label.sracth.sle"/>
							</td>
							<td align="right">
								<s:set name="scratchSaleJsp" value="%{#session.scratchSale}" /><%=FormatNumber.formatNumberForJSP(pageContext
							.getAttribute("scratchSaleJsp"))%></td>
						</tr>
					</s:if>
				</s:if>


				<s:if test="#application.IS_CS == 'YES'">
					<tr>
						<td colspan="2" align="center">
							----------------------------------------------------
						</td>
					</tr>
					<tr>
						<td align="left" nowrap="nowrap">
							<s:text name="label.cs.sle"/>
						</td>
						<td align="right">
							<s:set name="csSaleJsp" value="%{#session.netSaleCS}" /><%=FormatNumber.formatNumberForJSP(pageContext
						.getAttribute("csSaleJsp"))%></td>
					</tr>
				</s:if>
				
				<s:if test="#application.IS_IW == 'YES'">
					<tr>
						<td colspan="2" align="center">
							----------------------------------------------------
						</td>
					</tr>
					<tr>
						<td align="left" nowrap="nowrap">
							<s:text name="label.iw.sle"/>
						</td>
						<td align="right">
							<s:set name="vsSaleJsp" value="%{#session.netSaleIW}" /><%=FormatNumber.formatNumberForJSP(pageContext
						.getAttribute("vsSaleJsp"))%></td>
					</tr>
					<tr>
						<td align="left" nowrap="nowrap">
							<s:text name="label.iw.pwt"/>
						</td>
						<td align="right">
							<s:set name="iwPwtJsp" value="%{#session.netPwtIW}" /><%=FormatNumber.formatNumberForJSP(pageContext
						.getAttribute("iwPwtJsp"))%></td>
					</tr>
				</s:if>
				
				<s:if test="#application.IS_VS == 'YES'">
					<tr>
						<td colspan="2" align="center">
							----------------------------------------------------
						</td>
					</tr>
					<tr>
						<td align="left" nowrap="nowrap">
							<s:text name="label.vs.sale"/>
						</td>
						<td align="right">
							<s:set name="vsSaleJsp" value="%{#session.netSaleVS}" /><%=FormatNumber.formatNumberForJSP(pageContext
						.getAttribute("vsSaleJsp"))%></td>
					</tr>
					<tr>
						<td align="left" nowrap="nowrap">
							<s:text name="label.vs.pwt"/>
						</td>
						<td align="right">
							<s:set name="vsPwtJsp" value="%{#session.netPwtVS}" /><%=FormatNumber.formatNumberForJSP(pageContext
						.getAttribute("vsPwtJsp"))%></td>
					</tr>
				</s:if>
				<tr>
					<td colspan="2" align="center">
						----------------------------------------------------
					</td>
				</tr>


				<tr>
					<td align="left">
						<s:text name="label.cash.col"/>
					</td>
					<td align="right">
						<s:set name="cashColJsp" value="%{#session.cashCol}" /><%=FormatNumber.formatNumberForJSP(pageContext
					.getAttribute("cashColJsp"))%></td>
				</tr>

				<tr>
					<td colspan="2" align="left">
						********************************
					</td>
				</tr>




			</table>
		</div>
	</body>
</html>
