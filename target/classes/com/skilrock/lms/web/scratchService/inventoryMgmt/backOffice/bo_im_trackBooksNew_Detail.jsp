<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">

<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />

<link rel="stylesheet" href="<%=request.getContextPath()%>/LMSImages/css/styles.css" type="text/css" />

<title><%=application.getAttribute("JSP_PAGE_TITLE")%></title>

<s:head theme="ajax" debug="false" />

<style type="text/css">
.outerBorderLeft {
	border-left: 3px solid #000000;
}
/* .div1{
    float:left;
    margin:100px;
    border:1px solid black;
} */
</style>
</head>
<body>
	<div id="wrap">
		<s:if test="invTransitionWarehouseWiseBean.bookLocation == 0">
			<div style = "text-align: center">Book Doesn't Exits</div>
		</s:if>
		<s:else>
		 <br/><br/>
			<table width="100%" border="1" cellpadding="3" cellspacing="0" bordercolor="#CCCCCC" align="center">
				<tr>
					<td align = "center" colspan = "8"><b>BOOK DETAILS</b></td>
				</tr>
				<tr>
				    <td><b>Game Name</b></td>
					<td><s:property value="invTransitionWarehouseWiseBean.scratchBookDataBean.gameName"/></td>					
					<td><b>Book Price</b></td>
					<td align = "right"><s:property value="invTransitionWarehouseWiseBean.scratchBookDataBean.bookPrice"/></td>
					<td><b>Book Received On</b></td>
					<td align = "right"><s:property value="invTransitionWarehouseWiseBean.scratchBookDataBean.bookReceiveDate"/></td>
					<td><b>Current Status</b></td>
					<td align = "right"><s:property value="invTransitionWarehouseWiseBean.scratchBookDataBean.bookStatus"/></td>
				</tr>
				<tr>
					<td><b>Game Number</b></td>
					<td><s:property value="invTransitionWarehouseWiseBean.scratchBookDataBean.gameNbr"/></td>
					<td><b>Ticket price</b></td>
					<td align = "right"><s:property value="invTransitionWarehouseWiseBean.scratchBookDataBean.ticketPrice"/></td>
					<td><b>Book Activated On</b></td>
					<td align = "right"><s:property value="invTransitionWarehouseWiseBean.scratchBookDataBean.bookActivationDate"/></td>
					<td><b>Total Tickets</b></td>
					<td align = "right"><s:property value="invTransitionWarehouseWiseBean.scratchBookDataBean.totalTkts"/></td>
					
				</tr>
				<tr>
					<td><b>Book Number</b></td>
					<td><s:property value="invTransitionWarehouseWiseBean.scratchBookDataBean.bookNbr"/></td>
					<td><b>Net Amount</b></td>
					<td align = "right"><s:property value="invTransitionWarehouseWiseBean.scratchBookDataBean.bookPrice"/></td>
					<td><b>Invoice Done On</b></td>
					<td align = "right"><s:property value="invTransitionWarehouseWiseBean.scratchBookDataBean.retailerInvoiceDate"/></td>
					<td><b>Tickets Claimed</b></td>
					<td align = "right"><s:property value="invTransitionWarehouseWiseBean.scratchBookDataBean.totalClaimedTkts"/></td>
				</tr>
				<tr>
					<td><b>Book Owner</b></td>
					<td><s:property value="invTransitionWarehouseWiseBean.scratchBookDataBean.ownerType"/></td>
					<td></td><td></td>
					<td><b>Invoice Id</b></td>
					<td align = "right"><s:property value="invTransitionWarehouseWiseBean.scratchBookDataBean.retailerInvoiceId"/></td>
					<td><b>Tickets Sold</b></td>
					<td align = "right"><s:property value="invTransitionWarehouseWiseBean.scratchBookDataBean.soldTkts"/></td>
				</tr>
				<tr>
					<td><b>Book Owner Name</b></td>
					<td><s:property value="invTransitionWarehouseWiseBean.scratchBookDataBean.ownerName"/></td>
					<td></td><td></td>
					<td><b>Invoice Method</b></td>
					<td align = "right"><s:property value="invTransitionWarehouseWiseBean.scratchBookDataBean.retailerInvoiceMethod"/></td>
					<td></td><td></td>
				</tr>
			</table>
			<br />
			<br />
						
			<s:if test="invTransitionWarehouseWiseBean.bookLocation == 1">
			<b>BOOK FLOW</b>
				<table border="0" bordercolor="black" cellspacing="0" cellpadding="0">
					
					<tr>
						<td width="20%"><b><s:text name="label.bo" /></b></td>
						<td width="20%"><b><s:property value="#application.TIER_MAP.AGENT.toUpperCase()" /></b></td>
						<td width="20%"><b><s:property value="#application.TIER_MAP.RETAILER.toUpperCase()" /></b></td></tr> <tr>
						<td width="20%"><img src="<%=request.getContextPath()%>/LMSImages/images/black02.jpg" width="20" height="110" border="0" /></td> 
						<td width="20%"><img src="<%=request.getContextPath()%>/LMSImages/images/black01.jpg" width="20" height="110" border="0" /></td>
						<td width="20%"><img src="<%=request.getContextPath()%>/LMSImages/images/black01.jpg" width="20" height="110" border="0" /></td> 
					</tr> 
				</table>
			</s:if> 
			<s:else>
			  <b>BOOK FLOW</b>
			  <br/><br/>  
				<table border="0" bordercolor="black" cellspacing="0" cellpadding="0">
				   	<tr>
						<s:iterator value="invTransitionWarehouseWiseBean.headers">
							<td><b><s:property /> </b>
							</td>
						</s:iterator>
					</tr>
						<s:iterator value="invTransitionWarehouseWiseBean.warehouseWiseDataBeans">
						<tr>
							<s:set name="startIndex" value="startIndex"></s:set>
							<s:set name="size" value="size"></s:set>
							<s:set name="size100x" value="size * 100"></s:set>
							<s:set name="direction" value="direction"></s:set>
							<s:set name="upperString" value="upperString"></s:set>
							<s:set name="lowerString" value="lowerString"></s:set>
							<s:set name="bookStatusString" value = "bookStatusString"></s:set>
							<s:iterator value="invTransitionWarehouseWiseBean.headers" status="status">
							<%-- <s:iterator value="loopList" status="status"> --%>
								<s:if test="%{#status.index == #startIndex}">
									<td colspan='<s:property value="%{#size}" />' style='border-left: 3px solid #000000'>
										<table>
											<tr>
												<td>
													<label title = '<s:property value="bookStatusString" />'><s:property value="%{#upperString}"/></label>
												</td>
											</tr>
											<tr>
												<s:if test="%{#direction == true}">
													<td><img src="<%=request.getContextPath()%>/LMSImages/images/black.jpg" width='<s:property value="%{#size100x}" />%' height="10" border="0" /></td>
												</s:if>
												<s:else>
													<td><img src="<%=request.getContextPath()%>/LMSImages/images/redNew.jpg" width='<s:property value="%{#size100x}" />%' height="10" border="0" /></td>
												</s:else>
											</tr>
											<tr>
												<td>
												<div i></div>
												<s:property value="%{#lowerString}"/></td>
											</tr>
										</table>
									</td>
								</s:if>
								<s:else>
									<td style = 'border-left: 3px solid #000000'></td>
								</s:else>
								<%-- <s:property value="%{#startIndex}"/>
								<s:property value="%{#size}"/>
								<s:property value="%{#direction}"/> --%>
							</s:iterator>
							</tr>
						</s:iterator>
				</table>
			</s:else>
		</s:else>
	</div>
</body>
</html>
<code id="headId" style="visibility: hidden"> $RCSfile:
	bo_im_trackBooksNew_Detail.jsp,v $ $Revision: 1.3 $ </code>