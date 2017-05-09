
	var mainDiv;
	function retrieveURL(url,div) {
	  	//alert(projectName);
		mainDiv=div;
		url = projectName+"/"+url;
		//alert(url);
		var _resp  = _ajaxCall(url);
		//alert( _resp.data);
		if(_resp.result){
		   _id.i(mainDiv, _resp.data);
		} else {
		     alert("Problem: ");
		}
	}
	
	function fetchCityList() {
			var country = _id.o('plrCountry').value;
			var state = _id.o('plrState').value;
	 		_ajaxCall(projectName+"/com/skilrock/lms/web/userMgmt/getCity.action?country=" + country + "&state=" + state,'charactersCity');
	}


	
	function verPlrDetails(url, div){		
		
		 // validate the retailer organization  Entries
		  var frmVal  = new parent.frames[0].Validator("playerPWT");
		  
		  frmVal.aV("firstName","req","Please Enter First Name","fn_err");
		  frmVal.aV("firstName","alpha","Please Enter Valid Name (Special Characters and Numbers not Allowed)","fn_err");
		  
		  frmVal.aV("lastName","req","Please Enter Last Name","ln_err");
		  frmVal.aV("lastName","alpha","Please Enter Valid Name (Special Characters and Numbers not Allowed)","ln_err");
		
		  frmVal.aV("idType","dontselect=0","Please Select ID Type","it_err");
		  
		
		  frmVal.aV("idNumber","req","Please Enter Id Number","in_err");
		  frmVal.aV("idNumber","regexp=[^A-Za-z0-9\\s/,-]","Please Enter Valid Id No.","in_err");
		 
		  var valid = document.error_disp_handler.isValid; 
		  //alert(document.error_disp_handler.isValid+"   "+valid);	  
		  
		  if(valid) {
		  	url = url+"?firstName="+_id.v('firstName')+"&lastName="+_id.v('lastName')+"&idType="+
		  		_id.v('idType')+"&idNumber="+_id.v('idNumber');
		  
		  	var _resp =  _ajaxCall(url);
		  	if(_resp.result){
				var _respData=_resp.data;
				//alert(_respData)
				//alert(_id.o(div).disabled);
				
				_id.o(div).innerHTML = _respData;
				_id.blk(div);
				_id.vis(div);
				//alert(_id.o(div).innerHTML);
			  	_id.blk('subAny');
			}
			else {
			  alert("Problem In Fetching Player Details. Please Retry !!");
			}
		  }
		   
	
		    
		  
	}
	
	

	function validatePlrDetails(){
		
		var radButton = _id.n('playerType');
		for(var a=0; a<radButton.length; a++){
			//alert("radbutton = "+radButton[a].value);
			//alert("   == rad value == "+radButton[a].value=='anonymous');
			//alert(" ==checked = "+radButton[a].checked); 	
		 	if(radButton[a].value=='anonymous' && radButton[a].checked){
		 		//alert("inside if Anynomous");
		 		return true;
		 	}
		}
		
		var isRegistered=document.getElementById('isPlrRegistered').value;
		if(isRegistered=='YES')
		   return true;
		
		 var frmVal  = new parent.frames[0].Validator("playerPWT");
		 
		 
		 // validate the retailer organization  Entries
		 // alert("1");
		  frmVal.aV("firstName","req","Please Enter First Name","fn_err");
		  frmVal.aV("firstName","alpha","Please Enter Valid Name (Special Characters and Numbers not Allowed)","fn_err");
		 // alert("2");
		  frmVal.aV("lastName","req","Please Enter Last Name","ln_err");
		  frmVal.aV("lastName","alpha","Please Enter Valid Name (Special Characters and Numbers not Allowed)","ln_err");
		 // alert("3");
		  frmVal.aV("idType","dontselect=0","Please Select ID Type","it_err");
		  //alert("4");
		  frmVal.aV("idNumber","req","Please Enter Id Number","in_err");
		  frmVal.aV("idNumber","regexp=[^A-Za-z0-9\\s/,-]","Please Enter Valid Id No.","in_err");
		 // alert("5");
		 
		  // when plr details are not found in database
		  var eObj = document.getElementById('emailId');
		  var pObj = document.getElementById('phone');
		
		  //if((eObj!=null && pObj!=null)){
		 // alert("if");  
		      frmVal.aV("emailId","req","Please Enter Email Address","ei_err");
			  frmVal.aV("emailId","email","Please Enter Correct Email Address","ei_err");
			  		
		  	  frmVal.aV("phone","req","Please Enter Phone Number","ph_err");
			  frmVal.aV("phone","numeric","Please Enter Correct Value For Phone Number","ph_err");
			  
		  	  frmVal.aV("plrAddr1","req","Please Enter Address Line1","pa1_err");
		  	  frmVal.aV("plrAddr1","regexp=[^A-Za-z0-9\\s/,-]","Please Enter Valid Address","pa1_err");
		  
		  	  frmVal.aV("plrAddr2","regexp=[^A-Za-z0-9\\s/,-]","Please Enter Valid Address","pa2_err");		  	  
		  	
		      frmVal.aV("plrCountry","dontselect=0","Please Select Country","pc_err");
			  frmVal.aV("state","dontselect=0","Please Select State","ps_err");
			  
			  frmVal.aV("plrPin","req","Please Enter Pin","pp_err");
			  frmVal.aV("plrPin","numeric","Please Enter Correct Value For Pin","pp_err");
			
			
			  
			  // validate the player Bank Details  Entries
			  var bnkNo = document.getElementById('bankAccNbr').value;
			  var bankName = document.getElementById('bankName').value;
			  var bankBranch = document.getElementById('bankBranch').value;
			  var locationCity = document.getElementById('locationCity').value;
			  bnkNo = bnkNo.replace(" ", "")+bankName+bankBranch+locationCity;
			  
			 //alert(bnkNo);
			  if(bnkNo!=""){
			  	  			  
			  	  frmVal.aV("bankAccNbr","req","Please Enter Bank Account Number","ban_err");
				  frmVal.aV("bankAccNbr","numeric","Please Enter Numeric Bank Account Number","ban_err");
				  
				  frmVal.aV("bankName","req","Please Enter Bank Name","bkn_err");
				  frmVal.aV("bankName","regexp=[^A-Za-z\\s]","Please Enter Bank Name","bkn_err");				  
				 			  
				  frmVal.aV("bankBranch","req","Please Enter Last Name","bnb_err");
				  frmVal.aV("bankBranch","regexp=[^A-Za-z\\s]","Please Enter Valid Name","bnb_err");
								
				  frmVal.aV("locationCity","req","Please Enter Bank City Name","bnlc_err");
				  frmVal.aV("locationCity","regexp=[^A-Za-z\\s]","Please Enter Valid Bank City Name","bnlc_err");		  
			   }
			   
		//}
		//var flag = document.error_disp_handler.isValid;
		
		return document.error_disp_handler.isValid;
	}
	
	
	function validatePlrDtl(){
			return ( validatePlrDetails() && _subValid('subAny'));
		}


parent.frames[0].Version['$Source: /rep/LMS_Mgmt/WebRoot/com/skilrock/lms/web/scratchService/pwtMgmt/retailer/js/pwtDirPlayer.js,v $'] = '$Id: pwtDirPlayer.js,v 1.2 2016/10/31 11:40:12 neeraj Exp $';