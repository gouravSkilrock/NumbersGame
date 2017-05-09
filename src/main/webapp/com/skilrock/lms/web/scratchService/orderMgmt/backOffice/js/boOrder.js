



		function validateSearchData(){
		var isInValid = false;

		if(document.boOrderForm.agtOrgName.value == '-1'){
		isInValid = true;
		document.getElementById('agtOrgDiv').innerHTML = '<font color=red>Please select an '+tierMap['AGENT']+' first</font>';
		
		
		} else {
		document.getElementById('agtOrgDiv').innerHTML = '';
		}
		
		
		var ticketPrice = document.boOrderForm.ticketPrice.value;
		
		if(isNaN(ticketPrice)){
		isInValid = true;
		document.getElementById('ticketPriceDiv').innerHTML = '<font color=red>Please enter a numeric value</font>';
		} else {
		document.getElementById('ticketPriceDiv').innerHTML = '';
		}
		
		var gameNbr = document.boOrderForm.gameNbr.value;
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
		
		
		
		var currAvailBooks = document.boGameDetailsForm.nbrOfBooksAvailable.value - document.boGameDetailsForm.nbrOfBooksApp.value;
		
		var orderBooks = document.boGameDetailsForm.quantity.value;
		
		if(orderBooks == ""){
		isInValid = true;
		document.getElementById('booksDiv').innerHTML = '<font color=red>Please enter the Number of Books to order</font>';
		} else{
		document.getElementById('booksDiv').innerHTML = '';
		//alert(isNaN(orderBooks) + orderBooks + isNumeric(orderBooks));
		
		if(isNumeric(orderBooks)==true){
		      isInValid = true;
		document.getElementById('booksDiv').innerHTML = '<font color=red>Please enter a numeric value for Number of Books to order</font>'; 
		}else{		
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
		} 
		
		
				
		if(isInValid){
		return false;
		}
				
		return true;
		
		} 
	
	
	 function isNumeric(value) {
 	var strValidChars = "0123456789";
	var strChar;
	var flag = false;
	var strString = value;
	   //  test strString consists of valid characters listed above
	for (i = 0; i < strString.length; i++) {
		strChar = strString.charAt(i);
		if (strValidChars.indexOf(strChar) == -1) {
			flag = true;
			break;
		}
	}
	
	return flag;
}
	
	
		
  var mainDiv;
  var req;
  var which;



	function disable() {
		
		 document.getElementById('resultSearch').style.display='none';
		
	} 



  function retrieveURL(url1,div) {
  
 disable();

  url1 = url1+""+encodeURIComponent(document.boOrderForm.agtOrgName.value);
  	if(document.boOrderForm.agtOrgName.value == '-1'){
		isInValid = true;
		document.getElementById('agtOrgDiv').innerHTML = '<font color=red>Please select an '+tierMap['AGENT']+' first</font>';
		
		
		} else {
		document.getElementById('agtOrgDiv').innerHTML = '';
		}
  mainDiv=div;
  //alert(url1);
  url=""+url1;
  //var url=encodeURI(url);  // change to escape from encode
 //alert("fffff"+ url);
  
 if(url!="bo_om_selfIniOrderAgt_FetchAgtCrDetail.action?agtOrgName=-1"){
 _resp  = _ajaxCall(url);
	if(_resp.result){
            document.getElementById(mainDiv).innerHTML =_resp.data;
   			 var bal= document.getElementById("crBal");
		   if(parseFloat(bal.value)<=0.0){
		   document.getElementById("characters").style.display="block";
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
  parent.frames[0].Version['$Source: /rep/LMS_Mgmt/WebRoot/com/skilrock/lms/web/scratchService/orderMgmt/backOffice/js/boOrder.js,v $'] = '$Id: boOrder.js,v 1.2 2016/10/31 11:40:08 neeraj Exp $';
		