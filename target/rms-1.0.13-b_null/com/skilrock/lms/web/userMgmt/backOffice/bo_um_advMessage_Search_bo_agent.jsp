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
			<s:iterator id="task" value="%{organizationList}" status="taskIndex">
				<td>
					<input type="checkbox" name="retName"
						value="<s:property value='orgId' />~<s:property value='orgName' />" />
					<s:property value='orgName' />
				</td>
				<s:if test="%{#taskIndex.index%5 == 4}">
					</tr><tr>
				</s:if>
			</s:iterator>
		</tr>
	<tr>
		<td colspan="5" align="center">
			<table>
				<s:submit name="submit" key="btn.submit" cssClass="button" />
			</table>
		</td>
	</tr>
</table>

<code id="headId" style="visibility: hidden">
	$RCSfile: bo_um_advMessage_Search_bo_agent.jsp,v $ $Revision: 1.3 $
</code>