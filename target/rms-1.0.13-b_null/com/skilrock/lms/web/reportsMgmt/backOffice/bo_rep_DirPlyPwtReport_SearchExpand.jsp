
<%@page import="java.util.Map"%>
<%@page import="java.util.Iterator"%>
<%@page import="com.skilrock.lms.beans.CompleteCollectionBean"%>
<%@page import="java.util.LinkedHashMap"%>
<%@page import="com.skilrock.lms.common.utility.FormatNumber"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<div align="center">
	<h3>
		<u><s:text name="Direct_Player"/> <s:text name="Report"/> <s:text name="label.gamewise"/> </u>
	</h3>
</div>
<s:set name="DG" value="%{#session.isDG}"></s:set>
<s:set name="SE" value="%{#session.isSE}"></s:set>
<s:set name="SLE" value="%{#session.isSLE}"></s:set>
<s:set name="IW" value="%{#session.isIW}"></s:set>
<s:set name="VS" value="%{#session.isVS}"></s:set>
<s:set name="DGCount" value="0"></s:set>
<s:set name="SECount" value="0"></s:set>
<s:set name="SLECount" value="0"></s:set>
<s:set name="IWCount" value="0"></s:set>
<s:set name="VSCount" value="0"></s:set>
<!--<div id="excel"  align="center" text-align: left;width: 750px;font-size: 9pt"><s:a href="bo_rep_CompltCollReport_ExpExcel.action?orgType=BO">Export As Excel</s:a></div>-->
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

					if (!drawMap.isEmpty()) {
		%>


		<s:set name="DGCount" value="%{#session.resultService.DG.size()}"></s:set>
		<%
			}
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
								if(agentReportMap.containsKey(agentPair.getKey())){
									newDrawWiseMap = agentReportMap.get(agentPair.getKey());
								}
								newDrawWiseMap.put(gameName, agentPair
										.getValue());
								agentReportMap.put(agentPair.getKey(),
										newDrawWiseMap);

							}

						}
					}
					if (!scratchMap.isEmpty()) {
		%>

		<s:set name="SECount" value="%{#session.resultService.SE.size()}"></s:set>
		<%
			}
				}
				Map<String, Map<String, CompleteCollectionBean>> sleMap = reportMap
						.get("SLE");
				boolean isSleMap = false;
				if (sleMap != null) {
					isSleMap = true;
					Iterator<Map.Entry<String, Map<String, CompleteCollectionBean>>> iterSle = sleMap
							.entrySet().iterator();
					while (iterSle.hasNext()) {
						Map.Entry<String, Map<String, CompleteCollectionBean>> slePair = iterSle.next();
						String gameName = slePair.getKey();
						Map<String, CompleteCollectionBean> value = slePair.getValue();
						Iterator<Map.Entry<String, CompleteCollectionBean>> iterAgent = value.entrySet().iterator();
						while (iterAgent.hasNext()) {
							Map.Entry<String, CompleteCollectionBean> agentPair = iterAgent
									.next();
							if (isSleMap) {
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
					if (!sleMap.isEmpty()) {
		%>

		<s:set name="SLECount" value="%{#session.resultService.SLE.size()}"></s:set>
		<%
			}
				}
				Map<String, Map<String, CompleteCollectionBean>> iwMap = reportMap
						.get("IW");
				boolean isIwMap = false;
				if (iwMap != null) {
					isIwMap = true;
					Iterator<Map.Entry<String, Map<String, CompleteCollectionBean>>> iterIw = iwMap
							.entrySet().iterator();
					while (iterIw.hasNext()) {
						Map.Entry<String, Map<String, CompleteCollectionBean>> iwPair = iterIw.next();
						String gameName = iwPair.getKey();
						Map<String, CompleteCollectionBean> value = iwPair.getValue();
						Iterator<Map.Entry<String, CompleteCollectionBean>> iterAgent = value.entrySet().iterator();
						while (iterAgent.hasNext()) {
							Map.Entry<String, CompleteCollectionBean> agentPair = iterAgent
									.next();
							if (isIwMap) {
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
					if (!iwMap.isEmpty()) {
		%>

		<s:set name="IWCount" value="%{#session.resultService.IW.size()}"></s:set>
		
		<%
			}
				}
				Map<String, Map<String, CompleteCollectionBean>> vsMap = reportMap
						.get("VS");
				boolean isVsMap = false;
				if (vsMap != null) {
					isVsMap = true;
					Iterator<Map.Entry<String, Map<String, CompleteCollectionBean>>> iterVs = vsMap
							.entrySet().iterator();
					while (iterVs.hasNext()) {
						Map.Entry<String, Map<String, CompleteCollectionBean>> vsPair = iterVs.next();
						String gameName = vsPair.getKey();
						Map<String, CompleteCollectionBean> value = vsPair.getValue();
						Iterator<Map.Entry<String, CompleteCollectionBean>> iterAgent = value.entrySet().iterator();
						while (iterAgent.hasNext()) {
							Map.Entry<String, CompleteCollectionBean> agentPair = iterAgent
									.next();
							if (isVsMap) {
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
					if (!vsMap.isEmpty()) {
		%>

		<s:set name="VSCount" value="%{#session.resultService.VS.size()}"></s:set>
		
		<%
			}
				}
				Map<String, String> orgMap = (Map<String, String>) session
						.getAttribute("orgMap");
				//System.out.println(agentReportMap);
		%>

		<tr>
			<th align="center" colspan="<s:property value="%{(#DGCount + #SECount + #SLECount + #IWCount + #VSCount)}"/>" ><s:property value="#session.orgName" /></th>
			<th align="center"><s:text name="label.amt.in"/> <s:property value="#application.CURRENCY_SYMBOL" /></th>
		</tr>
		<tr>
			<td align="center" colspan="<s:property value="%{(#DGCount + #SECount + #SLECount + #IWCount + #VSCount)+1}"/>"><s:property value="#session.orgAdd" /></td>
		</tr>
		<tr align="center">
			<th rowspan="3">
				<s:if test="%{reportType=='Agent Wise Expand'}">
				<s:text name="label.party.name"/>
				</s:if>
				<s:elseif test="%{reportType=='Day Wise Expand'}">
				<s:text name="label.date"/>
				</s:elseif>
			</th>
			<s:if test="%{#DG}">
				<th colspan="<s:property value="%{#DGCount}" />">
					<s:text name="label.draw.game"/>
				</th>
			</s:if>
			<s:if test="%{#SE}">
				<th colspan="<s:property value="%{#SECount}" />">
					<s:text name="label.scratch.game"/>
				</th>
			</s:if>
			<s:if test="%{#SLE}">
				<th colspan="<s:property value="%{#SLECount}" />">
					<s:text name="label.sport.lot"/>
				</th>
			</s:if>
			<s:if test="%{#IW}">
				<th colspan="<s:property value="%{#IWCount}" />">
					<s:text name="label.instant.win"/>
				</th>
			</s:if>
			<s:if test="%{#VS}">
				<s:if test="%{#VSCount >0}">
					<th colspan="<s:property value="%{#VSCount}" />">
						<s:text name="label.virtual.sport"/>
				</th>
				</s:if>
			</s:if>
		</tr>
		<tr>

			<s:iterator value="%{#session.resultService.DG}">
				<td align="right">
					<s:property value="%{key}" />
				</td>
			</s:iterator>
			<s:iterator value="%{#session.resultService.SE}">
				<td align="right">
					<s:property value="%{key}" />
				</td>
			</s:iterator>
			<s:iterator value="%{#session.resultService.SLE}">
				<td align="right">
					<s:property value="%{key}" />
				</td>
			</s:iterator>
			<s:iterator value="%{#session.resultService.IW}">
				<td align="right">
					<s:property value="%{key}" />
				</td>
			</s:iterator>
			<s:iterator value="%{#session.resultService.VS}">
				<td align="right">
					<s:property value="%{key}" />
				</td>
			</s:iterator>
		</tr>
		<tr>
			<s:iterator value="%{#session.resultService.DG}">
				<th>
					<s:text name="PWT"/> <s:text name="label.amount"/>
				</th>
			</s:iterator>
			<s:iterator value="%{#session.resultService.SE}">
				<th>
					<s:text name="PWT"/> <s:text name="label.amount"/>
				</th>
			</s:iterator>
			<s:iterator value="%{#session.resultService.SLE}">
				<th>
					<s:text name="PWT"/> <s:text name="label.amount"/>
				</th>
			</s:iterator>
			<s:iterator value="%{#session.resultService.IW}">
				<th>
					<s:text name="PWT"/> <s:text name="label.amount"/>
				</th>
			</s:iterator>
			<s:iterator value="%{#session.resultService.VS}">
				<th>
					<s:text name="PWT"/> <s:text name="label.amount"/>
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
			<td align="right">
				<s:if test="%{reportType=='Agent Wise Expand'}">
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

			<td align="right"><%=FormatNumber.formatNumberForJSP(bean.getDrawDirPlyPwt()+ bean.getScratchDirPlyPwt()+bean.getSleDirPlyPwt()+bean.getIwDirPlyPwt()+bean.getVsDirPlyPwt())%></td>
			<%
				netAmt = netAmt + bean.getDrawSale()
									- bean.getDrawCancel() + bean.getScratchSale()
									- bean.getDrawPwt() - bean.getDrawDirPlyPwt()
									+ bean.getScratchPwt()
									- bean.getScratchDirPlyPwt()
									+ bean.getSleSale() - bean.getSleCancel()
									- bean.getSlePwt() - bean.getSleDirPlyPwt()
									+ bean.getIwSale() - bean.getIwCancel()
									- bean.getIwPwt() - bean.getIwDirPlyPwt()
									+ bean.getVsSale() - bean.getVsCancel()
									- bean.getVsPwt() - bean.getVsDirPlyPwt();
						}
			%>
		</tr>
		<%
			}
		%>

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
$RCSfile: bo_rep_DirPlyPwtReport_SearchExpand.jsp,v $
$Revision: 1.3 $
</code>
