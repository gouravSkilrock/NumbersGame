
var _zimlottoWinArr = new Array();
var curDrwTimelotto = "";
var zimlottocurSel = "row1";
var zimlottoIsAllQP = false;
var _zimlottoPrice = parseInt(gameDispName["zimlotto"].unitPrice.split("=")[1], 10);
function setDefaultzimlotto() {
	curDrwTimefortune = "";
	zimlottocurSel = "row1";
	zimlottoIsAllQP = false;
}
function _zimlottoWinAppend() {
	if (!_zimlottoWin.match("Result Awaited")) {
		_zimlottoWin = "Result Awaited=\"\" Nxt" + _zimlottoWin;
	}
}
function _zimlottoLatestDrawTime() {
	return _zimlottoStatus.split("=")[1];
}
function _zimlottoFillWinTbl() {
	var _zimlottoWinHt = "<table width=\"100%\" border=\"0\" bordercolor=\"red\" cellspacing=\"0\" cellpadding=\"0\" align=\"center\" bgcolor=\"#FFF1D2\"><tr><td colspan=\"2\" align=\"left\"><div valign=\"middle\" style=\"height:22px;background-color:#F8AD40;padding-top:3px;font-size:16px;font-family:Arial;font-weight:bold;color:#FFFFFF\">&nbsp;Win Result&nbsp;</div></td></tr><tr><td valign=\"top\" align=\"center\">";
	var winArr = _zimlottoWin.split("Nxt");
	var _zimlottoWinHTML = "<table width=\"100%\" border=\"0\" style=\"margin-top:3px\"><tr><td><div style=\"" + scrollColor("#FFDF96", "#F8AD40", "184") + "\">";
	for (var i = 0; i <= winArr.length - 2; i++) {
		if (!winArr[i].match("Result")) {
			var time = new Date(parseInt(winArr[i].split("=")[0].replace(" ", "")));
			var newTime = week[time.getDay()] + " " + time.getDate() + " " + month[time.getMonth()] + " " + (time.getHours() < 10 ? "0" + time.getHours() : time.getHours()) + ":" + (time.getMinutes() < 10 ? "0" + time.getMinutes() : time.getMinutes()) + ":" + (time.getSeconds() < 10 ? "0" + time.getSeconds() : time.getSeconds());
			var htm = "<table width=\"90%\" border=\"0\" bordercolor=\"green\" id=\"result" + i + "\" cellspacing=\"2\" cellpadding=\"10\" class=\"zimlotto_winningResult_table\"><tr><td class=\"zimlottowinningResultTime\" colspan=\"8\" height=\"39\">" + newTime + "</td></tr><tr>";
			var num = (winArr[i].split("=")[1]).split(",");
			for (var j = 0; j < num.length; j++) {
				if (j == 3) {
					htm = htm + "</tr><tr>";
				}
				var temp = (num[j] < 10 ? "0" + num[j] : num[j]);
				htm = htm + "<td width=\"10%\" align=\"center\"><span class=\"selected_win_zimlotto_numbers\">" + trim(temp, " ") + "</span></td>";
			}
			htm = htm + "</tr></table>";
			_zimlottoWinHTML = _zimlottoWinHTML + htm;
		} else {
			var htm = "<table width=\"90%\" bordercolor=\"blue\" border=\"0\" cellspacing=\"2\" cellpadding=\"10\" id=\"result" + i + "\" class=\"zimlotto_winningResult_table\" ><tr><td class=\"zimlottowinningResultTime\" colspan=\"8\" height=\"44\" >Result Awaited</td></tr><tr>";
			htm = htm + "<td class=\"selected_win_zimlotto_numbers\" colspan=\"8\"><div style=\"text-align:center\" id=\"winAwaitedDiv\" style=\"color:#DD8C17\"></div></td>";
			htm = htm + "</tr></table>";
			_zimlottoWinHTML = _zimlottoWinHTML + htm;
		}
	}
	_zimlottoWinHTML = _zimlottoWinHTML + "</div></td></tr></table>";
	return _zimlottoWinHt + _zimlottoWinHTML + "</td></tr></table>";
}
function _zimlottoWinAjx() {
	var url = "fetchWinZimLotto.action?frmDate=" + _zimlottoWinArr[_zimlottoWinArr.length - 1];
	_zimlottoWin = _zimlottoWin + (winAjaxReq(url)).data;
	_zimlottoFillWinTbl();
}
function zimlottoNmSel(lstNo) {
	var htm = "<div id=\"\"><table width=\"100%\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\" bgcolor=\"#FFDF96\" style=\"margin-top:3px;margin-bottom:3px\"> <tr>";
	for (var i = 1; i <= lstNo; i++) {
		if (i % 5 == 0) {
			htm = htm + "<td width=\"17%\" align=\"right\" valign=\"middle\" style=\"cursor:pointer;background:url(" + projectName + "/LMSImages/images/zimlotto_shade_white.gif) center no-repeat\" class=\"selected_zimlotto_numbers\" onmouseout=\"zimlottoonmouseouteffect(this.id)\" onmouseover=\"zimlottoonmouseovereffect(this.id)\"  onclick=\"fillNumzimLto(" + i + ")\"  id=\"" + i + "\" >" + (i < 10 ? "0" + i : i) + "</td></tr>";
		} else {
			htm = htm + "<td width=\"17%\" align=\"right\" valign=\"middle\"  class=\"selected_zimlotto_numbers\"  style=\"cursor:pointer;background:url(" + projectName + "/LMSImages/images/zimlotto_shade_white.gif) center no-repeat\" onmouseout=\"zimlottoonmouseouteffect(this.id)\" onmouseover=\"zimlottoonmouseovereffect(this.id)\" onclick=\"fillNumzimLto(" + i + ")\"  id=\"" + i + "\"  >" + (i < 10 ? "0" + i : i) + "</td>";
		}
	}
	htm = htm + "</tr></table></div>";
	return htm;
}
function zimlottoonmouseovereffect(id) {
	var count = 0;
	var isFilled = false;
	for (var i = 1; i <= 42; i++) {
		if (_id.o(i).className == "selected_zimlotto_numbersSelect") {
			count++;
		}
		if (count > 5) {
			isFilled = true;
			break;
		}
	}
	if (!_id.o("QP" + zimlottocurSel.replace("row", "")).checked && !isFilled && !zimlottoIsAllQP) {
		if (_id.o(id).className != "selected_zimlotto_numbersSelect") {
			_id.o(id).style.background = "url(" + projectName + "/LMSImages/images/zimlotto_shade_over.gif) center no-repeat";
			_id.o(id).style.color = "#FFFFFF";
			_id.o(id).className = "selected_zimlotto_numbersSelectOMO";
		}
	}
}
function zimlottoonmouseouteffect(id) {
	if (!_id.o("QP" + zimlottocurSel.replace("row", "")).checked && !zimlottoIsAllQP) {
		if (_id.o(id).className == "selected_zimlotto_numbersSelectOMO") {
			_id.o(id).style.background = "url(" + projectName + "/LMSImages/images/zimlotto_shade_white.gif) center no-repeat";
			_id.o(id).style.color = "#DD8C17";
			_id.o(id).className = "selected_zimlotto_numbers";
		} else {
			if (_id.o(id).className == "selected_zimlotto_numbersSelect") {
				_id.o(id).style.background = "url(" + projectName + "/LMSImages/images/zimlotto_shade_over.gif) center no-repeat";
				_id.o(id).style.color = "#FFFFFF";
			} else {
				if (_id.o(id).className == "selected_zimlotto_numbers") {
					_id.o(id).style.background = "url(" + projectName + "/LMSImages/images/zimlotto_shade_white.gif) center no-repeat";
					_id.o(id).style.color = "#DD8C17";
				}
			}
		}
	}
}
function zimlottochkAll(id) {
	if (_id.o(id).checked == true) {
		for (var i = 0; i < _id.n("isQuickPick").length; i++) {
			var e = _id.n("isQuickPick")[i];
			e.checked = true;
			quickPkzimLto(i + 1);
		}
	} else {
		for (var i = 0; i < _id.n("isQuickPick").length; i++) {
			var e = _id.n("isQuickPick")[i];
			e.checked = false;
			quickPkzimLto(i + 1);
		}
	}
	_id.o("radio1").checked = true;
	zimlottocurSel = "row1";
	zimlottoapplyCSS();
}
function zimlottoChkNoOfQP() {
	var val = parseInt(_id.o("tkts").value);
	if (isNaN(val)) {
		val = 0;
	}
	for (var i = 0; i < val; i++) {
		var e = _id.n("isQuickPick")[i];
		e.checked = true;
		quickPkzimLto(i + 1);
	}
	for (var i = parseInt(val); i < _id.n("isQuickPick").length; i++) {
		var e = _id.n("isQuickPick")[i];
		e.checked = false;
		quickPkzimLto(i + 1);
	}
	var rowNo = (parseInt(val) + 1 > 8) ? 1 : parseInt(val) + 1;
	_id.o("radio" + rowNo).checked = true;
	zimlottocurSel = "row" + rowNo;
	zimlottoapplyCSS();
}
function zimlottoChkQP(id) {
	if (_id.o(id).checked == true) {
		zimlottoIsAllQP = true;
		_id.o("tkts").readOnly = false;
		zimlottoapplyCSS();
		_id.o("tkts").focus();
		_id.o("tkts").value = "";
	} else {
		zimlottoIsAllQP = false;
		for (var i = 0; i < _id.n("isQuickPick").length; i++) {
			var e = _id.n("isQuickPick")[i];
			e.checked = false;
			quickPkzimLto(i + 1);
		}
		_id.o("tkts").value = "0";
		_id.o("tkts").readOnly = true;
		_id.o("radio1").checked = true;
		zimlottocurSel = "row1";
		zimlottoapplyCSS();
	}
}
function zimlottofillNoOfQp(obj,e) {
	var keyCode = (window.event) ? event.keyCode : e.which;
	if ((keyCode >= 48 && keyCode < 58) || keyCode == 45 || keyCode == 27 || keyCode == 13 || keyCode == 40 || keyCode == 38 || keyCode == 46 || keyCode == 8 || (keyCode >= 96 && keyCode < 106 || keyCode == 9 || keyCode == 189 || keyCode == 109)) {
		if (keyCode == 46 || keyCode == 8 || keyCode == 9) {
			zimlottoChkNoOfQP();
			return true;
		}
		if (obj.value == 0 || obj.value > 8 || isNaN(obj.value)) {
			obj.value = "";
			obj.focus();
		}
	} else {
		return false;
	}
	zimlottoChkNoOfQP();
	return true;
}
function zimlottoRow(noOfpanels) {
	var htm = "<table width=\"100%\" id=\"lottoRow\" border=\"0\" cellspacing=\"2\" cellpadding=\"0\"><tr> <td width=\"8%\" align=\"center\" valign=\"middle\">&nbsp;</td><td width=\"10%\" align=\"center\" valign=\"middle\" class=\"style5\">&nbsp;</td>";
	for (var indx = 1; indx < 7; indx++) {
		htm = htm + "<td width=\"12%\" align=\"center\" valign=\"middle\" style=\"color:#DD8C17;text-align: center;font-weight:bold;font-size:16px\">" + indx + "</td>";
	}
	htm = htm + "<td width=\"12%\" align=\"center\" valign=\"middle\" style=\"color:#DD8C17;text-align: center;font-weight:bold;font-size:16px\">QP</td> </tr> <tr>";
	for (var i = 1; i < parseInt(noOfpanels) + 1; i++) {
		htm = htm + "<tr onclick=\"fillSelzimLto(" + i + ")\" id=\"row" + i + "\"><td align=\"center\" style=\"color:#DD8C17;text-align: center;font-weight:bold;font-size:16px\">" + i + "</td><td><input type=\"radio\" name=\"lottoRadio\" id=\"radio" + i + "\" style=\"border:1px #FFDF96 solid\" onclick=\"fillSelzimLto(" + i + ")\" " + (i == 1 ? "checked" : "") + "/></td>";
		for (var j = 0; j < 6; j++) {
			htm = htm + "<td align=\"center\" valign=\"middle\" ><input type=\"text\" class=\"selected_zimlotto\" maxlength=\"2\" onkeydown=\"return keyPresszimLto(this," + i + ",event)\" onkeyup=\"return keyPresszimLto(this," + i + ",event)\" onblur=\"blurFnzimLto(this)\" onmousedown=\"return disClick(this)\"></td>";
		}
		htm = htm + " <td align=\"center\" valign=\"middle\"><input type=\"checkbox\" value=\"2\" style=\"border:1px #FFDF96 solid\" name=\"isQuickPick\" id=\"QP" + i + "\" onclick=\"quickPkzimLto(" + i + ");\" /><img src=\"" + fortImgPath + "clear.gif\" onclick=\"zimlottoclearBoxes(" + i + ")\" style=\"cursor:hand\"></td></tr>";
	}
	return htm + "</table>";
}
function zimlottoclearBoxes(val) {
	_id.resetInpTypes("row" + val, "selected_zimlotto", false);
	/*var obj = _id.o('row'+val);
var elms = obj.getElementsByTagName("input");
	for(var l=1;l<elms.length-1;l++){
			elms[l].value="";
	}*/
	_zimlottoCalAmt();
}
function zimlottoclearAllBoxes() {
	_id.resetInpTypes("lottoRow", "selected_zimlotto", false);
	_id.n("noOfDraws")[0].value = 1;
	_id.o("tkts").readOnly = true;
	zimlottocurSel = "row1";
	/*
	var obj = _id.o('lottoRow');
	var elms = obj.getElementsByTagName("input");

	for(var l=1;l<elms.length;l++){
		if(elms[l].type=='text'){
			elms[l].value="";elms[l].disabled=false;
			}
		else if(elms[l].type=='checkbox')
				elms[l].checked=false;
	}
	
	for(var i=1;i<=42;i++){	
		_id.o(i).className="selected_zimlotto_numbers";
		_id.o(i).style.background='url('+projectName+'/LMSImages/images/zimlotto_shade_white.gif) center no-repeat';
		_id.o(i).style.color='#F8AD40';
	}
	*/
	zimlottoIsAllQP = false;
	zimlottoapplyCSS();
	_id.o("qpChkAll").checked = false;
	_zimlottoCalAmt();
}
function _zimlottoFillNxtDrw() {
	var nxtDrwHTML = "<table width=\"100%\" cellspacing=\"0\"cellpadding=\"0\"  border=\"0\" bordercolor=\"red\"><tr><td colspan=\"2\" align=\"left\" valign=\"top\"><div valign=\"middle\" style=\"height:22px;width:180px;background-color:#F8AD40;padding-top:3px;font-size:16px;font-family:Arial;font-weight:bold;color:#FFFFFF\">&nbsp;Next Draw&nbsp;</div></td></tr><tr><td align=\"center\" valign=\"top\"><div><table width=\"100% bordercolor=\"red\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\" ><tr><td width=\"100%\" align=\"center\" valign=\"top\" colspan=\"2\"><div style=\"" + scrollColor("#FFDF96", "#F8AD40", "175") + "\"><table width=\"90%\" bordercolor=\"red\"  border=\"0\" cellspacing=\"0\" cellpadding=\"0\" class=\"borderbtmzimlotto\">";
	var nxtDrwArr = _zimlottoDrawTime.split(",");
	for (var i = 0, l = nxtDrwArr.length; i < l; i++) {
		var time = new Date(parseInt(nxtDrwArr[i].replace(" ", "")));
		var newTime = week[time.getDay()] + " " + time.getDate() + " " + month[time.getMonth()] + " " + (time.getHours() < 10 ? "0" + time.getHours() : time.getHours()) + ":" + (time.getMinutes() < 10 ? "0" + time.getMinutes() : time.getMinutes());
		var hiddenTime = "<input type='text'  id='timer" + i + "'>";
		var rowcolor;
		var data = "";
		if (i == 0) {
			data = time.getFullYear() + "-" + time.getMonth() + "-" + time.getDate() + "-" + (time.getHours() < 10 ? "0" + time.getHours() : time.getHours()) + "-" + (time.getMinutes() < 10 ? "0" + time.getMinutes() : time.getMinutes()) + "-" + (time.getSeconds() < 10 ? "0" + time.getSeconds() : time.getSeconds());
			var dateFuture1 = new Date(time.getFullYear(), time.getMonth(), time.getDate(), (time.getHours() < 10 ? "0" + time.getHours() : time.getHours()), (time.getMinutes() < 10 ? "0" + time.getMinutes() : time.getMinutes()), (time.getSeconds() < 10 ? "0" + time.getSeconds() : time.getSeconds()));
			_zimlottoNxtDrawTime = ("" + time).substring(0, ("" + time).lastIndexOf(":") + 3);
		}
		if (i % 2 == 0) {
			rowcolor = "#FFF1D2";
		} else {
			rowcolor = "#FFDF96";
		}
		nxtDrwHTML = nxtDrwHTML + "<tr><td width=\"100%\"><input type=\"hidden\" value=\"" + data + "\" id=\"" + i + "timer\"></td></tr><tr bgcolor=\"" + rowcolor + "\" style=\"height: 20px\"><td width=\"100%\"align=\"center\" class=\"borderbtmzimlotto\" colspan=\"2\"><div id=\"" + i + "blink\">" + newTime + "</div></td><td width=\"44%\"align=\"center\"><p><div id=\"nxtDrwD" + i + "\" style=\"display:none\"><img src=\"" + fortImgPath + "freezed.gif\"></div></p></td></tr>";
	}
	nxtDrwHTML = nxtDrwHTML + "</table></div></td></tr></table></div></td></tr></table>";
	return nxtDrwHTML;
}
function _zimlottoUpData(win, drwTime, drwStatus) {
	_zimlottoWin = win;
	_zimlottoDrawTime = drwTime;
	_zimlottoStatus = drwStatus;
}
function fillSelzimLto(val) {
	if (!zimlottoIsAllQP) {
		zimlottocurSel = "row" + val;
		_id.o("radio" + val).checked = true;
		zimlottoapplyCSS();
	}
}
function fillNumzimLto(val) {
	if (!_id.o("QP" + zimlottocurSel.replace("row", "")).checked && !zimlottoIsAllQP) {
		var objR = _id.o(zimlottocurSel);
		var clrChg = true;
		var elms = objR.getElementsByTagName("input");
		if (_id.o(val).className == "selected_zimlotto_numbersSelect") {
			for (var l = 1; l < elms.length - 1; l++) {
				if (elms[l].value == val) {
					elms[l].value = "";
					break;
				}
			}
		} else {
			for (var l = 1; l < elms.length - 1; l++) {
				if (elms[l].value == "") {
					elms[l].value = (val < 10 ? "0" + val : val);
					break;
				}
			}
			for (var l = 1; l < elms.length - 1; l++) {
				if (elms[l].value == "") {
					elms[l].focus();
					break;
				}
			}
		}
		zimlottoapplyCSS();
	}
}
function quickPkzimLto(val) {
	var isChq = false;
	zimlottocurSel = "row" + val;
	_id.o("radio" + val).checked = true;
	if (_id.o("QP" + val).checked) {
		isChq = true;
	}
	var obj = _id.o("row" + val);
	var elms = obj.getElementsByTagName("input");
	for (var l = 1; l < elms.length - 1; l++) {
		elms[l].disabled = isChq;
		elms[l].value = "";
	}
	_zimlottoCalAmt();
}
function _zimlottoCalAmt() {
	var noOfDrw = _id.v("noOfDraws");
	var noOfPanel = 0;
	var obj, elms;
	var isPanel = true;
	for (var i = 1; i < 9; i++) {
		isPanel = true;
		if (_id.o("QP" + i).checked) {
			noOfPanel++;
		} else {
			obj = _id.o("row" + i);
			elms = obj.getElementsByTagName("input");
			for (var l = 1; l < elms.length - 1; l++) {
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
	_id.o("totAmtLto").value = noOfDrw * noOfPanel * _zimlottoPrice;
	if (noOfPanel != 0 || _id.o("qpChkAll").checked == false) {
		_id.o("tkts").value = noOfPanel;
	} else {
		_id.o("tkts").value = "";
	}
}
function fillNoOfPanels(id) {
	var noOfpanels = _id.o(id).value;
	_id.i("lottoRowDiv", lottoRow(noOfpanels));
}
function keyPresszimLto(obj, val,e) {
	var keyCode = (window.event) ? event.keyCode : e.which;
	if ((keyCode >= 48 && keyCode < 58) || keyCode == 45 || keyCode == 27 || keyCode == 13 || keyCode == 40 || keyCode == 38 || keyCode == 46 || keyCode == 8 || (keyCode >= 96 && keyCode < 106 || keyCode == 9)) {
		if (keyCode == 46 || keyCode == 8 || keyCode == 9) {
			if (keyCode == 9) {
				fillSelzimLto(val);
			}
			return true;
		}
		if (obj.value.length > 1) {
			if (obj.value == 0 || obj.value > 42 || isNaN(obj.value)) {
				obj.value = "";
				obj.focus();
			} else {
				var objR = _id.o(zimlottocurSel);
				var clrChg = true;
				var elms = objR.getElementsByTagName("input");
				for (var l = 1; l < elms.length - 1; l++) {
					clrChg = true;
					if ((elms[l].value == obj.value) && (elms[l] != obj)) {
						obj.value = "";
						obj.focus();
						clrChg = false;
						break;
					}
				}
				if (clrChg) {
					var selNxtRow=false;//alert(zimlottocurSel );
					for (var l = 1; l < elms.length - 1; l++) {
						if (elms[l].value == "") {
							selNxtRow=false;
							elms[l].focus();
							break;
						}else{
							selNxtRow=true;
						}
					}//alert(selNxtRow);
					if(selNxtRow){
						var nxtRow=parseInt(zimlottocurSel.substring(zimlottocurSel.length-1),10)+1;
						if(nxtRow<=8){
						var nxtRowStr=zimlottocurSel.substring(0,zimlottocurSel.length-1)+nxtRow;
						//alert(nxtRowStr);
						_id.o(nxtRowStr).getElementsByTagName("input")[1].focus();
						fillSelzimLto(nxtRow);
						}
					}
				}
			}
			zimlottoapplyCSS();
		}
	} else {
		return false;
	}
	return true;
}
function blurFnzimLto(obj) {
	if ((obj.value.length < 2) || isNaN(obj.value) || obj.value == 0) {
		obj.value = "";
	}
	zimlottoapplyCSS();
}
function zimlottoapplyCSS() {
	for (var i = 1; i <= 42; i++) {
		_id.o(i).className = "selected_zimlotto_numbers";
		_id.o(i).style.background = "url(" + projectName + "/LMSImages/images/zimlotto_shade_white.gif) center no-repeat";
		_id.o(i).style.color = "#DD8C17";
	}
	var obj = _id.o(zimlottocurSel);
	var clrChg = true;
	var elms = obj.getElementsByTagName("input");
	for (var l = 1; l < elms.length - 1; l++) {
		for (var j = 0; j < elms.length - 1; j++) {
			if ((elms[l].value == elms[j].value) && (elms[l] != elms[j])) {
				elms[j].value = "";
			}
		}
	}
	var count = 0;
	for (var l = 1; l < elms.length - 1; l++) {
		if (elms[l].value != "") {
			if (count < 6) {
				_id.o(parseInt(elms[l].value, 10)).className = "selected_zimlotto_numbersSelect";
				_id.o(parseInt(elms[l].value, 10)).style.background = "url(" + projectName + "/LMSImages/images/zimlotto_shade_over.gif) center no-repeat";
				_id.o(parseInt(elms[l].value, 10)).style.color = "#FFFFFF";
				count++;
			}
		}
	}
	var obj1 = _id.o("lottoRow");
	var txtelms = obj1.getElementsByTagName("input");
	if (zimlottoIsAllQP) {
		for (var i = 1; i <= 42; i++) {
			_id.o(i).className = "selected_zimlotto_numbersDisabled";
			_id.o(i).style.background = "url(" + projectName + "/LMSImages/images/zimlotto_shade_disable.gif) center no-repeat";
			_id.o(i).style.color = "#FFFFFF";
		}
		for (var l = 0; l < txtelms.length; l++) {
			txtelms[l].disabled = true;
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
	_zimlottoCalAmt();
}
function zimlottoresetFrm() {
	document.forms[0].reset();
	for (var i = 1; i <= 42; i++) {
		_id.o(i).className = "selected_zimlotto_numbers";
	}
	var elms = document.forms[0].getElementsByTagName("input");
	for (var l = 0; l < elms.length; l++) {
		elms[l].disabled = false;
	}
	zimlottocurSel = "row1";
}
var gameChildW = null;
function zimlottoGameInfo() {
	if (gameChildW) {
	} else {
		gameChildW = window.open("zimLotto/gameInfo.jsp", "NewWin", "width=650,height=550,toolbar=0,resizable=0,scrollbars=1,alwaysRaised=0,location=0,menubar=0");
		gameChildW.close();
	}
	gameChildW = window.open("zimLotto/gameInfo.jsp", "NewWin", "width=650,height=550,toolbar=0,resizable=0,scrollbars=1,alwaysRaised=0,location=0,menubar=0");
}
function buyTicketzimlotto(id) {
	
	var compData = "";
	for (var i = 1; i < 9; i++) {
		var inputData = "";
		var obj = _id.o("row" + i);
		var elms = obj.getElementsByTagName("input");
		if (!_id.o("QP" + i).checked) {
			for (var l = 1; l < elms.length - 1; l++) {
				if (elms[l].value != "") {
					inputData = inputData + elms[l].value + ",";
				}
			}
			if (inputData != "" && inputData.split(",").length < 1 + parseInt(6)) {
				alert("Please fill incomplete entries");
				return false;
			}
			if (inputData != "" && checkNumber(inputData.substring(0, inputData.length - 1), 1, 42)) {
				alert("Please fill Number only between 1 to 42");
				return false;
			}
			if (inputData != "" && checkDuplicate(inputData.substring(0, inputData.length - 1))) {
				alert("Please fill different Number");
				return false;
			}
			if (inputData != "") {
				inputData = arrSort(inputData.substring(0, inputData.length - 1)) + "Nxt";
			}
		} else {
			inputData = "QP" + "Nxt";
		}
		compData = compData + inputData;
	}
	compData = compData.substring(0, compData.length - 3);
	_id.o("pickedNumbers").value = compData;
	if (compData.length < 1) {
		alert("Please fill incomplete entries");
		return false;
	}
	var amount = _id.o("totAmtLto").value;
	_id.o(id).disabled=true;
	gameBuyAjaxReq("zimLottoBuy.action?pickedNumbers=" + compData + "&noOfDraws=" + _id.n("noOfDraws")[0].value + "&totalPurchaseAmt=" + amount);
	zimlottoclearAllBoxes();
	return true;
}

function scriptBuy(){

for(var i=0;i<1000;i++){
gameBuyAjaxReq("zimLottoBuy.action?pickedNumbers=QPNxtQP&noOfDraws=1&totalPurchaseAmt=2");
}
}

var line = "<tr><td bgcolor=\"#F8AD40\"><table border=\"0\" bordercolor=\"blue\" width=\"97%\"><tr><td align=\"left\" width=\"25%\"><input type=\"hidden\" value=\"0\" id=\"counterId\"><input type=\"hidden\" id=\"dateFuture\"><input type=\"hidden\" id=\"freezeTimeId\" value=\"" + _zimlottofreezeTime + "\"><span class=\"style1\">Next Draw :</span><div id=\"latestDrawTime\" style=\"display:none\"></div></td><td width=\"30%\" align=\"left\"><div id=\"nxtDrawTime\" class=\"zimlottostyle2\"></div></td><td align=\"right\" width=\"25%\"><div id=\"timeLeft\"><span class=\"style1\"> Time Left :</div></span></td> <td align=\"left\" width=\"15%\"><div id=\"curDrwTime\" class=\"zimlottostyle2\"></div> </td><!--<td rowspan=\"2\" width=\"10%\"><img src=\"" + fortImgPath + "que.gif\" style=\"cursor:hand;\" onClick=\"gameInfo()\" alt=\"Game Info\" /> </td>--></tr> <tr bgcolor=\"#F8AD40\"><td align=\"left\"> <span class=\"style1\">No. of Draws :</span> </td> <td > <select\tname=\"noOfDraws\" id=\"noOfDraws\"\tclass=\"fortune_option\" align=\"left\"\twidth=\"20%\"\tonchange=\"_zimlottoCalAmt()\">" + getActiveDrawOptionList("zimlotto") + "</select> </td> <td colspan=\"1\" align=\"left\" valign=\"top\"> &nbsp; </td><td align=\"left\"> <span class=\"zimlottonum\">" + currSymbol+" " + _zimlottoPrice+ "/-</span></td>  </table></td></tr>";
var _zimlottoNxtDrwTbl = _zimlottoFillNxtDrw();
var _zimlottoHtmlTbl = "<table width=\"100%\" height=\"100%\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\" bordercolor=\"red\" bgcolor=\"#FFF1D2\">" + line + "<tr><td align=\"center\" valign=\"top\"><table width=\"100%\" cellspacing=\"0\" cellpadding=\"0\" border=\"0\"><tr><td width=\"45%\" valign=\"top\"><div style=\"padding-top:10px;\"><table border=\"0\" bordercolor=\"red\" width=\"97%\" align=\"center\"cellpadding=\"0\"cellspacing=\"0\"><tr><td class=\"style10\" align=\"left\"><div id='jackpotAmt' align=\"center\" valign=\"middle\" style=\"height:18px;background-color:#F8AD40;width:180px;float:left;padding-top:2px\"></div></td></tr><tr><td valign=\"middle\" style=\"border:1px #F8AD40 solid;background-color:#FFDF96\">" + zimlottoNmSel(42) + "</td></tr></table></div></td><td valign=\"top\" wdith=\"35%\"><div style=\"border:0px solid black; padding-top:10px;\"><table border=\"0\" bordercolor=\"red\" width=\"96%\" cellpadding=\"0\" cellspacing=\"0\" align=\"center\" style=\"margin-right:10px\"><tr><td align=\"right\" class=\"style10\" style=\"cursor:hand;\" onclick=\"zimlottoclearAllBoxes()\" ><div align=\"center\" valign=\"middle\" style=\"height:18px;background-color:#F8AD40;width:100px;float:right;padding-top:2px\">&nbsp;&nbsp;&nbsp;Clear All&nbsp;&nbsp;&nbsp;</div></td></tr><tr><td valign=\"middle\" style=\"border:1px #F8AD40 solid;background-color:#FFDF96\"><table width=\"100%\" border=\"0\" align=\"center\" cellspacing=\"0\" cellpadding=\"0\"><tr><td>" + zimlottoRow("8") + "</td></tr><tr><td bgcolor=\"#F8AD40\"><table width=\"95%\" border=\"0\" style=\"margin:6px 0px 5px 0px;\"><tr><td align=\"center\" class=\"style5\" rowspan=\"2\"><div style=\"border:1px #FFFFFF solid; width: 50px; height: 40px; float: left;\">QP<br /><input type=\"checkbox\" id=\"qpChkAll\" onclick=\"zimlottoChkQP(this.id)\" style=\"border:1px #F8AD40 solid\" /></div></td><td class=\"zimlottotxtStyle\" align=\"left\">Total Qty:</td><td><input type=\"hidden\" value=\"\" id=\"pickedNumbers\" name=\"pickedNumbers\" /><input type=\"text\" class=\"zimlottotextbox\" id=\"tkts\" maxlength=\"1\" readonly=\"true\" value=\"0\" onkeyup=\"return zimlottofillNoOfQp(this,event)\" onkeydown=\"return zimlottofillNoOfQp(this,event)\" onblur=\"zimlottoChkNoOfQP()\"/></td><td rowspan=\"2\"><a href=\"#\"><img src=\"" + fortImgPath + "zimlotto_buy.gif\" id='buy' onclick=\"return buyTicketzimlotto(this.id)\" alt=\"Buy\" style=\"margin-right: 4px;\" /></a></td></tr><tr><td class=\"zimlottotxtStyle\" align=\"left\">Total Amount:</td><td><input type=\"text\" id=\"totAmtLto\" name=\"totalPurchaseAmt\" value=\"0\" readonly=\"true\" class=\"zimlottotextbox\" /></td></tr> </table></td></tr></table></td></tr></table></div></td></tr></table></tr><tr><td style=\"line-height:10px\">&nbsp;</td></tr></table>";
var _zimlottoHTML = "<div id=\"strip\">" + drwMenu + "</div><form style=\"background-color:#FFFFFF;border: 1px solid #393939;\"><table  border=\"0\" width=\"100%\" height=\"100%\" cellspacing=\"0\" cellpadding=\"0\" align=\"center\" bgcolor=\"#FFFFFF\" bordercolor=\"red\"><tr><td width=\"10%\"  valign=\"top\" align=\"center\" onmousedown=\"return disClick(this)\"><input type=\"hidden\" id=\"drawType\"><div id=\"winningDispDiv\" style=\"border:1px #F8AD40 solid;background-color:#FFFFFF;margin-left:5px;margin-top:3px\"></div><div id=\"nxtDrawDiv\" style=\"border:1px #F8AD40 solid;background-color:#FFFFFF;margin-left:5px;margin-top:5px\"></div></td><td width=\"90%\" valign=\"top\"><div id=\"zimlottomiddle\" style=\"border:1px #F8AD40 solid;background-color:#FFDF96;width:98%;margin-left:5px;margin-top:3px;margin-bottom:1px\">" + _zimlottoHtmlTbl + "</div></td></tr></table></td></tr></table></form>";

