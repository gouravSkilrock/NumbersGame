<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%
	response.setHeader("Cache-Control", "no-store"); //HTTP 1.1
	response.setHeader("Pragma", "no-cache"); //HTTP 1.0
	response.setDateHeader("Expires", 0); //prevents caching at the proxy server
%>

<script type="text/javascript"
	src="<%=request.getContextPath() %>/com/skilrock/lms/web/common/globalJs/jquery-1.10.1.min.js"></script>

<s:set name="parentData" value="#session.DashBoardData.get(0)" />
<table width="100%" border="1" cellpadding="2" cellspacing="0"
	bordercolor="#CCCCCC" align="center">
	<tr>
		<th><s:text name="label.total.limit" /></th>
		<th><s:property value="%{#parentData.liveBal}" /> <s:hidden name="totLimit" id="totLimit" value="%{#parentData.liveBal}" /></th>
		<th><s:text name="label.dist.limit" /></th>
		<th><s:property value="%{#parentData.distributedBal}" /></th>
		<th><s:text name="label.bal.limit" /></th>
		<th id="balLimClr"><s:property value="%{#parentData.distributableBal}" /> <s:hidden name="balLimit" id="balLimit" value="%{#parentData.distributableBal}" /></th>
		<th wrap="wrap"><s:text name="label.effective.bal.after.change" />
		</th>
		<th style="background-color: #000000"><div style="color: #FFFFFF" id="effBalId"><s:property value="%{#parentData.distributableBal}" /></div></th>
		<s:if test="%{#parentData.orgId!=0}">
			<th><input type="button" value="<s:text name="label.refresh.data" />" onclick="fetchActData('<s:property value="%{#parentData.orgId}" />')" class="button" /></th>
		</s:if>
	</tr>
</table>
<br />
<s:set name="childDataMap" value="#session.DashBoardData.get(1)" />

<s:if test="#childDataMap.size()>0">
	<div id="dataDiv" style="">
		<table width="100%" border="1" cellpadding="2" cellspacing="0" bordercolor="#CCCCCC" align="center" valign="top" id="dataTable">			
				<s:if test="%{#application.COUNTRY_DEPLOYED=='BENIN'}">
			<tr>
				<th rowspan="2"><s:text name="label.sNo" /></th>
				<th nowrap="nowrap" rowspan="2"><s:text name="label.ret.name" /></th>
				<th rowspan="2"><s:text name="label.location" /></th>
				<th rowspan="2"><s:text name="label.offline" /></th>
				<th rowspan="2"><s:text name="label.last.login.device" /></th>
				<th rowspan="2"><s:text name="label.limit" /></th>
				<th rowspan="2"><s:text name="label.inc" /></th>
				<th rowspan="2"><s:text name="label.dec" /></th>
				<!--<th>Effective After Change</th>-->
				<th colspan="2"><s:text name="label.outstanding"/></th>
				<th rowspan="2"><s:text name="label.status" /></th>
				<th rowspan="2"><input type="checkbox" onclick="selAll(this.checked)" /> <s:text name="label.update.status" /></th>
				<th rowspan="2"><s:text name="label.update" /></th>
				<!-- <th>Activity Status</th><th>RG Status</th> -->
				<th rowspan="2"><s:text name="label.msg" /></th>
			</tr>
			</s:if>
			<s:if test="%{#application.COUNTRY_DEPLOYED=='GHANA' || #application.COUNTRY_DEPLOYED=='NIGERIA' || #application.COUNTRY_DEPLOYED=='SIKKIM' || #application.COUNTRY_DEPLOYED=='INDIA'}">
				<tr>
					<th rowspan="1"><s:text name="label.sNo" /></th>
					<th nowrap="nowrap" rowspan="1"><s:text name="label.ret.name" /></th>
					<!-- Commented as per the requirement from Lagos in Release R5 -->
					<%-- <th rowspan="1"><s:text name="label.location" /></th> --%>
					<th rowspan="1"><s:text name="label.offline" /></th>
					<th rowspan="1"><s:text name="label.last.login.device" /></th>
					<th rowspan="1"><s:text name="label.limit" /></th>
					<th rowspan="1"><s:text name="label.inc" /></th>
					<th rowspan="1"><s:text name="label.dec" /></th>
					<!--<th>Effective After Change</th>-->
					<s:if test="%{#application.COUNTRY_DEPLOYED=='BENIN'}">
						<th colspan="1"><s:text name="label.outstanding" /></th>
					</s:if>
					<th rowspan="1"><s:text name="label.status" /></th>
					<th rowspan="1"><input type="checkbox" onclick="selAll(this.checked)" /> <s:text name="label.update.status" /></th>
					<th rowspan="1"><s:text name="label.update" /></th>
					<!--<th>Activity Status</th><th>RG Status</th> -->
					<th rowspan="1"><s:text name="label.msg" /></th>
				</tr>
			</s:if>
			
			<s:if test="%{#application.COUNTRY_DEPLOYED=='BENIN'}">
				<tr>
					<th><s:text name="label.total.amt" /></th>
					<th><s:text name="label.from"/> <s:text name="label.num.days"/></th>
				</tr>
			</s:if>
			<s:iterator id="task" value="#childDataMap" status="taskIndex">
				<tr id="row<s:property value="#taskIndex.index+1" />">
					<td><s:property value="#taskIndex.index+1" /> <s:hidden name="orgId" value="%{value.orgId}" /></td>
					<td style="font-weight: bold;" nowrap="nowrap"><s:property value="value.orgName" /></td>
					<!-- Commented as per the requirement from Lagos in Release R5 -->
					<%-- <td nowrap="nowrap"><s:property value="value.location" /></td> --%>
					<td><s:property value="value.webType" /></td>
					<td nowrap="nowrap"><s:property value="value.deviceName" /></td>
					<td align="right"><s:property value="value.balance" /> <s:hidden name="balance" value="%{value.balance}" /></td>
					<td><input type="text" name="incAmt" value="0" maxLength="10" size="5" onkeypress="return onlyDecimal(this)" onchange="updRowColor('<s:property value="#taskIndex.index+1" />');showNetLimit();" /></td>
					<td><input type="text" name="decAmt" value="0" maxLength="10" size="5" onkeypress="return onlyDecimal(this)" onchange="updRowColor('<s:property value="#taskIndex.index+1" />');showNetLimit();" /></td>
					<s:if test="%{#application.COUNTRY_DEPLOYED=='BENIN'}">
						<td align="right"><s:property value="value.closingBalance" /><s:hidden name="closingBalance" value="%{value.closingBalance}" /></td>
						<td align="right"><s:property value="value.noOfDays" /> <s:hidden name="noOfDays" value="%{value.noOfDays}" /></td>
					</s:if>
					<!-- <td>
						<input type="text" name="effAmt" value="0" maxLength="10" size="5" disabled="disabled"
							onkeypress="return onlyDecimal(this)"
							onchange="updRowColor('<s:property value="#taskIndex.index+1" />')" />
					</td> -->
					<td>
						<s:if test="%{value.orgStatus == 'INACTIVE'}">
							<select name="orgStatus" onchange="updRowColor('<s:property value="%{#taskIndex.index+1}"/>')" id="Status" class="option">
								<option value="ACTIVE"><s:text name="ACTIVE" /></option>
								<option value="INACTIVE" disabled selected><s:text name="INACTIVE" /></option>
								<option value="BLOCK"><s:text name="BLOCK" /></option>
							</select>
						</s:if> <s:elseif test="%{value.orgStatus == 'ACTIVE'}">
							<select name="orgStatus" onchange="updRowColor('<s:property value="%{#taskIndex.index+1}"/>')" id="Status" class="option">
								<option value="ACTIVE" selected><s:text name="ACTIVE" /></option>
								<option value="INACTIVE"><s:text name="INACTIVE" /></option>
								<option value="BLOCK"><s:text name="BLOCK" /></option>
							</select>
						</s:elseif> <s:else>
							<select name="orgStatus" onchange="updRowColor('<s:property value="%{#taskIndex.index+1}"/>')" id="Status" class="option">
								<option value="ACTIVE"><s:text name="ACTIVE" /></option>
								<option value="INACTIVE" disabled><s:text name="INACTIVE" /></option>
								<option value="BLOCK" selected><s:text name="BLOCK" /></option>
							</select>
						</s:else></td>
					<td><input type="checkbox" name="tempStatus" /></td>
					<td><input type="button" onclick="updateData('<s:property value="#taskIndex.index" />')" value="<s:text name="label.update"/>" class="button" /></td>
					<!--<td>Activity Status</td><td>RG Status</td> -->
					<td><s:property value="value.errorMsg" /></td>
				</tr>
			</s:iterator>
			<tr>
				<td colspan="5" align="center"><table><input type="button" name="submit" value="<s:text name="btn.submit"/>" class="button" onclick="updateAll()" /></table></td>
				<td colspan="2" align="center"><table><input type="button" name="submit" value="<s:text name="btn.activate.selected"/>" class="button" onclick="updateStatus('ACTIVE')" /></table></td>
				<td colspan="2" align="center"><table><input type="button" name="submit" value="<s:text name="btn.block.selected"/>" class="button" onclick="updateStatus('BLOCK')" /></table></td>
			</tr>
		</table>
	</div>
</s:if>
<s:else>
	<div style="text-align: center"><s:text name="msg.no.record" /></div>
</s:else>

<code id="headId" style="visibility: hidden">
	$RCSfile: bo_rep_dashBoard_Result.jsp,v $ $Revision: 1.3 $
</code>