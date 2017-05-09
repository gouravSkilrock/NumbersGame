<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
	<head>
		<meta http-equiv="CACHE-CONTROL" content="NO-CACHE" />
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<link rel="stylesheet" href="<%=request.getContextPath() %>/LMSImages/css/styles.css"  type="text/css"/>
		<title><%=application.getAttribute("JSP_PAGE_TITLE")%></title>	
		<script type="text/javascript" src="<%=request.getContextPath() %>/com/skilrock/lms/web/drawGames/pwtMgmt/js/pwtTktValidation.js"></script>
		<s:head theme="ajax" debug="false"/>	
	</head>
	<body>
	      <div class="mainDiv" align="center">
	      <s:set name="newDrawstatus" value=""></s:set>
				<div id="wrap">
					<div id="top_form">
						<div align="center">
							<table cellspacing="0" cellpadding="3" bordercolor="#000000" border="1" width="100%" align="center" style="font-size:11px">
								<tbody>
									<tr  style="background: #eee">
										<td colspan="6">
											<div align="center" >
											 	<font style="font-size:14px "><b>Ticket Info</b></font>
											</div>
										</td>
									</tr>
									<tr  style="background: #eee">
										<th align="center" nowrap="nowrap">Ticket No</th>
										<th align="center" nowrap="nowrap">Purchase Date</th>
										<th align="center" nowrap="nowrap">Game Name</th>
										<th align="center" nowrap="nowrap">User Id</th>
										<th align="center" nowrap="nowrap">Merchant Name</th>
										<th align="center" nowrap="nowrap">Amount (<s:property value="%{#application.CURRENCY_SYMBOL}"/>)</th>
									</tr>					
									<tr>
										<td align="center" nowrap="nowrap"><s:property value="pwtVerifyBean.ticketNumber"/></td>
										<td align="center" nowrap="nowrap"><s:property value="pwtVerifyBean.purchaseDateTime"/> </td>
										<td align="center" nowrap="nowrap"><s:property value="pwtVerifyBean.gameName"/> </td>
										<td align="center" nowrap="nowrap"><s:property value="pwtVerifyBean.phoneNumber"/> </td>
										<td align="center" nowrap="nowrap" style="font-size: 12px"><b><s:property value="pwtVerifyBean.merchantName"/></b></td>
										<td align="center" nowrap="nowrap"><s:property value="pwtVerifyBean.totalPurchaseAmt"/> </td>
									</tr>
									
									<tr align="center"  style="background: #eee;font-size:12px">
										<td colspan="6">
											<span><b>Ticket Winning Status</b></span>
										</td>								
									</tr>
									<tr  style="background: #eee">
										<th align="center" nowrap="nowrap">Draw Name</th>
										<th align="center" nowrap="nowrap">Draw Date</th>
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
										

											<td align="center" nowrap="nowrap"><s:property value="drawStatus"/></td>
											<td align="center" nowrap="nowrap">
												<s:if test="%{drawStatus!='RESULT AWAITED'}">
														<s:if test="%{drawResult=='null' || drawResult==null}">
															N.A.
														</s:if>
														<s:else>
															<s:property value="drawResult"/>
														</s:else>
												</s:if>
												<s:else>
													N.A.
												</s:else>
											</td>
											<s:set name="newDrawstatus" value="%{drawStatus}"></s:set>
										
											<s:if test="%{drawStatus=='RESULT AWAITED'}">
												<td colspan="2" align="center">Winning is not declared!</td>
											</s:if>
											<s:elseif test="%{drawStatus=='DRAW CANCELLED'}">
												<td colspan="2" align="center">Draw is Cancelled!</td>
											</s:elseif>
											<s:else>
													<td colspan="2">
														<table border="1" width="100%" bordercolor="#000000" align="center" cellspacing="0" cellpadding="3">
															<tr  style="background: #eee">
																<th align="center" nowrap="nowrap">Panel No</th>
																<th align="center" nowrap="nowrap">No of Lines</th>
																<th align="center" nowrap="nowrap">Winning Amount</th>
																<th align="center" nowrap="nowrap">Claimed Status</th>
															</tr>
															<s:set name="boardDetails" value="boardTicketBeanList"></s:set>
															<s:iterator value="boardDetails">
																<tr>
																	<td align="center" nowrap="nowrap"><s:property value="boardId"/></td>
																	<td align="center" nowrap="nowrap"><s:property value="noOfLines"/></td>
																	<td align="center" nowrap="nowrap"><s:property value="winningAmt"/></td>
																	<td align="center" nowrap="nowrap"><s:property value="status"/></td>
																</tr>
															</s:iterator>
															
														</table>
													</td>
											</s:else>
										</tr>
										<tr align="center"  style="background: #eee">
										<td colspan="6"><br></td>

										</tr>
										<br>
										<tr style="font-size: 14px">
												<td colspan="5" align="center" style="background: #eee"><b>Total Amount To be Paid</b></td>
												<td align="center" nowrap="nowrap"><b><s:property value="pwtVerifyBean.totalWinAmt"/></b></td>
										</tr>
									</s:iterator>
								</tbody>
								</table>
								<br>
								<s:form action="payDirectPwtProcess.action">
									<div class="pay-pwt_form"> 								
											<s:if test="%{pwtVerifyBean.totalWinAmt > 0.0 && #newDrawstatus != 'DRAW EXPIRED'}">
												<s:if test="%{pwtVerifyBean.merchantCode == #application.MERCHANT_LIST_FOR_PWT_VERIFICATION}">
													<div id="verErrorMsg"></div>
													<div class="ver-code-text">		
														<s:textfield  size="14" name="verificationCode" id="verificationCode" key="Verification Code" maxlength="7"/>
														<s:submit formId="form" id="subTransDraw" key="Proceed To Pay" align="center"	cssClass="button" />
													</div>
												  </s:if>
												  <s:else>
														<div>
															<s:submit formId="form" id="subTransDraw" key="Proceed To Pay" align="center" cssClass="button"  />
														</div>	
												   </s:else>
											</s:if>
											<s:else>
												<s:submit formId="form" id="subTransDraw" key="Proceed To Pay" align="center" cssClass="btn btn-default" disabled="true"  />
											</s:else>
									</div>
								 </s:form>
						</div>
						</div>
				</div>
	      </div>
	</body>
	
</html>
<code id="headId" style="visibility: hidden">
$RCSfile: bo_merchant_pwt_dirplr_success.jsp,v $
$Revision: 1.3 $
</code>