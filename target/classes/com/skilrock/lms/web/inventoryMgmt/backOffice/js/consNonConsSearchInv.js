var i18Obj = parent.frames[0].i18Obj;
function _validateForm() {
	_id.i('downResult', "");
	_id.i('lastDown', "");
	var txtVal = _id.v('count_text');
	if (isNaN(txtVal) || txtVal.indexOf('.') != -1) {
		_id.i('err_div', i18Obj.prop('error.invalid.numeric.no.fr.count'), "e");
		return false;
	}
	return true;
}

var agtObj = null;
var resData = null;
var retObj = null;
function changeOwnerType(obj) {
	_id.i('downResult', "");
	_id.i('lastDown', "");
	var value = obj.value;
	if (value == "AGENT" || value == 'RETAILER') {

		var _resp = _ajaxCall(projectName
				+ "/com/skilrock/lms/web/userMgmt/getAllOrgNameList.action?orgType=AGENT");
		_respData = _resp.data;
		resData = _respData.split(":");
		retObj = _id.o("retOrgId");
		agtObj = _id.o("agentOrgId");
		agtObj.options.length = 1;
		showAgtOrg();
		retObj.innerHTML = "";
		retObj.innerHTML = "<option value='-1'>--Please Select--</option>";

		/*
		 * _id.blk('agt_label'); _id.blk('agt_org_list'); _id.non('ret_label');
		 * _id.non('ret_org_list');
		 */

	} else {
		// _id.i(targetDiv, "");
		document.getElementById("agentlistDiv").style.display = "none";
		document.getElementById("retlistDiv").style.display = "none";

		// _id.i("agentDiv", "");
	}
}

function invTypeShow(radVal) {
	_id.i('downResult', "");
	_id.i('lastDown', "");
	if ("NON_CONS" == radVal) {
		fillSelBox("-1", "inv_id", "nonCons_list", 'flag');
		_id.blk('brand');
		_id.blk('brand_label');
		_id.blk('model_label');
		_id.blk('model');
		_id.o('brand').options.length = 1;
		_id.o('model').options.length = 1;
	} else {
		fillSelBox("-1", "inv_id", "cons_list", 'flag');
		_id.non('brand');
		_id.non('brand_label');
		_id.blk('model_label');
		_id.blk('model');
		_id.o('model').options.length = 1;
	}
}

function fillBrandNModel(invVal) {
	_id.i('downResult', "");
	_id.i('lastDown', "");
	var radArr = _id.n('invType');
	for ( var i = 0, l = radArr.length; i < l; i += 1) {
		if (radArr[i].checked) {
			if (radArr[i].value == "NON_CONS") {
				fillSelBox(invVal, "brand", "brand_list");
				_id.o('model').options.length = 1;
			} else {
				fillSelBox(invVal, "model", "model_spec_list");
			}
		}
	}
}

function fillSelBox(invValue, updOptId, fetOptId, flag) {
	// alert(flag+" == "+invValue);
	var valArr = invValue.split("-");
	invValue = valArr[0];
	// alert(invValue);

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
		if (brandValueArr[1] == invValue || (invValue == "" && flag)) {
			var opt = new Option(text, brandValueArr[0]);
			brandObj.options[count] = opt;
			count = count + 1;
			// alert(text+" = "+ value);
		}

	}
	// alert(text+" = "+ value);
}
function showAgtOrg() {
	agtObj.innerHTML = "";
	agtObj.innerHTML = "<option value=''>--Please Select--</option>";
	for ( var i = 0; i < resData.length; i = i + 1) {
		var retNameArray = resData[i].split("|");
		if (retNameArray[0] != '') {
			var opt1 = new Option(retNameArray[0].toUpperCase(),
					retNameArray[1]);
			agtObj.options[i + 1] = opt1;
		}

	}

	document.getElementById("agentlistDiv").style.display = "block";
}

function getData(value) {

	var orgTypeVal = document.getElementById("owner_type").value;
	if (orgTypeVal == "RETAILER") {

		getRetailerList(
				projectName + '/com/skilrock/lms/web/userMgmt/fetchRetOrgList.action',
				'retlist', value);
	}
	var agentOrgId = document.getElementById("agentOrgId").value;
	if (agentOrgId.trim() == "") {
		retObj.innerHTML = "";
		retObj.innerHTML = "<option value='-1'>--Please Select--</option>";
	}

}
function getRetailerList(url, targetDiv, agentOrgId) {
	var changedUrl = url + '?orgId=' + agentOrgId;
	var orgType = _id.o('owner_type').value;
	// alert(changedUrl +" userType = "+orgType);
	if (orgType != 'RETAILER') {
		// _id.i(targetDiv, "");
		document.getElementById("retlistDiv").style.display = "none";

		return false;
	}
	// alert("retailer ");
	if (agentOrgId != '') {
		var _resp = _ajaxCall(changedUrl);
		_respData = _resp.data;
		resRetData = _respData.split(":");
		retObj = _id.o("retOrgId");
		retObj.options.length = 1;
		showRetOrg();
	} else {
		document.getElementById("retlistDiv").style.display = "none";

	}
}

function showRetOrg(seltype) {

	// alert(seltype);
	var orgTypeVal = document.getElementById("owner_type").value;
	if (orgTypeVal == 'RETAILER') {

		retObj.innerHTML = "";
		retObj.innerHTML = "<option value='-1'>--Please Select--</option>";
		for ( var i = 0; i < resRetData.length; i++) {
			var val = resRetData[i].split("|");
			if (val[0] != '') {
				var opt = new Option(val[0], val[1]);
				retObj.options[i + 1] = opt;
			}
		}

	}
	document.getElementById("retlistDiv").style.display = "block";

}

parent.frames[0].Version['$Source: /rep/LMS_Mgmt/WebRoot/com/skilrock/lms/web/inventoryMgmt/backOffice/js/consNonConsSearchInv.js,v $'] = '$Id: consNonConsSearchInv.js,v 1.3 2016/10/31 09:47:02 neeraj Exp $';