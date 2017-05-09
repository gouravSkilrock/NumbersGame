<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@page import="com.skilrock.lms.common.utility.FormatNumber"%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>


<%@page import="com.skilrock.lms.common.utility.FormatNumber"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
	<head>
		<s:head theme="ajax" debug="false" />
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<link rel="stylesheet"
			href="<%=request.getContextPath()%>/LMSImages/css/styles.css"
			type="text/css" />
		<title><%=application.getAttribute("JSP_PAGE_TITLE")%></title>
		<script
			src="<%=request.getContextPath()%>/com/skilrock/lms/web/drawGames/drawMgmt/js/bo_rep_drawGameConsolidate.js"></script>
	</head>
	<body>
		<div id="wrap">
			<div id="top_form">
			<s:if test="%{message neq null && message neq ''}">
			 <s:text name="label.select.st.date.after"/>  <s:property value="message"/>
			</s:if>
			<s:else>
				<div>
					<font size="2"> <center> <b></><s:text
							name="label.mtn.ledger.report" /> <s:text name="label.from" /> : <s:property
							value="startDate" /> &nbsp;<s:text name="label.to" />&nbsp;: <s:property
							value="endDate" /> </b> </center></font>
					<s:form action="bo_rep_mtnLedgerReport_exportToExcel" method="post"
						enctype="multipart/form-data">
						<s:hidden id="tableValue" name="reportData" />
						<s:hidden name="reportName" value="MTN Ledger Report" />
						<s:submit key="btn.exportasexcel"
							cssStyle="margin-right:-400px;font-size:11px; text-align:center"
							onclick="createTableData();" />
					</s:form>
				</div>
				<div id="tableDataDiv">
					<table id="dataTable"
						style="border-collapse: collapse; margin-bottom: 30px; margin-left: 100px;"
						border="1" width="75%" cellpadding="6" cellspacing="0"
						bordercolor="#CCCCCC" align="left">
						<s:if test="mtnDataMap neq null && mtnDataMap.size>0">
							<tr>
								<th style="background-color: #e6e6e6;" align="center">
									<s:text name="Date" />
								</th>
								<th style="background-color: #e6e6e6" align="center">
									<s:text name="Sales" />
								</th>
								<th style="background-color: #e6e6e6" align="center">
									<s:text name="Winning" />
								</th>
								<th style="background-color: #e6e6e6" align="center">
									<s:text name="Net Amount" />
								</th>

							</tr>

							<s:set name="totalSale" value="0" />
							<s:set name="totalWinning" value="0" />
							<s:set name="netBalance" value="0" />
							<s:set name="totalSalesBalance" value="0" />
							<s:set name="totalWinningBalance" value="0" />
							<s:set name="totalNetBalance" value="0" />
							<s:iterator value="mtnDataMap">
								<s:set name="dataMap" value="%{value}" />


								<tr class="startSortable">
									<td align="center" style="width: 80px;">
										<s:property value="key" />
									</td>
									<td style="width: 80px;" align="center">
										<s:set name="totalSalesBalance" value="%{#totalSalesBalance+#dataMap.totalSale}"/>
										<s:set name="totalSale" value="#dataMap.totalSale" />
										<%=FormatNumber.formatNumberForJSP(pageContext
							.getAttribute("totalSale"))%>
									</td>
									<td style="width: 80px;" align="center">
										<s:set name="totalWinningBalance" value="%{#totalWinningBalance+#dataMap.totalWinning}"/>
										<s:set name="totalWinning" value="#dataMap.totalWinning" />
										<%=FormatNumber.formatNumberForJSP(pageContext
							.getAttribute("totalWinning"))%>
									</td>
									<td style="width: 80px;" align="center">
										<s:set name="netBalance" value="#totalSale - #totalWinning" />
										<s:set name="totalNetBalance" value="%{#totalNetBalance+#netBalance}"/>
										<%=FormatNumber.formatNumberForJSP(pageContext
							.getAttribute("netBalance"))%>
									</td>
								</tr>
							</s:iterator>

							<tr>
								<td align="center">
									<s:text name="label.total" />
								</td>
								<td align="center">
									<s:set name="totalSalesBalance" value="%{#totalSalesBalance}" />

									<%=pageContext.getAttribute("totalSalesBalance")%>

								</td>
								<td align="center">
									<s:set name="totalWinningBalance" value="%{#totalWinningBalance}" />

									<%=pageContext.getAttribute("totalWinningBalance")%>

								</td>
								<td align="center">
									<s:set name="totalNetBalance" value="%{#totalNetBalance}" />

									<%=pageContext.getAttribute("totalNetBalance")%>

								</td>
							</tr>

						</s:if>
						<s:else>
							<tr>
								<td  align="center">
									<s:text name="msg.no.record.process" />
								</td>
							</tr>
						</s:else>
					</table>
				</div></s:else>
			</div>
		</div>
	</body>
</html>

<code id="headId" style="visibility: hidden"> $RCSfile:
	bo_rep_mtnLedgerReport_result.jsp,v $ $Revision: 1.3 $
</code>