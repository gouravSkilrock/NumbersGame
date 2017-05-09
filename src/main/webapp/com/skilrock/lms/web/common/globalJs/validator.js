var i18nObj = parent.frames[0].i18Obj;

function Validator(frmname)
{
    this.formobj=parent.frames[1].document.forms[frmname];
	if(!this.formobj)
	{
	  alert(parent.frames[0].i18Obj.prop('error.couldnot.get.frm.obj') +frmname);
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
	// alert(dispDiv+"---"+this.all_msgs[dispDiv]);
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
		alert(parent.frames[0].i18Obj.prop('error.frm.obj.not.set.proper'));
		return;
	}// if
	var itemobj = this.formobj[itemname];
    if(itemobj.length && isNaN(itemobj.selectedIndex) )
    // for radio button; don't do for 'select' item
	{
		itemobj = itemobj[0];
	}	
  if(!itemobj)
	{
		alert(parent.frames[0].i18Obj.prop('error.couldnot.get.ip.obj.named')+": "+itemname);
		return;
	}
	// alert("1"+itemname);
	parent.frames[1].document.error_disp_handler.clear_msgs(dispDiv);
	// alert("2");
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
         }// case required
        case "maxlength": 
        case "maxlen": 
          { 
			 ret = mxLen(objValue,cmdvalue,strError,dispDiv)
             break; 
          }// case maxlen
        case "minlength": 
        case "minlen": 
           { 
			 ret = mnLen(objValue,cmdvalue,strError,dispDiv)
             break; 
            }// case minlen
        case "alnum": 
        case "alphanumeric": 
           { 
				ret = inpTyp(objValue,"[^A-Za-z0-9àâäèéêëîïìôœùûüÿçÀÂÄÈÉÊËÎÏÔŒÙÛÜŸÇ]",strError, 
						objValue.name+": "+parent.frames[0].i18Obj.prop('error.only.alpha.allow'),dispDiv);
				break; 
           }
        case "alnum_s": 
        case "alphanumeric_space": 
           { 
				ret = inpTyp(objValue,"[^A-Za-z0-9àâäèéêëîïìôœùûüÿçÀÂÄÈÉÊËÎÏÔŒÙÛÜŸÇ\\s]",strError, 
						objValue.name+": "+parent.frames[0].i18Obj.prop('error.only.alpha.space.allow'),dispDiv);
				break; 
           }		   
        case "num": 
        case "numeric": 
           { 
                ret = inpTyp(objValue,"[^0-9]",strError, 
						objValue.name+": "+parent.frames[0].i18Obj.prop('error.only.digit'),dispDiv);
                break;               
           }
        case "dec": 
        case "decimal": 
           { 
                ret = inpTyp(objValue,"[^0-9\.]",strError, 
						objValue.name+": "+parent.frames[0].i18Obj.prop('error.only.nbr'),dispDiv);
                break;               
           }
        case "alphabetic": 
        case "alpha": 
           { 
                ret = inpTyp(objValue,"[^A-Za-zàâäèéêëîïìôœùûüÿçÀÂÄÈÉÊËÎÏÔŒÙÛÜŸÇ]",strError, 
						objValue.name+": "+parent.frames[0].i18Obj.prop('error.only.alphabet.char.allow'),dispDiv);
                break; 
           }
        case "alphabetic_space": 
        case "alpha_s": 
           { 
                ret = inpTyp(objValue,"[^A-Za-zàâäèéêëîïìôœùûüÿçÀÂÄÈÉÊËÎÏÔŒÙÛÜŸÇ\\s]",strError, 
						objValue.name+": "+parent.frames[0].i18Obj.prop('error.only.alphabet.char.space.allow'),dispDiv);
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
         }// case greaterthan
        case "regexp": 
         { 
         	ret = regX(objValue,cmdvalue,strError,dispDiv);
           break; 
         }
        case "regexpSerNo": 
         {
         	ret = regXSerNo(objValue,cmdvalue,strError,dispDiv);
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
		
		case "selone_chk":
		{
			ret = selCheckBox(objValue,strError,dispDiv);
		    break;
		}	
		case "non_zero":
		{
			ret = nonZeroValue(objValue,cmdvalue,strError,dispDiv);
			break;
		}
    }// switch
	return ret;
}

function reqInp(objValue,strError,dispDiv)
{
 var ret = true;
 var val = objValue.value;
 val = val.replace(/^\s+|\s+$/g,"");// trim
    if(eval(val.length) == 0) 
    { 
       if(!strError || strError.length ==0) 
       { 
         strError = objValue.name + " : "+parent.frames[0].i18Obj.prop('error.req.field'); 
       }// if
	   // alert()
       _errMsg(strError,dispDiv); 
       ret=false; 
    }// if
return ret;
}

function mxLen(objValue,strMaxLen,strError,dispDiv)
{
 var ret = true;
    if(eval(objValue.value.length) > eval(strMaxLen)) 
    { 
      if(!strError || strError.length ==0) 
      { 
        strError = objValue.name + " : "+ strMaxLen +parent.frames[0].i18Obj.prop('error.char.max'); 
      }// if
      _errMsg(strError,dispDiv); 
      ret = false; 
    }// if
return ret;
}

function mnLen(objValue,strMinLen,strError,dispDiv)
{
 var ret = true;
 objValue.style.color= '#777';
    if(eval(objValue.value.length) <  eval(strMinLen)) 
    { 
      if(!strError || strError.length ==0) 
      { 
        strError = objValue.name + " : " + strMinLen + parent.frames[0].i18Obj.prop('error.char.min'); 
      }// if
      _errMsg(strError,dispDiv); 
      ret = false;
      objValue.style.color="red";
    }// if
return ret;
}

function inpTyp(objValue,strRegExp,strError,strDefaultError,dispDiv)
{
   var ret = true;
 	
  
    var charpos = objValue.value.search(strRegExp); 
     if(objValue.value.length > 0 &&  charpos >= 0 || !validateDecimal(objValue.value)) 
    { 
      if(!strError || strError.length ==0) 
      { 
        strError = strDefaultError;
      }// if
      _errMsg(strError,dispDiv); 
      ret = false; 
    }// if
 return ret;
}

function _email(objValue,strError,dispDiv)
{
var ret = true;
     if(objValue.value.length > 0 && !validateEmail(objValue.value)	 ) 
     { 
       if(!strError || strError.length ==0) 
       { 
          strError = objValue.name+": "+parent.frames[0].i18Obj.prop('error.invalid.email'); 
       }// if
       _errMsg(strError,dispDiv); 
       ret = false; 
     }// if
return ret;
}
function lsThn(objValue,strLessThan,strError,dispDiv)
{
var ret = true;
	  if(isNaN(objValue.value)) 
	  { 
	    _errMsg(objValue.name +": "+parent.frames[0].i18Obj.prop('error.should.be.nbr'),objValue); 
	    ret = false; 
	  }// if
	  else
	  if(eval(objValue.value) >=  eval(strLessThan)) 
	  { 
	    if(!strError || strError.length ==0) 
	    { 
	      strError = objValue.name + " : "+parent.frames[0].i18Obj.prop('error.val.should.lt')+ strLessThan; 
	    }// if
	    _errMsg(strError,dispDiv); 
	    ret = false;                 
	   }// if
return ret;          
}
function grtrThn(objValue,strGreaterThan,strError,dispDiv)
{
var ret = true;
     if(isNaN(objValue.value)) 
     { 
       _errMsg(objValue.name+": "+parent.frames[0].i18Obj.prop('error.should.be.nbr'),objValue); 
       ret = false; 
     }// if
	 else
     if(eval(objValue.value) <=  eval(strGreaterThan)) 
      { 
        if(!strError || strError.length ==0) 
        { 
          strError = objValue.name + " : "+parent.frames[0].i18Obj.prop('error.val.should.gt')+ strGreaterThan; 
        }// if
        _errMsg(strError,dispDiv);  
        ret = false;
      }// if
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
      }// if
      _errMsg(strError,dispDiv); 
      ret = false; 
    }// if
 return ret;
}

function regXSerNo(objValue,strRegExp,strError,dispDiv)
{
   var ret = true;
    var charpos = objValue.value.search(strRegExp); 
    if(objValue.value.length > 0 &&  charpos >= 0) 
    { 
     if(!strError || strError.length ==0) 
      { 
        strError = strDefaultError;
      }// if
      _errMsg(strError,dispDiv);
      // alert(objValue.value);
      objValue.style.color="red";
      ret = false; 
    }// if
 return ret;
}

function dontSel(objValue,dont_sel_index,strError,dispDiv)
{
var ret = true;
    if(objValue.selectedIndex == null) 
    { 
      _errMsg(parent.frames[0].i18Obj.prop('error.dont.select.cmd.fr.nonselect.item')); 
      ret =  false; 
    } 
    if(objValue.selectedIndex == eval(dont_sel_index)) 
    { 
     if(!strError || strError.length ==0) 
      { 
      strError = objValue.name+": "+parent.frames[0].i18Obj.prop('error.select.one.option'); 
      }// if
      _errMsg(strError,dispDiv); 
      ret =  false;                                   
     } 
return ret;
}

function selCheckBox(objValue,strError,dispDiv) 
{
	// Vaibhav
	var objchkbox = objValue.form.elements[objValue.name];
	var selected=false;
	for(var i=0;i < objchkbox.length;i++)
	{
	  if(objchkbox[i].checked)
	  {
	  	selected=true;
		break;
	  }
	}
	if(false == selected)
	{
      if(!strError || strError.length ==0) 
       {
	    strError = parent.frames[0].i18Obj.prop('error.select.one.option.from') + objValue.name;
	   }	
	  _errMsg(strError,dispDiv);
	}
	
	return selected;
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
	    strError = parent.frames[0].i18Obj.prop('error.select.one.option.from') + objValue.name;
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
      var regexp_user=/^\"?[\u00C0-\u00FF\w-_\.]*\"?$/;
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

function validateDecimal(decimalValue)
{
  if(decimalValue==".")
     return false;
     
   var array = new Array();
			array = decimalValue.split(".");
			if(array.length>2)
			  return false;
			if(array.length >1 )
			 {
			  if(array[1].length > 2 || array[1].length < 1)
			   return false;
			      else
			       return true;
			 }else
			  return true;

}

function nonZeroValue(objValue,nonZero,strError,dispDiv){
	if(objValue.value.length > 0){
		if(isNaN(objValue.value)){
			return true;
		}else {
			if(objValue.value > 0){
			return true;
			}else{
				_errMsg(strError,dispDiv);
				objValue.style.color="red";
				return false;
			}
		}
	}
	return true;
}
parent.frames[0].Version['$Source: /rep/LMS_Mgmt/WebRoot/com/skilrock/lms/web/common/globalJs/validator.js,v $'] = '$Id: validator.js,v 1.6 2016/10/31 09:46:50 neeraj Exp $';