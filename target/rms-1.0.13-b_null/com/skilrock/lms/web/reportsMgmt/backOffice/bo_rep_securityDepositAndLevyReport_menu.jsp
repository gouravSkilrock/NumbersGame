<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
   <%@page import="com.skilrock.lms.common.utility.CommonMethods"%>
<%@page import="java.util.Calendar"%>
<%@page import="com.skilrock.lms.web.drawGames.common.Util"%>
<%@taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title><%=application.getAttribute("JSP_PAGE_TITLE")%></title>
<link rel="stylesheet" href="<%=request.getContextPath()%>/LMSImages/css/styles.css" type="text/css" />
<script>var projectName="<%=request.getContextPath()%>"</script>
<script type="text/javascript">
	function getAgentList() {
		var text = _ajaxCall(projectName+ "/com/skilrock/lms/web/userMgmt/getAgentNameList.action?orgType=AGENT");
		var firstArr = text.data.split(":");
		var retObj = _id.o("agentOrgId");
		for ( var i = 0; i < firstArr.length - 1; i++) {
			var retNameArray = firstArr[i].split("|");
			var opt = new Option(retNameArray[0].toUpperCase(), retNameArray[1]);
			retObj.options[i + 1] = opt;
		}
	}
</script>
</head>
<body onload="getAgentList();">
	<%@include file="/com/skilrock/lms/web/loginMgmt/menu.jsp"%>
		<div id="wrap_new">
			<div id="top_form">
				<h3 id="main-heading">
					<s:text name="label.sec.depo.and.levy.rep" />
				</h3>
				<s:form id="mForm" action="bo_rep_security_deposit_and_levy_result">
					<table width="450" border="0" cellpadding="2" cellspacing="0">
					<%
						Calendar prevCal = Calendar.getInstance();
						String startDate = CommonMethods.convertDateInGlobalFormat(new java.sql.Date(prevCal.getTimeInMillis()).toString(), "yyyy-mm-dd","yyyy-mm-dd");
						String date = Util.getCurrentTimeString();
					%>
						<s:hidden id="cur_date" value="<%=date.split(" ")[0]%>" />
						<s:hidden id="cur_time" value="<%=date.split(" ")[1]%>" />
							
						<tr>
							<td align="center" colspan="2">
								<div id="errorDiv" style="color: red;"></div>
							</td>
						</tr>
						<s:hidden id="cur_date" value="<%=startDate%>"></s:hidden>
						<tr>
							<td colspan="2">
								<s:select headerKey="-1" key="label.slct.agnt.org"
									headerValue="%{getText('label.ALL')}" name="agentOrgId"
									id="agentOrgId" list="{}" listKey="key" listValue="%{}"
									cssClass="option">
								</s:select>
							</td>
						</tr>
						<tr>
							<td colspan="2" align="center">
								<s:submit name="submit" id="submit" key="btn.submit"
									align="right" theme="ajax" targets="down" cssClass="button" />
							</td>
						</tr>
					</table>
				</s:form>
				<div id="down"></div>
			</div>
		</div>
		<script type="text/javascript" src="<%=request.getContextPath()%>/js/Export_Excel.js"></script>
		<script type="text/javascript" src="<%=request.getContextPath()%>/js/Export_PDF.js"></script>
		<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery.dataTables.js"></script>
</body>
</html>
<code id="headId" style="visibility: hidden">
	$RCSfile: bo_rep_securityDepositAndLevyReport_menu.jsp,v $ $Revision: 1.3 $
</code>