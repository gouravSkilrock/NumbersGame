<%@ page import="com.skilrock.lms.beans.DateBeans" %>
<%@ page import="com.skilrock.lms.common.utility.GetDate" %>
<%@ page import="com.skilrock.lms.common.utility.FormatNumber" %>
<%@ page import="java.util.Iterator" %>
<%@ page import="java.util.HashMap" %>
<%@ page import="com.skilrock.lms.web.scratchService.customerSpecificReport.beans.TicketByTicketSalePwt" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<html>
		<head>
			<link rel="stylesheet"
					href="<%=request.getContextPath()%>/LMSImages/css/demo_table_jui.css"
					type="text/css">
				<link rel="stylesheet"
					href="<%=request.getContextPath()%>/LMSImages/css/datatable-common.css"
					type="text/css">
				<style type="text/css">
						#dTable_wrapper, #dTableNew_wrapper, #net_sale_dTable_wrapper {
   							 width: auto;
						}
				</style>
		</head>
		<body>

<div id="result">
	<div align="center">
		<h3>
			<u><s:text name="Scratch Sale Winning Collection Report"/></u>
		</h3>
	</div>

	<div id="tableDataDiv">
		<table border="1" cellpadding="3" cellspacing="0" bordercolor="#CCCCCC" align="center"  id=dTableNew class="common_data_table display display_table">
			  <s:if test="%{!orgWiseTicketByTicketSalePwt.isEmpty()}">
			   <thead>	   
					<tr>
						<th align="center" colspan="62"><s:text name="Safari Scratch "/></th>					
					</tr>
					<tr align="center">
						<th rowspan="3"><s:text name="label.party.name" /></th>
						<th colspan="59"><s:text name="label.scratch.game" /></th>
					</tr>
					<tr>
						<s:iterator value="gameMap">
							<s:iterator value="value">
								<td colspan="3" align="right"><s:property value="%{value}" />
								</td>
							</s:iterator>
						</s:iterator>
						<th rowspan="2" nowrap="nowrap"><s:text name="Total "/><s:text name="label.net.amt"/></th>
					</tr>
					<tr>
						<s:iterator value="gameMap">
							<s:iterator value="value">
								<th nowrap="nowrap"><s:text name="label.sale.amt" /></th>
								<th nowrap="nowrap"><s:text name="PWT"/> <s:text name="label.amount" /></th>
								<th nowrap="nowrap"><s:text name="Net"/> <s:text name="Amount"/></th>
							</s:iterator>
						</s:iterator>
					</tr>
					
				</thead>	
		          <tbody>	
					<s:iterator value="orgWiseTicketByTicketSalePwt">
					    <s:set name="totalNetAmount" value="0"></s:set>
					    <s:set name="gameTktMap" value="%{value}"></s:set>
						<tr>
							<td nowrap="nowrap" align="right"><s:property value="key" />
							</td>
							<s:iterator value="gameMap">
								<!-- iterating game map -->
								<s:iterator value="value">
									<s:set name="flag" value="0"></s:set>
									<s:set name="game_id_from_gamemap" value="%{key}"></s:set>
		
									
										<s:iterator value="gameTktMap">
											<s:set name="game_id" value="%{key}"></s:set>
											<s:if test="%{#game_id == #game_id_from_gamemap}">
												<s:set name="flag" value="%{#flag+1}" />
		                                        
												<s:set name="tktBean" value="%{value}" />
												<td nowrap="nowrap" align="right">
												    <s:property value="%{#tktBean.sale}" />
												</td>
												<td nowrap="nowrap" align="right">
												  <s:property value="%{#tktBean.winning}" />
												</td>
												<td nowrap="nowrap" align="right">
												  <s:property value="%{#tktBean.sale - #tktBean.winning}" />
												</td>
												
												<s:set name="totalNetAmount" value="%{#totalNetAmount + #tktBean.sale - #tktBean.winning}" />
		
											</s:if>
										</s:iterator>
					
		
									<s:if test="%{#flag == 0}">
										<td nowrap="nowrap" align="right">0.00</td>
										<td nowrap="nowrap" align="right">0.00</td>
										<td nowrap="nowrap" align="right">0.00</td>
									</s:if>
		
								</s:iterator>
							</s:iterator>
			                <td nowrap="nowrap" align="right"><s:property value="%{#totalNetAmount}" /></td>
						</tr>		     
					</s:iterator>
				</s:if>
				<s:else>
					<tr>
						<th align="center">
							<s:if test="%{message==null}">
					           <s:text name="msg.no.record" />
					        </s:if>
							<s:else>
								<s:property value="message" />
							</s:else>
						</th>
					</tr>
				</s:else>
			</tbody>
		</table>
	 </div>
</div>
</body>
</html>
	<code id="headId" style="visibility: hidden">
	   $RCSfile: bo_customer_specific_report.jsp,v $ $Revision: 1.3 $ 
	</code>

