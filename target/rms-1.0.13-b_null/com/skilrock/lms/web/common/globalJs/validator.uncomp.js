function Validator(frmname)
{
  
  this.formobj=parent.frames[1].document.forms[frmname];
	if(!this.formobj)
	{
	  alert("Error: couldnot get Form object "+frmname);
		return;
	}
	this.formobj._sfm_form_name=frmname;
	this.aV = add_validation;

	parent.frames[1].document.error_disp_handler = new errorDisplayHandler();
}
function  _errMsg(strError,dispDiv){
	parent.frames[1].document.getElementById(dispDiv).innerHTML="<font color = 'red'>"+strError+"</font>";
	parent.frames[1].document.error_disp_handler.FinalShowMsg(strError,dispDiv);
}
function errorDisplayHandler()
{
  this.all_msgs=new Array();
  this.FinalShowMsg=showMsg;
  this.clear_msgs=clear_msg;
  this.isValid = true;
}
function clear_msg(dispDiv)
{
	//alert(dispDiv+"---"+this.all_msgs[dispDiv]);
    if(typeof (this.all_msgs[dispDiv])=='undefined'){
	  parent.frames[1].document.getElementById(dispDiv).innerHTML="";
	}
}

function showMsg(strError,dispDiv)
{
   this.all_msgs[dispDiv] = strError;
   this.isValid = false;
 }
function add_validation(itemname,descriptor,errstr,dispDiv)
{

  if(!this.formobj)
	{
		alert("Error: The form object is not set properly");
		return;
	}//if
	var itemobj = this.formobj[itemname];
    if(itemobj.length && isNaN(itemobj.selectedIndex) )
    //for radio button; don't do for 'select' item
	{
		itemobj = itemobj[0];
	}	
  if(!itemobj)
	{
		alert("Error: Couldnot get the input object named: "+itemname);
		return;
	}
	//alert("1"+itemname);
	parent.frames[1].document.error_disp_handler.clear_msgs(dispDiv);
	//alert("2");
	validateInput(descriptor,itemobj,errstr,dispDiv);
	
}


function validateInput(strValidateStr,objValue,strError,dispDiv) 
{ 
    var ret = true;
    var epos = strValidateStr.search("="); 
    var  command  = ""; 
    var  cmdvalue = ""; 
    if(epos >= 0) 
    { 
     command  = strValidateStr.substring(0,epos); 
     cmdvalue = strValidateStr.substr(epos+1); 
    } 
    else 
    { 
     command = strValidateStr; 
    } 
    switch(command) 
    { 
        case "req": 
        case "required": 
         { 
		
		   ret = reqInp(objValue,strError,dispDiv)
           break;             
         }//case required 
        case "maxlength": 
        case "maxlen": 
          { 
			 ret = mxLen(objValue,cmdvalue,strError,dispDiv)
             break; 
          }//case maxlen 
        case "minlength": 
        case "minlen": 
           { 
			 ret = mnLen(objValue,cmdvalue,strError,dispDiv)
             break; 
            }//case minlen 
        case "alnum": 
        case "alphanumeric": 
           { 
				ret = inpTyp(objValue,"[^A-Za-z0-9]",strError, 
						objValue.name+": Only alpha-numeric characters allowed ",dispDiv);
				break; 
           }
        case "alnum_s": 
        case "alphanumeric_space": 
           { 
				ret = inpTyp(objValue,"[^A-Za-z0-9\\s]",strError, 
						objValue.name+": Only alpha-numeric characters and space allowed ",dispDiv);
				break; 
           }		   
        case "num": 
        case "numeric": 
           { 
                ret = inpTyp(objValue,"[^0-9]",strError, 
						objValue.name+": Only digits allowed ",dispDiv);
                break;               
           }
        case "dec": 
        case "decimal": 
           { 
           		ret = inpTyp(objValue,"/^[-+]?\d(\.\d{1,3})?$/",strError, 
						objValue.name+": Only numbers allowed ",dispDiv);
                //ret = inpTyp(objValue,"[^0-9\.]",strError, 
				//		objValue.name+": Only numbers allowed ",dispDiv);
                break;               
           }
        case "alphabetic": 
        case "alpha": 
           { 
                ret = inpTyp(objValue,"[^A-Za-z]",strError, 
						objValue.name+": Only alphabetic characters allowed ",dispDiv);
                break; 
           }
        case "alphabetic_space": 
        case "alpha_s": 
           { 
                ret = inpTyp(objValue,"[^A-Za-z\\s]",strError, 
						objValue.name+": Only alphabetic characters and space allowed ",dispDiv);
                break; 
           }
        case "email": 
          { 
			   ret = _email(objValue,strError,dispDiv);
               break; 
          }
        case "lt": 
        case "lessthan": 
         { 
    	      ret = lsThn(objValue,cmdvalue,strError,dispDiv);
              break; 
         }
        case "gt": 
        case "greaterthan": 
         { 
			ret = grtrThn(objValue,cmdvalue,strError,dispDiv);
            break; 
         }//case greaterthan 
        case "regexp": 
         { 
         	ret = regX(objValue,cmdvalue,strError,dispDiv);
           break; 
         }
        case "dontselect": 
         { 
			 ret = dontSel(objValue,cmdvalue,strError,dispDiv)
             break; 
         }
		case "selone_radio":
		{
			ret = selRadio(objValue,strError,dispDiv);
		    break;
		}		 
    }//switch 
	return ret;
}

function reqInp(objValue,strError,dispDiv)
{
 var ret = true;
 var val = objValue.value;
 val = val.replace(/^\s+|\s+$/g,"");//trim
    if(eval(val.length) == 0) 
    { 
       if(!strError || strError.length ==0) 
       { 
         strError = objValue.name + " : Required Field"; 
       }//if 
	   //	alert()
       _errMsg(strError,dispDiv); 
       ret=false; 
    }//if 
return ret;
}

function mxLen(objValue,strMaxLen,strError,dispDiv)
{
 var ret = true;
    if(eval(objValue.value.length) > eval(strMaxLen)) 
    { 
      if(!strError || strError.length ==0) 
      { 
        strError = objValue.name + " : "+ strMaxLen +" characters maximum "; 
      }//if 
      _errMsg(strError,dispDiv); 
      ret = false; 
    }//if 
return ret;
}

function mnLen(objValue,strMinLen,strError,dispDiv)
{
 var ret = true;
    if(eval(objValue.value.length) <  eval(strMinLen)) 
    { 
      if(!strError || strError.length ==0) 
      { 
        strError = objValue.name + " : " + strMinLen + " characters minimum  "; 
      }//if               
      _errMsg(strError,dispDiv); 
      ret = false;   
    }//if 
return ret;
}

function inpTyp(objValue,strRegExp,strError,strDefaultError,dispDiv)
{
   var ret = true;

    var charpos = objValue.value.search(strRegExp); 
    if(objValue.value.length > 0 &&  charpos >= 0) 
    { 
     if(!strError || strError.length ==0) 
      { 
        strError = strDefaultError;
      }//if 
      _errMsg(strError,dispDiv); 
      ret = false; 
    }//if 
 return ret;
}

function _email(objValue,strError,dispDiv)
{
var ret = true;
     if(objValue.value.length > 0 && !validateEmail(objValue.value)	 ) 
     { 
       if(!strError || strError.length ==0) 
       { 
          strError = objValue.name+": Enter a valid Email address "; 
       }//if                                               
       _errMsg(strError,dispDiv); 
       ret = false; 
     }//if 
return ret;
}
function lsThn(objValue,strLessThan,strError,dispDiv)
{
var ret = true;
	  if(isNaN(objValue.value)) 
	  { 
	    _errMsg(objValue.name +": Should be a number ",objValue); 
	    ret = false; 
	  }//if 
	  else
	  if(eval(objValue.value) >=  eval(strLessThan)) 
	  { 
	    if(!strError || strError.length ==0) 
	    { 
	      strError = objValue.name + " : value should be less than "+ strLessThan; 
	    }//if               
	    _errMsg(strError,dispDiv); 
	    ret = false;                 
	   }//if   
return ret;          
}
function grtrThn(objValue,strGreaterThan,strError,dispDiv)
{
var ret = true;
     if(isNaN(objValue.value)) 
     { 
       _errMsg(objValue.name+": Should be a number ",objValue); 
       ret = false; 
     }//if 
	 else
     if(eval(objValue.value) <=  eval(strGreaterThan)) 
      { 
        if(!strError || strError.length ==0) 
        { 
          strError = objValue.name + " : value should be greater than "+ strGreaterThan; 
        }//if               
        _errMsg(strError,dispDiv);  
        ret = false;
      }//if  
return ret;           
}
function regX(objValue,strRegExp,strError,dispDiv)
{
   var ret = true;
    var charpos = objValue.value.search(strRegExp); 
    if(objValue.value.length > 0 &&  charpos >= 0) 
    { 
     if(!strError || strError.length ==0) 
      { 
        strError = strDefaultError;
      }//if 
      _errMsg(strError,dispDiv); 
      ret = false; 
    }//if 
 return ret;
}
function dontSel(objValue,dont_sel_index,strError,dispDiv)
{
var ret = true;
    if(objValue.selectedIndex == null) 
    { 
      _errMsg("ERROR: dontselect command for non-select Item"); 
      ret =  false; 
    } 
    if(objValue.selectedIndex == eval(dont_sel_index)) 
    { 
     if(!strError || strError.length ==0) 
      { 
      strError = objValue.name+": Please Select one option "; 
      }//if                                                               
      _errMsg(strError,dispDiv); 
      ret =  false;                                   
     } 
return ret;
}
function selRadio(objValue,strError,dispDiv)
{
	var objradio = objValue.form.elements[objValue.name];
	var one_selected=false;
	for(var r=0;r < objradio.length;r++)
	{
	  if(objradio[r].checked)
	  {
	  	one_selected=true;
		break;
	  }
	}
	if(false == one_selected)
	{
      if(!strError || strError.length ==0) 
       {
	    strError = "Please select one option from "+objValue.name;
	   }	
	  _errMsg(strError,dispDiv);
	}
return one_selected;
}


function validateEmail(email)
{
    var splitted = email.match("^(.+)@(.+)$");
    if(splitted == null) return false;
    if(splitted[1] != null )
    {
      var regexp_user=/^\"?[\w-_\.]*\"?$/;
      if(splitted[1].match(regexp_user) == null) return false;
    }
    if(splitted[2] != null)
    {
      var regexp_domain=/^[\w-\.]*\.[A-Za-z]{2,4}$/;
      if(splitted[2].match(regexp_domain) == null) 
      {
	    var regexp_ip =/^\[\d{1,3}\.\d{1,3}\.\d{1,3}\.\d{1,3}\]$/;
	    if(splitted[2].match(regexp_ip) == null) return false;
      }// if
      return true;
    }
return false;
}
parent.frames[0].Version['$Source: /rep/LMS_Mgmt/WebRoot/com/skilrock/lms/web/common/globalJs/validator.uncomp.js,v $'] = '$Id: validator.uncomp.js,v 1.1 2010/04/01 04:23:08 gaurav Exp $';