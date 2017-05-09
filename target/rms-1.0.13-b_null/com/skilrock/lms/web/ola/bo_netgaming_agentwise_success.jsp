<%@page import="com.skilrock.lms.beans.DateBeans"%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@page import="com.skilrock.lms.common.utility.FormatNumber"%>

<%
	response.setHeader("Cache-Control", "no-store"); //HTTP 1.1
	response.setHeader("Pragma", "no-cache"); //HTTP 1.0
	response.setDateHeader("Expires", 0); //prevents caching at the proxy server
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
<s:if test="%{message!=null}">
<s:property value="%{message}"/> 

</s:if>
<s:else>

<div id="wrap">

  <div id="top_form">
   		 <table width="684" border="1" cellpadding="3" cellspacing="0" bordercolor="#CCCCCC" align="center">
				 
			  
			   
			 
			 <tr>
			     <th>Agent Name</th>
			     <th>Total NetGaming</th>
			     
			    <th>Agent commission </th>
			     <th>TDS Agent commission </th>			     
			     <th>Net Agent commission </th>
			   </tr>
			  <s:iterator value="%{olaCommissionBeanList}">
			  <tr>
<td><s:a href="boNetGamingAgentRetailerWiseReportData.action?agtOrgId=%{agtOrdId}&date=%{date}&netGamingExpType=%{netGamingExpType}" theme="ajax" targets="retailerWiseData" >
<s:property value="agtOrgName"/>
</s:a></td>
<td>

<s:set name="agtNetGaming" value="agtNetGaming" />
<%=FormatNumber.formatNumberForJSP(pageContext
							.getAttribute("agtNetGaming"))%>
							</td>

<td>
<s:set name="agtComm" value="agtComm" />
<%=FormatNumber.formatNumberForJSP(pageContext
							.getAttribute("agtComm"))%>
							
							</td>
<td>
<s:set name="tdsagtComm" value="tdsagtComm" />
<%=FormatNumber.formatNumberForJSP(pageContext
							.getAttribute("tdsagtComm"))%>
							
							</td>
<td>
<s:set name="netAgtComm" value="netAgtComm" />
<%=FormatNumber.formatNumberForJSP(pageContext
							.getAttribute("netAgtComm"))%>
							
							</td>

</tr>
</s:iterator>
			 
			 
				
			
			 </table> <br/><br/><br/><br/></div></div>


</s:else>
<div id="retailerWiseData"></div>
</body>
</html>