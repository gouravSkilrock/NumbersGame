<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="com.skilrock.lms.common.utility.CommonMethods"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.util.*"%>
<%@taglib prefix="s" uri="/struts-tags"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
	<head>
		<s:head theme="ajax" debug="false" />
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<link rel="stylesheet" href="<%=request.getContextPath()%>/LMSImages/css/styles.css" type="text/css" />
		<title><%=application.getAttribute("JSP_PAGE_TITLE")%></title>
		<link type="text/css" rel="stylesheet" href="<%=request.getContextPath()%>/LMSImages/css/lmsCalendar.css" media="screen" />
		<script>var projectName="<%=request.getContextPath()%>"</script>
		<script type="text/javascript" src="<%=request.getContextPath()%>/com/skilrock/lms/web/reportsMgmt/backOffice/js/report.js"></script>
		<script type="text/javascript" src="<%=request.getContextPath()%>/com/skilrock/lms/web/common/globalJs/calender.js"></script>
	</head>
	<body">
		<div id="wrap">
			<div id="excelDiv">
				<s:if test="%{(rainbowBillingReportDataBeans neq null && rainbowBillingReportDataBeans.size() > 0) || (rainbowWinReportDataMap neq null && rainbowWinReportDataMap.size() > 0)}">
					<s:form action="bo_rep_daily_billing_report_Export" method="post" enctype="multipart/form-data">
						<s:hidden id="tableValue" name="reportData" />
						<s:submit value="Export As Excel" cssStyle="margin-right:-400px;font-size:15px" onclick="getData();" />
					</s:form>
					<div id = "tableDataDiv">
						<center><b><u><s:text name ="Daily Billing Report From : "/><s:property value="sDate"/><s:text name=" To :"/><s:property value="eDate"/></u></b></center>
						<br />
						<s:if test = "reportType == 'CONSOLIDATED'">
							<table width="100%" border="1" cellpadding="3" cellspacing="0" bordercolor="#CCCCCC" align="center">
								<tr>
									<th></th>
									<th></th>
									<th align="center" colspan = "2"><s:text name="label.draw.ist.prize"/></th>
									<th align="center" colspan = "2"><s:text name="label.draw.2nd.prize"/></th>
									<th align="center" colspan = "2"><s:text name="label.draw.special.prize"/></th>
									<th align="center" colspan = "2"><s:text name="label.total"/></th>
								</tr>
								<tr>
									<th align="center"><s:text name="label.game.type"/></th>
									<th align="center"><s:text name="label.draw.tkt.type"/></th>
									<th align="center"><s:text name="label.count"/></th>
									<th align="center"><s:text name="label.amount"/></th>
									<th align="center"><s:text name="label.count"/></th>
									<th align="center"><s:text name="label.amount"/></th>
									<th align="center"><s:text name="label.count"/></th>
									<th align="center"><s:text name="label.amount"/></th>
									<th align="center"><s:text name="label.count"/></th>
									<th align="center"><s:text name="label.amount"/></th>
								</tr>
								<s:iterator value="rainbowWinReportDataMap">
									<s:set name="totalWinTkts" value="%{value.totalFirstPrizeTkts + value.totalSecondPrizeTkts + value.totalSpecialPrizeTkts}" />
									<s:set name="totalWinAmt" value="%{value.totalFirstPrizeAmt + value.totalSecondPrizeAmt + value.totalSpecialPrizeAmt}" />
									<s:set name="totalClaimedTkt" value="%{value.totalFirstPrizeClaimedTkts + value.totalSecondPrizeClaimedTkts + value.totalSpecialPrizeClaimedTkts}" />
									<s:set name="totalClaimedAmt" value="%{value.totalFirstPrizeClaimedAmt + value.totalSecondPrizeClaimedAmt + value.totalSpecialPrizeClaimedAmt}" />
									
									<s:set name="fUnclaimedTkts" value="%{value.totalFirstPrizeTkts - value.totalFirstPrizeClaimedTkts}" />
									<s:set name="sUnclaimedTkts" value="%{value.totalSecondPrizeTkts - value.totalSecondPrizeClaimedTkts}" />
									<s:set name="spUnclaimedTkts" value="%{value.totalSpecialPrizeTkts - value.totalSpecialPrizeClaimedTkts}" />
									
									<s:set name="fUnclaimedAmt" value="%{value.totalFirstPrizeAmt - value.totalFirstPrizeClaimedAmt}" />
									<s:set name="sUnclaimedAmt" value="%{value.totalSecondPrizeAmt - value.totalSecondPrizeClaimedAmt}" />
									<s:set name="spUnclaimedAmt" value="%{value.totalSpecialPrizeAmt - value.totalSpecialPrizeClaimedAmt}" />
				
									<s:set name="totalUnClaimedTkt" value="%{#totalWinTkts - #totalClaimedTkt}" />
									<s:set name="totalUnClaimedAmt" value="%{#totalWinAmt - #totalClaimedAmt}" />
									<tr>
										<td rowspan = "3"><s:property value="key" /></td>
										<td><s:text name="label.draw.total.winning.tkts"/></td>
										<td><s:property value="value.totalFirstPrizeTkts"/></td>
										<td><s:property value="value.totalFirstPrizeAmt"/></td>
										<td><s:property value="value.totalSecondPrizeTkts"/></td>
										<td><s:property value="value.totalSecondPrizeAmt"/></td>
										<td><s:property value="value.totalSpecialPrizeTkts"/></td>
										<td><s:property value="value.totalSpecialPrizeAmt"/></td>
										<td><s:property value="%{#totalWinTkts}"/></td>
										<td><s:property value="%{#totalWinAmt}"/></td>
									</tr>
								   	<tr>
										<td><s:text name="label.claim.tkts"/></td>
										<td><s:property value="value.totalFirstPrizeClaimedTkts"/></td>
										<td><s:property value="value.totalFirstPrizeClaimedAmt"/></td>
										<td><s:property value="value.totalSecondPrizeClaimedTkts"/></td>
										<td><s:property value="value.totalSecondPrizeClaimedAmt"/></td>
										<td><s:property value="value.totalSpecialPrizeClaimedTkts"/></td>
										<td><s:property value="value.totalSpecialPrizeClaimedAmt"/></td>
										<td><s:property value="%{#totalClaimedTkt}"/></td>
										<td><s:property value="%{#totalClaimedAmt}"/></td>
									</tr>
									<tr>
										<td><s:text name="label.unclaim.tkts"/></td>
										<td><s:property value="%{#fUnclaimedTkts}"/></td>
										<td><s:property value="%{#fUnclaimedAmt}"/></td>
										<td><s:property value="%{#sUnclaimedTkts}"/></td>
										<td><s:property value="%{#sUnclaimedAmt}"/></td>
										<td><s:property value="%{#spUnclaimedTkts}"/></td>
										<td><s:property value="%{#spUnclaimedAmt}"/></td>
										<td><s:property value="%{#totalUnClaimedTkt}"/></td>
										<td><s:property value="%{#totalUnClaimedAmt}"/></td>
									</tr>
								</s:iterator>
							</table>
							<br/><br/><br/>
							<table width="100%" border="1" cellpadding="3" cellspacing="0" bordercolor="#CCCCCC" align="center">
								<tr>
									<th align="center"><s:text name="label.game.type"/></th> 
									<th align="center"><s:text name="label.draw.total.nbr.tkts" /></th>
									<th align="center"><s:text name="label.total.sale.amt"/></th>
								</tr>
								<s:iterator value="rainbowWinReportDataMap">
									<s:if test="key == 'BASIC'">
										<s:set name="basicTotalTkts" value="%{value.totalTkts}" />
										<s:set name="basicTotalAmt" value="%{value.totalSale}" />
									</s:if>
									<s:elseif test="%{key == 'POWER'}">
										<s:set name="powerTotalTkts" value="%{value.totalTkts}" />
										<s:set name="powerTotalAmt" value="%{value.totalSale}" />
									</s:elseif>
									<tr> 
										<td><s:property value="key" /></td>
										<td><s:property value = "value.totalTkts"/></td>
										<td><s:property value = "value.totalSale"/></td>
									</tr>
								</s:iterator>
								<s:set name ="totalTickets" value = "%{#basicTotalTkts + #powerTotalTkts}"/>
								<s:set name ="totalTktsAmount" value = "%{#basicTotalAmt + #powerTotalAmt}"/>
								<tr>
									<td>Total</td>
									<td><s:property value="#totalTickets"/></td>
									<td><s:property value="#totalTktsAmount"/></td>
								</tr>
							</table>
						</s:if>
						<s:elseif test="reportType == 'DRAW_WISE'">
							<table width="100%" border="1" cellpadding="3" cellspacing="0" bordercolor="#CCCCCC" align="center">
								<tr>
									<th colspan = "4"></th>
									<th colspan = "6" align="center">BASIC</th>
									<th colspan = "6" align="center">POWER</th>
								</tr>
								<tr>
									<th align="center"><s:text name="label.draw.date.time"/></th>
									<th align="center"><s:text name="label.draw.event.id"/></th>
									<th align="center"><s:text name="label.sold.tkt.count" /></th>
									<th align="center"><s:text name="label.draw.total.sale.amount"/></th>
									<th align="center"><s:text name="label.draw.total.winning.tkts"/></th>
									<th align="center"><s:text name="label.draw.win.amount"/></th>
									<th align="center"><s:text name="label.claim.tkts" /></th>
									<th align="center"><s:text name="label.claim.amt"/></th>
									<th align="center"><s:text name="label.unclaim.tkts"/></th>
									<th align="center"><s:text name="label.unclaim.amt"/></th>
									<th align="center"><s:text name="label.draw.total.winning.tkts"/></th>
									<th align="center"><s:text name="label.draw.win.amount"/></th>
									<th align="center"><s:text name="label.claim.tkts" /></th>
									<th align="center"><s:text name="label.claim.amt"/></th>
									<th align="center"><s:text name="label.unclaim.tkts"/></th>
									<th align="center"><s:text name="label.unclaim.amt"/></th>
								</tr>
								<s:iterator value="rainbowBillingReportDataBeans">
									<s:set name="totalSoldTickets" value="%{basicDataBean.totalTkts + powerDataBean.totalTkts}"></s:set>
									<s:set name="totalSoldTicketsAmt" value="%{basicDataBean.totalSale + powerDataBean.totalSale}"></s:set>
									
									<s:set name="basicTotalWinTkts" value="%{basicDataBean.totalFirstPrizeTkts + basicDataBean.totalSecondPrizeTkts + basicDataBean.totalSpecialPrizeTkts}"></s:set>
									<s:set name="basicTotalWinAmt" value="%{basicDataBean.totalFirstPrizeAmt + basicDataBean.totalSecondPrizeAmt + basicDataBean.totalSpecialPrizeAmt}"></s:set>
									<s:set name="basicTotalClaimedTkts" value="%{basicDataBean.totalFirstPrizeClaimedTkts + basicDataBean.totalSecondPrizeClaimedTkts + basicDataBean.totalSpecialPrizeClaimedTkts}"></s:set>
									<s:set name="basicTotalClaimedAmt" value="%{basicDataBean.totalFirstPrizeClaimedAmt + basicDataBean.totalSecondPrizeClaimedAmt + basicDataBean.totalSpecialPrizeClaimedAmt}"></s:set>
									<s:set name="basicTotalUnClaimedTkts" value="%{#basicTotalWinTkts - #basicTotalClaimedTkts}"></s:set>
									<s:set name="basicTotalUnClaimedAmt" value="%{#basicTotalWinAmt - #basicTotalClaimedAmt}"></s:set>
									
									<s:set name="powerTotalWinTkts" value="%{powerDataBean.totalFirstPrizeTkts + powerDataBean.totalSecondPrizeTkts + powerDataBean.totalSpecialPrizeTkts}"></s:set>
									<s:set name="powerTotalWinAmt" value="%{powerDataBean.totalFirstPrizeAmt + powerDataBean.totalSecondPrizeAmt + powerDataBean.totalSpecialPrizeAmt}"></s:set>
									<s:set name="powerTotalClaimedTkts" value="%{powerDataBean.totalFirstPrizeClaimedTkts + powerDataBean.totalSecondPrizeClaimedTkts + powerDataBean.totalSpecialPrizeClaimedTkts}"></s:set>
									<s:set name="powerTotalClaimedAmt" value="%{powerDataBean.totalFirstPrizeClaimedAmt + powerDataBean.totalSecondPrizeClaimedAmt + powerDataBean.totalSpecialPrizeClaimedAmt}"></s:set>
									<s:set name="powerTotalUnClaimedTkts" value="%{#powerTotalWinTkts - #powerTotalClaimedTkts}"></s:set>
									<s:set name="powerTotalUnClaimedAmt" value="%{#powerTotalWinAmt - #powerTotalClaimedAmt}"></s:set>
									<tr>
										<td><s:property value="%{drawDateTime}"/></td>
										<td><s:property value="%{eventId}"/></td>
										<td><s:property value="%{#totalSoldTickets}"/></td>
										<td><s:property value="%{#totalSoldTicketsAmt}"/></td>
										
										<td><s:property value="%{#basicTotalWinTkts}"/></td>
										<td><s:property value="%{#basicTotalWinAmt}"/></td>
										<td><s:property value="%{#basicTotalClaimedTkts}"/></td>
										<td><s:property value="%{#basicTotalClaimedAmt}"/></td>
										<td><s:property value="%{#basicTotalUnClaimedTkts}"/></td>
										<td><s:property value="%{#basicTotalUnClaimedAmt}"/></td>
										
										<td><s:property value="%{#powerTotalWinTkts}"/></td>
										<td><s:property value="%{#powerTotalWinAmt}"/></td>
										<td><s:property value="%{#powerTotalClaimedTkts}"/></td>
										<td><s:property value="%{#powerTotalClaimedAmt}"/></td>
										<td><s:property value="%{#powerTotalUnClaimedTkts}"/></td>
										<td><s:property value="%{#powerTotalUnClaimedAmt}"/></td>
									</tr>
								</s:iterator> 
							</table>
						</s:elseif>
					</div>
				</s:if>
				<s:else>
					No Data Available
				</s:else>
			</div>
		</div>
	</body>
</html>
<code id="headId" style="visibility: hidden">
	$RCSfile: bo_rep_daily_billing_report_result.jsp,v $ $Revision:
	1.1.2.4.6.1 $
</code>