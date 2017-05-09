	<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
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
	
<div id="wrap">
  <div id="top_form">
  
   <h2 style="text-align: center"><u>VAT Component Detail For The Month &nbsp;<s:property value="#session.vatDetailMonth"/>,<s:property value="#session.vatDetailYear"/></u></h2>

	<form>
			 <table width="100%" border="1" cellpadding="3" cellspacing="0" bordercolor="#CCCCCC" align="center">
			   <tr><th colspan="8" align="center">VAT on Inputs &nbsp;<s:property value="#session.vatDetailMonth"/>,<s:property value="#session.vatDetailYear"/></th></tr>
			   <tr>
			     <th align="center" nowrap="nowrap">Date</th>
			     <th align="center">Invoice No.</th>
			     <th align="center">Total Sales</th>
			     <th align="center">Non-Taxable</th>
			     <th align="center">Tax Sales (VAT-INC)</th>
			     <th align="center">Invoice VAT exc.</th>
			     <th align="center">V.A.T.</th>
			     <th align="center">Supplier Name</th>
			  </tr>
			  
			  <s:iterator value="#session.vatDetailMap.purchaseDetailsList">
			  <tr>			  	
			     	<td align="left" nowrap="nowrap"><s:property value="date" /></td>
			     	<td align="left"><s:property value="invoiceNo" /></td>
			       	<td align="right" nowrap="nowrap"><s:property value="totalSale" /></td>
			       	<td align="right" nowrap="nowrap"><s:property value="nonTaxablePpr" /></td>
			     	<td align="right" nowrap="nowrap"><s:property value="taxSalesVatInc" /></td>
			     	<td align="right" nowrap="nowrap"><s:property value="taxableSale" /></td>
			     	<td align="right" nowrap="nowrap"><s:property value="vatAmount" /></td>
			     	<td align="left"><s:property value="name" /></td>			     	
			 </tr>
			 </s:iterator>
			  	
			  	<tr>
			  		<th align="center" colspan="2">Total </th>
			  		<th align="right" nowrap="nowrap"><s:property value="#session.vatDetailMap.sumPurchaseBean.totalSale" /></th>
			  		<th align="right" nowrap="nowrap"><s:property value="#session.vatDetailMap.sumPurchaseBean.nonTaxablePpr" /></th>	
			  		<th align="right" nowrap="nowrap"><s:property value="#session.vatDetailMap.sumPurchaseBean.taxSalesVatInc" /></th>
			  		<th align="right" nowrap="nowrap"><s:property value="#session.vatDetailMap.sumPurchaseBean.taxableSale" /></th>
			  		<th align="right" nowrap="nowrap"><s:property value="#session.vatDetailMap.sumPurchaseBean.vatAmount" /></th>
			  		<th align="right" nowrap="nowrap">&nbsp;</th>			  		
			  	</tr>
			  	
			 </table> 

			
			
			<br/><br/>
			 <table width="100%" border="1" cellpadding="3" cellspacing="0" bordercolor="#CCCCCC" align="center">
			   <tr><th colspan="8"  align="center">VAT on Outputs &nbsp;<s:property value="#session.vatDetailMonth"/>,<s:property value="#session.vatDetailYear"/></th></tr>
			   <tr>
			     <th align="center" nowrap="nowrap">Date</th>
			     <th align="center">Invoice No.</th>
			     <th align="center">Total Sales</th>
			     <th align="center">Non-Taxable</th>
			     <th align="center">Tax Sales (VAT-INC)</th>		
			  
			     <th align="center">Invoice VAT exc.</th>
			     <th align="center">V.A.T.</th>
			     <th align="center"><s:property value="#application.TIER_MAP.RETAILER" /> Name</th>
			  </tr>
			  
			  <s:iterator value="#session.vatDetailMap.saleDetailsList">
			  <tr>			  	
			     	<td align="left" nowrap="nowrap"><s:property value="date" /></td>
			     	<td align="left"><s:property value="invoiceNo" /></td>
			       	<td align="right" nowrap="nowrap"><s:property value="totalSale" /></td>
			       	<td align="right" nowrap="nowrap"><s:property value="nonTaxablePpr" /></td>
			     	<td align="right" nowrap="nowrap"><s:property value="taxSalesVatInc" /></td>
			     	<td align="right" nowrap="nowrap"><s:property value="taxableSale" /></td>
			     	<td align="right" nowrap="nowrap"><s:property value="vatAmount" /></td>
			     	<td align="left" nowrap="nowrap"><s:property value="name" /></td>			     	
			 </tr>
			 </s:iterator>
			  	
			  	<tr>
			  		<th align="center" colspan="2">Total </th>
			  		<th align="right" nowrap="nowrap"><s:property value="#session.vatDetailMap.sumSaleBean.totalSale" /></th>
			  					  		<th align="right" nowrap="nowrap"><s:property value="#session.vatDetailMap.sumSaleBean.nonTaxablePpr" /></th>
			  		<th align="right" nowrap="nowrap"><s:property value="#session.vatDetailMap.sumSaleBean.taxSalesVatInc" /></th>			
			  		<th align="right" nowrap="nowrap"><s:property value="#session.vatDetailMap.sumSaleBean.taxableSale" /></th>
			  		<th align="right" nowrap="nowrap"><s:property value="#session.vatDetailMap.sumSaleBean.vatAmount" /></th>
			  		<th align="right" nowrap="nowrap">&nbsp;</th>			  		
			  	</tr>
			</table> 
			
			<br/>
			<s:hidden name="taskId" value="%{taskId}" />
			<s:hidden name="month" value="%{month}" />
		
			<table width="100%">
				<tr>
					<td align="left">&nbsp;</td>
					<td align="right">Total Payable VAT Amount is &nbsp;<b><s:property value="#session.vatDetailMap.payableVatAgent"/></b></td>
					<td align="right">&nbsp;</td>
				</tr>
			</table>
	</form>
	</div></div>		
</body>
</html>
<code id="headId" style="visibility: hidden">
$RCSfile: agt_act_vatApprove_Detail.jsp,v $
$Revision: 1.3 $
</code>