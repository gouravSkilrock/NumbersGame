var i18nOb = i18Obj;

function govtRule() {
  //alert("inside function");
	var govtrule = document.getElementById("commRule");
 //alert(govtrule.value);
	if (govtrule.value == "fixedper") {
  //alert("inside fixedper");
		var name = document.getElementsByName("minAssProfit")[0];
		name.disabled = (name.disabled == true) ? false : true;
    
    //document.getElementById("minAssuredProfit").style.display='block';
  	//alert("hhhh");  
	} else {
		if (govtrule.value == "not") {
   //alert("gggg");
			document.getElementById("govtCommRate1").style.display = "block";
			document.getElementById("minAssuredProfit").style.display = "block";
		}
	}
}

function onLoadFn(url){
	_resp  = _ajaxCall(url);
	if(_resp.result){
	_respData=_resp.data;
	initialUploadDetails(_respData);
	}
}
function initialUploadDetails(outputResponse){

			var arr = new Array();
			arr = outputResponse.split(":");
			
			var agentSaleCommRate = arr[0];
			var agentPWTCommRate = arr[1];
			var retailerSaleCommRate = arr[2];
			var retailerPWTCommRate = arr[3];
			var commRule = arr[4];
			
			document.getElementById("agentSaleCommRate").value=agentSaleCommRate;
			document.getElementById("agentPWTCommRate").value=agentPWTCommRate;
			document.getElementById("retailerSaleCommRate").value=retailerSaleCommRate;
			document.getElementById("retailerPWTCommRate").value=retailerPWTCommRate;
			document.getElementById("commRule").value=commRule;
			
			govtRule();
}

function retrieveURL() {
	var goFlag = CheckBookPack();
	if (!goFlag) {
		return false;
	}
	var bookNbr = document.getElementById("booksperPack").value;
	var ticketNbr = document.getElementById("ticketpetBook").value;

	var url = "bo_gm_newGame_GenerateFormat.action?bookaNbr=" + bookNbr + "&ticketNbr=" + ticketNbr;
	_resp  = _ajaxCall(url);
	if(_resp.result){
			var str = _resp.data;
			var arr = new Array();
			arr = str.split(":");
			var bookDigits = arr[0];
			var packDigits = arr[1];
			var ticketDigits = arr[2];
			document.getElementById("digitsofBook").value = bookDigits;
			document.getElementById("digitsofPack").value = packDigits;
			document.getElementById("digitsofTicket").value = ticketDigits;
	}else{
		alert("Problem:------");
	}
	return false;
}

function ST5_Validation() {

   // alert("hiiiii");
	// Make quick references to our fields
	var gameVirnDigits = document.getElementById("gameVirnDigits");
	//alert(gameVirnDigits.value);
	var gameName = document.getElementById("gameName");
	var gameNumber = document.getElementById("gameNumber");
	//var totalTickets = document.getElementById('totalTickets');
	var priceperTicket = document.getElementById("priceperTicket");
	var ticketpetBook = document.getElementById("ticketpetBook");
	var booksperPack = document.getElementById("booksperPack");
	//var totalBooks = document.getElementById('totalBooks');
	var digitsofBook = document.getElementById("digitsofBook");
	var digitsofPack = document.getElementById("digitsofPack");
	var digitsofTicket = document.getElementById("digitsofTicket");
	var agentSaleCommRate = document.getElementById("agentSaleCommRate");
	var retailerSaleCommRate = document.getElementById("retailerSaleCommRate");
	//var booksperPack = document.getElementById('booksperPack');
	var govtCommRate = document.getElementById("govtCommRate");
	var minAssProfit = document.getElementById("minAssProfit");
	var rank = document.getElementById("rank");
	var image = document.getElementById("image");
	var prizePayOutRatio = document.getElementById("prizePayOutRatio");
	var vatPer = document.getElementById("vatPer");
	var ticketsinScheme = document.getElementById("ticketsinScheme");
	var flag = true;
	if (!isNumeric(gameNumber, i18nOb.prop('error.pls.entr.only.nbr.for.game.nbr'))) {
		flag = false;
	}
	if (!isNumeric(gameVirnDigits, i18nOb.prop("error.entr.nly.no.fr.vini.digit"))) {
		flag = false;
	}
	/*if (!isAlphanumeric(gameName, "Numbers and Letters Only for Game Name")) {
		flag = false;
	}*/
	
	if(gameName.value=="")
	{
	document.getElementById(gameName.name+"1").innerHTML="<font color = 'red'>"+i18nOb.prop('error.pls.entr.game.name')+"</font>";
	flag = false;
	}
	else
	{
	  if(isValidGameName(gameName))
	  {
	     document.getElementById(gameName.name+"1").innerHTML="";
	  }
	  else
	  {
	   document.getElementById(gameName.name+"1").innerHTML="<font color = 'red'>"+i18nOb.prop('error.pls.entr.valid.game.name')+"</font>";
	  }
	}
	if (isDecimal(priceperTicket, i18nOb.prop('error.pls.entr.only.decimal.nbr.for.price.per.ticket'))) {
		
		if (parseInt(priceperTicket.value,10) == "0") {
	var val=parseInt(((priceperTicket.value).substring((priceperTicket.value).indexOf(".")+1)),10);	
	
			if (val == "0") {
			document.getElementById("priceperTicket1").innerHTML = "<font color = 'red'>"+i18nOb.prop('error.zero.value.not.allowed')+"</font>";
			flag = false;
			}
			else{
			document.getElementById("priceperTicket1").innerHTML = "";			
			}
		} else {
			document.getElementById("priceperTicket1").innerHTML = "";
		}
	} else {
		document.getElementById("priceperTicket1").innerHTML = "<font color = 'red'>"+i18nOb.prop('error.only.decimal.values.required')+" </font>";
		flag = false;
	}
	if (!isNumeric(ticketpetBook, i18nOb.prop('error.entr.no.of.tkt.per.book'))) {
		flag = false;
	}
	if (parseInt(ticketpetBook.value,10) == "0") {
		document.getElementById(ticketpetBook.name + "1").innerHTML = "<font color = 'red'>"+i18nOb.prop('error.zero.value.not.allowed')+"</font>";
		flag = false;
	}

	//alert("main6 flag"+flag);
	if (!isNumeric(booksperPack, i18nOb.prop('error.entr.nly.no.of.book.per.pack'))) {
		flag = false;
	}
	if (parseInt(booksperPack.value,10) == "0") {
		document.getElementById(booksperPack.name + "1").innerHTML = "<font color = 'red'>"+i18nOb.prop('error.zero.value.not.allowed')+"</font>";
		flag = false;
	}
	if (digitsofPack.value >= 2 && digitsofBook.value >= 3 && (digitsofPack.value + digitsofBook.value) >= 6) {
		flag = true;
		document.getElementById("generate").innerHTML = "";
	} else {
		document.getElementById("generate").innerHTML = "<font color = 'red'>"+i18nOb.prop('error.cndn.pack.book.digts')+"</font>";
		flag = false;
	}
	if (rank.value == "") {
		document.getElementById("rank1").innerHTML = "<font color = 'red'>"+i18nOb.prop('error.slct.rank.file')+" </font>";
		flag = false;
	}else {
		document.getElementById("rank1").innerHTML = "";
	}
	if (image.value == "") {
		document.getElementById("image1").innerHTML = "<font color = 'red'>"+i18nOb.prop('error.slct.img.file')+" </font>";
		flag = false;
	}else {
		document.getElementById("image1").innerHTML = "";
	}
	if (isDecimal(govtCommRate, i18nOb.prop('error.pls.entr.only.decimal.nbr.for.good.cause.rate'))) {
		if (parseFloat(govtCommRate.value,10) == "0") {
			document.getElementById("govtCommRate1").innerHTML = "<font color = 'red'>"+i18nOb.prop('error.zero.value.not.allowed')+"</font>";
			flag = false;
		} else {
			document.getElementById("govtCommRate1").innerHTML = "";
		}
	} else {
		document.getElementById("govtCommRate1").innerHTML = "<font color = 'red'>"+i18nOb.prop('error.only.decimal.values.required')+" </font>";
		flag = false;
	}
	
	if (isDecimal(prizePayOutRatio, i18nOb.prop('error.pls.entr.only.decimal.nbr.for.pay.out.ratio'))) {
			if (parseFloat(prizePayOutRatio.value,10) == "0" || parseFloat(prizePayOutRatio.value,10) > "99.99") {
				document.getElementById("prizePayOutRatio1").innerHTML = "<font color = 'red'>"+i18nOb.prop('error.zero.value.not.allowed')+"</font>";
			flag = false;
		} else {
			document.getElementById("prizePayOutRatio1").innerHTML = "";
		}
	} else {
		document.getElementById("prizePayOutRatio1").innerHTML = "<font color = 'red'>"+i18nOb.prop('error.only.decimal.values.required')+" </font>";
		flag = false;
	}
	
	if (isDecimal(vatPer, "Please Enter Only Decimal Number for Vat")) {
				if (parseFloat(vatPer.value,10) > "99.99") {
				document.getElementById("vatPer1").innerHTML = "<font color = 'red'>"+i18nOb.prop('error.more.then.100.not.allowed')+"</font>";
			flag = false;
		} else {
			document.getElementById("vatPer1").innerHTML = "";
		}
	} else {
		document.getElementById("vatPer1").innerHTML = "<font color = 'red'>"+i18nOb.prop('error.only.decimal.values.required')+" </font>";
		flag = false;
	}
	
	
	return flag;
}
function doReset(elem) {
	var elem1 = elem.name + "1";
	document.getElementById(elem1).innerHTML = "";
}
function doDateReset() {
	var elem1 = elem.name + "1";
	document.getElementById(elem1).innerHTML = "";
}
function isDecimal(sText1) {
	var sText = sText1.value;
	var validChars = "0123456789.";
	var isNumber = true;
	var myChar;
	var count = 0;
	if (sText.length == 0) {
		isNumber = false;
	}
	for (i = 0; i < sText.length; i++) {
		myChar = sText.charAt(i);
		if (myChar == ".") {
			count++;
		}
		if (validChars.indexOf(myChar) == -1 || count > 1) {
			isNumber = false;
			break;
		}
	}
	return isNumber;
}
function isEmpty(elem, helperMsg) {
	if (elem.value.length == 0) {
	//	alert(helperMsg);
		// set the focus to this input
		return false;
	}
	return true;
}
function isNumeric(elem, helperMsg) {
	var strValidChars = "0123456789";
	var strChar;
	var flag = false;
	var strString = elem.value;
	if (strString.length == 0) {
		document.getElementById(elem.name + "1").style.display = "block";
		document.getElementById(elem.name + "1").innerHTML = "<font color = 'red'>" + helperMsg + "</font>";
		return false;
	}

   //  test strString consists of valid characters listed above
	for (i = 0; i < strString.length; i++) {
		strChar = strString.charAt(i);
		if (strValidChars.indexOf(strChar) == -1) {
			document.getElementById(elem.name + "1").style.display = "block";
			document.getElementById(elem.name + "1").innerHTML = "<font color = 'red'>" + helperMsg + "</font>";
			flag = true;
			break;
		}
	}
	if (!flag) {
		document.getElementById(elem.name + "1").innerHTML = "";
		document.getElementById(elem.name + "1").style.display = "none";
	}
	if (flag) {
		return false;
	}
	return true;
}
function isAlphabet(elem, helperMsg) {
	var alphaExp = /[a-zA-Z]/;
	if (elem.value.match(alphaExp)) {
		doReset(elem);
		return true;
	} else {
		//alert(helperMsg);
		var elem1 = elem.name + "1";
		document.getElementById(elem1).innerHTML = "<font color = 'red'>" + helperMsg + "</font>";
		
		return false;
	}
}
function isAlphanumeric(elem, helperMsg) {
	var alphaExp = /^[0-9a-zA-Z]+$/;
	if (elem.value.match(alphaExp)) {
		doReset(elem);
		return true;
	} else {
		//alert(helperMsg);
		var elem1 = elem.name + "1";
		//document.getElementById('gameName1').innerHTML = "<font color = 'red'>"+helperMsg+"</font>";
		document.getElementById(elem1).innerHTML = "<font color = 'red'>" + helperMsg + "</font>";
	
		return false;
	}
}
function lengthRestriction(elem, min, max) {
	var uInput = elem.value;
	if (uInput.length >= min && uInput.length <= max) {
		return true;
	} else {
		alert(i18nOb.prop('error.ols.entr.b/w') + min + i18nOb.prop('error.and') + max + i18nOb.prop('error.chars'));
		
		return false;
	}
}
function madeSelection(elem, helperMsg) {
	if (elem.value == "Please Choose") {
		alert(helperMsg);
		
		return false;
	} else {
		return true;
	}
}


function isValidGameName(gameName)
{
  var alphaExp = /[0-9a-zA-Z_ ]/;
  var strValue=gameName.value;
  var isValid = true;
	
	if((strValue.replace(/ /g,'')).replace(/_/g,'').length == 0)
	  return false;
	  
	var myChar;
	  for (i = 0; i < strValue.length; i++) {
		myChar = strValue.charAt(i);
		//alert(myChar);
		if (!myChar.match(alphaExp)) 
		     {
			   //alert("alphanumeric not matched");
			   isValid=false;
			   break;
			 }
			 
	   }
return isValid;

}


function CheckBookPack() {
	var ticketpetBook = document.getElementById("ticketpetBook");
	var booksperPack = document.getElementById("booksperPack");
	var flag = true;
	if (!isNumeric(ticketpetBook, i18nOb.prop('error.entr.no.of.tkt.per.book'))) {
		flag = false;
	}
	if (parseInt(ticketpetBook.value,10) == "0") {
		document.getElementById(ticketpetBook.name + "1").innerHTML = "<font color = 'red'>"+i18nOb.prop('error.zero.value.not.allowed')+"</font>";
		flag = false;
	}
	if (!isNumeric(booksperPack, i18nOb.prop('error.entr.nly.no.of.book.per.pack'))) {
		flag = false;
	}
	if (parseInt(booksperPack.value,10) == "0") {
		document.getElementById(booksperPack.name + "1").innerHTML = "<font color = 'red'>"+i18nOb.prop('error.zero.value.not.allowed')+"</font>";
		flag = false;
	}
	return flag;
}

parent.frames[0].Version['$Source: /rep/LMS_Mgmt/WebRoot/com/skilrock/lms/web/instantPrint/gameMgmt/backOffice/js/gameUploadValidation.js,v $'] = '$Id: gameUploadValidation.js,v 1.3 2016/10/31 09:46:36 neeraj Exp $';