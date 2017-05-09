<%@page import="com.skilrock.lms.beans.DateBeans"%>
<%@page import="com.skilrock.lms.common.utility.GetDate"%>
<%@page import="com.skilrock.lms.common.utility.FormatNumber"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<div align="center">
<s:form action="bo_rep_incentiveScheme_analysisReportSearch_ExportAsExcel.action"  method="post" enctype="multipart/form-data">
<s:hidden id="tableValue" name="valueToSend" />
<s:submit value="Export As Excel" cssStyle="margin-right:-400px;background:#77b3d7;font-size:15px" onclick="getData();"/>
</s:form>
</div>
<div id="tableDataDiv">
<s:if test="%{message!=null}">
<h1><s:label value="%{message}"/></h1>
</s:if>
<s:else>

<div align="center">
	<h3>
		<u>Incentive Scheme Analysis  <s:text name="Report" />
		 <%
 	if ("".equals(((DateBeans) session.getAttribute("datebean"))
 			.getReportType())) {
 %>
			From : <%=GetDate.getConvertedDate(((DateBeans) session
						.getAttribute("datebean")).getStartDate())%>
			&nbsp;&nbsp;To&nbsp;:&nbsp; <%=GetDate.getConvertedDate(((DateBeans) session
						.getAttribute("datebean")).getEndDate())%>

			<%
				} else {
					out.println("Of : "
							+ ((DateBeans) session.getAttribute("datebean"))
									.getReportType());
				}
			%>
		</u>
	</h3>
</div>

<table width="100%" border="1" cellpadding="3" cellspacing="0"
	bordercolor="#CCCCCC" align="center" id="expandTable"
	style="font-size: 12px">
	<s:set name="DG" value="%{#session.isDG}"></s:set>
	<s:set name="DGCount" value="0"></s:set>
	<s:set name="colCount" value="0" />
	<s:if test="%{incentiveReport!=null}">
		<s:iterator value="%{gameNameList}">
			<s:if test="%{value=='DG'}">
				<s:set name="DGCount" value="#DGCount+1"></s:set>
			</s:if>
			<s:set name="colCount" value="#colCount+1"></s:set>
		</s:iterator>
		<tr>
			<th align="center" colspan="<s:property value="#colCount+5"/>">
				<s:property value="%{orgName}" />
			</th>
			
		</tr>
		<tr>
			<td align="center" colspan="<s:property value="#colCount+5"/>">
				<s:property value="%{orgAdd}" />
			</td>
		</tr>
		<tr align="center" >
			<th nowrap="nowrap">
			Sr No.
			</th>
			<th nowrap="nowrap">
				<s:text name="Agent" />
				NAME
			</th>
			<th nowrap="nowrap">
				<s:text name="Retailer" />
				NAME
			</th>
			<th nowrap="nowrap">
			Location
			</th>
			<s:iterator value="%{gameNameList}">
					<s:if test="%{value=='DG'}">
						<th>
							<s:property value="%{key}"/> Sales
						</th>
					</s:if>
				</s:iterator>
				
				<th nowrap="nowrap">
				Total Sales
				</th>
			</tr>
		
		<s:set name="dgSale" value="0.0" />
		<s:set name="totTotal" value="0.0" />
		<s:iterator id="result" value="%{incentiveReport}" status="taskIndex">
			<s:set name="orgValue" value="%{value}"></s:set>
			<tr class="startSortable" style="white-space: nowrap;">
				
				<td align="left" nowrap="nowrap">
				<s:property value="#taskIndex.index+1" />
				</td>

				<td align="left" nowrap="nowrap">
					<s:property value="%{#orgValue.agentName}" />
				</td>
				<td align="left" nowrap="nowrap">
					<s:property value="%{#orgValue.retailerName}" />
				</td>
				<td>
				<s:property value="%{#orgValue.address}" />
				</td>
				<s:set name="gameBeanMap" value="#orgValue.gameBeanMap" />
				<s:set name="gameTotal" value="0.0" />
					<s:iterator value="%{gameNameList}">
						<s:if test="%{value=='DG'}">
							<s:set name="gameValue" value="%{#gameBeanMap[key]}" />
							<td align="right">
								<s:set name="sale" value="%{#gameValue.drawSale}" />
								<%=FormatNumber
										.formatNumberForJSP(pageContext
												.getAttribute("sale"))%>
							</td>
							<s:set name="gameTotal" value="%{#gameTotal+#gameValue.drawSale}" />
							</s:if>
					</s:iterator>
				<td align="right" nowrap="nowrap"> 
				<s:property value="%{#orgValue.gameTotal}" />
			</tr>
			</s:iterator>
			<tr id="totalRow" style="font-weight: bold" align="right">
			<th align="center">
				Total
			</th>
			<th align="center">
				
			</th>
			<th align="center">
				
			</th>
			<th align="center">
				
			</th>
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
</s:else>
</div>

<code id="headId" style="visibility: hidden">
	$RCSfile: bo_rep_incentiveScheme_analysisReportSearch.jsp,v $ $Revision: 1.2 $
</code>