<%@ taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
	<%
		response.setHeader("Cache-Control", "no-store"); //HTTP 1.1
		response.setHeader("Pragma", "no-cache"); //HTTP 1.0
		response.setDateHeader("Expires", 0); //prevents caching at the proxy server
	%>
	<head>
		<META HTTP-EQUIV="CACHE-CONTROL" CONTENT="NO-CACHE" />
		<meta http-equiv="Content-Type"
			content="text/html; charset=iso-8859-1" />
		<link rel="stylesheet"
			href="<%=request.getContextPath()%>/LMSImages/css/styles.css"
			"type="text/css" />
		<title><%=application.getAttribute("JSP_PAGE_TITLE")%></title>
		<script type="text/javascript"
			src="<%=request.getContextPath()%>/com/skilrock/lms/web/scratchService/inventoryMgmt/retailer/js/ret_activateBooks.js"></script>
	</head>

	<body>
		<%@include file="/com/skilrock/lms/web/loginMgmt/menu.jsp"%>
		<div id="wrap">
			<div id="top_form">
				<h3>
					Activate Books
				</h3>
				<s:if test="#session.GAME_BOOK_MAP.size()!=0">
					<s:form name="retGameBookDetails"
						action="ret_im_activate_books_Save" onsubmit="return validate()">
						<table width="100%" border="1" cellpadding="3" cellspacing="0"
							bordercolor="#CCCCCC" align="center">
							<tr>
								<td>
									<input type="checkbox" id="checkAll"
										onclick="selectAllBooks('checkAll')" />
									Select All Books
								</td>
							</tr>
							<s:iterator value="%{#session.GAME_BOOK_MAP}">
								<tr>
									<td>
										<table width="100%" id="<s:property value="%{key}" />"
											cellpadding="0" cellspacing="0" border="0">
											<tr>
												<th colspan="5" align="center">
													<input type="checkbox"
														id="gameChkAll_<s:property value="%{key}" />"
														onclick="selectGameBook('<s:property value="%{key}" />',this.id)" />
													<s:property value="%{key}" />
												</th>
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
														<table>
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
							<tr>
								<td align="center">
									<s:submit theme="simple" align="right" value="Activate Selected Books"
										cssClass="button" />
								</td>
							</tr>
						</table>

					</s:form>
				</s:if>
				<s:else>
					<table width="100%" border="1" cellpadding="3" cellspacing="0"
						bordercolor="#CCCCCC" align="center">
						<tr align="center">
							<td>No Books Available to Activate</td>
						</tr>
					</table>
				</s:else>
			</div>
		</div>
	</body>
</html>

<code id="headId" style="visibility: hidden">
$RCSfile: ret_im_activateBooks_Menu.jsp,v $
$Revision: 1.3 $
</code>