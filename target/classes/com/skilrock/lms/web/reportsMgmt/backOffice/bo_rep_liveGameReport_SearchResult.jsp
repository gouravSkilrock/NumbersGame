
<%@page import="org.eclipse.jdt.internal.compiler.ast.ForeachStatement"%><%@ taglib
	prefix="s" uri="/struts-tags"%>
<%
	System.out.println("***********Live Report*******");
%>
<table border="1" cellpadding="1" cellspacing="0" bordercolor="#CCCCCC"
	align="center">
	<s:set name="serName" value="serviceName"/>
	<s:if test="#session.reportMap!=null">
		<tr>
			<th>
				Organization Name
			</th>
			<th>
				Sale Amount
			</th>
			<s:if test="%{#serName=='DG'}">
			<th>
				Cancel Amount
			</th>
			</s:if>
			<th>
				PWT Amount
			</th>
			<th>
				Net Amount
			</th>
		</tr>
		<s:iterator id="reportMap" value="#session.reportMap">
			<s:set name="orgValue" value="%{value}"></s:set>
			<tr class="startSortable">
				<td>
					<s:property value="%{key}"/>
				</td>
				<%
					String orgValue = pageContext.getAttribute("orgValue")
									.toString();
							String orgValArr[] = orgValue.split(",");
							for (int i = 0; i < orgValArr.length; i++) {
				%>
				<td>
					<%=orgValArr[i]%>
				</td>
				<%
					}
				%>
			</tr>
		</s:iterator>
	</s:if>
	<s:else>
		<tr>
			<td>
				No Record
			</td>
		</tr>
	</s:else>
</table>

<code id="headId" style="visibility: hidden">
$RCSfile: bo_rep_liveGameReport_SearchResult.jsp,v $
$Revision: 1.2 $
</code>