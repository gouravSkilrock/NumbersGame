<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="com.skilrock.lms.common.utility.FormatNumber"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">

	<head>

		<meta http-equiv="Content-Type"
			content="text/html; charset=utf-8" />

		<link rel="stylesheet"
			href="<%=request.getContextPath() %>/LMSImages/css/styles.css"
			type="text/css" />
		<script type="text/javascript"
			src="<%=request.getContextPath() %>/com/skilrock/lms/web/accMgmt/backOffice/js/ACT_ST5_Validation.js" />
		<title><%=application.getAttribute("JSP_PAGE_TITLE")%></title>
		<s:head debug="false" />
	</head>

	<body>
		<%@include file="/com/skilrock/lms/web/loginMgmt/menu.jsp"%>
		<div id="wrap">
			<div id="top_form">
				<h3>
					<s:text name="label.cash.pay"/>
				</h3>
				<s:iterator value="#session.Receipt_Id">
					<table width="250" border="1" align="center" cellpadding="5"
						cellspacing="0" bordercolor="#CCCCCC">


						<tr>

							<td>
								<div align="center">
									<s:text name="Receipt Id"><s:text name="label.rec.id"/> : </s:text>
									<s:property value="%{#session.Receipt_Id}" />
								</div>
							</td>
						</tr>
						<tr>
							<td>
								<a theme="simple" target="_blank"
									href="<%=request.getContextPath() %>/com/skilrock/lms/web/reportsMgmt/rep_common_openPDF.action">
									<div align="center">
										<s:text name="label.show.rec"/>
									</div> </a>
							</td>
						</tr>
						<tr>
							<td>
								<s:set name="amount" value="%{#session.totalPay}"></s:set>
								<table>
									<s:property value="%{#session.orgName}" />
									<% if((Double)pageContext.getAttribute("amount")>0) {%>
									<s:text name="msg.acc.credited.by" />
									<% 
											out.print("  : "+FormatNumber.formatNumberForJSP(pageContext.getAttribute("amount")));
										}
									   else{%>
									   <s:text name="msg.acc.debit.by" />
									   <%
									   		out.print(" : " +FormatNumber.formatNumberForJSP((Double)pageContext.getAttribute("amount")*-1.0) );
									   		} 
									%>
									<s:property value="%{#application.CURRENCY_SYMBOL}" />
								</table>
							</td>
						</tr>
					</table>
				</s:iterator>
				<a
					href="<%=request.getContextPath() %>/com/skilrock/lms/web/accMgmt/bo_act_cash_Menu.action?request_locale=<%=session.getAttribute("WW_TRANS_I18N_LOCALE") %>"><s:text name="label.click.here.to.make.more.pay"/> </a>

			</div>
		</div>

		<s:if test="#session.APP_DATA !=null">
			<%
		StringBuffer codebaseBuffer = new StringBuffer();
		codebaseBuffer.append(!request.isSecure() ? "http://" : "https://");
		codebaseBuffer.append(request.getServerName());
		if (request.getServerPort() != (!request.isSecure() ? 80 : 443)) {
			codebaseBuffer.append(':');
			codebaseBuffer.append(request.getServerPort());
		}
		codebaseBuffer.append(request.getContextPath());
		codebaseBuffer.append('/');
	%>
			<table width="250" border="1" align="center" cellpadding="5"
				cellspacing="0" bordercolor="#CCCCCC">
				<script
					src="<%=request.getContextPath()%>/com/skilrock/lms/web/drawGames/playMgmt/js/commonRPOS.js"></script>
				<applet code="TicketApplet"
					codebase="<%=codebaseBuffer.toString()%>"
					jnlp_href="applets/App.jnlp" width="200" height="200"
					name="TicketApp" mayscript>
				<param name="data" value="108172000002746000" />
				<div style="font-size: 12px; height: 300px; line-height: center;">
					<table>
						<tr>
							<td height="300px;" align="center">
								<s:text name="msg.no.java.rt.found"/>
								<br />
								<a style="color: red"
									href="<%=request.getContextPath()%>/com/skilrock/lms/web/drawGames/common/jre-1_5_0_12-windows-i586-p.exe"><s:text name="msg.click.to.install"/></a>
							</td>
						</tr>
					</table>
				</div>
				</applet>
			</table>

			<script>setAppDataForVoucher('<%=request.getSession().getAttribute("APP_DATA")%>');
		</script>
		</s:if>
	</body>
</html>
<code id="headId" style="visibility: hidden">
	$RCSfile: bo_act_cash_Success.jsp,v $ $Revision: 1.3 $
</code>