<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%response.setDateHeader("Expires",
   System.currentTimeMillis() + 10*24*60*60*1000);%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">

<head>
	<s:head theme="ajax" debug="false"/>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<link rel="stylesheet" href="<%=request.getContextPath() %>/LMSImages/css/styles.css"  type="text/css"/>
	<title><%=application.getAttribute("JSP_PAGE_TITLE")%></title>		
</head>

<body>
	
	<div id="wrap"><div id="top_form">
    
 
 		<table width="1000px" border="1" cellpadding="3" cellspacing="0" bordercolor="#CCCCCC" align="center">
		<tr>				
			<th colspan="7" align="center"><s:text name="label.ola.bo.deposit/withdrawl.rep"/></th>										
		</tr>
		<tr>
			<th colspan="1"></th>
			<th colspan="2" align="center"><s:text name="label.deposit.details"/></th>
			<th colspan="2" align="center"><s:text name="label.withdrawl.details"/></th>
			<th colspan="2" align="center"><s:text name="label.net.gaming"/></th>
		</tr>
		<tr >				
			<th><s:text name="label.agent.org"/></th>
			<th><s:text name="label.depo.amt"/></th>
			<th><s:text name="label.commission.amt"/></th>
			<th><s:text name="label.wdl.amt"/></th>
			<th><s:text name="label.commission.amt"/></th>
			<th><s:text name="label.plr.net.gaming"/></th>
			<th><s:text name="label.commission.calculated"/></th>
		</tr>
		
		<s:if test="#session.OLA_DEP_WITH_DATA_LIST.size>0">
			<s:iterator id="beanCart" value="#session.OLA_DEP_WITH_DATA_LIST">
				<s:if test="%{(playerName != 'Total')}">
			<tr class="startSortable">				
				<td align="left"><s:property value="agentName"/></td>
				<td align="left"><s:property value="depositAmt"/></td>
				<td align="left"><s:property value="getText('{0,number,#,##0.00}', {agentDepositCommission})"/></td>
				<td align="left"><s:property value="withdrawlAmt"/></td>
				<td align="left"><s:property value="getText('{0,number,#,##0.00}', {agentWithdrawlCommission})"/></td>
				<td align="left"><s:property value="agentNetGaming"/></td>
				<td align="left"><s:property value="agentCommissionCalculated"/></td>							
			</tr>	
			</s:if>
			<s:else>
				<tr>				
				<td align="left"><s:property value="playerName"/></td>
				<td align="left"><s:property value="totalDepositAmount"/></td>
				<td align="left"><s:property value="getText('{0,number,#,##0.00}', {totalDepositCommission})"/></td>
				<td align="left"><s:property value="totalWithdrawlAmount"/></td>
				<td align="left"><s:property value="getText('{0,number,#,##0.00}', {totalWithdrawlCommission})"/></td>
				<td align="left"><s:property value="totalPlayerNetGaming"/></td>
				<td align="left"><s:property value="commissionCalculated"/></td>			
						
			</tr>	
			</s:else>
			</s:iterator>
			
			<div>
			<table width="100%" border="1" cellpadding="3" cellspacing="0" bordercolor="#CCCCCC" align="center" style="font-size: 12px" style>
			<tr align="center">
				<th colspan="2" >
					<s:text name="label.direct.plr.deposit/withdrawl.at.bo"/>
				</th>
			</tr>
			<tr align="center">
				
					<th>
						<s:text name="label.total.deposit"/>
					</th>
				
				
					<th>
						<s:text name="label.total.withdrawl"/>
					</th>
				
			</tr>
		
			<tr>
				
				
					<td align="right">
						<s:property value="#session.BO_BEAN.depositAmt" />
					</td>
				
				
					<td align="right">
						<s:property value="%{#session.BO_BEAN.withdrawlAmt}" />
					</td>
				
			</tr>
		</table>
	</div>
		 
		</s:if>
		<s:else>
			<tr>
				<td colspan="6" align="center"> <s:text name="msg.no.record.process"/></td>
			</tr>
		</s:else>		
	
	 </table>
	</div>
</div>


	
	
 	
 	 
</body>
</html>
<code id="headId" style="visibility: hidden">
$RCSfile: bo_rep_dep_with_ola_report_result.jsp,v $
$Revision: 1.3 $
</code>