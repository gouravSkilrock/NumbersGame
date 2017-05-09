


function validateData()
{
 
  var flag = true;
  
  var amount= document.getElementById('amount').value;
  var retName= document.getElementById('retName').value;
  var remarks= document.getElementById('remarks').value;
  
  if(retName== '-1')
  {
      
	  document.getElementById('retName1').innerHTML = '<font color=red>Please select '+tierMap['RETAILER']+' Name</font>';
	  flag=false;
 }
  else
  {
    document.getElementById('retName1').innerHTML = "";  
   }
  
  if(isDecimal(amount) && amount!=".")
  {
      if (parseFloat(amount,10) == "0")
       {
				document.getElementById("amount1").innerHTML = "<font color = 'red'>Zero Value not Allowed</font>";
			    flag = false;  
      }
      
      else
      {
        document.getElementById("amount1").innerHTML = "";
      }
   }   
  else
     {
       document.getElementById('amount1').innerHTML = '<font color=red>Only decimal values are required</font>';
        flag = false;
      }
  
   if(remarks=="")
   {
    document.getElementById('remarks1').innerHTML = '<font color=red>Please enter remarks</font>';
    flag = false;
   }
   else if(remarks.replace(/ /g,'').length==0)
   {
      document.getElementById('remarks1').innerHTML = '<font color=red>Please enter some meaningfull remarks</font>';
      flag = false;
   }
  else if(remarks.length > 99)
  {
   document.getElementById('remarks1').innerHTML = '<font color=red>Maximun 100 characters are allowed for remarks</font>';
    flag = false;
  }
  else
   {
     document.getElementById('remarks1').innerHTML = "";
   }
  
  	if(flag) {
	 	var isTrue = convertDigitToWord(amount);
	 	if(!isTrue) {
	 		flag = false;
	 		document.getElementById('amount').focus();
	 	}
 	}
  	
    return flag;
  
  
}

function isDecimal(sText1) {
	var sText = sText1;
	var validChars = "0123456789.";
	var isNumber = true;
	var myChar;
	var count = 0;
	if (sText.length == 0) {
		isNumber = false;
	}
	for (i = 0; i < sText.length; i++) {
		myChar = sText.charAt(i);
		if (myChar == ".") {
			count++;
		}
		if (validChars.indexOf(myChar) == -1 || count > 1) {
			isNumber = false;
			break;
		}
	}
	return isNumber;
}
parent.frames[0].Version['$Source: /rep/LMS_Mgmt/WebRoot/com/skilrock/lms/web/accMgmt/backOffice/js/debitNoteAgt.js,v $'] = '$Id: debitNoteAgt.js,v 1.2 2016/10/31 09:46:22 neeraj Exp $';