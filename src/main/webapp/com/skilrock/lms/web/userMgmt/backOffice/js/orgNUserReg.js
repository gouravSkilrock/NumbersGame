var i18nObj = parent.frames[0].i18Obj;
function isST3Filled() {
	var invOrgMsg = i18nObj.prop('invalid.org.name');
	var frmVal = new parent.frames[0].Validator("na");

	// validate the retailer organization Entries
	if (document.getElementById("agentOrgName")) {
		frmVal.aV("agentOrgName", "dontselect=0", i18nObj.prop('msg.pls.select')
				+ tierMap["AGENT"] + i18nObj.prop('msg.org'), "agtDiv");
	}
	// var invOrgMsg = i18nObj.prop('invalid.org.name');

	frmVal.aV("orgName", "req", invOrgMsg, "org");
	frmVal.aV("orgName", "regexp=[^A-Za-z0-9àâäèéêëîïìôœùûüÿçÀÂÄÈÉÊËÎÏÔŒÙÛÜŸÇ\\s_.]", invOrgMsg, "org");

	if (!(isNaN(_id.o("org_name").value))) {
		_id.o("org").innerHTML = "<font color='red'>" + invOrgMsg + "</font>";
		return false;
	}
	frmVal.aV("addrLine1", "req", i18nObj.prop('msg.blank.addr.line') + "1",
			"addr1");
	frmVal.aV("addrLine1", "regexp=[^A-Za-z0-9àâäèéêëîïìôœùûüÿçÀÂÄÈÉÊËÎÏÔŒÙÛÜŸÇ\\s/,-]", i18nObj
			.prop('msg.inv.addr'), "addr1");
	frmVal.aV("addrLine2", "regexp=[^A-Za-z0-9àâäèéêëîïìôœùûüÿçÀÂÄÈÉÊËÎÏÔŒÙÛÜŸÇ\\s/,-]", i18nObj
			.prop('msg.inv.addr'), "addr2");
	frmVal.aV("city", "req", i18nObj.prop('msg.blank.city'), "orgcity");
	frmVal.aV("city", "regexp=[^A-Za-zàâäèéêëîïìôœùûüÿçÀÂÄÈÉÊËÎÏÔŒÙÛÜŸÇ\\s/-]", i18nObj.prop('msg.blank.city'),
			"orgcity");
	frmVal.aV("country", "dontselect=0", i18nObj.prop('msg.blank.country'),
			"orgcountry");
	frmVal.aV("state", "dontselect=0", i18nObj.prop('msg.blank.state'),
			"orgstate");

	if ($('#bank').val() !== undefined) {
		clearFieldErrors();
		if ($('#bank').val() == -1) {
			$('#bankDiv').html(i18nObj.prop('error.slct.bank'));
			return false;
			// frmVal.aV("bankId", "req", "Please Select Bank", "bankDiv");
		} else {
			var bankType = ($('#bank').val()).split(":")[1];
			$('#bankId').val(($('#bank').val()).split(":")[0]);
			if (bankType != "E-ZWICH") {
				if ($('#branch').val() == -1) {
					$('#branchDiv').html(i18nObj.prop('error.slct.branch'));
					return false;
					// frmVal.aV("branchId", "req", "Please Select Branch",
					// "branchDiv");
					// return false;
				}
			}
		}

		var regx = /^[A-Za-z0-9 _.-]+$/;
		var space = /\s/;
		var accnbr = $('#accountNbr').val();
		if (accnbr == '') {
			$('#accountDiv').html(i18nObj.prop('error.slct.acc'));
			return false;
		} else if (!regx.test(accnbr) || space.test(accnbr)) {
			$('#accountDiv').html(i18nObj.prop('error.no.spcl.char'));
			return false;
		}

		// frmVal.aV("accountNbr", "req", "Please Enter Account Nbr",
		// "accountDiv");
		// frmVal.aV("accountNbr", "regexp=/^[A-Za-z0-9 _.-]+$/", "No Special
		// Char Allowed", "accountDiv");
		// frmVal.aV("accountNbr", "regexp=/\s/", "No White Space Allowed",
		// "accountDiv");

	}

	/*
	 * var regx = /^[A-Za-z0-9 _.-]+$/; var space = /\s/; var accnbr =
	 * $('#accountNbr').val(); if(accnbr == '') { frmVal.aV("accountNbr", "req",
	 * "Please Select account Nbr", "accountDiv"); }else if(!regx.test(accnbr) ||
	 * space.test(accnbr)){ $('#accountDiv').html("No Special Char Allowed"); }
	 */

	if (document.getElementById("area") != 'undefined'
			&& document.getElementById("area") != null) {
		frmVal.aV("area", "dontselect=0", i18nObj.prop('error.slct.zone'),
				"area_e");

	}
	if (document.getElementById("division") != 'undefined'
			&& document.getElementById("division") != null) {
		frmVal.aV("division", "dontselect=0", i18nObj
				.prop('error.slct.division'), "division_e");

	}

	// frmVal.aV("pin","req","Please Enter Pin","orgpin");
	// frmVal.aV("pin","numeric","Please Enter Correct Value For Pin","orgpin");
	frmVal.aV("creditLimit", "req", i18nObj.prop('error.creditlimit.empty'),
			"orglimit");
	frmVal.aV("creditLimit", "decimal", i18nObj.prop('error.incorrect.limit'),
			"orglimit");
	frmVal.aV("security", "req", i18nObj.prop('error.securitydeposit.empty'),
			"orgsecurity");
	frmVal.aV("security", "decimal", i18nObj.prop('error.incorrect.security'),
			"orgsecurity");
	frmVal.aV("vatRegNo", "regexp=[^A-Za-z0-9àâäèéêëîïìôœùûüÿçÀÂÄÈÉÊËÎÏÔŒÙÛÜŸÇ\\s/,-]", i18nObj
			.prop('error.vat.reg'), "vatReg");

	// validate the retailer user Entries
	frmVal.aV("firstName", "req", i18nObj.prop('error.fname.empty'), "fname");
	frmVal.aV("firstName", "alpha", i18nObj.prop('error.name.empty'), "fname");
	frmVal.aV("lastName", "req", i18nObj.prop('error.lname.empty'), "lname");
	frmVal.aV("lastName", "alpha", i18nObj.prop('error.lname.empty') + "("
			+ i18nObj.prop('error.spcl.char') + ")", "lname");
	frmVal.aV("userName", "req", i18nObj.prop('error.username.empty'), "user");
	frmVal.aV("userName", "regexp=[^A-Za-z0-9\\_]", i18nObj.prop('error.invalid.val'), "user");
	frmVal.aV("email", "req", i18nObj.prop('error.email.empty'), "Email");
	frmVal.aV("email", "email", i18nObj.prop('error.email.empty'), "Email");

	// frmVal.aV("idType","dontselect=0","Please Select
	// idType","id_type_error");
	if (document.getElementById("country_deployed") != 'undefined'
		&& document.getElementById("country_deployed") != null && document.getElementById("country_deployed").value != 'BENIN') {
	frmVal.aV("idNo", "req", i18nObj.prop('error.id.empty'), "id_no_error");
		frmVal.aV("idNo", "regexp=[^A-Za-z0-9àâäèéêëîïìôœùûüÿçÀÂÄÈÉÊËÎÏÔŒÙÛÜŸÇ\\s/,-]", i18nObj
			.prop('error.id.empty'), "id_no_error");
	}else{
		if($("#id_no").val().trim().length!=0){
			frmVal.aV("idNo", "minlen=13",  i18nObj.prop('error.id.empty'), "id_no_error");
			frmVal.aV("idNo", "regexp=[^0-9\\s/,-]", i18nObj.prop('error.id.empty'), "id_no_error");
		}
	}
	frmVal.aV("mobile", "req", i18nObj.prop('error.mobno.empty'), "Mobile");
	frmVal.aV("mobile", "numeric", i18nObj.prop('error.mobno.empty'), "Mobile");

	frmVal.aV("secQues", "dontselect=0", i18nObj.prop('error.secretque.empty'),
			"sQues");
	frmVal.aV("secAns", "req", i18nObj.prop('error.secretans.empty'), "sAns");
	frmVal.aV("secAns", "regexp=[^A-Za-z0-9àâäèéêëîïìôœùûüÿçÀÂÄÈÉÊËÎÏÔŒÙÛÜŸÇ\\_s@#$%/&*]", i18nObj
			.prop('error.secretans.empty'), "sAns");

	// frmVal.aV("serviceData","selone_chk","Please Select Minimum One
	// Service","serviceDivErr");

	// alert(document.error_disp_handler.isValid);
	if (document.getElementById("terminalId")) {
		// alert('ffggggggggg' + document.getElementById("terminalId").value);
		// alert('hello999999999911111111111111111');
		frmVal.aV("terminalId", "req", i18nObj.prop('error.enter.tid'),
				"termDiv");
		// alert('hello11117777');
		if (!isValidTerminal(document.getElementById("terminalId").value)) {
			_id.i('termDiv', i18nObj.prop('error.enter.tid'), 'e');
			return false;
		}
	}
	if (document.getElementById("sim1")) {

		var simModelName = document.getElementsByName("simModelName");
		var simNbrs = document.getElementsByName("sim");
		var isOneSelected = false;

		for ( var j = 0; j < simModelName.length; j++) {

			var simVal = simModelName[j].options[simModelName[j].selectedIndex].value;
			if (simVal != "-1") {
				isOneSelected = true;
				for ( var i = 0; i < simModelName.length; i++) {

					if (i != j
							&& simModelName[i].options[simModelName[i].selectedIndex].value
									.trim() == simVal.trim()) {

						_id.i('simDiv',
								i18nObj.prop('error.mul.sim.same.model'), 'e');
						return false;
					}

				}

				if (!isValidInv(simNbrs[j].value, simNbrs[j].id)) {
					_id.i('simDiv', i18nObj.prop('error.enter.simid'), 'e');
					return false;
				}

			}

		}

		if (!isOneSelected) {
			_id.i('simDiv',
					i18nObj.prop('error.assign.atleast.one.sim') + '!! ', 'e');
			return false;

		} else {
			_id.o('simDiv').innerHTML = "";

		}

	}
	return document.error_disp_handler.isValid;
}

function checkAvlOrgUser() {
	if (_id.v("org_name") == "") {
		_id.i("org", i18nObj.prop('error.enter.org'), "e");
		return false;
	}
	if (_id.v("user_name") == "") {
		_id.i("user", i18nObj.prop('error.username.empty'), "e");
		return false;
	}
	url = "bo_um_agentReg_orgNuserAvailability.action?orgName="
			+ _id.v("org_name") + "& userName=" + _id.v("user_name");
	// alert(url);
	_resp = _ajaxCall(url);
	if (_resp.result && _resp.data != "") {
		var _respData = _resp.data;
		// alert(_respData+" length "+_respData.split(",").length);
		var data = _respData.split(",");
		for ( var i = 0; i < data.length; i++) {
			var keyValue = data[i].split("=");
			// alert(keyValue[0]+keyValue[1])
			if (keyValue[0] == "orgError") {
				if (keyValue[1] == "A") {
					_id.o("org").innerHTML = "<font color='green'>"
							+ i18nObj.prop('msg.org.avail') + " !!</font>";
				} else {
					_id.i("org", keyValue[1], "e");
				}
			} else {
				if (keyValue[0] == " userError") {
					if (keyValue[1] == "A") {
						_id.o("user").innerHTML = "<font color='green'>"
								+ i18nObj.prop('msg.username.avail')
								+ "!!</font>";
					} else {
						_id.i("user", keyValue[1], "e");
					}
				}
			}
		}
	} else {
		_id.o("org").innerHTML = "<font color='green'>"
				+ i18nObj.prop('msg.org.avail') + " !!</font>";
		_id.o("user").innerHTML = "<font color='green'>"
				+ i18nObj.prop('msg.username.avail') + "!!</font>";
	}
}
function getEmailPrivList(url, divId) {
	// alert(url);
	var _resp = _ajaxCall(url);
	if (_resp.result) {
		var _respData = _resp.data;
		var emailPrivList = _respData.split(","), table = "<table><tr>", idAndTitle = "";
		for ( var i = 0, len = emailPrivList.length; i < len; i++) {
			idAndTitle = emailPrivList[i].split("=");
			var tr = "<td><input type=\"checkbox\" name=\"emailPrivId\" value=\""
					+ idAndTitle[0].replace(" ", "")
					+ "\"/>"
					+ "<label class=\"checkboxLabel\">"
					+ idAndTitle[1]
					+ "</label></td>";
			table = table + tr;
		}
		table = table + "</tr></table>";
		_id.i(divId, table);
	} else {
		_id.i(divId, "Error");
	}
}
function fetchScrapNOrgLimits(url) {
	var scrapValue;
	var dataArr;
	var _resp = _ajaxCall(url);
	if (_resp.result) {
		var _respData = _resp.data;
		// alert(_respData);
		dataArr = _respData.split("-");
		// alert(dataArr.length+" = "+dataArr);
		scrapValue = dataArr[0];
		// alert(scrapValue);
		if (scrapValue != "YES") {
			var autoScrap = _id.o("auto_scrap");
			autoScrap.selectedIndex = 0;
			autoScrap.disabled = true;
		}
		_id.o("verLimit").value = dataArr[1];
		_id.o("appLimit").value = dataArr[2];
		_id.o("payLimit").value = dataArr[3];
		_id.o("scrapLimit").value = dataArr[4];
	}
}
function setStatus(id, val) {
	var divId = id.replace("chk", "div");
	if (_id.o(id).checked == true) {
		_id.o(divId).value = val;
	} else {
		val = val.replace("ACTIVE", "INACTIVE");
		_id.o(divId).value = val;
	}
}

var terminalData = '';
var invRspDatasim1 = '';
var invRspDatasim2 = '';
var invRspDatasim3 = '';
function fillTerminalIds(modelName, id) {
	_id.o(id).value = '';
	AutoComplete_Create(id, '');
	if (modelName != '') {
		if (document.getElementById("country_deployed").value == 'BENIN') {
			_resp = _ajaxCall('fetchTerminalSerialNos.action?modelName=' + modelName);
		} else {
		_resp = _ajaxCall('fetchTerminalSerialNos.action?agentOrgId='
				+ _id.o('agentOrgName').value + '&modelName=' + modelName);
		}
		terminalData = _resp.data;
		var data = _resp.data.split(':');
		AutoComplete_Create(id, data);
	}
}
function fillInvIds(modelName, id) {
	_id.o(id).value = '';
	AutoComplete_Create(id, '');
	if (modelName != '') {
		_resp = _ajaxCall('fetchTerminalSerialNos.action?agentOrgId='
				+ _id.o('agentOrgName').value + '&modelName=' + modelName);
		if (id == 'sim1') {
			invRspDatasim1 = _resp.data;
		} else if (id == 'sim2') {
			invRspDatasim2 = _resp.data;
		} else {
			invRspDatasim3 = _resp.data;

		}

		var invdata = _resp.data.split(':');
		AutoComplete_Create(id, invdata);
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
function isValidInv(val, id) {
	var isTermValid = false;
	var dataArr = null;
	if (id == 'sim1') {
		var dataArr = invRspDatasim1.split(':');
	} else if (id == 'sim2') {
		var dataArr = invRspDatasim2.split(':');
	} else {
		var dataArr = invRspDatasim3.split(':');

	}

	for ( var k = 0; k < dataArr.length; k++) {
		if (val == dataArr[k]) {
			isTermValid = true;
		}
	}
	return isTermValid;
}
function getAgentList() {
	var text = _ajaxCall("getAgentNameList.action?orgType=AGENT");
	var firstArr = text.data.split(":");
	var retObj = _id.o("agentOrgName");
	for ( var i = 0; i < firstArr.length - 1; i++) {
		var retNameArray = firstArr[i].split("|");
		var opt = new Option(retNameArray[0].toUpperCase(), retNameArray[1]);
		retObj.options[i + 1] = opt;
	}
}

function getRetRegForm(id) {

	_id.finiFocus('na', 'orgName');// alert();
	// if(_id.o('termId'))
	// _ajaxCall('fetchTerminalIds.action?agentOrgId='+_id.o(id).value,'termId');
	_id.o('agtOrgId').value = _id.o(id).value;
}
function fetchCityList() {
	var country = _id.o('country').value;
	var state = _id.o('plrState').value;
	_ajaxCall("getCity.action?country=" + country + "&state=" + state,
			'charactersCity');
}
function selectState() {
	_id.o('plrState').selectedIndex = 1;
	fetchCityList();
}

function fetchCity() {
	_id.o('plrCity').selectedIndex = 1;
	fetchCityCode();
	document.getElementById('plrCity').selectedIndex = 1;
	if (typeof $('#area').val() !== 'undefined') {
		fetchAreaList();
	}

}
function fetchCityCode() {
	var city = _id.o('plrCity').value;
	var _res = _ajaxCall('getCityCode.action?city=' + city);
	_id.o('cityCode').value = _res.data;
}

function fillModelNames(agentOrgId) {
	if (document.getElementById("terminalId")) {
		_id.o('terminalId').value = '';
		if (document.getElementById("country_deployed").value != 'BENIN') {
			_resp = _ajaxCall('fetchTerminalModelNames.action?agentOrgId=' + _id
					.o(agentOrgId).value);
		} else {
			var orgId = null;
			_resp = _ajaxCall('fetchTerminalModelNames.action');
		}
		var dataArr = _resp.data.split(':');
		var modelId = document.getElementsByName("modelName");

		if (dataArr[0].trim() != "") {

			for ( var j = 0; j < modelId.length; j++) {

				modelId[j].options.length = 1;

				for ( var i = 0; i < dataArr.length; i++) {
					modelId[j].add(new Option(dataArr[i], dataArr[i], null));
				}
			}

		}

	}

	if (document.getElementById("sim1")) {
		_resp = _ajaxCall('fetchInvModelNames.action?agentOrgId=' + _id
				.o(agentOrgId).value);
		var dataArr = _resp.data.split(':');
		var simModelName = document.getElementsByName("simModelName");
		if (dataArr[0].trim() != "") {

			for ( var j = 0; j < simModelName.length; j++) {

				simModelName[j].options.length = 1;

				for ( var i = 0; i < dataArr.length; i++) {
					simModelName[j]
							.add(new Option(dataArr[i], dataArr[i], null));
				}
			}

		}

	}
}

function fetchAreaList() {
	if (typeof $('#area').val() !== 'undefined') {
		getAreaListWithCode(_id.o('country').value, _id.o('plrState').value,
				_id.o('plrCity').value);
		if (document.getElementById('area').length > 1) {
			document.getElementById('area').selectedIndex = 1;
		}

		fetchDivisonList();

	}
}

function getAreaListWithCode(countryCode, stateCode, cityCode) {

	$('#area').html("<option value='-1'>--Please Select--</option>");
	if (countryCode == "-1" || stateCode == "-1") {

		$('#area').val("-1");
	} else {

		var url = path
				+ "/com/skilrock/lms/web/userMgmt/getAreaListWithCode.action?country="
				+ countryCode + "&state=" + stateCode + "&city=" + cityCode;
		$.get(url, function(data, status) {
			fillAreaList(data, status);
		}

		);

	}

}

function fillAreaList(data, status) {

	if (data != "" && status == "success") {

		var options = '';
		$('#area').append($(options));
		var list = data.split("|");
		// alert("gameArr = "+gameArr.length+" data = "+gameArr);
		for ( var i = 0; i < list.length - 1; i = i + 1) {
			var listArr = list[i].split(":");
			var gkey = listArr[0];
			var dvalue = listArr[1];
			options += '<option value="' + gkey + '">' + dvalue + '</option>';
		}
		$('#area').append($(options));
		// $('#area').val(list[0].split("|")[0]);
		$('#area').val("-1");

	}
}
function fetchDivisonList() {

	if (typeof $('#division').val() !== 'undefined') {
		getDivisionList(_id.o('country').value, _id.o('plrState').value, _id
				.o('plrCity').value, _id.o('area').value)
	}

}
function getDivisionList(countryCode, stateCode, cityCode, areaCode) {

	$('#division').html("<option value='-1'>--Please Select--</option>");
	if (countryCode == "-1" || stateCode == "-1" || cityCode == "-1"
			|| areaCode == "-1") {

		$('#division').val("-1");
	} else {
		var url = path
				+ "/com/skilrock/lms/web/userMgmt/getDivisionListWithCode.action?country="
				+ countryCode + "&state=" + stateCode + "&city=" + cityCode
				+ "&area=" + areaCode;
		$.get(url, function(data, status) {

			if (data != "" && status == "success") {

				var options = '';
				$('#division').append($(options));
				var list = data.split("|");
				// alert("gameArr = "+gameArr.length+" data = "+gameArr);
				for ( var i = 0; i < list.length - 1; i = i + 1) {
					var listArr = list[i].split(":");
					var gkey = listArr[0];
					var dvalue = listArr[1];
					options += '<option value="' + gkey + '">' + dvalue
							+ '</option>';
				}
				$('#division').append($(options));
				$('#division').val(list[0].split("|")[0]);
				$('#division').val("-1");

			}

		});

	}
}

// ADDED FOR BANK DETAILS

$(document).ready(function() {
	// getAgentList();
		var url = projectName
				+ "/com/skilrock/lms/web/userMgmt/getAgentBankList.action?"
				+ new Date().getTime() + parent.frames[0].sesId + Math.random()
				* 1000000;
		$.get(url, function(data, status) {

			fillDropDownList(data, status, "bank");
		}

		);

		$('#bank').change(function() {
			clearDiv();
			$('#req').show();
			if ($('#bank').val() != -1) {
				var bankType = ($('#bank').val()).split(":")[1];
				$('#bankId').val(($('#bank').val()).split(":")[0]);
				if (bankType == "E-ZWICH") {
					$('#branch').attr("disabled", "disabled");
					$('#req').hide();
				} else {
					$('#branch').removeAttr('disabled');
					$('#req').show();
					getBranchList(($('#bank').val()).split(":")[0]);
				}

			}

		});
		$('#branch').change(function() {

			if ($('#branch').val() != -1) {
				$('#branchId').val(($('#branch').val()).split(":")[0]);
				$('#sortCode').val(($('#branch').val()).split(":")[1]);

			}

		});
	});

function getBranchList(bankId) {

	var url = projectName
			+ "/com/skilrock/lms/web/userMgmt/getAgentBranchList.action?bankId="
			+ bankId + "&" + new Date().getTime() + parent.frames[0].sesId
			+ Math.random() * 1000000;
	$.get(url, function(data, status) {
		fillDropDownList(data, status, "branch");
	});

}
function fillDropDownList(data, status, elementId) {
	if (data != "" && status == "success") {

		var options = '';
		$('#' + elementId).append($(options));
		var list = data.split("|");
		for ( var i = 0; i < list.length - 1; i = i + 1) {
			var listArr = list[i].split(":");
			var gkey = listArr[0] + ":" + listArr[1];
			var dvalue = listArr[2];
			options += '<option value="' + gkey + '">' + dvalue + '</option>';
		}
		$('#' + elementId).append($(options));

	}
}
function clearDiv() {
	$('#branch')[0].options.length = 1;
	$('#sortCode').val('');
	$('#accountNbr').val('');
}
function clearFieldErrors() {
	$('#bankDiv').html(' ');
	$('#branchDiv').html(' ');
	$('#accountDiv').html(' ');

}

parent.frames[0].Version["$Source: /rep/LMS_Mgmt/WebRoot/com/skilrock/lms/web/userMgmt/backOffice/js/orgNUserReg.js,v $"] = "$Id: orgNUserReg.js,v 1.6 2016/10/31 09:46:23 neeraj Exp $";
