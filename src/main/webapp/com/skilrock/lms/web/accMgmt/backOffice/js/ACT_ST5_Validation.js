var i18nObj = parent.frames[0].i18Obj;

function verifyAgent(agtName) {
	$("#limitDiv").html("");
	var e = document.getElementById("orgName");
	var agtDisplayName = e.options[e.selectedIndex].text;
	// alert(agtDisplayName+"asa");
	document.getElementById("agentNameValue").value =agtDisplayName;
	// alert(document.getElementById("agentNameValue").value+"121as1a");
// pass selected value to display on Details Page
   if($("#orgType").val()=='AGENT'){
 fetchLimits(agtName,agtDisplayName);
   }
 if(document.getElementById('orgName').value!="-1")
  {
  var returnType=confirm(i18nObj.prop('error.u.hav.slect') +tierMap['AGENT']+":: " + agtDisplayName)
  if(returnType)    
    {     
    document.getElementById('totalAmount').focus();
    // return true;
    }
   else
   {
       document.getElementById('orgName').selectedIndex=0;
       document.getElementById('orgName').focus();
       // return false;
   }
  }  
} 
function varifyRetailer(retName){
	var e = document.getElementById("retOrgName");
	var retDisplayName = e.options[e.selectedIndex].text;
	if(retName!=-1){
		$("#retOrgName1").html('');
	}
	fetchLimits(retName,retDisplayName);
}
 function HiddenForm()
{

// document.getElementById("payForm").style.visibility ="hidden";
// cash.style.show = "block";
// cheque.style.show = "block";
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
      alert (i18nObj.prop('error.slct.printer'));
		document.getElementById('orgType1').innerHTML = "<font color = 'red'>"+i18nObj.prop('error.slct.company.type')+" </font>";
		flag=false;
    }

    else{
    document.getElementById('orgType1').innerHTML = "";
    }

if ( document.cashChqForm.orgName.value == "-1" )
    {
       // alert ( "Please select Printer." );
		document.getElementById('orgName1').innerHTML = "<font color = 'red'>"+i18nObj.prop('error.company.name.empty')+" </font>";
	 flag=false;
    }
    else{
    document.getElementById('orgName1').innerHTML = "";
}
// alert(flag);
if(!isDecimal(totalAmount))
{
document.getElementById('totalAmount1').innerHTML = "<font color = 'red'>"+i18nObj.prop('error.enter.positive.decimal.amt')+" </font>";
flag=false;
}
else{
document.getElementById('totalAmount1').innerHTML = "";
}


if(!isDecimal(cashAmnt))
{ // alert("in cheque verify");
document.getElementById('cashAmnt1').innerHTML = "<font color = 'red'>"+i18nObj.prop('error.verify.cash.amt')+" </font>";
flag=false;
}
else{
document.getElementById('cashAmnt1').innerHTML = "";
}

if(cashAmnt.value!=totalAmount.value)
{   // alert("in cheque amount validation");
document.getElementById('cashAmnt1').innerHTML = "<font color = 'red'>"+i18nObj.prop('error.verify.cash.amt.field')+"</font>";
flag=false;
}
else{
document.getElementById('cashAmnt1').innerHTML = "";
}
if(!isNumeric(chequeNumber,i18nObj.prop('error.enter.siz.to.ten.dgt.fr.chq.no')))
 {// alert(flag);
 
     flag=false;
 }

  if(lengthRestriction(chequeNumber,6,10))
 {// alert(flag);
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
// alert("After Bank"+flag);
if(!isDecimal(chequeAmount))
{  // alert("in cheque amount validation");
document.getElementById('chequeAmount1').innerHTML = "<font color = 'red'>"+i18nObj.prop('error.cheque.amount.invalid')+" </font>";
flag=false;
}
else{
document.getElementById('chequeAmount1').innerHTML = "";
}
if(!isDecimal(verifychequeAmount))
{ // alert("in cheque verify");
document.getElementById('verifyAmnt').innerHTML = "<font color = 'red'>"+i18nObj.prop('error.verify.chq.amt')+" </font>";
flag=false;
}
else{
document.getElementById('verifyAmnt').innerHTML = "";
}

if(verifychequeAmount.value!=chequeAmount.value)
{   // alert("in cheque amount validation");
document.getElementById('verifyAmnt').innerHTML = "<font color = 'red'>"+i18nObj.prop('error.verify.chq.amt.field')+"</font>";
flag=false;
}
else{
document.getElementById('verifyAmnt').innerHTML = "";
}


// alert("After cheque Amount"+flag);
if(!isValidateDate())
{
flag=false;
}

// alert(flag);
return flag;

// document.getElementById("payForm").style.visibility ="hidden";
// cash.style.show = "block";
// cheque.style.show = "block";
}

function ST5_Validation_CashChqRet()
{   var flag=true; 
	var orgType = document.getElementById('orgType');
	var orgName = document.getElementById('orgName');
	var totalAmountRetailer = document.getElementById('totalAmount11');
	var verifyAmnt =  document.getElementById('verifyAmnt');
	// var totalAmount = document.getElementById('totalAmount');
	// var cashAmnt = document.getElementById('cashAmnt');
		
		var chequeNumber = document.getElementById('chequeNumber');
	var issuePartyname = document.getElementById('issuePartyname');
	var bankName = document.getElementById('bankName');
	var chequeAmount = document.getElementById('chequeAmount');
	var verifyAmount = document.getElementById('verifyAmount');
	
	
if ( document.cashChqRetForm.orgType.value == "-1" )
    {
      alert ( i18nObj.prop('error.slct.printer') );
		document.getElementById('id1').innerHTML = "<font color = 'red'>"+i18nObj.prop('error.slct.company.type')+"</font>";
		flag=false;
    }

    else{
    document.getElementById('id1').innerHTML = "";
    }

if ( document.cashChqRetForm.orgName.value == "-1" )
    {
       // alert ( "Please select Printer." );
		document.getElementById('id2').innerHTML = "<font color = 'red'>"+i18nObj.prop('error.company.name.empty')+"</font>";
	 flag=false;
    }
    else{
    document.getElementById('id2').innerHTML = "";
}
// alert(flag);
if(!isDecimal(totalAmountRetailer))
{
document.getElementById('totalAmount111').innerHTML = "<font color = 'red'>"+i18nObj.prop('error.enter.positive.decimal.amt')+"</font>";
flag=false;
}
else{
document.getElementById('totalAmount111').innerHTML = "";
}

if(!isDecimal(verifyAmnt))
{ // alert("in cheque verify");
document.getElementById('verifyAmnt1').innerHTML = "<font color = 'red'>"+i18nObj.prop('error.verify.cash.amt')+"</font>";
flag=false;
}
else{
document.getElementById('verifyAmnt1').innerHTML = "";
}

if(verifyAmnt.value!=totalAmountRetailer.value)
{   // alert("in cheque amount validation");
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
// alert("After Bank"+flag);

if(!isNumeric(chequeNumber,i18nObj.prop('error.enter.siz.to.ten.dgt.fr.chq.no')))
 {// alert(flag);
 
     flag=false;
 }

  if(lengthRestriction(chequeNumber,6,10))
 {// alert(flag);
 document.getElementById('chequeNumber1').innerHTML = "<font color = 'red'>"+i18nObj.prop('error.enter.siz.to.ten.dgt.fr.chq.no')+"</font>";
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
{ // alert("in cheque verify");
document.getElementById('verifyAmount1').innerHTML = "<font color = 'red'>"+i18nObj.prop('error.verify.chq.amt')+"</font>";
flag=false;
}
else{
document.getElementById('verifyAmount1').innerHTML = "";
}

if(verifyAmount.value!=chequeAmount.value)
{   // alert("in cheque amount validation");
document.getElementById('verifyAmount1').innerHTML = "<font color = 'red'>"+i18nObj.prop('error.verify.chq.amt.field')+"</font>";
flag=false;
}
else{
document.getElementById('verifyAmount1').innerHTML = "";
}
// alert("After cheque Amount"+flag);
if(!isValidateDate())
{
flag=false;
}
// alert(flag);
return flag;

// document.getElementById("payForm").style.visibility ="hidden";
// cash.style.show = "block";
// cheque.style.show = "block";
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
       // alert ( "Please select Printer." );
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
// alert(isNumericAmt(totalAmount.value) + ":: " + totalAmount.value);
// alert(flag);
if(isNumericAmt(totalAmount.value)==true || totalAmount.value==0 || totalAmount.value==".")
{
document.getElementById('totalAmount1').innerHTML = "<font color = 'red'>"+i18nObj.prop('error.enter.positive.decimal.amt')+" </font>";
flag=false;
}
else{
document.getElementById('totalAmount1').innerHTML = "";
}


if(!isDecimal(cashAmnt))
{ // alert("in cheque verify");
document.getElementById('cashAmnt1').innerHTML = "<font color = 'red'>"+i18nObj.prop('error.verify.cash.amt')+" </font>";
flag=false;
}
else{
document.getElementById('cashAmnt1').innerHTML = "";
}


if(cashAmnt.value!=totalAmount.value)
{   // alert("in cheque amount validation");
document.getElementById('cashAmnt1').innerHTML = "<font color = 'red'>"+i18nObj.prop('error.verify.cash.amt.field')+"</font>";
flag=false;
}
else{
document.getElementById('cashAmnt1').innerHTML = "";
}

 // alert(flag);
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
	   // test strString consists of valid characters listed above
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
// alert("hi");
    var flag=true;
	// Make quick references to our fields
	var totalAmountRetailer = document.getElementById('totalAmount11');
	var verifyAmnt =  document.getElementById('verifyAmnt');
	   
if ( document.cashretailerForm.orgType.value == "-1" )
    {
 // alert ( "Please select Printer." );
		document.getElementById('id1').innerHTML = "<font color = 'red'>"+i18nObj.prop('error.slct.company.type')+" </font>";
		flag=false;
    }

    else{
    document.getElementById('id1').innerHTML = "";
    }
// alert(flag);
if ( document.cashretailerForm.orgName.value == "-1" )
    {
        // alert ( "Please select Printer." );
		document.getElementById('id2').innerHTML = "<font color = 'red'>"+i18nObj.prop('error.company.name.empty')+"</font>";
	 flag=false;
    }
    else{ // alert(flag);
    document.getElementById('id2').innerHTML = "";
}
 // alert(flag);
if(!isDecimal(totalAmountRetailer))
{
document.getElementById('totalAmount111').innerHTML = "<font color = 'red'>"+i18nObj.prop('error.enter.positive.decimal.amt')+"</font>";
flag=false;
}
else{
document.getElementById('totalAmount111').innerHTML = "";
}

if(!isDecimal(verifyAmnt))
{ // alert("in cheque verify");
document.getElementById('verifyAmnt1').innerHTML = "<font color = 'red'>"+i18nObj.prop('error.verify.cash.amt')+"</font>";
flag=false;
}
else{
document.getElementById('verifyAmnt1').innerHTML = "";
}

if(verifyAmnt.value!=totalAmountRetailer.value)
{   // alert("in cheque amount validation");
document.getElementById('verifyAmnt1').innerHTML = "<font color = 'red'>"+i18nObj.prop('error.verify.cash.amt.field')+"</font>";
flag=false;
}
else{
document.getElementById('verifyAmnt1').innerHTML = "";
}

 // alert(flag);
return flag;
}

function ST5_Validation_Cheque(){
alert();
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
      // alert ( "Please select orgTYpe." );
		document.getElementById('orgType11').innerHTML = "<font color = 'red'>"+i18nObj.prop('error.slct.company.type')+"</font>";
		flag=false;
    }

    else{
    document.getElementById('orgType11').innerHTML = "";
    }

if ( document.chequeForm.orgName.value == "-1" )
    {
 // alert ( "Please select Printer." );
		document.getElementById('orgName11').innerHTML = "<font color = 'red'>"+i18nObj.prop('error.company.name.empty')+"</font>";
	 flag=false;
    }
   else{
    document.getElementById('orgName11').innerHTML = "";
    }
   
 // alert(flag);
 if(!isNumeric(chequeNumber,i18nObj.prop('error.enter.siz.to.ten.dgt.fr.chq.no')))
 {// alert(flag);
 
     flag=false;
 }

  if(lengthRestriction(chequeNumber,6,10))
 {// alert(flag);
 document.getElementById('chequeNumber1').innerHTML = "<font color = 'red'>"+i18nObj.prop('error.enter.siz.to.ten.dgt.fr.chq.no')+"</font>";
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
// alert("After Bank"+flag);
if(!isDecimal(chequeAmount) || chequeAmount.value==".")
{   // alert("in cheque amount validationn");
document.getElementById('chequeAmount1').innerHTML = "<font color = 'red'>"+i18nObj.prop('error.cheque.amount.invalid')+"</font>";
flag=false;
}
else{
document.getElementById('chequeAmount1').innerHTML = "";
}
if(!isDecimal(verifychequeAmount))
{ // alert("in cheque verify");
document.getElementById('verifyAmnt').innerHTML = "<font color = 'red'>"+i18nObj.prop('error.verify.chq.amt')+"</font>";
flag=false;
}
else{
document.getElementById('verifyAmnt').innerHTML = "";
}

if(verifychequeAmount.value!=chequeAmount.value)
{   // alert("in cheque amount validation");
	document.getElementById('verifyAmnt').innerHTML = "<font color = 'red'>"+i18nObj.prop('error.verify.chq.amt.field')+"</font>";
	flag=false;
}
else{
document.getElementById('verifyAmnt').innerHTML = "";
}


// alert("After cheque Amount"+flag);
// if(!isValidateDate()) {
// flag=false;
// }

// alert(flag);
 if(flag) {
 	var isTrue = localConvertDigitToWord(chequeAmount.value);
 	if(!isTrue) {
 		flag = false;
 		chequeAmount.focus();
 	}
 }


// alert(flag);
return flag;
}

function ST5_Validation_RetailerCheque(){
// alert("hi");
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
      // alert ( "Please select orgTYpe." );
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
   
 // alert(flag);


if(!isAlphabet(issuePartyname,i18nObj.prop('error.enter.correct.fr.issue.party.name')))
{ flag=false;
}

if(!isAlphabet(bankName,i18nObj.prop('error.bank.name.empty')))
{ flag=false;
}
// alert("After Bank"+flag);

if(!isNumeric(chequeNumber,i18nObj.prop('error.enter.siz.to.ten.dgt.fr.chq.no')))
 {// alert(flag);
 
     flag=false;
 }

  if(lengthRestriction(chequeNumber,6,10))
 {// alert(flag);
 document.getElementById('chequeNumber1').innerHTML = "<font color = 'red'>"+i18nObj.prop('error.enter.siz.to.ten.dgt.fr.chq.no')+"</font>";
     flag=false;
 }
else{
document.getElementById('chequeNumber1').innerHTML = "";
}


if(!isDecimal(chequeAmount))
{  // alert("in cheque amount validation");
document.getElementById('chequeAmount1').innerHTML = "<font color = 'red'>"+i18nObj.prop('error.cheque.amount.invalid')+"</font>";
flag=false;
}
else{
document.getElementById('chequeAmount1').innerHTML = "";
}
if(!isDecimal(verifyAmount))
{ // alert("in cheque verify");
document.getElementById('verifyAmount1').innerHTML = "<font color = 'red'>"+i18nObj.prop('error.verify.chq.amt')+"</font>";
flag=false;
}
else{
document.getElementById('verifyAmount1').innerHTML = "";
}

if(verifyAmount.value!=chequeAmount.value)
{   // alert("in cheque amount validation");
document.getElementById('verifyAmount1').innerHTML = "<font color = 'red'>"+i18nObj.prop('error.verify.chq.amt.field')+"</font>";
flag=false;
}
else{
document.getElementById('verifyAmount1').innerHTML = "";
}
// alert("After cheque Amount"+flag);
if(!isValidateDate())
{
flag=false;
}

// alert(flag);
return flag;
}



// //for decimal

function isEmpty(elem, helperMsg){
	if(elem.value.length == 0){
	// alert(helperMsg);
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
		// alert(helperMsg);
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
	// alert(elem1);
	 	document.getElementById(elem1).innerHTML = "<font color = 'red'>"+helperMsg+"</font>";
		
		elem.focus();
		return false;
	}
}

function lengthRestriction(elem, min, max){
	var uInput = trimAll(elem.value);

if(uInput.length < min || uInput.length > max  ){
	// alert("hellofgfhg");
		return true;
}else{
		// alert("hello1");
		return false;
	}
}


function isAlphabet(elem, helperMsg){
	// var alphaExp = /^[a-zA-Z]+$/;
	// var alphaExp= /^[a-zA-z]+(|\s+([a-zA-z]+|[a-zA-z]+\s+[a-zA-z]+))$/;
  var alphaExp=/[a-zA-Z]/;

	if(elem.value.match(alphaExp) && !trimAll(elem.value)==""){
	doReset(elem);
		return true;
	}else{
		// alert(helperMsg);
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
		// alert(helperMsg);
		var elem1=elem.name+"1";
		// document.getElementById('gameName1').innerHTML = "<font color =
		// 'red'>"+helperMsg+"</font>";
		document.getElementById(elem1).innerHTML = "<font color = 'red'>"+helperMsg+"</font>";
		elem.focus();
		return false;
	}
}


function trimAll(sString) 
{ // alert(sString);
while (sString.substring(0,1) == ' ')
{
sString = sString.substring(1, sString.length);
}
while (sString.substring(sString.length-1, sString.length) == ' ')
{
sString = sString.substring(0,sString.length-1);
}
// alert(sString);
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
// alert(document.getElementById("chqStdate").value);
  // alert(chqStDate);
  var cDate = chequeDate.getValue();
  // alert(cDate);
   
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
   
if(!isAlphanumeric(idNumber,i18nObj.prop('error.incorrect.id.no')))
{
flag22=false;
}
// alert(flag22);
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
// alert("fgg");
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
		document.getElementById('idType1').innerHTML = "<font color = 'red'>"+i18nObj.prop('error.select.id.type')+"</font>";
	 flag12=false;
    }
   else{
    document.getElementById('idType1').innerHTML = "";
    }
  
if(!isAlphanumeric(idNumber,i18nObj.prop('error.incorrect.id.no')))
{
flag12=false;
}
 
if(!emailValidator(emailId,i18nObj.prop('error.incorrect.email')))
{// alert("email id")
flag12=false;
}

if(!isNumeric(phone,i18nObj.prop('error.incorrect.ph')))
{
flag12=false;
}
// alert(flag12+"phone");
if(!isEmpty(plrAddr1,i18nObj.prop('error.enter.addr1')))
{
flag12=false;
}else if(!isAlphabet(plrAddr1,i18nObj.prop('msg.inv.addr')))
{
flag12=false;
}
// alert(flag12+"add1");

if ( document.getElementById("plrCountry").value == "-1" )
    {
 // alert ( "Please select Printer." );
		document.getElementById('plrCountry1').innerHTML = "<font color = 'red'> "+i18nObj.prop('msg.blank.country')+"</font>";
	 flag12=false;
    }
   else{
    document.getElementById('plrCountry1').innerHTML = "";
    }
 // alert(flag12+"country");
    if ( document.getElementById("plrState").value == "-1" )
    {
// alert ( "Please select Printer." );
		document.getElementById('plrState1').innerHTML = "<font color = 'red'> "+i18nObj.prop('msg.blank.state')+"</font>";
	 flag12=false;
    }
   else{
    document.getElementById('plrState1').innerHTML = "";
    }
     // alert(flag12+"state");
if(!isAlphabet(plrCity,i18nObj.prop('msg.blank.city')))
{
flag12=false;
}


if(!isNumeric(plrPin,i18nObj.prop('error.enter.only.no.fr.pin')))
{  
flag12=false;
}
// alert(flag12);
return flag12;
}








// single digit terms
function d1(x) { 
	var y = x+'';
// alert("d1 = "+y);
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
	// alert("d2 = "+y);
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
	// alert("d3 = "+y);
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
		// alert(""+decNumName);
	}
	else decNumName = " Zero ";
	
	message = message + decNumName;
	
	if(splitInput.length>1) {
		var numName = convert(splitInput[1]);
		message = message+" (  ."+splitInput[1]+" )";
	}
	var curr = parent.frames[0].document.getElementById("currencyWord").innerHTML;
	// alert(curr);
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
	// alert(x);
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
var isVerify = false;

function displayPaymentDetails(id) {
	var isValid = true;
	var orgType = _id.v("orgType");
	var orgName = _id.v("orgName");
	var retOrgName = _id.v("retOrgName");
	var totalAmount = _id.v("totalAmount");

	var cashAmnt = _id.v("cashAmnt");

	if (orgName == -1) {
		_id.i("orgName1", i18nObj.prop('error.agent.empty'), "e");
		isValid = false;

	} else {
		_id.i("orgName1", "");
	}
	if(orgType=='RETAILER'){
		var e = document.getElementById("retOrgName");
		var agtDisplayName = e.options[e.selectedIndex].text;
		document.getElementById("agentNameValue").value =agtDisplayName;
		if (retOrgName == -1) {
			_id.i("retOrgName1", i18nObj.prop('error.retailer.empty'), "e");
			isValid = false;
		} else {
			_id.i("retOrgName1", "");
		}
	}
	if (totalAmount == "") {
		_id.i("totalAmount1",i18nObj.prop('error.amount.empty'), "e");
		isValid = false;
	} else {
		_id.i("totalAmount1", "");
	}
	if (totalAmount <= 0) {
		_id.i("totalAmount1",i18nObj.prop('error.amount.gt.zero'), "e");
		isValid = false;
	}
	if (isNaN(totalAmount)) {
		_id.i("totalAmount1",i18nObj.prop('error.amount.invalid'), "e");
		isValid = false;
	}
	var amountArr = totalAmount.split('.');
	if (amountArr.length > 1) {
		if (amountArr[1].length > 2) {
			_id.i("totalAmount1",i18nObj.prop('error.amount.invalid'), "e");
			isValid = false;
		}
	}
	var regex = /^ /;
	if (totalAmount.match(regex)) {
		_id.i("totalAmount1",i18nObj.prop('error.amount.invalid'), "e");
		isValid = false;
	}
	if (cashAmnt == "") {
		_id.i("cashAmnt1",i18nObj.prop('error.amount.notverified'));
		isValid = false;
	} else {
		_id.i("cashAmnt1", "");
	}
	if (totalAmount != cashAmnt) {
		_id.i("cashAmnt1",i18nObj.prop('error.amount.notverified.correct'), "e");
		isValid = false;
	}
	// var flag=isPaymentValidation();
	/*
	 * alert(isValid); if (isValid == true) {
	 * alert(document.getElementById('totalAmt')); if
	 * (document.getElementById('totalAmt') != null) { isValid =
	 * validateAmount(); } }
	 */
	// isVerify = false;
	if (isValid == true) {
		if (isVerify == false) {
			if (document.getElementById('totalAmt') != null) {
				alert(i18nObj.prop('error.amount.notverified.submit'));
				isValid = false
			} else {
				isValid = true;
			}
		} else {
			isValid = true;
		}
	}
	if (isValid == true) {
		var amountFlag = localConvertDigitToWord(totalAmount);
		if (!amountFlag) {
			isVerify = false;
			isValid = false;
		} else {
			isValid = _subValid(id);
		}
	}

	return isValid;
}

function isPaymentValidation(){
var frmVal  = new parent.frames[0].Validator("CashForm");

  frmVal.aV("orgType","dontselect=0",i18nObj.prop('error.slct.company.type'),"orgType1");
  
  frmVal.aV("orgName","dontselect=0",i18nObj.prop('error.company.name.empty'),"orgName1");
  
  frmVal.aV("totalAmount","req",i18nObj.prop('error.enter.positive.decimal.amt'),"totalAmount1");
  frmVal.aV("totalAmount","decimal",i18nObj.prop('error.amount.invalid'),"totalAmount1");
  
  frmVal.aV("cashAmnt","req",i18nObj.prop('error.amount.verify'),"cashAmnt1");
  frmVal.aV("cashAmnt","decimal",i18nObj.prop('error.amount.invalid'),"cashAmnt1");
  
 
  
  // alert(document.error_disp_handler.isValid);
return document.error_disp_handler.isValid;

}


function removeMessages( ){
if($("#orgType").val()=='RETAILER'){
	$("#orgType").val("RETAILER");
}else{
	$("#orgType").val("AGENT");
	$("#retName").hide();
}
document.getElementById("orgName1").innerHTML="";
document.getElementById("totalAmount1").innerHTML="";
document.getElementById("cashAmnt1").innerHTML="";
document.getElementById("totalAmount").value="";
document.getElementById("cashAmnt").value="";
document.getElementById("orgName").value=-1;
document.getElementById("retOrgName").value=-1;
document.getElementById("retOrgName1").innerHTML="";
if(document.getElementById("limitDiv"))
document.getElementById("limitDiv").innerHTML="";
if (document.getElementById('totalAmt') != null) {
	if (document.getElementById('totAmtDiv').style.display != 'none') {
			for ( var i = 0; i < arr.length; i++) {
				document.getElementById('multiples_' + arr[i] + '').value = "";
			}
			for ( var i = 0; i < retArr.length; i++) {
				document.getElementById('retMultiples_' + retArr[i] + '').value = "";
				// remove the last item:
				retArr.pop();
				valueArr.pop();
			}
			_id.o('retTable').style.display = "none";
			_id.o('rd').style.display = "none";
			_id.o('totAmtDiv').style.display = "none";
			_id.o('recd').style.display = "none";
			_id.o('table').style.display = "none";
			isRetTabDisplay = false;
			isVerify = false;
			isRecTabDisplay = false;
			document.getElementById("totalAmt").value = "";
		}
	}
return false;
}

function removeData()
{
	document.getElementById("orgName1").innerHTML="";
	document.getElementById("totalAmount1").innerHTML="";
	document.getElementById("cashAmnt1").innerHTML="";
	document.getElementById("totalAmount").value="";
	document.getElementById("cashAmnt").value="";
	if (document.getElementById('totalAmt') != null) {
	if(document.getElementById('totAmtDiv').style.display!='none')
	{

		for ( var i = 0; i < arr.length; i++) {
			document.getElementById('multiples_' + arr[i] + '').value = "";
		}
		for ( var i = 0; i < retArr.length; i++) {
			document.getElementById('retMultiples_' + retArr[i] + '').value = "";
			// remove the last item:
	        retArr.pop();
	        valueArr.pop();
		}
		_id.o('retTable').style.display = "none";
		_id.o('rd').style.display = "none";
		_id.o('totAmtDiv').style.display = "none";
		_id.o('recd').style.display = "none";
		_id.o('table').style.display = "none";
		isRetTabDisplay = false;
		isRecTabDisplay = false;
		isVerify = false;
		document.getElementById("totalAmt").value = "";
	}
	}
	return false;
}


function removeAlerts(){
document.getElementById("chequeNumber21").innerHTML="";
document.getElementById("chequeNumber2").value="";
return false;
}

// mtehods added for bank deposit validation by amit

function displayPaymentDetailsForBD(id){
	// alert("kjhfjksdfhjk "+id.value);
	var flag=isPaymentValidationBD();
	if(document.getElementById("totalAmount").value!=document.getElementById("cashAmnt").value){
	flag=false;
	document.getElementById("cashAmnt1").innerHTML="<font color = 'red'>"+i18nObj.prop('error.amount.notverified.correct')+"</font>";
	}
	else if(document.getElementById("totalAmount").value==0 ||document.getElementById("cashAmnt").value==0 ){
	flag=false;
	document.getElementById("cashAmnt1").innerHTML="<font color = 'red'> "+i18nObj.prop('error.total.amount.gt.zero')+"</font>";

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

function isPaymentValidationBD(){
	var frmVal  = new parent.frames[0].Validator("Bank_Deposit_form");

	  frmVal.aV("orgName","dontselect=0",i18nObj.prop('error.agent.empty'),"orgName1");
	  if($("#orgType").val()=='RETAILER'){
	  frmVal.aV("retOrgName","dontselect=0",i18nObj.prop('error.retailer.empty'),"retOrgName1");
	  }
	  frmVal.aV("receiptNum","req",i18nObj.prop('error.enter.rcpt.no'),"receiptNum1");
	  
	  frmVal.aV("receiptNum","alnum",i18nObj.prop('error.enter.valid.rcpt.no'),"receiptNum1");
	  frmVal.aV("bankName","req",i18nObj.prop('error.enter.bank.name'),"bankName1");
	  //frmVal.aV("bankName", "regexp=[^A-Za-z\\s&.]",i18nObj.prop('error.enter.bank.name'), "bankName1");
	  frmVal.aV("branchName","alnum_s",i18nObj.prop('error.enter.branch.name'),"branchName1");
	  frmVal.aV("totalAmount","req",i18nObj.prop('error.enter.positive.decimal.amt'),"totalAmount1");
	  frmVal.aV("totalAmount","decimal",i18nObj.prop('error.amount.invalid'),"totalAmount1");
	  
	  frmVal.aV("cashAmnt","req",i18nObj.prop('error.amount.verify'),"cashAmnt1");
	  frmVal.aV("cashAmnt","decimal",i18nObj.prop('error.amount.invalid'),"cashAmnt1");
	  
	 
	  
	  // alert(document.error_disp_handler.isValid);
	return document.error_disp_handler.isValid;

	}

function removeMessagesBD( ){
	document.getElementById("orgName1").innerHTML="";
	document.getElementById("retOrgName1").innerHTML="";
	document.getElementById("cashAmnt1").innerHTML="";
	document.getElementById("totalAmount").value="";
	document.getElementById("cashAmnt").value="";
	document.getElementById("orgName").value=-1;
	document.getElementById("retOrgName").value=-1;
	document.getElementById("bankName").value="";
	document.getElementById("receiptNum").value="";
	document.getElementById("bankName1").innerHTML="";
	document.getElementById("receiptNum1").innerHTML="";
	document.getElementById("branchName").value="";
	$("#totalAmount1").html("");
	if($("#orgType").val()=='RETAILER'){
		$("#orgType").val("RETAILER");
	}else{
		$("#orgType").val("AGENT");
		$("#retName").hide();
	}	
	if(document.getElementById("limitDiv"))
	document.getElementById("limitDiv").innerHTML="";
	return false;
	}

function hideAndShow(){
	if($("#orgType").val()=='RETAILER'){
		$("#retName").show();
		$("#retOrgName1").html("");
		$("#orgName").val("-1");
		$("#retOrgName").val("-1");
		$("#limitDiv").html("");
	}else{
		$("#retName").hide();
		$("#retOrgName1").html("");
		$("#orgName").val("-1");
		$("#limitDiv").html("");
	}
}
function verifyAgentBD(agtName) {
	$("#limitDiv").html("");
	document.getElementById("orgName1").innerHTML="";
	var e = document.getElementById("orgName");
	var agtDisplayName = e.options[e.selectedIndex].text;
	// alert(agtDisplayName+"asa");
	document.getElementById("agentNameValue").value =agtDisplayName;
	// alert(document.getElementById("agentNameValue").value+"121as1a");
// pass selected value to display on Details Page
	if($("#orgType").val()=='AGENT'){
 fetchLimits(agtName,agtDisplayName);
	}
	  if(document.getElementById('orgName').value!="-1")
	  {
	  var returnType=confirm(i18nObj.prop('error.u.hav.slect')+tierMap['AGENT']+":: " + agtDisplayName)
	  if(returnType)    
	    {     
	    document.getElementById('receiptNum').focus();
	    // return true;
	    }
	   else
	   {
	       document.getElementById('orgName').selectedIndex=0;
	       document.getElementById('orgName').focus();
	       // return false;
	   }
	  }  
	}
var isRetTabDisplay = false;
var isRecTabDisplay = false;
var retArr = new Array();
var valueArr = new Array();
var arr = new Array();
function appendDenoTab(mode) {
	isVerify = false;
	if (mode == 'Rec' && document.getElementById('totalAmt') != null) {
		if (isRecTabDisplay == false) {
			var table = document.getElementById("table");
			for ( var i = table.rows.length - 1; i > 0; i--) {
				table.deleteRow(i);
			}
			document.getElementById("cashAmnt1").innerHTML = "";
			_id.o('totAmtDiv').style.display = "block";
			_id.o('recd').style.display = "block";
			_id.o('table').style.display = "block";
			_id.o('verifyButtonDiv').style.display = "block";
			addRecArrayDenomination();
			for ( var i = 0; i < arr.length; i++) {
				addReceivedDenomination(arr[i]);
			}
			isRecTabDisplay = true;
		}
	} else {
		isRetTabDisplay = true;
		for ( var i = 0; i < retArr.length; i++) {
			addReturnDenomination(retArr[i], valueArr[i]);
		}
	}
}
function addReceivedDenomination(deno) {
	var div = document.getElementById("table");
	var row = document.createElement("tr");
	var col1 = document.createElement("td");
	col1.setAttribute("style", "color: navy");
	var col2 = document.createElement("td");
	col1.setAttribute("align", "right");
	col1.setAttribute("width", "160");
	col2.setAttribute("width", "100");
	var	tdHtmlData1 = '<div style="background: transparent;border-width: 0px;color: navy;">'+deno+'   x </div>';
	// var tdHtmlData1 = '<input type="text" name="denoType" value="' + deno +
	// '" size="3" style="background: transparent;border-width: 0px;color:
	// navy;" readonly="readonly" />x';
	var tdHtmlData2 = '<input type="text" name="multiples" id="multiples_'
			+ deno
			+ '" size="6"  maxlength="10"  onkeypress="validateComm(event)" onChange="setRecAmt(\'multiples_'
			+ deno + '\');"></input>';
	col1.innerHTML = tdHtmlData1;
	col2.innerHTML = tdHtmlData2;
	row.appendChild(col1);
	row.appendChild(col2);
	div.appendChild(row);
}
function addReturnDenomination(deno, val) {
	var div = document.getElementById("retTable");
	var row = document.createElement("tr");
	var col1 = document.createElement("td");
	col1.setAttribute("style", "color: navy");
	var col2 = document.createElement("td");
	col1.setAttribute("align", "right");
	col1.setAttribute("width", "160");
	col2.setAttribute("width", "100");
	var tdHtmlData1 = '<input type="text" name="retDenoType" value="' + deno + '" size="3" style="background: transparent;border-width: 0px;color: navy;" readonly="readonly"  />x';
	var tdHtmlData2 = '<input type="text" name="retMultiples" id="retMultiples_'
			+ deno
			+ '" size="6" maxlength="10" onkeypress="validateComm(event)" value="'
			+ val + '" onChange="setRetAmt();"></input>';
	col1.innerHTML = tdHtmlData1;
	col2.innerHTML = tdHtmlData2;
	row.appendChild(col1);
	row.appendChild(col2);
	div.appendChild(row);
}

function setRecAmt(buttonId) {
	if (isRetTabDisplay) {
		_id.o('retTable').style.display = "none";
		_id.o('rd').style.display = "none";
		calculateTotalDenoAmount();

	}
	document.getElementById('totalAmt').value = 0;
	var totAmt = 0;
	for ( var i = 0; i < arr.length; i++) {
		var multiples = document.getElementById('multiples_' + arr[i] + '').value;
		if (multiples == "") {
			multiples = 0;
		}
		totAmt = parseInt(totAmt) + (arr[i] * multiples);
	}
	document.getElementById('totalAmt').value = totAmt;
	isVerify = false;
}
var k = 0;
var amount = 0;
function setRetAmt() {

	var totAmt = 0;

	for ( var i = 0; i < retArr.length; i++) {
		var multiples = document
				.getElementById('retMultiples_' + retArr[i] + '').value;
		if (multiples == "") {
			multiples = 0;
		}
		totAmt = parseInt(totAmt) - (retArr[i] * multiples);
	}
	if (k == 0) {

		amount = document.getElementById('totalAmt').value;
		k = k + 1;
		if (amount == "") {
			amount = 0;
		}

	}

	document.getElementById('totalAmt').value = totAmt + parseInt(amount);
	isVerify = false;
}
var popupFlag = false;
function validateAmount() {
	popupFlag = false;
	if (document.getElementById('totAmtDiv').style.display != 'none') {
		RefreshRetDenoTable();
		if (k == 1) {
			amount = document.getElementById('totalAmt').value;
		}
		calculateTotalDenoAmount();

		var gt = false;
		var lt = false;
		var denoTotAmt = document.getElementById('totalAmt').value;
		var totalAmt = document.getElementById('totalAmount').value;
		if (totalAmt == "") {
			totalAmt = 0;
		}
		if (denoTotAmt == "") {
			denoTotAmt = 0;
		}

		if (denoTotAmt != parseInt(totalAmt) && totalAmt != 0) {
			gt = parseInt(denoTotAmt) > parseInt(totalAmt);
			lt = parseInt(denoTotAmt) < parseInt(totalAmt);
			if (gt) {
				alert(i18nObj.prop('error.entered.cash.lt.amt.calculated.denom'));

				if (isRetTabDisplay == false) {
					var flag = confirm(i18nObj.prop('msg.kind.ret.denom')+"..");
					if (!flag) {
						isVerify = false;
						return false;
					} else {
						_id.o('retTable').style.display = "block";
						_id.o('rd').style.display = "block";
						addRetArrayDenomination(totalAmt - denoTotAmt);
						appendDenoTab('Ret');
					}
				}
			} else if (lt) {
				alert(i18nObj.prop('error.enter.proper.denom')+"... ");
			}
			isVerify = false;
			return false;
		} else {
			isVerify = true;
			if (popupFlag == false)
				alert(i18nObj.prop('msg.amt.verify.success')+"... ");
			return true;
		}
	}
}
function addRetArrayDenomination(retAmt) {
	var table = document.getElementById("retTable");
	for ( var i = table.rows.length - 1; i > 0; i--) {
		table.deleteRow(i);
	}
	retArr = new Array();
	valueArr = new Array();
	var url = "bo_act_add_RetArray_Denomination.action?retAmout=" + retAmt;
	var _resp = _ajaxCall(url);
	if (_resp.result && _resp.data != "") {
		var _respData = _resp.data;
		var data = _respData.split(",");
		for ( var i = 0; i < data.length; i = i + 1) {
			var arr1 = data[i].split("=");
			retArr.push(arr1[0]);
			if (arr1[1] == 0)
				valueArr.push("");
			else
				valueArr.push(arr1[1]);
		}
	}
}

function calculateTotalDenoAmount() {
	var pAmt = 0;
	var nAmt = 0;
	var multiples = "";
	var retMultiples = "";
	for ( var i = 0; i < arr.length; i++) {
		multiples = document.getElementById('multiples_' + arr[i] + '').value;
		if (multiples == "") {
			multiples = 0;
		}
		pAmt = parseInt(pAmt) + (arr[i] * multiples);
	}
	for ( var i = 0; i < retArr.length; i++) {
		retMultiples = document
				.getElementById('retMultiples_' + retArr[i] + '').value;
		if (retMultiples == "") {
			retMultiples = 0;
		}
		nAmt = parseInt(nAmt) - (retArr[i] * retMultiples);
	}

	document.getElementById('totalAmt').value = pAmt + nAmt;
}

function validateComm(evt) {
	var theEvent = evt || window.event;
	var key = theEvent.keyCode || theEvent.which;
	  if (key == 13 || key==8 || key==9) {
		return true;
	} else {
		key = String.fromCharCode(key);
		var regex = /[0-9]/;
		if (!regex.test(key)) {
			theEvent.returnValue = false;
			alert("Please enter valid Entry");
			if (theEvent.preventDefault)
				theEvent.preventDefault();
		}
	}
}
function RefreshRetDenoTable() {

	if (isRetTabDisplay == true) {
		var denoTotAmt = document.getElementById('totalAmt').value;
		var totalAmt = document.getElementById('totalAmount').value;

		if (totalAmt == "") {
			totalAmt = 0;
		}
		if (denoTotAmt == "") {
			denoTotAmt = 0;
		}

		gt = parseInt(denoTotAmt) > parseInt(totalAmt);
		lt = parseInt(denoTotAmt) < parseInt(totalAmt);
		if (gt) {
			for ( var i = 0; i < retArr.length; i++) {
				document.getElementById('retMultiples_' + retArr[i] + '').value = 0;
			}
			isVerify = false;

			calculateTotalDenoAmount();
			denoTotAmt = document.getElementById('totalAmt').value;
			totalAmt = document.getElementById('totalAmount').value;

			addRetArrayDenomination(totalAmt - denoTotAmt);
			appendDenoTab('Ret');
			_id.o('retTable').style.display = "block";
			_id.o('rd').style.display = "block";
			popupFlag = true;
		} else if (lt) {
			_id.o('retTable').style.display = "none";
			_id.o('rd').style.display = "none";
			for ( var i = 0; i < retArr.length; i++) {
				document.getElementById('retMultiples_' + retArr[i] + '').value = 0;
			}
			isVerify = false;

			calculateTotalDenoAmount();
			denoTotAmt = document.getElementById('totalAmt').value;
			totalAmt = document.getElementById('totalAmount').value;

			if (totalAmt < denoTotAmt) {
				addRetArrayDenomination(totalAmt - denoTotAmt);
				appendDenoTab('Ret');
				_id.o('retTable').style.display = "block";
				_id.o('rd').style.display = "block";
				popupFlag = true;
			}
		}
	}
}
function addRecArrayDenomination() {
	arr = new Array();
	var url = "bo_act_add_RecArray_Denomination.action";
	var _resp = _ajaxCall(url);
	if (_resp.result && _resp.data != "") {
		var _respData = _resp.data;
		var data = _respData.split(",");
		
		for ( var i = 0; i < data.length; i = i + 1) {
			arr.push(data[i]);
		}
	}
}


function getAgentList() {
	//$("#retName").hide();
	var text = _ajaxCall(projectName+"/com/skilrock/lms/web/userMgmt/getAgentNameList.action?orgType=AGENT");
	var firstArr = text.data.split(":");
	var retObj = _id.o("orgName");
	if(firstArr.length<1){
		_id.o("divFrm").style.display="none";
		_id.o("orgData").style.display="block";
	}
	for ( var i = 0; i < firstArr.length - 1; i=i+1) {
		var retNameArray = firstArr[i].split("|");
		var opt = new Option(retNameArray[0].toUpperCase(), retNameArray[1]);
		retObj.options[i + 1] = opt;
	}
}
function getRetList() {
	var agtId = _id.o("orgName");
	var text = _ajaxCall(path + "/com/skilrock/lms/web/userMgmt/fetchRetOrgList.action?orgId=" + agtId.options[agtId.selectedIndex].value);
	var firstArr = text.data.split(":");
	var retObj = _id.o("retOrgName");
	retObj.options.length = 1;
	for (var i = 0; i < firstArr.length-1; i++) {
		var retNameArray = firstArr[i].split("|");
		var opt = new Option(retNameArray[0].toUpperCase(), retNameArray[1]);
		retObj.options[i + 1] = opt;
	}
}
function getLMCId(){
	_id.o('lmctag').style.display = 'none';
	_id.o('lmcId').style.display = 'none';
	_id.o('rettag').style.display = 'none';
	_id.o('retailerId').style.display = 'none';
	 var retname=_id.o('orgName').value;
	if(retname==''){
	_id.i("retId", "Please Select Organization", "e");
	} else {
	_id.i("retId", "");
		var _resp  = _ajaxCall("geLMCID.action?orgName="+retname);
			var data = _resp.data;
			if(data.split(':')[0]=='Does Not Exist' || data.split(':')[1]=='Does Not Exist'){
			_id.o('lmcId').innerHTML='<b>'+data.split(':')[0]+'</b>';
			_id.o('retailerId').innerHTML='<b>'+data.split(':')[1]+'</b>';
			_id.o('lmcId').style.display = 'block';
			_id.o('retailerId').style.display = 'block';
			_id.o('lmctag').style.display = 'block';
			_id.o('rettag').style.display = 'block';
			}else if(data.split(':')[0]=='ERROR' || data.split(':')[1]=='ERROR'){
				_id.o('lmcId').innerHTML='<b>CONTACT TO BACK OFFICE...!!!</b>';
				_id.o('lmcId').style.display = 'block';
			}else{
	 var dataArr  = data.split(':');		
	_id.o('lmcId').innerHTML='<b>'+dataArr[0].split('|')[0]+'</b>';
	_id.o('retailerId').innerHTML='<b>'+dataArr[1].split('|')[0]+'</b>';
	_id.o('agtOrgId').value =dataArr[0].split('|')[1];
	_id.o('retOrgId').value =dataArr[1].split('|')[1];
	_id.o('sb').style.display = 'block';
	_id.o('lmctag').style.display = 'block';
	_id.o('lmcId').style.display = 'block';
	_id.o('rettag').style.display = 'block';
	_id.o('retailerId').style.display = 'block';
	}
	}
	}
	
		var retData;
		function fillRetailersId(){
			_id.o('orgName').value = '';
			AutoComplete_Create('orgName', '');
			var _resp  = _ajaxCall('geRetOrgNames.action');
			var retData = _resp.data;
			var data = _resp.data.split(':');
			AutoComplete_Create('orgName', data);
			
		}
		
		function isValidate(val){
		var isNameValid = false; 
		var dataArr = retData.split(':');
		for(var k=0; k<dataArr.length; k++){
		if(val == dataArr[k]){
			isNameValid = true;
		}
		}
		return isNameValid;	
		}
	
function validateTwoWayCashDep(id) {
var isValid = true;
var orgName = _id.v("orgName");
var totalAmount = _id.v("amount");
var totalAmount1 = _id.v("amount1");

if (orgName == '') {
	_id.i("retId",i18nObj.prop('error.orgname.empty'), "e");
	isValid=false;

} else {
	_id.i("retId", "");
}

if (totalAmount == '') {
	_id.i("amt",i18nObj.prop('error.amount.empty'), "e");
	isValid=false;
} else {
	_id.i("amt", "");
}

if(totalAmount>10000 || totalAmount<-10000){
	_id.i("amt",i18nObj.prop('error.ur.lmt.from.mtt.to.tt'), "e");
	isValid=false;
	return isValid;
}else{
	_id.i("amt", "");
}


var regex = /^-?[0-9]\d*(\.\d+)?$/;

if (totalAmount.match(regex) && totalAmount!=0) {
	_id.i("amt", "");
	if(totalAmount.split('.').length > 2)
	{
		totalAmount =totalAmount.replace(/\.+$/,"");
	}
}
else
{
	_id.i("amt", i18nObj.prop('error.amount.invalid'), "e");
	isValid=false;
	}

var amountArr = totalAmount.split('.');
if (amountArr.length > 1) {
	if (amountArr[1].length > 2) {
		_id.i("amt",i18nObj.prop('error.only.two.decimal.allow'), "e");
		isValid=false;
	}
}

if (totalAmount1 == '') {
	_id.i("amt1",i18nObj.prop('error.amount.notverified'));
	isValid=false;
} else {
	_id.i("amt1", "");
}
if (totalAmount != totalAmount1) {
	_id.i("amt1",i18nObj.prop('error.amount.notverified.correct'), "e");
	isValid=false;
}

if(totalAmount.indexOf("-")!=-1){
	if(totalAmount.lastIndexOf('-')>0){
	_id.i("amt", i18nObj.prop('error.amount.invalid'), "e");
		isValid=false;
	}else{
	if(totalAmount.length==1){
	_id.i("amt", i18nObj.prop('error.amount.invalid'), "e");
		isValid=false;
	}else{
	_id.i("amt", "");
	}
}
}


if(isValid){
	var message = i18nObj.prop('msg.credit.upload.cnf')+" \n\n"+ i18nObj.prop('msg.verify.agt.no.below.amt')+ "\n"+i18nObj.prop('msg.before.cnf.credit.sale')+"\n\n " + i18nObj.prop('label.tid')+" :" +orgName+"\n "+i18nObj.prop('label.amt')+" : GHS "+totalAmount;
 	isValid = confirm(message);
 }
return isValid;
}		

function hideSubmit(){
	_id.o('lmctag').style.display = 'none';
	_id.o('lmcId').style.display = 'none';
	_id.o('rettag').style.display = 'none';
	_id.o('retailerId').style.display = 'none';
	_id.o('sb').style.display = 'none';
	_id.o('amount').value = '';
	_id.o('amount1').value = '';
}

function removeTwoWayDepMessages(){
	_id.o('lmcId').innerHTML = '';
	_id.o('retailerId').innerHTML = '';
	_id.o('lmctag').style.display = 'none';
	_id.o('lmcId').style.display = 'none';
	_id.o('rettag').style.display = 'none';
	_id.o('retailerId').style.display = 'none';
	_id.o('sb').style.display = 'none';
	_id.o('amount').value = '';
	_id.o('amount1').value = '';
	_id.o('orgName').value = '';
	_id.o('retId').innerHTML = '';
	_id.o('amt').innerHTML = '';
	_id.o('amt1').innerHTML = '';
	
}

function onlyDecimalForNeg(evt) {
	var code = (document.all) ? event.keyCode:evt.which;
	var e = event || evt;
	if ((code >= 48 && code < 58) || (code >= 96 && code < 105) || (code == 46 || code == 8 || code == 37 || code == 39 || code == 45) || (code == 190 || code == 189 || code == 9 || code == 16)) {
			if(code == 190){
			var temp = e.srcElement.value;
			if ((temp.split(".")).length > 1) {
				return false;
				 }
			}
			if(code == 189){
				var temp = e.srcElement.value;
				if ((temp.split("-")).length > 1) {
					return false;
				}
			}
			return true;
	}
	return false;
}

parent.frames[0].Version['$Source: /rep/LMS_Mgmt/WebRoot/com/skilrock/lms/web/accMgmt/backOffice/js/ACT_ST5_Validation.js,v $'] = '$Id: ACT_ST5_Validation.js,v 1.3 2016/10/31 09:46:21 neeraj Exp $';
