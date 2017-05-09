var respData;
var index = 0;
var gameNameMap = {};
gameNameMap["KenoTwo"] = "Lucky Numbers";
gameNameMap["MiniRoulette"] = "Mini Roulette";
gameNameMap["TwelveByTwentyFour"] = "Mini Keno";
gameNameMap["OneToTwelve"] = "One To Twelve";
gameNameMap["TenByTwenty"] = "10-20-Game";
gameNameMap["FullRoulette"] = "Full Roulette";
gameNameMap["KenoSix"] = "Super Keno";
gameNameMap["ZimLottoBonus"] = "Bonus Lotto";
var actionNameMap = {
		"TwelveByTwentyFour" : path+'/com/skilrock/lms/web/drawGames/playMgmt/twelveByTwentyFourBuy.action',
		"OneToTwelve"        : path+'/com/skilrock/lms/web/drawGames/playMgmt/oneToTwelveBuy.action',
		"KenoSix" 			 : path+'/com/skilrock/lms/web/drawGames/playMgmt/kenoSixBuy.action',
		"MiniRoulette"       : path+'/com/skilrock/lms/web/drawGames/playMgmt/oneToTwelveRouletteBuy.action',
		"KenoTwo"            : path+'/com/skilrock/lms/web/drawGames/playMgmt/kenoTwoBuy.action',
		"Soccer 13"			 : path+'/rest/tpSlMgmt/slePurchaseTicket',
		"Soccer 10"          : path+'/rest/tpSlMgmt/slePurchaseTicket',
		"Soccer 6"           : path+'/rest/tpSlMgmt/slePurchaseTicket',
		"Soccer 4"           : path+'/rest/tpSlMgmt/slePurchaseTicket',
		"TenByTwenty"        : path+'/com/skilrock/lms/web/drawGames/playMgmt/tenByTwentyBuy.action',
		"FullRoulette"       : path+'/com/skilrock/lms/web/drawGames/playMgmt/fullRouletteBuy.action',                
		"ZimLottoBonus"      : path+'/com/skilrock/lms/web/drawGames/playMgmt/zimLottoBonusBuy.action' 
};

$(document).ready(function(){
    updatePanel();
	
	$(document).on('click','.pricebtnCon',function(){
		index = $(this).children().attr('index');
		/*var data =  respData;//$.parseJSON(respData);
		var saleType = data.responseData[index].saleType;
		var tokenId = data.responseData[index].tokenId;
		var retId = data.responseData[index].organizationId;
		var dataForSale = data.responseData[index].requestData;
		buyTicket(dataForSale,saleType,tokenId,retId);*/
		curTrx = "EBetTestPrint";
		var testPrint = {
	            "mode": curTrx,
	            "serviceName":"DGE"
		};
		printData = JSON.stringify(testPrint);
		setAppData(printData);	
	});

	$(document).on('click','.trashIcon',function() {
		var requestId = $(this).attr('reqId');
		var reqData = '{"requestId":'+requestId+'}';
		var isConfirm = confirm("Do you want to cancel the bet ?");
		if (!isConfirm) {
			return false;
		}
		var cancelRespData = _ajaxCallJson(path+'/com/skilrock/lms/eBetMgmt/cancelBetSlip.action','requestData='+reqData);
	    //cancelRespData = $.parseJSON(cancelRespData);
	    if (cancelRespData.isSuccess) {
	    	alert("Bet slip cancelled !!");
	    	updatePanel();
		} else {
			alert(cancelRespData.errorMsg);
		}
	   });
});

function updatePanel() {
		var reqData = '{"username":'+ $("#userName").val()+'}';
		respData = _ajaxCallJson(path+'/com/skilrock/lms/eBetMgmt/fetchActiveBetSlip.action','requestData='+reqData);
		//console.log(respData);
		//console.log(JSON.stringify(respData));
		createBetTab(respData);
}

function createBetTab(respData) {
	$('.blockWrapper').html('');
	var betTabHtml = '';
	var finalRespData = respData;//$.parseJSON(respData);
	var saleRequestData;
	//console.log(finalRespData);
	var count = 0;
	var flag = true;
	var totPurAmtForSle = 0;
	var respLength = finalRespData.responseData.length;
	$(finalRespData.responseData).each(function(i){
		saleRequestData = $.parseJSON(this.requestData);
		//pickedNumArr = []; 
		selectionArr = [];
		if ("DGE" == this.saleType) {
			if (saleRequestData.betTypeData.length > 1) {
				$(saleRequestData.betTypeData).each(function(){
					selectionArr.push(this.pickedNumbers);
				});
			}else {
				selectionArr = saleRequestData.betTypeData[0].pickedNumbers.split(",");
			}
			
		} else {
			//saleRequestData = decodeURIComponent(saleRequestData);
			//saleRequestData = $.parseJSON(saleRequestData.data);
			$(saleRequestData.drawInfo[0].eventData).each(function() {
				tempSelection = decodeURIComponent(this.eventSelected);
				evtSelected = tempSelection.length > 1 ? tempSelection.replace(/\,/g, '-'):tempSelection;
				selectionArr.push(evtSelected);
			});
		}
		var j = i == 0 ? 0 : i-1 ;
		if (count % 2 == 0 || (finalRespData.responseData[j]!=undefined && finalRespData.responseData[j].deviceCode != this.deviceCode)) {
			count = 0;
			if(flag){
				betTabHtml += '<div class="game_Block">';
			}else {
				betTabHtml += '<div class="game_Block mr_0">';

			}			
		}
		if(count % 2 == 0){
			betTabHtml += '<div class="screenNo">'+this.deviceCode+'</div>';
			betTabHtml += '<ul class="game_BlockInnerWrap clearfix">';
		}
		if (count == 0) {
			betTabHtml += '<li class="colleft">';
		} else {
			betTabHtml += '<li class="colright">';

		}
		betTabHtml += '<div class="trashIcon" reqId= "'+this.requestId+'"><i class="fa fa-trash-o"></i></div>';
		betTabHtml += '<div class="gameNameTitle">'+this.tokenId+'</div>';
		if ("DGE" == this.saleType) {
			betTabHtml += '<div class="game_type"><div class="game_icon"><img src="'+path+'/com/skilrock/lms/web/drawGames/common/images/KenoSix.png" alt="Game Icon"/></div>';
			betTabHtml += '<div class="game_typeName">'+ getGameDisplayName(saleRequestData.commonSaleData.gameName)+'</div></div>';
		} else {
			betTabHtml += '<div class="game_type"><div class="game_icon"><img src="'+path+'/com/skilrock/lms/web/drawGames/common/images/sportsLottery.png" alt="Game Icon"/></div>';
			betTabHtml += '<div class="game_typeName">'+saleRequestData.gameName+'</div></div>';

		}
		betTabHtml += '<div class="game_typeNumberWrap"><div class="numtxt">SELECTION</div>';
		betTabHtml += '<ul class="game_BlockInnerWrap clearfix">';
		for (var c = 0; c <selectionArr.length ; c++){
				if (c == selectionArr.length -1 ) {
					betTabHtml += '<li><span>'+selectionArr[c]+'</span></li>';
				} else {
					betTabHtml += '<li><span>'+selectionArr[c]+'</span>,</li>';
				}
			}
		betTabHtml += '</ul></div>';
		betTabHtml += '<button type="button" class="pricebtnCon clearfix">';
		betTabHtml += '<div class="priceBlock" index="'+i+'">';
		betTabHtml += '<div><span>$</span>'+saleRequestData.totalPurchaseAmt+'</div>';
		betTabHtml += '</div>';
		betTabHtml += '<div class="buynowBlock">';
		betTabHtml += '<strong>BUY</strong><div>NOW</div>';
		betTabHtml += '</div>';
		betTabHtml += '</button>';
	    betTabHtml += '</li>';
	    var j = i == finalRespData.responseData.length ? i : i+1 ;
	    var isDeviveCodeDiff = true;
	    if(count % 2 != 0 || (finalRespData.responseData[j] != undefined && finalRespData.responseData[j].deviceCode != this.deviceCode)){
	    	betTabHtml += '</ul></div>';
	    }
	    count++;
	    if(i%2 == 0){
	    	flag = !flag;
	    }
	});
	$('.blockWrapper').append(betTabHtml);
}

function buyTicket(requestData,saleType,tokenId,retailerId) {
    var position = 0;
	var gameName = "DGE" == saleType ? $.parseJSON(requestData).commonSaleData.gameName : $.parseJSON(requestData).gameName;
	var param1 = ',"tokenId":"'+tokenId+'"';
	var param2 = ',"retailerId":"'+retailerId+'"';
	position = requestData.length - 1;
	requestData = [requestData.slice(0, position), param1, requestData.slice(position)].join('');
	position = requestData.length - 1;
	requestData = [requestData.slice(0, position), param2, requestData.slice(position)].join('');
    //console.log(requestData);
	
	var actionURL = getActionName(gameName);
	var isConfirm = confirm("Do you want to purchase");
    if(!isConfirm){
    	return false;
    }
     //gameName = "TwelveByTwentyFour";
     // for Dge
     //var saleRespData ='{"isSuccess":true,"errorMsg":"","mainData":{"commonSaleData":{"ticketNumber":"70120407017128018","gameName":"12/24Game","purchaseDate":"2016-03-10","purchaseTime":"11: 08: 10","purchaseAmt":10,"drawData":[{"drawId":"318","drawDate":"10-03-2016","drawName":"Thursday12/24","drawTime":"18: 00: 00"}]},"betTypeData":[{"isQp":true,"betName":"Direct12","pickedNumbers":"02, 03, 05, 06, 10, 12, 14, 15, 16, 17, 18, 19","numberPicked":"12","unitPrice":10,"noOfLines":1,"betAmtMul":1,"panelPrice":10}],"advMsg":{"BOTTOM":["bottom msg for all dg"],"TOP":["12-24 LOTTO NOW MATCH 12 IS JACKPOT OF N 2.50 MILLIONS WINNERS SHARE JACKPOT EQUALLY.","top dg message for all game"]},"orgName":"TestRetailer","userName":"testret","isPromo":false}}';// _ajaxCallJson(actionURL,"json="+requestData);
     // For SLE
     //var saleRespData = '{"responseCode":0,"tktData":{"gameTypeId":1,"reprintCount":0,"retailerName":"Test Retailer","currSymbol":"USD","gameId":1,"boardData":[{"drawId":957,"drawTime":"06:00:00","drawName":"SUNDAY-S13","unitPrice":1,"noOfLines":1,"eventData":[{"eventLeague":"AFC Champions League","eventVenue":"Al-Hilal","eventCodeAway":"GUA","eventDisplayHome":"Al-Hilal","eventDate":"2016-09-25 00:00:00.0","eventDisplayAway":"Guangzhou Evergrande","selection":"H","eventCodeHome":"AL-"},{"eventLeague":"AFC Champions League","eventVenue":"Al-Ahli-1","eventCodeAway":"BEI","eventDisplayHome":"Al-Ahli-1","eventDate":"2016-09-25 00:00:00.0","eventDisplayAway":"Beijing Guoan","selection":"D","eventCodeHome":"AL-"},{"eventLeague":"AFC Champions League","eventVenue":"Arsenal","eventCodeAway":"BEI","eventDisplayHome":"Arsenal","eventDate":"2016-09-25 00:00:00.0","eventDisplayAway":"Beijing Guoan","selection":"A","eventCodeHome":"ARS"},{"eventLeague":"AFC Champions League","eventVenue":"Arsenal","eventCodeAway":"GUA","eventDisplayHome":"Arsenal","eventDate":"2016-09-25 00:00:00.0","eventDisplayAway":"Guangzhou Evergrande","selection":"H","eventCodeHome":"ARS"},{"eventLeague":"AFC Champions League","eventVenue":"Al Ain","eventCodeAway":"GUA","eventDisplayHome":"Al Ain","eventDate":"2016-09-25 00:00:00.0","eventDisplayAway":"Guangzhou Evergrande","selection":"D","eventCodeHome":"AL "},{"eventLeague":"AFC Champions League","eventVenue":"Al Ain","eventCodeAway":"AL-","eventDisplayHome":"Al Ain","eventDate":"2016-09-25 00:05:00.0","eventDisplayAway":"Al-Hilal","selection":"A","eventCodeHome":"AL "},{"eventLeague":"AFC Champions League","eventVenue":"FC Seoul","eventCodeAway":"BEI","eventDisplayHome":"FC Seoul","eventDate":"2016-09-25 00:05:00.0","eventDisplayAway":"Beijing Guoan","selection":"H","eventCodeHome":"FC "},{"eventLeague":"AFC Champions League","eventVenue":"Beijing Guoan","eventCodeAway":"AL-","eventDisplayHome":"Beijing Guoan","eventDate":"2016-09-25 00:05:00.0","eventDisplayAway":"Al-Hilal","selection":"D","eventCodeHome":"BEI"},{"eventLeague":"AFC Champions League","eventVenue":"Al-Hilal","eventCodeAway":"GUA","eventDisplayHome":"Al-Hilal","eventDate":"2016-09-25 00:05:00.0","eventDisplayAway":"Guangzhou Evergrande","selection":"A","eventCodeHome":"AL-"},{"eventLeague":"AFC Champions League","eventVenue":"Al Ain","eventCodeAway":"ARS","eventDisplayHome":"Al Ain","eventDate":"2016-09-25 00:35:00.0","eventDisplayAway":"Arsenal","selection":"H","eventCodeHome":"AL "},{"eventLeague":"AFC Champions League","eventVenue":"Al Sadd","eventCodeAway":"ARS","eventDisplayHome":"Al Sadd","eventDate":"2016-09-25 00:55:00.0","eventDisplayAway":"Arsenal","selection":"D","eventCodeHome":"AL "},{"eventLeague":"AFC Champions League","eventVenue":"Al-Hilal","eventCodeAway":"FC ","eventDisplayHome":"Al-Hilal","eventDate":"2016-09-25 00:55:00.0","eventDisplayAway":"FC Seoul","selection":"A","eventCodeHome":"AL-"},{"eventLeague":"AFC Champions League","eventVenue":"Al-Ahli-1","eventCodeAway":"BEI","eventDisplayHome":"Al-Ahli-1","eventDate":"2016-09-25 01:40:00.0","eventDisplayAway":"Beijing Guoan","selection":"H","eventCodeHome":"AL-"}],"boardPrice":1,"drawDate":"2016-09-25"}],"eventType":"[H, D, A]","gameName":"Soccer","orgName":"AFRICA LOTTO","jackpotMsg":"Current Jackpot Fund@(All Prizes Parimutuel)@13 out Of 13 Match@10000.00 USD","purchaseTime":"14:17:40","brCd":"887962266171280040","balance":"9970184.57","ticketNo":"887962266171280040","expiryPeriod":"","purchaseDate":"2016-09-22","gameTypeName":"Soccer 13","trxId":"50057","ticketAmt":"1.00"},"responseMsg":"Success","sampleStatus":"NO","mode":"Buy"}';
    //var saleRespData = _ajaxCallJson(actionURL,"json="+requestData);
    if ("DGE" == saleType) {
    	 var saleRespData = _ajaxCallJson(actionURL,"json="+requestData);
	} else {
		requestData = $.parseJSON(requestData);
		requestData.sessionId = $("#sessionId").val();
		requestData = JSON.stringify(requestData);
		//requestData = decodeURIComponent(requestData);
		//console.log(requestData);
		var saleRespData = _ajaxCallRest(actionURL,'','',requestData);

	}
    //console.log(JSON.stringify(saleRespData));
   
	if (saleRespData == "NETERROR") {
	 		alert("Server not reachable, Please check connection and try again !!!");
	 		return;
	 	}
     if ("DGE" == saleType) {
    	 if(!saleRespData.isSuccess) {
	    	 if(saleRespData.errorMsg != null && saleRespData.errorMsg != "" ) {
	    		alert(saleRespData.errorMsg);
	    		updatePanel();
	    		return;
			} else {
				alert("Internal System Error !!!");
				updatePanel();
				return;
			}  		
	     }else {
	    	 var finalSaleRespData = {};
	    	 //var saleRespData = {"isSuccess":true,"errorMsg":"","mainData":{"commonSaleData":{"ticketNumber":"70120407017128018","gameName":"12/24Game","purchaseDate":"2016-03-10","purchaseTime":"11: 08: 10","purchaseAmt":10,"drawData":[{"drawId":"318","drawDate":"10-03-2016","drawName":"Thursday12/24","drawTime":"18: 00: 00"}]},"betTypeData":[{"isQp":true,"betName":"Direct12","pickedNumbers":"02, 03, 05, 06, 10, 12, 14, 15, 16, 17, 18, 19","numberPicked":"12","unitPrice":10,"noOfLines":1,"betAmtMul":1,"panelPrice":10}],"advMsg":{"BOTTOM":["bottom msg for all dg"],"TOP":["12-24 LOTTO NOW MATCH 12 IS JACKPOT OF N 2.50 MILLIONS WINNERS SHARE JACKPOT EQUALLY.","top dg message for all game"]},"orgName":"TestRetailer","userName":"testret","isPromo":false}};	    	 
	    	 curTrx = "Buy";
	    	 if("OneToTwelve" == gameName || "MiniRoulette" == gameName || "FullRoulette" == gameName ){
	    		  finalSaleRespData = { 
				 					 "serviceName" : "DGE",
    			 					 "mode" : curTrx,
    			 					 "gameDevName" : gameName,
									 "orgName" : organizationName,
									 "advMsg" : saleRespData.mainData.advMessage, 
									 "retailerName" : saleRespData.mainData.userName,
									 "brCd" :  saleRespData.mainData.commonSaleData.barcodeCount, 
									 "ticketNumber" : saleRespData.mainData.commonSaleData.ticketNumber+"0",
									 "gameName" : saleRespData.mainData.commonSaleData.gameName,
									 "purchaseDate" :dateFormat(new Date(getCompataibleDate2(saleRespData.mainData.commonSaleData.purchaseDate)),dateFormat.masks.sikkimDate),
									 "purchaseTime" : getCompataibaleTime(saleRespData.mainData.commonSaleData.purchaseTime),
									 "purchaseAmt" : saleRespData.mainData.commonSaleData.purchaseAmt,
									 "betTypeData" : saleRespData.mainData.betTypeData,
									 "currencySymbol" : currencySymbol, 
									 "isPromo" : saleRespData.mainData.isPromo,
									 "drawData" : saleRespData.mainData.commonSaleData.drawData					  
	    		 };
	    	 } else {
		    	  finalSaleRespData = {
					    			 "serviceName" : "DGE",
				 					 "mode" : curTrx,		 
									 "orgName" : organizationName,
				 					 "gameDevName" : gameName,
									 "advMsg" : saleRespData.mainData.advMessage, 
									 "retailerName" : saleRespData.mainData.userName,
									 "brCd" : saleRespData.mainData.commonSaleData.barcodeCount, 
									 "ticketNumber" : saleRespData.mainData.commonSaleData.ticketNumber+"0",
									 "gameName" : saleRespData.mainData.commonSaleData.gameName,
									 "purchaseDate" :dateFormat(new Date(getCompataibleDate2(saleRespData.mainData.commonSaleData.purchaseDate)),dateFormat.masks.sikkimDate),
									 "purchaseTime" :getCompataibaleTime(saleRespData.mainData.commonSaleData.purchaseTime),
									 "purchaseAmt" : saleRespData.mainData.commonSaleData.purchaseAmt,
									 "isQp" : saleRespData.mainData.betTypeData[0].isQp,
									 "betName" : saleRespData.mainData.betTypeData[0].betName,
									 "unitPrice" : saleRespData.mainData.betTypeData[0].unitPrice,
									 "noOfLines" : saleRespData.mainData.betTypeData[0].noOfLines,
									 "betAmtMul" : saleRespData.mainData.betTypeData[0].betAmtMul,
									 "panelPrice" : saleRespData.mainData.betTypeData[0].panelPrice,
									 "currencySymbol" : currencySymbol, 
									 "isPromo" : saleRespData.mainData.isPromo,
									 "drawData" : saleRespData.mainData.commonSaleData.drawData,
									 "pickedNumbers" : saleRespData.mainData.betTypeData[0].pickedNumbers,
									 "numbersPicked" : saleRespData.mainData.betTypeData[0].numberPicked 
		    	  	};
	    	 }  
	     }	
	}else if ("SLE" == saleType) {
		if(saleRespData.responseCode != 0){
			if(saleRespData.responseCode== 10003){
				alert("Draw Freezed or Not Available!");
			} else if (saleRespData.responseCode== 118 || saleRespData.responseCode ==10012){
				alert("Time Out ! Login again !!");
				return;
			} else if (saleRespData.responseCode== 20001) {
				alert("Something went wrong! please try again!!" );
			} else {
				alert(saleRespData.responseMsg);
				return;
			}
		} else{
			curTrx = "BUY";
			var finalSaleRespData = {};
			finalSaleRespData= {
					 "serviceName" : "SLE",
					 "mode" : curTrx,		 
 					 "gameDevName" : saleRespData.tktData.gameTypeName,
					 "orgName" : saleRespData.tktData.orgName,
					 "retailerName" : saleRespData.tktData.retailerName,
					 "brCd" :  saleRespData.tktData.brCd, 
					 "ticketNumber" :saleRespData.tktData.ticketNo,
					 "gameName" : saleRespData.tktData.gameName,
					 "purchaseDate" :dateFormat(new Date(getCompataibleDate2(saleRespData.tktData.purchaseDate)),dateFormat.masks.sikkimDate),
					 "purchaseTime" : getCompataibaleTime(saleRespData.tktData.purchaseTime),
					 "purchaseAmt" : saleRespData.tktData.ticketAmt,
					 "boardData" :  saleRespData.tktData.boardData,
					 "currencySymbol" : saleRespData.tktData.currSymbol, 
					 "eventType":saleRespData.tktData.eventType,
					 "jackpotMsg":saleRespData.tktData.jackpotMsg
			};
		}
	 }
     printData = JSON.stringify(finalSaleRespData);
     setAppData(printData);
     updatePanel();
}

function setAppData(data) {
	isValid = false;
	var connSuccess = false;
	var connection = new WebSocket('ws://localhost:8082');
	connection.onopen = function () {
   		connection.send(data);
   		if(connection.readyState == 1){
   			connSuccess = true;
   		}
	};
	connection.onerror = function(){
		if("Buy" == curTrx){
			/*var data = {"ticketNumber":tktNum, "autoCancel":true};
			data = JSON.stringify(data);
			var _resp = winAjaxReq("cancelTicketAuto.action?json=" + data);*/
			parent.frames[0].updateBalance(path + "/com/skilrock/lms/web/loginMgmt/updateAvlCreditAmt.action");
			alert("Cannot communicate to printing application.\nPlease check your NodeJS server is running and try again.\nTo start NodeJs server either restart your system or\n follow the steps below:\n" +
					"1.) Open My Computer. \n2.) Goto C:/Program Files (x86)/skilrock/dgpcpos (For 64-bit system) and\n" +
					"     C:/Program Files (x86)/skilrock/dgpcpos (For 32-bit system).\n3.) Double click on runNodeInvisible to start NodeJs server.");
		} else if("EBetTestPrint" == curTrx){
			alert("Cannot communicate to printing application.\nPlease check your NodeJS server is running and try again.\nTo start NodeJs server either restart your system or\n follow the steps below:\n" +
					"1.) Open My Computer. \n2.) Goto C:/Program Files (x86)/skilrock/dgpcpos (For 64-bit system) and\n" +
					"     C:/Program Files (x86)/skilrock/dgpcpos (For 32-bit system).\n3.) Double click on runNodeInvisible to start NodeJs server.");
		}
		
	};
	connection.onmessage = function (evt) {
		//console.log(evt.data);
		var printStatus = "";
    	var dataReceived = $.parseJSON(evt.data);
    	if("APP_NOT_FOUND" == dataReceived){
    		printStatus = dataReceived;
    	}else{
    		printStatus = dataReceived.utf8Data;
    	}
        connection.close();
	    if(connSuccess){
	    	checkAndShowTicket(printStatus);
	    	
		}
    };
    //updatePanel();
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

function checkAndShowTicket(printStatus) {
	if("SUCCESS" == printStatus) {
		parent.frames[0].updateBalance(path + "/com/skilrock/lms/web/loginMgmt/updateAvlCreditAmt.action");
		reprintCountChild = 0;
		if("EBetTestPrint" == curTrx){
			var data =  respData;//$.parseJSON(respData);
			var saleType = data.responseData[index].saleType;
			var tokenId = data.responseData[index].tokenId;
			var retId = data.responseData[index].organizationId;
			var dataForSale = data.responseData[index].requestData;
			buyTicket(dataForSale,saleType,tokenId,retId);
		}
		return true;
	} else if("FAIL" == printStatus){
		alert("Printer not connected !!");
	} else if("APP_NOT_FOUND" == printStatus){
		if("Buy" == curTrx){
			/*var data = {"ticketNumber":tktNum, "autoCancel":true};
			data = JSON.stringify(data);
			var _resp = winAjaxReq("cancelTicketAuto.action?json="+data);
			parent.frames[0].updateBalance(path + "/com/skilrock/lms/web/loginMgmt/updateAvlCreditAmt.action");*/
			alert("Cannot communicate to printing application.\nPlease check your Printing Application is running and try again.\nTo start Printing Application either restart your system or\n follow the steps below:\n" +
					"1.) Open My Computer. \n2.) Goto C:/Program Files (x86)/skilrock/dgpcpos (For 64-bit system) and\n" +
					"     C:/Program Files (x86)/skilrock/dgpcpos (For 32-bit system).\n3.) Double click on PrintingApplication to start printing application.");
		}else if("EBetTestPrint" == curTrx){
			alert("Cannot communicate to printing application.\nPlease check your Printing Application is running and try again.\nTo start Printing Application either restart your system or\n follow the steps below:\n" +
					"1.) Open My Computer. \n2.) Goto C:/Program Files (x86)/skilrock/dgpcpos (For 64-bit system) and\n" +
					"     C:/Program Files (x86)/skilrock/dgpcpos (For 32-bit system).\n3.) Double click on PrintingApplication to start printing application.");
		}
	} else if("INVALIDINPUT" == printStatus) {
		if("Buy" == curTrx){
			alert("Ticket cannot be printed due to \nInternal System Error!!\nLast ticket has been cancelled!!");
		}else if("EBetTestPrint" == curTrx){
			alert("Printer not connected !!");
		}
	}
	
}

function getCompataibleDate2(date){
 	var dArr = date.split("-");
 	var finalDate = dArr[1]+"/"+dArr[2]+"/"+dArr[0];
 	return finalDate;
 }
 
//if time comes in HH:MM:SS format
 function getCompataibaleTime(time){
 	var tArr=time.split(":");
 	var finalTime= tArr[0]+":"+tArr[1];
 	return finalTime;
 }
 
 function getGameDisplayName(gameDevName) {
	  return gameNameMap[gameDevName];
 }
 function getActionName(gameDevName) {
	 return actionNameMap[gameDevName];
 } 

