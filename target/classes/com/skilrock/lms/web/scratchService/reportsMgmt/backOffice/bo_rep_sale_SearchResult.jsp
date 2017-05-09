
<%@page import="com.skilrock.lms.beans.DateBeans"%>
<%@page import="com.skilrock.lms.common.utility.GetDate"%><%@ taglib prefix="s" uri="/struts-tags" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html>
	<s:head theme="ajax"/>

<body onload="disable()">

<br/>
<div align="center"><h3><u>Sale <s:text name="Report" /> &nbsp;
	<%if("".equals(((DateBeans)session.getAttribute("datebean") ).getReportType())) {%>
  		 From : 
  		<%=GetDate.getConvertedDate(( (DateBeans)session.getAttribute("datebean") ).getStartDate())%>
  		&nbsp;&nbsp;To&nbsp;:&nbsp;
  		<%=GetDate.getConvertedDate(((DateBeans)session.getAttribute("datebean") ).getEndDate())%>
  		
	<%} else {out.println("Of : "+((DateBeans)session.getAttribute("datebean") ).getReportType());}%>
	</u></h3></div>
<p>	
<s:if test="#session.saletype!=null">
  		<s:if test="#session.searchResultBO.size>0">
			<table width="750" style="text-align: left;width: 100%;font-size: 9pt">
			<tr>
				<td align="left" style="text-align: left;width: 100%;font-size: 9pt">
					<s:a href="bo_rep_sale_ExpExcel.action ">Export As Excel</s:a>
				</td>
			</tr>
			</table>	
		</s:if>
	 <table width="100%" border="1" cellpadding="3" cellspacing="0" bordercolor="#CCCCCC" align="center">
		<tr >
			<th>&nbsp;</th>
			<th colspan="3">MRP Sale Details</th>			
			<th colspan="3">Deducted Sale Details</th>			
		</tr>
		<tr>	
			<th>Party Name </th>			
			<th>Sale Amount</th>
			<th>Sale Return Amount</th>
			<th>Net Sale Amount</th>
			<th>Sale Amount</th>
			<th>Sale Return Amount</th>			
			<th>Net Sale Amount</th>
						
		</tr>
		
		<s:if test="#session.searchResultBO.size>0">
			<s:iterator id="beanCart" value="#session.searchResultBO">
			<tr class="startSortable">				
				<td  align="left"><s:property value="name" /></td>				
				<td align="right"><s:property value="saleMrp" /></td>
				<td align="right"><s:property value="saleReturnMrp" /></td>
				<td align="right"><s:property value="netMrpAmt" /></td>
				<td align="right"><s:property value="saleAmt" /></td>
				<td align="right"><s:property value="returnAmt" /></td>				
				<td align="right"><s:property value="netAmt" /></td>				
			</tr>	
			</s:iterator> 
		</s:if>
		<s:else>
			<tr>
				<td colspan="9" align="center"> No Records to Process</td>
			</tr>
		</s:else>
		
	 </table>
	 <s:div id="naviga" cssStyle="text-align: center;width: 100%">
		<s:if test=" #session.APP_ORDER_LIST5.size >10 ">
    	
	<s:if test="#session.startValueOrderSearch!=0"><s:a theme="ajax" targets="down" href="bo_rep_sale_Navigate.action?end=first">First</s:a><s:a theme="ajax" targets="down" href="bo_rep_sale_Navigate.action?end=Previous" > Previous</s:a></s:if><s:else>First Previous</s:else>    
    <s:if test="#session.startValueOrderSearch==((#session.APP_ORDER_LIST5.size/10)*10)">Next Last</s:if><s:else><s:a theme="ajax" targets="down" href="bo_rep_sale_Navigate.action?end=Next" >Next</s:a><s:a theme="ajax" targets="down" href="bo_rep_sale_Navigate.action?end=last">Last</s:a></s:else>      
    
		</s:if>
		<!-- 
		<s:if test="#session.searchResultBO.size>0">
			<s:div id="excel" cssStyle="text-align: left;font-size: 9pt"><s:a href="exportExcelForBO.action">Export As Excel</s:a></s:div>
		</s:if>
		 -->
	 </s:div>
</s:if>

<s:else>
  		
  		<s:if test="#session.searchResultBO.size>0">
			<table width="800" style="text-align: left;width: 100%;font-size: 9pt">
			<tr>
				<td align="left" style="text-align: left;width: 750px;font-size: 9pt">
					<s:a href="bo_rep_sale_ExpExcel.action ">Export As Excel</s:a>
				</td>
			</tr>
			</table>	
		</s:if>
	 <table width="100%" border="1" cellpadding="3" cellspacing="0" bordercolor="#CCCCCC" align="center">
		<tr >
				
			<th>Game Name</th>
			<th>Cost/Ticket</th>
			<th>Tickets/Book</th>
			<!-- <th>Existing Books</th>	 -->
			<th>Cost/Book</th>
			<th>Books Sold</th>			
			<th>Books Returned</th>
			<!-- <th>Books Remaining</th>-->						
		</tr>
		
		<s:if test="#session.searchResultBO.size>0">
			<s:iterator id="beanCart" value="#session.searchResultBO">
			<tr class="startSortable">				
				<td  align="left"><s:property value="gamename" /></td>
				<td align="right"><s:property value="ticketCost" /></td>
				<td align="right"><s:property value="ticketsPerBook" /></td>
			<!-- <td><s:property value="existingBooks" /></td> -->
				<td align="right"><s:property value="bookCost" /></td>
				<td align="right"><s:property value="sale" /></td>				
				<td  align="right"><s:property value="salereturn" /></td>
				<!-- <td><s:property value="boRemBooks" /></td>-->				
			</tr>	
			</s:iterator> 
		</s:if>
		<s:else>
			<tr>
				<td colspan="8" align="center"> No Records to Process</td>
			</tr>
		</s:else>
		
	 </table>
	 <s:div id="naviga" cssStyle="text-align: center;width: 100%;">
		<s:if test=" #session.APP_ORDER_LIST5.size >10 ">
    	
<s:if test="#session.startValueOrderSearch!=0"><s:a theme="ajax" targets="down" href="bo_rep_sale_Navigate.action?end=first">First</s:a><s:a theme="ajax" targets="down" href="bo_rep_sale_Navigate.action?end=Previous" > Previous</s:a></s:if><s:else>First Previous</s:else>    
    <s:if test="#session.startValueOrderSearch==((#session.APP_ORDER_LIST5.size/10)*10)">Next Last</s:if><s:else><s:a theme="ajax" targets="down" href="bo_rep_sale_Navigate.action?end=Next" >Next</s:a><s:a theme="ajax" targets="down" href="bo_rep_sale_Navigate.action?end=last">Last</s:a></s:else>      
    
		</s:if>
		<!-- 
		<s:if test="#session.searchResultBO.size>0">
			<s:div id="excel" cssStyle="text-align: left;font-size: 9pt"><s:a href="exportExcelForBO.action">Export As Excel</s:a></s:div>
		</s:if>
		 -->
	 </s:div>
</s:else>	 

	
</body>
</html>
<code id="headId" style="visibility: hidden">
$RCSfile: bo_rep_sale_SearchResult.jsp,v $
$Revision: 1.2 $
</code>