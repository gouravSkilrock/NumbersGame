
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<table border="1" width="50%" cellspacing="0" cellpadding="2"
	bordercolor="#CCCCCC">
	<tr>
		<td align="center" colspan="2">
			<div id="serviceDivErr"></div>
		</td>
	</tr>
	<tr>
		<th align="center" colspan="2">
			Available Services
		</th>
	</tr>
	<tr>
		<th align="center">
			Service Name
		</th>
		<th align="center">
			InterFace type
		</th>
	</tr>
	<s:iterator value="%{#session.service_list}" status="index">


		<tr>
			<td align="left">
				<input type="hidden" id="div<s:property value="#index.index" />"
					name="statusTable"
					value="ACTIVE-<s:property value="privRepTable"/>" />
				<s:if test="%{statusInterface=='ACTIVE'}">
					<input type="checkbox" name="serviceData"
						id="chk<s:property value="#index.index" />"
						onclick="setStatus(this.id,'<s:property value="statusInterface"/>-<s:property value="privRepTable"/>');" checked="checked" />
				</s:if>
				<s:else>
					<input type="checkbox" disabled="disabled" />
				</s:else>
				<input type="hidden" name="id"
					value="<s:property value="mappingId"/>" />

				<s:property value="%{serviceName}" />
			</td>
			<td align="center">
				<s:property value="%{interfaceType}" />
			</td>
		</tr>
	</s:iterator>
</table>

<code id="headId" style="visibility: hidden">
$RCSfile: bo_um_agentReg_Service.jsp,v $
$Revision: 1.2 $
</code>