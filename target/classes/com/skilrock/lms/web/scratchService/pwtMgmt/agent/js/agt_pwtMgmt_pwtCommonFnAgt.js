var count=0,gmNoLnth=0,_selBxGm=0;

function checkDuplicate(type){
	var isFill = true;var isDp = false;
	for(var c=1;c<count+1;c++){
	    var tbl = _id.o('game'+type+c);
		var elmnt=tbl.getElementsByTagName("input");var _mArr = new Array();
	    for(var i=0,l=elmnt.length;i<l;i++){
		    if(elmnt[i].value!=""&&!_mArr[i]){
		     isFill = false;
		     for(j=i+1; j<l; j++){ 
		     		if(elmnt[j].value!=""&&!_mArr[j]){
			           	if ((elmnt[i].value).replace(/-/g,'') == (elmnt[j].value).replace(/-/g,'')){
			           	_id.vis(c+'errorDiv'+(j+1));
			           	_id.o(c+'errorDiv'+(j+1)).parentNode.style.backgroundColor="#cccccc";isDp=true;
			           	_mArr[j]=true;
			          }else{
			          	_id.hid(c+'errorDiv'+(j+1));
			           	_id.o(c+'errorDiv'+(j+1)).parentNode.style.backgroundColor=tbl.style.backgroundColor;
			          }
			          }
		         	}
		   	 }else if(elmnt[i].value==""){
			        	 _id.hid(c+'errorDiv'+(i+1));
			           	_id.o(c+'errorDiv'+(i+1)).parentNode.style.backgroundColor=tbl.style.backgroundColor;
			          }
	    }
	}	
	return {fill:isFill,dup:isDp}; 
}
function chkDupGame(id){
var gameSel = document.getElementsByName("gameNbr_Name");
		if(_id.o(id).value==-1){
		return false;
		}
	var answer = confirm ("You Have Selected "+_id.o(id).value+"?");
	if (answer){
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
	else{
	_id.o(id).selectedIndex=0;
	}
}

var ticketNbrLen ;
var virnLen;
function checkNum(id){
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

function checkVirn(id){
	if(!setLen(id))return false;
	
	var e = event || evt;
	var _kC = e.which || e.keyCode;
	if(isEqualToLenOfBookNum(id)){
		if(_kC == 46 || _kC == 8 || _kC >= 37 && _kC < 41){
			return true;
		}else{
			  moveToNextElement(id);
			   return false;
		}
	 }
}
function setLen(id){
var gameSel = document.getElementsByName("gameNbr_Name"),gameNm = null,gameIndex;
if(id.match("virnCode")){
gameIndex=id.substring(0,id.indexOf('virnCode'));
}else{
gameIndex=id.substring(0,id.indexOf('ticketNbr'));
}
gameNm = gameSel[gameIndex-1];
if(gameNm.selectedIndex=="0"){
_id.i('gamemessage'+gameIndex,'Please Select Game First','e');
_id.blk('gamemessage'+gameIndex);
return false;
}else{
_id.i('gamemessage'+gameIndex,'');_id.non('gamemessage'+gameIndex);
}

var arr =  gameArr[gameNm.selectedIndex-1].split(":");
ticketNbrLen = parseInt(arr[2])+parseInt(arr[3])+parseInt(arr[4])+parseInt(arr[5]);
virnLen = parseInt(arr[6]);

return true;
}

function verifyOrg(orgDiv,orgName,msg){
 
  if(_id.o(orgDiv).value!="-1"){
	  var returnType=confirm(msg+" " + orgName);
	  if(returnType) {
	    // var arrVirn=document.getElementsByName('virnCode');
	     //arrVirn[0].focus();
	     _id.i('retOrgDiv','');
	     return true;
	    }else{
	       _id.o(orgDiv).selectedIndex=0;
	       _id.iniFocus(orgDiv);
	       return false;
	   }
  }  
}

function _un(){
var isEmpty=true,gameSel = document.getElementsByName("gameNbr_Name");
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
function isEqualToLenOfBookNum(id){
	var currentVal = _id.v(id);
	if(id.match("virn")){
	len = virnLen;
	if(_id.v(id).length<len){return false;}
	}else if(id.match("ticket")){
		len = ticketNbrLen;
			if(currentVal.match("-")){
				var tktLen = _id.v(id)
				if(tktLen.replace(/-/g,'').length<len){return false;}
			}else{
				if(_id.v(id).length<len){return false;}
			}
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


function crtTabAg(txtBx,count){
	   var counter =1,_tD='<table border="0" id="game'+txtBx+count+'" width="60%" cellpadding="2" cellspacing="0"><tr><td colspan=2><div id="'+count+'errorDiv"></div></td></tr><tr>';
	   var vLbl = 'id="'+count+'saveBOPwtTicketsData_">Enter VIRN Code For Above Selected Game',tLbl='id="'+count+'verifyTickets_">Enter Ticket No. For Above Selected Game';
	   var fRow = '<td align="left" colspan="5"><b><label '+((txtBx=="Virn")?vLbl:tLbl)+'</label></b></td>';
	 
	  _tD=_tD+fRow;
	
    _tD=_tD+"</tr>";
	 
	   for (var i=0;i<6;i++){
		  _tD=_tD+'<tr>';
		   for(var j=0;j<5;j++){
		   var vInp='<b>'+((counter<10)?"0"+counter:counter)+'.</b><input type="text" id="'+count+'virnCode'+counter+'" name="virnCode" size="15" onkeydown="return checkVirn(this.id)" onkeyup="return checkVirn(this.id)"/><span id="'+count+'errorDiv'+counter+'" style="visibility:hidden">&nbsp;<img src="'+projectName+'/LMSImages/images/cross.gif" width="12" height="12"/></span></td>';
		   var tInp = '<b>'+((counter<10)?"0"+counter:counter)+'.</b><input type="text" id="'+count+'ticketNbr'+counter+'" name="ticketNumber" size="15" onkeydown="return checkNum(this.id)" onkeyup="return checkNum(this.id)"/><span id="'+count+'errorDiv'+counter+'" style="visibility:hidden">&nbsp;<img src="'+projectName+'/LMSImages/images/cross.gif" width="12" height="12"/></span></td>';
		   	 _tD=_tD+'<td width="20%" nowrap>';
			_tD=_tD+((txtBx=="Virn")?vInp:tInp);
			counter++;
			}
			_tD=_tD+'</tr>';
		}
		return _tD+'</table>';
		
		//_id.i((txtBx=='Virn')?'_virnDiv':'_tktDiv',_tD+'</table>');
	}	
	
function addInpRow(tab){
var type = tab.match('Virn')?'Virn':'Tkt';

 var gameNum = (type=='Virn')?(tab.replace("gameVirn","")):(tab.replace("gameTkt",""));
 //alert(tab+"---"+type+"--"+gameNum);
 	var empty=false;
	var tbl = _id.o(tab);
	var elmnt=tbl.getElementsByTagName("input");
    for(var i=0,l=elmnt.length;i<l;i++){
    if(elmnt[i].value=="")
    empty = true;
    }
    if(!empty){
   for(var k=0;k<2;k++){
    var iteration = tbl.rows.length;
    var row = tbl.insertRow(iteration);
 	 
	  counter=elmnt.length+1;
	  for(var j=0;j<5;j++){
	   var cellLeft = row.insertCell(j);
 	  var newdiv = document.createElement('div');
	  var vInp='<td width="20%" nowrap><b>'+counter+'.</b><input type="text" id="'+gameNum+'virnCode'+counter+'" name="virnCode" size="15" onkeydown="return checkVirn(this.id)" onkeyup="return checkVirn(this.id)"/><span id="'+gameNum+'errorDiv'+counter+'" style="visibility:hidden">&nbsp;<img src="'+projectName+'/LMSImages/images/cross.gif" width="12" height="12"/></td>';
	  var tInp = '<td width="20%" nowrap><b>'+counter+'.</b><input type="text" id="'+gameNum+'ticketNbr'+counter+'" name="ticketNumber" size="15" onkeydown="return checkNum(this.id)" onkeyup="return checkNum(this.id)"/><span id="'+gameNum+'errorDiv'+counter+'" style="visibility:hidden">&nbsp;<img src="'+projectName+'/LMSImages/images/cross.gif" width="12" height="12"/></td>';
	  newdiv.innerHTML =((type=="Virn")?vInp:tInp);
      cellLeft.appendChild(newdiv);
      counter++;
	  }
	  }
	}
}
function crtTabAgt(tabType,count){
var tab ='<table border="0" width="60%" id="gameOutTab'+count+'"><tr><td><div id="gamemessage'+count+'" style="text-align: left;"></div><input type="hidden" name="gameNbrFormat" id="gameNbrFormat'+count+'"/><input type="hidden" name="inpCount" id="inpCount'+count+'" value="0"/><table ><tr><td class="tdLabel"><label for="gameNbr_Name'+count+'" class="label">Game Number:</label></td><td>';
var addBtn=(tabType=="Virn")?'<input type="button"  name="addRow" value="Add More VIRN" class="button" onclick="addInpRow(\'gameVirn'+count+'\')"/>':'<input type="button"  name="addRow" value="Add More Ticket" class="button" onclick="addInpRow(\'gameTkt'+count+'\')"/>';
tab=tab+'<select name="gameNbr_Name" id="gameNbr_Name'+count+'" class="option" onchange="chkDupGame(this.id)"><option value="-1">--Please Select--</option></select></td></tr></table></td><td align="right">'+addBtn+'</td></tr><tr><td colspan="4"><div id="_virnDiv'+count+'">';
tab=tab+crtTabAg(tabType,count)+'</div></td></tr></table><br/>';
return tab;
}

function addGameToTable(type)
{

if(count<_selBxGm){	
  var tbl =_id.o('gameTab');
  var iteration = tbl.rows.length;
  var row = tbl.insertRow(iteration);
 	  var cellLeft = row.insertCell(0);
	  var newdiv = document.createElement('div');
	  var divid = "div"+iteration;
	  newdiv.setAttribute('id',divid);
	  newdiv.innerHTML = crtTabAgt(type,parseInt(count+1,10));
      cellLeft.appendChild(newdiv);
      fillSelect('gameNbr_Name'+parseInt(count+1,10));
      count++;
      altRowColor('gameOutTab'+count,count);
      }else{document.getElementsByName('addGame')[0].style.visibility="hidden";}
}

function appendGameTab(type){
var numOfGame = _id.v('nog');_selBxGm = _id.o('gameNbr_NameHid').options.length-1;
if(numOfGame>_selBxGm){
gmNoLnth = _selBxGm;
document.getElementsByName('addGame')[0].style.visibility="hidden";
if(gmNoLnth<1)
_id.i('top_form','<h3>'+document.getElementsByTagName('h3')[0].innerHTML+'</h3><br/><center>No Game Available for Pwt Acceptance</center>');
}
else{
gmNoLnth = numOfGame;
}

for(var i=0;i<gmNoLnth;i++){
count=i;
addGameToTable(type);
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
function fillCount(type){
for(var i=1;i<=count;i++){
	var tab = _id.o('game'+type+i);
	var elmnt=tab.getElementsByTagName("input");
	var noOfElmt = elmnt.length,filled=0;
	for(var j=0;j<noOfElmt;j++){
	if(elmnt[j].value!=""){
	filled++;
	}
	}
	_id.o("inpCount"+i).value=filled;

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
function fetchDet(){
crDiv();
_id.non('top_form');
var retSelBox = _id.o('retOrgName'),gameSelBox=_id.o('gameNbr_NameHid');
var _resp  = _ajaxCall("fetchRetailernGamenFmt.action");
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
		retArr = tmpArr[tmpArr.length-1].split(":");
		
		var keyValueArr;
		for(i=0,l=retArr.length;i<l-1;i++) {
			keyValueArr = retArr[i].split(";");
			var opt = new Option(keyValueArr[1],keyValueArr[0]);
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




parent.frames[0].Version['$Source: /rep/LMS_Mgmt/WebRoot/com/skilrock/lms/web/scratchService/pwtMgmt/agent/js/agt_pwtMgmt_pwtCommonFnAgt.js,v $'] = '$Id: agt_pwtMgmt_pwtCommonFnAgt.js,v 1.2 2010/04/14 04:01:23 arun Exp $';