var _0to9InpTxt='input type="text" maxlength="3" style="font-size: 13px; width: 45px;" onblur="_0to9Blur()" onkeydown="return keyPress0to9(this)" onkeyup="return keyPress0to9(this)" onmousedown="return disClick(this)"';
var numArr=['0','1','2','3','4','5','6','7','8','9'];
var curDrwTime0to9="";
var _0to9Color=['#2FADBB','#DCEDEE','white','white'];
function setDefault0to9(){
	curDrwTime0to9="";
}

var mapArr = new Array();	
mapArr[1]='0';mapArr[2]='1';mapArr[3]='2';mapArr[4]='3';mapArr[5]='4';mapArr[6]='5';	
mapArr[7]='6';mapArr[8]='7';mapArr[9]='8';mapArr[10]='9';

function _0to9WinAppend(){
	if(!_0to9Win.match("Result Awaited"))
 			_0to9Win='Result Awaited="" Nxt'+_0to9Win;
}

function _0to9LatestDrawTime(){
	return _0to9Status.split("=")[1];
}

//FILLING WINNING RESULT DIV 
function _0to9FillWinTbl(){
		var winArr = _0to9Win.split('Nxt');
		var winHtml='<table width="100%" border="0" cellspacing="0" cellpadding="0"><tr><td align="left"><div valign="middle" style="height:22px;background-color:'+_0to9Color[0]+';padding-top:3px;font-size:16px;font-family:Arial;font-weight:bold;color:#FFFFFF">&nbsp;Win Result&nbsp;</div></td></tr><tr><td><table width="98%" border="0" cellspacing="0" cellpadding="0"><tr><td><div style="'+scrollColor(_0to9Color[1],_0to9Color[0],"210")+'"><table width="90%" border="0"cellspacing="0"cellpadding="0" >';
		var winTab = '';
		for(var i=0;i<winArr.length-1;i++){
		if(!winArr[i].match("Result")){
		var time  = new Date(parseInt(winArr[i].split('=')[0].replace(" ","")));
		var newTime=time.getDate()+" "+month[time.getMonth()]+" "+(time.getHours()<10?"0"+time.getHours():time.getHours())+":"+(time.getMinutes()<10?"0"+time.getMinutes():time.getMinutes())+":"+(time.getSeconds()<10?"0"+time.getSeconds():time.getSeconds());
		var img =((winArr[i].split('=')[1]).split(',')).toString().replace(" ","").toLowerCase(); 
		winTab =winTab+'<tr><td width="69%"height="40"align="center"valign="middle"class="borderbtm0to9"><p>'+newTime+'</p></td><td width="31%" align="center" valign="middle"class="borderbtm0to9"><img src="'+_0to9ImgPath+img+'Win.png"alt="'+img.toUpperCase()+'"/></td></tr>';
		}
		else{
		winTab='<tr id="result'+i+'"><td height="60" colspan="2" align="left" valign="middle" class="borderbtm0to9" style="font-size:14px;">';
		winTab=winTab+'<table width="90%" bordercolor="blue" border="0" cellspacing="2" cellpadding="10" id="result'+i+'" ><tr><td colspan="8" height="40" align="center" style="font-size:16px;">Result Awaited</td></tr><tr>'
		winTab=winTab+'<td colspan="8" align="center"><div style="text-align:center" id="winAwaitedDiv" style="color:'+_0to9Color[0]+'"></div></td>';
		winTab=winTab+'</tr></table></td></tr>';
		//winTab =winTab+'Result Awaited</td><td colspan="2" height="20"   align="left"valign="middle"class="borderbtm0to9"><div id="winAwaitedDiv" style="color:#663300;font-size:12px;font-weight:800""></div></td></tr>';
		}
		}
		
		winHtml=winHtml+winTab+'</table></div></td></tr></table></td></tr></table>';
		return winHtml;
}

// FILLING NEXT DRAWS DIV
function _0to9FillNxtDrw(){
	var nxtDrwHTML = '<table cellspacing="0" cellpadding="0" width="100%" bgcolor='+_0to9Color[1]+'><tr><td align="left"><div valign="middle" style="height:22px;background-color:'+_0to9Color[0]+';padding-top:3px;font-size:16px;font-family:Arial;font-weight:bold;color:#FFFFFF">&nbsp;Next Draw&nbsp;</div></td></tr><tr><td valign="top"><table width="99%" border="0" cellspacing="0" cellpadding="0" bgcolor="'+_0to9Color[1]+'"><tr><td><div style="'+scrollColor(_0to9Color[1],_0to9Color[0],"155")+'"><table width="90%" border="0" cellspacing="0" cellpadding="0" class="nextdraw0to9">';
	var nxtDrwArr = _0to9DrawTime.split(',');
	for(var i=0,l=nxtDrwArr.length;i<l;i++){
	var time  = new Date(parseInt(nxtDrwArr[i].replace(" ","")));
	var newTime=time.getDate()+" "+month[time.getMonth()]+" "+(time.getHours()<10?"0"+time.getHours():time.getHours())+":"+(time.getMinutes()<10?"0"+time.getMinutes():time.getMinutes());
	var hiddenTime="<input type='text'  id='timer"+i+"'>";	
	var rowcolor;
	var data='';	
	if(i==0){
		 data="2010"+"-"+time.getMonth()+"-"+time.getDate()+"-"+(time.getHours()<10?"0"+time.getHours():time.getHours())+"-"+(time.getMinutes()<10?"0"+time.getMinutes():time.getMinutes())+"-"+(time.getSeconds()<10?"0"+time.getSeconds():time.getSeconds());		 
		 _0to9NxtDrawTime=(""+time).substring(0,(""+time).lastIndexOf(":")+3);
	}	
		if(i%2==0){rowcolor=_0to9Color[1];}else{rowcolor=_0to9Color[2];}	
		nxtDrwHTML=nxtDrwHTML+'<tr><td><input type="hidden" value="'+data+'" id="'+i+'timer"></td></tr><tr bgcolor="'+rowcolor+'" style="height: 20px"><td width="100%" class="borderbtm0to9"><div id="'+i+'blink">'+newTime+'</div></td><td width="44%"><p><div id="nxtDrwD'+i+'" style="display:none"><img src="'+_0to9ImgPath+'freezed.gif"></div></p></td></tr>';
	
	}
	
	nxtDrwHTML=nxtDrwHTML+'</table></div></td></tr></table></td></tr></table>';
	return nxtDrwHTML;
}

// BUY TICKET
function _0to9buyTicket(){
	var noOfDraws=document.getElementsByName('noOfDraws')[0].value;
	var isQP=_id.o('_0to9QuickPik').value;
	var sym="",noOfPan="";
	
	for(var l=0;l<numArr.length;l++){
		var elms=_id.o(numArr[l]);
		if(elms.value!=""){
			sym=sym+","+elms.name;
			noOfPan=noOfPan+","+elms.value;
		}
	}
	
	_id.o('symbols').value=sym.replace(",","");
	_id.o('noOfPanels').value=noOfPan.replace(",","");
	var symbols=_id.o('symbols').value;
	var noOfPanels=_id.o('noOfPanels').value;
	var totalPanels=_id.o('totalNoOfPanels').value;
	calAmt0to9();
	var amount=_id.o('totAmt').value;
	
	if(_id.o('totAmt').value==0||_id.o('totalNoOfPanels').value==0){
		alert("Please fill complete entries");
		if(_id.o('_0to9QuickPik').checked){
			_id.o('totalNoOfPanels').focus();
		}
	return false;
	}
	
	document.forms[0].reset();
	gameBuyAjaxReq("zeroToNineBuy.action?noOfDraws="+noOfDraws+"&symbols="+symbols+"&noOfPanels="+noOfPanels+"&isQuickPick="+isQP+"&totalNoOfPanels="+totalPanels+"&totalPurchaseAmt="+amount);
	_0to9clearAllBoxes();
	return true;
}

function _0to9Blur(val){
	if(!_id.o('_0to9QuickPik').checked){
		/*var obj = _id.o("numbers");
		
		var elms = obj.getElementsByTagName("input");*/
		var tot=0;
		var isExceed = false;
		for(var l=0;l<numArr.length;l++){
			var elms=_id.o(numArr[l]);
			if(elms.value!=""){
				if(isNaN(elms.value)){elms.value="";}
				else{
					tot=tot+parseInt(elms.value);
					if(tot>=1000&&val){
						alert('Cannot Select More than 999 Panels ');		
						val.value=val.value.substring(0,val.value.length-1);
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
		if(val && val.value>1000){
			val.value=val.value.substring(0,val.value.length-1);
		}
		if(isNaN(_id.o('totalNoOfPanels').value)){
		_id.o('totalNoOfPanels').value="";
		}
	}
	calAmt0to9();
}

function _0to9QP(id){
	/*var obj = _id.o("numbers");
	var elms = obj.getElementsByTagName("input");*/
	if(_id.o('_0to9QuickPik').checked){
		for(var l=0;l<numArr.length;l++){
			var elms=_id.o(numArr[l]);
			if(elms.type=='text'){
				elms.disabled=true;
				elms.value="";
			}	
		}
		_id.o('totalNoOfPanels').readOnly = false;
		_id.o(id).value='1';
		_id.o('totalNoOfPanels').focus();
	}else{
		for(var l=0;l<numArr.length;l++){
			var elms=_id.o(numArr[l]);
			if(elms.type=='text'){
				elms.disabled=false;
			}	
		}
		_id.o('totalNoOfPanels').readOnly = true;
		_id.o(id).value='2';	
		_id.o(numArr[0]).focus();
	}
	_id.o('totalNoOfPanels').value="";
	calAmt0to9();
}

function keyPress0to9(val){
	var keyCode = event.keyCode;
	//alert(keyCode)
	if ((keyCode >= 48 && keyCode < 58) || keyCode == 45 || keyCode == 27 ||keyCode == 13 ||keyCode == 40 || keyCode == 38 ||  (keyCode >= 96 && keyCode < 106 ||keyCode == 46 || keyCode == 8 || keyCode == 9 || keyCode == 189 ||keyCode == 109)) {
	_0to9Blur(val);	
	return true;
	}
	return false;
}

function _0to9UpData(win,drwTime,drwStatus){
	_0to9Win=win;
	_0to9DrawTime=drwTime;
	_0to9Status=drwStatus;
}

function calAmt0to9(){
	_id.o('totAmt').value=_id.v('totalNoOfPanels')*_0to9Price*_id.v('noOfDraws');
}

function selImage0to9(ele,func){
	var num = _id.v(ele);
	var totPanel = _id.v('totalNoOfPanels');
	if(!_id.o('_0to9QuickPik').checked){
		if(func=="plus"){
			if(num<999&&totPanel<999){
			_id.o(ele).value=parseInt(num==""?0:num,10)+1;
			_0to9Blur();
			}
		}else{
			if(num>0){
			_id.o(ele).value=parseInt(num==""?0:num,10)-1;
			_0to9Blur();
			}
		}
	}
}

function crNum(){
	var createNumBx = '<tr> <td colspan="4" align="center" valign="middle" style="line-height:7px">&nbsp;</td> </tr><tr>';
	for (var i=0;i<numArr.length;i++)
	{
	var numBox =' <td width="25%" align="center" valign="top" style="padding:0px 5px 0px 5px;"><table width="100%" height="113" border="0" style="border:1px '+_0to9Color[0]+' solid;" bordercolor="red" cellspacing="0" cellpadding="0"  class="borderbox0to9" bgcolor="'+_0to9Color[2]+'"><tr> <td colspan="3" align="center" valign="middle"><img src="'+_0to9ImgPath+numArr[i].toLowerCase()+'.gif" alt="'+numArr[i].toUpperCase()+'"/></td></tr><tr> <td width="35" height="25" align="center" valign="middle"><img src="'+_0to9ImgPath+'minus.gif"  style="cursor:pointer" alt="minus" onclick="selImage0to9(\''+numArr[i]+'\',\'minus\')"/></td><td width="40" align="left" ><'+_0to9InpTxt+' name="'+numArr[i]+'" id="'+numArr[i]+'"/> </td> <td width="35" align="center" valign="middle"><img src="'+_0to9ImgPath+'plus.gif" alt="Plus" onclick="selImage0to9(\''+numArr[i]+'\',\'plus\')" style="cursor:pointer"/></td>   </td></tr></table></td>';
	if(i%4==0&&i!=0){
	createNumBx=createNumBx+'</tr><tr> <td colspan="4" align="center" valign="middle" style="line-height:10px">&nbsp;</td> </tr><tr>'+numBox;
	}else{
	createNumBx=createNumBx+numBox;
	}
	}
	var bottomTlb='<td colspan="2" valign="top" style="padding:0px 5px 0px 5px;"><table width="100%" height="113" border="0" bordercolor="#000000" cellspacing="0" cellpadding="0" bgcolor="'+_0to9Color[0]+'" style="border:1px '+_0to9Color[3]+' solid" class="borderbox0to9"><tr align="left"><td class="style4" style="padding:5px;" wdith="20px">No. Of Tickets:</td><td class="style1" ><input type="text" maxlength="3" name="totalNoOfPanels" id="totalNoOfPanels" onblur="_0to9Blur()" onkeydown="return keyPress0to9(this)" onkeyup="return keyPress0to9(this)" onmousedown="return disClick(this)" readonly="true" style="width: 35px;height:10px;font:12px;"/></td></tr><tr align="left"><td  style="padding:5px;"><span class="style4">Total Amount:</span></td><td  class="style1"><input type="text"name="totalPurchaseAmt" id="totAmt" value="0" readonly="true" style="width: 35px;height:10px;font:12px;"/></td></tr><tr align="left"><td class="style4"  style="padding:5px;"><div style="border: 1px solid #FFFFFF; width: 50px; height: 42px; float: left;padding-top:3px" align="center">QP<br /><input style="border: 1px solid #FFFFFF;" type="checkbox" id="_0to9QuickPik" name="isQuickPick" value="2" onclick="_0to9QP(this.id)" /></div></td><td align="right" style="padding:5px;"><input type="hidden" name="noOfPanels" value="" id="noOfPanels"/><input type="hidden" name="symbols" value="" id="symbols"/><img style="margin-top: 3px; margin-bottom: 1px"src="'+_0to9ImgPath+'buy_0to9.gif" alt="Buy" style="cursor:hand" onclick="return _0to9buyTicket()"/></td></tr></table></td>';
	createNumBx=createNumBx+bottomTlb+'</tr><tr> <td colspan="4" align="center" valign="middle" style="line-height:9px">&nbsp;</td> </tr>';
	return createNumBx;
}

function _0to9Tab(){
	var line='<tr><td bgcolor="'+_0to9Color[0]+'"><table border="0" bordercolor="blue" width="100%" > <tr ><td align="left" width="25%"><input type="hidden" id="freezeTimeId" value="'+_0to9freezeTime+'"><span class="style1">Next Draw :</span><div id="latestDrawTime" style="display:none"></div></td><td width="30%" align="left"><div id="nxtDrawTime" class="style2_0to9"></div></td> <td align="right" width="20%"><div id="timeLeft"><span class="style1">Time Left :</span> </div></td><td align="left" width="18%"><div id="curDrwTime" class="style2_0to9"></div></td> <td  rowspan="2" width="12%"><img src="'+_0to9ImgPath+'que.gif" style="cursor:hand" onClick="gameInfo()" alt="Game Info" /></td> </tr> <tr bgcolor="'+_0to9Color[0]+'"> <td align="left"><span class="style1">No. of Draws :</span></td> <td> <select name="noOfDraws" id="noOfDraws" class="option_0to9" align="left" onchange="calAmt0to9()"><option value="1">1</option><option value="2">2</option><option value="3">3</option><option value="4">4</option><option value="5">5</option><option value="6">6</option></select></td><td colspan="1" align="left" valign="top"> &nbsp; </td><td align="center"><span class="num0to9">'+_0to9Price+'/-</span></td>  </table></td></tr>';	
	var _0to9Mid='<div style="border:1px '+_0to9Color[0]+' solid;background-color:'+_0to9Color[1]+';width:96%;margin-left:8px;margin-top:3px;margin-bottom:1px"><table width="100%" border="0" bordercolor="green" cellspacing="0" cellpadding="0" bgcolor="'+_0to9Color[1]+'">'+line+' <tr> <td colspan="6" align="center" valign="top"> <table width="98%" border="0" cellspacing="0" cellpadding="0" id="numbers" bgcolor="'+_0to9Color[1]+'" style="margin-top:3px"> '+crNum()+' </table></td></tr></table></div>';         
	return _0to9Mid;
}


function _0to9clearAllBoxes(){
	for(var l=0;l<numArr.length;l++){
		var elms=_id.o(numArr[l]);
		elms.disabled=false;
	}
	_id.o('_0to9QuickPik').value='2';
	_id.o('totalNoOfPanels').readOnly = true;
	_id.o(numArr[0]).focus();
}

var _0to9NxtDrwTbl=_0to9FillNxtDrw();
var _0to9HTML = '<div id="fortunestrip">'+drwMenu+'</div><form style="background-color:#FFFFFF;border: 1px solid #393939;"><table  width="100%" border="0" cellspacing="0"cellpadding="0"  bordercolor="aqua" bgcolor="'+_0to9Color[2]+'"><tr><td width="30%" align="center" valign="top" ><input type="hidden" value="0" id="counterId"><input type="hidden" id="drawType" ><input type="hidden" id="dateFuture"><div id="winningDispDiv" style="border:1px '+_0to9Color[0]+' solid;background-color:'+_0to9Color[1]+';margin-left:5px;margin-top:3px"></div><div id="nxtDrawDiv" style="border:1px '+_0to9Color[0]+' solid;background-color:'+_0to9Color[1]+';margin-left:5px;margin-top:5px"></div></td><td align="center" width="70%" valign="top">'+_0to9Tab()+'</td></tr></table></form>';
