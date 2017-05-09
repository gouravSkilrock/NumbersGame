
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="com.skilrock.lms.common.utility.CommonMethods"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.util.*" %>
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
		<script type="text/javascript" src="<%=request.getContextPath() %>/com/skilrock/lms/web/reportsMgmt/backOffice/js/ledger.js" />		
		<s:head theme="ajax" debug="false" />
		<script>isMulLedgerType='<%=application.getAttribute("IS_MUL_LEDGER_TYPE")%>';</script>
	</head>





	<body onload="onload()">
		<%@include
				file="/com/skilrock/lms/web/loginMgmt/menu.jsp"%>

			 
		<div id="wrap">

			<div id="top_form">
				<h3>
					Ledger 
				</h3>



				<s:form name="ledger" method="POST" onsubmit="return readScrp1()" target="_blank" action="bo_rep_agentLedger_Generate">
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
								<tr>
									<td colspan="2">
										<span id="list1" onclick="changeValue()"></span>


									</td>
								</tr>
						
							<tr id="selType">
								<td width="100">
									Select Type
								</td>
								<td>
									<s:radio name="ledgerType" theme="simple" value="'Accountwise'"
										list="#{'Accountwise':'Accountwise','Retailerwise':#application.TIER_MAP.RETAILER + 'wise','All':'Journal'}"
										onclick="retrieveURL('bo_rep_agentLedger_FetchAccList.action?ledgerType=' + this.value,'list')" />
								</td>
							</tr>
				

						<tr>
							<td colspan="2">
								<span id="accountDiv"></span>
							</td>
						</tr>
						<tr>
							<td colspan="2">
								<span id="list"></span>

							</td>
						</tr>
						
						<s:set name="enddate" value="#session.presentDate"></s:set>
						<s:set name="dateFormat" value="#session.date_format"></s:set>	
						<tr>
						<td width="100">From Date</td>
						<td>
							<%
									String endDate = CommonMethods.convertDateInGlobalFormat((String)session.getAttribute("presentDate"), "yyyy-mm-dd", (String)session.getAttribute("date_format"));
									SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
									SimpleDateFormat dateFormat = new SimpleDateFormat((String)session.getAttribute("date_format"));
									String newStartDate = CommonMethods.getLastArchDate();
									Date date1  = format.parse(newStartDate);
									Calendar cal = Calendar.getInstance();
									cal.setTime(date1);
									cal.add(Calendar.DAY_OF_MONTH, 1);
									String newStartDate1 = dateFormat.format(cal.getTime());
							%>
								<div id="frmdate"></div>
								<input type="text" name="fromDate" id="fromDate" value="<%=newStartDate1%>" readonly size="12"/>
    							<input type="button" style=" width:19px; height: 19px; background: url('<%=request.getContextPath() %>/LMSImages/imagesCal/dateIcon.gif'); top left; border:0 ; " onclick="displayCalendar(document.getElementById('fromDate'), '<s:property value="%{dateFormat}"/>', this, '<%=newStartDate1%>', '<%=newStartDate1%>','<%=endDate%>')" />
    	
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
						
							<s:submit value="GenerateLedger" align="right"
								cssClass="button" id="GenerateLedger"/>


						
							<s:submit value="GenerateLedger" align="right"
								cssClass="button" targets="down" theme="ajax"  id="GenerateLedgerNew"/>
						
						<s:hidden name="type" value="agent" />
						<s:hidden name="AgtLedger" value="yes" />
						<s:hidden name="Agt" value="0" />
							</td></tr>					</table>
				</s:form>
				<div id="down" style="text-align: center"></div>
			</div>
		</div>
	</body>

</html>
<code id="headId" style="visibility: hidden">
$RCSfile: bo_rep_agentLedger_Menu.jsp,v $
$Revision: 1.2 $
</code>