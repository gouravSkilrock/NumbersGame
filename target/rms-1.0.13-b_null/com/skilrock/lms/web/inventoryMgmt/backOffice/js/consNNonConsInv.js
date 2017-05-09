var i18nObj = parent.frames[0].i18Obj;
function _addInvValidate() {
	_id.i("inv_div_err", "");
	var invTypeArr = _id.n('invType');
	for ( var i = 0, len = invTypeArr.length; i < len; i++) {
		if (invTypeArr[i].checked) {
			// alert("1===="+invTypeArr[i].value);
			if (invTypeArr[i].value == "NON_CONS") {
				var addTypeObjArr = _id.n('addType');
				for ( var k = 0, klen = addTypeObjArr.length; k < klen; k++) {
					// alert("2===="+addTypeObjArr[k].value+"=="+addTypeObjArr[k].checked);
					if (addTypeObjArr[k].checked) {
						// alert("3===="+addTypeObjArr[k].value);
						if (addTypeObjArr[k].value == "ADD_INV") {
							// alert("=======addinv");
							var invNameArr = _id.n('newInvName');
							var flag = true, invValFlag = false;
							for ( var j = 0, jlen = invNameArr.length; j < jlen; j++) {
								if (invNameArr[j].value != "") {
									flag = false;
									if (!isMatch(invNameArr[j].value,
											"[^A-Za-z0-9àâäèéêëîïôœùûüÿçÀÂÄÈÉÊËÎÏÔŒÙÛÜŸÇ\\s]")) {
										_id
												.i(
														"inv_div_err",
														i18nObj
																.prop('error.insert.valid.name')
																+ ".", "e");
										invNameArr[j].style.color = 'red';
										invValFlag = true;
									} else {
										invNameArr[j].style.color = 'gray';
									}
								}
							}
							if (flag || invValFlag) {
								_id.i("inv_div_err", i18nObj
										.prop('error.insert.valid.entry'), "e");
								return false;
							}

						} else if (addTypeObjArr[k].value == "ADD_BRAND") {
							// alert("=======addbrand");
							var invNameArr = _id.n('invIdForBrand');
							var brandNameArr = _id.n('newBrandName');
							var flag = true, invValFlag = false;
							for ( var j = 0, jlen = brandNameArr.length; j < jlen; j++) {
								if (brandNameArr[j].value != "") {
									flag = false;
									if (invNameArr[j].value == -1) {
										_id
												.i(
														"inv_div_err",
														i18nObj
																.prop('error.insert.valid.entry'),
														"e");
										invValFlag = true;
										invNameArr[j].style.color = 'red';
									} else {
										invNameArr[j].style.color = '#4284B0';
									}

									if (!isMatch(brandNameArr[j].value,
											"[^A-Za-z0-9_\\s]")) {
										_id
												.i(
														"inv_div_err",
														i18nObj
																.prop('error.insert.valid.entry'),
														"e");
										brandNameArr[j].style.color = 'red';
										invValFlag = true;
									} else {
										brandNameArr[j].style.color = 'gray';
									}
								}
							}
							if (flag || invValFlag) {
								_id.i("inv_div_err", i18nObj
										.prop('error.insert.valid.entry'), "e");
								return false;
							}

						} else if (addTypeObjArr[k].value == "ADD_MODEL") {
							// alert("=======addmodel");
							var invNameArr = _id.n('invIdForModel');
							var brandNameArr = _id.n('brandIdForModel');
							var modelNameArr = _id.n('newModelName');
							var bindingLength = _id.n('checkBindingLength');
							var flag = true, invValFlag = false;

							for ( var j = 0, jlen = modelNameArr.length; j < jlen; j++) {

								if (modelNameArr[j].value != "") {
									flag = false;
									if (invNameArr[j].value == -1) {
										_id
												.i(
														"inv_div_err",
														i18nObj
																.prop('error.insert.valid.entry'),
														"e");
										invValFlag = true;
										invNameArr[j].style.color = 'red';
									} else {
										invNameArr[j].style.color = '#4284B0';
									}

									if (brandNameArr[j].value == -1) {
										_id
												.i(
														"inv_div_err",
														i18nObj
																.prop('error.insert.valid.entry'),
														"e");
										invValFlag = true;
										brandNameArr[j].style.color = 'red';
									} else {
										brandNameArr[j].style.color = '#4284B0';
									}

									if (!isMatch(modelNameArr[j].value,
											"[^A-Za-z0-9_\\s]")) {
										_id
												.i(
														"inv_div_err",
														i18nObj
																.prop('error.insert.valid.entry'),
														"e");
										modelNameArr[j].style.color = 'red';
										invValFlag = true;
									} else {
										modelNameArr[j].style.color = 'gray';

										for ( var k = 0, klen = bindingLength.length; k < klen; k++) {

											if (isNaN(bindingLength[k].value)) {
												bindingLength[k].style.color = 'red';
												invValFlag = true;
												break;
											}
											if (bindingLength[k].value != "") {
												// alert
												// (bindingLength[k].value);
												if (!isMatch(
														bindingLength[k].value,
														"[\d]+")
														|| bindingLength[k].value < 0
														|| bindingLength[k].value.length > 2) {
													bindingLength[k].style.color = 'red';
													invValFlag = true;
												} else {
													bindingLength[k].style.color = 'gray';
												}
											} else {
												bindingLength[k].value = 0;
												return true;
											}
										}
									}
								}
							}
							if (flag || invValFlag) {
								_id.i("inv_div_err", i18nObj
										.prop('error.insert.valid.entry'), "e");
								return false;
							}

						}

					}
				}
			} else {
				var addTypeObjArr = _id.n('consAddType');
				for ( var k = 0, klen = addTypeObjArr.length; k < klen; k++) {
					if (addTypeObjArr[k].checked) {
						if (addTypeObjArr[k].value == "ADD_INV") {
							// alert("=======consaddinv");
							var invNameArr = _id.n('newInvName');
							var flag = true, invValFlag = false;
							for ( var j = 0, jlen = invNameArr.length; j < jlen; j++) {
								if (invNameArr[j].value != "") {
									flag = false;
									if (!isMatch(invNameArr[j].value,
											"[^A-Za-z0-9\\s]")) {
										_id
												.i(
														"cons_inv_div_err",
														i18nObj
																.prop('error.insert.valid.name'),
														"e");
										invNameArr[j].style.color = 'red';
										invValFlag = true;
									} else {
										invNameArr[j].style.color = 'gray';
									}
								}
							}
							if (flag || invValFlag) {
								_id.i("cons_inv_div_err", i18nObj
										.prop('error.insert.valid.entry'), "e");
								return false;
							}
						} else if (addTypeObjArr[k].value == "ADD_MODEL_SPEC") {
							// alert("=======addmodel");
							var invNameArr = _id.n('invIdForModSpec');
							var specValueArr = _id.n('specValue');
							var costPerUnitArr = _id.n('costPerUnit');
							var specUnitArr = _id.n('specUnit');
							var flag = true, invValFlag = false;
							for ( var j = 0, jlen = invNameArr.length; j < jlen; j++) {
								if (specValueArr[j].value != ""
										|| costPerUnitArr[j].value != ""
										|| invNameArr[j].value != -1) {
									flag = false;
									// alert();
									if (invNameArr[j].value == -1) {
										_id
												.i(
														"cons_inv_div_err",
														i18nObj
																.prop('error.insert.valid.entry'),
														"e");
										invValFlag = true;
										invNameArr[j].style.color = 'red';
									} else {
										invNameArr[j].style.color = '#4284B0';
									}

									if (specUnitArr[j].value != ""
											&& !isMatch(specUnitArr[j].value,
													"[^A-Za-z]")) {
										_id
												.i(
														"cons_inv_div_err",
														i18nObj
																.prop('error.insert.valid.entry'),
														"e");
										specUnitArr[j].style.color = 'red';
										invValFlag = true;
									} else {
										specUnitArr[j].style.color = 'gray';
									}

									if (!isMatch(specValueArr[j].value,
											"[^0-9]")) {
										_id
												.i(
														"cons_inv_div_err",
														i18nObj
																.prop('error.insert.valid.entry'),
														"e");
										specValueArr[j].style.color = 'red';
										invValFlag = true;
									} else {
										specValueArr[j].style.color = 'gray';
									}
									// alert("1");
									if (isNaN(costPerUnitArr[j].value)) {
										_id
												.i(
														"cons_inv_div_err",
														i18nObj
																.prop('error.insert.valid.entry'),
														"e");
										costPerUnitArr[j].style.color = 'red';
										invValFlag = true;
										// alert("2=="+invValFlag);
									} else if (!isDecimal(costPerUnitArr[j].value)) {
										_id
												.i(
														"cons_inv_div_err",
														i18nObj
																.prop('error.insert.valid.entry'),
														"e");
										costPerUnitArr[j].style.color = 'red';
										invValFlag = true;
									} else {
										costPerUnitArr[j].style.color = 'gray';
									}

								}
							}
							// alert("11"+flag+"=="+invValFlag);
							if (flag || invValFlag) {
								_id.i("cons_inv_div_err", i18nObj
										.prop('error.insert.valid.entry'), "e");
								return false;
							}

						}
					}
				}
			}
			break;
		}
	}
	return true;
}

function isDecimal(sText1) {
	var sText = sText1;
	var validChars = "0123456789.-+";
	var isNumber = true;
	var myChar;
	var count = 0;
	if (sText.length == 0) {
		isNumber = false;
	}
	for (i = 0; i < sText.length; i++) {
		myChar = sText.charAt(i);
		if (myChar == "." || myChar == "-" || myChar == "+") {
			count++;
		}
		if (validChars.indexOf(myChar) == -1 || count > 3) {
			isNumber = false;
			break;
		}
	}
	return isNumber;
}

function isMatch(value, expr) {
	var ret = true;
	var charpos = value.search(expr);
	if (value.length > 0 && charpos >= 0) {
		ret = false;
	}
	// alert(value+"===111===="+ret);
	return ret;
}

function changeSpec(specId) {
	var idArr = specId.split("-");
	var row = idArr[1];
	// alert(row+"=="+specId.value);
	var value = _id.o(specId).value;
	if (value == "COUNT") {
		// alert()
		var obj = _id.o('cons_spec_unit-' + row);
		obj.value = "";
		obj.readOnly = true;
	} else {
		var obj = _id.o('cons_spec_unit-' + row);
		obj.value = "";
		obj.readOnly = false;
	}
}

function invTypeShow(obj) {
	var value = obj.value;
	// alert(document.forms[0].name);
	document.forms[0].reset();

	if ("CONS" == value) {// alert();
		_id.non('non_cons_tbl');
		_id.non('new_cons_model_div');

		_id.blk('cons_tbl');
		_id.blk('new_cons_inv_div');

		obj.checked = true;
	} else {
		_id.blk('non_cons_tbl');
		_id.blk('new_inv_div');

		_id.non('cons_tbl');
		_id.non('new_brand_div');
		_id.non('new_model_div');
		obj.checked = true;
	}
}

function addInvTbl(tblId, invType) {
	var prefix = invType + "_";
	var tbl = _id.o(tblId);
	var rowCount = parseInt(tbl.rows.length, 10);

	var row = tbl.insertRow(rowCount);
	var cell = row.insertCell(0);
	var newdiv = document.createElement("div");
	newdiv.setAttribute("id", "new_inv_div" + rowCount);

	newdiv.innerHTML = '<table style="display:block;text-align: left" border="0" cellpadding="4" cellspacing="0" >'
			// +'<tr>'
			// +'<td class="tdLabel"><label class="label">Inventory
			// Type:</label></td>'
			// +'<td><select name="invType" class="option">'
			// +'<option value="NON_CONS">Non Consuamable</option>'
			// +'<option value="CONS">Consuamable</option>'
			// +'</select>'
			// +'</td></tr>'
			+ '<tr><td class="tdLabel">'
			+ '<label class="label">'
			+ i18nObj.prop('label.inv.name')
			+ ':</label>'
			+ '</td><td>'
			+ '<input type="text" name="newInvName" maxlength="39" value="" id="'
			+ prefix
			+ 'new_inv_name-'
			+ rowCount
			+ '" style="text-align:left"/>'
			+ '</td>'
			+ '</tr><tr>'
			+ '<td class="tdLabel"><label for="new_inv_desc" class="label">'
			+ i18nObj.prop('label.desc')
			+ ':</label></td>'
			+ '<td><textarea name="newInvDesc" cols="" rows="" id="'
			+ prefix
			+ 'new_inv_desc-'
			+ rowCount
			+ '" style="height: 2cm;width: 5cm; border-color : black; " onkeypress="return (this.value.length &lt;= 100);"></textarea>'
			+ '</td></tr>'
			+ '<tr>'
			+ '<td class="tdLabel"><label class="label">'
			+ i18nObj.prop('label.inv.img')
			+ ':</label></td>'
			+ '<td><input type="file" name="invImg" value="" id="'
			+ prefix
			+ 'inv_img-' + rowCount + '" /></td>' + '</tr>' + '</table>';
	// alert(newdiv.innerHTML);
	cell.appendChild(newdiv);
	return true;
}

function addBrandTbl(tblId, invType) {
	var prefix = invType + "_";
	var tbl = _id.o(tblId);
	var rowCount = parseInt(tbl.rows.length, 10);

	var row = tbl.insertRow(rowCount);
	var cell = row.insertCell(0);
	var newdiv = document.createElement("div");
	newdiv.setAttribute("id", "new_brand_div" + rowCount);

	newdiv.innerHTML = '<table border="0" cellpadding="4" cellspacing="0" >'
			+ '<tr>'
			+ '<td class="tdLabel"><label class="label">'
			+ i18nObj.prop('label.inv.name')
			+ ':</label></td>'
			+ '<td><select name="invIdForBrand" id="'
			+ prefix
			+ 'b_inv_list-'
			+ rowCount
			+ '" class="option">'
			+ (_id.o(prefix + 'b_inv_list-0').innerHTML).replace('selected',
					' ')
			+ '</select>'
			+ '</td></tr>'
			+ '<tr><td class="tdLabel"><label class="label">'
			+ i18nObj.prop('label.brand.name')
			+ ':</label></td>'
			+ ' <td><input type="text" name="newBrandName" maxlength="39" value="" id="'
			+ prefix
			+ 'new_brand_name-'
			+ rowCount
			+ '"/>'
			+ '</td></tr>'
			+ '<tr><td class="tdLabel"><label class="label">'
			+ i18nObj.prop('label.desc')
			+ ':</label></td>'
			+ '<td><textarea name="newBrandDesc" cols="" rows="" id="'
			+ prefix
			+ 'new_brand_desc-'
			+ rowCount
			+ '" style="height: 2cm;width: 5cm; border-color : black; " onkeypress="return (this.value.length &lt;= 100);"></textarea>'
			+ '</td></tr>' + '</table>';
	// alert(newdiv.innerHTML);
	cell.appendChild(newdiv);
	return true;
}

function addModelTbl(tblId, invType) {
	var prefix = "nonCons_";
	var tbl = _id.o(tblId);
	var rowCount = parseInt(tbl.rows.length, 10);

	var row = tbl.insertRow(rowCount);
	var cell = row.insertCell(0);
	var newdiv = document.createElement("div");
	newdiv.setAttribute("id", "new_model_div" + rowCount);

	newdiv.innerHTML = '<table border="0" cellpadding="4" cellspacing="0" >'
			+ '<tr>'
			+ '<td class="tdLabel"><label class="label">'
			+ i18nObj.prop('label.inv.name')
			+ ':</label></td>'
			+ '<td><select name="invIdForModel" id="'
			+ prefix
			+ 'm_inv_list-'
			+ rowCount
			+ '" class="option" onchange="setBrandList(this.value, \''
			+ prefix
			+ 'm_brand_list-'
			+ rowCount
			+ '\')" >'
			+ (_id.o(prefix + 'm_inv_list-0').innerHTML).replace('selected',
					' ')
			+ '</select>'
			+ '</td></tr>'
			+ '<tr>'
			+ '<td class="tdLabel"><label class="label">'
			+ i18nObj.prop('label.brand.name')
			+ ':</label></td>'
			+ '<td><select name="brandIdForModel" id="'
			+ prefix
			+ 'm_brand_list-'
			+ rowCount
			+ '" class="option">'
			+ '<option value="-1">--Please Select--</option>'
			+ '</select>'
			+ '</td></tr>'
			+ '<tr><td class="tdLabel"><label class="label">'
			+ i18nObj.prop('label.model.name')
			+ ':</label></td>'
			+ ' <td><input type="text" name="newModelName" maxlength="39" value="" id="'
			+ prefix
			+ 'new_model_name-'
			+ rowCount
			+ '"/>'
			+ '</td></tr>'
			+ '<tr>'
			+ '<td class="tdLabel"><label class="label">'
			+ i18nObj.prop('label.req.on.reg')
			+ ':</label></td>'
			+ '<td><select name="isReqOnReg" id="'
			+ prefix
			+ 'm_brand_list-'
			+ rowCount
			+ '" class="option">'
			+ '<option value="NO">NO</option>'
			+ '<option value="YES">YES</option>'
			+ '</select>'
			+ '</td></tr>'
			+ '<tr><td class="tdLabel"><label class="label">'
			+ i18nObj.prop('label.bind.len.max.two.digit.allow')
			+ ':</label></td>'
			+ ' <td><input type="text" name="checkBindingLength" maxlength="39" value="" id="'
			+ prefix
			+ 'new_binding_length-'
			+ rowCount
			+ '"/>'
			+ '</td></tr>'
			+ '<tr><td class="tdLabel"><label for="new_model_desc" class="label">'
			+ i18nObj.prop('label.desc')
			+ ':</label></td>'
			+ '<td><textarea name="newModelDesc" cols="" rows="" id="'
			+ prefix
			+ 'new_model_desc-'
			+ rowCount
			+ '" style="height: 2cm;width: 5cm; border-color : black; " onkeypress="return (this.value.length &lt;= 100);"></textarea>'
			+ '</td></tr>' + '</table>';
	// alert(newdiv.innerHTML);
	cell.appendChild(newdiv);

	return true;
}

function addSpecTbl(tblId, invType) {
	var prefix = invType + "_";
	var tbl = _id.o(tblId);
	var rowCount = parseInt(tbl.rows.length, 10);

	var row = tbl.insertRow(rowCount);
	var cell = row.insertCell(0);
	var newdiv = document.createElement("div");
	newdiv.setAttribute("id", "new_spec_div" + rowCount);
	// alert(_id.o('cons_m_inv_list-0').innerHTML);
	newdiv.innerHTML = '<table border="0" cellpadding="4" cellspacing="0" >'
			+ '<tr>'
			+ '<td class="tdLabel"><label class="label">'
			+ i18nObj.prop('label.inv.name')
			+ ':</label></td>'
			+ '<td><select name="invIdForModSpec" id="cons_m_inv_list-'
			+ rowCount
			+ '" class="option">'
			+ (_id.o('cons_m_inv_list-0').innerHTML).replace('selected', '')
			+ '</select>'
			+ '</td></tr>'
			+ '<tr>'
			+ '<td class="tdLabel"><label class="label">'
			+ i18nObj.prop('label.spec.type')
			+ ':</label></td>'
			+ '<td><select name="specType" class="option" id="cons_spec_type-'
			+ rowCount
			+ '" onchange="changeSpec(this.id)">'
			+ '<option value="LENGTH">LENGTH</option>'
			+ '<option value="COUNT">COUNT</option>'
			+ '</select>'
			+ '</td></tr>'
			+ '<tr>'
			+ '<td class="tdLabel"><label class="label">'
			+ i18nObj.prop('label.spec.val')
			+ ':</label></td>'
			+ '<td><input type="text" name="specValue" maxlength="10" value="" id="cons_spec_value-'
			+ rowCount
			+ '"/>'
			+ '</td></tr>'
			+ '<tr>'
			+ '<td class="tdLabel"><label class="label">'
			+ i18nObj.prop('label.spec.unit')
			+ ':</label></td>'
			+ '<td><input type="text" name="specUnit" maxlength="19" value="" id="cons_spec_unit-'
			+ rowCount
			+ '"/>'
			+ '</td></tr>'
			+ '<tr>'
			+ '<td class="tdLabel"><label class="label">'
			+ i18nObj.prop('label.cost.per.unit')
			+ ':</label></td>'
			+ '<td><input type="text" name="costPerUnit" maxlength="10" value="" id="cons_cost_per_unit-'
			+ rowCount
			+ '"/>'
			+ '</td></tr>'
			+ '<tr>'
			+ '<td class="tdLabel"><label class="label">'
			+ i18nObj.prop('label.desc')
			+ ':</label></td>'
			+ '<td><textarea name="newModSpecDesc" cols="" rows="" id="cons_new_model_desc-'
			+ rowCount
			+ '" style="height: 2cm;width: 5cm; border-color : black; " onkeypress="return (this.value.length &lt;= 100);"></textarea></td>'
			+ '</tr>' + '</table>';
	// alert(newdiv.innerHTML);
	cell.appendChild(newdiv);

	return true;
}

function setBrandList(invValue, brandId) {
	var brandObj = _id.o(brandId);
	// alert(brandObj+"=="+brandId);
	brandObj.options.length = 1;
	// alert();
	var selObj = _id.o('m_brand_list');

	var text, value, count = 1;
	var brandValueArr;
	for ( var i = 1, l = selObj.length; i < l; i = i + 1) {
		value = selObj.options[i].value;
		brandValueArr = value.split("-");
		text = selObj.options[i].text;
		if (brandValueArr[1] == invValue || value == -1) {
			var opt = new Option(text, value);
			brandObj.options[count] = opt;
			count = count + 1;
		}

	}

}

parent.frames[0].Version['$Source: /rep/LMS_Mgmt/WebRoot/com/skilrock/lms/web/inventoryMgmt/backOffice/js/consNNonConsInv.js,v $'] = '$Id: consNNonConsInv.js,v 1.9 2016/10/31 09:47:02 neeraj Exp $';
