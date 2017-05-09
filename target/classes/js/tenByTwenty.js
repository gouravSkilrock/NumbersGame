var gameLimitTBT = {
	"Direct10" : {
		"min" : 10,
		"max" : 10,
		"maxBetMul" : null
	},
	"First10" : {
		"min" : 10,
		"max" : 10,
		"maxBetMul" : null,
		"numbers" : [1,2,3,4,5,6,7,8,9,10]
	},
	"Last10" : {
		"min" : 10,
		"max" : 10,
		"maxBetMul" : null,
		"numbers" : [11,12,13,14,15,16,17,18,19,20]
	},
	"AllOdd" : {
		"min" : 10,
		"max" : 10,
		"maxBetMul" : null,
		"numbers" : [1,3,5,7,9,11,13,15,17,19]
	},
	"AllEven" : {
		"min" : 10,
		"max" : 10,
		"maxBetMul" : null,
		"numbers" : [2,4,6,8,10,12,14,16,18,20]
	},
	"OddEven" : {
		"min" : 10,
		"max" : 10,
		"maxBetMul" : null,
		"numbers" : [1,3,5,7,9,12,14,16,18,20]
	},
	"EvenOdd" : {
		"min" : 10,
		"max" : 10,
		"maxBetMul" : null,
		"numbers" : [2,4,6,8,10,11,13,15,17,19]
	},
	"JumpEvenOdd" : {
		"min" : 10,
		"max" : 10,
		"maxBetMul" : null,
		"numbers" : [3,4,7,8,11,12,15,16,19,20]
	},
	"JumpOddEven" : {
		"min" : 10,
		"max" : 10,
		"maxBetMul" : null,
		"numbers" : [1,2,5,6,9,10,13,14,17,18]
	},
	"Perm10" : {
		"min" : 11,
		"max" : 12,
		"maxBetMul" : null
	},
	"Match10" : {
		"min" : 10,
		"max" : 10,
		"maxBetMul" : null
	}
};
var tenByTwentyFixedBetType=["First10","Last10","AllOdd","AllEven","OddEven","EvenOdd","JumpEvenOdd","JumpOddEven"];
var currentNoOfLines = 0;
var currentBetMul = 1;
var currentIsQP = false;
$(document).ready(function(){
	
	/**
	 * For Manual Input on click
	 */		
	$(".tenByTwentyManualNumberEnter").on('click',function (e) {
		$("#tktView").css("display","none");
	    $("#gamePrizeScheme").css("display","block");
		currVal = $(this).val();
		$(this).val(currVal);
		if ($.inArray(selectedBetName,tenByTwentyFixedBetType) < 0) {
			if(numbersChosen.length != 0){
				if($(this).val().length != 2){
					if((selectedBetName == "Perm10" && !(numbersChosen.length > gameList[selectedBetName].min && numbersChosen.length < gameList[selectedBetName].max)) || (selectedBetName == "Direct10")){
						$(".tenByTwentyManualNumberEnter").each(function(){
							if ($(this).val().length < 2) {
								$("#error-popup").css('display','block');		
								$("#error").html("Please enter proper number");		
								$('#error-popup').delay(2000).fadeOut('slow');
								$(this).focus();
								return false;
							}
						});
					}
				}
			}else{
				$(".tenByTwentyManualNumberEnter").first().focus();
				$("#error").text("");
				$("#error-popup").css('display','none');
			}
		}else{
			$("#error").text("");
			$("#error-popup").css('display','none');
		}
	});
	
	/**
	 * For Manual Input on keydown
	 */	
	
	$(".tenByTwentyManualNumberEnter").on('keydown',function (e) {
		$("#tktView").css("display","none");
	    $("#gamePrizeScheme").css("display","block");
		var betInfo = getBetInfo();
		var betType = selectedBetName;
		var totalNum = gameLimitTBT[betType].max;
		if(e.ctrlKey){
			e.preventDefault();
		}
		if($.inArray($(this).val(),numbersChosen) >= 0){
			if((e.which == 8 || e.which == 46)){
				if($("#qpCheck").is(":checked")){
					$("#qpCheck").prop("checked",false);
				}
				var index = numbersChosen.indexOf($(this).val());
				$('#tbtnum'+$(this).val()+'').removeClass('selectnumber');
				numbersChosen.splice(index,1);
				currVal = "";
			}else if((e.keyCode >= 48 && e.keyCode <= 57) || (e.keyCode >= 96 && e.keyCode <= 105)){
				currVal = $(this).val();
			}
		}
		if(betType == "Perm10"){
			if(numbersChosen.length == totalNum -1){
				updateCurrNoLines(gameLimitTBT[selectedBetName].min,betType);
				checkIsDrawSelected();
			}else if(numbersChosen.length == totalNum){
				updateCurrNoLines(gameLimitTBT[selectedBetName].max,betType);
				checkIsDrawSelected();
			}else{
				$("#noOfLines").html("0");
				$("#buy").attr("disabled",true);
			}
		}else if(betType == "Direct10"){
			if(numbersChosen.length == totalNum){
				updateCurrNoLines(gameLimitTBT[selectedBetName].max,betType);
				checkIsDrawSelected();
			}
			else{
				$("#noOfLines").html("0");
				$("#buy").attr("disabled",true);
			}
		}
		updateTicketPrice(1);
		$("#noOfNumber").html(numbersChosen.length);
	});
		
	/**
	 * For Manual Input on keyup
	 */	
	
	$(".tenByTwentyManualNumberEnter").on('keyup',function (e) {
		$("#tktGen").html("");
		$("#error").html("");
		$("#error-popup").css('display','none');
		if(e.which == 9){
			currVal = $(this).val();
			if($(this).prev('.tenByTwentyManualNumberEnter').val() != ""){
				if ($(this).prev('.tenByTwentyManualNumberEnter').val().replace(/(^\s+|\s+$)/g, "").length < 2) {
					$("#error-popup").css('display','block');		
					$("#error").html("Please enter proper number");		
					$('#error-popup').delay(2000).fadeOut('slow');
					$(this).prev('.tenByTwentyManualNumberEnter').focus();
				}else{
					$(this).val(currVal);
					$("#error").html("");
					$("#error-popup").css('display','none');
				}
			}else{
				$("#error-popup").css('display','block');		
				$("#error").html("Please enter proper number");		
				$('#error-popup').delay(2000).fadeOut('slow');
				$(".tenByTwentyManualNumberEnter").each(function(){
					if($(this).val()==""){
						$(this).focus();
						return false;
					}
				});
			}
		}
		var betType = selectedBetName;
		var totalNum = gameLimitTBT[betType].max;
		if ($.inArray(betType,tenByTwentyFixedBetType) >= 0) {
			$("#error-popup").css('display','block');		
			$("#error").text("Numbers can not be picked manually for this bet type!!");		
			$('#error-popup').delay(2000).fadeOut('slow');
			return false;
		}
		var enteredNum = $(this).val().replace(/(^\s+|\s+$)/g, "");
		if(!isNaN(enteredNum)){
			if (enteredNum.length == 2) {
				if(enteredNum >=1 && enteredNum<=20){
					if($.inArray(enteredNum,numbersChosen) >= 0){
						if((e.which == 8 || e.which == 46)){
							var index = numbersChosen.indexOf($(this).val());
							$('#tbtnum'+$(this).val()+'').removeClass('selectnumber');
							numbersChosen.splice(index,1);
							$(this).val(currVal);
						}else if((e.keyCode >= 48 && e.keyCode <= 57) || (e.keyCode >= 96 && e.keyCode <= 105)){
							$(this).val(currVal);
						}
					}else{
						numbersChosen = [];
						$(".tenByTwentyNumber").removeClass('selectnumber');
						$("#numPicked").html('');
						var i = 0;
						var firstNum = 0;
						$(".tenByTwentyManualNumberEnter").each(function(i){
							if($(this).val()!=""){
								numbersChosen.push($(this).val());
								$('#tbtnum'+$(this).val()+'').addClass('selectnumber');
								if(firstNum!=0){
									$("#numPicked").append(","+$(this).val());
								}else{
									$("#numPicked").append($(this).val());
									firstNum++;
								}
							}
							i++;
						});
						if(numbersChosen.length == totalNum){
							$(".unitPrice").first().focus();
						}else{
							isValid = false;
							$("#noOfLines").html("0");
							$(".tenByTwentyManualNumberEnter").each(function(){
								if($(this).val()==""){
									$(this).focus();
									return false;
								}
							});
							$("#buy").attr("disabled",true);
						}
					}
				}else{
					$(this).val('');
				}
			}
			if(betType == "Perm10"){
				if(numbersChosen.length == totalNum -1){
					updateCurrNoLines(gameLimitTBT[selectedBetName].min,betType);
					isValid = true;
					checkIsDrawSelected();
				}else if(numbersChosen.length == totalNum){
					updateCurrNoLines(gameLimitTBT[selectedBetName].max,betType);
					isValid = true;
					checkIsDrawSelected();
				}else{
					isValid = false;
					$("#noOfLines").html("0");
					$("#buy").attr("disabled",true);
				}
			}else if(betType == "Direct10" && numbersChosen.length == totalNum){
				updateCurrNoLines(gameLimitTBT[selectedBetName].max,betType);
				isValid = true;
				checkIsDrawSelected();
			}
		}else{
			$(this).val('');
		}
		updateTicketPrice(1);
		$("#noOfNumber").html(numbersChosen.length);
	});
	
	/**
	 * Onclick on reset button in tenbytwenty
	 */
	$(document).on("click","#resetTenByTwenty",function(){
		resetTenByTwentyGame();
		$(".bettypes").removeClass("select-bet");
		$(".bettypes").first().focus();
		$(".bettypes").first().addClass("select-bet");
		$("#betTypeSel").html("Direct10"); 
		$("#numPicked").html("");
	});
	
	/**
	 * For selecting numbers
	 */
	$("li.tenByTwentyNumber").on("click",function(event){
		$("#tktView").css("display","none");
	    $("#gamePrizeScheme").css("display","block");
		$("#tktGen").html("");
		$("#error").html("");
		$("#error-popup").css('display','none');
		var betType = selectedBetName;
		var totalNum;
		var numNotUpdated = true;
		var totalNum = gameLimitTBT[betType].max;
		if ($.inArray(betType,tenByTwentyFixedBetType) >= 0) {
			$("#error-popup").css('display','block');
			$("#error").html("Numbers can not be picked manually for this bet type!!");
			$('#error-popup').delay(2000).fadeOut('slow');
			return false;
		}
		else{
			$("#error").html("");
			$("#error-popup").css('display','none');
			var noSelected = pmsParseInt($(this).children().text());
			var noText = $(this).children().text();
			if($.inArray(noText,numbersChosen) >= 0){
				if($("#qpCheck").is(":checked")){
					$("#qpCheck").prop("checked",false);
				}
				var index = numbersChosen.indexOf(noText);
				$('#tbtnum'+noText+'').removeClass('selectnumber');
				numbersChosen.splice(index,1);
				$(".tenByTwentyManualNumberEnter").each(function(){
					if($(this).val() == noText){
						$(this).val("");
						return false;
					}
				});
				if(betType == "Direct10" && numbersChosen.length != totalNum){
					isValid = false;
					$("#noOfLines").html("0");
					$("#buy").attr("disabled",true);
				}
				if(betType == "Perm10" && numbersChosen.length < totalNum-1){
					isValid = false;
					$("#noOfLines").html("0");
					$("#buy").attr("disabled",true);
				}
			}else{
				$("#error").text("");
				$("#error-popup").css('display','none');
				if(numbersChosen.length != totalNum){
					numbersChosen.push(noText);
					$('#tbtnum'+noText+'').addClass('selectnumber');
					$(".tenByTwentyManualNumberEnter").each(function(){
						if($(this).val().length < 2){
							$(this).val(noText);
							numNotUpdated = false;
						}
						return numNotUpdated;
					});
					if(betType == "Direct10" || betType == "Perm10"){
						$("#noOfLines").html("0");
					}
					isValid = false;
					$("#buy").attr("disabled",true);
				}
				else{
					$("#error-popup").css('display','block');
					$("#error").html('You can only select '+totalNum+' numbers');
					$('#error-popup').delay(2000).fadeOut('slow');
				}
			}
		}
		if(betType == "Perm10"){
			if(numbersChosen.length == (totalNum - 1)){
						updateCurrNoLines(gameLimitTBT[selectedBetName].min,betType);
						isValid = true;
						checkIsDrawSelected();
					}
					else if(numbersChosen.length == totalNum){
						updateCurrNoLines(gameLimitTBT[selectedBetName].max,betType);
						isValid = true;
						checkIsDrawSelected();
					}
				}
				else if(betType == "Direct10" && numbersChosen.length == totalNum){
					updateCurrNoLines(gameLimitTBT[selectedBetName].max,betType);
					isValid = true;
					checkIsDrawSelected();
				}
		var i = 0;
		var firstNum = 0;
		$("#numPicked").html("");
		$(numbersChosen).each(function(i){
			if(firstNum!=0){
				$("#numPicked").append(","+numbersChosen[i]);
			}else{
				$("#numPicked").append(numbersChosen[i]);
				firstNum++;
			}
			i++;
		});
		updateTicketPrice(1);
		$("#noOfNumber").html(numbersChosen.length);
	});
});

function tenByTwentyQP(){
	$(".tenByTwentyManualNumberEnter").each(function(){
		$(this).val('');
	});
	numbersChosen = [];
	$("#numPicked").html("");
	$("#error").text("");
	$("#error-popup").css('display','none');
	var betInfo = getBetInfo();
	if(!$("#qpCheck").is(":checked")){
		$("#qpCheck").prop("checked",true);
	}
	currentIsQP = true;
	resetTenByTwentyGame();
	$("#num-pick-div").css("display","block");
	$("#buy").attr("disabled",false);
	if(selectedBetName == "Direct10"){
		$("#noOfNumber").html("10");
		$("#noOfLines").html("1");
		updateTicketPrice(1);
	}else if(selectedBetName == "Perm10"){
		$("#noOfNumber").html("11");
		$("#noOfLines").html("11");
		$("#num-pick-div").css("display","block");
		updateTicketPrice(1);
	}
	var noToSelect = gameLimitTBT[selectedBetName].max;
	var rngResp = getGameWiseRNG("TenByTwenty",noToSelect,1);
	if (!rngResp) {
		$("#qpCheck").prop("checked",false);
		$("#error-popup").css('display','block');	
		$("#error").html("Please check the Internet connection and try again !!!");
		$('#error-popup').delay(2000).fadeOut('slow');
		return;
	}
		if(rngResp.responseMsg == "success"){
			numbersChosen = rngResp.pickData[0].split(",");
			var h = 0;
			$(".tenByTwentyManualNumberEnter").each(function(){
				$(this).val(numbersChosen[h]);
				h++;
			});
			h=0;
			$(numbersChosen).each(function(h){
				$("#tbtnum"+numbersChosen[h]).addClass('selectnumber');
				if(h==0){
					$("#numPicked").append(numbersChosen[h]);
				} else {
					$("#numPicked").append(","+numbersChosen[h]);
				}
				h++;
			});
			isValid = true;
			if(!isDrawManual){
				noOfDrawsSelected = alreadySelDraw.length;
				noOfSelectedDraws = alreadySelDraw;
			}else{
				noOfDrawsSelected = drawsSel;
				$("#numOfDrawsSelected").val(noOfDrawsSelected);
			}
		}else{
			$("#error").html(rngResp.responseMsg);
			$("#error-popup").css('display','block');		
			$('#error-popup').delay(2000).fadeOut('slow');
		}

	checkIsDrawSelected();
	updateTicketPrice(1);
}

/**
 * For game reset
 */

function resetTenByTwentyGame(){
	$(".tenByTwentyManualNumberEnter").prop('disabled',false);
	$('li.tenByTwentyNumber').removeClass('selectnumber');
	$(".unitPrice").removeClass('amt-select');
	$(".unitPrice").first().addClass("amt-select");
	betAmtSelected = parseFloat($(".unitPrice").first().text());
	$(".ad-draw-check-box").removeClass("select-checkbox");
	$(".ad-draw-check-box").children().prop("checked",false);
	$("#noOfAdvDraws").html("");
	$("#numOfDrawsSelected").val("1");
	isDrawManual = true;
	noOfDrawsSelected = parseInt($("#numOfDrawsSelected").val());
	drawsSel = noOfDrawsSelected;
	$("#qpCheck").attr("disabled",false);
	numbersSelected = [];
	numbersChosen=[];
	$("#error").html("");
	$("#error-popup").css('display','none');
	$("#buy").attr("disabled",true);
	currentIsQP = false;
	var betInfo = getBetInfo();
	$(".tenByTwentyManualNumberEnter").each(function(){
		$(this).val('');
	});
	if(!betInfo){
		currentBetMul = 0;
		$("#qpNumCount").html("0");
	}else{
		$("#unitPrice").html(currentBetMul);
		if(currentIsQP){
			if(!betInfo){
				currentNoOfLines=0;
				$("#tktPrice").html(0);
				$("#buy").attr("disabled",true);
			} else{
				$("#noOfNumber").html(gameLimitTBT[selectedBetName].min);
				updateCurrNoLines(gameLimitTBT[selectedBetName].min,selectedBetName);
				updateTicketPrice((betInfo[0]+"").length);
				$("#buy").attr("disabled",false);
			}
			$("#noOfLines").html(currentNoOfLines);
		} else{
			currentNoOfLines=0;
			$("#noOfNumber").html("0");
			$("#noOfLines").html(currentNoOfLines);
			$("#tktPrice").html("0");
		}
		$("#ticketAmt").html("0");
	 	
	}
}

/**
 * On changing bet type
 */
function changeBetTypeTenByTwenty() {
	$(this).removeClass("selectnumber");
	$(".tenByTwentyManualNumberEnter").prop('disabled',false);
	$('li.tenByTwentyNumber').removeClass('selectnumber');
	currentIsQP = false;
	$("#error").text("");
	numbersChosen = [];
	$(".tenByTwentyManualNumberEnter").prop('disabled',false);
	$("#qpCheck").attr("disabled",false);
	$("#noOfNumber").html(numbersChosen.length);
	var betType = selectedBetName;
	var tmpArr = gameLimitTBT[selectedBetName];
	if(betType==""){
		return false;
	}
	else if(betType == 'Perm10'){
		$("#13ForP12").css("display","block");
		$("#14ForP12").css("display","block");
	}
	else{
		$("#13ForP12").css("display","none");
		$("#14ForP12").css("display","none");
	}
	if(!betType)
		return false;
	$(".tenByTwentyManualNumberEnter").each(function(){
		$(this).val('');
	});
	$("#numPicked").html('');
	if ($.inArray(betType,tenByTwentyFixedBetType) >= 0) {
		$("#qpCheck").prop("checked",false);
		$("#qpCheck").trigger("change");
		currentIsQP = true;
		isValid = true;
		$("#qpCheck").attr("disabled",true);
		var i = 0;
		$("li.tenByTwentyNumber").each(function(){
			var tmpNum = pmsParseInt($(this).children().text());
			var tmpPos = $.inArray(tmpNum, gameLimitTBT[betType].numbers);
			if(tmpPos!=-1){
				$(this).addClass("selectnumber");
				numbersChosen.push(tmpNum);
				$(".tenByTwentyManualNumberEnter").each(function(){
					if($(this).val() == ""){
						$(this).val(tmpNum<=9?'0'+tmpNum:tmpNum);
						return false;
					}
				});
				if(i!=0){
					$("#numPicked").append(","+(tmpNum<=9?'0'+tmpNum:tmpNum));
				}
				else{
					$("#numPicked").append(tmpNum<=9?'0'+tmpNum:tmpNum);
					i++;
				}
			}
		});
		$(".tenByTwentyManualNumberEnter").prop('disabled',true);
		updateCurrNoLines(gameLimitTBT[selectedBetName].min,betType);
	}else{
		isValid = false;
		$("#noOfLines").html("0");
		$("#qpCheck").prop("checked",false);
		$("#qpCheck").trigger("change");
		$("#qpCheck").attr("disabled",false);
	}
	currentBetMul=parseFloat(tmpArr[0]).toFixed(2)+"";
	$("#qpNumCount").html(gameLimitTBT[selectedBetName].min);
	$("#unitPrice").html(currentBetMul);
	$("#betAmt").html(currUnitPrice);
	if(currentIsQP){
		updateCurrNoLines(gameLimitTBT[selectedBetName].min,betType);
	}else{
		$("#tktPrice").html("0");
	}
	$("#noOfNumber").html(numbersChosen.length);
	checkIsDrawSelected();
	updateTicketPrice(1);
}


function resetTenByTwentyForQP(){	
	$(".tenByTwentyManualNumberEnter").each(function(){
		$(this).val('');
	});
	numbersChosen = [];
	$("#numPicked").html("");
	$("#error").text("");
	$("#error-popup").css('display','none');
	var betInfo = getBetInfo();
	if(!$("#qpCheck").is(":checked")){
		$("#qpCheck").prop("checked",true);
	}
	currentIsQP = true;
	resetTenByTwentyGame();
	$("#num-pick-div").css("display","block");
	$("#buy").attr("disabled",false);
	if(selectedBetName == "Direct10"){
		$("#noOfNumber").html("10");
		$("#noOfLines").html("1");
		$("#num-pick-div").css("display","block");
		updateTicketPrice(1);
	}else if(selectedBetName == "Perm10"){
		$("#noOfNumber").html("11");
		$("#noOfLines").html("11");
		$("#num-pick-div").css("display","block");
		updateTicketPrice(1);
	}
	checkIsDrawSelected();
	updateTicketPrice(1);

}