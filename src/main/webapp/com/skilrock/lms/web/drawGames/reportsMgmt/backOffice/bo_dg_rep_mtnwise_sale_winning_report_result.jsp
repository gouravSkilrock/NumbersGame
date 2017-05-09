<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@page import="com.skilrock.lms.common.utility.FormatNumber"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	<head>
		<base href="<%=basePath%>">

		<title>My JSP
			'bo_dg_rep_merchantwise_sale_winning_report_result.jsp' starting page</title>

		<meta http-equiv="pragma" content="no-cache">
		<meta http-equiv="cache-control" content="no-cache">
		<meta http-equiv="expires" content="0">
		<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
		<meta http-equiv="description" content="This is my page">
		<link rel="stylesheet"
			href="<%=request.getContextPath()%>/LMSImages/css/styles.css"
			type="text/css" />
	</head>

	<body>

		<s:if test="%{merchantwiseSaleWinningBean.response==true}">
		<div align="center">
			<h3>
				<u>MTN Sale Winning Report&nbsp; From : <s:property
						value="start_date" /> &nbsp;&nbsp;To&nbsp;:&nbsp; <s:property
						value="end_Date" /> </u>
			</h3>
		</div>
		<div align="left" id="tableDataDiv">
			<table width="100%" border="1" cellpadding="3" cellspacing="0"
				bordercolor="#CCCCCC" align="center">

				<tr>
					<th rowspan="2">
						Wallet Name
					</th>
					<s:iterator value="merchantwiseSaleWinningBean.gameList">

						<th colspan="2">
							<s:property />
						</th>

					</s:iterator>
					<th rowspan="2">
					Net Amount
					</th>

				</tr>
				<tr>
					<s:iterator value="merchantwiseSaleWinningBean.gameList">
						<th>
							Sale
						</th>
						<th>
							Winning
						</th>
					</s:iterator>
					
					

				</tr>
				<s:set name="merchantGameWalletData"
					value="merchantwiseSaleWinningBean.merchantGameWalletData" />

				<s:iterator value="#merchantGameWalletData">
					<s:set name="walletGameMap" value="%{value}" />
					<s:iterator value="#walletGameMap">
					<tr class="startSortable">
						<td>
									<s:property value="%{key}" />
								</td>
						<s:set name="gameMap" value="%{value}" />
						

							<s:set name="totalSale" value="0" />
							<s:set name="totalWin" value="0" />
							<s:iterator value="#gameMap">
								<s:set name="bean" value="%{value}" />
								<s:set name="saleAmount" value="#bean.saleAmt" />
							
								<td>
									<%=FormatNumber.formatNumberForJSP(pageContext
								.getAttribute("saleAmount"))%>
								</td>
								  <s:set name="totalSale" value="#totalSale+#bean.saleAmt"/>

								<s:set name="winAmount" value="#bean.winAmt" />
								<td>
									<%=FormatNumber.formatNumberForJSP(pageContext
								.getAttribute("winAmount"))%>
								</td>
								<s:set name="totalWin" value="#totalWin+#bean.winAmt"/>

							</s:iterator>
							<td>
								<s:set name="net" value="#totalSale-#totalWin"/>
					<%=FormatNumber.formatNumberForJSP(pageContext.getAttribute("net")) %></td>
						</tr>
					</s:iterator>
					
				</s:iterator>


			</table>
		</div>
	</s:if><s:else><h1> This data is available for date after <s:property value="merchantwiseSaleWinningBean.archivingDate"/></h1>
	</s:else>
	</body>
</html>
