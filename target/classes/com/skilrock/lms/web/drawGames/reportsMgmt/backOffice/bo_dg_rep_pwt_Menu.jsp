<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">

<head>
	<s:head theme="ajax" debug="false"/>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<link rel="stylesheet" href="<%=request.getContextPath() %>/LMSImages/css/styles.css" type="text/css"/>
	<title><%=application.getAttribute("JSP_PAGE_TITLE")%></title>
	<script type="text/javascript" src="<%=request.getContextPath() %>/com/skilrock/lms/web/drawGames/reportsMgmt/backOffice/js/report.js"></script>
	<link type="text/css" rel="stylesheet" href="<%=request.getContextPath() %>/LMSImages/css/lmsCalendar.css" media="screen"/>
	<script>var projectName="<%=request.getContextPath() %>"</script>
	<script type="text/javascript" src="<%=request.getContextPath() %>/com/skilrock/lms/web/common/globalJs/calender.js"></script>
	<script>
	function fetchData(data){
	_ajaxCall(projectName + "/com/skilrock/lms/web/drawGames/reportsMgmt/bo_dg_rep_pwt_Detail.action?"+data,'result');
	
	}
	function clearDiv(){
	_id.i('result','');
	}
	</script>
</head>

<body>
	<%@include file="/com/skilrock/lms/web/loginMgmt/menu.jsp"%>
	<div id="wrap">
     <div id="top_form">
  <h3><s:text name="label.draw.game"/> <s:text name="PWT"/> <s:text name="Report"/></h3>
	
	
  <s:form name="searchgame" action="bo_dg_rep_pwt_Search" onsubmit="return validateDates()"> 		
	 <table  border="0" cellpadding="2" cellspacing="0" width="400px">
		<tr>
			<td colspan="2"><s:select name="totaltime" label="%{getText('label.get')} %{getText('PWT')} %{getText('Report')} " list="{'Current Day','Current Week','Current Month','Date Wise'}" cssClass="option" onchange="setInputField(this.value),clearDiv()" /></td>
		</tr>
		
		<tr> 			
			<td colspan="2" align="center">
			<div id="date" style="display: none;">
				<s:set name="stDate" id="stDate" value="#session.presentDate" />
				<div id="dates"></div>
				<s:div theme="ajax" href="rep_common_fetchDate.action">
				</s:div>
			</div>
			</td>
			
		</tr>
		<tr>
			<td colspan="2"><s:select name="filter" label="%{getText('Report')} %{getText('label.for')} " list="#{'Game Wise':'Game Wise', 'Agent Wise':#application.TIER_MAP.AGENT + ' Wise', 'Retailer Wise':#application.TIER_MAP.RETAILER + ' Wise'}" cssClass="option" onchange="setField(this.value),clearDiv()"/></td>
		</tr>
		<tr>
			<td>
				<div id="game"></div>
			</td>
		</tr>

		<tr>		<td>
				<div id="list">
				<div id="agentlistDiv" style="margin-left:20px;display: none">
								<table style="margin-left:10px">
									<s:select name="AgentId" id="agtId"
									label="%{getText('label.select')} %{#application.TIER_MAP.AGENT} %{getText('label.org')}" list="{}" cssClass="option"
									headerKey="" headerValue="%{getText('label.please.select')}"
									 required="true" />
									</table>
								</div>
				
				</div>
			</td>
		</tr>
		<tr>
			<td><s:submit name="search" key="btn.srch" align="right"  targets="down" theme="ajax" cssClass="button" /></td>
		</tr>
		
	 </table>
	</s:form>
	<div id="down" style="text-align: center" ></div>
	<div id="result" style="text-align: center" ></div>
	 </div></div>
	
 	
 	 

</body>
</html>
<code id="headId" style="visibility: hidden">
$RCSfile: bo_dg_rep_pwt_Menu.jsp,v $
$Revision: 1.3 $
</code>