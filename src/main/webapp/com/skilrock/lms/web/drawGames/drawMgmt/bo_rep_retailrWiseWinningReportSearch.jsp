<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@page import="com.skilrock.lms.common.utility.FormatNumber"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="pragma" content="no-cache">
		<meta http-equiv="cache-control" content="no-cache">
		<meta http-equiv="expires" content="0">
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
				<img src="<%=request.getContextPath()%>/LMSImages/images/pdf-icon.png" width="30" title="<s:text name="label.exportaspdf"/>" id="expPdf" height="30" alt="PDF Download" align="right" />
				<img
					src="<%=request.getContextPath()%>/LMSImages/images/excel-icon.png"
					width="30" id="expExcel" height="30" alt="EXCEL Download"
					align="right" title="<s:text name="btn.exportasexcel"/>" hspace="10"/>
				<s:form id="excelForm" action="expToExcel" method="post"
					enctype="multipart/form-data"
					namespace="/com/skilrock/lms/web/reportsMgmt">
					<s:hidden id="tableData" name="exportData"></s:hidden>
				</s:form>
				<s:form id="pdfForm" action="expToPDF" method="post" enctype="multipart/form-data" namespace="/com/skilrock/lms/web/reportsMgmt">
					<s:hidden id="tableDataPDF" name="exportData"></s:hidden>
				</s:form>
			</div>

			<div class="box box-info box-style">
			<s:set name="netWinAmt" value="0.0"  />
				<div class="box-body">
				<s:hidden id="colCount" value="4"></s:hidden>
				<s:hidden id="rowCount" value="1"></s:hidden>
				<s:hidden id="leaveCount" value="4"></s:hidden>
					<div class="col-md-3"></div>
					<div class="panel panel-default col-md-12 box-panel-style"
						id="tableDataDiv">
						<table id="dTable" class="common_data_table display display_table" cellspacing="0"
							aria-describedby="example_info" width="100%">
							<thead>
								<tr class="dpsh">
									<th nowrap="nowrap">
										<s:text name="label.draw.detail" />
									</th>
									<th nowrap="nowrap">
										<s:text name="label.tckt.no" />
									</th>
									<th>
										<s:text name="label.agency.name" />
									</th>
									<th>
										<s:text name="label.sold.by" />
									</th>
									<th>
										<s:text name="label.win.amt" />
									</th>
									<th>
										<s:text name="label.paid" />
									</th>
									<th>
										<s:text name="label.paid.by"/>
									</th>
									<th>
										<s:text name="label.payment.date" />
									</th>
									<th>
										<s:text name="label.exp.date" />
									</th>
								</tr>
							</thead>
							<tbody>
								<s:iterator id="drawCart" value="%{drawBeanMap}">
									<s:set name="drawBean" value="%{value}"></s:set>
									<s:set name="TicketDeatlBeanList" value="#drawBean.winTicketInfoBean"></s:set>
									<s:iterator value="#TicketDeatlBeanList">
									<s:set name="netWinAmt" value="#netWinAmt+winAmt" />
										<tr>
											<td align="center">
												<s:property value="#drawBean.drawDateTime" />_<s:property value="#drawBean.drawName" />_<s:property value="#drawBean.eventId" />
											</td>
											<td align="center">
												<s:property value="ticketNo" /><s:property value="rpcCount"/>
											</td>
											<td align="center">
												<s:property value="%{parentOrgNameMap.get(purchasedByRet)}" />
											</td>
											<td align="center">
												<s:property value="%{orgCodeNameMap.get(purchasedByRet)}" />
											</td>
											<td align="right" class="amount-format">
												<s:property value="winAmt" />
											</td>
											<s:if test="%{isClaimed == 'YES'}">
												<s:if test="%{paymentDateMap.get(ticketNo)==null || paymentDateMap.get(ticketNo)==''}">
													<td align="center">
														<s:text name="In Process"></s:text>
													</td>
													<td align="left">
														<s:text name="In Process"></s:text>
													</td>
													<td align="center">
														<s:text name="In Process"></s:text>
													</td>
												</s:if>
												<s:else>
													<td align="center">
														<s:text name="YES"></s:text>
													</td>
													<td align="left" style="word-wrap: break-word">
														<s:property value="%{orgCodeNameMap.get(claimedByUser)}" />
													</td>
													<td align="center">												
														<s:property value="%{paymentDateMap.get(ticketNo)}" />
													</td>
												</s:else>
												
											</s:if>
											<td align="center">
												<s:property value="#drawBean.ticketExpDate" />
											</td>
										
											
											
										</tr>
									</s:iterator>
								</s:iterator>
			
							</tbody>
							<tfoot>
							<tr>
									<th colspan="4"><s:text name="label.total" /></th>
									<th align="right"></th>
									<th colspan="4"></th>
								</tr>
							</tfoot>
						</table>
					</div>
				</div>
			</div>
		</div>
	</body>
</html>
<code id="headId" style="visibility: hidden">
	$RCSfile: bo_rep_retailrWiseWinningReportSearch.jsp,v $ $Revision:
	1.1.2.2 $
</code>