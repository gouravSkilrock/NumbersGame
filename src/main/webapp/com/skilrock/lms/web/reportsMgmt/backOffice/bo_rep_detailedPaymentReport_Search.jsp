<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@page import="com.skilrock.lms.common.utility.FormatNumber"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	<head>
		<meta http-equiv="pragma" content="no-cache">
		<meta http-equiv="cache-control" content="no-cache">
		<meta http-equiv="expires" content="0">
		<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
		<link rel="stylesheet"
			href="<%=request.getContextPath()%>/LMSImages/css/styles.css"
			type="text/css" />
		<link rel="stylesheet"
			href="<%=request.getContextPath()%>/LMSImages/css/demo_table_jui.css"
			type="text/css">
		<link rel="stylesheet"
			href="<%=request.getContextPath()%>/LMSImages/css/datatable-common.css"
			type="text/css">
	</head>

	<body>
		<div class="container-new">
			<div align="left">
				<s:if test="%{reportData neq null and reportData.size>0}">
					<img
						src="<%=request.getContextPath()%>/LMSImages/images/excel-icon.png"
						width="30" title="<s:text name="btn.exportasexcel"/>"
						id="intakeReportExpExcel" height="30" alt="EXCEL Download"
						align="right" hspace="10" />
				</s:if>
				<s:form id="excelForm" action="expToExcel" method="post"
					enctype="multipart/form-data"
					namespace="/com/skilrock/lms/web/reportsMgmt">
					<s:hidden id="tableData" name="exportData"></s:hidden>
				</s:form>
			</div>

			<div class="box box-info box-style">
				<s:set name="netAmt" value="0.0" />
				<div class="box-body">
					<div class="col-md-3"></div>
					<s:if test="%{reportData neq null and reportData.size>0}">
						<s:set name="winAmt" value="0.0" />
						<s:set name="saleAmt" value="0.0" />
						<!--<table width="75%" align="left" cellspacing="0" cellpadding="6" bordercolor="#CCCCCC" border="1" style="border-collapse: collapse;margin-left:120px;" class="sortable"> -->
						<div class="panel panel-default col-md-12 box-panel-style"
							id="tableDataDiv">
							<table id="dTableNew"
								class="common_data_table display display_table" cellspacing="0"
								aria-describedby="example_info" width="100%">
								<thead>
									<tr class="dpsh">
										<s:if test="%{reportType=='ALL'}">
											<th>
												LMC ID
											</th>
											<th>
												Retailer ID
											</th>
											<th>
												Ticket Number
											</th>
											<th>
												Sale Date and Time
											</th>
											<th>
												Sales Amount
											</th>
											<th>
												Win Amount
											</th>
											<th>
												Product Name
											</th>
											<th>
												Date Prize Paid
											</th>
											<th>
												Event Number
											</th>
										</s:if>
										<s:else>
											<th>
												Number of Transactions
											</th>
											<th>
												Total Sales
											</th>
											<th>
												Total Prize Paid Out
											</th>
										</s:else>
									</tr>
								</thead>
								<tbody>
									<s:iterator value="reportData" status="taskIndex">
										<s:if test="%{reportType=='ALL'}">
											<tr>
												<s:set name="saleAmt" value="%{saleAmt}" />
												<s:set name="winAmt" value="%{winAmt}" />
												<td>
													<s:property value="agtOrgCode" />
												</td>
												<td>
													<s:property value="retOrgCode" />
												</td>
												<td>
													<s:property value="ticketNbr" />
												</td>
												<td>
													<s:property value="saleDateTime" />
												</td>
												<td><%=FormatNumber.formatNumberForJSP(pageContext
								.getAttribute("saleAmt"))%></td>
												<td><%=FormatNumber.formatNumberForJSP(pageContext
								.getAttribute("winAmt"))%></td>
												<td>
													<s:property value="drawName" />
												</td>
												<td>
													<s:property value="winTransDate" />
												</td>
												<td>
													<s:property value="eventId" />
												</td>
											</tr>
										</s:if>
										<s:else>
											<s:set name="totalSaleAmt" value="0.0" />
											<s:set name="claimedPwtAmt" value="0.0" />
											<s:set name="totalSaleAmt" value="%{totalSaleAmt}" />
											<s:set name="claimedPwtAmt" value="%{claimedPwtAmt}" />
											<tr>
												<td>
													<s:property value="totalTransactions" />
												</td>
												<td><%=FormatNumber.formatNumberForJSP(pageContext
								.getAttribute("totalSaleAmt"))%></td>
												<td><%=FormatNumber.formatNumberForJSP(pageContext
								.getAttribute("claimedPwtAmt"))%></td>

											</tr>
										</s:else>
									</s:iterator>
								</tbody>
								<tfoot>
								</tfoot>
							</table>
						</div>
					</s:if>
					<s:else>
						<div class="alert alert-warning col-md-6"
							style="margin-bottom: 13px">
							<center>
								<b><s:text name="msg.no.record.process" /> !!</b>
							</center>
						</div>
					</s:else>
				</div>
			</div>
		</div>
	</body>
</html>
<code id="headId" style="visibility: hidden">
	$RCSfile: bo_rep_detailedPaymentReport_Search.jsp,v $ $Revision:
	1.1.2.4 $
</code>