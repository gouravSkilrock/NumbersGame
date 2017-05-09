<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="java.util.*"%>
<%@page import="com.skilrock.lms.beans.TicketTracking"%>
<%@page import="com.skilrock.lms.beans.DrawIdBean"%>
<%@page import="com.skilrock.lms.beans.PanelIdBean"%>
<%@page import="com.skilrock.lms.web.drawGames.common.Util"%>
<%@page import="com.skilrock.lms.common.utility.FormatNumber"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%
	response.setDateHeader("Expires", 0);
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">

	<head>
		<s:head theme="ajax" debug="false" />
		<meta http-equiv="Content-Type"
			content="text/html; charset=iso-8859-1" />
		<link rel="stylesheet"
			href="<%=request.getContextPath()%>/LMSImages/css/styles.css"
			type="text/css" />
		<title><%=application.getAttribute("JSP_PAGE_TITLE")%></title>
		
	</head>

	<body>

		<div id="wrap">
			<div id="top_form">
				<%
				List<TicketTracking> trackingList= new ArrayList<TicketTracking>();
				trackingList=(List<TicketTracking>) session.getAttribute("TKT_WIN_STATUS");
					//<TicketTracking tktTrack = (TicketTracking) session
						//	.getAttribute("TKT_WIN_STATUS");
						TicketTracking tktTrack = null;
					if (trackingList != null && trackingList.size()>0) {
					for(int i=0;i<trackingList.size();i++){
					tktTrack = trackingList.get(i);     
					if(i>0){
                 if("REFERENCE".equalsIgnoreCase(tktTrack.getPromoTicketType())){
					%>
					<br />
					<b><big>Raffle Status :</big></b>
					<br />
					<%}
					else {
					 %>
					<b><big>Raffle Ticket Number :</big></b>
		
					<%
					out.println(tktTrack.getTktNumber() );   
					}
					}
						if (tktTrack.getStatus().equalsIgnoreCase("Invalid Ticket")) {
				%>
				<table width="100%" border="0" cellpadding="3" cellspacing="0"
					bordercolor="#CCCCCC" align="center">
					<tr>
						<td align="center" style="font-size: large; font-family: serif;">
							Invalid Ticket
						</td>
					</tr>
				</table>
				<%
					break;
					}
					
				%>
                 <% if("REFERENCE".equalsIgnoreCase(tktTrack.getPromoTicketType())){

                 %>
                <table width="100%" border="1" cellpadding="3" cellspacing="0"
						bordercolor="#CCCCCC" align="center">
						<tr>
							<th colspan="7" align="center">
								Ticket Winning Status
							</th>
						</tr>
						<tr>
							<th align="center">
								Draw Date
							</th>
							<th align="center">
								Result
							</th>
							<th align="center">
								Claim Time
							</th>
							<th align="center">
								Winning Details
							</th>
						</tr>


						<%
							int gameNo = tktTrack.getGameNo();

								List<DrawIdBean> drawWinList = tktTrack.getDrawWinList();
								HashMap<Integer, String> partyIdMap = tktTrack.getPartyId();
								List<PanelIdBean> panelWinList = null;
								String drawStatus = null;
								HashMap<Integer, String> pwtTimeMap = tktTrack.getPwtTimeMap();
								int panelSize = 1;
								for (DrawIdBean drawBean : drawWinList) {
									panelWinList = drawBean.getPanelWinList();
									panelSize = panelWinList.size();
									drawStatus = drawBean.getStatus();
								
						%>
						<tr class="data">
							<td align="center" nowrap="nowrap"><%=drawBean.getDrawDateTime()%></td>
							<%
								if (drawStatus.equals("CLAIM ALLOW")) {
							%>
							<td align="center" style="text-align:left">N.A.</td>
							<%
								} else if ("CANCEL".equalsIgnoreCase(drawStatus)) {
							%>
							<td align="center">
								DRAW CANCELLED
							</td>
							<%
								} else {
							%>
							<td align="center">
								RESULT AWAITED
							</td>
							<%
								}
							%>
							
						
							<td align="center"><%=pwtTimeMap.get(drawBean.getDrawId()) == null ? "N.A."
									: pwtTimeMap.get(drawBean.getDrawId())%></td>
							<td colspan="4">
							
							
							
							
								<table width="100%" border="1" cellpadding="3" cellspacing="0"
									bordercolor="#CCCCCC" align="center">
									<tr>
													
																			
										<%
											if (!tktTrack.getStatusCheck().equalsIgnoreCase(
															"Cancelled Ticket")) {
										%>
										<th>
											Claimed At
										</th>
										<th>
											Claimed Status
										</th>
										<th>
											Win Amount
										</th>
										<%
											}
										%>
									</tr>
								<!-- 	<tr class="data">
									<td>45</td>
									<td>allow</td>
									<td>100.0</td> 
									
									</tr> -->
									

									<tr class="data">
										
										<%PanelIdBean panelBean = new PanelIdBean();										
										for (int j = 0; j < panelWinList.size(); j++) {
													panelBean = panelWinList.get(j);
										 %>
										<%
											if (!tktTrack.getStatusCheck().equalsIgnoreCase(
																"Cancelled Ticket")) {
										%>
										<td align="left"><%=partyIdMap.get(panelBean
											.getPartyId()) == null ? "&nbsp;"
											: partyIdMap.get(panelBean
													.getPartyId())%></td>
										<td align="left"><%=drawStatus
											.equalsIgnoreCase("CLAIM HOLD") ? "N.A."
											: panelBean.getStatus()%></td>
										<td align="left">
											<%
												Object finalAmt;
																Object amt = drawStatus.equals("CLAIM ALLOW") ? panelBean
																		.getWinningAmt()
																		: "N.A.";
																if (amt instanceof Double) {
																	finalAmt = FormatNumber.formatNumberForJSP(amt);
																} else {
																	finalAmt = amt;
																}
											%>
											<%=finalAmt%>
											</td>
										<%
											}
										%>
									</tr>
									<%
							}
						%>

								</table>
							</td>
						</tr>
						<%
							}
						%>
					</table>
                  
                 <%
                  break;
                 } 
                 %>
				<table width="100%" border="1" cellpadding="3" cellspacing="0"
					bordercolor="#CCCCCC" align="center">
					<tr>
						<th colspan="6" align="center">
							Sale Details
						</th>
					</tr>
					<tr>
						<th>
							Sale Date
						</th>
						<th align="center">
							Game Name
						</th>
						<th align="center">
							Amount
						</th>
						<th align="center">
							<s:property value="#application.TIER_MAP.RETAILER" />
							Name
						</th>
						<th align="center">
							No. Of Draws
						</th>
						<th align="center">
							Sale Mode
						</th>
					</tr>
					<s:if test="%{#session.TKT_WIN_STATUS!=null}">
						<tr class="data">
							<td align="left">
							<%=tktTrack.getSaleDate() %>
							<!--  	<s:property value="#session.TKT_WIN_STATUS.saleDate" /> -->
							</td>
							<td align="center">
								<%=tktTrack.getGameName() %>
							<!-- 	<s:property value="#session.TKT_WIN_STATUS.gameName" /> -->
							</td>
							<td align="center">
								<%=tktTrack.getSaleAmt() %>
							<!-- 	<s:property value="#session.TKT_WIN_STATUS.saleAmt" /> -->
							</td>
							<td align="center">
							  <%=tktTrack.getRetailerName() %>
							<!-- 	<s:property value="#session.TKT_WIN_STATUS.retailerName" /> -->
							</td>
							<td align="center">
							   <%=tktTrack.getNoOfDraws() %>
							<!-- 	<s:property value="#session.TKT_WIN_STATUS.noOfDraws" /> -->
							</td>
							<td align="center">					
							 	<%=tktTrack.getSaleMode() %>
							<!-- 	<s:property value="#session.TKT_WIN_STATUS.saleMode" /> -->
							</td>
						</tr>
					</s:if>
					<s:else>
						<tr>
							<td colspan="4" align="center">
								No Records to Process
							</td>
						</tr>
					</s:else>

				</table>
				
				
				
				<s:if test="%{#session.TKT_WIN_STATUS!=null}">

					<%
						//tktTrack = (TicketTracking) session.getAttribute("TKT_WIN_STATUS");
							if (tktTrack.getStatusCheck().equalsIgnoreCase(
									"Cancelled Ticket")) {
					%>
					<table width="100%" border="1" cellpadding="3" cellspacing="0"
						bordercolor="#CCCCCC" align="center">
						<tr>
							<td align="center" style="font-size: large; font-family: serif">
								Cancelled Ticket
							</td>
						</tr>
					</table>
					<%
						//return;
							} else if (tktTrack.getStatusCheck().contains(
									"Reprinted Ticket")) {
					%>
					
					
					<table width="100%" border="1" cellpadding="3" cellspacing="0"
						bordercolor="#CCCCCC" align="center">
						<tr>
							<td align="center" style="font-size: large; font-family: serif">
								Ticket has Been Reprinted
								<%=tktTrack.getStatusCheck().replace(
							"Reprinted Ticket", "")%>
								time(s).
							</td>
						</tr>
					</table>
					<%
						//return;
							}
					%>



<%
						
					 if (tktTrack.getIsRaffle()==true && "ORIGINAL".equalsIgnoreCase(tktTrack.getPromoTicketType()))
					 {
					%>


<table width="100%" border="1" cellpadding="3" cellspacing="0"
						bordercolor="#CCCCCC" align="center">
						<tr>
							<th colspan="7" align="center">
								Ticket Winning Status
							</th>
						</tr>
						<tr>
							<th align="center">
								Draw Date
							</th>
							<th align="center">
								Result
							</th>
							<th align="center">
								Claim Time
							</th>
							<th align="center">
								Winning Details
							</th>
						</tr>


						<%
							int gameNo = tktTrack.getGameNo();

								List<DrawIdBean> drawWinList = tktTrack.getDrawWinList();
								HashMap<Integer, String> partyIdMap = tktTrack.getPartyId();
								List<PanelIdBean> panelWinList = null;
								String drawStatus = null;
								HashMap<Integer, String> pwtTimeMap = tktTrack.getPwtTimeMap();
								int panelSize = 1;
								for (DrawIdBean drawBean : drawWinList) {
									panelWinList = drawBean.getPanelWinList();
									panelSize = panelWinList.size();
									drawStatus = drawBean.getStatus();
								
						%>
						<tr class="data">
							<td align="center" nowrap="nowrap"><%=drawBean.getDrawDateTime()%></td>
							<%
								if (drawStatus.equals("CLAIM ALLOW")) {
							%>
							<td align="center" style="text-align:left"><%=drawBean.getWinResult()%></td>
							<%
								} else if ("CANCEL".equalsIgnoreCase(drawStatus)) {
							%>
							<td align="center">
								DRAW CANCELLED
							</td>
							<%
								} else {
							%>
							<td align="center">
								RESULT AWAITED
							</td>
							<%
								}
							%>
							
						
							<td align="center"><%=pwtTimeMap.get(drawBean.getDrawId()) == null ? "N.A."
									: pwtTimeMap.get(drawBean.getDrawId())%></td>
							<td colspan="4">
							
							
							
							
								<table width="100%" border="1" cellpadding="3" cellspacing="0"
									bordercolor="#CCCCCC" align="center">
									<tr>
													
																			
										<%
											if (!tktTrack.getStatusCheck().equalsIgnoreCase(
															"Cancelled Ticket")) {
										%>
										<th>
											Claimed At
										</th>
										<th>
											Claimed Status
										</th>
										<th>
											Win Amount
										</th>
										<%
											}
										%>
									</tr>
								<!-- 	<tr class="data">
									<td>45</td>
									<td>allow</td>
									<td>100.0</td> 
									
									</tr> -->
									

									<tr class="data">
										
										<%PanelIdBean panelBean = new PanelIdBean();										
										for (int j = 0; j < panelWinList.size(); j++) {
													panelBean = panelWinList.get(j);
										 %>
										<%
											if (!tktTrack.getStatusCheck().equalsIgnoreCase(
																"Cancelled Ticket")) {
										%>
										<td align="left"><%=partyIdMap.get(panelBean
											.getPartyId()) == null ? "&nbsp;"
											: partyIdMap.get(panelBean
													.getPartyId())%></td>
										<td align="left"><%=drawStatus
											.equalsIgnoreCase("CLAIM HOLD") ? "N.A."
											: panelBean.getStatus()%></td>
										<td align="left">
											<%
												Object finalAmt;
																Object amt = drawStatus.equals("CLAIM ALLOW") ? panelBean
																		.getWinningAmt()
																		: "N.A.";
																if (amt instanceof Double) {
																	finalAmt = FormatNumber.formatNumberForJSP(amt);
																} else {
																	finalAmt = amt;
																}
											%>
											<%=finalAmt%>
											</td>
										<%
											}
										%>
									</tr>
									<%
							}
						%>

								</table>
							</td>
						</tr>
						<%
							}
						%>
					</table>
                  
                  


<%
						
							} else  {
					%>



 


					<table width="100%" border="1" cellpadding="3" cellspacing="0"
						bordercolor="#CCCCCC" align="center">
						<tr>
							<th colspan="7" align="center">
								Ticket Winning Status
							</th>
						</tr>
						<tr>
							<th align="center">
								Draw Date
							</th>
							<th align="center">
								Result
							</th>
							<th align="center">
								Claim Time
							</th>
							<th align="center">
								Winning Details
							</th>
						</tr>


						<%
							int gameNo = tktTrack.getGameNo();

								List<DrawIdBean> drawWinList = tktTrack.getDrawWinList();
								HashMap<Integer, String> partyIdMap = tktTrack.getPartyId();
								List<PanelIdBean> panelWinList = null;
								String drawStatus = null;
								HashMap<Integer, String> pwtTimeMap = tktTrack.getPwtTimeMap();
								int panelSize = 1;
								for (DrawIdBean drawBean : drawWinList) {
									panelWinList = drawBean.getPanelWinList();
									panelSize = panelWinList.size();
									drawStatus = drawBean.getStatus();
						%>
						<tr class="data">
							<td align="center" nowrap="nowrap"><%=drawBean.getDrawDateTime()%></td>
							<%
								if (drawStatus.equals("CLAIM ALLOW")) {
							%>
							<td align="center"><%=drawBean.getWinResult()%></td>
							<%
								} else if ("CANCEL".equalsIgnoreCase(drawStatus)) {
							%>
							<td align="center">
								DRAW CANCELLED
							</td>
							<%
								} else {
							%>
							<td align="center">
								RESULT AWAITED
							</td>
							<%
								}
							%>
							<td align="center"><%=pwtTimeMap.get(drawBean.getDrawId()) == null ? "N.A."
									: pwtTimeMap.get(drawBean.getDrawId())%></td>
							<td colspan="4">
								<table width="100%" border="1" cellpadding="3" cellspacing="0"
									bordercolor="#CCCCCC" align="center">
									<tr>
										<th>
											Panel No.
										</th>
										<th>
											Panel Data
										</th>
										<%
											if ("Keno".equalsIgnoreCase(Util.getGameType(gameNo))
															|| "Zimlottotwo".equalsIgnoreCase(Util
																	.getGameName(gameNo))
															|| "Zimlottothree".equalsIgnoreCase(Util
																	.getGameName(gameNo))
																	|| "bonusballtwo".equalsIgnoreCase(Util
																	.getGameName(gameNo))
															|| "SuperTwo".equalsIgnoreCase(Util
																	.getGameName(gameNo))) {
										%>
										<th>
											Play Type
										</th>
										<th>
											No. Of Lines
										</th>
										<%
											}
										%>
										<%
											if (!tktTrack.getStatusCheck().equalsIgnoreCase(
															"Cancelled Ticket")) {
										%>
										<th>
											Claimed At
										</th>
										<th>
											Claimed Status
										</th>
										<th>
											Win Amount
										</th>
										<%
											}
										%>
									</tr>
									<%
										PanelIdBean panelBean = null;
												for (int k = 0; k < panelWinList.size(); k++) {
													panelBean = panelWinList.get(k);
													String pickData = panelBean.getPickedData();
									%>

									<tr class="data">
										<td align="left"><%=panelBean.getPanelId()%></td>
										<td align="left"><%=panelBean.getPickedData().replace("-1",
										"XX")%></td>
										<%
											if ("Keno".equalsIgnoreCase(Util.getGameType(gameNo))
																|| "Zimlottotwo".equalsIgnoreCase(Util
																		.getGameName(gameNo))
																|| "Zimlottothree".equalsIgnoreCase(Util
																		.getGameName(gameNo))
																		|| "bonusballtwo".equalsIgnoreCase(Util
																		.getGameName(gameNo))
																|| "SuperTwo".equalsIgnoreCase(Util
																		.getGameName(gameNo))) {
										%>
										<td><%=panelBean.getPlayType()%></td>
										<td><%=panelBean.getLineId()%></td>
										<%
											}
										%>
										<%
											if (!tktTrack.getStatusCheck().equalsIgnoreCase(
																"Cancelled Ticket")) {
										%>
										<td align="left"><%=partyIdMap.get(panelBean
											.getPartyId()) == null ? "&nbsp;"
											: partyIdMap.get(panelBean
													.getPartyId())%></td>
										<td align="left"><%=drawStatus
											.equalsIgnoreCase("CLAIM HOLD") ? "N.A."
											: panelBean.getStatus()%></td>
										<td align="left">
											<%
												Object finalAmt;
																Object amt = drawStatus.equals("CLAIM ALLOW") ? panelBean
																		.getWinningAmt()
																		: "N.A.";
																if (amt instanceof Double) {
																	finalAmt = FormatNumber.formatNumberForJSP(amt);
																} else {
																	finalAmt = amt;
																}
											%><%=finalAmt%></td>
										<%
											}
										%>
									</tr>
									<%
										}
									%>

								</table>
							</td>
						</tr>
						<%
							}
						%>
					</table>
					
					
					
					
<%
						
							} 
					%>
					
					
					
					
					
					
					
					
					
					
					
					
					
					
				</s:if>
				
				
				<%
				} 
				%>
				<%
				}
				 %>
				
				
				

			</div>
		</div>



	</body>
</html>

<code id="headId" style="visibility: hidden">
	$RCSfile: bo_rep_trackTransactionTicketResult.jsp,v $ $Revision: 1.6.2.1.4.7.2.14
	$
</code>