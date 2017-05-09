<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="com.skilrock.lms.common.utility.FormatNumber"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
	<head>
		<title><%=application.getAttribute("JSP_PAGE_TITLE")%></title>
		<script type="text/javascript">
			$(document).ready(function() {
				var drawTypeData = $('#drawType').val();
				var drawTypeList = jQuery.parseJSON(drawTypeData);
				$('#drawId').change(function() {
					if($(this).val()!=-1) {
						$('#eventId').empty().append($('<option></option>').val("-1").html("--Please Select--"));
						var drawId = $(this).prop("selectedIndex");
						//alert('drawId Index - '+drawId)-1;
						var eventList = drawTypeList[drawId-1].eventMasterList;
						//var eventList = drawTypeList[0].eventMasterList;
						if (eventList!=undefined && eventList.length>0 ) {
							var eventHtml = "";
							$.each(eventList, function(key, eventBean) {
								eventHtml += eventBean.eventDisplay;
								var eventOptionsList = eventBean.eventOptionsList;
								if (eventOptionsList!=undefined && eventOptionsList.length>0 ) {
									$.each(eventOptionsList, function(key, optionName) {
										eventHtml += "<input type='radio' checked='true' name='opt"+eventBean.eventId+"'value='"+optionName+"' /> "+optionName;
									});
									eventHtml += "<br/>";
								}
							});
							$('#eventResultDiv').html(eventHtml);
						} else {
							$('#eventId').empty().append($('<option></option>').val("-1").html("--Please Select--"));
						}
					} else {
						$('#eventId').empty().append($('<option></option>').val("-1").html("--Please Select--"));
					}
				});
			});

			function frmSubmit(){
				var resultVal="";
				var inputList = document.getElementsByTagName('input');
				for(var i=0; i<inputList.length; i++) {
					if (inputList[i].type == 'radio' && inputList[i].name.match('opt')) {
						var elem = inputList[i];
						if(elem.checked == true) {
							resultVal += elem.name.substring(3)+"_"+elem.value+",";
						}
					}
				}
				$('#eventResult').val(resultVal);

				return true;
			}
		</script>
	</head>
	<body>
		<div id="errorDiv" style="color: red;">
			<s:actionmessage />
		</div>
		<div id="wrap">
			<div id="top_form">
				<s:form action="resultSubmissionSubmit" onsubmit="return frmSubmit();" theme="simple">
					<table width="450" border="0" cellpadding="2" cellspacing="0">
						<tr>
							<td align="center" colspan="2">
								<div id="errorDiv" style="color: red;"></div>
								<s:hidden name="gameId" value="%{gameId}" />
								<s:hidden name="gameTypeId" value="%{gameTypeId}" />
								<s:hidden name="eventResult" id="eventResult" value="" />
							</td>
						</tr>
						<tr>
							<td align="left">
								Select Draw
							</td>
							<td align="left">
								<s:select id="drawId" headerKey="-1"
									headerValue="--Please Select--" name="drawId"
									list="%{drawMasterList}" listKey="drawId"
									listValue="%{drawName+' - '+drawDateTime}" cssClass="option"></s:select>
								<s:hidden id="drawType" name="drawType"
									value="%{new com.skilrock.lms.coreEngine.sportsLottery.common.SportsLotteryUtils().convertJSON(drawMasterList)}" />
							</td>
						</tr>
						<tr>
							<td align="left">
								Enter Events Result
							</td>
							<td align="left">
								<div id="eventResultDiv"></div>
							</td>
						</tr>
						<tr>
							<td>
								<s:submit name="submit" value="Submit" align="right"
									targets="down" theme="ajax" cssClass="button" />
							</td>
						</tr>
					</table>
				</s:form>
			</div>
		</div>
	</body>
</html>