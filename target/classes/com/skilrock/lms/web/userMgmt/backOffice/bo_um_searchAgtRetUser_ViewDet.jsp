<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@page import="java.text.NumberFormat"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">

	<head>



		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />

		<link rel="stylesheet"
			href="<%=request.getContextPath()%>/LMSImages/css/styles.css"
			type="text/css" />

		<title><%=application.getAttribute("JSP_PAGE_TITLE")%></title>



		<script type="text/javascript"
			src="<%=request.getContextPath()%>/com/skilrock/lms/web/common/globalJs/jquery-1.10.1.min.js"></script>
		<script>
	$(document).ready(function() {
		if ($("#Status").val() == "TERMINATE") {
			$(".hideButton").hide();
		}
	});
</script>
		<s:head theme="ajax" debug="false" />
	</head>

	<body>
		<%@include file="/com/skilrock/lms/web/loginMgmt/menu.jsp"%>

		<div id="wrap">

			<div id="top_form">
				<h3>
					<s:text name="label.org.detail" />
				</h3>


				<s:form action="bo_um_searchAgtRetUser_Edit">

					<s:actionerror />
					<s:fielderror>
					</s:fielderror>
					<table border="0" cellpadding="2" cellspacing="0" width="450">
						<s:set name="creditLimit"
							value="%{#session.USER_SEARCH_RESULTS.orgCreditLimit}"></s:set>
						<%!NumberFormat format = NumberFormat.getInstance();%>

						<%
							format.setMaximumFractionDigits(2);
								Double dobl = (Double) pageContext.getAttribute("creditLimit");
								String creditLimit = format.format(dobl);
								//creditLimit=creditLimit.replace(",","");
								session.setAttribute("CREDIT_LIMIT", creditLimit);
						%>



						<s:textfield key="label.org.name" name="orgName" readonly="true"
							value="%{#session.USER_SEARCH_RESULTS.orgName}" />
						<s:hidden theme="simple" name="orgType"
							value="%{#session.USER_SEARCH_RESULTS.orgType}" />
						<s:textfield key="label.org.type" readonly="true"
							value="%{#application.TIER_MAP[#session.USER_SEARCH_RESULTS.orgType].toUpperCase()}" />
						<s:textfield key="label.adrs.line1" name="orgAddr1"
							readonly="true" value="%{#session.USER_SEARCH_RESULTS.orgAddr1}" />
						<s:textfield key="label.adrs.line2" name="orgAddr2"
							readonly="true" value="%{#session.USER_SEARCH_RESULTS.orgAddr2}" />
						<s:textfield key="label.city" name="orgCity" readonly="true"
							value="%{#session.USER_SEARCH_RESULTS.orgCity}" />
						<s:textfield key="label.country" name="orgCountry" readonly="true"
							value="%{#session.USER_SEARCH_RESULTS.orgCountry}" />
						<s:textfield key="label.state" name="orgState" readonly="true"
							value="%{#session.USER_SEARCH_RESULTS.orgState}" />
						<s:hidden theme="simple" name="orgPin"
							value="%{#session.USER_SEARCH_RESULTS.orgPin}"></s:hidden>
						<!-- <s:textfield label="Pin" name="orgPin" readonly="true" value="%{#session.USER_SEARCH_RESULTS.orgPin}"/> -->
						<s:textfield key="Avl Credit Limit" name="orgCreditLimit"
							readonly="true" value="%{#session.CREDIT_LIMIT}" />
						<s:textfield key="label.org.status" name="orgStatus"
							readonly="true" value="%{#session.USER_SEARCH_RESULTS.orgStatus}" />
							<s:textfield key="label.terminal.count" name="terminalCount"
							readonly="true" value="%{#session.USER_SEARCH_RESULTS.terminalCount}" />
							<s:textfield key="label.outstanding.bal" name="outstandingBal"
							readonly="true" value="%{#session.USER_SEARCH_RESULTS.outstandingBal}" />


						<tr>
							<td>
								<h3>
									<s:text name="label.user.detail" />
								</h3>
							</td>
						</tr>


						<s:textfield key="label.fname" name="firstName" readonly="true"
							value="%{#session.USER_SEARCH_RESULTS.firstName}" />
						<s:textfield key="label.lname" name="lastName" readonly="true"
							value="%{#session.USER_SEARCH_RESULTS.lastName}" />
						<s:textfield key="label.status" name="status" readonly="true"
							value="%{#session.USER_SEARCH_RESULTS.status}" id="Status" />
						<s:textfield key="label.mail.addr" name="emailId" readonly="true"
							value="%{#session.USER_SEARCH_RESULTS.emailId}" />
						<s:textfield key="label.ph.no" name="phoneNbr" readonly="true"
							value="%{#session.USER_SEARCH_RESULTS.phoneNbr}" />
						<s:textfield key="label.mob.no" name="mobileNbr" readonly="true"
							value="%{#session.USER_SEARCH_RESULTS.mobileNbr}" />

						<tr>
							<td></td>
							<td>
								<s:a href="bo_um_searchAgtRetUser_ResetPass.action"
									cssClass="hideButton">
									<s:text name="label.reset.pwd" />
								</s:a>
								<s:a cssStyle="position:relative;left:50px;"
									cssClass="hideButton"
									href="bo_um_searchAgtRetUser_ResetLoginAttempts.action">
									<s:text name="label.reset.login.attempt" />
								</s:a>
							</td>
						</tr>
						<tr>
							<td>
							</td>
							<td align="left">
								<table>
									<s:submit key="btn.edit.detail" align="left"
										cssClass="button hideButton" cssStyle="button" />
								</table>
							</td>
						</tr>


					</table>
				</s:form>
			</div>
		</div>
	</body>
</html>
<code id="headId" style="visibility: hidden">
	$RCSfile: bo_um_searchAgtRetUser_ViewDet.jsp,v $ $Revision:
	1.1.8.5.10.2 $
</code>