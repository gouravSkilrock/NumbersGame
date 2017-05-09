var fortInpTxt='input type="text" maxlength="3" style="font-size: 13px; width: 45px; margin-left: 4px" onblur="fortBlur()" onkeydown="return keyPressFort(this,event)" onkeyup="return keyPressFort(this,event)" onmousedown="return disClick(this)"';
var signArr=['Aries','Taurus','Gemini','Cancer','Leo','Virgo','Libra','Scorpio','Sagittarius','Capricorn','Aquarius','Pisces'];
var curDrwTimefortune="";

// by pavan

var mapArr = new Array();	
mapArr[1]='Aries';mapArr[2]='Taurus';mapArr[3]='Gemini';mapArr[4]='Cancer';mapArr[5]='Leo';mapArr[6]='Virgo';	
mapArr[7]='Libra';mapArr[8]='Scorpio';mapArr[9]='Sagittarius';mapArr[10]='Capricorn';mapArr[11]='Aquarius';mapArr[12]='Pisces';


function _fortuneWinAppend(){
	if(!_fortuneWin.match("Result Awaited"))
 _fortuneWin='Result Awaited="" Nxt'+_fortuneWin;
}

function _fortuneLatestDrawTime(){
	return _fortuneStatus.split("=")[1];
}

function fortuneShortTimer(freezeTime){
var shortAmount=freezeTime;	
	//alert(shortAmount);
	// alert('shortAmount*******'+shortAmount);
	if(shortAmount< 0){	//alert(_id.o('drawType'));
	_id.o('drawType').value='ACTIVE';	
		fetchUpdatedData();			
	}
	// date is still good
	else{
		days=0;hours=0;mins=0;secs=0;out="";
		shortAmount = Math.floor(shortAmount/1000);  // seconds
		days=Math.floor(shortAmount/86400);
		shortAmount=shortAmount%86400;

		hours=Math.floor(shortAmount/3600);//hours
		shortAmount=shortAmount%3600;

		mins=Math.floor(shortAmount/60);//minutes
		shortAmount=shortAmount%60;

		secs=Math.floor(shortAmount);//seconds

		if(days != 0){
			out += days +"d";
		}
		if(days != 0 || hours != 0)	{
			out += (hours<10?"0"+hours:hours)+":";		
		}
		else if(hours==0){
			out +="0"+hours+":";
		}
		if(days != 0 || hours != 0 || mins != 0){
			out += (mins<10?"0"+mins:mins)+":";		
		}//(time.getHours()<10?"0"+time.getHours():time.getHours())
		else if(mins==0){
			out +="0"+mins+":";
		}
			out += (secs<10?"0"+secs:secs);		
		
		var seconds=hours*3600+mins*60+secs;
		//alert(out);		
		_id.i("winAwaitedDiv",out);
		//colorChange('result0');
		
		setTimeout("fortuneShortTimer('"+(freezeTime-1000)+"')", 1000);
	}
}




function _fortuneFillWinTbl(){
		var winArr = _fortuneWin.split('Nxt');
		var winHtml='<table width="220" border="0"cellspacing="0"cellpadding="0"><tr><td align="center"valign="top">&nbsp;</td></tr><tr><td align="center"valign="bottom"><img src="'+fortImgPath+'winr.gif"alt="winner"/></td></tr><tr><td align="center"valign="top"><table width="100%"border="0" style="height:180;" cellspacing="0"cellpadding="0"><tr><td width="91%"rowspan="4"align="center"valign="top"><div style="'+scrollColor("#83b34b","#638d31","167")+'"><table width="92%"bgcolor="#92bc61"border="0"cellspacing="0"cellpadding="0" >';
		var winTab = '';
		for(var i=0;i<winArr.length-1;i++){
		if(!winArr[i].match("Result")){
		var time  = new Date(parseInt(winArr[i].split('=')[0].replace(" ","")));
		var newTime=time.getDate()+" "+month[time.getMonth()]+" "+(time.getHours()<10?"0"+time.getHours():time.getHours())+":"+(time.getMinutes()<10?"0"+time.getMinutes():time.getMinutes())+":"+(time.getSeconds()<10?"0"+time.getSeconds():time.getSeconds());
		var img =((winArr[i].split('=')[1]).split(',')).toString().replace(" ","").toLowerCase(); 
		//img=mapArr[img].toLowerCase();
		winTab =winTab+'<tr><td width="69%"height="40"align="center"valign="middle"class="borderbtmfortune"><p>'+newTime+'</p><br/><p><span>'+img.toUpperCase()+'</span></p></td><td width="31%"align="center"valign="middle"class="borderbtmfortune"><img src="'+fortImgPath+img+'Win.png"alt="'+img.toUpperCase()+'"/></td></tr>';
		}
		else{
			winTab =winTab+'<tr id="result'+i+'"><td width="69%" height="60" align="center"valign="middle" style="font-size:12px;font-weight:800">Result Awaited</td><td width="31%" height="60"   align="center"valign="middle"class="borderbtmfortune"><div id="winAwaitedDiv" style="color:#663300;font-size:12px;font-weight:800""></div></td></tr>';
		}
		}
		
		winHtml=winHtml+winTab+'</table></div></td></tr></table></td></tr></table>';
		return winHtml;
}

function _fortuneFillNxtDrw(){
var nxtDrwHTML = '<table cellspacing="0"cellpadding="0" width="220"><tr><td align="center"valign="top"><img src="'+fortImgPath+'Next_draw_fortune.gif"alt="Next draw"/></td></tr><tr><td align="center"valign="top"><div><table width="100%"border="0"cellspacing="0"cellpadding="0"bgcolor="#83b34b"><tr><td width="100%"align="center"valign="top"><div style="'+scrollColor("#83b34b","#638d31","162")+'"><table width="92%"border="0"cellspacing="0"cellpadding="0"class="borderbtmfortune"">';
var nxtDrwArr = _fortuneDrawTime.split(',');

	for(var i=0,l=nxtDrwArr.length;i<l;i++){
	var time  = new Date(parseInt(nxtDrwArr[i].replace(" ","")));
	var newTime=time.getDate()+" "+month[time.getMonth()]+" "+(time.getHours()<10?"0"+time.getHours():time.getHours())+":"+(time.getMinutes()<10?"0"+time.getMinutes():time.getMinutes());
	var hiddenTime="<input type='text'  id='timer"+i+"'>";	
	var style="color:black";
	var data='';	
	if(i==0){
		data=time.getFullYear()+"-"+time.getMonth()+"-"+time.getDate()+"-"+(time.getHours()<10?"0"+time.getHours():time.getHours())+"-"+(time.getMinutes()<10?"0"+time.getMinutes():time.getMinutes())+"-"+(time.getSeconds()<10?"0"+time.getSeconds():time.getSeconds());		 
		 _fortuneNxtDrawTime=(""+time).substring(0,(""+time).lastIndexOf(":")+3);
		 style="color:black;font-size:12px;font-weight:bold;";
		}		
		nxtDrwHTML=nxtDrwHTML+'<tr><td><input type="hidden" value="'+data+'" id="'+i+'timer">&nbsp;</td></tr><tr><td width="56%"align="center" class="borderbtmfortune"><div id="'+i+'blink" style="'+style+'">'+newTime+'</div></td><td width="44%"align="center"><p><div id="nxtDrwD'+i+'" style="display:none"><img src="'+fortImgPath+'freezed.gif"></div></p></td></tr>';
	
	}
	
	nxtDrwHTML=nxtDrwHTML+'</table></div></td></tr></table></div></td></tr></table>';
	return nxtDrwHTML;
}

function buyTicket(){
var obj = _id.o("sunsign");
var noOfDraws=document.getElementsByName('noOfDraws')[0].value;
//alert(noOfDraws);
//alert(_id.o('symbols').value);
//alert(_id.o('noOfPanels').value);
var isQP=_id.o('fortQuickPik').value;
//alert(isQP);
var elms = obj.getElementsByTagName("input");
var sym="",noOfPan="";
for(var l=0;l<elms.length;l++){
	if(elms[l].value!=""){
		sym=sym+","+elms[l].name;
		noOfPan=noOfPan+","+elms[l].value;
	}
}
_id.o('symbols').value=sym.replace(",","");
_id.o('noOfPanels').value=noOfPan.replace(",","");
var symbols=_id.o('symbols').value;
var noOfPanels=_id.o('noOfPanels').value;
var totalPanels=_id.o('totalNoOfPanels').value;
calAmtFort();
if(_id.o('totAmt').value==0||_id.o('totalNoOfPanels').value==0){
alert("Please fill complete entries");if(_id.o('fortQuickPik').checked){_id.o('totalNoOfPanels').focus();}
return false;
}
//submitForm('fortuneBuy.action');
// updated by pavan
gameBuyAjaxReq("fortuneBuy.action?noOfDraws="+noOfDraws+"&symbols="+symbols+"&noOfPanels="+noOfPanels+"&isQuickPick="+isQP+"&totalNoOfPanels="+totalPanels);

/*for(var l=0;l<elms.length;l++){
	elms[l].disabled=false;
}elms[0].focus();*/
return true;
}

function fortBlur(val){

if(!_id.o('fortQuickPik').checked){
var obj = _id.o("sunsign");var isExceed = false;
var elms = obj.getElementsByTagName("input");
var tot=0;

for(var l=0;l<elms.length;l++){
	if(elms[l].value!=""){
		if(isNaN(elms[l].value)){elms[l].value="";}
		else{
		tot=tot+parseInt(elms[l].value);
		if(tot>1000&&val){
		
		val.value=val.value.substring(0,val.value.length-1);
		//fortBlur();
		isExceed=true;
		break;
		}
		}
	}
}
if(!isExceed){
_id.o('totalNoOfPanels').value=tot;
}

}else{
	if(val&&val.value>1000){
		val.value=val.value.substring(0,val.value.length-1);
	}
}
calAmtFort();

}

function fortQP(id){
	var obj = _id.o("sunsign");
	var elms = obj.getElementsByTagName("input");
if(_id.o('fortQuickPik').checked){
	for(var l=0;l<elms.length;l++){
			elms[l].disabled=true;
			elms[l].value="";		}
		
	_id.o('totalNoOfPanels').readOnly = false;
	_id.o(id).value='1';
	_id.o('totalNoOfPanels').focus();		
	_id.o('sunsign').disabled=true;
}else{

	for(var l=0;l<elms.length;l++){
		elms[l].disabled=false;
	}
	_id.o('totalNoOfPanels').readOnly = true;
	_id.o('sunsign').disabled=false;
	_id.o(id).value='2';	
	elms[0].focus();
}
_id.o('totalNoOfPanels').value="";
calAmtFort();
}

function keyPressFort(val,e){
	
	var keyCode = (window.event) ? event.keyCode : e.which;
	//alert(keyCode);
	if (keyCode >= 48 && keyCode < 58) {
	fortBlur(val);	
	return true;
	}
return false;
}
function _fortuneUpData(win,drwTime){
_fortuneWin=win;
_fortuneDrawTime=drwTime;
}

function calAmtFort(){
_id.o('totAmt').value=_id.v('totalNoOfPanels')*_fortunePrice*_id.v('noOfDraws');
}

function selImage(ele,func){
var num = _id.v(ele);
var totPanel = _id.v('totalNoOfPanels');
if(!_id.o('fortQuickPik').checked){
if(func=="plus"){
if(num<999&&totPanel<1000){
_id.fo(0,ele).value=parseInt(num==""?0:num,10)+1;
fortBlur();
}
}else{
if(num>0){
_id.o(ele).value=parseInt(num==""?0:num,10)-1;
fortBlur();
}
}
}
}

function crSign(){
var createSignBx = '<tr>';
for (var i=0;i<signArr.length;i++)
{
var signBox ='<td align="center" valign="top"><table width="126" height="90" style="border:1px #658a3a solid;" cellspacing="0" cellpadding="0" bgcolor="#becdab"><tr><td height="70" colspan="3" align="center" valign="middle"><img src="'+fortImgPath+signArr[i].toLowerCase()+'.gif" alt="'+signArr[i].toUpperCase()+'" /></td></tr><tr><td width="30" align="center" valign="middle" bgcolor="#71776a"  class="txt4" onclick="selImage(\''+signArr[i]+'\',\'minus\')" style="cursor:pointer">-</td><td width="40" align="center" valign="middle" bgcolor="#e5ebdd" class="txt5"> <'+fortInpTxt+' name="'+signArr[i]+'" id="'+signArr[i]+'"/> </td> <td width="30" align="center" valign="middle" bgcolor="#71776a" class="txt4"  onclick="selImage(\''+signArr[i]+'\',\'plus\')" style="cursor:pointer"> + </td></tr></table></td>';
if(i%4==0&&i!=0){
createSignBx=createSignBx+'</tr><tr><td colspan="4" align="center" valign="top" style="line-height:2px">&nbsp;</td></tr><tr>'+signBox;
}else{
createSignBx=createSignBx+signBox;
}
}
createSignBx=createSignBx+'</tr>';
return createSignBx;
}

function _fortuneActiveDrawOptions(){
	var nxtDrwArr = _fortuneDrawTime.split(',');
	var html = new Array();
	html.push('<select name="noOfDraws" id="noOfDraws"class="fortune_option" onchange="calAmtFort()" style="font-size:11px;">')
	for(var i=0; i< nxtDrwArr.length; i++){
		html.push('<option value="1">'+i+'</option>');
	}
	html.push('</select>');
	return html.join("");
}

function fortuneTab(){
var fortuneMid='<div id="tktDispDiv"style="display: none; position: absolute; top: 50px; left: 70px;"></div><div id="gameDispDiv"><form><table width="547"border="0" bordercolor="red" cellspacing="0"cellpadding="0"><tr><td colspan="1" style="font-size:12px; font-weight:bold" align="left"valign="top"><input type="hidden" id="freezeTimeId" value="'+_fortunefreezeTime+'">Next Draw Time:</td><td colspan="1" align="left" valign="top"><div id="latestDrawTime" style="display:none"></div><div id="nxtDrawTime" style=" color:#ffffff";></div></td><td><img src="images/info_icon1.gif" style="cursor:hand" onClick="fortuneGameInfo()"></td></tr><tr><td align="left"valign="middle"><p class="txt3">Time Left:</p></td><td align="left"valign="bottom"><p><div id="curDrwTime" class="txt9"></div></p></td><td width="100" align="right" valign="top" colspan="1" nowrap><b style="font-size:12px; font-weight:bold">Ticket Cost:</b><font size="2">'+_fortunePrice+'</font></td></tr><tr><td align="left"valign="middle" style="padding-top:5px"><label><span class="txt3">No.of Draws</span></label></td><td align="left"valign="top" style="padding-top:5px;padding-bottom:2px">'+ _fortuneActiveDrawOptions() +'</td><td align="right"valign="bottom"><a href="#" onclick="document.forms[0].reset()"><img src="'+fortImgPath+'reset.gif"alt="reset"/></a></td></tr><tr><td colspan="3"height="400"align="center"valign="top"bgcolor="#cbddb5"><table width="550" border="0" bordercolor="red" cellspacing="0" cellpadding="0" id="sunsign"><tr><td colspan="4" align="center" valign="top" style="line-height:4px">&nbsp;</td></tr>'+crSign()+'</table></td></tr><tr></tr><tr><td colspan="3"align="left"valign="top"></td></tr><tr><td colspan="3"align="center"valign="top"><table width="100%"border="0"cellspacing="0"cellpadding="0"><tr ><td width="18%"><label><span>Quick pick</span><input type="checkbox" id="fortQuickPik"name="isQuickPick"value="2" onclick="fortQP(this.id)"/></label></td><td width="34%"><label><span>No.of Tickets</span><input style="font-size:17px;border:1px solid #003399  ;" type="text" size="3"maxlength="4"name="totalNoOfPanels"id="totalNoOfPanels"onkeydown="return keyPressFort(this, event)" onkeyup="return keyPressFort(this, event)" onblur="blurFnFort(this)"readonly="true"/></label></td><td width="36%" nowrap="nowrap"><label><span>Total Amount '+curSym+'</span><input type="text" style="font-size:17px;border:1px solid #003399  ;" name="totalPurchaseAmt"id="totAmt"value="0"readonly="true"size="3"/></label></td><td width="12%"align="right"valign="top"><input type="hidden"name="noOfPanels"value=""id="noOfPanels"/><input type="hidden"name="symbols"value=""id="symbols"/><a href="#" onclick="return buyTicket()"><img src="'+fortImgPath+'buy.gif"alt="buy"style="margin-top: 6px"/></a></td></tr></table></td></tr></table></form></div>';
return fortuneMid;
}

function blurFnFort(){
calAmtFort();
}
var gameChildW=null;
function fortuneGameInfo(){
if (gameChildW) {}
else{
 	gameChildW=window.open("fortune/gameInfo.jsp","NewWin","width=650,height=550,toolbar=0,resizable=0,scrollbars=1,alwaysRaised=0,location=0,menubar=0"); 
	gameChildW.close();
	}
	gameChildW=window.open("fortune/gameInfo.jsp","NewWin","width=650,height=550,toolbar=0,resizable=0,scrollbars=1,alwaysRaised=0,location=0,menubar=0"); 
	
}

function disClick(obj){
if(event.button==2){
obj.oncontextmenu=new Function("return false");
return false;
}
}
var _fortuneNxtDrwTbl=_fortuneFillNxtDrw();
var _fortuneHTML = '<div id="strip">'+drwMenu+'</div><table width="100%"border="0"cellspacing="0"cellpadding="0"><tr><td width="23%"align="center"valign="top"bgcolor="#9acd5e" onmousedown="return disClick(this)"><div id="winningDispDiv"></div><div id="nxtDrawDiv"></div><div id="logoSD"><table><tr><td align="center"valign="top"><img src="'+fortImgPath+'SD.gif" alt="Sugal and Damini"/></td></tr><tr><td align="center"valign="top">&nbsp;<input type="hidden" value="0" id="counterId"><input type="hidden" id="drawType" ><input type="hidden" id="dateFuture"></td></tr></table></div></td><td align="center"width="56%"valign="top"bgcolor="#7ca946">'+fortuneTab()+'</td></tr></table>';
parent.frames[0].Version['$Source: /rep/LMS_Mgmt/WebRoot/com/skilrock/lms/web/drawGames/playMgmt/games/fortune/fortune_old.js,v $'] = '$Id: fortune_old.js,v 1.2 2016/10/31 09:46:38 neeraj Exp $';