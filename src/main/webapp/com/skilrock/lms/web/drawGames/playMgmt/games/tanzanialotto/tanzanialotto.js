
var _tanzanialottoWinArr = new Array();
var _tanzanialottoDrawIdArr = new Array();

var curDrwTimelotto = "";
var tanzanialottocurSel = "row1";
var tanzanialottoIsAllQP = false;
var _tanzanialottoPrice = parseFloat(gameDispName["tanzanialotto"].unitPrice.split("=")[1], 10);
var playType;
var startPoint;
var betAmt;
var _tanzanialottodrawSelect;
function setDefaulttanzanialotto() {
	curDrwTimefortune = "";
	tanzanialottocurSel = "row1";
	tanzanialottoIsAllQP = false;
	
	startPoint = 1;
}
function _tanzanialottoWinAppend() {
	if (!_tanzanialottoWin.match("Result Awaited")) {
		_tanzanialottoWin = "Result Awaited=\"\" Nxt" + _tanzanialottoWin;
	}
}
function _tanzanialottoLatestDrawTime() {
	return _tanzanialottoStatus.split("=")[1];
}
function _tanzanialottoFillWinTbl() {
	var _tanzanialottoWinHt = "<table width=\"100%\" border=\"0\" bordercolor=\"red\" cellspacing=\"0\" cellpadding=\"0\" align=\"center\" bgcolor=\"#FFF1D2\"><tr><td colspan=\"2\" align=\"left\"><div valign=\"middle\" style=\"height:22px;background-color:#F8AD40;padding-top:3px;font-size:16px;font-family:Arial;font-weight:bold;color:#FFFFFF\">&nbsp;Win Result&nbsp;</div></td></tr>      <tr><td valign=\"top\" align=\"center\" height=\"100\">";
	var winArr = _tanzanialottoWin.split("Nxt");
	var _tanzanialottoWinHTML = "<table width=\"100%\" border=\"0\" bordercolor=\"red\" style=\"margin-top:3px\"><tr><td><div style=\"" + scrollColor("#FFDF96", "#F8AD40", "184") + "\">";
	for (var i = 0; i <= winArr.length - 2; i++) {
		if (!winArr[i].match("Result")) {
			var time = new Date(parseInt(winArr[i].split("=")[0].replace(" ", "")));
			var newTime = week[time.getDay()] + " " + time.getDate() + " " + month[time.getMonth()] + "<br/>" + (time.getHours() < 10 ? "0" + time.getHours() : time.getHours()) + ":" + (time.getMinutes() < 10 ? "0" + time.getMinutes() : time.getMinutes()) + ":" + (time.getSeconds() < 10 ? "0" + time.getSeconds() : time.getSeconds());
			//alert(newTime);
			var htm = "<table width=\"90%\" border=\"0\" bordercolor=\"green\" id=\"result" + i + "\" cellspacing=\"2\" cellpadding=\"10\" class=\"tanzanialotto_winningResult_table\" ><tr><td class=\"tanzanialottowinningResultTime\" colspan=\"8\" height=\"53\">" + newTime + "</td></tr><tr>";
			var num = (winArr[i].split("=")[1]).split(",");
			for (var j = 0; j < num.length; j++) {
				var temp = (num[j] < 10 ? "0" + num[j] : num[j]);
				htm = htm + "<td width=\"10%\" class=\"selected_win_tanzanialotto_numbers\" align=\"center\">" + temp + "</td>";
			}
			htm = htm + "</tr></table>";
			_tanzanialottoWinHTML = _tanzanialottoWinHTML + htm;
		} else {
			var htm = "<table width=\"90%\" bordercolor=\"blue\" border=\"0\" cellspacing=\"2\" cellpadding=\"10\" id=\"result" + i + "\" class=\"tanzanialotto_winningResult_table\" ><tr><td class=\"tanzanialottowinningResultTime\" colspan=\"8\" height=\"53\" >Result Awaited</td></tr><tr>";
			htm = htm + "<td class=\"selected_win_tanzanialotto_numbers\"  colspan=\"8\"><div style=\"text-align:center\" id=\"winAwaitedDiv\" style=\"color:#DD8C17\"></div></td>";
			htm = htm + "</tr></table>";
			_tanzanialottoWinHTML = _tanzanialottoWinHTML + htm;
		}
	}
	_tanzanialottoWinHTML = _tanzanialottoWinHTML + "</div></td></tr></table>";
	return _tanzanialottoWinHt + _tanzanialottoWinHTML + "</td></tr></table>";
}

function _tanzanialottoWinAjx() {
	var url = "fetchWintanzanialotto.action?frmDate=" + _tanzanialottoWinArr[_tanzanialottoWinArr.length - 1];
	_tanzanialottoWin = _tanzanialottoWin + (winAjaxReq(url)).data;
	_tanzanialottoFillWinTbl();
}
function tanzanialottoNmSel(lstNo) {
	var htm = "<div id=\"\"><table width=\"100%\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\" bgcolor=\"#FFDF96\" style=\"margin-top:3px;margin-bottom:3px\"> <tr>";
	for (var i = 1; i <= lstNo; i++) {
		if (i % 5 == 0) {
			htm = htm + "<td width=\"17%\" align=\"right\" valign=\"middle\" style=\"cursor:pointer;background:url(" + projectName + "/LMSImages/images/tanzanialotto_shade_white.gif) center no-repeat\" class=\"selected_tanzanialotto_numbers\" onmouseout=\"tanzanialottoonmouseouteffect(this.id)\" onmouseover=\"tanzanialottoonmouseovereffect(this.id)\"  onclick=\"fillNumtanzlotto(" + i + ")\"  id=\"" + i + "\" >" + (i < 10 ? "0" + i : i) + "</td></tr>";
		} else {
			htm = htm + "<td width=\"17%\" align=\"right\" valign=\"middle\"  class=\"selected_tanzanialotto_numbers\"  style=\"cursor:pointer;background:url(" + projectName + "/LMSImages/images/tanzanialotto_shade_white.gif) center no-repeat\" onmouseout=\"tanzanialottoonmouseouteffect(this.id)\" onmouseover=\"tanzanialottoonmouseovereffect(this.id)\" onclick=\"fillNumtanzlotto(" + i + ")\"  id=\"" + i + "\"  >" + (i < 10 ? "0" + i : i) + "</td>";
		}
	}
	htm = htm + "</tr></table></div>";
	return htm;
}
function tanzanialottoonmouseovereffect(id) {
	var count = 0;
	var isFilled = false;
	for (var i = 1; i <= 49; i++) {
		if (_id.o(i).className == "selected_tanzanialotto_numbersSelect") {
			count++;
		}
		if (count > 5) {
			isFilled = true;
			break;
		}
	}
	if (!isFilled && !tanzanialottoIsAllQP) {
		if (_id.o(id).className != "selected_tanzanialotto_numbersSelect") {
			_id.o(id).style.background = "url(" + projectName + "/LMSImages/images/tanzanialotto_shade_over.gif) center no-repeat";
			_id.o(id).style.color = "#FFFFFF";
			_id.o(id).className = "selected_tanzanialotto_numbersSelectOMO";
		}
	}
}
function tanzanialottoonmouseouteffect(id) {
	if (!tanzanialottoIsAllQP) {
		if (_id.o(id).className == "selected_tanzanialotto_numbersSelectOMO") {
			_id.o(id).style.background = "url(" + projectName + "/LMSImages/images/tanzanialotto_shade_white.gif) center no-repeat";
			_id.o(id).style.color = "#DD8C17";
			_id.o(id).className = "selected_tanzanialotto_numbers";
		} else {
			if (_id.o(id).className == "selected_tanzanialotto_numbersSelect") {
				_id.o(id).style.background = "url(" + projectName + "/LMSImages/images/tanzanialotto_shade_over.gif) center no-repeat";
				_id.o(id).style.color = "#FFFFFF";
			} else {
				if (_id.o(id).className == "selected_tanzanialotto_numbers") {
					_id.o(id).style.background = "url(" + projectName + "/LMSImages/images/tanzanialotto_shade_white.gif) center no-repeat";
					_id.o(id).style.color = "#DD8C17";
				}
			}
		}
	}
}



function tanzanialottoChkQP(id) {
	if (_id.o(id).checked == true) {
		tanzanialottoIsAllQP = true;
		//_id.o("tkts").readOnly = false;
		tanzanialottoapplyCSS();
		
		_id.o("tkts").focus();
		_id.o("tkts").value = "";
		_id.o("tkts").readOnly = false;
		_tanzanialottoCalAmt();
		
	}
	else {
		tanzanialottoclearAllBoxes();
		}
}


function tanzanialottoRow(noOfpanels) {
	var htm = "<table width=\"100%\" id=\"lottoRow\" border=\"0\" cellspacing=\"2\" cellpadding=\"0\"><tr> <td width=\"8%\" align=\"center\" valign=\"middle\">&nbsp;</td><td width=\"10%\" align=\"center\" valign=\"middle\" class=\"style5\">&nbsp;</td>";
	for (var indx = 1; indx < 7; indx++) {
		htm = htm + "<td width=\"12%\" align=\"center\" valign=\"middle\" style=\"color:#DD8C17;text-align: center;font-weight:bold;font-size:16px\">" + indx + "</td>";
	}
	htm = htm + "</tr> <tr>";
	for (var i = 1; i < parseInt(noOfpanels) + 1; i++) {
		htm = htm + "<tr onclick=\"fillSeltanzlotto(" + i + ")\" id=\"row" + i + "\"><td align=\"center\" style=\"color:#DD8C17;text-align: center;font-weight:bold;font-size:16px\">" + i + "</td><td><input type=\"radio\" name=\"lottoRadio\" id=\"radio" + i + "\" style=\"border:1px #7dc924 solid\" onclick=\"fillSeltanzlotto(" + i + ")\" " + (i == 1 ? "checked" : "") + "/></td>";
		for (var j = 0; j < 6; j++) {
			htm = htm + "<td align=\"center\" valign=\"middle\" ><input type=\"text\" class=\"selected_tanzanialotto\" maxlength=\"2\" onkeydown=\"return keyPresstanzlotto(this," + i + ",event)\" onkeyup=\"return keyPresstanzlotto(this," + i + ",event)\" onblur=\"blurFntanzlotto(this)\" onmousedown=\"return disClick(this)\"></td>";
		}
		htm = htm + "</tr>";
	}
	//if(qp == 'yes'){
	//	_id.o('playDiv').innerHTML = htm+"</table>";
	//}
	return htm + "</table>";
}
function tanzanialottoclearBoxes(val) {
	_id.resetInpTypes("row" + val, "selected_tanzanialotto", false);
	_tanzanialottoCalAmt();
}
function tanzanialottoclearAllBoxes() {
	_id.resetInpTypes("lottoRow", "selected_tanzanialotto", false);
	_id.o("noOfDraws").value = 1;
	_id.o("tkts").readOnly = true;
	//tanzanialottocurSel = "row1";
	tanzanialottoIsAllQP = false;
	tanzanialottoapplyCSS();
	
	_id.o("qpChkAll").checked = false;
	_tanzanialottoCalAmt();
	document.getElementsByName('conOrNonCon')[0].click();

}
function _tanzanialottoFillNxtDrw() {
	var nxtDrwHTML = "<table width=\"100%\" cellspacing=\"0\"cellpadding=\"0\"  border=\"0\" bordercolor=\"red\"><tr><td colspan=\"2\" align=\"left\" valign=\"top\"><div valign=\"middle\" style=\"height:22px;width:180px;background-color:#F8AD40;padding-top:3px;font-size:16px;font-family:Arial;font-weight:bold;color:#FFFFFF\">&nbsp;Next Draw&nbsp;</div></td></tr><tr><td align=\"center\"valign=\"top\"><table width=\"100%\" bordercolor=\"red\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\" bgcolor=\"#FFDF96\"><tr><td width=\"100%\" align=\"left\" valign=\"top\" ><div style=\"" + scrollColor("#FFDF96", "#F8AD40", "175") + "\"><table width=\"90%\" align=\"left\" bordercolor=\"green\"  border=\"0\" cellspacing=\"0\" cellpadding=\"0\" class=\"borderbtmtanzanialotto\">";
	_tanzanialottodrawSelect = '<table width="auto" cellspacing="0"cellpadding="0"  border="1"><tr><td colspan="3" align="center"><b>Select Draws</td></tr>';
	var nxtDrwArr = _tanzanialottoDrawTime.split(",");
	for (var i = 0, l = nxtDrwArr.length; i < l; i++) {
		var time = new Date(parseInt(nxtDrwArr[i].replace(" ", "")));
		var newTime = week[time.getDay()] + " " + time.getDate() + " " + month[time.getMonth()] + " " + (time.getHours() < 10 ? "0" + time.getHours() : time.getHours()) + ":" + (time.getMinutes() < 10 ? "0" + time.getMinutes() : time.getMinutes());
		var hiddenTime = "<input type='text' id='timer" + i + "'>";
		var rowcolor;
		var data = "";
		if (i == 0) {
			data = time.getFullYear() + "-" + time.getMonth() + "-" + time.getDate() + "-" + (time.getHours() < 10 ? "0" + time.getHours() : time.getHours()) + "-" + (time.getMinutes() < 10 ? "0" + time.getMinutes() : time.getMinutes()) + "-" + (time.getSeconds() < 10 ? "0" + time.getSeconds() : time.getSeconds());
			var dateFuture1 = new Date(time.getFullYear(), time.getMonth(), time.getDate(), (time.getHours() < 10 ? "0" + time.getHours() : time.getHours()), (time.getMinutes() < 10 ? "0" + time.getMinutes() : time.getMinutes()), (time.getSeconds() < 10 ? "0" + time.getSeconds() : time.getSeconds()));
			_tanzanialottoNxtDrawTime = ("" + time).substring(0, ("" + time).lastIndexOf(":") + 3);
		}
		if (i % 2 == 0) {
			rowcolor = "#FFF1D2";
		} else {
			rowcolor = "#FFDF96";
		}
		nxtDrwHTML = nxtDrwHTML + "<tr><td width=\"100%\"><input type=\"hidden\" value=\"" + data + "\" id=\"" + i + "timer\"></td></tr><tr bgcolor=\"" + rowcolor + "\" style=\"height: 20px\"><td width=\"100%\" align=\"center\" class=\"borderbtmtanzanialotto\" colspan=\"2\"><div id=\"" + i + "blink\" >" + newTime + "</div></td><td width=\"44%\" align=\"center\"><p><div id=\"nxtDrwD" + i + "\" style=\"display:none\"><img src=\"" + fortImgPath + "freezed.gif\"></div></p></td></tr>";
		drawName = _tanzanialottoDrawIdObj[parseInt(nxtDrwArr[i].replace(" ", ""))].split(',')[1];
		if(drawName == 'null'){
			drawName = 'DRAW';
		}
		_tanzanialottodrawSelect = _tanzanialottodrawSelect + '<tr><td align="right"><input type="checkbox" title="'+newTime+'" name="drawIdChkBox" value="'+drawName+'" id="'+ _tanzanialottoDrawIdObj[parseInt(nxtDrwArr[i].replace(" ", ""))].split(',')[0] +'_did"/></td><td width="30%" nowarp="nowrap">'+drawName+'</td><td align="center">'+newTime+'</td></tr>'
	}
	nxtDrwHTML = nxtDrwHTML + "</table></div></td></tr></table></td></tr></table>";
	_tanzanialottodrawSelect = _tanzanialottodrawSelect + '<tr><td colspan="3" align="right"><input class="button" id="selNonConsecButton" onclick="drawSelectButton()" type="button" value="Select"/></td></tr></table>'
	return nxtDrwHTML;
}

function _tanzanialottoUpData(win, drwTime, drwStatus) {
	_tanzanialottoWin = win;
	_tanzanialottoDrawTime = drwTime;
	_tanzanialottoStatus = drwStatus;
}
function fillSeltanzlotto(val) {
	if (!tanzanialottoIsAllQP) {
	countFill=0;
		tanzanialottocurSel = "row" + val;
		_id.o("radio" + val).checked = true;
		var newObjR=_id.o(tanzanialottocurSel);
		var newElms = newObjR.getElementsByTagName("input");
		for (var l = startPoint; l < newElms.length; l++) {
			if (newElms[l].value == "") {
				newElms[l].focus();
				break;
			}else{
				countFill++;
			}
		}
		tanzanialottoapplyCSS();
	}
}
var countFill=0;
function fillNumtanzlotto(val) {   
	if (!tanzanialottoIsAllQP) {
		var objR = _id.o(tanzanialottocurSel);
		var clrChg = true;
		var elms = objR.getElementsByTagName("input");
		if (_id.o(val).className == "selected_tanzanialotto_numbersSelect") {
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
	
		if(countFill==6){
			//alert(parseInt(tanzanialottocurSel.replace("row",""),10)+1);
			countFill=0;
			var rowNo=parseInt(tanzanialottocurSel.replace("row",""),10)+1;
			if(rowNo>5){
				rowNo=1;
			}			
			tanzanialottocurSel="row"+rowNo;
			
			//alert(countFill);
			fillSeltanzlotto(rowNo);
		}
		else{
		tanzanialottoapplyCSS();
		}
	}
}
function _tanzanialottoCalAmt() {
	var noOfDrw = _id.o("noOfDraws").value;
	var noOfPanel = 0;
	var obj, elms;
	var isPanel = true;
	if(_id.n('conOrNonCon')[0]!= undefined && _id.n('conOrNonCon')[0].checked){
		noOfDrw = _id.n("noOfDraws")[0].value;
	}else if(_id.n('conOrNonCon')[1]!= undefined && _id.n('conOrNonCon')[1].checked){
		noOfDrw = _tanzanialottoDrawIdArr.length;
		//alert(noofDrw);
	}
	if (tanzanialottoIsAllQP) {
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
	var unitPrice=_tanzanialottoPrice;
		
	var totalPrice=noOfDrw * noOfPanel * _tanzanialottoPrice;
	totalPrice=tktPriceRound(totalPrice);
	//alert(totalPrice);
	_id.o("totAmtLto").value = totalPrice;
	//alert(document.getElementById('betAmount').value);
}
function fillNoOfPanels(id) {
	var noOfpanels = _id.o(id).value;
	_id.i("lottoRowDiv", lottoRow(noOfpanels));
}
function keyPresstanzlotto(obj, val,e) {
	var keyCode = (window.event)? event.keyCode : e.which;
	//var keyCode = event.keyCode;
	//alert(keyCode);
	if ((keyCode >= 48 && keyCode < 58) || keyCode == 45 || keyCode == 27 || keyCode == 13 || keyCode == 40 || keyCode == 38 || keyCode == 46 || keyCode == 8 || (keyCode >= 96 && keyCode < 106 || keyCode == 9)) {
		if (keyCode == 46 || keyCode == 8 || keyCode == 9) {
			if (keyCode == 9) {
				//alert(val);
				//fillSeltanzlotto(val);
				return false;
			}
			return true;
		}
		if (obj.value.length > 1) {
			if (obj.value == 0 || obj.value > 49 || isNaN(obj.value)) {
				obj.value = "";
				obj.focus();
			} else {
				var objR = _id.o(tanzanialottocurSel);
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
						var nxtRow = parseInt(tanzanialottocurSel.substring(tanzanialottocurSel.length - 1), 10) + 1;
						if (nxtRow <= 5) {
							var nxtRowStr = tanzanialottocurSel.substring(0, tanzanialottocurSel.length - 1) + nxtRow;
						//alert(nxtRowStr);
							_id.o(nxtRowStr).getElementsByTagName("input")[1].focus();
							fillSeltanzlotto(nxtRow);
						}
					}
				}
			}
			tanzanialottoapplyCSS();
		}
	} else {
		return false;
	}
	return true;
}
function blurFntanzlotto(obj) {
	if ((obj.value.length < 2) || isNaN(obj.value) || obj.value == 0) {
		obj.value = "";
	}
	tanzanialottoapplyCSS();
}
function tanzanialottoapplyCSS() {
	for (var i = 1; i <= 49; i++) {
		_id.o(i).className = "selected_tanzanialotto_numbers";
		_id.o(i).style.background = "url(" + projectName + "/LMSImages/images/tanzanialotto_shade_white.gif) center no-repeat";
		_id.o(i).style.color = "#DD8C17";
	}
	var clrChg = true;
	var obj = _id.o(tanzanialottocurSel);
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
				_id.o(parseInt(elms[l].value, 10)).className = "selected_tanzanialotto_numbersSelect";
				_id.o(parseInt(elms[l].value, 10)).style.background = "url(" + projectName + "/LMSImages/images/tanzanialotto_shade_over.gif) center no-repeat";
				_id.o(parseInt(elms[l].value, 10)).style.color = "#FFFFFF";
				count++;
			}
		}
	}
	var obj1 = _id.o("lottoRow");
	var txtelms = obj1.getElementsByTagName("input");
	if (tanzanialottoIsAllQP) {
		for (var i = 1; i <= 49; i++) {
			_id.o(i).className = "selected_tanzanialotto_numbersDisabled";
			_id.o(i).style.background = "url(" + projectName + "/LMSImages/images/tanzanialotto_shade_disable.gif) center no-repeat";
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
	_tanzanialottoCalAmt();
}
/*function tanzanialottoresetFrm() {
	document.forms[0].reset();
	for (var i = 1; i <= 49; i++) {
		_id.o(i).className = "selected_tanzanialotto_numbers";
	}
	var elms = document.forms[0].getElementsByTagName("input");
	for (var l = 0; l < elms.length; l++) {
		elms[l].disabled = false;
	}
	tanzanialottocurSel = "row1";
}*/
var gameChildW = null;
function tanzanialottoGameInfo() {
	if (gameChildW) {
	} else {
		gameChildW = window.open("tanzanialotto/gameInfo.jsp", "NewWin", "width=650,height=550,toolbar=0,resizable=0,scrollbars=1,alwaysRaised=0,location=0,menubar=0");
		gameChildW.close();
	}
	gameChildW = window.open("tanzanialotto/gameInfo.jsp", "NewWin", "width=650,height=550,toolbar=0,resizable=0,scrollbars=1,alwaysRaised=0,location=0,menubar=0");
}


function buyTickettanzanialotto(id) {
	
	var compData = "";
	var noPicked = 6;
		if (!tanzanialottoIsAllQP) {
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
				if (inputData != "" && checkNumber(inputData.substring(0, inputData.length - 1), 1, 49)) {
					alert("Please fill Number only between 1 to 49");
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
			if (noPanels % 5 != 0) {
			
				alert("Please Select At Least five Panel");
				_id.o("tkts").value = "";
				return false;
			}
		} else {
			var noPanels = _id.o("tkts").value;
			//alert(noPanels);
			if (noPanels % 5 != 0) {
				alert("Please Select At Least five Panel");
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
			for(i=0; i<_tanzanialottoDrawIdArr.length;i++){
				html += '&drawIdArr='+_tanzanialottoDrawIdArr[i];
			}
			html+='&isAdvancedPlay=1';
//alert("noOfDraws"+ _id.n("noOfDraws")[0].value);
				gameBuyAjaxReq("tanzaniaLottoBuy.action?pickedNumbers=" + compData + "&noOfDraws=" + _id.n("noOfDraws")[0].value  + "&totalPurchaseAmt=" + amount + "&noPicked=" + noPicked + html);		
				}
		else{
				gameBuyAjaxReq("tanzaniaLottoBuy.action?pickedNumbers=" + compData + "&noOfDraws=" + _id.n("noOfDraws")[0].value + "&totalPurchaseAmt=" + amount + "&noPicked=" + noPicked);	
}

	

	tanzanialottoclearAllBoxes();
	return true;
}


function tanzanialottoMultiPlay() {
	playTypeObj = _id.n("playType");
	for (var i = 0; i < playTypeObj.length; i += 1) {
		if (playTypeObj[i].checked == true) {
				_id.o("playDiv").innerHTML = tanzanialottoRow("5");
				tanzanialottocurSel = "row1";
				startPoint = 1;
			 
			playType = playTypeObj[i].value;
		}
	}
	tanzanialottoclearAllBoxes();
}





function keyPressQPtanzlotto(obj,e) {
	var keyCode = (window.event)? event.keyCode : e.which;
	if ((keyCode >= 48 && keyCode < 58) || keyCode == 45 || keyCode == 27 || keyCode == 13 || keyCode == 40 || keyCode == 38 || keyCode == 46 || keyCode == 8 || (keyCode >= 96 && keyCode < 106 || keyCode == 9)) {
		if (keyCode == 46 || keyCode == 8 || keyCode == 9) {
				tanzanialottoapplyCSS();
			
			return true;
		}
		if (obj.value.length >= 1) {
			var maxCount=25;
				//tanzanialottoRow(obj.value, 'yes');
			if (obj.value == 0 || obj.value > maxCount || isNaN(obj.value)) {
				obj.value = "";
				obj.focus();
			}
			
				tanzanialottoapplyCSS();
			
		}
	} else {
		return false;
	}
	return true;
}
function _tanzanialottoBetAmountMultiple(ele, func) {
	var num = _id.v(ele);
	
	if (func == "plus") {
		_id.o(ele).value = Math.round(((num < 5) ? (parseFloat(num == _tanzanialottoPrice ? _tanzanialottoPrice : num, 10) + parseFloat(_tanzanialottoPrice)) : num)*100)/100;
	} else {
		_id.o(ele).value = Math.round(((num > parseFloat(_tanzanialottoPrice)) ? (parseFloat(num == _tanzanialottoPrice ? _tanzanialottoPrice : num, 10) - parseFloat(_tanzanialottoPrice)) : num)*100)/100;
	}
	_tanzanialottoCalAmt(true);
}



var line = "<tr><td bgcolor=\"#F8AD40\"><table border=\"0\" bordercolor=\"blue\" width=\"97%\"><tr><td align=\"left\" width=\"25%\"><input type=\"hidden\" value=\"0\" id=\"counterId\"><input type=\"hidden\" id=\"dateFuture\"><input type=\"hidden\" id=\"freezeTimeId\" value=\"" + _tanzanialottofreezeTime + "\"><span class=\"style1\">Next Draw :</span><div id=\"latestDrawTime\" style=\"display:none\"></div></td><td width=\"20%\" align=\"left\"><div id=\"nxtDrawTime\" class=\"tanzanialottostyle2\"></div></td><td align=\"right\" width=\"25%\"><div id=\"timeLeft\"><span class=\"style1\"> Time Left :</div></span></td> <td align=\"left\" width=\"15%\"><div id=\"curDrwTime\" class=\"tanzanialottostyle2\"></div> </td><!--<td rowspan=\"2\" width=\"10%\"><img src=\"" + fortImgPath + "que.gif\" style=\"cursor:hand;\" onClick=\"gameInfo()\" alt=\"Game Info\" /> </td>--></tr> <tr bgcolor=\"#F8AD40\"><td align=\"left\"> <span class=\"style1\">No. of Draws :</span> </td> <td > <table><tr><td> <input type=\"radio\"/ name=\"conOrNonCon\" value=\"consec\" checked='true'  onclick=\"drawSelectButton(this)\"'/></td>  <td>  <select\tname=\"noOfDraws\" id=\"noOfDraws\"\tclass=\"fortune_option\" align=\"left\"\twidth=\"20%\"\tonchange=\"_tanzanialottoCalAmt()\">" + getActiveDrawOptionList("tanzanialotto") + "</select> </td> </tr>   </table>        </td>  <td>     <table> <tr><td><input type=\"radio\"/ name=\"conOrNonCon\" value=\"nonconsec\" onclick=\"specificDrawPopUp(_tanzanialottodrawSelect)\" />  </td><td class=\"style1\">Advance Draw</td></tr></table>     </td>      <td colspan=\"1\" align=\"left\" valign=\"top\"> &nbsp; </td><td align=\"left\"> <span class=\"tanzanialottonum\">" + currSymbol + " " + _tanzanialottoPrice + "/-</span></td> </tr><tr><td><table><tr><td align=\"right\" height=\"0px\" width=\"0px\"  id=\"drawSelectorDiv\" style=\"visibility:hidden;position:absolute;background-color:#FFFFFF\"></td></tr></table></td> </tr>         </table></td></tr>";
var _tanzanialottoNxtDrwTbl = _tanzanialottoFillNxtDrw();



var _tanzanialottoHtmlTbl = "<table width=\"100%\" height=\"100%\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\" bordercolor=\"red\" bgcolor=\"#FFF1D2\">" + line + "<tr><td align=\"center\" valign=\"top\"><table width=\"100%\" cellspacing=\"0\" cellpadding=\"0\" border=\"0\"><tr><td width=\"45%\" valign=\"top\"><div style=\"padding-top:10px;\"><table border=\"0\" bordercolor=\"red\" width=\"97%\" align=\"center\"cellpadding=\"0\"cellspacing=\"0\"><tr><td class=\"style10\" align=\"left\"><div id='jackpotAmt' align=\"center\" valign=\"middle\" style=\"height:18px;background-color:#F8AD40;width:180px;float:left;padding-top:2px\"></div></td></tr><tr><td valign=\"middle\" style=\"border:1px #4d8f00 solid;background-color:#FFDF96\">" + tanzanialottoNmSel(49) + "</td></tr></table></div></td><td valign=\"top\" wdith=\"35%\"><div style=\"border:0px solid black; padding-top:10px;\"><table border=\"0\" bordercolor=\"green\" width=\"96%\" cellpadding=\"0\" cellspacing=\"0\" align=\"center\" style=\"margin-right:10px\"><tr><td align=\"right\" class=\"style10\" style=\"cursor:hand;\" onclick=\"tanzanialottoclearAllBoxes()\" ><div align=\"center\" valign=\"middle\" style=\"height:18px;background-color:#F8AD40;width:100px;float:right;padding-top:2px\">&nbsp;&nbsp;&nbsp;Clear All&nbsp;&nbsp;&nbsp;</div></td></tr><tr><td valign=\"middle\" style=\"border:1px #F8AD40 solid;background-color:#FFDF96\" colspan=2><table width=\"100%\" border=\"0\" align=\"center\" cellspacing=\"0\" cellpadding=\"0\"><tr><td><div id='playDiv'>" + tanzanialottoRow("5") + "</div></td></tr><tr><td bgcolor=\"#F8AD40\"><table width=\"95%\" border=\"0\" style=\"margin:6px 0px 5px 0px;\">                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    <tr><td align=\"center\" class=\"style5\" rowspan=\"2\"><div style=\"border:1px #FFFFFF solid; width: 50px; height: 40px; float: left;\">QP<br /><input type=\"checkbox\" id=\"qpChkAll\" onclick=\"tanzanialottoChkQP(this.id)\" style=\"border:1px #4d8f00 solid\" /></div></td><td class=\"tanzanialottotxtStyle\" align=\"left\">Total Qty:</td><td><input type=\"hidden\" value=\"\" id=\"pickedNumbers\" name=\"pickedNumbers\" /><input type=\"text\" class=\"tanzanialottotextbox\" id=\"tkts\" readonly=\"true\" value=\"0\" onkeyup='return keyPressQPtanzlotto(this,event)' onkeydown='return keyPressQPtanzlotto(this,event)' onmousedown=\"return disClick(this)\"  /></td><td rowspan=\"2\"><a href=\"#\"><img src=\"" + fortImgPath + "tanzanialotto_buy.gif\" id='buy' onclick=\"return buyTickettanzanialotto(this.id)\" alt=\"Buy\" style=\"margin-right: 4px;\" /></a></td></tr><tr><td class=\"tanzanialottotxtStyle\" align=\"left\">Total Amount:</td><td><input type=\"text\" id=\"totAmtLto\" name=\"totalPurchaseAmt\" value=\"0\" readonly=\"true\" class=\"tanzanialottotextbox\" /></td></tr> </table></td></tr></table></td></tr></table></div></td></tr></table></tr><tr>      <td style=\"height:55px\";><div id=\"selectedDrawList\">No Draws Selected. Please Select at least one draw!</div></td></tr></table>";

                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                            																																																																																																																																																																																																																																				                                                                                                                                                                                                                                                                                                                                                                                                                                                                                        
var _tanzanialottoHTML = "<div id=\"strip\">" + drwMenu + "</div><form style=\"background-color:#FFFFFF;border: 1px solid #393939;\"><table  border=\"0\" width=\"100%\" height=\"100%\" cellspacing=\"0\" cellpadding=\"0\" align=\"center\" bgcolor=\"#FFFFFF\" bordercolor=\"red\"><tr><td width=\"10%\"  valign=\"top\" align=\"center\" onmousedown=\"return disClick(this)\"><input type=\"hidden\" id=\"drawType\"><div id=\"winningDispDiv\" style=\"border:1px #F8AD40 solid;background-color:#FFFFFF;margin-left:5px;margin-top:3px\"></div><div id=\"nxtDrawDiv\" style=\"border:1px #F8AD40 solid;background-color:#FFFFFF;margin-left:5px;margin-top:5px\"></div></td><td width=\"90%\" valign=\"top\"><div id=\"tanzanialottomiddle\" style=\"border:1px #7dc924 solid;background-color:#FFDF96;width:98%;margin-left:5px;margin-top:3px;margin-bottom:1px\">" + _tanzanialottoHtmlTbl + "</div></td></tr></table></td></tr></table></form>";



parent.frames[0].Version['$Source: /rep/LMS_Mgmt/WebRoot/com/skilrock/lms/web/drawGames/playMgmt/games/tanzanialotto/Attic/tanzanialotto.js,v $'] = '$Id: tanzanialotto.js,v 1.1.2.2.4.1 2013/09/19 05:12:34 yogesh Exp $';


