var zimLottoBonus = {
		"Direct6" : {
			"min" : 1,
			"max" : 4,
			"maxBetMul" : null
		},
		"Perm6" : {
			"min" : 7,
			"max" : 15,
			"maxBetMul" : null
		}
	}; 


var lineNum = 1;
var count=1;
var numbersChosen=[];
var numberSelected=[];
var tempNum=0;
$(document).ready(function(){
	

/**
 * Manual entry on clicking manual numbers
 */
$(document).on("click",".bonusLottoNumber",function(){
	if($("#qpCheck").is(":checked")){
		$("#qpCheck").prop("checked",false);
		$("#zimQpTextbox").val("");
		$("#zimQpTextboxName").empty();
		$("#zimQpTextboxName").css("display","none");
		$("#zimQpTextbox").attr("disabled",true);
	}
	isValid=false;
	//$("#numPicked").html("");
	if("Direct6"==selectedBetName){
		$(".nu-input-area").removeClass("active");
		var minLineReq=zimLottoBonus[selectedBetName].min;
		var maxLineReq=zimLottoBonus[selectedBetName].max;
		var noSelected = pmsParseInt($(this).children().text());
		var noText = $(this).text();
		if(noText.length==2 && numberSelected.length!=4){
			$("#buy").attr("disabled",true);
			if($.inArray(noText,numbersChosen)<0){
				$(this).each(function(){
					var index = numbersChosen.indexOf($(this).text());
					if($(this).hasClass("selectnumber")){
						$(this).removeClass("selectnumber");
						numbersChosen.splice(index,1);
						$("#zbl"+(index+1)).val("");
						$(this).focus();
					}
					else{
						if(numbersChosen.length<6){
							$(this).addClass("selectnumber");
							numbersChosen.push(noText);
							numbersChosen.length<2?$("#numPicked").append(noText):$("#numPicked").append(","+noText);
							//$("#numPicked").html(","+numbersChosen);
							$(".lineNum"+lineNum).each(function(){
								if($(this).val() == ""){
									$(this).val(noText);
									return false;
								}
							});
							$(".lineNum"+(lineNum)).each(function(){
								if($(this).val()==""){
									$(this).focus();
									return false;
								}
							});
						}
						else{
							$("#error").html("Maximum 6 element can be entered");
							$("#error-popup").css('display','block');		
							$('#error-popup').delay(2000).fadeOut('slow');
							return false;
						}
					}				
					if(numbersChosen.length%6==0){
						$("#numPicked").html("");
						numberSelected.push(numbersChosen.toString());
						$("#noOfLines").html(numberSelected.length);
						numbersChosen=[];
						$(".bonusLottoNumber").removeClass("selectnumber");
						lineNum=parseInt($("#radio"+(lineNum)).attr("lineNum"))+1;
						if(lineNum>=1 && lineNum<=4){
							$("#radio"+(lineNum)).prop('checked','checked');
							$("#line"+(lineNum)).addClass("active");
							$(".lineNum"+(lineNum)).first().focus();	
						}
						isValid=true;
						checkIsDrawSelected();
						//$("#buy").attr("disabled",false);
						if(numberSelected.length == maxLineReq){
							   $(".unitPrice").first().focus();
							   $("#numPicked").html(numberSelected[3]);
							}
						updateTicketPrice();
						if(lineNum==5){
							for(lineNum=1;lineNum<=4;lineNum++){
									if($(".lineNum"+lineNum).first().val()==null || $(".lineNum"+lineNum).first().val()==""){
										$(".line").attr("checked",false);
										$("#radio"+(lineNum)).prop('checked','checked');
										$("#line"+(lineNum)).addClass("active");
										$(".lineNum"+lineNum).first().focus();
										return false;
									}
							}
						}
					}
				});
			}
			else{
				if($(this).hasClass("selectnumber")){
					var index = numbersChosen.indexOf($(this).text());
					$(this).removeClass("selectnumber");
					$(".lineNum"+lineNum).each(function(){
						if($(this).val()==noText){
							numbersChosen.splice(index,1);
							$(this).val("");
							$(this).focus();
						}
					});
					h=0;
					$("#numPicked").html("");
					$(numbersChosen).each(function(h){
						if(h==0){
							$("#numPicked").append(numbersChosen[h]);
						} else {
							$("#numPicked").append(","+numbersChosen[h]);
						}
						h++;
					});
					if (numbersChosen.length==0){
						var storedArrayIndex = numberSelected.indexOf(numbersChosen);
						numberSelected.splice(storedArrayIndex,1);
						$("#noOfLines").html(numberSelected.length);
						$(".lineNum"+lineNum).first().focus();
					}

					updateTicketPrice();
				}
			}
		} else{
			$("#error").html("Maximum 4 lines can be entered");
			$("#error-popup").css('display','block');		
			$('#error-popup').delay(2000).fadeOut('slow');
			isValid=true;
			return false;
		}

	}
	else if("Perm6"==selectedBetName){
		
		var minLineReq=zimLottoBonus[selectedBetName].min;
		var maxLineReq=zimLottoBonus[selectedBetName].max;
		var noSelected = pmsParseInt($(this).children().text());
		var noText = $(this).text();
		if(noText.length==2){
			if($.inArray(noText,numbersChosen)<0){
				if(numbersChosen.length!=15){
					$(this).each(function(){
						if($(this).hasClass("selectnumber")){
							$(this).removeClass("selectnumber");
							$(".zimBonusLottoPermSix").each(function(){
								if($(this).val()==noText){
									$(this).val("");
									$(this).focus();
								}
							});
						}
						else{
							$(this).addClass("selectnumber");
							numbersChosen.push(noText);
							numbersChosen.length<2?$("#numPicked").append(noText):$("#numPicked").append(","+noText);
							$(".zimBonusLottoPermSix").each(function(){
								if($(this).val() == ""){
									$(this).val(noText);
									$(this).next().focus();
									return false;
								}
							});
						}
					});
				}else{
					alert("Maximum only 15 numbers ");
				}
				if(numbersChosen.length >= 7){
					numberSelected=[];
					$("#noOfLines").html("0");
					//numberSelected.push(numbersChosen);	
					calculateNoOfLines(numbersChosen.length);
					isValid=true;
					checkIsDrawSelected();
					//$("#buy").attr("disabled",false);
					updateTicketPrice();
				}
			}
			else{
				var index = numbersChosen.indexOf($(this).text());
				if($(this).hasClass("selectnumber")){
					$(this).removeClass("selectnumber");
					$(".zimBonusLottoPermSix").each(function(){
						if($(this).val()==noText){
							numbersChosen.splice(index,1);
							$(this).val("");
							$(this).focus();
						}
					});
					h=0;
					$("#numPicked").html("");
					$(numbersChosen).each(function(h){
						if(h==0){
							$("#numPicked").append(numbersChosen[h]);
						} else {
							$("#numPicked").append(","+numbersChosen[h]);
						}
						h++;
					});
					if(numbersChosen.length>=7){
						//$("#buy").attr("disabled",false);
						isValid=true;
						checkIsDrawSelected();
						calculateNoOfLines(numbersChosen.length);
					}
					else{
						$("#buy").attr("disabled",true);
						$("#noOfLines").html("0");
					}
						
					updateTicketPrice();
				}
			}
			

		}
	}
});

	/**
	 * on click on radio button 
	 */
	$(document).on("click",".line",function(){
		$(".nu-input-area").removeClass("active");
		tempLineNum=parseInt($(this).attr("lineNum"));
		$("#line"+tempLineNum).addClass("active");
		if(tempLineNum!=lineNum){
			if(numbersChosen.length<6 && numbersChosen.length>0){
				$("#error").html("Please fill the complete row then switch");
				$("#error-popup").css('display','block');		
				$('#error-popup').delay(2000).fadeOut('slow');
				numbersChosen=[];
				$(".bonusLottoNumber").removeClass("selectnumber");
			    $(".lineNum"+lineNum).each(function(){
					if($(this).val()!=null && $(this).val().replace(/(^\s+|\s+$)/g, "") != ""){
						numbersChosen.push($(this).val());
						$("#bonusLottoNum"+$(this).val()).addClass("selectnumber");
					}
				});
				$(".lineNum"+lineNum).each(function(){
					if($(this).val()==""){
						$(this).focus();
						return false;
					}
				});
				return false;
			}
		}
		numbersChosen=[];
		$(".bonusLottoNumber").removeClass("selectnumber");
		$(this).attr('checked',true);
		lineNum=parseInt($(this).attr("lineNum"));
		$("#radio"+lineNum).prop("checked","checked");
		$("#line"+(lineNum)).addClass("active");
		   
		$(".lineNum"+lineNum).each(function(){
			if($(this).val()!=null && $(this).val().replace(/(^\s+|\s+$)/g, "") != ""){
				numbersChosen.push($(this).val());
				$("#bonusLottoNum"+$(this).val()).addClass("selectnumber");
			}
			else{
				$(this).focus();
				return false;
			}
		});
	});
	$(document).on("click",".manualNumberZimBonusLotto",function(){
		$("#numPicked").html("");
		$(".nu-input-area").removeClass("active");
		tempLineNum=parseInt($(this).attr("lineNum"));
		if(tempLineNum!=lineNum){
			if(numbersChosen.length<6 && numbersChosen.length>0){
				$("#error").html("Please fill the complete row then switch");
				$("#error-popup").css('display','block');		
				$('#error-popup').delay(2000).fadeOut('slow');
				numbersChosen=[];
				$(".bonusLottoNumber").removeClass("selectnumber");
				$("#line"+lineNum).addClass("active");
			    $(".lineNum"+lineNum).each(function(){
					if($(this).val()!=null && $(this).val().replace(/(^\s+|\s+$)/g, "") != ""){
						numbersChosen.push($(this).val());
						$("#numPicked").html(numbersChosen);
						$("#bonusLottoNum"+$(this).val()).addClass("selectnumber");
					}
				});
				$(".lineNum"+lineNum).each(function(){
					if($(this).val()==""){
						$(this).focus();
						return false;
					}
				});
				return false;
			}
		}
		numbersChosen=[];
		$(".bonusLottoNumber").removeClass("selectnumber");
		$(this).attr('checked',true);
		lineNum=parseInt($(this).attr("lineNum"));
		$("#radio"+lineNum).prop("checked","checked");
		$("#line"+(lineNum)).addClass("active");
		   
		$(".lineNum"+lineNum).each(function(){
			if($(this).val()!=null && $(this).val().replace(/(^\s+|\s+$)/g, "") != ""){
				numbersChosen.push($(this).val());
				$("#numPicked").html(numbersChosen.toString());
				$("#bonusLottoNum"+$(this).val()).addClass("selectnumber");
			}
		});
		if(numbersChosen.length==0){
			$(".lineNum"+lineNum).first().focus();
		}
	});
	/**
	 * enter number manually in input box in Direct6
	 */
	$(document).on("keyup",".manualNumberZimBonusLotto",function(e){
		
		if($("#qpCheck").is(":checked")){
			$("#qpCheck").prop("checked",false);
			$("#zimQpTextbox").val("");
			$("#zimQpTextboxName").empty();
			$("#zimQpTextboxName").css("display","none");
			$("#zimQpTextbox").attr("disabled",true);
			
		}
		if(e.which==9){
			if($(this).prev().val().replace(/(^\s+|\s+$)/g, "").length<2){
				e.preventDefault();
				$(this).prev().val("");
				$(this).prev().focus();
			}
			return false;
		}
		currVal = $(this).val();
		$("#buy").attr("disabled",true);
		var maxLineReq=zimLottoBonus[selectedBetName].max;
		lineNum=parseInt($(this).attr("lineNum"));
		var enteredNum=$(this).val().replace(/(^\s+|\s+$)/g, "");
		if(!isNaN(enteredNum)){
			if(enteredNum.length==2){
				if(parseInt($(this).val(),10)<1 || parseInt($(this).val(),10)>42){
					$(this).val("");
					$("#error").html("Input must be between in 1 to 42 include both ");
					$("#error-popup").css('display','block');	
					$('#error-popup').delay(2000).fadeOut('slow');
				}
				else{
					if($.inArray(enteredNum,numbersChosen) >= 0){
						if(((e.keyCode >= 48 && e.keyCode <= 57) || (e.keyCode >= 96 && e.keyCode <= 105))){
							$(this).val("");
							$(this).focus();
							return false;
						}else{
							$(this).val(enteredNum);
							return false;
						}
					}
					else{
						$("#bonusLottoNum"+enteredNum).addClass("selectnumber");
						numbersChosen.push(enteredNum);
						numbersChosen.length<2?$("#numPicked").append(enteredNum):$("#numPicked").append(","+enteredNum);
						$(".lineNum"+(lineNum)).each(function(){
							if($(this).val()==""){
								$(this).focus();
								return false;
							}
						});
						if( numbersChosen.length%6==0){
							$("#numPicked").html("");
							numberSelected.push(numbersChosen.toString());
							$("#noOfLines").html(numberSelected.length);
							numbersChosen=[];
							$(".bonusLottoNumber").removeClass("selectnumber");
							lineNum=lineNum+1;
							if(lineNum>=1 && lineNum<=4){
								$(".nu-input-area").removeClass("active");
								$("#radio"+(lineNum)).prop('checked','checked');
								$("#line"+(lineNum)).addClass("active");
								$(".lineNum"+(lineNum)).first().focus();	
							}
							isValid=true;
							checkIsDrawSelected();
							updateTicketPrice();
							if(lineNum==5){
								for(lineNum=1;lineNum<=4;lineNum++){
									if($(".lineNum"+lineNum).first().val()==null || $(".lineNum"+lineNum).first().val()==""){
										$(".line").attr("checked",false);
										$("#radio"+(lineNum)).prop('checked','checked');
										$("#line"+(lineNum)).addClass("active");
										$(".lineNum"+lineNum).first().focus();
										return false;
									}
								}

							}
							if(numberSelected.length == (maxLineReq)){
								$(".unitPrice").first().focus();
								$("#numPicked").html(numberSelected[3]);
							}
							//$("#buy").attr("disabled",false);
							
						}

					}
				}

			}
			else{
				if(enteredNum.length==2 || enteredNum.length==0){
					if((numbersChosen.length==1) && (enteredNum.length==0)){
						var storedArrayIndex = numberSelected.indexOf(numbersChosen);
						numberSelected.splice(storedArrayIndex,1);
						$("#noOfLines").html(numberSelected.length);
						updateTicketPrice(1);
					}
					numbersChosen=[];
					$(".bonusLottoNumber").removeClass("selectnumber");
					$(".lineNum"+lineNum).each(function(){
						if($(this).val().replace(/(^\s+|\s+$)/g, "")!=""){
							numbersChosen.push($(this).val().replace(/(^\s+|\s+$)/g, ""));
							$("#bonusLottoNum"+($(this).val().replace(/(^\s+|\s+$)/g, ""))).addClass("selectnumber");
						}

					});
					h=0;
					$("#numPicked").html("");
					$(numbersChosen).each(function(h){
						if(h==0){
							$("#numPicked").append(numbersChosen[h]);
						} else {
							$("#numPicked").append(","+numbersChosen[h]);
						}
						h++;
					});
					if(numbersChosen.length==0 && numberSelected.length!=0){
						isValid=true;
						checkIsDrawSelected();
					}
				}
				/*else if(enteredNum.length==1){
					$(this).val(tempNum);
				}*/
			}
		}else{
			$(this).val("");
		}
	});
});
/**
 * keydown event in direct6 in manual entry 
 */

$(document).on("keydown",".manualNumberZimBonusLotto",function(e){
	tempNum=$(this).val().replace(/(^\s+|\s+$)/g, "");
	var enteredNum=$(this).val().replace(/(^\s+|\s+$)/g, "");
	if(!isNaN(enteredNum)){
		if((enteredNum.length==2)){
			if(parseInt($(this).val(),10)>=1 || parseInt($(this).val(),10)<=42){
				if($.inArray(enteredNum,numbersChosen) >= 0){
				     if((e.which == 8 || e.which == 46)){
						var index = numbersChosen.indexOf($(this).val());
						$("#bonusLottoNum"+($(this).val())).removeClass('selectnumber');
						numbersChosen.splice(index,1);
						$(this).val(currVal);
					}else if((e.keyCode >= 48 && e.keyCode <= 57) || (e.keyCode >= 96 && e.keyCode <= 105)){
						$(this).val(currVal);
					}
				}
			}
		}
		else{
			$(this).val(enteredNum);
			
		}
	}
	else{
		$(this).val(enteredNum);
		}

});
/**
 * manual entry in perm6
 */
$(document).on("keyup",".zimBonusLottoPermSix",function(e){
	$(".nu-input-area").removeClass("active");
	if($("#qpCheck").is(":checked")){
		$("#qpCheck").prop("checked",false);
		$("#zimQpTextbox").val("");
		$("#zimQpTextboxName").empty();
		$("#zimQpTextboxName").css("display","none");
		$("#zimQpTextbox").attr("disabled",true);
	}
	if(e.which==9){
		if($(this).prev().val().replace(/(^\s+|\s+$)/g, "").length<2){
			e.preventDefault();
			$(this).prev().val("");
			$(this).prev().focus();
		}
		return false;
	}
	$("#buy").attr("disabled",true);
	var maxLineReq=zimLottoBonus[selectedBetName].max;
	lineNum=parseInt($(this).attr("lineNum"));
	var enteredNum=$(this).val().replace(/(^\s+|\s+$)/g, "");
	if(!isNaN(enteredNum)){
		if(enteredNum.length==2){
			if(parseInt($(this).val(),10)>=1 && parseInt($(this).val(),10)<=42){
				if($.inArray(enteredNum,numbersChosen) < 0){
					$("#bonusLottoNum"+enteredNum).addClass("selectnumber");
					numbersChosen.push(enteredNum);
					$(".zimBonusLottoPermSix").each(function(){
						if($(this).val()==""){
							$(this).focus();
							return false;
						}
					});
					//$(this).next().focus();
					numbersChosen.length<2?$("#numPicked").append(enteredNum):$("#numPicked").append(","+enteredNum);
					if(numbersChosen.length>=7){
						//$("#buy").attr("disabled",false);
						isValid=true;
						checkIsDrawSelected();
						calculateNoOfLines(numbersChosen.length);
						updateTicketPrice();
					}
				}
				else{
					$(this).val("");
				}
			}
			else{
				$(this).val("");
				$("#error").html("Input must be between in 1 to 42 include both ");
				$("#error-popup").css('display','block');	
				$('#error-popup').delay(2000).fadeOut('slow');
			}
		}
		else{
			if(enteredNum==""){
				numbersChosen=[];
				$("#noOfLines").html("0");
				$(".bonusLottoNumber").removeClass("selectnumber");
				$(".zimBonusLottoPermSix").each(function(){
					if($(this).val().replace(/(^\s+|\s+$)/g, "")!=""){
						numbersChosen.push($(this).val());
						$("#bonusLottoNum"+($(this).val())).addClass("selectnumber");	 
					}
					h=0;
					$("#numPicked").html("");
					$(numbersChosen).each(function(h){
						if(h==0){
							$("#numPicked").append(numbersChosen[h]);
						} else {
							$("#numPicked").append(","+numbersChosen[h]);
						}
						h++;
					});
					if(numbersChosen.length>=7){
						isValid=true;
						checkIsDrawSelected();
						calculateNoOfLines(numbersChosen.length);
					}
					updateTicketPrice();
				});
			}
		}
	}
	else{
		$(this).val("");
	}
	if($("#numPicked").html()==","){
		$("#numPicked").html("");
	}
});
/**
 * click in perm6 in manual input box
 */
$(document).on("click",".zimBonusLottoPermSix",function(){
	if(numbersChosen.length<1){
		$(".zimBonusLottoPermSix").each(function(){
			if($(this).val().replace(/(^\s+|\s+$)/g, "")==""){
				$(this).focus();
				return false();
			}
		});
	}
});
/**
 *when we insert no in quick pick box 
 */

$(document).on("keyup","#zimQpTextbox",function(e){
	$(".nu-input-area").removeClass("active");
	numberSelected=[];
	$("#buy").attr("disabled",true);
	isValid=false;
	$("#noOfLines").html("0");
	var noPicked=0;
	var rngResp='';
	var maxLineReq=zimLottoBonus[selectedBetName].max;
	if (e.which == 8 || e.which == 46){
		changeBetTypeZimLottoBonus();
	}else{
		if("Direct6"==selectedBetName){
			noPicked=6;
			var linesSelectedQp=$(this).val().replace(/(^\s+|\s+$)/g, "");
			if(!isNaN(linesSelectedQp)){
				if(linesSelectedQp>=1 && linesSelectedQp <=4){
					changeBetTypeZimLottoBonus();
					rngResp=getGameWiseRNG("ZimLottoBonus",noPicked,linesSelectedQp);
					//rngResp='{"pickData":["10,18,26,29,32,33","05,19,23,25,31,42","16,18,22,29,30,40"],"responseCode":0,"responseMsg":"success"}';	
	                //rngResp=JSON.stringify(rngResp);
	                if (rngResp == "NETERROR") {
	            		$("#qpCheck").prop("checked",false);
	            		$("#zimQpTextbox").attr("disabled",true);
	            		$("#error-popup").css('display','block');	
	            		$("#error").html("Server not reachable, Please check connection and try again !!!");
	            		$('#error-popup').delay(2000).fadeOut('slow');
	            		return;
	            	}
					if(rngResp.responseMsg=="success"){
						$(rngResp.pickData).each(function(){
						 $("#noOfLines").html("0");
						 $(".bonusLottoNumber").removeClass("selectnumber");
						 //$(".lineNum"+lineNum).first().focus();
						 var i=0;
						 numbersChosen=[];
						 numbersChosen=this.split(",");
						 numberSelected.push(this.valueOf());
						 $(".lineNum"+lineNum).each(function(){
								$(this).val(numbersChosen[i]);
								$("#bonusLottoNum"+numbersChosen[i]).addClass("selectnumber");
								i++;
							});
							lineNum++;
							if(numberSelected.length<maxLineReq){
								numbersChosen=[];
								$("#radio"+(lineNum)).prop("checked",true);
								$("#line"+(lineNum)).addClass("active");
								$(".lineNum"+lineNum).first().focus();
								 $(".bonusLottoNumber").removeClass("selectnumber");
							}
							else{
								$(".unitPrice").first().focus(); 
								$("#numPicked").html(numberSelected[3]);
							}
							
							//$("#buy").attr("disabled",false);
							isValid=true;
							checkIsDrawSelected();
	  					});
						$("#noOfLines").html(numberSelected.length);
						updateTicketPrice(1);
						$(".unitPrice").first().focus();
					}
					else{
						$("#zimQpTextbox").attr("disabled",true);
						$("#qpCheck").prop("checked",false);
						$("#error").html(rngResp.responseMsg);
						$("#error-popup").css('display','block');		
						$('#error-popup').delay(2000).fadeOut('slow');
					}
	 			}
				else{
					$("#zimQpTextbox").val("");
					$("#error").html("Input is between 1 & 4 including both");
					$("#error-popup").css('display','block');		
					$('#error-popup').delay(2000).fadeOut('slow');
				}
			}
			else{
				$("#zimQpTextbox").val("");
			}
		}
		else if("Perm6"==selectedBetName){
			var noPicked=$(this).val().replace(/(^\s+|\s+$)/g, "");
			$("#buy").attr("disabled",true);
			if(!isNaN(noPicked)){
				if(noPicked>=7 && noPicked <=15){
					rngResp=getGameWiseRNG("ZimLottoBonus",noPicked,1);
					//rngResp='{"pickData":["03,10,13,17,18,20,26,28,35,41"],"responseCode":0,"responseMsg":"success"}';
					//rngResp=JSON.parse(rngResp);
					if (rngResp == "NETERROR") {
	            		$("#qpCheck").prop("checked",false);
	            		$("#error-popup").css('display','block');	
	            		$("#error").html("Server not reachable, Please check connection and try again !!!");
	            		$('#error-popup').delay(2000).fadeOut('slow');
	            		return;
	            	}
					if(rngResp.responseMsg=="success"){
						$(".bonusLottoNumber").removeClass("selectnumber");
						if(rngResp.pickData!=undefined || rngResp.pickData!=null){
							var i=0;
							numbersChosen=[];
							numberSelected=[];
							numbersChosen=rngResp.pickData[0].split(",");
							//numbersChosen.push(rngResp.pickData[0]);
							$(".zimBonusLottoPermSix").each(function(){
								$(this).val(numbersChosen[i]);
								$("#bonusLottoNum"+numbersChosen[i]).addClass("selectnumber");
								i++;
							});
							//$("#buy").attr("disabled",false);
							isValid=true;
							checkIsDrawSelected();
							$("#numPicked").html(numbersChosen.toString());
						}
						calculateNoOfLines(numbersChosen.length);
						updateTicketPrice();
					}
					else{
						$("#qpCheck").prop("checked",false);
						$("#error").html(rngResp.responseMsg);
						$("#error-popup").css('display','block');		
						$('#error-popup').delay(2000).fadeOut('slow');
					}
					
				}
				else{
					if($(this).val().replace(/(^\s+|\s+$)/g, "")!=1){
						$("#zimQpTextbox").val("");
						$("#error").html("Input is between 7 & 15 including both");
						$("#error-popup").css('display','block');		
						$('#error-popup').delay(2000).fadeOut('slow');	
					}
				}
				
			}
			else{
				$("#zimQpTextbox").val("");
			}
		}
	}
	
	
});

/**
 *on click on reset button 
 */
$(document).on("click","#resetZimLottoBonus",function(){
	resetZimLottoBonus();
	changeBetTypeZimLottoBonus();
});

/**
 * 
 */
function resetZimLottoBonus(){
	$("#zimQpTextbox").val("");
	$("#zimQpTextbox").attr("disabled",true);
	$("#qpCheck").attr("checked",false);
}
/**
 * when we change the bet type
 */
function changeBetTypeZimLottoBonus(){
	$(".nu-input-area").removeClass("active");
	isValid=false;
    $("#zimQpTextboxName").html("");
	$("#zimQpTextboxName").css("display","none");
	$(".manualNumberZimBonusLotto").val("");
	$("#numPicked").html("");
	$("#buy").attr("disabled",true);
	$("#noOfLines").html("0");
	$(".line").attr("checked",false);
    lineNum=1;
	count=1;
	tempNum=0;
	betAmtSelected=$(".unitPrice").first().text();
	numbersChosen=[];
	numberSelected=[];
	$(".unitPrice").removeClass("amt-select");
	$(".unitPrice").first().addClass("amt-select");
	updateTicketPrice();
	if("Direct6"==selectedBetName){
		$("#permSix").css("display","none");
		$("#directSix").css("display","block");
		$("#radio1").prop('checked','checked');
		$("#line1").addClass("active");
		$("#zbl1").focus();
		$(".bonusLottoNumber").removeClass("selectnumber");
	}
	else if("Perm6"==selectedBetName){
		$("#directSix").css("display","none");
		$("#permSix").css("display","block");
		$(".zimBonusLottoPermSix").first().focus();
		$(".bonusLottoNumber").removeClass("selectnumber");
		$(".zimBonusLottoPermSix").val("");
	}
}
/**
 * calculating number of lines
 */
function calculateNoOfLines(num){
	var noOfLines = (num * (num - 1) * (num - 2) * (num - 3) * (num - 4) * (num - 5)) / 720;
	$("#noOfLines").html(noOfLines);
}

