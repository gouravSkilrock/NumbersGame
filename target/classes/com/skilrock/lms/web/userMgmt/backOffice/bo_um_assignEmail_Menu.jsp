
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
		<script>
		var path ="<%=request.getContextPath()%>";
		</script>
		<script type="text/javascript"
			src="<%=request.getContextPath()%>/com/skilrock/lms/web/userMgmt/backOffice/js/reportMail.js"></script>
		<s:head theme="ajax" debug="false" />
	</head>

	<body>

		<%@include file="/com/skilrock/lms/web/loginMgmt/menu.jsp"%>
		<div id="wrap">
			<div id="top_form">
				<h3>
					Assign <s:text name="Report"/> E-Mail Privileges to User
				</h3>
				<br />

				<s:form action="bo_um_assignEmail_UpdatePriv" name="update_form">
					<s:hidden id="isRoleHeadUser"
						value="%{#session.USER_INFO.getIsRoleHeadUser()}"></s:hidden>

					<table width="100%" border="0" cellspacing="0" cellpadding="0">
						<tr>
							<td>

								<table width="450" border="0" cellspacing="0" cellpadding="4">
									<tr>
										<td class="label" align="right" width="170">
										</td>
										<td width="415">
											<s:select list="#{'BO':'BO', 'AGENT':#application.TIER_MAP.AGENT.toUpperCase(), 'RETAILER':#application.TIER_MAP.RETAILER.toUpperCase()}"
												label="Organisation Type:" name="orgType" cssClass="option"
												id="org_type" onchange="onTypeChangeValue(this.value)"
												required="true"></s:select>
										</td>
									</tr>
									<tr>
										<td class="label" align="right" width="170">
											Search User Type:
										</td>
										<td valign="top" align="left">
											<table width="100%" border="0" cellspacing="0"
												cellpadding="4">
												<tr>
													<td>
														<input type="radio" class="radio" value="EXISTING"
															name="user" id="user_existing"
															onclick="checkUserIs(this.id)" />
														EXISTING USERS
													</td>
													<td>
														<div id="new" style="display: block;">
															<input type="radio" class="radio" value="NEW" name="user"
																id="user_new" onclick="checkUserIs(this.id)" />
															NEW USERS
														</div>
													</td>
												</tr>
											</table>
										</td>
									</tr>
								</table>

							</td>
						</tr>

						<tr>
							<td>
								<table width="450" border="0" cellspacing="0" cellpadding="4"
									style="display: none;" id="new_user_form_row">
									<tr>
										<td align="left" colspan="2">
											<div id="new_first_error"
												style="text-align: center; color: red">
										</td>
									</tr>
									<tr>
										<td width="188" class="label" align="right">
											First Name:
										</td>
										<td width="414">
											<s:textfield name="firstName" id="new_first" theme="simple" />
										</td>
									</tr>
									<tr>
										<td align="left" colspan="2">
											<div id="new_last_error"
												style="text-align: center; color: red">
										</td>
									</tr>
									<tr>
										<td class="label" align="right">
											Last Name:
										</td>
										<td width="414">
											<s:textfield name="lastName" id="new_last" theme="simple" />
										</td>
									</tr>
									<tr>
										<td align="left" colspan="2">
											<div id="new_mob_error"
												style="text-align: center; color: red">
										</td>
									</tr>
									<tr>
										<td class="label" align="right">
											Mobile No.:
										</td>
										<td width="414">
											<s:textfield name="mobile" id="new_mob" theme="simple"
												value="" maxlength="15" />
										</td>
									</tr>
									<tr>
										<td colspan="2">
											<div id="email_error" style="text-align: center; color: red">
										</td>
									</tr>
									<tr>
										<td class="label" align="right">
											Email ID:
										</td>
										<td width="414">
											<s:textfield name="firstName" id="new_email_id"
												theme="simple" maxlength="45" />
										</td>
									</tr>
									<tr>
										<td>
											&nbsp;
										</td>
										<td>
											<input type="button" id="new_get_priv_button"
												value="Verify Email ID" class="button"
												onclick="verifyEmailAndGetPrivList()" />
										</td>
									</tr>

								</table>

							</td>
						</tr>

						<tr>
							<td colspan="2">
								<div id="new_Form_div"></div>
							</td>
						</tr>

						<tr>
							<td colspan="2">
								<div id="new_Form_div_button"></div>
							</td>
						</tr>



						<tr>
							<td>
								<table width="450" border="0" cellspacing="0" cellpadding="4"
									id="user_type_row" style="display: none;">
									<tr>
									<td width="199" class="label" align="right"></td>
									<td width="414">
									<s:hidden theme="simple" name="userType" id="user_type" value="All" ></s:hidden>
									</td>
									</tr>
									<!--<tr>
										<td width="199" class="label" align="right">
											User Type:
										</td>
										<td width="414">
											&nbsp;
											<s:select
												list="{'ALL','BO MASTER','BO ADMIN','BO ACCOUNT','BO INVENTORY','E-MAIL'}"
												headerKey="-1" headerValue="--Please Select--"
												name="userType" theme="simple" cssClass="option"
												id="user_type"></s:select>
												
										</td>
									</tr>-->

									<tr>
										<td>
											&nbsp;
										</td>
										<td width="413" valign="top" align="left">

											<table width="100%" border="0" cellspacing="0"
												cellpadding="4" style="display: none;" id="isrolehead_row">
												<tr>

													<td>
														<input type="radio" class="radio" value="ALL"
															name="isRoleHead" checked="checked" id="isRoleHead_ALL" />
														ALL
													</td>
												</tr>
												<tr>
													<td>
														<input type="radio" class="radio" value="ROLEHEAD"
															name="isRoleHead" id="isRoleHead_rolehead" />
														RoleHead
													</td>
												</tr>
												<tr>
													<td>
														<input type="radio" class="radio" value="NONEROLEHEAD"
															name="isRoleHead" id="isNotRoleHead_rolehead" />
														NON-RoleHead
													</td>
												</tr>
											</table>


										</td>
									</tr>
								</table>
							</td>
						</tr>
						<tr>
							<td>

								<table width="450" border="0" cellspacing="0" cellpadding="4"
									id="agent_list_row" style="display: none;">
									<tr>
										<td width="199" align="right" class="label">
											<s:property value="#application.TIER_MAP.AGENT" /> Org Type:
										</td>
										<td width="413" align="left">
											<s:select list="{}" headerKey="-1"
												headerValue="--Please Select--" name="agentUser"
												theme="simple" cssClass="option" id="agent_user_select"></s:select>
										</td>
									</tr>

								</table>

							</td>
						</tr>


						<tr>
							<td>
								<table cellspacing="0" cellpadding="4" border="0" width="450"
									id="search_button">
									<tr>
										<td width="132">
											&nbsp;
										</td>
										<td align="left">
											<input type="button" name="showDetail" id="button"
												value="SEARCH" class="button"
												onclick="getUserListForReportMail()" />
											&nbsp;
											<div id="loadingDiv" style="float: right">
												&nbsp;
											</div>
										</td>
									</tr>
								</table>
							</td>
						</tr>

						<tr>
							<td>
								<table cellpadding="4" cellspacing="0" border="0" width="600"
									id="user_name_row" style="display: none;">
									<tr>
										<td width="131" class="label" align="right">
											User Name:
										</td>
										<td width="437">
											<s:select list="{}" headerKey="-1"
												headerValue="--Please Select--" name="userName"
												theme="simple" cssClass="option" id="user_name_select"
												onchange="getPriviledgesList(this.value)"></s:select>
										</td>
									</tr>
								</table>
							</td>
						</tr>

						<tr>
							<td class="data">
								<div id="d2"></div>
							</td>
						</tr>
						<tr>
							<td align="left">
								<div id="d3">
									<div>
							</td>
						</tr>

					</table>

				</s:form>
			</div>
		</div>

	</body>
</html>

<code id="headId" style="visibility: hidden">
$RCSfile: bo_um_assignEmail_Menu.jsp,v $
$Revision: 1.2 $
</code>