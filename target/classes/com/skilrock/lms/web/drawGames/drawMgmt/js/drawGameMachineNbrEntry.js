

var newFormatTime = "";
var newFormatDate = "";
var year = "";
var day = "";
var month = "";
var hour = "";
var min = "";
var sec = "";
var maxValue = 0;
var isBonus = false;
function scrollColor(a, b, height) {
	return "height:" + height + "px;overflow-y: auto;scrollbar-arrow-color:" + a + ";scrollbar-base-color:" + b + ";scrollbar-darkshadow-color:" + a + ";scrollbar-face-color:" + b + "; scrollbar-highlight-color:" + a + "; scrollbar-shadow-color:" + a + "";
}
function getDateString() {
}
function makeHiddenPerformStatus() {
	var drawsArr = _id.n("drawIds");
	var perIdArr = fetchDivByName(document,"performStatusID");
	var hiddens = "";
	for (var i = 0; i < drawsArr.length; i += 1) {
		if (drawsArr[i].checked == true) {
			if (perIdArr[i].innerHTML.replace(/ /g, "").replace("&nbsp;", "") == "PMEP") {
				hiddens = hiddens + "<input type=\"hidden\" name=\"performStatus\" value=\"PMEP\"/>";
			} else {
				hiddens = hiddens + "<input type=\"hidden\" name=\"performStatus\" value=\"ANY\"/>";
			}
		}
	}
	_id.i("hiddenPerformStatus", hiddens);
}
function checkedDraw(url, statusType, drawType) {
	var isDrawChecked = isAnyChecked();
	if (!isDrawChecked) {
		return false;
	}
	var drawsStatus = _id.o("status").value;
	var drawsArr = _id.n("drawIds");
	var tblObj = _id.o("drawResTbl");
	var gameNo = _id.o("game_no");
				// alert(gameNo.value);
	var bodyStr = "";
	var tblStr = "<table width=\"800\" cellspacing=\"0\" cellpadding=\"5\" border=\"1\" bordercolor=\"#CCCCCC\" ><tr><th colspan=\"5\" align=\"center\"><input type=\"hidden\" name=\"gameNo\" value=\"" + gameNo.value + "\">Draw List</th></tr><tr><th width=\"15%\" scope=\"col\" align=\"center\">Draw Id</th><th width=\"25%\" scope=\"col\" align=\"center\">Draw Time</th><th width=\"25%\" scope=\"col\" align=\"center\">Draw Freeze Time</th><th width=\"25%\" scope=\"col\" align=\"center\">Draw Status</th>";
	if (drawType == "ppr") {
		tblStr += "<th width=\"25%\" scope=\"col\" align=\"center\">Result</th><th width=\"25%\" scope=\"col\" align=\"center\">PPR</th>";
	}
	tblStr += "</tr>";
	var count = 0;
	for (var i = 0, l = drawsArr.length; i < l; i++) {
					// alert(drawsArr[i]);
		if (drawsArr[i].checked) {
			// alert(_id.o(drawsArr[i].value).innerHTML);
						// add rows of table that are checked
			tblStr = tblStr + "<tr>" + _id.o(drawsArr[i].value).innerHTML + "</tr>";
			var drawDateTime = _id.o(drawsArr[i].value + "drawDateTime");
			var dateTime = drawDateTime.innerHTML.split(" ");
			var newFormatDate = dateTime[0].split("-");
			year = newFormatDate[0];
			month = newFormatDate[1];
			day = newFormatDate[2];
			var newFormatTime = dateTime[1].split(":");
			hour = newFormatTime[0];
			min = newFormatTime[1];
			sec = newFormatTime[2];
			tblStr = tblStr.replace("onclick=chk()", "onClick=\"return false\"");
			tblStr = tblStr.replace("checkbox", "hidden");
			if (drawsArr[i].type == "radio") {
				tblStr = tblStr.replace("name=drawIds", "name=drawIds CHECKED"); // for
																					// IE8
																					// changes
			}
			count += 1;
		}
	}
	tblStr = tblStr + "</table><input type='hidden' name='status' value=" + drawsStatus + ">";
	_id.o("to_date").value = _id.v("hidStDate");
	if (drawType == "p") {
		bodyStr = "<h3>Draw To Postpone</h3><form name=\"postponeForm\" method=\"post\" action=\"postponeDraw.action\" onsubmit=\"return _postponeValidate('" + count + "')\">";
		bodyStr = bodyStr + "<div id=\"postpone_err\"></div>" + tblStr;
		bodyStr = bodyStr + "<br><br><br><table cellspacing=\"0\" cellpadding=\"5\" border=\"0\" bordercolor=\"#CCCCCC\" >" + "<tr><td>Postpone Time:&nbsp;</td><td><input type=\"text\" name=\"postponeTime\" id=\"postponeTime\" maxlength=\"6\" onkeypress=\"return onlyNum(this.id)\"/>" + "&nbsp;In Minute</td>";
		if (count < 2) {
			var toDate = day + "/" + month + "/" + year;
					// set default postpone date to checked date
			_id.o("to_date").value = day + "/" + month + "/" + year;
			_id.o("to_hour").options[hour].selected = true;
			_id.o("to_min").options[min].selected = true;
			_id.o("to_sec").options[sec].selected = true;
			var dateStr = _id.o("dateTbl").innerHTML;
			dateStr = dateStr.replace("document.getElementById('from_date').value", "'" + toDate + "'");
			// alert(dateStr);
			bodyStr = bodyStr + "<td>OR &nbsp;Postpone Date</td><td><table>" + dateStr + "</table></td>";
		}
		bodyStr = bodyStr + "</tr><tr><td colspan=\"2\" align=\"right\">" + "<input type=\"submit\" value=\"Postpone Draws\" class=\"button\" />" + "</td></tr><tr><td colspan=\"3\" align=\"right\"><a href=\"drawMenu.action\">Back to Search Results</a> </td></tr></table></form>";
	} else {
		if (drawType == "c") {
			bodyStr = "<h3>Draw To Cancel</h3><form action=\"cancelDraw.action\">";
			bodyStr = bodyStr + tblStr;
			bodyStr = bodyStr + "<input type=\"submit\" value=\"Cancel Draws\" class=\"button\" ><tr><td colspan=\"3\" align=\"right\"><a href=\"drawMenu.action\">Back to Search Results</a> </td></tr></form>";
		} else {
			if (drawType == "change") {
				bodyStr = "<h3>Draw To Change Freeze Time</h3><form action=\"changeFreezeTime.action\" name=\"changeFreezeForm\" onsubmit=\"return _frrezeValidate()\">";
				bodyStr = bodyStr + "<div id=\"frreze_err\"></div>" + tblStr;
				bodyStr = bodyStr + "<br><br><br><table cellspacing=\"0\" cellpadding=\"3\" border=\"1\" bordercolor=\"#CCCCCC\" >" + "<tr><td>Change Freeze Time:&nbsp;</td>" + "<td><input type=\"text\" name=\"freezeTime\" maxlength=\"6\" onkeypress=\"return notZero(this.name)\"/>&nbsp;In Seconds</td>";
				bodyStr = bodyStr + "</tr><tr><td colspan=\"2\" align=\"right\">" + "<input type=\"submit\" value=\"Change Freeze Time\" class=\"button\" /></td></tr><tr><td colspan=\"3\" align=\"right\"><a href=\"drawMenu.action\">Back to Search Results</a> </td></tr></table></form>";
			} else {
				if (drawType == "h") {
					bodyStr = "<h3>Draw To Hold</h3><form action=\"holdDraw.action\">";
					bodyStr = bodyStr + tblStr;
					bodyStr = bodyStr + "<input type=\"submit\" value=\"Hold Draws\" class=\"button\" ><tr><td colspan=\"3\" align=\"right\"><a href=\"drawMenu.action\">Back to Search Results</a> </td></tr></form>";
				} else {
					if (drawType == "a") {
			// bodyStr = holdDraw(bodyStr);
						bodyStr = "<h3>Selected Hold Draw</h3><form action=\"actionOnHold.action\">";
						bodyStr = bodyStr + tblStr;
			// bodyStr = bodyStr + "<input type="submit" value="Hold Draws"
			// class="button" ><tr><td colspan="3" align="right"><a
			// href="drawMenu.action">Back to Search Results</a>
			// </td></tr></form>";
						bodyStr = holdDraw(bodyStr);
					} else {
						if (drawType == "ppr") {
			// bodyStr = holdDraw(bodyStr);
							bodyStr = "<h3>Selected Hold Draw</h3><form action=\"rankChkDraw.action\">";
							bodyStr = bodyStr + tblStr;
							bodyStr = bodyStr + "<tr><td colspan=\"3\" align=\"right\"><input type=\"submit\" name=\"subHold\" value=\"Claim Allow\" class=\"button\" >&nbsp;<a href=\"drawPerformMenu.action\">Back to Search Results</a> </td></tr></form>";
						} else {
							if (drawType == "resSubmit") {
			// bodyStr = holdDraw(bodyStr);
								bodyStr = "<h3>Selected Draw Result</h3><form action=\"saveDrawResult.action\" >";
								bodyStr = bodyStr + tblStr;
								bodyStr = bodyStr + "<tr><td colspan=\"3\" align=\"right\"><input type=\"submit\" value=\"Resolve\" class=\"button\" >&nbsp;<a href=\"resultSubMenu.action\">Back to Search Results</a> </td></tr></form>";
							}
						}
					}
				}
			}
		}
	}
	_id.i("data", bodyStr);
	return true;
}
function getSignVal(gameNameDev, val) {
	var signVal = new Object();
	var signArr;
	var chkGame = false;
	// alert(gameNameDev);
	if (gameNameDev == "kenosix"){
		maxValue = 80;
	}else if (gameNameDev == "keno" ||gameNameDev == "kenotwo"  ||   gameNameDev == "kenofive" || gameNameDev == "kenofour" || gameNameDev == "kenoeight") {
		maxValue = 90;
	} else {
		if (gameNameDev == "fastlotto") {
			maxValue = 35;
		} else {
			if (gameNameDev == "zimlotto" || gameNameDev == "zimlottotwo" || gameNameDev == "zimlottobonus" || gameNameDev == "zimlottobonusfree" || gameNameDev == "zimlottothree") {
				maxValue = 42;
			} else {
				if (gameNameDev == "lotto" || gameNameDev == "tanzanialotto") {
					maxValue = 49;
				}
			}
		}
	}
	if (gameNameDev.toLowerCase() == "card12") {
		var card12Arr = ["ace_spade", "ace_heart", "ace_diamond", "ace_club", "king_spade", "king_heart", "king_diamond", "king_club", "queen_spade", "queen_heart", "queen_diamond", "queen_club"];
		signArr = card12Arr;
	} else {
		if (gameNameDev.toLowerCase() == "card16") {
			card16Arr = ["ace_spade", "ace_heart", "ace_diamond", "ace_club", "king_spade", "king_heart", "king_diamond", "king_club", "queen_spade", "queen_heart", "queen_diamond", "queen_club", "jack_spade", "jack_heart", "jack_diamond", "jack_club"];
			signArr = card16Arr;
		} else {
			if (gameNameDev.toLowerCase() == "fortune") {
				fortuneArr = ["Aries", "Taurus", "Gemini", "Cancer", "Leo", "Virgo", "Libra", "Scorpio", "Sagittarius", "Capricorn", "Aquarius", "Pisces"];
				signArr = fortuneArr;
			} else {
				if (gameNameDev.toLowerCase() == "zerotonine") {
					zerotonineArr = ["Zero(0)", "One(1)", "Two(2)", "Three(3)", "Four(4)", "Five(5)", "Six(6)", "Seven(7)", "Eight(8)", "Nine(9)"];
					signArr = zerotonineArr;
				} else if (gameNameDev.toLowerCase() == "onetotwelve") {
					onetotwelveArr = ["One(1)", "Two(2)", "Three(3)", "Four(4)", "Five(5)", "Six(6)", "Seven(7)", "Eight(8)", "Nine(9)", "Ten(10)", "Eleven(11)", "Twelve(12)"];
					signArr = onetotwelveArr;
				} else {
					chkGame = true; // In case other type game
				}
			}
		}
	}
	// alert("******"+val+"*********");
	if (!chkGame) {
		for (var i = 0; i < signArr.length; i++) {
			signVal[signArr[i]] = i + 1;
		// alert(signVal[signArr[i]]+" "+signArr[i]);
		}
	}
	// alert(signArr[val-1]);
	return chkGame ? val : signVal[val.replace(" ", "")];
}
function resSubmitManual(url, statusType, drawType) {
	var drawsArr = _id.n("drawIds");
	var tblObj = _id.o("drawResTbl");
	var gameNo = _id.o("game_no");
	var gameNum = (gameNo.value).split("-")[0];
	var gameNameDev = _id.o("gameName").value.toLowerCase();
	var winOne = '';
	var winTwo = '';
	
	var bodyStr = "";
	var tblStr = "<table width=\"800\" cellspacing=\"0\" cellpadding=\"5\" border=\"1\" bordercolor=\"#CCCCCC\" ><tr><th colspan=\"7\" align=\"center\"><input type=\"hidden\" name=\"gameNo\" value='" + gameNum + "'>Draw List</th></tr><tr> <th scope=\"col\" align=\"center\">Draw Date Time </th><th scope=\"col\" align=\"center\">User1 Name</th><th scope=\"col\" align=\"center\">User1 Result </th>  <th scope=\"col\" align=\"center\">User1 Time</th><th scope=\"col\" align=\"center\">User2 Name </th><th scope=\"col\" align=\"center\">User2 Result </th><th scope=\"col\" align=\"center\"> User2 Time</th></tr>";
	var count = 0;
	for (var i = 0, l = drawsArr.length; i < l; i++) {
		if (drawsArr[i].checked) {
			winOne = getSignVal(gameNameDev, _id.o("winone" + drawsArr[i].value).innerHTML);
			winTwo = getSignVal(gameNameDev, _id.o("wintwo" + drawsArr[i].value).innerHTML);
			// add rows of table that are checked
			tblStr = tblStr + "<tr>" + _id.o(drawsArr[i].value).innerHTML + "</tr>";
			tblStr = tblStr + "<tr><td colspan=\"7\" align=\"center\"><table cellspacing=\"0\" cellpadding=\"5\" border=\"1\" bordercolor=\"#CCCCCC\" > <tr> <td><input type=\"radio\" name=\"winNumber\" value=\"" + winOne + "\" checked/></td> <td>" + _id.o("winone" + drawsArr[i].value).innerHTML + "</td> <td>User1 Result</td> </tr> <tr> <td><input type=\"radio\" name=\"winNumber\" value=\"" + winTwo + "\"/></td> <td>" + _id.o("wintwo" + drawsArr[i].value).innerHTML + "</td> <td>User2 Result</td> </tr> <tr  onclick=\"document.getElementById('newResult').checked=true\"> <td><input type=\"radio\" name=\"winNumber\" id=\"newResult\" value=\"newresult\"/></td><td>" + getWiningFields(gameNameDev.toLowerCase()) + "</td> <td>New Result</td><td><div id=\"msgDiv\"> </div></td> </tr> </table></td></tr>";
		}
	}
	tblStr = tblStr + "</table>";
	if (drawType == "resSubmit") {
		bodyStr = "<h3>Selected Draw Result</h3><form name=\"resolve\" onsubmit=\"return validateManualEntry2('" + gameNameDev + "')\"  action=\"resultSubSave.action\">";
		bodyStr = bodyStr + tblStr;
		bodyStr = bodyStr + "<tr><td colspan=\"3\" align=\"right\"><input type=\"submit\" value=\"Resolve\" class=\"button\" onclick=\"onResolveSubmit()\">&nbsp;<a href=\"resultSubMenu.action\">Back to Search Results</a> </td></tr></form>";
	}
	_id.i("data", bodyStr);
	// document.forms["resolve"].submit();
	return true;
}
function onResolveSubmit() {
	if (_id.o("cardType") != null) {
		_id.o("newResult").value = _id.o("cardType").value;
	}
}
function validateManualEntry2(gameNameDev) {
	if (_id.o("newResult").checked == true) {
		if(gameNameDev == "kenosix"){
			maxValue = 80;
		}else if (gameNameDev == "keno" || gameNameDev == "kenotwo"  ||   gameNameDev == "kenofive" || gameNameDev == "kenofour" || gameNameDev == "kenoeight") {
			maxValue = 90;
		} else {
			if (gameNameDev == "fastlotto") {
				maxValue = 35;
			} else {
				if (gameNameDev == "zimlotto" || gameNameDev == "zimlottotwo" || gameNameDev == "zimlottobonus" || gameNameDev == "zimlottobonusfree" || gameNameDev == "zimlottothree") {
					maxValue = 42;
				} else {
					if (gameNameDev == "lotto" || gameNameDev == "tanzanialotto") {
						maxValue = 49;
					}
				}
			}
		}
		if(gameNameDev == "supertwo"){
			maxValue = 99;
		}
		gameNameDev = gameNameDev.toLowerCase();
		var frmVal = new parent.frames[0].Validator("resolve");
		var count = 0;
		var zeroPresent = false;
		var maxPresent = false;
		var oneDigitPresent = false;
		var notNum = false;
		var resultEnter = "";
		var noOfBoxes = 0;
		if (gameNameDev == "kenosix") {
			noOfBoxes = 20;
		}else if (gameNameDev == "keno" || gameNameDev == "kenotwo" || gameNameDev == "fastlotto"  ||   gameNameDev == "kenofive" || gameNameDev == "kenofour" || gameNameDev == "kenoeight") {
			noOfBoxes = 5;
		} else {
			if (gameNameDev == "lotto" || gameNameDev == "zimlotto" || gameNameDev == "zimlottotwo"  || gameNameDev == "zimlottothree" || gameNameDev == "tanzanialotto") {
				noOfBoxes = 6;
			}else if(gameNameDev == "supertwo"){
				noOfBoxes = 2;
			}else if(gameNameDev == "twelvebytwentyfour"){
				noOfBoxes = 12;
			}
		}
		var occurences = new Array(6);
		_id.i("msgDiv", "");
		if (gameNameDev == "keno" || gameNameDev == "kenotwo" || gameNameDev == "fastlotto" || gameNameDev == "lotto" || gameNameDev == "zimlotto" || gameNameDev == "zimlottotwo" || gameNameDev == "zimlottothree" || gameNameDev == "supertwo"  ||   gameNameDev == "kenofive" || gameNameDev == "kenofour"  || gameNameDev == "kenosix" || gameNameDev == "kenoeight") {
			for (var counter = 1; counter <= noOfBoxes; counter += 1) {
				var textValue = _id.v("text_gameID:drawIDCtr:" + counter);
				// alert(textValue);
				resultEnter = resultEnter + textValue + ",";
				occurences[counter - 1] = textValue;
				if (textValue == "") {
					_id.i("msgDiv", "<font color = \"red\"> You cannot leave any box blank. </font>");
					return false;
				}
				if (textValue == "0") {
					zeroPresent = true;
				}
				if (textValue.length < 2) {// alert(1);
					oneDigitPresent = true;
				}
				if (textValue > maxValue) {
					maxPresent = true;
				}
				if (isNaN(textValue)) {// alert(notNum+" "+textValue);
					notNum = true;
				}
				// alert(notNum+" "+textValue);
			}
			// alert(notNum);
			if (notNum) {
				_id.i("msgDiv", "<font color = \"red\"> Please Enter a valid Number </font>");
				notNum = false;
				return false;
			}
			if (zeroPresent) {
				_id.i("msgDiv", "<font color = \"red\"> You cannot select 0 as winning number </font>");
				zeroPresent = false;
				return false;
			}
			if (oneDigitPresent) {
				_id.i("msgDiv", "<font color = \"red\"> Please Enter Winning Number in Double Digits </font>");
				oneDigitPresent = false;
				return false;
			}
			if (arrHasDupes(occurences)) {
				_id.i("msgDiv", "<font color = \"red\"> Duplicate number values </font>");
				return false;
			}
			if (maxPresent) {
				_id.i("msgDiv", "<font color = \"red\"> You cannot select greater then " + maxValue + " as winning number </font>");
				maxPresent = false;
				return false;
			}
			if (gameNameDev == "fastlotto" || gameNameDev == "lotto" || gameNameDev == "zimlotto" || gameNameDev == "zimlottotwo"  || gameNameDev == "zimlottothree") {
				if (checkSort(occurences)) {
					_id.i("msgDiv", "<font color = \"red\"> Please Enter data In Ascending Order. </font>");
					return false;
				}
			}
			occurences = new Array(6);
		} else {
			if (gameNameDev == "card12" || gameNameDev == "card16" || gameNameDev == "fortune" || gameNameDev == "zerotonine" || gameNameDev == "onetotwelve") {
				if (_id.o("cardType") != null) {
					if (_id.o("cardType").value == -1) {
						_id.i("msgDiv", "<font color = \"red\"> Please select your result </font>");
						return false;
					}
				}
			}
		}
		return true;
	}
}
function holdDraw(bodyStr) {
	var id = bodyStr.substring(0, bodyStr.indexOf("drawDateTime"));
	id = id.substring(id.lastIndexOf("id=") + 3, id.length);
	var btn = "&nbsp;<input type=\"submit\" name=\"subHold\" value=\"Cancel&Refund\" class=\"button\" >";
	var currTime = _id.o("curTime").innerHTML;
	currTime = currTime.substring(0, currTime.indexOf("."));
	var drawTime = _id.o(id + "drawDateTime").innerHTML;
	var freezeTime = _id.o(id + "freezeDateTime").innerHTML;
	if (freezeTime > currTime && drawTime > currTime) {
		btn = "<input type=\"submit\" name=\"subHold\" value=\"Active\" class=\"button\" >" + btn;
	} else {
		if (freezeTime < currTime && drawTime > currTime) {
			btn = "<input type=\"submit\" name=\"subHold\" value=\"Perform Automatic\" class=\"button\" >" + btn;
		} else {
			if (freezeTime < currTime && drawTime < currTime) {
				btn = "<input type=\"submit\" name=\"subHold\" value=\"Perform Manual\" class=\"button\" >" + btn;
			}
		}
	}
	return bodyStr + "<table width=\"800\" border=\"0\"><tr><td colspan=\"5\" align=\"right\">" + btn + "&nbsp;<a href=\"drawMenu.action\">Back to Search Results</a> </td></tr></table></form>";
}
function isAnyChecked() {
	var count = 0;
	var ret = true;
	for (var i = 0; i < _id.n("drawIds").length; i += 1) {
		var e = _id.n("drawIds")[i];
		if (e.checked == true) {
			count += 1;
		}
	}
	if (count == 0) {
		alert("Please choose atleast one draw");
		ret = false;
	}
	return ret;
}
function validation(time) {
	if (_id.o(time).value == "" || _id.o(time).value == 0) {
		alert("Please Enter Time");
		return false;
	}
	return true;
}
function postponeValidation(time, count) {
				// alert(count);
	var time = _id.o(time).value;
	if (!(time == "" || time == 0)) {
		return true;
	}
	if (count < 2) {
		var date = day + "/" + month + "/" + year;
		var newDate = _id.o("to_date").value;
		var newHour = _id.o("to_hour").value;
		if (newHour < 10) {
			newHour = "0" + newHour;
		}
		var newMin = _id.o("to_min").value;
		if (newMin < 10) {
			newMin = "0" + newMin;
		}
		var newSec = _id.o("to_sec").value;
		if (newSec < 10) {
			newSec = "0" + newSec;
		}
					// alert(date+"="+newDate+"|"+hour+"="+newHour+"|"+min+"="+newMin+"|"+sec+"="+newSec);
		if (date == newDate && hour == newHour && min == newMin && sec == newSec) {
			alert("Your postpone time and current time is same");
			return false;
		}
	} else {
		var flag = validation(time);
		return flag;
	}
	return true;
}
function fillOptions() {
	var alltag = document.getElementsByTagName("select");
	for (var i = 0; i < alltag.length; i++) {
		var k = 0;
		var tagType = alltag[i].id;
		if (tagType.match("hour")) {
			for (var j = 0; j < 24; j++) {
				if (j < 10) {
					j = "0" + j;
				}
				alltag[i].options[k] = new Option(j, k);
				if (tagType.match("to") && j == 23) {
					alltag[i].options[k].selected = true;
				}
				k++;
			}
		} else {
			if (tagType.match("min") || tagType.match("sec")) {
				for (var m = 0; m <= 59; m++) {
					if (m < 10) {
						m = "0" + m;
					}
					alltag[i].options[k] = new Option(m, k);
					if (tagType.match("to") && m == 59) {
						alltag[i].options[k].selected = true;
					}
					k++;
				}
			}
		}
	}
}

// Global constants for Manual Entry
function fillWinningDivs() {
 // alert('fhjgfghj');
	var drawsArr = _id.n("drawIds");
	var gameNo = _id.o("gameNo").value;
	var gameNameDev = _id.o("gameName").value;
	gameNameDev = gameNameDev.toLowerCase();
	
	// gameNameDev=gameNameDev.toLowerCase();
	
	// alert(gameNameDev+"-------------");
	if (gameNameDev == "kenosix") {
		maxValue = 80;
		_id.o("winNumSize").value = 20;
	}else if (gameNameDev == "keno"||gameNameDev == "kenotwo"  ||   gameNameDev == "kenofive" || gameNameDev == "kenofour" || gameNameDev == "kenoeight") {
		maxValue = 90;
		_id.o("winNumSize").value = 5;
	} else if (gameNameDev == "twelvebytwentyfour"){
		maxValue = 24;
		_id.o("winNumSize").value = 12;
	}else {
		if (gameNameDev == "fastlotto") {
			maxValue = 35;
			_id.o("winNumSize").value = 5;
		} else {
			if (gameNameDev == "zimlotto" || gameNameDev == "zimlottotwo "  || gameNameDev == "zimlottothree") {
				maxValue = 42;
				_id.o("winNumSize").value = 6;
			} else {
				if (gameNameDev == "lotto" || gameNameDev == "tanzanialotto") {
					maxValue = 49;
					_id.o("winNumSize").value = 6;
				}
			}
		}
	}
	if(gameNameDev == "supertwo"){
		maxValue = 99;
		_id.o("winNumSize").value = 2;
	}else {
		if(gameNameDev == "bonusballlotto"){
			maxValue = 39;
			isBonus = true;
			_id.o("winNumSize").value = 6;
		}
	}
	// perIdArr[i].innerHTML.replace(/ /g,"")=='PMEP'
	var winningFields = getWiningFields(gameNameDev);
	var performStatusObj = fetchDivByName(document,"performStatusID");
	for (var i = 0; i < drawsArr.length; i += 1) {
		_id.i(drawsArr[i].value + "WinRes", winningFields.replace(/drawID/g, drawsArr[i].value));
		if (performStatusObj != null && performStatusObj[i].innerHTML.replace(/ /g, "") == "PMEP") {
			_id.i(drawsArr[i].value + "DrawTypeSelect", "<input type=\"hidden\" name=\"drawType\" value=\"PMEM\"/><font color = \"green\"> Only Manual Status Applicable </font>");
		} else {
			var options = "<select name=\"drawType\" class=\"option\">" + "<option value=\"PMEM\">MANUAL</option>" + "<option value=\"PAEM\">AUTO</option>" + "</select>";
			_id.i(drawsArr[i].value + "DrawTypeSelect", options);
		}
	}
	
}
function getWiningFields(gameNameDev) {
	gameNameDev = gameNameDev.toLowerCase();
	var winningFillingFields = "";
	if (gameNameDev == "kenosix") {
		winningFillingFields = formTextBoxes(20);
	}else if (gameNameDev == "keno" || gameNameDev == "fastlotto" || gameNameDev == "kenotwo"  ||   gameNameDev == "kenofive" || gameNameDev == "kenofour" || gameNameDev == "kenoeight") {
		winningFillingFields = formTextBoxes(5);
	} else if (gameNameDev == "twelvebytwentyfour"){
		winningFillingFields = formTextBoxes(12);
	}else {
		if (gameNameDev == "lotto" || gameNameDev == "zimlotto" || gameNameDev == "zimlottotwo" || gameNameDev == "zimlottothree" || gameNameDev == "tanzanialotto" || gameNameDev == "bonusballlotto") {
			winningFillingFields = winningFillingFields + formTextBoxes(6);
		} else {
			if (gameNameDev == "card12") {
				var signArr = ["ace_spade", "ace_heart", "ace_diamond", "ace_club", "king_spade", "king_heart", "king_diamond", "king_club", "queen_spade", "queen_heart", "queen_diamond", "queen_club"];
				winningFillingFields = formOptions(signArr);
			} else {
				if (gameNameDev == "card16") {
					var signArr = ["ace_spade", "ace_heart", "ace_diamond", "ace_club", "king_spade", "king_heart", "king_diamond", "king_club", "queen_spade", "queen_heart", "queen_diamond", "queen_club", "jack_spade", "jack_heart", "jack_diamond", "jack_club"];
					winningFillingFields = formOptions(signArr);
				} else {
					if (gameNameDev == "fortune") {
						var signArr = ["Aries", "Taurus", "Gemini", "Cancer", "Leo", "Virgo", "Libra", "Scorpio", "Sagittarius", "Capricorn", "Aquarius", "Pisces"];
						winningFillingFields = formOptions(signArr);
					} else {
						if (gameNameDev == "zerotonine") {
							var signArr = ["Zero(0)", "One(1)", "Two(2)", "Three(3)", "Four(4)", "Five(5)", "Six(6)", "Seven(7)", "Eight(8)", "Nine(9)"];
							winningFillingFields = formOptions(signArr);
						} else if (gameNameDev == "onetotwelve") {
							var signArr = ["One(1)", "Two(2)", "Three(3)", "Four(4)", "Five(5)", "Six(6)", "Seven(7)", "Eight(8)", "Nine(9)", "Ten(10)", "Eleven(11)", "Twelve(12)"];
							winningFillingFields = formOptions(signArr);
						}
					}
				}
			}
		}
	}
	if(gameNameDev == "supertwo"){
		winningFillingFields = formTextBoxes(2);
	}
	return winningFillingFields;
}
function formTextBoxes(numberOfBoxes) {
	var textBoxes = "";
	for (var counter = 1; counter <= numberOfBoxes; counter += 1) {
		if(counter==6 && isBonus){
			textBoxes = textBoxes + "<input type=\"text\" maxlength=\"2\" minlength=\"1\" title=\"Bonus Ball Result\" style=\"width: 25px;font-size:10px; margin-bottom:4px; background:yellow\" name=\"winNumbers\" id=\"text_gameID:drawIDCtr:" + counter + "\" onkeyUp=\"return keyPressManualEntry(this,event)\" onmousedown=\"return disClick(this)\"/>";
		}else{
		textBoxes = textBoxes + "<input type=\"text\" maxlength=\"2\" minlength=\"1\" style=\"width: 25px;font-size:10px; margin-bottom:4px\" name=\"winNumbers\" id=\"text_gameID:drawIDCtr:" + counter + "\" onkeyUp=\"return keyPressManualEntry(this,event)\" onmousedown=\"return disClick(this)\"/>";
		}
	}
	return textBoxes;
}
function formOptions(signArr) {
	var select = "<select name=\"cardType\" id=\"select:drawID\" onchange='onSelectNewResult(this.value)'>";
	var firstOption = "<option value=\"-1\">--Please Select--</option>";
	var addedOptions = "";
	for (var i = 1; i <= signArr.length; i += 1) {
		addedOptions = addedOptions + "<option value=\"" + i + "\">" + signArr[i - 1] + "</option>";
	}
	return select + firstOption + addedOptions + "</select>";
}

function onSelectNewResult(result){
_id.o('newResult').value=result;
}

function validateManualEntry(gameNameDev) {
	if (gameNameDev == "kenosix" ) {
		maxValue = 80;
	}else if (gameNameDev == "keno" || gameNameDev == "kenotwo"  ||   gameNameDev == "kenofive" || gameNameDev == "kenofour" || gameNameDev == "kenoeight") {
		maxValue = 90;
	} else {
		if (gameNameDev == "fastlotto") {
			maxValue = 35;
		} else {
			if (gameNameDev == "zimlotto" || gameNameDev == "zimlottotwo"  || gameNameDev == "zimlottothree") {
				maxValue = 42;
			} else {
				if (gameNameDev == "lotto" || gameNameDev == "tanzanialotto") {
					maxValue = 49;
				}
			}
		}
	}
	if(gameNameDev == "supertwo"){
		maxValue = 99;
	}else{
		if(gameNameDev == "bonusballlotto"){
			maxValue = 39;
		}
	}
	gameNameDev = gameNameDev.toLowerCase();
	var frmVal = new parent.frames[0].Validator("manualEntryForm");
	var count = 0;
	var zeroPresent = false;
	var maxPresent = false;
	var oneDigitPresent = false;
	var notNum = false;
	var resultEnter = "";
	var drawsArr = _id.n("drawIds");
	var ret = new Array(drawsArr.length);
	for (var i = 0; i < drawsArr.length; i += 1) {
		ret[i] = true;
		if (drawsArr[i].checked == true) {
			count += 1;
		}
	}
	if (count == 0) {
		alert("Please choose draw");
		return false;
	}
	var noOfBoxes = 0;
	if (gameNameDev == "kenosix" ) {
		noOfBoxes = 20;
	} else if(gameNameDev == "twelvebytwentyfour"){
		noOfBoxes = 12;
	}else if (gameNameDev == "keno" || gameNameDev == "fastlotto" || gameNameDev == "kenotwo"  ||   gameNameDev == "kenofive" || gameNameDev == "kenofour" || gameNameDev == "kenoeight") {
		noOfBoxes = 5;
	} else {
		if (gameNameDev == "lotto" || gameNameDev == "zimlotto" || gameNameDev == "zimlottotwo"  || gameNameDev == "zimlottothree" || gameNameDev == "tanzanialotto" || gameNameDev == "bonusballlotto") {
			noOfBoxes = 6;
		}else if(gameNameDev == "supertwo"){
			noOfBoxes = 2;
		}
	}
	var drawType = _id.n("drawType");
	var occurences = new Array(noOfBoxes);
	for (var i = 0; i < drawsArr.length; i += 1) {
		_id.i(drawsArr[i].value + "Message", "");
		document.error_disp_handler.isValid = true;
	}
	for (var i = 0; i < drawsArr.length; i += 1) {
		document.error_disp_handler.isValid = true;
		if (drawsArr[i].checked) {
			if (gameNameDev == "keno" || gameNameDev == "kenotwo" || gameNameDev == "fastlotto" || gameNameDev == "lotto" || gameNameDev == "zimlotto" || gameNameDev == "zimlottotwo"  || gameNameDev == "zimlottothree" || gameNameDev == "supertwo" || gameNameDev == "tanzanialotto" || gameNameDev == "bonusballlotto"  ||   gameNameDev == "kenofive" || gameNameDev == "kenofour" || gameNameDev == "kenoeight" || gameNameDev == "kenosix" || gameNameDev == "twelvebytwentyfour") {
				for (var counter = 1; counter <= noOfBoxes; counter += 1) {
					var textValue = _id.v("text_gameID:" + drawsArr[i].value + "Ctr:" + counter);
						// alert(textValue);
					resultEnter = resultEnter + textValue + ",";
					occurences[counter - 1] = textValue;
					if (textValue == "") {
						frmVal.aV("text_gameID:" + drawsArr[i].value + "Ctr:" + counter, "req", "Please select Machine result", drawsArr[i].value + "Message");
						ret[i] = false;
					}
					if (textValue == "0") {
						zeroPresent = true;
					}
					if (textValue.length < 2) {// alert(oneDigitPresent);
						oneDigitPresent = true;
					}
					if (textValue > maxValue) {
						maxPresent = true;
					}
					if (isNaN(textValue)) {
						notNum = true;
					}
				}
				if (ret[i] && notNum) {
					_id.i(drawsArr[i].value + "Message", "<font color = \"red\"> Please Enter a valid Number </font>");
					ret[i] = false;
					notNum = false;
				}
				if (ret[i] && zeroPresent) {
					_id.i(drawsArr[i].value + "Message", "<font color = \"red\"> You cannot select 0 as Machine number </font>");
					ret[i] = false;
					zeroPresent = false;
				}
				if (ret[i] && oneDigitPresent) {
					_id.i(drawsArr[i].value + "Message", "<font color = \"red\"> Please Enter Machine Number in Double Digits </font>");
					ret[i] = false;
					oneDigitPresent = false;
				}
				if (ret[i] && arrHasDupes(occurences)) {
					_id.i(drawsArr[i].value + "Message", "<font color = \"red\"> Duplicate number values </font>");
					ret[i] = false;
				}
				if (ret[i] && maxPresent) {
					_id.i(drawsArr[i].value + "Message", "<font color = \"red\"> You cannot select greater then " + maxValue + " as Machine number </font>");
					ret[i] = false;
					maxPresent = false;
				}
				if (ret[i] && gameNameDev == "fastlotto" || gameNameDev == "lotto" || gameNameDev == "zimlotto" || gameNameDev == "zimlottotwo"  || gameNameDev == "zimlottothree") {
					if (checkSort(occurences)) {
						_id.i(drawsArr[i].value + "Message", "<font color = \"red\"> Please Enter data In Ascending Order. </font>");
						ret[i] = false;
						return false;
					}
				}
				occurences = new Array(noOfBoxes);
			} else {
				if (gameNameDev == "card12" || gameNameDev == "card16" || gameNameDev == "fortune" || gameNameDev == "zerotonine" || gameNameDev == "onetotwelve") {
					if (_id.o("select:" + drawsArr[i].value).value == "-1") {
						ret[i] = false;
					}
					resultEnter = _id.o("select:" + drawsArr[i].value).value;
					frmVal.aV("select:" + drawsArr[i].value, "dontselect=0", "Please select winning result", drawsArr[i].value + "Message");
				}
			}
			if (ret[i] && document.error_disp_handler.isValid) {
				_id.i(drawsArr[i].value + "Message", "<font color = \"green\"> Correct data now.</font>");
			}
				// alert(resultEnter);
		}
	}
	for (var i = 0; i < ret.length; i += 1) {
		if (ret[i] == false) {
			break;
		}
	}
	var confirmSubmit = false;
	if (i == ret.length && document.error_disp_handler.isValid) {
		var winRes = resultEnter;
		var signArr;
		var chkGame=false;
		if (gameNameDev.toLowerCase() == "card12") {
			var card12Arr = ["ace_spade", "ace_heart", "ace_diamond", "ace_club", "king_spade", "king_heart", "king_diamond", "king_club", "queen_spade", "queen_heart", "queen_diamond", "queen_club"];
			signArr = card12Arr;
		} else {
			if (gameNameDev.toLowerCase() == "card16") {
				card16Arr = ["ace_spade", "ace_heart", "ace_diamond", "ace_club", "king_spade", "king_heart", "king_diamond", "king_club", "queen_spade", "queen_heart", "queen_diamond", "queen_club", "jack_spade", "jack_heart", "jack_diamond", "jack_club"];
				signArr = card16Arr;
			} else {
				if (gameNameDev.toLowerCase() == "fortune") {
					fortuneArr = ["Aries", "Taurus", "Gemini", "Cancer", "Leo", "Virgo", "Libra", "Scorpio", "Sagittarius", "Capricorn", "Aquarius", "Pisces"];
					signArr = fortuneArr;
				} else {
					if (gameNameDev.toLowerCase() == "zerotonine") {
						zerotonineArr = ["Zero(0)", "One(1)", "Two(2)", "Three(3)", "Four(4)", "Five(5)", "Six(6)", "Seven(7)", "Eight(8)", "Nine(9)"];
						signArr = zerotonineArr;
					} else if (gameNameDev.toLowerCase() == "onetotwelve") {
						onetotwelveArr = ["One(1)", "Two(2)", "Three(3)", "Four(4)", "Five(5)", "Six(6)", "Seven(7)", "Eight(8)", "Nine(9)", "Ten(10)", "Eleven(11)", "Twelve(12)"];
						signArr = onetotwelveArr;
					} else {
						chkGame = true; // In case other type game
					}
				}
			}
		}
		
		
		if (!chkGame){
			//alert(gameNameDev);
			winRes=signArr[resultEnter-1];
		}
		//alert("sds"+winRes);
		confirmSubmit = confirm("You are about to submit Machine Number  : '"+winRes+"'  Do you want to proceed?");
		// showSelectedResults(gameNo);
		if (confirmSubmit) {
			for (var i = 0; i < drawsArr.length; i += 1) {
				if (!drawsArr[i].checked) {
					_id.i(drawsArr[i].value + "WinRes", "");
					_id.i(drawsArr[i].value + "DrawTypeSelect", "");
				}
			}
		}
	}
	return confirmSubmit;
}
function arrHasDupes(arr) {     	// finds any duplicate array elements using
									// the fewest possible comparison
	var i, len = arr.length, out = [], obj = {};
	for (i = 0; i < len; i += 1) {
		obj[arr[i]] = 0;
	}
	for (i in obj) {
		out.push(i);
	}
	return out.length != arr.length;
}
function keyPressManualEntry(obj, e) {// alert(_id.n('winNumbers').length);
	var keyCode = (window.event)? event.keyCode : e.which;
	if ((keyCode >= 48 && keyCode < 58) || keyCode == 45 || keyCode == 27 || keyCode == 13 || keyCode == 40 || keyCode == 38 || keyCode == 46 || keyCode == 8 || (keyCode >= 96 && keyCode < 106 || keyCode == 9)) {
		if (keyCode == 46 || keyCode == 8 || keyCode == 9) {
			return true;
		}
		if (obj.value.length > 1) {
			if (obj.value == 0 || obj.value > maxValue || isNaN(obj.value)) {
				revertValue(obj);
			} else {
				var elms = _id.n("winNumbers");
				for (var l = 0; l < elms.length; l++) {
					if (elms[l].value == "") {
						elms[l].focus();
						break;
					}
				}
			}
		}
	} else {
		revertValue(obj);
		return false;
	}
	return true;
}
function revertValue(obj) {
	obj.value = "";
	obj.focus();
}
function checkAll(id) {
	var isChkd = false;
	if (_id.n(id)[0].checked == true) {   // For IE8 and Safari by Mahesh
		isChkd = true;
	}
	for (var i = 0; i < _id.n("drawIds").length; i++) {
		var e = _id.n("drawIds")[i];
		e.checked = isChkd;
	}
}
function chk() {
	var count = 0;
	for (var i = 0; i < _id.n("drawIds").length; i++) {
		var e = _id.n("drawIds")[i];
		if (e.checked == true) {
			count++;
		}
	}
	var t = parseInt(_id.o("drawResTbl").rows.length);				
			// alert("---"+_id.o('drawResTbl').rows.length-2); //Changes By
			// Mahesh for IE 8 and Safari
	if (count == parseInt(_id.o("drawResTbl").rows.length)) {
		_id.n("check").checked = true;
	} else {
		_id.n("check").checked = false;
	}
}
function _validate() {
	if (_id.o("performStatus") != null && _id.o("performStatus").value == "-1") {
		alert("Please choose perform Status!");
		return false;
	}
	var frmVal = new parent.frames[0].Validator("searchForm");
	if (_id.o("drawId").value == 0) {
		_id.o("drawId").value = "";
	}
	
	// add by vaibhav
	var status = /please select/i;
	if (_id.o("status") != null && status.test(_id.o("status").value)) {
		alert("Please Select Draw Status");
		_id.o("status").focus();
		return false;
	}
				// frmVal.aV("phone","req","Please Enter Phone Number","Phone");
	frmVal.aV("drawId", "numeric", "Please Enter Correct Value For Draw Id", "draw_err");
	return document.error_disp_handler.isValid;
}
function notZero(id, evt) {
	var oneChar = 0;
	var e = event || evt;
	var charCode = e.which || e.keyCode;
	if (charCode == 48 && _id.o(id).value.length == 0) {
		return false;
	}
	return true;
}
function onlyNum(id, evt) {
	var oneChar = 0;
	var e = event || evt;
	var charCode = e.which || e.keyCode;
	if (charCode < 48 || charCode > 57) {
		return false;
	}
	return true;
}
function _frrezeValidate() {
	var frmVal = new parent.frames[0].Validator("changeFreezeForm");
	if (_id.o("freezeTime").value == 0) {
		_id.o("freezeTime").value = "";
	}
	frmVal.aV("freezeTime", "req", "Please Enter Freeze Time", "frreze_err");
	frmVal.aV("freezeTime", "numeric", "Please Enter Correct Value For Freeze Time", "frreze_err");
	return document.error_disp_handler.isValid;
}
function _postponeValidate(count) {
	var frmVal = new parent.frames[0].Validator("postponeForm");
	var time = _id.o("postponeTime").value;
	if (!(time == "" || time == 0)) {
		return true;
	}
	if (count < 2) {
		var date = day + "/" + month + "/" + year;
		var newDate = _id.o("to_date").value;
		var newHour = _id.o("to_hour").value;
		if (newHour < 10) {
			newHour = "0" + newHour;
		}
		var newMin = _id.o("to_min").value;
		if (newMin < 10) {
			newMin = "0" + newMin;
		}
		var newSec = _id.o("to_sec").value;
		if (newSec < 10) {
			newSec = "0" + newSec;
		}
					// alert(date+"="+newDate+"|"+hour+"="+newHour+"|"+min+"="+newMin+"|"+sec+"="+newSec);
		if (date == newDate && hour == newHour && min == newMin && sec == newSec) {
			alert("Your postpone time and current time is same");
			return false;
		} else {
			var dateArr = date.split("/");
			var newDateArr = newDate.split("/");
			var dateObj = new Date(dateArr[2], dateArr[1], dateArr[0], hour, min, sec);
			var newDateObj = new Date(newDateArr[2], newDateArr[1], newDateArr[0], newHour, newMin, newSec);
			var postPoneMin = Math.ceil((newDateObj.getTime() - dateObj.getTime()) / (1000 * 60));
			if (postPoneMin < 0) {
				alert("Your postpone time is less then current time");
				return false;
			}
			_id.o("postponeTime").value = postPoneMin;
		}
	} else {
		if (_id.o("postponeTime").value == 0) {
			_id.o("postponeTime").value = "";
		}
		frmVal.aV("postponeTime", "req", "Please Enter Postpone Time", "postpone_err");
		frmVal.aV("postponeTime", "numeric", "Please Enter Correct Value For Postpone Time", "postpone_err");
		return document.error_disp_handler.isValid;
	}
	return true;
}
function disClick(obj) {
	if (event.button == 2) {
		obj.oncontextmenu = new Function("return false");
		return false;
	}
}
function checkSort(arr) {
	var length = arr.length;
	var i = 0;
	var j = 0;
	for (i = 0; i < length; i++) {
		for (j = (i+1); j < length; j++) {
			if (parseInt(arr[i],10) >= parseInt(arr[j],10)) {
				return true;
			}
		}
	}
	return false;
}


parent.frames[0].Version["$Source: /rep/LMS_Mgmt/WebRoot/com/skilrock/lms/web/drawGames/drawMgmt/js/drawGameMachineNbrEntry.js,v $"] = "$Id: drawGameMachineNbrEntry.js,v 1.3 2016/10/31 09:46:59 neeraj Exp $";

