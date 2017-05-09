
var _bonusballtwoWinArr = new Array();
var _bonusballtwoDrawIdArr = new Array();
var curDrwTimelotto = "";
var bonusballtwocurSel = "row1";
var bonusballtwoIsAllQP = false;
var _bonusballtwoPrice = parseFloat(gameDispName["bonusballtwo"].unitPrice.split("=")[1], 10);
var playType;
var startPoint;
var betAmt;
var _bonusballtwodrawSelect;
function setDefaultbonusballtwo() {
	curDrwTimefortune = "";
	bonusballtwocurSel = "row1";
	bonusballtwoIsAllQP = false;
	playType = "Direct5";
	startPoint = 1;
}
function _bonusballtwoWinAppend() {
	if (!_bonusballtwoWin.match("Result Awaited")) {
		_bonusballtwoWin = "Result Awaited=\"\" Nxt" + _bonusballtwoWin;
	}
}
function _bonusballtwoLatestDrawTime() {
	return _bonusballtwoStatus.split("=")[1];
}
function _bonusballtwoFillWinTbl() {
	var _bonusballtwoWinHt = "<table width=\"100%\" border=\"0\" bordercolor=\"red\" cellspacing=\"0\" cellpadding=\"0\" align=\"center\" bgcolor=\"#a0e352\"><tr><td colspan=\"2\" align=\"left\"><div valign=\"middle\" style=\"height:22px;background-color:#4d8f00;padding-top:3px;font-size:16px;font-family:Arial;font-weight:bold;color:#FFFFFF\">&nbsp;Win Result&nbsp;</div></td></tr>      <tr><td valign=\"top\" align=\"center\" height=\"100\">";
	var winArr = _bonusballtwoWin.split("Nxt");
	var _bonusballtwoWinHTML = "<table width=\"100%\" border=\"0\" bordercolor=\"red\" style=\"margin-top:3px\"><tr><td><div style=\"" + scrollColor("#7dc924", "#4d8f00", "184") + "\">";
	for (var i = 0; i <= winArr.length - 2; i++) {
		if (!winArr[i].match("Result")) {
			var time = new Date(parseInt(winArr[i].split("=")[0].replace(" ", "")));
			var newTime = week[time.getDay()] + " " + time.getDate() + " " + month[time.getMonth()] + "<br/>" + (time.getHours() < 10 ? "0" + time.getHours() : time.getHours()) + ":" + (time.getMinutes() < 10 ? "0" + time.getMinutes() : time.getMinutes()) + ":" + (time.getSeconds() < 10 ? "0" + time.getSeconds() : time.getSeconds());
			//alert(newTime);
			var htm = "<table width=\"90%\" border=\"0\" bordercolor=\"green\" id=\"result" + i + "\" cellspacing=\"2\" cellpadding=\"10\" class=\"bonusballtwo_winningResult_table\" ><tr><td class=\"bonusballtwowinningResultTime\" colspan=\"8\" height=\"53\">" + newTime + "</td></tr><tr>";
			var num = (winArr[i].split("=")[1]).split(",");
			for (var j = 0; j < num.length; j++) {
				var temp = (num[j] < 10 ? "0" + num[j] : num[j]);
				htm = htm + "<td width=\"10%\" class=\"selected_win_bonusballtwo_numbers\" align=\"center\">" + temp + "</td>";
			}
			htm = htm + "</tr></table>";
			_bonusballtwoWinHTML = _bonusballtwoWinHTML + htm;
		} else {
			var htm = "<table width=\"90%\" bordercolor=\"blue\" border=\"0\" cellspacing=\"2\" cellpadding=\"10\" id=\"result" + i + "\" class=\"bonusballtwo_winningResult_table\" ><tr><td class=\"bonusballtwowinningResultTime\" colspan=\"8\" height=\"53\" >Result Awaited</td></tr><tr>";
			htm = htm + "<td class=\"selected_win_bonusballtwo_numbers\"  colspan=\"8\"><div style=\"text-align:center\" id=\"winAwaitedDiv\" style=\"color:#00466E\"></div></td>";
			htm = htm + "</tr></table>";
			_bonusballtwoWinHTML = _bonusballtwoWinHTML + htm;
		}
	}
	_bonusballtwoWinHTML = _bonusballtwoWinHTML + "</div></td></tr></table>";
	return _bonusballtwoWinHt + _bonusballtwoWinHTML + "</td></tr></table>";
}

function _bonusballtwoWinAjx() {
	var url = "fetchWinbonusballtwo.action?frmDate=" + _bonusballtwoWinArr[_bonusballtwoWinArr.length - 1];
	_bonusballtwoWin = _bonusballtwoWin + (winAjaxReq(url)).data;
	_bonusballtwoFillWinTbl();
}
function bonusballtwoNmSel(lstNo) {
	var htm = "<div id=\"\"><table width=\"100%\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\" bgcolor=\"#7dc924\" style=\"margin-top:3px;margin-bottom:3px\"> <tr>";
	for (var i = 1; i <= lstNo; i++) {
		if (i % 5 == 0) {
			htm = htm + "<td width=\"17%\" align=\"right\" valign=\"middle\" style=\"cursor:pointer;background:url(" + projectName + "/LMSImages/images/bonusballtwo_shade_white.gif) center no-repeat\" class=\"selected_bonusballtwo_numbers\" onmouseout=\"bonusballtwoonmouseouteffect(this.id)\" onmouseover=\"bonusballtwoonmouseovereffect(this.id)\"  onclick=\"fillNumbonustwo(" + i + ")\"  id=\"" + i + "\" >" + (i < 10 ? "0" + i : i) + "</td></tr>";
		} else {
			htm = htm + "<td width=\"17%\" align=\"right\" valign=\"middle\"  class=\"selected_bonusballtwo_numbers\"  style=\"cursor:pointer;background:url(" + projectName + "/LMSImages/images/bonusballtwo_shade_white.gif) center no-repeat\" onmouseout=\"bonusballtwoonmouseouteffect(this.id)\" onmouseover=\"bonusballtwoonmouseovereffect(this.id)\" onclick=\"fillNumbonustwo(" + i + ")\"  id=\"" + i + "\"  >" + (i < 10 ? "0" + i : i) + "</td>";
		}
	}
	htm = htm + "</tr></table></div>";
	return htm;
}
function bonusballtwoonmouseovereffect(id) {
	var count = 0;
	var isFilled = false;
	for (var i = 1; i <= 30; i++) {
		if (_id.o(i).className == "selected_bonusballtwo_numbersSelect") {
			count++;
		}
		if (count > 5) {
			isFilled = true;
			break;
		}
	}
	if (!isFilled && !bonusballtwoIsAllQP) {
		if (_id.o(id).className != "selected_bonusballtwo_numbersSelect") {
			_id.o(id).style.background = "url(" + projectName + "/LMSImages/images/bonusballtwo_shade_over.gif) center no-repeat";
			_id.o(id).style.color = "#FFFFFF";
			_id.o(id).className = "selected_bonusballtwo_numbersSelectOMO";
		}
	}
}
function bonusballtwoonmouseouteffect(id) {
	if (!bonusballtwoIsAllQP) {
		if (_id.o(id).className == "selected_bonusballtwo_numbersSelectOMO") {
			_id.o(id).style.background = "url(" + projectName + "/LMSImages/images/bonusballtwo_shade_white.gif) center no-repeat";
			_id.o(id).style.color = "#155E33";
			_id.o(id).className = "selected_bonusballtwo_numbers";
		} else {
			if (_id.o(id).className == "selected_bonusballtwo_numbersSelect") {
				_id.o(id).style.background = "url(" + projectName + "/LMSImages/images/bonusballtwo_shade_over.gif) center no-repeat";
				_id.o(id).style.color = "#FFFFFF";
			} else {
				if (_id.o(id).className == "selected_bonusballtwo_numbers") {
					_id.o(id).style.background = "url(" + projectName + "/LMSImages/images/bonusballtwo_shade_white.gif) center no-repeat";
					_id.o(id).style.color = "#155E33";
				}
			}
		}
	}
}
function bonusballtwoChkQP(id) {
	if (_id.o(id).checked == true) {
		bonusballtwoIsAllQP = true;
		//_id.o("tkts").readOnly = false;
		bonusballtwoapplyCSS();
		if(playType=='Direct5'){
		
		_id.o("tkts").value = "4";
		_id.o("tkts").readOnly = true;
        _id.o("tkts").focus();
		_bonusballtwoCalAmt();
	}else if(playType=='Perm5'){
_id.o("tkts").value="";
		_id.o("totAmtLto").value="";
		_id.o("noPicked").value = "";
		_id.o("noPicked").readOnly = false;
		_id.o("noPicked").focus();
}
	}
	else {
bonusballtwoIsAllQP = false;
		_id.o("tkts").value = "0";
		_id.o("tkts").readOnly = true;
		if(playType=='Direct5'){
			_id.o("radio1").checked = true;
			bonusballtwocurSel = "row1";
		}else if(playType=='Perm5'){
			_id.o("noPicked").readOnly = true;
			_id.o("noPicked").value = "0";
		}
bonusballtwoapplyCSS();
		}
}


function bonusballtwoRow(noOfpanels) {
	var htm = "<table width=\"100%\" id=\"lottoRow\" border=\"0\" cellspacing=\"2\" cellpadding=\"0\"><tr> <td width=\"8%\" align=\"center\" valign=\"middle\">&nbsp;</td><td width=\"10%\" align=\"center\" valign=\"middle\" class=\"style5\">&nbsp;</td>";
	for (var indx = 1; indx < 6; indx++) {
		htm = htm + "<td width=\"12%\" align=\"center\" valign=\"middle\" style=\"color:#155E33;text-align: center;font-weight:bold;font-size:16px\">" + indx + "</td>";
	}
	htm = htm + "</tr> <tr>";
	for (var i = 1; i < parseInt(noOfpanels) + 1; i++) {
		htm = htm + "<tr onclick=\"fillSelbonustwo(" + i + ")\" id=\"row" + i + "\"><td align=\"center\" style=\"color:#155E33;text-align: center;font-weight:bold;font-size:16px\">" + i + "</td><td><input type=\"radio\" name=\"lottoRadio\" id=\"radio" + i + "\" style=\"border:1px #7dc924 solid\" onclick=\"fillSelbonustwo(" + i + ")\" " + (i == 1 ? "checked" : "") + "/></td>";
		for (var j = 0; j < 5; j++) {
			htm = htm + "<td align=\"center\" valign=\"middle\" ><input type=\"text\" class=\"selected_bonusballtwo\" maxlength=\"2\" onkeydown=\"return keyPressbonusTwo(this," + i + ",event)\" onkeyup=\"return keyPressbonusTwo(this," + i + ",event)\" onblur=\"blurFnbonustwo(this)\" onmousedown=\"return disClick(this)\"></td>";
		}
		htm = htm + "</tr>";
	}
	return htm + "</table>";
}
function bonusballtwoclearBoxes(val) {
	_id.resetInpTypes("row" + val, "selected_bonusballtwo", false);
	_bonusballtwoCalAmt();
}
function bonusballtwoclearAllBoxes() {
	_id.resetInpTypes("lottoRow", "selected_bonusballtwo", false);
	_id.o("noOfDraws").value = 1;
	_id.o("tkts").readOnly = true;
	//bonusballtwocurSel = "row1";
	bonusballtwoIsAllQP = false;
	_id.o("betAmount").value = _bonusballtwoPrice;
	bonusballtwoapplyCSS();
if(playType!='Perm5'){
	bonusballtwocurSel = "row1";
	}
	_id.o("qpChkAll").checked = false;
	_bonusballtwoCalAmt();
	document.getElementsByName('conOrNonCon')[0].click();

}
function _bonusballtwoFillNxtDrw() {
	var nxtDrwHTML = "<table width=\"100%\" cellspacing=\"0\"cellpadding=\"0\"  border=\"0\" bordercolor=\"red\"><tr><td colspan=\"2\" align=\"left\" valign=\"top\"><div valign=\"middle\" style=\"height:22px;width:180px;background-color:#4d8f00;padding-top:3px;font-size:16px;font-family:Arial;font-weight:bold;color:#FFFFFF\">&nbsp;Next Draw&nbsp;</div></td></tr><tr><td align=\"center\"valign=\"top\"><table width=\"100%\" bordercolor=\"red\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\" bgcolor=\"#7dc924\"><tr><td width=\"100%\" align=\"left\" valign=\"top\" ><div style=\"" + scrollColor("#7dc924", "#4d8f00", "175") + "\"><table width=\"90%\" align=\"left\" bordercolor=\"green\"  border=\"0\" cellspacing=\"0\" cellpadding=\"0\" class=\"borderbtmbonusballtwo\">";
	_bonusballtwodrawSelect = '<table width="auto" cellspacing="0"cellpadding="0"  border="1"><tr><td colspan="3" align="center"><b>Select Draws</td></tr>';
	var nxtDrwArr = _bonusballtwoDrawTime.split(",");
	for (var i = 0, l = nxtDrwArr.length; i < l; i++) {
		var time = new Date(parseInt(nxtDrwArr[i].replace(" ", "")));
		var newTime = week[time.getDay()] + " " + time.getDate() + " " + month[time.getMonth()] + " " + (time.getHours() < 10 ? "0" + time.getHours() : time.getHours()) + ":" + (time.getMinutes() < 10 ? "0" + time.getMinutes() : time.getMinutes());
		var hiddenTime = "<input type='text' id='timer" + i + "'>";
		var rowcolor;
		var data = "";
		if (i == 0) {
			data = time.getFullYear() + "-" + time.getMonth() + "-" + time.getDate() + "-" + (time.getHours() < 10 ? "0" + time.getHours() : time.getHours()) + "-" + (time.getMinutes() < 10 ? "0" + time.getMinutes() : time.getMinutes()) + "-" + (time.getSeconds() < 10 ? "0" + time.getSeconds() : time.getSeconds());
			var dateFuture1 = new Date(time.getFullYear(), time.getMonth(), time.getDate(), (time.getHours() < 10 ? "0" + time.getHours() : time.getHours()), (time.getMinutes() < 10 ? "0" + time.getMinutes() : time.getMinutes()), (time.getSeconds() < 10 ? "0" + time.getSeconds() : time.getSeconds()));
			_bonusballtwoNxtDrawTime = ("" + time).substring(0, ("" + time).lastIndexOf(":") + 3);
		}
		if (i % 2 == 0) {
			rowcolor = "#A0E352";
		} else {
			rowcolor = "#7dc924";
		}
		nxtDrwHTML = nxtDrwHTML + "<tr><td width=\"100%\"><input type=\"hidden\" value=\"" + data + "\" id=\"" + i + "timer\"></td></tr><tr bgcolor=\"" + rowcolor + "\" style=\"height: 20px\"><td width=\"100%\" align=\"center\" class=\"borderbtmbonusballtwo\" colspan=\"2\"><div id=\"" + i + "blink\" >" + newTime + "</div></td><td width=\"44%\" align=\"center\"><p><div id=\"nxtDrwD" + i + "\" style=\"display:none\"><img src=\"" + fortImgPath + "freezed.gif\"></div></p></td></tr>";
		drawName = _bonusballtwoDrawIdObj[parseInt(nxtDrwArr[i].replace(" ", ""))].split(',')[1];
		if(drawName == 'null'){
			drawName = 'DRAW';
		}
		_bonusballtwodrawSelect = _bonusballtwodrawSelect + '<tr><td align="right"><input type="checkbox" title="'+newTime+'" name="drawIdChkBox" value="'+drawName+'" id="'+ _bonusballtwoDrawIdObj[parseInt(nxtDrwArr[i].replace(" ", ""))].split(',')[0] +'_did"/></td><td width="30%" nowarp="nowrap">'+drawName+'</td><td align="center">'+newTime+'</td></tr>'
	}
	nxtDrwHTML = nxtDrwHTML + "</table></div></td></tr></table></td></tr></table>";
	_bonusballtwodrawSelect = _bonusballtwodrawSelect + '<tr><td colspan="3" align="right"><input class="button" id="selNonConsecButton" onclick="drawSelectButton()" type="button" value="Select"/></td></tr></table>'
	return nxtDrwHTML;
}

function _bonusballtwoUpData(win, drwTime, drwStatus) {
	_bonusballtwoWin = win;
	_bonusballtwoDrawTime = drwTime;
	_bonusballtwoStatus = drwStatus;
}
function fillSelbonustwo(val) {
	if (!bonusballtwoIsAllQP) {
	countFill=0;
		bonusballtwocurSel = "row" + val;
		_id.o("radio" + val).checked = true;
		var newObjR=_id.o(bonusballtwocurSel);
		var newElms = newObjR.getElementsByTagName("input");
		for (var l = startPoint; l < newElms.length; l++) {
			if (newElms[l].value == "") {
				newElms[l].focus();
				break;
			}else{
				countFill++;
			}
		}
		bonusballtwoapplyCSS();
	}
}


var countFill=0;
function fillNumbonustwo(val) {   
	if (!bonusballtwoIsAllQP) {
		var objR = _id.o(bonusballtwocurSel);
		var clrChg = true;
		var elms = objR.getElementsByTagName("input");
		if (_id.o(val).className == "selected_bonusballtwo_numbersSelect") {
			for (var l = startPoint; l < elms.length; l++) {
				if (elms[l].value == val) {
					elms[l].value = "";
					countFill--;
					break;
				}
			}
		} else {
			for (var l = startPoint; l < elms.length; l++) {
				if (elms[l].value == "") {
					countFill++;
					elms[l].value = (val < 10 ? "0" + val : val);
					break;
				}
			}
			for (var l = startPoint; l < elms.length; l++) {
				if (elms[l].value == "") {
					elms[l].focus();
					break;
				}
			}
		}
		//alert(countFill);
	
		if(playType=="Direct5" && countFill==5){
			//alert(parseInt(bonusballtwocurSel.replace("row",""),10)+1);
			countFill=0;
			var rowNo=parseInt(bonusballtwocurSel.replace("row",""),10)+1;
			if(rowNo>4){
				rowNo=1;
			}			
			bonusballtwocurSel="row"+rowNo;
			
			//alert(countFill);
			fillSelbonustwo(rowNo);
		}
		else{
		bonusballtwoapplyCSS();
		}
	}
}
function _bonusballtwoCalAmt() {
	var noOfDrw = _id.o("noOfDraws").value;
	var noOfPanel = 0;
	var obj, elms;
	var isPanel = true;
	if(_id.n('conOrNonCon')[0]!= undefined && _id.n('conOrNonCon')[0].checked){
		noOfDrw = _id.n("noOfDraws")[0].value;
	}else if(_id.n('conOrNonCon')[1]!= undefined && _id.n('conOrNonCon')[1].checked){
		noOfDrw = _bonusballtwoDrawIdArr.length;
	}
	if (bonusballtwoIsAllQP) {
		noOfPanel = _id.o("tkts").value;
	}else{
if (playType == "Direct5") {			
	for (var i = 1; i < 5; i++) {
				isPanel = true;
				obj = _id.o("row" + i);
				elms = obj.getElementsByTagName("input");
				for (var l = 1; l < elms.length; l++) {
					if (elms[l].value == "" || elms[l].value.length < 2 || isNaN(elms[l].value)) {
						isPanel = false;
						elms[l].value = "";
					}
				}
				if (isPanel) {
					noOfPanel++;
				}
			}
	}else{
if (playType == "Perm5") {
			obj = _id.o("lottoRow");
			elms = obj.getElementsByTagName("input");
			for (var l = 0; l < elms.length; l++) {
				if (elms[l].value == "" || elms[l].value.length < 2 || isNaN(elms[l].value)) {
					elms[l].value = "";
				} else {
					noOfPanel++;
				}
			}
			_id.o("noPicked").value = noOfPanel;
			noOfPanel = calPermLinebonusballtwo();
		}
}
	
	}
	if (noOfPanel != 0 || _id.o("qpChkAll").checked == false) {
		_id.o("tkts").value = noOfPanel;
	} else {
		_id.o("tkts").value = "";
	}
	var unitPrice=document.getElementById('betAmount').value;
	betAmt=unitPrice/_bonusballtwoPrice;
	
	
	var totalPrice=noOfDrw * noOfPanel * _bonusballtwoPrice*betAmt;
	totalPrice=tktPriceRound(totalPrice);
	//alert(totalPrice);
	_id.o("totAmtLto").value = totalPrice;
	//alert(document.getElementById('betAmount').value);
}
function fillNoOfPanels(id) {
	var noOfpanels = _id.o(id).value;
	_id.i("lottoRowDiv", lottoRow(noOfpanels));
}
function keyPressbonusTwo(obj, val,e) {
	var keyCode = (window.event)? event.keyCode : e.which;
	//var keyCode = event.keyCode;
	//alert(keyCode);
	if ((keyCode >= 48 && keyCode < 58) || keyCode == 45 || keyCode == 27 || keyCode == 13 || keyCode == 40 || keyCode == 38 || keyCode == 46 || keyCode == 8 || (keyCode >= 96 && keyCode < 106 || keyCode == 9)) {
		if (keyCode == 46 || keyCode == 8 || keyCode == 9) {
			if (keyCode == 9) {
				//alert(val);
				//fillSelbonustwo(val);
				return false;
			}
			return true;
		}
		if (obj.value.length > 1) {
			if (obj.value == 0 || obj.value > 30 || isNaN(obj.value)) {
				obj.value = "";
				obj.focus();
			} else {
				var objR = _id.o(bonusballtwocurSel);
				var clrChg = true;
				var elms = objR.getElementsByTagName("input");
				for (var l = startPoint; l < elms.length; l++) {
					clrChg = true;
					if ((elms[l].value == obj.value) && (elms[l] != obj)) {
						obj.value = "";
						obj.focus();
						clrChg = false;
						break;
					}
				}
				if (clrChg) {
					var selNxtRow = false;
					for (var l = startPoint; l < elms.length; l++) {
						if (elms[l].value == "") {
							selNxtRow = false;
							elms[l].focus();
							break;
						} else {
							selNxtRow = true;
						}
					}//alert(selNxtRow);
					if (selNxtRow) {
						var nxtRow = parseInt(bonusballtwocurSel.substring(bonusballtwocurSel.length - 1), 10) + 1;
						if (nxtRow <= 4) {
							var nxtRowStr = bonusballtwocurSel.substring(0, bonusballtwocurSel.length - 1) + nxtRow;
						//alert(nxtRowStr);
							_id.o(nxtRowStr).getElementsByTagName("input")[1].focus();
							fillSelbonustwo(nxtRow);
						}
					
						
					}
				}
			}
			bonusballtwoapplyCSS();
		}
	} else {
		return false;
	}
	return true;
}
function blurFnbonustwo(obj) {
	if ((obj.value.length < 2) || isNaN(obj.value) || obj.value == 0) {
		obj.value = "";
	}
	bonusballtwoapplyCSS();
}
function bonusballtwoapplyCSS() {
	for (var i = 1; i <= 30; i++) {
		_id.o(i).className = "selected_bonusballtwo_numbers";
		_id.o(i).style.background = "url(" + projectName + "/LMSImages/images/bonusballtwo_shade_white.gif) center no-repeat";
		_id.o(i).style.color = "#155E33";
	}
	var clrChg = true;
	var obj = _id.o(bonusballtwocurSel);
	var elms = obj.getElementsByTagName("input");
	for (var l = startPoint; l < elms.length; l++) {
		for (var j = 0; j < elms.length - 1; j++) {
			if ((elms[l].value == elms[j].value) && (elms[l] != elms[j])) {
				elms[j].value = "";
			}
		}
	}
	var count = 0;
	var maxCount = 6;
	if (playType == "Perm5") {
		maxCount = 30;
	}
	for (var l = startPoint; l < elms.length; l++) {
		if (elms[l].value != "") {
			if (count < maxCount) {
				_id.o(parseInt(elms[l].value, 10)).className = "selected_bonusballtwo_numbersSelect";
				_id.o(parseInt(elms[l].value, 10)).style.background = "url(" + projectName + "/LMSImages/images/bonusballtwo_shade_over.gif) center no-repeat";
				_id.o(parseInt(elms[l].value, 10)).style.color = "#FFFFFF";
				count++;
			}
		}
	}
	var obj1 = _id.o("lottoRow");
	var txtelms = obj1.getElementsByTagName("input");
	if (bonusballtwoIsAllQP) {
		for (var i = 1; i <= 30; i++) {
			_id.o(i).className = "selected_bonusballtwo_numbersDisabled";
			_id.o(i).style.background = "url(" + projectName + "/LMSImages/images/bonusballtwo_shade_disable.gif) center no-repeat";
			_id.o(i).style.color = "#FFFFFF";
		}
if (playType == "Perm5") {
			for (var l = 0; l < txtelms.length; l++) {
				txtelms[l].disabled = true;
				txtelms[l].value="";
			}
		}else{
			for (var l = 0; l < txtelms.length; l++) {
				txtelms[l].disabled = true;
				txtelms[l].value="";
			}
			
			}
		
	} else {
		for (var l = 0; l < txtelms.length; l++) {
			txtelms[l].disabled = false;
			if (txtelms[l].type == "checkbox") {
				if (txtelms[l].checked == true) {
					for (var i = 1; i <= 6; i++) {
						txtelms[l - i].disabled = true;
					}
				}
			}
		}
	}
	_bonusballtwoCalAmt();
}
/*function bonusballtworesetFrm() {
	document.forms[0].reset();
	for (var i = 1; i <= 30; i++) {
		_id.o(i).className = "selected_bonusballtwo_numbers";
	}
	var elms = document.forms[0].getElementsByTagName("input");
	for (var l = 0; l < elms.length; l++) {
		elms[l].disabled = false;
	}
	bonusballtwocurSel = "row1";
}*/
var gameChildW = null;
function bonusballtwoGameInfo() {
	if (gameChildW) {
	} else {
		gameChildW = window.open("bonusballtwo/gameInfo.jsp", "NewWin", "width=650,height=550,toolbar=0,resizable=0,scrollbars=1,alwaysRaised=0,location=0,menubar=0");
		gameChildW.close();
	}
	gameChildW = window.open("bonusballtwo/gameInfo.jsp", "NewWin", "width=650,height=550,toolbar=0,resizable=0,scrollbars=1,alwaysRaised=0,location=0,menubar=0");
}


function buyTicketbonusballtwo(id) {
	
	var compData = "";
	var noPicked = 5;
if (playType == "Direct5") {
		if (!bonusballtwoIsAllQP) {
			for (var i = 1; i <= 4; i++) {
				var inputData = "";
				var obj = _id.o("row" + i);
				var elms = obj.getElementsByTagName("input");
				for (var l = 1; l < elms.length; l++) {
					if (elms[l].value != "") {
						inputData = inputData + elms[l].value + ",";
					}
				}
				if (inputData != "" && inputData.split(",").length < 1 + parseInt(5)) {
					alert("Please fill incomplete entries");
					return false;
				}
				if (inputData != "" && checkNumber(inputData.substring(0, inputData.length - 1), 1, 30)) {
					alert("Please fill Number only between 1 to 30");
					return false;
				}
				if (inputData != "" && checkDuplicate(inputData.substring(0, inputData.length - 1))) {
					alert("Please fill different Number");
					return false;
				}
				if (inputData != "") {
					inputData = arrSort(inputData.substring(0, inputData.length - 1)) + "Nxt";
				}
				compData = compData + inputData;
			}
			var noPanels = compData.split("Nxt").length-1;
			//alert(compData.split("Nxt").length-1);
			//if (noPanels % 4 != 0) {
			if (noPanels != 4) {
				alert("Please Select All Panels");
				_id.o("tkts").value = "";
				return false;
			}
		} else {
			var noPanels = _id.o("tkts").value;
			//alert(noPanels);
			if (noPanels != 4) {
				alert("Please Enter Value in Multiple Of 4");
				_id.o("tkts").value = "";
				_id.o("totAmtLto").value = "";
				return false;
			}
			if(noPanels >4){
				alert('You can select maximum 4 panels at a time');
				_id.o("tkts").value = "";
				_id.o("totAmtLto").value = "";
				return false;
			}
			
			for (var i = 1; i <= noPanels / 4; i++) {
				inputData = "QPNxtQPNxtQPNxtQPNxt";
				compData = compData + inputData;
			}
			
		}
		}else{

	//alert(zimlottotwoIsAllQP);
	if (!bonusballtwoIsAllQP) {
		var inputData = "";
		var obj = _id.o("lottoRow");
		var elms = obj.getElementsByTagName("input");
		for (var l = 0; l < elms.length; l++) {
			if (elms[l].value != "") {
				inputData = inputData + elms[l].value + ",";
			}
		}
		//alert(inputData+'   '+inputData.split(",").length);
		if (inputData != "" && inputData.split(",").length < (2 + parseInt(5))) {
			alert("Please pick minimum 6 numbers");
			return false;
		}
		if (inputData != "" && checkNumber(inputData.substring(0, inputData.length - 1), 1, 42)) {
			alert("Please fill Number only between 1 to 30");
			return false;
		}
		if (inputData != "" && checkDuplicate(inputData.substring(0, inputData.length - 1))) {
			alert("Please fill different Number");
			return false;
		}
		if (inputData != "") {
			inputData = arrSort(inputData.substring(0, inputData.length - 1)) + "Nxt";
		}
		compData = compData + inputData;
		noPicked = compData.split(",").length;
		//alert(noPicked);
	}else{
		compData="QPNxt";
		noPicked=_id.o('noPicked').value;
		if (noPicked < (1 + parseInt(5))) {
			alert("Please pick minimum 6 numbers");
			return false;
		}
		if(noPicked >(1 + parseInt(9))){
			alert('You can pick maximum 10 numbers');
			_id.o('noPicked').value="";
			_id.o('tkts').value="";
			_id.o('totAmtLto').value="";
			return false;
		}
	}
	}
//alert(compData);
		compData = compData.substring(0, compData.length - 3);
		_id.o("pickedNumbers").value = compData;
		if (compData.length < 1) {
			alert("Please fill incomplete entries");
			return false;
		}
	
	
	var amount = _id.o("totAmtLto").value;
	//alert(amount);
	_id.o(id).disabled = true;
		if(_id.n('conOrNonCon')[1]!= undefined && _id.n('conOrNonCon')[1].checked){
			var html = '';
			for(i=0; i<_bonusballtwoDrawIdArr.length;i++){
				html += '&drawIdArr='+_bonusballtwoDrawIdArr[i];
			}
			html+='&isAdvancedPlay=1';
//alert("noOfDraws"+ _id.n("noOfDraws")[0].value);
				gameBuyAjaxReq("bonusballTwoBuy.action?pickedNumbers=" + compData + "&playType=" + playType + "&noOfDraws=" + _bonusballtwoDrawIdArr.length +"&betAmountMultiple="+betAmt + "&totalPurchaseAmt=" + amount + "&noPicked=" + noPicked + html);		
				}
		else{
				gameBuyAjaxReq("bonusballTwoBuy.action?pickedNumbers=" + compData + "&playType=" + playType + "&noOfDraws=" + _id.n("noOfDraws")[0].value+"&betAmountMultiple="+betAmt + "&totalPurchaseAmt=" + amount + "&noPicked=" + noPicked +"&isAdvancedPlay=" + 0 );	
}

	

	bonusballtwoclearAllBoxes();
	return true;
}



function bonusballtwoMultiPlay() {
	playTypeObj = _id.n("playType");
	for (var i = 0; i < playTypeObj.length; i += 1) {
		if (playTypeObj[i].checked == true) {
if (playTypeObj[i].value == "Direct5") {
				_id.o("playDiv").innerHTML = bonusballtwoRow("4");
				bonusballtwocurSel = "row1";
				startPoint = 1;
			 
	} else {
if (playTypeObj[i].value == "Perm5") {
					_id.o("playDiv").innerHTML = bonusballtwoPermTlb();
					bonusballtwocurSel = "lottoRow";
					startPoint = 0;
				}
			}
			playType = playTypeObj[i].value;
		}
	}
	bonusballtwoclearAllBoxes();
}

function bonusballtwoPermTlb() {
	var htm = "<table width=\"100%\" id=\"lottoRow\" border=\"0\" cellspacing=\"2\" cellpadding=\"0\"><tr>";
	for (var i = 1; i < 3; i++) {
		htm = htm + "<tr id=\"rowPerm\">";
		for (var j = 0; j < 5; j++) {
			htm = htm + "<td align=\"center\" valign=\"middle\" ><input name='numbers' type=\"text\" class=\"selected_bonusballtwo\" maxlength=\"2\" onkeydown=\"return keyPressbonusTwo(this," + i + ",event)\" onkeyup=\"return keyPressbonusTwo(this," + i + ",event)\" onblur=\"blurFnbonustwo(this)\" onmousedown=\"return disClick(this)\"></td>";
		}
		htm = htm + " </tr>";
	}
	htm = htm + "</table><br/><table border=\"0\" style=\"height:18px;background-color:#4D8F00;\"><tr><td class=\"bonusballtwotxtStyle\" align=\"left\">No. Picked</td><td><input class=\"bonusballtwotextbox\" type='text' id='noPicked' value='0' onkeyup='return keyPressQPBonusTwo(this,event)' onkeydown='return keyPressQPBonusTwo(this,event)' onblur=\"bonusballtwoCalPermQP()\" onmousedown=\"return disClick(this)\"/></td></tr>";
	return htm + "</table>";
}
var lines = 0;
function calPermLinebonusballtwo() {
	var num = _id.o("noPicked").value;
	if (num >= 5) {
		lines = (num * (num - 1) * (num - 2) * (num - 3) * (num - 4)) / 120;
	} else {
		if (num != 0) {
			lines = 1;
		} else {
			lines = 0;
		}
	}
	//_id.o("noLines").value = lines;
	return lines;
}

function bonusballtwoCalPermQP(){
	_id.o('tkts').value=calPermLinebonusballtwo();
	bonusballtwoapplyCSS();
}
function keyPressQPBonusTwo(obj,e) {
	var keyCode = (window.event)? event.keyCode : e.which;
	if ((keyCode >= 48 && keyCode < 58) || keyCode == 45 || keyCode == 27 || keyCode == 13 || keyCode == 40 || keyCode == 38 || keyCode == 46 || keyCode == 8 || (keyCode >= 96 && keyCode < 106 || keyCode == 9)) {
		if (keyCode == 46 || keyCode == 8 || keyCode == 9) {
				bonusballtwoapplyCSS();
			
			return true;
		}
		if (obj.value.length >= 1) {
			var maxCount=10;
			if(playType=='Direct5'){
			 maxCount=50;
			}
			if (obj.value == 0 || obj.value > maxCount || isNaN(obj.value)) {
				obj.value = "";
				obj.focus();
			}
			if(playType=='Direct5'){
					bonusballtwoapplyCSS();
			}else{
				bonusballtwoCalPermQP();
			}
			
			
		}
	} else {
		return false;
	}
	return true;
}
function _bonusballTwoBetAmountMultiple(ele, func) {
	var num = _id.v(ele);
	
	if (func == "plus") {
		_id.o(ele).value = Math.round(((num < 5) ? (parseFloat(num == _bonusballtwoPrice ? _bonusballtwoPrice : num, 10) + parseFloat(_bonusballtwoPrice)) : num)*100)/100;
	} else {
		_id.o(ele).value = Math.round(((num > parseFloat(_bonusballtwoPrice)) ? (parseFloat(num == _bonusballtwoPrice ? _bonusballtwoPrice : num, 10) - parseFloat(_bonusballtwoPrice)) : num)*100)/100;
	}
	_bonusballtwoCalAmt(true);
}



var line = "<tr><td bgcolor=\"#4d8f00\"><table border=\"0\" bordercolor=\"blue\" width=\"97%\"><tr><td align=\"left\" width=\"25%\"><input type=\"hidden\" value=\"0\" id=\"counterId\"><input type=\"hidden\" id=\"dateFuture\"><input type=\"hidden\" id=\"freezeTimeId\" value=\"" + _bonusballtwofreezeTime + "\"><span class=\"style1\">Next Draw :</span><div id=\"latestDrawTime\" style=\"display:none\"></div></td><td width=\"20%\" align=\"left\"><div id=\"nxtDrawTime\" class=\"bonusballtwostyle2\"></div></td><td align=\"right\" width=\"25%\"><div id=\"timeLeft\"><span class=\"style1\"> Time Left :</div></span></td> <td align=\"left\" width=\"15%\"><div id=\"curDrwTime\" class=\"bonusballtwostyle2\"></div> </td><!--<td rowspan=\"2\" width=\"10%\"><img src=\"" + fortImgPath + "que.gif\" style=\"cursor:hand;\" onClick=\"gameInfo()\" alt=\"Game Info\" /> </td>--></tr> <tr bgcolor=\"#4d8f00\"><td align=\"left\"> <span class=\"style1\">No. of Draws :</span> </td> <td > <table><tr><td> <input type=\"radio\"/ name=\"conOrNonCon\" value=\"consec\" checked='true'  onclick=\"drawSelectButton(this)\"'/></td>  <td>  <select\tname=\"noOfDraws\" id=\"noOfDraws\"\tclass=\"fortune_option\" align=\"left\"\twidth=\"20%\"\tonchange=\"_bonusballtwoCalAmt()\">" + getActiveDrawOptionList("bonusballtwo") + "</select> </td> </tr>   </table>        </td>  <td>     <table> <tr><td><input type=\"radio\"/ name=\"conOrNonCon\" value=\"nonconsec\" onclick=\"specificDrawPopUp(_bonusballtwodrawSelect)\" />  </td><td class=\"style1\">Advance Draw</td></tr></table>     </td>      <td colspan=\"1\" align=\"left\" valign=\"top\"> &nbsp; </td><td align=\"left\"> <span class=\"bonusballtwonum\">" + currSymbol + " " + _bonusballtwoPrice + "/-</span></td> </tr><tr><td><table><tr><td align=\"right\" height=\"0px\" width=\"0px\"  id=\"drawSelectorDiv\" style=\"visibility:hidden;position:absolute;background-color:#FFFFFF\"></td></tr></table></td> </tr>         </table></td></tr>";
var _bonusballtwoNxtDrwTbl = _bonusballtwoFillNxtDrw();
var _bonusballtwoHtmlTbl = "<table width=\"100%\" height=\"100%\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\" bordercolor=\"red\" bgcolor=\"#a0e352\">" + line + "<tr><td align=\"center\" valign=\"top\"><table width=\"100%\" cellspacing=\"0\" cellpadding=\"0\" border=\"0\"><tr><td width=\"45%\" valign=\"top\"><div style=\"padding-top:10px;\"><table border=\"0\" bordercolor=\"red\" width=\"97%\" align=\"center\"cellpadding=\"0\"cellspacing=\"0\"><tr><td class=\"style10\" align=\"left\"><div align=\"center\" valign=\"middle\" style=\"height:18px;background-color:#4d8f00;width:180px;float:left;padding-top:2px\">&nbsp;Select Number Panel&nbsp;</div></td></tr><tr><td valign=\"middle\" style=\"border:1px #4d8f00 solid;background-color:#7dc924\">" + bonusballtwoNmSel(30) + "</td></tr></table></div></td><td valign=\"top\" wdith=\"35%\"><div style=\"border:0px solid black; padding-top:10px;\"><table border=\"0\" bordercolor=\"green\" width=\"96%\" cellpadding=\"0\" cellspacing=\"0\" align=\"center\" style=\"margin-right:10px\"><tr><td><input type='radio' name='playType' value='Direct5' checked='true' onclick='bonusballtwoMultiPlay()'>Direct-5<input type='radio' name='playType' value='Perm5' onclick='bonusballtwoMultiPlay()'>Perm-5</td> <td align=\"right\" class=\"style10\" style=\"cursor:hand;\" onclick=\"bonusballtwoclearAllBoxes()\" ><div align=\"center\" valign=\"middle\" style=\"height:18px;background-color:#4d8f00;width:100px;float:right;padding-top:2px\">&nbsp;&nbsp;&nbsp;Clear All&nbsp;&nbsp;&nbsp;</div></td></tr><tr><td valign=\"middle\" style=\"border:1px #4d8f00 solid;background-color:#7dc924\" colspan=2><table width=\"100%\" border=\"0\" align=\"center\" cellspacing=\"0\" cellpadding=\"0\"><tr><td><div id='playDiv'>" + bonusballtwoRow("4") + "</div></td></tr><tr><td bgcolor=\"#4d8f00\"><table width=\"95%\" border=\"0\" style=\"margin:6px 0px 5px 0px;\">   <tr ><td>&nbsp;&nbsp</td><td class=\"bonusballtwotxtStyle\" align=\"middle\">Unit Price</td><td><img src=\"" + fortImgPath + "minus.gif\"  style=\"cursor:pointer\" alt=\"minus\" onclick=\"_bonusballTwoBetAmountMultiple('betAmount','minus')\"/><input type=\"text\" class=\"bonusballtwotextbox\" name=\"betAmount\" id=\"betAmount\" value='"+_bonusballtwoPrice+"' readOnly=\"true\" style=\"width:40px\" /><img src=\"" + fortImgPath + "plus.gif\" alt=\"Plus\" onclick=\"_bonusballTwoBetAmountMultiple('betAmount','plus')\" style=\"cursor:pointer\"/></td></tr>                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                 <tr><td align=\"center\" class=\"style5\" rowspan=\"2\"><div style=\"border:1px #FFFFFF solid; width: 50px; height: 40px; float: left;\">QP<br /><input type=\"checkbox\" id=\"qpChkAll\" onclick=\"bonusballtwoChkQP(this.id)\" style=\"border:1px #4d8f00 solid\" /></div></td><td class=\"bonusballtwotxtStyle\" align=\"left\">Total Qty:</td><td><input type=\"hidden\" value=\"\" id=\"pickedNumbers\" name=\"pickedNumbers\" /><input type=\"text\" class=\"bonusballtwotextbox\" id=\"tkts\" readonly=\"true\" value=\"0\" onkeyup='return keyPressQPBonusTwo(this,event)' onkeydown='return keyPressQPBonusTwo(this,event)' onmousedown=\"return disClick(this)\"  /></td><td rowspan=\"2\"><a href=\"#\"><img src=\"" + fortImgPath + "bonusballtwo_buy.gif\" id='buy' onclick=\"return buyTicketbonusballtwo(this.id)\" alt=\"Buy\" style=\"margin-right: 4px;\" /></a></td></tr><tr><td class=\"bonusballtwotxtStyle\" align=\"left\">Total Amount:</td><td><input type=\"text\" id=\"totAmtLto\" name=\"totalPurchaseAmt\" value=\"0\" readonly=\"true\" class=\"bonusballtwotextbox\" /></td></tr> </table></td></tr></table></td></tr></table></div></td></tr></table></tr><tr>      <td style=\"height:55px\";><div id=\"selectedDrawList\">No Draws Selected. Please Select at least one draw!</div></td></tr></table>";

                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                            																																																																																																																																																																																																																																				                                                                                                                                                                                                                                                                                                                                                                                                                                                                                        
var _bonusballtwoHTML = "<div id=\"strip\">" + drwMenu + "</div><form style=\"background-color:#FFFFFF;border: 1px solid #393939;\"><table  border=\"0\" width=\"100%\" height=\"100%\" cellspacing=\"0\" cellpadding=\"0\" align=\"center\" bgcolor=\"#FFFFFF\" bordercolor=\"red\"><tr><td width=\"10%\"  valign=\"top\" align=\"center\" onmousedown=\"return disClick(this)\"><input type=\"hidden\" id=\"drawType\"><div id=\"winningDispDiv\" style=\"border:1px #7dc924 solid;background-color:#FFFFFF;margin-left:5px;margin-top:3px\"></div><div id=\"nxtDrawDiv\" style=\"border:1px #7dc924 solid;background-color:#FFFFFF;margin-left:5px;margin-top:5px\"></div></td><td width=\"90%\" valign=\"top\"><div id=\"bonusballtwomiddle\" style=\"border:1px #7dc924 solid;background-color:#4d8f00;width:98%;margin-left:5px;margin-top:3px;margin-bottom:1px\">" + _bonusballtwoHtmlTbl + "</div></td></tr></table></td></tr></table></form>";
parent.frames[0].Version['$Source: /rep/LMS_Mgmt/WebRoot/com/skilrock/lms/web/drawGames/playMgmt/games/bonusballtwo/Attic/bonusballtwo.js,v $'] = '$Id: bonusballtwo.js,v 1.1.2.6.4.1 2013/09/19 05:12:35 yogesh Exp $';