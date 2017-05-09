var _vf=false, _isS= false;

function onSub(){
if(_vf){
for(var i=0;i<count;i++){
var tbl ='gameBookTable'+i;
var divEl = _id.o(tbl).getElementsByTagName("div");
for(var j=0,l=divEl.length;j<l;j++){
if(divEl[j].innerHTML.replace(/-/g,'')!=""){
alert("Please remove invalid entries first");hidSubmit();
return false;
}}}}
else{alert("Please Verify entries first");hidSubmit();return false;}
if(_isS=="false"){
	alert("Balance Limit is not Enough to Proceed for direct Sale");hidSubmit();return false;
}

 var returnType=confirm("Do You Want to Proceed");
   if(returnType)    
    { _id.i('subCheck',lodDiv);
	_id.hid('subTrans');_id.hid('verBk');
    } else{return false;
   }
return true;
}

function verifyRetailer()
{
	var e = document.getElementById("retOrgName");
	var orgDisplayName = e.options[e.selectedIndex].text;
  return verifyOrg('retOrgName',orgDisplayName,'You have selected '+tierMap['RETAILER']+':',e.value);
}

function hidSubmit(){
_vf=false;_id.hid('subTrans');
}

function verifyOrg(orgDiv,orgName,msg,orgId){
 hidSubmit();
  if(_id.o(orgDiv).value!="-1"){
	  var returnType=confirm(msg+" " + orgName);
	  if(returnType) {
	    // var arrVirn=document.getElementsByName('virnCode');
	     //arrVirn[0].focus();
	     _id.i('retOrgDiv','');_id.i('retBal',ACArr[orgId]);_id.vis('retBalLbl');
	      return true;
	    }else{
	       _id.o(orgDiv).selectedIndex=0;
	       _id.iniFocus(orgDiv);_id.i('retBal','');_id.hid('retBalLbl');
	       return false;
	   }
  }else{_id.i('retBal','');_id.hid('retBalLbl');}  
}
   function addAnotherGame(){
  
   if(count<gmInSelBx){		
	var table = _id.o("gameTab");
	var lastRow = table.rows.length;
	var row = table.insertRow(lastRow);
	var cellLeft = row.insertCell(0);
	var html="";
	var tdHtmlData = '<tr><td><div id="gamemessage'+count+'"></div><table id="gameBookTable'+count+'" cellpadding="3" cellspacing="0" border="0" bordercolor="#CCCCCC" width="710"><tr><td colspan="2"><i>Game Number: &nbsp;</i><select name="gameName" id="gameSel_'+count+'" class="option" onchange="chkDupGame(this.id)"><option value="-1">--Please Select--</option></td></tr><tr><td colspan="2"><input type="button" name="b1" value="Add Book"  onclick="addBookRowStatic(\'book_'+count+'\')" class="button" />&nbsp;&nbsp;&nbsp;<input type="button" name="bookSeries" onclick="addBookSeriesStatic(\'bookseries_'+count+'\')" value="Add Book Series" class="button" /></td></tr>';
	tdHtmlData+='<tr><td><table id="book_'+count+'"></table></td></tr>	<tr><td><table id="bookseries_'+count+'"></table></td></tr></table></td></tr></br></br>'
	//alert(count+""+lastRow);
	cellLeft.innerHTML=tdHtmlData;
	for(var m=0;m<3;m++)
	addBookRowStatic('book_'+count);addBookSeriesStatic('bookseries_'+count);
	fillSelect('gameSel_'+count);
	altRowColor('gameBookTable'+count,count);
      count++;
      }else{_id.n('addGame')[0].style.visibility="hidden";}
}


function addBookRowStatic(buttonId){
   var arr = new Array();
   arr = buttonId.split("_");
    var table = _id.o(buttonId);
   // alert(table + table.rows.length);
	var lastRow = table.rows.length;
	var row = table.insertRow(lastRow);
	var cellLeft = row.insertCell(0);
	cellLeft.setAttribute("nowrap","nowrap");
	var html='Book Number: <input type="text" name="bookNumber_'+arr[1]+'" id="'+lastRow+'bookId_'+arr[1]+'" onkeydown="return checkNum(this.id)" onkeyup="return checkNum(this.id)"/>';
	cellLeft.innerHTML=html;
	var cellLeft1 = row.insertCell(1);
	cellLeft1.setAttribute("nowrap","nowrap");
	var html1='<div id="'+lastRow+'bookId_'+arr[1]+'Err"></div>';
	cellLeft1.innerHTML=html1;
	}
function addBookSeriesStatic(buttonId)
{
  var arr = new Array();
   arr = buttonId.split("_");
    var table = _id.o(buttonId);
	var lastRow = table.rows.length;
	var row = table.insertRow(lastRow);
	var cellLeft = row.insertCell(0);
	cellLeft.setAttribute("nowrap","nowrap");
	var html='Book Number From:  <input type="text" name="bookNoFrom_'+arr[1]+'" id="'+lastRow+'bookNoFrom_'+arr[1]+'" onkeydown="return checkNum(this.id)" onkeyup="return checkNum(this.id)"/>   To:<input type="text" name="bookNoTo_'+arr[1]+'" id="'+lastRow+'bookNoTo_'+arr[1]+'" onkeydown="return checkNum(this.id)" onkeyup="return checkNum(this.id)"/>';
	cellLeft.innerHTML=html;
	var cellLeft1 = row.insertCell(1);
	cellLeft1.setAttribute("nowrap","nowrap");
	var html1='<div id="'+lastRow+'bookNoFrom_'+arr[1]+'Err"></div>';
	cellLeft1.innerHTML=html1;
}

function countGmBks(){
      var gameArr=new Array();
      var bookArr=new Array();
      var bookFromArr=new Array();
      var bookTOArr=new Array();
       var bookCountArr=new Array();
       var bookFromCountArr=new Array();

var _gmC=0,_bkC=0,_bkSC=0,_bkAC=0,_bkSAC=0,bks,bkF,bkT;

for(var i=0;i<count;i++){
var gmSelV =_id.o('gameSel_'+i).value;
	if(gmSelV!=-1){
		gameArr[_gmC]=gmSelV;
		bks=_id.n("bookNumber_"+i);_bkAC=0;
		for(var j=0;j<bks.length;j++){
			if(bks[j].value!=""){
			bookArr[_bkC]=bks[j].value;_bkC++;_bkAC++;
			}
		}
		bkF=_id.n("bookNoFrom_"+i);bkT=_id.n("bookNoTo_"+i);_bkSAC=0;
		for(var j=0;j<bkF.length;j++){
			if(bkF[j].value!=""&&bkT[j].value!=""){
			bookFromArr[_bkSC]=bkF[j].value;
			bookTOArr[_bkSC]=bkT[j].value;_bkSC++;_bkSAC++;
			}
		}
		bookCountArr[_gmC]=_bkAC;
		bookFromCountArr[_gmC]=_bkSAC;
		_gmC++;
	}
}

return "gameName="+gameArr+"&bookCountArr1="+bookCountArr+"&bookArr="+bookArr+"&bookFromCountArr1="+bookFromCountArr+"&bookFromArr="+bookFromArr+"&bookTOArr="+bookTOArr;

}

function verifyBks(){
_id.i('subCheck',lodDiv);
_id.hid('subTrans');_id.hid('verBk');
if(validateData()){
url = "agt_im_directSale_VerifyBooks.action?retOrgName="+_id.v('retOrgName')+"&"+countGmBks();
var _resp  = _ajaxCall(url);
	if(_resp.result){
	_respData=_resp.data;
	_id.i('mainDiv',resData(_respData));
	//alert(_respData);
	}
	
	for (var m=0;m<resGm;m++){
	  fillSelect('gameSel_'+m);
	  altRowColor('gameBookTable'+m,m);
	  _id.o('gameSel_'+m).value=gmNmSelArr[m];
	}
	if(resGm<gmNoLnth){
	for(var i=resGm;i<gmNoLnth;i++){
		count=i;
		addAnotherGame();
		}
	}
	
	_vf=true;_id.vis('subTrans');
		if(_isS=="false"){hidSubmit();
		alert("Balance Limit is not Enough to Proceed for direct Sale");
		}
		}else{hidSubmit();}
_id.i('subCheck','');
_id.vis('verBk');
}

var resGm =0;
var gmNmSelArr= new Array();
function resData(rD){
var _gmMnTab='<table border="0" cellpadding="0" cellspacing="0" width="100%" id="gameTab">' ,_gmTab,_gmBk,_gmBkS;
var gmTbArr = rD.replace(/null/g,'').split('Nx*');
resGm = gmTbArr.length-2;count=0;_isS= gmTbArr[resGm+1];
for (var i=0;i<resGm;i++){
var gmNmArr = gmTbArr[i].split('*G*');var _gmTab="",_gmBk="",_gmBkS="";
_gmTab='<tr><td><div id="gamemessage'+i+'"></div><table id="gameBookTable'+i+'" cellpadding="3" cellspacing="0" border="1" bordercolor="#CCCCCC" width="90%"><tr><td colspan="2"><i>Game Number: &nbsp;</i><select name="gameName" id="gameSel_'+i+'" class="option" onchange="chkDupGame(this.id)"><option value="-1">--Please Select--</option></select></td></tr>';
_gmTab=_gmTab+'<tr><td colspan="2"><input type="button" name="b1" value="Add Book"  onclick="addBookRowStatic(\'book_'+i+'\')" class="button" />&nbsp;&nbsp;&nbsp;<input type="button" name="bookSeries" onclick="addBookSeriesStatic(\'bookseries_'+i+'\')" value="Add Book Series" class="button" /></td></tr>';
var gmNm = gmNmArr[0];var bkLst = gmNmArr[1].split('*M*');var bkSeLst = gmNmArr[2].split('*M*');
gmNmSelArr[i]=gmNm;

for(var j=0;j<bkLst.length-1;j++){
var bkVal = bkLst[j].split(':')[0];var bkSts = bkLst[j].split(':')[1];
_gmBk = _gmBk+ '<tr><td>Book Number: <input type="text" name="bookNumber_'+i+'" id="'+j+'bookId_'+i+'" value="'+bkVal+'" onkeydown="return checkNum(this.id)" onkeyup="return checkNum(this.id)"/></td><td style="color:red;"><div id="'+j+'bookId_'+i+'Err">'+bkSts+'</div></td></tr>';
}
if(bkLst.length-1<3){
for(var j=bkLst.length-1;j<3;j++){
_gmBk = _gmBk+ '<tr><td>Book Number: <input type="text" name="bookNumber_'+i+'" id="'+j+'bookId_'+i+'" value="" onkeydown="return checkNum(this.id)" onkeyup="return checkNum(this.id)"/></td><td><div id="'+j+'bookId_'+i+'Err"></div></td></tr>';
}
}
if(bkSeLst.length-1<1){
_gmBkS = _gmBkS+ '<tr><td>Book Number From: <input type="text" name="bookNoFrom_'+i+'" id="0bookNoFrom_'+i+'" value="" onkeydown="return checkNum(this.id)" onkeyup="return checkNum(this.id)"/>	To:<input type="text" name="bookNoTo_'+i+'" id="0bookNoTo_'+i+'" value="" onkeydown="return checkNum(this.id)" onkeyup="return checkNum(this.id)"/></td><td><div id="0bookNoFrom_'+i+'Err"></div></td></tr>';

}
for(var j=0;j<bkSeLst.length-1;j++){
var bkSe = bkSeLst[j].split(':');
var bkFmVal = bkSe[0];var bkToVal = bkSe[1];var bkSeSts = bkSe[2];
_gmBkS = _gmBkS+ '<tr><td>Book Number From: <input type="text" name="bookNoFrom_'+i+'" id="'+j+'bookNoFrom_'+i+'" value="'+bkFmVal+'" onkeydown="return checkNum(this.id)" onkeyup="return checkNum(this.id)"/>	To:<input type="text" name="bookNoTo_'+i+'" id="'+j+'bookNoTo_'+i+'" value="'+bkToVal+'" onkeydown="return checkNum(this.id)" onkeyup="return checkNum(this.id)"/></td><td style="color:red;"><div id="'+j+'bookNoFrom_'+i+'Err">'+bkSeSts+'</div></td></tr>';
}
_gmTab=_gmTab+'<tr><td><table id="book_'+i+'">'+_gmBk+'</table></td></tr><tr><td><table id="bookseries_'+i+'">'+_gmBkS+'</table></td></tr></table><tr><td>';
_gmMnTab=_gmMnTab+_gmTab;count++;
}
//alert(_gmMnTab);
return _gmMnTab+"</table>";
}


var count=0,gmNoLnth=0;gmInSelBx=0;

function appendGameTab(){
var numOfGame = _id.v('nog');gmInSelBx = _id.o('gameNbr_NameHid').options.length-1;

if(numOfGame>gmInSelBx){
gmNoLnth = gmInSelBx;
document.getElementsByName('addGame')[0].style.visibility="hidden";
if(gmNoLnth<1)
_id.i('top_form','<h3>'+document.getElementsByTagName('h3')[0].innerHTML+'</h3><br/><center>No Game Available for Sale</center>');
}
else{
gmNoLnth = numOfGame;
}
for(var i=0;i<gmNoLnth;i++){
count=i;
addAnotherGame();
}
}
function fillSelect(selectBox)
{
	var ajaxList=_id.o('gameNbr_NameHid');
	var newSelectbox = _id.o(selectBox);
		for(i=0,l=ajaxList.options.length;i<l;i++) {
			if(i!=0){
				var obj = ajaxList[i];
				var opt = new Option(obj.text,obj.value);
				newSelectbox.options[i] = opt;
			}
		}
}

function altRowColor(id,count){ 
   var table = _id.o(id);   
   if(count % 2 == 0){ 
       table.className = "light_border_green"; 
       table.style.backgroundColor = "#F0FFF8"; 
     }else{ 
       table.className = "light_border_red"; 
       table.style.backgroundColor = "#FFF0F0"; 
     }       
}


var gameArr = new Array();
var ACArr = new Array();
function fetchDet(){
crDiv();
_id.non('top_form');
var retSelBox = _id.o('retOrgName'),gameSelBox=_id.o('gameNbr_NameHid');
var _resp  = _ajaxCall("fetchChildOrgNGamenFmtSale.action?ownerType=AGENT");
	if(_resp.result){
	_respData=_resp.data;
	var retArr = new Array();
	
	var tmpArr = _resp.data.split('Nx*');
	for(var i=0,l=tmpArr.length;i<l-1;i++){
	gameArr[i]=tmpArr[i];
	var text = gameArr[i].split(":")[1]+"-"+gameArr[i].split(":")[0];
	var opt = new Option(text,text);
	gameSelBox.options[i+1] = opt;
	}
	retArr = tmpArr[tmpArr.length-1].split('Rt*');
	
	for(i=0,l=retArr.length;i<l-1;i++) {
				var val = retArr[i].split(':')[0];
				ACArr[val.split('|')[1]]=(parseFloat((retArr[i].split(':')[1]).replace(/,/g,""))-parseFloat((retArr[i].split(':')[2]).replace(/,/g,"")))+" "+parent.frames[0].document.getElementById("currencyWord").innerHTML;
				//ACArr[val]=(parseFloat((retArr[i].split(':')[1]).replace(/,/g,""))-parseFloat((retArr[i].split(':')[2]).replace(/,/g,"")))+" "+parent.frames[0].document.getElementById("currencyWord").innerHTML;
				//var opt = new Option(val,val);
				var opt = new Option(val.split('|')[0],val.split('|')[1]);
				retSelBox.options[i+1] = opt;
		}
	}
_id.non('dyDiv');
_id.blk('top_form');
}

function crDiv(){
	  var newdiv = document.createElement('div'),divId = 'ajaxDiv';
	  newdiv.setAttribute('id',"dyDiv");
	 newdiv.innerHTML = 'Please Wait....<img src="'+projectName+'/LMSImages/images/loadingAjax.gif"/>';
	document.body.appendChild(newdiv); 
}
function chkDupGame(id){
hidSubmit();
var gameSel = document.getElementsByName("gameName");
		for(var i=0;i<gameSel.length;i++){
		if(gameSel[i].selectedIndex!="0"){
			if(gameSel[i].value==_id.v(id)&&gameSel[i].id!=id){
			alert('You have already selected the Game\nPlease use "Add More" button, if you Need More Entries for the same Game');
			gameSel[i].focus();
			_id.o(id).selectedIndex=0;
			}
		}
	}
}

function validateData(){
		var chkD = checkDupBk();hidSubmit();
	  	if((frmValidtr()&_un())&&(chkD.All&&!chkD.Emp)){
	  	 return true;
	  	}
	  	if(chkD.Emp){
		_id.i('retOrgDiv',_id.o('retOrgDiv').innerHTML+'<br/> Please Fill one valid Entry','e');
		}
		alert("Please check error messages on page");
		return false;
}
function frmValidtr(){
	var frmVal  = new parent.frames[0].Validator("directSale");
    frmVal.aV("retOrgName","dontselect=0","Please select a valid "+tierMap['RETAILER'],"retOrgDiv");
	return document.error_disp_handler.isValid;
}
function _un(){
var isEmpty=true,gameSel = document.getElementsByName("gameName");
for(var i=0;i<gameSel.length;i++){
if(gameSel[i].selectedIndex!="0")
isEmpty= false;
}
if(_id.o('retOrgDiv').innerHTML==""&&isEmpty){
_id.i('retOrgDiv','Please Select Any One Game','e');
gameSel[0].focus();
return false;
}else if (_id.o('retOrgDiv').innerHTML!=""&&isEmpty){
_id.i('retOrgDiv',_id.o('retOrgDiv').innerHTML+'<br/>Please Select Any One Game','e');
return false;
}
return true;
}
function checkDupBk(){
var isAllV = true,isE = true;;
for(var i=0;i<count;i++){
var bkNm = _id.n("bookNumber_"+i);var gmDigIn = _id.o('gameSel_'+i);var isV =true;
if(gmDigIn.value!=-1){
var gmA = gameArr[gmDigIn.selectedIndex-1];
var bkLen = parseInt(gmA.split(":")[2],10)+ parseInt(gmA.split(":")[3],10)+ parseInt(gmA.split(":")[5],10);var gmNo = gmA.split(":")[1]; 

//alert(bkLen+"**"+gameArr[gmDigIn.selectedIndex-1]+"***"+gmNo);
//BOOK Number Validation
for(var j=0,l=bkNm.length;j<l;j++){
		if((bkNm[j].value!="")&&(bkNm[j].value.replace(/-/g,'').length==bkLen)&&(bkNm[j].value.substring(0,gmA.split(":")[5])==gmNo)){
		for(k=j+1; k<l; k++){ 
		if ((bkNm[j].value!="")&&((bkNm[j].value).replace(/-/g,'') == (bkNm[k].value).replace(/-/g,''))&&(bkNm[j].id!=bkNm[k].id)){
		_id.i(bkNm[j].id+"Err",'Duplicate Book','e');_id.i(bkNm[k].id+"Err",'Duplicate Book','e');isV= false;
		isAllV= false;
		}}isE=false;
		}else{
				if(bkNm[j].value!=""){_id.i(bkNm[j].id+"Err",'Invalid Book','e');isV= false;isAllV= false};
		}
		if(isV){_id.i(bkNm[j].id+"Err",'');};
}//alert("After bkNum"+isAllV);

//BOOK Series Validation
var bkNmFrm = _id.n("bookNoFrom_"+i);
var bkNmTo = _id.n("bookNoTo_"+i);
for(var j=0,l=bkNmFrm.length;j<l;j++){
var isV = true;		
		if((_id.v(bkNmFrm[j].id)!=""&&_id.v(bkNmTo[j].id)!="")&&(_id.v(bkNmFrm[j].id).replace(/-/g,'').length==bkLen&&_id.v(bkNmTo[j].id).replace(/-/g,'').length==bkLen)&&(bkNmFrm[j].value.substring(0,gmA.split(":")[5])==gmNo)&&(bkNmTo[j].value.substring(0,gmA.split(":")[5])==gmNo)){
			
			if(_id.v(bkNmFrm[j].id)>_id.v(bkNmTo[j].id)){
			_id.i(bkNmFrm[j].id+"Err",'From BookNum Greater than To BookNum','e');
			isV=false;isAllV=false;
			}else{
			for(k=j+1; k<l; k++){ 
			if (((bkNmFrm[j].value!="")&&((bkNmFrm[j].value).replace(/-/g,'') == (bkNmFrm[k].value).replace(/-/g,''))&&(bkNmFrm[j].id!=bkNmFrm[k].id))||((bkNmTo[j].value!="")&&((bkNmTo[j].value).replace(/-/g,'') == (bkNmTo[k].value).replace(/-/g,''))&&(bkNmTo[j].id!=bkNmTo[k].id))){
			_id.i(bkNmFrm[j].id+"Err",'Duplicate From or To BookNum','e');
			_id.i(bkNmFrm[k].id+"Err",'Duplicate  From or To BookNum','e');
			isV= false;isAllV=false;
			}
			}
			}isE=false;
		
		}else{
				if(_id.v(bkNmFrm[j].id)!=""||_id.v(bkNmTo[j].id)!=""){
				_id.i(bkNmFrm[j].id+"Err",'Invalid Series','e');isV=false;isAllV=false;
				}
		}

		if(isV){_id.i(bkNmFrm[j].id+"Err",'');};
}
//alert("After bkSer"+isAllV);
}
}
return {All:isAllV,Emp:isE};
}

var bkNbrLen ;
function checkNum(id){
	hidSubmit();
	if(!setLen(id))return false;
	var e = event || evt;
	var _cC = e.which || e.keyCode;
			//alert(_cC);
			if(isEqualToLenOfBookNum(id)){
			       if(_cC == 46 || _cC == 8 || _cC >= 37 && _cC < 41){return true;}
			        else{
				        moveToNextElement(id);
				        return false;
			        }
       		 }
 	if ((_cC >= 48 && _cC < 58) || _cC == 45 ||_cC == 13 ||_cC == 40 || _cC == 38 || (_cC >= 96 && _cC < 106 ||_cC == 46 || _cC == 8 || _cC == 9 ||_cC == 189 ||_cC == 109)) {
		return true;
	}
	return false;
}

function setLen(id){
var gameSel = document.getElementsByName("gameName"),gameNm = null,gameIndex=id.substring(id.indexOf('_')+1,id.length);
gameNm = gameSel[gameIndex];
if(gameNm.selectedIndex=="0"){
_id.i('gamemessage'+gameIndex,'Please Select Game First','e');
_id.blk('gamemessage'+gameIndex);
return false;
}else{
_id.i('gamemessage'+gameIndex,'');_id.non('gamemessage'+gameIndex);
}
var arr =  gameArr[gameNm.selectedIndex-1].split(":");
bkNbrLen = parseInt(arr[2])+parseInt(arr[3])+parseInt(arr[5]);
return true;
}


function isEqualToLenOfBookNum(id){
	var currentVal = _id.v(id);
		len = bkNbrLen;
			if(currentVal.match("-")){
				var tktLen = _id.v(id)
				if(tktLen.replace(/-/g,'').length<len){return false;}
			}else{
				if(_id.v(id).length<len){return false;}
			}
		return true;
}
    
function moveToNextElement(id){
    totalElement = document.forms[0].elements.length;
      for (var i=0;i <totalElement ;i++)
		{
		var e = document.forms[0].elements[i];
			if(e.id==id){
				if(i<totalElement){
					var nxtElement = document.forms[0].elements[i+1];
					if(nxtElement.type=="button"&&i+2<totalElement){
					nxtElement = document.forms[0].elements[i+2];
					}
					if(nxtElement.type=="text")
					document.getElementById(nxtElement.id).focus();
					break;
				}
			}
	}
}

parent.frames[0].Version['$Source: /rep/LMS_Mgmt/WebRoot/com/skilrock/lms/web/scratchService/inventoryMgmt/agent/js/directSale.js,v $'] = '$Id: directSale.js,v 1.2 2016/10/31 11:40:08 neeraj Exp $';
