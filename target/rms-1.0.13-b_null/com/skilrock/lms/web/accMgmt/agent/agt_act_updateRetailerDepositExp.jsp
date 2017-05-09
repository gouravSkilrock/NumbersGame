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
			S.No
		</th>
		<th width="25%">
			Agent Name
		</th>
		<th width="10%">
			Default Deposit Comm 
		</th>
		<th width="15%">
			Variance
		</th>
		<th width="10%">
			Total Deposit Comm
		</th>
		<th width="15%">
			<input type="checkbox" onclick="selAll(this.checked)" />
			Update All
		</th>
	<!-- 	<th>
			Update
		</th>  -->
		<th width="30%">
			Message
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
					value="<s:property value="value.commVar" />"
					maxLength="10" size="5" onkeypress="return onlyDecimalSigned(event,this)"
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
				<div  id="msg<s:property value="#taskIndex.index+1" />"></div>
			</td>
		</tr>
	</s:iterator>
	<tr>
		<td colspan="5" align="center">
			<table>
				<input type="button" onclick="updateRetailerSelected()"
					value="Update Selected" class="button" />
			</table>
		</td>
	</tr>
</table>
<code id="headId" style="visibility: hidden">
	$RCSfile: agt_act_updateRetailerDepositExp.jsp,v $ $Revision: 1.2 $
</code>