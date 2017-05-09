<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="com.skilrock.lms.beans.DateBeans"%>
<%@page import="com.skilrock.lms.common.utility.GetDate"%>
<%@page import="com.skilrock.lms.common.utility.FormatNumber"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<html>
	<s:head theme="ajax" debug="false" />
	<body>

		<div align="center">
			<h2>
				<u>Consolidated Rank Wise Winning Report (For 12/24 Game only)</u>
			</h2>
			
			<s:form action="exportToExcelRankWinningReport.action" method="post" enctype="multipart/form-data">
			<s:hidden name="gameNo" value="%{gameNo}"/>
			<s:hidden name="drawName" value="%{drawName}"/>
					<s:hidden id="tableValue" name="reportData" />
					<s:hidden name="reportName" value="Rank Wise Winning Report" />
					<s:submit key="btn.exportasexcel" cssStyle="margin-right:-400px;font-size:15px; text-align:center" onclick="createTableData();"/>
			</s:form>
			
			<s:if test="%{rankWinningData.size > 0}">
			
			<div id="tableDataDiv">
			<table width="100%" border="1" cellpadding="3" cellspacing="0" bordercolor="#CCCCCC" align="center" >
			
						<tr>
							<th>SA Name</th>
							<th>PA Name</th>
							<th>12 Match Tickets</th>
							<th>11 Match Tickets</th>
							<th>10 Match Tickets</th>
							<th>9 Match Tickets</th>
							<th>Match 10 (Direct-10)</th>
							<th>Total Sale</th>
							<th>Total Winning</th>
													
						</tr>
				
				
				<s:set name="match12" value="0" />
				<s:set name="match11" value="0" />
				<s:set name="match10" value="0" />
				<s:set name="match9" value="0" />
				<s:set name="match10DC" value="0" />
				<s:set name="finalSale" value="0.0" />
				<s:set name="finalWinning" value="0.0" />
						<s:iterator value="rankWinningData">
						
							<s:if test="{value.sum > 0}">
				
								<tr class="startSortable" align="center">
									
									<td><s:property value="value.retailerName"/></td>
									<td><s:property value="value.agentName"/></td>
									
									
									<s:iterator value="value.winningTktMap">
									
										<td><s:property value="value"/></td>
										<s:if test="key == 1">
										<s:set name="match12" value="%{#match12 + value}" />
										</s:if>
										<s:if test="key == 2">
										<s:set name="match11" value="%{#match11 + value}" />
										</s:if>
										<s:if test="key == 3">
										<s:set name="match10" value="%{#match10 + value}" />
										</s:if>
										<s:if test="key == 4">
										<s:set name="match9" value="%{#match9 + value}" />
										</s:if>
										<s:if test="key == 5">
										<s:set name="match10DC" value="%{#match10DC + value}" />
										</s:if>
									
															
									
									</s:iterator>
									<td><s:set name="sale" value="value.totalSaleAmt"/>
									<%= FormatNumber.formatNumberForJSP(pageContext.getAttribute("sale")) %>
									</td>
										<s:set name="finalSale" value="%{#finalSale + value.totalSaleAmt}" />
									<td><s:set name="winning" value="value.totalWinningAmt"/>
									<%= FormatNumber.formatNumberForJSP(pageContext.getAttribute("winning")) %>
									</td>			
									<s:set name="finalWinning" value="%{#finalWinning + value.totalWinningAmt}" />
								
								</tr>
							
							</s:if>
						
						</s:iterator>
						<tr align="center">
						<td colspan="2">Total</td>
						
						<td><%= FormatNumber.formatNumberForJSPWithoutZero(pageContext.getAttribute("match12")) %></td>
						<td><%= FormatNumber.formatNumberForJSPWithoutZero(pageContext.getAttribute("match11")) %></td>
						<td><%= FormatNumber.formatNumberForJSPWithoutZero(pageContext.getAttribute("match10")) %></td>
						<td><%= FormatNumber.formatNumberForJSPWithoutZero(pageContext.getAttribute("match9")) %></td>
						<td><%= FormatNumber.formatNumberForJSPWithoutZero(pageContext.getAttribute("match10DC")) %></td>
						<td><%= FormatNumber.formatNumberForJSP(pageContext.getAttribute("finalSale")) %></td>
						<td><%= FormatNumber.formatNumberForJSP(pageContext.getAttribute("finalWinning")) %></td>
						</tr>
				
			</table>
			</div>
			</s:if>
			
		<s:else>
				<div>No Record Exist !!</div>
							</s:else>
		</div>
	</body>
</html>

<code id="headId" style="visibility: hidden">
	$RCSfile: bo_rep_rankWiseWinningReportSearch.jsp,v $ $Revision: 1.3 $
</code>