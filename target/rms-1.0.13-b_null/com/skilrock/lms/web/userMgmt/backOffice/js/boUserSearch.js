var i18nObj = parent.frames[0].i18Obj;  
 function validateData()
 {
     var flag=true;
     var emailId=document.getElementById('emailId').value;
     var phoneNbr=document.getElementById('phoneNbr').value;
     var emailValidity=ValidateEMail(emailId);
     var phoneNbrValidity=isNumeric(phoneNbr);
    
     if(emailValidity==false)
      {
       document.getElementById('errorDiv').innerHTML = "<font color = 'red'>"+i18nObj.prop('error.email.empty')+"</font>";
       flag=false;
      }else
            document.getElementById('errorDiv').innerHTML = "";
      
      if(phoneNbrValidity==true || phoneNbr=="")
      {
       document.getElementById('phonNbrErrorDiv').innerHTML = "<font color = 'red'>"+i18nObj.prop('error.phno.empty')+"</font>";
       flag=false;
      }  
      else
      document.getElementById('phonNbrErrorDiv').innerHTML ="";
      
     // alert(flag);
      return flag;
          
 
 }
 
  
  var req;
  function verifyEmail(url,email) {
  // alert("hello");
  var emailValidity= ValidateEMail(email);
  // alert("email validity "+emailValidity);
  if(emailValidity==false)
     {
       document.getElementById('submitButton').disabled=true;
       document.getElementById('errorDiv').innerHTML = "<font color = 'red'>"+i18nObj.prop('error.email.empty')+"</font>";
       return false;
     }else
     {
          document.getElementById('submitButton').disabled=false;
          document.getElementById('errorDiv').innerHTML = "";
      }     
   document.getElementById('lodingImage').style.display="block";
 _resp  = _ajaxCall(url);
	if(_resp.result){
             document.getElementById('lodingImage').style.display="none";
		       var verifiedMail= _resp.data;
		           if(verifiedMail=="true")
		           {
		             document.getElementById('errorDiv').innerHTML="";
		             document.getElementById('submitButton').disabled=false;   
		           }    
		      else        
		           {
		            document.getElementById('errorDiv').innerHTML = "<font color = 'red'>"+i18nObj.prop('error.email.already.reg')+"</font>";
		            document.getElementById('submitButton').disabled=true;
		           }
      } else {
        alert(i18nObj.prop('error.problem'));
        }
  }

 
function enableBoxes()
  {
     document.getElementById('emailId').readOnly=false;
     document.getElementById('phoneNbr').readOnly=false;
     document.getElementById('submitButton').disabled=false;
     
     // change the background color
     document.getElementById('phoneNbr').style.backgroundColor="#F9FCCD";
      document.getElementById('emailId').style.backgroundColor="#F9FCCD";
       
  }	
	
 function resetPasswordBO(url,email) {
 var emailValidity= ValidateEMail(email);
 // alert("email validity "+emailValidity);
  if(emailValidity==false)
     {
       document.getElementById('errorDiv').innerHTML = "<font color = 'red'>"+i18nObj.prop('error.email.empty')+"</font>";
       return false;
     }else
     {
          document.getElementById('errorDiv').innerHTML = "";
      }     
  // alert(url);
  document.getElementById('lodingImagePass').style.display="block";
    if (window.XMLHttpRequest) { // Non-IE browsers
      req = new XMLHttpRequest();
      req.onreadystatechange = processStateChangePassword;
      try {
        req.open("GET", url, true);
        req.setRequestHeader("If-Modified-Since", new Date().getTime());
      } catch (e) {
        alert(e);
      }
      req.send(null);
    } else if (window.ActiveXObject) { // IE
      req = new ActiveXObject("Microsoft.XMLHTTP");
      if (req) {
        req.onreadystatechange = processStateChangePassword;
        req.open("GET", url, true);
        req.setRequestHeader("If-Modified-Since", new Date().getTime());
        req.send();
      }
    }
  }	
	
 function processStateChangePassword() {

    if (req.readyState == 4) { // Complete
      if (req.status == 200) { // OK response
       
       var passwordChanged= req.responseText;
       document.getElementById('lodingImagePass').style.display="none";
       // alert(passwordChanged);
           if(passwordChanged=="true")
                      document.getElementById('errorDiv').innerHTML="<font color = 'red'>"+i18nObj.prop('msg.pass.reset.success')+"</font>";
                        
      else  
            document.getElementById('errorDiv').innerHTML = "<font color = 'red'>"+i18nObj.prop('error.some.error.tr.again')+"</font>";
                 			      
      } else {
        alert("Problem: " + req.statusText);
      }
    }
  }



function ValidateEMail(emailStr) {

var ldot = emailStr.indexOf(".");
	var emailPat = /^(.+)@(.+)$/;
	var specialChars = "\\(\\)<>@,;:\\\\\\\"\\.\\[\\]";
	var validChars = "[^\\s" + specialChars + "]";
	var quotedUser = "(\"[^\"]*\")";
	var ipDomainPat = /^\[(\d{1,3})\.(\d{1,3})\.(\d{1,3})\.(\d{1,3})\]$/;
	var atom = validChars + "+";
	var word = "(" + atom + "|" + quotedUser + ")";
	var userPat = new RegExp("^" + word + "(\\." + word + ")*$");
	var domainPat = new RegExp("^" + atom + "(\\." + atom + ")*$");
	var matchArray = emailStr.match(emailPat);
	if (matchArray == null) {
		return false;
	}
	var user = matchArray[1];
	var domain = matchArray[2];
	// alert("user "+user);
	// alert("domain "+domain);
	if (user.match(userPat) == null) {
		return false;
	}
	var IPArray = domain.match(ipDomainPat);
	if (IPArray != null) {
		for (var i = 1; i <= 4; i++) {
			if (IPArray[i] > 255) {
				return false;
			}
		}
		
		return true;
	}
	var domainArray = domain.match(domainPat);
	if (domainArray == null) {
		return false;
	}
	var atomPat = new RegExp(atom, "g");
	var domArr = domain.match(atomPat);
	// alert("domArr "+domArr);
	var len = domArr.length;
	if (domArr[domArr.length - 1].length < 2 || domArr[domArr.length - 1].length > 4) {
		return false;
	}
	if (len < 2) {
		var errStr = i18nObj.prop('error.addr.mis.host')+"!";
		return false;
	}	
	if (isNumcont(emailStr.substring(ldot + 1))) {		
		return false;
	}
	return true;
}


 function isNumeric(value) {
 	var strValidChars = "0123456789";
	var strChar;
	var flag = false;
	var strString = value;
	   // test strString consists of valid characters listed above
	for (i = 0; i < strString.length; i++) {
		strChar = strString.charAt(i);
		if (strValidChars.indexOf(strChar) == -1) {
			flag = true;
			break;
		}
	}
	
	return flag;
}
	
function isNumcont(value) {

	var strValidChars = "0123456789";
	var strChar;
	var strString = value;
	
   // test strString consists of valid characters listed above
	for (i = 0; i < strString.length; i++) {
		strChar = strString.charAt(i);
		if (strValidChars.indexOf(strChar) != -1) {
			return true;
		}
	}
	return false;
}	
parent.frames[0].Version['$Source: /rep/LMS_Mgmt/WebRoot/com/skilrock/lms/web/userMgmt/backOffice/js/boUserSearch.js,v $'] = '$Id: boUserSearch.js,v 1.3 2016/10/31 09:46:23 neeraj Exp $';	