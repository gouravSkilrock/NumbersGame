
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
	<title><%=application.getAttribute("JSP_PAGE_TITLE")%></title>
	<link rel="stylesheet" href="<%=request.getContextPath() %>/LMSImages/css/styles.css"  type="text/css"/>
	<s:head theme="ajax" />
</head>

<body>
<%@include file="/com/skilrock/lms/web/loginMgmt/menu.jsp"%>
		<div  style="height:158px;overflow-y: auto;">		
		<table width="800" border="1" cellpadding="4" cellspacing="0" id="drawResTbl" bordercolor="#CCCCCC" align="center">
		  <tr><th colspan="4" align="center" style="color: red"><s:property value="#session.RESULT_LIST.responseMessage" /> LIST</th></tr>
		  <tr>		  
		  
		    <th width="15%" scope="col" align="center">Draw Id</th>
		    <th width="25%" scope="col" align="center">Draw <s:text name="Freeze" /> Time</th>
		    <th width="25%" scope="col" align="center">Draw Time</th>
		    <th width="25%" scope="col" align="center">Status</th>		    
		  </tr>		
		  <s:iterator value="#session.RESULT_LIST.drawScheduleList">
			  <tr id="<s:property value="draw_id" />">
			    <td>
			    	&nbsp;<s:property value="draw_id" />
			    </td>
			    <td><s:property value="updatedFreezeTime" /></td>
			    <td><s:property value="draw_time" /></td>	
			    <td><font color="green"><s:property value="status" /></font></td>			  		   
			  </tr>
		  </s:iterator>	
		  <tr><td colspan="4" align="right">  
		  <s:a href="drawMenu.action" theme="simple">Back to Search Results</s:a>
		  </td></tr>
		</table>
	</div>
	
	
	
	
</body>
</html>

<code id="headId" style="visibility: hidden">
$RCSfile: holdSuccess.jsp,v $
$Revision: 1.2 $
</code>