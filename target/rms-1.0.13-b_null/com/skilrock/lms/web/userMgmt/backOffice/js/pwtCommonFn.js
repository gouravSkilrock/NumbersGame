function checkDuplicate(A, B) {
	var C = true, fileEmp = true;
	var D = _id.fo(A, B);
	for (i = 0, l = D.length; i < l; i++) {
		if (D[i].value != "") {
			C = false;
			for (j = i + 1; j < l; j++) {
				if ((D[i].value).replace(/-/g, '') == (D[j].value).replace(
						/-/g, '')) {
					_id.i('errorDiv', 'Please remove duplicate entries', 'e');
					_id.i('allEmpty', '');
					return true;
				}
			}
		}
	}
	_id.i('errorDiv', '');
	if (document.getElementById('uploadFile') != null) {
		fileEmp = _id.v('uploadFile', 'r');
	}
	if ((C && (typeof _id.o('allEmpty') != 'undefined')) && (fileEmp)) {
		_id.i('allEmpty', 'Please Enter One Field', 'e');
		return true;
	}
	_id.i('allEmpty', '');
	return false;
}
var ticketNbrLen;
var virnLen;
function checkNum(A) {
	var B = event || evt;
	var C = B.which || B.keyCode;
	if (isEqualToLenOfBookNum(A)) {
		if (C == 46 || C == 8 || C >= 37 && C < 41) {
			return true;
		} else {
			moveToNextElement(A);
			return false;
		}
	}
	if ((C >= 48 && C < 58)
			|| C == 45
			|| C == 13
			|| C == 40
			|| C == 38
			|| (C >= 96 && C < 106 || C == 46 || C == 8 || C == 9 || C == 189 || C == 109)) {
		return true;
	}
	return false;
}
function checkVirn(A) {
	var B = event || evt;
	var C = B.which || B.keyCode;
	if (isEqualToLenOfBookNum(A)) {
		if (C == 46 || C == 8 || C >= 37 && C < 41) {
			return true;
		} else {
			moveToNextElement(A);
			return false;
		}
	}
}
function verifyOrg(A, B, C) {
	if (_id.o(A).value != "-1") {
		var D = confirm(C + " " + B);
		if (D) {
			var E = document.getElementsByName('virnCode');
			E[0].focus();
			return true;
		} else {
			_id.o(A).selectedIndex = 0;
			_id.iniFocus(A);
			return false;
		}
	}
}
function _un(A) {
	if (typeof A == 'undefined') {
		_id.i('gamemessage', 'Please select the Game first', 'e');
		document.forms[0].gameNbr_Name.selectedIndex = 0;
		document.forms[0].gameNbr_Name.focus();
		return false;
	}
	return true;
}
function isEqualToLenOfBookNum(A) {
	var B = _id.v(A);
	if (A.match("virn")) {
		len = virnLen;
		if (!_un(len))
			return false;
		if (_id.v(A).length < len) {
			return false;
		}
	} else if (A.match("ticket")) {
		len = ticketNbrLen;
		if (!_un(len))
			return false;
		if (B.match("-")) {
			if (_id.v(A).length < len + 2) {
				return false;
			}
		} else {
			if (_id.v(A).length < len) {
				return false;
			}
		}
	}
	return true;
}
function moveToNextElement(A) {
	totalElement = document.forms[0].elements.length;
	for ( var B = 0; B < totalElement; B++) {
		var C = document.forms[0].elements[B];
		if (C.id == A) {
			if (B < totalElement) {
				var D = document.forms[0].elements[B + 1];
				if (D.type == "button" && B + 2 < totalElement) {
					D = document.forms[0].elements[B + 2];
				}
				if (D.type == "text")
					document.getElementById(D.id).focus();
				break;
			}
		}
	}
}
var selectedInd = 0;
var divRep = "";
var _loadImg = '<img src="' + path + '/LMSImages/images/loadingAjax.gif"/>';
function searchPwtResult(A) {
	if (A.match("tmpRcptDetail")) {
		divRep = "tmpReceiptdetail";
		_id.i("tmpReceiptdetail", 'Please Wait............' + _loadImg);
	} else if (A.match("getGameNbrFormat")) {
		divRep = "gameNbrFormat";
		_id.i("loadingDiv", _loadImg);
	} else {
		var B = _id.v("resultList").split("to");
		selectedInd = _id.o("resultList").selectedIndex;
		A = A + "?start =" + (B[0].replace(" ", "") - 1) + "&end="
				+ B[1].replace(" ", "");
		_id
				.i(
						"bottom",
						'<table width="684" border="1" cellpadding="3" cellspacing="0" bordercolor="#CCCCCC" align="center"><tr align="center"><td colspan="6"><b>Search Results</b></td><td nowrap align="right"><select disabled="true"><option value="">'
								+ _id.v("resultList")
								+ '</select>'
								+ _loadImg
								+ '</td></tr></table>');
		divRep = "";
	}
	_resp = _ajaxCall(A);
	if (_resp.result) {
		_respData = _resp.data;
		if (divRep == "tmpReceiptdetail") {
			_id.i("tmpReceiptdetail", _respData);
		} else if (divRep == "gameNbrFormat") {
			_id.o("gameNbrFormat").value = _respData;
			var B = _respData.split(":");
			ticketNbrLen = parseInt(B[0]) + parseInt(B[1]) + parseInt(B[2])
					+ parseInt(B[3]);
			virnLen = parseInt(B[5]);
			_id.i("loadingDiv", "");
		} else {
			_id.i("bottom", _respData);
			_id.o("resultList").selectedIndex = selectedInd;
		}
	}
}
function crtTab(A) {
	var B = 1, _tD = '<table border="0" width="30%" cellpadding="2" cellspacing="0"><tr><td colspan=2><div id="errorDiv"></div></td></tr>';
	var C = 'id="saveBOPwtTicketsData_">VIRN Code', tLbl = 'id="verifyTickets_">Ticket No.';
	for ( var D = 0; D < 11; D++) {
		_tD = _tD + '<tr>';
		for ( var E = 0; E < 4; E++) {
			var F = '<input type="text" id="virnCode' + B + '" name="virnCode" size="16" onkeydown="return checkVirn(this.id)" onkeyup="return checkVirn(this.id)"/>';
			var G = '<input type="text" id="ticketNbr' + B + '" name="ticketNumber" size="16" onkeydown="return checkNum(this.id)" onkeyup="return checkNum(this.id)"/></td>';
			_tD = _tD + '<td width="10%" nowrap="nowrap"><label '
					+ ((A == "virn") ? C : tLbl)
					+ '</label></td><td width="20%">';
			_tD = _tD + ((A == "virn") ? F : G);
			B++;
		}
		_tD = _tD + '</tr>';
	}
	_id.i((A == 'virn') ? '_virnDiv' : '_tktDiv', _tD + '</table>');
}
parent.frames[0].Version['$Source: /rep/LMS_Mgmt/WebRoot/com/skilrock/lms/web/userMgmt/backOffice/js/pwtCommonFn.js,v $'] = '$Id: pwtCommonFn.js,v 1.3 2016/10/31 09:46:23 neeraj Exp $';