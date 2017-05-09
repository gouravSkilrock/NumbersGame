<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="com.skilrock.lms.common.utility.FormatNumber"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">

<head>

<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />

<link rel="stylesheet" href="<%=request.getContextPath() %>/LMSImages/css/styles.css" type="text/css"/>

<title><%=application.getAttribute("JSP_PAGE_TITLE")%></title>			
<s:head theme="ajax" debug="false"/>
	</head>

<body>
	
		<div style="margin-top: 20px;float: left;width: 100%;">
			<s:if test="scratchBookOrderDataBean == null">
				Invalid Order Id
			</s:if>
			<table border = "1" width = "97%" bordercolor="#CCCCCC" style="border-collapse: collapse;margin-left: 17px;" >
				<tr>
					<td colspan = "2" align = 'center'><b>Order Detail's</b></td>
				</tr>
				<tr>
					<td>Order Id</td>
					<td><s:property value="scratchBookOrderDataBean.orderId"/></td>
				</tr>
				<tr>
					<td>Order Date</td>
					<td><s:property value="scratchBookOrderDataBean.orderDate"/></td>
				</tr>
				<tr>
					<td>Order Status</td>
					<td><s:property value="scratchBookOrderDataBean.orderStatus"/></td>
				</tr>
				<tr>
					<td>DL Number</td>
					<td><s:property value="scratchBookOrderDataBean.dlNbr"/></td>
				</tr>
				<tr>
					<td colspan = "2">
						<table border = "0" style="border-collapse: collapse;" cellpadding="3"  width = "100%">
							<s:iterator value="scratchBookOrderDataBean.gameDataMap">
								<tr >
									<td colspan = "2" align = 'center' rowspan = "2" style="border-right:1px solid #ccc;border-bottom:1px solid #ccc;"><s:property value="key"/></td>
									<s:iterator value="value">
										<td  style="border-left:1px solid #ccc; border-bottom:1px solid #ccc;" align = 'center' ><s:property value="key"/></td>
									</s:iterator>
								</tr>
								<tr style="border-bottom: 1px solid #ccc;">
									<s:iterator value="value">
										<td  align = 'center' ><s:property value="value"/></td>
									</s:iterator>
								</tr>
							</s:iterator>
						</table>
					</td>
				</tr>
			</table>
		</div>
</body>
</html>
<code id="headId" style="visibility: hidden">
$RCSfile: orderdescriptionRetailer.jsp,v $
$Revision: 1.3 $
</code>