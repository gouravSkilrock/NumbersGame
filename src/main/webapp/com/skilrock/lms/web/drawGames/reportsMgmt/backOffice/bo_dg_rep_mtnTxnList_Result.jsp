<%@page import="com.skilrock.lms.beans.DateBeans"%>
<%@page import="com.skilrock.lms.common.utility.GetDate"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.List"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.skilrock.lms.common.utility.FormatNumber"%>
<%@page import="com.skilrock.lms.dge.beans.MtnCustomerCenterBean"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html>
	<%
		response.setHeader("Cache-Control", "no-store"); //HTTP 1.1
		response.setHeader("Pragma", "no-cache"); //HTTP 1.0
		response.setDateHeader("Expires", 0); //prevents caching at the proxy server
	%>
	<head>
		<s:head theme="ajax" />
	</head>
	<body onload="disable()">
		<br />
		<s:if test="merchantTransactioDataBeans.size>0">

			<s:form action="mtn_txn_report_export" method="post"
				enctype="multipart/form-data">
				<s:hidden id="tableValue" name="reportData" />
				<s:hidden name="reportName" value="MTN Transaction Report" />
				<s:submit key="btn.exportasexcel"
					cssStyle="margin-right:-400px;font-size:11px; text-align:center"
					onclick="createTableData();" />
			</s:form>

		</s:if>
		<div align="center">
			<h3>
				<u><s:text name="label.mtn.customer.center.report"></s:text> From <s:property
						value="startDate" /> To <s:property value="endDate" /> </u>
			</h3>
		</div>
		<div id="tableDataDiv">
			<table id="dataTable" style="border-collapse: collapse;border-color: #000000; width="100%" border="1" cellpadding="6"
				cellspacing="0" bordercolor="#CCCCCC" align="center" class="TFtable">
				<s:if test="mtnCustomerCenterBeans.size>0">
					<tr style="background: #cecece">
						<th>
							S.No
						</th>
						<th>
							Transaction Type
						</th>
						<th>
							Ticket Number
						</th>
						<th>
							Amount
						</th>
						<th>
							Wallet Name
						</th>
						<th>
							Transaction Amount
						</th>
						<th>
							Transaction Date
						</th>
						<th>
							Winlot Transaction Id
						</th>
						<th>
							Winlot Transaction Status
						</th>
						<th>
							MTN Transaction Id
						</th>
					</tr>
					<tr>
					
						<s:iterator id="beanCart" value="mtnCustomerCenterBeans" status="indx">
							<s:set name="data" value="%{value}"></s:set>
							<tr>
								<td rowspan='<s:property value="transactionMap.size"/>'>
									<s:property value="#indx.index+1" />
								</td>
								<td rowspan='<s:property value="transactionMap.size"/>'>
									<s:property value="txnType" />
								</td>
								<td rowspan='<s:property value="transactionMap.size"/>'  >
									<div style="color: blue" onclick="myfunction('<s:property value="gameTicketNo" />')"><span style="cursor:pointer"><s:property value="gameTicketNo" /></span></div>
								</td>
								<td rowspan='<s:property value="transactionMap.size"/>'>
									<s:property value="ticketAmount" />
								</td>
								<td rowspan='<s:property value="transactionMap.size"/>'>
									<s:property value="walletName" />
								</td>
								<s:iterator value="transactionMap">
									<td>
										<s:property value="value.transactionAmount" />
									</td>
									<td>
										<s:property value="value.txnDate" />
									</td>
									<td>
										<s:property value="key" />
									</td>
									<td>
										<s:property value="value.engineTxnStatus" />
									</td>
									<td>
										<s:property value="value.merchantTxnId" />
									</td>
									
									</tr>
								</s:iterator>
							
						</s:iterator>

					</tr>
				</s:if>
				<s:else>
					<tr>
						<td colspan="9" align="center">
							No Records to Process
						</td>
					</tr>
				</s:else>
			</table>
		</div>
	</body>
</html>

<code id="headId" style="visibility: hidden">
	$RCSfile: bo_dg_rep_mtnTxnList_Result.jsp,v $ $Revision: 1.3 $
</code>