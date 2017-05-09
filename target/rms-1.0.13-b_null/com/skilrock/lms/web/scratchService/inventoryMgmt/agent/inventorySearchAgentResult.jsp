<%@ taglib prefix="s" uri="/struts-tags" %>

<html>
	<s:head theme="ajax"/>

<body onload="disable()">

<br>
  
	
	
  		
	 <table width="800" border="1" cellpadding="3" cellspacing="0" bordercolor="#CCCCCC" align="center">
		<tr >
			<th>Game Number</th>	
			<th>Game Name</th>
			<th>Game Status</th>
			<th>PWT End Date</th>
			<th>Total No Of Books from BO</th>
			<th>No Of Books Remaining With <s:property value="#application.TIER_MAP.AGENT" /></th>
			<th>No Of Books With <s:property value="#application.TIER_MAP.RETAILER" /></th>
		<s:if test="#session.retailerOnline!=null">
			<th>No Of Books Active With <s:property value="#application.TIER_MAP.RETAILER" /></th>
		</s:if>
			
		</tr>
		
		<s:if test="#session.searchResultAgent.size>0">
			<s:iterator id="beanCart" value="#session.searchResultAgent">
			<tr>
				<td><s:property value="gamenbr" /></td>
				<td><s:property value="gamename" /></td>
				<td><s:property value="gamestatus"/></td>
				<td><s:property value="pwtenddate" /></td>
				<td><s:property value="totalbooks" /></td>
				<td><s:property value="bookagent" /></td>
				<td><s:property value="bookretailer" /></td>
			<s:if test="#session.retailerOnline!=null">
				<td><s:property value="activebooks" /></td>
			</s:if>
				
			</tr>	
			</s:iterator> 
		</s:if>
		<s:else>
			<tr>
				<td colspan="8" align="center"> No Records to Process</td>
			</tr>
		</s:else>
		
	 </table>
	 <s:div id="naviga" cssStyle="text-align: center">
		<s:if test=" #session.APP_ORDER_LIST5.size >5 ">
     		
		
		
	<s:if test="#session.startValueOrderSearch!=0"><s:a theme="ajax" targets="down" href="paginationForAgent.action?end=first">First</s:a><s:a theme="ajax" targets="down" href="paginationForAgent.action?end=Previous" > Previous</s:a></s:if><s:else>First Previous</s:else>    
    <s:if test="#session.startValueOrderSearch==((#session.APP_ORDER_LIST5.size/5)*5)">Next Last</s:if><s:else><s:a theme="ajax" targets="down" href="paginationForAgent.action?end=Next" >Next</s:a><s:a theme="ajax" targets="down" href="paginationForAgent.action?end=last">Last</s:a></s:else>      
    
		</s:if>
	 </s:div>
	 

	
</body>
</html>
<code id="headId" style="visibility: hidden">
$RCSfile: inventorySearchAgentResult.jsp,v $
$Revision: 1.2 $
</code>