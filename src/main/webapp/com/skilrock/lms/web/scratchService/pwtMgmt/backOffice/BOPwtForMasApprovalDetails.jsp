<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@page import="com.skilrock.lms.common.utility.FormatNumber"%>
<%@page import="com.skilrock.lms.common.utility.CommonMethods"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">


<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">


<head>

	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
	<link rel="stylesheet" href="<%=request.getContextPath() %>/LMSImages/css/styles.css"  type="text/css"/>
    <script>var projectName="<%=request.getContextPath() %>"</script>
    <script  src="<%=request.getContextPath() %>/com/skilrock/lms/web/common/globalJs/calender.js"></script>
    <link type="text/css" rel="stylesheet" href="<%=request.getContextPath() %>/LMSImages/css/lmsCalendar.css" media="screen"/>
	<title><%=application.getAttribute("JSP_PAGE_TITLE")%></title>	
	
   <script  src="<%=request.getContextPath() %>/com/skilrock/lms/web/scratchService/pwtMgmt/backOffice/js/pwtBO.js" />	
   		
	<s:head theme="ajax" debug="false"/>	


</head>
   <body>

      <%@include file="/com/skilrock/lms/web/loginMgmt/menu.jsp" %>

	 <div id="wrap">
         <div id="top_form">
           <s:form> 
               <!-- hidden fields like task id ,virn number etc. -->               
               <s:hidden name="taskId" value="%{plrPwtBean.task_id}"/>
                <s:hidden name="pwtAmount" value="%{plrPwtBean.pwt_amt}"/>
                <s:hidden name="ticketNbr" value="%{plrPwtBean.ticket_nbr}"/>
                <s:hidden name="virnNbr" value="%{plrPwtBean.virn_nbr}"/>
                <s:hidden name="gameId" value="%{plrPwtBean.game_id}"/>
                 <s:hidden name="gameNbr" value="%{plrPwtBean.game_nbr}"/>
               <s:hidden name="partyId" value="%{plrPwtBean.partyId}"/>
               <s:hidden name="partyType" value="%{plrPwtBean.partyType}"/>
               <s:hidden name="requesterType" value="%{plrPwtBean.requester_type}"/>
                <s:hidden name="requestedById" value="%{plrPwtBean.requested_by_org_id}"/>
           
     <table  border="1" cellpadding="2" cellspacing="0" width="450" align="center" bordercolor="#cccccc">
     <tr>
     <td align="center" class="bold_txt" colspan="2"><s:text name="label.plr.info"/></td>
     </tr>
	                 <tr>
	                     <td width="110" class="label" align="right"><s:text name="label.plr.name"/>:</td>
	                     <td  class="data"><s:property value="%{plrPwtBean.partyName}"/></td>
	                 </tr>                  
	                 <tr>
	                    <td class="label" align="right"><s:text name="label.party.type"/>:</td>
	                    <td  class="data"><s:property value="%{plrPwtBean.partyType}"/></td>
	                 </tr>                  
	                 <tr>
	                    <td class="label" align="right"><s:text name="label.party.city"/>:</td>
	                    <td  class="data"><s:property value="%{plrPwtBean.City}"/></td>
	                 </tr>
              </table><br />

              
          <table  border="1" cellpadding="2" cellspacing="0" width="450" align="center" bordercolor="#cccccc">
               <tr>
     <td align="center" class="bold_txt" colspan="2"><s:text name="label.pwt.info"/></td>
     </tr>               
	                 <tr>
	                    <td width="110" class="label" align="right"><s:text name="label.tckt.no"/>:</td>
	                    <td class="data"><s:property value="%{plrPwtBean.ticket_nbr}"/></td>
	                 </tr>                 
	                  <tr>
	                    <td class="label" align="right"><s:text name="label.virn.nbr"/>:</td>
	                    <td class="data"><s:property value="%{plrPwtBean.virn_nbr}"/></td>
	                 </tr>
	                 <tr>
	                    <td class="label" align="right"><s:text name="label.pwt.amt"/>:</td>
	                     <s:set name="pwtAmtFrmt" value="%{plrPwtBean.pwt_amt}"></s:set>
	                     <td class="data"><%=FormatNumber.formatNumberForJSP(pageContext.getAttribute("pwtAmtFrmt")) %></td>
	                    
	                 </tr> 
	               <!--  
	                 <tr>                   
	                    <s:textfield label="Comm Amount" name="taxAmt" id="tax"   value="%{plrPwtBean.comm_amt}" readonly="true"/>
	                 </tr>
	                  
	                 <tr>
	                       <s:textfield label="Net Amount" name="netAmt" id="netAmt"   value="%{plrPwtBean.net_amt}"/>
	                 </tr>
	                  -->  
              </table><br />
              
               
             <table  border="0" cellpadding="2" cellspacing="0" width="400" align="center"0>
	                <tr>
				   		
				   		<td>
					   		<table  border="0" cellpadding="2" cellspacing="0" width="400" align="center"0>
					   		    <div id="subCheck" style="position:absolute;"></div>
					   			<s:submit cssClass="button" theme="simple"  action="approvePendingReqByMasterBO" key="btn.approve" id="subTrans" />
					   			<input type="button" value="<s:text name="btn.deny"/>" class="button" onclick=" return showDenyFields();">
					 		</table>
				 		</td>
				    </tr>
             </table>
             
             
             <table id="denyfields" width="50%" border="1" cellspacing="0" align="center" cellpadding="3" style="display : none;">
			   			<tr>
			   				<% //'CANCELLED_PERMANENT':'Permanent Cancellation', 'UNCLM_CANCELLED':'Temporary Cancellation' %>
			   				<td colspan="2"><table><s:radio name="denyPwtStatus" title="On selecting 'Temporary Cancellation' This Ticket No. and VIRN No. Will be Accepted in LMS in Future." id="denyTempPwtStatusId" list="{'Temporary Cancellation'}"   onclick="return confirm('On selecting Temporary Cancellation This Ticket No. and VIRN No. Will be Accepted in LMS in Future.')" /></table></td>
			   			</tr>
			   			<tr>
			   				<% //'CANCELLED_PERMANENT':'Permanent Cancellation', 'UNCLM_CANCELLED':'Temporary Cancellation' %>
			   				<td colspan="2"><table><s:radio name="denyPwtStatus" title="On selecting 'Permanent Cancellation' This Ticket No. and VIRN No. Will Never be Accepted in LMS in Future." id="denyPwtStatusId" list="{'Permanent Cancellation'}"  onclick="return confirm('On selecting Permanent Cancellation This Ticket No. and VIRN No. Will Never be Accepted in LMS in Future.')"/></table></td>
			   			</tr>
			   			<tr><td align="right" colspan="2"><s:submit cssClass="button" action="denyPendingReqByMasterBO" onclick="return checkDenyPwtStatus()" key="btn.deny.req"/></td></tr>			   			
			</table>
             
          </s:form> 

         

         </div>       

      </div>

     

     

     

   </body>




</HTML>
<code id="headId" style="visibility: hidden">
$RCSfile: BOPwtForMasApprovalDetails.jsp,v $
$Revision: 1.3 $
</code>