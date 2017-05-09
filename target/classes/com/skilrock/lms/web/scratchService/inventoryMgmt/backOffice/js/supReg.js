


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
        document.getElementById("sup").innerHTML = "<font color = 'red'>Please Enter Name</font>";
             
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
        document.getElementById("sup").innerHTML = "<font color = 'red'>Please Enter Valid Name</font>";
        isInValid = true;
      }  
    }
    
     if(document.na.addrLine1.value=="")
     {
         document.getElementById("addr1").innerHTML = "<font color = 'red'>Please Enter Address Line1</font>";
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
        document.getElementById("addr1").innerHTML = "<font color = 'red'>Please Enter Valid Address</font>";
        isInValid = true;
      }  
    }
    
    
    
    
    if(document.na.city.value=="")
     {
         document.getElementById("City").innerHTML = "<font color = 'red'>Please Enter City</font>";
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
        document.getElementById("City").innerHTML = "<font color = 'red'>Please Enter Valid City</font>";
        isInValid = true;
      }  
    }
     
    
   if(document.na.country.value==-1)
     {
         document.getElementById("supCountry").innerHTML = "<font color = 'red'>Please Select Country</font>";
         isInValid = true;      
     }
     else
     {
      document.getElementById("supCountry").innerHTML = "";
       if(document.na.state.value==-1)
     {
         document.getElementById("supState").innerHTML = "<font color = 'red'>Please Select State</font>";
         isInValid = true;      
     }
     else
     {
      document.getElementById("supState").innerHTML = "";
     }
     } 
    
   
   if(document.na.pin.value=="")
     {
       
         document.getElementById("Pin").innerHTML = "<font color = 'red'>Please Enter Pin</font>";
         isInValid = true;      
     }
     else
     {
      var alphaExp = /^[0-9]+$/;
      if(!document.na.pin.value.match(alphaExp))
       {
     
        document.getElementById("Pin").innerHTML = "<font color = 'red'>Please Enter Correct Value For Pin</font>";
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

	parent.frames[0].Version['$Source: /rep/LMS_Mgmt/WebRoot/com/skilrock/lms/web/scratchService/inventoryMgmt/backOffice/js/supReg.js,v $'] = '$Id: supReg.js,v 1.1 2010/04/02 12:12:07 gaurav Exp $';