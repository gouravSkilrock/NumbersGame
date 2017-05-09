
var actionVal = "";
function setData(actionval) {
	document.getElementById("changeNotificationDiv").innerHTML = "";
	document.getElementById("actionTypeId").value = actionval;
	actionVal = actionval;
}
var radioVal = "";
function hideShow(freezeTimeType, conType) {
	for (var i = 0; i < document.getElementsByName(freezeTimeType).length; i++) {
		var e = document.getElementsByName(freezeTimeType)[i];
		if (e.checked == true) {
			radioVal = e.value;
		}
	}
	if (document.getElementById("single" + conType + "Tr").style.display == "block") {
		document.getElementById("single" + conType + "Tr").style.display = "none";
		document.getElementById("multiple" + conType + "Tr").style.display = "block";
	} else {
		document.getElementById("single" + conType + "Tr").style.display = "block";
		document.getElementById("multiple" + conType + "Tr").style.display = "none";
	}
}
function hideShow1(freezeTimeType, conType) {
	for (var i = 0; i < document.getElementsByName(freezeTimeType).length; i++) {
		var e = document.getElementsByName(freezeTimeType)[i];
		if (e.checked == true) {
			radioVal = e.value;
		}
	}
}
var req;
function processChangeFreezeSchedule(drawId, conType, gameNo, freezeTime) {
	document.getElementById("changeNotificationDiv").innerHTML = "";
	document.getElementById("ChngFrzResultDiv").innerHTML = "";
	var gameNo = document.getElementById(gameNo).value;
	var newDrawId = document.getElementById(drawId).value;
	var freezeTime = document.getElementById(freezeTime).value;
	var url = "";
	var fromDate = document.getElementById(conType + "FromDate").value;
	var toDate = document.getElementById(conType + "ToDate").value;
	var fromHr = document.getElementById(conType + "FromHour").value;
	var fromMin = document.getElementById(conType + "FromMin").value;
	var fromSec = document.getElementById(conType + "FromSec").value;
	var toHr = document.getElementById(conType + "ToHour").value;
	var toMin = document.getElementById(conType + "ToMin").value;
	var toSec = document.getElementById(conType + "ToSec").value;
	if (radioVal == "multiple_freeze") {
		url = "getDrawSchdule.action?actionValue=" + actionVal + "&freezeChangeType=" + radioVal;
		url = url + "&freezeFromDate=" + fromDate + "&freezeToDate=" + toDate + "&freezeFromHour=" + fromHr + "&freezeFromMin=" + fromMin;
		url = url + "&freezeFromSec=" + fromSec + "&freezeToHour=" + toHr + "&freezeToMin=" + toMin + "&freezeToSec=" + toSec + "&gameNo=" + gameNo + "&freezeTime=" + freezeTime + "&freezeDrawId=" + newDrawId;
	} else {
		url = "getDrawSchdule.action?actionValue=" + actionVal + "&freezeChangeType=" + radioVal + "&gameNo=" + gameNo + "&freezeDrawId=" + newDrawId + "&freezeTime=" + freezeTime;
	}
	if (window.XMLHttpRequest) { // Non-IE browsers
		req = new XMLHttpRequest();
		req.onreadystatechange = processStateChangeFreeze;
		try {
			req.open("GET", url, true);
			req.setRequestHeader("If-Modified-Since", new Date().getTime());
		}
		catch (e) {
		}
		req.send(null);
	} else {
		if (window.ActiveXObject) { // IE
			req = new ActiveXObject("Microsoft.XMLHTTP");
			if (req) {
				req.onreadystatechange = processStateChangeFreeze;
				req.open("GET", url, true);
				req.setRequestHeader("If-Modified-Since", new Date().getTime());
				req.send();
			}
		}
	}
	
//}
}
function processStateChangeFreeze() {
	if (req.readyState == 1) {
		document.getElementById("chngFrzLoadingDiv").style.display = "block";
	}
	if (req.readyState == 4) { // Complete
		if (req.status == 200) { // OK response  		
			var isChange = req.responseText;
			document.getElementById("chngFrzLoadingDiv").style.display = "none";
			document.getElementById("ChngFrzResultDiv").style.display = "block";
			document.getElementById("ChngFrzResultDiv").innerHTML = isChange;
		} else {
			alert("Problem: " + req.statusText);
		}
	}
}
var cancelReq;
function processCancelDrawSchedule(drawId, conType, gameNo, cancelMoneyStatus) {
	document.getElementById("cancelResultDiv").innerHTML = "";
	var gameNo = document.getElementById(gameNo).value;
	var drawId = document.getElementById(drawId).value;
	var fromDate = document.getElementById(conType + "FromDate").value;
	var toDate = document.getElementById(conType + "ToDate").value;
	var fromHr = document.getElementById(conType + "FromHour").value;
	var fromMin = document.getElementById(conType + "FromMin").value;
	var fromSec = document.getElementById(conType + "FromSec").value;
	var toHr = document.getElementById(conType + "ToHour").value;
	var toMin = document.getElementById(conType + "ToMin").value;
	var toSec = document.getElementById(conType + "ToSec").value;
	var cancelMoneyStatus = document.getElementById(cancelMoneyStatus).value;
	var url = "getDrawSchdule.action?cancelDrawId=" + drawId + "&actionValue=" + actionVal + "&cancelDrawType=" + radioVal + "&cancelMoneyStatus=" + cancelMoneyStatus;
	url = url + "&cancelFromDate=" + fromDate + "&cancelToDate=" + toDate + "&cancelFromHour=" + fromHr + "&cancelFromMin=" + fromMin;
	url = url + "&cancelFromSec=" + fromSec + "&cancelToHour=" + toHr + "&cancelToMin=" + toMin + "&cancelToSec=" + toSec + "&gameNo=" + gameNo;
	if (window.XMLHttpRequest) { // Non-IE browsers
		cancelReq = new XMLHttpRequest();
		cancelReq.onreadystatechange = processStateCancelDraw;
		try {
			cancelReq.open("GET", url, true);
			cancelReq.setRequestHeader("If-Modified-Since", new Date().getTime());
		}
		catch (e) {
		}
		cancelReq.send(null);
	} else {
		if (window.ActiveXObject) { // IE
			cancelReq = new ActiveXObject("Microsoft.XMLHTTP");
			if (cancelReq) {
				cancelReq.onreadystatechange = processStateCancelDraw;
				cancelReq.open("GET", url, true);
				cancelReq.setRequestHeader("If-Modified-Since", new Date().getTime());
				cancelReq.send();
			}
		}
	}
	
//}
}
function processStateCancelDraw() {
	if (cancelReq.readyState == 1) {
		document.getElementById("cancelLoadingDiv").style.display = "block";
	}
	if (cancelReq.readyState == 4) { // Complete
		if (cancelReq.status == 200) { // OK response  		
			var isChange = cancelReq.responseText;
			document.getElementById("cancelLoadingDiv").style.display = "none";
			document.getElementById("cancelResultDiv").style.display = "block";
			document.getElementById("cancelResultDiv").innerHTML = isChange;
		} else {
			alert("Problem: " + cancelReq.statusText);
		}
	}
}
var postponeReq;
function processPostponeReqDrawSchedule(drawId, gameNo, postponedMin) {
	document.getElementById("postPoneResultDiv").innerHTML = "";
	var gameNo = document.getElementById(gameNo).value;
	var drawId = document.getElementById(drawId).value;
	var single_fromDate = document.getElementById("postpone_single_postponedDate").value;
	var single_fromHr = document.getElementById("postpone_singleFromHour").value;
	var single_fromMin = document.getElementById("postpone_singleFromMin").value;
	var single_fromSec = document.getElementById("postpone_singleFromSec").value;
	var multiple_fromDate = document.getElementById("postpone_multiple_FromDate").value;
	var multiple_fromHr = document.getElementById("postpone_multipleFromHour").value;
	var multiple_fromMin = document.getElementById("postpone_multipleFromMin").value;
	var multiple_fromSec = document.getElementById("postpone_multipleFromSec").value;
	var multiple_toDate = document.getElementById("postpone_multiple_ToDate").value;
	var multiple_toHr = document.getElementById("postpone_multipleToHour").value;
	var multiple_toMin = document.getElementById("postpone_multipleToMin").value;
	var multiple_toSec = document.getElementById("postpone_multipleToSec").value;
	var postponedMin = document.getElementById(postponedMin).value;
	var url = "getDrawSchdule.action?postponedDrawId=" + drawId + "&actionValue=" + actionVal + "&postponedDrawType=" + radioVal;
	url = url + "&postpone_single_postponedDate=" + single_fromDate + "&postpone_singleFromHour=" + single_fromHr + "&postpone_singleFromMin=" + single_fromMin + "&postpone_singleFromSec=" + single_fromSec;
	url = url + "&postpone_multiple_FromDate=" + multiple_fromDate + "&postpone_multipleFromHour=" + multiple_fromHr + "&postpone_multipleFromMin=" + multiple_fromMin + "&postpone_multipleFromSec=" + multiple_fromSec;
	url = url + "&postpone_multiple_ToDate=" + multiple_toDate + "&postpone_multipleToHour=" + multiple_toHr + "&postpone_multipleToMin=" + multiple_toMin + "&postpone_multipleToSec=" + multiple_toSec;
	url = url + "&postponedMin=" + postponedMin + "&gameNo=" + gameNo;
	if (window.XMLHttpRequest) { // Non-IE browsers
		postponeReq = new XMLHttpRequest();
		postponeReq.onreadystatechange = processStatePostponeDraw;
		try {
			postponeReq.open("GET", url, true);
			postponeReq.setRequestHeader("If-Modified-Since", new Date().getTime());
		}
		catch (e) {
		}
		postponeReq.send(null);
	} else {
		if (window.ActiveXObject) { // IE
			postponeReq = new ActiveXObject("Microsoft.XMLHTTP");
			if (postponeReq) {
				postponeReq.onreadystatechange = processStatePostponeDraw;
				postponeReq.open("GET", url, true);
				postponeReq.setRequestHeader("If-Modified-Since", new Date().getTime());
				postponeReq.send();
			}
		}
	}
	
//}
}
function processStatePostponeDraw() {
	if (postponeReq.readyState == 1) {
		document.getElementById("postPoneLoadingDiv").style.display = "block";
	}
	if (postponeReq.readyState == 4) { // Complete
		if (postponeReq.status == 200) { // OK response  		
			var isChange = postponeReq.responseText;
			document.getElementById("postPoneLoadingDiv").style.display = "none";
			document.getElementById("postPoneResultDiv").style.display = "block";
			document.getElementById("postPoneResultDiv").innerHTML = isChange;
		} else {
			alert("Problem: " + postponeReq.statusText);
		}
	}
}
function showTableSearch(div1, div2) {
	document.getElementById(div1).style.display = "block";
	document.getElementById(div2).style.display = "none";
}
dojo.event.topic.subscribe("/searchDraw", function (data, type, request) {
	alert("call--");
	if (type == "before") {
		document.getElementById("addImageMsgSearch").style.display = "block";
	}
	if (type == "load") {
		document.getElementById("addImageMsgSearch").style.display = "none";
	}
});
function validation() {
	if (document.getElementById("gameNo").value == "") {
		alert("Please Fill Game Number");
		return false;
	}
	
}
parent.frames[0].Version['$Source: /rep/LMS_Mgmt/WebRoot/com/skilrock/lms/web/drawGames/playMgmt/js/drawGame.js,v $'] = '$Id: drawGame.js,v 1.2 2016/10/31 09:46:17 neeraj Exp $';

