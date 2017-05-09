var i18nObj = parent.frames[0].i18Obj;

var terminalData = '';

$(document).ready(function() {
	getAgentList(path);
	$("#agentOrgId").change(function() {
			clearDiv();
			getRetList(this.value);

		});
	$("#inv").change(function() {
			clearDiv();
			chageBrandList(this.value, 'brand', 'brand_list', '-1');
			_id.o('model').options.length = 1;
			_id.o('invNo').options.length = 1;

		});
	$("#brand").change(function() {
			clearDiv();
			chageBrandList(this.value, 'model', 'model_list', '-1');
			_id.o('invNo').options.length = 1;

		});
	$("#model").change(function() {
			clearDiv();
			changeModelList(this.value, 'terminalId', 'terminal_list', '-1');
		});
});

function clearDiv() {
	$("#agentName_e").html("");
	$("#retOrgId_e").html("");
	$("#inv_err").html("");
	$("#brand_err").html("");
	$("#model_err").html("");
	$("#tid_err").html("");
}

function validateForm() {
	clearDiv();
	var agent = $("#agentOrgId").val();
	var ret = $("#retOrgId").val();
	var inv = $("#inv").val();
	var brand = $("#brand").val();
	var model = $("#model").val();
	var tid = $("#terminalId").val();

	if (agent == "-1") {
		$("#agentName_e").html(i18nObj.prop('error.agent.empty'));
		return false;
	}
	if (ret == "-1") {
		$("#retOrgId_e").html(i18nObj.prop('error.retailer.empty'));
		return false;
	}
	if (inv == "-1") {
		$("#inv_err").html(i18nObj.prop('error.select.inv.name'));
		return false;
	}
	if (brand == "-1") {
		$("#brand_err").html(i18nObj.prop('error.select.brand.name'));
		return false;
	}
	if (model == "-1") {
		$("#model_err").html(i18nObj.prop('error.select.model.name'));
		return false;
	}
	if (tid == "") {
		$("#tid_err").html(i18nObj.prop('error.select.inv'));
		return false;
	}
	if (!isValidTerminal(tid)) {
		$("#tid_err").html(i18nObj.prop('error.select.valid.inv'));
		return false;
	}
}

function isValidTerminal(val) {
	var isTermValid = false;
	var dataArr = terminalData.split(':');
	for ( var k = 0; k < dataArr.length; k++) {
		if (val == dataArr[k]) {
			isTermValid = true;
		}
	}
	return isTermValid;
}

function getAgentList() {
	var text = _ajaxCall(path
			+ "/com/skilrock/lms/web/userMgmt/getAgentNameList.action?orgType=AGENT");
	var firstArr = text.data.split(":");
	var agtObj = _id.o("agentOrgId");
	for ( var i = 0; i < firstArr.length - 1; i++) {
		var agtNameArray = firstArr[i].split("|");
		var opt = new Option(agtNameArray[0].toUpperCase(), agtNameArray[1]);
		agtObj.options[i + 1] = opt;
	}
}
function getRetList(agentOrgName) {
	var agent = $("#agentOrgId").val();
	if (agent == "-1") {
		$("#agentName_e").html(i18nObj.prop('error.agent.empty'));
		return false;
	}
	var text = _ajaxCall(path
			+ "/com/skilrock/lms/web/userMgmt/fetchRetOrgList.action?orgId="
			+ agentOrgName);
	var firstRetArr = text.data.split(":");
	var retObj = _id.o("retOrgId");
	retObj.innerHTML = "";
	retObj.innerHTML = "<option value='-1'>--"+i18nObj.prop('msg.pls.select')+"--</option>";

	for ( var i = 0; i < firstRetArr.length - 1; i = i + 1) {
		var retNameArray = firstRetArr[i].split("|");
		var opt = new Option(retNameArray[0].toUpperCase(), retNameArray[1]);
		retObj.options[i + 1] = opt;
	}
}

function chageBrandList(invValue, updOptId, fetOptId, val) {

	var valArr = invValue.split("-");
	invValue = valArr[0];
	var brandObj = _id.o(updOptId);
	brandObj.options.length = 1;
	var selObj = _id.o(fetOptId);
	var text, value, count = 1;
	var brandValueArr;
	for ( var i = 0, l = selObj.length; i < l; i = i + 1) {
		value = selObj.options[i].value;
		brandValueArr = value.split("-");
		text = selObj.options[i].text;
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
	var valArr = invValue.split("-");
	invValue = valArr[0];
	var modelObj = _id.o(updOptId);
	var selObj = _id.o(fetOptId);
	var text, value, count = 1;
	var modelValueArr;
	for ( var i = 0, l = selObj.length; i < l; i = i + 1) {
		value = selObj.options[i].value;
		modelValueArr = value.split("-");
		text = selObj.options[i].text;
		if (modelValueArr[2] == invValue || invValue == -1) {
			var opt = new Option(modelValueArr[1], modelValueArr[1]);
			terminalData += modelValueArr[1]+":";
			count = count + 1;
		}
	}
	var tidComplete = terminalData.split(':');
	AutoComplete_Create(updOptId, tidComplete);
}

parent.frames[0].Version['$Source: /rep/LMS_Mgmt/WebRoot/com/skilrock/lms/web/inventoryMgmt/backOffice/js/assignInvToRet.js,v $'] = '$Id: assignInvToRet.js,v 1.3 2016/10/31 09:47:02 neeraj Exp $';