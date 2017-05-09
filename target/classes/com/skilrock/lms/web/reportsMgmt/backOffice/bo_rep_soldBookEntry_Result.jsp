<%@ page contentType="text/html; charset=ISO-8859-1"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<HTML xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
	<head>
		<meta http-equiv="Content-Type"
			content="text/html; charset=UTF-8" />
		<link rel="stylesheet"
			href="<%=request.getContextPath()%>/LMSImages/css/styles.css"
			type="text/css" />
		<title><%=application.getAttribute("JSP_PAGE_TITLE")%></title>
		<s:head theme="ajax" debug="false" />
	</head>

	<body>

		<table border="1" cellpadding="2" cellspacing="0" width="100%"
			bordercolor="#cccccc" align="center">
			<tr style="background: #eee">
					<th align="center">
						<s:text name="label.game.name"/>
					</th>
					<th align="center">
						<s:text name="label.book.details"/>
					</th>
				</tr>
			<s:iterator value="#session.SoldBookMap">
				<s:set id="bookNbrList" name="bookNbrList" value="value" />
				
				<tr>
					<th align="center">
						<s:property value="key" />
					</th>
					<td align="center">
					    <s:if test="#bookNbrList.size>0">
						<table border="1" cellpadding="2" cellspacing="0" width="100%" bordercolor="#cccccc" align="center">
							<tr>
								
								<td align="center"><s:text name="label.book.no"/></td>
								
								<td align="center"><s:text name="label.remaining"/></td>
							</tr>
							<s:iterator value="#bookNbrList">
							<tr>
								<td align="center"><s:property value="key"/></td>
								
								<td align="center"><s:property value="value"/></td>
							</tr>
							
							</s:iterator>
						</table>
						</s:if>
						<s:else>
						<s:text name="msg.no.record"/>
						</s:else>
					</td>
				</tr>
			</s:iterator>
		</table>

	</body>



</HTML>

<code id="headId" style="visibility: hidden">
$RCSfile: bo_rep_soldBookEntry_Result.jsp,v $
$Revision: 1.3 $
</code>