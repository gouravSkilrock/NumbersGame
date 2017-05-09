<%@page import="java.util.Map"%>
<%@page import="java.util.Iterator"%>
<%@page import="com.skilrock.lms.beans.CompleteCollectionBean"%>
<%@page import="java.util.LinkedHashMap"%>
<%@page import="com.skilrock.lms.common.utility.FormatNumber"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<div align="center">
	<h3>
		<u><s:text name="SALE"/> <s:text name="PWT"/> <s:text name="Collection"/> <s:text name="Report"/> <s:text name="label.gamewise"/> </u>
	</h3>
</div>
<s:set name="DG" value="%{#session.isDG}"></s:set>
<s:set name="SE" value="%{#session.isSE}"></s:set>
<s:set name="CS" value="%{#session.isCS}"></s:set>
<s:set name="OLA" value="%{#session.isOLA}"></s:set>
<s:set name="SLE" value="%{#session.isSLE}"></s:set>
<s:set name="IW" value="%{#session.isIW}"></s:set>
<s:set name="VS" value="%{#session.isVS}"></s:set>
<s:set name="DGCount" value="0"></s:set>
<s:set name="SECount" value="0"></s:set>
<s:set name="CSCount" value="0"></s:set>
<s:set name="OLACount" value="0"></s:set>
<s:set name="SLECount" value="0"></s:set>
<s:set name="IWCount" value="0"></s:set>
<s:set name="VSCount" value="0"></s:set>
<!--<div id="excel"  align="center" text-align: left;width: 750px;font-size: 9pt"><s:a href="bo_rep_CompltCollReport_ExpExcel.action?orgType=BO">Export As Excel</s:a></div>-->
<table width="100%" border="1" cellpadding="3" cellspacing="0"
	bordercolor="#CCCCCC" align="center" style="font-size: 12px">
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
				boolean isCSMap = false;
				boolean isOLAMap = false;
				boolean isSLEMap = false;
				boolean isIWMap = false;
				boolean isVSMap = false;
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
				Map<String, Map<String, CompleteCollectionBean>> CSMap = reportMap
						.get("CS");
				if (CSMap != null) {
					isCSMap = true;
					Iterator<Map.Entry<String, Map<String, CompleteCollectionBean>>> iterCS = CSMap
							.entrySet().iterator();
					while (iterCS.hasNext()) {
						Map.Entry<String, Map<String, CompleteCollectionBean>> CSPair = iterCS
								.next();
						String catName = CSPair.getKey();
						Map<String, CompleteCollectionBean> value = CSPair
								.getValue();
						Iterator<Map.Entry<String, CompleteCollectionBean>> iterAgent = value
								.entrySet().iterator();
						while (iterAgent.hasNext()) {
							isAgentMapFilled = true;
							Map.Entry<String, CompleteCollectionBean> agentPair = iterAgent
									.next();
							if (agentReportMap.get(agentPair.getKey()) != null) {
								agentReportMap.get(agentPair.getKey()).put(
										catName, agentPair.getValue());
							} else {
								Map<String, CompleteCollectionBean> newDrawWiseMap = new LinkedHashMap<String, CompleteCollectionBean>();
								newDrawWiseMap.put(catName, agentPair
										.getValue());
								agentReportMap.put(agentPair.getKey(),
										newDrawWiseMap);
							}
						}
					}

					if (!CSMap.isEmpty()) {
		%>


		<s:set name="CSCount" value="%{#session.resultService.CS.size()}"></s:set>
		<%
			}
				}
				Map<String, Map<String, CompleteCollectionBean>> OLAMap = reportMap
						.get("OLA");
				if (OLAMap != null) {
					isOLAMap = true;
					Iterator<Map.Entry<String, Map<String, CompleteCollectionBean>>> iterOLA = OLAMap
							.entrySet().iterator();
					while (iterOLA.hasNext()) {
						Map.Entry<String, Map<String, CompleteCollectionBean>> OLAPair = iterOLA
								.next();
						String walletName = OLAPair.getKey();
						Map<String, CompleteCollectionBean> value = OLAPair
								.getValue();
						Iterator<Map.Entry<String, CompleteCollectionBean>> iterAgent = value
								.entrySet().iterator();
						while (iterAgent.hasNext()) {
							isAgentMapFilled = true;
							Map.Entry<String, CompleteCollectionBean> agentPair = iterAgent
									.next();
							if (agentReportMap.get(agentPair.getKey()) != null) {
								agentReportMap.get(agentPair.getKey()).put(
										walletName, agentPair.getValue());
							} else {
								Map<String, CompleteCollectionBean> newWalletWiseMap = new LinkedHashMap<String, CompleteCollectionBean>();
								newWalletWiseMap.put(walletName, agentPair
										.getValue());
								agentReportMap.put(agentPair.getKey(),
										newWalletWiseMap);
							}
						}
					}

					if (!OLAMap.isEmpty()) {
		%>


		<s:set name="OLACount" value="%{#session.resultService.OLA.size()}"></s:set>
		<%
			}
				}
				//For SLE******************************

				Map<String, Map<String, CompleteCollectionBean>> sleMap = reportMap
						.get("SLE");
				if (sleMap != null) {
					isSLEMap = true;
					Iterator<Map.Entry<String, Map<String, CompleteCollectionBean>>> iterSle = sleMap
							.entrySet().iterator();
					while (iterSle.hasNext()) {
						Map.Entry<String, Map<String, CompleteCollectionBean>> slePair = iterSle
								.next();
						String gameName = slePair.getKey();
						Map<String, CompleteCollectionBean> value = slePair
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

					if (!sleMap.isEmpty()) {
		%>


		<s:set name="SLECount" value="%{#session.resultService.SLE.size()}"></s:set>
		<%
			}
				}

				Map<String, Map<String, CompleteCollectionBean>> iwMap = reportMap
						.get("IW");
				if (iwMap != null) {
					isIWMap = true;
					Iterator<Map.Entry<String, Map<String, CompleteCollectionBean>>> iterIw = iwMap
							.entrySet().iterator();
					while (iterIw.hasNext()) {
						Map.Entry<String, Map<String, CompleteCollectionBean>> iwPair = iterIw
								.next();
						String gameName = iwPair.getKey();
						Map<String, CompleteCollectionBean> value = iwPair
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

					if (!iwMap.isEmpty()) {
		%>


		<s:set name="IWCount" value="%{#session.resultService.IW.size()}"></s:set>
		<%
			}
				}

				Map<String, Map<String, CompleteCollectionBean>> vsMap = reportMap
						.get("VS");
				if (vsMap != null) {
					isVSMap = true;
					Iterator<Map.Entry<String, Map<String, CompleteCollectionBean>>> iterVs = vsMap
							.entrySet().iterator();
					while (iterVs.hasNext()) {
						Map.Entry<String, Map<String, CompleteCollectionBean>> vsPair = iterVs
								.next();
						String gameName = vsPair.getKey();
						Map<String, CompleteCollectionBean> value = vsPair
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
			<th align="center"
				colspan="<s:property value="%{(#DGCount*2 + #SECount*2 + #CSCount*1 + #OLACount*2 + #SLECount*2 + #IWCount*2 + #VSCount*2)+1}"/>">
				<s:property value="#session.orgName" />
			</th>
			<th align="center">
				Amount in
				<s:property value="#application.CURRENCY_SYMBOL" />
			</th>
		</tr>
		<tr>
			<td align="center"
				colspan="<s:property value="%{(#DGCount*2 + #SECount*2 + #CSCount*1 + #OLACount*2 + #SLECount*2 + #IWCount*2 + #VSCount*2)+2}"/>">
				<s:property value="#session.orgAdd" />
			</td>
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
				<th colspan="<s:property value="%{#DGCount*2}" />">
					<s:text name="label.draw.game"/>
				</th>
			</s:if>
			<s:if test="%{#SE}">
				<th colspan="<s:property value="%{#SECount*2}" />">
					<s:text name="label.scratch.game"/>
				</th>
			</s:if>
			<s:if test="%{#CS}">
				<th colspan="<s:property value="%{#CSCount*1}" />">
					<s:text name="label.comm.serv"/>
				</th>
			</s:if>
			<s:if test="%{#OLA}">
				<th colspan="<s:property value="%{#OLACount*2}" />">
					<s:text name="label.offline.aff"/>
				</th>
			</s:if>
			<s:if test="%{#SLE}">
				<th colspan="<s:property value="%{#SLECount*2}" />">
					Sports Lottery
				</th>
			</s:if>
			<s:if test="%{#IW}">
				<th colspan="<s:property value="%{#IWCount*2}" />">
					Instant Win
				</th>
			</s:if>
			<s:if test="%{#VS}">
				<s:if test="%{#VSCount > 0}">
					<th colspan="<s:property value="%{#VSCount*2}" />">
						Virtual Sports
					</th>
				</s:if>
			</s:if>
			<th rowspan="3" nowrap="nowrap">
				<s:text name="label.net.amt"/>
			</th>
		</tr>
		<tr>

			<s:iterator value="%{#session.resultService.DG}">
				<td colspan="2" align="right">
					<s:property value="%{key}" />
				</td>
			</s:iterator>
			<s:iterator value="%{#session.resultService.SE}">
				<td colspan="2" align="right">
					<s:property value="%{key}" />
				</td>
			</s:iterator>
			<s:iterator value="%{#session.resultService.CS}">
				<td colspan="1" align="right">
					<s:property value="%{key}" />
				</td>
			</s:iterator>
			<s:iterator value="%{#session.resultService.OLA}">
				<td colspan="2" align="right">
					<s:property value="%{key}" />
				</td>
			</s:iterator>
			<s:iterator value="%{#session.resultService.SLE}">
				<td colspan="2" align="right">
					<s:property value="%{key}" />
				</td>
			</s:iterator>
			<s:iterator value="%{#session.resultService.IW}">
				<td colspan="2" align="right">
					<s:property value="%{key}" />
				</td>
			</s:iterator>
			<s:iterator value="%{#session.resultService.VS}">
				<td colspan="2" align="right">
					<s:property value="%{key}" />
				</td>
			</s:iterator>
		</tr>
		<tr>
			<s:iterator value="%{#session.resultService.DG}">
				<th nowrap="nowrap">
					<s:text name="label.sale.amt"/>
				</th>
				<th nowrap="nowrap">
					<s:text name="PWT"/> <s:text name="label.amount"/>
				</th>
			</s:iterator>
			<s:iterator value="%{#session.resultService.SE}">
				<th nowrap="nowrap">
					<s:text name="label.sale.amt"/>
				</th>
				<th nowrap="nowrap">
					<s:text name="PWT"/> <s:text name="label.amount"/>
				</th>
			</s:iterator>
			<s:iterator value="%{#session.resultService.CS}">
				<th nowrap="nowrap">
					<s:text name="label.sale.amt"/>
				</th>
			</s:iterator>
			<s:iterator value="%{#session.resultService.OLA}">
				<th nowrap="nowrap">
					<s:text name="label.depo.amt"/>
				</th>
				<th nowrap="nowrap">
					<s:text name="label.wdl.amt"/>
				</th>
				<!--<th nowrap="nowrap">
					Net Gaming Amount
				</th>
			-->
			</s:iterator>
			<s:iterator value="%{#session.resultService.SLE}">
				<th nowrap="nowrap">
					Sale Amount
				</th>
				<th nowrap="nowrap">
					<s:text name="PWT" />
					Amount
				</th>
			</s:iterator>
			<s:iterator value="%{#session.resultService.IW}">
				<th nowrap="nowrap">
					Sale Amount
				</th>
				<th nowrap="nowrap">
					<s:text name="PWT" />
					Amount
				</th>
			</s:iterator>
			<s:iterator value="%{#session.resultService.VS}">
				<th nowrap="nowrap">
					Sale Amount
				</th>
				<th nowrap="nowrap">
					<s:text name="PWT" />
					Amount
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
			<td nowrap="nowrap" align="right">
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
							Map.Entry<String, CompleteCollectionBean> agtCSPair = iterGame
									.next();
							CompleteCollectionBean bean = agtCSPair.getValue();
							/*System.out.println(orgMap.get(orgPair.getKey())
									+ "-----" + bean.getDrawSale() + "**"
									+ bean.getDrawCancel() + "**"
									+ bean.getScratchSale() + "**"
									+ bean.getDrawPwt() + "*DP*"
									+ bean.getDrawDirPlyPwt() + "**"
									+ bean.getScratchPwt() + "*SDP*"
									+ bean.getScratchDirPlyPwt());*/
			%>

			<td nowrap="nowrap" align="right"><%=FormatNumber.formatNumberForJSP(bean
								.getDrawSale()
								- bean.getDrawCancel()
								+ bean.getScratchSale()
								+ bean.getCSSale()
								- bean.getCSCancel()
								+ bean.getOlaDepositAmt()
								- bean.getOlaDepositCancelAmt()
								+ bean.getSleSale() - bean.getSleCancel()
								+ bean.getIwSale() - bean.getIwCancel()
								+ bean.getVsSale() - bean.getVsCancel())%></td>
			<%
				if ((agtCSPair.getKey().indexOf("Mobile") == -1)
									&& (agtCSPair.getKey().indexOf("ETU") == -1)
									&& (agtCSPair.getKey().indexOf("Pin") == -1)) {
			%>
			<td nowrap="nowrap" align="right"><%=FormatNumber
									.formatNumberForJSP(bean.getDrawPwt()
											+ bean.getDrawDirPlyPwt()
											+ bean.getScratchPwt()
											+ bean.getScratchDirPlyPwt()
											+ bean.getOlaWithdrawalAmt()
											+ bean.getSlePwt()
											+ bean.getSleDirPlyPwt()
											+ bean.getIwPwt()
											+ bean.getIwDirPlyPwt()
											+ bean.getVsPwt()
											+ bean.getVsDirPlyPwt())%></td>
			<%
				}
			%>
			<!--<s:if test="%{#OLA}">
				<td nowrap="nowrap" align="right"><%=FormatNumber.formatNumberForJSP(bean
									.getOlaNetGaming())%></td>
			</s:if>
			-->
			<%
				netAmt = netAmt + bean.getDrawSale()
									- bean.getDrawCancel() + bean.getScratchSale()
									- bean.getDrawPwt() - bean.getDrawDirPlyPwt()
									+ bean.getScratchPwt()
									- bean.getScratchDirPlyPwt() + bean.getCSSale()
									- bean.getCSCancel() + bean.getOlaDepositAmt()
									- bean.getOlaDepositCancelAmt()
									- bean.getOlaWithdrawalAmt()
									- bean.getOlaNetGaming()
									+ bean.getSleSale() - bean.getSleCancel() - bean.getSlePwt() - bean.getSleDirPlyPwt()
									+ bean.getIwSale() - bean.getIwCancel() - bean.getIwPwt() - bean.getIwDirPlyPwt()
									+ bean.getVsSale() - bean.getVsCancel() - bean.getVsPwt() - bean.getVsDirPlyPwt();
						}
			%>
			<td nowrap="nowrap" align="right"><%=FormatNumber.formatNumberForJSP(netAmt)%></td>
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
	$RCSfile: bo_rep_completeCollectionReport_SearchExpand.jsp,v $
	$Revision: 1.3 $
</code>

