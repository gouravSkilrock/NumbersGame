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
			content="text/html; charset=iso-8859-1" />
		<link rel="stylesheet"
			href="<%=request.getContextPath()%>/LMSImages/css/styles.css"
			type="text/css" />
		<title><%=application.getAttribute("JSP_PAGE_TITLE")%></title>
	</head>

	<body>

		<div id="wrap">
			<div id="top_form">
			<s:actionerror/>	

			
			
					<s:set name="dataBeanMap" value="agentDataMap"/>
					 <s:set name="gameMap" value="gameMap" />
					<s:if test="#dataBeanMap.size>0">
					<div align="left">
				<s:form action="exportReportAsExcel.action" method="post"
					enctype="multipart/form-data">
					<s:hidden id="tableValue" name="reportData" />
					<s:hidden name="reportName" value="Slot Sale Report" />
					<s:submit value="Export As Excel"
						cssStyle="margin-right:-400px;font-size:15px" onclick="getData();" />
				</s:form>
			</div>
				<div id="tableDataDiv">
				<table align="center">
			<tr>
			<td><h3><u>  <s:text name="Agent"/> Wise Slot Sale Report from Date: <s:property value="startDate"/> to Date: <s:property value="endDate"/> </u></h3></td>
			</tr>
			</table>
					<table width="100%" border="1" cellpadding="3" cellspacing="0"	bordercolor="#CCCCCC" align="center">
						
					
					
				
					<tr>
					
					
						<th  colspan="2"  align="center">Amount in <s:property value="#application.CURRENCY_SYMBOL" /></th>
					
						
						<s:set name="map" id="map" value="#gameMap" />
							<s:iterator id="Cart" value="%{#map}">
							<th colspan="2">		<s:property value="value" /></th>
							
							</s:iterator>
					
					</tr>
					<tr>
						<th >SNo.</th>
						<th ><s:text name="Agent"/>Name</th>
							<s:set name="map" id="map" value="#gameMap" />
							<s:iterator id="Cart" value="%{#map}">
							<th >Mrp Sale Amount</th>
							<th >Time Slotted Sale Amount</th>
						</s:iterator>
						
					
					</tr>
					<!-- Initialize Variales For Table Data Total -->
	
				<s:set name="sn" value="0" />
					<s:set name="dataBean" id="dataBean" value="#dataBeanMap" />
							<s:iterator id="beanCart" value="%{#dataBean}">
							<s:set name="name" value="key"  ></s:set>
						<s:if test="%{#name!=-1}">
								<tr class="startSortable">
								<td>
								<s:set name="sn" value="%{#sn +1}" />
								<s:property value="#sn"/>
								
								</td>
									<td align="left">
								
								<s:property value="value.agentName" />
						
										
									</td> 
											<s:set name="agt" id="agt" value="value.gameBeanMap" />
											<s:set name="saleMap" id="saleMap" value="#agt" />
												<s:if test="#agt.size>0">
									<s:iterator id="saleCart" value="%{#saleMap}">
										
								
										<td align="right"><s:set name="saleAmt" value="value.drawSale" />
										<%= FormatNumber.formatNumberForJSP(pageContext.getAttribute("saleAmt"))%></td>
										<td align="right">
											
										<s:set name="slotSale" value="value.drawSlotSale" />
										<%= FormatNumber.formatNumberForJSP(pageContext.getAttribute("slotSale"))%>
									
										</td>
								
									</s:iterator>
									</s:if>
									<s:else>
										<s:iterator id="Cart" value="%{#map}">
											<td align="right"><s:set name="saleAmt" value="0.00" />
										<%= FormatNumber.formatNumberForJSP(pageContext.getAttribute("saleAmt"))%></td>
										<td align="right">

										<s:set name="slotSale" value="0.00" />
										<%= FormatNumber.formatNumberForJSP(pageContext.getAttribute("slotSale"))%>
										<!--<s:set name="totalSale" value="%{#totalSale +value.saleAmt}" />
									--></td>
							
										</s:iterator>
									</s:else>
									
									
									
								</tr>
						</s:if>
						<s:else>
						<tr>
						
								<td colspan="2"  align="center">
										TOTAL
									</td>
									
									<s:set name="agt" id="agt" value="value.gameBeanMap" />
											<s:set name="saleMap" id="saleMap" value="#agt" />
												<s:if test="#agt.size>0">
									<s:iterator id="saleCart" value="%{#saleMap}">
										
									
										<td align="right"><s:set name="saleAmt" value="value.drawSale" />
										<%= FormatNumber.formatNumberForJSP(pageContext.getAttribute("saleAmt"))%></td>
										<td align="right">
												
										<s:set name="slotSale" value="value.drawSlotSale" />
										<%= FormatNumber.formatNumberForJSP(pageContext.getAttribute("slotSale"))%>
									
										</td>
						
									</s:iterator>
									</s:if>
									<s:else>
										<s:iterator id="Cart" value="%{#map}">
											<td align="right"><s:set name="saleAmt" value="0.00" />
										<%= FormatNumber.formatNumberForJSP(pageContext.getAttribute("saleAmt"))%></td>
										<td align="right">

										<s:set name="slotSale" value="0.00" />
										<%= FormatNumber.formatNumberForJSP(pageContext.getAttribute("slotSale"))%>
										<!--<s:set name="totalSale" value="%{#totalSale +value.saleAmt}" />
									--></td>	
										</s:iterator>
										</s:else>
									</tr>
									
						</s:else>
						</s:iterator>
						
							</table>
							</div>
					</s:if>
					<s:else>
						<tr>
							<td colspan="6" align="center">
								No Records to Process
							</td>
						</tr>
					</s:else>
			




			</div>
		</div>



	</body>
	
</html>
<code id="headId" style="visibility: hidden">
$RCSfile: bo_rep_customSlotSaleResult.jsp,v $
$Revision: 1.3 $
</code>
