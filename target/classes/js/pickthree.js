var pick1 = -1;
var pick2 = -1;
var pick3 = -1;
var selNo = 0;
$(document).ready(function(){
	
	$(".pick-input").on("click",function(){
		$("#tktView").css("display","none");
	    $("#gamePrizeScheme").css("display","block");
		currVal = $(this).val();
		$(this).val(currVal);
	});
	
	/**
	 * For picking 1st number
	 */
	
	$("#pick1").on("keydown",function(e){
		$("#tktView").css("display","none");
	    $("#gamePrizeScheme").css("display","block");
		currVal = $(this).val();
		if(e.ctrlKey){
			e.preventDefault();
		}
		if(parseInt($(this).val()) == pick1){
			if((e.which == 8 || e.which == 46)){
				$(this).val('');
				$("#p1num"+pick1).removeClass("selected-number");
				pick1 = -1;
				$("#pick1").focus();
				selNo--;
				$("#error").text("");
			}else if((e.keyCode >= 48 && e.keyCode <= 57) || (e.keyCode >= 96 && e.keyCode <= 105)){
				currVal = $(this).val();
			}
		}
	});
	
	$("#pick1").on("keyup",function(e){
		$("#tktGen").html("");
		if(e.which == 9){
			currVal = $(this).val();
			if($("#pick1").val() != ""){
				if ($("#pick1").val().trim().length < 2) {
					$("#error").text("Please enter proper number!!");
					$("#pick1").focus();
				}else{
					$(this).val(currVal);
					$("#error").text("");
				}
			}else{
				$("#error").text("Please enter number!!");
			}
		}
		var enteredNum = $(this).val().trim();
		if(!isNaN(enteredNum)){
			if(enteredNum.length == this.maxLength) {
				if(enteredNum >=0 && enteredNum<=9 && enteredNum!=""){
					if(parseInt($(this).val()) == pick1){
						if((e.keyCode >= 48 && e.keyCode <= 57) || (e.keyCode >= 96 && e.keyCode <= 105)){
							$(this).val(currVal);
						}
					}else{
						if(pick1!=-1)
							selNo--;
						$("#p1num"+pick1).removeClass("selected-number");
						pick1 = parseInt($(this).val()); 
						$("#p1num"+pick1).addClass("selected-number");
						selNo++;
						if($("#pick2").val() == "")
							$("#pick2").focus();
						else if($("#pick3").val() == "")
							$("#pick3").focus();
						else
							$("#unitPrice").focus();
					}
				}else{
					$(this).val('');
				}
			}
		}else{
			$(this).val('');
		}
		showSelNum();
		$("#noOfNumber").html(selNo);
		updateTicketPrice(selectedBetName.length);
		isSelValid();
	});
	
	$("ul li.num1-li-width").on("click",function(e){
		$("#tktView").css("display","none");
	    $("#gamePrizeScheme").css("display","block");
		$("#tktGen").html("");
		if(selectedBetName == "BackPair"){
			e.preventDefault();
		}else{
			var numSelected = $(this).text();
			if(pick1!=-1){
				if(numSelected == pick1){
					$("#error").text("");
					$("#p1num"+numSelected).removeClass("selected-number");
					$("#pick1").val('');
					pick1 = -1;
					selNo--;
				}else{
					$("#error").text("Only one number can be selected!!");
				}
			}else{
				$("#error").text("");
				$("#p1num"+numSelected).addClass("selected-number");
				pick1 = numSelected;
				selNo++;
				$("#pick1").val("0"+pick1);
			}
		}
		showSelNum();
		$("#noOfNumber").html(selNo);
		updateTicketPrice(selectedBetName.length);
		isSelValid();
	});
	
	/**
	 * For picking 2nd number
	 */
	
	$("#pick2").on("keydown",function(e){
		$("#tktView").css("display","none");
	    $("#gamePrizeScheme").css("display","block");
		currVal = $(this).val();
		if(e.ctrlKey){
			e.preventDefault();
		}
		if(parseInt($(this).val()) == pick2){
			if((e.which == 8 || e.which == 46)){
				$(this).val('');
				$("#p2num"+pick2).removeClass("selected-number");
				pick2 = -1;
				selNo--;
				$("#pick2").focus();
				$("#error").text("");
			}else if((e.keyCode >= 48 && e.keyCode <= 57) || (e.keyCode >= 96 && e.keyCode <= 105)){
				currVal = $(this).val();
			}
		}
	});
	
	$("#pick2").on("keyup",function(e){
		$("#tktGen").html("");
		if(e.which == 9){
			currVal = $(this).val();
			if($("#pick2").val() != ""){
				if ($("#pick2").val().trim().length < 2) {
					$("#error").text("Please enter proper number!!");
					$("#pick2").focus();
				}else{
					$(this).val(currVal);
					$("#error").text("");
				}
			}else{
				$("#error").text("Please enter number!!");
			}
		}
		var enteredNum = $(this).val().trim();
		if(!isNaN(enteredNum)){
			if(enteredNum.length == this.maxLength) {
				if(enteredNum >=0 && enteredNum<=9 && enteredNum!=""){
					if(parseInt($(this).val()) == pick2){
						if((e.keyCode >= 48 && e.keyCode <= 57) || (e.keyCode >= 96 && e.keyCode <= 105)){
							$(this).val(currVal);
						}
					}else{
						if(pick1!=-2)
							selNo--;
						$("#p2num"+pick2).removeClass("selected-number");
						pick2 = parseInt($(this).val()); 
						$("#p2num"+pick2).addClass("selected-number");
						selNo++;
						if($("#pick1").val() == "")
							$("#pick1").focus();
						else if($("#pick3").val() == "")
							$("#pick3").focus();
						else
							$("#unitPrice").focus();
					}
				}else{
					$(this).val('');
				}
			}
		}else{
			$(this).val('');
		}
		showSelNum();
		$("#noOfNumber").html(selNo);
		updateTicketPrice(selectedBetName.length);
		isSelValid();
	});
	
	$(".num2-li-width").on("click",function(){
		$("#tktView").css("display","none");
	    $("#gamePrizeScheme").css("display","block");
		$("#tktGen").html("");
		var numSelected = $(this).text();
		if(pick2!=-1){
			if(numSelected == pick2){
				$("#error").text("");
				$("#p2num"+numSelected).removeClass("selected-number");
				$("#pick2").val('');
				pick2 = -1;
				selNo--;
			}else{
				$("#error").text("Only one number can be selected!!");
			}
		}else{
			$("#error").text("");
			$("#p2num"+numSelected).addClass("selected-number");
			pick2 = numSelected;
			selNo++;
			$("#pick2").val("0"+pick2);
		}
		showSelNum();
		$("#noOfNumber").html(selNo);
		updateTicketPrice(selectedBetName.length);
		isSelValid();
	});
	
	/**
	 * For picking 2nd number
	 */
	
	$("#pick3").on("keydown",function(e){
		$("#tktView").css("display","none");
	    $("#gamePrizeScheme").css("display","block");
		currVal = $(this).val();
		if(e.ctrlKey){
			e.preventDefault();
		}
		if(parseInt($(this).val()) == pick3){
			if((e.which == 8 || e.which == 46)){	
				$(this).val('');
				$("#p3num"+pick3).removeClass("selected-number");
				pick3 = -1;
				selNo--;
				$("#pick3").focus();
				$("#error").text("");
			}else if((e.keyCode >= 48 && e.keyCode <= 57) || (e.keyCode >= 96 && e.keyCode <= 105)){
				currVal = $(this).val();
			}
		}
	});
	
	$("#pick3").on("keyup",function(e){
		$("#tktGen").html("");
		if(e.which == 9){
			currVal = $(this).val();
			if($("#pick3").val() != ""){
				if ($("#pick3").val().trim().length < 2) {
					$("#error").text("Please enter proper number!!");
					$("#pick3").focus();
				}else{
					$(this).val(currVal);
					$("#error").text("");
				}
			}else{
				$("#error").text("Please enter number!!");
			}
		}
		var enteredNum = $(this).val().trim();
		if(e.which == 9){
			var num2 = $("#pick2").val(); 
			$("#pick2").val(num2);
		}
		if(!isNaN(enteredNum)){
			if(enteredNum.length == this.maxLength) {
				if(enteredNum >=0 && enteredNum<=9 && enteredNum!=""){
					if(parseInt($(this).val()) == pick3){
						if((e.keyCode >= 48 && e.keyCode <= 57) || (e.keyCode >= 96 && e.keyCode <= 105)){
							$(this).val(currVal);
						}
					}else{
						if(pick3!=-1)
							selNo--;
						$("#p3num"+pick3).removeClass("selected-number");
						pick3 = parseInt($(this).val());
						$("#p3num"+pick3).addClass("selected-number");
						selNo++;
						if($("#pick1").val() == "")
							$("#pick1").focus();
						else if($("#pick2").val() == "")
							$("#pick2").focus();
						else
							$("#unitPrice").focus();
					}
				}else{
					$(this).val('');
				}
			}
		}else{
			$(this).val('');
		}
		showSelNum();
		$("#noOfNumber").html(selNo);
		updateTicketPrice(selectedBetName.length);
		isSelValid();
	});
	
	$(".num3-li-width").on("click",function(e){
		$("#tktView").css("display","none");
	    $("#gamePrizeScheme").css("display","block");
		$("#tktGen").html("");
		if(selectedBetName == "FrontPair"){
			e.preventDefault();
		}else{
			var numSelected = $(this).text();
			if(pick3!=-1){
				if(numSelected == pick3){
					$("#error").text("");
					$("#p3num"+numSelected).removeClass("selected-number");
					$("#pick3").val('');
					pick3 = -1;
					selNo--;
				}else{
					$("#error").text("Only one number can be selected!!");
				}
			}else{
				$("#error").text("");
				$("#p3num"+numSelected).addClass("selected-number");
				pick3 = numSelected;
				selNo++;
				$("#pick3").val("0"+pick3);
			}
		}
		showSelNum();
		$("#noOfNumber").html(selNo);
		updateTicketPrice(selectedBetName.length);
		isSelValid();
	});
	
	$("#betAmt").on("keydown",function(e){
		if(e.ctrlKey){
			e.preventDefault();
		}
	});
});

/**
 * Shows selected numbers
 */
function showSelNum(){
	var numSelected = $("#numbersSelForPick3").text();
	$("#numPicked").html("");
	$("#numbersSelForPick3").html("");
	numbersChosen = [];
	if(pick1!=-1){
		$("#numPicked").append("0"+pick1);
		$("#numbersSelForPick3").append("0"+pick1);
		numbersChosen.push(pick1);
	}
	if(pick2!=-1){
		if(pick1 == -1){
			$("#numPicked").append("0"+pick2);
			$("#numbersSelForPick3").append("0"+pick2);
		}else{
			$("#numPicked").append(",0"+pick2);
			$("#numbersSelForPick3").append(",0"+pick2);
		}
		numbersChosen.push(pick2);
	}
	if(pick3!=-1){
		if(pick1 == -1 && pick2 == -1){
			$("#numPicked").append("0"+pick3);
			$("#numbersSelForPick3").append("0"+pick3);
		}else{
			$("#numPicked").append(",0"+pick3);
			$("#numbersSelForPick3").append(",0"+pick3);	
		}
		numbersChosen.push(pick3);
	}
	updateNoOfLinesPickThree();
	if(selectedBetName == "FrontPair"){
		if(pick1 != -1 && pick2 != -1){
			isValid = true;
			checkIsDrawSelected();
		}else{
			isValid = false;
			$("#noOfLines").html("0");
			$("#buy").attr("disabled",true);
		}
	}else if(selectedBetName == "BackPair"){
		if(pick2 != -1 && pick3 != -1){
			isValid = true;
			checkIsDrawSelected();
		}else{
			isValid = false;
			$("#noOfLines").html("0");
			$("#buy").attr("disabled",true);
		}
	}else if(selectedBetName != "FrontPair" || selectedBetName != "BackPair"){
		if(pick1!=-1 && pick2!=-1 && pick3!=-1){
			isValid = true;
			checkIsDrawSelected();
		}else{
			isValid = false;
			$("#noOfLines").html("0");
			$("#buy").attr("disabled",true);
		}
	} 
}

/**
 * Resets the game.
 */
function resetPickThreeGame(){
	$("#pick1").val('');
	$("#pick2").val('');
	$("#pick3").val('');
	$("#pick1").attr("disabled",false);
	$("#pick3").attr("disabled",false);
	$(".unitPrice").removeClass('amt-select');
	betAmtSelected = 0;
	$("#pickOneDisable").removeClass('selected-sec-active');
	$("#pickThreeDisable").removeClass('selected-sec-active');
	$("#parentApplet").css("display","none");
	$(".num1-li-width").removeClass('selected-number');
	$(".num2-li-width").removeClass('selected-number');
	$(".num3-li-width").removeClass('selected-number');
	$(".num1-li-width").attr("disabled",false);
	$(".num3-li-width").attr("disabled",false);
	pick1=-1;
	pick2=-1;
	pick3=-1;
	selNo = 0;
	numbersChosen = [];
	$("#error").html("");
	$("#tbtSelFreeze2").css("display","none");
	$("#buy").attr("disabled",true);/*
	$("#qpCheck").css("display","block");
	$("#qpHead").css('visibility', 'visible');
	$("#qpCheck").prop("checked",false);
	$("#pickThreeNumberWrapper").css("display","block");*/
	$("#numSelForP3").css("display","block");
	$("#numbersSelForPick3").html('');
	$("#noOfNumber").html("0");
	$("#numPicked").html("");
	$("#noOfLines").html("0");
	$("ticketAmt").html("0");
	$("#unitPrice").val(currUnitPrice);
	$("#betAmt").html(currUnitPrice);
	$(".pick-number").html("");
	$("#buy").attr("disabled",true);
	updateTicketPrice(selectedBetName.length);
}
	
function pickThreeQP(){
	if($("#qpCheck").is(":checked")){
		resetPickThreeGame();
		$("#pickThreeNumberWrapper").css("display","none");
		$("#numSelForP3").css("display","none");
		$("#numPicked").html("QP");
		$("#tbtSelFreeze2").css("display","block");
		$("#qpCheck").prop("checked",true);
		$("#buy").attr("disabled",false);
		$("#pickOneDisable").removeClass('selected-sec-active');
		$("#pickThreeDisable").removeClass('selected-sec-active');
		if(selectedBetName == "Combination"){
			$("#noOfNumber").html("3");
			$("#noOfLines").html("6");
		}else if(selectedBetName == "StraightBox"){
			$("#noOfNumber").html("3");
			$("#noOfLines").html("1");
		}else if(selectedBetName == "FrontPair"){
			$("#pickThreeDisable").addClass('selected-sec-active');
			$("#pickOneDisable").removeClass('selected-sec-active');
			$("#pick3").val("x");
			$("#pick3").attr("disabled",true);
			$("#noOfNumber").html("2");
			$("#noOfLines").html("1");
		}else if(selectedBetName == "BackPair"){
			$("#pickOneDisable").addClass('selected-sec-active');
			$("#pickThreeDisable").removeClass('selected-sec-active');
			$("#pick1").val("x");
			$("#pick1").attr("disabled",true);
			$("#noOfNumber").html("2");
			$("#noOfLines").html("1");
		}else{
			$("#noOfNumber").html("3");
			$("#noOfLines").html("1");
		}
		isValid = true;
		checkIsDrawSelected();
		updateTicketPrice(selectedBetName.length);
	}else{
		isValid = false;
		resetPickThreeGame();
		if(selectedBetName == "FrontPair"){
			$("#pickThreeDisable").addClass('selected-sec-active');
			$("#pickOneDisable").removeClass('selected-sec-active');
			$("#pick3").val("x");
			$("#pick3").attr("disabled",true);
		}else if(selectedBetName == "BackPair"){
			$("#pickOneDisable").addClass('selected-sec-active');
			$("#pickThreeDisable").removeClass('selected-sec-active');
			$("#pick1").val("x");
			$("#pick1").attr("disabled",true);
		}
		$("#pickThreeNumberWrapper").css("display","block");
		$("#numSelForP3").css("display","block");
		$("#numPicked").html("");
		$("#tbtSelFreeze2").css("display","none");
		$(".pick-number").html("");
		$("#buy").attr("disabled",true);
	}
}

function changeBetTypePickThree(){
	resetPickThreeGame();
/*	$("#qpCheck").css("display","block");
	$("#qpHead").css('visibility', 'visible');
	$("#qpCheck").prop("checked",false);
	$("#noOfNumber").html("0");*/
	if(selectedBetName == "FrontPair"){
		$("#pickThreeDisable").addClass('selected-sec-active');
		$("#pickOneDisable").removeClass('selected-sec-active');
		$("#pick3").val("x");
		$("#pick3").attr("disabled",true);
	}else if(selectedBetName == "BackPair"){
		$("#pickOneDisable").addClass('selected-sec-active');
		$("#pickThreeDisable").removeClass('selected-sec-active');
		$("#pick1").val("x");
		$("#pick1").attr("disabled",true);
	}else{ 
		$("#pickOneDisable").removeClass('selected-sec-active');
		$("#pickThreeDisable").removeClass('selected-sec-active');
		$(".num1-li-width").attr("disabled",false);
		$("#pick1").val("");
		$("#pick1").attr("disabled",false);
		$(".num3-li-width").attr("disabled",false);
		$("#pick3").val("");
		$("#pick3").attr("disabled",false);
	}
}

function updateNoOfLinesPickThree(){
	if(selectedBetName == "Combination"){
		if(pick1 != pick2 && pick2 != pick3 && pick1 != pick3){
			$("#noOfLines").html("6");
		}else if(pick1 == pick2 || pick1 == pick3 || pick3 == pick2){
			$("#noOfLines").html("3");
		}
	}else if(selectedBetName == "FrontPair"){
		if(pick1 != -1 && pick2 != -1){
			$("#noOfLines").html("1");
		}
	}else if(selectedBetName == "BackPair"){
		if(pick2 != -1 && pick3 != -1){
			$("#noOfLines").html("1");
		}
	}else if(selectedBetName == "StraightBox"){
		$("#noOfLines").html("1");
	}else{
		$("#noOfLines").html("1");
	}
}

function isSelValid(){
	if(pick1!=-1 && pick2!=-1 && pick3!=-1){
		if("Box" == selectedBetName || "StraightBox" == selectedBetName || "Combination" == selectedBetName){
			if(pick1 == pick2 && pick1 == pick3){
				$("#error").html("All numbers cannot be same for this bet type!!");
				$("#buy").attr("disabled",true);
			}
		} else {
			$("#error").html("");
			$("#buy").attr("disabled",false);
		}
	}	
}
