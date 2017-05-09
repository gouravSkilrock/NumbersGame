var _twelvebytwentyfourWinArr = new Array();
var _twelvebytwentyfourDrawIdArr = new Array();
var isQP = false;
var curDrwTimetwelvebytwentyfour = "";
var curSel = "twelvebytwentyfourRow";
var totalPicked = "12;";
var maxPickedNum = "12";
var qpType = "noOfQp";
var _twelvebytwentyfourPrice = 0;
var isPerm1 = true;
var staticType = false;
var _twelvebytwentyfourdrawSelect;
function setDefaulttwelvebytwentyfour() {
	isQP = false;
	curDrwTimetwelvebytwentyfour = "";
	curSel = "twelvebytwentyfourRow";
	maxPickedNum = "12";
	totalPicked = "12";
	qpType = "noOfQp";
	_twelvebytwentyfourPrice = unitBetTypePriceArrTwelveByTwentyFour["Direct12"].unitBetTypePriceTwelveByTwentyFour;
}
function _twelvebytwentyfourWinAppend() {
	if (!_twelvebytwentyfourWin.match("Result Awaited")) {
		_twelvebytwentyfourWin = "Result Awaited=\"\" Nxt" + _twelvebytwentyfourWin;
	}
}
function _twelvebytwentyfourLatestDrawTime() {
	return _twelvebytwentyfourStatus.split("=")[1];
}
function _twelvebytwentyfourFillWinTbl() {
	var _twelvebytwentyfourWinHt = "<table width=\"100%\" border=\"0\" bordercolor=\"red\" cellspacing=\"0\" cellpadding=\"0\" align=\"center\" bgcolor=\"#9CCEFF\"><tr><td colspan=\"2\" align=\"left\"><div valign=\"middle\" style=\"height:22px;background-color:#00639C;padding-top:3px;font-size:16px;font-family:Arial;font-weight:bold;color:#FFFFFF\">&nbsp;Win Result&nbsp;</div></td></tr>      <tr><td valign=\"top\" align=\"center\" height=\"100\">";
	var winArr = _twelvebytwentyfourWin.split("Nxt");
	var _twelvebytwentyfourWinHTML = "<table width=\"100%\" border=\"0\" bordercolor=\"red\" style=\"margin-top:3px\"><tr><td><div style=\"" + scrollColor("#9CCEFF", "#00639C", "184") + "\">";
	for (var i = 0; i <= winArr.length - 2; i++) {
		if (!winArr[i].match("Result")) {
			var time = new Date(parseInt(winArr[i].split("=")[0].replace(" ", "")));
			var newTime = week[time.getDay()] + " " + time.getDate() + " " + month[time.getMonth()] + "<br/>" + (time.getHours() < 10 ? "0" + time.getHours() : time.getHours()) + ":" + (time.getMinutes() < 10 ? "0" + time.getMinutes() : time.getMinutes()) + ":" + (time.getSeconds() < 10 ? "0" + time.getSeconds() : time.getSeconds());
			var htm = "<table width=\"90%\" border=\"0\" bordercolor=\"green\" id=\"result" + i + "\" cellspacing=\"2\" cellpadding=\"10\" class=\"twelvebytwentyfour_winningResult_table\" ><tr><td class=\"twelvebytwentyfourwinningResultTime\" colspan=\"8\" height=\"53\">" + newTime + "</td></tr><tr>";
			var num = (winArr[i].split("=")[1]).split(",");
			
			for (var j = 0; j < num.length; j++) {
				if(j%6 == 0)
				{
					htm = htm + "<tr/><tr>";
				}
				var temp = (num[j] < 10 ? "0" + num[j] : num[j]);
				htm = htm + "<td width=\"10%\" class=\"selected_win_twelvebytwentyfour_numbers\" align=\"center\">" + temp + "</td>";
			}
			htm = htm + "</tr></table>";
			_twelvebytwentyfourWinHTML = _twelvebytwentyfourWinHTML + htm;
		} else {
			var htm = "<table width=\"90%\" bordercolor=\"blue\" border=\"0\" cellspacing=\"2\" cellpadding=\"10\" id=\"result" + i + "\" class=\"twelvebytwentyfour_winningResult_table\" ><tr><td class=\"twelvebytwentyfourwinningResultTime\" colspan=\"8\" height=\"53\" >Result Awaited</td></tr><tr>";
			htm = htm + "<td class=\"selected_win_twelvebytwentyfour_numbers\"  colspan=\"8\"><div style=\"text-align:center\" id=\"winAwaitedDiv\" style=\"color:#00466E\"></div></td>";
			htm = htm + "</tr></table>";
			_twelvebytwentyfourWinHTML = _twelvebytwentyfourWinHTML + htm;
		}
	}
	_twelvebytwentyfourWinHTML = _twelvebytwentyfourWinHTML + "</div></td></tr></table>";
	return _twelvebytwentyfourWinHt + _twelvebytwentyfourWinHTML + "</td></tr></table>";
}
function _twelvebytwentyfourFillNxtDrw() {
	var nxtDrwHTML = "<table width=\"100%\" cellspacing=\"0\"cellpadding=\"0\"  border=\"0\" bordercolor=\"red\"><tr><td colspan=\"2\" align=\"left\" valign=\"top\"><div valign=\"middle\" style=\"height:22px;width:100%;background-color:#00639C;padding-top:3px;font-size:16px;font-family:Arial;font-weight:bold;color:#FFFFFF\">&nbsp;Next Draw&nbsp;</div></td></tr><tr><td align=\"center\"valign=\"top\"><table width=\"100%\" bordercolor=\"red\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\" bgcolor=\"#9CCEFF\"><tr><td width=\"100%\" align=\"left\" valign=\"top\" ><div style=\"" + scrollColor("#9CCEFF", "#00639C", "175") + "\"><table width=\"90%\" align=\"center\" bordercolor=\"green\"  border=\"0\" cellspacing=\"0\" cellpadding=\"0\" class=\"borderbtmtwelvebytwentyfour\">";
	_twelvebytwentyfourdrawSelect = '<table width="auto" cellspacing="0"cellpadding="0"  border="1"><tr><td colspan="3" align="center"><b>Select Draws</td></tr>';
	var nxtDrwArr = _twelvebytwentyfourDrawTime.split(",");
	for (var i = 0, l = nxtDrwArr.length; i < l; i++) {
		if(nxtDrwArr[i] == undefined || nxtDrwArr[i] == null || nxtDrwArr[i] == "")
			isReqActive = true;
		var time = new Date(parseInt(nxtDrwArr[i].replace(" ", "")));
		var newTime = week[time.getDay()] + " " + time.getDate() + " " + month[time.getMonth()] + " " + (time.getHours() < 10 ? "0" + time.getHours() : time.getHours()) + ":" + (time.getMinutes() < 10 ? "0" + time.getMinutes() : time.getMinutes());
		var hiddenTime = "<input type='text' id='timer" + i + "'>";
		var rowcolor;
		var data = "";
		if (i == 0) {
			data = time.getFullYear() + "-" + time.getMonth() + "-" + time.getDate() + "-" + (time.getHours() < 10 ? "0" + time.getHours() : time.getHours()) + "-" + (time.getMinutes() < 10 ? "0" + time.getMinutes() : time.getMinutes()) + "-" + (time.getSeconds() < 10 ? "0" + time.getSeconds() : time.getSeconds());
			var dateFuture1 = new Date(time.getFullYear(), time.getMonth(), time.getDate(), (time.getHours() < 10 ? "0" + time.getHours() : time.getHours()), (time.getMinutes() < 10 ? "0" + time.getMinutes() : time.getMinutes()), (time.getSeconds() < 10 ? "0" + time.getSeconds() : time.getSeconds()));
			_twelvebytwentyfourNxtDrawTime = ("" + time).substring(0, ("" + time).lastIndexOf(":") + 3);
		}
		if (i % 2 == 0) {
			rowcolor = "#9CCEFF";
		} else {
			rowcolor = "#639CCE";
		}
		nxtDrwHTML = nxtDrwHTML + "<tr><td width=\"100%\"><input type=\"hidden\" value=\"" + data + "\" id=\"" + i + "timer\"></td></tr><tr bgcolor=\"" + rowcolor + "\" style=\"height: 20px\"><td width=\"100%\" align=\"center\" class=\"borderbtmtwelvebytwentyfour\" colspan=\"2\"><div id=\"" + i + "blink\" >" + newTime + "</div></td><td width=\"44%\" align=\"center\"><p><div id=\"nxtDrwD" + i + "\" style=\"display:none\"><img src=\"" + fortImgPath + "freezed.gif\"></div></p></td></tr>";
		drawName = _twelvebytwentyfourDrawIdObj[parseInt(nxtDrwArr[i].replace(" ", ""))].split(',')[1];
		if(drawName == 'null'){
			drawName = 'DRAW';
		}
		_twelvebytwentyfourdrawSelect = _twelvebytwentyfourdrawSelect + '<tr><td align="right"><input type="checkbox" title="'+newTime+'" name="drawIdChkBox" value="'+drawName+'" id="'+ _twelvebytwentyfourDrawIdObj[parseInt(nxtDrwArr[i].replace(" ", ""))].split(',')[0] +'_did"/></td><td width="30%" nowarp="nowrap">'+drawName+'</td><td align="center">'+newTime+'</td></tr>'
	}
	nxtDrwHTML = nxtDrwHTML + "</table></div></td></tr></table></td></tr></table>";
	_twelvebytwentyfourdrawSelect = _twelvebytwentyfourdrawSelect + '<tr><td colspan="3" align="right"><input class="button" id="selNonConsecButton" onclick="drawSelectButton()" type="button" value="Select"/></td></tr></table>'
	return nxtDrwHTML;
}
function _twelvebytwentyfourUpData(win, drwTime, drwStatus) {
	_twelvebytwentyfourWin = win;
	_twelvebytwentyfourDrawTime = drwTime;
	_twelvebytwentyfourStatus = drwStatus;
}
function _twelvebytwentyfourWinAjx() {
	var url = "fetchWinTwelveByTwentyFour.action?frmDate=" + _twelvebytwentyfourWinArr[_twelvebytwentyfourWinArr.length - 1];
	_twelvebytwentyfourWin = _twelvebytwentyfourWin + (winAjaxReq(url)).data;
	_twelvebytwentyfourFillWinTbl();
}
function twelvebytwentyfourNmSel(lstNo) {
	var htm = "<div id=\"\"><table width=\"99%\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\" bgcolor=\"#639CCE\" style=\"margin-top:6px;margin-bottom:5px\"><tr>";
	for (var i = 1; i <= lstNo; i++) {
		if (i % 6 == 0) {
			htm = htm + "<td style=\"cursor:pointer;background:url(" + projectName + "/LMSImages/images/shade_white.gif) center no-repeat\" class=\"selected_twelvebytwentyfour_numbers\" onmouseout=\"twelvebytwentyfouronmouseouteffect(this.id)\" onmouseover=\"twelvebytwentyfouronmouseovereffect(this.id)\"  onclick=\"fillNumTwelveByTwentyFour(" + i + ")\"  id=\"" + i + "\" >" + (i < 10 ? "0" + i : i) + "</td></tr>";
		} else {
			htm = htm + "<td class=\"selected_twelvebytwentyfour_numbers\"  style=\"cursor:pointer;background:url(" + projectName + "/LMSImages/images/shade_white.gif) center no-repeat\" onmouseout=\"twelvebytwentyfouronmouseouteffect(this.id)\" onmouseover=\"twelvebytwentyfouronmouseovereffect(this.id)\" onclick=\"fillNumTwelveByTwentyFour(" + i + ")\"  id=\"" + i + "\"  >" + (i < 10 ? "0" + i : i) + "</td>";
		}
	}
	htm = htm + "</tr></table></div>";
	return htm;
}
function twelvebytwentyfouronmouseovereffect(id) {
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
		if (_id.o(id).className != "selected_twelvebytwentyfour_numbersSelect") {
			_id.o(id).style.background = "url(" + projectName + "/LMSImages/images/shade_over.gif) center center no-repeat";
			_id.o(id).style.color = "#FFFFFF";
			_id.o(id).className = "selected_twelvebytwentyfour_numbersSelectOMO";
		}
	}
}
function twelvebytwentyfouronmouseouteffect(id) {
	if (_id.o(id).className == "selected_twelvebytwentyfour_numbersSelectOMO") {
		_id.o(id).style.background = "url(" + projectName + "/LMSImages/images/shade_white.gif) center center no-repeat";
		_id.o(id).style.color = "#00639C";
		_id.o(id).className = "selected_twelvebytwentyfour_numbers";
	} else {
		if (_id.o(id).className == "selected_twelvebytwentyfour_numbersSelect") {
			_id.o(id).style.background = "url(" + projectName + "/LMSImages/images/shade_over.gif) center center no-repeat";
			_id.o(id).style.color = "#FFFFFF";
		} else {
			if (_id.o(id).className == "selected_twelvebytwentyfour_numbers") {
				_id.o(id).style.background = "url(" + projectName + "/LMSImages/images/shade_white.gif) center center no-repeat";
				_id.o(id).style.color = "#00639C";
			}
		}
	}
}
function fillNumTwelveByTwentyFour(val) {
	var length = 0;
	if (!isQP && !staticType) {
		var clrChg = true;
		var obj = _id.o(curSel);
		var elms = document.getElementsByName("selNumTxt");
		if (_id.o(val).className == "selected_twelvebytwentyfour_numbersSelect") {
			for (var l = 0; l < elms.length; l++) {
				if (elms[l].value == val) {
					elms[l].value = "";
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
			for (var l = 0; l < length; l++) {
				if (elms[l].value == "") {
					break;
				}
			}
		}
		twelvebytwentyfourapplyCSS();
		isQP = false;
		_id.o("qpVal").value = "2";
		_id.o("qpChkAll").checked = false;

		//_twelvebytwentyfourCalAmt();
		//_id.o("betAmount").value = parseFloat(_twelvebytwentyfourPrice, 10);
		//_id.o("totAmtLto").value = 0;
		//_id.o("noOfLines").value = 0;
		//_id.o("noOfDrwId").value = 1;

		var playType = _id.o("playType").value;
		if(playType == 'Perm12') {
			var counter = 0;
			for (var l = 0; l < elms.length; l++)
				if (elms[l].value == "")
					counter++;

			counter = maxPickedNum-counter;
			if(counter == 13)
				_id.o("noOfLines").value = 13;
			else if(counter == 14)
				_id.o("noOfLines").value = 91;
			else
				_id.o("noOfLines").value = 0;
			_twelvebytwentyfourCalAmt();
		}
	}
}
function twelvebytwentyfourRow(playType) {
	var htm = "<table width=\"95%\" border=\"0\" cellspacing=\"4\" cellpadding=\"0\">";
	var className = "selected_twelvebytwentyfour";
	if (playType.toLowerCase() == "direct12") {
		htm = htm + "<tr style='display:none'><td class=\"twelvebytwentyfourtxtStyle\" align=\"left\"><input type=\"radio\" name=\"twelvebytwentyfourRadio\" id=\"twelvebytwentyfourRadio\" onclick=\"fillSelTwelveByTwentyFour('twelvebytwentyfourRow')\" checked='true'/>Number Picked:</td><td><input type=\"text\" class=\"twelvebytwentyfourtextbox\" name=\"noPicked\" id=\"noOfQp\" value=\"0\" maxlength=\"2\" readOnly=\"true\" onkeyup=\"return _twelvebytwentyfourfillNoOfQp(this, undefined, event)\" onkeydown=\"return _twelvebytwentyfourfillNoOfQp(this, undefined, event)\" onblur=\"_twelvebytwentyfourCalAmt()\"/></td></tr>";
		htm = htm + "<tr><td colspan='2'><table width=\"95%\" id='twelvebytwentyfourRow' border=\"0\" cellspacing=\"4\" cellpadding=\"0\"><tr id=\"row" + i + "\">";
		htm = htm + "<tr id=\"row" + i + "\">";
		for (var i = 0; i < parseInt(maxPickedNum); i++) {
			if(i%4 == 0) {
				htm = htm + "</tr>";
				htm = htm + "<tr id=\"row" + i + "\">";
			}
			htm = htm + "<td><input type=\"text\" name='selNumTxt' class=\"" + className + "\" maxlength=\"2\" onkeydown=\"return keyPressTwelveByTwentyFour(this,event)\" onkeyup=\"return keyPressTwelveByTwentyFour(this,event)\"  onblur=\"blurFnTwelveByTwentyFour(this)\" onmousedown=\"return disClick(this)\"></td>";
		}
		htm = htm + "</tr>";
		htm = htm + "</tr></table></tr></td>";
	} else if (playType.toLowerCase() == "match10") {
		htm = htm + "<tr style='display:none'><td class=\"twelvebytwentyfourtxtStyle\" align=\"left\"><input type=\"radio\" name=\"twelvebytwentyfourRadio\" id=\"twelvebytwentyfourRadio\" onclick=\"fillSelTwelveByTwentyFour('twelvebytwentyfourRow')\" checked='true'/>Number Picked:</td><td><input type=\"text\" class=\"twelvebytwentyfourtextbox\" name=\"noPicked\" id=\"noOfQp\" value=\"0\" maxlength=\"2\" readOnly=\"true\" onkeyup=\"return _twelvebytwentyfourfillNoOfQp(this, undefined, event)\" onkeydown=\"return _twelvebytwentyfourfillNoOfQp(this, undefined, event)\" onblur=\"_twelvebytwentyfourCalAmt()\"/></td></tr>";
		htm = htm + "<tr><td colspan='2'><table width=\"95%\" id='twelvebytwentyfourRow' border=\"0\" cellspacing=\"4\" cellpadding=\"0\"><tr id=\"row" + i + "\">";
		htm = htm + "<tr id=\"row" + i + "\">";
		for (var i = 0; i < parseInt(maxPickedNum); i++) {
			if(i%5 == 0) {
				htm = htm + "</tr>";
				htm = htm + "<tr id=\"row" + i + "\">";
			}
			htm = htm + "<td><input type=\"text\" name='selNumTxt' class=\"" + className + "\" maxlength=\"2\"   onkeydown=\"return keyPressTwelveByTwentyFour(this,event)\" onkeyup=\"return keyPressTwelveByTwentyFour(this,event)\"  onblur=\"blurFnTwelveByTwentyFour(this)\" onmousedown=\"return disClick(this)\"></td>";
		}
		htm = htm + "</tr>";
		htm = htm + "</tr></table></tr></td>";
	}  else if (playType.toLowerCase() == "statictype") {
		
		htm = htm + "<tr style='display:none'><td class=\"twelvebytwentyfourtxtStyle\" align=\"left\"><input type=\"radio\" name=\"twelvebytwentyfourRadio\" id=\"twelvebytwentyfourRadio\" onclick=\"fillSelTwelveByTwentyFour('twelvebytwentyfourRow')\" checked='true'/>Number Picked:</td><td><input type=\"text\" class=\"twelvebytwentyfourtextbox\" readonly=\"true\" name=\"noPicked\" id=\"noOfQp\" value=\"0\" maxlength=\"2\" readOnly=\"true\" onkeyup=\"return _twelvebytwentyfourfillNoOfQp(this, undefined, event)\" onkeydown=\"return _twelvebytwentyfourfillNoOfQp(this, undefined, event)\" onblur=\"_twelvebytwentyfourCalAmt()\"/></td></tr>";
		htm = htm + "<tr><td colspan='2'><table width=\"95%\" id='twelvebytwentyfourRow' border=\"0\" cellspacing=\"4\" cellpadding=\"0\"><tr id=\"row" + i + "\">";
		htm = htm + "<tr id=\"row" + i + "\">";
		for (var i = 0; i < parseInt(maxPickedNum); i++) {
			if(i%4 == 0) {
				htm = htm + "</tr>";
				htm = htm + "<tr id=\"row" + i + "\">";
			}
			htm = htm + "<td><input type=\"text\" name='selNumTxt' readonly='readonly' class=\"" + className + "\" maxlength=\"2\"   onkeydown=\"return keyPressTwelveByTwentyFour(this,event)\" onkeyup=\"return keyPressTwelveByTwentyFour(this,event)\"  onblur=\"blurFnTwelveByTwentyFour(this)\" onmousedown=\"return disClick(this)\"></td>";
		}
		htm = htm + "</tr>";
		htm = htm + "</tr></table></tr></td>";
	} else if (playType.toLowerCase() == "perm12") {
		htm = htm + "<tr style='display:block'><td class=\"twelvebytwentyfourtxtStyle\" align=\"left\"><input type=\"radio\" name=\"twelvebytwentyfourRadio\" id=\"twelvebytwentyfourRadio\" onclick=\"fillSelTwelveByTwentyFour('twelvebytwentyfourRow')\" checked='true'/>Number Picked:</td><td><input type=\"text\" class=\"twelvebytwentyfourtextbox\" name=\"noPicked\" id=\"noOfQp\" value=\"0\" maxlength=\"2\" readOnly=\"true\" onkeyup=\"return _twelvebytwentyfourfillNoOfQp(this, undefined, event)\" onkeydown=\"return _twelvebytwentyfourfillNoOfQp(this, undefined, event)\" onblur=\"_twelvebytwentyfourCalAmt()\"/></td></tr>";
		htm = htm + "<tr><td colspan='2'><table width=\"95%\" id='twelvebytwentyfourRow' border=\"0\" cellspacing=\"4\" cellpadding=\"0\"><tr id=\"row" + i + "\">";
		htm = htm + "<tr id=\"row" + i + "\">";
		for (var i = 0; i < parseInt(maxPickedNum); i++) {
			if(i%4 == 0) {
				htm = htm + "</tr>";
				htm = htm + "<tr id=\"row" + i + "\">";
			}
			htm = htm + "<td><input type=\"text\" name='selNumTxt' class=\"" + className + "\" maxlength=\"2\"   onkeydown=\"return keyPressTwelveByTwentyFour(this,event)\" onkeyup=\"return keyPressTwelveByTwentyFour(this,event)\"  onblur=\"blurFnTwelveByTwentyFour(this)\" onmousedown=\"return disClick(this)\"></td>";
		}
		htm = htm + "</tr>";
		htm = htm + "</tr></table></tr></td>";
	}

	return htm + "</table>";
}

function fillSelTwelveByTwentyFour(val) {
	curSel = val;
	var temp = val.substring(val.length - 2, val.length);
	if (temp == "UL") {
		_id.o("twelvebytwentyfourRadioUL").checked = true;
		qpType = "noOfQp" + temp;
		maxPickedNum = "4";
	} else {
		if (temp == "BL") {
			_id.o("twelvebytwentyfourRadioBL").checked = true;
			qpType = "noOfQp" + temp;
			maxPickedNum = "20";
		} else {
			_id.o("twelvebytwentyfourRadio").checked = true;
			qpType = "noOfQp";
		}
	}
	if (isQP) {
		_id.o(qpType).focus();
	}
	twelvebytwentyfourapplyCSS();
}
function blurFnTwelveByTwentyFour(obj) {
	if (isNaN(obj.value) || obj.value <= 0 || obj.value > 24) {
		obj.value = "";
	} else {
		if (obj.value.length == 1) {
			obj.value = "0" + obj.value;
		}
	}
	twelvebytwentyfourapplyCSS();
	_twelvebytwentyfourCalAmt();
}
function twelvebytwentyfourclearAllBoxes() {
	if(!staticType) {
		_id.n("noOfDraws")[0].value = 1;
		playType = _id.o("playType").value;
		if (playType == "Direct12" || playType == "Perm12" || playType == "Match10") {
			_id.resetInpTypes("twelvebytwentyfourRow", "selected_twelvebytwentyfour", false);
			_id.o("noOfQp").value = 0;
			_id.o("noOfQp").readOnly = true;
		}
		for (var i = 1; i <= 24; i++) {
			_id.o(i).className = "selected_twelvebytwentyfour_numbers";
			_id.o(i).style.background = "url(" + projectName + "/LMSImages/images/shade_white.gif) center center no-repeat";
			_id.o(i).style.color = "#00639C";
		}
	}

	isQP = false;
	_id.o("betAmount").value = parseFloat(_twelvebytwentyfourPrice, 10);
	_id.o("qpVal").value = "2";
	_id.o("totAmtLto").value = 0;
	_id.o("noOfLines").value = 0;
	_id.o("noOfDrwId").value = 1;
	_id.o("qpChkAll").checked = false;

	document.getElementsByName('conOrNonCon')[0].click();
}
function twelvebytwentyfourChkQP(id) {
	var elms = document.getElementsByName("selNumTxt");
	var playType = _id.o("playType").value;
	if (_id.o(id).checked == true) {
		isQP = true;
		_id.o("qpVal").value = "1";
		for (var l = 0; l < elms.length; l++) {
			elms[l].value = "";
			elms[l].disabled = true;
		}
		if (playType == "Direct12" || playType == "First12" || playType == "Last12" || playType == "AllOdd" || playType == "AllEven" || playType == "OddEven" || playType == "EvenOdd" || playType == "JumpEvenOdd" || playType == "JumpOddEven") {
			_id.o(qpType).value = 12;
			_id.o("noOfLines").value = "1";
			_twelvebytwentyfourCalAmt();
		} else if (playType == "Match10") {
			_id.o(qpType).value = 10;
			_id.o("noOfLines").value = "1";
			_twelvebytwentyfourCalAmt();
		} else if (playType == "Perm12") {
			_id.o(qpType).value = "";
			_id.o(qpType).readOnly = false;
			_id.o(qpType).focus();
			_id.o("noOfLines").value = "0";
			_id.o("totAmtLto").value = "0";
		}
	} else {
		isQP = false;
		_id.o("qpVal").value = "2";
		for (var l = 0; l < elms.length; l++) {
			elms[l].value = "";
			elms[l].disabled = false;
		}

		_id.o(qpType).value = 0;
		//_id.o("noOfLines").value = 0;
		//_id.o("totAmtLto").value = 0;
		_id.o(qpType).readOnly = true;
	}
	twelvebytwentyfourapplyCSS();
}
function _twelvebytwentyfourCalAmt(changeBetAmt) {
	var noOfDrw = 0;
	if(_id.n('conOrNonCon')[0]!= undefined && _id.n('conOrNonCon')[0].checked){
		noOfDrw = _id.n("noOfDraws")[0].value;
	}else if(_id.n('conOrNonCon')[1]!= undefined && _id.n('conOrNonCon')[1].checked){
		noOfDrw = _twelvebytwentyfourDrawIdArr.length;
	}
		_twelvebytwentyfourfetchLines(changeBetAmt);
		var amt = _id.v("totAmtLto");
		if (isNaN(amt)) {
			amt = 0;
		}
		var totalPrice=noOfDrw * amt;
		//totalPrice=tktPriceRound(totalPrice);
		totalPrice = parseFloat(totalPrice).toFixed(1);
		_id.o("totAmtLto").value = totalPrice;
}
function _twelvebytwentyfourfillNoOfQp(obj, val, e) {
	var keyCode = (window.event) ? event.keyCode : e.which;
	if ((keyCode >= 48 && keyCode < 58) || keyCode == 45 || keyCode == 27 || keyCode == 13 || keyCode == 40 || keyCode == 38 || keyCode == 46 || keyCode == 8 || (keyCode >= 96 && keyCode < 106 || keyCode == 9)) {
		if (keyCode == 46 || keyCode == 8 || keyCode == 9) {
			if (typeof val != "undefined" && keyCode == 9) {
				fillSelTwelveByTwentyFour("twelvebytwentyfourRow" + val);
			}
			_twelvebytwentyfourCalAmt();
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
	_twelvebytwentyfourCalAmt();
	return true;
}
function keyPressTwelveByTwentyFour(obj, e) {
	var keyCode = (window.event) ? event.keyCode : e.which;
	if ((keyCode >= 48 && keyCode < 58) || keyCode == 45 || keyCode == 27 || keyCode == 13 || keyCode == 40 || keyCode == 38 || keyCode == 46 || keyCode == 8 || (keyCode >= 96 && keyCode < 106 || keyCode == 9)) {
		if (keyCode == 46 || keyCode == 8 || keyCode == 9) {
			return true;
		}
		if (obj.value.length > 1) {
			if (obj.value == 0 || obj.value > 24 || isNaN(obj.value)) {
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
			twelvebytwentyfourapplyCSS();
		}
	} else {
		return false;
	}

	return true;
}
function twelvebytwentyfourapplyCSS() {
	for (var i = 1; i <= 24; i++) {
		_id.o(i).className = "selected_twelvebytwentyfour_numbers";
		_id.o(i).style.background = "url(" + projectName + "/LMSImages/images/shade_white.gif) center center no-repeat";
		_id.o(i).style.color = "#00639C";
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
			_id.o(parseInt(elms[l].value, 10)).className = "selected_twelvebytwentyfour_numbersSelect";
			_id.o(parseInt(elms[l].value, 10)).style.background = "url(" + projectName + "/LMSImages/images/shade_over.gif) center center no-repeat";
			_id.o(parseInt(elms[l].value, 10)).style.color = "#FFFFFF";
			count += 1;
		}
	}
	if (!isQP) {
		_id.o(qpType).value = count;
	}

	if ((count == totalPicked) || isQP) {
		for (var l = 1; l <= 24; l++) {
			if (_id.o(l).className != "selected_twelvebytwentyfour_numbersSelect") {
				_id.o(l).className = "selected_twelvebytwentyfour_numbersDisabled";
				_id.o(l).style.background = "url(" + projectName + "/LMSImages/images/shade_disable.gif) center center no-repeat";
			}
		}
	}
}
function buyTickettwelvebytwentyfour(id) {
	var compData = "";
	var playType = _id.o("playType").value;
	if (_id.o("qpChkAll").checked == false) {
		var inputData = new Array();
		var obj = _id.o("twelvebytwentyfourRow");
		var elms = obj.getElementsByTagName("input");
		var count = 0;
		for (var l = 0; l < elms.length; l++) {
			if (elms[l].value != "") {
				inputData[count++] = elms[l].value;
			}
		}

		if (inputData.length < maxPickedNum && playType != "Perm12") {
			alert("Please Pick " + maxPickedNum + " Numbers");
			return false;
		} else if(playType == "Perm12") {
			if (inputData.length < 13) {
				alert("Please Pick Minimum 13 Numbers");
				return false;
			}
		}
		compData = compData + inputData.toString();
	} else {
		compData = "QP";
		var qpCheck = false;
		if (playType == "Direct12" || playType == "Perm12" || playType == "First12") {
			var qpVal = _id.o(qpType).value;
			if (qpVal == 0 || qpVal == "") {
				alert("Please fill incomplete entries");
				qpCheck = true;
			} else {
				if (playType == "Perm12" && qpVal < 13) {
					alert("Please Pick Minimum 13 Numbers");
					qpCheck = true;
				}

				if (playType == "Perm12" && qpVal > 14) {
					alert("Please Pick Maximum 14 Numbers");
					qpCheck = true;
				}
			}
		}

		if (qpCheck) {
			return false;
		}
	}
	var amount = _id.o("totAmtLto").value;
	var betAmt = _id.o("betAmount").value;

	betAmt=Math.round(betAmt/unitBetTypePriceArrTwelveByTwentyFour[playType].unitBetTypePriceTwelveByTwentyFour);
	_id.o(id).disabled = true;

	if(_id.n('conOrNonCon')[1]!= undefined && _id.n('conOrNonCon')[1].checked) {
		var html = '';
		for(i=0; i<_twelvebytwentyfourDrawIdArr.length;i++){
			html += '&drawIdArr='+_twelvebytwentyfourDrawIdArr[i];
		}
		html+='&isAdvancedPlay=1';
		gameBuyAjaxReq("twelveByTwentyFourBuy.action?pickedNumbers=" + compData + "&noOfDraws=" + _id.n("noOfDraws")[0].value + "&totalPurchaseAmt=" + amount + "&noPicked=" + _id.n("noOfQp")[0].value + "&playType=" + playType + "&QP=" + _id.n("qpVal")[0].value + "&betAmt=" + betAmt + "&noOfLines=" + _id.n("noOfLines")[0].value + html);
	} else {
		gameBuyAjaxReq("twelveByTwentyFourBuy.action?pickedNumbers=" + compData + "&noOfDraws=" + _id.n("noOfDraws")[0].value + "&totalPurchaseAmt=" + amount + "&noPicked=" + _id.n("noOfQp")[0].value + "&playType=" + playType + "&QP=" + _id.n("qpVal")[0].value + "&betAmt=" + betAmt + "&noOfLines=" + _id.n("noOfLines")[0].value);
	}

	twelvebytwentyfourclearAllBoxes();
	return true;
}

var lines = 0;
function _twelvebytwentyfourfetchLines(changeBetAmt) {
	var playType = _id.o("playType").value;
	lines = 0;
	if (playType == "Direct12" || playType == "Match10" || playType == "First12" || playType == "Last12" || playType == "AllOdd" || playType == "AllEven" || playType == "OddEven" || playType == "EvenOdd" || playType == "JumpEvenOdd" || playType == "JumpOddEven") {
		lines = 1;
	} else if (playType == "Perm12") {
		var num = _id.o("noOfQp").value;
		if (isNaN(num) || num == "") {
			_id.o(qpType).value = "";
		} else {
			if (num == 13) {
				lines = 13;
			} else if (num == 14) {
				lines = 91;
			}
		}
	}
	if (isNaN(lines)) {
		lines = 0;
	}
	_id.o("noOfLines").value = lines;
	_id.o("totAmtLto").value = lines * _id.o("betAmount").value;
}
function _twelvebytwentyfourChkPickType(id) {
	staticType = false;
	var playType = _id.o(id).value;
	_id.o("playType").value = playType;
	_twelvebytwentyfourPrice = unitBetTypePriceArrTwelveByTwentyFour[playType].unitBetTypePriceTwelveByTwentyFour;
	_id.o("twelvebytwentyfourPrice").innerHTML =currSymbol + " "+ _twelvebytwentyfourPrice + "/-";
	
	_id.o("betAmount").value = parseFloat(_twelvebytwentyfourPrice, 10);

	if (playType == "Direct12") {
		maxPickedNum = 12;
		totalPicked = maxPickedNum;
		_id.o("twelvebytwentyfourRowDiv").innerHTML = twelvebytwentyfourRow(playType);
		curSel = "twelvebytwentyfourRow";
		qpType = "noOfQp";
	} else if (playType == "Match10") {
		maxPickedNum = 10;
		totalPicked = maxPickedNum;
		_id.o("twelvebytwentyfourRowDiv").innerHTML = twelvebytwentyfourRow(playType);
		curSel = "twelvebytwentyfourRow";
		qpType = "noOfQp";
	} else if (playType == "First12" || playType == "Last12" || playType == "AllOdd" || playType == "AllEven" || playType == "OddEven" || playType == "EvenOdd" || playType == "JumpEvenOdd" || playType == "JumpOddEven") {
		staticType = true;
		maxPickedNum = 12;
		totalPicked = maxPickedNum;
		_id.o("twelvebytwentyfourRowDiv").innerHTML = twelvebytwentyfourRow("staticType");
		curSel = "twelvebytwentyfourRow";
		qpType = "noOfQp";

		var numbers;
		if(playType == 'First12')
			numbers = ["01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12"];
		else if(playType == 'Last12')
			numbers = ["13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24"];
		else if(playType == 'AllOdd')
			numbers = ["01", "03", "05", "07", "09", "11", "13", "15", "17", "19", "21", "23"];
		else if(playType == 'AllEven')
			numbers = ["02", "04", "06", "08", "10", "12", "14", "16", "18", "20", "22", "24"];
		else if(playType == 'OddEven')
			numbers = ["01", "03", "05", "07", "09", "11", "14", "16", "18", "20", "22", "24"];
		else if(playType == 'EvenOdd')
			numbers = ["02", "04", "06", "08", "10", "12", "13", "15", "17", "19", "21", "23"];
		else if(playType == 'JumpEvenOdd')
			numbers = ["03", "04", "07", "08", "11", "12", "15", "16", "19", "20", "23", "24"];
		else if(playType == 'JumpOddEven')
			numbers = ["01", "02", "05", "06", "09", "10", "13", "14", "17", "18", "21", "22"];

		var elms = document.getElementsByName("selNumTxt");
		for (var l = 0; l < elms.length; l++) {
			elms[l].value = numbers[l];
		}

		twelvebytwentyfourapplyCSS();
		_id.o(qpType).value = 12;
	} else if (playType == "Perm12") {
		maxPickedNum = 14;
		totalPicked = maxPickedNum;
		curSel = "twelvebytwentyfourRow";
		_id.o("twelvebytwentyfourRowDiv").innerHTML = twelvebytwentyfourRow(playType);
		qpType = "noOfQp";
	}

	if (playType == "Direct12" || playType == "Match10" || playType == "First12" || playType == "Last12" || playType == "AllOdd" || playType == "AllEven" || playType == "OddEven" || playType == "EvenOdd" || playType == "JumpEvenOdd" || playType == "JumpOddEven") {
		_id.o("noOfLines").value = 1;
	} else if (playType == "Perm12") {
		_id.o("noOfLines").value = 0;
	}

	twelvebytwentyfourclearAllBoxes();
	//_id.o(qpType).value = 0;
	_twelvebytwentyfourCalAmt();

	document.getElementById("qpChkAll").disabled = staticType;
	document.getElementById("qpChkAll").checked = false;
}
function _twelvebytwentyfourBetAmountMultiple(ele, func) {
	var num = _id.v(ele);
	var playType = _id.o("playType").value;
	if (func == "plus") {
		_id.o(ele).value = Math.round(((num < 100) ? (parseFloat(num == _twelvebytwentyfourPrice ? _twelvebytwentyfourPrice : num, 10) + parseFloat(_twelvebytwentyfourPrice)) : num)*100)/100;
	} else {
		_id.o(ele).value = Math.round(((num > parseFloat(_twelvebytwentyfourPrice)) ? (parseFloat(num == _twelvebytwentyfourPrice ? _twelvebytwentyfourPrice : num, 10) - parseFloat(_twelvebytwentyfourPrice)) : num)*100)/100;
	}
	_twelvebytwentyfourCalAmt(true);
}
var unitBetPriceTwelveByTwentyFour = gameDispName["twelvebytwentyfour"].unitPrice.split(",");
var unitBetTypePriceArrTwelveByTwentyFour = new Object();
function betTypePriceTwelveByTwentyFour(value) {
	this.unitBetTypePriceTwelveByTwentyFour = value;
}
function getPlayTypeUnitPriceTwelveByTwentyFour() {
	for (var i = 0; i < unitBetPriceTwelveByTwentyFour.length; i++) {
		var pickTypeOptVal = unitBetPriceTwelveByTwentyFour[i].replace(" ", "").split("=");
		unitBetTypePriceArrTwelveByTwentyFour[pickTypeOptVal[0]] = new betTypePriceTwelveByTwentyFour(pickTypeOptVal[1]);
	}
}
getPlayTypeUnitPriceTwelveByTwentyFour();
_twelvebytwentyfourPrice = unitBetTypePriceArrTwelveByTwentyFour["Direct12"].unitBetTypePriceTwelveByTwentyFour;

var line = "<tr><td bgcolor=\"#00639C\"><table border=\"0\" bordercolor=\"blue\" width=\"97%\"> <tr ><td align=\"left\" width=\"25%\"> <input type=\"hidden\" value=\"0\" id=\"counterId\"> <input type=\"hidden\" id=\"dateFuture\"> <input type=\"hidden\" id=\"freezeTimeId\" value=\"" + _twelvebytwentyfourfreezeTime + "\"> <span class=\"style1\">Next Draw :</span><div id=\"latestDrawTime\" style=\"display:none\"></div></td><td width=\"20%\" align=\"left\"><div id=\"nxtDrawTime\" class=\"twelvebytwentyfourstyle2\"></div></td><td align=\"right\" width=\"25%\"><div id=\"timeLeft\"> <span class=\"style1\"> Time Left :</div></span></td> <td align=\"left\" width=\"15%\"><div id=\"curDrwTime\" class=\"twelvebytwentyfourstyle2\"></div> </td><td rowspan=\"2\" width=\"10%\"><img src=\"" + fortImgPath + "que.gif\" style=\"cursor:hand\" onClick=\"gameInfo()\" alt=\"Game Info\" /> </td></tr> <tr bgcolor=\"#00639C\"> <td align=\"left\"> <span class=\"style1\">No. of Draws :</span> </td> <td >         <table><tr><td> <input type=\"radio\"/ name=\"conOrNonCon\" value=\"consec\" checked='true' onclick=\"drawSelectButton(this)\"'/></td><td> <select\tname=\"noOfDraws\" id=\"noOfDrwId\"\tclass=\"fortune_option\" align=\"left\"\twidth=\"20%\"\tonchange=\"_twelvebytwentyfourCalAmt()\">" + getActiveDrawOptionList("twelvebytwentyfour") + "</select> </td></tr></table>        </td><td>     <table><tr><td><input type=\"radio\"/ name=\"conOrNonCon\" value=\"nonconsec\" onclick=\"specificDrawPopUp(_twelvebytwentyfourdrawSelect)\" /></td><td class=\"style1\">Advance Draw</td></tr></table>     </td><td colspan=\"1\" align=\"left\" valign=\"top\"> &nbsp; </td><td align=\"left\"> <span class=\"twelvebytwentyfournum\" id='twelvebytwentyfourPrice'>"+currSymbol+" " + parseFloat(_twelvebytwentyfourPrice, 10) + "/-</span></td></tr><tr><td><table><tr><td align=\"right\" height=\"0px\" width=\"0px\" id=\"drawSelectorDiv\" style=\"visibility:hidden;position:absolute;background-color:#FFFFFF\"></td></tr></table></td></tr>  </table></td></tr>";
var _twelvebytwentyfourNxtDrwTbl = _twelvebytwentyfourFillNxtDrw();
var _twelvebytwentyfourHtmlTbl = "<table width=\"100%\"height=\"100%\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\" bordercolor=\"red\" bgcolor=\"#9CCEFF\">" + line + "<tr><td align=\"center\" valign=\"top\"><table width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\"><tr><td width=\"65%\" valign=\"top\" ><div style=\"padding-top:10px;\"><table border=\"0\" bordercolor=\"red\" width=\"97%\" align=\"center\"cellpadding=\"0\"cellspacing=\"0\"><tr><td class=\"style10\" align=\"left\"><div align=\"center\" valign=\"middle\" style=\"height:18px;background-color:#00639C;width:180px;float:left;padding-top:2px\">&nbsp;Select Number Panel&nbsp;</div></td></tr><tr><td valign=\"middle\" style=\"border:0px #00639C solid;background-color:#639CCE\">" + twelvebytwentyfourNmSel(24) + "</td></tr><tr><td><div id=\"selectedDrawList\">No Draws Selected. Please Select at least one draw!</div></td></tr></table></div></td><td valign=\"top\" wdith=\"35%\"><div style=\"border:0px solid black; padding-top:10px;\"><table border=\"0\" bordercolor=\"red\" width=\"96%\" cellpadding=\"0\" cellspacing=\"0\" align=\"center\" style=\"margin-right:10px\"><tr><td align=\"right\"class=\"style10\"style=\"cursor:hand;\"onclick=\"twelvebytwentyfourclearAllBoxes()\"><div align=\"center\" valign=\"middle\" style=\"height:18px;background-color:#00639C;width:100px;float:right;padding-top:2px\">&nbsp;&nbsp;&nbsp;Clear All&nbsp;&nbsp;&nbsp;</div></td></tr><tr><td valign=\"middle\" style=\"border:1px #00639C solid;background-color:#639CCE\"><table width=\"100%\" border=\"0\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\"><tr><td bgcolor=\"#00639C\"><input type=\"hidden\" value=\"Direct12\" name=\"playType\" id=\"playType\"><select name=\"pickType\" id=\"pickType\" style=\"width:100%;background-color:#00639C;color:#FFFFFF;font-size:12px;font-family:Arial;font-weight:bold\" onchange=\"_twelvebytwentyfourChkPickType(this.id)\"><option value=\"Direct12\">Direct12</option><option value=\"First12\">First12</option><option value=\"Last12\">Last12</option><option value=\"AllOdd\">AllOdd</option><option value=\"AllEven\">AllEven</option><option value=\"OddEven\">OddEven</option><option value=\"EvenOdd\">EvenOdd</option><option value=\"JumpEvenOdd\">JumpEvenOdd</option><option value=\"JumpOddEven\">JumpOddEven</option><option value=\"Perm12\">Perm12</option></select></td></tr><tr><td><div id='twelvebytwentyfourRowDiv' style='height:220px'>" + twelvebytwentyfourRow("Direct12") + "</div></td></tr><tr><td bgcolor=\"#00639C\"><table width=\"95%\" border=\"0\"><tr><td class=\"twelvebytwentyfourtxtStyle\" align=\"left\">Unit Price</td><td><img src=\"" + fortImgPath + "minus.gif\"  style=\"cursor:pointer\" alt=\"minus\" onclick=\"_twelvebytwentyfourBetAmountMultiple('betAmount','minus')\"/><input type=\"text\" class=\"twelvebytwentyfourtextbox\" name=\"betAmount\" id=\"betAmount\" value='"+_twelvebytwentyfourPrice+"' readOnly=\"true\" style=\"width:40px\" /><img src=\"" + fortImgPath + "plus.gif\" alt=\"Plus\" onclick=\"_twelvebytwentyfourBetAmountMultiple('betAmount','plus')\" style=\"cursor:pointer\"/></td></tr><tr><td class=\"twelvebytwentyfourtxtStyle\" align=\"left\">No Of Lines :</td><td><input type=\"text\" class=\"twelvebytwentyfourtextbox\" name=\"noLines\" id=\"noOfLines\" value=\"1\" maxlength=\"2\" readOnly=\"true\" /></td></tr><tr><td class=\"twelvebytwentyfourtxtStyle\" align=\"left\">Total Amount:</td><td><input type=\"text\" id=\"totAmtLto\" name=\"totalPurchaseAmt\" value=\""+_twelvebytwentyfourPrice+"\" readonly=\"true\" class=\"twelvebytwentyfourtextbox\"/></td></tr><tr><td align=\"center\"class=\"style5\"><div style=\"border:1px solid #639CCE;width:50px;height:42px; float:left\">QP<br/><input type=\"hidden\" value=\"2\" name=\"QP\" id=\"qpVal\"><input type=\"checkbox\"id=\"qpChkAll\" onclick=\"twelvebytwentyfourChkQP(this.id)\"/></div></td><td><a href=\"#\" ><img id='buy' src=\"" + fortImgPath + "buy_lotto.gif\" onclick=\"return buyTickettwelvebytwentyfour(this.id)\"alt=\"Buy\" style=\"margin-right:4px;border: 1px #9CCEFF solid;\"/></a></td></tr></table></td></tr></table></td></tr></table></div></td></tr></table></tr><tr><td>&nbsp;</td></tr></table>";
var _twelvebytwentyfourHTML = "<div id=\"strip\">" + drwMenu + "</div><form style=\"background-color:#FFFFFF;border: 1px solid #393939;\"><table  border=\"0\" width=\"100%\" height=\"100%\" cellspacing=\"0\" cellpadding=\"0\" align=\"center\" bgcolor=\"#FFFFFF\"  bordercolor=\"red\"><tr><td width=\"10%\"  valign=\"top\" align=\"center\" onmousedown=\"return disClick(this)\"><input type=\"hidden\" id=\"drawType\"><div id=\"winningDispDiv\" style=\"border:1px #00639C solid;background-color:#639CCE;margin-left:5px;margin-top:3px\"></div><div id=\"nxtDrawDiv\" style=\"border:1px #00639C solid;background-color:#639CCE;margin-left:5px;margin-top:5px\"></div></td><td width=\"90%\" valign=\"top\" align=\"right\"><div id=\"twelvebytwentyfourmiddle\" style=\"border:1px #00639C solid;background-color:#639CCE;width:98%;margin-left:5px;margin-top:3px;margin-bottom:1px\">" + _twelvebytwentyfourHtmlTbl + "</div></td></tr></table></td></tr></table></form>";

parent.frames[0].Version['$Source: /rep/LMS_Mgmt/WebRoot/com/skilrock/lms/web/drawGames/playMgmt/games/twelvebytwentyfour/twelvebytwentyfour.js,v $'] = '$Id: twelvebytwentyfour.js,v 1.3 2016/10/31 09:46:47 neeraj Exp $';