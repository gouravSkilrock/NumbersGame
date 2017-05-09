
var mainDiv;
var req;
var which;
function retrieveURL(url, div) {
	mainDiv = div;
	_resp  = _ajaxCall(url);
	if(_resp.result){
		document.getElementById(mainDiv).innerHTML = _resp.data;
	}else{
		alert("Problem:------");
	}
}

function IsNumeric(name) {
	var value1 = name;
	if (isNaN(document.getElementById(name).value)) {
		alert("Enter Correct Value For " + name);
		document.getElementById(name).focus();
		return true;
	}
}
function isSelect() {
	if (document.na.country.value == -1) {
		alert("Please Select the Country");
		document.na.country.focus();
		return false;
	} else {
		if (document.na.state.value == -1) {
			alert("Please Select the State");
			document.na.state.focus();
			return false;
		}
	}
}
function initialFocus() {
	document.na.orgName.focus();
	if (document.na.country.value != -1) {
		retrieveURL("getState.action?country=" + document.na.country.value, "characters");
	}
}
function isST3Filled() {
	var isInValid = false;
	var alphaExp = /[a-zA-Z]/;
	if (document.na.orgName.value == "") {
		isInValid = true;
		document.getElementById("org").innerHTML = "<font color = 'red'>Please Enter Company Name</font>";
	} else {
		if (document.na.orgName.value.match(alphaExp)) {
			document.getElementById("org").innerHTML = "";
		} else {
			document.getElementById("org").innerHTML = "<font color = 'red'>Please Enter Valid Company Name</font>";
			isInValid = true;
		}
	}
	if (document.na.addrLine1.value == "") {
		document.getElementById("addr1").innerHTML = "<font color = 'red'>Please Enter Address Line1</font>";
		isInValid = true;
	} else {
		if (document.na.addrLine1.value.match(alphaExp)) {
			document.getElementById("addr1").innerHTML = "";
		} else {
			document.getElementById("addr1").innerHTML = "<font color = 'red'>Please Enter Valid Address</font>";
			isInValid = true;
		}
	}
	if (document.na.city.value == "") {
		document.getElementById("orgcity").innerHTML = "<font color = 'red'>Please Enter City</font>";
		isInValid = true;
	} else {
		if (document.na.city.value.match(alphaExp)) {
			document.getElementById("orgcity").innerHTML = "";
		} else {
			document.getElementById("orgcity").innerHTML = "<font color = 'red'>Please Enter Valid City Name</font>";
			isInValid = true;
		}
	}
	if (document.na.country.value == -1) {
		document.getElementById("orgcountry").innerHTML = "<font color = 'red'>Please Select Country</font>";
		isInValid = true;
	} else {
		document.getElementById("orgcountry").innerHTML = "";
		if (document.na.state.value == -1) {
			document.getElementById("orgstate").innerHTML = "<font color = 'red'>Please Select State</font>";
			isInValid = true;
		} else {
			document.getElementById("orgstate").innerHTML = "";
		}
	}
	if (document.na.pin.value == "") {
		document.getElementById("orgpin").innerHTML = "<font color = 'red'>Please Enter Pin</font>";
		isInValid = true;
	} else {
		var numericExpression = /^[0-9]+$/;
		if (!document.na.pin.value.match(numericExpression)) {
			document.getElementById("orgpin").innerHTML = "<font color = 'red'>Please Enter Correct Value For Pin</font>";
			isInValid = true;
		} else {
			document.getElementById("orgpin").innerHTML = "";
		}
	}
	if (document.na.creditLimit.value == "") {
		document.getElementById("orglimit").innerHTML = "<font color = 'red'>Please Enter Limit</font>";
		isInValid = true;
	} else {
		var numericExpression = /^[0-9.]+$/;
		if (!document.na.creditLimit.value.match(numericExpression)) {
			document.getElementById("orglimit").innerHTML = "<font color = 'red'>Please Enter Correct Value For Limit</font>";
			isInValid = true;
		} else {
			document.getElementById("orglimit").innerHTML = "";
		}
	}
	if (document.na.security.value == "") {
		document.getElementById("orgsecurity").innerHTML = "<font color = 'red'>Please Enter Security</font>";
		isInValid = true;
	} else {
		var numericExpression = /^[0-9.]+$/;
		if (!document.na.security.value.match(numericExpression)) {
			document.getElementById("orgsecurity").innerHTML = "<font color = 'red'>Please Enter Correct Value For Security</font>";
			isInValid = true;
		} else {
			if (parseInt(document.na.security.value) == "0") {
				document.getElementById("orgsecurity").innerHTML = "<font color = 'red'>Please Enter Correct Value For Security</font>";
				isInValid = true;
			} else {
				document.getElementById("orgsecurity").innerHTML = "";
			}
		}
	}
	if (isInValid) {
		return false;
	}
	return true;
}
function isValuesValid() {
	var isInValid = false;
	var alphaExp = /[a-zA-Z]/;
	if (document.orgDetails.orgPin.value == "") {
		document.getElementById("pin").innerHTML = "<font color = 'red'>Please Enter Pin</font>";
		isInValid = true;
	} else {
		if (isNaN(document.orgDetails.orgPin.value)) {
			document.getElementById("pin").innerHTML = "<font color = 'red'>Please Enter Correct Value For Pin</font>";
			isInValid = true;
		} else {
			document.getElementById("pin").innerHTML = "";
		}
	}
	if (document.orgDetails.orgCreditLimit.value == "") {
		document.getElementById("limit").innerHTML = "<font color = 'red'>Please Enter Limit</font>";
		isInValid = true;
	} else {
		if (isNaN(document.orgDetails.orgCreditLimit.value)) {
			document.getElementById("limit").innerHTML = "<font color = 'red'>Please Enter Correct Value For Limit</font>";
			isInValid = true;
		} else {
			document.getElementById("limit").innerHTML = "";
		}
	}
	if (document.orgDetails.orgAddr1.value == "") {
		document.getElementById("addr1").innerHTML = "<font color = 'red'>Please Enter Address Line1</font>";
		isInValid = true;
	} else {
		if (document.orgDetails.orgAddr1.value.match(alphaExp)) {
			document.getElementById("addr1").innerHTML = "";
		} else {
			document.getElementById("addr1").innerHTML = "<font color = 'red'>Please Enter Valid Address</font>";
			isInValid = true;
		}
	}
	if (document.orgDetails.orgCity.value == "") {
		document.getElementById("orgcity").innerHTML = "<font color = 'red'>Please Enter City</font>";
		isInValid = true;
	} else {
		if (document.orgDetails.orgCity.value.match(alphaExp)) {
			document.getElementById("orgcity").innerHTML = "";
		} else {
			document.getElementById("orgcity").innerHTML = "<font color = 'red'>Please Enter Valid City Name</font>";
			isInValid = true;
		}
	}
	if (isInValid) {
		return false;
	}
	return true;
}

parent.frames[0].Version['$Source: /rep/LMS_Mgmt/WebRoot/com/skilrock/lms/web/userMgmt/backOffice/js/orgReg.js,v $'] = '$Id: orgReg.js,v 1.1 2010/04/01 04:22:20 gaurav Exp $';