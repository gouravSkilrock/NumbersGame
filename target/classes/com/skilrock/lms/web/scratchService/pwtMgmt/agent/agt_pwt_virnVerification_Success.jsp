<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="com.skilrock.lms.common.utility.FormatNumber"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">

<head>



<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />

<link rel="stylesheet" href="<%=request.getContextPath() %>/LMSImages/css/styles.css"  type="text/css"/>

<title><%=application.getAttribute("JSP_PAGE_TITLE")%></title>

	</head>
<script>
function crtResTab(){

var resData = _id.o('resDiv').innerHTML.replace(/null/g,'Emp'),gmArr = resData.split('Nx*'),erCd = gmArr[gmArr.length-1].split('Msg');
var erCdMap = new Array(),gmNm;
var fT="";
for(var k=0;k<erCd.length-1;k++){
erCdMap[erCd[k].split('-')[0]]=erCd[k].split('-')[1];
}
for (var i=0,l=gmArr.length-1;i<l;i++){
var tktDet = gmArr[i].split(':');gmNm=tktDet[0],_tT=tktDet.length-2,_vT=0,_iT=0,_tP=0,_eR="";
if(i%2==0){_bC="#f4fdf9";_cls="light_border_green";_tR="tktResultGr"}else{_bC="#fdf3f3";_cls="light_border_red";_tR="tktResultRd"}
var gmTab = '<table width="100%" border="0" cellspacing="1" cellpadding="3" bgcolor="#FFFFFF" class="'+_cls+'"><tr><th class="'+_tR+'">Virn Code</th><th class="'+_tR+'">Winning Amount</th><th class="'+_tR+'">Verification Status</th><th class="'+_tR+'">Message Code</th><th class="'+_tR+'">Message</th></tr>';
for(var j=1,m=tktDet.length-1;j<m;j++){
var _c=tktDet[j].split('*M*')[0],_m=tktDet[j].split('*M*')[1],_prz=tktDet[j].split('*M*')[2];
if(_prz=="Emp"){
_prz="No Prize";_eR="errRow";}else{_tP=_tP+parseInt(_prz);_eR="nonErrRow"}
_prz=="No Prize"?_iT++:_vT++;
gmTab=gmTab+'<tr bgcolor="'+_bC+'" class="'+_eR+'"><td>'+_c+'</td><td>'+_prz+'</td><td>'+erCdMap[_m].split(':')[1]+'</td><td>'+_m+'</td><td>'+erCdMap[_m].split(':')[0]+'</td></tr>';
}
var gmOuTab='<table width="100%" class="'+_cls+'" cellspacing="0" cellpadding="3"><tr><td colspan="4" height="20" bgcolor="'+_bC+'"><b>Game Name: </b>'+gmNm+'</td></tr><tr bgcolor="'+_bC+'"><td><b>Valid VIRN: </b>'+_vT+'</td><td><b>Total VIRN: </b>'+_tT+'</td></tr><tr bgcolor="'+_bC+'"><td><b>Invalid VIRN: </b>'+_iT+'</td><td><b>Total Prize Amount: </b>'+_tP+'</td></tr><tr><td colspan="2">';

gmOuTab=gmOuTab+gmTab+'</table></td></tr></table><br/>';

fT=fT+gmOuTab;
//alert(gmTab);
}
//alert(document.getElementById('crResDiv'));
_id.i('crResDiv',fT);
}
</script>
<body onload="crtResTab()">

<%@include file="/com/skilrock/lms/web/loginMgmt/menu.jsp"%>

<div id="wrap">

  <div id="top_form">
	
		

		
<h3>VIRN Validation for Winning Receipt</h3>
		<s:form>
		       <s:if test="%{#session.savePwtTicketsData.isSuccess}">
			   <tr><th colspan="7" align="center">The Winning of Rs: <s:set name="strAmount" value="%{#session.savePwtTicketsData.amount}" /><%=FormatNumber.formatNumberForJSP(pageContext.getAttribute("strAmount")) %> is valid and should be paid</th></tr>            
               </s:if>
		<div id="resDiv" style="display:none"><s:property value="#session.VERIFIED_VIRN_JSSTRING"/></div>
	
		<table width="100%" border="1" align="center" cellpadding="3" cellspacing="0" bordercolor="#CCCCCC" id="resTab"><tr><td><div id='crResDiv'></div></td></tr>
		</table>
		
		<!-- 
		    <table border="1" cellpadding="3" cellspacing="0" bordercolor="#CCCCCC" width="100%">                
               
               <s:if test="%{#session.savePwtTicketsData.isSuccess}">
			   <tr><th colspan="7" align="center">The Winning of Rs: <s:set name="strAmount" value="%{#session.savePwtTicketsData.amount}" /><%=FormatNumber.formatNumberForJSP(pageContext.getAttribute("strAmount")) %> is valid and should be paid</th></tr>            
               </s:if>
               <tr>
	               <th nowrap="nowrap">Game Name</th>
	               <th nowrap="nowrap">Retailer Name</th>
	               <th>Virn Code</th>
	               <th>Winning Amount</th>
	               <th>Verification Status</th>
	               <th>Message Code</th>
	               <th>Message</th>                 
               </tr>
			<s:iterator value="%{#session.savePwtTicketsData.gameVirnMap}" >
			   <s:iterator value="value" >
			
			   <tr>
			    
				    <s:if test="%{isValid}">			    				
				    	<td nowrap="nowrap"><s:property value="key" /></td>
				    	<td nowrap="nowrap"><s:property value="%{#session.savePwtTicketsData.retOrgName}" /></td>	   
					    <td><s:property  value="%{virnCode}"/></td>
					    <td><s:set name="strpwtAmount" value="%{pwtAmount}" /><%=FormatNumber.formatNumberForJSP(pageContext.getAttribute("strpwtAmount")) %></td>
					    <td><s:property   value="%{verificationStatus}"/></td>
					    <td><s:property   value="%{messageCode}"/></td>
					    <td><s:property   value="%{message}"/></td>				    			
				    </s:if>
			       <s:else>
				   <s:if test="%{!isHighLevel}">
				   		<td nowrap="nowrap"><font color="red"><s:property value="key" /></font></td>
				    	<td nowrap="nowrap"><font color="red"><s:property value="%{#session.savePwtTicketsData.retOrgName}" /></font></td>
				    	<td><font color="red"><s:property  value="%{virnCode}"/></font></td>
					    <td><font color="red"><s:label theme="simple" value="No Prize"/></font></td>
					    <td><font color="red"><s:property   value="%{verificationStatus}"/></font></td>
					    <td><font color="red"><s:property   value="%{messageCode}"/></font></td>
					    <td><font color="red"><s:property   value="%{message}"/></font></td>				    
					  </s:if>
					  <s:else>
					  	<td nowrap="nowrap"><font color="blue"><s:property value="key" /></font></td>
				    	<td nowrap="nowrap"><font color="blue"><s:property value="%{#session.savePwtTicketsData.retOrgName}" /></font></td>
				    	<td><font color="blue"><s:property  value="%{virnCode}"/></font></td>
					    <td><font color="blue"><s:label theme="simple" value="No Prize"/></font></td>
					    <td><font color="blue"><s:property   value="%{verificationStatus}"/></font></td>
					    <td><font color="blue"><s:property   value="%{messageCode}"/></font></td>
					    <td><font color="blue"><s:property   value="%{message}"/></font></td>
				     </s:else>
				   </s:else>		    
			</tr>     
			</s:iterator>
			</s:iterator>
        </table>
 		<br /><br /> -->
			<table border="0" cellpadding="3" cellspacing="0" bordercolor="#CCCCCC" width="350">
			<s:if test="%{#session.savePwtTicketsData.isSuccess}">
			<tr>
			<td>
			       <a href="<%=request.getContextPath() %>/com/skilrock/lms/web/reportsMgmt/rep_common_openPDF.action" target="_blank">Show Receipt</a>
			</td>
			</tr>
			</s:if>
			<tr>
			  <td><s:a theme="simple" href="agt_pwt_virnVerification_Menu.action">Enter More Winning</s:a></td>
			</tr>
			
		  </table>
		  <br />
                

		
		</s:form></div></div>

	</body>
</html>

<code id="headId" style="visibility: hidden">
$RCSfile: agt_pwt_virnVerification_Success.jsp,v $
$Revision: 1.2 $
</code>