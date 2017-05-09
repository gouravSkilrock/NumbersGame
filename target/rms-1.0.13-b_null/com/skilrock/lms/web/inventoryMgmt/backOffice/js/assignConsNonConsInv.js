var i18nObj = parent.frames[0].i18Obj;
function changeOwnerType(obj) {
	
	var value = obj.value;
	_id.o('ret_org_list').options.length=1;
	_id.o('agt_org_list').selectedIndex = 0;
	if(value == "AGENT") {		
		_id.non('ret_label');		
		_id.non('ret_org_list');		
	}else if(value == "RETAILER") {
		_id.blk('ret_org_list');		
		_id.blk('ret_label');			
	}
	
}


function createConsTbl(tblId, invType) {
	var prefix = invType+"_";
	var tbl = _id.o(tblId);	
	var rowCount = parseInt(tbl.rows.length, 10);
	
	var row = tbl.insertRow(rowCount);
	var cell = row.insertCell(0);
	var newdiv = document.createElement("div");
	newdiv.setAttribute("id", "new_nonCons_div"+rowCount); 
	
	newdiv.innerHTML = '<table border="0" cellpadding="2" cellspacing="0" width="100%">'			
		+'<tr>'
		+'<td align="right"><label class="label">'+i18nObj.prop('label.inv.name')+'&nbsp;<span class="required">*</span>:</label></td>'
		+'<td><select name="consInvId" id="'+prefix+'inv_id_'+rowCount+'" onfocus="_id.non(\'assign\')" class="option" onchange="fillSelBox(this.value, \''+prefix+'model_'+rowCount+'\', \'model_spec_list\')">'
		+'<option value="-1">--'+i18nObj.prop('msg.pls.select')+'--</option>'
		+(_id.o('cons_list').innerHTML).replace('selected', '')		
		+'</select></td>'
		+'<td align="right"><label id="model_label" style="display:block" class="label">'+i18nObj.prop('label.model.spec.name')+'&nbsp;<span class="required">*</span>:</label></td>'
		+'<td><select name="consModelId" id="'+prefix+'model_'+rowCount+'" onfocus="_id.non(\'assign\')" class="option">'
		+'<option value="-1">--'+i18nObj.prop('msg.pls.select')+'--</option>'
		+'</select></td>'
		+'<td align="right"><label id="model_label" style="display:block" class="label">'+i18nObj.prop('label.count')+'&nbsp;<span class="required">*</span>:</label></td>'
		+'<td><input type="text" name="consQty" maxlength="10" onchange="_id.non(\'assign\')"  onfocus="_id.non(\'assign\');this.style.color = \'black\'" value="" id="'+prefix+'qty_'+rowCount+'"/></td>'
		+'</tr>'
		+'</table>';	
	//alert(newdiv.innerHTML);
	cell.appendChild(newdiv);
	getAgentList();
	return true;	
}

function createReturnInvConsTbl(tblId, invType) {
	var prefix = invType+"_";
	var tbl = _id.o(tblId);	
	var rowCount = parseInt(tbl.rows.length, 10);
	
	var row = tbl.insertRow(rowCount);
	var cell = row.insertCell(0);
	var newdiv = document.createElement("div");
	newdiv.setAttribute("id", "new_nonCons_div"+rowCount); 
	
	newdiv.innerHTML = '<table border="0" cellpadding="2" cellspacing="0" width="100%">'			
		+'<tr>'
		+'<td align="right"><label class="label">'+i18nObj.prop('label.inv.name')+'&nbsp;<span class="required">*</span>:</label></td>'
		+'<td><select name="consInvId" id="'+prefix+'inv_id_'+rowCount+'" onfocus="_id.non(\'assign\')" class="option" onchange="fillSelBox(this.value, \''+prefix+'model_'+rowCount+'\', \'model_spec_list\')">'
		+'<option value="-1">--'+i18nObj.prop('msg.pls.select')+'--</option>'
		+(_id.o('cons_list').innerHTML).replace('selected', '')		
		+'</select></td>'
		+'<td align="right"><label id="model_label" style="display:block" class="label">'+i18nObj.prop('label.model.spec.name')+'&nbsp;<span class="required">*</span>:</label></td>'
		+'<td><select name="consModelId" id="'+prefix+'model_'+rowCount+'" onfocus="_id.non(\'assign\')" class="option">'
		+'<option value="-1">--'+i18nObj.prop('msg.pls.select')+'--</option>'
		+'</select></td>'
		+'<td align="right"><label id="model_label" style="display:block" class="label">'+i18nObj.prop('label.count')+'&nbsp;<span class="required">*</span>:</label></td>'
		+'<td><input type="text" name="consQty" maxlength="10" onchange="_id.non(\'assign\')"  onfocus="_id.non(\'assign\');this.style.color = \'black\'" value="" id="'+prefix+'qty_'+rowCount+'"/></td>'
		+'</tr>'
		+'</table>';	
	//alert(newdiv.innerHTML);
	cell.appendChild(newdiv);
	getAllAgentList();
	return true;	
}


function createNonConsTbl(tblId, invType) {
	var prefix = invType+"_";
	var tbl = _id.o(tblId);	
	var rowCount = parseInt(tbl.rows.length, 10);
	//alert(rowCount);
	var row = tbl.insertRow(rowCount);
	var cell = row.insertCell(0);
	var newdiv = document.createElement("div");
	newdiv.setAttribute("id", "new_nonCons_div_"+rowCount); 
	var tblVar = "txtTbl_"+rowCount;
	newdiv.innerHTML = '<table border="0" id="outTbl_'+rowCount+'" cellpadding="2" name="mTbl" cellspacing="0" width="100%">'					
		+'<tr>'
		+'<td align="right"><label class="label">'+i18nObj.prop('label.inv.name')+'&nbsp;<span class="required">*</span>:</label></td>'
		+'<td><select name="nonConsInvId" id="'+prefix+'inv_id_'+rowCount+'" class="option" onchange="fillSelBox(this.value, \''+prefix+'brand_'+rowCount+'\', \'brand_list\')">'
		+'<option value="-1">--'+i18nObj.prop('msg.pls.select')+'--</option>'
		+(_id.o('nonCons_list').innerHTML).replace('selected', '')		 
		+'</select>'
		+'</td>'							
		+'<td align="right"><label id="brand_label" style="display:block" class="label">'+i18nObj.prop('label.brand.name')+'&nbsp;<span class="required">*</span>:</label></td>'
		+'<td><select name="nonConsBrandId" id="'+prefix+'brand_'+rowCount+'" onfocus="_id.non(\'assign\')" class="option" style="display:block" onchange="fillSelBox(this.value, \''+prefix+'model_'+rowCount+'\', \'model_list\');">'		  
		+'<option value="-1">--'+i18nObj.prop('msg.pls.select')+'--</option>'
		+'</select>'
		+'</td>'							
		+'<td align="right"><label id="model_label" style="display:block" class="label">'+i18nObj.prop('label.model.name')+'&nbsp;<span class="required">*</span>:</label></td>'
		+'<td><select name="nonConsModelId" id="'+prefix+'model_'+rowCount+'" onfocus="_id.non(\'assign\')" class="option">'
		+'<option value="-1">--'+i18nObj.prop('msg.pls.select')+'--</option>'
		+'</select>'								
		+'</td>'
		+'<td align="right"><input type="button" value="'+i18nObj.prop('label.add.txt.field')+'" onclick="addNonConsTxtTbl(\'txtTbl_'+rowCount+'\', \'nonCons\')"/></td>'
		+'</tr>'	
		+'</table>'
		+createTxtFildTbl("txtTbl_"+rowCount, 'name = "txtFields"');
		
	//alert(newdiv.innerHTML);
	cell.appendChild(newdiv);
	return true;

}

function addNonConsTxtTbl(tblId, invType) {
	var prefix = invType+"_";
	var tbl = _id.o(tblId);	
	var rowCount = parseInt(tbl.rows.length, 10);
	
	var row = tbl.insertRow(rowCount);
	var cell = row.insertCell(0);
	cell.colSpan="10";	
	var newdiv = document.createElement("div");
	newdiv.innerHTML = createTxtFildTbl('temp_'+rowCount);
	//alert(newdiv.innerHTML);
	cell.appendChild(newdiv);
	return true;
}


function createTxtFildTbl(tblId, name) {
	var tbl = '<table  border="0" cellpadding="2" '+name+' cellspacing="0" id="'+tblId+'" width="100%">';	
	var tr = '';
	var n=-1,m=-5; 		
	for(var i=0; i<10; i++) {
		n+=1; m+=1;
		if(n%5 == 0){
			tbl= tbl+'<tr>';
		}
		tbl = tbl+'<td> &nbsp;&nbsp;&nbsp;&nbsp;</td><td><input type="text" name="sNo" size="20" onfocus="_id.non(\'assign\')" maxlength="50" value=""/></td>';
		if(m%5==0) {
			tbl= tbl+'</tr>';
		}		
	}
	tbl = tbl+'</table>'
	return 	tbl;
}


function fillSelBox(invValue, updOptId, fetOptId, flag) {	
	//alert(flag+" == "+invValue);
	var valArr = invValue.split("-");	
	invValue = valArr[0];
	//alert(invValue);
	
	var brandObj = _id.o(updOptId);	
	brandObj.options.length=1;	
	var selObj = _id.o(fetOptId);
	var text, value, count=1;
	var brandValueArr;
	for(var i=0,l= selObj.length; i<l; i=i+1) {
		value = selObj.options[i].value;
		brandValueArr = value.split("-");
		text = selObj.options[i].text;
		//alert(text+" = "+ value);
		if(brandValueArr[1] == invValue || (invValue == "" && flag)) {			
			var opt = new Option(text, brandValueArr[0]);
			brandObj.options[count] = opt;
			count = count +1;
			//alert(text+" = "+ value);
		}
		
	}	
	//alert(text+" = "+ value);
}




function  _validateForm(){	

	// organization validation
	var agtName = _id.v('agt_org_list');
	if(agtName==-1) {
		_id.i('err_div', i18nObj.prop('label.select')+" "+tierMap['AGENT']+" "+i18nObj.prop('msg.org.name'), "e");
		return false;
	}
	if(_id.v('owner_type')=='RETAILER' && _id.v('ret_org_list')==-1){
		_id.i('err_div', i18nObj.prop('label.select')+" "+tierMap['RETAILER']+" "+i18nObj.prop('msg.org.name'), "e");
		return false;
		
	}
	
	_id.i('err_div', "");
	
	
	var entryFillFlag = false;
	
	
	
	// Consumable inventory validation	
	var txtValArr = _id.n('consQty');
	var consInvNameArr = _id.n('consInvId');
	var consModelNameArr = _id.n('consModelId');
	var retFlag = true, consFlag = true, consDupFlag = true; 
	var consInpValue = "", tempValue="";
	for(var i = 0, len = txtValArr.length; i<len; i=i+1){
		if((txtValArr[i].value!="") || (consInvNameArr[i].value!=-1) || (consModelNameArr[i].value!=-1)){
			entryFillFlag = true;
			consFlag = true;
			if(consInvNameArr[i].value==-1){
				_id.i('cons_err_div', i18nObj.prop('error.fill.valid.entry'), "e");	
				retFlag = false;
				consFlag = false;
				consInvNameArr[i].style.color = 'red';				
			}else{
				consInvNameArr[i].style.color = '#4284B0';
			}
			
			if(consModelNameArr[i].value==-1){
				_id.i('cons_err_div', i18nObj.prop('error.fill.valid.entry'), "e");	
				retFlag = false;
				consFlag = false;
				consModelNameArr[i].style.color = 'red';				
			}else{
				consModelNameArr[i].style.color = '#4284B0';
			}
			
			if(txtValArr[i].value=="" || parseInt(txtValArr[i].value)==0){			
				_id.i('cons_err_div', i18nObj.prop('error.fill.valid.entry'), "e");	
				retFlag = false;
				consFlag = false;
				txtValArr[i].style.color = 'red';				
			}else{
				txtValArr[i].style.color = 'gray';
			}
			
			
			if(isNaN(txtValArr[i].value)){
				_id.i('cons_err_div', i18nObj.prop('error.fill.valid.entry'), "e");	
				txtValArr[i].style.color = 'red';		
				retFlag = false;
				consFlag = false;
			}
			
			
			tempValue = consInvNameArr[i].value+"-"+consModelNameArr[i].value;
					
			//alert(tempValue+"=="+consFlag);
			if(consFlag){
				if(isDupEntries(tempValue, consInpValue)){
					consDupFlag = false;
					consModelNameArr[i].style.color = 'red';
					consInvNameArr[i].style.color = 'red';
				}else{
					//txtValArr[i].style.color = '#4284B0';
					consModelNameArr[i].style.color = '#4284B0';
					consInvNameArr[i].style.color = '#4284B0';
				}
				
				consInpValue = consInpValue+","+tempValue;
			}
			
			
		}
		
	}
	if(retFlag){
		_id.i('cons_err_div', "");
	}
	
	if(!consDupFlag){
		_id.i('cons_err_div', i18nObj.prop('error.remove.duplicate.entry.inv'), "e");	
	}
	
	
	//alert(retFlag);
	
	
	
	
	// Non Consumable inventory validation
	var nonConsTbl = _id.o("nonCons_tbl");	
	var elms, inpValue;
	_id.i("hiddFldDiv", "");
	
	var nonConsInvIdArr = _id.n('nonConsInvId');
	var nonConsBrandIdArr = _id.n('nonConsBrandId');			
	var nonConsModelIdArr = _id.n('nonConsModelId');	
	var dupCheckTempArr;
	var dupFlag=true;
	for(var i =1, length = parseInt(nonConsTbl.rows.length, 10);i<length; i=i+1 ) {
		
		
		// Hidden Fields created Based On inventory Type 
		elms = _id.o('txtTbl_'+i).getElementsByTagName("input");		
		inpValue="";
		for(var k=0, elmLen = elms.length; k<elmLen; k=k+1){
			if(elms[k].value!=""){
				if(isDupEntries(elms[k].value, inpValue)){
					dupFlag = false;
					elms[k].style.color = 'red';
				}else{
						elms[k].style.color = 'gray';
				}
				inpValue = inpValue+","+elms[k].value;
			}				
		}
		inpValue = inpValue.replace(",","");
		_id.o("hiddFldDiv").innerHTML = _id.o("hiddFldDiv").innerHTML
					+'<input type="hidden" name="serNo" value="'+inpValue+'" />';
		
		// validate Inventory or Brand or Model Name 			
		if(inpValue!="" ||(nonConsInvIdArr[i-1].value!=-1 || nonConsBrandIdArr[i-1].value!=-1 
				|| nonConsModelIdArr[i-1].value!=-1)){
			entryFillFlag = true;
			if((nonConsInvIdArr[i-1].value==-1 || nonConsBrandIdArr[i-1].value==-1 
				|| nonConsModelIdArr[i-1].value==-1)){
				_id.i('nonCons_err_div', i18nObj.prop('error.select.valid.entry.inv'), "e");	
				return false;
			}
		}
		
		if(inpValue=="" && (nonConsInvIdArr[i-1].value!=-1 && nonConsBrandIdArr[i-1].value!=-1 
				&& nonConsModelIdArr[i-1].value!=-1)){
			_id.i('nonCons_err_div', i18nObj.prop('error.enter.atleast.one.sr.inv'), "e");	
			return false;
		}					
	}
	if(!dupFlag){
		_id.i('nonCons_err_div', i18nObj.prop('error.remove.duplicate.entry.inv'), "e");	
	}else{
	
	}
	
	if(!entryFillFlag){
		_id.i('nonCons_err_div', i18nObj.prop('error.fill.valid.entry.inv'), "e");	
	}
	
	
	
	return (retFlag && entryFillFlag && dupFlag && consDupFlag);
}


function isDupEntries(elmsVal, inpValueStr){
	var inpValueArr = inpValueStr.split(',');
	for(var k=0, elmLen = inpValueArr.length; k<elmLen; k=k+1){
		if(elmsVal==inpValueArr[k])
		return true;
	}
	return false;
}

function verifyInv(url){
	if(!_validateForm())
		return false;

	url = url+"?1=1";
	// manage Consuamable inventory parameters 
	var txtValArr = _id.n('consQty');
	var consInvNameArr = _id.n('consInvId');
	var consModelNameArr = _id.n('consModelId');
		
		
	for(var i = 0, len = txtValArr.length; i<len; i=i+1){ 
		if((txtValArr[i].value!="" && !isNaN(txtValArr[i].value)) 
			&& consInvNameArr[i].value!=-1 && consModelNameArr[i].value!=-1){						
				url = url+"&consInvId="+consInvNameArr[i].value+"&consModelId="+consModelNameArr[i].value
					+"&consQty="+txtValArr[i].value;					
		}
	}
	
		
	
	// manage Non Consuamable inventory parameters 
	var nonConsInvIdArr = _id.n('nonConsInvId');
	var nonConsBrandIdArr = _id.n('nonConsBrandId');			
	var nonConsModelIdArr = _id.n('nonConsModelId');	
	var serNoArr = _id.n('serNo');	
	
	if(serNoArr.length == nonConsInvIdArr.length){
		for(var i =0, length = parseInt(nonConsInvIdArr.length, 10);i<length; i=i+1 ) {
			if(serNoArr[i].value!="" && (nonConsInvIdArr[i].value!=-1 && nonConsBrandIdArr[i].value!=-1 
				&& nonConsModelIdArr[i].value!=-1)){
					url = url+"&nonConsInvId="+nonConsInvIdArr[i].value+"&nonConsBrandId="+nonConsBrandIdArr[i].value
						+"&nonConsModelId="+nonConsModelIdArr[i].value+"&serNo="+serNoArr[i].value;
			}		
		}
	}
	
	//alert("2==="+url);

	var resObj = _ajaxCall(url);
	if(resObj.result && resObj.data!=""){
 		var _respData=resObj.data;
 		//alert("=="+_respData+"==");
 		/*if(_respData==":"){  			
 			_id.blk('assign');
 			_id.i('cons_err_div', "You have insufficient inventory to Assign ", "e");
 			
 		}else { */	
 			//alert(_respData);
 			_id.blk('assign');	//vaibhav
 			//_id.non('assign');
 			_id.i('cons_err_div', "");
 			var nonConsTbl = _id.o("nonCons_tbl");	
			var elms;			
			var nonConsModelIdArr = _id.n('nonConsModelId');
 			
 			var resDataArr = _respData.split(":"); 
 					
 			
 			var nonConsInvalidArr = resDataArr[0].split(",");
 			var keyValueArr;
 			var modelId, sno;
 		
 			
 			for(var j=0, jlen=nonConsInvalidArr.length; j<jlen; j++){
 				keyValueArr = nonConsInvalidArr[j].split("="); 	
 				if(keyValueArr.length>1){						
	 				modelId = keyValueArr[0].replace(" ", ""); 				
	 				sno = keyValueArr[1];					
	 				for(var i =1, length = parseInt(nonConsTbl.rows.length, 10);i<length; i=i+1 ) {							
						if(parseInt(nonConsModelIdArr[i-1].value, 10)==parseInt(modelId, 10)){							
							elms = _id.o('txtTbl_'+i).getElementsByTagName("input");
							for(var k=0, elmLen = elms.length; k<elmLen; k=k+1){								
								if(elms[k].value==sno){				
									elms[k].style.color = 'red';
									_id.i('nonCons_err_div', i18nObj.prop('error.you.haveThis.inv.to.assign'), "e");
									_id.non('assign');	
									break;
								}						
							}
						}
									
					} 	// for loop k is finished	
				}// if finished
				
 			} 	// for loop i is finished		 		 
 		 
 	
 	
	 		var txtValArr = _id.n('consQty');	
			var consModelNameArr = _id.n('consModelId');
			
			
			
			var consInvalidArr = resDataArr[1].split(",");
			for(var j=0, jlen=consInvalidArr.length; j<jlen; j++){
				keyValueArr = consInvalidArr[j].split("="); 							
				if(keyValueArr.length>1){	
					modelId = keyValueArr[0].replace(" ", ""); 				
					sno = keyValueArr[1];	
					for(var i = 0, len = txtValArr.length; i<len; i=i+1){ 
						if(txtValArr[i].value==sno && consModelNameArr[i].value==modelId){				
							txtValArr[i].style.color = 'red';
							_id.i('cons_err_div', i18nObj.prop('error.you.have.insuff.inv.to.assign'), "e");
							_id.non('assign');	
							break;					
						}
					}
				}			
				
			} 	// for loop j is finished		 		 
		
	//	} // else is finished
 		if(_respData==":"){ //vaibhav
 			_id.blk('assign');
 			_id.o('cons_err_div').innerHTML='';
 			_id.o('nonCons_err_div').innerHTML='';
 		}
 	}
 	
 	
}



function verifyReturnInv(url){
	
	if(!_validateForm())
		return false;

	// organization validation
	var orgId;
	if(_id.v('owner_type')=='RETAILER') {		
		orgId =_id.v('ret_org_list');
	}else{
		orgId =_id.v('agt_org_list');	
	}
	
	url = url+"?orgId="+orgId;
	// manage Consuamable inventory parameters 
	var txtValArr = _id.n('consQty');
	var consInvNameArr = _id.n('consInvId');
	var consModelNameArr = _id.n('consModelId');
		
		
	for(var i = 0, len = txtValArr.length; i<len; i=i+1){ 
		if((txtValArr[i].value!="" && !isNaN(txtValArr[i].value)) 
			&& consInvNameArr[i].value!=-1 && consModelNameArr[i].value!=-1){						
				url = url+"&consInvId="+consInvNameArr[i].value+"&consModelId="+consModelNameArr[i].value
					+"&consQty="+txtValArr[i].value;					
		}
	}
	
		
	
	// manage Non Consuamable inventory parameters 
	var nonConsInvIdArr = _id.n('nonConsInvId');
	var nonConsBrandIdArr = _id.n('nonConsBrandId');			
	var nonConsModelIdArr = _id.n('nonConsModelId');	
	var serNoArr = _id.n('serNo');	
	
	if(serNoArr.length == nonConsInvIdArr.length){
		for(var i =0, length = parseInt(nonConsInvIdArr.length, 10);i<length; i=i+1 ) {
			if(serNoArr[i].value!="" && (nonConsInvIdArr[i].value!=-1 && nonConsBrandIdArr[i].value!=-1 
				&& nonConsModelIdArr[i].value!=-1)){
					url = url+"&nonConsInvId="+nonConsInvIdArr[i].value+"&nonConsBrandId="+nonConsBrandIdArr[i].value
						+"&nonConsModelId="+nonConsModelIdArr[i].value+"&serNo="+serNoArr[i].value;
			}		
		}
	}
	
	//alert("2==="+url);

	var resObj = _ajaxCall(url);
	if(resObj.result && resObj.data!=""){
 		var _respData=resObj.data;
 	/*	if(_respData==":"){ 
 			_id.i('cons_err_div', "You have insufficient inventory to Return ", "e");
 			_id.blk('assign');
 			
 		}else { */	
 			//alert(_respData);	
 			_id.non('Return');
 			_id.i('cons_err_div', "");
 			var nonConsTbl = _id.o("nonCons_tbl");	
			var elms;			
			var nonConsModelIdArr = _id.n('nonConsModelId');
 			
 			var resDataArr = _respData.split(":"); 
 					
 			
 			var nonConsInvalidArr = resDataArr[0].split(",");
 			var keyValueArr;
 			var modelId, sno;
 		
 			
 			for(var j=0, jlen=nonConsInvalidArr.length; j<jlen; j++){
 				keyValueArr = nonConsInvalidArr[j].split("="); 	
 				if(keyValueArr.length>1){						
	 				modelId = keyValueArr[0].replace(" ", ""); 				
	 				sno = keyValueArr[1];					
	 				for(var i =1, length = parseInt(nonConsTbl.rows.length, 10);i<length; i=i+1 ) {							
						if(parseInt(nonConsModelIdArr[i-1].value, 10)==parseInt(modelId, 10)){							
							elms = _id.o('txtTbl_'+i).getElementsByTagName("input");
							for(var k=0, elmLen = elms.length; k<elmLen; k=k+1){								
								if(elms[k].value==sno){				
									elms[k].style.color = 'red';
									_id.i('nonCons_err_div', i18nObj.prop('error.you.not.haveThis.inv.to.ret'), "e");	
									break;
								}						
							}
						}
									
					} 	// for loop k is finished	
				}// if finished
				
 			} 	// for loop i is finished		 		 
 		 
 	
 	
	 		var txtValArr = _id.n('consQty');	
			var consModelNameArr = _id.n('consModelId');
			
			
			
			var consInvalidArr = resDataArr[1].split(",");
			for(var j=0, jlen=consInvalidArr.length; j<jlen; j++){
				keyValueArr = consInvalidArr[j].split("="); 							
				if(keyValueArr.length>1){	
					modelId = keyValueArr[0].replace(" ", ""); 				
					sno = keyValueArr[1];	
					for(var i = 0, len = txtValArr.length; i<len; i=i+1){ 
						if(txtValArr[i].value==sno && consModelNameArr[i].value==modelId){				
							txtValArr[i].style.color = 'red';
							_id.i('cons_err_div', i18nObj.prop('error.you.haveThis.inv.to.ret'), "e");	
							break;					
						}
					}
				}			
				
			} 	// for loop j is finished		 		 
		
		//} // else is finished
		
		if(_respData==":"){ //vaibhav
 			_id.blk('Return');
 			_id.o('cons_err_div').innerHTML='';
 			_id.o('nonCons_err_div').innerHTML='';
 		}
 		
 	}
 	
 	
}

function getAgentList() {
	var text = _ajaxCall(projectName+"/com/skilrock/lms/web/userMgmt/getAgentNameList.action?orgType=AGENT");
	var firstArr = text.data.split(":");
	var retObj = _id.o("agt_org_list");

	for ( var i = 0; i < firstArr.length - 1; i=i+1) {
		var retNameArray = firstArr[i].split("|");
		var opt = new Option(retNameArray[0].toUpperCase(), retNameArray[1]);
		retObj.options[i + 1] = opt;
	}
}

function getAllAgentList() {
	var text = _ajaxCall(projectName+"/com/skilrock/lms/web/userMgmt/getAllOrgNameList.action?orgType=AGENT");
	var firstArr = text.data.split(":");
	var retObj = _id.o("agt_org_list");

	for ( var i = 0; i < firstArr.length - 1; i=i+1) {
		var retNameArray = firstArr[i].split("|");
		var opt = new Option(retNameArray[0].toUpperCase(), retNameArray[1]);
		retObj.options[i + 1] = opt;
	}
}

parent.frames[0].Version['$Source: /rep/LMS_Mgmt/WebRoot/com/skilrock/lms/web/inventoryMgmt/backOffice/js/assignConsNonConsInv.js,v $'] = '$Id: assignConsNonConsInv.js,v 1.9 2016/10/31 09:47:02 neeraj Exp $';