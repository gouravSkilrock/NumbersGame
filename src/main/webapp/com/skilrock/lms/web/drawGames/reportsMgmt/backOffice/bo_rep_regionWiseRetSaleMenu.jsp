<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="com.skilrock.lms.common.utility.CommonMethods"%>
<%@page import="java.util.Calendar"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%
	response.setHeader("Cache-Control", "no-store"); //HTTP 1.1
	response.setHeader("Pragma", "no-cache"); //HTTP 1.0
	response.setDateHeader("Expires", 0); //prevents caching at the proxy server
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
		<s:head theme="ajax" debug="false" />
		<meta http-equiv="Content-Type"
			content="text/html; charset=UTF-8" />
		<link rel="stylesheet"
			href="<%=request.getContextPath()%>/LMSImages/css/styles.css"
			type="text/css" />
		<title><%=application.getAttribute("JSP_PAGE_TITLE")%></title>

		<link type="text/css" rel="stylesheet"
			href="<%=request.getContextPath()%>/LMSImages/css/lmsCalendar.css"
			media="screen" />
				<script>var projectName="<%=request.getContextPath()%>"</script>
		<script type="text/javascript"	src="<%=request.getContextPath()%>/com/skilrock/lms/web/common/globalJs/calender.js"></script>
		<script>var projectName="<%=request.getContextPath()%>"</script>
		<script type="text/javascript">path="<%=request.getContextPath()%>" ; </script>
			<script>
	function getData() {
		var tblData = document.getElementById("tableDataDiv").innerHTML;
		tblData = tblData.replace(document.getElementById("sortRow").innerHTML,
				"");
		document.getElementById('tableValue').value = tblData;
		return false;
	}
</script>

	
</head>
<body>
<%@include file="/com/skilrock/lms/web/loginMgmt/menu.jsp"%>
		<div id="wrap">
			<div id="top_form">
				
				<h3>
					<s:text name="label.state.wise.ret_sale.rep"/>
				</h3>
			<s:form name="repForm" id="repForm"	action="bo_rep_regionWiseRetSaleSearch" onsubmit="return validated();" method="post">
						
					<table border="0" cellpadding="2" cellspacing="0" width="400"
						style="width: 100%; vertical-align: middle;">
					
						<tr>
							<td align="left" colspan="2">
								<div id="error"><s:actionerror /> <s:actionmessage/>  </div>
							</td>
						</tr>
						<tr>
										<td align="center" colspan="2">
											<div id="gameId_e"   style="color: red; text-align: center"></div>
										</td>
									</tr>
						<tr>
							<td colspan="2">
									<label class="label">
											&nbsp;<s:text name="label.game.name"/>:
												<span style="color:red">*</span>:&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
											</label>
								
								<s:select name="gameId" id="gameId" theme="simple" headerKey="-1" headerValue="%{getText('label.please.select')}"
									 list="#session.DRAWGAME_LIST"
									cssClass="option"/>
							</td>
						</tr>
						<tr>
										<td align="center" colspan="2">
											<div id="date_e"   style="color: red; text-align: center"></div>
										</td>
									</tr>
						<tr>
							<td colspan="2">
								<table cellpadding="3"
									cellspacing="0" border="0">
									<s:set name="stDate" id="stDate" value="#session.presentDate" />
									<%
										Calendar prevCal = Calendar.getInstance();
											String startDate = CommonMethods
													.convertDateInGlobalFormat(new java.sql.Date(prevCal
															.getTimeInMillis()).toString(), "yyyy-mm-dd",
															"yyyy-mm-dd");
									%>
									<tr>
										<td>
											<label class="label">
												<s:text name="label.start.date"/>
												<!--<span style="color:red">*</span>-->:&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
											</label>
											<input type="text" name="start_date" id="startDate"
												value="<%=startDate%>" readonly size="12"  >
											<input type="button"
												style="width: 19px; height: 19px; background: url('<%=request.getContextPath()%>/LMSImages/imagesCal/dateIcon.gif'); top left; border: 0;"
												onclick="displayCalendar(document.getElementById('startDate'),'yyyy-mm-dd', this, '<%=startDate%>', false, '<%=startDate%>')" />
										</td>
									</tr>
									<tr>
										<td>
											<label class="label">
												<s:text name="label.end.date"/>
													<!--<span style="color:red">*</span>-->:&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
											</label>
											<input type="text" name="end_Date" id="endDate"
												value="<%=startDate%>" readonly size="12"  >
											<input type="button"
												style="width: 19px; height: 19px; background: url('<%=request.getContextPath()%>/LMSImages/imagesCal/dateIcon.gif'); top left; border: 0;"
												onclick="displayCalendar(document.getElementById('endDate'),'yyyy-mm-dd', this, '<%=startDate%>',false, '<%=startDate%>')" />
										</td>
									</tr>
							</table>
							</td>
							</tr>		
						<tr>
										<td align="center" colspan="2">
											<div id="orgcountry_e"   style="color: red; text-align: center"></div>
										</td>
									</tr>
									<tr style="display: none;">
										<td align="right">
											<label class="label">
												<s:text name="label.country"/>
												<!-- <span class="required">*</span> -->: 
											</label>
										</td>
										<td>
											<s:select theme="simple" key="label.country" id="country"
												name="countryCode" headerKey="-1"
												headerValue="%{getText('label.please.select')}" list="{}"
												cssClass="option" required="true" />
										</td>
									</tr>

									<tr>
										<td align="center" colspan="2">
											<div id="orgstate_e"  style="color: red; text-align: center"></div>
										</td>
									</tr>
									<tr>
										
										<td colspan="2">
										<label class="label">
												<s:text name="label.slct.state"/> 
											<!-- 	<span style="color:red">*</span> -->:&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
											</label>
											</label>
										
											<s:select theme="simple"  id="state"
												name="stateCode" headerKey="-1"
												headerValue="--%{getText('label.select.all')}--" list="{}"
												cssClass="option" required="true" />
										</td>
										
										
										
									</tr>

									<tr>
										<td align="center" colspan="2">
											<div id="orgcity_e"  style="color: red; text-align: center"></div>
										</td>
									</tr>
									<tr>
											
										<td colspan="2">
										<label class="label">
														<s:text name="label.slct.city"/> 
															<!--<span style="color:red">*</span> -->:&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
											</label>
									
											<s:select theme="simple"  id="city"
												name="cityCode" headerKey="-1"
												headerValue="--%{getText('label.select.all')}--" list="{}"
												cssClass="option" required="true" />
										</td>
										
									</tr>
							<s:if test="%{#application.COUNTRY_DEPLOYED=='GHANA'}">
									<tr>
										<td align="center" colspan="2">
											<div id="area_e"  style="color: red; text-align: center"></div>
										</td>
									</tr>
									<tr>
											
										<td colspan="2">
										
										<div id="areaDiv">
										
										</div>
										</td>
										
									</tr>
								</s:if>	
							<tr>
					<td colspan="2">
								<div id="agtDiv" >
									<table>
										<s:select cssClass="option" name="agentOrgId"
											id="agentOrgId" list="{}"
											label="%{#application.TIER_MAP.AGENT} Organization"
											headerValue="--%{getText('label.ALL')}--" headerKey="-1" required="true"></s:select>
									</table>
								</div>
							</td>
						</tr>						
						
						<tr>
							<td colspan="2">
								<s:submit name="search" key="btn.srch" align="center"
									targets="down" theme="ajax" cssClass="button" />
							</td>
						</tr>
						
						

					</table>
				</s:form>

			
					<div id="down" style="text-align: center"></div>
				
				<br />
				<br />	
				
			</div>
			</div>	
				
<script type="text/javascript"	src="<%=request.getContextPath()%>/com/skilrock/lms/web/drawGames/reportsMgmt/backOffice/js/bo_rep_regionWise.js"></script>
</body>


</html>

<code id="headId" style="visibility: hidden">
	$RCSfile: bo_rep_regionWiseRetSaleMenu.jsp,v $ $Revision: 1.3 $
</code>