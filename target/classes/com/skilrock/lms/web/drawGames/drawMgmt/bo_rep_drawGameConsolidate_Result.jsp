<%@page import="com.skilrock.lms.common.utility.FormatNumber"%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

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
	    <script  src="<%=request.getContextPath() %>/com/skilrock/lms/web/drawGames/drawMgmt/js/bo_rep_drawGameConsolidate.js"></script>
	</head>
	<body>
	
		<div id="wrap">
			<div id="top_form">
			 <s:actionerror/>
			 <s:set name="dataBeanList" value="drawDataBeanList"/>
					<s:if test="#dataBeanList.size>0">
			 <input type="checkbox" name="noOfTkts" onchange="check('noOfTkts');" value="No Of Tickets">No Of Tickets</input>
			 <input type="checkbox" name="sale" checked="checked" onchange="check('sale');" value="Sale" disabled="disabled">Sale</input>
			 <input type="checkbox" name="winAmt" onchange="check('winAmt');" value="Winning Amount">Winning Amount</input>
			 <input type="checkbox" name="claimedAmt" onchange="check('claimedAmt');" value="Claimed Amount">Claimed Amount</input>
			 </s:if>
				<table width="1000px" border="1" cellpadding="3" cellspacing="0"
					bordercolor="#CCCCCC" align="center">
					<s:set name="dataBeanList" value="drawDataBeanList"/>
					<s:if test="#dataBeanList.size>0">
					<tr>
					<s:if test="%{#application.COUNTRY_DEPLOYED=='ZIMBABWE'}">
                        <th colspan="25" align="center">
							<s:text name="label.draw.data" /> 
					    </th>
					</s:if>
					<s:elseif test="%{#application.COUNTRY_DEPLOYED=='LAGOS'}">
                        <th colspan="21" align="center">
							<s:text name="label.draw.data" /> 
					    </th>
					</s:elseif>
					<s:else>
						<th colspan="17" align="center">
							<s:text name="label.draw.data" /> 
						</th>
					</s:else>
					</tr>
					<tr>
						<th rowspan="2"><s:text name="label.draw.time" /></th>
						<th rowspan="2"><s:text name="label.draw.freeze.time" /></th>
						<th rowspan="2" align="center">
							<s:text name="label.draw.name" />
						</th >
						<th  rowspan="2" align="center">
							<s:text name="label.draw.status" />
						</th>
						<th rowspan="2" align="center">
							<s:text name="label.win.res" />
						</th>
						<th class="column" colspan="1" align="center">
							<s:text name="label.pms.sale" /> 
						</th>
						<th class="column" colspan="1" align="center">
							<s:text name="label.weaver.sale" /> 
						</th>
						<th class="column" colspan="1"  align="center">
							<s:text name="label.lms.sale" /> 
						</th>
						<s:if test="%{#application.COUNTRY_DEPLOYED=='ZIMBABWE'}">
							<th class="column" colspan="1" align="center">
		                        <s:text name="label.okpos.sale"/>					
							</th>
							<th class="column" colspan="1" align="center">
							<s:text name="label.afrosoft.sale"/>
							</th>
						</s:if>
						<s:elseif test="%{#application.COUNTRY_DEPLOYED == 'NIGERIA'}">
							<th class="column" colspan="1" align="center">
								<s:text name="label.mtn.sale"/>
							</th>
						</s:elseif>
						<th class="column" colspan="1"  align="center">
							<s:text name="label.total" />
						</th>
					</tr>
					<tr>
						<th class="noOfTkts" style="display: none;">
							<s:text name="label.no.tkts" /> 
						</th>
						<th class="sale">
							<s:text name="label.sale.amt" />
						</th>
						<th class="winAmt" style="display: none;">
							<s:text name="label.win.amt" />
						</th>
						<th class="claimedAmt" style="display: none;">
							<s:text name="label.claim.amt" />
						</th>
						<th class="noOfTkts" style="display: none;">
							<s:text name="label.no.tkts" /> 
						</th>
						<th class="sale">
							<s:text name="label.sale.amt" />
						</th>
						<th class="winAmt" style="display: none;">
							<s:text name="label.win.amt" />
						</th>
						<th class="claimedAmt" style="display: none;">
							<s:text name="label.claim.amt" />
						</th>
						<s:if test="%{#application.COUNTRY_DEPLOYED=='ZIMBABWE'}">
							<th class="noOfTkts" style="display: none;">
								<s:text name="label.no.tkts" /> 
							</th>
							<th class="sale">
								<s:text name="label.sale.amt" />
							</th>
							<th class="winAmt" style="display: none;">
								<s:text name="label.win.amt" />
							</th>
							<th class="claimedAmt" style="display: none;">
								<s:text name="label.claim.amt" />
							</th>
							<th class="noOfTkts" style="display: none;">
								<s:text name="label.no.tkts" /> 
							</th>
							<th class="sale">
								<s:text name="label.sale.amt" />
							</th>
							<th class="winAmt" style="display: none;">
								<s:text name="label.win.amt" />
							</th>
							<th class="claimedAmt" style="display: none;">
								<s:text name="label.claim.amt" />
							</th>
						</s:if>
						<s:elseif test="%{#application.COUNTRY_DEPLOYED=='NIGERIA'}">
							<th class="noOfTkts" style="display: none;">
								<s:text name="label.no.tkts" /> 
							</th>
							<th class="sale">
								<s:text name="label.sale.amt" />
							</th>
							<th class="winAmt" style="display: none;">
								<s:text name="label.win.amt" />
							</th>
							<th class="claimedAmt" style="display: none;">
								<s:text name="label.claim.amt" />
							</th>
						</s:elseif>
						<th class="noOfTkts" style="display: none;">
							<s:text name="label.no.tkts" /> 
						</th>
						<th class="sale">
							<s:text name="label.sale.amt" />
						</th>
						<th class="winAmt" style="display: none;">
							<s:text name="label.win.amt" />
						</th>
						<th class="claimedAmt" style="display: none;">
							<s:text name="label.claim.amt" />
						</th>
					</tr>
					
						<!-- Initialize Variales For Table Data Total -->
						<s:set name="pmsTotalTicekts" value="0" />
						<s:set name="pmsTotalSal" value="0" />
						<s:set name="pmsTotalWin" value="0" />
						<s:set name="pmsTotalClaim" value="0" />
					
						<s:set name="weaverTotalTicekts" value="0" />
						<s:set name="weaverTotalSal" value="0" />
						<s:set name="weaverTotalWin" value="0" />
						<s:set name="weaverTotalClaim" value="0" />
							
						<s:set name="lmsTotalTicekts" value="0" />
						<s:set name="lmsTotalSal" value="0" />
						<s:set name="lmsTotalWin" value="0" />
						<s:set name="lmsTotalClaim" value="0" />
					    		
						<s:set name="okPosTotalTicekts" value="0" />
						<s:set name="okPosTotalSal" value="0" />
						<s:set name="okPosTotalWin" value="0" />
						<s:set name="okPosTotalClaim" value="0" />
					    
					    <s:set name="aSoftTotalTicekts" value="0" />
						<s:set name="aSoftTotalSal" value="0" />
						<s:set name="aSoftTotalWin" value="0" />
						<s:set name="aSoftTotalClaim" value="0" />
						
						<s:set name="mtnTotalTicekts" value="0" />
						<s:set name="mtnTotalSal" value="0" />
						<s:set name="mtnTotalWin" value="0" />
						<s:set name="mtnTotalClaim" value="0" />
					    
						<s:set name="totalTicekts" value="0" />
						<s:set name="totalSal" value="0" />
						<s:set name="totalWin" value="0" />
						<s:set name="totalClaim" value="0" />
						<s:set name="dataBean" id="dataBean" value="#dataBeanList" />
							<s:iterator id="beanCart" value="%{#dataBean}">
								<tr>
									<td align="left">
										<s:property value="drawDateTime" />
									</td>
									<td align="left">
										<s:property value="drawFreezeTime" />
									</td>
									<td align="left">
										<s:property value="drawName" />
									</td>
									<td align="left">
										<s:property value="drawStatus" />
									</td>
									<td align="left">
										<s:property value="winningResult" />
									</td>
									<td align="right" class="noOfTkts" style="display: none;">
										 <s:property value="pmsSaleBean.totalNoTickets" /> 
									</td>
									<s:set name="pmsTotalTicekts" value="%{#pmsTotalTicekts +pmsSaleBean.totalNoTickets}" />
									<td align="right" class="sale">
										<s:set name="pmsSale" value="pmsSaleBean.totalSaleValue" />
										<%=FormatNumber.formatNumberForJSP(pageContext
												.getAttribute("pmsSale"))%>
									</td>
									<s:set name="pmsTotalSal" value="%{#pmsTotalSal +pmsSaleBean.totalSaleValue}" />	
									<td align="right" class="winAmt" style="display: none;">
										<s:set name="pmsWinAmt" value="pmsSaleBean.totalWinningAmount" />
										<%=FormatNumber.formatNumberForJSP(pageContext
												.getAttribute("pmsWinAmt"))%>
									</td>
									<s:set name="pmsTotalWin" value="%{#pmsTotalWin +pmsSaleBean.totalWinningAmount}" />		
										<td align="right" class="claimedAmt" style="display: none;">
										<s:set name="pmsClaimAmt" value="pmsSaleBean.claimedAmount" />
										<%=FormatNumber.formatNumberForJSP(pageContext
												.getAttribute("pmsClaimAmt"))%>
									</td>
									<s:set name="pmsTotalClaim" value="%{#pmsTotalClaim +pmsSaleBean.claimedAmount}" />
								    <td align="right" class="noOfTkts" style="display: none;">
								    	<s:property  value="lmsSaleBean.totalNoTickets" />
									</td>
									
									
									<td align="right" class="noOfTkts" style="display: none;">
										 <s:property value="weaverSaleBean.totalNoTickets" /> 
									</td>
									<s:set name="weaverTotalTicekts" value="%{#weaverTotalTicekts +weaverSaleBean.totalNoTickets}" />
									<td align="right" class="sale">
										<s:set name="weaverSale" value="weaverSaleBean.totalSaleValue" />
										<%=FormatNumber.formatNumberForJSP(pageContext
												.getAttribute("weaverSale"))%>
									</td>
									<s:set name="weaverTotalSal" value="%{#weaverTotalSal +weaverSaleBean.totalSaleValue}" />	
									<td align="right" class="winAmt" style="display: none;">
										<s:set name="weaverWinAmt" value="weaverSaleBean.totalWinningAmount" />
										<%=FormatNumber.formatNumberForJSP(pageContext
												.getAttribute("weaverWinAmt"))%>
									</td>
									<s:set name="weaverTotalWin" value="%{#weaverTotalWin +weaverSaleBean.totalWinningAmount}" />		
										<td align="right" class="claimedAmt" style="display: none;">
										<s:set name="weaverClaimAmt" value="weaverSaleBean.claimedAmount" />
										<%=FormatNumber.formatNumberForJSP(pageContext
												.getAttribute("weaverClaimAmt"))%>
									</td>
									<s:set name="weaverTotalClaim" value="%{#weaverTotalClaim +weaverSaleBean.claimedAmount}" />
								    <td align="right" class="noOfTkts" style="display: none;">
								    	<s:property  value="lmsSaleBean.totalNoTickets" />
									</td>
									
								<s:set name="lmsTotalTicekts" value="%{#lmsTotalTicekts +lmsSaleBean.totalNoTickets}" />
									<td align="right" class="sale">
										<s:set name="lmsSale" value="lmsSaleBean.totalSaleValue" />
											<%=FormatNumber.formatNumberForJSP(pageContext
												.getAttribute("lmsSale"))%>
									</td>
									<s:set name="lmsTotalSal" value="%{#lmsTotalSal +lmsSaleBean.totalSaleValue}" />	
									<td align="right" class="winAmt" style="display: none;">
										<s:set name="lmsWinAmt" value="lmsSaleBean.totalWinningAmount" />
											<%=FormatNumber.formatNumberForJSP(pageContext
												.getAttribute("lmsWinAmt"))%>
									</td>
										<s:set name="lmsTotalWin" value="%{#lmsTotalWin +lmsSaleBean.totalWinningAmount}"/>
									<td align="right" class="claimedAmt" style="display: none;">
										<s:set name="lmsClaimAmt" value="lmsSaleBean.claimedAmount" />
										<%=FormatNumber.formatNumberForJSP(pageContext
												.getAttribute("lmsClaimAmt"))%>
									</td>
									<s:set name="lmsTotalClaim" value="%{#lmsTotalClaim +lmsSaleBean.claimedAmount}" />
				                   <s:if test="%{#application.COUNTRY_DEPLOYED=='ZIMBABWE'}">
					                    <td align="right" class="noOfTkts" style="display: none;">
					                    	<s:property  value="okPosSaleBean.totalNoTickets" />
										</td>
											<s:set name="okPosTotalTicekts" value="%{#okPosTotalTicekts +okPosSaleBean.totalNoTickets}" />
										<td align="right" class="sale">
											<s:set name="okPosSale" value="okPosSaleBean.totalSaleValue" />
												<%=FormatNumber.formatNumberForJSP(pageContext
													.getAttribute("okPosSale"))%>
										</td>
										<s:set name="okPosTotalSal" value="%{#okPosTotalSal +okPosSaleBean.totalSaleValue}" />	
										<td align="right" class="winAmt" style="display: none;">
											<s:set name="okPosWinAmt" value="okPosSaleBean.totalWinningAmount" />
												<%=FormatNumber.formatNumberForJSP(pageContext
													.getAttribute("okPosWinAmt"))%>
										</td>
											<s:set name="okPosTotalWin" value="%{#okPosTotalWin +okPosSaleBean.totalWinningAmount}"/>
										<td align="right" class="claimedAmt" style="display: none;">
											<s:set name="okPosClaimAmt" value="okPosSaleBean.claimedAmount" />
											<%=FormatNumber.formatNumberForJSP(pageContext
													.getAttribute("okPosClaimAmt"))%>
										</td>
										<s:set name="okPosTotalClaim" value="%{#okPosTotalClaim +okPosSaleBean.claimedAmount}" />
					
					                    <td align="right" class="noOfTkts" style="display: none;">
					                    	<s:property  value="aSoftSaleBean.totalNoTickets" />
										</td>
											<s:set name="aSoftTotalTicekts" value="%{#aSoftTotalTicekts +aSoftSaleBean.totalNoTickets}" />
										<td align="right" class="sale">
											<s:set name="aSoftSale" value="aSoftSaleBean.totalSaleValue" />
												<%=FormatNumber.formatNumberForJSP(pageContext
													.getAttribute("aSoftSale"))%>
										</td>
										<s:set name="aSoftTotalSal" value="%{#aSoftTotalSal +aSoftSaleBean.totalSaleValue}" />	
										<td align="right" class="winAmt" style="display: none;">
											<s:set name="aSoftWinAmt" value="aSoftSaleBean.totalWinningAmount" />
												<%=FormatNumber.formatNumberForJSP(pageContext
													.getAttribute("aSoftWinAmt"))%>
										</td>
											<s:set name="aSoftTotalWin" value="%{#aSoftTotalWin +aSoftSaleBean.totalWinningAmount}"/>
										<td align="right" class="claimedAmt" style="display: none;">
											<s:set name="aSoftClaimAmt" value="aSoftSaleBean.claimedAmount" />
											<%=FormatNumber.formatNumberForJSP(pageContext
													.getAttribute("aSoftClaimAmt"))%>
										</td>
										<s:set name="aSoftTotalClaim" value="%{#aSoftTotalClaim +aSoftSaleBean.claimedAmount}" />
				                    </s:if>
				                     <s:elseif test="%{#application.COUNTRY_DEPLOYED=='NIGERIA'}">
					                    <td align="right" class="noOfTkts" style="display: none;">
					                    	<s:property  value="mtnSaleBean.totalNoTickets" />
										</td>
											<s:set name="mtnTotalTicekts" value="%{#mtnTotalTicekts + mtnSaleBean.totalNoTickets}" />
										<td align="right" class="sale">
											<s:set name="mtnSale" value="mtnSaleBean.totalSaleValue" />
												<%=FormatNumber.formatNumberForJSP(pageContext
													.getAttribute("mtnSale"))%>
										</td>
										<s:set name="mtnTotalSal" value="%{#mtnTotalSal +mtnSaleBean.totalSaleValue}" />	
										<td align="right" class="winAmt" style="display: none;">
											<s:set name="mtnWinAmt" value="mtnSaleBean.totalWinningAmount" />
												<%=FormatNumber.formatNumberForJSP(pageContext
													.getAttribute("mtnWinAmt"))%>
										</td>
											<s:set name="mtnTotalWin" value="%{#mtnTotalWin +mtnSaleBean.totalWinningAmount}"/>
										<td align="right" class="claimedAmt" style="display: none;">
											<s:set name="mtnClaimAmt" value="mtnSaleBean.claimedAmount" />
											<%=FormatNumber.formatNumberForJSP(pageContext
													.getAttribute("mtnClaimAmt"))%>
										</td>
										<s:set name="mtnTotalClaim" value="%{#mtnTotalClaim +mtnSaleBean.claimedAmount}" />
				                    </s:elseif>
			                  		<td align="right" class="noOfTkts" style="display: none;">
									<s:if test="%{#application.COUNTRY_DEPLOYED=='ZIMBABWE'}">
										<s:property  value="lmsSaleBean.totalNoTickets+pmsSaleBean.totalNoTickets+okPosSaleBean.totalNoTickets+aSoftSaleBean.totalNoTickets+weaverSaleBean.totalNoTickets" />
									</s:if>
									<s:elseif test="%{#application.COUNTRY_DEPLOYED=='NIGERIA'}">
										<s:property  value="lmsSaleBean.totalNoTickets+pmsSaleBean.totalNoTickets+mtnSaleBean.totalNoTickets+weaverSaleBean.totalNoTickets" />
									</s:elseif>
									<s:else>
										<s:property  value="lmsSaleBean.totalNoTickets+pmsSaleBean.totalNoTickets+weaverSaleBean.totalNoTickets" />
									</s:else>
									</td>
									<s:if test="%{#application.COUNTRY_DEPLOYED=='ZIMBABWE'}">
										<s:set name="totalTicekts" value="%{#totalTicekts +lmsSaleBean.totalNoTickets+pmsSaleBean.totalNoTickets+okPosSaleBean.totalNoTickets+aSoftSaleBean.totalNoTickets+weaverSaleBean.totalNoTickets}" />
									</s:if>
									<s:elseif test="%{#application.COUNTRY_DEPLOYED=='NIGERIA'}">
										<s:set name="totalTicekts" value="%{#totalTicekts +lmsSaleBean.totalNoTickets+pmsSaleBean.totalNoTickets+mtnSaleBean.totalNoTickets+weaverSaleBean.totalNoTickets}" />
									</s:elseif>
									<s:else>
					                    <s:set name="totalTicekts" value="%{#totalTicekts +lmsSaleBean.totalNoTickets+pmsSaleBean.totalNoTickets+weaverSaleBean.totalNoTickets}" />				
									</s:else>
									<td align="right" class="sale">
									<s:if test="%{#application.COUNTRY_DEPLOYED=='ZIMBABWE'}">
										<s:set name="totalSaleAmt" value="lmsSaleBean.totalSaleValue+pmsSaleBean.totalSaleValue+okPosSaleBean.totalSaleValue+aSoftSaleBean.totalSaleValue+weaverSaleBean.totalSaleValue" />
									</s:if>
									<s:elseif test="%{#application.COUNTRY_DEPLOYED=='NIGERIA'}">
										<s:set name="totalSaleAmt" value="lmsSaleBean.totalSaleValue+pmsSaleBean.totalSaleValue+mtnSaleBean.totalSaleValue+weaverSaleBean.totalSaleValue" />
									</s:elseif>
									<s:else>
										<s:set name="totalSaleAmt" value="lmsSaleBean.totalSaleValue+pmsSaleBean.totalSaleValue+weaverSaleBean.totalSaleValue" />
									</s:else>
										<%=FormatNumber.formatNumberForJSP(pageContext
												.getAttribute("totalSaleAmt"))%>
									</td>
									<s:if test="%{#application.COUNTRY_DEPLOYED=='ZIMBABWE'}">
										<s:set name="totalSal" value="%{#totalSal +lmsSaleBean.totalSaleValue+pmsSaleBean.totalSaleValue+okPosSaleBean.totalSaleValue+aSoftSaleBean.totalSaleValue+weaverSaleBean.totalSaleValue}" />
									</s:if>
									<s:elseif test="%{#application.COUNTRY_DEPLOYED=='NIGERIA'}">
										<s:set name="totalSal" value="%{#totalSal +lmsSaleBean.totalSaleValue+pmsSaleBean.totalSaleValue+mtnSaleBean.totalSaleValue+weaverSaleBean.totalSaleValue}" />
									</s:elseif>
									<s:else>
										<s:set name="totalSal" value="%{#totalSal +lmsSaleBean.totalSaleValue+pmsSaleBean.totalSaleValue+weaverSaleBean.totalSaleValue}" />
									</s:else>
									<td align="right" class="winAmt" style="display: none;">
									<s:if test="%{#application.COUNTRY_DEPLOYED=='ZIMBABWE'}">
										<s:set name="totalWinAmt" value="lmsSaleBean.totalWinningAmount+pmsSaleBean.totalWinningAmount+okPosSaleBean.totalWinningAmount+aSoftSaleBean.totalWinningAmount+weaverSaleBean.totalWinningAmount" />
									</s:if>
									<s:elseif test="%{#application.COUNTRY_DEPLOYED=='NIGERIA'}">
										<s:set name="totalWinAmt" value="lmsSaleBean.totalWinningAmount+pmsSaleBean.totalWinningAmount+mtnSaleBean.totalWinningAmount+weaverSaleBean.totalWinningAmount" />
									</s:elseif>
									<s:else>
										<s:set name="totalWinAmt" value="lmsSaleBean.totalWinningAmount+pmsSaleBean.totalWinningAmount+weaverSaleBean.totalWinningAmount" />
									</s:else>
										<%=FormatNumber.formatNumberForJSP(pageContext
												.getAttribute("totalWinAmt"))%>
									</td>
									<s:if test="%{#application.COUNTRY_DEPLOYED=='ZIMBABWE'}">
										<s:set name="totalWin" value="%{#totalWin +lmsSaleBean.totalWinningAmount+pmsSaleBean.totalWinningAmount+okPosSaleBean.totalWinningAmount+aSoftSaleBean.totalWinningAmount+weaverSaleBean.totalWinningAmount}" />
									</s:if>
									<s:elseif test="%{#application.COUNTRY_DEPLOYED=='NIGERIA'}">
										<s:set name="totalWin" value="%{#totalWin +lmsSaleBean.totalWinningAmount+pmsSaleBean.totalWinningAmount+mtnSaleBean.totalWinningAmount+weaverSaleBean.totalWinningAmount}" />
									</s:elseif>
									<s:else>
										<s:set name="totalWin" value="%{#totalWin +lmsSaleBean.totalWinningAmount+pmsSaleBean.totalWinningAmount+weaverSaleBean.totalWinningAmount}" />
									</s:else>
									<td align="right" class="claimedAmt" style="display: none;">
									<s:if test="%{#application.COUNTRY_DEPLOYED=='ZIMBABWE'}">
										<s:set name="totalclaimAmt" value="lmsSaleBean.claimedAmount+pmsSaleBean.claimedAmount+okPosSaleBean.claimedAmount+aSoftSaleBean.claimedAmount+weaverSaleBean.claimedAmount" />
									</s:if>
									<s:elseif test="%{#application.COUNTRY_DEPLOYED=='NIGERIA'}">
										<s:set name="totalclaimAmt" value="lmsSaleBean.claimedAmount+pmsSaleBean.claimedAmount+mtnSaleBean.claimedAmount+weaverSaleBean.claimedAmount" />
									</s:elseif>
									<s:else>
										<s:set name="totalclaimAmt" value="lmsSaleBean.claimedAmount+pmsSaleBean.claimedAmount+weaverSaleBean.claimedAmount" />
									</s:else>
										<%=FormatNumber.formatNumberForJSP(pageContext
												.getAttribute("totalclaimAmt"))%>
									</td>
									<s:if test="%{#application.COUNTRY_DEPLOYED=='ZIMBABWE'}">
										<s:set name="totalClaim" value="%{#totalClaim +lmsSaleBean.claimedAmount+pmsSaleBean.claimedAmount+okPosSaleBean.claimedAmount+aSoftSaleBean.claimedAmount+weaverSaleBean.claimedAmount}" />
				                    </s:if>
				                    <s:elseif test="%{#application.COUNTRY_DEPLOYED=='NIGERIA'}">
										<s:set name="totalClaim" value="%{#totalClaim +lmsSaleBean.claimedAmount+pmsSaleBean.claimedAmount+mtnSaleBean.claimedAmount+weaverSaleBean.claimedAmount}" />
				                    </s:elseif>
				                    <s:else>
				                	    <s:set name="totalClaim" value="%{#totalClaim +lmsSaleBean.claimedAmount+pmsSaleBean.claimedAmount+weaverSaleBean.claimedAmount}" />
				                    </s:else>
				                  </tr>
						</s:iterator>
						<tr>
									<td colspan="5"  align="center">
										TOTAL
									</td>
									<td align="right" class="noOfTkts" style="display: none;">	<%= pageContext
												.getAttribute("pmsTotalTicekts") %></td>
									<td align="right" class="sale"><%=FormatNumber.formatNumberForJSP(pageContext
												.getAttribute("pmsTotalSal"))%></td>
									<td align="right" class="winAmt" style="display: none;"><%=FormatNumber.formatNumberForJSP(pageContext
												.getAttribute("pmsTotalWin"))%></td>
									<td align="right" class="claimedAmt" style="display: none;"><%=FormatNumber.formatNumberForJSP(pageContext
												.getAttribute("pmsTotalClaim"))%></td>
									
									
									<td align="right" class="noOfTkts" style="display: none;">	<%= pageContext
												.getAttribute("weaverTotalTicekts") %></td>
									<td align="right" class="sale"><%=FormatNumber.formatNumberForJSP(pageContext
												.getAttribute("weaverTotalSal"))%></td>
									<td align="right" class="winAmt" style="display: none;"><%=FormatNumber.formatNumberForJSP(pageContext
												.getAttribute("weaverTotalWin"))%></td>
									<td align="right" class="claimedAmt" style="display: none;"><%=FormatNumber.formatNumberForJSP(pageContext
												.getAttribute("weaverTotalClaim"))%></td>
									
									<td align="right" class="noOfTkts" style="display: none;">	<%= pageContext
												.getAttribute("lmsTotalTicekts")%></td>
									<td align="right" class="sale"><%=FormatNumber.formatNumberForJSP(pageContext
												.getAttribute("lmsTotalSal"))%></td>
									<td align="right" class="winAmt" style="display: none;"><%=FormatNumber.formatNumberForJSP(pageContext
												.getAttribute("lmsTotalWin"))%></td>
									<td align="right" class="claimedAmt" style="display: none;"><%=FormatNumber.formatNumberForJSP(pageContext
												.getAttribute("lmsTotalClaim"))%></td>
									<s:if test="%{#application.COUNTRY_DEPLOYED=='ZIMBABWE'}">
										<td align="right" class="noOfTkts" style="display: none;">	<%= pageContext
													.getAttribute("okPosTotalTicekts")%></td>
										<td align="right" class="sale"><%=FormatNumber.formatNumberForJSP(pageContext
													.getAttribute("okPosTotalSal"))%></td>
										<td align="right" class="winAmt" style="display: none;"><%=FormatNumber.formatNumberForJSP(pageContext
													.getAttribute("okPosTotalWin"))%></td>
										<td align="right" class="claimedAmt" style="display: none;"><%=FormatNumber.formatNumberForJSP(pageContext
													.getAttribute("okPosTotalClaim"))%></td>
										<td align="right" class="noOfTkts" style="display: none;">	<%= pageContext
													.getAttribute("aSoftTotalTicekts")%></td>
										<td align="right" class="sale"><%=FormatNumber.formatNumberForJSP(pageContext
													.getAttribute("aSoftTotalSal"))%></td>
										<td align="right" class="winAmt" style="display: none;"><%=FormatNumber.formatNumberForJSP(pageContext
													.getAttribute("aSoftTotalWin"))%></td>
										<td align="right" class="claimedAmt" style="display: none;"><%=FormatNumber.formatNumberForJSP(pageContext
													.getAttribute("aSoftTotalClaim"))%></td>
									</s:if>
									<s:elseif test="%{#application.COUNTRY_DEPLOYED=='NIGERIA'}">
										<td align="right" class="noOfTkts" style="display: none;">	<%= pageContext
													.getAttribute("mtnTotalTicekts")%></td>
										<td align="right" class="sale"><%=FormatNumber.formatNumberForJSP(pageContext
													.getAttribute("mtnTotalSal"))%></td>
										<td align="right" class="winAmt" style="display: none;"><%=FormatNumber.formatNumberForJSP(pageContext
													.getAttribute("mtnTotalWin"))%></td>
										<td align="right" class="claimedAmt" style="display: none;"><%=FormatNumber.formatNumberForJSP(pageContext
													.getAttribute("mtnTotalClaim"))%></td>
									</s:elseif>
									<td align="right" class="noOfTkts" style="display: none;"><%=  pageContext
												.getAttribute("totalTicekts") %></td>
									<td align="right" class="sale">
									<%=FormatNumber.formatNumberForJSP(pageContext
												.getAttribute("totalSal"))%>
									</td>
									<td align="right" class="winAmt" style="display: none;"> 	<%=FormatNumber.formatNumberForJSP(pageContext
												.getAttribute("totalWin"))%></td>
									<td align="right" class="claimedAmt" style="display: none;"><%=FormatNumber.formatNumberForJSP(pageContext
												.getAttribute("totalClaim"))%></td>
				
						</tr>
					</s:if>
					<s:else>
						<tr>
						<s:if test="%{#application.COUNTRY_DEPLOYED=='ZIMBABWE'}">
							<td colspan="25" align="center">
								<s:text name="msg.no.record.process" />
							</td>
						</s:if>
						<s:elseif test="%{#application.COUNTRY_DEPLOYED=='NIGERIA'}">
							<td colspan="21" align="center">
								<s:text name="msg.no.record.process" />
							</td>
						</s:elseif>
						<s:else>
						    <td colspan="17" align="center">
								<s:text name="msg.no.record.process" />
							</td>
						</s:else>
						</tr>
					</s:else>
				</table>
			</div>
		</div>
	</body>
</html>
<code id="headId" style="visibility: hidden">
	$RCSfile: bo_rep_drawGameConsolidate_Result.jsp,v $
	$Revision: 1.3 $
</code>