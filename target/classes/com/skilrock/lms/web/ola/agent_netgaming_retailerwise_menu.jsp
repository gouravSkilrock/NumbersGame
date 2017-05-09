<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">

	<head>

		<meta http-equiv="Content-Type"
			content="text/html; charset=iso-8859-1" />

		<link rel="stylesheet"
			href="<%=request.getContextPath()%>/LMSImages/css/styles.css"
			type="text/css" />

		<title><%=application.getAttribute("JSP_PAGE_TITLE")%></title>
		<s:head theme="ajax" debug="false" />
		<script>var path = "<%=request.getContextPath()%>";
			</script>
		<script
			src="<%=request.getContextPath()%>/com/skilrock/lms/web/ola/js/netGamingExpense.js" />
		<script></script>
		<link type="text/css" rel="stylesheet"
			href="<%=request.getContextPath()%>/LMSImages/css/lmsCalendar.css"
			media="screen" />
		<script>var projectName="<%=request.getContextPath()%>"</script>
		<script type="text/javascript"
			src="<%=request.getContextPath()%>/com/skilrock/lms/web/common/globalJs/calender.js"></script>
		<script type="text/javascript" src="<%=request.getContextPath() %>/com/skilrock/lms/web/ola/js/commonOla.js"></script>
		
	</head>
	<body onload="fetchActiveWallets('<%=request.getContextPath()%>');fetchCommUpdateType('<%=request.getContextPath()%>'); ">
		<%@include file="/com/skilrock/lms/web/loginMgmt/menu.jsp"%>

		<div id="wrap">

			<div id="top_form">
				<h3>
					Agent Net Gaming RetailerWise Commission Report
					
				</h3>


				<s:form name="agtNetGamingRetailerWiseReportSuccess" id="agtNetGamingRetailerWiseReportSuccess" action="agtNetGamingRetailerWiseReportSuccess" onsubmit="return isSelected()">

					<table border="0" width="100%" cellpadding="2" cellspacing="0">
						<tr>
							<td colspan="2">
								<div style="color: red; text-align: center" id="walletName_e"></div>
							</td>
						</tr>
						<tr>
							
							<td>
									<s:select label="Wallet Name" name="walletName" headerKey="-1"
										id="walletName" headerValue="--Please Select--" list="{}"
										cssClass="option" required="true"/>
								</td>
						</tr>
						
						<tr>
							<td align="right">
								<i>Select Type :</i>
							</td>
							<td nowrap="nowrap">

								<s:select theme="simple" id="netGamingExpType" cssClass="option"
									list="{}" name="netGamingExpType"
									emptyOption="false" headerKey="-1"
									headerValue="--Please Select--"  onchange="resetType()" />
							</td>
						</tr>
						<tr>
							<td align="right">
								<label class="label">
									Select Day of the Week/Date
									<span class="required">*</span>:
								</label>
							</td>
							<td>
								<input type="text" name="date" id="date"
									value="<s:property value="#session.presentDate"/>" readonly
									size="12" onchange="resetType()" />
								<input type="button"
									style=" width:19px; height: 19px; background: url('<%=request.getContextPath()%>/LMSImages/imagesCal/dateIcon.gif'); top left; border:0 ; "
									onclick="displayCalendar(document.getElementById('date'),'yyyy-mm-dd', this, '<s:property value="#session.presentDate"/>',false, '<s:property value="#session.presentDate"/>')" />
							</td>
						</tr>
						<tr>
							<td colspan="2">
								<s:submit name="search" value="Search" align="center"
									targets="reportDiv" theme="ajax" cssClass="button" />
								<div id="orgLoading" style="position: absolute;"></div>
							</td>
						</tr>
					</table>

					<div id="completeDiv"
						style="position: absolute; background-color: #CCCCCC; opacity: 1; filter: alpha(opacity = 60); display: none; width: 830px">
						<center> <b><font size="3">Please Wait Data is
							Being Processed......</font> </b> </center>
					</div>

					<div id="resultDiv" style="display: none">
					</div>
					<br/><br/>
					<br/><br/>
					
					<div id="reportDiv">
					</div>
				</s:form>
			</div>
		</div>

	</body>
</html>

<code id="headId" style="visibility: hidden">
	$RCSfile: agent_netgaming_retailerwise_menu.jsp,v $ $Revision: 1.2 $
</code>