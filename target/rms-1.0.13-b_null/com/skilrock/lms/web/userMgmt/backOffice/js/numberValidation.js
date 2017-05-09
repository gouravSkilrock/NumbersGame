
function isNumeric(id) {
	var elem = document.getElementById(id);
	var numericExpression = /^[0-9]+$/;
	if (elem.value.match(numericExpression) && !trimAll(elem.value) == "") {
		doReset(elem);
		return true;
	} else {
		var elem1 = elem.id + "1";
	//alert(elem1);
		document.getElementById(elem1).innerHTML = "<font color = 'red'>Please Enter Valid Value.</font>";
		elem.focus();
		return false;
	}
}
function doReset(elem) {
	var elem1 = elem.id + "1";
	document.getElementById(elem1).innerHTML = "";
}
function onlyNumbers(evt) {
	var e = event || evt;
	var charCode = e.which || e.keyCode;
	if (charCode >= 48 && charCode < 58) {
		return true;
	}
	return false;
}
function isNumeric(id) {
	var strValidChars = "0123456789.";
	var strChar;
	var flag = false;
	var dotCount = 0;
	var strString = document.getElementById(id).value;
	if (strString.length == 0) {
		return false;
	}

   //  test strString consists of valid characters listed above
	for (i = 0; i < strString.length; i++) {
		strChar = strString.charAt(i);
		if (strChar == ".") {
			dotCount++;
		}
		if (strValidChars.indexOf(strChar) == -1) {
			document.getElementById(id + "1").style.display = "block";
			document.getElementById(id + "1").innerHTML = "<font color = 'red'>Please Enter Valid Value.</font>";
			document.getElementById(id).focus();
			flag = true;
			break;
		}
	}
	if (dotCount > 1) {
		document.getElementById(id + "1").style.display = "block";
		document.getElementById(id + "1").innerHTML = "<font color = 'red'>Please Enter Valid Value.</font>";
		document.getElementById(id).focus();
		flag = true;
	}
	if (!flag) {
		document.getElementById(id + "1").innerHTML = "";
		document.getElementById(id + "1").style.display = "none";
	}
}
function specialCharValidate() {
	var strValidChars =  "!@#$%^&*()+=-[]\\\';,./{}|\":<>?";
	var strChar;	
	var strString = document.getElementById(id).value;	

   //  test strString consists of valid characters listed above
	for (i = 0; i < strString.length; i++) {
		strChar = strString.charAt(i);	
		if (strValidChars.indexOf(strChar)!= -1) {
			document.getElementById(id + "1").style.display = "block";
			document.getElementById(id + "1").innerHTML = "<font color = 'red'>Please Enter Valid Value.</font>";			
			flag = true;
			break;
		}
	}
}

parent.frames[0].Version['$Source: /rep/LMS_Mgmt/WebRoot/com/skilrock/lms/web/userMgmt/backOffice/js/numberValidation.js,v $'] = '$Id: numberValidation.js,v 1.1 2010/04/01 04:22:21 gaurav Exp $';