<%@page import="com.skilrock.lms.common.utility.GetDate"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html>
	<head>
		<s:head theme="ajax" />
	</head>

	<body>

		<table style="margin-top: 20px" width="100%" border="1"
			cellpadding="3" cellspacing="0" bordercolor="#CCCCCC" align="center">
			<tr>
				<th colspan="2" align="center">
					<s:text name="label.terminal.sr.owner" />
				</th>
			</tr>




			<s:if test="#session.AGT_MODEL_SERIAL_MAP.size>0">

				<s:iterator id="AGT_MODEL_SERIAL_MAP"
					value="#session.AGT_MODEL_SERIAL_MAP">
					<tr width="">
						<td>
							<s:property value="%{key}" />
							<br>
						</td>
						<td>
							<s:property value="%{value}" />
						</td>
					</tr>
				</s:iterator>
			</s:if>



			<s:else>
				<tr>
					<td colspan="2" align="center">
						<s:text name="msg.no.result" />
					</td>
				</tr>
			</s:else>
		</table>




	</body>
</html>
<code id="headId" style="visibility: hidden">
	$RCSfile: bo_im_consNonConsSearchModelSerial_searchResult.jsp,v $
	$Revision: 1.3 $
</code>