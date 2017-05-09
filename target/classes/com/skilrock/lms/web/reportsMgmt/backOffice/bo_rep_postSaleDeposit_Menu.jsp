<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="java.util.Calendar"%>
<%@page import="com.skilrock.lms.common.utility.CommonMethods"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<%
	response.setHeader("Cache-Control", "no-store"); //HTTP 1.1
	response.setHeader("Pragma", "no-cache"); //HTTP 1.0
	response.setDateHeader("Expires", 0); //prevents caching at the proxy server
%>
<html>
	<head>
		<s:head theme="ajax" debug="false" />
		<meta http-equiv="Content-Type"
			content="text/html; charset=iso-8859-1" />
		<link rel="stylesheet"
			href="<%=request.getContextPath()%>/LMSImages/css/styles.css"
			type="text/css" />
		<title><%=application.getAttribute("JSP_PAGE_TITLE")%></title>
		<link type="text/css" rel="stylesheet"
			href="<%=request.getContextPath()%>/LMSImages/css/lmsCalendar.css"
			media="screen" />
				<script>
	var projectName="<%=request.getContextPath()%>"

	function getData(){
		var tblData = document.getElementById("tableDataDiv").innerHTML;
		tblData = tblData.replace(document.getElementById("sortRow").innerHTML,"");
		document.getElementById('tableValue').value = tblData;

		return false;
	}

</script>
		<script type="text/javascript"
			src="<%=request.getContextPath()%>/com/skilrock/lms/web/common/globalJs/calender.js"></script>
				<script type="text/javascript"
			src="<%=request.getContextPath()%>/com/skilrock/lms/web/common/globalJs/jquery-1.10.1.min.js"></script>
			<script  type="text/javascript"	src="<%=request.getContextPath()%>/com/skilrock/lms/web/reportsMgmt/backOffice/js/postSaleDepositRep.js"></script>
			
	
	</head>
	<body>
		<%@include file="/com/skilrock/lms/web/loginMgmt/menu.jsp"%>

		<div id="wrap">
			<div id="top_form">
				<h3>
					Post Sale Deposit Report
				</h3>
				<s:form action="bo_rep_postSaleDeposit_Search"
					onsubmit="return validateDateRange();" theme="simple">
					<table width="450" border="0" cellpadding="2" cellspacing="0">
						<tr>
							<td align="center" colspan="2">
								<div id="errorDiv" style="color: red;"></div>
							</td>
						</tr>
						<tr>
							<td align="left" colspan="2">
								Select Month :&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
								<s:select id="month" name="month"
									list="#{'1':'Jan', '2':'Feb', '3':'Mar', '4':'Apr', '5':'May', '6':'Jun', '7':'Jul', '8':'Aug', '9':'Sep', '10':'Oct', '11':'Nov', '12':'Dec'}"
									cssClass="option" onchange="" />
						
							</td>
						</tr>
						<tr>
							<td align="left" colspan="2">
								Select  Year :&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
								<s:select id="year" name="year"
									list="#{'2011':'2011', '2012':'2012', '2013':'2013', '2014':'2014' ,'2015':'2015'}"
									cssClass="option" onchange="" />
							</td>
						</tr>
								<tr>
											<td align="center" colspan="2">
											<div id="repType_e" style="color: red; text-align: center"></div>
											</td>
											</tr>
						<tr>
												
											<td align="left"  colspan="2">
											Select Bank Type :
										<s:radio id="repType" name="repType" list="#{'E-ZWICH':'E-ZWICH','Other':'Other','ALL':'ALL'}" value="#{'ALL'}" />
										</td>
							</tr>
						<tr>
							<td>
								<s:submit name="search" value="Search" align="right"
									targets="down" theme="ajax" cssClass="button"
									onclick="clearDiv()" />
							</td>
						</tr>
					</table>
				</s:form>
				<div id="down"></div>
				<div id="result"></div>
			</div>
		</div>
	</body>
</html>

<code id="headId" style="visibility: hidden">
	$RCSfile: bo_rep_postSaleDeposit_Menu.jsp,v $ $Revision: 1.2 $
</code>