
function getRetailerList() {
	// Only Getting Start Date End Date 
	_id.non("chq_dtl_div");
	_id.non("x");
	var A = _ajaxCall("getRetailerNameListNDate.action?retType=ALL");
	var B = A.data.split(":");
	/*if(B[0] != ''){
		var C = B[0].split(",");
		var D = _id.o("orgName");
		for (var E = 0; E < C.length; E++) {
			var F = C[E].split("=")[1];
			var G = new Option(F, F);
			D.options[E + 1] = G;
		}
	}*/
	getRetailerUserList();// get Retailer List Method is in ConfirmAgent.js
	var H = B[1].split(",");
	_id.n("sd").value = H[0];
	_id.n("ed").value = H[1]; 
	_id.n("chequeDate").value = H[1]; 
	var I = "<input type=\"button\"  style=\" width:19px; height: 19px; background: url(" + image + "); top left; border:0 ; \" onclick=\"displayCalendar(document.getElementById('chequeDate'), 'mm/dd/yyyy', this, '" + H[1] + "', '" + H[0] + "', '" + H[1] + "')\" />";
	_id.non("im");
	_id.i("chqSub", I);
	_id.blk("x");
}
function validateChequeEntries() {
	var A = true;
	var B = new parent.frames[0].Validator("na");
	B.aV("orgName", "dontselect=0", "Please Select Organization Name", "orgNameError");
	B.aV("chequeNumber", "req", "Please Enter Valid Cheque Number", "chequeNumberError");
	B.aV("chequeNumber", "numeric", "Please Enter Only Number From 0-9", "chequeNumberError");
	
	B.aV("chequeNumber", "minlen=6", "Enter 6 to 10 digits Numeric value for Cheque Number", "chequeNumberError");
	B.aV("chequeNumber", "maxlen=10", "Enter 6 to 10 digits Numeric value for Cheque Number", "chequeNumberError");
	
	B.aV("issuePartyname", "req", "Please Enter Issue Party Name.", "issuePartynameError");
	B.aV("issuePartyname", "regexp=[^A-Za-z\\s]", "Please Enter Valid Issue Party Name.", "issuePartynameError");
	B.aV("bankName", "req", "Please Enter Bank Name.", "bankNameError");
	B.aV("bankName", "regexp=[^A-Za-z\\s&.]", "Please Enter Valid Bank Name.", "bankNameError");
	B.aV("chequeAmount", "req", "Please Enter Cheque Amount", "chequeAmountError");
	B.aV("chequeAmount", "decimal", "Please Enter Correct Value For Cheque Amount", "chequeAmountError");
	B.aV("verifychequeAmount", "req", "Please Enter Verify Cheque Amount", "verifyAmntError");
	B.aV("verifychequeAmount", "decimal", "Please Enter Correct Value For Verify Cheque Amount", "verifyAmntError");
	A = document.error_disp_handler.isValid;
	if (A && _id.v("verifychequeAmount") != _id.v("chequeAmount")) {
		_id.i("verifyAmntError", "Please Verify Cheque Amount fields", "e");
		A = false;
		_id.o("verifychequeAmount").focus();
	} else {
		if (A) {
			_id.i("verifyAmntError", "");
		}
	}
	if (A) {
		var C = convertDigitToWord(_id.v("chequeAmount"));
		if (C) {
			if (isChqDuplicate()) {
				return false;
			} else {
				_id.i("table_error_div", "");
			}
			addChqDetCrt(false);
			_id.o("orgName").disabled = true;
			_id.o("orgType").disabled = true;
			_id.o("chequeNumber").value = "";
			_id.o("issuePartyname").value = "";
			_id.o("bankName").value = "";
			_id.o("chequeAmount").value = "";
			_id.o("verifychequeAmount").value = "";
			_id.o("chequeDate").value = _id.v("chq_end_date");
		} else {
			A = false;
			_id.o("chequeAmount").focus();
		}
	}
	return false;
}
function isChqDuplicate() {
	for (var A in tabArr) {
		var B = tabArr[A].split(":");
		if (B.length > 1 && B[0] == _id.v("chequeNumber") && B[2].split(",")[1] == " " + _id.v("bankName")) {
			_id.i("table_error_div", "You Are Trying to Enter Duplicate Cheque Entries.", "e");
			_id.blk("table_error_div");
			return true;
		}
	}
	return false;
}
var _gblFlag = true;
var tabArr = new Array();
function addChqDetCrt(A) {
	var B = 1;
	var e = document.getElementById("orgName");
	var orgDisplayName = e.options[e.selectedIndex].text;
	if (!A) {
		tabArr[tabArr.length] = _id.v("chequeNumber") + ":" + orgDisplayName + ":" + _id.v("issuePartyname") + ", " + _id.v("bankName") + ":" + _id.v("chequeAmount") + ":" + _id.v("chequeDate") + ":<a href=\"#\" onclick=\"remRowFrmTab('" + tabArr.length + "')\">remove</a>";
	}
	var C = "<table width=\"100%\" border=\"1\" cellpadding=\"3\" cellspacing=\"0\" bordercolor=\"#CCCCCC\" align=\"center\"><tr><th align=\"center\">S.No</th><th align=\"center\">Cheque Number</th><th align=\"center\">Organization Name</th><th align=\"center\">Issuing Party & Bank Name</th><th align=\"center\">Cheque Amount</th><th align=\"center\">Cheque Date</th><th align=\"center\">Remove</th></tr>";
	var D = "";
	for (var E in tabArr) {
		var F = tabArr[E].split(":");
		if (F.length > 1) {
			D = D + "<tr><td align=\"right\">" + B + "</td><td>" + F[0] + "</td><td>" + F[1] + "</td><td>" + F[2] + "</td><td>" + F[3] + "</td><td align=\"right\">" + F[4] + "</td><td>" + F[5] + "</td></tr>";
			B++;
		}
	}
	C = C + D + "</table>";
	_id.i("chq_detail_tbl", C);
	_id.blk("cart");
	_id.blk("chq_dtl_div");
	if (B < 2) {
		_id.non("chq_dtl_div");
	}
}
function remRowFrmTab(A) {
	tabArr[A] = "";
	addChqDetCrt(true);
}
function createHidenFields(A) {
	var chAmt = 0;
	var B = new Array("<input type=\"hidden\" name=\"", "Hidden\" value=\"");
	var C = B[0] + "orgType" + B[1] + _id.v("orgType") + "\" />" + B[0] + "orgName" + B[1] + _id.v("orgName") + "\" />";
	for (var D in tabArr) {
		var E = tabArr[D].split(":");
		if (E.length > 1) {
			C = C + B[0] + "chequeNumber" + B[1] + E[0] + "\" />" + B[0] + "issuePartyName" + B[1] + E[2].split(",")[0] + "\" />" + B[0] + "bankName" + B[1] + E[2].split(",")[1] + "\" />" + B[0] + "chequeAmount" + B[1] + E[3] + "\" />" + B[0] + "chequeDate" + B[1] + E[4] + "\" />";
			chAmt += parseFloat(E[3]);
		}
	}
	_id.i("hiddenValues", C);
	var _resp = _ajaxCall("fetchAgtCrLimit.action?listType=" + chAmt);
	if (_resp.data != "TRUE") {
		_id.i("table_error_div", "<center>" + _resp.data + " </center>");
		return false;
	}
	if (!_subValid(A)) {
		return false;
	}
	return true;
}

function removeMessages()
{
document.getElementById("orgNameError").innerHTML="";
document.getElementById("chequeNumberError").innerHTML="";
document.getElementById("issuePartynameError").innerHTML="";
document.getElementById("bankNameError").innerHTML="";
document.getElementById("chequeAmountError").innerHTML="";
document.getElementById("verifyAmntError").innerHTML="";
document.forms["na"].reset();
document.getElementById("orgName").value=-1;
if(document.getElementById("limitDiv"))
document.getElementById("limitDiv").innerHTML="";
return false;
}
parent.frames[0].Version['$Source: /rep/LMS_Mgmt/WebRoot/com/skilrock/lms/web/accMgmt/agent/js/agt_accMgmt_cashChequePayment.js,v $'] = '$Id: agt_accMgmt_cashChequePayment.js,v 1.2 2016/10/31 09:46:37 neeraj Exp $';