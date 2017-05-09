<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
	
	<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
	<link rel="stylesheet" href="<%=request.getContextPath()%>/LMSImages/css/styles.css"  type="text/css"/>
	<title><%=application.getAttribute("JSP_PAGE_TITLE")%></title>
	<script type="text/javascript" src="<%=request.getContextPath()%>/com/skilrock/lms/web/ola/js/commonOla.js"></script>
	<s:head theme="ajax" debug="false" />
	<link type="text/css" rel="stylesheet" href="<%=request.getContextPath()%>/LMSImages/css/lmsCalendar.css" media="screen"/>
		<script>var projectName="<%=request.getContextPath()%>"</script>
		<script type="text/javascript" src="<%=request.getContextPath()%>/com/skilrock/lms/web/common/globalJs/calender.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/com/skilrock/lms/web/ola/js/withdrawal.js"></script>
</head>
<body>
	<%@include file="/com/skilrock/lms/web/loginMgmt/menu.jsp"%>
			<div id="wrap">
			<div id="top_form">
				<h1> Approve Withdrawal  </h1>
			<s:form action="olaWithdrawalApproval_Search" name="withdrawalApprove" id="withdrawalApprove" onsubmit="return validate()">
				<table width="450" border="0" cellpadding="2" cellspacing="0">
				<tr>
				<td>
				<div id="dateMsg"></div>
				</td>
				</tr>
				<tr>
						<td><label class="label">
									Select Start Date
										<span><font color="RED">*</font> </span>:&nbsp;
									</label></td>
							<td>
							<input type="text" name="startDate" id="startDate"
									value="<s:property value="#session.presentDate"/>" readonly
									size="12" onchange="resetType()" />
								<input type="button"
									style=" width:19px; height: 19px; background: url('<%=request.getContextPath()%>/LMSImages/imagesCal/dateIcon.gif'); top left; border:0 ; "
									onclick="displayCalendar(document.getElementById('startDate'),'yyyy-mm-dd', this, '<s:property value="#session.presentDate"/>',false, '<s:property value="#session.presentDate"/>')" />
							</td>
									
				</tr>
				<tr>
						<td><label class="label">
								Select	End Date
										<span><font color="RED">*</font> </span>:&nbsp;
									</label></td>
							
							<td>
							<input type="text" name="endDate" id="endDate"
									value="<s:property value="#session.presentDate"/>" readonly
									size="12" onchange="resetType()" />
								<input type="button"
									style=" width:19px; height: 19px; background: url('<%=request.getContextPath()%>/LMSImages/imagesCal/dateIcon.gif'); top left; border:0 ; "
									onclick="displayCalendar(document.getElementById('endDate'),'yyyy-mm-dd', this, '<s:property value="#session.presentDate"/>',false, '<s:property value="#session.presentDate"/>')" />
							</td>
				</tr>
				<tr>
				<td>
				<div id="modeMsg"></div>
				</td>
				</tr>
				<tr>
				<td>
							<s:select id="transferMode" name="transferMode" list="#{'ALL':'ALL','ONLINE':'ONLINE','CHEQUE':'CHEQUE'}" label="Transfer Mode" headerValue="--Please Select--" headerKey="-1" 
							cssClass="option" required="true" onchange="" ></s:select>
							</td>
				</tr>
				<tr>
				<td>
				<div id="statusMsg"></div>
				</td>
				</tr>
				<tr>
				<td>
							<s:select id="status" name="status" list="#{'ALL':'ALL','DONE':'DONE','PENDING':'PENDING'}" label="Select Status" headerValue="--Please Select--" headerKey="-1" 
							cssClass="option" required="true" onchange="" ></s:select>
							</td>
				</tr>
				<tr>
							<td>
								<s:submit name="search" value="Search" align="right"
									targets="down" theme="ajax" cssClass="button"
									onclick=""/>
							</td>
						</tr>
				
				</table>
			</s:form>
			<div id="down"></div>
			<div id="result" style="overflow-x:auto;overflow-y:hidden;"></div>
			</div>
			</div>

</body>

</html>
<code id="headId" style="visibility: hidden">
$RCSfile: bo_ola_approve_withdrawal_menu.jsp,v $
$Revision: 1.2 $
</code>