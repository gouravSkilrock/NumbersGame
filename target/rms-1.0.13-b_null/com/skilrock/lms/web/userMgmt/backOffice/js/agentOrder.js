

function disable() {
		
		 document.getElementById('result').style.display='none';
		
	} 

		function validateSearchData(){
		var isInValid = false;
		
		
		if(document.agentOrderForm.retOrgName.value == '-1'){
		isInValid = true;
		document.getElementById('retOrgDiv').innerHTML = '<font color=red>Please select a '+tierMap['RETAILER']+' first</font>';
		
		
		} else {
		document.getElementById('retOrgDiv').innerHTML = '';
		}
		
		
		var ticketPrice = document.agentOrderForm.ticketPrice.value;
		
		if(isNaN(ticketPrice)){
		isInValid = true;
		document.getElementById('ticketPriceDiv').innerHTML = '<font color=red>Please enter a numeric value</font>';
		} else {
		document.getElementById('ticketPriceDiv').innerHTML = '';
		}
		
		var gameNbr = document.agentOrderForm.gameNbr.value;
		if(isNaN(gameNbr)){
		isInValid = true;
		document.getElementById('gameNumDiv').innerHTML = '<font color=red>Please enter a numeric value</font>';
		}else {
		document.getElementById('gameNumDiv').innerHTML = '';
		}
		
		if(isInValid){
		return false;
		}
				
		return true;
		
		}
		
		
		function validateData(){
		
		var isInValid = false;
		
		
		
		var currAvailBooks = document.agentGameDetailsForm.nbrOfBooksAvailable.value - document.agentGameDetailsForm.nbrOfBooksApp.value;
		
		var orderBooks = document.agentGameDetailsForm.quantity.value;
		
		if(orderBooks == ""){
		isInValid = true;
		document.getElementById('booksDiv').innerHTML = '<font color=red>Please enter the Number of Books to order</font>';
		} else{
		document.getElementById('booksDiv').innerHTML = '';
		if(isNaN(orderBooks)){
		isInValid = true;
		document.getElementById('booksDiv').innerHTML = '<font color=red>Please enter a numeric value for Number of Books to order</font>';
		}else {
		document.getElementById('booksDiv').innerHTML = '';
		
		 if(orderBooks <= 0){
		 isInValid = true;
		  document.getElementById('booksDiv').innerHTML = '<font color=red>Please enter a value greater than zero for Number of Books to order </font>';
		 } else{
		  document.getElementById('booksDiv').innerHTML = '';
		  
		   if( orderBooks > currAvailBooks){
		   isInValid = true;
		   document.getElementById('booksDiv').innerHTML = '<font color=red>The number of books to order should be less than the difference of Available Books and Approved Books:' + currAvailBooks + '</font>';
		   }else{
		  document.getElementById('booksDiv').innerHTML = '';
		  }
		  
		 } 
		 
		 
		 }
		} 
		
		
				
		if(isInValid){
		return false;
		}
				
		return true;
		
		} 
		
		 var mainDiv;
  var req;
  var which;

  function retrieveURL(url1,div) {
  disable();
  
  url1=url1+""+encodeURIComponent(document.agentOrderForm.retOrgName.value);
  if(document.agentOrderForm.retOrgName.value == '-1'){
		isInValid = true;
		document.getElementById('retOrgDiv').innerHTML = '<font color=red>Please select a '+tierMap['RETAILER']+' first</font>';
		
		
		} else {
		document.getElementById('retOrgDiv').innerHTML = '';
		}
  mainDiv=div;
  var url=url1;
 if(url!="agt_om_selfIniOrderRet_FetchRetCrDetail.action?retOrgName=-1"){
 document.getElementById("characters").style.display="block";
_resp  = _ajaxCall(url);
	if(_resp.result){
            document.getElementById(mainDiv).innerHTML = _resp.data;
		    var bal= document.getElementById("crBal");
		   if( parseFloat(bal.value)<=0.0){
		   document.getElementById("search").disabled=true;
		   }else{
		   document.getElementById("characters").style.display="none";
		    document.getElementById("search").disabled=false;
		   }
      } else {
        alert("Problem: ");
        }
  }
  else{
  document.getElementById("characters").style.display="none";
  document.getElementById("search").disabled=false;
  }
}
parent.frames[0].Version['$Source: /rep/LMS_Mgmt/WebRoot/com/skilrock/lms/web/userMgmt/backOffice/js/agentOrder.js,v $'] = '$Id: agentOrder.js,v 1.2 2016/10/31 11:40:07 neeraj Exp $';