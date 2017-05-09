
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="com.skilrock.lms.common.utility.FormatNumber,com.skilrock.lms.beans.OrganizationBean"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<s:if test="%{#session.ACA=='LOGIN'}">
	<font color=red><s:text name="msg.user.cur.org.login.as.offline.usr" />.</font>
</s:if>

<s:else>
	<s:set name="creditLimitStr" value="#session.creditLimit"></s:set>
	<s:set name="securityDeposit" value="#session.securityDeposit"></s:set>
	<s:set name="aca" value="#session.ACA"></s:set>
	<%
		if (pageContext.getAttribute("aca").toString().contains(
					"Amount")) {
	%>
	<font color="red"><s:property value="#session.ACA" /></font>
	<%
		} else {
		double balance = (Double)session.getAttribute("ACA") - (Double)session.getAttribute("claimBal");
		pageContext.setAttribute("balance",balance);
	%>
<s:text name="new.crdt.limit.is"></s:text> <font color=red><%=FormatNumber.formatNumberForJSP(pageContext
									.getAttribute("creditLimitStr"))%></font>
	<s:property value="%{#application.currencyWord}" /> <s:text name="and.sec.dpst"></s:text> <font
		color=green><b><%=FormatNumber.formatNumberForJSP(pageContext
									.getAttribute("securityDeposit"))%></b> </font>
	<s:property value="%{#application.currencyWord}" />.Balance <font
		color=green><b><%=FormatNumber.formatNumberForJSP(pageContext
									.getAttribute("balance"))%></b> </font>
	<%
		}
	%>
</s:else>
<%
	session.removeAttribute("ACA");
%>

<code id="headId" style="visibility: hidden">
$RCSfile: bo_um_searchAgtRet_CreditLimitSuccess.jsp,v $
$Revision: 1.3 $
</code>