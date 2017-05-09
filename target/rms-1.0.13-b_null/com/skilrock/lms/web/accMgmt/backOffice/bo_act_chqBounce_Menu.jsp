<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%response.setDateHeader("Expires",
   System.currentTimeMillis() + 10*24*60*60*1000);%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">

<head>



<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />

<link rel="stylesheet" href="<%=request.getContextPath() %>/LMSImages/css/styles.css"  type="text/css"/>

<title><%=application.getAttribute("JSP_PAGE_TITLE")%></title>
	
		<script src="<%=request.getContextPath() %>/com/skilrock/lms/web/accMgmt/backOffice/js/ACT_ST5_Validation.js"></script>
<s:head theme="ajax" debug="false"/>
	</head>


	

	<body>
	
<%@include file="/com/skilrock/lms/web/loginMgmt/menu.jsp" %>

	<div id="wrap">

  <div id="top_form">
   <h3> Cheque Bounce</h3>
	
	
	
	<div  id="x">
		<s:form name="ChequeBounce" action="bo_act_chqBounce_Search" onsubmit="return chequeBounce()">
			
			
			<table border="0" cellpadding="2" cellspacing="0" width="300">
		        <tr><td colspan="2">
		            <div id="chequeNumber21"></div>
		        </td></tr>
				<tr>
				<td colspan="2"><s:textfield  name="chequeNumber" label="Enter Cheque Number" id="chequeNumber2" value="" size="10" maxlength="10" required="true"></s:textfield></td>
			<br/>	</tr>
			
						
				<tr>
				<td colspan="2" align="center"><s:submit theme="simple" targets="bottom" value="Search" cssClass="button" align="right"/>
								<s:reset  theme="simple" value="Reset" cssClass="button" align="right" onclick="return removeAlerts()"/></td>
				</tr>

			</table>
			
			</s:form>
			 </div>
   
     <s:div theme="ajax" id="bottom" >  
			
		
		 </s:div></div>
 </div>
	</body>
</html>
<code id="headId" style="visibility: hidden">
$RCSfile: bo_act_chqBounce_Menu.jsp,v $
$Revision: 1.2 $
</code>