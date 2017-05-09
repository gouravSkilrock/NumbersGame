<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>	
	<link rel="stylesheet" href="<%=request.getContextPath() %>/LMSImages/css/styles.css"  type="text/css"/>
	<title><%=application.getAttribute("JSP_PAGE_TITLE")%></title>
	<script>var projectName="<%=request.getContextPath() %>"</script>
	<script  src="<%=request.getContextPath() %>/com/skilrock/lms/web/scratchService/pwtMgmt/backOffice/js/pwtBO.js" />	
	<s:head theme="ajax" debug="false"/>
	
			
</head>
 
   <body>
      <%@include file="/com/skilrock/lms/web/loginMgmt/menu.jsp"%>
	 <div id="wrap">
         <div id="top_form">
         	<h3><s:text name="label.aprov.req.pwt"/>'s</h3>
           <s:form id="topForm" >
            <table border="0" cellpadding="2" cellspacing="0" width="400">
             	   
             	              	   
             	   <tr>
						<td>						
							<s:select  cssClass="option" name="requesterType" id="Org_type" key="label.req.by.type" list="{'BO'}" ></s:select>
						</td>
					</tr>
             	   
             	   <tr><td align="left" style="float: left" id="agentDiv"></td><td align="left"><div align="left" style="float: left" id="agentlist"></div></td></tr>
		           <tr><td align="left"  id="retDiv"></td><td align="left"><div align="left" style="float: left" id="retlist"></div></td></tr>
             	   <tr>
						<td>						
							<s:select  cssClass="option" name="partyType" id="party_Type" key="label.req.for.type" list="#{'PLAYER':getText('PLAYER'),'AGENT':getText('Agent'),'RETAILER':getText('Retailer')}" ></s:select>
						</td>
					</tr>
             	   		
					<tr>
						<td>
							
							<s:textfield key="label.rqst.id" name="requestId"></s:textfield>
						</td>
					</tr>
					
             	   <tr>
						<td>
							
							<s:textfield key="label.plyr.fname" name="firstName"></s:textfield>
						</td>
					</tr>
					
					<tr>
						<td>
							
							<s:textfield key="label.plyr.lname" name="lastName"></s:textfield>
						</td>
					</tr>
					
					
					
					<tr>
						<td>						
							<s:select value="'Pending PWT'" cssClass="option" name="status" key="label.status" list="{'PND_MAS'}"></s:select>
						</td>
					</tr>
					
					<tr>
					<td></td>
					  <td>
					    <table>
					       <s:submit key="btn.srch.draw" cssClass="button" id="drawSubmit" theme="ajax" targets="serchResults" action="draw_getPendingRequestForMasterApproval"></s:submit>  					    
					    </table>
					  </td>
					</tr>					
            </table>
          </s:form>  
         
         <s:div id="serchResults" theme="ajax"> </s:div> 
         </div>
      </div>
     
   </body>



</HTML>
<code id="headId" style="visibility: hidden">
$RCSfile: BOPwtForMasApprovalSearch.jsp,v $
$Revision: 1.3 $
</code>