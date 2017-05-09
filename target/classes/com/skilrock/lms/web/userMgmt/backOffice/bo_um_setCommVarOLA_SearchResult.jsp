<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html>
	<s:head theme="ajax"/>
	
<body><br>
  
	
  	<s:if test="#session.salePwtCommVarSearchList.size>0">	
	 <table width="100%" border="1" cellpadding="3" id="gameDetails" cellspacing="0" bordercolor="#CCCCCC" align="center">			
			<tr >	
				<th align="center" >&nbsp;</th>
				<th align="center" colspan="3">Current Withdrawal Profit Variance</th>
				<th align="center" colspan="3">Current Deposit Profit Variance</th>
				<th align="center" colspan="3">Current Net Gaming Profit Variance</th>					
			</tr>
			<tr >	
				<th align="center" nowrap="nowrap">Wallet Name</th>
				<th align="center">Def Profit Rate</th>
				<th align="center">Profit Var</th>
				<th align="center">Net Applicable Rate</th>
				<th align="center">Def Profit Rate</th>
				<th align="center">Profit Var</th>
				<th align="center">Net Applicable Rate</th>
				<th align="center">Def Profit Rate</th>
				<th align="center">Profit Var</th>
				<th align="center">Net Applicable Rate</th>
			</tr>		
		
			<s:iterator id="beanCart" value="#session.salePwtCommVarSearchList" status="gameList">
			<tr id="<s:property value="#gameList.index"/>">				
				<s:set name="trId" value="#gameList.index"></s:set>
				<td align="left"><s:a href="#" onclick="setDivValue(%{trId})" ><s:property value="%{gameName}" /></s:a></td>
				<td align="right"><s:property value="defWidCommRate" /></td>
				<td align="right"><s:property value="currentWidCommVar" /></td>
				<td align="right"><s:property value="netWidCommRate" /></td>
				<td align="right"><s:property value="defDepCommRate" /></td>
				<td align="right"><s:property value="currentDepCommVar" /></td>
				<td align="right"><s:property value="netDepCommRate" /></td>
				<td align="right"><s:property value="defNetGCommRate" /></td>
				<td align="right"><s:property value="currentNetGCommVar" /></td>
				<td align="right"><s:property value="netNetGCommRate" /></td>
			</tr>	
			</s:iterator> 
			
	 </table>
	 <div id="all" style="display: none;">
	 <table width="90%" border="0" cellpadding="3" cellspacing="0" bordercolor="#CCCCCC" align="center">
	 	<tr>
				<td align="left" colspan="2"><s:checkbox theme="simple" name="allgames" id="allcheck" label="Change All Games" onclick="enableInputFieldsOLA(this.id)" /><label class="label">Change All Games</label></td>
				<td align="right"><label class="label">Withdrawal Profit Variance:</label><s:textfield name="widCommRateAll" label="Withdrawal Comm Variance" theme="simple" id="allWidCommRate" disabled="true" value="" /></td>
				<td align="right">&nbsp;</td>
				<td align="right" colspan="2"><label class="label">Deposit Profit Variance:</label><s:textfield name="depCommRateAll" label="deposit Comm Variance" theme="simple" id="allDepCommRate" disabled="true" value=""  /></td>
				<td align="right"><input type="button" name='button' id="submitbutoon" value="Update All" align="right"  disabled="disabled" style="button" onclick="submitUpdatedAllValue()"/></td>
				<td align="right">&nbsp;</td>
				<td align="right" colspan="2"><label class="label">Net Gaming Profit Variance:</label><s:textfield name="netGCommRateAll" label="Net Gaming Comm Variance" theme="simple" id="allNetGCommRate" disabled="true" value=""  /></td>
				<td align="right"><input type="button" name='button' id="submitbutoon" value="Update All" align="right"  disabled="disabled" style="button" onclick="submitUpdatedAllValue()"/></td>
		</tr>
	 </table>
	 </div>
	</s:if>

	<s:else> 	No Record Found 	</s:else>
	<br/><br/>
	
		
	</body>
</html>
<code id="headId" style="visibility: hidden">
$RCSfile: bo_um_setCommVarOLA_SearchResult.jsp,v $
$Revision: 1.2 $
</code>