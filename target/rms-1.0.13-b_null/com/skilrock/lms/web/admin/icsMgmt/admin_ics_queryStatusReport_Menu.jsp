<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="java.util.Calendar"%>
<%@page import="com.skilrock.lms.common.utility.CommonMethods"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">

	<head>
		<s:head theme="ajax" debug="false" />
		<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
		<link rel="stylesheet" href="<%=request.getContextPath()%>/LMSImages/css/styles.css" type="text/css" />
		<script>var projectName="<%=request.getContextPath()%>"</script>
		<link type="text/css" rel="stylesheet" href="<%=request.getContextPath()%>/LMSImages/css/lmsCalendar.css" media="screen" />
		<script type="text/javascript" src="<%=request.getContextPath()%>/com/skilrock/lms/web/common/globalJs/calender.js"></script>
		<script type="text/javascript" src="<%=request.getContextPath()%>/com/skilrock/lms/web/common/globalJs/validator.js"></script>
		<script type="text/javascript" src="<%=request.getContextPath()%>/com/skilrock/lms/web/common/globalJs/jquery-1.10.1.min.js"></script>
		<title><%=application.getAttribute("JSP_PAGE_TITLE")%></title>
		<script type="text/javascript">
			function validateMailTo() {
				var mailToNameList = document.getElementById('mailToNameList').value;
				if(mailToNameList == null || mailToNameList.length == 0) {
					$('#mailErrorDiv').html('Please Enter Email Address.');
					return false;
				}
				var email = mailToNameList.split(',');
				for(i=0; i<email.length; i++) {
					if(!validateEmail(email[i].trim())) {
						$('#mailErrorDiv').html('Enter Valid Email Address.');
						return false;
					}
				}
			}

			$(document).ready(function() {
				$('#selectAll').click(function() {
					var x = this.checked;
					$(this).parent().parent().parent().parent().parent()
							.find('.check_one').each(function() {
								this.checked = x;
					});
				});
			});

			function validateDates() {
				var isInValid = false;
				var startDate = _id.o("startDate").value;
				var endDate =  _id.o("endDate").value;	
				if (startDate == "" || endDate == "") {
					isInValid = true;
					_id.o("dates").innerHTML = "<font color = 'red'>Please Enter all the Dates</font>";			           
				} else {
					if (endDate < startDate) {
						isInValid = true;
						_id.o("dates").innerHTML = "<font color = 'red'>End Date Should be Greater Then or Equals to Start Date</font>";          
			        }
				}				
				if (isInValid) {				
					return false;
				}
				_id.o("dates").innerHTML = "";
				return true;			
			}

			function confirmRun() {
				var ret = confirm("Do You Want to Run?");
				return ret;
			}

			function validateForm() {
				var checked = $("#myForm input:checked").length > 0;
				if (!checked) {
					alert("Please Select at least one Query.");
					return false;
				}
			}

			function displaySelDiv(val) {
				$('#down').html('');
				$('#reportDiv, #runICSDiv, #updateICSDiv').hide();

				if(val == 'ICS_REPORT')
					$('#reportDiv').show();
				else if(val == 'ICS_RUN')
					$('#runICSDiv').show();
				else if(val == 'ICS_UPDATE')
					$('#updateICSDiv').show();
			}

			function setUpdateValues(queryId, pqueryDescription, pisCritical, perrorMessage, pstatus) {
				var queryDescription = $('#queryDescription_'+queryId).val();
				var isCritical = $('#isCritical_'+queryId).val();
				var errorMessage = $('#errorMessage_'+queryId).val();
				var status = $('#status_'+queryId).val();

				if(isCritical==pisCritical && errorMessage==perrorMessage && status==pstatus) {
					$('#message_'+queryId).html('Please Update Atleast one Value');
					return false;
				}
				//alert(queryDescription+'-'+isCritical+'-'+errorMessage+'-'+status);
				var paramString = 'queryId='+queryId+'&queryDescription='+queryDescription+"&isCritical="+isCritical+"&errorMessage="+errorMessage+"&status="+status;
				var resp = _ajaxCallDiv('admin_ics_updateIcsQueries_Submit.action', paramString, null);
				$('#message_'+queryId).css("color", "green");
				$('#message_'+queryId).html('Value Updated Successfully');
			}
		</script>
	</head>
	<body>
		<%@include file="/com/skilrock/lms/web/loginMgmt/menu.jsp"%>
		<div id="wrap">
			<div id="top_form">
				<h3>
					ICS Query Status Report
				</h3>
				<s:radio name="optionSel" id="optionSel" list="#{'ICS_REPORT':'ICS Report','ICS_RUN':'ICS Run','ICS_UPDATE':'ICS Update'}"
					value="%{'ICS_REPORT'}" onchange="displaySelDiv(this.value)" />
				<br/>
				<div id="reportDiv">
					<s:form action="admin_ics_queryStatusReport_Search" onsubmit="return validateDates();">
						<s:actionerror />
						<%
							Calendar prevCal = Calendar.getInstance();
								String startDate = CommonMethods.convertDateInGlobalFormat(new java.sql.Date(prevCal.getTimeInMillis()).toString(), "yyyy-mm-dd", "yyyy-mm-dd");
						%>
						<table border="0" cellpadding="2" cellspacing="0" width="360px"
							style="text-align: left">
							<tr>
								<td colspan="2">
									<div id="dates"></div>
								</td>
							</tr>
							<tr>
								<td>
									<label class="label">Start Date<span>*</span></label>
								</td>
								<td>
									<input type="text" name="startDate" id="startDate" value="<%=startDate%>" readonly size="12" />
									<input type="button" style="width: 19px; height: 19px; background: url('<%=request.getContextPath()%>/LMSImages/imagesCal/dateIcon.gif'); top left; border: 0;" onclick="displayCalendar(document.getElementById('startDate'),'yyyy-mm-dd', this, '<%=startDate%>', false, '')" />
								</td>
							</tr>
							<tr>
								<td>
									<label class="label">End Date<span>*</span></label>
								</td>
								<td>
									<input type="text" name="endDate" id="endDate" value="<%=startDate%>" readonly size="12" />
									<input type="button" style="width: 19px; height: 19px; background: url('<%=request.getContextPath()%>/LMSImages/imagesCal/dateIcon.gif'); top left; border: 0;" onclick="displayCalendar(document.getElementById('endDate'),'yyyy-mm-dd', this, '<%=startDate%>', false, '')" />
								</td>
							</tr>
							<tr>
								<td colspan="2">
									<s:submit name="search" value="Search" align="center" targets="down" theme="ajax" cssClass="button" />
								</td>
							</tr>
						</table>
					</s:form>
				</div>

				<div id="runICSDiv" style="display:none; ">
					<s:form id="myForm" action="admin_ics_runIcsQueries_Submit" onsubmit="return validateForm();" theme="simple">
						<table width="100%" border="1" cellpadding="2" cellspacing="0"
							bordercolor="#CCCCCC" align="center" valign="top" id="searchTable">
							<tr>
								<th align="center">S. No.</th>
								<th align="center">Query Title</th>
								<th align="center">Query Description</th>
								<th align="center">Last Successful Date</th>
								<th align="center">Is Critical</th>
								<th align="center">Select All<input type="checkbox" id="selectAll" /></th>
								<th align="center">Run Query</th>
							</tr>
							<s:set name="indx" value="0" />
							<s:iterator value="%{icsReportList}" status="taskIndex">
								<s:if test="%{queryStatus eq 'ACTIVE'}">
									<s:set name="indx" value="%{#indx+1}"/>
									<tr>
										<td><s:property value="#indx"/></td>
										<td><s:property value="queryTitle"/></td>
										<td><s:property value="queryDescription"/></td>
										<td><s:property value="lastSuccessfulDate"/></td>
										<td><s:property value="isCritical"/></td>
										<td><input type="checkbox" name="queryId" value='<s:property value="queryId" />' class="check_one" /></td>
										<s:url id="runQuery" action="admin_ics_runIcsQueries_Submit">
											<s:param name="queryId" value="%{queryId}" />
										</s:url>
										<td><s:a href="%{runQuery}" onclick="return confirmRun();">Run</s:a></td>
									</tr>
								</s:if>
							</s:iterator>
							<tr>
								<td colspan="8" align="center">
									<s:submit value="Run" />
								</td>
							</tr>
						</table>
					</s:form>
				</div>

				<div id="updateICSDiv" style="display:none; ">
					<table width="100%" border="1" cellpadding="2" cellspacing="0"
						bordercolor="#CCCCCC" align="center" valign="top" id="searchTable">
						<tr>
							<th align="center">S. No.</th>
							<th align="center">Query Title</th>
							<!--<th align="center">Query Description</th>-->
							<th align="center">Is Critical</th>
							<th align="center">Error Message</th>
							<th align="center">Status</th>
							<th align="center">Update</th>
							<th align="center">Message</th>
						</tr>
						<s:iterator value="%{icsReportList}" status="taskIndex">
							<tr>
								<td><s:property value="#taskIndex.index+1"/></td>
								<td>
									<s:property value="queryTitle"/>
									<s:textfield id="queryDescription_%{queryId}" value="%{queryDescription}" theme="simple" cssStyle="display:none; " />
								</td>
								<!--<td><s:textfield id="queryDescription_%{queryId}" value="%{queryDescription}" theme="simple" /></td>-->
								<td><s:select id="isCritical_%{queryId}" list="{'YES','NO'}" value="%{isCritical}" cssClass="option" theme="simple" /></td>
								<td><s:textfield id="errorMessage_%{queryId}" value="%{errorMessage}" theme="simple" /></td>
								<td><s:select id="status_%{queryId}" list="{'ACTIVE','INACTIVE'}" value="%{queryStatus}" cssClass="option" theme="simple" /></td>
								<td><s:a href="#" onclick="setUpdateValues('%{queryId}', '%{queryDescription}', '%{isCritical}', '%{errorMessage}', '%{queryStatus}')">Update</s:a></td>
								<td><span id='message_<s:property value="queryId" />' style="color:red; "></span></td>
							</tr>
						</s:iterator>
					</table>
				</div>

				<div id="down" style="text-align: center"></div>
			</div>
		</div>
	</body>
</html>

<code id="headId" style="visibility: hidden">
	$RCSfile: admin_ics_queryStatusReport_Menu.jsp,v $ $Revision: 1.1.2.4 $
</code>