var miniRouletteBetTypes ={
		"allOddNumbers" : {"numbers":[01,03,05,07,09,11]},
		"allEvenNumbers" : {"numbers":[02,04,06,08,10,12]},
		"redNumbers" : {"numbers":[01,03,05,07,09,11]},
		"blackNumbers" : {"numbers":[02,04,06,08,10,12]},
		"firstColumn" : {"numbers":[01,02,03]},
		"secondColumn" : {"numbers":[04,05,06]},
		"thirdColumn" : {"numbers":[07,08,09]},
		"fourthColumn" : {"numbers":[10,11,12]},
		"firstRow" : {"numbers":[03,06,09,12]},
		"secondRow" : {"numbers":[02,05,08,11]},
		"thirdRow" : {"numbers":[01,04,07,10]},
		"firstQuarter" : {"numbers":[02,03,05,06]},
		"secondQuarter" :{"numbers": [01,02,04,05]},
		"thirdQuarter" : {"numbers":[05,06,08,09]},
		"fourthQuarter" : {"numbers":[04,05,07,08]},
		"fifthQuarter" : {"numbers":[07,08,10,11]},
		"sixthQuarter" : {"numbers":[08,09,11,12]},
		"zero" : {"numbers":[0]},
		"oneToSix" : {"numbers":[01,02,03,04,05,06]},
		"fourToNine" : {"numbers":[04,05,06,07,08,09]},
		"sevenToTwelve" : {"numbers":[07,08,09,10,11,12]},
		"roulette":{}
};
var miniRouletteBetAmt ={
		"allOddNumbers" : {"betAmt": 0},
		"allEvenNumbers" : {"betAmt": 0},
		"redNumbers" : {"betAmt": 0},
		"blackNumbers" : {"betAmt": 0},
		"firstColumn" : {"betAmt": 0},
		"secondColumn" : {"betAmt": 0},
		"thirdColumn" : {"betAmt": 0},
		"fourthColumn" : {"betAmt": 0},
		"firstRow" : {"betAmt": 0},
		"secondRow" : {"betAmt": 0},
		"thirdRow" : {"betAmt": 0},
		"firstQuarter" : {"betAmt": 0},
		"secondQuarter" : {"betAmt": 0},
		"thirdQuarter" : {"betAmt": 0},
		"fourthQuarter" : {"betAmt": 0},
		"fifthQuarter" : {"betAmt": 0},
		"sixthQuarter" : {"betAmt": 0},
		"zero" : {"betAmt": 0},
		"oneToSix" : {"betAmt": 0},
		"fourToNine" : {"betAmt": 0},
		"sevenToTwelve" : {"betAmt": 0},
		"roulette":{"betAmt":[0,0,0,0,0,0,0,0,0,0,0,0,0]}
};
var numbers = [0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0];
var betTypeCount = 0;
var betCoinAmount;
$(document).ready(function(){
	
	
	var  customcur = '';
	 $(".clearCursor,.tenCursor,.twentyCursor,.fiftyCursor,.hundredCursor,.FiveHundredCursor").click(function(){
		if($(this).attr('id') == customcur){
			$('.minirou-left-rightWrap').removeClass(customcur);
			customcur = ''; 
		}else{	
			$('.minirou-left-rightWrap').removeClass(customcur);
			$('.minirou-left-rightWrap').addClass($(this).attr('id'));
			customcur = $(this).attr('id');
		}			
	});
	
	$(".minirou-left-rightWrap").click(function() {
		$('.minirou-left-rightWrap').removeClass(customcur);
	});
	
	$(".subPlay").click(function(e) {
		e.stopPropagation();
	});
	
	$(".rouPlayEle.parent").mouseenter(function(){
		 var str = $(this).attr('id');
		 var sub_var = "." + str.replace("parent", "sub");
		 $(sub_var).addClass('selectedCell');
	});
	$(".rouPlayEle.parent").mouseleave(function(){
		 var str = $(this).attr('id');
		 var sub_var = "." + str.replace("parent_", "sub_");
		 $(sub_var).removeClass('selectedCell');
	});	
	
	/**
	 * When betcoins are clicked
	 */
	$(".betCoins").on("click",function(){
		$("#tktView").css("display","none");
	    $("#gamePrizeScheme").css("display","block");
		$(".betCoins").removeClass("selected");
		$("#error").html("");
		$("#error-popup").css('display','none');
		betCoinAmount = parseFloat($(this).attr("betCoinAmt"));
		$(this).addClass("selected");
		calculateBetAmt();
		isBetTypeSelected();
		checkIsDrawSelected();
		updateTicketPriceForMiniRoulette();
	});
	
	/**
	 * When mini roulette bet types or numbers are selected
	 */
	$(".miniRouletteBetType").on("click",function(){
		$("#tktView").css("display","none");
	    $("#gamePrizeScheme").css("display","block");
		var rouletteBettTypeSel = $(this).attr("rouletteBettype");
		if(betCoinAmount != 0){
			$(this).addClass("betTypeActive");
			if("roulette" != rouletteBettTypeSel){
				$("#rouletteAmt"+rouletteBettTypeSel).parent().addClass("active");
				miniRouletteBetAmt[rouletteBettTypeSel].betAmt = parseFloat(miniRouletteBetAmt[rouletteBettTypeSel].betAmt) + parseFloat(betCoinAmount);
				//betAmtSelected = betAmtSelected + miniRouletteBetAmt[rouletteBettTypeSel].betAmt;
				$("#rouletteAmt"+rouletteBettTypeSel).html(miniRouletteBetAmt[rouletteBettTypeSel].betAmt);
				getBetOnEachNumber(rouletteBettTypeSel,0,"add");
			} else{
				var selNum = parseInt($(this).children().first().text());
				miniRouletteBetAmt[rouletteBettTypeSel].betAmt[selNum] = parseFloat(miniRouletteBetAmt[rouletteBettTypeSel].betAmt[selNum]) + parseFloat(betCoinAmount);
				//betAmtSelected = betAmtSelected + miniRouletteBetAmt[rouletteBettTypeSel].betAmt[selNum];
				$("#rouletteAmt"+selNum).html(miniRouletteBetAmt[rouletteBettTypeSel].betAmt[selNum]);
				$("#rouletteAmt"+selNum).parent().addClass("active");
				getBetOnEachNumber(rouletteBettTypeSel,selNum,"add");
			}
			$("#error").html("");
			$("#error-popup").css('display','none');
		}else if(betCoinAmount == 0){
			$(this).removeClass("betTypeActive");
			if("roulette" != rouletteBettTypeSel && miniRouletteBetAmt[rouletteBettTypeSel].betAmt!=0){
				$("#rouletteAmt"+rouletteBettTypeSel).parent().removeClass("active");
				getBetOnEachNumber(rouletteBettTypeSel,0,"remove");
				miniRouletteBetAmt[rouletteBettTypeSel].betAmt = 0;
				$("#rouletteAmt"+rouletteBettTypeSel).html("");
				$("#error").html("");
				$("#error-popup").css('display','none');
			}else if("roulette" == rouletteBettTypeSel && miniRouletteBetAmt[rouletteBettTypeSel].betAmt[parseInt($(this).children().first().text())]!=0){
				var selNum = parseInt($(this).children().first().text());
				$("#rouletteAmt"+selNum).parent().removeClass("active");
				getBetOnEachNumber(rouletteBettTypeSel,selNum,"remove");
				miniRouletteBetAmt[rouletteBettTypeSel].betAmt[selNum] = 0;
				$("#rouletteAmt"+selNum).html("");
				$("#error").html("");
				$("#error-popup").css('display','none');
			}else{
				$("#error-popup").css('display','block');		
				$("#error").html("Please select bet amount to proceed!");	
				$("#error-popup").delay(2000).fadeOut('slow');
			}
		}
		calculateBetAmt();
		isBetTypeSelected();
		checkIsDrawSelected();
		updateTicketPriceForMiniRoulette();
	});
	
});

function resetMiniRoulette(){
	$(".betCoins").removeClass("selected");
	betCoinAmount = $(".betCoins").eq(1).attr("betCoinAmt");
	$(".betCoins").eq(1).addClass("selected");
	for(var rouletteBetType in miniRouletteBetAmt){
		if("roulette" == rouletteBetType){
			for(var i=1;i<=12;i++){
				miniRouletteBetAmt[rouletteBetType].betAmt[i] = 0;
				$("#rouletteAmt"+i).parent().removeClass("active");
			}	
		}else{
			miniRouletteBetAmt[rouletteBetType].betAmt = 0;
			$("#rouletteAmt"+rouletteBetType).parent().removeClass("active");
		}
	}
	for(var p=0;p<=12;p++){
		numbers[p] = 0.0;
	}
	betAmtSelected = 0;
	isValid = false;
	$(".ad-draw-check-box").removeClass("select-checkbox");
	$(".ad-draw-check-box").children().prop("checked",false);
	$("#noOfAdvDraws").html("...");
	$("#numOfDrawsSelected").val("1");
	isDrawManual = true;
	noOfDrawsSelected = parseInt($("#numOfDrawsSelected").val());
	drawsSel = noOfDrawsSelected;
	$("#buy").prop("disabled",true);
	$("#ticketAmt").html("0");
	$("#noOfNumber").html("0");
	$("#noOfLines").html("0");
	$("#numPicked").html("");
	$("#numOfDrawsSelected").val("");
	$(".ad-draw-check-box").removeClass("select-checkbox");
	$("#error").html("");
	$("#error-popup").css('display','none');
	$(".betOnEachNum").html("0");
	updateTicketPriceForMiniRoulette();
}

function isBetTypeSelected(){
	betTypeCount = 0;
	$(".betTypeActive").each(function(){
		betTypeCount++;
	});
	if(betTypeCount != 0){
		isValid = true;
	}else{
		isValid = false;
		betAmtSelected = 0;
	}
}

function calculateBetAmt(){
	betAmtSelected = 0;
	for(var rouletteBetType in miniRouletteBetAmt){
		if("roulette" == rouletteBetType){
			for(var i=1;i<=12;i++){
				betAmtSelected = betAmtSelected + miniRouletteBetAmt[rouletteBetType].betAmt[i];
			}	
		}else{
			betAmtSelected = betAmtSelected + miniRouletteBetAmt[rouletteBetType].betAmt;
		}
	}
}

function updateTicketPriceForMiniRoulette(){
	var tktPrice = noOfDrawsSelected * betAmtSelected;
	$("#tktPrice").html(tktPrice.toFixed(2));
}

function getBetOnEachNumber(betType,selNum,action){
	if("add" == action){
		if("roulette"!=betType){
			var num = miniRouletteBetTypes[betType].numbers;
			var totalNum = num.length;
			var betOnEachNum = (parseFloat(betCoinAmount)/parseFloat(totalNum));
			$(num).each(function(){
				var curBetAmt = parseFloat(numbers[parseInt(this)]) + parseFloat(betOnEachNum);
				numbers[parseInt(this)] = curBetAmt;
				$("#mr"+parseInt(this)).html(curBetAmt.toFixed(1));
			});
		}else{
			var curBetAmt = parseFloat(numbers[selNum]) + parseFloat(betCoinAmount);
			numbers[parseInt(selNum)] = curBetAmt;
			$("#mr"+parseInt(selNum)).html(curBetAmt.toFixed(1));
		}
	} else if("remove" == action){
		if("roulette"!=betType){
			var betAmt = miniRouletteBetAmt[betType].betAmt;
			var num = miniRouletteBetTypes[betType].numbers;
			var totalNum = num.length;
			var betOnEachNum = (parseFloat(betAmt)/parseFloat(totalNum));
			$(num).each(function(){
				var curBetAmt = parseFloat(numbers[this]) - parseFloat(betOnEachNum);
				if(curBetAmt.toFixed(1) == -0.0 || curBetAmt.toFixed(1) == 0.0){
					curBetAmt = 0;
				}
				numbers[parseInt(this)] = curBetAmt;
				$("#mr"+parseInt(this)).html(curBetAmt.toFixed(1));
			});
		} else{
			var betAmt = parseFloat(miniRouletteBetAmt["roulette"].betAmt[selNum]);
			var curBetAmt = parseFloat(numbers[selNum]) - betAmt;
			if(curBetAmt.toFixed(1) == -0.0 || curBetAmt.toFixed(1) == 0.0){
				curBetAmt = 0;
			}
			numbers[parseInt(selNum)] = curBetAmt;
			$("#mr"+parseInt(selNum)).html(curBetAmt.toFixed(1));
		}
	}
}
