<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="com.skilrock.lms.common.utility.FormatNumber"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%
	response.setHeader("Cache-Control", "no-store"); //HTTP 1.1
	response.setHeader("Pragma", "no-cache"); //HTTP 1.0
	response.setDateHeader("Expires", 0); //prevents caching at the proxy server
%>

<table width="100%" border="1" cellpadding="2" cellspacing="0"
	bordercolor="#CCCCCC" align="center" valign="top">
	<s:if test="%{serviceId == 7}">
	  <tr>
		<th width="5%">
			<s:text name="label.sNo" />
		</th>
		<th width="25%">
			<s:text name="label.agt.name" />
		</th>
		<th width="7%">
			<s:text name="label.default.trn.exp" />
		</th>
		<th width="10%">
			<s:text name="Variance" />
		</th>
		<th width="7%">
			<s:text name="label.total.trn.exp" />
		</th>
		<th width="7%">
			<s:text name="label.def.promo.trn.exp" />
		</th>
		<th width="10%">
			<s:text name="label.promo.var" />
		</th>
		<th width="7%">
			<s:text name="label.tot.promo.trn.exp" />
		</th>
		<th width="7%">
			<s:text name="label.incentive.exp" />
		</th>
		<th width="10%">
			<s:text name="label.incentive.var" />
		</th>
		<th width="7%">
			<s:text name="label.tot.incentive.exp" />
		</th>
		<th width="12%">
			<input type="checkbox" onclick="selAll(this.checked)" />
			<s:text name="label.update.all" />
		</th>
		<!-- 	<th>
			Update
		</th>  -->
		<th width="30%">
			<s:text name="label.msg" />
		</th>
	</tr>
	</s:if>
	<s:else>
		<tr>
		<th width="5%">
			<s:text name="label.sNo" />
		</th>
		<th width="25%">
			<s:text name="label.agt.name" />
		</th>
		<th width="10%">
			<s:text name="label.default.trn.exp" />
		</th>
		<th width="15%">
			<s:text name="Variance" />
		</th>
		<th width="10%">
			<s:text name="label.total.trn.exp" />
		</th>
		<th width="10%">
			<s:text name="label.def.promo.trn.exp" />
		</th>
		<th width="15%">
			<s:text name="label.promo.var" />
		</th>
		<th width="10%">
			<s:text name="label.tot.promo.trn.exp" />
		</th>
		<th width="15%">
			<input type="checkbox" onclick="selAll(this.checked)" />
			<s:text name="label.update.all" />
		</th>
		<!-- 	<th>
			Update
		</th>  -->
		<th width="30%">
			<s:text name="label.msg" />
		</th>
	</tr>
	</s:else>
	<s:iterator id="task" value="#session.tranOrgMap" status="taskIndex">
		<s:set name="bean" value="%{value}" />
		<tr id="row<s:property value="#taskIndex.index+1" />">
			<td>
				<s:property value="#taskIndex.index+1" />
				<s:hidden name="agtId" value="%{value.orgId}" />
			</td>
			<td>
				<s:property value="value.orgName" />
			</td>
			<td align="right">
				<s:hidden id="defTranPer" value="%{value.trainingPer}"></s:hidden>
				<s:property value="value.trainingPer" />
			</td>
			<td>
				<input type="text" name="trVar"
					id="trVar<s:property value="#taskIndex.index+1" />"
					value="<s:property value="value.trainingPerVariance" />"
					maxLength="10" size="5"
					onkeypress="return onlyDecimalSigned(event,this)"
					onchange="updRowColor('<s:property value="#taskIndex.index+1" />')" />
			</td>
			<td align="right">
				<s:set name="valueToDisplay"
					value="%{value.trainingPer+value.trainingPerVariance}" />
				<%=FormatNumber.formatNumberForJSP(pageContext.getAttribute("valueToDisplay")) %>
			</td>
			<td align="right">
				<s:hidden id="defExtraTranPer" value="%{value.extraTrainingPer}"></s:hidden>
				<s:property value="value.extraTrainingPer" />
			</td>
			<td>
				<input type="text" name="extraTrVar"
					id="extraTrVar<s:property value="#taskIndex.index+1" />"
					value="<s:property value="value.extraTrainingPerVariance" />"
					maxLength="10" size="5"
					onkeypress="return onlyDecimalSigned(event,this)"
					onchange="updRowColor('<s:property value="#taskIndex.index+1" />')" />
			</td>
			<td align="right">
				<s:set name="extraValueToDisplay"
					value="%{value.extraTrainingPer+value.extraTrainingPerVariance}" />
				<%=FormatNumber.formatNumberForJSP(pageContext.getAttribute("extraValueToDisplay")) %>
			</td>

			<!-- For Incentive Start -->
			<s:if test="%{serviceId == 7}">
				<td align="right">
					<s:hidden id="defIncentivePer" value="%{value.incentivePer}"></s:hidden>
					<s:property value="value.incentivePer" />
				</td>
			
				<td>
					<input type="text" name="incentiveVar"
						id="incentiveVar<s:property value="#taskIndex.index+1" />"
						value="<s:property value="value.incentivePerVariance" />"
						maxLength="10" size="5"
						onkeypress="return onlyDecimalSigned(event,this)"
						onchange="updRowColor('<s:property value="#taskIndex.index+1" />')" />
				</td>
			
				<td align="right">
					<s:set name="incentiveValueToDisplay"
						value="%{value.incentivePer+value.incentivePerVariance}" />
					<%=FormatNumber.formatNumberForJSP(pageContext.getAttribute("incentiveValueToDisplay")) %>
				</td>
			</s:if>
			<!-- For Incentive End -->

			<td>
				<input type="checkbox" name="tempStatus"
					id="chkBox<s:property value="#taskIndex.index+1" />" />
			</td>
			<!--  	<td>
				<input type="button"
					onclick="updateData('<s:property value="#taskIndex.index" />')"
					value="Update" class="button" />
			</td> -->
			<td>
				<s:property value="value.errorMsg" />
				<div id="msg<s:property value="#taskIndex.index+1" />"></div>
			</td>
		</tr>
	</s:iterator>
	<tr>
		<td colspan="5" align="center">
			<table>
				<input type="button" onclick="updateSelected()"
					value="<s:text name="btn.update.selected" />" class="button" />
			</table>
		</td>
	</tr>
</table>

<code id="headId" style="visibility: hidden">
	$RCSfile: bo_act_updateAgentTrainnigExp.jsp,v $ $Revision: 1.1.2.4.8.2
	$
</code>