<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%
	response.setHeader("Cache-Control", "no-store"); //HTTP 1.1
	response.setHeader("Pragma", "no-cache"); //HTTP 1.0
	response.setDateHeader("Expires", 0); //prevents caching at the proxy server
%>

<table width="100%" border="1" cellpadding="2" cellspacing="0"
	bordercolor="#CCCCCC" align="center" valign="top" id="searchTable">
	<tr>
		<td align="left">
			<b><input type="checkbox" id="selAll" name="retName" value="-1"
					onclick="selAllChkBx();" />Select All Organization's </b>
		</td>
	</tr>
	<s:iterator id="task" value="#session.RET_MAP" status="taskIndex">
		<tr>
			<td align="left">
				<b><input type="checkbox"
						id="P<s:property value="#taskIndex.index" />"
						name="agtName"
						value="<s:property value="%{key}" />"
						onclick="fillChk('<s:property value="#taskIndex.index" />');" />
					<s:property value="%{key}" /> </b>
			</td>
		</tr>
		<tr>
			<td>
				<table id="tab<s:property value="#taskIndex.index" />">
					<s:set name="retList" value="%{value}"></s:set>
					<tr>
						<s:iterator value="#retList" status="retIndex">
							<td>
								<input type="checkbox" name="retName" value="<s:property value='%{key}' />~<s:property value='%{value}' />" />
								<s:property value='%{value}' />
							</td>
							<s:if test="(#retIndex.index+1)%5==0">
					</tr>
					<tr>
						</s:if>
						</s:iterator>
					</tr>
				</table>
			</td>
		</tr>
	</s:iterator>
	<tr>
		<td colspan="5" align="center">
			<table>
				<s:submit name="submit" value="Submit" cssClass="button" />
			</table>
		</td>
	</tr>
</table>

<code id="headId" style="visibility: hidden">
$RCSfile: bo_um_advMessage_Search.jsp,v $
$Revision: 1.2 $
</code>