
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="com.skilrock.lms.common.utility.FormatNumber"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">

	<head>



		<meta http-equiv="Content-Type"
			content="text/html; charset=iso-8859-1" />

		<link rel="stylesheet"
			href="<%=request.getContextPath()%>/LMSImages/css/styles.css"
			type="text/css" />

		<title><%=application.getAttribute("JSP_PAGE_TITLE")%></title>

		<script type="text/javascript">
	function Confirm()
	{
	if(document.StatusClose.orderStatus.value=="Close"){
	var confirmValue = confirm("You are going to Close this Order...Are you sure?");
   	 if( confirmValue ){
   	 return true;
   	 }else{
   	 return false;
   	}
	}
	}
	</script>
	</head>



	<body>
		<%@include file="/com/skilrock/lms/web/loginMgmt/menu.jsp"%>

		<div id="wrap">

			<div id="top_form">
				<h3>
					Change Order Status
				</h3>

				<s:actionerror />
				<s:form name="StatusClose" id="b">

					<table width="100%">
						<tr>
							<td>
								<table border="1" cellpadding="3" cellspacing="0"
						bordercolor="#CCCCCC" width="100%" align="center">
									<tr>
										<th align="left" width="90">
											Order Number :
										</th>
										<td width="69">
											<s:property value="#session.ORDER_ID" />
											<s:hidden name="orderId" value="%{#session.ORDER_ID}" />
										</td>
										<th width="115" align="left">
											Order Date :
										</th>
										<td width="103">
											<s:property value="#session.ORDER_DATE" />
										</td>
									</tr>
									<tr>
										<th align="left">
											<s:property value="#application.TIER_MAP.AGENT" />
											Name :
										</th>
										<td>
											<s:property value="#session.AGT_ORG_NAME" />
										</td>
										<th align="left">

										</th>
										<td>

										</td>
									</tr>
									<tr>
										<th align="left">
											Address :
										</th>
										<td colspan="3" align="left">
											<s:property value="#session.ORG_ADDR.addrLine1" />
											,
											<s:property value="#session.ORG_ADDR.addrLine2" />
											<s:property value="#session.ORG_ADDR.state" />
											,
											<s:property value="#session.ORG_ADDR.country" />
										</td>
									</tr>

									<tr>
										<th align="left">
											Credit Limit :
										</th>
										<td>
											<s:set name="strCreditLimit" value="%{#session.CREDIT_LIMIT}" /><%=FormatNumber.formatNumberForJSP(pageContext
						.getAttribute("strCreditLimit"))%>
										</td>
										<th align="left">
											Current Credit :
										</th>
										<td>
											<s:set name="strCreditAmt" value="%{#session.CREDIT_AMT}" /><%=FormatNumber.formatNumberForJSP(pageContext
						.getAttribute("strCreditAmt"))%>
										</td>
									</tr>
								</table>
							</td>
						</tr>
						<tr>
						<td>
							<table border="0" cellpadding="3" cellspacing="0" bordercolor="#CCCCCC" width="100%" align="center">
								<tr>
										<td width="184">
											<b><s:text name="Change Order Status To:" />
											</b>
										</td>
										<td>
											<s:select theme="simple" cssClass="option" name="orderStatus"
												list="{'Close'}" value="Close"></s:select>
										</td>
										<td></td>
									</tr>
									<tr>
									<td></td>
										<td align="left">
											<s:submit theme="simple" cssClass="button"
												value="Change Status" action="bo_om_closeOrder_Save"
												formId="b" onclick="return Confirm()" />
										</td>
										<td>
											<s:submit theme="simple" value="Cancel"
												action="bo_om_closeOrder_Menu" formId="b" cssClass="button" />
										</td>
									</tr>
							</table>
						</td>
						</tr>
					</table>
				</s:form>
			</div>
		</div>
	</body>
</html>
<code id="headId" style="visibility: hidden">
	$RCSfile: bo_om_closeOrder_EditDet.jsp,v $ $Revision: 1.3 $
</code>