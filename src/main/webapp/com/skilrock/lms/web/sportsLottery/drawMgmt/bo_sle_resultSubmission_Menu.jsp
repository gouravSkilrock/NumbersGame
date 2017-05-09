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
		<title><%=application.getAttribute("JSP_PAGE_TITLE")%></title>
		<script>
			var projectName="<%=request.getContextPath()%>"
		</script>
		<script type="text/javascript"
			src="<%=request.getContextPath()%>/com/skilrock/lms/web/common/globalJs/jquery-1.10.1.min.js"></script>
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
	</head>
	<body>
		<%@include file="/com/skilrock/lms/web/loginMgmt/menu.jsp"%>
		<div id="wrap">
			<div id="top_form">
				<h3>
					Sports Game Result Submission
				</h3>
				<s:form action="resultSubmissionDrawEventSearch" onsubmit="return formSubmit(this.id,'down');" theme="simple">
					<table width="450" border="0" cellpadding="2" cellspacing="0">
						<tr>
							<td align="center" colspan="2">
								<div id="errorDiv" style="color: red;"></div>
							</td>
						</tr>
						<tr>
							<td align="left">
								Select Game Name
							</td>
							<td align="left">
								<s:select id="gameId" headerKey="-1" headerValue="--Please Select--"
									name="gameId" list="%{gameMap}" listKey="key"
									listValue="%{value.gameDispName}" cssClass="option"></s:select>
								<s:hidden id="gameType" name="gameType" value="%{new com.skilrock.lms.coreEngine.sportsLottery.common.SportsLotteryUtils().convertJSON(gameMap)}" />
							</td>
						</tr>
						<tr>
							<td align="left">
								Select Game Type
							</td>
							<td align="left">
								<s:select id="gameTypeId" headerKey="-1" headerValue="--Please Select--"
									name="gameTypeId" list="{}" cssClass="option"></s:select>
							</td>
						</tr>
						<tr>
							<td>
								<s:submit name="submit" value="Submit" align="right" cssClass="button" />
							</td>
						</tr>
					</table>
				</s:form>
				<div id="down"></div>
				<div id="result"></div>
			</div>
		</div>
	</body>
</html>

<code id="headId" style="visibility: hidden">
	$RCSfile: bo_sle_resultSubmission_Menu.jsp,v $ $Revision: 1.1.2.1 $
</code>