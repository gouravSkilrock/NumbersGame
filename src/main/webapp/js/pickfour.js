var pfpick1 = -1;
var pfpick2 = -1;
var pfpick3 = -1;
var pfpick4 = -1;
var selNo = 0;
var numSel = [0,0,0,0,0,0,0,0,0,0];
$(document).ready(function(){
	
	/**
	 * For picking 1st number
	 */
	
	$(".pick4-input").on("click",function(){
		$("#tktView").css("display","none");
	    $("#gamePrizeScheme").css("display","block");
		currVal = $(this).val();
		$(this).val(currVal);
	});
	
	$("#pfpick1").on("keydown",function(e){
		$("#tktView").css("display","none");
	    $("#gamePrizeScheme").css("display","block");
		currVal = $(this).val();
		if(e.ctrlKey){
			e.preventDefault();
		}
		if(parseInt($(this).val()) == pfpick1){
			if((e.which == 8 || e.which == 46)){
				$(this).val('');
				$("#pick4-p1num"+pfpick1).removeClass("selected-number");
				numSel[pfpick1]--;
				pfpick1 = -1;
				$("#pfpick1").focus();
				selNo--;
				$("#error").text("");
			}else if((e.keyCode >= 48 && e.keyCode <= 57) || (e.keyCode >= 96 && e.keyCode <= 105)){
				currVal = $(this).val();
			}
		}
	});
	
	$("#pfpick1").on("keyup",function(e){
		$("#tktGen").html("");
		if(e.which == 9){
			currVal = $(this).val();
			if($("#pfpick1").val() != ""){
				if ($("#pfpick1").val().trim().length < 2) {
					$("#error").text("Please enter proper number!!");
					$("#pfpick1").focus();
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
					if(parseInt($(this).val()) == pfpick1){
						if((e.keyCode >= 48 && e.keyCode <= 57) || (e.keyCode >= 96 && e.keyCode <= 105)){
							$(this).val(currVal);
						}
					}else{
						if(pfpick1!=-1){
							selNo--;
						}						
						$("#pick4-p1num"+pfpick1).removeClass("selected-number");
						pfpick1 = parseInt($(this).val()); 
						$("#pick4-p1num"+pfpick1).addClass("selected-number");
						numSel[pfpick1]++;
						selNo++;
						if($("#pfpick2").val() == "")
							$("#pfpick2").focus();
						else if($("#pfpick3").val() == "")
							$("#pfpick3").focus();
						else if($("#pfpick4").val() == "")
							$("#pfpick4").focus();
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
		showSelNumForPick4();
		$("#noOfNumber").html(selNo);
		updateTicketPrice(selectedBetName.length);
		isSelValidPF();
	});
	
	$(".pick4-num1-li-width").on("click",function(){
		$("#tktView").css("display","none");
	    $("#gamePrizeScheme").css("display","block");
		$("#tktGen").html("");
		var numSelected = $(this).text();
		if(pfpick1!=-1){
			if(numSelected == pfpick1){
				$("#error").text("");
				$("#pick4-p1num"+numSelected).removeClass("selected-number");
				$("#pfpick1").val('');
				numSel[pfpick1]--;
				pfpick1 = -1;
				selNo--;
			}else{
				$("#error").text("Only one number can be selected!!");
			}
		}else{
			$("#error").text("");
			$("#pick4-p1num"+numSelected).addClass("selected-number");
			pfpick1 = numSelected;
			numSel[pfpick1]++;
			selNo++;
			$("#pfpick1").val("0"+pfpick1);
		}
		showSelNumForPick4();
		$("#noOfNumber").html(selNo);
		updateTicketPrice(selectedBetName.length);
		isSelValidPF();
	});
	
	/**
	 * For picking 2nd number
	 */
	
	$("#pfpick2").on("keydown",function(e){
		$("#tktView").css("display","none");
	    $("#gamePrizeScheme").css("display","block");
		currVal = $(this).val();
		if(e.ctrlKey){
			e.preventDefault();
		}
		if(parseInt($(this).val()) == pfpick2){
			if((e.which == 8 || e.which == 46)){
				$(this).val('');
				$("#pick4-p2num"+pfpick2).removeClass("selected-number");
				numSel[pfpick2]--;
				pfpick2 = -1;
				$("#pfpick2").focus();
				selNo--;
				$("#error").text("");
			}else if((e.keyCode >= 48 && e.keyCode <= 57) || (e.keyCode >= 96 && e.keyCode <= 105)){
				currVal = $(this).val();
			}
		}
	});
	
	$("#pfpick2").on("keyup",function(e){
		$("#tktGen").html("");
		if(e.which == 9){
			currVal = $(this).val();
			if($("#pfpick2").val() != ""){
				if ($("#pfpick2").val().trim().length < 2) {
					$("#error").text("Please enter proper number!!");
					$("#pfpick2").focus();
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
					if(parseInt($(this).val()) == pfpick2){
						if((e.keyCode >= 48 && e.keyCode <= 57) || (e.keyCode >= 96 && e.keyCode <= 105)){
							$(this).val(currVal);
						}
					}else{
						if(pfpick2!=-1){
							selNo--;
						}						
						$("#pick4-p2num"+pfpick2).removeClass("selected-number");
						pfpick2 = parseInt($(this).val()); 
						$("#pick4-p2num"+pfpick2).addClass("selected-number");
						numSel[pfpick2]++;
						selNo++;
						if($("#pfpick1").val() == "")
							$("#pfpick1").focus();
						else if($("#pfpick3").val() == "")
							$("#pfpick3").focus();
						else if($("#pfpick4").val() == "")
							$("#pfpick4").focus();
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
		showSelNumForPick4();
		$("#noOfNumber").html(selNo);
		updateTicketPrice(selectedBetName.length);
		isSelValidPF();
	});
	
	$(".pick4-num2-li-width").on("click",function(){
		$("#tktView").css("display","none");
	    $("#gamePrizeScheme").css("display","block");
		$("#tktGen").html("");
		var numSelected = $(this).text();
		if(pfpick2!=-1){
			if(numSelected == pfpick2){
				$("#error").text("");
				$("#pick4-p2num"+numSelected).removeClass("selected-number");
				$("#pfpick2").val('');
				numSel[pfpick2]--;
				pfpick2 = -1;
				selNo--;
			}else{
				$("#error").text("Only one number can be selected!!");
			}
		}else{
			$("#error").text("");
			$("#pick4-p2num"+numSelected).addClass("selected-number");
			pfpick2 = numSelected;
			numSel[pfpick2]++;
			selNo++;
			$("#pfpick2").val("0"+pfpick2);
		}
		showSelNumForPick4();
		$("#noOfNumber").html(selNo);
		updateTicketPrice(selectedBetName.length);
		isSelValidPF();
	});
	
	/**
	 * For picking 3rd number
	 */
	
	$("#pfpick3").on("keydown",function(e){
		$("#tktView").css("display","none");
	    $("#gamePrizeScheme").css("display","block");
		currVal = $(this).val();
		if(e.ctrlKey){
			e.preventDefault();
		}
		if(parseInt($(this).val()) == pfpick3){
			if((e.which == 8 || e.which == 46)){
				$(this).val('');
				$("#pick4-p3num"+pfpick3).removeClass("selected-number");
				numSel[pfpick3]--;
				pfpick3 = -1;
				$("#pfpick3").focus();
				selNo--;
				$("#error").text("");
			}else if((e.keyCode >= 48 && e.keyCode <= 57) || (e.keyCode >= 96 && e.keyCode <= 105)){
				currVal = $(this).val();
			}
		}
	});
	
	$("#pfpick3").on("keyup",function(e){
		$("#tktGen").html("");
		if(e.which == 9){
			currVal = $(this).val();
			if($("#pfpick3").val() != ""){
				if ($("#pfpick3").val().trim().length < 2) {
					$("#error").text("Please enter proper number!!");
					$("#pfpick3").focus();
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
					if(parseInt($(this).val()) == pfpick3){
						if((e.keyCode >= 48 && e.keyCode <= 57) || (e.keyCode >= 96 && e.keyCode <= 105)){
							$(this).val(currVal);
						}
					}else{
						if(pfpick3!=-1){
							selNo--;
						}						
						$("#pick4-p3num"+pfpick3).removeClass("selected-number");
						pfpick3 = parseInt($(this).val()); 
						$("#pick4-p3num"+pfpick3).addClass("selected-number");
						numSel[pfpick3]++;
						selNo++;
						if($("#pfpick1").val() == "")
							$("#pfpick1").focus();
						else if($("#pfpick2").val() == "")
							$("#pfpick2").focus();
						else if($("#pfpick4").val() == "")
							$("#pfpick4").focus();
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
		showSelNumForPick4();
		$("#noOfNumber").html(selNo);
		updateTicketPrice(selectedBetName.length);
		isSelValidPF();
	});
	
	$(".pick4-num3-li-width").on("click",function(){
		$("#tktView").css("display","none");
	    $("#gamePrizeScheme").css("display","block");
		$("#tktGen").html("");
		var numSelected = $(this).text();
		if(pfpick3!=-1){
			if(numSelected == pfpick3){
				$("#error").text("");
				$("#pick4-p3num"+numSelected).removeClass("selected-number");
				$("#pfpick3").val('');
				numSel[pfpick3]--;
				pfpick3 = -1;
				selNo--;
			}else{
				$("#error").text("Only one number can be selected!!");
			}
		}else{
			$("#error").text("");
			$("#pick4-p3num"+numSelected).addClass("selected-number");
			pfpick3 = numSelected;
			numSel[pfpick3]++;
			selNo++;
			$("#pfpick3").val("0"+pfpick3);
		}
		showSelNumForPick4();
		$("#noOfNumber").html(selNo);
		updateTicketPrice(selectedBetName.length);
		isSelValidPF();
	});
	
	/**
	 * For picking 4th number
	 */
	
	$("#pfpick4").on("keydown",function(e){
		$("#tktView").css("display","none");
	    $("#gamePrizeScheme").css("display","block");
		currVal = $(this).val();
		if(e.ctrlKey){
			e.preventDefault();
		}
		if(parseInt($(this).val()) == pfpick4){
			if((e.which == 8 || e.which == 46)){
				$(this).val('');
				$("#pick4-p4num"+pfpick4).removeClass("selected-number");
				numSel[pfpick4]--;
				pfpick4 = -1;
				$("#pfpick4").focus();
				selNo--;
				$("#error").text("");
			}else if((e.keyCode >= 48 && e.keyCode <= 57) || (e.keyCode >= 96 && e.keyCode <= 105)){
				currVal = $(this).val();
			}
		}
	});
	
	$("#pfpick4").on("keyup",function(e){
		$("#tktGen").html("");
		if(e.which == 9){
			currVal = $(this).val();
			if($("#pfpick4").val() != ""){
				if ($("#pfpick4").val().trim().length < 2) {
					$("#error").text("Please enter proper number!!");
					$("#pfpick4").focus();
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
					if(parseInt($(this).val()) == pfpick4){
						if((e.keyCode >= 48 && e.keyCode <= 57) || (e.keyCode >= 96 && e.keyCode <= 105)){
							$(this).val(currVal);
						}
					}else{
						if(pfpick4!=-1){
							selNo--;
						}						
						$("#pick4-p4num"+pfpick4).removeClass("selected-number");
						pfpick4 = parseInt($(this).val()); 
						$("#pick4-p4num"+pfpick4).addClass("selected-number");
						numSel[pfpick4]++;
						selNo++;
						if($("#pfpick1").val() == "")
							$("#pfpick1").focus();
						else if($("#pfpick2").val() == "")
							$("#pfpick2").focus();
						else if($("#pfpick3").val() == "")
							$("#pfpick3").focus();
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
		showSelNumForPick4();
		$("#noOfNumber").html(selNo);
		updateTicketPrice(selectedBetName.length);
		isSelValidPF();
	});
	
	$(".pick4-num4-li-width").on("click",function(){
		$("#tktView").css("display","none");
	    $("#gamePrizeScheme").css("display","block");
		$("#tktGen").html("");
		var numSelected = $(this).text();
		if(pfpick4!=-1){
			if(numSelected == pfpick4){
				$("#error").text("");
				$("#pick4-p4num"+numSelected).removeClass("selected-number");
				$("#pfpick4").val('');
				numSel[pfpick4]--;
				pfpick4 = -1;
				selNo--;
			}else{
				$("#error").text("Only one number can be selected!!");
			}
		}else{
			$("#error").text("");
			$("#pick4-p4num"+numSelected).addClass("selected-number");
			pfpick4 = numSelected;
			numSel[pfpick4]++;
			selNo++;
			$("#pfpick4").val("0"+pfpick4);
		}
		showSelNumForPick4();
		$("#noOfNumber").html(selNo);
		updateTicketPrice(selectedBetName.length);
		isSelValidPF();
	});
	
});

function resetPickFourGame(){
	$("#error").html("");
	$("#pfpick1").val('');
	$("#pfpick2").val('');
	$("#pfpick3").val('');
	$("#pfpick4").val('');
	$(".unitPrice").removeClass('amt-select');
	betAmtSelected = 0;
	$(".pick4-num1-li-width").removeClass('selected-number');
	$(".pick4-num2-li-width").removeClass('selected-number');
	$(".pick4-num3-li-width").removeClass('selected-number');
	$(".pick4-num4-li-width").removeClass('selected-number');
	$("#parentApplet").css("display","none");
	pfpick1=-1;
	pfpick2=-1;
	pfpick3=-1;
	pfpick4=-1;
	numSel = [0,0,0,0,0,0,0,0,0,0];
	selNo = 0;
	numbersChosen = [];
	$("#tbtSelFreeze3").css("display","none");
	$("#buy").attr("disabled",true);
	/*$("#qpCheck").css("display","block");
	$("#qpHead").css('visibility', 'visible');
	$("#qpCheck").prop("checked",false);
	$("#pickFourNumberWrapper").css("display","block");*/
	$("#numSelForP4").css("display","block");
	$("#numbersSelForPick4").html('');
	$("ticketAmt").html("0");
	$("#noOfNumber").html("0");
	$("#noOfLines").html("0");
	$("#numPicked").html("");
	$("#unitPrice").val(currUnitPrice);
	$("#betAmt").html(currUnitPrice);
	updateTicketPrice(selectedBetName.length);
}
	
function pickFourQP(){
	if($("#qpCheck").is(":checked")){
		resetPickFourGame();
		$("#pickFourNumberWrapper").css("display","none");
		$("#numSelForP4").css("display","none");
		$("#qpCheck").prop("checked",true);
		$("#tbtSelFreeze3").css("display","block");
		$("#numPicked").html("QP");
		if( selectedBetName == "Combination"){
			$("#noOfNumber").html("4");
			$("#noOfLines").html("24");
		}else if(selectedBetName == "StraightBox"){
			$("#noOfNumber").html("4");
			$("#noOfLines").html("1");
		}else {
			$("#noOfNumber").html("4");
			$("#noOfLines").html("1");
		}
		isValid = true;
		checkIsDrawSelected();
		updateTicketPrice(selectedBetName.length);
	}else{
		isValid = false;
		resetPickFourGame();
		$("#pickFourNumberWrapper").css("display","block");
		$("#numSelForP4").css("display","block");
		$("#numPicked").html("");
		$("#tbtSelFreeze3").css("display","none");
		$("#buy").attr("disabled",true);
	}
}

function showSelNumForPick4(){
	$("#numPicked").html("");
	$("#numbersSelForPick4").html("");
	numbersChosen = [];
	if(pfpick1!=-1){
		$("#numPicked").append("0"+pfpick1);
		$("#numbersSelForPick4").append("0"+pfpick1);
		numbersChosen.push(pfpick1);
	}
	if(pfpick2!=-1){
		if(pfpick1 == -1){
			$("#numPicked").append("0"+pfpick2);
			$("#numbersSelForPick4").append("0"+pfpick2);
		}else{
			$("#numPicked").append(",0"+pfpick2);
			$("#numbersSelForPick4").append(",0"+pfpick2);
		}
		numbersChosen.push(pfpick2);
	}
	if(pfpick3!=-1){
		if(pfpick1 == -1 && pfpick2 == -1){
			$("#numPicked").append("0"+pfpick3);
			$("#numbersSelForPick4").append("0"+pfpick3);
		}else{
			$("#numPicked").append(",0"+pfpick3);
			$("#numbersSelForPick4").append(",0"+pfpick3);	
		}
		numbersChosen.push(pfpick3);
	}
	if(pfpick4!=-1){
		if(pfpick1 == -1 && pfpick2 == -1 && pfpick3 == -1){
			$("#numPicked").append("0"+pfpick4);
			$("#numbersSelForPick4").append("0"+pfpick4);
		}else{
			$("#numPicked").append(",0"+pfpick4);
			$("#numbersSelForPick4").append(",0"+pfpick4);	
		}
		numbersChosen.push(pfpick4);
	}
	updateNoOfLinesPickFour();
	if(pfpick1!=-1 && pfpick2!=-1 && pfpick3!=-1 && pfpick4!=-1){
		isValid = true;
		checkIsDrawSelected();
	}else{
		isValid = false;
		$("#noOfLines").html("0");
		$("#buy").attr("disabled",true);
	}
}

function changeBetTypePickFour(){
	resetPickFourGame();
/*	$("#qpCheck").css("display","block");
	$("#qpHead").css('visibility', 'visible');
	$("#qpCheck").prop("checked",false);
	$("#noOfNumber").html(numbersChosen.length);*/
}

function updateNoOfLinesPickFour(){
	if( selectedBetName == "Combination"){
		var i = 0;
		var count1 = 0,count2 = 0,count3 = 0;
		$(numSel).each(function(i){
			if(numSel[i] == 1){
				count1++;
			}else if(numSel[i] == 2){
				count2++;
			}else if(numSel[i] == 3){
				count3++;
			}
			i++;	
		});
		if(count1 == 4){
			$("#noOfLines").html("24");
		}else if(count2 == 2){
			$("#noOfLines").html("6");
		}else if(count3 == 1){
			$("#noOfLines").html("4");
		}else if(count2 == 1 && count1 == 2){
			$("#noOfLines").html("12");
		}
	}else if(selectedBetName == "StraightBox"){
		$("#noOfLines").html("1");
	}else {
		$("#noOfLines").html("1");
	}
}

function isSelValidPF(){
	if(pfpick1!=-1 && pfpick2!=-1 && pfpick3!=-1 && pfpick4!=-1){
		if("Box" == selectedBetName || "StraightBox" == selectedBetName || "Combination" == selectedBetName){
			if(pfpick1 == pfpick2 && pfpick2 == pfpick3 && pfpick3 == pfpick4){
				$("#error").html("All numbers cannot be same for this bet type!!");
				$("#buy").attr("disabled",true);
			}
		} else {
			$("#error").html("");
			$("#buy").attr("disabled",false);
		}
	}	
}