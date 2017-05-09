<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<link rel="stylesheet"
	href="<%=request.getContextPath()%>/LMSImages/css/styles.css"
	type="text/css" />
<title><%=application.getAttribute("JSP_PAGE_TITLE")%></title>
<s:head theme="ajax" debug="false" />
</head>
<body>
	<div id="wrap">
		<div id="top_form">
			<table width="100%" border="1" cellpadding="3" cellspacing="0"
				bordercolor="#CCCCCC" align="center">
				<tr>
					<th><s:text name="label.name" />
					</th>
					<th><s:text name="label.usr.type" />
					</th>
					<th><s:text name="label.dept" />
					</th>
					<th><s:text name="label.rep.mail" />
					</th>
					<th><s:text name="label.status" />
					</th>
					<th><s:text name="label.doneBy" />
					</th>
					<th><s:text name="label.user.registrationDate" />
					</th>
				</tr>
				<s:if test=" #session.USER_LIST1.size !=0 ">
					<s:iterator value="#session.USER_LIST">
						<tr>
							<td><s:a
									href="bo_um_searchBoUser_Detail.action?user_id=%{userId}&& type=%{boUserType}&&userName={userName}">
									<s:property value="firstName" />
									<s:property value="lastName" />
								</s:a>
							</td>
							<td><s:property value="boUserType" />
							</td>
							<td><s:property value="department" />
							</td>
							<td><s:property value="mailingStatus" />
							</td>
							<td><s:property value="status" />
							</td>
							<td><s:property value="registerByUserName" />
							</td>
							<td><s:property value="registrationDate" />
							</td>
						</tr>
					</s:iterator>
				</s:if>
				<s:else>
					<tr>
						<td colspan="5" align="center"><s:text name="msg.no.record" />
						</td>
					</tr>
				</s:else>
			</table>
			<s:div id="naviga" cssStyle="text-align: center">
				<s:if test=" #session.USER_LIST1.size >5 ">
					<s:if test="#session.startValueUserSearch!=0">
						<s:a theme="ajax" targets="searchDiv"
							href="bo_um_searchBoUser_Navigate.action?end=first">
							<s:text name="label.first" />
						</s:a>
						<s:a theme="ajax" targets="searchDiv"
							href="bo_um_searchBoUser_Navigate.action?end=Previous">
							<s:text name="label.pre" />
						</s:a>
					</s:if>
					<s:else>
						<s:text name="label.first.pre" />
					</s:else>
					<s:if
						test="#session.startValueUserSearch==((#session.USER_LIST1.size/5)*5)">
						<s:text name="label.next.last" />
					</s:if>
					<s:else>
						<s:a theme="ajax" targets="searchDiv"
							href="bo_um_searchBoUser_Navigate.action?end=Next">
							<s:text name="label.next" />
						</s:a>
						<s:a theme="ajax" targets="searchDiv"
							href="bo_um_searchBoUser_Navigate.action?end=last">
							<s:text name="label.last" />
						</s:a>
					</s:else>
				</s:if>
			</s:div>
		</div>
	</div>
</body>
</html>
<code id="headId" style="visibility: hidden"> $RCSfile:
	bo_um_searchBoUser_SearchResult.jsp,v $ $Revision: 1.3 $ </code>