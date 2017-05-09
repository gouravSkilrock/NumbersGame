var _id = {
o:function(el_id){return document.getElementById(el_id);},
n:function(el_id){return document.getElementsByName(el_id);},
tn:function(el_id){return document.getElementsByTagName(el_id);},
i:function(el_id,msg,e){
if(e)msg="<font color = 'red'>"+msg+"</font>";
this.o(el_id).innerHTML=msg;
},
v:function(el_id,r){if(r)return this.o(el_id).value=="";return this.o(el_id).value;},
iniFocus:function(el_id){this.o(el_id).focus();},
blk:function(el_id){this.o(el_id).style.display="block";},
non:function(el_id){this.o(el_id).style.display="none";},
hid:function(el_id){this.o(el_id).style.visibility="hidden";},
vis:function(el_id){this.o(el_id).style.visibility="visible";},
fo:function(f,ele){return document.forms[f].elements[ele];},
fi:function(f,ele,msg,e){
if(e)msg="<font color = 'red'>"+msg+"</font>";
this.fo(f,ele).innerHTML=msg;
},
fv:function(f,ele,r){if(r)return this.fo(f,ele).value=="";return this.fo(f,ele).value;},
finiFocus:function(f,ele){this.fo(f,ele).focus();}
}

var request;
var selectBox;
function onLoadAjax(url, val) {
//alert(url);
selectBox= val;
_resp  = _ajaxCall(url);
//alert(_resp.result+":"+_resp.data);
	if(_resp.result){
	_respData=_resp.data;
			
		  	  if(selectBox=='avlCredit'){
		  	  	
				if(_respData=="N");
				else {
						if(BrowserDetect.browser=="Firefox") {
							window.parent.top.document.getElementById(selectBox).innerValue=_respData;
						} else{
							parent.frames[0].document.getElementById(selectBox).innerText=_respData;
						}
					}
			  } else {
		  	  
			  	  var newdiv = document.createElement('div'); // This creates a dynamic new div for holding data from ajax response
				  var divId = 'ajaxDiv';
				  newdiv.setAttribute('id',divId);
				  newdiv.style.visibility="hidden";
				  newdiv.innerHTML = _respData;
				  document.body.appendChild(newdiv); 
				  if(selectBox=="game"){
				 		selectBox="supplierName";
				 		newdiv.innerHTML = (_respData).substring((_respData).indexOf('</select')+9);
				  		uploadInventory(_respData);
				  		load();
				  }
				  else {
				  	if(navigator.appName == "Netscape"){
				  		AddOptions(document.getElementsByName("ajaxList")[0]);
				  	} else {	
					  AddOptions(document.getElementById("ajaxList"));
					}
				  }
			}
	}
}


function AddOptions(ajaxList)
{
 	var newSelectbox = document.getElementById(selectBox);
	newSelectbox.options.length=1;
		for(i=0,l=ajaxList.options.length;i<l;i++) {
			if(i!=0){
				var obj = ajaxList[i];
				var opt = new Option(obj.text,obj.value);
				newSelectbox.options[i] = opt;
			}
		}
}

function uploadInventory(responseText){

document.getElementById("game").innerHTML=(responseText).substring(0,(responseText).indexOf('</select>'))+"</select>";
AddOptions(document.getElementById("ajaxList"));
}

var eCode= "",logJsErr = projectName+"/com/skilrock/lms/web/loginMgmt/logJsError.action?message=";
var orgName = "",brNmVer = "",refreshDivLabl="",frm0=parent.frames[0],frm1=parent.frames[1],_bd;
var e1="Error on Page (Error Code : ",e2=")\n Please contact Vendor.";
function checkCache(){
		
		var jspPage="";
		
		if(BrowserDetect){
		_bd=BrowserDetect;
		brNmVer="Browser--"+_bd.browser+" Ver --"+_bd.version+" OS--"+_bd.OS;
		logJsErr = logJsErr+"--"+brNmVer;
		}
		
		if(frm0.document.getElementById("currentLoggedUser"))
		orgName="&agentOrgName="+frm0.document.getElementById("currentLoggedUser").childNodes[0].nodeValue;
		
		if(frm0.document.getElementById("refreshDivLabel")){
		refreshDivLabl = frm0.document.getElementById("refreshDivLabel");
		}
		//alert(frm0.cachedMap.length+"headId---"+document.getElementById("headId").childNodes[0].nodeValue +"*"+frm0.Version.length);
		if(document.getElementById("headId")){
			var obj = document.getElementById("headId").childNodes[0].nodeValue;
			jspPage = obj.split(',')[0].replace('$Id: ','');
			var version = ((obj.split(',')[1]).substring(1,(obj.split(',')[1]).indexOf('/')-4)).replace(/ /g,'');
			//alert("**"+jspPage+"**\n**"+frm0.cachedMap[jspPage]+"**\n**"+version+"**\n**"+obj);
			if(typeof jspPage=="undefined"){
				eCode="V10002";
				//alert(e1+eCode+e2);
				repRef(jspPage,logJsErr+e1+eCode+e2+"File Name-"+jspPage+" Jsp Ver-"+version);
				return;
			}			
			else if(typeof frm0.cachedMap[jspPage]!='undefined'){
					if(version!=frm0.cachedMap[jspPage]){
					eCode="V10002";
					//alert(e1+eCode+e2);
					repRef(jspPage,logJsErr+e1+eCode+e2+"FileName- "+jspPage+"Ver "+version+" Ver in Map  "+frm0.cachedMap[jspPage]);
					return;
					}
				}
			checkCacheJS(jspPage);
		}
		else{
			frm0.uncachedRefresh=frm0.uncachedRefresh+1;
			if(frm0.uncachedRefresh<2){
			frm1.location.reload(true);
			refreshDivLabl.style.display="block";
			return;
			}else{
			eCode="V10004";
			//alert(e1+eCode+e2);
				frm0.ajaxReq(logJsErr+e1+eCode+e2+"**"+orgName,"jsErr");
				frm0.uncachedRefresh = 0;
				frm1.location.replace(projectName+"/com/skilrock/lms/web/loginMgmt/internalErr.action");
				//alert(jsErrMsg);
				return;
			}
		}
}

function repRef(jspPage,errMsg){
	var matched = false,refPgsLen = frm0.refJspPgs.length,cntRef = 0;
				if(frm0.refJspPgs[jspPage]){
						matched=true;
						cntRef=frm0.refJspPgs[jspPage];
				}
				if(matched){
					if(cntRef<1){incRefCount(jspPage);return;
					}else{
					frm0.refJspPgs[jspPage]=""+parseInt(parseInt(frm0.refJspPgs[jspPage])+1);
					//alert(errMsg+orgName+" Accessed "+frm0.refJspPgs[jspPage]+" times after Error");
					frm0.ajaxReq(errMsg+orgName+" Accessed "+frm0.refJspPgs[jspPage]+" times after Error","jsErr");
					}
				}else{
					incRefCount(jspPage);
				}
}

function incRefCount(jspPage){
					frm0.refJspPgs[jspPage]="1";
					frm1.location.reload(true);
					refreshDivLabl.style.display="block";
}


function checkCacheJS(jspPage){
		var keyInMap="",dataToMatch = "",dataInServ = "";
		var curPageJSArr = new Array();
		for(var i in document.getElementsByTagName("script")){
		var jsFileName = document.getElementsByTagName("script")[i].src;
			if(jsFileName&&!jsFileName.match("struts")&&!jsFileName.match("ajaxRequest")&&!jsFileName.match("ajaxDojo")){
				if(jsFileName.indexOf(projectName)!=-1){
				curPageJSArr[jsFileName]=jsFileName.substring(jsFileName.lastIndexOf('/')+1,jsFileName.length);
				}else{
				curPageJSArr[jsFileName]=jsFileName.substring(jsFileName.lastIndexOf('/')+1,jsFileName.length);
				}
			
			}
			
		}

		
		for(var j in curPageJSArr){
		var inVersion = false,verOnPg="";
			for(var i in frm0.Version){
					if(i.match(curPageJSArr[j])){
					inVersion=true;
					keyInMap = i.substring(i.indexOf("com")-1,i.indexOf(".js,v"));
					var ver = frm0.Version[i];
					verOnPg = ver.substring(ver.indexOf('.js,v')+5,ver.indexOf('/')-4).replace(/ /g,'');
					break;
					}
			}
			//alert("JS on serv-"+keyInMap+"\n ver on serv-*"+frm0.cachedMap[keyInMap]+"*\nJS on page-"+j+"\n Ver on page-*"+verOnPg+"*");
			if(!inVersion){
				eCode="V10005";
				refRes=repRef(jspPage,logJsErr+e1+eCode+e2+"File Not in Ver-- FileName on Page  "+curPageJSArr[j]);
				return;
				break;
			}else{
					dataInServ=frm0.cachedMap[keyInMap];
								if(typeof dataInServ=="undefined"){
									eCode="V10003";
									//alert(e1+eCode+e2);
									repRef(jspPage,logJsErr+e1+eCode+e2+"FileName on page "+keyInMap+" Version on Server Not Present- Ver on Page  "+verOnPg);
									return;
									break;
								}
				if(verOnPg!=dataInServ){
							eCode="V10003";
							//alert(e1+eCode+e2);
							repRef(jspPage,logJsErr+e1+eCode+e2+"JS on serv-"+keyInMap+" ver on serv-"+dataInServ+"JS on page-"+j+" Ver on page-"+verOnPg);
							return;
							break;
							}	
				}
			}
}


function _ajaxCall(url,div) {
var _req;
var _resData;
var _result=false;
				var _c="?";
				if(url.indexOf("?")!=-1){_c="&";}
				url=url+_c+new Date().getTime()+parent.frames[0].sesId+Math.random()*1000000;
	
		_req =_XMLRequest();
		_req.onreadystatechange = function () {
											if (_req.readyState == 4) { 
												if (_req.status == 200) {
												_resData=_req.responseText;
												_result=true;
												if(typeof div!='undefined')
												_id.i(div,_resData);
												}
											}
										};
		_req.open("GET", url, false);
		_req.setRequestHeader("If-Modified-Since", new Date().getTime());
		_req.send(null);
		
return {result:_result,data:_req.responseText};
}

function _XMLRequest(){
if (window.XMLHttpRequest)
return new XMLHttpRequest();
return new ActiveXObject("Microsoft.XMLHTTP");
}




function _subValid(subBtnId) {

var divDisp = document.getElementById('subCheck');
 if(typeof subBtnId!='undefined')
  document.getElementById(subBtnId).style.visibility="hidden";
  if(typeof divDisp!='undefined')
  divDisp.innerHTML='Please Wait....<img src="'+projectName+'/LMSImages/images/loadingAjax.gif"/>';
  var returnType=confirm("Do You Want to Proceed");
   if(returnType)    
    { 
      return true;
    }
   else
   {
       if(typeof subBtnId!='undefined')
       document.getElementById(subBtnId).style.visibility="visible";
       divDisp.innerHTML="";
       return false;
   }
   
}
function _subValidAnchor() {
var arrAnchorElem = document.getElementsByTagName('a');
var anchorLen = arrAnchorElem.length;
for(var i=0;i<anchorLen;i++){
		var ele = arrAnchorElem[i];
		if((ele.onclick!=null)&& (typeof ele.onclick!='undefined')){
		var isSubVal = (ele.onclick).toString();
		if((isSubVal.indexOf('_subValidAnchor'))!=-1){
		ele.innerHTML="";
		}
		}
	}
  document.getElementById('subCheck').innerHTML='Please Wait....<img src="'+projectName+'/LMSImages/images/loadingAjax.gif"/>';
return true;

}

