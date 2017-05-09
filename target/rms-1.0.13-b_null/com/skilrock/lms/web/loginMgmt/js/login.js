var i18nOb = i18Obj;

function checkMacId(value){
	document.getElementById('macId').value = value;
}

function initialFocus()
	{

	if(window.location.protocol=="http:"){
	if(host!="http"){
	// alert(window.location.host+"in newLogin"+presentProjectName+"
	// url--"+(window.location.host).split(':')[0]);
	window.location.href =host+"://"+(window.location.host).split(':')[0]+port+presentProjectName+"/com/skilrock/lms/web/loginMgmt/newLogin.action";
	}
	}
	 document.login.username.focus();
	 
	}

/*
 * function loadBundles(lang, val) { $.i18n.properties({ name: 'Messages', path:
 * 'bundle/', mode: 'both', language: lang, callback: function() { // val =
 * $.i18n.prop('msg_welcome');
 * $("#msg_welcome").text($.i18n.prop('msg_welcome'));
 * $("#blank_username").text(blank_username);
 * $("#blank_password").text(blank_password); } }); }
 */

function isST3Filled()
{
  
  var isInValid = false;
  if(document.login.username.value=="")
  {
	  var msg = i18nOb.prop('blank_username');
	  // var msg = getValueFromPropertyFile('blank_username');
	  isInValid = true;
      document.getElementById("user").innerHTML = "<span class=\"welcomeNote\" id=\"blank_username\">"+msg+"</span>";

  }
   else
   {
      document.getElementById("user").innerHTML = "";
    }
    
     if(document.login.password.value=="")
     {
    	 // var msg = getValueFromPropertyFile('blank_password');
    	var msg = i18nOb.prop('blank_password');
    	document.getElementById("pass").innerHTML = "<span class=\"welcomeNote\" id=\"blank_password\">"+msg+"</span>";
    	isInValid = true;
     }
     else
     {
      document.getElementById("pass").innerHTML = "";
     }
    if(isInValid){
		return false;
		}
				
		return true;
}		
// function added for Detecting caps key
function capLock(e){
	 kc = e.keyCode?e.keyCode:e.which;
	 sk = e.shiftKey?e.shiftKey:((kc == 16)?true:false);
	 if(((kc >= 65 && kc <= 90) && !sk)||((kc >= 97 && kc <= 122) && sk)){
	  document.getElementById('caps').innerHTML = i18nOb.prop('msg.capslock.on');}
	 else{
	  document.getElementById('caps').innerHTML =" ";}
}

function loginAgain()
{	
	document.forms[0].action=presentProjectName+"/com/skilrock/lms/web/loginMgmt/showLogin.action";
	document.forms[0].target="_top";
	document.forms[0].submit();
}

var BrowserDetect = {
	init: function () {
		this.browser = this.searchString(this.dataBrowser) || "An unknown browser";
		this.version = this.searchVersion(navigator.userAgent)
			|| this.searchVersion(navigator.appVersion)
			|| "an unknown version";
		this.OS = this.searchString(this.dataOS) || "an unknown OS";
	},
	searchString: function (data) {
		for (var i=0;i<data.length;i++)	{
			var dataString = data[i].string;
			var dataProp = data[i].prop;
			this.versionSearchString = data[i].versionSearch || data[i].identity;
			if (dataString) {
				if (dataString.indexOf(data[i].subString) != -1)
					return data[i].identity;
			}
			else if (dataProp)
				return data[i].identity;
		}
	},
	searchVersion: function (dataString) {
		var index = dataString.indexOf(this.versionSearchString);
		if (index == -1) return;
		return parseFloat(dataString.substring(index+this.versionSearchString.length+1));
	},
	dataBrowser: [
		{
			string: navigator.userAgent,
			subString: "Chrome",
			identity: "Chrome"
		},
		{ 	string: navigator.userAgent,
			subString: "OmniWeb",
			versionSearch: "OmniWeb/",
			identity: "OmniWeb"
		},
		{
			string: navigator.vendor,
			subString: "Apple",
			identity: "Safari",
			versionSearch: "Version"
		},
		{
			prop: window.opera,
			identity: "Opera"
		},
		{
			string: navigator.vendor,
			subString: "iCab",
			identity: "iCab"
		},
		{
			string: navigator.vendor,
			subString: "KDE",
			identity: "Konqueror"
		},
		{
			string: navigator.userAgent,
			subString: "Firefox",
			identity: "Firefox"
		},
		{
			string: navigator.vendor,
			subString: "Camino",
			identity: "Camino"
		},
		{		// for newer Netscapes (6+)
			string: navigator.userAgent,
			subString: "Netscape",
			identity: "Netscape"
		},
		{
			string: navigator.userAgent,
			subString: "MSIE",
			identity: "Explorer",
			versionSearch: "MSIE"
		},
		{
			string: navigator.userAgent,
			subString: "Gecko",
			identity: "Mozilla",
			versionSearch: "rv"
		},
		{ 		// for older Netscapes (4-)
			string: navigator.userAgent,
			subString: "Mozilla",
			identity: "Netscape",
			versionSearch: "Mozilla"
		}
	],
	dataOS : [
		{
			string: navigator.platform,
			subString: "Win",
			identity: "Windows"
		},
		{
			string: navigator.platform,
			subString: "Mac",
			identity: "Mac"
		},
		{
			string: navigator.platform,
			subString: "Linux",
			identity: "Linux"
		}
	]

};
// Removed -> parent.frames[0].
var Version = new Array();
Version['$Source: /rep/LMS_Mgmt/WebRoot/com/skilrock/lms/web/loginMgmt/js/login.js,v $'] = '$Id: login.js,v 1.4 2016/10/31 09:46:35 neeraj Exp $';

