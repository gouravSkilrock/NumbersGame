<%@page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="UTF-8"%>
<%@page import="com.skilrock.lms.common.utility.FormatNumber"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	<head>

	</head>
	<script>
	function printLed() {
		parent.window.frames['transactionFrame'].focus();
		parent.window.frames['transactionFrame'].print();
	}
</script>

	<!--<body onload="printLed()">
		-->
	<body>
		<div style="width: 345px; height: 330px;">
			<table border="0" align="left" cellpadding="0" cellspacing="0">
				<tr>
					<td colspan="2" align="center">
						*****************************************
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
						<span><b><s:text name="label.txn.rep" /> </b> </span>
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
						<s:text name="label.from" />
						:
						<s:property value="#session.fromTime" />
					</td>
				</tr>
				<tr>
					<td align="right">
						<s:text name="label.to" />
						:
						<s:property value="#session.toTime" />
					</td>
				</tr>
				<tr>
					<td colspan="2" align="center">
						------------------------------------------
					</td>
				</tr>

				<tr>
					<td colspan="2" align="left">
						<b><s:text name="SALE" /> </b>
					</td>
				</tr>

				<s:if test="#session.gameDetailMap.size>0">

					<s:iterator id="gameDetailMap" value="#session.gameDetailMap">
						<tr>
							<td align="left" nowrap>
								<s:property value="%{key}" />
							</td>
							<td align="right">
								<s:property value="%{value.split(':')[0]}" />
							</td>
						</tr>
						<s:if test="%{value.split(':')[2] neq null}">
							<tr>
								<td align="left" nowrap>
									<s:property value="%{key}" />
									(
									<s:text name="label.slot" />
									)
								</td>
								<td align="right">
									<s:property value="%{value.split(':')[2]}" />
								</td>
							</tr>
						</s:if>
					</s:iterator>
				</s:if>


				<tr>
					<td colspan="2" align="center">
						------------------------------------------
					</td>
				</tr>
				<tr>
					<td align="left">
						<s:text name="label.total" />
					</td>

					<td align="right">
						<s:property value="#session.totalSale" />
					</td>

				</tr>
				<tr>
					<td colspan="2" align="center">
						------------------------------------------
					</td>
				</tr>
				<tr>
					<td colspan="2" align="left">
						<b><s:text name="WINNING" /> </b>
					</td>
				</tr>
				<s:if test="#session.gameDetailMap.size>0">

					<s:iterator id="gameDetailMap" value="#session.gameDetailMap">
						<tr>
							<td align="left" nowrap>
								<s:property value="%{key}" />
							</td>
							<td align="right">
								<s:property value="%{value.split(':')[1]}" />
							</td>
						</tr>

					</s:iterator>
				</s:if>
							<s:if test="#application.IS_SCRATCH == 'YES'">
					<tr>
							<td align="left" nowrap>
								Scratch
							</td>
							<td align="right">
								<s:property value="#session.ScratchPWT"/>
							</td>
					</tr>
				</s:if>	
			
				<tr>
					<td colspan="2" align="center">
						------------------------------------------
					</td>
				</tr>
				<tr>
					<td align="left">
						<s:text name="label.total" />
					</td>

					<td align="right">
						<s:property value="#session.totalPwt" />
					</td>

				</tr>
				<tr>
					<td colspan="2" align="center">
						------------------------------------------
					</td>
				</tr>

				<tr>
					<td align="left">
						<s:text name="label.cash.in.hand" />
					</td>
					<td align="right">
						<s:property value="#session.cashInHand" />
					</td>
				</tr>
				<tr>
					<td align="left">
						<s:text name="label.pay.to.agent" />
					</td>
					<td align="right">
						<s:property value="#session.PTPA" />
					</td>
				</tr>
				<tr>
					<td colspan="2" align="center">
						------------------------------------------
					</td>
				</tr>
				<tr>
					<td align="left">
						<s:text name="label.profit" />
					</td>
					<td align="right">
						<s:property value="#session.Profit" />
					</td>
				</tr>

				<tr>
					<td colspan="2" align="left">
						*****************************************
					</td>
				</tr>

			</table>
		</div>
	</body>
</html>
