
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="com.skilrock.lms.common.utility.FormatNumber"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<s:if test="%{#session.ACA=='LOGIN'}">
	<font color=red><s:text name="msg.user.cur.org.login.as.offline.usr" />.</font>
</s:if>

<s:else>
	<s:set name="extendedLimitStr" value="#session.extendedLimit"></s:set>
	<s:set name="aca" value="#session.ACA"></s:set>
	<%
		if (pageContext.getAttribute("aca").toString().contains(
					"Amount")) {
	%>
	<font color=red><s:property value="#session.ACA" /></font>
	<%
		} else {
			double balance = (Double)session.getAttribute("ACA") - (Double)session.getAttribute("claimBal");
			pageContext.setAttribute("balance",balance);
	%>
	<s:text name="msg.crd.limit.extended.by" /> <font color=red><%=FormatNumber.formatNumberForJSP(pageContext
									.getAttribute("extendedLimitStr"))%></font>
	<s:property value="%{#application.currencyWord}" /> <s:text name="label.for" /> <font
		color=red><s:property value="#session.extendedLimitForDays" />
	</font> <s:text name="label.days" />.<s:text name="label.blnce" /> <font color=green><b><%=FormatNumber.formatNumberForJSP(pageContext
									.getAttribute("balance"))%></b> </font>
	<%
		}
	%>
</s:else>
<%
	session.removeAttribute("ACA");
%>

<code id="headId" style="visibility: hidden">
$RCSfile: bo_um_searchAgtRet_ExtendLimitSuccess.jsp,v $
$Revision: 1.3 $
</code>