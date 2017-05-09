<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="com.skilrock.lms.common.utility.FormatNumber"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
<title><%=application.getAttribute("JSP_PAGE_TITLE")%></title>	
	

	
</head>
		<body>


			<s:set name="dataBeanList" value="bankRepDataBeanList"/>
			<s:if test="#dataBeanList.size>0">
			<div align="left">
				<s:form action="exportReportAsExcel.action" method="post"
					enctype="multipart/form-data">
					<s:hidden id="tableValue" name="reportData" />
					<s:hidden name="reportName" value="CashierWiseTransactionReport" />
					<s:submit value="Export As Excel"
						cssStyle="margin-right:-400px;font-size:15px" onclick="getData();" />
				</s:form>
			</div>
			<div id="tableDataDiv">
			<table align="center">
			<tr>
			<td><h3><u> Cashier Wise Transaction <s:text name="Report"></s:text>  From Date: <s:property value="startDate"/> To Date: <s:property value="endDate"/></u></h3></td>
			</tr>
			</table>
				<table width="100%" border="1" cellpadding="3" cellspacing="0"
					bordercolor="#CCCCCC" align="center">
					
					<tr>
						<th >
							Bank Name
						</th>
						<th >
							
							
							Branch Name
						</th>
						<th >
							
							
							Transaction Ref
						</th>
						<th >
							
							
							Transaction Date
						</th>
						<th >
							Cashier
						</th>
							<s:if test="repType!='Payment'">
						<th >
							
							
							LMC  Name
						</th>
						<th >
							
							
							LMC Id
						</th>	
						<th >
							
							
							Terminal Id
						</th>	
						</s:if>
						<th >
							
							
							Transaction Type
						</th>
						<th >
							Amount 
						</th>
					</tr>
					<s:set name="netAmount" value="0.0" ></s:set>
					<s:set name="netPayemnt" value="0.0" ></s:set>
					<s:set name="netCredit" value="0.0" ></s:set>
				
					<s:iterator value="%{#dataBeanList}">
					
							<tr class="startSortable">
								
								<td align="left">
								<s:property value="bankFullName" />&nbsp;	(<s:property value="bankName" />)
								</td>
								<td align="left">
						<s:property value="branchFullName" />&nbsp;	(<s:property value="branchName" />)
								</td>
								<td align="left">
									<s:property value="refId" />
								</td>
								<td align="right">
									<s:property value="trnDate" />
								</td>
								<td align="center">
									<s:property value="cashierName" />
								</td>
								<s:if test="repType!='Payment'">
								<td align="center">
									<s:property value="agentName" />
								</td>
									<td align="center">
									<s:property value="agentCode" />
								</td>
								<td align="center">
									<s:property value="terminalId" />
								</td>
								</s:if>
								<td align="center">
									<s:property value="trnType" />
								</td>
								<td align="right">
								<s:if test="repType!='Payment'">
								<s:set name="netCredit" value="#netCredit+totalAmount" ></s:set>
								
								</s:if>
								<s:else>
									<s:set name="netPayemnt" value="#netPayemnt+totalAmount" ></s:set>
								
								</s:else>
								<s:if test="trnType=='Payment'">
								<s:set name="netAmount" value="#netAmount-totalAmount" />
								</s:if>
								<s:else>
								<s:set name="netAmount" value="#netAmount+totalAmount" />
								</s:else>
								<s:property value="totalAmount" />
								</td>
										
										
							</tr>
						
					</s:iterator>
						<tr>
							<s:if test="repType!='Payment'">
							<td colspan="9">Total</td>
							</s:if>
							<s:else>
							<td colspan="6">Total</td>
							</s:else>
							<td align="right">	
								<s:if test="repType=='Payment'">
									<s:set name="netAmount" value="#netPayemnt" />
								</s:if>
								<s:if test="repType=='Credit'">
									<s:set name="netAmount" value="#netCredit" />
								</s:if>
											
							
							<%=FormatNumber.formatNumberForJSP(pageContext
															.getAttribute("netAmount"))%></td>
						</tr>
				

				</table>
					</div>	
		</s:if>
			<s:else>
						<table  align="center"><tr><td>
								No Records to Process
								</td></tr>
						</table>
			</s:else>
	
		</body>
</html>		