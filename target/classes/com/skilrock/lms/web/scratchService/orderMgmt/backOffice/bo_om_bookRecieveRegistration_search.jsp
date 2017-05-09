<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	<head>
		<title><%=application.getAttribute("JSP_PAGE_TITLE")%></title>
	</head>
	<s:head theme="ajax" />
	<body>
		<s:if test="#session.GAME_BOOK_MAP.size()!=0">
			<s:form name="retGameBookDetails" action="bo_om_bookRecieveRegistration_Success"
				onsubmit="return validate()">
				<s:set name="games" value="0" />
				<s:set name="books" value="0" />
				<s:iterator value="%{#session.GAME_BOOK_MAP}" status="gamesStatus">
					<s:iterator value="%{value}">
						<s:set name="books" value="%{#books +1}" />
					</s:iterator>
					<s:set name="games" value="%{#gamesStatus.count}" />
				</s:iterator>
				<table border="1"  border="1" cellpadding="3" cellspacing="0" bordercolor="#CCCCCC" width="100%">
					<tr>
						<td>
							Number of games :
							<s:property value="%{#games}" />
						</td>
						<td>
							Number of books :
							<s:property value="%{#books}" />
						</td>
					</tr>
				</table>
				<table border="1" cellpadding="3" cellspacing="0" bordercolor="#CCCCCC" width="100%">
					<tr>
						<td>
							<input type="checkbox" id="checkAll" onclick="selectAllBooks( 'checkAll');"/>
							Select All Books
						</td>
					</tr>
					<s:iterator value="%{#session.GAME_BOOK_MAP}">
						<tr>
							<td>
								<table width="100%" id="<s:property value="%{key}" />"
									cellpadding="0" cellspacing="0" border="0">
									<tr>
										<td align="center">
											<table  width="100%" cellpadding="0" cellspacing="0" >
												<tr>
													<td align="center">
											<input type="checkbox"
												id="gameChkAll_<s:property value="%{key}" />"
												onclick="selectGameBook('<s:property value="%{key}" />',this.id)" />
											<s:property value="%{key}" />
										</td>
												</tr>
											</table>
										</td>
									</tr>
									<%!int x = 0, y = 0;%>
									<%
										x = -1;
													y = -5;
									%>
									<s:iterator value="%{value}" id="bookNo">
										<%
											x += 1;
															y += 1;
															if (x % 5 == 0) {
										%>
										<tr>
											<%
												}
											%>
											<td>
												<table width="100%" style="font-size: 12px;">
													<s:checkbox label="%{bookNo}" name="bookNumber"
														fieldValue="%{bookNo}" value="false"></s:checkbox>
												</table>
											</td>
											<%
												if (y % 5 == 0) {
											%>
										</tr>

										<%
											}
										%>

									</s:iterator>
								</table>
							</td>
						</tr>
					</s:iterator>
					<tr style="text-align: center;">
						<td>
							<s:submit theme="simple" value="Mark As Received" cssClass="button"
								align="right" />
						</td>
					</tr>
				</table>

			</s:form>
		</s:if>
		<s:else>
			<table width="100%" border="1" cellpadding="3" cellspacing="0"
				bordercolor="#CCCCCC" align="center">
				<tr align="center">
					<td>
						No Books Available
					</td>
				</tr>
			</table>
		</s:else>
	</body>
</html>


<code id="headId" style="visibility: hidden">
	$RCSfile: bo_om_bookRecieveRegistration_search.jsp,v $ $Revision: 1.3 $
</code>