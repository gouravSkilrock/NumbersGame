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
		<style type="text/css">
form {
	background-color: #ffffff;
	border: 0 solid #ffffff;
	margin: 0;
	padding: 0;
}
</style>
	</head>
	<body>
		<%@include file="/com/skilrock/lms/web/loginMgmt/menu.jsp"%>
		<div id="wrap">
			<div id="top_form">
				<h3>
					Dispatch Order
				</h3>
				<s:actionerror />
				<s:form name="StatusClose" id="b">
				<table border="0" cellpadding="0" cellspacing="0" width="800"
					bgcolor="#FFFFFF" align="center">
					<tr>
						<td>
							<table border="1" cellpadding="3" cellspacing="0" bordercolor="#CCCCCC" width="100%" bgcolor="#fff">
								<tr>
									<th align="left">
										Order Number :
									</th>
									<td align="left">
										<s:property value="#session.ORDER_ID" />
										<s:hidden name="orderId" value="%{#session.ORDER_ID}" />
									</td>
									<th align="left">
										Order Date :
									</th>
									<td align="left">
										<s:property value="#session.ORDER_DATE" />
									</td>
								</tr>
								<tr>
									<th align="left">
										<s:property value="#application.TIER_MAP.RETAILER" />
										Name :
									</th>
									<td align="left">
										<s:property value="#session.RET_ORG_NAME" />
									</td>
									<th align="left">
										Address :
									</th>
									<td align="left">
										<s:property value="#session.ORG_ADDR.addrLine1" />
										,
										<s:property value="#session.ORG_ADDR.addrLine2" />
										,
										<s:property value="#session.ORG_ADDR.state" />
										,
										<s:property value="#session.ORG_ADDR.country" />
									</td>
								</tr>
								<tr>
										<th colspan='2' align="left">
											Delivery Challan Id :
										</th>
										<td align="left" colspan='2'>
											<s:property value="#session.CHALLAN_ID" />
										</td>
									</tr>
							</table>
							<br />
							</div>
							<center>
							<s:if test="#session.BOOK_LIST.responseCode == 200">
									<s:iterator value="#session.BOOK_LIST.orderData">
								<table border="1" cellpadding="3" cellspacing="0" bordercolor="#CCCCCC" width="100%">
									<tr align="center">
										<p style="font-size: 17px">
											<s:property value="key" />
										</p>
									</tr>
									<s:iterator value="value">
										<tr>
											<td align="center" style="font-size: 13px" colspan="2">
												Pack Number:
												<s:property value="key" />
											</td>
											<td colspan='<s:property value="value.size()" />-2'
												style="font-size: 13px" align="left">
												Number of Books:
												<s:property value="value.size()" />
											</td>
										</tr>
										<tr>
											<s:iterator value="value" status="valueStatus">
												<s:if test="#valueStatus.index%8 == 0">
													</tr>
													<tr>
											</s:if>
											<td align="center" style="color: rgb(12, 12, 113); font-size: 13px">
												<s:property />
											</td>
									</s:iterator>
									</tr>
									</s:iterator>
								</table>
								</br>
							</s:iterator>
							<br />
							<s:submit theme="simple" cssClass="button" value="Dispatch Order"
								action="agt_im_dispatchOrder_Save" formId="b" /></center>
								</s:if>
						</td>
					</tr>
				</table>
				</s:form>
				<s:if test="#session.BOOK_LIST.responseCode neq 200"> 
						<div style="text-align: center">This order can not be dispatched as few books has already been returned/missed.
								</div>
					</s:if>
			</div>
	</body>
</html>
<code id="headId" style="visibility: hidden">
	$RCSfile: agt_im_dispatchOrder_Details.jsp,v $ $Revision: 1.3 $
</code>