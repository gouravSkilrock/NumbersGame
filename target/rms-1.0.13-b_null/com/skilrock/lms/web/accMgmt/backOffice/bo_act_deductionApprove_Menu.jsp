	<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="com.skilrock.lms.common.utility.FormatNumber"%>
	<%@ taglib prefix="s" uri="/struts-tags"%>
	<%@page import="java.text.NumberFormat"%>
	<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
	
	<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
	
	<head>
	
	
	
	<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
	
	<link rel="stylesheet" href="<%=request.getContextPath() %>/LMSImages/css/styles.css" type="text/css"/>
	
	<title><%=application.getAttribute("JSP_PAGE_TITLE")%></title>
		
	
	<s:head theme="ajax" debug="false"/>
		<script type="text/javascript"
			src="<%=request.getContextPath()%>/com/skilrock/lms/web/accMgmt/backOffice/js/SearchVat.js"></script>
		</head>

<body>
		<%@include file="/com/skilrock/lms/web/loginMgmt/menu.jsp"%>

<div id="wrap">

  <div id="top_form">
   <h3>Deduction TO BE APPROVED</h3>


			 <s:form id="searchtds" action="bo_act_deductionApprove_Search"
					onsubmit="return validateFormEntries();">

					<input type="hidden" style="background-color: yellow"
						name="buttonValue" id="hiddenInput" value="Search" />
					<table width="450" border="0" cellpadding="2" cellspacing="0">
						<tr>
							<td align="center" colspan="2">
								<div id="error1"></div>
							</td>
						</tr>
						<tr>
							<td align="right">
								<s:select name="serviceName" id="serviceName" headerKey="-1"
									headerValue="--Please Select--" label="Select Service"
									list="#session.serviceNameMap" cssClass="option" />
							</td>
						</tr>
		
						<tr>
							<td>
							</td>
							<td align="left">
								<table>
									<s:submit name='button' id="searchbutoon" value="Search"
										align="right" targets="result" theme="ajax" cssClass="button"
										onclick="setInputHiddenValue(this.value)" />
								</table>
							</td>
						</tr>
					</table>

				</s:form>
   	<div id="result">
   	
   	</div>
	
	<div id="result1" style="position:absolute;top:350px">
	
	</div>
	
	
	</div></div>		
</body>
</html>
<code id="headId" style="visibility: hidden">
$RCSfile: bo_act_deductionApprove_Menu.jsp,v $
$Revision: 1.5 $
</code>