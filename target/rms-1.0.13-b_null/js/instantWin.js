var gameIndex = 0;
var respData = {};
var printData = "";
var curTrx="";
var status;
var legendInfoNotReqTheme=["MSA","MSS","YNHDN","WNYN"];
var leftRightMidTheme=["MSA","MSS","FSWLP","MWLP"];
var purchaseReqData;
var tktNbr='';
var keyupFiredCount=0;
var printUsingApplet;
var nVer = navigator.appVersion;
var nAgt = navigator.userAgent;
var browserName  = navigator.appName;
var fullVersion  = ''+parseFloat(navigator.appVersion); 
var majorVersion = parseInt(navigator.appVersion,10);
var nameOffset,verOffset,ix;


$(document).ready(function(){
	if ((verOffset=nAgt.indexOf("MSIE"))!=-1) {
    	printUsingApplet = "YES";
    }else if ((verOffset=nAgt.indexOf("Chrome"))!=-1) {
    	printUsingApplet = "NO";
    }
	
    var gameListReqData = JSON.stringify({
		"domainName": "lms",
		"userName"    : $('#userName').val(),
		"deviceType": "PCPOS",
		"clientType": "print",
		"isPrint": "Y",
    });
    var requestParam = path+'/com/skilrock/lms/web/instantWin/playMgmt/action/fetchGameData.action';
	var gameData=_ajaxCallJson(requestParam,"requestData="+gameListReqData);
	if(gameData.responseCode==0){
		respData=gameData;
		updateMidPanel();
	}else{
		alert(gameData.responseMsg);
	}
	
	/**
	 * When PWT is clicked
	 */
	$("#pwt").on("click",function(){
		keyupFiredCount=0;
		$("#pwt-win").css("display","block");
		$("#pwtTicket").val('');
		$("#error-message").html("");
		$("#pwtTicket").focus();
	});
	
	/**
	 * When close button of error popup is clicked
	 */
	$("#err-popup-button").on("click",function(){
		$("#error-popup").css('display','none');
		$("#error").html("");
	});
	
	
	
	 /**
     * Barcode scanner and MSR code starts
     */
    function DelayExecution(f, delay) {
        var timer = null; 
        return function () {
            var context = this, args = arguments;
            clearTimeout(timer);
            timer = window.setTimeout(function () {
                f.apply(context, args);
            },
            delay || 100);
        };
    }
    
    $.fn.ConvertToBarcodeTextbox = function () {
         $(this).keyup(DelayExecution(function (event) {
         if((event.keyCode==13 || $("#pwtTicket").val().length==12 ||$("#pwtTicket").val().length==14) && keyupFiredCount<=1){
        	 $('#pwtOk').click();
         }
             var keycode = (event.keyCode ? event.keyCode : event.which);  
                 keyupFiredCount = keyupFiredCount + 1;  
         }));
     };
     
     try {
    	 
         $("#pwtTicket").ConvertToBarcodeTextbox();

     } catch (e) { 
    	 
     }
	
});

/**
 * When click on game div buy popup will be display
 */
$(document).on('click',".gameDiv",function(){
	gameIndex = $(this).parent().attr('menu-id');
	$("#gameName .popTitle").html(respData.games[gameIndex].gameName);
	$("#img").attr('src',respData.games[gameIndex].gameLogoPath);
	$(".priceBox").html(currencySymbol+' '+respData.games[gameIndex].gamePrice);
	$("#gameDesc").html(respData.games[gameIndex].gameDescription);
	$("#parentAppletIW").css("display", "none");
	$("#buyPopUp").css("display","block");
});

/**
 * When cancel or close button on buy popup is selected
 */
$(document).on('click',"#cancelBtn,.popUpClose",function(){	
	$("#buyPopUp").css("display","none");
});

/**
 * When cancel or close button on pwt popup is selected
 */
$(document).on('click',"#pwtClose,#pwtCancel",function(){	
	$("#pwtTicket").val("");
	$("#error-message").html("");
	$("#pwt-win").css("display","none");
});

/**
 * Hide popups when escape is pressed
 */
$(document).on('keyup',function(evt) {
	if (evt.keyCode == 27) {
    	if($("#buyPopUp").css("display") == "block"){
    		$('#buyPopUp').css("display","none");
		}
    	if($("#pwt-win").css("display") == "block"){
    		$('#pwt-win').css("display","none");
		}
    	if($("#error-popup").css("display") == "block"){
    		$('#error-popup').css("display","none");
		}
    }
});

/**
 * For create mid panel of POS
 */
function updateMidPanel() {
	 if(respData.responseCode==0){
    	 $("#iwMidPanel").html("");
    	 var iwMidPanelHtml = '';
    	 var i=0;
    		$(respData.games).each(function(){ 
    			iwMidPanelHtml+='<li class="scratchgames">';
    			iwMidPanelHtml+='<div class="instantgamesOuterWrap" menu-id="'+i+'">';
    			iwMidPanelHtml+='<div class="instantGameName">'+this.gameName+'</div>';
    			iwMidPanelHtml+='<a data-toggle="modal" data-target="#globalModal" class="gameDiv">';
    			iwMidPanelHtml+='<div class="instantgamesInnerWrap">';
    			iwMidPanelHtml+='<div class="instantgamesInnerWrap1">';
    			iwMidPanelHtml+='<div class="gameTypeIconTic"><img src='+this.gameLogoPath+'></div>';
    			iwMidPanelHtml+='</div>';
    			iwMidPanelHtml+='<div class="instantgamesInnerWrap2">';
    			iwMidPanelHtml+='<div class="instantAmount">';
    			iwMidPanelHtml+='<div class="ticketPrice"><span class="currency">'+currencySymbol+'</span>'+this.gamePrice+'</div>';
    			iwMidPanelHtml+='</div>';
    			iwMidPanelHtml+='</div>';						 
    			iwMidPanelHtml+='</div>';
    			iwMidPanelHtml+='</a>';
    			iwMidPanelHtml+='</div>';	
    			iwMidPanelHtml+='</li>';
    			i++;
			});
    		$("#iwMidPanel").append(iwMidPanelHtml);
    }else {
		alert(respData.responseMsg);
		return false;
	}
}


/**
 * For Sale
 */
function doPurchase(){
	purchaseReqData = JSON.stringify({
			"domainName": "lms",
			"gameNumber": respData.games[gameIndex].gameNumber,
			"currencyCode": currencySymbol,
			"merchantKey": 2,
			"playerId": 0,
			"clientType": "print",
			"merchantSessionId": $('#sessionId').val(),
			"lang": "eng",
			"screenSize": "424x326",
			"userName"    : $('#userName').val(),
			"deviceType": "PCPOS",
			"appType": "retapp",
			"userAgent": navigator.userAgent
	});
	
	curTrx = "TestPrint";
	var testPrint = {
            "mode": curTrx,
            "serviceName":"IW"
	};
	printData = JSON.stringify(testPrint);
//	status= "TRUE";
	if("YES" == printUsingApplet){
		setAppletData(printData);
	}else{
		setAppData(printData);
	}
}

function processPurchase(){
	console.log("processPurchase");
	if("YES" == printUsingApplet){
		status = "TRUE";
	}
	if(status == "TRUE"){
		var requestParam = path+'/com/skilrock/lms/web/instantWin/playMgmt/action/purchaseIWPcPosTicket.action';
		var purchaseResponseData=_ajaxCallJson(requestParam,"requestData="+purchaseReqData);
		if(purchaseResponseData.responseCode==0){
			curTrx="Buy";
			 printData = JSON.stringify({
				  "gameLogo": respData.games[gameIndex].gameLogo,
				  "symbolLogo":respData.games[gameIndex].symbolLogo,
				  "prizeLogo": respData.games[gameIndex].prizeLogo,
				  "gameTheme": respData.games[gameIndex].gameTheme,
				  "gameName":respData.games[gameIndex].gameName,
				  "gameDesc":respData.games[gameIndex].gameDescription,
				  "legendInfo":respData.games[gameIndex].legendInfo==null?"":respData.games[gameIndex].legendInfo,
				  "txnId": purchaseResponseData.txnId,
				  "ticketNbr": purchaseResponseData.ticketNbr,
				  "playData": purchaseResponseData.playData,
				  "purchaseDate":purchaseResponseData.purchaseTime.split(" ")[0],
				  "purchaseTime":purchaseResponseData.purchaseTime.split(" ")[1],
				  "priceAmt":respData.games[gameIndex].gamePrice,
				  "prizeAmt":purchaseResponseData.prizeAmnt,
				  "currencySymbol":currencySymbol,
				  "orgName":organizationName,
				  "retailerName": $('#userName').val(),
				  "bottomAdvMsg": purchaseResponseData.bottomAdvMsg==null?"":purchaseResponseData.bottomAdvMsg,
				  "topAdvMsg": purchaseResponseData.topAdvMsg==null?"":purchaseResponseData.topAdvMsg,
				  "serviceName":"IW",
				  "mode":curTrx,
				  "gameLogoPath":respData.games[gameIndex].gameLogoPath,
				  "parentOrgName":$('#parentOrgName').val()
			});			
			if("YES" == printUsingApplet){
				setAppletData(printData);
	    	}else{
	    		setAppData(printData);
	    	}
			$("#buyPopUp").css("display","none");
		}else{
			$("#buyPopUp").css("display","none");
			$("#error-popup").css('display','block');	
	 		$("#error").html(purchaseResponseData.responseMsg);
	 		$('#error-popup').delay(2000).fadeOut('slow');
		}
	}
}

/**
 * For PWT
 */
$(document).on('click',"#pwtOk",function(){
	keyupFiredCount=0;
	var payPwtResp;
	var pwtVerifyResp;
	var totalWinAmt = 0;
	var tktNumber = $("#pwtTicket").val();
	if(!isNaN(tktNumber)){
		if(tktNumber.length < 1){
			$("#error-message").html("Please Enter Ticket Number !!!");
			return false;
		}
		if(tktNumber.length == 12 || tktNumber.length == 14){
			var verifyTicketData = JSON.stringify({
				"domainName": "lms",
				"currencyCode": currencySymbol,
				"merchantKey": 2,
				"playerId": 0,
				"clientType": "print",
				"merchantSessionId": $('#sessionId').val(),
				"lang": "eng",
				"screenSize": "424x326",
				"userName": $('#userName').val(),
				"deviceType": "PCPOS",
			     "appType": "retapp",
				"ticketNbr":tktNumber,
				"userAgent": navigator.userAgent

			});
			 var requestParam = path+'/com/skilrock/lms/web/instantWin/playMgmt/action/verifyIWTicket.action';
			 pwtVerifyResp=_ajaxCallJson(requestParam,"requestData="+verifyTicketData);
			 if(pwtVerifyResp.responseCode==0){
				 totalWinAmt=pwtVerifyResp.winningAmt;
				 isPay = confirm("Winning is " + totalWinAmt + "\n Do you want to pay ?");
				 if (isPay) {
					 tktNbr=tktNumber;
					    curTrx = "TestPrintPwt";
						var testPrint = {
					            "mode": "TestPrint",
					            "serviceName":"IW"
						};
						printData = JSON.stringify(testPrint);
						status="";
						if("YES" == printUsingApplet){
							setAppletData(printData);
				    	}else{
				    		setAppData(printData);
				    	}
				 } else {
				    	$('#pwt-win').css('display','none');
				        return false;
				    }			 
			 }else{
				 	$('#pwt-win').css('display','none');
					$("#error-popup").css('display','block');	
			 		$("#error").html(pwtVerifyResp.responseMsg);
			 		$('#error-popup').delay(2000).fadeOut('slow');
			 }
		}else{
			$('#pwt-win').css('display','none');
			$("#error-popup").css('display','block');	
	 		$("#error").html(" Invalid Ticket Number !!!");
	 		$('#error-popup').delay(2000).fadeOut('slow');
			return false;
		}
	}else{
		$('#pwt-win').css('display','none');
		$("#error-popup").css('display','block');	
 		$("#error").html(" Invalid Ticket Number !!!");
 		$('#error-popup').delay(2000).fadeOut('slow');
		return false;
	}
});

/**
 * For PWT
 */
function payPwt() {
	if("YES" == printUsingApplet){
		status = "TRUE";
	}
	if(status=="TRUE"){
		var payPwtRequestData = JSON.stringify({		
			"domainName": "lms",
			"currencyCode": currencySymbol,
			"merchantKey": 2,
			"playerId": 0,
			"clientType": "print",
			"merchantSessionId": $('#sessionId').val(),
			"lang": "eng",
			"screenSize": "424x326",
			"userName": $('#userName').val(),
			"deviceType": "PCPOS",
		     "appType": "retapp",
			"ticketNbr":tktNbr,
			"userAgent": navigator.userAgent

		});
		var requestParam = path+'/com/skilrock/lms/web/instantWin/playMgmt/action/payIWPwt.action';
		var payPwtResponseData=_ajaxCallJson(requestParam,"requestData="+payPwtRequestData);
		if(payPwtResponseData.responseCode==0){
			curTrx="PWT";
			 printData = JSON.stringify({
				  "gameName":payPwtResponseData.gameName,
				  "ticketNbr": payPwtResponseData.ticketNbr,
				  "winAmt":payPwtResponseData.prizeAmnt,
				  "currencySymbol":currencySymbol,
				  "orgName":organizationName,
				  "serviceName":"IW",
				  "mode":curTrx,
				  "claimTime":payPwtResponseData.claimTime,
				  "responseMsg":payPwtResponseData.responseMsg,
				  "parentOrgName":$('#parentOrgName').val()
			});
			if("YES" == printUsingApplet){
				setAppletData(printData);
	    	}else{
	    		setAppData(printData);
	    	}
			$('#pwt-win').css('display','none');
		}else{
			$('#pwt-win').css('display','none');
			$("#error-popup").css('display','block');	
	 		$("#error").html(payPwtResponseData.responseMsg);
	 		$('#error-popup').delay(2000).fadeOut('slow');
		}
	}
}

//prepare sale ticket
function prepareSaleTicket(obj){
	$("#ticketDiv").html("");
	var dispTkt = "";
	var i=0;
	dispTkt+="<div class='ticket-format'>";	
	//top adv message
	if(obj.topAdvMsg!=null){	
		while(obj.topAdvMsg[i]!=null){
			dispTkt+="<div class='dg-dg'><span>"+obj.topAdvMsg[i++]+"</span></div>";
		}
	}
	dispTkt+='<div class="tkt-logo border-top">';
		//obj Name
	
		if(obj.parentOrgName.includes("Africa Bet") > 0){
			dispTkt+="<div class='logo-rel'><img src='"+path+"/com/skilrock/lms/web/drawGames/common/pcposImg/africaBet_black.png' width='140' height='67' /></div>";
		}else{
			dispTkt+="<div class='logo-rel'><img src='"+path+"/com/skilrock/lms/web/drawGames/common/pcposImg/africa_loto.png' width='140' height='67' /></div>";			
		}
	
		//game Name or logo
		if(obj.gameLogo=='T'){
			dispTkt+="<span class='tkt-gm_nm'>"+obj.gameName+"</span>";
		}else{
			dispTkt+="<span class='tkt-gm_nm'><img src="+obj.gameLogoPath+" alt='' width='110'></span>";
		}
	dispTkt+="</div>";
	
	//purchase date and tkt no info
	dispTkt+="<div class='tkt-dt-m'>";
		dispTkt+="<div class='tkt-rw'>";
			dispTkt+="<span class='no-of-tkt left'>"+obj.purchaseDate+"</span>";
			dispTkt+="<span class='no-of-tkt'>"+obj.purchaseTime+"</span>";
		dispTkt+="</div>";
		dispTkt+="<div class='tkt-rw'>";
			dispTkt+="<span class='no-o-tkt'>Ticket No. :</span>";
			dispTkt+="<span class='no-of-tkt'>"+obj.ticketNbr+"</span>";
		dispTkt+="</div>";
	dispTkt+="</div>";
	
	//symbol logo
	dispTkt+=prepareMiddleSectionThemeWise(obj);
	
	//game desc
	dispTkt+="<div class='text-p border-top'>";
		dispTkt+="<p>"+obj.gameDesc+"</p>";
	dispTkt+="</div>";
	
	
	dispTkt+='<div class="tkt-dt-m ">';
	
		if($.inArray(obj.gameTheme, legendInfoNotReqTheme)==-1){
			//legend info
			
			var symbolData=obj.legendInfo.split("#");
			 i = 0;
			while(symbolData[i]!=null){
				dispTkt+='<div class="tkt-rw">';
					dispTkt+="<span class='no-o-tkt text-upar'><pre>"+symbolData[i++]+"</pre></span>";
					//dispTkt+='<span class="no-of-tkt">1000</span>';
				dispTkt+='</div>';
			}
			
		}	
		
		//prize and price info
		if(obj.gameTheme=='MSS'){
			dispTkt+='<div class="tkt-rw tlam nop">';
				dispTkt+="<span class='no-o-tkt'>Prize ("+obj.currencySymbol+"):</span>";
				dispTkt+="<span class='no-of-tkt'>"+parseFloat(obj.prizeAmt)+"</span>";
			dispTkt+='</div>';
		
			dispTkt+='<div class="tkt-rw">';
				dispTkt+="<span class='no-o-tkt'>Price ("+obj.currencySymbol+"):</span>";
				dispTkt+="<span class='no-of-tkt'>"+parseFloat(obj.priceAmt)+"</span>";
			dispTkt+='</div>';
		}else{
			dispTkt+='<div class="tkt-rw tlam">';
				dispTkt+="<span class='no-o-tkt'>Price ("+obj.currencySymbol+"):</span>";
				dispTkt+="<span class='no-of-tkt'>"+parseFloat(obj.priceAmt)+"</span>";
			dispTkt+='</div>';
		}		
	dispTkt+='</div>';
	
	
	dispTkt+='<div class="code-img">';
		dispTkt+="<img src='"+path+"/LMSImages/images/barcode.png' class='code_img' />";	
		dispTkt+="<span class='code-no'>"+obj.ticketNbr+"</span>";	
	dispTkt+='</div>';
	dispTkt+="<div class='bottom-username border-top'>";
		dispTkt+="<span>"+obj.retailerName+"</span>";
	dispTkt+='</div>';
	
	//bottom adv message
	i=0;
	if(obj.bottomAdvMsg!=null){	
		while(obj.bottomAdvMsg[i]!=null){
			dispTkt+="<div class='dg-dg'><span>"+obj.bottomAdvMsg[i++]+"</span></div>";
		}
	}
	dispTkt+='</div>';
	$("#ticketDiv").html(dispTkt); 
}


//prepare mid panel ofsale ticket
function prepareMiddleSectionThemeWise(obj){
	var dispTkt="";
	var i=0;
	if($.inArray(obj.gameTheme, leftRightMidTheme)>=0){
		var symbolData=obj.playData.gameDataString.split(":");
		console.log(symbolData);
		if(obj.symbolLogo=='T'){
			dispTkt+="<div class='tkt-rw border-top'>";
			while(symbolData[i]!=null){
				
				if(symbolData.length%2!=0 && i==symbolData.length-1){
					dispTkt+="<div class='list-of-gm center-align'>";
						dispTkt+="<div class='res-icon-odd-no'><span>"+symbolData[i++]+"</span></div>";
					dispTkt+="</div>";
				}else{
					dispTkt+="<div class='list-of-gm center-align'>";
						dispTkt+="<div class='res-icon-gm-l-new'><span>"+symbolData[i++]+"</span></div>";
						dispTkt+="<div class='res-icon-gm-l-new'>"+(symbolData[i]==undefined?"":"<span>"+symbolData[i++]+"</span>")+"</div>";
					dispTkt+="</div>";
				}
				
			}
			dispTkt+="</div>";
		}else{
			dispTkt+="<div class='tkt-rw border-top'>";
			while(symbolData[i]!=null){
				dispTkt+="<div class='list-of-gm center-align'>";
					dispTkt+="<div class='res-icon-gm-l-ne'><img src="+obj.playData.baseImagePath+symbolData[i++]+"></div>";
					dispTkt+="<div class='res-icon-gm-l-ne'>"+(symbolData[i]==undefined?"":("<img src="+obj.playData.baseImagePath+symbolData[i++]+" >"))+"</div>";
				dispTkt+="</div>";
			}
			dispTkt+="</div>";
		}
	}else if(obj.gameTheme=="HR"){
		dispTkt+="<div class='tkt-rw border-top'>";
			dispTkt+="<div class='vrt-gm'>";
				dispTkt+="<div class='vrt-hd'>Horse</div>";
				dispTkt+="<div class='vrt-hd'>Rank</div>";
			dispTkt+="</div>";
			var symbolData=obj.playData.gameDataString.split("#");
			while(symbolData[i]!=null){
				dispTkt+="<div class='vrt-gm-nnm'>";
					var symbol=symbolData[i++].split(":");
					dispTkt+="<div class='vrt-list'>"+symbol[0]+"</div>";
					dispTkt+="<div class='vrt-list-item-vr'>"+symbol[1]+"</div>";
				dispTkt+="</div>";
			}
			
		dispTkt+="</div>";
	}else if(obj.gameTheme=="WNYN"){
		var winNumber=obj.playData.gameDataString.split("##")[0].split("~")[1].split(":").join(",");
		dispTkt+="<div class='tkt-rw border-top center-align'>";
			dispTkt+="<div class='nm-wn'>Win Numbers</div>";
			dispTkt+=" <ul class='call-list'>";
			var winNo=winNumber.split(",");
			$.each(winNo, function( index, value ) {
				dispTkt+="<li>"+value+"</li>";
			});
			dispTkt+="</ul>";
		dispTkt+="</div>";
		
		dispTkt+="<div class='tkt-rw border-top'>";
			dispTkt+="<div class='vrt-gm'>";
				dispTkt+="<div class='vrt-hd'>Your Number</div>";
				dispTkt+=" <div class='vrt-hd'>Prize ("+currencySymbol+")</div>";
			dispTkt+="</div>";
			var yourNumber=obj.playData.gameDataString.split("##")[1].split("~")[1].split("#");
			while(yourNumber[i]!=null){
				dispTkt+="<div class='vrt-gm-nnm'>";
				var symbol=yourNumber[i++].split(":");
					dispTkt+="<div class='vrt-list'>"+symbol[0]+"</div>";
					dispTkt+="<div class='vrt-list-item'>"+symbol[1]+"</div>";
				dispTkt+="</div>";
			}
			
		dispTkt+="</div>";
	}else if(obj.gameTheme=='YNHDN'){
		dispTkt+="<div class='tkt-rw border-top'>";
			dispTkt+="<div class='nm-wn'><div class='yr-no-tk'>Your Number</div><div class='yr-no-tk1'>Dealer Number</div><div class='yr-no-tk2'>Prize ("+obj.currencySymbol+")</div></div>";
		dispTkt+="</div>";
		
		
		dispTkt+="<div class='tkt-rw border-top'>";
			var symbolData=obj.playData.gameDataString.split("#");
			while(symbolData[i]!=null){
				dispTkt+="<div class='vrt-gm-nnm'>";
					var symbol=symbolData[i++].split(":");
					dispTkt+="<div class='step-col'>#"+i+"</div>";
					dispTkt+="<div class='step-col1'>"+symbol[0]+"</div>";
					dispTkt+="<div class='step-col2'>"+symbol[1]+"</div>";
					dispTkt+="<div class='step-col3'>"+symbol[2]+"</div>";
				dispTkt+="</div>";
			}
				
		dispTkt+="</div>";
		
		
		
	}else if(obj.gameTheme=="BINGO"){
		dispTkt+="<div class='tkt-rw border-top'>";
			dispTkt+="<div class='call-no'>";
				dispTkt+="<span>Called Numbers</span>";
				dispTkt+="<ul class='call-list'>";
				var calledNo=obj.playData.gameDataString.split("#")[0].split("~")[1].split(":");
				$.each(calledNo, function( index, value ) {
					dispTkt+="<li>"+value+"</li>";
				});
				dispTkt+="</ul>";
			dispTkt+="</div>";
		dispTkt+="</div>";
		
		dispTkt+="<div class='tkt-rw border-top'>";
			dispTkt+="<div class='call-no'>";
				dispTkt+="<span>Your Bingo Card</span>";
				dispTkt+=" <ul class='bingo-list'>";
				var yourNo=obj.playData.gameDataString.split("#")[1].split("~")[1].split(":");
				$.each(yourNo, function( index, value ) {
					dispTkt+="<li>"+value+"</li>";
				});
				dispTkt+="</ul>";
			dispTkt+="</div>";
		dispTkt+="</div>";
	}	
	return dispTkt;
}


//prepare pwt ticket
function preparePwtTicket(obj){
	$("#ticketDiv").html("");
	var dispTkt = "";

	dispTkt+='<div class="ticket-format">';
		dispTkt+='<div class="tkt-logo border-top">';
			//obj Name
		if(obj.parentOrgName.includes("Africa Bet") > 0){
			dispTkt+="<div class='logo-rel'><img src='"+path+"/com/skilrock/lms/web/drawGames/common/pcposImg/africaBet_black.png' width='140' height='67' /></div>";
		}else{
			dispTkt+="<div class='logo-rel'><img src='"+path+"/com/skilrock/lms/web/drawGames/common/pcposImg/africa_loto.png' width='140' height='67' /></div>";			
		}
		//	dispTkt+="<div class='logo-rel'><img src='"+path+"/com/skilrock/lms/web/drawGames/common/pcposImg/organisationLogo.png' width='140' height='67' /></div>";
			dispTkt+='<span class="win-ticket">Winning Verification</span>';
			dispTkt+="<span class='tkt-gm_nm'>"+obj.gameName+"</span>";
		dispTkt+='</div>';
    
		dispTkt+='<div class="tkt-dt-m">';
			dispTkt+='<div class="tkt-rw">';
				dispTkt+='<span class="no-o-tkt">Ticket No. :</span>';
				dispTkt+="<span class='no-of-tkt'>"+obj.ticketNbr+"</span>";
			dispTkt+='</div>';
		dispTkt+='</div>';
	
		dispTkt+='<div class="tkt-rw border-top">';
			dispTkt+='<div class="tkt-rw">';
				dispTkt+='<span class="no-o-tkt">Date</span>';
				dispTkt+='<span class="no-of-tkt">Time</span>';
			dispTkt+='</div>';
			dispTkt+='<div class="tkt-rw">';
				dispTkt+="<span class='no-o-tkt'>"+obj.claimTime.split(" ")[0]+"</span>";
				dispTkt+="<span class='no-of-tkt'>"+obj.claimTime.split(" ")[1]+"</span>";
			dispTkt+='</div>';
		dispTkt+='</div>';
		dispTkt+='<div class="text-p border-top">';
				dispTkt+='<p>Winning Succesfully Credited</p>';
		dispTkt+=' </div>';
    
		dispTkt+='<div class="tkt-dt-m ">';
			dispTkt+='<div class="tkt-rw tlam">';
				dispTkt+="<span class='no-o-tkt'>Total Pay("+currencySymbol+")</span>";
				dispTkt+="<span class='no-of-tkt'>"+obj.winAmt+"</span>";
			dispTkt+='</div>';
		dispTkt+='</div>';
	dispTkt+='</div>';
	$("#ticketDiv").html(dispTkt); 
}

function setAppData(data) {
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
				alert("Cannot communicate to printing application.\nPlease check your NodeJS server is running and try again.\nTo start NodeJs server either restart your system or\n follow the steps below:\n" +
						"1.) Open My Computer. \n2.) Goto C:/Program Files (x86)/skilrock/dgpcpos (For 64-bit system) and\n" +
						"     C:/Program Files (x86)/skilrock/dgpcpos (For 32-bit system).\n3.) Double click on runNodeInvisible to start NodeJs server.");
				parent.frames[0].updateBalance(path + "/com/skilrock/lms/web/loginMgmt/updateAvlCreditAmt.action");
			} else if("PWT" == curTrx){
				alert("Cannot communicate to printing application.\nPlease check your NodeJS server is running and try again.\nTo start NodeJs server either restart your system or\n follow the steps below:\n" +
						"1.) Open My Computer. \n2.) Goto C:/Program Files (x86)/skilrock/dgpcpos (For 64-bit system) and\n" +
						"     C:/Program Files (x86)/skilrock/dgpcpos (For 32-bit system).\n3.) Double click on runNodeInvisible to start NodeJs server.\n\nTicket has been claimed!!");
				parent.frames[0].updateBalance(path + "/com/skilrock/lms/web/loginMgmt/updateAvlCreditAmt.action");
			} else if("TestPrint" == curTrx){
				alert("Cannot communicate to printing application.\nPlease check your NodeJS server is running and try again.\nTo start NodeJs server either restart your system or\n follow the steps below:\n" +
						"1.) Open My Computer. \n2.) Goto C:/Program Files (x86)/skilrock/dgpcpos (For 64-bit system) and\n" +
						"     C:/Program Files (x86)/skilrock/dgpcpos (For 32-bit system).\n3.) Double click on runNodeInvisible to start NodeJs server.");
			}
		};
		connection.onmessage = function (evt) {
	    	var dataReceived = $.parseJSON(evt.data);
	    	var printStatus = dataReceived.utf8Data;
	    	if("SUCCESS"!=printStatus && "FAIL"!=printStatus){
	    		printStatus = "APP_NOT_FOUND";
	    	} 
	        connection.close();
		    if(connSuccess){
		        printTicket(printStatus);
			}
	    };
}

function printTicket(printStatus){
	if("SUCCESS" == printStatus){
		$("#buy").prop('disabled',true);
		parent.frames[0].updateBalance(path + "/com/skilrock/lms/web/loginMgmt/updateAvlCreditAmt.action");
		if("Buy" == curTrx){
			prepareSaleTicket(JSON.parse(printData));
		}else if("PWT" == curTrx){
			preparePwtTicket(JSON.parse(printData));
		}else if("TestPrint" == curTrx){
			status = "TRUE";
			processPurchase();
		}else if("TestPrintPwt" == curTrx){
			status = "TRUE";
			payPwt();
		}
	} else if("FAIL" == printStatus){
		status = "FALSE";
		if($("#buyPopUp").css("display") == "block"){
    		$('#buyPopUp').css("display","none");
		}
    	if($("#pwt-win").css("display") == "block"){
    		$('#pwt-win').css("display","none");
		}
		alert("Printer not connected!!");
	} else if("APP_NOT_FOUND" == printStatus){
		if("Buy" == curTrx){
			alert("Cannot communicate to printing application.\nPlease check your Printing Application is running and try again.\nTo start Printing Application either restart your system or\n follow the steps below:\n" +
					"1.) Open My Computer. \n2.) Goto C:/Program Files (x86)/skilrock/dgpcpos (For 64-bit system) and\n" +
					"     C:/Program Files (x86)/skilrock/dgpcpos (For 32-bit system).\n3.) Double click on PrintingApplication to start printing application.\n\nLast ticket has been cancelled!!");
			$("#buy").prop('disabled',true);
		} else if("PWT" == curTrx){
			alert("Cannot communicate to printing application.\nPlease check your Printing Application is running and try again.\nTo start Printing Application either restart your system or\n follow the steps below:\n" +
					"1.) Open My Computer. \n2.) Goto C:/Program Files (x86)/skilrock/dgpcpos (For 64-bit system) and\n" +
					"     C:/Program Files (x86)/skilrock/dgpcpos (For 32-bit system).\n3.) Double click on PrintingApplication to start printing application.\n\nTicket has been claimed!!");
			preparePwtTicket(JSON.parse(printData));
		}
	}
}

function setAppletData(buyData) {
	$("#regDivIW").innerHTML = "";
	$("#regButtonIW").innerHTML = "";
	// From Here We are calling applet pcpos jar
	document.applets[0].showStatus(buyData);
	document.applets[0].repaint();
}

//Here We are getting response back from above call
function checkAndShowTicketForApplet(tktNum, totAmt, printStatus){
	console.log('*****'+tktNum+'*************'+totAmt+'***********'+printStatus+'*********');
	if("SUCCESS" == printStatus){
		$("#buy").prop('disabled',true);
		parent.frames[0].updateBalance(path + "/com/skilrock/lms/web/loginMgmt/updateAvlCreditAmt.action");
		if("TestPrintPwt" == curTrx || "TestPrintPwt" == curTrx){
			$("#parentAppletIW").css("display", "none");
		}else{		
			$("#parentAppletIW").css("display", "block");
		}		
		if("TestPrint" == curTrx){
			setTimeout(processPurchase, 500);
		}else if("TestPrintPwt" == curTrx){
			setTimeout(payPwt, 500);
		}
	} else if("FAIL" == printStatus){
		status = "FALSE";
		if("Buy" == curTrx){
		   $("#parentAppletIW").css("display", "none");
		}else if("PWT" == curTrx){
		   $("#parentAppletIW").css("display", "none");
		}
		
		if($("#buyPopUp").css("display") == "block"){
    		$('#buyPopUp').css("display","none");
		}
    	if($("#pwt-win").css("display") == "block"){
    		$('#pwt-win').css("display","none");
		}
    	$("#parentApplet").css("display","none");
		alert("Printer not connected!!");
	}
}