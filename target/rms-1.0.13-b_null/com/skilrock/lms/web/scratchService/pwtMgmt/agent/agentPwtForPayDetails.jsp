<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@page import="com.skilrock.lms.common.utility.FormatNumber"%>
<%@page import="com.skilrock.lms.common.utility.CommonMethods"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
	<link rel="stylesheet" href="<%=request.getContextPath() %>/LMSImages/css/styles.css"  type="text/css"/>
    <script>var projectName="<%=request.getContextPath() %>"</script>
    <script  src="<%=request.getContextPath() %>/com/skilrock/lms/web/common/globalJs/calender.js"></script>
    <link type="text/css" rel="stylesheet" href="<%=request.getContextPath() %>/LMSImages/css/lmsCalendar.css" media="screen"/>
	<title><%=application.getAttribute("JSP_PAGE_TITLE")%></title>
	<script>
	 
	 function validateFormData(){	 
		 
		 var frmVal  = new parent.frames[0].Validator("playerDetails");
		 
		  var radButton = _id.n('paymentType');
			for(var a=0; a<radButton.length; a++){
				 	if(radButton[a].value !='cheque' && radButton[a].checked){
			 		 return true;
			 	}
			}
			  
			  frmVal.aV("chqNbr","req","Please Enter Cheque Number","chequeNbrError");	  
		  	  frmVal.aV("chqNbr","regexp=[^0-9]","Please Enter Valid cheque Number","chequeNbrError");
		  	  
		  	   frmVal.aV("draweeBank","req","Please Enter Drawee Bank","draweeBankError");	  
		  	   frmVal.aV("draweeBank","regexp=[^A-Za-z\\s/,.]","Please Enter Correct Drawee Bank","draweeBankError");
		  	  
			  return document.error_disp_handler.isValid;
			   
	 }
	 
	 
	 
	 function calculateNetAmt(){	 
		  var frmVal  = new parent.frames[0].Validator("playerDetails");
		  var taxAmount=_id.n('taxAmt')[0].value;
		  var pwtAmount=_id.n('pwtAmount')[0].value;
		  //alert("dd" + taxAmount);
		   frmVal.aV("taxAmt","req","Please Enter Tax value","taxError");
		  if(!document.error_disp_handler.isValid){
		     _id.i('taxError','Please Enter correct Value for Tax');
		    	  return false;
		  }	
		  
		  if(isNaN(taxAmount)){
		         _id.i('taxError','Please Enter correct Value for Tax');
		    	  return false;
		   }else if(parseFloat(taxAmount) >= parseFloat(pwtAmount)){
		           _id.i('taxError','Tax should not be greater then Winning Amount');
		    		return false;
		   }else if(parseFloat(taxAmount) < 0){
		      		_id.i('taxError','Tax Amount can not be a negative value');
		    		return false;
		   }
		  _id.i('taxError','');	 	 
		  var netAmount= parseFloat(pwtAmount) - parseFloat(taxAmount);
		  _id.o('netAmt').value=netAmount;
		  
		  if(validateFormData())
		  	  return true;
		  else
		     return false;
	 }
	
	function _subTrans(){
			return (  calculateNetAmt() && _subValid('subTrans'));
			}
	
	</script>
	<s:head theme="ajax" debug="false"/>
</head>

   <body>

      <%@include file="/com/skilrock/lms/web/loginMgmt/menu.jsp" %>

	 <div id="wrap">

         <div id="top_form">

           <s:form name="playerDetails"> 
               <!-- hidden fields like task id ,virn number etc. -->
               
               <s:hidden name="taskId" value="%{plrPwtBean.task_id}"/>
                <s:hidden name="pwtAmount" value="%{plrPwtBean.pwt_amt}"/>
                <s:hidden name="ticketNbr" value="%{plrPwtBean.ticket_nbr}"/>
                <s:hidden name="virnNbr" value="%{plrPwtBean.virn_nbr}"/>
                <s:hidden name="gameId" value="%{plrPwtBean.game_id}"/>
                 <s:hidden name="gameNbr" value="%{plrPwtBean.game_nbr}"/>
               <s:hidden name="partyId" value="%{plrPwtBean.partyId}"/>
               <s:hidden name="partyType" value="%{plrPwtBean.partyType}"/>
             

        <table border="1" cellpadding="4" cellspacing="0" width="450" bordercolor="#cccccc" align="center">
              <tr>
              <td align="center" class="bold_txt" colspan="3" height="20">Player Info</td>
              </tr>

                 <tr>

                    <td width="110" class="label" align="right">Player Name:</td>

                    <td  class="data"><s:property value="%{plrPwtBean.partyName}"/></td>

                 </tr> 
                 
                 <tr>

                    <td class="label" align="right">Party Type:</td>

                    <td  class="data"><s:property value="%{plrPwtBean.partyType}"/></td>

                 </tr> 
                 
                 
                 <tr>

                    <td class="label" align="right">Party City:</td>

                    <td  class="data"><s:property value="%{plrPwtBean.City}"/></td>

                 </tr> </table><br />

                          

         <table  border="1" cellpadding="4" cellspacing="0" width="450" bordercolor="#cccccc" align="center">
				<tr>
              <td align="center" class="bold_txt" colspan="3" height="20">Winning Info</td>
              </tr>
                 
                 <tr>

                    <td width="110" class="label" align="right">Ticket Number:</td>
                    <td class="data"><s:property value="%{plrPwtBean.ticket_nbr}"/></td>
                 </tr>
                 
                  <tr>

                    <td class="label" align="right">Virn Number:</td>
                    <td class="data"><s:property value="%{plrPwtBean.virn_nbr}"/></td>
                 </tr>
                 
                 
                 
                 <tr>

                    <td class="label" align="right">Winning Amount:</td>
                    <td class="data"><s:property value="%{plrPwtBean.pwt_amt}"/></td>
                 </tr> 
                 
                 <tr>    				
                 	<td colspan="2"><div style="color: red;" id="taxError"></div></td>
                 </tr>
                 
                 <tr>                   
                    <s:textfield label="Comm Amount" name="taxAmt" value="%{plrPwtBean.comm_amt}" onblur="return calculateNetAmt()"/>
                 </tr>


                 

                 <tr>
                       <s:textfield label="Net Amount" readonly="true" name="netAmt" id="netAmt" value="%{plrPwtBean.net_amt}"/>
                 </tr> </table><br />

            
              <table  border="1" cellpadding="4" cellspacing="0" width="450" bordercolor="#cccccc" align="center">
               <tr>
              <td align="center" class="bold_txt" colspan="3" height="20">Payment Info</td>
              </tr>
                 <tr>
                   <td width="110" class="label" align="right">Payment Mode:</td>
	                   <td class="data">
	                     <s:if test="%{plrPwtBean.partyType=='PLAYER'}">
					          <input type="radio" value="cash" checked="checked" name="paymentType" onclick="document.getElementById('chqDiv').style.display='none';document.getElementById('tptDiv').style.display='none';"/>CASH 
					           <input type="radio" value="cheque" name="paymentType" onclick="document.getElementById('chqDiv').style.display='block';document.getElementById('tptDiv').style.display='none';"/>CHEQUE 
					             <s:if test="%{plrPwtBean.bankActNbr==null || plrPwtBean.bankActNbr.equals('')}">
					                <input type="radio" disabled="disabled"  title="Account Details are not Available So You can not make third party" value="TPT" name="paymentType" onclick="document.getElementById('tptDiv').style.display='block';document.getElementById('chqDiv').style.display='none';"/>TPT
			                      </s:if>
				                      <s:else>
				                         <input type="radio"  value="TPT" name="paymentType" onclick="document.getElementById('tptDiv').style.display='block';document.getElementById('chqDiv').style.display='none';"/>TPT
				                      </s:else>
			             </s:if>
			                 <s:else>
			                      <input type="radio"  value="cash" name="paymentType" onclick="document.getElementById('chqDiv').style.display='none';document.getElementById('tptDiv').style.display='none';"/>CASH
			                  </s:else> 
			           </td>
                 </tr>
                    <tr><td colspan="2">
		                <div id="chqDiv" style="display: none;">
<table border="0" cellpadding="4" cellspacing="0" width="100%" align="center">
			                  <tr>
			                  	<td colspan="2">
			                  	 <div id="chequeNbrError"></div>
			                  	</td>
			                  </tr>
			                  <tr>
			                    <td class="label" width="26%" align="right">Cheque Number:</td>
			                    <td class="data"><input type="text" size="10" maxlength="6" name="chqNbr"/></td>
			                  </tr>
			                    <tr>
						                    <td class="label" align="right">Cheque Date:</td>
						                    <td> 
							                     <table>
							                     <%
													String endDate = CommonMethods.convertDateInGlobalFormat((String)session.getAttribute("presentDate"), "yyyy-mm-dd", "dd-MM-yyyy");
													%>								 					                     
					     			             												 
							    				 <input  type="text" name="chequeDate" id="chequeDate" value="<%=endDate%>" readonly size="12"/>
							    				 <input type="button" style=" width:19px; height: 19px; background: url('<%=request.getContextPath() %>/LMSImages/imagesCal/dateIcon.gif'); top left; border:0 ; " onclick="displayCalendar(document.getElementById('chequeDate'), 'dd-MM-yyyy', this, '<%=endDate%>', '<%=endDate%>', 'false')" />

									        		
					     		                 </table>
					     		             </td>   
					                  </tr>
			                  <tr>
			                  	<td colspan="2">
			                  	 	<div id="draweeBankError"></div>
			                  	</td>
			                  </tr>
			                  <tr>
			                    <td class="label" align="right">Drawee Bank:</td>
			                    <td class="data"><input type="text" size="15" maxlength="45" name="draweeBank"/></td>
			                  </tr>
			                   <tr>
			                    <td class="label" align="right">Issuing Party:</td>
			                    <td class="data"><table cellpadding="0" c><s:textfield readonly="true" name="issuiningParty" value="%{#session.USER_INFO.orgName}"/></table></td>
			                  </tr>
		                 </table> 
		               </div>
		               
		               <div id="tptDiv" style="display: none;">
		                 <table border="0" cellpadding="4" cellspacing="0" width="100%">
					                  <tr>
					                    <td class="label" width="26%" align="right">Account Number:</td>
					                    <td class="data"><s:property value="%{plrPwtBean.bankActNbr}"/></td>
					                  </tr>
					                  <tr>
					                    <td class="label" align="right">Bank Name:</td>
					                    <td class="data"><s:property value="%{plrPwtBean.bankName}"/></td>
					                  </tr>
					                  <tr>
					                    <td class="label" align="right">Bank Branch:</td>
					                    <td class="data"><s:property value="%{plrPwtBean.bankBranch}"/></td>
					                  </tr>
					                   <tr>
					                    <td class="label" align="right">Bank City:</td>
					                    <td class="data"><s:property value="%{plrPwtBean.bankCity}"/></td>
					                  </tr>
				          </table> 
		               </div>
		               
                  </td></tr>
               </table>        
               <br></br>
                 <table  border="0" cellpadding="4" cellspacing="0" width="400" bordercolor="#cccccc" align="center">
                <tr>
		 		<td align="right">
			   		<table><div id="subCheck" style="position:absolute;"></div>
			   			<s:submit theme="simple" cssClass="button"  action="payPendingPwts" value="Submit" id="subTrans" onclick="return _subTrans();" />
			   			<s:hidden name="denyPwtStatus" value="Temporary Cancellation"></s:hidden>
			   			<s:submit theme="simple" cssClass="button"  action="denyPendingPwts" value="Deny" id='subTransDeny' onclick=" return (confirm('On selecting Temporary Cancellation This Ticket No. and VIRN No. Will be Accepted in LMS in Future.') && _subValid(this.id));" onkeypress="return (confirm('On selecting Temporary Cancellation This Ticket No. and VIRN No. Will be Accepted in LMS in Future.') && _subValid(this.id));" />
			 		</table>
			 		</td>
			    </tr>
             </table>
             
          </s:form> 

         

         </div>       

      </div>

     

     

     

   </body>




</HTML>
<code id="headId" style="visibility: hidden">
$RCSfile: agentPwtForPayDetails.jsp,v $
$Revision: 1.2 $
</code>