
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
	<%-- <div align="center">
		<h3>
			<u>Today's Scratch Ticket Sale Status <s:text name="Report" />
			</u>
		</h3>
	</div> --%>

	<s:set name="SE" value="%{#session.isSE}"></s:set>
	<s:set name="SECount" value="0"></s:set>

	<!-- btn.exportasexcel -->
	<div id="tableDataDiv">

		<table border="1" cellpadding="3" cellspacing="0"
			bordercolor="#CCCCCC" align="center" id="dTableNew"
			style="width: 100%">
			<s:if test="%{ticketDetailStatusList!=null && ticketDetailStatusList.size>0}">
				<%-- <s:div id="excel"
					cssStyle="text-align: center;width: 100%;font-size: 9pt">
					<s:a href="agt_rep_tikcetStatus_ExpExcel.action ">Export As Excel</s:a>
				</s:div> --%>				
					<tr style="font-size:larger;">
						<th align="center">Ticket Number</th>
						<th align="center">Sold Time</th>
						<th align="center">Status</th>
					</tr>							
					<s:iterator value="ticketDetailStatusList">
						<tr class="startSortable" style="font-size:larger;">
							<td align="center"><s:property value="ticketNumber" /></td>
							<td align="center"><s:property value="soldTime" /></td>
							<td align="center"><s:property value="status" /></td>
						</tr>
					</s:iterator>			
				<tfoot>
					<tr style="font-size:larger;">
						<td align="center" colspan="">Total</td>
						<td></td>
						<td align="center"><s:property value="%{ticketDetailStatusList.size()}"/></td>
					</tr>
				</tfoot>

			</s:if>
			<s:else>
				<tr>
					<th align="center">No Records Found</th>
				</tr>
			</s:else>

		</table>
	</div>
</body>
</html>
<code id="headId" style="visibility: hidden"> $RCSfile:
	agt_rep_ticketDetailStatus.jsp,v $ $Revision: 1.3 $ </code>