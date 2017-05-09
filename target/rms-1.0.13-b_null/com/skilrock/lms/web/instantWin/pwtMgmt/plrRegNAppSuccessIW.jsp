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
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
	<link rel="stylesheet" href="<%=request.getContextPath() %>/LMSImages/css/styles.css"  type="text/css"/>
	<title><%=application.getAttribute("JSP_PAGE_TITLE")%></title>
</head>

<body>
	<%@include file="/com/skilrock/lms/web/loginMgmt/menu.jsp"%>

<div id="wrap"><div id="top_form">

  <h3>
	   <s:text name="label.plr"/>
	  	<s:property value="%{#session.plrPwtAppDetMap.plrBean.firstName}"  /> &nbsp;
	  	<s:property value="%{#session.plrPwtAppDetMap.plrBean.lastName}"  />
	  <s:text name="label.regitrd.for"/> <s:text name="Direct_Player"/> <s:text name="PWT"/> <s:text name="label.process"/>.
  </h3>	
   <h3>
	    <s:text name="label.remarks"/> : - 
	  	<s:property value="%{#session.plrPwtAppDetMap.remarks}"  /> &nbsp;
   </h3>	
	<div id="first">
	 
			 <table  border="0" cellpadding="2" cellspacing="0"  width="400px">
			 	
			 	<tr>	
				 	<td>
				 		<s:text name="label.rec.id"/> : 
				 		 <a  href="<%=request.getContextPath() %>/com/skilrock/lms/web/reportsMgmt/rep_common_openPDF.action" target="_blank">
				 			<s:property value="#session.plrPwtAppDetMap.recId"  />
				 		</a>
				 	</td>			
				</tr>		
			 </table>		 

	</div>
	
</div></div>

</body>
</html>
<code id="headId" style="visibility: hidden">
$RCSfile: plrRegNAppSuccessIW.jsp,v $
$Revision: 1.3 $
</code>