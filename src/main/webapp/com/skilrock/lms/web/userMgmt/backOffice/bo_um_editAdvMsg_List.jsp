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
		<th>
			<s:text name="label.sNo" />
		</th>
		<th>
			<s:text name="label.subject" />
		</th>
		<th>
			<s:text name="label.content" />
		</th>
		<th>
			<s:text name="label.msg.date" />
		</th>
		<th>
			<s:text name="label.status" />
		</th>
		<th>
			<s:text name="label.update" />
		</th>
	</tr>
	<s:iterator value="messageList" status="itr">
		<tr>
			<td>
				<s:property value="#itr.index+1" />
			</td>
			<td>
				<s:property value="messageSubject" />
			</td>
			<td>
				<s:if test="%{messageContent.length()<50}">
					<s:property value="messageContent" />
				</s:if>
				<s:else>
					<s:property value="messageContent.substring(0, 50)" /> ...
				</s:else>
			</td>
			<td>
				<s:property value="messageDate" />
			</td>
			<td>
				<s:select name="status" id="status_%{messageId}"
					list="{'ACTIVE', 'INACTIVE'}" value="%{status}" cssClass="option"
					theme="simple" />
			</td>
			<td>
				<s:a href="#" onclick="updateMessageStatus('%{messageId}');">
					<s:text name="label.update" />
				</s:a>
			</td>
		</tr>
	</s:iterator>
</table>

<code id="headId" style="visibility: hidden">
	$RCSfile: bo_um_editAdvMsg_List.jsp,v $ $Revision: 1.3 $
</code>