
var _bonusballlottoWinArr = new Array();
var _bonusballlottoDrawIdArr = new Array();

var curDrwTimelotto = "";
var bonusballlottocurSel = "row1";
var bonusballlottoIsAllQP = false;
var _bonusballlottoPrice = parseFloat(gameDispName["bonusballlotto"].unitPrice.split("=")[1], 10);
var playType;
var startPoint;
var betAmt;
var _bonusballlottodrawSelect;

function setDefaultbonusballlotto() {
	curDrwTimefortune = "";
	bonusballlottocurSel = "row1";
	bonusballlottoIsAllQP = false;
	//playType = "bonus5";
	startPoint = 1;
}
function _bonusballlottoWinAppend() {
	if (!_bonusballlottoWin.match("Result Awaited")) {
		_bonusballlottoWin = "Result Awaited=\"\" Nxt" + _bonusballlottoWin;
	}
}
function _bonusballlottoLatestDrawTime() {
	return _bonusballlottoStatus.split("=")[1];
}
function _bonusballlottoFillWinTbl() {
	var _bonusballlottoWinHt = "<table width=\"100%\" border=\"0\" bordercolor=\"red\" cellspacing=\"0\" cellpadding=\"0\" align=\"center\" bgcolor=\"#a0e352\"><tr><td colspan=\"2\" align=\"left\"><div valign=\"middle\" style=\"height:22px;background-color:#4d8f00;padding-top:3px;font-size:16px;font-family:Arial;font-weight:bold;color:#FFFFFF\">&nbsp;Win Result&nbsp;</div></td></tr>      <tr><td valign=\"top\" align=\"center\" height=\"100\">";
	var winArr = _bonusballlottoWin.split("Nxt");
	var _bonusballlottoWinHTML = "<table width=\"100%\" border=\"0\" bordercolor=\"red\" style=\"margin-top:3px\"><tr><td><div style=\"" + scrollColor("#7dc924", "#4d8f00", "184") + "\">";
	for (var i = 0; i <= winArr.length - 2; i++) {
		if (!winArr[i].match("Result")) {
			var time = new Date(parseInt(winArr[i].split("=")[0].replace(" ", "")));
			var newTime = week[time.getDay()] + " " + time.getDate() + " " + month[time.getMonth()] + "<br/>" + (time.getHours() < 10 ? "0" + time.getHours() : time.getHours()) + ":" + (time.getMinutes() < 10 ? "0" + time.getMinutes() : time.getMinutes()) + ":" + (time.getSeconds() < 10 ? "0" + time.getSeconds() : time.getSeconds());
			//alert(newTime);
			var htm = "<table width=\"90%\" border=\"0\" bordercolor=\"green\" id=\"result" + i + "\" cellspacing=\"2\" cellpadding=\"10\" class=\"bonusballlotto_winningResult_table\" ><tr><td class=\"bonusballlottowinningResultTime\" colspan=\"8\" height=\"53\">" + newTime + "</td></tr><tr>";
			var num = (winArr[i].split("=")[1]).split(",");
			for (var j = 0; j < num.length; j++) {
				var temp = (num[j] < 10 ? "0" + num[j] : num[j]);
				htm = htm + "<td width=\"10%\" class=\"selected_win_bonusballlotto_numbers\" align=\"center\">" + temp + "</td>";
			}
			htm = htm + "</tr></table>";
			_bonusballlottoWinHTML = _bonusballlottoWinHTML + htm;
		} else {
			var htm = "<table width=\"90%\" bordercolor=\"blue\" border=\"0\" cellspacing=\"2\" cellpadding=\"10\" id=\"result" + i + "\" class=\"bonusballlotto_winningResult_table\" ><tr><td class=\"bonusballlottowinningResultTime\" colspan=\"8\" height=\"53\" >Result Awaited</td></tr><tr>";
			htm = htm + "<td class=\"selected_win_bonusballlotto_numbers\"  colspan=\"8\"><div style=\"text-align:center\" id=\"winAwaitedDiv\" style=\"color:#00466E\"></div></td>";
			htm = htm + "</tr></table>";
			_bonusballlottoWinHTML = _bonusballlottoWinHTML + htm;
		}
	}
	_bonusballlottoWinHTML = _bonusballlottoWinHTML + "</div></td></tr></table>";
	return _bonusballlottoWinHt + _bonusballlottoWinHTML + "</td></tr></table>";
}

function _bonusballlottoWinAjx() {
	var url = "fetchWinbonusballlotto.action?frmDate=" + _bonusballlottoWinArr[_bonusballlottoWinArr.length - 1];
	_bonusballlottoWin = _bonusballlottoWin + (winAjaxReq(url)).data;
	_bonusballlottoFillWinTbl();
}
function bonusballlottoNmSel(lstNo) {
	var htm = "<div id=\"\"><table width=\"100%\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\" bgcolor=\"#7dc924\" style=\"margin-top:3px;margin-bottom:3px\"> <tr>";
	for (var i = 1; i <= lstNo; i++) {
		if (i % 5 == 0) {
			htm = htm + "<td width=\"17%\" align=\"right\" valign=\"middle\" style=\"cursor:pointer;background:url(" + projectName + "/LMSImages/images/bonusballlotto_shade_white.gif) center no-repeat\" class=\"selected_bonusballlotto_numbers\" onmouseout=\"bonusballlottoonmouseouteffect(this.id)\" onmouseover=\"bonusballlottoonmouseovereffect(this.id)\"  onclick=\"fillNumbonuslotto(" + i + ")\"  id=\"" + i + "\" >" + (i < 10 ? "0" + i : i) + "</td></tr>";
		} else {
			htm = htm + "<td width=\"17%\" align=\"right\" valign=\"middle\"  class=\"selected_bonusballlotto_numbers\"  style=\"cursor:pointer;background:url(" + projectName + "/LMSImages/images/bonusballlotto_shade_white.gif) center no-repeat\" onmouseout=\"bonusballlottoonmouseouteffect(this.id)\" onmouseover=\"bonusballlottoonmouseovereffect(this.id)\" onclick=\"fillNumbonuslotto(" + i + ")\"  id=\"" + i + "\"  >" + (i < 10 ? "0" + i : i) + "</td>";
		}
	}
	htm = htm + "</tr></table></div>";
	return htm;
}
function bonusballlottoonmouseovereffect(id) {
	var count = 0;
	var isFilled = false;
	for (var i = 1; i <= 39; i++) {
		if (_id.o(i).className == "selected_bonusballlotto_numbersSelect") {
			count++;
		}
		if (count > 5) {
			isFilled = true;
			break;
		}
	}
	if (!isFilled && !bonusballlottoIsAllQP) {
		if (_id.o(id).className != "selected_bonusballlotto_numbersSelect") {
			_id.o(id).style.background = "url(" + projectName + "/LMSImages/images/bonusballlotto_shade_over.gif) center no-repeat";
			_id.o(id).style.color = "#FFFFFF";
			_id.o(id).className = "selected_bonusballlotto_numbersSelectOMO";
		}
	}
}
function bonusballlottoonmouseouteffect(id) {
	if (!bonusballlottoIsAllQP) {
		if (_id.o(id).className == "selected_bonusballlotto_numbersSelectOMO") {
			_id.o(id).style.background = "url(" + projectName + "/LMSImages/images/bonusballlotto_shade_white.gif) center no-repeat";
			_id.o(id).style.color = "#155E33";
			_id.o(id).className = "selected_bonusballlotto_numbers";
		} else {
			if (_id.o(id).className == "selected_bonusballlotto_numbersSelect") {
				_id.o(id).style.background = "url(" + projectName + "/LMSImages/images/bonusballlotto_shade_over.gif) center no-repeat";
				_id.o(id).style.color = "#FFFFFF";
			} else {
				if (_id.o(id).className == "selected_bonusballlotto_numbers") {
					_id.o(id).style.background = "url(" + projectName + "/LMSImages/images/bonusballlotto_shade_white.gif) center no-repeat";
					_id.o(id).style.color = "#155E33";
				}
			}
		}
	}
}



function bonusballlottoChkQP(id) {
	if (_id.o(id).checked == true) {
		bonusballlottoIsAllQP = true;
		//_id.o("tkts").readOnly = false;
		bonusballlottoapplyCSS();
		
		_id.o("tkts").focus();
		_id.o("tkts").value = "";
		_id.o("tkts").readOnly = false;
		_bonusballlottoCalAmt();
		
	}
	else {
		bonusballlottoclearAllBoxes();
		}
}


function bonusballlottoRow(noOfpanels) {
	var htm = "<table width=\"100%\" id=\"lottoRow\" border=\"0\" cellspacing=\"2\" cellpadding=\"0\"><tr> <td width=\"8%\" align=\"center\" valign=\"middle\">&nbsp;</td><td width=\"10%\" align=\"center\" valign=\"middle\" class=\"style5\">&nbsp;</td>";
	for (var indx = 1; indx < 6; indx++) {
		htm = htm + "<td width=\"12%\" align=\"center\" valign=\"middle\" style=\"color:#155E33;text-align: center;font-weight:bold;font-size:16px\">" + indx + "</td>";
	}
	htm = htm + "</tr> <tr>";
	for (var i = 1; i < parseInt(noOfpanels) + 1; i++) {
		htm = htm + "<tr onclick=\"fillSelbonuslotto(" + i + ")\" id=\"row" + i + "\"><td align=\"center\" style=\"color:#155E33;text-align: center;font-weight:bold;font-size:16px\">" + i + "</td><td><input type=\"radio\" name=\"lottoRadio\" id=\"radio" + i + "\" style=\"border:1px #7dc924 solid\" onclick=\"fillSelbonuslotto(" + i + ")\" " + (i == 1 ? "checked" : "") + "/></td>";
		for (var j = 0; j < 5; j++) {
			htm = htm + "<td align=\"center\" valign=\"middle\" ><input type=\"text\" class=\"selected_bonusballlotto\" maxlength=\"2\" onkeydown=\"return keyPressbonuslotto(this," + i + ",event)\" onkeyup=\"return keyPressbonuslotto(this," + i + ",event)\" onblur=\"blurFnbonuslotto(this)\" onmousedown=\"return disClick(this)\"></td>";
		}
		htm = htm + "</tr>";
	}
	//if(qp == 'yes'){
	//	_id.o('playDiv').innerHTML = htm+"</table>";
	//}
	return htm + "</table>";
}
function bonusballlottoclearBoxes(val) {
	_id.resetInpTypes("row" + val, "selected_bonusballlotto", false);
	_bonusballlottoCalAmt();
}
function bonusballlottoclearAllBoxes() {
	_id.resetInpTypes("lottoRow", "selected_bonusballlotto", false);
	_id.o("noOfDraws").value = 1;
	_id.o("tkts").readOnly = true;
	//bonusballlottocurSel = "row1";
	bonusballlottoIsAllQP = false;
	bonusballlottoapplyCSS();
	
	_id.o("qpChkAll").checked = false;
	_bonusballlottoCalAmt();
	document.getElementsByName('conOrNonCon')[0].click();

}
function _bonusballlottoFillNxtDrw() {
	var nxtDrwHTML = "<table width=\"100%\" cellspacing=\"0\"cellpadding=\"0\"  border=\"0\" bordercolor=\"red\"><tr><td colspan=\"2\" align=\"left\" valign=\"top\"><div valign=\"middle\" style=\"height:22px;width:180px;background-color:#4d8f00;padding-top:3px;font-size:16px;font-family:Arial;font-weight:bold;color:#FFFFFF\">&nbsp;Next Draw&nbsp;</div></td></tr><tr><td align=\"center\"valign=\"top\"><table width=\"100%\" bordercolor=\"red\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\" bgcolor=\"#7dc924\"><tr><td width=\"100%\" align=\"left\" valign=\"top\" ><div style=\"" + scrollColor("#7dc924", "#4d8f00", "175") + "\"><table width=\"90%\" align=\"left\" bordercolor=\"green\"  border=\"0\" cellspacing=\"0\" cellpadding=\"0\" class=\"borderbtmbonusballlotto\">";
	_bonusballlottodrawSelect = '<table width="auto" cellspacing="0"cellpadding="0"  border="1"><tr><td colspan="3" align="center"><b>Select Draws</td></tr>';
	var nxtDrwArr = _bonusballlottoDrawTime.split(",");
	for (var i = 0, l = nxtDrwArr.length; i < l; i++) {
		var time = new Date(parseInt(nxtDrwArr[i].replace(" ", "")));
		var newTime = week[time.getDay()] + " " + time.getDate() + " " + month[time.getMonth()] + " " + (time.getHours() < 10 ? "0" + time.getHours() : time.getHours()) + ":" + (time.getMinutes() < 10 ? "0" + time.getMinutes() : time.getMinutes());
		var hiddenTime = "<input type='text' id='timer" + i + "'>";
		var rowcolor;
		var data = "";
		if (i == 0) {
			data = time.getFullYear() + "-" + time.getMonth() + "-" + time.getDate() + "-" + (time.getHours() < 10 ? "0" + time.getHours() : time.getHours()) + "-" + (time.getMinutes() < 10 ? "0" + time.getMinutes() : time.getMinutes()) + "-" + (time.getSeconds() < 10 ? "0" + time.getSeconds() : time.getSeconds());
			var dateFuture1 = new Date(time.getFullYear(), time.getMonth(), time.getDate(), (time.getHours() < 10 ? "0" + time.getHours() : time.getHours()), (time.getMinutes() < 10 ? "0" + time.getMinutes() : time.getMinutes()), (time.getSeconds() < 10 ? "0" + time.getSeconds() : time.getSeconds()));
			_bonusballlottoNxtDrawTime = ("" + time).substring(0, ("" + time).lastIndexOf(":") + 3);
		}
		if (i % 2 == 0) {
			rowcolor = "#A0E352";
		} else {
			rowcolor = "#7dc924";
		}
		nxtDrwHTML = nxtDrwHTML + "<tr><td width=\"100%\"><input type=\"hidden\" value=\"" + data + "\" id=\"" + i + "timer\"></td></tr><tr bgcolor=\"" + rowcolor + "\" style=\"height: 20px\"><td width=\"100%\" align=\"center\" class=\"borderbtmbonusballlotto\" colspan=\"2\"><div id=\"" + i + "blink\" >" + newTime + "</div></td><td width=\"44%\" align=\"center\"><p><div id=\"nxtDrwD" + i + "\" style=\"display:none\"><img src=\"" + fortImgPath + "freezed.gif\"></div></p></td></tr>";
		drawName = _bonusballlottoDrawIdObj[parseInt(nxtDrwArr[i].replace(" ", ""))].split(',')[1];
		if(drawName == 'null'){
			drawName = 'DRAW';
		}
		_bonusballlottodrawSelect = _bonusballlottodrawSelect + '<tr><td align="right"><input type="checkbox" title="'+newTime+'" name="drawIdChkBox" value="'+drawName+'" id="'+ _bonusballlottoDrawIdObj[parseInt(nxtDrwArr[i].replace(" ", ""))].split(',')[0] +'_did"/></td><td width="30%" nowarp="nowrap">'+drawName+'</td><td align="center">'+newTime+'</td></tr>'
	}
	nxtDrwHTML = nxtDrwHTML + "</table></div></td></tr></table></td></tr></table>";
	_bonusballlottodrawSelect = _bonusballlottodrawSelect + '<tr><td colspan="3" align="right"><input class="button" id="selNonConsecButton" onclick="drawSelectButton()" type="button" value="Select"/></td></tr></table>'
	return nxtDrwHTML;
}

function _bonusballlottoUpData(win, drwTime, drwStatus) {
	_bonusballlottoWin = win;
	_bonusballlottoDrawTime = drwTime;
	_bonusballlottoStatus = drwStatus;
}
function fillSelbonuslotto(val) {
	if (!bonusballlottoIsAllQP) {
	countFill=0;
		bonusballlottocurSel = "row" + val;
		_id.o("radio" + val).checked = true;
		var newObjR=_id.o(bonusballlottocurSel);
		var newElms = newObjR.getElementsByTagName("input");
		for (var l = startPoint; l < newElms.length; l++) {
			if (newElms[l].value == "") {
				newElms[l].focus();
				break;
			}else{
				countFill++;
			}
		}
		bonusballlottoapplyCSS();
	}
}
var countFill=0;
function fillNumbonuslotto(val) {   
	if (!bonusballlottoIsAllQP) {
		var objR = _id.o(bonusballlottocurSel);
		var clrChg = true;
		var elms = objR.getElementsByTagName("input");
		if (_id.o(val).className == "selected_bonusballlotto_numbersSelect") {
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
	
		if(countFill==5){
			//alert(parseInt(bonusballlottocurSel.replace("row",""),10)+1);
			countFill=0;
			var rowNo=parseInt(bonusballlottocurSel.replace("row",""),10)+1;
			if(rowNo>5){
				rowNo=1;
			}			
			bonusballlottocurSel="row"+rowNo;
			
			//alert(countFill);
			fillSelbonuslotto(rowNo);
		}
		else{
		bonusballlottoapplyCSS();
		}
	}
}
function _bonusballlottoCalAmt() {
	var noOfDrw = _id.o("noOfDraws").value;
	var noOfPanel = 0;
	var obj, elms;
	var isPanel = true;
	if(_id.n('conOrNonCon')[0]!= undefined && _id.n('conOrNonCon')[0].checked){
		noOfDrw = _id.n("noOfDraws")[0].value;
	}else if(_id.n('conOrNonCon')[1]!= undefined && _id.n('conOrNonCon')[1].checked){
		noOfDrw = _bonusballlottoDrawIdArr.length;
		//alert(noofDrw);
	}
	if (bonusballlottoIsAllQP) {
		noOfPanel = _id.o("tkts").value;
	}else{
	
	
			for (var i = 1; i < 6; i++) {
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
		
	
	}
	if (noOfPanel != 0 || _id.o("qpChkAll").checked == false) {
		_id.o("tkts").value = noOfPanel;
	} else {
		_id.o("tkts").value = "";
	}
	var unitPrice=_bonusballlottoPrice;
		
	var totalPrice=noOfDrw * noOfPanel * _bonusballlottoPrice;
	totalPrice=tktPriceRound(totalPrice);
	//alert(totalPrice);
	_id.o("totAmtLto").value = totalPrice;
	//alert(document.getElementById('betAmount').value);
}
function fillNoOfPanels(id) {
	var noOfpanels = _id.o(id).value;
	_id.i("lottoRowDiv", lottoRow(noOfpanels));
}
function keyPressbonuslotto(obj, val,e) {
	var keyCode = (window.event)? event.keyCode : e.which;
	//var keyCode = event.keyCode;
	//alert(keyCode);
	if ((keyCode >= 48 && keyCode < 58) || keyCode == 45 || keyCode == 27 || keyCode == 13 || keyCode == 40 || keyCode == 38 || keyCode == 46 || keyCode == 8 || (keyCode >= 96 && keyCode < 106 || keyCode == 9)) {
		if (keyCode == 46 || keyCode == 8 || keyCode == 9) {
			if (keyCode == 9) {
				//alert(val);
				//fillSelbonuslotto(val);
				return false;
			}
			return true;
		}
		if (obj.value.length > 1) {
			if (obj.value == 0 || obj.value > 39 || isNaN(obj.value)) {
				obj.value = "";
				obj.focus();
			} else {
				var objR = _id.o(bonusballlottocurSel);
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
						var nxtRow = parseInt(bonusballlottocurSel.substring(bonusballlottocurSel.length - 1), 10) + 1;
						if (nxtRow <= 5) {
							var nxtRowStr = bonusballlottocurSel.substring(0, bonusballlottocurSel.length - 1) + nxtRow;
						//alert(nxtRowStr);
							_id.o(nxtRowStr).getElementsByTagName("input")[1].focus();
							fillSelbonuslotto(nxtRow);
						}
					}
				}
			}
			bonusballlottoapplyCSS();
		}
	} else {
		return false;
	}
	return true;
}
function blurFnbonuslotto(obj) {
	if ((obj.value.length < 2) || isNaN(obj.value) || obj.value == 0) {
		obj.value = "";
	}
	bonusballlottoapplyCSS();
}
function bonusballlottoapplyCSS() {
	for (var i = 1; i <= 39; i++) {
		_id.o(i).className = "selected_bonusballlotto_numbers";
		_id.o(i).style.background = "url(" + projectName + "/LMSImages/images/bonusballlotto_shade_white.gif) center no-repeat";
		_id.o(i).style.color = "#155E33";
	}
	var clrChg = true;
	var obj = _id.o(bonusballlottocurSel);
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
	
	for (var l = startPoint; l < elms.length; l++) {
		if (elms[l].value != "") {
			if (count < maxCount) {
				_id.o(parseInt(elms[l].value, 10)).className = "selected_bonusballlotto_numbersSelect";
				_id.o(parseInt(elms[l].value, 10)).style.background = "url(" + projectName + "/LMSImages/images/bonusballlotto_shade_over.gif) center no-repeat";
				_id.o(parseInt(elms[l].value, 10)).style.color = "#FFFFFF";
				count++;
			}
		}
	}
	var obj1 = _id.o("lottoRow");
	var txtelms = obj1.getElementsByTagName("input");
	if (bonusballlottoIsAllQP) {
		for (var i = 1; i <= 39; i++) {
			_id.o(i).className = "selected_bonusballlotto_numbersDisabled";
			_id.o(i).style.background = "url(" + projectName + "/LMSImages/images/bonusballlotto_shade_disable.gif) center no-repeat";
			_id.o(i).style.color = "#FFFFFF";
		}
			for (var l = 0; l < txtelms.length; l++) {
				txtelms[l].disabled = true;
				txtelms[l].value="";
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
	_bonusballlottoCalAmt();
}
/*function bonusballlottoresetFrm() {
	document.forms[0].reset();
	for (var i = 1; i <= 39; i++) {
		_id.o(i).className = "selected_bonusballlotto_numbers";
	}
	var elms = document.forms[0].getElementsByTagName("input");
	for (var l = 0; l < elms.length; l++) {
		elms[l].disabled = false;
	}
	bonusballlottocurSel = "row1";
}*/
var gameChildW = null;
function bonusballlottoGameInfo() {
	if (gameChildW) {
	} else {
		gameChildW = window.open("bonusballlotto/gameInfo.jsp", "NewWin", "width=650,height=550,toolbar=0,resizable=0,scrollbars=1,alwaysRaised=0,location=0,menubar=0");
		gameChildW.close();
	}
	gameChildW = window.open("bonusballlotto/gameInfo.jsp", "NewWin", "width=650,height=550,toolbar=0,resizable=0,scrollbars=1,alwaysRaised=0,location=0,menubar=0");
}


function buyTicketbonusballlotto(id) {
	
	var compData = "";
	var noPicked = 5;
		if (!bonusballlottoIsAllQP) {
			for (var i = 1; i <= 5; i++) {
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
				if (inputData != "" && checkNumber(inputData.substring(0, inputData.length - 1), 1, 39)) {
					alert("Please fill Number only between 1 to 39");
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
			if (noPanels <1) {
				alert("Please Select At Least one Panel");
				_id.o("tkts").value = "";
				return false;
			}
		} else {
			var noPanels = _id.o("tkts").value;
			//alert(noPanels);
			if (noPanels <1) {
				alert("Please Select At Least one Panel");
				_id.o("tkts").value = "";
				_id.o("totAmtLto").value = "";
				return false;
			}
			if(noPanels >25){
				alert('You can select maximum 25 panels at a time');
				_id.o("tkts").value = "";
				_id.o("totAmtLto").value = "";
				return false;
			}
			
			for (var i = 1; i <= noPanels ; i++) {
				inputData = "QPNxt";
				compData = compData + inputData;
				//alert("compdata"+compData);
			}
			
		}
		
		//compData = compData.substring(0, compData.length - 3);
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
			for(i=0; i<_bonusballlottoDrawIdArr.length;i++){
				html += '&drawIdArr='+_bonusballlottoDrawIdArr[i];
			}
			html+='&isAdvancedPlay=1';
//alert("noOfDraws"+ _id.n("noOfDraws")[0].value);
				gameBuyAjaxReq("bonusballLottoBuy.action?pickedNumbers=" + compData + "&noOfDraws=" + _id.n("noOfDraws")[0].value  + "&totalPurchaseAmt=" + amount + "&noPicked=" + noPicked +"&userName=" + retName + html);		
				}
		else{
				gameBuyAjaxReq("bonusballLottoBuy.action?pickedNumbers=" + compData + "&noOfDraws=" + _id.n("noOfDraws")[0].value + "&totalPurchaseAmt=" + amount + "&noPicked=" + noPicked +"&isAdvancedPlay=" + 0 +"&userName=" + retName);	
}

	

	bonusballlottoclearAllBoxes();
	return true;
}


function bonusballlottoMultiPlay() {
	playTypeObj = _id.n("playType");
	for (var i = 0; i < playTypeObj.length; i += 1) {
		if (playTypeObj[i].checked == true) {
				_id.o("playDiv").innerHTML = bonusballlottoRow("5");
				bonusballlottocurSel = "row1";
				startPoint = 1;
			 
			playType = playTypeObj[i].value;
		}
	}
	bonusballlottoclearAllBoxes();
}





function keyPressQPBonuslotto(obj,e) {
	var keyCode = (window.event)? event.keyCode : e.which;
	if ((keyCode >= 48 && keyCode < 58) || keyCode == 45 || keyCode == 27 || keyCode == 13 || keyCode == 40 || keyCode == 38 || keyCode == 46 || keyCode == 8 || (keyCode >= 96 && keyCode < 106 || keyCode == 9)) {
		if (keyCode == 46 || keyCode == 8 || keyCode == 9) {
				bonusballlottoapplyCSS();
			
			return true;
		}
		if (obj.value.length >= 1) {
			var maxCount=25;
				//bonusballlottoRow(obj.value, 'yes');
			if (obj.value == 0 || obj.value > maxCount || isNaN(obj.value)) {
				obj.value = "";
				obj.focus();
			}
			
				bonusballlottoapplyCSS();
			
		}
	} else {
		return false;
	}
	return true;
}
function _bonusballlottoBetAmountMultiple(ele, func) {
	var num = _id.v(ele);
	
	if (func == "plus") {
		_id.o(ele).value = Math.round(((num < 5) ? (parseFloat(num == _bonusballlottoPrice ? _bonusballlottoPrice : num, 10) + parseFloat(_bonusballlottoPrice)) : num)*100)/100;
	} else {
		_id.o(ele).value = Math.round(((num > parseFloat(_bonusballlottoPrice)) ? (parseFloat(num == _bonusballlottoPrice ? _bonusballlottoPrice : num, 10) - parseFloat(_bonusballlottoPrice)) : num)*100)/100;
	}
	_bonusballlottoCalAmt(true);
}



var line = "<tr><td bgcolor=\"#4d8f00\"><table border=\"0\" bordercolor=\"blue\" width=\"97%\"><tr><td align=\"left\" width=\"25%\"><input type=\"hidden\" value=\"0\" id=\"counterId\"><input type=\"hidden\" id=\"dateFuture\"><input type=\"hidden\" id=\"freezeTimeId\" value=\"" + _bonusballlottofreezeTime + "\"><span class=\"style1\">Next Draw :</span><div id=\"latestDrawTime\" style=\"display:none\"></div></td><td width=\"20%\" align=\"left\"><div id=\"nxtDrawTime\" class=\"bonusballlottostyle2\"></div></td><td align=\"right\" width=\"25%\"><div id=\"timeLeft\"><span class=\"style1\"> Time Left :</div></span></td> <td align=\"left\" width=\"15%\"><div id=\"curDrwTime\" class=\"bonusballlottostyle2\"></div> </td><!--<td rowspan=\"2\" width=\"10%\"><img src=\"" + fortImgPath + "que.gif\" style=\"cursor:hand;\" onClick=\"gameInfo()\" alt=\"Game Info\" /> </td>--></tr> <tr bgcolor=\"#4d8f00\"><td align=\"left\"> <span class=\"style1\">No. of Draws :</span> </td> <td > <table><tr><td> <input type=\"radio\"/ name=\"conOrNonCon\" value=\"consec\" checked='true'  onclick=\"drawSelectButton(this)\"'/></td>  <td>  <select\tname=\"noOfDraws\" id=\"noOfDraws\"\tclass=\"fortune_option\" align=\"left\"\twidth=\"20%\"\tonchange=\"_bonusballlottoCalAmt()\">" + getActiveDrawOptionList("bonusballlotto") + "</select> </td> </tr>   </table>        </td>  <td>     <table> <tr><td><input type=\"radio\"/ name=\"conOrNonCon\" value=\"nonconsec\" onclick=\"specificDrawPopUp(_bonusballlottodrawSelect)\" />  </td><td class=\"style1\">Advance Draw</td></tr></table>     </td>      <td colspan=\"1\" align=\"left\" valign=\"top\"> &nbsp; </td><td align=\"left\"> <span class=\"bonusballlottonum\">" + currSymbol + " " + _bonusballlottoPrice + "/-</span></td> </tr><tr><td><table><tr><td align=\"right\" height=\"0px\" width=\"0px\"  id=\"drawSelectorDiv\" style=\"visibility:hidden;position:absolute;background-color:#FFFFFF\"></td></tr></table></td> </tr>         </table></td></tr>";
var _bonusballlottoNxtDrwTbl = _bonusballlottoFillNxtDrw();
var _bonusballlottoHtmlTbl = "<table width=\"100%\" height=\"100%\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\" bordercolor=\"red\" bgcolor=\"#a0e352\">" + line + "<tr><td align=\"center\" valign=\"top\"><table width=\"100%\" cellspacing=\"0\" cellpadding=\"0\" border=\"0\"><tr><td width=\"45%\" valign=\"top\"><div style=\"padding-top:10px;\"><table border=\"0\" bordercolor=\"red\" width=\"97%\" align=\"center\"cellpadding=\"0\"cellspacing=\"0\"><tr><td class=\"style10\" align=\"left\"><div align=\"center\" valign=\"middle\" style=\"height:18px;background-color:#4d8f00;width:180px;float:left;padding-top:2px\">&nbsp;Select Number Panel&nbsp;</div></td></tr><tr><td valign=\"middle\" style=\"border:1px #4d8f00 solid;background-color:#7dc924\">" + bonusballlottoNmSel(39) + "</td></tr></table></div></td><td valign=\"top\" wdith=\"35%\"><div style=\"border:0px solid black; padding-top:10px;\"><table border=\"0\" bordercolor=\"green\" width=\"96%\" cellpadding=\"0\" cellspacing=\"0\" align=\"center\" style=\"margin-right:10px\"><tr><td align=\"right\" class=\"style10\" style=\"cursor:hand;\" onclick=\"bonusballlottoclearAllBoxes()\" ><div align=\"center\" valign=\"middle\" style=\"height:18px;background-color:#4d8f00;width:100px;float:right;padding-top:2px\">&nbsp;&nbsp;&nbsp;Clear All&nbsp;&nbsp;&nbsp;</div></td></tr><tr><td valign=\"middle\" style=\"border:1px #4d8f00 solid;background-color:#7dc924\" colspan=2><table width=\"100%\" border=\"0\" align=\"center\" cellspacing=\"0\" cellpadding=\"0\"><tr><td><div id='playDiv'>" + bonusballlottoRow("5") + "</div></td></tr><tr><td bgcolor=\"#4d8f00\"><table width=\"95%\" border=\"0\" style=\"margin:6px 0px 5px 0px;\">                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    <tr><td align=\"center\" class=\"style5\" rowspan=\"2\"><div style=\"border:1px #FFFFFF solid; width: 50px; height: 40px; float: left;\">QP<br /><input type=\"checkbox\" id=\"qpChkAll\" onclick=\"bonusballlottoChkQP(this.id)\" style=\"border:1px #4d8f00 solid\" /></div></td><td class=\"bonusballlottotxtStyle\" align=\"left\">Total Qty:</td><td><input type=\"hidden\" value=\"\" id=\"pickedNumbers\" name=\"pickedNumbers\" /><input type=\"text\" class=\"bonusballlottotextbox\" id=\"tkts\" readonly=\"true\" value=\"0\" onkeyup='return keyPressQPBonuslotto(this,event)' onkeydown='return keyPressQPBonuslotto(this,event)' onmousedown=\"return disClick(this)\"  /></td><td rowspan=\"2\"><a href=\"#\"><img src=\"" + fortImgPath + "bonusballlotto_buy.gif\" id='buy' onclick=\"return buyTicketbonusballlotto(this.id)\" alt=\"Buy\" style=\"margin-right: 4px;\" /></a></td></tr><tr><td class=\"bonusballlottotxtStyle\" align=\"left\">Total Amount:</td><td><input type=\"text\" id=\"totAmtLto\" name=\"totalPurchaseAmt\" value=\"0\" readonly=\"true\" class=\"bonusballlottotextbox\" /></td></tr> </table></td></tr></table></td></tr></table></div></td></tr></table></tr><tr>      <td style=\"height:55px\";><div id=\"selectedDrawList\">No Draws Selected. Please Select at least one draw!</div></td></tr></table>";

                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                            																																																																																																																																																																																																																																				                                                                                                                                                                                                                                                                                                                                                                                                                                                                                        
var _bonusballlottoHTML = "<div id=\"strip\">" + drwMenu + "</div><form style=\"background-color:#FFFFFF;border: 1px solid #393939;\"><table  border=\"0\" width=\"100%\" height=\"100%\" cellspacing=\"0\" cellpadding=\"0\" align=\"center\" bgcolor=\"#FFFFFF\" bordercolor=\"red\"><tr><td width=\"10%\"  valign=\"top\" align=\"center\" onmousedown=\"return disClick(this)\"><input type=\"hidden\" id=\"drawType\"><div id=\"winningDispDiv\" style=\"border:1px #7dc924 solid;background-color:#FFFFFF;margin-left:5px;margin-top:3px\"></div><div id=\"nxtDrawDiv\" style=\"border:1px #7dc924 solid;background-color:#FFFFFF;margin-left:5px;margin-top:5px\"></div></td><td width=\"90%\" valign=\"top\"><div id=\"bonusballlottomiddle\" style=\"border:1px #7dc924 solid;background-color:#4d8f00;width:98%;margin-left:5px;margin-top:3px;margin-bottom:1px\">" + _bonusballlottoHtmlTbl + "</div></td></tr></table></td></tr></table></form>";

parent.frames[0].Version['$Source: /rep/LMS_Mgmt/WebRoot/com/skilrock/lms/web/drawGames/playMgmt/games/bonusballlotto/Attic/bonusballlotto.js,v $'] = '$Id: bonusballlotto.js,v 1.1.2.2.4.1 2013/09/19 05:12:31 yogesh Exp $';