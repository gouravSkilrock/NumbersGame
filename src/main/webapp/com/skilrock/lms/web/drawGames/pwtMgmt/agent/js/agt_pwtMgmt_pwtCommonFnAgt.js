
var retUserIdArray = new Array();
function getRetailerList() {
	var text = _ajaxCall("getRetailerNameList.action?orgType=RETAILER");
	var firstArr = text.data.split(":");
	var retObj = _id.o("retOrgName");
	for (var i = 0; i < firstArr.length - 1; i++) {
		var retNameArray = firstArr[i].split("|");
		var opt = new Option(retNameArray[2]+"="+(retNameArray[0].toUpperCase()), retNameArray[1]);
		retObj.options[i + 1] = opt;
	}
}
function verifyOrg(orgDiv, orgName, msg) {
	if (_id.o(orgDiv).value != "-1") {
		var returnType = confirm(msg + " " + orgName);
		if (returnType) {
			_id.i("retOrgDiv", "");
			return true;
		} else {
			_id.o(orgDiv).selectedIndex = 0;
			_id.iniFocus(orgDiv);
			return false;
		}
	}
}
function getRetailerUserList(orgId) {
	if(orgId != -1){
		var text = _ajaxCall("getRetailerUserNameList.action?orgId=" + orgId);
		var firstArr = text.data.split(":");
		for (var i = 0; i < firstArr.length - 1; i++) {
			var retUserArr = firstArr[i].split("|");
			retUserIdArray[i] = retUserArr[1];
		}
	} 
}
function checkNum(obj) {
	if (_id.o("retOrgName").value == "-1") {
		document.getElementById("retOrgDiv").innerHTML = "<font color='red'>*PLEASE SELECT "+tierMap['RETAILER'].toUpperCase()+".</font>";
	} else {
		var e = event || evt;
		var _cC = e.which || e.keyCode;
		obj.style.color = "";
		/*if(obj.value.length == 4){
			checkUserId(obj);
		}*/
		if ((_cC >= 48 && _cC < 58) || _cC == 45 || _cC == 13 || _cC == 40 || _cC == 38 || (_cC >= 96 && _cC < 106 || _cC == 46 || _cC == 8 || _cC == 9 || _cC == 37 || _cC == 39)) {
			return true;
		}
	}
	return false;
}
function checkUserId(value) {
	var flag = 0;
	for (i = 0; i < retUserIdArray.length; i++) {
		if (value == retUserIdArray[i]) {
			flag = 1;
			break;
		}
	}
	if (flag == 0) {
		return true;
	} else {
		return false;
	}
}
function checkLen(obj) {
	if (obj.value.length >= 16) {
		moveToNextElement(obj);
	}
}
function moveToNextElement(obj) {
	totalElement = document.forms[0].elements.length;
	for (var i = 0; i < totalElement; i++) {
		var e = document.forms[0].elements[i];
		if (e.id == obj.id) {
			var nxtElement = document.forms[0].elements[i + 1];
			nxtElement.focus();
			break;
		}
	}
}
function checkDuplicate() {
	var elements = _id.n("ticketNum");
	var blankCount = 0;
	var invalidCount = 0;
	var dupCount = 0;
	var ret = 0;
	var allBlank = true;
	if (_id.o("retOrgName").value == -1) {
		_id.i("retOrgDiv", "*PLEASE SELECT "+tierMap['RETAILER'].toUpperCase(), "err");
		return false;
	}
	for (var i = 0; i < elements.length; i++) {
		if (elements[i].value.length == 16&&!(isNaN(elements[i].value))) {
			allBlank = false;
			if("NO" == isPwtClaimEvery){
				var chk = checkUserId(elements[i].value.substring(0, 4));
				if (chk) {
					elements[i].style.color = "#CC33FF";//Retailer Different Org
					invalidCount++;
				}
			}
			
			for (var j = i + 1; j < elements.length; j++) {
				if (elements[i].value == elements[j].value) {
					dupCount++;
					elements[j].style.color = "red";
				}
			}
		} else {
			if(elements[i].value != ""){
			allBlank = false;
			elements[i].style.color = "red";//Invalid Ticket
			invalidCount++;
			}
		}
		
		
	}
	if (allBlank) {
			_id.i('retOrgDiv','*Enter Atlest One Ticket No.','err');
			return false;
		}
	if (dupCount > 0) {
		_id.i('retOrgDiv','*Duplicate Tickets','err');
		ret++;
	}
	if (invalidCount > 0) {
		var msg = "*Invalid Tickets";
		if (invalidCount == 1) {
			msg = "*Invalid Ticket";
		}
		if (dupCount > 0) {
			document.getElementById("retOrgDiv").innerHTML = document.getElementById("retOrgDiv").innerHTML + "<br><font color='red'>" + msg;
		} else {
			_id.i('retOrgDiv',msg,'err');
		}
		ret++;
	}
	if (ret > 0) {
		return false;
	}
}
parent.frames[0].Version['$Source: /rep/LMS_Mgmt/WebRoot/com/skilrock/lms/web/drawGames/pwtMgmt/agent/js/agt_pwtMgmt_pwtCommonFnAgt.js,v $'] = '$Id: agt_pwtMgmt_pwtCommonFnAgt.js,v 1.2 2016/10/31 09:46:57 neeraj Exp $';

