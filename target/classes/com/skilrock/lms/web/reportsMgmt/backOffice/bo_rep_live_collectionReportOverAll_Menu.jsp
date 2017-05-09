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
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<link rel="stylesheet"
			href="<%=request.getContextPath()%>/LMSImages/css/styles.css"
			type="text/css" />
		<title><%=application.getAttribute("JSP_PAGE_TITLE")%></title>
		<script type="text/javascript"
			src="<%=request.getContextPath()%>/com/skilrock/lms/web/common/globalJs/jquery-1.10.1.min.js"></script>
		<script type="text/javascript"
			src="<%=request.getContextPath()%>/com/skilrock/lms/web/reportsMgmt/backOffice/js/report.js"></script>
		<link type="text/css" rel="stylesheet"
			href="<%=request.getContextPath()%>/LMSImages/css/lmsCalendar.css"
			media="screen" />

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
			}
			
			
		function calcTotal(){
	var table = document.getElementById("expandTable");
	var rowCount = table.rows.length;
	var colCount=table.rows[3].getElementsByTagName('td').length;
	var headCount=table.rows[3].getElementsByTagName('th').length;
	for(var c=1;c<colCount+headCount;c++){
		var count=0.0;
		for(var r=3;r<rowCount-1;r++){
			if(c<colCount){
				count=parseFloat(count)+parseFloat((table.rows[r].getElementsByTagName('td')[c].innerHTML).replace(/,/g,""));
			}else{
				count=parseFloat(count)+parseFloat((table.rows[r].getElementsByTagName('th')[c-colCount].innerHTML).replace(/,/g,""));
			}
		}
		var x=table.rows[rowCount-1].insertCell(c);
		x.innerHTML=addCommas(count.toFixed(2));
	}
	
}	


function getData(){
//alert(_id.o('sortable4').innerHTML);
//_id.o('tableData').innerHTML=_id.o('data').innerHTML;
var dd=_id.o('sortable4').innerHTML;
// alert(_id.o('expandData').innerHTML);
var n=dd.replace(_id.o('expandData').innerHTML,"");
var finalstr=n.replace(_id.o('sortRow').innerHTML,"");
//alert(finalstr.replace('<tr id="sortRow"></tr>',""));
document.getElementById('tableData').value=finalstr.replace('<tr id="sortRow"></tr>',"");
 
return false;
}

		</script>
		<script type="text/javascript"
			src="<%=request.getContextPath()%>/com/skilrock/lms/web/common/globalJs/calender.js"></script>

	</head>
	<body>
		<%@include file="/com/skilrock/lms/web/loginMgmt/menu.jsp"%>

		<div id="wrap">
			<div id="top_form">
				<h3>
					<s:text name="Detailed" />
					<s:text name="Collection" />
					<s:text name="Report" />
				</h3>
				<s:form action="bo_rep_live_collectionReportOverAll_Search"
					onsubmit="return validateDates()">
					<table width="450" border="0" cellpadding="2" cellspacing="0">
						<tr>
							<td align="center" colspan="2">
								<div id="errorDiv"></div>
							</td>
						</tr>
						<tr>
							<td align="center" colspan="2">
								<s:select name="totaltime"
									label="%{getText('Report')} Duration "
									list="#{'Current Day':'Current Day','Date Wise':'Date Wise'}"
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
									<div id="dates"></div>
									<s:div id="newDates" theme="ajax"
										href="rep_common_fetchDate.action">
									</s:div>
								</div>
							</td>

						</tr>
						<tr>
							<td>
								<s:submit name="search" key="btn.srch" align="right"
									targets="down" theme="ajax" cssClass="button"
									onclick="clearDiv()" />
							</td>
						</tr>
					</table>
				</s:form>
				<div id="down"></div>
				<div id="result" style="overflow-x: auto; overflow-y: hidden;"></div>
			</div>
		</div>
	</body>
</html>

<code id="headId" style="visibility: hidden">
	$RCSfile: bo_rep_live_collectionReportOverAll_Menu.jsp,v $ $Revision:
	1.1.4.2 $
</code>