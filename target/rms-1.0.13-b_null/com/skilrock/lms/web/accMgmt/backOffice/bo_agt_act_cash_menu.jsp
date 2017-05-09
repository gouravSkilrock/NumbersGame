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
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />

		<link rel="stylesheet"
			href="<%=request.getContextPath()%>/LMSImages/css/styles.css"
			type="text/css" />
		<link rel="stylesheet"
			href="<%=request.getContextPath()%>/com/skilrock/lms/web/userMgmt/backOffice/js/AutoComplete.css"
			media="screen" type="text/css" />
		<title><%=application.getAttribute("JSP_PAGE_TITLE")%></title>
		<script type="text/javascript"
			src="<%=request.getContextPath()%>/com/skilrock/lms/web/common/globalJs/jquery-1.10.1.min.js"></script>
		<script type="text/javascript"
			src="<%=request.getContextPath()%>/com/skilrock/lms/web/accMgmt/backOffice/js/rightClickDisable.js"></script>
		<script type="text/javascript"
			src="<%=request.getContextPath()%>/com/skilrock/lms/web/accMgmt/backOffice/js/ACT_ST5_Validation.js"></script>
		<script type="text/javascript"
			src="<%=request.getContextPath()%>/com/skilrock/lms/web/accMgmt/backOffice/js/digitToWord.js"></script>
		<s:head theme="ajax" debug="false" />
		<script type="text/javascript"
			src="<%=request.getContextPath()%>/com/skilrock/lms/web/userMgmt/backOffice/js/AutoComplete.js"></script>
	</head>

	<body>
		<%@include file="/com/skilrock/lms/web/loginMgmt/menu.jsp"%>
		<div id="wrap">
			<div id="top_form">
				<h3>
					<s:text name="label.payment.detail" />
				</h3>
				<s:form name="TwoWayPaymentForm">
					<s:actionerror />
					<s:token name="token" />
					<table cellpadding="2" cellspacing="0" border="0">
						<tr>
							<th colspan="4">
								<s:text name="label.two.way.pay.sys" />
							</th>
						</tr>
						<tr>
							<td align="right" class="label">
								<label class="label">
									<s:text name="label.trmnl.id" />
									<span class="required">*</span>:
								</label>
							</td>
							<td align="left">
								<input type="text" id="orgName" name="orgName" size="30"
									onfocus="hideSubmit();" />
							</td>
							<td align="left">
								<input type="button" id="validate" name="validate"
									value="<s:text name="btn.vldte" />" onclick="getLMCId();"/>
							</td>
							<td align="left">
								<div id="retId"></div>
							</td>
						</tr>
						<tr>
							<td align="right" class="label">
								<div id="lmctag" style="display: none;">
									<label class="label">
										<s:text name="label.lmc.id" />&nbsp;&nbsp;&nbsp;<s:text name="label.name" />&nbsp;&nbsp;&nbsp;
									</label>
								</div>
							</td>
							<td align="left" class="label">
								<div id="lmcId" style="display: none;">
								</div>
							</td>
							<td align="left">
								<s:hidden name="agtOrgId" id="agtOrgId"></s:hidden>
							</td>
							<td align="left">

							</td>
						</tr>
						<tr>
							<td align="right" class="label">
								<div id="rettag" style="display: none;">
									<label class="label">
										<s:text name="label.lmc.id" />&nbsp;&nbsp;&nbsp;<s:text name="label.name" />&nbsp;&nbsp;&nbsp;
									</label>
								</div>
							</td>
							<td align="left" class="label">
								<div id="retailerId" style="display: none;">
								</div>
							</td>
							<td align="left">
								<s:hidden name="retOrgId" id="retOrgId"></s:hidden>
							</td>
							<td align="left">

							</td>
						</tr>
						<tr>
							<td align="right" class="label">
								<label class="label">
									<s:text name="label.amount" />
									<span class="required">*</span>:
								</label>
							</td>
							<td align="left">
								<input type="text" id="amount" name="amount" size="10"
									maxlength="10" onkeydown="return onlyDecimalForNeg(event);"/>
							</td>
							<td align="left">

							</td>
							<td align="left">
								<div id="amt"></div>
							</td>
						</tr>
						<tr>
							<td align="right" class="label">
								<label class="label">
									<s:text name="label.vrfy.amount" />
									<span class="required">*</span>:
								</label>
							</td>
							<td align="left">
								<input type="text" id="amount1" name="amount1" size="10"
									maxlength="10" onkeydown="return onlyDecimalForNeg(event);" />
							</td>
							<td align="left">

							</td>
							<td align="left">
								<div id="amt1"></div>
							</td>
						</tr>
						<tr>
							<td>
								<s:submit theme="simple" key="btn.submit.pay" id="sb"
									action="bo_agt_act_cash_success"
									onclick="return validateTwoWayCashDep(this.id);"
									cssClass="button" cssStyle="display: none;" />
							</td>
							<td>
								<s:reset theme="simple" key="btn.reset" cssClass="button"
									onclick="return removeTwoWayDepMessages()" />
							</td>
							<td>

							</td>
						</tr>
					</table>
				</s:form>
			</div>
		</div>
	</body>
</html>

<code id="headId" style="visibility: hidden">
	$RCSfile: bo_agt_act_cash_menu.jsp,v $ $Revision: 1.3 $
</code>
