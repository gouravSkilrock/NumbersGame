var numbersChosen = [];
var mainBetInfo = [];
var selectedDrawIds =[];
var selectedBetCode = 0;
var selectedBetName ='';
var currBetAmtMul = 0;
var currUnitPrice = 0;
var gameIndex = 0;
var respData = {};
var currVal;
var cDate;
var allFreezeTimeArray = [];
var minTimeToFetchData;
var interval;
var _resp;
var actionURL;
var curTrx = "";
var getDrawResult = {};
var drawInfo = [];
var winningNumber = [];
var reprintCountChild = 0;
var gameIndex = 0;
var reprintCount;
var gameResult = {};
var noOfSelectedDraws = [];
var alreadySelDraw = [];
var timerArr = [];
var isValid = false;
var printData = "";
var tktNum;
var drawFrequency = "";
var betTypeSel = "";
var intervalCount = 2;
var numOfDraws = 0;
var betAmtSelected = 0;
var isDrawManual = true;
var noOfDrawsSelected = 1;
var drawsSel = "";
var isPriceSel = false;
var betAmount = 0;
var gameSelected = "";
var keyupFiredCount=0;
var keyupFiredCount1=0;
var delayTime;
var serUpdatedCurDate='';
var freezeTimerIntervalId;
var setIntervalId1;
var setIntervalId2;
var setIntervalId3;
var setIntervalId4;
var setIntervalId5;
var setIntervalId6;
var setIntervalId7;
var setIntervalId8;
var setIntervalId9;
var setIntervalId10;
var isSideMenuBuild = false;
var isForTimer = false;
var gameInFocus = "";
var printResDataJson ={};
var userActionList = '';
var priceArray = {"TwelveByTwentyFour":{"0":"0.5","1":"1","2":"1.5","3":"2","4":"5"},"KenoSix":{"0":"0.5","1":"1","2":"1.5","3":"2","4":"5"},"OneToTwelve":{"0":"0.5","1":"1","2":"1.5","3":"2","4":"5"},"MiniRoulette":{"0":"0.1","1":"0.5","2":"1","3":"2","4":"5"},"KenoTwo":{"0":"0.1","1":"0.2","2":"0.3","3":"0.4","4":"0.5"},"FullRoulette":{"0":"0.1","1":"0.5","2":"1","3":"2","4":"5"},"ZimLottoBonus":{"0":"0.2","1":"0.4","2":"0.6","3":"0.8","4":"1"},"TenByTwenty":{"0":"0.25","1":"0.5","2":"0.75","3":"1","4":"2"}};
var kenoSixBetTypeWisePrizeScheme = {"Direct10":{"10":"2,500,000*","9":"75,000","8":"10,000","7":"1,000","6":"100","5":"50"},"Direct9":{"9":"1,000,000*","8":"50,000","7":"1,000","6":"250","5":"50"},"Direct8":{"8":"300,000*","7":"5,000","6":"1,000","5":"100"},"Direct7":{"7":"35,000","6":"1,250","5":"150","4":"40"},"Direct6":{"6":"7,500","5":"750","4":"80","3":"20"},"Direct5":{"5":"4,500","4":"250","3":"30"},"Direct4":{"4":"500","3":"50","2":"20"},"Direct3":{"3":"180","2":"40"},"Direct2":{"2":"140"},"Direct1":{"1":"30"}};
var prizeSchemeArray = {"TwelveByTwentyFour":{"12":"500,000","11":"20,000","10":"1,000","9":"300","0":"200,000"},"KenoSix":kenoSixBetTypeWisePrizeScheme,"OneToTwelve":{"1":"100"},"MiniRoulette":{"1":"15,00,00,000","0":"3,00,000"},"KenoTwo":{"1" :"4","2":"20","3":"200"},"FullRoulette":{"1":"15,00,00,000","0":"3,00,000"},"TenByTwenty":{"10":"500,000","9":"20,000","8":"1,000","7":"300","0":"200,000"},"ZimLottoBonus":{"1":"100"}};
var oneByTwelveZodiacSigns = ["Aries","Taurus","Gemini","Cancer","Leo","Virgo","Libra","Scorpio","Sagittarius","Capricon","Aquarius","Pisces"];
var userActionListArray = {"TwelveByTwentyFour":"twelveByTwentyFourBuy", "KenoSix":"kenoSixBuy", "OneToTwelve":"oneToTwelveBuy", "MiniRoulette":"oneToTwelveRouletteBuy","FullRoulette":"fullRouletteBuy" ,"KenoTwo":"kenoTwoBuy","ZimLottoBonus":"zimLottoBonusBuy","TenByTwenty" :"tenByTwentyBuy"};
var gameDevelopmentName = [];
var cardNo='';
var isCalled = false;
var currentFocus = 'select-game';
var printUsingApplet;
var nVer = navigator.appVersion;
var nAgt = navigator.userAgent;
var browserName  = navigator.appName;
var fullVersion  = ''+parseFloat(navigator.appVersion); 
var majorVersion = parseInt(navigator.appVersion,10);
var nameOffset,verOffset,ix;
$(document).ready(function(){
	resultPrintFunc();
	currServerTime();
	var resetTimer = true;
    updateMidPanel(0,0,resetTimer);
    $('.beforePageLoad').delay(100).fadeOut('slow');
    
    if ((verOffset=nAgt.indexOf("MSIE"))!=-1) {
    	printUsingApplet = "YES";
    }else if ((verOffset=nAgt.indexOf("Chrome"))!=-1) {
    	printUsingApplet = "NO";
    }
      
    /**
     * Barcode scanner and MSR code starts
     */
    function DelayExecution(f, delay) {
        var timer = null; 
        return function () {
            var context = this, args = arguments;
            clearTimeout(timer);
            timer = window.setTimeout(function () {
                f.apply(context, args);
            },
            delay || 100);
        };
    }
    
    $.fn.ConvertToBarcodeTextbox = function () {
         $(this).keyup(DelayExecution(function (event) {
    	 if(event.keyCode==13){
        		$('#pwtOk').click();
    	 }
         if(($("#pwtTicket").val().length==18 ||$("#pwtTicket").val().length==20) && keyupFiredCount<=1){
        	 $('#pwtOk').click();
         }
             var keycode = (event.keyCode ? event.keyCode : event.which);  
                 keyupFiredCount = keyupFiredCount + 1;  
         }));
     };
     
     function DelayExecution1(f, delay) {
         var timer = null; 
         return function () {
             var context = this, args = arguments;
             clearTimeout(timer);
             timer = window.setTimeout(function () {
                 f.apply(context, args);
             },
             delay || 8);
         };
     }
     $.fn.ConverToCardScanTextBox = function () {
    	 $(this).keyup(DelayExecution1(function (event) {
    		 if(event.keyCode!=27){
    			 if($(this).val().length>=10 && keyupFiredCount1<=1){
    	        	 $(this).val($(this).val().replace(';','').replace('?',''));
    	        	 $(this).attr("readonly",true);
    	         }
                 var keycode = (event.keyCode ? event.keyCode : event.which);  
    	 			if($(this).val().length<10){
    	         		alert("Please enter value by swipe reader only!!");
    	         		$(this).val('');
    	         	}    
    		 }
	        
         }));
     };
     try {
    	 
         $("#pwtTicket").ConvertToBarcodeTextbox();
         $("#cardNo").ConverToCardScanTextBox();

     } catch (e) { 
    	 
     }
     
     //Barcode scanner and MSR code Ends 
     
	$(".sideMenuList").on("keydown",function(e){
		e.preventDefault();
		if(e.which == 9 || e.which == 13){
			// $('.chkBox').first().focus();
			if(gameInFocus != "" && $(this).children().eq(1).text() == gameInFocus){
				e.preventDefault();
				return;
			}else{
				$("#numOfDrawsSelected").focus();
			}
		}else if(e.which == 38){
			$(this).prev().focus();
			$(this).prev().trigger('click');
		}else if(e.which == 40){
			$(this).next().focus();
			$(this).next().trigger('click');
		}
	});
	
	$(document).on("keydown",".chkBox",function(e){
		noOfSelectedDraws =[];
		$('input[type=checkbox]').each(function () {
			if($(this).is(":checked")){
			  noOfSelectedDraws.push($(this).val());
			} 
		});	
		switch(e.which) {
	        case 37: // left
	        	$(this).parent().parent().prev().children().children().focus();
	        break;
	       
	        case 39: // right
	        	$(this).parent().parent().next().children().children().focus();
	        break;
	         
	        case 9:
	        	e.preventDefault();
	        	if(noOfSelectedDraws.length >0 ){
	        		$("#error").html('');
	        		$("#drawSubmit").trigger('click');
	        		$('.bettypes').first().focus();
	        	}
	        	else {
	        		$("#advError").html("Atleast one draw is required to play !");
	        		$(this).focus();
	        	}
	        	
	        break;
	        
	        case 13:
	        	e.preventDefault();
	        	if(noOfSelectedDraws.length > 0 ){
	        		$("#error").html('');
	        		$("#drawSubmit").trigger('click');
	        		if ("OneToTwelve" == gameSelected) {
	        			$("#qpCheck").focus();
						return;
					}
	        		$('.bettypes').first().focus();
	        	}
	        	else {
	        		$("#advError").html("Atleast one draw is required to play !!!");
	        		$(this).focus();
	        	}	        	
	        break;	        
	        case 32:
	        	e.preventDefault();
	        	var chkBrowser = window.navigator.userAgent;
	        	//console.log(a);
	        	var bIndex = chkBrowser.indexOf("Chrome");
	        	if(bIndex >0){
	        		$(this).parent().click();
	        	 }else {
	        		 $(this).parent().on('click');
	        		 //$(this).parent().blur();
				}    
	        	break; 
	        default: $(this).focus(); // stay focused on the default.
		}
	});
	
	/**
	 * Bet type radio button keyEvent click event handler
	 */ 
	 
       $(document).on("keydown",".bettypes",function(e){
    	   switch(e.which){
    	   	case 13:
	    		   if ($("#qpCheck").prop('disabled')) {
	    			    $(".unitPrice").first().focus();
					} else {
						$("#qpCheck").focus();
		
					}
	    	   e.preventDefault();	   
    		   break;
    	   	case 9:
	    	       if ($("#qpCheck").prop('disabled')) {
	    				$(".unitPrice").first().focus();	
	    			} else {
	    				$("#qpCheck").focus();
	
	    			}
	    	   e.preventDefault();    
    	       break;
    	   	case 37:
    	   		    if($(this).prev().length == 0){
    	   		    	return;
    	   		    }
	    	   		$(this).prev().focus();
	    	   		$(this).prev().trigger('click');
	    	   		$(this).removeClass("select-bet");
	    	   		$(this).prev().addClass("select-bet");
	    	   		//console.log($(this).prev().length);
	    	   	e.preventDefault();	
    	   		break;
    	   	case 39:
    	   		    if($(this).next().length == 0){
    	   		    	return;
    	   		    }
	    	   		$(this).next().focus();
	    	   		$(this).next().trigger('click');
	    	   		$(this).removeClass("select-bet");
	    	   		$(this).next().addClass("select-bet");
	    	   		//console.log($(this).next().length);
	    	    e.preventDefault();		
    	   		break;
    	   	default :
    	   		e.preventDefault();
    	   		$(this).focus();
    	   		
    	   }
       });	
	
   	
   	/**
   	 * Qp  check box key press event handler
   	 * 
   	 */
       $(document).on("keydown","#qpCheck",function(e){
    	   switch (e.which) {
    	   		case 9:
    	   			e.preventDefault();
    	   			if ("TwelveByTwentyFour" == gameSelected) {
    	   				$(".manualNumberEnter").first().focus();
					}else if ("KenoSix" == gameSelected) {
						$(".game80ManualEnter").first().focus();
					}else if ("OneToTwelve" == gameSelected) {
						$(".ottManualEnter").first().focus();
					}else if ("MiniRoulette" == gameSelected) {
						return;
					}else if("KenoTwo" == gameSelected){
						$(".kenoTwoNumberEnter").first().focus();
					}else if ("FullRoulette" == gameSelected) {
						return;
					}
    	   		break;
    	   		case 13:
    	   			e.preventDefault();
    	   			if ("TwelveByTwentyFour" == gameSelected) {
    	   				$(".manualNumberEnter").first().focus();
					}else if ("KenoSix" == gameSelected) {
						$(".game80ManualEnter").first().focus();
					}else if ("OneToTwelve" == gameSelected) {
						$(".ottManualEnter").first().focus();
					}else if ("MiniRoulette" == gameSelected) {
						return;
					}else if("KenoTwo" == gameSelected){
						$(".kenoTwoNumberEnter").first().focus();
					}else if ("FullRoulette" == gameSelected) {
						return;
					}
    	   			break;
    	   		case 32:
    	   			e.preventDefault();
    	   			$(this).trigger('click');
    	   			if ("OneToTwelve" == gameSelected) {
						$("#numToBePicked").focus();
						return;
					}else if("KenoTwo" == gameSelected){
						$("#numToBePickedFN").first().focus();
						return;
					}
    	   			$(".unitPrice").first().focus();
    	   		break;
		        default:
		        	e.preventDefault();
			    break;
		}
    	   
       });
       
     	/**
      	 * unit price  button key press event handler
      	 * 
      	 */  
       
         $(document).on("keydown",".unitPrice",function(e){
        	 switch (e.which) {
        	 	case 9:
        	 		e.preventDefault();
				break;
        	 	case 13:
        	 		e.preventDefault();
        	 		if ("Other" == $(this).text()) {
        	 			$(this).trigger('click');
						$(".other-amt").focus();
					}else {
						$(this).trigger('click');
						$("#buy").focus();
					}
        	 	break;
        	 	case 37:
        	 		if ($(this).prev().length == 0) {
						return;
					}
        	 		else if ($(this).hasClass("amt-select") ) {
						$(this).removeClass("amt-select");
						
					}
        	 		$(this).prev().focus();
        	 		$(this).prev().addClass("amt-select");
        	 		e.preventDefault();
        	 	break;
        	 	case 39:
        	 		if ($(this).next().length == 0 || "Other" == $(this).text() ) {
						return;
					}
        	 		else if ($(this).hasClass("amt-select")) {
						$(this).removeClass("amt-select");
					}
        	 		$(this).next().focus();
        	 		$(this).next().addClass("amt-select");
        	 		e.preventDefault();
        	 	break;
        	 	case 32:
        	 		e.preventDefault();
        	 		$(this).trigger('click');
        	 		$("#buy").focus();
        	 	break;	
			    default:
				    e.preventDefault();
				break;
			}
        });
         
     /**
 	 * Other amount input key press event handler
 	 */ 
         $(document).on("keydown",".other-amt",function(e){
        	 switch (e.which) {
        	 	case 9:
        	 		e.preventDefault();
        	 		if ($(this).val() == "") {
						alert("please enter amount !");
						$(this).focus();
						return;
					}
        	 		//this.value = this.value.replace(/[^0-9]/g, '');
        	 		$("#buy").focus();
				break;
				case 13:
					e.preventDefault();
        	 		if ($(this).val() == "") {
						alert("please enter amount !");
						$(this).focus();
						return;
					}
        	 		//this.value = this.value.replace(/[^0-9]/g, '');
        	 		$("#buy").focus();

			default:
				break;
			}
        	 
         });
    /**
 	 * Buy button key press event handler
 	 */
     $(document).on("keydown","#buy",function(e){
    	 switch (e.which) {
    	 	 case 13:
    	 		 e.preventDefault();
    	 		 $(this).blur();
    	 		 $(this).trigger('click');
    	 		 return;
    	     break;
    	 	 case 32:
    	 		 e.preventDefault();
    	 		 $(this).blur();
    	 		 $(this).trigger('click');
    	 		 return;
    	 	 break;	 
    	 	 default:
		    	 e.preventDefault();
			 break;
		}
    	 
     });
	         
	 /**
	 * Q and R key press event handler
	 */    
     $(document).bind("keydown",function(e){
    	 switch (e.which) {
    	 	case 81:
    	 		e.preventDefault();
    	 		if (!$("#qpCheck").prop('disabled')) {
    	 			if(!$("#qpCheck").is(":checked")){
	    	 			$("#qpCheck").trigger('click');
	    	 			if ("OneToTwelve" == gameSelected) {
	    	 				$("#numToBePicked").focus();
	    	 				return;
						}
	    	 			$(".unitPrice").first().focus();
	    	 		}else {
						return;
					}
				}else {
					return;
				}
    	 		
			break;
			case 82:
				e.preventDefault();
				var toReset = confirm("Do you want to reset");
				if (toReset){
					resetAllGames();
				} else {
					return false;
				}
			break;	
			default:
			break;
		}
     });    
	
	
	/**
	 * Bet type radio button div click event handler
	 */ 
	 $(document).on('click','.bettypes',function(){
		 $('.bettypes').removeClass('select-bet');
		 $("#tktView").css("display","none");
		 $("#parentApplet").css("display","none");
	     $("#gamePrizeScheme").css("display","block");
		 $(this).addClass('select-bet');
		 $(this).focus();
		 var gameName = '';
		 $("#tktGen").html("");
		 $(".sideMenuList").each(function(){
			 if($(this).hasClass("select-game")){
				 gameName = $(this).children().eq(1).text();
				 return false;
			 }
		 });
		 selectedBetName = getBetType(gameName);
	});

	/**
	 * When quick pick is clicked.
	 */
		$(document).on('click','#qpCheck',function() {
			$("#parentApplet").css("display","none");
			$("#tktView").css("display","none");
		    $("#gamePrizeScheme").css("display","block");
			var gameName = '';
			$("#unitPrice").val(currUnitPrice);
			$("#betAmt").html(currUnitPrice);
			$("#tktGen").html("");
			$(".sideMenuList").each(function(){
				if($(this).hasClass("select-game")){
					gameName = $(this).children().eq(1).text();
					return false;
				}
			});
			quickPickSelected(gameName);
		});
	
	/**
	 * For buy buttom mouse over
	 */
	$(".buy-now").mouseover(function(e){
		if("ZimLottoBonus"==gameSelected && !isValid){
			 $("#buyNowMessage").css('display','block');	
			 $("#buyMessage").html("Either complete the line or select Qp!!! ");
			 return false; 
		}
	    if(noOfSelectedDraws.length == 0 && $("#numOfDrawsSelected").val()==0){
		    $("#buyNowMessage").css('display','block');	
		    $("#buyMessage").html("Please select draws !!! ");
	    }
	    else if("MiniRoulette"!= gameSelected && $("#qpCheck").is(':checked') == false && !isValid ){
    	  	$("#buyNowMessage").css('display','block');	
    		$("#buyMessage").html("Please select either QP or manual numbers  !!! ");
		}
	    else if("FullRoulette"!= gameSelected && $("#qpCheck").is(':checked') == false && !isValid ){
    	  	$("#buyNowMessage").css('display','block');	
    		$("#buyMessage").html("Please select either QP or manual numbers  !!! ");
		}
	    else if( "OneToTwelve"== gameSelected && $("#numToBePicked").val() == "" && !isValid){
	    	$("#buyNowMessage").css('display','block');	
    		$("#buyMessage").html("Please enter number to be picked  !!! ");
	    }
	    else if("MiniRoulette"!= gameSelected &&  $("#otherAmt").val() == 0 && parseFloat($("#tktPrice").text()) == 0 ){
		    $("#buyNowMessage").css('display','block');	
		    $("#buyMessage").html("Please select bet amount !!! ");
	    }
	    else if("FullRoulette"!= gameSelected &&  $("#otherAmt").val() == 0 && parseFloat($("#tktPrice").text()) == 0 ){
		    $("#buyNowMessage").css('display','block');	
		    $("#buyMessage").html("Please select bet amount !!! ");
	    }
	    else if("MiniRoulette"== gameSelected && betCoinAmount == 0 && parseFloat($("#tktPrice").text()) == 0 ){
	   	    $("#buyNowMessage").css('display','block');	
		    $("#buyMessage").html("Please select bet amount !!! ");
	    }
	    else if("FullRoulette"== gameSelected && betCoinAmount == 0 && parseFloat($("#tktPrice").text()) == 0 ){
	   	    $("#buyNowMessage").css('display','block');	
		    $("#buyMessage").html("Please select bet amount !!! ");
	    }
        else if("MiniRoulette"== gameSelected && !isValid ){
	    	$("#buyNowMessage").css('display','block');	
    		$("#buyMessage").html("Please select bet types to proceed !!! ");
	    }    
        else if("FullRoulette"== gameSelected && !isValid ){
	    	$("#buyNowMessage").css('display','block');	
    		$("#buyMessage").html("Please select bet types to proceed !!! ");
	    }
	});
	
	$(".buy-now").mouseout(function(e){
		$("#buyNowMessage").css('display','none');	
		$("#buyMessage").html("");
	});

	/**
	 * For Sale
	 */
	$("#buy").on('click',function(){
		if(cardInfoReq=='TRUE'){
			cardNo='';
			$("#card-no").css("display","block");
			$("#cardNo").attr("readonly",false);
			$("#cardNo").val("");
			$("#cardNo").focus();
			$("#error-message1").html("");
		}else{
			purchaseTicket();
		}
		
	});
	
	$("#cardOk").on('click',function(){
		if($("#cardNo").val()==''){
			$("#error-message1").html("Please enter card no to submit card detail");
			return false;
		}
		$("#card-no").css("display","none");
		cardNo=$("#cardNo").val();
		purchaseTicket();
	});
	
	
	$("#cardClose,#cardCancel").on('click',function(){
		$("#cardNo").val("");
		$("#card-no").css("display","none");
		purchaseTicket();
	});
			
	function purchaseTicket(){
		$(".sideMenuList").each(function(){
			 if($(this).hasClass("select-game")){
				 gameName = $(this).children().eq(1).text();
				 return false;
			 }
		 });
	    if("TwelveByTwentyFour" == gameName){
	    	actionURL = path+'/com/skilrock/lms/web/drawGames/playMgmt/twelveByTwentyFourBuy.action';
	    }
        else if ("OneToTwelve" == gameName) {
			actionURL = path+'/com/skilrock/lms/web/drawGames/playMgmt/oneToTwelveBuy.action';
		}
        else if ("PickThree"== gameName ) {
        	actionURL = path+'/com/skilrock/lms/web/drawGames/playMgmt/pickThreeBuy.action';
		}
        else if ("KenoSix"== gameName) {
        	actionURL = path+'/com/skilrock/lms/web/drawGames/playMgmt/kenoSixBuy.action';
		}
	    else if ("PickFour"== gameName) {
			actionURL = path+'/com/skilrock/lms/web/drawGames/playMgmt/pickFourBuy.action';
		}
	    else if ("MiniRoulette"== gameName) {
			actionURL = path+'/com/skilrock/lms/web/drawGames/playMgmt/oneToTwelveRouletteBuy.action';
		}
	    else if ("KenoTwo"== gameName) {
			actionURL = path+'/com/skilrock/lms/web/drawGames/playMgmt/kenoTwoBuy.action';
		}
	    else if ("FullRoulette"== gameName) {
			actionURL = path+'/com/skilrock/lms/web/drawGames/playMgmt/fullRouletteBuy.action';
		}
	    else if ("ZimLottoBonus"== gameName) {
			actionURL = path+'/com/skilrock/lms/web/drawGames/playMgmt/zimLottoBonusBuy.action';
		}
	    else if ("TenByTwenty"== gameName) {
			actionURL = path+'/com/skilrock/lms/web/drawGames/playMgmt/tenByTwentyBuy.action';
		}
	    var drawIds = [];
		var betTypeData = [];
		var totalPurchaseAmt = parseFloat($("#tktPrice").text());
		var isAdvancedPlay = false;
	    if ("MiniRoulette" == gameName) {
	    	var betTypes = [];
	    	for(var rouletteBetType in miniRouletteBetAmt){
	    		if("roulette" == rouletteBetType){
	    			for(var i=1;i<=12;i++){
	    				if(miniRouletteBetAmt[rouletteBetType].betAmt[i]!=0){
	    					var panelData = {
	    						        "noPicked": "1",
	    						        "betAmtMul": 1,
	    						        "isQp": false,
	    						        "pickedNumbers": i<10?"0"+i:i,
	    						        "betName": rouletteBetType,
	    						        "unitPrice": miniRouletteBetAmt[rouletteBetType].betAmt[i],
	    						        "noOfLines": 1
	    					};
	    					betTypeData.push(panelData);
	    				}
	    			}
	    		}else{
	    			if(miniRouletteBetAmt[rouletteBetType].betAmt!=0){
			    		var panelData = {
							        "noPicked": miniRouletteBetTypes[rouletteBetType].numbers.length,
							        "betAmtMul": 1,
							        "isQp": false,
							        "pickedNumbers": ""+miniRouletteBetTypes[rouletteBetType].numbers,
							        "betName": rouletteBetType,
							        "unitPrice": miniRouletteBetAmt[rouletteBetType].betAmt,
							        "noOfLines": miniRouletteBetTypes[rouletteBetType].numbers.length
						};
						betTypeData.push(panelData);
	    			}
	    		}
	    	}
	    }else if("FullRoulette" == gameName){
	    	var betTypes = [];
	    	for(var rouletteBetType in fullRouletteBetAmt){
	    		if("roulette" == rouletteBetType){
	    			for(var i=1;i<=36;i++){
	    				if(fullRouletteBetAmt[rouletteBetType].betAmt[i]!=0){
	    					var panelData = {
	    						        "noPicked": "1",
	    						        "betAmtMul": 1,
	    						        "isQp": false,
	    						        "pickedNumbers": i<10?"0"+i:i,
	    						        "betName": rouletteBetType,
	    						        "unitPrice": fullRouletteBetAmt[rouletteBetType].betAmt[i],
	    						        "noOfLines": 1
	    					};
	    					betTypeData.push(panelData);
	    				}
	    			}
	    }else{
	    	if(fullRouletteBetAmt[rouletteBetType].betAmt!=0){
		 		var panelData = {
						        "noPicked": fullRouletteBetTypes[rouletteBetType].numbers.length,
						        "betAmtMul": 1,
						        "isQp": false,
						        "pickedNumbers": ""+fullRouletteBetTypes[rouletteBetType].numbers,
						        "betName": rouletteBetType,
						        "unitPrice": fullRouletteBetAmt[rouletteBetType].betAmt,
						        "noOfLines": fullRouletteBetTypes[rouletteBetType].numbers.length
					};
					betTypeData.push(panelData);
	    		}
	    	}
	    }
	    
	    }
	    else{
			var betType;
			var betsPicked = [];
			var gameName;
			$(".bettypes").each(function(){
				if($(this).hasClass("select-bet")){
					betsPicked.push($(this).attr('betName')); 
				}
			});
			var isQPPre = false;
			var playType = betType;
			var noPicked = parseInt($("#noOfNumber").text());
			var betAmtMul = $("#unitPrice").val();
			var pickednumbers;
			var isQp =false;
			if('OneToTwelve' == gameName){
				if($("#qpCheck").is(':checked')){
					isQp = true;
					isQPPre = true;
				}
				for(var i=0;i<numbersChosen.length;i++){
					betTypeSel = $("#betTypeSel").text();
					var panelData = {
						      "noPicked": "1",
						      "betAmtMul": parseFloat(betAmtSelected*10)/parseFloat(currUnitPrice*10),
						      "isQp": isQp,
						      "pickedNumbers": (pmsParseInt(numbersChosen[i]) < 10 ? "0" : "") + pmsParseInt(numbersChosen[i]),
						      "betName": betsPicked[0],
						      "QPPreGenerated" : isQPPre
						    };
					betTypeData.push(panelData);
				}
			}else if('ZimLottoBonus' == gameName){
				if($("#qpCheck").is(':checked')){
					isQp = true;
					isQPPre = true;
				}
				if("Perm6"==selectedBetName){
				   numberSelected.push(numbersChosen.toString());	
				}
				pickednumbers = numberSelected;	
				for(var i=0; i < pickednumbers.length; i++){
					betTypeSel = betsPicked[0];
					var panelData = {
						      "noPicked": ("Perm6"==selectedBetName)?numbersChosen.length:"6",
						      "betAmtMul": parseFloat(betAmtSelected*10)/parseFloat(currUnitPrice*10),
						      "isQp": isQp,
						      "pickedNumbers": pickednumbers[i],
						      "betName": betTypeSel,
						      "QPPreGenerated" : isQPPre
						    };
					betTypeData.push(panelData);
				}
			}
			else{
                if($("#qpCheck").is(':checked')){
					isQp = true;
					isQPPre = true;
					if("KenoTwo" == gameName && (selectedBetName == "Perm2" || selectedBetName == "Perm3")){
	                	noPicked = $("#numToBePickedFN").val().replace(/(^\s+|\s+$)/g, "");
	                }
				}
                
				var numbers="";
				for(var i=0;i<numbersChosen.length;i++){
					numbers += (pmsParseInt(numbersChosen[i]) < 10 ? "0" : "") + pmsParseInt(numbersChosen[i]) +",";
				}
				numbers = numbers.substr(0,numbers.length-1);
				pickednumbers = numbers;
				for(var i=0; i<betsPicked.length; i++){
					betTypeSel = betsPicked[i];
					var panelData = {
						      "noPicked": noPicked+"",
						      "betAmtMul": parseFloat(betAmtSelected*10)/parseFloat(currUnitPrice*10),
						      "isQp": isQp,
						      "pickedNumbers": pickednumbers,
						      "betName": betsPicked[i],
						      "QPPreGenerated" : isQPPre
						    };
					betTypeData.push(panelData);
				}
			
			}
	    }
		if(!isDrawManual){
			if(noOfSelectedDraws.length == 1){
				if($(".ad-draw-check-box").first().hasClass('select-checkbox')){
					isAdvancedPlay = false;
				} else {
					isAdvancedPlay = true;
				}
			}else if(noOfSelectedDraws.length > 1){
				isAdvancedPlay = true;
			}
			for(var i=0; i<noOfSelectedDraws.length; i++){
				drawIds.push({"drawId": noOfSelectedDraws[i]});
			}
			numOfDrawsSel = noOfSelectedDraws.length;
		}
		var saleRequest = {
				  	  "commonSaleData": {
					    "isAdvancePlay": isAdvancedPlay,
					    "drawData":drawIds,
					    "noOfDraws": noOfDrawsSelected,
					    "isDrawManual" : isDrawManual,
					    "gameName":gameName
					  },
					  "betTypeData": betTypeData,
					  "noOfPanel": betTypeData.length,
					  "totalPurchaseAmt": totalPurchaseAmt+""
					};
	     saleRequest = JSON.stringify(saleRequest);
	     var isConfirm = confirm("Do you want to purchase");
	     if(!isConfirm){
	    	 return false;
	     }
	     betAmtSelected = 0;
	     noOfDrawsSelected = 1;
	     noOfSelectedDraws = [];
	 	 alreadySelDraw = [];
		 drawsSel = "";
	     var saleRespData = _ajaxCallJson(actionURL,"json="+saleRequest);
	     if (saleRespData == "NETERROR") {
	 		$("#error-popup").css('display','block');	
	 		$("#error").html("Server not reachable, Please check connection and try again !!!");
	 		$('#error-popup').delay(2000).fadeOut('slow');
	 		return;
	 	}
	     if(!saleRespData.isSuccess){
	    	resetAllGames();
	    	$(".dg-radio-div").removeClass('select-radio');
	    	$(".dg-radio-div").each(function(){
	    		if($(this).children().first().attr('betName') == betTypeSel){
	    			$(this).addClass('select-radio');
	    			$(this).children().first().prop('checked',true);
	    			$(this).trigger('click');
	    		}
	    	});
	    	$("#betTypeSel").html(betTypeSel);
	    	if(saleRespData.errorMsg != null && saleRespData.errorMsg != "" ){
	    		$("#error-popup").css('display','block');	
	    		$("#error").html(saleRespData.errorMsg);
	    		$('#error-popup').delay(2000).fadeOut('slow');
			}else {
				$("#error-popup").css('display','block');	
				$("#error").html("Internal System Error !!!");
				$('#error-popup').delay(2000).fadeOut('slow');
			}  		
	     }else {
	    	 finalSaleRespData = {};
	    	 //var saleRespData = {"isSuccess":true,"errorMsg":"","mainData":{"commonSaleData":{"ticketNumber":"70120407017128018","gameName":"12/24Game","purchaseDate":"2016-03-10","purchaseTime":"11: 08: 10","purchaseAmt":10,"drawData":[{"drawId":"318","drawDate":"10-03-2016","drawName":"Thursday12/24","drawTime":"18: 00: 00"}]},"betTypeData":[{"isQp":true,"betName":"Direct12","pickedNumbers":"02, 03, 05, 06, 10, 12, 14, 15, 16, 17, 18, 19","numberPicked":"12","unitPrice":10,"noOfLines":1,"betAmtMul":1,"panelPrice":10}],"advMsg":{"BOTTOM":["bottom msg for all dg"],"TOP":["12-24 LOTTO NOW MATCH 12 IS JACKPOT OF N 2.50 MILLIONS WINNERS SHARE JACKPOT EQUALLY.","top dg message for all game"]},"orgName":"TestRetailer","userName":"testret","isPromo":false}};	    	 
	    	 curTrx = "Buy";
	    	 if("OneToTwelve" == gameName || "MiniRoulette" == gameName || "FullRoulette" == gameName ){
	    		 var finalSaleRespData = { 
	    				 					 "serviceName" : "DGE",
		    			 					 "mode" : curTrx,
		    			 					 "gameDevName" : gameName,
											 "orgName" : organizationName,
											 "advMsg" : saleRespData.mainData.advMessage, 
											 "retailerName" : saleRespData.mainData.userName,
											 "brCd" :  saleRespData.mainData.commonSaleData.barcodeCount, 
											 "ticketNumber" : saleRespData.mainData.commonSaleData.ticketNumber+"0",
											 "gameName" : saleRespData.mainData.commonSaleData.gameName,
											 "purchaseDate" :dateFormat(new Date(getCompataibleDate2(saleRespData.mainData.commonSaleData.purchaseDate)),dateFormat.masks.sikkimDate),
											 "purchaseTime" : getCompataibaleTime(saleRespData.mainData.commonSaleData.purchaseTime),
											 "purchaseAmt" : saleRespData.mainData.commonSaleData.purchaseAmt,
											 "betTypeData" : saleRespData.mainData.betTypeData,
											 "currencySymbol" : currencySymbol, 
											 "isPromo" : saleRespData.mainData.isPromo,
											 "drawData" : saleRespData.mainData.commonSaleData.drawData,
											 "cardNo":cardNo,
											 "countryDeployed":countryDeployed,
											 "parentOrgName":saleRespData.mainData.parentOrgName
	    		 };
	    	 }else{
		    	 var finalSaleRespData = { 
							    			 "serviceName" : "DGE",
						 					 "mode" : curTrx,		 
											 "orgName" : organizationName,
						 					 "gameDevName" : gameName,
											 "advMsg" : saleRespData.mainData.advMessage, 
											 "retailerName" : saleRespData.mainData.userName,
											 "brCd" :  saleRespData.mainData.commonSaleData.barcodeCount, 
											 "ticketNumber" : saleRespData.mainData.commonSaleData.ticketNumber+"0",
											 "gameName" : saleRespData.mainData.commonSaleData.gameName,
											 "purchaseDate" :dateFormat(new Date(getCompataibleDate2(saleRespData.mainData.commonSaleData.purchaseDate)),dateFormat.masks.sikkimDate),
											 "purchaseTime" :getCompataibaleTime(saleRespData.mainData.commonSaleData.purchaseTime),											 "purchaseAmt" : saleRespData.mainData.commonSaleData.purchaseAmt,
											 "isQp" : saleRespData.mainData.betTypeData[0].isQp,
											 "betName" : saleRespData.mainData.betTypeData[0].betName,
											 "unitPrice" : saleRespData.mainData.betTypeData[0].unitPrice,
											 "noOfLines" : saleRespData.mainData.betTypeData[0].noOfLines,
											 "betAmtMul" : saleRespData.mainData.betTypeData[0].betAmtMul,
											 "panelPrice" : saleRespData.mainData.betTypeData[0].panelPrice,
											 "currencySymbol" : currencySymbol, 
											 "isPromo" : saleRespData.mainData.isPromo,
											 "drawData" : saleRespData.mainData.commonSaleData.drawData,
											 "pickedNumbers" : saleRespData.mainData.betTypeData[0].pickedNumbers,
											 "numbersPicked" : saleRespData.mainData.betTypeData[0].numberPicked,
											 "cardNo":cardNo,
											 "countryDeployed":countryDeployed,
											 "parentOrgName":saleRespData.mainData.parentOrgName
	    	 };
 	}
	        setDefaultBetType();
	        printData = JSON.stringify(finalSaleRespData);
	        tktNum = finalSaleRespData.ticketNumber;
			$("#tktGen").html("");
	    	$("#gamePrizeScheme").css("display","none");
	    	if("YES" == printUsingApplet){
	    		setAppletData(printData);
	    	}else{
	    		$("#tktView").css("display","block");
	    		setAppData(printData);
	    	}
	    }
	}
	
	/**
	 * Reprint Ticket
	 */
	$("#reprint").on("click",function(){
		if(userActionList.indexOf("reprintTicket")==-1){
		  $("#error-popup").css('display','block');	
		  $("#error").html("Unauthorised Access, Please Contact With Agent !!!");
		  $('#error-popup').delay(2000).fadeOut('slow');	
		  return;
		}
		$("#tktGen").html("");
		var respData = _ajaxCallJson(path+"/com/skilrock/lms/web/drawGames/playMgmt/reprintTicket.action","");
		if (respData == "NETERROR") {
			$("#error-popup").css('display','block');	
			$("#error").html("Server not reachable, Please check connection and try again !!!");
			$('#error-popup').delay(2000).fadeOut('slow');
			return;
		} 
		if(respData.isSuccess){
			curTrx = "Reprint";
			if('OneToTwelve' == respData.mainData.commonReprintData.gameDevName || 'MiniRoulette' == respData.mainData.commonReprintData.gameDevName|| 'FullRoulette' == respData.mainData.commonReprintData.gameDevName){
	    		 var reprintData = { 
	    				 	   'serviceName' : "DGE",
		    				   'mode':curTrx,
							   'orgName':organizationName,
							   'advMsg':respData.mainData.advMessage,
							   'gameDevName' : respData.mainData.commonReprintData.gameDevName,
							   'retailerName':respData.mainData.userName,
							   'brCd':respData.mainData.commonReprintData.barcodeCount,
							   'ticketNumber':respData.mainData.commonReprintData.ticketNumber,
							   'gameName':respData.mainData.commonReprintData.gameName,
							   'purchaseDate':dateFormat(new Date(getCompataibleDate2(respData.mainData.commonReprintData.purchaseDate)),dateFormat.masks.sikkimDate),
							   'purchaseTime':getCompataibaleTime(respData.mainData.commonReprintData.purchaseTime.split(".")[0]),
							   'purchaseAmt':respData.mainData.commonReprintData.purchaseAmt,
							   'betTypeData' : respData.mainData.betTypeData,
							   'currencySymbol':currencySymbol,
							   'isPromo':respData.isPromo,
							   'drawData':respData.mainData.commonReprintData.drawData,
							   'countryDeployed':countryDeployed,
							   'parentOrgName':respData.mainData.parentOrgName
	    		 };
	    	 } else{
	    		 var reprintData = {
	    				 	   'serviceName' : "DGE",
		    				   'mode':curTrx,
							   'orgName':organizationName,
						       'advMsg':respData.mainData.advMessage,
							   'gameDevName' : respData.mainData.commonReprintData.gameDevName,							   
							   'retailerName':respData.mainData.userName,
							   'brCd':respData.mainData.commonReprintData.barcodeCount,
							   'ticketNumber':respData.mainData.commonReprintData.ticketNumber,
							   'gameName':respData.mainData.commonReprintData.gameName,
							   'purchaseDate':dateFormat(new Date(getCompataibleDate2(respData.mainData.commonReprintData.purchaseDate)),dateFormat.masks.sikkimDate),
							   'purchaseTime':getCompataibaleTime(respData.mainData.commonReprintData.purchaseTime.split(".")[0]),
							   'purchaseAmt':respData.mainData.commonReprintData.purchaseAmt,
							   'isQp':respData.mainData.betTypeData[0].isQp,
							   'betName':respData.mainData.betTypeData[0].betName,
							   'pickedNumbers':respData.mainData.betTypeData[0].pickedNumbers,
							   'unitPrice':respData.mainData.betTypeData[0].unitPrice,
							   'noOfLines':respData.mainData.betTypeData[0].noOfLines,
							   'betAmtMul':respData.mainData.betTypeData[0].betAmtMul,
							   'panelPrice':respData.mainData.betTypeData[0].panelPrice,
							   'currencySymbol':currencySymbol,
							   'isPromo':respData.isPromo,
							   'drawData':respData.mainData.commonReprintData.drawData,
							   'countryDeployed':countryDeployed,
							   'parentOrgName':respData.mainData.parentOrgName
	    		 };
	    	 }
			reprintCount = reprintData.reprintCount;
			printData = JSON.stringify(reprintData);
			//console.log(printData);
			$("#tktGen").html("");
	    	$("#gamePrizeScheme").css("display","none");
	    	if("YES" == printUsingApplet){
	    		setAppletData(printData);
	    	}else{
	    		$("#tktView").css("display","block");
	    		setAppData(printData);
	    	}
	    	//prepareReprintTicket(JSON.parse(printData),'Reprint');
		}else{
			resetAllGames();
			if(respData.errorMsg != null && respData.errorMsg != "" ){
				$("#error-popup").css('display','block');	
				$("#error").html(respData.errorMsg);
				$('#error-popup').delay(2000).fadeOut('slow');
			}else {
				$("#error-popup").css('display','block');	
				$("#error").html("Internal System Error !!!");
				$('#error-popup').delay(2000).fadeOut('slow');
			}
		}
	});
	
	/**
	 * Cancel Ticket
	 */
	$("#cancel").on("click",function(){
		if(userActionList.indexOf("cancelTicket")==-1){
		  $("#error-popup").css('display','block');	
		  $("#error").html("Unauthorised Access, Please Contact With Agent !!!");
		  $('#error-popup').delay(2000).fadeOut('slow');	
		  return;
		}
		var toCancel = false;
		toCancel = confirm("Do you want to cancel last ticket ?");
		if (toCancel) {
			var data = {"ticketNumber":null, "autoCancel":false};
			data = JSON.stringify(data);			
			var respData = _ajaxCallJson(path+"/com/skilrock/lms/web/drawGames/playMgmt/cancelTicket.action?json="+data,"");			
			curTrx = "Cancel";
			if (respData == "NETERROR") {
				$("#error-popup").css('display','block');	
				$("#error").html("Server not reachable, Please check connection and try again !!!");
				$('#error-popup').delay(2000).fadeOut('slow');
				return;
			} 
			if(respData.isSuccess){
				var cancelData = {
								'serviceName' : "DGE",
								'mode': curTrx,
								'ticketNumber':respData.mainData.ticketNumber,
								'orgName':organizationName,
								'gameName':respData.mainData.gameDispName,
								'gameDispName':respData.mainData.gameDispName,
								'refundAmount':respData.mainData.refundAmount,
								'cancelTime':respData.mainData.cancelTime,
								'advMsg':respData.mainData.advMessage,
								'currencySymbol':currencySymbol,
								'countryDeployed':countryDeployed,
								'parentOrgName':respData.mainData.parentOrgName
							};
				
				
				printData = JSON.stringify(cancelData);
				$("#tktGen").html("");
		    	$("#gamePrizeScheme").css("display","none");
				//var data1 = "{'mode':'Cancel','ticketNumber':'195634067469470030','purchaseAmt':25.00,'orgName':'WINLOT','gameName':'TwelveByTwentyFour','gameDispName':'12/24 Game','refundAmount':455,'cancelTime':'2016-03-07 17:04:48','advMsg':{'BOTTOM':['bottom msg for all dg'],'TOP':['12-24 LOTTO NOW MATCH 12 IS JACKPOT OF N 2.50 MILLIONS WINNERS SHARE JACKPOT EQUALLY.','top dg message for all game']},'currencySymbol':'Rs'}";		    	
		    	if("YES" == printUsingApplet){
		    		setAppletData(printData);
		    	}else{
		    		$("#tktView").css("display","block");
		    		setAppData(printData);
		    	}
		    	//prepareCancelTicket(JSON.parse(printData));
			}else{
				resetAllGames();
				if(respData.errorMsg != null && respData.errorMsg != "" ){
					$("#error-popup").css('display','block');	
					$("#error").html(respData.errorMsg);
					$('#error-popup').delay(2000).fadeOut('slow');
				}else {
					$("#error-popup").css('display','block');	
					$("#error").html("Internal System Error !!!");
					$('#error-popup').delay(2000).fadeOut('slow');
				}
			}
			
		} else {
			return false;

		}
	});
	
	/**
	 * For PWT
	 */
	$("#pwtOk").on('click',function(){
		var payPwtResp;
		var pwtVerifyResp;
		var totalWinAmt = 0;
		var tktStatus;
		var tktNumber = $("#pwtTicket").val();
		if(!isNaN(tktNumber)){
			if(tktNumber.length < 1){
				$("#error-message").html("Please Enter Ticket Number !!!");
				return false;
			}
			if(tktNumber.length == 18 || tktNumber.length == 20){
				var data = {"ticketNumber":tktNumber};
				data = JSON.stringify(data);
				pwtVerifyResp = _ajaxCallJson(path+'/com/skilrock/lms/web/drawGames/pwtMgmt/verifyTicket.action?json='+data, '');
				if (pwtVerifyResp == "NETERROR") {
					$('#pwt-win').css('display','none');
					$("#error-popup").css('display','block');	
					$("#error").html("Server not reachable, Please check connection and try again !!!");
					$('#error-popup').delay(2000).fadeOut('slow');
					return;
				} 
				/*pwtVerifyResp = '{"isSuccess":true,"advMsg":{},"gameName":"Power Play","ticketNumber":"134335140242140510","totalWinAmt":400,"totalPay":400,"orgName":"sikkim","retailerName":"testret","reprintCountPwt":"0","currencySymbol":"usd","drawData":[{"drawDate":"19-05-2016","drawTime":"19:45:00","winStatus":"RES_AWAITED"},{"drawDate":"21-05-2016","drawTime":"19:45:00","winStatus":"NORMAL_PAY","winningAmt":"400.0"}]}';
				pwtVerifyResp = JSON.parse(pwtVerifyResp);*/
			}else{
				$("#error-message").html("Invalid Ticket Number !!!");	
				return false;
			}
		}else{
			$("#error-message").html(" Invalid Ticket Number !!!");
			return false;
		}
		if(pwtVerifyResp.isSuccess){
			var drawWinStatus = false;
			var drawWinAmt = 0.0;
			var isPay = false;
			$(pwtVerifyResp.drawData).each(function() {
			    if (this.winStatus == "Win!!") {
	                totalWinAmt = this.winningAmt;
	                drawWinStatus = true;
	                drawWinAmt += parseFloat(totalWinAmt);
	                tktStatus = this.winStatus;
			    } else if(this.winStatus != "Result Awaited!!" && this.winStatus != "Win!!"){
			    	if(tktStatus != "Win!!"){
			    	tktStatus = this.winStatus;
			    	}
			    } else {
			    	if(tktStatus != "Win!!"){
			    	tktStatus = this.winStatus;
			    }
			    }
			});
			if('CLAIMED' == tktStatus){
				$('#pwt-win').css('display','none');
				$("#error-popup").css('display','block');		
				$("#error").html("Ticket Already Claimed !!");		
				$('#error-popup').delay(2000).fadeOut('slow');
			}
			else if (!drawWinStatus) {
				$('#pwt-win').css('display','none');
				$("#error-popup").css('display','block');		
				$("#error").html(tktStatus);		
				$('#error-popup').delay(2000).fadeOut('slow');
			} else {
			    isPay = confirm("Winning is " + drawWinAmt + "\n Do you want to pay ?");
	
			    if (isPay) {
			        var data = {
			            "main": pwtVerifyResp
			        };
			        data = JSON.stringify(data);
			        payPwtResp = _ajaxCallJson(path + '/com/skilrock/lms/web/drawGames/pwtMgmt/payPwtTicket.action?json=' + data, '');
			        if (payPwtResp == "NETERROR") {
			        	$('#pwt-win').css('display','none');
						$("#error-popup").css('display','block');	
						$("#error").html("Server not reachable, Please check connection and try again !!!");
						$('#error-popup').delay(2000).fadeOut('slow');
						return;
					} 
			        /*payPwtResp = '{"isSuccess":true,"advMsg":{},"gameName":"Power Play","ticketNumber":"134335140242140510","totalWinAmt":400,"totalPay":400,"orgName":"sikkim","retailerName":"testret","reprintCountPwt":"0","currencySymbol":"usd","drawData":[{"drawDate":"19-05-2016","drawTime":"19:45:00","drawName":"ABCD","winStatus":"RES_AWAITED"},{"drawDate":"21-05-2016","drawTime":"19:45:00","drawName":"PQRS","winStatus":"NON WIN","winningAmt":"400.0"}],"isReprint":true,"reprintData":{"actionName":"","advMsg":{},"barcodeCount":81,"barcodeType":"","betAmountMultiple":[4],"betDispName":[],"bonus":"","deviceType":"","drawDateTime":["2016-05-19 19:45:00&1013","2016-05-21 19:45:00&1015"],"drawIdTableMap":null,"drawNameList":[],"fortunePurchaseBean":null,"gameDispName":"Power Play","gameId":5,"game_no":0,"isAdvancedPlay":1,"isQuickPick":[2],"lastGameId":0,"lastSoldTicketNo":"","noOfDraws":2,"noOfLines":[1],"noOfPanel":0,"noPicked":[],"noofDrawsForPromo":0,"partyId":13,"partyType":"RETAILER","playType":["First12"],"playerData":["01,02,03,04,05,06,07,08,09,10,11,12"],"playerPicked":[],"plrMobileNumber":"","promoPurchaseBean":null,"promoSaleStatus":"SUCCESS","promotkt":false,"purchaseChannel":"LMS_Web","purchaseTime":"2016-05-19 10:53:45.0","raffelAssociated":false,"raffleDrawIdTableMap":null,"raffleNo":0,"rafflePurchaseBeanList":[],"refMerchantId":"","refTransId":"212","reprintCount":"1","saleStatus":"SUCCESS","serviceId":0,"ticket_no":"13433514024214051","totalPurchaseAmt":4,"unitPrice":[0.5],"userId":0,"userMappingId":0}}';
			        payPwtResp = $.parseJSON(payPwtResp);*/
			        if (payPwtResp.isSuccess) {
			        	curTrx = "PWT";
			        	var reprintData = "";
			        	var drawDataArr  = [];
			        	if(payPwtResp.isReprint){		
			        		if('OneToTwelve' == payPwtResp.gameDevName || 'MiniRoulette' == payPwtResp.gameDevName || 'FullRoulette' == payPwtResp.gameDevName){			        	
			        			reprintData ={
					     				   'orgName': payPwtResp.orgName,
					     				   'advMsg' : payPwtResp.reprintData.advMsg,
					     				   'retailerName':payPwtResp.retailerName,
					     				   'gameDevName' : payPwtResp.gameDevName,
					     				   'brCd':payPwtResp.barcodeCount,
					     				   'ticketNumber':payPwtResp.reprintData.ticket_no,
					     				   'gameName':payPwtResp.gameName,
					     				   'purchaseDate':dateFormat(new Date(getCompataibleDate2(payPwtResp.reprintData.purchaseTime.split(" ")[0])),dateFormat.masks.sikkimDate),
					     				   'purchaseTime':payPwtResp.reprintData.purchaseTime.split(" ")[1],
					     				   'purchaseAmt':payPwtResp.reprintData.totalPurchaseAmt,
					     				   'betTypeData' : payPwtResp.betTypeData,
					     				   'currencySymbol':currencySymbol,
					     				   'drawData': payPwtResp.drawData, 
					     				   'countryDeployed':countryDeployed,
					     				   'parentOrgName':payPwtResp.parentOrgName
					     	    	};
			        			//console.log(reprintData);
			        		}else if('ZimLottoBonus' == payPwtResp.gameDevName){
				        		reprintData ={
					     				   'orgName': payPwtResp.orgName,
					     				   'advMsg' : payPwtResp.reprintData.advMsg,
					     				   'retailerName':payPwtResp.retailerName,
					     				   'gameDevName' : payPwtResp.gameDevName,
					     				   'brCd':payPwtResp.barcodeCount,
					     				   'ticketNumber':payPwtResp.reprintData.ticket_no,
					     				   'gameName':payPwtResp.gameName,
					     				   'purchaseDate':payPwtResp.reprintData.purchaseTime.split(" ")[0],
					     				   'purchaseTime':payPwtResp.reprintData.purchaseTime.split(" ")[1],
					     				   'purchaseAmt':payPwtResp.reprintData.totalPurchaseAmt,
					     				   'isQp':payPwtResp.reprintData.isQuickPick[0] == 1?true :false ,
					     				   'betName':payPwtResp.reprintData.playType,
					     				   'pickedNumbers':payPwtResp.reprintData.playerPicked,
					     				   'unitPrice':payPwtResp.reprintData.unitPrice[0],
					     				   'noOfLines':payPwtResp.reprintData.noOfLines[0],
					     				   'betAmtMul':payPwtResp.reprintData.betAmountMultiple[0],
					     				   'currencySymbol':currencySymbol,
					     				   'drawData': payPwtResp.drawData,
					     				   'countryDeployed':countryDeployed,
					     				   'parentOrgName':payPwtResp.parentOrgName
				        		};
			        		}else{
				        		reprintData ={
					     				   'orgName': payPwtResp.orgName,
					     				   'advMsg' : payPwtResp.reprintData.advMsg,
					     				   'retailerName':payPwtResp.retailerName,
					     				   'gameDevName' : payPwtResp.gameDevName,
					     				   'brCd':payPwtResp.barcodeCount,
					     				   'ticketNumber':payPwtResp.reprintData.ticket_no,
					     				   'gameName':payPwtResp.gameName,
					     				   'purchaseDate':payPwtResp.reprintData.purchaseTime.split(" ")[0],
					     				   'purchaseTime':payPwtResp.reprintData.purchaseTime.split(" ")[1],
					     				   'purchaseAmt':payPwtResp.reprintData.totalPurchaseAmt,
					     				   'isQp':payPwtResp.reprintData.isQuickPick[0] == 1?true :false ,
					     				   'betName':payPwtResp.reprintData.playType[0],
					     				   'pickedNumbers':payPwtResp.reprintData.playerData[0],
					     				   'unitPrice':payPwtResp.reprintData.unitPrice[0],
					     				   'noOfLines':payPwtResp.reprintData.noOfLines[0],
					     				   'betAmtMul':payPwtResp.reprintData.betAmountMultiple[0],
					     				   'currencySymbol':currencySymbol,
					     				   'drawData': payPwtResp.drawData,
					     				   'countryDeployed':countryDeployed,
					     				   'parentOrgName':payPwtResp.parentOrgName
				        		};
			        		}
			        	}
			            var finalPayPwtAppData = {
			            					"serviceName" : "DGE",
							                "mode": curTrx,
							                "advMsg" :payPwtResp.advMsg,
							                "isReprint": payPwtResp.isReprint,
							                "gameName": payPwtResp.gameName,
							                "ticketNumber": payPwtResp.ticketNumber,
							                "totalWinAmt":payPwtResp.totalWinAmt,
							                "totalPay" : payPwtResp.totalPay,
							                "orgName": organizationName,
							                "retailerName": payPwtResp.retailerName, 
							                "reprintCountPwt": payPwtResp.reprintCountPwt, 
											"currencySymbol" : currencySymbol,
							                "drawData": payPwtResp.drawData,
							                "reprintData" : reprintData,
							                "responseMsg": "SUCCESS",
							                "countryDeployed":countryDeployed,
							                "parentOrgName":payPwtResp.parentOrgName
			            };
			            printData = JSON.stringify(finalPayPwtAppData);
   			           // console.log(printData);
			            $("#tktGen").html("");
				    	$("#gamePrizeScheme").css("display","none");
				    	if("YES" == printUsingApplet){
				    		setAppletData(printData);
				    	}else{
				    		$("#tktView").css("display","block");
				    		setAppData(printData);
				    	}
				    	//preparePwtTicket(JSON.parse(printData));
			            $('#pwt-win').css('display','none');
	
			        } else {
			        	$('#pwt-win').css('display','none');		
			        	$("#error-popup").css('display','block');	
						$("#error").html(payPwtResp.errorMsg == null || payPwtResp.errorMsg =="" ?"Internal System Error !!!":payPwtResp.errorMsg);
						$('#error-popup').delay(2000).fadeOut('slow');
			        	return false;
			        }
			    } else {
			    	$('#pwt-win').css('display','none');
			        return false;
			    }
			}
		}else {
			resetAllGames();
			$('#pwt-win').css('display','none');
			if(pwtVerifyResp.errorMsg != null && pwtVerifyResp.errorMsg != "" ){
				$("#error-popup").css('display','block');	
				$("#error").html(pwtVerifyResp.errorMsg);
				$('#error-popup').delay(2000).fadeOut('slow');
			}else {
				$("#error-popup").css('display','block');	
				$("#error").html("Internal System Error !!!");
				$('#error-popup').delay(2000).fadeOut('slow');
			}
		}				        
	});
	
	/**
	 * Show results
	 */
	$(document).on("click","#results",function(){
		resetDateTimePicker();
		 $("#drawTimeSelect").empty();
		 $('<option/>').val(-1).html("Please Select").appendTo('#drawTimeSelect');
		 clearResultPrintErrorDiv();
		var data = {"gameCode":"-1","merchantCode":"LMS"};
		var json = JSON.stringify(data);
		getDrawResult = _ajaxCallJson(path+'/com/skilrock/lms/web/drawGames/playMgmt/fetchDrawGameDataResultPCPOS.action', "json="+json);
		if (getDrawResult == "NETERROR") {
			$("#error-popup").css('display','block');	
			$("#error").html("Server not reachable, Please check connection and try again!!!");
			$('#error-popup').delay(2000).fadeOut('slow');
			return;
		} 
		if(getDrawResult.responseCode==0){
			var numOfGames = 0;
			var drawGameResult = '';
			$("#drawGameSel").html("");
			$(respData.games).each(function(){
				var gameAction = userActionListArray[this.gameCode];
				if(userActionList.indexOf(gameAction)!=-1){
					gamesInfo='<button class="result-ui"><span><img src="'+path+'/com/skilrock/lms/web/drawGames/common/pcposImg/'+this.gameCode+'Result.png" width="105"></span><span style="display:none;">'+this.gameCode+'</span><span class="gm-name-tab span-keno-new\">'+this.gameDispName+'</span></button>';
					//gamesInfo = '<button class="result-ui"><span><img src="'+path+'/com/skilrock/lms/web/drawGames/common/pcposImg/'+this.gameCode+'.png"></span><span style="display:none;">'+this.gameCode+'</span><span class="gm-name-tab">'+this.gameDispName+'</span></button>';
					$("#drawGameSel").append(gamesInfo);
					numOfGames++;
				}
			});
			$("#drawGameSel").addClass("total-"+numOfGames+"-games");
			$("#drawGameSel").children().first().addClass('active-button-result');
			$("#drawGameResults").html("");
			
			var gameSelected = $(".active-button-result").first().children().eq(1).text();
			$(getDrawResult.gameData).each(function(){
				if(this.gameCode == gameSelected){
					gameResult = this.resultData;
					if("TwelveByTwentyFour"==gameSelected){
						drawGameResult +='<div class="result-window-div">';
					} else if("KenoSix" == gameSelected){
						drawGameResult +='<div class="result-div-nd">';
					} else if("ZimLottoBonus" == gameSelected){
						drawGameResult +='<div class="result-div-zlb">';
					}else if("KenoTwo" == gameSelected){
						drawGameResult +='<div class="result-div-knt">';
					} else{
						drawGameResult +='<div class="result-div-2">';
					}
					
					$(gameResult).each(function(){
						var drawDate =(this.resultDate == undefined || this.resultDate == null || this.resultDate == "null"|| this.resultDate=="")?"":dateFormat(new Date(getCompataibleDate3(this.resultDate)),dateFormat.masks.sikkimDate);
						$(this.resultInfo).each(function(){
							var drawName = ((this.drawName == undefined || this.drawName == null || this.drawName == "null"|| this.drawName=="")?"":(this.drawName+" - "));
							var drawTime =(this.drawTime == undefined || this.drawTime == null || this.drawTime == "null"|| this.drawTime=="")?"":this.drawTime;							
							drawGameResult += '<div class="col-span-6"><div class="span-heading"><h4>'+drawName+'</h4><span title="Print" class="printResult" ><img src="'+path+'/LMSImages/images-UI/print.png" alt=""/></span></div><div class="result-div">';
							if("OneToTwelve" == this.gameCode){
								drawGameResult += '<div class="result-pop-dt result-pop-dt-ott" drawDateTime="'+drawDate+' '+drawTime+'"><div class="rs-dr-d">'+drawDate.split(",")[0]+'</div><div class="rs-dr-t">'+drawTime.substr(0,5)+'</div></div>';
							} else if("MiniRoulette" == this.gameCode){
								drawGameResult += '<div class="result-pop-dt result-pop-dt-roulette"drawDateTime="'+drawDate+' '+drawTime+'"><div class="rs-dr-d">'+drawDate.split(",")[0]+'</div><div class="rs-dr-t">'+drawTime.substr(0,5)+'</div></div>';
							} else{
								drawGameResult += '<div class="result-pop-dt" drawDateTime="'+drawDate+' '+drawTime+'"><div class="rs-dr-d">'+drawDate.split(",")[0]+'</div><div class="rs-dr-t">'+drawTime.substr(0,5)+'</div></div>';
							}
							if("KenoSix" == this.gameCode){
								drawGameResult += '<ul class="draw-result-12 draw-result-20">';
							} else if("OneToTwelve" == this.gameCode){
								drawGameResult += '<ul class="draw-result-12 draw-result-ott">';
							} else if("MiniRoulette" == this.gameCode || "FullRoulette" == this.gameCode){
								drawGameResult += '<ul class="draw-result-12 draw-result-roulette">';
							}else{
								drawGameResult += '<ul class="draw-result-12">';
							}
							var l = 0;
							var winNum = this.winningNo.split(',') ;
							if(winNum!=null && winNum !=undefined){
								if("MiniRoulette"==gameSelected || "FullRoulette"==gameSelected){
									$(winNum).each(function(l){
										if(parseInt(winNum[l].split("(")[1].replace(")",""))%2 == 0){
											drawGameResult += "<li class='bxGray'>"+winNum[l].split("(")[1].replace(")","")+"</li>";
										}else if(parseInt(winNum[l].split("(")[1].replace(")","")) == 0){
											drawGameResult += "<li class='bxGreen'>"+winNum[l].split("(")[1].replace(")","")+"</li>";
										}else {
											drawGameResult += "<li class='bxRed'>"+winNum[l].split("(")[1].replace(")","")+"</li>";
										}
										l++;
									});
								}else if("OneToTwelve" == this.gameCode){
									$(winNum).each(function(l){
										drawGameResult += "<li>"+winNum[l].split("(")[1].replace(")","")+"<span>"+oneByTwelveZodiacSigns[parseInt(winNum[l].split("(")[1].replace(")",""))-1]+"</span></li>";
										l++;
									});
								}else {
									$(winNum).each(function(l){
										drawGameResult +='<li>'+winNum[l]+'</li>';
										l++;
									});
								}
							}
							drawGameResult += '</ul></div></div>';
						});
					});
					return false;	
				}
			});
			drawGameResult += '</div>';		
			$("#drawGameResults").append(drawGameResult);
		}else{
			$("#drawGameResults").html("No Result Available !!");
		}
		$("#gameResults").css("display","block");
	});
	
	/**
	 * Show result according to selected game
	 */
	$(document).on("click",".result-ui",function(){
		resetDateTimePicker();
		$("#drawTimeSelect").empty();
		$('<option/>').val(-1).html("Please Select").appendTo('#drawTimeSelect');
		clearResultPrintErrorDiv();
        var drawGameResult = '';
		$(".active-button-result").removeClass("active-button-result");
		$("#drawGameResults").html("");
		var gameSelected = $(this).children().eq(1).text();
		$(getDrawResult.gameData).each(function(){
			if(this.gameCode == gameSelected){
				$(this).addClass('active-button-result');
				gameResult = this.resultData;
				if("TwelveByTwentyFour"==gameSelected){
					drawGameResult +='<div class="result-window-div">';
				} else if("KenoSix" == gameSelected){
					drawGameResult +='<div class="result-div-nd">';
				} else if("ZimLottoBonus" == gameSelected){
					drawGameResult +='<div class="result-div-zlb">';
				}else if("KenoTwo" == gameSelected){
					drawGameResult +='<div class="result-div-knt">';
				} else{
					drawGameResult +='<div class="result-div-2">';
				}
				$(gameResult).each(function(){
					var drawDate =(this.resultDate == undefined || this.resultDate == null || this.resultDate == "null"|| this.resultDate=="")?"":dateFormat(new Date(getCompataibleDate3(this.resultDate)),dateFormat.masks.sikkimDate);
					$(this.resultInfo).each(function(){
						var drawName = ((this.drawName == undefined || this.drawName == null || this.drawName == "null"|| this.drawName=="")?"":(this.drawName+" - "));
						var drawTime =(this.drawTime == undefined || this.drawTime == null || this.drawTime == "null"|| this.drawTime=="")?"":this.drawTime;
						drawGameResult += '<div class="col-span-6"><div class="span-heading"><h4>'+drawName+'</h4><span title="Print" class="printResult" ><img src="'+path+'/LMSImages/images-UI/print.png" alt=""/></span></div><div class="result-div">';
						if("OneToTwelve" == gameSelected){
							drawGameResult += '<div class="result-pop-dt result-pop-dt-ott" drawDateTime="'+drawDate+' '+drawTime+'"><div class="rs-dr-d">'+drawDate.split(",")[0]+'</div><div class="rs-dr-t">'+drawTime.substr(0,5)+'</div></div>';
						} else if("MiniRoulette" == gameSelected || "FullRoulette" == gameSelected){
							drawGameResult += '<div class="result-pop-dt result-pop-dt-roulette" drawDateTime="'+drawDate+' '+drawTime+'"><div class="rs-dr-d">'+drawDate.split(",")[0]+'</div><div class="rs-dr-t">'+drawTime.substr(0,5)+'</div></div>';
						} else{
							drawGameResult += '<div class="result-pop-dt" drawDateTime="'+drawDate+' '+drawTime+'"><div class="rs-dr-d">'+drawDate.split(",")[0]+'</div><div class="rs-dr-t">'+drawTime.substr(0,5)+'</div></div>';
						}
						if("KenoSix" == gameSelected){
							drawGameResult += '<ul class="draw-result-12 draw-result-20">';
						} else if("OneToTwelve" == gameSelected){
							drawGameResult += '<ul class="draw-result-12 draw-result-ott">';
						} else if("MiniRoulette" == gameSelected || "FullRoulette" == gameSelected){
							drawGameResult += '<ul class="draw-result-12 draw-result-roulette">';
						} else{
							drawGameResult += '<ul class="draw-result-12">';
						}
						var l =0;
						var winNum = this.winningNo.split(',') ;
						
						if("MiniRoulette"==gameSelected){
							$(winNum).each(function(l){
								if(parseInt(winNum[l].split("(")[1].replace(")",""))%2 == 0){
									drawGameResult += "<li class='bxGray'>"+winNum[l].split("(")[1].replace(")","")+"</li>";
								}else if(parseInt(winNum[l].split("(")[1].replace(")","")) == 0){
									drawGameResult += "<li class='bxGreen'>"+winNum[l].split("(")[1].replace(")","")+"</li>";
								}else {
									drawGameResult += "<li class='bxRed'>"+winNum[l].split("(")[1].replace(")","")+"</li>";
								}
								l++;
							});
						} else if("FullRoulette" == gameSelected){
							$(winNum).each(function(l){
								if(parseInt(winNum[l])%2 == 0){
									drawGameResult += "<li class='bxGray'>"+winNum[l]+"</li>";
								}else if(parseInt(winNum[l]) == 0){
									drawGameResult += "<li class='bxGreen'>"+winNum[l]+"</li>";
								}else {
									drawGameResult += "<li class='bxRed'>"+winNum[l]+"</li>";
								}
								l++;
							});
						}else if("OneToTwelve" ==gameSelected){
							$(winNum).each(function(l){
								drawGameResult += "<li>"+winNum[l].split("(")[1].replace(")","")+"<span>"+oneByTwelveZodiacSigns[parseInt(winNum[l].split("(")[1].replace(")",""))-1]+"</span></li>";
								l++;
							});
						}else {
							$(winNum).each(function(l){
								drawGameResult +='<li>'+winNum[l]+'</li>';
								l++;
							});
						}
						drawGameResult += '</ul></div></div>';
					});
				});
				return false;	
			}
			// i++;
		});
		drawGameResult += '</div>';
		$("#drawGameResults").append(drawGameResult);
		$(this).addClass("active-button-result");
	});
	
	/**
	 * For Reports
	 */
	$("#reports").on("click",function(){
		betAmtSelected = 0;
		parent.frames[1].location.replace(path+"/com/skilrock/lms/web/drawGames/reportsMgmt/retailer/mainPageReports.jsp");
	});
	
	/**
	 * For Settings
	 */
	$("#settings").on("click",function(){
		$("#error").html("");
		$("#error-popup").css('display','none');
		$("#tktView").css("display","none");
		$("#parentApplet").css("display","none");
	    $("#gamePrizeScheme").css("display","block");
		if($("#open-list-btn").css("display") == "block"){
			$("#open-list-btn").css("display","none");
		} else if($("#open-list-btn").css("display") == "none"){
			$("#open-list-btn").css("display","block");
		}
		
	});
	
	/**
	 * Closing settings when clicked outside
	 */
	$(document).click(function(event) { 
	    if(!$(event.target).closest('#settings').length &&
	       !$(event.target).is('#settings')) {
	        if($("#open-list-btn").css("display") == "block") {
	            $("#open-list-btn").css("display","none");
	        }
	    }        
	});
	
	/**
	 * Manually Entering no. of draws to be selected
	 */
	$(document).on("keypress","#numOfDrawsSelected",function(e){
		if (e.which == 9 || e.which == 13) {
			e.preventDefault();
			if ($(this).val() == "") {
				$("#advDrawSel").children().focus();
				return;
			}else {
				if ("OneToTwelve" == gameSelected) {
					$("#qpCheck").focus();
					return;
				}else if (parseInt($(this).val()) > 0 && parseInt($(this).val()) <= numOfDraws) {
					$(".bettypes").first().focus();
					return;
				}
				$(this).focus();
			}
			
		}
		$("#tktView").css("display","none");
		$("#parentApplet").css("display","none");
	    $("#gamePrizeScheme").css("display","block");
		if(e.which == 8 || e.which == 46){
			drawsSel = "";
			noOfDrawsSelected = 0;
		}
		$("#drawName").html("");
		alreadySelDraw = [];
		noOfSelectedDraws = [];
		$("#noOfAdvDraws").html("...");
		$(".ad-draw-check-box").each(function(){
			if($(this).hasClass('select-checkbox')){
				$(this).children().prop('checked',false); 
				$(this).removeClass("select-checkbox");
			}
		});
		if(!isNaN($(this).val())){
			if(parseInt($(this).val()) > 0 && parseInt($(this).val()) <= numOfDraws){
				$("#error").html("");
				$("#error-popup").css('display','none');
				isDrawManual = true;
				noOfDrawsSelected = parseInt($(this).val());
				drawsSel = noOfDrawsSelected;
				$("#drawName").html("<span>"+parseInt($(this).val())+"</span>");
			} else{
				if(!(e.which == 8)){
					if (parseInt($(this).val()) > 0 && parseInt($(this).val()) <= numOfDraws) {
						$("#error").html("Maximum "+numOfDraws+" draws can be selected!");
						$("#error-popup").css('display','block');		
						$('#error-popup').delay(2000).fadeOut('slow');
						$(this).val("");
						$("#buy").prop("disabled",true);
					}
				}
			}
		} else{
			$(this).val("");
			$("#buy").prop("disabled",true);
		}
		checkIsDrawSelected();
		if("MiniRoulette" == gameSelected){
			updateTicketPriceForMiniRoulette();	
		}else if("FullRoulette" == gameSelected){
			updateTicketPriceForFullRoulette();	
		}else{
			updateTicketPrice(1);
		}
	});
	
	$(document).on("keyup","#numOfDrawsSelected",function(e){
		if(e.which == 9){
			e.preventDefault();
		}
		if(e.which == 8 || e.which == 46){
			drawsSel = "";
			noOfDrawsSelected = 0;
		}
		if(!isNaN($(this).val())){
			if(parseInt($(this).val()) > 0 && parseInt($(this).val()) <= numOfDraws){
				$("#error").html("");
				$("#error-popup").css('display','none');
				isDrawManual = true;
				noOfDrawsSelected = parseInt($(this).val());
				drawsSel = noOfDrawsSelected;
				$("#drawName").html("<span>"+parseInt($(this).val())+"</span>");
				if("OneToTwelve" == gameSelected){
					$(this).focus();
				}else {
					$(this).focus();
				}
			}
		}
		
			if ($(this).val() == "") {
				checkIsDrawSelected();
				if("MiniRoulette" == gameSelected){
					updateTicketPriceForMiniRoulette();	
				}else if ("FullRoulette" == gameSelected){
					updateTicketPriceForFullRoulette();
				}else{
					updateTicketPrice(1);
				}
			return;
		}else if (parseInt($(this).val()) < 0 || parseInt($(this).val()) > numOfDraws) {
			$("#error").html("Maximum "+numOfDraws+" draws can be selected!");
			$("#error-popup").css('display','block');		
			$('#error-popup').delay(2000).fadeOut('slow');
			$(this).val("");
			$("#buy").prop("disabled",true);
		}
		checkIsDrawSelected();
		if("MiniRoulette" == gameSelected){
			updateTicketPriceForMiniRoulette();	
		} else if("FullRoulette" == gameSelected){
			updateTicketPriceForFullRoulette();	
		}else{
			updateTicketPrice(1);
		}	
	});
	
	/**
	 * When blur event is fired on no. of draws selected
	 */
	$(document).on("blur","#numOfDrawsSelected",function(e){
		if (parseInt($(this).val()) > 0 && parseInt($(this).val()) > numOfDraws) {
			$("#error").html("Maximum "+numOfDraws+" draws can be selected!");
			$("#error-popup").css('display','block');		
			$('#error-popup').delay(2000).fadeOut('slow');
			$(this).val("");
			$("#buy").prop("disabled",true);
		}
	});
	
	$(document).on("keydown","#advDrawSel",function(e){
		drawSelArray =[];
		switch(e.which){
			case 13:
				$("#advDrawSel").trigger('click');
				$(".chkBox").first().focus();
			break;
			case 9:
				e.preventDefault();
				$('input[type=checkbox]').each(function () {
					if($(this).is(":checked")){
						drawSelArray.push($(this).val());
					} 
				});
				if(drawSelArray.length >0){
					$(".bettypes").first().focus();
					
				}else {
					if(parseInt($("#numOfDrawsSelected").val()) < 1 || parseInt($("#numOfDrawsSelected").val()) > numOfDraws || $("#numOfDrawsSelected").val() == ""){
						$(this).focus();
						$("#error").html("Please Select Draw!");
						$("#error-popup").css('display','block');		
						$('#error-popup').delay(2000).fadeOut('slow');
						return;
					}
					$(".bettypes").first().focus();
				}
			break;
			default:
				$(this).focus();
            break;	
			}
	});
	
	/**
	 * When clicked on advance draw
	 */
	$("#advDrawSel").on("click",function(){
		$("#advanceDraw").css("display","block");
		$("#advError").html("");
		$("#tktView").css("display","none");
		$("#parentApplet").css("display","none");
	    $("#gamePrizeScheme").css("display","block");
		alreadySelDraw = noOfSelectedDraws;
	});
	
	/**
	 * When draws are selected
	 */
	$(document).on("click",".ad-draw-check-box",function(){
		$("#drawName").html("");
		$("#advError").html("");
		if($(this).hasClass('select-checkbox')){
			$(this).children().prop('checked',false); 
			$(this).removeClass("select-checkbox");
		}else {
			$(this).children().prop('checked',true);
			$(this).addClass("select-checkbox");
		}
		noOfSelectedDraws = [];
		$('.ad-draw-check-box').each(function(){
			if($(this).hasClass("select-checkbox")){
				noOfSelectedDraws.push($(this).children().first().val());
			}
		});
		if(noOfSelectedDraws.length < 1){
			$("#advError").html("Atleast one draw is required to play !!!");
			$(this).parent().children().first().children().focus();
			return false;
		} else{
			noOfDrawsSelected = noOfSelectedDraws.length;
			drawsSel = noOfDrawsSelected;
		}
	});
	
	/**
	 * When clicked on cancel button of advance draw popup
	 */
	$("#drawCancel").on("click",function(){
		$("#drawName").html("");
		$("#advanceDraw").css("display","none");
		$(".ad-draw-check-box").children().prop('checked',false);
		$(".ad-draw-check-box").removeClass("select-checkbox");
		if(alreadySelDraw.length < 1){
			$("#drawName").html("<span>"+$("#numOfDrawsSelected").val()+"</span>");
			noOfSelectedDraws = [];
			isDrawManual = true;
		} else{
			isDrawManual = false;
			$(".ad-draw-check-box").each(function(){
				for(var u = 0; u<alreadySelDraw.length;u++){
					if($(this).children().val() == alreadySelDraw[u]){
						$(this).children().prop('checked',true);
						$(this).addClass("select-checkbox");
						continue;
					}
				}
			});
		}
		if(!isDrawManual){
			$(".ad-draw-check-box").each(function(){
				if($(this).hasClass('select-checkbox')){
					$("#drawName").append("<span>"+$(this).text()+"</span>");
				}
			});
		}else{
			$("#drawName").html("<span>"+$("#numOfDrawsSelected").val()+"</span>");
		}
		noOfSelectedDraws = alreadySelDraw;
		$("#noOfAdvDraws").html(noOfSelectedDraws.length < 1 ?"...":noOfSelectedDraws.length);
		checkIsDrawSelected();
	});
	
	/**
	 * When clicked on submit button of advance draw popup
	 */
	$("#drawSubmit").on("click",function(){
		drawsSel = "";
		$("#drawName").html("");
		if(noOfSelectedDraws.length < 1){
			isDrawManual = true;
			$("#advError").html("Select atleast 1 draw to submit");
		} else {
			isDrawManual = false;
			$("#advError").html("");
			$("#advanceDraw").css("display","none");
			$("#numOfDrawsSelected").val("");
			alreadySelDraw = noOfSelectedDraws;	
		}
		$(".ad-draw-check-box").each(function(){
			if($(this).hasClass('select-checkbox')){
				$("#drawName").append("<span>"+$(this).text()+"</span>");
			}
		});
		$("#noOfAdvDraws").html(noOfSelectedDraws.length < 1 ?"...":noOfSelectedDraws.length);
		checkIsDrawSelected();
		if("MiniRoulette" == gameSelected){
			updateTicketPriceForMiniRoulette();	
		}else if("FullRoulette" == gameSelected){
			updateTicketPriceForFullRoulette();	
		}else{
			updateTicketPrice(1);
		}
	});
	
	/**
	 * When clicked on close button
	 */
	$("#advClose").on("click",function(){
		$("#advanceDraw").css("display","none");
		$("#drawCancel").trigger("click");
	});
	
	
	/**
	 * Selecting bet amount
	 */
	$(document).on("click",".unitPrice",function(){
		$("#tktView").css("display","none");
		$("#parentApplet").css("display","none");
	    $("#gamePrizeScheme").css("display","block");
		$(".unitPrice").removeClass("amt-select");
		$("#tktPrice").html("0");
		if(!isNaN($(this).text())){
			isPriceSel = true;
			betAmtSelected = $(this).text();
			$(this).addClass("amt-select");
			$("#otherAmt").css("display","none");
			$("#otherBtn").css("display","block");
			$("#otherAmt").val("");
		} else{
			isPriceSel = false;
			betAmtSelected = 0;
			$(this).css("display","none");
			$("#otherAmt").css("display","block");
			$("#otherAmt").focus();
			$("#buy").prop("disabled",true);
		}
		checkIsDrawSelected();
		updateTicketPrice(1);
	});
	
	$(document).on("keyup","#otherAmt",function(e){
		if(e.which == 8 || e.which == 46){
			isPriceSel = false;
			$(this).val("");
			$(this).focus();
			$("#tktPrice").html("0");
			$("#error").html("");
			$("#error-popup").css('display','none');
		}
		if(!isNaN($(this).val().replace(/(^\s+|\s+$)/g, ""))){
			if(parseFloat($(this).val().replace(/(^\s+|\s+$)/g, "")) < parseFloat(currUnitPrice)){
				if((parseFloat(($(this).val().replace(/(^\s+|\s+$)/g, ""))*10) % (parseFloat(currUnitPrice))*10) == 0){
					isPriceSel = true;
					betAmtSelected = $(this).val().replace(/(^\s+|\s+$)/g, "");
					$("#error").html("");
					$("#error-popup").css('display','none');
				}
				else{
					isPriceSel = false;
					$(this).focus();
					$("#tktPrice").html("0");
					$("#error").html("Price must be greater than "+currUnitPrice);
					$("#error-popup").css('display','block');		
					$('#error-popup').delay(2000).fadeOut('slow');
				}
			}
			else if(parseFloat($(this).val().replace(/(^\s+|\s+$)/g, "")) > parseFloat(currUnitPrice * currBetAmtMul) || parseFloat(($(this).val().replace(/(^\s+|\s+$)/g, "") * 10) % parseFloat(currUnitPrice*10)) != 0){
				isPriceSel = false;
				$(this).focus();
				$(this).val("");
				betAmtSelected=0;
				$("#tktPrice").html("0");
				$("#error").html("Price must less than "+(currUnitPrice * currBetAmtMul)+" and a multiple of "+currUnitPrice);
				$("#error-popup").css('display','block');		
				$('#error-popup').delay(2000).fadeOut('slow');
			} else{
				isPriceSel = true;
				betAmtSelected = $(this).val().replace(/(^\s+|\s+$)/g, "");
				$("#error").html("");
				$("#error-popup").css('display','none');
			}
		} else{
			isPriceSel = false;
			$(this).focus();
			$(this).val("");
			$("#tktPrice").html("0");
			$("#error").html("Price must be multiple of "+currUnitPrice);
			$("#error-popup").css('display','block');		
			$('#error-popup').delay(2000).fadeOut('slow');
		}
		checkIsDrawSelected();
		updateTicketPrice(1);
	});
	
	/**
	 * Reset the games
	 */
	$("#resetTwelveByTwentyFour").on("click",function(){
		resetAllGames();
	});

	$("#resetKenoSix").on("click",function(){
		resetAllGames();
	});
	
	$("#resetOneByTwelve").on("click",function(){
		resetAllGames();
	});
	
	$("#resetMiniRoulette").on("click",function(){
		resetAllGames();
	});
	
	$("#resetKenoTwo").on("click",function(){
		resetAllGames();
	});
	
	$("#resetFullRoulette").on("click",function(){
		resetAllGames();
	});

	/**
	 * When PWT is clicked
	 */
	$("#pwt").on("click",function(){
		keyupFiredCount=0;
		if(userActionList.indexOf("verifyTicket")==-1){	
		    $("#error-popup").css('display','block');	
		    $("#error").html("Unauthorised Access, Please Contact With Agent !!!");
		    $('#error-popup').delay(2000).fadeOut('slow');
		    $('#pwt-win').css('display','none');
		}else{
			$("#pwt-win").css("display","block");
			$("#tktView").css("display","none");
			$("#parentApplet").css("display","none");
		    $("#gamePrizeScheme").css("display","block");
			$("#pwtTicket").val('');
			$("#error-message").html("");
			$("#pwtTicket").focus();
		}
	});
	
	/**
	 * When cancel option in PWT popup is selected
	 */
	$("#pwtCancel").on("click",function(){
		$("#pwtTicket").val("");
		$("#error-message").html("");
		$("#pwt-win").css("display","none");
	});
	
	
	/**
	 * Hide popups when escape is pressed
	 */
	$(document).on('keyup',function(evt) {
		if (evt.keyCode == 27) {
	    	if($("#gameResults").css("display") == "block"){
	    		$('#gameResults').css("display","none");
	    		$('#selectedDateTimePicker').datetimepicker('hide');
			}
	    	if($("#pwt-win").css("display") == "block"){
	    		$('#pwt-win').css("display","none");
			}
	    	if($("#advanceDraw").css("display") == "block"){
	    		$('#advanceDraw').css("display","none");
	    		$("#drawCancel").trigger('click');
			}
	    	if($("#card-no").css("display") == "block"){
	    		$('#cardClose').click();
			}
	    	if($("#error-popup").css("display") == "block"){
	    		$('#error-popup').css("display","none");
			}
	    }
	});
	
	/**
	 * When close button of error popup is clicked
	 */
	$("#err-popup-button").on("click",function(){
		$("#error-popup").css('display','none');
		$("#error").html("");
	});
	
	/**
	 * When PWT close button is clicked
	 */
	$("#pwtClose").on("click",function(){
		$("#pwt-win").css("display","none");
	});
	
	/**
	 * When result close button is clicked
	 */
	$("#resultClose").on("click",function(){
		$("#gameResults").css("display","none");
	});
	
	$("#testPrint").on("click",function(){
		curTrx = "TestPrint";
		var testPrint = {
				"serviceName" : "DGE",
                "mode": curTrx
		};
		printData = JSON.stringify(testPrint);
		if("YES" == printUsingApplet){
			setAppletData(printData);
    	}else{
    		setAppData(printData);
    	}
	});
	
	$(document).on('click','.sideMenuList',function(){
		drawsSel = "";
		noOfDrawsSelected = 0;
		alreadySelDraw = [];
		noOfSelectedDraws = [];
		$("#numToBePicked").val("");
		$("#tktView").css("display","none");
		$("#parentApplet").css("display","none");
	    $("#gamePrizeScheme").css("display","block");
		$("#tktGen").html("");
		$("#error").html("");
		$("#error-popup").css('display','none');
		$(".dr-view-game").removeClass (function (index,css) {
		    return (css.match (/(^|\s)color-\S+/g) || []).join(' ');
		});
		
		gameSelected = 	$(this).children().eq(1).text();
		//gameSelected =  gameCode;
		$(".sideMenuList").each(function(){
			$(this).removeClass("select-game");
		});
		isValid = false;
		var menuId = $(this).attr('id');
		var gameId = menuId.split('~')[1];
		$(this).addClass("select-game");
		resetTwelveByTwentyFourGame();
		resetPickThreeGame();
		resetPickFourGame();
		resetOneByTwelveGame();
		resetEightyTwentyTenGame();
		resetMiniRoulette();
		resetKenoTwo();
		resetFullRoulette();
		changeBetTypeZimLottoBonus();
		if(!isNaN(gameId)){
			var resetTimer = false;
			updateMidPanel(menuId,gameId,resetTimer);
			$("#allGames").css("display","block");
			$("#resultDiv").css("display","none");
			$("#rightSideArea").css("display","block");
		}
	});
	$(document).on('click','.printResult',function(e){
		clearResultPrintErrorDiv();
		var drawResult=$(this).parent().siblings().children().eq(1).children().map(
				function() {
					return $(this).text(); 
					}).get().join(',');
		var drawName=$(this).parent().siblings().children().eq(0).attr('drawDateTime');
		var printData = JSON.stringify({
			"gameDispName":$(".active-button-result").first().children().eq(2).text(),
			"drawName":drawName,
			"drawResult":drawResult,
			"mode":"PrintResult",
			"serviceName" : "DGE",
			"countryDeployed":countryDeployed
		});
		curTrx="PrintResult";
		if("YES" == printUsingApplet){
			setAppletData(printData);
    	}else{
    		setAppData(printData);
    	}
	});

	$(document).on('click','#printResult',function(e){
		clearResultPrintErrorDiv();
		if("____/__/__"==$("#selectedDateTimePicker").val()){
			$("#noDrawDiv").html("Please select date!!");
			$("#timeErrorDiv").css("display","block");
			$("#timeErrorDiv").delay(2000).fadeOut("slow");
			return;
			
		}
		if($('select#drawTimeSelect option').length==1){
			$("#noDrawDiv").html("No draw available!!");
			$("#timeErrorDiv").css("display","block");
			$("#timeErrorDiv").delay(2000).fadeOut("slow");
			
		}
		if($('select#drawTimeSelect option').length>1 && $("#drawTimeSelect").val()==-1){
			$("#noDrawDiv").html("Please select draw!!");
			$("#timeErrorDiv").css("display","block");
			$("#timeErrorDiv").delay(2000).fadeOut("slow");
		
		}
		if($("#drawTimeSelect").val()!=-1 && (printResDataJson[$("#drawTimeSelect").val()]!=undefined ||printResDataJson[$("#drawTimeSelect").val()]!="")){
			var printDataArr=printResDataJson[$("#drawTimeSelect").val()].split("#");
			var drawName=(printDataArr[0]==null || printDataArr[0]=="null"||printDataArr[0]=="")?"":printDataArr[0];
			var drawDatetime=printDataArr[1]+" "+$("#drawTimeSelect").val();
			var printData = JSON.stringify({
				"gameDispName":$(".active-button-result").first().children().eq(2).text(),
				"drawName":drawName==""?dateFormat(new Date(getCompataibleDate3(drawDatetime)),dateFormat.masks.sikkimDateTime):drawName+" "+dateFormat(new Date(getCompataibleDate3(drawDatetime)),dateFormat.masks.sikkimDateTime),
				"drawResult":("OneToTwelve" == $(".active-button-result").first().children().eq(1).text() || "MiniRoulette"==$(".active-button-result").first().children().eq(1).text() || "FullRoulette"==$(".active-button-result").first().children().eq(1).text())?printDataArr[2].split("(")[1].replace(")",""):printDataArr[2],
				"mode":"PrintResult",
				"serviceName" : "DGE",
				"countryDeployed":countryDeployed,
				//"parentOrgName":saleRespData.mainData.parentOrgName
			});
			curTrx="PrintResult";
			if("YES" == printUsingApplet){
				setAppletData(printData);
	    	}else{
	    		setAppData(printData);
	    	}
		}
		
	});

	$(document).on("focus","#qpCheck",function(){
	   $(this).parent().parent().parent().addClass("qpFocus");
	});

	$(document).on("blur","#qpCheck",function(){
		   $(this).parent().parent().parent().removeClass("qpFocus");
	});
	
});


/**
 * Create Side Menu and Mid Panel Functions.
 */
function updateMidPanel(menuId,gameId,resetTimer){
	clearInterval(freezeTimerIntervalId);
	gameIndex = 0;
	var i = 0;
	var gameCode = '';
	var arr = '';
	isCalled = false;
	timerArr = [];
	if(menuId!=0){
		$("#"+menuId).addClass("selected");
	}
     if(!isSideMenuBuild){
		var drawGameData = _ajaxCallJson(path+'/com/skilrock/lms/web/drawGames/playMgmt/fetchDrawGameDataPCPOS.action', '');
		userActionList = _ajaxCallJson(path+'/com/skilrock/lms/web/drawGames/playMgmt/fetchAllowedUserAction.action', '');
		respData = drawGameData;
		//respData = {"responseCode":0,"responseMsg":"success","games":[{"gameCode":"TwelveByTwentyFour","gameDispName":"Power Play","ticketExpiry":60,"jackpotLimit":0.0,"gameType":0,"timeToFetchUpdatedGameInfo":"15-06-2016 17:35:00","lastDrawResult":"01,02,03,04,05,06,07,08,09,10,11,12","lastDrawTime":"31-05-2016 19:45:00","gameId":5,"bets":[{"unitPrice":0.5,"maxBetAmtMul":100,"betDispName":"Direct12","betCode":1,"betName":"Direct12"},{"unitPrice":0.5,"maxBetAmtMul":100,"betDispName":"First12","betCode":2,"betName":"First12"},{"unitPrice":0.5,"maxBetAmtMul":100,"betDispName":"Last12","betCode":3,"betName":"Last12"},{"unitPrice":0.5,"maxBetAmtMul":100,"betDispName":"AllOdd","betCode":4,"betName":"AllOdd"},{"unitPrice":0.5,"maxBetAmtMul":100,"betDispName":"AllEven","betCode":5,"betName":"AllEven"},{"unitPrice":0.5,"maxBetAmtMul":100,"betDispName":"OddEven","betCode":6,"betName":"OddEven"},{"unitPrice":0.5,"maxBetAmtMul":100,"betDispName":"EvenOdd","betCode":7,"betName":"EvenOdd"},{"unitPrice":0.5,"maxBetAmtMul":100,"betDispName":"JumpEvenOdd","betCode":8,"betName":"JumpEvenOdd"},{"unitPrice":0.5,"maxBetAmtMul":100,"betDispName":"JumpOddEven","betCode":9,"betName":"JumpOddEven"}],"draws":[{"drawId":44,"drawName":"","drawDateTime":"15-06-2016 19:45:00","drawFreezeTime":"15-06-2016 19:30:00"},{"drawId":45,"drawName":"null","drawDateTime":"16-06-2016 19:45:00","drawFreezeTime":"16-06-2016 19:30:00"},{"drawId":46,"drawName":"null","drawDateTime":"17-06-2016 19:45:00","drawFreezeTime":"17-06-2016 19:30:00"}],"displayOrder":1,"drawFrequencyType":"L"},{"gameCode":"KenoSix","gameDispName":"KenoSix","ticketExpiry":7,"jackpotLimit":0.0,"gameType":0,"timeToFetchUpdatedGameInfo":"15-06-2016 17:34:00","lastDrawResult":"1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20","lastDrawTime":"14-05-2016 20:50:00","gameId":3,"bets":[{"unitPrice":10.0,"maxBetAmtMul":2000,"betDispName":"Direct1","betCode":1,"betName":"Pick1"},{"unitPrice":10.0,"maxBetAmtMul":2000,"betDispName":"Direct2","betCode":2,"betName":"Pick2"},{"unitPrice":10.0,"maxBetAmtMul":2000,"betDispName":"Direct3","betCode":3,"betName":"Pick3"},{"unitPrice":10.0,"maxBetAmtMul":2000,"betDispName":"Direct4","betCode":4,"betName":"Pick4"},{"unitPrice":10.0,"maxBetAmtMul":2000,"betDispName":"Direct5","betCode":5,"betName":"Pick5"},{"unitPrice":10.0,"maxBetAmtMul":2000,"betDispName":"Direct6","betCode":6,"betName":"Pick6"},{"unitPrice":10.0,"maxBetAmtMul":2000,"betDispName":"Direct7","betCode":7,"betName":"Pick7"},{"unitPrice":10.0,"maxBetAmtMul":2000,"betDispName":"Direct8","betCode":8,"betName":"Pick8"},{"unitPrice":10.0,"maxBetAmtMul":2000,"betDispName":"Direct9","betCode":9,"betName":"Pick9"},{"unitPrice":10.0,"maxBetAmtMul":2000,"betDispName":"Direct10","betCode":10,"betName":"Pick10"}],"draws":[{"drawId":35,"drawName":"MID WEEK","drawDateTime":"15-06-2016 20:50:00","drawFreezeTime":"15-06-2016 20:30:00"},{"drawId":36,"drawName":"FORTUNE","drawDateTime":"16-06-2016 20:50:00","drawFreezeTime":"16-06-2016 20:30:00"},{"drawId":37,"drawName":"BONANZA","drawDateTime":"17-06-2016 20:50:00","drawFreezeTime":"17-06-2016 20:30:00"}],"displayOrder":1,"drawFrequencyType":"L"}]};		
		if(respData.responseCode==0){
            $("#side-game-menu").html("");
			var sideMenuHtml = '';
			$(respData.games).each(function(){
				var gameAction = userActionListArray[this.gameCode];
				if(userActionList.indexOf(gameAction)!=-1){
					sideMenuHtml = '<button class="sideMenuList" id="'+i+"~"+this.gameId+'" gameName="'+this.gameCode+'"><span class="logo-game"><img src="'+path+'/com/skilrock/lms/web/drawGames/common/pcposImg/'+this.gameCode+'.png" width="90" alt=""></span><span style="display:none;" id="gameCode'+i+'">'+this.gameCode+'</span><span class="next-draw-time"><span class="next-dr-name">Next Draw</span><span class="dr-time-nxt countdate" id="countDown'+i+'" tdate ="'+this.timeToFetchUpdatedGameInfo+'"></span><span class="next-dr-tm"></span></button>';
					$("#side-game-menu").append(sideMenuHtml);
					gameDevelopmentName.push(this.gameCode);
				}
				i++;
			});
			gameCode = gameDevelopmentName[0];
			$(".countdate").each(function(){
				timerArr.push(getCompataibleDate($(this).attr("tdate")));
			});
			if(timerArr.length == 0){
				$("#pc_pos_game_pannel").css('display','none');
				$("#no_game_avail").css('display','block');				
				return;
			} 
			$("#gameSelected").html('<img src="'+path+'/com/skilrock/lms/web/drawGames/common/pcposImg/'+$("#gameCode0").text()+'.png"><span>'+$(".sideMenuList").children().first().children().eq(1).text()+'</span>');
			isSideMenuBuild = true;
		}else {
			if(respData.responseMsg == undefined){
				window.parent.location.reload();
			}else{
				alert(respData.responseMsg);
			}
			return false;
		}
		if(resetTimer){
		 var k = 0;
		 var j = 0;
		 $(respData.games).each(function(k){ 
			 var gameAction = userActionListArray[this.gameCode];
			 if(userActionList.indexOf(gameAction)!=-1){
				 var indexOfGame = gameDevelopmentName.indexOf(this.gameCode);
				 if(respData.games[k].gameCode == "TwelveByTwentyFour")
					 setTimer1("countDown"+k,timerArr[indexOfGame],this.gameId);
				 else if(respData.games[k].gameCode == "KenoSix")
					 setTimer2("countDown"+k,timerArr[indexOfGame],this.gameId);
				 else if(respData.games[k].gameCode == "PickThree")
					 setTimer3("countDown"+k,timerArr[indexOfGame],this.gameId);
				 else if(respData.games[k].gameCode == "PickFour")
					 setTimer4("countDown"+k,timerArr[indexOfGame],this.gameId);
				 else if(respData.games[k].gameCode == "OneToTwelve")
					 setTimer5("countDown"+k,timerArr[indexOfGame],this.gameId);
				 else if(respData.games[k].gameCode == "MiniRoulette")
					 setTimer6("countDown"+k,timerArr[indexOfGame],this.gameId);
				 else if(respData.games[k].gameCode == "KenoTwo")
					 setTimer7("countDown"+k,timerArr[indexOfGame],this.gameId);
				 else if(respData.games[k].gameCode == "ZimLottoBonus")
					 setTimer8("countDown"+k,timerArr[indexOfGame],this.gameId);
				 else if(respData.games[k].gameCode == "FullRoulette")
					 setTimer9("countDown"+k,timerArr[indexOfGame],this.gameId);
				 else if(respData.games[k].gameCode == "TenByTwenty")
					 setTimer10("countDown"+k,timerArr[indexOfGame],this.gameId);
				 j++;
			 }
			 k++;
		 });
		}
	 	 allFreezeTimeArray = timerArr.sort();
		 minTimeToFetchData = allFreezeTimeArray[0];
		 var dateArr = allFreezeTimeArray[0].split(" ")[0].split("/");
		 minTimeToFetchData = dateArr[2]+"/"+dateArr[0]+"/"+dateArr[1] +" "+allFreezeTimeArray[0].split(" ")[1];

	}else{
		var i = 0;
		$(respData.games).each(function(){
			if(this.gameId == gameId){
				gameIndex = i;
				gameCode = this.gameCode;
				return false;
			}
			i++;
		});
	}
	if(!isForTimer){
		$(".sideMenuList").removeClass("select-game");
		var indexOfGame = gameDevelopmentName.indexOf(gameCode);
		$("#side-game-menu").children().eq(indexOfGame).addClass("select-game");
		$("#side-game-menu").children().eq(indexOfGame).focus();
		gameIndex = $(".select-game").attr("id").split('~')[0];
		createMidPanel(gameIndex);
	}else{
		isForTimer = false;
		var gameInFocusAfterFreeze = respData.games[menuId.split('~')[0]].gameCode;
		$(".sideMenuList").removeClass("select-game");
		$(".sideMenuList").each(function(){
			if($(this).attr('gamename') == gameInFocusAfterFreeze){
				$(this).addClass("select-game");
				$(this).focus();
				return false;
			}
		});
		if($(".select-game").children().eq(1).text() == gameInFocus){
			createMidPanel(menuId.split('~')[0]);
		}
	}
	assignFocus();
}
/**
 * Call draw date timer when draw freezes
 */
function callDrawDateTimer(gameId){
	var gIndex = 0;
	var i = 0;
	$(respData.games).each(function(){
		if(this.gameId == gameId){
			gIndex = i;
			return false;
		}
		i++;
	});
	var lastDrawFreezeTime='';
	var lastDrawDateTime='';
	var timerFreezeTime='';
	var timerDateTime='';
	
	if(respData.games[gIndex].lastDrawFreezeTime != undefined && respData.games[gIndex].lastDrawFreezeTime != ''){
		lastDrawFreezeTime=new Date(getCompataibleDate(respData.games[gIndex].lastDrawFreezeTime)).getTime();
	}
	//last Draw Date Time
	if(respData.games[gIndex].lastDrawDateTime!= undefined && respData.games[gIndex].lastDrawDateTime!=''){
		lastDrawDateTime=new Date(getCompataibleDate(respData.games[gIndex].lastDrawDateTime)).getTime();
	}

	if (lastDrawFreezeTime == '' || lastDrawDateTime == '' || serUpdatedCurDate > lastDrawDateTime) {
		//current draw freeze Time
		currDrawFreezeTime=new Date(getCompataibleDate(respData.games[gIndex].draws[0].drawFreezeTime)).getTime();
		timerFreezeTime=currDrawFreezeTime;
		
		//current draw Date Time
		currDrawDateTime=new Date(getCompataibleDate(respData.games[gIndex].draws[0].drawDateTime)).getTime();
		timerDateTime=currDrawDateTime;
	}else {
		timerFreezeTime=lastDrawFreezeTime;
		timerDateTime=lastDrawDateTime;
	}	
	checkCurrentDrawFreezeScreenActive(timerFreezeTime,timerDateTime,"timer-"+respData.games[gIndex].gameCode);
}

/**
 * Creating Draw and Bets Info
 */
function createMidPanel(gameIndex){
	$("#drawName").html("");
	$("#drawInfo").html("");
	$(".ottManualEnter").css("display","block");
	if(respData.responseCode==0){
		noOfDrawsSelected = 1;
		alreadySelDraw = [];
		noOfSelectedDraws = [];
		drawsSel = "";
		var currDrawId =0; 
		var upperDrawDiv = '';
		var lowBetInfoDiv = '';
		var minTime ='';
		var winNum = [];
		var lowerDrawInfoDiv = '';
		var gameCode = respData.games[gameIndex].gameCode;
		gameSelected = gameCode;
		$("#winNum").html("");
		$("#winDraw").html("");
		$("#fortunewinNum").html("");
		$("#winNumRoulette").html("");
		$("#prizeScheme").html("");
		$("#betAmtPrice").html("");
		$("#pickthree").css("display","none");
		$("#pickfour").css("display","none");
		$("#onebytwelve").css("display","none");
		$("#eightytwentyten").css("display","none");
		$("#miniRoulette").css("display","none");
		$("#twelveByTwentyFour").css("display","none");
		$("#kenoTwo").css("display","none");
		$("#fullRoulette").css("display","none");
		$("#tenByTwenty").css("display","none");
		if(respData.games[gameIndex].draws.length > 0){
			currDrawId = respData.games[gameIndex].draws[0].drawId;
		}
		var currBetCode = respData.games[gameIndex].bets[0].betCode;
		var drawDisplay;
		var arr=[];
		allFreezeTimeArray = [];
		$(respData.games).each(function(){
			if(this.draws.length > 0)
			allFreezeTimeArray.push(this.timeToFetchUpdatedGameInfo.split(" ")[0].split("-").reverse().join("/")+" "+this.timeToFetchUpdatedGameInfo.split(" ")[1]);
		});
		
		$("#draw-info-div").html("");
		drawFrequency = respData.games[gameIndex].drawFrequencyType;
		numOfDraws = respData.games[gameIndex].draws.length;
		if(numOfDraws > 0){
			$(respData.games[gameIndex].draws).each(function(){
				drawDisplay=(this.drawName=="null"?"":this.drawName)+' '+dateFormat(new Date(getCompataibleDate(this.drawDateTime)), dateFormat.masks.sikkimDateTime);
				lowerDrawInfoDiv = '<div class="col-span-4"><div class="ad-draw-check-box"><input type="checkbox" name="drawName" class="chkBox" value="'+this.drawId+'"><label for="checkBox1">'+drawDisplay+'</label></div></div>';
				$("#drawInfo").append(lowerDrawInfoDiv); 
			});
		}else {
			 $("#draw-info-div").html("No draw available !!");
		}
		$("#numOfDrawsSelected").val("1");
		$("#rouletteResults").css("display","none");
		isDrawManual = true;
		noOfDrawsSelected = parseInt($("#numOfDrawsSelected").val());
		drawsSel = noOfDrawsSelected;
		$("#drawName").html("<span>"+noOfDrawsSelected+"</span>");
		if(respData.games[gameIndex].lastDrawTime!=null && respData.games[gameIndex].lastDrawTime!=undefined && respData.games[gameIndex].lastDrawTime!="" && respData.games[gameIndex].lastDrawResult!=null && respData.games[gameIndex].lastDrawResult!=undefined && respData.games[gameIndex].lastDrawResult!=""){
			$("#winDraw").css("display","none");
			winNum = respData.games[gameIndex].lastDrawWinningResult[0].winningNumber.split(",");
			tempDraw=dateFormat(new Date(getCompataibleDate(respData.games[gameIndex].lastDrawWinningResult[0].lastDrawDateTime)), dateFormat.masks.sikkimDateTime);
			if("OneToTwelve" == gameCode){
				winNum = respData.games[gameIndex].lastDrawWinningResult;
				$("#othersResults").css("display","block");
				$("#rouletteResults").css("display","none");
				var l = 0;
				var winningNumber = "";
				$(winNum).each(function(){
					var data = dateFormat(new Date(getCompataibleDate(respData.games[gameIndex].lastDrawWinningResult[l].lastDrawDateTime)), dateFormat.masks.sikkimDateTime);
					var arr = data.split(',');
					var arr1 = arr[1].split(' ');
					var arr2 =arr1[2].split(':');
					var num = parseInt(winNum[l].winningNumber.split("(")[1].replace(")",""))<10?("0"+winNum[l].winningNumber.split("(")[1].replace(")","")):winNum[l].winningNumber.split("(")[1].replace(")","");
					winningNumber += "<div class='fortune-re'><div class='fortune-res'><span class='span-re'>"+arr[0]+"</span><span class='span-re1'>"+arr2[0]+":"+arr2[1]+"</span></div><div class='res-ftr'><span class='span-re2'>"+num+"</span><span class='span-re3'>"+oneByTwelveZodiacSigns[parseInt(winNum[l].winningNumber.split("(")[1].replace(")",""))-1]+"</span></div></div>";
					l++;
					if(l==5){
						return false;
					}
				});
				$("#fortunewinNum").css("display","block");
				$("#fortunewinNum").append(winningNumber);
			}else if("MiniRoulette" == gameCode){
				$("#othersResults").css("display","none");
				$("#rouletteResults").css("display","block");
				winNum = respData.games[gameIndex].lastDrawWinningResult;
				var winningNumber = "";
				var l = 0;
				$(winNum).each(function(){
					var num = parseInt(winNum[l].winningNumber.split("(")[1].replace(")",""));
					var data = dateFormat(new Date(getCompataibleDate(respData.games[gameIndex].lastDrawWinningResult[l].lastDrawDateTime)), dateFormat.masks.sikkimDateTime);
					var arr = data.split(',');
					var arr1 = arr[1].split(' ');
					var arr2 =arr1[2].split(':');
					if(num == 0){
						winningNumber += "<li class='bxGreen'><span class='draw_time'>"+arr[0]+"</span><span class='draw_time'>"+arr2[0]+":"+arr2[1]+"</span><span class='draw_number'>"+winNum[l].winningNumber.split("(")[1].replace(")","")+"</span></li>";
					} else if(num%2 == 0){
						winningNumber += "<li class='bxGray'><span class='draw_time'>"+arr[0]+"</span><span class='draw_time1'>"+arr2[0]+":"+arr2[1]+"</span><span class='draw_number'>"+winNum[l].winningNumber.split("(")[1].replace(")","")+"</span></li>";
					}else{
						winningNumber += "<li class='bxRed'><span class='draw_time'>"+arr[0]+"</span><span class='draw_time1'>"+arr2[0]+":"+arr2[1]+"</span><span class='draw_number'>"+winNum[l].winningNumber.split("(")[1].replace(")","")+"</span></li>";				
					}
					l++;
				});
				$("#winNumRoulette").append(winningNumber);
			}else if("FullRoulette" == gameCode){
				$("#othersResults").css("display","none");
				$("#rouletteResults").css("display","block");
				winNum = respData.games[gameIndex].lastDrawWinningResult;
				var winningNumber = "";
				var l = 0;
				$(winNum).each(function(){
					var num = parseInt(winNum[l].winningNumber);
					var data = dateFormat(new Date(getCompataibleDate(respData.games[gameIndex].lastDrawWinningResult[l].lastDrawDateTime)), dateFormat.masks.sikkimDateTime);
					var arr = data.split(',');
					var arr1 = arr[1].split(' ');
					var arr2 =arr1[2].split(':');
					if(num == 0){
						winningNumber += "<li class='bxGreen'><span class='draw_time'>"+arr[0]+"</span><span class='draw_time'>"+arr2[0]+":"+arr2[1]+"</span><span class='draw_number'>"+winNum[l].winningNumber+"</span></li>";
					} else if(num%2 == 0){
						winningNumber += "<li class='bxGray'><span class='draw_time'>"+arr[0]+"</span><span class='draw_time1'>"+arr2[0]+":"+arr2[1]+"</span><span class='draw_number'>"+winNum[l].winningNumber+"</span></li>";
					}else{
						winningNumber += "<li class='bxRed'><span class='draw_time'>"+arr[0]+"</span><span class='draw_time1'>"+arr2[0]+":"+arr2[1]+"</span><span class='draw_number'>"+winNum[l].winningNumber+"</span></li>";				
					}
					l++;
				});
				$("#winNumRoulette").append(winningNumber);
			}else {
				$("#winDraw").css("display","block");
				$("#fortunewinNum").css("display","none");
				
				var l = 0;
				$("#othersResults").css("display","block");
				$("#rouletteResults").css("display","none");
				var data = dateFormat(new Date(getCompataibleDate(respData.games[gameIndex].lastDrawWinningResult[0].lastDrawDateTime)), dateFormat.masks.sikkimDateTime);
				var arr = data.split(',');
				var arr1 = arr[1].split(' ');
				var arr2 =arr1[2].split(':');
				$("#winDraw").html("<span class='month-span'>"+arr[0]+"</span><span class='time-span'>"+arr2[0]+":"+arr2[1]+"</span>");
				$(winNum).each(function(l){
					if("ZimLottoBonus" == gameCode){
						var winningNumber = "<li class='zimResultUi' ><span>"+winNum[l]+"</span></li>";
					}
					else{
						var winningNumber = "<li><span>"+winNum[l]+"</span></li>";	
					}
					$("#winNum").append(winningNumber);
					l++;
				});
			}
			if("KenoSix" == gameCode){
				$(".results-number").addClass("kenoresult");
			} else{
				$(".results-number").removeClass("kenoresult");
			}
		}
		currUnitPrice = respData.games[gameIndex].bets[0].unitPrice;
		currBetAmtMul = respData.games[gameIndex].bets[0].maxBetAmtMul;
		$("#betList").text(respData.games[gameIndex].bets[0].betDispName);
		selectedBetName = respData.games[gameIndex].bets[0].betDispName;
		selectedBetCode = respData.games[gameIndex].bets[0].betCode;
		$("#gameName").html('');
		$("#gameName").text(respData.games[gameIndex].gameCode);
		$("#bet-info-div").html("");
		$(respData.games[gameIndex].bets).each(function(){
			lowBetInfoDiv = '<button class="bettypes" id="'+this.betCode+'" value="'+this.betCode+'" betName="'+this.betDispName+'" allBetInfo="'+this.unitPrice+"~"+this.maxBetAmtMul+'">'+this.betName+'</button>';
			$("#bet-info-div").append(lowBetInfoDiv);
			if(respData.games[gameIndex].bets.length < 2){
				$("#betTypeSection").css("display","none");
			} else{
				$("#betTypeSection").css("display","block");
			}
		});
		$("#numbertopicked").css("display","none");
		$("#zimLottoBonusQp").css("display","none");
		$("#numToBePickedFN").prop("disabled",true);
		$("#zimLottoBonusQp").css("display","none");
		$("#zimLottoBonus").css("display","none");
		$("#directSix").css("display","none");
		$("#kenoTwoQp").css("display","none");
		$("#zimQpTextbox").prop("disabled",true);
		if("OneToTwelve" == gameCode){
			$(".quick-p-picked").addClass("float-left");
			$("#numbertopicked").css("display","block");
		}else if("KenoTwo" == gameCode){
			$(".quick-p").removeClass("float-left");
			$("#kenoTwoQp").css("display","block");
			$("#numToBePickedFN").prop("disabled",true);
		}else if("ZimLottoBonus" == gameCode){
			$(".quick-p").removeClass("float-left");
			$("#zimLottoBonusQp").css("display","block");
			$("#zimQpTextbox").prop("disabled",true);
		}else{
			$(".quick-p").removeClass("float-left");
		}
		$("#otherGamePrizeScheme").css("display","none");
		$("#miniRoulettePrizeScheme").css("display","none");
		$(".list-prize").removeClass("new-prize");
		var gamePrize = "";
		var gameScheme = prizeSchemeArray[gameCode];
		if("KenoSix" == gameCode){
			$("#otherGamePrizeScheme").css("display","block");
			gamePrize = setKenoSixPrizeScheme(selectedBetName);
			$("#prizeScheme").html(gamePrize);
		}else if("MiniRoulette" == gameCode || "FullRoulette" == gameCode){
			$("#miniRoulettePrizeScheme").css("display","block");
			$(".list-prize").addClass("new-prize");
			gamePrize += "<div class='prize-s'><div class='prize-s1'><span class='prize-sp'>Win </span><span class='prz'><i class='fa fa-usd'></i>120</span><span class='prize-sp'>for bet of </span><span class='prz'><i class='fa fa-usd'></i>10</span></div></div>";
			$("#miniRoulettePrizeScheme").html(gamePrize);
		}else{
			$("#otherGamePrizeScheme").css("display","block");
			for(var i in gameScheme){
				gamePrize += "<tr><td>"+i+"</td><td>"+gameScheme[i]+"</td></tr>";
			}
			$("#prizeScheme").html(gamePrize);
		}
		var basePrices = priceArray[gameCode];
		var pricesBox = "";
		for(var i in basePrices){
			pricesBox += "<button class='unitPrice' id='unitPrice"+i+"'><i class='fa fa-usd' aria-hidden='true'></i>"+basePrices[i]+"</button>";
		}
		pricesBox +="<button class='unitPrice' id='otherBtn'>Other</button>";
		pricesBox +="<input type='text' class='other-amt' style='display: none;' id='otherAmt'>";
		$("#betAmtPrice").html(pricesBox);
		$(".bettypes").first().addClass("select-bet");
		$("#betTypeSel").html(selectedBetName);
		$("#numPicked").html('');
		$("#betAmt").val(respData.games[gameIndex].bets[0].unitPrice);
		$("#buy").attr("disabled",true);
		
		if("MiniRoulette" == gameCode){
			$(".bet-type").css("display","none");
			$("#betAmtHead").css("display","none");
			$("#betAmtPrice").css("display","none");
			$("#purchaseDetails").css("display","none");
			$("#roulettePurchaseDetails").css("display","block");
			$("#fullRoulettePurchaseDetails").css("display","none");
		}else if ("FullRoulette" == gameCode){
			$(".bet-type").css("display","none");
			$("#betAmtHead").css("display","none");
			$("#betAmtPrice").css("display","none");
			$("#purchaseDetails").css("display","none");
			$("#roulettePurchaseDetails").css("display","none");
			$("#fullRoulettePurchaseDetails").css("display","block");
		}else{
			$(".bet-type").css("display","block");
			$("#betAmtHead").css("display","block");
			$("#betAmtPrice").css("display","block");
			$("#purchaseDetails").css("display","block");
			$("#roulettePurchaseDetails").css("display","none");
			$("#fullRoulettePurchaseDetails").css("display","none");
		}
		
		if('TwelveByTwentyFour' == gameCode){
			$("#twelveByTwentyFour").css("display","block");
		}else if('PickThree' == gameCode){
			$("#pickthree").css("display","block");
		}else if('PickFour' == gameCode){
			$("#pickfour").css("display","block");
		}else if('OneToTwelve' == gameCode){
			$("#onebytwelve").css("display","block");
		}else if('KenoSix' == gameCode){
			$("#eightytwentyten").css("display","block");
		}else if("MiniRoulette" == gameCode){
			$("#miniRoulette").css("display","block");
			betCoinAmount = $(".betCoins").eq(1).attr("betCoinAmt");
			$(".betCoins").eq(1).addClass("selected");
		}else if('KenoTwo' == gameCode){
			$("#kenoTwo").css("display","block");
		}else if('FullRoulette' == gameCode){
			$("#fullRoulette").css("display","block");
		}
		else if('ZimLottoBonus' == gameCode){
			$("#zimLottoBonus").css("display","block");
			$("#directSix").css("display","block");
		}
		else if('TenByTwenty' == gameCode){
			$("#tenByTwenty").css("display","block");
		}
		getBetType(gameCode);
		$(".unitPrice").first().addClass("amt-select");
		if("MiniRoulette" == gameCode || "FullRoulette" == gameCode){
			betAmtSelected = 0;
		}else{
			betAmtSelected = basePrices[0];
		}
		isPriceSel = true;
	}
	else {
		alert(respData.responseMsg);
		return false;
	}
}


function getBetType(gameName){
	$(".bettypes").each(function(){
		if($(this).hasClass("select-bet")){
			selectedBetName = $(this).attr("betName");
			$("#betTypeSel").html($(this).text());
			selectedBetCode = $(this).val();
			currUnitPrice = $(this).attr("allBetInfo").split("~")[0];
			currBetAmtMul = $(this).attr("allBetInfo").split("~")[1];
			return false;
		}
	});
	if("TwelveByTwentyFour" == gameName){
    	if('Direct12' == selectedBetName || 'Perm12' == selectedBetName){
    		$("#qpCheck").prop("disabled",false);
	    }
    	else {
    		$("#qpCheck").prop("disabled",true);
    	}
    	changeBetTypeTTF();
    }else if("PickThree" == gameName){
	    if('Combination' == selectedBetName){
	    	  $("#qpCheck").prop("disabled",false);
	  	}else {
	  		$("#qpCheck").prop("disabled",true);
	    }
	    changeBetTypePickThree();
    }else if("PickFour" == gameName){
    	if('Combination' == selectedBetName){
    		$("#qpCheck").prop("disabled",false);
    	}else {
    		$("#qpCheck").prop("disabled",true);
	    }
    	changeBetTypePickFour();
    }else if("OneToTwelve" == gameName){
    	changeBetTypeOneByTwelve();
    }else if("KenoSix" == gameName){
    	changeBetTypeEightyTwentyTen();
    }else if("MiniRoulette" == gameName){
    	
    }else if("KenoTwo" == gameName){
    	changeBetTypeKenoTwo();
    }else if("ZimLottoBonus" == gameName){
    	changeBetTypeZimLottoBonus();
    	resetZimLottoBonus();
    }else if("TenByTwenty" == gameName){
    	changeBetTypeTenByTwenty();
    }
	if(!isDrawManual){
		$("#numOfDrawsSelected").val("");
	}
	return selectedBetName;
}
/**
 * Get bet type for each game.
 */
function getBetInfo(){
	var betType = gameLimit[selectedBetName];
	if(betType==""){
		$("#error-popup").css('display','block');
		$("error").text("Please select bet type!!");	
		$('#error-popup').delay(2000).fadeOut('slow');
		$('li.pmsnumber').removeClass('selected');
		return false;
	}
	else{
		$("error").text("");
	}
	return betType;
}

function quickPickSelected(gameName){
	$("#otherBtn").css("display","block");
	$("#otherAmt").val("");
	$("#otherAmt").css("display","none");
	if("TwelveByTwentyFour" == gameName){
		if("Perm12" == selectedBetName){
			if($("#qpCheck").is(":checked")){
				resetTwelveByTwentyFourGame();
				$("#twelveByTwentyQpTextboxName").css("display","block");
				$("#twelveByTwentyQp").css("display","block");	
				$("#twelveByTwentyQpBox").css('display','inline');
				$("#twelveByTwentyQpTextboxName").css('display','block');
			}
			else{
				resetTwelveByTwentyFourGame();
			}
			
		}
		else{
			twelveByTwentyFourQP();	
		}
	}else if("PickThree" == gameName){
		pickThreeQP();
	}else if("PickFour" == gameName){
		pickFourQP();
	}else if("OneToTwelve" == gameName){
		if(!($("#qpCheck").is(":checked"))){
			$("#numToBePicked").prop("disabled",true);
			resetOneByTwelveGame();
			if(!isDrawManual){
				noOfDrawsSelected = alreadySelDraw.length;
				noOfSelectedDraws = alreadySelDraw;
			}else{
				noOfDrawsSelected = drawsSel;
				$("#numOfDrawsSelected").val(noOfDrawsSelected);
			}
		}else{
			$("#numToBePicked").prop("disabled",false);
			resetForQP();
		}
		$(".onetotwelveNum").removeClass("selected-number");
		$(".ottManualEnter").val("");
		numbersChosen = [];
		$("#numPicked").html("");
		$("#error").html("");
		$("#error-popup").css('display','none');
	}else if("KenoSix" == gameName){
		eightyTwentyTenQP();
	}else if("MiniRoulette" == gameName){
	}else if("KenoTwo" == gameName){
		$("#kenoTwoQpTextboxName").css("display","none");
		$(".kenoTwoNumber").removeClass('selectnumber');
		$(".kenoTwoNumberEnter").val("");
		if(!$("#qpCheck").is(":checked")){
			$("#numToBePickedFN").prop("disabled",true);
			resetKenoTwo();
			var numOfTextBoxes = kenoTwoBetList[selectedBetName].max;
			for(var i=1; i<=numOfTextBoxes; i++){
				$("#ktnum"+i).css("display","block");
			}
			if("Perm2" != selectedBetName && "Perm3" != selectedBetName){
				$("#qpCheck").attr("checked");
				kenoTwoQP();
			}
			if(!isDrawManual){
				noOfDrawsSelected = alreadySelDraw.length;
				noOfSelectedDraws = alreadySelDraw;
			}else{
				noOfDrawsSelected = drawsSel;
				$("#numOfDrawsSelected").val(noOfDrawsSelected);
			}
		}else{
			$(".kenoTwoNumber").removeClass("selected-number");
			$(".kenoTwoNumberEnter").val("");
			numbersChosen = [];
			$("#numPicked").html("");
			$("#error").html("");
			$("#error-popup").css('display','none');
			if("Perm2" != selectedBetName && "Perm3" != selectedBetName){
				$("#numToBePickedFN").prop("disabled",true);
				kenoTwoQP();
			}
			else{
				$("#numToBePickedFN").prop("disabled",false);	
				resetKenoTwoForQP();
				$("#kenoTwoQpTextboxName").css("display","block");
			}
		}
	}else if("FullRoulette" == gameName){
	}
	else if("TenByTwenty" == gameName){
		resetTenByTwentyForQP();
		tenByTwentyQP();
	}
	else if("ZimLottoBonus" == gameName){
		if(!$("#qpCheck").is(":checked")){
			$("#zimQpTextbox").val("");
			$("#zimQpTextboxName").empty();
			$("#zimQpTextboxName").css("display","none");
			$("#zimQpTextbox").attr("disabled",true);
			changeBetTypeZimLottoBonus();
		}else{
			$("#zimQpTextbox").attr("disabled",false);
			changeBetTypeZimLottoBonus();
			$("#zimQpTextbox").focus();
			if("Perm6"==selectedBetName){
				$("#zimQpTextboxName").html("Enter No. To Be Picked");
				$("#zimQpTextboxName").css("display","block");
			}
			else{
				$("#zimQpTextboxName").html("Enter No. Of Lines");
				$("#zimQpTextboxName").css("display","block");
			}
		}

	}

}
/**
 * Updates no. of lines for each bet type.
 */
function updateCurrNoLines(num,currentGameType) {
	currentNoOfLines = 1;
	if (currentGameType.match(/perm/i) != null) {
		var temp = pmsParseInt(currentGameType.split("m")[1]);
		currentNoOfLines = getCombinations(num, temp);
	}
	$("#noOfLines").html(currentNoOfLines);
}

/**
 * Update bet amount multiple.
 */
function updateBetMul(sign,gameName){
	var betInfo;
	$(".radio-btn").each(function(){
		if($(this).is(":checked")){
			betInfo = $(this).parent().text();
			return false;
		}
	});
	$("#error").html("");
	$("#error-popup").css('display','none');
	if(!betInfo)
		return false;
	var unitPrice = parseFloat(currUnitPrice);
	var maxBetAmtMul = parseFloat(currBetAmtMul);
	var currentBetMultiple = parseFloat($("#unitPrice").val());
	if(sign == "-"){
		if(currentBetMultiple == unitPrice){
			currentBetMultiple = maxBetAmtMul;
		}else{
			if(currentBetMultiple==0){
				currentBetMultiple = unitPrice;
			}else{
				currentBetMultiple = currentBetMultiple-unitPrice;
			}
		}
	}else{
		if(currentBetMultiple == maxBetAmtMul){
			currentBetMultiple = unitPrice;
		}else{
			currentBetMultiple = currentBetMultiple + unitPrice;
		}
	}
	$("#unitPrice").val(currentBetMultiple);
	$("#betAmt").html(pmsRoundOff(currentBetMultiple,unitPrice.length));
	updateTicketPrice(unitPrice.length);
}

/**
 * Update total numbers selected
 */
function updateNumCount(sign,gameName){
	var betInfo;
	$(".radio-btn").each(function(){
		if($(this).is(":checked")){
			betInfo = $(this).parent().text();
			return false;
		}
	});
	$("#error").text("");
	$("#error-popup").css('display','none');
	if(!betInfo)
		return false;
	if(gameName == 'TwelveByTwentyFour'){
		var counterObj	= $("#qpNumCount");
		var min=gameLimit[betInfo].min;
		var max=gameLimit[betInfo].max;
	}else if(gameName == 'KenoSix'){
		var counterObj	= $("#qpNumCount-game80");
		var min=eightTwentyTenBetList[betInfo].min;
		var max=eightTwentyTenBetList[betInfo].max;
	}else if(gameName == 'OneToTwelve'){
		var counterObj	= $("#qpNumCount-game1by12");
		var min=1;
		var max=100;
	}
	var count = parseFloat(counterObj.html());
	if(sign=="-"){
		if(count == min){
			count = max;
		}else{
			if(count == 0){
				count = min;
			}else{
				count--;
			}
		}
	}else if(sign == "+"){
		if(count == max){
			count = min;
		}else{
			count++;
		}
	}
	$("#noOfNumber").html(count);
	counterObj.html(count);
	updateCurrNoLines(count,selectedBetName);
	updateTicketPrice(betInfo.length);
}

/**
 * Gives no. of possible combinations.
 */
function getCombinations(n, r) {
	var temp = n;
	for (var i= n - 1; i > (n - r); i--) {
		temp = temp * i;
	}
	for (var i= r; i > 1; i--) {
		temp = temp / i;
	}
	return temp;
}

/**
 * Updates ticket price.
 */
function updateTicketPrice(places){
	var numberOfDraws = 0;
	var gameName = $(".select-game").children().eq(1).text();
	if("OneToTwelve" == gameName){
		var noOfLines = parseFloat($("#noOfNumber").text());
	}else{
		var noOfLines = parseFloat($("#noOfLines").text());
	}
	var betAmount = parseFloat(betAmtSelected);
	if(!isDrawManual){
		var drawsPicked = [];
		$.each($("input[name='drawName']:checked"), function(){            
			drawsPicked.push($(this).val());
		});
		numberOfDraws = drawsPicked.length;
	} else {
		numberOfDraws = parseInt($("#numOfDrawsSelected").val());
	}
	var tktPrice = pmsRoundOff(noOfLines * betAmount * numberOfDraws,places);
	$("#tktPrice").text(tktPrice);
}

/**
 * Gives formatted number.
 */
function getFormattedNo(numnumSelectDivber){
    return (pmsParseInt(number) < 10 ? "0" : "") + pmsParseInt(number);
}

/**
 * Get original amount.
 */
function getOriginalAmount(amount){
    return Number(amount.replace(",",""));
}

/**
 * Get required number.
 */
function getNumberNumeric(amount){
    if(amount.indexOf(",") != -1)
        return Number(amount.replace(",",""));
    else
        return Number(amount);
}

/**
 * Get formatted amount.
 */
function getFormattedAmount(amount){
    amount = amount+"";
    amount = amount.replace(",","");
    var tmp = amount.split(".");
    if(tmp.length==2)
    {
        if(tmp[1].length>2)
        {
            if(tmp[1].substr(0,2)!="00")
                return number_format(amount, 2);
        }
        else if(tmp[1].length==2)
        {
            if(tmp[1]!="00")
                return number_format(amount, 2);
        }
        else if(tmp[1].length==1)
        {
            if(tmp[1]!="0")
                return number_format(amount, 2);
        }
        else
        {
            return number_format(amount, 0);
        }
    }
    return number_format(amount, 0);
}

/**
 * Format selected numbers.
 */
function number_format(number, decimals, dec_point, thousands_sep) {
    number = (number + '').replace(/[^0-9+\-Ee.]/g, '');
    var n = !isFinite(+number) ? 0 : +number,
        prec = !isFinite(+decimals) ? 0 : Math.abs(decimals),
        sep = (typeof thousands_sep == 'undefined') ? ',' : thousands_sep,
        dec = (typeof dec_point == 'undefined') ? '.' : dec_point,
        s = '',
        toFixedFix = function(n, prec) {
            var k = Math.pow(10, prec);
            return '' + (Math.round(n * k) / k).toFixed(prec);
        };
    s = (prec ? toFixedFix(n, prec) : '' + Math.round(n)).split('.');
    if (s[0].length > 3) {
        s[0] = s[0].replace(/\B(?=(?:\d{3})+(?!\d))/g, sep);
    }
    if ((s[1] || '').length < prec) {
        s[1] = s[1] || '';
        s[1] += new Array(prec - s[1].length + 1).join('0');
    }
    return s.join(dec);
}

/**
 * For rounding off values
 */
function pmsRoundOff(val,place){
    return getFormattedAmount(val);
}

/**
 * For parsing int values.
 */
function pmsParseInt(number){
	return parseInt(number, 10);
}

/**
 * For rounding off ticket price.
 */
function amountRoundOff(tktPrice){
    tmpTktPrice=0;
    tktPrice=tktPrice+"";
    tktPrice = tktPrice.replace(",","");
    if(tktPrice<=0)
    {
        tmpTktPrice = "0";
    }else if(tktPrice>10){
        var tmpArr=tktPrice.split(".");
        tmpTktPrice = tmpArr[0]+".00";
    }else{
        if(tktPrice<"0.50"){
            tmpTktPrice = "0.50";
        }else{
            var tmpArr = tktPrice.split(".");
            var roundedVal = 0;
            var mainValue = pmsParseInt(tmpArr[0]);
            var secondValue ="00";
            if(tmpArr[1] != undefined){
                secondValue =  tmpArr[1] + (tmpArr[1]< "10" ? "0" : "");
            }
            if(secondValue >= "00" && secondValue < "25"){
                roundedVal = "00";
            }
            if(secondValue >= "25" && secondValue < "75"){
                roundedVal = "50";
            }
            if(secondValue >= "75"){
                mainValue++;
                roundedVal = "00";
            }
            tmpTktPrice = mainValue+"."+roundedVal;
        }
    }
    return getFormattedAmount(tmpTktPrice);
}

/**
 * Checks whether any draw is selected or not.
 */
function checkIsDrawSelected(){
	//console.log("No. of Draws Selected: "+noOfDrawsSelected+ " Bet Amount is: "+betAmtSelected+ " and is game play valid: "+isValid);
	if("MiniRoulette" != gameSelected){
		if(noOfDrawsSelected != 0 && betAmtSelected != 0 && isValid && isPriceSel){
			$("#buy").attr("disabled",false);
		}else{
			$("#buy").attr("disabled",true);
		}
	}else{
		if(noOfDrawsSelected != 0 && betAmtSelected != 0 && isValid){
			$("#buy").attr("disabled",false);
		}else{
			$("#buy").attr("disabled",true);
		}
	}
 }

 /**
  * Method calling printing application by passing data.
  */
function setAppData(data) {
	isValid = false;
	resetAllGames();
	var connSuccess = false;
	var connection = new WebSocket('ws://localhost:8082');
	connection.onopen = function () {
   		connection.send(data);
   		if(connection.readyState == 1){
   			connSuccess = true;
   		}
	};
	connection.onerror = function(){
		if("Buy" == curTrx){
			var data = {"ticketNumber":tktNum, "autoCancel":true};
			data = JSON.stringify(data);
			var _resp = winAjaxReq("cancelTicketAuto.action?json=" + data);
			parent.frames[0].updateBalance(path + "/com/skilrock/lms/web/loginMgmt/updateAvlCreditAmt.action");
			alert("Cannot communicate to printing application.\nPlease click on Test Print option under settings\nand verify printing application is working or not." +
				"\n\nLast ticket has been cancelled!!");
			$("#buy").prop('disabled',true);
		} else if("Reprint" == curTrx){
			alert("Cannot communicate to printing application.\nPlease click on Test Print option under settings\nand verify printing application is working or not.");
		} else if("Cancel" == curTrx){
			parent.frames[0].updateBalance(path + "/com/skilrock/lms/web/loginMgmt/updateAvlCreditAmt.action");
			alert("Cannot communicate to printing application.\nPlease click on Test Print option under settings\nand verify printing application is working or not."+
				"\n\nLast ticket has been cancelled!");
		} else if("PWT" == curTrx){
			parent.frames[0].updateBalance(path + "/com/skilrock/lms/web/loginMgmt/updateAvlCreditAmt.action");
			alert("Cannot communicate to printing application.\nPlease click on Test Print option under settings\nand verify printing application is working or not." +
					"\n\nTicket has been claimed!!");
			preparePwtTicket(JSON.parse(printData));
		} else if("TestPrint" == curTrx){
			alert("Cannot communicate to printing application.\nPlease check your NodeJS server is running and try again.\nTo start NodeJs server either restart your system or\n follow the steps below:\n" +
					"1.) Open My Computer. \n2.) Goto C:/Program Files (x86)/skilrock/dgpcpos (For 64-bit system) and\n" +
					"     C:/Program Files (x86)/skilrock/dgpcpos (For 32-bit system).\n3.) Double click on runNodeInvisible to start NodeJs server.");
		}
	};
	connection.onmessage = function (evt) {
		//console.log(evt.data);
		var printStatus = "";
    	var dataReceived = $.parseJSON(evt.data);
    	if("APP_NOT_FOUND" == dataReceived){
    		printStatus = dataReceived;
    	}else{
    		printStatus = dataReceived.utf8Data;
    	}
        connection.close();
	    if(connSuccess){
	    	checkAndShowTicket(printStatus);
		}
    };
}

/**
 * Resets all the games
 */
function resetAllGames(){
	resetTwelveByTwentyFourGame();
	resetPickThreeGame();
	resetPickFourGame();
	resetOneByTwelveGame();
	resetEightyTwentyTenGame();
	resetMiniRoulette();
	resetKenoTwo();
	resetFullRoulette();
	changeBetTypeZimLottoBonus();
	$(".sideMenuList").each(function(){
		 if($(this).hasClass("select-game")){
			var menuId = $(this).attr('id');
			var gameId = menuId.split('~')[1];
			resetTimer = false;
			updateMidPanel(menuId, gameId,resetTimer);
			$(this).focus();
		 }
	 });
}

/**
 * Method called from applet after attempting printing.
 */
function checkAndShowTicket(printStatus){
	if("SUCCESS" == printStatus){
		$("#buy").prop('disabled',true);
		parent.frames[0].updateBalance(path + "/com/skilrock/lms/web/loginMgmt/updateAvlCreditAmt.action");
		reprintCountChild = 0;
		if("Buy" == curTrx){
			prepareSaleTicket(JSON.parse(printData));
		}else if("Reprint" == curTrx){
			prepareReprintTicket(JSON.parse(printData),'Reprint');
		}else if("Cancel" == curTrx){
			prepareCancelTicket(JSON.parse(printData));
		}else if("PWT" == curTrx){
			preparePwtTicket(JSON.parse(printData));
		}else if("TestPrint" == curTrx){
			$("#success").html("Printing application is working fine!");
			$("#success-popup").css('display','block');		
			$('#success-popup').delay(1500).fadeOut('slow');
		}
		return true;
	} else if("FAIL" == printStatus){
		if("Buy" == curTrx){
			if(reprintCountChild < 2){
				var status = confirm("Printer not connected!! \nCheck your printer and press OK to reprint or press Cancel to cancel the ticket!!");
				if(!status){
					var data = {"ticketNumber":tktNum, "autoCancel":true};
					data = JSON.stringify(data);
					var _resp = winAjaxReq("cancelTicketAuto.action?json="+data);
					parent.frames[0].updateBalance(path + "/com/skilrock/lms/web/loginMgmt/updateAvlCreditAmt.action");
					$("#buy").prop('disabled',true);
					reprintCountChild = 0;
					return false;
				}else{
					setAppData(printData);
					reprintCountChild++;
				}
			} else{
				var data = {"ticketNumber":tktNum, "autoCancel":true};
				data = JSON.stringify(data);
				var _resp = winAjaxReq("cancelTicketAuto.action?json="+data);
				parent.frames[0].updateBalance(path + "/com/skilrock/lms/web/loginMgmt/updateAvlCreditAmt.action");
				$("#buy").prop('disabled',true);
				reprintCountChild = 0;
				alert("Last ticket has been cancelled!!");
				return false;
			}
		}else if ("Reprint" == curTrx) {
			if (reprintCount < 2) {
				alert("Printer not connected. \nCheck your printer and try again!!");
				return true;
			} else {
				return false;
			}
		}else if("Cancel" == curTrx){
			alert("Printer not connected.\nLast ticket has been cancelled!!");
			parent.frames[0].updateBalance(path + "/com/skilrock/lms/web/loginMgmt/updateAvlCreditAmt.action");
		}else if("TestPrint" == curTrx){
			alert("Printer not connected!!");
		}
	} else if("APP_NOT_FOUND" == printStatus){
		if("Buy" == curTrx){
			var data = {"ticketNumber":tktNum, "autoCancel":true};
			data = JSON.stringify(data);
			var _resp = winAjaxReq("cancelTicketAuto.action?json="+data);
			parent.frames[0].updateBalance(path + "/com/skilrock/lms/web/loginMgmt/updateAvlCreditAmt.action");
			alert("Cannot communicate to printing application.\nPlease click on Test Print option under settings\nand verify printing application is working or not." +
				"\n\nLast ticket has been cancelled!!");
			$("#buy").prop('disabled',true);
		} else if("Reprint" == curTrx){
			alert("Cannot communicate to printing application.\nPlease click on Test Print option under settings\nand verify printing application is working or not.");
		} else if("Cancel" == curTrx){
			parent.frames[0].updateBalance(path + "/com/skilrock/lms/web/loginMgmt/updateAvlCreditAmt.action");
			alert("Cannot communicate to printing application.\nPlease click on Test Print option under settings\nand verify printing application is working or not."+
				"\n\nLast ticket has been cancelled!");
		} else if("PWT" == curTrx){
			parent.frames[0].updateBalance(path + "/com/skilrock/lms/web/loginMgmt/updateAvlCreditAmt.action");
			alert("Cannot communicate to printing application.\nPlease click on Test Print option under settings\nand verify printing application is working or not." +
				"\n\nTicket has been claimed!!");
			preparePwtTicket(JSON.parse(printData));
		}else if("TestPrint" == curTrx){
			alert("Printing Application not running!!");
		}
	} else if("INVALIDINPUT" == printStatus){
		if("Buy" == curTrx){
			var data = {"ticketNumber":tktNum, "autoCancel":true};
			data = JSON.stringify(data);
			var _resp = winAjaxReq("cancelTicketAuto.action?json="+data);
			parent.frames[0].updateBalance(path + "/com/skilrock/lms/web/loginMgmt/updateAvlCreditAmt.action");
			alert("Ticket cannot be printed due to \nInternal System Error!!\nLast ticket has been cancelled!!");
			$("#buy").prop('disabled',true);
		} else if("Reprint" == curTrx){
			alert("Ticket cannot be printed due to \nInternal System Error!!");
		} else if("Cancel" == curTrx){
			parent.frames[0].updateBalance(path + "/com/skilrock/lms/web/loginMgmt/updateAvlCreditAmt.action");
			alert("Ticket cannot be printed due to \nInternal System Error!!");
		} else if("PWT" == curTrx){
			parent.frames[0].updateBalance(path + "/com/skilrock/lms/web/loginMgmt/updateAvlCreditAmt.action");
			alert("Ticket cannot be printed due to \nInternal System Error!!\nTicket has been claimed!!");
			preparePwtTicket(JSON.parse(printData));
		}
	}
}

function winAjaxReq(url) {
	var _req = null;
	var _result = false;
	url = _randCache(url);
	_req = XMLRequestDojo();
	_req.onreadystatechange = function () {
		if (_req.readyState == 4) {
			if (_req.status == 200) {
				_result = true;
			}
		}
	};
	_req.open("GET", url, false);
	_req.setRequestHeader("If-Modified-Since", new Date().getTime());
	_req.send(null);
	return {result:_result, data:_req.responseText};
}

function _randCache(url) {
	var _c = "?";
	if (url.indexOf("?") != -1) {
		_c = "&";
	}
	url = url + _c + new Date().getTime() + Math.random() * 1000000;
	return url;
}

function XMLRequestDojo() {
	if (window.XMLHttpRequest) {
		return new XMLHttpRequest();
	}
	return new ActiveXObject("Microsoft.XMLHTTP");
}

function setDefaultBetType(){
	$(".radio-btn").parent().removeClass('select-radio');
	$(".radio-btn").first().focus();
	$(".radio-btn").first().prop('checked',true);
	$(".radio-btn").first().parent().addClass('select-radio');
	var selectedGame ='';
	 $(".sideMenuList").each(function(){
		 if($(this).hasClass("select-game")){
			 selectedGame = $(this).children().eq(1).text();
			 return false;
		 }
	 });
	 getBetType(selectedGame);
}

function prepareSaleTicket(obj){
	 $("#tktGen").html("");
	 var dispTkt = "";
	 var i=0;
	 var topAdvMsg=null;
	 dispTkt+="<div class='ticket-format'>";
		if(obj.advMsg!=null && obj.advMsg.TOP!=null){	
			var topAdvMsg=obj.advMsg.TOP[0];
			
			while(obj.advMsg.TOP[i]!=null){
				dispTkt+="<div class='dg-dg'>"+obj.advMsg.TOP[i++]+"</div>";
			}
		}
		
		//Setting Org LOGOpath
		dispTkt+="<div class='tkt-logo border-top'>";
		if(obj.parentOrgName.includes("Africa Bet") > 0){
			dispTkt+="<div class='logo-rel'><img src='"+path+"/com/skilrock/lms/web/drawGames/common/pcposImg/africaBet_black.png' width='140' height='67' /></div>";
		}else{
			dispTkt+="<div class='logo-rel'><img src='"+path+"/com/skilrock/lms/web/drawGames/common/pcposImg/africa_loto.png' width='140' height='67' /></div>";			
		}
		//Setting game name
		if(obj.gameName!=null){
			dispTkt+="<span class='tkt-gm_nm'>"+obj.gameName+"</span>";
			
		}
		dispTkt+="</div>";
		
		//Setting ticket number
		dispTkt+="<div class='tkt-dt-m border-line'>";
		if(obj.ticketNumber!=null){
			dispTkt+="<div class='tkt-rw'>" + "<span class='no-o-tkt'>Ticket No:</span>" + "<span class='no-of-tkt'>"+obj.ticketNumber+"</span>" +"</div>";
			if(obj.cardNo!=''){
				dispTkt+="<div class='tkt-rw'>" + "<span class='no-o-tkt'>Card No:</span>" + "<span class='no-of-tkt'>"+obj.cardNo+"</span>" +"</div>";
			}
			dispTkt+="<div class='tkt-rw'>" + "<span class='no-o-tkt'>Purchased On:</span>" + "<span class='no-of-tkt'>"+obj.purchaseDate+" "+obj.purchaseTime+"</span>" +"</div>";
		}
		dispTkt+="</div>";
		
/*		//Setting purchase time
		dispTkt+="<div class='bg-gary-tkt'>";
		if(obj.purchaseTime!=null){
			dispTkt+="<span class='dt-gary-tkt'>"+obj.purchaseDate+" "+obj.purchaseTime+"</span>";
		}
		dispTkt+="</div>";*/
		dispTkt+="<div class='tkt-dt-m border-top'>";
		//Setting purchase date
		dispTkt+="<div class='tkt-rw'>";
		if(obj.purchaseDate!=null){
			dispTkt+="<span class='no-o-dd'>Draw Date</span>";
			dispTkt+="<span class='no-o-dt'>Draw Time</span>";
			
		}
		dispTkt+="</div>";
		/*dispTkt+="<div class='tkt-rw'>";
		//Setting purchase time
		if(obj.purchaseTime!=null){
			dispTkt+="<span class='no-of-ddd'>"+obj.purchaseDate+"</span>";
			dispTkt+="<span class='no-of-dtt'>"+obj.purchaseTime+"</span>";
		}
		dispTkt+="</div>";*/
		
		
		//Setting draw date and time
		if("ZimLottoBonus" == obj.gameDevName || "KenoTwo" == obj.gameDevName ){
			$(obj.drawData).each(function(){
				dispTkt+="<div class='tkt-rw'>";
					if(this.drawDate!=null){
						dispTkt+="<span class='no-of-ddd'>"+dateFormat(new Date(getCompataibleDate4(this.drawDate)),dateFormat.masks.sikkimDate)+"</span>";
					}
					if(this.drawTime != null){
						dispTkt+="<span class='no-of-dtt'>"+getCompataibaleTime(this.drawTime)+"</span>";
					}
					/*if(this.drawName!=null){
						dispTkt+="<div class='no-of-tkt'>"+this.drawName+"</div>";
					}*/
					dispTkt+="</div>";
				});
		}
		else{
			$(obj.drawData).each(function(){
				dispTkt+="<div class='tkt-rw'>";
					if(this.drawDate!=null){
						dispTkt+="<span class='no-of-ddd'>"+dateFormat(new Date(getCompataibleDate3(this.drawDate)),dateFormat.masks.sikkimDate)+"</span>";
					}
					if(this.drawTime != null){
						dispTkt+="<span class='no-of-dtt'>"+getCompataibaleTime(this.drawTime)+"</span>";
					}
					/*if(this.drawName!=null){
						dispTkt+="<div class='no-of-tkt'>"+this.drawName+"</div>";
					}*/
					dispTkt+="</div>";
				});
		}
		dispTkt+="<div class='tkt-rw border-top'>";
		
		// Setting bet type name
		if("OneToTwelve" == obj.gameDevName || "MiniRoulette" == obj.gameDevName || "FullRoulette" == obj.gameDevName){
			$(obj.betTypeData).each(function(){
				dispTkt+="<div class='list-draw-result-d'>";
				dispTkt+="<div class='span-rt'>";
				if(this.betName!=null){
					dispTkt+="<span class='draw-nm-s'>"+this.betName+"</span>";
				}
				//Setting up quick pick or manual
				if(this.isQp){		
					dispTkt+="<span class='draw-nm-m'>QP</span>";
				}else{
					dispTkt+="<span class='draw-nm-m'>Manual</span>";
				}
				dispTkt+="</div>";
				
				
				//Setting numbers picked fortune
				
				if("OneToTwelve" == obj.gameDevName){
					if(this.pickedNumbers!=null){
						dispTkt+="<ul class='furtune-rest'><li><span>"+this.pickedNumbers+"</span>-<span class='zod'>"+oneByTwelveZodiacSigns[parseInt(this.pickedNumbers)-1]+"</span></li></ul>";
					}
				} else{
					if(this.pickedNumbers!=null){
						dispTkt+="<div class='ticket-text-pn'>"+this.pickedNumbers+"</div>";
						
					}
				}
				dispTkt+="</div>";
				
				//Setting number of lines
				if("MiniRoulette" == obj.gameDevName || "FullRoulette" == obj.gameDevName){
					dispTkt+="<div class='tkt-dt-m border-top'>";
					dispTkt+="<div class='tkt-rw'>";
					if(this.noOfLines!=null){
						dispTkt+="<span class='no-o-tkt'>No. of Line(s)</span>";
						dispTkt+="<span class='no-of-tkt'>"+this.noOfLines+"</span>";
					}
					dispTkt+="</div>";
					dispTkt+="</div>";
				}
				
				//Setting unit price
				dispTkt+="<div class='tkt-rw border-top-t'>";
				if("MiniRoulette" == obj.gameDevName || "FullRoulette" == obj.gameDevName){
					dispTkt+="<span class='no-o-tkt'>Bet Amount("+currencySymbol+")</span>";
					dispTkt+="<span class='no-of-tkt'>"+this.panelPrice+"</span>";
				}else{
					if(this.unitPrice!=null){
						dispTkt+="<span class='no-o-tkt'>Bet Amount("+currencySymbol+")</span>";
						dispTkt+="<span class='no-of-tkt'>"+this.unitPrice * this.betAmtMul+"</span>";
					}
				}
				dispTkt+="</div>";
				
			});
			
		} else{
			if(obj.betName!=null){
				dispTkt+="<span class='draw-nm-s'>"+obj.betName+"</span>";
			}
			// Setting up quick pick or manual
			if(obj.isQp){		
				dispTkt+="<span class='draw-nm-m'>QP</span>";
			}else{
				dispTkt+="<span class='draw-nm-m'>Manual</span>";
			}
			
			// Setting numbers picked
			var i=0;
			if("ZimLottoBonus" == obj.gameDevName && obj.pickedNumbers!=null){
				$(obj.pickedNumbers).each(function(){
					dispTkt+="<div class='ticket-text-pn'>"+this+"</div><br>";	
				});
			}
			else{
				if(obj.pickedNumbers!=null){
					dispTkt+="<div class='ticket-text-pn'>"+obj.pickedNumbers+"</div>";	
				}
			}
			dispTkt+="</div>";
			
			
			dispTkt+="<div class='tkt-dt-m border-top'>";
			// Setting number of lines
			dispTkt+="<div class='tkt-rw'>";
			if(obj.noOfLines!=null){
				dispTkt+="<span class='no-o-tkt'>No. of Line(s)</span>";
				dispTkt+="<span class='no-of-tkt'>"+obj.noOfLines+"</span>";
			}
			dispTkt+="</div>";
			// Setting unit price
			if("ZimLottoBonus" != obj.gameDevName){
				dispTkt+="<div class='tkt-rw'>";
				if(obj.unitPrice!=null){
						dispTkt+="<span class='no-o-tkt'>Bet Amount("+currencySymbol+")</span>";
					dispTkt+="<span class='no-of-tkt'>"+obj.unitPrice * obj.betAmtMul+"</span>";
				}
				dispTkt+="</div>";
			}
		}
		// Setting number of draws
		dispTkt+="<div class='tkt-rw'>";
		if(obj.drawData!=null){
			dispTkt+="<span class='no-o-tkt'>No. of Draws:</span>";
			dispTkt+="<span class='no-of-tkt'>"+obj.drawData.length+"</span>";
		}
		dispTkt+="</div>";
		// Setting total amount
		dispTkt+="<div class='tkt-rw tlam'>";
		if(obj.purchaseAmt!=null){
			dispTkt+="<span class='no-o-tkt '>Total Amount("+currencySymbol+")</span>";
			dispTkt+="<span class='no-of-tkt'>"+obj.purchaseAmt+"</span>";
		}
		dispTkt+="</div>";
		dispTkt+="</div>";
		
		// Setting barcode
		dispTkt+="<div class='code-img'>";
		dispTkt+="<img src='"+path+"/LMSImages/images/barcode.png' class='code_img' />";	
		dispTkt+="<span class='code-no'>"+obj.ticketNumber+"</span>";
		dispTkt+="</div>";
		if("SIKKIM" == countryDeployed){
			dispTkt+="<div class='lottery-sign'><img src='"+path+"/com/skilrock/lms/web/drawGames/common/pcposImg/signature.png' class='sign_img' /></div>";
		}
		
		//Setting retailer name
		dispTkt+="<div class='bottom-username border-top'>";
		if(obj.retailerName!=null){
			dispTkt+="<span>"+obj.retailerName+"</span>";
		}
		dispTkt+="</div>";
		//Setting bottom adv message if any
		dispTkt+="<div class='bottom-username'>";
		var i=0;
		if(obj.advMsg!=null && obj.advMsg.BOTTOM!=null)
		{
			while(obj.advMsg.BOTTOM[i]!=null){
				dispTkt+="<div class='dg-dg'>"+obj.advMsg.BOTTOM[i++]+"</div>";
			}
		dispTkt+="</div>";
		}
		dispTkt+="</div>";
		dispTkt+="</div>";
		$("#tktGen").append(dispTkt);
	 
}


function preparePwtTicket(obj){
	 $("#tktGen").html("");
	 var dispTkt = "";
	   dispTkt+="<div class='hr-line-s'></div>";
	   var topAdvMsg=null;
   	   var i=0;
   	   dispTkt+="<div class='ticket-format'>";
		if(obj.advMsg!=null && obj.advMsg.TOP!=null){
			var topAdvMsg=obj.advMsg.TOP[0];
			
			while(obj.advMsg.TOP[i]!=null){
				dispTkt+="<div class='ticket-text ticket-magrin'><p class='ticket-margin' style='margin:0;font-size: 9px;'>"+obj.advMsg.TOP[i++]+"</p></div>";
			}
		}
		
	 
	   // setting org logo
		
	   dispTkt+="<div class='tkt-logo border-top'>";
	   if(obj.parentOrgName.includes("Africa Bet") > 0){
			dispTkt+="<div class='logo-rel'><img src='"+path+"/com/skilrock/lms/web/drawGames/common/pcposImg/africaBet_black.png' width='140' height='67' /></div>";
		}else{
			dispTkt+="<div class='logo-rel'><img src='"+path+"/com/skilrock/lms/web/drawGames/common/pcposImg/africa_loto.png' width='140' height='67' /></div>";			
		}
	   
	   // Setting game name
	   dispTkt+="<span class='tkt-gm_nm'>"+obj.gameName+"</span>";
	   
	   dispTkt+="</div'>";
	   
	   dispTkt+="<div class='name-tkt-2'>Winning Verification</div>"; 
	   dispTkt+="<div class='tkt-dt-m border-line'>";
	   dispTkt+="<div class='tkt-rw'><span class='no-o-tkt'>Ticket No:</span><span class='no-of-tkt'>"+obj.ticketNumber+"</span></div>";
	   dispTkt+="</div'>";
	   
	   dispTkt+="<div class='tkt-rw'><span class='no-o-dd'>Draw Date</span><span class='no-o-dt'>Draw Time</span></div>";
	   
	   
	   $(obj.drawData).each(function(){
		   dispTkt+="<div class='ndop'>";
			if(this.drawDate!=null && this.drawTime!=null){
				dispTkt+="<div class='tkt-rw'><span class='no-of-ddd'>"+dateFormat(new Date(getCompataibleDate3(this.drawDate)),dateFormat.masks.sikkimDate)+"</span><span class='no-of-dtt'>"+getCompataibaleTime(this.drawTime)+"</span></div>";
			}
			 
			if(this.drawName != null){
				dispTkt+="<div class='ticket-dr-name'>"+this.drawName+"</div>";
			}
			if(this.winStatus!=null){
				dispTkt+="<div class='tkt-rw'><span class='tkt-dv-10'></span><span class='ticket-dr-name'>"+this.winStatus.replace("_"," ")+"</span></div>";
			}
			if(this.winningAmt!=null){
				dispTkt+="<div class='tkt-rw'><span class='no-o-tkt'>Win Amt("+currencySymbol+"): </span><span class='no-of-tkt'>"+this.winningAmt+"</span></div>";
			}
			dispTkt+="</div>";
		});
	   
	   dispTkt+="<div class='tkt-rw border-top'><span class='no-o-tkt'>Total Pay: </span><span class='no-of-tkt'>("+currencySymbol+") "+obj.totalPay+"</div></div>";
	   
	   dispTkt+="<div class='bottom-username border-top'><span>"+obj.retailerName+"</span></div>";
	   dispTkt+="</div>";
	   $("#tktGen").append(dispTkt);
	  
	   // if PWT reprint case is there
	    if(obj.isReprint){
	    	prepareReprintTicket(obj.reprintData,'PWTReprint');
	    }
	   
}


function prepareReprintTicket(obj,reprintFor){
	if("PWT"!=curTrx){
		$("#tktGen").html("");
	}
	 var dispTkt = "";
	 var i=0;
	 var topAdvMsg=null;
	 dispTkt+="<div class='ticket-format'>";
		if(obj.advMsg!=null && obj.advMsg.TOP!=null){	
			var topAdvMsg=obj.advMsg.TOP[0];
			
			while(obj.advMsg.TOP[i]!=null){
				dispTkt+="<div class='dg-dg'>"+obj.advMsg.TOP[i++]+"</div>";
			}
		}
		
		//Setting Org LOGOpath
		dispTkt+="<div class='tkt-logo border-top'>";
		if(obj.parentOrgName.includes("Africa Bet") > 0){
			dispTkt+="<div class='logo-rel'><img src='"+path+"/com/skilrock/lms/web/drawGames/common/pcposImg/africaBet_black.png' width='140' height='67' /></div>";
		}else{
			dispTkt+="<div class='logo-rel'><img src='"+path+"/com/skilrock/lms/web/drawGames/common/pcposImg/africa_loto.png' width='140' height='67' /></div>";			
		}
		//Setting game name
		if(obj.gameName!=null){
			dispTkt+="<span class='tkt-gm_nm'>"+obj.gameName+"</span>";
			
		}
		dispTkt+="</div>";
		
		//Setting Reprint at message
		if(obj.retailerName!=null){
			dispTkt+="<div class='name-tkt-2'>Reprint-at : <span>"+obj.retailerName+"</span></div>";
			
		}
		
		//Setting ticket number
		dispTkt+="<div class='tkt-dt-m border-line'>";
		if(obj.ticketNumber!=null){
			dispTkt+="<div class='tkt-rw'>" + "<span class='no-o-tkt'>Ticket No:</span>" + "<span class='no-of-tkt'>"+obj.ticketNumber+"</span>" +"</div>";
			dispTkt+="<div class='tkt-rw'>" + "<span class='no-o-tkt'>Purchased On:</span>" + "<span class='no-of-tkt'>"+obj.purchaseDate+" "+obj.purchaseTime+"</span>" +"</div>";
		}
		dispTkt+="</div>";
		
		/*//Setting purchase time
		dispTkt+="<div class='bg-gary-tkt'>";
		if(obj.purchaseTime!=null){
			dispTkt+="<span class='dt-gary-tkt'>"+obj.purchaseDate+" "+obj.purchaseTime+"</span>";
		}
		dispTkt+="</div>";*/
		dispTkt+="<div class='tkt-dt-m border-top'>";
		//Setting purchase date
		dispTkt+="<div class='tkt-rw'>";
		if(obj.purchaseDate!=null){
			dispTkt+="<span class='no-o-dd'>Draw Date</span>";
			dispTkt+="<span class='no-o-dt'>Draw Time</span>";
			
		}
		dispTkt+="</div>";
		/*dispTkt+="<div class='tkt-rw'>";
		//Setting purchase time
		if(obj.purchaseTime!=null){
			dispTkt+="<span class='no-of-ddd'>"+obj.purchaseDate+"</span>";
			dispTkt+="<span class='no-of-dtt'>"+obj.purchaseTime+"</span>";
		}
		dispTkt+="</div>";*/
		
		
		//Setting draw date and time
	   if("KenoTwo" == obj.gameDevName){
			$(obj.drawData).each(function(){
				dispTkt+="<div class='tkt-rw'>";
				if(this.drawDate!=null){
					if(reprintFor=='Reprint'){
						dispTkt+="<span class='no-of-ddd'>"+dateFormat(new Date(getCompataibleDate4(this.drawDate)),dateFormat.masks.sikkimDate)+"</span>";
					}else{
						dispTkt+="<span class='no-of-ddd'>"+dateFormat(new Date(getCompataibleDate4(this.drawDate)),dateFormat.masks.sikkimDate)+"</span>";
					}
					
				}
				if(this.drawTime != null){
					dispTkt+="<span class='no-of-dtt'>"+getCompataibaleTime(this.drawTime)+"</span>";
				}
					/*if(this.drawName!=null){
						dispTkt+="<div class='no-of-tkt'>"+this.drawName+"</div>";
					}*/
					dispTkt+="</div>";
				});
	   }
	   else{
			$(obj.drawData).each(function(){
				dispTkt+="<div class='tkt-rw'>";
				if(this.drawDate!=null){
					if(reprintFor=='Reprint'){
						dispTkt+="<span class='no-of-ddd'>"+dateFormat(new Date(getCompataibleDate2(this.drawDate)),dateFormat.masks.sikkimDate)+"</span>";
					}else{
						dispTkt+="<span class='no-of-ddd'>"+dateFormat(new Date(getCompataibleDate3(this.drawDate)),dateFormat.masks.sikkimDate)+"</span>";
					}
					
				}
				if(this.drawTime != null){
					dispTkt+="<span class='no-of-dtt'>"+getCompataibaleTime(this.drawTime)+"</span>";
				}
					/*if(this.drawName!=null){
						dispTkt+="<div class='no-of-tkt'>"+this.drawName+"</div>";
					}*/
					dispTkt+="</div>";
				});
	   }
		
		
		dispTkt+="<div class='tkt-rw border-top'>";
		
		// Setting bet type name
		if("OneToTwelve" == obj.gameDevName || "MiniRoulette" == obj.gameDevName || "FullRoulette" == obj.gameDevName){
			$(obj.betTypeData).each(function(){
				dispTkt+="<div class='span-rt'>";
				if(this.betName!=null){
					dispTkt+="<span class='draw-nm-s'>"+this.betName+"</span>";
				}
				//Setting up quick pick or manual
				if(this.isQp){		
					dispTkt+="<span class='draw-nm-m'>QP</span>";
				}else{
					dispTkt+="<span class='draw-nm-m'>Manual</span>";
				}
				dispTkt+="</div>";
				
				
				//Setting numbers picked fortune
				dispTkt+="<div class='list-draw-result-d'>";
				if("OneToTwelve" == obj.gameDevName){
					if(this.pickedNumbers!=null){
						dispTkt+="<ul class='furtune-rest'><li><span>"+this.pickedNumbers+"</span>-<span class='zod'>"+oneByTwelveZodiacSigns[parseInt(this.pickedNumbers)-1]+"</span></li></ul>";
					}
				} else{
					if(this.pickedNumbers!=null){
						dispTkt+="<div class='ticket-text-pn'>"+this.pickedNumbers+"</div>";
					}
				}
				dispTkt+="</div>";
				
				//Setting number of lines
				if("MiniRoulette" == obj.gameDevName || "FullRoulette" == obj.gameDevName){
					dispTkt+="<div class='tkt-dt-m border-top'>";
					dispTkt+="<div class='tkt-rw'>";
					if(this.noOfLines!=null){
						dispTkt+="<span class='no-o-tkt'>No. of Line(s)</span>";
						dispTkt+="<span class='no-of-tkt'>"+this.noOfLines+"</span>";
					}
					dispTkt+="</div>";
					dispTkt+="</div>";
				}
				
				//Setting unit price
				dispTkt+="<div class='tkt-rw border-top-t'>";
				if("MiniRoulette" == obj.gameDevName || "FullRoulette" == obj.gameDevName){
					dispTkt+="<span class='no-o-tkt'>Bet Amount("+currencySymbol+")</span>";
					dispTkt+="<span class='no-of-tkt'>"+this.panelPrice+"</span>";
				}else{
					if(this.unitPrice!=null){
						dispTkt+="<span class='no-o-tkt'>Bet Amount("+currencySymbol+")</span>";
						dispTkt+="<span class='no-of-tkt'>"+this.unitPrice * this.betAmtMul+"</span>";
					}
				}
				dispTkt+="</div>";
				
			});
			
		} else{
			if(obj.betName!=null){
				dispTkt+="<span class='draw-nm-s'>"+obj.betName+"</span>";
			}
			// Setting up quick pick or manual
			if(obj.isQp){		
				dispTkt+="<span class='draw-nm-m'>QP</span>";
			}else{
				dispTkt+="<span class='draw-nm-m'>Manual</span>";
			}
			
			// Setting numbers picked
			var i=0;
			if("ZimLottoBonus" == obj.gameDevName && obj.pickedNumbers!=null){
				$(obj.pickedNumbers).each(function(){
					dispTkt+="<div class='ticket-text-pn'>"+this+"</div>";	
				});
			}
			else{
				if(obj.pickedNumbers!=null){
					dispTkt+="<div class='ticket-text-pn'>"+obj.pickedNumbers+"</div>";	
				}
			}
			dispTkt+="</div>";
			
			
			dispTkt+="<div class='tkt-dt-m border-top'>";
			// Setting number of lines
			dispTkt+="<div class='tkt-rw'>";
			if(obj.noOfLines!=null){
				dispTkt+="<span class='no-o-tkt'>No. of Line(s)</span>";
				dispTkt+="<span class='no-of-tkt'>"+obj.noOfLines+"</span>";
			}
			dispTkt+="</div>";
			// Setting unit price
			if("ZimLottoBonus" == obj.gameDevName){
				dispTkt+="<div class='tkt-rw'>";
				if(obj.unitPrice!=null){
						dispTkt+="<span class='no-o-tkt'>Bet Amount("+currencySymbol+")</span>";
					dispTkt+="<span class='no-of-tkt'>"+obj.unitPrice * obj.betAmtMul+"</span>";
				}
				dispTkt+="</div>";
			}
		}		
		
		
		
		// Setting number of draws
		dispTkt+="<div class='tkt-rw'>";
		if(obj.drawData!=null){
			dispTkt+="<span class='no-o-tkt'>No. of Draws:</span>";
			dispTkt+="<span class='no-of-tkt'>"+obj.drawData.length+"</span>";
		}
		dispTkt+="</div>";
		// Setting total amount
		dispTkt+="<div class='tkt-rw tlam'>";
		if(obj.purchaseAmt!=null){
			dispTkt+="<span class='no-o-tkt '>Total Amount("+currencySymbol+")</span>";
			dispTkt+="<span class='no-of-tkt'>"+obj.purchaseAmt+"</span>";
		}
		dispTkt+="</div>";
		dispTkt+="</div>";
		
		// Setting barcode
		dispTkt+="<div class='code-img'>";
		dispTkt+="<img src='"+path+"/LMSImages/images/barcode.png' class='code_img' />";	
		dispTkt+="<span class='code-no'>"+obj.ticketNumber+"</span>";
		dispTkt+="</div>";
		if("SIKKIM" == countryDeployed){
			dispTkt+="<div class='lottery-sign'><img src='"+path+"/com/skilrock/lms/web/drawGames/common/pcposImg/signature.png' class='sign_img' /></div>";
		}
		
		//Setting retailer name
		dispTkt+="<div class='bottom-username border-top'>";
		if(obj.retailerName!=null){
			dispTkt+="<span>"+obj.retailerName+"</span>";
		}
		dispTkt+="</div>";
		//Setting bottom adv message if any
		dispTkt+="<div class='bottom-username'>";
		var i=0;
		if(obj.advMsg!=null && obj.advMsg.BOTTOM!=null)
		{
			while(obj.advMsg.BOTTOM[i]!=null){
				dispTkt+="<div class='dg-dg'>"+obj.advMsg.BOTTOM[i++]+"</div>";
			}
		dispTkt+="</div>";
		}
		dispTkt+="</div>";
		dispTkt+="</div>";
		$("#tktGen").append(dispTkt);
	 

}

function prepareCancelTicket(obj){
	 $("#tktGen").html("");
	 var dispTkt = "";
	 var i=0;
		var topAdvMsg=null;
		dispTkt+="<div class='ticket-format'>";
		
		if(obj.advMsg!=null && obj.advMsg.TOP!=null){
			var topAdvMsg=obj.advMsg.TOP[0];
		    
		    while(obj.advMsg.TOP[i]!=null){
		    	dispTkt+="<div class='ticket-text ticket-magrin'><div class='name-tkt-2'>"+obj.advMsg.TOP[i++]+"</div></div>";		    	
		    }
		}
		
		//Setting Org Name
		dispTkt+="<div class='tkt-logo border-top'>";
		if(obj.parentOrgName.includes("Africa Bet") > 0){
			dispTkt+="<div class='logo-rel'><img src='"+path+"/com/skilrock/lms/web/drawGames/common/pcposImg/africaBet_black.png' width='140' height='67' /></div>";
		}else{
			dispTkt+="<div class='logo-rel'><img src='"+path+"/com/skilrock/lms/web/drawGames/common/pcposImg/africa_loto.png' width='140' height='67' /></div>";			
		}
		
		//Setting game name
		if(obj.gameDispName!=null){
			dispTkt+="<span class='tkt-gm_nm'>"+obj.gameDispName+"</span>";
		}
		dispTkt+="</div>";
		
		dispTkt+="<div class='tkt-dt-m border-line'>";
		//Setting ticket number
		if(obj.ticketNumber!=null){
			dispTkt+="<div class='tkt-rw'>" + "<span class='no-o-tkt'>Ticket No:</span>" + "<span class='no-of-tkt'>"+obj.ticketNumber+"</span>" + "</div>";
		}
		
		dispTkt+="<div class='tkt-rw'>";
		if(obj.cancelTime!=null){
			dispTkt+="<div class='tkt-rw'>" + "<span class='no-o-tkt'>Cancel Time:</span>" + "<span class='no-of-tkt'>"+dateFormat(new Date(getCompataibleDate2(obj.cancelTime.split(" ")[0])),dateFormat.masks.sikkimDate)+" "+getCompataibaleTime(obj.cancelTime.split(" ")[1])+"</span>" + "</div>";
			
		}
		if(obj.cancelTime!=null){
			
			
		}
		dispTkt+="</div>";
		dispTkt+="</div>";

		dispTkt+="<div class='bar-cd pdig'>Ticket Has Been Cancelled.</div>";
		
		if(obj.refundAmount!=null){
			dispTkt+="<div class='tkt-rw tlam'>" +
					"<span class='no-o-tkt '>Refund Amount:</span>" +
					"<span class='no-of-tkt'>"+currencySymbol+" "+obj.refundAmount+"/-</span>" +
							"</div>";
		}
		if("SIKKIM" == countryDeployed){
			dispTkt+="<div class='lottery-sign'><img src='"+path+"/com/skilrock/lms/web/drawGames/common/pcposImg/signature.png' class='sign_img' /></div>";
		}
		//Setting bottom adv message if any
		dispTkt+="<div class='bottom-username border-top'>";
		var i=0;
		if(obj.advMsg!=null && obj.advMsg.BOTTOM!=null){
			while(obj.advMsg.BOTTOM[i]!=null){
				dispTkt+="<span>"+obj.advMsg.BOTTOM[i++]+"</span>";
			}
			dispTkt+="</div>";
		}
		dispTkt+="</div'>";
		
		$("#tktGen").append(dispTkt);
	 
}


function setTimer1(elem_id, date, id) {
	setIntervalId1 = setInterval(function () {
		commonSetTimer(elem_id,date,id);
	}, 1000);
}
function setTimer2(elem_id, date, id) {
	setIntervalId2 = setInterval(function () {
		commonSetTimer(elem_id,date,id);
	}, 1000);
}
function setTimer3(elem_id, date, id) {
	setIntervalId3 = setInterval(function () {
		commonSetTimer(elem_id,date,id);
	}, 1000);
}
function setTimer4(elem_id, date, id) {
	setIntervalId4 = setInterval(function () {
		commonSetTimer(elem_id,date,id);
	}, 1000);
}
function setTimer5(elem_id, date, id) {
	setIntervalId5 = setInterval(function () {
		commonSetTimer(elem_id,date,id);
	}, 1000);
}
function setTimer6(elem_id, date, id) {
	setIntervalId6 = setInterval(function () {
		commonSetTimer(elem_id,date,id);
	}, 1000);
}
function setTimer7(elem_id, date, id) {
	setIntervalId7 = setInterval(function () {
		commonSetTimer(elem_id,date,id);
	}, 1000);
}
function setTimer8(elem_id, date, id) {
	setIntervalId8 = setInterval(function () {
		commonSetTimer(elem_id,date,id);
	}, 1000);
}
function setTimer9(elem_id, date, id) {
	setIntervalId9 = setInterval(function () {
		commonSetTimer(elem_id,date,id);
	}, 1000);
}
function setTimer10(elem_id, date, id) {
	setIntervalId10 = setInterval(function () {
		commonSetTimer(elem_id,date,id);
	}, 1000);
}
function commonSetTimer(elem_id, date, id){
	var target_date = new Date(date).getTime();
	//console.log(new Date(target_date)+"  server time "+new Date(serUpdatedCurDate));
	var days, hours, minutes, seconds;
	var current_date = serUpdatedCurDate;
	var seconds_left = (target_date - current_date) / 1000;
	days = parseInt(seconds_left / 86400);
	seconds_left = seconds_left % 86400;
	hours = parseInt(seconds_left / 3600);
	seconds_left = seconds_left % 3600;
	minutes = parseInt(seconds_left / 60);
	seconds = parseInt(seconds_left % 60);
	if(days == 00 && hours == 00 && minutes == 00 && seconds <= 00 && !isCalled){
		$("#"+elem_id).text(seconds <= 0 ? "0":seconds);
		$("#"+elem_id).siblings(".next-dr-tm").text(seconds>1?"secs":"sec");
		isCalled = true;
		callDrawDateTimer(id);
	}else if(days == 00 && hours == 00 && minutes == 00){
		if(seconds >= 0 ){
			$("#"+elem_id).text(seconds);
			$("#"+elem_id).siblings(".next-dr-tm").text(seconds>1?"secs":"sec");
		}
	}else if (days == 00 && hours == 00) {
		if(minutes >= 0 ){
			$("#"+elem_id).text(minutes);
			$("#"+elem_id).siblings(".next-dr-tm").text(minutes>1?"mins":"min");
		}
	}else if (days == 00) {
			$("#"+elem_id).text(hours);
			$("#"+elem_id).siblings(".next-dr-tm").text(hours>1?"hrs":"hr");
	}else {
			$("#"+elem_id).text(days);
			$("#"+elem_id).siblings(".next-dr-tm").text(days>1?"days":"day");
	}
}

function currServerTime(){
	var dArr =[]; 
	dArr = $("#currentServerTime").val().split(" ")[0].split("-");
	var fDate = dArr[1]+"/"+dArr[2]+"/"+dArr[0]+" "+$("#currentServerTime").val().split(" ")[1];
	serUpdatedCurDate= new Date(fDate).getTime();
	var id = setInterval(function(){
		serUpdatedCurDate = serUpdatedCurDate + 1000;
	},1000);
}
	
function getGameWiseRNG(gameCode,noPicked,linesSelectedQp){
	var data = {"gameCode":gameCode,"noPicked":parseInt(noPicked),"noOfLines":parseInt(linesSelectedQp)};
	var json = JSON.stringify(data);
	var rngResp = _ajaxCallJson(path+'/com/skilrock/lms/web/drawGames/playMgmt/fetchGameWiseRNG.action', "json="+json);
	return rngResp;
}
function checkCurrentDrawFreezeScreenActive(timerDrawFreezeTime,timerDrawDateTime,gameDiv) {
	$("."+gameDiv).html("");
	var gameSel = "";
	gameInFocus = gameDiv.split("-")[1];
	$(".sideMenuList").each(function(){
		if($(this).hasClass("select-game")){
			gameSel = $(this).children().eq(1).text();
		} 
	});
	freezeTimerIntervalId = setInterval(function () {
		if(gameInFocus == gameSel){
			$("#advanceDraw").css("display","none");
			$(".select-game").focus();
			if('TwelveByTwentyFour' == gameInFocus){
				resetTwelveByTwentyFourForQP();
			}else if('PickThree' == gameInFocus){
				resetPickThreeGame();
			}else if('PickFour' == gameInFocus){
				resetPickFourGame();
			}else if('OneToTwelve' == gameInFocus){
				resetOneByTwelveGame();
			}else if('KenoSix' == gameInFocus){
				resetEightyTwentyTenGame();
			}else if("MiniRoulette" == gameInFocus){
				resetMiniRoulette();
			}else if("KenoTwo" == gameInFocus){
				resetKenoTwo();
			}else if("FullRoulette" == gameInFocus){
				resetFullRoulette();
			}else if("TenByTwenty" == gameInFocus){
				resetTenByTwentyGame();
			}else if("ZimLottoBonus" == gameInFocus){
				changeBetTypeZimLottoBonus();
			}
		}
		startDrawScreen(timerDrawFreezeTime,timerDrawDateTime,gameDiv);
	},1000);
}
function startDrawScreen(timerDrawFreezeTime,timerDrawDateTime,gameDiv){
	//console.log("serUpdatedCurDate "+new Date(serUpdatedCurDate) +" timerDrawFreezeTime "+new Date(timerDrawFreezeTime)+" timerDrawDateTime "+new Date(timerDrawDateTime));
	if(serUpdatedCurDate >= timerDrawFreezeTime && serUpdatedCurDate <= timerDrawDateTime){
		var delayTime = ((timerDrawDateTime - serUpdatedCurDate)/1000) +1;
		if(delayTime > 0){
			clearInterval(freezeTimerIntervalId);
			$("."+gameDiv).circularCountDown({
	              delayToFadeIn: 500,
				  size: 182,
				  fontColor: '#fff',
				  colorCircle: 'black',
				  background: '#2ECC71',
	              reverseLoading: false,
	              duration: {
	                  seconds: parseInt(delayTime)
	              },
	              beforeStart: function() {
	                  //$('.launcher').hide();
	              },
	              end: function(countdown) {
	                  countdown.destroy();
	                  $("."+gameDiv).css("display","none");
                      clearInterval(setIntervalId1);
	      			  clearInterval(setIntervalId2);
	      			  clearInterval(setIntervalId3);
	      			  clearInterval(setIntervalId4);
	      			  clearInterval(setIntervalId5);
	      			  clearInterval(setIntervalId6);
	      			  clearInterval(setIntervalId7);
	      			  clearInterval(setIntervalId8);
	      			  clearInterval(setIntervalId9);
	      			  clearInterval(setIntervalId10);
	      			  currentFocus = document.activeElement.id;
	      			  isCalled = false;
	                  isSideMenuBuild = false;
	                  isForTimer = true;
	                  var resetTimer = true;
	                  var menuId = "";
	                  $(".sideMenuList").each(function(){
	                	 if($(this).hasClass("select-game")){
	                		 menuId = $(this).attr('id');
	                	 } 
	                  });
	                  var gameId = menuId.split('~')[1];
	                  updateMidPanel(menuId, gameId,resetTimer);
	              }
	          });
		}
	}
}

//if date comes in dd-MM-yyyy hh:mm:ss format
function getCompataibleDate(date){
	var arr = date.split(" ")[0].split("-");
	var finalDate = arr[1]+"/"+arr[0]+"/"+arr[2]+" "+date.split(" ")[1];
	return finalDate;
}

//if date comes in yyyy-MM-dd format
function getCompataibleDate2(date){
	var dArr = date.split("-");
	var finalDate = dArr[1]+"/"+dArr[2]+"/"+dArr[0];
	return finalDate;
}

//if time comes in HH:MM:SS format
function getCompataibaleTime(time){
	var tArr=time.split(":");
	var finalTime= tArr[0]+":"+tArr[1];
	return finalTime;
}		

//if date comes in dd-MM-yyyy format
function getCompataibleDate3(date){
	var arr = date.split("-");
	var finalDate = arr[1]+"/"+arr[0]+"/"+arr[2];
	return finalDate;
}

function getCompataibleDate4(date){
	var arr = date.split("-");
	var finalDate = arr[1]+"/"+arr[2]+"/"+arr[0];
	return finalDate;
}

function resultPrintFunc(){
	resetDateTimePicker();	
	$('#selectedDateTimePicker').click(function(){
		$('#selectedDateTimePicker').blur();
	});

	$('#selectedDateTimePickerDiv').click(function(){
		$('#selectedDateTimePicker').datetimepicker('show');
		$('#selectedDateTimePicker').blur();
	});
	
	$('#selectedDateTimePicker').change(function(){
		clearResultPrintErrorDiv();
		$("#drawTimeSelect").empty();
		$('<option/>').val(-1).html("Please Select").appendTo('#drawTimeSelect');
		var data = {"gameCode":$(".active-button-result").first().children().eq(1).text(),"merchantCode":"LMS","date":$("#selectedDateTimePicker").val()};
		var json = JSON.stringify(data);
		var drawResultResp = _ajaxCallJson(path+'/com/skilrock/lms/web/drawGames/playMgmt/fetchDrawGameDataResultPCPOS.action', "json="+json);
		printResDataJson = {};
		var drawTimeArr = [];
		if(drawResultResp.responseCode == 0){
			if(drawResultResp.gameData[0].resultData.length>0){
				var drawDate=drawResultResp.gameData[0].resultData[0].resultDate;
				$(drawResultResp.gameData[0].resultData[0].resultInfo).each(function(){
					printResDataJson[this.drawTime]=this.drawName+"#"+drawDate+"#"+this.winningNo;
					drawTimeArr.push(this.drawTime);
					
				});
				for (var i=0;i<drawTimeArr.length;i++){
					   $('<option/>').val(drawTimeArr[i]).html(drawTimeArr[i]).appendTo('#drawTimeSelect');
					}
			}else{
				
				$("#timeErrorDiv").css("display","block");
				$("#noDrawDiv").html("No draw available!!");
				$("#timeErrorDiv").delay(2000).fadeOut("slow");
			
			}
		}
	});

}

function resetDateTimePicker() {
	 $('#selectedDateTimePicker').datetimepicker( {
		 mask : '9999/19/39',
		 format : 'Y-m-d',
		 formatDate : 'Y-m-d',
		 timepicker : false,
		 maxDate: '0'
	 });
}

function clearResultPrintErrorDiv(){
	$("#dateErrorDiv").html("");
	$("#dateErrorDiv").css("display","none");
	
	$("#timeErrorDiv").css("display","none");
	$("#close-popup-button").click(function(){
		$("#timeErrorDiv").css("display","none");
	});
}

function setAppletData(buyData) {
	resetAllGames();
	$("#regDiv").innerHTML = "";
	$("#regButton").innerHTML = "";
	isPrintFail = false;
	if("PrintResult" == curTrx){
		$("#parentApplet").css("display", "none");
	}else{		
		$("#parentApplet").css("display", "block");
	}
	document.applets[0].showStatus(buyData);
	document.applets[0].repaint();
}

var isPrintFail = false;
function checkAndShowTicketForApplet(tktNum, totAmt, printStatus){
	if("SUCCESS" == printStatus){
		$("#buy").prop('disabled',true);
		parent.frames[0].updateBalance(path + "/com/skilrock/lms/web/loginMgmt/updateAvlCreditAmt.action");
		reprintCountChild = 0;
		if("TestPrint" == curTrx){
			$("#success").html("Printer is working fine!");
			$("#success-popup").css('display','block');		
			$('#success-popup').delay(1500).fadeOut('slow');
		}else if("PrintResult" == curTrx){
			$("#parentApplet").css("display", "none");
		}
		return true;
	} else if("FAIL" == printStatus){
		if("Buy" == curTrx){
			if(reprintCountChild < 2){
				var status = confirm("Printer not connected!! \nCheck your printer and press OK to reprint or press Cancel to cancel the ticket!!");
				if(!status){
					var data = {"ticketNumber":tktNum, "autoCancel":true};
					data = JSON.stringify(data);
					var _resp = winAjaxReq("cancelTicketAuto.action?json="+data);
					parent.frames[0].updateBalance(path + "/com/skilrock/lms/web/loginMgmt/updateAvlCreditAmt.action");
					$("#buy").prop('disabled',true);
					$("#parentApplet").css("display","none");
					reprintCountChild = 0;
					return false;
				}else{
					setAppletData(printData);
					reprintCountChild++;
				}
			} else{
				var data = {"ticketNumber":tktNum, "autoCancel":true};
				data = JSON.stringify(data);
				var _resp = winAjaxReq("cancelTicketAuto.action?json="+data);
				parent.frames[0].updateBalance(path + "/com/skilrock/lms/web/loginMgmt/updateAvlCreditAmt.action");
				$("#buy").prop('disabled',true);
				reprintCountChild = 0;
				$("#parentApplet").css("display","none");
				alert("Printer not connected!! \nLast ticket has been cancelled!!");
				return false;
			}
		}else if ("Reprint" == curTrx) {
			if (reprintCount < 2) {
				$("#parentApplet").css("display","none");
				alert("Printer not connected. \nCheck your printer and try again!!");
				return true;
			} else {
				return false;
			}
		}else if("Cancel" == curTrx){
			$("#parentApplet").css("display","none");
			alert("Printer not connected.\nLast ticket has been cancelled!!");
			parent.frames[0].updateBalance(path + "/com/skilrock/lms/web/loginMgmt/updateAvlCreditAmt.action");
		}else if("TestPrint" == curTrx){
			alert("Printer not connected!!");
		}
	}
}

function assignFocus(){
	$("#"+currentFocus).focus();
}