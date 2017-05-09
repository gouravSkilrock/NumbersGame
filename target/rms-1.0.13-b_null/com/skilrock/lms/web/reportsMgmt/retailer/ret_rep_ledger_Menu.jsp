
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="com.skilrock.lms.common.utility.CommonMethods"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">

	<head>
		<meta http-equiv="Content-Type"
			content="text/html; charset=iso-8859-1" />
		<link rel="stylesheet" href="<%=request.getContextPath() %>/LMSImages/css/styles.css"
			type="text/css" />
		<title><%=application.getAttribute("JSP_PAGE_TITLE")%></title>
		<link type="text/css" rel="stylesheet" href="<%=request.getContextPath() %>/LMSImages/css/lmsCalendar.css" media="screen"/>
		<script>var projectName="<%=request.getContextPath() %>"</script>
		<script type="text/javascript" src="<%=request.getContextPath() %>/com/skilrock/lms/web/common/globalJs/calender.js"></script>
		<script type="text/javascript" src="<%=request.getContextPath() %>/com/skilrock/lms/web/reportsMgmt/retailer/js/ret_rep_ledger.js" />		
		<s:head theme="ajax" debug="false" />
	</head>





	<body >
		<%@include
				file="/com/skilrock/lms/web/loginMgmt/menu.jsp"%>

			 
		<div id="wrap">

			<div id="top_form">
				<h3>
					Self Ledger For <s:property value="#application.TIER_MAP.RETAILER" /> 
				</h3>



				<s:form name="ledger" method="POST" onsubmit="return readScrp1()" target="_blank" action="ret_rep_ledger_Generate">
					<table border="0" cellpadding="2" cellsapacing="0">
						<tr>
							<td>
								<div id="errorId"></div>
							</td>
						</tr>
					</table>
					<table border="0" cellpadding="2" cellsapacing="0">

						<tr>
							<td colspan="2">
								<s:actionerror />
								<s:fielderror />
							</td>
						</tr>
								
						
						
				

				
						<s:hidden name="ledgerType" value="Retailer Self" />
						<s:set name="enddate" value="#session.presentDate"></s:set>
						<s:set name="dateFormat" value="#session.date_format"></s:set>	
						<tr>
						<td width="100">From Date</td>
						<td>
							<%
									String endDate = CommonMethods.convertDateInGlobalFormat((String)session.getAttribute("presentDate"), "yyyy-mm-dd", (String)session.getAttribute("date_format"));
								
							%>
								<div id="frmdate"></div>
								<input type="text" name="fromDate" id="fromDate" value="<%=endDate%>" readonly size="12"/>
    							<input type="button" style=" width:19px; height: 19px; background: url('<%=request.getContextPath() %>/LMSImages/imagesCal/dateIcon.gif'); top left; border:0 ; " onclick="displayCalendar(document.getElementById('fromDate'), '<s:property value="%{dateFormat}"/>', this, '<%=endDate%>', false, '<%=endDate%>')" />
    	
							</td>
						</tr>
						<tr>
						<td width="100">To Date</td>
							<td>
							 	<div id="todate1"></div>
								<input type="text" name="toDate" id="toDate" value="<%=endDate%>" readonly size="12"/>
    							<input type="button" style=" width:19px; height: 19px; background: url('<%=request.getContextPath() %>/LMSImages/imagesCal/dateIcon.gif'); top left; border:0 ; " onclick="displayCalendar(document.getElementById('toDate'), '<s:property value="%{dateFormat}"/>', this, '<%=endDate%>', false, '<%=endDate%>')" />
								
							</td>
						</tr>
						<tr>
						<td align="right">
						
							<s:submit value="Generate Ledger" cssClass="button" align="right" />
						
						
							</td></tr>					</table>
				</s:form>
			</div>
		</div>
	</body>

</html>
<code id="headId" style="visibility: hidden">
$RCSfile: ret_rep_ledger_Menu.jsp,v $
$Revision: 1.2 $
</code>