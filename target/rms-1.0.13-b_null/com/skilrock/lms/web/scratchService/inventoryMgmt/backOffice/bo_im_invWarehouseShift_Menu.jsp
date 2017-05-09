<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
	<head>

		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
		<link rel="stylesheet"
			href="<%=request.getContextPath() %>/LMSImages/css/styles.css"
			type="text/css" />
		<style>
.tabNo {
	text-align: left;
	vertical-align: top;
}
</style>
		<title><%=application.getAttribute("JSP_PAGE_TITLE")%></title>
		<s:head theme="ajax" debug="false" />
		<script
			src="<%=request.getContextPath() %>/com/skilrock/lms/web/scratchService/inventoryMgmt/backOffice/js/shiftInv.js"></script>
	</head>
	<body onload="fetchDet(),appendGameTab()">

		<%@include file="/com/skilrock/lms/web/loginMgmt/menu.jsp"%>
		<div id="wrap">
			<div id="top_form">
				<h3>
					<s:text name="Shifting Inventory to Another Warehouse " />
				</h3>

				<s:form name="shiftIn" action="bo_im_invWarehouseShift_save"
					onsubmit="return onSub()">
					<table border="0" cellpadding="3" cellspacing="0" width="100%">
						<tr>
							<td colspan="3">
								<div id="retOrgDiv" style="text-align: left;"></div>
							</td>
						</tr>
						<tr>
							<td width="70%" style="display: none;">

								<table>
									<tr>
										<td align="right">
											<i><s:property value="#application.TIER_MAP.AGENT" /> <s:text
													name="label.name" /> : </i>
										</td>
										<td>
											<select class="option" name="agtOrgName" id="agtOrgName"
												onchange="return verifyRetailer();">
												<option value="-1">
													<s:text name="label.please.select" />
												</option>
											</select>
										</td>
									</tr>
									<tr>
										<td align="right">
											<div id="retBalLbl" style="visibility: hidden;">
												<i><s:text name="label.blnce" /> : </i>
											</div>
										</td>
										<td class="data">
											<div id="retBal"></div>
										</td>
									</tr>
								</table>

							</td>

							<td width="70%">
								<div id="retOrgDiv" style="text-align: left; display: none;"></div>
								<table>
									<tr>
										<td align="right">
											<i> <s:text name="label.warehouse.from" /> : </i>
										</td>
										<td>
											<s:select list="#session.FROM_WAREHOUSES" theme="simple"
												cssClass="option" id="fromWH" headerValue="Please Select"
												headerKey="-1" />
										</td>
									</tr>
									<tr>
										<td align="right">
											<i> <s:text name="label.warehouse.to" /> : </i>
										</td>
										<td>
											<s:select list="#session.TO_WAREHOUSES" theme="simple"
												cssClass="option" id="toWH" headerValue="Please Select"
												headerKey="-1" />
										</td>
									</tr>
								</table>

							</td>
							<td align="left" width="30%">
								<input type="button" name="addGame"
									value="<s:text name="Add More Game"/>" class="button"
									onclick="addAnotherGame()" style="visibility: block;" />
								<table style="display: none">
									<s:select cssClass="option" name="gameNbr_NameHid"
										id="gameNbr_NameHid" headerKey="-1"
										headerValue="%{getText('label.please.select')}" list="{}" />
									<tr>
										<td>
											<input type="hidden" id="nog" name="nog"
												value="<%=application.getAttribute("NO_OF_GAME_PER_PAGE")%>" />
										</td>
									</tr>

								</table>

							</td>

						</tr>
						<tr>
							<td colspan="2">
								<hr
									style="border: 0; height: 1px; background-image: -webkit-linear-gradient(left, #f0f0f0, #8c8b8b, #f0f0f0); background-image: -moz-linear-gradient(left, #f0f0f0, #8c8b8b, #f0f0f0); background-image: -ms-linear-gradient(left, #f0f0f0, #8c8b8b, #f0f0f0); background-image: -o-linear-gradient(left, #f0f0f0, #8c8b8b, #f0f0f0);" />
							</td>
						</tr>
						<tr>
							<td colspan="3">
								<div id="mainDiv">
									<table border="0" cellpadding="0" cellspacing="0" width="100%"
										id="gameTab">

									</table>
								</div>

							</td>
						</tr>
						<tr>
							<td align="right" colspan="2">
								<table border="0" align="center">
									<tr align="right">
										<td>
											<div id="subCheck" style="position: absolute;"></div>
											<input type="button" value="Submit" class="button"
												style="visibility: hidden" id="subTrans"
												onclick="submitInv()" />
										</td>
										<td>

											<input type="button" value="<s:text name="btn.verify.all"/>"
												class="button" id="verBk" onclick="verifyBks()" />
										</td>
									</tr>
								</table>
							</td>
						</tr>
					</table>
				</s:form>
			</div>
		</div>
	</body>
</html>
<code id="headId" style="visibility: hidden">
	btn.verify.all $RCSfile: bo_im_invWarehouseShift_Menu.jsp,v $
	$Revision: 1.3 $
</code>