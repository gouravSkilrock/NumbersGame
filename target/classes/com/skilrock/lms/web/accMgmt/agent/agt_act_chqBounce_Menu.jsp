<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">

<head>



<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />

<link rel="stylesheet" href="<%=request.getContextPath() %>/LMSImages/css/styles.css"  type="text/css"/>
<script src="<%=request.getContextPath() %>/com/skilrock/lms/web/accMgmt/agent/js/agt_accMgmt_validation.js" />
		<title><%=application.getAttribute("JSP_PAGE_TITLE")%></title>
<s:head debug="false"/>
	</head>

	<body>
	<%@include file="/com/skilrock/lms/web/loginMgmt/menu.jsp"%>
<div id="wrap">
<div id="top_form">
<h3>RetailerChequeBounce</h3>
	<s:div  id="x">
		<s:form name="retailerChequeBounce" action="agt_act_chqBounce_Search" onsubmit="return chequeBounce()">
			<table border="0" cellpadding="2" cellspacing="0" width="270"> 
		        <tr><td colspan="2">
		            <div id="chequeNumber21"></div>
		        </td></tr>
				<tr>
				<td colspan="2"><s:textfield label="Enter Cheque Number" name="chequeNumber" id="chequeNumber2" size="10" maxlength="10" value="" required="true"></s:textfield></td>
				</tr>
										
				
			<tr>
			<td align="center"><s:submit theme="simple" targets="bottom" value="Search"cssClass="button"/>
			<s:reset theme="simple"   value="Reset" cssClass="button" onclick="return removeMsgs()"/></td>
			</tr>			

			</table>
			
			</s:form>
			 
   
     <s:div theme="ajax" id="bottom" >  
			
		
		 </s:div>
 </s:div></div></div>
	</body>
</html>
<code id="headId" style="visibility: hidden">
$RCSfile: agt_act_chqBounce_Menu.jsp,v $
$Revision: 1.2 $
</code>