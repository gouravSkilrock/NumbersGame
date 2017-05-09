


var reportType = null;
function retrieveURL(url, div) {
	url = url + "&reportType = "+document.getElementById("reportType").value;
	reportType = document.getElementById("reportType").value;
   _resp  = _ajaxCall(url);
	if(_resp.result){
         if(reportType=='Game Pwt Status'){
  		 AddOptionsGraph("singleGame");
     
   }else{
			AddOptionsGraph("selectGames");
		} 
      } else {
        alert("Problem: ");
        }
}
function removeAllOptions(selectbox) {
var i;
for(i=selectbox.options.length-1;i>=0;i--)
{
if(i>1)
selectbox.remove(i);
}
}

function AddOptionsGraph(val)
{
    
 	document.getElementById("optionTransferDiv").innerHTML = req.responseText;
	var selectGames = document.getElementById(val);
	//alert(selectGames);
	removeAllOptions(selectGames);
	for(i=0;i<document.getElementById('selectOptionManual').options.length;i++) {
		if(i!=0){
	var key=	document.getElementById('selectOptionManual')[i].text;
	//alert(key+"  keyy   "+document.getElementById('selectOptionManual')[i].value+" text "+document.getElementById('selectOptionManual')[i].text);
		var val = document.getElementById('selectOptionManual')[i].value
		var opt = new Option(key, val);
		 selectGames.options[i] = opt;
		 }
}
}




var reportName = "";
function disable(value){

if(value=="Monthly Sale Gamewise")
{

month.style.display="block";
date.style.display="none";
select.style.display="none";
gameStatus.style.display="block";
selectOption.style.display="block";
document.na.selectGames.disabled=false;
document.na.selectedGames.disabled=false;
}
else if(value=="GameWise Sale")
{
month.style.display="none";
date.style.display="block";
select.style.display="none";
gameStatus.style.display="block";
selectOption.style.display="block";
document.na.selectGames.disabled=false;
document.na.selectedGames.disabled=false;
}
else if(value=="Yearly Sale Pricewise")
{
month.style.display="none";
date.style.display="block";
select.style.display="none";
gameStatus.style.display="block";
selectOption.style.display="block";
document.na.selectGames.disabled=false;
document.na.selectedGames.disabled=false;
}
else if(value=="Game Pwt Status")
{
month.style.display="none";
date.style.display="none";
select.style.display="block";
gameStatus.style.display="block";
selectOption.style.display="none";
document.na.selectGames.disabled=true;
document.na.selectedGames.disabled=true;
document.getElementById('selectedGamesDiv').innerHTML = "";
}
else if(value=="PWT Status Gamewise")
{
month.style.display="none";
date.style.display="none";
select.style.display="none";
gameStatus.style.display="block";
selectOption.style.display="block";
document.na.selectGames.disabled=false;
document.na.selectedGames.disabled=false;

}
else if(value=="Sale of Game Monthly PriceWise")
{
month.style.display="block";
date.style.display="none";
select.style.display="none";
gameStatus.style.display="none";
selectOption.style.display="none";
document.na.selectGames.disabled=true;
document.na.selectedGames.disabled=true;
document.getElementById('selectedGamesDiv').innerHTML = "";
}
else if(value=="Inventory Status")
{

month.style.display="none";
date.style.display="none";
select.style.display="none";
gameStatus.style.display="block";
selectOption.style.display="block";
document.na.selectGames.disabled=false;
document.na.selectedGames.disabled=false;
}
else
{
month.style.display="block";
date.style.display="none";
select.style.display="none";
selectOption.style.display="block";
}
}

function check(){
//alert("inside chcekc");
for (var i=0;i < document.forms[0].elements.length;i++)
	{
		var e = document.forms[0].elements[i];
		 if(e.name=="reportType")
		{
			reportName = e.value;
		}
		break;
	}
	if((reportName == "Game Pwt Status")||(reportName == "Sale of Game Monthly PriceWise")){
document.na.selectGames.disabled=true;
}
else
{

if(document.na.selectedGames.length>6)
{

	document.na.selectGames.disabled=true;
}
else
{

	document.na.selectGames.disabled=false;
}
}
}

 function validate(){
 var isInValid = false;
 var rep = "";

 for (var i=0;i < document.forms[0].elements.length;i++)
	{
		var e = document.forms[0].elements[i];
		 if(e.name=="reportType")
		{
			rep = e.value;
			
			if(e.value=="--Please Select--")
			{
			//alert(e.name+"--Please Select--");
			document.getElementById('reportDiv').innerHTML = "<font color = 'red'>Please Select Report Type</font>";
			isInValid=true;
			}
			else{
					
			if((e.value=="GameWise Sale")||(e.value=="Yearly Sale Pricewise"))
			{
			//alert(e.value);
					
					  var fromDt = document.getElementById("fromDate");
					  var fromDtValue = fromDt.value;
					
					   if(fromDtValue=="")
					  {
					  document.getElementById('frmdate').innerHTML = "<font color = 'red'>Please Enter From Date</font>";
					  isInValid=true;
					   }
					 else{
					   document.getElementById('frmdate').innerHTML = "";
					   }
					  
					  var to = document.getElementById("toDate");
					  var toDtValue= to.value;
					  ////alert(toDtValue);
					  
					  if(toDtValue!=""){
					  	// done by arun
					  		var toDtArr = toDtValue.split("-");
							var fromDtArr = fromDtValue.split("-");					  
					 	//alert((fromDtArr[2]+fromDtArr[1]+fromDtArr[0])+"   "+(toDtArr[2]+toDtArr[1]+toDtArr[0]))
					  if ((fromDtArr[2]+fromDtArr[1]+fromDtArr[0]) >(toDtArr[2]+toDtArr[1]+toDtArr[0])) {
					 
					 // if(fromDtValue>toDtValue){
					  document.getElementById('todate1').innerHTML = "<font color = 'red'>Please Select To date Greater than From Date</font>";
					  ////alert("Please Select to date Greater than From Date");
					  isInValid=true;
					  }
					  else{
					  document.getElementById('todate1').innerHTML = "";
					  }
					  }
			}
			document.getElementById('reportDiv').innerHTML = "";
		}	
				
		}
		if((rep == "GameWise Sale")||(rep == "Yearly Sale Pricewise")||(rep == "Game Pwt Status")||(rep == "PWT Status Gamewise")||(rep == "Inventory Status")){
		//alert(rep+"--blank--");
		}
		else{
		if (e.name == "selectMonth")
		{
		//alert(e.name+"--selectMonth--");
			if(e.value=="--Please Select--")
			{
			//alert(e.value+"--value--");
			document.getElementById('monthDiv').innerHTML = "<font color = 'red'>Please Select Month</font>";
			isInValid=true;
			}
			else{
			document.getElementById('monthDiv').innerHTML = "";
			}
		}
		}
		if((rep == "Game Pwt Status")||(rep == "Sale of Game Monthly PriceWise")){
		//alert(rep+"--blank--222");
			document.getElementById('selectedGamesDiv').innerHTML = "";
			}
			else {
			
		if (e.name == "selectedGames"){
		//alert(e.name+"--selectedGames--");
			if(e.length<3){
			//alert(e.length+"--length--");
			document.getElementById('selectedGamesDiv').innerHTML = "<font color = 'red'>Please Select Games</font>";
			isInValid=true;
			}
			else if(document.na.selectedGames.length>7)
		{
		//alert(e.length+"--else length--");
			document.getElementById('selectedGamesDiv').innerHTML = "<font color = 'red'>Please Select Only Five Games</font>";
			isInValid=true;
		}
		else{
			document.getElementById('selectedGamesDiv').innerHTML = "";
			}

		}
		}
		
	}
	//alert("isInValid"+isInValid);	
  if(isInValid){
  return false;
  }
  return true;
  }
parent.frames[0].Version['$Source: /rep/LMS_Mgmt/WebRoot/com/skilrock/lms/web/reportsMgmt/backOffice/js/graphReports.js,v $'] = '$Id: graphReports.js,v 1.1 2010/04/01 04:23:08 gaurav Exp $';