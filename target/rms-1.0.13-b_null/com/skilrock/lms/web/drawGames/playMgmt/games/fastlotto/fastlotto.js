
var _fastlottoWinArr = new Array();
var curDrwTimelotto = "";
var fastlottocurSel = "row1";
var fastlottoIsAllQP = false;
var _fastlottoPrice = parseInt(gameDispName["fastlotto"].unitPrice.split("=")[1], 10);
function setDefaultfastlotto() {
	curDrwTimefortune = "";
	fastlottocurSel = "row1";
	fastlottoIsAllQP = false;
}
/*
function fastlottogameInfo(){

var url="fastLottoInfo.action";
_id.o('topId').style.display="block";
runAccordion('5');

 _id.i('info5Div','<iframe src="'+url+'" id="tktFrame" style="width:100%;height:100%" frameborder="0"></iframe>' );
_id.o('info5Div').style.marginLeft="200px";
_id.o('info5Div').style.marginTop="60px";
}
*/
function _fastlottoWinAppend() {
	if (!_fastlottoWin.match("Result Awaited")) {
		_fastlottoWin = "Result Awaited=\"\" Nxt" + _fastlottoWin;
	}
}
function _fastlottoLatestDrawTime() {
	return _fastlottoStatus.split("=")[1];
}
function _fastlottoFillWinTbl() {
	var _fastlottoWinHt = "<table width=\"100%\" border=\"0\" bordercolor=\"red\" cellspacing=\"0\" cellpadding=\"0\" align=\"center\" bgcolor=\"#C3C3E5\"><tr><td colspan=\"2\" align=\"left\"><div valign=\"middle\" style=\"height:22px;background-color:#513E74;padding-top:3px;font-size:16px;font-family:Arial;font-weight:bold;color:#FFFFFF\">&nbsp;Win Result&nbsp;</div></td></tr><tr><td valign=\"top\" align=\"center\" height=\"100\">";
	var winArr = _fastlottoWin.split("Nxt");
	var _fastlottoWinHTML = "<table width=\"100%\" border=\"0\" style=\"margin-top:3px\"><tr><td><div style=\"" + scrollColor("#8270A5", "#513E74", "184") + "\">";
	for (var i = 0; i <= winArr.length - 2; i++) {
		if (!winArr[i].match("Result")) {
			var time = new Date(parseInt(winArr[i].split("=")[0].replace(" ", "")));
			var newTime = week[time.getDay()] + " " + time.getDate() + " " + month[time.getMonth()] + "<br/>" + (time.getHours() < 10 ? "0" + time.getHours() : time.getHours()) + ":" + (time.getMinutes() < 10 ? "0" + time.getMinutes() : time.getMinutes()) + ":" + (time.getSeconds() < 10 ? "0" + time.getSeconds() : time.getSeconds());
			var htm = "<table width=\"90%\" border=\"0\" bordercolor=\"green\" id=\"result" + i + "\" cellspacing=\"2\" cellpadding=\"10\" class=\"fastlotto_winningResult_table\" ><tr><td class=\"fastlottowinningResultTime\" colspan=\"8\" height=\"53\">" + newTime + "</td></tr><tr>";
			var num = (winArr[i].split("=")[1]).split(",");
			for (var j = 0; j < num.length; j++) {
				var temp = (num[j] < 10 ? "0" + num[j] : num[j]);
				htm = htm + "<td width=\"10%\" class=\"selected_win_fastlotto_numbers\" align=\"center\">" + temp + "</td>";
			}
			htm = htm + "</tr></table>";
			_fastlottoWinHTML = _fastlottoWinHTML + htm;
		} else {
			var htm = "<table width=\"90%\" bordercolor=\"blue\" border=\"0\" cellspacing=\"2\" cellpadding=\"10\" id=\"result" + i + "\"  class=\"fastlotto_winningResult_table\"><tr><td class=\"fastlottowinningResultTime\" colspan=\"8\" height=\"53\" >Result Awaited</td></tr><tr>";
			htm = htm + "<td width=\"10%\" class=\"selected_win_fastlotto_numbers\" align=\"center\"><div style=\"text-align:center\" id=\"winAwaitedDiv\" style=\"color:#513E74\"></div></td>";
			htm = htm + "</tr></table>";
			_fastlottoWinHTML = _fastlottoWinHTML + htm;
		}
	}
	_fastlottoWinHTML = _fastlottoWinHTML + "</div></td></tr></table>";
	return _fastlottoWinHt + _fastlottoWinHTML + "</td></tr></table>";
}
function _fastlottoWinAjx() {
	var url = "fetchWinFastLotto.action?frmDate=" + _fastlottoWinArr[_fastlottoWinArr.length - 1];
	_fastlottoWin = _fastlottoWin + (winAjaxReq(url)).data;
	_fastlottoFillWinTbl();
}
function fastlottoNmSel(lstNo) {
	var htm = "<div id=\"\"><table width=\"99%\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\" bgcolor=\"#8270A5\" style=\"margin-top:5px;margin-bottom:8px\"><tr>";
	for (var i = 1; i <= lstNo; i++) {
		if (i % 5 == 0) {
			htm = htm + "<td style=\"cursor:pointer;background:url(" + projectName + "/LMSImages/images/fastlotto_shade_white.gif) center no-repeat\" class=\"selected_fastlotto_numbers\" onmouseout=\"fastlottoonmouseouteffect(this.id)\" onmouseover=\"fastlottoonmouseovereffect(this.id)\"  onclick=\"fillNumfastLto(" + i + ")\"  id=\"" + i + "\" >" + (i < 10 ? "0" + i : i) + "</td></tr>";
		} else {
			htm = htm + "<td class=\"selected_fastlotto_numbers\"  style=\"cursor:pointer;background:url(" + projectName + "/LMSImages/images/fastlotto_shade_white.gif) center no-repeat\" onmouseout=\"fastlottoonmouseouteffect(this.id)\" onmouseover=\"fastlottoonmouseovereffect(this.id)\" onclick=\"fillNumfastLto(" + i + ")\"  id=\"" + i + "\"  >" + (i < 10 ? "0" + i : i) + "</td>";
		}
	}
	htm = htm + "</tr></table></div>";
	return htm;
}
function fastlottoonmouseovereffect(id) {
	var count = 0;
	var isFilled = false;
	for (var i = 1; i <= 35; i++) {
		if (_id.o(i).className == "selected_fastlotto_numbersSelect") {
			count++;
		}
		if (count > 4) {
			isFilled = true;
			break;
		}
	}
	if (!_id.o("QP" + fastlottocurSel.replace("row", "")).checked && !isFilled && !fastlottoIsAllQP) {
		if (_id.o(id).className != "selected_fastlotto_numbersSelect") {
			_id.o(id).style.background = "url(" + projectName + "/LMSImages/images/fastlotto_shade_over.gif) center no-repeat";
			_id.o(id).style.color = "#FFFFFF";
			_id.o(id).className = "selected_fastlotto_numbersSelectOMO";
		}
	}
}
function fastlottoonmouseouteffect(id) {
	if (!_id.o("QP" + fastlottocurSel.replace("row", "")).checked && !fastlottoIsAllQP) {
		if (_id.o(id).className == "selected_fastlotto_numbersSelectOMO") {
			_id.o(id).style.background = "url(" + projectName + "/LMSImages/images/fastlotto_shade_white.gif) center no-repeat";
			_id.o(id).style.color = "#443266";
			_id.o(id).className = "selected_fastlotto_numbers";
		} else {
			if (_id.o(id).className == "selected_fastlotto_numbersSelect") {
				_id.o(id).style.background = "url(" + projectName + "/LMSImages/images/fastlotto_shade_over.gif) center no-repeat";
				_id.o(id).style.color = "#FFFFFF";
			} else {
				if (_id.o(id).className == "selected_fastlotto_numbers") {
					_id.o(id).style.background = "url(" + projectName + "/LMSImages/images/fastlotto_shade_white.gif) center no-repeat";
					_id.o(id).style.color = "#443266";
				}
			}
		}
	}
}
function fastlottoRow(noOfpanels) {
	var htm = "<table width=\"100%\" id=\"fastlottoRow\" border=\"0\" cellspacing=\"2\" cellpadding=\"0\" style=\"margin-top:3px\"><tr> <td width=\"8%\" align=\"center\" valign=\"middle\">&nbsp;</td><td width=\"10%\" align=\"center\" valign=\"middle\" >&nbsp;</td>";
	for (var indx = 1; indx < 6; indx++) {
		htm = htm + "<td width=\"12%\" align=\"center\" valign=\"middle\" style=\"color:#FFFFFF;text-align: center;font-weight:bold;font-size:16px\">" + indx + "</td>";
	}
	htm = htm + "<td width=\"12%\" align=\"center\" valign=\"middle\" style=\"color:#FFFFFF;text-align: center;font-weight:bold;font-size:16px\">QP</td> </tr> <tr>";
	for (var i = 1; i < parseInt(noOfpanels) + 1; i++) {
		htm = htm + "<tr onclick=\"fillSelfastLto(" + i + ")\" id=\"row" + i + "\"><td height=\"29\" style=\"color:#FFFFFF;text-align: center;font-weight:bold;font-size:16px\">" + i + "</td><td><input type=\"radio\" name=\"lottoRadio\" id=\"radio" + i + "\" style=\"border:1px #8270A5 solid\" onclick=\"fillSelfastLto(" + i + ")\" " + (i == 1 ? "checked" : "") + "/></td>";
		for (var j = 0; j < 5; j++) {
			htm = htm + "<td align=\"center\" valign=\"middle\" ><input type=\"text\" class=\"selected_fastlotto\" maxlength=\"2\" onkeydown=\"return keyPressfastLto(this," + i + ",event)\" onkeyup=\"return keyPressfastLto(this," + i + ",event)\" onblur=\"blurFnfastLto(this)\" onmousedown=\"return disClick(this)\"></td>";
		}
		htm = htm + " <td align=\"center\" valign=\"middle\"><input type=\"checkbox\" style=\"border:1px #8270A5 solid\" value=\"2\" name=\"isQuickPick\" id=\"QP" + i + "\" onclick=\"quickPkfastLto(" + i + ");\" /><img src=\"" + fortImgPath + "clear.gif\" onclick=\"fastlottoclearBoxes(" + i + ")\" style=\"cursor:hand\"></td></tr>";
	}
	return htm + "</table>";
}
function fastlottoclearBoxes(val) {
	_id.resetInpTypes("row" + val, "selected_fastlotto", false);
	/*
var obj = _id.o('row'+val);
var elms = obj.getElementsByTagName("input");
	for(var l=1;l<elms.length-1;l++){
elms[l].value="";
	}
	*/
	_fastlottoCalAmt();
}
function fastlottoclearAllBoxes() {
	_id.resetInpTypes("fastlottoRow", "selected_fastlotto", false);
	_id.o("noOfDraws").value = 1;
	_id.o("tkts").readOnly = true;
	fastlottocurSel = "row1";
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

	for(var i=1;i<=35;i++){	
		_id.o(i).className="selected_fastlotto_numbers";
		_id.o(i).style.background='url('+projectName+'/LMSImages/images/fastlotto_shade_white.gif) center no-repeat';
		_id.o(i).style.color='#443266';
	}*/
	fastlottoIsAllQP = false;
	_id.o("tkts").readOnly = true;
	_id.o("tkts").value = 0;
	fastlottoapplyCSS();
	_id.o("noOfDrwId").value = 1;
	_id.o("qpChkAll").checked = false;
	_fastlottoCalAmt();
}
function _fastlottoFillNxtDrw() {
	var nxtDrwHTML = "<table width=\"100%\" cellspacing=\"0\" cellpadding=\"0\"  border=\"0\" bordercolor=\"red\"><tr><td colspan=\"2\" align=\"left\" valign=\"top\"><div valign=\"middle\" style=\"height:22px;width:180px;background-color:#513E74;padding-top:3px;font-size:16px;font-family:Arial;font-weight:bold;color:#FFFFFF\">&nbsp;Next Draw&nbsp;</div></td></tr><tr><td align=\"center\" valign=\"top\"><div><table width=\"100%\" bordercolor=\"red\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\" ><tr><td width=\"100%\" align=\"center\" valign=\"top\" colspan=\"2\"><div style=\"" + scrollColor("#8270A5", "#513E74", "175") + "\"><table width=\"90%\" bordercolor=\"red\"  border=\"0\" cellspacing=\"0\" cellpadding=\"0\" class=\"borderbtmfastlotto\">";
	var nxtDrwArr = _fastlottoDrawTime.split(",");
	for (var i = 0, l = nxtDrwArr.length; i < l; i++) {
		var time = new Date(parseInt(nxtDrwArr[i].replace(" ", "")));
		var newTime = week[time.getDay()] + " " + time.getDate() + " " + month[time.getMonth()] + " " + (time.getHours() < 10 ? "0" + time.getHours() : time.getHours()) + ":" + (time.getMinutes() < 10 ? "0" + time.getMinutes() : time.getMinutes());
		var hiddenTime = "<input type='text'  id='timer" + i + "'>";
		var rowcolor;
		var data = "";
		if (i == 0) {
			data = time.getFullYear() + "-" + time.getMonth() + "-" + time.getDate() + "-" + (time.getHours() < 10 ? "0" + time.getHours() : time.getHours()) + "-" + (time.getMinutes() < 10 ? "0" + time.getMinutes() : time.getMinutes()) + "-" + (time.getSeconds() < 10 ? "0" + time.getSeconds() : time.getSeconds());
			var dateFuture1 = new Date(time.getFullYear(), time.getMonth(), time.getDate(), (time.getHours() < 10 ? "0" + time.getHours() : time.getHours()), (time.getMinutes() < 10 ? "0" + time.getMinutes() : time.getMinutes()), (time.getSeconds() < 10 ? "0" + time.getSeconds() : time.getSeconds()));
			_fastlottoNxtDrawTime = ("" + time).substring(0, ("" + time).lastIndexOf(":") + 3);
		}
		if (i % 2 == 0) {
			rowcolor = "#C3C3E5";
		} else {
			rowcolor = "#8270A5";
		}
		nxtDrwHTML = nxtDrwHTML + "<tr><td width=\"100%\"><input type=\"hidden\" value=\"" + data + "\" id=\"" + i + "timer\"></td></tr><tr bgcolor=\"" + rowcolor + "\" style=\"height: 20px\"><td width=\"100%\" align=\"center\" class=\"borderbtmfastlotto\" colspan=\"2\"><div id=\"" + i + "blink\">" + newTime + "</div></td><td width=\"44%\" align=\"center\"><p><div id=\"nxtDrwD" + i + "\" style=\"display:none\"><img src=\"" + fortImgPath + "freezed.gif\"></div></p></td></tr>";
	}
	nxtDrwHTML = nxtDrwHTML + "</table></div></td></tr></table></td></tr></table>";
	return nxtDrwHTML;
}
function _fastlottoUpData(win, drwTime, drwStatus) {
	_fastlottoWin = win;
	_fastlottoDrawTime = drwTime;
	_fastlottoStatus = drwStatus;
}
function fillSelfastLto(val) {
	if (!fastlottoIsAllQP) {
		fastlottocurSel = "row" + val;
		_id.o("radio" + val).checked = true;
		fastlottoapplyCSS();
	}
}
function fillAllBoxes() {
	var elms = _id.o("lottoRow").getElementsByTagName("input");
	var rdnm = 1;
	var tmpindx = 0;
	var bxnm = 1;
	var tmparr = new Array();
	for (var l = 1; l < elms.length - 1; l++) {
		var again = false;
		if (elms[l].className == "selected_fastlotto") {
			var num = Math.floor(Math.random() * (34)) + 1;
			num = num < 10 ? "0" + num : num;
			tmparr[tmpindx] = num;
			if (notUnique(tmparr)) {
				--tmpindx;
				l--;
				again = true;
			}
			if (!again) {
				elms[l].value = num;
				if (bxnm % 6 == 0) {
					fillSelLto(rdnm);
					rdnm++;
					tmpindx = 0;
				}
				bxnm++;
			}
			tmpindx++;
		}
	}
	for (var m = 1; m < elms.length - 1; m++) {
		if (elms[m].className == "selected_fastlotto") {
			if (elms[m].disabled == true) {
				elms[m].value = "";
			}
		}
	}
	_fastlottoCalAmt();
}
function notUnique(a) {
	var r = new Array();
	var duplicate = false;
	for (var i = 0, n = a.length; i < n; i++) {
		for (var x = i + 1; x < n; x++) {
			if (a[x] == a[i]) {
				duplicate = true;
				break;
			}
		}
	}
	return duplicate;
}
function fillNumfastLto(val) {
	if (!_id.o("QP" + fastlottocurSel.replace("row", "")).checked && !fastlottoIsAllQP) {
		var objR = _id.o(fastlottocurSel);
		var clrChg = true;
		var elms = objR.getElementsByTagName("input");
		if (_id.o(val).className == "selected_fastlotto_numbersSelect") {
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
		fastlottoapplyCSS();
	}
}
function quickPkfastLto(val) {
	var isChq = false;
	fastlottocurSel = "row" + val;
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
	_fastlottoCalAmt();
}
function isAllChecked() {
	var count = 0;
	for (var i = 0; i < _id.n("isQuickPick").length; i++) {
		var e = _id.n("isQuickPick")[i];
		if (e.checked == true) {
			count++;
		}
	}
	if (count == _id.n("isQuickPick").length) {
		return true;
	}
	return false;
}
function _fastlottoCalAmt() {
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
	_id.o("totAmtLto").value = noOfDrw * noOfPanel * _fastlottoPrice;
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
function keyPressfastLto(obj, val, e) {
	var keyCode = (window.event) ? event.keyCode : e.which;
	if ((keyCode >= 48 && keyCode < 58) || keyCode == 45 || keyCode == 27 || keyCode == 13 || keyCode == 40 || keyCode == 38 || keyCode == 46 || keyCode == 8 || (keyCode >= 96 && keyCode < 106 || keyCode == 9)) {
		if (keyCode == 46 || keyCode == 8 || keyCode == 9) {
			if (keyCode == 9) {
				fillSelfastLto(val);
			}
			return true;
		}
		if (obj.value.length > 1) {
			if (obj.value == 0 || obj.value > 35 || isNaN(obj.value)) {
				obj.value = "";
				obj.focus();
			} else {
				var objR = _id.o(fastlottocurSel);
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
					for (var l = 1; l < elms.length - 1; l++) {
						if (elms[l].value == "") {
							elms[l].focus();
							break;
						}
					}
				}
			}
			fastlottoapplyCSS();
		}
	} else {
		return false;
	}
	return true;
}
function blurFnfastLto(obj) {
	if (isNaN(obj.value) || obj.value <= 0 || obj.value > 35) {
		obj.value = "";
	} else {
		if (obj.value.length == 1) {
			obj.value = "0" + obj.value;
		}
	}
	fastlottoapplyCSS();
}
function fastlottoapplyCSS() {
	for (var i = 1; i <= 35; i++) {
		_id.o(i).className = "selected_fastlotto_numbers";
		_id.o(i).style.background = "url(" + projectName + "/LMSImages/images/fastlotto_shade_white.gif) center no-repeat";
		_id.o(i).style.color = "#443266";
	}
	var obj = _id.o(fastlottocurSel);
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
			if (count < 5) {
				_id.o(parseInt(elms[l].value, 10)).className = "selected_fastlotto_numbersSelect";
				_id.o(parseInt(elms[l].value, 10)).style.background = "url(" + projectName + "/LMSImages/images/fastlotto_shade_over.gif) center no-repeat";
				_id.o(parseInt(elms[l].value, 10)).style.color = "#FFFFFF";
				count++;
			}
		}
	}
	//alert(_id.o('fastlottoRow'));	
	var obj1 = _id.o("fastlottoRow");
	var txtelms = obj1.getElementsByTagName("input");
	if (fastlottoIsAllQP) {
		for (var i = 1; i <= 35; i++) {
			_id.o(i).className = "selected_fastlotto_numbersDisabled";
			_id.o(i).style.background = "url(" + projectName + "/LMSImages/images/fastlotto_shade_disable.gif) center no-repeat";
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
	_fastlottoCalAmt();
}
function fastlottochkAll(id) {
	if (_id.o(id).checked == true) {
		for (var i = 0; i < _id.n("isQuickPick").length; i++) {
			var e = _id.n("isQuickPick")[i];
			e.checked = true;
			quickPkfastLto(i + 1);
		}
	} else {
		for (var i = 0; i < _id.n("isQuickPick").length; i++) {
			var e = _id.n("isQuickPick")[i];
			e.checked = false;
			quickPkfastLto(i + 1);
		}
	}
	_id.o("radio1").checked = true;
	fastlottocurSel = "row1";
	fastlottoapplyCSS();
}
function fastlottoChkNoOfQP() {
	var val = parseInt(_id.o("tkts").value);
	if (isNaN(val)) {
		val = 0;
	}
	for (var i = 0; i < val; i++) {
		var e = _id.n("isQuickPick")[i];
		e.checked = true;
		quickPkfastLto(i + 1);
	}
	for (var i = parseInt(val); i < _id.n("isQuickPick").length; i++) {
		var e = _id.n("isQuickPick")[i];
		e.checked = false;
		quickPkfastLto(i + 1);
	}
	var rowNo = (parseInt(val) + 1 > 8) ? 1 : parseInt(val) + 1;
	_id.o("radio" + rowNo).checked = true;
	fastlottocurSel = "row" + rowNo;
	fastlottoapplyCSS();
}
function fastlottoChkQP(id) {
	if (_id.o(id).checked == true) {
		fastlottoIsAllQP = true;
		_id.o("tkts").value = "";
		_id.o("tkts").readOnly = false;
		fastlottoapplyCSS();
		_id.o("tkts").focus();
	} else {
		fastlottoIsAllQP = false;
		for (var i = 0; i < _id.n("isQuickPick").length; i++) {
			var e = _id.n("isQuickPick")[i];
			e.checked = false;
			quickPkfastLto(i + 1);
		}
		_id.o("tkts").value = "0";
		_id.o("tkts").readOnly = true;
		_id.o("radio1").checked = true;
		fastlottocurSel = "row1";
		fastlottoapplyCSS();
	}
}
function fastlottofillNoOfQp(obj, e) {
	var keyCode = (window.event) ? event.keyCode : e.which;
	if ((keyCode >= 48 && keyCode < 58) || keyCode == 45 || keyCode == 27 || keyCode == 13 || keyCode == 40 || keyCode == 38 || keyCode == 46 || keyCode == 8 || (keyCode >= 96 && keyCode < 106 || keyCode == 9)) {
		if (keyCode == 46 || keyCode == 8 || keyCode == 9) {
			fastlottoChkNoOfQP();
			return true;
		}
		if (obj.value == 0 || obj.value > 8 || isNaN(obj.value)) {
			obj.value = "";
			obj.focus();
		}
	} else {
		return false;
	}
	fastlottoChkNoOfQP();
	return true;
}
function fastlottoresetFrm() {
	document.forms[0].reset();
	for (var i = 1; i <= 35; i++) {
		_id.o(i).className = "selected_fastlotto_numbers";
		_id.o(i).style.background = "url(" + projectName + "/LMSImages/images/fastlotto_shade_white.gif) center no-repeat";
		_id.o(i).style.color = "#443266";
	}
	var elms = document.forms[0].getElementsByTagName("input");
	for (var l = 0; l < elms.length; l++) {
		elms[l].disabled = false;
	}
	fastlottocurSel = "row1";
}
var gameChildW = null;
function fastlottoGameInfo() {
	if (gameChildW) {
	} else {
		gameChildW = window.open("fastLotto/gameInfo.jsp", "NewWin", "width=650,height=550,toolbar=0,resizable=0,scrollbars=1,alwaysRaised=0,location=0,menubar=0");
		gameChildW.close();
	}
	gameChildW = window.open("fastLotto/gameInfo.jsp", "NewWin", "width=650,height=550,toolbar=0,resizable=0,scrollbars=1,alwaysRaised=0,location=0,menubar=0");
}
function buyTicketfastlotto(id) {
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
			if (inputData != "" && inputData.split(",").length < 1 + parseInt(5)) {
				alert("Please fill incomplete entries");
				return false;
			}
			if (inputData != "" && checkNumber(inputData.substring(0, inputData.length - 1), 1, 35)) {
				alert("Please fill Number only between 1 to 35");
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
	gameBuyAjaxReq("fastLottoBuy.action?pickedNumbers=" + compData + "&noOfDraws=" + _id.o("noOfDraws").value + "&totalPurchaseAmt=" + amount);
	fastlottoclearAllBoxes();
	return true;
}
var line = "<tr><td bgcolor=\"#513E74\"><table border=\"0\" bordercolor=\"blue\" width=\"97%\"><tr><td align=\"left\" width=\"25%\"><input type=\"hidden\" value=\"0\" id=\"counterId\"><input type=\"hidden\" id=\"dateFuture\"><input type=\"hidden\" id=\"freezeTimeId\" value=\"" + _fastlottofreezeTime + "\"><span class=\"style1\">Next Draw :</span><div id=\"latestDrawTime\" style=\"display:none\"></div></td><td width=\"30%\" align=\"left\"><div id=\"nxtDrawTime\" class=\"fastlottostyle2\"></div></td><td align=\"right\" width=\"25%\"><div id=\"timeLeft\"><span class=\"style1\"> Time Left :</div></span></td> <td align=\"left\" width=\"15%\"><div id=\"curDrwTime\" class=\"fastlottostyle2\"></div> </td><td rowspan=\"2\" width=\"10%\"><img src=\"" + fortImgPath + "que.gif\" style=\"cursor:hand\" onClick=\"gameInfo()\" alt=\"Game Info\" /> </td></tr> <tr bgcolor=\"#513E74\"><td align=\"left\"> <span class=\"style1\">No. of Draws :</span> </td> <td > <select\tname=\"noOfDraws\" id=\"noOfDrwId\"\tclass=\"fortune_option\" align=\"left\"\twidth=\"20%\"\tonchange=\"_fastlottoCalAmt()\">" + getActiveDrawOptionList("fastlotto") + "</select> </td> <td colspan=\"1\" align=\"left\" valign=\"top\"> &nbsp; </td><td align=\"left\"> <span class=\"fastlottonum\">"+ currSymbol+" " + _fastlottoPrice+ "/-</span></td>  </table></td></tr>";
var _fastlottoNxtDrwTbl = _fastlottoFillNxtDrw();
var _fastlottoHtmlTbl = "<table width=\"100%\" height=\"100%\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\" bordercolor=\"red\" bgcolor=\"#C3C3E5\">" + line + "<tr><td align=\"center\" valign=\"top\"><table width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\"><tr><td width=\"50%\" valign=\"top\" ><div style=\"padding-top:10px;\"><table border=\"0\" bordercolor=\"red\" width=\"97%\" align=\"center\"cellpadding=\"0\"cellspacing=\"0\"><tr><td class=\"style10\" align=\"left\"><div align=\"center\" valign=\"middle\" style=\"height:18px;background-color:#513E74;width:180px;float:left;padding-top:2px\">&nbsp;Select Number Panel&nbsp;</div></td></tr><tr><td valign=\"middle\" style=\"border:1px #513E74 solid;background-color:#8270A5\">" + fastlottoNmSel(35) + "</td></tr></table></div></td><td valign=\"top\" wdith=\"35%\"><div style=\"border:0px solid black; padding-top:10px;\"><table border=\"0\" bordercolor=\"red\" width=\"96%\" cellpadding=\"0\" cellspacing=\"0\" align=\"center\" style=\"margin-right:10px\"><tr><td align=\"right\" class=\"style10\" style=\"cursor:hand;\" onclick=\"fastlottoclearAllBoxes()\"><div align=\"center\" valign=\"middle\" style=\"height:18px;background-color:#513E74;width:100px;float:right;padding-top:2px\">&nbsp;&nbsp;&nbsp;Clear All&nbsp;&nbsp;&nbsp;</div></td></tr><tr><td valign=\"middle\" style=\"border:1px #513E74 solid;background-color:#8270A5\"><table width=\"100%\" border=\"0\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\"><tr><td>" + fastlottoRow("8") + "</td></tr><tr><td bgcolor=\"#513E74\"><table width=\"95%\" border=\"0\" style=\"margin-top:2px\"> \t<tr><td align=\"center\" class=\"style5\" rowspan=\"2\"> <div \tstyle=\"border: 1px solid #8270A5; width: 50px; height: 40px; float: left\">QP<br /><input type=\"checkbox\" id=\"qpChkAll\" onclick=\"fastlottoChkQP(this.id)\" style=\"border: 1px #513E74 solid;\" /> </div></td><td class=\"fastlottotxtStyle\" align=\"left\">Total Qty:</td><td> <input type=\"hidden\" value=\"\" id=\"pickedNumbers\" name=\"pickedNumbers\" /> <input type=\"text\" class=\"fastlottotextbox\" id=\"tkts\" maxlength=\"1\" readonly=\"true\" value=\"0\" onkeyup=\"return fastlottofillNoOfQp(this, event)\" onkeydown=\"return fastlottofillNoOfQp(this, event)\" onblur=\"fastlottoChkNoOfQP()\"/></td><td rowspan=\"2\"> <a href=\"#\"><img id='buy' src=\"" + fortImgPath + "fastlotto_buy.gif\" onclick=\"return buyTicketfastlotto(this.id)\" alt=\"Buy\" style=\"margin-right: 4px;\" /> </a></td> \t</tr> \t<tr><td class=\"fastlottotxtStyle\" align=\"left\"> Total Amount:</td><td> <input type=\"text\" id=\"totAmtLto\" name=\"totalPurchaseAmt\" value=\"0\" \treadonly=\"true\" class=\"fastlottotextbox\" /></td> \t</tr> </table></td></tr></table></td></tr></table></div></td></tr></table></tr><tr><td style=\"line-height:14px\">&nbsp;</td></tr></table>";
var _fastlottoHTML = "<div id=\"strip\">" + drwMenu + "</div><form style=\"background-color:#FFFFFF;border: 1px solid #393939;\"><table  border=\"0\" width=\"100%\" height=\"100%\" cellspacing=\"0\" cellpadding=\"0\" align=\"center\" bgcolor=\"#FFFFFF\"  bordercolor=\"red\"><tr><td width=\"10%\"  valign=\"top\" align=\"center\" onmousedown=\"return disClick(this)\"><input type=\"hidden\" id=\"drawType\"><div id=\"winningDispDiv\" style=\"border:1px #513E74 solid;background-color:#8270A5;margin-left:5px;margin-top:3px\"></div><div id=\"nxtDrawDiv\" style=\"border:1px #513E74 solid;background-color:#8270A5;margin-left:5px;margin-top:5px\"></div></td><td width=\"90%\" valign=\"top\"><div id=\"fastlottomiddle\" style=\"border:1px #513E74 solid;background-color:#8270A5;width:98%;margin-left:5px;margin-top:3px;margin-bottom:1px\">" + _fastlottoHtmlTbl + "</div></td></tr></table></td></tr></table></form>";

parent.frames[0].Version['$Source: /rep/LMS_Mgmt/WebRoot/com/skilrock/lms/web/drawGames/playMgmt/games/fastlotto/Attic/fastlotto.js,v $'] = '$Id: fastlotto.js,v 1.1.2.2.2.2.2.3.8.1 2013/09/19 05:12:33 yogesh Exp $';