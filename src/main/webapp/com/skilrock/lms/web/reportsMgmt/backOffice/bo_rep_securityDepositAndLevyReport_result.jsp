<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title><%=application.getAttribute("JSP_PAGE_TITLE")%></title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
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
			
		<div class="box box-info box-style">
			<div align="left">
	  	  	<s:if test="securityDepostAndLevyBeanList.size>0">
	  	  		<img src="<%=request.getContextPath()%>/LMSImages/images/pdf-icon.png" width="30" title="<s:text name="label.exportaspdf"/>" id="expPdf" height="30" alt="PDF Download" align="right" />
	  	  		<img src="<%=request.getContextPath()%>/LMSImages/images/excel-icon.png" width="30" title="<s:text name="btn.exportasexcel"/>" id="expExcel" height="30" alt="EXCEL Download" align="right" hspace="10" />
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
					<s:if test="securityDepostAndLevyBeanList.size>0">
						<div class="panel panel-default col-md-12 box-panel-style">									
							<table id="dTable" class="common_data_table display display_table" cellspacing="0" aria-describedby="example_info" width="100%">
								<thead>
									<tr class="dpsh">
										<th align="center"><s:text name="label.agt.name"/></th>
										<th align="center"><s:text name="label.ret.name"/></th>
										<th align="center"><s:text name="label.initial.sec.depo"/></th>
										<th align="center"><s:text name="label.expected.sec.depo"/></th>
										<th align="center"><s:text name="label.collected.sec.depo"/></th>
										<th align="center"><s:text name="label.remaining.sec.depo"/></th>
										<th align="center"><s:text name="label.levy.rate"/></th>
									</tr>
								</thead>
								<tbody>
									<s:iterator id="beanCart" value="securityDepostAndLevyBeanList">
										<tr>
											<td align="left"><s:property value="agentName" /></td>
											<td align="left"><s:property value="retailerName" /></td>
											<td align="right" class="amount-format"><s:property value="initialSD" /></td>
											<td align="right" class="amount-format"><s:property value="expectedSD" /></td>
											<td align="right" class="amount-format"><s:property value="collectedSD" /></td>
											<td align="right" class="amount-format"><s:property value="remainingSD" /></td>
											<td align="right"><s:property value="levyRate" /></td>
										</tr>										
									</s:iterator>
								</tbody>
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
	$RCSfile: bo_rep_securityDepositAndLevyReport_result.jsp,v $ $Revision: 1.3 $
</code>
