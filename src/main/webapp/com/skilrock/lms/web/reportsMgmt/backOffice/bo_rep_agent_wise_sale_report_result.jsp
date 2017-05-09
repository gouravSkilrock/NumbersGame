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
    <link rel="stylesheet" href="<%=request.getContextPath()%>/LMSImages/css/styles.css" type="text/css" />
	<link rel="stylesheet" href="<%=request.getContextPath()%>/LMSImages/css/demo_table_jui.css" type="text/css">
	<link rel="stylesheet" href="<%=request.getContextPath()%>/LMSImages/css/datatable-common.css" type="text/css">
   </head>
  
  <body>
 <div class="container-new">
  <div align="left" class="box-header box-header-style">
	<i class="fa fa-gear"></i><h3 id="table_header" class="box-title" style="font-size: 1.4em;"></h3>
  </div>
  <div align="left">
		</div>
				<div class="box box-info box-style">
					<div align="left">
					<s:hidden id="colCount" value="5"></s:hidden>
					<s:hidden id="rowCount" value="1"></s:hidden>
					<s:hidden id="leaveCount" value="1"></s:hidden>
	  	  				<s:if test="agentWiseSaleMap.size>0">
	  	  					<img src="<%=request.getContextPath()%>/LMSImages/images/pdf-icon.png" width="30" title="<s:text name="label.exportaspdf"/>" id="expPdf" height="30" alt="PDF Download" align="right" />
	  	  					<img src="<%=request.getContextPath()%>/LMSImages/images/excel-icon.png" width="30" title="<s:text name="btn.exportasexcel"/>" id="expExcel" height="30" alt="EXCEL Download" align="right" hspace="10"/>
	  	  					<s:form id="excelForm" action="expToExcel" method="post" enctype="multipart/form-data" namespace="/com/skilrock/lms/web/reportsMgmt">
								<s:hidden id="tableData" name="exportData"></s:hidden>
							</s:form>	
							<s:form id="pdfForm" action="expToPDF" method="post" enctype="multipart/form-data" namespace="/com/skilrock/lms/web/reportsMgmt">
								<s:hidden id="tableDataPDF" name="exportData"></s:hidden>
							</s:form>	
	  	  				</s:if>
					</div>
					<div class="box-body" >
						<div class="col-md-3"></div>
							<s:if test="agentWiseSaleMap.size>0">
								<div class="panel panel-default col-md-12 box-panel-style" id="tableDataDiv">									
									<table id="dTable" class="common_data_table display display_table" cellspacing="0" aria-describedby="example_info" width="100%">
										<thead>
											<tr class="dpsh">
												<th><s:text name="label.agency.name"/></th>
												<th><s:text name="label.mrp.sale"/></th>
												<th><s:text name="label.govt.comm"/></th>
												<th><s:text name="Commision"/></th>
												<th><s:text name="label.levy.amt"/></th>
												<th><s:text name="label.net.comm"/></th>
												<th><s:text name="label.signature"/></th>
											</tr>
										</thead>
										<tbody>
											<s:iterator  id="beanCart" value="agentWiseSaleMap">
												<tr>
													<td align="left"><s:property value="%{value.agentName}"/></td>
													<td align="right" class="amount-format">
														<s:set name="mrp" value="%{value.mrpAmt}" />
														<%=FormatNumber.formatToTwoDecimal(pageContext.getAttribute("mrp"))%>
													</td>
													<td align="right" class="amount-format">
														<s:set name="govtComm" value="%{value.govtComm}" />
														<%=FormatNumber.formatToTwoDecimal(pageContext.getAttribute("govtComm"))%>
													</td>
													<td align="right" class="amount-format">
														<s:set name="agentComm" value="%{value.agentComm}" />
														<%=FormatNumber.formatToTwoDecimal(pageContext.getAttribute("agentComm"))%>
													
													</td>
													<td align="right" class="amount-format">
														<s:set name="agentLevyComm" value="%{value.agentLevyComm}" />
														<%=FormatNumber.formatToTwoDecimal(pageContext.getAttribute("agentLevyComm"))%>
													</td>	
													<td align="right" class="amount-format">
														<s:set name="netComm" value="%{value.agentComm-value.agentLevyComm}" />
														<%=FormatNumber.formatToTwoDecimal(pageContext.getAttribute("netComm"))%>
													 </td>
													<td align="right"> </td>
												</tr>								
											</s:iterator>
										</tbody>
										<tfoot>
											<tr>
											    <th colspan="5" align="center"><s:text name="label.total"/></th>
												<th align="right"></th>
										   		<th></th>
											</tr>
										</tfoot>
									</table>
								</div>
							</s:if>
							<s:else>
								<div class="alert alert-warning col-md-6" style="margin-bottom:13px"><center><b><s:text name="msg.no.record.process" /> !!</b></center></div>
							</s:else>									
						</div>
					</div>
				
		</div>
</body>
</html>
<code id="headId" style="visibility: hidden">
	$RCSfile: bo_rep_agent_wise_sale_report_result.jsp,v $ $Revision: 1.3 $
</code>
