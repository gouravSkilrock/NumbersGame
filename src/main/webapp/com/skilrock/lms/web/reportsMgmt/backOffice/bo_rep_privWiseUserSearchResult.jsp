
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

		<s:head theme="ajax" debug="false" />
	</head>

	<body>
		<div id="wrap">
			<div id="top_form">

				<s:if test="%{privUsersList.isEmpty()}">
					<table>
						<tr>
							<td>
								<s:text name="msg.this.priv.not.active.any.usr" />
							</td>
						</tr>
					</table>
				</s:if>

				<s:else>

					<div id="wrap" style="text-align: center">
						<b><s:property value="message" /> </b>
					</div>
					<div></div>
					<div></div>

					<table width="70%" border="1" cellpadding="3" cellspacing="0"
						bordercolor="#CCCCCC" align="center">
						<tr>
							<th>
								<s:text name="label.uname" />
							</th>
							<th>
								<s:text name="label.name" />
							</th>
							<th>
								<s:text name="label.dept" />
							</th>
							<th>
								<s:text name="label.usr.type" />
							</th>
							<th>
								<s:text name="label.usr.status" />
							</th>
						</tr>

						<s:iterator value="%{privUsersList}">
							<tr>

								<s:if test='%{isRoleHead == "Not Head"}'>
									<s:url action="../userMgmt/bo_um_editBoSubUserPriv_Menu"
										id="privMenu">
										<s:param name="userName">
											<s:property value="userName" />
										</s:param>
									</s:url>
								</s:if>
								<s:else>
									<s:url action="../roleMgmt/bo_rm_editRole_Menu" id="privMenu">
										<s:param name="roleName">
											<s:property value="department" />
										</s:param>
										<s:param name="roleId">
											<s:property value="roleId" />
										</s:param>
									</s:url>
								</s:else>


								<td>
									<s:a href="%{privMenu}">
										<s:property value="userName" />
									</s:a>
								</td>
								<td>
									<s:property value="firstName" />
									<s:property value="lastName" />
								</td>
								<td>
									<s:property value="department" />
								</td>
								<td>
									<s:property value="isRoleHead" />
								</td>
								<td>
									<s:property value="status" />
								</td>


							</tr>
						</s:iterator>

					</table>

				</s:else>
			</div>

		</div>
	</body>
</html>

<code id="headId" style="visibility: hidden">
	$RCSfile: bo_rep_privWiseUserSearchResult.jsp,v $ $Revision: 1.3 $
</code>
