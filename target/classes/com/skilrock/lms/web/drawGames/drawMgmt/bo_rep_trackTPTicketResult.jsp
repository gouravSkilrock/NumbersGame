<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<%@page import="com.skilrock.lms.common.utility.FormatNumber"%>
	<head>
		<meta http-equiv="CACHE-CONTROL" content="NO-CACHE" />
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<link rel="stylesheet" href="<%=request.getContextPath()%>/LMSImages/css/styles.css"  type="text/css"/>
		<title><%=application.getAttribute("JSP_PAGE_TITLE")%></title>	
		<script type="text/javascript" src="<%=request.getContextPath()%>/com/skilrock/lms/web/drawGames/pwtMgmt/js/pwtTktValidation.js"></script>
		<s:head theme="ajax" debug="false"/>	
	</head>
	<body>
	      <div class="mainDiv" align="center">
	      <s:set name="newDrawstatus" value=""></s:set>
				<div id="wrap" class="wrapper_dv">
					<div id="top_form">
						<div align="center">
							<table cellspacing="0" cellpadding="3" bordercolor="#000000" border="1" width="100%" align="center" style="font-size: 11px">
								<tbody>
									<tr  style="background: #eee">
										<td colspan="7">
											<div align="center" >
											 	<font style="font-size: 14px"><b>Track Ticket Details</b></font>
											</div>
										</td>
									</tr>
									<tr  style="background: #eee">
										<th align="center" nowrap="nowrap">Ticket No</th>
										<th align="center" nowrap="nowrap">Game Name</th>
										<th align="center" nowrap="nowrap">Purchase Date</th>
										<th align="center" nowrap="nowrap">Merchant Name</th>
										<th align="center" nowrap="nowrap">User Id</th>
										<th align="center" nowrap="nowrap">Amount (<s:property value="%{#application.CURRENCY_SYMBOL}"/>)</th>
										<th align="center" nowrap="nowrap">No. Of Draw</th>
									</tr>					
									<tr>
										<td align="center" nowrap="nowrap"><s:property value="pwtVerifyBean.ticketNumber"/></td>
										<td align="center" nowrap="nowrap"><s:property value="pwtVerifyBean.gameName"/> </td>
										<td align="center" nowrap="nowrap"><s:property value="pwtVerifyBean.purchaseDateTime"/> </td>
										<td align="center" nowrap="nowrap"><b><s:property value="pwtVerifyBean.merchantName"/></b></td>
										<td align="center" nowrap="nowrap"><s:property value="pwtVerifyBean.userId"/> </td>
										<td align="center" nowrap="nowrap">
											<s:set name="amount" value="pwtVerifyBean.totalPurchaseAmt"/>
			  								<%=FormatNumber.formatNumberForJSP(pageContext.getAttribute("amount")) %>
										</td>
										<td align="center" nowrap="nowrap"><s:property value="pwtVerifyBean.noOfDraws"/> </td>
									</tr>
									
									<tr align="center"  style="background: #eee;font-size: 12px">
										<td colspan="7">
											<s:if test="%{pwtVerifyBean.ticketStatus=='CANCELLED'}">
												<span><b>Cancelled Ticket</b></span>
											</s:if>
											<s:else>
												<span><b>Ticket Winning Status</b></span>
											</s:else>
										</td>								
									</tr>
									<tr  style="background: #eee">
										<th align="center" nowrap="nowrap">Draw Name</th>
										<th align="center" nowrap="nowrap">Draw Date</th>
										<th align="center" nowrap="nowrap">Claim Time</th>
										<th align="center" nowrap="nowrap">Draw Status</th>
										<th align="center" nowrap="nowrap">Draw Result</th>
										<th align="center" nowrap="nowrap" colspan="2">Winning Details</th>
									</tr>
									<s:set name="drawDetail" value="pwtVerifyBean.verifyTicketDrawDataBeanList"></s:set>
									<s:iterator value="drawDetail">
										<tr>
											<td align="center" nowrap="nowrap">
												<s:if test="%{drawName=='null' || drawName==null}">
													N.A.
												</s:if>
												<s:else>
													<s:property value="drawName"/>
												</s:else>
											</td>
											<td align="center" nowrap="nowrap"><s:property value="drawDateTime"/></td>
											<td align="center" nowrap="nowrap">
												<s:if test="%{drawClaimTime=='null' || drawClaimTime==null}">
													N.A.
												</s:if>
												<s:else>
													<s:property value="drawClaimTime"/>
												</s:else>
											</td>
											<td align="center" nowrap="nowrap"><s:property value="drawStatus"/></td>
											<td align="center" nowrap="nowrap">
												<s:if test="%{drawStatus!='RESULT AWAITED'}">
													<s:property value="drawResult" escape="false"/>
												</s:if>
												<s:else>
													N.A.
												</s:else>
											</td>
													<td colspan="2">
														<table border="1" width="100%" bordercolor="#000000" align="center" cellspacing="0" cellpadding="3">
															<tr  style="background: #eee">
																<th align="center" nowrap="nowrap">Panel No</th>
																<th align="center" nowrap="nowrap">Play Type</th>
																<th align="center" nowrap="nowrap">Panel Data</th>
																<th align="center" nowrap="nowrap">No of Lines</th>
																<th align="center" nowrap="nowrap">Winning Amount</th>
																<th align="center" nowrap="nowrap">Claimed Status</th>
																<th align="center" nowrap="nowrap">Claimed At</th>
															</tr>
															<s:set name="boardDetails" value="boardTicketBeanList"></s:set>
															<s:iterator value="boardDetails">
																<s:set name="winAmt" value="panelWinAmt"></s:set>
																<tr>
																	<td align="center" nowrap="nowrap"><s:property value="boardId"/></td>
																	<td align="center" nowrap="nowrap"><s:property value="playType"/></td>
																	<td align="center" nowrap="nowrap"><s:property value="pickedData"/></td>
																	<td align="center" nowrap="nowrap"><s:property value="noOfLines"/></td>
																	<td align="center" nowrap="nowrap">
																		<%=FormatNumber.formatNumberForJSP(pageContext.getAttribute("winAmt"))%>
																	<td align="center" nowrap="nowrap"><s:property value="ticketStatus" escape="false" /></td>
																	<td align="center" nowrap="nowrap">
																	<s:if test="%{claimedAt=='null' || claimedAt==null}">
																			N.A.
																	</s:if>
																	<s:else>
																		<s:property value="claimedAt"/>
																	</s:else>
																	</td>
																</tr>
															</s:iterator>
															
														</table>
													</td>
										</tr>
									</s:iterator>
								</tbody>
								</table>
						</div>
						</div>
				</div>
	      </div>
	</body>
	
</html>
<code id="headId" style="visibility: hidden">
$RCSfile: bo_rep_trackTPTicketResult.jsp,v $
$Revision: 1.3 $
</code>