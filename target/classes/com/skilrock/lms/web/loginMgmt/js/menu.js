//** Chrome Drop Down Menu- Author: Skilrock Technologies Pvt. Ltd (http://www.skilrock.com)


var cssdropdown={
disappeardelay: 250, //set delay in miliseconds before menu disappears onmouseout
dropdownindicator: '<img src="'+projectName+'/LMSImages/images/winlogoArrow.gif" border="0" />', //specify full HTML to add to end of each menu item with a drop down menu
enableswipe: 1, //enable swipe effect? 1 for yes, 0 for no
enableiframeshim: 1, //enable "iframe shim" in IE5.5/IE6? (1=yes, 0=no)

//No need to edit beyond here////////////////////////

dropmenuobj: null, asscmenuitem: null, domsupport: document.all || document.getElementById, standardbody: null, iframeshimadded: false, swipetimer: undefined, bottomclip:0,

getposOffset:function(what, offsettype){
	var totaloffset=(offsettype=="left")? what.offsetLeft : what.offsetTop;
	var parentEl=what.offsetParent;
	while (parentEl!=null){
		totaloffset=(offsettype=="left")? totaloffset+parentEl.offsetLeft : totaloffset+parentEl.offsetTop;
		parentEl=parentEl.offsetParent;
	}
	return totaloffset;
},

swipeeffect:function(){
	if (this.bottomclip<parseInt(this.dropmenuobj.offsetHeight)){
		this.bottomclip+=10+(this.bottomclip/10) //unclip drop down menu visibility gradually
		this.dropmenuobj.style.clip="rect(0 auto "+this.bottomclip+"px 0)"
	}
	else
		return
	this.swipetimer=setTimeout("cssdropdown.swipeeffect()", 10)
},

css:function(el, targetclass, action){
	var needle=new RegExp("(^|\\s+)"+targetclass+"($|\\s+)", "ig")
	if (action=="check")
		return needle.test(el.className)
	else if (action=="remove"){
		if(el)
		el.className=el.className.replace(needle, "")
		}
	else if (action=="add" && !needle.test(el.className))
		el.className+=" "+targetclass
},

showhide:function(obj, e){
	this.dropmenuobj.style.left=this.dropmenuobj.style.top="-500px"
	if (this.enableswipe==1){
		if (typeof this.swipetimer!="undefined")
			clearTimeout(this.swipetimer)
		obj.clip="rect(0 auto 0 0)" //hide menu via clipping
		this.bottomclip=0
		this.swipeeffect()
	}
	obj.visibility="visible"
	this.css(this.asscmenuitem, "selected", "add")
},

clearbrowseredge:function(obj, whichedge){
	var edgeoffset=0
	if (whichedge=="rightedge"){
		var windowedge=document.all && !window.opera? this.standardbody.scrollLeft+this.standardbody.clientWidth-15 : window.pageXOffset+window.innerWidth-15
		this.dropmenuobj.contentmeasure=this.dropmenuobj.offsetWidth
		if (windowedge-this.dropmenuobj.x < this.dropmenuobj.contentmeasure)  //move menu to the left?
			edgeoffset=this.dropmenuobj.contentmeasure-obj.offsetWidth
	}
	else{
		var topedge=document.all && !window.opera? this.standardbody.scrollTop : window.pageYOffset
		var windowedge=document.all && !window.opera? this.standardbody.scrollTop+this.standardbody.clientHeight-15 : window.pageYOffset+window.innerHeight-18
		this.dropmenuobj.contentmeasure=this.dropmenuobj.offsetHeight
		if (windowedge-this.dropmenuobj.y < this.dropmenuobj.contentmeasure){ //move up?
			edgeoffset=this.dropmenuobj.contentmeasure+obj.offsetHeight
			if ((this.dropmenuobj.y-topedge)<this.dropmenuobj.contentmeasure) //up no good either?
				edgeoffset=this.dropmenuobj.y+obj.offsetHeight-topedge
		}
	}
	return edgeoffset
},

dropit:function(obj, e, dropmenuID){
	if (this.dropmenuobj!=null) //hide previous menu
		this.hidemenu() //hide menu
	this.clearhidemenu()
	this.dropmenuobj=document.getElementById(dropmenuID) //reference drop down menu
	this.asscmenuitem=obj //reference associated menu item
	this.showhide(this.dropmenuobj.style, e)
	this.dropmenuobj.x=this.getposOffset(obj, "left")
	this.dropmenuobj.y=this.getposOffset(obj, "top")
	this.dropmenuobj.style.left=this.dropmenuobj.x-this.clearbrowseredge(obj, "rightedge")+"px"
	this.dropmenuobj.style.top=this.dropmenuobj.y-this.clearbrowseredge(obj, "bottomedge")+obj.offsetHeight+1+"px"
	this.positionshim() //call iframe shim function
},

positionshim:function(){ //display iframe shim function
	if (this.enableiframeshim && typeof this.shimobject!="undefined"){
		if (this.dropmenuobj.style.visibility=="visible"){
			this.shimobject.style.width=this.dropmenuobj.offsetWidth+"px"
			this.shimobject.style.height=this.dropmenuobj.offsetHeight+"px"
			this.shimobject.style.left=this.dropmenuobj.style.left
			this.shimobject.style.top=this.dropmenuobj.style.top
		}
	this.shimobject.style.display=(this.dropmenuobj.style.visibility=="visible")? "block" : "none"
	}
},

hideshim:function(){
	if (this.enableiframeshim && typeof this.shimobject!="undefined")
		this.shimobject.style.display='none'
},

isContained:function(m, e){
	var e=window.event || e
	var c=e.relatedTarget || ((e.type=="mouseover")? e.fromElement : e.toElement)
	while (c && c!=m)try {c=c.parentNode} catch(e){c=m}
	if (c==m)
		return true
	else
		return false
},

dynamichide:function(m, e){
	if (!this.isContained(m, e)){
		this.delayhidemenu()
	}
},

delayhidemenu:function(){
	this.delayhide=setTimeout("cssdropdown.hidemenu()", this.disappeardelay) //hide menu
},

hidemenu:function(){
	this.css(this.asscmenuitem, "selected", "remove")
	this.dropmenuobj.style.visibility='hidden'
	this.dropmenuobj.style.left=this.dropmenuobj.style.top=0
	this.hideshim()
},

clearhidemenu:function(){
	if (this.delayhide!="undefined")
		clearTimeout(this.delayhide)
},

addEvent:function(target, functionref, tasktype){
	if (target.addEventListener)
		target.addEventListener(tasktype, functionref, false);
	else if (target.attachEvent)
		target.attachEvent('on'+tasktype, function(){return functionref.call(target, window.event)});
},

startchrome:function(){
	if (!this.domsupport)
		return
	this.standardbody=(document.compatMode=="CSS1Compat")? document.documentElement : document.body
	for (var ids=0; ids<arguments.length; ids++){
		var menuitems=document.getElementById(arguments[ids]).getElementsByTagName("a")
		for (var i=0; i<menuitems.length; i++){
			if (menuitems[i].getAttribute("rel")){
				var relvalue=menuitems[i].getAttribute("rel")
				var asscdropdownmenu=document.getElementById(relvalue)
				this.addEvent(asscdropdownmenu, function(){cssdropdown.clearhidemenu()}, "mouseover")
				this.addEvent(asscdropdownmenu, function(e){cssdropdown.dynamichide(this, e)}, "mouseout")
				this.addEvent(asscdropdownmenu, function(){cssdropdown.delayhidemenu()}, "click")
				try{
					menuitems[i].innerHTML=menuitems[i].innerHTML+" "+this.dropdownindicator
				}catch(e){}
				this.addEvent(menuitems[i], function(e){ //show drop down menu when main menu items are mouse over-ed
					if (!cssdropdown.isContained(this, e)){
						var evtobj=window.event || e
						cssdropdown.dropit(this, evtobj, this.getAttribute("rel"))
					}
				}, "mouseover")
				this.addEvent(menuitems[i], function(e){cssdropdown.dynamichide(this, e)}, "mouseout") //hide drop down menu when main menu items are mouse out
				this.addEvent(menuitems[i], function(){cssdropdown.delayhidemenu()}, "click") //hide drop down menu when main menu items are clicked on
			}
		} //end inner for
	} //end outer for
	if (window.createPopup && !window.XmlHttpRequest &&!this.iframeshimadded){ //if IE5.5 to IE6, create iframe for iframe shim technique
		document.write('<IFRAME id="iframeshim"  src="" style="display: none; left: 0; top: 0; z-index: 90; position: absolute; filter: progid:DXImageTransform.Microsoft.Alpha(style=0,opacity=0)" frameBorder="0" scrolling="no"></IFRAME>')
		this.shimobject=document.getElementById("iframeshim") //reference iframe object
		this.iframeshimadded=true
	}
} //end startchrome

}


window.onscroll = function() {
var root = (document.compatMode == "CSS3Compat"?document.documentElement: document.body);
var elem = document.getElementById("chromemenu");
elem.style.left = root.scrollLeft + 0 + "px";
elem.style.top = root.scrollTop + 0 + "px";
};

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
if(parent.frames[0].Version){
parent.frames[0].Version['$Source: /rep/LMS_Mgmt/WebRoot/com/skilrock/lms/web/loginMgmt/js/menu.js,v $'] = '$Id: menu.js,v 1.1 2010/04/01 04:23:09 gaurav Exp $';
}
if(parent.frames[0].document.getElementById("refreshDivLabel")){
parent.frames[0].document.getElementById("refreshDivLabel").style.display="none";
}