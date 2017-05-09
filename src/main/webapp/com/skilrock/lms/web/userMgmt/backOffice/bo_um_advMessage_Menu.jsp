<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.Calendar"%>
<%@page import="com.skilrock.lms.common.utility.CommonMethods"%>
<%@taglib prefix="s" uri="/struts-tags"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">

	<head>
		<s:head theme="ajax" debug="false" />
		
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<link rel="stylesheet"
			href="<%=request.getContextPath()%>/LMSImages/css/styles.css"
			type="text/css" />
		<title><%=application.getAttribute("JSP_PAGE_TITLE")%></title>
		<link type="text/css" rel="stylesheet"
			href="<%=request.getContextPath()%>/LMSImages/css/lmsCalendar.css"
			media="screen" />
			<script type="text/javascript"
			src="<%=request.getContextPath() %>/com/skilrock/lms/web/common/globalJs/jquery-1.10.1.min.js"></script>
		<script>var projectName="<%=request.getContextPath()%>"</script>
		<script type="text/javascript"
			src="<%=request.getContextPath()%>/com/skilrock/lms/web/common/globalJs/calender.js"></script>
		<script type="text/javascript"
			src="<%=request.getContextPath()%>/com/skilrock/lms/web/userMgmt/backOffice/js/bo_um_advMessage.js"></script>
		<link type="text/css" rel="stylesheet"
			href="<%=request.getContextPath()%>/LMSImages/css/lmsCalendar.css"
			media="screen" />
		<script type="text/javascript">
			$(document).ready(function() {
				setChkDivStatus($("#mode option:selected" ).val());

				$("#subject").keyup(function (e) {
				var mode = $("#mode option:selected").text();
				var interfaceType = $("#interfaceType").val();
				
				if(interfaceType == 'TERMINAL')
				{
				if(mode == 'FLASH' || mode == 'INBOX')
				{
				$("#subject").val($("#subject").val().replace(/;/g, "").replace(/\//g, ""))  ;
				}
				}
			//	$("#subject").val($("#subject").val().replace(';', "").replace('/', ""))  ;
					if (e.keyCode == 13 || e.keyCode == 35 || e.keyCode == 37 || e.keyCode == 124 || e.keyCode == 126) {
						return false;
					}
				});
				
				$("#message").keyup(function (e) {
				$("#message").val($("#message").val().replace(/;/g, "").replace(/\//g, ""))  ;
					if (e.keyCode == 13 || e.keyCode == 35 || e.keyCode == 37 || e.keyCode == 124 || e.keyCode == 126) {
						return false;
					}
				});

				$("#content").keyup(function (e) {
				var mode = $("#mode option:selected").text();
				var interfaceType = $("#interfaceType").val();
				
				if(interfaceType == 'TERMINAL')
				{
				if(mode == 'FLASH' || mode == 'INBOX')
				{
				$("#content").val($("#content").val().replace(/;/g, "").replace(/\//g, ""))  ;
				}
				}
				
			//	$("#content").val($("#content").val().replace(';', "").replace('/', ""))  ;
					if (e.keyCode == 13 || e.keyCode == 35 || e.keyCode == 37 || e.keyCode == 124 || e.keyCode == 126) {	
						return false;
					}
				});

				$("#mode").change(function() {
				    $("#contentCharLeft").css('display','block');
				    $("#contentCharLeft").html('(250/250)');
				    $("#msgCharLeft").css('display','none');
					setChkDivStatus($("#mode option:selected" ).val());
					var userType = $("#orgId").val();
					var interfaceType = $("#interfaceType").val();
					var mode = $("#mode option:selected").text();
					$("#isForNewUser").prop("checked", false);
					$("input:radio[name=userSelection]:nth(1)").attr("checked", false);
					$("input:radio[name=userSelection]:nth(0)").attr("checked", true);
					$("#messageType").val($("#mode option:selected").text());
	
					$("#msgDiv2").css("display", "block");
					$("#userSelDiv").css("display", "block");
					$("#charDiv").show();
					$("#expDiv").show();
					$("#chkDiv").show();
					$("#popupDiv").show();
					$("#msgCharLeft").show();
					$("#mandatoryDiv").show();
					$("#result").html("");
					$("#regSubmit").css("display", "block");
					$("#subject").val("");
					$("#content").val("");

					if(interfaceType == 'WEB') {
						$("#charDiv").hide();
						$("#expDiv").hide();
						$("#popupDiv").hide();

						$("#subject").attr('maxlength','100');
						$("#content").attr('maxlength','250');
					} else {
						$("#subject").attr('maxlength','20');
						$("#content").attr('maxlength','250');
					}

					if(mode == 'FLASH') {
						$("#chkDiv").hide();
						$("#expDiv").show();
					} else if(mode == 'REGISTRATION') {
						if(interfaceType == 'WEB') {
							$("#popupDiv").hide();
						}
						$("#result").hide();
						$("#regSubmit").css("display", "block");
						$("#userSelDiv").css("display", "none");
					} else {
						$("#result").show();
					}

					if(mode != 'REGISTRATION') {
						var flag = false;
						var regVal = -1;
						$("#mode > option").each(function() {
							if(this.text == 'REGISTRATION') {
								flag = true;
								regVal = this.value;
							}
						});

						if(!flag || mode == 'FLASH') {
							$("#isForNewUser").prop("checked", false);
							$("#isForNewUser").css("display", "none");
							$("#isForNewUserLbl").css("display", "none");
						} else {
							$("#isForNewUser").prop("checked", true);
							$("#isForNewUser").css("display", "block");
							$("#isForNewUserLbl").css("display", "block");
						}
						$("#isForNewUser").val(regVal);
					}
				});

				$("input[name=userSelection]:radio").change(function () {
					var userType = $("#orgId").val();
					if($(this).val() == 'SEL') {
						fetchAdvRet(userType);
						$("#regSubmit").css("display", "none");
					} else {
						$("#result").html("");
						$("#regSubmit").css("display", "block");
					}
				});
			});

			function setChkDivStatus(mode) {
				if(mode == 'FLASH')
					$("#chkDiv").hide();
				else
					$("#chkDiv").show();
			}

			function getInterfaceList() {
				$("#msgDiv2").css("display", "none");
				$("#regSubmit").css("display", "none");
				$("#isPopup").prop("checked", false);
				$("#isMandatory").prop("checked", false);
				$("#isForNewUser").prop("checked", false);
				$("input:radio[name=userSelection]:nth(1)").attr("checked", false);
				$("input:radio[name=userSelection]:nth(0)").attr("checked", true);
				$("#result").html("");
				$("#subject").val("");
				$("#content").val("");

				clearAllDivs();

				var userType = $("#orgId").val();
				if(userType == 'PLAYER') {
				$("#charDiv1").show();
				$("#result").html('<img src="/LMSLinuxNew/LMSImages/images/loadingAjax.gif">Loading...');
					//var _resp = _ajaxCallText(projectName + "/com/skilrock/lms/web/userMgmt/bo_um_getInterfaceList.action?orgType="+userType);
					showMsgDiv(userType);
					fetchAdvRet(userType);
				} else {
					$("#modeDiv").css("display", "block");
					$("#interfaceType").html("");
					//$("#interfaceType").append(new Option("--Please Select--", "-1"));
					$('#interfaceType').append($('<option></option>').val('-1').html('<s:text name="label.please.select"/>'));
					$("#mode").html("");
					//$("#mode").append(new Option("--Please Select--", "-1"));
					$('#mode').append($('<option></option>').val('-1').html('<s:text name="label.please.select"/>'));
					var _resp = _ajaxCallText(projectName + "/com/skilrock/lms/web/userMgmt/bo_um_getInterfaceList.action","orgType="+userType);
					var json = JSON.parse(_resp);
					$.each(json.responseData, function(key, value) {
						//$("#interfaceType").append(new Option(value, value));
						$('#interfaceType').append($('<option></option>').val(value).html(value));
					});
					
				}
			}

			function getModeList() {
				$("#isPopup").prop("checked", false);
				$("#isMandatory").prop("checked", false);
				$("#isForNewUser").prop("checked", false);
				$("input:radio[name=userSelection]:nth(1)").attr("checked", false);
				$("input:radio[name=userSelection]:nth(0)").attr("checked", true);
				$("#result").html("");
				$("#subject").val("");
				$("#content").val("");

				var userType = $("#orgId").val();
				var interfaceType = $("#interfaceType").val();
				$("#mode").html("");
				//$("#mode").append(new Option("--Please Select--", "-1"));
				$('#mode').append($('<option></option>').val('-1').html('<s:text name="label.please.select"/>'));
				var _resp = _ajaxCallText(projectName + "/com/skilrock/lms/web/userMgmt/bo_um_getModeList.action?orgType="+userType+"&interfaceType="+interfaceType);
				var json = JSON.parse(_resp);
				$.each(json.responseData, function(key, value) {
					//$("#mode").append(new Option(value, key));
					$('#mode').append($('<option></option>').val(key).html(value));
				});
			}
		</script>
	</head>

	<body onload="fillHrMinSec();">
		<%@include file="/com/skilrock/lms/web/loginMgmt/menu.jsp"%>
		<div id="wrap">
			<div id="top_form">
				<h3>
					<s:text name="label.adv.msg.entry" />
				</h3>
				<div id="tktErrMsgDiv"></div>
				<s:form name="advMessage" id="advMessage"
					action="bo_um_advMessage_Submit" onsubmit="return validate()">
					<s:token name="token" />
					<input type="hidden" id="serviceType" name="serviceType"
						value="blank"></input>
					<div id="errMsgDiv">
						<s:actionerror />
					</div>
					<table id="retTable" border="0" cellpadding="1" cellspacing="0"
						bordercolor="#CCCCCC" width="100%">
						<tr>
							<td colspan="2">
								<s:text name="label.slct.msg.for" />
								<s:select theme="simple" list="userTypeMap"
									key="label.slct.msg.for" id="orgId" name="orgType"
									cssClass="option" headerKey="-1"
									headerValue="%{getText('label.please.select')}" onchange="getInterfaceList(); "></s:select>
							</td>
						</tr>
						<tr>
							<td colspan="2">
								<div id="serviceDiv" style="display: none;">
									<table>
										<tr>
											<td>
												<s:text name="label.slct.service" />
											</td>
											<td>
												<s:select theme="simple"
													list="#application.SERVICES_CODE_NAME_MAP" id="serviceId"
													name="serviceId" onchange="onServiceChng(this.value);"
													headerKey="-1" headerValue="%{getText('label.please.select')}"
													cssClass="option" />
											</td>
										</tr>
									</table>
								</div>
							</td>
						</tr>
						<tr>
							<td colspan="2">
								<div id="modeDiv">
									<table>
										<tr>
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
										<tr>
											<td>
												<s:text name="label.msg.type" />
											</td>
											<td>
												<s:hidden name="messageType" id="messageType" value="" />
												<s:select theme="simple" list="{}"
													headerValue="%{getText('label.please.select')}" headerKey="-1" id="mode"
													name="mode" cssClass="option" />
											</td>
										</tr>
									</table>
								</div>
							</td>
						</tr>
						<%
							Calendar prevCal = Calendar.getInstance();
								//prevCal.add(Calendar.DAY_OF_MONTH, 1);
								String startDate = CommonMethods
										.convertDateInGlobalFormat(new java.sql.Date(prevCal
												.getTimeInMillis()).toString(), "yyyy-mm-dd",
												"yyyy-mm-dd");
						%>
						<tr>
							<td colspan="2">
								<div id="msgDiv1" style="display: none;">
								<div id="charDiv1" style="display: none;">
										<i>(<s:text name="msg.do.not.use.these.chars.sub.content" />
											;, /)</i>
									</div>
									<s:text name="Advertisement_Message" />
									<s:textarea theme="simple" key="Advertisement_Message"
										name="message" id="message" cols="50" rows="2" 
										onkeydown="return chklimit(this.value);"
										onkeyup="return chklimit(this.value);"></s:textarea>
								</div>
								<div id="msgDiv2" style="display: none;">
									<div id="charDiv" style="display: none;">
										<i>(<s:text name="msg.do.not.use.these.chars.sub.content" />
											~, #, |, ;, /, %)</i>
									</div>
									<br />
									<s:text name="label.subject" />
									<s:textfield theme="simple" key="Advertisement_Message"
										name="messageSubject" id="subject" size="25" maxlength="100"
										onkeydown="return chklimitSubject(this.value);"
										onkeyup="return chklimitSubject(this.value);"></s:textfield><div id="msgCharLeft"></div>
							
									<br />
									<s:text name="label.content" />
									<s:textarea theme="simple" name="messageContent" id="content"
										onkeyup="return checkContentLimit();"
										onkeydown="return checkContentLimit();"></s:textarea>
									<!--<div id="contentCharLeft">(250/250)</div>-->
									<div id="contentCharLeft"></div>
									<br />
									<div id="chkDiv">
										<div id="popupDiv">
											<s:checkbox theme="simple" name="isPopup" id="isPopup" />
											<label for="isPopup">
												<s:text name="label.popup" />
											</label>
										</div>
										<div id="mandatoryDiv">
											<s:checkbox theme="simple" name="isMandatory"
												id="isMandatory" />
											<label for="isMandatory">
												<s:text name="label.mndtry" />
											</label>
										</div>
									</div>
									<div id="expDiv">
										<s:text name="label.expry.prd" />
										<input type="text" name="expiryDate" id="start_date"
											value="<%=startDate%>" readonly size="12" />
										<input type="button"
											style="width: 19px; height: 19px; background: url('<%=request.getContextPath()%>/LMSImages/imagesCal/dateIcon.gif'); top left; border: 0;"
											onclick="displayCalendar(document.getElementById('start_date'),'yyyy-mm-dd', this, '<%=startDate%>', '<%=startDate%>', '')" />
										<select name="expHr" id="expHr" class="option"></select>
										<select name="expMin" id="expMin" class="option"></select>
										<select name="expSec" id="expSec" class="option"></select>
										<br />
									</div>
									<div id="userSelDiv" style="display: none;">
										<s:radio
											list="#{'ALL':getText('label.all.organization'), 'SEL':getText('label.slctd.organizations')}"
											value="%{'ALL'}" name="userSelection" cssClass="userSel"
											theme="simple"></s:radio>
										<br />
										<input type="checkbox" name="isForNewUser" id="isForNewUser"
											value="-1" checked="checked" />
										<label for="isForNewUser" id="isForNewUserLbl">
											<s:text name="label.nw.rgstd.usr" />
										</label>
									</div>
								</div>
							</td>
						</tr>
						<tr>
							<!--<td colspan="2">
								<div id="msgCharLeft"></div>
							</td>
						--></tr>
						<tr>
							<td colspan="2">
								<div id="gamesDiv" style="display: none;">
									<s:text name="label.slct.game" />
									<s:iterator value="%{drawSerList}">
										<s:checkbox name="gameNo" theme="simple" label="%{value}"
											fieldValue="%{key}"></s:checkbox>
										<s:property value="%{value}" />
									</s:iterator>
								</div>
								<div id="sleGameDiv" style="display: none;">
									<s:text name="label.slct.game" />
									<s:iterator value="%{sleServiceMap}">
										<s:checkbox name="sleGameNo" theme="simple" label="%{value}"
											fieldValue="%{key}"></s:checkbox>
										<s:property value="%{value}" />
									</s:iterator>
								</div>
								<div id="walletDiv" style="display: none;">
									<s:text name="label.slct.wallet" />
									<s:iterator value="%{olaSerList}">
										<s:checkbox name="walletNo" theme="simple" label="%{value}"
											fieldValue="%{key}"></s:checkbox>
										<s:property value="%{value}" />
									</s:iterator>
								</div>
								<div id="iwGameDiv" style="display: none;">
									<s:text name="label.slct.game" />
									<s:iterator value="%{iwServiceMap}">
										<s:checkbox name="iwGameNo" theme="simple" label="%{value}" fieldValue="%{key}"></s:checkbox>
										<s:property value="%{value}" />
									</s:iterator>
								</div>
								<div id="vsGameDiv" style="display: none;">
									<s:text name="label.slct.game" />
									<s:iterator value="%{vsServiceMap}">
										<s:checkbox name="vsGameNo" theme="simple" label="%{value}" fieldValue="%{key}"></s:checkbox>
										<s:property value="%{value}" />
									</s:iterator>
								</div>
							</td>
						</tr>
						<tr>
							<td colspan="2">
								<div id="categoryDiv" style="display: none;">
									<s:text name="label.slct.cat" />
									<s:iterator value="%{commSerList}">
										<s:checkbox name="categoryNo" theme="simple" label="%{value}"
											fieldValue="%{key}"></s:checkbox>
										<s:property value="%{value}" />
									</s:iterator>
								</div>
							</td>
						</tr>
						<tr>
							<td colspan="2">
								<div id="searchTypeDiv" style="display: none;">
									<s:text name="label.srch.type" />

									<s:radio theme="simple"
										list="#{'AgentWise':getText('label.agt.wise'),'LocationWise':getText('label.location')}"
										name="search_type" id="search_type" value="'AgentWise'"
										label="Search Type" onclick="fetchAdvRet(this.value)" />
								</div>
							</td>
						</tr>
						<tr>
							<td colspan="2">
								<div id="msgLocDiv" style="display: none;">
									<s:text name="label.msg.location" />
									<s:select theme="simple"
										list="#{'TOP':getText('label.top.of.tkt'),'BOTTOM':getText('label.bottom.of.tkt')}"
										id="msgLoc" name="msgLocation" cssClass="option"
										headerKey="-1" headerValue="%{getText('label.please.select')}" />
								</div>
							</td>
						</tr>
						<tr>
							<td colspan="2">
								<div id="activityDiv" style="display: none;">
									<s:text name="Activity" />

									<s:select theme="simple"
										list="#{'ALL':getText('label.ALL'), 'SALE':getText('SALE'), 'PWT':getText('PWT'), 'CANCEL':getText('CANCEL')}"
										id="activity" name="activity" cssClass="option" />
								</div>
							</td>
						</tr>
						<tr>
							<td colspan="2">
								<div id="activityDiv1" style="display: none">
									<s:text name="Activity" />
									<s:select theme="simple"
										list="#{'Instant':getText('label.instant'), 'Draw Perform':getText('label.draw.perform'), 'Scheduled':getText('label.schedule')}"
										id="activity1" name="activity1" cssClass="option" />
								</div>
							</td>
						</tr>
						<tr>
							<td colspan="2">
								<div id="activityDiv2" style="display: none">
									<s:text name="Activity" />
									<s:select theme="simple" list="#{'SALE':getText('SALE')}" id="activity2"
										name="activity2" cssClass="option" />
								</div>
							</td>
						</tr>
						<tr>
							<td colspan="2">
								<div id="activityDiv3" style="display: none">
									<s:text name="Activity" />
									<s:select theme="simple"
										list="#{'DEPOSIT':getText('DEPOSIT'), 'WITHDRAW':getText('WITHDRAW')}"
										id="activity3" name="activity3" cssClass="option" />
								</div>
							</td>
						</tr>
						<tr>
							<td colspan="2">
								<div id="regSubmit" style="display: none;">
									<s:submit name="submit" key="btn.submit" theme="simple"
										cssClass="button" />
								</div>
							</td>
						</tr>
					</table>
					<div id="result"></div>
				</s:form>
			</div>
		</div>
	</body>
</html>

<code id="headId" style="visibility: hidden">
	$RCSfile: bo_um_advMessage_Menu.jsp,v $ $Revision: 1.3 $
</code>