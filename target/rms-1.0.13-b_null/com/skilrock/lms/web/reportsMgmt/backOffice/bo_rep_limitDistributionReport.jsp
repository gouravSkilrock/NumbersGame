<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="com.skilrock.lms.common.utility.FormatNumber"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<%
	response.setHeader("Cache-Control", "no-store"); //HTTP 1.1
	response.setHeader("Pragma", "no-cache"); //HTTP 1.0
	response.setDateHeader("Expires", 0); //prevents caching at the proxy server
%>
<html>
	<head>
		<s:head theme="ajax" debug="false" />
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<link rel="stylesheet"
			href="<%=request.getContextPath()%>/LMSImages/css/styles.css"
			type="text/css" />
		<title><%=application.getAttribute("JSP_PAGE_TITLE")%></title>
		<link type="text/css" rel="stylesheet"
			href="<%=request.getContextPath()%>/LMSImages/css/lmsCalendar.css"
			media="screen" />
		<script type="text/javascript">
	function updateBalLimitClr() {
		//alert(_id.o('agtCount').value);
		var cnt = parseInt(_id.o('agtCount').value);
		for (i = 1; i <= cnt; i++) {
			var x = parseFloat(_id.o('liveLimit_' + i).innerHTML);
			var y = parseFloat(_id.o('balLimit_' + i).innerHTML);
			if (y < (x * 0.05)) {
				// _id.o('balLimit_'+i).style.background="red";
				_id.o('balLimit_' + i).style.color = "red";
			} else if (y < (x * 0.2)) {
				//_id.o('balLimit_'+i).style.background="#ffa825";
				_id.o('balLimit_' + i).style.color = "#ffa825";
			} else {
				//_id.o('balLimit_'+i).style.background="green";
				_id.o('balLimit_' + i).style.color = "green";
			}
		}
	}
</script>
	</head>
	<body onload="updateBalLimitClr()">
		<%@include file="/com/skilrock/lms/web/loginMgmt/menu.jsp"%>
		<s:hidden id="agtCount" name="agtCount"
			value="%{#session.BOLimitDistribList.size}" />
		<s:if test="#session.BOLimitDistribList.size>0">
			<div id="wrap">
				<div id="top_form">
					<h3>
						<s:text name="AGENT" />
					</h3>
					<table border="1" cellpadding="2" cellspacing="0"
						bordercolor="#CCCCCC" align="center">
						<tr>
							<th align="center">
								<s:text name="label.sNo" />
							</th>
							<th align="center">
								<s:property value="#application.TIER_MAP.AGENT" />
								<s:text name="label.name" />
							</th>
							<th align="center" nowrap="nowrap">
								<s:text name="label.crdtlmt" />
							</th>
							<th align="center" nowrap="nowrap">
								<s:text name="label.ext.credit.limit" />
							</th>
							<th align="center" nowrap="nowrap">
								<s:text name="label.rem.day" />
							</th>
							<th align="center" nowrap="nowrap">
								<s:text name="label.live.limit" />
							</th>
							<th align="center" nowrap="nowrap">
								<s:text name="label.dist.limit" />
							</th>
							<th align="center" nowrap="nowrap">
								<s:text name="label.distributable.limit" />
							</th>
							<th align="center" nowrap="nowrap">
								<s:text name="label.no.of.terminal" />
							</th>
						</tr>
						<s:set name="creditSumTot" value="0.0" />
						<s:set name="exCreditSumTot" value="0.0" />
						<s:set name="liveSumTot" value="0.0" />
						<s:set name="distributedSumTot" value="0.0" />
						<s:set name="distributableSumTot" value="0.0" />
						<s:set name="totalTerminal" value="0" />
						<s:iterator value="#session.BOLimitDistribList" status="taskIndex">
							<s:set name="agentBean" value="value"></s:set>
							<tr class="startSortable">
								<td>
									<s:property value="#taskIndex.index+1" />
								</td>
								<s:set name="creditSum" value="%{#agentBean.crLimit}" />
								<s:set name="exCreditSum" value="%{#agentBean.xcrLimit}" />
								<s:set name="liveSum" value="%{#agentBean.liveBal}" />
								<s:set name="distributedSum"
									value="%{#agentBean.distributedBal}" />
								<s:set name="distributableSum"
									value="%{#agentBean.distributableBal}" />
								<td align="left">
									<s:a theme="ajax"
										href="bo_um_agtBalDistribution_Search.action?userOrgId=%{#agentBean.orgId}"
										targets="agentLimitDiv">
										<s:property value="%{#agentBean.name}" />
									</s:a>
								</td>
								<td align="right"><%=FormatNumber.formatNumberForJSP(pageContext
							.getAttribute("creditSum"))%></td>
								<td align="right"><%=FormatNumber.formatNumberForJSP(pageContext
							.getAttribute("exCreditSum"))%></td>
								<td align="right">
									<s:property value="%{#agentBean.xcrLimitUpto}" />
								</td>
								<td align="right"
									id="liveLimit_<s:property value="#taskIndex.index+1" />"><%=FormatNumber.formatNumberForJSP(pageContext
							.getAttribute("liveSum"))%></td>
								<td align="right"><%=FormatNumber.formatNumberForJSP(pageContext
							.getAttribute("distributedSum"))%></td>
								<td align="right"
									id="balLimit_<s:property value="#taskIndex.index+1" />"><%=FormatNumber.formatNumberForJSP(pageContext
							.getAttribute("distributableSum"))%></td>
								<td align="right">
									<s:property value="%{#agentBean.terminalCount}" />
									<s:set name="terminalCount" value="%{#agentBean.terminalCount}" />
								</td>
							</tr>
							<s:set name="creditSumTot" value="%{#creditSumTot+#creditSum}" />
							<s:set name="exCreditSumTot"
								value="%{#exCreditSumTot+#exCreditSum}" />
							<s:set name="liveSumTot" value="%{#liveSumTot+#liveSum}" />
							<s:set name="distributedSumTot"
								value="%{#distributedSumTot+#distributedSum}" />
							<s:set name="distributableSumTot"
								value="%{#distributableSumTot+#distributableSum}" />
							<s:set name="totalTerminal"
								value="%{#totalTerminal+#terminalCount}" />
						</s:iterator>
						<tr>
							<td align="center" colspan="2">
								Total
							</td>
							<td align="right"><%=FormatNumber.formatNumberForJSP(pageContext
						.getAttribute("creditSumTot"))%></td>
							<td align="right"><%=FormatNumber.formatNumberForJSP(pageContext
						.getAttribute("exCreditSumTot"))%></td>
							<td align="right">
								&nbsp;
							</td>
							<td align="right"><%=FormatNumber.formatNumberForJSP(pageContext
						.getAttribute("liveSumTot"))%></td>
							<td align="right"><%=FormatNumber.formatNumberForJSP(pageContext
						.getAttribute("distributedSumTot"))%></td>
							<td align="right"><%=FormatNumber.formatNumberForJSP(pageContext
						.getAttribute("distributableSumTot"))%></td>
							<td align="right"><%=pageContext.getAttribute("totalTerminal")%></td>
						</tr>
					</table>
					<table cellpadding="2" align="center">
						<tr>
							<th>
								<s:text name="label.color.code" />
							</th>
							<th>
								<s:text name="label.status" />
							</th>
							<th>
								<s:text name="label.condition" />
							</th>
						</tr>
						<tr></tr>
						<tr>
							<td style="background-color: RED"></td>
							<td>
								<s:text name="label.very.low" />
							</td>
							<td>
								<s:text name="label.when" />
								<s:text name="label.sNo" />
								<strong><s:text name="label.dist.limit" /> </strong>
								<s:text name="label.less.than" />
								5%
								<s:text name="label.of" />
								<strong><s:text name="label.live.limit" /> </strong>
							</td>
						</tr>
						<tr>
							<td style="background-color: #FFA825"></td>
							<td>
								<s:text name="label.medium" />
							</td>
							<td>
								<s:text name="label.when" />
								<strong><s:text name="label.dist.limit" /> </strong>
								<s:text name="label.less.than" />
								20%
								<s:text name="label.of" />
								<strong><s:text name="label.live.limit" /> </strong>
							</td>
						</tr>
						<tr>
							<td style="background-color: Green"></td>
							<td>
								<s:text name="label.high" />
							</td>
							<td>
								<s:text name="label.other" />
							</td>
						</tr>
					</table>
				</div>
			</div>
		</s:if>
		<s:else>
			<div style="text-align: center">
				<s:text name="msg.no.record" />
			</div>
		</s:else>

		<table border="0" cellpadding="2" cellspacing="0"
			bordercolor="#CCCCCC" align="center">
			<tr>
				<td>
					<div id="agentLimitDiv">
					</div>
				</td>
			</tr>
		</table>
	</body>
</html>
<code id="headId" style="visibility: hidden">
	$RCSfile: bo_rep_limitDistributionReport.jsp,v $ $Revision:
	1.1.2.13.2.3 $
</code>