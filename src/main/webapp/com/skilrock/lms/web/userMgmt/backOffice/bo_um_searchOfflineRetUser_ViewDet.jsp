<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@page import="java.text.NumberFormat"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">

	<head>



		<meta http-equiv="Content-Type"
			content="text/html; charset=iso-8859-1" />

		<link rel="stylesheet"
			href="<%=request.getContextPath()%>/LMSImages/css/styles.css"
			type="text/css" />

		<title><%=application.getAttribute("JSP_PAGE_TITLE")%></title>


		<s:head theme="ajax" debug="false" />
	</head>
	<body>
		<%@include file="/com/skilrock/lms/web/loginMgmt/menu.jsp"%>

		<div id="wrap">

			<div id="top_form">
				<h3>
					User Details
				</h3>
			<s:form action="bo_um_searchOfflineRetUser_Edit">

					<s:actionerror />
					<s:fielderror>
					</s:fielderror>
					<table border="0" cellpadding="2" cellspacing="0" width="450">
						<s:textfield label="User Name" name="userName" readonly="true"
							value="%{#session.USER_BEAN.userName}" />
						<s:textfield label="Organization Name" name="orgName"
							readonly="true"
							value="%{#session.USER_BEAN.userOrgName}" />
						<s:textfield label="Parent Organization Name" name="parentOrgName"
							readonly="true"
							value="%{#session.USER_BEAN.parentOrgName}" />
						<s:textfield label="Login Status" name="loginStatus"
							readonly="true"
							value="%{#session.USER_BEAN.loginStatus}" />
						<s:textfield label="Offline Status" name="offlineStatus"
							readonly="true"
							value="%{#session.USER_BEAN.offlineStatus}" />
						<s:textfield label="Is Offline" name="isOffline" readonly="true"
							value="%{#session.USER_BEAN.isOffline}" />

						<tr>
							<td>
							</td>
							<td align="left">
								<table>
									<s:submit value="Edit Details" align="left" cssClass="button"
										cssStyle="button" />
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
$RCSfile: bo_um_searchOfflineRetUser_ViewDet.jsp,v $
$Revision: 1.2 $
</code>