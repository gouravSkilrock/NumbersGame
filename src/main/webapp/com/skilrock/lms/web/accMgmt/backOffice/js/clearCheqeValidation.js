             



 function enableRow(agentNameId,chkboxId,orgNameId,chequeNbrId,chequeAmtId,chequeDateId,issuingPartyNameid,draweeBankid,chequeStatusId,isClearedId,commAmtId,rowid,isClearedHiddenId,clearanceDateId,clearanceDateButtonId)
   {
       
     
     document.getElementById(rowid).style.background='#F2F2F2';
     
     document.getElementById(chkboxId).checked="checked";
    
     document.getElementById(orgNameId).disabled=false;
     document.getElementById(agentNameId).disabled=false;
     document.getElementById(chequeNbrId).disabled=false;
     document.getElementById(chequeAmtId).disabled=false;
     document.getElementById(chequeDateId).disabled=false;
    document.getElementById(issuingPartyNameid).disabled=false;
    document.getElementById(draweeBankid).disabled=false;
     document.getElementById(chequeStatusId).disabled=false;
     document.getElementById(commAmtId).disabled=false;
      document.getElementById(isClearedHiddenId).disabled=false;
      
      document.getElementById(isClearedHiddenId).value=document.getElementById(isClearedId).value;
     //alert("id is" + clearanceDateId)
     //dojo.widget.byId(clearanceDateId).enable();
     document.getElementById(clearanceDateId).disabled=false;
      document.getElementById(clearanceDateButtonId).disabled=false;
    
    //enable comm amount button
   // alert(document.getElementById(isClearedId).value);
      if(document.getElementById(isClearedId).value=="Cancel")
      {
           document.getElementById(commAmtId).readOnly=false;
           alert("Please fill Bounce Charges and cheque cancellation Date");
      }
      else
      {
        document.getElementById(commAmtId).readOnly=true;
         document.getElementById(commAmtId).value=parseFloat(0.00);
         alert("Please fill cheque clearation Date");
             }
     
  
   }


  function disableRow(agentNameId,chkboxId,orgNameId,chequeNbrId,chequeAmtId,chequeDateId,issuingPartyNameid,draweeBankid,chequeStatusId,commAmtId,rowid,isClearedYesId,isClearedNoId,isClearedHiddenId,clearanceDateId,clearanceDateButtonId)
   {
     document.getElementById(rowid).style.background='#EDF7FE';
     
     //document.getElementById(chkboxId).checked="checked";
    
     document.getElementById(orgNameId).disabled=true;
     document.getElementById(agentNameId).disabled=true;  
     document.getElementById(chequeNbrId).disabled=true;
     document.getElementById(chequeAmtId).disabled=true;
     document.getElementById(chequeDateId).disabled=true;
    document.getElementById(issuingPartyNameid).disabled=true;
    document.getElementById(draweeBankid).disabled=true;
     document.getElementById(chequeStatusId).disabled=true;
     document.getElementById(commAmtId).disabled=true;
    document.getElementById(isClearedHiddenId).disabled=true;
    
    //dojo.widget.byId(clearanceDateId).disable();
    document.getElementById(clearanceDateId).disabled=true;
         document.getElementById(clearanceDateButtonId).disabled=true;
      
      
       document.getElementById(isClearedYesId).checked=false;
       document.getElementById(isClearedNoId).checked=false;
       
   }


  function verifyData()
   {
       var flag=false;
       
       
      var allElements= document.getElementsByName('chequeForm');
     // alert(allElements+"all element length is " + allElements[0].elements.length);
      var allElmtLength=allElements[0].elements.length;
      for(var j=0;j<allElmtLength;j++)
       {
          	
          	var e = allElements[0].elements[j];
          	//alert("element type is  "  +e.type)
          	 if (e.type == "radio" && e.checked==true)
          	  {
          	    //alert(e.value + "checked " + e.checked);
          	     flag=true;
                 document.getElementById('errorDiv').innerHTML="";
                 break;
          	  } 
          	   
       
       }
       
    if(flag==false)
    
     document.getElementById('errorDiv').innerHTML="<font color='red'>Please Clear or Cancel at least one cheque</font>";
    
    
    
    
     
     if(verifyCommAmt()==false)
     {
      flag=false;
     document.getElementById('errorCommAmtDiv').innerHTML="<font color='red'>Enter Valid Value For Bounce Charges</font>";
     }
   return flag;
   }
  
function verifyCommAmt()
{
 var commAmount=document.getElementsByName('commAmt');
 
for(var i=0;i<commAmount.length;i++)
{
 
  if(!isDecimal(commAmount[i].value) || commAmount[i].value==".")
    {
     return false;
     break;
    } else {
    	var amountArr = commAmount[i].value.split('.');
		if(amountArr.length > 1){
			if(amountArr[1].length > 2){
				 return false;
  				 break; 
			} 
		} 
    }

}
 return true;
}

function isDecimal(sText1)
{  
 var sText=sText1;
   var validChars = "0123456789.";
   var isNumber=true;
   var myChar;
var count=0;
 if(sText.length==0){
 isNumber=false;

 }

   for (i = 0; i < sText.length ; i++) 
      { 
      myChar = sText.charAt(i); 
      if(myChar==".")
      {count++;}
      
      if (validChars.indexOf(myChar) == -1 || count>1) 
         {
         isNumber = false;
         break;
         }
      }
   return isNumber;
   
   }


function verifyAgent(agtName){
	var e = document.getElementById("agetntOrganizationId");
	var agtDisplayName = e.options[e.selectedIndex].text;
	//alert(agtDisplayName+"asa");
	document.getElementById("agentNameValue").value =agtDisplayName;
	//alert(document.getElementById("agentNameValue").value+"121as1a");
// pass selected value to display on Details Page	
 fetchLimits(agtName,agtDisplayName);
if(agtName != -1){
		var returnType = confirm("You have selected "+tierMap['AGENT']+":: " + agtDisplayName)
		if(returnType){     
			document.getElementById('chkNumberId').focus();
		} else {
			document.getElementById('agetntOrganizationId').selectedIndex=0;
			document.getElementById('agetntOrganizationId').focus();
		}
	}
}
function getAgentList() {
	var text = _ajaxCall(projectName+"/com/skilrock/lms/web/userMgmt/getAgentNameList.action?orgType=AGENT");
	var firstArr = text.data.split(":");
	var retObj = _id.o("agetntOrganizationId");

	for ( var i = 0; i < firstArr.length - 1; i=i+1) {
		var retNameArray = firstArr[i].split("|");
		var opt = new Option(retNameArray[0].toUpperCase(), retNameArray[1]);
		retObj.options[i + 1] = opt;
	}
}

parent.frames[0].Version['$Source: /rep/LMS_Mgmt/WebRoot/com/skilrock/lms/web/accMgmt/backOffice/js/clearCheqeValidation.js,v $'] = '$Id: clearCheqeValidation.js,v 1.2 2016/10/31 09:46:22 neeraj Exp $';
