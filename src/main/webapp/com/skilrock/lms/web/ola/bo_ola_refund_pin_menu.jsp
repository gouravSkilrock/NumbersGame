<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
	
	<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
	<link rel="stylesheet" href="<%=request.getContextPath()%>/LMSImages/css/styles.css"  type="text/css"/>
	<title><%=application.getAttribute("JSP_PAGE_TITLE")%></title>
	<script type="text/javascript" src="<%=request.getContextPath()%>/com/skilrock/lms/web/ola/js/commonOla.js"></script>
	<s:head theme="ajax" debug="false" />
	<link type="text/css" rel="stylesheet" href="<%=request.getContextPath()%>/LMSImages/css/lmsCalendar.css" media="screen"/>
		<script>var projectName="<%=request.getContextPath()%>"</script>
		<script type="text/javascript" src="<%=request.getContextPath()%>/com/skilrock/lms/web/common/globalJs/calender.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/com/skilrock/lms/web/ola/js/cancelPin.js"></script>
</head>
<body onload="fetchActiveWallets('<%=request.getContextPath()%>')">
	<%@include file="/com/skilrock/lms/web/loginMgmt/menu.jsp"%>
			<div id="wrap">
			<div id="top_form">
			<h1>Cancel Pin  </h1>
			<div style="color:red"><s:actionmessage/></div>
			
			<s:form action="olaRummyPinRefundSubmit" name="refundPin" id="refundPin" onsubmit="return validate()">
				<s:token></s:token>
				<table width="450" border="0" cellpadding="2" cellspacing="0">
				<tr>
				<td>
				<div id="typeMsg"></div>
				</td>
				</tr>
				<tr>
								<td colspan="2">
									<s:select label="Wallet Name" name="walletName" headerKey="-1"
										id="walletName" headerValue="--Please Select--" list="{}"
										cssClass="option" required="true"></s:select>
								</td>
				</tr>
				<tr>
								<td colspan="2">
									<div  id="pinMsg"></div>
								</td>
							</tr>

							<tr>
								<td colspan="2">
									<s:textfield name="pin" label="Pin Number" id="pin"
										maxLength="16"  required="true" />
								</td>
							</tr>
			
				<tr>
								<td colspan="2">
									<div  id="serialMsg"></div>
								</td>
							</tr>

							<tr>
								<td colspan="2">
									<s:textfield name="serial" label="Serial Number" id="serial"
										maxLength="16"  required="true" />
								</td>
							</tr>
				
				<tr>
								<td colspan="2">
									<div  id="amountMsg"></div>
								</td>
							</tr>

							<tr>
								<td colspan="2">
									<s:textfield name="amount" label="Amount" id="amount"
										maxLength="10"  required="true" />
								</td>
							</tr>
				
				<tr>
								<td colspan="2">
									<div  id="plrnameMsg"></div>
								</td>
							</tr>

							<tr>
								<td colspan="2">
									<s:textfield name="plrname" label="Player Name" id="plrname"
										maxLength="30" />
								</td>
							</tr>
				
				<tr>
							<td>
								<s:submit name="submit" value="Cancel Pin"/>
							</td>
						</tr>
				
				</table>
			</s:form>
			</div>
			</div>

</body>

</html>
<code id="headId" style="visibility: hidden">
$RCSfile: bo_ola_refund_pin_menu.jsp,v $
$Revision: 1.2 $
</code>