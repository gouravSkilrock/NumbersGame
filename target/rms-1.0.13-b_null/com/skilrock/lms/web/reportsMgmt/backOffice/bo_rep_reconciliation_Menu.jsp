<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="com.skilrock.lms.common.utility.CommonMethods"%>
<%@page import="java.util.Calendar"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%response.setDateHeader("Expires",  System.currentTimeMillis() + 10*24*60*60*1000);%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
	<%
		response.setHeader("Cache-Control", "no-store"); //HTTP 1.1
		response.setHeader("Pragma", "no-cache"); //HTTP 1.0
		response.setDateHeader("Expires", 0); //prevents caching at the proxy server
	%>
	
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">

	<head>
	
		<META HTTP-EQUIV="CACHE-CONTROL" CONTENT="NO-CACHE" />
		<meta http-equiv="Content-Type"
			content="text/html; charset=iso-8859-1" />
		<link rel="stylesheet" href="<%=request.getContextPath() %>/LMSImages/css/styles.css"
			type="text/css" />
			<link type="text/css" rel="stylesheet" href="<%=request.getContextPath() %>/LMSImages/css/lmsCalendar.css" media="screen"/>
		<title><%=application.getAttribute("JSP_PAGE_TITLE")%></title>
		<script>var projectName="<%=request.getContextPath() %>"</script>
		<script type="text/javascript" src="<%=request.getContextPath() %>/com/skilrock/lms/web/common/globalJs/calender.js"></script>
		<s:head theme="ajax" debug="false" />
	</head>
<script>
function getReconReport(url, divId) {
url = url+"?agtOrgId="+_id.v("orgMap");
ajaxReqDojo(url,'',divId,false)
}
function validateRecon(){
var isInValid = false;

for (var i=0;i < document.getElementsByName("reconViewType").length;i++)
	{
	var e=document.getElementsByName("reconViewType")[i];
	if(e.checked){
	if(e.value=="consolidated"){
	document.getElementById("agttError").innerHTML="";
	}else{
		if(document.getElementById("agentOrgMap").value=="-1"){
	  document.getElementById("agttError").innerHTML="<br><font color = 'red'>Please Select  <s:property value="#application.TIER_MAP.AGENT" /> Name</font>";
	  isInValid=true;
	  }else{
	  document.getElementById("agttError").innerHTML="";
	  }
	}
	
	}

}



var fromDt = document.getElementById("fromDate");
	var fromDtValue = fromDt.value;
	if (fromDtValue == "") {
		document.getElementById("frmdate").innerHTML = "<br><font color = 'red'>Please Enter From Date</font>";
		isInValid = true;
	} else {
		document.getElementById("frmdate").innerHTML = "";
	}
var to = document.getElementById("toDate");
	var toDtValue = to.value;
	  if (toDtValue != "") {
		var toDtArr = toDtValue.split("-");
			var fromDtArr = fromDtValue.split("-");
		if ((fromDtArr[2]+fromDtArr[1]+fromDtArr[0]) >(toDtArr[2]+toDtArr[1]+toDtArr[0])) {
			document.getElementById("todate1").innerHTML = "<br><font color = 'red'>Please Select To date Greater than From Date</font>";
  			isInValid = true;
		} else {
			document.getElementById("todate1").innerHTML = "";
		}
	}

 if(isInValid){
  return false;
  }
  
 
  
  return true;

}

function printTable(page){
		//alert(page);	
		
		//window.open(page);
		window.open(page,'newwin','fullscreen');			
		 //window.open("backOffice/bo_rep_recon_consolidated_print.jsp");	
	}
function divShow(viewType){
if(viewType=="consolidated"){
	pp.style.display="none";
	document.getElementById("agttError").innerHTML="";
	}else{
	pp.style.display="block";
	document.getElementById("agttError").innerHTML="";
	}
	
	
}



</script>
<body onload="divShow('consolidated')">
		<%@include file="/com/skilrock/lms/web/loginMgmt/menu.jsp"%>
		<div id="wrap">
			<div id="top_form">
				<h3>
					Reconciliation <s:text name="Report"/>
				</h3>
				<s:form name="reconReport" action="bo_rep_reconciliation_Detail">
				<tr> <td align="right"eft">Report Type:</td>
				     <td> <s:radio name="reconViewType" id="reconViewType" theme="simple"
						value="'consolidated'"
						list="#{'consolidated':'Consolidated','detailed':'Detailed'}"
						onclick="divShow(this.value)">
				</s:radio> </td>
				</tr>
				<tr>
						<td>
							<div id="agttError"></div>
						</td>
					</tr>
				
				<tr>
				<td colspan="2">
				<table id="pp">
								<s:select cssClass="option" label="Select %{#application.TIER_MAP.AGENT}" id="agentOrgMap"
									name="agentOrgMap" headerValue="--- Please Select ---"
									headerKey="-1" list="orgMap"></s:select>
				</table>
				</td>
				</tr>


				

					<s:set name="enddate" value="#session.presentDate"></s:set>
					<s:set name="dateFormat" value="#session.date_format"></s:set>
					<%
					Calendar prevCal= Calendar.getInstance();
					prevCal.add(Calendar.DATE,-1);
					
					String endDate = CommonMethods.convertDateInGlobalFormat(new java.sql.Date(prevCal.getTimeInMillis()).toString(), "yyyy-mm-dd", (String)session.getAttribute("date_format"));
						%>

					<tr>
						<td align="right">
							<div id="frmdate"></div>
							<label class="label">
								From Date
								<span>*</span>:&nbsp;
							</label>
						</td>
						<td>
							<input type="text" name="fromDate" id="fromDate"
								value="<%=endDate%>" readonly="readonly" size="12" />
							<input type="button"
								style="width: 19px; height: 19px; background: url('<%=request.getContextPath()%>/LMSImages/imagesCal/dateIcon.gif'); top left; border: 0;"
								onclick="displayCalendar(document.getElementById('fromDate'),'<s:property value="%{dateFormat}"/>', this, '<%=endDate%>', false, '<%=endDate%>')" />
						</td>
					</tr>
					<tr>
						<td align="right">
							<div id="todate1"></div>
							<label class="label">
								To date
								<span>*</span>:&nbsp;
							</label>
						</td>
						<td>
							<input type="text" name="toDate" id="toDate" value="<%=endDate%>"
								readonly="readonly" size="12" />
							<input type="button"
								style="width: 19px; height: 19px; background: url('<%=request.getContextPath()%>/LMSImages/imagesCal/dateIcon.gif'); top left; border: 0;"
								onclick="displayCalendar(document.getElementById('toDate'),'<s:property value="%{dateFormat}"/>', this, '<%=endDate%>', false, '<%=endDate%>')" />
						</td>
					</tr>

					<tr>
						<td align="center">


							<s:submit value="GenerateReconReport.." align="right"
								cssClass="button" theme="ajax" targets="down"
								onclick="return validateRecon()" />

						</td>
				</s:form>

				<div id="down" style="text-align: center"></div>
			</div>
		</div>




	</body>
</html>
<code id="headId" style="visibility: hidden">
$RCSfile: bo_rep_reconciliation_Menu.jsp,v $
$Revision: 1.2 $
</code>