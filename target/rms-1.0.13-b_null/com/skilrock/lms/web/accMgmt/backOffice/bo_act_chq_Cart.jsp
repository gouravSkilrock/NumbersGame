
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="com.skilrock.lms.common.utility.FormatNumber"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">

<head>
		<meta http-equiv="Content-Type"	content="text/html; charset=iso-8859-1" />
		<link rel="stylesheet" href="<%=request.getContextPath() %>/LMSImages/css/styles.css" type="text/css" />
		<title><%=application.getAttribute("JSP_PAGE_TITLE")%></title>
		<s:head theme="ajax" debug="false" />
</head>


<body>
		
		<div id="wrap">
		<div id="top_form">
		<h3>Payment Detail</h3>
		<s:actionerror />
				
		<s:if test="%{#session.CHEQUE_PAYMENT_LIST.size>0}">
			<s:form name="agentPaymentForm">
				<s:token name="token" />
				<table width="684" border="1" cellpadding="3" cellspacing="0" bordercolor="#CCCCCC" align="center">
					<tr>
						<td colspan="5" align="center"><b>Total Payment Description</b></td>
					</tr>
					<tr>
						<th width="30">S.No</th>
						<th>Cheque Nbr</th>
						<th>Bank</th>
						<th>Amount</th>
						<th>Remove</th>
					</tr>
					<s:iterator id="payList" value="#session.CHEQUE_PAYMENT_LIST" status="sno">
					<tr>
						<td><s:property value="#sno.index+1" /><s:set name="sNo" value="#sno.index+1" /></td>
						<td><s:property value="chequeNo" /></td>
						<td><s:property value="bankName" /></td>
						<td><s:set name="strpwtAmount" value="%{strAmount}" /><%=FormatNumber.formatNumberForJSP(pageContext.getAttribute("strpwtAmount")) %></td>
						<td>
							<s:url id="url" action="bo_act_chq_RemoveFrmCart.action">
								<s:param name="sNo" value="%{sNo}"></s:param>
								<s:param name="chequeNumber" value="%{chequeNo}"></s:param>
							</s:url>
							<s:a href="%{url}" theme="ajax" targets="one1">Remove</s:a>
						</td>
					</tr>
					</s:iterator>

						<tr>
						
							<td colspan="5" align="center">
								<div id="subCheck" style="position:absolute;"></div>
								<s:submit theme="simple"  value="Submit Payment" action="bo_act_chq_Save" id="subTrans" onclick="return _subValid(this.id);" cssClass="button" align="right"/>
								<s:submit theme="simple" action="bo_act_chq_CancelCart" value="Cancel" cssClass="button" align="right"/>
							</td>
							
						</tr>
					

						</table>

					</s:form>

				</s:if>
				<s:else>
					<p>
						<b>Nothing to process</b>
					</p>
				</s:else>
			</div>
		</div>
	</body>
</html>
<code id="headId" style="visibility: hidden">
$RCSfile: bo_act_chq_Cart.jsp,v $
$Revision: 1.2 $
</code>