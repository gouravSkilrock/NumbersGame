<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page import="java.util.*"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">

	<head>
		<s:head theme="ajax" debug="false" />
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<link rel="stylesheet"
			href="<%=request.getContextPath()%>/LMSImages/css/styles.css"
			type="text/css" />
		<title><%=application.getAttribute("JSP_PAGE_TITLE")%></title>
		<script type="text/javascript"
			src="<%=request.getContextPath()%>/com/skilrock/lms/web/common/globalJs/jquery-1.10.1.min.js"></script>
		<script type="text/javascript"
			src="<%=request.getContextPath()%>/com/skilrock/lms/web/reportsMgmt/backOffice/js/report.js"></script>
	</head>



	<body onload="resetValues();">
		<%@include file="/com/skilrock/lms/web/loginMgmt/menu.jsp"%>
		<div id="wrap">
			<div id="top_form">
				<h3>
					<s:text name="label.priv.rep" />
				</h3>

				<s:form name="editPriv" onsubmit="return _privServiceValidate();"
					method="post" enctype="multipart/form-data">
					<s:actionerror />

					<s:select name="Spec" id="service_list" theme="simple"
						cssStyle="display:none" list="%{serviceMap}" />
					<s:select name="Spec" id="group_list" theme="simple"
						cssStyle="display:none" list="%{groupMap}" />
					<s:select name="Spec" id="priv_list" theme="simple"
						cssStyle="display:none" list="%{grpPrivMap}" />
					<s:select name="Spec" id="user_list" theme="simple"
						cssStyle="display:none" list="%{userMap}" />
					<s:select name="Spec" id="dir_ser_priv" theme="simple"
						cssStyle="display:none" list="%{dirSerPrivMap}" />
					<s:select name="Spec" id="dir_rel_priv" theme="simple"
						cssStyle="display:none" list="%{dirRelPrivMap}" />
					<s:select name="Spec" id="userType_list" theme="simple"
						cssStyle="display:none" list="%{userTypeMap}" />

					<table width="100%" border="0" cellspacing="0" cellpadding="0">

						<tr>
							<td>

								<table border="0" cellpadding="2" cellspacing="0" width="450"
									style="text-align: left">
									<tr>
										<td class="label" align="right" width="170">
											<s:text name="label.slct.report" />:
										</td>
										<td valign="top" align="left">
											<table width="100%" border="0" cellspacing="0"
												cellpadding="4">
												<tr>
													<td>
														<input type="radio" class="radio" value="PRIV"
															name="repType" id="privWiseReport"
															onclick="checkPrivIs(this.id)" checked="checked" />
														<s:text name="label.privwise" />
													</td>
													<td>
														<input type="radio" class="radio" value="<s:text name="label.user" />"
															name="repType" id="userWiseReport"
															onclick="checkUserIs(this.id)" />
														<s:text name="label.userwise" />
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

								<table id="privTab" border="0" cellpadding="2" cellspacing="0"
									width="450" style="text-align: left; display: block;">
									<tr>
										<td colspan="2">
											<div id="service_err"></div>
										</td>
									</tr>
									<s:select name="serviceId" id="service" key="label.srv.name"
										headerKey="-1" headerValue="--Please Select--"
										list="%{ServiceMap}" cssClass="option"
										onchange="clearContent('service','resultPriv');chageGroupList(this.value, 'group', 'group_list','-1'); _id.o(\'priv').options.length=1; changeValuebyService(this.value, 'dir_ser_priv', 'privIds')" />
									<tr>
										<td colspan="2">
											<div id="group_err"></div>
										</td>
									</tr>
									<s:select name="groupId" id="group" key="label.group.name"
										headerKey="-1" headerValue="--Please Select--" list="{}"
										cssClass="option"
										onchange="chagePrivList(this.value, 'priv', 'priv_list','-1'); changeValuebyRelated(this.value, 'dir_rel_priv', 'privIds' )" />
									<tr>
										<td colspan="2">
											<div id="priv_err"></div>
										</td>
									</tr>
									<s:select name="privId" id="priv" key="label.priv.group.name"
										headerKey="-1" headerValue="--Please Select--" list="{}"
										cssClass="option"
										onchange="chageValuebyPriv(this.value, 'privIds')" />
									<s:textfield name="privIds" id="privIds" value=""
										maxlength="700" cssStyle="display:none" />
								</table>

							</td>
						</tr>

						<tr>
							<td>

								<table id="userTab" border="0" cellpadding="2" cellspacing="0"
									width="450" style="text-align: left; display: none;">
									<tr>
										<td colspan="2">
											<div id="dept_err"></div>
										</td>
									</tr>
									<s:select name="deptId" id="dept" key="label.dept"
										headerKey="-1" headerValue="--Please Select--"
										list="%{deptMap}" cssClass="option"
										onchange="clearContent('dept','resultPriv2'); chageUserTypeList(this.value, 'tier', 'userType_list','-1'); chageUserList(this.value, 'user', 'user_list','-1');" />
									<tr>
										<td colspan="2">
											<div id="tier_err"></div>
										</td>
									</tr>
									<s:select name="tierId" id="tier" key="label.usr.type"
										headerKey="-1" headerValue="--Please Select--" list="{}"
										cssClass="option"
										onchange="clearContent('tier','resultPriv2'); chageUserList(this.value, 'user', 'user_list','-1');" />
									<tr>
										<td colspan="2">
											<div id="users_err"></div>
										</td>
									</tr>
									<s:select name="userId" id="user" key="label.users" headerKey="-1"
										headerValue="--Please Select--" list="{}" cssClass="option"
										onchange="clearContent('user','resultPriv2');" />
								</table>

							</td>
						</tr>

						<tr>
							<td>

								<table id="searchId" border="0" cellpadding="5" cellspacing="0"
									width="360px">
									<s:submit key="btn.srch" align="right" cssClass="button"
										name="b2" theme="ajax" />
								</table>

							</td>
						</tr>

					</table>
				</s:form>
				<table>
					<tr>
						<td id="lodingImage" style="display: none; color: red;">
							<s:text name="label.wait"/>...
							<img
								src="<%=request.getContextPath()%>/LMSImages/images/loadingAjax.gif" />
						</td>
					</tr>
				</table>

				<div id="resultPriv" style="text-align: center; display: none"></div>
				<div id="resultPriv2" style="text-align: center; display: none"></div>
	</body>
</html>

<code id="headId" style="visibility: hidden">
	$RCSfile: bo_rep_privWiseUserReport_Menu.jsp,v $ $Revision: 1.3 $
</code>