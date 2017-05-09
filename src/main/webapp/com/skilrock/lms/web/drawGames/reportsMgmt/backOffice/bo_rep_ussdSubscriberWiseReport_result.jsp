<%@page import="com.skilrock.lms.beans.DateBeans"%>
<%@page import="com.skilrock.lms.common.utility.GetDate"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.List"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.skilrock.lms.common.utility.FormatNumber"%>
<%@page import="com.skilrock.lms.dge.beans.MtnCustomerCenterBean"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html>
	<%
		response.setHeader("Cache-Control", "no-store"); //HTTP 1.1
		response.setHeader("Pragma", "no-cache"); //HTTP 1.0
		response.setDateHeader("Expires", 0); //prevents caching at the proxy server
	%>
	<head>
		<s:head theme="ajax" />
	</head>
	<body>
		<br />
			<s:if test="%{message neq null && message neq ''}">
			 <s:text name="label.select.st.date.after"/>  <s:property value="message"/>
			</s:if>
			<s:else>
		<div align="center">
	
			<h3>
				<u><s:text name="label.ussd.subscriber.wise.report"/> From <s:property
						value="startDate" /> To <s:property value="endDate" /> </u>
			</h3>
			<s:if test="mtnCustomerCenterBeans.size()>0">

			<s:form action="bo_rep_ussdSubscriberWiseReport_exportToExcel" method="post"
				enctype="multipart/form-data">
				<s:hidden id="tableValue" name="reportData" />
				<s:hidden name="reportName" value="USSD Subscriber Wise Report" />
				<s:submit key="btn.exportasexcel" 
					cssStyle="font-size:11px; text-align:center"
					onclick="createTableData();" />
			</s:form>
			
		</s:if>
		</div>
		<div id="tableDataDiv" >
		<s:if test="mtnCustomerCenterBeans.size>0">
			<table id="dataTable" style='border-collapse: collapse;border-color: #737373; <s:if test="%{mobileType eq 'ALL'}">display: none;</s:if><s:else>display: block;</s:else>' width="100%" border="1" cellpadding="6"
				cellspacing="0" bordercolor="#CCCCCC" align="center" class="TFtable">
				
					<tr>
						<th rowspan="2" style="background: #cecece">
							S.No
						</th>
						<s:if test="%{mobileType eq 'ALL'}">
						<th rowspan="2" style="background: #cecece">
							Mobile Number
							</th>
						</s:if>
						<th colspan="6" style="background: #d9d9d9">
							Sale
						</th>
						<th colspan="4" style="background: #d9d9d9">
							Winning
						</th>
					</tr>
					<tr style="background: #cecece">
						<th>
							Transaction Date
						</th>
						<th>
							Game Name
						</th>
						<th>
							Draw Name
						</th>
						<th>
							Ticket Number
						</th>
						<th>
							Amount
						</th>
						<th>
							Wallet
						</th>
						<th>
							Winning Amount
						</th>
						<th>
							Claimed Date
						</th>
						<th>
							Wallet
						</th>
						<th>
							Status
						</th>
					</tr>
					<tr>
						<s:set name="totalSaleAmt" value="0" />
						<s:set name="totalWinAmt" value="0"/>
						<s:iterator id="beanCart" value="mtnCustomerCenterBeans" status="indx">
							<tr>
								<td rowspan='<s:property value="transactionMap.size"/>'>
									<s:property value="#indx.index+1" />
								</td>
								<s:if test="%{mobileType eq 'ALL'}">
								<td rowspan='<s:property value="transactionMap.size"/>'>
									<s:property value="mobileNbr" />
								</td>
								</s:if>
								<td rowspan='<s:property value="transactionMap.size"/>'>
									<s:property value="saleTransDate" />
								</td>
								<td rowspan='<s:property value="transactionMap.size"/>'>
									<s:property value="gameName" />
								</td>
								<td rowspan='<s:property value="transactionMap.size"/>'>
									<s:property value="drawName" />
								</td>
								<td rowspan='<s:property value="transactionMap.size"/>'>
									<s:property value="ticketNumber" />
								</td>
								<td rowspan='<s:property value="transactionMap.size"/>'>
									<s:property value="saleAmt" />
								</td>
					       		 <s:set name="totalSaleAmt" value="#totalSaleAmt + saleAmt" />
								<td rowspan='<s:property value="transactionMap.size"/>'>
									<s:property value="saleWallet" />
								</td>
								<s:iterator value="transactionMap">
									<td>
										<s:property value="value.winAmt" />
									</td>
									<s:set name="totalWinAmt" value="#totalWinAmt+value.winAmt"/>
									<td>
										<s:property value="value.winTransDate" />
									</td>
									<td>
										<s:property value="value.winWallet" />
									</td>
									<td>
										<s:property value="value.status" />
									</td>
									</tr>
								</s:iterator>
							
						</s:iterator>
					<tr>
						<td colspan='<s:if test="%{mobileType eq 'ALL'}">6</s:if><s:else>5</s:else>'>
							Total
						</td>
						
						<td>
							<%=FormatNumber.formatNumberForJSP(pageContext.getAttribute("totalSaleAmt")) %>
						</td>
						<td>
						</td>
						<td>
							<%=FormatNumber.formatNumberForJSP(pageContext.getAttribute("totalWinAmt")) %>
						</td>
					
					</tr>
					</tr>
				</table>		
			</s:if>
			<s:else>
				<font size="3px"><b>	No Records to Process</b></font>
			</s:else>
		</div></s:else>
	</body>
</html>

<code id="headId" style="visibility: hidden">
	$RCSfile: bo_rep_ussdSubscriberWiseReport_result.jsp,v $ $Revision: 1.3 $
</code>