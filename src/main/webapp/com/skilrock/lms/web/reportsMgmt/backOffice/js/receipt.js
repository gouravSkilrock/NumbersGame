
		function _validateForm(){
			var frmVal  = new parent.frames[0].Validator("searchgame");		
		  	frmVal.aV("recieptnumber","alphanumeric","Please Enter Valid Receipt Number","recieptnumber1");	
		  	if(!validateDates()) return false;
		   	return document.error_disp_handler.isValid;
	  	}
	  
	  	function changeReceiptType(orgType,isDG,isSE,isOLA,isCS,isSLE) {
	  		// key contans   'party_type' + '-' + 'transaction_type' 
	  		_id.i("down", "");	   	 
	  	//	alert(orgType);

	  		var homeDefault={'CR_NOTE':'CR_NOTE', 'DR_NOTE':'DR_NOTE','RECEIPT':'RECEIPT','DLNOTE':'DLNOTE'};
	  		var ifDG={'DG_INVOICE':'DG_INVOICE','DG_RECEIPT':'DG_RECEIPT'};
	  		var ifSC={'INVOICE':'INVOICE','DLCHALLAN':'DLCHALLAN', 'DSRCHALLAN':'Sales Return Note'};
	  		var ifOLA={'OLA_INVOICE':'OLA_INVOICE','OLA_RECEIPT':'OLA_RECEIPT'};
	  		var ifCS={'CS_INVOICE':'CS_INVOICE'};
	  		var ifSLE={'SLE_INVOICE':'SLE_INVOICE','SLE_RECEIPT':'SLE_RECEIPT'};
	  		
	  		var govt = {'TDS':'TDS_RECEIPT', 'VAT':'VAT_RECEIPT', 'GOVT_COMM':'GOOD_CAUSE_RECEIPT'};
	  		var player = {'PWT_PLR':'PAYMENT'};
	//  	var AGT_RET = {'RECEIPT':'RECEIPT', 'INVOICE':'INVOICE','DG_INVOICE':'DG_INVOICE','DG_RECEIPT':'DG_RECEIPT','CS_INVOICE':'CS_INVOICE', 'CR_NOTE':'CR_NOTE', 'DR_NOTE':'DR_NOTE', 'DLCHALLAN':'DLCHALLAN', 'DSRCHALLAN':'Sales Return Note','DLNOTE':'DLNOTE','OLA_INVOICE':'OLA_INVOICE','OLA_RECEIPT':'OLA_RECEIPT'};
	  		var copyArrMap;
	  		var recTypeObj = _id.o("rec_type");
	  		recTypeObj.options.length=1;
	  		//alert();
	  		
	  		if(orgType == "AGENT" || orgType == "RETAILER") {
	  			copyArrMap = homeDefault;
	  			if(isDG=="YES"){
	  				copyArrMap=merge_options(copyArrMap,ifDG);}
	  			if(isSE=="YES"){
	  				copyArrMap=merge_options(copyArrMap,ifSC);}
	  			if(isOLA=="YES"){
	  				copyArrMap=merge_options(copyArrMap,ifOLA);}
	  			if(isCS=="YES"){
	  				copyArrMap=merge_options(copyArrMap,ifCS);}
	  			if(isSLE=="YES"){
	  				copyArrMap=merge_options(copyArrMap,ifSLE);}
	  		} else if(orgType == "GOVT") {
	  			copyArrMap = govt;
	  		} else if(orgType == "PLAYER") {
	  			copyArrMap = player;
	  		}
	  		// copy array values into receipt type select box
	  		var count = 1;
	  		for (key in copyArrMap) {
				var val = copyArrMap[key];		
				//alert(val+"=="+key);		
				var opt = new Option(val, key);
				recTypeObj.options[count] = opt;
				count = count +1;
			} 	
			//recTypeObj.focus();	
	  	}

	  	//get the agent list from database
	  	var agtObj=null;
	  	var resData=null;
	  	var retObj=null;
	  	function retURL(url, targetDiv, orgType) {
	  	   			  		
	  		if(orgType=='AGENT' || orgType=='RETAILER') {
				var _resp = _ajaxCall(url);
				_respData = _resp.data;
				resData = _respData.split(":");
				retObj=_id.o("retOrgId");
				agtObj = _id.o("agentOrgId");
				agtObj.options.length = 1;
				showAgtOrg();
				retObj.innerHTML ="";
				retObj.innerHTML="<option value='-1'>--Please Select--</option>";
				//alert(ajaxCall.data);
				//_id.i(targetDiv,"&nbsp;&nbsp;"+ajaxCall.data);
				//_id.i("agentDiv",tierMap['AGENT']+" Name:");		
			 }
			 else {
			 	 _id.i(targetDiv, "");
			 	 _id.i("agentDiv", "");				 	 	 
			 }			 
	  	}
		var resRetData=null;
	  	function getRetailerList(url, targetDiv, agentOrgId) {
	  		var changedUrl = url+'?agentOrgId='+agentOrgId;
	  		var orgType = _id.o('user_type').value;
	  		//alert(changedUrl +"    userType = "+orgType);
	  		if(orgType!='RETAILER') {
	  			//_id.i(targetDiv, "");
	  			 document.getElementById("retlistDiv").style.display="none";	
			 	 
			 	 return false;
	  		}
	  		//alert("retailer ");
	  		if(agentOrgId!='') {
				var _resp = _ajaxCall(changedUrl);
				_respData = _resp.data;
				resRetData = _respData.split(":");
				retObj = _id.o("retOrgId");
				retObj.options.length = 1;
				showRetOrg();
			 }
			 else {
				 document.getElementById("retlistDiv").style.display="none";	
			 	
			 }
	  	}
	  	
	  	function getRetailerListForAgent(url, targetDiv, orgType) {
	  		// alert();	
	  		if(orgType=='RETAILER') {
				var ajaxCall = _ajaxCall(url);
				//alert(ajaxCall.data);
				_id.i(targetDiv,"&nbsp;&nbsp;"+ajaxCall.data);
				_id.i("retDiv",tierMap['RETAILER']+" Name:");		
			 }
			 else {
			 	 _id.i(targetDiv, "");
			 	 _id.i("retDiv", "");	 	 
			 }
	  	}
  	
  		function changeReceiptTypeForAgent(orgType,isDG,isSC,isOLA,isCS) {
  		//alert(orgType);
	  		// key contans   'party_type' + '-' + 'transaction_type' 
	  		_id.i("down", "");	   	 
	  		
	  		var homeDefault={'CR_NOTE':'CR_NOTE', 'DR_NOTE':'DR_NOTE','RECEIPT':'RECEIPT','DLNOTE':'DLNOTE'};
	  		var ifDG={'DG_INVOICE':'DG_INVOICE','DG_RECEIPT':'DG_RECEIPT'};
	  		var ifSC={'INVOICE':'INVOICE','DLCHALLAN':'DLCHALLAN', 'DSRCHALLAN':'Sales Return Note'};
	  		var ifOLA={'OLA_INVOICE':'OLA_INVOICE','OLA_RECEIPT':'OLA_RECEIPT'};
	  		var ifCS={'CS_INVOICE':'CS_INVOICE'};
	  		var govt = {'TDS':'TDS_RECEIPT', 'GOVT_COMM':'GOOD_CAUSE_RECEIPT'};
	  		var player = {'PWT_PLR':'PAYMENT'};
	  		//var AGT_RET = {'RECEIPT':'RECEIPT', 'INVOICE':'INVOICE','DG_INVOICE':'DG_INVOICE','DG_RECEIPT':'DG_RECEIPT','CS_INVOICE':'CS_INVOICE', 'CR_NOTE':'CR_NOTE', 'DR_NOTE':'DR_NOTE', 'DLCHALLAN':'DLCHALLAN', 'DSRCHALLAN':'Sales Return Note','DLNOTE':'DLNOTE','OLA_INVOICE':'OLA_INVOICE','OLA_RECEIPT':'OLA_RECEIPT'};
	  		/*if(isSE == 'YES'){
	  			var AGT_RET = {'RECEIPT':'RECEIPT','INVOICE':'INVOICE', 'CR_NOTE':'CR_NOTE', 'DR_NOTE':'DR_NOTE', 'DLCHALLAN':'DLCHALLAN', 'DSRCHALLAN':'Sales Return Note'};
	  		}
	  		if(isDG == 'YES'){
	  			var AGT_RET = {'RECEIPT':'RECEIPT','INVOICE':'INVOICE','DG_INVOICE':'DG_INVOICE','DG_RECEIPT':'DG_RECEIPT', 'CR_NOTE':'CR_NOTE', 'DR_NOTE':'DR_NOTE', 'DLCHALLAN':'DLCHALLAN', 'DSRCHALLAN':'Sales Return Note'};
	  		}*/
	  		var copyArrMap;
	  		var recTypeObj = _id.o("rec_type");
	  		recTypeObj.options.length=1;
	  		
	  		
	  		if(orgType == "RETAILER") {
	  			copyArrMap = homeDefault;
	  			if(isDG=="YES"){
	  				copyArrMap=merge_options(copyArrMap,ifDG);}
	  			if(isSC=="YES"){
	  				copyArrMap=merge_options(copyArrMap,ifSC);}
	  			if(isOLA=="YES"){
	  				copyArrMap=merge_options(copyArrMap,ifOLA);}
	  			if(isCS=="YES"){
	  				copyArrMap=merge_options(copyArrMap,ifCS);}
	  		} else if(orgType == "GOVT") {
	  			copyArrMap = govt;
	  		}
	  		 else if(orgType == "PLAYER") {
	  			copyArrMap = player;
	  			//alert("inside plr = "+orgType);
	  		}
	  		// copy array values into receipt type select box
	  		var count = 1;
	  		for (key in copyArrMap) {
				var val = copyArrMap[key];		
				//alert(val+"=="+key);		
				var opt = new Option(val, key);
				recTypeObj.options[count] = opt;
				count = count +1;
			} 	
			//recTypeObj.focus();	
	  	}
  		
  		function merge_options(obj1,obj2){
  		    var obj3 = {};
  		    for (var attrname in obj1) {
  		    	obj3[attrname] = obj1[attrname]; }
  		    for (var attrname in obj2) { 
  		    	obj3[attrname] = obj2[attrname]; }
  		    return obj3;
  		}
  		
		function validateDates() {
			var isInValid = false;
			var startDate = _id.v("st");
			var endDate =  _id.v("ed");    
			if (startDate == "" || endDate == "") {
				isInValid = true;
				_id.i("dates", "Please Enter all the dates", "e");       
			}else {
				if (endDate < startDate) {
					isInValid = true;
					_id.i("dates", "End date should be greater then or equals to start date", "e"); 
				}
			}
			if (isInValid) {	
				return false;
			}
			_id.i("dates", "");
			return true;
		
		}
		function getData(value){
			
			var orgTypeVal = document.getElementById("user_type").value;
				if(orgTypeVal=="RETAILER"){
					getRetailerList('bo_rep_searchReceipt_FetchRetailer.action','retlist',value);
				}
			var agentOrgId=document.getElementById("agentOrgId").value;
				if(agentOrgId.trim()==""){
					retObj.innerHTML ="";
					retObj.innerHTML="<option value='-1'>--Please Select--</option>";
				}





			}	
function showAgtOrg(seltype){
			
			//alert(seltype);
			var orgTypeVal = document.getElementById("user_type").value;
		if(orgTypeVal!=-1){	
			
				agtObj.innerHTML ="";
				agtObj.innerHTML="<option value=''>--Please Select--</option>";
				for (var i = 0; i < resData.length ; i=i+1) {
					var retNameArray = resData[i].split("|");
					if(retNameArray[0] != ''){
					var opt1 = new Option(retNameArray[0].toUpperCase(), retNameArray[1]);
					agtObj.options[i + 1] = opt1;
					}
				
				}
				
			}
		document.getElementById("agentlistDiv").style.display="block";	
		}

function showRetOrg(seltype){
	
	//alert(seltype);
	var orgTypeVal = document.getElementById("user_type").value;
 if(orgTypeVal=='RETAILER'){
	
		retObj.innerHTML ="";
		retObj.innerHTML="<option value='-1'>--Please Select--</option>";
		for (var i = 0; i < resRetData.length ; i++) {
			var val = resRetData[i].split("|");
				if(val[0] != ''){
				var opt = new Option(val[0], val[1]);
				retObj.options[i + 1] = opt;
				}
			}
		
	}
 document.getElementById("retlistDiv").style.display="block";	 
	
}


function voucherValidateForm(){
	var frmVal  = new parent.frames[0].Validator("searchgame");		
  	frmVal.aV("recieptnumber","alphanumeric","Please Enter Valid Receipt Number","recieptnumber1");	
  	if(!voucherValidateDates()) return false;
   	return document.error_disp_handler.isValid;
	}
	
	function voucherValidateDates() {
	var isInValid = false;
	var VStartDate = _id.v("vst");
	var VEndDate =  _id.v("ved");    
	var TStartDate = _id.v("tst");
	var TEndDate =  _id.v("ted");  
	var ReceiptID =  _id.v("recieptnumber");
	if (ReceiptID == "" && (VStartDate == "" || VEndDate == "") && (TStartDate == "" || TEndDate == "")) {
		isInValid = true;
		_id.i("dates", "Please Enter all the dates", "e");       
	}else {
		if ((VEndDate < VStartDate) || (TEndDate < TStartDate)) {
			isInValid = true;
			_id.i("dates", "End date should be greater then or equals to start date", "e"); 
		}
	}
	if (isInValid) {	
		return false;
	}
	_id.i("dates", "");
	return true;

}
			
			
			
			
		
parent.frames[0].Version['$Source: /rep/LMS_Mgmt/WebRoot/com/skilrock/lms/web/reportsMgmt/backOffice/js/receipt.js,v $'] = '$Id: receipt.js,v 1.3 2016/10/31 09:46:26 neeraj Exp $';