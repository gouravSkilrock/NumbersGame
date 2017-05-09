<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="com.skilrock.lms.common.utility.FormatNumber"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">

	<%
	response.setHeader("Cache-Control","no-store"); //HTTP 1.1
	response.setHeader("Pragma","no-cache"); //HTTP 1.0
	response.setDateHeader ("Expires", 0); //prevents caching at the proxy server
%>
	<head>
	

	<META HTTP-EQUIV="CACHE-CONTROL" CONTENT="NO-CACHE" />



<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />

<link rel="stylesheet" href="<%=request.getContextPath() %>/LMSImages/css/styles.css"  type="text/css"/>

		<title><%=application.getAttribute("JSP_PAGE_TITLE")%></title>
<script type="text/javascript" src="<%=request.getContextPath() %>/com/skilrock/lms/web/scratchService/pwtMgmt/backOffice/js/ACT_ST5_Validation.js" ></script>
	

	<script>
			function IsNumeric(name)
			{
			var value1 = name;
			  if(isNaN(document.getElementById(name).value))
	   		{
	        alert("Enter Number Only for field "+document.getElementById(name).name);
	  		document.getElementById(name).focus();
	  		return false;
	  	}
	  		return true;
   			}
   function IsString(name)
	{
		var value1 = name;
		  if(!isNaN(document.getElementById(name).value))
	   {
	        alert("Enter String Only for field "+document.getElementById(name).name);
	  		document.getElementById(name).focus();
	  		return false;
	   }
		return true;	  
   }
 function isValidation(){
  

if(document.playerPWT.firstName.value=="" ||document.playerPWT.lastName.value=="" ||document.playerPWT.idNumber.value=="")
  		{
				alert("Enter Requiered fields");
				return false;
		}
else {
	if( document.playerPWT.idType.value =="-1")
    	{
        alert ( "Please select ID Type." );
        return false;
    	}
  	else  
  	{  
       return true;
       
   }
}

}
</script>
<script>
 var mainDiv;
  function retrieveURL(url,div) {
  mainDiv=div;
   _resp  = _ajaxCall(url);
	if(_resp.result){
      _id.i(mainDiv,"State:" +_resp.data);
      } else {
        alert("Problem: ");
        }
  }
function hi(){
alert("hi");
alert(document.na.text1.value);
}

function IsNumeric(name)
	{
		var value1 = name;
		  if(isNaN(document.getElementById(name).value))
	   {
	        alert("Enter Correct Value For "+ name);
	  		document.getElementById(name).focus();
	  		return true;
	   }
	  
   }
function isSelect()
{


  if(document.na.country.value==-1)
  {
      alert("Please Select the Country");
      document.na.country.focus();
      return false;        
  }
   else
     if(document.na.state.value==-1)
     {
          alert("Please Select the State");
      document.na.state.focus();
      return false;       
     }
    
}

	function _subTrans(){
	return ( ST5_isplayerValidate() && _subValid('subTrans'));
	}

</script>
	<s:head theme="ajax"/> 
	
	</head>

<body>
<%@include file="/com/skilrock/lms/web/loginMgmt/menu.jsp" %>
     <s:form id="playerPWT"   name="playerPWT" >
     <tr>
     <td align="left">
	<table  border="0">
	<tr><td>
	<table  border="0"> 
				
				
					<tr>
					  	<th>
					     <s:iterator value="#session.PWTLIST">
					       Winning Amount is 
					        <s:set name="pwtAmount" value="%{pwtAmount}"/>
					       <%=FormatNumber.formatNumberForJSP(pageContext.getAttribute("pwtAmount")) %>
					      
					     </s:iterator>
					    </th>
					</tr>
				
				
				<tr><th> 
				Player Verification 
				</th></tr>

  
              <tr> <td> <div id = "firstName1" align="center"></div></td></tr>
				<tr>
					<s:textfield label="First Name" name="firstName" id="firstName"   required="true" size="10" maxlength="15" ></s:textfield>
				</tr>
				         <tr> <td> <div id = "lastName1" align="center"></div></td></tr>
				<tr>
					<s:textfield label="Last Name" name="lastName" id="lastName"  required="true" size="10" maxlength="15" ></s:textfield>
				</tr>
				         <tr> <td> <div id = "idType1" align="center"></div></td></tr>
				<tr>
					<s:select cssClass="option" headerKey="-1" headerValue="-- Please Select --" label="ID Type" name="idType" id="idType" list="{'Passport','Driving Licence','NID'}" required="true"></s:select>
				</tr>
				 <tr> <td> <div id = "idNumber1" align="center"></div></td></tr>
				<tr>
					<s:textfield label="ID Number" name="idNumber" id="idNumber"  required="true" size="10" maxlength="15"  ></s:textfield>
				</tr>
            </table>
	</td></tr>
	</table>	
  
            <div id="remainDetail" align="left">
			 <s:a   theme="ajax" formId="playerPWT" href="doPlayerVerify.action" targets="remainDetail1" onclick="return ST5_isplayerValidation()" >Verify Player</s:a>
			 </div>
             </td>
             </tr>
			<tr><td ><div id="remainDetail1"> </div></td></tr>
			 <tr><td width="800">&nbsp;</td></tr>
            </s:form>	
			 
				<s:iterator value="#session.COUNTER" >
				<s:set name="count" value="%{intValue()}"/>
				</s:iterator>
				
   <p align="left"><font size="4" color="red"><s:label theme="simple" value="Step %{count} of  3"/></font></p>
		
	</body>
</html>
<code id="headId" style="visibility: hidden">
$RCSfile: playerPWTDetail.jsp,v $
$Revision: 1.2 $
</code>