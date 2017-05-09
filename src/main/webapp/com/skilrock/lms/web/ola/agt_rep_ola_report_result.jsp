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
	
	<div id="wrap"><div id="top_form">
	<s:if test="#session.OLA_AGENT_DATA_LIST.size>0">
    	<s:div id="excel" cssStyle="text-align: left;width: 100%;font-size: 9pt"><s:a href="agentReportExpExcel.action ">Export As Excel</s:a></s:div>
    	</s:if>
 
 		<table width="1000px" border="1" cellpadding="3" cellspacing="0" bordercolor="#CCCCCC" align="center">
		<tr>				
			<th colspan="5" align="center">OLA Agent Report Results</th>										
		</tr>
		<tr>
			<th colspan="1"></th>
			<th colspan="2" align="center">Net Gaming</th>
			<th colspan="2">Commission Details</th>
			</tr>
		<tr >				
			<th>Retailer Name</th>
			<th>Player Net Gaming</th>
			<th>Total Commission</th>
			<th>Total Done Commission</th>
			<th>Total Pending Commission</th>
		</tr>
		
			<s:if test="#session.OLA_AGENT_DATA_LIST.size>0">
				<s:iterator id="beanCart" value="#session.OLA_AGENT_DATA_LIST">
					<s:if test="%{(playerName != 'Total')}">
						<tr class="startSortable">				
							<td align="left"><s:property value="playerName"/></td>
							<td align="left"><s:property value="playerNetGaming"/></td>
							<td align="left"><s:property value="commissionCalculated"/></td>					
							<td align="left"><s:property value="doneCommission"/></td>
							<td align="left"><s:property value="pendingCommission"/></td>
						</tr>	
					</s:if>
					
					<s:else>
						<tr>			
							<td align="left"><s:property value="playerName"/></td>
							<td align="left"><s:property value="playerNetGaming"/></td>
							<td align="left"><s:property value="commissionCalculated"/></td>					
							<td align="left"><s:property value="doneCommission"/></td>
							<td align="left"><s:property value="pendingCommission"/></td>	
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
</body>
</html>
<code id="headId" style="visibility: hidden">
$RCSfile: agt_rep_ola_report_result.jsp,v $
$Revision: 1.2 $
</code>