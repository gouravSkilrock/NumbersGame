
<%@ taglib prefix="s" uri="/struts-tags"%>
<html>
		<head>
			<link rel="stylesheet"
					href="<%=request.getContextPath()%>/LMSImages/css/demo_table_jui.css"
					type="text/css">
				<link rel="stylesheet"
					href="<%=request.getContextPath()%>/LMSImages/css/datatable-common.css"
					type="text/css">
				<style type="text/css">
						#dTable_wrapper, #dTableNew_wrapper, #net_sale_dTable_wrapper {
   							 width: auto;
						}
				</style>
		</head>
		<body>
<div align="center">
	<h3>
		<u>Today's Scratch Ticket Sale Status <s:text name="Report" /> </u>
	</h3>
</div>

<s:set name="SE" value="%{#session.isSE}"></s:set>
<s:set name="SECount" value="0"></s:set>

<!-- btn.exportasexcel -->
	<div id="tableDataDiv">
	
<table  border="1" cellpadding="3" cellspacing="0"
	bordercolor="#CCCCCC" align="center"  id="dTableNew" style="width: 100%" >
			<s:if  test="%{ticketStatusList!=null&&ticketStatusList.size>0}">
				<s:div id="excel"
					cssStyle="text-align: center;width: 100%;font-size: 9pt">
					<s:a href="agt_rep_tikcetStatus_ExpExcel.action ">Export As Excel</s:a>
				</s:div>
		<thead>
		<tr>
			<th align="center">
				<s:property value="#application.TIER_MAP.RETAILER" /> Name
			</th>
			<th align="center">
				Game Name
			</th>
			<th align="center">
				Book Number
			</th>
			<th align="center">
				Ticket(s) Issued Today
			</th>
			<th align="center">
				Tickets Sold Today
			</th>
			<th align="center">
				Tickets Remaining
			</th>
		</tr>
	</thead>
		<tbody>
		<s:iterator value="ticketStatusList">
 				 <tr>
 				 		<td align="center"><s:property value="retailerOrgName"/></td>
 				 		<td align="center"><s:property value="gameName"/></td>
 				 		<td align="center"><a href="#" onclick="getTicketDetailStatus('<s:property value="bookNumber"/>','N')"><s:property value="bookNumber"/></a></td>
 				 		<td align="center"><s:property value="totalTickets"/></td>
 				 		<td align="center"><s:property value="ticketsSold"/></td>
 				 		<td align="center"><a href="#" onclick="getTicketDetailStatus('<s:property value="bookNumber"/>','Y')"><s:property value="ticketsRemaining"/></a></td>
 				 </tr>
 				  
			</s:iterator>
		</tbody>

	</s:if>
	<s:else>
		<tr>
			<th align="center">
				No Records Found
			</th>
		</tr>
	</s:else>

</table>
</div>
</body>
</html>
<code id="headId" style="visibility: hidden">
	$RCSfile: agt_rep_ticketStatus.jsp,v $
	$Revision: 1.3 $
</code>