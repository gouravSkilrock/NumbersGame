<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="com.skilrock.lms.common.utility.FormatNumber"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">

	<%
		response.setHeader("Cache-Control", "no-store"); //HTTP 1.1
		response.setHeader("Pragma", "no-cache"); //HTTP 1.0
		response.setDateHeader("Expires", 0); //prevents caching at the proxy server
	%>
	<head>

		<META HTTP-EQUIV="CACHE-CONTROL" CONTENT="NO-CACHE" />
		<meta http-equiv="Content-Type"
			content="text/html; charset=utf-8" />
		<link rel="stylesheet"
			href="<%=request.getContextPath()%>/LMSImages/css/styles.css"
			type="text/css" />
		<title><%=application.getAttribute("JSP_PAGE_TITLE")%></title>
		<script type="text/javascript"
			src="<%=request.getContextPath()%>/com/skilrock/lms/web/accMgmt/agent/js/agt_accMgmt_validation.js" />
		<script type="text/javascript"
			src="<%=request.getContextPath()%>/com/skilrock/lms/web/accMgmt/agent/js/agt_accMgmt_rightClickDisable.js" />
		<script type="text/javascript"
			src="<%=request.getContextPath()%>/com/skilrock/lms/web/accMgmt/agent/js/agt_accMgmt_digitToWord.js"></script>
		<link type="text/css" rel="stylesheet"
			href="<%=request.getContextPath()%>/LMSImages/css/lmsCalendar.css"
			media="screen" />
		<script>var projectName="<%=request.getContextPath()%>"</script>
        <script type="text/javascript"
			src="<%=request.getContextPath()%>/com/skilrock/lms/web/common/globalJs/calender.js"></script>
		<script type="text/javascript"
			src="<%=request.getContextPath()%>/com/skilrock/lms/web/common/globalJs/confirmAgent.js"></script>
		<s:head theme="ajax" debug="false" />
	</head>

	<body>
		<%@include file="/com/skilrock/lms/web/loginMgmt/menu.jsp"%>

		<div id="wrap">

			<div id="top_form">
			<div id = "limitDiv" style="align:center;"></div>
				<h3>
					<s:text name="label.payment.detail"/>
				</h3>
				<s:form name="CashForm" method="post">
					<s:token />
					<s:actionerror />
					<table cellpadding="2" cellspacing="0" border="0" width="450">
						<tr>
							<td valign="top" align="left">

								<div id="cashInput">

									<table cellpadding="0" cellspacing="2" border="0" width="100%">
										<tr>
											<th>
												<s:text name="label.cash.pay"/>
											</th>
										</tr>
										<s:hidden name="paymentType" value="CASH" />
										<tr>
											<td align="center" colspan="2">
												<div id="orgName1">
												</div>
											</td>
										</tr>
											<s:hidden name="orgNameValue" id="orgNameValue"></s:hidden>
										
											<tr align="left">
												<td>
													<div id="retlistDiv">
													
													<s:select headerKey="-1" headerValue="%{getText('label.please.select')}"
														key="label.org.name" name="orgId" id="orgName"
														list="%{retailerInfoMap}" cssClass="option"
														onchange="verifyRetailerWithOutstandingBalance(this.value,'orgName','totalAmount');"></s:select>
														
								
													</div>
												</td>
											</tr>
									

										<tr>
											<td align="center" colspan="2">
												<div id="totalAmount1">
												</div>
											</td>
										</tr>
										<tr align="left">
											<td>
											  
												<s:textfield key="label.cash.amt" required="true"
													name="totalAmount" id="totalAmount" size="15"
													maxlength="09" value=""></s:textfield>
											</td>
										</tr>
										<tr>
											<td align="center" colspan="2">
												<div id="cashAmnt1">
												</div>
											</td>
										</tr>
										<tr align="left">
											<td>
												<s:textfield key="label.verify.cash.amt" required="true"
													name="cashAmnt" id="cashAmnt" size="15" maxlength="09" value=""></s:textfield>
											</td>

										</tr>
									</table>
								</div>

							</td>
						</tr>
						<tr>
							<td>





								<div id="finalSubmit">
									<table cellpadding="0" cellspacing="2" border="0" width="100%">
										<tr>

											<td align="center">
												<div id="subCheck" style="position: absolute;"></div>
												<s:submit theme="simple" key="btn.submit.cash" id="sb"
													action="agt_act_cash_Save"
													onclick="return displayPaymentDetails(this.id) "
													cssClass="button" />

												<s:reset theme="simple" key="btn.reset" cssClass="button"
													onclick="return removeMessages()" />
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
		</div>
	</body>
</html>

<code id="headId" style="visibility: hidden">
$RCSfile: agt_act_cash_Menu.jsp,v $
$Revision: 1.3 $
</code>