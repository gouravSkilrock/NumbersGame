<%@page import="com.skilrock.lms.common.utility.GetDate"%><%@ taglib
	prefix="s" uri="/struts-tags"%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html>
	<head>
		<s:head theme="ajax" />
	</head>

	<body>

		<table width="100%" border="1" cellpadding="3" cellspacing="0"
			bordercolor="#CCCCCC" align="center">
			<tr>
				<th colspan="6" align="center">
					<s:text name="label.non.cons.inv.wise.detail" />
				</th>
			</tr>
			<tr>
				<th align="center">
					<s:text name="label.owner.name" />
				</th>
				<th align="center">
					<s:text name="label.owner.type" />
				</th>
				<th align="center">
					<s:text name="label.inv.name" />
				</th>
				<th align="center">
					<s:text name="label.model.name" />
				</th>
				<th align="center">
					<s:text name="label.count" />
				</th>

			</tr>

			<s:if test="#session.INV_WISE_DET_LIST.size>0">
				<s:iterator id="beanCart" value="#session.INV_WISE_DET_LIST">
					<tr>
						<td align="left">
							<s:property value="ownerName" />
						</td>
						<td align="left">
							<s:property value="ownerType" />
						</td>
						<td align="left">
							<s:property value="invName" />
						</td>
						<td align="left">
							<s:property value="modelName" />
						</td>
						<td align="right">
							<s:property value="count" />
						</td>
					</tr>
				</s:iterator>
			</s:if>
			<s:else>
				<tr>
					<td colspan="9" align="center">
						<s:text name="msg.no.result" />
					</td>
				</tr>
			</s:else>
		</table>

	</body>
</html>
<code id="headId" style="visibility: hidden">
	$RCSfile: bo_im_consSearchInv_detResult.jsp,v $ $Revision: 1.3 $
</code>