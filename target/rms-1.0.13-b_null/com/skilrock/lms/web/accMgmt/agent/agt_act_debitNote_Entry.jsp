<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="com.skilrock.lms.common.utility.FormatNumber"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">

<%
	response.setHeader("Cache-Control","no-store"); //HTTP 1.1
	response.setHeader("Pragma","no-cache"); //HTTP 1.0
	response.setDateHeader ("Expires", 0); //prevents caching at the proxy server
%>
	<head>

	<META HTTP-EQUIV="CACHE-CONTROL" CONTENT="NO-CACHE" />



<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />

<link rel="stylesheet" href="<%=request.getContextPath() %>/LMSImages/css/styles.css"  type="text/css"/>

		<title><%=application.getAttribute("JSP_PAGE_TITLE")%></title>
<s:head debug="false"/>
	</head>

	<body>
	<%@include file="/com/skilrock/lms/web/loginMgmt/menu.jsp"%>
<div id="wrap">
<div id="top_form">
<h3><s:property value="#application.TIER_MAP.RETAILER" /> <s:text name="Debit_Note"/></h3>
	<s:div  id="x">
		<s:form  action="agt_act_debitNote_Save">
		<s:token />
		<s:set name="stramount" value="%{amount}" />
			<table border="0" cellpadding="2" cellspacing="0" width="270"> 
		        
				<tr>
				<td colspan="2"><s:text name="label.party.type"/> ::
				<s:property value="%{#application.TIER_MAP[partyType].toUpperCase()}"></s:property>
				</td>
				</tr>
				
				<tr>
				<td colspan="2"><s:property value="#application.TIER_MAP.RETAILER" /> <s:text name="label.name"/> ::  
				<s:property value="%{orgNameValue}"></s:property>
				<s:hidden name="orgName"  value="%{orgName}"/>
				</td>
				</tr>
			
				<tr>
				<td colspan="2"><s:text name="label.amount"/> ::
				<%=FormatNumber.formatNumberForJSP(pageContext.getAttribute("stramount")) %>
				<s:hidden name="amount"  value="%{amount}"/>
				</td>
				</tr>
				
				<tr>
				<td colspan="2"><s:text name="label.remarks"/> ::
				<s:property value="%{remarks}"></s:property>
				<s:hidden name="remarks"  value="%{remarks}"/>
				</td>
				</tr>
				
						
				
			<tr>
			<td align="center">
			<div id="subCheck" style="position:absolute;"></div>
			<s:submit theme="simple" targets="bottom" key="btn.ok" id="subTrans" onclick="return _subValid(this.id);" cssClass="button"/>
			</td>
			</tr>			

			</table>
			
			</s:form>
			 
   
     <s:div theme="ajax" id="bottom" >  
			
		
		 </s:div>
 </s:div></div></div>
	</body>
</html>
<code id="headId" style="visibility: hidden">
$RCSfile: agt_act_debitNote_Entry.jsp,v $
$Revision: 1.3 $
</code>