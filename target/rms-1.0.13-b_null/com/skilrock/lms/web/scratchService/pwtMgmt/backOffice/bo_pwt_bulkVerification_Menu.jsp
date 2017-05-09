	<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
	<%@ taglib prefix="s" uri="/struts-tags"%>
	<%response.setDateHeader("Expires",1000);%>	
	<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
	
	<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
	
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />	
		<link rel="stylesheet" href="<%=request.getContextPath() %>/LMSImages/css/styles.css" type="text/css"/>
		<script>var path = "<%=request.getContextPath() %>";</script>
		<title><%=application.getAttribute("JSP_PAGE_TITLE")%></title>
		<s:head theme="ajax" debug="false"/>
		<link type="text/css" rel="stylesheet" href="<%=request.getContextPath() %>/LMSImages/css/lmsCalendar.css" media="screen"/>
		<script>var projectName="<%=request.getContextPath() %>"</script>
		<script type="text/javascript" src="<%=request.getContextPath() %>/com/skilrock/lms/web/common/globalJs/calender.js"></script>		
		<script  src="<%=request.getContextPath() %>/com/skilrock/lms/web/scratchService/pwtMgmt/backOffice/js/pwtCommonFn.js"></script>
	</head>

	<body onload="onLoadAjax('getListAjax.action?listType=agentPwt','agtOrgName')">
	<%@include file="/com/skilrock/lms/web/loginMgmt/menu.jsp" %>

	<div id="wrap">

  	<div id="top_form">
   <h3>Ticket and <s:text name="PWT"/> Verification Against Bulk Receipt</h3>
	
		<s:form action="bo_pwt_bulkVerification_Search" >
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
				
				
				
				
				<s:submit  theme="ajax" targets="bottom" value="Search"  cssClass="button"/>

</table>
			
			</s:form>
					
 <div  id="bottom" >  
	
 </div>
</div></div>
	
	</body>
</html>
<code id="headId" style="visibility: hidden">
$RCSfile: bo_pwt_bulkVerification_Menu.jsp,v $
$Revision: 1.2 $
</code>