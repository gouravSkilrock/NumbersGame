
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="com.skilrock.lms.beans.UserInfoBean"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">

	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<link rel="stylesheet"
			href="<%=request.getContextPath()%>/LMSImages/css/styles.css"
			type="text/css" />
		<title><%=application.getAttribute("JSP_PAGE_TITLE")%></title>
		
		<script type="text/javascript"
			src="<%=request.getContextPath()%>/com/skilrock/lms/web/userMgmt/backOffice/js/boUserSearch.js"></script>
		<script type="text/javascript"
			src="<%=request.getContextPath()%>/com/skilrock/lms/web/common/globalJs/jquery-1.10.1.min.js"></script>

		<script>
			$(document).ready(function() { 	
							if($("#Status").val() == "TERMINATE"){
									$(".hideButton").hide();									
							}
							$("#Status option[value='INACTIVE']").prop('disabled',false);
				});	
		</script>
		<script>
			function makeEmpty(){
				document.getElementsByName('comment')[0].value= '';
				return true;
			}
			
			function chkOrgStatusNew(){
					var tdHide = document.getElementById('cmntTdId');
					if(document.getElementsByName('status')[0].value != 'TERMINATE'){
							document.getElementById('cmnt').innerHTML = '<input onclick="return makeEmpty()" type="textfield" name="comment" value="<s:text name="msg.enter.valid.reason"/>" />';
							tdHide.style.display = 'block';
					}else{
						tdHide.style.display = 'none';
						document.getElementById('cmnt').innerHTML = '';
					}
					return true;
			}
		</script>
	</head>

	<body>

		<%@include file="/com/skilrock/lms/web/loginMgmt/menu.jsp"%>
		<%
			UserInfoBean user = null;
			user = (UserInfoBean) session.getAttribute("USER_INFO");
			System.out.println("Name:--" + user.getUserName() + " Head "
					+ user.getIsRoleHeadUser() + " Role  "
					+ user.getIsMasterRole());
			String isRoleHeadUser = user.getIsRoleHeadUser();
			String isMasterRole = user.getIsMasterRole();
			if (isRoleHeadUser.equalsIgnoreCase("Y")
					& isMasterRole.equalsIgnoreCase("Y")) {
		%>
		<div id="wrap">

			<div id="top_form">
				<h3>
					<s:text name="label.srch.bo.usr" />
					:
					<s:text name="label.bo.user.detail" />
				</h3>
				<s:form onsubmit="return validateData()">
					<s:hidden name="user_id" value="%{userDetailsBean.UserId}"></s:hidden>
					<s:hidden name="type" value="%{userDetailsBean.boUserType}"></s:hidden>
					<s:hidden name="firstName" value="%{userDetailsBean.firstName}"></s:hidden>
					<s:hidden name="lastName" value="%{userDetailsBean.lastName}"></s:hidden>
					<table
						style="background-color: #F9F9FF; width: 450px; bordercolor: #CCCCCC;"
						border="0" cellpadding="3" cellspacing="0" align="left">
						<tr>
							<td align="left" class="label">
								<s:text name="label.name" />
								:
							</td>
							<td align="left" class="data">

								<s:property value="userDetailsBean.firstName" />
								<s:property value="userDetailsBean.lastName" />
							</td>
						</tr>

						<tr>
							<td align="left" class="label">
								<s:text name="label.dept" />
								:
							</td>
							<td align="left" class="data">
								<s:property value="userDetailsBean.department" />
							</td>
						</tr>

						<tr>
							<td align="left" class="label">
								<s:text name="label.usr.type" />
								:
							</td>
							<td align="left" class="data">
								<s:property value="%{type}" />
							</td>
						</tr>

						<tr>
							<td align="left" class="label">
								<s:text name="label.mail.status" />
								:
							</td>
							<td align="left" class="data">
								<s:property value="userDetailsBean.mailingStatus" />
							</td>
						</tr>

						<tr>
							<td align="center" colspan="2">
								<div id="errorDiv"></div>
							</td>
						</tr>

						<s:if test="%{type.equals('Mailing Users')}">
							<tr>
								<td>
									<s:textfield name="emailId" maxlength="40"
										key="label.mail.addr" id="emailId" readonly="true"
										value="%{userDetailsBean.emailId}"
										onchange="verifyEmail('verifyEmail.action?user_id=%{userDetailsBean.UserId}&&emailId=' + this.value,this.value)"></s:textfield>
								</td>
								<td id="lodingImage" style="display: none; color: red;">
									Wait...
									<img
										src="<%=request.getContextPath()%>/LMSImages/images/loadingAjax.gif" />
								</td>
							</tr>
						</s:if>
						<s:else>
							<tr>
								<td colspan="2">
							<s:textfield name="emailId" key="label.mail.addr" maxlength="40"
								id="emailId" readonly="true" value="%{userDetailsBean.emailId}"></s:textfield>
								</td>
							</tr>
						</s:else>
						<tr>
							<td align="center" colspan="2">
								<div id="phonNbrErrorDiv"></div>
							</td>
						</tr>
						<tr>
							<s:textfield name="phoneNbr" key="label.ph.no" id="phoneNbr"
								maxlength="15" readonly="true"
								value="%{userDetailsBean.phoneNbr}"></s:textfield>
						</tr>
						<s:if
							test="%{type.equals('Head') && (userDetailsBean.department).equals('Master')}">

						</s:if>
						<s:else>
							<tr>
								<s:select name="status" key="label.status"
									value="%{userDetailsBean.status}"
									list="{'ACTIVE','INACTIVE','BLOCK','TERMINATE'}"
									cssClass="option" id="Status"
									onchange="return chkOrgStatusNew()">
								</s:select>
							</tr>
							<tr>
								<td id="cmntTdId" style="display: none" align="left">
									<s:text name="label.comment" />
									:
								</td>
								<td>
									<s:div id="cmnt"></s:div>
								</td>
							</tr>
						</s:else>
						<tr>
							<td align="right">
								<input class="button hideButton" type="button" value="<s:text name="label.edit"/>"
									onclick="enableBoxes()" />
							</td>
							<s:if test="%{!type.equals('Mailing Users')}">
								<td align="left">
									<input class="button hideButton" type="button"
										value="<s:text name="label.reset.pwd"/>" id="resetBtn"
										onclick="resetPasswordBO('bo_um_searchBoUser_ResetPassBO.action?user_id=<s:property value="userDetailsBean.UserId"/>&&emailId=<s:property value="userDetailsBean.emailId"/>&&firstName=<s:property value="userDetailsBean.firstName"/>&&lastName=<s:property value="userDetailsBean.lastName"/>&&userName=<s:property value="userDetailsBean.userName"/>','<s:property value="userDetailsBean.emailId"/>')" />
								</td>
								<td id="lodingImagePass" style="display: none; color: red;">
									<s:text name="label.wait" />...
									<img
										src="<%=request.getContextPath()%>/LMSImages/images/loadingAjax.gif" />
								</td>
							</s:if>
							<td>
								<s:a id="hyprlink" cssClass="hideButton"
									href="bo_um_searchBoUser_ResetLoginAttemptsBOUser.action?user_id=%{userDetailsBean.UserId}&userName=%{userDetailsBean.userName}">
									<s:text name="label.reset.login.attempt" />
								</s:a>
							</td>
						</tr>
						<tr>
							<td>
								&nbsp;
							</td>
							<td align="left">
								<table>
									<s:submit key="btn.submit" id="submitButton" disabled="true"
										cssClass="button hideButton" action="bo_um_searchBoUser_Save" />
								</table>
							</td>
						</tr>
					</table>


				</s:form>





			</div>

		</div>
		<%
			} else {
		%>
		<div>
			<jsp:include page="/com/skilrock/lms/web/loginMgmt/unauthorize.jsp"></jsp:include>
		</div>
		<%
			}
		%>
	</body>
</html>

<code id="headId" style="visibility: hidden">
	$RCSfile: bo_um_searchBoUser_EditDet.jsp,v $ $Revision:
	1.1.2.1.6.4.10.2 $
</code>