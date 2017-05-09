
	// It contain the details of all desplayed games
var i18nObj = parent.frames[0].i18Obj;
	var gameDetMap = new Object();  
	
	function gameDetails() {
		var url = path+"/com/skilrock/lms/web/scratchService/inventoryMgmt/bo_im_fetchGameDetails_ajax.action";
		var _resp = _ajaxCall(url);
		//alert(_resp.data)
		if(_resp.result && _resp.data!=""){
 			var _respData = _resp.data;
 			var option = _id.o("game_id");
 			
			var gameArr = _respData.split("Nx*");
			//alert("gameArr = "+gameArr.length+" data = "+gameArr);
			var gameWiseMap;
			for(var i=0; i<gameArr.length; i++) {
				// creating map of all details of game
				gameWiseMap = new Object();
				
				var gameDetailsArr = gameArr[i].split(":");
				gameWiseMap['gameId'] = gameDetailsArr[0];
				gameWiseMap['gameNbr'] = gameDetailsArr[1];
				gameWiseMap['pack_nbr_digits'] = gameDetailsArr[3];
				gameWiseMap['book_nbr_digits'] = gameDetailsArr[4];
				gameWiseMap['ticket_nbr_digits'] = gameDetailsArr[5];
				gameWiseMap['game_nbr_digits'] = gameDetailsArr[6];
				//gameWiseMap['game_virn_digits'] = gameDetailsArr[7];						
				
				var gkey = gameDetailsArr[0];//+"-"+gameDetailsArr[1]+"-"+gameDetailsArr[2];						
				gameDetMap[gkey] = gameWiseMap;						
				
				// create game option
				var dvalue = gameDetailsArr[1]+"-"+gameDetailsArr[2];
				var opt = new Option(dvalue, gkey);
				option.options[i+1] = opt;						
			}
			//alert(gameDetMap);
		}		
	}
	
	
	var pkLength=10, bkLength=10;
	function setTextBoxMaxLength(gameId) {
		//document.forms[0].reset();
		_id.o('game_id').value = gameId;
		if(gameId=="-1") {
			document.forms[0].reset();
			_id.i("game_err", "");
			return false;
		}
		pkLength = parseInt((gameDetMap[gameId])['game_nbr_digits'],10) 
				+ parseInt((gameDetMap[gameId])['pack_nbr_digits'], 10)+1;
		bkLength = pkLength	+ parseInt((gameDetMap[gameId])['book_nbr_digits'], 10);
		//alert(pkLength+" === "+bkLength);		
		setMaxLength( _id.n('bookNbr'), bkLength); 
		setMaxLength( _id.n('bookSeriesFrom'), bkLength);
		setMaxLength( _id.n('bookSeriesTo'), bkLength);
		setMaxLength( _id.n('packNbr'), pkLength); 
		setMaxLength( _id.n('packSeriesFrom'), pkLength); 
		setMaxLength( _id.n('packSeriesTo'), pkLength); 
		_id.i("game_err", "");
		
			 	
	}
	
	function setMaxLength(textObjArr, maxLength) {
		for(var i=0,l=textObjArr.length; i<l; i++) {
			textObjArr[i].maxLength = maxLength;
		}
		//alert(textObjArr[0].maxLength+"  === "+maxLength);
	}
	
	
	function addBookRow(tblId, rowType) {
		//alert("inside book no.");
		var tbl = _id.o(tblId);
		var totRows = parseInt(tbl.rows.length, 10);		
		var row = tbl.insertRow(totRows);		
		if(rowType=="book") {
			var cellFir = row.insertCell(0);
			cellFir.appendChild(document.createTextNode("Book Number"));
			var cellSec = row.insertCell(1);
			
			var frdiv = document.createElement("div");
			frdiv.innerHTML = '<input type="text"  name="bookNbr"  size="12" onfocus="this.style.color=\'gray\'" onblur="isValidBookNbr(this)" onkeypress="return isValidEntries(this)" maxLength="'+bkLength+'"  />';
			cellSec.appendChild(frdiv);
			
		}else {
			var cellFir = row.insertCell(0);
			cellFir.appendChild(document.createTextNode("Book Series"));
			
			var cellSec = row.insertCell(1);
			var frdiv = document.createElement("div");
			frdiv.innerHTML = 'From <input type="text"  name="bookSeriesFrom"  size="12" onfocus="this.style.color=\'gray\'" onblur="isValidBookNbr(this)" onkeypress="return isValidEntries(this)" maxLength="'+bkLength+'" />';
			cellSec.appendChild(frdiv);
			
			var cellThi = row.insertCell(2);
			var todiv = document.createElement("div");
			todiv.innerHTML = 'To <input type="text"  name="bookSeriesTo"  size="12" onfocus="this.style.color=\'gray\'" onblur="isValidBookNbr(this)" onkeypress="return isValidEntries(this)" maxLength="'+bkLength+'" />';
			cellThi.appendChild(todiv);
			
		}
	
	}
	
	
	function addPackRow(tblId, rowType) {
		//alert("inside pack number");
		var tbl = _id.o(tblId);
		var totRows = parseInt(tbl.rows.length, 10);		
		var row = tbl.insertRow(totRows);		
		if(rowType=="pack") {
			var cellFir = row.insertCell(0);
			cellFir.appendChild(document.createTextNode("Pack Number"));
			var cellSec = row.insertCell(1);
			
			var frdiv = document.createElement("div");
			frdiv.innerHTML = '<input type="text"  name="packNbr"  size="12" onfocus="this.style.color=\'gray\'" onblur="isValidBookNbr(this)" onkeypress="return isValidEntries(this)" maxLength="'+pkLength+'" />';
			cellSec.appendChild(frdiv);
			
		}else {
			var cellFir = row.insertCell(0);
			cellFir.appendChild(document.createTextNode("Pack Series"));
			
			var cellSec = row.insertCell(1);
			var frdiv = document.createElement("div");
			frdiv.innerHTML = 'From <input type="text"  name="packSeriesFrom"  size="12" onfocus="this.style.color=\'gray\'" onblur="isValidBookNbr(this)" onkeypress="return isValidEntries(this)" maxLength="'+pkLength+'" />';
			cellSec.appendChild(frdiv);
			
			var cellThi = row.insertCell(2);
			var todiv = document.createElement("div");
			todiv.innerHTML = 'To <input type="text"  name="packSeriesTo"  size="12" onfocus="this.style.color=\'gray\'" onblur="isValidBookNbr(this)" onkeypress="return isValidEntries(this)" maxLength="'+pkLength+'" />';
			cellThi.appendChild(todiv);
			
		}
	
	}
	
	

	
	
	function _subTrans(){
		return ( isEmpty() && _subValid('subTrans'));
	}
	
	
	function isValidBookNbr(obj) {
		var gameId = _id.o('game_id').value;	
		if(gameId == "-1") {
			_id.i('game_err', i18nObj.prop('error.pls.slct.the.game.name'), "e");
			return false;
		}
		if(obj.value!="") {
			var gameNbrDigit = (gameDetMap[gameId])['game_nbr_digits'];
			var insValue = obj.value;
			var isDesh = insValue.indexOf("-")!=-1;
			if(!isDesh) {
				obj.value = insValue.substring(0, parseInt(gameNbrDigit, 10))
					+"-"+insValue.substring(parseInt(gameNbrDigit, 10), insValue.length);
			}
		}
	}
	
	
	var isErrMes = false;
	function verifyTextBoxValue(gameNbr, gameNbrDigit, obj, digLength) {
		var err = "game_err";
		var valueGameNbr = obj.value.substring(0, parseInt(gameNbrDigit, 10));
		if(gameNbr != valueGameNbr){
			_id.i(err, i18nObj.prop('error.pls.insert.valid.entriesIn.red.text.boxes'), "ee");
			obj.style.color = "RED";
			isErrMes = true;			
		}else {		
			var insValue = obj.value;
			var isDesh = insValue.indexOf("-")!=-1;			
			var deshCountArr =  insValue.split("-");
			if((isDesh && insValue.length==digLength && deshCountArr.length == 2) 
			 	|| (!isDesh && insValue.length==digLength-1)){
			 	obj.style.color = "GRAY";
				if(!isErrMes) {
					_id.i(err, "");
				}		 		
			} else {
			 	_id.i(err, i18nObj.prop('error.pls.insert.valid.entriesIn.red.text.boxes'), "e");
			 	obj.style.color = "RED";
				isErrMes = true;
			}				
		}	
		
	}
	
	
	function isValidEntries(obj, evt) {
		var gameId = _id.o('game_id').value;	
		if(gameId == "-1") {
			_id.i('game_err', i18nObj.prop('error.pls.slct.the.game.name'), "e");
			return false;
		}
		//alert(obj.value);
		var e = event || evt;
		var charCode = e.which || e.keyCode;
		if ((charCode >= 48 && charCode < 58)) {
			return true;
		}
		if(charCode == 45) {
			//var tValue = obj.value;
			//alert(obj.value)
			//alert((obj.value).indexOf("-"));
			//if((!(tValue=="")) && tValue.indexOf("-")==-1) {
				//alert(obj.maxLength);
			//	obj.maxLength = (obj.maxLength)+1;
				//alert("== max ="+obj.maxLength);
			//}
			return true;
		}
		return false;
	}
	
	
	function isEmpty() {
		var returnFlag = true;
		var gameId = _id.o('game_id').value;	
		if(gameId == "-1") {
			_id.i('game_err', i18nObj.prop('error.pls.slct.the.game.name'), "e");
			return (returnFlag = false);
		}		
		isErrMes = false;
		var gameNbrDigit = (gameDetMap[gameId])['game_nbr_digits'];
		var gameNbr = parseInt((gameDetMap[gameId])['gameNbr'], 10);
				
		// check that the form elements filled completed or not 
		var isAllElementsBlank = true;
		//alert("111111111");
		var bkArr = _id.n('bookNbr'); 
		var bkSerFromArr =  _id.n('bookSeriesFrom');
		var bkSerToArr = _id.n('bookSeriesTo');
		var pkArr = _id.n('packNbr'); 
		var pkSerFromArr = _id.n('packSeriesFrom'); 
		var pkSerToArr = _id.n('packSeriesTo'); 
		
		
		//alert("===========")
		for (var i = 0,l=bkArr.length; i<l; i++) {			
			if (bkArr[i].value != "") { 
				isAllElementsBlank = false;
				verifyTextBoxValue(gameNbr, gameNbrDigit, bkArr[i], bkLength);
			}			
		}
		//alert("2222222222");
		
		var bkSerErr = false;
		for (var i = 0,l=bkSerFromArr.length; i<l; i++) {			
			if (bkSerFromArr[i].value != "" && bkSerToArr[i] !="") {
				isAllElementsBlank = false;
				
				if(bkSerToArr[i].value<=bkSerFromArr[i].value) {
					bkSerToArr[i].style.color = "RED";
					bkSerErr = true;	
					_id.i("bookSerErr", i18nObj.prop('error.from.value.mustbe.less.then.to'), "e");				
				}else {
					bkSerToArr[i].style.color = "GRAY";	
					if(!bkSerErr){
						_id.i("bookSerErr", "");
					}
					verifyTextBoxValue(gameNbr, gameNbrDigit, bkSerFromArr[i], bkLength);
					verifyTextBoxValue(gameNbr, gameNbrDigit, bkSerToArr[i], bkLength);					
				}
			}
		}
		//alert("333333333");
		for (var i = 0,l=pkArr.length; i<l; i++) {			
			if (pkArr[i].value != "") {
				isAllElementsBlank = false;	
				verifyTextBoxValue(gameNbr, gameNbrDigit, pkArr[i], pkLength);	
			}		
		}
		//alert("4444444444");
		var pkSerErr = false;
		for (var i = 0,l=pkSerFromArr.length; i<l; i++) {			
			if (pkSerFromArr[i].value != "" && pkSerToArr[i].value !="") { 
				isAllElementsBlank = false;				
				
				if(pkSerToArr[i].value<=pkSerFromArr[i].value) {
					pkSerToArr[i].style.color = "RED";
					pkSerErr = true;
					_id.i("packSerErr",i18nObj.prop('error.from.value.mustbe.less.then.to') , "e");					
				}else {
					pkSerToArr[i].style.color = "GRAY";	
					if(!bkSerErr){
						_id.i("packSerErr", "");
					}
					verifyTextBoxValue(gameNbr, gameNbrDigit, pkSerFromArr[i], pkLength);
					verifyTextBoxValue(gameNbr, gameNbrDigit, pkSerToArr[i], pkLength);					
				}	
			}			
		}
		//alert("666666666");
		if(_id.o('file').value !="")	isAllElementsBlank = false;	
		
		//alert(isAllElementsBlank);
		if(isAllElementsBlank) {
			_id.i("game_err", i18nObj.prop('error.pls.fill.crect.valuesIn.blank.fields'), "e");
			return false;
		}
		
		if(_id.o('remarks').value == ""){
			_id.i("remarks_err",i18nObj.prop('error.pls.fill.detailsIn.remarks'), "e");
			returnFlag = false;
		}else if(parseInt((_id.o('remarks').value).length, 10)>100){
			_id.i("remarks_err",i18nObj.prop('error.pls.fill.remarks.within.hundred.words'), "e");
			returnFlag = false;
		}else{
			_id.i("remarks_err","");
		}
		
		if(isErrMes) return false;			
		//alert(returnFlag);	
		
		return returnFlag;
	}
	
	
	
	
	
	
		
	
parent.frames[0].Version['$Source: /rep/LMS_Mgmt/WebRoot/com/skilrock/lms/web/scratchService/inventoryMgmt/backOffice/js/updBookNVirnStatus.js,v $'] = '$Id: updBookNVirnStatus.js,v 1.3 2016/10/31 09:46:20 neeraj Exp $';	