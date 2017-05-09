<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="com.skilrock.lms.common.utility.FormatNumber"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
<title><%=application.getAttribute("JSP_PAGE_TITLE")%></title>	
	

	
</head>
		<body>

				<div id="errorDiv" style="color: red;"><s:actionmessage/>   </div>
			<s:set name="dataBeanList" value="postSaleRepBeanList"/>
			<s:if test="#dataBeanList.size>0">
			<div align="left">
				<s:form action="exportReportAsExcel.action" method="post"
					enctype="multipart/form-data">
					<s:hidden id="tableValue" name="reportData" />
					<s:hidden name="reportName" value=" Post Sale Deposit Report" />
					<s:submit value="Export As Excel"
						cssStyle="margin-right:-400px;font-size:15px" onclick="getData();" />
				</s:form>
			</div>
			<div id="tableDataDiv">
			<table align="center">
			<tr>
			<td><h3><u> Post Sale Deposit<s:text name="Report"></s:text>  </td>
			</tr>
			</table>
				<table width="100%" border="1" cellpadding="3" cellspacing="0"
					bordercolor="#CCCCCC" align="center">
					
					<tr>
						<th >
							<s:text name="agent"></s:text> NAME
						</th>
						<th >
							
							
							Deposit Amount
						</th>
						<th >
							
							
							Deposit Commission Amount
						</th>
						<th >
							
							
							Tax Amount
						</th>
						<th >
							Charges
						</th>
						<th >
							
							
							Bank Name
						</th>
						<s:if test="repType!='EZWICH'">
						
						<th >
							
							
							Branch Name
						</th>	
						<th >
							
							
							Branch Sort Code
						</th>		
						</s:if>
						
						<th >
							
							
							Account Number
						</th>
						<th >
							Net Amount to be paid 
						</th>
					</tr>
			
				
					<s:iterator value="%{#dataBeanList}">
					
							<tr class="startSortable">
								
								<td align="left">
								<s:property value="agentName" />
								</td>
								<td align="left">
								<s:property value="depositAmount" />
								</td>
								<td align="left">
									<s:property value="depositCommAmount" />
								</td>
								<td align="right">
									<s:property value="taxAmount" />
								</td>
								<td align="center">
									<s:property value="charge1Amount" />
								</td>
								
								<td align="center">
									<s:property value="bankName" />
								</td>
								<s:if test="repType!='EZWHICH'">
									<td align="center">
									<s:property value="branchName" />
								</td>
								<td align="center">
									<s:property value="branchSortCode" />
								</td>
								</s:if>
							
							
								<td align="center">
									<s:property value="accountNumber" />
								</td>
								<td align="right">
								
								<s:property value="netAmount" />
								</td>
										
										
							</tr>
						
					</s:iterator>
				

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