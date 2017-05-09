
function ST5_Validation(){

	// Make quick references to our fields
	var gameName = document.getElementById('gameName');
	var gameNumber = document.getElementById('gameNumber');
	var totalTickets = document.getElementById('totalTickets');
	var priceperTicket = document.getElementById('priceperTicket');
	var ticketpetBook = document.getElementById('ticketpetBook');
	var booksperPack = document.getElementById('booksperPack');
	var totalBooks = document.getElementById('totalBooks');
	var agentSaleCommRate = document.getElementById('agentSaleCommRate');
	var retailerSaleCommRate = document.getElementById('retailerSaleCommRate');
	var booksperPack = document.getElementById('booksperPack');
	var govtCommRate = document.getElementById('govtCommRate');
	var flag=true;

if(!isNumeric(gameNumber, "Please enter only Number for Game Number"))
 {flag=false;
}

	//alert("main1 flag"+flag);


if(!isAlphanumeric(gameName, "Numbers and Letters Only for Game Name"))
{
flag=false;
}

	//alert("main2 flag"+flag);
if(!isValidateDate())
 {
flag=false;
 }

//	alert("main3 flag"+flag);	
if(isDecimal(priceperTicket, "Please enter only Decimal Number for Price Per Ticket")){
}
else{flag=false;}
//	alert("main4 flag"+flag);
if(!isNumeric(totalTickets, "Please enter only Number for Total Number of Tickets")){
flag=false;
 }

	//alert("main5 flag"+flag);
if(!isNumeric(ticketpetBook, "Please enter only Number for Ticket Per Book"))
{
flag=false;
}

	//alert("main6 flag"+flag);
if(!isNumeric(booksperPack, "Please enter only Number for Books Per Pack"))
{
 flag=false;
}

	//alert("main7 flag"+flag);
	
	if(!isNumeric(totalBooks, "Please enter only Number for Total Books"))
 {
flag=false;
 }

 if(!isNumeric(agentSaleCommRate, "Please enter only Number for "+tierMap['AGENT']+" Sale Profit rate"))
 {
flag=false;
 }
  if(!isNumeric(govtCommRate, "Please enter only Number for Good Cause rate"))
 {
flag=false;
 }

	//alert("main8 flag"+flag);
if(!isNumeric(retailerSaleCommRate, "Please enter only Number "+tierMap['RETAILER']+" Sale Profit rate"))
{
flag=false;
 }

//alert("main9 flag"+flag);
if(!doSupplierSelection())
{flag=false;
	}
	
//alert("main flag"+flag);
 return flag;
}


function doReset(elem){
	var elem1=elem.name+"1";
	
		document.getElementById(elem1).innerHTML = "";
		
}

function doDateReset(){
	var elem1=elem.name+"1";
	
		document.getElementById(elem1).innerHTML = "";
		
}

function isDecimal(sText1)
{  
 var sText=sText1.value;
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

function isEmpty(elem, helperMsg){
	if(elem.value.length == 0){
	//	alert(helperMsg);
		elem.focus(); // set the focus to this input
		return false;
	}
	return true;
}

function isNumeric(elem, helperMsg){
	var numericExpression = /^[0-9]+$/;
	if(elem.value.match(numericExpression)){
	doReset(elem);
		return true;
	}else{
		//alert(helperMsg);
		var elem1=elem.name+"1";
	
		document.getElementById(elem1).innerHTML = "<font color = 'red'>"+helperMsg+"</font>";
		
		elem.focus();
		return false;
	}
}

function isAlphabet(elem, helperMsg){
	var alphaExp = /[a-zA-Z]/;
	if(elem.value.match(alphaExp)){
	doReset(elem);
		return true;
	}else{
		//alert(helperMsg);
		var elem1=elem.name+"1";
	
		document.getElementById(elem1).innerHTML = "<font color = 'red'>"+helperMsg+"</font>";
		elem.focus();
		return false;
	}
}

function isAlphanumeric(elem, helperMsg){
	var alphaExp = /^[0-9a-zA-Z]+$/;
	if(elem.value.match(alphaExp)){
	doReset(elem);
		return true;
	}else{
		//alert(helperMsg);
		var elem1=elem.name+"1";
		//document.getElementById('gameName1').innerHTML = "<font color = 'red'>"+helperMsg+"</font>";
		document.getElementById(elem1).innerHTML = "<font color = 'red'>"+helperMsg+"</font>";
		elem.focus();
		return false;
	}
}

function lengthRestriction(elem, min, max){
	var uInput = elem.value;
	if(uInput.length >= min && uInput.length <= max){
		return true;
	}else{
		alert("Please enter between " +min+ " and " +max+ " characters");
		elem.focus();
		return false;
	}
}

function madeSelection(elem, helperMsg){
	if(elem.value == "Please Choose"){
		alert(helperMsg);
		elem.focus();
		return false;
	}else{
		return true;
	}
}

function emailValidator(elem, helperMsg){
	var emailExp = /^[\w\-\.\+]+\@[a-zA-Z0-9\.\-]+\.[a-zA-z0-9]{2,4}$/;
	if(elem.value.match(emailExp)){
		return true;
	}else{
		//alert(helperMsg);
		elem.focus();
		return false;
	}
}

function isValidateDate(){

  var flag1=true;
  var startDate = dojo.widget.byId("startDate");
  var sDate = startDate.getValue();
   if(sDate=="")
  { //alert("second");
  //alert(flag1);
  document.getElementById('startDate1').innerHTML = "<font color = 'red'>Please Enter Start Date</font>";
     flag1=false;
   }
  else{
   document.getElementById('startDate1').innerHTML = "";
 
   }

  var saleendDate = dojo.widget.byId("saleendDate");
  var saleDate= saleendDate.getValue();

  

var pwtendDate = dojo.widget.byId("pwtendDate");
  var pwtDate= pwtendDate.getValue();
 // alert(pwtDate);

  if(saleDate=="")
  { //alert("third");
   //alert(flag1);
  document.getElementById('saleendDate1').innerHTML = "<font color = 'red'>Please Sale End Date</font>";
   flag1=false;
  }
   else{
   document.getElementById('saleendDate1').innerHTML = "";
  flag1=true;
   }


   if(pwtDate=="")
  {//alert("four");
   //alert(flag1);
  document.getElementById('pwtendDate1').innerHTML = "<font color = 'red'>Please pwt end Date</font>";
  //alert("Please Enter PWT End Date");
  flag1=false;
  }
else{
   document.getElementById('pwtendDate1').innerHTML = "";
  flag1=true;
   }
//alert("five");
 //alert(flag1);
  if(saleDate<=sDate){
 //alert("six");
  document.getElementById('saleendDate1').innerHTML = "<font color = 'red'>Please Select Sale End date Greater than Start Date</font>";
// alert("Please Select to sale date Greater than start Date");
  flag1=false;
  }
 else{
   document.getElementById('saleendDate1').innerHTML = "";
    flag1=true;
   }
  // alert("seven");
 //  alert(flag1);
  if(saleDate>=pwtDate){
   //alert("seven1");
  document.getElementById('pwtendDate1').innerHTML = "<font color = 'red'>Please Select PWT End date Greater than sale end Date Date</font>";
 //alert("Please Select PWT End date Greater than sale end Date");
      flag1=false;
    }
 else{
   document.getElementById('pwtendDate1').innerHTML = "";
    flag1=true;
   }
    //  alert("seven111");
    //alert(flag1);
return flag1;

  }
function doSupplierSelection(){
if ( document.uploadForm.supplierName.value == "-1" )
    {
       // alert ( "Please select Printer." );
		document.getElementById('selectSupp').innerHTML = "<font color = 'red'>Please Select Printer </font>";
		
        return false;
    }
    else{
    document.getElementById('selectSupp').innerHTML = "";
return true;
}
}
function ST5_CheckReqField(){
 var totalTicket = document.uploadForm.totalTickets.value;
var ticketPerBook=document.uploadForm.ticketpetBook.value;
var books=totalTicket/ticketPerBook;
document.uploadForm.totalBooks.value=books;
return true;
} 
////validation for Cheque Search and cheque/cash submit pages

parent.frames[0].Version['$Source: /rep/LMS_Mgmt/WebRoot/com/skilrock/lms/web/scratchService/inventoryMgmt/backOffice/js/INV_ST5_Validation.js,v $'] = '$Id: INV_ST5_Validation.js,v 1.2 2016/10/31 11:40:03 neeraj Exp $';