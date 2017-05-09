var _kenoWinArr=new Array();
var isQP=false;
var curDrwTimekeno="";
var curSel="kenoRow";
var pickType="1";

function setDefaultkeno(){
	isQP=false;
	curDrwTimekeno="";
	curSel="kenoRow";
	pickType="1";
}
/*
function gameInfo(){
	var url="kenoInfo.action";
	_id.o('topId').style.display="block";
	runAccordion('5');
	 _id.i('info5Div','<iframe src="'+url+'" id="tktFrame" style="width:100%;height:100%" frameborder="0"></iframe>' );

}
*/
function _kenoWinAppend(){ 
	if(!_kenoWin.match("Result Awaited"))
 		_kenoWin='Result Awaited="" Nxt'+_kenoWin;
}

function _kenoLatestDrawTime(){
	return _kenoStatus.split("=")[1]; 
}

function _kenoFillWinTbl(){ 
	var _kenoWinHt='<table width="100%" border="0" bordercolor="red" cellspacing="0" cellpadding="0" align="center" bgcolor="#9cceff"><tr><td colspan="2" align="left"><div valign="middle" style="height:22px;background-color:#00639c;padding-top:3px;font-size:16px;font-family:Arial;font-weight:bold;color:#FFFFFF">&nbsp;Win Result&nbsp;</div></td></tr>      <tr><td valign="top" align="center" height="100">';
	var winArr = _kenoWin.split('Nxt');
	var _kenoWinHTML='<table width="100%" border="0" bordercolor="red" style="margin-top:3px"><tr><td><div style="'+scrollColor("#9CCEFF","#00639c","184")+'">';	
	for(var i=0;i<=winArr.length-2;i++){
	if(!winArr[i].match("Result")){
		var time  = new Date(parseInt(winArr[i].split('=')[0].replace(" ","")));
		var newTime=week[time.getDay()]+" "+time.getDate()+" "+month[time.getMonth()]+"<br/>"+(time.getHours()<10?"0"+time.getHours():time.getHours())+":"+(time.getMinutes()<10?"0"+time.getMinutes():time.getMinutes())+":"+(time.getSeconds()<10?"0"+time.getSeconds():time.getSeconds());
		var htm='<table width="90%" border="0" bordercolor="green" id="result'+i+'" cellspacing="2" cellpadding="10" class="keno_winningResult_table" ><tr><td class="kenowinningResultTime" colspan="8" height="53">'+newTime+'</td></tr><tr>';
		var num = (winArr[i].split('=')[1]).split(',');
		for(var j=0;j<num.length;j++){
		htm=htm+'<td width="10%" class="selected_win_keno_numbers" align="center">'+num[j]+'</td>';
		}
		htm=htm+'</tr></table>';
		_kenoWinHTML=_kenoWinHTML+htm;
		}
		else{
		var htm='<table width="90%" bordercolor="blue" border="0" cellspacing="2" cellpadding="10" id="result'+i+'" class="keno_winningResult_table" ><tr><td class="kenowinningResultTime" colspan="8" height="53" >Result Awaited</td></tr><tr>'
		htm=htm+'<td class="selected_win_keno_numbers"  colspan="8"><div style="text-align:center" id="winAwaitedDiv" style="color:#00466E"></div></td>';
		htm=htm+'</tr></table>';		
		_kenoWinHTML=_kenoWinHTML+htm;
	}
		
	}
	
	_kenoWinHTML=_kenoWinHTML+'</div></td></tr></table>';
	return _kenoWinHt+_kenoWinHTML+'</td></tr></table>';
}



function _kenoWinAjx(){
var url = "fetchWinKeno.action?frmDate="+_kenoWinArr[_kenoWinArr.length-1];
_kenoWin=_kenoWin+(winAjaxReq(url)).data;
_kenoFillWinTbl();
}

function kenoNmSel(lstNo){
var htm = '<div id=""><table width="99%" border="0" cellspacing="0" cellpadding="0" bgcolor="#639cce" style="margin-top:6px;margin-bottom:5px"><tr>';
for(var i=1;i<=lstNo;i++){
if(i%10==0){
htm = htm+'<td style="cursor:pointer;background:url('+projectName+'/LMSImages/images/shade_white.gif) center no-repeat" class="selected_keno_numbers" onmouseout="kenoonmouseouteffect(this.id)" onmouseover="kenoonmouseovereffect(this.id)"  onclick="fillNumKeno('+i+')"  id="'+i+'" >'+(i<10?"0"+i:i)+'</td></tr>';
}
else{
htm = htm+'<td class="selected_keno_numbers"  style="cursor:pointer;background:url('+projectName+'/LMSImages/images/shade_white.gif) center no-repeat" onmouseout="kenoonmouseouteffect(this.id)" onmouseover="kenoonmouseovereffect(this.id)" onclick="fillNumKeno('+i+')"  id="'+i+'"  >'+(i<10?"0"+i:i)+'</td>';
}
}
htm = htm+'</tr></table></div>';
return htm;
}


function kenoonmouseovereffect(id){
		var count=0;
		var isFilled=false;
		var isAllPick=false;
		for(var i=1;i<=90;i++){	
			if(_id.o(i).className=="selected_keno_numbersSelect")
				count++;		
				if(count>5){
					isFilled=true;
					break;
				}
		}
		// check filled
		var obj = _id.o('kenoRow');
		var elms = obj.getElementsByTagName("input");
		var countFill=0;
		for(var l=0;l<elms.length;l++){
			if(elms[l].value!=""){
				countFill+=1;
			}
		}
		if(countFill==pickType)	{
			isAllPick=true;
		}	
		if(!isFilled && !isQP && !isAllPick){	
			if(_id.o(id).className!="selected_keno_numbersSelect"){
			_id.o(id).style.background='url('+projectName+'/LMSImages/images/shade_over.gif) left center no-repeat';
			_id.o(id).style.color='#FFFFFF';
			_id.o(id).className='selected_keno_numbersSelectOMO';
			}
		}
}

function kenoonmouseouteffect(id){
	if(_id.o(id).className=='selected_keno_numbersSelectOMO'){
	_id.o(id).style.background='url('+projectName+'/LMSImages/images/shade_white.gif) left center no-repeat';
	_id.o(id).style.color='#00639c';
	_id.o(id).className='selected_keno_numbers';
	}
	else if(_id.o(id).className=='selected_keno_numbersSelect'){
	_id.o(id).style.background='url('+projectName+'/LMSImages/images/shade_over.gif) left center no-repeat';
	_id.o(id).style.color='#FFFFFF';
	}
	else if(_id.o(id).className=='selected_keno_numbers'){
	_id.o(id).style.background='url('+projectName+'/LMSImages/images/shade_white.gif) left center no-repeat';
	_id.o(id).style.color='#00639c';
	}
}

function kenoRow(){
	var htm='<table width="95%" id="kenoRow" border="0" cellspacing="4" cellpadding="0">';   
	var disableVal = "",className="selected_keno";
	for(var i=1;i<parseInt(6)+1;i++){
		htm=htm+'<tr id="row'+i+'">';
			for(var j=0;j<5;j++){
				if(!(i==1 && j==0)){
				disableVal="disabled";
				className="selected_kenoHidden";
				}
				htm=htm+'<td><input type="text" class="'+className+'" maxlength="2" '+disableVal+'  onkeydown="return keyPressKeno(this, event)" onkeyup="return keyPressKeno(this, event)"  onblur="blurFnKeno(this)" onmousedown="return disClick(this)"></td>';
				
		}
		htm=htm+'</tr>';
		}
	return htm+'</table>';

}


function blurFnKeno(obj){
	if((obj.value.length<2)||isNaN(obj.value)||obj.value<=0||obj.value>90){
		obj.value="";
	}
	kenoapplyCSS();
	calAmtKeno();
}


function kenoclearAllBoxes(){
	_id.resetInpTypes('kenoRow','selected_keno',false);
	_id.resetInpTypes('kenoRow','selected_kenoHidden',true,pickType);

	for(var i=1;i<=90;i++){	
		_id.o(i).className="selected_keno_numbers";
		_id.o(i).style.background='url('+projectName+'/LMSImages/images/shade_white.gif) left center no-repeat';
		_id.o(i).style.color='#00639c';
	}
		isQP=false;
		_id.o('qpVal').value="No"; 
		_id.o("totAmtLto").value=0;
		_id.o("noOfQp").value=0;
		_id.o("noOfLines").value=0;
		_id.o("qpChkAll").checked=false;
}

function _kenoFillNxtDrw(){	
	var nxtDrwHTML = '<table width="100%" cellspacing="0"cellpadding="0"  border="0" bordercolor="red"><tr><td colspan="2" align="left" valign="top"><div valign="middle" style="height:22px;width:180px;background-color:#00639c;padding-top:3px;font-size:16px;font-family:Arial;font-weight:bold;color:#FFFFFF">&nbsp;Next Draw&nbsp;</div></td></tr><tr><td align="center"valign="top"><table width="100%" bordercolor="red" border="0" cellspacing="0" cellpadding="0" bgcolor="#9cceff"><tr><td width="100%" align="left" valign="top" ><div style="'+scrollColor("#9CCEFF","#00639c","175")+'"><table width="90%" align="left" bordercolor="green"  border="0" cellspacing="0" cellpadding="0" class="borderbtmkeno">';
	
	var nxtDrwArr = _kenoDrawTime.split(',');
	for(var i=0,l=nxtDrwArr.length;i<l;i++){
		var time  = new Date(parseInt(nxtDrwArr[i].replace(" ","")));
		var newTime=week[time.getDay()]+" "+time.getDate()+" "+month[time.getMonth()]+" "+(time.getHours()<10?"0"+time.getHours():time.getHours())+":"+(time.getMinutes()<10?"0"+time.getMinutes():time.getMinutes());
		var hiddenTime="<input type='text' id='timer"+i+"'>";
		var rowcolor;
		var data='';	
		
		if(i==0){
			data=time.getFullYear()+"-"+time.getMonth()+"-"+time.getDate()+"-"+(time.getHours()<10?"0"+time.getHours():time.getHours())+"-"+(time.getMinutes()<10?"0"+time.getMinutes():time.getMinutes())+"-"+(time.getSeconds()<10?"0"+time.getSeconds():time.getSeconds());		 
			var dateFuture1= new Date(time.getFullYear(),time.getMonth(),time.getDate(),(time.getHours()<10?"0"+time.getHours():time.getHours()),(time.getMinutes()<10?"0"+time.getMinutes():time.getMinutes()),(time.getSeconds()<10?"0"+time.getSeconds():time.getSeconds())); 
			_kenoNxtDrawTime=(""+time).substring(0,(""+time).lastIndexOf(":")+3);
			}
		if(i%2==0){rowcolor="#9CCEFF"}else{rowcolor="#639CCE"}
		nxtDrwHTML=nxtDrwHTML+'<tr><td width="100%"><input type="hidden" value="'+data+'" id="'+i+'timer"></td></tr><tr bgcolor="'+rowcolor+'" style="height: 20px"><td width="100%" align="center" class="borderbtmkeno" colspan="2"><div id="'+i+'blink" >'+newTime+'</div></td><td width="44%" align="center"><p><div id="nxtDrwD'+i+'" style="display:none"><img src="'+fortImgPath+'freezed.gif"></div></p></td></tr>';
	}
	nxtDrwHTML=nxtDrwHTML+'</table></div></td></tr></table></td></tr></table>';
	return nxtDrwHTML;
}

function _kenoUpData(win,drwTime,drwStatus){
_kenoWin=win;
_kenoDrawTime=drwTime;
_kenoStatus=drwStatus;
}


function chkQp(id){
	var objR = _id.o(curSel);
	var elms = objR.getElementsByTagName("input");
	
	for(var i=1;i<=90;i++){	
		_id.o(i).className="selected_keno_numbers";
		_id.o(i).style.background='url('+projectName+'/LMSImages/images/shade_white.gif) left center no-repeat';
		_id.o(i).style.color='#00639c';
	}
	
	if (_id.o(id).checked == true) {
		isQP=true;
		_id.o('qpVal').value="Yes";
		for(var l=0;l<elms.length;l++){
			elms[l].value="";
			elms[l].disabled=true;
		}
		if(pickType==30){
		_id.o('noOfQp').value="";
		_id.o('noOfQp').readOnly=false;
		_id.o('noOfQp').focus();
		_id.o("noOfLines").value="0";
		_id.o("totAmtLto").value="0";
		}
		else{
			if(pickType==1)
			{
				_id.o('noOfQp').value="1";
				_id.o("noOfLines").value="1";
			}
			else if(pickType==2)
			{
				_id.o('noOfQp').value="2";
				_id.o("noOfLines").value="1";
			}
			else if(pickType==3)
			{
				_id.o('noOfQp').value="3";
				_id.o("noOfLines").value="1";
			}
			else if(pickType==4)
			{
				_id.o('noOfQp').value="4";
				_id.o("noOfLines").value="1";
			}
			else if(pickType==5)
			{
				_id.o('noOfQp').value="5";
				_id.o("noOfLines").value="1";
			}
			calAmtKeno();
		}		
	} 
	else {
		isQP=false;
		_id.o('qpVal').value="No";
		for(var l=0;l<elms.length;l++){
			elms[l].value="";
			elms[l].disabled=false;
		}
		_id.o('noOfQp').value=0;
		_id.o("noOfLines").value=0;
		_id.o("totAmtLto").value=0;
		_id.o('noOfQp').readOnly=true;	
	}
}

function fillNumKeno(val){
var length=0;
if(!isQP){
	var objR = _id.o(curSel);var clrChg = true;
	var elms = objR.getElementsByTagName("input");
	if(pickType=="30"){length=elms.length;}
	if(pickType=="1"){length=1;}
	if(pickType=="2"){length=2;}
	if(pickType=="3"){length=3;}
	if(pickType=="4"){length=4;}
	if(pickType=="5"){length=5;}
	
	if(_id.o(val).className=="selected_keno_numbersSelect"){
		for(var l=0;l<length;l++){
			if(elms[l].value==val){
			elms[l].value="";
			break;
			}
		}
	}else{
		for(var l=0;l<length;l++){
			if(elms[l].value==""){
				elms[l].value=(val<10?"0"+val:val);
				break;}
			}
		for(var l=0;l<length;l++){
			if(elms[l].value==""){
			break;
			}
		}						
	}
	
kenoapplyCSS();	
calAmtKeno();
}
}


function calAmtKeno(){

	var noOfDrw = _id.v("noOfDraws");
	/*var noOfPanel = 1;
	var obj,elms;var isPanel=true;
	obj = _id.o(curSel);
	elms = obj.getElementsByTagName("input");
		for(var l=0;l<elms.length;l++){
				if(elms[l].value==""||elms[l].value.length<2||isNaN(elms[l].value)){elms[l].value="";}
		}
	_id.o("totAmtLto").value=noOfDrw*noOfPanel*_kenoPrice;*/
	fetchLines();
	var amt=_id.v("totAmtLto");
	_id.o("totAmtLto").value=noOfDrw*amt;
	//_id.o('noOfLines').value=lines;	
}

function fillNoOfPanels(id){
	var noOfpanels=_id.o(id).value;
	_id.i('kenoRowDiv',kenoRow(noOfpanels));
}

function fillNoOfQp(obj, e){
	var keyCode = (window.event) ? event.keyCode : e.which;
	if ((keyCode >= 48 && keyCode < 58) || keyCode == 45 || keyCode == 27 ||keyCode == 13 ||keyCode == 40 || keyCode == 38 ||keyCode == 46 || keyCode == 8  ||   (keyCode >= 96 && keyCode < 106 || keyCode == 9 || keyCode == 189 ||keyCode == 109)) {
			if(keyCode == 46 || keyCode == 8 || keyCode == 9 ){
				calAmtKeno();
				return true;
			}
			if(obj.value.length>1){
				if(obj.value==0||obj.value>30||isNaN(obj.value)){
							obj.value="";
							obj.focus();
						}
				
			}
	}
	else{
			return false;
	}
	calAmtKeno();
	return true;
}

function keyPressKeno(obj,e){
	var keyCode = (window.event) ? event.keyCode : e.which;
	if ((keyCode >= 48 && keyCode < 58) || keyCode == 45 || keyCode == 27 ||keyCode == 13 ||keyCode == 40 || keyCode == 38 ||keyCode == 46 || keyCode == 8  ||   (keyCode >= 96 && keyCode < 106 || keyCode == 9 || keyCode == 189 ||keyCode == 109)) {
		if(keyCode == 46 || keyCode == 8 || keyCode == 9 )return true;
		if(obj.value.length>1){
				if(obj.value==0||obj.value>90||isNaN(obj.value)){
					obj.value="";
					obj.focus();
				}else{
					var objR = _id.o(curSel);var clrChg = true;
					var elms = objR.getElementsByTagName("input");
						for(var l=0;l<elms.length;l++){
								clrChg = true;
								if((elms[l].value==obj.value)&&(elms[l]!=obj)){
								obj.value="";
								obj.focus();
								clrChg = false;break;
								}
						}
					if(clrChg){
						for(var l=0;l<elms.length;l++){
								if(elms[l].value==""){
									if(elms[l].disabled==false){
									elms[l].focus();
									}
									break;
								}
						}
					
					}	
				}
			kenoapplyCSS();	
			}				
	}else{
		return false;
		}
	return true;	
}


function kenoapplyCSS(){
	for(var i=1;i<=90;i++){	
		_id.o(i).className="selected_keno_numbers";
		_id.o(i).style.background='url('+projectName+'/LMSImages/images/shade_white.gif) left center no-repeat';
		_id.o(i).style.color='#00639c';
	}
	var obj = _id.o(curSel);var clrChg = true;
	var elms = obj.getElementsByTagName("input");
	for(var l=0;l<elms.length;l++){
		for(var j=0;j<elms.length;j++){
			if((elms[l].value==elms[j].value)&&(elms[l]!=elms[j])){
				elms[j].value="";
			}
		}
	}
	
	var count=0;
	for(var l=0;l<elms.length;l++){
		if(elms[l].value!=""){
			_id.o(parseInt(elms[l].value,10)).className="selected_keno_numbersSelect";
			_id.o(parseInt(elms[l].value,10)).style.background='url('+projectName+'/LMSImages/images/shade_over.gif) left center no-repeat';
			_id.o(parseInt(elms[l].value,10)).style.color='#FFFFFF';		
				count+=1;	
				}
				_id.o('noOfQp').value=count;
	}
	if(count==pickType){
		for(var l=1;l<=90;l++){
			if(_id.o(l).className!="selected_keno_numbersSelect"){			
				_id.o(l).className="selected_keno_numbersDisabled";
				_id.o(l).style.background='url('+projectName+'/LMSImages/images/shade_disable.gif) left center no-repeat';
			}			
		}
	}
}

function resetFrm(){
	document.forms[0].reset();
	for(var i=1;i<=49;i++){
		_id.o(i).className="selected_lotto_numbers";
		_id.o(i).style.background='url('+projectName+'/LMSImages/images/shade_white.gif) left center no-repeat';
		_id.o(i).style.color='#00639c';
	}
		var elms = document.forms[0].getElementsByTagName("input");
		for(var l=0;l<elms.length;l++){
			elms[l].disabled=false;
		}
	curSel = "row1";	
}

var gameChildW=null;

function kenoGameInfo(){
	if (gameChildW) {}
	else{
	 	gameChildW=window.open("keno/gameInfo.jsp","NewWin","width=650,height=550,toolbar=0,resizable=0,scrollbars=1,alwaysRaised=0,location=0,menubar=0"); 
		gameChildW.close();
	}
	gameChildW=window.open("keno/gameInfo.jsp","NewWin","width=650,height=550,toolbar=0,resizable=0,scrollbars=1,alwaysRaised=0,location=0,menubar=0"); 
}

function buyTicketKeno(){
	var compData=""; 
	if(_id.o('qpChkAll').checked==false){
		var inputData="";
		var obj = _id.o(curSel);
		var elms = obj.getElementsByTagName("input");		
		for(var l=0;l<elms.length;l++){
				if (elms[l].value != "") {
						inputData = inputData + elms[l].value + ",";
					} else {
						inputData = inputData + "-1,";
					}	
				if(parseInt(pickType)!=30){				
					if(l==4)
					 break;
				}
		}
		if(inputData!="")
		inputData=inputData.substring(0,inputData.length-1)+"";
		compData=compData+inputData;	
	}
	else{
		compData="QP";
	}
	
	if(_id.o('qpVal').value=="No"){
		//compData=compData.substring(0,compData.length-3);
		var pickDataArr;
		if(compData.match("-1")!=null){	
			pickDataArr=(compData.substring(0,compData.indexOf("-1")-1)).split(",");
		}
		else{
			pickDataArr=compData.split(",");
		}		
		_id.o('pickedNumbers').value=compData;
		//if((pickDataArr.length<parseInt(pickType))&& _id.o('picktype').value!=30||pickDataArr[0]==''){
		
		if((pickDataArr.length<parseInt(pickType))&& pickType!=30 ||pickDataArr[0]==''){
			alert("Please fill incomplete entries");
			return false;
		}
		else if(pickType==30 && pickDataArr.length==1&&_id.o('playType').value=='Perm2'){
			alert("Please Pick Minimum 2 Numbers");
			return false;
		}else if(pickType==30 && pickDataArr.length<=2&&_id.o('playType').value=='Perm3'){
			alert("Please Pick Minimum 3 Numbers");
			return false;
		}
		
	}
	else{
		if((_id.o('noOfQp').value==0)||(_id.o('noOfQp').value=='')){
			alert("Please fill incomplete entries");
			_id.o('noOfQp').value='';
			_id.o('noOfQp').focus();
			return false;
		}
		else if(_id.o('noOfQp').value==1 &&_id.o('playType').value=='Perm2'){
			alert("Please Pick Minimum 2 Numbers");
			_id.o('noOfQp').value='';
			_id.o('noOfQp').focus();
			return false;
		}
		else if((_id.o('noOfQp').value<=2)&&_id.o('playType').value=='Perm3'){
			alert("Please Pick Minimum 3 Numbers");
			_id.o('noOfQp').value='';
			_id.o('noOfQp').focus();
			return false;
		}	
	}

var amount=_id.o('totAmtLto').value;
var t=_id.o('playType').value;
gameBuyAjaxReq("kenoBuy.action?pickedNumbers="+compData+"&noOfDraws="+_id.o('noOfDraws').value+"&totalPurchaseAmt="+amount+"&noPicked="+_id.o('noOfQp').value+"&playType="+t+"&QP="+_id.o('qpVal').value);	
kenoclearAllBoxes();
return true;
}

var lines=0;
var _reqLine;


/*function factorial (n)
{ if (n == 0)
   return 1;
  else
   return n * factorial(n-1);
}*/


function fetchLines(){
//alert("jjjj");
var pickType=_id.o('pickType').value;
var num=_id.o('noOfQp').value;
if(isNaN(num)){_id.o('noOfQp').value="";}
else if(num>30){_id.o('noOfQp').value="";_id.o('noOfQp').focus();return false;}
else{
if(pickType==30){
	if(num!=0&&num!=1&&num!=2){
		lines = num*(num-1)/2;	
	}
	
	else{
		lines=1;
	}
}
else if(pickType==31){
	if(num!=0&&num!=1&&num!=2&& num!=3){
		lines = num*(num-1)*(num-2)/6;	
	}
	
	else{
		lines=1;
	}
}

else
	lines=1;

_id.o('noOfLines').value=lines;
_id.o('totAmtLto').value=lines*_kenoPrice;
}
}

function chkPickType(id){
pickType=_id.o(id).value;
if(pickType==30){ 
_id.o('playType').value="Perm2";
}else if(pickType==31){ 
_id.o('playType').value="Perm3";
pickType=30;
}
else{
_id.o('playType').value="Direct";
_id.o('noOfLines').value=0;
}
kenoclearAllBoxes();
_id.o("totAmtLto").value=0;
_id.o("noOfQp").value=0;
_id.o('noOfLines').value=0;
}

var line='<tr><td bgcolor="#00639c"><table border="0" bordercolor="blue" width="97%"> <tr ><td align="left" width="25%"> <input type="hidden" value="0" id="counterId"> <input type="hidden" id="dateFuture"> <input type="hidden" id="freezeTimeId" value="'+_kenofreezeTime+'"> <span class="style1">Next Draw :</span><div id="latestDrawTime" style="display:none"></div></td><td width="30%" align="left"><div id="nxtDrawTime" class="kenostyle2"></div></td><td align="right" width="25%"><div id="timeLeft"> <span class="style1"> Time Left :</div></span></td> <td align="left" width="15%"><div id="curDrwTime" class="kenostyle2"></div> </td><td rowspan="2" width="10%"><img src="'+fortImgPath+'que.gif" style="cursor:hand" onClick="gameInfo()" alt="Game Info" /> </td></tr> <tr bgcolor="#00639c"> <td align="left"> <span class="style1">No. of Draws :</span> </td> <td > <select	name="noOfDraws" id="noOfDrwId"	class="fortune_option" align="left"	width="20%"	onchange="calAmtKeno()">	<option	value="1"> 1 </option> <option value="2"> 2 </option> <option value="3"> 3	</option> <option value="4"> 4 </option> <option value="5">	5 </option>	<option	value="6"> 6 </option> </select> </td> <td colspan="1" align="left" valign="top"> &nbsp; </td><td align="left"> <span class="kenonum">'+_kenoPrice+'/-</span></td>  </table></td></tr>';
var _kenoNxtDrwTbl=_kenoFillNxtDrw();
var _kenoHtmlTbl='<table width="100%"height="100%" border="0" cellspacing="0" cellpadding="0" bordercolor="red" bgcolor="#9cceff">'+line+'<tr><td align="center" valign="top"><table width="100%" cellpadding="0" cellspacing="0" border="0"><tr><td width="65%" valign="top" ><div style="padding-top:10px;"><table border="0" bordercolor="red" width="97%" align="center"cellpadding="0"cellspacing="0"><tr><td class="style10" align="left"><div align="center" valign="middle" style="height:18px;background-color:#00639c;width:180px;float:left;padding-top:2px">&nbsp;Select Number Panel&nbsp;</div></td></tr><tr><td valign="middle" style="border:1px #00639c solid;background-color:#639cce">'+kenoNmSel(90)+'</td></tr></table></div></td><td valign="top" wdith="35%"><div style="border:0px solid black; padding-top:10px;"><table border="0" bordercolor="red" width="96%" cellpadding="0" cellspacing="0" align="center" style="margin-right:10px"><tr><td align="right"class="style10"style="cursor:hand;"onclick="kenoclearAllBoxes()"><div align="center" valign="middle" style="height:18px;background-color:#00639c;width:100px;float:right;padding-top:2px">&nbsp;&nbsp;&nbsp;Clear All&nbsp;&nbsp;&nbsp;</div></td></tr><tr><td valign="middle" style="border:1px #00639c solid;background-color:#639cce"><table width="100%" border="0" align="center" cellpadding="0" cellspacing="0"><tr><td bgcolor="#00639c"><input type="hidden" value="Direct" name="playType" id="playType"><select name="pickType" id="pickType" style="width:100%;background-color:#00639c;color:#FFFFFF;font-size:12px;font-family:Arial;font-weight:bold" onchange="chkPickType(this.id)"><option value="1"selected="selected">Direct-1</option><option value="2">Direct-2</option><option value="3">Direct-3</option><option value="4">Direct-4</option><option value="5">Direct-5</option><option value="30">Perm-2</option><option value="31">Perm-3</option></select></td></tr><tr><td>'+ kenoRow() +'</td></tr><tr><td bgcolor="#00639c"><table width="95%" border="0"><tr><td class="kenotxtStyle" align="left">Number Picked:</td><td><input type="hidden" value="" id="pickedNumbers" name="pickedNumbers"/><input type="text" class="kenotextbox" name="noPicked" id="noOfQp" value="0" maxlength="2" readOnly="true" onkeyup="return fillNoOfQp(this, event)" onkeydown="return fillNoOfQp(this, event)" onblur="calAmtKeno()"/></td></tr><tr><td class="kenotxtStyle" align="left">No Of Lines :</td><td><input type="text" class="kenotextbox" name="noLines" id="noOfLines" value="0" maxlength="2" readOnly="true" /></td></tr><tr><td class="kenotxtStyle" align="left">Total Amount:</td><td><input type="text" id="totAmtLto" name="totalPurchaseAmt" value="0" readonly="true" class="kenotextbox"/></td></tr><tr><td align="center"class="style5"><div style="border:1px solid #639cce;width:50px;height:42px; float:left">QP<br/><input type="hidden" value="No" name="QP" id="qpVal"><input type="checkbox"id="qpChkAll" onclick="chkQp(this.id)"/></div></td><td><a href="#" ><img src="'+fortImgPath+'buy_lotto.gif"onclick="return buyTicketKeno()"alt="Buy" style="margin-right:4px;border: 1px #9CCEFF solid;"/></a></td></tr></table></td></tr></table></td></tr></table></div></td></tr></table></tr><tr><td>&nbsp;</td></tr></table>';
var _kenoHTML = '<div id="strip">'+drwMenu+'</div><form style="background-color:#FFFFFF;border: 1px solid #393939;"><table  border="0" width="100%" height="100%" cellspacing="0" cellpadding="0" align="center" bgcolor="#FFFFFF"  bordercolor="red"><tr><td width="10%"  valign="top" align="center" onmousedown="return disClick(this)"><input type="hidden" id="drawType"><div id="winningDispDiv" style="border:1px #00639c solid;background-color:#639cce;margin-left:5px;margin-top:3px"></div><div id="nxtDrawDiv" style="border:1px #00639c solid;background-color:#639cce;margin-left:5px;margin-top:5px"></div></td><td width="90%" valign="top" align="right"><div id="kenomiddle" style="border:1px #00639c solid;background-color:#639cce;width:98%;margin-left:5px;margin-top:3px;margin-bottom:1px">'+_kenoHtmlTbl+'</div></td></tr></table></td></tr></table></form>';

parent.frames[0].Version['$Source: /rep/LMS_Mgmt/WebRoot/com/skilrock/lms/web/drawGames/playMgmt/games/keno/keno_old.js,v $'] = '$Id: keno_old.js,v 1.2 2016/10/31 09:45:04 neeraj Exp $';