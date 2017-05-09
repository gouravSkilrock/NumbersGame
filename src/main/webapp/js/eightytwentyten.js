var eightTwentyTenBetList = {
		"Direct1" : {
			"min" : 1,
			"max" : 1,
			"maxBetMul" : null
		},
		"Direct2" : {
			"min" : 2,
			"max" : 2,
			"maxBetMul" : null
		},
		"Direct3" : {
			"min" : 3,
			"max" : 3,
			"maxBetMul" : null
		},
		"Direct4" : {
			"min" : 4,
			"max" : 4,
			"maxBetMul" : null
		},
		"Direct5" : {
			"min" : 5,
			"max" : 5,
			"maxBetMul" : null
		},
		"Direct6" : {
			"min" : 6,
			"max" : 6,
			"maxBetMul" : null
		},
		"Direct7" : {
			"min" : 7,
			"max" : 7,
			"maxBetMul" : null
		},
		"Direct8" : {
			"min" : 8,
			"max" : 8,
			"maxBetMul" : null
		},
		"Direct9" : {
			"min" : 9,
			"max" : 9,
			"maxBetMul" : null
		},
		"Direct10" : {
			"min" : 10,
			"max" : 10,
			"maxBetMul" : null
		},
		"Perm2" : {
			"min" : 3,
			"max" : 10,
			"maxBetMul" : null
		},
		"Perm3" : {
			"min" : 4,
			"max" : 20,
			"maxBetMul" : null
		}
	};

$(document).ready(function(){
	/**
	 * Number selected by mouse
	 */
	$(".eightTwentyTenNum").on('click',function(){
		$("#tktView").css("display","none");
	    $("#gamePrizeScheme").css("display","block");
		$("#tktGen").html("");
		$("#error").html("");
		$("#error-popup").css('display','none');
		var minReqdNum = eightTwentyTenBetList[selectedBetName].min;
		var selectedNumber = $(this).text();
		if($.inArray(selectedNumber,numbersChosen) >= 0){
			if($("#qpCheck").is(":checked")){
				$("#qpCheck").prop("checked",false);
			}
			$(this).removeClass("selectnumber");
			var index = numbersChosen.indexOf(selectedNumber);
			numbersChosen.splice(index,1);
			$(".game80ManualEnter").each(function(){
				if($(this).val() == selectedNumber){
					$(this).val("");
					return false;
				}
			});
		}else{
			if(selectedBetName != 'Perm2' && selectedBetName != 'Perm3'){
				if(numbersChosen.length < minReqdNum){
					$("#error").html("");
					$("#error-popup").css('display','none');
					$(this).addClass("selectnumber");
					numbersChosen.push(selectedNumber);
					$(".game80ManualEnter").each(function(){
						if($(this).val() == ""){
							$(this).val(selectedNumber);
							return false;
						}
					});
				}else{
					$("#error-popup").css('display','block');
					$("#error").html('You can only select '+minReqdNum+' numbers');
					$('#error-popup').delay(2000).fadeOut('slow');
				}
			}else{
				if(selectedBetName != "Perm2" && selectedBetName != "Perm3" && numbersChosen.length == eightTwentyTenBetList[selectedBetName].max){
					$("#error-popup").css('display','block');		
					$("#error").html('You can only select '+eightTwentyTenBetList[selectedBetName].min+' numbers');
					$('#error-popup').delay(2000).fadeOut('slow');
				}else if((selectedBetName == "Perm2" || selectedBetName == "Perm3") && numbersChosen.length == eightTwentyTenBetList[selectedBetName].max){
					$("#error-popup").css('display','block');
					$("#error").html('You can only select maximum '+eightTwentyTenBetList[selectedBetName].max+' numbers');
					$('#error-popup').delay(2000).fadeOut('slow');
				}else{
					$("#error").html("");
					$("#error-popup").css('display','none');
					$(this).addClass("selectnumber");
					numbersChosen.push(selectedNumber);
					$(".game80ManualEnter").each(function(){
						if($(this).val() == ""){
							$(this).val(selectedNumber);
							return false;
						}
					});
				}
			}
		}
		if(selectedBetName != 'Perm2' && selectedBetName != 'Perm3' && numbersChosen.length == minReqdNum){
			updateNoOfLinesEightyTwentyTen(numbersChosen.length);
			isValid = true;
			checkIsDrawSelected();
		}else if(selectedBetName == 'Perm2' && numbersChosen.length >= eightTwentyTenBetList[selectedBetName].min && numbersChosen.length <= eightTwentyTenBetList[selectedBetName].max){
			updateNoOfLinesEightyTwentyTen(numbersChosen.length);
			isValid = true;
			checkIsDrawSelected();
		}else if(selectedBetName == 'Perm3' && numbersChosen.length >= eightTwentyTenBetList[selectedBetName].min && numbersChosen.length <= eightTwentyTenBetList[selectedBetName].max){
			updateNoOfLinesEightyTwentyTen(numbersChosen.length);
			isValid = true;
			checkIsDrawSelected();
		}else{
			isValid = false;
			$("#noOfLines").html("0");
			$("#buy").attr("disabled",true);
		}
		var k = 0;
		$("#eighty20Nums").html("");
		$("#numPicked").html('');
		$(numbersChosen).each(function(k){
			$("#eighty20Nums").append(numbersChosen[k]);
			$("#numPicked").append(numbersChosen[k]);
			if(k < numbersChosen.length - 1){
				$("#eighty20Nums").append(",");
				$("#numPicked").append(",");
			}
			k++;
		});
		$("#noOfNumber").html(numbersChosen.length);
		updateTicketPrice(selectedBetName.length);
	});
	
	/**
	 * For Manual Input on click
	 */		
	$(".game80ManualEnter").on('click',function (e) {
		$("#tktView").css("display","none");
	    $("#gamePrizeScheme").css("display","block");
	    currVal = $(this).val();
		$(this).val(currVal);
		if(numbersChosen.length != 0){
			if($(this).val().length != 2){
				if(((selectedBetName == "Perm2" || selectedBetName == "Perm3") && !(numbersChosen.length > gameList[selectedBetName].min && numbersChosen.length < gameList[selectedBetName].max)) || (selectedBetName != "Perm2" && selectedBetName != "Perm3")){
					$(".game80ManualEnter").each(function(){
						if ($(this).val().length < 2) {
							$("#error-popup").css('display','block');
							$("#error").html("Please enter proper number!!");
							$('#error-popup').delay(2000).fadeOut('slow');
							$(this).focus();
							return false;
						}
					});
				}
			}
		}else{
			$(".game80ManualEnter").first().focus();
			$("#error").html("");
			$("#error-popup").css('display','none');
		}
	});
	
	/**
	 * Manual entry of numbers on key down
	 */
	
	$(".game80ManualEnter").on('keydown',function (e) {
		$("#tktView").css("display","none");
	    $("#gamePrizeScheme").css("display","block");
		var totalNum = eightTwentyTenBetList[selectedBetName].max;
		if(e.ctrlKey){
			e.preventDefault();
		}
		if($.inArray($(this).val(),numbersChosen) >= 0){
			if((e.which == 8 || e.which == 46)){
				if($("#qpCheck").is(":checked")){
					$("#qpCheck").prop("checked",false);
				}
				var index = numbersChosen.indexOf($(this).val());
				$('#ettnum'+$(this).val()+'').removeClass('selectnumber');
				numbersChosen.splice(index,1);
				currVal = "";
			}else if((e.keyCode >= 48 && e.keyCode <= 57) || (e.keyCode >= 96 && e.keyCode <= 105)){
				currVal = $(this).val();
			}
		}
		$("#noOfNumber").html(numbersChosen.length);
		updateTicketPrice(selectedBetName.length);
	});
	
	/**
	 * Manual entry of numbers on keyup
	 */
	$(".game80ManualEnter").on("keyup",function(e){
		$("#tktGen").html("");
		$("#error").html("");
		$("#error-popup").css('display','none');
		var totalNum = eightTwentyTenBetList[selectedBetName].max;
		if(e.which == 9){
			currVal = $(this).val();
			if($(this).prev('.game80ManualEnter').val() != ""){
				if ($(this).prev('.game80ManualEnter').val().replace(/(^\s+|\s+$)/g, "").length < 2) {
					$("#error-popup").css('display','block');
					$("#error").html("Please enter proper number!!");
					$('#error-popup').delay(2000).fadeOut('slow');
					$(this).prev('.game80ManualEnter').focus();
				}else{
					$(this).val(currVal);
					$("#error").html("");
					$("#error-popup").css('display','none');
				}
			}else{
				$("#error-popup").css('display','block');
				$("#error").html("Please enter proper number!!");
				$('#error-popup').delay(2000).fadeOut('slow');
				$(".game80ManualEnter").each(function(){
					if($(this).val() == ""){
						$(this).focus();
						return false;
					}
				});
			}
		}
		var enteredNum = $(this).val().replace(/(^\s+|\s+$)/g, "");
		if(!isNaN(enteredNum)){
			if (enteredNum.length == 2) {
				if(enteredNum >=1 && enteredNum<=80){
					if($.inArray(enteredNum,numbersChosen) >= 0){
						if((e.which == 8 || e.which == 46)){
							var index = numbersChosen.indexOf($(this).val());
							$('#ettnum'+$(this).val()+'').removeClass('selectnumber');
							numbersChosen.splice(index,1);
							$(this).val(currVal);
						}else if((e.keyCode >= 48 && e.keyCode <= 57) || (e.keyCode >= 96 && e.keyCode <= 105)){
							$(this).val(currVal);
						}
					}else{
						$("#error").html("");
						$("#error-popup").css('display','none');
						numbersChosen = [];
						$(".eightTwentyTenNum").removeClass('selectnumber');
						$("#numPicked").html('');
						var i = 0;
						$(".game80ManualEnter").each(function(i){
							if($(this).val()!=""){
								numbersChosen.push($(this).val());
								$('#ettnum'+$(this).val()+'').addClass('selectnumber');
								$("#numPicked").append($(this).val());
								if(i!=totalNum-1){
									$("#numPicked").append(",");
								}
							}
							i++;
						});
						if(numbersChosen.length == totalNum){
							$(".unitPrice").first().focus();
						}else{
							$("#noOfLines").html("0");
							$(".game80ManualEnter").each(function(){
								if($(this).val()==""){
									$(this).focus();
									return false;
								}
							});
							isValid = false;
							$("#buy").attr("disabled",true);
						}
					}
				}else{
					$(this).val('');
				}
			}
			if(selectedBetName == "Perm2" || selectedBetName == "Perm3"){
				if(numbersChosen.length >= eightTwentyTenBetList[selectedBetName].min && numbersChosen.length <= eightTwentyTenBetList[selectedBetName].max){
					updateNoOfLinesEightyTwentyTen(numbersChosen.length);
					isValid = true;
					checkIsDrawSelected();
				}else{
					isValid = false;
					$("#noOfLines").html("0");
					$("#buy").attr("disabled",true);
				}
			}else if(selectedBetName != "Perm2" && selectedBetName != "Perm3" && numbersChosen.length == totalNum){
				updateNoOfLinesEightyTwentyTen(numbersChosen.length);
				isValid = true;
				checkIsDrawSelected();
			}else{
				isValid = false;
				$("#noOfLines").html("0");
				$("#buy").attr("disabled",true);
			}
		}else{
			$(this).val('');
		}
		var k = 0;
		$("#eighty20Nums").html("");
		$("#numPicked").html('');
		$(numbersChosen).each(function(k){
			$("#eighty20Nums").append(numbersChosen[k]);
			$("#numPicked").append(numbersChosen[k]);
			if(k < numbersChosen.length - 1){
				$("#eighty20Nums").append(",");
				$("#numPicked").append(",");
			}
			k++;
		});
		$("#noOfNumber").html(numbersChosen.length);
		updateTicketPrice(selectedBetName.length);
	});
	
});

function eightyTwentyTenQP(){
	numbersChosen = [];
	if(!$("#qpCheck").is(":checked")){
		$("#qpCheck").prop("checked",true);
	}
	resetKenoSixForQP();
	
	if(selectedBetName != ""){
		$("#noOfNumber").html(eightTwentyTenBetList[selectedBetName].min);
		$("#noOfLines").html("1");
	}
	if(selectedBetName == "Perm2" || selectedBetName == "Perm3"){
		$("#num-pick-div-game80").css("display","block");
		$("#qpNumCount-game80").html(eightTwentyTenBetList[selectedBetName].min);
	}else{
		$("#num-pick-div-game80").css("display","none");
	}
	var numOfTextBoxes = eightTwentyTenBetList[selectedBetName].max;
	for(var i=1; i<=numOfTextBoxes; i++){
		$("#game-eighty-num"+i).css("display","block");
	}
	var rngResp = getGameWiseRNG("KenoSix",numOfTextBoxes,1);
	if (rngResp == "NETERROR") {
		$("#qpCheck").prop("checked",false);
		$("#error-popup").css('display','block');	
		$("#error").html("Server not reachable, Please check connection and try again !!!");
		$('#error-popup').delay(2000).fadeOut('slow');
		return;
	}
		if(rngResp.responseMsg == "success"){
			numbersChosen = rngResp.pickData[0].split(",");
			var h = 0;
			$(".game80ManualEnter").each(function(){
				$(this).val(numbersChosen[h]);
				h++;
			});
			h=0;
			$(numbersChosen).each(function(h){
				$("#ettnum"+numbersChosen[h]).addClass('selectnumber');
				if(h==0){
					$("#numPicked").append(numbersChosen[h]);
				} else {
					$("#numPicked").append(","+numbersChosen[h]);
				}
				h++;
			});
			$("#qpCheck").prop("checked",true);
			$("#game80SelNum").css("display","none");
			$("#game80-input").css("display","none");
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

function changeBetTypeEightyTwentyTen(){
	for(var i in eightTwentyTenBetList){
		$(".input-box-fill-2").removeClass(i.toLowerCase()+"-block");
	}
	isValid = false;
	$("#numOfDrawsSelected").val(noOfDrawsSelected);
	$("#qpCheck").prop("checked",false);
	$("#qpCheck").attr("disabled",false);
	$(".eightTwentyTenNum").removeClass('selectnumber');
	$(".game80ManualEnter").val("");
	$(".game80ManualEnter").css("display","none");
	var numOfTextBoxes = eightTwentyTenBetList[selectedBetName].max;
	for(var i=1; i<=numOfTextBoxes; i++){
		$("#game-eighty-num"+i).css("display","block");
	}
	numbersChosen = [];
	$("#ticketAmt").html("0");
	$("#noOfNumber").html("0");
	$("#noOfLines").html("0");
	$("#numPicked").html("");
	var gameScheme = setKenoSixPrizeScheme(selectedBetName);
	$("#prizeScheme").html(gameScheme);
	$(".input-box-fill-2").addClass(selectedBetName.toLowerCase()+"-block");
	checkIsDrawSelected();
	updateTicketPrice(selectedBetName.length);
}

function resetEightyTwentyTenGame(){
	numbersChosen = [];
	$(".ad-draw-check-box").removeClass("select-checkbox");
	$(".ad-draw-check-box").children().prop("checked",false);
	$("#noOfAdvDraws").html("...");
	$("#numOfDrawsSelected").val("1");
	isDrawManual = true;
	noOfDrawsSelected = parseInt($("#numOfDrawsSelected").val());
	drawsSel = noOfDrawsSelected;
	$("#error").html("");
	$("#error-popup").css('display','none');
	$(".unitPrice").removeClass('amt-select');
	$(".unitPrice").first().addClass("amt-select");
	betAmtSelected = parseInt($(".unitPrice").first().text());
	$(".eightTwentyTenNum").removeClass('selectnumber');
	$("#buy").attr("disabled",true);
	$("#eighty20Nums").html("");
	$("#qpCheck").prop("checked",false);
	$("#qpCheck").attr("disabled",false);
	$("#ticketAmt").html("0");
	$("#noOfNumber").html("0");
	$("#noOfLines").html("0");
	$("#numPicked").html("");
	$("#unitPrice").val(currUnitPrice);
	$("#betAmt").html(currUnitPrice);
	$("#game80-input").css("display","block");
	$("#numClick-Game80").css("display","block");
	$(".game80ManualEnter").val("");
	$(".game80ManualEnter").css("display","none");
	$("#game-eighty-num1").css("display","block");
	$("#buy").attr("disabled",true);
	updateTicketPrice(selectedBetName.length);
}

function updateNoOfLinesEightyTwentyTen(totalNum){
	if(selectedBetName != "Perm2" && selectedBetName != "Perm3"){
		$("#noOfLines").html("1");
	}else if(selectedBetName == "Perm2"){
		var numOfLines = getCombinations(totalNum,2) ;
		$("#noOfLines").html(numOfLines);
	}else if(selectedBetName == "Perm3"){
		var numOfLines = getCombinations(totalNum,3) ;
		$("#noOfLines").html(numOfLines);
	}
}

function resetKenoSixForQP(){
	numbersChosen = [];
	$("#error").html("");
	$("#error-popup").css('display','none');
	$(".eightTwentyTenNum").removeClass('selectnumber');
	$("#buy").attr("disabled",true);
	$("#eighty20Nums").html("");
	$("#qpCheck").prop("checked",false);
	$("#qpCheck").attr("disabled",false);
	$("#ticketAmt").html("0");
	$("#noOfNumber").html("0");
	$("#noOfLines").html("0");
	$("#numPicked").html("");
	$("#betAmt").html(currUnitPrice);
	$("#game80-input").css("display","block");
	$("#numClick-Game80").css("display","block");
	$(".game80ManualEnter").val("");
	$(".game80ManualEnter").css("display","none");
	$("#game-eighty-num1").css("display","block");
	$("#buy").attr("disabled",true);
	updateTicketPrice(selectedBetName.length);
}

function setKenoSixPrizeScheme(betType){
	$("#prizeScheme").html("");
	var domElement = '';
	var betScheme = kenoSixBetTypeWisePrizeScheme[betType];
	for(var i in betScheme){
		domElement += "<tr><td>"+i+"</td><td>"+betScheme[i]+"</td></tr>";
	}
	return domElement;
}