/*var isOnSubmit=true,subUrl="",o_url="",url="",subTarget="",isLoadingFirst=true,elmt="",_bd="";var _ev={o:function(A){return elmt.getAttribute(A);}}
function buttonPressed(A){elmt=A.srcElement;subUrl=elmt.form.action;formValidation=elmt.form.onsubmit;if(BrowserDetect.browser=="Safari")A.preventDefault();isOnSubmit=true;subTarget=elmt.getAttribute('targets');subUrl=subUrl+appndParam();_valid(formValidation);if(_ev.o('onclick')){_valid((new Function(_ev.o('onclick'))).toString().replace('}',';return true;'));}if(isOnSubmit){ajaxReqDojo(subUrl,'',subTarget,true);}return false;}function _valid(A){var B=A.toString().split('return');var C=new Array();var D=0;if(B.length>2){for(var E in B){if(E!=0&&E!=B.length-1){C[D]=B[E].replace(';','');D++;}}}for(var E in C){if(!eval(C[E])){isOnSubmit=false;}}}function appndParam(){var A="?sk=sk";var B=document.getElementsByTagName('input');var C=document.getElementsByTagName('select');for(var D in B){if(B[D]!='undefined'&&B[D]!=null&&B[D].name!='undefined'&&B[D].name!=null&&B[D].name!=""&&!B[D].name.match('action:')){A=A+"&"+B[D].name+"="+B[D].value;}}for(var D in C){if(C[D]!='undefined'&&C[D]!=null&&C[D].name!='undefined'&&C[D].name!=null){A=A+"&"+C[D].name+"="+C[D].value;}}return A;}function anchorPressed(A){_bd=BrowserDetect.browser;elmt=A.srcElement;isOnSubmit=true;o_url=elmt.href;url=o_url;targets=_ev.o('targets');if(_bd!="Safari"){elmt.href="#";}else{A.preventDefault();}if(_ev.o('formId')){url=url+appndParam();_valid(_id.o((_ev.o('formId'))).onsubmit);}if(_ev.o('onclick')){_valid((new Function(_ev.o('onclick'))).toString().replace('}',';return true;'));}if(isOnSubmit)ajaxReqDojo(url,'',targets,true);elmt.href=o_url;return false;}function loadDivDojo(A,B){ajaxReqDojo(B,'',A,false);}var initDojo={startDojo:function (){var A=document.getElementsByTagName('input');var B=document.getElementsByTagName('a');var C=document.getElementsByTagName('div');for(var D=0,len=A.length;D<len;D++){if(A[D].getAttribute('dojoType')&&A[D].getAttribute('dojoType')!=null){removeEvents(A[D],buttonPressed);addEvents(A[D],buttonPressed);}}for(var D=0,len=B.length;D<len;D++){if(B[D].getAttribute('dojoType')&&B[D].getAttribute('dojoType')!=null){removeEvents(B[D],anchorPressed);addEvents(B[D],anchorPressed);}}if(isLoadingFirst){for(var D=0,len=C.length;D<len;D++){if(C[D].getAttribute('dojoType')&&C[D].getAttribute('dojoType')!=null){var G=C[D].getAttribute('href');if(G!='undefined'&&G!=''&&G!=null)loadDivDojo(C[D].id,G);}}}return false;}}
function addEvents(A,B){if(A.addEventListener){var C=A.addEventListener('click',B,false);}else if(A.attachEvent){A.attachEvent("onclick",B);}}function removeEvents(A,B){if(A.removeEventListener){A.removeEventListener('click',B,false);}else if(A.detachEvent){A.detachEvent('onclick',B);}}function ajaxReqDojo(A,B,C,D){var E;var F;var G=false;var H="?";if(A.indexOf("?")!=-1){H="&";}A=A+H+new Date().getTime()+parent.frames[0].sesId+Math.random()*1000000;_id.i(C,'Loading.....<img src="'+projectName+'/LMSImages/images/loadingAjax.gif"/>');_id.o(C).style.display="block";E=XMLRequestDojo();E.onreadystatechange=function (){if(E.readyState==4){if(E.status==200){_id.i(C,E.responseText);_id.o(C).style.display="block";if(D)initDojo.startDojo();isLoadingFirst=false;}}};E.open("GET",A,true);E.setRequestHeader("If-Modified-Since",new Date().getTime());E.send(null);}function XMLRequestDojo(){if(window.XMLHttpRequest)return new XMLHttpRequest();return new ActiveXObject("Microsoft.XMLHTTP");}
*/

var isOnSubmit = true,subUrl="",o_url="",url="",subTarget="",isLoadingFirst=true,elmt ="",_bd="",formId="",fnToCall='';
var _ev={
o:function(eid){return elmt.getAttribute(eid);}
}
function buttonPressed(event){
	if(event.srcElement){
		elmt=event.srcElement;
	}
	if(event.target){
		elmt=event.target;
	}
//alert(elmt.id+"-"+elmt.targets+"-"+elmt.form+"-"+elmt.form.onsubmit+"-"+elmt.name+"***"+elmt.getAttribute('name')+"***"+elmt.getAttribute('notifyTopics'));
if(elmt.name.split(':').length>1){
subUrl=elmt.getAttribute('name').split(':')[1]+".action";
}else{
subUrl=elmt.form.action;
}

if(elmt.getAttribute('formId')){
formId = elmt.getAttribute('formId');
}
if(elmt.getAttribute('notifyTopics')){
fnToCall = elmt.getAttribute('notifyTopics');
}

formValidation=elmt.form.onsubmit;
//alert(BrowserDetect.browser);
if(BrowserDetect.browser=="Safari" || BrowserDetect.browser=="Firefox" || BrowserDetect.browser=="Chrome")
event.preventDefault();
isOnSubmit=true;
//alert(elmt.id+"****"+elmt.getAttribute('targets')+"****"+subUrl+"**"+elmt.formId);
subTarget = elmt.getAttribute('targets');
		subUrl=subUrl+appndParam();
		_valid(formValidation);
		if(_ev.o('onclick')){
		_valid((new Function(_ev.o('onclick'))).toString().replace('}',';return true;')); 
		}
		if(isOnSubmit){
		ajaxReqDojo(subUrl,fnToCall,subTarget,true); 
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
if(formId!=""){
arrInpElem = document.getElementById(formId).getElementsByTagName('input');
arrSelectElem = document.getElementById(formId).getElementsByTagName('select');
}
	for(var i=0,l=arrInpElem.length;i<l;i++){
		if(arrInpElem[i]!='undefined'&&arrInpElem[i]!=null&&arrInpElem[i].name!='undefined'&&arrInpElem[i].name!=null&&arrInpElem[i].name!=""&&!arrInpElem[i].name.match('action:')){
			if(arrInpElem[i].type=="checkbox"||arrInpElem[i].type=="radio"){
				if(arrInpElem[i].checked==true){
				param=param+"&"+arrInpElem[i].name+"="+arrInpElem[i].value;}
			}else{
			param=param+"&"+arrInpElem[i].name+"="+arrInpElem[i].value;
			}
		}
	}
	for(var i=0,l=arrSelectElem.length;i<l;i++){
	if(arrSelectElem[i]!='undefined'&&arrSelectElem[i]!=null&&arrSelectElem[i].name!='undefined'&&arrSelectElem[i].name!=null){
				param=param+"&"+arrSelectElem[i].name+"="+arrSelectElem[i].value;
			}
		}
return param;
}
 
function anchorPressed(event){
	_bd=BrowserDetect.browser;
	if(event.srcElement){
		elmt=event.srcElement;
	}
	if(event.target){
		elmt=event.target;
	}
	isOnSubmit=true;
	o_url=elmt.href;
	url=o_url;
	targets=_ev.o('targets');
	fnToCall = _ev.o('notifyTopics');
	if(_bd == "Safari" || _bd == "Firefox" || _bd == "Chrome"){
		event.preventDefault();
	}else{
		elmt.href="#";
	}
	if(_ev.o('formId')){
	url=url+appndParam();
	_valid(_id.o((_ev.o('formId'))).onsubmit);
	}
	if(_ev.o('onclick')){
	_valid((new Function(_ev.o('onclick'))).toString().replace('}',';return true;')); 
	}
	if(isOnSubmit)
	ajaxReqDojo(url,fnToCall,targets,true);
	elmt.href=o_url;
	return false;		
}

function loadDivDojo(id,href){
//alert(id+"in fun"+href)
ajaxReqDojo(href,fnToCall,id,false);
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

function ajaxReqDojo(url,fnToCall,dispDiv,isInitDojo) {
var _req;
var _resData;
var _result=false;

				var _c="?";
				if(url.indexOf("?")!=-1){_c="&";}
				url=url+_c+new Date().getTime()+parent.frames[0].sesId+Math.random()*1000000;
				//alert('Ajax dojo  \n'+url);
_id.i(dispDiv,'Loading.....<img src="'+projectName+'/LMSImages/images/loadingAjax.gif"/>');
_id.o(dispDiv).style.display="block";
		_req =XMLRequestDojo();
		_req.onreadystatechange = function () {
											if (_req.readyState == 4) { 
												if (_req.status == 200) {
												//alert('Ajax dojo  \n'+_req.responseText);
													if(_req.responseText.match("/com/skilrock/lms/web/loginMgmt/loggedOut.jsp")){
														window.open(projectName+"/com/skilrock/lms/web/loginMgmt/loggedOut.jsp", target="_top");
													}
												_id.i(dispDiv,_req.responseText);
												_id.o(dispDiv).style.display="block";
												commonApp.init($);
												if(isInitDojo)
													initDojo.startDojo();
													isLoadingFirst=false;
												}if(fnToCall!=''){
													eval(fnToCall);
												}if(_req.responseText.indexOf('class="startSortable"')!=-1){
													sortables_init();
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
		
        parent.frames[0].Version['$Source: /rep/LMS_Mgmt/WebRoot/js/ajaxDojo.js,v $'] = '$Id: ajaxDojo.js,v 1.4 2016/10/31 09:45:30 neeraj Exp $';

