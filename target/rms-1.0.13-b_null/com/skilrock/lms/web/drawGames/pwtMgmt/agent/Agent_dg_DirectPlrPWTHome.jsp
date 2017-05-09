<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<%
	response.setHeader("Cache-Control","no-store"); //HTTP 1.1
	response.setHeader("Pragma","no-cache"); //HTTP 1.0
	response.setDateHeader ("Expires", 0); //prevents caching at the proxy server
%>
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">

<head>
	<s:head theme="ajax" debug="false"/>
	<meta http-equiv="CACHE-CONTROL" content="NO-CACHE" />
	<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
	<link rel="stylesheet" href="<%=request.getContextPath() %>/LMSImages/css/styles.css"  type="text/css"/>
	<title><%=application.getAttribute("JSP_PAGE_TITLE")%></title>
	<script>
     	var path = "<%=request.getContextPath() %>";         
    </script>
    
	<script type="text/javascript" src="<%=request.getContextPath() %>/com/skilrock/lms/web/scratchService/pwtMgmt/agent/js/agt_pwtMgmt_gameTktVirnvalidate.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath() %>/com/skilrock/lms/web/drawGames/pwtMgmt/js/pwtTktValidation.js"></script>
</head>

<body>
<%@include file="/com/skilrock/lms/web/loginMgmt/menu.jsp" %>

<div id="wrap"><div id="top_form">

  <h3>Winning Verification(Direct Player)</h3>	
	<div id="first">
		<div id="tktNbrErrMsg"></div>
	  <s:form id="ab" name="tv" action="/com/skilrock/lms/web/drawGames/pwtMgmt/pwt_agtDirectplr_verifyTicket_dg.action" onsubmit="return _subTrans()"> 		
			 <table  border="0" cellpadding="2" cellspacing="0"  width="400px">
			 	<s:actionerror/>
			 	<tr>				
					<td colspan="2"><div style="color: red; text-align: center" id="gName_e"></div></td>
				</tr>
			 	
				<tr>				
					<td><s:textfield name="ticketNbr" id="ticketNbr" label="Ticket Number" maxlength = "20" required="true" onkeyup="return moveFocusOnSubmit(this.value);"/></td>
				</tr>
						
				<tr>
					<td>&nbsp;</td>							
					<td><div id="subCheck" style="position:absolute;"></div><table><s:submit  formId="ab" id="subTrans" value="Verify And Save" align="right" cssClass="button" name="search"/></table></td>
				</tr>		
			 </table>		 
	  </s:form> 
	  <div id="down"></div>
	</div>	
	
</div></div>
<div id="loading"></div>

</body>
</html>
<code id="headId" style="visibility: hidden">
$RCSfile: Agent_dg_DirectPlrPWTHome.jsp,v $
$Revision: 1.2 $
</code>