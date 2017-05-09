var i18nObj = parent.frames[0].i18Obj;

function verifyAgent(agtName) {
 	fetchLimits(agtName);
  if(document.getElementById('orgName').value!="-1")
  {
  var returnType=confirm(i18nObj.prop('error.u.hav.slect') +tierMap['AGENT']+":: " + agtName)
  if(returnType)    
    {     
    document.getElementById('totalAmount').focus();
    // return true;
    }
   else
   {
       document.getElementById('orgName').selectedIndex=0;
       document.getElementById('orgName').focus();
       //return false;
   }
  }  
} 

 function HiddenForm()
{

//document.getElementById("payForm").style.visibility ="hidden";
//cash.style.show = "block";
//cheque.style.show = "block";
}
 
 
function ST5_Validation_CashChq()
{   var flag=true; 
	var orgType = document.getElementById('orgType');
	var orgName = document.getElementById('orgName');
	var totalAmount = document.getElementById('totalAmount');
	var cashAmnt = document.getElementById('cashAmnt');
		var chequeNumber = document.getElementById('chequeNumber');
	var issuePartyname = document.getElementById('issuePartyname');
	var bankName = document.getElementById('bankName');
	var chequeAmount = document.getElementById('chequeAmount');
	var verifychequeAmount = document.getElementById('verifychequeAmount');  
	
if ( document.cashChqForm.orgType.value == "-1" )
    {
      alert ( "Please select Printer." );
		document.getElementById('orgType1').innerHTML = "<font color = 'red'>"+i18nObj.prop('error.slct.company.type')+"</font>";
		flag=false;
    }

    else{
    document.getElementById('orgType1').innerHTML = "";
    }

if ( document.cashChqForm.orgName.value == "-1" )
    {
       // alert ( "Please select Printer." );
		document.getElementById('orgName1').innerHTML = "<font color = 'red'>"+i18nObj.prop('error.company.name.empty')+"</font>";
	 flag=false;
    }
    else{
    document.getElementById('orgName1').innerHTML = "";
}
// alert(flag);
if(!isDecimal(totalAmount))
{
document.getElementById('totalAmount1').innerHTML = "<font color = 'red'>"+i18nObj.prop('error.enter.positive.decimal.amt')+"</font>";
flag=false;
}
else{
document.getElementById('totalAmount1').innerHTML = "";
}


if(!isDecimal(cashAmnt))
{ //alert("in cheque verify");
document.getElementById('cashAmnt1').innerHTML = "<font color = 'red'>"+i18nObj.prop('error.verify.cash.amt')+" </font>";
flag=false;
}
else{
document.getElementById('cashAmnt1').innerHTML = "";
}

if(cashAmnt.value!=totalAmount.value)
{   //alert("in cheque amount validation");
document.getElementById('cashAmnt1').innerHTML = "<font color = 'red'>"+i18nObj.prop('error.verify.cash.amt.field')+"</font>";
flag=false;
}
else{
document.getElementById('cashAmnt1').innerHTML = "";
}
if(!isNumeric(chequeNumber,i18nObj.prop('error.enter.siz.to.ten.dgt.fr.chq.no')))
 {//alert(flag);
 
     flag=false;
 }

  if(lengthRestriction(chequeNumber,6,10))
 {//alert(flag);
 document.getElementById('chequeNumber1').innerHTML = "<font color = 'red'>"+i18nObj.prop('error.enter.siz.to.ten.dgt.fr.chq.no')+" </font>";
     flag=false;
 }
else{
document.getElementById('chequeNumber1').innerHTML = "";
}

if(!isAlphabet(issuePartyname,i18nObj.prop('error.enter.correct.fr.issue.party.name')))
{ flag=false;
}

if(!isAlphabet(bankName,i18nObj.prop('error.bank.name.empty')))
{ flag=false;
}
//alert("After Bank"+flag);
if(!isDecimal(chequeAmount))
{  // alert("in cheque amount validation");
document.getElementById('chequeAmount1').innerHTML = "<font color = 'red'>"+i18nObj.prop('error.cheque.amount.invalid')+" </font>";
flag=false;
}
else{
document.getElementById('chequeAmount1').innerHTML = "";
}
if(!isDecimal(verifychequeAmount))
{ //alert("in cheque verify");
document.getElementById('verifyAmnt').innerHTML = "<font color = 'red'>"+i18nObj.prop('error.verify.chq.amt')+"</font>";
flag=false;
}
else{
document.getElementById('verifyAmnt').innerHTML = "";
}

if(verifychequeAmount.value!=chequeAmount.value)
{   //alert("in cheque amount validation");
document.getElementById('verifyAmnt').innerHTML = "<font color = 'red'>"+i18nObj.prop('error.verify.chq.amt.field')+"</font>";
flag=false;
}
else{
document.getElementById('verifyAmnt').innerHTML = "";
}


//alert("After cheque Amount"+flag);
if(!isValidateDate())
{
flag=false;
}

//alert(flag);
return flag;

//document.getElementById("payForm").style.visibility ="hidden";
//cash.style.show = "block";
//cheque.style.show = "block";
}

function ST5_Validation_CashChqRet()
{   var flag=true; 
	var orgType = document.getElementById('orgType');
	var orgName = document.getElementById('orgName');
	var totalAmountRetailer = document.getElementById('totalAmount11');
	var verifyAmnt =  document.getElementById('verifyAmnt');
	//var totalAmount = document.getElementById('totalAmount');
	//var cashAmnt = document.getElementById('cashAmnt');
		
		var chequeNumber = document.getElementById('chequeNumber');
	var issuePartyname = document.getElementById('issuePartyname');
	var bankName = document.getElementById('bankName');
	var chequeAmount = document.getElementById('chequeAmount');
	var verifyAmount = document.getElementById('verifyAmount');
	
	
if ( document.cashChqRetForm.orgType.value == "-1" )
    {
      alert ( "Please select Printer." );
		document.getElementById('id1').innerHTML = "<font color = 'red'>"+i18nObj.prop('error.slct.company.type')+" </font>";
		flag=false;
    }

    else{
    document.getElementById('id1').innerHTML = "";
    }

if ( document.cashChqRetForm.orgName.value == "-1" )
    {
       // alert ( "Please select Printer." );
		document.getElementById('id2').innerHTML = "<font color = 'red'>"+i18nObj.prop('error.company.name.empty')+" </font>";
	 flag=false;
    }
    else{
    document.getElementById('id2').innerHTML = "";
}
// alert(flag);
if(!isDecimal(totalAmountRetailer))
{
document.getElementById('totalAmount111').innerHTML = "<font color = 'red'>"+i18nObj.prop('error.enter.positive.decimal.amt')+" </font>";
flag=false;
}
else{
document.getElementById('totalAmount111').innerHTML = "";
}

if(!isDecimal(verifyAmnt))
{ //alert("in cheque verify");
document.getElementById('verifyAmnt1').innerHTML = "<font color = 'red'>"+i18nObj.prop('error.verify.cash.amt')+" </font>";
flag=false;
}
else{
document.getElementById('verifyAmnt1').innerHTML = "";
}

if(verifyAmnt.value!=totalAmountRetailer.value)
{   //alert("in cheque amount validation");
document.getElementById('verifyAmnt1').innerHTML = "<font color = 'red'>"+i18nObj.prop('error.verify.cash.amt.field')+"</font>";
flag=false;
}
else{
document.getElementById('verifyAmnt1').innerHTML = "";
}

if(!isAlphabet(issuePartyname,i18nObj.prop('error.enter.correct.fr.issue.party.name')))
{ flag=false;
}

if(!isAlphabet(bankName,i18nObj.prop('error.bank.name.empty')))
{ flag=false;
}
//alert("After Bank"+flag);

if(!isNumeric(chequeNumber,i18nObj.prop('error.enter.siz.to.ten.dgt.fr.chq.no')))
 {//alert(flag);
 
     flag=false;
 }

  if(lengthRestriction(chequeNumber,6,10))
 {//alert(flag);
 document.getElementById('chequeNumber1').innerHTML = "<font color = 'red'>"+i18nObj.prop('error.enter.siz.to.ten.dgt.fr.chq.no')+" </font>";
     flag=false;
 }
else{
document.getElementById('chequeNumber1').innerHTML = "";
}


if(!isDecimal(chequeAmount))
{  // alert("in cheque amount validation");
document.getElementById('chequeAmount1').innerHTML = "<font color = 'red'>"+i18nObj.prop('error.cheque.amount.invalid')+" </font>";
flag=false;
}
else{
document.getElementById('chequeAmount1').innerHTML = "";
}
if(!isDecimal(verifyAmount))
{ //alert("in cheque verify");
document.getElementById('verifyAmount1').innerHTML = "<font color = 'red'>"+i18nObj.prop('error.verify.chq.amt')+" </font>";
flag=false;
}
else{
document.getElementById('verifyAmount1').innerHTML = "";
}

if(verifyAmount.value!=chequeAmount.value)
{   //alert("in cheque amount validation");
document.getElementById('verifyAmount1').innerHTML = "<font color = 'red'>"+i18nObj.prop('error.verify.chq.amt.field')+"</font>";
flag=false;
}
else{
document.getElementById('verifyAmount1').innerHTML = "";
}
//alert("After cheque Amount"+flag);
if(!isValidateDate())
{
flag=false;
}
//alert(flag);
return flag;

//document.getElementById("payForm").style.visibility ="hidden";
//cash.style.show = "block";
//cheque.style.show = "block";
}



function ST5_Validation_Cash(){

    var flag=true;
	// Make quick references to our fields
	var orgType = document.getElementById('orgType');
	var orgName = document.getElementById('orgName');
	var totalAmount = document.getElementById('totalAmount');
	var cashAmnt = document.getElementById('cashAmnt');

if ( document.retailerCashForm.orgType.value == "-1" )
    {
       //alert ( "Please select Printer." );
		document.getElementById('orgType1').innerHTML = "<font color = 'red'>"+i18nObj.prop('error.slct.company.type')+" </font>";
		flag=false;
    }

    else{
    document.getElementById('orgType1').innerHTML = "";
    }



if ( document.retailerCashForm.orgName.value == "-1" )
    {
       // alert ( "Please select Printer." );
		document.getElementById('orgName1').innerHTML = "<font color = 'red'>"+i18nObj.prop('error.company.name.empty')+" </font>";
	 flag=false;
    }
    else{
    document.getElementById('orgName1').innerHTML = "";
}
//alert(isNumericAmt(totalAmount.value) + ":: " + totalAmount.value);
//alert(flag);
if(isNumericAmt(totalAmount.value)==true || totalAmount.value==0 || totalAmount.value==".")
{
document.getElementById('totalAmount1').innerHTML = "<font color = 'red'>"+i18nObj.prop('error.enter.positive.decimal.amt')+" </font>";
flag=false;
}
else{
document.getElementById('totalAmount1').innerHTML = "";
}


if(!isDecimal(cashAmnt))
{ //alert("in cheque verify");
document.getElementById('cashAmnt1').innerHTML = "<font color = 'red'>"+i18nObj.prop('error.verify.cash.amt')+" </font>";
flag=false;
}
else{
document.getElementById('cashAmnt1').innerHTML = "";
}


if(cashAmnt.value!=totalAmount.value)
{   //alert("in cheque amount validation");
document.getElementById('cashAmnt1').innerHTML = "<font color = 'red'>"+i18nObj.prop('error.verify.cash.amt.field')+"</font>";
flag=false;
}
else{
document.getElementById('cashAmnt1').innerHTML = "";
}

 //alert(flag);
 if(flag) {

 	var isTrue = localConvertDigitToWord(totalAmount.value);
 
 	if(!isTrue) {
 		flag = false;
 		document.getElementById('totalAmount').focus();
 	}
 }
 

 
return flag;
}

function isNumericAmt(value) {

 	var strValidChars = "1234567890.";
 	var dotCount=0;
	var strChar;
	var flag = false;
	var strString = value;
	   //  test strString consists of valid characters listed above
	for (i = 0; i < strString.length; i++) {
		strChar = strString.charAt(i);
		if(strChar==".")
		  dotCount=dotCount+1;
		if (strValidChars.indexOf(strChar) == -1) {
			flag = true;
			break;
		}
	}
	if(dotCount > 1 )
	 flag=true;
	return flag;
}

function ST5_Validation_RetailerCash(){
//alert("hi");
    var flag=true;
	// Make quick references to our fields
	var totalAmountRetailer = document.getElementById('totalAmount11');
	var verifyAmnt =  document.getElementById('verifyAmnt');
	   
if ( document.cashretailerForm.orgType.value == "-1" )
    {
 //alert ( "Please select Printer." );
		document.getElementById('id1').innerHTML = "<font color = 'red'>"+i18nObj.prop('error.slct.company.type')+" </font>";
		flag=false;
    }

    else{
    document.getElementById('id1').innerHTML = "";
    }
//alert(flag);
if ( document.cashretailerForm.orgName.value == "-1" )
    {
        //alert ( "Please select Printer." );
		document.getElementById('id2').innerHTML = "<font color = 'red'>"+i18nObj.prop('error.company.name.empty')+" </font>";
	 flag=false;
    }
    else{ //alert(flag);
    document.getElementById('id2').innerHTML = "";
}
 //alert(flag);
if(!isDecimal(totalAmountRetailer))
{
document.getElementById('totalAmount111').innerHTML = "<font color = 'red'>"+i18nObj.prop('error.enter.positive.decimal.amt')+" </font>";
flag=false;
}
else{
document.getElementById('totalAmount111').innerHTML = "";
}

if(!isDecimal(verifyAmnt))
{ //alert("in cheque verify");
document.getElementById('verifyAmnt1').innerHTML = "<font color = 'red'>"+i18nObj.prop('error.verify.cash.amt')+" </font>";
flag=false;
}
else{
document.getElementById('verifyAmnt1').innerHTML = "";
}

if(verifyAmnt.value!=totalAmountRetailer.value)
{   //alert("in cheque amount validation");
document.getElementById('verifyAmnt1').innerHTML = "<font color = 'red'>"+i18nObj.prop('error.verify.cash.amt.field')+"</font>";
flag=false;
}
else{
document.getElementById('verifyAmnt1').innerHTML = "";
}

 //alert(flag);
return flag;
}

function ST5_Validation_Cheque(){

    var flag=true;
	// Make quick references to our fields
	var orgType = document.getElementById('orgType');
	var orgName = document.getElementById('orgName');
	var chequeNumber = document.getElementById('chequeNumber');
	var issuePartyname = document.getElementById('issuePartyname');
	var bankName = document.getElementById('bankName');
	var chequeAmount = document.getElementById('chequeAmount');
	var verifychequeAmount = document.getElementById('verifychequeAmount');
	
	   
if ( document.chequeForm.orgType.value == "-1" )
    {
      //alert ( "Please select orgTYpe." );
		document.getElementById('orgType11').innerHTML = "<font color = 'red'>"+i18nObj.prop('error.slct.company.type')+" </font>";
		flag=false;
    }

    else{
    document.getElementById('orgType11').innerHTML = "";
    }

if ( document.chequeForm.orgName.value == "-1" )
    {
 // alert ( "Please select Printer." );
		document.getElementById('orgName11').innerHTML = "<font color = 'red'>"+i18nObj.prop('error.company.name.empty')+" </font>";
	 flag=false;
    }
   else{
    document.getElementById('orgName11').innerHTML = "";
    }
   
 //alert(flag);
 if(!isNumeric(chequeNumber,i18nObj.prop('error.enter.siz.to.ten.dgt.fr.chq.no')))
 {//alert(flag);
 
     flag=false;
 }

  if(lengthRestriction(chequeNumber,6,10))
 {//alert(flag);
 document.getElementById('chequeNumber1').innerHTML = "<font color = 'red'>"+i18nObj.prop('error.enter.siz.to.ten.dgt.fr.chq.no')+" </font>";
     flag=false;
 }
else{
document.getElementById('chequeNumber1').innerHTML = "";
}

if(!isAlphabet(issuePartyname,i18nObj.prop('error.enter.correct.fr.issue.party.name')) )
{ flag=false;
}

if(!isAlphabet(bankName,i18nObj.prop('error.bank.name.empty')) )
{ flag=false;
}
//alert("After Bank"+flag);
if(!isDecimal(chequeAmount) || chequeAmount.value==".")
{   //alert("in cheque amount validationn");
document.getElementById('chequeAmount1').innerHTML = "<font color = 'red'>"+i18nObj.prop('error.cheque.amount.invalid')+" </font>";
flag=false;
}
else{
document.getElementById('chequeAmount1').innerHTML = "";
}
if(!isDecimal(verifychequeAmount))
{ //alert("in cheque verify");
document.getElementById('verifyAmnt').innerHTML = "<font color = 'red'>"+i18nObj.prop('error.verify.chq.amt')+" </font>";
flag=false;
}
else{
document.getElementById('verifyAmnt').innerHTML = "";
}

if(verifychequeAmount.value!=chequeAmount.value)
{   //alert("in cheque amount validation");
	document.getElementById('verifyAmnt').innerHTML = "<font color = 'red'>"+i18nObj.prop('error.verify.chq.amt.field')+"</font>";
	flag=false;
}
else{
document.getElementById('verifyAmnt').innerHTML = "";
}


//alert("After cheque Amount"+flag);
//if(!isValidateDate()) {
//	flag=false;
//}

//alert(flag);
 if(flag) {
 	var isTrue = localConvertDigitToWord(chequeAmount.value);
 	if(!isTrue) {
 		flag = false;
 		chequeAmount.focus();
 	}
 }


//alert(flag);
return flag;
}

function ST5_Validation_RetailerCheque(){
//alert("hi");
    var flag=true;
	// Make quick references to our fields
	var orgType = document.getElementById('orgType');
	var orgName = document.getElementById('orgName');
		var chequeNumber = document.getElementById('chequeNumber');
	var issuePartyname = document.getElementById('issuePartyname');
	var bankName = document.getElementById('bankName');
	var chequeAmount = document.getElementById('chequeAmount');
	var verifyAmount = document.getElementById('verifyAmount');
	
	   
if ( document.chequeretailerForm.orgType.value == "-1" )
    {
      //alert ( "Please select orgTYpe." );
		document.getElementById('orgType11').innerHTML = "<font color = 'red'>"+i18nObj.prop('error.select.org.type')+" </font>";
		flag=false;
    }

    else{
    document.getElementById('orgType11').innerHTML = "";
    }

if ( document.chequeretailerForm.orgName.value == "-1" )
    {
 // alert ( "Please select Printer." );
		document.getElementById('orgName11').innerHTML = "<font color = 'red'>"+i18nObj.prop('error.select.org.name')+" </font>";
	 flag=false;
    }
   else{
    document.getElementById('orgName11').innerHTML = "";
    }
   
 //alert(flag);


if(!isAlphabet(issuePartyname,i18nObj.prop('error.enter.correct.fr.issue.party.name')))
{ flag=false;
}

if(!isAlphabet(bankName,i18nObj.prop('error.bank.name.empty')))
{ flag=false;
}
//alert("After Bank"+flag);

if(!isNumeric(chequeNumber,i18nObj.prop('error.enter.siz.to.ten.dgt.fr.chq.no')))
 {//alert(flag);
 
     flag=false;
 }

  if(lengthRestriction(chequeNumber,6,10))
 {//alert(flag);
 document.getElementById('chequeNumber1').innerHTML = "<font color = 'red'>"+i18nObj.prop('error.enter.siz.to.ten.dgt.fr.chq.no')+" </font>";
     flag=false;
 }
else{
document.getElementById('chequeNumber1').innerHTML = "";
}


if(!isDecimal(chequeAmount))
{  // alert("in cheque amount validation");
document.getElementById('chequeAmount1').innerHTML = "<font color = 'red'>"+i18nObj.prop('error.cheque.amount.invalid')+" </font>";
flag=false;
}
else{
document.getElementById('chequeAmount1').innerHTML = "";
}
if(!isDecimal(verifyAmount))
{ //alert("in cheque verify");
document.getElementById('verifyAmount1').innerHTML = "<font color = 'red'>"+i18nObj.prop('error.verify.chq.amt')+" </font>";
flag=false;
}
else{
document.getElementById('verifyAmount1').innerHTML = "";
}

if(verifyAmount.value!=chequeAmount.value)
{   //alert("in cheque amount validation");
document.getElementById('verifyAmount1').innerHTML = "<font color = 'red'>"+i18nObj.prop('error.verify.chq.amt.field')+"</font>";
flag=false;
}
else{
document.getElementById('verifyAmount1').innerHTML = "";
}
//alert("After cheque Amount"+flag);
if(!isValidateDate())
{
flag=false;
}

//alert(flag);
return flag;
}



////for decimal 

function isEmpty(elem, helperMsg){
	if(elem.value.length == 0){
	//	alert(helperMsg);
		// set the focus to this input
			var elem1=elem.id+"1";
		document.getElementById(elem1).innerHTML = "<font color = 'red'>"+helperMsg+"</font>";
		elem.focus(); 
		return false;
	}
	else{doReset(elem);
	return true;
	}
}
function emailValidator(elem, helperMsg){
	var emailExp = /^[\w\-\.\+]+\@[a-zA-Z0-9\.\-]+\.[a-zA-z0-9]{2,4}$/;
	if(elem.value.match(emailExp)){
doReset(elem);
		return true;
	}else{
		//alert(helperMsg);
		var elem1=elem.id+"1";
		document.getElementById(elem1).innerHTML = "<font color = 'red'>"+helperMsg+"</font>";
		elem.focus();
		return false;
	}
}

function doReset(elem){
	var elem1=elem.id+"1";
	
		document.getElementById(elem1).innerHTML = "";
		
}

  function doDateReset(){
	var elem1=elem.name+"1";
	
		document.getElementById(elem1).innerHTML = "";
		
}


function isNumeric(elem, helperMsg){

	var numericExpression = /^[0-9]+$/;
	if(elem.value.match(numericExpression) && !trimAll(elem.value)==""){
	
	doReset(elem);
		return true;
	}else{
	
		var elem1=elem.id+"1";
	//alert(elem1);
	 	document.getElementById(elem1).innerHTML = "<font color = 'red'>"+helperMsg+"</font>";
		
		elem.focus();
		return false;
	}
}

function lengthRestriction(elem, min, max){
	var uInput = trimAll(elem.value);

if(uInput.length < min || uInput.length > max  ){
	//alert("hellofgfhg");
		return true;
}else{
		//alert("hello1");
		return false;
	}
}


function isAlphabet(elem, helperMsg){
	//var alphaExp = /^[a-zA-Z]+$/;
	//var alphaExp= /^[a-zA-z]+(|\s+([a-zA-z]+|[a-zA-z]+\s+[a-zA-z]+))$/;
  var alphaExp=/[a-zA-Z]/;

	if(elem.value.match(alphaExp) && !trimAll(elem.value)==""){
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
	if(elem.value.match(alphaExp) && !trimAll(elem.value)==""){
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


function trimAll(sString) 
{ //alert(sString);
while (sString.substring(0,1) == ' ')
{
sString = sString.substring(1, sString.length);
}
while (sString.substring(sString.length-1, sString.length) == ' ')
{
sString = sString.substring(0,sString.length-1);
}
//alert(sString);
return sString;
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
 if(sText==0)
 {
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
function isValidateDate(){

  var flag1=false;
  var chequeDate = dojo.widget.byId("chequeDate");
 // var chqStDate=dojo.widget.byId('chqStdate').getValue();
//  alert(document.getElementById("chqStdate").value);
  //alert(chqStDate);
  var cDate = chequeDate.getValue();
  //alert(cDate);
   
   if(cDate.toString()=="" || cDate!=null)
  { 
  document.getElementById('chequeDate1').innerHTML = "<font color = 'red'>"+i18nObj.prop('error.enter.chq.date')+"</font>";
   }
  else{
   document.getElementById('chequeDate1').innerHTML = "";
   flag1=true;
   }
   if(cDate.toString()=="" || cDate==null || cDate.toString()<document.getElementById("chqStdate").value){
        document.getElementById('chequeDate1').innerHTML = "<font color = 'red'>"+i18nObj.prop('error.chq.date.cannot.before.six.months')+"</font>";
   }
	else{
   document.getElementById('chequeDate1').innerHTML = "";
   flag1=true;
   }
return flag1;

  }
function chequeBounce(){

var flag2=true;		
var chequeNumber2 = document.getElementById('chequeNumber2');
 if(!isNumeric(chequeNumber2,i18nObj.prop('error.enter.siz.to.ten.dgt.fr.chq.no')) && !lengthRestriction(chequeNumber2,6,10))
 { 
     flag2=false;
 }
  else if(lengthRestriction(chequeNumber2,6,10))
 { document.getElementById('chequeNumber21').innerHTML = "<font color = 'red'>"+i18nObj.prop('error.enter.siz.to.ten.dgt.fr.chq.no')+"</font>";
     flag2=false;
 }
 else{
 
 document.getElementById('chequeNumber21').innerHTML = "";
 }
 return flag2;
}



function ST5_isplayerValidation()
{
var flag22=true;
var firstName = document.getElementById('firstName');
	var lastName = document.getElementById('lastName');
		var idNumber = document.getElementById('idNumber');
	    var idType = document.getElementById('idType');
	

if(!isAlphabet(firstName,i18nObj.prop('error.enter.only.char.fr.fname')))
{ flag22=false;
}


if(!isAlphabet(lastName,i18nObj.prop('error.enter.only.char.fr.lname')))
{ flag22=false;
}

if ( idType.value == "-1" )
    {
 // alert ( "Please select Printer." );
		document.getElementById('idType1').innerHTML = "<font color = 'red'>"+i18nObj.prop('error.select.id.type')+" </font>";
	 flag22=false;
    }
   else{
    document.getElementById('idType1').innerHTML = "";
    }
   
if(!isAlphanumeric(idNumber, i18nObj.prop('error.incorrect.id.no')))
{
flag22=false;
}
//alert(flag22);
if(!flag22){
remainDetail1.style.visibility = "hidden";
remainDetail.style.visibility = "visible";
}
else{
remainDetail1.style.visibility = "visible";
remainDetail.style.visibility = "visible";
}

return flag22;
}


function ST5_isplayerValidate()
{var flag12=true;
//alert("fgg");
var firstName = document.getElementById('firstName');
	var lastName = document.getElementById('lastName');
		var idNumber = document.getElementById('idNumber');
	    var idType = document.getElementById('idType');
	     var emailId = document.getElementById('emailId');
	       var phone= document.getElementById('phone');
	       var plrAddr1= document.getElementById('plrAddr1');
	         var plrCity= document.getElementById('plrCity');
	         var plrState= document.getElementById('plrState');
	           var plrCountry= document.getElementById('plrCountry');
	             var plrPin= document.getElementById('plrPin');
	

if(!isAlphabet(firstName,i18nObj.prop('error.enter.only.char.fr.fname')))
{ flag12=false;
}


if(!isAlphabet(lastName,i18nObj.prop('error.enter.only.char.fr.fname')))
{ flag12=false;
}

if ( idType.value == "-1" )
    {
 // alert ( "Please select Printer." );
		document.getElementById('idType1').innerHTML = "<font color = 'red'>"+i18nObj.prop('error.select.id.type')+" </font>";
	 flag12=false;
    }
   else{
    document.getElementById('idType1').innerHTML = "";
    }
  
if(!isAlphanumeric(idNumber, i18nObj.prop('error.incorrect.id.no')))
{
flag12=false;
}
 
if(!emailValidator(emailId, i18nObj.prop('error.incorrect.email')))
{//alert("email id")
flag12=false;
}

if(!isNumeric(phone, i18nObj.prop('error.incorrect.ph')))
{
flag12=false;
}
//alert(flag12+"phone");
if(!isEmpty(plrAddr1, i18nObj.prop('error.enter.addr1')))
{
flag12=false;
}else if(!isAlphabet(plrAddr1, i18nObj.prop('msg.inv.addr')))
{
flag12=false;
}
//alert(flag12+"add1");

if ( document.getElementById("plrCountry").value == "-1" )
    {
 // alert ( "Please select Printer." );
		document.getElementById('plrCountry1').innerHTML = "<font color = 'red'> "+i18nObj.prop('msg.blank.country')+" </font>";
	 flag12=false;
    }
   else{
    document.getElementById('plrCountry1').innerHTML = "";
    }
 //alert(flag12+"country");
    if ( document.getElementById("plrState").value == "-1" )
    {
//alert ( "Please select Printer." );
		document.getElementById('plrState1').innerHTML = "<font color = 'red'> "+i18nObj.prop('msg.blank.state')+"  </font>";
	 flag12=false;
    }
   else{
    document.getElementById('plrState1').innerHTML = "";
    }
     //alert(flag12+"state");
if(!isAlphabet(plrCity, i18nObj.prop('msg.blank.city')))
{
flag12=false;
}


if(!isNumeric(plrPin, i18nObj.prop('error.enter.only.no.fr.pin')))
{  
flag12=false;
}
//alert(flag12);
return flag12;
}








// single digit terms
function d1(x) { 
	var y = x+'';
//alert("d1 = "+y);
	switch(y) {
		case '0': n= ""; break;
		case '1': n= " One "; break;
		case '2': n= " Two "; break;
		case '3': n= " Three "; break;
		case '4': n= " Four "; break;
		case '5': n= " Five "; break;
		case '6': n= " Six "; break;
		case '7': n= " Seven "; break;
		case '8': n= " Eight "; break;
		case '9': n= " Nine "; break;
		default: n = "Not a Number";
	}
	return n;
}


// 10x digit terms
function d2(x) { 
	var y = x+'';
	//alert("d2 = "+y);
	switch(y) {
		case '0': n= ""; break;
		case '1': n= ""; break;
		case '2': n= " Twenty "; break;
		case '3': n= " Thirty "; break;
		case '4': n= " Forty "; break;
		case '5': n= " Fifty "; break;
		case '6': n= " Sixty "; break;
		case '7': n= " Seventy "; break;
		case '8': n= " Eighty "; break;
		case '9': n= " Ninety "; break;
		default: n = "Not a Number";
	}
	return n;
}



function d3(x) { // teen digit terms
	var y = x+'';
	//alert("d3 = "+y);
	switch(y) {
		case '0': n= " Ten "; break;
		case '1': n= " Eleven "; break;
		case '2': n= " Twelve "; break;
		case '3': n= " Thirteen "; break;
		case '4': n= " Fourteen "; break;
		case '5': n= " Fifteen "; break;
		case '6': n= " Sixteen "; break;
		case '7': n= " Seventeen "; break;
		case '8': n= " Eighteen "; break;
		case '9': n= " Nineteen "; break;
		default: n=  "Not a Number";
	}
	return n;
}


var n = "";
function localConvertDigitToWord(input) {
	
	var splitInput = input.split(".");
	var message = i18nObj.prop('error.amt.enter.by.you.is')+" : - ";
	var decNumName;
	//
	if(splitInput[0]) {
		decNumName = convert(splitInput[0]);
		//alert(""+decNumName);
	}
	else decNumName = " Zero ";
	
	message = message + decNumName;
	
	if(splitInput.length>1) {
		var numName = convert(splitInput[1]);
		message = message+" (  ."+splitInput[1]+" )";
	}
	var curr = parent.frames[0].document.getElementById("currencyWord").innerHTML;
	//alert(curr);
	curr=curr.replace("\n","").replace("\n","").trim();
	message = message+" "+curr+" Only";
	var flag = confirm(message);
	return flag;
}

function convert(input) {

	var inputlength = input.length;	
	var x = 0;
	var teen1 = "";
	var teen2 = "";
	var teen3 = "";
	var numName = "";
	var invalidNum = "";
	// for insertion of million, thousand, hundred 
	var a1 = "";
	var a2 = "";
	var a3 = "";
	var a4 = "";
	var a5 = "";
	// stores output
	digit = new Array(inputlength); 
	for (i = 0; i < inputlength; i++)  {
	// puts digits into array
	digit[inputlength - i] = input.charAt(i)};
	store = new Array(9); // store output
	for (i = 0; i < inputlength; i++) {
	x= inputlength - i;
	// assign text to each digit
	//alert(x);
	switch (x) { 
		case 9: d1(digit[x]); store[x] = n; break;
		case 8: if (digit[x] == "1") {teen3 = "yes";}
				  else {teen3 = "";}; d2(digit[x]); store[x] = n; break;
		case 7: if (teen3 == "yes") {teen3 = ""; d3(digit[x]);}
				  else {d1(digit[x]);}; store[x] = n; break;
		case 6: d1(digit[x]); store[x] = n; break;
		
		case 5: if (digit[x] == "1") { teen2 = "yes";}
				  else {teen2 = "";}; d2(digit[x]); store[x] = n; break;
		
		case 4: if (teen2 == "yes") {teen2 = ""; d3(digit[x]);}    
				  else {d1(digit[x]);}; store[x] = n; break;
		case 3: d1(digit[x]); store[x] = n; break;
		case 2: if (digit[x] == "1") {teen1 = "yes";}
				  else {teen1 = ""}; d2(digit[x]); store[x] = n; break;
		case 1: if (teen1 == "yes") {teen1 = "";d3(digit[x]);}     
				  else {d1(digit[x]);}; store[x] = n; break;
	}

	
	if (store[x] == "Not a Number"){invalidNum = "yes"};
		switch (inputlength){
			case 1:   store[2] = ""; 
			case 2:   store[3] = ""; 
			case 3:   store[4] = ""; 
			case 4:   store[5] = "";
			case 5:   store[6] = "";
			case 6:   store[7] = "";
			case 7:   store[8] = "";
			case 8:   store[9] = "";
		}
	if (store[9] != "") { a1 =" Hundred, "} else {a1 = ""};
	if ((store[9] != "")||(store[8] != "")||(store[7] != ""))
	{ a2 =" Million, "} else {a2 = ""};
	if (store[6] != "") { a3 =" Hundred "} else {a3 = ""};
	if ((store[6] != "")||(store[5] != "")||(store[4] != ""))
	{ a4 =" Thousand, "} else {a4 = ""};
	if (store[3] != "") { a5 =" Hundred "} else {a5 = ""};
	}
	// add up text, cancel if invalid input found
	if (invalidNum == "yes"){numName = "Invalid Input"}
	else {
	numName =  store[9] + a1 + store[8] + store[7] 
	+ a2 + store[6] + a3 + store[5] + store[4] 
	+ a4 + store[3] + a5 + store[2] + store[1];
	}
	store[1] = ""; store[2] = ""; store[3] = ""; 
	store[4] = ""; store[5] = ""; store[6] = "";
	store[7] = ""; store[8] = ""; store[9] = "";
	if (numName == "")	numName = "Zero";
	
	return numName;

}


function displayPaymentDetails(id){
//alert("kjhfjksdfhjk  "+id.value);
var flag=isPaymentValidation();
if(document.getElementById("totalAmount").value!=document.getElementById("cashAmnt").value){
flag=false;
document.getElementById("cashAmnt1").innerHTML="<font color = 'red'> "+i18nObj.prop('error.amount.notverified.correct')+" </font>";

}else if(document.getElementById("totalAmount").value==0.0 ||document.getElementById("cashAmnt").value==0.0 ){
flag=false;
document.getElementById("cashAmnt1").innerHTML="<font color = 'red'> "+i18nObj.prop('error.amount.gt.zero')+" </font>";

}
if (flag==true){
document.getElementById("cashAmnt1").innerHTML="";
var amountFlag=localConvertDigitToWord(document.getElementById("totalAmount").value);
if(!amountFlag){
flag=false;
}
}
 if(flag==true){
flag=_subValid(id);
 }
return flag;
}

function isPaymentValidation(){
var frmVal  = new parent.frames[0].Validator("CashForm");

 // frmVal.aV("orgType","dontselect=0","Please Select Company Type ","orgType1");
  
  frmVal.aV("orgName","dontselect=0",i18nObj.prop('error.company.name.empty'),"orgName1");
  
  frmVal.aV("totalAmount","req",i18nObj.prop('error.enter.positive.decimal.amt')+" 1","totalAmount1");
  frmVal.aV("totalAmount","decimal",i18nObj.prop('error.amount.invalid'),"totalAmount1");
  
  frmVal.aV("cashAmnt","req",i18nObj.prop('error.amount.verify'),"cashAmnt1");
  frmVal.aV("cashAmnt","decimal",i18nObj.prop('error.amount.invalid'),"cashAmnt1");
  
 
  
  //alert(document.error_disp_handler.isValid);
return document.error_disp_handler.isValid;

}
function removeMessages( ){
document.getElementById("orgName1").innerHTML="";
document.getElementById("totalAmount1").innerHTML="";
document.getElementById("cashAmnt1").innerHTML="";
document.forms[0].reset();
document.getElementById("orgName").value=-1;
if(document.getElementById("limitDiv"))
document.getElementById("limitDiv").innerHTML="";
return false;
}

function removeMsgs(){
document.getElementById("chequeNumber21").innerHTML="";
document.getElementById("chequeNumber2").value="";
return false;
}
function verifyChequeBounceAmt(){
	var amountArr = _id.o('chequeBounceCharges').value.split('.');
	if(amountArr.length > 1){
		if(amountArr[1].length > 2){
			_id.o('chequeBounceErrMsg').innerHTML = "<font color = 'red'>"+i18nObj.prop('error.amt.upto.two.decimal.point.allow')+"</font>";
			return false;
		} else {
			_id.o('chequeBounceErrMsg').innerHTML = "";
      	}
	} else {
		_id.o('chequeBounceErrMsg').innerHTML = "";
	}
	return true;
}

parent.frames[0].Version['$Source: /rep/LMS_Mgmt/WebRoot/com/skilrock/lms/web/accMgmt/agent/js/agt_accMgmt_validation.js,v $'] = '$Id: agt_accMgmt_validation.js,v 1.3 2016/10/31 09:46:38 neeraj Exp $';





