
var convDateTime = "AbsoluteTime";  //'AbsoluteTime','RelativeTime'
var statusArr = ["ACTIVE", "IDLE", "TERMINATE", "INACTIVE"];
var serTime = "";
var isDrawPwt = "false";   //2
var isCancel = "false";    //3
var isScratchPwt = "false";//4
var isLogin = "false";	 //5
var isLastTrx = "false";	 //5
var retCount = 0;
var curCheckStatus = null;
var activeTime = (1000 * 60 * 60 * 2 * 1);//Millisecond*Second*Minute*Hour*Days
var idleTime = (1000 * 60 * 60 * 24 * 2);
var offset = parent.frames[0].offset;
var tempDataArr = new Array();

function fetchActData(val) {
	//alert('*****'+val+'***********');
	_id.o("reportDiv").innerHTML = "";
	//alert('*****'+_id.o("reportDiv")+'***********');
	if(val == 'AGENT'){
		_id.o("reportDiv").innerHTML = "";
		var _resp = _ajaxCall("bo_um_dashBoard_new_Search.action");
		//alert('*****'+_resp+'***********');
		//alert('*****'+_resp.data+'***********');
		_id.i("reportDiv", _resp.data);
		_id.blk("reportDiv");
		//alert('****************');
		fillTempArr();
		
		updateBalLimitClr();	
		
	} 
}

function updateData(row) {
	_id.i('errDiv','');
	//alert('***********'+row+'************');
	//heightDiv();
	//var rowId = _id.o(row);
	//alert('************'+rowId+'**************');
	//alert('**********'+_id.n("incAmt")+'**********');
	var incAmt = _id.n("incAmt")[row].value;
	//alert('*****incAmt*******'+incAmt+'**************');
	var decAmt = _id.n("decAmt")[row].value;
	//alert('*****decAmt*******'+decAmt+'**************');
	var balance = _id.n("balance")[row].value;
	//alert('****balance********'+balance+'**************');
	var status = _id.n("orgStatus")[row].value;
	//alert('*****orgStatus*******'+status+'**************');
	var retOrgId = _id.n("orgId")[row].value;
	//alert('*****orgId*******'+retOrgId+'**************');
	var xclAmt = _id.n("xclAmt")[row].value;
	//alert('*****xclAmt*******'+xclAmt+'**************');
	var xclDays = _id.n("xclDays")[row].value;
	//alert('*****xclDays*******'+xclDays+'**************');
	var isInvalidDays = isNumeric(xclDays);
	//alert('***isInvalidDays****'+isInvalidDays+'****');
	if(isInvalidDays == true){
		//alert('in if');
		_id.i('errDiv','Please Select Valid XCL Days','e');
		return false;
	}
	//alert('testing');
	var balance = parseFloat(incAmt) - parseFloat(decAmt);
	if ((incAmt == 0 && decAmt == 0) || isNaN(balance)) {
		balance = 0;//Means No Updation
	}
	var updData = "&retOrgId=" + retOrgId + "&balance=" + balance + "&orgStatus=" + status + "&xclAmt=" + xclAmt + "&xclDays=" + xclDays;
	//alert('******'+updData+'**********');
	//alert(updData);
	//_ajaxUnsync(path + "/com/skilrock/lms/web/userMgmt/bo_um_dashBoard_new_Update.action?" + updData, "afterAjax");
	
	var _resp = _ajaxCall("bo_um_dashBoard_new_Update.action?" + updData);
		_id.i("reportDiv", _resp.data);
		_id.blk("reportDiv");
	
	updateBalLimitClr();
	//showEffLimit(row+1);
	return true;
}
function afterAjax() {
	_id.i("reportDiv", asyncResult);
	//alert('********'+asyncResult+'**********');
	_id.blk("resultDiv");
	//_id.o("completeDiv").style.display = "block";
	fillTempArr();
}



function updateBalLimitClr(){
	//alert('8888');
	//var x = _id.o('totLimit').value;
	//alert('**********'+x+'******');
	//var y = _id.o('balLimit').value;
	//alert('*******'+y+'*********');
	//	if(y<(x*0.05)){
	//	_id.o('balLimClr').style.color="red";
	//}else if(y<(x*0.2)){
		
	//	_id.o('balLimClr').style.color="#ffa825";
	//}else{
	//	_id.o('balLimClr').style.color="green";
	//}
		return true;
}

function selAll(val) {
	var chkBx = _id.n("tempStatus");
	for (var i = 0; i < chkBx.length; i++) {
		chkBx[i].checked = val;
	}
}
function updateStatus(val) {
	var agtId = _id.o("agtOrgName");
	var chkBx = _id.n("tempStatus");
	var updData = "";
	for (var i = 0; i < chkBx.length; i++) {
		if (chkBx[i].checked == true) {
			var incAmt = _id.n("incAmt")[i].value;
			var decAmt = _id.n("decAmt")[i].value;
			var balance = parseFloat(incAmt) - parseFloat(decAmt);
			if ((incAmt == 0 && decAmt == 0) || isNaN(balance)) {
				balance = 0;//Means No Updation
			}
			var status = val;
			var retOrgId = _id.n("orgId")[i].value;
			updData = updData + "&retOrgId=" + retOrgId + "&balance=" + balance + "&orgStatus=" + status;
		}
	}
	if (updData.length < 1) {
		return;
	}
	//heightDiv();
	
	//alert(updData);
	_ajaxUnsync(path + "/com/skilrock/lms/web/userMgmt/bo_um_dashBoard_new_Update.action?", "afterAjax", "agentOrgId=" + agtId.value + updData);
	updateBalLimitClr();
}
function updateAll() {
	_id.i('errDiv','');
	//alert('********'++'****');
	//var agtId = _id.o("agtOrgName");
	//alert('***agtId***'+agtId+'***');
	var chkBx = _id.n("tempStatus");
	//alert('***chkBx*****'+chkBx+'****');
	var updData = "";
	for (var i = 0; i < chkBx.length; i++) {
		if(chkBx[i].checked == true){
			var incAmt = _id.n("incAmt")[i].value;
			//alert('***incAmt*****'+incAmt+'****');
			var decAmt = _id.n("decAmt")[i].value;
			var balance = parseFloat(incAmt) - parseFloat(decAmt);
			if ((incAmt == 0 && decAmt == 0) || isNaN(balance)) {
				balance = 0;//Means No Updation
			}
			var status = _id.n("orgStatus")[i].value;
			var retOrgId = _id.n("orgId")[i].value;
			
			var xclAmt = _id.n("xclAmt")[i].value;
			//alert('*****xclAmt*******'+xclAmt+'**************');
			var xclDays = _id.n("xclDays")[i].value;
			//alert('*****xclDays*******'+xclDays+'**************');
			
			var isInvalidDays = isNumeric(xclDays);
			//alert('***isInvalidDays****'+isInvalidDays+'****');
			if(isInvalidDays == true){
				//alert('in if');
				_id.i('errDiv','Please Select Valid XCL Days','e');
				return false;
			}
		
			updData = updData + "&retOrgId=" + retOrgId + "&balance=" + balance + "&orgStatus=" + status + "&xclAmt=" + xclAmt + "&xclDays=" + xclDays;
		}
	}
	
	if (updData.length < 1) {
		return;
	}
	//alert('***updData****'+updData+'**');
	//heightDiv();
	//alert(updData);
	_ajaxUnsync(path + "/com/skilrock/lms/web/userMgmt/bo_um_dashBoard_new_Update.action?", "afterAjax", updData);
	updateBalLimitClr();
	return true;
}
function updRowColor(rowId) {
	//alert('************'+rowId+'*************');
	var row = _id.o("row" + rowId);
	var inc = _id.n("incAmt")[rowId - 1].value;
	//alert('****'+_id.n("incAmt")+'******');
	var dec = _id.n("decAmt")[rowId - 1].value;
	var status = _id.n("orgStatus")[rowId - 1].value;
	if (inc == "") {
		_id.n("incAmt")[rowId - 1].value = 0;
		inc = 0;
	}
	if (dec == "") {
		_id.n("decAmt")[rowId - 1].value = 0;
		dec = 0;
	}
	if (tempDataArr[rowId] == (inc + ":" + dec + ":" + status)) {
		row.style.background = "white";
	} else {
		row.style.background = "#F8A858";
	}
	//alert('ending');
}
function fillTempArr() {
	var inc = _id.n("incAmt");
	var dec = _id.n("decAmt");
	var status = _id.n("orgStatus");
	for (var i = 1, len = inc.length; i <= len; i++) {
		tempDataArr[i] = inc[i - 1].value + ":" + dec[i - 1].value + ":" + status[i - 1].value;
	}
}

function showEffLimit(rowId){
	document.getElementsByName('effAmt')[rowId - 1].value = parseFloat(document.getElementsByName('balance')[rowId - 1].value) + parseFloat(document.getElementsByName('incAmt')[rowId - 1].value) - parseFloat(document.getElementsByName('decAmt')[rowId - 1].value);
}


function showNetLimit(){
	//alert("******1****");
	var cnt = document.getElementsByName('balance').length;
	//alert("*****2*****");
	var sum = 0.0;
	//alert("******3****");
	for(var i=0;i<cnt;i++){
	//alert("*****4*****");
		sum = sum + parseFloat(document.getElementsByName('decAmt')[i].value) - parseFloat(document.getElementsByName('incAmt')[i].value);
	}
	//alert("*****5*****" + document.getElementById('effBalId'));
	//document.getElementById('effBalId').innerHTML = parseFloat(document.getElementById('balLimit').value) + sum;
	//alert("******6****");
}

 function isNumeric(value) {
 	//alert("called");
 	var strValidChars = "1234567890";
	var strChar;
	var flag = false;
	var strString = value;
	   //  test strString consists of valid characters listed above
	for (i = 0; i < strString.length; i++) {
		strChar = strString.charAt(i);
		if (strValidChars.indexOf(strChar) == -1) {
			flag = true;
			break;
		}
	}
	
	return flag;
}
parent.frames[0].Version["$Source: /rep/LMS_Mgmt/WebRoot/com/skilrock/lms/web/userMgmt/backOffice/js/dashBoard_new.js,v $"] = "$Id: dashBoard_new.js,v 1.2 2016/10/31 11:40:07 neeraj Exp $";

