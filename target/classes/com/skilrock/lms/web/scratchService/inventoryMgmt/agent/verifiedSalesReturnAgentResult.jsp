<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">

	<head>



		<meta http-equiv="Content-Type"
			content="text/html; charset=iso-8859-1" />

		<link rel="stylesheet" href="<%=request.getContextPath() %>/LMSImages/css/styles.css"
			 type="text/css" />

		<title><%=application.getAttribute("JSP_PAGE_TITLE")%></title>


		<s:head theme="ajax" debug="false" />
	</head>

	<body>

	<%@include
				file="/com/skilrock/lms/web/loginMgmt/menu.jsp"%>

		<div id="wrap">

			<div id="top_form">
				<h3>
					Sales Return
				</h3>
				<s:form action="saveSalesReturnDataAgent">
					<table>
						<tr>
							<td>
								<table align="left" cellpadding="2" cellspacing="0" 
									bordercolor="#CCCCCC">
									<tr>
										<td align="right">
											<s:property value="#application.TIER_MAP.RETAILER" /> Organization:
										</td>
										<td>
											<b><s:property value="#session.ORG_NAME" /></b>
										</td>
									</tr>

									<tr>
										<td align="right">
											Game:
										</td>
										<td>
											<b><s:property value="#session.GAME_NAME" /></b>
										</td>
									</tr>
								</table>

							</td>
						</tr>
						<tr>
							<td>
							<s:if test="#session.VERIFIED_PACK_LIST.size!=0">
								<table border="1" align="center" cellpadding="5" cellspacing="0"
									bordercolor="#CCCCCC" width="400">
									<tr>
										<td width="50%">
											<s:text name="Pack Number" />
										</td>
										<td width="50%">
											<s:text name="Validity Status" />
										</td>
									</tr>

									<s:iterator value="#session.VERIFIED_PACK_LIST">
										<tr>
											<td width="50%">
												<s:property value="%{packNumber}" />
											</td>

											<td width="50%">
												<font color="red">
												<s:property value="%{status}" /> </font>
											</td>
										</tr>
									</s:iterator>
								</table></s:if><br />
							</td>
						</tr>
						<tr>
							<td>
								<s:if test="#session.VERIFIED_BOOK_LIST.size!=0">
								<table border="1" align="center" cellpadding="5" cellspacing="0"
									bordercolor="#CCCCCC" width="400">
									<tr>
										<td width="50%">
											<s:text name="Book Number" />
										</td>
										<td width="50%">
											<s:text name="Validity Status" />
										</td>
									</tr>
									<s:iterator value="#session.VERIFIED_BOOK_LIST">
										<tr>
											<td width="50%">
												<s:property value="%{bookNumber}" />
											</td>

											<td width="50%">
												<font color="red">
												<s:property value="%{status}" /> </font>
											</td>
										</tr>
									</s:iterator>
								</table></s:if>
							</td>
						</tr>
						<tr>
							<td>
								<s:if test="%{#session.SALE_RET_FLAG=='Valid'}">
									<div align="center">
										<table width="75%">
											<tr></tr>
											<tr></tr>
											<tr>
												<td align="right">
												<div id="subCheck" style="position:absolute;"></div>
													<s:submit theme="simple" value="Accept Valid Books /Packs"
														cssClass="button" id="subTrans" onclick="return _subValid(this.id);"/>
												</td>
											</tr>
										</table>
									</div>
								</s:if>

								<s:else>

									<div align="center">
										<table width="75%">
											<tr>
												<td>
													<s:a theme="simple"
														href="displaySalesReturnEntryPageAgent.action">
														<div align="right">
															Do Another Sale Return
														</div>
													</s:a>
												</td>
											</tr>
											
										</table>
									</div>
								</s:else>

							</td>
						</tr>
					</table>

				</s:form>
			</div>
		</div>
	</body>
</html>
<code id="headId" style="visibility: hidden">
$RCSfile: verifiedSalesReturnAgentResult.jsp,v $
$Revision: 1.2 $
</code>