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
					<s:hidden name="reportName" value="BankWiseCreditUpdate" />
					<s:submit value="Export As Excel"
						cssStyle="margin-right:-400px;font-size:15px" onclick="getData();" />
				</s:form>
			</div>
						 
			<div id="tableDataDiv">
			<table align="center">
			<tr>
			<td><h3><u> Bank Wise Winning Update   Report From Date: <s:property value="startDate"/> To Date: <s:property value="endDate"/></u></h3></td>
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
							Amount 
						</th>
					</tr>
					<s:set name="netAmount" value="0.0"  />
				
					<s:iterator value="%{#dataBeanList}">
					
							<tr class="startSortable">
								
							<td align="left">
								<s:property value="bankFullName" />&nbsp;	(<s:property value="bankName" />)
								</td>
								<td align="left">
							<s:property value="branchFullName" />&nbsp;	(<s:property value="branchName" />)
								</td>
								
								<td align="right">
								<s:set name="netAmount"
										value="#netAmount+totalAmount" />
									<s:property value="totalAmount" />
								</td>
										
										
							</tr>
						
					</s:iterator>
						<tr>
							<td colspan="2">Total </td>
							
							<td align="right"><%=FormatNumber.formatNumberForJSP(pageContext
															.getAttribute("netAmount"))%>
							
							</td>
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