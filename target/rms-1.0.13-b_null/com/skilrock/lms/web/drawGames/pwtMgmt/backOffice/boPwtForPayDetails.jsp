<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@page import="com.skilrock.lms.common.utility.FormatNumber"%>
<%@page import="com.skilrock.lms.common.utility.CommonMethods"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">


<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">


<head>

	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<link rel="stylesheet" href="<%=request.getContextPath() %>/LMSImages/css/styles.css"  type="text/css"/>
    <script>
     var projectName="<%=request.getContextPath() %>";
     function _subTrans(){
			return (  calculateNetAmt() && _subValid('subTrans'));
			}
    
    </script>
    <script  src="<%=request.getContextPath() %>/com/skilrock/lms/web/common/globalJs/calender.js"></script>
    <link type="text/css" rel="stylesheet" href="<%=request.getContextPath() %>/LMSImages/css/lmsCalendar.css" media="screen"/>
	<title><%=application.getAttribute("JSP_PAGE_TITLE")%></title>	
   	<script src="<%=request.getContextPath() %>/com/skilrock/lms/web/drawGames/pwtMgmt/backOffice/js/pwtBO.js" />	
   		
	<s:head theme="ajax" debug="false"/>	


</head>
   <body>

      <%@include file="/com/skilrock/lms/web/loginMgmt/menu.jsp" %>

	 <div id="wrap">
         <div id="top_form">
           <s:form name="playerDetails" > 
               <!-- hidden fields like task id ,virn number etc. -->               
               <s:hidden name="taskId" value="%{plrPwtBean.task_id}"/>
                <s:hidden name="pwtAmount" value="%{plrPwtBean.pwt_amt}"/>
                <s:hidden name="taxAmt" value="%{plrPwtBean.comm_amt}"/>
                <s:hidden name="ticketNbr" value="%{plrPwtBean.ticket_nbr}"/>
                <s:hidden name="dispTicketNbr" value="%{plrPwtBean.displayTktNbr}"/>
                <s:hidden name="drawId" value="%{plrPwtBean.drawId}"/>
                <s:hidden name="panelId" value="%{plrPwtBean.panelId}"/>
                <s:hidden name="gameId" value="%{plrPwtBean.game_id}"/>
                 <s:hidden name="gameNbr" value="%{plrPwtBean.game_nbr}"/>
               <s:hidden name="partyId" value="%{plrPwtBean.partyId}"/>
               <s:hidden name="partyType" value="%{plrPwtBean.partyType}"/>
             
           
              <table  border="1" cellpadding="4" cellspacing="0" width="450" bordercolor="#cccccc" align="center">
               <tr>
              <td align="center" class="bold_txt" colspan="3" height="20"><s:text name="label.plr.info" /></td>
              </tr>
	                 <tr>
	                     <td width="110" class="label" align="right"><s:text name="label.plr.name" />:</td>
	                     <td class="data"><s:property value="%{plrPwtBean.partyName}"/></td>
	                 </tr>                  
	                 <tr>
	                    <td class="label" align="right"><s:text name="label.party.type" />:</td>
	                    <td  class="data"><s:property value="%{plrPwtBean.partyType}"/></td>
	                 </tr>                  
	                 <tr>
	                    <td class="label" align="right"><s:text name="label.party.city" />:</td>
	                    <td  class="data"><s:property value="%{plrPwtBean.City}"/></td>
	                 </tr>
              </table><br />

              

             
              <table  border="1" cellpadding="4" cellspacing="0" width="450" bordercolor="#cccccc" align="center"> 
               <tr>
              <td align="center" class="bold_txt" colspan="3" height="20"><s:text name="label.pwt.info" /></td>
              </tr>                
	                 <tr>
	                    <td width="110" class="label" align="right"><s:text name="label.tckt.no" />:</td>
	                    <td class="data"><s:property value="%{plrPwtBean.displayTktNbr}"/></td>
	                 </tr>                 
	                <!--    <tr>
	                    <td class="label" align="right">Draw Id:</td>
	                    <td class="data"><s:property value="%{plrPwtBean.drawId}"/></td>
	                 </tr>
	                
	                 <tr>
	                    <td class="label" align="right">Panel Id:</td>
	                    <td class="data"><s:property value="%{plrPwtBean.panelId}"/></td>
	                 </tr>
	               	 -->
	                 <tr>
	                    <td class="label" align="right"><s:text name="label.pwt.amt" />:</td>
	                    <td class="data"><s:set name="pwtAmt" value="%{plrPwtBean.pwt_amt}"/><%=FormatNumber.formatNumberForJSP(pageContext.getAttribute("pwtAmt")) %></td>
	                 </tr> 
	                 
	                 <tr>    				
                 		<td colspan="2"><div style="color: red;" id="taxError"></div></td>
                 	</tr>
                 
	                 <tr>
	                 <td align="right"><s:text name="label.comsn.amt" /></td>
	                 <td align="left">              
	                   <s:textfield label="Comm Amount" theme="simple" name="tax" id="tax" readonly="true"  value="%{plrPwtBean.comm_amt}" onblur="return calculateNetAmt()"/>
	              </td></tr> 
	                 <tr>
	                 <td align="right"><s:text name="label.net.amt" /></td>
	                 <td>
	                 <s:set name="netAmt" value="%{plrPwtBean.net_amt}"/><%String nAmt=FormatNumber.formatNumberForJSP(pageContext.getAttribute("netAmt")).replaceAll(",",""); %>
	              <s:textfield label="Net Amount" theme="simple" name="netAmt" id="netAmt"   value="<%=nAmt%>"/>
	                 </td></tr>  
              </table><br />              

              
         <table  border="1" cellpadding="4" cellspacing="0" width="450" bordercolor="#cccccc" align="center">
         <tr>
              <td align="center" class="bold_txt" colspan="3" height="20"><s:text name="label.pay.info" /></td>
              </tr> 
	                 <tr>
		                   <td width="110" class="label" align="right"><s:text name="label.pay.type" />:</td>
		                   <td class="data">
			                   <s:if test="%{plrPwtBean.partyType=='PLAYER'}">
				                   <input type="radio" value="Cash" checked="checked" name="paymentType" onclick="document.getElementById('chqDiv').style.display='none';document.getElementById('tptDiv').style.display='none';"/><s:text name="CASH" /> 
				                   <input type="radio" value="Cheque" name="paymentType" onclick="document.getElementById('chqDiv').style.display='block';document.getElementById('tptDiv').style.display='none';"/><s:text name="CHEQUE" /> 
				                   <s:if test="%{plrPwtBean.bankActNbr==null ||plrPwtBean.bankActNbr.equals('')}">
				                   	<input type="radio" disabled="disabled"  title="Account Details are not Available So You can not make third party" value="TPT" name="paymentType" onclick="document.getElementById('tptDiv').style.display='block';document.getElementById('chqDiv').style.display='none';"/><s:text name="TPT" />
		                           </s:if>
		                             <s:else>
		                               <input type="radio"  value="<s:text name="TPT" />" name="paymentType" onclick="document.getElementById('tptDiv').style.display='block';document.getElementById('chqDiv').style.display='none';"/><s:text name="TPT" />
		                             </s:else>
		                       </s:if>
		                       <s:else>
		                            <input type="radio"  value="Cash" name="paymentType" onclick="document.getElementById('chqDiv').style.display='none';document.getElementById('tptDiv').style.display='none';"/><s:text name="CASH" />
		                       </s:else>
		                   </td>
	                 </tr>
	                 <tr>                    
	                    <td colspan="2">
			                <div id="chqDiv" style="display: none;text-align: center" align="center">
				                 <table border="0" cellpadding="3" cellspacing="0" width="100%">
					                  <tr>
			                  			<td colspan="2">
			                  			 <div id="chequeNbrError"></div>
			                  			</td>
			                  		 </tr>
					                  <tr>
					                    <td class="label" width="26%" align="right"><s:text name="label.chq.nbr" />:</td>
					                    <td class="data" align="left"><input type="text" size="10" maxlength="6" name="chqNbr"/></td>
					                  </tr>
					                  <tr>
						                    <td class="label" align="right"><s:text name="label.chq.date" />:</td>
						                    <td align="left"> 
							                   <table align="left">							                     
													<%
													String endDate = CommonMethods.convertDateInGlobalFormat((String)session.getAttribute("presentDate"), "yyyy-mm-dd", "dd-MM-yyyy");
													%>								 					                     
					     			             												 
							    				 <input  type="text" name="chequeDate" id="chequeDate" value="<%=endDate%>" readonly="readonly" size="12"/>
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
					                    <td class="label" align="right"><s:text name="label.drawee.bank" />:</td>
					                    <td class="data" align="left"><input type="text" size="15" maxlength="45" name="draweeBank"/></td>
					                  </tr>
					                   <tr>
					                    <td class="label" align="right"><s:text name="label.isue.party" />:</td>
					                    <td class="data" align="left"><table align="left" cellpadding="0" cellspacing="0"><s:textfield readonly="true"  name="issuiningParty" cssStyle="float:left" value="%{#session.USER_INFO.orgName}"/></table></td>
					                  </tr>
				                 </table> 
			               </div>
			               
			               <div id="tptDiv" style="display: none; text-align: center" align="center">
				                 <table border="0" cellpadding="3" cellspacing="0" width="100%" align="left">
					                  <tr>
					                    <td class="label" width="26%" align="right"><s:text name="label.acc.no" />:</td>
					                    <td class="data" align="left"><s:property value="%{plrPwtBean.bankActNbr}"/></td>
					                  </tr>
					                  <tr>
					                    <td class="label" align="right"><s:text name="label.bnk.name" />:</td>
					                    <td class="data" align="left"><s:property value="%{plrPwtBean.bankName}"/></td>
					                  </tr>
					                  <tr>
					                    <td class="label" align="right"><s:text name="label.bnk.brnch" />:</td>
					                    <td class="data" align="left"><s:property value="%{plrPwtBean.bankBranch}"/></td>
					                  </tr>
					                   <tr>
					                    <td class="label" align="right"><s:text name="label.bnk.city" />:</td>
					                    <td class="data" align="left"><s:property value="%{plrPwtBean.bankCity}"/></td>
					                  </tr>
				                 </table> 
			               </div>
			               
	                     </td>
	                  </tr>
               </table>        
             
                    <br/>
               
                <table border="0" cellpadding="4" cellspacing="0" width="400" bordercolor="#cccccc" align="center">
                <tr>			   		
			   		<td align="right">
			   		<table>
			   		    <div id="subCheck" style="position:absolute;"></div>
			   			<s:submit theme="simple" cssClass="button"  action="draw_payPendingPwtsBO" key="btn.submit" id="subTrans"  onclick="return _subTrans();"/>
			   			<input type="button" value="<s:text name="btn.deny" />" class="button" onclick=" return showDenyFields();"></input>
			 		</table>
			 		</td>
			    </tr>
             </table>
             
             
             <table id="denyfields" width="50%" border="1" cellspacing="0" align="center" cellpadding="3" style="display : none;border-color: #CCCCCC">
			   			<tr>
			   				<% //'CANCELLED_PERMANENT':'Permanent Cancellation', 'UNCLM_CANCELLED':'Temporary Cancellation' %>
			   				<td colspan="2"><table><s:radio name="denyPwtStatus" title="On selecting 'Temporary Cancellation' This Ticket No. Will be Accepted in LMS in Future." id="denyTempPwtStatusId" list="{'Temporary Cancellation'}"   onclick="return confirm('On selecting Temporary Cancellation This Ticket No. Will be Accepted in LMS in Future.')" /></table></td>
			   			</tr>
			   			<tr>
			   				<% //'CANCELLED_PERMANENT':'Permanent Cancellation', 'UNCLM_CANCELLED':'Temporary Cancellation' %>
			   				<td colspan="2"><table><s:radio name="denyPwtStatus" title="On selecting 'Permanent Cancellation' This Ticket No. Will Never be Accepted in LMS in Future." id="denyPwtStatusId" list="{'Permanent Cancellation'}"  onclick="return confirm('On selecting Permanent Cancellation This Ticket No. Will Never be Accepted in LMS in Future.')"/></table></td>
			   			</tr>
			   			<tr><td align="right" colspan="2"><s:submit cssStyle="button" action="draw_denyPendingPwtsBO" id="subTransDeny" onclick="return (checkDenyPwtStatus() &&  _subValid(this.id))" key="btn.deny.req"/></td></tr>			   			
			</table>
             
          </s:form> 

         

         </div>       

      </div>

     

     

     

   </body>




</HTML>
<code id="headId" style="visibility: hidden">
$RCSfile: boPwtForPayDetails.jsp,v $
$Revision: 1.3 $
</code>