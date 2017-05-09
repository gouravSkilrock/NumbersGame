<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
<title><%=application.getAttribute("JSP_PAGE_TITLE")%></title>			
	</head>
	<body>
		<s:set name="dataBeanList" value="pinStatusBeanList"/>
		<s:if test="#dataBeanList.size>0">
			<table width="100%" border="1" cellpadding="3" cellspacing="0"
					bordercolor="#CCCCCC" align="center">
					<tr>
					<th>Pin Generation Date</th>
					<th>Distributor</th>
					<th>Denomination</th>
					<th>No of Pin Generated</th>
					<th>No of Pin Redeemed</th>
					<th>Sale Commission Rate</th>
					<th>Redeemed At</th>
					<th>Expiry Date</th>
					</tr>	
					<s:set name="dataBean" id="dataBean" value="#dataBeanList" />
						<s:iterator id="beanCart" value="%{#dataBean}" >
						<tr class="startSortable" >
						<td><s:date name="generationDate"  format="dd-MM-yyyy"/></td>
						<td><s:property value="distributor" /></td>
						<td align="right" nowrap="nowrap" ><s:property value="amount" /></td>
						<td align="right" nowrap="nowrap" ><s:property value="pinGenerated" /></td>
						<td align="right" nowrap="nowrap" ><s:property value="pinRedeemed" /></td>
						<td align="right" nowrap="nowrap" ><s:property value="saleCommRate" /></td>
						<td><s:property value="redeemedAt" /></td>
						<td><s:date name="expiryDate"  format="dd-MM-yyyy"/></td>
						</tr>
						</s:iterator>
			</table>		
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
$RCSfile: bo_ola_rep_pin_status_result.jsp,v $
 $Revision: 1.3 $ 
</code>