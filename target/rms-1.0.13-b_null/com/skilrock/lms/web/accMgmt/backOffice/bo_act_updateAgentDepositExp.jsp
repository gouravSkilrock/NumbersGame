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
	<tr>
		<th width="5%">
			<s:text name="label.sNo" />
		</th>
		<th width="25%">
			<s:text name="label.agt.name" />
		</th>
		<th width="10%">
			<s:text name="label.default.dep.comm" />
		</th>
		<th width="15%">
			<s:text name="Variance" />
		</th>
		<th width="10%">
			<s:text name="label.total.dep.comm" />
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
	<s:iterator id="task" value="%{depositOrgCommMap}" status="taskIndex">
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
				<s:hidden id="defDepositPer" value="%{value.defaultCommVar}"></s:hidden>
				<s:property value="value.defaultCommVar" />
			</td>
			<td>
				<input type="text" name="depositVar"
					id="depositVar<s:property value="#taskIndex.index+1" />"
					value="<s:property value="value.commVar" />" maxLength="10"
					size="5" onkeypress="return onlyDecimalSigned(event,this)"
					onchange="updRowColor('<s:property value="#taskIndex.index+1" />')" />
			</td>
			<td align="right">
				<s:set name="valueToDisplay" value="%{value.totalCommVar}" />
				<%=FormatNumber.formatNumberForJSP(pageContext.getAttribute("valueToDisplay")) %>
			</td>
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
	$RCSfile: bo_act_updateAgentDepositExp.jsp,v $ $Revision: 1.3 $
</code>