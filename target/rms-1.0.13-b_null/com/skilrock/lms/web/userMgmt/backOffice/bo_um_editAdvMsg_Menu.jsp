<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
	<%
		response.setHeader("Cache-Control", "no-store"); //HTTP 1.1
		response.setHeader("Pragma", "no-cache"); //HTTP 1.0
		response.setDateHeader("Expires", 0); //prevents caching at the proxy server
	%>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<link rel="stylesheet"
			href="<%=request.getContextPath()%>/LMSImages/css/styles.css"
			type="text/css" />
		<title><%=application.getAttribute("JSP_PAGE_TITLE")%></title>
		<s:head theme="ajax" debug="false" />
		<script>var path = "<%=request.getContextPath()%>";</script>
		<script type="text/javascript"
			src="<%=request.getContextPath() %>/com/skilrock/lms/web/common/globalJs/jquery-1.10.1.min.js"></script>
		<script type="text/javascript"><!--
			$(document).ready(function() {
				$("#playerDiv").hide();
			});

			function setUpdateValues(messageId, pexpiryDate, pisPopup, pisMandatory,
					pstatus) {
				var expiryDate = $('#expiryDate_' + messageId).val();
				var isPopup = $('#isPopup_' + messageId).val();
				var isMandatory = $('#isMandatory_' + messageId).val();
				var status = $('#status_' + messageId).val();
		
				var isValid = !!new Date(expiryDate.replace(/-/g, '/')).getTime();
		
				$('#message_' + messageId).css("color", "red");
				if (expiryDate == pexpiryDate && isPopup == pisPopup
						&& isMandatory == pisMandatory && status == pstatus) {
					$('#message_' + messageId).html('<s:text name="error.select.atleast.one.val"/>');
				} else if (!isValid) {
					$('#message_' + messageId).html('<s:text name="error.enter.correct.date"/>');
				} else if ((new Date(expiryDate.replace(/-/g, '/')).getTime() - new Date()
						.getTime()) < 0) {
					$('#message_' + messageId).html('<s:text name="error.enter.date.after.cur.time"/>');
				} else {
					var paramString = 'type=retMessage&msgId=' + messageId
							+ '&expiryDate=' + expiryDate + "&isPopup="
							+ ((isPopup == 'YES') ? true : false) + "&isMandatory="
							+ ((isMandatory == 'YES') ? true : false) + "&status="
							+ status;
					var resp = _ajaxCallDiv('bo_um_advMessage_Edit_Save.action',
							paramString, null);
					$('#message_' + messageId).css("color", "green");
					$('#message_' + messageId).html('<s:text name="msg.val.update.success"/>');
				}
			}

			function getInterfaceList() {
				$("#result").html("");
				var userType = $("#orgId").val();
				if (userType != 'PLAYER') {
					$("#playerDiv").hide();
					$(".barOption").show();
				} else {
					$("#playerDiv").show();
					$(".barOption").hide();
				}
		
				$("#interfaceType").html("");
				//$("#interfaceType").append(new Option("--Please Select--", "-1"));
				$('#interfaceType').append(
						$('<option></option>').val('-1').html('<s:text name="label.please.select"/>'));
				$("#mode").html("");
				//$("#mode").append(new Option("--Please Select--", "-1"));
				$('#mode').append(
						$('<option></option>').val('-1').html('<s:text name="label.please.select"/>'));
				var _resp = _ajaxCallText(projectName
						+ "/com/skilrock/lms/web/userMgmt/bo_um_getInterfaceList.action?orgType="
						+ userType);
				var json = JSON.parse(_resp);
				$.each(json.responseData, function(key, value) {
					//$("#interfaceType").append(new Option(value, value));
						$('#interfaceType').append(
								$('<option></option>').val(value).html(value));
					});
			}

			function getModeList() {
				$("#result").html("");
				var userType = $("#orgId").val();
				var interfaceType = $("#interfaceType").val();
				$("#mode").html("");
				//$("#mode").append(new Option("--Please Select--", "-1"));
				$('#mode').append(
						$('<option></option>').val('-1').html('<s:text name="label.please.select"/>'));
				var _resp = _ajaxCallText(projectName
						+ "/com/skilrock/lms/web/userMgmt/bo_um_getModeList.action?orgType="
						+ userType + "&interfaceType=" + interfaceType);
				var json = JSON.parse(_resp);
				$.each(json.responseData, function(key, value) {
					//$("#mode").append(new Option(value, key));
						$('#mode').append(
								$('<option></option>').val(key).html(value));
					});
			}

			function getMessageListForEdit() {
				$("#result").html("");
				var userType = $("#orgId").val();
				var interfaceType = $("#interfaceType").val();
				var mode = $("#mode").val();
				var _resp = _ajaxCallDiv(
						projectName
								+ "/com/skilrock/lms/web/userMgmt/bo_um_getMessageListForEdit.action",
						"mode=" + mode, "result");
			}

			function updateMessageStatus(messageId) {
				if(window.confirm("Are you sure to change status ?")) {
					var status = $("#status_" + messageId).val();
					var _resp = _ajaxCall(projectName
							+ "/com/skilrock/lms/web/userMgmt/bo_um_updateMessageStatus.action?messageId="
							+ messageId + "&status=" + status);
					alert('<s:text name="msg.val.update.success"/>');
				}
			}
			function confirmChanges(elem) {
				return window.confirm("Are you sure to change status ?");
			}
	</script>
	</head>
	<body>
		<%@include file="/com/skilrock/lms/web/loginMgmt/menu.jsp"%>
		<div id="wrap">
			<div id="top_form">
				<h3>
					<s:text name="label.edit.msg" />
				</h3>
				<table width="40%" cellpadding="3" cellspacing="0" style="float:none;">
					<tr>
						<td>
							<s:text name="label.slct.msg.for" />
						</td>
						<td>
							<s:select theme="simple" list="userTypeMap"
								key="label.slct.msg.for" id="orgId" name="orgType"
								cssClass="option" headerKey="-1" headerValue="%{getText('label.please.select')}"
								onchange="getInterfaceList(); "></s:select>
						</td>
					</tr>
					<tr class="barOption">
						<td>
							<s:text name="label.intrfc" />
						</td>
						<td>
							<s:select theme="simple" list="{}"
								headerValue="%{getText('label.please.select')}" headerKey="-1"
								id="interfaceType" name="interfaceType" cssClass="option"
								onchange="getModeList();" />
						</td>
					</tr>
					<tr class="barOption">
						<td>
							<s:text name="label.mode" />
						</td>
						<td>
							<s:hidden name="messageType" id="messageType" value="" />
							<s:select theme="simple" list="{}"
								headerValue="%{getText('label.please.select')}" headerKey="-1" id="mode"
								name="mode" cssClass="option"
								onchange="getMessageListForEdit();" />
						</td>
					</tr>
				</table>
				<br />
				<div id="playerDiv">
					<table width="100%" border="1" cellpadding="3" cellspacing="0"
						bordercolor="#CCCCCC" align="center">
						<tr>
							<th nowrap="nowrap" align="center">
								<s:text name="label.date" />
							</th>
							<th nowrap="nowrap" align="center">
								<s:text name="label.msg.txt" />
							</th>
							<th nowrap="nowrap" align="center">
								<s:text name="label.status" />
							</th>
							<th nowrap="nowrap" align="center">
								<s:text name="label.location" />
							</th>
							<th nowrap="nowrap" align="center">
								<s:text name="label.msg.for" />
							</th>
							<th nowrap="nowrap" align="center">
								<s:text name="Activity" />
							</th>
							<th nowrap="nowrap" align="center">
								<div id="subCheck"></div>
							</th>
						</tr>
						<s:if test="#session.ADV_MSG_MAP.size != 0">
							<s:iterator id="beanCart" value="#session.ADV_MSG_MAP">
								<tr>
									<s:iterator id="innerMap" value="value">
										<td>
											<s:property value="value" />
										</td>
									</s:iterator>
									<td>
										<a
											href="bo_um_advMessage_Edit_Save.action?type=plrMessage&msgId=<s:property  value="key"/>"
											id="subTrans" onclick="return confirmChanges(this.id);"> <s:text
												name="label.inactivate" /> </a>
									</td>
								</tr>
							</s:iterator>
						</s:if>
						<s:else>
							<tr>
								<td colspan="7" align="center" style="color: black;">
									<s:text name="msg.no.record.process" />
								</td>
							</tr>
						</s:else>
					</table>
				</div>
				<br />
				<div id="result"></div>
			</div>
		</div>
	</body>
</html>
<code id="headId" style="visibility: hidden">
	$RCSfile: bo_um_editAdvMsg_Menu.jsp,v $ $Revision: 1.3 $
</code>