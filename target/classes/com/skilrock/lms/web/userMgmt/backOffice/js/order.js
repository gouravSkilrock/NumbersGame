



		function validateSearchData(){
		var isInValid = false;
		
		
		var gameNbr = document.orderForm.gameNbr.value;
		
		if(isNaN(gameNbr)){
		isInValid = true;
		document.getElementById('gameNumDiv').innerHTML = '<font color=red>Please enter a numeric value</font>';
		}else {
		document.getElementById('gameNumDiv').innerHTML = '';
		}	
		
		var ticketPrice = document.orderForm.ticketPrice.value;
		
		if(isNaN(ticketPrice)){
		isInValid = true;
		document.getElementById('ticketPriceDiv').innerHTML = '<font color=red>Please enter a numeric value</font>';
		} else {
		document.getElementById('ticketPriceDiv').innerHTML = '';
		}
		
		
		
		if(isInValid){
		return false;
		}
				
		return true;
		
		}
		
		
		function validateData(){
		
		var isInValid = false;
		
		
		
		
		
		var orderBooks = document.gameDetailsForm.quantity.value;
		
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
		  
		  
		  
		 } 
		 
		 
		 }
		} 
		
		
				
		if(isInValid){
		return false;
		}
				
		return true;
		
		} 
		
		parent.frames[0].Version['$Source: /rep/LMS_Mgmt/WebRoot/com/skilrock/lms/web/userMgmt/backOffice/js/order.js,v $'] = '$Id: order.js,v 1.1 2010/04/01 04:22:20 gaurav Exp $';