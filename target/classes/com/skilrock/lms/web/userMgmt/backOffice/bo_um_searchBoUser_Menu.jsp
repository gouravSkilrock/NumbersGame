
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en">

	<head>

		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<meta http-equiv="pragma" content="no-cache"/>
		<meta http-equiv="cache-control" content="no-cache"/>
		<meta http-equiv="expires" content="0"/>   

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
					<s:text name="label.srch.bo.usr" />
				</h3>
				<s:form action="bo_um_searchBoUser_Search">

					<table border="0" cellpadding="2" cellspacing="0" width="450">

						<tr>
							<s:textfield key="label.name" name="userName"></s:textfield>
						</tr>
						<tr>
							<s:select key="label.dept" name="roleId" headerKey="-1"
								headerValue="%{getText('label.please.select')}" list="roleMap"
								cssClass="option"></s:select>
						</tr>
						<tr>
							<s:select key="label.type" name="type"
								list="#{'All':getText('label.all'),'Head':getText('label.head'),'Sub Users':getText('label.subusers'),'Mailing Users':getText('label.MailingUsers')}"
								cssClass="option" onchange=""></s:select>
						</tr>
						<tr>
							<s:select key="label.status" name="status"
								list="#{'Active':getText('ACTIVE'),'Inactive':getText('INACTIVE'),'Block':getText('BLOCK'),'Terminate':getText('TERMINATE')}"
								cssClass="option"></s:select>
						</tr>
					</table>
					<table border="0" cellpadding="2" cellspacing="0" width="450">
						<tr>
							<td style="width: 157px">
								&nbsp;
							</td>
							<td align="left">
								<table>

									<s:submit key="btn.srch" theme="ajax" targets="searchDiv"
										cssClass="button"></s:submit>
								</table>
							</td>
						</tr>
					</table>


				</s:form>

				<div id="searchDiv">
				</div>


			</div>

		</div>
	</body>
</html>
<code id="headId" style="visibility: hidden">
	$RCSfile: bo_um_searchBoUser_Menu.jsp,v $ $Revision: 1.3 $
</code>