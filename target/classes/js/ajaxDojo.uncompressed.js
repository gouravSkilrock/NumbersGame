
var isOnSubmit = true,subUrl="",o_url="",url="",subTarget="",isLoadingFirst=true,elmt ="",_bd="";
var _ev={
o:function(eid){return elmt.getAttribute(eid);}
}
function buttonPressed(event){
elmt=event.srcElement;
//alert(elmt.id+"-"+elmt.targets+"-"+elmt.form+"-"+elmt.form.onsubmit+"-"+elmt.form.action);
subUrl=elmt.form.action;
formValidation=elmt.form.onsubmit;
if(BrowserDetect.browser=="Safari")
event.preventDefault();
isOnSubmit=true;
//alert(elmt.id+"****"+elmt.getAttribute('targets'));
subTarget = elmt.getAttribute('targets');
		subUrl=subUrl+appndParam();
		_valid(formValidation);
		if(_ev.o('onclick')){
		_valid((new Function(_ev.o('onclick'))).toString().replace('}',';return true;')); 
		}
		if(isOnSubmit){
		ajaxReqDojo(subUrl,'',subTarget,true); 
		}
	return false;		
}

function _valid(formValidation){
	var arrValid = formValidation.toString().split('return');
	var arrValidFunc = new Array();
	var count=0;
		if(arrValid.length>2){
			for(var j in arrValid){
				if(j!=0&&j!=arrValid.length-1){
					arrValidFunc[count]=arrValid[j].replace(';','');
					count++;
					
				}
			}
		}
		for(var j in arrValidFunc){
					if(!eval(arrValidFunc[j])){
					isOnSubmit=false;
		}
	}

}

function appndParam(){
var param="?sk=sk";
var arrInpElem = document.getElementsByTagName('input');
var arrSelectElem = document.getElementsByTagName('select');

	for(var i in arrInpElem){
		if(arrInpElem[i]!='undefined'&&arrInpElem[i]!=null&&arrInpElem[i].name!='undefined'&&arrInpElem[i].name!=null&&arrInpElem[i].name!=""&&!arrInpElem[i].name.match('action:')){
			param=param+"&"+arrInpElem[i].name+"="+arrInpElem[i].value;
			}
	}
	for(var i in arrSelectElem){
	if(arrSelectElem[i]!='undefined'&&arrSelectElem[i]!=null&&arrSelectElem[i].name!='undefined'&&arrSelectElem[i].name!=null){
				param=param+"&"+arrSelectElem[i].name+"="+arrSelectElem[i].value;
			}
		}
return param;
}
 
function anchorPressed(event){
	_bd=BrowserDetect.browser;
	elmt=event.srcElement;
	isOnSubmit=true;
	o_url=elmt.href;
	url=o_url;
	targets=_ev.o('targets');
	if(_bd!="Safari"){
	elmt.href="#";
	}else{
	event.preventDefault();
	}
	if(_ev.o('formId')){
	url=url+appndParam();
	_valid(_id.o((_ev.o('formId'))).onsubmit);
	}
	if(_ev.o('onclick')){
	_valid((new Function(_ev.o('onclick'))).toString().replace('}',';return true;')); 
	}
	if(isOnSubmit)
	ajaxReqDojo(url,'',targets,true);
	elmt.href=o_url;
	return false;		
}

function loadDivDojo(id,href){
//alert(id+"in fun"+href)
ajaxReqDojo(href,'',id,false);
}

var initDojo = {

startDojo:function (){
	
	var arrInp = document.getElementsByTagName('input');
	var arrAnchorElem = document.getElementsByTagName('a');
	var arrDivElem = document.getElementsByTagName('div');
	
	for(var i=0,len=arrInp.length;i<len;i++){
		if(arrInp[i].getAttribute('dojoType')&&arrInp[i].getAttribute('dojoType')!=null){
			removeEvents(arrInp[i],buttonPressed);
			addEvents(arrInp[i],buttonPressed);
		
		}
	}
	
	for(var i=0,len=arrAnchorElem.length;i<len;i++){
		if(arrAnchorElem[i].getAttribute('dojoType')&&arrAnchorElem[i].getAttribute('dojoType')!=null){
		
		//alert(arrAnchorElem[i].targets+""+arrAnchorElem[i].href)
			removeEvents(arrAnchorElem[i],anchorPressed);
			addEvents(arrAnchorElem[i],anchorPressed);
		}
	}
	
	if(isLoadingFirst){
		for(var i=0,len=arrDivElem.length;i<len;i++){
			if(arrDivElem[i].getAttribute('dojoType')&&arrDivElem[i].getAttribute('dojoType')!=null){
				var _h = arrDivElem[i].getAttribute('href');
				//alert(arrDivElem[i].id+"***"+_h)
				if(_h!='undefined'&&_h!=''&&_h!=null)
				loadDivDojo(arrDivElem[i].id,_h);
				
			}
		}
	}

return false;	
}
}
function addEvents(item,func){
			
			if (item.addEventListener) {
		        var a = item.addEventListener('click', func, false);
		    }else if (item.attachEvent) {
		        item.attachEvent("onclick", func);
		    }
}
function removeEvents(item,func){

			if (item.removeEventListener) {
			        item.removeEventListener('click', func, false);
			    } else if (item.detachEvent) {
			       item.detachEvent('onclick', func);
			    }
}

/*
	var reqAjaxDojo;
	var fnToCallGlbl;
	var dispDivGlbl;
	var isInitDojoGlbl;
		
		function ajaxReqDojo(url,fnToCall,dispDiv,isInitDojo) {
		
				fnToCallGlbl = fnToCall;
				dispDivGlbl=dispDiv;
				isInitDojoGlbl = isInitDojo;
				reqAjaxDojo =XMLRequestDojo();
				
				_id.i(dispDivGlbl,'Loading.....<img src="'+projectName+'/LMSImages/images/loadingAjax.gif"/>');
				_id.o(dispDivGlbl).style.display="block";
			
						reqAjaxDojo.onreadystatechange = prsStatDojo;
						reqAjaxDojo.open("GET", url, true);
						reqAjaxDojo.setRequestHeader("If-Modified-Since", new Date().getTime());
						reqAjaxDojo.send(null);
				alert(dispDiv+"****"+projectName+"******"+url+"---"+reqAjaxDojo);
		}
		function prsStatDojo() {
		alert("1");
			if (reqAjaxDojo.readyState == 4) { // Complete
				if (reqAjaxDojo.status == 200) {
				alert(dispDivGlbl+"**********"+reqAjaxDojo.responseText);
					_id.i(dispDivGlbl,reqAjaxDojo.responseText);
					_id.o(dispDivGlbl).style.display="block";
					if(isInitDojoGlbl)
					initDojo.startDojo();
					isLoadingFirst=false;
				}
			}else{
			alert("ero");
			}
		}*/

function ajaxReqDojo(url,fnToCall,dispDiv,isInitDojo) {
var _req;
var _resData;
var _result=false;

				var _c="?";
				if(url.indexOf("?")!=-1){_c="&";}
				url=url+_c+new Date().getTime()+parent.frames[0].sesId+Math.random()*1000000;
				
_id.i(dispDiv,'Loading.....<img src="'+projectName+'/LMSImages/images/loadingAjax.gif"/>');
_id.o(dispDiv).style.display="block";
		_req =XMLRequestDojo();
		_req.onreadystatechange = function () {
											if (_req.readyState == 4) { 
												if (_req.status == 200) {
												_id.i(dispDiv,_req.responseText);
												_id.o(dispDiv).style.display="block";
												if(isInitDojo)
													initDojo.startDojo();
													isLoadingFirst=false;
												}
											}
										};
		_req.open("GET", url, true);
		_req.setRequestHeader("If-Modified-Since", new Date().getTime());
		_req.send(null);
}
		function XMLRequestDojo(){
		if (window.XMLHttpRequest)
		return new XMLHttpRequest();
		return new ActiveXObject("Microsoft.XMLHTTP");
		}		
