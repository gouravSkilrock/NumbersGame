<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
<title><%=application.getAttribute("JSP_PAGE_TITLE")%></title>			
	</head>
	<body>
		<s:if test="#session.WITHDRAWAL_DATA_LIST.size>0">
		<div id="errorMsg" style="color: red; text-align: left;"></div>
		<div id="exportdata" align="center" style="display:none">
		<s:a href="olaWithdrawalApproval_Export.action">Export Data</s:a>	
				</div>
	<table width="100%" border="1" cellpadding="3" cellspacing="0"
					bordercolor="#CCCCCC" align="center">
	<tr>
	<th>Check  All	<input type="checkbox" onclick="checkAll(this.checked)" /> </th>
	<th>Account Id</th>
	<th>Name</th>
	<!--<th>Address</th>
	-->
	<th>Request Date(DD-MM-YYYY)</th>
	<th>Amount</th>
	<th>Bank Account Number</th>
	<th>Bank Name</th>
	<th>Transfer Mode</th>
	<th>Status</th>
		</tr>
		
			<s:set name="dataBean" id="dataBean" value="#session.WITHDRAWAL_DATA_LIST" />
					<s:iterator id="beanCart" value="%{#dataBean}" >
					<tr class="startSortable" id="row<s:property value="taskId"/>">
							<td>	
								<s:if test="%{status!='DONE'}"><input type="checkbox" id="check<s:property value="taskId" />" name="check" value="<s:property value="taskId" />" /> </s:if>
								<s:else><input type="checkbox" id="check" name="check" disabled="disabled" value="<s:property value="taskId" />" /> </s:else>
							</td>
								<td align="left" nowrap="nowrap" >
									<s:property value="accountId" />
								</td>
						
							<td >
							<s:property value="name" />
												
							</td>
							<!--<td >
							<s:property value="address" />
									
							
							</td>
							-->
							<td>
							<s:date name="requestDate"  format="dd-MM-yyyy"/>
							</td>
							<td >
							<s:property value="amount" />
									
							
							</td>
							<td >
							<s:property value="bankAcNumber" />
									
							
							</td>
							<td >
							<s:property value="bankName" />
									
							
							</td>
							<td >
							<s:property value="transferMode" />
									
							
							</td>
							<td >
							<div id="status<s:property value="taskId" />">
							<s:property value="status" />
							</div>	
							
							</td>
										
							</tr>
						
							</s:iterator>
							</table>	
							<table align="center">
								<br></br>
							<tr><td colspan="9" >
							<div id="submit">	
							<input type="button" name="submit" value="Approve Checked" class="button" onclick="updateChecked()" /></div>
						</td></tr></table>
						<div id="imageLoad" name="imageLoad"></div>
						</s:if>
						<s:else>
						<table  align="center"><tr><td>
								No Records to Process
								</td></tr>
						</table>
						</s:else>
					

</body>

</html>
<code id="headId" style="visibility: hidden">
$RCSfile: bo_ola_approve_withdrawal_Search.jsp,v $
 $Revision: 1.2 $ 
</code>