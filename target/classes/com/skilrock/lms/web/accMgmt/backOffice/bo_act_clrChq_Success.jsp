<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="com.skilrock.lms.common.utility.FormatNumber"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">

<head>

<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />

<link rel="stylesheet" href="<%=request.getContextPath() %>/LMSImages/css/styles.css" type="text/css"/>

<title><%=application.getAttribute("JSP_PAGE_TITLE")%></title>
	

	</head>
	<body>
	<%@include file="/com/skilrock/lms/web/loginMgmt/menu.jsp" %>
	<div id="wrap">

  <div id="top_form">
  
  
  
  <s:iterator value="%{#session.agtReciptbeanlist}"><!--
 
  <h5><s:property value="#application.TIER_MAP.AGENT" /> Name:: <s:property value="%{agtOrgName}"/></h5>
  -->
  
  <br/><br/>
  <table border="1" cellpadding="3" cellspacing="0" >
 <s:if test="%{receiptIdForClear!=0}">
	 <tr>
	   <td>Account credited by <s:set name="stramountCreditedby" value="%{amountCreditedBy}" /><%=FormatNumber.formatNumberForJSP(pageContext.getAttribute("stramountCreditedby")) %>
	  <td><a href="<%=request.getContextPath() %>/com/skilrock/lms/web/reportsMgmt/bo_rep_showReceipt.action?receiptId=<s:property value="%{receiptIdForClear}"/> & orgName=<s:property value="%{agtOrgName}"/> & orgType=AGENT" target="_blank"> Show receipt  receipt Id <s:property value="%{receiptIdForClear}"/>  </a></td>
	 </tr>
  </s:if>
  <tr>
  	<s:if test="%{receiptIdForCancel!=0}">
  		<td>Account debited by <s:set name="stramountDebitedby" value="%{amountDebitedBy}" /><%=FormatNumber.formatNumberForJSP(pageContext.getAttribute("stramountDebitedby")) %> 
 		<td> <a href="<%=request.getContextPath() %>/com/skilrock/lms/web/reportsMgmt/bo_rep_showReceipt.action?receiptId=<s:property value="receiptIdForCancel"/> & orgName=<s:property value="%{agtOrgName}"/> & orgType=AGENT" target="_blank"> Show Debit Note  Debit Note Id <s:property value="%{receiptIdForCancel}"/>  </a></td>
    </s:if>
   </tr>
  
  </table>
  </s:iterator>
  
  
   
  </div></div>
	</body>
	</html>
<code id="headId" style="visibility: hidden">
$RCSfile: bo_act_clrChq_Success.jsp,v $
$Revision: 1.2 $
</code>