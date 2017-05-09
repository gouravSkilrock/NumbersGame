<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="java.util.Date"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
	<%
		response.setHeader("Cache-Control", "no-store"); //HTTP 1.1
		response.setHeader("Pragma", "no-cache"); //HTTP 1.0
		response.setDateHeader("Expires", 0); //prevents caching at the proxy server
	%>
<html>
	<head>
		<title><%=application.getAttribute("JSP_PAGE_TITLE")%></title>
		<s:head theme="ajax" debug="false" />
	</head>
	<body>

		<div id="bottom_table">
			<table width="100%" border="1" cellpadding="3" cellspacing="0"
				bordercolor="#CCCCCC" align="center">

				<tr>
					<td colspan="9" align="center">
						<b>Search Results</b>
					</td>
				</tr>

				<s:if test=" #session.USER_SEARCH_RESULTS.size !=0 ">
					<tr>

						<th align="center">
							User Id
						</th>
						<th align="center">
							User Name
						</th>
						<th align="center">
							With Organization
						</th>
						<th align="center">
							parent Organization
						</th>
						<th align="center">
							Offline Status
						</th>
						<th align="center">
							Login Status
						</th>
						<th align="center">
							IsOffline
						</th>


					</tr>
					<%session.setAttribute("rand",new Date().getTime()); %>
					
					<s:iterator id="SearchUserResults1"
						value="#session.USER_SEARCH_RESULTS">
						<tr>
							<td>
								<s:a id="link2" targets="top"
									href="bo_um_searchOfflineRetUser_Detail.action?userId=%{userId}&rand=%{#session.rand}">
									<s:property id="gn" value="userId" />
								</s:a>
							</td>
							<td>
								<s:property value="userName" />
							</td>
							<td>
								<s:property value="userOrgName" />
							</td>
							<td>
								<s:property value="parentOrgName" />
							</td>
							<td>
								<s:property value="offlineStatus" />
							</td>
							<td>
								<s:property value="loginStatus" />
							</td>
							<td>
								<s:property value="isOffline" />
							</td>


						</tr>
					</s:iterator>
				</s:if>
				<s:else>
					<tr>
						<td colspan="9" align="center">
							<s:text name="result">No Users to Process </s:text>
						</td>
					</tr>
				</s:else>
			</table>
			<s:div id="naviga" cssStyle="text-align: center">
				<s:if test=" #session.USER_SEARCH_RESULTS1.size >5 ">

					<s:if test="#session.startValueUserSearch!=0">
						<s:a theme="ajax" targets="bottom"
							href="bo_um_searchOfflineRetUser_Navigate.action?end=first">First</s:a>
						<s:a theme="ajax" targets="bottom"
							href="bo_um_searchOfflineRetUser_Navigate.action?end=Previous"> Previous</s:a>
					</s:if>
					<s:else>First Previous</s:else>
					<s:if
						test="#session.startValueUserSearch==((#session.USER_SEARCH_RESULTS1.size/5)*5)">Next Last</s:if>
					<s:else>
						<s:a theme="ajax" targets="bottom"
							href="bo_um_searchOfflineRetUser_Navigate.action?end=Next">Next</s:a>
						<s:a theme="ajax" targets="bottom"
							href="bo_um_searchOfflineRetUser_Navigate.action?end=last">Last</s:a>
					</s:else>


				</s:if>
			</s:div>
		</div>
	</body>
</html>

<code id="headId" style="visibility: hidden">
$RCSfile: bo_um_searchOfflineRetUser_SearchResult.jsp,v $
$Revision: 1.2 $
</code>