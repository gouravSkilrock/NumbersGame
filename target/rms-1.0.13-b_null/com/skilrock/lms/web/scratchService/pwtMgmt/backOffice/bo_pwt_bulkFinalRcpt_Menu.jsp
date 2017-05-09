	<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
	<%@ taglib prefix="s" uri="/struts-tags"%>
	
	<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
	
	<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
	
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
		
		<link rel="stylesheet" href="<%=request.getContextPath() %>/LMSImages/css/styles.css" type="text/css"/>
		<title><%=application.getAttribute("JSP_PAGE_TITLE")%></title>
		<s:head theme="ajax" debug="false"/>
		<link type="text/css" rel="stylesheet" href="<%=request.getContextPath() %>/LMSImages/css/lmsCalendar.css" media="screen"/>
		<script>var projectName="<%=request.getContextPath() %>"</script>
		<script type="text/javascript" src="<%=request.getContextPath() %>/com/skilrock/lms/web/common/globalJs/calender.js"></script>		
	
	</head>
	<script>
	
	function _validate()
	{
		_id.i("tmpReceiptdetail","");
		return true;
	
	}
	
	
	var path = "<%=request.getContextPath() %>";
	function heightDiv(){
	
	document.getElementById("completeDiv").style.height=document.body.clientHeight-220+"px";
	document.getElementById("completeDiv").style.display="block"
	document.getElementById("resultList").disabled="true";
	}
	</script>
	<script  src="<%=request.getContextPath() %>/com/skilrock/lms/web/scratchService/pwtMgmt/backOffice/js/pwtCommonFn.js"></script>
	
	<body onload="onLoadAjax('getListAjax.action?listType=agentPwt','agtOrgName')">
	<%@include file="/com/skilrock/lms/web/loginMgmt/menu.jsp" %>

	<div id="wrap">

  	<div id="top_form">
   <h3>PWT Receipt Against Bulk</h3>
	
		<s:form action="bo_pwt_bulkFinalRcpt_Search" onsubmit="return _validate();">
			<table border="0" cellpadding="2" cellspacing="0">
			<tr>
				<th>
					Search Bulk <s:text name="PWT"/> Receipt
				</th>
			</tr>

				<tr>
					<s:select label="%{#application.TIER_MAP.AGENT} Name" id="agtOrgName" name="agtOrgName" headerKey="-1"
										headerValue="--Please Select--"
										list="{}" cssClass="option" />
				</tr>
				<tr>
					<s:textfield label="Receipt Number" name="receiptNum"></s:textfield>
				</tr>
				<tr>
					<s:select  label="Status" id="status" name="status" headerKey="-1"
										headerValue="--Please Select--"
										list="{'OPEN','CLOSE'}" cssClass="option"  />
				</tr>
				<tr>
										<td colspan="3">

											<s:text name="from">Receipt Generation</s:text>
											<s:text name="from">&nbsp;From:</s:text>
											
											<input  type="text" name="fromDate" id="fromDate" value="" readonly size="12">
    										<input type="button" style=" width:19px; height: 19px; background: url('<%=request.getContextPath() %>/LMSImages/imagesCal/dateIcon.gif'); top left; border:0 ; " onclick="displayCalendar(document.getElementById('fromDate'),'yyyy-mm-dd', this, '<s:property value="%{#session.PWT_CURRENT_DATE}"/>', false, '<s:property value="%{#session.PWT_CURRENT_DATE}"/>')" />
							
											<s:text name="to">&nbsp;To:</s:text>							
											<input  type="text" name="toDate" id="toDate" value="" readonly size="12">
    										<input type="button" style=" width:19px; height: 19px; background: url('<%=request.getContextPath() %>/LMSImages/imagesCal/dateIcon.gif'); top left; border:0 ; " onclick="displayCalendar(document.getElementById('toDate'),'yyyy-mm-dd', this, '<s:property value="%{#session.PWT_CURRENT_DATE}"/>', false, '<s:property value="%{#session.PWT_CURRENT_DATE}"/>')" />
							
										</td>

				</tr>
				
				<s:submit  theme="ajax" targets="bottom" value="Search" cssClass="button"/>

</table>
			
			</s:form>
 <div id="completeDiv" style="position: absolute; background-color: #CCCCCC ;opacity:1 ;filter:alpha(opacity=60);display:none;width:830px" >
 <center><b><font size="3" >Please Wait Data is Being Processed......</font></b></center>
</div>
 <div id="completeDivOuter" width="100%" >
					
 <div  id="bottom" >  
	
 </div>
<div  id="tmpReceiptdetail" >  
	
 </div>
  
</div>
 </div>
</div>
	
	</body>
</html>
<code id="headId" style="visibility: hidden">
$RCSfile: bo_pwt_bulkFinalRcpt_Menu.jsp,v $
$Revision: 1.2 $
</code>