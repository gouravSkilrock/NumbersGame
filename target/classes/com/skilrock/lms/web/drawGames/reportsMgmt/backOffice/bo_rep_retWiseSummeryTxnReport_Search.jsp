
<%@page import="com.skilrock.lms.common.utility.FormatNumber"%><%@ taglib prefix="s" uri="/struts-tags"%>
<html>
	<%
		response.setHeader("Cache-Control", "no-store"); //HTTP 1.1
		response.setHeader("Pragma", "no-cache"); //HTTP 1.0
		response.setDateHeader("Expires", 0); //prevents caching at the proxy server
	%>
	<s:head theme="ajax" />
	<body>
		<s:if test="%{status!=null}">
			<h3 style="color: red;" align="center">
				<s:property value="status" />
			</h3>
		</s:if>
		<s:else>
			<div align="center">
				<h3>
					<u>Retailer Wise Summary Transaction Report From <s:property
							value="startDate" /> to <s:property value="endDate" /> </u>
				</h3>
			</div>
			<s:if
				test="%{retWiseSummaryTxnBeans!=null and retWiseSummaryTxnBeans.size!=0}">
				<div align="left">
					<s:form action="exportReportAsExcel.action" method="post"
						enctype="multipart/form-data">
						<s:hidden id="tableValue" name="reportData" />
						<s:hidden name="reportName" id="reportTitle" value="Retailer Wise Summary Transaction Report" />
						<s:submit value="Export As Excel" cssStyle="margin-right:-450px; " onclick="getData();" />
					</s:form>
				</div>
			<div id="tableDataDiv">
				<table width="100%" border="1" cellpadding="3" cellspacing="0"
					bordercolor="#CCCCCC" align="center">
						<tr align="center">
							<th nowrap="nowrap" width="12%">
								Serial No.
							</th>
							<th nowrap="nowrap" width="40%">
								Retailer Name
							</th>
							<th nowrap="nowrap" width="12%">
								Sale Transaction
							</th>
							<th nowrap="nowrap" width="12%">
								Refund Transaction
							</th>
							<th nowrap="nowrap" width="12%">
								PWT Transaction
							</th>
							<th nowrap="nowrap" width="12%">
								Total Transactions
							</th>
						</tr>
						<s:set name="totalSale" value="0" />
						<s:set name="totalRefund" value="0" />
						<s:set name="totalPwt" value="0" />
						<s:set name="totalAll" value="0" />
						<s:iterator value="retWiseSummaryTxnBeans" status="itr">
							<tr align="center" class="startSortable">
								<td align="left">
									<s:property value="#itr.count" />
								</td>
								<td align="left">
									<s:property value="retName" />
								</td>
								<td align="left">
									<s:set name="dgSaleCount" value="%{dgSaleCount}" />
									<s:property value="#dgSaleCount" />
								</td>
								<td align="left">
									<s:set name="dgRefundCount" value="%{dgRefundCount}" />
									<s:property value="#dgRefundCount" />
								</td>
								<td align="left">
									<s:set name="dgPwtCount" value="%{dgPwtCount}" />
									<s:property value="#dgPwtCount" />
								</td>
								<td align="left">
									<s:set name="totalCount" value="%{totalCount}" />
									<s:property value="#totalCount" />
								</td>
							</tr>
							<s:set name="totalSale" value="#totalSale+#dgSaleCount" />
							<s:set name="totalRefund" value="#totalRefund+#dgRefundCount" />
							<s:set name="totalPwt" value="#totalPwt+#dgPwtCount" />
							<s:set name="totalAll" value="#totalAll+#totalCount" />
						</s:iterator>
						<tr align="center">
								<td align="left">&nbsp;</td>
								<td align="left">Total</td>
								<td align="left">
									<s:property value="#totalSale" />
								</td>
								<td align="left">
									<s:property value="#totalRefund" />
								</td>
								<td align="left">
									<s:property value="#totalPwt" />
								</td>
								<td align="left">
									<s:property value="#totalAll" />
								</td>
							</tr>
					<s:else>
						<tr>
							<th align="center">
								No Records Found
							</th>
						</tr>
					</s:else>
				</table>
			</div>
			</s:if>
		</s:else>
	</body>
</html>
<code id="headId" style="visibility: hidden">
	$RCSfile: bo_rep_retWiseSummeryTxnReport_Search.jsp,v $ $Revision:
	1.1.2.3.4.1 $
</code>