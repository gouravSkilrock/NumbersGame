<%@page import="java.util.Map"%>
<%@page import="java.util.Iterator"%>
<%@page import="com.skilrock.lms.beans.AgentWiseTktByTktSaleTxnBean"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.LinkedHashMap"%>
<%@page import="com.skilrock.lms.common.utility.FormatNumber"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<html>
		<head>
			<link rel="stylesheet"
					href="<%=request.getContextPath()%>/LMSImages/css/demo_table_jui.css"
					type="text/css">
				<link rel="stylesheet"
					href="<%=request.getContextPath()%>/LMSImages/css/datatable-common.css"
					type="text/css">
				<style type="text/css">
						#dTable_wrapper, #dTableNew_wrapper, #net_sale_dTable_wrapper {
   							 width: auto;
						}
				</style>
		</head>
		<body>
<div align="center">
	<h3>
		<u>Collection <s:text name="Report" /> Cashier Wise </u>
	</h3>
</div>
<s:set name="SE" value="%{#session.isSE}"></s:set>
<s:set name="SECount" value="0"></s:set>

	<div id="tableDataDiv">
<table  border="1" cellpadding="3" cellspacing="0"
	bordercolor="#CCCCCC" align="center"  id=dTableNew class="common_data_table display display_table">
	<s:if
		test="%{#session.resultService!=null && #session.resultService.size()>0}">
		<%
			Map<String, Map<String, Map<String, AgentWiseTktByTktSaleTxnBean>>> reportMap = (Map<String, Map<String, Map<String, AgentWiseTktByTktSaleTxnBean>>>) session
						.getAttribute("resultService");
				System.out.println("*reportMap***" + reportMap);
				Map<String, Map<String, AgentWiseTktByTktSaleTxnBean>> agentReportMap = new LinkedHashMap<String, Map<String, AgentWiseTktByTktSaleTxnBean>>();
				//System.out.println("*Drawmap***" + drawMap);
				Map<String, Map<String, AgentWiseTktByTktSaleTxnBean>> scratchMap = reportMap
						.get("SE");
				if (scratchMap != null) {

					Iterator<Map.Entry<String, Map<String, AgentWiseTktByTktSaleTxnBean>>> iterScratch = scratchMap
							.entrySet().iterator();
					while (iterScratch.hasNext()) {
						Map.Entry<String, Map<String, AgentWiseTktByTktSaleTxnBean>> scratchPair = iterScratch
								.next();
						String gameName = scratchPair.getKey();
						Map<String, AgentWiseTktByTktSaleTxnBean> value = scratchPair
								.getValue();
						Iterator<Map.Entry<String, AgentWiseTktByTktSaleTxnBean>> iterAgent = value
								.entrySet().iterator();
						while (iterAgent.hasNext()) {
							Map.Entry<String, AgentWiseTktByTktSaleTxnBean> agentPair = iterAgent
									.next();
						
								Map<String, AgentWiseTktByTktSaleTxnBean> newDrawWiseMap = new LinkedHashMap<String, AgentWiseTktByTktSaleTxnBean>();
								if(agentReportMap.containsKey(agentPair.getKey())){
									newDrawWiseMap = agentReportMap.get(agentPair.getKey());
								}
								newDrawWiseMap.put(gameName, agentPair
										.getValue());
								agentReportMap.put(agentPair.getKey(),
										newDrawWiseMap);
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
		<thead>
		<tr>
			<th align="center"
				colspan="<s:property value="%{( #SECount*3 )+1}"/>">
				<s:property value="#session.retailerName" />
			</th>
			<th align="center">
				Amount in
				<s:property value="#application.CURRENCY_SYMBOL" />
			</th>
		</tr>
	<%-- 	<tr>
			<td align="center"
				colspan="<s:property value="%{( #SECount*3 )+2}"/>">
				<s:property value="#session.orgAdd" />
			</td>
		</tr> --%>
		<tr align="center">
			<th rowspan="3">
				<s:if test="%{reportType=='Retailer Wise Expand'}">
				Party Name
				</s:if>
			</th>
			
			<s:if test="%{#SE}">
				<th colspan="<s:property value="%{#SECount*3}" />">
					Scratch Game
				</th>
			</s:if>
			
			<th rowspan="3">
				Net Amount
			</th>
		</tr>
		<tr>
			<s:iterator value="%{#session.resultService.SE}">
				<td colspan="3">
					<s:property value="%{key}" />
				</td>
			</s:iterator>
			
		</tr>
		<tr>
			<s:iterator value="%{#session.resultService.SE}">
				<th>
					Sale Amount
				</th>
				<th>
					<s:text name="PWT" />
					Amount
				</th>
				<th>
					Total
				</th>
			</s:iterator>
			
			
		</tr>
		</thead>
		<tbody>
		<%
			Iterator<Map.Entry<String, Map<String, AgentWiseTktByTktSaleTxnBean>>> iterOrg = agentReportMap
						.entrySet().iterator();
				while (iterOrg.hasNext()) {
					Map.Entry<String, Map<String, AgentWiseTktByTktSaleTxnBean>> orgPair = iterOrg
							.next();
		%><tr>
			<%
				double netAmt = 0.0;
			%>
			<td>
					<%=orgMap.get(orgPair.getKey())%>
				
			</td>
			<%
				Iterator<Map.Entry<String, AgentWiseTktByTktSaleTxnBean>> iterGame = orgPair
								.getValue().entrySet().iterator();
						while (iterGame.hasNext()) {
							AgentWiseTktByTktSaleTxnBean bean = iterGame.next()
									.getValue();
							
							
							/* System.out.println(orgMap.get(orgPair.getKey())
									+ bean.getScratchGameWiseWinning() + "*SDP*"
									+ bean.getScratchGameWiseSale() + "*SDP*"
									); */
			%>

			<td><%=FormatNumber.formatNumberForJSP(
								+ bean.getScratchGameWiseSale()
								)%></td>
			<td><%=FormatNumber.formatNumberForJSP(
								+ bean.getScratchGameWiseWinning()
								)%></td>
			<td><%=FormatNumber.formatNumberForJSP(
								+ bean.getScratchGameWiseSale()
								- bean.getScratchGameWiseWinning()
								)%></td>
			<%
				netAmt = netAmt +  bean.getScratchGameWiseSale()
									- bean.getScratchGameWiseWinning();
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
	</
</table>
</div>
</body>
</html>
<code id="headId" style="visibility: hidden">
	$RCSfile: agt_rep_tktByTktCompleteCollectionReport.jsp,v $
	$Revision: 1.3 $
</code>