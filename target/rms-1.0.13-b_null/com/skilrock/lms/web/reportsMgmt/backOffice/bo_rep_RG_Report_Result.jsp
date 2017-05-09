<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="java.util.Map"%>
<%@ taglib prefix="s" uri="/struts-tags"%>


<div align="center"><h3><u><s:text name="RG"/> <s:text name="Report"/>
<s:if test="%{filter == 'All'}">
	From <s:property value="%{start_date}"/> to <s:property value="%{end_Date}" />
</s:if> 
<s:else>
	On <s:property value="filter"/> <s:text name="RG"/> Basis
</s:else>
</u></h3></div>
<div id="reportData" style="height:auto;overflow-x:auto;overflow-y:visible">
	<table cellspacing="0" cellpadding="5" width="100%" border="1">
		<tr>
			<th>S.No.</th>
			<th><s:property value="#application.TIER_MAP.RETAILER" /> Name</th>
			<s:if test="%{organization_Name == -1}">
				<th><s:property value="#application.TIER_MAP.AGENT" /> Name</th>
			</s:if>
			<th>Location</th>
			<s:set name="critList" value="#session.criteriaMap"/>
			<s:iterator value="#session.criteriaMap">
				<th headers="<s:property value="key"/>"><s:property value="key.toUpperCase()"/> </th>
			</s:iterator>
		</tr>
		<s:if test="#session.rgRawRepMap.size>0">
			<s:iterator value="#session.rgRawRepMap" status="indx">
			<tr>
				<td><s:property value="#indx.count"/></td>
				<td><s:property value="retName"/></td>
				<s:if test="%{organization_Name == -1}">
					<td><s:property value="retParentName"/></td>
				</s:if>
				<td><s:property value="location"/></td>
				<s:iterator value="critMap">
					<s:set name="critName" value="key"/>
					<s:set name="critValue" value="value"/>
					<% Map<String, Double> map = (Map<String, Double>) pageContext.getAttribute("critList");
						String critName = (String) pageContext.getAttribute("critName");
						Double critValue = (Double) pageContext.getAttribute("critValue");
							if(map.get(critName).compareTo(critValue) >= 0){
								%>
									<td headers="<s:property value="critName"/>"><s:property value="value"/></td>
								<%
							}
							else{
								if(!((String)request.getAttribute("filter")).equalsIgnoreCase("all")){
									%>
										<td headers="<s:property value="critName"/>" style="background-color:#FF0000;color:#000000;"><s:property value="value"/></td>
									<%
								}
								else{
									%>
										<td headers="<s:property value="critName"/>"><b><s:property value="value"/></b></td>
									<%
								}
							}
					%>
				</s:iterator>
			</tr>
			</s:iterator>
		</s:if>
		<s:else>
			<td>No Records Found</td>
		</s:else>
	</table>
</div>

<code id="headId" style="visibility: hidden">
$RCSfile: bo_rep_RG_Report_Result.jsp,v $
$Revision: 1.3 $
</code>