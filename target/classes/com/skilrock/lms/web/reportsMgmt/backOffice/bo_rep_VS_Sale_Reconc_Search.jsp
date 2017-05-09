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
				<s:if test="%{reconcDataMap neq null and reconcDataMap.size>0}">
					<img
						src="<%=request.getContextPath()%>/LMSImages/images/excel-icon.png"
						width="30" title="<s:text name="btn.exportasexcel"/>"
						id="vsSaleReconExpExcel" height="30" alt="EXCEL Download"
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
					<s:if test="reconcDataMap.size>0">
						<s:set name="pwtAmt" value="0.0" />
						<!--<table width="75%" align="left" cellspacing="0" cellpadding="6" bordercolor="#CCCCCC" border="1" style="border-collapse: collapse;margin-left:120px;" class="sortable"> -->
						<div class="panel panel-default col-md-12 box-panel-style"
							id="tableDataDiv">
							<s:form name="myForm" action=""
							theme="simple" onsubmit="getCheckBoxValues()">
							<table id="dTableNew"
								class="common_data_table display display_table" cellspacing="0"
								aria-describedby="example_info" width="100%">
								<thead>
									<tr  class="dpsh">
									<th>
										S. No.
									</th>
									<th>
										Ticket Number
									</th>
									<th>
										Transaction Date
									</th>
									<th>
										LMS Txn ID
									</th>
									<th>
										GR Txn ID
									</th>
									<th>
										LMS Status
									</th>
									<th>
										GR Status
									</th>
									<th>
										Amount
									</th>
									<s:if test = "%{status == 'PENDING'}">
									<th></th>
									</s:if>
								</tr>
								</thead>
								<tbody>
									<s:iterator value="reconcDataMap" status="index">
									<tr>
										<td>
											<s:property value="%{#index.count}" />
										</td>
										<td id="ticketNumber"><s:property value="%{value.ticketNumber}" /></td>
										<td>
											<s:property value="%{value.transactionDate.toString().replace('.0','')}" id="transDate" />
										</td>
										<td>
											<s:property value="%{value.lmsTxnId}" id="lmsTxnId" />
										</td>
										<td id="txnId"><s:property value="%{value.txnId}"/></td>
										<td>
											<s:property value="%{value.lmsStatus}" id="lmsStatus" />
										</td>
										<td>
											<s:property value="%{value.grStatus}" id="grStatus" />
										</td>
										<td id="mrpAmt"><s:property value="%{value.mrpAmt}"/></td>
										<s:if test = "%{status == 'PENDING'}">
										<td>
											<s:checkbox name="data" id="data" fieldValue="%{value}" />
										</td></s:if>
										  <s:hidden value="%{value.userId}" id="userId" />  <s:hidden value="%{value.gameId}" id="gameId" />
									</tr>
								</s:iterator>
								<s:if test = "%{status == 'PENDING'}">
								<tr>
								<td colspan="9" align="center"><input type="submit" value="Settle Transactions" class="button"/> </td>
								</tr>
								</s:if>
								</tbody>
								<tfoot>
								</tfoot>
							</table>
							</s:form>
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
	$RCSfile: bo_rep_VS_Sale_Reconc_Search.jsp,v $ $Revision: 1.3 $
</code>