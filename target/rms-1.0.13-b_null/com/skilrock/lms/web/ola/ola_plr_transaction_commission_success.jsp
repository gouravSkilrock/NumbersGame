<%@page import="com.skilrock.lms.beans.DateBeans"%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="com.skilrock.lms.common.utility.FormatNumber"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
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
			     <th>Player Name</th>
			     <th>Total NetGaming</th>
			     <th>Total commission </th>
			     			     
			     
			    
			   </tr>
			 	 <s:set name="netGameTotal" value="0.0" />
			    <s:set name="commTotal" value="0.0" />
			  <s:iterator value="%{olaCommissionBeanList}">
			  <tr>
<td><s:a href="olaPlrTransactionCommissionSEARCH.action?playerId=%{playerId}&date=%{date}&netGamingExpType=%{netGamingExpType}" theme="ajax" targets="playerData" >
<s:property value="playerId"/>
</s:a></td>
<td align="right">

<s:set name="totalPlayerNetGaming" value="totalPlayerNetGaming" />
<%=FormatNumber.formatNumberForJSP(pageContext
							.getAttribute("totalPlayerNetGaming"))%>
<s:set name="netGameTotal" value="%{#netGameTotal+#totalPlayerNetGaming}"/>							
							</td>
<td align="right">

<s:set name="totalCommissionCalculated" value="totalCommissionCalculated" />
<%=FormatNumber.formatNumberForJSP(pageContext
							.getAttribute("totalCommissionCalculated"))%>
<s:set name="commTotal" value="%{#commTotal+#totalCommissionCalculated}"/>							

</td>

</tr>
</s:iterator>
<tr><th>Total</th>
<th align="right"><%=FormatNumber.formatNumberForJSP(pageContext
							.getAttribute("netGameTotal"))%></th>
<th align="right"><%=FormatNumber.formatNumberForJSP(pageContext
							.getAttribute("commTotal"))%></th>
</tr>			 
			 
				
			
			 </table> <br/><br/><br/><br/></div></div>


</s:else>
<div id="playerData"></div>
</body>
</html>