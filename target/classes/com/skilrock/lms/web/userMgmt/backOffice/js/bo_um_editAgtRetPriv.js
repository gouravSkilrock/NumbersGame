var req;
var i18nObj = parent.frames[0].i18Obj;
function getOrgPriveledges(orgId) {

	if (orgId > 0) {
		var organizationType = _id.o('Org_type').value;
		var url = 'bo_um_editOrgPriv_FetchPriv.action?orgId=' + orgId
				+ '&organizationType=' + organizationType;
		document.getElementById('lodingImage').style.display = "block";
		_resp = _ajaxCall(url);
		if (_resp.result) {
			document.getElementById('lodingImage').style.display = "none";
			_id.i('result', _resp.data);
			document.getElementById("result").style.display = 'block';
		} else
			alert(i18nObj.prop('error.problem'));

	} else {
		_id.i("result", "");
	}

}

function validate() {
	if (_id.o('Org_type').value == "-1") {
		_id.i('orgType_e', i18nObj.prop('error.org.type.empty'));
		return false;
	} else
		_id.i('orgType_e', '');

	if (_id.o('agentOrgId').value <= 0) {
		_id.i('agtName_e', i18nObj.prop('msg.pls.select') + tierMap['AGENT']);
		return false;
	} else
		_id.i('agtName_e', '');

	var checkArray = document.editOrgPriv.privGroupName;
	var flag = false;
	for ( var i = 0; i < checkArray.length; i++) {
		if (checkArray[i].checked != false) {
			flag = true;
			break;
		}
	}
	if (flag == false) {
		_id.i('priv_e', i18nObj
				.prop('error.pls.assign.atleast.one.priv.edit.stat'));
	}
	return flag;
}

function selectAll(rowId, checkBoxId) {
	var checkAll;
	if (_id.o(checkBoxId).checked == true)
		checkAll = true;
	else
		checkAll = false;

	var rowCheckbox = _id.o(rowId).getElementsByTagName("input");
	var length = rowCheckbox.length;
	for ( var i = 0; i < length; i++) {
		if (rowCheckbox[i].type == 'checkbox')
			rowCheckbox[i].checked = checkAll;
	}
}

// get the agent list from database
var agtObj = null;
var resData = null;
var retObj = null
function retURL(targetDiv, orgType) {
	document.getElementById("agentlistDiv").style.display = 'none';
	document.getElementById("retlistDiv").style.display = 'none';
	document.getElementById("result").style.display = 'none';

	if (orgType == 'AGENT' || orgType == 'RETAILER') {
		var _resp = _ajaxCall('getAgentForEditPriv.action?');
		_respData = _resp.data;
		resData = _respData.split(":");
		retObj = _id.o("retOrgId");
		agtObj = _id.o("agentOrgId");
		agtObj.options.length = 1;
		showAgtOrg();
		retObj.innerHTML = "";
		retObj.innerHTML = "<option value='-1'>--"+i18nObj.prop('msg.pls.select')+"--</option>";
		// alert(ajaxCall.data);
		// _id.i(targetDiv,"&nbsp;&nbsp;"+ajaxCall.data);
		// _id.i("agentDiv",tierMap['AGENT']+" Name:");
	} else {
		_id.i(targetDiv, "");
		_id.i("agentDiv", "");
	}
}

var resRetData = null;
function getRetailerList(url, targetDiv, agentOrgId) {

	var changedUrl = url + '?agentOrgId=' + agentOrgId;
	var orgType = _id.o('Org_type').value;
	// alert(changedUrl +" userType = "+orgType);
	if (orgType != 'RETAILER') {
		_id.i(targetDiv, "");
		_id.i("retDiv", "");
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
		_id.i(targetDiv, "");
		_id.i("retDiv", "");
	}
}

function changeTab(interCol, privRow, noCol) {
	// alert("vaibhav");
	var i, interColName, privRowName;
	interColName = '';
	privRowName = '';
	for (i = 0; i < interCol.length - 1; i++)
		interColName = interColName + interCol.charAt(i);
	for (i = 0; i < privRow.length - 1; i++)
		privRowName = privRowName + privRow.charAt(i);
	// alert(privRowName);

	var check;
	check = true;
	if (document.getElementById(privRow).style.display == 'block') {
		check = false;
	}
	for (i = 1; i <= noCol; i++) {
		document.getElementById(interColName + i).className = "unselectedTab";
		document.getElementById(privRowName + i).style.display = 'none';
	}
	if (check == true) {
		document.getElementById(interCol).className = "selectedTab";
		document.getElementById(privRow).style.display = 'block';
	}
}
function showHideTab(interRow) {
	// alert("vaibhav");
	if (document.getElementById(interRow).style.display == 'none') {
		document.getElementById(interRow).style.display = 'block';
	} else {
		document.getElementById(interRow).style.display = 'none';
	}
}

// added by pavan/vaibhav

function setRolePriv(tableId, countId) {
	/*
	 * if(document.getElementById(id).checked==true)
	 * document.getElementById(countId).value=parseInt(document.getElementById(countId).value)+1;
	 * else{ if(document.getElementById(countId).value!=0)
	 * document.getElementById(countId).value=parseInt(document.getElementById(countId).value)-1; }
	 */

	var tag = document.getElementById(tableId).getElementsByTagName("input");
	var count = 0;
	for ( var i = 0; i < tag.length; i++) {
		if (tag[i].type == 'checkbox' && tag[i].checked == true
				&& tag[i].name == 'groupName') {
			count++;
		}
	}

	document.getElementById(countId).value = count
}

function getData(value) {

	var orgTypeVal = document.getElementById("Org_type").value;
	if (orgTypeVal == "RETAILER") {
		getRetailerList('getRetailerListForEditPriv.action', 'retlist', value);
	} else if (orgTypeVal == "AGENT") {
		getOrgPriveledges(value);

	}

}
function showRetOrg(seltype) {

	// alert(seltype);
	var orgTypeVal = document.getElementById("Org_type").value;
	if (orgTypeVal == 'RETAILER') {

		retObj.innerHTML = "";
		retObj.innerHTML = "<option value='-1'>--"+i18nObj.prop('msg.pls.select')+"--</option>";
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
function showAgtOrg(seltype) {

	// alert(seltype);
	var orgTypeVal = document.getElementById("Org_type").value;
	if (orgTypeVal != -1) {

		agtObj.innerHTML = "";
		agtObj.innerHTML = "<option value=''>--"+i18nObj.prop('msg.pls.select')+"--</option>";
		for ( var i = 0; i < resData.length; i = i + 1) {
			var retNameArray = resData[i].split("|");
			if (retNameArray[0] != '') {
				var opt1 = new Option(retNameArray[0].toUpperCase(),
						retNameArray[1]);
				agtObj.options[i + 1] = opt1;
			}

		}

	}
	document.getElementById("agentlistDiv").style.display = "block";
}

parent.frames[0].Version['$Source: /rep/LMS_Mgmt/WebRoot/com/skilrock/lms/web/userMgmt/backOffice/js/bo_um_editAgtRetPriv.js,v $'] = '$Id: bo_um_editAgtRetPriv.js,v 1.3 2016/10/31 09:46:23 neeraj Exp $';
