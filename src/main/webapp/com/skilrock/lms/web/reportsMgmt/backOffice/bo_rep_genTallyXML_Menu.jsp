








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
		<script type="text/javascript" src="<%=request.getContextPath() %>/com/skilrock/lms/web/reportsMgmt/backOffice/js/tallyXML.js" />
		<s:head theme="ajax" debug="false" />
	</head>





	<body onload="onload1()">
		<%@include file="/com/skilrock/lms/web/loginMgmt/menu.jsp"%>

		<div id="wrap">

			<div id="top_form">
				<h3>
					Tally XML
				</h3>



				<s:form name="tallyXML" method="POST">
						<table border="2" cellpadding="2" cellsapacing="0" width="450">

						<tr>
							<td colspan="2">
								<s:actionerror />
								<s:fielderror />
							</td>
						</tr>

						<tr>
							<td >
								<div id="compDiv"></div>
								<s:textfield name="compName" id="compName" label="Company Name" />

							</td>
						</tr>
						<tr>
							<td nowrap="nowrap">
								Select Tally Version
							</td>
							<td>
								<div id="tallyDiv"></div>
								<s:radio theme="simple" name="tallyVersion" value="'Tally7.2'"
									list="#{'Tally7.2':'Tally 7.2','Tally9':'Tally 9'}" />

							</td>
						</tr>
						
							<tr>
								<td>
									Select Type
								</td>
								<td>

									<s:radio theme="simple" name="xmlType" value="'Accountwise'"
										list="#{'Accountwise':'Accountwise','All Accounts':'All'}"
										onclick="retrieveURL('bo_rep_genTallyXML_FetchAccList.action?xmlType=' +this.value,'accountDiv')" />

								</td>
							</tr>
						
						<!--<s:if test="%{type=='agent'}">

							<tr>
								<td width="100">
									Select Type
								</td>
								<td>
									<s:radio name="xmlType" theme="simple" value="'Accountwise'"
										list="#{'Accountwise':'Accountwise','All Accounts':'All'}"
										onclick="retrieveURL('agentXMLAjax.action?xmlType=' +this.value,'accountDiv')" />
								</td>
							</tr>
						</s:if>
						-->
						<s:set name="enddate" value="#session.presentDate"></s:set>
						<s:set name="dateFormat" value="#session.date_format"></s:set>
						<tr>
							<td colspan="2">
								<span id="acctDiv"></span>
							</td>
						</tr>
						<tr>
						<td>
						<div>
						
						</div>
						</td>
						</tr>
							<tr id="accountRow">
							<td align='left'>Select Account: </td>
							<td>
							<select class='option' name='tallyAccount' id='tallyAccount'>
							<OPTION VALUE=-1>--Please Select--</OPTION>
							</select><span id="accountDiv" style="display:none;"></span>
							</td>
						</tr>

						<%
							String endDate = CommonMethods.convertDateInGlobalFormat((String)session.getAttribute("presentDate"), "yyyy-mm-dd", (String)session.getAttribute("date_format"));
								
						%>

						<tr>
							<td>
							<div id="frmdate"></div>
								<label class="label">
									Start Date
									<span>*</span>:&nbsp;
								</label></td><td>
								<input type="text" name="fromDate" id="fromDate" value="<%=endDate%>" readonly="readonly" size="12"/>
								<input type="button"
									style="width: 19px; height: 19px; background: url('<%=request.getContextPath()%>/LMSImages/imagesCal/dateIcon.gif'); top left; border: 0;"
									onclick="displayCalendar(document.getElementById('fromDate'),'<s:property value="%{dateFormat}"/>', this, '<%=endDate%>', false, '<%=endDate%>')" />
							</td>
						</tr>
						<tr>
							<td><div id="todate1"></div>
								<label class="label">
									End date
									<span>*</span>:&nbsp;
								</label></td><td>
								<input type="text" name="toDate" id="toDate" value="<%=endDate%>" readonly="readonly" size="12"/>
								<input type="button"
									style="width: 19px; height: 19px; background: url('<%=request.getContextPath()%>/LMSImages/imagesCal/dateIcon.gif'); top left; border: 0;"
									onclick="displayCalendar(document.getElementById('toDate'),'<s:property value="%{dateFormat}"/>', this, '<%=endDate%>', false, '<%=endDate%>')" />
							</td>
						</tr>
						<tr>
							<td align="right">

								
									<s:submit value="GenerateXML" action="bo_rep_genTallyXML_Save"
										align="right" onclick="return validate()" cssClass="button" />
								
							</td>
						</tr>
					</table>
				</s:form>
			</div>
		</div>
	</body>

</html>
<code id="headId" style="visibility: hidden">
$RCSfile: bo_rep_genTallyXML_Menu.jsp,v $
$Revision: 1.2 $
</code>