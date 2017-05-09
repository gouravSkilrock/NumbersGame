<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="com.skilrock.lms.beans.UserInfoBean"%>
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
		<script type="text/javascript" src="<%=request.getContextPath()%>/com/skilrock/lms/web/userMgmt/backOffice/js/ipTimeLimit.js"></script>
		<s:head theme="ajax" debug="false" />
	</head>
	<body onload="fillDates(); ">
		<%@include file="/com/skilrock/lms/web/loginMgmt/menu.jsp"%>
		<div id="wrap">
			<div id="top_form">
				<h3>
					User Login Time Limit Assignment
				</h3>
				<s:form name="bo_um_ipTimeLimit_Save" action="bo_um_ipTimeLimit_Save" onsubmit="return setData();" theme="simple">
				<s:radio name="type" id="selCriteria" list="#{'SU':'Single User','MU':'Multiple User'}" value="%{'SU'}" onchange="toggleUserSel(this.value);" />
				<s:hidden name="status" id="status" value="ACTIVE" />
					<div id="singleUser">
						<table>
							<tr>
								<td>
									Select Organization Type
								</td>
								<!-- 
								<td>
									<s:select name="orgType" id="orgTypeSingle" list="{'BO', 'AGENT', 'RETAILER'}"
									headerKey="-1" headerValue="--Please Select--"
									cssClass="option" onchange="getOrgListing(this.value, 'SU');" />
								</td>
								 -->
								 <!-- This code is commented because , in Lagos they dont want to see this option becuase they ned to hand over the priv to some one elase when they are not in office -->
								 <td>
									<s:select name="orgType" id="orgTypeSingle" list="{'BO'}"
									headerKey="-1" headerValue="--Please Select--"
									cssClass="option" onchange="getOrgListing(this.value, 'SU');" />
								</td>
								<td>
									<div id="errOrgTypeSingle" style="display:none;">
										<label style="color:red;">Please Select Organization Type.</label>
									</div>
								</td>
							</tr>
							<tr>
								<td colspan="2">
									<div id="resultSU"></div>
								</td>
								<td>
									<div id="errRetListSingle" style="display:none;">
										<label style="color:red;">Please Select Organization.</label>
									</div>
								</td>
							</tr>
							<tr>
								<td colspan="3">
									&nbsp;
								</td>
							</tr>
						</table>
					</div>
					<div id="multipleUser" style="display:none;">
						<table>
							<tr>
								<td>
									Select Organization Type
								</td>
								<!-- <td>
									<s:select name="orgType" id="orgTypeMultiple" list="{'BO','AGENT','RETAILER'}"
									headerKey="-1" headerValue="--Please Select--"
									cssClass="option" onchange="getOrgListing(this.value, 'MU');" />
								</td> -->
								<!-- This code is commented because , in Lagos they dont want to see this option becuase they ned to hand over the priv to some one elase when they are not in office -->
								<td>
									<s:select name="orgType" id="orgTypeMultiple" list="{'BO'}"
									headerKey="-1" headerValue="--Please Select--"
									cssClass="option" onchange="getOrgListing(this.value, 'MU');" />
								</td>
								
								<td>
									<div id="errOrgTypeMultiple" style="display:none;">
										<label style="color:red;">Please Select Organization Type.</label>
									</div>
								</td>
							</tr>
							<tr>
								<td colspan="3">
									<div id="errRetListMultiple" style="display:none;">
										<label style="color:red;">Please Select Atleast One Organization.</label>
									</div>
								</td>
							</tr>
							<tr>
								<td colspan="3">
									<div id="resultMU"></div>
								</td>
							</tr>
							<tr>
								<td colspan="3">
									&nbsp;
								</td>
							</tr>
						</table>
					</div>
					<div id="tblDiv" style="display:none;">
						<table width="65%" border="1" cellpadding="3" cellspacing="0" bordercolor="#CCCCCC" align="left" id="tbl">
							<tr>
								<td colspan="2">
									Remove Time Limit Restriction <input type="checkbox" id="cbRst" onchange="toggleRestriction(this.checked);" />
								</td>
								<td colspan="2">
									<div id="restMsgDiv" style="display:none;">
										<center>No Time Restrictions.</center>
									</div>
								</td>
							</tr>
							<tr>
								<th align="center">&nbsp;</th>
								<th align="center">Start Time</th>
								<th align="center">End Time</th>
								<th align="center">&nbsp;<s:hidden name="timing" id="timing" /></th>
							</tr>
							<tr>
								<th align="center">Monday</th>
								<td>
									<select name="monStartHr" id="monStartHr" class="option" onchange="dispTiming('mon');"></select> :
									<select name="monStartMin" id="monStartMin" class="option" onchange="dispTiming('mon');"></select>
								</td>
								<td>
									<select name="monEndHr" id="monEndHr" class="option" onchange="dispTiming('mon');"></select> :
									<select name="monEndMin" id="monEndMin" class="option" onchange="dispTiming('mon');"></select>
								</td>
								<td align="center">
									<div id="monDispTiming">
										<label id="monStartDispTiming">00:00 To </label><label id="monEndDispTiming">00:00</label>
									</div>
								</td>
							</tr>
							<tr>
								<th align="center">Tuesday</th>
								<td>
									<select name="tueStartHr" id="tueStartHr" class="option" onchange="dispTiming('tue');"></select> :
									<select name="tueStartMin" id="tueStartMin" class="option" onchange="dispTiming('tue');"></select>
								</td>
								<td>
									<select name="tueEndHr" id="tueEndHr" class="option" onchange="dispTiming('tue');"></select> :
									<select name="tueEndMin" id="tueEndMin" class="option" onchange="dispTiming('tue');"></select>
								</td>
								<td align="center">
									<div id="tueDispTiming">
										<label id="tueStartDispTiming">00:00 To </label><label id="tueEndDispTiming">00:00</label>
									</div>
								</td>
							</tr>
							<tr>
								<th align="center">Wednesday</th>
								<td>
									<select name="wedStartHr" id="wedStartHr" class="option" onchange="dispTiming('wed');"></select> :
									<select name="wedStartMin" id="wedStartMin" class="option" onchange="dispTiming('wed');"></select>
								</td>
								<td>
									<select name="wedEndHr" id="wedEndHr" class="option" onchange="dispTiming('wed');"></select> :
									<select name="wedEndMin" id="wedEndMin" class="option" onchange="dispTiming('wed');"></select>
								</td>
								<td align="center">
									<div id="wedDispTiming">
										<label id="wedStartDispTiming">00:00 To </label><label id="wedEndDispTiming">00:00</label>
									</div>
								</td>
							</tr>
							<tr>
								<th align="center">Thursday</th>
								<td>
									<select name="thuStartHr" id="thuStartHr" class="option" onchange="dispTiming('thu');"></select> :
									<select name="thuStartMin" id="thuStartMin" class="option" onchange="dispTiming('thu');"></select>
								</td>
								<td>
									<select name="thuEndHr" id="thuEndHr" class="option" onchange="dispTiming('thu');"></select> :
									<select name="thuEndMin" id="thuEndMin" class="option" onchange="dispTiming('thu');"></select>
								</td>
								<td align="center">
									<div id="thuDispTiming">
										<label id="thuStartDispTiming">00:00 To </label><label id="thuEndDispTiming">00:00</label>
									</div>
								</td>
							</tr>
							<tr>
								<th align="center">Friday</th>
								<td>
									<select name="friStartHr" id="friStartHr" class="option" onchange="dispTiming('fri');"></select> :
									<select name="friStartMin" id="friStartMin" class="option" onchange="dispTiming('fri');"></select>
								</td>
								<td>
									<select name="friEndHr" id="friEndHr" class="option" onchange="dispTiming('fri');"></select> :
									<select name="friEndMin" id="friEndMin" class="option" onchange="dispTiming('fri');"></select>
								</td>
								<td align="center">
									<div id="friDispTiming">
										<label id="friStartDispTiming">00:00 To </label><label id="friEndDispTiming">00:00</label>
									</div>
								</td>
							</tr>
							<tr>
								<th align="center">Saturday</th>
								<td>
									<select name="satStartHr" id="satStartHr" class="option" onchange="dispTiming('sat');"></select> :
									<select name="satStartMin" id="satStartMin" class="option" onchange="dispTiming('sat');"></select>
								</td>
								<td>
									<select name="satEndHr" id="satEndHr" class="option" onchange="dispTiming('sat');"></select> :
									<select name="satEndMin" id="satEndMin" class="option" onchange="dispTiming('sat');"></select>
								</td>
								<td align="center">
									<div id="satDispTiming">
										<label id="satStartDispTiming">00:00 To </label><label id="satEndDispTiming">00:00</label>
									</div>
								</td>
							</tr>
							<tr>
								<th align="center">Sunday</th>
								<td>
									<select name="sunStartHr" id="sunStartHr" class="option" onchange="dispTiming('sun');"></select> :
									<select name="sunStartMin" id="sunStartMin" class="option" onchange="dispTiming('sun');"></select>
								</td>
								<td>
									<select name="sunEndHr" id="sunEndHr" class="option" onchange="dispTiming('sun');"></select> :
									<select name="sunEndMin" id="sunEndMin" class="option" onchange="dispTiming('sun');"></select>
								</td>
								<td align="center">
									<div id="sunDispTiming">
										<label id="sunStartDispTiming">00:00 To </label><label id="sunEndDispTiming">00:00</label>
									</div>
								</td>
							</tr>
							<tr>
								<td align="center" colspan="4">
									<s:submit value="Submit" cssClass="button" />
								</td>
							</tr>
						</table>
					</div>
				</s:form>
			</div>
		</div>
	</body>
</html>
<code id="headId" style="visibility: hidden">
	$RCSfile: bo_um_ipTimeLimit_Menu.jsp,v $ $Revision: 1.2 $
</code>