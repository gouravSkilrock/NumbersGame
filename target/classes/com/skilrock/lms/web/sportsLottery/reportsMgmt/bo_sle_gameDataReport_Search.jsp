<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="com.skilrock.lms.common.utility.FormatNumber"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
	<head>
		<title><%=application.getAttribute("JSP_PAGE_TITLE")%></title>

		<script type="text/javascript">
			$(document).ready(function() {
			});

			$(function() {
				$("#accordion").accordion();
			});
		</script>
	</head>
	<body>
		<!--
		<div id="accordion">
			<h3>
				Section 1
			</h3>
			<div>
				<p>
					Mauris mauris ante, blandit et, ultrices a, suscipit eget, quam.
					Integer ut neque. Vivamus nisi metus, molestie vel, gravida in,
					condimentum sit amet, nunc. Nam a nibh. Donec suscipit eros. Nam
					mi. Proin viverra leo ut odio. Curabitur malesuada. Vestibulum a
					velit eu ante scelerisque vulputate.
				</p>
			</div>
			<h3>
				Section 2
			</h3>
			<div>
				<p>
					Sed non urna. Donec et ante. Phasellus eu ligula. Vestibulum sit
					amet purus. Vivamus hendrerit, dolor at aliquet laoreet, mauris
					turpis porttitor velit, faucibus interdum tellus libero ac justo.
					Vivamus non quam. In suscipit faucibus urna.
				</p>
			</div>
			<h3>
				Section 3
			</h3>
			<div>
				<p>
					Nam enim risus, molestie et, porta ac, aliquam ac, risus. Quisque
					lobortis. Phasellus pellentesque purus in massa. Aenean in pede.
					Phasellus ac libero ac tellus pellentesque semper. Sed ac felis.
					Sed commodo, magna quis lacinia ornare, quam ante aliquam nisi, eu
					iaculis leo purus venenatis dui.
				</p>
			</div>
		</div>
		-->
		<div id="errorDiv" style="color: red;">
			<s:actionmessage />
		</div>
		<div id="wrap">
			<table width="100%" border="1" cellpadding="3" cellspacing="0"
				bordercolor="#CCCCCC" align="center">
				<tr>
					<th align="center">
						Game Name
					</th>
					<th align="center">
						Game Type Name
					</th>
					<th align="center">
						Draw Name
					</th>
					<th align="center">
						Draw Time
					</th>
					<th align="center">
						Draw Freeze Time
					</th>
					<th align="center">
						Draw Status
					</th>
					<th align="center">
						Total Sale
					</th>
					<th align="center">
						Total Sale Amount
					</th>
					<th align="center">
						Total Winning
					</th>
					<th align="center">
						Total Winning Amount
					</th>
				</tr>
				<s:if test="gameDataReportList.size>0">

					<s:set name="totalNoOfSale" value="0.0" />
					<s:set name="totalSaleAmount" value="0.0" />
					<s:set name="totalNoOfWinning" value="0.0" />
					<s:set name="totalWinningAmount" value="0.0" />

					<s:iterator id="beanCart" value="gameDataReportList">
						<tr class="startSortable">
							<td align="left" nowrap="nowrap">
								<s:property value="%{gameName}" />
							</td>
							<td align="left" nowrap="nowrap">
								<s:property value="%{gameTypeName}" />
							</td>
							<td align="left" nowrap="nowrap">
								<s:property value="%{drawName}" />
							</td>
							<td align="left" nowrap="nowrap">
								<s:property value="%{drawTime}" />
							</td>
							<td align="left" nowrap="nowrap">
								<s:property value="%{drawFreezeTime}" />
							</td>
							<td align="left" nowrap="nowrap">
								<s:property value="%{drawStatus}" />
							</td>
							<td align="right" nowrap="nowrap">
								<s:set name="noOfSale" value="%{noOfSale}" />
								<s:property value="noOfSale" />
							</td>
							<td align="right" nowrap="nowrap">
								<s:set name="saleAmount" value="%{saleAmount}" />
								<%=FormatNumber.formatNumberForJSP(pageContext.getAttribute("saleAmount"))%>
							</td>
							<td align="right" nowrap="nowrap">
								<s:set name="noOfWinning" value="%{noOfWinning}" />
								<s:property value="noOfWinning" />
							</td>
							<td align="right" nowrap="nowrap">
								<s:set name="winningAmount" value="%{winningAmount}" />
								<%=FormatNumber.formatNumberForJSP(pageContext.getAttribute("winningAmount"))%>
							</td>
						</tr>
						<s:set name="totalNoOfSale" value="%{#totalNoOfSale+#noOfSale}" />
						<s:set name="totalSaleAmount" value="%{#totalSaleAmount+#saleAmount}" />
						<s:set name="totalNoOfWinning" value="%{#totalNoOfWinning+#noOfWinning}" />
						<s:set name="totalWinningAmount" value="%{#totalWinningAmount+#winningAmount}" />
					</s:iterator>
					<tr>
						<th colspan="6" align="center">Total</th>
						<th align="right"><%=FormatNumber.formatNumberForJSPWithoutZero(pageContext.getAttribute("totalNoOfSale"))%></th>
						<th align="right"><%=FormatNumber.formatNumberForJSP(pageContext.getAttribute("totalSaleAmount"))%></th>
						<th align="right"><%=FormatNumber.formatNumberForJSPWithoutZero(pageContext.getAttribute("totalNoOfWinning"))%></th>
						<th align="right"><%=FormatNumber.formatNumberForJSP(pageContext.getAttribute("totalWinningAmount"))%></th>
					</tr>
				</s:if>
				<s:else>
					<tr>
						<td colspan="10" align="center">
							No Records to Process
						</td>
					</tr>
				</s:else>
			</table>
		</div>
	</body>
</html>