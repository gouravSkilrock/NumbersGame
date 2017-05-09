<%@page import="com.skilrock.lms.common.utility.FormatNumber"%>
<%@ taglib prefix="s" uri="/struts-tags"%>


<center><s:a href="bo_rep_ConsLiveGame_ExpAsExcel.action?orgType=BO">Export As Excel</s:a></center>
<table width="100%" border="1" cellpadding="3" cellspacing="0"
	bordercolor="#CCCCCC" align="center">
	<tr>
		<th rowspan="1" align="center">
			Retailer Name
		</th>
		<s:iterator value="%{#session.gameList}">
			<th colspan="2" align="center">
				<s:property />
			</th>
		</s:iterator>
		<th rowspan="2">
			Total Sale Amount
		</th>
		<th rowspan="2">
			Total Winning Amount
		</th>
		<th rowspan="2">
			Net Sale Amount
		</th>
	</tr>
	<tr>
		<td colspan="1" align="center">
		</td>
		<s:iterator value="%{#session.gameList}">

			<th align="center">
				Sale Amount
			</th>
			<th align="center">
				Pwt amount
			</th>
		</s:iterator>
	</tr>
	<s:iterator value="%{#session.resultService}">
		<tr class="startSortable">
			<s:iterator>
				<td>
					<s:property />
				</td>
			</s:iterator>
		</tr>
	</s:iterator>
</table>
<code id="headId" style="visibility: hidden">
	$RCSfile: bo_rep_consLiveGameReport_SearchExpand.jsp,v $ $Revision:
	1.1.2.16 $
</code>