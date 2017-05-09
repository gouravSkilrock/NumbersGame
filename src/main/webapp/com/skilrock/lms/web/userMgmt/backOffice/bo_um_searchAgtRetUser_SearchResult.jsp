<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
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
						<b><s:text name="btn.srch.res" /> </b>
					</td>
				</tr>

				<s:if test=" #session.USER_SEARCH_RESULTS.size !=0 ">
					<tr>

						<th align="center">
							<s:text name="label.user.id" />
						</th>
						<th align="center">
							<s:text name="label.uname" />
						</th>
						<th align="center">
							<s:text name="label.with.org" />
						</th>
						<th align="center">
							<s:text name="label.prnt.org" />
						</th>
						<th align="center">
							<s:text name="label.status" />
						</th>
						<th align="center">
							<s:text name="label.role.name" />
						</th>
						<th align="center">
							<s:text name="label.reg.date" />
						</th>

					</tr>
					<s:iterator id="SearchUserResults1"
						value="#session.USER_SEARCH_RESULTS">
						<s:url id="123">
							<param name="ff" value="=%{userId}">
						</s:url>
						<tr>
							<td align="center">
								<s:a id="link2" targets="top"
									href="com/skilrock/lms/admin/bo_um_searchAgtRetUser_Detail.action?userId=%{userId}">
									<s:property id="gn" value="userId" />
								</s:a>
							</td>
							<td align="center">
								<s:property value="userName" />
							</td>
							<td align="center">
								<s:property value="userOrgName" />
							</td>
							<td align="center">
								<s:property value="parentOrgName" />
							</td>
							<td align="center">
								<s:property value="userStatus" />
							</td>
							<td align="center">
								<s:property value="userRoleName" />
							</td>
							<td align="center">
								<s:property value="registerDate" />
							</td>


						</tr>
					</s:iterator>
				</s:if>
				<s:else>
					<tr>
						<td colspan="9" align="center">
							<s:text name="result">
								<s:text name="msg.no.usr.to.process" />
							</s:text>
						</td>
					</tr>
				</s:else>
			</table>
			<s:div id="naviga" cssStyle="text-align: center">
				<s:if test=" #session.USER_SEARCH_RESULTS1.size >5 ">

					<s:if test="#session.startValueUserSearch!=0">
						<s:a theme="ajax" targets="bottom"
							href="bo_um_searchAgtRetUser_Navigate.action?end=first">
							<s:text name="label.first" />
						</s:a>
						<s:a theme="ajax" targets="bottom"
							href="bo_um_searchAgtRetUser_Navigate.action?end=Previous">
							<s:text name="label.pre" />
						</s:a>
					</s:if>
					<s:else>
						<s:text name="label.first.pre" />
					</s:else>
					<s:if
						test="#session.startValueUserSearch==((#session.USER_SEARCH_RESULTS1.size/5)*5)">
						<s:text name="label.next.last" />
					</s:if>
					<s:else>
						<s:a theme="ajax" targets="bottom"
							href="bo_um_searchAgtRetUser_Navigate.action?end=Next">
							<s:text name="label.next" />
						</s:a>
						<s:a theme="ajax" targets="bottom"
							href="bo_um_searchAgtRetUser_Navigate.action?end=last">
							<s:text name="label.last" />
						</s:a>
					</s:else>
				</s:if>
			</s:div>
		</div>
	</body>
</html>
<code id="headId" style="visibility: hidden">
	$RCSfile: bo_um_searchAgtRetUser_SearchResult.jsp,v $ $Revision:
	1.1.8.3 $
</code>