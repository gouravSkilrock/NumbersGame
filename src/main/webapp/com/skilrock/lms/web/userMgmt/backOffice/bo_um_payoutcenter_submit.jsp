<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<script type="text/javascript"
	src="<%=request.getContextPath()%>/com/skilrock/lms/web/userMgmt/backOffice/js/payOutCenter.js"></script>
<s:if test="%{orgPwtLimitBeanList != null && orgPwtLimitBeanList.size() > 0}">
	<table width="100%" border="1" cellpadding="2" cellspacing="0" bordercolor="#CCCCCC" align="center" valign="top" id="dataTable">
		<tr>
			<th>
				S.No
			</th>
			<th nowrap="nowrap">
				Agent Name
			</th>
			<th>
				Block Amount
			</th>
			<th>
				Change Block Amount
			</th>
			<th>
				Block Days
			</th>
			<th>
				Change Block Days
			</th>
			<th>
				Block Action
			</th>
			<th>
				Change Block Action
			</th>
			<th>
				<input type="checkbox" id="selectAll" />
				Select All
			</th>
			<th>
				Update
			</th>
			<th>
				Message
			</th>
		</tr>
		<s:iterator id="task" value="%{orgPwtLimitBeanList}" status="taskIndex">
			<tr id="row<s:property value="#taskIndex.index+1" />" class="data_row">
				<td>
					<s:property value="#taskIndex.index+1" />
					<s:hidden name="orgId" id="orgId" value="%{organizationId}" />
				</td>
				<td style="font-weight: bold;" nowrap="nowrap">
					<s:property value="orgCode" />
				</td>
				<td nowrap="nowrap">
					<s:property value="blockAmt" />
				</td>
				<td align="right">
					<s:textfield name="blockAmt" id="blockAmt" value="%{blockAmt}" maxlength="7" size="10" theme="simple"></s:textfield>
				</td>
				<td nowrap="nowrap">
					<s:property value="blockDays" />
				</td>
				<td align="right">
					<s:textfield name="blockDays" id="blockDays" value="%{blockDays}" maxlength="7" size="10" theme="simple"></s:textfield>
				</td>
				<td>
					<s:if test="%{blockAction eq 'NO_ACTION'}">
	                	NO ACTON
	                </s:if>
					<s:elseif test="%{blockAction eq 'LOGIN_BLOCK'}">
	                	LOGIN BLOCK
	                </s:elseif>
	                <s:elseif test="%{blockAction eq 'SALE_BLOCK'}">
	                	SALE BLOCK
	                </s:elseif>
				</td>
				<td>
					<select id="blockAction">
						<s:if test="%{blockAction == 'NO_ACTION'}">
							<option value="NO_ACTION" selected="selected">
								NO ACTION
							</option>
							<!--<option value="LOGIN_BLOCK">
								LOGIN BLOCK
							</option>-->
							<option value="SALE_BLOCK">
								SALE BLOCK
							</option>
						</s:if>
						<s:elseif test="%{blockAction == 'LOGIN_BLOCK'}">
							<option value="NO_ACTION">
								NO ACTION
							</option>
							<!--<option value="LOGIN_BLOCK" selected="selected">
								LOGIN BLOCK
							</option>-->
							<option value="SALE_BLOCK">
								SALE BLOCK
							</option>
						</s:elseif>
						<s:else>
							<option value="NO_ACTION">
								NO ACTION
							</option>
							<!--<option value="LOGIN_BLOCK">
								LOGIN BLOCK
							</option>-->
							<option value="SALE_BLOCK" selected="selected">
								SALE BLOCK
							</option>
						</s:else>
					</select>
				</td>
				<td align="center">
					<input type="checkbox" name="tempStatus" class="update_one" />
				</td>
				<td>
					<input id="updateButton" type="button" value="Update" class="updateButton" />
				</td>
				<td class="errorMsg">
					<s:property value="status" />
				</td>
			</tr>
		</s:iterator>
		<tr>
			<td colspan="5" align="center">
				<input type="button" name="submit" value="Submit" id="update_all" class="button" action="bo_um_agentPayOutCenter_submit" />
			</td>
		</tr>
	</table>
</s:if>
<s:else>
	<div style="text-align: center">
		No Records Found
	</div>
</s:else>
<code id="headId" style="visibility: hidden">
	$RCSfile: bo_um_payoutcenter_submit.jsp,v $ $Revision: 1.3 $
</code>