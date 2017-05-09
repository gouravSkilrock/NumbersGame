    function doReset(elem){
	var elem1=elem.name+"1";
	
		document.getElementById(elem1).innerHTML = "";
		
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
		
		
		return false;
	}
}
function isNumericForGameNumber(elem, helperMsg){
	var numericExpression = /^[0-9]+$/;
	if((elem.value).charAt(0)==0)
	{
	  var elem1=elem.name+"1";
	  document.getElementById(elem1).innerHTML = "<font color = 'red'>Game Number cant be start with Zero</font>";
	  return false;
	}
	if(elem.value.match(numericExpression)){
	doReset(elem);
		return true;
	}else{
		//alert(helperMsg);
		var elem1=elem.name+"1";
	
		document.getElementById(elem1).innerHTML = "<font color = 'red'>"+helperMsg+"</font>";
		
	
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
		
		return false;
	}
}

function lengthRestriction(elem, min, max){
	var uInput = elem.value;
	if(uInput.length >= min && uInput.length <= max){
		return true;
	}else{
		alert("Please enter between " +min+ " and " +max+ " characters");
	
		return false;
	}
}

function madeSelection(elem, helperMsg){
	if(elem.value == "Please Choose"){
		alert(helperMsg);
	
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
		
		return false;
	}
}

function isValidateDate(){

  var flag1=false;
  var startDate = dojo.widget.byId("startDate");
  var sDate = startDate.getValue();
   if(sDate=="")
  { //alert("second");
  //alert(flag1);
  document.getElementById('startDate1').innerHTML = "<font color = 'red'>Please Enter Start Date</font>";
   }
  else{
   document.getElementById('startDate1').innerHTML = "";
   flag1=true;
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

  }
else{
   document.getElementById('pwtendDate1').innerHTML = "";
  flag1=true;
   }
//alert("five");
 //alert(flag1);
  if(sDate=="" || saleDate=="" || saleDate<sDate){
 //alert("six");
  document.getElementById('saleendDate1').innerHTML = "<font color = 'red'>Please Select Sale End date Greater than Start Date</font>";
// alert("Please Select to sale date Greater than start Date");

  }
 else{
   document.getElementById('saleendDate1').innerHTML = "";
    flag1=true;
   }
  // alert("seven");
 //  alert(flag1);
  if(saleDate=="" || pwtDate=="" || saleDate>pwtDate){
   //alert("seven1");
  document.getElementById('pwtendDate1').innerHTML = "<font color = 'red'>Please Select PWT End date Greater than sale end Date Date</font>";
 //alert("Please Select PWT End date Greater than sale end Date");
    }
 else{
   document.getElementById('pwtendDate1').innerHTML = "";
    flag1=true;
   }
    //  alert("seven111");
    //  alert(flag1);
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
function trimAll(sString) 
{
while (sString.substring(0,1) == '')
{
sString = sString.substring(1, sString.length);
}
while (sString.substring(sString.length-1, sString.length) == '')
{
sString = sString.substring(0,sString.length-1);
}
return sString;
}
function validateAllowBook()
{alert("");
var flag=true;
var allow=document.getElementById("allow");
var remain=document.getElementById("remain");
var allowBook=allow.value;
var remainBook=allow.value;
//alert("allowed"+allowBook);
//alert("remainBook"+remainBook);
return false;
}


////validation for Cheque Search and cheque/cash submit pages

parent.frames[0].Version['$Source: /rep/LMS_Mgmt/WebRoot/com/skilrock/lms/web/userMgmt/backOffice/js/ADM_ST5_Validation.js,v $'] = '$Id: ADM_ST5_Validation.js,v 1.1 2010/04/01 04:22:20 gaurav Exp $';