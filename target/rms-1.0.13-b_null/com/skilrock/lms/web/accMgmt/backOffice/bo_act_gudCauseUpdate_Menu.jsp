	<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
	<%@ taglib prefix="s" uri="/struts-tags"%>	
	<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
	<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
		<link rel="stylesheet" href="<%=request.getContextPath() %>/LMSImages/css/styles.css" type="text/css"/>
		<title><%=application.getAttribute("JSP_PAGE_TITLE")%></title>
		<script type="text/javascript" src="<%=request.getContextPath() %>/com/skilrock/lms/web/common/globalJs/jquery-1.10.1.min.js"></script>
		<script>
			var path = "<%=request.getContextPath() %>";

			$(document).ready(function() {
				$("#trType").hide();
			});
		</script>
		<script type="text/javascript" src="<%=request.getContextPath() %>/com/skilrock/lms/web/accMgmt/backOffice/js/updateGovCommRate.js"></script>		
		<s:head theme="ajax" debug="false"/>
	</head>

<body >

	<%@include file="/com/skilrock/lms/web/loginMgmt/menu.jsp" %>
	<div id="wrap">
    <div id="top_form">
 	<h3>Update  <s:text name="Good_Cause"/> Rate</h3> <br />
 	
 	<s:form action="bo_act_gudCauseUpdate_Save" name="govCommForm" onsubmit="return checkFormDetails()">
	
		<table border="1" width="60%" style="text-align: left" cellpadding="2" cellspacing="0">
		
		<tr>
	 		<td colspan="2" align="center"><div id="service_error" style="color: red;text-align: center;"></div></td>
	 	</tr>
	 	
	 	<tr>
			<td width="40%" align="left"><label class="label">Select Service : </label></td>
	    	<td align="left">
					<select name="serviceName" class="option" id="serviceName" onchange="getGameListing('getGameList.action?serviceName='+ this.value)">
						<option value="-1">--Please Select--</option>
						<s:iterator value="#session.serviceNameMap">
						<option value="<s:property value="key"/>"><s:property value="value"/></option>
						</s:iterator>
					</select>
			</td>
		</tr>	

		<tr>
	 		<td colspan="2" align="center"><div id="game_error" style="color: red;text-align: center;"></div></td>
	 	</tr>
		
		<tr>
			<td width="40%" align="left"><label class="label">Game Name : </label></td>
	    	<td align="left">
					<select name="gameId" class="option" id="game_id" onchange="getCurrentGovRate(this.value)">
						<option value="-1">--Please Select--</option>
						
					</select>
			</td>
		</tr>
		<tr id="trType">
			<td width="40%" align="left"><label class="label">Select Type : </label></td>
	    	<td align="left">
				<select name="type" class="option" id="type" onchange="changeCommRate(this.value);">
					<option value="SALE">Sale</option>
					<option value="PWT">Winning</option>
				</select>
			</td>
		</tr>
		<tr><td width="40%" align="left" ><label class="label">Current  <s:text name="Good_Cause"/> Rate : </label></td>
			<td align="left">
				<input type="text" id="cGovCommRate" name="currGovRate" value="" size="8" readonly="readonly" />
		 	</td>
	 	</tr>
	 	<tr>
	 		<td colspan="2" align="left"><div id="newGovComm_error" style="color: red;text-align: center;"></div></td>
	 	</tr>
		<tr id="new"><td colspan="1" width="40%"><label class="label">New  <s:text name="Good_Cause"/> Rate : </label></td>
			<td colspan="1">
				<input type="text" id="newGovComm" name="newGovCommRate" value="" size="8" style="background-color: yellow;"/>
		 	</td>
	 	</tr>
		<tr>
		 	<td colspan="2" width="80%" align="right">
				<table><s:submit name="Update" id="submit" value="Update" cssClass="button" /></table>
		</td>
		 	
	 	</tr>	
	 	 	
	 	</table>	 	
	
	 </s:form>
	 </div></div>	
	
	
</body>
</html>
<code id="headId" style="visibility: hidden">
$RCSfile: bo_act_gudCauseUpdate_Menu.jsp,v $
$Revision: 1.4 $
</code>