var i18nObj = parent.frames[0].i18Obj;

var curCheckStatus = null;
var count = {"LIVE":0, "IDLE":0, "NO SALE":0,"New Login":0};
var retCount = 0;
var mapcolor = "FF0000";
var serTime = "";
var isDG = "false";   
var isCS = "false";    
var isSE = "false";
var isSLE = 'false';
var isIW = 'false';
var isVS = 'false';
var isOLA = "false";
var isSale = "false";
var isPwt = "false";
var isLogin = "false";
var isHeartBeat = "false";
var countMethod = "percent"; 
var activeTime = (1000 * 60 * 60 * 2 * 1);// Millisecond*Second*Minute*Hour*Days
var idleTime = (1000 * 60 * 60 * 14 * 1);
//var statusArr = ["LIVE", "IDLE", "NO SALE" ,"TERMINATE" , "NO TERMINAL","New Login"];
var statusArr = [i18nObj.prop('LIVE'), i18nObj.prop('IDLE'), i18nObj.prop('NO_SALE'), i18nObj.prop('TERMINATE'), i18nObj.prop('NO_TERMINAL'), i18nObj.prop('label.newLogin')];
var colCount = 9;// This is the no. of column count before Last Sale
					// excluding (S.no).

function getAgentList() {
	var text = _ajaxCall(path
			+ "/com/skilrock/lms/web/userMgmt/getAgentNameList.action?orgType=AGENT");
	var firstArr = text.data.split(":");
	var retObj = _id.o("agtOrgName");
	for ( var i = 0; i < firstArr.length - 1; i++) {
		var retNameArray = firstArr[i].split("|");
		var opt = new Option(retNameArray[0], retNameArray[1]);
		retObj.options[i + 1] = opt;
	}
}



function filterSimType(){
	var activityType = _id.v("terminal");
	if(activityType=="PC" || activityType.match("ICT")){
		_id.o('conDiv').style.display = "none";
	} else{
		_id.o('conDiv').style.display = "block";
	}
	 filterData();
}

function fetchActData(agtId) {
	document.getElementById('reportDiv').style.display = 'block';
	document.getElementById('expDiv').style.display = 'block';
	_id.o("resultDiv").innerHTML = "";
	_id.o("reportDiv").innerHTML = "";
	var selectMode = _id.v("selectMode");
	var activityType = _id.v("activityType");
	var _resp = _ajaxCall("bo_rep_new_retActivity_Result.action?agentOrgId="
			+ agtId + "&selectMode=" + selectMode + "&activityType="
			+ activityType);
	_id.i("resultDiv", _resp.data);
	 filterData();
}
function fetchActDataForAgt() {
	document.getElementById('reportDiv').style.display = 'block';
	_id.o("resultDiv").innerHTML = "";
	_id.o("reportDiv").innerHTML = "";
	var selectMode = _id.v("selectMode");
	var activityType = _id.v("activityType");
	var _resp = _ajaxCall("agt_rep_new_retActivity_Result.action?"
			+"&selectMode=" + selectMode + "&activityType="
			+ activityType);
	_id.i("resultDiv", _resp.data);
	 filterData();
}
function filterDataAtModeBased() {
	var agentId = _id.v("agtOrgName");
	var sMode = _id.v("selectMode");
	if (sMode == 'ACTIVITY') {
		_id.o('statusTab').style.display = "block";
		_id.o('actDiv').style.display = "block";
		_id.o("sliderDiv").style.display = "block";
		fetchActData(agentId);
	} else if(sMode == "HEARTBEAT"){
			_id.o('statusTab').style.display = "block";
			_id.o('actDiv').style.display = "none";
			_id.o("sliderDiv").style.display = "block";
			fetchActData(agentId);
		} else {
		_id.o("sliderDiv").style.display = "none";
		_id.o('actDiv').style.display = "none";
		_id.o('statusTab').style.display = "none";
		fetchActData(agentId);
	}
}
function filterDataAtModeBasedForAgent() {
	var agentId = _id.v("agtOrgName");
	var sMode = _id.v("selectMode");
	if (sMode == 'ACTIVITY') {
		_id.o('statusTab').style.display = "block";
		_id.o('actDiv').style.display = "block";
		_id.o("sliderDiv").style.display = "block";
		fetchActDataForAgt();
	} else if(sMode == "HEARTBEAT"){
			_id.o('statusTab').style.display = "block";
			_id.o('actDiv').style.display = "none";
			_id.o("sliderDiv").style.display = "block";
			fetchActDataForAgt();
		} else {
		_id.o("sliderDiv").style.display = "none";
		_id.o('actDiv').style.display = "none";
		_id.o('statusTab').style.display = "none";
		fetchActDataForAgt();
	}
}

function filterData() {
	isDG = _id.v("DG");
	isSE = _id.v("SE");
	isSLE = _id.v("SLE");
	isIW = _id.v("IW");
	isVS = _id.v("VS");
	isLogin = _id.v("Login");
	var activityType = _id.o("activityType").value;
	if(activityType == "LOGIN"){
		isLogin = "true";
	}
	var valueA = _id.v("valueA");
	var valueB = _id.v("valueB");
	activeTime = valueA * 1000*60*60;
	idleTime = valueB * 1000*60*60;
	fillCurSelStatus();
	count["LIVE"] = 0;
	count["IDLE"] = 0;
	count["NO SALE"] = 0;
	count["New Login"] = 0;
	createReport(_id.o("reportData").innerHTML);
	var table = document.getElementById("activityTab");
	var rowCount = table.rows.length;
	var sMode = _id.v("selectMode");
	var table1 = document.getElementById("mapTable");
	var rowCount1 = table1.rows.length;
	var alCol = document.getElementsByName("alStatus");
	var locCol = _id.o("location").value;
	var terminalType = _id.o("terminal").value;
	var conType = _id.o("conMode").value;
	var agentKey = _id.o("agtOrgName").selectedIndex;
	var agentId = _id.o("agtOrgName").options;
	agentValue = agentId[agentKey].text;
	var md = "";
	var j = 0;
	if (alCol.length != 0) {
		for (var i = 3; i < rowCount; i++) {
			var row = table.rows[i];
			var row1 = table1.rows[i-3];
			var colData = alCol[i-3];
			row.className ="startSortable";
			if(BrowserDetect.browser=="Safari" || BrowserDetect.browser=="Firefox" || BrowserDetect.browser=="Chrome"){
				
				var mData = "";
				for (var j = 0; j < 4; j++) {
					mData += row1.textContent.split(',')[j] + ":";
				}
				if(agentValue != 'ALL'){ 
					if (!(row.textContent).match(agentValue)) {
						row.style.display = "none";
						mData = "";
					}
				}
				if (sMode != 'TERMINATE' && sMode!='NO TERMINAL') {	
						if (!selStatus(colData.textContent)) {
							row.style.display = "none";
							mData = "";
						}
				}
				
				if (locCol != "ALL" ) {
					if (!(row.textContent).match(locCol)) {
						row.style.display = "none";
						mData = "";
					}
				}
				
				if(terminalType != "ALL"){
					var terM = new RegExp("\\b"+terminalType+"\\b");
					//	var terM=/\b'+terminalType+'\b/;
					if (!(row.textContent).match(terM)) {
						row.style.display = "none";
						mData = "";
					}
				}
			
				if(conType != "ALL"){
		
					if (!(row.textContent).match(new RegExp(conType,"i"))) {
						row.style.display = "none";
						mData = "";
					}
				}
			}else{
				
				if(agentValue != i18nObj.prop('label.ALL')){
					if (!(row.textContent).match(agentValue) && agentValue != "ALL") {
						row.style.display = "none";
						mData = "";
					}
				}
				
				if (sMode != 'TERMINATE' && sMode!='NO TERMINAL') {	
					if (!selStatus(colData.innerText)) {
						row.style.display = "none";
						mData = "";
					}
				}
				
				if (locCol != 'ALL') {
					if (!(row.innerText).match(locCol)) {
						row.style.display = "none";
						mData = "";
					}
				}
				
				if(terminalType != 'ALL'){
					var terM = new RegExp("\\b"+terminalType+"\\b");
					//var terM='/\\b'+terminalType+'\\b/';
					if (!(row.innerText).match(terM)) {
						row.style.display = "none";
						mData = "";
					}
				}
				
				if(conType != 'ALL'){
		
					if (!(row.textContent).match(new RegExp(conType,"i"))) {
						row.style.display = "none";
						mData = "";
					}
				}
			}
			md+= mData+",";
		}			
		countStatus();
	}
	_id.i("mapData",md);
	if (countMethod == "count") {
		_id.i("activeCnt", count["LIVE"]);
		_id.i("idleCnt", count["IDLE"]);
		_id.i("nosaleCnt", count["NO SALE"]);
		_id.i("newLoginCnt", count["New Login"]);

		$("#activeCount").val(count["LIVE"]);
		$("#idleCount").val(count["IDLE"]);
		$("#noSaleCount").val(count["NO SALE"]);
		$("#newLoginCount").val(count["New Login"]);
	} else {
		if (countMethod == "percent") {
			_id.i("activeCnt", (count["LIVE"]));
			_id.i("idleCnt", (count["IDLE"]));
			_id.i("nosaleCnt", (count["NO SALE"]));
			_id.i("newLoginCnt", count["New Login"]);

			$("#activeCount").val(count["LIVE"]);
			$("#idleCount").val(count["IDLE"]);
			$("#noSaleCount").val(count["NO SALE"]);
			$("#newLoginCount").val(count["New Login"]);
		}
	}
	sortables_init();
	return true;
}

function selStatus(colData) {
	var str = colData.trim();
	for (var i = 0; i < curCheckStatus.length; i++) {
		if (curCheckStatus[i] == str) {
			return true;
		}
	}
	return false;
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

function createReport(data) {
	_id.o("reportDiv").innerHTML = "";
	retCount = 0;
	serTime = _id.o("curDate").innerHTML;
	var dataArr = data.split(",");
	var colCount = 1;
	var repData = "";
	var mapData = "";
	
	if (isDG == "true") {
		repData += "<th rowspan=\"2\" colspan=\"6\"  align=\"center\">"+i18nObj.prop('ALERT')+"</th></tr><tr><th align=\"center\">"+i18nObj.prop('label.last.sale')+"</th>";
		repData += "<th align=\"center\">"+i18nObj.prop('label.last.pwt')+"</th>";
		repData +="<th align=\"center\">"+i18nObj.prop('label.days.since.last.sale')+"</th>";
		colCount++;
	}
	if (isIW == "true") {
		repData += "<th align=\"center\">"+i18nObj.prop('label.last.sale')+"</th>";
		repData += "<th align=\"center\">"+i18nObj.prop('label.last.pwt')+"</th>";
		repData += "<th align=\"center\">"+i18nObj.prop('label.days.since.last.sale')+"</th>";
		colCount+3;
	}
	if (isVS == "true") {
		repData += "<th align=\"center\">"+i18nObj.prop('label.last.sale')+"</th>";
		repData += "<th align=\"center\">"+i18nObj.prop('label.last.pwt')+"</th>";
		repData += "<th align=\"center\">"+i18nObj.prop('label.days.since.last.sale')+"</th>";
		colCount+3;
	}

	if (isSE == "true") {
		repData += "<th align=\"center\">"+i18nObj.prop('label.last.sale')+"</th>";
		repData += "<th align=\"center\">"+i18nObj.prop('label.last.pwt')+"</th>";
		colCount = colCount+2;
	}
	if (isSLE == "true") {
		repData += "<th align=\"center\">"+i18nObj.prop('label.last.sale')+"</th>";
		repData += "<th align=\"center\">"+i18nObj.prop('label.last.pwt')+"</th>";
		repData +="<th align=\"center\">"+i18nObj.prop('label.days.since.last.sale')+"</th>";
		colCount = colCount+3;
	}
	repData += "</tr>";
	startData = "<table border=\"1\"  id=\"activityTab\" cellpadding=\"2\" width=\"95%\" cellspacing=\"0\" bordercolor=\"#CCCCCC\" align=\"center\"><tr><td align=\"center\" colspan=\"";
	var mapTable = "<table id=\"mapTable\">";
	globalColCount = colCount;

	var retailer = tierMap["RETAILER"];
	var agent = tierMap["AGENT"];
		startData += (globalColCount + 14) + "\"><b>"+i18nObj.prop('label.rep.gen.time')+":</b> " + _id.o("repGenTime").innerHTML + "</td></tr><tr><th rowspan=\"2\"  >" + retailer + i18nObj.prop('label.name')+"</th><th rowspan=\"2\" >" + agent + i18nObj.prop('label.name')+"</th><th rowspan=\"2\" >"+ i18nObj.prop('label.location')+"</th>";
		startData += "<th rowspan=\"2\" align=\"center\">"+i18nObj.prop('label.tid')+"</th>";
		startData += "<th rowspan=\"2\" align=\"center\">"+i18nObj.prop('label.pos.version')+"</th>";
		startData += "<th rowspan=\"2\" align=\"center\">"+i18nObj.prop('label.last.conn.mode')+"</th>";
		startData += "<th rowspan=\"2\">"+i18nObj.prop('label.user.status')+"</th>";
		if(isLogin == "true"){
		startData += "<th rowspan=\"2\">"+i18nObj.prop('label.login.status')+"</th>";
		}
		startData += "<th rowspan=\"2\" align=\"center\">"+i18nObj.prop('label.last.heart.beat')+"</th>";
		startData += "<th rowspan=\"2\" align=\"center\">"+i18nObj.prop('label.last.login')+"</th>";
		if(isDG == "true"){
		startData += "<th align=\"center\" colspan=\"3\">"+i18nObj.prop('label.draw.game')+"</th>";
		}
		if (isSE == "true") {
			startData += "<th align=\"center\" colspan=\"2\">"+i18nObj.prop('label.sc.game')+"</th>";
		}
		if (isIW == "true") {
			startData += "<th align=\"center\" colspan=\"3\">IW</th>";
		}
		if (isVS == "true") {
			startData += "<th align=\"center\" colspan=\"3\">VS</th>";
		}
		if (isSLE == "true") {
			startData += "<th align=\"center\" colspan=\"3\">"+i18nObj.prop('label.sports.lot')+"</th>";
		}
		repData = startData + repData;
		for (var i = 0; i < dataArr.length - 1; i++) {
			var rowData = dataArr[i].split(":");
			repData ="<tr >"+ fillRowData(rowData, repData) + "</tr>";
			mapTable +="<tr>"+prepareMapData(rowData)+"</tr>";
		}
	if (data == "") {
		repData += "<tr><td colspan=\"" + (colCount + 5) + "\" align=\"center\">"+i18nObj.prop('msg.no.record')+"</td></tr>";
	}
	repData += "</table>";
	mapTable += "</table>";
	_id.i("reportDiv", repData);
	_id.i("mapDiv", mapTable);
}

function prepareMapData(mapData,mapTable){
	var dt = "";
	for (var j = 0; j < 3; j++) {
		dt += "<td>"+mapData[j] + ",</td>";
	}
	dt+="<td>"+mapcolor+",</td>";
	return dt;
}


function countStatus() {
	var table = document.getElementById("activityTab");
	var rowCount = table.rows.length;
	var alCol = document.getElementsByName("alStatus");
	var j = 0;
	retCount = 0;
	if (alCol.length != 0) {
		for (var i = 3; i < rowCount; i=i+1) {
			var row = table.rows[i];
			var colData = alCol[i - 3];
			if(BrowserDetect.browser=="Safari" || BrowserDetect.browser=="Firefox" || BrowserDetect.browser=="Chrome"){
				var status = colData.textContent;
			}else{
				var status = colData.innerText;
			}
			
			if (row.style.display != "none") {
				retCount=retCount+1;
				if (status == i18nObj.prop('LIVE')) {
					count["LIVE"] = count["LIVE"] + 1;
				} else {
					if (status == i18nObj.prop('IDLE')) {
						count["IDLE"] = count["IDLE"] + 1;
					} else {
						if (status == i18nObj.prop('NO_SALE')) {
							count["NO SALE"] = count["NO SALE"] + 1;
						} else {
							if(status == i18nObj.prop('label.newLogin')){
								count["New Login"] = count["New Login"] + 1;
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
function fillRowData(rowData, repData) {
	var p = 1;
	var lastTime = "";
	var lastLoignTime="";
	var lastSaleTime="";
	var lastPwtTime ="";
	var newLogin ="false";
	
	var activityType = _id.o("activityType").value;
	for (var j = 3; j < rowData.length - p; j++) {
		if(isLogin == "false" && j == 10){
			j = j+1;
		} 
		// for Days Since Last DG SALE
		if( j == 15 && rowData[15]==-1){
			repData += "<td> </td>";
					
		}else if( j == 18 && rowData[18]==-1){
			repData += "<td> </td>";
		}else{
			repData += "<td>" + rowData[j] + "</td>";
		}
	}
	lastLoignTime =parseDate(rowData[12]);
	lastSaleTime=parseDate(rowData[13]);
	lastPwtTime=parseDate(rowData[14]);
	if(activityType=="LOGIN"){
		lastTime = lastLoignTime;
		
	} else if(activityType=="SALE"){
		lastTime =lastSaleTime;
		
	} else if(activityType=="PWT"){
		lastTime = lastPwtTime;
		
	} else {
		
			lastTime = parseDate(rowData[11]);
	}

	

	var midData = ""; 
	//alert(rowData[9]);
	if(lastLoignTime<=0 ||(lastSaleTime<=0&&lastPwtTime<=0)){
		midData += alertStatus(-1,rowData[9]);// new Login
	}else{
		midData += alertStatus(lastTime,rowData[9]);
	}
	


	
	return (repData + midData);
}

function parseDate(input) {
if(input==0){
	input='0000:00:00 00:00:00';
}
var parts = input.match(/(\d+)/g);
//return   new Date(parts[0], parts[1]-1,parts[2], parts[3], parts[4], parts[5], 0).getTime();
return   Date.UTC(parts[0], parts[1]-1,parts[2], parts[3], parts[4], parts[5], 0);
 // return new Date(parts[0], parts[1]-1, parts[2]); // months are 0-based
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

function alertStatus(lastTime,userStatus) {
	
	
	
	var sMode = _id.v("selectMode");
	//alert(sMode);
/*	if (sMode == 'HEARTBEAT') {
		activeTime = (1000 * 60 * 60 * 1 * 1);
		idleTime = (1000 * 60 * 60 * 3 * 1);
	}*/
	if (sMode == 'TERMINATE') {
		mapcolor = "778899";
		if(userStatus.trim() == 'TERMINATE'){
			return "<td style=\"background:LightSlateGrey ;color:white\" name=\"alStatus\"  id=\"alStatus\" >" + statusArr[3] + "</td>";				
		
		}else if(userStatus.trim() == 'BLOCK'){
			return "<td style=\"background:LightSlateGrey ;color:white\" name=\"alStatus\"  id=\"alStatus\" >" + 'BLOCK' + "</td>";				

		}
		}

	if (sMode == 'NO TERMINAL') {
		mapcolor = "9370D8";
		return "<td style=\"background:MediumPurple ;color:white\" name=\"alStatus\"  id=\"alStatus\" >" + statusArr[4] + "</td>";				
	}
	if(lastTime==-1){
		mapcolor = "1E90FF";
		return "<td style=\"background:DodgerBlue;color:white\" name=\"alStatus\" id=\"alStatus\">" + statusArr[5] + "</td>";
	}else {
		var timeDiff = serTime - lastTime;
		if (timeDiff < activeTime) {
				mapcolor = "00FF00";
				return "<td style=\"background:green;color:white\" name=\"alStatus\"  id=\"alStatus\" >" + statusArr[0] + "</td>";				
		} 
		else {
			if (timeDiff > activeTime && timeDiff < idleTime) {
				mapcolor = "FFFF00";
				return "<td style=\"background:yellow;color:black\" name=\"alStatus\" id=\"alStatus\" >" + statusArr[1] + "</td>";
			}
				else {
				mapcolor = "FF0000";
				return "<td style=\"background:red;color:white\" name=\"alStatus\" id=\"alStatus\">" + statusArr[2] + "</td>";
				}
			}
		
	}
	
}


parent.frames[0].Version['$Source: /rep/LMS_Mgmt/WebRoot/com/skilrock/lms/web/reportsMgmt/backOffice/js/newActivityRep.js,v $'] = '$Id: newActivityRep.js,v 1.3 2016/10/31 09:46:26 neeraj Exp $';