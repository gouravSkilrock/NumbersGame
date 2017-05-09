<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@page import="com.skilrock.lms.common.utility.FormatNumber"%>
<%@page import="com.skilrock.lms.common.utility.GetDate"%>
<%
	response.setHeader("Cache-Control", "no-store"); //HTTP 1.1
	response.setHeader("Pragma", "no-cache"); //HTTP 1.0
	response.setDateHeader("Expires", 0); //prevents caching at the proxy server
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
	<head>
		<s:head theme="ajax" debug="false" />
		<meta http-equiv="Content-Type"
			content="text/html; charset=iso-8859-1" />
		<link rel="stylesheet"
			href="<%=request.getContextPath()%>/LMSImages/css/styles.css"
			type="text/css" />
		<title><%=application.getAttribute("JSP_PAGE_TITLE")%></title>
	</head>
	<body>
		<div id="wrap">
			<div id="top_form">
				<s:if test="%{regionWiseBankDetailBeanList neq null and regionWiseBankDetailBeanList.size()>0}">
					<div align="center">
						<h3>
							<u>Bank Wise Cashier Wise Transaction Report &nbsp; From :
							<s:property
						value="startDate" /> &nbsp;&nbsp;To&nbsp;:&nbsp; <s:property
						value="endDate" /> </u>
				</h3>
					</div>
					<div align="left">
						<s:form action="exportReportAsExcel.action" method="post"
							enctype="multipart/form-data">
							<s:hidden id="tableValue" name="reportData" />
							<s:hidden name="startDate" value="%{startDate}" />
							<s:hidden name="endDate" value="%{endDate}" />
							<s:hidden name="reportName"
								value="Bank_Wise_Cashier_Wise_Transaction_Report" />
							<s:submit value="Export As Excel"
								cssStyle="margin-right:-400px;font-size:15px"
								onclick="getData();" />
						</s:form>
					</div>
					<div id='tableDataDiv'>
						<table width="100%" border="1" cellpadding="3" cellspacing="0"
							bordercolor="#CCCCCC" align="center">
							<tr>
								<th align="center" colspan=11>
									<s:property value="orgName" />
								</th>
								<th align="center">
									Amount in
									<s:property value="#application.CURRENCY_SYMBOL" />
								</th>
							</tr>
							<tr>
								<th align="center" colspan=12>
									<s:property value="orgaddress" />

								</th>
							</tr>
							<tr>
								<th align="center">
									S.No
								</th>
								<th align="center">
									Region
								</th>
								<th align="center">
									Bank Name
								</th>
								<th align="center">
									Branch Name
								</th>
								<th align="center">
									Txn Reference
								</th>
								<th align="center">
									Txn Date
								</th>
								<th align="center">
									Cashier
								</th>
								<th align="center">
									Customer Name
								</th>
								<th align="center">
									Terminal ID
								</th>
								<th align="center">
									<s:text name="AGENT" />
									ID
								</th>
								<th align="center">
									Transaction Type
								</th>
								<%-- <th align="center">Amount in <s:property
								value="#application.CURRENCY_SYMBOL" />
						</th> --%>
								<th align="center">
									Amount
								</th>
							</tr>

							<s:iterator id="result" value="regionWiseBankDetailBeanList"
								status="status">

								<tr class="startSortable">
									<td>
										<s:property value="%{#status.index+1}" />
									</td>
									<td>
										<s:property value="region" />
									</td>
									<td>
										<s:property value="bankName" />
									</td>
									<td>
										<s:property value="branchName" />
									</td>
									<td>
										<s:property value="txnRef" />
									</td>
									<td>
										<s:property value="txnDate" />
									</td>
									<td>
										<s:property value="cashierName" />
									</td>
									<td>
										<s:property value="customerName" />
									</td>
									<td>
										<s:property value="terminalId" />
									</td>
									<td>
										<s:property value="lmcId" />
									</td>
									<td>
										<s:property value="transactionType" />
									</td>
									<td>
										<s:set name="amountFormated" value="amount" />
										<%=FormatNumber.formatNumberForJSP(pageContext
							.getAttribute("amountFormated"))%></td>
								</tr>
							</s:iterator>

						</table>
					</div>
				</s:if>
				<s:else>
					<center>
					<h1>
						NO RECORDS TO PROCESS
					</h1>
					</center>
				</s:else>
			</div>
		</div>
	</body>
</html>
<code id="headId" style="visibility: hidden">
	$RCSfile: bo_rep_bankDetails_Search.jsp,v $ $Revision: 1.3 $
</code>