<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>	
	<s:head theme="ajax" debug="false"/>
	
	<link rel="stylesheet" href="<%=request.getContextPath() %>/LMSImages/css/styles.css"  type="text/css"/>
	<title><%=application.getAttribute("JSP_PAGE_TITLE")%></title>
	<script>var projectName="<%=request.getContextPath() %>"</script>
	
	<script  src="<%=request.getContextPath() %>/com/skilrock/lms/web/scratchService/pwtMgmt/backOffice/js/pwtBO.js" />	
	
	<script>
	
	</script>
	
</head>
 
   <body >
      <%@include file="/com/skilrock/lms/web/loginMgmt/menu.jsp"%>
	 <div id="wrap">
         <div id="top_form">
         <h3><s:text name="label.pay.pending.pwt's"/></h3>
           <s:form name="aaa" method="post">
            <table border="0" cellpadding="2" cellspacing="0" width="400">             	   
             	   
             	   
             	   <tr>
						<td>						
							<s:select  cssClass="option" name="gameType" id="gameType" key="label.game.type" list="{'Scratch'}" onchange="changeSubmitButton(this.value);"></s:select>
						</td>
					</tr>
             	   
             	   
             	   <tr>
						<td>						
							<s:select  cssClass="option" name="paymentPendingAt" id="Org_type" key="label.pymy.pndng.at" list="#{'BO':'BO','AGENT':#application.TIER_MAP.AGENT.toUpperCase(),'ALL':'ALL'}" onchange="agtURL('getAgent.action','agentList',this.value)"></s:select>
						</td>
					</tr><!--
             	   
             	   <tr><td><div id="agentDiv" ></div></td><td><div  id="agentlist"></div></td></tr>
		           
		           -->
		           
		           <tr  >
		<td><table style="display : none" id="agentDiv" ><tr>	<td  colspan="1" ><label class="label"><s:property value="#application.TIER_MAP.AGENT" />   <s:text name="label.org.name"/>: </label></td></tr></table></td>
		<td colspan="1"><table style="display : none" id="agentList" ><tr>
			
			<td colspan="1" align="left" >
	
				<s:select headerKey="-1"  headerValue="%{getText('label.please.select')}" list="{}" theme="simple" name="agtId" cssClass="option"  id="agent_org_name" ></s:select>
		 		
		 	</td>
		 	</tr></table></td>
	 	</tr>
		           
		           <tr>
						<td align="left">						
							<s:select cssClass="option" name="partyType"  key="label.party.type" list="#{'PLAYER':'PLAYER','AGENT':#application.TIER_MAP.AGENT.toUpperCase()}"></s:select>
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
							<s:select value="'Pending PWT'" cssClass="option" name="status" key="label.status" list="{'PND_PAY'}"></s:select>
						</td>
					</tr>
					
					<tr>
					<td></td>
					  <td>
					    <table>
					      <s:submit key="btn.srch"  cssClass="button" id="scratchSubmit" theme="ajax" targets="serchResults" action="getPendingPwtForPayBO"></s:submit>
					      <s:submit key="btn.srch.draw" cssClass="button" id="drawSubmit" theme="ajax" targets="serchResults" action="draw_getPendingPwtForPayBO" cssStyle="display: none;"></s:submit>					    					    
					    </table>
					  </td>
					</tr>
            </table>
          </s:form>
         
         
         <s:div id="serchResults" theme="ajax">     
         </s:div>
         
         </div>
      </div>
     
   </body>



</HTML>
<code id="headId" style="visibility: hidden">
$RCSfile: boPwtForpaySearch.jsp,v $
$Revision: 1.3 $
</code>