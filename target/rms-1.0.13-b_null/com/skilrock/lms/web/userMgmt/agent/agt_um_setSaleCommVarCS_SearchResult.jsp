<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html>
	<s:head theme="ajax"/>
	
<body><br>
  
	
  	<s:if test="#session.salePwtCommVarSearchList.size>0">	
	 <table width="100%" border="1" cellpadding="3" id="gameDetails" cellspacing="0" bordercolor="#CCCCCC" align="center">			
			<tr >	
				<th align="center" >&nbsp;</th>
				<th align="center" colspan="3">Current Sale Profit Variance</th>				
			</tr>
			<tr >	
				<th align="center" nowrap="nowrap">Product Name</th>
				<th align="center">Def Profit Rate</th>
				<th align="center">Profit Var</th>
				<th align="center">Net Applicable Rate</th>
			</tr>		
		
			<s:iterator id="beanCart" value="#session.salePwtCommVarSearchList" status="gameList">
			<tr id="<s:property value="#gameList.index"/>">				
				<s:set name="trId" value="#gameList.index"></s:set>
				<td align="left"><s:a href="#" onclick="setDivValue(%{trId})" ><s:property value="%{gameName}" /></s:a></td>
				<td align="right"><s:property value="defSaleCommRate" /></td>
				<td align="right"><s:property value="currentSaleCommVar" /></td>
				<td align="right"><s:property value="netSaleCommRate" /></td>
			</tr>	
			</s:iterator> 
			
	 </table>
	 <div id="all" style="display: none;">
	 <table width="90%" border="0" cellpadding="3" cellspacing="0" bordercolor="#CCCCCC" align="center">
	 	<tr>
				<td align="left" colspan="2"><s:checkbox theme="simple" name="allgames" id="allcheck" label="Change All Games" onclick="enableInputFields(this.id)" /><label class="label">Change All Games</label></td>
				<td align="right"><label class="label">Sale Profit Variance:</label><s:textfield name="saleCommRateAll" label="Sale Comm Variance" theme="simple" id="allSaleCommRate" disabled="true" value="" /></td>
				<td align="right">&nbsp;</td>
				<td align="right" colspan="2"><label class="label">Winning Profit Variance:</label><s:textfield name="pwtCommRateAll" label="Winning Comm Variance" theme="simple" id="allPwtCommRate" disabled="true" value=""  /></td>
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
$RCSfile: agt_um_setSaleCommVarCS_SearchResult.jsp,v $
$Revision: 1.2 $
</code>