<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="com.skilrock.lms.common.utility.FormatNumber"%>
<%@page import="java.util.Calendar"%>
<%@page import="com.skilrock.lms.common.utility.CommonMethods"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">

	<%
		response.setHeader("Cache-Control", "no-store"); //HTTP 1.1
		response.setHeader("Pragma", "no-cache"); //HTTP 1.0
		response.setDateHeader("Expires", 0); //prevents caching at the proxy server
	%>
	<head>
		<meta http-equiv="CACHE-CONTROL" content="NO-CACHE" />
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		
		<title><%=application.getAttribute("JSP_PAGE_TITLE")%></title>
		
		<script>var projectName="<%=request.getContextPath()%>"</script>
		<script>var path = "<%=request.getContextPath()%>"</script>
		
		<link type="text/css" rel="stylesheet" href="<%=request.getContextPath()%>/LMSImages/css/lmsCalendar.css" media="screen" />
		<link rel="stylesheet" href="<%=request.getContextPath()%>/LMSImages/css/styles.css" type="text/css" />
		
		<script type="text/javascript" src="<%=request.getContextPath()%>/com/skilrock/lms/web/common/globalJs/calender.js"></script>
		<script type="text/javascript" src="<%=request.getContextPath()%>/com/skilrock/lms/web/common/globalJs/jquery-1.10.1.min.js"></script>
		<script type="text/javascript" src="<%=request.getContextPath()%>/com/skilrock/lms/web/accMgmt/backOffice/js/rightClickDisable.js"></script>
		
		<s:head theme="ajax" debug="false" />
		
	</head>

	<body>
		<%@include file="/com/skilrock/lms/web/loginMgmt/menu.jsp"%>
		<div id="wrap">
			<div id="top_form">
				
				 <s:if test="bdList != null && bdList.size > 0">
					<table cellspacing="0" cellpadding="2" border="1" id="resultTable" style="	border-collapse: collapse; margin-top:50px; width:500px; margin-left:130px">
   					<thead style="background-color:#e6e6e6">
				   		<tr>
				   			<th>Sr. No.</th>
				   			<th>PA Name</th>
					    	<th>Deposit Amount</th>
					     	<th>Receipt No</th>
					     	<th>Deposit Date</th>
					     	<th>Status</th>
				   		</tr>
  					</thead>
  					<tbody>
						<s:iterator value="%{bdList}" status="task">
						<tr>
							<td><s:property value="%{#task.index+1}"/></td>
							<td><s:property value="agtName"/></td>
							<td><s:property value="totalAmt"/></td>
							<td><a href="<%=request.getContextPath() %>/com/skilrock/lms/web/accMgmt/generateReportPDF.action?receiptId=<s:property value="refNo"/>"><s:property value="refNo"/></a></td>
							<td><s:property value="depositDate"/></td>
							<td>SUCCESS</td>
						</tr>
					</s:iterator>
				</tbody>				
   				</table>
<div style="margin-top:20px"><a href="<%=request.getContextPath() %>/com/skilrock/lms/web/accMgmt/bo_act_multi_bank_deposit_menu.action" style="margin-left:270px;"><b>Please Click Here to make more payments !!</b></a></div>  				</s:if>

			</div>
		</div>
	</body>

</html>

<code id="headId" style="visibility: hidden">
	$RCSfile: bo_act_multi_bank_deposit_success.jsp,v $ $Revision: 1.3 $
</code>