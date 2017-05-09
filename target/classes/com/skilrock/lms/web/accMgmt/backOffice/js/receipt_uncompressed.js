
		function _validateForm(){
			var frmVal  = new parent.frames[0].Validator("searchgame");		
		  	frmVal.aV("recieptnumber","alphanumeric","Please Enter Valid Receipt Number","recieptnumber1");	
		  	if(!validateDates()) return false;
		   	return document.error_disp_handler.isValid;
	  	}
	  
	  	function changeReceiptType(orgType) {
	  		// key contans   'party_type' + '-' + 'transaction_type' 
	  		_id.i("down", "");	   	 
	  		var govt = {'TDS':'TDS_RECEIPT', 'VAT':'VAT_RECEIPT', 'GOVT_COMM':'GOOD_CAUSE_RECEIPT'};
	  		
	  		var player = {'PWT_PLR':'PAYMENT'};
	  		var AGT_RET = {'RECEIPT':'RECEIPT', 'INVOICE':'INVOICE', 'CR_NOTE':'CR_NOTE', 'DR_NOTE':'DR_NOTE', 'DLCHALLAN':'DLCHALLAN', 'DSRCHALLAN':'Sales Return Note'};
	  		var copyArrMap;
	  		var recTypeObj = _id.o("rec_type");
	  		recTypeObj.options.length=1;
	  		//alert();
	  		
	  		if(orgType == "AGENT" || orgType == "RETAILER") {
	  			copyArrMap = AGT_RET;
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
	  	function retURL(url, targetDiv, orgType) {
	  		_id.i('retlist', "");
			_id.i("retDiv", "");
			_id.i("down", "");	   			  		
	  		if(orgType=='AGENT' || orgType=='RETAILER') {
				var ajaxCall = _ajaxCall(url);
				//alert(ajaxCall.data);
				_id.i(targetDiv,"&nbsp;&nbsp;"+ajaxCall.data);
				_id.i("agentDiv",tierMap['AGENT']+" Name:");		
			 }
			 else {
			 	 _id.i(targetDiv, "");
			 	 _id.i("agentDiv", "");				 	 	 
			 }			 
	  	}
	  	
	  	function getRetailerList(url, targetDiv, agentOrgId) {
	  		var changedUrl = url+'?agentOrgId='+agentOrgId;
	  		var orgType = _id.o('user_type').value;
	  		//alert(changedUrl +"    userType = "+orgType);
	  		if(orgType!='RETAILER') {
	  			_id.i(targetDiv, "");
			 	 _id.i("retDiv", "");	 
			 	 return false;
	  		}
	  		//alert("retailer ");
	  		if(agentOrgId!='') {
				var ajaxCall = _ajaxCall(changedUrl);
				//alert(ajaxCall.data);
				_id.i(targetDiv,"&nbsp;&nbsp;"+ajaxCall.data);
				_id.i("retDiv",tierMap['RETAILER']+" Name:");		
			 }
			 else {
			 	 _id.i(targetDiv, "");
			 	 _id.i("retDiv", "");	 	 
			 }
	  	}
	  	
	  	function getRetailerListForAgent(url, targetDiv, orgType) {
	  		  //alert();	
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
  	
  		function changeReceiptTypeForAgent(orgType) {
	  		// key contans   'party_type' + '-' + 'transaction_type' 
	  		_id.i("down", "");	   	 
	  		var govt = {'TDS':'TDS_RECEIPT', 'GOVT_COMM':'GOOD_CAUSE_RECEIPT'};
	  		
	  		var player = {'PWT_PLR':'PAYMENT'};
	  		var AGT_RET = {'RECEIPT':'RECEIPT', 'INVOICE':'INVOICE', 'CR_NOTE':'CR_NOTE', 'DR_NOTE':'DR_NOTE', 'DLCHALLAN':'DLCHALLAN', 'DSRCHALLAN':'Sales Return Note'};
	  		var copyArrMap;
	  		var recTypeObj = _id.o("rec_type");
	  		recTypeObj.options.length=1;
	  		//alert();
	  		
	  		if(orgType == "RETAILER") {
	  			copyArrMap = AGT_RET;
	  		} else if(orgType == "GOVT") {
	  			copyArrMap = govt;
	  		}
	  		 else if(orgType == "PLAYER") {
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

parent.frames[0].Version['$Source: /rep/LMS_Mgmt/WebRoot/com/skilrock/lms/web/accMgmt/backOffice/js/receipt_uncompressed.js,v $'] = '$Id: receipt_uncompressed.js,v 1.2 2016/10/31 09:46:22 neeraj Exp $';