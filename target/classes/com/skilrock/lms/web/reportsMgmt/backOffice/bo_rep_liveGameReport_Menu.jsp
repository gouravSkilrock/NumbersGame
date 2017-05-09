<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
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
		
		function validate(){
			var serviceName=_id.o('serviceName').value;
			var agtOrgId=_id.o('agtOrgId').value;
			var chk=true;
			if(serviceName==-1){
				_id.o('errorDiv').innerHTML="<p style='color: red'>Please Select Service Name</p>";
				chk=false;
			}else if(agtOrgId==-1){
				_id.o('errorDiv').innerHTML="<p style='color: red'>Please Select Organisation Name</p>";
				chk=false;
			}
			return chk;
		}
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
					//_id.o("start_date").value='<s:property value="#session.presentDate"/>';
					//_id.o("end_date").value='<s:property value="#session.presentDate"/>';
					document.getElementById("date").style.display="none";
					}
					else {
						document.getElementById("date").style.display="none";
					}
				}
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
					Live Game <s:text name="Report"/>
				</h3>
				<s:form action="bo_rep_liveGameReport_Search"
					onsubmit="return validate()">
					<table width="450" border="0" cellpadding="2" cellspacing="0">
						<tr>
							<td align="center" colspan="2">
								<div id="errorDiv"></div>
							</td>
						</tr>
						<tr>
							<td align="center" colspan="2">
								<s:select name="serviceName" id="serviceName" headerKey="-1"
									headerValue="--Please Select--" label="Select Service"
									list="#session.serviceNameMap" cssClass="option"
									required="true" onchange="_id.o('down').innerHTML=''" />
							</td>
						</tr>
						<tr>
							<td align="center" colspan="2">
								<s:select name="agtOrgId" id="agtOrgId" headerKey="-1"
									headerValue="--Please Select--" label="Select Organization"
									list="#session.orgMap" cssClass="option" required="true"
									onchange="_id.o('down').innerHTML=''" />
							</td>
						</tr>
						<tr>
							<td align="center" colspan="2">
								<s:select name="totaltime" label="Get %{getText('Collection')} %{getText('Report')} "
									list="#{'Current Day':'Current Day','Date Wise':'Date Wise'}"
									cssClass="option" onchange="setDateField(this.value)" />
							</td>
						</tr>
						<tr>
							<td align="right" colspan="2">
								<div id="date" style="display: none;">
									<s:div id="newDates" theme="ajax"
										href="rep_common_fetchDate.action">
									</s:div>
								</div>
							</td>

						</tr>
						<tr>
							<td>
								<s:submit name="search" value="Search" align="right"
									targets="down" theme="ajax" cssClass="button" />
							</td>
						</tr>
					</table>
				</s:form>
				<div id="down"></div>
			</div>
		</div>
	</body>
</html>

<code id="headId" style="visibility: hidden">
$RCSfile: bo_rep_liveGameReport_Menu.jsp,v $
$Revision: 1.2 $
</code>