
var i18nObj = parent.frames[0].i18Obj; 

function validateData()
{
	
	var e = document.getElementById("orgName");
	var orgDisplayName = e.options[e.selectedIndex].text;
	//alert(agtDisplayName+"asa");
	document.getElementById("orgNameValue").value =orgDisplayName;
 
  var flag = true;
  
  var amount= document.getElementById('amount').value;
  var retName= document.getElementById('orgName').value;
  var remarks= document.getElementById('remarks').value;
  
  if(retName== '-1')
  {
      
	  document.getElementById('retName1').innerHTML = '<font color=red>'+i18nObj.prop('msg.pls.select')+' '+tierMap['RETAILER']+' '+i18nObj.prop('msg.name')+'</font>';
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
				document.getElementById("amount1").innerHTML = "<font color = 'red'>"+i18nObj.prop('error.zero.val.not.allow')+"</font>";
			    flag = false;  
      }
      
      else {
      	var amountArr = amount.split('.');
      	if(amountArr.length > 1){
      		if(amountArr[1].length > 2){
      			document.getElementById("amount1").innerHTML = "<font color = 'red'>"+i18nObj.prop('error.amt.upto.two.decimal.point.allow')+"</font>";
			    flag = false;  
      		} else {
      			document.getElementById("amount1").innerHTML = "";
      		}
      	} else {
      		document.getElementById("amount1").innerHTML = "";
      	}
      }
   }   
  else
     {
       document.getElementById('amount1').innerHTML = '<font color=red>'+i18nObj.prop('error.only.dcml.value.req')+'</font>';
        flag = false;
      }
  
   if(remarks=="")
   {
    document.getElementById('remarks1').innerHTML = '<font color=red>'+i18nObj.prop('error.enter.remark')+'</font>';
    flag = false;
   }
   else if(remarks.replace(/ /g,'').length==0)
   {
      document.getElementById('remarks1').innerHTML = '<font color=red>'+i18nObj.prop('error.enter.some.meaningful.remark')+'</font>';
      flag = false;
   }
  else if(remarks.length > 99)
  {
   document.getElementById('remarks1').innerHTML = '<font color=red>'+i18nObj.prop('error.max.hund.chars.allow.remark')+'</font>';
    flag = false;
  }
  else
   {
     document.getElementById('remarks1').innerHTML = "";
   }
  
  if(flag){
     var  _resp  = _ajaxCall("fetchAgtCrNoteLimit.action?listType="+_id.o('amount').value);
		if(_resp.data!='TRUE'){
	 		 alert(_resp.data);
	 		 flag=false;
	 	} 
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
function removeMsgs(){
document.getElementById("retName1").innerHTML="";
document.getElementById("amount1").innerHTML="";
document.getElementById("remarks1").innerHTML="";
document.forms["0"].reset();
document.getElementById("retName").value=-1;
if(document.getElementById("limitDiv"))
document.getElementById("limitDiv").innerHTML="";
return false;
}

parent.frames[0].Version['$Source: /rep/LMS_Mgmt/WebRoot/com/skilrock/lms/web/accMgmt/agent/js/agt_accMgmt_creditNoteAgt.js,v $'] = '$Id: agt_accMgmt_creditNoteAgt.js,v 1.3 2016/10/31 09:46:38 neeraj Exp $';