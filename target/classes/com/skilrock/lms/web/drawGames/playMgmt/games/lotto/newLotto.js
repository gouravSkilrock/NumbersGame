var _lottoWinArr=new Array();
var curDrwTimelotto="";var lottocurSel="row1";
var isAllQP=false;
function setDefaultlotto(){
	curDrwTimefortune="";
	lottocurSel="row1";
	isAllQP=false;
}

/*
function gameInfo(){
var url="lottoInfo.action";
_id.o('topId').style.display="block";
runAccordion('5');
 _id.i('info5Div','<iframe src="'+url+'" id="tktFrame" style="width:100%;height:100%" frameborder="0"></iframe>' );
_id.o('info5Div').style.marginLeft="200px";
_id.o('info5Div').style.marginTop="60px";

}
*/
function _lottoWinAppend(){ 
	if(!_lottoWin.match("Result Awaited"))
 _lottoWin='Result Awaited="" Nxt'+_lottoWin;
}

function _lottoLatestDrawTime(){ 
	return _lottoStatus.split("=")[1]; 
}

function _lottoFillWinTbl(){ 
	var _lottoWinHt='<table width="100%" border="0" bordercolor="red" cellspacing="0" cellpadding="0" align="center" bgcolor="#FFFFFF">      <tr><td colspan="2" align="left"><div valign="middle" style="height:22px;background-color:#666633;padding-top:3px;font-size:16px;font-family:Arial;font-weight:bold;color:#FFFFFF">&nbsp;Win Result&nbsp;</div></td></tr><tr><td valign="top" align="center">';
	var winArr = _lottoWin.split('Nxt');
	var _lottoWinHTML='<table width="100%" border="0"><tr><td><div style="'+scrollColor("#CCCC99","#666633","182")+'">';	
	for(var i=0;i<=winArr.length-2;i++){
	if(!winArr[i].match("Result")){
		var time  = new Date(parseInt(winArr[i].split('=')[0].replace(" ","")));
		var newTime=week[time.getDay()]+" "+time.getDate()+" "+month[time.getMonth()]+" "+(time.getHours()<10?"0"+time.getHours():time.getHours())+":"+(time.getMinutes()<10?"0"+time.getMinutes():time.getMinutes())+":"+(time.getSeconds()<10?"0"+time.getSeconds():time.getSeconds());
		var htm='<table width="90%" border="0" bordercolor="green" id="result'+i+'" cellspacing="2" cellpadding="10" class="lotto_winningResult_table" ><tr><td class="lottowinningResultTime" colspan="8" height="42">'+newTime+'</td></tr><tr>';
		var num = (winArr[i].split('=')[1]).split(',');
		for(var j=0;j<num.length;j++){
			if(j==3){
				htm=htm+'</tr><tr>';
			}
			var temp=(num[j]<10?"0"+num[j]:num[j]);
		htm=htm+'<td><span class="selected_win_lotto_numbers">'+temp+'</span></td>';
		}
		htm=htm+'</tr></table>';
		_lottoWinHTML=_lottoWinHTML+htm;
		}
		else{
		var htm='<table width="90%" bordercolor="blue" border="0" cellspacing="2" cellpadding="10" id="result'+i+'" class="lotto_winningResult_table" ><tr><td class="lottowinningResultTime" colspan="8" height="60" >Result Awaited</td></tr><tr>'
		htm=htm+'<td class="selected_win_lotto_numbers" style="padding-left:55px"  colspan="8"><div style="text-align:center" id="winAwaitedDiv" style="color:#663300"></div></td>';
		htm=htm+'</tr></table>';		
		_lottoWinHTML=_lottoWinHTML+htm;
	}
		
	}
	
	_lottoWinHTML=_lottoWinHTML+'</div></td></tr></table>';
	return _lottoWinHt+_lottoWinHTML+'</td></tr></table>';
}



function _lottoWinAjx(){
var url = "fetchWinLotto.action?frmDate="+_lottoWinArr[_lottoWinArr.length-1];
_lottoWin=_lottoWin+(winAjaxReq(url)).data;
_lottoFillWinTbl();
}

function lottoNmSel(lstNo){
var htm = '<div id=""><table width="99%" border="0" cellspacing="0" cellpadding="0" bgcolor="#CCCC99" style="margin-top:5px;margin-bottom:3px"><tr>';
for(var i=1;i<=lstNo;i++){
if(i%5==0){
htm = htm+'<td style="cursor:pointer;background:url('+projectName+'/LMSImages/images/lotto_shade_white.gif) center no-repeat" class="selected_lotto_numbers" onmouseout="lottoonmouseouteffect(this.id)" onmouseover="lottoonmouseovereffect(this.id)"  onclick="fillNumLto('+i+')"  id="'+i+'" >'+(i<10?"0"+i:i)+'</td></tr>';
}
else{
htm = htm+'<td class="selected_lotto_numbers"  style="cursor:pointer;background:url('+projectName+'/LMSImages/images/lotto_shade_white.gif) center no-repeat" onmouseout="lottoonmouseouteffect(this.id)" onmouseover="lottoonmouseovereffect(this.id)" onclick="fillNumLto('+i+')"  id="'+i+'"  >'+(i<10?"0"+i:i)+'</td>';
}
}
htm = htm+'</tr></table></div>';
return htm;
}


function lottoonmouseovereffect(id){
		var count=0;
		var isFilled=false;
		for(var i=1;i<=49;i++){	
			if(_id.o(i).className=="selected_lotto_numbersSelect")
				count++;		
				if(count>5){
					isFilled=true;
					break;
				}
		}

	if(!_id.o('QP'+lottocurSel.replace("row","")).checked && !isFilled && !isAllQP){	
		if(_id.o(id).className!="selected_lotto_numbersSelect"){
		_id.o(id).style.background='url('+projectName+'/LMSImages/images/lotto_shade_over.gif) center no-repeat';
		_id.o(id).style.color='#FFFFFF';
		_id.o(id).className='selected_lotto_numbersSelectOMO';
		}
	}
}

function lottoonmouseouteffect(id){
if(!_id.o('QP'+lottocurSel.replace("row","")).checked  && !isAllQP){	
if(_id.o(id).className=='selected_lotto_numbersSelectOMO'){
_id.o(id).style.background='url('+projectName+'/LMSImages/images/lotto_shade_white.gif) center no-repeat';
_id.o(id).style.color='#666633';
_id.o(id).className='selected_lotto_numbers';
}
else if(_id.o(id).className=='selected_lotto_numbersSelect'){
_id.o(id).style.background='url('+projectName+'/LMSImages/images/lotto_shade_over.gif) center no-repeat';
_id.o(id).style.color='#FFFFFF';
}
else if(_id.o(id).className=='selected_lotto_numbers'){
_id.o(id).style.background='url('+projectName+'/LMSImages/images/lotto_shade_white.gif) center no-repeat';
_id.o(id).style.color='#666633';
}
}

}


function lottoRow(noOfpanels){
var htm='<table width="100%" id="lottoRow" border="0" cellspacing="2" cellpadding="0" style="margin-top:3px"><tr><td width="8%" align="center" valign="middle">&nbsp;</td><td width="10%" align="center" valign="middle" class="style10">&nbsp;</td>';   
for(var indx=1;indx<1+parseInt(6);indx++){
htm=htm+'<td width="12%" align="center" valign="middle" class="style10" style="color:#666633">'+indx+'</td>';
}
htm=htm+'<td width="12%" align="center" valign="middle" class="style10" style="color:#666633">QP</td> </tr> <tr>';

for(var i=1;i<parseInt(noOfpanels)+1;i++){
	htm=htm+'<tr onclick="fillSelLto('+i+')" id="row'+i+'"><td class="style10" height="30"></td><td><span class="style10" style="color:#666633">'+i+'&nbsp;<input type="radio" name="lottoRadio" id="radio'+i+'" onclick="fillSelLto('+i+')" '+(i==1?"checked":"")+'/></td>';
		for(var j=0;j<6;j++){
			htm=htm+'<td align="center" valign="middle" ><input type="text" class="selected_lotto" maxlength="2" onkeydown="return keyPressLto(this)" onkeyup="return keyPressLto(this)" onblur="blurFnLto(this)"></td>';
		}
	htm=htm+' <td align="center" valign="middle"><input type="checkbox" value="2" name="isQuickPick" id="QP'+i+'" onclick="quickPkLto('+i+');" /><img src="'+fortImgPath+'clear.gif" onclick="clearBoxes('+i+')" style="cursor:hand"></td></tr>';
	}
return htm+'</table>';

}

function clearBoxes(val){
_id.resetInpTypes('row'+val,'selected_lotto',false);
/*
var obj = _id.o('row'+val);
var elms = obj.getElementsByTagName("input");
	for(var l=1;l<elms.length-1;l++){
			elms[l].value="";
	}*/
calAmtLto();
}

function clearAllBoxes(){
_id.resetInpTypes('lottoRow','selected_lotto',false);
lottocurSel="row1";
/*
var obj = _id.o('lottoRow');
var elms = obj.getElementsByTagName("input");

	for(var l=1;l<elms.length;l++){
		if(elms[l].type=='text'){
			elms[l].value="";elms[l].disabled=false;
			}
		else if(elms[l].type=='checkbox')
				elms[l].checked=false;
	}

	for(var i=1;i<=49;i++){	
		_id.o(i).className="selected_lotto_numbers";
		_id.o(i).style.background='url('+projectName+'/LMSImages/images/lotto_shade_white.gif) center no-repeat';
		_id.o(i).style.color='#666633';
	}
*/
	isAllQP=false;
	lottoapplyCSS();
	_id.o("qpChkAll").checked=false;
	calAmtLto();
}

function _lottoFillNxtDrw(){	
var nxtDrwHTML = '<table width="100%" cellspacing="0"cellpadding="0"  border="0" bordercolor="red"><tr><td colspan="2" align="left" valign="top"><div valign="middle" style="height:22px;width:180px;background-color:#666633;padding-top:3px;font-size:16px;font-family:Arial;font-weight:bold;color:#FFFFFF">&nbsp;Next Draw&nbsp;</div></td></tr><tr><td align="center" valign="top"><div><table width="100% bordercolor="red" border="0" cellspacing="0" cellpadding="0" ><tr><td width="100%" align="center" valign="top" colspan="2"><div style="'+scrollColor("#CCCC99","#666633","180")+'"><table width="90%" bordercolor="red"  border="0" cellspacing="0" cellpadding="0" class="borderbtmlotto">';

var nxtDrwArr = _lottoDrawTime.split(',');
	for(var i=0,l=nxtDrwArr.length;i<l;i++){
	var time  = new Date(parseInt(nxtDrwArr[i].replace(" ","")));
	var newTime=week[time.getDay()]+" "+time.getDate()+" "+month[time.getMonth()]+" "+(time.getHours()<10?"0"+time.getHours():time.getHours())+":"+(time.getMinutes()<10?"0"+time.getMinutes():time.getMinutes());
	var hiddenTime="<input type='text'  id='timer"+i+"'>";	
	var rowcolor;
	var data='';	
	
	if(i==0){
		data="2010"+"-"+time.getMonth()+"-"+time.getDate()+"-"+(time.getHours()<10?"0"+time.getHours():time.getHours())+"-"+(time.getMinutes()<10?"0"+time.getMinutes():time.getMinutes())+"-"+(time.getSeconds()<10?"0"+time.getSeconds():time.getSeconds());		 
		var dateFuture1= new Date('2010',time.getMonth(),time.getDate(),(time.getHours()<10?"0"+time.getHours():time.getHours()),(time.getMinutes()<10?"0"+time.getMinutes():time.getMinutes()),(time.getSeconds()<10?"0"+time.getSeconds():time.getSeconds())); 
		
		 _lottoNxtDrawTime=(""+time).substring(0,(""+time).lastIndexOf(":")+3);
		}
		if(i%2==0){rowcolor="#FFFFFF"}else{rowcolor="#CCCC99"}		
		nxtDrwHTML=nxtDrwHTML+'<tr><td width="100%"><input type="hidden" value="'+data+'" id="'+i+'timer"></td></tr><tr bgcolor="'+rowcolor+'" style="height: 20px"><td width="100%" align="center" class="borderbtmlotto" colspan="2"><div id="'+i+'blink">'+newTime+'</div></td><td width="44%"align="center"><p><div id="nxtDrwD'+i+'" style="display:none"><img src="'+fortImgPath+'freezed.gif"></div></p></td></tr>';
	
	}
	
	nxtDrwHTML=nxtDrwHTML+'</table></div></td></tr></table></div></td></tr></table>';
	return nxtDrwHTML;
}
function _lottoUpData(win,drwTime,drwStatus){
_lottoWin=win;
_lottoDrawTime=drwTime;
_lottoStatus=drwStatus;
}

function fillSelLto(val){
	if(!isAllQP){
		lottocurSel = "row"+val;_id.o('radio'+val).checked=true;
		lottoapplyCSS();
	}
}

function chkAll(id){
if (_id.o(id).checked == true) {
		for (var i = 0; i < _id.n("isQuickPick").length; i++) {
			var e = _id.n("isQuickPick")[i];
			e.checked = true;
			quickPkLto(i+1);
		}
	} else {
		for (var i = 0; i < _id.n("isQuickPick").length; i++) {
			var e = _id.n("isQuickPick")[i];
			e.checked = false;
			quickPkLto(i+1);
		}
	}
_id.o('radio1').checked=true;lottocurSel = "row1";
lottoapplyCSS();
}

function lottoChkNoOfQP(){
		var val=parseInt(_id.o('tkts').value);
		if(isNaN(val)){
			val=0;
		}
		for (var i = 0; i < val; i++) {
			var e = _id.n("isQuickPick")[i];
			e.checked = true;
			quickPkLto(i+1);
		}
		for (var i = parseInt(val); i < _id.n("isQuickPick").length; i++) {
			var e = _id.n("isQuickPick")[i];
			e.checked = false;
			quickPkLto(i+1);
		}
		var rowNo=(parseInt(val)+1 > 8)?1:parseInt(val)+1;
		_id.o('radio'+rowNo).checked=true;
		lottocurSel = "row"+rowNo;
		lottoapplyCSS();
			
}

function lottoChkQP(id){
	if (_id.o(id).checked == true) {
		isAllQP=true;
		_id.o("tkts").value="";
		_id.o("tkts").readOnly=false;
		lottoapplyCSS();
		_id.o("tkts").focus();
		
	}
	else{
		isAllQP=false;
		for (var i = 0; i < _id.n("isQuickPick").length; i++) {
			var e = _id.n("isQuickPick")[i];
			e.checked = false;
			quickPkLto(i+1);
		}
		_id.o("tkts").value="0";
		_id.o("tkts").readOnly=true;
		_id.o('radio1').checked=true;
		lottocurSel = "row1";
		lottoapplyCSS();
	}
}

function lottofillNoOfQp(obj){
	var keyCode = event.keyCode;
	if ((keyCode >= 48 && keyCode < 58) || keyCode == 45 || keyCode == 27 ||keyCode == 13 ||keyCode == 40 || keyCode == 38 ||keyCode == 46 || keyCode == 8  ||   (keyCode >= 96 && keyCode < 106 || keyCode == 9 || keyCode == 189 ||keyCode == 109)) {
			if(keyCode == 46 || keyCode == 8 || keyCode == 9 ){
				lottoChkNoOfQP();
				return true;
			}
			if(obj.value==0||obj.value>8||isNaN(obj.value)){
				obj.value="";
				obj.focus();
			}
	}
	else{
			return false;
	}
	
	lottoChkNoOfQP();
	return true;
}

function fillNumLto(val){
if(!_id.o('QP'+lottocurSel.replace("row","")).checked && !isAllQP){	
	var objR = _id.o(lottocurSel);var clrChg = true;
	var elms = objR.getElementsByTagName("input");
	if(_id.o(val).className=="selected_lotto_numbersSelect"){ 
		for(var l=1;l<elms.length-1;l++){
			if(elms[l].value==val){
			elms[l].value="";break;
			}
		}
	}else{
		for(var l=1;l<elms.length-1;l++){
			if(elms[l].value==""){elms[l].value=(val<10?"0"+val:val);break;}
		}
		for(var l=1;l<elms.length-1;l++){
			if(elms[l].value==""){elms[l].focus();break;}
		}								
	}
	
lottoapplyCSS();	
}
}

function quickPkLto(val){
var isChq = false;lottocurSel = "row"+val;
_id.o('radio'+val).checked=true;
if(_id.o('QP'+val).checked){
	isChq=true;	
}
var obj = _id.o('row'+val);
var elms = obj.getElementsByTagName("input");
	for(var l=1;l<elms.length-1;l++){
			elms[l].disabled=isChq;
			elms[l].value="";
	}
calAmtLto();	
}


function calAmtLto(){

var noOfDrw = _id.v("noOfDraws");
var noOfPanel = 0;
var obj,elms;var isPanel=true;
	for(var i=1;i<9;i++){
		isPanel=true;
		if(_id.o("QP"+i).checked){
		noOfPanel++;	
		}else{
		obj = _id.o('row'+i);
		elms = obj.getElementsByTagName("input");
			for(var l=1;l<elms.length-1;l++){
					if(elms[l].value==""||elms[l].value.length<2||isNaN(elms[l].value)){isPanel = false;elms[l].value="";}
			}
		if(isPanel)noOfPanel++;	
		}
	}	
_id.o("totAmtLto").value=noOfDrw*noOfPanel*_lottoPrice;
if(noOfPanel!=0 || _id.o('qpChkAll').checked==false)
_id.o("tkts").value=noOfPanel;
else
_id.o("tkts").value="";
}

function fillNoOfPanels(id){
var noOfpanels=_id.o(id).value;
	_id.i('lottoRowDiv',lottoRow(noOfpanels));
}

function keyPressLto(obj){
	var keyCode = event.keyCode;
	if ((keyCode >= 48 && keyCode < 58) || keyCode == 45 || keyCode == 27 ||keyCode == 13 ||keyCode == 40 || keyCode == 38 ||keyCode == 46 || keyCode == 8  ||   (keyCode >= 96 && keyCode < 106 || keyCode == 9 || keyCode == 189 ||keyCode == 109)) {
		if(keyCode == 46 || keyCode == 8 || keyCode == 9 )return true;
		if(obj.value.length>1){
				if(obj.value==0||obj.value>49||isNaN(obj.value)){
					obj.value="";
					obj.focus();
				}else{
					var objR = _id.o(lottocurSel);var clrChg = true;
					var elms = objR.getElementsByTagName("input");
						for(var l=1;l<elms.length-1;l++){
								clrChg = true;
								if((elms[l].value==obj.value)&&(elms[l]!=obj)){
								obj.value="";obj.focus();clrChg = false;break;
								}
						}
					if(clrChg){
						for(var l=1;l<elms.length-1;l++){
								if(elms[l].value==""){elms[l].focus();break;}
						}
					
					}	
				}
			lottoapplyCSS();	
			}				
	}else{
		return false;
		}
	return true;	
}

function blurFnLto(obj){
if((obj.value.length<2)||isNaN(obj.value)||obj.value==0){
	obj.value="";
}
lottoapplyCSS();
}

function lottoapplyCSS(){
	for(var i=1;i<=49;i++){
		_id.o(i).className="selected_lotto_numbers";
		_id.o(i).style.background='url('+projectName+'/LMSImages/images/lotto_shade_white.gif) center no-repeat';
		_id.o(i).style.color='#666633';
	}
			
	var obj = _id.o(lottocurSel);var clrChg = true;
	var elms = obj.getElementsByTagName("input");
		for(var l=1;l<elms.length-1;l++){
			for(var j=0;j<elms.length-1;j++){
				if((elms[l].value==elms[j].value)&&(elms[l]!=elms[j])){
					elms[j].value="";
				}
			}
		}		var count=0;
		for(var l=1;l<elms.length-1;l++){
			if(elms[l].value!=""){
			if(count<6){	
				_id.o(parseInt(elms[l].value,10)).className="selected_lotto_numbersSelect";
				_id.o(parseInt(elms[l].value,10)).style.background='url('+projectName+'/LMSImages/images/lotto_shade_over.gif) center no-repeat';
				_id.o(parseInt(elms[l].value,10)).style.color='#FFFFFF';		
					count++;	}	
			}		
		}
		
	var obj1=_id.o('lottoRow');
	var txtelms = obj1.getElementsByTagName("input");
	if(isAllQP){
		for(var i=1;i<=49;i++){
			_id.o(i).className="selected_lotto_numbersDisabled";
			_id.o(i).style.background='url('+projectName+'/LMSImages/images/lotto_shade_disable.gif) center no-repeat';
			_id.o(i).style.color='#FFFFFF';
		}
		
		for(var l=0;l<txtelms.length;l++){
				txtelms[l].disabled=true;
		}
	}
	else{
		for(var l=0;l<txtelms.length;l++){
				txtelms[l].disabled=false;
				if(txtelms[l].type=='checkbox'){
					if(txtelms[l].checked==true){
						for(var i=1;i<=6;i++){
							txtelms[l-i].disabled=true;
						}
					}
				}
		}
	}
calAmtLto();
		
}

function resetFrm(){
	document.forms[0].reset();
	for(var i=1;i<=49;i++){
		_id.o(i).className="selected_lotto_numbers";
		_id.o(i).style.background='url('+projectName+'/LMSImages/images/lotto_shade_white.gif) center no-repeat';
		_id.o(i).style.color='#666633';
	}
		var elms = document.forms[0].getElementsByTagName("input");
		for(var l=0;l<elms.length;l++){
			elms[l].disabled=false;
		}
	lottocurSel = "row1";	
}
var gameChildW=null;
function lottoGameInfo(){
if (gameChildW) {}
else{
 	gameChildW=window.open("lotto/gameInfo.jsp","NewWin","width=650,height=550,toolbar=0,resizable=0,scrollbars=1,alwaysRaised=0,location=0,menubar=0"); 
	gameChildW.close();
	}
	gameChildW=window.open("lotto/gameInfo.jsp","NewWin","width=650,height=550,toolbar=0,resizable=0,scrollbars=1,alwaysRaised=0,location=0,menubar=0"); 
	
}

function buyTicketLotto(){
	var compData="";
	for(var i=1;i<9;i++){
		var inputData="";
		var obj = _id.o('row'+i);
		var elms = obj.getElementsByTagName("input");
			if(!_id.o('QP'+i).checked){
			for(var l=1;l<elms.length-1;l++){
					if(elms[l].value!=""){
					inputData=inputData+elms[l].value+",";}
			}
			if(inputData!=""&&inputData.split(",").length<1+parseInt(6)){
			alert("Please fill incomplete entries");
			return false;}
			if(inputData!="")
			inputData=arrSort(inputData.substring(0,inputData.length-1))+"Nxt";
			}else{
			inputData="QP"+"Nxt";
			}
		
		compData=compData+inputData;	
		}
		compData=compData.substring(0,compData.length-3);
	_id.o('pickedNumbers').value=compData;
	if(compData.length<1){
	alert("Please fill incomplete entries");
	return false;
	}

var amount=_id.o('totAmtLto').value;
gameBuyAjaxReq("lottoBuy.action?pickedNumbers="+compData+"&noOfDraws="+_id.o('noOfDraws').value+"&totalPurchaseAmt="+amount);	
clearAllBoxes();
return true;
}

var line='<tr><td bgcolor="#666633"><table border="0" bordercolor="blue" width="97%"><tr><td align="left" width="25%"><input type="hidden" value="0" id="counterId"><input type="hidden" id="dateFuture"><input type="hidden" id="freezeTimeId" value="'+_lottofreezeTime+'"><span class="style1">Next Draw :</span><div id="latestDrawTime" style="display:none"></div></td><td width="30%" align="left"><div id="nxtDrawTime" class="lottostyle2"></div></td><td align="right" width="25%"><div id="timeLeft"><span class="style1"> Time Left :</div></span></td> <td align="left" width="15%"><div id="curDrwTime" class="lottostyle2"></div> </td><td rowspan="2" width="10%"><img src="'+fortImgPath+'que.gif" style="cursor:hand" onClick="gameInfo()" alt="Game Info" /> </td></tr> <tr bgcolor="#666633"><td align="left"> <span class="style1">No. of Draws :</span> </td> <td > <select	name="noOfDraws" id="noOfDrwId"	class="fortune_option" align="left"	width="20%"	onchange="calAmtLto()"><option	value="1"> 1 </option> <option value="2"> 2 </option> <option value="3"> 3	</option> <option value="4"> 4 </option> <option value="5">	5 </option>	<option	value="6"> 6 </option> </select> </td> <td colspan="1" align="left" valign="top"> &nbsp; </td><td align="left"> <span class="lottonum">'+_lottoPrice+'/-</span></td>  </table></td></tr>';
var _lottoNxtDrwTbl=_lottoFillNxtDrw();
var _lottoHtmlTbl='<table width="100%" height="100%" border="0" cellspacing="0" cellpadding="0" bordercolor="red" bgcolor="#FFFFFF">'+line+'<tr><td align="center" valign="top"><table width="100%" cellspacing="0" cellpadding="0" border="0"><tr><td width="50%" valign="top"><div style="padding-top:10px;"><table border="0" bordercolor="red" width="97%" align="center"cellpadding="0"cellspacing="0"><tr><td class="style10" align="left"><div align="center" valign="middle" style="height:18px;background-color:#666633;width:180px;float:left;padding-top:2px">&nbsp;Select Number Panel&nbsp;</div></td></tr><tr><td valign="middle" style="border:1px #666633 solid;background-color:#CCCC99">'+lottoNmSel(49)+'</td></tr></table></div></td><td valign="top" wdith="35%"><div style="border:0px solid black; padding-top:10px;"><table border="0" bordercolor="red" width="96%" cellpadding="0" cellspacing="0" align="center" style="margin-right:10px"><tr><td align="right" class="style10" style="cursor:hand;" onclick="clearAllBoxes()"><div align="center" valign="middle" style="height:18px;background-color:#666633;width:100px;float:right;padding-top:2px">&nbsp;&nbsp;&nbsp;Clear All&nbsp;&nbsp;&nbsp;</div></td></tr><tr><td valign="middle" style="border:1px #666633 solid;background-color:#CCCC99"><table width="100%" border="0" align="center" cellpadding="0" cellspacing="0"><tr><td>'+lottoRow("8")+'</td></tr><tr><td bgcolor="#666633"><table width="95%" border="0"><tr><td align="center" class="style5" rowspan="2"><div style="border: 1px solid #CCCC99; width: 50px; height: 40px; float: left">QP<br /><input type="checkbox" id="qpChkAll" onclick="lottoChkQP(this.id)" /></div></td><td class="style8" align="left">Qty:</td><td><input type="hidden" value="" id="pickedNumbers" name="pickedNumbers" /><input type="text" style="width:25px;font-size: 10px; margin-bottom: 4px;" id="tkts" maxlength="1" readonly="true" value="0" onkeyup="return lottofillNoOfQp(this)" onkeydown="return lottofillNoOfQp(this)" onblur="lottoChkNoOfQP()"/></td><td rowspan="2"><a href="#"><img src="'+fortImgPath+'lotto_buy.gif" onclick="return buyTicketLotto()" alt="Buy" style="margin-right: 4px; border: 1px #C3C3E5 solid;" /></a></td></tr><tr><td class="style8" align="left">Total Amount:</td><td><input type="text" id="totAmtLto" name="totalPurchaseAmt" value="0" readonly="true" style="width:25px; font-size:10px;" /></td></tr> </table></td></tr></table></td></tr></table></div></td></tr></table></tr><tr><td>&nbsp;</td></tr></table>';
var _lottoHTML = '<div id="strip">'+drwMenu+'</div><form style="background-color:#CCCC99;border: 1px solid #393939;"><table  border="0" width="100%" height="100%" cellspacing="0" cellpadding="0" align="center" bgcolor="#CCCC99" bordercolor="red"><tr><td width="10%"  valign="top" align="center" ><input type="hidden" id="drawType"><div id="winningDispDiv" style="border:1px #666633 solid;background-color:#FFFFFF;margin-left:5px;margin-top:3px"></div><div id="nxtDrawDiv" style="border:1px #666633 solid;background-color:#FFFFFF;margin-left:5px;margin-top:5px"></div></td><td width="90%" valign="top"><div id="lottomiddle" style="border:1px #666633 solid;background-color:#CCCC99;width:98%;margin-left:8px;margin-top:3px">'+_lottoHtmlTbl+'</div></td></tr></table></td></tr><tr><td>&nbsp;</td></tr></table></form>';
