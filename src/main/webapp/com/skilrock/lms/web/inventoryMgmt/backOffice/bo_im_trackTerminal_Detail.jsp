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

		<style type="text/css">
.outerBorderLeft {
	border-left: 3px solid #000000;
}
</style>
	</head>
	<body>

		<div id="wrap">

			<div id="top_form">

				<h3>
					<s:text name="label.terminal.flow" />
				</h3>
				<s:if test="#session.IS_VALID_TERMINAL != 'false'">
					<s:if test="%{transitionList.size == 0}">
						<table border="0" bordercolor="black" cellspacing="0"
							cellpadding="0">
							<tr>
								<td width="20%">
									<b><s:text name="label.bo" />
									</b>
								</td>
								<td width="20%">
									<b><s:property
											value="#application.TIER_MAP.AGENT.toUpperCase()" /> </b>
								</td>
								<td width="20%">
									<b><s:property
											value="#application.TIER_MAP.RETAILER.toUpperCase()" /> </b>
								</td>
							</tr>
							<tr>
								<td width="20%">
									<img
										src="<%=request.getContextPath()%>/LMSImages/images/black02.jpg"
										width="20" height="110" border="0" />
								</td>
								<td width="20%">
									<img
										src="<%=request.getContextPath()%>/LMSImages/images/black01.jpg"
										width="20" height="110" border="0" />
								</td>
								<td width="20%">
									<img
										src="<%=request.getContextPath()%>/LMSImages/images/black01.jpg"
										width="20" height="110" border="0" />
								</td>
							</tr>
						</table>

					</s:if>

					<s:else>

						<table border="0" bordercolor="black" cellspacing="0"
							cellpadding="0">
							<tr>
								<td>
									<b><s:text name="label.bo" />
									</b>
								</td>
								<td>
									<b><s:property
											value="#application.TIER_MAP.AGENT.toUpperCase()" /> </b>
								</td>
								<td>
									<b><s:property
											value="#application.TIER_MAP.RETAILER.toUpperCase()" /> </b>
								</td>
							</tr>
							<tr>
								<td class="outerBorderLeft">



									<table border="0" cellpadding="0" cellspacing="0">
										<s:iterator value="transitionList">
											<tr>
												<td align="center" valign="baseline">
													<s:if test="%{isBOToAgent}">
														<s:text name="label.bo" /><s:text name="label.to" /> <s:property
															value="#application.TIER_MAP.AGENT" />:<s:property
															value="%{toOwnerName}" />
													</s:if>

													<s:if test="%{isAgentToBO}">
														<s:property value="#application.TIER_MAP.AGENT" />:<s:property
															value="%{fromOwnerName}" />
														<s:text name="label.to" /><s:text name="label.bo" />
			</s:if>

													<s:if test="%{isAgentToRetailer}">
			&nbsp;
			</s:if>

													<s:if test="%{isRetailerToAgent}">
			&nbsp;
			</s:if>
												</td>
											</tr>
											<tr>
												<td>
													<s:if test="%{isBOToAgent}">
														<img
															src="<%=request.getContextPath()%>/LMSImages/images/black.jpg"
															width="100%" height="10" border="0" />

													</s:if>

													<s:if test="%{isAgentToBO}">
														<img
															src="<%=request.getContextPath()%>/LMSImages/images/redNew.jpg"
															width="100%" height="10" border="0" />
													</s:if>

													<s:if test="%{isAgentToRetailer}">
														<div align="right">
															<s:property value="%{fromOwnerId}" />
														</div>
													</s:if>

													<s:if test="%{isRetailerToAgent}">
														<div align="right">
															<s:property value="%{toOwnerId}" />
														</div>
													</s:if>

													<s:if test="%{isAgentToRetailer}">
			&nbsp;
			</s:if>

													<s:if test="%{isRetailerToAgent}">
			&nbsp;
			</s:if>
												</td>
											</tr>
											<tr>
												<td align="center" valign="top">


													<s:if test="%{isBOToAgent}">
														<s:property value="%{txTime}" />
													</s:if>

													<s:if test="%{isAgentToBO}">
														<s:property value="%{txTime}" />
													</s:if>
													<s:if test="%{isAgentToRetailer}">
			&nbsp;
			</s:if>

													<s:if test="%{isRetailerToAgent}">
			&nbsp;
			</s:if>
												</td>

											</tr>
										</s:iterator>
									</table>
								</td>
								<td class="outerBorderLeft">
									<table border="0" cellpadding="0" cellspacing="0">

										<s:iterator value="transitionList">
											<tr>
												<td align="center" valign="baseline">
													<s:if test="%{isBOToAgent}">
					&nbsp;
			</s:if>

													<s:if test="%{isAgentToBO}">
					&nbsp;
			</s:if>

													<s:if test="%{isAgentToRetailer}">
														<s:property value="#application.TIER_MAP.AGENT" />:<s:property
															value="%{fromOwnerName}" />
														<s:text name="label.to" /> <s:property
															value="#application.TIER_MAP.RETAILER" />:<s:property
															value="%{toOwnerName}" />
													</s:if>

													<s:if test="%{isRetailerToAgent}">
														<s:property value="#application.TIER_MAP.RETAILER" />:<s:property
															value="%{fromOwnerName}" />
														<s:text name="label.to" /> <s:property
															value="#application.TIER_MAP.AGENT" />:<s:property
															value="%{toOwnerName}" />
													</s:if>
												</td>
											</tr>
											<tr>
												<td>
													<s:if test="%{isBOToAgent}">
														<s:property value="%{toOwnerId}" />
													</s:if>

													<s:if test="%{isAgentToBO}">
														<s:property value="%{fromOwnerId}" />
													</s:if>

													<s:if test="%{isAgentToRetailer}">

														<img
															src="<%=request.getContextPath()%>/LMSImages/images/black.jpg"
															width="100%" height="10" border="0" />
													</s:if>

													<s:if test="%{isRetailerToAgent}">
														<img
															src="<%=request.getContextPath()%>/LMSImages/images/redNew.jpg"
															width="100%" height="10" border="0" />
													</s:if>

													<s:if test="%{isBOToAgent}">
					&nbsp;
			</s:if>

													<s:if test="%{isAgentToBO}">
					&nbsp;
			</s:if>


												</td>

											</tr>
											<tr>
												<td align="center" valign="top">

													<s:if test="%{isAgentToRetailer}">
														<s:property value="%{txTime}" />
													</s:if>

													<s:if test="%{isRetailerToAgent}">
														<s:property value="%{txTime}" />
													</s:if>

													<s:if test="%{isBOToAgent}">
					&nbsp;
			</s:if>

													<s:if test="%{isAgentToBO}">
					&nbsp;
			</s:if>

												</td>

											</tr>

										</s:iterator>

									</table>
								</td>
								<td class="outerBorderLeft">
									<table border="0" cellpadding="0" cellspacing="0">

										<s:iterator value="transitionList">
											<tr>
												<td align="center" valign="baseline">
													<s:if test="%{isBOToAgent}">
					&nbsp;
			</s:if>

													<s:if test="%{isAgentToBO}">
					&nbsp;
			</s:if>

													<s:if test="%{isAgentToRetailer}">
					&nbsp;
			</s:if>

													<s:if test="%{isRetailerToAgent}">
					&nbsp;
			</s:if>
												</td>
											</tr>
											<tr>
												<td>
													<s:if test="%{isBOToAgent}">
					&nbsp;
			</s:if>

													<s:if test="%{isAgentToBO}">
					&nbsp;
			</s:if>
													<s:if test="%{isAgentToRetailer}">
														<s:property value="%{toOwnerId}" />
													</s:if>

													<s:if test="%{isRetailerToAgent}">
														<s:property value="%{fromOwnerId}" />
													</s:if>
												</td>

											</tr>
											<tr>
												<td align="center" valign="top">
													<s:if test="%{isBOToAgent}">
			&nbsp;
			</s:if>

													<s:if test="%{isAgentToBO}">
			&nbsp;
			</s:if>
													<s:if test="%{isAgentToRetailer}">
			&nbsp;
			</s:if>

													<s:if test="%{isRetailerToAgent}">
			&nbsp;
			</s:if>
												</td>

											</tr>
										</s:iterator>

									</table>
									`
								</td>
							</tr>
						</table>
					</s:else>
				</s:if>
				<s:else>
					<s:text name="label.invalid.tid" />
	</s:else>




			</div>
		</div>
	</body>
</html>
<code id="headId" style="visibility: hidden">
	$RCSfile: bo_im_trackTerminal_Detail.jsp,v $ $Revision: 1.3 $
</code>