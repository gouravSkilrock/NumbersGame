<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">

<head>
	<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
	<link rel="stylesheet" href="<%=request.getContextPath() %>/LMSImages/css/styles.css"  type="text/css"/>
	<title><%=application.getAttribute("JSP_PAGE_TITLE")%></title>	
	<script src="<%=request.getContextPath() %>/com/skilrock/lms/web/scratchService/pwtMgmt/agent/js/agt_pwtMgmt_pwtAgt.js" />
	<s:head theme="ajax" debug="false"/>
	
</head>
  
   <body >
      <%@include file="/com/skilrock/lms/web/loginMgmt/menu.jsp" %>
	 <div id="wrap">
         <div id="top_form">
         	<h3>Pay Pending PWT's</h3>
           <s:form id="topForm" action="getPendingPwtForPay.action">
            <table border="0" cellpadding="2" cellspacing="0" width="400">
             	   
             	  
             	   <tr>
			        <td colspan="1"><label class="label">Payment To<span class="required"></span>:</label></td>
			        <td ><table><s:select name="partyType" id="party_type" list="#{'PLAYER':'PLAYER','RETAILER':#application.TIER_MAP.RETAILER.toUpperCase()}" onchange="getRetailerListForAgent('getRetailerList.action','retlist', this.value);" cssClass="option"/></table></td>
		           </tr>
		           
		             <tr><td id="retDiv"></td><td><div  id="retlist"></div></td></tr>
		      	   
					
					<tr>
						<td align="right">						
							<s:textfield cssClass="label" label="Request Id" name="requestId"></s:textfield>
						</td>
					</tr>
					
             	   <tr>
						<td align="right">						
							<s:textfield label="Player First Name" name="firstName"></s:textfield>
						</td>
					</tr>
					
					<tr>
						<td align="right">							
							<s:textfield label="Player Last Name" name="lastName"></s:textfield>
						</td>
					</tr>
					
					
					
					<tr>
						<td>						
							<s:select value="'Pending PWT'" cssClass="option" name="status" label="Status" list="{'PND_PAY'}"></s:select>
						</td>
					</tr>
					
					<tr>
					  <td></td>	
					  <td>
					    <table>
					     <s:submit value="Search" cssClass="button"  theme="ajax" targets="serchResults"></s:submit>					    
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
$RCSfile: agentPwtForpaySearch_dg.jsp,v $
$Revision: 1.2 $
</code>