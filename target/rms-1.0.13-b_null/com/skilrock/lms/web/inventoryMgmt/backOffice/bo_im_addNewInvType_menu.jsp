<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">

	<head>
		<s:head theme="ajax" debug="false" />
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<link rel="stylesheet"
			href="<%=request.getContextPath()%>/LMSImages/css/styles.css"
			type="text/css" />
		<title><%=application.getAttribute("JSP_PAGE_TITLE")%></title>
		<script type="text/javascript"
			src="<%=request.getContextPath()%>/com/skilrock/lms/web/common/globalJs/jquery-1.10.1.min.js"></script>
		<script type="text/javascript"
			src="<%=request.getContextPath()%>/com/skilrock/lms/web/inventoryMgmt/backOffice/js/consNNonConsInv.js"></script>
	</head>


	<body onload="document.forms[0].reset()">
		<%@include file="/com/skilrock/lms/web/loginMgmt/menu.jsp"%>

		<div id="wrap">

			<div id="top_form">
				<h3>
					<s:text name="label.add.inv.detail" />
				</h3>

				<s:form name="addInv" action="bo_im_newInvType_save"
					onsubmit="return _addInvValidate()" method="post"
					enctype="multipart/form-data">
					<s:actionerror />

					<table border="0" cellpadding="3" cellspacing="0" width="500px">

						<s:radio
							list="#{'NON_CONS':getText('label.nonconsum'), 'CONS':getText('label.consum')}"
							id="inv_type" value="'NON_CONS'" key="label.invntry.type"
							name="invType" onclick="invTypeShow(this)" />


						<tr>
							<td colspan="2">
								<table style="display: block;" border="0" cellpadding="3"
									cellspacing="0" width="500px" id="non_cons_tbl">

									<tr>
										<td>
											<table border="0" cellpadding="3" cellspacing="0"
												width="500px">
												<tr>
													<td align="left">
														<s:radio theme="simple"
															list="#{'ADD_INV':getText('label.addnew.nonconsum.invntry')}"
															value="%{'ADD_INV'}" name="addType" id="add_inv"
															onclick="_id.non('new_brand_div');_id.non('new_model_div');_id.blk('new_inv_div'); _id.i('inv_div_err', '');" />
													</td>
												</tr>
												<tr>
													<td align="left">
														<s:radio theme="simple"
															list="#{'ADD_BRAND':getText('label.add.newbrand.exstng.invntry')}"
															name="addType" id="add_brand"
															onclick="_id.non('new_model_div');_id.non('new_inv_div');_id.blk('new_brand_div');_id.i('inv_div_err', '');" />
													</td>
												</tr>
												<tr>
													<td align="left">
														<s:radio theme="simple"
															list="#{'ADD_MODEL':getText('label.addnew.model.existing.brand')}"
															name="addType" id="add_model"
															onclick="_id.non('new_brand_div');_id.non('new_inv_div');_id.blk('new_model_div');_id.blk('new_length_div');_id.i('inv_div_err', '');" />
													</td>
												</tr>
											</table>
										</td>
									</tr>


									<tr>
										<td colspan="2">
											<div id="inv_div_err">
											</div>
										</td>
									</tr>
									<tr>
										<td colspan="2">
											<table id="new_inv_div"
												style="display: block; text-align: left" border="1"
												cellpadding="4" cellspacing="0" width="400px"
												bordercolor="#CCCCCC">
												<tr>
													<th align="right">
														<s:text name="label.add.new.inv.detail" />
														&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
														<input type="button"
															value="<s:text name="btn.add.more.inv" />"
															onclick="addInvTbl('new_inv_div', 'nonCons')" />
														&nbsp;&nbsp;
													</th>
												</tr>
												<tr>
													<td>
														<table style="display: block; text-align: left" border="0"
															cellpadding="4" cellspacing="0">
															<s:textfield name="newInvName" key="label.inv.name"
																id="nonCons_new_inv_name-0" cssStyle="text-align:left"
																maxlength="39" />
															<s:textarea key="label.dscrptn"
																cssStyle="height: 2cm;width: 5cm; border-color : black; "
																id="nonCons_new_inv_desc-0" name="newInvDesc" value=""
																onkeypress="return (this.value.length <= 100);" />
															<s:file name="invImg" id="nonCons_inv_img-0" value=""
																key="label.upload.inv.img" />
														</table>
													</td>
												</tr>
											</table>
										</td>
									</tr>

									<tr>
										<td colspan="2">
											<table id="new_brand_div"
												style="display: none; text-align: left" border="1"
												cellpadding="4" cellspacing="0" width="400px"
												bordercolor="#CCCCCC">
												<tr>
													<th align="right">
														<s:text name="label.add.new.brand.detail" />
														&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
														<input type="button"
															value="<s:text name="btn.add.more.brand" />"
															onclick="addBrandTbl('new_brand_div', 'nonCons')" />
														&nbsp;&nbsp;
													</th>
												</tr>
												<tr>
													<td>
														<table border="0" cellpadding="4" cellspacing="0">
															<s:select name="invIdForBrand" id="nonCons_b_inv_list-0"
																key="label.invntry.name" headerKey="-1"
																headerValue="%{getText('label.please.select')}"
																list="#session.INV_NAME_LIST" cssClass="option" />
															<s:textfield name="newBrandName" key="label.brand.name"
																maxlength="39" id="nonCons_new_brand_name-0" />
															<s:textarea key="label.dscrptn"
																cssStyle="height: 2cm;width: 5cm; border-color : black; "
																name="newBrandDesc" id="nonCons_new_brand_desc-0"
																value="" onkeypress="return (this.value.length <= 100);" />
														</table>
													</td>
												</tr>
											</table>
										</td>
									</tr>

									<tr>
										<td colspan="2">
											<table id="new_model_div"
												style="display: none; text-align: left" border="1"
												cellpadding="4" cellspacing="0" width="400px"
												bordercolor="#CCCCCC">
												<tr>
													<th align="right">
														<div id="b_div">
															<s:select name="brandIds" id="m_brand_list"
																cssStyle="display:none;" theme="simple" headerKey="-1"
																headerValue="%{getText('label.please.select')}"
																list="#session.BRAND_NAME_LIST" cssClass="option" />
														</div>
														<s:text name="label.add.new.model.detail" />
														&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
														<input type="button"
															value="<s:text name="btn.add.more.model" />"
															onclick="addModelTbl('new_model_div', 'nonCons')" />
														&nbsp;&nbsp;
													</th>
												</tr>
												<tr>
													<td>
														<table border="0" cellpadding="4" cellspacing="0">
															<s:select name="invIdForModel" id="nonCons_m_inv_list-0"
																key="label.invntry.name" headerKey="-1"
																headerValue="%{getText('label.please.select')}"
																list="#session.INV_NAME_LIST" cssClass="option"
																onchange="setBrandList(this.value, 'nonCons_m_brand_list-0')" />
															<s:select name="brandIdForModel"
																id="nonCons_m_brand_list-0" key="label.brand.name"
																headerKey="-1" headerValue="%{getText('label.please.select')}" list="{}"
																cssClass="option" />
															<s:textfield name="newModelName" key="label.model.name"
																maxlength="39" id="nonCons_new_model_name-0" />
															<s:select list="{'NO','YES'}" name="isReqOnReg"
																id="isReqOnReg" key="label.req.on.reg" cssClass="option" />
															<s:textfield name="checkBindingLength"
																id="binding_length"
																key="label.bind.len.max.two.digit.allow" maxlength="39" />
															<s:textarea key="label.dscrptn"
																cssStyle="height: 2cm;width: 5cm; border-color : black; "
																name="newModelDesc" id="nonCons_new_model_desc-0"
																value="" onkeypress="return (this.value.length <= 100);" />
														</table>
													</td>
												</tr>
											</table>
										</td>
									</tr>
								</table>
							</td>
						</tr>





						<tr>
							<td colspan="2">
								<table border="0" cellpadding="3" cellspacing="0" width="500px"
									id="cons_tbl" style="display: none;">

									<tr>
										<td>
											<table border="0" cellpadding="3" cellspacing="0"
												width="500px">
												<tr>
													<td align="left">
														<s:radio theme="simple"
															list="#{'ADD_INV':getText('label.addnew.consum.inv')}"
															value="%{'ADD_INV'}" name="consAddType" id="add_inv"
															onclick="_id.non('new_cons_model_div');_id.blk('new_cons_inv_div');_id.i('cons_inv_div_err', '');" />
													</td>
												</tr>
												<tr>
													<td align="left">
														<s:radio theme="simple"
															list="#{'ADD_MODEL_SPEC':getText('label.addnew.spec.for.consum.inv')}"
															name="consAddType" id="add_model"
															onclick="_id.non('new_cons_inv_div');_id.blk('new_cons_model_div');_id.i('cons_inv_div_err', '');" />
													</td>
												</tr>
											</table>
										</td>
									</tr>

									<tr>
										<td colspan="2">
											<div id="cons_inv_div_err"></div>
										</td>
									</tr>
									<tr>
										<td colspan="2">
											<table id="new_cons_inv_div"
												style="display: block; text-align: left" border="1"
												cellpadding="4" cellspacing="0" width="400px"
												bordercolor="#CCCCCC">
												<tr>
													<th align="right">
														<s:text name="label.add.new.inv.detail" />
														&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
														<input type="button"
															value="<s:text name="btn.add.more.inv" />"
															onclick="addInvTbl('new_cons_inv_div', 'cons')" />
														&nbsp;&nbsp;
													</th>
												</tr>
												<tr>
													<td>
														<table style="display: block; text-align: left" border="0"
															cellpadding="4" cellspacing="0">
															<s:textfield name="newInvName" key="label.inv.name"
																id="cons_new_inv_name-0" cssStyle="text-align:left"
																maxlength="39" />
															<s:textarea key="label.dscrptn"
																cssStyle="height: 2cm;width: 5cm; border-color : black; "
																id="cons_new_inv_desc-0" name="newInvDesc" value=""
																onkeypress="return (this.value.length <= 100);" />
															<s:file name="invImg" id="inv_img-0" value=""
																key="label.upload.inv.img" />
														</table>
													</td>
												</tr>
											</table>
										</td>
									</tr>

									<tr>
										<td>
											<table id="new_cons_model_div"
												style="display: none; text-align: left" border="1"
												cellpadding="4" cellspacing="0" width="400px"
												bordercolor="#CCCCCC">
												<tr>
													<th align="right">
														<s:text name="label.add.new.model.detail" />
														&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
														<input type="button"
															value="<s:text name="label.add.more.spec" />"
															onclick=addSpecTbl( 'new_cons_model_div', 'cons');
/>
														&nbsp;&nbsp;
													</th>
												</tr>
												<tr>
													<td>
														<table border="0" cellpadding="4" cellspacing="0">
															<s:select name="invIdForModSpec" id="cons_m_inv_list-0"
																key="label.invntry.name" headerKey="-1"
																headerValue="%{getText('label.please.select')}"
																list="#session.CONS_INV_NAME_LIST" cssClass="option" />
															<s:select name="specType" key="label.spcfctn.type"
																list="{'LENGTH', 'COUNT'}" id="cons_spec_type-0"
																cssClass="option" onchange="changeSpec(this.id)" />
															<s:textfield name="specValue" key="label.spcfctn.val"
																maxlength="10" id="cons_spec_value-0" />
															<s:textfield name="specUnit" key="label.spcfctn.unit"
																maxlength="19" id="cons_spec_unit-0" />
															<s:textfield name="costPerUnit" key="label.cost.per.unit"
																maxlength="10" id="cons_cost_per_unit-0" />
															<s:textarea key="label.dscrptn"
																cssStyle="height: 2cm;width: 5cm; border-color : black; "
																name="newModSpecDesc" id="cons_new_model_desc-0"
																value="" onkeypress="return (this.value.length <= 100);" />
														</table>
													</td>
												</tr>
											</table>
										</td>
									</tr>
								</table>
							</td>
						</tr>

						<tr>
							<td colspan="2">
								<table width="500px" style="text-align: left" border="0">
									<tr>
										<td colspan="2" align="center">
											<s:submit name="search" key="btn.submit" align="center"
												cssClass="button" />
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
	$RCSfile: bo_im_addNewInvType_menu.jsp,v $ $Revision: 1.6 $
</code>