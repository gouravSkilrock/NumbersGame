
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
function getAgentList() {
	_ajaxUnsync(path + "/com/skilrock/lms/web/userMgmt/getAgentNameList.action?orgType=AGENT", "fillAgent");
}
function fillAgent() {
	asyncResult=asyncResult.substring(0, asyncResult.length-1);
	var firstArr = asyncResult.split(":");
	var retObj = _id.o("agtOrgName");
	var sortArr=firstArr;
	for (var i = 0; i < sortArr.length; i++) {
		var retNameArray = sortArr[i].split("|");
		var opt = new Option(retNameArray[0].toUpperCase(), retNameArray[1]);
		retObj.options[i + 1] = opt;
	}
}
function updateData(row) {
	heightDiv();
	var agtId = _id.o("agtOrgName");
	var rowId = _id.o(row);
	var incAmt = _id.n("incAmt")[row].value;
	var decAmt = _id.n("decAmt")[row].value;
	var balance = _id.n("balance")[row].value;
	var status = _id.n("orgStatus")[row].value;
	var retOrgId = _id.n("orgId")[row].value;
	var balance = parseFloat(incAmt) - parseFloat(decAmt);
	if ((incAmt == 0 && decAmt == 0) || isNaN(balance)) {
		balance = 0;//Means No Updation
	}
	var updData = "&retOrgId=" + retOrgId + "&balance=" + balance + "&orgStatus=" + status;
	//alert(updData);
	_ajaxUnsync(path + "/com/skilrock/lms/web/userMgmt/bo_rep_dashBoard_Update.action?agentOrgId=" + agtId.value + updData, "afterAjax");
	updateBalLimitClr();
	//showEffLimit(row+1);
}
function afterAjax() {
	_id.i("resultDiv", asyncResult);
	_id.blk("resultDiv");
	_id.o("completeDiv").style.display = "none";
	fillTempArr();
}
function heightDiv() {
	$("#completeDiv").height(document.body.clientHeight - 160 + "px");
	//_id.o("completeDiv").style.height = document.body.clientHeight - 160 + "px";
	_id.o("completeDiv").style.display = "block";
}
function fetchActData(agtId) {
	_id.o("resultDiv").innerHTML = "";
	_id.o("reportDiv").innerHTML = "";
	var _resp = _ajaxUnsync("bo_rep_dashBoard_Search.action?agentOrgId=" + agtId, "afterAjax");
	//_id.i("resultDiv", _resp.data);
	//_id.blk("resultDiv");
	//filterData();
	updateBalLimitClr();
}

function updateBalLimitClr(){
	var x = _id.o('totLimit').value;
	var y = _id.o('balLimit').value;
		if(y<(x*0.05)){
		_id.o('balLimClr').style.color="red";
	}else if(y<(x*0.2)){
		
		_id.o('balLimClr').style.color="#ffa825";
	}else{
		_id.o('balLimClr').style.color="green";
	}
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
	heightDiv();
	
	//alert(updData);
	_ajaxUnsync(path + "/com/skilrock/lms/web/userMgmt/bo_rep_dashBoard_Update.action?", "afterAjax", "agentOrgId=" + agtId.value + updData);
	updateBalLimitClr();
}
function updateAll() {
	var agtId = _id.o("agtOrgName");
	var chkBx = _id.n("tempStatus");
	var updData = "";
	for (var i = 0; i < chkBx.length; i++) {
		var incAmt = _id.n("incAmt")[i].value;
		var decAmt = _id.n("decAmt")[i].value;
		var balance = parseFloat(incAmt) - parseFloat(decAmt);
		if ((incAmt == 0 && decAmt == 0) || isNaN(balance)) {
			balance = 0;//Means No Updation
		}
		var status = _id.n("orgStatus")[i].value;
		var retOrgId = _id.n("orgId")[i].value;
		updData = updData + "&retOrgId=" + retOrgId + "&balance=" + balance + "&orgStatus=" + status;
	}
	if (updData.length < 1) {
		return;
	}
	heightDiv();
	//alert(updData);
	_ajaxUnsync(path + "/com/skilrock/lms/web/userMgmt/bo_rep_dashBoard_Update.action?", "afterAjax", "agentOrgId=" + agtId.value + updData);
	updateBalLimitClr();
}
function updRowColor(rowId) {
	var row = _id.o("row" + rowId);
	var inc = _id.n("incAmt")[rowId - 1].value;
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
	var cnt = document.getElementsByName('balance').length;
	var sum = 0.0;
	for(var i=0;i<cnt;i++){
		sum = sum + parseFloat(document.getElementsByName('decAmt')[i].value) - parseFloat(document.getElementsByName('incAmt')[i].value);
	}
	document.getElementById('effBalId').innerHTML = parseFloat(document.getElementById('balLimit').value) + sum;
}
parent.frames[0].Version["$Source: /rep/LMS_Mgmt/WebRoot/com/skilrock/lms/web/reportsMgmt/backOffice/js/dashBoard.js,v $"] = "$Id: dashBoard.js,v 1.3 2016/10/31 09:46:26 neeraj Exp $";

