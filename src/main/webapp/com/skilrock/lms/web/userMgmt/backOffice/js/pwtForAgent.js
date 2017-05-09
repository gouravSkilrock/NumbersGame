


function validateData(){
		
		var isInValid = false;
		
		if(document.pwtAgent.gameNbr_Name.value == '-1'){
		isInValid = true;
		document.getElementById('gamemessage').innerHTML = '<font color=red>Please select the game first</font>';
		
		
		} else {
		document.getElementById('gamemessage').innerHTML = '';
		}
		
		if(document.pwtAgent.retOrgName.value == '-1'){
		isInValid = true;
		document.getElementById('retOrgDiv').innerHTML = '<font color=red>Please select a valid '+tierMap['RETAILER']+'</font>';
		}else {
		document.getElementById('retOrgDiv').innerHTML = '';
		}
		
		var virnCode = document.pwtAgent.virnCode;
		
		
		
		//if(virnCode.value == ""){
		//isInValid = true;
		//document.getElementById('1').innerHTML = '<font color=red>Please enter the VIRN code</font>';
		//} else{
		//document.getElementById('1').innerHTML = '';
		//}
		
		
		//for(var i=0;i<virnCode.length;i++){
		//    var j = i + 1;
		//	if(document.pwtAgent.virnCode[i].value == ""){
		//		isInValid = true;
		//		document.getElementById(j).innerHTML = '<font color=red>Please enter the VIRN code</font>';
		//		
		//		
		//	} else{
		//	document.getElementById(j).innerHTML = '';
		//	}
	//	}
		
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
  
      var virnCode = document.pwtAgent.virnCode;
            
      for(i=0; i<virnCode.length; i++){
            if( virnCode[i].value!=""){
            for(j=i+1; j<virnCode.length; j++){ 
            	if ( virnCode[i].value == virnCode[j].value){
           		 return true;
         	}
         }
      }
   }
      return false;
  }	 
  
  function checkVirn(id){
	var e = event || evt;
	var keyCode = e.which || e.keyCode;
	
	if(isEqualToLenOfBookNum(id)){
	
	if(keyCode == 46 || keyCode == 8 || keyCode >= 37 && keyCode < 41){}
	        else{
		        moveToNextElement(id);
		        return false;
	        }
	 }
return true;	 
}
  
function isEqualToLenOfBookNum(id){
	var currentVal = document.getElementById(id).value;
	if(id.match("virn")){
		len = virnLen;
			if(document.getElementById(id).value.length<len){
					return false
					}
	}else if(id.match("ticket")){
		len = ticketNbrLen;
				if(currentVal.match("-")){
				
					if(document.getElementById(id).value.length<len+2){
						return false
					}
				}else{
					if(document.getElementById(id).value.length<len){
						return false
					}
			}
	}
	
	return true;
	}
    
    function moveToNextElement(id){
    
    totalElement = document.forms[0].elements.length
    
		    for (var i=0;i <totalElement ;i++)
				{
						var e = document.forms[0].elements[i];
								if(e.id==id){
									if(i<totalElement){
										var nxtElement = document.forms[0].elements[i+1];
										if(nxtElement.type=="button"&&i+2<totalElement){
										nxtElement = document.forms[0].elements[i+2];
										}
										if(nxtElement.type=="text")
										document.getElementById(nxtElement.id).focus();
										break;
									}
								}
				}
	   }
		
	parent.frames[0].Version['$Source: /rep/LMS_Mgmt/WebRoot/com/skilrock/lms/web/userMgmt/backOffice/js/pwtForAgent.js,v $'] = '$Id: pwtForAgent.js,v 1.2 2016/10/31 11:40:07 neeraj Exp $';