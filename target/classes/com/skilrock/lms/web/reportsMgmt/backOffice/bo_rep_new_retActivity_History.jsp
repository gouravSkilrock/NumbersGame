
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
		<th align="center" rowspan="2" width="8%">
			Date
		</th>
		<s:if test="%{#DG=='YES'}">
			<th colspan="3" align="center">
				Draw Game
			</th>
		</s:if>
		<s:if test="%{#SL=='YES'}">
			<th colspan="3" align="center">
				Sports Lottery
			</th>
		</s:if>
		<s:if test="%{#IW=='YES'}">
			<th colspan="3" align="center">
				Instant Win
			</th>
		</s:if>
		<s:if test="%{#VS=='YES'}">
			<th colspan="3" align="center">
				Virtual Sports
			</th>
		</s:if>
		<s:if test="%{#SE=='YES'}">
			<th colspan="3" align="center">
				Scratch Game
			</th>
		</s:if>
		<s:if test="%{#OLA=='YES'}">
			<th colspan="3" align="center">
				Offline Affiliates
			</th>
		</s:if>
		<s:if test="%{#CS=='YES'}">
			<th colspan="2" align="center">
				Commercial Service
			</th>
		</s:if>
		<th align="center" rowspan="2">
			Login Retailer Count
		</th>
		<th align="center" rowspan="2">
			HeartBeat Retailer Count
		</th>
		<th align="center" rowspan="2">
			Inactive Retailer Count
		</th>
		<th align="center" rowspan="2">
			Terminate Retailer Count
		</th>
		<th align="center" rowspan="2">
			Total Retailer Count
		</th>
	</tr>
	<tr>
		<s:if test="%{#DG=='YES'}">
			<th align="center">
				Retailer Count
			</th>
			<th align="center">
				Sale Retailer Count
			</th>
			<th align="center">
				Pwt Retailer Count
			</th>
		</s:if>
		
		<s:if test="%{#SL=='YES'}">
			<th align="center">
				Retailer Count
			</th>
			<th align="center">
				Sale Retailer Count
			</th>
			<th align="center">
				Pwt Retailer Count
			</th>
		</s:if>
		<s:if test="%{#IW=='YES'}">
			<th align="center">
				Retailer Count
			</th>
			<th align="center">
				Sale Retailer Count
			</th>
			<th align="center">
				Pwt Retailer Count
			</th>
		</s:if>
		<s:if test="%{#VS=='YES'}">
			<th align="center">
				Retailer Count
			</th>
			<th align="center">
				Sale Retailer Count
			</th>
			<th align="center">
				Pwt Retailer Count
			</th>
		</s:if>
		<s:if test="%{#SE=='YES'}">
			<th align="center">
				Retailer Count
			</th>
			<th align="center">
				Sale Retailer Count
			</th>
			<th align="center">
				Pwt Retailer Count
			</th>
		</s:if>
		<s:if test="%{#OLA=='YES'}">
			<th align="center">
				Retailer Count
			</th>
			<th align="center">
				Deposit Retailer Count
			</th>
			<th align="center">
				Withdrawal Retailer Count
			</th>
		</s:if>
		<s:if test="%{#CS=='YES'}">
			<th align="center">
				Retailer Count
			</th>
			<th align="center">
				Sale Retailer Count
			</th>
		</s:if>
	</tr>
	<s:if test="retCountMap.size>0">
		<s:iterator value="retCountMap">

			<tr class="startSortable">
				<td>
					<s:property value="%{key}" />
				</td>
				<s:set name="retValue" value="%{value}"></s:set>
				<s:if test="%{#DG=='YES'}">
					<td>
						<s:property value="%{#retValue.dgCount}" />
					</td>
					<td>
						<s:property value="%{#retValue.dgSaleCount}" />
					</td>
					<td>
						<s:property value="%{#retValue.dgPwtCount}" />
					</td>
				</s:if>
	                			
				<s:if test="%{#SL=='YES'}">
					<td>
						<s:property value="%{#retValue.slCount}" />
					</td>
					<td>
						<s:property value="%{#retValue.slSaleCount}" />
					</td>
					<td>
						<s:property value="%{#retValue.slPwtCount}" />
					</td>
				</s:if>
				<s:if test="%{#IW=='YES'}">
					<td>
						<s:property value="%{#retValue.iwCount}" />
					</td>
					<td>
						<s:property value="%{#retValue.iwSaleCount}" />
					</td>
					<td>
						<s:property value="%{#retValue.iwPwtCount}" />
					</td>
				</s:if>
				<s:if test="%{#VS=='YES'}">
					<td>
						<s:property value="%{#retValue.vsCount}" />
					</td>
					<td>
						<s:property value="%{#retValue.vsSaleCount}" />
					</td>
					<td>
						<s:property value="%{#retValue.vsPwtCount}" />
					</td>
				</s:if>
				<s:if test="%{#SE=='YES'}">
					<td>
						<s:property value="%{#retValue.seCount}" />
					</td>
					<td>
						<s:property value="%{#retValue.seSaleCount}" />
					</td>
					<td>
						<s:property value="%{#retValue.sePwtCount}" />
					</td>
				</s:if>
				<s:if test="%{#OLA=='YES'}">
					<td>
						<s:property value="%{#retValue.olaCount}" />
					</td>
					<td>
						<s:property value="%{#retValue.olaDepoCount}" />
					</td>
					<td>
						<s:property value="%{#retValue.olaWdlCount}" />
					</td>
				</s:if>
				<s:if test="%{#CS=='YES'}">
					<td>
						<s:property value="%{#retValue.csCount}" />
					</td>
					<td>
						<s:property value="%{#retValue.csSaleCount}" />
					</td>
				</s:if>
				<td>
					<s:property value="%{#retValue.loginCount}" />
				</td>
				<td>
					<s:property value="%{#retValue.heartBeatCount}" />
				</td>
				<td>
					<s:property value="%{#retValue.inactiveRetCount}" />
				</td>
				<td>
					<s:property value="%{#retValue.terRetCount}" />
				</td>
				<td>
					<s:property value="%{#retValue.totalCount}" />
				</td>
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
	$RCSfile: bo_rep_new_retActivity_History.jsp,v $ $Revision: 1.3 $
</code>