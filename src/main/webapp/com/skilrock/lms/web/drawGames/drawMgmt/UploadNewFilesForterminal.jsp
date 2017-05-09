<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@page import="com.skilrock.lms.common.utility.CommonMethods"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	<head>
		<s:head theme="ajax" debug="false" />
		<meta http-equiv="Content-Type"
			content="text/html; charset=iso-8859-1" />
		<link rel="stylesheet"
			href="<%=request.getContextPath()%>/LMSImages/css/styles.css"
			type="text/css" />
		<title><%=application.getAttribute("JSP_PAGE_TITLE")%></title>

		<link type="text/css" rel="stylesheet"
			href="<%=request.getContextPath()%>/LMSImages/css/lmsCalendar.css"
			media="screen" />
		<script>var projectName="<%=request.getContextPath()%>"</script>
		<script type="text/javascript"
			src="<%=request.getContextPath()%>/com/skilrock/lms/web/common/globalJs/calender.js"></script>
		<script type="text/javascript">
		
		
	function fillFormItems(val){
	_id.o('IngenicoDiv').style.display = 'none';
	_id.o('IngenicoDiv1').style.display = 'none';
	_id.o('CommonDiv').style.display = 'none';
	_id.o("error").innerHTML="";
	_id.i('down','');

		if(val.indexOf('iCT220')!=-1 || val.indexOf('iWL220')!=-1 || val.indexOf('TPS300')!=-1 || val.indexOf('TPS800')!=-1){
			_id.o('CommonDiv').style.display = 'block';
			}
		else if(val.indexOf('EFT930')!=-1){
			_id.o('IngenicoDiv').style.display = 'block';
			_id.o('IngenicoDiv1').style.display = 'block';
			}
			getTerminalVersion();
		}
			
	function validateFormEntries() {
	var devType=_id.o("deviceType");
	var ingen = _id.o("sgnFileUploader");
	var ingen1 = _id.o("adfFileUploader");
	var ict  =_id.o("agnFileUploader");

	    if (ingen.value == "" && ingen1.value == "" && ict.value == "") {
		_id.i("error", "Please Select The Files To Be Uploaded .", "e");
		return false;
		} 
		if(_id.o("version").value == ""){
		_id.i("error", "Please Select The Version .", "e");
		return false;
		}
		if(_respData!='Not available' && _id.o("version").value<=_respData){
		_id.i("error", "Please Enter The Correct Version .", "e");
		return false;
		}
		return true;
		}
	
	function getTerminalVersion(){
	var devType=_id.o("deviceType").value;
	var proName=_id.o("profileName").value;
	var item=_id.o("itemNames").value;
	var url = "get_terminal_version.action?deviceType="+devType+"&profileName="+proName+"&itemNames="+item;
	var _resp = _ajaxCall(url);
		_respData=_resp.data;
		_id.o("verDivId").innerHTML="Current Version-->"+_respData;
		}

	</script>
	</head>
	<body>
		<%@include file="/com/skilrock/lms/web/loginMgmt/menu.jsp"%>
		<div id="wrap">
			<div id="top_form">
				<h3>
					File Upload For Terminal
				</h3>
				<s:form name="UploadFile"
					action="bo_dg_uploadFileForTerminal_Search" method="post"
					onsubmit=" return validateFormEntries()"
					enctype="multipart/form-data">

					<table border="0" cellpadding="2" cellspacing="0" width="400"
						style="width: 100%; vertical-align: middle;">
						<tr>
							<td align="left" colspan="2">
								<div id="error"></div>
							</td>
						</tr>
						<tr>
							<td colspan="3">
								Device Type :
								<s:select name="deviceType" id="deviceType" theme="simple"
									list="#session.LIST_OF_DEVICES" cssClass="option"
									onchange="fillFormItems(this.value)" />
							</td>
						</tr>
						<tr>
							<td colspan="3">
								Profile Name:
								<s:select name="profileName" id="profileName" theme="simple"
									list="#session.LIST_OF_PROFILES" cssClass="option"
									onchange="getTerminalVersion()" />
							</td>
						</tr>
						<tr>
							<td colspan="1">
								Item Name:
								<s:select name="itemNames" id="itemNames" theme="simple"
									list="#session.LIST_OF_ITEMS" cssClass="option"
									onchange="getTerminalVersion()" />
							</td>
							<td colspan="2">
								<div id="verDivId"></div>
							</td>
						</tr>
						<tr>
							<td>
								Enter The Version
								<s:textfield name="version" theme="simple" id="version"
									required="true" size="5" onkeypress="return onlyDecimal(this)" />
							</td>
						</tr>
						<tr>
							<td colspan="3">
								<div id="isMand">
									Is Mandatory :
									<s:select name="isMandatory" id="isMandatory" theme="simple"
										list="#{'1':'NO','2':'YES'}" cssClass="option" />
								</div>
							</td>
						</tr>
						<tr>
							<td colspan="3">
								<div id="statusType">
									Version Status :
									<s:select name="status" id="status" theme="simple"
										list="#{'1':'ACTIVE','2':'INACTIVE'}" cssClass="option" />
								</div>
							</td>
						</tr>
						<tr>
							<td colspan="2">
								<div id="IngenicoDiv" style="display: none;">
									Select
									<b><u>SGN File</u>
									</b>
									<s:file name="sgnFileUploader" theme="simple"
										id="sgnFileUploader" required="true" accept="text/*" />
								</div>

							</td>
						</tr>
						<tr>
							<td colspan="2">
								<div id="IngenicoDiv1" style="display: none;">
									Select
									<b><u>ADF File</u> </b>
									<s:file name="adfFileUploader" theme="simple"
										id="adfFileUploader" required="true" accept="text/*" />
								</div>
							</td>
						</tr>
						<tr>
							<td colspan="2">
								<div id=CommonDiv style="display: none;">
									Select
									<b><u>File</u> </b>
									<s:file name="agnFileUploader" theme="simple"
										id="agnFileUploader" required="true" />
								</div>
							</td>
						</tr>
						<tr>
							<td colspan="3">
								<s:submit name="search" value="Upload" align="center" />
							</td>
						</tr>
					</table>
				</s:form>
				<div id="down" style="text-align: center"></div>
				<br />
				<br />
			</div>
		</div>
	</body>
</html>

<code id="headId" style="visibility: hidden">
	$RCSfile: UploadNewFilesForterminal.jsp,v $ $Revision: 1.2 $
</code>