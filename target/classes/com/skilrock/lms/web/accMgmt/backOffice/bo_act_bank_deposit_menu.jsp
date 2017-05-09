<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="com.skilrock.lms.common.utility.FormatNumber"%>
<%@page import="java.util.Calendar"%>
<%@page import="com.skilrock.lms.common.utility.CommonMethods"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">

	<%
		response.setHeader("Cache-Control", "no-store"); //HTTP 1.1
		response.setHeader("Pragma", "no-cache"); //HTTP 1.0
		response.setDateHeader("Expires", 0); //prevents caching at the proxy server
	%>
	<head>
		<link type="text/css" rel="stylesheet"
			href="<%=request.getContextPath()%>/LMSImages/css/lmsCalendar.css"
			media="screen" />
		<script>var projectName="<%=request.getContextPath()%>"</script>
		<script>var path = "<%=request.getContextPath()%>"</script>
		<script type="text/javascript"
			src="<%=request.getContextPath()%>/com/skilrock/lms/web/common/globalJs/calender.js"></script>

		<META HTTP-EQUIV="CACHE-CONTROL" CONTENT="NO-CACHE" />

		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />

		<link rel="stylesheet"
			href="<%=request.getContextPath()%>/LMSImages/css/styles.css"
			type="text/css" />

		<title><%=application.getAttribute("JSP_PAGE_TITLE")%></title>
		<script type="text/javascript"
			src="<%=request.getContextPath()%>/com/skilrock/lms/web/accMgmt/backOffice/js/rightClickDisable.js"></script>
		<script type="text/javascript"
			src="<%=request.getContextPath()%>/com/skilrock/lms/web/accMgmt/backOffice/js/ACT_ST5_Validation.js"></script>
		<script type="text/javascript"
			src="<%=request.getContextPath()%>/com/skilrock/lms/web/accMgmt/backOffice/js/digitToWord.js"></script>
		<s:head theme="ajax" debug="false" />
	</head>

	<body onload="getAgentList();">
		<%@include file="/com/skilrock/lms/web/loginMgmt/menu.jsp"%>
		<div id="wrap">
			<div id="top_form">
				<div id="limitDiv" style="align: center;"></div>
				<h3>
					<s:text name="label.payment.detail"/>
				</h3>
				<div id="divFrm" style="display: block">
					<s:form name="Bank_Deposit_form">
						<s:actionerror />
						<s:token></s:token>
						<table cellpadding="2" cellspacing="0" border="0" width="450">
							<tr>
								<td valign="top" align="left">

									<div id="cashInput">
										<table border="0" cellpadding="2" cellspacing="0" width="100%">
											<tr>
												<th>
													<s:text name="label.bank.depo.pay"/>
												</th>
											</tr>
											<s:hidden name="paymentType" value="BANK_DEPOSIT" />
											<s:if test="%{#application.IS_BANK_DEPOISIT_ENABLE_FOR_RETAILER=='YES'}">
												<tr align="left">
													<td>
														<s:select cssClass="option" required="true"
															key="label.org.type" id="orgType" name="orgType"
															list="#{'RETAILER':'RETAILER'}"
															onchange="hideAndShow();"></s:select>
													</td>
												</tr>
											</s:if>
											<s:else>
												<tr align="left">
													<td>
														<s:select cssClass="option" required="true"
															key="label.org.type" id="orgType" name="orgType"
															list="#{'AGENT':#application.TIER_MAP.AGENT.toUpperCase()}"
															onchange="hideAndShow();"></s:select>
													</td>
												</tr>
											</s:else>
											<tr>
												<td>
													<div id="orgName1">
													</div>
													<s:hidden name="agentNameValue" id="agentNameValue"></s:hidden>
												</td>
											</tr>
												<tr align="left">
													<td>
														<s:select cssClass="option" headerKey="-1" required="true"
														headerValue="%{getText('label.please.select')}"
														key="label.agt.name" name="orgName" id="orgName" list="{}"
														onchange="getRetList();verifyAgentBD(this.value);">
														</s:select>
													</td>
												</tr>
											<tr>
												<td>
													<div id="retOrgName1">
													</div>
												</td>
											</tr>
											<s:if test="%{#application.IS_BANK_DEPOISIT_ENABLE_FOR_RETAILER=='YES'}">
												<tr id="retName">
											</s:if>
											<s:else>
												<tr id="retName" style="display: none;">
											</s:else>									
												<td style="text-align: right;">
													<s:text name="label.ret.name"></s:text><span class="required">*</span>
												</td>
												<td>
													<table cellpadding="0" cellspacing="0" width="100%">
														<tbody>
												<tr>
																<td>
																	<s:select id="retOrgName" required="true"
																		cssClass="option" list="{}" name="retOrgName"
																		headerKey="-1"
																		headerValue="%{getText('label.please.select')}"
																		onchange="varifyRetailer(this.value)" />
																</td>
															</tr>
														</tbody>
													</table>
													</td>
												</tr>
											<tr>
												<td>
													<div id=receiptNum1>
													</div>
												</td>
											</tr>
											<tr align="left">
												<td>
													<s:textfield key="label.bnk.depo.rec.code" required="true"
														name="receiptNum" id="receiptNum" size="20" maxlength="20"
														value="" onkeypress="return onlyNum(this)"></s:textfield>
												</td>
											</tr>
											<tr>
												<td>
													<div id=bankName1>
													</div>
												</td>
											</tr>
											<tr align="left">
												<td>
													<!--<s:select
														list="{'Diamond Bank','Zenith Bank'}"
														key="label.bnk.name" name="bankName" id="bankName" cssClass="option" />-->
														
														<s:select list="bankMap"  key="label.bnk.name" name="bankName" id="bankName" cssClass="option"/>
												</td>
											</tr>
											<tr>
												<td>
													<div id=branchName1>
													</div>
												</td>
											</tr>
											<tr align="left">
												<td>
													<s:textfield key="label.bank.br.name" required="false"
														name="branchName" id="branchName" size="20" maxlength="20"
														value=""></s:textfield>
												</td>
											</tr>
											<tr>
												<td>
													<div id="totalAmount1">
													</div>
												</td>
											</tr>
											<tr align="left">
												<td>
													<s:textfield key="label.depo.amt" required="true"
														name="totalAmount" id="totalAmount" size="15"
														maxlength="09" value=""></s:textfield>
												</td>
											</tr>
											<tr>
												<td>
													<div id="cashAmnt1">
													</div>
												</td>
											</tr>
											<tr>
												<td align="left">
													<s:textfield key="label.verify.cash.amt" required="true"
														name="cashAmnt" id="cashAmnt" size="15" maxlength="09"></s:textfield>
												</td>
											</tr>
											<%
												Calendar prevCal = Calendar.getInstance();
													String startDate = CommonMethods
															.convertDateInGlobalFormat(new java.sql.Date(prevCal
																	.getTimeInMillis()).toString(), "yyyy-mm-dd",
																	"yyyy-mm-dd");
													String calStartDate = CommonMethods.convertDateInGlobalFormat(
															(String) application.getAttribute("DEPLOYMENT_DATE"),
															"dd-mm-yyyy", (String) application
																	.getAttribute("date_format"));
											%>
											<tr>
												<td>
													<div >
													</div>
												</td>
											</tr>
											<tr align="right">
												<td>
													<label class="label">
														<s:text name="label.depo.date"/>
														<span class="required">*</span>:
													</label>
													</td>
													<td align="left">
													<input type="text" name="depositDate" id="deposit_date"
														value="<%=startDate%>" readonly size="12" />
													<input type="button"
														style=" width:19px; height: 19px; background: url('<%=request.getContextPath()%>/LMSImages/imagesCal/dateIcon.gif'); top left; border:0 ; "
														onclick="displayCalendar(document.getElementById('deposit_date'),'yyyy-mm-dd', this, '<%=startDate%>',false, '<%=startDate%>')" />
												</td>
											</tr>
										</table>


									</div>
								</td>
							</tr>
							<tr>
								<td>
									<div id="finalSubmit">
										<table border="0" cellpadding="2" cellspacing="0" width="100%">
											<tr>

												<td colspan="5" align="center" style="padding-left: 110px;">
													<div id="subCheck"></div>
													<s:submit theme="simple" key="btn.submit.pay" id="sb"
														action="bo_act_bank_deposit_save"
														onclick="return displayPaymentDetailsForBD(this.id);"
														cssClass="button" />

													<s:reset theme="simple" key="btn.reset" cssClass="button"
														onclick="return removeMessagesBD()" />
												</td>
											</tr>

										</table>
									</div>
								</td>
							</tr>
						</table>
						<s:div id="bottom">
						</s:div>

					</s:form>
				</div>
				<div id="orgData" style="display: none">
					<table cellpadding="2" cellspacing="0" border="0" width="450">
						<tr>
							<td valign="top" align="center">
								<s:text name="label.no.org.reg"/>
							</td>
						</tr>
					</table>
				</div>
			</div>
		</div>
	</body>

</html>

<code id="headId" style="visibility: hidden">
	$RCSfile: bo_act_bank_deposit_menu.jsp,v $ $Revision: 1.3 $
</code>