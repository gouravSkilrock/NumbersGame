<%@page import="com.skilrock.lms.beans.DateBeans"%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="com.skilrock.lms.common.utility.FormatNumber"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%
	response.setHeader("Cache-Control", "no-store"); //HTTP 1.1
	response.setHeader("Pragma", "no-cache"); //HTTP 1.0
	response.setDateHeader("Expires", 0); //prevents caching at the proxy server
%>
<br />

<s:if test="%{#session.TrExpenseData.size()>0}"><center>
	<h3><u>Training Expenses <s:text name="Report"/> for <%
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
					Sale Amt
				</th>
				<th>
					Tr. Exp %
				</th>
				<th>
					Tr. Exp Amt
				</th>
			
				<th>
					Time Slotted Mrp Sale
				</th>
				<th>
					Ex. Tr. Exp %
				</th>
				<th>
					Ex. Tr. Exp Amt
				</th>
				<th>
					Total Tr. Exp Amt
				</th>
				<s:if test="%{serviceId == 7}">
					<th>
						Incentive Mrp %
					</th>
					<th>
						Incentive Exp %
					</th>
					<th>
						Incentive Exp Amt
					</th>
				</s:if>
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
					CRNote No.
				</th>
				<s:if test="%{serviceId == 7}">
				<th id="incentiveCrNote">
					Incentive CRNote No.
				</th>
				</s:if>
				<th>
					Approved Date
				</th>
				
			</tr>
			<s:set name="saleSumTot" value="0.0" />
			<s:set name="expSumTot" value="0.0" />
			
			<s:set name="saleSumTotSlot" value="0.0" />
			<s:set name="expSumTotSlot" value="0.0" />
			<s:set name="trngTotSale" value="0.0" />
			<s:set name="incentiveTot" value="0.0" />
			
			<s:iterator value="#session.TrExpenseData" status="taskIndex">
				<s:set name="orgValue" value="%{value}"></s:set>
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
						<s:property value="value.saleAmt" />
						<s:set name="saleSum"
							value="%{#orgValue.saleAmt}" />
					</td>
					<td align="right">
						<s:property value="value.trainingPer" />

					</td>
					<td nowrap="nowrap" align="right">
						<s:property value="value.trainingAmt" />
					<s:set name="expSum"
							value="%{#orgValue.trainingAmt}" />
					</td>
					
					<td align="right">
						<s:property value="value.timeSlottedMrpSale" />
						<s:set name="saleSumSlot" value="%{#orgValue.timeSlottedMrpSale}" />
					</td>
					<td align="right">
						<s:property value="value.extraTrainingPer" />
					</td>
					<td align="right">
						<s:property value="value.extraTrainingAmt" />
						<s:set name="expSumSlot" value="%{#orgValue.extraTrainingAmt}" />
					</td>
					<td align="right">
										
						<s:set name="trngSale" value="%{#orgValue.trainingAmt + #orgValue.extraTrainingAmt}" />
						<%=FormatNumber.formatNumberForJSP(pageContext.getAttribute("trngSale"))%>
					</td>
					<!-- Incentive Amt Starts-->
					<s:if test="%{serviceId == 7}">
						<td align="right">
							<s:property value="value.incentiveMrp" />
						</td>
						<td align="right">
							<s:property value="value.incentivePer" />
						</td>
						<td align="right">
							<s:property value="value.incentiveAmt" />
							<s:set name="incExpSumSlot" value="%{#orgValue.incentiveAmt}" />
						</td>
					</s:if>
					<!-- Incentive Amt Ends -->
					
					<td nowrap="nowrap">
						<div id="status<s:property value="#rowCount" />">
							<s:property value="value.status" />
						</div>
					</td>
					<td align="center">

						<s:if test="%{value.status=='PENDING'}">
							<input type="checkbox"
								id="chkBox<s:property value="#rowCount" />" name="tempStatus"
								onchange="updRowColor(this.id)"
								value="<s:property value="value.taskId" />" />
						</s:if>
						<s:else>
							<input type="checkbox"
								id="chkBox<s:property value="#rowCount" />" name="tempStatus"
								disabled="disabled" value="<s:property value="value.taskId" />" />
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
						<div id="crNote<s:property value="#rowCount" />">
							<s:property value="value.crNote" />
						</div>
					</td>
					<s:if test="%{serviceId == 7}">
						<td>
							<div id="incCrNote<s:property value="#rowCount" />">
								<s:property value="value.incentiveCrNote" />
							</div>
						</td>
					</s:if>
					<td>
						<div id="upDate<s:property value="#rowCount" />">
							<s:property value="value.updateDate" />
						</div>
					</td>
				</tr>
				<s:set name="saleSumTot"
					value="%{#saleSumTot+#saleSum}" />
				<s:set name="expSumTot"
					value="%{#expSumTot+#expSum}" />
				<s:set name="saleSumTotSlot"
					value="%{#saleSumTotSlot+#saleSumSlot}" />
				<s:set name="expSumTotSlot"
					value="%{#expSumTotSlot+#expSumSlot}" />
				<s:set name="trngTotSale"
					value="%{#trngTotSale+#trngSale}" />
					
				<s:set name="incentiveTot"
					value="%{#incentiveTot+#incExpSumSlot}" />
				<%-- <s:set name="incentiveTot" value="0.0" /> --%>
					
					</s:iterator>
			<tr>
				<td colspan="2">Total</td>
				<td align="right"><%=FormatNumber.formatNumberForJSP(pageContext.getAttribute("saleSumTot"))%></td>
				<td>&nbsp;</td>
				<td align="right"><%=FormatNumber.formatNumberForJSP(pageContext.getAttribute("expSumTot"))%></td>
				
				<td align="right"><%=FormatNumber.formatNumberForJSP(pageContext.getAttribute("saleSumTotSlot"))%></td>
				<td>&nbsp;</td>
				<td align="right"><%=FormatNumber.formatNumberForJSP(pageContext.getAttribute("expSumTotSlot"))%></td>
				<td align="right"><%=FormatNumber.formatNumberForJSP(pageContext.getAttribute("trngTotSale"))%></td>
				<s:if test="%{serviceId == 7}">
					<td>&nbsp;</td>
					<td align="right"><%=FormatNumber.formatNumberForJSP(pageContext.getAttribute("incentiveTot"))%></td>
				</s:if>
				
				<td>&nbsp;</td>
				<td>&nbsp;</td>
				<td>&nbsp;</td>
				<s:if test="%{serviceId == 7}">
					<td>&nbsp;</td>
				</s:if>
				<td>&nbsp;</td>
			</tr>
			<tr>
				<s:if test="%{trngExpType neq 'WEEKLY'}">
					<td colspan="5" align="center">
						<input type="button" name="submit" value="Approve All" class="button" onclick="updateAllAgent()" />
					</td>
					<td colspan="8" align="center">
				</s:if>
				<s:else>
					<td colspan="13" align="center">
				</s:else>
					<input type="button" name="submit" value="Approve Selected" class="button" onclick="updateSelected()" />
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
	$RCSfile: bo_act_training_exp_Result.jsp,v $ $Revision: 1.3 $
</code>