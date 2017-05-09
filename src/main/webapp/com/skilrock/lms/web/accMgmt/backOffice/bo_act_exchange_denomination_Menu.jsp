<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>


<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">

	<head>

		<meta http-equiv="Content-Type"
			content="text/html; charset=iso-8859-1" />
		<link rel="stylesheet"
			href="<%=request.getContextPath()%>/LMSImages/css/styles.css"
			type="text/css" />
		<title><%=application.getAttribute("JSP_PAGE_TITLE")%></title>
		<script type="text/javascript"
			src="<%=request.getContextPath()%>/com/skilrock/lms/web/accMgmt/backOffice/js/common_cash_register.js"></script>

	</head>

	<body onload="fillTables()">
		<%@include file="/com/skilrock/lms/web/loginMgmt/menu.jsp"%>
		<div id="wrap">
		<h2><div id="errorDiv" style="color: red;"></div></h2>
		
			<div id="top_form">
				<h3>
					Exchange Denomination
				</h3>
				<s:form action="bo_act_exhange_denomination_success">
					<table border="0" cellpadding="2" cellspacing="0">
						<tr>
							<td>
								<h3>
									Enter Initial Denomination
								</h3>
								<table border="1" cellpadding="0" cellspacing="0" width="200"
									id="eTable" style="border-bottom-color: purple;">
									<tr>
										<th>
											Type
										</th>
										<th></th>
									</tr>
								</table>
							</td>
							<td>
								================>>
							</td>
							<td>
								<h3>
									Enter exchange denomination
								</h3>
								<table border="1" cellpadding="0" cellspacing="0" width="200"
									id="exchangeTable" style="border-bottom-color: purple;">
									<tr>
										<th>
											Type
										</th>
										<th></th>
									</tr>

								</table>
							</td>

						</tr>
						<tr>
							<td colspan="3">
								<s:submit value="Submit" align="center" cssClass="button"
									onclick="return verifyExchangeAmount()" />

							</td>
						</tr>
					</table>
				</s:form>
			</div>
		</div>
	</body>

</html>
<code id="headId" style="visibility: hidden">
	$RCSfile: bo_act_exchange_denomination_Menu.jsp,v $ $Revision: 1.1.2.8
	$
</code>

