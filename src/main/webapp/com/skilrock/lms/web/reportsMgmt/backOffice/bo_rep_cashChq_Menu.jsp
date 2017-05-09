<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@page import="com.skilrock.lms.common.utility.CommonMethods"%>
<%@page import="java.util.Calendar"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<%java.util.Calendar calendar= java.util.Calendar.getInstance();
		calendar.setTimeInMillis(System.currentTimeMillis());
		calendar.set(calendar.HOUR_OF_DAY,23);
		calendar.set(calendar.MINUTE,59);
		calendar.set(calendar.SECOND,59);%>
<%response.setDateHeader("Expires",calendar.getTimeInMillis());%>
	<head>
		<s:head theme="ajax" debug="false" />
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<link rel="stylesheet"
			href="<%=request.getContextPath()%>/LMSImages/css/styles.css"
			type="text/css" />
		<title><%=application.getAttribute("JSP_PAGE_TITLE")%></title>
		<script type="text/javascript"
			src="<%=request.getContextPath()%>/com/skilrock/lms/web/common/globalJs/jquery-1.10.1.min.js"
			charset="UTF-8"></script>
		<script type="text/javascript"
			src="<%=request.getContextPath()%>/com/skilrock/lms/web/reportsMgmt/backOffice/js/report.js"></script>
		<link type="text/css" rel="stylesheet"
			href="<%=request.getContextPath()%>/LMSImages/css/lmsCalendar.css"
			media="screen" />
		<script>var projectName="<%=request.getContextPath()%>";</script>
		<script type="text/javascript"
			src="<%=request.getContextPath()%>/com/skilrock/lms/web/common/globalJs/calender.js"></script>
		<script>
			$(document).ready(function(){
				var countryData=$('#countryData').val();
				$('#countryData').val("");
				var obj = jQuery.parseJSON(countryData);
				var countryName=$('#countryDeployed').val();
				var countryCode=(countryName=='ZIMBABWE'?'ZIM':(countryName=='INDIA'?'IND':'NIG'));
   				$.each(obj,function(index,value){
  					// alert(value.countryName);
   					if(countryCode == value.countryCode){
  					// alert("equals");
   						if(value.stateBeanList != undefined){
    						$.each(value.stateBeanList,function(index1,value1){
   							//alert(value1.stateName);
  								$('#state').append(
                    				$('<option></option>').val(value1.stateCode).html(value1.stateName));
   								});
  							 }
   						}
    			});
 
  				$('#state').change(function(){
					$('#city')
					    .find('option')
					    .remove()
					    .end()
					    .append($('<option></option>').val(-1).html('ALL'));
 
 					var stateCode=$('#state').val();
 						$.each(obj,function(index,value){
					  	// alert(value.countryName);
					   if('NIG' == value.countryCode || 'IND' == value.countryCode || 'ZIM'==value.countryCode){
					 	//  alert("equals");
					   if(value.stateBeanList != undefined){
					    $.each(value.stateBeanList,function(index1,value1){
					   //alert(value1.stateName);
					   if(stateCode == value1.stateCode){
					  // alert("equals");
					   if(value1.cityBeanList != undefined){
					   $.each(value1.cityBeanList,function(index2,value2){
					   //alert(value2.cityName);
  
     					$('#city').append(
                    		$('<option></option>').val(value2.cityCode).html(value2.cityName));
   						});
   					}
   				}
			});
   		}
   	}
   }); 
 });
	});
		
		

function clearDiv() {
	_id.i("down", "");
	_id.i("result", "");
	_id.i("userName_e", "");
	$('#stateName').val($('#state option:selected').text());
	$('#cityName').val($('#city option:selected').text());
}
function checkDetails(){
	if(_id.o("reportsTypeUserwise").checked){return true;}
	else{alert('<s:text name="alert.select.cashiserwise.radio.btn"/>'); return false;}
}

function removeStateCityInCashier(){
	var reportType=$('input:radio[name=reportType]:checked').val();
	if(reportType=='Userwise'){
		$('#stateTr').hide();
		$('#cityTr').hide();
	}else{
		$('#stateTr').show();
		$('#cityTr').show();
	}
}


		</script>

	</head>

	<body>
		<%@include file="/com/skilrock/lms/web/loginMgmt/menu.jsp"%>
		<div id="wrap">
			<div id="top_form">
				<h3>
					<s:text name="label.cash.colln.rep" />
				</h3>

				<s:form name="searchgame" action="bo_rep_cashChq_Search"
					theme="simple" onsubmit="return validateDates();">
					<s:hidden id="stateName" name="stateName" />
					<s:hidden id="cityName" name="cityName" />
					<s:hidden id="countryDeployed" value="%{#application.COUNTRY_DEPLOYED}"/>	
					<s:hidden id="countryData" name="countryData"></s:hidden>
					<s:hidden name="userData" id="userData" value="%{userData}"></s:hidden>
					<table border="0" cellpadding="2" cellspacing="0" width="400px">
						<tr>
							<td>
								<div style="color: red; text-align: center;" id="userName_e"></div>
							</td>
						</tr>
						<tr>
							<td>
								<label class="label">
									<s:text name="%{getText('Report')}" />
									<s:text name="label.type" />
								</label>
							</td>
							<td>
								<s:radio name="reportType" id="reportType" value="'Agentwise'"
									list="#{'Agentwise':getText('AGENT') +' '+ getText('label.wise'),'Daywise':getText('label.day') +' '+ getText('label.wise'),'Userwise':getText('label.cashier') +' '+ getText('label.wise')}"
									onclick="clearDiv();removeStateCityInCashier();" />
							</td>
						</tr>

						<tr>
							<td>
								<label class="label">
									<s:text name="%{getText('Report')}" />
									<s:text name="label.duration" />
								</label>
							</td>
							<td>
								<s:select name="totaltime"
									list="#{'Current Day':getText('label.cur.day'),'Date Wise':getText('label.datewise')}"
									cssClass="option"
									onchange="setInputField(this.value),clearDiv()" />
							</td>
						</tr>
						<tr id="stateTr">
							<td>
								<label class="label">
									State
									<span class="required">*</span>:
								</label>
							</td>
							<td>
								<s:select label="State" cssClass="option" name="state"
									id="state" list="{}" onchange="clearDiv()" headerKey="-1"
									headerValue="ALL" theme="simple"></s:select>
							</td>
						</tr >
						<tr id="cityTr">
							<td>
								<label class="label">
									City
									<span class="required">*</span>:
								</label>
							</td>
							<td>
								<s:select label="City" cssClass="option" name="city" id="city"
									list="{}" onchange="clearDiv()" headerKey="-1"
									headerValue="ALL" theme="simple"></s:select>
							</td>
						</tr>
						<tr>
							<td colspan="2">
								<div id="dates"></div>
							</td>
						</tr>
						<tr>
							<td colspan="2" align="center">
								<div id="date" style="display: none;">

									<table align="center" width="400px">
										<s:set name="stDate" id="stDate" value="#session.presentDate" />
										<%
											Calendar prevCal = Calendar.getInstance();
												String startDate = CommonMethods
														.convertDateInGlobalFormat(new java.sql.Date(prevCal
																.getTimeInMillis()).toString(), "yyyy-mm-dd",
																(String) session.getAttribute("date_format"));
										%>
										<tr>
											<td>
												<label class="label">
													<s:text name="label.start.date" />
													<span class="required">*</span>:&nbsp;
												</label>
												<input type="text" name="start_date" id="start_date"
													value="<%=startDate%>" readonly size="12" style="margin-left:30px">
													<input type="button"
														style=" width:19px; height: 19px; background: url('<%=request.getContextPath()%>/LMSImages/imagesCal/dateIcon.gif'); top left; border:0 ; "
														onclick="displayCalendar(document.getElementById('start_date'),'dd-mm-yyyy', this, '<%=startDate%>', false, '<%=startDate%>')" />
											</td>
										</tr>
										<tr>
											<td>
												<label class="label">
													<s:text name="label.end.date" />
													<span class="required">*</span>:&nbsp;
												</label>
												<input type="text" name="end_Date" id="end_date"
													value="<%=startDate%>" readonly size="12" style="margin-left:30px">
													<input type="button"
														style=" width:19px; height: 19px; background: url('<%=request.getContextPath()%>/LMSImages/imagesCal/dateIcon.gif'); top left; border:0 ; "
														onclick="displayCalendar(document.getElementById('end_date'),'dd-mm-yyyy', this, '<%=startDate%>',false, '<%=startDate%>')" />
											</td>
										</tr>
									</table>
								</div>
							</td>

						</tr>
						<tr>
							<td>
								<s:submit name="search" key="btn.srch" align="right"
									targets="down" theme="ajax" cssClass="button"
									onclick="clearDiv()" />
							</td>
						</tr>

					</table>
				</s:form>
				<div id="down" style="text-align: center"></div>
				<s:div id="result"></s:div>

			</div>
		</div>


	</body>
</html>

<code id="headId" style="visibility: hidden">
	$RCSfile: bo_rep_cashChq_Menu.jsp,v $ $Revision: 1.3 $
</code>