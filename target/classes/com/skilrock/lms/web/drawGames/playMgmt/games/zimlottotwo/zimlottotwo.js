
var _zimlottotwoWinArr = new Array();
var curDrwTimelotto = "";
var zimlottotwocurSel = "row1";
var zimlottotwoIsAllQP = false;
var _zimlottotwoPrice = parseFloat(gameDispName["zimlottotwo"].unitPrice.split("=")[1], 10);
var playType;
var startPoint;
function setDefaultzimlottotwo() {
	curDrwTimefortune = "";
	zimlottotwocurSel = "row1";
	zimlottotwoIsAllQP = false;
	playType = "Direct6";
	startPoint = 1;
}
function _zimlottotwoWinAppend() {
	if (!_zimlottotwoWin.match("Result Awaited")) {
		_zimlottotwoWin = "Result Awaited=\"\" Nxt" + _zimlottotwoWin;
	}
}
function _zimlottotwoLatestDrawTime() {
	return _zimlottotwoStatus.split("=")[1];
}
function _zimlottotwoFillWinTbl() {
	var _zimlottotwoWinHt = "<table width=\"100%\" border=\"0\" bordercolor=\"red\" cellspacing=\"0\" cellpadding=\"0\" align=\"center\" bgcolor=\"#FFF1D2\"><tr><td colspan=\"2\" align=\"left\"><div valign=\"middle\" style=\"height:22px;background-color:#F8AD40;padding-top:3px;font-size:16px;font-family:Arial;font-weight:bold;color:#FFFFFF\">&nbsp;Win Result&nbsp;</div></td></tr><tr><td valign=\"top\" align=\"center\">";
	var winArr = _zimlottotwoWin.split("Nxt");
	var _zimlottotwoWinHTML = "<table width=\"100%\" border=\"0\" style=\"margin-top:3px\"><tr><td><div style=\"" + scrollColor("#FFDF96", "#F8AD40", "184") + "\">";
	for (var i = 0; i <= winArr.length - 2; i++) {
		if (!winArr[i].match("Result")) {
			var time = new Date(parseInt(winArr[i].split("=")[0].replace(" ", "")));
			var newTime = week[time.getDay()] + " " + time.getDate() + " " + month[time.getMonth()] + " " + (time.getHours() < 10 ? "0" + time.getHours() : time.getHours()) + ":" + (time.getMinutes() < 10 ? "0" + time.getMinutes() : time.getMinutes()) + ":" + (time.getSeconds() < 10 ? "0" + time.getSeconds() : time.getSeconds());
			var htm = "<table width=\"90%\" border=\"0\" bordercolor=\"green\" id=\"result" + i + "\" cellspacing=\"2\" cellpadding=\"10\" class=\"zimlottotwo_winningResult_table\"><tr><td class=\"zimlottotwowinningResultTime\" colspan=\"8\" height=\"39\">" + newTime + "</td></tr><tr>";
			var num = (winArr[i].split("=")[1]).split(",");
			for (var j = 0; j < num.length; j++) {
				if (j == 3) {
					htm = htm + "</tr><tr>";
				}
				var temp = (num[j] < 10 ? "0" + num[j] : num[j]);
				htm = htm + "<td width=\"10%\" align=\"center\"><span class=\"selected_win_zimlottotwo_numbers\">" + trim(temp, " ") + "</span></td>";
			}
			htm = htm + "</tr></table>";
			_zimlottotwoWinHTML = _zimlottotwoWinHTML + htm;
		} else {
			var htm = "<table width=\"90%\" bordercolor=\"blue\" border=\"0\" cellspacing=\"2\" cellpadding=\"10\" id=\"result" + i + "\" class=\"zimlottotwo_winningResult_table\" ><tr><td class=\"zimlottotwowinningResultTime\" colspan=\"8\" height=\"44\" >Result Awaited</td></tr><tr>";
			htm = htm + "<td class=\"selected_win_zimlottotwo_numbers\" colspan=\"8\"><div style=\"text-align:center\" id=\"winAwaitedDiv\" style=\"color:#DD8C17\"></div></td>";
			htm = htm + "</tr></table>";
			_zimlottotwoWinHTML = _zimlottotwoWinHTML + htm;
		}
	}
	_zimlottotwoWinHTML = _zimlottotwoWinHTML + "</div></td></tr></table>";
	return _zimlottotwoWinHt + _zimlottotwoWinHTML + "</td></tr></table>";
}
function _zimlottotwoWinAjx() {
	var url = "fetchWinZimLottotwo.action?frmDate=" + _zimlottotwoWinArr[_zimlottotwoWinArr.length - 1];
	_zimlottotwoWin = _zimlottotwoWin + (winAjaxReq(url)).data;
	_zimlottotwoFillWinTbl();
}
function zimlottotwoNmSel(lstNo) {
	var htm = "<div id=\"\"><table width=\"100%\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\" bgcolor=\"#FFDF96\" style=\"margin-top:3px;margin-bottom:3px\"> <tr>";
	for (var i = 1; i <= lstNo; i++) {
		if (i % 5 == 0) {
			htm = htm + "<td width=\"17%\" align=\"right\" valign=\"middle\" style=\"cursor:pointer;background:url(" + projectName + "/LMSImages/images/zimlottotwo_shade_white.gif) center no-repeat\" class=\"selected_zimlottotwo_numbers\" onmouseout=\"zimlottotwoonmouseouteffect(this.id)\" onmouseover=\"zimlottotwoonmouseovereffect(this.id)\"  onclick=\"fillNumzimLto(" + i + ")\"  id=\"" + i + "\" >" + (i < 10 ? "0" + i : i) + "</td></tr>";
		} else {
			htm = htm + "<td width=\"17%\" align=\"right\" valign=\"middle\"  class=\"selected_zimlottotwo_numbers\"  style=\"cursor:pointer;background:url(" + projectName + "/LMSImages/images/zimlottotwo_shade_white.gif) center no-repeat\" onmouseout=\"zimlottotwoonmouseouteffect(this.id)\" onmouseover=\"zimlottotwoonmouseovereffect(this.id)\" onclick=\"fillNumzimLto(" + i + ")\"  id=\"" + i + "\"  >" + (i < 10 ? "0" + i : i) + "</td>";
		}
	}
	htm = htm + "</tr></table></div>";
	return htm;
}
function zimlottotwoonmouseovereffect(id) {
	var count = 0;
	var isFilled = false;
	for (var i = 1; i <= 42; i++) {
		if (_id.o(i).className == "selected_zimlottotwo_numbersSelect") {
			count++;
		}
		if (count > 5) {
			isFilled = true;
			break;
		}
	}
	if (!isFilled && !zimlottotwoIsAllQP) {
		if (_id.o(id).className != "selected_zimlottotwo_numbersSelect") {
			_id.o(id).style.background = "url(" + projectName + "/LMSImages/images/zimlottotwo_shade_over.gif) center no-repeat";
			_id.o(id).style.color = "#FFFFFF";
			_id.o(id).className = "selected_zimlottotwo_numbersSelectOMO";
		}
	}
}
function zimlottotwoonmouseouteffect(id) {
	if (!zimlottotwoIsAllQP) {
		if (_id.o(id).className == "selected_zimlottotwo_numbersSelectOMO") {
			_id.o(id).style.background = "url(" + projectName + "/LMSImages/images/zimlottotwo_shade_white.gif) center no-repeat";
			_id.o(id).style.color = "#DD8C17";
			_id.o(id).className = "selected_zimlottotwo_numbers";
		} else {
			if (_id.o(id).className == "selected_zimlottotwo_numbersSelect") {
				_id.o(id).style.background = "url(" + projectName + "/LMSImages/images/zimlottotwo_shade_over.gif) center no-repeat";
				_id.o(id).style.color = "#FFFFFF";
			} else {
				if (_id.o(id).className == "selected_zimlottotwo_numbers") {
					_id.o(id).style.background = "url(" + projectName + "/LMSImages/images/zimlottotwo_shade_white.gif) center no-repeat";
					_id.o(id).style.color = "#DD8C17";
				}
			}
		}
	}
}
function zimlottotwoChkQP(id) {
	if (_id.o(id).checked == true) {
		zimlottotwoIsAllQP = true;
		//_id.o("tkts").readOnly = false;
		zimlottotwoapplyCSS();
		if(playType=='Direct6'){
		_id.o("tkts").value = "";
		_id.o("tkts").readOnly = false;
		_id.o("tkts").focus();
		}else if(playType=='Perm6'){
		_id.o("tkts").value="";
		_id.o("totAmtLto").value="";
		_id.o("noPicked").value = "";
		_id.o("noPicked").readOnly = false;
		_id.o("noPicked").focus();
		}
	} else {
		zimlottotwoIsAllQP = false;
		_id.o("tkts").value = "0";
		_id.o("tkts").readOnly = true;
		if(playType=='Direct6'){
			_id.o("radio1").checked = true;
			zimlottotwocurSel = "row1";
		}else if(playType=='Perm6'){
			_id.o("noPicked").readOnly = true;
			_id.o("noPicked").value = "0";
		}
		zimlottotwoapplyCSS();
	}
}
function zimlottotwoRow(noOfpanels) {
	var htm = "<table width=\"100%\" id=\"lottoRow\" border=\"0\" cellspacing=\"2\" cellpadding=\"0\"><tr> <td width=\"8%\" align=\"center\" valign=\"middle\">&nbsp;</td><td width=\"10%\" align=\"center\" valign=\"middle\" class=\"style5\">&nbsp;</td>";
	for (var indx = 1; indx < 7; indx++) {
		htm = htm + "<td width=\"12%\" align=\"center\" valign=\"middle\" style=\"color:#DD8C17;text-align: center;font-weight:bold;font-size:16px\">" + indx + "</td>";
	}
	htm = htm + "</tr> <tr>";
	for (var i = 1; i < parseInt(noOfpanels) + 1; i++) {
		htm = htm + "<tr onclick=\"fillSelzimLto(" + i + ")\" id=\"row" + i + "\"><td align=\"center\" style=\"color:#DD8C17;text-align: center;font-weight:bold;font-size:16px\">" + i + "</td><td><input type=\"radio\" name=\"lottoRadio\" id=\"radio" + i + "\" style=\"border:1px #FFDF96 solid\" onclick=\"fillSelzimLto(" + i + ")\" " + (i == 1 ? "checked" : "") + "/></td>";
		for (var j = 0; j < 6; j++) {
			htm = htm + "<td align=\"center\" valign=\"middle\" ><input type=\"text\" class=\"selected_zimlottotwo\" maxlength=\"2\" onkeydown=\"return keyPresszimLtoTwo(this," + i + ",event)\" onkeyup=\"return keyPresszimLtoTwo(this," + i + ",event)\" onblur=\"blurFnzimLto(this)\" onmousedown=\"return disClick(this)\"></td>";
		}
		htm = htm + "</tr>";
	}
	return htm + "</table>";
}
function zimlottotwoclearBoxes(val) {
	_id.resetInpTypes("row" + val, "selected_zimlottotwo", false);
	_zimlottotwoCalAmt();
}
function zimlottotwoclearAllBoxes() {
	_id.resetInpTypes("lottoRow", "selected_zimlottotwo", false);
	_id.o("noOfDraws").value = 1;
	_id.o("tkts").readOnly = true;
	//zimlottotwocurSel = "row1";
	zimlottotwoIsAllQP = false;
	zimlottotwoapplyCSS();
	if(playType!='Perm6'){
	zimlottotwocurSel = "row1";
	}
	_id.o("qpChkAll").checked = false;
	_zimlottotwoCalAmt();
}
function _zimlottotwoFillNxtDrw() {
	var nxtDrwHTML = "<table width=\"100%\" cellspacing=\"0\"cellpadding=\"0\"  border=\"0\" bordercolor=\"red\"><tr><td colspan=\"2\" align=\"left\" valign=\"top\"><div valign=\"middle\" style=\"height:22px;width:180px;background-color:#F8AD40;padding-top:3px;font-size:16px;font-family:Arial;font-weight:bold;color:#FFFFFF\">&nbsp;Next Draw&nbsp;</div></td></tr><tr><td align=\"center\" valign=\"top\"><div><table width=\"100% bordercolor=\"red\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\" ><tr><td width=\"100%\" align=\"center\" valign=\"top\" colspan=\"2\"><div style=\"" + scrollColor("#FFDF96", "#F8AD40", "175") + "\"><table width=\"90%\" bordercolor=\"red\"  border=\"0\" cellspacing=\"0\" cellpadding=\"0\" class=\"borderbtmzimlottotwo\">";
	var nxtDrwArr = _zimlottotwoDrawTime.split(",");
	for (var i = 0, l = nxtDrwArr.length; i < l; i++) {
		var time = new Date(parseInt(nxtDrwArr[i].replace(" ", "")));
		var newTime = week[time.getDay()] + " " + time.getDate() + " " + month[time.getMonth()] + " " + (time.getHours() < 10 ? "0" + time.getHours() : time.getHours()) + ":" + (time.getMinutes() < 10 ? "0" + time.getMinutes() : time.getMinutes());
		var hiddenTime = "<input type='text'  id='timer" + i + "'>";
		var rowcolor;
		var data = "";
		if (i == 0) {
			data = time.getFullYear() + "-" + time.getMonth() + "-" + time.getDate() + "-" + (time.getHours() < 10 ? "0" + time.getHours() : time.getHours()) + "-" + (time.getMinutes() < 10 ? "0" + time.getMinutes() : time.getMinutes()) + "-" + (time.getSeconds() < 10 ? "0" + time.getSeconds() : time.getSeconds());
			var dateFuture1 = new Date(time.getFullYear(), time.getMonth(), time.getDate(), (time.getHours() < 10 ? "0" + time.getHours() : time.getHours()), (time.getMinutes() < 10 ? "0" + time.getMinutes() : time.getMinutes()), (time.getSeconds() < 10 ? "0" + time.getSeconds() : time.getSeconds()));
			_zimlottotwoNxtDrawTime = ("" + time).substring(0, ("" + time).lastIndexOf(":") + 3);
		}
		if (i % 2 == 0) {
			rowcolor = "#FFF1D2";
		} else {
			rowcolor = "#FFDF96";
		}
		nxtDrwHTML = nxtDrwHTML + "<tr><td width=\"100%\"><input type=\"hidden\" value=\"" + data + "\" id=\"" + i + "timer\"></td></tr><tr bgcolor=\"" + rowcolor + "\" style=\"height: 20px\"><td width=\"100%\"align=\"center\" class=\"borderbtmzimlottotwo\" colspan=\"2\"><div id=\"" + i + "blink\">" + newTime + "</div></td><td width=\"44%\"align=\"center\"><p><div id=\"nxtDrwD" + i + "\" style=\"display:none\"><img src=\"" + fortImgPath + "freezed.gif\"></div></p></td></tr>";
	}
	nxtDrwHTML = nxtDrwHTML + "</table></div></td></tr></table></div></td></tr></table>";
	return nxtDrwHTML;
}
function _zimlottotwoUpData(win, drwTime, drwStatus) {
	_zimlottotwoWin = win;
	_zimlottotwoDrawTime = drwTime;
	_zimlottotwoStatus = drwStatus;
}
function fillSelzimLto(val) {
	if (!zimlottotwoIsAllQP) {
	countFill=0;
		zimlottotwocurSel = "row" + val;
		_id.o("radio" + val).checked = true;
		var newObjR=_id.o(zimlottotwocurSel);
		var newElms = newObjR.getElementsByTagName("input");
		for (var l = startPoint; l < newElms.length; l++) {
			if (newElms[l].value == "") {
				newElms[l].focus();
				break;
			}else{
				countFill++;
			}
		}
		zimlottotwoapplyCSS();
	}
}
var countFill=0;
function fillNumzimLto(val) {   
	if (!zimlottotwoIsAllQP) {
		var objR = _id.o(zimlottotwocurSel);
		var clrChg = true;
		var elms = objR.getElementsByTagName("input");
		if (_id.o(val).className == "selected_zimlottotwo_numbersSelect") {
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
		if(playType=="Direct6" && countFill==6){
			//alert(parseInt(zimlottotwocurSel.replace("row",""),10)+1);
			countFill=0;
			var rowNo=parseInt(zimlottotwocurSel.replace("row",""),10)+1;
			if(rowNo>5){
				rowNo=1;
			}			
			zimlottotwocurSel="row"+rowNo;
			
			//alert(countFill);
			fillSelzimLto(rowNo);
		}else{
		zimlottotwoapplyCSS();
		}
	}
}
function _zimlottotwoCalAmt() {
	var noOfDrw = _id.o("noOfDraws").value;
	var noOfPanel = 0;
	var obj, elms;
	var isPanel = true;
	if (zimlottotwoIsAllQP) {
		noOfPanel = _id.o("tkts").value;
	}else{
	if (playType == "Direct6") {
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
	} else {
		if (playType == "Perm6") {
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
			noOfPanel = calPermLineZimLotto();
		}
	}
	}
	if (noOfPanel != 0 || _id.o("qpChkAll").checked == false) {
		_id.o("tkts").value = noOfPanel;
	} else {
		_id.o("tkts").value = "";
	}
	
	var totalPrice=noOfDrw * noOfPanel * _zimlottotwoPrice;
	totalPrice=tktPriceRound(totalPrice);
	//alert(totalPrice);
	_id.o("totAmtLto").value = totalPrice;
}
function fillNoOfPanels(id) {
	var noOfpanels = _id.o(id).value;
	_id.i("lottoRowDiv", lottoRow(noOfpanels));
}
function keyPresszimLtoTwo(obj, val,e) {
	var keyCode = (window.event)? event.keyCode : e.which;
	//var keyCode = event.keyCode;
	//alert(keyCode);
	if ((keyCode >= 48 && keyCode < 58) || keyCode == 45 || keyCode == 27 || keyCode == 13 || keyCode == 40 || keyCode == 38 || keyCode == 46 || keyCode == 8 || (keyCode >= 96 && keyCode < 106 || keyCode == 9)) {
		if (keyCode == 46 || keyCode == 8 || keyCode == 9) {
			if (keyCode == 9) {
				//alert(val);
				//fillSelzimLto(val);
				return false;
			}
			return true;
		}
		if (obj.value.length > 1) {
			if (obj.value == 0 || obj.value > 42 || isNaN(obj.value)) {
				obj.value = "";
				obj.focus();
			} else {
				var objR = _id.o(zimlottotwocurSel);
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
						var nxtRow = parseInt(zimlottotwocurSel.substring(zimlottotwocurSel.length - 1), 10) + 1;
						if (nxtRow <= 5) {
							var nxtRowStr = zimlottotwocurSel.substring(0, zimlottotwocurSel.length - 1) + nxtRow;
						//alert(nxtRowStr);
							_id.o(nxtRowStr).getElementsByTagName("input")[1].focus();
							fillSelzimLto(nxtRow);
						}
					}
				}
			}
			zimlottotwoapplyCSS();
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
	zimlottotwoapplyCSS();
}
function zimlottotwoapplyCSS() {
	for (var i = 1; i <= 42; i++) {
		_id.o(i).className = "selected_zimlottotwo_numbers";
		_id.o(i).style.background = "url(" + projectName + "/LMSImages/images/zimlottotwo_shade_white.gif) center no-repeat";
		_id.o(i).style.color = "#DD8C17";
	}
	var clrChg = true;
	var obj = _id.o(zimlottotwocurSel);
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
	if (playType == "Perm6") {
		maxCount = 30;
	}
	for (var l = startPoint; l < elms.length; l++) {
		if (elms[l].value != "") {
			if (count < maxCount) {
				_id.o(parseInt(elms[l].value, 10)).className = "selected_zimlottotwo_numbersSelect";
				_id.o(parseInt(elms[l].value, 10)).style.background = "url(" + projectName + "/LMSImages/images/zimlottotwo_shade_over.gif) center no-repeat";
				_id.o(parseInt(elms[l].value, 10)).style.color = "#FFFFFF";
				count++;
			}
		}
	}
	var obj1 = _id.o("lottoRow");
	var txtelms = obj1.getElementsByTagName("input");
	if (zimlottotwoIsAllQP) {
		for (var i = 1; i <= 42; i++) {
			_id.o(i).className = "selected_zimlottotwo_numbersDisabled";
			_id.o(i).style.background = "url(" + projectName + "/LMSImages/images/zimlottotwo_shade_disable.gif) center no-repeat";
			_id.o(i).style.color = "#FFFFFF";
		}
		if (playType == "Perm6") {
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
	_zimlottotwoCalAmt();
}
/*function zimlottotworesetFrm() {
	document.forms[0].reset();
	for (var i = 1; i <= 42; i++) {
		_id.o(i).className = "selected_zimlottotwo_numbers";
	}
	var elms = document.forms[0].getElementsByTagName("input");
	for (var l = 0; l < elms.length; l++) {
		elms[l].disabled = false;
	}
	zimlottotwocurSel = "row1";
}*/
var gameChildW = null;
function zimlottotwoGameInfo() {
	if (gameChildW) {
	} else {
		gameChildW = window.open("zimLotto/gameInfo.jsp", "NewWin", "width=650,height=550,toolbar=0,resizable=0,scrollbars=1,alwaysRaised=0,location=0,menubar=0");
		gameChildW.close();
	}
	gameChildW = window.open("zimLotto/gameInfo.jsp", "NewWin", "width=650,height=550,toolbar=0,resizable=0,scrollbars=1,alwaysRaised=0,location=0,menubar=0");
}
function buyTicketzimlottotwo(id) {
	//alert(playType);
	var compData = "";
	var noPicked = 6;
	if (playType == "Direct6") {
		if (!zimlottotwoIsAllQP) {
			for (var i = 1; i <= 5; i++) {
				var inputData = "";
				var obj = _id.o("row" + i);
				var elms = obj.getElementsByTagName("input");
				for (var l = 1; l < elms.length; l++) {
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
				compData = compData + inputData;
			}
			var noPanels = compData.split("Nxt").length-1;
			//alert(compData.split("Nxt").length-1);
			if (noPanels % 5 != 0) {
				alert("Please Select All Panels");
				_id.o("tkts").value = "";
				return false;
			}
		} else {
			var noPanels = _id.o("tkts").value;
			//alert(noPanels);
			if (noPanels % 5 != 0) {
				alert("Please Enter Value in Multiple Of 5");
				_id.o("tkts").value = "";
				_id.o("totAmtLto").value = "";
				return false;
			}
			if(noPanels >50){
				alert('You can select maximum 50 panels at a time');
				_id.o("tkts").value = "";
				_id.o("totAmtLto").value = "";
				return false;
			}
			for (var i = 1; i <= noPanels / 5; i++) {
				inputData = "QPNxtQPNxtQPNxtQPNxtQPNxt";
				compData = compData + inputData;
			}
		}
	} else {
	//alert(zimlottotwoIsAllQP);
	if (!zimlottotwoIsAllQP) {
		var inputData = "";
		var obj = _id.o("lottoRow");
		var elms = obj.getElementsByTagName("input");
		for (var l = 0; l < elms.length; l++) {
			if (elms[l].value != "") {
				inputData = inputData + elms[l].value + ",";
			}
		}
		//alert(inputData+'   '+inputData.split(",").length);
		if (inputData != "" && inputData.split(",").length < (2 + parseInt(6))) {
			alert("Please pick minimum 7 numbers");
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
		compData = compData + inputData;
		noPicked = compData.split(",").length;
		//alert(noPicked);
	}else{
		compData="QPNxt";
		noPicked=_id.o('noPicked').value;
		if (noPicked < (1 + parseInt(6))) {
			alert("Please pick minimum 7 numbers");
			return false;
		}
		if(noPicked >(1 + parseInt(14))){
			alert('You can pick maximum 15 numbers');
			_id.o('noPicked').value="";
			_id.o('tkts').value="";
			_id.o('totAmtLto').value="";
			return false;
		}
	}
	}
	compData = compData.substring(0, compData.length - 3);
	_id.o("pickedNumbers").value = compData;
	if (compData.length < 1) {
		alert("Please fill incomplete entries");
		return false;
	}
	var amount = _id.o("totAmtLto").value;
	_id.o(id).disabled = true;
	gameBuyAjaxReq("zimLottotwoBuy.action?noPicked=" + noPicked + "&playType=" + playType + "&pickedNumbers=" + compData + "&noOfDraws=" + _id.o("noOfDraws").value + "&totalPurchaseAmt=" + amount);
	zimlottotwoclearAllBoxes();
	return true;
}
function zimlottotwoMultiPlay() {
	playTypeObj = _id.n("playType");
	for (var i = 0; i < playTypeObj.length; i += 1) {
		if (playTypeObj[i].checked == true) {
			if (playTypeObj[i].value == "Direct6") {
				_id.o("playDiv").innerHTML = zimlottotwoRow("5");
				zimlottotwocurSel = "row1";
				startPoint = 1;
			} else {
				if (playTypeObj[i].value == "Perm6") {
					_id.o("playDiv").innerHTML = zimlottotwoPermTlb();
					zimlottotwocurSel = "lottoRow";
					startPoint = 0;
				}
			}
			playType = playTypeObj[i].value;
		}
	}
	zimlottotwoclearAllBoxes();
}
function zimlottotwoPermTlb() {
	var htm = "<table width=\"100%\" id=\"lottoRow\" border=\"0\" cellspacing=\"2\" cellpadding=\"0\"><tr>";
	for (var i = 1; i < 4; i++) {
		htm = htm + "<tr id=\"rowPerm\">";
		for (var j = 0; j < 5; j++) {
			htm = htm + "<td align=\"center\" valign=\"middle\" ><input name='numbers' type=\"text\" class=\"selected_zimlottotwo\" maxlength=\"2\" onkeydown=\"return keyPresszimLtoTwo(this," + i + ",event)\" onkeyup=\"return keyPresszimLtoTwo(this," + i + ",event)\" onblur=\"blurFnzimLto(this)\" onmousedown=\"return disClick(this)\"></td>";
		}
		htm = htm + " </tr>";
	}
	htm = htm + "</table><br/><table border=\"0\" style=\"height:18px;background-color:#F8AD40;\"><tr><td class=\"zimlottotwotxtStyle\" align=\"left\">No. Picked</td><td><input class=\"zimlottotwotextbox\" type='text' id='noPicked' value='0' onkeyup='return keyPressQPzimLtoTwo(this,event)' onkeydown='return keyPressQPzimLtoTwo(this,event)' onblur=\"zimlottoCalPermQP()\" onmousedown=\"return disClick(this)\"/></td></tr>";
	return htm + "</table>";
}
var lines = 0;
function calPermLineZimLotto() {
	var num = _id.o("noPicked").value;
	if (num >= 6) {
		lines = (num * (num - 1) * (num - 2) * (num - 3) * (num - 4) * (num - 5)) / 720;
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

function zimlottoCalPermQP(){
	_id.o('tkts').value=calPermLineZimLotto();
	zimlottotwoapplyCSS();
}

function keyPressQPzimLtoTwo(obj,e) {
	var keyCode = (window.event)? event.keyCode : e.which;
	if ((keyCode >= 48 && keyCode < 58) || keyCode == 45 || keyCode == 27 || keyCode == 13 || keyCode == 40 || keyCode == 38 || keyCode == 46 || keyCode == 8 || (keyCode >= 96 && keyCode < 106 || keyCode == 9)) {
		if (keyCode == 46 || keyCode == 8 || keyCode == 9) {
			if(playType=='Direct6'){
				zimlottotwoapplyCSS();
			}else{
				zimlottoCalPermQP();
			}
			return true;
		}
		if (obj.value.length >= 1) {
			var maxCount=15;
			if(playType=='Direct6'){
			 maxCount=50;
			}
			if (obj.value == 0 || obj.value > maxCount || isNaN(obj.value)) {
				obj.value = "";
				obj.focus();
			}
			if(playType=='Direct6'){
				zimlottotwoapplyCSS();
			}else{
				zimlottoCalPermQP();
			}
		}
	} else {
		return false;
	}
	return true;
}

function scriptBuy() {
	for (var i = 0; i < 1000; i += 1) {
		gameBuyAjaxReq("zimLottoBuytwo.action?pickedNumbers=QPNxtQP&noOfDraws=1&totalPurchaseAmt=2");
	}
}
var line = "<tr><td bgcolor=\"#F8AD40\"><table border=\"0\" bordercolor=\"blue\" width=\"97%\"><tr><td align=\"left\" width=\"25%\"><input type=\"hidden\" value=\"0\" id=\"counterId\"><input type=\"hidden\" id=\"dateFuture\"><input type=\"hidden\" id=\"freezeTimeId\" value=\"" + _zimlottotwofreezeTime + "\"><span class=\"style1\">Next Draw :</span><div id=\"latestDrawTime\" style=\"display:none\"></div></td><td width=\"30%\" align=\"left\"><div id=\"nxtDrawTime\" class=\"zimlottotwostyle2\"></div></td><td align=\"right\" width=\"25%\"><div id=\"timeLeft\"><span class=\"style1\"> Time Left :</div></span></td> <td align=\"left\" width=\"15%\"><div id=\"curDrwTime\" class=\"zimlottotwostyle2\"></div> </td><!--<td rowspan=\"2\" width=\"10%\"><img src=\"" + fortImgPath + "que.gif\" style=\"cursor:hand;\" onClick=\"gameInfo()\" alt=\"Game Info\" /> </td>--></tr> <tr bgcolor=\"#F8AD40\"><td align=\"left\"> <span class=\"style1\">No. of Draws :</span> </td> <td > <select\tname=\"noOfDraws\" id=\"noOfDraws\"\tclass=\"fortune_option\" align=\"left\"\twidth=\"20%\"\tonchange=\"_zimlottotwoCalAmt()\">" + getActiveDrawOptionList("zimlottotwo") + "</select> </td> <td colspan=\"1\" align=\"left\" valign=\"top\"> &nbsp; </td><td align=\"left\"> <span class=\"zimlottotwonum\">" + currSymbol + " " + _zimlottotwoPrice + "/-</span></td>  </table></td></tr>";
var _zimlottotwoNxtDrwTbl = _zimlottotwoFillNxtDrw();
var _zimlottotwoHtmlTbl = "<table width=\"100%\" height=\"100%\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\" bordercolor=\"red\" bgcolor=\"#FFF1D2\">" + line + "<tr><td align=\"center\" valign=\"top\"><table width=\"100%\" cellspacing=\"0\" cellpadding=\"0\" border=\"0\"><tr><td width=\"45%\" valign=\"top\"><div style=\"padding-top:10px;\"><table border=\"0\" bordercolor=\"red\" width=\"97%\" align=\"center\"cellpadding=\"0\"cellspacing=\"0\"><tr><td class=\"style10\" align=\"left\"><div id='jackpotAmt' align=\"center\" valign=\"middle\" style=\"height:18px;background-color:#F8AD40;width:180px;float:left;padding-top:2px\"></div></td></tr><tr><td valign=\"middle\" style=\"border:1px #F8AD40 solid;background-color:#FFDF96\">" + zimlottotwoNmSel(42) + "</td></tr></table></div></td><td valign=\"top\" wdith=\"35%\"><div style=\"border:0px solid black; padding-top:10px;\"><table border=\"0\" bordercolor=\"green\" width=\"96%\" cellpadding=\"0\" cellspacing=\"0\" align=\"center\" style=\"margin-right:10px\"><tr><td><input type='radio' name='playType' value='Direct6' checked='true' onclick='zimlottotwoMultiPlay()'>Direct-6<input type='radio' name='playType' value='Perm6' onclick='zimlottotwoMultiPlay()'>Perm-6</td><td align=\"right\" class=\"style10\" style=\"cursor:hand;\" onclick=\"zimlottotwoclearAllBoxes()\" ><div align=\"center\" valign=\"middle\" style=\"height:18px;background-color:#F8AD40;width:100px;float:right;padding-top:2px\">&nbsp;&nbsp;&nbsp;Clear All&nbsp;&nbsp;&nbsp;</div></td></tr><tr><td valign=\"middle\" style=\"border:1px #F8AD40 solid;background-color:#FFDF96\" colspan=2><table width=\"100%\" border=\"0\" align=\"center\" cellspacing=\"0\" cellpadding=\"0\"><tr><td><div id='playDiv'>" + zimlottotwoRow("5") + "</div></td></tr><tr><td bgcolor=\"#F8AD40\"><table width=\"95%\" border=\"0\" style=\"margin:6px 0px 5px 0px;\"><tr><td align=\"center\" class=\"style5\" rowspan=\"2\"><div style=\"border:1px #FFFFFF solid; width: 50px; height: 40px; float: left;\">QP<br /><input type=\"checkbox\" id=\"qpChkAll\" onclick=\"zimlottotwoChkQP(this.id)\" style=\"border:1px #F8AD40 solid\" /></div></td><td class=\"zimlottotwotxtStyle\" align=\"left\">Total Qty:</td><td><input type=\"hidden\" value=\"\" id=\"pickedNumbers\" name=\"pickedNumbers\" /><input type=\"text\" class=\"zimlottotwotextbox\" id=\"tkts\" readonly=\"true\" value=\"0\" onkeyup='return keyPressQPzimLtoTwo(this,event)' onkeydown='return keyPressQPzimLtoTwo(this,event)' onmousedown=\"return disClick(this)\"  /></td><td rowspan=\"2\"><a href=\"#\"><img src=\"" + fortImgPath + "zimlottotwo_buy.gif\" id='buy' onclick=\"return buyTicketzimlottotwo(this.id)\" alt=\"Buy\" style=\"margin-right: 4px;\" /></a></td></tr><tr><td class=\"zimlottotwotxtStyle\" align=\"left\">Total Amount:</td><td><input type=\"text\" id=\"totAmtLto\" name=\"totalPurchaseAmt\" value=\"0\" readonly=\"true\" class=\"zimlottotwotextbox\" /></td></tr> </table></td></tr></table></td></tr></table></div></td></tr></table></tr><tr><td style=\"line-height:10px\">&nbsp;</td></tr></table>";
var _zimlottotwoHTML = "<div id=\"strip\">" + drwMenu + "</div><form style=\"background-color:#FFFFFF;border: 1px solid #393939;\"><table  border=\"0\" width=\"100%\" height=\"100%\" cellspacing=\"0\" cellpadding=\"0\" align=\"center\" bgcolor=\"#FFFFFF\" bordercolor=\"red\"><tr><td width=\"10%\"  valign=\"top\" align=\"center\" onmousedown=\"return disClick(this)\"><input type=\"hidden\" id=\"drawType\"><div id=\"winningDispDiv\" style=\"border:1px #F8AD40 solid;background-color:#FFFFFF;margin-left:5px;margin-top:3px\"></div><div id=\"nxtDrawDiv\" style=\"border:1px #F8AD40 solid;background-color:#FFFFFF;margin-left:5px;margin-top:5px\"></div></td><td width=\"90%\" valign=\"top\"><div id=\"zimlottotwomiddle\" style=\"border:1px #F8AD40 solid;background-color:#FFDF96;width:98%;margin-left:5px;margin-top:3px;margin-bottom:1px\">" + _zimlottotwoHtmlTbl + "</div></td></tr></table></td></tr></table></form>";



parent.frames[0].Version['$Source: /rep/LMS_Mgmt/WebRoot/com/skilrock/lms/web/drawGames/playMgmt/games/zimlottotwo/Attic/zimlottotwo.js,v $'] = '$Id: zimlottotwo.js,v 1.1.2.17.4.1 2013/09/19 05:12:34 yogesh Exp $';
