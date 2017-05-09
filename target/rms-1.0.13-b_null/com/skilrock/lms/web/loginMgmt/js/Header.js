var i18nObj = parent.frames[0].i18Obj;
var ajaxReqReceived=false;

	var cachedMap = new Array();
	var Version = new Array();
	var refJspPgs = new Array();
	var uncachedRefresh = 0;

	var ownerType="";

	var currentUser="";
	var count=1;		
	
	function tierMap(){
	var tempTierMap = new Array();
	var tierData =lmstierMap.replace("{","").replace("}","").split(",");
	for(var i=0;i<tierData.length;i++){
	tempTierMap[tierData[i].split("=")[0].replace(/ /g,"")]=tierData[i].split("=")[1];
	}
	return tempTierMap;
	}

	var tierArr = tierMap();
	function getMenu(){
	
		var arr = new Array('one','two','four','five','six','seven','three','eight','nine','ten','eleven','twelve','thirteen','fourteen','fifteen','sixteen','seventeen');
		for(var k=0;k<arr.length;k++){
		parent.frames[1].document.getElementById("dropmenu"+k).innerHTML=parent.frames[0].document.getElementById("dropmenu"+k).innerHTML;
		}
	
		for(var i=0;i<arr.length;i++){
		var dropmenu = "dropmenu"+i;
		if(parent.frames[1].document.getElementById(dropmenu)){
			if(parent.frames[1].document.getElementById(dropmenu).innerHTML==""){
				// parent.frames[1].document.getElementById(arr[i]).style.background
				// =
				// "url('"+projectName+"/LMSImages/images/chromebg6-over.gif')";
				// alert(dropmenu);
				if(parent.frames[1].document.getElementById(arr[i]))
				parent.frames[1].document.getElementById(arr[i]).style.display= "none";
				parent.frames[1].document.getElementById(dropmenu).className = "dropmenudivdis";
			
		}
		}
	
		}

	}
	
	function getCookie() {
		Version = new Array();
		var cookie = document.cookie;
		
		if(cookie!=null){
			var cookieArray = cookie.split('; ');
			var currentLoggedUser = "";
			// Parse the cookies: get the stored time
			
			for(var loop=0; loop < cookieArray.length; loop++)
			{
				var nameValue = cookieArray[loop].split('=');
				if(parent.frames[1].BrowserDetect.browser=="Firefox"){
				currentLoggedUser=(parent.frames[0].document.getElementById("currentLoggedUserMoz")).value;
				}else{
				currentLoggedUser=parent.frames[0].document.getElementById("currentLoggedUser").innerText;
				}
				
				if( nameValue[0].toString() == 'LMSCookie' ){
					if( nameValue[1].toUpperCase().replace(/ /g,'').replace(/"/g,'')!=currentLoggedUser.toUpperCase().replace(/ /g,'')){
					
					var jsErrMsg = "Current Logged User--"+currentLoggedUser.toUpperCase()+"--User in Cookie--"+nameValue[1].toString().toUpperCase()+"-Brw-"+parent.frames[1].BrowserDetect.browser+" v "+parent.frames[1].BrowserDetect.version+"&agentOrgName="+currentLoggedUser;

					ajaxReq(projectName+"/com/skilrock/lms/web/loginMgmt/logJsError.action?message="+jsErrMsg,"jsErr");
					
					window.open(""+projectName+"/com/skilrock/lms/web/loginMgmt/multipleLogin.action", target="_top");
					return false;
					}
				}
			}
		}
			
	return true;
	}
	
		var reqAjax;
		var fnToCallGlbl;
		
		function ajaxReq(url,fnToCall) {
				   
				var _c="?";
				if(url.indexOf("?")!=-1){_c="&";}
				if(!url.match('js/ajaxRequest.js'))
				url=url+_c+new Date().getTime()+sesId+Math.random()*1000000;

				fnToCallGlbl = fnToCall;
				reqAjax =XMLRequest2();
						reqAjax.onreadystatechange = prsStat3;
						reqAjax.open("GET", url, true);
						reqAjax.setRequestHeader("If-Modified-Since", new Date().getTime());
						reqAjax.send(null);
					
		}
		
				function prsStat3() {
			// alert(isDraw+"==="+isScratch);
			if (reqAjax.readyState == 4) { // Complete
				if (reqAjax.status == 200) {
				
					if(fnToCallGlbl=="fillUserDetails"){
					fillUserDetails(reqAjax.responseText);
					ajaxReq(projectName+"/js/ajaxRequest.js","");
					}else if(fnToCallGlbl==""){
						ajaxReqReceived=true;
						if(ownerType=='RETAILER'){
							if(isDraw && idDGActive){
								createServiceMenu("dg");
								parent.frames[1].location.replace(projectName+"/com/skilrock/lms/web/drawGames/playMgmt/rpos.action");
							}else{
								//parent.frames[1].location.replace(projectName+"/com/skilrock/lms/web/loginMgmt/mainPageDisplay.action");
								parent.frames[1].location.replace(projectName+"/com/skilrock/lms/web/scratchService/scratchPOS.jsp");
							}
							
							
							
						}else if(ownerType=='BO'){
							parent.frames[1].location.replace(projectName+"/com/skilrock/lms/web/reportsMgmt/graphAndCharts_dashBoardForBo.action?time="+Math.random()*1000000);
						}else{
							parent.frames[1].location.replace(projectName+"/com/skilrock/lms/web/loginMgmt/mainPageDisplay.action");
						}
						document.getElementById("startDivLabel").style.display="none";
						document.getElementById("startDiv").style.display="none";
						
					}
				}
			}
		}
		function XMLRequest2(){
		if (window.XMLHttpRequest)
		return new XMLHttpRequest();
		return new ActiveXObject("Microsoft.XMLHTTP");
		}


	function fillUserDetails(responseData){
	
		var respData = responseData.split("cacheBreakPoint");
		
		var detArr = respData[0].split(":");
		document.getElementById("currentLoggedUser").innerHTML=detArr[0];
		if(document.getElementById("currentLoggedUserDiv") != null)
			document.getElementById("currentLoggedUserDiv").innerHTML=detArr[0];
		document.getElementById("currentLoggedUserMoz").value=detArr[0];
		currentUser= detArr[0];
		ownerType = detArr[2];
		var limit = document.getElementById("limitDiv");
		var avlCred = document.getElementById("avlCredit");
		
		document.getElementById('ExtLimit').style.visibility="hidden";
		document.getElementById('Remain_Days').style.visibility="hidden" ;
		document.getElementById('ExtLimitDiv').style.visibility ="hidden";
		document.getElementById('CredLimitDiv').style.visibility ="hidden";
		document.getElementById('CredLimit').style.visibility="hidden";
		document.getElementById('RemainingDaysDiv').style.visibility ="hidden";
		// document.getElementById('space').style.visibility = "hidden";
		
		// alert(detArr[2]);
		if(detArr[2]!="BO"){
		
			var bal = detArr[1].split("=");
			// alert(detArr[1]+" $$$$$$$$$ "+bal.length+" == "+bal)
			document.getElementById("avlCredit").innerText=bal[0];
			document.getElementById('avlCreditClm').innerHTML = bal[1];
			// document.getElementById('avlCreditUNClm').innerHTML = bal[2];
			// document.getElementById('balLimitCredit').innerHTML =
			// parseFloat(bal[0], 10)-parseFloat(bal[1], 10);
			document.getElementById('balLimitCredit').innerHTML = bal[3];		
			limit.style.display="none";
			avlCred.style.display="none";			
			document.getElementById('limitDivClm').style.display="none";
			document.getElementById('avlCreditClm').style.display="none";
			document.getElementById('limitDivUNClm').style.visibility="visible";
			document.getElementById('clmLink').style.visibility="visible";
			
			// document.getElementById('avlCreditUNClm').style.visibility="visible";
			document.getElementById('balLimitDiv').style.visibility="visible";
			document.getElementById('balLimitCredit').style.visibility="visible";
			// alert(parseInt(bal[4], 10)+" "+parseInt(bal[5], 10) );
			if(parseInt(bal[4], 10)> 0 && parseInt(bal[5], 10)> 0){
				document.getElementById('ExtLimit').innerHTML = bal[4];
				document.getElementById('Remain_Days').innerHTML = bal[5];	
				document.getElementById('pipe').style.visibility ="visible";	
				document.getElementById('ExtLimit').style.visibility="visible";
				document.getElementById('Remain_Days').style.visibility="visible" ;			
				document.getElementById('ExtLimitDiv').style.visibility ="visible";			
				document.getElementById('RemainingDaysDiv').style.visibility ="visible";
				// document.getElementById('space').style.visibility ="visible";
								
				document.getElementById('ExtLimit').title = i18nObj.prop('error.ur.xcl.worth')+bal[4]+"/- "+i18nObj.prop('error.will.expireIn') +bal[5]+i18nObj.prop('error.day.deposit.fund');
				document.getElementById('Remain_Days').title = i18nObj.prop('error.ur.xcl.worth')+bal[4]+"/- "+i18nObj.prop('error.will.expireIn')+bal[5]+i18nObj.prop('error.day.deposit.fund');
				document.getElementById('ExtLimitDiv').title = i18nObj.prop('error.ur.xcl.worth')+bal[4]+"/- "+i18nObj.prop('error.will.expireIn')+bal[5]+i18nObj.prop('error.day.deposit.fund');
				document.getElementById('RemainingDaysDiv').title = i18nObj.prop('error.ur.xcl.worth')+bal[4]+"/- "+i18nObj.prop('error.will.expireIn')+bal[5]+i18nObj.prop('error.day.deposit.fund');
			}
			document.getElementById('CredLimit').innerHTML = bal[6];
			document.getElementById('CredLimitDiv').style.visibility ="visible";
			document.getElementById('CredLimit').style.visibility ="visible";
			document.getElementById('CredLimit').title = ((i18nObj == undefined) ? 'Your Credit Limit Worth ' : i18nObj.prop('error.ur.cl.worth')) +bal[6]+"/-.";
			document.getElementById('CredLimitDiv').title = ((i18nObj == undefined) ? 'Your Credit Limit Worth ' : i18nObj.prop('error.ur.cl.worth'))  +bal[6]+"/-.";
		}else{
			limit.style.display="none";
			avlCred.style.display="none";
			document.getElementById('limitDivClm').style.display="none";
			document.getElementById('avlCreditClm').style.display="none";
			document.getElementById('limitDivUNClm').style.visibility="hidden";
			document.getElementById('avlCreditUNClm').style.visibility="hidden";
			document.getElementById('clmLink').style.visibility="hidden";
			document.getElementById('balLimitDiv').style.visibility="hidden";
			document.getElementById('balLimitCredit').style.visibility="hidden";
			document.getElementById('pipe').style.visibility ="hidden";
		}
		if(ownerType=='RETAILER' && isDraw == 'YES'){
			document.getElementById('rposDiv').style.display="block";
		}
	/*	try{
			var cachedPages = respData[1].replace("{","").replace("}","").split(",");
		}catch (e) {
			window.open(projectName+"/com/skilrock/lms/web/loginMgmt/loggedOut.jsp", target="_top");
			
		}
		for(var i in cachedPages){
			if(typeof cachedPages[i].split("=")[1] != "undefined"
			&& typeof cachedPages[i].split("=")[0] != "undefined"){
						cachedMap[(cachedPages[i].split("=")[0]).replace(/ /g,"")] = (cachedPages[i].split("=")[1]).replace(/ /g,"");
			}

		}*/
		
		createArray(respData[2]);
	}
	
	var serviceMenuArr = new Array();
	var currentService = "";
	function createServiceMenu(serviceName){
	if(currentService!=""){
	document.getElementById(currentService).className="tabnew";
	}
	document.getElementById(serviceName).className="tabnew tabnewClick";
	currentService = serviceName;
	var compArr= serviceMenuArr[serviceName];
	
	var name = new Array();
	var action = new Array();
	var module = new Array();
	var relatedTo = new Array();
	if(compArr){
	for(var i=1;i<compArr.length;i++){
				var spliData = compArr[i].split(';');
				name[i] = spliData[1];
				action[i] =spliData[2];
				relatedTo[i] = spliData[0];
			}
	}				
			
				
				
		var path = projectName;
		var PROD_MGT = "";
		var ADMIN_MGT = "";
		var USER_MGT = "";
		var GAME_MGT = "";
		var DRAW_MGT = "";
		var PLAY_MGT = "";
		var MISC = "";
		var INV_MGT = "";
		var ORDER_MGT = "";
		var ACT_MGT = "";
		var PWT = "";
		var REPORTS = "";
		var ROLE_MGT = "";
		var serName = "";
		var BO_USER_MGT = "";
		var AGENT_USER_MGT = "";
		var RET_USER_MGT = "";
		var ola_Mgmt = "";
		var SLE_MGT = "";
		var IW_PWT = "";
		// var ola_Report = "";
		if(serviceName=='se'){
		serName="/scratchService";
		}else if(serviceName=='dg'){
		serName="/drawGames";
		}else if(serviceName == 'cs'){
		serName="/commercialService";
		}else if(serviceName == 'ipe'){
		serName="/instantPrint";
		}else if(serviceName == 'ola'){
		serName="/ola";
		}else if(serviceName == 'sle'){
		serName="/sportsLottery";
		}else if(serviceName == 'iw'){
		serName="/instantWin";
		}
		var counter ="";
		
		for(var i=0;i<relatedTo.length;i++){
		counter=" "+counter+"*"+relatedTo[i];
		/*
		 * if(relatedTo[i]=="ola_Report"){ ola_Report+='<a
		 * href="'+path+'/com/skilrock/lms/web'+serName+'/reportsMgmt/'+action[i]+'.action"
		 * onclick = " return getCookie()">'+name[i]+'</a>'; }
		 */
		if(ownerType!='RETAILER' && relatedTo[i]=="ola_Mgmt"){
			if(serviceName == 'ola'){
				ola_Mgmt+='<a href="'+path+'/com/skilrock'+serName+'/web/userMgmt/action/'+action[i]+'.action?request_locale='+lang+'" onclick = " return getCookie()">'+name[i]+'</a>';
			}else{
				ola_Mgmt+='<a href="'+path+'/com/skilrock/lms/web'+serName+'/olaMgmt/'+action[i]+'.action?request_locale='+lang+'" onclick = " return getCookie()">'+name[i]+'</a>';
			}
		}
		
		if(relatedTo[i]=="USER_MGT"){
				USER_MGT+='<a href="'+path+'/com/skilrock/lms/web'+serName+'/userMgmt/'+action[i]+'.action?request_locale='+lang+'" onclick = " return getCookie()">'+name[i]+'</a>';
		}
		if(relatedTo[i]=="GAME_MGT"){
		// alert(action[i]);
		GAME_MGT+='<a href="'+path+'/com/skilrock/lms/web'+serName+'/gameMgmt/'+action[i]+'.action?request_locale='+lang+'" onclick = " return getCookie()">'+name[i]+'</a>';
		}
		if(relatedTo[i]=="DRAW_MGT"){
		DRAW_MGT+='<a href="'+path+'/com/skilrock/lms/web'+serName+'/drawMgmt/'+action[i]+'.action?request_locale='+lang+'" onclick = " return getCookie()">'+name[i]+'</a>';
		}
		/*
		 * if(relatedTo[i]=="PLAY_MGT"){ PLAY_MGT+='<a
		 * href="'+path+'/com/skilrock/lms/web'+serName+'/playMgmt/'+action[i]+'.action"
		 * onclick = " return getCookie()">'+name[i]+'</a>'; }
		 */
		if(!(ownerType=='RETAILER' && serviceName=='se' && action[i]!='ret_im_bookWiseInvDet_Menu') && relatedTo[i]=="INV_MGT"){
		INV_MGT+='<a href="'+path+'/com/skilrock/lms/web'+serName+'/inventoryMgmt/'+action[i]+'.action?request_locale='+lang+'" onclick = " return getCookie()">'+name[i]+'</a>';
		
		}
		if(ownerType!='RETAILER' &&  relatedTo[i]=="ORDER_MGT"){
		ORDER_MGT+='<a href="'+path+'/com/skilrock/lms/web'+serName+'/orderMgmt/'+action[i]+'.action?request_locale='+lang+'" onclick = " return getCookie()">'+name[i]+'</a>';
		}
		
		if(!(ownerType=='RETAILER' && serviceName=='ola') && relatedTo[i]=="ACT_MGT"){
			// alert(serviceName);
			if(serviceName == 'ola'){
				ACT_MGT+='<a href="'+path+'/com/skilrock'+serName+'/web/accMgmt/action/'+action[i]+'.action?request_locale='+lang+'" onclick = " return getCookie()">'+name[i]+'</a>';/*
				 * '<a
				 * href="'+path+'/com/skilrock/lms/web'+serName+'/olaMgmt/'+action[i]+'.action"
				 * onclick = "
				 * return
				 * getCookie()">'+name[i]+'</a>';'<a
				 * href="'+path+'/com/skilrock'+serName+'/web/bo/accMgmt/action/'+action[i]+'.action"
				 * onclick = "
				 * return
				 * getCookie()">'+name[i]+'</a>'
				 */
			}else{
				ACT_MGT+='<a href="'+path+'/com/skilrock/lms/web'+serName+'/accMgmt/'+action[i]+'.action?request_locale='+lang+'" onclick = " return getCookie()">'+name[i]+'</a>';
			}		
		}
		
		/*
		 * if(relatedTo[i]=="ACT_MGT"){ ACT_MGT+='<a
		 * href="'+path+'/com/skilrock/lms/web'+serName+'/accMgmt/'+action[i]+'.action"
		 * onclick = " return getCookie()">'+name[i]+'</a>'; }
		 */
		
		if(relatedTo[i]=="PWT"){
		PWT+='<a href="'+path+'/com/skilrock/lms/web'+serName+'/pwtMgmt/'+action[i]+'.action?request_locale='+lang+'" onclick = " return getCookie()">'+name[i]+'</a>';
		
		}
		if(relatedTo[i]=="REPORTS" || relatedTo[i]=="IW_REPORTS"){
			if(serviceName == 'ola'){
				REPORTS+='<a href="'+path+'/com/skilrock'+serName+'/web/reportsMgmt/action/'+action[i]+'.action?request_locale='+lang+'" onclick = " return getCookie()">'+name[i]+'</a>';
			}else{
				REPORTS+='<a href="'+path+'/com/skilrock/lms/web'+serName+'/reportsMgmt/'+action[i]+'.action?request_locale='+lang+'" onclick = " return getCookie()">'+name[i]+'</a>';
			}
		
		
		}
		if(relatedTo[i]=="ROLE_MGT"){
		ROLE_MGT+='<a href="'+path+'/com/skilrock/lms/web'+serName+'/roleMgmt/'+action[i]+'.action?request_locale='+lang+'" onclick = " return getCookie()">'+name[i]+'</a>';
		}
		if(relatedTo[i]=="MISC"){
		MISC+='<a href="'+path+'/com/skilrock/lms/web'+serName+'/userMgmt/'+action[i]+'.action?request_locale='+lang+'" onclick = " return getCookie()">'+name[i]+'</a>';
		}
		if(relatedTo[i]=="BO_USER_MGT"){
		BO_USER_MGT+='<a href="'+path+'/com/skilrock/lms/web'+serName+'/userMgmt/'+action[i]+'.action?request_locale='+lang+'" onclick = " return getCookie()">'+name[i]+'</a>';
			
		}
		if(relatedTo[i]=="AGENT_USER_MGT"){
		AGENT_USER_MGT+='<a href="'+path+'/com/skilrock/lms/web'+serName+'/userMgmt/'+action[i]+'.action?request_locale='+lang+'" onclick = " return getCookie()">'+name[i]+'</a>';
		}
		if(relatedTo[i]=="RET_USER_MGT"){
		RET_USER_MGT+='<a href="'+path+'/com/skilrock/lms/web'+serName+'/userMgmt/'+action[i]+'.action?request_locale='+lang+'" onclick = " return getCookie()">'+name[i]+'</a>';
		}
		if(relatedTo[i]=="PROD_MGT"){
		PROD_MGT+='<a href="'+path+'/com/skilrock/lms/web'+serName+'/productMgmt/'+action[i]+'.action?request_locale='+lang+'" onclick = " return getCookie()">'+name[i]+'</a>';
		}
		if(relatedTo[i]=="SLE_MGT") {
			SLE_MGT+='<a href="'+path+'/com/skilrock/lms/web'+serName+'/gameMgmt/'+action[i]+'.action?request_locale='+lang+'" onclick = " return getCookie()">'+name[i]+'</a>';
		}
		if(relatedTo[i]=="IW_PWT") {
			IW_PWT+='<a href="'+path+'/com/skilrock/lms/web'+serName+'/pwtMgmt/action/'+action[i]+'.action?request_locale='+lang+'" onclick = " return getCookie()">'+name[i]+'</a>';
		}
		}
		// if(ownerType=='BO'){
		// ROLE_MGT+='<a
		// href="'+path+'/com/skilrock/lms/web/drawGames/drawMenu.action"
		// onclick = " return getCookie()">Draw Game Management</a>';
		// }

		/*
		 * GAME_MGT+='<a
		 * href="'+path+'/com/skilrock/lms/web/drawGames/fetchDrawDataMenu.action"
		 * onclick = " return getCookie()">Draw Game </a>'; GAME_MGT+='<a
		 * href="'+path+'/com/skilrock/lms/web/drawGames/trackDrawTktMenu.action"
		 * onclick = " return getCookie()">Track Ticket </a>'; GAME_MGT+='<a
		 * href="'+path+'/com/skilrock/lms/web/drawGames/drawMenu.action?invoker=ManualEntry"
		 * onclick = " return getCookie()">Manual Winning Entry </a>';
		 * GAME_MGT+='<a
		 * href="'+path+'/com/skilrock/lms/web/drawGames/drawMenu.action?invoker=ManualDeclare"
		 * onclick = " return getCookie()">Manual Declaration Entry </a>';
		 * GAME_MGT+='<a
		 * href="'+path+'/com/skilrock/lms/web/drawGames/drawPerformMenu.action"
		 * onclick = " return getCookie()">Claim Conditions </a>';
		 */
		// USER_MGT+='<a
		// href="'+path+'/com/skilrock/lms/web/reportsMgmt/bo_rep_cashChqAdv_Menu.action"
		// onclick = " return getCookie()">Activity rep </a>';

		if(ownerType=='AGENT'){
		AGENT_USER_MGT=AGENT_USER_MGT+ROLE_MGT;
		}if(ownerType=='BO'){
		
		if('bomaster'==parent.frames[0].document.getElementById("currentLoggedUser").textContent && serviceName=='mgmt'){
		ROLE_MGT+='<a href="'+path+'/com/skilrock/lms/web/userMgmt/bo_um_editBoMasPriv_Menu.action?request_locale='+lang+'" onclick = " return getCookie()">'+(i18nObj == undefined ? "Edit BO Master Priviledges" : i18nObj.prop('menu.edit.bo.priv'))+'</a>';
		// REPORTS+='<a		
		// href="'+path+'/com/skilrock/lms/web/reportsMgmt/bo_rep_soldBookEntry_Menu.action"
		// onclick = " return getCookie()">Retailer Sold Book Entry Report</a>';
		}
		BO_USER_MGT=ROLE_MGT+BO_USER_MGT;
		}
		
		parent.frames[0].document.getElementById("dropmenu0").innerHTML = USER_MGT;
		parent.frames[0].document.getElementById("dropmenu1").innerHTML = GAME_MGT;
		parent.frames[0].document.getElementById("dropmenu2").innerHTML = ORDER_MGT;
		parent.frames[0].document.getElementById("dropmenu3").innerHTML = ACT_MGT;
		parent.frames[0].document.getElementById("dropmenu4").innerHTML = PWT;
		parent.frames[0].document.getElementById("dropmenu5").innerHTML = REPORTS;
		parent.frames[0].document.getElementById("dropmenu6").innerHTML = INV_MGT;
		parent.frames[0].document.getElementById("dropmenu7").innerHTML = MISC;
		parent.frames[0].document.getElementById("dropmenu8").innerHTML = BO_USER_MGT;
		parent.frames[0].document.getElementById("dropmenu9").innerHTML = AGENT_USER_MGT;
		parent.frames[0].document.getElementById("dropmenu10").innerHTML = RET_USER_MGT;
		parent.frames[0].document.getElementById("dropmenu11").innerHTML = DRAW_MGT;
		parent.frames[0].document.getElementById("dropmenu12").innerHTML = ADMIN_MGT;
		parent.frames[0].document.getElementById("dropmenu13").innerHTML = PROD_MGT;
		parent.frames[0].document.getElementById("dropmenu14").innerHTML = ola_Mgmt;
		parent.frames[0].document.getElementById("dropmenu15").innerHTML = SLE_MGT;
		parent.frames[0].document.getElementById("dropmenu16").innerHTML = IW_PWT;
		// parent.frames[0].document.getElementById("dropmenu15").innerHTML =
		// ola_Report;
	}
	function createArray(responseData){
	
	var tabsArr = new Array();
	var compArr= new Array();
	var serviceTab ="";
	var firstService;
		tabsArr = responseData.split('TABS-');
		
		for(var j=1;j<tabsArr.length;j++){
			compArr = tabsArr[j].split('RT-');
			
			serviceMenuArr[compArr[0].split("-")[1].toLowerCase()]=compArr;
			
			if(j==1){firstService=compArr[0].split("-")[1].toLowerCase();}
			if((j+1) == tabsArr.length){
				serviceTab=serviceTab+'<div class="tabnew lastTab" id="'+compArr[0].split("-")[1].toLowerCase()+'"> <a href="#" onclick="'+compArr[0].split("-")[1].toLowerCase()+'Click()">'+compArr[0].split("-")[0]+'</a></div>';
			}else{
				serviceTab=serviceTab+'<div class="tabnew" id="'+compArr[0].split("-")[1].toLowerCase()+'"> <a href="#" onclick="'+compArr[0].split("-")[1].toLowerCase()+'Click()">'+compArr[0].split("-")[0]+'</a></div>';	
			}
		}
		document.getElementById('services').innerHTML=serviceTab;
		createServiceMenu(firstService);
	
	}

var isDraw, isScratch;
function mgmtClick(){
	createServiceMenu("mgmt");	
	if(ownerType=='BO')
		parent.frames[1].location.replace(projectName+"/com/skilrock/lms/web/reportsMgmt/graphAndCharts_dashBoardForBo.action?request_locale="+lang+"&time="+Math.random()*1000000);
	else
		parent.frames[1].location.replace(projectName+"/com/skilrock/lms/web/loginMgmt/mainPageDisplay.action?request_locale='+lang+'");
		
}
function ipeClick(){
	createServiceMenu("ipe");	
	parent.frames[1].location.replace(projectName+"/com/skilrock/lms/web/loginMgmt/mainPageDisplay.action?request_locale='+lang+'");
}


function firstCallFunc(){
if(count==1){
count++;
if(currentUser=="bomaster"){
		document.getElementById("login").innerHTML = _commonAjaxFunction(projectName+"/com/skilrock/lms/web/loginMgmt/fetchUser.action?request_locale='+lang+'");
}
}
}

function confirmLogin(){
var name = document.getElementById("username").value
if(name!=-1){
var returnType = confirm(i18nObj.prop('error.you.want.login.as') +name.toUpperCase());
	if (returnType) {
		window.open(""+projectName+"/com/skilrock/lms/web/loginMgmt/LoginBoMaster.action?username="+name, target="_top");
	} else {
	document.getElementById("username").value=-1;
		return false;
	}
	}
}
	// done by Arun Upadhyay for Common ajax call in whole javascript
function _commonAjaxFunction(passedUrl) {
	var MAXIMUM_WAITING_TIME = 3 * 60 * 1000;
	var xhReq = new createXMLHttpRequest();
				var _c="?";
				if(passedUrl.indexOf("?")!=-1){_c="&";}
				passedUrl=passedUrl+_c+new Date().getTime()+parent.frames[0].sesId+Math.random()*1000000;
	
	xhReq.open("GET", passedUrl, false);
	var requestTimer = setTimeout(function () {
			xhReq.abort();
			_commonAjaxFunction(passedUrl);
		}, MAXIMUM_WAITING_TIME);
		
	var serverResponse;
	xhReq.onreadystatechange = function () {
			if (xhReq.readyState != 4 || xhReq.status != 200) { return;	}
			clearTimeout(requestTimer);			
			serverResponse = xhReq.responseText;
			return;
	};
	
	xhReq.setRequestHeader("If-Modified-Since", new Date().getTime());
	xhReq.send(null);
	if (xhReq.readyState != 4 || xhReq.status != 200) {
		return;
	}
	clearTimeout(requestTimer);			
	serverResponse = xhReq.responseText;
	 // alert(serverResponse);
	return serverResponse;
}

function createXMLHttpRequest() {
	try {	return new XMLHttpRequest();	}	catch (e) {	}
	try {	return new ActiveXObject("Msxml2.XMLHTTP");	}	catch (e) {	}
	try {	return new ActiveXObject("Microsoft.XMLHTTP");	}	catch (e) {	}
	alert(i18nObj.prop('error.xmlhttp.not.support'));
	return null;
}

if (window.addEventListener) {
    window.addEventListener ("message", receive, false);        
}
else {
    if (window.attachEvent) {
        window.attachEvent("onmessage",receive, false);
    }
}
function receive(event){
	if(event.data == 'getBalance'){
		getBalance(event);
	} else if(event.data == 'updateBalance'){
		updateBalance(projectName+"/com/skilrock/lms/web/loginMgmt/updateAvlCreditAmt.action");
	}
}
function getBalance(event){
	event.source.postMessage(parseFloat($('#balLimitCredit').html()),event.origin);
}

function updateBalance(url, isClm) {
	// alert(url);
	// alert(document.getElementById('avlCreditClm').innerHTML);
	if(parseInt(document.getElementById('avlCreditClm').innerHTML)==0 && isClm) {
		alert(i18nObj.prop('error.claim.bal.not.avail'));
		return true;
	}
	// alert(url);
	var _resp  = _commonAjaxFunction(url);

	if(_resp.match("/com/skilrock/lms/web/loginMgmt/loggedOut.jsp"))
		window.open(projectName+"/com/skilrock/lms/web/loginMgmt/loggedOut.jsp", target="_top");
	
	var bal=_resp.split("=");
	// alert(bal);
	if(bal=="AlreadyUpdated"){
		alert(i18nObj.prop('error.already.update'));
		return;
	}
	
	if(bal=="" && bal.length<3){
		alert(i18nObj.prop('error.problemBal.update'));
	}
	else{
			document.getElementById('avlCreditClm').innerHTML=bal[1];
			// document.getElementById('avlCreditUNClm').innerHTML = bal[2];
			document.getElementById('balLimitCredit').innerHTML = bal[3];
			if(document.getElementById("balLimitCredit").style.color=="#FFE898"){
				// document.getElementById('avlCreditUNClm').style.color =
				// 'white';
				document.getElementById('balLimitCredit').style.color = 'white';
			}else {
				// document.getElementById('avlCreditUNClm').style.color =
				// '#FFE898';
				document.getElementById('balLimitCredit').style.color = '#FFE898';
			}
	}	
	// alert(document.getElementById('avlCreditClm').innerHTML);
}	



if(parent.frames[0].Version){
	parent.frames[0].Version['$Source: /rep/LMS_Mgmt/WebRoot/com/skilrock/lms/web/loginMgmt/js/Header.js,v $'] = '$Id: Header.js,v 1.4 2016/11/01 05:52:06 neeraj Exp $';
	}
















