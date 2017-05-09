<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">

	<head>



		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />

		<link rel="stylesheet"
			href="<%=request.getContextPath()%>/LMSImages/css/styles.css"
			type="text/css" />
		<script type="text/javascript"
			src="<%=request.getContextPath()%>/com/skilrock/lms/web/accMgmt/backOffice/js/ACT_ST5_Validation.js" />
		<title><%=application.getAttribute("JSP_PAGE_TITLE")%></title>
		<s:head debug="false" />
	</head>

	<body>
		<%@include file="/com/skilrock/lms/web/loginMgmt/menu.jsp"%>
		<div id="wrap">
			<div id="top_form">
				<h3>
					<s:property value="#application.TIER_MAP.AGENT" />
					<s:text name="Payment" />
				</h3>
				<table width="250" border="1" align="center" cellpadding="5"
					cellspacing="0" bordercolor="#CCCCCC">


					<tr>

						<td>
							<div align="center">
								<s:text name="Receipt Id"/> <s:text name="label.status"/>:
								<s:property value="%{#session.status}" />
							</div>
						</td>
					</tr>
				</table>
				<a
					href="<%=request.getContextPath()%>/com/skilrock/lms/web/accMgmt/bo_agt_act_cash_menu.action"><s:text name="label.click.here.to.make.more.pay" /> </a>

			</div>
		</div>
	</body>
</html>
<code id="headId" style="visibility: hidden">
	$RCSfile: bo_agt_act_cash_success.jsp,v $ $Revision: 1.3 $
</code>