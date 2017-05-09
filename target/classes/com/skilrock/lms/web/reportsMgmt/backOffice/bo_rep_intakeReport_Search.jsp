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
				<s:if test="%{reportList neq null and reportList.size>0}">
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
					<s:hidden name="serviceName" value="%{serviceDispName}"/>
				</s:form>
			</div>
			<div class="box box-info box-style">
				<s:set name="netAmt" value="0.0" />
				<div class="box-body">
					<div class="col-md-3"></div>
					<s:if test="%{reportList neq null and reportList.size>0}">
						<s:set name="pwtAmt" value="0.0" />
						<!--<table width="75%" align="left" cellspacing="0" cellpadding="6" bordercolor="#CCCCCC" border="1" style="border-collapse: collapse;margin-left:120px;" class="sortable"> -->
						<div class="panel panel-default col-md-12 box-panel-style"
							id="tableDataDiv">
							<table id="dTableNew"
								class="common_data_table display display_table" cellspacing="0"
								aria-describedby="example_info" width="100%">
								<thead>
									<tr class="dpsh">
										<th>
											Transaction Date
										</th>
										<th>
											Retailer ID
										</th>
										<th>
											LMC ID
										</th>
										<th>
											Transaction Reference
										</th>
										<th>
											Type of Transaction
										</th>
										<th>
											Amount Credited
										</th>
										<th>
											POS sales Location
										</th>
										<th>
											Post Zone Location
										</th>
									</tr>
								</thead>
								<tbody>
									<s:iterator value="reportList" status="taskIndex">
										<tr>
											<s:set name="pwtAmt" value="%{pwtAmount}" />
											<td>
												<s:property value="transDate" />
											</td>
											<td>
												<s:property value="retOrgCode" />
											</td>
											<td>
												<s:property value="agtOrgCode" />
											</td>
											<td>
												Sent from FPFCC system
											</td>
											<td>
												Small Win Payment
											</td>
											<td><%=FormatNumber.formatNumberForJSP(pageContext
							.getAttribute("pwtAmt"))%></td>
											<td>
												<s:property value="city" />
											</td>
											<td>
												<s:property value="areaName" />
											</td>

										</tr>
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
	$RCSfile: bo_rep_intakeReport_Search.jsp,v $ $Revision: 1.3 $
</code>