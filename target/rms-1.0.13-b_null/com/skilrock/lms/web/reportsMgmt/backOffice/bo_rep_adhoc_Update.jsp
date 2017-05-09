<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%response.setDateHeader("Expires",
   System.currentTimeMillis() + 10*24*60*60*1000);%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">

<head>
	<s:head theme="ajax" debug="false"/>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
	<link rel="stylesheet" href="<%=request.getContextPath() %>/LMSImages/css/styles.css"  type="text/css"/>
	<title><%=application.getAttribute("JSP_PAGE_TITLE")%></title>
	<link type="text/css" rel="stylesheet" href="<%=request.getContextPath() %>/LMSImages/css/lmsCalendar.css" media="screen"/>
	<script>
	function plsConfirm(){
	var flag=confirm("<s:text name='msg.u.sure.want.update.ledger'/>");
	if(flag){
	return true;
	}
	else{
	return false;
	}
	}
	
	function disableButton(){
	_id.o('updte').disabled=true;
	_id.blk('showMsg');
	}
	</script>
	
</head>

  <body>
  	<%@include file="/com/skilrock/lms/web/loginMgmt/menu.jsp"%>
  	
  	<h3><s:text name="label.report.adhoc.update" /> </h3>
  	
  	<s:form name="adhoc" action="bo_rep_updateLedger_Save" onsubmit="disableButton()">
  	<table border="0" width="100%">
  	<tr> 
  	<td align="center">
  	<s:submit name="updte"  id="updte" value="%{getText('Ad_Hoc_Update')} %{getText('Report')}" align="center" cssClass="button" onclick="return plsConfirm()"></s:submit>
  	</td> 
  	<td id="showMsg" style="display: none;">
  	<s:text name="msg.pls.wait.adhoc.update.running" /> ....
  	</td>
  	</tr>
  	</table>
  	
  	</s:form>
  </body>
</html>
<code id="headId" style="visibility: hidden">
$RCSfile: bo_rep_adhoc_Update.jsp,v $
$Revision: 1.3 $
</code>