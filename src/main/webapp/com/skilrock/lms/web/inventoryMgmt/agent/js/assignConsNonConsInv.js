var i18nObj = parent.frames[0].i18Obj;
function _validateFormItems() {
	if (_id.o("ret_org_list").value == -1) {
		_id.i("ron", i18nObj.prop('msg.pls.select') + tierMap['RETAILER']
				+ i18nObj.prop('msg.org.name'), "e");
		return false;
	} else
		_id.o("ron").innerHTML = "";
	if (_id.o("invNo").value == -1) {
		_id.i("trn", i18nObj.prop('error.select.inv'), "e");
		return false;
	} else
		_id.o("trn").innerHTML = "";
	return true;
}
function clearDiv() {
	_id.o("ron").innerHTML = "";
	_id.o("ret_err").innerHTML = "";
	_id.o("inv_err").innerHTML = "";
	_id.o("brand_err").innerHTML = "";
	_id.o("model_err").innerHTML = "";
	_id.o("trn").innerHTML = "";
}
function _validateDirInvReturnItems() {
	clearDiv();
	if (_id.o("agentOrgId").value == -1) {
		_id.i("ron", i18nObj.prop('msg.pls.select')+" " + tierMap['AGENT']
				+" "+ i18nObj.prop('msg.org.name'), "e");
		return false;
	} else
		_id.o("ron").innerHTML = "";
	
	if (_id.o("retOrgId").value == -1) {
		_id.i("ret_err", i18nObj.prop('msg.pls.select') +" "+ tierMap['RETAILER']
				+" "+ i18nObj.prop('msg.org.name'), "e");
		return false;
	} else
		_id.o("ret_err").innerHTML = "";
	
	if (_id.o("invName").value == -1) {
		_id.i("inv_err", i18nObj.prop('error.select.inv.name'), "e");
		return false;
	} else
		_id.o("inv_err").innerHTML = "";
	if (_id.o("brand").value == -1) {
		_id.i("brand_err", i18nObj.prop('error.select.brand.name'), "e");
		return false;
	} else
		_id.o("brand_err").innerHTML = "";
	if (_id.o("model").value == -1) {
		_id.i("model_err", i18nObj.prop('error.select.model.name'), "e");
		return false;
	} else
		_id.o("model_err").innerHTML = "";
	if (_id.o("invNo").value == -1) {
		_id.i("trn", i18nObj.prop('error.select.inv'), "e");
		return false;
	} else
		_id.o("trn").innerHTML = "";
	return true;
}

function _validateForm() {
	if (_id.o("ret_org_list").value == -1) {
		_id.i("ron", i18nObj.prop('msg.pls.select') + tierMap['RETAILER']
				+ i18nObj.prop('msg.org.name'), "e");
		return false;
	} else {
		_id.o("ron").innerHTML = "";
	}
	if (_id.o("invNo").value == -1) {
		_id.i("trn", i18nObj.prop('error.select.inv'), "e");
		return false;
	} else {
		_id.o("trn").innerHTML = "";
	}
	return true;
}

function getRetailerUserList() {
	var text = _ajaxCall(projectName
			+ "/com/skilrock/lms/web/userMgmt/getAllOrgNameList.action?orgType=RETAILER");
	var firstArr = text.data.split(":");
	var retObj = _id.o("ret_org_list");
	for ( var i = 0; i < firstArr.length - 1; i++) {
		var retNameArray = firstArr[i].split("|");
		var opt = new Option(retNameArray[0].toUpperCase(), retNameArray[1]);
		retObj.options[i + 1] = opt;
	}
	// document.getElementById("retlistDiv").style.display="block";
}

function getRetUserListForReassign() {
	var text = _ajaxCall(projectName
			+ "/com/skilrock/lms/web/inventoryMgmt/fetchRetList.action");
	var firstArr = text.data.split(":");
	var retObj = _id.o("ret_org_list");
	for ( var i = 0; i < firstArr.length - 1; i++) {
		var retNameArray = firstArr[i].split("|");
		var opt = new Option(retNameArray[0].toUpperCase(), retNameArray[1]);
		retObj.options[i + 1] = opt;
	}
	// document.getElementById("retlistDiv").style.display="block";
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
	modelObj.options.length = 1;
	var selObj = _id.o(fetOptId);
	var text, value, count = 1;
	var modelValueArr;
	for ( var i = 0, l = selObj.length; i < l; i = i + 1) {
		value = selObj.options[i].value;
		modelValueArr = value.split("-");
		text = selObj.options[i].text;
		// alert("ho"+modelValueArr[0]+" "+invValue);
		if (modelValueArr[2] == invValue || invValue == -1) {

			var opt = new Option(modelValueArr[1], modelValueArr[1]);
			modelObj.options[count] = opt;
			if (modelObj.options.length > 1) {
				modelObj.options[1].selected = true;
				// _id.o('bindLen').value=modelObj.options[1].text;
			}
			/*
			 * if(value==val){ modelObj.options.selected=true; }
			 */
			count = count + 1;
		}
	}

	// alert(text+" = "+ value);
}

function getInvList(retOrgId) {
	var text = _ajaxCall(projectName
			+ "/com/skilrock/lms/web/inventoryMgmt/getInventory.action?retOrgId="
			+ retOrgId + "&" + new Date().getTime() + parent.frames[0].sesId
			+ Math.random() * 1000000);
	var firstArr = text.data.split(";");
	var invObj = _id.o("inv_List");
	for ( var i = 0; i < firstArr.length - 1; i++) {
		// var invList = firstArr[i].split("-");
		var opt = new Option(firstArr[i], firstArr[i]);
		invObj.options[i + 1] = opt;
	}
	// document.getElementById("retlistDiv").style.display="block";
}

parent.frames[0].Version['$Source: /rep/LMS_Mgmt/WebRoot/com/skilrock/lms/web/inventoryMgmt/agent/js/assignConsNonConsInv.js,v $'] = '$Id: assignConsNonConsInv.js,v 1.3 2016/10/31 09:47:02 neeraj Exp $';
