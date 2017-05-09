    function validate(){
	var isInValid = false;
	var flagTicket=0;
	var j =0;
	//alert(document.forms[0].elements.length);
	for (var i=0;i < document.forms[0].elements.length;i++)
		{
			var e = document.forms[0].elements[i];
			if(e.name=="gameNbr_Name")
			{
				if(e.value=="-1")
				{
					document.getElementById('gamemessage').innerHTML = "<font color = 'red'>Please Select Game Number</font>";
		  			isInValid=true;
		  			return false;
				}
				else{
	  				 document.getElementById('gamemessage').innerHTML = "";
	   			}
				
			}
		var checkDuplicate = checkDuplicateVirnEntry();
		if(checkDuplicate){
		isInValid = true;
		document.getElementById('errorDiv').innerHTML = '<font color=red>Please change the duplicate Ticket entries</font>';
		} else{
		document.getElementById('errorDiv').innerHTML = '';
		}
				
				
			}
	  	if(isInValid){
	 			 return false;
	  		}
	  	return true;
	  }

function checkDuplicateVirnEntry(){
  
      var ticketNumber = document.forms[0].ticketNumber;
      for(i=0; i<ticketNumber.length; i++){
            if(ticketNumber[i].value!=""){
            for(j=i+1; j<ticketNumber.length; j++){ 
	           	if (ticketNumber[i].value == ticketNumber[j].value){
	            return true;
         	}
         	}
         }
      }
   
      return false;
  }	 

var ticketNbrLen ;
var virnLen;
function checkNum(id){
	var e = event || evt;
	var charCode = e.which || e.keyCode;
			//alert(charCode);
			if(isEqualToLenOfBookNum(id)){
			        if(charCode == 46 || charCode == 8 || charCode >= 37 && charCode < 41){return true;}
			        else{
				        moveToNextElement(id);
				        return false;
			        }
       		 }
        
	if ((charCode >= 48 && charCode < 58) || charCode == 45 ||charCode == 13 ||charCode == 40 || charCode == 38 || (charCode >= 96 && charCode < 106 ||charCode == 46 || charCode == 8 || charCode == 9 ||charCode == 189 ||charCode == 109)) {
		return true;
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
	   parent.frames[0].Version['$Source: /rep/LMS_Mgmt/WebRoot/com/skilrock/lms/web/userMgmt/backOffice/js/pwtTicketEntry.js,v $'] = '$Id: pwtTicketEntry.js,v 1.1 2010/04/01 04:22:22 gaurav Exp $';