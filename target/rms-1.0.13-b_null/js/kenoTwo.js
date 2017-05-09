var kenoTwoBetList = {
							"Direct1" : {
								"min" : 1,
								"max" : 1
							},
							"Direct2" : {
								"min" : 2,
								"max" : 2
							},
							"Direct3" : {
								"min" : 3,
								"max" : 3
							},
							"Direct4" : {
								"min" : 4,
								"max" : 4
							},
							"Direct5" : {
								"min" : 5,
								"max" : 5
							},
							"Perm1" : {
								"min" : 10,
								"max" : 10
							},
							"Perm2" : {
								"min" : 3,
								"max" : 20
							},
							"Perm3" : {
								"min" : 4,
								"max" : 20
							}
						};

$(document).ready(function(){
	/**
	 * Number selected by mouse
	 */
	$(".kenoTwoNumber").on('click',function(){
		$("#tktView").css("display","none");
	    $("#gamePrizeScheme").css("display","block");
		$("#tktGen").html("");
		$("#error").html("");
		$("#error-popup").css('display','none');
		var minReqdNum = kenoTwoBetList[selectedBetName].min;
		var maxReqdNum = kenoTwoBetList[selectedBetName].max;
		var selectedNumber = $(this).text();
		if($.inArray(selectedNumber,numbersChosen) >= 0){
			ifSelectedNoIsInnumberChosenArray(selectedNumber,this);
		}else{
			ifSelectedNoIsNotInnumberChosenArray(maxReqdNum,selectedNumber,this);
		}
		if(numbersChosen.length >= minReqdNum){
			updateNoOfLinesForKenoTwo(numbersChosen.length);
			isValid = true;
			checkIsDrawSelected();
		}else{
			isValid = false;
			$("#noOfLines").html("0");
			$("#buy").attr("disabled",true);
		}
		prepareNumberList();
		$("#noOfNumber").html(numbersChosen.length);
		updateTicketPrice(selectedBetName.length);
		$("#numToBePickedFN").blur(); 
		
	});
	
	/**
	 * For Manual Input on click
	 */		
	$(".kenoTwoNumberEnter").on('click',function (e) {
		$("#tktView").css("display","none");
	    $("#gamePrizeScheme").css("display","block");
	    currVal = $(this).val();
		$(this).val(currVal);
		if(numbersChosen.length != 0){
			if($(this).val().length != 2){
				$(".kenoTwoNumberEnter").each(function(){
					if ($(this).val().length < 2) {
						$("#error").html("Please enter proper number!!");
						erroPopUp();
						$(this).focus();
						return false;
					}
				});
			}
		}else{
			$(".kenoTwoNumberEnter").first().focus();
			$("#error").html("");
			$("#error-popup").css('display','none');
		}
	});
	
	/**
	 * Manual entry of numbers on key down
	 */
	
	$(".kenoTwoNumberEnter").on('keydown',function (e) {
		$("#tktView").css("display","none");
	    $("#gamePrizeScheme").css("display","block");
		var totalNum = kenoTwoBetList[selectedBetName].max;
		if(e.ctrlKey){
			e.preventDefault();
		}
		if($.inArray($(this).val(),numbersChosen) >= 0){
			if((e.which == 8 || e.which == 46)){
				toDisableCheckBoxIfQuickPickChecked();
				var index = numbersChosen.indexOf($(this).val());
				$('#kenoTwoNum'+$(this).val()+'').removeClass('selectnumber');
				numbersChosen.splice(index,1);
				currVal = "";
			}else if((e.keyCode >= 48 && e.keyCode <= 57) || (e.keyCode >= 96 && e.keyCode <= 105)){
				currVal = $(this).val();
			}
		}
		$("#noOfNumber").html(numbersChosen.length);
		updateTicketPrice(selectedBetName.length);
		$("#numToBePickedFN").blur(); 
		
	});
	
	/**
	 * Manual entry of numbers on keyup
	 */
	
	$(".kenoTwoNumberEnter").on("keyup",function(e){
		$("#tktGen").html("");
		$("#error").html("");
		$("#error-popup").css('display','none');
		var minReqdNum = kenoTwoBetList[selectedBetName].min;
		var totalNum = kenoTwoBetList[selectedBetName].max;
		if(e.which == 9){
			handlingTabWhenNoEnterInTextField(this);
		}
		var enteredNum = $(this).val().replace(/(^\s+|\s+$)/g, "");
		if(!isNaN(enteredNum)){
			if (enteredNum.length == 2) {
				if(enteredNum >=1 && enteredNum<=90){
					toDisableCheckBoxIfQuickPickChecked();
					if($.inArray(enteredNum,numbersChosen) >= 0){
						ifEnteredNoExists(this,e);
					}else{
						ifEnteredNoNotExists(minReqdNum,totalNum);
					}
				}else{
					$(this).val('');
				}
			}
			if(numbersChosen.length >= minReqdNum){
				updateNoOfLinesForKenoTwo(numbersChosen.length);
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
		prepareNumberList();
		$("#noOfNumber").html(numbersChosen.length);
		updateTicketPrice(selectedBetName.length);
		$("#numToBePickedFN").blur(); 
		
	});
	
	$("#numToBePickedFN").on("keyup",function(e){
		if ((e.which == 8 || e.which == 46) && ($(this).val() == null || $(this).val().replace(/(^\s+|\s+$)/g, "").length < 2 || $(this).val().replace(/(^\s+|\s+$)/g, "") == "")) {
			$(this).val('');
			$(".kenoTwoNumber").removeClass("selectnumber");
			$(".kenoTwoNumberEnter").val("");
			numbersChosen = [];
			$("#numPicked").html("");
			if("Perm1" == selectedBetName){
				$("#error").html("Total numbers can be selected is 10");	
			}else if("Perm2" == selectedBetName){
				$("#error").html("Total numbers can be selected between 3 to 20");
			}else if("Perm3" == selectedBetName){
				$("#error").html("Total numbers can be selected between 4 to 20");
			}
			erroPopUp();
			$("#noOfLines").html("0");
			isValid = false;
			checkIsDrawSelected();
			updateTicketPrice(1);
			$("#numToBePickedFN").blur(); 
			
			$("#buy").attr("disabled",true);
			return;
		}
		if((e.which == 8 || e.which == 46) && $(this).val().replace(/(^\s+|\s+$)/g, "").length < 1){
			$(".kenoTwoNumber").removeClass("selectnumber");
			$(".kenoTwoNumberEnter").val("");
			numbersChosen = [];
			$("#numPicked").html("");
			$("#error").html("");
			$("#error-popup").css('display','none');
		}else if (e.which == 32) {
			e.preventDefault();
			$(this).focus();
			return;
		}else if (e.which >=37 && e.which <= 40) {
			e.preventDefault();
			return;
		}else if(!((e.which >= 48 && e.which <= 57) || (e.which >= 96 && e.which <= 105))){
			e.preventDefault();
			$(this).val("");
			if(e.which == 81){
				e.preventDefault();
				return;
			}
			if("Perm1" == selectedBetName){
				$("#error").html("Total numbers can be selected is 10");	
			}else if("Perm2" == selectedBetName){
				$("#error").html("Total numbers can be selected between 3 to 20");
			}else if("Perm3" == selectedBetName){
				$("#error").html("Total numbers can be selected between 4 to 20");
			}
			erroPopUp();
	    }else{
			if(!isNaN($(this).val())){
				if("Perm1" == selectedBetName){
					if(parseInt($(this).val())!=10){
						perm1IfSelectedNoNotOfCorrectLimit(this);
					} else{
						commonBlockForPerm123IFENteredNoAreInLimit(this);
					}
				}else if("Perm2" == selectedBetName){
					if(parseInt($(this).val())<3 || parseInt($(this).val())>20){
						perm2IfSelectedNoNotOfCorrectLimit(this);
					} else{
						commonBlockForPerm123IFENteredNoAreInLimit(this);
					}
				}else if("Perm3" == selectedBetName){
					if(parseInt($(this).val())<4 || parseInt($(this).val())>20){
						perm3IfSelectedNoNotOfCorrectLimit(this);
					} else{
						commonBlockForPerm123IFENteredNoAreInLimit(this);
					}
				}
			} else{
				$(this).val("");
				if("Perm1" == selectedBetName){
					$("#error").html("Total numbers can be selected is 10");	
				}else if("Perm2" == selectedBetName){
					$("#error").html("Total numbers can be selected between 3 to 20");
				}else if("Perm3" == selectedBetName){
					$("#error").html("Total numbers can be selected between 4 to 20");
				}
				erroPopUp();
			}
		}
	
	});
	
	$(document).on("keydown","#numToBePickedFN",function(e){
	   	 switch (e.which) {
				case 13:
					e.preventDefault();
					if (!$(this).val() == "") {
						$(".unitPrice").first().focus();
						return;
					}
					$(this).focus();
				break;
				case 9:
					e.preventDefault();
					if (!$(this).val() == "") {
						$(".unitPrice").first().focus();
						return;
					}
					$(this).focus();
	            break;
				default:
					$(this).focus();
				break;
			}
  	 
  	 
   });	
	
});

function kenoTwoQP(){
	var numToBePicked = 0;
	numbersChosen = [];
	if(!$("#qpCheck").is(":checked")){
		$("#qpCheck").prop("checked",true);
	}
	resetKenoTwoForQP();
	
	if(selectedBetName != ""){
		$("#noOfNumber").html(kenoTwoBetList[selectedBetName].min);
	}
	var numOfTextBoxes = kenoTwoBetList[selectedBetName].max;
	for(var i=1; i<=numOfTextBoxes; i++){
		$("#ktnum"+i).css("display","block");
	}
	if("Perm2" == selectedBetName || "Perm3" == selectedBetName){
		numToBePicked = parseInt($("#numToBePickedFN").val());
		var rngResp = getGameWiseRNG("KenoTwo",numToBePicked,1);
	}
	else{
		var rngResp = getGameWiseRNG("KenoTwo",numOfTextBoxes,1);
	}
	if (!rngResp) {
		$("#qpCheck").prop("checked",false);
		$("#error").html("Please check the Internet connection and try again !!!");
		erroPopUp();
		return;
	}
	
	if(rngResp.responseMsg == "success"){
		ifRandomNoGenerated(rngResp);
	}else{
		$("#error").html(rngResp.responseMsg);
		erroPopUp();
	}
	if("Perm1" != selectedBetName){
		updateCurrNoLines(numToBePicked,selectedBetName);
	}else{
		updateCurrNoLines(numOfTextBoxes,selectedBetName);
		
	}
	checkIsDrawSelected();
	updateTicketPrice(1);
	$("#numToBePickedFN").blur(); 
	
}
function ifRandomNoGenerated(rngResp){

	numbersChosen = rngResp.pickData[0].split(",");
	var h = 0;
	$(".kenoTwoNumberEnter").each(function(){
		$(this).val(numbersChosen[h]);
		h++;
	});
	h=0;
	$(numbersChosen).each(function(h){
		$("#kenoTwoNum"+(numbersChosen[h])).addClass('selectnumber');
		if(h==0){
			$("#numPicked").append(numbersChosen[h]);
		} else {
			$("#numPicked").append(" ,"+(numbersChosen[h]));				
		}
		h++;
	});
	$("#qpCheck").prop("checked",true);
	isValid = true;
	if(!isDrawManual){
		noOfDrawsSelected = alreadySelDraw.length;
		noOfSelectedDraws = alreadySelDraw;
	}else{
		noOfDrawsSelected = drawsSel;
		$("#numOfDrawsSelected").val(noOfDrawsSelected);
	}

}

function changeBetTypeKenoTwo(){
	isValid = false;
	$(".lnum-input-area").removeClass("t-box");
	$("#numOfDrawsSelected").val(noOfDrawsSelected);
	$("#qpCheck").prop("checked",false);
	$("#qpCheck").attr("disabled",false);
	$("#kenoTwoQpTextboxName").css("display","none");
	$(".kenoTwoNumber").removeClass('selectnumber');
	$(".kenoTwoNumberEnter").val("");
	$(".kenoTwoNumberEnter").css("display","none");
	numbersChosen = [];
	$("#ticketAmt").html("0");
	$("#noOfNumber").html("0");
	$("#noOfLines").html("0");
	$("#numPicked").html("");
	$("#numToBePickedFN").val("");
	$("#numToBePickedFN").prop("disabled",true);
	for(var i in kenoTwoBetList){
		$(".lnum-input-area").removeClass(i.toLowerCase()+"-block");
	}
	var numOfTextBoxes = kenoTwoBetList[selectedBetName].max;
	for(var i=1; i<=numOfTextBoxes; i++){
		$("#ktnum"+i).css("display","block");
	 }
	if("Perm1" != selectedBetName){
		$(".lnum-input-area").addClass("t-box");
	}
	else{
		$(".lnum-input-area").removeClass("t-box");
	}
	$(".lnum-input-area").addClass(selectedBetName.toLowerCase()+"-block");
	checkIsDrawSelected();
	updateTicketPrice(selectedBetName.length);
	$("#numToBePickedFN").blur(); 
	
}

function resetKenoTwo(){
	numbersChosen = [];
	$(".lnum-input-area").removeClass("t-box");
	$(".ad-draw-check-box").removeClass("select-checkbox");
	$(".ad-draw-check-box").children().prop("checked",false);
	$("#noOfAdvDraws").html("");
	$("#numOfDrawsSelected").val("1");
	isDrawManual = true;
	noOfDrawsSelected = parseInt($("#numOfDrawsSelected").val());
	drawsSel = noOfDrawsSelected;
	$("#error").html("");
	$("#error-popup").css('display','none');
	$("#numToBePickedFN").val("");
	$("#numToBePickedFN").prop('disabled',true);
	$(".unitPrice").removeClass('amt-select');
	$(".unitPrice").first().addClass("amt-select");
	betAmtSelected = parseFloat($(".unitPrice").first().text());
	$(".kenoTwoNumber").removeClass('selectnumber');
	$("#buy").attr("disabled",true);
	$("#qpCheck").prop("checked",false);
	$("#qpCheck").attr("disabled",false);
	$("#ticketAmt").html("0");
	$("#noOfNumber").html("0");
	$("#noOfLines").html("0");
	$("#numPicked").html("");
	$("#unitPrice").val(currUnitPrice);
	$("#betAmt").html(currUnitPrice);
	$(".kenoTwoNumberEnter").val("");
	$(".kenoTwoNumberEnter").css("display","none");
	var selectedGame = "";
	for(var i=1; i<=10; i++){
		$("#ktnum"+i).css("display","block");
	}
	if("Perm1" != selectedBetName){
		$(".lnum-input-area").addClass("t-box");
	}
	else{
		$(".lnum-input-area").removeClass("t-box");
	}
	$("#buy").attr("disabled",true);
	updateTicketPrice(selectedBetName.length);
	$("#numToBePickedFN").blur(); 
	
}

function updateNoOfLinesForKenoTwo(totalNum){
	if(selectedBetName == "Perm1"){
		var numOfLines = getCombinations(totalNum,1) ;
		$("#noOfLines").html(numOfLines);
	}else if(selectedBetName == "Perm2"){
		var numOfLines = getCombinations(totalNum,2) ;
		$("#noOfLines").html(numOfLines);
	}else if(selectedBetName == "Perm3"){
		var numOfLines = getCombinations(totalNum,3) ;
		$("#noOfLines").html(numOfLines);
	}else{
		$("#noOfLines").html("1");
	}
}

function resetKenoTwoForQP(){
	numbersChosen = [];
	$("#error").html("");
	$(".lnum-input-area").removeClass("t-box");
	$("#error-popup").css('display','none');
	$(".kenoTwoNumber").removeClass('selectnumber');
	$("#buy").attr("disabled",true);
	$("#ticketAmt").html("0");
	$("#noOfNumber").html("0");
	$("#noOfLines").html("0");
	$("#numPicked").html("");
	$("#betAmt").html(currUnitPrice);
	$(".kenoTwoNumberEnter").val("");
	$(".kenoTwoNumberEnter").css("display","none");
	var numOfTextBoxes = kenoTwoBetList[selectedBetName].max;
	for(var i=1; i<=numOfTextBoxes; i++){
		$("#ktnum"+i).css("display","block");
	}
	if("Perm1" != selectedBetName){
		$(".lnum-input-area").addClass("t-box");
	}
	else{
		$(".lnum-input-area").removeClass("t-box");
	}
	$("#buy").attr("disabled",true);
	checkIsDrawSelected();
	updateTicketPrice(selectedBetName.length);
	$("#numToBePickedFN").blur(); 
	
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
function ifSelectedNoIsInnumberChosenArray(selectedNumber,kenoTwoNumberid){
	toDisableCheckBoxIfQuickPickChecked();
	$(kenoTwoNumberid).removeClass("selectnumber");
	var index = numbersChosen.indexOf(selectedNumber);
	numbersChosen.splice(index,1);
	$(".kenoTwoNumberEnter").each(function(){
		if($(this).val() == selectedNumber){
			$(this).val("");
			return false;
		}
	});

}
function ifSelectedNoIsNotInnumberChosenArray(maxReqdNum,selectedNumber,kenoTwoNumberid){

	if(numbersChosen.length == maxReqdNum){
		erroPopUp();
		$("#error").html("You can select "+maxReqdNum+" numbers only!!");
		return false;
	}else{
		$("#error").html("");
		$("#error-popup").css('display','none');
		if("Perm1"!=selectedBetName && $("#qpCheck").is(":checked")){
			toDisableCheckBoxIfQuickPickChecked();
		}
		$(kenoTwoNumberid).addClass("selectnumber");
		numbersChosen.push(selectedNumber);
		$(".kenoTwoNumberEnter").each(function(){
			if($(this).val() == ""){
				$(this).val(selectedNumber);
				return false;
			}
		});
	}
}
function toDisableCheckBoxIfQuickPickChecked(){
	if($("#qpCheck").is(":checked")){
		$("#qpCheck").prop("checked",false);
		$("#numToBePickedFN").attr('disabled',true);
		$("#numToBePickedFN").val("");
		$("#kenoTwoQpTextboxName").css("display","none");
	}
}
function handlingTabWhenNoEnterInTextField(kenoTwoNumberEnterId){
	currVal = $(kenoTwoNumberEnterId).val();
	if($(kenoTwoNumberEnterId).prev('.kenoTwoNumberEnter').val() != ""){
		if ($(kenoTwoNumberEnterId).prev('.kenoTwoNumberEnter').val().replace(/(^\s+|\s+$)/g, "").length < 2) {
			$("#error").html("Please enter proper number!!");
			erroPopUp();
			$(kenoTwoNumberEnterId).prev('.kenoTwoNumberEnter').focus();
		}else{
			$(kenoTwoNumberEnterId).val(currVal);
			$("#error").html("");
			$("#error-popup").css('display','none');
		}
	}else{
		$("#error").html("Please enter proper number!!");
		erroPopUp();
		$(".kenoTwoNumberEnter").each(function(){
			if($(this).val() == ""){
				$(this).focus();
				return false;
			}
		});
	}

}
function ifEnteredNoExists(kenoTwoNumberEnterId,e){
	if((e.which == 8 || e.which == 46)){
		var index = numbersChosen.indexOf($(this).val());
		$('#kenoTwoNum'+$(kenoTwoNumberEnterId).val()+'').removeClass('selectnumber');
		numbersChosen.splice(index,1);
		$(kenoTwoNumberEnterId).val(currVal);
	}else if((e.keyCode >= 48 && e.keyCode <= 57) || (e.keyCode >= 96 && e.keyCode <= 105)){
		$(kenoTwoNumberEnterId).val(currVal);
	}
}
function ifEnteredNoNotExists(minReqdNum,totalNum){
	$("#error").html("");
	$("#error-popup").css('display','none');
	numbersChosen = [];
	$(".kenoTwoNumber").removeClass('selectnumber');
	$("#numPicked").html('');
	var i = 0;
	$(".kenoTwoNumberEnter").each(function(i){
		if($(this).val()!=""){
			numbersChosen.push($(this).val());
			$('#kenoTwoNum'+$(this).val()+'').addClass('selectnumber');
			$("#numPicked").append($(this).val());
			if(i!=totalNum-1){
				$("#numPicked").append(" ,");
			}
		}
		i++;
	});
	if(numbersChosen.length == totalNum){
		$(".unitPrice").first().focus();
	}else if(numbersChosen.length >= minReqdNum && numbersChosen.length < totalNum){
		$(".kenoTwoNumberEnter").each(function(){
			if($(this).val()==""){
				$(this).focus();
				return false;
			}
		});
		isValid = true;
	}else{
		$(".kenoTwoNumberEnter").each(function(){
			if($(this).val()==""){
				$(this).focus();
				return false;
			}
		});
		isValid = false;
		$("#buy").attr("disabled",true);
	}

}

function commonBlockForPerm123IFENteredNoAreInLimit(numToBePickedFNId){
	$("#error").html("");
	$("#error-popup").css('display','none');
	kenoTwoQP(parseInt($(numToBePickedFNId).val()));
}
function commonErrorBlockForPerm123 (){
	$("#qpCheck").prop("checked",true);
	$("#qpCheck").prop("disabled",false);
	erroPopUp();
}
function perm1IfSelectedNoNotOfCorrectLimit(perm1numToBePickedFNId){
	if($(perm1numToBePickedFNId).val().replace(/(^\s+|\s+$)/g, "")!=1){
		$(perm1numToBePickedFNId).val("");
		resetKenoTwoForQP();
		commonErrorBlockForPerm123();
		$("#error").html("Total numbers can be selected is 10");
	}
}
function erroPopUp(){
	$("#error-popup").css('display','block');	
	$('#error-popup').delay(2000).fadeOut('slow');
}
function perm2IfSelectedNoNotOfCorrectLimit(perm2numToBePickedFNId){
	if($(perm2numToBePickedFNId).val().replace(/(^\s+|\s+$)/g, "")!=1 && $(perm2numToBePickedFNId).val().replace(/(^\s+|\s+$)/g, "")!=2){
		$(perm2numToBePickedFNId).val("");
		resetKenoTwoForQP();
		commonErrorBlockForPerm123();
		$("#error").html("Total numbers can be selected between 3 to 20");
	}
}
function perm3IfSelectedNoNotOfCorrectLimit(perm3numToBePickedFNId){
	if($(perm3numToBePickedFNId).val().replace(/(^\s+|\s+$)/g, "")!=1 && $(perm3numToBePickedFNId).val().replace(/(^\s+|\s+$)/g, "")!=2){
		$(perm3numToBePickedFNId).val("");
		resetKenoTwoForQP();
		commonErrorBlockForPerm123();
		$("#error").html("Total numbers can be selected between 4 to 20");
	}
}
function prepareNumberList(){
	var k = 0;
	$("#numPicked").html('');
	$(numbersChosen).each(function(k){
		$("#numPicked").append(numbersChosen[k]);
		if(k < numbersChosen.length - 1){
			$("#numPicked").append(" ,");
		}
		k++;
	});
}