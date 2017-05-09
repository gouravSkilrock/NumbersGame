<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="com.skilrock.lms.common.utility.FormatNumber"%>
<%@page import="java.util.Map"%>
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

<link rel="stylesheet" href="<%=request.getContextPath() %>/LMSImages/css/styles.css" type="text/css"/>
<script type="text/javascript">

function updateValues(editButton,tdId,commAmtId)
{
    
  var editButtonValue=document.getElementById(editButton).value;
  //alert(document.getElementById(editButton).value);
 
   if(editButtonValue=="edit")
       {
         document.getElementById(editButton).value="update";
         document.getElementById(tdId).style.display="block";
         document.getElementById('headerId').style.display="block";
         
        }
     else
        {  
        
          var commAmount=document.getElementById(commAmtId);
           document.getElementById('headerId').style.display="none";
         // alert(isDecimal(commAmount.value));
           if(isDecimal(commAmount.value)==false || commAmount.value==".")
            {
	          document.getElementById('errorCommAmtDiv').innerHTML="<font color='red'>Please Fill Correct value for comm amt</font>";
	          document.getElementById(commAmtId).style.color="red";
	          document.getElementById(commAmtId).focus();
	        } 
	        else{
	        	var amountArr = commAmount.value.split('.');
	        	if(amountArr.length > 1){
	        		if(amountArr[1].length > 2){
	      				document.getElementById("errorCommAmtDiv").innerHTML = "<font color = 'red'>Amount upto Two Decimal Point is Allowed</font>";
					} else {
						document.getElementById(editButton).value="edit";
				        document.getElementById(tdId).style.display="none";
				        document.getElementById(commAmtId).style.color="black";
				        document.getElementById(commAmtId).readOnly=true;
				        document.getElementById('errorCommAmtDiv').innerHTML=""; 
					}
	        	} else {
	        		document.getElementById(editButton).value="edit";
			        document.getElementById(tdId).style.display="none";
			        document.getElementById(commAmtId).style.color="black";
			        document.getElementById(commAmtId).readOnly=true;
			        document.getElementById('errorCommAmtDiv').innerHTML=""; 
	        	}
		          
	         }         
        }
        
 //alert(document.getElementById(editButton).value);

}


function editChequeDetails(radioValue,agtOrgName,chkNbr,chkAmt,chkDate,chkStatus,draweeBank,issParty,commAmt)

{
   if(radioValue=="Remove")
    {
       document.getElementById(agtOrgName).disabled=true;
       document.getElementById(chkNbr).disabled=true;
       document.getElementById(chkAmt).disabled=true;
       document.getElementById(chkDate).disabled=true;
       document.getElementById(chkStatus).disabled=true;
       
       document.getElementById(chkStatus).value="Removed";  
       
       document.getElementById(draweeBank).disabled=true;
       document.getElementById(issParty).disabled=true;
       document.getElementById(commAmt).disabled=true;
       document.getElementById(commAmt).value=0.00;
       
    
    }
   
  if(radioValue=="Cleared")
    {
       document.getElementById(agtOrgName).disabled=false;
       document.getElementById(chkNbr).disabled=false;
       document.getElementById(chkAmt).disabled=false;
       document.getElementById(chkDate).disabled=false;
       document.getElementById(chkStatus).disabled=false;
       
       document.getElementById(chkStatus).value="Cleared";       
              
       document.getElementById(draweeBank).disabled=false;
       document.getElementById(issParty).disabled=false;
       document.getElementById(commAmt).disabled=false;
       document.getElementById(commAmt).readOnly=true;
       document.getElementById(commAmt).value=0.00;
    
    }
    
    
    if(radioValue=="Cancel")
    {
      //alert("inside cancel");
       document.getElementById(agtOrgName).disabled=false;
       document.getElementById(chkNbr).disabled=false;
       document.getElementById(chkAmt).disabled=false;
       document.getElementById(chkDate).disabled=false;
       document.getElementById(chkStatus).disabled=false;
       
       document.getElementById(chkStatus).value="Cancel";
       
       document.getElementById(draweeBank).disabled=false;
       document.getElementById(issParty).disabled=false;
       document.getElementById(commAmt).disabled=false;
      // alert("dds " + document.getElementById(commAmt).readOnly);
       document.getElementById(commAmt).readOnly=false;
       // alert(document.getElementById(commAmt).readOnly);
           
    }


}

function verifyData()
{
  var flag=true;
  var allEditButton=document.getElementsByName('edit');
  for(var i=0;i<allEditButton.length;i++)
     {
       if(allEditButton[i].value=="update")
        {
         // alert('please update all');
           flag=false;
           document.getElementById('errorDiv').innerHTML="<font color='red'>Please Update All Edit values</font>";
           return false;
           break;
           
        }else
        document.getElementById('errorDiv').innerHTML="";
     }
     
     var allChequeStatus=document.getElementsByName('isCleared');
   // alert(allChequeStatus.length);
     for(var j=0;j<allChequeStatus.length;j++)
     {
       if(allChequeStatus[j].value=="Removed")
       {
       //alert('inside df');
         flag=false;
         document.getElementById('errorNocheque').innerHTML="<font color='red'>Please Clear or cancel at least one cheque</font>";       
       }
       
       else
        {
          document.getElementById('errorNocheque').innerHTML="";
          flag=true;
          break;
        }
     
     }
     
   //alert("alert for flag is " + flag);  
   if(flag)
   return _subValid('subTrans');    
  return flag;
}

function verifyCommAmt()
{

 var commAmount=document.getElementsByName('commAmt');
 
for(var i=0;i<commAmount.length;i++)
{
 
  if(!isDecimal(commAmount[i].value))
    {
     
     return false;
     break;
    }

}

 return true;
}


function isDecimal(sText1)
{  
 var sText=sText1;
   var validChars = "0123456789.";
   var isNumber=true;
   var myChar;
var count=0;
 if(sText.length==0){
 isNumber=false;

 }

   for (i = 0; i < sText.length ; i++) 
      { 
      myChar = sText.charAt(i); 
      if(myChar==".")
      {count++;}
      
      if (validChars.indexOf(myChar) == -1 || count>1) 
         {
         isNumber = false;
         break;
         }
      }
   return isNumber;
   
   }

</script>
<title><%=application.getAttribute("JSP_PAGE_TITLE")%></title>

<s:head theme="ajax" debug="false"/>
	</head>
	<body>
	<%@include file="/com/skilrock/lms/web/loginMgmt/menu.jsp"%>

	<div id="wrap">

  <div id="top_form">
   <h3>Verify Cheques For Clearance</h3>
  </div></div>
  <s:form action="bo_act_clrChq_Save" name="verifyCheque" onsubmit="return verifyData();">
  <s:token />
   <table>
	   <tr><td colspan="9"><div id="errorDiv"></div></td></tr>
	    <tr><td colspan="9"><div id="errorCommAmtDiv"></div></td></tr>
	    <tr><td colspan="9"><div id="errorNocheque"></div></td></tr>
	</table>
  <s:iterator   value="%{chqAgtBeanList}" status="rowIndexMain">
 
  <h3 align="left"><s:property value="#application.TIER_MAP.AGENT" /> Name::  <s:property value="%{agentName}"/></h3>
  
     <table border="1" width="98%" cellpadding="3" cellspacing="0">
     <tr>
      <th>Cheque Nbr</th>
      <th>Cheque Amount</th>
      <th>Cheque Date</th>
      <th>Clearance Date</th>
      <th>Cheque to</th>
      <th>Drawee Bank</th>
      <th>issuing party</th>
      <th nowrap="nowrap">Bounce Charges</th>
      <th  id="headerId" style="display: none;">update Status</th>
      <th nowrap="nowrap">edit/update</th>
     </tr>
     <s:iterator  value="%{chequeDetails}" status="rowIndex">
         <tr id="r<s:property value="#rowIndex.index"/><s:property value="#rowIndexMain.index"/>">
	       <input type="hidden" name="agtOrgName" id='agtOrgName<s:property value="#rowIndex.index"/><s:property value="#rowIndexMain.index"/>' value="<s:property value="%{agtOrgName}"/>"/>
	       <input type="hidden" name="agentName" id='agentName<s:property value="#rowIndex.index"/><s:property value="#rowIndexMain.index"/>' value="<s:property value="%{agentName}"/>"/>
	       <td>
	          <s:property value="%{chqNBR}"/>
	          <input type="hidden" theme="simple" id='chkNbr<s:property value="#rowIndex.index"/><s:property value="#rowIndexMain.index"/>' size="8" name="chequeNbr"  readonly="true" value="<s:property value="%{chqNBR}"/>"/>
	      	  <input name="taskId"  type="hidden" value="<s:property value="%{taskId}"/>"/>
	       </td>
	       <s:set name="strChequeAmount" value="%{chequeAmount}" />
	        
	        <td>
	         <%=FormatNumber.formatNumberForJSP(pageContext.getAttribute("strChequeAmount")) %>
	         <input type="hidden" theme="simple" id='chkAmt<s:property value="#rowIndex.index"/><s:property value="#rowIndexMain.index"/>' size="11" name="chequeAmt" readonly="true"  value="<%=FormatNumber.formatNumberForJSP(pageContext.getAttribute("strChequeAmount")) %>"/>
	        </td>
	       
	       <td>
	          <s:property value="%{chequeDate}"/>
	         <input type="hidden" theme="simple" id='chkDate<s:property value="#rowIndex.index"/><s:property value="#rowIndexMain.index"/>' size="11" name="chequeDate" readonly="true" value="<s:property value="%{chequeDate}"/>"/>
	        </td>
	        
	       <td>
	        <s:property value="%{chequeClearanceDate}"/>
	         <input type="hidden" theme="simple" id='chkDate<s:property value="#rowIndex.index"/><s:property value="#rowIndexMain.index"/>' size="11" name="clearanceDate" readonly="true" value="<s:property value="%{chequeClearanceDate}"/>"/>
	        </td>
	        
	       <td>
	          <input type="text" theme="simple" id='chkStatus<s:property value="#rowIndex.index"/><s:property value="#rowIndexMain.index"/>' size="8" name="isCleared" readonly="true" value="<s:property value="%{chequeStatus}"/>"/>
	        </td>
	       
	       <td>
	         <s:property value="%{draweebank}"/>
	          <input type="hidden" theme="simple" id='draweeBank<s:property value="#rowIndex.index"/><s:property value="#rowIndexMain.index"/>' size="15" name="draweeBank" readonly="true" value="<s:property value="%{draweebank}"/>"/>
	        </td>
	        
	       <td>
	        <s:property value="%{issuingPartyName}"/>
	         <input type="hidden" theme="simple" id='issParty<s:property value="#rowIndex.index"/><s:property value="#rowIndexMain.index"/>' size="12" name="issuingPartyName" readonly="true" value="<s:property value="%{issuingPartyName}"/>"/>
	       </td>
	       
	       <td>
	         <input type="text" theme="simple" id='commAmt<s:property value="#rowIndex.index"/><s:property value="#rowIndexMain.index"/>' size="10" name="commAmt" readonly="true" maxlength="10" value="<s:property value="%{commAmt}"/>"/>
	       </td>
	      
	       
	       <td width="80" align="left" valign="top" style="display: none;" nowrap="nowrap" id="td<s:property value="#rowIndex.index"/><s:property value="#rowIndexMain.index"/>">
	         <select class="option" name="isClear<s:property value="#rowIndex.index"/>" onchange="editChequeDetails(this.value,'agtOrgName<s:property value="#rowIndex.index"/><s:property value="#rowIndexMain.index"/>','chkNbr<s:property value="#rowIndex.index"/><s:property value="#rowIndexMain.index"/>','chkAmt<s:property value="#rowIndex.index"/><s:property value="#rowIndexMain.index"/>','chkDate<s:property value="#rowIndex.index"/><s:property value="#rowIndexMain.index"/>','chkStatus<s:property value="#rowIndex.index"/><s:property value="#rowIndexMain.index"/>','draweeBank<s:property value="#rowIndex.index"/><s:property value="#rowIndexMain.index"/>','issParty<s:property value="#rowIndex.index"/><s:property value="#rowIndexMain.index"/>','commAmt<s:property value="#rowIndex.index"/><s:property value="#rowIndexMain.index"/>')"><option value="Cleared">Clear</option><option value="Cancel">Cancel</option><option value="Remove">Remove</option></select>
	         <!--  
	         <input type="radio" name="isClear<s:property value="#rowIndex.index"/>" value="Cleared" onclick="editChequeDetails(this.value,'agtOrgName<s:property value="#rowIndex.index"/><s:property value="#rowIndexMain.index"/>','chkNbr<s:property value="#rowIndex.index"/><s:property value="#rowIndexMain.index"/>','chkAmt<s:property value="#rowIndex.index"/><s:property value="#rowIndexMain.index"/>','chkDate<s:property value="#rowIndex.index"/><s:property value="#rowIndexMain.index"/>','chkStatus<s:property value="#rowIndex.index"/><s:property value="#rowIndexMain.index"/>','draweeBank<s:property value="#rowIndex.index"/><s:property value="#rowIndexMain.index"/>','issParty<s:property value="#rowIndex.index"/><s:property value="#rowIndexMain.index"/>','commAmt<s:property value="#rowIndex.index"/><s:property value="#rowIndexMain.index"/>')"/>Clear<br /> 
	         <input type="radio" name="isClear<s:property value="#rowIndex.index"/>" value="Cancel" onclick="editChequeDetails(this.value,'agtOrgName<s:property value="#rowIndex.index"/><s:property value="#rowIndexMain.index"/>','chkNbr<s:property value="#rowIndex.index"/><s:property value="#rowIndexMain.index"/>','chkAmt<s:property value="#rowIndex.index"/><s:property value="#rowIndexMain.index"/>','chkDate<s:property value="#rowIndex.index"/><s:property value="#rowIndexMain.index"/>','chkStatus<s:property value="#rowIndex.index"/><s:property value="#rowIndexMain.index"/>','draweeBank<s:property value="#rowIndex.index"/><s:property value="#rowIndexMain.index"/>','issParty<s:property value="#rowIndex.index"/><s:property value="#rowIndexMain.index"/>','commAmt<s:property value="#rowIndex.index"/><s:property value="#rowIndexMain.index"/>')"/>Cancel<br />
	         <input type="radio" name="isClear<s:property value="#rowIndex.index"/>" value="Remove" onclick="editChequeDetails(this.value,'agtOrgName<s:property value="#rowIndex.index"/><s:property value="#rowIndexMain.index"/>','chkNbr<s:property value="#rowIndex.index"/><s:property value="#rowIndexMain.index"/>','chkAmt<s:property value="#rowIndex.index"/><s:property value="#rowIndexMain.index"/>','chkDate<s:property value="#rowIndex.index"/><s:property value="#rowIndexMain.index"/>','chkStatus<s:property value="#rowIndex.index"/><s:property value="#rowIndexMain.index"/>','draweeBank<s:property value="#rowIndex.index"/><s:property value="#rowIndexMain.index"/>','issParty<s:property value="#rowIndex.index"/><s:property value="#rowIndexMain.index"/>','commAmt<s:property value="#rowIndex.index"/><s:property value="#rowIndexMain.index"/>')"/>Remove         
	        -->
	       </td>
           
             <td nowrap="nowrap">
               <input type="button" class="button" value="edit" name="edit" id='edit<s:property value="#rowIndex.index"/><s:property value="#rowIndexMain.index"/>' onclick="updateValues('edit<s:property value="#rowIndex.index"/><s:property value="#rowIndexMain.index"/>','td<s:property value="#rowIndex.index"/><s:property value="#rowIndexMain.index"/>','commAmt<s:property value="#rowIndex.index"/><s:property value="#rowIndexMain.index"/>')"></input>
             </td>
             
           </tr>
     </s:iterator>
      </table>
  
  
  </s:iterator>
  <div id="subCheck" style="position:absolute;"></div>
    <table>
    <tr ><td align="left"> <s:submit value="submit" id="subTrans" cssClass="button"/></td></tr>
    </table> 
  </s:form>
  
	</body>
</html>
<code id="headId" style="visibility: hidden">
$RCSfile: bo_act_clrChq_VerifyResult.jsp,v $
$Revision: 1.2 $
</code>