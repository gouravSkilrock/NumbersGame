<%@page import="com.skilrock.lms.beans.DateBeans"%>
<%@page import="com.skilrock.lms.common.utility.GetDate"%>
<%@page import="com.skilrock.lms.common.utility.FormatNumber"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<div align="center"><h3><u>Retailer  <s:text name="5/90 Transaction" /> <s:text name="Report"/> &nbsp;
	<%if("".equals(((DateBeans)session.getAttribute("datebean") ).getReportType())) {%>
  		 On : 
  		<%=GetDate.getConvertedDate(( (DateBeans)session.getAttribute("datebean") ).getStartDate())%>
  		
  		
	<%} else {out.println("Of : "+((DateBeans)session.getAttribute("datebean") ).getReportType());}%>
	</u></h3></div>	

<div id="excel"  align="center" style="display:none;" text-align: left;width: 750px;font-size: 9pt"><s:a href="bo_rep_CollReportOverAll_ExpExcel.action?orgType=BO&start_date=%{start_date}&end_Date=%{end_Date}&reportType=%{totaltime}">Export As Excel</s:a></div>
<table width="100%" border="1" cellpadding="3" cellspacing="0"
	bordercolor="#CCCCCC" align="center" style="font-size: 12px">

	<s:if test="%{#session.result!=null}">
		
		<tr align="center">
				<th  nowrap="nowrap">
				Transaction No
			</th>
			<th  nowrap="nowrap">
				Date/Time
			</th>
			<th  nowrap="nowrap">
				Particular
			</th>
			
			<th  nowrap="nowrap">
				Amount
			</th>
			
			<th  nowrap="nowrap">
				Status
			</th>
			
		</tr>
	<s:set name="totalSale" value="0.0" />
	
		
		<s:iterator id="result" value="#session.result">
			
			<tr> 
			<s:set name="orgValue" value="%{value}"></s:set>
				<!--<td nowrap="nowrap">
				<s:if test= "%{#orgValue.transactionType == 'CL' || #orgValue.transactionType == 'XCL' }">
				
				</s:if>
					<s:else><s:property value="%{key}" /></s:else>
				</td>
			
				
					-->
					<td align="right" noWrap="nowrap">
					<s:property value="%{#orgValue.transactionNo}"/>
						
					</td>
					<td align="right" noWrap="nowrap">
					<s:property value="%{#orgValue.date}"/>
						
					</td>
				
				
					<td align="right" noWrap="nowrap">
					<s:property value="%{#orgValue.gameName}"/>
						
					</td>
			
				
					<td align="right" noWrap="nowrap">
					<!--<s:property value="%{#orgValue.amount}"/>
						-->
						<s:set name="drawSale" value="%{#orgValue.amount}" />
						<%=FormatNumber.formatNumberForJSP(pageContext.getAttribute("drawSale"))%>
					</td>
				
				
				<td align="right">
				<s:property value="%{#orgValue.status}"/>
				
				</td>
				<s:set name="totalSale"
					value="%{#totalSale + #drawSale}" />
			</tr>
		</s:iterator>
	<tr>
	<th colspan="3">Total Sale Value</th>
	<th align="right" noWrap="nowrap">
				<%=FormatNumber.formatNumberForJSP(pageContext.getAttribute("totalSale"))%>
			</th>
	<th></th>
	</tr>
	</s:if>
	<s:else>
		<tr>
			<th align="center">
				No Records Found
			</th>
		</tr>
	</s:else>
</table>


<code id="headId" style="visibility: hidden">
$RCSfile: bo_rep_5_90_TransactionReport_search.jsp,v $
$Revision: 1.2 $
</code>