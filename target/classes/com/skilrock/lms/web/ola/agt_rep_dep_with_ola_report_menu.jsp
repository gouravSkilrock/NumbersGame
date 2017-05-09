<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="com.skilrock.lms.common.utility.CommonMethods"%>
<%@page import="java.util.Calendar"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%
	java.util.Calendar calendar = java.util.Calendar.getInstance();
	calendar.setTimeInMillis(System.currentTimeMillis());
	calendar.set(calendar.HOUR_OF_DAY, 23);
	calendar.set(calendar.MINUTE, 59);
	calendar.set(calendar.SECOND, 59);
%>
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
			content="text/html; charset=iso-8859-1" />
		<link rel="stylesheet"
			href="<%=request.getContextPath()%>/LMSImages/css/styles.css"
			type="text/css" />
		<title><%=application.getAttribute("JSP_PAGE_TITLE")%></title>
		<script type="text/javascript">
			function validateDates() {			
				var isInValid = false;
				var startDate = _id.o("start_date").value;
				var endDate =  _id.o("end_date").value;	
				if (startDate == "" || endDate == "") {
					isInValid = true;
					_id.o("dates").innerHTML = "<font color = 'red'>Please Enter all the dates</font>";			           
				} else {
					if (endDate < startDate) {
						isInValid = true;
						_id.o("dates").innerHTML = "<font color = 'red'>end date should be greater then or equals to start date</font>";          
			        }
				}				
				if (isInValid) {				
					return false;
				}
				_id.o("dates").innerHTML = "";
				return true;			
			}
	</script>
		<link type="text/css" rel="stylesheet" href="<%=request.getContextPath() %>/LMSImages/css/lmsCalendar.css" media="screen"/>
		<script>var projectName="<%=request.getContextPath() %>"</script>
		<script type="text/javascript" src="<%=request.getContextPath() %>/com/skilrock/lms/web/common/globalJs/calender.js"></script>
		<script type="text/javascript" src="<%=request.getContextPath() %>/com/skilrock/lms/web/ola/js/commonOla.js"></script>
		<script>
 function _subTrans(){
			var returnType = verifyReport();
			//alert(returnType);
			return returnType;
		}
</script>
	</head>

	<align="center"><body onload="fetchActiveWallets('<%=request.getContextPath()%>')" align="center" >
		<%@include file="/com/skilrock/lms/web/loginMgmt/menu.jsp"%>
		<div id="wrap">

			<div id="top_form">
				<h3>
					OLA Agent Deposit/Withdraw Report
				</h3>
				<s:form name="searchgame"  action="olaAgentDepWithReportResult" onsubmit="return validateDates()">

					<table border="0" cellpadding="2" cellspacing="0"  width="400">
					<tr>				
						<td colspan="2"><div style="color: red; text-align: center" id="walletName_e"></div></td>
					</tr>
		 <tr><td>
<s:select label="Wallet Name" name="walletName" headerKey="-1"
														id="walletName" headerValue="--Please Select--"
														 list="{}" 
														cssClass="option" required="true" onchange="verifyWalletNameForReport(this.value);"></s:select>
</td></tr>
		
				
					<tr><td colspan="2"><div id="dates"></div></td></tr>
					<tr> 			
			<td colspan="2" align="center">
			<table align="center" width="400px" cellpadding="3"
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
												Start Date
												<span>*</span>:&nbsp;
											</label>
											<input type="text" name="start_date" id="start_date" 
											value="<%=startDate%>" readonly size="12">
											<input type="button"
												style="width: 19px; height: 19px; background: url('<%=request.getContextPath()%>/LMSImages/imagesCal/dateIcon.gif'); top left; border: 0;"
												onclick="displayCalendar(document.getElementById('start_date'),'yyyy-mm-dd', this, '<%=startDate%>', false, '')" />
										</td>
									</tr>
									<tr>
										<td>
											<label class="label">
												End Date
												<span>*</span>:&nbsp;
											</label>
											<input type="text" name="end_Date" id="end_date"
												value="<%=startDate%>" readonly size="12">
											<input type="button"
												style="width: 19px; height: 19px; background: url('<%=request.getContextPath()%>/LMSImages/imagesCal/dateIcon.gif'); top left; border: 0;"
												onclick="displayCalendar(document.getElementById('end_date'),'yyyy-mm-dd', this, '<%=startDate%>',false, '')" />
										</td>
									</tr>
								</table>
			</td>			
		</tr>
		
		<tr>
			<td colspan="2"><s:submit name="submit" value="Search" align="right"  targets="down" theme="ajax" cssClass="button" onclick="return _subTrans()" /></td>
		</tr>
					</table>

						
				</s:form>

				<div id="down" style="text-align: center"></div>
			</div>
		</div>
	</body></align>
</html>

<code id="headId" style="visibility: hidden">
$RCSfile: agt_rep_dep_with_ola_report_menu.jsp,v $
$Revision: 1.2 $
</code>