
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<table border="1" cellpadding="2" cellspacing="0" bordercolor="#CCCCCC"
	align="center" width="100%">
	<s:set name="DG" value="#application.IS_DRAW" />
	<s:set name="SL" value="#application.IS_SLE" />
	<s:set name="IW" value="#application.IS_IW" />
	<s:set name="VS" value="#application.IS_VS" />
	<s:set name="SE" value="#application.IS_SCRATCH" />
	<s:set name="CS" value="#application.IS_CS" />
	<s:set name="OLA" value="#application.IS_OLA" />
	<s:set name="colCount" value="0" />
	<tr>
		<th align="center" rowspan="2">
			Date
		</th>
		<s:if test="%{#DG=='YES'}">
			<th colspan="5" align="center">
				Draw Game
			</th>
		</s:if>
        <s:if test="%{#SL=='YES'}">		
			<th colspan="5" align="center">
				Sports Lottery
			</th>
		</s:if>
		<s:if test="%{#IW=='YES'}">		
			<th colspan="5" align="center">
				Instant Win
			</th>
		</s:if>
		<s:if test="%{#VS=='YES'}">		
			<th colspan="5" align="center">
				Virtual Sports
			</th>
		</s:if>
		<s:if test="%{#SE=='YES'}">
			<th colspan="2" align="center">
				Scratch Game
			</th>
		</s:if>
		<s:if test="%{#OLA=='YES'}">
			<th colspan="2" align="center">
				Offline Affiliates
			</th>
		</s:if>
		<s:if test="%{#CS=='YES'}">
			<th colspan="1" align="center">
				Commercial Service
			</th>
		</s:if>
	</tr>
	<tr>
		<s:if test="%{#DG=='YES'}">
			<th align="center">
				Total Sale
			</th>
			<th align="center">
				Total Pwt
			</th>
			<th align="center">
				Total Ticket Count
			</th>
			<th align="center">
				Total Pwt Count
			</th>
			<th align="center">
				Average Sale Per Retailer
			</th>
		</s:if>
		<s:if test="%{#SL=='YES'}">
			<th align="center">
				Total Sale
			</th>
			<th align="center">
				Total Pwt
			</th>
			<th align="center">
				Total Ticket Count
			</th>
			<th align="center">
				Total Pwt Count
			</th>
			<th align="center">
				Average Sale Per Retailer
			</th>
		</s:if>
		<s:if test="%{#IW=='YES'}">
			<th align="center">
				Total Sale
			</th>
			<th align="center">
				Total Pwt
			</th>
			<th align="center">
				Total Ticket Count
			</th>
			<th align="center">
				Total Pwt Count
			</th>
			<th align="center">
				Average Sale Per Retailer
			</th>
		</s:if>
		<s:if test="%{#VS=='YES'}">
			<th align="center">
				Total Sale
			</th>
			<th align="center">
				Total Pwt
			</th>
			<th align="center">
				Total Ticket Count
			</th>
			<th align="center">
				Total Pwt Count
			</th>
			<th align="center">
				Average Sale Per Retailer
			</th>
		</s:if>
		<s:if test="%{#SE=='YES'}">
			<th align="center">
				Total Sale
			</th>
			<th align="center">
				Total Pwt
			</th>
		</s:if>
		<s:if test="%{#OLA=='YES'}">
			<th align="center">
				Total Deposit
			</th>
			<th align="center">
				Total withdrawal
			</th>
		</s:if>
		<s:if test="%{#CS=='YES'}">
			<th align="center">
				Total Sale
			</th>
		</s:if>
	</tr>
	<s:if test="transVolumeMap.size>0">
		<s:iterator value="transVolumeMap">

			<tr class="startSortable">
				<td>
					<s:property value="%{key}" />
				</td>
				<s:set name="retValue" value="%{value}"></s:set>
				<s:if test="%{#DG=='YES'}">
					<td align="right">
						<s:property value="%{#retValue.dgTotalSale}" />
					</td>
					<td align="right">
						<s:property value="%{#retValue.dgTotalPwt}" />
					</td>
					<td align="right">
						<s:property value="%{#retValue.dgTicketCount}" />
					</td>
					<td align="right">
						<s:property value="%{#retValue.dgPwtTotalCount}" />
					</td>
					<td align="right">
						<s:property value="%{#retValue.dgAvgSalePerRet}" />
					</td>
				</s:if>
				<s:if test="%{#SL=='YES'}">
					<td align="right">
						<s:property value="%{#retValue.slTotalSale}" />
					</td>
					<td align="right">
						<s:property value="%{#retValue.slTotalPwt}" />
					</td>
					<td align="right">
						<s:property value="%{#retValue.slTicketCount}" />
					</td>
					<td align="right">
						<s:property value="%{#retValue.slPwtTotalCount}" />
					</td>
					<td align="right">
						<s:property value="%{#retValue.slAvgSalePerRet}" />
					</td>
				</s:if>
				<s:if test="%{#IW=='YES'}">
					<td align="right">
						<s:property value="%{#retValue.iwTotalSale}" />
					</td>
					<td align="right">
						<s:property value="%{#retValue.iwTotalPwt}" />
					</td>
					<td align="right">
						<s:property value="%{#retValue.iwTicketCount}" />
					</td>
					<td align="right">
						<s:property value="%{#retValue.iwPwtTotalCount}" />
					</td>
					<td align="right">
						<s:property value="%{#retValue.iwAvgSalePerRet}" />
					</td>
				</s:if>
				<s:if test="%{#VS=='YES'}">
					<td align="right">
						<s:property value="%{#retValue.vsTotalSale}" />
					</td>
					<td align="right">
						<s:property value="%{#retValue.vsTotalPwt}" />
					</td>
					<td align="right">
						<s:property value="%{#retValue.vsTicketCount}" />
					</td>
					<td align="right">
						<s:property value="%{#retValue.vsPwtTotalCount}" />
					</td>
					<td align="right">
						<s:property value="%{#retValue.vsAvgSalePerRet}" />
					</td>
				</s:if>
				<s:if test="%{#SE=='YES'}">
					<td align="right"> 
						<s:property value="%{#retValue.seTotalSale}" />
					</td>
					<td align="right">
						<s:property value="%{#retValue.seTotalPwt}" />
					</td>
				</s:if>
				<s:if test="%{#OLA=='YES'}">
					<td align="right">
						<s:property value="%{#retValue.olaTotalDepo}" />
					</td>
					<td align="right">
						<s:property value="%{#retValue.olaTotalWdrl}" />
					</td>
				</s:if>
				<s:if test="%{#CS=='YES'}">
					<td align="right">
						<s:property value="%{#retValue.csTotalSale}" />
					</td>
				</s:if>
			</tr>

		</s:iterator>
	</s:if>
	<s:else>
		<tr>
			<td colspan="17" align="center">
				No Records to Process
			</td>
		</tr>
	</s:else>
</table>

<code id="headId" style="visibility: hidden">
	$RCSfile: bo_rep_retActivity_transVolume_History.jsp,v $ $Revision:
	1.1.2.6 $
</code>