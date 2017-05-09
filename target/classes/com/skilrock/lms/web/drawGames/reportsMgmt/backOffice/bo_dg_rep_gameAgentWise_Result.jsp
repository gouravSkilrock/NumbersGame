<%@page import="com.skilrock.lms.beans.DateBeans"%>
<%@page import="com.skilrock.lms.common.utility.GetDate"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.List" %>
<%@page import="java.util.ArrayList"%>
<%@page import="com.skilrock.lms.common.utility.FormatNumber"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<s:set name="IW" value="%{#session.isIW}"></s:set>
<html>
	<%
		response.setHeader("Cache-Control", "no-store"); //HTTP 1.1
		response.setHeader("Pragma", "no-cache"); //HTTP 1.0
		response.setDateHeader("Expires", 0); //prevents caching at the proxy server
	%>
	<s:head theme="ajax" />
	<body onload="disable()">

		<br />
		<div align="center">
			<h3>
				<u><s:text name="label.draw.game.sale.and"/> <s:text name="PWT"/> <s:text name="Report"/> &nbsp; <%
 	if ("".equals(((DateBeans) session.getAttribute("datebean"))
 			.getReportType())) {
 %> From : <%=GetDate.getConvertedDate(((DateBeans) session
								.getAttribute("datebean")).getStartDate())%>
					&nbsp;&nbsp;To&nbsp;:&nbsp; <%=GetDate.getConvertedDate(((DateBeans) session
								.getAttribute("datebean")).getEndDate())%> <%
 	} else {
 		out.println("Of : "
 				+ ((DateBeans) session.getAttribute("datebean"))
 						.getReportType());
 	}
 %> </u>
			</h3>
		</div>

<div id="excel"  text-align: left;width: 750px;font-size: 9pt"><s:a href="bo_rep_DGFullReport_ExpExcel.action?orgType=BO"><s:text name="btn.exportasexcel"/></s:a></div>
		<table width="100%" border="1" cellpadding="3" cellspacing="0"
			bordercolor="#CCCCCC" align="center">
			<%int gameCnt =  (Boolean)session.getAttribute("isIW") == false ? ((Map<Integer, String>)session.getAttribute("ActiveGameNumMap")).size() : ((Map<Integer, String>)session.getAttribute("ActiveGameNumMap")).size() + ((Map<Integer, String>)session.getAttribute("IWGameMap")).size();
				double[] total = new double[gameCnt*3];
				int c = 0;
				while(c< gameCnt*3){
					total[c] = 0.0;
					c++;
				}
	   		%>
			<tr>
				<th align="center" colspan="<%=(total.length +1) %>" ><s:property value="#session.orgName" /></th>
				<th align="center"><s:text name="label.amt.in"/> <s:property value="#application.CURRENCY_SYMBOL" /></th>
			</tr>
			<tr>
				<td align="center" colspan="<%=(total.length +2) %>"><s:property value="#session.orgAdd" /></td>
			</tr>
			<tr>
				<th rowspan="3" width="11%" nowrap="nowrap">
					 <s:property value="#application.TIER_MAP.AGENT" /> <s:text name="label.name"/>
				</th>
				<th rowspan="3" width="11%" nowrap="nowrap">
					<s:text name="label.location"/>
				</th>
				<s:if test="%{#IW}">
				<th colspan="<s:property value="%{#session.ActiveGameNumMap.size() + #session.IWGameMap.size()}" />">
					<s:text name="label.sales"/>
				</th>
				</s:if><s:else>
				<th colspan="<s:property value="%{#session.ActiveGameNumMap.size()}" />">
					<s:text name="label.sales"/>
				</th>
				</s:else>
				<s:if test="%{#IW}">
				<th colspan="<s:property value="%{#session.ActiveGameNumMap.size() + #session.IWGameMap.size()}" />">
					<s:text name="label.prize.payout"/>
				</th>
				</s:if><s:else>
				<th colspan="<s:property value="%{#session.ActiveGameNumMap.size()}" />">
					<s:text name="label.prize.payout"/>
				</th>
				</s:else>
				<s:if test="%{#IW}">
				<th colspan="<s:property value="%{#session.ActiveGameNumMap.size() + #session.IWGameMap.size()}" />">
					<s:text name="label.net.portion"/>
				</th>
				</s:if><s:else>
				<th colspan="<s:property value="%{#session.ActiveGameNumMap.size()}" />">
					<s:text name="label.net.portion"/>
				</th>
				</s:else>
			</tr>
			<tr>
				<th colspan="<s:property value="#session.ActiveGameNumMap.size()"/>">
					DG
				</th>
				<s:if test="%{#IW}">
				<th colspan="<s:property value="#session.IWGameMap.size()"/>">
					IW
				</th>
				</s:if>
				<th colspan="<s:property value="#session.ActiveGameNumMap.size()"/>">
					DG
				</th>
				<s:if test="%{#IW}">
				<th colspan="<s:property value="#session.IWGameMap.size()"/>">
					IW
				</th>
				</s:if>
				<th colspan="<s:property value="#session.ActiveGameNumMap.size()"/>">
					DG
				</th>
				<s:if test="%{#IW}">
				<th colspan="<s:property value="#session.IWGameMap.size()"/>">
					IW
				</th>
				</s:if>
			</tr>
			<tr >
					<s:iterator id="beanCart" value="#session.ActiveGameNumMap">
						<th width="10%">
							<s:property value="value" /> 
						</th>
					</s:iterator>
					<s:if test="%{#IW}">
					<s:iterator id="beanCart" value="#session.IWGameMap">
						<th width="10%">
							<s:property value="value" /> 
						</th>
					</s:iterator>
					</s:if>
					<s:iterator id="beanCart" value="#session.ActiveGameNumMap">
						<th width="10%">
							<s:property value="value" /> 
						</th>
					</s:iterator>
					<s:if test="%{#IW}">
					<s:iterator id="beanCart" value="#session.IWGameMap">
						<th width="10%">
							<s:property value="value" /> 
						</th>
					</s:iterator>
					</s:if>
					<s:iterator id="beanCart" value="#session.ActiveGameNumMap">
						<th width="10%">
							<s:property value="value" /> 
						</th>
					</s:iterator>
					<s:if test="%{#IW}">
					<s:iterator id="beanCart" value="#session.IWGameMap">
						<th width="10%">
							<s:property value="value" /> 
						</th>
					</s:iterator>
					</s:if>
		
			
			
			<s:if test="#session.searchResultRet.size>0">
				<s:iterator id="beanCart" value="#session.searchResultRet">
					<s:set name="data" value="value"></s:set>
					<%
						ArrayList l = (ArrayList) pageContext.getAttribute("data");
								//System.out.println("*******list******" + l);
					%>
					<tr class="startSortable">
						<td width="12%" align="left" nowrap="nowrap">
							<s:property value="key" />
						</td>
						<s:if test="%{#IW}">
						<td width="10%">
							<%=l.get(6)   %>
						</td>
						</s:if>

						<%
							HashMap m = (HashMap) l.get(0);
							HashMap m4 = null ;
							HashMap m5 = null ;
							HashMap m6 = null ;
							int i = 0;			
						%>
						<s:iterator id="beanCart" value="#session.ActiveGameNumMap">
							<td width="10%" align="right">
								<s:set name="gameNbr" value="key"/>
								<%int gNbr = (Integer)pageContext.getAttribute("gameNbr"); %>
								<%=FormatNumber.formatNumber(null==m.get(gNbr)?"0":m.get(gNbr))%>
								<%total[i++] += Double.parseDouble(null==m.get(gNbr)?"0":m.get(gNbr).toString()); %>
							</td>
						</s:iterator>
						<s:if test="%{#IW}">
						<s:iterator id="beanCart" value="#session.IWGameMap">
							<td width="10%" align="right">
								<s:set name="gameNbr" value="key"/>
								<%int iwgNbr = (Integer)pageContext.getAttribute("gameNbr"); %>
								<%=FormatNumber.formatNumber(null==((HashMap) l.get(3)).get(iwgNbr)?"0":((HashMap) l.get(3)).get(iwgNbr))%>
								<%total[i++] += Double.parseDouble(null==((HashMap) l.get(3)).get(iwgNbr)?"0":((HashMap) l.get(3)).get(iwgNbr).toString()); %>
							</td>
						</s:iterator>
						</s:if>
						
						
						<%
							m = (HashMap) l.get(1);
							HashMap dp = (HashMap) l.get(2);
										
						%>
						<s:iterator id="beanCart" value="#session.ActiveGameNumMap">
							<td width="10%" align="right">
								<s:set name="gameNbr" value="key"/>
								<%int gNbr = (Integer)pageContext.getAttribute("gameNbr"); %>
								<%=FormatNumber.formatNumber(Double.parseDouble((m.get(gNbr)==null?0:m.get(gNbr)).toString())+Double.parseDouble((dp.get(gNbr)==null?0:dp.get(gNbr)).toString()))%>
								<%total[i++] += Double.parseDouble((m.get(gNbr)==null?0:m.get(gNbr)).toString())+Double.parseDouble((dp.get(gNbr)==null?0:dp.get(gNbr)).toString()); %>
							</td>
						</s:iterator>
						
						<%
						if((Boolean)session.getAttribute("isIW"))
							m = (HashMap) l.get(4);
							HashMap dp1 = (HashMap) l.get(5);
										
						%>
						<s:if test="%{#IW}">
						<s:iterator id="beanCart" value="#session.IWGameMap">
							<td width="10%" align="right">
								<s:set name="gameNbr" value="key"/>
								<%int iwgNbr = (Integer)pageContext.getAttribute("gameNbr"); %>
								<%=FormatNumber.formatNumber(Double.parseDouble((m.get(iwgNbr)==null?0:m.get(iwgNbr)).toString())+Double.parseDouble((dp1.get(iwgNbr)==null?0:dp1.get(iwgNbr)).toString()))%>
								<%total[i++] += Double.parseDouble((m.get(iwgNbr)==null?0:m.get(iwgNbr)).toString())+Double.parseDouble((dp1.get(iwgNbr)==null?0:dp1.get(iwgNbr)).toString()); %>
							</td>
						</s:iterator>
						</s:if>
						
						<%
						HashMap m1 = (HashMap) l.get(0);
						HashMap m2 = (HashMap) l.get(1);
						HashMap m3 = (HashMap) l.get(2);
						if((Boolean)session.getAttribute("isIW")){
						m4 = (HashMap) l.get(3);
						m5 = (HashMap) l.get(4);
						m6 = (HashMap) l.get(5);
						}
						%>
						<s:iterator id="beanCart" value="#session.ActiveGameNumMap">
							<td width="10%" align="right">
								<s:set name="gameNbr" value="key"/>
								<%int gNbr = (Integer)pageContext.getAttribute("gameNbr"); %>
								<%=FormatNumber.formatNumber(Double.parseDouble((m1.get(gNbr)==null?0:m1.get(gNbr)).toString())-Double.parseDouble((m2.get(gNbr)==null?0:m2.get(gNbr)).toString())-Double.parseDouble((m3.get(gNbr)==null?0:m3.get(gNbr)).toString()))%>
								<%total[i++] += Double.parseDouble((m1.get(gNbr)==null?0:m1.get(gNbr)).toString())-Double.parseDouble((m2.get(gNbr)==null?0:m2.get(gNbr)).toString())-Double.parseDouble((m3.get(gNbr)==null?0:m3.get(gNbr)).toString()); %>
							</td>
						</s:iterator>
						<s:if test="%{#IW}">
						<s:iterator id="beanCart" value="#session.IWGameMap">
							<td width="10%" align="right">
								<s:set name="gameNbr" value="key"/>
								<%int iwgNbr = (Integer)pageContext.getAttribute("gameNbr"); %>
								<%=FormatNumber.formatNumber(Double.parseDouble((m4.get(iwgNbr)==null?0:m4.get(iwgNbr)).toString())-Double.parseDouble((m5.get(iwgNbr)==null?0:m5.get(iwgNbr)).toString())-Double.parseDouble((m6.get(iwgNbr)==null?0:m6.get(iwgNbr)).toString()))%>
								<%total[i++] += Double.parseDouble((m4.get(iwgNbr)==null?0:m4.get(iwgNbr)).toString())-Double.parseDouble((m5.get(iwgNbr)==null?0:m5.get(iwgNbr)).toString())-Double.parseDouble((m6.get(iwgNbr)==null?0:m6.get(iwgNbr)).toString()); %>
							</td>
						</s:iterator>
						</s:if>
					</tr>
				</s:iterator>
				<td>&nbsp;</td>
				<th width=""><s:text name="label.total" /></th>
				<% for(int i=0; i<total.length ;i++){
				%>
					<th width="10%" align="right">
						<%=FormatNumber.formatNumber(total[i]) %>
					</th>
				<%
					}
				 %>
			</s:if>
			<s:else>
				<tr>
					<td colspan="9" align="center">
						<s:text name="msg.no.record.process" />
					</td>
				</tr>
			</s:else>

		</table>
	</body>
</html>

<code id="headId" style="visibility: hidden">
$RCSfile: bo_dg_rep_gameAgentWise_Result.jsp,v $
$Revision: 1.3 $
</code>