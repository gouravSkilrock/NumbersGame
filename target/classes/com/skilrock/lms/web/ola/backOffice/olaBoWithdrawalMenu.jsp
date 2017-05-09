<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<%
	response.setHeader("Cache-Control", "no-store"); //HTTP 1.1
	response.setHeader("Pragma", "no-cache"); //HTTP 1.0
	response.setDateHeader("Expires", 0); //prevents caching at the proxy server
%>
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">

	<head>
		<s:head theme="ajax" debug="false" />
		<meta http-equiv="CACHE-CONTROL" content="NO-CACHE" />
		<meta http-equiv="Content-Type"
			content="text/html; charset=iso-8859-1" />
		<link rel="stylesheet"
			href="<%=request.getContextPath()%>/LMSImages/css/styles.css"
			type="text/css" />
		<title><%=application.getAttribute("JSP_PAGE_TITLE")%></title>
		<script  > var path="<%=request.getContextPath()%>" ;</script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/com/skilrock/lms/web/common/globalJs/jquery-1.10.1.min.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/com/skilrock/lms/web/ola/backOffice/js/commonBoOla.js"></script>
		<script type="text/javascript" src="<%=request.getContextPath()%>/com/skilrock/lms/web/ola/backOffice/js/withdrawalBoOla.js"></script>

		<style type="text/css">
.outerBorderLeft {
	border-left: 3px solid #000000;
}
</style>

	</head>
	<body >
		<%@include file="/com/skilrock/lms/web/loginMgmt/menu.jsp"%>
		<div id="wrap">
			<div id="top_form">

				<h3>
					Withdrawal Information
				</h3>
				<div id="first">
					<s:form action="olaBoWithdrawal_Save" id="withDrawalFrm">
						<s:token  name="token" />
						<div style="color: red; text-align: left;">
							<s:actionmessage />
						</div>
						<s:actionerror />
						<table border="0" cellpadding="2" cellspacing="0" width="400px">
							<tr>
								<td colspan="2">
									<div style="color: red; text-align: center" id="walletName_e"></div>
								</td>
							</tr>
							<tr>
								<td>
									<s:select label="Wallet Name" name="walletName" headerKey="-1"
										id="walletName" headerValue="--Please Select--" list="{}"
										cssClass="option" required="true"></s:select>
								</td>
							</tr>
							<tr>
								<td colspan="2">
									<div style="color: red; text-align: center" id="amount_e"></div>
								</td>
							</tr>
							<tr>
								<td>
									<s:textfield name="amount" label="Amount" id="amount"
										maxLength="7" />
								</td>
							</tr>
							<tr>
								<td colspan="2">
									<div style="color: red; text-align: center" id="verifyAmount_e"></div>
								</td>
							</tr>

							<tr>
								<td>
									<s:textfield name="verifyAmount" label="Verify Amount"
										id="verifyAmount" maxLength="7" />
								</td>
							</tr>
							<tr>
								<td colspan="2">
									<div style="color: red; text-align: center" id="uName_e"></div>
								</td>
							</tr>


							<tr>
								<td>
									<s:textfield name="userName" label="User Name" id="user_name" />
								</td>
							</tr>
						</table>
						<tr>
							<td>
								<table id="sms_code" style="display: none;" border="0"
									cellpadding="2" cellspacing="0" width="400px">
									<tr>
										<td colspan="2">
											<div style="color: red; text-align: right" id="smsCode_e"></div>
										</td>
									</tr>
									<tr>
										<td>
											<s:textfield name="smsCode" label="Authentication code"
												id="smsCode" />
										</td>
									</tr>
								</table>
							</td>
						</tr>
						<tr>
							<td>
								&nbsp;
							</td>
							<td>
								<table>
									<s:submit value="Submit" align="right" cssClass="button" />
								</table>
							</td>
						</tr>



					</s:form>
				</div>

				<s:div id="results">
				</s:div>

			</div>
		</div>

	</body>
</html>
<code id="headId" style="visibility: hidden">
	$RCSfile: olaBoWithdrawalMenu.jsp,v $ $Revision: 1.1.2.1 $
</code>