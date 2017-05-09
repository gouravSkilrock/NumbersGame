var i18nObj = parent.frames[0].i18Obj;

 
var replaceDiv;
var saleReq;    
var gameNumber;    
var lenOfPackNum;
var lenOfBookNum;
function saleReturnAjax(url,val){
 
 replaceDiv = val;
 if(val=="gameDiv"){
 document.getElementById("orgLoading").innerHTML='<img src="'+path+'/LMSImages/images/loadingAjax.gif"/>';
 disableTable();
 emptyAllFields();
 }
 if(url.match("gameName")){
 document.getElementById("gameLoading").innerHTML='<img src="'+path+'/LMSImages/images/loadingAjax.gif"/>';
 url = url+escape(document.getElementById("agtOrgName").value);
 replaceDiv = "gameName";
 disableTable();
 emptyAllFields();
 }
_resp  = _ajaxCall(url);
	if(_resp.result){
   		 if(replaceDiv=="gameDiv"){
		 document.getElementById("gameDiv").innerHTML=_resp.data;
		 document.getElementById("orgLoading").innerHTML='';
		 if(document.getElementById("gameName").options.length<2){
		 disableTable();
		 }
		 }
		if(replaceDiv=="gameName"){
		 getPacknBookArray(_resp.data);
		 document.getElementById("gameLoading").innerHTML='';
		 }
      } else {
        alert("Problem: ");
        }
}
 
function verifyOrg(orgDiv,orgName,msg){
 
  if(_id.o(orgDiv).value!="-1"){
	  var returnType=confirm(msg+" " + orgName)
	  if(!returnType) {
	       _id.o(orgDiv).selectedIndex=0
	       _id.iniFocus(orgDiv);
	       return false;
	   }
  }
  return true;  
}

    
var bookArr = new Array();
var packArr = new Array();

function getPacknBookArray(responseData){

var packArrStr = responseData.split("book_nbr")[0];
if(packArrStr!=""){
packArr = ((packArrStr).substring(0,packArrStr.lastIndexOf(':'))).split(":");
lenOfPackNum = packArr[0].length;
}else{
packArr = new Array();
}
//alert(packArr.length+"-------"+packArrStr+"*******");
if(packArr.length<1){
document.getElementById("packRow").innerHTML = "<center>"+tierMap['AGENT']+i18nObj.prop('error.doesnot.have.complete.pack.ofThis.game')+"</center>";
}
else{
document.getElementById("packRow").innerHTML ='<table id="packTable" width="100%" border="0" cellpadding="2" cellspacing="0"><tr><td align="left" colspan="2"><input type="button" value="Add More Pack" name="addPack" id="addPack" class="button" onclick="addPackRow()"/>	</td> </tr><tr>	<td colspan="3"><div id="packErrDiv"></div>	</td></tr>	<tr><td width="20%">Pack Number :</td><td width="15%"><input type="text" name ="packNumber" id="packNbr1" value="" size="16"/></td><td><div id="packNbr1Div">	</div></td>	</tr></table>';
setAutoComplete(packArr,'pack');
}

var bookArrStr = responseData.split("book_nbr")[1];
if(bookArrStr!=""){
	bookArr = ((bookArrStr).substring(0,bookArrStr.lastIndexOf(':'))).split(":");
	lenOfBookNum = bookArr[0].length;
	}else{
		bookArrStr = new Array();
	}
/*if(bookArrStr!="")
bookArr = ((bookArrStr).substring(0,bookArrStr.lastIndexOf(':'))).split(":");
lenOfBookNum = bookArr[0].length;*/
if(packArr.length<1&&bookArr.length<1){
disableTable();
}else{
document.getElementById("completeDiv").style.display="none";
gameNumber = bookArr[0].split("-")[0];


}

setAutoComplete(bookArr,'book');
}

function setAutoComplete(data,type){
	for (var i=0;i < document.forms[0].elements.length;i++)
		{
		var e = document.forms[0].elements[i];
						if(type=="book"){
							condition = e.name=="bookNumber"||e.name=="bookSeriesFrom"||e.name=="bookSeriesTo";
						}else if(type=="pack"){
							condition = e.name=="packNumber";
						}
				if(condition){
				AutoComplete_Create(e.id, data);
				}
		}

}

function isPackNbrValid(packNbr){
	for(var i=0;i<packArr.length;i++){
		if(packNbr==packArr[i]){
		return true;
		}
	}
	return false;
}
function isBookNbrValid(bookNbr){
	for(var i=0;i<bookArr.length;i++){
		if(bookNbr==bookArr[i]){
		return true;
		}
	}
	return false;
}

function isBookInPack(bookNbr){
var currentPackNbr=document.getElementsByName('packNumber');
				for(var m=0;m<currentPackNbr.length;m++){
					if(currentPackNbr[m].value!=""){
						if(bookNbr.match(currentPackNbr[m].value)){
						return true;
						}
					}
				}
return false;
}

function isSeriesValid(bookNbrFrom,bookNbrTo){
  var frmLast =(bookNbrFrom).split("-")[1];
  var toLast =(bookNbrTo).split("-")[1];
  var fromLocation;
  var toLocation;
				for(var i=0;i<bookArr.length;i++){
					if(bookArr[i]==bookNbrFrom){
						fromLocation=i;
					}else if(bookArr[i]==bookNbrTo){
						toLocation =i;
						break;
					} 
				}
				//alert(toLocation+"*-*"+fromLocation+"-*-"+toLast+"*-*"+frmLast);
				if((toLocation-fromLocation)==(toLast-frmLast)){
				return true;
				}
return false;
}

function isSeriesInAnotherSeries(bookNbrFrom,bookNbrTo,frmArr,toArr,location){
		for(var i=0;i<frmArr.length;i++){
		if(frmArr[i]!=""&&toArr[i]!=""){
				if(i!=location){
						if(bookNbrFrom==bookNbrTo){
								if(frmArr[i]<bookNbrFrom&&toArr[i]>bookNbrFrom){
									return true;
								} 
						}else{
								if((frmArr[i]<bookNbrFrom&&bookNbrFrom<toArr[i])||frmArr[i]<bookNbrTo&&bookNbrTo<toArr[i]){
								return true;
								}
						}
				}	
			}
		}
	return false;
}

function isBookNbrInSeries(bookNbrFrom,bookNbrTo,bookNbr){
				for(var i=0;i<bookNbrFrom.length;i++){
					if(bookNbrFrom[i]<=bookNbr&&bookNbrTo[i]>=bookNbr&&bookNbr!=""){
						return true;
					} 
				}
return false;
}

function addBookSeriesRow(){
	if(isFieldEmpty('isBookSeriesEmpty')){
	return false;
	}
	var table = document.getElementById("bookSeriesTable");
	var lastRow = table.rows.length;
	var row = table.insertRow(lastRow);
	lastRow=lastRow-1;
	var cellLeft = row.insertCell(0);
	cellLeft.setAttribute("nowrap", "true");
	cellLeft.innerHTML='Book Number From :';
	
	var cellLeft1 = row.insertCell(1);
	cellLeft1.innerHTML='<input type="text" name="bookSeriesFrom"  id="bookNbrFmSe'+lastRow+'" size="16"/>';
	
	var cellLeft2 = row.insertCell(2);
	
	AutoComplete_Create('bookNbrFmSe'+lastRow, bookArr);
	
	cellLeft2.innerHTML='To :&nbsp;';

	var cellLeft3 = row.insertCell(3);

	cellLeft3.innerHTML='<input type="text" name="bookSeriesTo" id="bookNbrToSe'+lastRow+'" size="16"  />';

	var cellLeft4 = row.insertCell(4);
	cellLeft4.innerHTML='<div name="serStatusDiv" id="bookNbrSe'+(parseInt(lastRow))+'Div">&nbsp;</div>';
	
	AutoComplete_Create('bookNbrToSe'+lastRow, bookArr);
}

function addBookRow(){
	if(isFieldEmpty('isBookEmpty')){
	return false;
	}
	var table = document.getElementById("bookTable");

	var lastRow = table.rows.length;
	var row = table.insertRow(lastRow);
	lastRow=lastRow-1;
	var cellLeft = row.insertCell(0);
	cellLeft.innerHTML='Book Number :';
	var cellLeft2 = row.insertCell(1);
	
	cellLeft2.innerHTML='<input type="text"	name="bookNumber" id="bookNbr'+lastRow+'" size="16" />';
	var cellLeft3 = row.insertCell(2);
	cellLeft3.innerHTML='<div id="bookNbr'+lastRow+'Div">&nbsp;</div>';
//alert(cellLeft.innerHTML);
	AutoComplete_Create('bookNbr'+lastRow, bookArr);
}

function addPackRow(){
	if(isFieldEmpty('isPackEmpty')){
	return false;
	}
	var table = document.getElementById("packTable");
	var lastRow = table.rows.length;
	
	var row = table.insertRow(lastRow);
	lastRow=lastRow-1;
	var cellLeft = row.insertCell(0);
	cellLeft.innerHTML='Pack Number :';
	var cellLeft2 = row.insertCell(1);
	
	cellLeft2.innerHTML='<input type="text"	name="packNumber" id="packNbr'+lastRow+'" size="16" />';
	var cellLeft3 = row.insertCell(2);
	cellLeft3.innerHTML='<div id="packNbr'+lastRow+'Div">&nbsp;</div>';
	
	AutoComplete_Create('packNbr'+lastRow,packArr);
}

function isFieldEmpty(check){
var condition;
		for (var i=0;i < document.forms[0].elements.length;i++)
			{
				var e = document.forms[0].elements[i];
				if(check=="isBookSeriesEmpty"){
				condition = e.name=="bookSeriesFrom"||e.name=="bookSeriesTo";
				}else if(check=="isBookEmpty"){
				condition = e.name=="bookNumber";
				}else if(check=="isPackEmpty"){
				condition = e.name=="packNumber";
				}
				if(condition){
					if(e.value==""){
					return true;
					}
				}
			}
return false;

}
function isAllFieldEmpty(){
	for (var i=0;i < document.forms[0].elements.length;i++)
	{
		var e = document.forms[0].elements[i];
		if(e.name=="bookNumber"||e.name=="bookSeriesFrom"||e.name=="bookSeriesTo"||e.name=="packNumber"){
			if(e.value!=""){			
			return false;
			}
		}
	}
return true;
}

function emptyAllFields(){
	for (var i=0;i < document.forms[0].elements.length;i++)
	{
		var e = document.forms[0].elements[i];
		if(e.name=="bookNumber"||e.name=="bookSeriesFrom"||e.name=="bookSeriesTo"||e.name=="packNumber"){
			if(e.value!=""){			
			document.getElementById(e.id).value="";
				if(e.name=="bookNumber")
				document.getElementById(e.id+'Div').innerHTML="";
			
				else if(e.name=="bookSeriesFrom")
				document.getElementById(e.id.replace("Fm","")+'Div').innerHTML="";
			
				else if(e.name=="packNumber")
				document.getElementById(e.id+'Div').innerHTML="";
			}
		}
	}
}


function addHyphenToValues(){
for (var i=0;i < document.forms[0].elements.length;i++)
	{
		var e = document.forms[0].elements[i];
		if(e.name=="bookNumber"||e.name=="bookSeriesFrom"||e.name=="bookSeriesTo"||e.name=="packNumber"){
			if(e.value!=""){
			if(e.value.length>gameNumber.length){
			if(!e.value.match("-"))
			
			document.getElementById(e.id).value=e.value.substring(0,gameNumber.length)+"-"+e.value.substring(gameNumber.length,e.value.length);
			}
			}
		}
	}

}



function ValidateBookSeries(bookNbrFrom,bookNbrTo){
isValid = true;	

  for(var i=0;i<bookNbrFrom.length;i++){
  isCurrentValid = true;
  bookNbrFrom[i]=document.getElementsByName('bookSeriesFrom')[i].value;
  bookNbrTo[i]=document.getElementsByName('bookSeriesTo')[i].value;
  //alert( bookNbrFrom[i]+"---"+bookNbrTo[i]);
  if(bookNbrFrom[i]!=""&&bookNbrTo[i]!=""&&bookNbrFrom[i].length>3&&bookNbrTo[i].length>3){
  	  var bookNbrFrmArr = bookNbrFrom;
	  var bookNbrToArr = bookNbrTo;
	  var dup =0;
	  for(var j=0;j<bookNbrFrmArr.length;j++){
	  	if(bookNbrFrom[i]==bookNbrFrmArr[j]||bookNbrFrom[i]==bookNbrToArr[j]||bookNbrTo[i]==bookNbrToArr[j]||bookNbrTo[i]==bookNbrFrmArr[j]){
	  	dup++;
	  	}
	  }
	  if(dup>1){
	  document.getElementById('bookNbrSe'+(i+1)+'Div').innerHTML=i18nObj.prop('error.duplicate.booknum');
	  isValid=false;
      isCurrentValid = false; 
	  }
  if(bookNbrFrom[i].length!=bookNbrTo[i].length){
  document.getElementById('bookNbrSe'+(i+1)+'Div').innerHTML=i18nObj.prop('error.pls.entr.corct.booknum');
  isValid=false;
  isCurrentValid=false;
  }else if(bookNbrFrom[i]>bookNbrTo[i]){
  document.getElementById('bookNbrSe'+(i+1)+'Div').innerHTML=i18nObj.prop('error.from.booknum.greater.then.to.booknum');
  isValid=false;
  isCurrentValid=false;
  }
  //  alert(gameNumber+"---"+gameNumber.length);
  if(bookNbrFrom[i].substring(0,gameNumber.length)!=gameNumber){
  document.getElementById('bookNbrSe'+(i+1)+'Div').innerHTML=i18nObj.prop('error.booknum.from.not.of.selected.game');
  isValid=false;
  isCurrentValid=false;
  }
  if(bookNbrTo[i].substring(0,gameNumber.length)!=gameNumber){
  document.getElementById('bookNbrSe'+(i+1)+'Div').innerHTML=i18nObj.prop('error.to.not.of.selected.game');
  isValid=false;
  isCurrentValid=false;
  }
  var arrFrm =(bookNbrFrom[i]).split("-");
  var arrTo =(bookNbrTo[i]).split("-");
  //alert(arrFrm.length+"--"+arrTo.length);
  if(arrFrm.length>2||arrTo.length>2){
  document.getElementById('bookNbrSe'+(i+1)+'Div').innerHTML=i18nObj.prop('error.pls.use.only.oneIn.booknum');
  isValid=false;
  isCurrentValid=false;
  }

  if(isCurrentValid){
  		  	if(bookNbrFrom[i]!=bookNbrTo[i]&&!isSeriesValid(bookNbrFrom[i],bookNbrTo[i])){
		  	  document.getElementById('bookNbrSe'+(i+1)+'Div').innerHTML=i18nObj.prop('error.some.booknumIn.series.are.missing');
			  isValid=false;
			  isCurrentValid=false;
		  	}else{
			  		if(isSeriesInAnotherSeries(bookNbrFrom[i],bookNbrTo[i],bookNbrFrom,bookNbrTo,i)){
			  		  	  document.getElementById('bookNbrSe'+(i+1)+'Div').innerHTML=i18nObj.prop('error.booknumIn.series.areIn.another.series');
						  isValid=false;
						  isCurrentValid=false;
			  		}
		  		}
		  	if(isBookInPack(bookNbrFrom[i])){
		  				  document.getElementById('bookNbrSe'+(i+1)+'Div').innerHTML=i18nObj.prop('error.book.series.alreadyIn.pack.selected');
						  isValid=false;
						  isCurrentValid=false;
		  	}	
  		
  	}
  }
  else if(bookNbrFrom[i]==""&&bookNbrTo[i]==""){
  document.getElementById('bookNbrSe'+(i+1)+'Div').innerHTML="";
  }
  else{
  document.getElementById('bookNbrSe'+(i+1)+'Div').innerHTML=i18nObj.prop('error.pls.entr.correct.values');
	isValid=false;
	isCurrentValid = false; 
  }
  if(isCurrentValid){
  document.getElementById('bookNbrSe'+(i+1)+'Div').innerHTML="";
  }


}


var book = document.getElementsByName('bookNumber');
		for(i=0; i< book.length; i++){
			isCurrentValid = true;
			if(book[i].value != ""){
				if((book[i].value).substring(0,gameNumber.length)!=gameNumber||(book[i].value).length<4){
				  document.getElementById('bookNbr'+(i+1)+'Div').innerHTML=i18nObj.prop('error.pls.entr.corct.booknum');
				  isValid=false;
				  isCurrentValid = false;
				}else if(((book[i].value).split("-")).length>2){
					document.getElementById('bookNbr'+(i+1)+'Div').innerHTML=i18nObj.prop('error.pls.use.only.oneIn.booknum');
				  isValid=false;
				  isCurrentValid = false;
				}
				else {
				
					var isDup = false;
					for(var j=0;j<book.length;j++){
						if(i!=j){
							if(book[i].value==book[j].value){
							isDup = true;
							break;
							}
						}
					}
					
						if(isDup){
						  document.getElementById('bookNbr'+(i+1)+'Div').innerHTML=i18nObj.prop('error.duplicate.booknum');
						  isValid=false;
						  isCurrentValid = false;
					  }
				}
				if(!isBookNbrValid(book[i].value)){
				document.getElementById('bookNbr'+(i+1)+'Div').innerHTML=i18nObj.prop('error.invalid.booknum');
				isValid=false;
				isCurrentValid = false;
				}
			}
				if(isCurrentValid){
				  if(isBookNbrInSeries(bookNbrFrom,bookNbrTo,book[i].value)){
				  document.getElementById('bookNbr'+(i+1)+'Div').innerHTML=i18nObj.prop('error.booknumIn.series');
				  isValid=false;
				  isCurrentValid = false;
				  }else{
				  document.getElementById('bookNbr'+(i+1)+'Div').innerHTML="";
				  }
				  }
				  if(isBookInPack(book[i].value)&&book[i].value!=""){
		  			document.getElementById('bookNbr'+(i+1)+'Div').innerHTML=i18nObj.prop('error.booknum.alreadyIn.pack.selected');
					isValid=false;
					isCurrentValid=false;
		  	}
				
		}
		
	
return isValid;
}




  function 	VerifyBooks(orgType){
	  document.getElementById("errorDiv").innerHTML="";
	 
	  if(orgType == 'BO')
	  {
		  var agtId =document.getElementById("agtOrgName").value;
			if(agtId==-1){
				  document.getElementById("errorDiv").innerHTML =i18nObj.prop('error.pls.slct.org') ;
				return false;
			}
	  }else if(orgType == 'AGENT'){
		  var retId =document.getElementById("retName").value;
			if(retId==-1){
				document.getElementById("errorDiv").innerHTML =i18nObj.prop('error.pls.slct.org')  ;
				return false;
			}
		  
	  }
	  if(document.getElementById("gameName").value==-1) {
		  document.getElementById("errorDiv").innerHTML =i18nObj.prop('error.pls.slct.game');
		  return false;
		  
	  }
  addHyphenToValues();
  
  var bookNbrFrom= new Array();
  var bookNbrTo= new Array();
  for(var i=0;i<document.getElementsByName('bookSeriesFrom').length;i++){
  bookNbrFrom[i]=document.getElementsByName('bookSeriesFrom')[i].value;
  bookNbrTo[i]=document.getElementsByName('bookSeriesTo')[i].value;
  }
  var pack = validatePack();
  var book = ValidateBookSeries(bookNbrFrom,bookNbrTo);
  var allFieldsEmpty = isAllFieldEmpty();  
  if(pack&&book&&!allFieldsEmpty) {

	  if(orgType == 'BO')
	  {
		document.getElementById("agtName").value =  document.getElementById("agtOrgName").options[document.getElementById("agtOrgName").selectedIndex].text;
		
	  } else if(orgType == 'AGENT'){
		  document.getElementById("retName").value =  document.getElementById("ret_org_name").options[document.getElementById("ret_org_name").selectedIndex].text;  
		 
	  
	  }
		

	  return true;
  }

  return false;
}


function validatePack(){
isValid = true;	
var currentPackNbr=document.getElementsByName('packNumber');
				for(var m=0;m<currentPackNbr.length;m++){
					isCurrentValid = true;
					if(currentPackNbr[m].value!=""){
						if(isPackNbrValid(currentPackNbr[m].value)){
							var isDup = false;
						for(var j=0;j<currentPackNbr.length;j++){
							if(m!=j){
								if(currentPackNbr[m].value==currentPackNbr[j].value){
								isDup = true;
								break;
								}
							}
						}
						
							if(isDup){
							  document.getElementById('packNbr'+(m+1)+'Div').innerHTML=i18nObj.prop('error.duplicate.packnbr');
							  isValid=false;
							  isCurrentValid = false;
							 
						  }
						}else{
						document.getElementById('packNbr'+(m+1)+'Div').innerHTML=i18nObj.prop('error.invalid.packnbr');
						isValid = false;	
						isCurrentValid = false;
						}
						if(isCurrentValid){
						document.getElementById('packNbr'+(m+1)+'Div').innerHTML="";
						}
					}else{
					document.getElementById('packNbr'+(m+1)+'Div').innerHTML="";
					}
					
				}
		return isValid;

}




function disableTable(){
var completeDiv = document.getElementById("completeDiv");
completeDiv.style.display="block";
completeDiv.style.height=document.getElementById("completeTable").offsetHeight;
completeDiv.style.width=document.getElementById("completeTable").offsetWidth;

} 

function verifyAgent(agtName)
{
	var e = document.getElementById("agtOrgName");
	var orgDisplayName = e.options[e.selectedIndex].text;
  if(e.value!="-1")
  {
  var returnType=confirm("You have selected "+tierMap['AGENT']+":: " + orgDisplayName)
  if(returnType)    
    {     
    // return true;
    }
   else
   {
       document.getElementById('agtOrgName').selectedIndex=0
       document.getElementById('gameName').focus();
       //return false;
   }
  }  
}
function getAgentList() {
	var text = _ajaxCall(projectName+"/com/skilrock/lms/web/userMgmt/getAgentNameList.action?orgType=AGENT");
	var firstArr = text.data.split(":");
	var retObj = _id.o("agtOrgName");

	for ( var i = 0; i < firstArr.length - 1; i=i+1) {
		var retNameArray = firstArr[i].split("|");
		var opt = new Option(retNameArray[0].toUpperCase(), retNameArray[1]);
		retObj.options[i + 1] = opt;
	}
}
function fetchRetOrgList() {
	var text = _ajaxCall(projectName+"/com/skilrock/lms/web/userMgmt/getOrgList.action?orgType=RETAILER");
	var firstArr = text.data.split(":");
	var retObj = _id.o("ret_org_name");

	for ( var i = 0; i < firstArr.length - 1; i=i+1) {
		var retNameArray = firstArr[i].split("|");
		var opt = new Option(retNameArray[0].toUpperCase(), retNameArray[1]);
		retObj.options[i + 1] = opt;
	}
	
}
parent.frames[0].Version['$Source: /rep/LMS_Mgmt/WebRoot/com/skilrock/lms/web/scratchService/inventoryMgmt/backOffice/js/saleReturn.js,v $'] = '$Id: saleReturn.js,v 1.3 2016/10/31 09:46:20 neeraj Exp $';