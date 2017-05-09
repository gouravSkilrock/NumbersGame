var i18nObj = parent.frames[0].i18Obj;

var drawDateTime="";
var freezeDateTime="";
var d="";
var d2="";
var gameName="";
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
var isZeroAllowed = false;
var isMultiResult = false;
window.addEventListener("keydown", checkKeyPressed, false);
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

/*
 * For Automatic Result Submission Privilege 
 */

function automaticResultSubmision(){
	$("#error").html("");
	var dispMessage="";
	//var gameId=$("#gameId").val();
	gameName=$("#gameName").val();
	var isDrawChecked = isAnyCheckedRow();
	if (!isDrawChecked) {
		return false;
	}
	var drawsArr = _id.n("drawIds");
	for (var i = 0, l = drawsArr.length; i < l; i++) {
		if (drawsArr[i].checked) {
			drawId=drawsArr[i].value;
			drawDateTime = _id.o(drawsArr[i].value + "drawDateTime");
			drawDateTime=drawDateTime.innerHTML.split(" ");
			d = new Date(drawDateTime);
			freezeDateTime = _id.o(drawsArr[i].value + "freezeDateTime");
			freezeDateTime=freezeDateTime.innerHTML.split(" ");
		    d2 = new Date(freezeDateTime);
        }
	}
//	request=confirm("Draw DateTime: "+d.toString().substr(3,22).substr(0,7)+','+d.toString().substr(3,22).substr(8)+"\nDraw FreezeTime: "+d2.toString().substr(3,22).substr(0,7)+','+d2.toString().substr(3,22).substr(8)+" ");
		dispMessage+="<div class='row-d'>";
		dispMessage+="<div class='dt-tm-d'>Draw DateTime:</div><div class='dt-tm-dd'> "+d.toString().substr(3,22).substr(0,7)+','+d.toString().substr(3,22).substr(8)+" </div>";
		dispMessage+="<div class='dt-tm-d'>Draw FreezeTime:</div><div class='dt-tm-dd'>"+d2.toString().substr(3,22).substr(0,7)+','+d2.toString().substr(3,22).substr(8)+"</div>";
		dispMessage+="</div>";
		$("#error").append(dispMessage);
	    $("#error-popup").css("display","block");
		$(".div-cont").css('display','block');

}

/*
 * For checking draw selected 
 */
function isAnyCheckedRow(){
	var count = 0;
	var ret = true;
	for (var i = 0; i < _id.n("drawIds").length; i += 1) {
		var e = _id.n("drawIds")[i];
		if (e.checked == true) {
			count += 1;
		}
	}
	if (count == 0) {
		$("#error").html("Please choose atleast one draw !!!");
		$("#error-popup").css('display','block');
		$('#error-popup').delay(2000).fadeOut('slow');
		$(".div-cont").css('display','none');
		ret = false;
	}
	return ret;
}

/*
 * After Clicking on Yes Confirmation  
 */

function successConfirmation(){
	closeSuccessConfirmation();
	url='getResponseFromDGW.action?gameName='+gameName+'&drawId='+drawId+'&drawDateTime='+drawDateTime+'&freezeDateTime='+freezeDateTime+'';
	var _resp = _ajaxCall(url);
	if(_resp == null && _resp == undefined){
		$("#error").html("Something went wrong !!!");
		$("#error-popup").css('display','block');	
		//$('#error-popup').delay(2000).fadeOut('slow');
		return false;
	}
	var finalRespData = $.parseJSON(_resp.data);
	//console.log($.parseJSON(_resp.data));
	if(finalRespData.isSuccess){
		/*
		alert(finalRespData.responseMessage);*/
		$("#searchSubmit").click();
		$("#error").html(finalRespData.responseMessage);
		$("#error-popup").css('display','block');
		$('#error-popup').delay(2000).fadeOut('slow');
	}
	else{
		
		/*alert(finalRespData.responseMessage);*/
		$("#searchSubmit").click();
		$("#error").html(finalRespData.responseMessage);
		$("#error-popup").css('display','block');	
		$('#error-popup').delay(2000).fadeOut('slow');
		return false;
	}
}

/*
 * For Automatic Result Submission Privilege 
 * 
 * confirmation box closing on Escape key 
 */

function checkKeyPressed(e){
	if(e.which == 27){
		$("#error").html("");
		$("#error-popup").css('display','none');
		$(".div-cont").css('display','none');
		return false;
	}
}

/*
 * For Automatic Result Submission Privilege
 * clear confirmation box 
 */

function closeSuccessConfirmation(){
	$("#error").html("");
	$("#error-popup").css('display','none');
	$(".div-cont").css('display','none');
}
/*
 * For Automatic Result Submission Privilege
 * clicking on NO button of confirmation box 
 */

function failedConfirmation(){
	$("#error").html("");
	$("#error-popup").css('display','none');
	$(".div-cont").css('display','none');
	return false;
}

function checkedDraw(url, statusType, drawType) {
	var cndtn = _id.o("privName").value;
	var isDrawChecked = isAnyChecked();
	if (!isDrawChecked) {
		return false;
	}
	var drawsStatus = _id.o("status").value;
	var drawsArr = _id.n("drawIds");
	var tblObj = _id.o("drawResTbl");
	var gameNo = _id.o("game_no");
	var bodyStr = "";
	var drawSlctd = "";
	if(_id.o("eventIdCap")!=null){
	var eventId = (_id.o("eventIdCap").innerHTML).trim();
	}
	var drawName = (_id.o("drawNameCap").innerHTML).trim();
	if(eventId==null || eventId==""){
		var tblStr = "<table width=\"800\" cellspacing=\"0\" cellpadding=\"5\" border=\"1\" bordercolor=\"#CCCCCC\" ><tr><th colspan=\"8\" align=\"center\"><input type=\"hidden\" name=\"gameNo\" value=\"" + gameNo.value + "\">"+i18nObj.prop('label.draw.list')+"</th></tr><tr><th width=\"20%\" scope=\"col\" align=\"center\">"+i18nObj.prop('label.draw.id')+"</th><th width=\"20%\" scope=\"col\" align=\"center\">"+i18nObj.prop('label.draw.time')+"</th><th width=\"20%\" scope=\"col\" align=\"center\">"+i18nObj.prop('label.draw.freeze.time')+"</th><th width=\"20%\" scope=\"col\" align=\"center\">"+i18nObj.prop('label.draw.status')+"</th><th width=\"20%\" scope=\"col\" align=\"center\">"+drawName;
	}
	else{
		var tblStr = "<table width=\"800\" cellspacing=\"0\" cellpadding=\"5\" border=\"1\" bordercolor=\"#CCCCCC\" ><tr><th colspan=\"8\" align=\"center\"><input type=\"hidden\" name=\"gameNo\" value=\"" + gameNo.value + "\">"+i18nObj.prop('label.draw.list')+"</th></tr><tr><th width=\"20%\" scope=\"col\" align=\"center\">"+i18nObj.prop('label.draw.id')+"</th><th width=\"20%\" scope=\"col\" align=\"center\">"+i18nObj.prop('label.draw.time')+"</th><th width=\"20%\" scope=\"col\" align=\"center\">"+i18nObj.prop('label.draw.freeze.time')+"</th><th width=\"20%\" scope=\"col\" align=\"center\">"+i18nObj.prop('label.draw.status')+"</th><th width=\"20%\" scope=\"col\" align=\"center\">"+drawName+"</th><th width=\"20%\" scope=\"col\" align=\"center\">"+eventId;
	}
	if (drawType == "ppr" || drawType == "clmhld" ) {
		
		// alert(eventId + '' +drawName );
		tblStr += "</th><th width=\"20%\" scope=\"col\" align=\"center\">"+i18nObj.prop('label.result')+"</th><th width=\"20%\" scope=\"col\" align=\"center\">"+i18nObj.prop('label.ppr')+"</th>";
	}
	if (drawType == "c") {
		tblStr += "<th width=\"25%\" scope=\"col\" align=\"center\">"+i18nObj.prop('label.no.of.tkt.sold')+"</th>";
	}
	tblStr += "</tr>";
	var count = 0;
	for (var i = 0, l = drawsArr.length; i < l; i++) {
		if (drawsArr[i].checked) {
			drawSlctd=drawsArr[i].value;
		}
	}
	if (drawType == "c") {
		var url = 'fetchCancelDrawData.action?gameNo='+gameNo.value+'&drawIdString='+drawSlctd;
		var _resp = _ajaxCall(url);
		var ticketData = _resp.data;
	}
	for (var i = 0, l = drawsArr.length; i < l; i++) {
		if (drawsArr[i].checked) {
						// add rows of table that are checked
			if (drawType == "c") {
				tblStr = tblStr + "<tr>" + _id.o(drawsArr[i].value).innerHTML + ticketData + "</tr>";
			}
			else{
				tblStr = tblStr + "<tr>" + _id.o(drawsArr[i].value).innerHTML + "</tr>";
			}
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
			tblStr = tblStr.replace("radio", "hidden");
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
		bodyStr = "<h3>"+i18nObj.prop('label.draw.to.change.draw.time')+"</h3><form name=\"postponeForm\" method=\"post\" action=\"postponeDraw.action\" onsubmit=\"return _postponeValidate('" + count + "')\">";
		if(document.getElementById("forToken") != null)
			bodyStr = bodyStr + document.getElementById("forToken").innerHTML;
		bodyStr = bodyStr + "<div id=\"postpone_err\"></div>" + tblStr;
		bodyStr = bodyStr + "<br><br><br><table cellspacing=\"0\" cellpadding=\"5\" border=\"0\" bordercolor=\"#CCCCCC\" >" + "<tr><td>"+i18nObj.prop('label.change.draw.time.by')+":&nbsp;</td><td><input type=\"text\" name=\"postponeTime\" id=\"postponeTime\" maxlength=\"6\" onkeypress=\"return onlyNumeric(event)\"/>" + "&nbsp;"+i18nObj.prop('label.mins')+"</td>";
		/*if (count < 2) {
			var toDate = day + "/" + month + "/" + year;
					// set default postpone date to checked date
			_id.o("to_date").value = day + "/" + month + "/" + year;
			_id.o("to_hour").options[hour].selected = true;
			_id.o("to_min").options[min].selected = true;
			_id.o("to_sec").options[sec].selected = true;
			var dateStr = _id.o("dateTbl").innerHTML;
			dateStr = dateStr.replace("document.getElementById('from_date').value", "'" + toDate + "'");
			// alert(dateStr);
			bodyStr = bodyStr + "<td>OR &nbsp;New Draw Date</td><td><table>" + dateStr + "</table></td>";
		}*/
		bodyStr = bodyStr + "<br><input type=\"checkbox\" name=\"cBox\" onclick=\"if(this.form['cBox'].checked){this.form['sub'].disabled=false;}else{this.form['sub'].disabled=true;}\">"+i18nObj.prop('label.agree.that.all.ret.logout')+"<br />";
		bodyStr = bodyStr + "</tr><tr><td colspan=\"2\" align=\"right\">" + "<input type=\"submit\" name=\"sub\" value=\"Change Draw Time\" class=\"button\" disabled />" + "</td></tr><tr><td colspan=\"3\" align=\"right\"><a href=\""+(cndtn == 'DRAW_MGMT'?'drawMenu':'drawPerformMenu')+".action?request_locale="+document.getElementById("mylocale").value+"\">"+i18nObj.prop('label.bak.to.srch.res')+"</a> </td></tr></table></form>";
		bodyStr = bodyStr + "<div id=\"loadWait\"></div>"
	} else {
		if (drawType == "f") {
			bodyStr = "<h3>"+i18nObj.prop('label.draw.to.freeze')+"</h3><form action=\"freezeDraw.action\">";
			if(document.getElementById("forToken") != null)
				bodyStr = bodyStr + document.getElementById("forToken").innerHTML;
			bodyStr = bodyStr + tblStr;
			bodyStr = bodyStr + "<br><input type=\"checkbox\" name=\"cBox\" onclick=\"if(this.form['cBox'].checked){this.form['sub'].disabled=false;}else{this.form['sub'].disabled=true;}\">"+i18nObj.prop('label.agree.that.all.ret.logout')+"<br />";
			bodyStr = bodyStr + "<input type=\"submit\" name=\"sub\" value=\""+i18nObj.prop('label.freeze.draw')+"\" class=\"button\" disabled><tr><td colspan=\"3\" align=\"right\"><a href=\""+(cndtn == 'DRAW_MGMT'?'drawMenu':'drawPerformMenu')+".action?request_locale="+document.getElementById("mylocale").value+"\">"+i18nObj.prop('label.bak.to.srch.res')+"</a> </td></tr></form>";
		} else {
			if (drawType == "c") {
				bodyStr = "<h3>"+i18nObj.prop('label.draw.to.cancel')+"</h3><form action=\"cancelDraw.action\">";
				if(document.getElementById("forToken") != null)
					bodyStr = bodyStr + document.getElementById("forToken").innerHTML;
				bodyStr = bodyStr + tblStr;
				bodyStr = bodyStr + "<br><h4>"+i18nObj.prop('label.choose.action.perform')+":</h4><input type=\"radio\" name=\"action\" value=\"noAction\" checked/>"+i18nObj.prop('label.cancel.darw.without.cancel.tkt')+"<br /><input type=\"radio\" name=\"action\" value=\"cancelAll\" />"+i18nObj.prop('label.cancel.all.tkt')+" <br />";
				bodyStr = bodyStr + "<br><input type=\"checkbox\" name=\"cBox\" onclick=\"if(this.form['cBox'].checked){this.form['sub'].disabled=false;}else{this.form['sub'].disabled=true;}\">"+i18nObj.prop('label.agree.that.all.ret.logout')+"<br />";
				bodyStr = bodyStr + "<input type=\"submit\" name=\"sub\" value=\""+i18nObj.prop('label.cancel.draws')+"\" class=\"button\" disabled/><tr><td colspan=\"3\" align=\"right\"><a href=\""+(cndtn == 'DRAW_MGMT'?'drawMenu':'drawPerformMenu')+".action?request_locale="+document.getElementById("mylocale").value+"\">"+i18nObj.prop('label.bak.to.srch.res')+"</a> </td></tr></form>";
				//bodyStr = bodyStr + "<br><br><input type=\"checkbox\" name=\"action\" value=\"cancelAll\" /> Cancel all Tickets<br /></form>";
			} else {
			if (drawType == "change") {
				bodyStr = "<h3>"+i18nObj.prop('label.draw.to.change.freeze.time')+"</h3><form action=\"changeFreezeTime.action\" name=\"changeFreezeForm\" onsubmit=\"return validateFreezeTime();\">";
				if(document.getElementById("forToken") != null)
					bodyStr = bodyStr + document.getElementById("forToken").innerHTML;
				bodyStr = bodyStr + "<div id=\"frreze_err\"></div>" + tblStr;
				bodyStr = bodyStr + "<br><br><br><table cellspacing=\"0\" cellpadding=\"3\" border=\"1\" bordercolor=\"#CCCCCC\" >" + "<tr><td colspan=\"2\"><div id=\"freezeMsg\" style=\"color: red; text-align: center;\"></div></td></tr><tr><td>"+i18nObj.prop('label.change.freeze.time')+":&nbsp;</td>" + "<td><input type=\"text\" name=\"freezeTime\" id=\"freezeTime\" maxlength=\"6\" onkeypress=\"return onlyNumeric(event)\"/>&nbsp;"+i18nObj.prop('label.sec')+"</td>";
				bodyStr = bodyStr + "<br><input type=\"checkbox\" name=\"cBox\" onclick=\"if(this.form['cBox'].checked){this.form['sub'].disabled=false;}else{this.form['sub'].disabled=true;}\">"+i18nObj.prop('label.agree.that.all.ret.logout')+"<br />";
				bodyStr = bodyStr + "</tr><tr><td colspan=\"2\" align=\"right\">" + "<input type=\"submit\" name=\"sub\" value=\""+i18nObj.prop('Change Freeze Time')+"\" class=\"button\" disabled/></td></tr><tr><td colspan=\"3\" align=\"right\"><a href=\""+(cndtn == 'DRAW_MGMT'?'drawMenu':'drawPerformMenu')+".action?request_locale="+document.getElementById("mylocale").value+"\">"+i18nObj.prop('label.bak.to.srch.res')+"</a> </td></tr></table>";	
			} else {
				if (drawType == "h") {
					bodyStr = "<h3>"+i18nObj.prop('label.draw.to.hold')+"</h3><form action=\"holdDraw.action\">";
					if(document.getElementById("forToken") != null)
						bodyStr = bodyStr + document.getElementById("forToken").innerHTML;
					bodyStr = bodyStr + tblStr;
					bodyStr = bodyStr + "<input type=\"submit\" value=\""+i18nObj.prop('label.hold.draw')+"\" class=\"button\" ><tr><td colspan=\"3\" align=\"right\"><a href=\""+(cndtn == 'DRAW_MGMT'?'drawMenu':'drawPerformMenu')+".action?request_locale="+document.getElementById("mylocale").value+"\">"+i18nObj.prop('label.bak.to.srch.res')+"</a> </td></tr></form>";
				} else {
					if (drawType == "a") {
			// bodyStr = holdDraw(bodyStr);
						bodyStr = "<h3>"+i18nObj.prop('label.selected.hold.draw')+"</h3><form action=\"actionOnHold.action\">";
						bodyStr = bodyStr + tblStr;
			// bodyStr = bodyStr + "<input type="submit" value="Hold Draws"
			// class="button" ><tr><td colspan="3" align="right"><a
			// href="drawMenu.action">Back to Search Results</a>
			// </td></tr></form>";
						bodyStr = holdDraw(bodyStr);
					} else {
						if (drawType == "ppr") {
			// bodyStr = holdDraw(bodyStr);
							bodyStr = "<h3>"+i18nObj.prop('label.selected.hold.draw')+"</h3><form action=\"rankChkDraw.action\">";
							bodyStr = bodyStr + tblStr;
							bodyStr = bodyStr + "<tr><td colspan=\"3\" align=\"right\"><input type=\"submit\" value=\""+i18nObj.prop('label.claim.allow')+"\" class=\"button\" >&nbsp;<a href=\""+(cndtn == 'DRAW_MGMT'?'drawMenu':'drawPerformMenu')+".action?request_locale="+document.getElementById("mylocale").value+"\">"+i18nObj.prop('label.bak.to.srch.res')+"</a> </td></tr><tr><input type=\"hidden\" name=\"subHold\" value=\"CLAIM ALLOW\"></tr></form>";
						} else if(drawType == "clmhld"){
											bodyStr = "<h3>"+i18nObj.prop('label.selected.claim.allow.draw')+"</h3><form action=\"rankChkDraw.action\">";
											bodyStr = bodyStr + tblStr;
											bodyStr = bodyStr + "<tr><td colspan=\"3\" align=\"right\"><input type=\"submit\" value=\""+i18nObj.prop('label.claim.hold')+"\" class=\"button\" >&nbsp;<a href=\""+(cndtn == 'DRAW_MGMT'?'drawMenu':'drawPerformMenu')+".action?request_locale="+document.getElementById("mylocale").value+"\">"+i18nObj.prop('label.bak.to.srch.res')+"</a> </td></tr><tr><input type=\"hidden\" name=\"subHold\" value=\"CLAIM HOLD\"></tr></form>";
										
						}
							
							else {
							if (drawType == "resSubmit") {
			// bodyStr = holdDraw(bodyStr);
								bodyStr = "<h3>"+i18nObj.prop('label.selected.draw.res')+"</h3><form action=\"saveDrawResult.action\" >";
								bodyStr = bodyStr + tblStr;
								bodyStr = bodyStr + "<tr><td colspan=\"3\" align=\"right\"><input type=\"submit\" value=\""+i18nObj.prop('label.resolve')+"\" class=\"button\" >&nbsp;<a href=\"resultSubMenu.action?request_locale="+document.getElementById("mylocale").value+"\">"+i18nObj.prop('label.bak.to.srch.res')+"</a> </td></tr></form>";
							}
						}
					}
				}
			}
		}
		}
	}
	//alert(bodyStr);
	_id.i("data", bodyStr);
	return true;
}
function getSignVal(gameNameDev, val) {
	var signVal = new Object();
	var signArr;
	var chkGame = false;
	// alert(gameNameDev);
	if (gameNameDev == "kenosix") {
		maxValue = 80;
	} else if(gameNameDev == "twelvebytwentyfour") {
		maxValue = 24;
	}  else if(gameNameDev == "tenbytwenty") {
		maxValue = 20;
	} else if(gameNameDev == "rainbowgame") {
		maxValue = 12;
	} else if(gameNameDev == "pickthree" || gameNameDev == "pickfour") {
			maxValue = 9;
	} else if (gameNameDev == "keno" ||gameNameDev == "kenotwo"  ||     gameNameDev == "kenofive"  ||  gameNameDev == "kenofour"  ||  gameNameDev == "kenoseven" ||  gameNameDev == "kenoeight"  ) {
		maxValue = 90;
	} else if(gameNameDev == "zimlottobonustwo" || gameNameDev == "zimlottobonustwofree"){
		maxValue = 36;


	} else if(gameNameDev == "tenbythirty") {
		maxValue = 30;
	} else if(gameNameDev == "kenonine") {
		maxValue = 36;
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
				else {
					if (gameNameDev == "bonusballtwo") {
						maxValue = 30;
					}
					else if(gameNameDev == "bonusballlotto"){
						maxValue=39;
					}
						
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
				} else if(gameNameDev.toLowerCase() == "onetotwelve") {
					oneToTwelveArr = ["One(1)", "Two(2)", "Three(3)", "Four(4)", "Five(5)", "Six(6)", "Seven(7)", "Eight(8)", "Nine(9)", "Ten(10)", "Eleven(11)", "Twelve(12)"];
					signArr = oneToTwelveArr;
				}else if(gameNameDev.toLowerCase() == "miniroulette") {
					miniRouletteArr = ["Zero(0)", "One(1)", "Two(2)", "Three(3)", "Four(4)", "Five(5)", "Six(6)", "Seven(7)", "Eight(8)", "Nine(9)", "Ten(10)", "Eleven(11)", "Twelve(12)"];
					signArr = miniRouletteArr;
				}else if(gameNameDev.toLowerCase() == "fullroulette") {
					fullRouletteArr = ["Zero(0)", "One(1)", "Two(2)", "Three(3)", "Four(4)", "Five(5)", "Six(6)", "Seven(7)", "Eight(8)", "Nine(9)", "Ten(10)", "Eleven(11)", "Twelve(12)","Thirteen(13)","Fourteen(14)", "Fifteen(15)", "Sixteen(16)", "Seventeen(17)", "Eighteen(18)", "Ninteen(19)","Twenty(20)", "Twenty One(21)", "Twenty Two(22)", "Twenty Three(23)", "Twenty Four(24)", "Twenty Five(25)", "Twenty Six(26)", "Twenty Seven(27)", "Twenty Eight(28)", "Twenty Nine(29)", "Thirty(30)", "Thirty One(31)", "Thirty Two(32)", "Thirty Three(33)", "Thirty Four(34)", "Thirty Five(35)", "Thirty Six(36)"];
					signArr = fullRouletteArr;
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
	// alert(signVal[val.replace(" ","")]);
	return chkGame ? val : signVal[val.trim().replace(" ", "")];

}
function resSubmitManual(url, statusType, drawType) {
	var drawsArr = _id.n("drawIds");
	var tblObj = _id.o("drawResTbl");
	var gameNo = _id.o("game_no");
	var gameNum = (gameNo.value).split("-")[0];
	var gameNameDev = _id.o("gameName").value.toLowerCase();
	var winOne = '';
	var winTwo = '';
	
	var macOne = '';
	var macTwo = '';
	
	var bodyStr = "";
	var tblStr ="";
	var eventId =""
	if(_id.o("eventIdCap")!=null){
		eventId = (_id.o("eventIdCap").innerHTML).trim();
	}
	var drawName = (_id.o("drawNameCap").innerHTML).trim();
	if(gameNameDev=="kenofive" || gameNameDev=="kenofour")
		tblStr = "<table width=\"800\" cellspacing=\"0\" cellpadding=\"5\" border=\"1\" bordercolor=\"#CCCCCC\" ><tr><th colspan=\"10\" align=\"center\"><input type=\"hidden\" name=\"gameNo\" value='" + gameNum + "'>"+i18nObj.prop('label.draw.list')+"</th></tr><tr><th scope=\"col\" align=\"center\">"+i18nObj.prop('label.draw.id')+" </th> <th scope=\"col\" align=\"center\">"+i18nObj.prop('label.draw.date.time')+" </th><th scope=\"col\" align=\"center\">"+i18nObj.prop('label.draw.name')+"</th><th scope=\"col\" align=\"center\">"+i18nObj.prop('label.usr1.name')+"</th><th scope=\"col\" align=\"center\">"+i18nObj.prop('label.usr1.res')+" </th><th scope=\"col\" align=\"center\">"+i18nObj.prop('label.usr1.mc.res')+"</th><th scope=\"col\" align=\"center\">"+i18nObj.prop('label.usr1.time')+"</th><th scope=\"col\" align=\"center\">"+i18nObj.prop('label.usr2.name')+" </th><th scope=\"col\" align=\"center\">"+i18nObj.prop('label.usr2.res')+" </th><th scope=\"col\" align=\"center\">"+i18nObj.prop('label.usr2.mc.res')+"</th><th scope=\"col\" align=\"center\"> "+i18nObj.prop('label.usr2.time')+"</th></tr>";
	else
		if(eventId==null || eventId==""){
			tblStr = "<table width=\"800\" cellspacing=\"0\" cellpadding=\"5\" border=\"1\" bordercolor=\"#CCCCCC\" ><tr><th colspan=\"10\" align=\"center\"><input type=\"hidden\" name=\"gameNo\" value='" + gameNum + "'>"+i18nObj.prop('label.draw.list')+"</th></tr><tr><th scope=\"col\" align=\"center\">"+i18nObj.prop('label.draw.id')+" </th><th scope=\"col\" align=\"center\">"+i18nObj.prop('label.draw.date.time')+" </th><th scope=\"col\" align=\"center\">"+drawName+"</th><th scope=\"col\" align=\"center\">"+i18nObj.prop('label.usr1.name')+" </th><th scope=\"col\" align=\"center\">"+i18nObj.prop('label.usr1.res')+" </th>  <th scope=\"col\" align=\"center\">"+i18nObj.prop('label.usr1.time')+"</th><th scope=\"col\" align=\"center\">"+i18nObj.prop('label.usr2.name')+" </th><th scope=\"col\" align=\"center\">"+i18nObj.prop('label.usr2.res')+" </th><th scope=\"col\" align=\"center\"> "+i18nObj.prop('label.usr2.time')+"</th></tr>";
		} else{
			tblStr = "<table width=\"800\" cellspacing=\"0\" cellpadding=\"5\" border=\"1\" bordercolor=\"#CCCCCC\" ><tr><th colspan=\"10\" align=\"center\"><input type=\"hidden\" name=\"gameNo\" value='" + gameNum + "'>"+i18nObj.prop('label.draw.list')+"</th></tr><tr><th scope=\"col\" align=\"center\">"+i18nObj.prop('label.draw.id')+" </th><th scope=\"col\" align=\"center\">"+i18nObj.prop('label.draw.date.time')+" </th><th scope=\"col\" align=\"center\">"+drawName+"</th><th scope=\"col\" align=\"center\">"+eventId+" <th scope=\"col\" align=\"center\">"+i18nObj.prop('label.usr1.name')+" </th><th scope=\"col\" align=\"center\">"+i18nObj.prop('label.usr1.res')+" </th>  <th scope=\"col\" align=\"center\">"+i18nObj.prop('label.usr1.time')+"</th><th scope=\"col\" align=\"center\">"+i18nObj.prop('label.usr2.name')+" </th><th scope=\"col\" align=\"center\">"+i18nObj.prop('label.usr2.res')+" </th><th scope=\"col\" align=\"center\"> "+i18nObj.prop('label.usr2.time')+"</th></tr>";
		}
		
	
	
	var count = 0;
	for (var i = 0, l = drawsArr.length; i < l; i++) {
		if (drawsArr[i].checked) {
			winOne = getSignVal(gameNameDev, _id.o("winone" + drawsArr[i].value).innerHTML);
			winTwo = getSignVal(gameNameDev, _id.o("wintwo" + drawsArr[i].value).innerHTML);

			var winOneText = winOne;
			var winTwoText = winTwo;
			if(gameNameDev=="rainbowgame") {
				var myarray=new Array(7);
				for (var j=0; j<7; j++)
				    myarray[j]=new Array(2);
				myarray[0][0]="13";
				myarray[0][1]="Voilet";
				myarray[1][0]="14";
				myarray[1][1]="Indigo";
				myarray[2][0]="15";
				myarray[2][1]="Blue";
				myarray[3][0]="16";
				myarray[3][1]="Green";
				myarray[4][0]="17";
				myarray[4][1]="Yellow";
				myarray[5][0]="18";
				myarray[5][1]="Orange";
				myarray[6][0]="19";
				myarray[6][1]="Red";

				var winOneSplit = winOneText.split(",");
				var winTwoSplit = winTwoText.split(",");
				winOneText = "";
				winTwoText = "";
				var length = (winOneSplit.length == winTwoSplit.length) ? winOneSplit.length : 0;
				for(var j=0; j<length; j++) {
					if(j<3) {
						winOneText += winOneSplit[j].trim() + ",";
						winTwoText += winTwoSplit[j].trim() + ",";
					} else {
						for(var k=0; k<7; k++) {
							if(myarray[k][0] == winOneSplit[j].trim())
								winOneText += myarray[k][1] + ",";
							if(myarray[k][0] == winTwoSplit[j].trim())
								winTwoText += myarray[k][1] + ",";
						}
					}
				}
				winOneText = winOneText.substring(0, winOneText.length-1);
				winTwoText = winTwoText.substring(0, winTwoText.length-1);
			}

			if(gameNameDev=="kenofive" ||  gameNameDev=="kenofour"){
				macOne = getSignVal(gameNameDev, _id.o("macone" + drawsArr[i].value).innerHTML);
				macTwo = getSignVal(gameNameDev, _id.o("mactwo" + drawsArr[i].value).innerHTML);
			}
			
			// add rows of table that are checked
			tblStr = tblStr + "<tr>" + _id.o(drawsArr[i].value).innerHTML + "</tr>";
			if(gameNameDev=="fortunetwo" || gameNameDev=="fortunethree"){
				isMultiResult = true ;
				tblStr = tblStr + "<tr><td colspan=\"7\" align=\"center\"><table cellspacing=\"0\" cellpadding=\"5\" border=\"1\" bordercolor=\"#CCCCCC\" > <tr> <td><input type=\"radio\" name=\"winNumber\" value=\"" + winOne + "\" checked/></td> <td colspan=\"4\" align=\"center\">" + _id.o("winone" + drawsArr[i].value).innerHTML + "</td> <td>"+i18nObj.prop('label.usr1.res')+"</td> </tr> <tr> <td><input type=\"radio\" name=\"winNumber\" value=\"" + winTwo + "\"/></td> <td colspan=\"4\" align=\"center\">" + _id.o("wintwo" + drawsArr[i].value).innerHTML + "</td> <td>"+i18nObj.prop('label.usr2.res')+"</td> </tr> <tr  onclick=\"document.getElementById('newResult').checked=true\"> <td><input type=\"radio\" name=\"winNumber\" id=\"newResult\" value=\"newresult\"/></td><td>" + getWiningFields(gameNameDev.toLowerCase()) + "</td> <td>" + getWiningFields(gameNameDev.toLowerCase()) + "</td> <td>" + getWiningFields(gameNameDev.toLowerCase()) + "</td> <td>" + getWiningFields(gameNameDev.toLowerCase()) + "</td> <td>"+i18nObj.prop('label.newRes')+"</td><td><div id=\"msgDiv\"> </div></td> </tr> </table></td></tr>";
			}else{
				if(gameNameDev=="kenofive" || gameNameDev=="kenofour" )
					tblStr = tblStr + "<tr><td colspan=\"10\" align=\"center\"><table cellspacing=\"0\" cellpadding=\"5\" border=\"1\" bordercolor=\"#CCCCCC\" > <tr> <td><input type=\"radio\" id=\"win1\" name=\"winNumber\" onchange=\"changeMac(this.id);\" value=\"" + winOne + "\" checked/></td> <td>" + _id.o("winone" + drawsArr[i].value).innerHTML + "</td><td><input type=\"radio\" id=\"mac1\" name=\"macNumber\" checked = \"true\" style = \"display:none;\" value=\"" + macOne + "\"/> " + _id.o("macone" + drawsArr[i].value).innerHTML + "</td>    <td>"+i18nObj.prop('label.usr1.res')+"</td> </tr> <tr> <td><input type=\"radio\" name=\"winNumber\" id=\"win2\" onchange=\"changeMac(this.id);\" value=\"" + winTwo + "\"/></td><td><input type=\"radio\" name=\"macNumber\" id=\"mac2\" style = \"display:none;\"   value=\"" + macTwo + "\"/> " + _id.o("wintwo" + drawsArr[i].value).innerHTML + "</td><td>" + _id.o("mactwo" + drawsArr[i].value).innerHTML + "</td> <td>"+i18nObj.prop('label.usr2.res')+"</td> </tr> <tr  onclick=\"document.getElementById('newResult').checked=true\"> <td><input type=\"radio\" name=\"winNumber\" id=\"newResult\" value=\"newresult\"/></td><td>" + getWiningFields(gameNameDev.toLowerCase()) + "</td> <td>" + getWiningFields(gameNameDev.toLowerCase()).replace(/winNumbers/g, "macNumbers").replace(/text_gameID/g, "mac_gameID").replace(/keyPressManualEntry/g, "keyPressManualMachEntry") + "</td> <td>"+i18nObj.prop('label.newRes')+"</td><td><div id=\"msgDiv\"> </div></td> </tr> </table></td></tr>";
				else if(gameNameDev == "rainbowgame")
					tblStr = tblStr + "<tr><td colspan=\"10\" align=\"center\"><table cellspacing=\"0\" cellpadding=\"5\" border=\"1\" bordercolor=\"#CCCCCC\" > <tr> <td><input type=\"radio\" name=\"winNumber\" value=\"" + winOne + "\" checked/></td> <td>" + winOne + "</td> <td>"+i18nObj.prop('label.usr1.res')+"</td> </tr> <tr> <td><input type=\"radio\" name=\"winNumber\" value=\"" + winTwo + "\"/></td> <td>" + winTwo + "</td> <td>"+i18nObj.prop('label.usr2.res')+"</td> </tr> <tr  onclick=\"document.getElementById('newResult').checked=true\"> <td><input type=\"radio\" name=\"winNumber\" id=\"newResult\" value=\"newresult\"/></td><td>" + getWiningFields(gameNameDev.toLowerCase()) + "</td> <td>"+i18nObj.prop('label.newRes')+"</td><td><div id=\"msgDiv\"> </div></td> </tr> </table></td></tr>";
				else {
					tblStr = tblStr + "<tr><td colspan=\"10\" align=\"center\"><table cellspacing=\"0\" cellpadding=\"5\" border=\"1\" bordercolor=\"#CCCCCC\" > <tr> <td><input type=\"radio\" name=\"winNumber\" value=\"" + winOne + "\" checked/></td> <td>" + _id.o("winone" + drawsArr[i].value).innerHTML + "</td> <td>"+i18nObj.prop('label.usr1.res')+"</td> </tr> <tr> <td><input type=\"radio\" name=\"winNumber\" value=\"" + winTwo + "\"/></td> <td>" + _id.o("wintwo" + drawsArr[i].value).innerHTML + "</td> <td>"+i18nObj.prop('label.usr2.res')+"</td> </tr> <tr  onclick=\"document.getElementById('newResult').checked=true\"> <td><input type=\"radio\" name=\"winNumber\" id=\"newResult\" value=\"newresult\"/></td><td>" + getWiningFields(gameNameDev.toLowerCase()) + "</td> <td>"+i18nObj.prop('label.newRes')+"</td><td><div id=\"msgDiv\"> </div></td> </tr> </table></td></tr>";
				}	
			}
			
		}
	}
	tblStr = tblStr + "</table>";
	if (drawType == "resSubmit") {
		bodyStr = "<h3>"+i18nObj.prop('label.selected.draw.res')+"</h3><form name=\"resolve\" onsubmit=\"return validateManualEntry2('" + gameNameDev + "')\"  action=\"resultSubSave.action\">";
		bodyStr = bodyStr + tblStr;
		bodyStr = bodyStr + "<tr><td colspan=\"3\" align=\"right\"><input type=\"submit\" value=\""+i18nObj.prop('label.resolve')+"\" class=\"button\" onclick=\"onResolveSubmit()\">&nbsp;<a href=\"resultSubMenu.action\">"+i18nObj.prop('label.bak.to.srch.res')+"</a> </td></tr></form>";
	}
	_id.i("data", bodyStr);
	// document.forms["resolve"].submit();
	return true;
}

function changeMac(value){
	_id.o(value.replace('win','mac')).checked=true;
}

function onResolveSubmit() {
	if (_id.o("cardType") != null) {
		_id.o("newResult").value = _id.o("cardType").value;
	}
}
function validateManualEntry2(gameNameDev) {
	if(document.getElementsByName("drawIds")[0].checked==false){
		_id.i("msgDiv", "<font color = \"red\">"+i18nObj.prop('error.select.draw.date.time')+" </font>");
		return false;
	}else{
	if (_id.o("newResult").checked == true) {
		if (gameNameDev == "kenosix") {
			maxValue = 80;
		} if(gameNameDev == "twelvebytwentyfour") {
			maxValue = 24;
		} if(gameNameDev == "tenbytwenty") {
			maxValue = 20;
		} if(gameNameDev == "tenbythirty") {
			maxValue = 30;
		} if(gameNameDev == "kenonine") {
			maxValue = 36;
		} if(gameNameDev == "rafflegame1") {
			maxValue = 90;
		} if(gameNameDev == "rainbowgame") {
			maxValue = 12;
		}if(gameNameDev == "pickthree" || gameNameDev == "pickfour") {
				maxValue = 9;
			}
		else if (gameNameDev == "keno" || gameNameDev == "kenotwo" ||      gameNameDev == "kenofive"  ||  gameNameDev == "kenofour"   ||  gameNameDev == "kenoseven" ||  gameNameDev == "kenoeight") {
			maxValue = 90;
		} else {
			if (gameNameDev == "fastlotto") {
				maxValue = 35;
			} else {
				if (gameNameDev == "zimlotto" || gameNameDev == "zimlottotwo"  || gameNameDev == "zimlottobonus" || gameNameDev == "zimlottobonusfree" || gameNameDev == "zimlottothree") {
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
		}else if(gameNameDev == "bonusballlotto"){
				maxValue = 39;
			}else if(gameNameDev == "bonusballtwo"){
				maxValue = 30;
			}
		
		gameNameDev = gameNameDev.toLowerCase();
		var frmVal = new parent.frames[0].Validator("resolve");
		var count = 0;
		var zeroPresent = false;
		var maxPresent = false;
		var oneDigitPresent = false;
		var notNum = false;
		var isZero = false;
		var resultEnter = "";
		var noOfBoxes = 0;
		
		if (gameNameDev == "kenosix") {
			noOfBoxes = 20;
		} else if(gameNameDev == "rainbowgame" || gameNameDev == "pickthree") {
			noOfBoxes = 3;
		} else if(gameNameDev == "pickfour") {
			noOfBoxes = 4;
		} else if(gameNameDev == "twelvebytwentyfour") {
			noOfBoxes = 12;
		} else if(gameNameDev == "tenbytwenty") {
			noOfBoxes = 10;
		} else if(gameNameDev == "tenbythirty") {
			noOfBoxes = 10;
		} else if(gameNameDev == "kenonine") {
			noOfBoxes = 6;
		} else if(gameNameDev == "rafflegame1") {
			noOfBoxes = 5;
		} else if(gameNameDev == "kenoseven") {
			noOfBoxes = 10;
		}
		else if ( gameNameDev == "kenotwo" || gameNameDev == "fastlotto" ||      gameNameDev == "kenofive"  ||  gameNameDev == "kenofour") {
			noOfBoxes = 5;
		} else if (gameNameDev == "keno"    ||  gameNameDev == "kenoeight") {
			noOfBoxes = 6;
		}else {
			if (gameNameDev == "lotto" || gameNameDev == "zimlotto" || gameNameDev == "zimlottotwo" || gameNameDev == "zimlottothree" || gameNameDev == "tanzanialotto"|| gameNameDev == "bonusballlotto" || gameNameDev == "bonusballtwo") {
				noOfBoxes = 6;
			}else if(gameNameDev == "zimlottobonus" || gameNameDev == "zimlottobonustwo" ||gameNameDev == "zimlottobonusfree" || gameNameDev == "zimlottobonustwofree" ){
				noOfBoxes = 7;
			}else if(gameNameDev == "supertwo"){
				noOfBoxes = 2;
			}
		}
		
		var macTextValue;
		var macResultEnter ="";
		var occurences = new Array(6);
		_id.i("msgDiv", "");
		if (gameNameDev == "keno" || gameNameDev == "kenotwo" ||      gameNameDev == "kenofive"  ||  gameNameDev == "kenofour"  ||   gameNameDev == "kenosix"  || gameNameDev == "fastlotto" || gameNameDev == "lotto" || gameNameDev == "zimlotto" || gameNameDev == "zimlottotwo" || gameNameDev == "zimlottobonus" || gameNameDev == "zimlottobonusfree" || gameNameDev == "zimlottothree" || gameNameDev == "supertwo" || gameNameDev == "tanzanialotto" || gameNameDev == "bonusballlotto" || gameNameDev == "bonusballtwo"||gameNameDev == "zimlottobonustwo" ||gameNameDev == "zimlottobonusfree" || gameNameDev == "zimlottobonustwofree" || gameNameDev == "twelvebytwentyfour" || gameNameDev == "tenbytwenty" || gameNameDev == "rafflegame1" ||  gameNameDev == "kenoseven" || gameNameDev == "rainbowgame" || gameNameDev == "pickfour" || gameNameDev == "pickthree" || gameNameDev == "tenbythirty" || gameNameDev == "kenonine" ||  gameNameDev == "kenoeight") {
			for (var counter = 1; counter <= noOfBoxes; counter += 1) {
				var textValue = _id.v("text_gameID:drawIDCtr:" + counter);
				resultEnter = resultEnter + textValue + ",";
				
				if(gameNameDev == "kenofive"  || gameNameDev=="kenofour" ){
					macTextValue = _id.v("mac_gameID:drawIDCtr:" + counter);
					macResultEnter = macResultEnter + macTextValue + ",";
				}
				// alert(textValue);
				if(!((gameNameDev == "keno" || gameNameDev == "kenoeight") && counter == 6)){
					if (textValue == "" || textValue == " " || textValue == "  "  ) {
						_id.i("msgDiv", "<font color = \"red\">"+i18nObj.prop('error.you.cannot.leave.field.blank')+" </font>");
						return false;
					}
				}
				occurences[counter - 1] = textValue;
				
				
				if (textValue == "0") {
					zeroPresent = true;
				}
				if (textValue.trim() != "" && textValue.length < 2) {// alert(oneDigitPresent);
					oneDigitPresent = true;
				}
				if (textValue.trim() != "" && textValue > maxValue) {
					maxPresent = true;
				}
				if (textValue.trim() != "" && isNaN(textValue)) {
					notNum = true;
				}
				if(textValue.trim() != ""  && textValue <=00 && gameNameDev!="pickthree" && gameNameDev!="pickfour" && gameNameDev!="rainbowgame" ){
					isZero = true;
				}
				// alert(notNum+" "+textValue);

				if(gameNameDev == "kenofive"  || gameNameDev=="kenofour" )
				{
				occurences[counter+noOfBoxes - 1] = macTextValue;
				if (macTextValue == "" || macTextValue == " " || macTextValue == "  "  ) {
					_id.i("msgDiv", "<font color = \"red\"> "+i18nObj.prop('error.you.cannot.leave.field.blank')+" </font>");
					return false;
				}
				if (macTextValue == "0") {
					zeroPresent = true;
				}
				if (macTextValue.length < 2) {// alert(1);
					oneDigitPresent = true;
				}
				if (textValue > maxValue) {
					maxPresent = true;
				}
				if (isNaN(macTextValue)) {// alert(notNum+" "+textValue);
					notNum = true;
				}
				if(macTextValue <=00){
					isZero = true;
				}
			}
				
				
			}
			// alert(notNum);
			if (notNum) {
				_id.i("msgDiv", "<font color = \"red\"> "+i18nObj.prop('error.enter.valid.nbr')+" </font>");
				notNum = false;
				return false;
			}
			if (zeroPresent) {
				_id.i("msgDiv", "<font color = \"red\"> "+i18nObj.prop('error.you.cannot.select.zero.as.win.nbr')+" "+(gameNameDev == "kenofive" || gameNameDev=="kenofour" ?"/"+i18nObj.prop('error.mc.nbr')+"":"")+" </font>");
				zeroPresent = false;
				return false;
			}
			if (oneDigitPresent) {
				_id.i("msgDiv", "<font color = \"red\"> "+i18nObj.prop('error.enter.mc.nbr')+(gameNameDev == "kenofive" || gameNameDev=="kenofour" ?"/"+i18nObj.prop('error.mc.nbr')+"":"")+" "+i18nObj.prop('error.double.digit')+"in Double Digits </font>");
				oneDigitPresent = false;
				return false;
			}
			if (arrHasDupes(occurences) && gameNameDev != "supertwo" && gameNameDev != "rainbowgame" && gameNameDev != "pickfour" && gameNameDev != "pickthree") {
				_id.i("msgDiv", "<font color = \"red\"> "+i18nObj.prop('error.duplicate.nbr.val')+"</font>");
				return false;
			}
			if (maxPresent) {
				_id.i("msgDiv", "<font color = \"red\"> "+i18nObj.prop('error.you.cannot.select.gt') + maxValue + " "+i18nObj.prop('error.as.win.nbr')+"as winning number"+(gameNameDev == "kenofive" || gameNameDev=="kenofour" ?"/"+i18nObj.prop('label.freeze.draw')+""+i18nObj.prop('error.mc.nbr')+"":"")+" </font>");
				maxPresent = false;
				return false;
			}
			if (isZero) {
				_id.i("msgDiv", "<font color = \"red\"> "+i18nObj.prop('error.select.gt.zero.as.win.nbr')+(gameNameDev == "kenofive" || gameNameDev=="kenofour" ?"/"+i18nObj.prop('error.mc.nbr'):"")+"("+i18nObj.prop('error.refer.game.rule')+") </font>");
				isZero = false;
				return false;
			}
			if (gameNameDev == "fastlotto" || gameNameDev == "lotto" || gameNameDev == "zimlotto" || gameNameDev == "zimlottotwo" || gameNameDev == "zimlottothree") {
				if (checkSort(occurences)) {
					_id.i("msgDiv", "<font color = \"red\"> "+i18nObj.prop('error.enter.data.asc.order')+"</font>");
					return false;
				}
			}
			occurences = new Array(6);
		} else {
			if (gameNameDev == "card12" || gameNameDev == "card16" || gameNameDev == "fortune" || gameNameDev == "zerotonine" || gameNameDev == "onetotwelve" || gameNameDev == "miniroulette" || gameNameDev == "fullroulette") {
				if (_id.o("cardType") != null) {
					if (_id.o("cardType").value == -1) {
						_id.i("msgDiv", "<font color = \"red\"> "+i18nObj.prop('error.select.ur.res')+" </font>");
						return false;
					}
				}
			}else{
				if(gameNameDev == "fortunetwo" || gameNameDev == "fortunethree"){
					var winNumsF2 = _id.n('winNumbers');
					for(var j = 1; j<=winNumsF2.length ;j++){
						if (winNumsF2[j].value==-1) {
							_id.i("msgDiv", "<font color = \"red\"> "+i18nObj.prop('error.select.ur.res')+"</font>");
						return false;
						}
					}
				}
			
			}
		}
		if(gameNameDev=="rainbowgame") {
			var winNbrs = document.getElementsByName("winNumbers");
			for(var j=3; j<winNbrs.length; j+=1) {
				if(winNbrs[j].value == '-1') {
					//_id.i(drawsArr[i].value +"Message", "<font color = \"red\">Please Select Color.</font>");
					_id.i("msgDiv", "<font color = \"red\">Please Select Color.</font>");
					return false;
				}
			}
		}

		return true;
		}
	}
}
function holdDraw(bodyStr) {
	var id = bodyStr.substring(0, bodyStr.indexOf("drawDateTime"));
	id = id.substring(id.lastIndexOf("id=") + 3, id.length);
	var btn = "&nbsp;<input type=\"submit\" name=\"subHold\" value=\""+i18nObj.prop('error.cancelNRef')+"\" class=\"button\" >";
	var currTime = _id.o("curTime").innerHTML;
	currTime = currTime.substring(0, currTime.indexOf("."));
	var drawTime = _id.o(id + "drawDateTime").innerHTML;
	var freezeTime = _id.o(id + "freezeDateTime").innerHTML;
	if (freezeTime > currTime && drawTime > currTime) {
		btn = "<input type=\"submit\" name=\"subHold\" value=\""+i18nObj.prop('label.active')+"\" class=\"button\" >" + btn;
	} else {
		if (freezeTime < currTime && drawTime > currTime) {
			btn = "<input type=\"submit\" name=\"subHold\" value=\""+i18nObj.prop('error.perform.auto')+"\" class=\"button\" >" + btn;
		} else {
			if (freezeTime < currTime && drawTime < currTime) {
				btn = "<input type=\"submit\" name=\"subHold\" value=\""+i18nObj.prop('error.perform.manual')+"\" class=\"button\" >" + btn;
			}
		}
	}
	return bodyStr + "<table width=\"800\" border=\"0\"><tr><td colspan=\"5\" align=\"right\">" + btn + "&nbsp;<a href=\"drawMenu.action\">"+i18nObj.prop('label.bak.to.srch.res')+"</a> </td></tr></table></form>";
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
		alert(i18nObj.prop('error.choose.atleast.one.draw'));
		ret = false;
	}
	return ret;
}
function validation(time) {
	if (_id.o(time).value == "" || _id.o(time).value == 0) {
		alert(i18nObj.prop('error.enter.time'));
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
			alert(i18nObj.prop('error.ur.postpone.timen.curtime.same'));
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
	var gameNo = _id.o("gameId").value;
	var gameNameDev = _id.o("gameName").value;
	gameNameDev = gameNameDev.toLowerCase();
	
	// gameNameDev=gameNameDev.toLowerCase();
	
	// alert(gameNameDev+"-------------");
	if (gameNameDev == "kenosix") {
		maxValue = 80;
		_id.o("winNumSize").value = 20;
	}else if (gameNameDev == "keno"||gameNameDev == "kenotwo" ||      gameNameDev == "kenofive"  ||  gameNameDev == "kenofour" ||  gameNameDev == "kenoeight"  ) {
		maxValue = 90;
		_id.o("winNumSize").value = 5;
	}else if (gameNameDev == "kenoseven") {
		maxValue = 90;
		_id.o("winNumSize").value = 10;
	}else if(gameNameDev == "zimlottobonus" || gameNameDev == "zimlottobonusfree"){
		maxValue = 42;
		isBonus = true;
		_id.o("winNumSize").value = 7;
	}else if(gameNameDev == "zimlottobonustwo" || gameNameDev == "zimlottobonustwofree"){
		maxValue = 36;
		isBonus = true;
		_id.o("winNumSize").value = 7;
	}else if (gameNameDev == "twelvebytwentyfour") {
		maxValue = 24;
		_id.o("winNumSize").value = 12;
	} else if (gameNameDev == "tenbytwenty") {
		maxValue = 20;
		_id.o("winNumSize").value = 10;
	} else if (gameNameDev == "tenbythirty") {
		maxValue = 30;
		_id.o("winNumSize").value = 10;
	} else if (gameNameDev == "kenonine") {
		maxValue = 36;
		_id.o("winNumSize").value = 6;
	} else if (gameNameDev == "rafflegame1") {
		maxValue = 90;
		_id.o("winNumSize").value = 5;
	}else if (gameNameDev == "rainbowgame") {
		maxValue = 12;
		_id.o("winNumSize").value = 6;
		isZeroAllowed = true;
	}else if (gameNameDev == "pickthree") {
		maxValue = 9;
		_id.o("winNumSize").value = 3;
		isZeroAllowed = true;
	}else if (gameNameDev == "pickfour") {
		maxValue = 9;
		_id.o("winNumSize").value = 4;
		isZeroAllowed = true;
	}else {
		if (gameNameDev == "fastlotto") {
			maxValue = 35;
			_id.o("winNumSize").value = 5;
		} else {
			if (gameNameDev == "zimlotto" || gameNameDev == "zimlottotwo" || gameNameDev == "zimlottothree") {
				maxValue = 42;
				_id.o("winNumSize").value = 6;
			} else if(gameNameDev == "zimlottobonustwo"){
				maxValue = 36;
				_id.o("winNumSize").value = 6;
			}else {
				if (gameNameDev == "lotto" || gameNameDev == "tanzanialotto") {
					maxValue = 49;
					_id.o("winNumSize").value = 6;
				}else{
					if(gameNameDev == "fortunetwo" || gameNameDev == "fortunethree"){
						_id.o("winNumSize").value = 4;
						isMultiResult = true;
					}else{
						if(gameNameDev == "fourdigit" || gameNameDev == "FourDigit"){
							//alert("selected name")
							maxValue = 9999;
							_id.o("winNumSize").value = 23;
							isZeroAllowed = true;
						}
					}
				}
			}
		}
	}
	if(gameNameDev == "supertwo"){
		maxValue = 99;
		_id.o("winNumSize").value = 2;
		isZeroAllowed = true;
	}else {
		if(gameNameDev == "bonusballlotto"){
			maxValue = 39;
			isBonus = true;
			_id.o("winNumSize").value = 6;
		}
		else {
			if(gameNameDev == "bonusballtwo"){
				maxValue = 30;
				isBonus = true;
				_id.o("winNumSize").value = 6;
			}
	}
	}
	// perIdArr[i].innerHTML.replace(/ /g,"")=='PMEP'
	var winningFields = getWiningFields(gameNameDev);
	var performStatusObj = fetchDivByName(document,"performStatusID");
	//fields only for FourDigitGame
	if(gameNameDev == "fourdigit"){
		var temp = gameNameDev;
		var topPrizeFields = getWiningFields(temp+"T");
		var specialPrizeFields = getWiningFields(temp+"S");
		var consPrizeFields = getWiningFields(temp+"C");
	}
		
	for (var i = 0; i < drawsArr.length; i += 1) {
		if(isMultiResult){
		_id.i(drawsArr[i].value + "WinRes1", winningFields.replace(/drawID/g, drawsArr[i].value+"1"));
		_id.i(drawsArr[i].value + "WinRes2", winningFields.replace(/drawID/g, drawsArr[i].value+"2"));
		_id.i(drawsArr[i].value + "WinRes3", winningFields.replace(/drawID/g, drawsArr[i].value+"3"));
		_id.i(drawsArr[i].value + "WinRes4", winningFields.replace(/drawID/g, drawsArr[i].value+"4"));
		}else {
			if(gameNameDev == "kenofive" || gameNameDev=="kenofour" ){
				for(var j=0;j<2;j+=1){
					if(j==1)
					_id.i(drawsArr[i].value + "MacRes", winningFields.replace(/drawID/g, drawsArr[i].value).replace(/winNumbers/g, "macNumbers").replace(/text_gameID/g, "mac_gameID").replace(/keyPressManualEntry/g, "keyPressManualMachEntry"));
					else
					_id.i(drawsArr[i].value + "WinRes", winningFields.replace(/drawID/g, drawsArr[i].value));
				}
			}if(gameNameDev == "fourdigit"){				
					//alert("in else");
					_id.i(drawsArr[i].value + "WinRes1", topPrizeFields.replace(/drawID/g, drawsArr[i].value));
					_id.i(drawsArr[i].value + "WinRes2", specialPrizeFields.replace(/drawID/g, drawsArr[i].value));
					_id.i(drawsArr[i].value + "WinRes3", consPrizeFields.replace(/drawID/g, drawsArr[i].value));
			}
			else{
			_id.i(drawsArr[i].value + "WinRes", winningFields.replace(/drawID/g, drawsArr[i].value));
			}
		}
		if (performStatusObj != null && performStatusObj[i].innerHTML.replace(/ /g, "") == "PMEP") {
			_id.i(drawsArr[i].value + "DrawTypeSelect", "<input type=\"hidden\" name=\"drawType\" value=\"PMEM\"/><font color = \"green\">"+i18nObj.prop('error.only.man.status.applicable')+" </font>");
		} else {
			var options = "<select name=\"drawType\" class=\"option\">" + "<option value=\"PMEM\">"+i18nObj.prop('label.manual')+"</option>" + "<option value=\"PAEM\">"+i18nObj.prop('label.auto')+"</option>" + "</select>";
			_id.i(drawsArr[i].value + "DrawTypeSelect", options);
		}
	}
	

	}
function getWiningFields(gameNameDev) {
	gameNameDev = gameNameDev.toLowerCase();
	var winningFillingFields = "";
	if(gameNameDev == "fourdigitt"){
		winningFillingFields = formTextBoxesForFourDigit(3,1);
	}else if(gameNameDev == "fourdigits"){
		winningFillingFields = formTextBoxesForFourDigit(10,4);
	}else if(gameNameDev == "fourdigitc"){
		winningFillingFields = formTextBoxesForFourDigit(10,14);
	}else if(gameNameDev == "kenosix"){
		winningFillingFields = formTextBoxes(20);
	}else if(gameNameDev == "kenofive" || gameNameDev=="kenofour" ){
		winningFillingFields = formTextBoxes(5);
	}else if(gameNameDev == "kenoseven"){
		winningFillingFields = formTextBoxes(10);
	}else if ( gameNameDev == "fastlotto" || gameNameDev == "kenotwo" ) {
		winningFillingFields = formTextBoxes(5);
	}else if (gameNameDev == "keno" || gameNameDev == "kenoeight") {
		winningFillingFields = formTextBoxes(6);
	}else if (gameNameDev == "twelvebytwentyfour") {
		winningFillingFields = formTextBoxes(12);
	}else if (gameNameDev == "tenbytwenty") {
		winningFillingFields = formTextBoxes(10);
	} else if (gameNameDev == "tenbythirty") {
		winningFillingFields = formTextBoxes(10);
	} else if (gameNameDev == "kenonine") {
		winningFillingFields = formTextBoxes(6);
	} else if (gameNameDev == "rafflegame1") {
		winningFillingFields = formTextBoxes(5);
	}else if (gameNameDev == "rainbowgame") {
		winningFillingFields = formTextBoxesForRainbow(3, 3);
		isZeroAllowed=true;
	}else if (gameNameDev == "pickthree") {
		isZeroAllowed=true;
		winningFillingFields = formTextBoxes(3);
	}else if (gameNameDev == "pickfour") {
		isZeroAllowed=true;
		winningFillingFields = formTextBoxes(4);
	}else if(gameNameDev == "zimlottobonus" || gameNameDev == "zimlottobonusfree"||gameNameDev == "zimlottobonustwo" || gameNameDev == "zimlottobonustwofree"){
		winningFillingFields = winningFillingFields + formTextBoxes(7);
	} else {
		if (gameNameDev == "lotto" || gameNameDev == "zimlotto" || gameNameDev == "zimlottotwo" || gameNameDev == "zimlottothree" || gameNameDev == "tanzanialotto" || gameNameDev == "bonusballlotto"  || gameNameDev == "bonusballtwo") {
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
					if (gameNameDev == "fortune" || gameNameDev == "fortunetwo") {
						var signArr = ["Aries", "Taurus", "Gemini", "Cancer", "Leo", "Virgo", "Libra", "Scorpio", "Sagittarius", "Capricorn", "Aquarius", "Pisces"];
						winningFillingFields = formOptions(signArr);
					} else {
						if (gameNameDev == "zerotonine") {
							var signArr = ["Zero(0)", "One(1)", "Two(2)", "Three(3)", "Four(4)", "Five(5)", "Six(6)", "Seven(7)", "Eight(8)", "Nine(9)"];
							winningFillingFields = formOptions(signArr);
						} else if (gameNameDev == "onetotwelve") {
							var signArr = ["One(1)", "Two(2)", "Three(3)", "Four(4)", "Five(5)", "Six(6)", "Seven(7)", "Eight(8)", "Nine(9)", "Ten(10)", "Eleven(11)", "Twelve(12)"];
							winningFillingFields = formOptions(signArr);
						}else if (gameNameDev == "miniroulette") {
							var signArr = ["Zero(0)", "One(1)", "Two(2)", "Three(3)", "Four(4)", "Five(5)", "Six(6)", "Seven(7)", "Eight(8)", "Nine(9)", "Ten(10)", "Eleven(11)", "Twelve(12)"];
							winningFillingFields = formOptions(signArr);
						}else if (gameNameDev == "fullroulette") {
							var signArr = ["Zero(0)", "One(1)", "Two(2)", "Three(3)", "Four(4)", "Five(5)", "Six(6)", "Seven(7)", "Eight(8)", "Nine(9)", "Ten(10)", "Eleven(11)", "Twelve(12)","Thirteen(13)","Fourteen(14)", "Fifteen(15)", "Sixteen(16)", "Seventeen(17)", "Eighteen(18)", "Ninteen(19)","Twenty(20)", "Twenty One(21)", "Twenty Two(22)", "Twenty Three(23)", "Twenty Four(24)", "Twenty Five(25)", "Twenty Six(26)", "Twenty Seven(27)", "Twenty Eight(28)", "Twenty Nine(29)", "Thirty(30)", "Thirty One(31)", "Thirty Two(32)", "Thirty Three(33)", "Thirty Four(34)", "Thirty Five(35)", "Thirty Six(36)"];
							winningFillingFields = formOptions(signArr);
						} else {
							if(gameNameDev == "fortunethree"){
								var signArr = [ "A-Spade",
								                "A-Heart",
								                "A-Club",
								                "A-Diamond",
								                "Q-Spade",
								                "Q-Heart",
								                "Q-Club",
								                "Q-Diamond",
								                "K-Spade",
								                "K-Heart",
								                "K-Club",
								                "K-Diamond"];
								winningFillingFields = formOptions(signArr);
							}
						}
					}
				}
			}
		}
	}
	if(gameNameDev == "supertwo"){
		winningFillingFields = formTextBoxes(2);
	}
	//alert ("returning winning fields");
	return winningFillingFields;
}
function formTextBoxes(numberOfBoxes) {
	var textBoxes = "";
	for (var counter = 1; counter <= numberOfBoxes; counter += 1) {
		if(counter==6 && isBonus && numberOfBoxes !=7){
			
			textBoxes = textBoxes + "<input type=\"text\" maxlength=\"2\" minlength=\"1\" title=\""+i18nObj.prop('label.freeze.draw')+"Bonus Ball Result\" style=\"width: 25px;font-size:10px; margin-bottom:4px; background:yellow\" name=\"winNumbers\" id=\"text_gameID:drawIDCtr:" + counter + "\" onkeyUp=\"return keyPressManualEntry(this,event)\" onmousedown=\"return disClick(this)\"/>";
		}else if(counter==7 && isBonus){
			textBoxes = textBoxes + "<input type=\"text\" maxlength=\"2\" minlength=\"1\" title=\""+i18nObj.prop('label.freeze.draw')+"Bonus Ball Result\" style=\"width: 25px;font-size:10px; margin-bottom:4px; background:yellow\" name=\"winNumbers\" id=\"text_gameID:drawIDCtr:" + counter + "\" onkeyUp=\"return keyPressManualEntry(this,event)\" onmousedown=\"return disClick(this)\"/>";

		}else{
		textBoxes = textBoxes + "<input type=\"text\" maxlength=\"2\" minlength=\"1\" style=\"width: 25px;font-size:10px; margin-bottom:4px\" name=\"winNumbers\" id=\"text_gameID:drawIDCtr:" + counter + "\" onkeyUp=\"return keyPressManualEntry(this,event)\" onmousedown=\"return disClick(this)\"/>";
		}
	}
	return textBoxes;
}

function formTextBoxesForFourDigit(numberOfBoxes,counterValue) {
	//alert("perparing boxes");
	var textBoxes = "";
	for (var counter = counterValue; counter <= (numberOfBoxes+counterValue)-1; counter += 1) {
			textBoxes = textBoxes + "<input type=\"text\" maxlength=\"4\" minlength=\"4\" style=\"width: 50px;font-size:10px; margin-bottom:4px\" name=\"winNumbers\" id=\"text_gameID:drawIDCtr:" + counter + "\" onkeyUp=\"return keyPressManualEntryForFourDigit(this,event)\" onmousedown=\"return disClick(this)\"/>";
	}
	//alert(textBoxes);
	return textBoxes;
}

function formTextBoxesForKenoFive(numberOfBoxes) {
	var textBoxes = "";
	for (var counter = 1; counter <= numberOfBoxes; counter += 1) {
		if(counter>5)
			textBoxes = textBoxes + "<input type=\"text\" maxlength=\"2\" minlength=\"1\" style=\"width: 25px;font-size:10px; margin-bottom:4px\" name=\"macNumbers\" id=\"text_gameID:drawIDCtr:" + counter + "\" onkeyUp=\"return keyPressManualEntry(this,event)\" onmousedown=\"return disClick(this)\"/>";
		else
			textBoxes = textBoxes + "<input type=\"text\" maxlength=\"2\" minlength=\"1\" style=\"width: 25px;font-size:10px; margin-bottom:4px\" name=\"winNumbers\" id=\"text_gameID:drawIDCtr:" + counter + "\" onkeyUp=\"return keyPressManualEntry(this,event)\" onmousedown=\"return disClick(this)\"/>";
	}
	return textBoxes;
}

function formTextBoxesForRainbow(numberOfBoxes, numberOfSelect) {
	var textBoxes = "";
	var selectList = "";
	for (var counter = 1; counter <= numberOfBoxes; counter += 1) {
		textBoxes = textBoxes + "<input type=\"text\" maxlength=\"2\" minlength=\"1\" style=\"width: 35px; height:20px; font-size:15px; margin-right:4px; margin-bottom:1px\" name=\"winNumbers\" id=\"text_gameID:drawIDCtr:" + counter + "\" onkeyUp=\"return keyPressManualEntry(this,event)\" onmousedown=\"return disClick(this)\"/>";
	}
	var idCounter = counter;
	for (var counter = 1; counter <= numberOfSelect; counter += 1) {
		idCounter++;
		selectList = selectList + "<select style=\"font-size:10px; margin-right:2px; margin-bottom:1px\" name=\"winNumbers\" class=\"option\" id=\"text_gameID:drawIDCtr:" + idCounter + "\"><option value=\"-1\">-Select-</option><option value=\"13\">Voilet</option><option value=\"14\">Indigo</option><option value=\"15\">Blue</option><option value=\"16\">Green</option><option value=\"17\">yellow</option><option value=\"18\">Orange</option><option value=\"19\">Red</option></select>";
	}
	return "<table border=\"0\"><tr><td align=\"right\"><b>Enter Numbers: </b></td><td align=\"left\">" + textBoxes + "</td></tr><tr><td align=\"right\"><b>Enter Colors:</b></td><td>" + selectList + "</td></tr></table>";
}

function formOptions(signArr) {
	if(!isMultiResult){
		var select = "<select name=\"cardType\" class=\"option\" id=\"select:drawID\" onchange='onSelectNewResult(this.value)'>";
		var firstOption = "<option value=\"-1\">--Please Select--</option>";
		var addedOptions = "";
		for (var i = 1; i <= signArr.length; i += 1) {
			addedOptions = addedOptions + "<option value=\"" + i + "\">" + signArr[i - 1] + "</option>";
		}
		return select + firstOption + addedOptions + "</select>";
	}else {
		var select = "<select name=\"winNumbers\" id=\"select:drawID\">";
		var firstOption = "<option value=\"-1\">--Please Select--</option>";
		var addedOptions = "";
		for (var i = 1; i <= signArr.length; i += 1) {
			addedOptions = addedOptions + "<option value=\"" + i + "\">" + signArr[i - 1] + "</option>";
		}
		return select + firstOption + addedOptions + "</select>";
	}
}

function onSelectNewResult(result){
_id.o('newResult').value=result;
}

function validateManualEntry(gameNameDev) {
    gameNameDev = gameNameDev.toLowerCase();
    if (gameNameDev == "kenosix") {
		maxValue = 80;
	}else if (gameNameDev == "keno" || gameNameDev == "kenotwo"   ||     gameNameDev == "kenofive"  ||  gameNameDev == "kenofour"  ||  gameNameDev == "kenoseven" ||  gameNameDev == "kenoeight"  ) {
		maxValue = 90;
	} else if(gameNameDev == "twelvebytwentyfour") {
		maxValue = 24;
	}  else if(gameNameDev == "tenbytwenty") {
		maxValue = 20;
	}  else if(gameNameDev == "tenbythirty") {
		maxValue = 30;
	}  else if(gameNameDev == "kenonine") {
		maxValue = 36;
	} else if(gameNameDev == "rafflegame1") {
		maxValue = 90;
	} else if(gameNameDev == "pickthree" || gameNameDev == "pickfour") {
		maxValue = 9;
	} else if(gameNameDev == "rainbowgame") {
		maxValue = 12;
	}
	else {
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
	if(gameNameDev == "fourdigit")
	{
		maxValue = 9999;
		//alert("Max:" + maxValue);
	}
	if(gameNameDev == "supertwo"){
		maxValue = 99;
	}else{
		if(gameNameDev == "bonusballlotto"){
			maxValue = 39;
		}
		else{
			if(gameNameDev == "bonusballtwo"){
				maxValue = 30;
			}
	}
	}
	
	var frmVal = new parent.frames[0].Validator("manualEntryForm");
	//alert("FormVal:"+frmVal);
	var count = 0;
	var zeroPresent = false;
	var maxPresent = false;
	var oneDigitPresent = false;
	var notNum = false;
	var resultEnter = "";
	var valueLength = false;
	var drawsArr = _id.n("drawIds");
	//alert("drawIdlength :"+drawsArr.length);
	var ret = new Array(drawsArr.length);
	//alert
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
	if(gameNameDev == "fourdigit")
	{
		noOfBoxes = 23;
		//alert("nofofboxes: "+noOfBoxes);
	}else if (gameNameDev == "kenosix") {
		noOfBoxes = 20;
	}else if (gameNameDev == "fastlotto" || gameNameDev == "kenotwo"   ||     gameNameDev == "kenofive"  ||  gameNameDev == "kenofour") {
		noOfBoxes = 5;
	}else if (gameNameDev == "keno" ||  gameNameDev == "kenoeight"  ) {
		noOfBoxes = 6;
	}else if (gameNameDev == "kenoseven") {
		noOfBoxes = 10;
	}else if(gameNameDev == "zimlottobonus" || gameNameDev == "zimlottobonustwo" ||gameNameDev == "zimlottobonusfree" || gameNameDev == "zimlottobonustwofree"){
		noOfBoxes = 7;
	} else if(gameNameDev == "rafflegame1"){
		noOfBoxes = 5;
	} if(gameNameDev == "twelvebytwentyfour") {
		noOfBoxes = 12;
	} if(gameNameDev == "tenbytwenty") {
		noOfBoxes = 10;
	} if(gameNameDev == "tenbythirty") {
		noOfBoxes = 10;
	} if(gameNameDev == "kenonine") {
		noOfBoxes = 6;
	} if(gameNameDev == "rainbowgame" || gameNameDev == "pickthree" ) {
		noOfBoxes = 3;
	} if( gameNameDev == "pickfour") {
		noOfBoxes = 4;
	} else {
		if (gameNameDev == "lotto" || gameNameDev == "zimlotto" || gameNameDev == "zimlottotwo" || gameNameDev == "zimlottothree" || gameNameDev == "tanzanialotto" || gameNameDev == "bonusballlotto" || gameNameDev == "bonusballtwo") {
			noOfBoxes = 6;
		}else if(gameNameDev == "supertwo"){
			noOfBoxes = 2;
		}
	}
	var drawType = _id.n("drawType");
	var occurences = new Array(noOfBoxes);
	for (var i = 0; i < drawsArr.length; i += 1) {
		_id.i(drawsArr[i].value + "Message", "");
		//alert(drawsArr[i].value);
		document.error_disp_handler.isValid = true;
	}
	
	var macTextValue;
	var macResultEnter ="";
	for (var i = 0; i < drawsArr.length; i += 1) {
		document.error_disp_handler.isValid = true;
		if (drawsArr[i].checked) {
			if (gameNameDev=="fourdigit" || gameNameDev == "keno" || gameNameDev == "kenotwo" || gameNameDev == "fastlotto" || gameNameDev == "lotto" || gameNameDev == "zimlotto" || gameNameDev == "zimlottotwo" || gameNameDev == "zimlottothree" || gameNameDev == "supertwo" || gameNameDev == "tanzanialotto" || gameNameDev == "bonusballlotto" || gameNameDev == "bonusballtwo" || gameNameDev == "zimlottobonus" || gameNameDev == "zimlottobonusfree"   ||     gameNameDev == "kenofive"  ||  gameNameDev == "kenofour"  || gameNameDev == "kenosix"||gameNameDev == "zimlottobonustwo" ||gameNameDev == "zimlottobonustwofree" || gameNameDev == "twelvebytwentyfour" || gameNameDev == "tenbytwenty" || gameNameDev == "kenoeight" || gameNameDev == "rainbowgame" || gameNameDev == "pickfour" || gameNameDev == "pickthree" || gameNameDev == "tenbythirty" || gameNameDev == "kenonine") {
				for (var counter = 1; counter <= noOfBoxes; counter += 1) {
					var textValue = _id.v("text_gameID:" + drawsArr[i].value + "Ctr:" + counter);
					//alert("TextValue:"+textValue);
					if(gameNameDev == "kenofive" || gameNameDev=="kenofour" ){
						macTextValue = _id.v("mac_gameID:" + drawsArr[i].value + "Ctr:" + counter);
						macResultEnter = macResultEnter + macTextValue + ",";
					}
						 //alert(textValue);
					resultEnter = resultEnter + textValue + ",";
					occurences[counter - 1] = textValue;
					if(!((gameNameDev == "keno" || gameNameDev == "kenoeight") && counter == 6)){
						if (textValue == "") {
							frmVal.aV("text_gameID:" + drawsArr[i].value + "Ctr:" + counter, "req", i18nObj.prop('error.select.win')+(gameNameDev == "kenofive" || gameNameDev=="kenofour" ?"/"+ i18nObj.prop('label.machine'):"")+"result", drawsArr[i].value + i18nObj.prop('label.msg'));
							ret[i] = false;
						}
					}
					if (textValue == "0") {
						zeroPresent = true;
					}
					if (textValue != "" && textValue.length < 2) {// alert(oneDigitPresent);
						oneDigitPresent = true;
					}
					if (textValue != "" && textValue > maxValue) {
						maxPresent = true;
					}
					if (textValue != "" && isNaN(textValue)) {
						notNum = true;
					}
					
					if(gameNameDev == "fourdigit")
					{
							if(textValue.length != 4)
							{
								valueLength = true;
							}
					}

					if(gameNameDev == "kenofive" || gameNameDev=="kenofour" || gameNameDev == "rafflegame1"){
						occurences[counter+noOfBoxes - 1] = macTextValue;
						if (macTextValue == "") {
							//frmVal.aV("text_gameID:" + drawsArr[i].value + "Ctr:" + counter, "req", "Please select winning "+(gameNameDev == "kenofive"?"/machine ":"")+"result", drawsArr[i].value + "Message");
							_id.o(drawsArr[i].value+ i18nObj.prop('label.msg')).innerHTML="<font color = 'red'>"+i18nObj.prop('error.select.win')+(gameNameDev == "kenofive" || gameNameDev=="kenofour" ?"/"+ i18nObj.prop('label.machine'):"")+i18nObj.prop('label.result')+"</font>";
							ret[i] = false;
						}
						if (macTextValue == "0") {
							zeroPresent = true;
						}
						if (macTextValue.length < 2) {// alert(oneDigitPresent);
							oneDigitPresent = true;
						}
						if (macTextValue > maxValue) {
							maxPresent = true;
						}
						if (isNaN(macTextValue)) {
							notNum = true;
						}
					}
					
				}
				if (ret[i] && notNum) {
					_id.i(drawsArr[i].value + i18nObj.prop('label.msg'), "<font color = \"red\"> "+i18nObj.prop('error.enter.valid.nbr')+" </font>");
					ret[i] = false;
					notNum = false;
				}
				if (ret[i] && zeroPresent) {
					_id.i(drawsArr[i].value + i18nObj.prop('label.msg'), "<font color = \"red\"> "+i18nObj.prop('error.you.cannot.select.zero.as.win.nbr')+(gameNameDev == "kenofive" || gameNameDev=="kenofour" ?"/"+i18nObj.prop('label.freeze.draw')+i18nObj.prop('error.mc.nbr'):"")+" </font>");
					ret[i] = false;
					zeroPresent = false;
				}
				if (ret[i] && oneDigitPresent) {
					_id.i(drawsArr[i].value + i18nObj.prop('label.msg'), "<font color = \"red\"> "+i18nObj.prop('error.enter.mc.nbr')+(gameNameDev == "kenofive" || gameNameDev=="kenofour" ?"/"+i18nObj.prop('label.freeze.draw')+i18nObj.prop('error.mc.nbr'):"")+i18nObj.prop('error.double.digit')+"</font>");
					ret[i] = false;
					oneDigitPresent = false;
				}
				if (ret[i] && arrHasDupes(occurences) && gameNameDev != "supertwo" && gameNameDev != "rainbowgame" && gameNameDev != "pickthree" && gameNameDev != "pickfour") {
					_id.i(drawsArr[i].value + i18nObj.prop('label.msg'), "<font color = \"red\"> "+i18nObj.prop('error.duplicate.nbr.val')+"</font>");
					ret[i] = false;
				}
				if (ret[i] && maxPresent) {
					_id.i(drawsArr[i].value + i18nObj.prop('label.msg'), "<font color = \"red\"> "+i18nObj.prop('error.you.cannot.select.gt') + maxValue + i18nOn.prop('error.as.win.nbr')+(gameNameDev == "kenofive" || gameNameDev=="kenofour" ?"/"+i18nObj.prop('error.mc.nbr'):"")+" </font>");
					ret[i] = false;
					maxPresent = false;
				}
				if (ret[i] && gameNameDev == "fastlotto" || gameNameDev == "lotto" || gameNameDev == "zimlotto" || gameNameDev == "zimlottotwo" || gameNameDev == "zimlottothree") {
					if (checkSort(occurences)) {
						_id.i(drawsArr[i].value + i18nObj.prop('label.msg'), "<font color = \"red\"> "+i18nObj.prop('error.enter.data.asc.order')+"</font>");
						ret[i] = false;
						return false;
					}
				}				
				if(ret[i] && valueLength)
				{
					_id.i(drawsArr[i].value + i18nObj.prop('label.msg'), "<font color = \"red\"> "+i18nObj.prop('error.enter.win.nbr.four.digit.only')+" </font>");
					ret[i] = false;
					maxPresent = false;
				}
				occurences = new Array(noOfBoxes);
			} else {
				if (gameNameDev == "card12" || gameNameDev == "card16" || gameNameDev == "fortune" || gameNameDev == "zerotonine" || gameNameDev == "onetotwelve" || gameNameDev == "miniroulette" || gameNameDev == "fullroulette") {
					if (_id.o("select:" + drawsArr[i].value).value == "-1") {
						ret[i] = false;
					}
					resultEnter = _id.o("select:" + drawsArr[i].value).value;
					frmVal.aV("select:" + drawsArr[i].value, "dontselect=0", i18nObj.prop('error.select.win.res'), drawsArr[i].value + "Message");
				}else{
					if(gameNameDev == "fortunetwo" || gameNameDev == "fortunethree"){
						for(var j = 1; j<=_id.o("winNumSize").value ;j++){
							if (_id.o(i18nObj.prop('label.select')+":" + drawsArr[i].value+j).value == "-1") {
								ret[i] = false;
							}
							resultEnter = _id.o(i18nObj.prop('label.select')+":" + drawsArr[i].value+j).value;
							frmVal.aV(i18nObj.prop('label.select')+":" + drawsArr[i].value+j, "dontselect=0", i18nObj.prop('error.select.win.res'), drawsArr[i].value + i18nObj.prop('label.msg'));
						}
					}
				}
			}
			if(ret[i] && gameNameDev=="rainbowgame") {
				var winNbrs = document.getElementsByName("winNumbers");
				for(var j=3; j<winNbrs.length; j+=1) {
					if(winNbrs[j].value == '-1') {
						_id.i(drawsArr[i].value +"Message", "<font color = \"red\">Please Select Color.</font>");
						ret[i] = false;
						break;
					}
				}
			}
			if (ret[i] && document.error_disp_handler.isValid) {
				_id.i(drawsArr[i].value +"Message", "<font color = \"green\"> "+i18nObj.prop('label.correct.data.now')+"</font>");
			}
			if (ret[i] && document.error_disp_handler.isValid) {
				_id.i(drawsArr[i].value +"Message", "<font color = \"green\"> "+i18nObj.prop('label.correct.data.now')+"</font>");
			}
		}
	}
	for (var i = 0; i < ret.length; i += 1) {
		if (ret[i] == false) {
			break;
		}
	}
	var macNbrs;
	var macTextAppender;
	var confirmSubmit = false;
	if (i == ret.length && document.error_disp_handler.isValid) {
		var alertText = i18nObj.prop('label.win.nbrs')+": ";
		if(gameNameDev=="zerotonine"){
		   var winNbrs=jQuery( "[name='cardType']" ).val()-1;
		   alertText +=winNbrs;
		} else if(gameNameDev=="rainbowgame"){
			var winNbrs = document.getElementsByName("winNumbers");
			for(var i=0; i<winNbrs.length; i++) {
				var color = "";
				if(winNbrs[i].value == '13')
					color = "Voilet";
				if(winNbrs[i].value == '14')
					color = "Indigo";
				if(winNbrs[i].value == '15')
					color = "Blue";
				if(winNbrs[i].value == '16')
					color = "Green";
				if(winNbrs[i].value == '17')
					color = "Yellow";
				if(winNbrs[i].value == '18')
					color = "Orange";
				if(winNbrs[i].value == '19')
					color = "Red";

				if(color == "")
					alertText += winNbrs[i].value+"-";
				else
					alertText += color+"-";
			}
			alertText = alertText.substring(0, alertText.length-1);
		} else if (gameNameDev=="onetotwelve") {			
			var winNbrs = document.getElementsByName("cardType");
			for(var i=0; i<winNbrs.length; i++)
				alertText += winNbrs[i].value+"-";
			alertText = alertText.substring(0, alertText.length-1);
		} else if (gameNameDev == "miniroulette" || gameNameDev == "fullroulette") {
			var winNbrs = document.getElementsByName("cardType");
			for(var i=0; i<winNbrs.length; i++)
				alertText += (parseInt(winNbrs[i].value) - 1)+"-";
			alertText = alertText.substring(0, alertText.length-1);
		}		
		else {
			var winNbrs = document.getElementsByName("winNumbers");
			for(var i=0; i<winNbrs.length; i++)
				alertText += winNbrs[i].value+"-";
			alertText = alertText.substring(0, alertText.length-1);
		}
		if(gameNameDev == "kenofive" || gameNameDev=="kenofour" ){
			macNbrs = document.getElementsByName("macNumbers");
			macTextAppender = i18nObj.prop('error.mc.nbr')+"s: "
			for(var j=0; j<macNbrs.length; j+=1){
				macTextAppender += macNbrs[j].value+"-";
				}
			macTextAppender = macTextAppender.substring(0, macTextAppender.length-1);
			alertText+=macTextAppender;
		}
		confirmSubmit = confirm(alertText+".\n\n"+i18nObj.prop('error.you.r.abt.to.submit.ur.win.entry.want.proceed'));
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
			if(isZeroAllowed){
				if (obj.value > maxValue || isNaN(obj.value)) {
					revertValue(obj);
				}else{
					var elms = _id.n("winNumbers");
					for (var l = 0; l < elms.length; l++) {
						if (elms[l].value == "") {
							elms[l].focus();
							break;
						}
					}
				}
			}else{
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
		}
	} else {
		revertValue(obj);
		return false;
	}
	return true;
}


function keyPressManualEntryForFourDigit(obj, e) {// alert(_id.n('winNumbers').length);
	var keyCode = (window.event)? event.keyCode : e.which;
	// alert(keyCode);
	if ((keyCode >= 48 && keyCode < 58) || keyCode == 45 || keyCode == 27 || keyCode == 13 || keyCode == 40 || keyCode == 38 || keyCode == 46 || keyCode == 8 || (keyCode >= 96 && keyCode < 106 || keyCode == 9)) {
		if (keyCode == 46 || keyCode == 8 || keyCode == 9) {
			return true;
		}
		if (obj.value.length ==4) {	
			if(isZeroAllowed){
				if (obj.value > maxValue || isNaN(obj.value)) {
					revertValue(obj);
				}else{
					var elms = _id.n("winNumbers");
					for (var l = 0; l < elms.length; l++) {
						if (elms[l].value == "") {
							elms[l].focus();
							break;
						}
					}
				}
			}else{
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
		}
	} else {
		revertValue(obj);
		return false;
	}
	return true;
}



function keyPressManualMachEntry(obj, e) {
	var keyCode = (window.event)? event.keyCode : e.which;
	if ((keyCode >= 48 && keyCode < 58) || keyCode == 45 || keyCode == 27 || keyCode == 13 || keyCode == 40 || keyCode == 38 || keyCode == 46 || keyCode == 8 || (keyCode >= 96 && keyCode < 106 || keyCode == 9)) {
		if (keyCode == 46 || keyCode == 8 || keyCode == 9) {
			return true;
		}
		if (obj.value.length > 1) {	
			if(isZeroAllowed){
				if (obj.value > maxValue || isNaN(obj.value)) {
					revertValue(obj);
				}else{
					var elms = _id.n("macNumbers");
					for (var l = 0; l < elms.length; l++) {
						if (elms[l].value == "") {
							elms[l].focus();
							break;
						}
					}
				}
			}else{
				if (obj.value == 0 || obj.value > maxValue || isNaN(obj.value)) {
					revertValue(obj);
				} else {
					var elms = _id.n("macNumbers");
					for (var l = 0; l < elms.length; l++) {
						if (elms[l].value == "") {
							elms[l].focus();
							break;
						}
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
		alert(i18nObj.prop('error.choose.perform.status'));
		return false;
	}
	var i18nObj = top.frames[0].i18Obj;
	var frmVal = new parent.frames[0].Validator("searchForm");
	if (_id.o("drawId").value == 0) {
		_id.o("drawId").value = "";
	}
	
	// add by vaibhav
	var status = /please select/i;
	if (_id.o("status") != null && status.test(_id.o("status").value)) {
		alert(i18nObj.prop('error.draw.status.empty'));
		_id.o("status").focus();
		return false;
	}
				// frmVal.aV("phone","req","Please Enter Phone Number","Phone");
	frmVal.aV("drawId", "numeric", i18nObj.prop('error.enter.corerct.draw.id'), "draw_err");
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
function onlyNumeric(e) {
	var keyCode = (window.event)? event.keyCode : e.which;
	if ((keyCode < 48 || keyCode > 57) && keyCode != 8) {
		var key = String.fromCharCode(keyCode);
		// alert(key);
		// if(key != '-' && key != '+')
		if(key != '-')	
			return false;
		
			
	}
	return true;
}
function _frrezeValidate() {
	var frmVal = new parent.frames[0].Validator("changeFreezeForm");
	if (_id.o("freezeTime").value == 0) {
		_id.o("freezeTime").value = "";
	}
	frmVal.aV("freezeTime", "req", i18nObj.prop('error.enter.freeze.time'), "frreze_err");
	frmVal.aV("freezeTime", "numeric", i18nObj.prop('error.enter.correct.freeze.time'), "frreze_err");
	
	return document.error_disp_handler.isValid;
}
function _postponeValidate(count) {
	document.getElementById("loadWait").innerHTML = i18nObj.prop('label.wait')+".....<img src=\"" + projectName + "/LMSImages/images/loadingAjax.gif\"/>";
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
			alert(i18nObj.prop('error.ur.postpone.timen.curtime.same'));
			return false;
		} else {
			var dateArr = date.split("/");
			var newDateArr = newDate.split("/");
			var dateObj = new Date(dateArr[2], dateArr[1], dateArr[0], hour, min, sec);
			var newDateObj = new Date(newDateArr[2], newDateArr[1], newDateArr[0], newHour, newMin, newSec);
			var postPoneMin = Math.ceil((newDateObj.getTime() - dateObj.getTime()) / (1000 * 60));
			if (postPoneMin < 0) {
				alert(i18nObj.prop('error.ur.postpone.time.lt.cur.time'));
				return false;
			}
			_id.o("postponeTime").value = postPoneMin;
		}
	} else {
		if (_id.o("postponeTime").value == 0) {
			_id.o("postponeTime").value = "";
		}
		frmVal.aV("postponeTime", "req", i18nObj.prop('error.enter.postpone.time'), "postpone_err");
		frmVal.aV("postponeTime", "numeric", i18nObj.prop('error.enter.correct.postpone.time'), "postpone_err");
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
function validateFreezeTime(){
	document.getElementById("freezeMsg").innerHTML="";
	var freezeTime=document.getElementById("freezeTime").value;
	var freezeArr =freezeTime.split("-");
	if(freezeArr.length>2){
	document.getElementById("freezeMsg").innerHTML=i18nObj.prop('error.enter.valid.freeze.time');
	return false;
	}
	return true;
	}
parent.frames[0].Version["$Source: /rep/LMS_Mgmt/WebRoot/com/skilrock/lms/web/drawGames/drawMgmt/js/drawGameMgmt.js,v $"] = "$Id: drawGameMgmt.js,v 1.7 2016/10/31 09:46:59 neeraj Exp $";

