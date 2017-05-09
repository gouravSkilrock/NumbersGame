function getRetailerList() {
	_id.non('chq_dtl_div');
	_id.non("x");
	var text =  _ajaxCall("getRetailerNameListNDate.action?retType=ALL");
	var firstArr = text.data.split(":");
	var retNameArray = firstArr[0].split(",");
	var retObj = _id.o("orgName");
	for(var i =0; i<retNameArray.length; i++) {		
		var val = retNameArray[i].split("=")[1];
		var opt = new Option(val, val);
		retObj.options[i+1] = opt;		
	}
	var dateArray = firstArr[1].split(",");
	//alert(dateArray[0]+"=="+dateArray[1]);
	_id.o("sd").value = dateArray[0];
	_id.o("ed").value = dateArray[1];
	_id.o("chequeDate").value = dateArray[1];
	var butText = '<input type="button"  style=" width:19px; height: 19px; background: url('+image+'); top left; border:0 ; " onclick="displayCalendar(document.getElementById(\'chequeDate\'), \'mm/dd/yyyy\', this, \''+dateArray[1]+'\', \''+dateArray[0]+'\', \''+dateArray[1]+'\')" />';
	//alert(butText);
	_id.non("im");
	_id.i("chqSub", butText);
	_id.blk("x");
}

function validateChequeEntries(){
	var flag = true;	
	var frmVal  = new parent.frames[0].Validator("na");
		  // validate the retailer organization  Entries
		  		  
		  frmVal.aV("orgName","dontselect=0","Please Select Organization Name","orgNameError");
		  
		  frmVal.aV("chequeNumber","req","Please Enter Valid Cheque Number","chequeNumberError");
		  frmVal.aV("chequeNumber","numeric","Please Enter Only Number From 0-9","chequeNumberError");
		  
		  frmVal.aV("issuePartyname","req","Please Enter Issue Party Name.","issuePartynameError");
		  frmVal.aV("issuePartyname","regexp=[^A-Za-z\\s]","Please Enter Valid Issue Party Name.","issuePartynameError");
		  
		  frmVal.aV("bankName","req","Please Enter Bank Name.","bankNameError");
		  frmVal.aV("bankName","regexp=[^A-Za-z\\s&.]","Please Enter Valid Bank Name.","bankNameError");		  
		  
		  frmVal.aV("chequeAmount","req","Please Enter Cheque Amount","chequeAmountError");
		  frmVal.aV("chequeAmount","decimal","Please Enter Correct Value For Cheque Amount","chequeAmountError");
		  
		  frmVal.aV("verifychequeAmount","req","Please Enter Verify Cheque Amount","verifyAmntError");
		  frmVal.aV("verifychequeAmount","decimal","Please Enter Correct Value For Verify Cheque Amount","verifyAmntError");
		  
		  flag = document.error_disp_handler.isValid;		  			
		
		  if(flag && _id.v('verifychequeAmount')!=_id.v('chequeAmount')) { 
		  	_id.i('verifyAmntError', "Please Verify Cheque Amount fields", "e");
		  	flag = false;
		  	_id.o('verifychequeAmount').focus();
		  } else if(flag) {
			_id.i('verifyAmntError', "");
		  }
		 	
		 
		  if(flag) {
		 	var isTrue = convertDigitToWord(_id.v('chequeAmount'));
		 	if(isTrue) {
		 		if(isChqDuplicate())	return false;
		 		else	_id.i("table_error_div", "");
		 		//add cheque details into table
		 		addChqDetCrt(false);
		 		// reset the form values
		 		_id.o('orgName').disabled = true;
		 		_id.o('orgType').disabled = true;
				_id.o('chequeNumber').value = "";	
				_id.o('issuePartyname').value = "";
				_id.o('bankName').value = "";	
				_id.o('chequeAmount').value = "";
				_id.o('verifychequeAmount').value = "";
				_id.o('chequeDate').value = _id.v('chq_end_date');					 		
		 	}
		 	else {
		 		flag = false;
		 		_id.o('chequeAmount').focus();
		 	}
		 }	
		return false;
		
}

function isChqDuplicate() {
	for(var i in tabArr){
		var trArr = tabArr[i].split(':');
		//alert((trArr.length>1 && trArr[0]==_id.v('chequeNumber') && trArr[2].split(',')[1]==_id.v('bankName')));
		if(trArr.length>1 && trArr[0]==_id.v('chequeNumber') && trArr[2].split(',')[1]==" "+_id.v('bankName')){			
			_id.i("table_error_div", "You Are Trying to Enter Duplicate Cheque Entries.", "e");
			_id.blk("table_error_div");
			return true;
		}
	}
	return false;
}

var _gblFlag = true;
var tabArr = new Array();

function addChqDetCrt(isRem){
	var count=1;
	if(!isRem)
		tabArr[tabArr.length]=_id.v('chequeNumber')+":"+_id.v('orgName')+":"+_id.v('issuePartyname')+", "+_id.v('bankName')+":"+_id.v('chequeAmount')+":"+_id.v('chequeDate')+':<a href="#" onclick="remRowFrmTab(\''+tabArr.length+'\')">remove</a>';
	var tabDet = '<table width="100%" border="1" cellpadding="3" cellspacing="0" bordercolor="#CCCCCC" align="center"><tr><th align="center">S.No</th><th align="center">Cheque Number</th><th align="center">Organization Name</th><th align="center">Issuing Party & Bank Name</th><th align="center">Cheque Amount</th><th align="center">Cheque Date</th><th align="center">Remove</th></tr>';
	var trDet="";
	for(var i in tabArr){
		var trArr = tabArr[i].split(':');
		if(trArr.length>1){
			trDet=trDet+'<tr><td align="right">'+count+'</td><td>'+trArr[0]+'</td><td>'+trArr[1]+'</td><td>'+trArr[2]+'</td><td>'+trArr[3]+'</td><td align="right">'+trArr[4]+'</td><td>'+trArr[5]+'</td></tr>';
			count++;
		}
	}
	tabDet = tabDet+trDet+'</table>';
	_id.i('chq_detail_tbl',tabDet);
	_id.blk('cart');
	_id.blk('chq_dtl_div');
	if(count<2)	_id.non('chq_dtl_div');
}

function remRowFrmTab(id){
	tabArr[id]="";
	addChqDetCrt(true);
}

function createHidenFields(id) {
	if(!_subValid(id))
		return false;
	var _nA=new Array('<input type="hidden" name="','Hidden" value="');
	var hiddenText=_nA[0]+'orgType'+_nA[1]+_id.v('orgType')+'" />'+_nA[0]+'orgName'+_nA[1]+_id.v('orgName')+'" />';
	for(var i in tabArr){
		var trArr = tabArr[i].split(':');
		if(trArr.length>1){			
			hiddenText = hiddenText +_nA[0]+'chequeNumber'+_nA[1]+trArr[0]+'" />'
				+_nA[0]+'issuePartyName'+_nA[1]+trArr[2].split(',')[0]+'" />'
				+_nA[0]+'bankName'+_nA[1]+trArr[2].split(',')[1]+'" />'
				+_nA[0]+'chequeAmount'+_nA[1]+trArr[3]+'" />'
				//+_nA[0]+'verifyChequeAmount'+_nA[1]+trArr[3]+'" />'
				+_nA[0]+'chequeDate'+_nA[1]+trArr[4]+'" />';
		}
	}
	_id.i('hiddenValues', hiddenText);
	//alert(hiddenText);
	return true;
}

parent.frames[0].Version['$Source: /rep/LMS_Mgmt/WebRoot/com/skilrock/lms/web/accMgmt/backOffice/js/cashChequePayment_uncompressed.js,v $'] = '$Id: cashChequePayment_uncompressed.js,v 1.1 2010/04/01 04:23:07 gaurav Exp $';
