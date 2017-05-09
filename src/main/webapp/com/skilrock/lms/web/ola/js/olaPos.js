var mobileLimit='';
var walletDevName='';
var withReqData='';
var depReqData='';
var regReqData='';
var keyupFiredCount=0;
var regType='';
var walletName = '';
var displayName='';
var isUserErrorMessage = false;
var isMobileErrorMessage = false;
var userActionListArray = ["olaPlayerRegistrationSubmit","olaRetDepositSave","olaRetWithdrawalSuccess"];
$(document).ready(function (){
	setSideLabelFunctionality();
	$("#regWalletTab").html('');
	//fetch wallet details
	$("#idType").css("display","none");
	$("#idNo").css("display","none");
	$("#cardNo").css("display","none");
	$("#idUpload").css("display","none");
	$("#depCardDiv").css("display","none");
	$("#withCardDiv").css("display","none");
	$("#regButtonDiv").css("display","none");
	createWalletTab();
	
	
    function DelayExecution(f, delay) {
        var timer = null; 
        return function () {
            var context = this, args = arguments;
            clearTimeout(timer);
            timer = window.setTimeout(function () {
                f.apply(context, args);
            },
            delay || 10);
        };
    }
    
    $.fn.ConvertToBarcodeTextbox = function () {
         $(this).keyup(DelayExecution(function (event) {
    	/* if(event.keyCode==13){
        		$('#pwtOk').click();
    	 }*/
         if($(this).val().length>=10 && keyupFiredCount<=1){
        	 $(this).attr("readonly",true);
         }
             var keycode = (event.keyCode ? event.keyCode : event.which);  
             	if($(this).val().length<10){
             		alert("Please enter value by swipe reader only!!");
             		$(this).val('');
             		//keyupFiredCount = keyupFiredCount + 1;  
             	}    
         }));
     };
     try {
    	 
         $("#regCardNo,#depCardNo,#withCardNo").ConvertToBarcodeTextbox();

     } catch (e) { 
    	 
     }
//to create reg form according to reg type
$(document).on("click",".wallet",function(){
	clearDiv();
	$("#regBtn").attr("disabled",false);
	regType=$(this).children().first().attr('regType');  
	walletName=$(this).children().first().attr('id');
	displayName=$(this).children().first().attr('displayName');
	prepareRegForm(regType);
});

//when side menu deposit clicked
$(document).on("click","#dep,#backDep",function(){
	clearDiv();
	$("#mainForm").css("display","block");
	$("#depBtn").attr("disabled",false);
	$('.clearFix').children().children().first().prop('checked', true);
	$("#depositForm").css("display","block");
	$("#withdrawalForm").css("display","none");
	$("#regForm").css("display","none");
	$('#regSuccessBox').css("display",'none');
	$("#withSuccessBox").css("display","none");
	$("#depSuccessBox").css("display","none");
	$(".page-title").html("OLA Deposit");
	prepareRegForm($('.clearFix').children().children().first().prop('checked', true).attr('regType'));
});

//when side menu withdrawal clicked
$(document).on("click","#withrwl,#backWith",function(){
	clearDiv();
	$("#mainForm").css("display","block");
	$("#withBtn").attr("disabled",false);
	$('.clearFix').children().children().first().prop('checked', true);
	$("#withdrawalForm").css("display","block");
	$("#depositForm").css("display","none");
	$("#regForm").css("display","none");
	$(".page-title").html("OLA Withdrawal");
	$("#withSuccessBox").css("display","none");
	$('#regSuccessBox').css("display",'none');
	$("#depSuccessBox").css("display","none");
	prepareRegForm($('.clearFix').children().children().first().prop('checked', true).attr('regType'));
});
//when side menu REPORT clicked
$(document).on("click","#report",function(){
	clearDiv();
	parent.frames[1].location.replace(projectName+"/com/skilrock/lms/web/drawGames/reportsMgmt/retailer/mainPageReports.jsp");
});


//when side menu Registration clicked
$(document).on("click","#reg",function(){
	clearDiv();
	$("#mainForm").css("display","block");
	$('.clearFix').children().children().first().prop('checked', true);
	$("#regForm").css("display","block");
	$("#regButtonDiv").css("display","block");
	$("#withdrawalForm").css("display","none");
	$("#depositForm").css("display","none");
	$('#regSuccessBox').css("display",'none');
	$("#withSuccessBox").css("display","none");
	$("#depSuccessBox").css("display","none");
	$(".page-title").html("OLA Registration");
	prepareRegForm($('.clearFix').children().children().first().prop('checked', true).attr('regType'));
});

//on change of mobile no @deposit form
$(document).on("change","#depMob,#depCardNo",function(){
	//var regType=$('input:radio[name=selectOne_lbl]:checked').attr('regType');
	$("#depBtn").attr("disabled",true);
	var refCode='';
	var isValid;
	var errorDiv='';
	if(regType=='ALA'){
		errorDiv='dep-cardNo-msg';
		refCode=$.trim($("#depCardNo").val());
		isValid=validateCardNo(refCode, errorDiv);
	}else{
		errorDiv='dep-mobile-msg';
		refCode=$.trim($("#depMob").val());
		isValid=validateMobileNo(refCode, mobileLimit, errorDiv);
	}
	if(isValid){
		//var walletName=getSelectedWallet();
		$("#"+errorDiv).html("Validating mobile number.....<img src=\"" + projectName + "/LMSImages/images/loadingAjax.gif\"/>");
		var url = projectName+"/com/skilrock/ola/commonMethods/action/verifyRefCode.action?refCode="+refCode+"&walletName="+walletName;
		var resp = _ajaxCallJson(url);
		if (resp.isSuccess) {
			$("#"+errorDiv).parent().addClass("success-msg");
			$("#"+errorDiv).html(resp.message);
			$("#depBtn").attr("disabled",false);
			$("#depBtn").focus();
		} else{
			$("#"+errorDiv).html(resp.message);
			$("#"+errorDiv).parent().addClass("error-msg");
			$("#depBtn").attr("disabled",true);
		}
	}
	
});


//to submit deposit form
$(document).on("click","#depBtn",function(){
	$("#depPopUpData").html('');
	var isValidCard=true;
	var isValidMob=true;
	//var regType=$('input:radio[name=selectOne_lbl]:checked').attr('regType');
	$("#depBtn").attr("disabled",true);
	var mobile=$.trim($("#depMob").val());
	var cardNo=$.trim($("#depCardNo").val());
	var amount1=$.trim($("#depAmt").val());
	var amount2=$.trim($("#depVerAmt").val());
	if(regType=='ALA'){
		isValidCard=validateCardNo(cardNo, 'dep-cardNo-msg');
	}else{
		isValidMob=validateMobileNo(mobile, mobileLimit, "dep-mobile-msg");
	}
	 
	var isValidAmt=validateAmountOrVerCode(amount1,amount2,"dep-amt-msg","dep-ver-amt-msg","DEPOSIT");
	if(isValidMob && isValidAmt && isValidCard){
		var s='';
		if(regType=='ALA'){
			mobile=cardNo;
			s+="<div class='detail-con'><span>Card Number:</span><span>"+cardNo+"</span></div>";
		}else{
			s+="<div class='detail-con'><span>Mobile Number:</span><span>"+mobile+"</span></div>";
		}
		
		s+="<div class='detail-con'><span>Amount:</span><span>"+amount1+" "+currencySymbol+"</span></div>";
		
		$("#depPopUpData").append(s);
		$("#olaDepConfirmBox").css("display","block");
		$("#depConfirm").focus();

		 depReqData = JSON.stringify({
				  			"refCode": mobile,
			  				"depositAmt": amount1,
			  				//"walletDevName":getSelectedWallet()
			  				"walletDevName":walletName
						});
	}else{
		$("#depBtn").attr("disabled",false);
	}
	
});

$(document).on("click","#depConfirm",function(){
	$("#depSuccessData").html('');
	//var regType=$('input:radio[name=selectOne_lbl]:checked').attr('regType');
	$("#olaDepConfirmBox").css("display","none");
	var actionURL=projectName+'/com/skilrock/ola/web/accMgmt/action/olaRetDepositSave.action';
	var depRespData = _ajaxCallJson(actionURL,"reqData="+depReqData);
	$("#depBtn").attr("disabled",false);
	clearDiv();
	if(depRespData.isSuccess){
		var res=$.parseJSON(depReqData);
		var s='';
		if(regType=='ALA'){
			s+="<div class='detail-con'><span>Card Number :</span><span>"+res.refCode+"</span></div>";
		}else{
			s+="<div class='detail-con'><span>Mobile Number :</span><span>"+res.refCode+"</span></div>";
		}
		s+="<div class='detail-con'><span>Amount Deposited :</span><span>"+res.depositAmt+" "+currencySymbol+"</span></div>";
		$("#depSuccessData").append(s);
		$("#mainForm").css("display","none");
		$('#depFailureImg').css('display','none');
		$('#depFailureMsg').css('display','none');
		$("#depSuccessBox").css("display","block");
		$("#depSuccessImg").css("display","block");
		$("#depSuccessMsg").css("display","block");
		$('#depSuccessData').removeClass("error-msg-new");
	}else{
		  $('#mainForm').css('display','none');
		  $('#depSuccessData').html('');
		  $('#depSuccessBox').css('display','block');
		  $('#depSuccessImg').css('display','none');
		  $('#depFailureImg').css('display','block');
		  $('#depSuccessMsg').css('display','none');		  
		  $('#depFailureMsg').css('display','block');
		  $('#depSuccessData').addClass("error-msg-new");
		  $("#depSuccessData").append(depRespData.responseMsg);
	}
});

$(document).on("click","#depCancel",function(){
	$("#olaDepConfirmBox").css("display","none");
	$("#depBtn").attr("disabled",false);
});


//on change of mobile no @withdrawal form
$(document).on("change","#withMob,#withCardNo",function(){
	//var regType=$('input:radio[name=selectOne_lbl]:checked').attr('regType');
	$("#withBtn").attr("disabled",true);
	var isValid;
	var errorDiv='';
	var refCode='';
	if(regType=='ALA'){
		errorDiv="with-cardNo-msg";
		refCode=$.trim($("#withCardNo").val());
		isValid=validateCardNo(refCode, errorDiv);
	}else{
		errorDiv="with-mobile-msg";
		refCode=$.trim($("#withMob").val());
		isValid=validateMobileNo(refCode, mobileLimit, errorDiv);
	}
	
	if(isValid){
		//var walletName=getSelectedWallet();
		$("#"+errorDiv).html("Validating mobile number.....<img src=\"" + projectName + "/LMSImages/images/loadingAjax.gif\"/>");
		var url = projectName+"/com/skilrock/ola/commonMethods/action/verifyRefCode.action?refCode="+refCode+"&walletName="+walletName;
		var resp = _ajaxCallJson(url);
		if (resp.isSuccess) {
			$("#"+errorDiv).parent().addClass("success-msg");
			$("#"+errorDiv).html(resp.message);
			$("#withBtn").attr("disabled",false);
			$("#withBtn").focus();
		} else{
			$("#"+errorDiv).html(resp.message);
			$("#"+errorDiv).parent().addClass("error-msg");
			$("#withBtn").attr("disabled",true);
		}
	}
});

//to submit withdrawal form
$(document).on('click',"#withBtn",function(){
	$("#withPopUpData").html('');
	$("#withBtn").attr("disabled",true);
	var isValidCard=true;
	var isValidMob=true;
	//var regType=$('input:radio[name=selectOne_lbl]:checked').attr('regType');
	var mobile=$.trim($("#withMob").val());
	var cardNo=$.trim($("#withCardNo").val());
	var amount1=$("#withAmt").val();
	var verCode=$.trim($("#withVerCode").val());
	if(regType=='ALA'){
		isValidCard=validateCardNo(cardNo, 'with-cardNo-msg');
	}else{
		isValidMob=validateMobileNo(mobile, mobileLimit, "with-mobile-msg");
	}
	
	var isValidAmt=validateAmountOrVerCode(amount1,verCode,"with-amt-msg","with-verCode-msg","WITHDRAWAL");
	if(isValidMob && isValidAmt &&isValidCard){
		var s='';
		if(regType=='ALA'){
			mobile=cardNo;
			s+="<div class='detail-con'><span>Card Number:</span><span>"+cardNo+"</span></div>";
		}else{
			s+="<div class='detail-con'><span>Mobile Number:</span><span>"+mobile+"</span></div>";
		}
		s+="<div class='detail-con'><span>Amount:</span><span>"+amount1+" "+currencySymbol+"</span></div>";
		s+="<div class='detail-con'><span>Verification Code:</span><span>"+verCode+"</span></div>";
		$("#withPopUpData").append(s);
		$("#olaWithConfirmBox").css("display","block");
		$("#withConfirm").focus();
		withReqData = JSON.stringify({
				  				"refCode": mobile,
								"withdrawlAmt": amount1,
								"authenticationCode": verCode,
								//"devWalletName":getSelectedWallet()
								"devWalletName":walletName
		});
	}else{
		$("#withBtn").attr("disabled",false);
	}
	
});

$(document).on('click',"#withConfirm",function(){
	$("#olaWithConfirmBox").css("display","none");
	$("#withSuccessData").html('');
	//var regType=$('input:radio[name=selectOne_lbl]:checked').attr('regType');
	var actionURL=projectName+'/com/skilrock/ola/web/accMgmt/action/olaRetWithdrawalSuccess.action';
	var withRespData = _ajaxCallJson(actionURL,"reqData="+withReqData);
	$("#withBtn").attr("disabled",false);
	clearDiv();
	if(withRespData.isSuccess){
		var res=$.parseJSON(withReqData);
		var s='';
		if(regType=='ALA'){
			s+="<div class='detail-con'><span>Card Number :</span><span>"+res.refCode+"</span></div>";
		}else{
			s+="<div class='detail-con'><span>Mobile Number :</span><span>"+res.refCode+"</span></div>";
		}
		s+="<div class='detail-con'><span>Amount Withdrawan :</span><span>"+res.withdrawlAmt+" "+currencySymbol+"</span></div>";
		$("#withSuccessData").append(s);
		$('#withSuccessData').removeClass("error-msg-new");
		$("#mainForm").css("display","none");
		$('#withFailureImg').css('display','none');
		$('#withFailureMsg').css('display','none');
		$("#withSuccessBox").css("display","block");
		$("#withSuccessImg").css("display","block");
		$("#withSuccessMsg").css("display","block");
	}else{
		  $('#mainForm').css('display','none');
		  $('#withSuccessData').html('');
		  $('#withSuccessBox').css('display','block');
		  $('#withSuccessImg').css('display','none');
		  $('#withFailureImg').css('display','block');
		  $('#withSuccessMsg').css('display','none');		  
		  $('#withFailureMsg').css('display','block');
		  $('#withSuccessData').addClass("error-msg-new");
		  $('#withSuccessData').append(withRespData.responseMsg);
	}
	
});

$(document).on('click',"#withCancel",function(){
	$("#olaWithConfirmBox").css("display","none");
	$("#withBtn").attr("disabled",false);
});

$(document).on('change','#regMobile',function(){
	isMobileErrorMessage = false;
	$("#regBtn").attr("disabled",true);
	var mobile = $.trim($('#regMobile').val());
	//walletDevName= getSelectedWallet();
	
	walletDevName=walletName;
	var isValid = validateMobileNo(mobile,mobileLimit,'reg-phone-msg');
	if(isValid){
		_id.o("reg-phone-msg").innerHTML = "Validating Mobile Number.....<img src=\"" + projectName + "/LMSImages/images/loadingAjax.gif\"/>";
		url = projectName+"/com/skilrock/ola/commonMethods/action/checkMobileNumAvalblity.action?phone="+mobile+"&walletName="+walletDevName;
		var resp1 = _ajaxCallJson(url);
		if (resp1.isSuccess) {
			isMobileErrorMessage = true;
			$("#reg-phone-msg").parent().addClass("error-msg");
			$('#reg-phone-msg').html(resp1.message);
			
		} else{
				isMobileErrorMessage = false;
			    $("#regBtn").attr("disabled",false);
				$("#reg-phone-msg").parent().addClass("success-msg");
				$('#reg-phone-msg').html(resp1.message);
		  }
	} else{
		isMobileErrorMessage = false;
	}
});

$(document).on('change','#regUname,#regCardNo',function(){
	$("#regBtn").attr("disabled",true);
	isUserErrorMessage = false;
	//var regType=$('input:radio[name=selectOne_lbl]:checked').attr('regType');
	var userName='';
	var isValid;
	var errorDiv='';
	
	if(regType=='ALA'){
		errorDiv='reg-cardNo-msg';
		userName = $.trim($('#regCardNo').val());
		userName = userName.substring(1,userName.length -1 );
		$('#regCardNo').val(userName);
		isValid = validateCardNo(userName, errorDiv);
	}else{
		errorDiv='reg-username-msg';
		userName = $.trim($('#regUname').val());
		isValid = validateUsername(userName,errorDiv);
	}
	//walletDevName= getSelectedWallet();
	walletDevName=walletName;
	if(isValid){
		$("#regMobile").focus();
		_id.o(errorDiv).innerHTML = "Validating User Name.....<img src=\"" + projectName + "/LMSImages/images/loadingAjax.gif\"/>";
		url = projectName+"/com/skilrock/ola/commonMethods/action/checkUserNameAvalblity.action?userName="+userName+"&walletName="+walletDevName;
		var resp1 = _ajaxCallJson(url);
		if (resp1.isSuccess) {
				isUserErrorMessage = true;
				$('#'+errorDiv).parent().addClass("error-msg");
				$('#'+errorDiv).html(resp1.message);
		} else{
				isUserErrorMessage = false;
			    $("#regBtn").attr("disabled",false);
				$('#'+errorDiv).parent().addClass("success-msg");
				$('#'+errorDiv).html(resp1.message);
				
		  }
	} else{
		isUserErrorMessage = false;
	}
});



$(document).on('change','#regEmail',function(){
	$("#regBtn").attr("disabled",true);
	var email = $.trim($('#regEmail').val());
//	walletDevName= getSelectedWallet();
	walletDevName=walletName;
	
	var isValid = validateEmail(email,'reg-email-msg');
	if(isValid){
		_id.o("reg-email-msg").innerHTML =  "Validating Emain ID.....<img src=\"" + projectName + "/LMSImages/images/loadingAjax.gif\"/>";
		url = projectName+"/com/skilrock/ola/commonMethods/action/checkEmailAvalblity.action?email="+email+"&walletName="+walletDevName;
		var resp1 = _ajaxCallJson(url);

		if (resp1.isSuccess) {
			$('#reg-email-msg').html(resp1.message);
			$("#reg-email-msg").parent().addClass("error-msg");
			
		} else{
			$("#regBtn").attr("disabled",false);
				$('#reg-email-msg').html(resp1.message);
				$("#reg-email-msg").parent().addClass("success-msg");
		  }
}
	
});


$(document).on('click','#regBtn',function(){
   validateRegForm(regType);
   //$('input:radio[name=selectOne_lbl]:checked').attr('regType')
});





$(document).on("click","#regConfirm",function(){
	//var regType=$('input:radio[name=selectOne_lbl]:checked').attr('regType');
	$("#olaRegConfirmBox").css("display","none");
	$("#regSuccessData").html('');
	clearDiv();
	var actionURL=projectName+'/com/skilrock/ola/web/userMgmt/action/olaPlayerRegistrationSubmit.action';
	var regRespData = _ajaxCallJson(actionURL,"regReqData="+regReqData);
	if(regRespData.isSuccess){
		$('#mainForm').css('display','none');
		$('#regFailureImg').css('display','none');
		$('#regFailureMsg').css('display','none');
		 var s='';
		 if(regType=='FULL'){
			 s+="<div class='detail-con'><span>User Name :</span><span >"+regRespData.username+"</span></div>" ;
			 s+="<div class='detail-con'><span>Mobile Number:</span><span >"+regRespData.phone+"</span></div>" ;
			 s+="<div class='detail-con'><span>Email :</span><span >"+regRespData.email+"</span></div>" ;
		 }else if(regType=='MOBILE'){
			 s+="<div class='detail-con'><span>Mobile Number:</span><span >"+regRespData.phone+"</span></div>" ;
		 }else if(regType=='ALA'){
			 s+="<div class='detail-con'><span>User Name :</span><span >"+regRespData.username+"</span></div>" ;
			 s+="<div class='detail-con'><span>Mobile Number:</span><span >"+regRespData.phone+"</span></div>" ;
		 }else{
			 s+="<div class='detail-con'><span>User Name :</span><span >"+regRespData.username+"</span></div>" ;
		 }
		 if(regRespData.depositAmt!=0){
			 s+="<div class='detail-con'><span>Amount :</span><span >"+regRespData.depositAmt+" "+currencySymbol+"</span></div>" ;
		 }
		 $('#regSuccessData').append(s);
		 $('#regSuccessData').removeClass("error-msg-new");
		 $('#regSuccessBox').css('display','block');
		 $('#regSuccessImg').css('display','block');
		 $('#regSuccessMsg').css('display','block');		  
	
		 //for ticket printing{}
		if(regType=='ANONYMOUS'){
			var regPrintData = JSON.stringify({
				"regDate": regRespData.plrRegDate.split(" ")[0],
				"regTime": regRespData.plrRegDate.split(" ")[1],
				"username":regRespData.username,
				//"walletName": getWalletDisplayName(),
				"walletName": displayName,
				"password":regRespData.password,
				"retailerName":regRespData.reatilerName,
				"mode":"REG",
				"serviceName":"OLA"
			    });
		    	setAppDataOLA(regPrintData);
		    }
	  }else{
		  $('#mainForm').css('display','none');
		  $('#regSuccessData').html('');
		  $('#regSuccessBox').css('display','block');
		  $('#regSuccessImg').css('display','none');
		  $('#regFailureImg').css('display','block');
		  $('#regSuccessMsg').css('display','none');		  
		  $('#regFailureMsg').css('display','block');
		  $('#regSuccessData').addClass("error-msg-new");
		  $('#regSuccessData').append(regRespData.responseMsg);
    }
	
});

$(document).on("click","#regCancel",function(){
	$("#olaRegConfirmBox").css("display","none");
	
});


/**
 * on keydown on input fields
 */

$(document).on("keydown","#depMob",function(){
	if($("#dep-mobile-msg").html()!=""){
		$("#dep-mobile-msg").html("");
	}
});

$(document).on("keydown","#depAmt",function(){
	if($("#dep-amt-msg").html()!=""){
		$("#dep-amt-msg").html("");
	}
});

$(document).on("keydown","#depVerAmt",function(){
	if($("#dep-ver-amt-msg").html()!=""){
		$("#dep-ver-amt-msg").html("");
	}
	
});

$(document).on("keydown","#withMob",function(){
	if($("#with-mobile-msg").html()!=""){
		$("#with-mobile-msg").html("");
	}
});
$(document).on("keydown","#withAmt",function(){
	if($("#with-amt-msg").html()!=""){
		$("#with-amt-msg").html("");
	}
});
$(document).on("keydown","#withVerCode",function(){
	if($("#with-verCode-msg").html()!=""){
		$("#with-verCode-msg").html("");
	}
});

$(document).on("keydown","#regMobile",function(){
	if($("#reg-phone-msg").html()!=""){
		$("#reg-phone-msg").html("");
	}
});
$(document).on("keydown","#regAmt",function(){
	if($("#reg-amount-msg").html()!=""){
		$("#reg-amount-msg").html("");
	}
});
$(document).on("keydown","#regVerAmt",function(){
	if($("#reg-verifyamount-msg").html()!=""){
		$("#reg-verifyamount-msg").html("");
	}
});
$(document).on("keydown","#regUname",function(){
	if($("#reg-username-msg").html()!=""){
		$("#reg-username-msg").html("");
	}
});

$(document).on("keydown","#regEmail",function(){
	if($("#reg-email-msg").html()!=""){
		$("#reg-email-msg").html("");
	}
});


$(document).on("keydown","#depMob",function(){
	if($("#dep-mobile-msg").html()!=""){
		$("#dep-mobile-msg").html("");
	}
});


$(document).on("keydown","#regCardNo",function(){
	if($("#reg-cardNo-msg").html()!=""){
		$("#reg-cardNo-msg").html("");
	}
});

$(document).on("keydown","#depCardNo",function(){
	if($("#dep-cardNo-msg").html()!=""){
		$("#dep-cardNo-msg").html("");
	}
});

$(document).on("keydown","#withCardNo",function(){
	if($("#with-cardNo-msg").html()!=""){
		$("#with-cardNo-msg").html("");
	}
});


/**
 * 
 */


/**
 * on keyUp on input fields
 */

$(document).on("keyup","#depAmt,#withAmt,#depVerAmt,#regAmt,#regVerAmt",function(e){
	if(e.which != 37 && e.which != 39  ){
		this.value = this.value.replace(/[^0-9\.]/g,'');
	}
	
});

/*$(document).on("keyup","#withMob,#depMob,#regMobile",function(e){
	if(e.which != 37 && e.which != 39 ){
	this.value = this.value.replace(/[^0-9]/g,'');
	}
});*/

$(document).on("change","#imgUpload",function(e){
    readURL(this);
});


$("#regWalletTab > ul > li").click(function() { 
	  $('ul > li > input').removeClass('checked'); 
	  $(this).children().addClass('checked');
	});

$("#accordian>ul>li").click(function(){
	  $('ul > li > input').removeClass('checked'); 
	  regType = $('.clearFix').children().children().first().prop('checked', true).addClass('checked').attr('regType');
}); 

});

function validateUsername(username,errorDiv) {
	var regex=/^([A-Za-z])([A-Za-z0-9\.]){4,20}$/;
	$("#"+errorDiv).parent().addClass("error-msg");
	
	if (username == "") {
		$("#"+errorDiv).html("Please enter username");
		isValid = false;
		return isValid;
	}else if(!username.match(regex)){
		$("#"+errorDiv).html("Please enter valid username");
		isValid=false;
		return isValid;
	} else if (isUserErrorMessage) {
		$("#"+errorDiv).html($("#"+errorDiv).html());
		isValid = false;
		return isValid;
	} else {
		$("#"+errorDiv).html("");
		$("#"+errorDiv).parent().removeClass("error-msg");
			
		return true;
	}
}


function validateMobileNo(mobile,mobileNumLimit,errorDiv) {
	var regex = /^\d+$/;
	$("#"+errorDiv).parent().addClass("error-msg");
	if (mobile == "") {
		$("#"+errorDiv).html("Please enter mobile number");
		isValid = false;
		return isValid;
	}else if(!mobile.match(regex)){
		$("#"+errorDiv).html("Please enter valid mobile number");
		isValid=false;
		return isValid;
	}else if(mobile.length!=parseInt(mobileNumLimit)){
		$("#"+errorDiv).html("Please enter valid mobile number");
		isValid=false;
		return isValid;
	}else if(isMobileErrorMessage){
		$("#"+errorDiv).html($("#"+errorDiv).html());
		isValid=false;
		return isValid;
	} else {
		$("#"+errorDiv).parent().removeClass("error-msg");
		$("#"+errorDiv).html("");
		return true;
	}
}

function validateCardNo(cardNo,errorDiv) {
	$("#"+errorDiv).parent().addClass("error-msg");
	if (cardNo == "") {
		$("#"+errorDiv).html("Please enter card number");
		isValid = false;
		return isValid;
	} else if(cardNo.length<16){
		isValid = false;
		return isValid;
	}else {
		$("#"+errorDiv).parent().removeClass("error-msg");
		$("#"+errorDiv).html("");
		return true;
	}
}


function validateAmountOrVerCode(amount1,amount2,amount1Div,amount2Div,verifyFor){
	var isValid=true;
	$("#"+amount1Div).html('');
	$("#"+amount2Div).html('');
	var amountArr = amount1.split('.');
	var regex = /^[0-9\.]{1,20}$/;
	if (amount1 == "") {
		$("#"+amount1Div).html("Please enter amount");
		isValid = false;
	}else if (isNaN(amount1)) {
		$("#"+amount1Div).html("Please enter valid amount");
		isValid = false;
	}else if (amountArr.length > 1) {
		if (amountArr[1].length > 2) {
			$("#"+amount1Div).html("Please enter valid amount");
			isValid = false;
		}
	}else if(!amount1.match(regex)){
		$("#"+amount1Div).html("Please enter valid amount");
		isValid = false;
	}
	if(verifyFor=="DEPOSIT"){
		if(amount2 == "")
		{
			$("#"+amount2Div).html("Please enter amount to verify");
			isValid = false;
		}else if(parseFloat(amount1)!=parseFloat(amount2))
		{
			$("#"+amount2Div).html("Please verify the amount correctly");
			isValid= false;
		}
	}else{
		if(amount2 == ""){
			$("#"+amount2Div).html("Please enter verification code");
			isValid = false;
		}		
	}	
	return isValid;
}

function validateEmail(email,errorDiv) {
	var regex = /^([A-Za-z0-9\x27\x2f!#$%&*+=?^_`{|}~-]+(\.[A-Za-z0-9\x27\x2f!#$%&*+=?^_`{|}~-]+)*)@(([a-zA-Z0-9][a-zA-Z0-9-]{0,61}[a-zA-Z0-9]|[a-zA-Z0-9]{1,63})(\.([a-zA-Z0-9][a-zA-Z0-9-]{0,61}[a-zA-Z0-9]|[a-zA-Z0-9]{1,63}))*\.[a-zA-Z0-9]{2,63})$/;
	if (email == "") {
		$("#"+errorDiv).html("please enter email");
		$("#"+errorDiv).parent().addClass("error-msg");
		isValid = false;
		return isValid;
	}else if(!email.match(regex)){
		$("#"+errorDiv).html("please enter valid email");
		$("#"+errorDiv).parent().addClass("error-msg");
		isValid=false;
		return isValid;
	}else {
		$("#"+errorDiv).html("");
		$("#"+errorDiv).parent().removeClass("error-msg");
		return true;
	}
}

function validateAmount(amount1,amount2,errorDiv1,errorDiv2,regType){
	var isValid;
	var regex = /^[0-9\.]{1,20}$/;
	$("#"+errorDiv1).parent().addClass("error-msg");
	$("#"+errorDiv2).parent().addClass("error-msg");
	if(regType == 'ANONYMOUS'){
		if (amount1 == "") {
			$("#"+errorDiv1).html("please enter amount !!");
			isValid = false;
			return isValid;
		}
		if (amount2 == "") {
			$("#"+errorDiv2).html("please enter amount !!");
			isValid = false;
			return isValid;
	   }
	
		if(amount1 != "" && amount2 != "" && parseFloat(amount1) != parseFloat(amount2)){
			   $('#'+errorDiv2).html("please enter same amount!!");
			   isValid=false;
			   return isValid;
    }
  }
	if(amount1 != "" && isNaN(amount1)){
		$("#"+errorDiv1).html("please enter valid amount");
		isValid=false;
	} else if(amount1 != "" && !amount1.match(regex)){
		$("#"+errorDiv1).html("please enter valid amount number!!");
		isValid=false;	
	}else if(amount1 != "" && amount2 != "" && parseFloat(amount1) != parseFloat(amount2)){
	   $('#'+errorDiv2).html("please enter same amount!!");
	   isValid=false;
   }else {
		$("#"+errorDiv1).html("");
		$("#"+errorDiv1).parent().removeClass("error-msg");
		$("#"+errorDiv2).html("");
		$("#"+errorDiv2).parent().removeClass("error-msg");
		isValid=true;
	}
	return isValid;
}



// TO show the Fields according to the regType

function prepareRegForm(regType){
	$("#regCardNo,#depCardNo,#withCardNo").attr("readonly",false);
	if($('#reg').children().hasClass('active')){
		$("#regButtonDiv").css("display","block");
		$("#regBtn").focus();
		if(regType == 'FULL'){
			$("#amountAmt").css("visibility","hidden");
			$("#amountVAmt").css("visibility","hidden");
			$("#username").css("display","block");
			$("#mobile").css("display","block");
			$("#email").css("display","block");
			$("#cardNo").css("display","none");
			$("#idType").css("display","none");
			$("#idNo").css("display","none");
			$("#idUpload").css("display","none");
			$("#regUname").focus();
		}
		if(regType == 'ANONYMOUS'){
			$("#amountAmt").css("visibility","visible");
			$("#amountVAmt").css("visibility","visible");
			$("#username").css("display","none");
			$("#mobile").css("display","none");
			$("#email").css("display","none");
			$("#cardNo").css("display","none");
			$("#idType").css("display","none");
			$("#idNo").css("display","none");
			$("#idUpload").css("display","none");
			$("#regAmt").focus();
		}
		if(regType == 'MOBILE'){
			$("#amountAmt").css("visibility","hidden");
			$("#amountVAmt").css("visibility","hidden");
			$("#username").css("display","none");
			$("#email").css("display","none");
			$("#cardNo").css("display","none");
			$("#idType").css("display","none");
			$("#idNo").css("display","none");
			$("#idUpload").css("display","none");
			$("#regMobile").focus();
		}
		if(regType == 'ALA'){
			$("#cardNo").css("display","block");
			$("#mobile").css("display","block");
			$("#username").css("display","none");
			$("#email").css("display","none");
			$("#idType").css("display","block");
			$("#idNo").css("display","block");
			$("#idUpload").css("display","block");
			$("#regCardNo").focus();
		}
	}else if($('#dep').children().hasClass('active')){
		if(regType == 'ALA'){
			$("#depCardDiv").css("display","block");
			$("#depMobDiv").css("display","none");
			$("#depCardNo").focus();
		}else{
			$("#depCardDiv").css("display","none");
			$("#depMobDiv").css("display","block");
			$("#depMob").focus();
		}
		
		
	}else if($('#withrwl').children().hasClass('active')){
		if(regType == 'ALA'){
			$("#withCardDiv").css("display","block");
			$("#withMobDiv").css("display","none");
			$("#withCardNo").focus();
		}else{
			$("#withCardDiv").css("display","none");
			$("#withMobDiv").css("display","block");
			$("#withMob").focus();
		}
	}
}

function validateRegForm(regType){
	 $("#regPopUpData").html('');
	//var regType = $('input:radio[name=selectOne_lbl]:checked').attr('regType');
	var isValidMail=true;
	var isValidName=true;
	var isValidMob=true;
	var isValidAmt=true;
	var isValidCard=true;
	var email =    $.trim($('#regEmail').val());
	var username = $.trim($('#regUname').val());
	var mobile =   $.trim($('#regMobile').val());
	var amount1 =  $.trim($('#regAmt').val())==""?0:$.trim($('#regAmt').val());
	var amount2 =  $.trim($('#regVerAmt').val())==""?0:$.trim($('#regVerAmt').val());
	var cardNo =    $.trim($('#regCardNo').val());
	if(regType == 'FULL'){
			isValidMail = validateEmail(email,'reg-email-msg');
			isValidName= validateUsername(username,'reg-username-msg');
			isValidMob = validateMobileNo(mobile,mobileLimit,'reg-phone-msg');
			isValidAmt = validateAmount(amount1,amount2,'reg-amount-msg','reg-verifyamount-msg',regType);
	}
	if(regType == 'ANONYMOUS'){
		isValidAmt = validateAmount(amount1,amount2,'reg-amount-msg','reg-verifyamount-msg',regType);
	}
	if(regType == 'MOBILE'){
		isValidMob = validateMobileNo(mobile,mobileLimit,'reg-phone-msg');
		isValidAmt =validateAmount(amount1,amount2,'reg-amount-msg','reg-verifyamount-msg',regType);
	}
	if(regType=='ALA'){
		isValidCard=validateCardNo(cardNo,'reg-cardNo-msg');
		isValidMob = validateMobileNo(mobile,mobileLimit,'reg-phone-msg');
		isValidAmt =validateAmount(amount1,amount2,'reg-amount-msg','reg-verifyamount-msg',regType);
	}
	
	if(isValidMail && isValidName && isValidMob && isValidAmt && isValidCard){
		username=(regType=='ALA')?cardNo:username;
		var refCode=(regType=='ALA')?cardNo:mobile;
		var depositData = {
			"refCode": refCode,
			"depositAmt": amount1,
			//"walletDevName":getSelectedWallet(),
			"walletDevName":walletName,
			};
		 regReqData = JSON.stringify({
			"phone": mobile,
			"email": email,
			//"walletName":getSelectedWallet(),
			"walletName":walletName,
			"username": username,
			"depositData":depositData,
			"regFieldType":regType,
		});
		 var s='';
		 if(regType=='FULL'){
			 s+="<div class='detail-con'><span>User Name :</span><span >"+username+"</span></div>" ;
			 s+="<div class='detail-con'><span>Mobile :</span><span >"+mobile+"</span></div>" ;
			 s+="<div class='detail-con'><span>Email :</span><span >"+email+"</span></div>" ;
		 }else if(regType=='MOBILE'){
			 s+="<div class='detail-con'><span>Mobile :</span><span >"+mobile+"</span></div>" ;
		 }else if(regType=='ALA'){
			 s+="<div class='detail-con'><span>Card No :</span><span >"+cardNo+"</span></div>" ;
			 s+="<div class='detail-con'><span>Mobile :</span><span >"+mobile+"</span></div>" ;
		 }
		 if(amount1!=0){
			 s+="<div class='detail-con'><span>Amount :</span><span >"+amount1+" "+currencySymbol+"</span></div>" ;
		 }
		 $("#regPopUpData").append(s);
		$("#olaRegConfirmBox").css("display","block");
		$("#regConfirm").focus();
	  }
}

//Utility Methods

function setSideLabelFunctionality() {
	$("#accordian>ul>li>a").click(function(){
		//slide up all the link lists
		$("#accordian ul ul").slideUp();
		$("#accordian>ul>li>a").removeClass("active");
		//slide down the link list below the h3 clicked - only if its closed
		if(!$(this).next().is(":visible"))
		{
			$(this).next().slideDown();
			$(this).toggleClass("active");
		}
	});	
}

function setSideMenu(){
	var flag=true;
	$("#accordian>ul>li").each(function(){
		if(flag){
			if ($(this).is(":visible")) {
				$(this).children().addClass("active");
				$(this).trigger("click");
				flag=false;
				return;
			}
		}		
	});
}


//To make the radio buttons 
function createWalletTab(){
	var userActionList = _ajaxCallJson(projectName+'/com/skilrock/lms/web/drawGames/playMgmt/fetchAllowedUserAction.action', '');
	$.inArray(userActionListArray[0],userActionList)!=-1?$("#reg").css("display","block"):$("#reg").css("display","none");
	$.inArray(userActionListArray[1],userActionList)!=-1?$("#dep").css("display","block"):$("#dep").css("display","none");
	$.inArray(userActionListArray[2],userActionList)!=-1?$("#withrwl").css("display","block"):$("#withrwl").css("display","none");
	setSideMenu();

	var url = projectName+"/com/skilrock/ola/commonMethods/action/getWalletDetail.action";
	var resp = _ajaxCallJson(url);
	if(resp.isSuccess){
		mobileLimit = resp.mobilelimit;
		$("#regMobile").attr('maxlength',mobileLimit);
		$("#depMob").attr('maxlength',mobileLimit);
		$("#withMob").attr('maxlength',mobileLimit);
		var s = '';
		s+= '<ul class="clearFix">';
		$.each(resp.walletDetail, function(){    
			s += '<li class="wallet">';
			s+='<input type="radio" id = '+ this.walletDevName +' name="selectOne_lbl" regType= '+ this.registrationType +' displayName= '+ this.walletDisplayName +'/>';
			s+='<label for = '+ this.walletDevName +'> '+this.walletDisplayName+'</label>';
			s+='</li>';
		});
		s+= '</ul>';
		$("#regWalletTab").append(s);
		regType=$('.clearFix').children().children().first().prop('checked', true).addClass('checked').attr('regType');
		walletName=$(".wallet").children().first().attr('id');
		walletDevName=walletName;
		displayName=$(".wallet").children().first().attr('displayName');
		prepareRegForm(regType);
		
	}else{
		alert(resp.responseMsg);
	}
}
/*function getSelectedWallet(){
	var walletName='';
	$(".wallet").each(function(){
		if($(this).children().is(':checked')){
			walletName=$(this).children().attr('id');
		}
	});
	return walletName;
}*/

/*function getWalletDisplayName(){
var displayName='';
	$(".wallet").each(function(){
		if($(this).children().is(':checked')){
			displayName=$(this).children().attr('displayName');
		}
	});
	return displayName;
}*/


function clearDiv(){
	$("#withMob").val('');
	$("#depMob").val('');
	$("#depAmt").val('');
	$("#depVerAmt").val('');
	$("#withAmt").val('');
	$("#withVerCode").val('');
	$("#regUname").val('');
	$("#regMobile").val('');
	$("#regEmail").val('');
	$("#regAmt").val('');
	$("#regVerAmt").val('');
	$("#with-mobile-msg").empty();
	$("#with-amt-msg").empty();
	$("#with-verCode-msg").empty();
	$("#withCardNo").val('');
	$("#depCardNo").val('');
	$("#regCardNo").val('');
	$("#dep-mobile-msg").empty();
	$("#dep-amt-msg").empty();
	$("#dep-ver-amt-msg").empty();
	$("#reg-username-msg").empty();
	$("#reg-phone-msg").empty();
	$("#reg-email-msg").empty();
	$("#reg-amount-msg").empty();
	$("#reg-verifyamount-msg").empty();
	$("#reg-cardNo-msg").empty();
	$("#dep-cardNo-msg").empty();
	$("#with-cardNo-msg").empty();
	$("#regIdType").val(-1);
	$("#regIdNo").val('');
	$("#userImg").css('display','none');
	$("#imgUpload").val('');
}
	
function setAppDataOLA(data) {
	var connSuccess = false;
	var connection = new WebSocket('ws://localhost:8082');
	connection.onopen = function () {
   		connection.send(data);
   		if(connection.readyState == 1){
   			connSuccess = true;
   		}
	};
	connection.onerror = function(){
		alert("Cannot communicate to printing application.\nPlease check your NodeJS server is running and try again.\nTo start NodeJs server either restart your system or\n follow the steps below:\n" +
				"1.) Open My Computer. \n2.) Goto C:/Program Files (x86)/skilrock/dgpcpos (For 64-bit system) and\n" +
				"     C:/Program Files (x86)/skilrock/dgpcpos (For 32-bit system).\n3.) Double click on runNodeInvisible to start NodeJs server.");
	};
	connection.onmessage = function (evt) {
		var printStatus = "";
    	var dataReceived = $.parseJSON(evt.data);
    	if("APP_NOT_FOUND" == dataReceived){
    		printStatus = dataReceived;
    	}else{
    		printStatus = dataReceived.utf8Data;
    	}
        connection.close();
	    if(connSuccess){
	    	checkAndShowTicketOLA(printStatus);
		}
    };
}

function checkAndShowTicketOLA(printStatus){
	if("FAIL" == printStatus){
		alert("Printer not connected!! Check your printer!");
	} else if("APP_NOT_FOUND" == printStatus){
		alert("Printing Application not running!!");
	} else if("INVALIDINPUT" == printStatus){
		alert("Ticket cannot be printed due to \nInternal System Error!!");
	}
}
	
function readURL(input) {
        if (input.files && input.files[0]) {
            var reader = new FileReader();
            
            reader.onload = function (e) {
                $('#userImg').attr('src', e.target.result);
            };
            
            reader.readAsDataURL(input.files[0]);
            $("#userImg").css("display","block");
        }
    }

