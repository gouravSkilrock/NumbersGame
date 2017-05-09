<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="com.skilrock.lms.common.utility.FormatNumber"%>
<%@ page
	import="java.util.*,com.skilrock.lms.dge.beans.PWTDrawBean,com.skilrock.lms.dge.beans.DrawIdBean,com.skilrock.lms.dge.beans.FortunePurchaseBean,com.skilrock.lms.dge.beans.PanelIdBean"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
	<%
		response.setHeader("Cache-Control", "no-store"); //HTTP 1.1
		response.setHeader("Pragma", "no-cache"); //HTTP 1.0
		response.setDateHeader("Expires", 0); //prevents caching at the proxy server
	%>
	<head>
		<meta http-equiv="CACHE-CONTROL" content="NO-CACHE" />
		<meta http-equiv="Content-Type"
			content="text/html; charset=iso-8859-1" />
		<link rel="stylesheet"
			href="<%=request.getContextPath()%>/LMSImages/css/styles.css"
			type="text/css" />
		<title><%=application.getAttribute("JSP_PAGE_TITLE")%></title>
		<script type="text/javascript"
			src="<%=request.getContextPath()%>/com/skilrock/lms/web/drawGames/pwtMgmt/agent/js/pwtDirPlayerBO.js"></script>

		<s:head theme="ajax" debug="false" />

	</head>

	<body>
		<%@include file="/com/skilrock/lms/web/loginMgmt/menu.jsp"%>
		<s:if test="%{#session.PWT_RES.pwtStatus=='OUT_LIMITS'}">
			<h3>
				Amount is out of Limits please contact Back Office.
			</h3>
		</s:if>
		<s:else>
			<div id="wrap">
				<div id="top_form">
					<h3>
						Winning amount:
						<b><s:property value="%{#session.PWT_AMT}" /> </b>
					</h3>

					<s:form id="playerPWT" name="playerPWT"
						onsubmit="return validatePlrDtl()">


						<table border="0" cellpadding="3" cellspacing="0" width="400px">
							<tr>
								<th nowrap="nowrap">
									<input type="radio" checked="checked" value="player"
										name="playerType"
										onclick="_id.blk('plrDetail');_id.blk('subAny');_id.non('subAnyPlr');_id.blk('remainDetail');"" />
									Player Verification
								</th>
								<th nowrap="nowrap">

									<input type="radio" value="anonymous" name="playerType"
										onclick="_id.non('plrDetail');_id.non('subAny');_id.blk('subAnyPlr');_id.non('remainDetail');"" />
									Anonymous Player

								</th>
							</tr>
						</table>

						<table border="0" cellpadding="3" cellspacing="0" id="plrDetail">

							<tr>
								<td colspan="2">
									<div id="fn_err" style="text-align: center"></div>
								</td>
							</tr>
							<tr>
								<td>
									<label>
										First Name&nbsp;:
									</label>
								</td>
								<td>
									<table>
										<s:textfield name="firstName" id="firstName" required="true"
											size="10" maxlength="15"
											onchange="_id.non('subAny');_id.non('remainDetail')" />
									</table>
								</td>
							</tr>

							<tr>
								<td colspan="2">
									<div id="ln_err" style="text-align: center"></div>
								</td>
							</tr>
							<tr>
								<td>
									<label>
										Last Name&nbsp;:
									</label>
								</td>
								<td>
									<table>
										<s:textfield name="lastName" id="lastName" required="true"
											size="10" maxlength="15"
											onchange="_id.non('subAny');_id.non('remainDetail')" />
									</table>
								</td>
							</tr>

							<tr>
								<td colspan="2">
									<div id="it_err" style="text-align: center"></div>
								</td>
							</tr>
							<tr>
								<td>
									<label>
										ID Type&nbsp;:
									</label>
								</td>
								<td>
									<table>
										<s:select cssClass="option" headerKey="-1"
											headerValue="-- Please Select --" name="idType" id="idType"
											required="true"
											list="{'Passport','Driving Licence','NID'}"
											onchange="_id.non('subAny');_id.non('remainDetail')" />
									</table>
								</td>
							</tr>

							<tr>
								<td colspan="2">
									<div id="in_err" style="text-align: center"></div>
								</td>
							</tr>
							<tr>
								<td>
									<label>
										ID Number&nbsp;:
									</label>
								</td>
								<td>
									<table>
										<s:textfield name="idNumber" id="idNumber" required="true"
											size="10" maxlength="15"
											onchange="_id.non('subAny');_id.non('remainDetail')" />
									</table>
								</td>
							</tr>

							<tr>
								<td colspan="2">

									<div id="getPlrDetails" style="text-align: center">
										<a href="#"
											onclick="verPlrDetails('com/skilrock/lms/web/scratchService/pwtMgmt/pwt_common_fetchPlayerDetails.action', 'remainDetail')">Verify
											Player</a>
									</div>

								</td>
							</tr>

							<tr>
								<td colspan="2">
									<div id="remainDetail"></div>
								</td>
							</tr>

						</table>
						<div id="subCheck" style="position: absolute;"></div>
						<s:submit cssClass="button" formId="playerPWT"
							value="Send Request or Save" cssStyle="display: none;"
							id="subAny" action="pwt_plrRegAndApprovalReqAtAgent" />
						<s:submit cssClass="button" formId="playerPWT"
							value="Save Winning Data " cssStyle="display: none;" id="subAnyPlr"
							action="pwt_AnonymousPlayerRegAgent" />


					</s:form>

				</div>
			</div>
		</s:else>
	</body>

</html>

<code id="headId" style="visibility: hidden">
$RCSfile: Agt_draw_pwt_dirPlr_Registration.jsp,v $
$Revision: 1.3 $
</code>