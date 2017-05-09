var i18nObj = parent.frames[0].i18Obj;
var PWTWord = "";
var _id = {o:function (el_id) {
	return document.getElementById(el_id);
}, n:function (el_id) {
	return document.getElementsByName(el_id);
}, tn:function (el_id) {
	return document.getElementsByTagName(el_id);
}, i:function (el_id, msg, e) {
	if (e) {
		msg = "<font color = 'red'>" + msg + "</font>";
	}
	this.o(el_id).innerHTML = msg;
}, v:function (el_id, r) {
	if (r) {
		return this.o(el_id).value == "";
	}
	return this.o(el_id).value;
}, iniFocus:function (el_id) {
	this.o(el_id).focus();
}, blk:function (el_id) {
	this.o(el_id).style.display = "block";
}, non:function (el_id) {
	this.o(el_id).style.display = "none";
}, hid:function (el_id) {
	this.o(el_id).style.visibility = "hidden";
}, vis:function (el_id) {
	this.o(el_id).style.visibility = "visible";
}, fo:function (f, ele) {
	return document.forms[f].elements[ele];
}, fi:function (f, ele, msg, e) {
	if (e) {
		msg = "<font color = 'red'>" + msg + "</font>";
	}
	this.fo(f, ele).innerHTML = msg;
}, fv:function (f, ele, r) {
	if (r) {
		return this.fo(f, ele).value == "";
	}
	return this.fo(f, ele).value;
}, finiFocus:function (f, ele) {
	this.fo(f, ele).focus();
}, contains:function (arr, ele) {
	for (var i = 0; i < arr.length; i++) {
		if (arr[i] === ele) {
			return true;
		}
	}
	return false;
}, resetInpTypes:function (id, cssName, val, startFrom) {
	var isRadio = true;
	if (_id.o(id) != null) {
		arr = _id.o(id).getElementsByTagName("input");
		if (startFrom == undefined) {
			startFrom = 0;
		}
		for (var i = startFrom; i < arr.length; i++) {
			if (arr[i].type == "text") {
				arr[i].value = "";
				arr[i].disabled = val;
				arr[i].className = cssName;
			} else {
				if (arr[i].type == "checkbox") {
					arr[i].checked = val;
				} else {
					if (arr[i].type == "radio" && isRadio) {
						arr[i].checked = !val;
						isRadio = false;
					}
				}
			}
		}
	}
}};
var drawType = "";
var dateFuture = "";
var counter = 0;
var month = ["Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"];
var week = ["Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"];
var sign = "";
var timeDiffInMin = 0;
var freezeTime = 0;
var curSym = "Rs";//parent.frames[0].document.getElementById("currencyWord").innerHTML;
var curActive = "";
var isTimer = false;
var refreshTime = 60000;//1000*60sec
var isPrintable = true;
var frzTime = 0;
var frzCount = 0;
var visitGameArr = new Array();
var latestFreezeTime = new Array();
var storedGameObj;
//var drawSelect;
/***************************************/
var uniqueDrawTime;
var uniqueDrawTimeArr = new Array();
var reprintCountChild = 0;
var curTrx = "";
var isPayAction = true;
var allowBuy = true;
function updateUniqueDrawTime() {
	var i;
	var temp = new Array();
	for (i = 0; i < activeGames.length; i++) {
		uniqueDrawTime = eval("_" + activeGames[i] + "DrawTime");
		temp = uniqueDrawTime.replace(/ /g, "").split(",");
		uniqueDrawTimeArr = uniqueDrawTimeArr.concat(temp);
	}
	uniqueDrawTimeArr = unique(uniqueDrawTimeArr.sort());
}
updateUniqueDrawTime();
function unique(a) {
	var r = new Array();
o:
	for (var i = 0, n = a.length; i < n; i++) {
		for (var x = 0, y = r.length; x < y; x++) {
			if (r[x] == a[i]) {
				continue o;
			}
		}
		r[r.length] = a[i];
	}
	return r;
}
/***************************************/
//alert(uniqueDrawTimeArr);alert(uniqueDrawTimeArr.length);
var timeLeftForFrz = uniqueDrawTimeArr[0] - curSerTime;
//alert("(1)"+new Date(uniqueDrawTimeArr[0])+"  (2)"+new Date(curSerTime)+"  (3)"+new Date(uniqueDrawTimeArr[0]-curSerTime))
//alert("timeLeftForFrz::"+timeLeftForFrz+"uniqueDrawTimeArr[0]"+uniqueDrawTimeArr[0]+"curSerTime"+curSerTime);
function XMLRequestDojo() {
	if (window.XMLHttpRequest) {
		return new XMLHttpRequest();
	}
	return new ActiveXObject("Microsoft.XMLHTTP");
}
function _randCache(url) {
	var _c = "?";
	if (url.indexOf("?") != -1) {
		_c = "&";
	}
	url = url + _c + new Date().getTime() + Math.random() * 1000000;
	return url;
}
var childW = null;

// load game
function gameObj(game) {
	//alert("GAME OBJECT"+game);
	this.gameName = game;
	//alert("this.gameName"+this.gameName);
	this.drawDivO = eval("_" + game + "HTML");
	//alert("this.drawDivO==="+this.drawDivO);
	this.nxtDrawDivO = eval("_" + game + "FillNxtDrw()");
	//alert("this.nxtDrawDivO+++"+this.nxtDrawDivO);
	this.nxtDrawTimeO = eval("_" + game + "NxtDrawTime");
	//alert("this.nxtDrawTimeO+++"+this.nxtDrawTimeO);
	this.latestDrawTimeO = eval("_" + game + "DrawTime").split(",")[0];
	//alert("this.latestDrawTimeO+++"+this.latestDrawTimeO);
	
	
	this.winningDispDivO = eval("_" + game + "FillWinTbl()");
	//alert("this.winningDispDivO+++"+this.winningDispDivO);
	this.ClassName = "headerSelect" + game;
}
function gamesData() {
	for (var i = 0; i < activeGames.length; i++) {
		visitGameArr[activeGames[i]] = new gameObj(activeGames[i]);
		latestFreezeTime[activeGames[i]] = visitGameArr[activeGames[i]].latestDrawTimeO;
	}
}
function checkStatus(id) {
	var sp = _id.o("sideTbl").getElementsByTagName("span");
	for (var i = 0; i < sp.length; i++) {
		sp[i].className = "txt8";
	}
	if (id != undefined) {
		_id.o(id).className = "txt7";
	} else {
		if (sp[0] != undefined) {
			if(sp[0].innerHTML.toString() == ' '+PWTWord || sp[0].innerHTML.toString() == PWTWord){
				eval("pwtTable()");
			}else{
				eval((sp[0].innerHTML.toString()).split(" ")[0].toLowerCase() + "Table()");
			}
		}
	}
}

function drawMenuClick(val){
	//alert('Gaurav'+activeGames[val]+ allowBuy);
	if(allowBuy){
		fillGame(activeGames[val],'manualCall');
	}
}

var drwMenu = "<div id=\"menuDiv\"></div>";
function drwMenuDynamic() {
	drwMenu = "<table border=\"0\" width=\"100%\" height=\"29px\" cellspacing=\"2\" cellpadding=\"0\" align=\"center\"  bordercolor=\"red\"><tr>";
	for (var gameindx = activeGames.length - 1; gameindx >= 0; gameindx--) {
		drwMenu = drwMenu + "<td id='" + activeGames[gameindx] + "Tab' class='drawgameheader' onclick=\"drawMenuClick("+gameindx+")\" >" + gameDispName[activeGames[gameindx]].gameDisplayName.toUpperCase();
		drwMenu = drwMenu + "</td>";
	}
	drwMenu = drwMenu + "</tr></table>";
	_id.i("menuDiv", drwMenu);
}
function fillGame(game, callBy) {
	if (typeof game == "undefined") {
		alert(i18nObj.prop('msg.your.sale.onhold'));
		return;
	}
	//gamealert(game); //***********
	curActive = game;
	storedGameObj = visitGameArr[game];
	frzTime = 0;
	frzCount = 0;
	if (callBy == "manualCall") {
		clearTimeout(_gameGetShortTimer);
		clearTimeout(GetCountTimer);
		eval("setDefault" + game + "()");
		_id.i("drawDiv", storedGameObj.drawDivO);
		checkStatus();
	}
	//fetchTimeData();
	//alert(storedGameObj.nxtDrawDivO);
	_id.i("nxtDrawDiv", storedGameObj.nxtDrawDivO);
	_id.i("nxtDrawTime", storedGameObj.nxtDrawTimeO);
	//_id.i("demo4", "");
	if(game == "kenosix"){
		_id.i("belowNextDraw", storedGameObj.nxtDrawTimeO);
		//_id.i("timerDiv", "");
		//callTimer(storedGameObj.nxtDrawTimeO);
	}
	_id.i("latestDrawTime", storedGameObj.latestDrawTimeO);
	freezeTime = _id.o("freezeTimeId").value;
	dateFuture = document.getElementById("0timer").value.split("-");
	_id.o("dateFuture").value = dateFuture;
	var latestDrawTime = latestFreezeTime[game];
	/*if (aftrFrz) {
		latestDrawTime = drwTime;
		//alert(latestDrawTime+"in fillgame sftr frz true"+drwTime);
	}*/
	var gameTime = new getGameDrawTime();
	//alert(new Date(parseFloat(latestDrawTime))+"**"+new Date(parseFloat(drwTime))+"**"+new Date(parseFloat(curSerTime)));
	//alert(latestDrawTime - curSerTime +"   **"+freezeTime*1000);
	if ((latestDrawTime - curSerTime) < freezeTime * 1000) {//alert("short");
		eval("_" + game + "WinAppend()");
		_id.i("winningDispDiv", eval("_" + game + "FillWinTbl()"));
		var temp = latestDrawTime - curSerTime;
		gameTime.gameShortTimer(temp);
	} else {
		_id.i("winningDispDiv", storedGameObj.winningDispDivO);
	}
	drwMenuDynamic();
	_id.o(game + "Tab").className = storedGameObj.ClassName;
	gameTime.getDrawTime();
	if (_id.o("jackpotAmt")) {
		if (typeof jackpotAmt[game] != "undefined") {
			if(game=="tanzanialotto"){
				_id.o("jackpotAmt").innerHTML = "Jackpot TSH=" + jackpotAmt[game];
			}else{
				_id.o("jackpotAmt").innerHTML = "Jackpot USD=" + jackpotAmt[game];
			}			
		} else {
			_id.o("jackpotAmt").innerHTML = "";
		}
	}
}
function gameShortTimer(freezeTime) {
	clearTimeout(_gameGetShortTimer);
	var shortAmount = freezeTime;
	if (shortAmount < 0) {
		_id.i("winAwaitedDiv", "Updating Data...");
	} else {
		days = 0;
		hours = 0;
		mins = 0;
		secs = 0;
		out = "";
		shortAmount = Math.floor(shortAmount / 1000);  // seconds
		days = Math.floor(shortAmount / 86400);
		shortAmount = shortAmount % 86400;
		hours = Math.floor(shortAmount / 3600);//hours
		shortAmount = shortAmount % 3600;
		mins = Math.floor(shortAmount / 60);//minutes
		shortAmount = shortAmount % 60;
		secs = Math.floor(shortAmount);//seconds
		if (days != 0) {
			out += days + "d";
		}
		if (days != 0 || hours != 0) {
			out += (hours < 10 ? "0" + hours : hours) + ":";
		} else {
			if (hours == 0) {
				out += "0" + hours + ":";
			}
		}
		if (days != 0 || hours != 0 || mins != 0) {
			out += (mins < 10 ? "0" + mins : mins) + ":";
		} else {
			if (mins == 0) {
				out += "0" + mins + ":";
			}
		}
		out += (secs < 10 ? "0" + secs : secs);
		_id.i("winAwaitedDiv", out);
		_gameGetShortTimer = setTimeout("gameShortTimer('" + (freezeTime - 1000) + "')", 1000);
	}
}
var _reqUPD = null;
var isReqActive = false;
function fetchUpdatedData() {
	clearTimeout(sUpdateData);
	timeLeftForFrz = uniqueDrawTimeArr[0] - curSerTime;
	//alert(timeLeftForFrz+"::::"+uniqueDrawTimeArr[0]+":::"+curSerTime);
	
	serverCallCounter();
	if (!isReqActive) {
		isReqActive = true;
		_reqUPD = XMLRequestDojo();
		//alert("Before call");
		var url = "updatedData.action";
		_reqUPD.onreadystatechange = stateChangeUpd;
		_reqUPD.open("GET", url, false);
		//alert("After CAll call");
		_reqUPD.setRequestHeader("If-Modified-Since", new Date().getTime());
		_reqUPD.send(null);
	}
	/*if (!isReqActive) {
		stateChangeUpd();
		isReqActive = true;
	} else {
		isReqActive = false;
	}*/
}
//var k = 2;
function stateChangeUpd() {
	//alert("stateChangeUpd");
	if (_reqUPD.readyState == 4) {
		if (_reqUPD.status == 200) {
			isReqActive = false;
			manipulateResp(_reqUPD.responseText);
			/*if (k % 2 == 0) {
		manipulateResp("Thu Jul 08 16:20:51 IST 2010=1278586251046ServerDate0to9UPD1278586380000, 1278586500000, 1278586620000, 1278586740000, 1278586860000, 1278586980000, 1278587100000, 1278587220000, 1278587340000, 1278587460000UPD1278586020000=3 Nxt 1278585900000=9 Nxt 1278585780000=6 Nxt 1278585660000=9 Nxt 1278585540000=8 Nxt 1278585420000=8 Nxt 1278585300000=9 Nxt 1278585180000=5 Nxt 1278585060000=4 Nxt UPDFREEZE=1278586260000Gamecard12UPD1278586380000, 1278586500000, 1278586620000, 1278586740000, 1278586860000, 1278586980000, 1278587100000, 1278587220000, 1278587340000, 1278587460000UPD1278586140000=ace_spade Nxt 1278586020000=ace_club Nxt 1278585900000=queen_club Nxt 1278585780000=queen_heart Nxt 1278585660000=queen_heart Nxt 1278585540000=king_diamond Nxt 1278585420000=ace_club Nxt 1278585300000=ace_heart Nxt 1278585180000=queen_club Nxt 1278585060000=king_club Nxt UPDFREEZE=1278586260000Gamecard16UPD1278586380000, 1278586560000, 1278586740000, 1278586920000, 1278587100000, 1278587280000, 1278587460000, 1278587640000, 1278587820000, 1278588000000UPD1278586200000=ace_heart Nxt 1278586020000=queen_heart Nxt 1278585840000=king_club Nxt 1278585660000=king_club Nxt 1278585480000=jack_club Nxt 1278585300000=ace_heart Nxt 1278585120000=jack_club Nxt UPDACTIVE=1278586380000GamefastlottoUPD1278586500000, 1278586740000, 1278586980000, 1278587220000, 1278587460000, 1278587700000, 1278587940000, 1278588180000, 1278588420000, 1278588660000UPD1278586020000=6,3,29,15,34 Nxt 1278585780000=29,6,3,13,2 Nxt 1278585540000=10,2,11,17,14 Nxt 1278585300000=32,25,10,22,24 Nxt 1278585060000=5,33,2,24,19 Nxt UPDFREEZE=1278586260000GamefortuneUPD1278586380000, 1278586500000, 1278586620000, 1278586740000, 1278586860000, 1278586980000, 1278587100000, 1278587220000, 1278587340000, 1278587460000UPD1278586140000=Pisces Nxt 1278586020000=Aries Nxt 1278585900000=Virgo Nxt 1278585780000=Aries Nxt 1278585660000=Scorpio Nxt 1278585540000=Capricorn Nxt 1278585420000=Gemini Nxt 1278585300000=Libra Nxt 1278585180000=Gemini Nxt 1278585060000=Cancer Nxt UPDFREEZE=1278586260000GamekenoUPD1278586500000, 1278586860000, 1278587220000, 1278587580000, 1278587940000, 1278588300000, 1278588660000, 1278589020000, 1278589380000, 1278589740000UPD1278586140000=27,12,18,35,64 Nxt 1278585780000=77,32,10,37,86 Nxt 1278585420000=88,56,66,42,76 Nxt 1278585060000=24,73,18,35,34 Nxt UPDACTIVE=1278586500000GamelottoUPD1278586320000, 1278586500000, 1278586680000, 1278586860000, 1278587040000, 1278587220000, 1278587400000, 1278587580000, 1278587760000, 1278587940000UPD1278586140000=11,36,38,24,8,33 Nxt 1278585960000=9,17,33,14,46,7 Nxt 1278585780000=5,33,47,49,34,46 Nxt 1278585600000=2,8,44,18,6,46 Nxt 1278585420000=38,9,1,48,3,39 Nxt 1278585240000=49,4,38,33,32,25 Nxt 1278585060000=4,15,17,31,30,38 Nxt UPDACTIVE=1278586320000GamezimlottoUPD1278586440000, 1278586740000, 1278587040000, 1278587340000, 1278587640000, 1278587940000, 1278588240000, 1278588540000, 1278588840000, 1278589140000UPD1278586140000=9,29,36,28,3,35 Nxt 1278585840000=11,24,25,32,12,10 Nxt 1278585540000=21,29,24,33,14,3 Nxt 1278585240000=36,12,28,13,33,3 Nxt UPDACTIVE=1278586440000Game");
		isReqActive = false;
	}
	k++;*/
			//fillGame(curActive);
		}
	}
}
function submitForm(action) {
	document.forms[0].action = action;
	if (childW) {
	} else {
		childW = window.open("", "NewWin", "width=250,height=10,toolbar=0,resizable=0,scrollbars=1,alwaysRaised=0,location=0,menubar=0");
		childW.close();
	}
	childW = window.open("", "NewWin", "width=250,height=250,toolbar=0,resizable=0,scrollbars=1,alwaysRaised=0,location=0,menubar=0");
	document.forms[0].target = "NewWin";
	document.forms[0].submit();
	document.forms[0].reset();
}
function scrollColor(a, b, height) {
	return "height:" + height + "px;overflow-y: auto;overflow-x: hidden;scrollbar-arrow-color:" + a + ";scrollbar-base-color:" + b + ";scrollbar-darkshadow-color:" + a + ";scrollbar-face-color:" + b + "; scrollbar-highlight-color:" + a + "; scrollbar-shadow-color:" + a + "";
}
function winAjaxReq(url) {
	var _req = null;
	var _result = false;
	url = _randCache(url);
	_req = XMLRequestDojo();
	_req.onreadystatechange = function () {
		if (_req.readyState == 4) {
			if (_req.status == 200) {
				_result = true;
			}
		}
	};
	_req.open("GET", url, false);
	_req.setRequestHeader("If-Modified-Since", new Date().getTime());
	_req.send(null);
	return {result:_result, data:_req.responseText};
}
var fillTimeOut;

//manipulateResp("Sat May 12 16:10:29 IST 2012=1336819229701ServerDatekenotwoUPD1336825800000, 1336883400000, 1336890600000, 1336897800000, 1336905000000, 1336912200000UPD1333987200000=87,21,68,88,82 Nxt 1333980000000=82,79,25,69,38 Nxt 1333972800000=56,13,76,50,58 Nxt 1333965600000=90,87,69,28,24 Nxt 1333958400000=40,81,83,33,68 Nxt 1333900800000=69,14,8,55,39 Nxt 1333893600000=28,6,2,19,82 Nxt 1333886400000=45,84,11,63,72 Nxt 1333879200000=41,54,82,86,31 Nxt 1333872000000=55,53,81,5,16 Nxt UPD1336912200000:881,Super Saturday,1336825800000:882,Lazy Sunday,1336883400000:883,Lazy Sunday,1336890600000:884,Lazy Sunday,1336897800000:885,Lazy Sunday,1336905000000:886,Lazy SundayUPDACTIVE=1336825800000GamezimlottothreeUPD1336832100000UPD1333820700000=5,6,9,15,21,32 Nxt 1333215900000=4,14,16,37,40,41 Nxt 1332611100000=3,12,16,26,33,34 Nxt 1332006300000=18,25,27,30,33,35 Nxt UPD17,null,1336832100000:UPDACTIVE=1336832100000GamezimlottotwoUPD1336832100000UPD1333820700000=5,6,9,15,21,32 Nxt 1333215900000=4,14,16,37,40,41 Nxt 1332611100000=3,12,16,26,33,34 Nxt 1332006300000=18,25,27,30,33,35 Nxt 1331401500000=7,11,37,38,39,42 Nxt 1330796700000=8,13,30,32,36,42 Nxt 1330191900000=5,8,11,18,24,34 Nxt 1329587100000=1,8,9,19,28,30 Nxt 1328982300000=16,19,23,25,31,36 Nxt 1328377500000=6,12,14,26,27,36 Nxt UPD59,null,1336832100000:UPDACTIVE=1336832100000Game");

function manipulateResp(data) {
	//alert("RESPONSE DATA::"+data);
	clearTimeout(fillTimeOut);
	var dateArr = data.split("ServerDate");
	//alert(dateArr);
	newServTime = dateArr[0];
	//alert("newServTime"+newServTime);
	var gameArr = dateArr[1].split("Game");
	//alert("gameArr"+gameArr);
	var newDrwArr = new Array();
	//alert("newDrwArr"+newDrwArr);
	//alert("gameArr.length"+gameArr.length);
	
	for (var i = 0; i < gameArr.length - 1; i++) {
		var dArr = gameArr[i].split("UPD");
		//alert("BEFORE NEW DRW ARRAY"+newDrwArr);
		newDrwArr = newDrwArr.concat(dArr[1].replace(/ /g, "").split(","));
		//alert("newDrwArr::"+newDrwArr);
		
	}
	//alert("BEFOREuniqueDrawTimeArr++"+uniqueDrawTimeArr);
	uniqueDrawTimeArr = unique(newDrwArr.sort());
	//alert("uniqueDrawTimeArr++"+uniqueDrawTimeArr);
	//alert("BeforecurSerTime"+curSerTime);
	curSerTime = parseFloat(newServTime.split("=")[1]);
	//alert("curSerTime"+curSerTime);
	
	for (var i = 0; i < gameArr.length - 1; i++) {
		var dArr = gameArr[i].split("UPD");
		//alert("dArr"+dArr);
		//alert("dArr.length"+dArr.length);
		if (_id.contains(activeGames, dArr[0].toLowerCase())) {
			//alert("dArr[0].toLowerCase()::"+dArr[0].toLowerCase()+"::dArr[2]::"+dArr[2]+"::dArr[3]::"+dArr[3]+"::dArr[4]::"+dArr[4]+"::dArr[1]"+dArr[1]);
			
		//	alert("BEFORE NEW DRW ARRAY"+newDrwArr);
			newDrwArr = newDrwArr.concat(dArr[1].replace(/ /g, "").split(","));
			//alert("newDrwArr::"+newDrwArr);
			var drawIdArr = dArr[3].split(':');
			//alert("drawIdArr=="+drawIdArr);
			//alert("drawIdArr.length-2=="+drawIdArr.length-2);
				for(j=0; j<drawIdArr.length-1;j++){
				var temp = drawIdArr[j].split(',');
				eval("  "+"_"+dArr[0]+"DrawIdObj[parseInt("+temp[2]+")] = '"+temp[0]+","+temp[1]+"' ;") ;
				}
			eval("_" + dArr[0].toLowerCase() + "UpData('" + dArr[2] + "','" + dArr[1] + "','" + dArr[4] + "')");
			visitGameArr[dArr[0].toLowerCase()] = new gameObj(dArr[0].toLowerCase());
			//alert("sumit ok");
			latestFreezeTime[dArr[0].toLowerCase()] = visitGameArr[dArr[0].toLowerCase()].latestDrawTimeO.split(",")[0];
		}
	}
	//alert(timeLeftForFrz + "**" + new Date(parseFloat(uniqueDrawTimeArr[0])) + "**" + new Date(parseFloat(curSerTime)));
	/*alert("BEFOREuniqueDrawTimeArr++"+uniqueDrawTimeArr);
	uniqueDrawTimeArr = unique(newDrwArr.sort());
	alert("uniqueDrawTimeArr++"+uniqueDrawTimeArr);
	alert("BeforecurSerTime"+curSerTime);
	curSerTime = parseFloat(newServTime.split("=")[1]);
	alert("curSerTime"+curSerTime);*/
	if (uniqueDrawTimeArr[0] > curSerTime) {
		
		timeLeftForFrz = uniqueDrawTimeArr[0] - curSerTime;
		//alert("timeLeftForFrz"+timeLeftForFrz);
	}
	//alert(timeLeftForFrz + "**" + new Date(parseFloat(uniqueDrawTimeArr[0])) + "**" + new Date(parseFloat(curSerTime)));
	
	fillTimeOut = setTimeout("fillGame('" + curActive + "')", 999);
}
function arrSort(str) {
	return (str.split(",").sort()).toString();
}
function pwtTable() {
	checkStatus("pwtStyl");
	var pwtTab = "<div style=\"height: 90px;\"><table border=\"0\" cellspacing=\"0\" cellpadding=\"0\" width=\"100%\"><tr><td><div id=\"pwtInnerDiv\"><table border=\"0\" cellspacing=\"0\" cellpadding=\"0\" width=\"100%\"><tr style=\"display: none\"><td align=\"left\"><input type=\"radio\" name=\"pwtRadio\" value=\"drawPwt\" onclick=\"showPwtDiv(this.value)\" checked=\"checked\" /></td><td align=\"left\">Draw Pwt </td><td align=\"left\"><input type=\"radio\" name=\"pwtRadio\" value=\"scratchPwt\" \tstyle=\"display: none\" onclick=\"showPwtDiv()\" /></td><td align=\"left\" style=\"display: none\">Scratch Pwt</td></tr><tr> \t\t\t\t\t\t<td colspan=\"4\" align=\"right\"> \t\t\t\t\t\t\t<div id=\"pwtDrawDiv\"> \t\t\t\t\t\t\t\t<table width=\"100%\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\" style=\"height: 100%;\"><tr><td align=\"left\"><b> Enter Ticket No.</b></td></tr><tr><td align=\"left\"><input type=\"text\" maxlength=\"20\"  id=\"pwtTickNum\" name=\"pwtTickNum\" value=\"\" onblur=\"checkSpcChar()\"/> </td></tr><tr><td align=\"left\"><input type=\"button\" value=\"submit\" onclick=\"verifyPwt()\" /></td></tr></table></div> \t\t\t\t\t\t</td> \t\t\t\t\t</tr> \t\t\t\t\t<tr> \t\t\t\t\t\t<td colspan=\"4\"> \t\t\t\t\t\t\t<div id=\"pwtScratchDiv\"> \t\t\t\t\t\t\t\t<table border=\"0\" cellspacing=\"0\" cellpadding=\"0\" width=\"100%\"> \t\t\t\t\t\t\t\t\t<tr> \t\t\t\t\t\t\t\t\t\t<td colspan=\"2\"> \t\t\t\t\t\t\t\t\t\t\t<div style=\"color: white; text-align: center\" id=\"gName_e\"></div> \t\t\t\t\t\t\t\t\t\t</td> \t\t\t\t\t\t\t\t\t</tr> \t\t\t\t\t\t\t\t\t<tr> \t\t\t\t\t\t\t\t\t\t<td align=\"right\"> \t\t\t\t\t\t\t\t\t\t\t<label for=\"game_name\" class=\"label\"> \t\t\t\t\t\t\t\t\t\t\t\tGame: \t\t\t\t\t\t\t\t\t\t\t</label> \t\t\t\t\t\t\t\t\t\t</td> \t\t\t\t\t\t\t\t\t\t<td align=\"left\"> \t\t\t\t\t\t\t\t\t\t\t<select name=\"gameIdNbrName\" id=\"game_name\" class=\"option\"> \t\t\t\t\t\t\t\t\t\t\t\t<option value=\"-1\"> \t\t\t\t\t\t\t\t\t\t\t\t\t--Please Select-- \t\t\t\t\t\t\t\t\t\t\t\t</option> \t\t\t\t\t\t\t\t\t\t\t</select> \t\t\t\t\t\t\t\t\t\t</td> \t\t\t\t\t\t\t\t\t</tr> \t\t\t\t\t\t\t\t\t<tr> \t\t\t\t\t\t\t\t\t\t<td colspan=\"2\"> \t\t\t\t\t\t\t\t\t\t\t<div \t\t\t\t\t\t\t\t\t\t\t\tstyle=\"color: white; font-size: 14px; text-align: center\" \t\t\t\t\t\t\t\t\t\t\t\tid=\"tktNo_e\"> \t\t\t\t\t\t\t\t\t\t\t\t<s:property value=\"pwtErrorMap.tktBean.status\" /> \t\t\t\t\t\t\t\t\t\t\t</div> \t\t\t\t\t\t\t\t\t\t</td> \t\t\t\t\t\t\t\t\t</tr> \t\t\t\t\t\t\t\t\t<tr> \t\t\t\t\t\t\t\t\t\t<td align=\"right\"> \t\t\t\t\t\t\t\t\t\t\t<label for=\"tktNo\" class=\"label\"> \t\t\t\t\t\t\t\t\t\t\t\tTic No: \t\t\t\t\t\t\t\t\t\t\t</label> \t\t\t\t\t\t\t\t\t\t</td> \t\t\t\t\t\t\t\t\t\t<td align=\"left\"> \t\t\t\t\t\t\t\t\t\t\t<input type=\"text\" name=\"ticketNbr\" value=\"\" id=\"tktNo\" /> \t\t\t\t\t\t\t\t\t\t</td> \t\t\t\t\t\t\t\t\t</tr> \t\t\t\t\t\t\t\t\t<tr> \t\t\t\t\t\t\t\t\t\t<td colspan=\"2\"> \t\t\t\t\t\t\t\t\t\t\t<div \t\t\t\t\t\t\t\t\t\t\t\tstyle=\"color: white; font-size: 14px; text-align: center\" \t\t\t\t\t\t\t\t\t\t\t\tid=\"virnNbr_e\"> \t\t\t\t\t\t\t\t\t\t\t\t<s:property value=\"pwtErrorMap.pwtBean.message\" /> \t\t\t\t\t\t\t\t\t\t\t</div> \t\t\t\t\t\t\t\t\t\t</td> \t\t\t\t\t\t\t\t\t</tr> \t\t\t\t\t\t\t\t\t<tr> \t\t\t\t\t\t\t\t\t\t<td align=\"right\"> \t\t\t\t\t\t\t\t\t\t\t<label for=\"virnNbr\" class=\"label\"> \t\t\t\t\t\t\t\t\t\t\t\tVIRN: \t\t\t\t\t\t\t\t\t\t\t</label> \t\t\t\t\t\t\t\t\t\t</td> \t\t\t\t\t\t\t\t\t\t<td align=\"left\"> \t\t\t\t\t\t\t\t\t\t\t<input type=\"text\" name=\"virnNbr\" value=\"\" id=\"virnNbr\" /> \t\t\t\t\t\t\t\t\t\t</td> \t\t\t\t\t\t\t\t\t</tr> \t\t\t\t\t\t\t\t\t<tr> \t\t\t\t\t\t\t\t\t<tr> \t\t\t\t\t\t\t\t\t\t<td align=\"right\"> \t\t\t\t\t\t\t\t\t\t\t&nbsp; \t\t\t\t\t\t\t\t\t\t</td> \t\t\t\t\t\t\t\t\t\t<td colspan=\"2\" align=\"left\"> \t\t\t\t\t\t\t\t\t\t\t<input type=\"button\" id=\"subTrans\" name=\"search\" \t\t\t\tstyle=\"cursor: hand;\"\t\t\t\t\t\t\t\tvalue=\"Verify And Save\" onclick=\"verifyScratchPwt()\" /> \t\t\t\t\t\t\t\t\t\t</td> \t\t\t\t\t\t\t\t\t</tr> \t\t\t\t\t\t\t\t</table> \t\t\t\t\t\t\t</div> \t\t\t\t\t\t</td> \t\t\t\t\t</tr> \t\t\t\t</table> \t\t\t</div> \t\t</td> \t</tr> \t<tr> \t\t<td> </td> \t</tr> </table></div>";
	_id.i("pwtDiv", pwtTab);
	_id.o("pwtTickNum").focus();
	showPwtDiv("drawPwt");
	//alert(document.getElementById('pwtTickNum'));
}
function checkSpcChar() {
	var num = _id.o("pwtTickNum").value;
	if (isNaN(num)) {
		_id.o("pwtTickNum").value = "";
	}
}
function showPlrDet(val) {
	if (val == "anonymous") {
		_id.non("plrDetail");
	} else {
		_id.blk("plrDetail");
	}
}
function verifyPlrDetails() {
	var playerTypeArr = document.getElementsByName("playerType");
	var playerType;
	for (var i = 0; i < playerTypeArr.length; i++) {
		if (playerTypeArr[i].checked) {
			playerType = playerTypeArr[i].value;
			break;
		}
	}
	if (isDtetailsValid(playerType)) {
		var plrRegUrl = path + "/com/skilrock/lms/web/pwtMgmt/pwt_plrRPOSRegAndApprovalReq.action?firstName=" + document.getElementsByName("firstName")[0].value + "&lastName=" + document.getElementsByName("lastName")[0].value + "&idType=" + document.getElementsByName("idType")[0].value + "&idNumber=" + document.getElementsByName("idNumber")[0].value + "&playerType=" + playerType;
		_id.i("pwtResult", "<iframe src=\"" + plrRegUrl + "\" name=\"tktFrame\" id=\"tktFrame\" style=\"width:230px;height:300px;\" frameborder=\"0\"></iframe>");
	}
}
function isDtetailsValid(playerType) {
	var valid = true;
	if (playerType == "player") {
		if (_id.o("firstName").value == "") {
			_id.i("fName_e", i18nObj.prop('error.fname.empty'));
			valid = false;
		} else {
			_id.i("fName_e", "");
		}
		if (_id.o("lastName").value == "") {
			_id.i("lName_e", i18nObj.prop('error.lname.empty'));
			valid = false;
		} else {
			_id.i("lName_e", "");
		}
		if (_id.o("idNumber").value == "") {
			_id.i("idNum_e", i18nObj.prop('error.pls.entr.id.nbr'));
			valid = false;
		} else {
			_id.i("idNum_e", "");
		}
	}
	return valid;
}
function showPwtDiv(val) {
	if (val == "drawPwt") {
		_id.blk("pwtDrawDiv");
		_id.non("pwtScratchDiv");
	} else {
		if (val == "drawCanTable") {
		} else {
			if (val == "drawReprintTable") {
			} else {
				if(val == "drawTrackTPTable"){
				}else{
				gameDetails();
				_id.blk("pwtScratchDiv");
				_id.non("pwtDrawDiv");
			}
		}
	}
	_id.i("pwtResult", "");
	_id.blk("pwtResult");
	_id.o("parentApplet").style.visibility = 'hidden';
}}
function verifyScratchPwt() {
	if (isValidTktInput()) {
		var urlScratch = path + "/com/skilrock/lms/web/pwtMgmt/pwt_retRPOS_verifyAndSaveTicketNVirn.action?ticketNbr=" + document.getElementsByName("ticketNbr")[0].value + "&virnNbr=" + document.getElementsByName("virnNbr")[0].value + "&gameIdNbrName=" + document.getElementsByName("gameIdNbrName")[0].value;
		_id.i("pwtResult", "<iframe src=\"" + urlScratch + "\" name=\"tktFrame\" id=\"tktFrame\" style=\"width:230px;height:300px;\" frameborder=\"0\"></iframe>");
		parent.frames[0].updateBalance(projectName + "/com/skilrock/lms/web/loginMgmt/updateAvlCreditAmt.action");
	}
}
function isValidTktInput() {
	if (_id.o("game_name").value == "-1") {
		_id.i("gName_e", i18nObj.prop('error.pls.slct.game'));
		return false;
	} else {
		_id.i("gName_e", "");
		return true;
	}
}
var pwttktCount = 0;
function verifyPwtAuto(e) {
	var keyCode = (window.event) ? event.keyCode : e.which;
	if ((keyCode >= 48 && keyCode < 58) || keyCode == 45 || keyCode == 27 || keyCode == 13 || keyCode == 40 || keyCode == 38 || keyCode == 46 || keyCode == 8 || (keyCode >= 96 && keyCode < 106 || keyCode == 9)) {
		pwttktCount = _id.o("pwtTickNum").value.length;
	} else {
		return false;
	}
	/*if (ticketLength == pwttktCount) {
		checkSpcChar();
		verifyPwt();
		pwttktCount = 0;
	}*/
	return true;
}
function verifyPwt() {
	var tktNum = document.getElementsByName("pwtTickNum")[0].value;
	_id.o("regDiv").innerHTML = "";
	_id.o("regButton").innerHTML = "";
	if (tktNum == "" || isNaN(parseInt(tktNum)) || tktNum == 0 /*|| (tktNum.length <16)*/) {
		alert(i18nObj.prop('error.pls.entr.valid.tkt'));
		document.getElementsByName("pwtTickNum")[0].value = '';
		return false;
	}else if(tktNum.length == 16 || tktNum.length == 18 || tktNum.length == 20){
		pwtAjaxReq(projectName + "/com/skilrock/lms/web/drawGames/pwtMgmt/verifyTicket.action?ticketNo=" + tktNum, true);
		_id.o("pwtTickNum").value = "";
		return true;
	}else{
		alert(i18nObj.prop('error.pls.entr.valid.tkt'));
		document.getElementsByName("pwtTickNum")[0].value = '';
		return false;
	}
}
function printFunc() {
	window.frames["tktFrame"].focus();
//alert(isPrintable);
	if (isPrintable == true) {
		//window.frames["tktFrame"].print();
	}
}
function updBalIframe() {
	parent.frames[0].updateBalance(projectName + "/com/skilrock/lms/web/loginMgmt/updateAvlCreditAmt.action");
}
function pwtAjaxReq(url, isPay) {
	if (isPay) {
		isPayAction = true;
	} else {
		isPayAction = false;
	}
	curTrx = "PWT";
	reprintCountChild = 0;
	url = _randCache(url);
	totTktAmt = 0;
	_id.i("pwtResult", "<iframe src=\"" + url + "\" name=\"tktFrame\" id=\"tktFrame\" style=\"width:230px;height:300px;\" frameborder=\"0\"></iframe>");
}
function tpPwtAjaxReq(url, isPay) {
	if (isPay) {
		isPayAction = true;
	} else {
		isPayAction = false;
	}
	curTrx = "TPPWT";
	reprintCountChild = 0;
	url = _randCache(url);
	totTktAmt = 0;
	_id.i("pwtResult", "<iframe src=\"" + url + "\" name=\"tktFrame\" id=\"tktFrame\" style=\"width:230px;height:300px;\" frameborder=\"0\"></iframe>");
}
function regPlr() {
	var regTab = "<div style=\"height:250;valign:top;\"><table border=\"0\" width=\"100%\" height=\"100%\" valign=\"top\"><tr><td colspan=\"2\" align=\"center\"><b>Player Registration</b></td></tr><tr><td align=\"left\"><b>First Name</b></td><td><input type=\"text\" name=\"firstName\" value=\"\"/></td></tr><tr><td align=\"left\"><b>Last Name</b></td><td><input type=\"text\" name=\"lastName\" value=\"\"/></td></tr><tr><td align=\"left\"><b>Id Number</b></td><td><input type=\"text\" name=\"idNumber\" value=\"\"/></td></tr><tr><td align=\"left\"><b>Id Type</b></td><td><select name=\"idType\" id=\"idType\" class=\"option\" ><option value=\"-1\">-- Please Select --</option><option value=\"SSL\">SSL</option><option value=\"DL\">DL</option><option value=\"PANCARD\">PANCARD</option><option value=\"VOTERCARD\">VOTERCARD</option><option value=\"PASSPORT\">PASSPORT</option><option value=\"NRC\">NRC</option></select></td></tr><tr><td colspan = \"2\" align=\"right\"><input type=\"button\" value =\"submit\" onclick=\"regPlrSubmit()\"/></td></tr></table></div>";
	_id.i("regDiv", regTab);
	_id.o("parentApplet").scrollTop = 1;
}
function regPlrSubmit() {
	if (_id.n("firstName")[0].value == "" || _id.n("lastName")[0].value == "" || _id.n("idType")[0].value == -1 || _id.n("idNumber")[0].value == "") {
		alert(i18nObj.prop('error.pls.fill.complete.entries'));
		return false;
	}
	var url = "LMS_Mgmt_NewDB/WebRoot/com/skilrock/lms/web/drawGames/pwtMgmt/registerPlayer.action?firstName=" + _id.n("firstName")[0].value + "&lastName=" + _id.n("lastName")[0].value + "&idType=" + _id.n("idType")[0].value + "&idNumber=" + _id.n("idNumber")[0].value;
	_resp = winAjaxReq(url);
	if (_resp.result) {
		_id.i("regDiv", "<b>" + _resp.data + "</b><br /><input type='button' value='Proceed' onclick='pwtTable()'/>");
	}
}
function cancelTable() {
	checkStatus("canTktStyl");
	var canTab = "";
	if (cancelType == "LAST_SOLD_TICKET") {
		canTab = "<div style=\"height: 90px;\"><div id=\"cancelTicDiv\"><table border=\"0\" width=\"100%\" style=\"height:50%;\"><tr><td align=\"center\"> <a href=\"#\" onclick=\"cancelLastTkt()\">Cancel Last Ticket</a></td></tr></table></div><div id=\"cancelResDiv\"></div></div>";
	} else {
		canTab = "<div style=\"height: 90px;\"><div id=\"cancelTicDiv\"><table border=\"0\" width=\"100%\" cellspacing=\"0\" cellpadding=\"0\" style=\"height: 50%;\"><tr><td align=\"left\"><b> Cancel Ticket No.</b></td> </tr><tr><td align=\"left\"><input type=\"text\" id=\"cancelTickNum\" onkeyup=\"return cancelAutoEnter()\" maxlength=\"16\" name=\"cancelTickNum\" value=\"\" onblur=\"ChkSpecialChar(this.id)\"/> </td></tr><tr><td align=\"left\"><input type=\"button\" value=\"submit\"  onclick=\"return cancelTkt()\" /></td></tr> <tr><td><div id=\"cancelResDiv\"></div></td></tr></table></div></div>";
	}
	_id.i("pwtDiv", canTab);
	showPwtDiv("drawCanTable");
}
function cancelAutoEnter(){
	var flag=true;
	if(_id.o("cancelTickNum").value.length == 16){
		flag = cancelTkt();
	}
	return flag;
}
function ChkSpecialChar(id) {
	var num = _id.o(id).value;
	if (isNaN(num)) {
		_id.o(id).value = "";
	}
}
function cancelTkt() {
	curTrx = "CANCEL";
	var tktNum = _id.n("cancelTickNum")[0].value;
	if (tktNum == "" || isNaN(parseInt(tktNum))) {
		alert(i18nObj.prop('error.pls.entr.valid.tkt'));
		return false;
	}
	var chkCancel = confirm(i18nObj.prop('msg.cancel.tkt.no') + tktNum);
	if (!chkCancel) {
		return false;
	}
	_id.o("parentApplet").style.visibility = 'hidden';
	var res = "";
	//alert(tktNum);
	_resp = winAjaxReq("cancelTicket.action?autoCancel=false&ticketNo=" + tktNum);
	//alert(_resp.data);
	if (_resp.result) {
		if (_resp.data.match(/Sorry<br>Your Sale Time Period has been expired <br> Thank you/ig)) {
			document.getElementById("pwtDiv").innerHTML = i18nObj.prop('msg.sorry.your.sale.time.period.exp');
		} else {
			if (_resp.data.match(/Cancel Ticket Limit Exceed/)) {
				displayErrMsg(i18nObj.prop('msg.cancel.tkt.exceed'));
			} else {
				if (_resp.data.match(/refundAmt/)) {
					var tktAmt = _resp.data.substring(_resp.data.indexOf("refundAmt=") + 10, _resp.data.indexOf("|ctr="));
					res = "Ticket Cancelled. <br> Refund Amount is: " + tktAmt;
					var cancelData = "mode=Cancel|tktStatus=Cancelled|" + _resp.data;
					setAppData(cancelData);
				} else {
					res = _resp.data;
				}
			}
		}
	}
	_id.i("cancelResDiv", res);
	_id.o("cancelTickNum").value = "";
	parent.frames[0].updateBalance(projectName + "/com/skilrock/lms/web/loginMgmt/updateAvlCreditAmt.action");
	return true;
}
function cancelLastTkt() {
	curTrx = "CANCEL";
	var chkCancel = confirm(i18nObj.prop('msg.cancel.last.tkt'));
	if (!chkCancel) {
		return false;
	}
	var res = "";
	_resp = winAjaxReq("cancelTicket.action?autoCancel=false");
	//alert(_resp.data);
	if (_resp.result) {
		if (_resp.data.match(/Sorry<br>Your Sale Time Period has been expired <br> Thank you/ig)) {
			document.getElementById("pwtDiv").innerHTML = i18nObj.prop('msg.sorry.your.sale.time.period.exp');
		} else {
			if (_resp.data.match(/Cancel Ticket Limit Exceed/)) {
				displayErrMsg(i18nObj.prop('msg.cancel.tkt.exceed'));
			} else {
				if (_resp.data.match(/refundAmt/)) {
					var tktAmt = _resp.data.substring(_resp.data.indexOf("refundAmt=") + 10, _resp.data.indexOf("|ctr="));
					res = i18nObj.prop('msg.tkt.cancelled')+" <br> "+i18nObj.prop('msg.refunded.amt')  + tktAmt;
					var cancelData = "mode=Cancel|tktStatus=Cancelled|" + _resp.data;
					setAppData(cancelData);
					//alert(cancelData);
				} else {
					res = _resp.data;
				}
			}
		}
	}
	_id.i("cancelResDiv", res);
	parent.frames[0].updateBalance(projectName + "/com/skilrock/lms/web/loginMgmt/updateAvlCreditAmt.action");
	return true;
}
function reprintTable() {
	checkStatus("repTktStyl");
	var repTab = "";
	if (reprintType == "LAST_SOLD_TICKET") {
		repTab = "<div style=\"height: 90px;\"><div id=\"repTicDiv\"><table border=\"0\" width=\"100%\" style=\"height:100%;\"><tr><td align=\"center\"> <a href=\"#\" onclick=\"reprintLastTkt()\">Reprint Last Ticket</a></td></tr></table></div><div id=\"repResDiv\"></div></div>";
	} else {
		repTab = "<div style=\"height: 90px;\"><div id=\"repTicDiv\"><table border=\"0\" width=\"100%\" cellspacing=\"0\" cellpadding=\"0\" style=\"height: 50%;\"><tr><td align=\"left\"><b> Reprint Ticket No.</b></td> </tr><tr><td align=\"left\"><input type=\"text\" id=\"reprintTickNum\" name=\"reprintTickNum\" value=\"\" onblur=\"ChkSpecialChar(this.id)\"/> </td></tr><tr><td align=\"left\"><input type=\"button\" value=\"submit\" onclick=\"return reprintTkt()\" /></td></tr></table></div></div>";
	}
	_id.i("pwtDiv", repTab);
	showPwtDiv("drawReprintTable");
}

function trackTPTable() {
	checkStatus("trackTPTktStyl");
	var repTab = "";
		repTab = "<div style=\"height: 90px;\"><div id=\"trkTPTktDiv\"><table border=\"0\" width=\"100%\" style=\"height:100%;\"><tr><td align=\"center\" colspan='2'>Track Third Party Ticket</td></tr><tr></tr><tr><td align='right'>Enter Ticket No.</td><td><input type='text' id='ticketNumber' name='ticketNumber' /></td></tr><tr><td align='center'  colspan='2'><input type='button' onclick=\"trackTPTicket()\" name='Submit' value='Submit' /></td></tr></table><table id='vertable' style='display:none;'> <tr><td>Enter Code</td><td><input type='text' id='verCode'  style='width:60px; name='verificationCode' /></td><td ><input type='button' id='subButn'  onclick=\"return editVerBox();\" name='submit' value='Submit' /></td></tr></table>" +
				"</div><div id=\"repResDiv\"></div></div>";
	_id.i("pwtDiv", repTab);
	showPwtDiv("drawTrackTPTable");
}
function editVerBox()
{
	
	var verCon = _id.o("verCode").value;
	if(verCon == '' || verCon == null){
		alert("Please enter verification code.");
		return false;
	}
	else{
	_id.o("ticketNumber").value = '';
	document.getElementById("vertable").style.display = 'none';
	var url = projectName + "/com/skilrock/lms/web/drawGames/pwtMgmt/payTpTicketProcessRet.action?verificationCode="+verCon;
	tpPwtAjaxReq(url, false);
	_id.o("verCode").value = '';
	}
}
function trackTPTicket()
{
	showPwtDiv("drawTrackTPTable");
	var tktNum = _id.o("ticketNumber").value;
	if(tktNum.length == 15 || tktNum.length == 18){
		tpPwtAjaxReq(projectName + "/com/skilrock/lms/web/drawGames/pwtMgmt/verifyTpTicketRet.action?ticketNumber=" + tktNum, true);
		return true;
	}else{
		alert(i18nObj.prop('error.pls.entr.valid.tkt'));
		_id.o("ticketNumber").value = '';
		return false;
	}
}


function payTPTicket()
{
	var url = "/LMSLinuxNew/com/skilrock/lms/web/drawGames/pwtMgmt/payTpTicketProcessRet.action?verificationCode=" + _id.o("verCode").value;
	_resp = winAjaxReq(url);
	var data = _resp.data;
	
	if (data.indexOf('Error') > -1) {
		var code = data.split("|")[1].split(":")[1];
		var msg = data.split("|")[0].split(":")[1];
		if(code == '124'){
		alert(msg);
		_id.o("verCode").value="";
		}
		else{
			alert(msg);
			_id.o("vertable").style.display = "none";
		}
	}
	else{
		alert(data);
		_id.o("vertable").style.display = "none";
	}
	
	
	}


function reprintLastTkt() {
	var chkReprint = confirm(i18nObj.prop('msg.reprint.last.tkt'));
	if (!chkReprint) {
		return false;
	}
	reprintTktAjaxReq("reprintTicket.action");
}
function reprintTkt() {
	var tktNum = _id.v("reprintTickNum");
	var chkReprint = confirm(i18nObj.prop('msg.reprint.tkt.no')+" " + tktNum);
	if (!chkReprint) {
		return false;
	}
	reprintTktAjaxReq("reprintTicket.action?ticketNo=" + tktNum);
	return true;
}
function max() {
	if (!isMax) {
		var obj = new ActiveXObject("Wscript.shell");
		obj.SendKeys("{F11}");
		isMax = true;
	}
}
function gameBuyAjaxReq(url) {
	//alert(url);
	var checkSumUrl = getStringForCRC(url);
	var crc = '&CRC='+getCheckSum32(checkSumUrl);
	url = url+crc;
	var amtUrl = url.substring(url.indexOf("totalPurchaseAmt=") + 17, url.length);
	var amt = amtUrl.substring(0, ((amtUrl.indexOf("&") == -1) ? amtUrl.length : amtUrl.indexOf("&")));
	if (amt > 10000) {
		var isBuy = confirm(i18nObj.prop('msg.are.u.sure.u.want.to.buyThis.tkt'));
		if (!isBuy) {
			_id.o("buy").disabled = false;
			allowBuy = true;
			return;
		}
	}
	curTrx = "BUY";
	reprintCountChild = 0;
	checkStatus();
	url = _randCache(url);
	_id.i("pwtResult", "<iframe src=\"" + url + "\" name=\"tktFrame\" id=\"tktFrame\" style=\"width:230px;height:300px;\" frameborder=\"0\" scrolling=\"yes\"></iframe>");
}
function reprintTktAjaxReq(url) {
	curTrx = "RPT";
	reprintCountChild = 0;
	url = _randCache(url);
	_id.i("pwtResult", "<iframe src=\"" + url + "\" name=\"tktFrame\" id=\"tktFrame\" style=\"width:230px;height:300px;\" frameborder=\"0\" scrolling=\"yes\"></iframe>");
}
window.onerror = null;
var bName = navigator.appName;
var bVer = parseInt(navigator.appVersion);
var NS4 = (bName == "Netscape" && bVer >= 4);
var IE4 = (bName == "Microsoft Internet Explorer" && bVer >= 4);
var NS3 = (bName == "Netscape" && bVer < 4);
var IE3 = (bName == "Microsoft Internet Explorer" && bVer < 4);
var blink_speed = 300;
var i = 0;
var clrChng = 0;
if (NS4 || IE4) {
	if (navigator.appName == "Netscape") {
		layerStyleRef = "layer.";
		layerRef = "document.layers";
		styleSwitch = "";
	} else {
		layerStyleRef = "layer.style.";
		layerRef = "document.all";
		styleSwitch = ".style";
	}
}


//BLINKING
function Blink(layerName) {
	if (NS4 || IE4) {
		if (i % 2 == 0) {
			document.getElementById(layerName).style.visibility = "visible";
		} else {
			document.getElementById(layerName).style.visibility = "hidden";
		}
	}
	if (i < 1) {
		i++;
	} else {
		i--;
	}
}
function colorChange(layerName) {
	if (NS4 || IE4) {
		if (clrChng % 2 == 0) {
			document.getElementById(layerName).style.backgroundColor = "#326f8e";
		} else {
			document.getElementById(layerName).style.backgroundColor = "blue";
		}
	}
	if (clrChng < 1) {
		clrChng++;
	} else {
		clrChng--;
	}
}
function getGameDrawTime() {
	this.getDrawTime = getDrawTime;
	this.gameShortTimer = gameShortTimer;
}
var dateNow = new Date();
function getDrawTime() {
	document.getElementById("curDrwTime").style.fontSize = "13px";
	var counter = 0;
	var timerData = _id.o("dateFuture").value;
	timerData = timerData.split(",");
	dateFuture = new Date(timerData[0], timerData[1], timerData[2], timerData[3], timerData[4], timerData[5]);
	//alert("dfgdfg");
	frzTime = (dateFuture.getTime() - freezeTime * 1000) - curSerTime;
	
	//alert((dateFuture - freezeTime * 1000)+"****"+curSerTime+"****"+frzTime+"****"+frzCount);
	GetCount();
}
function GetCount() {
	clearTimeout(GetCountTimer);
	var milliSec = 0;
	frzTimeLeft = frzTime - frzCount * 1000;
	if ((frzTimeLeft) < 0) {
		_id.i("nxtDrawDiv", "Updating Data...");
		_id.i("nxtDrawTime", "Updating Data...");
		aftFrz();
	} else {
		days = 0;
		hours = 0;
		mins = 0;
		secs = 0;
		out = "";
		frzTimeLeft = Math.floor(frzTimeLeft / 1000);  // seconds
		days = Math.floor(frzTimeLeft / 86400);
		frzTimeLeft = frzTimeLeft % 86400;
		hours = Math.floor(frzTimeLeft / 3600);//hours
		frzTimeLeft = frzTimeLeft % 3600;
		mins = Math.floor(frzTimeLeft / 60);//minutes
		frzTimeLeft = frzTimeLeft % 60;
		secs = Math.floor(frzTimeLeft);//seconds
		if (days != 0) {
			_id.o("timeLeft").style.display = "none";
		} else {
			if (hours != 0 && mins != 0) {
				_id.o("timeLeft").style.display = "block";
				out = hours + " hr " + mins +" mins";
			} else {
				if (hours != 0 && mins == 0) {
					_id.o("timeLeft").style.display = "block";
					out = hours + " hr " + (mins < 10 ? "0" + mins : mins) + " min";
				} else {
					if (hours == 0 && mins != 0) {
						_id.o("timeLeft").style.display = "block";
						out = (mins < 10 ? "0" + mins : mins) + " min " + (secs < 10 ? "0" + secs : secs) + "sec";
					} else {
						if (hours == 0 && mins == 0) {
							_id.o("timeLeft").style.display = "block";
							out = (secs < 10 ? "0" + secs : secs) + " seconds";
						}
					}
				}
			}
		}
		_id.i("curDrwTime", out);
		frzCount++;
		GetCountTimer = setTimeout("GetCount()", 1000);
	}
}
var _reqTime = XMLRequestDojo();

// GETTING SERVER TIME 
function fetchTimeData() {
	_reqTime = XMLRequestDojo();
	var url = "getServerTime.action";
	_reqTime.onreadystatechange = stateChangeTime;
	_reqTime.open("GET", url, false);
	_reqTime.setRequestHeader("If-Modified-Since", new Date().getTime());
	_reqTime.send(null);
}
function stateChangeTime() {
	if (_reqTime.readyState == 4) {
		if (_reqTime.status == 200) {
			newServTime = _reqTime.responseText;
		}
	}
}

//  FOR MANAGING THE SPEED OF OPENING GAME INFORMATION DIV 
var ContentHeight = 400;
var TimeToSlide = 2100;
var openAccordion = "";
var isDivOpen = false;
var openingId = "";
function runAccordion(index) {
	isDivOpen = false;
	openAccordion = "";
	var nID = "info" + index + "Div";
	if (openAccordion == nID) {
		nID = "";
	}
	setTimeout("animate(" + new Date().getTime() + "," + TimeToSlide + ",'" + openAccordion + "','" + nID + "')", 33);
	openAccordion = nID;
}
function animate(lastTick, timeLeft, closingId, openingId) {
	openingId = openingId;
	var curTick = new Date().getTime();
	var elapsedTicks = curTick - lastTick;	//alert(openingId);
	var opening = (openingId == "") ? null : document.getElementById(openingId);
	var closing = (closingId == "") ? null : document.getElementById(closingId);
	if (timeLeft <= elapsedTicks) {
		if (opening != null) {
			opening.style.height = ContentHeight + "px";
			isDivOpen = true;
		}
		return;
	}
	timeLeft -= elapsedTicks;
	var newClosedHeight = Math.round((timeLeft / TimeToSlide) * ContentHeight);
	if (opening != null) {
		opening.style.display = "block";
		opening.style.height = (ContentHeight - newClosedHeight) + "px";
	}
	if (closing != null) {
		closing.style.height = newClosedHeight + "px";
	}
	setTimeout("animate(" + curTick + "," + timeLeft + ",'" + closingId + "','" + openingId + "')", 33);
}
function hideDiv() {
	_id.o("info5Div").style.display = "none";
	_id.o("topId").style.display = "none";
}


// DISPLAY GAME INFO INTO NEW DIV
function gameInfo() {
	var url = curActive + "Info.action";
	_id.o("topId").style.display = "block";
	runAccordion("5");
	_id.o("info5Div").style.display = "block";
	_id.i("info5Div", "<iframe src=\"" + url + "\" id=\"tktFrame\" style=\"width:100%;height:100%\" frameborder=\"0\"></iframe>");
	_id.o("info5Div").style.marginLeft = "30px";
	_id.o("info5Div").style.marginTop = "60px";
}

function specificDrawPopUp(drawSelect){
	if(_id.n('conOrNonCon')[1]!= undefined && _id.n('conOrNonCon')[1].checked){
	_id.o('drawSelectorDiv').style.visibility = 'visible';
	_id.o('drawSelectorDiv').style.zIndex = '1';	
	_id.i('drawSelectorDiv', drawSelect);
		//showOverlay();
	}
}


function showOverlay() {
	if (document.getElementById('facebox_overlay'))
		document.getElementById('facebox_overlay').style.position= 'absolute';
		document.getElementById('facebox_overlay').style.top= '0px';
		document.getElementById('facebox_overlay').style.left= '0px';
		document.getElementById('facebox_overlay').style.height='200%';
		document.getElementById('facebox_overlay').style.width='200%';
		//document.getElementById('facebox_overlay').style.backgroundColor='#000000';
		document.getElementById('facebox_overlay').style.zIndex='0';		
		//document.getElementById('facebox_overlay').style.opacity='0.1';
	return false;
}

function hideOverlay() {
	//document.getElementById('facebox_overlay').style.backgroundColor='#FFFFFF';
	document.getElementById('facebox_overlay').style.zIndex='1';
	document.getElementById('facebox_overlay').style.opacity='1';
	return false;
}


function drawSelectButton(obj){
	if(obj == undefined){
		eval('_'+curActive+'DrawIdArr = new Array()');
		drwIdInpArr = document.getElementsByName('drawIdChkBox');
		html = '<table><tr><td colspan="3">'+i18nObj.prop('msg.you.slct.following.draws')+'</td></tr>';
		j=0;
		for(i=0; i<drwIdInpArr.length;i++){
			if(drwIdInpArr[i].checked == true){
				html += '<tr>';
				html += '<td>'+(j+1) + '</td><td align="center" width="30%" nowarp="nowrap">'+ drwIdInpArr[i].value +'</td><td>'+drwIdInpArr[i].title+'</td>';
				html += '</tr>';
				//alert(drwIdInpArr[i].id);
				eval('_'+curActive+'DrawIdArr[j]= drwIdInpArr[i].id.split(\'_\')[0]');
				j++;
			}
		}
		if(j==0){
			_id.n('conOrNonCon')[0].checked=true;
			html = '<table><tr><td colspan="3">'+i18nObj.prop('msg.no.advance.draws.slct')+' </td></tr>';
		}
		_id.i('selectedDrawList',html);
			//hideOverlay();
	}else{
		eval('_'+curActive+'DrawIdArr = new Array()');
		_id.i('selectedDrawList',i18nObj.prop('msg.advance.play.mode.disabled'));
	}
	eval('_'+curActive+'CalAmt()');
	_id.o('drawSelectorDiv').style.visibility = 'hidden';
}

function drawSelectButtonKenoSix(obj){
	if(obj == undefined){
		var colorVar = 0;
		eval('_'+curActive+'DrawIdArr = new Array()');
		drwIdInpArr = document.getElementsByName('drawIdChkBox');
		html = '<table id="advDrawTable" overflow="scroll" width="100%" cellspacing="0" cellpadding="0"><tr><td bgcolor="#00639c" align="center" colspan="3" overflow:"auto"><font color="#FFFFFF" size="2">Selected Draws</font></td></tr>';
		j=0;
		for(i=0; i<drwIdInpArr.length;i++){
			if(drwIdInpArr[i].checked == true){
				colorVar+=1;				
				if (colorVar % 2 == 0) {
					rowcolor = "#E0E0D1";
				} else {
					rowcolor = "#FFFFFF";
				}
				html += '<tr>';
				html += '<td bgcolor="'+rowcolor+'" style="border-bottom:1px solid #639cce;border-left:1px solid #639cce;"><font color="#00639c" size="1">'+(j+1) + '</font></td><td width="45%" bgcolor="'+rowcolor+'" nowarp="nowrap" style="border-bottom:1px solid #639cce;border-left:1px solid #639cce;border-right:1px solid #639cce;"><font size="1" color="#00639c">&nbsp;'+ drwIdInpArr[i].value +'</font></td><td style="border-bottom:1px solid #639cce;border-right:1px solid #639cce;" bgcolor="'+rowcolor+'"><font size="1" color="#00639c">&nbsp;'+drwIdInpArr[i].title+'</font></td>';
				html += '</tr>';
				//alert(drwIdInpArr[i].id);
				eval('_'+curActive+'DrawIdArr[j]= drwIdInpArr[i].id.split(\'_\')[0]');
				j++;
			}
		}
		if(j==0){
			_id.n('conOrNonCon')[0].checked=true;
			html = '<table style="margin-top:5px"><tr><td colspan="3" class="selected_kenosix_numbers" style="border-radius:3px;text-align:center; height:70px; width:155px;background: none repeat scroll 0 0 #FFFFFF;border:1px solid #9cceff;"><font size="2">No Advance Draws Selected </font></td></tr>';
		}
		_id.i('selectedDrawList',html);
			//hideOverlay();
	}else{
		eval('_'+curActive+'DrawIdArr = new Array()');
		_id.i('selectedDrawList','<table style="margin-top:5px"><tr><td colspan="3" class="selected_kenosix_numbers" style="border-radius:3px;text-align:center; height:70px; width:155px;background: none repeat scroll 0 0 #FFFFFF;border:1px solid #9cceff;"><font size="2">Advanced Play Mode is Disabled! </font></td></tr>');
	}
	eval('_'+curActive+'CalAmt()');
	_id.o('drawSelectorDiv').style.visibility = 'hidden';
}

function ClearAllGame(tabId) {
	//var obj = _id.o(tabId);
	//var elms = obj.getElementsByTagName("input");
}
function disClick(obj) {
	if(window.event){
		if (event.button == 2) {
			obj.oncontextmenu = new Function("return false");
			return false;
		}
	}
}
function trim(str, chars) {
	return ltrim(rtrim(str, chars), chars);
}
function ltrim(str, chars) {
	chars = chars || "\\s";
	return str.replace(new RegExp("^[" + chars + "]+", "g"), "");
}
function rtrim(str, chars) {
	chars = chars || "\\s";
	return str.replace(new RegExp("[" + chars + "]+$", "g"), "");
}
function rposKeyPress(e) {
	var kC = (window.event) ? event.keyCode : e.which;  // MSIE or Firefox?
	var Esc = (window.event) ? 27 : e.DOM_VK_ESCAPE; // MSIE : Firefox  
	var Enter = 13; // MSIE : Firefox
	var qpKeyPress = 81;
	var checkPwtEnter=false;
	
	//alert(kC+"  "+Esc);
	if(document.activeElement.id=='pwtTickNum')
		checkPwtEnter=true

		
	if(checkPwtEnter && kC == Enter)
		if (allowBuy) {
			if(verifyPwt())
			allowBuy = false;
			else
			allowBuy = true;
		}
		
		
	if (kC == Esc) {
		if (_id.o("info5Div").style.display == "block") {
			hideDiv();
		}
	}
	if (kC == Enter && !checkPwtEnter) {//alert(curActive);
		//alert(_id.o('pwtResult').focused);
		if (allowBuy) {
			if(eval("buyTicket" + curActive + "('buy')"))
			allowBuy = false;
			else
			allowBuy = true;
		}
	}
	if (kC == qpKeyPress) {//alert(curActive.indexOf("lotto"));
		if (curActive.indexOf("lotto") != -1 || curActive == "keno") {
			_id.o("qpChkAll").checked = !(_id.o("qpChkAll").checked);
			eval(curActive + "ChkQP" + "('qpChkAll')");
			_id.o("tkts").value = "";
			//alert(_id.o('tkts')+"***"+_id.o('tkts').value);
		}
	}
	if (kC == 67) {  //Cancel Ticket
		cancelTable();
		if (cancelType == "LAST_SOLD_TICKET") {
			cancelLastTkt();
		} else {
			_id.n("cancelTickNum")[0].focus();
		}
	}
	if (kC == 82) {  //Reprint Ticket
		reprintTable();
		if (reprintType == "LAST_SOLD_TICKET") {
			reprintLastTkt();
		} else {
			_id.n("reprintTickNum")[0].focus();
		}
	}
	if (kC == 80) {   //Winning Ticket
		pwtTable();
		_id.o("pwtTickNum").focus();
	}
}
function rposKeyPressUp(e) {
	var kC = (window.event) ? event.keyCode : e.which;  // MSIE or Firefox?
	if (kC == 81) {
		if (curActive.indexOf("lotto") != -1) {
			_id.o("tkts").value = "";
		}
	}
}
function aftFrz() {
	var nxtDrwArr = eval("_" + curActive + "DrawTime");
	var nxtStatus = "ACTIVE=" + nxtDrwArr.split(",")[1];
	eval("_" + curActive + "UpData('" + eval("_" + curActive + "Win") + "','" + nxtDrwArr.substring(nxtDrwArr.indexOf(",") + 1, nxtDrwArr.length) + "','" + nxtStatus + "')");
	visitGameArr[curActive] = new gameObj(curActive);
	//alert(uniqueDrawTimeArr[0]);
	uniqueDrawTimeArr.shift();
	//alert(uniqueDrawTimeArr[0]);
	getActiveDrawOptionList(curActive);
	if (uniqueDrawTimeArr[0] == undefined) {
		clearTimeout(sCallCount);
		//fillNoDraws();
	} else {
		latestFreezeTime[curActive] = storedGameObj.latestDrawTimeO;
		fillGame(curActive, "autoCall");
	}
}
var sCounter;
function servTimeCounter() {
	//alert("sumit" + curSerTime);
	clearTimeout(sCounter);
	curSerTime = curSerTime + 1000;
	sCounter = setTimeout("servTimeCounter()", 999);
}
servTimeCounter();
var sUpdateData;

var sCallCount;
function serverCallCounter() {//alert(new Date(timeLeftForFrz));
	clearTimeout(sCallCount);
	//alert("timeLeftForFrz"+timeLeftForFrz);
	if ((timeLeftForFrz) < 0) {
//alert("Updating data");
		sUpdateData=setTimeout("fetchUpdatedData()", 999);
	} else {
		timeLeftForFrz = timeLeftForFrz - 1000;
		sCallCount = setTimeout("serverCallCounter()", 1000);
	}
}
serverCallCounter();
function getActiveDrawOptionList(game) {
	var drawTimeArr = eval("_" + game + "DrawTime");
	var nxtDrwArrLen = drawTimeArr.split(",").length;
	var html = new Array();
	for (var i = 0; i < nxtDrwArrLen; i++) {
		html.push("<option value=\"" + (i + 1) + "\">" + (i + 1) + "</option>");
	}
	return html.join("");
}
function displayErrMsg(msg) {
	//document.getElementById("parentApplet").style.display = "none";
	document.getElementById("parentApplet").style.visibility = "hidden";
	document.getElementById("pwtResult").style.display = "block";
	if (typeof msg != "undefined") {
		alert(msg);
		parent.frames[0].location.reload();
	}
}
var isPrintFail = false;
function cancelTktAuto(tktNum, totAmt, printStatus) {
	//alert('*****'+tktNum+'*************'+totAmt+'***********'+printStatus+'*********');
	if (printStatus == "SUCCESS") {
		_id.o("buy").disabled = false;
		allowBuy = true;
		document.getElementById("parentApplet").style.visibility = 'visible';
		return true;
	} else if(printStatus == "MISSMATCH") {
		_id.o("parentApplet").style.visibility = 'hidden';
		var _resp = winAjaxReq("cancelTicketAuto.action?autoCancel=true&ticketNo=" + tktNum+"&cancelType=dataError");
		if (_resp.result) {
			_id.o("buy").disabled = false;
			allowBuy = true;
			//alert(res);
			alert(i18nObj.prop('msg.last.tkt.cannot.purchased'));
			parent.frames[0].updateBalance(projectName + "/com/skilrock/lms/web/loginMgmt/updateAvlCreditAmt.action");
			return false;
		}
		isPrintFail = true;
		_id.o("parentApplet").style.visibility = 'hidden';
		
		/*
		_id.o("parentApplet").style.visibility = 'hidden';
		var repCount = reprintCountChild;
		var curTrxLoc = curTrx;
		var status = "";
		if (curTrxLoc == "BUY") {
			if (repCount < 1) {
				status = confirm("ALERT!!! Please Contact Back Office \n Press OK to cancel This Ticket");
				if (!status) {
					var _resp = winAjaxReq("cancelTicketAuto.action?autoCancel=true&ticketNo=" + tktNum+"&cancelType=dataError");
					if (_resp.result) {
						_id.o("buy").disabled = false;
						allowBuy = true;
						//alert(res);
						alert("Last Ticket Can not be Purchased");
						parent.frames[0].updateBalance(projectName + "/com/skilrock/lms/web/loginMgmt/updateAvlCreditAmt.action");
						return false;
					}
					isPrintFail = true;
					_id.o("parentApplet").style.visibility = 'hidden';
				} else {
					reprintCountChild++;
					//document.getElementById("parentApplet").style.visibility = 'visible';
					return true;
				}
			} else {
				var _resp = winAjaxReq("cancelTicketAuto.action?autoCancel=true&ticketNo=" + tktNum);
				if (_resp.result) {
					_id.o("buy").disabled = false;
					allowBuy = true;
					res = _resp.data;
					//alert(res);
					alert("Last Ticket Can not be Purchased");
					parent.frames[0].updateBalance(projectName + "/com/skilrock/lms/web/loginMgmt/updateAvlCreditAmt.action");
				}
				isPrintFail = true;
				_id.o("parentApplet").style.visibility = 'hidden';
				return false;
			}
		} else {
			if (curTrxLoc == "PWT") {
				if (repCount < 2) {
					if (totAmt > 0) {
						reprintCountChild++;
						if (repCount == 0) {
							alert("\tPRINTER NOT CONNECTED. \nCheck Your Printer and then Press OK to Reprint Ticket");
						} else {
							if (repCount == 1) {
								alert("\tPRINTER NOT CONNECTED. \nYour Winning Amount is: " + totAmt + "\nYour Ticket No is: " + tktNum);
							}
						}
						document.getElementById("parentApplet").style.visibility = 'visible';
						return true;
					}
				} else {
					isPrintFail = true;
					document.getElementById("parentApplet").style.visibility = 'visible';
				}
			} else {
				if (curTrxLoc == "RPT") {
					if (repCount < 2) {
						reprintCountChild++;
						alert("\tPRINTER NOT CONNECTED\nCheck Your Printer and then Press OK to Reprint Ticket");
						document.getElementById("parentApplet").style.visibility = 'visible';
						return true;
					} else {
						isPrintFail = true;
						_id.o("parentApplet").style.visibility = 'hidden';
					}
				} else {
					if (curTrxLoc == "CANCEL") {
						alert("PRINTER NOT CONNECTED.\n\nTicket Has Been Cancelled.");
					}
				}
			}
		}
	*/}else{

		_id.o("parentApplet").style.visibility = 'hidden';
		var repCount = reprintCountChild;
		var curTrxLoc = curTrx;
		var status = "";
		if (curTrxLoc == "BUY") {
			if (repCount < 2) {
				status = confirm("\t     PRINTER NOT CONNECTED\nCheck Your Printer and then Press OK to Reprint or Cancel to Cancel Ticket");
				if (!status) {
					var _resp = winAjaxReq("cancelTicketAuto.action?autoCancel=true&ticketNo=" + tktNum);
					if (_resp.result) {
						_id.o("buy").disabled = false;
						allowBuy = true;
						//alert(res);
						alert(i18nObj.prop('msg.last.tkt.cannot.purchased'));
						parent.frames[0].updateBalance(projectName + "/com/skilrock/lms/web/loginMgmt/updateAvlCreditAmt.action");
						return false;
					}
					isPrintFail = true;
					_id.o("parentApplet").style.visibility = 'hidden';
				} else {
					reprintCountChild++;
					//document.getElementById("parentApplet").style.visibility = 'visible';
					return true;
				}
			} else {
				var _resp = winAjaxReq("cancelTicketAuto.action?autoCancel=true&ticketNo=" + tktNum);
				if (_resp.result) {
					_id.o("buy").disabled = false;
					allowBuy = true;
					res = _resp.data;
					//alert(res);
					alert(i18nObj.prop('msg.last.tkt.cannot.purchased'));
					parent.frames[0].updateBalance(projectName + "/com/skilrock/lms/web/loginMgmt/updateAvlCreditAmt.action");
				}
				isPrintFail = true;
				_id.o("parentApplet").style.visibility = 'hidden';
				return false;
			}
		} else {
			if (curTrxLoc == "PWT") {
				if (repCount < 2) {
					if (totAmt > 0) {
						reprintCountChild++;
						if (repCount == 0) {
							alert("\t"+i18nObj.prop('msg.printer.not.connected')+" \n"+i18nObj.prop('msg.check.your.printer.and.press.ok.to.reprint.tkt'));
						} else {
							if (repCount == 1) {
								alert("\t"+i18nObj.prop('msg.printer.not.connected')+" \n"+i18nObj.prop('msg.your.winning.amt') + totAmt + "\n"+i18nObj.prop('msg.your.tkt.no') + tktNum);
							}
						}
						document.getElementById("parentApplet").style.visibility = 'visible';
						return true;
					}
				} else {
					isPrintFail = true;
					document.getElementById("parentApplet").style.visibility = 'visible';
				}
			} else {
				if (curTrxLoc == "RPT") {
					if (repCount < 2) {
						reprintCountChild++;
						alert("\t"+i18nObj.prop('msg.printer.not.connected')+" \n"+i18nObj.prop('msg.check.your.printer.and.press.ok.to.reprint.tkt'));
						document.getElementById("parentApplet").style.visibility = 'visible';
						return true;
					} else {
						isPrintFail = true;
						_id.o("parentApplet").style.visibility = 'hidden';
					}
				} else {
					if (curTrxLoc == "CANCEL") {
						alert(i18nObj.prop('msg.printer.not.connected')+"\n\n"+i18nObj.prop('msg.tkt.hasbeen.cancelled'));
					}
				}
			}
		}
	}
}


// This method is being used for 'UNSIGNED APPLET'
function cancelTktAuto2(tktNum, totAmt, printStatus) {
	//alert(printStatus);
	if (printStatus == "SUCCESS") {
		_id.o("buy").disabled = false;
		allowBuy = true;
		document.getElementById("parentApplet").style.visibility = 'visible';
		return true;
	} else {
		_id.o("parentApplet").style.visibility = 'hidden';
		var repCount = reprintCountChild;
		var curTrxLoc = curTrx;
		var status = "";
		if (curTrxLoc == "BUY") {
			if (repCount < 2) {
				status = confirm("\t     "+i18nObj.prop('msg.printer.not.connected')+"\n"+i18nObj.prop('msg.check.your.printer.and.press.ok.to.reprint.or.cancel.tkt'));
				if (!status) {
					var _resp = winAjaxReq("cancelTicketAuto.action?autoCancel=true&ticketNo=" + tktNum);
					if (_resp.result) {
						_id.o("buy").disabled = false;
						allowBuy = true;
						//alert(res);
						alert(i18nObj.prop('msg.last.tkt.cannot.purchased'));
						parent.frames[0].updateBalance(projectName + "/com/skilrock/lms/web/loginMgmt/updateAvlCreditAmt.action");
					}
					isPrintFail = true;
					_id.o("parentApplet").style.visibility = 'hidden';
				} else {
					reprintCountChild++;
					document.getElementById("parentApplet").style.visibility = 'visible';
					document.applets[0].init();
				}
			} else {
				var _resp = winAjaxReq("cancelTicketAuto.action?autoCancel=true&ticketNo=" + tktNum);
				if (_resp.result) {
					_id.o("buy").disabled = false;
					allowBuy = true;
					res = _resp.data;
					//alert(res);
					alert(i18nObj.prop('msg.last.tkt.cannot.purchased'));
					parent.frames[0].updateBalance(projectName + "/com/skilrock/lms/web/loginMgmt/updateAvlCreditAmt.action");
				}
				isPrintFail = true;
				_id.o("parentApplet").style.visibility = 'hidden';
			}
		} else {
			if (curTrxLoc == "PWT") {
				if (repCount < 2) {
					if (totAmt > 0) {
						reprintCountChild++;
						if (repCount == 0) {
							alert("\t"+i18nObj.prop('msg.printer.not.connected')+" \n"+i18nObj.prop('msg.check.your.printer.and.press.ok.to.reprint.tkt'));
						} else {
							if (repCount == 1) {
								alert("\t"+i18nObj.prop('msg.printer.not.connected')+" \n"+i18nObj.prop('msg.your.winning.amt') + totAmt + "\n"+i18nObj.prop('msg.your.tkt.no') + tktNum);
							}
						}
						_id.o("parentApplet").style.visibility = 'hidden';
						document.applets[0].init();
					} else {
						document.getElementById("parentApplet").style.visibility = 'visible';
					}
				} else {
					isPrintFail = true;
					_id.o("parentApplet").style.visibility = 'hidden';
				}
			} else {
				if (curTrxLoc == "RPT") {
					if (repCount < 2) {
						reprintCountChild++;
						alert("\t"+i18nObj.prop('msg.printer.not.connected')+" \n"+i18nObj.prop('msg.check.your.printer.and.press.ok.to.reprint.tkt'));
						document.getElementById("parentApplet").style.visibility = 'visible';
						document.applets[0].init();
					} else {
						isPrintFail = true;
						_id.o("parentApplet").style.visibility = 'hidden';
					}
				} else {
					if (curTrxLoc == "CANCEL") {
						alert(i18nObj.prop('msg.printer.not.connected')+"\n\n"+i18nObj.prop('msg.tkt.hasbeen.cancelled'));
					}
				}
			}
		}
	}
}
function setAppData(buyData) {
	_id.non("pwtResult");
	_id.o("parentApplet").style.visibility = 'hidden';
	_id.o("regDiv").innerHTML = "";
	_id.o("regButton").innerHTML = "";
	isPrintFail = false;
	adjustAppletSize(buyData.substring(buyData.indexOf("ctr=") + 4, buyData.length));
	//_id.non("pwtResult");
	if (curTrx == "PWT") {
		if (isPayAction) {
			_id.o("parentApplet").style.visibility = 'hidden';
			var amt = calAmt(buyData);
			if (amt > 0) {
				var isPay = confirm(i18nObj.prop('msg.winning.amt.is') + amt + " \n"+i18nObj.prop('msg.doU.want.to.claim'));
				var url = projectName + "/com/skilrock/lms/web/drawGames/pwtMgmt/payPwtTicket.action";
				if (isPay) {
					pwtAjaxReq(url, false);
				}
				isPrintFail = true;
				_id.o("parentApplet").style.visibility = 'hidden';
			} else {
				document.applets[0].showStatus(buyData);
				document.getElementById("parentApplet").style.visibility = 'visible';
			}
		} else {
			document.applets[0].showStatus(buyData);
			document.getElementById("parentApplet").style.visibility = 'visible';
		}
	}else if (curTrx == "TPPWT") {
		var ticketNo = _id.o("ticketNumber").value ;
		if (isPayAction) {
			_id.o("parentApplet").style.visibility = 'hidden';
			var amt = calAmt(buyData);
			if (amt > 0) {
				var isPay = confirm(i18nObj.prop('msg.winning.amt.is') + amt + " \n"+i18nObj.prop('msg.doU.want.to.claim')+"?");
				if(isPay)
				{
					if(ticketNo.length == 15){
					//var verCon = prompt("Enter Verification Code","");
					document.getElementById("vertable").style.display = 'block';
					return;
					}
					else{
					var url = projectName + "/com/skilrock/lms/web/drawGames/pwtMgmt/payTpTicketProcessRet.action";
					tpPwtAjaxReq(url, false);
					_id.o("ticketNumber").value = '';
					}
					
				}
				isPrintFail = true;
				_id.o("parentApplet").style.visibility = 'hidden';
			} else {
				document.applets[0].showStatus(buyData);
				document.getElementById("parentApplet").style.visibility = 'visible';
			}
		} else {
			document.applets[0].showStatus(buyData);
			document.getElementById("parentApplet").style.visibility = 'visible';
		}
	} else {
		document.applets[0].showStatus(buyData);
		document.getElementById("parentApplet").style.visibility = 'visible';
	}
	document.applets[0].repaint();
}
function adjustAppletSize(height) {
	document.applets[0].height = height;
}
function showRegButton() {
	var html = "<span class=\"ttlpay\"></span><span class=\"amt\"><input type=\"button\" value=\"Register\" onclick=\"regPlr()\" /> </span>";
	_id.i("regButton", html);
}
function calAmt(buyData) {
	var amt = 0;
	var amtArr = buyData.substring(buyData.indexOf("totMainTktAmt=") + 14, buyData.indexOf("|ctr=")).replace(/NA/g, "0").split(",");
	for (var i = 0; i < amtArr.length; i++) {
		if (amtArr[i] != "") {
			amt = amt + amtArr[i];
		}
	}
	return amt;
}
function checkNumber(str, startNum, endNum) {
	var strArr = str.split(",");
	var isCheck = false;
	for (var i = 0; i < strArr.length; i++) {
		if(strArr[i].length<2){
			isCheck = true;
		}
		if (strArr[i] < startNum || strArr[i] > endNum) {
			isCheck = true;
		}
	}
	return isCheck;
}
function checkDuplicate(str) {
	var strArr = str.split(",");
	var isCheck = false;
	for (var i = 0; i < strArr.length; i++) {
		for (var j = i + 1; j < strArr.length; j++) {
			if (strArr[i] == strArr[j]) {
				isCheck = true;
			}
		}
	}
	return isCheck;
}
function tktPriceRound(totalPrice){
	if(totalPrice<=10.0){
		return parseFloat(parseInt(totalPrice*2+0.5,10)/2,10);
	}else{
		return parseFloat(parseInt(totalPrice));
	}
}

//var hex_table = "00000000 77073096 EE0E612C 990951BA 076DC419 706AF48F E963A535 9E6495A3 0EDB8832 79DCB8A4 E0D5E91E 97D2D988 09B64C2B 7EB17CBD E7B82D07 90BF1D91 1DB71064 6AB020F2 F3B97148 84BE41DE 1ADAD47D 6DDDE4EB F4D4B551 83D385C7 136C9856 646BA8C0 FD62F97A 8A65C9EC 14015C4F 63066CD9 FA0F3D63 8D080DF5 3B6E20C8 4C69105E D56041E4 A2677172 3C03E4D1 4B04D447 D20D85FD A50AB56B 35B5A8FA 42B2986C DBBBC9D6 ACBCF940 32D86CE3 45DF5C75 DCD60DCF ABD13D59 26D930AC 51DE003A C8D75180 BFD06116 21B4F4B5 56B3C423 CFBA9599 B8BDA50F 2802B89E 5F058808 C60CD9B2 B10BE924 2F6F7C87 58684C11 C1611DAB B6662D3D 76DC4190 01DB7106 98D220BC EFD5102A 71B18589 06B6B51F 9FBFE4A5 E8B8D433 7807C9A2 0F00F934 9609A88E E10E9818 7F6A0DBB 086D3D2D 91646C97 E6635C01 6B6B51F4 1C6C6162 856530D8 F262004E 6C0695ED 1B01A57B 8208F4C1 F50FC457 65B0D9C6 12B7E950 8BBEB8EA FCB9887C 62DD1DDF 15DA2D49 8CD37CF3 FBD44C65 4DB26158 3AB551CE A3BC0074 D4BB30E2 4ADFA541 3DD895D7 A4D1C46D D3D6F4FB 4369E96A 346ED9FC AD678846 DA60B8D0 44042D73 33031DE5 AA0A4C5F DD0D7CC9 5005713C 270241AA BE0B1010 C90C2086 5768B525 206F85B3 B966D409 CE61E49F 5EDEF90E 29D9C998 B0D09822 C7D7A8B4 59B33D17 2EB40D81 B7BD5C3B C0BA6CAD EDB88320 9ABFB3B6 03B6E20C 74B1D29A EAD54739 9DD277AF 04DB2615 73DC1683 E3630B12 94643B84 0D6D6A3E 7A6A5AA8 E40ECF0B 9309FF9D 0A00AE27 7D079EB1 F00F9344 8708A3D2 1E01F268 6906C2FE F762575D 806567CB 196C3671 6E6B06E7 FED41B76 89D32BE0 10DA7A5A 67DD4ACC F9B9DF6F 8EBEEFF9 17B7BE43 60B08ED5 D6D6A3E8 A1D1937E 38D8C2C4 4FDFF252 D1BB67F1 A6BC5767 3FB506DD 48B2364B D80D2BDA AF0A1B4C 36034AF6 41047A60 DF60EFC3 A867DF55 316E8EEF 4669BE79 CB61B38C BC66831A 256FD2A0 5268E236 CC0C7795 BB0B4703 220216B9 5505262F C5BA3BBE B2BD0B28 2BB45A92 5CB36A04 C2D7FFA7 B5D0CF31 2CD99E8B 5BDEAE1D 9B64C2B0 EC63F226 756AA39C 026D930A 9C0906A9 EB0E363F 72076785 05005713 95BF4A82 E2B87A14 7BB12BAE 0CB61B38 92D28E9B E5D5BE0D 7CDCEFB7 0BDBDF21 86D3D2D4 F1D4E242 68DDB3F8 1FDA836E 81BE16CD F6B9265B 6FB077E1 18B74777 88085AE6 FF0F6A70 66063BCA 11010B5C 8F659EFF F862AE69 616BFFD3 166CCF45 A00AE278 D70DD2EE 4E048354 3903B3C2 A7672661 D06016F7 4969474D 3E6E77DB AED16A4A D9D65ADC 40DF0B66 37D83BF0 A9BCAE53 DEBB9EC5 47B2CF7F 30B5FFE9 BDBDF21C CABAC28A 53B39330 24B4A3A6 BAD03605 CDD70693 54DE5729 23D967BF B3667A2E C4614AB8 5D681B02 2A6F2B94 B40BBE37 C30C8EA1 5A05DF1B 2D02EF8D";
var dec_table = [0,1996959894,3993919788,2567524794,124634137,1886057615,3915621685,2657392035,249268274,2044508324,3772115230,2547177864,162941995,2125561021,3887607047,2428444049,498536548,1789927666,4089016648,2227061214,450548861,1843258603,4107580753,2211677639,325883990,1684777152,4251122042,2321926636,335633487,1661365465,4195302755,2366115317,997073096,1281953886,3579855332,2724688242,1006888145,1258607687,3524101629,2768942443,901097722,1119000684,3686517206,2898065728,853044451,1172266101,3705015759,2882616665,651767980,1373503546,3369554304,3218104598,565507253,1454621731,3485111705,3099436303,671266974,1594198024,3322730930,2970347812,795835527,1483230225,3244367275,3060149565,1994146192,31158534,2563907772,4023717930,1907459465,112637215,2680153253,3904427059,2013776290,251722036,2517215374,3775830040,2137656763,141376813,2439277719,3865271297,1802195444,476864866,2238001368,4066508878,1812370925,453092731,2181625025,4111451223,1706088902,314042704,2344532202,4240017532,1658658271,366619977,2362670323,4224994405,1303535960,984961486,2747007092,3569037538,1256170817,1037604311,2765210733,3554079995,1131014506,879679996,2909243462,3663771856,1141124467,855842277,2852801631,3708648649,1342533948,654459306,3188396048,3373015174,1466479909,544179635,3110523913,3462522015,1591671054,702138776,2966460450,3352799412,1504918807,783551873,3082640443,3233442989,3988292384,2596254646,62317068,1957810842,3939845945,2647816111,81470997,1943803523,3814918930,2489596804,225274430,2053790376,3826175755,2466906013,167816743,2097651377,4027552580,2265490386,503444072,1762050814,4150417245,2154129355,426522225,1852507879,4275313526,2312317920,282753626,1742555852,4189708143,2394877945,397917763,1622183637,3604390888,2714866558,953729732,1340076626,3518719985,2797360999,1068828381,1219638859,3624741850,2936675148,906185462,1090812512,3747672003,2825379669,829329135,1181335161,3412177804,3160834842,628085408,1382605366,3423369109,3138078467,570562233,1426400815,3317316542,2998733608,733239954,1555261956,3268935591,3050360625,752459403,1541320221,2607071920,3965973030,1969922972,40735498,2617837225,3943577151,1913087877,83908371,2512341634,3803740692,2075208622,213261112,2463272603,3855990285,2094854071,198958881,2262029012,4057260610,1759359992,534414190,2176718541,4139329115,1873836001,414664567,2282248934,4279200368,1711684554,285281116,2405801727,4167216745,1634467795,376229701,2685067896,3608007406,1308918612,956543938,2808555105,3495958263,1231636301,1047427035,2932959818,3654703836,1088359270,936918000,2847714899,3736837829,1202900863,817233897,3183342108,3401237130,1404277552,615818150,3134207493,3453421203,1423857449,601450431,3009837614,3294710456,1567103746,711928724,3020668471,3272380065,1510334235,755167117];
//dec_table = hex_table.split(' ').map(function(s){return parseInt(s,16) });
//ADDED BY MANDEEP FOR CALCULATING THE CRC
function getCheckSum32 (str) 
{
		var crc = crc ^ (-1);
		for(var i = 0, iTop = str.length; i < iTop; i += 1) {
		     crc = ( crc >>> 8 ) ^ dec_table[( crc ^ str.charCodeAt( i ) ) & 0xFF];
		 }
		//alert("CRC FOR " + str + " is "+ ((crc ^ (-1)) >>> 0));
		return (crc ^ (-1)) >>> 0;
	}

	function getStringForCRC(str){
		var startIndex = str.indexOf('pickedNumbers');
		var endIndex = str.indexOf('&' , str.indexOf('pickedNumbers'));
		var crcString = str.substring(startIndex+'pickedNumbers'.length+1 , endIndex);
		startIndex = str.indexOf('noPicked');
		endIndex = str.indexOf('&' , str.indexOf('noPicked'));
		crcString = crcString + str.substring(startIndex+'noPicked'.length+1 , endIndex);
		startIndex = str.indexOf('playType');
		endIndex = str.indexOf('&' , str.indexOf('playType'));
		crcString = crcString + str.substring(startIndex+'playType'.length+1 , endIndex);
		//alert(temp);
		return crcString;
	}

parent.frames[0].Version['$Source: /rep/LMS_Mgmt/WebRoot/com/skilrock/lms/web/drawGames/playMgmt/js/common.js,v $'] = '$Id: common.js,v 1.21 2016/10/31 09:46:17 neeraj Exp $';