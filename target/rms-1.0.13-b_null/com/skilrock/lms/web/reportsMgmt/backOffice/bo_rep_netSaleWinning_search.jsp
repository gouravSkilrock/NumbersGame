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
	 
    <link rel="stylesheet" href="<%=request.getContextPath()%>/LMSImages/css/styles.css" type="text/css" />
	<link rel="stylesheet" href="<%=request.getContextPath()%>/LMSImages/css/demo_table_jui.css" type="text/css">
	<link rel="stylesheet" href="<%=request.getContextPath()%>/LMSImages/css/datatable-common.css" type="text/css">
  	</head>

	<body>
	
	<div class="container-new">
  	  	<div align="left">
	  	  	<s:if test="netSaleWinDataMap.size>0">
	  	  		<img src="<%=request.getContextPath()%>/LMSImages/images/pdf-icon.png" width="30" title="<s:text name="label.exportaspdf"/>" id="netSaleWinExpPdf" height="30" alt="PDF Download" align="right" />
	  	  		<img src="<%=request.getContextPath()%>/LMSImages/images/excel-icon.png" width="30" title="<s:text name="btn.exportasexcel"/>" id="nSaleWinExpExcel" height="30" alt="EXCEL Download" align="right" hspace="10"/>
	  	  	</s:if>
			<s:form id="excelForm" action="expToExcel" method="post" enctype="multipart/form-data" namespace="/com/skilrock/lms/web/reportsMgmt">
				<s:hidden id="tableData" name="exportData"></s:hidden>
			</s:form>
			<s:form id="pdfForm" action="expToPDF" method="post" enctype="multipart/form-data" namespace="/com/skilrock/lms/web/reportsMgmt">
				<s:hidden id="tableDataPDF" name="exportData"></s:hidden>
			</s:form>
		</div>

		<div class="box box-info box-style">
  			<s:set name="netAmt" value="0.0"/>
			<div class="box-body">
				<div class="col-md-3"></div>
				<s:if test="netSaleWinDataMap.size>0">
				<s:if test="%{reportType.equalsIgnoreCase('RETAILER')}">
					<s:hidden id="colCount" value="7"></s:hidden>
					<s:hidden id="rowCount" value="1"></s:hidden>
				</s:if>
				<s:else>
					<s:hidden id="colCount" value="6"></s:hidden>
					<s:hidden id="rowCount" value="1"></s:hidden>
				</s:else>
					
					<div class="panel panel-default col-md-12 box-panel-style"
						id="tableDataDiv">
						<table id="net_sale_dTable" class="common_data_table display display_table" cellspacing="0" aria-describedby="example_info" width="100%">
							<thead>
								<tr class="dpsh">
									<s:if test="%{reportType.equalsIgnoreCase('RETAILER')}" >
										<th align="center">
											<s:text name="label.agencyName" />
										</th>
									</s:if>
									<s:if test="%{agentOrgId == -1 && reportType.equalsIgnoreCase('AGENT')}">
										<th align="center">
											<s:text name="label.agency.name" />
										</th>
									</s:if>
									<s:else>
										<th align="center">
											<s:text name="label.ret.name" />
										</th>									
									</s:else>
									<th align="center">
										<s:text name="label.mrp.name" />
									</th>
									<th align="center">
										<s:text name="label.win.paid" />
									</th>
									<th align="center">
										<s:text name="Commision" />
									</th>
									<th align="center">
										<s:text name="label.secur.dpst" />
									</th>
									<th align="center">
										<s:text name="label.levy.amt" />
									</th>
									<th align="center">
										<s:text name="label.net.amt" />
									</th>
									<th>
										<s:text name="label.sign" />
									</th>
								</tr>
							</thead>
							<tbody>
								<s:iterator id="beanCart" value="netSaleWinDataMap">
									<tr>
										<s:if test="%{reportType.equalsIgnoreCase('RETAILER')}" >
											<td align="center">
												<s:property value="value.orgName" />
											</td>
										</s:if>
										<td align="center">
											<s:property value="key" />
										</td>
										<td align="right" class="amount-format">
											<s:set name="dgMrpSale" value="%{value.dgMrpSale}" />
											<%=FormatNumber.formatNumberForJSP(pageContext.getAttribute("dgMrpSale")) %>
										</td>
										<td align="right" class="amount-format">
											<s:set name="dgPwt" value="%{value.dgPwt}" />
											<%=FormatNumber.formatNumberForJSP(pageContext.getAttribute("dgPwt")) %>
										</td>
										<td align="right" class="amount-format">
											<s:set name="dgCommAmt" value="%{value.dgCommAmt}" />
											<%=FormatNumber.formatNumberForJSP(pageContext.getAttribute("dgCommAmt")) %>
										</td>
										<td align="right" class="amount-format">
											<s:set name="dgSDAmt" value="%{value.dgSDAmt}" />
											<%=FormatNumber.formatNumberForJSP(pageContext.getAttribute("dgSDAmt")) %>
										</td>
										<td align="right" class="amount-format">
											<s:set name="dgLeviAmt" value="%{value.dgLeviAmt}" />
											<%=FormatNumber.formatNumberForJSP(pageContext.getAttribute("dgLeviAmt")) %>
										</td>
										<td align="right" class="amount-format">
											<s:set name="dgNetSale" value="%{value.dgNetSale}" />
											<%=FormatNumber.formatNumberForJSP(pageContext.getAttribute("dgNetSale")) %>
										    <s:set name="netAmt" value="#netAmt+value.dgNetSale" />
										</td>
										<td align="center">
											
										</td>
									</tr>
								</s:iterator>
							</tbody>
							<tfoot>
								<tr>
								   <s:if test="%{reportType.equalsIgnoreCase('RETAILER')}" >
									   <th colspan="7" align="center"><s:text name="label.total"/></th>
								   </s:if>
								   <s:else>
								       <th colspan="6" align="center"><s:text name="label.total"/></th>
								   </s:else>
									<th align="right"></th>
								    <th></th>
								</tr>
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
	$RCSfile: bo_rep_netSaleWinning_search.jsp,v $ $Revision: 1.3 $
</code>