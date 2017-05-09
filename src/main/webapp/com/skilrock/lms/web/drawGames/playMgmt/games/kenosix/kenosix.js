
var _kenosixWinArr = new Array();
var _kenosixDrawIdArr = new Array();
var isQP = false;
var curDrwTimekenosix = "";
var curSel = "kenosixRow";
var totalPicked = "1;";
var maxPickedNum = "1";
var qpType = "noOfQp";
var _kenosixPrice = 0;
var isPerm1 = true;
var _kenosixdrawSelect;
var freezeTime = "";
var unitBetTypePriceArrKenoSix = new Object();

/*var myCountdown2 = new Countdown({
	time: 300, 
	width:150, 
	height:80, 
	rangeHi:"minute"	// <- no comma on last item!
});*/

/*function callTimer(timerTime)	{
	//alert('');
	//jQuery.noConflict();
	//alert("in Timer Function");
	//alert(timerTime);
	$('#timerDiv').dsCountDown({
		endDate: new Date(timerTime),
		theme: 'flat'
	});			
}*/

function setDefaultkenosix() {
	isQP = false;
	curDrwTimekenosix = "";
	curSel = "kenosixRow";
	maxPickedNum = "1";
	totalPicked = "20";
	qpType = "noOfQp";
	_kenosixPrice = "10.0";
}

function _kenosixWinAppend() {
	if (!_kenosixWin.match("Result Awaited")) {
		_kenosixWin = "Result Awaited=\"\" Nxt" + _kenosixWin;
	}
}

function _kenosixLatestDrawTime() {
	return _kenosixStatus.split("=")[1];
}

function _kenosixFillWinTbl() {
	var _kenosixWinHt = "<table width=\"100%\" border=\"0\" bordercolor=\"red\" cellspacing=\"0\" cellpadding=\"0\" align=\"center\" bgcolor=\"#9cceff\"><tr><td colspan=\"2\" align=\"left\"><div valign=\"middle\" style=\"height:22px;background-color:#00639c;padding-top:3px;font-size:16px;font-family:Arial;font-weight:bold;color:#FFFFFF\">&nbsp;Win Result&nbsp;</div></td></tr>      <tr><td valign=\"top\" align=\"center\" height=\"100\">";
	var winArr = _kenosixWin.split("Nxt");
	var _kenosixWinHTML = "<table width=\"100%\" border=\"0\" bordercolor=\"red\" style=\"margin-top:3px\"><tr><td><div style=\"" + scrollColor("#9CCEFF", "#00639c", "184") + "\">";
	for (var i = 0; i <= winArr.length - 2; i++) {
		if (!winArr[i].match("Result")) {
			var time = new Date(parseInt(winArr[i].split("=")[0].replace(" ", "")));
			var newTime = week[time.getDay()] + " " + time.getDate() + " " + month[time.getMonth()] + "<br/>" + (time.getHours() < 10 ? "0" + time.getHours() : time.getHours()) + ":" + (time.getMinutes() < 10 ? "0" + time.getMinutes() : time.getMinutes()) + ":" + (time.getSeconds() < 10 ? "0" + time.getSeconds() : time.getSeconds());
			//alert(newTime);
			var htm = "<table width=\"90%\" border=\"0\" bordercolor=\"green\" id=\"result" + i + "\" cellspacing=\"2\" cellpadding=\"10\" class=\"kenosix_winningResult_table\" ><tr><td class=\"kenosixwinningResultTime\" colspan=\"8\" height=\"53\">" + newTime + "</td></tr><tr>";
			var num = (winArr[i].split("=")[1]).split(",");
			var k = 0, j = 0;
			for(k=0; k<num.length;){
				htm = htm + "<tr>";
				for (j = k; j < k+5; j++) {
					var temp = (num[j] < 10 ? "0" + num[j] : num[j]);
					htm = htm + "<td width=\"10%\" class=\"selected_win_kenosix_numbers\" align=\"center\">" + temp + "</td>";
				}
				k = j;
				htm = htm + "</tr>";
			}
			htm = htm + "</tr></table>";
			_kenosixWinHTML = _kenosixWinHTML + htm;
		} else {
			var htm = "<table width=\"90%\" bordercolor=\"blue\" border=\"0\" cellspacing=\"2\" cellpadding=\"10\" id=\"result" + i + "\" class=\"kenosix_winningResult_table\" ><tr><td class=\"kenosixwinningResultTime\" colspan=\"8\" height=\"53\" >Result Awaited</td></tr><tr>";
			htm = htm + "<td class=\"selected_win_kenosix_numbers\"  colspan=\"8\"><div style=\"text-align:center\" id=\"winAwaitedDiv\" style=\"color:#00466E\"></div></td>";
			htm = htm + "</tr></table>";
			_kenosixWinHTML = _kenosixWinHTML + htm;
		}
	}
	_kenosixWinHTML = _kenosixWinHTML + "</div></td></tr></table>";
	return _kenosixWinHt + _kenosixWinHTML + "</td></tr></table>";
}
function _kenosixFillNxtDrw() {
	var nxtDrwHTML = "<table width=\"100%\" cellspacing=\"0\"cellpadding=\"0\"  border=\"0\" bordercolor=\"red\"><tr><td colspan=\"2\" align=\"left\" valign=\"top\"><div valign=\"middle\" style=\"height:22px;width:180px;background-color:#00639c;padding-top:3px;font-size:16px;font-family:Arial;font-weight:bold;color:#FFFFFF\">&nbsp;Next Draw&nbsp;</div></td></tr><tr><td align=\"center\"valign=\"top\">";
	_kenosixdrawSelect = '<table width="auto" cellspacing="0" cellpadding="0" style="border:1px solid #00639c;box-shadow: 0.5px 0.5px 2px 2px #D8D8D8;"><tr><td align="center" colspan="3" style="background-color:#00639c;" colspan="3" align="center" class="kenosixtxtStyle"><b>Select Draws</td></tr>';
	
	var _kenosixNxtDrwHTML = "<table style=\"background-color:#9cceff\" width=\"100%\" border=\"0\" bordercolor=\"red\" style=\"margin-top:3px\"><tr><td><div style=\"margin-top:3px;" + scrollColor("#9CCEFF", "#00639c", "259") + "\">";
	var nxtDrwArr = _kenosixDrawTime.split(",");
	for (var i = 0, l = nxtDrwArr.length; i < l; i++) {
		var time = new Date(parseInt(nxtDrwArr[i].replace(" ", "")));
		var newTime = week[time.getDay()] + " " + time.getDate() + " " + month[time.getMonth()] + " " + (time.getHours() < 10 ? "0" + time.getHours() : time.getHours()) + ":" + (time.getMinutes() < 10 ? "0" + time.getMinutes() : time.getMinutes());
		var hiddenTime = "<input type='text' id='timer" + i + "'>";
		var rowcolor;
		var data = "";
		//alert(newTime.length);
		if (i == 0) {
			data = time.getFullYear() + "-" + time.getMonth() + "-" + time.getDate() + "-" + (time.getHours() < 10 ? "0" + time.getHours() : time.getHours()) + "-" + (time.getMinutes() < 10 ? "0" + time.getMinutes() : time.getMinutes()) + "-" + (time.getSeconds() < 10 ? "0" + time.getSeconds() : time.getSeconds());
			var dateFuture1 = new Date(time.getFullYear(), time.getMonth(), time.getDate(), (time.getHours() < 10 ? "0" + time.getHours() : time.getHours()), (time.getMinutes() < 10 ? "0" + time.getMinutes() : time.getMinutes()), (time.getSeconds() < 10 ? "0" + time.getSeconds() : time.getSeconds()));
			_kenosixNxtDrawTime = ("" + time).substring(0, ("" + time).lastIndexOf(":") + 3);
		}
		if (i % 2 == 0) {
			rowcolor = "#9CCEFF";
		} else {
			rowcolor = "#639CCE";
		}
		
		nxtDrwHTML = nxtDrwHTML + "<tr><td width=\"100%\"><input type=\"hidden\" value=\"" + data + "\" id=\"" + i + "timer\"></td></tr><tr style=\"display:none\" bgcolor=\"" + rowcolor + "\" style=\"height: 20px\"><td width=\"100%\" align=\"center\" class=\"borderbtmkenosix\" colspan=\"2\" style=\"display:none\"><div id=\"" + i + "blink\" >" + newTime + "</div></td><td width=\"44%\" align=\"center\"><p><div id=\"nxtDrwD" + i + "\" style=\"display:none\"><img src=\"" + fortImgPath + "freezed.gif\"></div></p></td></tr>";
		drawName = _kenosixDrawIdObj[parseInt(nxtDrwArr[i].replace(" ", ""))].split(',')[1];
		if(drawName == 'null'){
			drawName = 'DRAW';
		}
		var htm = "<table width=\"90%\" border=\"0\" bordercolor=\"green\" id=\"nxtdraw" + i + "\" cellspacing=\"2\" cellpadding=\"10\" class=\"kenosix_nxtDrw_table\"><tr><td width=\"60%\" height=\"5\" style=\"text-align:left;\" class=\"kenosixwinningResultTime\"><font size=\"2\">"+newTime.substring(0, newTime.length-6)+"</font></td><td height=\"5\" class=\"kenosixwinningResultTime\" colspan=\"2\" style=\"text-align:right\"><font size=\"2\">"+newTime.substring(newTime.length-5, newTime.length)+"</font></td></font></td></tr><tr><tr><td width=\"10%\" colspan=\"3\" class=\"selected_nxtDrw_kenosix_drawname\" align=\"center\"><font size=\"2\">" + drawName + "</font></td></tr></tr></table>";
		_kenosixdrawSelect = _kenosixdrawSelect + '<tr><td style="background-color:#9CCEFF;border-bottom:1px solid #00639c;" align="center"><input type="checkbox" title="'+newTime+'" name="drawIdChkBox" value="'+drawName+'" id="'+ _kenosixDrawIdObj[parseInt(nxtDrwArr[i].replace(" ", ""))].split(',')[0] +'_did"/></td><td width="50%" nowarp="nowrap" style="background-color:#9CCEFF;border-bottom:1px solid #00639c;">'+drawName+'</td><td style="background-color:#9CCEFF;border-bottom:1px solid #00639c;" align="center">'+newTime+'</td></tr>'
		_kenosixNxtDrwHTML = _kenosixNxtDrwHTML + htm;
	}
	_kenosixNxtDrwHTML = _kenosixNxtDrwHTML + "</div></td></tr></table>";
	//nxtDrwHTML = nxtDrwHTML + "</table></div></td></tr></table></td></tr></table>";
	_kenosixdrawSelect = _kenosixdrawSelect + '<tr><td colspan="3" style="background-color:#00639c;"align="right" colspan="3"><input class="button" id="selNonConsecButton" onclick="drawSelectButtonKenoSix()" type="button" value="Select"/></td></tr></table>'
	return nxtDrwHTML + _kenosixNxtDrwHTML + "</td></tr></table>";;
}
function _kenosixUpData(win, drwTime, drwStatus) {
	_kenosixWin = win;
	_kenosixDrawTime = drwTime;
	_kenosixStatus = drwStatus;
}
function _kenosixWinAjx() {
	var url = "fetchWinkenosix.action?frmDate=" + _kenosixWinArr[_kenosixWinArr.length - 1];
	_kenosixWin = _kenosixWin + (winAjaxReq(url)).data;
	_kenosixFillWinTbl();
}
function kenosixNmSel(lstNo) {
	var htm = "<div id=\"\"><table width=\"99%\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\" bgcolor=\"#639cce\" style=\"margin-top:6px;margin-bottom:5px\"><tr>";
	for (var i = 1; i <= lstNo; i++) {
		if (i % 10 == 0) {
			htm = htm + "<td style=\"cursor:pointer;background:url(" + projectName + "/LMSImages/images/shade_white.gif) center no-repeat\" class=\"selected_kenosix_numbers\" onmouseout=\"kenosixonmouseouteffect(this.id)\" onmouseover=\"kenosixonmouseovereffect(this.id)\"  onclick=\"fillNumKenosix(" + i + ")\"  id=\"" + i + "\" >" + (i < 10 ? "0" + i : i) + "</td></tr>";
		} else {
			htm = htm + "<td class=\"selected_kenosix_numbers\"  style=\"cursor:pointer;background:url(" + projectName + "/LMSImages/images/shade_white.gif) center no-repeat\" onmouseout=\"kenosixonmouseouteffect(this.id)\" onmouseover=\"kenosixonmouseovereffect(this.id)\" onclick=\"fillNumKenosix(" + i + ")\"  id=\"" + i + "\"  >" + (i < 10 ? "0" + i : i) + "</td>";
		}
	}
	htm = htm + "</tr></table></div><div style=\"background-color:#00639c;border-top:1px solid #FFFFFF;border-left:1px solid #FFFFFF;border-right:1px solid #FFFFFF;height:41px\" valign=\"middle\"><table style=\"margin-top:5px\"><tbody><tr><td class=\"selected_kenosix_numbers\" style=\"text-align:left; height:15px; width:320px;background: none repeat scroll 0 0 #FFFFFF;border:1px solid #9cceff;border-radius:3px;padding: 5px 5px 5px 3px;\"><font size=\"2\">Next Draw &nbsp;&nbsp;&nbsp; &nbsp;&nbsp;&nbsp; &nbsp;&nbsp;&nbsp; &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; &nbsp;&nbsp;&nbsp;<div id=\"belowNextDraw\" style=\"display:inline;\"></div></font></td></tr></tbody></table></div><div style=\"background-color:#00639c;border-top:1px solid #FFFFFF;border-left:1px solid #FFFFFF;border-right:1px solid #FFFFFF;border-bottom:1px solid #FFFFFF;height:41px\" valign=\"middle\"><table style=\"margin-top:5px\"><tbody><tr><td class=\"selected_kenosix_numbers\" style=\"text-align:center; height:15px; width:320px; border-radius:3px; background: none repeat scroll 0 0 #FFFFFF;border:1px solid #9cceff;padding: 5px 5px 5px 3px;\"><font size=\"2\">Remaining Time in Next Draw</font></td></tr></tbody></table></div><div align=\"center\" id=\"emptydivMiddle\" class=\"kenosixtxtStyle\" style=\"background-color:#00639c;border:1px solid #FFFFFF;height:63px\"><div style=\"height:15px\"></div><div class=\"kenosixstyle2\" id=\"curDrwTime\" style=\"font-size: 30px; color: white\"></div></div></div>";
	return htm;
}

	

function kenosixonmouseovereffect(id) {
	var isAllPick = false;
	// check filled
	var elms = document.getElementsByName("selNumTxt");
	var countFill = 0;
	for (var l = 0; l < elms.length; l++) {
		if (elms[l].value != "") {
			countFill += 1;
		}
	}
	if (countFill == elms.length) {
		isAllPick = true;
	}
	if (!isQP && !isAllPick) {
		if (_id.o(id).className != "selected_kenosix_numbersSelect") {
			_id.o(id).style.background = "url(" + projectName + "/LMSImages/images/shade_over.gif) left center no-repeat";
			_id.o(id).style.color = "#FFFFFF";
			_id.o(id).className = "selected_kenosix_numbersSelectOMO";
		}
	}
}
function kenosixonmouseouteffect(id) {
	if (_id.o(id).className == "selected_kenosix_numbersSelectOMO") {
		_id.o(id).style.background = "url(" + projectName + "/LMSImages/images/shade_white.gif) left center no-repeat";
		_id.o(id).style.color = "#00639c";
		_id.o(id).className = "selected_kenosix_numbers";
	} else {
		if (_id.o(id).className == "selected_kenosix_numbersSelect") {
			_id.o(id).style.background = "url(" + projectName + "/LMSImages/images/shade_over.gif) left center no-repeat";
			_id.o(id).style.color = "#FFFFFF";
		} else {
			if (_id.o(id).className == "selected_kenosix_numbers") {
				_id.o(id).style.background = "url(" + projectName + "/LMSImages/images/shade_white.gif) left center no-repeat";
				_id.o(id).style.color = "#00639c";
			}
		}
	}
}
function fillNumKenosix(val) {
	var length = 0;
	if (!isQP) {
		var clrChg = true;
		var obj = _id.o(curSel);
		var elms = document.getElementsByName("selNumTxt");
		if (_id.o(val).className == "selected_kenosix_numbersSelect") {
			for (var l = 0; l < elms.length; l++) {
				if (elms[l].value == val) {
					elms[l].value = "";
					elms[l].focus();
					break;
				}
			}
		} else {
			elms = obj.getElementsByTagName("input");
			for (var l = 0; l < elms.length; l++) {
				if (elms[l].value == "") {
					elms[l].value = (val < 10 ? "0" + val : val);
					break;
				}
			}
			for (var l = 0; l < elms.length; l++) {
				if (elms[l].value == "") {
					elms[l].focus();
					break;
				}
			}
		}
		kenosixapplyCSS();
		_kenosixCalAmt();
	}
}

function kenosixRowDirect(playType, maxNum) {
	var htm = "<table width=\"95%\" border=\"0\" cellspacing=\"4\" cellpadding=\"0\">";
	var className = "selected_kenosix";
	
	htm = htm + "<tr style='display:none'><td class=\"kenosixtxtStyle\" align=\"left\"><input type=\"radio\" name=\"kenosixRadio\" id=\"kenosixRadio\" onclick=\"fillSelKenosix('kenosixRow')\" checked='true'/>Number Picked:</td><td><input type=\"text\" class=\"kenosixtextbox\" name=\"noPicked\" id=\"noOfQp\" value=\"0\" maxlength=\"2\" readOnly=\"true\" onkeyup=\"return _kenoSixfillNoOfQp(this, undefined, event)\" onkeydown=\"return _kenoSixfillNoOfQp(this, undefined, event)\" onblur=\"_kenosixCalAmt()\" style=\"width:30px\"/></td></tr>";
	htm = htm + "<tr><td colspan='2'><table width=\"95%\" id='kenosixRow' border=\"0\" cellspacing=\"4\" cellpadding=\"0\"><tr id=\"row" + i + "\">";
	if(maxNum <=5){
	for (var j = 0; j < parseInt(maxNum); j++) {
			htm = htm + "<td><input type=\"text\" name='selNumTxt' class=\"" + className + "\" maxlength=\"2\"  onkeydown=\"return keyPressKenosix(this,event)\" onkeyup=\"return keyPressKenosix(this,event)\"  onblur=\"blurFnKenosix(this)\" onmousedown=\"return disClick(this)\"></td>";
		}
	}else{
		for (var j = 0; j < 5; j++) {
			htm = htm + "<td><input type=\"text\" name='selNumTxt' class=\"" + className + "\" maxlength=\"2\"  onkeydown=\"return keyPressKenosix(this,event)\" onkeyup=\"return keyPressKenosix(this,event)\"  onblur=\"blurFnKenosix(this)\" onmousedown=\"return disClick(this)\"></td>";
		}
		htm = htm + "<tr>";
		for (var j = 5; j < parseInt(maxNum); j++) {
			htm = htm + "<td><input type=\"text\" name='selNumTxt' class=\"" + className + "\" maxlength=\"2\"  onkeydown=\"return keyPressKenosix(this,event)\" onkeyup=\"return keyPressKenosix(this,event)\"  onblur=\"blurFnKenosix(this)\" onmousedown=\"return disClick(this)\"></td>";
		}
		htm = htm + "</tr>";
	}
	htm = htm + "</tr></table></tr></td>";
	return htm + "</table>";
}


function kenosixRow(playType) {
	var htm = "<table width=\"95%\" border=\"0\" cellspacing=\"4\" cellpadding=\"0\">";
	var className = "selected_kenosix";
	
	if (playType.toLowerCase() == "direct") {
		htm = htm + "<tr style='display:none'><td class=\"kenosixtxtStyle\" align=\"left\"><input type=\"radio\" name=\"kenosixRadio\" id=\"kenosixRadio\" onclick=\"fillSelKenosix('kenosixRow')\" checked='true'/>Number Picked:</td><td><input type=\"text\" class=\"kenosixtextbox\" name=\"noPicked\" id=\"noOfQp\" value=\"0\" maxlength=\"2\" readOnly=\"true\" onkeyup=\"return _kenoSixfillNoOfQp(this, undefined, event)\" onkeydown=\"return _kenoSixfillNoOfQp(this, undefined, event)\" onblur=\"_kenosixCalAmt()\" style=\"width:30px\" /></td></tr>";
		htm = htm + "<tr><td colspan='2'><table width=\"95%\" id='kenosixRow' border=\"0\" cellspacing=\"4\" cellpadding=\"0\"><tr id=\"row" + i + "\">";
		for (var j = 0; j < parseInt(maxPickedNum); j++) {
			htm = htm + "<td><input type=\"text\" name='selNumTxt' class=\"" + className + "\" maxlength=\"2\"  onkeydown=\"return keyPressKenosix(this,event)\" onkeyup=\"return keyPressKenosix(this,event)\"  onblur=\"blurFnKenosix(this)\" onmousedown=\"return disClick(this)\"></td>";
		}
		htm = htm + "</tr></table></tr></td>";
	} else {
		if(playType.toLowerCase() == "perm") {
			htm = htm + "<tr><td class=\"kenosixtxtStyle\" align=\"left\"><input type=\"radio\" name=\"kenosixRadio\" id=\"kenosixRadio\" onclick=\"fillSelKenosix('kenosixRow')\" checked='true'/>Number Picked:</td><td><input type=\"text\" class=\"kenosixtextbox\" name=\"noPicked\" id=\"noOfQp\" value=\"0\" maxlength=\"2\" readOnly=\"true\" onkeyup=\"return _kenoSixfillNoOfQp(this, undefined, event)\" onkeydown=\"return _kenoSixfillNoOfQp(this, undefined, event)\" onblur=\"_kenosixCalAmt()\" style=\"width:30px\"/></td></tr>";
			htm = htm + "<tr><td colspan='2'><table width=\"95%\" id='kenosixRow' border=\"0\" cellspacing=\"4\" cellpadding=\"0\">";
			for (var i = 0; i < parseInt(maxPickedNum)/5; i++) {
				htm = htm + "<tr id=\"row" + i + "\">";
				for (var j = 0; j < 5; j++) {
					htm = htm + "<td><input type=\"text\" name='selNumTxt' class=\"" + className + "\" maxlength=\"2\"   onkeydown=\"return keyPressKenosix(this,event)\" onkeyup=\"return keyPressKenosix(this,event)\"  onblur=\"blurFnKenosix(this)\" onmousedown=\"return disClick(this)\"></td>";
				}
				htm = htm + "</tr>";
			}
			htm = htm + "</table></tr></td>";		
	}}
	return htm + "</table>";
}
function fillSelKenosix(val) {
	curSel = val;
	var temp = val.substring(val.length - 2, val.length);
	if (temp == "UL") {
		_id.o("kenosixRadioUL").checked = true;
		qpType = "noOfQp" + temp;
		maxPickedNum = "4";
	} else {
		if (temp == "BL") {
			_id.o("kenosixRadioBL").checked = true;
			qpType = "noOfQp" + temp;
			maxPickedNum = "20";
		} else {
			_id.o("kenosixRadio").checked = true;
			qpType = "noOfQp";
		}
	}
	if (isQP) {
		_id.o(qpType).focus();
	}
	kenosixapplyCSS();
}
function blurFnKenosix(obj) {
	if (isNaN(obj.value) || obj.value <= 0 || obj.value > 80) {
		obj.value = "";
	} else {
		if (obj.value.length == 1) {
			obj.value = "0" + obj.value;
		}
	}
	kenosixapplyCSS();
	_kenosixCalAmt();
}
function kenosixclearAllBoxes() {
	_id.n("noOfDraws")[0].value = 1;
	playType = _id.o("playType").value;
	if (playType.indexOf("Direct") != -1 || playType.indexOf("Perm") != -1) {
		_id.resetInpTypes("kenosixRow", "selected_kenosix", false);
		_id.o("noOfQp").value = 0;
		_id.o("noOfQp").readOnly = true;
	} 
	for (var i = 1; i <= 80; i++) {
		_id.o(i).className = "selected_kenosix_numbers";
		_id.o(i).style.background = "url(" + projectName + "/LMSImages/images/shade_white.gif) left center no-repeat";
		_id.o(i).style.color = "#00639c";
	}
	isQP = false;
	//_id.o("betAmount").value = parseFloat("10.0");
	_id.o("qpVal").value = "2";
	_id.o("totAmtLto").value = 0;
	_id.o("noOfLines").value = 0;
	_id.o("noOfDrwId").value = 1;
	_id.o("qpChkAll").checked = false;
	
	//for manual selection of draws: Set Default Values again
	//document.getElementsByName('conOrNonCon')[0].click();
}
function kenosixChkQP(id) {
	var elms = document.getElementsByName("selNumTxt");
	var playType = _id.o("playType").value;
	if (_id.o(id).checked == true) {
		isQP = true;
		_id.o("qpVal").value = "1";
		for (var l = 0; l < elms.length; l++) {
			elms[l].value = "";
			elms[l].disabled = true;
		}
		if (playType.indexOf("Direct") != -1) {
			_id.o(qpType).value = playType.substring(6, playType.length);
			_id.o("noOfLines").value = "1";
			_kenosixCalAmt();
		} else {
			if (playType == "Perm2" || playType == "Perm3") {
				_id.o(qpType).style.background = "#FFFF66";
				_id.o(qpType).value = "";
				_id.o(qpType).readOnly = false;
				_id.o(qpType).focus();
				_id.o("noOfLines").value = "0";
				_id.o("totAmtLto").value = "0";
			} else {
				if (playType == "Banker") {
					var temp = (curSel.substring(curSel.length - 2) == "BL") ? "UL" : "BL";
					_id.o("noOfQp" + temp).value = "";
					_id.o("noOfQp" + temp).readOnly = false;
					_id.o(qpType).value = "";
					_id.o(qpType).readOnly = false;
					_id.o(qpType).focus();
					_id.o("noOfLines").value = "0";
					_id.o("totAmtLto").value = "0";
				} else {
					if (playType == "Banker1AgainstAll") {
						_id.o(qpType).value = "1";
						_id.o("noOfLines").value = "89";
						_kenosixCalAmt();
					}else{
						if(playType == "Perm1"){
							_id.o(qpType).value = "10";
							_id.o(qpType).readOnly = true;
							//_id.o(qpType).focus();
							//_id.o("noOfLines").value = "20";
							_kenosixCalAmt();
						}
					}
				}
			}
		}
	} else {
		isQP = false;
		_id.o("qpVal").value = "2";
		for (var l = 0; l < elms.length; l++) {
			elms[l].value = "";
			elms[l].disabled = false;
		}
		if (playType == "Banker") {
			var temp = (curSel.substring(curSel.length - 2) == "BL") ? "UL" : "BL";
			_id.o("noOfQp" + temp).value = 0;
			_id.o("noOfQp" + temp).readOnly = true;
		}
		_id.o(qpType).value = 0;
		_id.o("noOfLines").value = 0;
		_id.o("totAmtLto").value = 0;
		_id.o(qpType).readOnly = true;
	}
	kenosixapplyCSS();
}
function _kenosixCalAmt(changeBetAmt) {
	var noOfDrw = 0;
	if(_id.n('conOrNonCon')[0]!= undefined && _id.n('conOrNonCon')[0].checked){
		noOfDrw = _id.n("noOfDraws")[0].value;
	}else if(_id.n('conOrNonCon')[1]!= undefined && _id.n('conOrNonCon')[1].checked){
		noOfDrw = _kenosixDrawIdArr.length;
	}
		_kenoSixfetchLines(changeBetAmt);
		var amt = _id.v("totAmtLto");
		if (isNaN(amt)) {
			amt = 0;
		}
		var totalPrice=noOfDrw * amt;
		totalPrice=tktPriceRound(totalPrice);
		//alert(totalPrice);
		_id.o("totAmtLto").value = totalPrice;
}
function _kenoSixfillNoOfQp(obj, val, e) {
	var keyCode = (window.event) ? event.keyCode : e.which;
	if ((keyCode >= 48 && keyCode < 58) || keyCode == 45 || keyCode == 27 || keyCode == 13 || keyCode == 40 || keyCode == 38 || keyCode == 46 || keyCode == 8 || (keyCode >= 96 && keyCode < 106 || keyCode == 9)) {
		if (keyCode == 46 || keyCode == 8 || keyCode == 9) {
			if (typeof val != "undefined" && keyCode == 9) {
				fillSelKenosix("kenosixRow" + val);
			}
			_kenosixCalAmt();
			return true;
		}
		if (obj.value.length >= 1) {
			if (obj.value == 0 || obj.value > parseInt(maxPickedNum) || isNaN(obj.value)) {
				obj.value = "";
				obj.focus();
			}
		}
	} else {
		return false;
	}
	_kenosixCalAmt();
	return true;
}
function keyPressKenosix(obj, e) {
	var keyCode = (window.event) ? event.keyCode : e.which;
	if ((keyCode >= 48 && keyCode < 58) || keyCode == 45 || keyCode == 27 || keyCode == 13 || keyCode == 40 || keyCode == 38 || keyCode == 46 || keyCode == 8 || (keyCode >= 96 && keyCode < 106 || keyCode == 9)) {
		if (keyCode == 46 || keyCode == 8 || keyCode == 9) {
			return true;
		}
		if (obj.value.length > 1) {
			if (obj.value == 0 || obj.value > 80 || isNaN(obj.value)) {
				obj.value = "";
				obj.focus();
			} else {
				var objR = _id.o(curSel);
				var clrChg = true;
				var elms = objR.getElementsByTagName("input");
				for (var l = 0; l < elms.length; l++) {
					clrChg = true;
					if ((elms[l].value == obj.value) && (elms[l] != obj)) {
						obj.value = "";
						obj.focus();
						clrChg = false;
						break;
					}
				}
				if (clrChg) {
					for (var l = 0; l < elms.length; l++) {
						if (elms[l].value == "") {
							if (elms[l].disabled == false) {
								elms[l].focus();
							}
							break;
						}
					}
				}
			}
			kenosixapplyCSS();
		}
	} else {
		return false;
	}
	return true;
}
function kenosixapplyCSS() {
	for (var i = 1; i <= 80; i++) {
		_id.o(i).className = "selected_kenosix_numbers";
		_id.o(i).style.background = "url(" + projectName + "/LMSImages/images/shade_white.gif) left center no-repeat";
		_id.o(i).style.color = "#00639c";
	}
	var clrChg = true;
	var elms = document.getElementsByName("selNumTxt");
	for (var l = 0; l < elms.length; l++) {
		for (var j = 0; j < elms.length; j++) {
			if ((elms[l].value == elms[j].value) && (elms[l] != elms[j])) {
				elms[j].value = "";
			}
		}
	}
	var count = 0;
	for (var l = 0; l < elms.length; l++) {
		if (elms[l].value != "") {
			_id.o(parseInt(elms[l].value, 10)).className = "selected_kenosix_numbersSelect";
			_id.o(parseInt(elms[l].value, 10)).style.background = "url(" + projectName + "/LMSImages/images/shade_over.gif) left center no-repeat";
			_id.o(parseInt(elms[l].value, 10)).style.color = "#FFFFFF";
			count += 1;
		}
	}
	if (!isQP) {
		_id.o(qpType).value = count;
	}
	if (count == totalPicked) {
		for (var l = 1; l <= 80; l++) {
			if (_id.o(l).className != "selected_kenosix_numbersSelect") {
				_id.o(l).style.background = "url(" + projectName + "/LMSImages/images/shade_white.gif) left center no-repeat";
			}
		}
	}
	if (isQP) {
		for (var l = 1; l <= 80; l++) {
			if (_id.o(l).className != "selected_kenosix_numbersSelect") {
				_id.o(l).className = "selected_kenosix_numbersDisabled";
				_id.o(l).style.background = "url(" + projectName + "/LMSImages/images/shade_disable.gif) left center no-repeat";
			}
		}
	}
}
function buyTicketkenosix(id) {
	//alert ("in Buy Ticket Keno Six !!");
	var compData = "";
	var playType = _id.o("playType").value;	
	if(playType == "Direct"){
		playType = "Direct1";
	}
	if(playType == "Perm"){
		playType = "Perm2"
	}	
	//alert(playType);
	if (_id.o("qpChkAll").checked == false) {
		var inputData = new Array();
		if (playType.indexOf("Direct") != -1 || playType.indexOf("Perm") != -1) {
			var obj = _id.o("kenosixRow");
			var elms = obj.getElementsByTagName("input");
			var count = 0;
			for (var l = 0; l < elms.length; l++) {
				if (elms[l].value != "") {
					inputData[count++] = elms[l].value;
				}
			}
			//alert(inputData+"*******"+inputData.length+ "*******"+ maxPickedNum);
			if (inputData.length < maxPickedNum && playType.substring(0, playType.length - 1) != "Perm") {
				alert("Please Pick " + maxPickedNum + " Numbers");
				return false;
			} else {
				if (playType == "Perm2" && (inputData.length == 0 || inputData.length < 3)) {					
					alert("Please Pick Minimum 3 Numbers");
					return false;
				} else {
					if (playType == "Perm3" && (inputData.length == 0 || inputData.length < 4)) {
						alert("Please Pick Minimum 4 Numbers");
						return false;
					}
				}
			}
		} 
		compData = compData + inputData.toString();
	} else {
		compData = "QP";
		var qpCheck = false;
		if (playType.indexOf("Direct")!= -1 || playType.indexOf("Perm")!= -1) {
			var qpVal;
			if(playType == "Direct1"){
				qpVal = 1;
				_id.n("noPicked")[0].value = 1;
			}else{
				qpVal = _id.o(qpType).value;
			}			
			//alert(qpVal);
			if (qpVal == 0 || qpVal == "") {
				alert("Please fill incomplete entries");
				_id.o(qpType).value = "";
				_id.o(qpType).focus();
				qpCheck = true;
			} else {
				if (playType == "Perm2" && qpVal < 3) {
					alert("Please Pick Minimum 3 Numbers");
					_id.o(qpType).value = "";
					_id.o(qpType).focus();
					qpCheck = true;
				} else {
					if (playType == "Perm3" && qpVal < 4) {
						alert("Please Pick Minimum 4 Numbers");
						_id.o(qpType).value = "";
						_id.o(qpType).focus();
						qpCheck = true;
					}
				}
				if (qpVal > 20 && (playType == "Perm2" || playType == "Perm3")) {
					alert("Please Pick Maximum 20 Numbers");
					qpCheck = true;
				}
			}
		} 
		//alert(qpCheck);
		if (qpCheck) {
			return false;
		}
	}
	var amount = _id.o("totAmtLto").value;
	var betAmt = _id.o("betAmount").value;
	
	//alert(betAmt+'****'+unitBetTypePriceArrKenoSix[playType].unitBetTypePriceKenoSix);
	betAmt=Math.round(betAmt/_kenosixPrice);
	//alert(betAmt);
	//alert(compData + "  bhbhb   " + _id.o("noOfLines").value);
	_id.o(id).disabled = true;
	if (playType.indexOf("Direct") != -1 || playType.indexOf("Perm") != -1) {
			//alert(_id.n('conOrNonCon')[1].checked);
			//alert(_kenosixDrawIdArr.length);
			if(_id.n('conOrNonCon')[1]!= undefined && _id.n('conOrNonCon')[1].checked && _kenosixDrawIdArr.length > 0){
				var html = '';
				for(var i=0; i<_kenosixDrawIdArr.length;i+=1){
					html += '&drawIdArr='+_kenosixDrawIdArr[i];
					//alert(html+"GAURAV");
				}
				html+='&isAdvancedPlay=1';
				gameBuyAjaxReq("kenoSixBuy.action?pickedNumbers=" + compData + "&noOfDraws=" + _id.n("noOfDraws")[0].value + "&totalPurchaseAmt=" + amount + "&noPicked=" + _id.n("noPicked")[0].value + "&playType=" + playType + "&QP=" + _id.n("QP")[0].value + "&betAmt=" + betAmt + "&noOfLines=" + _id.n("noLines")[0].value + html);
			}else if(_id.n('conOrNonCon')[1].checked && _kenosixDrawIdArr.length == 0){
				alert("Please select atleast one draw by clicking on select button.");
				drawMenuClick(1);			
				//return false;					
			}else{
				gameBuyAjaxReq("kenoSixBuy.action?pickedNumbers=" + compData + "&noOfDraws=" + _id.n("noOfDraws")[0].value + "&totalPurchaseAmt=" + amount + "&noPicked=" + _id.n("noPicked")[0].value + "&playType=" + playType + "&QP=" + _id.n("QP")[0].value + "&betAmt=" + betAmt + "&noOfLines=" + _id.n("noLines")[0].value);
			}
	} 
	kenosixclearAllBoxes();
	document.getElementById("radioDirect").checked = true;
	//document.getElementById("radioPerm").checked = false;
	makeList("Direct");
	return true;
}
var lines = 0;
function _kenoSixfetchLines(changeBetAmt) {
	var playType = _id.o("playType").value;
	lines = 0;
	if (playType.indexOf("Direct") != -1) {
		lines = 1;
	} else {
		if (playType == "Perm2") {
			var num = _id.o("noOfQp").value;//alert("******"+isNaN(num)+"******");
			if (isNaN(num) || num == "") {
				_id.o(qpType).value = "";
			} else {
				if (num != 0 && num != 1 && num != 2) {
					lines = num * (num - 1) / 2;
				} else {
					lines = 1;
				}
			}
		} else {
			if (playType == "Perm3") {
				var num = _id.o("noOfQp").value;
				if (isNaN(num) || num == "") {
					_id.o(qpType).value = "";
				} else {
					if (num != 0 && num != 1 && num != 2 && num != 3) {
						lines = num * (num - 1) * (num - 2) / 6;
					} else {
						lines = 1;
					}
				}
			} else {
				if (playType == "Banker") {
					var numUL = isNaN(_id.o("noOfQpUL").value) ? 0 : _id.o("noOfQpUL").value;
					var numBL = isNaN(_id.o("noOfQpBL").value) ? 0 : _id.o("noOfQpBL").value;
					lines = parseInt(numUL, 10) * parseInt(numBL, 10);
				} else {
					if (playType == "Banker1AgainstAll") {
						lines = 89;
					}else{
						if(playType == "Perm1"){
							var num = _id.o("noOfQp").value;
							if (isNaN(num) || num == "") {
								_id.o(qpType).value = "";
							} else {
								lines = num;
							}
						}
					}
				}
			}
		}
	}
	//alert("******"+lines+"*********");
	if (isNaN(lines)) {
		lines = 0;
	}
	
	/*if(playType == "Perm1" && (20 % _id.o("noOfQp").value) == 0){
		_kenosixPrice=unitBetTypePriceArrKenoSix[playType].unitBetTypePriceKenoSix;
		_kenosixPrice=_kenosixPrice*20/lines;
		if (changeBetAmt == undefined) {
			_id.o("betAmount").value = parseFloat(_kenosixPrice, 10);
		}
	}*/
	
	_id.o("noOfLines").value = lines;
	_id.o("totAmtLto").value = lines * _id.o("betAmount").value;
}
function _kenoSixChkPickType(id) {
	isPerm1 = false ; 
	var playType = _id.o(id).value;
	_id.o("playType").value = playType;
	_kenosixPrice = unitBetTypePriceArrKenoSix[playType].unitBetTypePriceKenoSix;
	_id.o("kenosixPrice").innerHTML =currSymbol + " "+ _kenosixPrice + "/-";
	
	_id.o("betAmount").value = parseFloat(_kenosixPrice, 10);
	
	if (playType.indexOf("Direct") != -1) {
		maxPickedNum = playType.substring(6,playType.length);
		totalPicked = maxPickedNum;
		_id.o("kenosixRowDiv").innerHTML = kenosixRowDirect(playType.substring(0, playType.length - 1), playType.substring(6, playType.length));
		curSel = "kenosixRow";
		qpType = "noOfQp";
	} else {
		if (playType == "Perm2" || playType == "Perm3") {
			maxPickedNum = "20";
			totalPicked = maxPickedNum;
			curSel = "kenosixRow";
			_id.o("kenosixRowDiv").innerHTML = kenosixRow(playType.substring(0, playType.length - 1));
			qpType = "noOfQp";
		} 
	}	
	kenosixclearAllBoxes();
	_id.o("totAmtLto").value = 0;
	_id.o(qpType).value = 0;
	_id.o("noOfLines").value = 0;
}
function _kenoSixBetAmountMultiple(ele, func) {
	var num = _id.v(ele);
	var playType = _id.o("playType").value;
	if (func == "plus") {
		if(parseFloat(num) < _kenosixPrice*parseFloat(10)){
			_id.o(ele).value = Math.round(((num > 1) ? (parseFloat(num == _kenosixPrice ? _kenosixPrice : num, 10) + parseFloat(_kenosixPrice)) : num)*100)/100;
		}
	} else {
		_id.o(ele).value = Math.round(((num > parseFloat(_kenosixPrice)) ? (parseFloat(num == _kenosixPrice ? _kenosixPrice : num, 10) - parseFloat(_kenosixPrice)) : num)*100)/100;
	}
	_kenosixCalAmt(true);
}
//alert(gameDispName["kenosix"]._kenosixPrice);

var unitBetPriceKenoSix = gameDispName["kenosix"].unitPrice.split(",");

function betTypePriceKenoSix(value) {
	this.unitBetTypePriceKenoSix = value;
}
function getPlayTypeUnitPriceKenoSix() {
	for (var i = 0; i < unitBetPriceKenoSix.length; i++) {
		var pickTypeOptVal = unitBetPriceKenoSix[i].replace(" ", "").split("=");
		unitBetTypePriceArrKenoSix[pickTypeOptVal[0]] = new betTypePriceKenoSix(pickTypeOptVal[1]);
	//alert("*******"+unitBetTypePriceArrKenoSix[pickTypeOptVal[0]].unitBetTypePriceKenoSix+"****"+pickTypeOptVal[0]+"***");
	}
}
getPlayTypeUnitPriceKenoSix();
_kenosixPrice = unitBetTypePriceArrKenoSix["Direct1"].unitBetTypePriceKenoSix;
_kenosixDispPrice = unitBetTypePriceArrKenoSix["Direct1"].unitBetTypePriceKenoSix;

function changePriceValue(){
	if(document.getElementById("playType").value.toString().indexOf("Direct")!= -1){
		_kenosixPrice = unitBetTypePriceArrKenoSix[document.getElementById("pickTypeDirect").value.toString()].unitBetTypePriceKenoSix;
	}
	else{
		_kenosixPrice = unitBetTypePriceArrKenoSix[document.getElementById("pickTypePerm").value.toString()].unitBetTypePriceKenoSix;
	}
	_id.o("betAmount").value = _kenosixPrice;
}

/*function changePriceValue(playtype){
	if(playtype.indexOf("Direct")!= -1){
		_kenosixDispPrice = unitBetTypePriceArrKenoSix["Direct1"].unitBetTypePriceKenoSix;
	}else{
		_kenosixDispPrice = unitBetTypePriceArrKenoSix["Perm2"].unitBetTypePriceKenoSix;
	}
	_id.o("betAmount").value = _kenosixPrice;
}*/



function makeList(playType){
	if(playType.toLowerCase() == "direct"){
		document.getElementById("pickTypeDirect").style.display = "block";
		document.getElementById("pickTypePerm").style.display = "none";
		document.getElementById("kenosixRowDiv").innerHTML = "";
		document.getElementById("kenosixRowDiv").innerHTML= kenosixRowDirect("direct1", 1);
		document.getElementById("pickTypeDirect").selectedIndex = 0;
		document.getElementById("playType").value = "Direct1";
		kenosixclearAllBoxes();
		maxPickedNum = "1";
		document.getElementById("kenosixPrice").innerHTML = currSymbol+" "+unitBetTypePriceArrKenoSix["Direct1"].unitBetTypePriceKenoSix+"/-";
		_kenosixPrice = unitBetTypePriceArrKenoSix["Direct1"].unitBetTypePriceKenoSix;
		_id.o("betAmount").value = unitBetTypePriceArrKenoSix["Direct1"].unitBetTypePriceKenoSix;
		
	}else{		
		document.getElementById("pickTypeDirect").style.display = "none";
		document.getElementById("pickTypePerm").style.display = "block";
		document.getElementById("kenosixRowDiv").innerHTML = "";
		maxPickedNum = "20";		 
		document.getElementById("kenosixRowDiv").innerHTML = kenosixRow("perm");
		document.getElementById("pickTypePerm").selectedIndex = 0;
		document.getElementById("playType").value = "Perm2";
		kenosixclearAllBoxes();
		document.getElementById("kenosixPrice").innerHTML = currSymbol+" "+unitBetTypePriceArrKenoSix["Perm2"].unitBetTypePriceKenoSix+"/-";
		_kenosixPrice = unitBetTypePriceArrKenoSix["Perm2"].unitBetTypePriceKenoSix;
		_id.o("betAmount").value = unitBetTypePriceArrKenoSix["Perm2"].unitBetTypePriceKenoSix;
	}
}



var line = "<tr><td bgcolor=\"#00639c\"><table border=\"0\" bordercolor=\"blue\" width=\"97%\"> <tr ><td align=\"left\" width=\"15%\"  style=\"display:none\"> <input type=\"hidden\" value=\"0\" id=\"counterId\"> <input type=\"hidden\" id=\"dateFuture\"> <input type=\"hidden\" id=\"freezeTimeId\" value=\"" + _kenosixfreezeTime + "\"> <span class=\"style1\">Next Draw :</span><div id=\"latestDrawTime\" style=\"display:none\"></div></td><td width=\"20%\" colspan=\"2\" align=\"left\"  style=\"display:none\"><div id=\"nxtDrawTime\" class=\"kenosixstyle2\"></div></td><td align=\"center\" width=\"10%\"  style=\"display:none\"><div id=\"timeLeft\"> <span class=\"style1\"> Time Left :</div></span></td> <td align=\"left\" width=\"10%\"  style=\"display:none\"><div id=\"xyz\" class=\"kenosixstyle2\"></div> </td><td width=\"3%\" align=\"right\"  style=\"display:none\"><img src=\"" + fortImgPath + "que.gif\" height=\"25px\" width = \"25px\" style=\"cursor:hand\" onClick=\"gameInfo()\" alt=\"Game Info\" /> </td></tr> <tr bgcolor=\"#00639c\"> <td width=\"110\"  align=\"left\"> <span class=\"style1\" style=\"height:22px;background-color:#00639c;padding-top:3px;font-size:16px;font-family:Arial;font-weight:bold;color:#FFFFFF\">No. of Draws :</span> </td> <td width=\"10%\">         <table><tr><td> <input type=\"radio\"/ name=\"conOrNonCon\" value=\"consec\" checked='true' onclick=\"drawSelectButtonKenoSix(this)\"'/></td><td> <select\tname=\"noOfDraws\" id=\"noOfDrwId\"\tclass=\"fortune_option\" align=\"left\"\twidth=\"20%\"\tonchange=\"_kenosixCalAmt()\">" + getActiveDrawOptionList("kenosix") + "</select> </td></tr></table>        </td><td width=\"27%\">     <table><tr><td><input type=\"radio\"/ name=\"conOrNonCon\" value=\"nonconsec\" onclick=\"specificDrawPopUp(_kenosixdrawSelect)\" /></td><td class=\"style1\">Advance Draw</td></tr></table>     </td><td colspan=\"1\" align=\"left\" valign=\"top\"> &nbsp; </td><td width=\"10px\" align=\"center\" colspan=\"2\"><div id=\"kenosixPrice\" style=\"width:100px;border:1px solid #FFFFFF;margin-left:31px\" class=\"kenosixnum\">"+currSymbol+" " + _kenosixDispPrice + "/-</div></td><td width=\"10px\" align=\"right\" colspan=\"2\"> <img src=\"" + fortImgPath + "que.gif\" height=\"20px\" width = \"20px\" style=\"cursor:hand\" onClick=\"gameInfo()\" alt=\"Game Info\" /></td></tr>  <tr><td><table><tr><td align=\"right\" height=\"0px\" width=\"0px\" id=\"drawSelectorDiv\" style=\"visibility:hidden;position:absolute;background-color:#FFFFFF\"></td></tr></table></td></tr></table></td></tr>";
var _kenosixNxtDrwTbl = _kenosixFillNxtDrw();
var _kenosixHtmlTbl = "<table width=\"100%\"height=\"100%\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\" bordercolor=\"red\" bgcolor=\"#9cceff\">" + line + "<tr><td align=\"center\" valign=\"top\"><table width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\"><tr><td width=\"65%\" valign=\"top\" ><div style=\"padding-top:10px;\"><table border=\"0\" bordercolor=\"red\" width=\"97%\" align=\"center\"cellpadding=\"0\"cellspacing=\"0\"><tr><td class=\"style10\" align=\"left\"><div align=\"center\" valign=\"middle\" style=\"height:18px;background-color:#00639c;width:180px;float:left;padding-top:2px\">&nbsp;Select Number Panel&nbsp;</div></td></tr><tr><td valign=\"middle\" style=\"border:1px #00639c solid;background-color:#639cce\">" + kenosixNmSel(80) + "</td></tr></table></div></td><td valign=\"top\" wdith=\"35%\"><div style=\"border:0px solid black; width:190px; padding-top:0px;\"><div style=\"background-color:#00639c;border-top:1px solid #FFFFFF;border-left:1px solid #FFFFFF;border-right:1px solid #FFFFFF;height:35px; width:180px; margin-left:0px\"><table style=\"margin-top:4px\"><tbody><tr><td style=\"text-align:center; height:18px; width:160px;background: none repeat scroll 0 0 #FFFFFF;border:1px solid #9cceff;border-radius:3px;padding: 2px 2px 2px 2px;\" class=\"selected_kenosix_numbers\"><font size=\"2\">Purchase Info</font></td></tr></tbody></table></div><table border=\"0\" bordercolor=\"red\" width=\"96%\" cellpadding=\"0\" cellspacing=\"0\" align=\"center\" style=\"margin-right:10px\"><tr><td><div align=\"left\" style=\"margin-left:0px; border-top:1px solid #FFFFFF;border-left:1px solid #FFFFFF;border-bottom:1px solid #FFFFFF;height:18px;background-color:#00639c;width:111px;float:right;padding-top:2px\" valign=\"left\" class=\"style10\"><input type='radio' name='playType' id='radioDirect' value='Direct' checked='true' onclick='makeList(\"direct\")'>Spot &nbsp;<input id='radioPerm' type='radio' name='playType' value='Perm' onclick='makeList(\"perm\")'>Perm</div></td><td align=\"left\"class=\"style10\"style=\"cursor:hand;\"onclick=\"kenosixclearAllBoxes()\"><div align=\"center\" valign=\"right\" style=\"border:1px solid #FFFFFF;height:18px;width:67px;background-color:#00639c;float:right;padding-top:2px\">&nbsp;&nbsp;&nbsp;Clear All&nbsp;&nbsp;&nbsp;</div></td></tr><tr><td colspan=\"2\" valign=\"middle\" style=\"border:1px #00639c solid;background-color:#639cce\"><table width=\"100%\" border=\"0\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\"><tr><td bgcolor=\"#00639c\"><input style=\"display:none\" type=\"text\" value=\"Direct1\" name=\"playType\" id=\"playType\" onchange =\"changePriceValue()\"><select name=\"pickTypePerm\" id=\"pickTypePerm\" style=\"width:100%;background-color:#00639c;color:#FFFFFF;font-size:12px;font-family:Arial;font-weight:bold;display:none\" onchange=\"_kenoSixChkPickType(this.id); changePriceValue()\"><option value=\"Perm2\">Perm-2</option><option value=\"Perm3\">Perm-3</option></select><select name=\"pickTypeDirect\" id=\"pickTypeDirect\" style=\"width:100%;background-color:#00639c;color:#FFFFFF;font-size:12px;font-family:Arial;font-weight:bold;\" onchange=\"_kenoSixChkPickType(this.id); changePriceValue()\"><option value=\"Direct1\">Spot-1</option><option value=\"Direct2\">Spot-2</option><option value=\"Direct3\">Spot-3</option><option value=\"Direct4\">Spot-4</option><option value=\"Direct5\">Spot-5</option><option value=\"Direct6\">Spot-6</option><option value=\"Direct7\">Spot-7</option><option value=\"Direct8\">Spot-8</option><option value=\"Direct9\">Spot-9</option><option value=\"Direct10\">Spot-10</option></select></td></tr><tr><td><div id='kenosixRowDiv' style='height:180px'>" + kenosixRowDirect("direct1", 1) + "</div><div id='kenosixRowDiv' style='height:220px; display:none'>" + kenosixRow("perm") + "</div><div style=\"background-color:#00639c;border:1px solid #FFFFFF;height:98px; overflow-y: auto;overflow-x: hidden;scrollbar-arrow-color:#9CCEFF;scrollbar-base-color:#00639c;scrollbar-darkshadow-color:#9CCEFF;scrollbar-face-color:#00639c; scrollbar-highlight-color:#9CCEFF; scrollbar-shadow-color:#9CCEFF\" class=\"kenosixtxtStyle\" id=\"selectedDrawList\" align=\"center\" id=\"selectedDrawList\"><table style=\"margin-top:5px\"><tbody><tr><td style=\"text-align:center; height:70px; width:155px;background: none repeat scroll 0 0 #FFFFFF;border-radius:3px;border:1px solid #9cceff;\" class=\"selected_kenosix_numbers\"><font size=\"2\">No Advance Draws Selected !! </font></td></tr></tbody></table></div></td></tr><tr><td bgcolor=\"#00639c\" style=\"border-bottom:1px solid #FFFFFF;border-left:1px solid #FFFFFF;border-right:1px solid #FFFFFF;\"><table width=\"95%\" border=\"0\"><tr><td width=\"60%\"class=\"kenosixtxtStyle\" align=\"left\">Unit Price</td><td width=\"5%\" align=\"right\"><img src=\"" + fortImgPath + "minus.gif\"  width=\"13px\" height=\"15px\" style=\"cursor:pointer\" alt=\"minus\" onclick=\"_kenoSixBetAmountMultiple('betAmount','minus')\"/></td><td><input type=\"text\" class=\"kenosixtextbox\" name=\"betAmount\" id=\"betAmount\" value='"+_kenosixPrice+"' readOnly=\"true\" style=\"width:48px\" /></td><td width=\"5%\" align=\"left\"><img src=\"" + fortImgPath + "plus.gif\" width=\"13px\" height=\"15px\" alt=\"Plus\" onclick=\"_kenoSixBetAmountMultiple('betAmount','plus')\" style=\"cursor:pointer\"/></td></tr><tr><td class=\"kenosixtxtStyle\" align=\"left\">No Of Lines :</td><td></td><td><input type=\"text\" class=\"kenosixtextbox\" name=\"noLines\" id=\"noOfLines\" value=\"0\" maxlength=\"2\" readOnly=\"true\" /></td></tr><tr><td class=\"kenosixtxtStyle\" align=\"left\">Total Amount:</td><td></td><td><input type=\"text\" id=\"totAmtLto\" name=\"totalPurchaseAmt\" value=\"0\" readonly=\"true\" class=\"kenosixtextbox\"/></td></tr><tr><td align=\"center\"class=\"style5\"><div style=\"border:1px solid #639cce;width:40px;height:30px; float:left\">QP<br/><input type=\"hidden\" value=\"2\" name=\"QP\" id=\"qpVal\"><input type=\"checkbox\"id=\"qpChkAll\" onclick=\"kenosixChkQP(this.id)\"/></div></td><td></td><td align=\"center\"><a href=\"#\" ><img height=\"30px\" width=\"40px\" id='buy' src=\"" + fortImgPath + "buy_lotto.gif\" onclick=\"return buyTicketkenosix(this.id)\"alt=\"Buy\" style=\"margin-right:4px;border: 1px #9CCEFF solid;\"/></a></td></tr></table></td></tr></table></td></tr></table></div></td></tr></table></table>";
var _kenosixHTML = "<div id=\"strip\">" + drwMenu + "</div><form style=\"background-color:#FFFFFF;border: 1px solid #393939;\"><table  border=\"0\" width=\"100%\" height=\"100%\" cellspacing=\"0\" cellpadding=\"0\" align=\"center\" bgcolor=\"#FFFFFF\"  bordercolor=\"red\"><tr><td width=\"10%\"  valign=\"top\" align=\"center\" onmousedown=\"return disClick(this)\"><input type=\"hidden\" id=\"drawType\"><div id=\"winningDispDiv\" style=\"border:1px #00639c solid;background-color:#9CCEFF; margin-left:5px;margin-top:3px;\"></div><div id=\"nxtDrawDiv\" style=\"border:1px #00639c solid;background-color:#9cceff;margin-left:5px;margin-top:5px;margin-bottom:3px; height:302px\"></div></td><td width=\"90%\" valign=\"top\" align=\"right\"><div id=\"kenosixmiddle\" style=\"border:1px #00639c solid;background-color:#639cce;width:98%;height:525px;margin-left:5px;margin-top:3px;margin-bottom:1px\">" + _kenosixHtmlTbl + "</div></td></tr></table></td></tr></table></form>";
//freezeTime = _id.o("freezeTimeId").value;



parent.frames[0].Version['$Source: /rep/LMS_Mgmt/WebRoot/com/skilrock/lms/web/drawGames/playMgmt/games/kenosix/kenosix.js,v $'] = '$Id: kenosix.js,v 1.2 2016/10/31 09:46:08 neeraj Exp $';