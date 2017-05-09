
<%@page import="com.skilrock.lms.beans.DateBeans"%>
<%@page import="com.skilrock.lms.common.utility.GetDate"%>
<%@page import="com.skilrock.lms.common.utility.FormatNumber"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html>
	<%
		response.setHeader("Cache-Control", "no-store"); //HTTP 1.1
		response.setHeader("Pragma", "no-cache"); //HTTP 1.0
		response.setDateHeader("Expires", 0); //prevents caching at the proxy server
	%>
	<s:head theme="ajax" />

	<body onload="disable()">

		<br />

		<div align="center">
			<h3>
				<u><s:text name="label.draw.game"/> <s:text name="PWT"/> <s:text name="Report"/> &nbsp; <%
 	if ("".equals(((DateBeans) session.getAttribute("datebean"))
 			.getReportType())) {
 %>
					From : <%=GetDate.getConvertedDate(((DateBeans) session
								.getAttribute("datebean")).getStartDate())%>
					&nbsp;&nbsp;To&nbsp;:&nbsp; <%=GetDate.getConvertedDate(((DateBeans) session
								.getAttribute("datebean")).getEndDate())%>

					<%
						} else {
							out.println("Of : "
									+ ((DateBeans) session.getAttribute("datebean"))
											.getReportType());
						}
					%>
				</u>
			</h3>
		</div>
<div id="excel"  text-align: left;width: 750px;font-size: 9pt"><s:a href="bo_rep_DGPwtReport_ExpExcel.action?orgType=BO"><s:text name="btn.exportasexcel"/></s:a></div>
		<span style="position:relative;left:250px;"><s:text name="label.amt.in"/> <s:property value="#application.CURRENCY_SYMBOL" /></span>
		<table width="70%" border="1" cellpadding="3" cellspacing="0"
			bordercolor="#CCCCCC" align="center">
			<tr>
				<th align="center" colspan="2" ><s:property value="#session.orgName" /></th>
			</tr>
			<tr>
				<td align="center" colspan="2"><s:property value="#session.orgAdd" /></td>
			</tr>
			<tr>
				<s:if test="%{filter=='Game Wise'}">
					<th width="20%" nowrap="nowrap">
						<s:text name="label.game.name"/>
					</th>
				</s:if>
				<s:else>
					<s:if test="%{filter=='Agent Wise'}">
						<th width="20%" nowrap="nowrap">
							<s:property value="#application.TIER_MAP.AGENT" /> <s:text name="label.name"/>
						</th>
					</s:if>
					<s:else>
						<th width="20%" nowrap="nowrap">
							<s:property value="#application.TIER_MAP.RETAILER" /><s:text name="label.name"/>
						</th>
						</s:else>
					
				</s:else>
				<th width="80%">
					<s:text name="label.total.pwt"/>
				</th>
			</tr>
			<s:if test="#session.searchResultRet.size>0">
				<s:iterator id="beanCart" value="#session.searchResultRet">
				<s:set name="pwtBean" value="PwtReportBean"/>
					<tr class="startSortable">
						<s:if test="%{(gameName!=null && gameName != 'Total')||(agentName!=null && agentName != 'Total')||(retName!=null && retName != 'Total')}">
							<td width="12%" align="left" nowrap="nowrap">
								<s:property value="%{gameName}" />
								<s:property value="%{agentName}" />
								<s:property value="%{retName}" />
							</td>
							<td width="10%" align="right">&nbsp;
								<s:set name="clmMrpJsp" value='%{clmMrp}' />
								<a href='#' onclick="fetchData('filter=<s:property value="%{filter}"/>&agtOrgName=<s:property value="%{agentName}"/><s:property value="%{retName}"/>')"><%=FormatNumber.formatNumberForJSP(pageContext
										.getAttribute("clmMrpJsp"))%></a>
							</td>
						</s:if>
						<s:else>
							<s:set name="totalPwtJsp" value="%{clmMrp}"/>
					</s:else>
					</tr>
				</s:iterator>
				
					<th width="12%" align="center" nowrap="nowrap"><s:text name="label.total"/></th>				
					<th width="10%" align="right"><%=FormatNumber.formatNumberForJSP(pageContext.getAttribute("totalPwtJsp"))%></th>
			</s:if>
			<s:else>
				<tr>
					<td colspan="9" align="center">
						<s:text name="msg.no.record"/>
					</td>
				</tr>
			</s:else>

		</table>
		<br/><br/>
		<div id="dirPlrTable">
			<s:if test="%{filter == 'Game Wise'}">
				<div align="center">
					<h3>
						<u><s:text name="Report"/> <s:text name="label.for"/> <s:text name="PWT"/> <s:text name="label.by.plr"/> &nbsp; <%
 	if ("".equals(((DateBeans) session.getAttribute("datebean"))
 				.getReportType())) {
 %>
							From : <%=GetDate.getConvertedDate(((DateBeans) session
									.getAttribute("datebean")).getStartDate())%>
							&nbsp;&nbsp;To&nbsp;:&nbsp; <%=GetDate.getConvertedDate(((DateBeans) session
									.getAttribute("datebean")).getEndDate())%>

							<%
								} else {
										out.println("Of : "
												+ ((DateBeans) session.getAttribute("datebean"))
														.getReportType());
									}
							%>
						</u>
					</h3>
				</div><span style="position:relative;left:250px;"><s:text name="label.amt.in"/> <s:property value="#application.CURRENCY_SYMBOL" /></span>
				<table width="70%" border="1" cellpadding="3" cellspacing="0"
					bordercolor="#CCCCCC" align="center">
					<tr>
						<th align="center" colspan="2"><s:property value="#session.orgName" /></th>
					</tr>
					<tr>
						<td align="center" colspan="2"><s:property value="#session.orgAdd" /></td>
					</tr>
					<tr>
						<s:if test="%{filter=='Game Wise'}">
							<th width="20%" nowrap="nowrap">
								<s:text name="label.game.name"/>
							</th>
						</s:if>
						<th width="80%">
							<s:text name="label.total"/> <s:text name="PWT"/>
						</th>
					</tr>
					<s:if test="#session.dgDirPlrPwtRepList.size>0">
						<s:iterator id="beanCart" value="#session.dgDirPlrPwtRepList">
							<s:if test="%{(gameName!=null && gameName != 'Total')||(agentName!=null && agentName != 'Total')||(retName!=null && retName != 'Total')}">
								<tr class="startSortable">
									<td width="12%" align="left" nowrap="nowrap">
										<s:property value="%{gameName}" />
										<s:property value="%{retName}" />
									</td>
									<td width="10%" align="right">
										&nbsp;
										<s:set name="clmMrpJspa" value='%{pwtAmtClm}' /><%=FormatNumber.formatNumberForJSP(pageContext
											.getAttribute("clmMrpJspa"))%></td>
								</tr>
							</s:if>
							<s:else>
								<s:set name="totalDPPwtJsp" value="%{pwtAmtClm}"/>
							</s:else>
						</s:iterator>
							<th width="12%" align="center" nowrap="nowrap">Total</th>				
							<th width="10%" align="right"><%=FormatNumber.formatNumberForJSP(pageContext.getAttribute("totalDPPwtJsp"))%></th>
					</s:if>
					<s:else>
						<tr>
							<td colspan="9" align="center">
								<s:text name="msg.no.record"/>
							</td>
						</tr>
					</s:else>
				</table>
			</s:if>
		</div>
	</body>
</html>

<code id="headId" style="visibility: hidden">
$RCSfile: bo_dg_rep_pwt_SearchResult.jsp,v $
$Revision: 1.3 $
</code>