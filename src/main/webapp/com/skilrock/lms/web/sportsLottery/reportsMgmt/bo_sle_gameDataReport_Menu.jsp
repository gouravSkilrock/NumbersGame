<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="java.util.Calendar"%>
<%@page import="com.skilrock.lms.common.utility.CommonMethods"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<%
	response.setHeader("Cache-Control", "no-store"); //HTTP 1.1
	response.setHeader("Pragma", "no-cache"); //HTTP 1.0
	response.setDateHeader("Expires", 0); //prevents caching at the proxy server
%>
<html>
	<head>
		<s:head theme="ajax" debug="false" />
		<meta http-equiv="Content-Type"
			content="text/html; charset=iso-8859-1" />
		<link rel="stylesheet"
			href="<%=request.getContextPath()%>/LMSImages/css/styles.css"
			type="text/css" />
		<link type="text/css"
			rel="stylesheet" href="<%=request.getContextPath() %>/LMSImages/css/lmsCalendar.css" media="screen"/>
		<title><%=application.getAttribute("JSP_PAGE_TITLE")%></title>
		<script>
			var projectName="<%=request.getContextPath()%>"
		</script>
	</head>
	<body>
		<%@include file="/com/skilrock/lms/web/loginMgmt/menu.jsp"%>
		<script type="text/javascript">
			$(document).ready(function() {
				var gameTypeData = $('#gameType').val();
				//alert('--Game Type Data--'+gameTypeData);
				var gameTypeMap = jQuery.parseJSON(gameTypeData);
				$('#gameId').change(function() {
					//var jsonString = JSON.stringify(obj[$('#gameId').val()])
					//alert(jsonString);
					if($(this).val()!=-1) {
						$('#gameTypeId').empty().append($('<option></option>').val("-1").html("--Please Select--"));
						var gameId = $(this).val();
						var gameTypeList = gameTypeMap[gameId].gameTypeMasterList;
						if (gameTypeList!=undefined && gameTypeList.length>0 ) {
							$.each(gameTypeList, function(key, gameTypeBean) {
								$('#gameTypeId').append($('<option></option>').val(gameTypeBean.gameTypeId).html(gameTypeBean.gameTypeDispName));
							});
						} else {
							$('#gameTypeId').empty().append($('<option></option>').val("-1").html("--Please Select--"));
						}
					} else {
						$('#gameTypeId').empty().append($('<option></option>').val("-1").html("--Please Select--"));
					}
				});
			});
		</script>
		<div id="wrap">
			<div id="top_form">
				<h3>
					Sports Lottery Game Data Report
				</h3>
				<s:form action="slGameDataReportSearch" onsubmit="return formSubmit(this.id,'result');" theme="simple">
					<table width="450" border="0" cellpadding="2" cellspacing="0">
						<tr>
							<td align="center" colspan="2">
								<div id="errorDiv" style="color: red;"></div>
							</td>
						</tr>
						<tr>
							<td align="right">
								Select Game Name
							</td>
							<td>
								<s:select id="gameId" headerKey="-1" headerValue="--Please Select--"
									name="gameId" list="%{gameMap}" listKey="key"
									listValue="%{value.gameDispName}" cssClass="option"></s:select>
								<s:hidden id="gameType" name="gameType" value="%{new com.skilrock.lms.coreEngine.sportsLottery.common.SportsLotteryUtils().convertJSON(gameMap)}" />
							</td>
						</tr>
						<tr>
							<td align="right">
								Select Game Type
							</td>
							<td>
								<s:select id="gameTypeId" headerKey="-1" headerValue="--Please Select--"
									name="gameTypeId" list="{}" cssClass="option"></s:select>
							</td>
						</tr>
						<tr>
							<s:set name="stDate" id="stDate" value="#session.presentDate" />
							<%
								Calendar prevCal = Calendar.getInstance();
								String startDate = CommonMethods.convertDateInGlobalFormat(new java.sql.Date(prevCal.getTimeInMillis()).toString(), "yyyy-mm-dd", "yyyy-mm-dd");
							%>
							<td align="right">
								Start Date<span>*</span>:&nbsp;
							</td>
							<td>
								<input type="text" name="startDate" id="start_date"
									value="<%=startDate%>" readonly="readonly" size="12" />
								<input type="button"
									style="width: 19px; height: 19px; background: url('<%=request.getContextPath()%>/LMSImages/imagesCal/dateIcon.gif'); top left; border: 0;"
									onclick="displayCalendar(document.getElementById('start_date'),'yyyy-mm-dd', this, '<%=startDate%>', false, '')" />
							</td>
						</tr>
						<tr>
							<td align="right">
								End Date<span>*</span>:&nbsp;
							</td>
							<td>
								<input type="text" name="endDate" id="end_date"
									value="<%=startDate%>" readonly="readonly" size="12" />
								<input type="button"
									style="width: 19px; height: 19px; background: url('<%=request.getContextPath()%>/LMSImages/imagesCal/dateIcon.gif'); top left; border: 0;"
									onclick="displayCalendar(document.getElementById('end_date'),'yyyy-mm-dd', this, '<%=startDate%>',false, '')" />
							</td>
						</tr>
						<tr>
							<td>&nbsp;</td>
							<td>
								<s:radio value="%{'DRAW_WISE'}" name="reportType" list="#{'DRAW_WISE':'Draw Wise', 'DATE_WISE':'Date Wise'}" />
							</td>
						</tr>
						<tr>
							<td colspan="2" align="right">
								<s:submit name="submit" value="Submit" align="right" cssClass="button" />
							</td>
						</tr>
					</table>
				</s:form>
				<div id="result"></div>
			</div>
		</div>
		
		<script type="text/javascript"
			src="<%=request.getContextPath() %>/com/skilrock/lms/web/common/globalJs/calender.js"></script>

		<link rel="stylesheet" href="http://code.jquery.com/ui/1.10.3/themes/smoothness/jquery-ui.css" />
		<script src="http://code.jquery.com/ui/1.10.3/jquery-ui.js"></script>
	</body>
</html>

<code id="headId" style="visibility: hidden">
	$RCSfile: bo_sle_gameDataReport_Menu.jsp,v $ $Revision: 1.1.2.2 $
</code>