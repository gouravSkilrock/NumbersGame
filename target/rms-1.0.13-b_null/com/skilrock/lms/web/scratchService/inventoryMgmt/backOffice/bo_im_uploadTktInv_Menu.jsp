<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">

<head>
<s:head theme="ajax" debug="false" />
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<link rel="stylesheet"
	href="<%=request.getContextPath()%>/LMSImages/css/styles.css"
	type="text/css" />
<title><%=application.getAttribute("JSP_PAGE_TITLE")%></title>

<script type="text/javascript"
	src="<%=request.getContextPath() %>/com/skilrock/lms/web/scratchService/inventoryMgmt/backOffice/js/uploadGameTicketInventory.js"></script>
</head>

<body
	onload="getGameListForInventory('bo_im_uploadTktInv_SupplierList.action?gameType=NEW& callFlag=FIRST', 'game_Name', 'supplierName')">
	<%@include file="/com/skilrock/lms/web/loginMgmt/menu.jsp"%>
	<div id="wrap">
		<div id="top_form">
			<h3>
				<s:text name="label.game.tkts.inv.upload" />
			</h3>
			<s:form name="ticketupload" method="POST" onsubmit="return isEmpty()">
				<table border="0" cellpadding="3" cellspacing="0">
					<tr>
						<td colspan="2">
							<table>
								<s:radio id="gtype" key="label.slct.game.type" name="gameType"
									value="#{'NEW':'New Games'}"
									list="#{'NEW':'New Games','OPEN':'Old Open Games'}"
									onclick="getGameListForInventory('bo_im_uploadTktInv_SupplierList.action?gameType=' + this.value, 'game_Name'), disableSubmit()" />
							</table>
						</td>
					</tr>
					<tr>
						<td colspan="2"><div id="game_name_error"></div>
						</td>
					</tr>
					<tr>
						<td colspan="1" align="right"><label class="label"><s:text
									name="label.slct.game" /></>:</label>
						</td>
						<td colspan="1"><s:select id="game_Name" theme="simple"
								key="label.slct.game" list="{}" name="gameName"
								emptyOption="false" headerKey="Please Select"
								headerValue="%{getText('label.please.select')}"
								onchange="disableSubmit(), reloadOriginal(this.selectedIndex)"
								cssClass="option" />
						</td>
					</tr>
					<tr>
						<td colspan="2"><div id="supp_name_error"></div>
						</td>
					</tr>
					<tr>
						<td colspan="1" align="right"><label class="label"><s:text
									name="label.slct.supplier" />:</label>
						</td>
						<td colspan="1"><s:select id="supplierName" theme="simple"
								key="label.slct.supplier" list="{}" name="supplierId"
								emptyOption="false" headerKey="Please Select"
								headerValue="%{getText('label.please.select')}"
								cssClass="option" /></td>
					</tr>
					<tr>
						<td colspan="2"><div id="supp_name_error"></div>
						</td>
					</tr>
					<tr>
						<td colspan="2"><div id="warehouse_name_error"></div>
						</td>
					</tr>
					<tr>
						<td colspan="1" align="right">
							<label class="label"><s:text name="label.slct.warehouse" />:</label>
						</td>
						<td colspan="1">
							<s:select id="warehouseId" theme="simple"
								key="label.slct.warehouse" list="warehouseMap" name="warehouseId"
								emptyOption="false" headerKey="Please Select"
								headerValue="%{getText('label.please.select')}"
								cssClass="option" />
						</td>
					</tr>
				</table>

				<tr>
					<td align="center"><div id="fill_error"></div>
					</td>
				</tr>


				<table width="100%" border="0" cellpadding="3" cellspacing="0"
					style="bordercolor : #CCCCCC">
					<tr>
						<td colspan="4"><div id="errorId1"></div>
						</td>
					</tr>
					<tr>
						<td style="width :50%" align="center"><div id="pack1"></div>
						</td>
					</tr>
				</table>






				<table id="addrowtb2" border="1" width="100%" cellpadding="3"
					cellspacing="0" style="bordercolor : #CCCCCC">
					<tr>
						<th colspan="4" align="center" style="width :45%"><s:text
								name="lable.entr.pack.nbr.series.from.to"></s:text></th>
						<th align="center" style="width :10%"><s:text
								name="label.nbr.of.packs"></s:text></th>
						<th align="center" style="width :15%"><input type="button"
							name="addPack" value="<s:text name="btn.add.row"/>" id="addPack"
							onclick="return addPackToTable('addrowtb2');" class="button"
							style="background-color: white" /></th>
						<th align="center" style="width :30%"><s:text
								name="label.error.msg"></s:text></th>
					</tr>
					<tr>
						<td style="width :10%"><s:text name="label.from"></s:text>
						</td>
						<td style="width :15%"><s:textfield theme="simple" size="10"
								name="packNumberFrom" id="pnumfrom-0"
								onkeypress=" return isValidEntries(this)"
								onblur="validateFrom(this.id)" onchange="disableSubmit()" /></td>
						<td style="width :5%"><s:text name="label.to"></s:text>
						</td>
						<td style="width :15%"><s:textfield theme="simple" size="10"
								name="packNumberTo" id="pnumto-0"
								onkeypress=" return isValidEntries()"
								onblur="validateFromTo(this.id)" onchange="disableSubmit()"></s:textfield>
						</td>
						<td style="width :10%">
							<div id="totpk-0">
								<s:textfield id="totpk-0" theme="simple" size="3"
									name="totalpack" readonly="true" />
							</div></td>
						<td style="width :15%">&nbsp;</td>
						<td style="width :30%"><div id="error-0">&nbsp;</div>
						</td>
					</tr>
				</table>


				<table width="100%" border="0" cellpadding="3" cellspacing="0"
					style="border-color: #CCCCCC">
					<tr>
						<td align="center"><input type="button" id="verify"
							name="Verify" value="<s:text name="btn.verify"/>" class="button"
							onclick="verifyInsertedPacks('bo_im_uploadTktInv_ChkSeriesValidity.action', 'fill_error')"
							onkeypress="verifyInsertedPacks('bo_im_uploadTktInv_ChkSeriesValidity.action', 'fill_error')" />
							<s:submit theme="simple" id="subButton" key="btn.submit"
								onclick="return isEmpty()" onkeypress="return isEmpty()"
								action="bo_im_uploadTktInv_Save" cssClass="button" /></td>
					</tr>
				</table>


				<%
					int gameDigit = 0;
						if (session.getAttribute("GAME_DIGITS") != null)
							gameDigit = (Integer) session.getAttribute("GAME_DIGITS");
				%>
				<div id="hiddenDiv" style="display: none">
					<input type="hidden" value="<%=gameDigit%>" id="hidd" />
				</div>




			</s:form>
		</div>
	</div>
</body>
</html>
<code id="headId" style="visibility: hidden"> $RCSfile:
	bo_im_uploadTktInv_Menu.jsp,v $ $Revision: 1.3 $ </code>