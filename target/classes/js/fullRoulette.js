var fullRouletteBetTypes ={
		"allOddNumbers" : {"numbers":[01,03,05,07,09,11,13,15,17,19,21,23,25,27,29,31,33,35]},
		"allEvenNumbers" : {"numbers":[02,04,06,08,10,12,14,16,18,20,22,24,26,28,30,32,34,36]},
		"redNumbers" : {"numbers":[01,03,05,07,09,11,13,15,17,19,21,23,25,27,29,31,33,35]},
		"blackNumbers" : {"numbers":[02,04,06,08,10,12,14,16,18,20,22,24,26,28,30,32,34,36]},
		"street" : {"numbers":[01,04,07,10,13,16,19,22,25,28,31,34]},
		"firstColumn" : {"numbers":[01,04,07,10,13,16,19,22,25,28,31,34]},
		"secondColumn" : {"numbers":[02,05,08,11,14,17,20,23,26,29,32,35]},
		"thirdColumn" : {"numbers":[03,06,09,12,15,18,21,24,27,30,33,36]},
		"firstRow" : {"numbers":[01,02,03]},
		"secondRow" : {"numbers":[04,05,06]},
		"thirdRow" : {"numbers":[07,08,09]},
		"fourthRow" : {"numbers":[10,11,12]},
		"fifthRow" : {"numbers":[13,14,15]},
		"sixthRow" : {"numbers":[16,17,18]},
		"seventhRow" : {"numbers":[19,20,21]},
		"eighthRow" : {"numbers":[22,23,24]},
		"ninthRow" : {"numbers":[25,26,27]},
		"tenthRow" : {"numbers":[28,29,30]},
		"eleventhRow" : {"numbers":[31,32,33]},
		"twelfthRow" : {"numbers":[34,35,36]},
		"firstDozen": {"numbers":[01,02,03,04,05,06,07,08,09,10,11,12]},
		"secondDozen": {"numbers":[13,14,15,16,17,18,19,20,21,22,23,24]},
		"thirdDozen": {"numbers":[25,26,27,28,29,30,31,32,33,34,35,36]},
		"firstHalf": {"numbers":[01,02,03,04,05,06,07,08,09,10,11,12,13,14,15,16,17,18]},
		"secondHalf": {"numbers":[ 19,20,21,22,23,24,25,26,27,28,29,30,31,32,33,34,35,36]},
		 "zero" : {"numbers":[0]},
		"roulette":{},
		 "firstQuarter" : {"numbers":[3,6,2,5]},
		 "secondQuarter" : {"numbers":[6,9,5,8]},
		 "thirdQuarter" : {"numbers":[9,12,8,11]},
		 "fourthQuarter" : {"numbers":[12,15,11,14]},
		 "fifthQuarter" : {"numbers":[15,18,14,17]},
		 "sixthQuarter" : {"numbers":[18,21,17,20]},
		 "seventhQuarter" : {"numbers":[21,24,20,23]},
		 "eighthQuarter" : {"numbers":[24,27,23,26]},
		 "ninthQuarter" : {"numbers":[27,30,26,29]},
		 "tenthQuarter" : {"numbers":[30,33,29,32]},
		 "eleventhQuarter" : {"numbers":[33,36,32,35]},
		 "twelfthQuarter" : {"numbers":[2,5,1,4]},
		 "thirteenthQuarter" : {"numbers":[5,8,4,7]},
		 "fourteenthQuarter" : {"numbers":[8,11,7,10]},
		 "fifteenthQuarter" : {"numbers":[11,14,10,13]},
		 "sixteenthQuarter" : {"numbers":[14,17,13,16]},
		 "seventeenthQuarter" : {"numbers":[17,20,16,19]},
		 "eighteenthQuarter" : {"numbers":[20,23,19,22]},
		 "ninteenthQuarter" : {"numbers":[23,26,22,25]},
		 "twentiethQuarter" : {"numbers":[26,29,25,28]},
		 "twentyFirstQuarter" : {"numbers":[29,32,28,31]},
		 "twentySecondQuarter" : {"numbers":[32,35,31,34]}
};
var fullRouletteBetAmt ={
		"allOddNumbers" : {"betAmt": 0},
		"allEvenNumbers" : {"betAmt": 0},
		"redNumbers" : {"betAmt": 0},
		"blackNumbers" : {"betAmt": 0},
		"street" : {"betAmt": 0},
		"firstColumn" : {"betAmt": 0},
		"secondColumn" : {"betAmt": 0},
		"thirdColumn" : {"betAmt": 0},
		"firstRow" : {"betAmt": 0},
		"secondRow" : {"betAmt": 0},
		"thirdRow" : {"betAmt": 0},
		"fourthRow" : {"betAmt": 0},
		"fifthRow" : {"betAmt": 0},
		"sixthRow" : {"betAmt": 0},
		"seventhRow" : {"betAmt": 0},
		"eighthRow" : {"betAmt": 0},
		"ninthRow" : {"betAmt": 0},
		"tenthRow" : {"betAmt": 0},
		"eleventhRow" : {"betAmt": 0},
		"twelfthRow" : {"betAmt": 0},
		"firstDozen": {"betAmt": 0},
		"secondDozen": {"betAmt": 0},
		"thirdDozen": {"betAmt": 0},
		"firstHalf": {"betAmt": 0},
		"secondHalf": {"betAmt": 0},
		 "zero" : {"betAmt": 0},
		"roulette":{"betAmt":[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0]},
		"firstQuarter" : {"betAmt": 0},
		 "secondQuarter" : {"betAmt": 0},
		 "thirdQuarter" : {"betAmt": 0},
		 "fourthQuarter" : {"betAmt": 0},
		 "fifthQuarter" : {"betAmt": 0},
		 "sixthQuarter" : {"betAmt": 0},
		 "seventhQuarter" : {"betAmt": 0},
		 "eighthQuarter" : {"betAmt": 0},
		 "ninthQuarter" : {"betAmt": 0},
		 "tenthQuarter" : {"betAmt": 0},
		 "eleventhQuarter" : {"betAmt": 0},
		 "twelfthQuarter" : {"betAmt": 0},
		 "thirteenthQuarter" : {"betAmt": 0},
		 "fourteenthQuarter" : {"betAmt": 0},
		 "fifteenthQuarter" : {"betAmt": 0},
		 "sixteenthQuarter" : {"betAmt": 0},
		 "seventeenthQuarter" : {"betAmt": 0},
		 "eighteenthQuarter" : {"betAmt": 0},
		 "ninteenthQuarter" : {"betAmt": 0},
		 "twentiethQuarter" : {"betAmt": 0},
		 "twentyFirstQuarter" : {"betAmt": 0},
		 "twentySecondQuarter" : {"betAmt": 0}
};
var numbers = [0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0];
var betTypeCount = 0;
var betCoinAmount;
$(document).ready(function(){
	
	var  customcur = '';
	 $(".clearCursor,.tenCursor,.twentyCursor,.fiftyCursor,.hundredCursor,.FiveHundredCursor").click(function(){
		if($(this).attr('id') == customcur)
		{
			$('.minirou-area').removeClass(customcur);
			customcur = ''; 
			
		}
		else{	
				$('.minirou-area').removeClass(customcur);
				$('.minirou-area').addClass($(this).attr('id'));
				customcur = $(this).attr('id'); 	
		}			
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
	 * When betCoinsFR are clicked
	 */
	$(".betCoinsFR").on("click",function(){
		$("#tktView").css("display","none");
	    $("#gamePrizeScheme").css("display","block");
		$(".betCoinsFR").removeClass("selected");
		$("#error").html("");
		$("#error-popup").css('display','none');
		betCoinAmount = parseFloat($(this).attr("betCoinAmt"));
		$(this).addClass("selected");
		calculateBetAmtFullRoulette();
		isBetTypeSelectedFullRoulette();
		checkIsDrawSelected();
		updateTicketPriceForFullRoulette();
	});
	
	/**
	 * When full roulette bet types or numbers are selected
	 */
	$(".fullRouletteBetType").on("click",function(){
		$("#tktView").css("display","none");
	    $("#gamePrizeScheme").css("display","block");
		var rouletteBettTypeSel = $(this).attr("fullRouletteBetType");
		if(betCoinAmount != 0){
			$(this).addClass("betTypeActive");
			if("roulette" != rouletteBettTypeSel){
				$("#fullRouletteAmt"+rouletteBettTypeSel).parent().addClass("active");
				fullRouletteBetAmt[rouletteBettTypeSel].betAmt = parseFloat(fullRouletteBetAmt[rouletteBettTypeSel].betAmt) + parseFloat(betCoinAmount);
				$("#fullRouletteAmt"+rouletteBettTypeSel).html(fullRouletteBetAmt[rouletteBettTypeSel].betAmt);
				getBetOnEachNumberFullRoullete(rouletteBettTypeSel,0,"add");
			} else{
				var selNum = parseInt($(this).children().first().text());
				fullRouletteBetAmt[rouletteBettTypeSel].betAmt[selNum] = parseFloat(fullRouletteBetAmt[rouletteBettTypeSel].betAmt[selNum]) + parseFloat(betCoinAmount);
				$("#fullRouletteAmt"+selNum).html(fullRouletteBetAmt[rouletteBettTypeSel].betAmt[selNum]);
				$("#fullRouletteAmt"+selNum).parent().addClass("active");
				getBetOnEachNumberFullRoullete(rouletteBettTypeSel,selNum,"add");
			}
			$("#error").html("");
			$("#error-popup").css('display','none');
		}else if(betCoinAmount == 0){
			$(this).removeClass("betTypeActive");
			if("roulette" != rouletteBettTypeSel && fullRouletteBetAmt[rouletteBettTypeSel].betAmt!=0){
				$("#fullRouletteAmt"+rouletteBettTypeSel).parent().removeClass("active");
				getBetOnEachNumberFullRoullete(rouletteBettTypeSel,0,"remove");
				fullRouletteBetAmt[rouletteBettTypeSel].betAmt = 0;
				$("#fullRouletteAmt"+rouletteBettTypeSel).html("");
				$("#error").html("");
				$("#error-popup").css('display','none');
			}else if("roulette" == rouletteBettTypeSel && fullRouletteBetAmt[rouletteBettTypeSel].betAmt[parseInt($(this).children().first().text())]!=0){
				var selNum = parseInt($(this).children().first().text());
				$("#fullRouletteAmt"+selNum).parent().removeClass("active");
				getBetOnEachNumberFullRoullete(rouletteBettTypeSel,selNum,"remove");
				fullRouletteBetAmt[rouletteBettTypeSel].betAmt[selNum] = 0;
				$("#fullRouletteAmt"+selNum).html("");
				$("#error").html("");
				$("#error-popup").css('display','none');
			}else{
				$("#error-popup").css('display','block');		
				$("#error").html("Please select bet amount to proceed!");	
				$("#error-popup").delay(2000).fadeOut('slow');
			}
		}
		calculateBetAmtFullRoulette();
		isBetTypeSelectedFullRoulette();
		checkIsDrawSelected();
		updateTicketPriceForFullRoulette();
	});
	
});

function resetFullRoulette(){
	$(".betCoinsFR").removeClass("selected");
	betCoinAmount = $(".betCoinsFR").eq(1).attr("betCoinAmt");
	$(".betCoinsFR").eq(1).addClass("selected");
	for(var rouletteBetType in fullRouletteBetAmt){
		if("roulette" == rouletteBetType){
			for(var i=1;i<=36;i++){
				fullRouletteBetAmt[rouletteBetType].betAmt[i] = 0;
				$("#fullRouletteAmt"+i).parent().removeClass("active");
			}	
		}else{
			fullRouletteBetAmt[rouletteBetType].betAmt = 0;
			$("#fullRouletteAmt"+rouletteBetType).parent().removeClass("active");
		}
	}
	for(var p=0;p<=36;p++){
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
	$("#fullRoulettePurchaseDetails").css("display","none");
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
	updateTicketPriceForFullRoulette();
}

function isBetTypeSelectedFullRoulette(){
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

function calculateBetAmtFullRoulette(){
	betAmtSelected = 0;
	for(var rouletteBetType in fullRouletteBetAmt){
		if("roulette" == rouletteBetType){
			for(var i=1;i<=36;i++){
				betAmtSelected = betAmtSelected + fullRouletteBetAmt[rouletteBetType].betAmt[i];
			}	
		}else{
			betAmtSelected = betAmtSelected + fullRouletteBetAmt[rouletteBetType].betAmt;
		}
	}
}

function updateTicketPriceForFullRoulette(){
	var tktPrice = noOfDrawsSelected * betAmtSelected;
	$("#tktPrice").html(tktPrice.toFixed(2));
}

function getBetOnEachNumberFullRoullete(betType,selNum,action){
	if("add" == action){
		if("roulette"!=betType){
			var num = fullRouletteBetTypes[betType].numbers;
			var totalNum = num.length;
			var betOnEachNum = (parseFloat(betCoinAmount)/parseFloat(totalNum));
			$(num).each(function(){
				var curBetAmt = parseFloat(numbers[parseInt(this)]) + parseFloat(betOnEachNum);
				numbers[parseInt(this)] = curBetAmt;
				$("#fr"+parseInt(this)).html(curBetAmt.toFixed(1));
			});
		}else{
			var curBetAmt = parseFloat(numbers[selNum]) + parseFloat(betCoinAmount);
			numbers[parseInt(selNum)] = curBetAmt;
			$("#fr"+parseInt(selNum)).html(curBetAmt.toFixed(1));
		}
	} else if("remove" == action){
		if("roulette"!=betType){
			var betAmt = fullRouletteBetAmt[betType].betAmt;
			var num = fullRouletteBetTypes[betType].numbers;
			var totalNum = num.length;
			var betOnEachNum = (parseFloat(betAmt)/parseFloat(totalNum));
			$(num).each(function(){
				var curBetAmt = parseFloat(numbers[this]) - parseFloat(betOnEachNum);
				if(curBetAmt.toFixed(1) == -0.0 || curBetAmt.toFixed(1) == 0.0){
					curBetAmt = 0;
				}
				numbers[parseInt(this)] = curBetAmt;
				$("#fr"+parseInt(this)).html(curBetAmt.toFixed(1));
			});
		} else{
			var betAmt = parseFloat(fullRouletteBetAmt["roulette"].betAmt[selNum]);
			var curBetAmt = parseFloat(numbers[selNum]) - betAmt;
			if(curBetAmt.toFixed(1) == -0.0 || curBetAmt.toFixed(1) == 0.0){
				curBetAmt = 0;
			}
			numbers[parseInt(selNum)] = curBetAmt;
			$("#fr"+parseInt(selNum)).html(curBetAmt.toFixed(1));
		}
	}
}
