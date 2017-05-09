<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%response.setDateHeader("Expires",
   System.currentTimeMillis() + 10*24*60*60*1000);%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">

<head>
	<s:head theme="ajax" debug="false"/>
	<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
	<link rel="stylesheet" href="<%=request.getContextPath() %>/LMSImages/css/styles.css"  type="text/css"/>
	<title><%=application.getAttribute("JSP_PAGE_TITLE")%></title>		
</head>

<body>
	<s:if test="%{arch == true}">
			<div align="center">
				<h2>
					<u>Please Select start date after <s:property value="%{lastPurgeDate}" />
					</u>
				</h2>
			</div>
	</s:if>
	<s:else>
	<div id="wrap"><div id="top_form">    
 
 		<table width="800px" border="1" cellpadding="3" cellspacing="0" bordercolor="#CCCCCC" align="center">
 		
		<tr>				
			<!--<th colspan="7" align="center">OLA Results</th> -->
			<th colspan="5" align="center">OLA Results</th>									
		</tr>
		<!--<tr>
			<th colspan="1"></th>
			<th colspan="2" align="center">Deposit Details</th>
			<th colspan="2" align="center">Withdrawal Details</th>
			<th colspan="2" align="center">Net Gaming</th>
		</tr> -->
		<tr >				
			<th>Player Name</th>
			<th>Player Type</th>
			<th>Deposit Amount in <s:property value="#application.CURRENCY_SYMBOL" /></th>
			<!--<th>Commission Amount</th>
			--><th>Withdrawal Amount in <s:property value="#application.CURRENCY_SYMBOL" /></th>
			<!-- <th>Transaction Date(yyyy-MM-dd)</th> -->
			<!--<th>Commission Amount</th>
			<th>Player Net Gaming</th>
			<th>Commission Calculated</th>
		-->
		</tr>
		<s:if test="#session.OLA_DATA_LIST.size>1">
			<s:iterator id="beanCart" value="#session.OLA_DATA_LIST">
				<s:if test="%{(playerName != 'Total')}">
			<tr class="startSortable">				
				<td align="left"><s:property value="playerName"/></td>
				<td align="left"><s:property value="playerType"/></td>
				<td align="right"><s:property value="depositAmt"/></td>
				<!--
				<td align="left"><s:property value="retailerDepositCommission"/></td> -->
				<td align="right"><s:property value="withdrawlAmt"/></td>
			<%-- 	<td align="left"><s:property value="trnDate"/></td> --%>
				<!--<td align="left"><s:property value="retailerWithdrawlCommission"/></td>
				<td align="left"><s:property value="retailerNetGaming"/></td>
				<td align="left"><s:property value="commissionCalculated"/></td> -->
				</tr>	
			</s:if>
			<s:else>
				<tr>				
				<td align="left"><s:property value="playerName"/></td>
				<td align="right"></td>
				<td align="right"><s:property value="depositAmt"/></td>
				
				<!--<td align="left"><s:property value="retailerDepositCommission"/></td>	-->
				<td align="right"><s:property value="withdrawlAmt"/></td>
			<!-- 	<td align="right"></td> -->
				<!--<td align="left"><s:property value="retailerWithdrawlCommission"/></td>
				<td align="left"><s:property value="retailerNetGaming"/></td>
				<td align="left"><s:property value="commissionCalculated"/></td> -->
			</tr>	
			</s:else>
			</s:iterator> 
		</s:if>
		<s:else>
			<tr>
				<td colspan="6" align="center"> No Records to Process</td>
			</tr>
		</s:else>		
	 </table>
	 
	
  	

	</div></div>
	</s:else>
 	
 	 
</body>
</html>
<code id="headId" style="visibility: hidden">
$RCSfile: ret_rep_ola_report_result.jsp,v $
$Revision: 1.3 $
</code>