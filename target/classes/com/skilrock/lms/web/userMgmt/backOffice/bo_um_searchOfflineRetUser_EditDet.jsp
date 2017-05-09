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

		<s:head theme="ajax" debug="false" />
	</head>
	<script>
	function fetchFileUpload(){
	_ajaxCall('<%=request.getContextPath()%>/com/skilrock/lms/web/drawGames/drawMgmt/bo_dm_searchOfflineRetUser_FileUpload.action?retUserId=<s:property value="%{#session.USER_BEAN.userId}"/>','fileUploadDiv')
	}
	function mergeFile(refNo,status,retUserId,gameNo){
		_ajaxCall('<%=request.getContextPath()%>/com/skilrock/lms/web/drawGames/drawMgmt/bo_dm_fileMerge.action?fileRefId='+refNo+'&status='+status+'&retUserId='+retUserId+'&gameNo='+gameNo,refNo);
	}
	function declineFile(refNo,gameNo){
		//alert('decline');
		_ajaxCall('<%=request.getContextPath()%>/com/skilrock/lms/web/drawGames/drawMgmt/bo_dm_declineFile.action?fileRefId='+refNo+'&gameNo='+gameNo,refNo);
	}
	</script>
	<body onload="fetchFileUpload()">
		<%@include file="/com/skilrock/lms/web/loginMgmt/menu.jsp"%>

		<div id="wrap">

			<div id="top_form">
				<h3>
					User Details
				</h3>
			


				<s:form>

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
						<s:select label="Offline Status" name="offlineStatus" cssStyle="background-color:  #F9FCCD" list="{'ACTIVE','INACTIVE'}" value="%{#session.USER_BEAN.offlineStatus}">
						</s:select>
						<s:select label="Is Offline" name="isOffline" cssStyle="background-color:  #F9FCCD" list="{'YES','NO'}" value="%{#session.USER_BEAN.isOffline}">
						</s:select>
						<tr>
							<td>
							</td>
							<td align="left">
								<table>
									<s:submit value="Update Changes" action="bo_um_searchOfflineRetUser_Save"  align="center" cssClass="button" />
								</table>
							</td>
						</tr>
					</table>
				</s:form>
				<table border="0" cellpadding="2" cellspacing="0" width="100%">
				<tr><td align="center"><s:div id="fileUploadDiv"></s:div></td></tr>
				</table>
				
				
			</div>
		</div>
	</body>
</html>

<code id="headId" style="visibility: hidden">
$RCSfile: bo_um_searchOfflineRetUser_EditDet.jsp,v $
$Revision: 1.2 $
</code>