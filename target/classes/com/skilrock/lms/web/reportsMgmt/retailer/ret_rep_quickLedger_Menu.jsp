<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">

<head>
	<s:head theme="ajax" debug="false"/>
	<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
	<link rel="stylesheet" href="<%=request.getContextPath() %>/LMSImages/css/styles.css"  type="text/css"/>
	<title><%=application.getAttribute("JSP_PAGE_TITLE")%></title>
	<script type="text/javascript" src="<%=request.getContextPath() %>/com/skilrock/lms/web/reportsMgmt/retailer/js/ret_rep_ledger.js"></script>
	<link type="text/css" rel="stylesheet" href="<%=request.getContextPath() %>/LMSImages/css/lmsCalendar.css" media="screen"/>
	<script>var projectName="<%=request.getContextPath() %>"</script>
	
</head>

<body>
	<%@include file="/com/skilrock/lms/web/loginMgmt/menu.jsp"%>
	<div id="wrap">
    	<div id="top_form">
	  		<h3>Quick Ledger <s:text name="Report"/></h3>
	  		<s:form name="searchgame" action="ret_rep_quickLedger_Result"> 		
				<table  border="0" cellpadding="2" cellspacing="0" width="400px">
					<tr>
						<td colspan="2"><s:select name="type" label="Get Ledger For " list="{'Current Day','Last Day'}" cssClass="option" onchange="setInputField(this.value)" /></td>
					</tr>
					<tr>
						<td><s:submit name="search" value="Search" align="right"  targets="down" theme="ajax" cssClass="button"/></td>
					</tr>
				</table>
			</s:form>
			<div id="down" style="text-align: center" ></div>
		</div>
	</div>
</body>
</html>
<code id="headId" style="visibility: hidden">
$RCSfile: ret_rep_quickLedger_Menu.jsp,v $
$Revision: 1.2 $
</code>