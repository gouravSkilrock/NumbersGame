<%@ page import="java.util.*"%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@page import="com.skilrock.lms.beans.UserInfoBean"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" 

"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
	<%
		response.setHeader("Cache-Control", "no-store"); //HTTP 1.1
		response.setHeader("Pragma", "no-cache"); //HTTP 1.0
		response.setDateHeader("Expires", 0); //prevents caching at the proxy server
	%>
	<head>
		<s:head theme="ajax" debug="false" />
		<META HTTP-EQUIV="CACHE-CONTROL" CONTENT="NO-CACHE" />
		<meta http-equiv="Content-Type"
			content="text/html; charset=iso-8859-1" />

		<link rel="stylesheet"
			href="<%=request.getContextPath()%>/LMSImages/css/styles.css"
			type="text/css" />

		<title><%=application.getAttribute("JSP_PAGE_TITLE")%></title>
		<script type="text/javascript"
			src="<%=request.getContextPath()%>/com/skilrock/lms/web/roleMgmt/backOffice/js/updateService.js"></script>
	</head>

	<body>
		<%@include file="/com/skilrock/lms/web/loginMgmt/menu.jsp"%>
		<%
			UserInfoBean user = null;
			user = (UserInfoBean) session.getAttribute("USER_INFO");
			System.out.println("Name:--" + user.getUserName() + " Head "
					+ user.getIsRoleHeadUser() + " Role  "
					+ user.getIsMasterRole());
			String isRoleHeadUser = user.getIsRoleHeadUser();
			String isMasterRole = user.getIsMasterRole();
			if (isRoleHeadUser.equalsIgnoreCase("Y")
					& isMasterRole.equalsIgnoreCase("Y")) {
		%>
		<div id="wrap">
			<div id="top_form">
				<h3>
					Create Service
				</h3>

				<div id="outerDiv">
				<s:actionerror />
				<s:iterator value="%{serviceDataMap}" status="serviceIndex">
					<s:form id="form%{key}" action="ProcessServiceAction.action"
						method="post" onsubmit="return check('form%{key}');">
						<table width="100%" cellpadding="0" cellspacing="0" border="0"
							class="tabBdr">
							<tr id="serTr<s:property value="%{key}" />" <s:if test="%{#serviceIndex.index %2 ==0}"> bgcolor="#d1e3f3" </s:if>
							<s:else> bgcolor="#f0ead4" </s:else> onclick="showHideServiceDiv('serDiv<s:property value="%{key}" />')" onmouseover="this.style.cursor='hand';">
								<td>
									<input type="hidden" name="serviceId" id='serviceId<s:property value="%{key}" />'
										value="<s:property value="%{value}" />" />
									<s:property value="%{key}" />
								</td>
							</tr>
							<tr>
								<td>
								<div id="serDiv<s:property value="%{key}" />" style="display: none;">
									<table width="100%" cellpadding="0" cellspacing="0" border="0"
										class="tabBdr" <s:if test="%{#serviceIndex.index %2 ==0}"> bgcolor="#d1e3f3" </s:if>
							<s:else> bgcolor="#f0ead4" </s:else>>
										<tr>
											<td>
												<label class="label">Tiers<span class="required">*</span>:</label> 
											</td>
											<td>
												<s:select  list="%{tierList}" id="tierLevel%{key}"
													name="tierLevel" theme="simple" cssClass="option"
													headerKey="-1" headerValue="--Please Select--"
													onchange="getTierInterfaceDiv(this.value,'%{key}')" ></s:select>
											</td>
										</tr>

										<tr>
											<td colspan="2">
												<div id="radioDiv<s:property value="%{key}" />"
													style="display: none; background-color: #eeeeee; margin: 3px 0px 3px 0px">
													<table width="100%" cellpadding="0" cellspacing="0"
														border="0" bordercolor="#cccccc" class="tabBdr"
														bgcolor="#eeeeee">
														<tr>
															<td>
																<input type="hidden" name="tierValue"
																	id="tierVal<s:property value="%{key}" />" value="All" />
																<input type="radio" value="All"
																	id="radioAll<s:property value="%{key}" />"
																	name="radio<s:property value="%{key}" />"
																	onclick="getParentOrg(this,'tierLevel<s:property value="%{key}" />')" />
																All
															</td>
															<td>
																<input type="radio" value="Specific"
																	id="radioSpecific<s:property value="%{key}" />"
																	name="radio<s:property value="%{key}" />"
																	onclick="getParentOrg(this,'tierLevel<s:property value="%{key}" />')" />
																Specific
															</td>
														</tr>
													</table>
												</div>
												<div id="parentOrgDiv<s:property value="%{key}"/>"
													style="display: none; margin: 3px 0px 3px 0px">
													<table width="100%" cellpadding="0" cellspacing="0"
														border="0" bordercolor="#cccccc" class="tabBdr">
														<tr>
															<td>
																Parent Org
															</td>
															<td>
																<s:if test="%{#serviceIndex.index %2 ==0}">
																	<select id="parentOrgList<s:property value="%{key}" />"
																		name="parentOrgList" class="option"
																		onchange="getChildOrg(this.id,'<s:property value="%{key}"/>','#e7ebef')">
																	</select>
																</s:if>
																<s:else>
																	<select id="parentOrgList<s:property value="%{key}" />"
																		name="parentOrgList" class="option"
																		onchange="getChildOrg(this.id,'<s:property value="%{key}"/>','#f7f4ea')">
																	</select>
																</s:else>

															</td>
														</tr>
													</table>
												</div>
												<div id="childOrgDiv<s:property value="%{key}"/>"
													style="display: none; margin: 3px 0px 3px 0px">
												</div>
												<div id="tierInterfaceDiv_<s:property value="%{key}" />"
													style="display: none; margin: 3px 0px 3px 0px">

												</div>
												<div id="hiddenInterface<s:property value="%{key}" />"
													style="display: none; margin: 3px 0px 3px 0px">
												</div>
											</td>
										</tr>
										<tr>
											<td colspan="2">
												<s:submit align="left" cssClass="button" formId="form%{key}"
													theme="ajax" targets="outerDiv"
													onclick="return interfaceStatusChange('tierInterfaceDiv_%{key}')"/>
											</td>
										</tr>
									</table>
								</div>
								</td>
							</tr>

						</table>
					</s:form>
				</s:iterator>
			</div>
			</div>
		</div>
		<%
			} else {
		%>
		<div>
			<jsp:include page="/com/skilrock/lms/web/loginMgmt/unauthorize.jsp"></jsp:include>
		</div>
		<%
			}
		%>
	</body>
</html>

<code id="headId" style="visibility: hidden">
$RCSfile: bo_rm_UpdateService_menu.jsp,v $
$Revision: 1.2 $
</code>