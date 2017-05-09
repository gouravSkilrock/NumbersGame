<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%
	response.setHeader("Cache-Control", "no-store"); //HTTP 1.1
	response.setHeader("Pragma", "no-cache"); //HTTP 1.0
	response.setDateHeader("Expires", 0); //prevents caching at the proxy server
%>
<s:set name="childDataMap" value="#session.DashBoardNewData.get(0)" />

<s:if test="#childDataMap.size()>0">
	<div id="dataDiv" style="">
		<table width="100%" border="1" cellpadding="2" cellspacing="0"
			bordercolor="#CCCCCC" align="center" valign="top" id="dataTable">
			<tr>
				<th>
					<s:text name="label.sNo" />
				</th>
				<th nowrap="nowrap">
					<s:text name="label.agt.name" />
				</th>
				<th>
					<s:text name="label.location" />
				</th>

				<th>
					<s:text name="label.limit" />
				</th>
				<th>
					<s:text name="label.cl.inc" />
				</th>
				<th>
					<s:text name="label.cl.dec" />
				</th>
				<th>
					<s:text name="XCL" />

				</th>
				<th>
					<s:text name="label.num.days" />
				</th>

				<!--<th>
				Effective After Change
			</th>-->
				<th>
					<s:text name="label.status" />
				</th>

				<th>
					<input type="checkbox" onclick="selAll(this.checked)" />
					<s:text name="label.select.all" />
				</th>
				<th>
					<s:text name="label.update" />
				</th>
				<!-- 
			<th>
				Activity Status
			</th>
			<th>
				RG Status
			</th> -->
				<th>
					<s:text name="label.msg" />
				</th>
			</tr>
			<s:iterator id="task" value="#childDataMap" status="taskIndex">
				<tr id="row<s:property value="#taskIndex.index+1" />">
					<s:set name="xclAmtTemp" value="value.xclAmt" />
					<s:set name="xclDaysTemp" value="value.xclDays" />
					<td>
						<s:property value="#taskIndex.index+1" />
						<s:hidden name="orgId" value="%{value.orgId}" />
					</td>
					<td style="font-weight: bold;" nowrap="nowrap">
						<s:property value="value.orgName" />
					</td>
					<td nowrap="nowrap">
						<s:property value="value.location" />
					</td>
					<td align="right">
						<s:property value="value.balance" />
						<s:hidden name="balance" value="%{value.balance}" />
					</td>
					<td>
						<input type="text" name="incAmt" value="0" maxLength="10" size="5"
							onkeypress="return onlyDecimal(this)"
							onchange="updRowColor('<s:property value="#taskIndex.index+1" />');showNetLimit();" />
					</td>
					<td>
						<input type="text" name="decAmt" value="0" maxLength="10" size="5"
							onkeypress="return onlyDecimal(this)"
							onchange="updRowColor('<s:property value="#taskIndex.index+1" />');showNetLimit();" />
					</td>
					<td>
						<input type="text" name="xclAmt"
							value="<%=(String)pageContext.getAttribute("xclAmtTemp") %>"
							maxLength="10" size="5" onkeypress="return onlyDecimal(this)"
							onchange="updRowColor('<s:property value="#taskIndex.index+1" />');showNetLimit();" />
					</td>
					<td>
						<input type="text" name="xclDays"
							value="<%=(String)pageContext.getAttribute("xclDaysTemp") %>"
							maxLength="6" size="5" onkeypress="return onlyDecimal(this)"
							onchange="updRowColor('<s:property value="#taskIndex.index+1" />');showNetLimit();" />
					</td>
					<td>
						<s:textfield theme="simple" readonly="true" name="orgStatus"
							size="8" value="%{value.orgStatus}" />
					</td>
					<td align="center">
						<input type="checkbox" name="tempStatus" />
					</td>
					<td>
						<input type="button"
							onclick="return updateData('<s:property value="#taskIndex.index" />')"
							value="<s:text name="label.update"/>" class="button" />
					</td>
					<!--<td>
						Activity Status
					</td>
					<td>
						RG Status
					</td> -->
					<td>
						<s:property value="value.errorMsg" />
					</td>

				</tr>
			</s:iterator>
			<tr>
				<td colspan="5" align="center">
					<table>
						<input type="button" name="submit"
							value="<s:text name="btn.submit"/>" class="button"
							onclick="return updateAll()" />
					</table>
				</td>
				<!-- 	<td colspan="2" align="center">
					<table>
						<input type="button" name="submit" value="Activate Selected"
							class="button" onclick="updateStatus('ACTIVE')" />
					</table>
				</td>
				<td colspan="2" align="center">
					<table>
						<input type="button" name="submit" value="InActivate Selected"
							class="button" onclick="updateStatus('INACTIVE')" />
					</table>
				</td> -->
			</tr>
		</table>
</s:if>
<s:else>
	<div style="text-align: center">
		<s:text name="msg.no.record" />
	</div>
</s:else>

<code id="headId" style="visibility: hidden">
	$RCSfile: bo_um_dashBoard_new_Result.jsp,v $ $Revision: 1.3 $
</code>