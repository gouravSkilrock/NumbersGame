<%@page import="com.skilrock.lms.beans.DateBeans"%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%
	response.setHeader("Cache-Control", "no-store"); //HTTP 1.1
	response.setHeader("Pragma", "no-cache"); //HTTP 1.0
	response.setDateHeader("Expires", 0); //prevents caching at the proxy server
%>
<br />

<s:if test="%{#session.NetGamingExpenseData.size()>0}"><center>
	<h3><u>Net Gaming Expenses <s:text name="Report"/> for <%
 	if ("WEEKLY".equalsIgnoreCase(((DateBeans) session.getAttribute("DATE_BEANS"))
 			.getReportType())) {
 %> the week From : <%=((DateBeans) session
								.getAttribute("DATE_BEANS")).getStrDateString().split(" ")[0]%>
					&nbsp;&nbsp;To&nbsp;:&nbsp; <%=((DateBeans) session
								.getAttribute("DATE_BEANS")).getEndDateString().split(" ")[0]%> <%
 	} else {
 %> <%="the day : "+ ((DateBeans) session.getAttribute("DATE_BEANS"))
								.getStrDateString().split(" ")[0]%> <%
 	}
 %> </u>
			</h3></center>
	<div id="dataDiv" style="">
		<table width="100%" border="1" cellpadding="2" cellspacing="0"
			bordercolor="#CCCCCC" align="center" valign="top" id="dataTable">
			<tr>
				<th>
					S.No
				</th>
				<th nowrap="nowrap">
					Agent Name
				</th>
				<th>
					Net Gaming Amt
				</th>
				
				<th>
					Net Gaming Exp Amt
				</th>
				<th>
					Status
				</th>
				<th>
					<input type="checkbox" onclick="selAll(this.checked)" />
					Approve
				</th>
				<!-- <th>
					Update
				</th> -->
				
				<th>
					Approved Date
				</th>
			</tr>
			<s:iterator value="#session.NetGamingExpenseData" status="taskIndex">
				<s:set name="rowCount" id="rowCount" value="#taskIndex.index+1"></s:set>
				<tr id="row<s:property value="#rowCount"/>">
					<td>
						<s:property value="#rowCount" />
						<s:hidden name="orgId" value="value.orgId" />
						<s:hidden name="taskId" value="value.taskId" />
					</td>
					<td nowrap="nowrap">
						<s:property value="value.orgName" />
					</td>
					<td nowrap="nowrap" align="right">
						<s:property value="value.NetGamingAmt" />
					</td>
					
					<td nowrap="nowrap" align="right">
						<s:property value="value.NetGamingCommissionAmt" />
					</td>
					<td nowrap="nowrap">
						<div id="status<s:property value="#rowCount" />">
							<s:property value="value.status" />
						</div>
					</td>
					<td align="center">

						<s:if test="%{value.status!='DONE'}">
							<input type="checkbox"
								id="chkBox<s:property value="#rowCount" />" name="tempStatus"
								onchange="updRowColor(this.id)"
								value="<s:property value="value.orgId" />" />
						</s:if>
						<s:else>
							<input type="checkbox"
								id="chkBox<s:property value="#rowCount" />" name="tempStatus"
								disabled="disabled" value="<s:property value="value.orgId" />" />
						</s:else>

					</td>
					<!-- 
					<td>
						<div id="status<s:property value="#rowCount" />">
							<input type="button"
								onclick="updateData('<s:property value="value.taskId" />','<s:property value="#rowCount" />')"
								value="Update" class="button" />
						</div>
					</td> -->
					
					<td>
						<div id="upDate<s:property value="#rowCount" />">
							<s:property value="value.updateDate" />
						</div>
					</td>
				</tr>
			</s:iterator>
			<tr>
				<td colspan="5" align="center">
					<table>
						<input type="button" name="submit" value="Approve All"
							class="button" onclick="updateAllAgent()" />
						
					</table>
				</td>
				<td colspan="4" align="center">
					<table>
						<input type="button" name="submit" value="Approve Selected"
							class="button" onclick="updateSelected()" />
					</table>
				</td>
			</tr>
			
		</table>
	</div>
</s:if>
<s:else>
	<div style="text-align: center">
		No Records Found
	</div>
</s:else>

<code id="headId" style="visibility: hidden">
	$RCSfile: bo_ola_net_gaming_exp_Result.jsp,v $ $Revision: 1.2 $
</code>