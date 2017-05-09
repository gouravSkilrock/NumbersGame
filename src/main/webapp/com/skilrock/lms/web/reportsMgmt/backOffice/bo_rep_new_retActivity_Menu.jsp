<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<html xmlns="http://www.w3.org/1999/xhtml">

	<head>

		<!-- jQuery UI theme switcher -->
		<meta http-equiv="Content-Type"
			content="text/html; charset=UTF-8" />

		<link rel="stylesheet"
			href="<%=request.getContextPath()%>/LMSImages/css/styles.css"
			type="text/css" />

		<title><%=application.getAttribute("JSP_PAGE_TITLE")%></title>
		<s:head theme="ajax" debug="false" />
		<script>var path = "<%=request.getContextPath()%>";
			</script>
		<script
			src="<%=request.getContextPath()%>/com/skilrock/lms/web/reportsMgmt/backOffice/js/newActivityRep.js" />
		<link type="text/css" rel="stylesheet"
			href="<%=request.getContextPath()%>/LMSImages/css/lmsCalendar.css"
			media="screen" />

		<script type="text/javascript"
			src="<%=request.getContextPath()%>/com/skilrock/lms/web/common/globalJs/calender.js"></script>
		<script src="facefiles/jquery-1.2.2.pack.js" type="text/javascript"></script>
		<link href="facefiles/facebox.css" media="screen" rel="stylesheet"
			type="text/css" />
		<script src="facefiles/facebox.js" type="text/javascript"></script>
		<script type="text/javascript">
			jQuery(document).ready(function($) {
				$('a[rel*=facebox]').facebox()
			});

			function getData() {
				var tblData = document.getElementById("reportDiv").innerHTML;
//				tblData = tblData.replace(document.getElementById("sortRow").innerHTML, "");
				$("#tableValue").val(tblData);
				$("#orgNameText").val($('#agtOrgName option:selected').text());
				$("#locationText").val($('#location option:selected').text());
				$("#terminalTypeText").val($('#terminal option:selected').text());
				$("#connModeText").val($('#conMode option:selected').text());
				$("#modeText").val($('#selectMode option:selected').text());
				$("#activityText").val($('#activityType option:selected').text());
				$("#timeA").val($('#valueA option:selected').text());
				$("#timeB").val($('#valueB option:selected').text());
				return false;
			}
	</script>
	<script type="text/javascript" src="slider/js/jquery-1.3.2.min.js"></script>
	<script>
var jq = jQuery.noConflict();
</script>
	<script type="text/javascript" src="slider/js/jquery-ui-1.7.1.custom.min.js"></script>
	<script type="text/javascript" src="slider/js/selectToUISlider.jQuery.js"></script>
	<script>
var jqSlider = jQuery.noConflict();
</script>
	<link rel="stylesheet" href="slider/css/redmond/jquery-ui-1.7.1.custom.css" type="text/css" />
	<link rel="Stylesheet" href="slider/css/ui.slider.extras.css" type="text/css" />
	<style type="text/css">
		body {font-size: 62.5%; font-family:"Segoe UI","Helvetica Neue",Helvetica,Arial,sans-serif; }
		fieldset { border:0; margin: 0em; height: 0em;}	
		label {font-weight: normal; float: left; margin-right: .5em; font-size: 1.1em;}
		select {margin-right: 1em; float: left;}
		.ui-slider {clear: both; top: 0em;}
	</style>

<script type="text/javascript">	
var $j = jQuery.noConflict();
	$(function() {
		jqSlider('select#valueA, select#valueB').selectToUISlider();
		//fix color 
		fixToolTipColor();
	});
	//purely for theme-switching demo... ignore this unless you're using a theme switcher
	//quick function for tooltip color match
	function fixToolTipColor() {
		//grab the bg color from the tooltip content - set top border of pointer to same
		$('.ui-tooltip-pointer-down-inner').each(
				function() {
					var bWidth = $('.ui-tooltip-pointer-down-inner').css(
							'borderTopWidth');
					var bColor = $(this).parents('.ui-slider-tooltip').css(
							'backgroundColor')
					$(this).css('border-top', bWidth + ' solid ' + bColor);
				});
	}
</script>
	</head>

	<body onload="getAgentList(),fetchActData(-1)">
		<%@include file="/com/skilrock/lms/web/loginMgmt/menu.jsp"%>
		<div id="wrap_new">
			<div id="top_form_new">

				<h3>
					<s:text name="label.ret.activity.status.rep"/>
				</h3>
				<s:hidden id="latLong"  value="%{getText('latLong')}"/>
				<s:form name="retActivity" id="retActivity">
					<table border="0" cellpadding="2" cellspacing="0">
						<tr>
							<td align="right">
								<i><s:text name="label.slct.agnt.org"/> :</i>
							</td>
							<td>
								<s:select theme="simple" id="agtOrgName" cssClass="option"
									list="{}" name="agtOrgName" emptyOption="false"
									headerKey="-1" headerValue="ALL"
									onchange="filterDataAtModeBased()" />
							</td>
						</tr>

						<tr>
							<td>
								<s:select id="location" cssClass="option" list="city"
									name="locationName" emptyOption="false" headerKey="ALL"
									headerValue="%{getText('label.ALL')}" onchange="filterData()" key="label.location" />
							</td>
						</tr>

						<tr>
							<td>
								<s:select id="terminal" cssClass="option"
									list="terminalType"
									name="terminal" emptyOption="false" headerKey="ALL"
									headerValue="%{getText('label.ALL')}" key="label.trmnl.type" onchange="filterSimType()" />
							</td>
						</tr>
						<tr>
							<td align="left" colspan="2">
								<div id="conDiv" style="display: block" ><table><tr><td><s:text name="label.conctvty.mode"/>:</td><td><s:select id="conMode" cssClass="option" theme="simple"
									list="conDevice"
									name="conMode" emptyOption="false" headerKey="ALL"
									headerValue="%{getText('label.ALL')}" onchange="filterData()" cssStyle="margin-left: 26px; margin-top: 8px;"/></td></tr></table>	
								</div>
							</td>
						</tr>
	
						<tr>
							<td>
								<s:select name="selectMode" id="selectMode" key="label.slct.mode"
									cssClass="option"
									list="#{'ACTIVITY':getText('ACTIVITY'),'HEARTBEAT':getText('HEARTBEAT'),'TERMINATE':getText('TERMINATE_BLOCK'),'NO TERMINAL':getText('NO_TERMINAL')}"
									onchange="filterDataAtModeBased()" />
							</td>
						</tr>
						<tr>
							<td align="left" colspan="2">

							<div id="actDiv">	 <table><tr><td><s:text name="label.slct.actvty"/> :</td>
							<td>
							
							<s:select id="activityType" name="activityType" theme="simple"
										cssClass="option" headerKey="ANY" headerValue="%{getText('label.ANY')}"
										list="#{'SALE':getText('SALE'),'PWT':getText('PWT'),'LOGIN':getText('label.LOGIN')}"
										onchange="filterData()"  cssStyle="margin-left: 45px; margin-top: 8px;"/>
							</td></tr>
									
							</table></div>
							</td>
						</tr>
					
					</table>
					<table width="900">
						<tr>
							<td>
								<div id="statusTab" style="display: block;">
									<table border="0" cellpadding="6" cellspacing="0">
										<tr>

											<td align="right">
												<i><s:text name="label.slct.status"/> :</i>
											</td>
											<td>
												<input type="checkbox" name="statusName" value="<s:text name="LIVE" />"
													id="retActivity_statusName" checked="checked" onclick=filterData(); />
												&nbsp;&nbsp;
												<span
													style="font-weight: bold; background: green; color: white; padding: 2px;"><s:text
														name="LIVE" /> [<span id="activeCnt"></span>]</span>&nbsp;&nbsp;

												<input type="checkbox" name="statusName" value="<s:text name="IDLE" />"
													id="retActivity_statusName" checked="checked" onclick=filterData(); />
												&nbsp;&nbsp;
												<span
													style="font-weight: bold; background: yellow; color: black; padding: 2px;"><s:text
														name="IDLE" />[<span id="idleCnt"></span>]</span>&nbsp;&nbsp;

												<input type="checkbox" name="statusName" value="<s:text name="NO_SALE" />"
													id="retActivity_statusName" checked="checked" onclick=filterData(); />
												&nbsp;&nbsp;
												<span
													style="font-weight: bold; background: red; color: white; padding: 2px;"><s:text
														name="NO_SALE" /> [<span id="nosaleCnt"></span>]</span>&nbsp;&nbsp;
														
													<input type="checkbox" name="statusName" value="<s:text name="label.new.login" />"
													id="retActivity_statusName" checked="checked" onclick=filterData(); />
												&nbsp;&nbsp;
												<span
													style="font-weight: bold; background: DodgerBlue; color: white; padding: 2px;"><s:text
														name="label.new.login" /> [<span id="newLoginCnt"></span>]</span>&nbsp;&nbsp;		
														
											</td>
										</tr>
										
									</table>
								</div>
							</td>
							<td>
								<p>
									<h2><a href="#mydiv" rel="facebox"><s:text name="label.locate.user.in.google.map"/></a></h2>
								</p>
							</td>
						</tr>
						<tr>
							<td>
									<div id="sliderDiv" style="display: block;">
								<fieldset>
										<select name="valueA" id="valueA" style="display: none;">
										<option value="0">0:00</option>
											<option value="1">1:00</option>
											<option value="2" selected="selected">2:00</option>
											<option value="3">3:00</option>
											<option value="4">4:00</option>
											<option value="5">5:00</option>
											<option value="6">6:00</option>
											<option value="7">7:00</option>
											<option value="8">8:00</option>
											<option value="9" >9:00</option>
											<option value="10">10:00</option>
											<option value="11">11:00</option>
											<option value="12">12</option>
											<option value="13">13:00</option>
											<option value="14">14:00</option>
											<option value="15">15:00</option>
											<option value="16">16:00</option>
											<option value="17">17:00</option>
											<option value="18">18:00</option>
											<option value="19">19:00</option>
											<option value="20">20:00</option>
											<option value="21">21:00</option>
											<option value="22">22:00</option>
											<option value="23">23:00</option>
											<option value="24">24:00</option>
										</select>
										<select name="valueB" id="valueB" style="display: none;">
										<option value="0">0:00</option>
										<option value="1">1:00</option>
											<option value="2" >2:00</option>
											<option value="3">3:00</option>
											<option value="4">4:00</option>
											<option value="5">5:00</option>
											<option value="6">6:00</option>
											<option value="7">7:00</option>
											<option value="8">8:00</option>
											<option value="9" >9:00</option>
											<option value="10">10:00</option>
											<option value="11">11:00</option>
											<option value="12">12</option>
											<option value="13">13:00</option>
											<option value="14" selected="selected">14:00</option>
											<option value="15">15:00</option>
											<option value="16">16:00</option>
											<option value="17">17:00</option>
											<option value="18">18:00</option>
											<option value="19">19:00</option>
											<option value="20">20:00</option>
											<option value="21">21:00</option>
											<option value="22">22:00</option>
											<option value="23">23:00</option>
											<option value="24">24:00</option>
										</select>
									</fieldset>
									</div>
							</td>
						</tr>
						<tr>
						<td>
							<br/>
						</td>
						</tr>
					</table>
				</s:form>
				<div id="expDiv" style="display: none;">
					<s:form action="bo_rep_new_retActivity_ExportExcel" method="post" enctype="multipart/form-data">
						<s:hidden id="tableValue" name="reportData" />
						<s:hidden id="orgNameText" name="orgNameText" />
						<s:hidden id="locationText" name="locationText" />
						<s:hidden id="terminalTypeText" name="terminalTypeText" />
						<s:hidden id="connModeText" name="connModeText" />
						<s:hidden id="modeText" name="modeText" />
						<s:hidden id="activityText" name="activityText" />
						<s:hidden id="activeCount" name="activeCount" />
						<s:hidden id="idleCount" name="idleCount" />
						<s:hidden id="noSaleCount" name="noSaleCount" />
						<s:hidden id="newLoginCount" name="newLoginCount" />
						<s:hidden id="timeA" name="timeA" />
						<s:hidden id="timeB" name="timeB" />
						<s:submit value="Export As Excel" cssStyle="margin-right:-400px;background:#77b3d7;font-size:15px" onclick="getData();" />
					</s:form>
				</div>
				<div id="resultDiv" style="display: none;">
				</div>
				<div id="pageLinks"></div>
				<div id="reportDiv"></div>
				<div id="histReportDiv"></div>
				<div id="mapDiv" style="display: none;"></div>
				<div id="mapData" style="display: none;"></div>
			</div>
		</div>
		<div id="mydiv" style="display: none">
			
			<iframe id="frmpop" src="facefiles/GMap.jsp" width="1140px"
				height="400px" scrolling="no" style="border: none;"></iframe>
		</div>
	</body>

	<script>
	var timeoutPeriod = 15 * 60 * 1000; //hour*min*sec*millisec
	function autoRefresh() {
		setTimeout("location.reload(true)", timeoutPeriod);
	}
	autoRefresh();
</script>
</html>

<code id="headId" style="visibility: hidden">
	$RCSfile: bo_rep_new_retActivity_Menu.jsp,v $ $Revision: 1.3 $
</code>