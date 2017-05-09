var i18nObj = parent.frames[0].i18Obj;

function _subTrans(){
		if(_validator()){
		_id.o('subTrans').style.display = 'none';
		_id.o('loading').innerHTML = i18nObj.prop('msg.loading')+".....<img src=\"" + path + "/LMSImages/images/loadingAjax.gif\"/>";
			return ( verifyNSave() && _subValid('subTrans'));
		} else {
			return false;	
		}
		
	}
	
	function _validator(){
		var isValid = false;
		var ticketNbr=document.getElementById('ticketNbr');
		if(ticketNbr == null){
			_id.o('tktNbrErrMsg').innerHTML = "<font color='red'>"+i18nObj.prop('error.enter.valid.tkt.nbr')+"</font>";
			_id.o('ticketNbr').focus();
			return false;
		}
		 
		if(ticketNbr.value=="" || ticketNbr.value=="0000000000000000" ){
			_id.o('tktNbrErrMsg').innerHTML = "<font color='red'>"+i18nObj.prop('error.enter.valid.tkt.nbr')+"</font>";
			ticketNbr.focus();
			return false;
		}
		if(ticketNbr.value.length < 18 || ticketNbr.value.length > 20 ){
			_id.o('tktNbrErrMsg').innerHTML = "<font color='red'>"+i18nObj.prop('error.enter.valid.tkt.nbr')+"</font>";
			ticketNbr.focus();
			return false;
		}else{
			isValid = true;
		}
 
		/*var nums = "0123456789";
		var checkNbr=ticketNbr.value;
		var isValid=true;
		for (i=0;i<=checkNbr.length;i++){
			ch = checkNbr.charAt(i);
			for(j=0;j<nums.length;j++)
				if(j==nums.charAt(j))
					break;
			
			if(j==nums.length){
				isValid = false;
				break;
			}
 		}*/
 		var str = (ticketNbr.value).toString();
		var patt1 = /^[0-9]+$/;	
		if(!(patt1.test(str))){
			isValid = false;
		}
 
 		if(!isValid){
			_id.o('tktNbrErrMsg').innerHTML = "<font color='red'>"+i18nObj.prop('error.enter.valid.tkt.nbr')+"</font>";
			ticketNbr.focus();
			return false;
 		}
		return true;
	}
	
	function _validateVerificationCode(){
		var isValid = false;
		var verificationCode=document.getElementById('verificationCode');
		_id.o('verErrorMsg').innerHTML ="";
		var str = (verificationCode.value).toString();
		var patt1 = /^[0-9]+$/;	
		if(!(patt1.test(str))){
			_id.o('verErrorMsg').innerHTML = "<font color='red'>"+i18nObj.prop('error.enter.valid.tkt.nbr')+"</font>";
			ticketNbr.focus();
			return false;
		}
		return true;
	}

	function _validatorMerchantPwtTicket(){
		var isValid = false;
		var ticketNbr=document.getElementById('ticketNumber');
		_id.o('down').innerHTML="";
		_id.o('tktNbrErrMsg').innerHTML ="";
		if(ticketNbr == null || ticketNbr == undefined){
			_id.o('tktNbrErrMsg').innerHTML = "<font color='red'>"+i18nObj.prop('error.enter.valid.tkt.nbr')+"</font>";
			_id.o('ticketNumber').focus();
			return false;
		}
		 
		if(ticketNbr.value=="" || ticketNbr.value=="0000000000000000" ){
			_id.o('tktNbrErrMsg').innerHTML = "<font color='red'>"+i18nObj.prop('error.enter.valid.tkt.nbr')+"</font>";
			ticketNbr.focus();
			return false;
		}
		if(ticketNbr.value.length != 15 && ticketNbr.value.length != 18 ){
			_id.o('tktNbrErrMsg').innerHTML = "<font color='red'>"+i18nObj.prop('error.enter.valid.tkt.nbr')+"</font>";
			ticketNbr.focus();
			return false;
		}else{
			var str = (ticketNbr.value).toString();
			var patt1 = /^[0-9]+$/;	
			if(!(patt1.test(str))){
				_id.o('tktNbrErrMsg').innerHTML = "<font color='red'>"+i18nObj.prop('error.enter.valid.tkt.nbr')+"</font>";
				ticketNbr.focus();
				return false;
			}
			return true;
		}
		return true;
		
	}
	
	function validateTkt() {
		var keyCode = (window.event) ? event.keyCode : e.keyCode;
		var tktNbrLen = '';
		if ((keyCode >= 48 && keyCode < 58) || keyCode == 45 || keyCode == 27 || keyCode == 13 || keyCode == 40 || keyCode == 38 || keyCode == 46 || keyCode == 8 || (keyCode >= 96 && keyCode < 106 || keyCode == 9)) {
			tktNbrLen = _id.o("ticketNbr").value.length;
		} else {
			return false;
		}
		if (tktNbrLen == 16) {
			if(isNaN(_id.o("ticketNbr").value)) {
				_id.o("ticketNbr").value = "";
				_id.o('tktNbrErrMsg').innerHTML = "<font color='red'>"+i18nObj.prop('error.enter.valid.tkt.nbr')+"</font>";
				return false;
			} else {
				_id.o('subTrans').click();
			}
		}
		return true;
	}
	
	function moveFocusOnSubmit(value){
			if(value.length == 18){
				_id.o('search').focus();
			}
			return true;
		}


parent.frames[0].Version['$Source: /rep/LMS_Mgmt/WebRoot/com/skilrock/lms/web/drawGames/pwtMgmt/js/pwtTktValidation.js,v $'] = '$Id: pwtTktValidation.js,v 1.3 2016/10/31 09:47:03 neeraj Exp $';