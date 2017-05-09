function checkDuplicate(frmName,elmnt){
      var allEmpty =true,fileEmp=true;
      var ele = _id.fo(frmName,elmnt);
      for(i=0,l=ele.length; i<l; i++){
          if(ele[i].value!=""){
            allEmpty=false;
            for(j=i+1; j<l; j++){ 
	           	if ((ele[i].value).replace(/-/g,'') == (ele[j].value).replace(/-/g,'')){
	           	_id.i('errorDiv','Please remove duplicate entries','e'); _id.i('allEmpty',''); 
	            return true;
	            }
         	}
         }
      }
    _id.i('errorDiv','');  
   
    if(document.getElementById('uploadFile')!=null){
    fileEmp=_id.v('uploadFile','r');
    }
    if((allEmpty&&(typeof _id.o('allEmpty')!='undefined'))&&(fileEmp)){
    _id.i('allEmpty','Please Enter One Field','e');
    return true;
    }
    _id.i('allEmpty','');
    return false;
}	 

var ticketNbrLen ;
var virnLen;
function checkNum(id){
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

function verifyOrg(orgDiv,orgName,msg){
 
  if(_id.o(orgDiv).value!="-1"){
	  var returnType=confirm(msg+" " + orgName);
	  if(returnType) {
	     var arrVirn=document.getElementsByName('virnCode');
	     arrVirn[0].focus();
	     return true;
	    }else{
	       _id.o(orgDiv).selectedIndex=0;
	       _id.iniFocus(orgDiv);
	       return false;
	   }
  }  
}

function _un(len){
	if(typeof len == 'undefined'){
	_id.i('gamemessage','Please select the Game first','e');
	document.forms[0].gameNbr_Name.selectedIndex=0;
	document.forms[0].gameNbr_Name.focus();
	return false;
	}
	return true;
}  
function isEqualToLenOfBookNum(id){
	var currentVal = _id.v(id);
	if(id.match("virn")){
	len = virnLen;
	if(!_un(len))return false;
	if(_id.v(id).length<len){return false;}
	}else if(id.match("ticket")){
		len = ticketNbrLen;
		if(!_un(len))return false;
			if(currentVal.match("-")){
				if(_id.v(id).length<len+2){return false;}
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

var selectedInd = 0;
var divRep = "";
var _loadImg = '<img src="'+path+'/LMSImages/images/loadingAjax.gif"/>';
function searchPwtResult(url) {
if(url.match("tmpRcptDetail")){
divRep = "tmpReceiptdetail";
_id.i("tmpReceiptdetail",'Please Wait............'+_loadImg);
}else if(url.match("getGameNbrFormat")){
 divRep = "gameNbrFormat";
_id.i("loadingDiv",_loadImg);
}else{
var arr = _id.v("resultList").split("to");
selectedInd=_id.o("resultList").selectedIndex;
url = url+"?start ="+(arr[0].replace(" ","")-1)+"&end="+arr[1].replace(" ","");
_id.i("bottom",'<table width="684" border="1" cellpadding="3" cellspacing="0" bordercolor="#CCCCCC" align="center"><tr align="center"><td colspan="6"><b>Search Results</b></td><td nowrap align="right"><select disabled="true"><option value="">'+_id.v("resultList")+'</select>'+_loadImg+'</td></tr></table>');
divRep = "";
}
//alert(url);
_resp  = _ajaxCall(url);

		if(_resp.result){
		_respData=_resp.data;
	
			//alert(_respData);
			if(divRep=="tmpReceiptdetail"){
			_id.i("tmpReceiptdetail", _respData);
			}else if(divRep=="gameNbrFormat"){
			 _id.o("gameNbrFormat").value = _respData;
			 var arr =  _respData.split(":");
			 ticketNbrLen = parseInt(arr[0])+parseInt(arr[1])+parseInt(arr[2])+parseInt(arr[3]);
			 virnLen = parseInt(arr[5]);
			  _id.i("loadingDiv","");
			}else{
			_id.i("bottom",_respData);
	  	  	_id.o("resultList").selectedIndex=selectedInd;
			}
	
	}

}
function crtTab(txtBx){
	   var counter =1,_tD='<table border="0" width="30%" cellpadding="2" cellspacing="0"><tr><td colspan=2><div id="errorDiv"></div></td></tr>';
	   var vLbl = 'id="saveBOPwtTicketsData_">VIRN Code',tLbl='id="verifyTickets_">Ticket No.';
	   for (var i=0;i<11;i++){
		  _tD=_tD+'<tr>';
		   for(var j=0;j<4;j++){
		   var vInp='<input type="text" id="virnCode'+counter+'" name="virnCode" size="16" onkeydown="return checkVirn(this.id)" onkeyup="return checkVirn(this.id)"/>';
		   var tInp = '<input type="text" id="ticketNbr'+counter+'" name="ticketNumber" size="16" onkeydown="return checkNum(this.id)" onkeyup="return checkNum(this.id)"/></td>';
		   	 _tD=_tD+'<td width="10%" nowrap="nowrap"><label '+((txtBx=="virn")?vLbl:tLbl)+'</label></td><td width="20%">';
			_tD=_tD+((txtBx=="virn")?vInp:tInp);
				counter++;
			}
			_tD=_tD+'</tr>';
		}
		_id.i((txtBx=='virn')?'_virnDiv':'_tktDiv',_tD+'</table>');
	}
parent.frames[0].Version['$Source: /rep/LMS_Mgmt/WebRoot/com/skilrock/lms/web/userMgmt/backOffice/js/pwtCommonFn.uncomp.js,v $'] = '$Id: pwtCommonFn.uncomp.js,v 1.1 2010/04/01 04:22:22 gaurav Exp $';