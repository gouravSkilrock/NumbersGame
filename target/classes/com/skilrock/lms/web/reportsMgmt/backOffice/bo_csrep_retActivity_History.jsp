<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="java.util.List"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.skilrock.lms.common.utility.FormatNumber"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<div id="reportData">
	<table width="100%" border="1" cellpadding="3" cellspacing="0"
		bordercolor="#CCCCCC" align="center">
		<tr>
			<th align="center">
				<s:text name="label.sNo" />.
			</th>
			<th width="10%" align="center">
				<s:text name="label.date" />
			</th>
			<th width="12%" align="center">
				<s:text name="label.live.ret" />
			</th>
			<th width="12%" align="center">
				<s:text name="label.no.sale.ret" />
			</th>
			<th width="12%" align="center">
				<s:text name="label.inact.ret" />
			</th>
			<th width="12%" align="center">
				<s:text name="label.term.ret" />
			</th>
			<th align="center">
				<s:text name="label.total.sales" />
			</th>
			<th align="center">
				<s:text name="label.avg.sale.per.ret" />
			</th>
		</tr>
		<s:if test="#session.retActHistMapForCS.size>0">
			<s:iterator value="#session.retActHistMapForCS">
				<s:set name="rowDataJsp" />
				<% String rowData = (String)pageContext.getAttribute("rowDataJsp");
			   String[] rowDataArr = rowData.split(",");
			   System.out.println(rowDataArr[3]);
			%>
				<tr>
					<% int i=0;
					for(String data: rowDataArr){
						i++;
						if(i == 7 || i==8 || i == 11){
							%>
					<td nowrap="nowrap" align="right"><%=FormatNumber.formatNumberForJSP(data) %></td>
					<%
						}else{
							if(i>2){
								%>
					<td nowrap="nowrap" align="right"><%=data %></td>
					<%
							}else{
								%>
					<td nowrap="nowrap" align="left"><%=data %></td>
					<%
							}
						}
					}
				 %>
				</tr>
			</s:iterator>
		</s:if>
		<s:else>
			<tr>
				<td colspan="11" align="center">
					<s:text name="msg.no.record.process" />
				</td>
			</tr>
		</s:else>
	</table>
</div>


<code id="headId" style="visibility: hidden">
	$RCSfile: bo_csrep_retActivity_History.jsp,v $ $Revision: 1.3 $
</code>