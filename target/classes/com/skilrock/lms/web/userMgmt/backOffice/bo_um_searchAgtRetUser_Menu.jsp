
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%
	response.setDateHeader("Expires", 0);
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en">

	<head>



		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />

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
					<s:text name="btn.srch" />
					<s:property value="#application.TIER_MAP.AGENT" />
					/
					<s:property value="#application.TIER_MAP.RETAILER" />
					's
					<s:text name="label.user" />
				</h3>
				<s:form action="bo_um_searchAgtRetUser_Search">

					<table border="0" cellpadding="2" cellspacing="0" width="450">
						<tr>

						</tr>
						<tr>
							<s:textfield key="label.uname" name="userName"></s:textfield>
						</tr>
						<tr>
							<td>
								<s:textfield key="label.org.name" name="compName"></s:textfield>
							</td>
							<td>
								<s:textfield key="label.prnt.org.name" name="parentCompName"></s:textfield>
							</td>
						</tr>
						<tr>
							<s:select headerKey="1" headerValue="%{getText('label.please.select')}"
								key="label.role" name="userRole" list="#session.roleList"
								cssClass="option"></s:select>
						</tr>

						<tr>
							<s:select headerKey="1" headerValue="%{getText('label.please.select')}"
								key="label.status" name="userStatus"
								list="#{'ACTIVE':getText('ACTIVE'),'INACTIVE':getText('INACTIVE'),'BLOCK':getText('BLOCK'),'TERMINATE':getText('TERMINATE')}"
								cssClass="option"></s:select>
						</tr>
						<tr>
							<td>
							</td>
							<td align="left">
								<table>
									<s:submit align="left" theme="ajax" targets="bottom"
										key="btn.srch" cssClass="button" />
								</table>
							</td>
						</tr>

					</table>

				</s:form>

				<div id="bottom">
				</div>
			</div>

		</div>
	</body>
</html>
<code id="headId" style="visibility: hidden">
	$RCSfile: bo_um_searchAgtRetUser_Menu.jsp,v $ $Revision: 1.4 $
</code>