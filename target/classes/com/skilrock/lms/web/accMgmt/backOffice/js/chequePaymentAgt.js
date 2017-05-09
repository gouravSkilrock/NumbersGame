

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
		document.getElementById('orgType1').innerHTML = "<font color = 'red'>Please Select Company Type </font>";
		flag=false;
    }

    else{
    document.getElementById('orgType1').innerHTML = "";
    }

if ( document.cashChqForm.orgName.value == "-1" )
    {
       // alert ( "Please select Printer." );
		document.getElementById('orgName1').innerHTML = "<font color = 'red'>Please Select Company Name </font>";
	 flag=false;
    }
    else{
    document.getElementById('orgName1').innerHTML = "";
}
// alert(flag);
if(!isDecimal(totalAmount))
{
document.getElementById('totalAmount1').innerHTML = "<font color = 'red'>Please Enter Positive Decimal value for  Amount </font>";
flag=false;
}
else{
document.getElementById('totalAmount1').innerHTML = "";
}


if(!isDecimal(cashAmnt))
{ //alert("in cheque verify");
document.getElementById('cashAmnt1').innerHTML = "<font color = 'red'>Please  Verify Cash Amount </font>";
flag=false;
}
else{
document.getElementById('cashAmnt1').innerHTML = "";
}

if(cashAmnt.value!=totalAmount.value)
{   //alert("in cheque amount validation");
document.getElementById('cashAmnt1').innerHTML = "<font color = 'red'>Please verify Cash Amount field</font>";
flag=false;
}
else{
document.getElementById('cashAmnt1').innerHTML = "";
}
if(!isNumeric(chequeNumber,"Enter 6 to 10 digits Numeric value for Cheque Number"))
 {//alert(flag);
 
     flag=false;
 }else if(lengthRestriction(chequeNumber,6,10))
 {//alert(flag);
 document.getElementById('chequeNumber1').innerHTML = "<font color = 'red'>Enter 6 to 10 digits Numeric value for Cheque Number ::::::::</font>";
     flag=false;
 }else if(chequeNumber.value == '000000'){
 document.getElementById('chequeNumber1').innerHTML = "<font color = 'red'>Enter Valid Cheque Number </font>";
 flag=false;
 }
else{
document.getElementById('chequeNumber1').innerHTML = "";
}

if(!isAlphabet(issuePartyname,"Please Enter correct for Issueing Party Name"))
{ flag=false;
}

if(!isAlphabet(bankName,"Please Enter correct for Bank Name"))
{ flag=false;
}
//alert("After Bank"+flag);
if(!isDecimal(chequeAmount))
{  // alert("in cheque amount validation");
document.getElementById('chequeAmount1').innerHTML = "<font color = 'red'>Please Enter Positive Decimal value for Cheque Amount </font>";
flag=false;
}
else{
document.getElementById('chequeAmount1').innerHTML = "";
}
if(!isDecimal(verifychequeAmount))
{ //alert("in cheque verify");
document.getElementById('verifyAmnt').innerHTML = "<font color = 'red'>Please  Verify Cheque Amount </font>";
flag=false;
}
else{
document.getElementById('verifyAmnt').innerHTML = "";
}

if(verifychequeAmount.value!=chequeAmount.value)
{   //alert("in cheque amount validation");
document.getElementById('verifyAmnt').innerHTML = "<font color = 'red'>Please Verify Cheque Amount field</font>";
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
		document.getElementById('id1').innerHTML = "<font color = 'red'>Please Select Company Type </font>";
		flag=false;
    }

    else{
    document.getElementById('id1').innerHTML = "";
    }

if ( document.cashChqRetForm.orgName.value == "-1" )
    {
       // alert ( "Please select Printer." );
		document.getElementById('id2').innerHTML = "<font color = 'red'>Please Select Company Name </font>";
	 flag=false;
    }
    else{
    document.getElementById('id2').innerHTML = "";
}
// alert(flag);
if(!isDecimal(totalAmountRetailer))
{
document.getElementById('totalAmount111').innerHTML = "<font color = 'red'>Please Enter Positive Decimal value for  Amount </font>";
flag=false;
}
else{
document.getElementById('totalAmount111').innerHTML = "";
}

if(!isDecimal(verifyAmnt))
{ //alert("in cheque verify");
document.getElementById('verifyAmnt1').innerHTML = "<font color = 'red'>Please  Verify Cash Amount </font>";
flag=false;
}
else{
document.getElementById('verifyAmnt1').innerHTML = "";
}

if(verifyAmnt.value!=totalAmountRetailer.value)
{   //alert("in cheque amount validation");
document.getElementById('verifyAmnt1').innerHTML = "<font color = 'red'>Please verify Cash Amount field</font>";
flag=false;
}
else{
document.getElementById('verifyAmnt1').innerHTML = "";
}

if(!isAlphabet(issuePartyname,"Please Enter correct for Issueing Party Name"))
{ flag=false;
}

if(!isAlphabet(bankName,"Please Enter correct for Bank Name"))
{ flag=false;
}
//alert("After Bank"+flag);

if(!isNumeric(chequeNumber,"Enter 6 to 10 digits Numeric value for Cheque Number"))
 {//alert(flag);
 
     flag=false;
 }else if(lengthRestriction(chequeNumber,6,10))
 {//alert(flag);
 document.getElementById('chequeNumber1').innerHTML = "<font color = 'red'>Enter 6 to 10 digits Numeric value for Cheque Number </font>";
     flag=false;
 }else if(chequeNumber.value == '000000'){
 document.getElementById('chequeNumber1').innerHTML = "<font color = 'red'>Enter Valid Cheque Number </font>";
 flag=false;
 }
else{
document.getElementById('chequeNumber1').innerHTML = "";
}


if(!isDecimal(chequeAmount))
{  // alert("in cheque amount validation");
document.getElementById('chequeAmount1').innerHTML = "<font color = 'red'>Please Enter Positive Decimal value for Cheque Amount </font>";
flag=false;
}
else{
document.getElementById('chequeAmount1').innerHTML = "";
}
if(!isDecimal(verifyAmount))
{ //alert("in cheque verify");
document.getElementById('verifyAmount1').innerHTML = "<font color = 'red'>Please  Verify Cheque Amount </font>";
flag=false;
}
else{
document.getElementById('verifyAmount1').innerHTML = "";
}

if(verifyAmount.value!=chequeAmount.value)
{   //alert("in cheque amount validation");
document.getElementById('verifyAmount1').innerHTML = "<font color = 'red'>Please Verify Cheque Amount field</font>";
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







function ST5_Validation_Cheque(){
	//alert('fgdfg');
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
		document.getElementById('orgType11').innerHTML = "<font color = 'red'>Please Select Company Type </font>";
		flag=false;
    }

    else{
    document.getElementById('orgType11').innerHTML = "";
    }

if ( document.chequeForm.orgName.value == "-1" )
    {
 // alert ( "Please select Printer." );
		document.getElementById('orgName11').innerHTML = "<font color = 'red'>Please Select Company Name </font>";
	 flag=false;
    }
   else{
    document.getElementById('orgName11').innerHTML = "";
    }
   
 //alert(flag);
 if(!isNumeric(chequeNumber,"Enter 6 to 10 digits Numeric value for Cheque Number"))
 {//alert(flag);
 
     flag=false;
 }else if(lengthRestriction(chequeNumber,6,10))
 {//alert(flag);
 document.getElementById('chequeNumber1').innerHTML = "<font color = 'red'>Enter 6 to 10 digits Numeric value for Cheque Number </font>";
     flag=false;
 }else if(chequeNumber.value == '000000'){
 document.getElementById('chequeNumber1').innerHTML = "<font color = 'red'>Enter Valid Cheque Number </font>";
 flag=false;
 }
else{
document.getElementById('chequeNumber1').innerHTML = "";
}

if(!isAlphabet(issuePartyname,"Please Enter correct for Issuing Party Name") )
{ flag=false;
}

if(!isAlphabet(bankName,"Please Enter correct for Bank Name") )
{ flag=false;
}
//alert("After Bank"+flag);
if(isNumericAmt(chequeAmount.value)==true || chequeAmount.value==0 || chequeAmount.value==".")
{  // alert("in cheque amount validation");
document.getElementById('chequeAmount1').innerHTML = "<font color = 'red'>Please Enter Positive Decimal value for Cheque Amount </font>";
flag=false;
}
else{
	var amountArr = chequeAmount.value.split('.');
	if(amountArr.length > 1){
		if(amountArr[1].length > 2){
			document.getElementById("chequeAmount1").innerHTML = "<font color = 'red'>Amount upto Two Decimal Point is Allowed</font>";
			flag = false;  
		} else {
			document.getElementById("chequeAmount1").innerHTML = "";
      	}
	} else {
		document.getElementById("chequeAmount1").innerHTML = "";
	}
}
if(!isDecimal(verifychequeAmount))
{ //alert("in cheque verify");
document.getElementById('verifyAmnt').innerHTML = "<font color = 'red'>Please  Verify Cheque Amount </font>";
flag=false;
}
else{
document.getElementById('verifyAmnt').innerHTML = "";
}

if(verifychequeAmount.value!=chequeAmount.value)
{   //alert("in cheque amount validation");
document.getElementById('verifyAmnt').innerHTML = "<font color = 'red'>Please Verify Cheque Amount field</font>";
flag=false;
}
else{
document.getElementById('verifyAmnt').innerHTML = "";
}



//commented by arun because of no need of date validation in new calender implementation

//alert("After cheque Amount"+flag);
//if(!isValidateDate())
//{
//flag=false;
//}






if(flag) {
 	var isTrue = convertDigitToWord(chequeAmount.value);
 	if(!isTrue) {
 		flag = false;
 		chequeAmount.focus();
 	}
 }

//alert(flag);
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
  var chequeDate = document.getElementById("chequeDate");
 // var chqStDate=dojo.widget.byId('chqStdate').getValue();
//  alert(document.getElementById("chqStdate").value);
  //alert(chqStDate);
  var cDate = chequeDate.value;
  alert("hfjkhjfhkj"+cDate);
   
   if(cDate=="" || cDate==null) { 
   		alert("2222222222");
  		document.getElementById('chequeDate1').innerHTML = "<font color = 'red'>Please Enter Cheque Date</font>";
   }
  else {
   		document.getElementById('chequeDate1').innerHTML = "";
   		flag1=true;
   }
   if(cDate=="" || cDate==null || cDate.toString()<document.getElementById("chqStdate").value){
   		alert(cDate.toString()+"::"+document.getElementById("chqStdate").value);
        document.getElementById('chequeDate1').innerHTML = "<font color = 'red'>Cheque Date can not be of before  six months</font>";
   }
	else{
   document.getElementById('chequeDate1').innerHTML = "";
   flag1=true;
   }
return flag1;

  }
  
  
function removeMsgs(){
document.getElementById("bankName1").innerHTML="";
document.getElementById("chequeNumber1").innerHTML="";
document.getElementById("issuePartyname1").innerHTML="";
document.getElementById("chequeAmount1").innerHTML="";
document.getElementById("verifyAmnt").innerHTML="";
document.getElementById('orgName11').innerHTML = "";
document.getElementById("chequeNumber").value="";
document.getElementById("issuePartyname").value="";
document.getElementById("bankName").value="";
document.getElementById("chequeAmount").value="";
document.getElementById("verifychequeAmount").value="";
//document.getElementById("orgType").value=-1;
document.getElementById("orgName").value=-1;
if(document.getElementById("limitDiv"))
document.getElementById("limitDiv").innerHTML="";
orgName
return false;
}

function getAgentList() {
	var text = _ajaxCall(projectName+"/com/skilrock/lms/web/userMgmt/getAgentNameList.action?orgType=AGENT");
	var firstArr = text.data.split(":");
	var retObj = _id.o("orgName");
	
	for ( var i = 0; i < firstArr.length - 1; i=i+1) {
		var retNameArray = firstArr[i].split("|");
		var opt = new Option(retNameArray[0].toUpperCase(), retNameArray[1]);
		retObj.options[i + 1] = opt;
	}
}

function verifyAgent(agtName)
{
	var e = document.getElementById("orgName");
	var agtDisplayName = e.options[e.selectedIndex].text;
	//alert(agtDisplayName+"asa");
	document.getElementById("agentNameValue").value =agtDisplayName;
	//alert(document.getElementById("agentNameValue").value+"121as1a");
// pass selected value to display on Details Page	
 fetchLimits(agtName,agtDisplayName);

if(document.getElementById('orgName').value!="-1")
{
var returnType=confirm("You have selected "+tierMap['AGENT']+":: " + agtDisplayName)
if(returnType)    
{     
document.getElementById('chequeNumber').focus();
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


parent.frames[0].Version['$Source: /rep/LMS_Mgmt/WebRoot/com/skilrock/lms/web/accMgmt/backOffice/js/chequePaymentAgt.js,v $'] = '$Id: chequePaymentAgt.js,v 1.2 2016/10/31 09:46:21 neeraj Exp $';





