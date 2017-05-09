
var fortInpTxt = "input type=\"text\" maxlength=\"3\" style=\"font-size: 13px; width: 45px;\" onblur=\"fortBlur(this)\" onkeydown=\"return keyPressFort(this,event)\" onkeyup=\"return keyPressFort(this,event)\" onmousedown=\"return disClick(this)\"";
var signArr = ["Aries", "Taurus", "Gemini", "Cancer", "Leo", "Virgo", "Libra", "Scorpio", "Sagittarius", "Capricorn", "Aquarius", "Pisces"];
var curDrwTimefortune = "";
var _fortunePrice=parseInt(gameDispName["fortune"].unitPrice.split("=")[1],10);
var _fortuneColor = ["#2FADBB", "#DCEDEE", "white", "white"];
function setDefaultfortune() {
	curDrwTimefortune = "";
}
/*
// DISPLAY GAME INFO INTO NEW DIV
function fortuneInfo(){
	var url="fortuneInfo.action";
	_id.o('topId').style.display="block";
	runAccordion('5');
	_id.o('info5Div').style.display="block";
	 _id.i('info5Div','<iframe src="'+url+'" id="tktFrame" style="width:100%;height:100%" frameborder="0"></iframe>' );
	_id.o('info5Div').style.marginLeft="200px";
	_id.o('info5Div').style.marginTop="60px";
}
*/
var mapArr = new Array();
mapArr[1] = "Aries";
mapArr[2] = "Taurus";
mapArr[3] = "Gemini";
mapArr[4] = "Cancer";
mapArr[5] = "Leo";
mapArr[6] = "Virgo";
mapArr[7] = "Libra";
mapArr[8] = "Scorpio";
mapArr[9] = "Sagittarius";
mapArr[10] = "Capricorn";
mapArr[11] = "Aquarius";
mapArr[12] = "Pisces";
function _fortuneWinAppend() {
	if (!_fortuneWin.match("Result Awaited")) {
		_fortuneWin = "Result Awaited=\"\" Nxt" + _fortuneWin;
	}
}
function _fortuneLatestDrawTime() {
	return _fortuneStatus.split("=")[1];
}

//FILLING WINNING RESULT DIV 
function _fortuneFillWinTbl() {
	var winArr = _fortuneWin.split("Nxt");
	var winHtml = "<table width=\"100%\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\" bgcolor=\"#d5faa9\"><tr><td align=\"left\"><div valign=\"middle\" style=\"height:22px;background-color:#749a47;padding-top:3px;font-size:16px;font-family:Arial;font-weight:bold;color:#FFFFFF\">&nbsp;Win Result&nbsp;</div></td></tr><tr><td><table width=\"98%\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\"><tr><td><div style=\"" + scrollColor("#83b34b", "#638d31", "200") + "\"><table width=\"90%\" bgcolor=\"#d5faa9\" border=\"0\"cellspacing=\"0\"cellpadding=\"0\" >";
	var winTab = "";
	for (var i = 0; i < winArr.length - 1; i++) {
		if (!winArr[i].match("Result")) {
			var time = new Date(parseInt(winArr[i].split("=")[0].replace(" ", "")));
			var newTime = time.getDate() + " " + month[time.getMonth()] + " " + (time.getHours() < 10 ? "0" + time.getHours() : time.getHours()) + ":" + (time.getMinutes() < 10 ? "0" + time.getMinutes() : time.getMinutes()) + ":" + (time.getSeconds() < 10 ? "0" + time.getSeconds() : time.getSeconds());
			var img = ((winArr[i].split("=")[1]).split(",")).toString().replace(" ", "").toLowerCase();
			winTab = winTab + "<tr><td width=\"69%\"height=\"40\"align=\"center\"valign=\"middle\" class=\"borderbtmfortune\"><p>" + newTime + "</p><br/><p><span>" + img.toUpperCase() + "</span></p></td><td width=\"31%\"align=\"center\"valign=\"middle\"class=\"borderbtmfortune\"><img src=\"" + fortImgPath + img + "Win.png\"alt=\"" + img.toUpperCase() + "\"/></td></tr>";
		} else {
			winTab = "<tr id=\"result" + i + "\"><td height=\"60\" colspan=\"2\" align=\"left\" valign=\"middle\" class=\"borderbtmzerotonine\" style=\"font-size:14px;\">";
			winTab = winTab + "<table width=\"90%\" bordercolor=\"blue\" border=\"0\" cellspacing=\"2\" cellpadding=\"10\" id=\"result" + i + "\" ><tr><td colspan=\"8\" height=\"40\" align=\"center\" style=\"font-size:16px;\">Result Awaited</td></tr><tr>";
			winTab = winTab + "<td colspan=\"8\" align=\"center\"><div style=\"text-align:center\" id=\"winAwaitedDiv\" style=\"color:" + _fortuneColor[0] + "\"></div></td>";
			winTab = winTab + "</tr></table></td></tr>";
		//winTab =winTab+'<tr id="result'+i+'"><td width="69%" height="60" align="left" valign="middle" style="font-size:12px;font-weight:800">Result Awaited</td><td width="31%" height="60"   align="left"valign="middle"class="borderbtmfortune"><div id="winAwaitedDiv" style="color:#663300;font-size:12px;font-weight:800""></div></td></tr>';
		}
	}
	winHtml = winHtml + winTab + "</table></div></td></tr></table></td></tr></table>";
	return winHtml;
}

// FILLING NEXT DRAWS DIV
function _fortuneFillNxtDrw() {
	var nxtDrwHTML = "<table cellspacing=\"0\" cellpadding=\"0\" width=\"100%\" bgcolor=\"#83b34b\"><tr><td align=\"left\"><div valign=\"middle\" style=\"height:22px;background-color:#749a47;padding-top:3px;font-size:16px;font-family:Arial;font-weight:bold;color:#FFFFFF\">&nbsp;Next Draw&nbsp;</div></td></tr><tr><td valign=\"top\"><table width=\"99%\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\" bgcolor=\"#83b34b\"><tr><td><div style=\"" + scrollColor("#83b34b", "#638d31", "167") + "\"><table width=\"90%\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\" class=\"nextdrawfortune\">";
	var nxtDrwArr = _fortuneDrawTime.split(",");
	for (var i = 0, l = nxtDrwArr.length; i < l; i++) {
		var time = new Date(parseInt(nxtDrwArr[i].replace(" ", "")));
		var newTime = time.getDate() + " " + month[time.getMonth()] + " " + (time.getHours() < 10 ? "0" + time.getHours() : time.getHours()) + ":" + (time.getMinutes() < 10 ? "0" + time.getMinutes() : time.getMinutes());
		var hiddenTime = "<input type='text'  id='timer" + i + "'>";
		var rowcolor;
		var data = "";
		if (i == 0) {
			data = time.getFullYear() + "-" + time.getMonth() + "-" + time.getDate() + "-" + (time.getHours() < 10 ? "0" + time.getHours() : time.getHours()) + "-" + (time.getMinutes() < 10 ? "0" + time.getMinutes() : time.getMinutes()) + "-" + (time.getSeconds() < 10 ? "0" + time.getSeconds() : time.getSeconds());
			_fortuneNxtDrawTime = ("" + time).substring(0, ("" + time).lastIndexOf(":") + 3);
		}
		if (i % 2 == 0) {
			rowcolor = "#d5faa9";
		} else {
			rowcolor = "#9ACD5E";
		}
		nxtDrwHTML = nxtDrwHTML + "<tr><td><input type=\"hidden\" value=\"" + data + "\" id=\"" + i + "timer\"></td></tr><tr bgcolor=\"" + rowcolor + "\" style=\"height: 20px\"><td width=\"100%\" class=\"borderbtmfortune\"><div id=\"" + i + "blink\">" + newTime + "</div></td><td width=\"44%\"><p><div id=\"nxtDrwD" + i + "\" style=\"display:none\"><img src=\"" + fortImgPath + "freezed.gif\"></div></p></td></tr>";
	}
	nxtDrwHTML = nxtDrwHTML + "</table></div></td></tr></table></td></tr></table>";
	return nxtDrwHTML;
}

// BUY TICKET
function buyTicketfortune(id) {
	var obj = _id.o("sunsign");
	var noOfDraws = document.getElementsByName("noOfDraws")[0].value;
	var isQP = _id.o("fortQuickPik").value;
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
	_fortuneCalAmt();
	var amount = _id.o("totAmt").value;
	if (_id.o("totAmt").value == 0 || _id.o("totalNoOfPanels").value == 0) {
		alert("Please fill complete entries");
		if (_id.o("fortQuickPik").checked) {
			_id.o("totalNoOfPanels").focus();
		}
		return false;
	}
	
	// updated by pavan
	document.forms[0].reset();
	_id.o(id).disabled=true;
	gameBuyAjaxReq("fortuneBuy.action?noOfDraws=" + noOfDraws + "&symbols=" + symbols + "&noOfPanels=" + noOfPanels + "&isQuickPick=" + isQP + "&totalNoOfPanels=" + totalPanels + "&totalPurchaseAmt=" + amount);
	fortuneclearAllBoxes();
	return true;
}
function fortBlur(val) {
	//alert(val.value);
	if (!_id.o("fortQuickPik").checked) {
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
			val.value = "";
		}
		if (isNaN(_id.o("totalNoOfPanels").value)) {
			_id.o("totalNoOfPanels").value = "";
		}
	}
	_fortuneCalAmt();
}
function fortQP(id) {
	var obj = _id.o("sunsign");
	var elms = obj.getElementsByTagName("input");
	if (_id.o("fortQuickPik").checked) {
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
	_fortuneCalAmt();
}
function keyPressFort(val,e) {
	keyCode = (window.event) ? event.keyCode : e.which;
	//alert(keyCode)
	if ((keyCode >= 48 && keyCode < 58) || keyCode == 45 || keyCode == 27 || keyCode == 13 || keyCode == 40 || keyCode == 38 || (keyCode >= 96 && keyCode < 106 || keyCode == 46 || keyCode == 8 || keyCode == 9)) {
		fortBlur(val);
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
function _fortuneUpData(win, drwTime, drwStatus) {
	_fortuneWin = win;
	_fortuneDrawTime = drwTime;
	_fortuneStatus = drwStatus;
}
function _fortuneCalAmt() {
	_id.o("totAmt").value = _id.v("totalNoOfPanels") * _fortunePrice * _id.v("noOfDraws");
}
function selImageFortune(ele, func) {
	var num = _id.v(ele);
	var totPanel = _id.v("totalNoOfPanels");
	if (!_id.o("fortQuickPik").checked) {
		if (func == "plus") {
			if (num < 999 && totPanel < 1000) {
				_id.o(ele).value = parseInt(num == "" ? 0 : num, 10) + 1;
				fortBlur(_id.o(ele));
			}
		} else {
			if (num > 0) {
				_id.o(ele).value = parseInt(num == "" ? 0 : num, 10) - 1;
				fortBlur(_id.o(ele));
			}
		}
	}
}
function crSign() {
	var createSignBx = "<tr> <td colspan=\"3\" align=\"center\" valign=\"middle\" style=\"line-height:3px\">&nbsp;</td> </tr><tr>";
	for (var i = 0; i < signArr.length; i++) {
		var signBox = " <td width=\"25%\" align=\"center\" valign=\"top\"><table width=\"111\" border=\"0\" style=\"border:1px #749a47 solid;\" bordercolor=\"red\" cellspacing=\"0\" cellpadding=\"0\"  class=\"borderbox\" bgcolor=\"#9acd5e\"><tr> <td colspan=\"3\" align=\"center\" valign=\"top\"><img src=\"" + fortImgPath + signArr[i].toLowerCase() + ".gif\" alt=\"" + signArr[i].toUpperCase() + "\"/></td></tr><tr> <td width=\"35\" height=\"25\" align=\"center\" valign=\"middle\"><img src=\"" + fortImgPath + "minus.gif\"  style=\"cursor:pointer\" alt=\"minus\" onclick=\"selImageFortune('" + signArr[i] + "','minus')\"/></td><td width=\"40\" align=\"left\" ><" + fortInpTxt + " name=\"" + signArr[i] + "\" id=\"" + signArr[i] + "\"/> </td> <td width=\"35\" align=\"center\" valign=\"middle\"><img src=\"" + fortImgPath + "plus.gif\" alt=\"Plus\" onclick=\"selImageFortune('" + signArr[i] + "','plus')\" style=\"cursor:pointer\"/></td>   </td></tr></table></td>";
		if (i % 4 == 0 && i != 0) {
			createSignBx = createSignBx + "</tr><tr> <td colspan=\"3\" align=\"center\" valign=\"middle\" style=\"line-height:5px\">&nbsp;</td> </tr><tr>" + signBox;
		} else {
			createSignBx = createSignBx + signBox;
		}
	}
	createSignBx = createSignBx + "</tr>";
	return createSignBx;
}
function fortuneTab() {
	var line = "<tr><td bgcolor=\"#749a47\"><table border=\"0\" bordercolor=\"blue\" width=\"100%\"> <tr ><td align=\"left\" width=\"25%\"><input type=\"hidden\" id=\"freezeTimeId\" value=\"" + _fortunefreezeTime + "\"><span class=\"style1\">Next Draw :</span><div id=\"latestDrawTime\" style=\"display:none\"></div></td><td width=\"30%\" align=\"left\"><div id=\"nxtDrawTime\" class=\"style2\"></div></td> <td align=\"right\" width=\"20%\"><div id=\"timeLeft\"><span class=\"style1\">Time Left :</span> </div></td><td align=\"left\" width=\"18%\"><div id=\"curDrwTime\" class=\"style2\"></div></td> <td  rowspan=\"2\" width=\"12%\"><img src=\"" + fortImgPath + "que.gif\" style=\"cursor:hand\" onClick=\"gameInfo()\" alt=\"Game Info\" /></td> </tr> <tr bgcolor=\"#749a47\"> <td align=\"left\"><span class=\"style1\">No. of Draws :</span></td> <td> <select name=\"noOfDraws\" id=\"noOfDraws\" class=\"fortune_option\" align=\"left\" onchange=\"_fortuneCalAmt()\">"+ getActiveDrawOptionList('fortune') +"</select></td><td colspan=\"1\" align=\"left\" valign=\"top\"> &nbsp; </td><td align=\"center\"><span class=\"num\">"+ currSymbol+" " + _fortunePrice+ "/-</span></td>  </table></td></tr>";
	var fortuneMid = "<div style=\"border:1px #749a47 solid;background-color:#d5faa9;width:96%;margin-left:8px;margin-top:3px;margin-bottom:1px\"><table width=\"100%\" border=\"0\" bordercolor=\"green\" cellspacing=\"0\" cellpadding=\"0\" bgcolor=\"#d5faa9\">" + line + " <tr> <td colspan=\"6\" align=\"center\" valign=\"top\"> <table width=\"98%\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\" id=\"sunsign\" bgcolor=\"#d5faa9\" style=\"margin-top:3px\"> " + crSign() + " </table></td></tr><tr> <td colspan=\"6\" align=\"center\" valign=\"top\" style=\"margin-top:2px;\"> \t\t\t<table width=\"100%\" border=\"0\" cellspacing=\"0\" align=\"center\"\t\t\tcellpadding=\"0\" \t\t\tbgcolor=\"#749a47\"><tr><td bgcolor=\"#d5faa9\" colspan=\"7\" align=\"center\" valign=\"middle\" style=\"line-height:3px\">&nbsp;</td></tr> <tr> <td width=\"17%\" height=\"28\" class=\"style4\"> Quick Pick : </td> <td \t\t\t\t\t\twidth=\"9%\"> <input type=\"checkbox\" id=\"fortQuickPik\"name=\"isQuickPick\"value=\"2\" onclick=\"fortQP(this.id)\"/>\t\t\t</td> <td width=\"23%\" class=\"style4\"> No. Of Tickets : \t\t\t</td> \t\t\t<td width=\"10%\"> <input type=\"text\" \tmaxlength=\"3\" name=\"totalNoOfPanels\" id=\"totalNoOfPanels\" onblur=\"fortBlur(this)\" onkeydown=\"return keyPressFort(this,event)\" onkeyup=\"return keyPressFort(this,event)\" onmousedown=\"return disClick(this)\" readonly=\"true\"\t\tstyle=\"width: 35px\" /> </td> <td width=\"21%\"> <span \t\t\t\t\t\tclass=\"style4\">Total Amount :</span> </td> <td \t\t\twidth=\"8%\"> <input type=\"text\" name=\"totalPurchaseAmt\"id=\"totAmt\"value=\"0\"readonly=\"true\"size=\"3\" style=\"width: 30px\" /> \t\t\t</td> \t\t\t<td width=\"12%\" align=\"center\"> <input type=\"hidden\"name=\"noOfPanels\"value=\"\"id=\"noOfPanels\"/><input type=\"hidden\"name=\"symbols\"value=\"\"id=\"symbols\"/><img id='buy' style=\"margin-top:3px;margin-bottom:1px\"\tsrc=\"" + fortImgPath + "buy.gif\" width=\"45\" height=\"25\" alt=\"Buy\" style=\"cursor:hand\" onclick=\"return buyTicketfortune(this.id)\"/> </td> </tr> </table> \t\t\t</td> </tr>  </table></div>";
	return fortuneMid;
}
function blurFnFort() {
	_fortuneCalAmt();
}
var gameChildW = null;
function fortuneGameInfo() {
	if (gameChildW) {
	} else {
		gameChildW = window.open("fortune/gameInfo.jsp", "NewWin", "width=650,height=550,toolbar=0,resizable=0,scrollbars=1,alwaysRaised=0,location=0,menubar=0");
		gameChildW.close();
	}
	gameChildW = window.open("fortune/gameInfo.jsp", "NewWin", "width=650,height=550,toolbar=0,resizable=0,scrollbars=1,alwaysRaised=0,location=0,menubar=0");
}
function fortuneclearAllBoxes() {
	var obj = _id.o("sunsign");
	var elms = obj.getElementsByTagName("input");
	for (var l = 0; l < elms.length; l++) {
		elms[l].disabled = false;
	}
	_id.o("totalNoOfPanels").readOnly = true;
	_id.o("sunsign").disabled = false;
	_id.o("fortQuickPik").value = "2";
	_id.o('noOfDraws').value=1;
	elms[0].focus();
}
var _fortuneNxtDrwTbl = _fortuneFillNxtDrw();
var _fortuneHTML = "<div id=\"strip\">" + drwMenu + "</div><form style=\"background-color:#FFFFFF;border: 1px solid #393939;\"><table  width=\"100%\" border=\"0\" cellspacing=\"0\"cellpadding=\"0\"  bordercolor=\"aqua\" bgcolor=\"#9acd5e\"><tr><td width=\"30%\" align=\"center\" valign=\"top\" onmousedown=\"return disClick(this)\"><input type=\"hidden\" value=\"0\" id=\"counterId\"><input type=\"hidden\" id=\"drawType\" ><input type=\"hidden\" id=\"dateFuture\"><div id=\"winningDispDiv\" style=\"border:1px #749a47 solid;background-color:#83b34b;margin-left:5px;margin-top:3px\"></div><div id=\"nxtDrawDiv\" style=\"border:1px #749a47 solid;background-color:#83b34b;margin-left:5px;margin-top:3px\"></div></td><td align=\"center\" width=\"70%\" valign=\"top\"  bgcolor=\"#9acd5e\">" + fortuneTab() + "</td></tr></table></form>";


parent.frames[0].Version['$Source: /rep/LMS_Mgmt/WebRoot/com/skilrock/lms/web/drawGames/playMgmt/games/fortune/fortune.js,v $'] = '$Id: fortune.js,v 1.2 2016/10/31 09:46:38 neeraj Exp $';
