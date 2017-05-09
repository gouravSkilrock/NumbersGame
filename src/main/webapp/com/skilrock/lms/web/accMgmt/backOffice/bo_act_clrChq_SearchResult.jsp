<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="com.skilrock.lms.common.utility.FormatNumber"%>
<%@page import="com.skilrock.lms.common.utility.CommonMethods"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">

<head>

<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
<script type="text/javascript" src="<%=request.getContextPath() %>/com/skilrock/lms/web/accMgmt/backOffice/js/clearCheqeValidation.js"></script>
<link rel="stylesheet" href="<%=request.getContextPath() %>/LMSImages/css/styles.css" type="text/css"/>

<title><%=application.getAttribute("JSP_PAGE_TITLE")%></title>
	
<s:head theme="ajax" debug="false"/>

	</head>
	<body>
	<div id="wrap">

  <div id="top_form">
  <s:if test="%{pendingChqList.size>0}">
  <s:form action="bo_act_clrChq_Verify" name="chequeForm"  onsubmit="return verifyData()">

  <table border="1" cellpadding="3" cellspacing="0">
  
    <tr style="background: #eee">
    <s:if test="%{chqStatus.equals('PENDING')}">
      <th> </th>
     </s:if> 
     <th><s:property value="#application.TIER_MAP.AGENT" /> Name</th>
      <th>Cheque Nbr</th>
       <th>Cheque Amount</th>
        <th>Cheque Date</th>
         <th>Issuing Party</th>
          <th>Drawee Bank</th>
           <th>Status</th>
           <s:if test="%{chqStatus.equals('PENDING')}">
	            <th>Cheque to</th>
	             <th>Bounce Charges</th>
	             <th>Clearance Date(mm/dd/yyyy)</th>
           </s:if>   
    
    </tr>
  
    <tr><td colspan="11"><div id="errorDiv"></div></td></tr>
    <tr><td colspan="11"><div id="errorCommAmtDiv"></div></td></tr>
  
  <s:iterator value="%{pendingChqList}" status="rowIndex">
  <tr id="r<s:property value="#rowIndex.index"/>" >
    
    <s:if test="%{chqStatus.equals('PENDING')}">
	    <td>
	      <input type="checkbox" id="chkBox<s:property value="#rowIndex.index"/>" onclick="disableRow('agentName<s:property value="#rowIndex.index"/>','chkBox<s:property value="#rowIndex.index"/>','agtOrgName<s:property value="#rowIndex.index"/>','chkNo<s:property value="#rowIndex.index"/>','chkAmt<s:property value="#rowIndex.index"/>','chkDate<s:property value="#rowIndex.index"/>','issPrtyName<s:property value="#rowIndex.index"/>','chkBank<s:property value="#rowIndex.index"/>','chkStatus<s:property value="#rowIndex.index"/>','commAmt<s:property value="#rowIndex.index"/>','r<s:property value="#rowIndex.index"/>','isClrYes<s:property value="#rowIndex.index"/>','isClrNO<s:property value="#rowIndex.index"/>','isCleared<s:property value="#rowIndex.index"/>','dateCheque<s:property value="#rowIndex.index"/>','dateChequeButton<s:property value="#rowIndex.index"/>')"/>
	    </td>
    </s:if>
    
    <td>
      <s:property value="%{agtOrgName}"/>
      <input  type="hidden" value="<s:property value="%{agtOrgId}"/>"  disabled="disabled"   size="15" name="agtOrgName" id="agtOrgName<s:property value="#rowIndex.index"/>"/>
      <input  type="hidden" value="<s:property value="%{agtOrgName}"/>"  disabled="disabled"  name="agentName" id="agentName<s:property value="#rowIndex.index"/>"/> 
     </td>
    
     <td>
      <s:property value="%{chequeNbr}"/>
       <input  type="hidden" value="<s:property value="%{chequeNbr}"/>:<s:property value="%{taskId}"/>"   disabled="disabled" readonly="true" size="8" name="chequeNbr" id="chkNo<s:property value="#rowIndex.index"/>"/>
     </td>
     <s:set name="strChequeAmount" value="%{chequeAmt}" />
     
     <td>
      <%=FormatNumber.formatNumberForJSP(pageContext.getAttribute("strChequeAmount"))%>
       <input type="hidden" value="<%=FormatNumber.formatNumberForJSP(pageContext.getAttribute("strChequeAmount")) %>"  disabled="disabled" readonly="true" size="15" name="chequeAmt" id="chkAmt<s:property value="#rowIndex.index"/>"/>
      </td>
     
      <td>
      <s:property value="%{chequeDate}"/>
      <input type="hidden" value="<s:property value="%{chequeDate}"/>" disabled="disabled" readonly="true" size="11" name="chequeDate" id="chkDate<s:property value="#rowIndex.index"/>"/>
       <s:set name="clearDateStart" value="%{chequeDate}"/> 
       
     </td>
     
     <td>
     <s:property value="%{issuingPartyName}"/>
       <input type="hidden" value="<s:property value="%{issuingPartyName}"/>"  disabled="disabled" readonly="true" size="15" name="issuingPartyName" id="issPrtyName<s:property value="#rowIndex.index"/>"/>
       
     </td>
     
       <td>
       <s:property value="%{draweeBank}"/>
       <input type="hidden" value="<s:property value="%{draweeBank}"/>"  disabled="disabled" readonly="true" size="15" name="draweeBank" id="chkBank<s:property value="#rowIndex.index"/>"/>
      
     </td> 
     <td>
     <s:property value="%{chequeStatus}"/>
     <input type="hidden" value="<s:property value="%{chequeStatus}"/>"  disabled="disabled" readonly="true" size="8" name="chequeStatus"  id="chkStatus<s:property value="#rowIndex.index"/>"/>
       
     </td>   
     
     <s:if test="%{chequeStatus.equals('PENDING')}">
     
		     <td width="60">
		       <input type="hidden" name="isCleared" disabled="true" id='isCleared<s:property value="#rowIndex.index"/>'/>
		       <input type="radio"  name="isCleared<s:property value="#rowIndex.index"/>" id='isClrYes<s:property value="#rowIndex.index"/>'  value="Cleared" onclick="enableRow('agentName<s:property value="#rowIndex.index"/>','chkBox<s:property value="#rowIndex.index"/>','agtOrgName<s:property value="#rowIndex.index"/>','chkNo<s:property value="#rowIndex.index"/>','chkAmt<s:property value="#rowIndex.index"/>','chkDate<s:property value="#rowIndex.index"/>','issPrtyName<s:property value="#rowIndex.index"/>','chkBank<s:property value="#rowIndex.index"/>','chkStatus<s:property value="#rowIndex.index"/>','isClrYes<s:property value="#rowIndex.index"/>','commAmt<s:property value="#rowIndex.index"/>','r<s:property value="#rowIndex.index"/>','isCleared<s:property value="#rowIndex.index"/>','dateCheque<s:property value="#rowIndex.index"/>','dateChequeButton<s:property value="#rowIndex.index"/>')"/>Clear<br/>
		       <input type="radio" name="isCleared<s:property value="#rowIndex.index"/>" id='isClrNO<s:property value="#rowIndex.index"/>'  value="Cancel"  onclick="enableRow('agentName<s:property value="#rowIndex.index"/>','chkBox<s:property value="#rowIndex.index"/>','agtOrgName<s:property value="#rowIndex.index"/>','chkNo<s:property value="#rowIndex.index"/>','chkAmt<s:property value="#rowIndex.index"/>','chkDate<s:property value="#rowIndex.index"/>','issPrtyName<s:property value="#rowIndex.index"/>','chkBank<s:property value="#rowIndex.index"/>','chkStatus<s:property value="#rowIndex.index"/>','isClrNO<s:property value="#rowIndex.index"/>','commAmt<s:property value="#rowIndex.index"/>','r<s:property value="#rowIndex.index"/>','isCleared<s:property value="#rowIndex.index"/>','dateCheque<s:property value="#rowIndex.index"/>','dateChequeButton<s:property value="#rowIndex.index"/>')"/>Cancel
		     </td>
		     
		     <td>
		     <input type="text" value="0.00"  disabled="disabled" readonly="true" size="5" maxlength="6" name="commAmt" id="commAmt<s:property value="#rowIndex.index"/>" />
		       
		     </td>
		    
     	       		
       		<%				
								String endDate =CommonMethods.convertDateInGlobalFormat((String)session.getAttribute("presentDate"), "yyyy-mm-dd", "mm/dd/yyyy");
								String startDate = CommonMethods.convertDateInGlobalFormat(String.valueOf(pageContext.getAttribute("clearDateStart")), "yyyy-mm-dd", "mm/dd/yyyy");
			%>
       		<td>
       		
       		  <input  type="text" name="clearanceDate" id="dateCheque<s:property value="%{#rowIndex.index}"/>" value="<%=endDate %>"  size="11" readonly="readonly" disabled="disabled"/>
    		  <input type="button" id="dateChequeButton<s:property value="%{#rowIndex.index}"/>" style=" width:19px; height: 19px; background: url('<%=request.getContextPath() %>/LMSImages/imagesCal/dateIcon.gif'); top left; border:0 ; " onclick="displayCalendar(document.getElementById('dateCheque<s:property value="%{#rowIndex.index}"/>'),'mm/dd/yyyy', this, '<%=endDate%>', '<%=startDate %>', '<%=endDate%>')" disabled="disabled"/>
   						
       		
       		</td>
       		
       		
       		
     </s:if>
     
  </tr>
     
  
  
  
  
  
  
  
  </s:iterator>
 <s:hidden name="totalIndex" value="%{pendingChqList.size()}"/>
  </table>
  
	   <s:if test="%{chqStatus.equals('PENDING')}">
	  		<s:submit value="Submit" cssClass="button"></s:submit>
	  </s:if>
  </s:form>
 </s:if>
 <s:else>
 
 No records found
 </s:else>
  
  </div></div>
	</body>
	</html>
<code id="headId" style="visibility: hidden">
$RCSfile: bo_act_clrChq_SearchResult.jsp,v $
$Revision: 1.2 $
</code>