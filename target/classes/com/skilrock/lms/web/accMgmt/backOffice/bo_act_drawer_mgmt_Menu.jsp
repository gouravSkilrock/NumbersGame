<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>


<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">

	<head>

		<meta http-equiv="Content-Type"
			content="text/html; charset=iso-8859-1" />
		<link rel="stylesheet"
			href="<%=request.getContextPath()%>/LMSImages/css/styles.css"
			type="text/css" />
		<title><%=application.getAttribute("JSP_PAGE_TITLE")%></title>
		<script type="text/javascript"
			src="<%=request.getContextPath()%>/com/skilrock/lms/web/accMgmt/backOffice/js/common_cash_register.js"></script>

	</head>

	<body onload="appendDenoTab();">
		<%@include file="/com/skilrock/lms/web/loginMgmt/menu.jsp"%>
		<div id="wrap">
			<div id="top_form">

				<h3>
					Drawer Management
				</h3>
				<s:form action="bo_act_create_drawer_success" id="form1" onsubmit="return verifyCreateDrawer()" >
					<s:radio
						list="#{'CREATE':'CREATE DRAWER' ,'ASSIGN':'ASSIGN DRAWER', 'CLEAR':'CLEAR DRAWER'}"
						id="drawer_mgmt_Type" value="'CRETAE'"
						label="Drawer Management Type" name="drawer_mgmt_Type"
						onclick="drawerMgmtTypeShow(this)" />


					<table id="createTab" style="display: none;" cellpadding="0"
						cellspacing="0">
						<tr>
							<td>
								<h3>
									Create Drawer
								</h3>
							</td>
						</tr>

						<tr>
							<td colspan="2">
								<div style="color: red; text-align: center"
									id="createDrawerName_e"></div>

							</td>
						</tr>
						<tr>
							<td>
								<s:textfield name="createDrawerName" label="Drawer Name"
									id="createDrawerName" name="createDrawerName"
									onchange="_id.non('createDivSuccess');" />
							</td>
						</tr>
						<tr>
							<td>
								<s:textarea label="Description" name="remarks"
									cssStyle="height: 2cm;width: 5cm; border-color : black; "
									value="" id="remarks" />
							</td>
						</tr>
						<tr>
							<td colspan="3">
								<s:submit id="submit3" name="submit3" value="Submit"
									align="center" cssClass="button" theme="ajax"
									targets="createDivSuccess" />
							</td>
						</tr>
						<tr>
							<td>
								<div style="color: red; text-align: left;">
									<s:actionmessage />
								</div>
							</td>
						</tr>
					</table>
				</s:form>
				<s:form action="bo_act_assign_drawer_success" id="form2" onsubmit="return verifyAssignDrawee()">
					<table border="0" cellpadding="2" cellspacing="0"
						style="display: none;" id="asssignTab">
						<tr>
							<td>
								<h3>
									Assign Drawer
								</h3>
							</td>
						</tr>

						<tr>
							<td>
								<table border="0" cellpadding="2" cellspacing="0">
									<tr>
										<td colspan="2">
											<div style="color: red; text-align: center"
												id="drawerexist_e"></div>

										</td>
									</tr>
									<tr>
										<td colspan="2">
											<div style="color: red; text-align: center" id="drawerName_e"></div>

										</td>
									</tr>
									<tr>
										<td colspan="1">
											<s:select label="Drawer Name" name="drawerName"
												headerKey="-1" id="drawerName"
												headerValue="--Please Select--" list="{}" cssClass="option"
												required="true" onchange="checkDrawerAvailability()"></s:select>
										</td>
									</tr>
									<tr>
										<td colspan="2">
											<div style="color: red; text-align: center"
												id="cashierName_e"></div>
										</td>
									</tr>
									<tr>
										<td colspan="2">
											<s:select label="Cashier Name" name="cashierName"
												headerKey="-1" id="cashierName"
												headerValue="--Please Select--" list="{}" cssClass="option"
												required="true"></s:select>
										</td>
										<td align="right">
											<b>TOTAL AMOUNT:</b>
										</td>
										<td>
											<input type="text" id="totalAmt" size="10"
												readonly="readonly" />
										</td>
									</tr>

								</table>
							</td>

							<td colspan="2"></td>
							<td colspan="4">
								<h3>
									Enter Intial Amount
								</h3>
								<table border="1" cellpadding="0" cellspacing="0" width="200"
									id="table" style="border-bottom-color: purple;">
									<tr>
										<th>
											Type
										</th>
										<th></th>
									</tr>

								</table>

							</td>
						</tr>
						<tr>
							<td colspan="3">
								<s:submit id="submit2" name="submit2" value="Submit"
									align="center" cssClass="button" theme="ajax"
									targets="assignDiv" />

							</td>
						</tr>

					</table>
				</s:form>
				<s:form action="bo_act_clear_drawer_success" id="form3" onsubmit="return verifyClearDrawerData()">
					<table border="0" cellpadding="2" cellspacing="0"
						style="display: none;" id="clearTab">
						<tr>
							<td>
								<h3>
									Clear Drawer
								</h3>
							</td>
						</tr>
						<tr>
							<td>
								<div id="clearDrawer_e" style="color: red;" />
							</td>
						</tr>
						<tr>
							<td colspan="2">
								<s:select label="Drawer Name" name="drawerId" headerKey="-1"
									id="drawerId" headerValue="--Please Select--" list="{}"
									cssClass="option" required="true"
									onchange="checkDrawerStatusBeforeClear()"></s:select>
							</td>
						</tr>

						<tr>
							<td>
								<table>
									<s:submit id="submit1" name="submit1" value="Submit"
										align="right" cssClass="button" theme="ajax"
										targets="clearDivSuccess" />
								</table>
							</td>
						</tr>
					</table>

				</s:form>
				<div id="clearDivSuccess"></div>
				<div id="assignDiv"></div>
				<div id="createDivSuccess"></div>
			</div>
		</div>
	</body>

</html>
<code id="headId" style="visibility: hidden">
	$RCSfile: bo_act_drawer_mgmt_Menu.jsp,v $ $Revision: 1.2 $
</code>
