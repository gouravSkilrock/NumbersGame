
<%@page import="com.skilrock.lms.common.utility.FormatNumber"%><%@ taglib
	prefix="s" uri="/struts-tags"%>
<div align="center">
	<h3>
		<u><s:text name="Direct_Player"/><s:text name="PWT"/> <s:text name="Report"/> <s:text name="label.for"/> <s:property value="%{agtOrgName}" /> </u>
	</h3>
</div>
<s:set name="DG" value="%{#session.isDG}"></s:set>
<s:set name="SE" value="%{#session.isSE}"></s:set>
<s:set name="SLE" value="%{#session.isSLE}"></s:set>
<s:set name="IW" value="%{#session.isIW}"></s:set>
<s:set name="VS" value="%{#session.isVS}"></s:set>
<s:set name="colCountForRet" value="1" />
<s:set name="colCount" value="1" />
<s:if test="%{#DG&&#SE}">
	<s:set name="colCount" value="1" />
	<s:set name="colCountForRet" value="2" />
</s:if>
<s:if test="%{#DG&&#SE&&#SLE}">
	<s:set name="colCount" value="2" />
	<s:set name="colCountForRet" value="3" />
</s:if>
<s:if test="%{#DG&&#IW}">
	<s:set name="colCount" value="1" />
	<s:set name="colCountForRet" value="2" />
</s:if>
<s:if test="%{#DG&&#VS}">
	<s:set name="colCount" value="1" />
	<s:set name="colCountForRet" value="2" />
</s:if>
<s:if test="%{#DG&&#SE&&#SLE&&#IW}">
	<s:set name="colCount" value="3" />
	<s:set name="colCountForRet" value="4" />
</s:if>
<s:if test="%{#DG&&#SE&&#SLE&&#VS}">
	<s:set name="colCount" value="3" />
	<s:set name="colCountForRet" value="4" />
</s:if>
<table width="100%" border="1" cellpadding="3" cellspacing="0"
	bordercolor="#CCCCCC" align="center">
	<tr>
		<th align="center" colspan="<s:property value="#colCountForRet"/>">
			<s:property value="#session.orgName" />
		</th>
		<th align="center">
			<s:text name="label.amt.in"/>
			<s:property value="#application.CURRENCY_SYMBOL" />
		</th>
	</tr>
	<tr>
		<td align="center" colspan="<s:property value="#colCountForRet+1"/>">
			<s:property value="#session.orgAdd" />
		</td>
	</tr>
	<s:if
		test="%{#session.result_retailer!=null&& #session.result_retailer.size>0}">
		<tr align="center">
			<th rowspan="2">
				<s:text name="label.org.name"/>
			</th>
			<s:if test="%{#DG}">
				<th>
					<s:text name="label.draw.game"/>
				</th>
			</s:if>
			<s:if test="%{#SE}">
				<th>
					<s:text name="label.scratch.game"/>
				</th>
			</s:if>
			<s:if test="%{#SLE}">
				<th>
					<s:text name="label.sport.lot"/>
				</th>
			</s:if>
			<s:if test="%{#IW}">
				<th>
					<s:text name="label.instant.win"/>
				</th>
			</s:if>
			<s:if test="%{#VS}">
				<th>
					<s:text name="label.virtual.sport"/>
				</th>
			</s:if>
		</tr>
		<tr align="center">
			<s:if test="%{#DG&&#SE}">
				<th>
					<s:text name="PWT"/> <s:text name="label.amount"/>
				</th>
			</s:if>
			<th>
				<s:text name="PWT"/> <s:text name="label.amount"/>
			</th>
			<s:if test="%{#SLE}">
				<th>
					<s:text name="PWT"/> <s:text name="label.amount"/>
				</th>
			</s:if>
			<s:if test="%{#IW}">
				<th>
					<s:text name="PWT"/> <s:text name="label.amount"/>
				</th>
			</s:if>
			<s:if test="%{#VS}">
				<th>
					<s:text name="PWT"/> <s:text name="label.amount"/>
				</th>
			</s:if>
		</tr>

		<s:set name="dgPwt" value="0.0" />
		<s:set name="sePwt" value="0.0" />
		<s:set name="slePwt" value="0.0" />
		<s:set name="iwPwt" value="0.0" />
		<s:set name="vsPwt" value="0.0" />
		<s:iterator id="result" value="#session.result_retailer">
			<s:set name="orgValue" value="%{value}"></s:set>
			<tr class="startSortable">
				<td>
					<s:property value="%{#orgValue.orgName}" />
				</td>
				<s:if test="%{#DG}">
					<td align="right">
						<s:set name="drawPwt" value="%{#orgValue.drawDirPlyPwt}" />
						<%=FormatNumber.formatNumberForJSP(pageContext
										.getAttribute("drawPwt"))%>
					</td>
				</s:if>
				<s:if test="%{#SE}">
					<td align="right">
						<s:set name="scratchPwt" value="%{#orgValue.scratchDirPlyPwt}" />
						<%=FormatNumber.formatNumberForJSP(pageContext
										.getAttribute("scratchPwt"))%>
					</td>
				</s:if>
				<s:if test="%{#SLE}">
					<td align="right">
						<s:set name="slPwt" value="%{#orgValue.sleDirPlyPwt}" />
						<%=FormatNumber.formatNumberForJSP(pageContext
										.getAttribute("slPwt"))%>
					</td>
				</s:if>
				<s:if test="%{#IW}">
					<td align="right">
						<s:set name="iwPw" value="%{#orgValue.iwDirPlyPwt}" />
						<%=FormatNumber.formatNumberForJSP(pageContext
										.getAttribute("iwPw"))%>
					</td>
				</s:if>
				<s:if test="%{#VS}">
					<td align="right">
						<s:set name="vsPw" value="%{#orgValue.vsDirPlyPwt}" />
						<%=FormatNumber.formatNumberForJSP(pageContext
										.getAttribute("vsPw"))%>
					</td>
				</s:if>
				<s:set name="dgPwt" value="%{#dgPwt+#orgValue.drawDirPlyPwt}" />
				<s:set name="sePwt" value="%{#sePwt+#orgValue.scratchDirPlyPwt}" />
				<s:set name="slePwt" value="%{#slePwt+#orgValue.sleDirPlyPwt}" />
				<s:set name="iwPwt" value="%{#iwPwt+#orgValue.iwDirPlyPwt}" />
				<s:set name="vsPwt" value="%{#vsPwt+#orgValue.vsDirPlyPwt}" />
			</tr>
		</s:iterator>
		<tr>
			<th>
				<s:text name="label.total"/>
			</th>
			<s:if test="%{#DG}">
				<th align="right">
					<%=FormatNumber.formatNumberForJSP(pageContext
									.getAttribute("dgPwt"))%>
				</th>
			</s:if>
			<s:if test="%{#SE}">
				<th align="right">
					<%=FormatNumber.formatNumberForJSP(pageContext
									.getAttribute("sePwt"))%>
				</th>
			</s:if>
			<s:if test="%{#SLE}">
				<th align="right">
					<%=FormatNumber.formatNumberForJSP(pageContext
									.getAttribute("slePwt"))%>
				</th>
			</s:if>
			<s:if test="%{#IW}">
				<th align="right">
					<%=FormatNumber.formatNumberForJSP(pageContext
									.getAttribute("iwPwt"))%>
				</th>
			</s:if>
			<s:if test="%{#VS}">
				<th align="right">
					<%=FormatNumber.formatNumberForJSP(pageContext
									.getAttribute("vsPwt"))%>
				</th>
			</s:if>
		</tr>
	</s:if>
	<s:else>
		<tr>
			<th align="center">
				<s:text name="msg.no.record"/>
			</th>
		</tr>
	</s:else>
</table>

<code id="headId" style="visibility: hidden">
$RCSfile: bo_rep_DirPlyPwtReport_RetailerWise.jsp,v $
$Revision: 1.3 $
</code>