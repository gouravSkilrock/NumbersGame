<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">

	<head>

		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />

		<link rel="stylesheet"
			href="<%=request.getContextPath()%>/LMSImages/css/styles.css"
			type="text/css" />

		<title><%=application.getAttribute("JSP_PAGE_TITLE")%></title>

	</head>

	<body>

		<%@include file="/com/skilrock/lms/web/loginMgmt/menu.jsp"%>

		<div id="wrap">
			<div id="top_form">

				<h3>
					<s:property value="#application.TIER_MAP.RETAILER" />
					<s:text name="label.org" />
					:
					<font color=red> <s:property
						value="#session.ORGANIZATION_NAME" /></font>
					<s:text name="label.has.been.created.success" />
					.
				</h3>
				<h3>
					<s:text name="label.uname" />
					:
					<s:property value="#session.USER_NAME" />
				</h3>
				<h3>
					<s:text name="label.org.code" />
					:
					<font color=blue> <s:property value="#session.ORG_CODE" /></font>
				</h3>
				<s:if test="#application.RET_PASSWORD_SHOWN_STATUS=='YES'">

					<h3>
						<s:text name="msg.ur.pwd.is" />
						:
						<font color=blue> <s:property
							value="#session.RETAILER_PASSWORD" /></font>
					</h3>

				</s:if>

				<table border="0" cellpadding="3" cellspacing="0">
					<tr style="display: none;">
						<td>
							<s:a theme="simple" href="agt_um_retReg_Menu.action?request_locale=%{#session.WW_TRANS_I18N_LOCALE}">
								<s:text name="msg.reg.auth" />
								<s:property value="#application.TIER_MAP.RETAILER" />
								<s:text name="label.user" />
							</s:a>
						</td>
					</tr>
				</table>

			</div>
		</div>
	</body>
</html>
<code id="headId" style="visibility: hidden">
	$RCSfile: agt_um_retReg_Success.jsp,v $ $Revision: 1.1.2.1.4.1.2.4.8.1
	$
</code>