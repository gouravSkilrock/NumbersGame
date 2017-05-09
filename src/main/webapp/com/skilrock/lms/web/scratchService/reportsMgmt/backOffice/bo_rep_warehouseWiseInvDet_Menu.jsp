<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<link rel="stylesheet"
	href="<%=request.getContextPath()%>/LMSImages/css/styles.css"
	type="text/css" />
<title><%=application.getAttribute("JSP_PAGE_TITLE")%></title>
<script>path ="<%=request.getContextPath()%>";</script>

<script type="text/javascript"
	src="//ajax.googleapis.com/ajax/libs/jquery/1.9.1/jquery.min.js"></script>

<script type="text/javascript"
	src="<%=request.getContextPath() %>/com/skilrock/lms/web/scratchService/reportsMgmt/backOffice/js/warehouseInventoryDetails.js"></script>
<s:head theme="ajax" debug="false" />
</head>

<body>
	<%@include file="/com/skilrock/lms/web/loginMgmt/menu.jsp"%>
	<div id="wrap">
		<div id="top_form">
			<h3>
				<s:text name="label.book.wise.inv.details" />
			</h3>
			<br />
			<table border="0" width="60%" bordercolor="red" style="text-align: left" cellpadding="2" cellspacing="0">
				<tr>
					<td colspan="2">
						<table width="100%">
							<tr>
								<td width="40%"><label class="label">
									<s:text name="label.warehouse.name" /> : </label>
								</td>
								<td align="left"><select name="warehouseId" class="option" id="warehouseId">
										<option value="-1">ALL</option>
										<s:iterator value="#session.wareHouseMap">
											<option value="<s:property value="key"/>">
												<s:property value="value" />
											</option>
										</s:iterator>
								</select>
								</td>
							</tr>
							<tr>
								<td width="40%">
									<label class="label"> <s:text name="label.game.name" /> : </label>
								</td>
								<td align="left"><select name="gameId" class="option" id="gameId">
										<option value="-1">ALL</option>
										<s:iterator value="#session.gameMap">
											<option value="<s:property value="key"/>">
												<s:property value="value" />
											</option>
										</s:iterator>
								</select>
								</td>
							</tr>
						</table>
					</td>
				</tr>
				<tr>
					<td colspan="1" width="95%" align="right">
						<table>
							<s:submit name="showDetail" targets="d2" id="submit"
								key="btn.show.details" cssClass="button"
								onclick="getInventoryDetails('bo_rep_warehouseWiseInvDet_Detail.action', 'd2')" />
						</table>
					</td>
					<td colspan="1" width="5%">
						<div id="loadingDiv">&nbsp;</div>
					</td>
				</tr>
			</table>
			<br />
			<br />
			<div id="d2"></div>
		</div>
	</div>
</body>
</html>
<code id="headId" style="visibility: hidden"> $RCSfile:
	bo_rep_warehouseWiseInvDet_Menu.jsp,v $ $Revision: 1.3 $ </code>