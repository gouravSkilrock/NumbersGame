var i18nObj = parent.frames[0].i18Obj;
var convDateTime = "AbsoluteTime";  // 'AbsoluteTime','RelativeTime'
var countMethod = "percent"; // 'count','percent'
var statusArr = ["LIVE", "IDLE", "TERMINATE", "NO SALE","NO TERMINAL"];
var serTime = "";
var isDrawPwt = "false";   // 2
var isCancel = "false";    // 3
var isScratchPwt = "false";// 4
var isLogin = "false";	 // 5
var isLastTrx = "false";	 // 5
var isCurrentVersion = "false";
var isTerminalId = "false";
var isOfflineStatus = "false";
var retCount = 0;
var count = {"LIVE":0, "IDLE":0, "TERMINATE":0, "NO SALE":0,"NO TERMINAL":0};
var curCheckStatus = null;
var activeTime = (1000 * 60 * 60 * 2 * 1);// Millisecond*Second*Minute*Hour*Days
var idleTime = (1000 * 60 * 60 * 14 * 1);
var offset = parent.frames[0].offset;
var colCount = 6;// This is the no. of column count before Last Sale
					// excluding (S.no).
function getAgentList() {
	var text = _ajaxCall(path + "/com/skilrock/lms/web/userMgmt/getAgentNameList.action?orgType=AGENT");
	var firstArr = text.data.split(":");
	var retObj = _id.o("agtOrgName");
	for (var i = 0; i < firstArr.length - 1; i++) {
		var retNameArray = firstArr[i].split("|");
		var opt = new Option(retNameArray[0].toUpperCase(), retNameArray[1]);
		retObj.options[i + 1] = opt;
	}
}
function getRetList() {
	var agtId = _id.o("agtOrgName");
	var text = _ajaxCall(path + "/com/skilrock/lms/web/userMgmt/fetchRetOrgList.action?orgId=" + agtId.options[agtId.selectedIndex].value);
	var firstArr = text.data.split(":");
	var retObj = _id.o("retOrgName");

	retObj.options.length = 1;

	for (var i = 0; i < firstArr.length-1; i++) {
		var retNameArray = firstArr[i].split("|");
		var opt = new Option(retNameArray[0].toUpperCase(), retNameArray[1]);

		retObj.options[i + 1] = opt;
	}
}
function verifyServiceName()
{
	var serviceName = _id.v("serviceName");
	if (serviceName == -1) {
		_id.i("serviceName_e", i18nObj.prop('error.select.srv.name'), "e");
		return false;
	} else {
			_id.i("serviceName_e", "");
			return true;
			}
	return true;
}
function fetchActData(agtId) {
	// if(document.getElementById('reportType').value == 'current'){
	// var toHide = document.getElementsByTagName('TD');
	// var len = toHide.length;
	// for(i=6;i<len;i++){
	// toHide[i].style.visibility = 'visible';
	// }
	// document.getElementById('date').style.display = 'none';
		document.getElementById('reportDiv').style.display = 'block';
	// document.getElementById('histReportDiv').style.display = 'none';
	// var all = document.getElementsByTagName('INPUT');
	// var allLen = all.length;
	// for(i=0;i<allLen;i++){
	// if(all[i].value=='Search'){
	// all[i].style.display = 'none';
	// }
	// }
		_id.o("resultDiv").innerHTML = "";
		_id.o("reportDiv").innerHTML = "";
		var _resp = _ajaxCall("bo_rep_retActivity_Result.action?agentOrgId=" + agtId);
		_id.i("resultDiv", _resp.data);
		// _id.blk("resultDiv");
		filterData();
	// }
	// else{
	// fetchRetActivityHistory();
	// }
}
function fetchRetActivityHistory(){
	if(document.getElementById('reportType').value == 'history'){
		document.getElementById('date').style.display = 'block';
		document.getElementById('reportDiv').style.display = 'none';
		document.getElementById('histReportDiv').style.display = 'block';
		document.getElementById('histReportDiv').innerHTML = '';
		var toHide = document.getElementsByTagName('TD');
		var len = toHide.length;
		for(i=6;i<len;i++){
			// alert(toHide[i].innerHTML);
				if(toHide[i].innerHTML.search('struts:Bind') == -1){
					toHide[i].style.visibility = 'hidden';
				}else{
					// alert(toHide[i].innerHTML.search('struts:Bind'));
				}
		}
		var all = document.getElementsByTagName('INPUT');
		var allLen = all.length;
		for(i=0;i<allLen;i++){
			if(all[i].value=='Search'){
				all[i].style.display = 'block';
			}
		}
	}
	return true;
}

function filterData() {
	fillCurSelStatus();
	count["LIVE"] = 0;
	count["IDLE"] = 0;
	count["NO SALE"] = 0;
	count["TERMINATE"] = 0;
	count["NO TERMINAL"] = 0;
	createReport(_id.o("reportData").innerHTML);
	var repData = _id.o("reportDiv").innerHTML;
	var table = document.getElementById("activityTab");
	var rowCount = table.rows.length;
	var alCol = document.getElementsByName("alStatus");
	var locCol = _id.o("location").value;
	var terminalType = _id.o("terminal").value;
	var j = 0;
	if (alCol.length != 0) {
		for (var i = 3; i < rowCount; i++) {
			var row = table.rows[i];
			var colData = alCol[i - 3];
			if(BrowserDetect.browser=="Safari" || BrowserDetect.browser=="Firefox" || BrowserDetect.browser=="Chrome"){
				if (!selStatus(colData.textContent)) {
					row.style.display = "none";
				}
				if (locCol != "ALL" ) {
					if (!(row.textContent).match(locCol)) {
						row.style.display = "none";
					}
				}
				if(terminalType != "ALL"){
					if(terminalType != 'N.A.'){
						if (!(row.textContent).match(terminalType)) {
							row.style.display = "none";
						}
					}else{
						if (!(row.textContent).match('NO TERMINAL')) {
							row.style.display = "none";
						}
					}
				}
			}else{
				if (!selStatus(colData.innerText)) {
					row.style.display = "none";
				}
				if (locCol != "ALL") {
					if (!(row.innerText).match(locCol)) {
						row.style.display = "none";
					}
				}
				if(terminalType != "ALL"){
					if (!(row.innerText).match(terminalType)) {
						row.style.display = "none";
					}
				}
			}
		}
		genSerialnum(table, rowCount);
		countStatus();
	}
	if (countMethod == "count") {
		_id.i("activeCnt", count["LIVE"]);
		_id.i("idleCnt", count["IDLE"]);
		_id.i("nosaleCnt", count["NO SALE"]);
		_id.i("terminateCnt", count["TERMINATE"]);
		_id.i("noTerminal",count["NO TERMINAL"]);
	} else {
		if (countMethod == "percent") {
			if(_id.o('terminal').value != 'N.A.'){
				_id.i("activeCnt", (count["LIVE"] * 100 / (retCount - count["TERMINATE"] - count["NO TERMINAL"])).toFixed(2) + "%");
				_id.i("idleCnt", (count["IDLE"] * 100 / (retCount - count["TERMINATE"] - count["NO TERMINAL"])).toFixed(2) + "%");
				_id.i("nosaleCnt", (count["NO SALE"] * 100 / (retCount - count["TERMINATE"] - count["NO TERMINAL"])).toFixed(2) + "%");
				_id.i("terminateCnt", count["TERMINATE"]);
				_id.i("noTerminal",count["NO TERMINAL"]);
				_id.n('statusName')[4].style.visibility = 'hidden';
				_id.o('noTermId').style.visibility = 'hidden';
			}else{
				_id.i("activeCnt", "0%");
				_id.i("idleCnt", "0%");
				_id.i("nosaleCnt", "0%");
				_id.i("terminateCnt", 0);
				_id.i("noTerminal",count["NO TERMINAL"]);
				_id.n('statusName')[4].style.visibility = 'visible';
				_id.o('noTermId').style.visibility = 'visible';
			}
		}
	}
	return true;
}
function fillCurSelStatus() {
	var curSel = _id.n("statusName");
	curCheckStatus = new Array();
	for (var i = 0; i < curSel.length; i++) {
		if (curSel[i].checked) {
			curCheckStatus.push(curSel[i].value);
		}
	}
}
function selStatus(colData) {
	for (var i = 0; i < curCheckStatus.length; i++) {
		if (curCheckStatus[i] == colData) {
			return true;
		}
	}
	return false;
}
function genSerialnum(table, rowCount) {
	var snoData = _id.n("sno");
	var count = 0;
	for (var i = 3; i < rowCount; i++) {
		var row = table.rows[i];
		var colData = snoData[i - 3];
		if (row.style.display != "none") {
			count++;
			colData.innerHTML = count;
		}
	}
}
function updStatusMsg() {
	var stats = _id.o("statusDiv").innerHTML;
	var stArr = stats.split(";");
	var newArr = new Array();
	for (var i = 0; i < stArr.length - 1; i++) {
		newArr[stArr[i].split("=")[0].replace(" ", "")] = stArr[i].split("=")[1].replace(" ", "");
	}
	isDrawPwt = (newArr["isDrawPwt"] != "undefined" ? newArr["isDrawPwt"] : "false");
	isCancel = (newArr["isCancel"] != "undefined" ? newArr["isCancel"] : "false");
	isScratchPwt = (newArr["isScratchPwt"] != "undefined" ? newArr["isScratchPwt"] : "false");
	isLogin = (newArr["isLogin"] != "undefined" ? newArr["isLogin"] : "false");
	isLastTrx = (newArr["isLastTrx"] != "undefined" ? newArr["isLastTrx"] : "false");
	isCurrentVersion = (newArr["isCurrentVersion"] != "undefined" ? newArr["isCurrentVersion"] : "false");
	isTerminalId = (newArr["isTerminalId"] != "undefined" ? newArr["isTerminalId"] : "false");
	isOfflineStatus = (newArr["isOfflineStatus"] != "undefined" ? newArr["isOfflineStatus"] : "false");
}
function createReport(data) {
	_id.o("reportDiv").innerHTML = "";
	retCount = 0;
	serTime = _id.o("curDate").innerHTML;
	updStatusMsg();
	var dataArr = data.split(",");
	var colCount = 1;
	var repData = "";
	if (isScratchPwt == "true") {
		repData += "<th align=\"center\">"+i18nObj.prop('label.sc.game')+"</th>";
	}
	if (isLogin == "true") {
		repData += "<th rowspan=\"2\" align=\"center\">"+i18nObj.prop('label.last.login')+"</th>";
	}
	if (isLastTrx == "true") {
		repData += "<th rowspan=\"2\" align=\"center\">"+i18nObj.prop('label.last.activity')+"</th>";
	}
	repData += "<th rowspan=\"2\" align=\"center\">ALERT</th></tr><tr><th align=\"center\">"+i18nObj.prop('label.last.sale')+"</th>";
	if (isDrawPwt == "true") {
		repData += "<th align=\"center\">"+i18nObj.prop('label.last.pwt')+"</th>";
		colCount++;
	}
	if (isCancel == "true") {
		repData += "<th align=\"center\">"+i18nObj.prop('label.last.cancel')+"</th>";
		colCount++;
	}
	if (isScratchPwt == "true") {
		repData += "<th align=\"center\">"+i18nObj.prop('label.last.sc.pwt')+"</th>";
	}
	repData += "</tr>";
	startData = "<table border=\"1\"  id=\"activityTab\" cellpadding=\"3\" cellspacing=\"0\" bordercolor=\"#CCCCCC\" align=\"center\"><tr><td align=\"center\" colspan=\"";
	globalColCount = colCount;
	if (isCurrentVersion == "true") {
		globalColCount += 1;
	}
	if (isTerminalId == "true") {
		globalColCount += 1;
	}
	if (isOfflineStatus == "true") {
		globalColCount += 1;
	}
	startData += (globalColCount + 6) + "\"><b>"+i18nObj.prop('label.rep.gen.time')+":</b> " + _id.o("repGenTime").innerHTML + "</td></tr><tr><th rowspan=\"2\">"+i18nObj.prop('label.sNo')+".</th><th rowspan=\"2\">" + tierMap["RETAILER"] +i18nObj.prop('label.name')+" </th><th rowspan=\"2\">" + tierMap["AGENT"] + i18nObj.prop('label.name')+"</th><th rowspan=\"2\">"+i18nObj.prop('label.location')+"</th>";
	if (isOfflineStatus == "true") {
		startData += "<th rowspan=\"2\">"+i18nObj.prop('label.offline.status')+"</th>";
	}
	if (isCurrentVersion == "true") {
		startData += "<th rowspan=\"2\">"+i18nObj.prop('label.version')+"</th>";
	}
	if (isTerminalId == "true") {
		startData += "<th rowspan=\"2\">"+i18nObj.prop('label.terminal')+"</th>";
	}
	startData += "<th align=\"center\" colspan=\"" + colCount + "\">"+i18nObj.prop('label.draw.game')+"</th>";
	repData = startData + repData;
	for (var i = 0; i < dataArr.length - 1; i++) {
		var rowData = dataArr[i].split(":");
		repData = "<tr>" + fillRowData(rowData, repData) + "</tr>";
	}
	// alert(count['LIVE']+' '+count['IDLE']+' '+count['TERMINATE']+'
	// '+count['NO SALE']+' ');
	if (data == "") {
		repData += "<tr><td colspan=\"" + (colCount + 5) + "\" align=\"center\">"+i18nObj.prop('msg.no.record')+"</td></tr>";
	}
	repData += "</table>";
	_id.i("reportDiv", repData);
}
function callDateTime(value) {
	if (convDateTime == "AbsoluteTime") {
		return convertDate(value);
	} else {
		if (convDateTime == "RelativeTime") {
			return convertTime(value);
		}
	}
}
function fillRowData(rowData, repData) {
	var lastTime = "";
	retCount++;
	repData += "<td id='sno' name='sno'>" + retCount + ".</td>";
	for (var j = 0; j < rowData.length - 2; j++) {
		repData += "<td>" + callDateTime(rowData[j]) + "</td>";
	}
	lastTime = latestTime(rowData);
	if (isLastTrx == "true") {
		repData += "<td>" + callDateTime(lastTime) + "</td>";
	}
	var tmId = rowData[5].replace(/(\s)/g, "").replace(/(\n)/g,"");
	var orgStatus = rowData[rowData.length - 2].replace(/(\s)/g, "").replace(/(\n)/g,"");
	var midData = ""; 
		if(tmId == 'N.A.'){
			if(orgStatus != 'TERMINATE'){
				midData += alertStatus(lastTime,'NO TERMINAL');
			}else{
				midData += alertStatus(lastTime,orgStatus);
			}
		}
		else{
			midData += alertStatus(lastTime,orgStatus);
		}
	return (repData + midData);
}
function latestTime(arr) {
	var max = arr[colCount];
	var len = arr.length;
	for (var i = colCount + 1; i < len; i++) {
		if (!isNaN(arr[i]) && arr[i] != 0) {
			if (parseInt(arr[i]) > parseInt(max)) {
				max = arr[i];
			}
		}
	}
	if (isNaN(max)) {
		return "N.A.";
	}
	return max;
}
function convertTime(timeData) {
	if (timeData.indexOf("version") != -1) {
		return timeData.substring(0, timeData.length - 7);
	}
	if (isNaN(timeData)) {
		return timeData;
	} else {
		if (timeData == 0) {
			return "N.A.";
		}
	}
	var timeDiff = serTime - timeData;
	// alert(timeDiff);
	var milliSec = 0;
	days = 0;
	hours = 0;
	mins = 0;
	secs = 0;
	out = "";
	timeDiff = Math.floor(timeDiff / 1000);  // seconds
	days = Math.floor(timeDiff / 86400);
	timeDiff = timeDiff % 86400;
	hours = Math.floor(timeDiff / 3600);// hours
	timeDiff = timeDiff % 3600;
	mins = Math.floor(timeDiff / 60);// minutes
	timeDiff = timeDiff % 60;
	secs = Math.floor(timeDiff);// seconds
	if (days != 0) {
		out += days + " d ";
	}
	if (days != 0 || hours != 0) {
		out += hours + " hr ";
	} else {
		if (hours == 0) {
			out += hours + " hr ";
		}
	}
	if (days != 0 || hours != 0 || mins != 0) {
		out += mins + " min ";
	} else {
		if (mins == 0) {
			out += mins + " min ";
		}
	}
	// out += (secs < 10 ? "0" + secs : secs )+" sec";
	return out;
}
function alertStatus(lastTime, status) {
	// console.log(status);
	if (status == "TERMINATE") {
		return "<td style=\"background:black;color:white\" name=\"alStatus\" id=\"alStatus\" >" + statusArr[2] + "</td>";
	}
	if(status == 'NO TERMINAL'){
		return "<td style=\"background:black;color:white\" name=\"alStatus\" id=\"alStatus\" >" + statusArr[4] + "</td>";
	}				
	else{
		var timeDiff = serTime - lastTime;
		if (timeDiff < activeTime) {
			return "<td style=\"background:green;color:white\" name=\"alStatus\"  id=\"alStatus\" >" + statusArr[0] + "</td>";				
		} 
		else {
			if (timeDiff > activeTime && timeDiff < idleTime) {
				return "<td style=\"background:yellow;color:black\" name=\"alStatus\" id=\"alStatus\" >" + statusArr[1] + "</td>";
			}
			else {
				return "<td style=\"background:red;color:white\" name=\"alStatus\" id=\"alStatus\">" + statusArr[3] + "</td>";
			}
		}
	}
}
function countStatus() {
	var table = document.getElementById("activityTab");
	var rowCount = table.rows.length;
	var alCol = document.getElementsByName("alStatus");
	var j = 0;
	retCount = 0;
	if (alCol.length != 0) {
		for (var i = 3; i < rowCount; i++) {
			var row = table.rows[i];
			var colData = alCol[i - 3];
			if(BrowserDetect.browser=="Safari" || BrowserDetect.browser=="Firefox" || BrowserDetect.browser=="Chrome"){
				var status = colData.textContent;
			}else{
				var status = colData.innerText;
			}
			
			if (row.style.display != "none") {
				retCount++;
				if (status == "LIVE") {
					count["LIVE"] = count["LIVE"] + 1;
				} else {
					if (status == "IDLE") {
						count["IDLE"] = count["IDLE"] + 1;
					} else {
						if (status == "NO SALE") {
							count["NO SALE"] = count["NO SALE"] + 1;
						} else {
							if (status == "TERMINATE") {
								count["TERMINATE"] = count["TERMINATE"] + 1;
							}else{
								console.log('INSIDE NO TERMINAL');
								count["NO TERMINAL"] = count["NO TERMINAL"] + 1;
							}
						}
					}
				}
			}
		}
	}
	if (retCount == 0) {
		retCount = 1;
	}
}
function convertDate(timeData) {
	if (timeData.indexOf("version") != -1) {
		return timeData.substring(0, timeData.length - 7);
	}
	if (isNaN(timeData)) {
		return timeData;
	} else {
		if (timeData == 0) {
			return "N.A.";
		}
	}
	d = new Date(parseInt(timeData));
	utc = d.getTime() + (d.getTimezoneOffset() * 60000);
	nd = new Date(utc + (offset));
	return (nd.toString().substring(0, nd.toString().lastIndexOf(":") + 3));
}
function fetchSoldEntryData() {
	_id.o("resultDiv").innerHTML = "";
	if (_id.v("retOrgName") != -1) {
		var _resp = _ajaxCall("bo_rep_soldBookEntry_Result.action?retOrgId=" + _id.v("retOrgName") + "&curRemaining=" + _id.v("bookType"));
		_id.i("resultDiv", _resp.data);
	}
}
parent.frames[0].Version["$Source: /rep/LMS_Mgmt/WebRoot/com/skilrock/lms/web/reportsMgmt/backOffice/js/activityRep.js,v $"] = "$Id: activityRep.js,v 1.3 2016/10/31 09:46:26 neeraj Exp $";

