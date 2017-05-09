
function checkPaymentMode(){
	var returnFlag=false;
	var chequeNbr=document.getElementById('chequeNbr').value;
	var draweeBank=document.getElementById('draweeBank').value;
	var chequeDate=document.getElementById('chequeDate').value;
	 //alert(chequeNbr);
	 //alert(draweeBank);
	 //alert("cheque date is " + chequeDate);
	  if(chequeNbr=="" && draweeBank=="" && chequeDate=="")
	    {
	      document.getElementById("tax1").innerHTML="";
	      returnFlag=true; 
	   
	    }
	    else 
	      if (chequeNbr!="" && draweeBank!="" && chequeDate!="")
	         {
	         //alert("inside all details are filled");
	         document.getElementById("tax1").innerHTML="";
	          returnFlag=true;         
	         }
	         else
	          {
	            //alert("Please fill All the Details of cheque or pay by CASH ");
	            document.getElementById("chequeVerify").innerHTML="<font color='red'>Please fill All the Details of cheque or pay by CASH </font>";
	            returnFlag=false;
	          }
	         
	
	 return returnFlag;
	}
	
	
	
	
function calculateNetAmt(){
	var pwtAmt=document.getElementById("pwtAmt").value;
	
	if(document.getElementById("tax").value=="")
	 {
	  document.getElementById("tax1").innerHTML="<font color='red'>Please enter the Tax </font>";  
	  document.getElementById("netAmt").value="";
	  return false;
	 }
	if(isNaN(document.getElementById("tax").value)){
	document.getElementById("tax1").innerHTML="<font color='red'>tax is not a number</font>";
	document.getElementById("netAmt").value="";
	return false;
	}
	else{
	document.getElementById("tax1").innerHTML="";
	
	}
	var tax=proc(document.getElementById("tax").value);
	
	if(tax > parseFloat(pwtAmt))
	{
	   alert('Tax amount can not be greater then or Equal to PWT Amount');
	  document.getElementById("netAmt").value="";
	  return false;
	}
	//alert(pwtAmt);
	//alert(tax);
	var net=pwtAmt-tax;
	//alert(net);
	document.getElementById("netAmt").value=net;
	var retunType=checkPaymentMode();
	//alert(retunType);
	if(!retunType)
		return false;
		
	return true;
} 

	
function proc(tax) { 
	
	  var num = tax;
	  return r2(num) ;
} 
	
	// Round to 2 decimal places 
	
function r2(n) { 
	
	  ans = n * 1000 
	  ans = Math.round(ans /10) + "" 
	  while (ans.length < 3) {ans = "0" + ans} 
	  len = ans.length 
	  ans = ans.substring(0,len-2) + "." + ans.substring(len-2,len)
	  return ans 
} 
parent.frames[0].Version['$Source: /rep/LMS_Mgmt/WebRoot/com/skilrock/lms/web/accMgmt/backOffice/js/playerPendingPWTDetails.js,v $'] = '$Id: playerPendingPWTDetails.js,v 1.1 2010/04/01 04:23:07 gaurav Exp $';