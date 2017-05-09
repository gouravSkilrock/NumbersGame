<!--
/*
* © copyright 2007, SkilRock Technologies, A division of Sugal & Damani Lottery Agency Pvt. Ltd.
* All Rights Reserved
* The contents of this file are the property of Sugal & Damani Lottery Agency Pvt. Ltd.
* and are subject to a License agreement with Sugal & Damani Lottery Agency Pvt. Ltd.; you may
* not use this file except in compliance with that License.  You may obtain a
* copy of that license from:
* Legal Department
* Sugal & Damani Lottery Agency Pvt. Ltd.
* 6/35,WEA, Karol Bagh,
* New Delhi
* India - 110005
* This software is distributed under the License and is provided on an “AS IS�?
* basis, without warranty of any kind, either express or implied, unless
* otherwise provided in the License.  See the License for governing rights and
* limitations under the License.
*/
-->

<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>


<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	<head>


		<title><%=application.getAttribute("JSP_PAGE_TITLE")%></title>

	</head>
	<s:head theme="ajax" />


	<body>

		<s:form>


			
				<table width="684" border="1" cellpadding="3" cellspacing="0" bordercolor="#CCCCCC" align="center">


					<tr>
						<th>
							Order Number
						</th>
						<th>
							<s:property value="#application.TIER_MAP.AGENT" /> Name
						</th>
						<th>
							Order Date
						</th>
						<th>
							Order Status
						</th>
						<th>
							Change Status
						</th>

					</tr>
					<s:if test="#session.APP_ORDER_LIST!=null">
					<s:iterator value="#session.APP_ORDER_LIST">

						<tr>
							<s:url id="es1" action="bo_om_closeOrder_Detail.action" encode="true">
								<s:param name="orderId" value="%{orderId}" />
								<s:param name="agentOrgId" value="%{agentOrgId}" />
								<s:param name="agtOrgName" value="%{agentOrgName}" />
								<s:param name="orderDate" value="%{orderDate}" />

							</s:url>
							<td>
								<s:property value="orderId" />
							</td>
							<td>
								<s:property value="agentOrgName" />
							</td>
							<td>
								<s:property value="orderDate" />
							</td>
							<td>
								<s:property value="orderStatus" />
							</td>
							<td>
								<s:a href="%{es1}">Change Order Status</s:a>
							</td>

						</tr>

					</s:iterator>
					</s:if>
			<s:else>
				<tr>
			   			<td colspan="5" align="center">No Order to Process</td>
			   	</tr> 
			</s:else>
				</table>
				<s:div id="naviga">
					<s:if test=" #session.APP_ORDER_LIST1.size >5 ">
						
					
					<s:if test="#session.startValueOrderSearch!=0"><s:a theme="ajax" targets="bottom" href="bo_om_closeOrder_Navigate.action?end=first">First</s:a><s:a theme="ajax" targets="bottom" href="bo_om_closeOrder_Navigate.action?end=Previous" > Previous</s:a></s:if><s:else>First Previous</s:else>    
    <s:if test="#session.startValueOrderSearch==((#session.APP_ORDER_LIST1.size/5)*5)">Next Last</s:if><s:else><s:a theme="ajax" targets="bottom" href="bo_om_closeOrder_Navigate.action?end=Next" >Next</s:a><s:a theme="ajax" targets="bottom" href="bo_om_closeOrder_Navigate.action?end=last">Last</s:a></s:else>      
    
					
					</s:if>
				</s:div>
			


		</s:form>

	</body>
</html>
<code id="headId" style="visibility: hidden">
$RCSfile: bo_om_closeOrder_SearchResult.jsp,v $
$Revision: 1.2 $
</code>