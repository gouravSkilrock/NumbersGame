var gameLimit = {
	"Direct12" : {
		"min" : 12,
		"max" : 12,
		"maxBetMul" : null
	},
	"First12" : {
		"min" : 12,
		"max" : 12,
		"maxBetMul" : null,
		"numbers" : [1,2,3,4,5,6,7,8,9,10,11,12]
	},
	"Last12" : {
		"min" : 12,
		"max" : 12,
		"maxBetMul" : null,
		"numbers" : [13,14,15,16,17,18,19,20,21,22,23,24]
	},
	"AllOdd" : {
		"min" : 12,
		"max" : 12,
		"maxBetMul" : null,
		"numbers" : [1,3,5,7,9,11,13,15,17,19,21,23]
	},
	"AllEven" : {
		"min" : 12,
		"max" : 12,
		"maxBetMul" : null,
		"numbers" : [2,4,6,8,10,12,14,16,18,20,22,24]
	},
	"OddEven" : {
		"min" : 12,
		"max" : 12,
		"maxBetMul" : null,
		"numbers" : [1,3,5,7,9,11,14,16,18,20,22,24]
	},
	"EvenOdd" : {
		"min" : 12,
		"max" : 12,
		"maxBetMul" : null,
		"numbers" : [2,4,6,8,10,12,13,15,17,19,21,23]
	},
	"JumpEvenOdd" : {
		"min" : 12,
		"max" : 12,
		"maxBetMul" : null,
		"numbers" : [3,4,7,8,11,12,15,16,19,20,23,24]
	},
	"JumpOddEven" : {
		"min" : 12,
		"max" : 12,
		"maxBetMul" : null,
		"numbers" : [1,2,5,6,9,10,13,14,17,18,21,22]
	},
	"Perm12" : {
		"min" : 13,
		"max" : 14,
		"maxBetMul" : null
	},
	"Match10" : {
		"min" : 10,
		"max" : 10,
		"maxBetMul" : null
	}
};
var twelveByTwentyFourFixedBetType=["First12","Last12","AllOdd","AllEven","OddEven","EvenOdd","JumpEvenOdd","JumpOddEven"];
var currentNoOfLines = 0;
var currentBetMul = 1;
var noPicked = 0;
var currentIsQP = false;
$(document).ready(function(){

	
	/**
	 * For Manual Input on click
	 */		
	$(".manualNumberEnter").on('click',function (e) {
		$("#tktView").css("display","none");
	    $("#gamePrizeScheme").css("display","block");
		currVal = $(this).val();
		$(this).val(currVal);
		if ($.inArray(selectedBetName,twelveByTwentyFourFixedBetType) < 0) {
			if(numbersChosen.length != 0){
				if($(this).val().length != 2){
					if((selectedBetName == "Perm12" && !(numbersChosen.length > gameList[selectedBetName].min && numbersChosen.length < gameList[selectedBetName].max)) || (selectedBetName == "Direct12")){
						$(".manualNumberEnter").each(function(){
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
				$(".manualNumberEnter").first().focus();
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
	
	$(".manualNumberEnter").on('keydown',function (e) {
		$("#tktView").css("display","none");
	    $("#gamePrizeScheme").css("display","block");
		var betInfo = getBetInfo();
		var betType = selectedBetName;
		var totalNum = gameLimit[betType].max;
		if(e.ctrlKey){
			e.preventDefault();
		}
		if($.inArray($(this).val(),numbersChosen) >= 0){
			if((e.which == 8 || e.which == 46)){
				if($("#qpCheck").is(":checked")){
					$("#qpCheck").prop("checked",false);
				}
				var index = numbersChosen.indexOf($(this).val());
				$('#num'+$(this).val()+'').removeClass('selectnumber');
				numbersChosen.splice(index,1);
				currVal = "";
			}else if((e.keyCode >= 48 && e.keyCode <= 57) || (e.keyCode >= 96 && e.keyCode <= 105)){
				currVal = $(this).val();
			}
		}
		if(betType == "Perm12"){
			if(numbersChosen.length == totalNum -1){
				updateCurrNoLines(gameLimit[selectedBetName].min,betType);
				checkIsDrawSelected();
			}else if(numbersChosen.length == totalNum){
				updateCurrNoLines(gameLimit[selectedBetName].max,betType);
				checkIsDrawSelected();
			}else{
				$("#noOfLines").html("0");
				$("#buy").attr("disabled",true);
			}
		}else if(betType == "Direct12"){
			if(numbersChosen.length == totalNum){
				updateCurrNoLines(gameLimit[selectedBetName].max,betType);
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
	
	$(".manualNumberEnter").on('keyup',function (e) {
		$("#tktGen").html("");
		$("#error").html("");
		$("#error-popup").css('display','none');
		if(e.which == 9){
			currVal = $(this).val();
			if($(this).prev('.manualNumberEnter').val() != ""){
				if ($(this).prev('.manualNumberEnter').val().replace(/(^\s+|\s+$)/g, "").length < 2) {
					$("#error-popup").css('display','block');		
					$("#error").html("Please enter proper number");		
					$('#error-popup').delay(2000).fadeOut('slow');
					$(this).prev('.manualNumberEnter').focus();
				}else{
					$(this).val(currVal);
					$("#error").html("");
					$("#error-popup").css('display','none');
				}
			}else{
				$("#error-popup").css('display','block');		
				$("#error").html("Please enter proper number");		
				$('#error-popup').delay(2000).fadeOut('slow');
				$(".manualNumberEnter").each(function(){
					if($(this).val()==""){
						$(this).focus();
						return false;
					}
				});
			}
		}
		var betType = selectedBetName;
		var totalNum = gameLimit[betType].max;
		if ($.inArray(betType,twelveByTwentyFourFixedBetType) >= 0) {
			$("#error-popup").css('display','block');		
			$("#error").text("Numbers can not be picked manually for this bet type!!");		
			$('#error-popup').delay(2000).fadeOut('slow');
			return false;
		}
		var enteredNum = $(this).val().replace(/(^\s+|\s+$)/g, "");
		if(!isNaN(enteredNum)){
			if (enteredNum.length == 2) {
				if(enteredNum >=1 && enteredNum<=24){
					if($.inArray(enteredNum,numbersChosen) >= 0){
						if((e.which == 8 || e.which == 46)){
							var index = numbersChosen.indexOf($(this).val());
							$('#num'+$(this).val()+'').removeClass('selectnumber');
							numbersChosen.splice(index,1);
							$(this).val(currVal);
						}else if((e.keyCode >= 48 && e.keyCode <= 57) || (e.keyCode >= 96 && e.keyCode <= 105)){
							$(this).val(currVal);
						}
					}else{
						numbersChosen = [];
						$(".pmsnumber").removeClass('selectnumber');
						$("#numPicked").html('');
						var i = 0;
						var firstNum = 0;
						$(".manualNumberEnter").each(function(i){
							if($(this).val()!=""){
								numbersChosen.push($(this).val());
								$('#num'+$(this).val()+'').addClass('selectnumber');
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
							$(".manualNumberEnter").each(function(){
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
			if(betType == "Perm12"){
				if(numbersChosen.length == totalNum -1){
					updateCurrNoLines(gameLimit[selectedBetName].min,betType);
					isValid = true;
					checkIsDrawSelected();
				}else if(numbersChosen.length == totalNum){
					updateCurrNoLines(gameLimit[selectedBetName].max,betType);
					isValid = true;
					checkIsDrawSelected();
				}else{
					isValid = false;
					$("#noOfLines").html("0");
					$("#buy").attr("disabled",true);
				}
			}else if(betType == "Direct12" && numbersChosen.length == totalNum){
				updateCurrNoLines(gameLimit[selectedBetName].max,betType);
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
    * on click on qp box for perm12 
    */
	
	$(document).on("keyup","#twelveByTwentyQpBox",function(){
	   noPicked=$(this).val().replace(/(^\s+|\s+$)/g, "");	
	   if(!isNaN(noPicked)){
		   if(noPicked.length==2){

			   if(noPicked == 13 || noPicked == 14 ){
				   if(noPicked == 13){
					   $("#noOfLines").html("13");
					   $("#noOfNumber").html("13");
				   }
				   else{
					   $("#noOfNumber").html("14");
					   $("#noOfLines").html("91");
				   }
				   var rngResp = getGameWiseRNG("TwelveByTwentyFour",noPicked,1);	
					if (rngResp == "NETERROR") {
						$("#qpCheck").prop("checked",false);
						$("#error-popup").css('display','block');	
						$("#error").html("Server not reachable, Please check connection and try again !!!");
						$('#error-popup').delay(2000).fadeOut('slow');
						return;
					}
				     	var betInfo = getBetInfo();
					    currentIsQP = true;
						if(rngResp.responseMsg == "success"){
							numbersChosen = rngResp.pickData[0].split(",");
							var h = 0;
							$(".manualNumberEnter").each(function(){
								$(this).val(numbersChosen[h]);
								h++;
							});
							h=0;
							$(numbersChosen).each(function(h){
								$("#num"+numbersChosen[h]).addClass('selectnumber');
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
					$(".unitPrice").first().focus();
			   }
			   else{
				    $("#noOfLines").html("0");
				    $("#numPicked").html("");
				    $("#tktPrice").html("0");
				    $(".pmsnumber").removeClass("selectnumber");
				    $(".manualNumberEnter").val("");
				    $("#buy").attr("disabled",true);
				    numbersChosen=[];
				    $("#error").html("Wrong Input, Number should be 13 or 14 ");
					$("#error-popup").css('display','block');		
					$('#error-popup').delay(2000).fadeOut('slow');
			   }
		   }else{
			   $("#noOfLines").html("0");
			    $("#numPicked").html("");
			    $("#tktPrice").html("0");
			    $(".pmsnumber").removeClass("selectnumber");
			    $(".manualNumberEnter").val("");
			    numbersChosen=[];
			    $("#buy").attr("disabled",true);
			    /*$("#error").html("Wrong Input, Number should be 13 or 14 ");
				$("#error-popup").css('display','block');		
				$('#error-popup').delay(2000).fadeOut('slow');*/
		   }
	   }
	   else{
		   $(this).val("");
	   }
	   
	});
	
	/**
	 * For selecting numbers
	 */
	$("li.pmsnumber").on("click",function(event){
		$("#tktView").css("display","none");
	    $("#gamePrizeScheme").css("display","block");
		$("#tktGen").html("");
		$("#error").html("");
		$("#error-popup").css('display','none');
		var betType = selectedBetName;
		var totalNum;
		var numNotUpdated = true;
		var totalNum = gameLimit[betType].max;
		if($("#qpCheck").is(":checked") && betType == "Perm12"){
			$("#qpCheck").prop("checked",false);
			//$("#twelveByTwentyQpBox").attr('disabled',true);
			$("#twelveByTwentyQpBox").css('display','none');
			$("#twelveByTwentyQpBox").val("");
			$("#twelveByTwentyQp").css("display","none");
			$("#twelveByTwentyQpTextboxName").css('display','none');
			
		}
		if ($.inArray(betType,twelveByTwentyFourFixedBetType) >= 0) {
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
				$('#num'+noText+'').removeClass('selectnumber');
				numbersChosen.splice(index,1);
				$(".manualNumberEnter").each(function(){
					if($(this).val() == noText){
						$(this).val("");
						return false;
					}
				});
				if(betType == "Direct12" && numbersChosen.length != totalNum){
					isValid = false;
					$("#noOfLines").html("0");
					$("#buy").attr("disabled",true);
				}
				if(betType == "Perm12" && numbersChosen.length < totalNum-1){
					isValid = false;
					$("#noOfLines").html("0");
					$("#buy").attr("disabled",true);
				}
			}else{
				$("#error").text("");
				$("#error-popup").css('display','none');
				if(numbersChosen.length != totalNum){
					numbersChosen.push(noText);
					$('#num'+noText+'').addClass('selectnumber');
					$(".manualNumberEnter").each(function(){
						if($(this).val().length < 2){
							$(this).val(noText);
							numNotUpdated = false;
						}
						return numNotUpdated;
					});
					if(betType == "Direct12" || betType == "Perm12"){
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
		if(betType == "Perm12"){
			if(numbersChosen.length == (totalNum - 1)){
						updateCurrNoLines(gameLimit[selectedBetName].min,betType);
						isValid = true;
						checkIsDrawSelected();
					}
					else if(numbersChosen.length == totalNum){
						updateCurrNoLines(gameLimit[selectedBetName].max,betType);
						isValid = true;
						checkIsDrawSelected();
					}
				}
				else if(betType == "Direct12" && numbersChosen.length == totalNum){
					updateCurrNoLines(gameLimit[selectedBetName].max,betType);
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

function twelveByTwentyFourQP(){
	$(".manualNumberEnter").each(function(){
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
	resetTwelveByTwentyFourForQP();
	$("#num-pick-div").css("display","block");
	$("#buy").attr("disabled",false);
	if(selectedBetName == "Direct12"){
		$("#noOfNumber").html("12");
		$("#noOfLines").html("1");
		$("#num-pick-div").css("display","block");
		updateTicketPrice(betInfo.length);
	}
	var noToSelect = gameLimit[selectedBetName].max;
	var rngResp = getGameWiseRNG("TwelveByTwentyFour",noToSelect,1);	
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
			$(".manualNumberEnter").each(function(){
				$(this).val(numbersChosen[h]);
				h++;
			});
			h=0;
			$(numbersChosen).each(function(h){
				$("#num"+numbersChosen[h]).addClass('selectnumber');
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

function resetTwelveByTwentyFourGame(){
	$("#twelveByTwentyQpBox").val("");
	$("#twelveByTwentyQpTextboxName").css("display","none");
	$("#twelveByTwentyQp").css("display","none");
	$(".manualNumberEnter").prop('disabled',false);
	$('li.pmsnumber').removeClass('selectnumber');
	$(".unitPrice").removeClass('amt-select');
	$(".unitPrice").first().addClass("amt-select");
	betAmtSelected = $(".unitPrice").first().text();
	$(".ad-draw-check-box").removeClass("select-checkbox");
	$(".ad-draw-check-box").children().prop("checked",false);
	$("#noOfAdvDraws").html("...");
	$("#numOfDrawsSelected").val("1");
	isDrawManual = true;
	noOfDrawsSelected = parseInt($("#numOfDrawsSelected").val());
	drawsSel = noOfDrawsSelected;
	$("#qpCheck").attr("disabled",false);
	numbersSelected = [];
	$("#error").html("");
	$("#error-popup").css('display','none');
	$("#buy").attr("disabled",true);
	$("#num-pick-div").css("display","none");
	currentIsQP = false;
	var betInfo = getBetInfo();
	$(".manualNumberEnter").each(function(){
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
				$("#noOfNumber").html(gameLimit[selectedBetName].min);
				updateCurrNoLines(gameLimit[selectedBetName].min,selectedBetName);
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
function changeBetTypeTTF() {
	$(".twelveByTwenty-perm12-input-area").css("display","none");
	if("Perm12"==selectedBetName){
		$(".twelveByTwenty-perm12-input-area").css("display","block");
	}
	//resetTwelveByTwentyFourGame();
	//console.log(betAmtSelected);
	$(this).removeClass("selectnumber");
	$(".manualNumberEnter").prop('disabled',false);
	$('li.pmsnumber').removeClass('selectnumber');
	currentIsQP = false;
	/*if(!isDrawManual){
		noOfDrawsSelected = alreadySelDraw.length;
		noOfSelectedDraws = alreadySelDraw;
	}else{
		noOfDrawsSelected = drawsSel;
		$("#numOfDrawsSelected").val(noOfDrawsSelected);
	}*/
	$("#error").text("");
	numbersChosen = [];
	$("#twelveByTwentyQpBox").val("");
	$("#twelveByTwentyQpTextboxName").css("display","none");
	$("#twelveByTwentyQp").css("display","none"); 
	$(".manualNumberEnter").prop('disabled',false);
	$("#qpCheck").attr("disabled",false);
	$("#noOfNumber").html(numbersChosen.length);
	var betType = selectedBetName;
	var tmpArr = gameLimit[betType];
	if(betType==""){
		return false;
	}
	else if(betType == 'Perm12'){
		$("#13ForP12").css("display","block");
		$("#14ForP12").css("display","block");
	}
	else{
		$("#13ForP12").css("display","none");
		$("#14ForP12").css("display","none");
	}
	if(!betType)
		return false;
	$(".manualNumberEnter").each(function(){
		$(this).val('');
	});
	$("#numPicked").html('');
	if ($.inArray(betType,twelveByTwentyFourFixedBetType) >= 0) {
		$("#qpCheck").prop("checked",false);
		$("#qpCheck").trigger("change");
		currentIsQP = true;
		isValid = true;
		$("#qpCheck").attr("disabled",true);
		var i = 0;
		$("li.pmsnumber").each(function(){
			var tmpNum = pmsParseInt($(this).children().text());
			var tmpPos = $.inArray(tmpNum, gameLimit[betType].numbers);
			if(tmpPos!=-1){
				$(this).addClass("selectnumber");
				numbersChosen.push(tmpNum);
				$(".manualNumberEnter").each(function(){
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
		$(".manualNumberEnter").prop('disabled',true);
		updateCurrNoLines(gameLimit[selectedBetName].min,betType);
	}else{
		isValid = false;
		$("#noOfLines").html("0");
		$("#qpCheck").prop("checked",false);
		$("#qpCheck").trigger("change");
		$("#qpCheck").attr("disabled",false);
	}
	currentBetMul=parseFloat(tmpArr[0]).toFixed(2)+"";
	$("#qpNumCount").html(gameLimit[selectedBetName].min);
	$("#unitPrice").html(currentBetMul);
	$("#betAmt").html(currUnitPrice);
	if(currentIsQP){
		updateCurrNoLines(gameLimit[selectedBetName].min,betType);
	}else{
		$("#tktPrice").html("0");
	}
	$(".twelveByTwenty-input-area").removeClass("permTwelve-block");
	if("Perm12" == selectedBetName){
		$(".twelveByTwenty-input-area").addClass("permTwelve-block");
	}
	$("#noOfNumber").html(numbersChosen.length);
	checkIsDrawSelected();
	updateTicketPrice(1);
}


function resetTwelveByTwentyFourForQP(){
	$("#twelveByTwentyQpBox").val("");
	$("#twelveByTwentyQpTextboxName").css("display","none");
	$("#twelveByTwentyQp").css("display","none");
	$(".manualNumberEnter").prop('disabled',false);
	$('li.pmsnumber').removeClass('selectnumber');
	$("#qpCheck").attr("disabled",false);
	numbersSelected = [];
	$("#error").html("");
	$("#error-popup").css('display','none');
	$("#buy").attr("disabled",true);
	$(".manualNumberEnter").each(function(){
		$(this).val('');
	});
	currentIsQP = false;
	if(currentIsQP){
		if(!betInfo){
			currentNoOfLines=0;
			$("#tktPrice").html(0);
			$("#buy").attr("disabled",true);
		} else{
			$("#noOfNumber").html(gameLimit[selectedBetName].min);
			updateCurrNoLines(gameLimit[selectedBetName].min,selectedBetName);
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