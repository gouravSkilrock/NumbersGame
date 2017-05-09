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
		src="<%=request.getContextPath()%>/com/skilrock/lms/web/ola/js/commonOla.js"></script>
	</head>
	<body>
		<%@include file="/com/skilrock/lms/web/loginMgmt/menu.jsp"%>
		<div id="wrap">
			<div id="top_form">

				<s:form action="ret_ola_pending_Withdrawal_Cancel">
			
					<s:hidden name="playerName" value="%{userName}"></s:hidden>
					<s:hidden name="newwalletName" value="%{walletName}"></s:hidden>
					<s:hidden name="depositAmount" value="%{amount}"></s:hidden>
					<table width="500px" border="1" cellpadding="3" cellspacing="0"
						bordercolor="#CCCCCC" align="left">

						<tr>
							<th colspan="3" align="center">
								OLA Pending Withdrawal List
							</th>
						</tr>
						<tr>
							<td colspan="3">
								<div style="color: red; text-align: center" id="checkboxName_e"></div>
								<div style="color: red; text-align: center"><s:actionmessage/></div>
							</td>
						</tr>
						<tr>
							<th>
								withdrawal Request Code
							</th>
							<th>
								Amount
							</th>
							<th>
								Date
							</th>
						</tr>

						<s:if test="#session.pendingRequestMap.size()>0">
							<s:iterator value="#session.pendingRequestMap" status="stat">

								<tr>
									<td>
										<s:checkbox name="withdrawalCodeArr" id="withdrawalCodeArr"
											theme="simple" key="%{key}" fieldValue="%{key}"></s:checkbox>

										<s:property value="%{key}" />
									</td>
									<s:iterator value="%{value}">
										<td>
											<s:property />
										</td>
									</s:iterator>
								</tr>
							</s:iterator>
							<tr>

								<td align="center">

									<table>
										<s:submit cssClass="button" label="Submit" align="left"
											value="Cancel" onclick="return verifyCheckbox()" ></s:submit>
									</table>
								</td>
								<td align="center">
									<table>
										<s:submit cssClass="button" label="Submit" align="right"
											value="Continue Deposit" action="ret_ola_deposit_Submit"></s:submit>
									</table>
								</td>


							</tr>
						</s:if>
						<s:else>
							<tr>
								<th colspan="4" align="center">

									No Records Found
								</th>
							</tr>

						</s:else>
					</table>
				</s:form>
			</div>
		</div>

	</body>
</html>
<code id="headId" style="visibility: hidden">
	$RCSfile: ola_pending_withdrawal_request.jsp,v $ $Revision:
	1.1.6.1.2.5 $
</code>