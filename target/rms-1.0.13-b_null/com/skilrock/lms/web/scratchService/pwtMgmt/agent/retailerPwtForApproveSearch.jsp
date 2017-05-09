<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">

<head>
	<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
	<link rel="stylesheet" href="<%=request.getContextPath() %>/LMSImages/css/styles.css"  type="text/css"/>
	<title><%=application.getAttribute("JSP_PAGE_TITLE")%></title>	
	<s:head theme="ajax" debug="false"/>	
	<script src="<%=request.getContextPath() %>/com/skilrock/lms/web/scratchService/pwtMgmt/agent/js/agt_pwtMgmt_pwtAgt.js" />
	<script>
	
	  
	
	</script>
	
</head>
  
   <body onload="getRetailerListForAgent('getRetailerList.action','retlist', 'RETAILER');">
      <%@include file="/com/skilrock/lms/web/loginMgmt/menu.jsp"%>
	 <div id="wrap">	  
         <div id="top_form">
           <h3>Approve Winning Requests</h3>
           <s:form id="topForm" action="searchPendingPwtForApproval.action">          
            <table border="0" cellpadding="2" cellspacing="0" width="300px">
             	   
             	   <tr><td  id="retDiv" align="right"></td><td align="left"><div  id="retlist"></div></td></tr>
             	  <!--  
             	   <tr>
						<td>
							
							<s:textfield label="Retailer Name" name="requestedById"></s:textfield>
						</td>
					</tr>
				  -->	
					<tr>
						<td>
							
							<s:textfield label="Request Id" name="requestId"></s:textfield>
						</td>
					</tr>
					
             	   <tr>
						<td>
							
							<s:textfield label="Player First Name" name="firstName"></s:textfield>
						</td>
					</tr>
					
					<tr>
						<td>
							
							<s:textfield label="Player Last Name" name="lastName"></s:textfield>
						</td>
					</tr>
					
					
					
					<tr>
						<td>						
							<s:select value="'Pending Winning'" cssClass="option" name="status" label="Status" list="{'REQUESTED'}"></s:select>
						</td>
					</tr>
					
					<tr>
					  <td></td>
					  <td>
					    <table >
					     <s:submit value="Search" cssClass="button"  theme="ajax" targets="serchResults" onclick="_id.non('serchResultsDetails')"></s:submit>					    
					    </table>
					  </td>
					</tr>
					
					
            </table>
          </s:form>         
         
         
         </div>
         <s:div id="serchResults" theme="ajax">     
         </s:div>
         
         
          <s:div id="serchResultsDetails" theme="ajax" cssStyle="display: none;">
           
           <h3>Winning Details</h3>
           
           <s:form cssStyle="background-color: white;border:0px">
            <s:hidden id="taskId" name="taskId"/>
            <s:hidden id="pwtAmount" name="pwtAmount"/>
            <s:hidden id="requestedById" name="ratailerId"/>
            <s:hidden id="ticketNbr" name="ticketNbr"/>
            <s:hidden id="virnNbr" name="virnNbr"/>
            <s:hidden id="gameId" name="gameId"/>
            <s:hidden id="gameNbr" name="gameNbr"/>
           <table border="1" cellpadding="2" cellspacing="0" width="400" bordercolor="#cccccc" align="center">
              <tr>
                <td class="label" align="right">Request id</td>
                <td id="req_id" class="data"></td> 
              </tr>
              
               <tr>
                <td class="label" align="right">Request Status</td>
                <td id="req_status" class="data"></td> 
              </tr>
              
               <tr>
                <td class="label" align="right">Party Name</td>
                <td id="first_name" class="data"></td> 
              </tr>
              <tr>
              	<td class="label" align="right">Ticket</td>
              	<td id="ticket_nbr" class="data"></td> 
              </tr>              
	          
	          <tr>
		          <td class="label" align="right">Virn</td>
		          <td id="virn_nbr" class="data"></td>
	          </tr> 
               
               <tr>
                <td class="label" align="right">Winning Amount</td>
                <td id="pwt_amt" class="data"></td> 
              </tr>
               <tr>
                <td class="label" align="right">Retailet Name</td>
                <td id="ret_name" class="data"></td> 
              </tr>
              
              <tr>
                <td colspan="2" align="center">
	               	 <input type="button" onclick="approveRequest()" value="Approve Request" id="subTrans" class="button"/>
	               	 <input type="button" onclick="denyRequest()" value="Deny Request" id="subTransDeny" class="button"/>                  
                </td>
              </tr>
              
           </table>
           </s:form>     
         </s:div>
         
      </div>
     
     
     
   </body>



</HTML>
<code id="headId" style="visibility: hidden">
$RCSfile: retailerPwtForApproveSearch.jsp,v $
$Revision: 1.2 $
</code>