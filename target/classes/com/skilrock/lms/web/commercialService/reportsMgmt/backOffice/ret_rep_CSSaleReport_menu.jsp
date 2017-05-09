<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@page import="java.util.Calendar"%>
<%@page import="com.skilrock.lms.common.utility.CommonMethods"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">

<head>
	<s:head theme="ajax" debug="false"/>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<link rel="stylesheet" href="<%=request.getContextPath() %>/LMSImages/css/styles.css"  type="text/css"/>
	<title><%=application.getAttribute("JSP_PAGE_TITLE")%></title>
	<script type="text/javascript" src="<%=request.getContextPath() %>/com/skilrock/lms/web/drawGames/reportsMgmt/retailer/js/report.js"></script>
	<link type="text/css" rel="stylesheet" href="<%=request.getContextPath() %>/LMSImages/css/lmsCalendar.css" media="screen"/>
	<script>var projectName="<%=request.getContextPath() %>"</script>

	<script>
		var projectName="<%=request.getContextPath()%>"
		function setDateField(tvalue) {//alert(tvalue);
				document.getElementById("down").innerHTML="";
				if(tvalue=='Date Wise') {
					//alert("inside if"+document.getElementById("date").style.visibility);
					document.getElementById("date").style.display="block";
				}				
				else{ 
					//alert('--in else'+tvalue);
					if(tvalue=='Current Day') {//alert();
					//alert('<s:property value="#session.presentDate"/>');
					_id.o("start_date").value=_id.o("curDate").value;
					_id.o("end_date").value=_id.o("curDate").value;
					document.getElementById("date").style.display="none";
					}
					else {
						document.getElementById("date").style.display="none";
					}
				}
				_id.i('dates','');
		}
		function clearDiv(){
			_id.i("down","");
			_id.i("result","");
			_id.i("dates","");
		}
		
		
		
	
		function validateDates() {
				var isInValid = false;
				var startDate = document.getElementById("start_date").value;
				var endDate =  document.getElementById("end_date").value;
				if(startDate==null ||endDate==null ){
				return false;
				}
				if(startDate == null || endDate == null){
					return false;
				}
    			if (startDate == "" || endDate == "") {
					isInValid = true;
					document.getElementById("dates").innerHTML = "<font color = 'red'><s:text name='error.dates.empty'/></font>";
				} else {
					var stArr = startDate.split('-');
					var endArr = endDate.split('-');
					var tempStDate = stArr[1] + '-' + stArr[0] + '-' + stArr[2]; 
					var tempEndDate = endArr[1] + '-' + endArr[0] + '-' + endArr[2]; 
					if ((Date.parse(tempEndDate) - Date.parse(tempStDate))<0) {
						isInValid = true;
						document.getElementById("dates").innerHTML = "<font color = 'red'><s:text name='error.enddate.gt.strtdate'/></font>";          
          				return false;
					}
				}
				if (isInValid) {
					if(document.getElementsByName('totaltime')[0].value != 'Date Wise'){
						return true;
					}
					return false;
				}
				return true;
		}			
		
		</script>
		<script type="text/javascript"
			src="<%=request.getContextPath()%>/com/skilrock/lms/web/common/globalJs/calender.js"></script>

	</head>
<body>
	<%@include file="/com/skilrock/lms/web/loginMgmt/menu.jsp"%>
	<div id="wrap">
     <div id="top_form">
  <h3><s:text name="label.comm.serv.sale"/> <s:text name="Report"/></h3>
	
	
  <s:form action="ret_cs_saleReport_search" onsubmit="return validateDates()"> 		
	 <table  border="0" cellpadding="2" cellspacing="0" width="400px">
						<tr>
							<td align="center" colspan="2">
								<s:select name="totaltime" label="Reports Duration "
									list="#{'Current Day':getText('label.current.day'),'Date Wise':getText('label.datewise')}"
									cssClass="option"
									onchange="setDateField(this.value),clearDiv()" />
							</td>
						</tr>
							<tr>
							<td>
								<div id="dates"></div>
							</td>
						</tr>
						<tr>
							<td>
								<div id="dates"></div>
							</td>
						</tr>
						<tr>
							<td align="center" colspan="2">
								<%
									Calendar prevCal = Calendar.getInstance();
										String startDate = CommonMethods
												.convertDateInGlobalFormat(new java.sql.Date(prevCal
														.getTimeInMillis()).toString(), "yyyy-mm-dd",
														(String) session.getAttribute("date_format"));
								%>
								<s:hidden name="curDate" id="curDate" value="<%=startDate%>"></s:hidden>
								<div id="date"
									style="display: none; text-align: left; width: 60%">
									<s:div id="newDates" theme="ajax"
										href="%{request.getContextPath()}/com/skilrock/lms/web/reportsMgmt/rep_common_fetchDate.action">
									</s:div>
								</div>
							</td>

						</tr>
		
		<tr>
			<td><s:submit name="search" key="btn.srch" align="right"  targets="down" theme="ajax" cssClass="button"/></td>
		</tr>
		
	 </table>
	</s:form>
	<div id="down" style="text-align: center" ></div>
	 </div></div>
	
 	
 	 

</body>
</html>
<code id="headId" style="visibility: hidden">
$RCSfile: ret_rep_CSSaleReport_menu.jsp,v $
$Revision: 1.3 $
</code>