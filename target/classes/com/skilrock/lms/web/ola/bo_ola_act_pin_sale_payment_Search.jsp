<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
<title><%=application.getAttribute("JSP_PAGE_TITLE")%></title>			
	</head>
	<body>
			<div style="color: red; text-align: left;">
		<s:actionmessage /></div>
						<s:actionerror />
		<s:if test="#session.PIN_PAYMENT_BEAN_LIST.size>0">
	<s:form action="bo_act_pin_sale_approve" id="approve_pin_sale_payment" name="approve_pin_sale_payment" onsubmit="return verifyCheckBox()">
		<div id="errorMsg"></div>
		<s:token />
			
	<table width="100%" border="1" cellpadding="3" cellspacing="0"
					bordercolor="#CCCCCC" align="center">
	<tr>
	<th>Start Date(dd-MM-yyyy)</th>
	<th>End Date(dd-MM-yyyy)</th>
	<th>Total Amount</th>
	<th>Commission Rate</th>
	<th>Net Amount</th>
	<th>Wallet</th>
	<th>Check  All	<input type="checkbox" onclick="checkAll(this.checked)" /> </th>
	</tr>
		
			<s:set name="dataBean" id="dataBean" value="#session.PIN_PAYMENT_BEAN_LIST" />
					<s:iterator id="beanCart" value="%{#dataBean}" >
					<tr class="startSortable" id="row<s:property value="taskId"/>">
						<td align="left" nowrap="nowrap" >
						<s:date name="startDate"  format="dd-MM-yyyy"/>
						</td>
						<td align="left" nowrap="nowrap" >
							<s:date name="endDate"  format="dd-MM-yyyy"/>
						</td>
						<td  align="right" nowrap="nowrap" >
							<s:property value="totalAmount" />
						</td>
						<td align="right" nowrap="nowrap" >
							<s:property value="commRate" />
						</td>
						<td align="right" nowrap="nowrap" >
							<s:property value="getText('{0,number,##0.00}', {netAmount})" />
						</td>
						<td >
							<s:property value="walletName" />
						</td>
						<td>	
								<input type="checkbox" id="check<s:property value="taskId" />" name="check" value="<s:property value="taskId" />" />
								
						</td>
						</tr>
						</s:iterator>
							</table>	
						<table align="center">
								<br></br>
							<tr><td colspan="9" >	
							<div id="submit">
									<input type="submit" name="submit" value="Approve Checked" class="button" />
							</div>
							<div id="imageLoad" name="imageLoad">
							</div>
						
						</td></tr></table>	
					</s:form>		
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
$RCSfile: bo_ola_act_pin_sale_payment_Search.jsp,v $
 $Revision: 1.3 $
</code>