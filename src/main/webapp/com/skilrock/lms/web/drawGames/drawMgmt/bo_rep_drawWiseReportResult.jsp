<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@page import="com.skilrock.lms.dge.beans.ReportBeanDrawModule"%>
<%@page import="com.skilrock.lms.dge.beans.ReportDrawBean"%>
<%@page import="com.skilrock.lms.dge.beans.ReportGameBean"%>
<%@page import="com.skilrock.lms.common.utility.FormatNumber"%>
<%
	response.setDateHeader("Expires", 0);
%>
<%
	response.setHeader("Cache-Control", "no-store"); //HTTP 1.1
	response.setHeader("Pragma", "no-cache"); //HTTP 1.0
	response.setDateHeader("Expires", 0); //prevents caching at the proxy server
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">

	<head>
		<s:head theme="ajax" debug="false" />
		<meta http-equiv="Content-Type"
			content="text/html; charset=utf-8" />
		<link rel="stylesheet"
			href="<%=request.getContextPath()%>/LMSImages/css/styles.css"
			type="text/css" />
		<title><%=application.getAttribute("JSP_PAGE_TITLE")%></title>
	</head>

	<body>
	<div align="left">
				<s:form action="exportReportAsExcelWithoutTable.action" method="post"
					enctype="multipart/form-data">
					<s:hidden id="tableValue" name="reportData" />
					<s:hidden name="reportName"
						value="Draw_Wise_Report" />
					<s:submit key="btn.exportasexcel"
						cssStyle="margin-right:-400px;font-size:15px" onclick="getTableData();" />
				</s:form>
			</div>

		<div id="wrap">
			<div id="top_form">
			
				
			
			<s:actionerror/>	
	<div id='tableDataDiv'>
				<table width="100%" border="1" cellpadding="3" cellspacing="0"	bordercolor="#CCCCCC" align="center">
					<tr >
					<center><u><b></><s:text name="label.draw.game.rep" /> <s:text name="label.from" /> : <s:property
						value="dateOfDraw" /> &nbsp;&nbsp;<s:text name="label.to" />&nbsp;:&nbsp; <s:property
						value="end_Date" /></b> </u></center>
				   </tr>
					<tr>
						<th colspan="16" align="center">
							<s:text name="label.draw.data" />
						</th>
					</tr>
					<tr>
						<th ><s:text name="label.draw.time" /></th>
						<th ><s:text name="label.draw.freeze.time" /></th>
						 <th>
							<s:text name="label.draw.name" />
						 </th>						
						<th>
							<s:text name="label.draw.status" />
						</th>
						<th>
							<s:text name="label.win.res" />
						</th>
						<th>
							<s:text name="label.no.tkts" />
						</th>
						<th>
							<s:text name="label.sale.val" />
						</th>
						<th>
							<s:text name="label.avg.sale.per.tkt" />
						</th>
						<th>
							<s:text name="label.no.win.tkt" />
						</th>
						<th>
							<s:text name="label.win.amt" />
						</th>
						<th>
							<s:text name="label.claim.tkts" />
						</th>
						<th>
							<s:text name="label.unclaim.tkts" />
						</th>
						<th>
							<s:text name="label.claim.amt" />
						</th>
						<th>
							<s:text name="label.unclaim.amt" />
						</th>
						<th>
							<s:text name="label.avg.sale.per.ret" />
						</th>
						<th>
						    <s:text name="label.ppr"/>
						</th>
					</tr>
					
					<s:set name="dataBeanList" value="drawDataBeanList"/>
					<s:if test="#dataBeanList.size>0">
					<!-- Initialize Variales For Table Data Total -->
				
					
					<s:set name="lmsTotalTicektsCount" value="0" />
					<s:set name="lmsTotalTicekts" value="0" />
					<s:set name="lmsTotalSal" value="0" />
					<s:set name="lmsTotalAvgSalePerTkt" value="0" />
					<s:set name="lmsTotalavgSalePerTkt" value="0" />	
					<s:set name="lmsTotalWinTkt" value="0" />
					<s:set name="lmsTotalClaimTkt" value="0" />
					<s:set name="lmsTotalWin" value="0" />
					<s:set name="lmsTotalClaim" value="0" />
					<s:set name="lmsTotalAvgSalePerRet" value="0" />
					<s:set name="lmsTotalavgSalePerRet" value="0" />
					<s:set name="lmsTotalCount" value="0" />
				    <s:set name="totalPPR" value="0"/>
					<s:set name="dataBean" id="dataBean" value="#dataBeanList" />
							<s:iterator id="beanCart" value="%{#dataBean}">
						        
								<tr class="startSortable">
									<td align="left">
										<s:property value="drawDateTime" />
									</td>
									<td align="left">
										<s:property value="drawFreezeTime" />
									</td>
									<td align="left" id='<s:property value="drawDateTime"/>--<s:property value="drawName"/>' onclick="getTicketWiseData(this.id)">
										<s:a   href="#"> <s:property value="drawName"/></s:a>
									</td>
									<td align="left" id='<s:property value="drawDateTime"/>'>
										<s:property value="drawStatus" />
									</td>
									<td align="left">
										<s:property value="winningResult" />
									</td>
												
									<td align="right">
										<s:set name="totalTkts" value="lmsSaleBean.totalNoTickets" />
										<%=pageContext.getAttribute("totalTkts")%>
									</td>
										<s:set name="lmsTotalTicekts" value="%{#lmsTotalTicekts +lmsSaleBean.totalNoTickets}" />
									<s:if test="#totalTkts>0">
										<s:set name="lmsTotalTicektsCount" value="%{#lmsTotalTicektsCount +1}" />
									</s:if>
							
							
								
				
									<td align="right">
										<s:set name="totalSaleAmt" value="lmsSaleBean.totalSaleValue" />
										<%=FormatNumber.formatNumberForJSP(pageContext.getAttribute("totalSaleAmt"))%>
											<s:set name="lmsTotalSal" value="%{#lmsTotalSal +#totalSaleAmt}" />
									</td>
								
									<td align="right">
										<s:set name="avgSalePerTkt" value="lmsSaleBean.avgSalePerTkt" />
										<s:if test="#avgSalePerTkt>0" >
										<s:set name="lmsTotalavgSalePerTkt" value="%{#lmsTotalavgSalePerTkt +#avgSalePerTkt}" />
										
										</s:if>
											<%=FormatNumber.formatNumberForJSP(pageContext.getAttribute("avgSalePerTkt"))%>
									</td>
									
									<td align="right">
										<s:set name="totalWinTkts" value="lmsSaleBean.totalWinningTickets" />
											<%=pageContext.getAttribute("totalWinTkts")%>
									</td>	
									<s:set name="lmsTotalWinTkt" value="%{#lmsTotalWinTkt +lmsSaleBean.totalWinningTickets}" />
									<td align="right">
										<s:set name="totalWinAmt" value="lmsSaleBean.totalWinningAmount" />
										<%=FormatNumber.formatNumberForJSP(pageContext.getAttribute("totalWinAmt"))%>
									</td>
									<s:set name="lmsTotalWin" value="%{#lmsTotalWin +lmsSaleBean.totalWinningAmount}"/>	
									<td align="right">
										<s:set name="totalClaimTkts" value="lmsSaleBean.totalClaimedTickets" />
												<%=pageContext.getAttribute("totalClaimTkts")%>
									</td>
										<s:set name="lmsTotalClaimTkt" value="%{#lmsTotalClaimTkt +lmsSaleBean.totalClaimedTickets}"/>
										
										<td align="right">
										<s:set name="totalUnclaimTkts" value="%{#totalWinTkts-#totalClaimTkts}" />
												<%=pageContext.getAttribute("totalUnclaimTkts")%>
										</td>

									<td align="right">
										<s:set name="lmsClaimAmt" value="lmsSaleBean.claimedAmount" />
										<%=FormatNumber.formatNumberForJSP(pageContext.getAttribute("lmsClaimAmt"))%>
									</td>
									<s:set name="lmsTotalClaim" value="%{#lmsTotalClaim +lmsSaleBean.claimedAmount}" />
										
																				
										<td align="right">
										<s:set name="lmsUnclaimAmt" value="%{#totalWinAmt-#lmsClaimAmt}" />
												<%=FormatNumber.formatNumberForJSP(pageContext.getAttribute("lmsUnclaimAmt"))%>
										</td>
										
									<td align="right">
										<s:set name="avgSalePerRet" value="lmsSaleBean.avgSalePerRet" />
										<%=FormatNumber.formatNumberForJSP(pageContext.getAttribute("avgSalePerRet")
										)%>
									</td>
									<s:if test="#avgSalePerRet>0">
									<s:set name="lmsTotalavgSalePerRet" value="%{#lmsTotalavgSalePerRet +#avgSalePerRet}" />
									<s:set name="lmsTotalCount" value="%{#lmsTotalCount +1}"  />
									</s:if>	
									<td align="right">
									     <s:if test="%{#totalSaleAmt!=0.00}">
											<s:set name="PPR" value="#totalWinAmt/#totalSaleAmt*100"/>
											<%=FormatNumber.formatNumberForJSP(pageContext.getAttribute("PPR"))%>
										</s:if>
										<s:else>
										   <s:property value="0.0"/>
										</s:else>
									</td>
								</tr>
						
						</s:iterator>
						<tr>
						
									<td colspan="5"  align="center">
										<s:text name="label.total" />
									</td>
									<td align="right"><s:set name="totalTkts" value="%{#lmsTotalTicekts}" />
									
									<%=pageContext.getAttribute("totalTkts")%>
									
									</td>
									<td align="right"><s:set name="totalSale" value="%{#lmsTotalSal}" />
									
										<%=FormatNumber.formatNumberForJSP(pageContext.getAttribute("totalSale"))%>
									</td>
									<s:if test="#lmsTotalTicektsCount>0">
											<s:set name="lmsTotalAvgSalePerTkt" value="%{#totalSale/#totalTkts}" />
										<td align="right">
										<%=FormatNumber.formatNumberForJSP(pageContext.getAttribute("lmsTotalAvgSalePerTkt"))%>
										</td>
									</s:if>
									<s:else>
									<td align="right"><s:property value="%{#lmsTotalAvgSalePerTkt}" /></td>
									</s:else>
									<td align="right"><s:set name="totalWinTkts" value="%{#lmsTotalWinTkt}" />
									
									<%=pageContext.getAttribute("totalWinTkts")%>
									</td>
									<td align="right"><s:set name="totalWinAmt" value="%{#lmsTotalWin}" />
									
									
									<%=FormatNumber.formatNumberForJSP(pageContext.getAttribute("totalWinAmt"))%>
									</td>
									<td align="right"><s:set name="totalClaimTkt" value="%{#lmsTotalClaimTkt}" />
									
									
									<%=pageContext.getAttribute("totalClaimTkt")%>
									</td>
									<td align="right"><s:set name="totalUnclaimTkt" value="%{#lmsTotalWinTkt-#lmsTotalClaimTkt}" />
									
									
									<%=pageContext.getAttribute("totalUnclaimTkt")%>
									</td>
									
									<td align="right"><s:set name="totalClaimAmt" value="%{#lmsTotalClaim}" />
									<%=FormatNumber.formatNumberForJSP(pageContext.getAttribute("totalClaimAmt"))%>
									</td>
									
									<td align="right"><s:set name="totalUnClaimAmt" value="%{#lmsTotalWin-#lmsTotalClaim}" />
									<%=FormatNumber.formatNumberForJSP(pageContext.getAttribute("totalUnClaimAmt"))%>
									</td>
									
									
									<s:if test="#lmsTotalCount>0">
											<s:set name="lmsTotalAvgSalePerRet" value="%{#lmsTotalavgSalePerRet/#lmsTotalCount}" />
										<td align="right">
										<%=FormatNumber.formatNumberForJSP(pageContext.getAttribute("lmsTotalAvgSalePerRet"))%>
										</td>
									</s:if>
									<s:else>
									<td align="right"><s:property value="%{#lmsTotalAvgSalePerRet}" /></td>
									</s:else>
									<td align="right">
									    <s:if test="%{#lmsTotalSal!=0.00}">
									   		 <s:set name="totalPPR" value="#lmsTotalWin/#lmsTotalSal*100" />
											 <%=FormatNumber.formatNumberForJSP(pageContext.getAttribute("totalPPR"))%>
										</s:if>
										<s:else>
										   <s:property value="0.0"/>
										</s:else>
									</td>
						</tr>
					</s:if>
					<s:else>
						<tr>
							<td colspan="6" align="center">
								<s:text name="msg.no.record.process" />
							</td>
						</tr>
					</s:else>
				</table>



</div>
			</div>
		</div>



	</body>
	
</html>
<code id="headId" style="visibility: hidden">
$RCSfile: bo_rep_drawWiseReportResult.jsp,v $
$Revision: 1.5 $
</code>
