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
			     <th>Retailer Name</th>
			     <th>Total NetGaming</th>
			     <th>Retailer commission </th>
			     <th>TDS Retailer commission </th>			     
			     <th>Net Retailer commission </th>
			    <th>Agent commission </th>
			     <th>TDS Agent commission </th>			     
			     <th>Net Agent commission </th>
			   </tr>
			   <s:set name="retNetGamingTotal" value="0.0"/>
			   <s:set name="retCommTotal" value="0.0"/>
			   <s:set name="tdsRetCommTotal" value="0.0"/>
			   <s:set name="netRetCommTotal" value="0.0"/>
			   <s:set name="agtCommTotal" value="0.0"/>
			   <s:set name="tdsagtCommTotal" value="0.0"/>
			   <s:set name="netAgtCommTotal" value="0.0"/>  
			  <s:iterator value="%{olaCommissionBeanList}">
			  <tr>
<td><s:a href="agtNetGamingRetailerPlayerWiseReportData.action?retOrgId=%{retOrdId}&date=%{date}&netGamingExpType=%{netGamingExpType}" theme="ajax" targets="playerWiseData" >
<s:property value="retOrgName"/>
</s:a></td>
<td align="right">
<s:set name="retNetGaming" value="retNetGaming" />
<%=FormatNumber.formatNumberForJSP(pageContext
							.getAttribute("retNetGaming"))%>
 <s:set name="retNetGamingTotal" value="%{#retNetGamingTotal+#retNetGaming}"/>							


</td>
<td align="right"><s:set name="retComm" value="retComm" />
<%=FormatNumber.formatNumberForJSP(pageContext
							.getAttribute("retComm"))%>
<s:set name="retCommTotal" value="%{#retCommTotal+#retComm}"/>							
							</td>
<td align="right">
<s:set name="tdsRetComm" value="tdsRetComm" />
<%=FormatNumber.formatNumberForJSP(pageContext
							.getAttribute("tdsRetComm"))%>
<s:set name="tdsRetCommTotal" value="%{#tdsRetCommTotal+#tdsRetComm}"/>							
</td>
<td align="right">

<s:set name="netRetComm" value="netRetComm" />
<%=FormatNumber.formatNumberForJSP(pageContext
							.getAttribute("netRetComm"))%>
  <s:set name="netRetCommTotal" value="%{#netRetCommTotal+#netRetComm}"/>							
							</td>
<td align="right">

<s:set name="agtComm" value="agtComm" />
<%=FormatNumber.formatNumberForJSP(pageContext
							.getAttribute("agtComm"))%></td>
<s:set name="agtCommTotal" value="%{#agtCommTotal+#agtComm}"/>
<td align="right">

<s:set name="tdsagtComm" value="tdsagtComm" />
<%=FormatNumber.formatNumberForJSP(pageContext
							.getAttribute("tdsagtComm"))%>
<s:set name="tdsagtCommTotal" value="%{#tdsagtCommTotal+#tdsagtComm}"/>							
							
							</td>
<td align="right">

<s:set name="netAgtComm" value="netAgtComm" />
<%=FormatNumber.formatNumberForJSP(pageContext
							.getAttribute("netAgtComm"))%>
	   <s:set name="netAgtCommTotal" value="%{#netAgtCommTotal+#netAgtComm}"/> 							
							</td>

</tr>
</s:iterator>
<tr>
<th >Total</th>
<th align="right"><%=FormatNumber.formatNumberForJSP(pageContext
							.getAttribute("retNetGamingTotal"))%></th>
<th align="right"><%=FormatNumber.formatNumberForJSP(pageContext
							.getAttribute("retCommTotal"))%></th>
<th align="right"><%=FormatNumber.formatNumberForJSP(pageContext
							.getAttribute("tdsRetCommTotal"))%></th>
<th align="right"><%=FormatNumber.formatNumberForJSP(pageContext
							.getAttribute("netRetCommTotal"))%></th>
<th align="right"><%=FormatNumber.formatNumberForJSP(pageContext
							.getAttribute("agtCommTotal"))%></th>
<th align="right"><%=FormatNumber.formatNumberForJSP(pageContext
							.getAttribute("tdsagtCommTotal"))%></th>
<th align="right"><%=FormatNumber.formatNumberForJSP(pageContext
							.getAttribute("netAgtCommTotal"))%></th>
</tr>			 
			 
				
			
			 </table> <br/><br/><br/><br/></div></div>


</s:else>
<div id="playerWiseData"></div>
</body>
</html>