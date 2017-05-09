

function checkInputs() {
	var flag=true;
	if(document.getElementById('totalPrice').value=="" || document.getElementById('totalPrice').value=="0")
		flag=false;
	if(document.getElementById('noOfTickets').value=="") 
		flag=false;
	if(flag);
	else alert("Please Enter tickets to Buy");

	return flag;
}



function show() {
	var Digital=new Date();
	var hours=Digital.getHours();
	var minutes=Digital.getMinutes();
	var seconds=Digital.getSeconds();
	var dn="AM"; 
	
	if (hours>12) {
		dn="PM";
		hours=hours-12;
	}
	if (hours==0)	hours=12;
	
	if (minutes<=9) minutes="0"+minutes;
	if (seconds<=9) seconds="0"+seconds;
	document.getElementById('Clock').innerHTML=hours+":"+minutes+":"+seconds+" "+dn;
	setTimeout("show()",1000);
}
show();


function totalPriceAndTicket() {

	var noOfTickets=0;
	for(var i=1;i<=12;i=i+1) {
		if(document.getElementById('col'+i).value=="");
		else {
			var tickets=parseInt(document.getElementById('col'+i).value);
			noOfTickets=noOfTickets+tickets;
		}
	}

	document.getElementById('noOfTickets').value=noOfTickets;
	document.getElementById('totalPrice').value=noOfTickets*20;
}

function uncheckedIsQuickPick () {	
		
		if(document.getElementById('isQuickPick').checked==true) {
			document.getElementById('isQuickPick').checked=false;		
			document.getElementById('noOfTickets').readOnly=true;
			document.getElementById('noOfTickets').value="0";
			document.getElementById('totalPrice').value="0";
			document.getElementById('noOfTickets').style.backgroundColor='white';
		}
		
}

// by arun
function isQuickPickClick(){

	if(document.getElementById('isQuickPick').checked==true) {		
		document.getElementById('noOfTickets').value="0";
		document.getElementById('totalPrice').value="0";
		document.getElementById('noOfTickets').readOnly=false;
		document.getElementById('noOfTickets').focus();
		document.getElementById('noOfTickets').style.backgroundColor='yellow';
		
		for(var i=1;i<=12;i=i+1) {
			document.getElementById('col'+i).value="";				
		}
	
	}
	else {	
	
		document.getElementById('noOfTickets').value="0";
		document.getElementById('noOfTickets').readOnly=true;
		document.getElementById('noOfTickets').style.backgroundColor='white';	
	}	
}



function quickPick() {
	
	if(document.getElementById('isQuickPick').checked==true) {	
		for(var i=1;i<=12;i=i+1) {
			document.getElementById('col'+i).value="";				
		}
		var noOfTicket;
		if(document.getElementById('noOfTickets').value=="")  
			noOfTicket=0;
		else  
			noOfTicket=parseInt(document.getElementById('noOfTickets').value);

		document.getElementById('totalPrice').value=noOfTicket*20;
	
		for(var i=1; i<=noOfTicket;) {	
				var row=Math.floor(Math.random()*13);
				
				if(row>0) {
					if(document.getElementById('col'+row).value==""){
						 document.getElementById('col'+row).value=0;
						document.getElementById('col'+row).value=1;
						//alert("inside if");
					}
					else
					document.getElementById('col'+row).value=parseInt(document.getElementById('col'+row).value)+1;
					i++;
				} 
		}
	}
}




  var radioLabelValue = 1;    
  var radioLabelNewVal=1;  
  var radioPanelChecked=0;  
  
  function setPanel(id){
   	radioPanelChecked= parseInt( id.substring(5,id.length));
  }  
    
  
  function imageSelection(val) {	
	var rowNo=parseInt(val.substring(0,1));	
	var imageid;
	for(var i=1; i<=12; i++) {
		imageid=rowNo+'col'+i;
			
		if(imageid==val) { 		
		} else {
			document.getElementById(imageid).className = 'align5';
			document.getElementById(imageid).style.borderColor = 'black';
			document.getElementById(imageid).style.borderWidth = '1px'
		}
	}	
	document.getElementById('isQuickPick'+rowNo).checked = false;
	document.getElementById('panel'+rowNo).checked=true;
	
	if(document.getElementById(val).className=="align5"){
		document.getElementById(val).className = 'imageWithBorder';
		document.getElementById(val).style.borderColor = '#0000ff';
		document.getElementById(val).style.borderWidth = '3px'
	}
	else if(document.getElementById(val).className=="imageWithBorder") {
			document.getElementById(val).className = 'align5';
			document.getElementById(val).style.borderColor = 'black';
			document.getElementById(val).style.borderWidth = '1px'
	}
}
  
  
  
function radioLabel(){
var tab=document.getElementById('panel');
var lng=tab.rows.length;
for(var k = 0 ;k<lng;k++){
if(document.getElementById("radioLabel"+k)==null){
}
else{
radioLabelNewVal++;
}
}
}


function emptyRow(row){
	var table = document.getElementById("panel");  
	//alert("innnnnnnnnnn"+row);  
	var lastRow = row-1;	
	var row = table.insertRow(row);
	row.setAttribute("id","rowDiv" + lastRow+"r");
	var cell1=row.insertCell(0);
    cell1.innerHTML="";
	var cell2=row.insertCell(1);
	cell2.setAttribute("width","1%");
	cell2.setAttribute("align","center");
	cell2.innerHTML="";
	var cell3=row.insertCell(2);
	cell3.setAttribute("width","1%");
	cell3.setAttribute("align","center");
	cell3.innerHTML="";
	var cell4=row.insertCell(3);
	cell4.setAttribute("width","1%");
	cell4.setAttribute("align","center");
	cell4.innerHTML="";
	var cell5=row.insertCell(4);
	cell5.setAttribute("width","1%");
	cell5.setAttribute("align","center");
	cell5.innerHTML="";
	var cell6=row.insertCell(5);
	cell6.innerHTML="";
	var cell7=row.insertCell(6);
	cell7.innerHTML="";	
	var cell8=row.insertCell(7);
	cell8.innerHTML="";
	var cell9=row.insertCell(8);
	cell9.innerHTML="";
	
}
function checkpic(){
if(document.getElementById("gameTypepick3").checked==true){

	 var tab=document.getElementById('panel');
 	 var lng=tab.rows.length;
	 for(var i=0;i<lng;i++){
		if( document.getElementById(i+"col4")!=null){			
			document.getElementById(i+"col4").value="0";
		  }
	  }	
	}
}
function pick3pick4(id)
{
 var tab=document.getElementById('panel');
 var lng=tab.rows.length;
 
	 if(document.getElementById(id).value.match("pick4")==null){

	 for(var i=0;i<lng;i++){
		if( document.getElementById(i+"col4")!=null){
		document.getElementById(i+"col4").value="";
			document.getElementById(i+"col4").disabled=true;
			document.getElementById(i+"col4").style.background="d4eefd";
		  }
	  }	
	}
	else{

	 for(var i=0;i<lng;i++){
		if( document.getElementById(i+"col4")!=null){
			document.getElementById(i+"col4").disabled=false;
				document.getElementById(i+"col4").style.background="ffffff";
		  		document.getElementById(i+"col4").className = "BorderOff";
		  }
	  }	
	 }
}






function playOption(id){
   
    if(document.getElementById(id).value.match("NumberOfDraws")==null){
    document.getElementById("noOfDrawsPlayedFor").disabled=true;
      document.getElementById("advancePlay").disabled=false;
    
    }else{
    document.getElementById("noOfDrawsPlayedFor").disabled=false;
     document.getElementById("advancePlay").disabled=true;
     }
    }   
    
    function insertNo(id){
    if(radioPanelChecked!=0){ 
    var col = id.substring(0,4);    
    document.getElementById(radioPanelChecked+""+col).value=document.getElementById(id).value;
     }
    else{
    alert("Please Select Panel");
    }
    }
    
 
 
 
 //this methosd called when form submitted
 function isChecked(){       
     var tab=document.getElementById('panel');
     var lng=tab.rows.length;
     
     alert("total no. of rows = "+lng);
     
     for(var i=1;i<=lng;i++) alert("isQuickPick"+i+" = "+document.getElementById("isQuickPick"+i));
     
     
     
     for(var i=1;i<=lng;i++){
     	if(document.getElementById("isQuickPick"+i)!=null){
        	if(document.getElementById(i+"col1").value!=""){        
         		if(document.getElementById("isQuickPick"+i).checked==true){
              		document.getElementById("isQuickPick"+i).value=true;
          }
          else{
              document.getElementById("isQuickPick"+i).checked=true;
              document.getElementById("isQuickPick"+i).value=false;
          }
          
          }
       }
      }   
    } 
    



function clear1(){   
     var tab=document.getElementById('panel');     
     var lng=tab.rows.length;
     var i=0; 
     for( i=1;i<=lng;i++){
     if(document.getElementById("panel" +i)==null){
     }else{
     if(document.getElementById("panel" +i).checked==true){   
     
     document.getElementById( i+"col1").value="";
      document.getElementById( i+"col2").value="";
       document.getElementById( i+"col3").value="";
        document.getElementById( i+"col4").value="";
     break;
     }
   
     }
     }
   
    if(i>lng){
    alert("please select panel");
    }
    }
    function clearAll(){
     var tab=document.getElementById('panel');     
     var lng=tab.rows.length;
     var i=0; 
     for( i=1;i<=lng;i++){
     if(document.getElementById("panel" +i)==null){
     }else{
     document.getElementById( i+"col1").value="";
      document.getElementById( i+"col2").value="";
       document.getElementById( i+"col3").value="";
        document.getElementById( i+"col4").value="";
    
   
     }
     }
   
   
    }
    
    function onKeyPressBlockNumbers(e)
{
	var e = event || evt;
	var charCode = e.which || e.keyCode;
	if (charCode >= 48 && charCode < 58) {
		return true;
	}
	alert("Please Insert only Number");
	return false;
}
 function checkRange(id){
 if(document.getElementById(id).value>9){
 alert("please enter value between 0-9");
 document.getElementById(id).value="";
 document.getElementById(id).focus();
 }
 }
 

function dispDate(dateVal) {
DaystoAdd=dateVal
TodaysDate = new Date();

var hour=TodaysDate.getHours();


TodaysDay = new Array('Sun', 'Mon', 'Tue','Wed', 'Thu', 'Fri', 'Sat');
TodaysMonth = new Array('Jan', 'Feb', 'Mar','Apr', 'May','Jun', 'Jul', 'Aug', 'Sep','Oct', 'Nov', 'Dec');
DaysinMonth = new Array('31', '28', '31', '30', '31', '30', '31', '31', '30', '31', '30', '31');
function LeapYearTest (Year) {
if (((Year % 400)==0) || (((Year % 100)!=0) && (Year % 4)==0)) {
return true;
}
else {
return false;
}
}
CurrentYear = TodaysDate.getFullYear();
if (CurrentYear < 2000) 
CurrentYear = CurrentYear + 1900;
currentMonth = TodaysDate.getMonth();
DayOffset = TodaysDate.getDay();
currentDay = TodaysDate.getDate();
month = TodaysMonth[currentMonth];
if (month == 'Feb') {
if (((CurrentYear % 4)==0) && ((CurrentYear % 100)!=0) || ((CurrentYear % 400)==0)) {
DaysinMonth[1] = 29;
}
else {
DaysinMonth[1] = 28;
}
}
days = DaysinMonth[currentMonth];
currentDay += DaystoAdd;
if (currentDay > days) {
if (currentMonth == 11) {
currentMonth = 0;
month = TodaysMonth[currentMonth];
CurrentYear = CurrentYear + 1
}
else {
month =
TodaysMonth[currentMonth+1];
}
currentDay = currentDay - days;
}
DayOffset += DaystoAdd;
function offsettheDate (offsetCurrentDay) {
if (offsetCurrentDay > 6) {
offsetCurrentDay -= 6;
DayOffset = TodaysDay[offsetCurrentDay-1];
offsettheDate(offsetCurrentDay-1);
}
else {
DayOffset = TodaysDay[offsetCurrentDay];
return true;
}
}
offsettheDate(DayOffset);TheDate = DayOffset + ', ';
TheDate += month + ' ';
TheDate += currentDay + ', '; 
if (CurrentYear<100) CurrentYear="19" + CurrentYear;
TheDate += CurrentYear;
document.write(' '+TheDate);
}

 
 parent.frames[0].Version['$Source: /rep/LMS_Mgmt/WebRoot/com/skilrock/lms/web/userMgmt/backOffice/js/fortuneDream.js,v $'] = '$Id: fortuneDream.js,v 1.2 2016/10/31 11:40:07 neeraj Exp $';