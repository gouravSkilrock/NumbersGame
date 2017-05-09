<%@page import="com.skilrock.lms.common.utility.GetDate"%><%@ taglib prefix="s" uri="/struts-tags" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html>
<head>
	<s:head theme="ajax"/>
</head>	
	
<body>

	 <table width="100%" border="1" cellpadding="3" cellspacing="0" bordercolor="#CCCCCC" align="center">
		<tr>
			<th colspan="5" align="center">Consumable & Non Consumable Inventory Details</th>			
		</tr>
		<tr>	
			<th align="center">Owner Name </th>	
			<th align="center">Owner Type</th>					
			<th align="center">Inv Type</th>
			<th align="center">Inv Name</th>
			<th align="center">Count</th>									
		</tr>
		
		<s:if test="#session.INV_DET_LIST.size>0">
			<s:iterator id="beanCart" value="#session.INV_DET_LIST_NAV">
			<tr>
				<td  align="left"><s:property value="ownerName" /></td>					
				<td align="left"><s:property value="ownerType" /></td>							
				<td align="center"><s:property value="invType" /></td>				
				<td align="right">
					<s:a targets="lastDown" theme="ajax" href="agt_im_consNonConsSearchInv_detail.action?ownerId=%{ownerId}& invId=%{invId}& invType=%{invType}& brandId=%{brandId}& modelId=%{modelId}" onclick="this.style.color='blue'">
						<s:property value="invName" />
					</s:a>
				</td>		
				<td align="right"><s:property value="count" /></td>					
			</tr>	
			</s:iterator> 
		</s:if>
		<s:else>
			<tr>
				<td colspan="9" align="center"> No Result Found</td>
			</tr>
		</s:else>		
	 </table>
	 
	 
	 <s:div id="naviga" cssStyle="text-align: center;width: 100%">
		<s:if test=" #session.INV_DET_LIST.size >10 ">    	
			<s:if test="#session.startValueOrderSearch!=0"><s:a theme="ajax" targets="downResult" href="agt_im_consNonConsSearchInv_navigate.action?end=first">First</s:a><s:a theme="ajax" targets="downResult" href="agt_im_consNonConsSearchInv_navigate.action?end=Previous" > Previous</s:a></s:if><s:else>First Previous</s:else>    
    		<s:if test="#session.startValueOrderSearch==((#session.INV_DET_LIST.size/10)*10)">Next Last</s:if><s:else><s:a theme="ajax" targets="downResult" href="agt_im_consNonConsSearchInv_navigate.action?end=Next" >Next</s:a><s:a theme="ajax" targets="downResult" href="agt_im_consNonConsSearchInv_navigate.action?end=last">Last</s:a></s:else>      
    	</s:if>		
	 </s:div>
 

	
	</body>
</html>
<code id="headId" style="visibility: hidden">
$RCSfile: agt_im_consNonConsSearchInv_searchResult.jsp,v $
$Revision: 1.2 $
</code>