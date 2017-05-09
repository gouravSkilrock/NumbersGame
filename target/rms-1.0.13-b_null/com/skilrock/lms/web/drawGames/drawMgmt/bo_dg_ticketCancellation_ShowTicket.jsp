<%@ taglib prefix="s" uri="/struts-tags"%>

<div id="ErrMsgDivChecks" align="center"></div>
<table border="1" cellpadding="1" cellspacing="0" bordercolor="#CCCCCC"
	align="center">
	<tr>
		<td colspan="5">
			<center>
				<div id="errDiv"></div>
			</center>
		</td>
	</tr>
	<tr>
		<th colspan="2">
			<s:text name="label.tckt.no"/>
		</th>
		<th>
			<s:text name="label.mrp.amt"/>
		</th>
		<th>
			<s:text name="label.purchase.time"/>
		</th>
		<th>
			<s:text name="label.rsn.for.cancltn"/>
		</th>
	</tr>

	<s:if test="#session.ticketMap.size()>0">
		<s:iterator value="#session.ticketMap" status="stat">
			<tr>
				<td>
					<s:checkbox name="ticketNumArr" id="ticketNumArr" theme="simple"
						fieldValue="%{key}"></s:checkbox>
				</td>
				<td>
					<s:property value="%{key}" />
				</td>
				<s:iterator value="%{value}">
					<td>
						<s:property />
					</td>
				</s:iterator>
				<td>
					<s:select id="reason_%{key}" name="reason_%{key}"
						list="{'WRONG_NUMBER_PICKED','WRONG_AMOUNT_PLAYED','TICKET_NOT_PRINTED', 'OTHER'}"
						headerKey="-1" headerValue="%{getText('label.pls.select.reason')}" theme="simple"
						cssClass="option" />
				</td>
			</tr>
		</s:iterator>
		<tr>
			<td colspan="5" align="center">
				<table>
					<s:submit id="subButton" formId="ticketCancellation"
						cssClass="button" key="btn.submit" align="right"
						action="bo_dg_ticketCancellation_BlkSubmit"></s:submit>
				</table>
			</td>
		</tr>
	</s:if>
	<s:else>
		<tr>
			<th colspan="5" align="center">
				<s:text name="msg.no.record"/>
			</th>
		</tr>
	</s:else>
</table>

<code id="headId" style="visibility: hidden">
	$RCSfile: bo_dg_ticketCancellation_ShowTicket.jsp,v $ $Revision:
	1.1.2.4 $
</code>