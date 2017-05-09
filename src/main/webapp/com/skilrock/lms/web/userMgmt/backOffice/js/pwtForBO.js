


	
		
		
		function validateData(){
		//alert("dfghdfgdfg");
		var isInValid = false;
		
		if(document.pwtBO.gameNbr_Name.value == '-1'){
		isInValid = true;
		document.getElementById('gamemessage').innerHTML = '<font color=red>Please select the game first</font>';
		
		
		} else {
		document.getElementById('gamemessage').innerHTML = '';
		}
		
		if(document.pwtBO.agtOrgName.value == '-1'){
		isInValid = true;
		document.getElementById('agtOrgDiv').innerHTML = '<font color=red>Please select a valid ' + tierMap['AGENT'] + '</font>';
		}else {
		document.getElementById('agtOrgDiv').innerHTML = '';
		}
		
		var virnCode = document.pwtBO.virnCode;
		
		var checkDuplicate = checkDuplicateVirnEntry();
		
		if(checkDuplicate){
		isInValid = true;
		document.getElementById('errorDiv').innerHTML = '<font color=red>Please change the duplicate VIRN Code entries</font>';
		} else{
		document.getElementById('errorDiv').innerHTML = '';
		}
		
		if(isInValid){
		return false;
		}
				
		return true;
		}   
		
		
	function checkDuplicateVirnEntry(){
  
     
      var virnCode = document.pwtBO.virnCode;
     
      
      for(var i=0; i<virnCode.length; i++){
            
            for(j=i+1; j<virnCode.length; j++){ 
          
               
         	if ( virnCode[i].value == virnCode[j].value && virnCode[i].value!=""){
            return true;
         	}
         }
      }
   
      return false;
  }	 
  
	parent.frames[0].Version['$Source: /rep/LMS_Mgmt/WebRoot/com/skilrock/lms/web/userMgmt/backOffice/js/pwtForBO.js,v $'] = '$Id: pwtForBO.js,v 1.2 2016/10/31 11:40:07 neeraj Exp $';	