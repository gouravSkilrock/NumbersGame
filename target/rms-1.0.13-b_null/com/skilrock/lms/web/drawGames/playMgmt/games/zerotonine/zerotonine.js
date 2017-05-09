
var _zerotonineInpTxt = "input type=\"text\" maxlength=\"3\" style=\"font-size: 13px; width: 45px;\" onblur=\"_zerotonineBlur(this)\" onkeydown=\"return keyPresszerotonine(this,event)\" onkeyup=\"return keyPresszerotonine(this,event)\" onmousedown=\"return disClick(this)\"";
var numArr = ["Zero(0)","One(1)", "Two(2)", "Three(3)", "Four(4)", "Five(5)", "Six(6)",
			"Seven(7)", "Eight(8)", "Nine(9)"];
var curDrwTimezerotonine = "";
var _zerotonineColor = ["#2FADBB", "#DCEDEE", "white", "white"];
var _zerotoninePrice=parseInt(gameDispName["zerotonine"].unitPrice.split("=")[1], 10);
function setDefaultzerotonine() {
	curDrwTimezerotonine = "";
}
var mapArr = new Array();
mapArr[1] = "0";
mapArr[2] = "1";
mapArr[3] = "2";
mapArr[4] = "3";
mapArr[5] = "4";
mapArr[6] = "5";
mapArr[7] = "6";
mapArr[8] = "7";
mapArr[9] = "8";
mapArr[10] = "9";
function _zerotonineWinAppend() {
	if (!_zerotonineWin.match("Result Awaited")) {
		_zerotonineWin = "Result Awaited=\"\" Nxt" + _zerotonineWin;
	}
}
function _zerotonineLatestDrawTime() {
	return _zerotonineStatus.split("=")[1];
}

//FILLING WINNING RESULT DIV 
function _zerotonineFillWinTbl() {
	var winArr = _zerotonineWin.split("Nxt");
	var winHtml = "<table width=\"100%\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\"><tr><td align=\"left\"><div valign=\"middle\" style=\"height:22px;background-color:" + _zerotonineColor[0] + ";padding-top:3px;font-size:16px;font-family:Arial;font-weight:bold;color:#FFFFFF\">&nbsp;Win Result&nbsp;</div></td></tr><tr><td><table width=\"98%\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\"><tr><td><div style=\"" + scrollColor(_zerotonineColor[1], _zerotonineColor[0], "210") + "\"><table width=\"90%\" border=\"0\"cellspacing=\"0\"cellpadding=\"0\" >";
	var winTab = "";
	for (var i = 0; i < winArr.length - 1; i++) {
		if (!winArr[i].match("Result")) {
			var time = new Date(parseInt(winArr[i].split("=")[0].replace(" ", "")));
			var newTime = time.getDate() + " " + month[time.getMonth()] + " " + (time.getHours() < 10 ? "0" + time.getHours() : time.getHours()) + ":" + (time.getMinutes() < 10 ? "0" + time.getMinutes() : time.getMinutes()) + ":" + (time.getSeconds() < 10 ? "0" + time.getSeconds() : time.getSeconds());
			var img = ((winArr[i].split("=")[1]).split(",")).toString().replace(" ", "").toLowerCase();
			winTab = winTab + "<tr><td width=\"69%\"height=\"40\"align=\"center\"valign=\"middle\"class=\"borderbtmzerotonine\"><p>" + newTime + "</p></td><td width=\"31%\" align=\"center\" valign=\"middle\"class=\"borderbtmzerotonine\"><img src=\"" + _zerotonineImgPath + img + "Win.png\"alt=\"" + img.toUpperCase() + "\"/></td></tr>";
		} else {
			winTab = "<tr id=\"result" + i + "\"><td height=\"60\" colspan=\"2\" align=\"left\" valign=\"middle\" class=\"borderbtmzerotonine\" style=\"font-size:14px;\">";
			winTab = winTab + "<table width=\"90%\" bordercolor=\"blue\" border=\"0\" cellspacing=\"2\" cellpadding=\"10\" id=\"result" + i + "\" ><tr><td colspan=\"8\" height=\"40\" align=\"center\" style=\"font-size:16px;\">Result Awaited</td></tr><tr>";
			winTab = winTab + "<td colspan=\"8\" align=\"center\"><div style=\"text-align:center\" id=\"winAwaitedDiv\" style=\"color:" + _zerotonineColor[0] + "\"></div></td>";
			winTab = winTab + "</tr></table></td></tr>";
		//winTab =winTab+'Result Awaited</td><td colspan="2" height="20"   align="left"valign="middle"class="borderbtmzerotonine"><div id="winAwaitedDiv" style="color:#663300;font-size:12px;font-weight:800""></div></td></tr>';
		}
	}
	winHtml = winHtml + winTab + "</table></div></td></tr></table></td></tr></table>";
	return winHtml;
}

// FILLING NEXT DRAWS DIV
function _zerotonineFillNxtDrw() {
	var nxtDrwHTML = "<table cellspacing=\"0\" cellpadding=\"0\" width=\"100%\" bgcolor=" + _zerotonineColor[1] + "><tr><td align=\"left\"><div valign=\"middle\" style=\"height:22px;background-color:" + _zerotonineColor[0] + ";padding-top:3px;font-size:16px;font-family:Arial;font-weight:bold;color:#FFFFFF\">&nbsp;Next Draw&nbsp;</div></td></tr><tr><td valign=\"top\"><table width=\"99%\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\" bgcolor=\"" + _zerotonineColor[1] + "\"><tr><td><div style=\"" + scrollColor(_zerotonineColor[1], _zerotonineColor[0], "155") + "\"><table width=\"90%\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\" class=\"nextdrawzerotonine\">";
	var nxtDrwArr = _zerotonineDrawTime.split(",");
	for (var i = 0, l = nxtDrwArr.length; i < l; i++) {
		var time = new Date(parseInt(nxtDrwArr[i].replace(" ", "")));
		var newTime = time.getDate() + " " + month[time.getMonth()] + " " + (time.getHours() < 10 ? "0" + time.getHours() : time.getHours()) + ":" + (time.getMinutes() < 10 ? "0" + time.getMinutes() : time.getMinutes());
		var hiddenTime = "<input type='text'  id='timer" + i + "'>";
		var rowcolor;
		var data = "";
		if (i == 0) {
			data = time.getFullYear() + "-" + time.getMonth() + "-" + time.getDate() + "-" + (time.getHours() < 10 ? "0" + time.getHours() : time.getHours()) + "-" + (time.getMinutes() < 10 ? "0" + time.getMinutes() : time.getMinutes()) + "-" + (time.getSeconds() < 10 ? "0" + time.getSeconds() : time.getSeconds());
			_zerotonineNxtDrawTime = ("" + time).substring(0, ("" + time).lastIndexOf(":") + 3);
		}
		if (i % 2 == 0) {
			rowcolor = _zerotonineColor[1];
		} else {
			rowcolor = _zerotonineColor[2];
		}
		nxtDrwHTML = nxtDrwHTML + "<tr><td><input type=\"hidden\" value=\"" + data + "\" id=\"" + i + "timer\"></td></tr><tr bgcolor=\"" + rowcolor + "\" style=\"height: 20px\"><td width=\"100%\" class=\"borderbtmzerotonine\"><div id=\"" + i + "blink\">" + newTime + "</div></td><td width=\"44%\"><p><div id=\"nxtDrwD" + i + "\" style=\"display:none\"><img src=\"" + _zerotonineImgPath + "freezed.gif\"></div></p></td></tr>";
	}
	nxtDrwHTML = nxtDrwHTML + "</table></div></td></tr></table></td></tr></table>";
	return nxtDrwHTML;
}

// BUY TICKET
function buyTicketzerotonine(id) {
	var noOfDraws = document.getElementsByName("noOfDraws")[0].value;
	var isQP = _id.o("_zerotonineQuickPik").value;
	var sym = "", noOfPan = "";
	for (var l = 0; l < numArr.length; l++) {
		var elms = _id.o(numArr[l]);
		if (elms.value != "" && elms.value != 0) {
			sym = sym + "," + elms.name;
			noOfPan = noOfPan + "," + elms.value;
		}
	}
	_id.o("symbols").value = sym.replace(",", "");
	_id.o("noOfPanels").value = noOfPan.replace(",", "");
	var symbols = _id.o("symbols").value;
	var noOfPanels = _id.o("noOfPanels").value;
	var totalPanels = _id.o("totalNoOfPanels").value;
	_zerotonineCalAmt();
	var amount = _id.o("totAmt").value;
	if (_id.o("totAmt").value == 0 || _id.o("totalNoOfPanels").value == 0) {
		alert("Please fill complete entries");
		if (_id.o("_zerotonineQuickPik").checked) {
			_id.o("totalNoOfPanels").focus();
		}
		return false;
	}
	document.forms[0].reset();
	_id.o(id).disabled=true;
	gameBuyAjaxReq("zeroToNineBuy.action?noOfDraws=" + noOfDraws + "&symbols=" + symbols + "&noOfPanels=" + noOfPanels + "&isQuickPick=" + isQP + "&totalNoOfPanels=" + totalPanels + "&totalPurchaseAmt=" + amount);
	_zerotonineclearAllBoxes();
	return true;
}
function _zerotonineBlur(val) {
	if (!_id.o("_zerotonineQuickPik").checked) {
		/*var obj = _id.o("numbers");
		
		var elms = obj.getElementsByTagName("input");*/
		var tot = 0;
		var curVal = val.value;
		var isExceed = false;
		for (var l = 0; l < numArr.length; l++) {
			var elms = _id.o(numArr[l]);
			if (elms.value != "") {
				if (isNaN(elms.value)) {
					elms.value = "";
				} else {
					tot = tot + parseInt(elms.value,10);
					if (tot > 1000 && val) {
						val.value = "";
						alert("Cannot Select More than 1000 Panels ");
						isExceed = true;
						break;
					}
				}
			}
		}
		if (!isExceed) {
			_id.o("totalNoOfPanels").value = tot;
		} 
	} else {
		if (val && val.value > 1000) {
			val.value = val.value.substring(0, val.value.length - 1);
		}
		if (isNaN(_id.o("totalNoOfPanels").value)) {
			_id.o("totalNoOfPanels").value = "";
		}
	}
	_zerotonineCalAmt();
}
function _zerotonineQP(id) {
	/*var obj = _id.o("numbers");
	var elms = obj.getElementsByTagName("input");*/
	if (_id.o("_zerotonineQuickPik").checked) {
		for (var l = 0; l < numArr.length; l++) {
			var elms = _id.o(numArr[l]);
			if (elms.type == "text") {
				elms.disabled = true;
				elms.value = "";
			}
		}
		_id.o("totalNoOfPanels").readOnly = false;
		_id.o(id).value = "1";
		_id.o("totalNoOfPanels").focus();
	} else {
		for (var l = 0; l < numArr.length; l++) {
			var elms = _id.o(numArr[l]);
			if (elms.type == "text") {
				elms.disabled = false;
			}
		}
		_id.o("totalNoOfPanels").readOnly = true;
		_id.o(id).value = "2";
		_id.o(numArr[0]).focus();
	}
	_id.o("totalNoOfPanels").value = "";
	_zerotonineCalAmt();
}
function keyPresszerotonine(val,e) {
	var keyCode = (window.event) ? event.keyCode : e.which;
	//alert(keyCode)
	if ((keyCode >= 48 && keyCode < 58) || keyCode == 45 || keyCode == 27 || keyCode == 13 || keyCode == 40 || keyCode == 38 || (keyCode >= 96 && keyCode < 106 || keyCode == 46 || keyCode == 8 || keyCode == 9)) {
		_zerotonineBlur(val);
		if (keyCode == 46 || keyCode == 8 || keyCode == 9) {
			return true;
		}
		if (val.value != 0 || val.value != "" || !isNaN(val.value)) {
			var clrChg = true;
			
			if(val.value==0){
			val.value="";
			}		
			for (var i = 0; i < numArr.length; i++) {
				if (val.value.length == 3) {
					if (_id.o(numArr[i]).value == "") {
						_id.o(numArr[i]).focus();
						break;
					}
				}
			}
			return true;
		} else {
			val.focus();
			return false;
		}
	}
	return false;
}
function _zerotonineUpData(win, drwTime, drwStatus) {
	_zerotonineWin = win;
	_zerotonineDrawTime = drwTime;
	_zerotonineStatus = drwStatus;
}
function _zerotonineCalAmt() {
	_id.o("totAmt").value = _id.v("totalNoOfPanels") * _zerotoninePrice * _id.v("noOfDraws");
}
function selImagezerotonine(ele, func) {
	var num = _id.v(ele);
	var totPanel = _id.v("totalNoOfPanels");
	if (!_id.o("_zerotonineQuickPik").checked) {
		if (func == "plus") {
			if (num < 1000 && totPanel < 1000) {
				_id.o(ele).value = parseInt(num == "" ? 0 : num, 10) + 1;
				_zerotonineBlur(_id.o(ele));
			}
		} else {
			if (num > 0) {
				_id.o(ele).value = parseInt(num == "" ? 0 : num, 10) - 1;
				_zerotonineBlur(_id.o(ele));
			}
		}
	}
}
function crNum() {
	var createNumBx = "<tr> <td colspan=\"4\" align=\"center\" valign=\"middle\" style=\"line-height:7px\">&nbsp;</td> </tr><tr>";
	for (var i = 0; i < numArr.length; i++) {
		var numBox = " <td width=\"25%\" align=\"center\" valign=\"top\" style=\"padding:0px 5px 0px 5px;\"><table width=\"100%\" height=\"113\" border=\"0\" style=\"border:1px " + _zerotonineColor[0] + " solid;\" bordercolor=\"red\" cellspacing=\"0\" cellpadding=\"0\"  class=\"borderboxzerotonine\" bgcolor=\"" + _zerotonineColor[2] + "\"><tr> <td colspan=\"3\" align=\"center\" valign=\"middle\"><img src=\"" + _zerotonineImgPath + i+ ".gif\" alt=\"" + numArr[i].toUpperCase() + "\"/></td></tr><tr> <td width=\"35\" height=\"25\" align=\"center\" valign=\"middle\"><img src=\"" + _zerotonineImgPath + "minus.gif\"  style=\"cursor:pointer\" alt=\"minus\" onclick=\"selImagezerotonine('" + numArr[i] + "','minus')\"/></td><td width=\"40\" align=\"left\" ><" + _zerotonineInpTxt + " name=\"" + numArr[i] + "\" id=\"" + numArr[i] + "\"/> </td> <td width=\"35\" align=\"center\" valign=\"middle\"><img src=\"" + _zerotonineImgPath + "plus.gif\" alt=\"Plus\" onclick=\"selImagezerotonine('" + numArr[i] + "','plus')\" style=\"cursor:pointer\"/></td>   </td></tr></table></td>";
		if (i % 4 == 0 && i != 0) {
			createNumBx = createNumBx + "</tr><tr> <td colspan=\"4\" align=\"center\" valign=\"middle\" style=\"line-height:10px\">&nbsp;</td> </tr><tr>" + numBox;
		} else {
			createNumBx = createNumBx + numBox;
		}
	}
	var bottomTlb = "<td colspan=\"2\" valign=\"top\" style=\"padding:0px 5px 0px 5px;\"><table width=\"100%\" height=\"113\" border=\"0\" bordercolor=\"#000000\" cellspacing=\"0\" cellpadding=\"0\" bgcolor=\"" + _zerotonineColor[0] + "\" style=\"border:1px " + _zerotonineColor[3] + " solid\" class=\"borderboxzerotonine\"><tr align=\"left\"><td class=\"style4\" style=\"padding:5px;\" wdith=\"20px\">No. Of Tickets:</td><td class=\"style1\" ><input type=\"text\" maxlength=\"4\" name=\"totalNoOfPanels\" id=\"totalNoOfPanels\" onblur=\"_zerotonineBlur(this)\" onkeydown=\"return keyPresszerotonine(this,event)\" onkeyup=\"return keyPresszerotonine(this,event)\" onmousedown=\"return disClick(this)\" readonly=\"true\" style=\"width: 35px;height:10px;font:12px;\"/></td></tr><tr align=\"left\"><td  style=\"padding:5px;\"><span class=\"style4\">Total Amount:</span></td><td  class=\"style1\"><input type=\"text\"name=\"totalPurchaseAmt\" id=\"totAmt\" value=\"0\" readonly=\"true\" style=\"width: 35px;height:10px;font:12px;\"/></td></tr><tr align=\"left\"><td class=\"style4\"  style=\"padding:5px;\"><div style=\"border: 1px solid #FFFFFF; width: 50px; height: 42px; float: left;padding-top:3px\" align=\"center\">QP<br /><input style=\"border: 1px solid #FFFFFF;\" type=\"checkbox\" id=\"_zerotonineQuickPik\" name=\"isQuickPick\" value=\"2\" onclick=\"_zerotonineQP(this.id)\" /></div></td><td align=\"right\" style=\"padding:5px;\"><input type=\"hidden\" name=\"noOfPanels\" value=\"\" id=\"noOfPanels\"/><input type=\"hidden\" name=\"symbols\" value=\"\" id=\"symbols\"/><img id='buy' style=\"margin-top: 3px; margin-bottom: 1px\"src=\"" + _zerotonineImgPath + "buy_zerotonine.gif\" alt=\"Buy\" style=\"cursor:hand\" onclick=\"return buyTicketzerotonine(this.id)\"/></td></tr></table></td>";
	createNumBx = createNumBx + bottomTlb + "</tr><tr> <td colspan=\"4\" align=\"center\" valign=\"middle\" style=\"line-height:9px\">&nbsp;</td> </tr>";
	return createNumBx;
}
function _zerotonineTab() {
	var line = "<tr><td bgcolor=\"" + _zerotonineColor[0] + "\"><table border=\"0\" bordercolor=\"blue\" width=\"100%\" > <tr ><td align=\"left\" width=\"25%\"><input type=\"hidden\" id=\"freezeTimeId\" value=\"" + _zerotoninefreezeTime + "\"><span class=\"style1\">Next Draw :</span><div id=\"latestDrawTime\" style=\"display:none\"></div></td><td width=\"30%\" align=\"left\"><div id=\"nxtDrawTime\" class=\"style2_zerotonine\"></div></td> <td align=\"right\" width=\"20%\"><div id=\"timeLeft\"><span class=\"style1\">Time Left :</span> </div></td><td align=\"left\" width=\"18%\"><div id=\"curDrwTime\" class=\"style2_zerotonine\"></div></td> <td  rowspan=\"2\" width=\"12%\"><img src=\"" + _zerotonineImgPath + "que.gif\" style=\"cursor:hand\" onClick=\"gameInfo()\" alt=\"Game Info\" /></td> </tr> <tr bgcolor=\"" + _zerotonineColor[0] + "\"> <td align=\"left\"><span class=\"style1\">No. of Draws :</span></td> <td> <select name=\"noOfDraws\" id=\"noOfDraws\" class=\"option_zerotonine\" align=\"left\" onchange=\"_zerotonineCalAmt()\">"+ getActiveDrawOptionList('zerotonine') +"</select></td><td colspan=\"1\" align=\"left\" valign=\"top\"> &nbsp; </td><td align=\"center\"><span class=\"numzerotonine\">" + currSymbol+" " + _zerotoninePrice+ "/-</span></td>  </table></td></tr>";
	var _zerotonineMid = "<div style=\"border:1px " + _zerotonineColor[0] + " solid;background-color:" + _zerotonineColor[1] + ";width:96%;margin-left:8px;margin-top:3px;margin-bottom:1px\"><table width=\"100%\" border=\"0\" bordercolor=\"green\" cellspacing=\"0\" cellpadding=\"0\" bgcolor=\"" + _zerotonineColor[1] + "\">" + line + " <tr> <td colspan=\"6\" align=\"center\" valign=\"top\"> <table width=\"98%\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\" id=\"numbers\" bgcolor=\"" + _zerotonineColor[1] + "\" style=\"margin-top:3px\"> " + crNum() + " </table></td></tr></table></div>";
	return _zerotonineMid;
}
function _zerotonineclearAllBoxes() {
	for (var l = 0; l < numArr.length; l++) {
		var elms = _id.o(numArr[l]);
		elms.disabled = false;
	}
	_id.o("_zerotonineQuickPik").value = "2";
	_id.o("totalNoOfPanels").readOnly = true;
	_id.o('noOfDraws').value=1;
	_id.o(numArr[0]).focus();
}
var _zerotonineNxtDrwTbl = _zerotonineFillNxtDrw();
var _zerotonineHTML = "<div id=\"fortunestrip\">" + drwMenu + "</div><form style=\"background-color:#FFFFFF;border: 1px solid #393939;\"><table  width=\"100%\" border=\"0\" cellspacing=\"0\"cellpadding=\"0\"  bordercolor=\"aqua\" bgcolor=\"" + _zerotonineColor[2] + "\"><tr><td width=\"30%\" align=\"center\" valign=\"top\" onmousedown=\"return disClick(this)\"><input type=\"hidden\" value=\"0\" id=\"counterId\"><input type=\"hidden\" id=\"drawType\" ><input type=\"hidden\" id=\"dateFuture\"><div id=\"winningDispDiv\" style=\"border:1px " + _zerotonineColor[0] + " solid;background-color:" + _zerotonineColor[1] + ";margin-left:5px;margin-top:3px\"></div><div id=\"nxtDrawDiv\" style=\"border:1px " + _zerotonineColor[0] + " solid;background-color:" + _zerotonineColor[1] + ";margin-left:5px;margin-top:5px\"></div></td><td align=\"center\" width=\"70%\" valign=\"top\">" + _zerotonineTab() + "</td></tr></table></form>";

