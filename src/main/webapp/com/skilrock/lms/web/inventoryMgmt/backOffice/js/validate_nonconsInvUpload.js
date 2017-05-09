	var i18nObj = parent.frames[0].i18Obj;
function _nonConsValidate() {

	var frmVal = new parent.frames[0].Validator("saveInv");
	// validate the retailer organization Entries
	frmVal.aV("invId", "dontselect=0", i18nObj.prop('error.select.inv.name'),
			"inv_err");
	frmVal.aV("brandsId", "dontselect=0", i18nObj
			.prop('error.select.brand.name'), "brand_err");
	frmVal.aV("modelId", "dontselect=0",
			i18nObj.prop('error.select.model.name'), "model_err");
	frmVal.aV("cost", "req", i18nObj.prop('error.enter.cost.per.unit'),
			"cost_err");
	frmVal.aV("cost", "decimal", i18nObj
			.prop('error.enter.correct.cost.per.unit'), "cost_err");

	var elementObj = _id.o('bind');
	if (elementObj.options.length > 1) {
		var bind_len = elementObj.options[1].text;
	}
	var serNoArr = _id.n('serNo');
	var invFlag = true;
	var textValue = "";

	if (invFlag && parseFloat(document.saveInv.cost.value) == "0") {
		parent.frames[1].document.getElementById("cost_err").innerHTML = "<font color = 'red'>"
				+ i18nObj.prop('error.provide.cost.val') + "</font>";
		invFlag = false;
	}

	// alert(serNoArr.length);
	var countArr = 0;
	for ( var i = 0, len = serNoArr.length; i < len; i++) {
		var arrValue = (serNoArr[i].value).replace(/\s/g, "");
		if (arrValue == "") {
			countArr++;
		}
	}
	// alert (countArr);
	var res = true;
	if (countArr != serNoArr.length
			|| document.getElementById("serNoFile").value.trim() != '') {
		for ( var i = 0, len = serNoArr.length; i < len; i++) {
			textValue = (serNoArr[i].value).replace(/\s/g, "");
			frmVal.aV(serNoArr[i].id, "regexpSerNo=[^A-Za-z0-9\\s/#-]", i18nObj
					.prop('error.enter.correct.sr'), "sr_err");
			frmVal.aV(serNoArr[i].id, "non_zero", i18nObj
					.prop('error.enter.correct.sr'), "sr_err");
			if (eval(textValue.length) > 0) {
				serNoArr[i].style.color = "grey";
				if (textValue.length < bind_len) {
					invFlag = false;
					frmVal.aV(serNoArr[i].id, "minlen=" + bind_len + "", i18nObj
							.prop('error.sr.must.have.atleast.size')
							+ bind_len + "", "sr_err");
				}
			}
		}
		if (!invFlag) {
			return (invFlag && document.error_disp_handler.isValid);
		}
	} else {
		invFlag = false;
		parent.frames[1].document.getElementById("sr_err").innerHTML = "<font color = 'red'><b>"
				+ i18nObj.prop('error.provide.atleast.one.sr.interface.file')
				+ "</b></font>";
		return (invFlag && document.error_disp_handler.isValid);
	}

	var dV = false;
	var serNoArr = _id.n('serNo');
	len = serNoArr.length;
	for ( var i = 0; i < len; i++) {

		if (serNoArr[i].value.length != 0) {
			var ival = serNoArr[i].value.trim();
			var ilen = serNoArr[i].value.length;
			if (bind_len > 0) {
				var strToMatch = ival.substring(ilen - bind_len, ilen);
			} else {
				var strToMatch = ival;
			}
			for (j = i + 1; j < len; j++) {
				var jval = serNoArr[j].value.trim();
				var jlen = serNoArr[j].value.length;
				if (jlen > 0) {
					if (bind_len > 0) {
						var strFromMatch = jval
								.substring(jlen - bind_len, jlen);
					} else {
						var strFromMatch = jval;
					}
					if (strFromMatch == strToMatch) {
						dV = true;
						serNoArr[j].style.color = "red";
						serNoArr[i].style.color = "red";
					}
				}
			}
		}
	}
	if (dV) {
		invFlag = false;
		parent.frames[1].document.getElementById("sr_err").innerHTML = "<font color = 'red'><b>"
				+ i18nObj.prop('error.duplicate.sr.found')
				+ " !!"
				+ "</b></font>";
		return (invFlag && document.error_disp_handler.isValid);
	}

	if (document.getElementById("invCode0")) {

		var countArr = 0;

		var invCodeArr = _id.n('invCode');
		len = invCodeArr.length;
		for ( var i = 0, len = invCodeArr.length; i < len; i++) {
			var arrValue = (invCodeArr[i].value).replace(/\s/g, "");
			if (arrValue == "") {
				countArr++;
			}
		}

		if (countArr != invCodeArr.length
				|| document.getElementById("serNoFile").value.trim() != '') {
			for ( var i = 0, len = invCodeArr.length; i < len; i++) {
				textValue = (invCodeArr[i].value).replace(/\s/g, "");
				frmVal.aV(invCodeArr[i].id, "regexpSerNo=[^A-Za-z0-9\\s/#-]",
						i18nObj.prop('error.enter.correct.val.fr.inv.code'),
						"sr_err");
				frmVal
						.aV(invCodeArr[i].id, "non_zero", i18nObj
								.prop('error.enter.correct.val.fr.inv.code'),
								"sr_err");
				if (eval(textValue.length) > 0) {
					invCodeArr[i].style.color = "grey";

				}
			}
			if (!invFlag) {
				return (invFlag && document.error_disp_handler.isValid);
			}
		} else {
			invFlag = false;
			parent.frames[1].document.getElementById("sr_err").innerHTML = "<font color = 'red'><b>"
					+ i18nObj
							.prop('error.provide.atleast.one.inv.code.interface.file')
					+ "</b></font>";
			return (invFlag && document.error_disp_handler.isValid);
		}

		for ( var i = 0; i < len; i++) {

			if (invCodeArr[i].value.length != 0) {

				var ival = invCodeArr[i].value.trim();

				for (j = i + 1; j < len; j++) {
					var jval = invCodeArr[j].value.trim();

					if (jval == ival) {
						dV = true;
						invCodeArr[j].style.color = "red";
						invCodeArr[i].style.color = "red";
					}

				}

			}

		}
	}

	if (dV) {
		invFlag = false;
		parent.frames[1].document.getElementById("sr_err").innerHTML = "<font color = 'red'><b>"
				+ i18nObj.prop('error.duplicate.inv.code.found')
				+ " !!"
				+ "</b></font>";
		return (invFlag && document.error_disp_handler.isValid);
	}

	var fileName = _id.v('serNoFile');
	fileName = fileName.replace(/\s/g, "");
	if (fileName != "") {
		invFlag = true;
	}
	return (invFlag && document.error_disp_handler.isValid);
}

function checkSerNo(objValue, strRegExp, strError, dispDiv) {
	var ret = true;
	var charpos = objValue.value.search(strRegExp);
	if (objValue.value.length > 0 && charpos >= 0) {
		if (!strError || strError.length == 0) {
			strError = strDefaultError;
		}// if
		_errMsg(strError, dispDiv);
		ret = false;
	}// if

	alert("final : " + ret);
	return ret;
}

function createTbl() {

	var tbl = '<table  border="1" align="center" cellpadding="2" cellspacing="0">';
	tbl = tbl + '<tr><th  colspan="8" align="center">'
			+ i18nObj.prop('label.sr.of.inv') + '</th>';
	var tr = '';
	var n = 2;
	m = 3, p = 4;
	for ( var i = 0; i < 5; i++) {
		/*
		 * n+=1; m+=1; if(n%4 == 0){ tbl= tbl+'<tr>'; }
		 */
		tbl = tbl + '<tr><td>' + i18nObj.prop('label.serial')
				+ '</td><td><input type="text" name="serNo" id="serNo' + i
				+ '" size="15" maxlength="50" value=""/></td><td>'
				+ i18nObj.prop('label.serial')
				+ '</td><td><input type="text" name="serNo" id="serNo' + n + i
				+ '" size="15" maxlength="50" value=""/></td><td>'
				+ i18nObj.prop('label.serial')
				+ '</td><td><input type="text" name="serNo" id="serNo' + m + i
				+ '" size="15" maxlength="50" value=""/></td><td>'
				+ i18nObj.prop('label.serial')
				+ '</td><td><input type="text" name="serNo" id="serNo' + p + i
				+ '" size="15" maxlength="50" value=""/></td> </tr>';
		/*
		 * if(m%4==0) { tbl= tbl+'</tr>'; }
		 */
	}
	tbl = tbl + '</table>'
	// alert(tbl);
	_id.i('dyn_tbl', tbl);
}

function hideCol(isCode) {

	if (isCode == 'Y') {

		var tbl = '<table  border="1" align="center" cellpadding="2" cellspacing="0">';
		tbl = tbl + '<tr><th  colspan="2" align="center">'+i18nObj.prop('label.sr.of.inv')+'</th><th colspan="2" align="center">'+i18nObj.prop('label.code.of.inv')+'</th><th  colspan="2" align="center">'+i18nObj.prop('label.sr.of.inv')+'</th><th colspan="2" align="center">'+i18nObj.prop('label.code.of.inv')+'</th></tr>';
		var tr = '';
		// var n=-1,m=-4;
		var n = 2;
		for ( var i = 0; i < 10; i++) {
			/*
			 * n+=1; m+=1; if(n%4 == 0){ tbl= tbl+'<tr>'; }
			 */
			tbl = tbl
					+ '<tr><td>'+i18nObj.prop('label.serial')+'</td><td><input type="text" name="serNo" id="serNo'
					+ i
					+ '" size="15" maxlength="50" value=""/></td> <td>'+i18nObj.prop('label.code')+'</td><td><input type="text" name="invCode" id="invCode'
					+ i
					+ '" size="15" maxlength="50" value=""/></td><td>'+i18nObj.prop('label.serial')+'</td><td><input type="text" name="serNo" id="serNo'
					+ n
					+ i
					+ '" size="15" maxlength="50" value=""/></td> <td>'+i18nObj.prop('label.code')+'</td><td><input type="text" name="invCode" id="invCode'
					+ n + i + '" size="15" maxlength="50" value=""/></td></tr>';
			/*
			 * if(m%4==0) { tbl= tbl+'</tr>'; }
			 */
		}
		tbl = tbl + '</table>'
		// alert(tbl);
		_id.i('dyn_tbl', tbl);

	} else {

		createTbl();

	}

}

function chageBrandList(invValue, updOptId, fetOptId, val) {

	var valArr = invValue.split("-");
	invValue = valArr[0];
	// alert(valArr[0]);

	var brandObj = _id.o(updOptId);
	brandObj.options.length = 1;
	var selObj = _id.o(fetOptId);
	var text, value, count = 1;
	var brandValueArr;
	for ( var i = 0, l = selObj.length; i < l; i = i + 1) {
		value = selObj.options[i].value;
		brandValueArr = value.split("-");
		text = selObj.options[i].text;
		// alert(text+" = "+ value);
		if (brandValueArr[1] == invValue || invValue == -1) {
			var opt = new Option(text, value);
			brandObj.options[count] = opt;

			if (value == val) {
				brandObj.options.selected = true;
			}
			count = count + 1;
		}
	}
}
function changeModelList(invValue, updOptId, fetOptId, val) {

	// alert("Hi"+invValue);
	var valArr = invValue.split("-");
	invValue = valArr[0];
	// alert(valArr[0]);

	var modelObj = _id.o(updOptId);
	var invCodeObj = _id.o("invCode_list");
	modelObj.options.length = 1;
	var selObj = _id.o(fetOptId);
	var text, value, count = 1;
	var modelValueArr;
	for ( var i = 0, l = selObj.length; i < l; i = i + 1) {
		value = selObj.options[i].value;
		modelValueArr = value.split("-");
		text = selObj.options[i].text;
		// alert("ho"+modelValueArr[0]+" "+invValue);
		if (modelValueArr[0] == invValue || invValue == -1) {
			if (invCodeObj != null && typeof invCodeObj != "undefined") {
				hideCol(invCodeObj.options[i].text);
			}

			var opt = new Option(text, value);
			modelObj.options[count] = opt;
			if (modelObj.options.length > 1) {
				modelObj.options[1].selected = true;
				_id.o('bindLen').value = modelObj.options[1].text;
			}
			/*
			 * if(value==val){ modelObj.options.selected=true; }
			 */
			count = count + 1;
		}
	}

	// alert(text+" = "+ value);
}

function resetValues() {

	document.getElementById("inv").selectedIndex = 0;
	document.getElementById("brand").selectedIndex = 0;
	document.getElementById("model").selectedIndex = 0;
	document.getElementById("termNumberId").value = "";

}

function clearContent(list, divId) {
	var obj = _id.o(list);
	document.getElementById(divId).innerHTML = '';
	document.getElementById("termNumberId").value = "";
}

function clearContentText(list, divId) {
	var obj = _id.o(list);
	document.getElementById(divId).innerHTML = '';
}
