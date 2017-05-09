
<%@page import="java.util.Map"%>
<%@page import="java.util.Iterator"%>
<%@page import="com.skilrock.lms.beans.CompleteCollectionBean"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.LinkedHashMap"%>
<%@page import="com.skilrock.lms.common.utility.FormatNumber"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<div align="center">
	<h3>
		<u>Collection <s:text name="Report"/> Game Wise </u>
	</h3>
</div>
<s:set name="DG" value="%{#session.isDG}"></s:set>
<s:set name="SE" value="%{#session.isSE}"></s:set>
<s:set name="DGCount" value="0"></s:set>
<s:set name="SECount" value="0"></s:set>
<table width="100%" border="1" cellpadding="3" cellspacing="0"
	bordercolor="#CCCCCC" align="center">
	<s:if
		test="%{#session.resultService!=null && #session.resultService.size()>0}">
		<%
			Map<String, Map<String, Map<String, CompleteCollectionBean>>> reportMap = (Map<String, Map<String, Map<String, CompleteCollectionBean>>>) session
						.getAttribute("resultService");
				System.out.println("*reportMap***" + reportMap);
				Map<String, Map<String, CompleteCollectionBean>> agentReportMap = new LinkedHashMap<String, Map<String, CompleteCollectionBean>>();
				Map<String, Map<String, CompleteCollectionBean>> drawMap = reportMap
						.get("DG");
				//System.out.println("*Drawmap***" + drawMap);
				boolean isDrawMap = false;
				boolean isAgentMapFilled = false;
				if (drawMap != null) {
					isDrawMap = true;
					Iterator<Map.Entry<String, Map<String, CompleteCollectionBean>>> iterDraw = drawMap
							.entrySet().iterator();
					while (iterDraw.hasNext()) {
						Map.Entry<String, Map<String, CompleteCollectionBean>> drawPair = iterDraw
								.next();
						String gameName = drawPair.getKey();
						Map<String, CompleteCollectionBean> value = drawPair
								.getValue();
						Iterator<Map.Entry<String, CompleteCollectionBean>> iterAgent = value
								.entrySet().iterator();
						while (iterAgent.hasNext()) {
							isAgentMapFilled = true;
							Map.Entry<String, CompleteCollectionBean> agentPair = iterAgent
									.next();
							if (agentReportMap.get(agentPair.getKey()) != null) {
								agentReportMap.get(agentPair.getKey()).put(
										gameName, agentPair.getValue());
							} else {

								Map<String, CompleteCollectionBean> newDrawWiseMap = new LinkedHashMap<String, CompleteCollectionBean>();
								newDrawWiseMap.put(gameName, agentPair
										.getValue());
								agentReportMap.put(agentPair.getKey(),
										newDrawWiseMap);
							}
						}
					}
		%>
		<s:set name="DGCount" value="%{#session.resultService.DG.size()}"></s:set>
		<%
			}
				Map<String, Map<String, CompleteCollectionBean>> scratchMap = reportMap
						.get("SE");
				if (scratchMap != null) {

					Iterator<Map.Entry<String, Map<String, CompleteCollectionBean>>> iterScratch = scratchMap
							.entrySet().iterator();
					while (iterScratch.hasNext()) {
						Map.Entry<String, Map<String, CompleteCollectionBean>> scratchPair = iterScratch
								.next();
						String gameName = scratchPair.getKey();
						Map<String, CompleteCollectionBean> value = scratchPair
								.getValue();
						Iterator<Map.Entry<String, CompleteCollectionBean>> iterAgent = value
								.entrySet().iterator();
						while (iterAgent.hasNext()) {
							Map.Entry<String, CompleteCollectionBean> agentPair = iterAgent
									.next();
							if (isDrawMap) {
								agentReportMap.get(agentPair.getKey()).put(
										gameName, agentPair.getValue());
							} else {
								Map<String, CompleteCollectionBean> newDrawWiseMap = new LinkedHashMap<String, CompleteCollectionBean>();
								newDrawWiseMap.put(gameName, agentPair
										.getValue());
								agentReportMap.put(agentPair.getKey(),
										newDrawWiseMap);

							}

						}
					}
		%>
		<s:set name="SECount" value="%{#session.resultService.SE.size()}"></s:set>
		<%
			}
				Map<String, String> orgMap = (Map<String, String>) session
						.getAttribute("orgMap");
				//System.out.println(agentReportMap);
		%>
		<tr>
			<th align="center" colspan="<s:property value="%{(#DGCount*2 + #SECount*2)+1}"/>" ><s:property value="#session.orgName" /></th>
			<th align="center">Amount in <s:property value="#application.CURRENCY_SYMBOL" /></th>
		</tr>
		<tr>
			<td align="center" colspan="<s:property value="%{(#DGCount*2 + #SECount*2)+2}"/>"><s:property value="#session.orgAdd" /></td>
		</tr>
		<tr align="center">
			<th rowspan="3">
				<s:if test="%{reportType=='Retailer Wise Expand'}">
				Party Name
				</s:if>
				<s:elseif test="%{reportType=='Day Wise Expand'}">
				Date
				</s:elseif>
			</th>
			<s:if test="%{#DG}">
				<th colspan="<s:property value="%{#DGCount*2}" />">
					Draw Game
				</th>
			</s:if>
			<s:if test="%{#SE}">
				<th colspan="<s:property value="%{#SECount*2}" />">
					Scratch Game
				</th>
			</s:if>
			<th rowspan="3">
				Net Amount
			</th>
		</tr>
		<tr>

			<s:iterator value="%{#session.resultService.DG}">
				<td colspan="2">
					<s:property value="%{key}" />
				</td>
			</s:iterator>
			<s:iterator value="%{#session.resultService.SE}">
				<td colspan="2">
					<s:property value="%{key}" />
				</td>
			</s:iterator>
		</tr>
		<tr>
			<s:iterator value="%{#session.resultService.DG}">
				<th>
					Sale Amount(MRP)
				</th>
				<th>
					<s:text name="PWT"/> Amount(MRP)
				</th>
			</s:iterator>
			<s:iterator value="%{#session.resultService.SE}">
				<th>
					Sale Amount(MRP)
				</th>
				<th>
					<s:text name="PWT"/> Amount(MRP)
				</th>
			</s:iterator>
		</tr>
		<%
			Iterator<Map.Entry<String, Map<String, CompleteCollectionBean>>> iterOrg = agentReportMap
						.entrySet().iterator();
				while (iterOrg.hasNext()) {
					Map.Entry<String, Map<String, CompleteCollectionBean>> orgPair = iterOrg
							.next();
		%><tr class="startSortable">
			<%
				double netAmt = 0.0;
			%>
			<td>
				<s:if test="%{reportType=='Retailer Wise Expand'}">
					<%=orgMap.get(orgPair.getKey())%>
				</s:if>
				<s:elseif test="%{reportType=='Day Wise Expand'}">
					<%=orgPair.getKey()%>
				</s:elseif>
			</td>
			<%
				Iterator<Map.Entry<String, CompleteCollectionBean>> iterGame = orgPair
								.getValue().entrySet().iterator();
						while (iterGame.hasNext()) {
							CompleteCollectionBean bean = iterGame.next()
									.getValue();
							/*System.out.println(orgMap.get(orgPair.getKey())
									+ "-----" + bean.getDrawSale() + "**"
									+ bean.getDrawCancel() + "**"
									+ bean.getScratchSale() + "**"
									+ bean.getDrawPwt() + "*DP*"
									+ bean.getDrawDirPlyPwt() + "**"
									+ bean.getScratchPwt() + "*SDP*"
									+ bean.getScratchDirPlyPwt());*/
			%>

			<td><%=FormatNumber.formatNumberForJSP(bean.getDrawSale()
								- bean.getDrawCancel() + bean.getScratchSale())%></td>
			<td><%=FormatNumber.formatNumberForJSP(bean.getDrawPwt()
								+ bean.getDrawDirPlyPwt()
								+ bean.getScratchPwt()
								+ bean.getScratchDirPlyPwt())%></td>
			<%
				netAmt = netAmt + bean.getDrawSale()
									- bean.getDrawCancel() + bean.getScratchSale()
									- bean.getDrawPwt() - bean.getDrawDirPlyPwt()
									+ bean.getScratchPwt()
									- bean.getScratchDirPlyPwt();
						}
			%>
			<td><%=FormatNumber.formatNumberForJSP(netAmt)%></td>
		</tr>
		<%
			}
		%>

	</s:if>
	<s:else>
		<tr>
			<th align="center">
				No Records Found
			</th>
		</tr>
	</s:else>
</table>

<code id="headId" style="visibility: hidden">
$RCSfile: agt_rep_completeCollectionReportMRP_SearchExpand.jsp,v $
$Revision: 1.2 $
</code>