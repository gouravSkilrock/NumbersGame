var i18nObj = parent.frames[0].i18Obj;
var message= i18nObj.prop('msg.right.click.disabled'); // Your no right click message here
var closeWin="1"; // Do you want to close window after message (1 for yes 0 for no)

// JavaScript by Dave Lauderdale
// Published at: www.digi-dl.com
if (document.layers)
	document.captureEvents(Event.KEYDOWN);
function FF(e){
if (navigator.appName == "Microsoft Internet Explorer"){

var e = event || e; 
var charCode = e.which || e.keyCode;
//alert(e.keyCode);
if(charCode==116){
alert(message); 
e.keyCode =123456789;
//alert(e.keyCode);
if(e.keyCode==123456789){
return false;
}
}
}
else{
if(e.which==116){
alert(message); 
return false;
}

}
    
}

function IE(e) 
{//alert("1");
     if (navigator.appName == "Microsoft Internet Explorer" && (event.button == "2" || event.button == "3" ))
     {
          alert(message); if(closeWin == "1") self.close();
          return false;
     }
}
function NS(e) 
{//alert("2");
     if (document.layers || (document.getElementById && !document.all))
     {
          if (e.which == "2" || e.which == "3" || e.which == "116")
          {
               alert(message); if(closeWin == "1") self.close();
               return false;
          }
     }
}
document.onkeydown= FF;document.onmousedown=IE;document.onmouseup=NS;document.oncontextmenu=new Function("return false");
parent.frames[0].Version['$Source: /rep/LMS_Mgmt/WebRoot/com/skilrock/lms/web/accMgmt/backOffice/js/rightClickDisable.js,v $'] = '$Id: rightClickDisable.js,v 1.3 2016/10/31 09:46:22 neeraj Exp $';


