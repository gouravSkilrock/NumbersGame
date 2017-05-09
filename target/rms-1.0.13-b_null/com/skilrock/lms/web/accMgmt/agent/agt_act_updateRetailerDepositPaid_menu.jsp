
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="com.skilrock.lms.common.utility.CommonMethods"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.util.*" %>
<%@ taglib prefix="s" uri="/struts-tags"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">

	<head>
	<s:head theme="ajax" debug="false" />
		<meta http-equiv="Content-Type"
			content="text/html; charset=iso-8859-1" />
		<link rel="stylesheet" href="<%=request.getContextPath() %>/LMSImages/css/styles.css"
			type="text/css" />
		<title><%=application.getAttribute("JSP_PAGE_TITLE")%></title>
		<link type="text/css" rel="stylesheet" href="<%=request.getContextPath() %>/LMSImages/css/lmsCalendar.css" media="screen"/>
		<script>var projectName="<%=request.getContextPath() %>"</script>
		<script type="text/javascript">
		
		
		function updateAgentComm(){
		//alert("on click");
	   // _id.o("butDiv").style.display="none";
		var text = _ajaxCall("<%=request.getContextPath()%>/com/skilrock/lms/web/accMgmt/agt_act_update_postretailer_deposit_submit.action?startDate="+_id.o("stDate").value+"&endDate="+_id.o("enDate").value+"&retailerOrgId="+_id.o("retailerOrgId").value);
		_id.o("down").innerHTML=text.data;
		
		}
		
function verifyField(){
	
	if(_id.o('retailerOrgId').value==-1){
		alert('Please Select Retailer Name First');
		//_id.o('agentOrgId').focus();
		return false;		
	}
	return true;
	}
			
			
			
	</script>
	
			<script type="text/javascript" src="<%=request.getContextPath() %>/com/skilrock/lms/web/reportsMgmt/backOffice/js/report.js"></script>
	
		<script type="text/javascript" src="<%=request.getContextPath() %>/com/skilrock/lms/web/common/globalJs/calender.js"></script>
	
	</head>

	<body>
	
		<%@include
				file="/com/skilrock/lms/web/loginMgmt/menu.jsp"%>

			 
		<div id="wrap">

			<div id="top_form">
				<h3>
					Approve Retailer Post Sale Commission
				</h3>

          <s:form action="agt_act_update_postretailer_deposit_search" onsubmit="return verifyField();">

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
													</td>
								<td>
									<s:select cssClass="option"  name="retailerOrgId" 
											id="retailerOrgId" list="%{retailerNameList}"
											label="Retailer Name "
											headerValue="--ALL--" headerKey="-1" required="true"></s:select>
								</td>
							</tr>
				<tr id="selType">
								
								<td>
									<s:select cssClass="option" label="Select month" name="month" id="month"
										headerKey="-1" headerValue="--Please Select--" list="#{'0':'January','1':'Febuary','2':'March'
										         ,'3':'April','4':'May','5':'June','6':'July','7':'August','8':'September','9':'October','10':'November','11':'December'}"
										
										/>
								</td>
								<td><s:select cssClass="option" label="Select Year" name="year" id="year"
										headerKey="-1" headerValue="--Please Select--" list="#{'2012':'2012','2013':'2013','2014':'2014','2015':'2015'}"
										
										/></td>
							</tr>

						<tr>
						<td align="right">
						
											
							<s:submit name="search" value="Search" align="right"
									targets="down" theme="ajax" cssClass="button"
									 />
						
					
							</td></tr>					</table>
				</s:form>
						<div id="down"></div>		
				<div id="result" style="overflow-x:auto;overflow-y:hidden;"></div>
			</div>
		</div>

	</body>

</html>
<code id="headId" style="visibility: hidden">
$RCSfile: agt_act_updateRetailerDepositPaid_menu.jsp,v $
$Revision: 1.2 $
</code>