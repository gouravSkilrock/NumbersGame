<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">

	<head>
		<meta http-equiv="Content-Type"
			content="text/html; charset=iso-8859-1" />
		<link rel="stylesheet"
			href="<%=request.getContextPath()%>/LMSImages/css/styles.css"
			type="text/css" />
		<title><%=application.getAttribute("JSP_PAGE_TITLE")%></title>
	</head>
	<body>
		<div id="wrap">
			<div id="top_form">
				<s:form action="admin_mainPriviledgeSave">
					<table width="100%" border="1" cellpadding="3" cellspacing="0"
						bordercolor="#CCCCCC" align="center">
						<s:hidden name="privTlbName" value="%{privTlbName}"></s:hidden>
						<tr>
							<th>
								S.No.
							</th>
							<th>
								Privilege Display NAme
							</th>
							<th>
								Privilege Owner
							</th>
							<th>
								Related To
							</th>
							<th>
								Menu Display Name
							</th>
							<th>
								Status
							</th>
						</tr>
						<s:iterator value="#session.PRIV_LIST" status="index">
							<tr>
								<td>
									<s:property value="#index.index+1" />
									<s:hidden name="actionId" value="%{actionId}"></s:hidden>
									<s:hidden name="privId" value="%{privId}"></s:hidden>
								</td>
								<td>
									<s:property value="privDispName" />
								</td>
								<td>
									<s:property value="privOwner" />
								</td>
								<td>
									<s:property value="relatedTo" />
								</td>
								<td>
									<s:textfield name="manuDispName" theme="simple"
										value="%{manuDispName}"></s:textfield>
								</td>
								<td>
									<s:select cssClass="option" list="{'ACTIVE','INACTIVE'}"
										name="status" value="%{status}" theme="simple"></s:select>
								</td>
							</tr>
						</s:iterator>
						
					</table>
					<s:submit value="Assign Priviledges" align="left"
								cssClass="button" />
				</s:form>
			</div>
		</div>
	</body>
</html>
<code id="headId" style="visibility: hidden">
	$RCSfile: admin_fetchMainPrivSearch.jsp,v $ $Revision: 1.2 $
</code>