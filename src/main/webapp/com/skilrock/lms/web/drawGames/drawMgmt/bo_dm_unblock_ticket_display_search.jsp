<%@ taglib prefix="s" uri="/struts-tags"%>

<s:form action="bo_dm_unblock_specific_ticket_result" onsubmit=" return validateUnblockTicket();">
	<div id="ErrMsgDivChecks" align="center"></div>
	<table  width="100%" cellpadding="5" cellspacing="5" bordercolor="#CCCCCC"
		align="center" style="border-left: 1;">
		<s:if
			test="%{blockTicketUserBeanList != null && blockTicketUserBeanList.size()>0}">
		<tr>
			<th>
				<input type="checkbox" name="checkAll" id="checkAll"
					onchange="toggleAllChecks();" />
			</th>
			<th>
				<s:text name="label.tckt.no"/>
			</th>
			<th>
				<s:text name="label.cur.stat"/>
			</th>
			<th>
				<s:text name="label.status"/>
			</th>
			<th>
				Date&Time
			</th>
			<th>
				Done By
			</th>
		</tr>
		
			<s:iterator id="beanCart" value="%{blockTicketUserBeanList}"
				status="taskIndex">
				<tr>
					<td>
						<input type="checkbox" name="ticketNumArr" id="ticketNumArr"
							value='<s:property value="ticketNumber" />'
							onchange="_id.o('checkAll').checked = false" />
					</td>
					<td>
						<s:property value="ticketNumber" />
					</td>
					<td>
						<s:property value="status" />
					</td>
					<td>
						<s:select id="%{#taskIndex.index}reasons" name="reasons"
							headerKey="-1" headerValue="%{getText('label.pls.select.reason')}" theme="simple" list="{'TEMPORARY_BLOCK','BLOCKED','UNBLOCKED'}" 
							cssClass="option" />
					</td>
					<td>
					     
					      <s:date name="%{updatedTime}" format="yyyy-MM-dd HH:mm:ss"/> 
					    
					</td>
					<td>
					    <s:property value="userName"/>
					</td>
				</tr>

			</s:iterator>
			<tr>
				<td colspan="3" align="center">
					<s:submit name="search" key="btn.take.action" id="search"
						align="center" targets="down1" theme="ajax" cssClass="button" />
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

</s:form>
<code id="headId" style="visibility: hidden">
	$RCSfile: bo_dm_unblock_ticket_display_search.jsp,v $ $Revision:
	1.1.2.4 $
</code>