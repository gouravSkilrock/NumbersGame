<%@page import="com.skilrock.lms.common.utility.GetDate"%><%@ taglib prefix="s" uri="/struts-tags" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html>
<head>
	<s:head theme="ajax"/>
</head>	
	
<body>

	 <table width="100%" border="1" cellpadding="3" cellspacing="0" bordercolor="#CCCCCC" align="center">
	 <s:set name="first" value="#session.INV_WISE_DET_LIST[0]"/>
		<tr>
			<th colspan="7" align="center">Non Consumable Inventory Wise Details</th>			
		</tr>
		<tr>	
			<th align="center">Owner Name </th>	
			<th align="center">Owner Type</th>
			<th align="center">Inv Name</th>
			<th align="center">Brand Name</th>
			<th align="center">Model Name</th>
			<th align="center">Serial No.</th>
			<th align="center">Inv Code</th>
								
		</tr>
		
		<s:if test="#session.INV_WISE_DET_LIST.size>0">
			<s:iterator id="beanCart" value="#session.INV_WISE_DET_LIST">
			<tr>
				<td  align="left"><s:property value="ownerName" /></td>					
				<td align="left"><s:property value="ownerType" /></td>							
				<td align="left"><s:property value="invName" /></td>
				<td align="left"><s:property value="brandName" /></td>	
				<td align="left"><s:property value="modelName" /></td>			
				<td align="left"><s:property value="serialNo" /></td>
				<s:if test="invCode!=null">
				<td align="left"><s:property value="invCode" /></td>	
				</s:if>
				<s:else><td align="left">N.A.</s:else>							
			</tr>	
			</s:iterator> 
		</s:if>
		<s:else>
			<tr>
				<td colspan="9" align="center"> No Result Found</td>
			</tr>
		</s:else>		
	 </table>
	
	</body>
</html>
<code id="headId" style="visibility: hidden">
$RCSfile: agt_im_nonConsSearchInv_detResult.jsp,v $
$Revision: 1.3 $
</code>