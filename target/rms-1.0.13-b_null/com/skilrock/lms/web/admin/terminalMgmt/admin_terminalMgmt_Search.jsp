<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%
	response.setHeader("Cache-Control", "no-store"); //HTTP 1.1
	response.setHeader("Pragma", "no-cache"); //HTTP 1.0
	response.setDateHeader("Expires", 0); //prevents caching at the proxy server
%>
<table align="center" border="0" width="100%">
							<s:select name="brandsId" id="brand" key="label.brand.name"
							headerKey="-1" headerValue="%{getText('label.please.select')}"
							list="#session.BRAND" cssClass="option"
							onchange="chageBrandList(this.value, 'model', 'model_list','-1');_id.o('invNo').options.length=1;" />
							
							<s:select name="Spec" id="model_list" theme="simple" cssStyle="display:none" list="%{modelMap}" />
							
							<s:select name="modelId" id="model" key="label.model.name"
							headerKey="-1" headerValue="%{getText('label.please.select')}"
							list="{}" cssClass="option"
							onchange="fetchRett('AgentWise');" />
		<tr>
			<td>
				<s:select id="version" name="version" headerKey="-1" headerValue="%{getText('label.please.select')}" list="#session.VER_LIST" label="Select version"  cssClass="option"/>
			</td>
		</tr>
</table>
<br/>
<table width="100%" border="1" cellpadding="2" cellspacing="0"
	bordercolor="#CCCCCC" align="center" valign="top" id="searchTable" style="display: none;">
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
				<table id="tab<s:property value="#taskIndex.index" />" width="100%">
					<s:set name="retList" value="%{value}"></s:set>
					<tr>
						<s:iterator value="#retList" status="retIndex">
							<td>
								<input type="checkbox" name="retName" value="<s:property />" />
								<s:property />
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
$RCSfile: admin_terminalMgmt_Search.jsp,v $
$Revision: 1.3 $
</code>