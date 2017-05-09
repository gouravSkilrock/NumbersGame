
var card16InpTxt = "input type=\"text\" maxlength=\"3\" style=\"font-size: 13px; width: 45px;color:#3F464A;font-weight:bold\" onblur=\"card16Blur(this)\" onkeydown=\"return keyPressCard16(this,event)\" onkeyup=\"return keyPressCard16(this,event)\" onmousedown=\"return disClick(this)\"";
var signArr = ["ace_spade", "ace_heart", "ace_diamond", "ace_club", "king_spade", "king_heart", "king_diamond", "king_club", "queen_spade", "queen_heart", "queen_diamond", "queen_club", "jack_spade", "jack_heart", "jack_diamond", "jack_club"];
var curDrwTimecard16 = "";
var _card16Price=parseInt(gameDispName["card16"].unitPrice.split("=")[1],10);
function setDefaultcard16() {
	curDrwTimefortune = "";
}
/*
// DISPLAY GAME INFO INTO NEW DIV
function card16Info(){
var url="card16Info.action";
_id.o('topId').style.display="block";
runAccordion('5');
_id.o('info5Div').style.display="block";
 _id.i('info5Div','<iframe src="'+url+'" id="tktFrame" style="width:100%;height:100%" frameborder="0"></iframe>' );
_id.o('info5Div').style.marginLeft="200px";
_id.o('info5Div').style.marginTop="60px";

}
*/
function _card16WinAppend() {
	if (!_card16Win.match("Result Awaited")) {
		_card16Win = "Result Awaited=\"\" Nxt" + _card16Win;
	}
}
function _card16LatestDrawTime() {
	return _card16Status.split("=")[1];
}

//FILLING WINNING RESULT DIV 
function _card16FillWinTbl() {
	var winArr = _card16Win.split("Nxt");
	var winHtml = "<table width=\"100%\" border=\"0\" bordercolor=\"red\" cellspacing=\"0\" cellpadding=\"0\" align=\"center\" bgcolor=\"#ebeaea\"><tr><td colspan=\"2\" align=\"left\"><div valign=\"middle\" style=\"height:22px;background-color:#970303;padding-top:3px;font-size:16px;font-family:Arial;font-weight:bold;color:#FFFFFF\">&nbsp;Win Result&nbsp;</div></td></tr><tr><td valign=\"top\" align=\"center\" height=\"100\">";
	winHtml = winHtml + "<table width=\"100%\" border=\"0\" bordercolor=\"red\" style=\"margin-top:3px\"><tr><td><div style=\"" + scrollColor("#c2c2c2", "#970303", "184") + "\">";
	var winTab = "";
	for (var i = 0; i < winArr.length - 1; i++) {
		winTab = winTab + "<table width=\"90%\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\" bordercolor=\"red\" style=\"margin:5px 1px 5px 1px;border:1px red solid\" bgcolor=\"#FFFFFF\">";
		if (!winArr[i].match("Result")) {
			var time = new Date(parseInt(winArr[i].split("=")[0].replace(" ", "")));
			var newTime = time.getDate() + " " + month[time.getMonth()] + " " + (time.getHours() < 10 ? "0" + time.getHours() : time.getHours()) + ":" + (time.getMinutes() < 10 ? "0" + time.getMinutes() : time.getMinutes()) + ":" + (time.getSeconds() < 10 ? "0" + time.getSeconds() : time.getSeconds());
			var img = ((winArr[i].split("=")[1]).split(",")).toString().replace(" ", "").toLowerCase();
			winTab = winTab + "<tr><td width=\"69%\" align=\"center\"valign=\"middle\" class=\"borderbtmcard16\"><p>" + newTime + "</p><br/><p><span style=\"font-weight: bold;\">" + img.toUpperCase() + "</span></p></td><td width=\"31%\"align=\"center\"valign=\"middle\" class=\"borderbtmcard16\"><img src=\"" + card16ImgPath + img + "Win.gif\"alt=\"" + img.toUpperCase() + "\"/></td></tr>";
		} else {
			winTab = winTab + "<tr id=\"result" + i + "\"><td height=\"30\" align=\"left\" valign=\"middle\" class=\"borderbtmcard16\" style=\"font-size:14px;\">Result Awaited</td></tr><tr><td height=\"23\" class=\"borderbtmcard16\" valign=\"middle\"><div id=\"winAwaitedDiv\" style=\"color:#970303;font-size:12px;font-weight:800;\"></div></td></tr>";
		}
		winTab = winTab + "</table>";
	}
	winHtml = winHtml + winTab + "</div></td></tr></table></td></tr></table>";
	return winHtml;
}

// FILLING NEXT DRAWS DIV
function _card16FillNxtDrw() {
	var nxtDrwHTML = "<table width=\"100%\" cellspacing=\"0\" cellpadding=\"0\" border=\"0\" bordercolor=\"red\"><tr><td colspan=\"2\" align=\"left\" valign=\"top\"><div valign=\"middle\" style=\"height:22px;width:180px;background-color:#970303;padding-top:3px;font-size:16px;font-family:Arial;font-weight:bold;color:#FFFFFF\">&nbsp;Next Draw&nbsp;</div></td></tr><tr><td align=\"center\"valign=\"top\"><table width=\"100%\" bordercolor=\"red\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\" bgcolor=\"#ffffd9\"><tr><td width=\"100%\" align=\"left\" valign=\"top\"><div style=\"" + scrollColor("#c2c2c2", "#970303", "175") + "\"><table width=\"90%\" align=\"left\" bordercolor=\"green\"  border=\"0\" cellspacing=\"0\" cellpadding=\"0\" class=\"borderbtmcard16\" >";
	var nxtDrwArr = _card16DrawTime.split(",");
	for (var i = 0, l = nxtDrwArr.length; i < l; i++) {
		var time = new Date(parseInt(nxtDrwArr[i].replace(" ", "")));
		var newTime = time.getDate() + " " + month[time.getMonth()] + " " + (time.getHours() < 10 ? "0" + time.getHours() : time.getHours()) + ":" + (time.getMinutes() < 10 ? "0" + time.getMinutes() : time.getMinutes());
		var hiddenTime = "<input type='text'  id='timer" + i + "'>";
		var rowcolor;
		var data = "";
		if (i == 0) {
			data = time.getFullYear() + "-" + time.getMonth() + "-" + time.getDate() + "-" + (time.getHours() < 10 ? "0" + time.getHours() : time.getHours()) + "-" + (time.getMinutes() < 10 ? "0" + time.getMinutes() : time.getMinutes()) + "-" + (time.getSeconds() < 10 ? "0" + time.getSeconds() : time.getSeconds());
			_card16NxtDrawTime = ("" + time).substring(0, ("" + time).lastIndexOf(":") + 3);
		}
		if (i % 2 == 0) {
			rowcolor = "#FFFFFF";
		} else {
			rowcolor = "#EBEAEA";
		}
		nxtDrwHTML = nxtDrwHTML + "<tr><td width=\"100%\"><input type=\"hidden\" value=\"" + data + "\" id=\"" + i + "timer\"></td></tr><tr bgcolor=\"" + rowcolor + "\" style=\"height: 20px;\"><td width=\"100%\" align=\"center\" class=\"borderbtmcard16\" colspan=\"2\" style=\"border-bottom:1px red solid;\"><div id=\"" + i + "blink\" >" + newTime + "</div></td><td width=\"44%\" align=\"center\"><p><div id=\"nxtDrwD" + i + "\" style=\"display:none\"><img src=\"" + fortImgPath + "freezed.gif\"></div></p></td></tr>";
	}
	nxtDrwHTML = nxtDrwHTML + "</table></div></td></tr></table></td></tr></table>";
	return nxtDrwHTML;
}

// BUY TICKET
function buyTicketcard16(id) {
	var obj = _id.o("sunsign");
	var noOfDraws = document.getElementsByName("noOfDraws")[0].value;
	var isQP = _id.o("card16QuickPik").value;
	var elms = obj.getElementsByTagName("input");
	var sym = "", noOfPan = "";
	for (var l = 0; l < elms.length; l++) {
		if (elms[l].value != "" && elms[l].value != 0) {
			sym = sym + "," + elms[l].name;
			noOfPan = noOfPan + "," + elms[l].value;
		}
	}
	_id.o("symbols").value = sym.replace(",", "");
	_id.o("noOfPanels").value = noOfPan.replace(",", "");
	var symbols = _id.o("symbols").value;
	var noOfPanels = _id.o("noOfPanels").value;
	var totalPanels = _id.o("totalNoOfPanels").value;
	_card16CalAmt();
	var amount = _id.o("totAmt").value;
	if (_id.o("totAmt").value == 0 || _id.o("totalNoOfPanels").value == 0) {
		alert("Please fill complete entries");
		if (_id.o("card16QuickPik").checked) {
			_id.o("totalNoOfPanels").focus();
		}
		return false;
	}

// updated by pavan
	document.forms[0].reset();
	_id.o(id).disabled=true;
	gameBuyAjaxReq("card16Buy.action?noOfDraws=" + noOfDraws + "&symbols=" + symbols + "&noOfPanels=" + noOfPanels + "&isQuickPick=" + isQP + "&totalNoOfPanels=" + totalPanels + "&totalPurchaseAmt=" + amount);
	card16clearAllBoxes();
	return true;
}
function card16Blur(val) {
	if (!_id.o("card16QuickPik").checked) {
		var obj = _id.o("sunsign");
		var isExceed = false;
		var elms = obj.getElementsByTagName("input");
		var tot = 0;
		for (var l = 0; l < elms.length; l++) {
			if (elms[l].value != "") {
				if (isNaN(elms[l].value)) {
					elms[l].value = "";
				} else {
					tot = tot + parseInt(elms[l].value,10);
					if (tot > 999 && val) {
						val.value = "";
						alert("Cannot Select More than 999 Panels ");
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
	_card16CalAmt();
}
function card16QP(id) {
	var obj = _id.o("sunsign");
	var elms = obj.getElementsByTagName("input");
	if (_id.o("card16QuickPik").checked) {
		for (var l = 0; l < elms.length; l++) {
			elms[l].disabled = true;
			elms[l].value = "";
		}
		_id.o("totalNoOfPanels").readOnly = false;
		_id.o(id).value = "1";
		_id.o("totalNoOfPanels").focus();
		_id.o("sunsign").disabled = true;
	} else {
		for (var l = 0; l < elms.length; l++) {
			elms[l].disabled = false;
		}
		_id.o("totalNoOfPanels").readOnly = true;
		_id.o("sunsign").disabled = false;
		_id.o(id).value = "2";
		elms[0].focus();
	}
	_id.o("totalNoOfPanels").value = "";
	_card16CalAmt();
}
function keyPressCard16(val, e) {
	var keyCode = (window.event) ? event.keyCode : e.which;
	if ((keyCode >= 48 && keyCode < 58) || keyCode == 45 || keyCode == 27 || keyCode == 13 || keyCode == 40 || keyCode == 38 || (keyCode >= 96 && keyCode < 106 || keyCode == 46 || keyCode == 8 || keyCode == 9)) {
		card16Blur(val);
		if (keyCode == 46 || keyCode == 8 || keyCode == 9) {
			return true;
		}
		if(val.value==0){
			val.value="";
			}
		if (val.value != 0 || val.value != "" || !isNaN(val.value)) {
			var obj = _id.o("sunsign");
			var clrChg = true;
			var elms = obj.getElementsByTagName("input");
			for (var i = 0; i < elms.length; i++) {
				if (val.value.length == 3) {
					if (elms[i].value == "") {
						elms[i].focus();
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
function _card16UpData(win, drwTime, drwStatus) {
	_card16Win = win;
	_card16DrawTime = drwTime;
	_card16Status = drwStatus;
}
function _card16CalAmt() {
	_id.o("totAmt").value = _id.v("totalNoOfPanels") * _card16Price * _id.v("noOfDraws");
}
function selImageCard16(ele, func) {
	var num = _id.v(ele);
	var totPanel = _id.v("totalNoOfPanels");
	if (!_id.o("card16QuickPik").checked) {
		if (func == "plus") {
			if (num < 999 && totPanel < 1000) {
				_id.o(ele).value = parseInt(num == "" ? 0 : num, 10) + 1;
				card16Blur(_id.o(ele));
			}
		} else {
			if (num > 0) {
				_id.o(ele).value = parseInt(num == "" ? 0 : num, 10) - 1;
				card16Blur(_id.o(ele));
			}
		}
	}
}
function card16crSign() {
	var createSignBx = "<tr>";
	for (var i = 0; i < signArr.length; i++) {
		var signBox = " <td width=\"25%\" align=\"center\" valign=\"top\"><table width=\"111\" border=\"0\" bordercolor=\"red\" cellspacing=\"0\" cellpadding=\"0\"  class=\"card16borderbox\"><tr> <td colspan=\"3\" align=\"center\" valign=\"top\"><img src=\"" + card16ImgPath + signArr[i].toLowerCase() + "16.gif\" alt=\"" + signArr[i].toUpperCase() + "\" /></td></tr><tr><td style=\"line-height:1px\"></td></tr><tr> <td width=\"35\" height=\"25\" align=\"center\" valign=\"middle\"><img src=\"" + card16ImgPath + "minus.gif\"  style=\"cursor:pointer\" alt=\"minus\" onclick=\"selImageCard16('" + signArr[i] + "','minus')\"/></td><td width=\"40\" align=\"left\" ><" + card16InpTxt + " name=\"" + signArr[i] + "\" id=\"" + signArr[i] + "\"/> </td> <td width=\"35\" align=\"center\" valign=\"middle\"><img src=\"" + card16ImgPath + "plus.gif\" alt=\"Plus\" onclick=\"selImageCard16('" + signArr[i] + "','plus')\" style=\"cursor:pointer\"/></td>   </td></tr><tr><td style=\"line-height:1px\"></td></tr></table></td>";
		if (i % 4 == 0 && i != 0) {
			createSignBx = createSignBx + "</tr><tr>" + signBox;
		} else {
			createSignBx = createSignBx + signBox;
		}
	}
	createSignBx = createSignBx + "</tr>";
	return createSignBx;
}
function card16Tab() {
	var line = "<tr><td bgcolor=\"#970303\"><table border=\"0\" bordercolor=\"blue\" width=\"100%\"> <tr ><td align=\"left\" width=\"25%\"><input type=\"hidden\" id=\"freezeTimeId\" value=\"" + _card16freezeTime + "\"><span class=\"style1\">Next Draw :</span><div id=\"latestDrawTime\" style=\"display:none\"></div></td><td width=\"30%\" align=\"left\"><div id=\"nxtDrawTime\" class=\"card16style2\"></div></td> <td align=\"right\" width=\"20%\"><div id=\"timeLeft\"><span class=\"style1\">Time Left :</span> </div></td><td align=\"left\" width=\"18%\"><div id=\"curDrwTime\" class=\"card16style2\"></div></td> <td  rowspan=\"2\" width=\"12%\"><img src=\"" + card16ImgPath + "que.gif\" style=\"cursor:hand\" onClick=\"gameInfo()\" alt=\"Game Info\" /></td> </tr><tr bgcolor=\"#970303\"><td align=\"left\"><span class=\"style1\">No. of Draws :</span></td> <td> <select name=\"noOfDraws\" id=\"noOfDraws\" class=\"fortune_option\" align=\"left\" onchange=\"_card16CalAmt()\">"+ getActiveDrawOptionList('card16') +"</select></td><td colspan=\"1\" align=\"left\" valign=\"top\"> &nbsp; </td><td align=\"center\"><span class=\"card16num\">" + currSymbol+" " + _card16Price+ "/-</span></td> </table></td></tr>";
	var card16Mid = "<div id=\"middlecard12\"><table width=\"100%\" border=\"0\" bordercolor=\"green\" cellspacing=\"0\" cellpadding=\"0\" bgcolor=\"#EBEAEA\">" + line + "<tr><td colspan=\"6\" align=\"center\" valign=\"top\"> <table width=\"98%\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\" id=\"sunsign\" bgcolor=\"#EBEAEA\" style=\"margin-top:4px;margin-bottom:4px;\"> " + card16crSign() + " </table></td></tr><tr><td colspan=\"6\" align=\"center\" valign=\"top\" style=\"margin-top:4px;\"><table width=\"100%\" border=\"0\" cellspacing=\"0\" align=\"center\" cellpadding=\"0\" bgcolor=\"#970303\"><tr> <td width=\"17%\" height=\"28\" class=\"style4\" align=\"right\"> Quick Pick : </td> <td width=\"9%\" align=\"left\"> <input type=\"checkbox\" style=\"border:1px #970303 solid\" id=\"card16QuickPik\" name=\"isQuickPick\"value=\"2\" onclick=\"card16QP(this.id)\"/></td> <td width=\"23%\" class=\"style4\" align=\"right\"> No. Of Tickets : \t&nbsp;\t\t</td> \t\t\t<td width=\"10%\" align=\"left\"> <input type=\"text\" \tmaxlength=\"3\"name=\"totalNoOfPanels\"id=\"totalNoOfPanels\"onkeydown=\"return keyPressCard16(this,event)\" onkeyup=\"return keyPressCard16(this,event)\" onblur=\"card16Blur(this)\" readonly=\"true\"\t\tstyle=\"width: 30px;color:#970303;font-weight:bold\"  /> </td> <td width=\"21%\"  class=\"style4\" align=\"right\"> Total Amount :&nbsp;</td> <td width=\"8%\" align=\"left\"> <input type=\"text\" name=\"totalPurchaseAmt\"id=\"totAmt\"value=\"0\"readonly=\"true\"size=\"3\" style=\"width: 30px;color:#970303;font-weight:bold\" /> \t\t\t</td> \t\t\t<td width=\"12%\" align=\"center\"> <input type=\"hidden\"name=\"noOfPanels\"value=\"\"id=\"noOfPanels\"/><input type=\"hidden\"name=\"symbols\"value=\"\"id=\"symbols\"/><img id='buy' style=\"margin-top:3px;margin-bottom:1px\"\tsrc=\"" + card16ImgPath + "buy_red.gif\" width=\"45\" height=\"25\" alt=\"Buy\" style=\"cursor:hand\" onclick=\"return buyTicketcard16(this.id)\"/> </td> </tr> </table> \t\t\t</td> </tr></table> </div>";
	return card16Mid;
}
function blurFnCard16() {
	_card16CalAmt();
}
var gameChildW = null;
function card16GameInfo() {
	if (gameChildW) {
	} else {
		gameChildW = window.open("card16/gameInfo.jsp", "NewWin", "width=650,height=550,toolbar=0,resizable=0,scrollbars=1,alwaysRaised=0,location=0,menubar=0");
		gameChildW.close();
	}
	gameChildW = window.open("card16/gameInfo.jsp", "NewWin", "width=650,height=550,toolbar=0,resizable=0,scrollbars=1,alwaysRaised=0,location=0,menubar=0");
}
function card16clearAllBoxes() {
	var obj = _id.o("sunsign");
	var elms = obj.getElementsByTagName("input");
	for (var l = 0; l < elms.length; l++) {
		elms[l].disabled = false;
	}
	_id.o("totalNoOfPanels").readOnly = true;
	_id.o("sunsign").disabled = false;
	_id.o("card16QuickPik").value = "2";
	_id.o('noOfDraws').value=1;
	elms[0].focus();
}
var _card16NxtDrwTbl = _card16FillNxtDrw();
var _card16HTML = "<div id=\"card16strip\">" + drwMenu + "</div><form style=\"background-color:#FFFFFF;border: 1px solid #393939;\"><table  border=\"0\" width=\"100%\" height=\"100%\" cellspacing=\"0\" cellpadding=\"0\" align=\"center\" bgcolor=\"#D70030\" bordercolor=\"red\"><tr><td width=\"10%\"  valign=\"top\" align=\"center\" onmousedown=\"return disClick(this)\"><input type=\"hidden\" value=\"0\" id=\"counterId\"><input type=\"hidden\" id=\"drawType\" ><input type=\"hidden\" id=\"dateFuture\"><div id=\"winningDispDiv\" style=\"border:1px red solid;background-color:#EBEAEA;margin-left:5px;margin-top:3px\"></div><div id=\"nxtDrawDiv\" style=\"border:1px red solid;background-color:#EBEAEA;margin-left:5px;margin-top:5px\"></div></td><td width=\"90%\" valign=\"top\" align=\"right\"><div id=\"card16middle\" style=\"border:1px red solid;background-color:#EBEAEA;width:98%;margin-left:5px;margin-top:3px;margin-bottom:1px\">" + card16Tab() + "</div></td></tr></table></td></tr></table></form>";

parent.frames[0].Version['$Source: /rep/LMS_Mgmt/WebRoot/com/skilrock/lms/web/drawGames/playMgmt/games/card16/card16.js,v $'] = '$Id: card16.js,v 1.14 2016/10/31 09:46:59 neeraj Exp $';