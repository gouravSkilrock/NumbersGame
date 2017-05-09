
var i18nObj = parent.frames[0].i18Obj;


function initialFocus()
{  
  document.na.supName.focus();
   if(document.na.country.value!=-1)
  {
    retrieveURL('getState.action?country=' + document.na.country.value,'characters');
   }
}

function isST3SupValidation()
{
 
   var isInValid = false;
  if(document.na.supName.value=="")
  {
        isInValid = true;
        document.getElementById("sup").innerHTML = "<font color = 'red'>"+i18nObj.prop('error.pls.enter.name')+"</font>";
             
  }
   else
   {
      var alphaExp = /[a-zA-Z]/;
      if(document.na.supName.value.match(alphaExp))
      {      
        document.getElementById("sup").innerHTML = "";
      }
      else
      {
        document.getElementById("sup").innerHTML = "<font color = 'red'>"+i18nObj.prop('error.name.empty')+"</font>";
        isInValid = true;
      }  
    }
    
     if(document.na.addrLine1.value=="")
     {
         document.getElementById("addr1").innerHTML = "<font color = 'red'>"+i18nObj.prop('error.addline1.empty')+"</font>";
         isInValid = true;      
     }
     else
   {
      var alphaExp = /[a-zA-Z]/;
      if(document.na.addrLine1.value.match(alphaExp))
      {      
        document.getElementById("addr1").innerHTML = "";
      }
      else
      {
        document.getElementById("addr1").innerHTML = "<font color = 'red'>"+i18nObj.prop('msg.inv.addr')+"</font>";
        isInValid = true;
      }  
    }
    
    
    
    
    if(document.na.city.value=="")
     {
         document.getElementById("City").innerHTML = "<font color = 'red'>"+i18nObj.prop('errro.pls.enter.city')+"</font>";
         isInValid = true;      
     }
     
        else
   {
      var alphaExp = /[a-zA-Z]/;
      if(document.na.city.value.match(alphaExp))
      {      
        document.getElementById("City").innerHTML = "";
      }
      else
      {
        document.getElementById("City").innerHTML = "<font color = 'red'>"+i18nObj.prop('msg.blank.city')+"</font>";
        isInValid = true;
      }  
    }
     
    
   if(document.na.country.value==-1)
     {
         document.getElementById("supCountry").innerHTML = "<font color = 'red'>"+i18nObj.prop('msg.blank.country')+"</font>";
         isInValid = true;      
     }
     else
     {
      document.getElementById("supCountry").innerHTML = "";
       if(document.na.state.value==-1)
     {
         document.getElementById("supState").innerHTML = "<font color = 'red'>"+i18nObj.prop('error.state.empty')+"</font>";
         isInValid = true;      
     }
     else
     {
      document.getElementById("supState").innerHTML = "";
     }
     } 
    
   
   if(document.na.pin.value=="")
     {
       
         document.getElementById("Pin").innerHTML = "<font color = 'red'>"+i18nObj.prop('error.pls.enter.pin')+"</font>";
         isInValid = true;      
     }
     else
     {
      var alphaExp = /^[0-9]+$/;
      if(!document.na.pin.value.match(alphaExp))
       {
     
        document.getElementById("Pin").innerHTML = "<font color = 'red'>"+i18nObj.prop('error.pls.enter.crrct.value.pin')+"</font>";
        isInValid = true;
       }
       else
       document.getElementById("Pin").innerHTML = "";
     }
   
   
   
    
    
    if(isInValid){
    
		return false;
		}
				
		return true;
		
}		

	parent.frames[0].Version['$Source: /rep/LMS_Mgmt/WebRoot/com/skilrock/lms/web/scratchService/userMgmt/backOffice/js/supReg.js,v $'] = '$Id: supReg.js,v 1.3 2016/10/31 09:47:05 neeraj Exp $';