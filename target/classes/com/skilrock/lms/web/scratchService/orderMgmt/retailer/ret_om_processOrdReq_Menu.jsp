<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@page import="com.skilrock.lms.common.utility.CommonMethods"%>
<%@page import="java.util.Calendar"%>
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
	<link rel="stylesheet" href="<%=request.getContextPath()%>/LMSImages/css/styles.css" type="text/css" />
	<title><%=application.getAttribute("JSP_PAGE_TITLE")%></title>
	<link type="text/css" rel="stylesheet" href="<%=request.getContextPath()%>/LMSImages/css/lmsCalendar.css" media="screen" />
	<script>var projectName="<%=request.getContextPath()%>"</script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/com/skilrock/lms/web/common/globalJs/calender.js"></script>
	<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" /> <link rel="stylesheet" href="<%=request.getContextPath()%>/LMSImages/css/styles.css" type="text/css" /> <title><%=application.getAttribute("JSP_PAGE_TITLE")%></title>
<script>
	function validateAllowBook() {
		//return false;
		var flag = true;
		var availablecredit = document.getElementById('availableCredit');
		//alert("available credit is " + availablecredit.value);
		//alert("credit limit is" + availablecredit.value);
		var totalBookPrice = 0;
		var ListSize1 = document.getElementById('ListSize');
		//vat listSize=Integer.parseInt(ListSize1.value);

		var quoteAllow = new Array(ListSize1);
		var quoteRemain = new Array(ListSize1);
		var bookPrice = new Array(ListSize1);
		//var Game= new Array(ListSize1);
		Game = document.getElementsByName('gameName');
		quoteAllow = document.getElementsByName('allowedBooks');
		quoteRemain = document
				.getElementsByName('differenceBtAgentandApprBooks');
		bookPrice = document.getElementsByName('oneBookPrice');
		var numericExpression = /^[0-9]+$/;
		var totalAllowedBooks = 0;
		for ( var i = 0; i < quoteAllow.length; i++) {

			//alert(Game[i].value);
			var allowBook = quoteAllow[i].value;
			var remainBook = quoteRemain[i].value;
			var gamebookPrice = bookPrice[i].value;
			totalBookPrice = gamebookPrice * allowBook;
			//alert( "book price for game" + totalBookPrice);		
			//alert(allowBook);
			//alert(remainBook);
			if (!isNumeric(quoteAllow[i],
					"Please Enter Corect Value for Allowd Book for Game "
							+ Game[i].value)) {
				flag = false;
				return flag;
			}
			var allow = parseInt(allowBook, 10);
			totalAllowedBooks = parseInt(totalAllowedBooks, 10) + allow;
			var remain = parseInt(remainBook, 10);
			//alert("dd"+allow);
			//alert(document.getElementById('allowedBooks1'));
			if (allow > remain) {
				document.getElementById('allowedBooks1').innerHTML = "<font color = 'red'>Alloted Books can not be greater than Remaining Books for Game "
						+ Game[i].value + "</font>";
				flag = false;
				return flag;
			}

			document.getElementById('allowedBooks1').innerHTML = "";
			flag = true;

		}

		if (parseInt(totalAllowedBooks, 10) <= 0) {
			document.getElementById('allowedBooks1').innerHTML = "<font color = 'red'>Zero Books Are not allowed for Approve</font>";
			flag = false;
			return flag;
		}

		if (availablecredit.value < totalBookPrice) {
			//alert("inside if");
			alert(tierMap['RETAILER']
					+ "s's credit limit is not enough to approve these all books ");
			return false;
		}
		return flag;
	}
	function abc(){
		$("html, body").animate({ scrollTop: 500 }, 200);
		}
	function isNumeric(elem, helperMsg) {
		//alert(helperMsg);
		var numericExpression = /^[0-9]+$/;
		if (elem.value.match(numericExpression)) {
			doReset(elem);
			return true;
		} else {
			var elem1 = elem.name + "1";
			document.getElementById(elem1).innerHTML = "<font color = 'red'>"
					+ helperMsg + "</font>";
			alert(helperMsg);
			elem.focus();
			return false;
		}
	}

	function doReset(elem) {
		var elem1 = elem.name + "1";

		document.getElementById(elem1).innerHTML = "";

	}
</script>
	<s:head theme="ajax" debug="false" /> 
</head> 
	<body>
		<%@include file="/com/skilrock/lms/web/loginMgmt/menu.jsp"%>
		<div id="wrap">
			<div id="top_form">
				<h3>Track Order</h3> <br /> 
				<s:form action="ret_om_processOrdReq_Search">
					<table border="0" cellpadding="2" cellspacing="0"> 
						<tr>
							<th>Search Order </th> 
						</tr>
						<tr>
							<td> <s:textfield label="Game Name" name="gameName"></s:textfield></td>
						</tr>
						<tr><td> <s:textfield label="Game Number" name="gameNumber"></s:textfield></td>
						</tr>
						<tr>
							<td><s:select cssClass="option" key="Order Status" name="orderStatus" list="#{'ALL' : 'ALL', 'PROCESSED' : 'PROCESSED','CLOSED' : 'CLOSED','REQUESTED' : 'REQUESTED','DENIED' : 'DENIED','SEMI_PROCESSED' : 'SEMI_PROCESSED','APPROVED' : 'APPROVED','AUTO_APPROVED' : 'AUTO_APPROVED'}" /></td>
						<tr> 
							<td><s:textfield label="Order Id" name="orderNumber" value=""></s:textfield> </td>
						</tr>
						<%
							Calendar prevCal = Calendar.getInstance();
							String startDate = CommonMethods.convertDateInGlobalFormat(new java.sql.Date(prevCal.getTimeInMillis()).toString(), "yyyy-mm-dd", "yyyy-mm-dd");
						%>
						<tr>
							<td colspan = "2">
								<label class="label">
									<s:text name="label.start.date" /><span>*</span>:&nbsp;
								</label>
								<input type="text" name="startDate" id="dateOfDraw" value="<%=startDate%>" readonly size="12">
								<input type="button" style="width: 19px; height: 19px; background: url('<%=request.getContextPath()%>/LMSImages/imagesCal/dateIcon.gif'); top left; border: 0;" onclick="displayCalendar(document.getElementById('dateOfDraw'),'yyyy-mm-dd', this, '<%=startDate%>', '<s:property value="%{depDate}"/>', '')" />
							</td>
						</tr>
						<tr>
							<td colspan = "2">
								<label class="label">
									<s:text name="label.end.date" /><span>*</span>:&nbsp;
								</label>
								<input type="text" name="endDate" id="end_date" value="<%=startDate%>" readonly size="12">
								<input type="button" style="width: 19px; height: 19px; background: url('<%=request.getContextPath()%>/LMSImages/imagesCal/dateIcon.gif'); top left; border: 0;" onclick="displayCalendar(document.getElementById('end_date'),'yyyy-mm-dd', this, '<%=startDate%>',false, '')" />
							</td>
						</tr>
						<tr>
							<td> <s:submit theme="ajax" targets="bottom" value="Search" cssClass="button" /> </td>
						</tr>
					</table>
				</s:form>
				<div id="bottom"></div>
				<div id="dailog" style="background-color: white" title="Ticket Information"></div>
			</div>
		</div> 
		<div id="right"></div> 
	</body>
</html>
<code id="headId" style="visibility: hidden"> $RCSfile:
ret_om_processOrdReq_Menu.jsp,v $ $Revision: 1.3 $ </code>