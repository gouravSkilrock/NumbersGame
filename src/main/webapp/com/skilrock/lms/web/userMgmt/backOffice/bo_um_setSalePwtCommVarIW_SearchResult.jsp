<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html>
<s:head theme="ajax" />
<body>
	<br>
	<s:if test="#session.salePwtCommVarSearchList.size>0">
		<table width="100%"; border="1"; cellpadding="3"; id="gameDetails"; cellspacing="0"; bordercolor="#CCCCCC"; align="center">
			<tr>
				<th align="center">&nbsp;</th>
				<th align="center" colspan="3"><s:text name="label.cur.sale.profit.var" /></th>
				<th align="center" colspan="3"><s:text name="label.cur" /> <s:text name="PWT" /> <s:text name="label.profit.var" /></th>
			</tr>
			<tr>
				<th align="center" nowrap="nowrap"><s:text name="label.game.name" /></th>
				<th align="center"><s:text name="label.def.profit.rate" /></th>
				<th align="center"><s:text name="label.profit.var" /></th>
				<th align="center"><s:text name="label.net.applicable.rate" /></th>
				<th align="center"><s:text name="label.def.profit.rate" /></th>
				<th align="center"><s:text name="label.profit.var" /></th>
				<th align="center"><s:text name="label.net.applicable.rate" /></th>
			</tr>

			<s:iterator id="beanCart" value="#session.salePwtCommVarSearchList"
				status="gameList">
				<tr id="<s:property value="#gameList.index"/>">
					<s:set name="trId" value="#gameList.index"></s:set>
					<td align="left"><s:a href="#" onclick="setDivValue(%{trId})"><s:property value="gameName" /></s:a></td>
					<td align="right"><s:property value="defSaleCommRate" /></td>
					<td align="right"><s:property value="currentSaleCommVar" /></td>
					<td align="right"><s:property value="netSaleCommRate" /></td>
					<td align="right"><s:property value="defPwtCommRate" /></td>
					<td align="right"><s:property value="currentPwtCommVar" /></td>
					<td align="right"><s:property value="netPwtCommRate" /></td>
				</tr>
			</s:iterator>
		</table>
		<div id="all" style="display: none;">
			<table style ='width="90%"; border="0"; cellpadding="3"; cellspacing="0"; bordercolor="#CCCCCC"; align="center"'>
				<tr>
					<td align="left" colspan="2"><s:checkbox theme="simple" name="allgames" id="allcheck" key="label.change.all.game" onclick="enableInputFields(this.id)" /><label class="label"><s:text name="label.change.all.game" /></label></td>
					<td align="right"><label class="label"><s:text name="label.sale.profit.variance" />:</label><s:textfield name="saleCommRateAll" key="label.sale.comm.var" theme="simple" id="allSaleCommRate" disabled="true" value="" /></td>
					<td align="right">&nbsp;</td>
					<td align="right" colspan="2"><label class="label"><s:text name="label.pwt.profit.var" />:</label><s:textfield name="pwtCommRateAll" key="label.pwt.comm.var" theme="simple" id="allPwtCommRate" disabled="true" value="" /></td>
					<td align="right"><input type="button" name='button' id="submitbutoon" value="<s:text name="label.update.all"/>" style = 'align="right";' disabled="disabled" style="button" onclick="submitUpdatedValueIW()" /></td>
				</tr>
			</table>
		</div>
	</s:if>
	<s:else>
		<s:text name="msg.no.record" />
	</s:else>
	<br />
	<br />
</body>
</html>
<code id="headId" style="visibility: hidden"> $RCSfile:
	bo_um_setSalePwtCommVarIW_SearchResult.jsp,v $ $Revision: 1.3 $
</code>