$(document).ready(function(){
	
	$(".ottManualEnter").on("keydown",function(e){
		$(".ottManualEnter").css("display","block");
		currVal = $(this).val();
		$("#tktView").css("display","none");
	    $("#gamePrizeScheme").css("display","block");
	    if(e.ctrlKey){
			e.preventDefault();
		}
	    if($.inArray($(this).val(),numbersChosen) >= 0){
			if((e.which == 8 || e.which == 46)){
				var index = numbersChosen.indexOf($(this).val());
				$('#ott'+$(this).val()+'').removeClass('selected-number');
				numbersChosen.splice(index,1);
				currVal = "";
			}else if((e.keyCode >= 48 && e.keyCode <= 57) || (e.keyCode >= 96 && e.keyCode <= 105)){
				currVal = $(this).val();
			}
		}
	});
	
	$(".ottManualEnter").on("keyup",function(e){
		$("#tktGen").html("");
		$("#error").text("");
		$("#error-popup").css('display','none');
		if($("#qpCheck").is(":checked")){
			$("#qpCheck").prop("checked",false);
			$("#numToBePicked").val("");
			$("#numToBePicked").prop("disabled",true);
		}
		if(e.which == 9){
			if(numbersChosen.length < 1){
				currVal = $(this).val();
				if($(this).first().val() != ""){
					if ($(this).first().val().replace(/(^\s+|\s+$)/g, "").length < 2) {
						$("#error").text("Please enter proper number!!");
						$("#error-popup").css('display','block');		
						$('#error-popup').delay(2000).fadeOut('slow');
						$(this).first().focus();
					}else{
						$(this).val(currVal);
						$("#error").text("");
					}
				}else{
					$("#error").text("Please enter proper number!!");
					$("#error-popup").css('display','block');		
					$('#error-popup').delay(2000).fadeOut('slow');
					$(".ottManualEnter").each(function(){
						if($(this).val() == ""){
							$(this).focus();
							return false;
						}
					});
				}
			}else if($(this).next().length == 0){
				$(".unitPrice").first().focus();
				return;
			}
		}else if (e.which == 13) {
			if (numbersChosen.length >0) {
				$(".unitPrice").first().focus();
				return;
			}
		}
		var enteredNum = $(this).val().replace(/(^\s+|\s+$)/g, "");
		if(!isNaN(enteredNum)){
			if (enteredNum.length == 2) {
				if(enteredNum >=1 && enteredNum<=12){
					if($.inArray(enteredNum,numbersChosen) >= 0){
						if((e.which == 8 || e.which == 46)){
							var index = numbersChosen.indexOf($(this).val());
							$('#ott'+$(this).val()+'').removeClass('selected-number');
							numbersChosen.splice(index,1);
							$(this).val(currVal);
						}else if((e.keyCode >= 48 && e.keyCode <= 57) || (e.keyCode >= 96 && e.keyCode <= 105)){
							$(this).val(currVal);
						}
					}else{
						if($("#qpCheck").is(":checked")){
							$("#qpCheck").prop("checked",false);
							$("#numToBePicked").val("");
							$("#numToBePicked").prop("disabled",true);
						}
						$("#error").text("");
						numbersChosen = [];
						$(".onetotwelveNum").removeClass('selected-number');
						$("#numPicked").html('');
						var i = 0;
						$(".ottManualEnter").each(function(i){
							if($(this).val()!=""){
								numbersChosen.push($(this).val());
								$('#ott'+$(this).val()+'').addClass('selected-number');
								$("#numPicked").append($(this).val());
								if(i!=4){
									$("#numPicked").append(",");
								}
							}
							i++;
						});
						if(numbersChosen.length >= 1){
							isValid = true;
							if(numbersChosen.length == 5){
								$(".unitPrice").first().focus();
							}else{
								$(this).next().focus();
							}
							$("#noOfLines").html("1");
						} else{
							isValid = false;
							$("#buy").attr("disabled",true);
						}
					}
				}else{
					$(this).val('');
				}
			}
		}else{
			$(this).val('');
		}
		$("#numPicked").html('');
		var arrLen = numbersChosen.length;
		$(numbersChosen).each(function(k){
			$("#numPicked").append(numbersChosen[k]);
			if(k < arrLen-1){
				$("#numPicked").append(",");
			}
			k++;
		});
		checkIsDrawSelected();
		$("#noOfNumber").html(numbersChosen.length);
		updateTicketPrice(selectedBetName.length);
	});
	
	$("ul li.onetotwelveNum").on("click",function(e){
		$("#tktView").css("display","none");
	    $("#gamePrizeScheme").css("display","block");
	    $(".ottManualEnter").css("display","block");
	    if($("#qpCheck").is(":checked")){
			$("#qpCheck").prop("checked",false);
			$("#numToBePicked").val("");
			$("#numToBePicked").prop("disabled",true);
		}
		var numSelected = $(this).children().first().text();
		if($.inArray(numSelected,numbersChosen) >= 0){
			$("#error").html("");
			$("#error-popup").css('display','none');
			var index = numbersChosen.indexOf(numSelected);
			$('#ott'+numSelected+'').removeClass('selected-number');
			numbersChosen.splice(index,1);
			$(".ottManualEnter").each(function(){
				if($(this).val() == numSelected){
					$(this).val("");
					return false;
				}
			});
		} else{
			if(numbersChosen.length == 5){
				$("#error").html("Maximum 5 numbers can be selected");
				$("#error-popup").css('display','block');		
				$('#error-popup').delay(2000).fadeOut('slow');
			} else{
				$("#error").html("");
				$("#error-popup").css('display','none');
				$("#ott"+numSelected).addClass("selected-number");
				numbersChosen.push(numSelected);
				$(".ottManualEnter").each(function(){
					if($(this).val() == ""){
						$(this).val(numSelected);
						return false;
					}
				});
			}
		}
		showSelNumFor1by12();
		if(numbersChosen.length > 0){
			isValid = true;
			$("#buy").attr("disabled",false);
		}else{
			isValid = false;
			$("#buy").attr("disabled",true);
		}
		checkIsDrawSelected();
		$("#noOfNumber").html(numbersChosen.length);
		updateTicketPrice(selectedBetName.length);
		
	});
	
	$("#numToBePicked").on("keyup",function(e){
		if (!(e.which == 8 || e.which == 46) && ($(this).val() == null || $(this).val().replace(/(^\s+|\s+$)/g, "").length > 1 || $(this).val().replace(/(^\s+|\s+$)/g, "") == "")) {
			$(this).val('');
			$(".onetotwelveNum").removeClass("selected-number");
			$(".ottManualEnter").val("");
			numbersChosen = [];
			$("#numPicked").html("");
			$("#error").html("Total numbers can be selected between 1 to 5");
			$("#error-popup").css('display','block');	
			$('#error-popup').delay(2000).fadeOut('slow');
			return;
		}
		if((e.which == 8 || e.which == 46) && $(this).val().replace(/(^\s+|\s+$)/g, "").length < 1){
			$(".onetotwelveNum").removeClass("selected-number");
			$(".ottManualEnter").val("");
			numbersChosen = [];
			$("#numPicked").html("");
			$("#error").html("");
			$("#error-popup").css('display','none');
			isValid = false;
			resetForQP();
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
			$("#error").html("Total numbers can be selected between 1 to 5");
			$("#error-popup").css('display','block');	
			$('#error-popup').delay(2000).fadeOut('slow');
	    }else{
			if(!isNaN($(this).val())){
				if(parseInt($(this).val().replace(/(^\s+|\s+$)/g))<1 || parseInt($(this).val().replace(/(^\s+|\s+$)/g))>5){
					$(this).val("");
					resetForQP();
					$("#numbertopicked").css("display","block");
					$("#qpCheck").prop("checked",true);
					$("#qpCheck").prop("disabled",false);
					$("#error").html("Total numbers can be selected between 1 to 5");
					$("#error-popup").css('display','block');	
					$('#error-popup').delay(2000).fadeOut('slow');
				} else{
					$("#error").html("");
					$("#error-popup").css('display','none');
					oneByTwelveQP(parseInt($(this).val().replace(/(^\s+|\s+$)/g)));
				}
			} else{
				$(this).val("");
				$("#error").html("Maximum 5 numbers can be selected");
				$("#error-popup").css('display','block');		
				$('#error-popup').delay(2000).fadeOut('slow');
			}
		}
	});
	$(document).on("keydown","#numToBePicked",function(e){
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
function showSelNumFor1by12(){
	if(numbersChosen.length > 0){
		$("#noOfLines").html("1");
		$("#numPicked").html("");
		var arrLen = numbersChosen.length;
		$(numbersChosen).each(function(k){
			$("#numPicked").append(numbersChosen[k]);
			if(k < arrLen-1){
				$("#numPicked").append(",");
			}
			k++;
		});
	} else{
		numbersChosen = [];
		$("#noOfLines").html('0');
		$("#numPicked").html("");
		$("#buy").attr("disabled",true);
	}
	updateTicketPrice(1);
}

function oneByTwelveQP(numOfNumbers){
	numbersChosen = [];
	resetForQP();
	$("#qpCheck").prop("checked",true);
	$("#noOfLines").html("1");
	var rngResp = getGameWiseRNG("OneToTwelve",numOfNumbers,1);
	if (rngResp == "NETERROR") {
		$("#error-popup").css('display','block');	
		$("#error").html("Server not reachable, Please check connection and try again !!!");
		$('#error-popup').delay(2000).fadeOut('slow');
		return;
	}
		if(rngResp.responseMsg == "success"){
			numbersChosen = rngResp.pickData[0].split(",");
			/*for(var u=1; u<=numOfNumbers; u++){
				$("#ottnum"+u).css("display","block");
			}*/
			var h = 0;
			var arrLen = numbersChosen.length;
			$(numbersChosen).each(function(h){
				$("#ott"+numbersChosen[h]).addClass('selected-number');
				$("#ottnum"+(h+1)).val(numbersChosen[h]);
				$("#numPicked").append(numbersChosen[h]);
				if(h < arrLen-1){
					$("#numPicked").append(",");
				}
				h++;
			});
			$("#noOfNumber").html(numbersChosen.length);
			isValid = true;
			if(!isDrawManual){
				noOfDrawsSelected = alreadySelDraw.length;
				noOfSelectedDraws = alreadySelDraw;
			}else{
				noOfDrawsSelected = drawsSel;
				$("#numOfDrawsSelected").val(noOfDrawsSelected);
			}
		} else{
			$("#error").html(rngResp.responseMsg);
			$("#error-popup").css('display','block');		
			$('#error-popup').delay(2000).fadeOut('slow');
		}
	
	checkIsDrawSelected();
	updateTicketPrice(1);
}

function changeBetTypeOneByTwelve(){
	resetOneByTwelveGame();
	$("#qpCheck").prop("checked",false);
	$("#qpCheck").prop("disabled",false);
	$("#noOfNumber").html(numbersChosen.length);
	
}

function resetOneByTwelveGame() {
	numbersChosen = [];
	$(".ad-draw-check-box").removeClass("select-checkbox");
	$(".ad-draw-check-box").children().prop("checked",false);
	$("#noOfAdvDraws").html("...");
	$("#numOfDrawsSelected").val("1");
	isDrawManual = true;
	noOfDrawsSelected = parseInt($("#numOfDrawsSelected").val());
	drawsSel = noOfDrawsSelected;
	$(".unitPrice").removeClass("amt-select");
	$(".unitPrice").first().addClass("amt-select");
	betAmtSelected = parseInt($(".unitPrice").first().text());
	$(".ottManualEnter").val("");
	$(".ottManualEnter").css("display","block");
	$("#noOfNumber").html("0");
	$("ticketAmt").html("0");
	$("#qpCheck").prop("checked",false);
	$("#qpCheck").prop("disabled",false);
	$("#numPicked").html("");
	$("#ottnum1").val("");
	$("#error").html("");
	$("#error-popup").css('display','none');
	$("#numToBePicked").val("");
	$("#numToBePicked").prop("disabled",true);
	//$("#numbertopicked").prop("disabled",true);
	$(".onetotwelveNum").removeClass("selected-number");
	$("#buy").prop("disabled",true);
	updateTicketPrice(1);
}

function resetForQP(){
	numbersChosen = [];
	$(".unitPrice").removeClass("amt-select");
	$(".unitPrice").first().addClass("amt-select");
	betAmtSelected = parseInt($(".unitPrice").first().text());
	$("#noOfNumber").html("0");
	$("ticketAmt").html("0");
	$("#numPicked").html("");
	$(".ottManualEnter").val("");
	$(".onetotwelveNum").removeClass("selected-number");
	$("#buy").prop("disabled",true);
	updateTicketPrice(1);
}