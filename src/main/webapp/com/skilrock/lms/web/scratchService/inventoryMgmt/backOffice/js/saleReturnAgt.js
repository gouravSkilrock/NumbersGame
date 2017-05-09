	
		 var mainDiv;
		 var req;
		 var which;
		var pack =0;
		var book = 0;
		var bookUrl;
	
		 function retrieveURL(urlY,div) {
			 mainDiv=document.getElementById('characters');
			var url = urlY+escape(document.getElementById('s1').value);
			_resp  = _ajaxCall(url);
				if(_resp.result){
			      _id.i(mainDiv,_resp.data);
			      } else {
			        alert("Problem: ");
			        }
		  }
  var urlAction;
  function retrieveURL1(urlYY) {
  var url = urlYY+escape(document.getElementById('s1').value);
  urlAction = urlYY;
  url=url+'&game_Name='+escape(document.getElementById('game_Name').value);
  bookUrl=escape(document.getElementById('s1').value)+'&& game_Name='+escape(document.getElementById('game_Name').value);
	_resp  = _ajaxCall(url);
		if(_resp.result){
	     		      		refreshDivPack();
		      				retrieveURLFetchBook(bookUrl);
	      } else {
	        alert("Problem: ");
	        }
  }


  function retrieveURL2(urlYY) {
  var url = urlYY;
  
  var comp1="";
	for (var i=0;i < document.forms[0].elements.length;i++)
	{
		var e = document.forms[0].elements[i];
		if(e.name=="packNumber"){
		comp1 = comp1+"packNumber="+e.value+"&";
		}
		
}
  comp1= comp1+"packNumber=1";
   var newData=comp1.substring(0,comp1.lastIndexOf("&packNumber")) ;
   var url = urlYY+"?"+newData;
_resp  = _ajaxCall(url);
	if(_resp.result){
     refreshDivPack();
      } else {
        alert("Problem: ");
        }
  }
   function retrieveURLFetchBook(bookUrl){
 //alert("book URl"+bookUrl);
 var url;
 var newUrl=urlAction.substring(urlAction.indexOf('x')+1,urlAction.indexOf('.')) ;
  //alert("new url"+newUrl);
  if(newUrl=='Agent'){
    url ='salesRetAjaxBookAgent.action?Type='+bookUrl; 
  }else{
     url ='salesRetAjaxBook.action?Type='+bookUrl; 
  }
	_resp  = _ajaxCall(url);
	if(_resp.result){
      refreshDivBook();
      } else {
        alert("Problem: ");
        }
  }
   function retrieveURLFetchPackBook(){
 //alert("book URl"+bookUrl);
//var pacNo=dojo.forEach("packNumber");
//alert('hhhhhhhhhhhhhhhhhhh');
var j =0;
var k = 0;
   for (var i=0;i < document.forms[0].elements.length;i++)
	{
		var e = document.forms[0].elements[i];
		if(e.name=="packNumber"){
		j++;	
		}
		}
	var pacNo= new Array(j);	
	for (var i=0;i < document.forms[0].elements.length;i++)
	{
		var e = document.forms[0].elements[i];
		if(e.name=="packNumber"){
		pacNo[k] = e.value;
		k++;	
		}
		}	
	//alert(pacNo[0]+""+pacNo[1]+" k  is"+k+ "j is "+j);	
 //var packArray=document.getElementsByName("packNumber").value;
 var packArray= new Array(j);
  var bookUrl=escape(document.getElementById('s1').value)+'& game_Name='+escape(document.getElementById('game_Name').value);
  var m =0;
 for(var i=0;i<pacNo.length;i++){
 if(!(pacNo[i].value=="-1")){
	packArray[m]=pacNo[i];
	m++;
 }
 }

 var url;
 var newUrl=urlAction.substring(urlAction.indexOf('x')+1,urlAction.indexOf('.')) ;
  //alert("new url"+newUrl);
  if(newUrl=='Agent'){
    url ='salesRetAjaxBookAgent.action?Type='+bookUrl+'&& packNumber='+packArray; 
  }else{
     url ='salesRetAjaxBook.action?Type='+bookUrl+' && packNumber='+packArray; 
  }
_resp  = _ajaxCall(url);
	if(_resp.result){
      refreshDivBook();
      } else {
        alert("Problem: ");
        }
  }

   function retrieveURLAddBook(urlYY) {
  // alert("hello");
    var comp1="";
	for (var i=0;i < document.forms[0].elements.length;i++)
	{
		var e = document.forms[0].elements[i];
		if(e.name=="bookNumber"){
		comp1 = comp1+"bookNumber="+e.value+"&";
		}
		
}
  comp1= comp1+"bookNumber=1";
   var newData=comp1.substring(0,comp1.lastIndexOf("&bookNumber")) ;
   var url = urlYY+"?"+newData;
_resp  = _ajaxCall(url);
	if(_resp.result){
     refreshDivBook();
      } else {
        alert("Problem: ");
        }
  }
	function convert(urlX){
		var exp= /&/g ;
		return urlX.replace(exp,"amp");
	
		}  	 
		  function addPackToTable(tb){
					var ret = addPack();
				  if(!ret){
				  document.getElementById('pack').innerHTML = "<font color = 'red'>Please Fill Empty Pack Number</font>";
  				  return false;
			}
			else{
  				 document.getElementById('pack').innerHTML = "";
   			}
				  var tbl = document.getElementById(tb);
				  var lastRow = tbl.rows.length;
				  var iteration = lastRow;
				  var row = tbl.insertRow(lastRow);
				  
				  // left cell
				  var cellLeft = row.insertCell(0);
				  var textNode = document.createTextNode("Pack Number");
				  cellLeft.appendChild(textNode);
				  
				  // right cell
				  var cellRight = row.insertCell(1);
				  var el = document.createElement('input');
				  el.type = 'text';
				  el.name = 'packNumber';
				  el.id = 'pack_num' + iteration;
				  el.size = 20;
				  cellRight.appendChild(el);
				  return true;
		 }
		 
		 function addBookToTable(tb){
		var ret = addBook();
				  if(!ret){
				  document.getElementById('book').innerHTML = "<font color = 'red'>Please Fill Empty Book Number</font>";
  				  return false;
			}
			else{
  				 document.getElementById('book').innerHTML = "";
   			}
				  var tbl = document.getElementById(tb);
				  var lastRow = tbl.rows.length;
				  var iteration = lastRow;
				  var row = tbl.insertRow(lastRow);
				  
				  // left cell
				  var cellLeft = row.insertCell(0);
				  var textNode = document.createTextNode("Book Number");
				  cellLeft.appendChild(textNode);
				  
				  // right cell
				  var cellRight = row.insertCell(1);
				  var el = document.createElement('input');
				  el.type = 'text';
				  el.name = 'bookNumber';
				  el.id = 'bnum' + iteration;
				  el.size = 20;
				  cellRight.appendChild(el);
				  return true;
		 }
	 

function validate(){
var isInValid = false;
book=1;
for (var i=0;i < document.forms[0].elements.length;i++)
	{
		var e = document.forms[0].elements[i];

		if(e.name=="organization_Name")
		{
			if(e.value=="Please Select")
			{
			document.getElementById('organization_NameDiv').innerHTML = "<font color = 'red'>Please Select Company</font>";
  			isInValid=true;
			}
			else{
  				 document.getElementById('organization_NameDiv').innerHTML = "";
   			}
	
		}
		if(e.name=="game_Name")
		{
			if(e.value=="Please Select")
			{
			document.getElementById('game_name1').innerHTML = "<font color = 'red'>Please Select Game</font>";
  			isInValid=true;
			}
			else{
  				 document.getElementById('game_name1').innerHTML = "";
   			}
	
		}
		if(e.name=="packNumber")
		{
			if(e.value==""){
			 pack =1;
			}
			else{
			pack =0;
			}
		}
		if(e.name=="bookNumber")
		{	
			if(book==1){
				if(e.value!=""){
					 book =0;
				}
			}
		}
	}
	//alert(pack);
	//alert(book);
	if(pack==1 && book==1)
	{
	document.getElementById('pack1').innerHTML = "<font color = 'red'>Please Enter Pack or Book Number</font>";
  			isInValid=true;
	}
	else{
	document.getElementById('pack1').innerHTML = "";
	}
 if(isInValid){
  return false;
  }
  return true;
  }
function addPack(){
for (var i=0;i < document.forms[0].elements.length;i++)
	{
		var e = document.forms[0].elements[i];
		if(e.name=="packNumber")
		{
			if(e.value==""){
			 pack =1;
			 return false;
			}
			
		}
		
	
}	
return true;
}	  
function addBook(){
for (var i=0;i < document.forms[0].elements.length;i++)
	{
		var e = document.forms[0].elements[i];
		if(e.name=="bookNumber")
		{
			if(e.value==""){
			 book =1;
			 return false;
			}
			
		}

	}
		return true;
}	

 function checkDuplicatePackEntry(packNbr){
 //alert("inside duplicate");
 	 document.getElementById("error1").innerHTML ="";
      document.getElementById("verify").disabled=false;
      document.getElementById("error").style.display="none";
      //var pack = document.getElementsByName("packNumber");
      var j =0;
	  var k = 0;
   for (var i=0;i < document.forms[0].elements.length;i++)
	{
		var e = document.forms[0].elements[i];
		if(e.name=="packNumber"){
		j++;	
		}
		}
	var pack= new Array(j);	
	for (var i=0;i < document.forms[0].elements.length;i++)
	{
		var e = document.forms[0].elements[i];
		if(e.name=="packNumber"){
		pack[k] = e.value;
		k++;	
		}
		}	
	//alert(pacNo[0]+""+pacNo[1]+" k  is"+k+ "j is "+j);	
 //var packArray=document.getElementsByName("packNumber").value;
     var duplicate = 0;
      
      for(var i=0; i<j; i++){
            
             
         	if ( (packNbr == pack[i]) && !(packNbr=="Please Select")&& !(pack[i]=="Please Select")){
         	//alert("duplicate pack"+packNbr+" "+pack[i].value); 
            duplicate++;
         	}
         
      }
      
      
   
      if(duplicate > 1){
     // 
       document.getElementById("error").style.display="block";
		  document.getElementById('error').innerHTML = '<font color=red> Duplicate Pack Number  </font>';
		  document.getElementById("verify").disabled=true;
		 
     // alert(document.getElementById('error').innerHTML);
      return false;
      }
      else{
       document.getElementById('error').innerHTML = '';
    
      
      // alert("true");
      retrieveURLFetchPackBook();
      return true;
      }
  }


function checkDuplicateBookEntry(bookNbr){
 //alert("inside duplicate"+bookNbr);
 document.getElementById('error').innerHTML ='';
      document.getElementById("verify").disabled=false;
      document.getElementById("error1").style.display="none";
      var book = document.getElementsByName("bookNumber");
      var j =0;
	  var k = 0;
   for (var i=0;i < document.forms[0].elements.length;i++)
	{
		var e = document.forms[0].elements[i];
		if(e.name=="bookNumber"){
		j++;	
		}
		}
	var book= new Array(j);	
	for (var i=0;i < document.forms[0].elements.length;i++)
	{
		var e = document.forms[0].elements[i];
		if(e.name=="bookNumber"){
		book[k] = e.value;
		k++;	
		}
		}
      var duplicate = 0;
      
      for(var i=0; i<j; i++){
            
            
         	if ( (bookNbr == book[i]) && !(bookNbr=="Please Select")&& !(book[i]=="Please Select")){
         	//alert("duplicate pack"+bookNbr+" "+book[i].value); 
            duplicate++;
         	}
         
      }
   
      if(duplicate > 1){
     // 
       document.getElementById("error1").style.display="block";
		  document.getElementById('error1').innerHTML = '<font color=red> Duplicate Book Number  </font>';
		  document.getElementById("verify").disabled=true;
		 
     // alert(document.getElementById('error').innerHTML);
      return false;
      }
      else{
       document.getElementById('error1').innerHTML = '';
    
      
      // alert("true");
      return true;
      }
  }
  
  
  ///
  function checkNullEntry(){
 //alert("inside checkNullEntry");
      
      document.getElementById("error").style.display="none";
     var duplicate=checkNullPack;
     var dupBook=checkNullBook;
     
     
      //alert("duplicate"+duplicate);
        //alert(duplicate+" ~~"+pack.length+" ~~"+dupBook+"~~ "+book.length);
        //alert(duplicate+" "+pack.length+1+" "+dupBook+" "+book.length+1);
   
      if((duplicate == pack.length)&&(dupBook == book.length)){
   //alert(duplicate+" "+pack.length+" "+dupBook+" "+book.length);
       document.getElementById("error").style.display="block";
		  document.getElementById('error').innerHTML = '<font color=red> No Pack/Book  Selected/Found </font>';
		 
		 
     // alert(document.getElementById('error').innerHTML);
      return false;
      }
      else{
       document.getElementById('error').innerHTML = '';
    
      
      // alert("true");
      return true;
      }
  }
  
  function checkNullPack(){
 // alert("checkNullPack");
   var j =0;
	  var k = 0;
   for (var i=0;i < document.forms[0].elements.length;i++)
	{
		var e = document.forms[0].elements[i];
		if(e.name=="packNumber"){
		j++;	
		}
		}
	var pack= new Array(j);	
	for (var i=0;i < document.forms[0].elements.length;i++)
	{
		var e = document.forms[0].elements[i];
		if(e.name=="packNumber"){
		pack[k] = e.value;
		k++;	
		}
		}	
	//alert(pacNo[0]+""+pacNo[1]+" k  is"+k+ "j is "+j);	
 //var packArray=document.getElementsByName("packNumber").value;
     var duplicate = 0;
      
      for(var i=0; i<j; i++){
            
             
         	if ( (packNbr == pack[i]) && !(packNbr=="-1")&& !(pack[i]=="-1")){
         	//alert("duplicate pack"+packNbr+" "+pack[i].value); 
            duplicate++;
         	}
         
      }
      return duplicate;
  }
  function checkNullBook(){
//  alert("checkNullBook");
  var book = document.getElementsByName("bookNumber");
      var j =0;
	  var k = 0;
   for (var i=0;i < document.forms[0].elements.length;i++)
	{
		var e = document.forms[0].elements[i];
		if(e.name=="bookNumber"){
		j++;	
		}
		}
	var book= new Array(j);	
	for (var i=0;i < document.forms[0].elements.length;i++)
	{
		var e = document.forms[0].elements[i];
		if(e.name=="bookNumber"){
		book[k] = e.value;
		k++;	
		}
		}
      var duplicate = 0;
      
      for(var i=0; i<j; i++){
            
            
         	if ( (bookNbr == book[i]) && !(bookNbr=="-1")&& !(book[i]=="-1")){
         	//alert("duplicate pack"+bookNbr+" "+book[i].value); 
            duplicate++;
         	}
         
      }
      return duplicate;
  }
  
  
  function refreshDivPack(){
   var ajaxResultDiv = dojo.widget.byId('ab'); 
  // alert(ajaxResultDiv);
   ajaxResultDiv.refresh(); 
   
  }
  
  function refreshDivBook(){
 // alert("1111111444444444444444444444");
   var ajaxResultDivBook = dojo.widget.byId('abc'); 
   //alert(ajaxResultDivBook);
   ajaxResultDivBook.refresh(); 
   
  }
  
  
  dojo.event.topic.subscribe("/checkDuplicate", function(data, type, e){
     // alert('inside a topic event. type='+type+'ssssssssss'+data+'  event '+e);
//alert('hi                     maaaaaaaaaaaaaaaaaa fffffffffffffffffffff');

checkDuplicatePackEntry(data);
      //data : text returned
      //type : "before", "load" or "error"
      //e    : request object
    });
	dojo.event.topic.subscribe("/checkDuplicateBook", function(data, type, e){
	document.getElementById('error').innerHTML ='';
      //alert('inside a topic event. type='+type+'ssssssssss'+data+'  event '+e);
//alert('hi maaaaaaaaaaaaaaaaaa fffffffffffffffffffff');
checkDuplicateBookEntry(data);
      //data : text returned
      //type : "before", "load" or "error"
      //e    : request object
    });	  
    
parent.frames[0].Version['$Source: /rep/LMS_Mgmt/WebRoot/com/skilrock/lms/web/scratchService/inventoryMgmt/backOffice/js/saleReturnAgt.js,v $'] = '$Id: saleReturnAgt.js,v 1.1 2010/04/02 12:12:07 gaurav Exp $';