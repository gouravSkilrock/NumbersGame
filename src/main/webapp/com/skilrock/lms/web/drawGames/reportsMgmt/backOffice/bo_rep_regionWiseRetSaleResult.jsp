<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@page import="com.skilrock.lms.dge.beans.ReportBeanDrawModule"%>
<%@page import="com.skilrock.lms.dge.beans.ReportDrawBean"%>
<%@page import="com.skilrock.lms.dge.beans.ReportGameBean"%>
<%@page import="com.skilrock.lms.common.utility.FormatNumber"%>
<%
	//response.setDateHeader("Expires", System.currentTimeMillis() + 10
	//	* 24 * 60 * 60 * 1000);
%>
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
			content="text/html; charset=UTF-8" />
		<link rel="stylesheet"
			href="<%=request.getContextPath()%>/LMSImages/css/styles.css"
			type="text/css" />
		<title><%=application.getAttribute("JSP_PAGE_TITLE")%></title>
	</head>

	<body>

		<div id="wrap">
			<div id="top_form">
			<s:actionerror/>	

			
					
					<s:set name="dataBeanMap" value="regionDataMap"/>
					<s:if test="#dataBeanMap.size>0">
					<div align="left">
				<s:form action="exportReportAsExcel.action" method="post"
					enctype="multipart/form-data">
					<s:hidden id="tableValue" name="reportData" />
					<s:hidden name="reportName" value="Region  wise Draw Report" />
					<s:submit key="btn.exportasexcel"
						cssStyle="margin-right:-400px;font-size:15px" onclick="getData();" />
				</s:form>
			</div>
				<div id="tableDataDiv">
				<table align="center">
			<tr>
			<td><h3><u> <s:text name="label.ret.sale.rep.state.wise"/> <s:text name="label.from.date"/>: <s:property value="start_date"/> <s:text name="label.to.date"/>: <s:property value="end_Date"/> </u></h3></td>
			</tr>
			</table>
					<table width="100%" border="1" cellpadding="3" cellspacing="0"	bordercolor="#CCCCCC" align="center">
					<tr>
					
						<th colspan="5" align="center">
						<s:text name="label.ret.sale.rep.state.wise"/>
						
						</th>
				
						<th align="center"><s:text name="label.amt.in"/> <s:property value="#application.CURRENCY_SYMBOL" /></th>
					</tr>
					<tr>
						<th ><s:text name="label.sNo"/></th>
						<th ><s:text name="Retailer"/> <s:text name="label.id"/></th>
						<th ><s:text name="Agent"/> <s:text name="label.id"/></th>
						<th ><s:text name="label.state"/></th>
						<th ><s:text name="label.city"/></th>
						<th ><s:text name="label.sale.amt"/></th>
					</tr>
					<!-- Initialize Variales For Table Data Total -->
						<s:set name="totalSale" value="0" />
					<s:set name="sn" value="0" />
					
				
				
					<s:set name="dataBean" id="dataBean" value="#dataBeanMap" />
							<s:iterator id="beanCart" value="%{#dataBean}">
						
								<tr class="startSortable">
								<td>
								<s:set name="sn" value="%{#sn +1}" />
								<s:property value="#sn"/>
								</td>
									<td align="left">
								
							<s:property value="value.orgName" />
						
										
									</td>
									<td align="left">
								
							<s:property value="value.parentOrgName" />
						
										
									</td>
									<td align="left">
								
							<s:property value="value.stateName" />
						
										
									</td>
									<td align="left">
								
							<s:property value="value.cityName" />
						
										
									</td>
								<td align="right">

										<s:set name="saleAmt" value="value.saleAmt" />
										<%=FormatNumber.formatNumberForJSP(pageContext
								.getAttribute("saleAmt"))%>
										<s:set name="totalSale" value="%{#totalSale +value.saleAmt}" />
									</td>
						
									
									
									
									
								</tr>
						
						</s:iterator>
						<tr>
						
								<td colspan="5"  align="center">
										<s:text name="TOTAL"/>
									</td>	
									
			
					<td align="right">
									<%=FormatNumber.formatNumberForJSP(pageContext
						.getAttribute("totalSale"))%>
									</td>
									
									
					
					
									
									
									
						</tr>
							</table>
							</div>
					</s:if>
					<s:else>
						<tr>
							<td colspan="6" align="center">
								<s:text name="msg.no.record.process"/>
							</td>
						</tr>
					</s:else>
			




			</div>
		</div>



	</body>
	
</html>
<code id="headId" style="visibility: hidden">
$RCSfile: bo_rep_regionWiseRetSaleResult.jsp,v $
$Revision: 1.3 $
</code>
