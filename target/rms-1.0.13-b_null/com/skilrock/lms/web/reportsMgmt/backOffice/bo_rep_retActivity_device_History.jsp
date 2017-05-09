
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<table border="1" cellpadding="2" cellspacing="0" bordercolor="#CCCCCC"
	align="center" width="100%">
	<s:if test="terminalType.size>0">
		<tr>
			<th align="center" rowspan="2">
				Date
			</th>
			<th align="center" colspan="<s:property value="terminalType.size"/>">
				Device Name
			</th>
		</tr>
		<tr>
			<s:iterator value="terminalType">
				<th align="center">
					<s:property />
				</th>
			</s:iterator>
		</tr>
	</s:if>

	<s:if test="deviceMap.size>0">
		<s:iterator value="deviceMap">
			<s:set name="count" value="0" />
			<tr class="startSortable">
				<td>
					<s:property value="%{key}" />
				</td>
				<s:iterator value="%{value}">
						<td>
						<s:set name="data" value="%{value[#count]}" />
						<a href="#" onclick="showdetails('<s:property value="%{key}" />','<s:property value="%{#data.split('Nxt')[1]}" />')">
						<s:property value="%{#data.split('Nxt')[0]}" />
						</a><!--
						<s:a targets="posVersionDiv" theme="ajax"
							href="bo_rep_retActivity_pos_version_History.action?start_date=%{key}&selectDevice=%{#data.split('Nxt')[1]}">
							<s:property value="%{#data.split('Nxt')[0]}" />
						</s:a>
						--><s:set name="count" value="#count+1" />
					</td>
				</s:iterator>
			</tr>

		</s:iterator>
	</s:if>
	<s:else>
		<tr>
			<td colspan="17" align="center">
				No Records to Process
			</td>
		</tr>
	</s:else>
</table>
<br/><br/>
<div id="posVersionDiv"></div>
<code id="headId" style="visibility: hidden">
	$RCSfile: bo_rep_retActivity_device_History.jsp,v $ $Revision: 1.1.2.6
	$
</code>