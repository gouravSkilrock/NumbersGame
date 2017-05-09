var isVerify = true;
function isVerified(){
isVerify = false;
}
function ValidateBookSeries(bookNbrFrom,bookNbrTo){
isValid = true;	
allEmpty = true;
  for(var i=0;i<bookNbrFrom.length;i++){
  bookNbrFrom[i]=document.getElementsByName('bookNoFrom')[i].value;
  bookNbrTo[i]=document.getElementsByName('bookNoTo')[i].value;
  //alert( bookNbrFrom[i]+"---"+bookNbrTo[i]);
  if(bookNbrFrom[i].length!=""&&bookNbrTo[i].length!=""&&bookNbrFrom[i].length>3&&bookNbrTo[i].length>3){
  if(bookNbrFrom[i].length!=bookNbrTo[i].length){
  document.getElementById('divSe'+(i+1)).innerHTML="Please Enter Correct BookNumber";
  isValid=false;
  }else if(bookNbrFrom[i]>bookNbrTo[i]){
  document.getElementById('divSe'+(i+1)).innerHTML="From BookNum Greater than To BookNum";
  isValid=false;
  }
  //  alert(gameNumber+"---"+gameNumber.length);
  if(bookNbrFrom[i].substring(0,gameNumber.length)!=gameNumber){
  document.getElementById('divSe'+(i+1)).innerHTML="Please Enter Correct BookNumber";
  isValid=false;
  }
  if(bookNbrTo[i].substring(0,gameNumber.length)!=gameNumber){
  document.getElementById('divSe'+(i+1)).innerHTML="Please Enter Correct BookNumber";
  isValid=false;
  }
  var arrFrm =(bookNbrFrom[i]).split("-");
  var arrTo =(bookNbrTo[i]).split("-");
  //alert(arrFrm.length+"--"+arrTo.length);
  if(arrFrm.length>2||arrTo.length>2){
  document.getElementById('divSe'+(i+1)).innerHTML="Please Use only one '-' in BookNum";
  isValid=false;
  }
  allEmpty=false;
  }else if(bookNbrFrom[i].length==""&&bookNbrTo[i].length==""){
  
  }
  else if(bookNbrFrom[i]==""&&bookNbrTo[i]==""){
  document.getElementById('divSe'+(i+1)).innerHTML="";
  }
  else{
  document.getElementById('divSe'+(i+1)).innerHTML="Please Enter Correct Values";
	isValid=false; 
  }
}

var book = document.getElementsByName('bookNo');
for(i=0; i< book.length; i++){
if(book[i].value != ""){
if((book[i].value).substring(0,gameNumber.length)!=gameNumber||(book[i].value).length<4){
  document.getElementById('div'+(i+1)).innerHTML="Please Enter Correct BookNumber";
  isValid=false;
}else if(((book[i].value).split("-")).length>2){
	document.getElementById('div'+(i+1)).innerHTML="Please Use only one '-' in BookNum";
  isValid=false;
}
allEmpty=false;
}
}
if(allEmpty){
 	var seriesStatus = document.getElementsByName("bookNoFrom");
 	for(var i=0;i<seriesStatus.length;i++){
 	var divId = "divSe"+(i+1);
 	document.getElementById(divId).innerHTML=""; 	
 	}
 	var book = document.getElementsByName('bookNo');
 	for(i=0; i< book.length; i++){
    document.getElementById('div'+(i+1)).innerHTML="";
	}

}
return isValid;
}

 
 
 
 function checkAllBooksEmpty(){
 var booksEmpty = true;
 var book = document.getElementsByName('bookNo');
 var bookNbrFrom=document.getElementsByName('bookNoFrom');
 var bookNbrTo=document.getElementsByName('bookNoTo');
	for(i=0; i< book.length; i++){
	if(book[i].value != ""){
	
	booksEmpty=false;
	}
	}
 for(var i=0;i<bookNbrFrom.length;i++){
 if(bookNbrFrom[i].value!=""&&bookNbrTo[i].value!=""){

 booksEmpty=false;
 }
 }

 return booksEmpty;
 }


 
 
 
  var mainDiv;
  var req;
  
  
  
  
  function VerifyAgentBookSeries(){
 // alert("game number is  " + gameNumber);
  var bookNbrFrom= new Array();
  var bookNbrTo= new Array();
  for(var i=0;i<document.getElementsByName('bookNoFrom').length;i++){
  bookNbrFrom[i]=document.getElementsByName('bookNoFrom')[i].value;
  bookNbrTo[i]=document.getElementsByName('bookNoTo')[i].value;
  }
 if(!ValidateBookSeries(bookNbrFrom,bookNbrTo)){
 return false;
 }
 document.getElementById("loadingDiv").style.visibility="visible";
  var gameId = document.dspchOrderGame.gameId.value;
    
  var book = document.getElementsByName('bookNo');
   
   //alert("bbb:" + book);
   
    var booksVal = "";
    //alert("Initial Length:" + booksVal.length);
    for(i=0; i< book.length; i++){
      if(book[i].value != ""){
      booksVal = booksVal + (book[i].value).replace(/-/g,"") + ",";
      }else{
       booksVal = booksVal + " " + ",";
      }
    }
    
    var length = booksVal.length;
   
    var agentBooks;
    if(length > 1){
    booksVal = booksVal.substring(0,length-1);
     agentBooks = booksVal;
    } else{
    agentBooks = document.forms[0].bookNo;
    }
  if(agentBooks==" "||agentBooks== undefined){
  agentBooks = " , ";
  }
  //alert("--------"+agentBooks);
  
  url = "agt_im_assignOrder_VerifyBookSeries.action?gameNbr="+gameNumber+" & gameId="+gameId+" & bookNbrFromArr="+bookNbrFrom +"&bookNbrToArr =" + bookNbrTo+"&bookNbr =" + agentBooks ;
  //alert("url is --" + url);
  document.getElementById("blankBooksDiv").innerHTML="";
  if (window.XMLHttpRequest) { // Non-IE browsers
      req = new XMLHttpRequest();
      req.onreadystatechange = processStateChangeForAgentBookSeries;
      try {
        req.open("GET", url, true);
      } catch (e) {
        //alert(e);
      }
      req.send(null);
    } else if (window.ActiveXObject) { // IE
      req = new ActiveXObject("Microsoft.XMLHTTP");
      if (req) {
        req.onreadystatechange = processStateChangeForAgentBookSeries;
        req.open("GET", url, true);
        
        req.send();
      }
    }
  
  return true;
  }
  
  
  function processStateChangeForAgentBookSeries() {

    if (req.readyState == 4) { // Complete
      if (req.status == 200) { // OK response
document.getElementById("loadingDiv").style.visibility="hidden";
   //alert(req.responseText);
   isVerify = true;
   document.getElementById('bookSerDiv').innerHTML = req.responseText;
   
   if(req.responseText=="valid"){
   //document.getElementById(mainDiv).innerHTML = '<input type=hidden name="hidBook"/>';
  	
   }
   else{
     // document.getElementById(mainDiv).innerHTML = '<input type=hidden name="invalidBook"/> Wrong Book Number';
    }
     
   //alert("dispatch books "  + document.dspchOrderGame.hidDispatchBooks.value);
   document.dspchOrderGame.noOfBooksToDispatch.value = document.dspchOrderGame.hidDispatchBooks.value;
   changeDispatchBooks();
   
     
   
     
      } else {
        //alert("Problem: " + req.statusText);
      }
    }
    
    
  }
  
  
  
  
  function verifyAgentBook(bookNbr,div) {
  
  document.getElementById('blankBooksDiv').innerHTML = '';
    
    mainDiv= "div"+div;
	
	/*
	if(bookNbr == ""){
	
	document.getElementById(mainDiv).innerHTML = '';
	changeDispatchBooks(); 
	return;
	}
	
	var duplicateCheck = checkDuplicateBookEntry(bookNbr);
	if(duplicateCheck){
	
	document.getElementById(mainDiv).innerHTML = '<input type=hidden name="invalidBook"/> Duplicate Book Entry';
	return;
	}
	*/
	
	var book = document.getElementsByName('bookNo');
	
	var booksVal = "";
    //alert("Initial Length:" + booksVal.length);
    for(i=0; i< book.length; i++){
      if(book[i].value != ""){
      booksVal = booksVal + book[i].value + ",";
      }else{
       booksVal = booksVal + " " + ",";
      }
    }
    
    var length = booksVal.length;
   
    var agtBooks;
    if(length > 1){
    booksVal = booksVal.substring(0,length-1);
     agtBooks = booksVal;
    } else{
    agtBooks = document.forms[0].bookNo;
    }
	
	
  	var gameId = document.dspchOrderGame.gameId.value;
  	
	url = "verifyAgentBook.action?gameId="+gameId+" & bookNbrToVerify="+bookNbr+"&bookNbr =" + agtBooks +"& bookCountId = " + div ;
	
	
	
    if (window.XMLHttpRequest) { // Non-IE browsers
      req = new XMLHttpRequest();
      req.onreadystatechange = processStateChangeForAgentBook;
      try {
        req.open("GET", url, true);
      } catch (e) {
        //alert(e);
      }
      req.send(null);
    } else if (window.ActiveXObject) { // IE
      req = new ActiveXObject("Microsoft.XMLHTTP");
      if (req) {
        req.onreadystatechange = processStateChangeForAgentBook;
        req.open("GET", url, true);
        
        req.send();
      }
    }
  }

  function processStateChangeForAgentBook() {

    if (req.readyState == 4) { // Complete
      if (req.status == 200) { // OK response
   
   document.getElementById('agentBookListDiv').innerHTML = req.responseText;
   
   if(req.responseText=="valid"){
   //document.getElementById(mainDiv).innerHTML = '<input type=hidden name="hidBook"/>';
  	
   }
   else{
      //document.getElementById(mainDiv).innerHTML = '<input type=hidden name="invalidBook"/> Wrong Book Number';
    }
     
   document.dspchOrderGame.noOfBooksToDispatch.value = document.dspchOrderGame.hidDispatchBooks.value;
   changeDispatchBooks();  
     
      } else {
        alert("Problem: " + req.statusText);
      }
    }
    
    
  }
  
  function checkDuplicateBookEntry(bookNbr){
  
      var book = document.getElementsByName("bookNbr");
      var duplicate = 0;
      
      for(i=0; i<book.length; i++){
            
               
         	if ( bookNbr == book[i].value ){
            duplicate++;
         	}
         
      }
   
      if(duplicate > 1){
      return true;
      }
      else{
      return false;
      }
  }
  
   function checkDuplicatePackEntry(packNbr){
  
      var pack = document.getElementsByName("packNbr");
      var duplicate = 0;
      
      for(i=0; i<pack.length; i++){
            
             
         	if ( packNbr == pack[i].value ){
            duplicate++;
         	}
         
      }
   
      if(duplicate > 1){
      return true;
      }
      else{
      return false;
      }
  }
  
  
function verifyAgentPack(packNbr,div) {
  //alert("game nbrhhhh " + gameNumber);
  mainDiv= "packDiv"+div;
 
   /*
  if(packNbr == ""){
	
	document.getElementById(mainDiv).innerHTML = '';
	changeDispatchBooks(); 
	return;
	}
	
	var duplicateCheck = checkDuplicatePackEntry(packNbr);
	if(duplicateCheck){
	
	document.getElementById(mainDiv).innerHTML = '<input type=hidden name="invalidPack"/> Duplicate Pack Entry';
	return;
	}
	*/
	
	var pack = document.getElementsByName('packNo');
   
   //alert("ppp:" + pack);
   
    var packsVal = "";
    //alert("Initial Length:" + packsVal.length);
    for(i=0; i< pack.length; i++){
      if(pack[i].value != ""){
      packsVal = packsVal + pack[i].value + ",";
      }else{
       packsVal = packsVal + " " + ",";
      }
    }
    
    var length = packsVal.length;
   
    var agtPacks;
    if(length > 1){
    packsVal = packsVal.substring(0,length-1);
     agtPacks = packsVal;
    } else{
    agtPacks = document.forms[0].packNo;
    }
	
  var gameId = document.dspchOrderGame.gameId.value;
  
 
  url = "verifyAgentPack.action?gameNbr="+gameNumber+" & gameId="+gameId+"&packNbrToVerify="+ packNbr+ "&packNbr =" + agtPacks +"& packCountId = " + div ;
  
    if (window.XMLHttpRequest) { // Non-IE browsers
      req = new XMLHttpRequest();
      req.onreadystatechange = processStateChangeForAgentPack;
      try {
        req.open("GET", url, true);
      } catch (e) {
        alert(e);
      }
      req.send(null);
    } else if (window.ActiveXObject) { // IE
      req = new ActiveXObject("Microsoft.XMLHTTP");
      if (req) {
        req.onreadystatechange = processStateChangeForAgentPack;
        req.open("GET", url, true);
        
        req.send();
      }
    }
  }
  function processStateChangeForAgentPack() {
	
    if (req.readyState == 4) { // Complete
      if (req.status == 200) { // OK response
  
  	document.getElementById('agentPackListDiv').innerHTML = req.responseText;
  	
   if(req.responseText=="valid"){
   
   //document.getElementById(mainDiv).innerHTML = '<input type=hidden name="hidPack"/>';
   }
   else{
      //document.getElementById(mainDiv).innerHTML = '<input type=hidden name="invalidPack"/> Wrong Pack Number';
      }
      
   document.dspchOrderGame.noOfBooksToDispatch.value = document.dspchOrderGame.hidDispatchPacksBooks.value;
   changeDispatchBooks();     
      
      } else {
        alert("Problem: " + req.statusText);
      }
    }
  }
 
 function addRowToTable()
{

  var tbl = document.getElementById('booksTable');
  var lastRow = tbl.rows.length;

  var iteration = lastRow;
  var row = tbl.insertRow(lastRow);
 
  
	  var cellLeft = row.insertCell(0);
	  var newdiv = document.createElement('div');
	  newdiv.setAttribute('id','div');
	  newdiv.innerHTML = 'Book Number: <input type=text name="bookNbr" id='+iteration+'   onchange="verifyAgentBook(this.value,this.id)" />';
      cellLeft.appendChild(newdiv);
 	  var cellLeft = row.insertCell(1);
	  var newdiv = document.createElement('div');
	  var divid = "div"+iteration;
	 
	  newdiv.setAttribute('id',divid);
	  cellLeft.appendChild(newdiv);
}


 

 function verifyEntry(){
 
    var seriesStatus = document.getElementsByName("bookNoFrom");
 	for(var i=0;i<seriesStatus.length;i++){
 	var divId = "divSe"+(i+1);
 	if(document.getElementById(divId).innerHTML!=""){
 	
 	document.getElementById('invalidEntryDiv').innerHTML = '<font color=red>Please remove invalid entries before proceeding</font>';
    return false;
 	}
 	
 	}
 	if(!checkAllBooksEmpty()){
 	if(!isVerify){
 	document.getElementById("blankBooksDiv").innerHTML='<font color=red><align="center">Please Verify Books';
 	return false;
 	}
 	}else{
 	//alert(isVerify);
 	if(!isVerify){
 	document.getElementById("blankBooksDiv").innerHTML='<font color=red><align="center">Please Verify Empty Books Also';
 	return false;
 	}
 	}
 
    /*
    var validBookCount = document.getElementsByName("hidBook");
    var bookCount = document.getElementsByName("bookNbr");
    
    
    var invalidBookCount = document.getElementsByName("invalidBook");
    
    if(invalidBookCount.length > 0){
     alert("Please remove invalid entries before proceeding");
     return false;
    }
    
     var invalidPackCount = document.getElementsByName("invalidPack");
    
    if(invalidPackCount.length > 0){
    alert("Please remove invalid entries before proceeding");
    return false;
    }
   	*/
   	//alert('hello');
   	var bookPrice=document.getElementById('bookPrice').value;
   	var retCommRate=document.getElementById('retCommRate').value;
    var nbrOfBooksToDispatch=document.getElementById('noOfBooksToDispatch').value;
    var remainingCreditAmt=document.getElementById('remainingCreditLimit').value;
    
    var retCommAmt=parseFloat(bookPrice)*.01*parseFloat(retCommRate);
   // alert("ret comm rate" + retCommAmt  );
    var totalBookAmtforDispatch=(parseFloat(bookPrice)-parseFloat(retCommAmt)) * nbrOfBooksToDispatch;
   // alert("book dispatch amt is " + totalBookAmtforDispatch);
    //alert("before if");
    if(parseFloat(totalBookAmtforDispatch) > parseFloat(remainingCreditAmt))
    {
      alert('Balance is not Sufficient to Dispatch This order Please remove some entries');
      return false;
    }
   	
   	
   	var invalidBookCount = document.getElementsByName("invalidBook");
    
    
    if(invalidBookCount.length > 0){
     //alert("Please remove invalid entries before proceeding");
     document.getElementById('invalidEntryDiv').innerHTML = '<font color=red>Please remove invalid entries before proceeding</font>';
     return false;
    } else{
    document.getElementById('invalidEntryDiv').innerHTML = '';
    }
    
     var invalidPackCount = document.getElementsByName("invalidPack");
    
    if(invalidPackCount.length > 0){
    document.getElementById('invalidEntryDiv').innerHTML = '<font color=red>Please remove invalid entries before proceeding</font>';
    return false;
    }else{
    document.getElementById('invalidEntryDiv').innerHTML = '';
    }
   	
    
   	/* 
   	if(document.dspchOrderGame.noOfBooksToDispatch.value > document.dspchOrderGame.nbrOfBooksApp.value){
   	 alert("The number of books to dispatch cannot be greater than approved books");
   	 return false;
   	}
   	*/
   	
   	if(parseInt(document.dspchOrderGame.noOfBooksToDispatch.value) > parseInt(document.dspchOrderGame.nbrOfBooksRemain.value)){
   	 //alert("The number of books to dispatch cannot be greater than approved books");
   	 document.getElementById('dispatchDiv').innerHTML = '<font color=red>The number of books to dispatch cannot be greater than remaining approved books</font>';
   	 return false;
   	} else {
   	document.getElementById('dispatchDiv').innerHTML = '';
   	}
   	
   	/*if(document.dspchOrderGame.noOfBooksToDispatch.value == 0){
   	 var confirmValue = confirm("You are about to dispatch zero books.Are you sure?");
   	 if( confirmValue ){
   	 return true;
   	 }else{
   	 return false;
   	}
   	 }*/
   	/*if(document.dspchOrderGame.noOfBooksToDispatch.value == 0){
   	alert("You can not Dispatch Zero Books..");
   	return false;
   	}*/
   	
   	
   	if(parseInt(document.dspchOrderGame.noOfBooksToDispatch.value) < parseInt(document.dspchOrderGame.nbrOfBooksRemain.value)){
   	 var confirmValue = confirm(" The dispatched books would be lesser than remaining approved books.Are you sure?");
   	 if( confirmValue ){
   	 return true;
   	 }else{
   	 return false;
   	}
   	 }
   	 
   	/* 
   	var validPackCount = document.getElementsByName("hidPack");
    var packCount = document.getElementsByName("packNbr");
   	
   	 if( bookCount.length != validBookCount.count){
   	   return  false;
   	 }
   	 
   	 if( validPackCount.length != packCount.count){
   	   return  false;
   	 }
   	 */
   	 
   	 return true;
 
 }
 
 function changeDispatchBooks(){
 //alert("hello");
     //var validBooks = document.getElementsByName("hidBook");
     //var validPacks = document.getElementsByName("hidPack");
     //var nbrOfBooksPerPack = document.dspchOrderGame.nbrOfBooksPerPack.value;
     
   	 
   	 //document.dspchOrderGame.noOfBooksToDispatch.value = (nbrOfBooksPerPack * validPacks.length) +  validBooks.length;
   	 
   	 if(parseInt(document.dspchOrderGame.noOfBooksToDispatch.value) >= parseInt(document.dspchOrderGame.nbrOfBooksApp.value)){
   	 
   	 if(document.dspchOrderGame.b1!=null)
   	 document.dspchOrderGame.b1.disabled=true;
   	 
   	 }
   	 else{
   	 if(document.dspchOrderGame.b1!=null)
   	 document.dspchOrderGame.b1.disabled=false;
   	 }
   	 
 
 }
function checkNum(){
	var e = event || evt;
	var charCode = e.which || e.keyCode;
	if ((charCode >= 48 && charCode < 58) || charCode == 45) {
		return true;
	}
	return false;
}



parent.frames[0].Version['$Source: /rep/LMS_Mgmt/WebRoot/com/skilrock/lms/web/scratchService/inventoryMgmt/backOffice/js/dsptchAgentOrder.js,v $'] = '$Id: dsptchAgentOrder.js,v 1.3 2016/10/31 09:46:19 neeraj Exp $';
