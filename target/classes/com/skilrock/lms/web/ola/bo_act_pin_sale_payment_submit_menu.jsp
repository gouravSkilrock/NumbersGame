<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">

<head>
	<s:head theme="ajax" debug="false"/>
	<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
	<link rel="stylesheet" href="<%=request.getContextPath() %>/LMSImages/css/styles.css"  type="text/css"/>
	<title><%=application.getAttribute("JSP_PAGE_TITLE")%></title>	
	<link type="text/css" rel="stylesheet" href="<%=request.getContextPath() %>/LMSImages/css/lmsCalendar.css" media="screen"/>
	<script>var projectName="<%=request.getContextPath() %>"</script>
	<script type="text/javascript" src="<%=request.getContextPath() %>/com/skilrock/lms/web/common/globalJs/calender.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/com/skilrock/lms/web/ola/js/pinSaleSubmitPayment.js"></script>
	
</head>

<body>
	<%@include file="/com/skilrock/lms/web/loginMgmt/menu.jsp"%>
	<div id="wrap">
     <div id="top_form">
  <h3>Make Pin Sale Payment</h3>
	
	
	
  <s:form action="bo_act_pin_sale_payment_submit_Search" onsubmit="return validateSubmitPayment()"> 	
  	<div id="actionDiv" style="color: red; text-align: left;"><s:actionmessage />
						<s:actionerror /> 		
						</div>	
	 <table  border="0" cellpadding="2" cellspacing="0" width="500">
	 <tr>
								<td colspan="2">
									<div  id="distributorTypeMsg"></div>
								</td>
							</tr>
	 	<tr>
							<td><label class="label">
										Select Pin Distributor
										<span><font color="RED">*</font> </span>:&nbsp;
									</label></td>
								<td >
									<table><s:select name="distributorType" headerKey="-1"
										id="distributorType" headerValue="--Please Select--" list="#{'PW':'PW','OLA':'OLA'}"
										cssClass="option" required="true"
										onchange=" "></s:select></table>
								</td>
		</tr>
		<tr>
			<td colspan="1"><s:submit name="search" id ="search" value="Search" align="center"  targets="down" theme="ajax" cssClass="button" /></td>
			<td></td>	
		</tr>
		
	 </table>
	
	</s:form> 
	
 	 <div id="down" style="text-align: center" ></div>
    </div></div>
</body>
</html>
<code id="headId" style="visibility: hidden">
$RCSfile: bo_act_pin_sale_payment_submit_menu.jsp,v $
$Revision: 1.2 $
</code>