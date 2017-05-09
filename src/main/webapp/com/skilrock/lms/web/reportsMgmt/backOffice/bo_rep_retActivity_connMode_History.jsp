
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<table border="1" cellpadding="2" cellspacing="0" bordercolor="#CCCCCC"
	align="center" width="100%">
	<s:if test="conDevice.size>0">
		<tr>
			<th align="center" rowspan="2">
				Date
			</th>
			<th align="center" colspan="<s:property value="conDevice.size"/>">
				Connectivity Mode Name
			</th>
		</tr>
		<tr>
			<s:iterator value="conDevice">
				<th align="center">
					<s:property />
				</th>
			</s:iterator>
		</tr>
	</s:if>
	<s:if test="connModeMap.size>0">
		<s:iterator value="connModeMap">

			<tr class="startSortable">
				<td>
					<s:property value="%{key}" />
				</td>
				<s:iterator value="%{value}">
					<td>
						<s:property />
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

<code id="headId" style="visibility: hidden">
	$RCSfile: bo_rep_retActivity_connMode_History.jsp,v $ $Revision:
	1.1.2.6 $
</code>