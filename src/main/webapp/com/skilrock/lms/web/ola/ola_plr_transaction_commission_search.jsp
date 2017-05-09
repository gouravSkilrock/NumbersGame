<%@page import="com.skilrock.lms.beans.DateBeans"%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="com.skilrock.lms.common.utility.FormatNumber"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%
	response.setHeader("Cache-Control", "no-store"); //HTTP 1.1
	response.setHeader("Pragma", "no-cache"); //HTTP 1.0
	response.setDateHeader("Expires", 0); //prevents caching at the proxy server
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
<s:if test="%{message!=null}">
<s:property value="%{message}"/> 

</s:if>
<s:else>

<div id="wrap">

  <div id="top_form">
   		 <table width="684" border="1" cellpadding="3" cellspacing="0" bordercolor="#CCCCCC" align="center">
		 <tr>
			     <th>Player Name</th>
			      <th>Date</th>
			     <th>Debit Card Deposit</th>
			     <th>Credit Card Deposit </th>
			     <th>Net Banking Deposit </th>			     
			     <th>Cash Card Deposit </th>
			    <th>Ola Bind Deposit </th>
			    <th>TechProcess Deposit </th>
			    <th>Ola Non Bind Deposit </th>
			    <s:if test="%{netGamingDistributionModel == 'depositratio'}">
										<th>Bonus Deposit </th>
			    <th>Inhouse Deposit </th>
										</s:if>
			    
			    <th>Wire Transfer Deposit </th>
			    <th>Total Deposit </th>
			    <th>Total Play </th>
			    <s:if test="%{netGamingDistributionModel == 'depositratio'}">
			    <th>Total Winning </th>
			    </s:if>
			   </tr>
			   <s:set name="debitCardTotal" value="0.0"/>
			   <s:set name="creditCardTotal" value="0.0"/>
			   <s:set name="netBankingTotal" value="0.0"/>
			   <s:set name="cashCardTotal" value="0.0"/>
			   <s:set name="olaBindTotal" value="0.0"/>
			   <s:set name="techProcessTotal" value="0.0"/>
			   <s:set name="olaNonBindTotal" value="0.0"/>
			   <s:if test="%{netGamingDistributionModel == 'depositratio'}">
			      <s:set name="bonusDepositTotal" value="0.0"/>
			      <s:set name="inhouseDepositTotal" value="0.0"/>
			   </s:if>
			   <s:set name="wireTransferTotal" value="0.0"/>
			   <s:set name="totalDepositTotal" value="0.0"/>
			   <s:set name="totalPlayTotal" value="0.0"/>
			   <s:if test="%{netGamingDistributionModel == 'depositratio'}">
			      <s:set name="totalWinTotal" value="0.0"/>
			   </s:if>			 
			  <s:iterator value="%{olaCommissionBeanList}">
			  <tr>
<td>
<s:property value="playerId"/>
</td>
<td ><s:property value="depositDate"/> </td>
<td  align="right"><s:property  value="debitCardDeposit"/><s:set name="debitCardDeposit" value="debitCardDeposit" /><s:set name="debitCardTotal" value="%{#debitCardTotal+#debitCardDeposit}"/></td>
<td  align="right"><s:property  value="creditCardDeposit"/><s:set name="creditCardDeposit" value="creditCardDeposit"/><s:set name="creditCardTotal" value="%{#creditCardTotal+#creditCardDeposit}"/></td>
<td  align="right"><s:property  value="netBankingDeposit"/><s:set name="netBankingDeposit" value="netBankingDeposit"/><s:set name="netBankingTotal" value="%{#netBankingTotal+#netBankingDeposit}"/></td>
<td  align="right">
<s:set name="cashCardDeposit" value="cashCardDeposit" />
<%=FormatNumber.formatNumberForJSP(pageContext
							.getAttribute("cashCardDeposit"))%>
<s:set name="cashCardTotal" value="%{#cashCardTotal+#cashCardDeposit}"/>
</td>
<td  align="right"><s:set name="olaBindDeposit" value="olaBindDeposit" />
<%=FormatNumber.formatNumberForJSP(pageContext
							.getAttribute("olaBindDeposit"))%>
<s:set name="olaBindTotal" value="%{#olaBindTotal+#olaBindDeposit}"/>							
</td>	
<td  align="right"><s:set name="techProcessDeposit" value="techProcessDeposit" />
<%=FormatNumber.formatNumberForJSP(pageContext
							.getAttribute("techProcessDeposit"))%>
<s:set name="techProcessTotal" value="%{#techProcessTotal+#techProcessDeposit}"/>
</td>
<td  align="right"><s:set name="olaNonBindDeposit" value="olaNonBindDeposit" />
<%=FormatNumber.formatNumberForJSP(pageContext
							.getAttribute("olaNonBindDeposit"))%>
<s:set name="olaNonBindTotal" value="%{#olaNonBindTotal+#olaNonBindDeposit}"/>
</td>

								<s:if test="%{netGamingDistributionModel == 'depositratio'}">
									<td  align="right">
										<s:set name="bonusDeposit" value="bonusDeposit" />
										<%=FormatNumber.formatNumberForJSP(pageContext
								.getAttribute("bonusDeposit"))%>
								<s:set name="bonusDepositTotal" value="%{#bonusDepositTotal+#bonusDeposit}"/>
								</td>
									<td  align="right">
										<s:property  value="inhouseDeposit" />
										<s:set name="inhouseDeposit" value="inhouseDeposit" />
										<s:set name="inhouseDepositTotal" value="%{#inhouseDepositTotal+#inhouseDeposit}"/>
									</td>

								</s:if>
								<td  align="right"><s:property value="wireTransferDeposit"/> 
									<s:set name="wireTransferDeposit" value="wireTransferDeposit" />
								<s:set name="wireTransferTotal" value="%{#wireTransferTotal+#wireTransferDeposit}"/>
								 </td>
<td  align="right"><s:set name="totalDeposit" value="totalDeposit" />
<%=FormatNumber.formatNumberForJSP(pageContext
							.getAttribute("totalDeposit"))%>
<s:set name="totalDepositTotal" value="%{#totalDepositTotal+#totalDeposit}"/>							
							
							</td>

<td  align="right"><s:set name="totalPlay" value="totalPlay" />
<%=FormatNumber.formatNumberForJSP(pageContext
							.getAttribute("totalPlay"))%>
							
<s:set name="totalPlayTotal" value="%{#totalPlayTotal+#totalPlay}"/>								
</td>
<s:if test="%{netGamingDistributionModel == 'depositratio'}">
<td  align="right"><s:set name="totalWin" value="totalWin" />
<%=FormatNumber.formatNumberForJSP(pageContext
							.getAttribute("totalWin"))%>
							
<s:set name="totalWinTotal" value="%{#totalWinTotal+#totalWin}"/>							
</td>
</s:if>
</tr>
</s:iterator>
<tr>
<th colspan="2">Total</th>
<th align="right"><%=FormatNumber.formatNumberForJSP(pageContext
							.getAttribute("debitCardTotal"))%></th>
<th align="right"><%=FormatNumber.formatNumberForJSP(pageContext
							.getAttribute("creditCardTotal"))%></th>
<th align="right"><%=FormatNumber.formatNumberForJSP(pageContext
							.getAttribute("netBankingTotal"))%></th>
<th align="right"><%=FormatNumber.formatNumberForJSP(pageContext
							.getAttribute("cashCardTotal"))%></th>
<th align="right"><%=FormatNumber.formatNumberForJSP(pageContext
							.getAttribute("olaBindTotal"))%></th>
<th align="right"><%=FormatNumber.formatNumberForJSP(pageContext
							.getAttribute("techProcessTotal"))%></th>
<th align="right"><%=FormatNumber.formatNumberForJSP(pageContext
							.getAttribute("olaNonBindTotal"))%></th>
<s:if test="%{netGamingDistributionModel == 'depositratio'}">
<th align="right"><%=FormatNumber.formatNumberForJSP(pageContext
							.getAttribute("bonusDepositTotal"))%></th>
<th align="right"><%=FormatNumber.formatNumberForJSP(pageContext
							.getAttribute("inhouseDepositTotal"))%></th>
</s:if>							
<th align="right"><%=FormatNumber.formatNumberForJSP(pageContext
							.getAttribute("wireTransferTotal"))%></th>
<th align="right"><%=FormatNumber.formatNumberForJSP(pageContext
							.getAttribute("totalDepositTotal"))%></th>
<th align="right"><%=FormatNumber.formatNumberForJSP(pageContext
							.getAttribute("totalPlayTotal"))%></th>
<s:if test="%{netGamingDistributionModel == 'depositratio'}">
<th align="right"><%=FormatNumber.formatNumberForJSP(pageContext
							.getAttribute("totalWinTotal"))%></th>
</s:if>						
											 
</tr>		 
				
			
			 </table> </div></div>



</s:else>
<div id="playerData"></div>
</body>
</html>