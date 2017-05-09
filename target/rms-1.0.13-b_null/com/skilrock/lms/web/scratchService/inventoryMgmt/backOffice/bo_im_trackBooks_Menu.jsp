	<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
	<%@ taglib prefix="s" uri="/struts-tags"%>
	
	<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
	
	<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
	
	<head>
	
	
	
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
	
	<link rel="stylesheet" href="<%=request.getContextPath() %>/LMSImages/css/styles.css" scrolling="no" type="text/css"/>
	<style type="text/css">
.outerBorderLeft 
		{
          border-left :  3px solid #000000;
		 
 }

</style>
	<title><%=application.getAttribute("JSP_PAGE_TITLE")%></title>
		
	
	
	<script>

 var mainDiv;
  var req;
  var which;

  function retrieveURL(url,div) {
  mainDiv=div;
   _resp  = _ajaxCall(url);
	if(_resp.result){
      _id.i(mainDiv,"&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Select Game:       " +_resp.data);
      } else {
        alert(i18nOb.prop('error.problem'));
        }
  }
  function checkValidation(){ 
  
  
  var isValid=true;
  if(document.getElementById("gameStatusId").value=="--Please Select--"){
  document.getElementById("gameStatusId1").innerHTML="<font color=\"red\"><s:text name='error.pls.slct.any.value'/></font>";
  isValid=false;
  }
  else{
  document.getElementById("gameStatusId1").innerHTML="";
  }

  
   if(document.getElementById("gameNameId").value=="--Please Select--"||document.getElementById("gameNameId").value=="-1"){

  
  document.getElementById("gameNameId1").innerHTML="<font color=\"red\"><s:text name='error.pls.slct.any.value'/></font>";
  isValid=false;
  }
  else{
  document.getElementById("gameNameId1").innerHTML="";
  }
   if(document.getElementById("bookNumberId").value==""){
   
  
  document.getElementById("bookNumberId1").innerHTML="<font color=\"red\"><s:text name='error.entr.any.value'/></font>";
  isValid=false;
  }
  else{
  var numId="bookNumberId"; 

  if(!isNumeric(numId)){			
		document.getElementById("bookNumberId1").innerHTML = "<font color=red><s:text name='error.pls.entr.valid.value'/></font>";
		isValid=false;
		}
		else{
		document.getElementById("bookNumberId1").innerHTML = "";
		}
		
  }
  return isValid;
  
  }
  function clearDiv(id)
  {
  document.getElementById(id+"1").innerHTML="";
  }
  function isNumeric(id) {
	var strValidChars = "0123456789-";
	var strChar;
	
	var dashCount=0;
	var strString=	document.getElementById(id).value;
	if (strString.charAt(0)=="-") {
			return false;
	}
	if (strString.charAt(strString.length-1)=="-") {
			return false;
	}

   //  test strString consists of valid characters listed above
	for (i = 0; i < strString.length ; i++) {
		strChar = strString.charAt(i);		
		if(strChar=="-"){dashCount++;}		
		if (strValidChars.indexOf(strChar) == -1) {
		
			document.getElementById(id+"1").innerHTML = "<font color = 'red'><s:text name='error.pls.entr.valid.value'/></font>";			
			
			return false;
			
		}
	}
	
	if(dashCount>1){
	
			document.getElementById(id+"1").innerHTML = "<font color = 'red'><s:text name='error.pls.entr.valid.value'/></font>";			
			return false;
	}
	
	
	return true;
}
</script>

<s:head theme="ajax" debug="false"/>
		</head>
	<body>
<%@include file="/com/skilrock/lms/web/loginMgmt/menu.jsp" %>

	<div id="wrap">

  <div id="top_form">
  
  		<h3><s:text name="label.track.books"/></h3>
		<s:form action="bo_im_trackBooks_Detail" onsubmit="return checkValidation()">
			<table>
				<tr>
					<td>
						<div id="gameStatusId1"></div>
						<s:select list="{'--Please Select--','OPEN','CLOSE','TERMINATE','SALE_HOLD','SALE_CLOSE'}" cssClass="option"
							id="gameStatusId" key="label.slct.game.status"
						 	onchange="retrieveURL('bo_im_trackBooks_GameList.action?status=' + this.value,'characters')" onclick="clearDiv(this.id)"></s:select>
					</td>
				</tr>

				<tr>
					<td colspan="3" align="center">
						<div id="gameNameId1"></div>
						<span id="characters"><s:text name="label.slct.game"/>
							: <select class="option"  name="gameNameNbr" id="gameNameId">
								<option value="--Please Select--">
									<s:text name="label.please.select"/>
								</option>
							</select> </span>
					</td>
				</tr>

				<tr>
					<td>
						<div id="bookNumberId1"></div>
						<s:textfield name="bookNumber" id="bookNumberId"
							key="label.entr.book.no" onfocus="clearDiv(this.id)"></s:textfield>
					</td>
				</tr>
				<tr>
					<s:submit key="btn.show" targets="bottom" cssClass="button" theme="ajax"></s:submit>
				</tr>
			</table>
		</s:form>

		<div id="bottom">

		</div>

</div></div>
	</body>

</html>
<code id="headId" style="visibility: hidden">
$RCSfile: bo_im_trackBooks_Menu.jsp,v $
$Revision: 1.3 $
</code>