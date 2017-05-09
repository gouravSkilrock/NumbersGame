
<%@page import="com.skilrock.lms.common.utility.FormatNumber"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<!-- <div id="excel" align="center" text-align:left;width: 750px;font-size: 9pt">
	<s:a
		href="bo_rep_CompltCollReport_ExpExcel.action?orgType=BO&start_date=%{start_date}&end_Date=%{end_Date}&reportType=%{reportType}">Export As Excel</s:a>
</div> -->
<table width="100%" border="1" cellpadding="3" cellspacing="0"
	bordercolor="#CCCCCC" align="center">
	<s:set name="DG" value="%{#session.isDG}"></s:set>
	<s:set name="SE" value="%{#session.isSE}"></s:set>
	<s:set name="SLE" value="%{#session.isSLE}"></s:set>
	<s:set name="IW" value="%{#session.isIW}"></s:set>
	<s:set name="VS" value="%{#session.isVS}"></s:set>
	<s:set name="colCount" value="1" />
	<s:if test="%{#DG&&#SE}">
		<s:set name="colCount" value="2" />
	</s:if>
	<s:if test="%{#DG&&#SLE}">
		<s:set name="colCount" value="2" />
	</s:if>
	<s:if test="%{#DG&&#SE&&#SLE}">
		<s:set name="colCount" value="3" />
	</s:if>
	<s:if test="%{#DG&&#IW}">
		<s:set name="colCount" value="2" />
	</s:if>
	<s:if test="%{#DG&&#VS}">
		<s:set name="colCount" value="2" />
	</s:if>
	<s:if test="%{#DG&&#SLE&&#IW}">
		<s:set name="colCount" value="3" />
	</s:if>
	<s:if test="%{#DG&&#SLE&&#VS}">
		<s:set name="colCount" value="3" />
	</s:if>
	<s:if test="%{#DG&&#SE&&#SLE&&#IW}">
		<s:set name="colCount" value="4" />
	</s:if>
	<s:if test="%{#DG&&#SE&&#SLE&&#VS}">
		<s:set name="colCount" value="4" />
	</s:if>
	<s:if test="%{#session.result!=null}">
		<tr>
			<th align="center" colspan="<s:property value="#colCount"/>">
				<s:property value="#session.orgName" />
			</th>
			<th align="center">
				<s:text name="label.amt.in"/>
				<s:property value="#application.CURRENCY_SYMBOL" />
			</th>
		</tr>
		<tr>
			<td align="center" colspan="<s:property value="#colCount+2"/>">
				<s:property value="#session.orgAdd" />
			</td>
		</tr>
		<tr align="center">
			<th rowspan="3">
				<s:if test="%{reportType=='Agent Wise'}">
				<s:text name="label.party.name"/>
				</s:if>
				<s:elseif test="%{reportType=='Day Wise'}">
				<s:text name="label.date"/>
				</s:elseif>
			</th>
			<th colspan="<s:property value="#colCount"/>">
				<s:a
					href="bo_rep_DirPlyPwtReport_SearchExpand.action?start_date=%{start_date}&end_Date=%{end_Date}&reportType=%{reportType} Expand"
					theme="ajax" targets="result"><s:text name="label.srv.name"/>
				</s:a>

			</th>
		</tr>
		<tr align="center">
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

		<s:iterator id="result" value="#session.result">
			<s:set name="orgValue" value="%{value}"></s:set>
			<tr class="startSortable">
				<td>

					<s:if test="%{reportType=='Agent Wise'}">
						<s:property value="%{#orgValue.orgName}" />
					</s:if>
					<s:elseif test="%{reportType=='Day Wise'}">
						<s:a
							href="bo_rep_DirPlyPwtReport_RetalerWise.action?reportType=Agent Wise&dateType=SQL&agtId=0&start_date=%{#orgValue.orgName}&end_Date=%{#orgValue.orgName}&agtOrgName=%{#orgValue.orgName}"
							theme="ajax" targets="result">
							<s:property value="%{#orgValue.orgName}" />
						</s:a>
					</s:elseif>
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
						<%=FormatNumber.formatNumberForJSP(pageContext.getAttribute("iwPw"))%>
					</td>
				</s:if>
				<s:if test="%{#VS}">
					<td align="right">
						<s:set name="vsPw" value="%{#orgValue.vsDirPlyPwt}" />
						<%=FormatNumber.formatNumberForJSP(pageContext.getAttribute("vsPw"))%>
					</td>
				</s:if>
				<s:set name="dgPwt" value="%{#dgPwt+#orgValue.drawDirPlyPwt}" />
				<s:set name="sePwt" value="%{#sePwt+#orgValue.scratchDirPlyPwt}" />
				<s:set name="slePwt" value="%{#slePwt+#orgValue.sleDirPlyPwt}" />
				<s:set name="iwPwt" value="%{#iwPwt+#orgValue.iwDirPlyPwt}" />
				<s:set name="vsPwt" value="%{#vsPwt+#orgValue.vsDirPlyPwt}" />
			</tr>
		</s:iterator>
		<tr class="sortbottom">
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
$RCSfile: bo_rep_DirPlyPwtReport_AgentWise.jsp,v $
$Revision: 1.3 $
</code>