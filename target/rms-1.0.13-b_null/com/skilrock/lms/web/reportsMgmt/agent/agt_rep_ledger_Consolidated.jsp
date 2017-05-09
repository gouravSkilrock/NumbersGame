<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="java.util.*"%>
<%@page import="com.skilrock.lms.common.utility.FormatNumber"%>
<%@page import="com.skilrock.lms.beans.RetLedAccDetailsBean"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%
		response.setHeader("Cache-Control", "no-store"); //HTTP 1.1
		response.setHeader("Pragma", "no-cache"); //HTTP 1.0
		response.setDateHeader("Expires", 0); //prevents caching at the proxy server
	%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
	
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
<link rel="stylesheet" href="<%=request.getContextPath() %>/LMSImages/css/styles.css"
			type="text/css" />
</head>
<body>
<div id="wrap">
    <div id="top_form">
		<%
			System.out.println("**Consolidated *******8");
		
			List consolidatedList = (List)session.getAttribute("CONSOLIDATED_LED_LIST");
			System.out.println("*******************22222222222 "+consolidatedList.size());
			Map<String, Double> accountMap=(Map<String, Double>)consolidatedList.get(9);
		 	List<RetLedAccDetailsBean> detLedAccBeanlist=(List<RetLedAccDetailsBean>)consolidatedList.get(10);		
	 		TreeMap<String, Map<String,RetLedAccDetailsBean>> ledDayWiseMap=(TreeMap<String, Map<String,RetLedAccDetailsBean>>)consolidatedList.get(11);
			Map<String,RetLedAccDetailsBean> ledDayMap=null;
			RetLedAccDetailsBean retLedAccDetailsBean=null;
			String retName=(String)consolidatedList.get(12);
		System.out.println("************* "+retName);
			
		 %>
		
	 	<table width="100%" border="1" cellspacing="0" cellpadding="4" bgcolor="#f7f7f7" bordercolor="#eeeeee">
		  <tr>
			<td width="30%" ><b>Back Office Ledger</b></td>
			<td width="10%" align="left" >From: </td>
			<td width="25%" class="bdr_btm" bgcolor="#eeeeee"><%=consolidatedList.get(0)%></td>
			<td width="10%" align="right" >To: </td>
			<td width="25%"  bgcolor="#eeeeee"><%=consolidatedList.get(1)%></td>
		  </tr>
		</table><br />
			
		<table width="100%" border="1" cellspacing="0" cellpadding="0" bgcolor="#FFF8F0" bordercolor="#FFF0DF">
  		<tr><td align="center" colspan="4"><h4 style="padding-top: 9px"> Ledger <s:text name="Report"/> for <s:property value="#application.TIER_MAP.RETAILER" /> : <%=retName %></h4></td></tr>
  		<tr><td>
  		<table  width="100%" border="1" cellspacing="0" cellpadding="4" bgcolor="#FFF8F0" bordercolor="#FFF0DF">
  		  <tr>
			<td width="25%" align="right" class="bdr_btm">Opening Balance Date: </td>
			<td width="21%" class="bdr_btm" bgcolor="#f8ede0"><%=consolidatedList.get(0)%></td>
			<td width="29%" align="right" class="bdr_btm">Opening Balance : </td>
			<td width="25%" class="bdr_btm" bgcolor="#f8ede0"><%=FormatNumber.formatNumberForJSP(consolidatedList.get(8))%></td>
			 </tr> </table ><br />
			 <table border="0" width="100%" cellspacing="0" cellpadding="0">
			 <tr><td>
			 <table width="100%" border="1" cellspacing="0" cellpadding="4" bgcolor="#FFF8F0" bordercolor="#FFF0DF" id="conDet">
        <tr>
        <th align="left" nowrap="nowrap">Transaction Type</th>
        <th align="left" nowrap="nowrap">Balance</th>
        </tr>
        <%
		 	Iterator acctItr = accountMap.entrySet().iterator();
				while (acctItr.hasNext()) {
				Map.Entry<String, Double> acctItrpair = (Map.Entry<String, Double>)acctItr.next();
				
				
			 
			 %>
				<tr><td class="label" width="50%" nowrap="nowrap">
				<a href="#" title="Click to See Date/Day Wise Ledger" onclick="showDayWise()"><%=acctItrpair.getKey()%></a></td>
				<td class="data" width="50%" nowrap="nowrap"><%=FormatNumber.formatNumberForJSP(acctItrpair.getValue())%></td>
                </tr>
			 <%
			 	}	%>
		 
		 </table>
		 </td>
		 </tr>
		 <tr><Td><table width="100%" border="1" cellspacing="0" cellpadding="4" bgcolor="#FFF8F0" bordercolor="#FFF0DF" id="conDayWise" style="display:none">
        <tr>
        <th align="left" nowrap="nowrap">Transaction Date</th>
        <th align="left" nowrap="nowrap">Particulars</th>
        <th align="left" nowrap="nowrap">Balance</th>
        </tr>
        <%
        System.out.println(" ************* #### "+ledDayWiseMap.size());
			 
		 	Iterator ledDayWiseItr = ledDayWiseMap.entrySet().iterator();
				while (ledDayWiseItr.hasNext()) {
				Map.Entry<String, Map<String,RetLedAccDetailsBean>> ledDayWiseItrPair =(Map.Entry<String, Map<String,RetLedAccDetailsBean>>)ledDayWiseItr.next();
				ledDayMap=ledDayWiseItrPair.getValue();
				Iterator ledDayItr = ledDayMap.entrySet().iterator();
				while(ledDayItr.hasNext()){
				 Map.Entry<String,RetLedAccDetailsBean> ledDayItrPair=(Map.Entry<String,RetLedAccDetailsBean>)ledDayItr.next();
				 retLedAccDetailsBean=ledDayItrPair.getValue();
				 System.out.println(retLedAccDetailsBean.getLedTransType()+" ************* "+ledDayWiseItrPair.getKey());
			 %>
				<tr><td class="label"  nowrap="nowrap">
				<a href="#" title="Click to See Detailed Ledger" onclick="showAllDet()"><%=ledDayWiseItrPair.getKey()%></a></td>
				<td class="label"  nowrap="nowrap"><%=retLedAccDetailsBean.getLedTransType()%></td>
				<td class="data"  nowrap="nowrap"><%=FormatNumber.formatNumberForJSP(retLedAccDetailsBean.getAmount())%></td>
				</tr>
			 <%
			 }
			 	}	%>
		 
		 </table>
		 </td>
		 </tr>
		 <tr><Td><table width="100%" border="1" cellspacing="0" cellpadding="4" bgcolor="#FFF8F0" bordercolor="#FFF0DF" id="conAllDet" style="display:none">
        <tr>
        <th align="left" nowrap="nowrap">Transaction Date</th>
        <th align="left" nowrap="nowrap">Voucher No</th>
        <th align="left" nowrap="nowrap">Particulars</th>
        <th align="left" nowrap="nowrap">Debit</th>
        <th align="left" nowrap="nowrap">Credit</th>
        <th align="left" nowrap="nowrap">Balance</th>
        </tr>
        <%
		 	Iterator retLedDetItr = detLedAccBeanlist.listIterator();
				double debit=0.0;
				double credit=0.0;
				String debitstr="";
				String creditstr="";
				while (retLedDetItr.hasNext()) {
				retLedAccDetailsBean = (RetLedAccDetailsBean)retLedDetItr.next();
				if(retLedAccDetailsBean.getAmount()<0.0){
				 debit=retLedAccDetailsBean.getAmount();
				 debitstr=FormatNumber.formatNumberForJSP(debit);
				 creditstr="";
				}else{
				credit=retLedAccDetailsBean.getAmount();
				creditstr=FormatNumber.formatNumberForJSP(credit);
				 debitstr="";
				}
				
			 
			 %>
				<tr><td class="label" width="9%" nowrap="nowrap"><%=retLedAccDetailsBean.getTrDate()%></td>
				<td class="label" width="9%" nowrap="nowrap"><%=retLedAccDetailsBean.getTransactionID()%></td>
				<td class="label" width="12%" nowrap="nowrap"><%=retLedAccDetailsBean.getLedTransType()%></td>
				<td class="label" width="20%" nowrap="nowrap"><%=debitstr%></td>
				<td class="label" width="20%" nowrap="nowrap"><%=creditstr%></td>
				<td class="label" width="30%" nowrap="nowrap"><%=FormatNumber.formatNumberForJSP(retLedAccDetailsBean.getAccBalance())%></td>
				</tr>
			 <%
			 	}	%>
		 
		 </table>
		 </td>
		 </tr>
		 
			 </table>
			 </td></tr>
           </table>
		
		 </div>
		 
	</div>
</body>
</html>
<code id="headId" style="visibility: hidden">
$RCSfile: agt_rep_ledger_Consolidated.jsp,v $
$Revision: 1.3 $
</code>